package cn.sharesdk.framework.utils;

import android.os.Handler;
import android.os.Message;
import com.mob.tools.MobHandlerThread;

/* compiled from: SSDKHandlerThread.java */
/* loaded from: classes.dex */
public abstract class d implements Handler.Callback {
    protected final Handler a = MobHandlerThread.newHandler(this);

    public void a(int i, int i2, Object obj) {
        Message message = new Message();
        message.what = -1;
        message.arg1 = i;
        message.arg2 = i2;
        message.obj = obj;
        this.a.sendMessage(message);
    }

    protected void a(Message message) {
    }

    public void b() {
        a(0, 0, null);
    }

    protected abstract void b(Message message);

    protected void c(Message message) {
    }

    @Override // android.os.Handler.Callback
    public final boolean handleMessage(Message message) {
        switch (message.what) {
            case -2:
                c(message);
                return false;
            case -1:
                a(message);
                return false;
            default:
                b(message);
                return false;
        }
    }
}
