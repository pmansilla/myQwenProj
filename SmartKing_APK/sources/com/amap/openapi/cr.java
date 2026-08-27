package com.amap.openapi;

import android.content.Context;
import android.location.LocationListener;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import java.util.List;

/* compiled from: AmapGpsManager.java */
/* loaded from: classes.dex */
public class cr {
    private static volatile cr a;
    private cz b;
    private dc c;
    private db d;
    private dd e;
    private cy f;
    private cw g;

    private cr(Context context) {
        this.b = cv.a(context);
        this.e = new dd(this.b, context);
        if (Build.VERSION.SDK_INT >= 24) {
            this.d = new db(this.b, context);
        }
        this.c = new dc(this.b, context);
        this.f = new cy(this.b);
        this.g = new cw(this.b, context.getApplicationContext());
    }

    public static cr a(@NonNull Context context) {
        if (a == null) {
            synchronized (cr.class) {
                if (a == null) {
                    a = new cr(context);
                }
            }
        }
        return a;
    }

    public List<String> a() {
        if (this.b == null) {
            return null;
        }
        return this.b.a();
    }

    public void a(LocationListener locationListener) {
        if (locationListener == null) {
            return;
        }
        this.g.a(locationListener);
    }

    public void a(cs csVar) {
        if (csVar == null) {
            return;
        }
        this.f.a(csVar);
    }

    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    public void a(cu cuVar) {
        if (cuVar == null || this.e == null) {
            return;
        }
        this.e.a(cuVar);
    }

    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    public void a(String str, long j, float f, LocationListener locationListener, Looper looper) {
        if (locationListener == null) {
            return;
        }
        this.g.a(str, j, f, locationListener, looper);
    }

    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    public boolean a(cs csVar, Looper looper) {
        if (csVar == null) {
            return false;
        }
        return this.f.a(csVar, looper);
    }

    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    public boolean a(cu cuVar, Looper looper) {
        return (cuVar == null || this.e == null || !this.e.a(cuVar, looper)) ? false : true;
    }

    public boolean a(String str) {
        if (this.b == null) {
            return false;
        }
        return this.b.a(str);
    }
}
