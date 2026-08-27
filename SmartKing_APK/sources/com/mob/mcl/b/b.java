package com.mob.mcl.b;

import com.mob.mcl.c.i;

/* compiled from: MCLink.java */
/* loaded from: classes.dex */
class b implements Runnable {
    final /* synthetic */ int a;
    final /* synthetic */ a b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(a aVar, int i) {
        this.b = aVar;
        this.a = i;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            if (i.c().f()) {
                return;
            }
            a.a(this.b, this.a);
        } catch (Throwable th) {
            com.mob.mcl.d.b.a().a(th);
        }
    }
}
