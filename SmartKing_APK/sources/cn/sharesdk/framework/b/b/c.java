package cn.sharesdk.framework.b.b;

import com.mob.MobSDK;

/* compiled from: BaseEvent.java */
/* loaded from: classes.dex */
public abstract class c {
    public long e;
    public String f;
    public String g;
    public int h;
    public String i;
    public int j;
    public String k;
    public String l;

    protected abstract String a();

    protected abstract void a(long j);

    protected abstract int b();

    protected abstract int c();

    protected abstract long d();

    protected abstract long e();

    protected abstract void f();

    public boolean g() {
        int b = b();
        int c = c();
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - e() < b) {
            return d() < ((long) c);
        }
        a(currentTimeMillis);
        return true;
    }

    public void h() {
        f();
    }

    public String toString() {
        return a() + ':' + this.e + '|' + this.f + '|' + MobSDK.getAppkey() + '|' + this.g + '|' + this.h + '|' + this.i + '|' + this.j + '|' + this.k;
    }
}
