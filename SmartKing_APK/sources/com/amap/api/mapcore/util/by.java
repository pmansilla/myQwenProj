package com.amap.api.mapcore.util;

import android.content.Context;
import com.amap.api.maps.AMap;
import java.util.LinkedHashMap;
import java.util.Map;

/* compiled from: TaskManager.java */
/* loaded from: classes.dex */
public class by {
    private static by a;
    private jy b;
    private LinkedHashMap<String, jz> c = new LinkedHashMap<>();
    private boolean d = true;

    private by(boolean z, int i) {
        if (z) {
            try {
                this.b = jy.a(i);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public static by a(int i) {
        return a(true, i);
    }

    private static synchronized by a(boolean z, int i) {
        by byVar;
        synchronized (by.class) {
            try {
                if (a == null) {
                    a = new by(z, i);
                } else if (z && a.b == null) {
                    a.b = jy.a(i);
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
            byVar = a;
        }
        return byVar;
    }

    public static void c() {
        a = null;
    }

    public void a() {
        synchronized (this.c) {
            if (this.c.size() < 1) {
                return;
            }
            for (Map.Entry<String, jz> entry : this.c.entrySet()) {
                entry.getKey();
                ((bu) entry.getValue()).a();
            }
            this.c.clear();
        }
    }

    public void a(bx bxVar) {
        synchronized (this.c) {
            bu buVar = (bu) this.c.get(bxVar.b());
            if (buVar == null) {
                return;
            }
            buVar.a();
            this.c.remove(bxVar.b());
        }
    }

    public void a(bx bxVar, Context context, AMap aMap) throws hc {
        jy jyVar = this.b;
        if (!this.c.containsKey(bxVar.b())) {
            bu buVar = new bu((co) bxVar, context.getApplicationContext(), aMap);
            synchronized (this.c) {
                this.c.put(bxVar.b(), buVar);
            }
        }
        this.b.a(this.c.get(bxVar.b()));
    }

    public void b() {
        a();
        jy.a();
        this.b = null;
        c();
    }

    public void b(bx bxVar) {
        bu buVar = (bu) this.c.get(bxVar.b());
        if (buVar != null) {
            synchronized (this.c) {
                buVar.b();
                this.c.remove(bxVar.b());
            }
        }
    }
}
