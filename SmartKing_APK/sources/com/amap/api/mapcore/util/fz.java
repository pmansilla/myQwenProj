package com.amap.api.mapcore.util;

import com.amap.api.mapcore.util.fz;

/* compiled from: Inlist.java */
/* loaded from: classes.dex */
public class fz<T extends fz<T>> {
    public T f;

    public static <T extends fz<?>> T a(T t, T t2) {
        if (t2.f != null) {
            throw new IllegalArgumentException("'item' is a list");
        }
        t2.f = t;
        return t2;
    }
}
