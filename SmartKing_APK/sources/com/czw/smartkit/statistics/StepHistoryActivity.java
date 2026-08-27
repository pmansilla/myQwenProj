package com.czw.smartkit.statistics;

import android.support.v4.app.Fragment;
import android.view.View;
import com.czw.smartkit.R;
import com.czw.smartkit.statistics.step.StepDayPage;
import com.czw.smartkit.statistics.step.StepMonthPage;
import com.czw.smartkit.statistics.step.StepWeekPage;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class StepHistoryActivity extends BaseHistoryActivity {
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private StepDayPage stepDayPage = null;
    private StepWeekPage stepWeekPage = null;
    private StepMonthPage stepMonthPage = null;

    @Override // com.czw.smartkit.statistics.BaseHistoryActivity, com.czw.smartkit.base.BaseActivity
    public void initView() {
        super.initView();
        this.centerText.setText(R.string.step_history);
    }

    @Override // com.czw.smartkit.statistics.BaseHistoryActivity
    protected ArrayList<Fragment> loadFragment() {
        this.fragmentArrayList.clear();
        if (this.stepDayPage == null) {
            this.stepDayPage = new StepDayPage();
        }
        if (this.stepWeekPage == null) {
            this.stepWeekPage = new StepWeekPage();
        }
        if (this.stepMonthPage == null) {
            this.stepMonthPage = new StepMonthPage();
        }
        this.fragmentArrayList.add(this.stepDayPage);
        this.fragmentArrayList.add(this.stepWeekPage);
        this.fragmentArrayList.add(this.stepMonthPage);
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
        if (this.stepDayPage != null) {
            this.stepDayPage.updateShow(str);
        }
    }

    @Override // com.czw.smartkit.statistics.BaseHistoryActivity
    protected void onMonthChoice(int i, int i2, int i3) {
        if (this.stepMonthPage != null) {
            this.stepMonthPage.updateShow(i, i2, i3);
        }
    }

    @Override // com.czw.smartkit.statistics.BaseHistoryActivity
    protected void onWeekChoice(long[] jArr) {
        if (this.stepWeekPage != null) {
            this.stepWeekPage.updateShow(jArr);
        }
    }
}
