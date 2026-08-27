package com.czw.smartkit.statistics.step;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.czw.smartkit.R;
import com.czw.smartkit.views.StepView;

/* loaded from: classes.dex */
public class StepDayPage_ViewBinding implements Unbinder {
    private StepDayPage target;

    @UiThread
    public StepDayPage_ViewBinding(StepDayPage stepDayPage, View view) {
        this.target = stepDayPage;
        stepDayPage.stepView = (StepView) Utils.findRequiredViewAsType(view, R.id.step_history_day_stepView, "field 'stepView'", StepView.class);
        stepDayPage.stepTargetTextview = (TextView) Utils.findRequiredViewAsType(view, R.id.step_history_day_stepTarget, "field 'stepTargetTextview'", TextView.class);
        stepDayPage.totalStepTextview = (TextView) Utils.findRequiredViewAsType(view, R.id.step_history_day_totalStep, "field 'totalStepTextview'", TextView.class);
    }

    @Override // butterknife.Unbinder
    @CallSuper
    public void unbind() {
        StepDayPage stepDayPage = this.target;
        if (stepDayPage == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        stepDayPage.stepView = null;
        stepDayPage.stepTargetTextview = null;
        stepDayPage.totalStepTextview = null;
    }
}
