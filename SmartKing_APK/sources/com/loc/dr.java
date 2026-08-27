package com.loc;

import android.content.Context;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class dr implements Runnable {
    final /* synthetic */ dq a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public dr(dq dqVar) {
        this.a = dqVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Context context;
        context = dq.d;
        dl.a(context);
    }
}
