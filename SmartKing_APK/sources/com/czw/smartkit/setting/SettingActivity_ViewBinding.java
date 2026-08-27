package com.czw.smartkit.setting;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.czw.smartkit.R;

/* loaded from: classes.dex */
public class SettingActivity_ViewBinding implements Unbinder {
    private SettingActivity target;

    @UiThread
    public SettingActivity_ViewBinding(SettingActivity settingActivity) {
        this(settingActivity, settingActivity.getWindow().getDecorView());
    }

    @UiThread
    public SettingActivity_ViewBinding(SettingActivity settingActivity, View view) {
        this.target = settingActivity;
        settingActivity.itemViewUpdatePwdLayout = Utils.findRequiredView(view, R.id.setting_item_update_pwd, "field 'itemViewUpdatePwdLayout'");
        settingActivity.itemViewUpdatePwdLine = Utils.findRequiredView(view, R.id.setting_item_update_pwd_line, "field 'itemViewUpdatePwdLine'");
        settingActivity.setting_item_clock = Utils.findRequiredView(view, R.id.setting_item_clock, "field 'setting_item_clock'");
        settingActivity.setting_item_clock_line = Utils.findRequiredView(view, R.id.setting_item_clock_line, "field 'setting_item_clock_line'");
        settingActivity.setting_item_hand_light = Utils.findRequiredView(view, R.id.setting_item_hand_light, "field 'setting_item_hand_light'");
        settingActivity.setting_item_hand_light_line = Utils.findRequiredView(view, R.id.setting_item_hand_light_line, "field 'setting_item_hand_light_line'");
        settingActivity.setting_item_long_sit = Utils.findRequiredView(view, R.id.setting_item_long_sit, "field 'setting_item_long_sit'");
        settingActivity.setting_item_long_sit_line = Utils.findRequiredView(view, R.id.setting_item_long_sit_line, "field 'setting_item_long_sit_line'");
    }

    @Override // butterknife.Unbinder
    @CallSuper
    public void unbind() {
        SettingActivity settingActivity = this.target;
        if (settingActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        settingActivity.itemViewUpdatePwdLayout = null;
        settingActivity.itemViewUpdatePwdLine = null;
        settingActivity.setting_item_clock = null;
        settingActivity.setting_item_clock_line = null;
        settingActivity.setting_item_hand_light = null;
        settingActivity.setting_item_hand_light_line = null;
        settingActivity.setting_item_long_sit = null;
        settingActivity.setting_item_long_sit_line = null;
    }
}
