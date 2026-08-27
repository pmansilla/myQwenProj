package com.czw.smartkit.preferenceModule;

import com.czw.smartkit.bleModule.data.RemindSetting;
import com.czw.smartkit.util.SaveObjectUtils;

/* loaded from: classes.dex */
public class SharePreferenceRemind {
    public static void clearAll() {
        save(null);
    }

    public static RemindSetting read() {
        RemindSetting remindSetting = (RemindSetting) SaveObjectUtils.getObject("cfg_remind", RemindSetting.class);
        return remindSetting == null ? new RemindSetting() : remindSetting;
    }

    public static void save(RemindSetting remindSetting) {
        SaveObjectUtils.setObject("cfg_remind", remindSetting);
    }
}
