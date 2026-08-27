package com.amap.api.mapcore.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import com.amap.location.common.model.AmapLoc;
import com.autonavi.amap.mapcore.Inner_3dMap_location;
import com.autonavi.amap.mapcore.Inner_3dMap_locationOption;

/* compiled from: MapNetLocation.java */
/* loaded from: classes.dex */
public final class kk {
    Context a;
    private kq e;
    private kp f;
    private ks h;
    private ConnectivityManager i;
    private ku j;
    private Inner_3dMap_locationOption l;
    private a g = null;
    boolean b = false;
    private StringBuilder k = new StringBuilder();
    String c = null;
    private ki m = null;
    long d = 0;
    private final String n = "\"status\":\"0\"";
    private final String o = "</body></html>";

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: MapNetLocation.java */
    /* loaded from: classes.dex */
    public class a extends BroadcastReceiver {
        private a() {
        }

        /* synthetic */ a(kk kkVar, byte b) {
            this();
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
                    if (kk.this.e != null) {
                        kk.this.e.c();
                    }
                } else {
                    if (!action.equals("android.net.wifi.WIFI_STATE_CHANGED") || kk.this.e == null) {
                        return;
                    }
                    kk.this.e.d();
                }
            } catch (Throwable th) {
                kw.a(th, "MapNetLocation", "onReceive");
            }
        }
    }

    public kk(Context context) {
        this.a = null;
        this.e = null;
        this.f = null;
        this.h = null;
        this.i = null;
        this.j = null;
        this.l = null;
        try {
            this.a = context.getApplicationContext();
            la.b(this.a);
            a(this.a);
            this.l = new Inner_3dMap_locationOption();
            if (this.e == null) {
                this.e = new kq(this.a, (WifiManager) la.a(this.a, "wifi"));
                this.e.a(this.b);
            }
            if (this.f == null) {
                this.f = new kp(this.a);
            }
            if (this.h == null) {
                this.h = ks.a(this.a);
            }
            if (this.i == null) {
                this.i = (ConnectivityManager) la.a(this.a, "connectivity");
            }
            this.j = new ku();
            c();
        } catch (Throwable th) {
            kw.a(th, "MapNetLocation", "<init>");
        }
    }

    private static ki a(ki kiVar) {
        return ke.a().a(kiVar);
    }

    private void a(Context context) {
        try {
            if (context.checkCallingOrSelfPermission(hp.c("EYW5kcm9pZC5wZXJtaXNzaW9uLldSSVRFX1NFQ1VSRV9TRVRUSU5HUw==")) == 0) {
                this.b = true;
            }
        } catch (Throwable unused) {
        }
    }

    private boolean a(long j) {
        if (la.b() - j < 800) {
            if ((km.a(this.m) ? la.a() - this.m.getTime() : 0L) <= 10000) {
                return true;
            }
        }
        return false;
    }

    private void c() {
        try {
            byte b = 0;
            if (this.g == null) {
                this.g = new a(this, b);
            }
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
            intentFilter.addAction("android.net.wifi.SCAN_RESULTS");
            this.a.registerReceiver(this.g, intentFilter);
            this.e.b(false);
            this.f.f();
        } catch (Throwable th) {
            kw.a(th, "MapNetLocation", "initBroadcastListener");
        }
    }

    private ki d() throws Exception {
        StringBuilder sb;
        String str;
        ki kiVar = new ki("");
        if (this.e != null && this.e.g()) {
            kiVar.setErrorCode(15);
            return kiVar;
        }
        try {
            if (this.j == null) {
                this.j = new ku();
            }
            this.j.a(this.a, this.l.isNeedAddress(), this.l.isOffset(), this.f, this.e, this.i, this.c);
            kl klVar = new kl();
            byte[] bArr = null;
            String str2 = "";
            try {
                try {
                    iz a2 = this.h.a(this.h.a(this.a, this.j.a(), kw.a()));
                    if (a2 != null) {
                        bArr = a2.a;
                        str2 = a2.c;
                    }
                    if (bArr == null || bArr.length == 0) {
                        kiVar.setErrorCode(4);
                        this.k.append("please check the network");
                        if (!TextUtils.isEmpty(str2)) {
                            this.k.append(" #csid:" + str2);
                        }
                        kiVar.setLocationDetail(this.k.toString());
                        return kiVar;
                    }
                    String str3 = new String(bArr, "UTF-8");
                    if (str3.contains("\"status\":\"0\"")) {
                        return klVar.a(str3, this.a, a2);
                    }
                    if (str3.contains("</body></html>")) {
                        kiVar.setErrorCode(5);
                        if (this.e == null || !this.e.a(this.i)) {
                            sb = this.k;
                            str = "request may be intercepted";
                        } else {
                            sb = this.k;
                            str = "make sure you are logged in to the network";
                        }
                        sb.append(str);
                        if (!TextUtils.isEmpty(str2)) {
                            this.k.append(" #csid:" + str2);
                        }
                        kiVar.setLocationDetail(this.k.toString());
                        return kiVar;
                    }
                    byte[] a3 = kr.a(bArr);
                    if (a3 == null) {
                        kiVar.setErrorCode(5);
                        this.k.append("decrypt response data error");
                        if (!TextUtils.isEmpty(str2)) {
                            this.k.append(" #csid:" + str2);
                        }
                        kiVar.setLocationDetail(this.k.toString());
                        return kiVar;
                    }
                    ki a4 = klVar.a(a3);
                    this.c = a4.a();
                    if (a4.getErrorCode() != 0) {
                        if (!TextUtils.isEmpty(str2)) {
                            a4.setLocationDetail(a4.getLocationDetail() + " #csid:" + str2);
                        }
                        return a4;
                    }
                    if (!km.a(a4)) {
                        String b = a4.b();
                        a4.setErrorCode(6);
                        StringBuilder sb2 = this.k;
                        StringBuilder sb3 = new StringBuilder("location faile retype:");
                        sb3.append(a4.d());
                        sb3.append(" rdesc:");
                        if (b == null) {
                            b = "null";
                        }
                        sb3.append(b);
                        sb2.append(sb3.toString());
                        if (!TextUtils.isEmpty(str2)) {
                            this.k.append(" #csid:" + str2);
                        }
                        a4.setLocationDetail(this.k.toString());
                        return a4;
                    }
                    a4.e();
                    if (a4.getErrorCode() == 0 && a4.getLocationType() == 0) {
                        if (AmapLoc.RESULT_TYPE_STANDARD.equals(a4.d()) || AmapLoc.RESULT_TYPE_WIFI_ONLY.equals(a4.d()) || AmapLoc.RESULT_TYPE_FUSED.equals(a4.d()) || AmapLoc.RESULT_TYPE_NEW_WIFI_ONLY.equals(a4.d()) || AmapLoc.RESULT_TYPE_NEW_FUSED.equals(a4.d()) || "-1".equals(a4.d())) {
                            a4.setLocationType(5);
                        } else {
                            a4.setLocationType(6);
                        }
                        this.k.append(a4.d());
                        if (!TextUtils.isEmpty(str2)) {
                            this.k.append(" #csid:" + str2);
                        }
                        a4.setLocationDetail(this.k.toString());
                    }
                    return a4;
                } catch (Throwable th) {
                    kw.a(th, "MapNetLocation", "getApsLoc req");
                    kiVar.setErrorCode(4);
                    this.k.append("please check the network");
                    kiVar.setLocationDetail(this.k.toString());
                    return kiVar;
                }
            } catch (Throwable th2) {
                kw.a(th2, "MapNetLocation", "getApsLoc buildV4Dot2");
                kiVar.setErrorCode(3);
                this.k.append("buildV4Dot2 error " + th2.getMessage());
                kiVar.setLocationDetail(this.k.toString());
                return kiVar;
            }
        } catch (Throwable th3) {
            kw.a(th3, "MapNetLocation", "getApsLoc");
            this.k.append("get parames error:" + th3.getMessage());
            kiVar.setErrorCode(3);
            kiVar.setLocationDetail(this.k.toString());
            return kiVar;
        }
    }

    public final Inner_3dMap_location a() {
        if (this.k.length() > 0) {
            this.k.delete(0, this.k.length());
        }
        if (a(this.d) && km.a(this.m)) {
            return this.m;
        }
        this.d = la.b();
        if (this.a == null) {
            this.k.append("context is null");
            Inner_3dMap_location inner_3dMap_location = new Inner_3dMap_location("");
            inner_3dMap_location.setErrorCode(1);
            inner_3dMap_location.setLocationDetail(this.k.toString());
            return inner_3dMap_location;
        }
        try {
            this.f.f();
        } catch (Throwable th) {
            kw.a(th, "MapNetLocation", "getLocation getCgiListParam");
        }
        try {
            this.e.b(true);
        } catch (Throwable th2) {
            kw.a(th2, "MapNetLocation", "getLocation getScanResultsParam");
        }
        try {
            this.m = d();
            this.m = a(this.m);
        } catch (Throwable th3) {
            kw.a(th3, "MapNetLocation", "getLocation getScanResultsParam");
        }
        return this.m;
    }

    public final void a(Inner_3dMap_locationOption inner_3dMap_locationOption) {
        this.l = inner_3dMap_locationOption;
        if (this.l == null) {
            this.l = new Inner_3dMap_locationOption();
        }
        try {
            kq kqVar = this.e;
            this.l.isWifiActiveScan();
            kqVar.c(this.l.isWifiScan());
        } catch (Throwable unused) {
        }
        try {
            this.h.a(this.l.getHttpTimeOut(), this.l.getLocationProtocol().equals(Inner_3dMap_locationOption.Inner_3dMap_Enum_LocationProtocol.HTTPS));
        } catch (Throwable unused2) {
        }
    }

    public final void b() {
        this.b = false;
        this.c = null;
        try {
            if (this.a != null && this.g != null) {
                this.a.unregisterReceiver(this.g);
            }
            if (this.f != null) {
                this.f.g();
            }
            if (this.e != null) {
                this.e.h();
            }
            this.g = null;
        } catch (Throwable th) {
            this.g = null;
            throw th;
        }
    }
}
