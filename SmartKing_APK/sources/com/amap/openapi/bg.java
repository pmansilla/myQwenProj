package com.amap.openapi;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.amap.location.collection.CollectionConfig;
import com.amap.openapi.bh;
import java.util.List;

/* compiled from: GpsWifiWrapper.java */
/* loaded from: classes.dex */
public class bg {
    private bh a;
    private boolean c;
    private cr d;
    private Context g;
    private Looper h;
    private bf i;
    private boolean j;
    private final Object f = new Object();
    private LocationListener b = new LocationListener() { // from class: com.amap.openapi.bg.1
        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) {
            if (bg.this.j) {
                try {
                    if (ba.a(location) && !ba.a(bg.this.g, location)) {
                        bg.this.b();
                        if (bg.this.i != null) {
                            bh.a f = bg.this.a.f();
                            bg.this.i.a(location, f.a, f.b, System.currentTimeMillis());
                        }
                    }
                } catch (Throwable unused) {
                }
            }
        }

        @Override // android.location.LocationListener
        public void onProviderDisabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onProviderEnabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onStatusChanged(String str, int i, Bundle bundle) {
        }
    };
    private cu e = new cu() { // from class: com.amap.openapi.bg.2
        @Override // com.amap.openapi.cu
        public void a() {
        }

        @Override // com.amap.openapi.cu
        public void a(int i) {
        }

        @Override // com.amap.openapi.cu
        public void a(int i, int i2, float f, List<ct> list) {
            bg.this.a(i);
        }

        @Override // com.amap.openapi.cu
        public void b() {
        }
    };

    public bg(Context context, @NonNull CollectionConfig.FpsCollectorConfig fpsCollectorConfig, @NonNull bf bfVar, @NonNull Looper looper) {
        this.g = context;
        this.h = looper;
        this.d = cr.a(context);
        this.i = bfVar;
        this.a = new bh(context, fpsCollectorConfig, looper);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i) {
        boolean z = i < 4;
        if (this.c != z) {
            this.c = z;
            if (z) {
                this.a.d();
            } else {
                this.a.c();
            }
        }
    }

    public void a() {
        synchronized (this.f) {
            this.j = false;
            try {
                this.d.a(this.b);
                this.d.a(this.e);
            } catch (Throwable unused) {
            }
        }
    }

    public void a(String str, long j, float f) {
        synchronized (this.f) {
            this.j = true;
            try {
                List<String> a = this.d.a();
                if (a.contains("gps") || a.contains("passive")) {
                    this.d.a(str, j, 0.0f, this.b, this.h);
                    this.d.a(this.e, this.h);
                }
            } catch (Throwable unused) {
            }
        }
    }

    public void b() {
        if (this.a.e()) {
            return;
        }
        this.a.a();
    }

    public void c() {
        if (this.a.e()) {
            this.a.b();
        }
    }
}
