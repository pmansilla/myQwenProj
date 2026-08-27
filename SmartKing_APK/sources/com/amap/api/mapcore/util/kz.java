package com.amap.api.mapcore.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;

/* compiled from: SpUtil.java */
/* loaded from: classes.dex */
public final class kz {
    public static int a(Context context, String str, String str2) {
        try {
            return context.getSharedPreferences(str, 0).getInt(str2, 200);
        } catch (Throwable th) {
            kw.a(th, "SpUtil", "getPrefsInt");
            return 200;
        }
    }

    public static String a(Context context) {
        return context == null ? "00:00:00:00:00:00" : b(context, "pref", "smac", "00:00:00:00:00:00");
    }

    public static void a(Context context, String str) {
        if (context == null || TextUtils.isEmpty(str)) {
            return;
        }
        a(context, "pref", "smac", str);
    }

    private static void a(Context context, String str, String str2, String str3) {
        try {
            SharedPreferences.Editor edit = context.getSharedPreferences(str, 0).edit();
            edit.putString(str2, str3);
            a(edit);
        } catch (Throwable th) {
            kw.a(th, "SpUtil", "setPrefsStr");
        }
    }

    @SuppressLint({"NewApi"})
    private static void a(SharedPreferences.Editor editor) {
        if (editor == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 9) {
            editor.apply();
        } else {
            b(editor);
        }
    }

    private static String b(Context context, String str, String str2, String str3) {
        try {
            return context.getSharedPreferences(str, 0).getString(str2, str3);
        } catch (Throwable th) {
            kw.a(th, "SpUtil", "getPrefsInt");
            return str3;
        }
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.amap.api.mapcore.util.kz$1] */
    private static void b(final SharedPreferences.Editor editor) {
        try {
            new AsyncTask<Void, Void, Void>() { // from class: com.amap.api.mapcore.util.kz.1
                private Void a() {
                    try {
                        if (editor == null) {
                            return null;
                        }
                        editor.commit();
                        return null;
                    } catch (Throwable th) {
                        kw.a(th, "SpUtil", "commit");
                        return null;
                    }
                }

                @Override // android.os.AsyncTask
                protected final /* synthetic */ Void doInBackground(Void[] voidArr) {
                    return a();
                }
            }.execute(null, null, null);
        } catch (Throwable th) {
            kw.a(th, "SpUtil", "commit1");
        }
    }

    public static boolean b(Context context, String str, String str2) {
        try {
            return context.getSharedPreferences(str, 0).getBoolean(str2, true);
        } catch (Throwable th) {
            kw.a(th, "SpUtil", "getPrefsBoolean");
            return true;
        }
    }
}
