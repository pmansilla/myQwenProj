package com.loc;

import android.content.Context;
import android.net.wifi.ScanResult;
import com.amap.location.common.model.AmapLoc;
import com.amap.opensdk.co.CoManager;
import com.autonavi.aps.amapapi.model.AMapLocationServer;
import java.util.List;
import org.json.JSONObject;

/* compiled from: CoManager.java */
/* loaded from: classes.dex */
public final class ef {
    private Context c;
    private CoManager d = null;
    boolean a = false;
    boolean b = false;
    private int e = -1;

    public ef(Context context) {
        this.c = context;
    }

    private static String a(Context context) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("sv", "4.7.1");
            jSONObject.put("als", "S128DF1572465B890OE3F7A13167KLEI");
            jSONObject.put("pn", u.c(context));
            jSONObject.put("ak", u.f(context));
            jSONObject.put("ud", x.g(context));
            jSONObject.put("au", x.a(context));
            jSONObject.put("isimei", true);
            return jSONObject.toString();
        } catch (Throwable unused) {
            return null;
        }
    }

    private static String a(ee eeVar) {
        try {
            JSONObject jSONObject = new JSONObject();
            if (eeVar == null) {
                return null;
            }
            ed c = eeVar.c();
            ed d = eeVar.d();
            if (c != null) {
                jSONObject.put("mainCgi", c.a());
            }
            if (d != null) {
                jSONObject.put("mainCgi2", d.a());
            }
            return jSONObject.toString();
        } catch (Throwable th) {
            es.a(th, "APSCoManager", "buildCgiJsonStr");
            return null;
        }
    }

    private void a(ee eeVar, List<ScanResult> list, AMapLocationServer aMapLocationServer, int i) {
        try {
            if (d() && fa.a(aMapLocationServer)) {
                e();
                if (this.d != null) {
                    String a = a(eeVar);
                    ScanResult[] a2 = a(list);
                    if (i == 1) {
                        this.d.trainingFps(a, a2);
                    } else if (i != 2) {
                        return;
                    } else {
                        this.d.correctOfflineLocation(a, a2, aMapLocationServer.getLatitude(), aMapLocationServer.getLongitude());
                    }
                    this.b = true;
                }
            }
        } catch (Throwable th) {
            StringBuilder sb = new StringBuilder("action-");
            sb.append(1 == i ? "training" : "correct");
            es.a(th, "APSCoManager", sb.toString());
        }
    }

    private static ScanResult[] a(List<ScanResult> list) {
        if (list == null) {
            return null;
        }
        try {
            if (list.size() <= 0) {
                return null;
            }
            ScanResult[] scanResultArr = new ScanResult[list.size()];
            for (int i = 0; i < list.size(); i++) {
                scanResultArr[i] = list.get(i);
            }
            return scanResultArr;
        } catch (Throwable th) {
            es.a(th, "APSCoManager", "buildScanResults");
            return null;
        }
    }

    private boolean d() {
        if (!er.w()) {
            c();
            return false;
        }
        if (er.x()) {
            return true;
        }
        if (this.b) {
            try {
                if (this.d != null) {
                    this.d.destroyOfflineLoc();
                }
            } catch (Throwable th) {
                es.a(th, "APSCoManager", "destroyOffline");
            }
            this.b = false;
        }
        return false;
    }

    private void e() {
        new Object[1][0] = "CoManager ==> init ";
        fa.a();
        try {
            if (this.d == null) {
                int b = ez.b(this.c, "pref", "ok5", 0);
                long b2 = ez.b(this.c, "pref", "ok7", 0L);
                if (b != 0 && b2 != 0 && System.currentTimeMillis() - b2 < 259200000) {
                    return;
                }
                ez.a(this.c, "pref", "ok5", b + 1);
                ez.a(this.c, "pref", "ok7", System.currentTimeMillis());
                new Object[1][0] = "CoManager ==> initForJar ";
                fa.a();
                try {
                    this.d = new CoManager(this.c);
                    try {
                        if (this.c != null) {
                            String a = a(this.c);
                            if (this.d != null) {
                                this.d.init(a);
                            }
                        }
                    } catch (Throwable th) {
                        es.a(th, "APSCoManager", "setConfig");
                    }
                    this.d.loadLocalSo();
                } catch (Throwable th2) {
                    es.a(th2, "APSCoManager", "initForJar");
                }
                ez.a(this.c, "pref", "ok5", 0);
                ez.a(this.c, "pref", "ok7", 0L);
            }
            try {
                int z = er.z();
                if (this.e == z) {
                    return;
                }
                this.e = z;
                if (this.d != null) {
                    this.d.setCloudConfigVersion(z);
                }
            } catch (Throwable th3) {
                es.a(th3, "APSCoManager", "setCloudVersion");
            }
        } catch (Throwable th4) {
            es.a(th4, "APSCoManager", "init");
        }
    }

    public final AMapLocationServer a(ee eeVar, List<ScanResult> list, AMapLocationServer aMapLocationServer) {
        String e;
        try {
        } catch (Throwable th) {
            es.a(th, "APSCoManager", "getOffLoc");
        }
        if (!d()) {
            return aMapLocationServer;
        }
        if (aMapLocationServer != null && aMapLocationServer.getErrorCode() == 7) {
            return aMapLocationServer;
        }
        e();
        if (this.d != null) {
            this.b = true;
            String offlineLoc = this.d.getOfflineLoc(a(eeVar), a(list), false);
            if (offlineLoc != null) {
                JSONObject jSONObject = new JSONObject(offlineLoc);
                AMapLocationServer aMapLocationServer2 = new AMapLocationServer("lbs");
                aMapLocationServer2.b(jSONObject);
                if (fa.a(aMapLocationServer2)) {
                    StringBuffer stringBuffer = new StringBuffer();
                    if (aMapLocationServer2.e().equals(AmapLoc.TYPE_OFFLINE_CELL)) {
                        e = "基站离线定位";
                    } else if (aMapLocationServer2.e().equals(AmapLoc.TYPE_OFFLINE_WIFI)) {
                        e = "WIFI离线定位";
                    } else {
                        stringBuffer.append("离线定位，");
                        e = aMapLocationServer2.e();
                    }
                    stringBuffer.append(e);
                    if (aMapLocationServer != null) {
                        stringBuffer.append("，在线定位失败原因:" + aMapLocationServer.getErrorInfo());
                    }
                    aMapLocationServer2.setTrustedLevel(2);
                    aMapLocationServer2.setLocationDetail(stringBuffer.toString());
                    aMapLocationServer2.setLocationType(8);
                }
                return aMapLocationServer2;
            }
        }
        return aMapLocationServer;
    }

    public final void a() {
        try {
            if (!er.w()) {
                c();
                return;
            }
            if (er.y()) {
                if (this.a) {
                    return;
                }
                e();
                if (this.d != null) {
                    this.d.startCollect();
                    this.a = true;
                    return;
                }
                return;
            }
            if (this.a) {
                try {
                    if (this.d != null) {
                        this.d.destroyCollect();
                    }
                } catch (Throwable th) {
                    es.a(th, "APSCoManager", "destroyCollection");
                }
                this.a = false;
            }
        } catch (Throwable th2) {
            es.a(th2, "APSCoManager", "startCollection");
        }
    }

    public final String b() {
        try {
            if (!er.w()) {
                c();
                return null;
            }
            if (this.d != null) {
                return this.d.getCollectVersion();
            }
            return null;
        } catch (Throwable th) {
            es.a(th, "APSCoManager", "getCollectionVersion");
            return null;
        }
    }

    public final void b(ee eeVar, List<ScanResult> list, AMapLocationServer aMapLocationServer) {
        try {
            a(eeVar, list, aMapLocationServer, 1);
        } catch (Throwable th) {
            es.a(th, "APSCoManager", "trainingFps");
        }
    }

    public final void c() {
        try {
            if (this.d != null) {
                this.d.destroy();
            }
            this.a = false;
            this.b = false;
            this.d = null;
        } catch (Throwable th) {
            es.a(th, "APSCoManager", "destroy");
        }
    }

    public final void c(ee eeVar, List<ScanResult> list, AMapLocationServer aMapLocationServer) {
        try {
            a(eeVar, list, aMapLocationServer, 2);
        } catch (Throwable th) {
            es.a(th, "APSCoManager", "correctOffLoc");
        }
    }
}
