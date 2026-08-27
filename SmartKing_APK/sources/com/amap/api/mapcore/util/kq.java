package com.amap.api.mapcore.util;

import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import org.apache.commons.lang.time.DateUtils;

/* compiled from: WifiManagerWrapper.java */
/* loaded from: classes.dex */
public final class kq {
    static long c;
    static long d;
    static long e;
    static long f;
    static long g;
    public static HashMap<String, Long> q = new HashMap<>(36);
    public static long r = 0;
    static int s = 0;
    WifiManager a;
    Context h;
    ArrayList<ScanResult> b = new ArrayList<>();
    boolean i = false;
    StringBuilder j = null;
    boolean k = true;
    boolean l = true;
    boolean m = true;
    private volatile WifiInfo v = null;
    String n = null;
    TreeMap<Integer, ScanResult> o = null;
    public boolean p = true;
    ConnectivityManager t = null;
    private long w = 30000;
    volatile boolean u = false;

    public kq(Context context, WifiManager wifiManager) {
        this.a = wifiManager;
        this.h = context;
    }

    private static boolean a(int i) {
        int i2;
        try {
            i2 = WifiManager.calculateSignalLevel(i, 20);
        } catch (ArithmeticException e2) {
            kw.a(e2, "Aps", "wifiSigFine");
            i2 = 20;
        }
        return i2 > 0;
    }

    public static boolean a(WifiInfo wifiInfo) {
        return (wifiInfo == null || TextUtils.isEmpty(wifiInfo.getSSID()) || !la.a(wifiInfo.getBSSID())) ? false : true;
    }

    private void d(boolean z) {
        this.k = z;
        this.l = true;
        this.m = true;
        this.w = 30000L;
    }

    public static String i() {
        return String.valueOf(la.b() - f);
    }

    private List<ScanResult> j() {
        long b;
        if (this.a != null) {
            try {
                List<ScanResult> scanResults = this.a.getScanResults();
                if (Build.VERSION.SDK_INT >= 17) {
                    HashMap<String, Long> hashMap = new HashMap<>(36);
                    for (ScanResult scanResult : scanResults) {
                        hashMap.put(scanResult.BSSID, Long.valueOf(scanResult.timestamp));
                    }
                    if (q.isEmpty() || !q.equals(hashMap)) {
                        q = hashMap;
                        b = la.b();
                    }
                    this.n = null;
                    return scanResults;
                }
                b = la.b();
                r = b;
                this.n = null;
                return scanResults;
            } catch (SecurityException e2) {
                this.n = e2.getMessage();
            } catch (Throwable th) {
                this.n = null;
                kw.a(th, "WifiManagerWrapper", "getScanResults");
            }
        }
        return null;
    }

    private WifiInfo k() {
        try {
            if (this.a != null) {
                return this.a.getConnectionInfo();
            }
            return null;
        } catch (Throwable th) {
            kw.a(th, "WifiManagerWrapper", "getConnectionInfo");
            return null;
        }
    }

    private int l() {
        if (this.a != null) {
            return this.a.getWifiState();
        }
        return 4;
    }

    private boolean m() {
        long b = la.b() - c;
        if (b < 4900) {
            return false;
        }
        if (n() && b < 9900) {
            return false;
        }
        if (s > 1) {
            long j = 30000;
            if (this.w != 30000) {
                j = this.w;
            } else if (kv.b() != -1) {
                j = kv.b();
            }
            if (Build.VERSION.SDK_INT >= 28 && b < j) {
                return false;
            }
        }
        if (this.a == null) {
            return false;
        }
        c = la.b();
        if (s < 2) {
            s++;
        }
        return this.a.startScan();
    }

    private boolean n() {
        if (this.t == null) {
            this.t = (ConnectivityManager) la.a(this.h, "connectivity");
        }
        return a(this.t);
    }

    private boolean o() {
        if (this.a == null) {
            return false;
        }
        return la.c(this.h);
    }

    private void p() {
        String valueOf;
        if (this.b == null || this.b.isEmpty()) {
            return;
        }
        if (la.b() - f > DateUtils.MILLIS_PER_HOUR) {
            b();
        }
        if (this.o == null) {
            this.o = new TreeMap<>(Collections.reverseOrder());
        }
        this.o.clear();
        int size = this.b.size();
        for (int i = 0; i < size; i++) {
            ScanResult scanResult = this.b.get(i);
            if (la.a(scanResult != null ? scanResult.BSSID : "") && (size <= 20 || a(scanResult.level))) {
                if (!TextUtils.isEmpty(scanResult.SSID)) {
                    valueOf = "<unknown ssid>".equals(scanResult.SSID) ? "unkwn" : String.valueOf(i);
                    this.o.put(Integer.valueOf((scanResult.level * 25) + i), scanResult);
                }
                scanResult.SSID = valueOf;
                this.o.put(Integer.valueOf((scanResult.level * 25) + i), scanResult);
            }
        }
        this.b.clear();
        Iterator<ScanResult> it = this.o.values().iterator();
        while (it.hasNext()) {
            this.b.add(it.next());
        }
        this.o.clear();
    }

    private void q() {
        if (t()) {
            long b = la.b();
            if (b - d >= 10000) {
                this.b.clear();
                g = f;
            }
            r();
            if (b - d >= 10000) {
                for (int i = 20; i > 0 && f == g; i--) {
                    try {
                        Thread.sleep(150L);
                    } catch (Throwable unused) {
                    }
                }
            }
        }
    }

    private void r() {
        if (t()) {
            try {
                if (m()) {
                    e = la.b();
                }
            } catch (Throwable th) {
                kw.a(th, "WifiManager", "wifiScan");
            }
        }
    }

    private void s() {
        if (g != f) {
            List<ScanResult> list = null;
            try {
                list = j();
            } catch (Throwable th) {
                kw.a(th, "WifiManager", "updateScanResult");
            }
            g = f;
            if (list == null) {
                this.b.clear();
            } else {
                this.b.clear();
                this.b.addAll(list);
            }
        }
    }

    private boolean t() {
        this.p = o();
        if (!this.p || !this.k) {
            return false;
        }
        if (e != 0) {
            if (la.b() - e < 4900 || la.b() - f < 1500) {
                return false;
            }
            int i = ((la.b() - f) > 4900L ? 1 : ((la.b() - f) == 4900L ? 0 : -1));
        }
        return true;
    }

    public final ArrayList<ScanResult> a() {
        if (this.b == null) {
            return null;
        }
        ArrayList<ScanResult> arrayList = new ArrayList<>();
        if (!this.b.isEmpty()) {
            arrayList.addAll(this.b);
        }
        return arrayList;
    }

    public final void a(boolean z) {
        Context context = this.h;
        if (!kv.a() || !this.m || this.a == null || context == null || !z || la.c() <= 17) {
            return;
        }
        ContentResolver contentResolver = context.getContentResolver();
        try {
            if (((Integer) ky.a("android.provider.Settings$Global", "getInt", new Object[]{contentResolver, "wifi_scan_always_enabled"}, (Class<?>[]) new Class[]{ContentResolver.class, String.class})).intValue() == 0) {
                ky.a("android.provider.Settings$Global", "putInt", new Object[]{contentResolver, "wifi_scan_always_enabled", 1}, (Class<?>[]) new Class[]{ContentResolver.class, String.class, Integer.TYPE});
            }
        } catch (Throwable th) {
            kw.a(th, "WifiManagerWrapper", "enableWifiAlwaysScan");
        }
    }

    public final boolean a(ConnectivityManager connectivityManager) {
        WifiManager wifiManager = this.a;
        if (wifiManager == null) {
            return false;
        }
        try {
            if (la.a(connectivityManager.getActiveNetworkInfo()) == 1) {
                return a(wifiManager.getConnectionInfo());
            }
            return false;
        } catch (Throwable th) {
            kw.a(th, "WifiManagerWrapper", "wifiAccess");
            return false;
        }
    }

    public final void b() {
        this.v = null;
        this.b.clear();
    }

    public final void b(boolean z) {
        if (z) {
            q();
        } else {
            r();
        }
        if (this.u) {
            this.u = false;
            b();
        }
        s();
        if (la.b() - f > 20000) {
            this.b.clear();
        }
        d = la.b();
        if (this.b.isEmpty()) {
            f = la.b();
            List<ScanResult> j = j();
            if (j != null) {
                this.b.addAll(j);
            }
        }
        p();
    }

    public final void c() {
        if (this.a != null && la.b() - f > 4900) {
            f = la.b();
        }
    }

    public final void c(boolean z) {
        d(z);
    }

    public final void d() {
        int i;
        if (this.a == null) {
            return;
        }
        try {
            i = l();
        } catch (Throwable th) {
            kw.a(th, "Aps", "onReceive part");
            i = 4;
        }
        if (this.b == null) {
            this.b = new ArrayList<>();
        }
        if (i != 4) {
            switch (i) {
                case 0:
                case 1:
                    break;
                default:
                    return;
            }
        }
        this.u = true;
    }

    public final boolean e() {
        return this.p;
    }

    public final WifiInfo f() {
        this.v = k();
        return this.v;
    }

    public final boolean g() {
        return this.i;
    }

    public final void h() {
        b();
        this.b.clear();
    }
}
