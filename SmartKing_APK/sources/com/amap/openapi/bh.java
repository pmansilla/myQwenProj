package com.amap.openapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.amap.location.collection.CollectionConfig;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* compiled from: WifiScanner.java */
/* loaded from: classes.dex */
public class bh {
    private Context a;
    private Looper b;
    private df c;
    private BroadcastReceiver f;
    private boolean h;
    private boolean i;
    private int j;
    private CollectionConfig.FpsCollectorConfig k;
    private boolean e = false;
    private Handler g = null;
    private final Object l = new Object();
    private List<ScanResult> m = new ArrayList();
    private long n = 0;
    private Comparator<ScanResult> o = new Comparator<ScanResult>() { // from class: com.amap.openapi.bh.2
        @Override // java.util.Comparator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(ScanResult scanResult, ScanResult scanResult2) {
            int compareTo = scanResult.BSSID.compareTo(scanResult.BSSID);
            if (compareTo > 0) {
                return 1;
            }
            return compareTo == 0 ? 0 : -1;
        }
    };
    private ReentrantReadWriteLock d = new ReentrantReadWriteLock();

    /* compiled from: WifiScanner.java */
    /* loaded from: classes.dex */
    public static class a {
        public List<ScanResult> a = new ArrayList();
        public long b;
    }

    /* compiled from: WifiScanner.java */
    /* loaded from: classes.dex */
    private final class b extends Handler {
        public b(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            try {
                switch (message.what) {
                    case 0:
                        bh.this.g();
                        bh.this.i();
                        return;
                    case 1:
                        bh.this.h();
                        bh.this.d.writeLock().lock();
                        try {
                            if (bh.this.g != null) {
                                bh.this.g.removeCallbacksAndMessages(null);
                                bh.this.g = null;
                            }
                            return;
                        } finally {
                            bh.this.d.writeLock().unlock();
                        }
                    case 2:
                        bh.this.i();
                        return;
                    default:
                        return;
                }
            } catch (Throwable unused) {
            }
        }
    }

    public bh(Context context, CollectionConfig.FpsCollectorConfig fpsCollectorConfig, Looper looper) {
        this.h = true;
        this.i = true;
        this.j = 20000;
        this.a = context;
        this.h = fpsCollectorConfig.isScanWifiAllowed();
        this.j = fpsCollectorConfig.getScanWifiInterval();
        this.i = fpsCollectorConfig.isScanActiveAllowed();
        this.k = fpsCollectorConfig;
        this.b = looper;
        this.c = df.a(this.a);
    }

    private void a(BroadcastReceiver broadcastReceiver) {
        if (broadcastReceiver == null || this.a == null) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.SCAN_RESULTS");
        try {
            this.a.registerReceiver(broadcastReceiver, intentFilter);
        } catch (Throwable unused) {
        }
    }

    private void b(BroadcastReceiver broadcastReceiver) {
        if (broadcastReceiver == null || this.a == null) {
            return;
        }
        try {
            this.a.unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        this.f = new BroadcastReceiver() { // from class: com.amap.openapi.bh.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    try {
                        if (intent.getAction() == null || !"android.net.wifi.SCAN_RESULTS".equals(intent.getAction())) {
                            return;
                        }
                        boolean z = true;
                        try {
                            if (bh.this.j() && intent.getExtras() != null) {
                                z = intent.getExtras().getBoolean("resultsUpdated", true);
                            }
                        } catch (Throwable unused) {
                        }
                        dl.a(100067);
                        if (z) {
                            synchronized (bh.this.l) {
                                bh.this.n = System.currentTimeMillis();
                                bh.this.m = bh.this.c.b();
                                as.b((List<ScanResult>) bh.this.m);
                            }
                        } else {
                            dl.a(100068);
                        }
                        if (bh.this.h) {
                            bh.this.d.readLock().lock();
                            try {
                                if (bh.this.g != null) {
                                    bh.this.g.removeMessages(2);
                                    bh.this.g.sendEmptyMessageDelayed(2, bh.this.j);
                                }
                            } finally {
                                bh.this.d.readLock().unlock();
                            }
                        }
                    } catch (Throwable unused2) {
                    }
                }
            }
        };
        a(this.f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h() {
        synchronized (this.l) {
            this.n = 0L;
            if (this.m != null) {
                this.m.clear();
            }
        }
        if (this.f != null) {
            b(this.f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i() {
        Object a2;
        if (this.h && this.c != null && this.c.c()) {
            boolean z = false;
            try {
                if (Build.VERSION.SDK_INT < 18 && this.i && (a2 = bc.a(this.c, "startScanActive", new Object[0])) != null) {
                    if ("true".equals(String.valueOf(a2))) {
                        z = true;
                    }
                }
            } catch (Exception unused) {
            }
            if (z) {
                return;
            }
            try {
                this.c.a();
            } catch (Exception unused2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean j() {
        CollectionConfig.FpsCollectorConfig fpsCollectorConfig = this.k;
        if (this.k != null) {
            return fpsCollectorConfig.isWifiFilterByUpdated();
        }
        return true;
    }

    public void a() {
        if (this.e) {
            return;
        }
        this.e = true;
        this.d.writeLock().lock();
        try {
            if (this.g == null) {
                this.g = new b(this.b);
            }
            this.g.sendEmptyMessage(0);
        } finally {
            this.d.writeLock().unlock();
        }
    }

    public void b() {
        if (this.e) {
            this.e = false;
            this.d.readLock().lock();
            try {
                if (this.g != null) {
                    this.g.sendEmptyMessage(1);
                }
            } finally {
                this.d.readLock().unlock();
            }
        }
    }

    public void c() {
        this.d.readLock().lock();
        try {
            if (this.g != null && !this.g.hasMessages(2)) {
                this.g.sendEmptyMessage(2);
            }
        } finally {
            this.d.readLock().unlock();
        }
    }

    public void d() {
        this.d.readLock().lock();
        try {
            if (this.g != null) {
                this.g.removeMessages(2);
            }
        } finally {
            this.d.readLock().unlock();
        }
    }

    public boolean e() {
        return this.e;
    }

    public a f() {
        a aVar = new a();
        synchronized (this.l) {
            if (this.m == null) {
                return aVar;
            }
            Iterator<ScanResult> it = this.m.iterator();
            while (it.hasNext()) {
                aVar.a.add(it.next());
            }
            aVar.b = this.n;
            return aVar;
        }
    }
}
