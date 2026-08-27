package com.czw.smartkit.homeModule.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.czw.smartkit.R;

/* loaded from: classes.dex */
public class FragmentStep_ViewBinding implements Unbinder {
    private FragmentStep target;

    @UiThread
    public FragmentStep_ViewBinding(FragmentStep fragmentStep, View view) {
        this.target = fragmentStep;
        fragmentStep.viewWeather = Utils.findRequiredView(view, R.id.status_weacher_info_layout, "field 'viewWeather'");
        fragmentStep.ivWeatherIcon = (ImageView) Utils.findRequiredViewAsType(view, R.id.status_weather_icon_iv, "field 'ivWeatherIcon'", ImageView.class);
        fragmentStep.tvTemp = (TextView) Utils.findRequiredViewAsType(view, R.id.status_weather_temp_tv, "field 'tvTemp'", TextView.class);
        fragmentStep.tvTempUnit = (TextView) Utils.findRequiredViewAsType(view, R.id.status_weather_temp_unit_tv, "field 'tvTempUnit'", TextView.class);
        fragmentStep.tvInfo = (TextView) Utils.findRequiredViewAsType(view, R.id.status_weather_info_tv, "field 'tvInfo'", TextView.class);
    }

    @Override // butterknife.Unbinder
    @CallSuper
    public void unbind() {
        FragmentStep fragmentStep = this.target;
        if (fragmentStep == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        fragmentStep.viewWeather = null;
        fragmentStep.ivWeatherIcon = null;
        fragmentStep.tvTemp = null;
        fragmentStep.tvTempUnit = null;
        fragmentStep.tvInfo = null;
    }
}
