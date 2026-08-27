package com.amap.api.mapcore.util;

import android.content.Context;

/* compiled from: OfflineLocEntity.java */
/* loaded from: classes.dex */
public class jf {
    private Context a;
    private ho b;
    private String c;

    public jf(Context context, ho hoVar, String str) {
        this.a = context.getApplicationContext();
        this.b = hoVar;
        this.c = str;
    }

    private static String a(Context context, ho hoVar, String str) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("\"sdkversion\":\"");
            sb.append(hoVar.c());
            sb.append("\",\"product\":\"");
            sb.append(hoVar.a());
            sb.append("\",\"nt\":\"");
            sb.append(hi.e(context));
            sb.append("\",\"details\":");
            sb.append(str);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public byte[] a() {
        return hp.a(a(this.a, this.b, this.c));
    }
}
