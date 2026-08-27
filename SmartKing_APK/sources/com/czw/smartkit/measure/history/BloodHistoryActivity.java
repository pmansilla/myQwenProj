package com.czw.smartkit.measure.history;

import android.view.View;
import butterknife.BindView;
import com.amap.location.common.model.AmapLoc;
import com.czw.smartkit.R;
import com.czw.smartkit.databaseModule.blood.BloodDataTable;
import com.czw.smartkit.databaseModule.blood.BloodServiceImpl;
import com.czw.smartkit.measure.ComAdapter;
import com.czw.smartkit.user.UserUtil;
import com.czw.smartkit.views.multiColumnView.MultiColumnBean;
import com.czw.smartkit.views.multiColumnView.MultiColumnView;
import com.czw.utils.LogUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.panpf.sketch.uri.FileUriModel;

/* loaded from: classes.dex */
public class BloodHistoryActivity extends BaseHistoryActivity {
    private ArrayList<BloodDataTable> bloodDataTables = new ArrayList<>();

    @BindView(R.id.blood_history_multiColumnView)
    MultiColumnView multiColumnView;

    private void updateShow(final List<BloodDataTable> list) {
        final ArrayList arrayList = new ArrayList();
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.measure.history.BloodHistoryActivity.2
            @Override // java.lang.Runnable
            public void run() {
                if (list.size() == 0 || list == null) {
                    BloodHistoryActivity.this.emptyLayout.setVisibility(0);
                    BloodHistoryActivity.this.listView.setVisibility(8);
                } else {
                    BloodHistoryActivity.this.emptyLayout.setVisibility(8);
                    BloodHistoryActivity.this.listView.setVisibility(0);
                    BloodHistoryActivity.this.bloodDataTables.clear();
                    int size = list.size();
                    for (int i = 0; i < size; i++) {
                        BloodDataTable bloodDataTable = (BloodDataTable) list.get(i);
                        if (!bloodDataTable.getBpH().equals(AmapLoc.RESULT_TYPE_GPS) && !bloodDataTable.getBpL().equals(AmapLoc.RESULT_TYPE_GPS)) {
                            BloodHistoryActivity.this.bloodDataTables.add(bloodDataTable);
                            String dateTimeStr = bloodDataTable.getDateTimeStr();
                            if (dateTimeStr.length() > 10) {
                                dateTimeStr = dateTimeStr.substring(10);
                            }
                            MultiColumnBean multiColumnBean = new MultiColumnBean(dateTimeStr);
                            ArrayList arrayList2 = new ArrayList();
                            arrayList2.add(Float.valueOf(bloodDataTable.getBpH()));
                            arrayList2.add(Float.valueOf(bloodDataTable.getBpL()));
                            multiColumnBean.setValueList(arrayList2);
                            ArrayList arrayList3 = new ArrayList();
                            arrayList3.add(-2069737);
                            arrayList3.add(-1534891);
                            multiColumnBean.setColorList(arrayList3);
                            arrayList.add(multiColumnBean);
                        }
                    }
                    BloodHistoryActivity.this.adapter.notifyDataSetChanged();
                }
                Collections.reverse(arrayList);
                BloodHistoryActivity.this.multiColumnView.updateData(arrayList);
            }
        });
    }

    @Override // com.czw.smartkit.measure.history.BaseHistoryActivity
    protected ComAdapter getAdapter() {
        return new ComAdapter<BloodDataTable>(this, this.bloodDataTables) { // from class: com.czw.smartkit.measure.history.BloodHistoryActivity.1
            @Override // com.czw.smartkit.modes.adapter.BaseListAdapter
            public void handDataAndView(ComAdapter.ItemTag itemTag, BloodDataTable bloodDataTable, int i) {
                itemTag.getTimeTv().setText(bloodDataTable.getDateTimeStr().substring(10));
                itemTag.getValueTv().setText(bloodDataTable.getBpH() + FileUriModel.SCHEME + bloodDataTable.getBpL() + "mmHg");
            }
        };
    }

    @Override // com.czw.smartkit.measure.history.BaseHistoryActivity, com.czw.smartkit.base.BaseActivity
    public void initView() {
        super.initView();
        this.titleBar.setTitle(R.string.title_blood_histoey);
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public int loadLayout() {
        return R.layout.ui_history_blood;
    }

    @Override // com.czw.smartkit.measure.history.BaseHistoryActivity, android.view.View.OnClickListener
    public /* bridge */ /* synthetic */ void onClick(View view) {
        super.onClick(view);
    }

    @Override // com.czw.smartkit.measure.history.BaseHistoryActivity
    public void onDayChoice(String str) {
        super.onDayChoice(str);
        LogUtil.e("denug==>" + str);
        queryBlood(str);
    }

    void queryBlood(String str) {
        updateShow(BloodServiceImpl.getInstance().getDataByDesc(UserUtil.getUid(), str));
    }
}
