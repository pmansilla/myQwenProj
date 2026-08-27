package com.amap.api.mapcore.util;

import com.amap.api.mapcore.util.ho;
import com.amap.location.common.model.AmapLoc;
import com.autonavi.amap.mapcore.Inner_3dMap_location;

/* compiled from: Util.java */
/* loaded from: classes.dex */
public final class km {
    private static final String[] b = {"com.amap.api.maps", "com.amap.api.mapcore", "com.autonavi.amap.mapcore", "com.amap.api.3dmap.admic", "com.amap.api.trace", "com.amap.api.trace.core"};
    private static final String[] c = {"com.amap.api.mapcore2d", "com.amap.api.maps2d"};
    private static final String[] d = {"com.amap.trace"};
    static ho a = null;

    public static ho a() throws hc {
        Class<?> cls;
        Class<?> cls2;
        if (a != null) {
            return a;
        }
        try {
            cls = Class.forName("com.amap.api.maps.MapsInitializer");
        } catch (Throwable unused) {
            cls = null;
        }
        try {
            if (cls != null) {
                String str = (String) ky.a(cls, "getVersion", (Object[]) null, (Class<?>[]) null);
                a = new ho.a("3dmap", str, "AMAP_SDK_Android_Map_" + str).a(b).a();
            } else {
                Class<?> cls3 = Class.forName("com.amap.api.maps2d.MapsInitializer");
                try {
                    String str2 = (String) ky.a(cls3, "getVersion", (Object[]) null, (Class<?>[]) null);
                    a = new ho.a("2dmap", str2, "AMAP_SDK_Android_2DMap_" + str2).a(c).a();
                } catch (Throwable unused2) {
                }
                cls = cls3;
            }
        } catch (Throwable unused3) {
        }
        if (cls == null) {
            try {
                cls2 = Class.forName("com.amap.trace.AMapTraceClient");
            } catch (Throwable unused4) {
                cls2 = null;
            }
            if (cls2 != null) {
                try {
                    String str3 = (String) ky.a(cls2, "getVersion", (Object[]) null, (Class<?>[]) null);
                    a = new ho.a("trace", str3, "AMAP_TRACE_Android_" + str3).a(d).a();
                } catch (Throwable unused5) {
                }
            }
        }
        return a;
    }

    public static boolean a(ki kiVar) {
        if (kiVar == null || kiVar.d().equals(AmapLoc.RESULT_TYPE_FAIL) || kiVar.d().equals(AmapLoc.RESULT_TYPE_SELF_LAT_LON) || kiVar.d().equals(AmapLoc.RESULT_TYPE_NO_LONGER_USED)) {
            return false;
        }
        return a((Inner_3dMap_location) kiVar);
    }

    public static boolean a(Inner_3dMap_location inner_3dMap_location) {
        double longitude = inner_3dMap_location.getLongitude();
        double latitude = inner_3dMap_location.getLatitude();
        return !(longitude == 0.0d && latitude == 0.0d) && longitude <= 180.0d && latitude <= 90.0d && longitude >= -180.0d && latitude >= -90.0d;
    }
}
