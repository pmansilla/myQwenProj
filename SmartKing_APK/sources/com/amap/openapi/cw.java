package com.amap.openapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.RequiresPermission;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.jvm.internal.LongCompanionObject;

/* compiled from: GpsLocationManager.java */
/* loaded from: classes.dex */
public class cw {
    private a a;
    private a b;

    /* compiled from: GpsLocationManager.java */
    /* loaded from: classes.dex */
    private static class a implements LocationListener {
        private cz a;
        private String b;
        private Context c;
        private C0030a d = new C0030a(this);
        private final List<cx> e = new ArrayList();
        private long f = LongCompanionObject.MAX_VALUE;
        private float g = Float.MAX_VALUE;
        private Location h;

        /* JADX INFO: Access modifiers changed from: private */
        /* compiled from: GpsLocationManager.java */
        /* renamed from: com.amap.openapi.cw$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        public class C0030a extends BroadcastReceiver {
            private LocationListener b;

            public C0030a(LocationListener locationListener) {
                this.b = locationListener;
            }

            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (cr.a(context).a("gps")) {
                    synchronized (a.this.e) {
                        if (a.this.e.size() > 0) {
                            a.this.a.a(this.b);
                            a.this.a.a(a.this.b, a.this.f, a.this.g, this.b, Looper.getMainLooper());
                        }
                    }
                }
            }
        }

        a(String str, cz czVar, Context context) {
            this.a = czVar;
            this.b = str;
            this.c = context;
        }

        private void a() {
            boolean isEmpty = this.e.isEmpty();
            float f = Float.MAX_VALUE;
            long j = LongCompanionObject.MAX_VALUE;
            if (isEmpty) {
                this.a.a(this);
                this.h = null;
                this.f = LongCompanionObject.MAX_VALUE;
                this.g = Float.MAX_VALUE;
                return;
            }
            for (cx cxVar : this.e) {
                j = Math.min(j, cxVar.b);
                f = Math.min(f, cxVar.c);
            }
            if (this.f == j && this.g == f) {
                return;
            }
            this.f = j;
            this.g = f;
            this.a.a(this);
            this.a.a(this.b, this.f, this.g, this, Looper.getMainLooper());
        }

        void a(long j, float f, LocationListener locationListener, Looper looper) {
            synchronized (this.e) {
                for (cx cxVar : this.e) {
                    if (cxVar.a == locationListener) {
                        if (cxVar.b != j || cxVar.c != f) {
                            cxVar.b = j;
                            cxVar.c = f;
                            a();
                        }
                        return;
                    }
                }
                if (this.e.size() == 0) {
                    try {
                        this.c.registerReceiver(this.d, new IntentFilter("android.location.PROVIDERS_CHANGED"));
                    } catch (Exception unused) {
                    }
                }
                this.e.add(new cx(locationListener, j, f, looper));
                a();
            }
        }

        void a(LocationListener locationListener) {
            synchronized (this.e) {
                boolean z = false;
                Iterator<cx> it = this.e.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    cx next = it.next();
                    if (next.a == locationListener) {
                        this.e.remove(next);
                        a();
                        z = true;
                        break;
                    }
                }
                if (this.e.size() == 0 && z) {
                    try {
                        this.c.unregisterReceiver(this.d);
                    } catch (Exception unused) {
                    }
                }
            }
        }

        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) {
            if (location == null) {
                return;
            }
            float abs = this.h == null ? Float.MAX_VALUE : Math.abs(location.distanceTo(this.h));
            synchronized (this.e) {
                Iterator<cx> it = this.e.iterator();
                while (it.hasNext()) {
                    it.next().a(location, abs);
                }
            }
            this.h = location;
        }

        @Override // android.location.LocationListener
        public void onProviderDisabled(String str) {
            synchronized (this.e) {
                Iterator<cx> it = this.e.iterator();
                while (it.hasNext()) {
                    it.next().a(str, false);
                }
            }
        }

        @Override // android.location.LocationListener
        public void onProviderEnabled(String str) {
            synchronized (this.e) {
                Iterator<cx> it = this.e.iterator();
                while (it.hasNext()) {
                    it.next().a(str, true);
                }
            }
        }

        @Override // android.location.LocationListener
        public void onStatusChanged(String str, int i, Bundle bundle) {
            synchronized (this.e) {
                Iterator<cx> it = this.e.iterator();
                while (it.hasNext()) {
                    it.next().a(str, i, bundle);
                }
            }
        }
    }

    public cw(cz czVar, Context context) {
        this.a = new a("gps", czVar, context);
        this.b = new a("passive", czVar, context);
    }

    public void a(LocationListener locationListener) {
        if (locationListener == null) {
            return;
        }
        this.a.a(locationListener);
        this.b.a(locationListener);
    }

    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    public void a(String str, long j, float f, LocationListener locationListener, Looper looper) {
        if (locationListener == null) {
            return;
        }
        a aVar = null;
        if ("gps".equals(str)) {
            aVar = this.a;
        } else if ("passive".equals(str)) {
            aVar = this.b;
        }
        a aVar2 = aVar;
        if (aVar2 != null) {
            aVar2.a(j, f, locationListener, looper);
        }
    }
}
