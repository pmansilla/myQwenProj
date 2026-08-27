package com.loc;

import android.content.Context;
import android.text.TextUtils;
import com.amap.api.location.AMapLocation;
import java.util.List;
import org.json.JSONObject;

/* compiled from: LastLocationManager.java */
/* loaded from: classes.dex */
public final class r {
    static ej b;
    static av e;
    static long g;
    String a = null;
    ej c = null;
    ej d = null;
    long f = 0;
    boolean h = false;
    private Context i;

    public r(Context context) {
        this.i = context.getApplicationContext();
    }

    private void e() {
        if (b == null || fa.c() - g > 180000) {
            ej f = f();
            g = fa.c();
            if (f == null || !fa.a(f.a())) {
                return;
            }
            b = f;
        }
    }

    private ej f() {
        Throwable th;
        ej ejVar;
        byte[] d;
        byte[] d2;
        String str = null;
        if (this.i == null) {
            return null;
        }
        a();
        try {
        } catch (Throwable th2) {
            th = th2;
            ejVar = null;
        }
        if (e == null) {
            return null;
        }
        List a = e.a("_id=1", ej.class, false);
        if (a.size() > 0) {
            ejVar = (ej) a.get(0);
            try {
                byte[] b2 = y.b(ejVar.c());
                String str2 = (b2 == null || b2.length <= 0 || (d2 = eh.d(b2, this.a)) == null || d2.length <= 0) ? null : new String(d2, "UTF-8");
                byte[] b3 = y.b(ejVar.b());
                if (b3 != null && b3.length > 0 && (d = eh.d(b3, this.a)) != null && d.length > 0) {
                    str = new String(d, "UTF-8");
                }
                ejVar.a(str);
                str = str2;
            } catch (Throwable th3) {
                th = th3;
                es.a(th, "LastLocationManager", "readLastFix");
                return ejVar;
            }
        } else {
            ejVar = null;
        }
        if (!TextUtils.isEmpty(str)) {
            AMapLocation aMapLocation = new AMapLocation("");
            es.a(aMapLocation, new JSONObject(str));
            if (fa.b(aMapLocation)) {
                ejVar.a(aMapLocation);
            }
        }
        return ejVar;
    }

    public final AMapLocation a(AMapLocation aMapLocation, String str, long j) {
        if (aMapLocation == null || aMapLocation.getErrorCode() == 0 || aMapLocation.getLocationType() == 1 || aMapLocation.getErrorCode() == 7) {
            return aMapLocation;
        }
        try {
            e();
            if (b != null && b.a() != null) {
                boolean z = false;
                if (TextUtils.isEmpty(str)) {
                    long c = fa.c() - b.d();
                    if (c >= 0 && c <= j) {
                        z = true;
                    }
                    aMapLocation.setTrustedLevel(3);
                } else {
                    z = fa.a(b.b(), str);
                    aMapLocation.setTrustedLevel(2);
                }
                if (!z) {
                    return aMapLocation;
                }
                AMapLocation a = b.a();
                try {
                    a.setLocationType(9);
                    a.setFixLastLocation(true);
                    a.setLocationDetail(aMapLocation.getLocationDetail());
                    return a;
                } catch (Throwable th) {
                    th = th;
                    aMapLocation = a;
                    es.a(th, "LastLocationManager", "fixLastLocation");
                    return aMapLocation;
                }
            }
            return aMapLocation;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public final void a() {
        if (this.h) {
            return;
        }
        try {
            if (this.a == null) {
                this.a = eh.a("MD5", x.u(this.i));
            }
            if (e == null) {
                e = new av(this.i, av.a((Class<? extends au>) ek.class));
            }
        } catch (Throwable th) {
            es.a(th, "LastLocationManager", "<init>:DBOperation");
        }
        this.h = true;
    }

    public final boolean a(AMapLocation aMapLocation, String str) {
        if (this.i != null && aMapLocation != null && fa.a(aMapLocation) && aMapLocation.getLocationType() != 2 && !aMapLocation.isMock() && !aMapLocation.isFixLastLocation()) {
            ej ejVar = new ej();
            ejVar.a(aMapLocation);
            if (aMapLocation.getLocationType() == 1) {
                ejVar.a((String) null);
            } else {
                ejVar.a(str);
            }
            try {
                b = ejVar;
                g = fa.c();
                this.c = ejVar;
                if (this.d != null && fa.a(this.d.a(), ejVar.a()) <= 500.0f) {
                    return false;
                }
                if (fa.c() - this.f > 30000) {
                    return true;
                }
            } catch (Throwable th) {
                es.a(th, "LastLocationManager", "setLastFix");
            }
        }
        return false;
    }

    public final AMapLocation b() {
        e();
        if (b != null && fa.a(b.a())) {
            return b.a();
        }
        return null;
    }

    public final void c() {
        try {
            d();
            this.f = 0L;
            this.h = false;
            this.c = null;
            this.d = null;
        } catch (Throwable th) {
            es.a(th, "LastLocationManager", "destroy");
        }
    }

    public final void d() {
        String str;
        try {
            a();
            if (this.c != null && fa.a(this.c.a()) && e != null && this.c != this.d && this.c.d() == 0) {
                String str2 = this.c.a().toStr();
                String b2 = this.c.b();
                this.d = this.c;
                String str3 = null;
                if (TextUtils.isEmpty(str2)) {
                    str = null;
                } else {
                    str = y.b(eh.c(str2.getBytes("UTF-8"), this.a));
                    if (!TextUtils.isEmpty(b2)) {
                        str3 = y.b(eh.c(b2.getBytes("UTF-8"), this.a));
                    }
                }
                if (TextUtils.isEmpty(str)) {
                    return;
                }
                ej ejVar = new ej();
                ejVar.b(str);
                ejVar.a(fa.c());
                ejVar.a(str3);
                e.a(ejVar, "_id=1");
                this.f = fa.c();
                if (b != null) {
                    b.a(fa.c());
                }
            }
        } catch (Throwable th) {
            es.a(th, "LastLocationManager", "saveLastFix");
        }
    }
}
