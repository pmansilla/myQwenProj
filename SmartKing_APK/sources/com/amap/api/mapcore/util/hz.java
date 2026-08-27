package com.amap.api.mapcore.util;

import android.content.Context;
import java.lang.Thread;

/* compiled from: BasicLogHandler.java */
/* loaded from: classes.dex */
public class hz {
    protected static hz a;
    protected Thread.UncaughtExceptionHandler b;
    protected boolean c = true;

    public static void a(Throwable th, String str, String str2) {
        if (a != null) {
            a.a(th, 1, str, str2);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a(Context context, ho hoVar, boolean z) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a(ho hoVar, String str, String str2) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a(Throwable th, int i, String str, String str2) {
    }
}
