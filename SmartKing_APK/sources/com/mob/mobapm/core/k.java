package com.mob.mobapm.core;

import android.os.Handler;
import android.os.Message;
import com.mob.tools.MobHandlerThread;

/* loaded from: classes.dex */
public abstract class k implements Handler.Callback {
    protected Object b = new Object();
    protected Handler a = MobHandlerThread.newHandler(this);

    public void a() {
        if (this.a == null) {
            this.a = MobHandlerThread.newHandler(this);
        }
        this.a.removeCallbacksAndMessages(null);
        this.a.sendEmptyMessage(0);
    }

    public void b() {
        try {
            if (this.a != null) {
                this.a.removeCallbacksAndMessages(null);
            }
        } catch (Throwable th) {
            com.mob.mobapm.d.a.a().d("APM: stop work error: " + th, new Object[0]);
        }
    }

    public abstract void c();

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        if (message.what != 0) {
            return false;
        }
        c();
        return false;
    }
}
