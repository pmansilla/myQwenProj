package com.czw.smartkit.measure.history;

import android.text.TextUtils;
import android.view.View;
import butterknife.BindView;
import com.amap.location.common.model.AmapLoc;
import com.czw.smartkit.R;
import com.czw.smartkit.databaseModule.ox.OxDataTable;
import com.czw.smartkit.databaseModule.ox.OxServiceImpl;
import com.czw.smartkit.measure.ComAdapter;
import com.czw.smartkit.user.UserUtil;
import com.czw.smartkit.views.lineview.LineDataBean;
import com.czw.smartkit.views.lineview.LineShowView;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OxHistoryActivity extends BaseHistoryActivity {

    @BindView(R.id.ox_history_lineView)
    LineShowView lineView;
    private ArrayList<OxDataTable> oxDTOArrayList = new ArrayList<>();

    private void updateShow(final List<OxDataTable> list) {
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.measure.history.OxHistoryActivity.2
            @Override // java.lang.Runnable
            public void run() {
                OxHistoryActivity.this.oxDTOArrayList.clear();
                if (list.size() == 0 || list == null) {
                    OxHistoryActivity.this.emptyLayout.setVisibility(0);
                    OxHistoryActivity.this.listView.setVisibility(8);
                } else {
                    OxHistoryActivity.this.emptyLayout.setVisibility(8);
                    OxHistoryActivity.this.listView.setVisibility(0);
                    for (OxDataTable oxDataTable : list) {
                        if (!oxDataTable.getNumber().equals(AmapLoc.RESULT_TYPE_GPS)) {
                            OxHistoryActivity.this.oxDTOArrayList.add(oxDataTable);
                        }
                    }
                    OxHistoryActivity.this.adapter.notifyDataSetChanged();
                }
                ArrayList arrayList = new ArrayList();
                if (list != null && list.size() > 0) {
                    for (int size = list.size() - 1; size >= 0; size--) {
                        OxDataTable oxDataTable2 = (OxDataTable) list.get(size);
                        if (oxDataTable2 != null && !TextUtils.isEmpty(oxDataTable2.getDateTimeStr())) {
                            String dateTimeStr = oxDataTable2.getDateTimeStr();
                            if (dateTimeStr.length() > 10) {
                                dateTimeStr = dateTimeStr.substring(10);
                            }
                            arrayList.add(new LineDataBean(dateTimeStr, Integer.valueOf(oxDataTable2.getNumber()).intValue()));
                        }
                    }
                }
                OxHistoryActivity.this.lineView.updateShow(arrayList);
            }
        });
    }

    @Override // com.czw.smartkit.measure.history.BaseHistoryActivity
    protected ComAdapter getAdapter() {
        return new ComAdapter<OxDataTable>(this, this.oxDTOArrayList) { // from class: com.czw.smartkit.measure.history.OxHistoryActivity.1
            @Override // com.czw.smartkit.modes.adapter.BaseListAdapter
            public void handDataAndView(ComAdapter.ItemTag itemTag, OxDataTable oxDataTable, int i) {
                itemTag.getTimeTv().setText(oxDataTable.getDateTimeStr().substring(10));
                itemTag.getValueTv().setText(oxDataTable.getNumber() + "%");
            }
        };
    }

    @Override // com.czw.smartkit.measure.history.BaseHistoryActivity, com.czw.smartkit.base.BaseActivity
    public void initView() {
        super.initView();
        this.titleBar.setTitle(R.string.title_ox_histoey);
        this.lineView.initValue(100, 40, 10, "%");
        this.lineView.initColor(-11093506, -11093506);
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public int loadLayout() {
        return R.layout.ui_history_ox;
    }

    @Override // com.czw.smartkit.measure.history.BaseHistoryActivity, android.view.View.OnClickListener
    public /* bridge */ /* synthetic */ void onClick(View view) {
        super.onClick(view);
    }

    @Override // com.czw.smartkit.measure.history.BaseHistoryActivity
    public void onDayChoice(String str) {
        super.onDayChoice(str);
        queryOx(str);
    }

    void queryOx(String str) {
        updateShow(OxServiceImpl.getInstance().getDataByDesc(UserUtil.getUid(), str));
    }
}
