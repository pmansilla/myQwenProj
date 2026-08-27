package com.amap.openapi;

import android.content.Context;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import com.amap.location.collection.CollectionConfig;
import java.util.List;

/* compiled from: FpsCollector.java */
/* loaded from: classes.dex */
public class m {
    private static final String a = "m";
    private Context b;
    private Handler c;
    private t d;
    private l e;
    private o f;
    private cs g;
    private long h;
    private long i;
    private Location j;
    private v l = new v();
    private h k = new h();

    public m(Context context, t tVar, CollectionConfig.FpsCollectorConfig fpsCollectorConfig, Looper looper) {
        this.b = context;
        this.d = tVar;
        this.c = new Handler(looper);
        this.e = new l(this.b, looper);
        this.f = new o(this.b, looper);
    }

    public void a() {
        this.e.a();
        this.f.a();
        this.g = new cs() { // from class: com.amap.openapi.m.1
            @Override // com.amap.openapi.cs
            public void a(long j, String str) {
                m.this.h = j;
            }
        };
        try {
            cr.a(this.b).a(this.g, this.c.getLooper());
        } catch (SecurityException | Exception unused) {
        }
    }

    public void a(Location location, List<ScanResult> list, long j, long j2) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (this.j == null || location.distanceTo(this.j) >= 10.0f) {
            q a2 = this.e.a(location);
            List<aa> a3 = this.f.a(location, list, j, j2);
            if (a2 != null || a3 != null) {
                ba.a(this.l, location, this.h, j2);
                byte[] a4 = this.k.a(this.b, this.l, a2, this.f.c(), a3);
                if (a4 != null) {
                    this.d.a(0, a4);
                }
            }
            this.j = location;
            this.i = elapsedRealtime;
        }
    }

    public void b() {
        try {
            cr.a(this.b).a(this.g);
        } catch (Exception unused) {
        }
        this.c.removeCallbacksAndMessages(null);
        this.e.b();
        this.f.b();
    }
}
