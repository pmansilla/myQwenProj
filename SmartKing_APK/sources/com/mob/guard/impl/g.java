package com.mob.guard.impl;

import com.mob.tools.MobLog;

/* loaded from: classes.dex */
public abstract class g extends Thread {
    protected abstract void a() throws Throwable;

    @Override // java.lang.Thread, java.lang.Runnable
    public final void run() {
        try {
            a();
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
    }
}
