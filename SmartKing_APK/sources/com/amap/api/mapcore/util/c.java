package com.amap.api.mapcore.util;

import android.content.Context;
import android.text.TextUtils;
import java.util.List;

/* compiled from: AdiuManager.java */
/* loaded from: classes.dex */
public class c {
    private static c a;
    private final Context b;
    private final String c = i.a("amap_device_adiu");
    private String d;

    public c(Context context) {
        this.b = context.getApplicationContext();
    }

    public static c a(Context context) {
        if (a == null) {
            synchronized (c.class) {
                if (a == null) {
                    a = new c(context);
                }
            }
        }
        return a;
    }

    public void a(String str) {
        d.a(this.b).a(this.c);
        d.a(this.b).b(str);
    }

    public synchronized boolean a() {
        if (TextUtils.isEmpty(this.d) && TextUtils.isEmpty(g.a())) {
            d.a(this.b).a(this.c);
            List<String> a2 = d.a(this.b).a();
            if (a2 != null && a2.size() > 0) {
                String str = a2.get(0);
                if (!TextUtils.isEmpty(str)) {
                    this.d = str;
                    g.a(this.d);
                    String str2 = "";
                    if (a2.size() > 1) {
                        String str3 = a2.get(1);
                        if (!TextUtils.isEmpty(str3)) {
                            str2 = str3;
                        }
                    }
                    if (a2.size() > 2) {
                        String str4 = a2.get(2);
                        if (!TextUtils.isEmpty(str4)) {
                            str2 = ":" + str4;
                        }
                    }
                    if (!TextUtils.isEmpty(str2)) {
                        g.b(str2);
                    }
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public String b() {
        return g.a();
    }
}
