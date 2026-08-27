package com.loc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.DPoint;
import com.amap.location.common.model.AmapLoc;
import com.autonavi.amap.mapcore.tools.GlMapUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import no.nordicsemi.android.dfu.internal.scanner.BootloaderScanner;
import org.json.JSONObject;

/* compiled from: GpsLocation.java */
/* loaded from: classes.dex */
public final class p {
    static AMapLocation j;
    static long k;
    static Object l = new Object();
    static long q = 0;
    static boolean t = false;
    static boolean u = false;
    Handler a;
    LocationManager b;
    AMapLocationClientOption c;
    cu f;
    private Context z;
    private long A = 0;
    long d = 0;
    boolean e = false;
    private int B = 0;
    int g = GlMapUtil.DEVICE_DISPLAY_DPI_MEDIAN;
    int h = 80;
    AMapLocation i = null;
    long m = 0;
    float n = 0.0f;
    Object o = new Object();
    Object p = new Object();
    AMapLocationClientOption.GeoLanguage r = AMapLocationClientOption.GeoLanguage.DEFAULT;
    boolean s = true;
    long v = 0;
    int w = 0;
    LocationListener x = new LocationListener() { // from class: com.loc.p.1
        @Override // android.location.LocationListener
        public final void onLocationChanged(Location location) {
            Handler handler;
            if (p.this.a != null) {
                p.this.a.removeMessages(8);
            }
            if (location == null) {
                return;
            }
            try {
                AMapLocation aMapLocation = new AMapLocation(location);
                if (fa.a(aMapLocation)) {
                    aMapLocation.setProvider("gps");
                    aMapLocation.setLocationType(1);
                    if (!p.this.e && fa.a(aMapLocation)) {
                        ey.a(p.this.z, fa.c() - p.this.A, es.a(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                        p.this.e = true;
                    }
                    if (fa.a(location, p.this.C)) {
                        aMapLocation.setMock(true);
                        aMapLocation.setTrustedLevel(4);
                        if (!p.this.c.isMockEnable()) {
                            if (p.this.w <= 3) {
                                p.this.w++;
                                return;
                            }
                            ey.a((String) null, 2152);
                            aMapLocation.setErrorCode(15);
                            aMapLocation.setLocationDetail("GpsLocation has been mocked!#1501");
                            aMapLocation.setLatitude(0.0d);
                            aMapLocation.setLongitude(0.0d);
                            aMapLocation.setAltitude(0.0d);
                            aMapLocation.setSpeed(0.0f);
                            aMapLocation.setAccuracy(0.0f);
                            aMapLocation.setBearing(0.0f);
                            aMapLocation.setExtras(null);
                            p.this.b(aMapLocation);
                            return;
                        }
                    } else {
                        p.this.w = 0;
                    }
                    aMapLocation.setSatellites(p.this.C);
                    p.b(p.this, aMapLocation);
                    p.c(p.this, aMapLocation);
                    p.a(aMapLocation);
                    AMapLocation d = p.d(p.this, aMapLocation);
                    p.e(p.this, d);
                    p pVar = p.this;
                    if (fa.a(d) && pVar.a != null && pVar.c.isNeedAddress()) {
                        long c = fa.c();
                        if (pVar.c.getInterval() <= 8000 || c - pVar.v > pVar.c.getInterval() - 8000) {
                            Bundle bundle = new Bundle();
                            bundle.putDouble("lat", d.getLatitude());
                            bundle.putDouble("lon", d.getLongitude());
                            Message obtain = Message.obtain();
                            obtain.setData(bundle);
                            obtain.what = 5;
                            synchronized (pVar.o) {
                                if (pVar.y == null) {
                                    handler = pVar.a;
                                } else if (fa.a(d, pVar.y) > pVar.h) {
                                    handler = pVar.a;
                                }
                                handler.sendMessage(obtain);
                            }
                        }
                    }
                    synchronized (p.this.o) {
                        p.a(p.this, d, p.this.y);
                    }
                    try {
                        if (fa.a(d)) {
                            if (p.this.i != null) {
                                p.this.m = location.getTime() - p.this.i.getTime();
                                p.this.n = fa.a(p.this.i, d);
                            }
                            synchronized (p.this.p) {
                                p.this.i = d.m9clone();
                            }
                            p.d(p.this);
                            p.e(p.this);
                            p.f(p.this);
                        }
                    } catch (Throwable th) {
                        es.a(th, "GpsLocation", "onLocationChangedLast");
                    }
                    p.this.b(d);
                }
            } catch (Throwable th2) {
                es.a(th2, "GpsLocation", "onLocationChanged");
            }
        }

        @Override // android.location.LocationListener
        public final void onProviderDisabled(String str) {
            try {
                if ("gps".equalsIgnoreCase(str)) {
                    p.this.d = 0L;
                    p.this.C = 0;
                }
            } catch (Throwable unused) {
            }
        }

        @Override // android.location.LocationListener
        public final void onProviderEnabled(String str) {
        }

        @Override // android.location.LocationListener
        public final void onStatusChanged(String str, int i, Bundle bundle) {
            if (i == 0) {
                try {
                    p.this.d = 0L;
                    p.this.C = 0;
                } catch (Throwable unused) {
                }
            }
        }
    };
    private int C = 0;
    private GpsStatus D = null;
    private GpsStatus.Listener E = new GpsStatus.Listener() { // from class: com.loc.p.2
        @Override // android.location.GpsStatus.Listener
        public final void onGpsStatusChanged(int i) {
            Iterable<GpsSatellite> satellites;
            try {
            } catch (Throwable th) {
                es.a(th, "GpsLocation", "onGpsStatusChanged");
            }
            if (p.this.b == null) {
                return;
            }
            p.this.D = p.this.b.getGpsStatus(p.this.D);
            int i2 = 0;
            switch (i) {
                case 1:
                default:
                    return;
                case 2:
                    p.this.C = 0;
                    return;
                case 3:
                    return;
                case 4:
                    try {
                        if (p.this.D != null && (satellites = p.this.D.getSatellites()) != null) {
                            Iterator<GpsSatellite> it = satellites.iterator();
                            int maxSatellites = p.this.D.getMaxSatellites();
                            while (it.hasNext() && i2 < maxSatellites) {
                                if (it.next().usedInFix()) {
                                    i2++;
                                }
                            }
                        }
                    } catch (Throwable th2) {
                        es.a(th2, "GpsLocation", "GPS_EVENT_SATELLITE_STATUS");
                    }
                    p.this.C = i2;
                    return;
            }
            es.a(th, "GpsLocation", "onGpsStatusChanged");
        }
    };
    public AMapLocation y = null;
    private String F = null;
    private boolean G = false;
    private int H = 0;
    private boolean I = false;

    public p(Context context, Handler handler) {
        this.f = null;
        this.z = context;
        this.a = handler;
        try {
            this.b = (LocationManager) this.z.getSystemService("location");
        } catch (Throwable th) {
            es.a(th, "GpsLocation", "<init>");
        }
        this.f = new cu();
    }

    private void a(int i, int i2, String str, long j2) {
        try {
            if (this.a == null || this.c.getLocationMode() != AMapLocationClientOption.AMapLocationMode.Device_Sensors) {
                return;
            }
            Message obtain = Message.obtain();
            AMapLocation aMapLocation = new AMapLocation("");
            aMapLocation.setProvider("gps");
            aMapLocation.setErrorCode(i2);
            aMapLocation.setLocationDetail(str);
            aMapLocation.setLocationType(1);
            obtain.obj = aMapLocation;
            obtain.what = i;
            this.a.sendMessageDelayed(obtain, j2);
        } catch (Throwable unused) {
        }
    }

    static /* synthetic */ void a(AMapLocation aMapLocation) {
        if (fa.a(aMapLocation) && er.C()) {
            long time = aMapLocation.getTime();
            long currentTimeMillis = System.currentTimeMillis();
            long a = et.a(time, currentTimeMillis, er.D());
            if (a != time) {
                aMapLocation.setTime(a);
                ey.a(time, currentTimeMillis);
            }
        }
    }

    static /* synthetic */ void a(p pVar, AMapLocation aMapLocation, AMapLocation aMapLocation2) {
        if (aMapLocation2 == null || !pVar.c.isNeedAddress() || fa.a(aMapLocation, aMapLocation2) >= pVar.g) {
            return;
        }
        es.a(aMapLocation, aMapLocation2);
    }

    private static boolean a(LocationManager locationManager) {
        try {
            if (t) {
                return u;
            }
            List<String> allProviders = locationManager.getAllProviders();
            if (allProviders == null || allProviders.size() <= 0) {
                u = false;
            } else {
                u = allProviders.contains("gps");
            }
            t = true;
            return u;
        } catch (Throwable unused) {
            return u;
        }
    }

    private boolean a(String str) {
        try {
            ArrayList<String> d = fa.d(str);
            ArrayList<String> d2 = fa.d(this.F);
            if (d.size() < 8 || d2.size() < 8) {
                return false;
            }
            return fa.a(this.F, str);
        } catch (Throwable unused) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(AMapLocation aMapLocation) {
        if (aMapLocation.getErrorCode() != 15 || AMapLocationClientOption.AMapLocationMode.Device_Sensors.equals(this.c.getLocationMode())) {
            if (this.c.getLocationMode().equals(AMapLocationClientOption.AMapLocationMode.Device_Sensors) && this.c.getDeviceModeDistanceFilter() > 0.0f) {
                c(aMapLocation);
            } else if (fa.c() - this.v >= this.c.getInterval() - 200) {
                this.v = fa.c();
                c(aMapLocation);
            }
        }
    }

    static /* synthetic */ void b(p pVar, AMapLocation aMapLocation) {
        try {
            if (!es.a(aMapLocation.getLatitude(), aMapLocation.getLongitude()) || !pVar.c.isOffset()) {
                aMapLocation.setOffset(false);
                aMapLocation.setCoordType(AMapLocation.COORD_TYPE_WGS84);
                return;
            }
            DPoint a = eu.a(pVar.z, new DPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
            aMapLocation.setLatitude(a.getLatitude());
            aMapLocation.setLongitude(a.getLongitude());
            aMapLocation.setOffset(pVar.c.isOffset());
            aMapLocation.setCoordType(AMapLocation.COORD_TYPE_GCJ02);
        } catch (Throwable unused) {
            aMapLocation.setOffset(false);
            aMapLocation.setCoordType(AMapLocation.COORD_TYPE_WGS84);
        }
    }

    private void c(AMapLocation aMapLocation) {
        if (this.a != null) {
            Message obtain = Message.obtain();
            obtain.obj = aMapLocation;
            obtain.what = 2;
            this.a.sendMessage(obtain);
        }
    }

    static /* synthetic */ void c(p pVar, AMapLocation aMapLocation) {
        try {
            if (pVar.C >= 4) {
                aMapLocation.setGpsAccuracyStatus(1);
            } else if (pVar.C == 0) {
                aMapLocation.setGpsAccuracyStatus(-1);
            } else {
                aMapLocation.setGpsAccuracyStatus(0);
            }
        } catch (Throwable unused) {
        }
    }

    static /* synthetic */ AMapLocation d(p pVar, AMapLocation aMapLocation) {
        if (!fa.a(aMapLocation) || pVar.B < 3) {
            return aMapLocation;
        }
        if (aMapLocation.getAccuracy() < 0.0f || aMapLocation.getAccuracy() == Float.MAX_VALUE) {
            aMapLocation.setAccuracy(0.0f);
        }
        if (aMapLocation.getSpeed() < 0.0f || aMapLocation.getSpeed() == Float.MAX_VALUE) {
            aMapLocation.setSpeed(0.0f);
        }
        return pVar.f.a(aMapLocation);
    }

    static /* synthetic */ String d(p pVar) {
        pVar.F = null;
        return null;
    }

    static /* synthetic */ void e(p pVar, AMapLocation aMapLocation) {
        if (fa.a(aMapLocation)) {
            pVar.d = fa.c();
            synchronized (l) {
                k = fa.c();
                j = aMapLocation.m9clone();
            }
            pVar.B++;
        }
    }

    private static boolean e() {
        try {
            return ((Boolean) ew.a(ad.c("KY29tLmFtYXAuYXBpLm5hdmkuQU1hcE5hdmk="), ad.c("UaXNOYXZpU3RhcnRlZA=="), (Object[]) null, (Class<?>[]) null)).booleanValue();
        } catch (Throwable unused) {
            return false;
        }
    }

    static /* synthetic */ boolean e(p pVar) {
        pVar.G = false;
        return false;
    }

    static /* synthetic */ int f(p pVar) {
        pVar.H = 0;
        return 0;
    }

    private AMapLocation f() {
        float f;
        float f2;
        try {
            if (fa.a(this.i) && er.s() && e()) {
                JSONObject jSONObject = new JSONObject((String) ew.a(ad.c("KY29tLmFtYXAuYXBpLm5hdmkuQU1hcE5hdmk="), ad.c("UZ2V0TmF2aUxvY2F0aW9u"), (Object[]) null, (Class<?>[]) null));
                long optLong = jSONObject.optLong("time");
                if (!this.I) {
                    this.I = true;
                    ey.a("useNaviLoc", "use NaviLoc");
                }
                if (fa.b() - optLong <= 5500) {
                    double optDouble = jSONObject.optDouble("lat", 0.0d);
                    double optDouble2 = jSONObject.optDouble("lng", 0.0d);
                    float f3 = 0.0f;
                    try {
                        f = Float.parseFloat(jSONObject.optString("accuracy", AmapLoc.RESULT_TYPE_GPS));
                    } catch (NumberFormatException unused) {
                        f = 0.0f;
                    }
                    double optDouble3 = jSONObject.optDouble("altitude", 0.0d);
                    try {
                        f2 = Float.parseFloat(jSONObject.optString("bearing", AmapLoc.RESULT_TYPE_GPS));
                    } catch (NumberFormatException unused2) {
                        f2 = 0.0f;
                    }
                    try {
                        f3 = (Float.parseFloat(jSONObject.optString("speed", AmapLoc.RESULT_TYPE_GPS)) * 10.0f) / 36.0f;
                    } catch (NumberFormatException unused3) {
                    }
                    AMapLocation aMapLocation = new AMapLocation("lbs");
                    aMapLocation.setLocationType(9);
                    aMapLocation.setLatitude(optDouble);
                    aMapLocation.setLongitude(optDouble2);
                    aMapLocation.setAccuracy(f);
                    aMapLocation.setAltitude(optDouble3);
                    aMapLocation.setBearing(f2);
                    aMapLocation.setSpeed(f3);
                    aMapLocation.setTime(optLong);
                    aMapLocation.setCoordType(AMapLocation.COORD_TYPE_GCJ02);
                    if (fa.a(aMapLocation, this.i) <= 300.0f) {
                        synchronized (this.p) {
                            this.i.setLongitude(optDouble2);
                            this.i.setLatitude(optDouble);
                            this.i.setAccuracy(f);
                            this.i.setBearing(f2);
                            this.i.setSpeed(f3);
                            this.i.setTime(optLong);
                            this.i.setCoordType(AMapLocation.COORD_TYPE_GCJ02);
                        }
                        return aMapLocation;
                    }
                }
            }
        } catch (Throwable unused4) {
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:44:0x00aa A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00ab  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.amap.api.location.AMapLocation a(com.amap.api.location.AMapLocation r16, java.lang.String r17) {
        /*
            Method dump skipped, instructions count: 240
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.p.a(com.amap.api.location.AMapLocation, java.lang.String):com.amap.api.location.AMapLocation");
    }

    public final void a() {
        if (this.b == null) {
            return;
        }
        try {
            if (this.x != null) {
                this.b.removeUpdates(this.x);
            }
        } catch (Throwable unused) {
        }
        try {
            if (this.E != null) {
                this.b.removeGpsStatusListener(this.E);
            }
        } catch (Throwable unused2) {
        }
        try {
            if (this.a != null) {
                this.a.removeMessages(8);
            }
        } catch (Throwable unused3) {
        }
        this.C = 0;
        this.A = 0L;
        this.v = 0L;
        this.d = 0L;
        this.B = 0;
        this.w = 0;
        this.f.a();
        this.i = null;
        this.m = 0L;
        this.n = 0.0f;
        this.F = null;
        this.I = false;
    }

    public final void a(AMapLocationClientOption aMapLocationClientOption) {
        LocationManager locationManager;
        String str;
        long j2;
        float f;
        LocationListener locationListener;
        this.c = aMapLocationClientOption;
        if (this.c == null) {
            this.c = new AMapLocationClientOption();
        }
        try {
            q = ez.b(this.z, "pref", "lagt", q);
        } catch (Throwable unused) {
        }
        if (this.b == null) {
            return;
        }
        try {
            try {
                if (fa.c() - k <= BootloaderScanner.TIMEOUT && fa.a(j) && (this.c.isMockEnable() || !j.isMock())) {
                    this.d = fa.c();
                    b(j);
                }
                this.s = true;
                Looper myLooper = Looper.myLooper();
                if (myLooper == null) {
                    myLooper = this.z.getMainLooper();
                }
                Looper looper = myLooper;
                this.A = fa.c();
                if (!a(this.b)) {
                    a(8, 14, "no gps provider#1402", 0L);
                    return;
                }
                try {
                    if (fa.b() - q >= 259200000) {
                        this.b.sendExtraCommand("gps", "force_xtra_injection", null);
                        q = fa.b();
                        ez.a(this.z, "pref", "lagt", q);
                    }
                } catch (Throwable unused2) {
                }
                if (!this.c.getLocationMode().equals(AMapLocationClientOption.AMapLocationMode.Device_Sensors) || this.c.getDeviceModeDistanceFilter() <= 0.0f) {
                    locationManager = this.b;
                    str = "gps";
                    j2 = 900;
                    f = 0.0f;
                    locationListener = this.x;
                } else {
                    locationManager = this.b;
                    str = "gps";
                    j2 = this.c.getInterval();
                    f = this.c.getDeviceModeDistanceFilter();
                    locationListener = this.x;
                }
                locationManager.requestLocationUpdates(str, j2, f, locationListener, looper);
                this.b.addGpsStatusListener(this.E);
                a(8, 14, "no enough satellites#1401", this.c.getHttpTimeOut());
            } catch (Throwable th) {
                es.a(th, "GpsLocation", "requestLocationUpdates part2");
            }
        } catch (SecurityException e) {
            this.s = false;
            ey.a((String) null, 2121);
            a(2, 12, e.getMessage() + "#1201", 0L);
        }
    }

    public final boolean b() {
        return fa.c() - this.d <= 2800;
    }

    @SuppressLint({"NewApi"})
    public final int c() {
        if (this.b == null || !a(this.b)) {
            return 1;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            int i = Settings.Secure.getInt(this.z.getContentResolver(), "location_mode", 0);
            if (i == 0) {
                return 2;
            }
            if (i == 2) {
                return 3;
            }
        } else if (!this.b.isProviderEnabled("gps")) {
            return 2;
        }
        return !this.s ? 4 : 0;
    }

    public final int d() {
        return this.C;
    }
}
