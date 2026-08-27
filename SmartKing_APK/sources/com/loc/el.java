package com.loc;

import android.content.Context;
import android.net.Proxy;
import android.os.Build;
import android.text.TextUtils;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;

/* compiled from: DnsManager.java */
/* loaded from: classes.dex */
public final class el {
    private static el c;
    private Object d;
    private Context e;
    private ExecutorService f = null;
    private boolean g = false;
    private boolean h = true;
    eo a = null;
    private final int i = 2;
    private String j = "";
    private String k = "";
    private String[] l = null;
    int b = 0;
    private final int m = 5;
    private final int n = 2;

    /* compiled from: DnsManager.java */
    /* loaded from: classes.dex */
    class a implements Runnable {
        eo a;

        a(eo eoVar) {
            this.a = null;
            this.a = eoVar;
        }

        @Override // java.lang.Runnable
        public final void run() {
            el.this.b++;
            el.this.b(this.a);
            el elVar = el.this;
            elVar.b--;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x006e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:41:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private el(android.content.Context r11) {
        /*
            Method dump skipped, instructions count: 239
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.el.<init>(android.content.Context):void");
    }

    public static el a(Context context) {
        if (c == null) {
            c = new el(context);
        }
        return c;
    }

    private String a(String str) {
        int i;
        String str2;
        String str3 = null;
        if (e()) {
            try {
                String[] strArr = (String[]) ew.a(this.d, "getIpsByHostAsync", str);
                if (strArr == null || strArr.length <= 0) {
                    str2 = null;
                } else if (this.l == null) {
                    this.l = strArr;
                    str2 = strArr[0];
                } else if (a(strArr, this.l)) {
                    str2 = this.l[0];
                } else {
                    this.l = strArr;
                    str2 = strArr[0];
                }
                str3 = str2;
                i = 1;
            } catch (Throwable unused) {
                i = 0;
            }
            ey.a(this.e, "HttpDns", i);
        }
        new Object[1][0] = "DnsManager ==> getIpAsync  host ： " + str + " ， ip ： " + str3;
        fa.a();
        return str3;
    }

    private static boolean a(String[] strArr, String[] strArr2) {
        if (strArr != null && strArr2 == null) {
            return false;
        }
        if (strArr == null && strArr2 != null) {
            return false;
        }
        if (strArr == null && strArr2 == null) {
            return true;
        }
        try {
            if (strArr.length != strArr2.length) {
                return false;
            }
            ArrayList arrayList = new ArrayList(12);
            ArrayList arrayList2 = new ArrayList(12);
            arrayList.addAll(Arrays.asList(strArr));
            arrayList2.addAll(Arrays.asList(strArr2));
            Collections.sort(arrayList);
            Collections.sort(arrayList2);
            for (int i = 0; i < arrayList.size(); i++) {
                if (!((String) arrayList.get(i)).equals(arrayList2.get(i))) {
                    return false;
                }
            }
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    public static void d() {
        c = null;
    }

    private boolean e() {
        return (this.d == null || f() || ez.b(this.e, "pref", "dns_faile_count_total", 0L) >= 2) ? false : true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v0 */
    /* JADX WARN: Type inference failed for: r3v1 */
    /* JADX WARN: Type inference failed for: r3v15 */
    /* JADX WARN: Type inference failed for: r3v16 */
    /* JADX WARN: Type inference failed for: r3v4 */
    /* JADX WARN: Type inference failed for: r3v5 */
    /* JADX WARN: Type inference failed for: r3v9 */
    /* JADX WARN: Type inference failed for: r4v1 */
    /* JADX WARN: Type inference failed for: r4v10 */
    /* JADX WARN: Type inference failed for: r4v13 */
    /* JADX WARN: Type inference failed for: r4v14 */
    /* JADX WARN: Type inference failed for: r4v15 */
    /* JADX WARN: Type inference failed for: r4v2 */
    /* JADX WARN: Type inference failed for: r4v3 */
    /* JADX WARN: Type inference failed for: r4v5 */
    /* JADX WARN: Type inference failed for: r4v6 */
    /* JADX WARN: Type inference failed for: r4v7 */
    private boolean f() {
        ?? r4;
        Throwable th;
        ?? r3 = 0;
        try {
            r4 = Build.VERSION.SDK_INT >= 14 ? 1 : 0;
            try {
                if (r4 != 0) {
                    String property = System.getProperty("http.proxyHost");
                    String property2 = System.getProperty("http.proxyPort");
                    if (property2 == null) {
                        property2 = "-1";
                    }
                    r3 = Integer.parseInt(property2);
                    r4 = property;
                } else {
                    String host = Proxy.getHost(this.e);
                    r3 = Proxy.getPort(this.e);
                    r4 = host;
                }
            } catch (Throwable th2) {
                th = th2;
                th.printStackTrace();
                r3 = -1;
                if (r4 == 0) {
                }
            }
        } catch (Throwable th3) {
            r4 = r3;
            th = th3;
        }
        return r4 == 0 && r3 != -1;
    }

    public final void a() {
        if (TextUtils.isEmpty(this.k)) {
            return;
        }
        if (TextUtils.isEmpty(this.j) || !this.k.equals(this.j)) {
            this.j = this.k;
            ez.a(this.e, "ip", "last_ip", this.k);
        }
    }

    public final void a(eo eoVar) {
        try {
            this.g = false;
            if (e() && eoVar != null) {
                this.a = eoVar;
                String c2 = eoVar.c();
                String host = new URL(c2).getHost();
                if (!"http://abroad.apilocate.amap.com/mobile/binary".equals(c2) && !"abroad.apilocate.amap.com".equals(host)) {
                    String str = "apilocate.amap.com".equalsIgnoreCase(host) ? "httpdns.apilocate.amap.com" : host;
                    String a2 = a(str);
                    if (this.h && TextUtils.isEmpty(a2)) {
                        this.h = false;
                        a2 = ez.b(this.e, "ip", "last_ip", "");
                        if (!TextUtils.isEmpty(a2)) {
                            this.j = a2;
                        }
                    }
                    if (TextUtils.isEmpty(a2)) {
                        return;
                    }
                    this.k = a2;
                    eoVar.g = c2.replace(host, a2);
                    eoVar.b().put("host", str);
                    eoVar.a(str);
                    this.g = true;
                }
            }
        } catch (Throwable unused) {
        }
    }

    public final void b() {
        if (this.g) {
            ez.a(this.e, "pref", "dns_faile_count_total", 0L);
        }
    }

    final synchronized void b(eo eoVar) {
        try {
            eoVar.g = es.a();
            long b = ez.b(this.e, "pref", "dns_faile_count_total", 0L);
            if (b >= 2) {
                return;
            }
            bg.a();
            bg.a(eoVar, false);
            ez.a(this.e, "pref", "dns_faile_count_total", b + 1);
        } catch (Throwable unused) {
            ez.a(this.e, "pref", "dns_faile_count_total", 0L);
        }
    }

    public final void c() {
        String[] strArr;
        try {
            if (e()) {
                if (this.g && this.l != null && (strArr = this.l) != null) {
                    try {
                        if (strArr.length > 1) {
                            ArrayList arrayList = new ArrayList(12);
                            arrayList.addAll(Arrays.asList(strArr));
                            Iterator it = arrayList.iterator();
                            String str = (String) it.next();
                            it.remove();
                            arrayList.add(str);
                            arrayList.toArray(strArr);
                        }
                    } catch (Throwable unused) {
                    }
                }
                if (this.b > 5 || !this.g) {
                    return;
                }
                if (this.f == null) {
                    this.f = aq.d();
                }
                if (this.f.isShutdown()) {
                    return;
                }
                this.f.submit(new a(this.a));
            }
        } catch (Throwable unused2) {
        }
    }
}
