package com.amap.api.mapcore.util;

import com.amap.api.mapcore.util.fz;

/* compiled from: AbstractPool.java */
/* loaded from: classes.dex */
public abstract class fy<T extends fz<?>> {
    protected T a;

    protected boolean a(T t) {
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0, types: [T extends com.amap.api.mapcore.util.fz<T>] */
    /* JADX WARN: Type inference failed for: r2v0, types: [T extends com.amap.api.mapcore.util.fz<?>, T extends com.amap.api.mapcore.util.fz<T>] */
    public T b(T t) {
        if (t == null) {
            return null;
        }
        while (t != null) {
            ?? r1 = t.f;
            a(t);
            t.f = this.a;
            this.a = t;
            t = r1;
        }
        return null;
    }
}
