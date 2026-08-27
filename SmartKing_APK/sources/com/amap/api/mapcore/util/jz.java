package com.amap.api.mapcore.util;

/* compiled from: ThreadTask.java */
/* loaded from: classes.dex */
public abstract class jz implements Runnable {
    a d;

    /* compiled from: ThreadTask.java */
    /* loaded from: classes.dex */
    interface a {
        void a(jz jzVar);

        void b(jz jzVar);

        void c(jz jzVar);
    }

    public final void cancelTask() {
        try {
            if (this.d != null) {
                this.d.c(this);
            }
        } catch (Throwable th) {
            ic.c(th, "ThreadTask", "cancelTask");
            th.printStackTrace();
        }
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            if (this.d != null) {
                this.d.a(this);
            }
            if (Thread.interrupted()) {
                return;
            }
            runTask();
            if (Thread.interrupted() || this.d == null) {
                return;
            }
            this.d.b(this);
        } catch (Throwable th) {
            ic.c(th, "ThreadTask", "run");
            th.printStackTrace();
        }
    }

    public abstract void runTask();
}
