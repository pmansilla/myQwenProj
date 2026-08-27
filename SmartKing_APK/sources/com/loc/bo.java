package com.loc;

import android.content.Context;

/* compiled from: OfflineLocEntity.java */
/* loaded from: classes.dex */
public final class bo {
    private Context a;
    private ac b;
    private String c;

    public bo(Context context, ac acVar, String str) {
        this.a = context.getApplicationContext();
        this.b = acVar;
        this.c = str;
    }

    private static String a(Context context, ac acVar, String str) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("\"sdkversion\":\"");
            sb.append(acVar.c());
            sb.append("\",\"product\":\"");
            sb.append(acVar.a());
            sb.append("\",\"nt\":\"");
            sb.append(x.d(context));
            sb.append("\",\"details\":");
            sb.append(str);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final byte[] a() {
        return ad.a(a(this.a, this.b, this.c));
    }
}
