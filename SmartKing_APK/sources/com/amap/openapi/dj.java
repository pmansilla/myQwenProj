package com.amap.openapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import com.amap.location.common.log.ALLog;
import com.amap.openapi.df;
import java.util.List;

/* compiled from: SystemWifiProvider.java */
/* loaded from: classes.dex */
public class dj implements di {
    private WifiManager a;

    public dj(Context context) {
        this.a = (WifiManager) context.getSystemService("wifi");
    }

    @Override // com.amap.openapi.di
    @Nullable
    @RequiresPermission("android.permission.ACCESS_WIFI_STATE")
    public List<ScanResult> a() {
        try {
            if (this.a == null) {
                return null;
            }
            return this.a.getScanResults();
        } catch (SecurityException unused) {
            ALLog.trace("@_24_3_@", "@_24_3_1_@");
            return null;
        }
    }

    @Override // com.amap.openapi.di
    public void a(Context context, final df.a aVar) {
        try {
            context.getApplicationContext().registerReceiver(new BroadcastReceiver() { // from class: com.amap.openapi.dj.1
                df.a a;

                {
                    this.a = aVar;
                }

                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context2, Intent intent) {
                    if (intent == null || !"android.net.wifi.SCAN_RESULTS".equals(intent.getAction()) || this.a == null) {
                        return;
                    }
                    this.a.a();
                }
            }, new IntentFilter("android.net.wifi.SCAN_RESULTS"));
        } catch (Throwable unused) {
        }
    }

    @Override // com.amap.openapi.di
    @RequiresPermission("android.permission.CHANGE_WIFI_STATE")
    public boolean b() {
        try {
            if (this.a != null) {
                return this.a.startScan();
            }
            return false;
        } catch (SecurityException unused) {
            ALLog.trace("@_24_3_@", "@_24_3_2_@");
            return false;
        } catch (Exception e) {
            ALLog.trace("@_24_3_@", "@_24_3_3_@" + e.toString());
            return false;
        }
    }

    @Override // com.amap.openapi.di
    public boolean c() {
        try {
            if (this.a == null) {
                return false;
            }
            return this.a.isWifiEnabled();
        } catch (Exception e) {
            ALLog.trace("@_24_3_@", "@_24_3_9_@", e);
            return false;
        }
    }
}
