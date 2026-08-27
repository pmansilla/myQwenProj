package com.amap.api.mapcore.util;

/* compiled from: UpdateStrategy.java */
/* loaded from: classes.dex */
public abstract class jw {
    jw a;

    public jw() {
    }

    public jw(jw jwVar) {
        this.a = jwVar;
    }

    private boolean d() {
        if (this.a != null) {
            return this.a.c();
        }
        return true;
    }

    public void a(int i) {
        if (this.a != null) {
            this.a.a(i);
        }
    }

    public void a(boolean z) {
        if (this.a != null) {
            this.a.a(z);
        }
    }

    protected abstract boolean a();

    public int b() {
        return Math.min(Integer.MAX_VALUE, this.a != null ? this.a.b() : Integer.MAX_VALUE);
    }

    public boolean c() {
        if (d()) {
            return a();
        }
        return false;
    }
}
