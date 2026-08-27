package com.amap.openapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import com.amap.location.collection.CollectionConfig;
import com.amap.location.common.log.ALLog;
import com.amap.location.common.network.HttpRequest;
import com.amap.location.common.network.HttpResponse;
import com.amap.location.common.network.IHttpClient;
import com.amap.openapi.bj;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.Executor;
import no.nordicsemi.android.dfu.DfuBaseService;

/* compiled from: UploadManager.java */
/* loaded from: classes.dex */
public class av {
    private Context a;
    private Handler b;
    private CollectionConfig c;
    private SharedPreferences d;
    private ConnectivityManager e;
    private BroadcastReceiver f;
    private t g;
    private IHttpClient i;
    private Looper l;
    private bj j = new bj();
    private a k = new a();
    private k h = new k();

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: UploadManager.java */
    /* loaded from: classes.dex */
    public class a implements bj.a {
        private boolean b;

        private a() {
            this.b = true;
        }

        @Override // com.amap.openapi.bj.a
        public Object a(long j) {
            av avVar;
            boolean z;
            if (this.b) {
                avVar = av.this;
                z = true;
            } else {
                avVar = av.this;
                z = false;
            }
            return avVar.a(z, 10000, j);
        }

        @Override // com.amap.openapi.bj.a
        public void a() {
        }

        @Override // com.amap.openapi.bj.a
        public void a(int i) {
            ALLog.trace("@_3_3_@", "@_3_3_2_@" + i);
        }

        @Override // com.amap.openapi.bj.a
        public void a(int i, Object obj) {
            av.this.a(i, obj);
        }

        @Override // com.amap.openapi.bj.a
        public boolean a(Object obj) {
            au auVar = (au) obj;
            byte[] a = av.this.h.a(av.this.a, av.this.c, auVar);
            boolean z = false;
            if (a != null) {
                byte[] bArr = null;
                try {
                    boolean z2 = auVar.b.get(0).b() == 0;
                    HashMap hashMap = new HashMap();
                    hashMap.put("Content-Type", DfuBaseService.MIME_TYPE_OCTET_STREAM);
                    HttpRequest httpRequest = new HttpRequest();
                    httpRequest.headers = hashMap;
                    httpRequest.body = a;
                    httpRequest.url = z2 ? CollectionConfig.sUseTestNet ? "http://aps.testing.amap.com/collection/collectData?src=baseCol&ver=v74&" : "http://cgicol.amap.com/collection/collectData?src=baseCol&ver=v74&" : CollectionConfig.sUseTestNet ? "http://aps.testing.amap.com/collection/collectData?src=extCol&ver=v74&" : "http://cgicol.amap.com/collection/collectData?src=extCol&ver=v74&";
                    HttpResponse post = av.this.i.post(httpRequest);
                    if (post != null && post.statusCode == 200) {
                        bArr = post.body;
                    }
                    if (bArr != null && "true".equals(new String(bArr, "UTF-8"))) {
                        z = true;
                    }
                    StringBuilder sb = new StringBuilder("@_3_3_1_@");
                    sb.append(bArr != null ? new String(bArr, "UTF-8") : "null");
                    ALLog.trace("@_3_3_@", sb.toString());
                } catch (Exception unused) {
                }
            }
            return z;
        }

        @Override // com.amap.openapi.bj.a
        public void b() {
        }

        @Override // com.amap.openapi.bj.a
        public void b(Object obj) {
            av.this.a((au) obj);
            this.b = !this.b;
        }

        @Override // com.amap.openapi.bj.a
        public boolean b(int i) {
            if (i == 1) {
                return true;
            }
            if (i == 0) {
                return av.this.c.getUploadConfig().isNonWifiUploadEnabled();
            }
            return false;
        }

        /* JADX WARN: Code restructure failed: missing block: B:18:0x0023, code lost:
        
            if (r3.a.g.c() > 512000) goto L6;
         */
        /* JADX WARN: Code restructure failed: missing block: B:23:0x0040, code lost:
        
            if (r3.a.g.d() > 512000) goto L6;
         */
        @Override // com.amap.openapi.bj.a
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public long c() {
            /*
                r3 = this;
                boolean r0 = r3.b
                r1 = 512000(0x7d000, float:7.17465E-40)
                if (r0 != 0) goto L26
                com.amap.openapi.av r0 = com.amap.openapi.av.this
                com.amap.openapi.t r0 = com.amap.openapi.av.b(r0)
                int r0 = r0.d()
                r2 = 1
                if (r0 > 0) goto L17
            L14:
                r3.b = r2
                goto L43
            L17:
                if (r0 >= r1) goto L43
                com.amap.openapi.av r0 = com.amap.openapi.av.this
                com.amap.openapi.t r0 = com.amap.openapi.av.b(r0)
                int r0 = r0.c()
                if (r0 <= r1) goto L43
                goto L14
            L26:
                com.amap.openapi.av r0 = com.amap.openapi.av.this
                com.amap.openapi.t r0 = com.amap.openapi.av.b(r0)
                int r0 = r0.c()
                r2 = 0
                if (r0 > 0) goto L34
                goto L14
            L34:
                if (r0 >= r1) goto L43
                com.amap.openapi.av r0 = com.amap.openapi.av.this
                com.amap.openapi.t r0 = com.amap.openapi.av.b(r0)
                int r0 = r0.d()
                if (r0 <= r1) goto L43
                goto L14
            L43:
                boolean r0 = r3.b
                if (r0 == 0) goto L52
                com.amap.openapi.av r0 = com.amap.openapi.av.this
                com.amap.openapi.t r0 = com.amap.openapi.av.b(r0)
                int r0 = r0.c()
                goto L5c
            L52:
                com.amap.openapi.av r0 = com.amap.openapi.av.this
                com.amap.openapi.t r0 = com.amap.openapi.av.b(r0)
                int r0 = r0.d()
            L5c:
                r2 = 4000(0xfa0, float:5.605E-42)
                if (r0 <= r2) goto L63
                r0 = 512000(0x7d000, float:7.17465E-40)
            L63:
                long r0 = (long) r0
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.av.a.c():long");
        }

        @Override // com.amap.openapi.bj.a
        public long c(int i) {
            return av.this.a(i);
        }

        @Override // com.amap.openapi.bj.a
        public int d() {
            return av.this.c.getUploadConfig().getMaxUploadFailCount();
        }

        @Override // com.amap.openapi.bj.a
        public long d(int i) {
            return i == 1 ? 512000L : 51200L;
        }

        @Override // com.amap.openapi.bj.a
        public long e() {
            return 300000L;
        }

        @Override // com.amap.openapi.bj.a
        public int f() {
            return 20000;
        }

        @Override // com.amap.openapi.bj.a
        public void g() {
        }

        @Override // com.amap.openapi.bj.a
        public Executor h() {
            return null;
        }
    }

    public av(Context context, Looper looper, t tVar, IHttpClient iHttpClient, CollectionConfig collectionConfig) {
        this.a = context;
        this.l = looper;
        this.g = tVar;
        this.i = iHttpClient;
        this.c = collectionConfig;
        this.e = (ConnectivityManager) context.getSystemService("connectivity");
        this.d = context.getSharedPreferences("AMAP_LOCATION_COLLECTOR", 0);
        if (e()) {
            return;
        }
        f();
    }

    private void a(String str, int i) {
        SharedPreferences.Editor edit = this.d.edit();
        edit.putInt(str, i);
        if (Build.VERSION.SDK_INT >= 9) {
            edit.apply();
        } else {
            edit.commit();
        }
    }

    private void c() {
        this.f = new BroadcastReceiver() { // from class: com.amap.openapi.av.1
            /* JADX WARN: Code restructure failed: missing block: B:6:0x0011, code lost:
            
                if (r2.isConnected() == false) goto L7;
             */
            @Override // android.content.BroadcastReceiver
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onReceive(android.content.Context r1, android.content.Intent r2) {
                /*
                    r0 = this;
                    r1 = 1
                    com.amap.openapi.av r2 = com.amap.openapi.av.this     // Catch: java.lang.Throwable -> L14
                    android.net.ConnectivityManager r2 = com.amap.openapi.av.f(r2)     // Catch: java.lang.Throwable -> L14
                    android.net.NetworkInfo r2 = r2.getActiveNetworkInfo()     // Catch: java.lang.Throwable -> L14
                    if (r2 == 0) goto L13
                    boolean r2 = r2.isConnected()     // Catch: java.lang.Throwable -> L14
                    if (r2 != 0) goto L14
                L13:
                    r1 = 0
                L14:
                    boolean r2 = r0.isInitialStickyBroadcast()     // Catch: java.lang.Throwable -> L21
                    if (r2 != 0) goto L21
                    if (r1 == 0) goto L21
                    com.amap.openapi.av r1 = com.amap.openapi.av.this     // Catch: java.lang.Throwable -> L21
                    com.amap.openapi.av.g(r1)     // Catch: java.lang.Throwable -> L21
                L21:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.av.AnonymousClass1.onReceive(android.content.Context, android.content.Intent):void");
            }
        };
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            this.a.registerReceiver(this.f, intentFilter, null, this.b);
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        try {
            if (g()) {
                this.j.a(500L);
            }
        } catch (Throwable unused) {
        }
    }

    private boolean e() {
        return Calendar.getInstance().get(6) == this.d.getInt("today_value", 0);
    }

    private void f() {
        a("today_value", Calendar.getInstance().get(6));
        a("uploaded_wifi_size", 0);
        a("uploaded_gprs_size", 0);
    }

    private boolean g() {
        return be.a(this.a) || this.c.getUploadConfig().isNonWifiUploadEnabled();
    }

    public synchronized long a(int i) {
        int i2;
        int maxMobileUploadSizePerDay;
        SharedPreferences sharedPreferences;
        String str;
        i2 = 0;
        try {
            if (i == 1) {
                if (!e()) {
                    f();
                }
                maxMobileUploadSizePerDay = this.c.getUploadConfig().getMaxWifiUploadSizePerDay();
                sharedPreferences = this.d;
                str = "uploaded_wifi_size";
            } else {
                if (i == 0) {
                    if (!e()) {
                        f();
                    }
                    maxMobileUploadSizePerDay = this.c.getUploadConfig().getMaxMobileUploadSizePerDay();
                    sharedPreferences = this.d;
                    str = "uploaded_gprs_size";
                }
            }
            i2 = maxMobileUploadSizePerDay - sharedPreferences.getInt(str, 0);
        } catch (Throwable th) {
            throw th;
        }
        return i2;
    }

    public synchronized au a(boolean z, int i, long j) {
        return this.g.a(z, i, j);
    }

    public void a() {
        this.b = new Handler(this.l);
        this.j.a(this.a, this.k, this.l);
        c();
        d();
    }

    public synchronized void a(int i, Object obj) {
        if (obj == null) {
            return;
        }
        if (!e()) {
            f();
        }
        au auVar = (au) obj;
        if (i == 1) {
            a("uploaded_wifi_size", this.d.getInt("uploaded_wifi_size", 0) + auVar.c);
        } else {
            if (i == 0) {
                a("uploaded_gprs_size", this.d.getInt("uploaded_gprs_size", 0) + auVar.c);
            }
        }
    }

    public synchronized void a(au auVar) {
        if (auVar != null) {
            this.g.a(auVar);
        }
    }

    public void b() {
        try {
            this.j.a();
            if (this.f != null) {
                this.a.unregisterReceiver(this.f);
                this.f = null;
            }
        } catch (Throwable unused) {
        }
        this.b.removeCallbacksAndMessages(null);
        this.b = null;
    }
}
