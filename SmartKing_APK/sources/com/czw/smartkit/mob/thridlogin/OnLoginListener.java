package com.czw.smartkit.mob.thridlogin;

import java.util.HashMap;

/* loaded from: classes.dex */
public interface OnLoginListener {
    boolean onLogin(String str, HashMap<String, Object> hashMap);

    boolean onRegister(ThridUserInfo thridUserInfo);
}
