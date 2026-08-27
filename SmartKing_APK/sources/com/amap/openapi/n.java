package com.amap.openapi;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import com.amap.location.collection.CollectionConfig;
import java.util.ArrayList;
import java.util.List;

/* compiled from: TrackCollector.java */
/* loaded from: classes.dex */
public class n {
    private static final String a = "n";
    private Context b;
    private CollectionConfig.TrackCollectorConfig c;
    private Handler d;
    private t f;
    private cu h;
    private long i;
    private Location j;
    private long k;
    private List<ct> m;
    private final Object e = new Object();
    private List<y> l = new ArrayList();
    private v n = new v();
    private j g = new j();

    public n(Context context, t tVar, CollectionConfig.TrackCollectorConfig trackCollectorConfig, Looper looper) {
        this.b = context;
        this.c = trackCollectorConfig;
        this.f = tVar;
        this.d = new Handler(looper);
    }

    private byte[] b(Location location) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (this.j != null && (elapsedRealtime - this.i < 2000 || location.distanceTo(this.j) < 5.0f)) {
            return null;
        }
        this.i = elapsedRealtime;
        this.j = location;
        boolean z = false;
        if (this.c.isCollectSatellites() && this.k != 0 && elapsedRealtime - this.k <= 3000) {
            z = true;
        }
        ba.a(this.n, ba.a(this.l, z, this.m), location, location.getTime(), System.currentTimeMillis());
        return this.g.a(this.b, this.n, this.l, this.c.getLocScene());
    }

    public void a() {
        this.h = new cu() { // from class: com.amap.openapi.n.1
            @Override // com.amap.openapi.cu
            public void a() {
            }

            @Override // com.amap.openapi.cu
            public void a(int i) {
            }

            @Override // com.amap.openapi.cu
            public void a(int i, int i2, float f, List<ct> list) {
                n.this.k = SystemClock.elapsedRealtime();
                n.this.m = list;
            }

            @Override // com.amap.openapi.cu
            public void b() {
            }
        };
        try {
            cr.a(this.b).a(this.h, this.d.getLooper());
        } catch (SecurityException | Exception unused) {
        }
    }

    public void a(Location location) {
        byte[] b = b(location);
        if (b != null) {
            this.f.a(1, b);
        }
    }

    public void b() {
        try {
            cr.a(this.b).a(this.h);
        } catch (SecurityException | Exception unused) {
        }
        synchronized (this.e) {
            this.d.removeCallbacksAndMessages(null);
            this.d = null;
        }
    }
}
