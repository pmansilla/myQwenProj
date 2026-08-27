package cn.sharesdk.framework.b.b;

import android.text.TextUtils;

/* compiled from: StartEvent.java */
/* loaded from: classes.dex */
public class g extends c {
    private static int a;
    private static long b;

    @Override // cn.sharesdk.framework.b.b.c
    protected String a() {
        return "[RUN]";
    }

    @Override // cn.sharesdk.framework.b.b.c
    protected void a(long j) {
        b = j;
    }

    @Override // cn.sharesdk.framework.b.b.c
    protected int b() {
        return 5000;
    }

    @Override // cn.sharesdk.framework.b.b.c
    protected int c() {
        return 5;
    }

    @Override // cn.sharesdk.framework.b.b.c
    protected long d() {
        return a;
    }

    @Override // cn.sharesdk.framework.b.b.c
    protected long e() {
        return b;
    }

    @Override // cn.sharesdk.framework.b.b.c
    protected void f() {
        a++;
    }

    @Override // cn.sharesdk.framework.b.b.c
    public boolean g() {
        cn.sharesdk.framework.b.a.e a2 = cn.sharesdk.framework.b.a.e.a();
        a = a2.g("insertRunEventCount");
        b = a2.f("lastInsertRunEventTime");
        return super.g();
    }

    @Override // cn.sharesdk.framework.b.b.c
    public void h() {
        super.h();
        cn.sharesdk.framework.b.a.e a2 = cn.sharesdk.framework.b.a.e.a();
        a2.a("lastInsertRunEventTime", Long.valueOf(b));
        a2.a("insertRunEventCount", a);
    }

    @Override // cn.sharesdk.framework.b.b.c
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append('|');
        if (!TextUtils.isEmpty(this.l)) {
            sb.append(this.l);
        }
        return sb.toString();
    }
}
