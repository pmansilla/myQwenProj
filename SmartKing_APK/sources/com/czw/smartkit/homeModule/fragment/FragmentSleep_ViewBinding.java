package com.czw.smartkit.homeModule.fragment;

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
public class FragmentSleep_ViewBinding implements Unbinder {
    private FragmentSleep target;

    @UiThread
    public FragmentSleep_ViewBinding(FragmentSleep fragmentSleep, View view) {
        this.target = fragmentSleep;
        fragmentSleep.sleepTableView = (SleepTableView) Utils.findRequiredViewAsType(view, R.id.sleepTableView, "field 'sleepTableView'", SleepTableView.class);
        fragmentSleep.sleepView = (SleepView) Utils.findRequiredViewAsType(view, R.id.sleepView, "field 'sleepView'", SleepView.class);
        fragmentSleep.sleepHourTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.sleep_hour_textview, "field 'sleepHourTextView'", TextView.class);
        fragmentSleep.sleepMinuteTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.sleep_minute_textview, "field 'sleepMinuteTextView'", TextView.class);
        fragmentSleep.sleepDeepHourTextview = (TextView) Utils.findRequiredViewAsType(view, R.id.sleep_deep_hour_textview, "field 'sleepDeepHourTextview'", TextView.class);
        fragmentSleep.sleepDeepMinuteTextview = (TextView) Utils.findRequiredViewAsType(view, R.id.sleep_deep_minute_textview, "field 'sleepDeepMinuteTextview'", TextView.class);
        fragmentSleep.sleepLightHourTextview = (TextView) Utils.findRequiredViewAsType(view, R.id.sleep_light_hour_textview, "field 'sleepLightHourTextview'", TextView.class);
        fragmentSleep.sleepLightMinuteTextview = (TextView) Utils.findRequiredViewAsType(view, R.id.sleep_light_minute_textview, "field 'sleepLightMinuteTextview'", TextView.class);
        fragmentSleep.sleepAwakeHourTextview = (TextView) Utils.findRequiredViewAsType(view, R.id.sleep_awake_hour_textview, "field 'sleepAwakeHourTextview'", TextView.class);
        fragmentSleep.sleepAwakeMinuteTextview = (TextView) Utils.findRequiredViewAsType(view, R.id.sleep_awake_minute_textview, "field 'sleepAwakeMinuteTextview'", TextView.class);
    }

    @Override // butterknife.Unbinder
    @CallSuper
    public void unbind() {
        FragmentSleep fragmentSleep = this.target;
        if (fragmentSleep == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        fragmentSleep.sleepTableView = null;
        fragmentSleep.sleepView = null;
        fragmentSleep.sleepHourTextView = null;
        fragmentSleep.sleepMinuteTextView = null;
        fragmentSleep.sleepDeepHourTextview = null;
        fragmentSleep.sleepDeepMinuteTextview = null;
        fragmentSleep.sleepLightHourTextview = null;
        fragmentSleep.sleepLightMinuteTextview = null;
        fragmentSleep.sleepAwakeHourTextview = null;
        fragmentSleep.sleepAwakeMinuteTextview = null;
    }
}
