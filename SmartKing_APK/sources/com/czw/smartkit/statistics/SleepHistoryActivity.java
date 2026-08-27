package com.czw.smartkit.statistics;

import android.support.v4.app.Fragment;
import android.view.View;
import com.czw.smartkit.R;
import com.czw.smartkit.statistics.sleep.SleepDayPage;
import com.czw.smartkit.statistics.sleep.SleepMonthPage;
import com.czw.smartkit.statistics.sleep.SleepWeekPage;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class SleepHistoryActivity extends BaseHistoryActivity {
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private SleepDayPage sleepDayPage = null;
    private SleepWeekPage sleepWeekPage = null;
    private SleepMonthPage sleepMonthPage = null;

    @Override // com.czw.smartkit.statistics.BaseHistoryActivity, com.czw.smartkit.base.BaseActivity
    public void initView() {
        super.initView();
        this.centerText.setText(R.string.sleep_history);
    }

    @Override // com.czw.smartkit.statistics.BaseHistoryActivity
    protected ArrayList<Fragment> loadFragment() {
        this.fragmentArrayList.clear();
        if (this.sleepDayPage == null) {
            this.sleepDayPage = new SleepDayPage();
        }
        if (this.sleepWeekPage == null) {
            this.sleepWeekPage = new SleepWeekPage();
        }
        if (this.sleepMonthPage == null) {
            this.sleepMonthPage = new SleepMonthPage();
        }
        this.fragmentArrayList.add(this.sleepDayPage);
        this.fragmentArrayList.add(this.sleepWeekPage);
        this.fragmentArrayList.add(this.sleepMonthPage);
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
        if (this.sleepDayPage != null) {
            this.sleepDayPage.loadSleepData(str);
        }
    }

    @Override // com.czw.smartkit.statistics.BaseHistoryActivity
    protected void onMonthChoice(int i, int i2, int i3) {
        if (this.sleepMonthPage != null) {
            this.sleepMonthPage.updateShow(i, i2, i3);
        }
    }

    @Override // com.czw.smartkit.statistics.BaseHistoryActivity
    protected void onWeekChoice(long[] jArr) {
        if (this.sleepWeekPage != null) {
            this.sleepWeekPage.updateShow(jArr);
        }
    }
}
