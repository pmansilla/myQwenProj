package com.czw.smartkit.statistics;

import android.support.v4.app.Fragment;
import android.view.View;
import com.czw.smartkit.R;
import com.czw.smartkit.statistics.trans.TransDayPage;
import com.czw.smartkit.statistics.trans.TransMonthPage;
import com.czw.smartkit.statistics.trans.TransWeekPage;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class TransHistoryActivity extends BaseHistoryActivity {
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    TransDayPage transDayPage = null;
    TransWeekPage transWeekPage = null;
    TransMonthPage transMonthPage = null;

    @Override // com.czw.smartkit.statistics.BaseHistoryActivity, com.czw.smartkit.base.BaseActivity
    public void initView() {
        super.initView();
        this.centerText.setText(R.string.trans_history);
    }

    @Override // com.czw.smartkit.statistics.BaseHistoryActivity
    protected ArrayList<Fragment> loadFragment() {
        this.fragmentArrayList.clear();
        if (this.transDayPage == null) {
            this.transDayPage = new TransDayPage();
        }
        if (this.transWeekPage == null) {
            this.transWeekPage = new TransWeekPage();
        }
        if (this.transMonthPage == null) {
            this.transMonthPage = new TransMonthPage();
        }
        this.fragmentArrayList.add(this.transDayPage);
        this.fragmentArrayList.add(this.transWeekPage);
        this.fragmentArrayList.add(this.transMonthPage);
        return this.fragmentArrayList;
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public int loadLayout() {
        return R.layout.activity_step_history;
    }

    @Override // com.czw.smartkit.statistics.BaseHistoryActivity, android.view.View.OnClickListener
    public /* bridge */ /* synthetic */ void onClick(View view) {
        super.onClick(view);
    }

    @Override // com.czw.smartkit.statistics.BaseHistoryActivity
    protected void onDayChoice(String str) {
        if (this.transDayPage != null) {
            this.transDayPage.updateShow(str);
        }
    }

    @Override // com.czw.smartkit.statistics.BaseHistoryActivity
    protected void onMonthChoice(int i, int i2, int i3) {
        if (this.transMonthPage != null) {
            this.transMonthPage.updateShow(i, i2, i3);
        }
    }

    @Override // com.czw.smartkit.statistics.BaseHistoryActivity
    protected void onWeekChoice(long[] jArr) {
        if (this.transWeekPage != null) {
            this.transWeekPage.updateShow(jArr);
        }
    }
}
