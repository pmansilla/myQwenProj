package com.amap.location.collection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.media.session.PlaybackStateCompat;
import com.amap.location.common.network.IHttpClient;
import com.amap.location.common.util.f;
import com.amap.openapi.as;
import com.amap.openapi.at;
import com.amap.openapi.au;
import com.amap.openapi.av;
import com.amap.openapi.az;
import com.amap.openapi.bf;
import com.amap.openapi.bg;
import com.amap.openapi.k;
import com.amap.openapi.m;
import com.amap.openapi.n;
import com.amap.openapi.t;
import java.util.List;

/* compiled from: CollectionManager.java */
/* loaded from: classes.dex */
public class a {
    private Context a;
    private CollectionConfig b;
    private IHttpClient c;
    private t d;
    private av e;
    private m f;
    private n g;
    private HandlerThread h;
    private volatile b i;
    private Looper j;
    private boolean k = false;
    private final Object l = new Object();
    private C0026a m;
    private k n;
    private bg o;
    private bf p;

    /* compiled from: CollectionManager.java */
    /* renamed from: com.amap.location.collection.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    private class C0026a extends BroadcastReceiver {
        private C0026a() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                char c = 65535;
                int hashCode = action.hashCode();
                if (hashCode != -2128145023) {
                    if (hashCode == -1454123155 && action.equals("android.intent.action.SCREEN_ON")) {
                        c = 1;
                    }
                } else if (action.equals("android.intent.action.SCREEN_OFF")) {
                    c = 0;
                }
                switch (c) {
                    case 0:
                        if (a.this.b.isStopCollectionWhenScreenOff()) {
                            a.this.f();
                            return;
                        }
                        return;
                    case 1:
                        if (a.this.b.isStopCollectionWhenScreenOff()) {
                            a.this.e();
                            return;
                        }
                        return;
                    default:
                        return;
                }
            } catch (Throwable unused) {
            }
        }
    }

    /* compiled from: CollectionManager.java */
    /* loaded from: classes.dex */
    class b extends Handler {
        b(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            if (a.this.m != null) {
                try {
                    a.this.a.unregisterReceiver(a.this.m);
                    a.this.m = null;
                } catch (Throwable unused) {
                }
            }
            a.this.f();
            removeCallbacksAndMessages(null);
            a.this.e.b();
            a.this.d.b();
            post(new Runnable() { // from class: com.amap.location.collection.a.b.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        a.this.h.quit();
                    } catch (Throwable unused2) {
                    }
                }
            });
        }
    }

    public a(Context context, CollectionConfig collectionConfig, IHttpClient iHttpClient) {
        this.a = context;
        this.b = collectionConfig;
        this.c = iHttpClient;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Location location, List<ScanResult> list, long j, long j2) {
        try {
            g();
            if (this.b.getFpsCollectorConfig().isEnabled()) {
                this.f.a(location, list, j, j2);
            }
            if (this.b.getTrackCollectorConfig().isEnabled()) {
                this.g.a(location);
            }
        } catch (Throwable unused) {
        }
    }

    private boolean d() {
        return this.b.getFpsCollectorConfig().isEnabled() || this.b.getTrackCollectorConfig().isEnabled();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        if (this.p != null) {
            return;
        }
        boolean isEnabled = this.b.getFpsCollectorConfig().isEnabled();
        boolean isEnabled2 = this.b.getTrackCollectorConfig().isEnabled();
        long j = 0;
        int i = 0;
        if (isEnabled) {
            j = 1000;
            i = 10;
        }
        if (isEnabled2) {
            j = isEnabled ? Math.min(j, 2000L) : 2000L;
            i = isEnabled ? Math.min(i, 5) : 5;
        }
        try {
            this.p = new bf() { // from class: com.amap.location.collection.a.2
                @Override // com.amap.openapi.bf
                public void a(Location location, List<ScanResult> list, long j2, long j3) {
                    a.this.a(location, list, j2, j3);
                }
            };
            if (this.o == null) {
                this.o = new bg(this.a, this.b.getFpsCollectorConfig(), this.p, this.j);
            }
            this.o.a("passive", j, i);
        } catch (Throwable unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
        try {
            if (this.p == null || this.o == null) {
                return;
            }
            this.o.c();
            this.o.a();
            this.p = null;
            h();
            as.a();
        } catch (Throwable unused) {
        }
    }

    private void g() {
        if (this.b.getFpsCollectorConfig().isEnabled() && this.f == null) {
            this.f = new m(this.a, this.d, this.b.getFpsCollectorConfig(), this.j);
            this.f.a();
        }
        if (this.b.getTrackCollectorConfig().isEnabled() && this.g == null) {
            this.g = new n(this.a, this.d, this.b.getTrackCollectorConfig(), this.j);
            this.g.a();
        }
    }

    private void h() {
        if (this.f != null) {
            this.f.b();
            this.f = null;
        }
        if (this.g != null) {
            this.g.b();
            this.g = null;
        }
    }

    public void a() {
        if (d()) {
            this.h = new HandlerThread("collection") { // from class: com.amap.location.collection.a.1
                @Override // android.os.HandlerThread
                protected void onLooperPrepared() {
                    try {
                        a.this.j = getLooper();
                        a.this.d = new t(a.this.a, a.this.j);
                        a.this.d.a();
                        a.this.e = new av(a.this.a, a.this.j, a.this.d, a.this.c, a.this.b);
                        a.this.e.a();
                        synchronized (a.this.l) {
                            a.this.i = new b(a.this.j);
                            if (a.this.k) {
                                a.this.k = false;
                                a.this.i.obtainMessage(1).sendToTarget();
                            }
                        }
                        if (a.this.b.isStopCollectionWhenScreenOff()) {
                            a.this.m = new C0026a();
                            IntentFilter intentFilter = new IntentFilter();
                            intentFilter.addAction("android.intent.action.SCREEN_ON");
                            intentFilter.addAction("android.intent.action.SCREEN_OFF");
                            try {
                                a.this.a.registerReceiver(a.this.m, intentFilter, null, a.this.i);
                            } catch (Throwable unused) {
                            }
                            if (!az.c(a.this.a)) {
                                return;
                            }
                        }
                        a.this.e();
                    } catch (Throwable unused2) {
                    }
                }
            };
            this.h.start();
        }
    }

    public void a(boolean z, at atVar) {
        if (atVar == null || this.i == null) {
            return;
        }
        try {
            au auVar = (au) atVar.b;
            this.e.a(f.a(this.a), auVar);
            if (z) {
                this.e.a(auVar);
            }
        } catch (Throwable unused) {
        }
    }

    public void b() {
        synchronized (this.l) {
            if (this.i != null) {
                this.i.obtainMessage(1).sendToTarget();
            } else {
                this.k = true;
            }
        }
    }

    public at c() {
        au a;
        byte[] a2;
        if (this.i == null) {
            return null;
        }
        try {
            if (this.n == null) {
                this.n = new k();
            }
            if (this.e.a(f.a(this.a)) <= 0 || (a = this.e.a(true, 1, PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)) == null || a.b.size() <= 0 || (a2 = this.n.a(this.a, this.b, a)) == null) {
                return null;
            }
            at atVar = new at();
            try {
                atVar.a = a2;
                atVar.b = a;
            } catch (Throwable unused) {
            }
            return atVar;
        } catch (Throwable unused2) {
            return null;
        }
    }
}
