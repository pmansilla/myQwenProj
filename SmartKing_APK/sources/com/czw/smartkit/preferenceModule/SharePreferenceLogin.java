package com.czw.smartkit.preferenceModule;

import com.czw.smartkit.sharedpreferences.domain.LoginInfo;
import com.czw.smartkit.util.SaveObjectUtils;

/* loaded from: classes.dex */
public class SharePreferenceLogin {
    public static void clearAll() {
        save(null);
    }

    public static LoginInfo read() {
        return (LoginInfo) SaveObjectUtils.getObject("cfg_login", LoginInfo.class);
    }

    public static void save(LoginInfo loginInfo) {
        SaveObjectUtils.setObject("cfg_login", loginInfo);
    }
}
