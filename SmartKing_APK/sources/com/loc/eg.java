package com.loc;

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
import java.util.Locale;
import java.util.TreeMap;
import org.apache.commons.lang.time.DateUtils;

/* compiled from: WifiManagerWrapper.java */
/* loaded from: classes.dex */
public final class eg {
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

    public eg(Context context, WifiManager wifiManager) {
        this.a = wifiManager;
        this.h = context;
    }

    public static long a() {
        return ((fa.c() - r) / 1000) + 1;
    }

    private static boolean a(int i) {
        int i2;
        try {
            i2 = WifiManager.calculateSignalLevel(i, 20);
        } catch (ArithmeticException e2) {
            es.a(e2, "Aps", "wifiSigFine");
            i2 = 20;
        }
        return i2 > 0;
    }

    public static boolean a(WifiInfo wifiInfo) {
        return (wifiInfo == null || TextUtils.isEmpty(wifiInfo.getSSID()) || !fa.a(wifiInfo.getBSSID())) ? false : true;
    }

    public static String k() {
        return String.valueOf(fa.c() - f);
    }

    private List<ScanResult> l() {
        long c2;
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
                        c2 = fa.c();
                    }
                    this.n = null;
                    return scanResults;
                }
                c2 = fa.c();
                r = c2;
                this.n = null;
                return scanResults;
            } catch (SecurityException e2) {
                this.n = e2.getMessage();
            } catch (Throwable th) {
                this.n = null;
                es.a(th, "WifiManagerWrapper", "getScanResults");
            }
        }
        return null;
    }

    private WifiInfo m() {
        try {
            if (this.a != null) {
                return this.a.getConnectionInfo();
            }
            return null;
        } catch (Throwable th) {
            es.a(th, "WifiManagerWrapper", "getConnectionInfo");
            return null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x0058, code lost:
    
        if (r0 < r6) goto L31;
     */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0078 A[Catch: Throwable -> 0x007f, TRY_LEAVE, TryCatch #0 {Throwable -> 0x007f, blocks: (B:4:0x0006, B:6:0x0014, B:8:0x0018, B:9:0x0024, B:13:0x0032, B:15:0x0037, B:17:0x003f, B:18:0x0050, B:22:0x0042, B:24:0x004c, B:25:0x005a, B:27:0x005e, B:29:0x0069, B:30:0x006e, B:32:0x0078), top: B:3:0x0006 }] */
    /* JADX WARN: Removed duplicated region for block: B:36:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void n() {
        /*
            r10 = this;
            boolean r0 = r10.o()
            if (r0 == 0) goto L87
            long r0 = com.loc.fa.c()     // Catch: java.lang.Throwable -> L7f
            long r2 = com.loc.eg.c     // Catch: java.lang.Throwable -> L7f
            r4 = 0
            long r0 = r0 - r2
            r2 = 4900(0x1324, double:2.421E-320)
            int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r4 < 0) goto L75
            android.net.ConnectivityManager r2 = r10.t     // Catch: java.lang.Throwable -> L7f
            if (r2 != 0) goto L24
            android.content.Context r2 = r10.h     // Catch: java.lang.Throwable -> L7f
            java.lang.String r3 = "connectivity"
            java.lang.Object r2 = com.loc.fa.a(r2, r3)     // Catch: java.lang.Throwable -> L7f
            android.net.ConnectivityManager r2 = (android.net.ConnectivityManager) r2     // Catch: java.lang.Throwable -> L7f
            r10.t = r2     // Catch: java.lang.Throwable -> L7f
        L24:
            android.net.ConnectivityManager r2 = r10.t     // Catch: java.lang.Throwable -> L7f
            boolean r2 = r10.a(r2)     // Catch: java.lang.Throwable -> L7f
            if (r2 == 0) goto L32
            r2 = 9900(0x26ac, double:4.8912E-320)
            int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r4 < 0) goto L75
        L32:
            int r2 = com.loc.eg.s     // Catch: java.lang.Throwable -> L7f
            r3 = 1
            if (r2 <= r3) goto L5a
            long r4 = r10.w     // Catch: java.lang.Throwable -> L7f
            r6 = 30000(0x7530, double:1.4822E-319)
            int r2 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r2 == 0) goto L42
            long r6 = r10.w     // Catch: java.lang.Throwable -> L7f
            goto L50
        L42:
            long r4 = com.loc.er.B()     // Catch: java.lang.Throwable -> L7f
            r8 = -1
            int r2 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
            if (r2 == 0) goto L50
            long r6 = com.loc.er.B()     // Catch: java.lang.Throwable -> L7f
        L50:
            int r2 = android.os.Build.VERSION.SDK_INT     // Catch: java.lang.Throwable -> L7f
            r4 = 28
            if (r2 < r4) goto L5a
            int r2 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1))
            if (r2 < 0) goto L75
        L5a:
            android.net.wifi.WifiManager r0 = r10.a     // Catch: java.lang.Throwable -> L7f
            if (r0 == 0) goto L75
            long r0 = com.loc.fa.c()     // Catch: java.lang.Throwable -> L7f
            com.loc.eg.c = r0     // Catch: java.lang.Throwable -> L7f
            int r0 = com.loc.eg.s     // Catch: java.lang.Throwable -> L7f
            r1 = 2
            if (r0 >= r1) goto L6e
            int r0 = com.loc.eg.s     // Catch: java.lang.Throwable -> L7f
            int r0 = r0 + r3
            com.loc.eg.s = r0     // Catch: java.lang.Throwable -> L7f
        L6e:
            android.net.wifi.WifiManager r0 = r10.a     // Catch: java.lang.Throwable -> L7f
            boolean r0 = r0.startScan()     // Catch: java.lang.Throwable -> L7f
            goto L76
        L75:
            r0 = 0
        L76:
            if (r0 == 0) goto L7e
            long r0 = com.loc.fa.c()     // Catch: java.lang.Throwable -> L7f
            com.loc.eg.e = r0     // Catch: java.lang.Throwable -> L7f
        L7e:
            return
        L7f:
            r0 = move-exception
            java.lang.String r1 = "WifiManager"
            java.lang.String r2 = "wifiScan"
            com.loc.es.a(r0, r1, r2)
        L87:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.eg.n():void");
    }

    private boolean o() {
        this.p = this.a == null ? false : fa.h(this.h);
        if (!this.p || !this.k) {
            return false;
        }
        if (e != 0) {
            if (fa.c() - e < 4900 || fa.c() - f < 1500) {
                return false;
            }
            int i = ((fa.c() - f) > 4900L ? 1 : ((fa.c() - f) == 4900L ? 0 : -1));
        }
        return true;
    }

    public final void a(boolean z) {
        Context context = this.h;
        if (!er.A() || !this.m || this.a == null || context == null || !z || fa.d() <= 17) {
            return;
        }
        ContentResolver contentResolver = context.getContentResolver();
        try {
            if (((Integer) ew.a("android.provider.Settings$Global", "getInt", new Object[]{contentResolver, "wifi_scan_always_enabled"}, (Class<?>[]) new Class[]{ContentResolver.class, String.class})).intValue() == 0) {
                ew.a("android.provider.Settings$Global", "putInt", new Object[]{contentResolver, "wifi_scan_always_enabled", 1}, (Class<?>[]) new Class[]{ContentResolver.class, String.class, Integer.TYPE});
            }
        } catch (Throwable th) {
            es.a(th, "WifiManagerWrapper", "enableWifiAlwaysScan");
        }
    }

    public final void a(boolean z, boolean z2, boolean z3, long j) {
        this.k = z;
        this.l = z2;
        this.m = z3;
        if (j < 10000) {
            this.w = 10000L;
        } else {
            this.w = j;
        }
    }

    public final boolean a(ConnectivityManager connectivityManager) {
        WifiManager wifiManager = this.a;
        if (wifiManager == null) {
            return false;
        }
        try {
            if (fa.a(connectivityManager.getActiveNetworkInfo()) == 1) {
                return a(wifiManager.getConnectionInfo());
            }
            return false;
        } catch (Throwable th) {
            es.a(th, "WifiManagerWrapper", "wifiAccess");
            return false;
        }
    }

    public final String b() {
        return this.n;
    }

    public final void b(boolean z) {
        String valueOf;
        if (!z) {
            n();
        } else if (o()) {
            long c2 = fa.c();
            if (c2 - d >= 10000) {
                this.b.clear();
                g = f;
            }
            n();
            if (c2 - d >= 10000) {
                for (int i = 20; i > 0 && f == g; i--) {
                    try {
                        Thread.sleep(150L);
                    } catch (Throwable unused) {
                    }
                }
            }
        }
        if (this.u) {
            this.u = false;
            d();
        }
        if (g != f) {
            List<ScanResult> list = null;
            try {
                list = l();
            } catch (Throwable th) {
                es.a(th, "WifiManager", "updateScanResult");
            }
            g = f;
            if (list != null) {
                this.b.clear();
                this.b.addAll(list);
            } else {
                this.b.clear();
            }
        }
        if (fa.c() - f > 20000) {
            this.b.clear();
        }
        d = fa.c();
        if (this.b.isEmpty()) {
            f = fa.c();
            List<ScanResult> l = l();
            if (l != null) {
                this.b.addAll(l);
            }
        }
        if (this.b == null || this.b.isEmpty()) {
            return;
        }
        if (fa.c() - f > DateUtils.MILLIS_PER_HOUR) {
            d();
        }
        if (this.o == null) {
            this.o = new TreeMap<>(Collections.reverseOrder());
        }
        this.o.clear();
        int size = this.b.size();
        for (int i2 = 0; i2 < size; i2++) {
            ScanResult scanResult = this.b.get(i2);
            if (fa.a(scanResult != null ? scanResult.BSSID : "") && (size <= 20 || a(scanResult.level))) {
                if (!TextUtils.isEmpty(scanResult.SSID)) {
                    valueOf = "<unknown ssid>".equals(scanResult.SSID) ? "unkwn" : String.valueOf(i2);
                    this.o.put(Integer.valueOf((scanResult.level * 25) + i2), scanResult);
                }
                scanResult.SSID = valueOf;
                this.o.put(Integer.valueOf((scanResult.level * 25) + i2), scanResult);
            }
        }
        this.b.clear();
        Iterator<ScanResult> it = this.o.values().iterator();
        while (it.hasNext()) {
            this.b.add(it.next());
        }
        this.o.clear();
    }

    public final ArrayList<ScanResult> c() {
        if (this.b == null) {
            return null;
        }
        ArrayList<ScanResult> arrayList = new ArrayList<>();
        if (!this.b.isEmpty()) {
            arrayList.addAll(this.b);
        }
        return arrayList;
    }

    public final void d() {
        this.v = null;
        this.b.clear();
    }

    public final void e() {
        if (this.a != null && fa.c() - f > 4900) {
            f = fa.c();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x001e  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0027  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void f() {
        /*
            r4 = this;
            android.net.wifi.WifiManager r0 = r4.a
            if (r0 != 0) goto L5
            return
        L5:
            r0 = 4
            android.net.wifi.WifiManager r1 = r4.a     // Catch: java.lang.Throwable -> L11
            if (r1 == 0) goto L19
            android.net.wifi.WifiManager r1 = r4.a     // Catch: java.lang.Throwable -> L11
            int r1 = r1.getWifiState()     // Catch: java.lang.Throwable -> L11
            goto L1a
        L11:
            r1 = move-exception
            java.lang.String r2 = "Aps"
            java.lang.String r3 = "onReceive part"
            com.loc.es.a(r1, r2, r3)
        L19:
            r1 = 4
        L1a:
            java.util.ArrayList<android.net.wifi.ScanResult> r2 = r4.b
            if (r2 != 0) goto L25
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>()
            r4.b = r2
        L25:
            if (r1 == r0) goto L2b
            switch(r1) {
                case 0: goto L2b;
                case 1: goto L2b;
                default: goto L2a;
            }
        L2a:
            goto L2e
        L2b:
            r0 = 1
            r4.u = r0
        L2e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.eg.f():void");
    }

    public final WifiInfo g() {
        this.v = m();
        return this.v;
    }

    public final boolean h() {
        return this.i;
    }

    public final String i() {
        if (this.j == null) {
            this.j = new StringBuilder(700);
        } else {
            this.j.delete(0, this.j.length());
        }
        this.i = false;
        this.v = g();
        String bssid = a(this.v) ? this.v.getBSSID() : "";
        int size = this.b.size();
        boolean z = false;
        boolean z2 = false;
        for (int i = 0; i < size; i++) {
            String str = this.b.get(i).BSSID;
            if (!this.l && !"<unknown ssid>".equals(this.b.get(i).SSID)) {
                z = true;
            }
            String str2 = "nb";
            if (bssid.equals(str)) {
                str2 = "access";
                z2 = true;
            }
            this.j.append(String.format(Locale.US, "#%s,%s", str, str2));
        }
        if (this.b.size() == 0) {
            z = true;
        }
        if (!this.l && !z) {
            this.i = true;
        }
        if (!z2 && !TextUtils.isEmpty(bssid)) {
            StringBuilder sb = this.j;
            sb.append("#");
            sb.append(bssid);
            this.j.append(",access");
        }
        return this.j.toString();
    }

    public final void j() {
        d();
        this.b.clear();
    }
}
