package com.amap.openapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import com.amap.location.common.model.CellStatus;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* compiled from: CellCollector.java */
/* loaded from: classes.dex */
public class l {
    private static final String a = "l";
    private Context b;
    private Handler c;
    private TelephonyManager e;
    private CellLocation f;
    private long g;
    private SignalStrength h;
    private boolean i;
    private CellLocation j;
    private CellInfo k;
    private Location l;
    private q m = new q();
    private q n = new q();
    private final List<CellStatus.HistoryCell> o = new ArrayList(3);
    private BroadcastReceiver p = new BroadcastReceiver() { // from class: com.amap.openapi.l.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action;
            if (intent == null || (action = intent.getAction()) == null) {
                return;
            }
            char c = 65535;
            if (action.hashCode() == -1076576821 && action.equals("android.intent.action.AIRPLANE_MODE")) {
                c = 0;
            }
            if (c != 0) {
                return;
            }
            l.this.i = !ax.a(l.this.b);
            if (l.this.i) {
                return;
            }
            l.this.f = null;
            l.this.g = 0L;
        }
    };
    private PhoneStateListener q = new PhoneStateListener() { // from class: com.amap.openapi.l.2
        @Override // android.telephony.PhoneStateListener
        public void onCellInfoChanged(List<CellInfo> list) {
            l.this.d.readLock().lock();
            try {
                if (l.this.c != null) {
                    l.this.c.post(new Runnable() { // from class: com.amap.openapi.l.2.3
                        @Override // java.lang.Runnable
                        public void run() {
                            l.this.e();
                        }
                    });
                }
            } finally {
                l.this.d.readLock().unlock();
            }
        }

        @Override // android.telephony.PhoneStateListener
        public void onCellLocationChanged(final CellLocation cellLocation) {
            l.this.d.readLock().lock();
            try {
                if (l.this.c != null) {
                    l.this.c.post(new Runnable() { // from class: com.amap.openapi.l.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            l.this.f = cellLocation;
                            l.this.g = SystemClock.elapsedRealtime();
                            l.this.e();
                        }
                    });
                }
            } finally {
                l.this.d.readLock().unlock();
            }
        }

        @Override // android.telephony.PhoneStateListener
        public void onSignalStrengthsChanged(final SignalStrength signalStrength) {
            l.this.d.readLock().lock();
            try {
                if (l.this.c != null) {
                    l.this.c.post(new Runnable() { // from class: com.amap.openapi.l.2.2
                        @Override // java.lang.Runnable
                        public void run() {
                            l.this.h = signalStrength;
                            l.this.e();
                        }
                    });
                }
            } finally {
                l.this.d.readLock().unlock();
            }
        }
    };
    private final ReentrantReadWriteLock d = new ReentrantReadWriteLock();

    public l(Context context, Looper looper) {
        this.b = context;
        this.e = (TelephonyManager) this.b.getSystemService("phone");
        this.c = new Handler(looper);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void a(q qVar) {
        List<CellStatus.HistoryCell> list;
        synchronized (this.o) {
            Iterator<r> it = qVar.c.iterator();
            while (it.hasNext()) {
                r next = it.next();
                if (1 == next.b) {
                    CellStatus.HistoryCell historyCell = new CellStatus.HistoryCell();
                    historyCell.lastUpdateTimeMills = SystemClock.elapsedRealtime();
                    historyCell.type = next.a;
                    switch (next.a) {
                        case 1:
                            if (next.f == 0) {
                                break;
                            } else {
                                w wVar = (w) next.f;
                                if (com.amap.location.common.util.f.a(wVar.c) && com.amap.location.common.util.f.b(wVar.d)) {
                                    historyCell.lac = wVar.c;
                                    historyCell.cid = wVar.d;
                                    historyCell.rssi = wVar.e;
                                    list = this.o;
                                    break;
                                }
                            }
                            break;
                        case 2:
                            if (next.f == 0) {
                                break;
                            } else {
                                p pVar = (p) next.f;
                                if (com.amap.location.common.util.f.c(pVar.a) && com.amap.location.common.util.f.d(pVar.b) && com.amap.location.common.util.f.e(pVar.c)) {
                                    historyCell.sid = pVar.a;
                                    historyCell.nid = pVar.b;
                                    historyCell.bid = pVar.c;
                                    historyCell.rssi = pVar.f;
                                    list = this.o;
                                    break;
                                }
                            }
                            break;
                        case 3:
                            if (next.f == 0) {
                                break;
                            } else {
                                x xVar = (x) next.f;
                                if (com.amap.location.common.util.f.a(xVar.c) && com.amap.location.common.util.f.b(xVar.d)) {
                                    historyCell.lac = xVar.c;
                                    historyCell.cid = xVar.d;
                                    historyCell.rssi = xVar.f;
                                    list = this.o;
                                    break;
                                }
                            }
                            break;
                        case 4:
                            if (next.f == 0) {
                                break;
                            } else {
                                z zVar = (z) next.f;
                                if (com.amap.location.common.util.f.a(zVar.c) && com.amap.location.common.util.f.b(zVar.d)) {
                                    historyCell.lac = zVar.c;
                                    historyCell.cid = zVar.d;
                                    historyCell.rssi = zVar.f;
                                    list = this.o;
                                    break;
                                }
                            }
                            break;
                        default:
                            continue;
                    }
                    com.amap.location.common.util.f.a(historyCell, list, 3);
                }
            }
            this.m.d.clear();
            this.m.d.addAll(this.o);
        }
    }

    private boolean b(Location location) {
        return location.distanceTo(this.l) > ((location.getSpeed() > 10.0f ? 1 : (location.getSpeed() == 10.0f ? 0 : -1)) > 0 ? 2000.0f : (location.getSpeed() > 2.0f ? 1 : (location.getSpeed() == 2.0f ? 0 : -1)) > 0 ? 500.0f : 100.0f);
    }

    private CellLocation c() {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (!((this.f == null || this.g == 0 || elapsedRealtime - this.g > 1500) ? false : true)) {
            try {
                this.f = this.e != null ? this.e.getCellLocation() : null;
                this.g = elapsedRealtime;
            } catch (Exception unused) {
                this.f = null;
                this.g = 0L;
            }
        }
        return this.f;
    }

    private List<CellInfo> d() {
        try {
            if (this.e == null || Build.VERSION.SDK_INT < 17) {
                return null;
            }
            return this.e.getAllCellInfo();
        } catch (Exception unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        if (this.i) {
            try {
                CellLocation c = c();
                if ((c instanceof CdmaCellLocation) && -1 == ((CdmaCellLocation) c).getNetworkId()) {
                    c = null;
                }
                List<CellInfo> d = d();
                CellInfo a2 = d != null ? ax.a(d) : null;
                if (c == null && a2 == null) {
                    return;
                }
                ax.a(this.b, this.n, c, this.h, d);
                as.a(this.n.c);
            } catch (Throwable unused) {
            }
        }
    }

    public q a(Location location) {
        if (!this.i) {
            return null;
        }
        CellLocation c = c();
        if ((c instanceof CdmaCellLocation) && -1 == ((CdmaCellLocation) c).getNetworkId()) {
            c = null;
        }
        List<CellInfo> d = d();
        CellInfo a2 = d != null ? ax.a(d) : null;
        if (c == null && a2 == null) {
            return null;
        }
        if (!(this.l == null || b(location) || !ax.a(c, this.j) || !ax.a(a2, this.k))) {
            return null;
        }
        ax.a(this.b, this.m, c, this.h, d);
        this.j = c;
        this.k = a2;
        this.l = location;
        as.a(this.m.c);
        a(this.m);
        return this.m;
    }

    public void a() {
        this.i = !ax.a(this.b);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        try {
            this.b.registerReceiver(this.p, intentFilter, null, this.c);
            if (this.e != null) {
                this.e.listen(this.q, Build.VERSION.SDK_INT >= 17 ? 1296 : 272);
            }
        } catch (Exception unused) {
        }
    }

    public void b() {
        try {
            this.b.unregisterReceiver(this.p);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.e != null) {
            this.e.listen(this.q, 0);
        }
        this.d.writeLock().lock();
        try {
            this.c.removeCallbacksAndMessages(null);
            this.c = null;
        } finally {
            this.d.writeLock().unlock();
        }
    }
}
