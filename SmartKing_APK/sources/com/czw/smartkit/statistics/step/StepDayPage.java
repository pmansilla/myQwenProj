package com.czw.smartkit.statistics.step;

import android.widget.TextView;
import butterknife.BindView;
import com.czw.smartkit.R;
import com.czw.smartkit.base.BaseFragment;
import com.czw.smartkit.databaseModule.step.StepDataTable;
import com.czw.smartkit.databaseModule.step.StepServiceImpl;
import com.czw.smartkit.user.UserUtil;
import com.czw.smartkit.views.StepView;
import org.apache.commons.lang.time.DateFormatUtils;

/* loaded from: classes.dex */
public class StepDayPage extends BaseFragment {

    @BindView(R.id.step_history_day_stepTarget)
    TextView stepTargetTextview;

    @BindView(R.id.step_history_day_stepView)
    StepView stepView;

    @BindView(R.id.step_history_day_totalStep)
    TextView totalStepTextview;

    @Override // com.czw.modes.fragment.RootFragment
    public void initAfterCreate() {
        updateShow(DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd"));
    }

    @Override // com.czw.modes.fragment.RootFragment
    public int loadLayout() {
        return R.layout.page_step_day;
    }

    public void updateShow(final String str) {
        if (getActivity() == null || this.stepTargetTextview == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() { // from class: com.czw.smartkit.statistics.step.StepDayPage.1
            @Override // java.lang.Runnable
            public void run() {
                int i;
                int i2;
                StepDataTable stepDataByDate = StepServiceImpl.getInstance().getStepDataByDate(UserUtil.getUid(), str);
                if (stepDataByDate != null) {
                    i = stepDataByDate.getWalkCounts();
                    i2 = stepDataByDate.getGoalWalk();
                    if (i2 == 0) {
                        i2 = UserUtil.getTarget();
                    }
                } else {
                    i = 0;
                    i2 = 8000;
                }
                StepDayPage.this.totalStepTextview.setText(i + "");
                StepDayPage.this.stepTargetTextview.setText(StepDayPage.this.getString(R.string.target) + i2);
                StepDayPage.this.stepView.updateShow((((float) i) * 1.0f) / ((float) i2));
            }
        });
    }
}
