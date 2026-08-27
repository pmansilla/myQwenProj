package com.czw.smartkit.preferenceModule;

import com.czw.smartkit.util.SaveObjectUtils;

/* loaded from: classes.dex */
public class SharePrefreshLaunch {
    public static void clearAll() {
        save(true);
    }

    public static boolean read() {
        Boolean bool = (Boolean) SaveObjectUtils.getObject("cfg_launch", Boolean.class);
        if (bool == null) {
            return true;
        }
        return bool.booleanValue();
    }

    public static void save(boolean z) {
        SaveObjectUtils.setObject("cfg_launch", Boolean.valueOf(z));
    }
}
