package com.mob.mcl.c;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: TcpHelper.java */
/* loaded from: classes.dex */
public class j implements Runnable {
    final /* synthetic */ i a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public j(i iVar) {
        this.a = iVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            if (this.a.f()) {
                return;
            }
            if (!this.a.i()) {
                this.a.j();
            }
            this.a.a(5000);
        } catch (Throwable unused) {
        }
    }
}
