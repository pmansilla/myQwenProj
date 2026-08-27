package com.amap.api.mapcore.util;

import android.content.Context;

/* compiled from: StatisticsUtil.java */
/* loaded from: classes.dex */
public class fp {
    private static String a(boolean z) {
        try {
            return "{\"Quest\":" + z + "}";
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public static void a(Context context, boolean z) {
        try {
            String a = a(z);
            jh jhVar = new jh(context, "3dmap", "6.9.3", "O001");
            jhVar.a(a);
            ji.a(jhVar, context);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
