package com.amap.location.common.model;

import android.net.wifi.ScanResult;
import android.os.Build;
import android.os.SystemClock;
import com.amap.location.common.util.f;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class WifiStatus {
    public WiFi mainWifi;
    public long updateTime;
    private List<WiFi> wifiList;

    public WifiStatus() {
        this.wifiList = Collections.emptyList();
    }

    public WifiStatus(long j) {
        this.wifiList = Collections.emptyList();
        this.updateTime = j;
    }

    public WifiStatus(long j, List<ScanResult> list) {
        this.wifiList = Collections.emptyList();
        this.updateTime = j;
        this.wifiList = scanResultList2WifiList(list);
    }

    public WifiStatus(long j, List<WiFi> list, int i) {
        this.wifiList = Collections.emptyList();
        this.updateTime = j;
        this.wifiList = list;
    }

    private String toStr(boolean z) {
        String str;
        String str2;
        StringBuilder sb;
        String obj;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("wifiStatus:[");
        sb2.append("updateTime=" + this.updateTime + ",");
        if (this.mainWifi != null) {
            str = "mainWifi:[" + this.mainWifi.toString() + "],";
        } else {
            str = "mainWifi:[null],";
        }
        sb2.append(str);
        if (this.wifiList != null) {
            ArrayList arrayList = new ArrayList();
            if (this.wifiList.size() <= 5) {
                arrayList.addAll(this.wifiList);
                sb = new StringBuilder("wifiList=");
            } else if (z) {
                arrayList.addAll(this.wifiList.subList(0, 5));
                sb = new StringBuilder("wifiList=");
                obj = arrayList.toString();
                sb.append(obj);
                str2 = sb.toString();
            } else {
                arrayList.addAll(this.wifiList);
                sb = new StringBuilder("wifiList=");
            }
            obj = this.wifiList.toString();
            sb.append(obj);
            str2 = sb.toString();
        } else {
            str2 = "wifiList=0";
        }
        sb2.append(str2);
        sb2.append("]");
        return sb2.toString();
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public WifiStatus m19clone() {
        WifiStatus wifiStatus = new WifiStatus(this.updateTime);
        if (this.mainWifi != null) {
            wifiStatus.mainWifi = this.mainWifi.m18clone();
        }
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.wifiList);
        wifiStatus.wifiList = arrayList;
        return wifiStatus;
    }

    public final WiFi getWiFi(int i) {
        return this.wifiList.get(i);
    }

    public List<WiFi> getWifiList() {
        return this.wifiList;
    }

    public final int numWiFis() {
        return this.wifiList.size();
    }

    public List<WiFi> scanResultList2WifiList(List<ScanResult> list) {
        ArrayList arrayList = new ArrayList();
        Iterator<ScanResult> it = list.iterator();
        if (Build.VERSION.SDK_INT >= 17) {
            while (it.hasNext()) {
                ScanResult next = it.next();
                if (next != null) {
                    arrayList.add(new WiFi(f.a(next.BSSID), next.SSID, next.level, next.frequency, next.timestamp / 1000));
                }
            }
        } else {
            while (it.hasNext()) {
                ScanResult next2 = it.next();
                if (next2 != null) {
                    arrayList.add(new WiFi(f.a(next2.BSSID), next2.SSID, next2.level, next2.frequency, SystemClock.elapsedRealtime()));
                }
            }
        }
        return arrayList;
    }

    public void setWifiList(List<WiFi> list) {
        this.wifiList = list;
    }

    public String toString() {
        return toStr(false);
    }

    public String toStringSimple() {
        return toStr(true);
    }
}
