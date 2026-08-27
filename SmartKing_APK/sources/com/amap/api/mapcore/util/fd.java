package com.amap.api.mapcore.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/* compiled from: LruCache.java */
/* loaded from: classes.dex */
public class fd<K, V> {
    private final LinkedHashMap<K, V> a;
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;
    private int g;
    private int h;

    public fd(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.c = i;
        this.a = new LinkedHashMap<>(0, 0.75f, true);
    }

    private void a(int i) {
        K key;
        V value;
        while (true) {
            synchronized (this) {
                if (this.b >= 0 && this.a.isEmpty()) {
                    int i2 = this.b;
                }
                if (this.b <= i) {
                    return;
                }
                Iterator<Map.Entry<K, V>> it = this.a.entrySet().iterator();
                Map.Entry<K, V> entry = null;
                while (it.hasNext()) {
                    entry = it.next();
                }
                if (entry == null) {
                    return;
                }
                key = entry.getKey();
                value = entry.getValue();
                this.a.remove(key);
                this.b -= c(key, value);
                this.f++;
            }
            a(true, key, value, null);
        }
    }

    private int c(K k, V v) {
        int b = b(k, v);
        if (b >= 0) {
            return b;
        }
        throw new IllegalStateException("Negative size: " + k + "=" + v);
    }

    public final V a(K k) {
        V v;
        if (k == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this) {
            V v2 = this.a.get(k);
            if (v2 != null) {
                this.g++;
                return v2;
            }
            this.h++;
            V b = b(k);
            if (b == null) {
                return null;
            }
            synchronized (this) {
                this.e++;
                v = (V) this.a.put(k, b);
                if (v != null) {
                    this.a.put(k, v);
                } else {
                    this.b += c(k, b);
                }
            }
            if (v != null) {
                a(false, k, b, v);
                return v;
            }
            a(this.c);
            return b;
        }
    }

    public final V a(K k, V v) {
        V put;
        if (k == null || v == null) {
            throw new NullPointerException("key == null || value == null");
        }
        synchronized (this) {
            this.d++;
            this.b += c(k, v);
            put = this.a.put(k, v);
            if (put != null) {
                this.b -= c(k, put);
            }
        }
        if (put != null) {
            a(false, k, put, v);
        }
        a(this.c);
        return put;
    }

    public final void a() {
        a(-1);
    }

    protected void a(boolean z, K k, V v, V v2) {
    }

    protected int b(K k, V v) {
        return 1;
    }

    protected V b(K k) {
        return null;
    }

    public final synchronized String toString() {
        int i;
        i = this.g + this.h;
        return String.format(Locale.US, "LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", Integer.valueOf(this.c), Integer.valueOf(this.g), Integer.valueOf(this.h), Integer.valueOf(i != 0 ? (this.g * 100) / i : 0));
    }
}
