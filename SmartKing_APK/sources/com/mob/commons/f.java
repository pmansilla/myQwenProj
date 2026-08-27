package com.mob.commons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* compiled from: MobChannel.java */
/* loaded from: classes.dex */
public class f {
    private static f a;
    private HashMap<String, Object> b;

    private f() {
        this.b = c();
        if (this.b == null) {
            this.b = new HashMap<>();
        }
        ArrayList<MobProduct> products = MobProductCollector.getProducts();
        if (products == null || products.isEmpty()) {
            return;
        }
        Iterator<MobProduct> it = products.iterator();
        while (it.hasNext()) {
            MobProduct next = it.next();
            if (!this.b.containsKey(next.getProductTag())) {
                this.b.put(next.getProductTag(), 0);
            }
        }
    }

    public static f a() {
        if (a == null) {
            synchronized (f.class) {
                if (a == null) {
                    a = new f();
                }
            }
        }
        return a;
    }

    private void a(HashMap<String, Object> hashMap) {
        try {
            i.c(hashMap);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private HashMap<String, Object> c() {
        try {
            return i.u();
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public synchronized void a(MobProduct mobProduct, int i) {
        if (mobProduct != null) {
            this.b.put(mobProduct.getProductTag(), Integer.valueOf(i));
            a(this.b);
        }
    }

    public synchronized HashMap<String, Object> b() {
        return this.b;
    }
}
