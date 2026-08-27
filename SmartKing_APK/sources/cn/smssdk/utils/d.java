package cn.smssdk.utils;

/* compiled from: SafelyThread.java */
/* loaded from: classes.dex */
public abstract class d extends Thread {
    public d(String str) {
        super(str);
    }

    protected abstract void a();

    @Override // java.lang.Thread, java.lang.Runnable
    public final void run() {
        try {
            a();
        } catch (Throwable th) {
            SMSLog.getInstance().d(th);
        }
    }
}
