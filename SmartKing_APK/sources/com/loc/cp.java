package com.loc;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.text.TextUtils;
import com.amap.location.common.model.AmapLoc;
import com.amap.location.common.model.CellState;
import com.amap.location.common.model.CellStatus;
import com.amap.location.common.model.FPS;
import com.amap.location.common.model.WifiStatus;
import com.amap.location.common.network.IHttpClient;
import com.amap.location.offline.OfflineConfig;
import com.amap.location.offline.OfflineManager;
import com.amap.location.offline.upload.UploadConfig;
import java.util.ArrayList;
import org.apache.commons.lang.time.DateUtils;
import org.json.JSONObject;

/* compiled from: OfflineLocManager.java */
/* loaded from: classes.dex */
public final class cp {
    Context a;
    private co b;

    public cp(Context context) {
        this.a = null;
        this.b = null;
        this.a = context;
        try {
            this.b = new co(this.a);
        } catch (Throwable unused) {
        }
    }

    public static void a(String str, ScanResult[] scanResultArr) {
        try {
            OfflineManager.getInstance().trainingFps(b(str, scanResultArr));
        } catch (Throwable th) {
            cm.a(th, "OfflineLocManager", "trainingFps");
        }
    }

    public static void a(String str, ScanResult[] scanResultArr, double d, double d2) {
        try {
            FPS b = b(str, scanResultArr);
            AmapLoc amapLoc = new AmapLoc();
            amapLoc.setLat(d);
            amapLoc.setLon(d2);
            OfflineManager.getInstance().correctLocation(b, amapLoc);
        } catch (Throwable th) {
            cm.a(th, "OfflineLocManager", "correctLocation");
        }
    }

    private static FPS b(String str, ScanResult[] scanResultArr) {
        CellStatus cellStatus = new CellStatus();
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                try {
                    JSONObject optJSONObject = jSONObject.optJSONObject("mainCgi");
                    if (optJSONObject != null) {
                        CellState cellState = new CellState(optJSONObject.optInt("type", 0), optJSONObject.optBoolean("registered", false));
                        cellState.mcc = optJSONObject.optInt("mcc");
                        cellState.mnc = optJSONObject.optInt("mnc");
                        cellState.lac = optJSONObject.optInt("lac");
                        cellState.cid = optJSONObject.optInt("cid");
                        cellState.sid = optJSONObject.optInt("sid");
                        cellState.nid = optJSONObject.optInt("nid");
                        cellState.bid = optJSONObject.optInt("bid");
                        cellState.signalStrength = optJSONObject.optInt("sig");
                        cellStatus.mainCell = cellState;
                    }
                } catch (Throwable th) {
                    cm.a(th, "OfflineLocManager", "buildFPS_1_1");
                }
                try {
                    JSONObject optJSONObject2 = jSONObject.optJSONObject("newCgi");
                    if (optJSONObject2 != null) {
                        CellState cellState2 = new CellState(optJSONObject2.optInt("type", 0), optJSONObject2.optBoolean("registered", false));
                        cellState2.mcc = optJSONObject2.optInt("mcc");
                        cellState2.mnc = optJSONObject2.optInt("mnc");
                        cellState2.lac = optJSONObject2.optInt("lac");
                        cellState2.cid = optJSONObject2.optInt("cid");
                        cellState2.sid = optJSONObject2.optInt("sid");
                        cellState2.nid = optJSONObject2.optInt("nid");
                        cellState2.bid = optJSONObject2.optInt("bid");
                        cellState2.signalStrength = optJSONObject2.optInt("sig");
                        cellStatus.mainCell2 = cellState2;
                    }
                } catch (Throwable th2) {
                    cm.a(th2, "OfflineLocManager", "buildFPS_1_2");
                }
            } catch (Throwable th3) {
                cm.a(th3, "OfflineLocManager", "buildFPS_1");
            }
        }
        WifiStatus wifiStatus = new WifiStatus();
        if (scanResultArr != null) {
            try {
                ArrayList arrayList = new ArrayList();
                for (ScanResult scanResult : scanResultArr) {
                    arrayList.add(scanResult);
                }
                wifiStatus.setWifiList(wifiStatus.scanResultList2WifiList(arrayList));
            } catch (Throwable th4) {
                cm.a(th4, "OfflineLocManager", "buildFPS_2");
            }
        }
        FPS fps = new FPS();
        fps.cellStatus = cellStatus;
        fps.wifiStatus = wifiStatus;
        return fps;
    }

    private OfflineConfig b(ci ciVar, IHttpClient iHttpClient) {
        String g;
        OfflineConfig offlineConfig = new OfflineConfig();
        offlineConfig.productId = (byte) 4;
        try {
            if (ciVar != null) {
                offlineConfig.packageName = ciVar.e();
                offlineConfig.mapKey = ciVar.d();
                offlineConfig.productVersion = ciVar.b();
                offlineConfig.license = ciVar.c();
                offlineConfig.adiu = ciVar.f();
                g = ciVar.g();
            } else {
                offlineConfig.mapKey = u.f(this.a);
                offlineConfig.packageName = u.c(this.a);
                offlineConfig.productVersion = "";
                offlineConfig.license = "S128DF1572465B890OE3F7A13167KLEI";
                offlineConfig.adiu = x.a(this.a);
                g = ciVar.g();
            }
            offlineConfig.uuid = g;
        } catch (Throwable th) {
            cm.a(th, "OfflineLocManager", "generateOfflineConfig");
        }
        UploadConfig uploadConfig = new UploadConfig();
        uploadConfig.bufferSize = 100L;
        uploadConfig.maxDbSize = 100000L;
        uploadConfig.expireTimeInDb = 864000000L;
        uploadConfig.storePeriod = DateUtils.MILLIS_PER_MINUTE;
        uploadConfig.uploadPeriod = DateUtils.MILLIS_PER_MINUTE;
        uploadConfig.sizePerRequest = 1000L;
        uploadConfig.maxSizePerDay = 500000L;
        uploadConfig.nonWifiEnable = false;
        offlineConfig.uploadConfig = uploadConfig;
        if (this.b == null) {
            try {
                this.b = new co(this.a);
            } catch (Throwable unused) {
            }
        }
        offlineConfig.coordinateConverter = this.b;
        offlineConfig.httpClient = iHttpClient;
        return offlineConfig;
    }

    public final String a(String str, ScanResult[] scanResultArr, boolean z) {
        try {
            AmapLoc location = OfflineManager.getInstance().getLocation(b(str, scanResultArr), z);
            if (location == null) {
                return null;
            }
            if (this.b != null) {
                double[] wgsToGcj = this.b.wgsToGcj(new double[]{location.getLat(), location.getLon()});
                location.setLat(wgsToGcj[0]);
                location.setLon(wgsToGcj[1]);
            }
            return location.toJSONStr(1);
        } catch (Throwable th) {
            cm.a(th, "OfflineLocManager", "getOfflineLocation");
            return null;
        }
    }

    public final void a() {
        try {
            OfflineManager.getInstance().destroy();
            this.b = null;
        } catch (Throwable th) {
            cm.a(th, "OfflineLocManager", "destroy");
        }
    }

    public final void a(ci ciVar, IHttpClient iHttpClient) {
        try {
            cn cnVar = new cn();
            OfflineManager.getInstance().init(this.a, b(ciVar, iHttpClient), cnVar);
        } catch (Throwable th) {
            cm.a(th, "OfflineLocManager", "init");
        }
    }
}
