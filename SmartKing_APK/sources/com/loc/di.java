package com.loc;

import android.content.Context;

/* loaded from: classes.dex */
final class di implements Runnable {
    final /* synthetic */ Context a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public di(Context context) {
        this.a = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        dl.a(this.a);
    }
}
