package com.czw.smartkit.measure.history;

import android.text.TextUtils;
import android.view.View;
import butterknife.BindView;
import com.amap.location.common.model.AmapLoc;
import com.czw.smartkit.R;
import com.czw.smartkit.databaseModule.hr.HrDataTable;
import com.czw.smartkit.databaseModule.hr.HrServiceImpl;
import com.czw.smartkit.measure.ComAdapter;
import com.czw.smartkit.user.UserUtil;
import com.czw.smartkit.views.lineview.LineDataBean;
import com.czw.smartkit.views.lineview.LineShowView;
import com.czw.utils.LogUtil;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class HrHistoryActivity extends BaseHistoryActivity {
    private List<HrDataTable> hrDTOList = new ArrayList();

    @BindView(R.id.hr_history_lineView)
    LineShowView lineView;

    private void updateShow(final List<HrDataTable> list) {
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.measure.history.HrHistoryActivity.2
            @Override // java.lang.Runnable
            public void run() {
                HrHistoryActivity.this.hrDTOList.clear();
                if (list.size() == 0 || list == null) {
                    HrHistoryActivity.this.emptyLayout.setVisibility(0);
                    HrHistoryActivity.this.listView.setVisibility(8);
                } else {
                    HrHistoryActivity.this.emptyLayout.setVisibility(8);
                    HrHistoryActivity.this.listView.setVisibility(0);
                    for (HrDataTable hrDataTable : list) {
                        if (!TextUtils.isEmpty(hrDataTable.getNumber()) && !hrDataTable.getNumber().equals(AmapLoc.RESULT_TYPE_GPS)) {
                            HrHistoryActivity.this.hrDTOList.add(hrDataTable);
                        }
                    }
                    HrHistoryActivity.this.adapter.notifyDataSetChanged();
                }
                LogUtil.e("===更新的心率列表数据:" + new Gson().toJson(HrHistoryActivity.this.hrDTOList));
                ArrayList arrayList = new ArrayList();
                if (list != null && list.size() > 0) {
                    for (int size = list.size() - 1; size >= 0; size--) {
                        HrDataTable hrDataTable2 = (HrDataTable) list.get(size);
                        if (hrDataTable2 != null && !TextUtils.isEmpty(hrDataTable2.getDateTimeStr())) {
                            String dateTimeStr = hrDataTable2.getDateTimeStr();
                            if (dateTimeStr.length() > 10) {
                                dateTimeStr = dateTimeStr.substring(10);
                            }
                            arrayList.add(new LineDataBean(dateTimeStr, Integer.valueOf(hrDataTable2.getNumber()).intValue()));
                        }
                    }
                }
                HrHistoryActivity.this.lineView.updateShow(arrayList);
            }
        });
    }

    @Override // com.czw.smartkit.measure.history.BaseHistoryActivity
    protected ComAdapter getAdapter() {
        return new ComAdapter<HrDataTable>(this, this.hrDTOList) { // from class: com.czw.smartkit.measure.history.HrHistoryActivity.1
            @Override // com.czw.smartkit.modes.adapter.BaseListAdapter
            public void handDataAndView(ComAdapter.ItemTag itemTag, HrDataTable hrDataTable, int i) {
                itemTag.getTimeTv().setText(hrDataTable.getDateTimeStr().substring(10));
                itemTag.getValueTv().setText(hrDataTable.getNumber() + "bpm");
            }
        };
    }

    @Override // com.czw.smartkit.measure.history.BaseHistoryActivity, com.czw.smartkit.base.BaseActivity
    public void initView() {
        super.initView();
        this.titleBar.setTitle(R.string.title_hr_histoey);
        this.lineView.initValue(140, 40, 10, "");
        this.lineView.initColor(-514716, -514716);
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public int loadLayout() {
        return R.layout.ui_history_hr;
    }

    @Override // com.czw.smartkit.measure.history.BaseHistoryActivity, android.view.View.OnClickListener
    public /* bridge */ /* synthetic */ void onClick(View view) {
        super.onClick(view);
    }

    @Override // com.czw.smartkit.measure.history.BaseHistoryActivity
    public void onDayChoice(String str) {
        super.onDayChoice(str);
        queryHr(str);
    }

    void queryHr(String str) {
        updateShow(HrServiceImpl.getInstance().getDataByDesc(UserUtil.getUid(), str));
    }
}
