package com.loc;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceListener;
import com.amap.api.fence.GeoFenceManagerBase;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.DPoint;
import com.amap.api.maps.model.MyLocationStyle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* compiled from: GeoFenceManager.java */
@SuppressLint({"NewApi"})
/* loaded from: classes.dex */
public final class j implements GeoFenceManagerBase {
    Context b;
    ey a = null;
    PendingIntent c = null;
    String d = null;
    GeoFenceListener e = null;
    private Object z = new Object();
    volatile int f = 1;
    ArrayList<GeoFence> g = new ArrayList<>();
    c h = null;
    Object i = new Object();
    Object j = new Object();
    a k = null;
    b l = null;
    volatile boolean m = false;
    volatile boolean n = false;
    volatile boolean o = false;
    k p = null;
    l q = null;
    AMapLocationClient r = null;
    volatile AMapLocation s = null;
    long t = 0;
    AMapLocationClientOption u = null;
    int v = 0;
    AMapLocationListener w = new AMapLocationListener() { // from class: com.loc.j.1
        @Override // com.amap.api.location.AMapLocationListener
        public final void onLocationChanged(AMapLocation aMapLocation) {
            boolean z;
            int i;
            try {
                if (!j.this.y && j.this.o) {
                    j.this.s = aMapLocation;
                    if (aMapLocation != null) {
                        i = aMapLocation.getErrorCode();
                        if (aMapLocation.getErrorCode() == 0) {
                            j.this.t = fa.c();
                            j.this.a(5, null, 0L);
                            z = true;
                        } else {
                            j.a("定位失败", aMapLocation.getErrorCode(), aMapLocation.getErrorInfo(), "locationDetail:" + aMapLocation.getLocationDetail());
                            z = false;
                        }
                    } else {
                        z = false;
                        i = 8;
                    }
                    if (z) {
                        j.this.v = 0;
                        j.this.a(6, null, 0L);
                        return;
                    }
                    Bundle bundle = new Bundle();
                    if (!j.this.m) {
                        j.this.a(7);
                        bundle.putLong("interval", 2000L);
                        j.this.a(8, bundle, 2000L);
                    }
                    j.this.v++;
                    if (j.this.v >= 3) {
                        bundle.putInt(GeoFence.BUNDLE_KEY_LOCERRORCODE, i);
                        j.this.a(1002, bundle);
                    }
                }
            } catch (Throwable unused) {
            }
        }
    };
    final int x = 3;
    volatile boolean y = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: GeoFenceManager.java */
    /* loaded from: classes.dex */
    public class a extends Handler {
        public a(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            String str;
            String str2;
            GeoFence a;
            try {
                int i = 1;
                switch (message.what) {
                    case 0:
                        j jVar = j.this;
                        Bundle data = message.getData();
                        try {
                            ArrayList<? extends Parcelable> arrayList = new ArrayList<>();
                            if (data == null || data.isEmpty()) {
                                str = "";
                            } else {
                                DPoint dPoint = (DPoint) data.getParcelable("centerPoint");
                                str = data.getString(GeoFence.BUNDLE_KEY_CUSTOMID);
                                if (dPoint != null) {
                                    if (dPoint.getLatitude() <= 90.0d && dPoint.getLatitude() >= -90.0d && dPoint.getLongitude() <= 180.0d && dPoint.getLongitude() >= -180.0d) {
                                        GeoFence a2 = jVar.a(data, false);
                                        i = jVar.a(a2);
                                        if (i == 0) {
                                            arrayList.add(a2);
                                        }
                                    }
                                    j.a("添加围栏失败", 1, "经纬度错误，传入的纬度：" + dPoint.getLatitude() + "传入的经度:" + dPoint.getLongitude(), new String[0]);
                                }
                            }
                            Bundle bundle = new Bundle();
                            bundle.putInt(MyLocationStyle.ERROR_CODE, i);
                            bundle.putParcelableArrayList("resultList", arrayList);
                            bundle.putString(GeoFence.BUNDLE_KEY_CUSTOMID, str);
                            jVar.a(1000, bundle);
                            return;
                        } catch (Throwable th) {
                            es.a(th, "GeoFenceManager", "doAddGeoFenceRound");
                            return;
                        }
                    case 1:
                        j jVar2 = j.this;
                        Bundle data2 = message.getData();
                        try {
                            ArrayList<? extends Parcelable> arrayList2 = new ArrayList<>();
                            if (data2 == null || data2.isEmpty()) {
                                str2 = "";
                            } else {
                                ArrayList parcelableArrayList = data2.getParcelableArrayList("pointList");
                                str2 = data2.getString(GeoFence.BUNDLE_KEY_CUSTOMID);
                                if (parcelableArrayList != null && parcelableArrayList.size() > 2 && (i = jVar2.a((a = jVar2.a(data2, true)))) == 0) {
                                    arrayList2.add(a);
                                }
                            }
                            Bundle bundle2 = new Bundle();
                            bundle2.putString(GeoFence.BUNDLE_KEY_CUSTOMID, str2);
                            bundle2.putInt(MyLocationStyle.ERROR_CODE, i);
                            bundle2.putParcelableArrayList("resultList", arrayList2);
                            jVar2.a(1000, bundle2);
                            return;
                        } catch (Throwable th2) {
                            es.a(th2, "GeoFenceManager", "doAddGeoFencePolygon");
                            return;
                        }
                    case 2:
                        j.this.c(message.getData());
                        return;
                    case 3:
                        j.this.b(message.getData());
                        return;
                    case 4:
                        j.this.d(message.getData());
                        return;
                    case 5:
                        j.this.d();
                        return;
                    case 6:
                        j.this.a(j.this.s);
                        return;
                    case 7:
                        j jVar3 = j.this;
                        try {
                            if (jVar3.r != null) {
                                jVar3.c();
                                jVar3.u.setOnceLocation(true);
                                jVar3.r.setLocationOption(jVar3.u);
                                jVar3.r.startLocation();
                                return;
                            }
                            return;
                        } catch (Throwable th3) {
                            es.a(th3, "GeoFenceManager", "doStartOnceLocation");
                            return;
                        }
                    case 8:
                        j jVar4 = j.this;
                        Bundle data3 = message.getData();
                        try {
                            if (jVar4.r != null) {
                                long j = 2000;
                                if (data3 != null && !data3.isEmpty()) {
                                    j = data3.getLong("interval", 2000L);
                                }
                                jVar4.u.setOnceLocation(false);
                                jVar4.u.setInterval(j);
                                jVar4.r.setLocationOption(jVar4.u);
                                if (jVar4.m) {
                                    return;
                                }
                                jVar4.r.stopLocation();
                                jVar4.r.startLocation();
                                jVar4.m = true;
                                return;
                            }
                            return;
                        } catch (Throwable th4) {
                            es.a(th4, "GeoFenceManager", "doStartContinueLocation");
                            return;
                        }
                    case 9:
                        j.this.a(message.getData());
                        return;
                    case 10:
                        j.this.a();
                        return;
                    case 11:
                        j.this.f(message.getData());
                        return;
                    case 12:
                        j.this.e(message.getData());
                        return;
                    case 13:
                        j.this.e();
                        return;
                    default:
                        return;
                }
            } catch (Throwable unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: GeoFenceManager.java */
    /* loaded from: classes.dex */
    public static class b extends HandlerThread {
        public b(String str) {
            super(str);
        }

        @Override // android.os.HandlerThread, java.lang.Thread, java.lang.Runnable
        public final void run() {
            try {
                super.run();
            } catch (Throwable unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: GeoFenceManager.java */
    /* loaded from: classes.dex */
    public class c extends Handler {
        public c() {
        }

        public c(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            try {
                Bundle data = message.getData();
                switch (message.what) {
                    case 1000:
                        j jVar = j.this;
                        if (data != null) {
                            try {
                                if (data.isEmpty()) {
                                    return;
                                }
                                int i = data.getInt(MyLocationStyle.ERROR_CODE);
                                ArrayList parcelableArrayList = data.getParcelableArrayList("resultList");
                                if (parcelableArrayList == null) {
                                    parcelableArrayList = new ArrayList();
                                }
                                String string = data.getString(GeoFence.BUNDLE_KEY_CUSTOMID);
                                if (string == null) {
                                    string = "";
                                }
                                if (jVar.e != null) {
                                    jVar.e.onGeoFenceCreateFinished((ArrayList) parcelableArrayList.clone(), i, string);
                                }
                                if (i == 0) {
                                    jVar.b();
                                    return;
                                }
                                return;
                            } catch (Throwable th) {
                                es.a(th, "GeoFenceManager", "resultAddGeoFenceFinished");
                                return;
                            }
                        }
                        return;
                    case 1001:
                        try {
                            j.this.b((GeoFence) data.getParcelable("geoFence"));
                            return;
                        } catch (Throwable th2) {
                            th2.printStackTrace();
                            return;
                        }
                    case 1002:
                        try {
                            j.this.b(data.getInt(GeoFence.BUNDLE_KEY_LOCERRORCODE));
                            return;
                        } catch (Throwable th3) {
                            th3.printStackTrace();
                            return;
                        }
                    default:
                        return;
                }
            } catch (Throwable unused) {
            }
        }
    }

    public j(Context context) {
        this.b = null;
        try {
            this.b = context.getApplicationContext();
            f();
        } catch (Throwable th) {
            es.a(th, "GeoFenceManger", "<init>");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float a(DPoint dPoint, List<DPoint> list) {
        float f = Float.MAX_VALUE;
        if (dPoint != null && list != null && !list.isEmpty()) {
            Iterator<DPoint> it = list.iterator();
            while (it.hasNext()) {
                f = Math.min(f, fa.a(dPoint, it.next()));
            }
        }
        return f;
    }

    private int a(List<GeoFence> list) {
        try {
            if (this.g == null) {
                this.g = new ArrayList<>();
            }
            Iterator<GeoFence> it = list.iterator();
            while (it.hasNext()) {
                a(it.next());
            }
            return 0;
        } catch (Throwable th) {
            es.a(th, "GeoFenceManager", "addGeoFenceList");
            a("添加围栏失败", 8, th.getMessage(), new String[0]);
            return 8;
        }
    }

    private static Bundle a(GeoFence geoFence, String str, String str2, int i, int i2) {
        Bundle bundle = new Bundle();
        if (str == null) {
            str = "";
        }
        bundle.putString(GeoFence.BUNDLE_KEY_FENCEID, str);
        bundle.putString(GeoFence.BUNDLE_KEY_CUSTOMID, str2);
        bundle.putInt("event", i);
        bundle.putInt(GeoFence.BUNDLE_KEY_LOCERRORCODE, i2);
        bundle.putParcelable(GeoFence.BUNDLE_KEY_FENCE, geoFence);
        return bundle;
    }

    static void a(String str, int i, String str2, String... strArr) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("===========================================\n");
        stringBuffer.append("              " + str + "                ");
        stringBuffer.append("\n");
        stringBuffer.append("-------------------------------------------\n");
        stringBuffer.append("errorCode:" + i);
        stringBuffer.append("\n");
        stringBuffer.append("错误信息:" + str2);
        stringBuffer.append("\n");
        if (strArr.length > 0) {
            for (String str3 : strArr) {
                stringBuffer.append(str3);
                stringBuffer.append("\n");
            }
        }
        stringBuffer.append("===========================================\n");
        Log.i("fenceErrLog", stringBuffer.toString());
    }

    private static boolean a(GeoFence geoFence, int i) {
        boolean z = false;
        if ((i & 1) == 1) {
            try {
                if (geoFence.getStatus() == 1) {
                    z = true;
                }
            } catch (Throwable th) {
                es.a(th, "Utils", "remindStatus");
                return z;
            }
        }
        if ((i & 2) == 2 && geoFence.getStatus() == 2) {
            z = true;
        }
        if ((i & 4) != 4) {
            return z;
        }
        if (geoFence.getStatus() == 3) {
            return true;
        }
        return z;
    }

    /* JADX WARN: Code restructure failed: missing block: B:42:0x008a, code lost:
    
        if (r10 != false) goto L38;
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:11:0x001f. Please report as an issue. */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static boolean a(com.amap.api.location.AMapLocation r10, com.amap.api.fence.GeoFence r11) {
        /*
            r0 = 1
            r1 = 0
            boolean r2 = com.loc.fa.a(r10)     // Catch: java.lang.Throwable -> L8f
            if (r2 == 0) goto L8d
            if (r11 == 0) goto L8d
            java.util.List r2 = r11.getPointList()     // Catch: java.lang.Throwable -> L8f
            if (r2 == 0) goto L8d
            java.util.List r2 = r11.getPointList()     // Catch: java.lang.Throwable -> L8f
            boolean r2 = r2.isEmpty()     // Catch: java.lang.Throwable -> L8f
            if (r2 != 0) goto L8d
            int r2 = r11.getType()     // Catch: java.lang.Throwable -> L8f
            r3 = 3
            switch(r2) {
                case 0: goto L5b;
                case 1: goto L24;
                case 2: goto L5b;
                case 3: goto L24;
                default: goto L22;
            }     // Catch: java.lang.Throwable -> L8f
        L22:
            goto L8d
        L24:
            java.util.List r11 = r11.getPointList()     // Catch: java.lang.Throwable -> L8f
            java.util.Iterator r11 = r11.iterator()     // Catch: java.lang.Throwable -> L8f
            r2 = 0
        L2d:
            boolean r4 = r11.hasNext()     // Catch: java.lang.Throwable -> L58
            if (r4 == 0) goto L56
            java.lang.Object r4 = r11.next()     // Catch: java.lang.Throwable -> L58
            java.util.List r4 = (java.util.List) r4     // Catch: java.lang.Throwable -> L58
            int r5 = r4.size()     // Catch: java.lang.Throwable -> L58
            if (r5 >= r3) goto L41
            r4 = 0
            goto L52
        L41:
            com.amap.api.location.DPoint r5 = new com.amap.api.location.DPoint     // Catch: java.lang.Throwable -> L58
            double r6 = r10.getLatitude()     // Catch: java.lang.Throwable -> L58
            double r8 = r10.getLongitude()     // Catch: java.lang.Throwable -> L58
            r5.<init>(r6, r8)     // Catch: java.lang.Throwable -> L58
            boolean r4 = com.loc.es.a(r5, r4)     // Catch: java.lang.Throwable -> L58
        L52:
            if (r4 == 0) goto L2d
            r2 = 1
            goto L2d
        L56:
            r0 = r2
            goto L98
        L58:
            r10 = move-exception
            r0 = r2
            goto L91
        L5b:
            com.amap.api.location.DPoint r2 = r11.getCenter()     // Catch: java.lang.Throwable -> L8f
            float r11 = r11.getRadius()     // Catch: java.lang.Throwable -> L8f
            r4 = 4
            double[] r4 = new double[r4]     // Catch: java.lang.Throwable -> L8f
            double r5 = r2.getLatitude()     // Catch: java.lang.Throwable -> L8f
            r4[r1] = r5     // Catch: java.lang.Throwable -> L8f
            double r5 = r2.getLongitude()     // Catch: java.lang.Throwable -> L8f
            r4[r0] = r5     // Catch: java.lang.Throwable -> L8f
            r2 = 2
            double r5 = r10.getLatitude()     // Catch: java.lang.Throwable -> L8f
            r4[r2] = r5     // Catch: java.lang.Throwable -> L8f
            double r5 = r10.getLongitude()     // Catch: java.lang.Throwable -> L8f
            r4[r3] = r5     // Catch: java.lang.Throwable -> L8f
            float r10 = com.loc.fa.a(r4)     // Catch: java.lang.Throwable -> L8f
            int r10 = (r10 > r11 ? 1 : (r10 == r11 ? 0 : -1))
            if (r10 > 0) goto L89
            r10 = 1
            goto L8a
        L89:
            r10 = 0
        L8a:
            if (r10 == 0) goto L8d
            goto L98
        L8d:
            r0 = 0
            goto L98
        L8f:
            r10 = move-exception
            r0 = 0
        L91:
            java.lang.String r11 = "Utils"
            java.lang.String r1 = "isInGeoFence"
            com.loc.es.a(r10, r11, r1)
        L98:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.j.a(com.amap.api.location.AMapLocation, com.amap.api.fence.GeoFence):boolean");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float b(DPoint dPoint, List<DPoint> list) {
        float f = Float.MIN_VALUE;
        if (dPoint != null && list != null && !list.isEmpty()) {
            Iterator<DPoint> it = list.iterator();
            while (it.hasNext()) {
                f = Math.max(f, fa.a(dPoint, it.next()));
            }
        }
        return f;
    }

    private static DPoint b(List<DPoint> list) {
        DPoint dPoint = new DPoint();
        if (list == null) {
            return dPoint;
        }
        try {
            double d = 0.0d;
            double d2 = 0.0d;
            for (DPoint dPoint2 : list) {
                d += dPoint2.getLatitude();
                d2 += dPoint2.getLongitude();
            }
            double size = list.size();
            Double.isNaN(size);
            double c2 = fa.c(d / size);
            double size2 = list.size();
            Double.isNaN(size2);
            return new DPoint(c2, fa.c(d2 / size2));
        } catch (Throwable th) {
            es.a(th, "GeoFenceUtil", "getPolygonCenter");
            return dPoint;
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:83:0x0058. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:11:0x00c7 A[Catch: all -> 0x01da, Throwable -> 0x01de, TRY_LEAVE, TryCatch #2 {Throwable -> 0x01de, blocks: (B:3:0x000c, B:5:0x0013, B:7:0x0019, B:11:0x00c7, B:34:0x01c2, B:43:0x017c, B:60:0x01b1, B:83:0x0058, B:88:0x005f, B:90:0x006c, B:92:0x0079, B:94:0x0086, B:96:0x0093, B:97:0x00bd), top: B:2:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x01a0 A[Catch: all -> 0x01ba, Throwable -> 0x01bd, TRY_ENTER, TRY_LEAVE, TryCatch #1 {Throwable -> 0x01bd, blocks: (B:14:0x00dc, B:22:0x014e, B:25:0x0157, B:28:0x015e, B:31:0x016b, B:41:0x0176, B:58:0x01a0, B:67:0x00eb, B:68:0x00f4, B:69:0x012d), top: B:13:0x00dc }] */
    /* JADX WARN: Removed duplicated region for block: B:82:0x01c0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void b(int r25, android.os.Bundle r26) {
        /*
            Method dump skipped, instructions count: 590
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.j.b(int, android.os.Bundle):void");
    }

    private static boolean b(AMapLocation aMapLocation, GeoFence geoFence) {
        boolean z = true;
        try {
            if (!a(aMapLocation, geoFence)) {
                if (geoFence.getStatus() != 2) {
                    try {
                        geoFence.setStatus(2);
                        geoFence.setEnterTime(-1L);
                    } catch (Throwable th) {
                        th = th;
                        es.a(th, "Utils", "isFenceStatusChanged");
                        return z;
                    }
                }
                z = false;
            } else if (geoFence.getEnterTime() == -1) {
                if (geoFence.getStatus() != 1) {
                    geoFence.setEnterTime(fa.c());
                    geoFence.setStatus(1);
                }
                z = false;
            } else {
                if (geoFence.getStatus() != 3 && fa.c() - geoFence.getEnterTime() > 600000) {
                    geoFence.setStatus(3);
                }
                z = false;
            }
        } catch (Throwable th2) {
            th = th2;
            z = false;
        }
        return z;
    }

    private void f() {
        if (!this.o) {
            this.o = true;
        }
        if (this.n) {
            return;
        }
        try {
            this.h = Looper.myLooper() == null ? new c(this.b.getMainLooper()) : new c();
        } catch (Throwable th) {
            es.a(th, "GeoFenceManger", "init 1");
        }
        try {
            this.l = new b("fenceActionThread");
            this.l.setPriority(5);
            this.l.start();
            this.k = new a(this.l.getLooper());
        } catch (Throwable th2) {
            es.a(th2, "GeoFenceManger", "init 2");
        }
        try {
            this.p = new k(this.b);
            this.q = new l();
            this.u = new AMapLocationClientOption();
            this.r = new AMapLocationClient(this.b);
            this.u.setLocationCacheEnable(true);
            this.u.setNeedAddress(false);
            this.r.setLocationListener(this.w);
            if (this.a == null) {
                this.a = new ey();
            }
        } catch (Throwable th3) {
            es.a(th3, "GeoFenceManger", "initBase");
        }
        this.n = true;
        try {
            if (this.d == null || this.c != null) {
                return;
            }
            createPendingIntent(this.d);
        } catch (Throwable th4) {
            es.a(th4, "GeoFenceManger", "init 4");
        }
    }

    final int a(GeoFence geoFence) {
        try {
            if (this.g == null) {
                this.g = new ArrayList<>();
            }
            if (this.g.contains(geoFence)) {
                return 17;
            }
            this.g.add(geoFence);
            return 0;
        } catch (Throwable th) {
            es.a(th, "GeoFenceManager", "addGeoFence2List");
            a("添加围栏失败", 8, th.getMessage(), new String[0]);
            return 8;
        }
    }

    final GeoFence a(Bundle bundle, boolean z) {
        GeoFence geoFence = new GeoFence();
        ArrayList arrayList = new ArrayList();
        DPoint dPoint = new DPoint();
        if (z) {
            geoFence.setType(1);
            arrayList = bundle.getParcelableArrayList("pointList");
            if (arrayList != null) {
                dPoint = b(arrayList);
            }
            geoFence.setMaxDis2Center(b(dPoint, arrayList));
            geoFence.setMinDis2Center(a(dPoint, arrayList));
        } else {
            geoFence.setType(0);
            dPoint = (DPoint) bundle.getParcelable("centerPoint");
            if (dPoint != null) {
                arrayList.add(dPoint);
            }
            float f = bundle.getFloat("fenceRadius", 1000.0f);
            if (f <= 0.0f) {
                f = 1000.0f;
            }
            geoFence.setRadius(f);
            geoFence.setMinDis2Center(f);
            geoFence.setMaxDis2Center(f);
        }
        geoFence.setActivatesAction(this.f);
        geoFence.setCustomId(bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID));
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(arrayList);
        geoFence.setPointList(arrayList2);
        geoFence.setCenter(dPoint);
        geoFence.setPendingIntentAction(this.d);
        geoFence.setExpiration(-1L);
        geoFence.setPendingIntent(this.c);
        StringBuilder sb = new StringBuilder();
        sb.append(l.a());
        geoFence.setFenceId(sb.toString());
        if (this.a != null) {
            this.a.a(this.b, 2);
        }
        return geoFence;
    }

    final void a() {
        if (this.n) {
            if (this.g != null) {
                this.g.clear();
                this.g = null;
            }
            if (this.o) {
                return;
            }
            try {
                synchronized (this.i) {
                    if (this.k != null) {
                        this.k.removeCallbacksAndMessages(null);
                    }
                    this.k = null;
                }
            } catch (Throwable th) {
                es.a(th, "GeoFenceManager", "destroyActionHandler");
            }
            if (this.r != null) {
                this.r.stopLocation();
                this.r.onDestroy();
            }
            this.r = null;
            if (this.l != null) {
                if (Build.VERSION.SDK_INT >= 18) {
                    this.l.quitSafely();
                } else {
                    this.l.quit();
                }
            }
            this.l = null;
            this.p = null;
            synchronized (this.z) {
                if (this.c != null) {
                    this.c.cancel();
                }
                this.c = null;
            }
            try {
                synchronized (this.j) {
                    if (this.h != null) {
                        this.h.removeCallbacksAndMessages(null);
                    }
                    this.h = null;
                }
            } catch (Throwable th2) {
                es.a(th2, "GeoFenceManager", "destroyResultHandler");
            }
            if (this.a != null) {
                this.a.b(this.b);
            }
            this.m = false;
            this.n = false;
        }
    }

    final void a(int i) {
        try {
            synchronized (this.i) {
                if (this.k != null) {
                    this.k.removeMessages(i);
                }
            }
        } catch (Throwable th) {
            es.a(th, "GeoFenceManager", "removeActionHandlerMessage");
        }
    }

    final void a(int i, Bundle bundle) {
        try {
            synchronized (this.j) {
                if (this.h != null) {
                    Message obtainMessage = this.h.obtainMessage();
                    obtainMessage.what = i;
                    obtainMessage.setData(bundle);
                    this.h.sendMessage(obtainMessage);
                }
            }
        } catch (Throwable th) {
            es.a(th, "GeoFenceManager", "sendResultHandlerMessage");
        }
    }

    final void a(int i, Bundle bundle, long j) {
        try {
            synchronized (this.i) {
                if (this.k != null) {
                    Message obtainMessage = this.k.obtainMessage();
                    obtainMessage.what = i;
                    obtainMessage.setData(bundle);
                    this.k.sendMessageDelayed(obtainMessage, j);
                }
            }
        } catch (Throwable th) {
            es.a(th, "GeoFenceManager", "sendActionHandlerMessage");
        }
    }

    final void a(Bundle bundle) {
        int i = 1;
        if (bundle != null) {
            try {
                i = bundle.getInt("activatesAction", 1);
            } catch (Throwable th) {
                es.a(th, "GeoFenceManager", "doSetActivatesAction");
                return;
            }
        }
        if (this.f != i) {
            if (this.g != null && !this.g.isEmpty()) {
                Iterator<GeoFence> it = this.g.iterator();
                while (it.hasNext()) {
                    GeoFence next = it.next();
                    next.setStatus(0);
                    next.setEnterTime(-1L);
                }
            }
            b();
        }
        this.f = i;
    }

    final void a(AMapLocation aMapLocation) {
        try {
            if (this.y || this.g == null || this.g.isEmpty() || aMapLocation == null || aMapLocation.getErrorCode() != 0) {
                return;
            }
            Iterator<GeoFence> it = this.g.iterator();
            while (it.hasNext()) {
                GeoFence next = it.next();
                if (next.isAble() && b(aMapLocation, next) && a(next, this.f)) {
                    next.setCurrentLocation(aMapLocation);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("geoFence", next);
                    a(1001, bundle);
                }
            }
        } catch (Throwable th) {
            es.a(th, "GeoFenceManager", "doCheckFence");
        }
    }

    @Override // com.amap.api.fence.GeoFenceManagerBase
    public final void addDistrictGeoFence(String str, String str2) {
        try {
            f();
            Bundle bundle = new Bundle();
            bundle.putString("keyWords", str);
            bundle.putString(GeoFence.BUNDLE_KEY_CUSTOMID, str2);
            a(4, bundle, 0L);
        } catch (Throwable th) {
            es.a(th, "GeoFenceManager", "addDistricetGeoFence");
        }
    }

    @Override // com.amap.api.fence.GeoFenceManagerBase
    public final void addKeywordGeoFence(String str, String str2, String str3, int i, String str4) {
        try {
            f();
            if (i <= 0) {
                i = 10;
            }
            if (i > 25) {
                i = 25;
            }
            Bundle bundle = new Bundle();
            bundle.putString("keyWords", str);
            bundle.putString("poiType", str2);
            bundle.putString("city", str3);
            bundle.putInt("searchSize", i);
            bundle.putString(GeoFence.BUNDLE_KEY_CUSTOMID, str4);
            a(2, bundle, 0L);
        } catch (Throwable th) {
            es.a(th, "GeoFenceManager", "addKeywordGeoFence");
        }
    }

    @Override // com.amap.api.fence.GeoFenceManagerBase
    public final void addNearbyGeoFence(String str, String str2, DPoint dPoint, float f, int i, String str3) {
        try {
            f();
            if (f <= 0.0f || f > 50000.0f) {
                f = 3000.0f;
            }
            if (i <= 0) {
                i = 10;
            }
            if (i > 25) {
                i = 25;
            }
            Bundle bundle = new Bundle();
            bundle.putString("keyWords", str);
            bundle.putString("poiType", str2);
            bundle.putParcelable("centerPoint", dPoint);
            bundle.putFloat("aroundRadius", f);
            bundle.putInt("searchSize", i);
            bundle.putString(GeoFence.BUNDLE_KEY_CUSTOMID, str3);
            a(3, bundle, 0L);
        } catch (Throwable th) {
            es.a(th, "GeoFenceManager", "addNearbyGeoFence");
        }
    }

    @Override // com.amap.api.fence.GeoFenceManagerBase
    public final void addPolygonGeoFence(List<DPoint> list, String str) {
        try {
            f();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("pointList", new ArrayList<>(list));
            bundle.putString(GeoFence.BUNDLE_KEY_CUSTOMID, str);
            a(1, bundle, 0L);
        } catch (Throwable th) {
            es.a(th, "GeoFenceManager", "addPolygonGeoFence");
        }
    }

    @Override // com.amap.api.fence.GeoFenceManagerBase
    public final void addRoundGeoFence(DPoint dPoint, float f, String str) {
        try {
            f();
            Bundle bundle = new Bundle();
            bundle.putParcelable("centerPoint", dPoint);
            bundle.putFloat("fenceRadius", f);
            bundle.putString(GeoFence.BUNDLE_KEY_CUSTOMID, str);
            a(0, bundle, 0L);
        } catch (Throwable th) {
            es.a(th, "GeoFenceManager", "addRoundGeoFence");
        }
    }

    final void b() {
        if (this.y || this.k == null) {
            return;
        }
        boolean z = false;
        if (this.s != null && fa.a(this.s) && fa.c() - this.t < 10000) {
            z = true;
        }
        if (z) {
            a(6, null, 0L);
            a(5, null, 0L);
        } else {
            a(7);
            a(7, null, 0L);
        }
    }

    final void b(int i) {
        try {
            if (this.b != null) {
                synchronized (this.z) {
                    if (this.c == null) {
                        return;
                    }
                    Intent intent = new Intent();
                    intent.putExtras(a(null, null, null, 4, i));
                    this.c.send(this.b, 0, intent);
                }
            }
        } catch (Throwable th) {
            es.a(th, "GeoFenceManager", "resultRemindLocationError");
        }
    }

    final void b(Bundle bundle) {
        b(2, bundle);
    }

    final void b(GeoFence geoFence) {
        PendingIntent pendingIntent;
        Context context;
        try {
            synchronized (this.z) {
                if (this.b != null) {
                    if (this.c == null && geoFence.getPendingIntent() == null) {
                        return;
                    }
                    Intent intent = new Intent();
                    intent.putExtras(a(geoFence, geoFence.getFenceId(), geoFence.getCustomId(), geoFence.getStatus(), 0));
                    if (this.d != null) {
                        intent.setAction(this.d);
                    }
                    intent.setPackage(u.c(this.b));
                    if (geoFence.getPendingIntent() != null) {
                        pendingIntent = geoFence.getPendingIntent();
                        context = this.b;
                    } else {
                        pendingIntent = this.c;
                        context = this.b;
                    }
                    pendingIntent.send(context, 0, intent);
                }
            }
        } catch (Throwable th) {
            es.a(th, "GeoFenceManager", "resultTriggerGeoFence");
        }
    }

    final void c() {
        try {
            if (this.m) {
                a(8);
            }
            if (this.r != null) {
                this.r.stopLocation();
            }
            this.m = false;
        } catch (Throwable unused) {
        }
    }

    final void c(Bundle bundle) {
        b(1, bundle);
    }

    @Override // com.amap.api.fence.GeoFenceManagerBase
    public final PendingIntent createPendingIntent(String str) {
        synchronized (this.z) {
            try {
                Intent intent = new Intent(str);
                intent.setPackage(u.c(this.b));
                this.c = PendingIntent.getBroadcast(this.b, 0, intent, 0);
                this.d = str;
                if (this.g != null && !this.g.isEmpty()) {
                    Iterator<GeoFence> it = this.g.iterator();
                    while (it.hasNext()) {
                        GeoFence next = it.next();
                        next.setPendingIntent(this.c);
                        next.setPendingIntentAction(this.d);
                    }
                }
            } catch (Throwable th) {
                es.a(th, "GeoFenceManager", "createPendingIntent");
            }
        }
        return this.c;
    }

    final void d() {
        float f;
        try {
            if (!this.y && fa.a(this.s)) {
                AMapLocation aMapLocation = this.s;
                ArrayList<GeoFence> arrayList = this.g;
                if (aMapLocation != null && aMapLocation.getErrorCode() == 0 && arrayList != null && !arrayList.isEmpty()) {
                    DPoint dPoint = new DPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    Iterator<GeoFence> it = arrayList.iterator();
                    float f2 = Float.MAX_VALUE;
                    while (true) {
                        if (!it.hasNext()) {
                            f = f2;
                            break;
                        }
                        GeoFence next = it.next();
                        if (next.isAble()) {
                            float a2 = fa.a(dPoint, next.getCenter());
                            if (a2 > next.getMinDis2Center() && a2 < next.getMaxDis2Center()) {
                                f = 0.0f;
                                break;
                            }
                            if (a2 > next.getMaxDis2Center()) {
                                f2 = Math.min(f2, a2 - next.getMaxDis2Center());
                            }
                            if (a2 < next.getMinDis2Center()) {
                                f2 = Math.min(f2, next.getMinDis2Center() - a2);
                            }
                        }
                    }
                } else {
                    f = Float.MAX_VALUE;
                }
                if (f == Float.MAX_VALUE) {
                    return;
                }
                if (f < 1000.0f) {
                    a(7);
                    Bundle bundle = new Bundle();
                    bundle.putLong("interval", 2000L);
                    a(8, bundle, 500L);
                    return;
                }
                if (f < 5000.0f) {
                    c();
                    a(7);
                    a(7, null, 10000L);
                } else {
                    c();
                    a(7);
                    a(7, null, ((f - 4000.0f) / 100.0f) * 1000.0f);
                }
            }
        } catch (Throwable th) {
            es.a(th, "GeoFenceManager", "doCheckLocationPolicy");
        }
    }

    final void d(Bundle bundle) {
        b(3, bundle);
    }

    final void e() {
        try {
            a(7);
            a(8);
            if (this.r != null) {
                this.r.stopLocation();
            }
            this.m = false;
        } catch (Throwable th) {
            es.a(th, "GeoFenceManager", "doPauseGeoFence");
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:40:0x0074, code lost:
    
        e();
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0077, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    final void e(android.os.Bundle r6) {
        /*
            r5 = this;
            if (r6 == 0) goto L84
            boolean r0 = r6.isEmpty()     // Catch: java.lang.Throwable -> L7c
            if (r0 == 0) goto La
            goto L84
        La:
            java.lang.String r0 = "fid"
            java.lang.String r0 = r6.getString(r0)     // Catch: java.lang.Throwable -> L7c
            boolean r1 = android.text.TextUtils.isEmpty(r0)     // Catch: java.lang.Throwable -> L7c
            if (r1 == 0) goto L17
            return
        L17:
            java.lang.String r1 = "ab"
            r2 = 1
            boolean r6 = r6.getBoolean(r1, r2)     // Catch: java.lang.Throwable -> L7c
            java.util.ArrayList<com.amap.api.fence.GeoFence> r1 = r5.g     // Catch: java.lang.Throwable -> L7c
            if (r1 == 0) goto L49
            java.util.ArrayList<com.amap.api.fence.GeoFence> r1 = r5.g     // Catch: java.lang.Throwable -> L7c
            boolean r1 = r1.isEmpty()     // Catch: java.lang.Throwable -> L7c
            if (r1 != 0) goto L49
            java.util.ArrayList<com.amap.api.fence.GeoFence> r1 = r5.g     // Catch: java.lang.Throwable -> L7c
            java.util.Iterator r1 = r1.iterator()     // Catch: java.lang.Throwable -> L7c
        L30:
            boolean r3 = r1.hasNext()     // Catch: java.lang.Throwable -> L7c
            if (r3 == 0) goto L49
            java.lang.Object r3 = r1.next()     // Catch: java.lang.Throwable -> L7c
            com.amap.api.fence.GeoFence r3 = (com.amap.api.fence.GeoFence) r3     // Catch: java.lang.Throwable -> L7c
            java.lang.String r4 = r3.getFenceId()     // Catch: java.lang.Throwable -> L7c
            boolean r4 = r4.equals(r0)     // Catch: java.lang.Throwable -> L7c
            if (r4 == 0) goto L30
            r3.setAble(r6)     // Catch: java.lang.Throwable -> L7c
        L49:
            if (r6 != 0) goto L78
            java.util.ArrayList<com.amap.api.fence.GeoFence> r6 = r5.g     // Catch: java.lang.Throwable -> L7c
            if (r6 == 0) goto L72
            java.util.ArrayList<com.amap.api.fence.GeoFence> r6 = r5.g     // Catch: java.lang.Throwable -> L7c
            boolean r6 = r6.isEmpty()     // Catch: java.lang.Throwable -> L7c
            if (r6 == 0) goto L58
            goto L72
        L58:
            java.util.ArrayList<com.amap.api.fence.GeoFence> r6 = r5.g     // Catch: java.lang.Throwable -> L7c
            java.util.Iterator r6 = r6.iterator()     // Catch: java.lang.Throwable -> L7c
        L5e:
            boolean r0 = r6.hasNext()     // Catch: java.lang.Throwable -> L7c
            if (r0 == 0) goto L72
            java.lang.Object r0 = r6.next()     // Catch: java.lang.Throwable -> L7c
            com.amap.api.fence.GeoFence r0 = (com.amap.api.fence.GeoFence) r0     // Catch: java.lang.Throwable -> L7c
            boolean r0 = r0.isAble()     // Catch: java.lang.Throwable -> L7c
            if (r0 == 0) goto L5e
            r6 = 0
            r2 = 0
        L72:
            if (r2 == 0) goto L7b
            r5.e()     // Catch: java.lang.Throwable -> L7c
            return
        L78:
            r5.b()     // Catch: java.lang.Throwable -> L7c
        L7b:
            return
        L7c:
            r6 = move-exception
            java.lang.String r0 = "GeoFenceManager"
            java.lang.String r1 = "doSetGeoFenceAble"
            com.loc.es.a(r6, r0, r1)
        L84:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.j.e(android.os.Bundle):void");
    }

    final void f(Bundle bundle) {
        try {
            if (this.g != null) {
                GeoFence geoFence = (GeoFence) bundle.getParcelable("fc");
                if (this.g.contains(geoFence)) {
                    this.g.remove(geoFence);
                }
                if (this.g.size() <= 0) {
                    a();
                } else {
                    b();
                }
            }
        } catch (Throwable unused) {
        }
    }

    @Override // com.amap.api.fence.GeoFenceManagerBase
    public final List<GeoFence> getAllGeoFence() {
        try {
            if (this.g == null) {
                this.g = new ArrayList<>();
            }
            return (ArrayList) this.g.clone();
        } catch (Throwable unused) {
            return new ArrayList();
        }
    }

    @Override // com.amap.api.fence.GeoFenceManagerBase
    public final boolean isPause() {
        return this.y;
    }

    @Override // com.amap.api.fence.GeoFenceManagerBase
    public final void pauseGeoFence() {
        try {
            f();
            this.y = true;
            a(13, null, 0L);
        } catch (Throwable th) {
            es.a(th, "GeoFenceManager", "pauseGeoFence");
        }
    }

    @Override // com.amap.api.fence.GeoFenceManagerBase
    public final void removeGeoFence() {
        try {
            this.o = false;
            a(10, null, 0L);
        } catch (Throwable th) {
            es.a(th, "GeoFenceManager", "removeGeoFence");
        }
    }

    @Override // com.amap.api.fence.GeoFenceManagerBase
    public final boolean removeGeoFence(GeoFence geoFence) {
        try {
            if (this.g != null && !this.g.isEmpty()) {
                if (!this.g.contains(geoFence)) {
                    return false;
                }
                if (this.g.size() == 1) {
                    this.o = false;
                }
                Bundle bundle = new Bundle();
                bundle.putParcelable("fc", geoFence);
                a(11, bundle, 0L);
                return true;
            }
            this.o = false;
            a(10, null, 0L);
            return true;
        } catch (Throwable th) {
            es.a(th, "GeoFenceManager", "removeGeoFence(GeoFence)");
            return false;
        }
    }

    @Override // com.amap.api.fence.GeoFenceManagerBase
    public final void resumeGeoFence() {
        try {
            f();
            if (this.y) {
                this.y = false;
                b();
            }
        } catch (Throwable th) {
            es.a(th, "GeoFenceManager", "resumeGeoFence");
        }
    }

    @Override // com.amap.api.fence.GeoFenceManagerBase
    public final void setActivateAction(int i) {
        try {
            f();
            if (i > 7 || i <= 0) {
                i = 1;
            }
            Bundle bundle = new Bundle();
            bundle.putInt("activatesAction", i);
            a(9, bundle, 0L);
        } catch (Throwable th) {
            es.a(th, "GeoFenceManager", "setActivateAction");
        }
    }

    @Override // com.amap.api.fence.GeoFenceManagerBase
    public final void setGeoFenceAble(String str, boolean z) {
        try {
            f();
            Bundle bundle = new Bundle();
            bundle.putString("fid", str);
            bundle.putBoolean("ab", z);
            a(12, bundle, 0L);
        } catch (Throwable th) {
            es.a(th, "GeoFenceManager", "setGeoFenceAble");
        }
    }

    @Override // com.amap.api.fence.GeoFenceManagerBase
    public final void setGeoFenceListener(GeoFenceListener geoFenceListener) {
        try {
            this.e = geoFenceListener;
        } catch (Throwable unused) {
        }
    }
}
