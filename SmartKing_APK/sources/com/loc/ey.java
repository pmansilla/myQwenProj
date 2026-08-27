package com.loc;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.location.common.model.AmapLoc;
import com.autonavi.amap.mapcore.tools.GLMapStaticValue;
import com.autonavi.aps.amapapi.model.AMapLocationServer;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* compiled from: ReportUtil.java */
/* loaded from: classes.dex */
public final class ey {
    public SparseArray<Long> a = new SparseArray<>();
    public int b = -1;
    public long c = 0;
    String[] d = {"ol", "cl", "gl", "ha", "bs", "ds"};
    public int e = -1;
    public long f = -1;
    private static List<bq> i = new ArrayList();
    private static JSONArray j = null;
    static AMapLocation g = null;
    static boolean h = false;

    public static void a(long j2, long j3) {
        try {
            if (h) {
                return;
            }
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("gpsTime:");
            stringBuffer.append(fa.a(j2, "yyyy-MM-dd HH:mm:ss.SSS"));
            stringBuffer.append(",");
            stringBuffer.append("sysTime:");
            stringBuffer.append(fa.a(j3, "yyyy-MM-dd HH:mm:ss.SSS"));
            stringBuffer.append(",");
            long E = er.E();
            String str = AmapLoc.RESULT_TYPE_GPS;
            if (0 != E) {
                str = fa.a(E, "yyyy-MM-dd HH:mm:ss.SSS");
            }
            stringBuffer.append("serverTime:");
            stringBuffer.append(str);
            a("checkgpstime", stringBuffer.toString());
            if (0 != E && Math.abs(j2 - E) < 31536000000L) {
                stringBuffer.append(", correctError");
                a("checkgpstimeerror", stringBuffer.toString());
            }
            stringBuffer.delete(0, stringBuffer.length());
            h = true;
        } catch (Throwable unused) {
        }
    }

    public static void a(Context context) {
        if (context != null) {
            try {
                if (er.i()) {
                    if (i != null && i.size() > 0) {
                        ArrayList arrayList = new ArrayList();
                        arrayList.addAll(i);
                        br.a(arrayList, context);
                        i.clear();
                    }
                    f(context);
                }
            } catch (Throwable th) {
                es.a(th, "ReportUtil", "destroy");
            }
        }
    }

    public static void a(Context context, int i2, int i3, long j2, long j3) {
        if (i2 == -1 || i3 == -1 || context == null) {
            return;
        }
        try {
            if (er.i()) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("param_int_first", i2);
                jSONObject.put("param_int_second", i3);
                jSONObject.put("param_long_first", j2);
                jSONObject.put("param_long_second", j3);
                a(context, "O012", jSONObject);
            }
        } catch (Throwable th) {
            try {
                es.a(th, "ReportUtil", "applyStatisticsEx");
            } catch (Throwable th2) {
                es.a(th2, "ReportUtil", "reportServiceAliveTime");
            }
        }
    }

    public static void a(Context context, long j2, boolean z) {
        if (context != null) {
            try {
                if (er.i()) {
                    a(context, "O015", z ? "domestic" : "abroad", null, Long.valueOf(j2).intValue(), Integer.MAX_VALUE);
                }
            } catch (Throwable th) {
                es.a(th, "ReportUtil", "reportGPSLocUseTime");
            }
        }
    }

    public static void a(Context context, AMapLocation aMapLocation) {
        String str;
        String str2;
        int i2;
        String str3;
        if (aMapLocation == null) {
            return;
        }
        try {
            if (!"gps".equalsIgnoreCase(aMapLocation.getProvider()) && aMapLocation.getLocationType() != 1) {
                String str4 = a(aMapLocation) ? "abroad" : "domestic";
                if (aMapLocation.getErrorCode() != 0) {
                    int errorCode = aMapLocation.getErrorCode();
                    if (errorCode != 11) {
                        switch (errorCode) {
                            case 4:
                            case 5:
                            case 6:
                                break;
                            default:
                                str3 = "cache";
                                break;
                        }
                        str2 = str3;
                        i2 = 0;
                    }
                    str3 = "net";
                    str2 = str3;
                    i2 = 0;
                } else {
                    switch (aMapLocation.getLocationType()) {
                        case 5:
                        case 6:
                            str = "net";
                            break;
                        default:
                            str = "cache";
                            break;
                    }
                    str2 = str;
                    i2 = 1;
                }
                a(context, "O016", str2, str4, i2, Integer.MAX_VALUE);
            }
        } catch (Throwable th) {
            es.a(th, "ReportUtil", "reportBatting");
        }
    }

    public static void a(Context context, ex exVar) {
        String str;
        if (context != null) {
            try {
                if (er.i()) {
                    AMapLocationServer c = exVar.c();
                    if (fa.a(c) && !"gps".equalsIgnoreCase(c.getProvider()) && c.getLocationType() != 1) {
                        int intValue = Long.valueOf(exVar.b() - exVar.a()).intValue();
                        boolean z = false;
                        int intValue2 = Long.valueOf(c.k()).intValue();
                        switch (c.getLocationType()) {
                            case 5:
                            case 6:
                                str = "net";
                                break;
                            default:
                                str = "cache";
                                z = true;
                                break;
                        }
                        String str2 = a(c) ? "abroad" : "domestic";
                        if (!z) {
                            a(context, "O014", str2, null, intValue2, intValue);
                        }
                        a(context, "O013", str, str2, intValue, Integer.MAX_VALUE);
                    }
                }
            } catch (Throwable th) {
                es.a(th, "ReportUtil", "reportLBSLocUseTime");
            }
        }
    }

    public static void a(Context context, String str, int i2) {
        if (context != null) {
            try {
                if (er.i()) {
                    JSONObject jSONObject = new JSONObject();
                    if (!TextUtils.isEmpty(str)) {
                        jSONObject.put("param_string_first", str);
                    }
                    if (i2 != Integer.MAX_VALUE) {
                        jSONObject.put("param_int_first", i2);
                    }
                    a(context, "O010", jSONObject);
                }
            } catch (Throwable th) {
                try {
                    es.a(th, "ReportUtil", "applyStatisticsEx");
                } catch (Throwable th2) {
                    es.a(th2, "ReportUtil", "reportDexFunction");
                }
            }
        }
    }

    private static void a(Context context, String str, String str2, String str3, int i2, int i3) {
        if (context != null) {
            try {
                if (er.i()) {
                    JSONObject jSONObject = new JSONObject();
                    if (!TextUtils.isEmpty(str2)) {
                        jSONObject.put("param_string_first", str2);
                    }
                    if (!TextUtils.isEmpty(str3)) {
                        jSONObject.put("param_string_second", str3);
                    }
                    if (i2 != Integer.MAX_VALUE) {
                        jSONObject.put("param_int_first", i2);
                    }
                    if (i3 != Integer.MAX_VALUE) {
                        jSONObject.put("param_int_second", i3);
                    }
                    a(context, str, jSONObject);
                }
            } catch (Throwable th) {
                es.a(th, "ReportUtil", "applyStatisticsEx");
            }
        }
    }

    private static void a(Context context, String str, JSONObject jSONObject) {
        if (context != null) {
            try {
                if (er.i()) {
                    bq bqVar = new bq(context, "loc", "4.7.1", str);
                    bqVar.a(jSONObject.toString());
                    i.add(bqVar);
                    if (i.size() >= 100) {
                        ArrayList arrayList = new ArrayList();
                        arrayList.addAll(i);
                        br.a(arrayList, context);
                        i.clear();
                    }
                }
            } catch (Throwable th) {
                es.a(th, "ReportUtil", "applyStatistics");
            }
        }
    }

    public static void a(AMapLocation aMapLocation, AMapLocation aMapLocation2) {
        try {
            if (g == null) {
                if (!fa.a(aMapLocation)) {
                    g = aMapLocation2;
                    return;
                }
                g = aMapLocation.m9clone();
            }
            if (fa.a(g) && fa.a(aMapLocation2)) {
                AMapLocation m9clone = aMapLocation2.m9clone();
                if (g.getLocationType() != 1 && g.getLocationType() != 9 && !"gps".equalsIgnoreCase(g.getProvider()) && g.getLocationType() != 7 && m9clone.getLocationType() != 1 && m9clone.getLocationType() != 9 && !"gps".equalsIgnoreCase(m9clone.getProvider()) && m9clone.getLocationType() != 7) {
                    long abs = Math.abs(m9clone.getTime() - g.getTime()) / 1000;
                    if (abs <= 0) {
                        abs = 1;
                    }
                    if (abs <= 1800) {
                        float a = fa.a(g, m9clone);
                        float f = a / ((float) abs);
                        if (a > 30000.0f && f > 1000.0f) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(g.getLatitude());
                            sb.append(",");
                            sb.append(g.getLongitude());
                            sb.append(",");
                            sb.append(g.getAccuracy());
                            sb.append(",");
                            sb.append(g.getLocationType());
                            sb.append(",");
                            if (aMapLocation.getTime() != 0) {
                                sb.append(fa.a(g.getTime(), "yyyyMMdd_HH:mm:ss:SS"));
                            } else {
                                sb.append(g.getTime());
                            }
                            sb.append("#");
                            sb.append(m9clone.getLatitude());
                            sb.append(",");
                            sb.append(m9clone.getLongitude());
                            sb.append(",");
                            sb.append(m9clone.getAccuracy());
                            sb.append(",");
                            sb.append(m9clone.getLocationType());
                            sb.append(",");
                            if (m9clone.getTime() != 0) {
                                sb.append(fa.a(m9clone.getTime(), "yyyyMMdd_HH:mm:ss:SS"));
                            } else {
                                sb.append(m9clone.getTime());
                            }
                            a("bigshiftstatistics", sb.toString());
                            sb.delete(0, sb.length());
                        }
                    }
                }
                g = m9clone;
            }
        } catch (Throwable unused) {
        }
    }

    public static void a(String str, int i2) {
        String valueOf = String.valueOf(i2);
        String str2 = "";
        switch (i2) {
            case GLMapStaticValue.MAP_PARAMETERNAME_SATELLITE /* 2011 */:
                str2 = "ContextIsNull";
                break;
            case 2021:
                str2 = "OnlyMainWifi";
                break;
            case GLMapStaticValue.MAP_PARAMETERNAME_CLEAR_INDOORBUILDING_LAST /* 2022 */:
                str2 = "OnlyOneWifiButNotMain";
                break;
            case 2031:
                str2 = "CreateApsReqException";
                break;
            case 2041:
                str2 = "ResponseResultIsNull";
                break;
            case 2051:
                str2 = "NeedLoginNetWork\t";
                break;
            case 2052:
                str2 = "MaybeIntercepted";
                break;
            case 2053:
                str2 = "DecryptResponseException";
                break;
            case 2054:
                str2 = "ParserDataException";
                break;
            case 2061:
                str2 = "ServerRetypeError";
                break;
            case 2062:
                str2 = "ServerLocFail";
                break;
            case 2081:
                str2 = "LocalLocException";
                break;
            case 2091:
                str2 = "InitException";
                break;
            case 2101:
                str2 = "BindAPSServiceException";
                break;
            case 2102:
                str2 = "AuthClientScodeFail";
                break;
            case 2103:
                str2 = "NotConfigAPSService";
                break;
            case 2111:
                str2 = "ErrorCgiInfo";
                break;
            case 2121:
                str2 = "NotLocPermission";
                break;
            case 2131:
                str2 = "NoCgiOAndWifiInfo";
                break;
            case 2132:
                str2 = "AirPlaneModeAndWifiOff";
                break;
            case 2133:
                str2 = "NoCgiAndWifiOff";
                break;
            case 2141:
                str2 = "NoEnoughStatellites";
                break;
            case 2151:
                str2 = "MaybeMockNetLoc";
                break;
            case 2152:
                str2 = "MaybeMockGPSLoc";
                break;
        }
        a(str, valueOf, str2);
    }

    public static void a(String str, String str2) {
        try {
            aq.b(es.b(), str2, str);
        } catch (Throwable th) {
            es.a(th, "ReportUtil", "reportLog");
        }
    }

    public static void a(String str, String str2, String str3) {
        try {
            aq.a(es.b(), "/mobile/binary", str3, str, str2);
        } catch (Throwable unused) {
        }
    }

    public static void a(String str, Throwable th) {
        try {
            if (th instanceof t) {
                aq.a(es.b(), str, (t) th);
            }
        } catch (Throwable unused) {
        }
    }

    private static boolean a(AMapLocation aMapLocation) {
        return fa.a(aMapLocation) ? !es.a(aMapLocation.getLatitude(), aMapLocation.getLongitude()) : "http://abroad.apilocate.amap.com/mobile/binary".equals(es.a);
    }

    public static void b(Context context, AMapLocation aMapLocation) {
        int i2;
        try {
            if (fa.a(aMapLocation)) {
                boolean z = false;
                switch (aMapLocation.getLocationType()) {
                    case 1:
                        i2 = 0;
                        z = true;
                        break;
                    case 2:
                    case 4:
                        i2 = 1;
                        z = true;
                        break;
                    case 3:
                    case 5:
                    case 6:
                    case 7:
                    default:
                        i2 = 0;
                        break;
                    case 8:
                        i2 = 3;
                        z = true;
                        break;
                    case 9:
                        i2 = 2;
                        z = true;
                        break;
                }
                if (z) {
                    if (j == null) {
                        j = new JSONArray();
                    }
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("lon", fa.c(aMapLocation.getLongitude()));
                    jSONObject.put("lat", fa.c(aMapLocation.getLatitude()));
                    jSONObject.put("type", i2);
                    jSONObject.put("timestamp", fa.b());
                    if (aMapLocation.getCoordType().equalsIgnoreCase(AMapLocation.COORD_TYPE_WGS84)) {
                        jSONObject.put("coordType", 1);
                    } else {
                        jSONObject.put("coordType", 2);
                    }
                    if (i2 == 0) {
                        JSONObject jSONObject2 = new JSONObject();
                        jSONObject2.put("accuracy", fa.b(aMapLocation.getAccuracy()));
                        jSONObject2.put("altitude", fa.b(aMapLocation.getAltitude()));
                        jSONObject2.put("bearing", fa.b(aMapLocation.getBearing()));
                        jSONObject2.put("speed", fa.b(aMapLocation.getSpeed()));
                        jSONObject.put("extension", jSONObject2);
                    }
                    JSONArray put = j.put(jSONObject);
                    j = put;
                    if (put.length() >= er.j()) {
                        f(context);
                    }
                }
            }
        } catch (Throwable th) {
            es.a(th, "ReportUtil", "recordOfflineLocLog");
        }
    }

    private static void f(Context context) {
        try {
            if (j == null || j.length() <= 0) {
                return;
            }
            bp.a(new bo(context, es.b(), j.toString()), context);
            j = null;
        } catch (Throwable th) {
            es.a(th, "ReportUtil", "writeOfflineLocLog");
        }
    }

    public final void a(Context context, int i2) {
        try {
            if (this.b == i2) {
                return;
            }
            if (this.b != -1 && this.b != i2) {
                this.a.append(this.b, Long.valueOf((fa.c() - this.c) + this.a.get(this.b, 0L).longValue()));
            }
            this.c = fa.c() - ez.b(context, "pref", this.d[i2], 0L);
            this.b = i2;
        } catch (Throwable th) {
            es.a(th, "ReportUtil", "setLocationType");
        }
    }

    public final void a(Context context, AMapLocationClientOption aMapLocationClientOption) {
        int i2;
        try {
            switch (aMapLocationClientOption.getLocationMode()) {
                case Battery_Saving:
                    i2 = 4;
                    break;
                case Device_Sensors:
                    i2 = 5;
                    break;
                case Hight_Accuracy:
                    i2 = 3;
                    break;
                default:
                    i2 = -1;
                    break;
            }
            if (this.e == i2) {
                return;
            }
            if (this.e != -1 && this.e != i2) {
                this.a.append(this.e, Long.valueOf((fa.c() - this.f) + this.a.get(this.e, 0L).longValue()));
            }
            this.f = fa.c() - ez.b(context, "pref", this.d[i2], 0L);
            this.e = i2;
        } catch (Throwable th) {
            es.a(th, "ReportUtil", "setLocationMode");
        }
    }

    public final void b(Context context) {
        try {
            long c = fa.c() - this.c;
            if (this.b != -1) {
                this.a.append(this.b, Long.valueOf(c + this.a.get(this.b, 0L).longValue()));
            }
            long c2 = fa.c() - this.f;
            if (this.e != -1) {
                this.a.append(this.e, Long.valueOf(c2 + this.a.get(this.e, 0L).longValue()));
            }
            for (int i2 = 0; i2 < this.d.length; i2++) {
                long longValue = this.a.get(i2, 0L).longValue();
                if (longValue > 0 && longValue > ez.b(context, "pref", this.d[i2], 0L)) {
                    ez.a(context, "pref", this.d[i2], longValue);
                }
            }
        } catch (Throwable th) {
            es.a(th, "ReportUtil", "saveLocationTypeAndMode");
        }
    }

    public final int c(Context context) {
        try {
            long b = ez.b(context, "pref", this.d[2], 0L);
            long b2 = ez.b(context, "pref", this.d[0], 0L);
            long b3 = ez.b(context, "pref", this.d[1], 0L);
            if (b == 0 && b2 == 0 && b3 == 0) {
                return -1;
            }
            long j2 = b2 - b;
            long j3 = b3 - b;
            return b > j2 ? b > j3 ? 2 : 1 : j2 > j3 ? 0 : 1;
        } catch (Throwable unused) {
            return -1;
        }
    }

    public final int d(Context context) {
        try {
            long b = ez.b(context, "pref", this.d[3], 0L);
            long b2 = ez.b(context, "pref", this.d[4], 0L);
            long b3 = ez.b(context, "pref", this.d[5], 0L);
            if (b == 0 && b2 == 0 && b3 == 0) {
                return -1;
            }
            return b > b2 ? b > b3 ? 3 : 5 : b2 > b3 ? 4 : 5;
        } catch (Throwable unused) {
            return -1;
        }
    }

    public final void e(Context context) {
        for (int i2 = 0; i2 < this.d.length; i2++) {
            try {
                ez.a(context, "pref", this.d[i2], 0L);
            } catch (Throwable unused) {
                return;
            }
        }
    }
}
