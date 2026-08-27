package cn.smssdk;

import android.os.Handler;
import android.os.Message;

/* loaded from: classes.dex */
public class ReflectableEnventHandler extends EventHandler {
    private int a;
    private Handler.Callback b;
    private int c;
    private Handler.Callback d;
    private int e;
    private Handler.Callback f;
    private int g;
    private Handler.Callback h;

    @Override // cn.smssdk.EventHandler
    public void afterEvent(int i, int i2, Object obj) {
        if (this.f != null) {
            Message message = new Message();
            message.what = this.e;
            message.obj = new Object[]{Integer.valueOf(i), Integer.valueOf(i2), obj};
            this.f.handleMessage(message);
        }
    }

    @Override // cn.smssdk.EventHandler
    public void beforeEvent(int i, Object obj) {
        if (this.d != null) {
            Message message = new Message();
            message.what = this.c;
            message.obj = new Object[]{Integer.valueOf(i), obj};
            this.d.handleMessage(message);
        }
    }

    @Override // cn.smssdk.EventHandler
    public void onRegister() {
        if (this.b != null) {
            Message message = new Message();
            message.what = this.a;
            this.b.handleMessage(message);
        }
    }

    @Override // cn.smssdk.EventHandler
    public void onUnregister() {
        if (this.h != null) {
            Message message = new Message();
            message.what = this.g;
            this.h.handleMessage(message);
        }
    }

    public void setAfterEventCallback(int i, Handler.Callback callback) {
        this.e = i;
        this.f = callback;
    }

    public void setBeforeEventCallback(int i, Handler.Callback callback) {
        this.c = i;
        this.d = callback;
    }

    public void setOnRegisterCallback(int i, Handler.Callback callback) {
        this.a = i;
        this.b = callback;
    }

    public void setOnUnregisterCallback(int i, Handler.Callback callback) {
        this.g = i;
        this.h = callback;
    }
}
