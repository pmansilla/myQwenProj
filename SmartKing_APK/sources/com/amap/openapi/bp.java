package com.amap.openapi;

import android.content.Context;
import android.support.annotation.NonNull;
import com.amap.location.common.log.ALLog;
import com.amap.location.common.model.AmapLoc;
import com.amap.location.common.model.CellStatus;
import com.amap.location.common.model.FPS;
import com.amap.location.common.model.WiFi;
import com.amap.location.common.model.WifiStatus;
import com.amap.location.offline.IOfflineCloudConfig;
import com.amap.location.offline.OfflineConfig;
import com.litesuits.orm.db.assit.SQLBuilder;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* compiled from: OfflineCore.java */
/* loaded from: classes.dex */
public class bp {
    private Context a;
    private OfflineConfig b;
    private cc c;
    private a d = new a();
    private int e = 0;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: OfflineCore.java */
    /* loaded from: classes.dex */
    public static class a implements Comparator<WiFi> {
        private a() {
        }

        @Override // java.util.Comparator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(WiFi wiFi, WiFi wiFi2) {
            return wiFi2.rssi - wiFi.rssi;
        }
    }

    public bp(@NonNull Context context, @NonNull OfflineConfig offlineConfig, @NonNull IOfflineCloudConfig iOfflineCloudConfig) {
        this.a = context;
        this.b = offlineConfig;
        this.c = new cc(context, offlineConfig, iOfflineCloudConfig);
    }

    private AmapLoc a(boolean z, AmapLoc amapLoc) {
        if (amapLoc == null) {
            return null;
        }
        if (z && AmapLoc.TYPE_OFFLINE_CELL.equals(amapLoc.getType())) {
            return null;
        }
        com.amap.location.offline.upload.a.a(z ? 100035 : 100036);
        return amapLoc;
    }

    private bs a(CellStatus cellStatus) {
        String a2 = cn.a(cellStatus);
        return by.a(this.a).a(a2, cn.a(a2));
    }

    private bu a(WifiStatus wifiStatus) {
        bu buVar = new bu();
        by.a(this.a).a(a(wifiStatus, buVar), buVar);
        return buVar;
    }

    private String a(WifiStatus wifiStatus, bu buVar) {
        StringBuilder sb = new StringBuilder();
        if (wifiStatus != null && wifiStatus.numWiFis() > 0) {
            List<WiFi> wifiList = wifiStatus.getWifiList();
            Collections.sort(wifiList, this.d);
            int min = Math.min(wifiList.size(), 30);
            buVar.a = min;
            boolean z = true;
            for (int i = 0; i < min; i++) {
                WiFi wiFi = wifiList.get(i);
                long a2 = cn.a(wiFi.mac);
                if (a2 != -1) {
                    if (z) {
                        z = false;
                    } else {
                        sb.append(',');
                    }
                    sb.append(a2);
                    bt btVar = new bt();
                    btVar.a = a2;
                    btVar.b = wiFi.mac;
                    btVar.c = wiFi.rssi;
                    buVar.b.put(Long.valueOf(a2), btVar);
                }
            }
        }
        return sb.toString();
    }

    private void a(AmapLoc amapLoc, bu buVar, bs bsVar, int i) {
        try {
            if (this.b.mLocateLogRecorder != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(amapLoc.getType());
                sb.append("|");
                sb.append(amapLoc.getLon());
                sb.append(",");
                sb.append(amapLoc.getLat());
                sb.append(",");
                sb.append(amapLoc.getAccuracy());
                sb.append("|");
                if (amapLoc.getType().equals(AmapLoc.TYPE_OFFLINE_WIFI)) {
                    sb.append((CharSequence) buVar.d);
                    sb.append("@");
                    sb.append((CharSequence) buVar.e);
                    sb.append("@");
                    sb.append(i);
                    sb.append("@");
                    sb.append(buVar.a);
                    if (bsVar != null) {
                        String str = bsVar.c + "," + bsVar.b + "," + bsVar.d;
                        sb.append("@");
                        sb.append(str);
                    }
                } else {
                    sb.append(bsVar.f);
                    sb.append("@");
                    sb.append(bsVar.c);
                    sb.append(",");
                    sb.append(bsVar.b);
                    sb.append(",");
                    sb.append(bsVar.d);
                }
                this.b.mLocateLogRecorder.onLocateSuccess(sb.toString().getBytes());
            }
        } catch (Exception unused) {
        }
    }

    public AmapLoc a(FPS fps, int i, boolean z) {
        AmapLoc amapLoc;
        String str;
        StringBuilder sb;
        try {
            bs a2 = a(fps.cellStatus);
            bu a3 = a(fps.wifiStatus);
            if (z) {
                amapLoc = null;
            } else {
                amapLoc = bq.a(a2, a3, i);
                if (amapLoc != null) {
                    str = "@_18_1_@";
                    sb = new StringBuilder("@_18_1_1_@");
                    sb.append(ALLog.logEncode(a2.toString() + "," + a3.toString() + "," + i));
                } else {
                    str = "@_18_1_@";
                    sb = new StringBuilder("@_18_1_3_@");
                    sb.append(a2.a);
                    sb.append(",");
                    sb.append(a3.a);
                    sb.append(",");
                    sb.append(a3.c);
                }
                ALLog.trace(str, sb.toString());
            }
            br.a(this.a, a2);
            br.a(this.a, a3);
            cl.a().a(this.a, a2);
            boolean z2 = true;
            this.e++;
            if (this.e > 20) {
                by.a(this.a).b();
                this.e = 0;
            }
            if (i <= 0) {
                z2 = false;
            }
            AmapLoc a4 = a(z2, amapLoc);
            if (a4 != null) {
                a(a4, a3, a2, i);
            }
            return a4;
        } catch (Throwable unused) {
            return null;
        }
    }

    public void a() {
        this.c.a();
    }

    public void a(FPS fps, AmapLoc amapLoc) {
        String str;
        StringBuilder sb;
        bs a2 = a(fps.cellStatus);
        bu a3 = a(fps.wifiStatus);
        if (bq.a(a2, a3, 0) != null) {
            str = "@_18_1_@";
            sb = new StringBuilder("@_18_1_2_@");
            sb.append(ALLog.logEncode(a2.toString() + "," + a3.toString() + ",(" + amapLoc.getLat() + "," + amapLoc.getLon() + SQLBuilder.PARENTHESES_RIGHT));
        } else {
            str = "@_18_1_@";
            sb = new StringBuilder("@_18_1_4_@");
            sb.append(a2.a);
            sb.append(",");
            sb.append(a3.c);
            sb.append(",");
            sb.append(a3.c);
        }
        ALLog.trace(str, sb.toString());
        if (amapLoc != null) {
            br.a(this.a, this.b, a2, a3, amapLoc);
        }
    }

    public void a(@NonNull OfflineConfig offlineConfig) {
        this.c.a(offlineConfig);
    }

    public void b() {
        this.c.b();
    }
}
