package cn.sharesdk.framework.b.b;

import android.text.TextUtils;

/* compiled from: ExitEvent.java */
/* loaded from: classes.dex */
public class e extends c {
    private static int b;
    private static long c;
    public long a;

    @Override // cn.sharesdk.framework.b.b.c
    protected String a() {
        return "[EXT]";
    }

    @Override // cn.sharesdk.framework.b.b.c
    protected void a(long j) {
        c = j;
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
        return b;
    }

    @Override // cn.sharesdk.framework.b.b.c
    protected long e() {
        return c;
    }

    @Override // cn.sharesdk.framework.b.b.c
    protected void f() {
        b++;
    }

    @Override // cn.sharesdk.framework.b.b.c
    public boolean g() {
        cn.sharesdk.framework.b.a.e a = cn.sharesdk.framework.b.a.e.a();
        b = a.g("insertExitEventCount");
        c = a.f("lastInsertExitEventTime");
        return super.g();
    }

    @Override // cn.sharesdk.framework.b.b.c
    public void h() {
        super.h();
        cn.sharesdk.framework.b.a.e a = cn.sharesdk.framework.b.a.e.a();
        a.a("lastInsertExitEventTime", Long.valueOf(c));
        a.a("insertExitEventCount", b);
    }

    @Override // cn.sharesdk.framework.b.b.c
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append('|');
        if (!TextUtils.isEmpty(this.l)) {
            sb.append(this.l);
        }
        sb.append('|');
        sb.append(Math.round(((float) this.a) / 1000.0f));
        return sb.toString();
    }
}
