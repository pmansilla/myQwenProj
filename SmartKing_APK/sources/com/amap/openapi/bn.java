package com.amap.openapi;

import android.os.Build;
import android.os.SystemClock;
import android.util.ArrayMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* compiled from: RssiInfoManager.java */
/* loaded from: classes.dex */
public class bn {
    private Map<Long, a> a;
    private Map<Long, a> b;
    private Map<Long, a> c;
    private Map<Long, a> d;
    private Object e = new Object();
    private Object f = new Object();

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: RssiInfoManager.java */
    /* loaded from: classes.dex */
    public static class a {
        int a;
        long b;
        boolean c;

        private a() {
        }
    }

    public bn() {
        Map<Long, a> hashMap;
        if (Build.VERSION.SDK_INT >= 19) {
            this.a = new ArrayMap();
            this.b = new ArrayMap();
            this.c = new ArrayMap();
            hashMap = new ArrayMap<>();
        } else {
            this.a = new HashMap();
            this.b = new HashMap();
            this.c = new HashMap();
            hashMap = new HashMap<>();
        }
        this.d = hashMap;
    }

    private short a(Map<Long, a> map, long j) {
        synchronized (map) {
            a aVar = map.get(Long.valueOf(j));
            if (aVar == null) {
                return (short) 0;
            }
            short max = (short) Math.max(1L, Math.min(32767L, (b() - aVar.b) / 1000));
            if (!aVar.c) {
                max = (short) (-max);
            }
            return max;
        }
    }

    private void a(List<bm> list, Map<Long, a> map, Map<Long, a> map2) {
        long b = b();
        if (map.isEmpty()) {
            for (bm bmVar : list) {
                a aVar = new a();
                aVar.a = bmVar.b();
                aVar.b = b;
                aVar.c = false;
                map2.put(Long.valueOf(bmVar.a()), aVar);
            }
            return;
        }
        for (bm bmVar2 : list) {
            long a2 = bmVar2.a();
            a aVar2 = map.get(Long.valueOf(a2));
            if (aVar2 == null) {
                aVar2 = new a();
            } else if (aVar2.a == bmVar2.b()) {
                map2.put(Long.valueOf(a2), aVar2);
            }
            aVar2.a = bmVar2.b();
            aVar2.b = b;
            aVar2.c = true;
            map2.put(Long.valueOf(a2), aVar2);
        }
    }

    private static long b() {
        return SystemClock.elapsedRealtime();
    }

    public short a(long j) {
        return a(this.a, j);
    }

    public void a() {
        synchronized (this.e) {
            this.a.clear();
        }
        synchronized (this.f) {
            this.c.clear();
        }
    }

    public void a(List<bm> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        synchronized (this.e) {
            a(list, this.a, this.b);
            Map<Long, a> map = this.a;
            this.a = this.b;
            this.b = map;
            this.b.clear();
        }
    }

    public short b(long j) {
        return a(this.c, j);
    }

    public void b(List<bm> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        synchronized (this.f) {
            a(list, this.c, this.d);
            Map<Long, a> map = this.c;
            this.c = this.d;
            this.d = map;
            this.d.clear();
        }
    }
}
