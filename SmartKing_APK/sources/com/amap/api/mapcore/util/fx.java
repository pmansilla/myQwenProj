package com.amap.api.mapcore.util;

/* compiled from: RectPacker.java */
/* loaded from: classes.dex */
public class fx {
    b a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: RectPacker.java */
    /* loaded from: classes.dex */
    public enum a {
        FAIL,
        PERFECT,
        FIT
    }

    /* compiled from: RectPacker.java */
    /* loaded from: classes.dex */
    class b {
        static final /* synthetic */ boolean e = !fx.class.desiredAssertionStatus();
        String a;
        c b;
        b c = null;
        b d = null;

        b(c cVar) {
            this.b = cVar;
        }

        b a(int i, int i2, String str) {
            if (!a()) {
                b a = this.c.a(i, i2, str);
                return a == null ? this.d.a(i, i2, str) : a;
            }
            if (this.a != null) {
                return null;
            }
            switch (b(i, i2)) {
                case FAIL:
                    return null;
                case PERFECT:
                    this.a = str;
                    return this;
                case FIT:
                    a(i, i2);
                    break;
            }
            return this.c.a(i, i2, str);
        }

        void a(int i, int i2) {
            c cVar;
            c cVar2;
            int i3 = this.b.c - i;
            int i4 = this.b.d - i2;
            if (!e && i3 < 0) {
                throw new AssertionError();
            }
            if (!e && i4 < 0) {
                throw new AssertionError();
            }
            if (i3 > i4) {
                c cVar3 = new c(this.b.a, this.b.b, i, this.b.d);
                cVar2 = new c(cVar3.a + i, this.b.b, this.b.c - i, this.b.d);
                cVar = cVar3;
            } else {
                cVar = new c(this.b.a, this.b.b, this.b.c, i2);
                cVar2 = new c(this.b.a, cVar.b + i2, this.b.c, this.b.d - i2);
            }
            this.c = new b(cVar);
            this.d = new b(cVar2);
        }

        boolean a() {
            return this.c == null;
        }

        boolean a(String str) {
            if (a()) {
                if (!str.equals(this.a)) {
                    return false;
                }
                this.a = null;
                return true;
            }
            boolean a = this.c.a(str);
            if (!a) {
                a = this.d.a(str);
            }
            if (a && !this.c.b() && !this.d.b()) {
                this.c = null;
                this.d = null;
            }
            return a;
        }

        a b(int i, int i2) {
            return (i > this.b.c || i2 > this.b.d) ? a.FAIL : (i == this.b.c && i2 == this.b.d) ? a.PERFECT : a.FIT;
        }

        boolean b() {
            return (this.a == null && a()) ? false : true;
        }
    }

    /* compiled from: RectPacker.java */
    /* loaded from: classes.dex */
    public static class c {
        public int a;
        public int b;
        public int c;
        public int d;

        c(int i, int i2, int i3, int i4) {
            this.a = i;
            this.b = i2;
            this.c = i3;
            this.d = i4;
        }

        public String toString() {
            return "[ x: " + this.a + ", y: " + this.b + ", w: " + this.c + ", h: " + this.d + " ]";
        }
    }

    public fx(int i, int i2) {
        this.a = new b(new c(0, 0, i, i2));
    }

    public int a() {
        return this.a.b.c;
    }

    public c a(int i, int i2, String str) {
        b a2 = this.a.a(i, i2, str);
        if (a2 != null) {
            return new c(a2.b.a, a2.b.b, a2.b.c, a2.b.d);
        }
        return null;
    }

    public boolean a(String str) {
        return this.a.a(str);
    }

    public int b() {
        return this.a.b.d;
    }
}
