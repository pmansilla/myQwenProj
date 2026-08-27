package com.loc;

import android.content.Context;
import java.lang.Thread;

/* compiled from: BasicLogHandler.java */
/* loaded from: classes.dex */
public class an {
    protected static an a;
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
    public void a(Context context, ac acVar, boolean z) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a(ac acVar, String str, String str2) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a(Throwable th, int i, String str, String str2) {
    }
}
