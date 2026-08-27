package com.amap.openapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.GnssStatus;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.amap.location.common.log.ALLog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* compiled from: SatelliteStatusManager.java */
/* loaded from: classes.dex */
public class dd {
    private cz b;
    private Context c;
    private GnssStatus.Callback e;
    private GpsStatus.Listener f;
    private GpsStatus g;
    private final List<a> a = new CopyOnWriteArrayList();
    private b d = new b();

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: SatelliteStatusManager.java */
    /* loaded from: classes.dex */
    public static class a {
        cu a;
        private Handler b;

        /* compiled from: SatelliteStatusManager.java */
        /* renamed from: com.amap.openapi.dd$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        private static class HandlerC0032a extends Handler {
            private cu a;

            HandlerC0032a(cu cuVar, Looper looper) {
                super(looper);
                this.a = cuVar;
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 1:
                        this.a.a();
                        return;
                    case 2:
                        this.a.b();
                        return;
                    case 3:
                        this.a.a(((Integer) message.obj).intValue());
                        return;
                    case 4:
                        c cVar = (c) message.obj;
                        this.a.a(cVar.a, cVar.b, cVar.c, cVar.d);
                        return;
                    default:
                        return;
                }
            }
        }

        a(cu cuVar, Looper looper) {
            this.a = cuVar;
            this.b = new HandlerC0032a(this.a, looper == null ? Looper.getMainLooper() : looper);
        }

        void a(int i, Object obj) {
            Message obtainMessage = this.b.obtainMessage();
            obtainMessage.what = i;
            obtainMessage.obj = obj;
            obtainMessage.sendToTarget();
        }

        boolean a(cu cuVar, Looper looper) {
            if (looper == null) {
                looper = Looper.getMainLooper();
            }
            return this.a == cuVar && this.b.getLooper() == looper;
        }
    }

    /* compiled from: SatelliteStatusManager.java */
    /* loaded from: classes.dex */
    private class b extends BroadcastReceiver {
        private b() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (cr.a(context).a("gps")) {
                synchronized (dd.this.a) {
                    if (dd.this.a.size() > 0) {
                        try {
                            if (Build.VERSION.SDK_INT >= 24) {
                                if (dd.this.e != null) {
                                    dd.this.b.b(dd.this.e);
                                    dd.this.b.a(dd.this.e);
                                }
                            } else if (dd.this.f != null) {
                                dd.this.b.b(dd.this.f);
                                dd.this.b.a(dd.this.f);
                            }
                        } catch (SecurityException e) {
                            try {
                                ALLog.trace("@_24_5_@", "卫星接口权限异常", e);
                            } catch (SecurityException e2) {
                                ALLog.trace("@_24_5_@", "卫星接口权限异常", e2);
                            }
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: SatelliteStatusManager.java */
    /* loaded from: classes.dex */
    public class c {
        int a;
        int b;
        float c;
        List<ct> d;

        public c(int i, int i2, float f, List<ct> list) {
            this.a = i;
            this.b = i2;
            this.c = f;
            this.d = list;
        }
    }

    public dd(cz czVar, Context context) {
        this.b = czVar;
        this.c = context;
        if (Build.VERSION.SDK_INT >= 24) {
            this.e = new GnssStatus.Callback() { // from class: com.amap.openapi.dd.1
                @Override // android.location.GnssStatus.Callback
                public void onFirstFix(int i) {
                    dd.this.a(i);
                }

                @Override // android.location.GnssStatus.Callback
                public void onSatelliteStatusChanged(GnssStatus gnssStatus) {
                    dd.this.a(gnssStatus);
                }

                @Override // android.location.GnssStatus.Callback
                public void onStarted() {
                    dd.this.a();
                }

                @Override // android.location.GnssStatus.Callback
                public void onStopped() {
                    dd.this.b();
                }
            };
        } else {
            this.f = new GpsStatus.Listener() { // from class: com.amap.openapi.dd.2
                @Override // android.location.GpsStatus.Listener
                public void onGpsStatusChanged(int i) {
                    if (i == 1) {
                        dd.this.a();
                        return;
                    }
                    if (i == 2) {
                        dd.this.b();
                        return;
                    }
                    if (i == 3) {
                        if (dd.this.g == null) {
                            dd.this.g = dd.this.b.a((GpsStatus) null);
                        } else {
                            dd.this.b.a(dd.this.g);
                        }
                        if (dd.this.g != null) {
                            dd.this.a(dd.this.g.getTimeToFirstFix());
                            return;
                        }
                        return;
                    }
                    if (i == 4) {
                        if (dd.this.g == null) {
                            dd.this.g = dd.this.b.a((GpsStatus) null);
                        } else {
                            dd.this.b.a(dd.this.g);
                        }
                        if (dd.this.g != null) {
                            dd.this.a(dd.this.g.getSatellites());
                        }
                    }
                }
            };
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        synchronized (this.a) {
            Iterator<a> it = this.a.iterator();
            while (it.hasNext()) {
                it.next().a(1, (Object) null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i) {
        synchronized (this.a) {
            Iterator<a> it = this.a.iterator();
            while (it.hasNext()) {
                it.next().a(3, Integer.valueOf(i));
            }
        }
    }

    private void a(int i, int i2, float f, List<ct> list) {
        synchronized (this.a) {
            Iterator<a> it = this.a.iterator();
            while (it.hasNext()) {
                it.next().a(4, new c(i, i2, f, list));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(GnssStatus gnssStatus) {
        try {
            if (Build.VERSION.SDK_INT < 24 || gnssStatus == null) {
                return;
            }
            int satelliteCount = gnssStatus.getSatelliteCount();
            ArrayList arrayList = new ArrayList();
            int i = 0;
            float f = 0.0f;
            for (int i2 = 0; i2 < satelliteCount; i2++) {
                arrayList.add(new ct(gnssStatus.usedInFix(i2), gnssStatus.getSvid(i2), gnssStatus.getCn0DbHz(i2), gnssStatus.getElevationDegrees(i2), gnssStatus.getAzimuthDegrees(i2), gnssStatus.getConstellationType(i2)));
                if (gnssStatus.usedInFix(i2)) {
                    i++;
                    f += gnssStatus.getCn0DbHz(i2);
                }
            }
            if (i != 0) {
                f /= i;
            }
            a(i, satelliteCount, f, arrayList);
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Iterable<GpsSatellite> iterable) {
        if (iterable == null) {
            return;
        }
        float f = 0.0f;
        try {
            ArrayList arrayList = new ArrayList();
            int i = 0;
            int i2 = 0;
            for (GpsSatellite gpsSatellite : iterable) {
                if (gpsSatellite != null) {
                    i2++;
                    arrayList.add(new ct(gpsSatellite.usedInFix(), gpsSatellite.getPrn(), gpsSatellite.getSnr(), gpsSatellite.getElevation(), gpsSatellite.getAzimuth(), 0));
                    if (gpsSatellite.usedInFix()) {
                        i++;
                        f += gpsSatellite.getSnr();
                    }
                }
            }
            if (i != 0) {
                f /= i;
            }
            a(i, i2, f, arrayList);
        } catch (Exception unused) {
        }
    }

    private a b(cu cuVar) {
        for (a aVar : this.a) {
            if (aVar.a == cuVar) {
                return aVar;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        synchronized (this.a) {
            Iterator<a> it = this.a.iterator();
            while (it.hasNext()) {
                it.next().a(2, (Object) null);
            }
        }
    }

    public void a(cu cuVar) {
        if (cuVar == null) {
            return;
        }
        synchronized (this.a) {
            a b2 = b(cuVar);
            if (b2 != null) {
                boolean remove = this.a.remove(b2);
                if (this.a.size() == 0 && remove) {
                    try {
                        if (Build.VERSION.SDK_INT >= 24) {
                            if (this.e != null) {
                                this.b.b(this.e);
                            }
                        } else if (this.f != null) {
                            this.b.b(this.f);
                        }
                        this.c.unregisterReceiver(this.d);
                    } catch (Exception e) {
                        ALLog.trace("@_24_5_@", "@_24_5_2_@", e);
                    }
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x0051 A[Catch: all -> 0x0072, TRY_LEAVE, TryCatch #0 {, blocks: (B:8:0x0007, B:10:0x000d, B:11:0x0011, B:13:0x0013, B:19:0x0026, B:21:0x002c, B:23:0x0030, B:26:0x0051, B:27:0x006e, B:30:0x0057, B:33:0x0067, B:34:0x003a, B:36:0x003e, B:16:0x0070, B:38:0x0048), top: B:7:0x0007, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0057 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @android.support.annotation.RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean a(com.amap.openapi.cu r5, android.os.Looper r6) {
        /*
            r4 = this;
            r0 = 0
            if (r5 != 0) goto L4
            return r0
        L4:
            java.util.List<com.amap.openapi.dd$a> r1 = r4.a
            monitor-enter(r1)
            com.amap.openapi.dd$a r2 = r4.b(r5)     // Catch: java.lang.Throwable -> L72
            if (r2 == 0) goto L13
            boolean r5 = r2.a(r5, r6)     // Catch: java.lang.Throwable -> L72
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L72
            return r5
        L13:
            com.amap.openapi.dd$a r2 = new com.amap.openapi.dd$a     // Catch: java.lang.Throwable -> L72
            r2.<init>(r5, r6)     // Catch: java.lang.Throwable -> L72
            java.util.List<com.amap.openapi.dd$a> r5 = r4.a     // Catch: java.lang.Throwable -> L72
            r5.add(r2)     // Catch: java.lang.Throwable -> L72
            java.util.List<com.amap.openapi.dd$a> r5 = r4.a     // Catch: java.lang.Throwable -> L72
            int r5 = r5.size()     // Catch: java.lang.Throwable -> L72
            r6 = 1
            if (r5 != r6) goto L70
            int r5 = android.os.Build.VERSION.SDK_INT     // Catch: java.lang.SecurityException -> L47 java.lang.Throwable -> L72
            r6 = 24
            if (r5 < r6) goto L3a
            android.location.GnssStatus$Callback r5 = r4.e     // Catch: java.lang.SecurityException -> L47 java.lang.Throwable -> L72
            if (r5 == 0) goto L4f
            com.amap.openapi.cz r5 = r4.b     // Catch: java.lang.SecurityException -> L47 java.lang.Throwable -> L72
            android.location.GnssStatus$Callback r6 = r4.e     // Catch: java.lang.SecurityException -> L47 java.lang.Throwable -> L72
            boolean r5 = r5.a(r6)     // Catch: java.lang.SecurityException -> L47 java.lang.Throwable -> L72
        L38:
            r0 = r5
            goto L4f
        L3a:
            android.location.GpsStatus$Listener r5 = r4.f     // Catch: java.lang.SecurityException -> L47 java.lang.Throwable -> L72
            if (r5 == 0) goto L4f
            com.amap.openapi.cz r5 = r4.b     // Catch: java.lang.SecurityException -> L47 java.lang.Throwable -> L72
            android.location.GpsStatus$Listener r6 = r4.f     // Catch: java.lang.SecurityException -> L47 java.lang.Throwable -> L72
            boolean r5 = r5.a(r6)     // Catch: java.lang.SecurityException -> L47 java.lang.Throwable -> L72
            goto L38
        L47:
            r5 = move-exception
            java.lang.String r6 = "@_24_5_@"
            java.lang.String r3 = "卫星接口权限异常"
            com.amap.location.common.log.ALLog.trace(r6, r3, r5)     // Catch: java.lang.Throwable -> L72
        L4f:
            if (r0 != 0) goto L57
            java.util.List<com.amap.openapi.dd$a> r5 = r4.a     // Catch: java.lang.Throwable -> L72
            r5.remove(r2)     // Catch: java.lang.Throwable -> L72
            goto L6e
        L57:
            android.content.Context r5 = r4.c     // Catch: java.lang.Exception -> L66 java.lang.Throwable -> L72
            com.amap.openapi.dd$b r6 = r4.d     // Catch: java.lang.Exception -> L66 java.lang.Throwable -> L72
            android.content.IntentFilter r2 = new android.content.IntentFilter     // Catch: java.lang.Exception -> L66 java.lang.Throwable -> L72
            java.lang.String r3 = "android.location.PROVIDERS_CHANGED"
            r2.<init>(r3)     // Catch: java.lang.Exception -> L66 java.lang.Throwable -> L72
            r5.registerReceiver(r6, r2)     // Catch: java.lang.Exception -> L66 java.lang.Throwable -> L72
            goto L6e
        L66:
            r5 = move-exception
            java.lang.String r6 = "@_24_6_@"
            java.lang.String r2 = "@_24_6_1_@"
            com.amap.location.common.log.ALLog.trace(r6, r2, r5)     // Catch: java.lang.Throwable -> L72
        L6e:
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L72
            return r0
        L70:
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L72
            return r6
        L72:
            r5 = move-exception
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L72
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.dd.a(com.amap.openapi.cu, android.os.Looper):boolean");
    }
}
