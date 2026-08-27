package com.amap.api.mapcore.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;

/* compiled from: SPUtil.java */
/* loaded from: classes.dex */
public class im {
    private String a;

    public im(String str) {
        this.a = hl.b(TextUtils.isDigitsOnly(str) ? "SPUtil" : str);
    }

    private void a(SharedPreferences.Editor editor) {
        if (editor == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 9) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    public void a(Context context, String str, boolean z) {
        try {
            SharedPreferences.Editor edit = context.getSharedPreferences(this.a, 0).edit();
            edit.putBoolean(str, z);
            a(edit);
        } catch (Throwable unused) {
        }
    }

    public boolean b(Context context, String str, boolean z) {
        if (context == null) {
            return z;
        }
        try {
            return context.getSharedPreferences(this.a, 0).getBoolean(str, z);
        } catch (Throwable unused) {
            return z;
        }
    }
}
