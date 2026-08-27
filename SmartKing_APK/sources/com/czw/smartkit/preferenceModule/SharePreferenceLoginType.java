package com.czw.smartkit.preferenceModule;

import com.czw.smartkit.sharedpreferences.domain.LoginType;
import com.czw.smartkit.util.SaveObjectUtils;

/* loaded from: classes.dex */
public class SharePreferenceLoginType {
    public static void clearAll() {
        save(null);
    }

    public static LoginType read() {
        return (LoginType) SaveObjectUtils.getObject("cfg_login_type", LoginType.class);
    }

    public static void save(LoginType loginType) {
        SaveObjectUtils.setObject("cfg_login_type", loginType);
    }
}
