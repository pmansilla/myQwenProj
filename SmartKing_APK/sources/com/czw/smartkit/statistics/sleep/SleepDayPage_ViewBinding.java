package com.czw.smartkit.statistics.sleep;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.czw.smartkit.R;
import com.czw.smartkit.views.SleepTableView;
import com.czw.smartkit.views.SleepView;

/* loaded from: classes.dex */
public class SleepDayPage_ViewBinding implements Unbinder {
    private SleepDayPage target;

    @UiThread
    public SleepDayPage_ViewBinding(SleepDayPage sleepDayPage, View view) {
        this.target = sleepDayPage;
        sleepDayPage.sleepView = (SleepView) Utils.findRequiredViewAsType(view, R.id.sleep_history_day_sleepView, "field 'sleepView'", SleepView.class);
        sleepDayPage.sleepTableView = (SleepTableView) Utils.findRequiredViewAsType(view, R.id.sleep_history_day_sleepTableView, "field 'sleepTableView'", SleepTableView.class);
        sleepDayPage.sleepHourTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.sleep_history_day_hour_textview, "field 'sleepHourTextView'", TextView.class);
        sleepDayPage.sleepMinuteTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.sleep_history_day_minute_textview, "field 'sleepMinuteTextView'", TextView.class);
        sleepDayPage.sleepDeepHourTextview = (TextView) Utils.findRequiredViewAsType(view, R.id.sleep_history_day_deep_hour_textview, "field 'sleepDeepHourTextview'", TextView.class);
        sleepDayPage.sleepDeepMinuteTextview = (TextView) Utils.findRequiredViewAsType(view, R.id.sleep_history_day_deep_minute_textview, "field 'sleepDeepMinuteTextview'", TextView.class);
        sleepDayPage.sleepLightHourTextview = (TextView) Utils.findRequiredViewAsType(view, R.id.sleep_history_day_light_hour_textview, "field 'sleepLightHourTextview'", TextView.class);
        sleepDayPage.sleepLightMinuteTextview = (TextView) Utils.findRequiredViewAsType(view, R.id.sleep_history_day_light_minute_textview, "field 'sleepLightMinuteTextview'", TextView.class);
        sleepDayPage.sleepAwakeHourTextview = (TextView) Utils.findRequiredViewAsType(view, R.id.sleep_history_day_awake_hour_textview, "field 'sleepAwakeHourTextview'", TextView.class);
        sleepDayPage.sleepAwakeMinuteTextview = (TextView) Utils.findRequiredViewAsType(view, R.id.sleep_history_day_awake_minute_textview, "field 'sleepAwakeMinuteTextview'", TextView.class);
    }

    @Override // butterknife.Unbinder
    @CallSuper
    public void unbind() {
        SleepDayPage sleepDayPage = this.target;
        if (sleepDayPage == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        sleepDayPage.sleepView = null;
        sleepDayPage.sleepTableView = null;
        sleepDayPage.sleepHourTextView = null;
        sleepDayPage.sleepMinuteTextView = null;
        sleepDayPage.sleepDeepHourTextview = null;
        sleepDayPage.sleepDeepMinuteTextview = null;
        sleepDayPage.sleepLightHourTextview = null;
        sleepDayPage.sleepLightMinuteTextview = null;
        sleepDayPage.sleepAwakeHourTextview = null;
        sleepDayPage.sleepAwakeMinuteTextview = null;
    }
}
