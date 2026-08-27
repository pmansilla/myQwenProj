package com.amap.openapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import java.util.ArrayList;
import java.util.List;

/* compiled from: WifiCollector.java */
/* loaded from: classes.dex */
public class o {
    private static final String a = "o";
    private Context b;
    private Handler c;
    private WifiManager d;
    private BroadcastReceiver e;
    private Location h;
    private long f = -1;
    private List<ScanResult> g = new ArrayList();
    private ArrayList<aa> i = new ArrayList<>();

    public o(Context context, Looper looper) {
        this.b = context;
        this.d = (WifiManager) context.getSystemService("wifi");
        this.c = new Handler(looper);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Intent intent) {
        String action = intent.getAction();
        if (action == null) {
            return;
        }
        char c = 65535;
        if (action.hashCode() == -343630553 && action.equals("android.net.wifi.STATE_CHANGE")) {
            c = 0;
        }
        if (c != 0) {
            return;
        }
        d();
    }

    private boolean a(Location location) {
        float f = 10.0f;
        if (location.getSpeed() > 10.0f) {
            f = 200.0f;
        } else if (location.getSpeed() > 2.0f) {
            f = 50.0f;
        }
        return location.distanceTo(this.h) > f;
    }

    private boolean a(Location location, long j, long j2) {
        return j > 0 && j2 - j < ((long) ((location.getSpeed() > 10.0f ? 1 : (location.getSpeed() == 10.0f ? 0 : -1)) >= 0 ? 2000 : 3500));
    }

    private boolean b(Location location, List<ScanResult> list, long j, long j2) {
        if (!be.a(this.d) || !a(location, j, j2) || list == null || list.size() <= 0) {
            return false;
        }
        if (this.h == null) {
            return true;
        }
        boolean a2 = a(location);
        return !a2 ? !be.a(list, this.g, 0.5d) : a2;
    }

    private void d() {
        this.f = -1L;
        try {
            WifiInfo connectionInfo = this.d == null ? null : this.d.getConnectionInfo();
            if (connectionInfo != null) {
                this.f = com.amap.location.common.util.f.a(connectionInfo.getBSSID());
            }
        } catch (Throwable unused) {
        }
    }

    public List<aa> a(Location location, List<ScanResult> list, long j, long j2) {
        if (!b(location, list, j, j2)) {
            return null;
        }
        be.a(this.i, list);
        this.g.clear();
        this.g.addAll(list);
        this.h = location;
        return this.i;
    }

    public void a() {
        this.e = new BroadcastReceiver() { // from class: com.amap.openapi.o.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                try {
                    o.this.a(intent);
                } catch (Throwable unused) {
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        try {
            this.b.registerReceiver(this.e, intentFilter, null, this.c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        d();
    }

    public void b() {
        if (this.e != null) {
            try {
                this.b.unregisterReceiver(this.e);
                this.e = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.c.removeCallbacksAndMessages(null);
        this.c = null;
    }

    public long c() {
        return this.f;
    }
}
