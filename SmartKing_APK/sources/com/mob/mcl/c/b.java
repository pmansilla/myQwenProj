package com.mob.mcl.c;

import android.os.Handler;
import android.os.Message;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: HeartBeatHelper.java */
/* loaded from: classes.dex */
public class b implements Handler.Callback {

    /* compiled from: HeartBeatHelper.java */
    /* loaded from: classes.dex */
    class a implements Runnable {
        a(b bVar) {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (i.c().f()) {
                return;
            }
            if (!i.c().i()) {
                i.c().j();
            }
            i.c().a(5000);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(d dVar) {
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        com.mob.mcl.b.a.e.execute(new a(this));
        return false;
    }
}
