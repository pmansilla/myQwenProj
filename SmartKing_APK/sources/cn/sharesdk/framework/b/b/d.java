package cn.sharesdk.framework.b.b;

/* compiled from: DemoEvent.java */
/* loaded from: classes.dex */
public class d extends c {
    private static int d;
    private static long m;
    public String a;
    public int b;
    public String c = "";

    @Override // cn.sharesdk.framework.b.b.c
    protected String a() {
        return "[EVT]";
    }

    @Override // cn.sharesdk.framework.b.b.c
    protected void a(long j) {
        m = j;
    }

    @Override // cn.sharesdk.framework.b.b.c
    protected int b() {
        return 5000;
    }

    @Override // cn.sharesdk.framework.b.b.c
    protected int c() {
        return 30;
    }

    @Override // cn.sharesdk.framework.b.b.c
    protected long d() {
        return d;
    }

    @Override // cn.sharesdk.framework.b.b.c
    protected long e() {
        return m;
    }

    @Override // cn.sharesdk.framework.b.b.c
    protected void f() {
        d++;
    }

    @Override // cn.sharesdk.framework.b.b.c
    public String toString() {
        return super.toString() + '|' + this.a + '|' + this.b + '|' + this.c;
    }
}
