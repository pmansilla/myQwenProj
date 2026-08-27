package cn.sharesdk.framework;

import android.os.Handler;
import android.os.Message;
import com.mob.tools.utils.UIHandler;
import java.util.HashMap;

/* loaded from: classes.dex */
public class ReflectablePlatformActionListener implements PlatformActionListener {
    private int a;
    private Handler.Callback b;
    private int c;
    private Handler.Callback d;
    private int e;
    private Handler.Callback f;

    @Override // cn.sharesdk.framework.PlatformActionListener
    public void onCancel(Platform platform, int i) {
        if (this.f != null) {
            Message message = new Message();
            message.what = this.e;
            message.obj = new Object[]{platform, Integer.valueOf(i)};
            UIHandler.sendMessage(message, this.f);
        }
    }

    @Override // cn.sharesdk.framework.PlatformActionListener
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (this.b != null) {
            Message message = new Message();
            message.what = this.a;
            message.obj = new Object[]{platform, Integer.valueOf(i), hashMap};
            UIHandler.sendMessage(message, this.b);
        }
    }

    @Override // cn.sharesdk.framework.PlatformActionListener
    public void onError(Platform platform, int i, Throwable th) {
        if (this.d != null) {
            Message message = new Message();
            message.what = this.c;
            message.obj = new Object[]{platform, Integer.valueOf(i), th};
            UIHandler.sendMessage(message, this.d);
        }
    }

    public void setOnCancelCallback(int i, Handler.Callback callback) {
        this.e = i;
        this.f = callback;
    }

    public void setOnCompleteCallback(int i, Handler.Callback callback) {
        this.a = i;
        this.b = callback;
    }

    public void setOnErrorCallback(int i, Handler.Callback callback) {
        this.c = i;
        this.d = callback;
    }
}
