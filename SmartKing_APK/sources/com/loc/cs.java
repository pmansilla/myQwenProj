package com.loc;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.location.common.model.AmapLoc;
import com.autonavi.amap.mapcore.tools.GLMapStaticValue;
import com.autonavi.aps.amapapi.model.AMapLocationServer;
import java.util.ArrayList;

/* compiled from: Aps.java */
@SuppressLint({"NewApi"})
/* loaded from: classes.dex */
public final class cs {
    static int D = -1;
    public static boolean H = true;
    private static boolean M = false;
    private static int O = -1;
    Context a = null;
    ConnectivityManager b = null;
    eg c = null;
    ee d = null;
    ei e = null;
    ct f = null;
    ep g = null;
    ArrayList<ScanResult> h = new ArrayList<>();
    a i = null;
    AMapLocationClientOption j = new AMapLocationClientOption();
    AMapLocationServer k = null;
    long l = 0;
    private int K = 0;
    eq m = null;
    boolean n = false;
    private String L = null;
    en o = null;
    StringBuilder p = new StringBuilder();
    boolean q = true;
    boolean r = true;
    AMapLocationClientOption.GeoLanguage s = AMapLocationClientOption.GeoLanguage.DEFAULT;
    boolean t = true;
    boolean u = false;
    WifiInfo v = null;
    boolean w = true;
    private String N = null;
    StringBuilder x = null;
    boolean y = false;
    public boolean z = false;
    int A = 12;
    private boolean P = true;
    eb B = null;
    boolean C = false;
    cu E = null;
    String F = null;
    ef G = null;
    IntentFilter I = null;
    LocationManager J = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: Aps.java */
    /* loaded from: classes.dex */
    public class a extends BroadcastReceiver {
        a() {
        }

        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            if (context == null || intent == null) {
                return;
            }
            try {
                String action = intent.getAction();
                if (TextUtils.isEmpty(action)) {
                    return;
                }
                if (action.equals("android.net.wifi.SCAN_RESULTS")) {
                    if (cs.this.c != null) {
                        cs.this.c.e();
                    }
                } else {
                    if (!action.equals("android.net.wifi.WIFI_STATE_CHANGED") || cs.this.c == null) {
                        return;
                    }
                    cs.this.c.f();
                }
            } catch (Throwable th) {
                es.a(th, "Aps", "onReceive");
            }
        }
    }

    private static AMapLocationServer a(int i, String str) {
        AMapLocationServer aMapLocationServer = new AMapLocationServer("");
        aMapLocationServer.setErrorCode(i);
        aMapLocationServer.setLocationDetail(str);
        if (i == 15) {
            ey.a((String) null, 2151);
        }
        return aMapLocationServer;
    }

    private AMapLocationServer a(AMapLocationServer aMapLocationServer, bk bkVar) {
        if (bkVar != null) {
            try {
                if (bkVar.a != null && bkVar.a.length != 0) {
                    ep epVar = new ep();
                    String str = new String(bkVar.a, "UTF-8");
                    if (!str.contains("\"status\":\"0\"")) {
                        if (!str.contains("</body></html>")) {
                            return null;
                        }
                        aMapLocationServer.setErrorCode(5);
                        if (this.c == null || !this.c.a(this.b)) {
                            this.p.append("请求可能被劫持了#0502");
                            ey.a((String) null, 2052);
                        } else {
                            this.p.append("您连接的是一个需要登录的网络，请确认已经登入网络#0501");
                            ey.a((String) null, 2051);
                        }
                        aMapLocationServer.setLocationDetail(this.p.toString());
                        return aMapLocationServer;
                    }
                    AMapLocationServer a2 = epVar.a(str, this.a, bkVar);
                    try {
                        a2.h(this.x.toString());
                        return a2;
                    } catch (Throwable th) {
                        th = th;
                        aMapLocationServer = a2;
                        aMapLocationServer.setErrorCode(4);
                        es.a(th, "Aps", "checkResponseEntity");
                        this.p.append("check response exception ex is" + th.getMessage() + "#0403");
                        aMapLocationServer.setLocationDetail(this.p.toString());
                        return aMapLocationServer;
                    }
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
        aMapLocationServer.setErrorCode(4);
        this.p.append("网络异常,请求异常#0403");
        aMapLocationServer.h(this.x.toString());
        aMapLocationServer.setLocationDetail(this.p.toString());
        if (bkVar != null) {
            ey.a(bkVar.d, 2041);
        }
        return aMapLocationServer;
    }

    @SuppressLint({"NewApi"})
    private AMapLocationServer a(boolean z, boolean z2) {
        int i;
        byte[] a2;
        StringBuilder sb;
        String str;
        AMapLocationServer aMapLocationServer = new AMapLocationServer("");
        try {
            if (this.m == null) {
                this.m = new eq();
            }
            if (this.j == null) {
                this.j = new AMapLocationClientOption();
            }
            this.m.a(this.a, this.j.isNeedAddress(), this.j.isOffset(), this.d, this.c, this.b, this.G != null ? this.G.b() : null, this.F);
            a2 = this.m.a();
            this.l = fa.c();
        } catch (Throwable th) {
            this.p.append("get parames error:" + th.getMessage() + "#0301");
            ey.a((String) null, 2031);
            i = 3;
        }
        try {
            es.c(this.a);
            eo a3 = this.o.a(this.a, a2, es.a(), z2);
            el.a(this.a).a(a3);
            bk a4 = this.o.a(a3);
            el.a(this.a).a();
            String str2 = "";
            if (a4 != null) {
                el.a(this.a).b();
                aMapLocationServer.a(this.o.a());
                if (!TextUtils.isEmpty(a4.c)) {
                    this.p.append("#csid:" + a4.c);
                }
                str2 = a4.d;
                aMapLocationServer.h(this.x.toString());
            }
            if (!z) {
                AMapLocationServer a5 = a(aMapLocationServer, a4);
                if (a5 != null) {
                    return a5;
                }
                byte[] a6 = eh.a(a4.a);
                if (a6 == null) {
                    aMapLocationServer.setErrorCode(5);
                    this.p.append("解密数据失败#0503");
                    aMapLocationServer.setLocationDetail(this.p.toString());
                    ey.a(str2, 2053);
                    return aMapLocationServer;
                }
                aMapLocationServer = this.g.a(aMapLocationServer, a6);
                if (!fa.a(aMapLocationServer)) {
                    this.L = aMapLocationServer.b();
                    ey.a(str2, !TextUtils.isEmpty(this.L) ? 2062 : 2061);
                    aMapLocationServer.setErrorCode(6);
                    StringBuilder sb2 = this.p;
                    StringBuilder sb3 = new StringBuilder("location faile retype:");
                    sb3.append(aMapLocationServer.d());
                    sb3.append(" rdesc:");
                    sb3.append(TextUtils.isEmpty(this.L) ? "" : this.L);
                    sb3.append("#0601");
                    sb2.append(sb3.toString());
                    aMapLocationServer.h(this.x.toString());
                    aMapLocationServer.setLocationDetail(this.p.toString());
                    return aMapLocationServer;
                }
                if (aMapLocationServer.getErrorCode() == 0 && aMapLocationServer.getLocationType() == 0) {
                    if (AmapLoc.RESULT_TYPE_STANDARD.equals(aMapLocationServer.d()) || AmapLoc.RESULT_TYPE_WIFI_ONLY.equals(aMapLocationServer.d()) || AmapLoc.RESULT_TYPE_FUSED.equals(aMapLocationServer.d()) || AmapLoc.RESULT_TYPE_NEW_WIFI_ONLY.equals(aMapLocationServer.d()) || AmapLoc.RESULT_TYPE_NEW_FUSED.equals(aMapLocationServer.d()) || "-1".equals(aMapLocationServer.d())) {
                        aMapLocationServer.setLocationType(5);
                    } else {
                        aMapLocationServer.setLocationType(6);
                    }
                }
                aMapLocationServer.setOffset(this.r);
                aMapLocationServer.a(this.q);
                aMapLocationServer.f(String.valueOf(this.s));
            }
            aMapLocationServer.e(AmapLoc.TYPE_NEW);
            aMapLocationServer.setLocationDetail(this.p.toString());
            this.F = aMapLocationServer.a();
            return aMapLocationServer;
        } catch (Throwable th2) {
            el.a(this.a).c();
            es.a(th2, "Aps", "getApsLoc req");
            ey.a("/mobile/binary", th2);
            if (fa.d(this.a)) {
                if (th2 instanceof t) {
                    t tVar = (t) th2;
                    if (tVar.a().contains("网络异常状态码")) {
                        StringBuilder sb4 = this.p;
                        sb4.append("网络异常，状态码错误#0404");
                        sb4.append(tVar.e());
                        i = 4;
                        AMapLocationServer a7 = a(i, this.p.toString());
                        a7.h(this.x.toString());
                        return a7;
                    }
                    if (tVar.e() == 23 || Math.abs((fa.c() - this.l) - this.j.getHttpTimeOut()) < 500) {
                        sb = this.p;
                        str = "网络异常，连接超时#0402";
                    }
                }
                sb = this.p;
                str = "网络异常,请求异常#0403";
            } else {
                sb = this.p;
                str = "网络异常，未连接到网络，请连接网络#0401";
            }
            sb.append(str);
            i = 4;
            AMapLocationServer a72 = a(i, this.p.toString());
            a72.h(this.x.toString());
            return a72;
        }
    }

    private StringBuilder a(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder(700);
        } else {
            sb.delete(0, sb.length());
        }
        sb.append(this.d.l());
        sb.append(this.c.i());
        return sb;
    }

    public static void b(Context context) {
        try {
            if (O == -1 || er.g(context)) {
                O = 1;
                er.a(context);
            }
        } catch (Throwable th) {
            es.a(th, "Aps", "initAuth");
        }
    }

    private void c(AMapLocationServer aMapLocationServer) {
        if (aMapLocationServer != null) {
            this.k = aMapLocationServer;
        }
    }

    private void l() {
        if (this.o != null) {
            try {
                if (this.j == null) {
                    this.j = new AMapLocationClientOption();
                }
                int i = 0;
                if (this.j.getGeoLanguage() != null) {
                    switch (this.j.getGeoLanguage()) {
                        case ZH:
                            i = 1;
                            break;
                        case EN:
                            i = 2;
                            break;
                    }
                }
                this.o.a(this.j.getHttpTimeOut(), this.j.getLocationProtocol().equals(AMapLocationClientOption.AMapLocationProtocol.HTTPS), i);
            } catch (Throwable unused) {
            }
        }
    }

    private void m() {
        try {
            if (this.i == null) {
                this.i = new a();
            }
            if (this.I == null) {
                this.I = new IntentFilter();
                this.I.addAction("android.net.wifi.WIFI_STATE_CHANGED");
                this.I.addAction("android.net.wifi.SCAN_RESULTS");
            }
            this.a.registerReceiver(this.i, this.I);
        } catch (Throwable th) {
            es.a(th, "Aps", "initBroadcastListener");
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:85:0x01d4, code lost:
    
        if (r11.w == false) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x021b, code lost:
    
        r1 = "cgi";
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x0218, code lost:
    
        if (r11.w == false) goto L87;
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:71:0x0174. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:75:0x02df  */
    /* JADX WARN: Removed duplicated region for block: B:80:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String n() {
        /*
            Method dump skipped, instructions count: 786
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.cs.n():java.lang.String");
    }

    private boolean o() {
        this.h = this.c.c();
        return this.h == null || this.h.size() <= 0;
    }

    public final AMapLocationServer a(double d, double d2) {
        try {
            String a2 = this.o.a(this.a, d, d2);
            if (!a2.contains("\"status\":\"1\"")) {
                return null;
            }
            AMapLocationServer a3 = this.g.a(a2);
            a3.setLatitude(d);
            a3.setLongitude(d2);
            return a3;
        } catch (Throwable unused) {
            return null;
        }
    }

    public final AMapLocationServer a(AMapLocationServer aMapLocationServer) {
        this.E.a(this.t);
        return this.E.a(aMapLocationServer);
    }

    public final AMapLocationServer a(boolean z) {
        int i;
        String str;
        if (this.a == null) {
            this.p.append("context is null#0101");
            ey.a((String) null, GLMapStaticValue.MAP_PARAMETERNAME_SATELLITE);
            i = 1;
        } else {
            if (this.c.h()) {
                i = 15;
                str = "networkLocation has been mocked!#1502";
                return a(i, str);
            }
            a();
            if (!TextUtils.isEmpty(this.N)) {
                AMapLocationServer a2 = a(false, z);
                if (fa.a(a2)) {
                    this.e.a(this.x.toString());
                    this.e.a(this.d.c());
                    c(a2);
                }
                return a2;
            }
            i = this.A;
        }
        str = this.p.toString();
        return a(i, str);
    }

    public final void a() {
        this.o = en.a(this.a);
        l();
        if (this.b == null) {
            this.b = (ConnectivityManager) fa.a(this.a, "connectivity");
        }
        if (this.m == null) {
            this.m = new eq();
        }
    }

    public final void a(Context context) {
        try {
            if (this.a != null) {
                return;
            }
            this.E = new cu();
            this.a = context.getApplicationContext();
            er.d(this.a);
            fa.b(this.a);
            if (this.c == null) {
                this.c = new eg(this.a, (WifiManager) fa.a(this.a, "wifi"));
            }
            if (this.d == null) {
                this.d = new ee(this.a);
            }
            if (this.e == null) {
                this.e = new ei();
            }
            if (this.g == null) {
                this.g = new ep();
            }
            if (this.G == null) {
                this.G = new ef(this.a);
            }
        } catch (Throwable th) {
            es.a(th, "Aps", "initBase");
        }
    }

    public final void a(AMapLocationClientOption aMapLocationClientOption) {
        AMapLocationClientOption.GeoLanguage geoLanguage;
        boolean z;
        boolean z2;
        boolean z3;
        this.j = aMapLocationClientOption;
        if (this.j == null) {
            this.j = new AMapLocationClientOption();
        }
        if (this.c != null) {
            eg egVar = this.c;
            this.j.isWifiActiveScan();
            egVar.a(this.j.isWifiScan(), this.j.isMockEnable(), AMapLocationClientOption.isOpenAlwaysScanWifi(), aMapLocationClientOption.getScanWifiInterval());
        }
        l();
        if (this.e != null) {
            this.e.a(this.j);
        }
        if (this.g != null) {
            this.g.a(this.j);
        }
        AMapLocationClientOption.GeoLanguage geoLanguage2 = AMapLocationClientOption.GeoLanguage.DEFAULT;
        try {
            geoLanguage = this.j.getGeoLanguage();
            try {
                z = this.j.isNeedAddress();
            } catch (Throwable unused) {
                z = true;
                z2 = true;
                z3 = true;
                this.r = z2;
                this.q = z;
                this.t = z3;
                this.s = geoLanguage;
            }
        } catch (Throwable unused2) {
            geoLanguage = geoLanguage2;
        }
        try {
            z2 = this.j.isOffset();
            try {
                z3 = this.j.isLocationCacheEnable();
                try {
                    this.u = this.j.isOnceLocationLatest();
                    this.C = this.j.isSensorEnable();
                    if (z2 != this.r || z != this.q || z3 != this.t || geoLanguage != this.s) {
                        try {
                            if (this.e != null) {
                                this.e.a();
                            }
                            c(null);
                            this.P = false;
                            if (this.E != null) {
                                this.E.a();
                            }
                        } catch (Throwable th) {
                            es.a(th, "Aps", "cleanCache");
                        }
                    }
                } catch (Throwable unused3) {
                }
            } catch (Throwable unused4) {
                z3 = true;
                this.r = z2;
                this.q = z;
                this.t = z3;
                this.s = geoLanguage;
            }
        } catch (Throwable unused5) {
            z2 = true;
            z3 = true;
            this.r = z2;
            this.q = z;
            this.t = z3;
            this.s = geoLanguage;
        }
        this.r = z2;
        this.q = z;
        this.t = z3;
        this.s = geoLanguage;
    }

    public final void b() {
        if (this.B == null) {
            this.B = new eb(this.a);
        }
        if (this.f == null) {
            this.f = new ct(this.a);
        }
        m();
        this.c.b(false);
        this.h = this.c.c();
        this.d.a(false, o());
        this.e.a(this.a);
        this.f.b();
        try {
            if (this.a.checkCallingOrSelfPermission(ad.c("EYW5kcm9pZC5wZXJtaXNzaW9uLldSSVRFX1NFQ1VSRV9TRVRUSU5HUw==")) == 0) {
                this.n = true;
            }
        } catch (Throwable unused) {
        }
        this.z = true;
    }

    public final void b(AMapLocationServer aMapLocationServer) {
        if (fa.a(aMapLocationServer)) {
            this.e.a(this.N, this.x, aMapLocationServer, this.a, true);
        }
    }

    public final void c() {
        if (this.p.length() > 0) {
            this.p.delete(0, this.p.length());
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(18:6|(1:10)|11|(1:13)(2:107|(4:109|(1:111)(1:115)|112|(3:114|15|(11:26|(2:28|(1:30)(1:31))|32|33|(6:38|39|41|42|43|(2:45|(2:47|48)(2:49|50))(2:51|(2:53|54)(15:55|(1:100)(1:57)|58|(1:60)(2:93|(2:95|(1:97)))|61|62|(2:66|(1:68)(2:69|(1:71)(2:72|(1:74)(1:75))))|76|(1:78)|79|(1:83)|84|(1:91)(1:88)|89|90)))|104|39|41|42|43|(0)(0))(4:19|(1:23)|24|25))))|14|15|(1:17)|26|(0)|32|33|(7:35|38|39|41|42|43|(0)(0))|104|39|41|42|43|(0)(0)) */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x00cc, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x00cd, code lost:
    
        com.loc.es.a(r0, "Aps", "getLocation getCgiListParam");
     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x00ba, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x00bb, code lost:
    
        com.loc.es.a(r0, "Aps", "getLocation getScanResultsParam");
     */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0089  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00e3  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x010f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.autonavi.aps.amapapi.model.AMapLocationServer d() throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 600
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.cs.d():com.autonavi.aps.amapapi.model.AMapLocationServer");
    }

    public final void e() {
        try {
            a(this.a);
            a(this.j);
            i();
            b(a(true, true));
        } catch (Throwable th) {
            es.a(th, "Aps", "doFusionLocation");
        }
    }

    @SuppressLint({"NewApi"})
    public final void f() {
        this.F = null;
        this.y = false;
        this.z = false;
        if (this.G != null) {
            this.G.c();
        }
        if (this.f != null) {
            this.f.a();
        }
        if (this.e != null) {
            this.e.b(this.a);
        }
        if (this.E != null) {
            this.E.a();
        }
        if (this.g != null) {
            this.g = null;
        }
        fa.h();
        try {
            try {
                if (this.a != null && this.i != null) {
                    this.a.unregisterReceiver(this.i);
                }
            } catch (Throwable th) {
                es.a(th, "Aps", "destroy");
            }
            if (this.d != null) {
                this.d.h();
            }
            if (this.c != null) {
                this.c.j();
            }
            if (this.h != null) {
                this.h.clear();
            }
            if (this.B != null) {
                this.B.e();
            }
            el.d();
            this.k = null;
            this.a = null;
            this.x = null;
            this.J = null;
        } finally {
            this.i = null;
        }
    }

    public final void g() {
        try {
            if (this.f != null) {
                this.f.c();
            }
        } catch (Throwable th) {
            es.a(th, "Aps", "bindAMapService");
        }
    }

    public final void h() {
        try {
            if (this.f != null) {
                this.f.d();
            }
        } catch (Throwable th) {
            es.a(th, "Aps", "bindOtherService");
        }
    }

    public final void i() {
        try {
        } catch (Throwable th) {
            es.a(th, "Aps", "initFirstLocateParam");
        }
        if (this.y) {
            return;
        }
        if (this.N != null) {
            this.N = null;
        }
        if (this.x != null) {
            this.x.delete(0, this.x.length());
        }
        if (this.u) {
            m();
        }
        this.c.b(this.u);
        this.h = this.c.c();
        this.d.a(true, o());
        this.N = n();
        if (!TextUtils.isEmpty(this.N)) {
            this.x = a(this.x);
        }
        this.y = true;
    }

    public final AMapLocationServer j() {
        int i;
        String sb;
        if (this.c.h()) {
            i = 15;
            sb = "networkLocation has been mocked!#1502";
        } else {
            if (!TextUtils.isEmpty(this.N)) {
                AMapLocationServer a2 = this.e.a(this.a, this.N, this.x, true);
                if (fa.a(a2)) {
                    c(a2);
                }
                return a2;
            }
            i = this.A;
            sb = this.p.toString();
        }
        return a(i, sb);
    }

    public final void k() {
        if (this.G != null) {
            this.G.a();
        }
    }
}
