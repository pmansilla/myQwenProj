package com.amap.api.mapcore.util;

import com.amap.api.maps.model.LatLng;

/* compiled from: SegmentsIntersect.java */
/* loaded from: classes.dex */
public class fm {
    private static double a(LatLng latLng, LatLng latLng2) {
        return (latLng.longitude * latLng2.latitude) - (latLng2.longitude * latLng.latitude);
    }

    private static double a(LatLng latLng, LatLng latLng2, LatLng latLng3) {
        return a(b(latLng3, latLng), b(latLng2, latLng));
    }

    public static boolean a(LatLng latLng, LatLng latLng2, LatLng latLng3, LatLng latLng4) {
        double a = a(latLng3, latLng4, latLng);
        double a2 = a(latLng3, latLng4, latLng2);
        double a3 = a(latLng, latLng2, latLng3);
        double a4 = a(latLng, latLng2, latLng4);
        if (((a > 0.0d && a2 < 0.0d) || (a < 0.0d && a2 > 0.0d)) && ((a3 > 0.0d && a4 < 0.0d) || (a3 < 0.0d && a4 > 0.0d))) {
            return true;
        }
        if (a == 0.0d && b(latLng3, latLng4, latLng)) {
            return true;
        }
        if (a2 == 0.0d && b(latLng3, latLng4, latLng2)) {
            return true;
        }
        if (a3 == 0.0d && b(latLng, latLng2, latLng3)) {
            return true;
        }
        return a4 == 0.0d && b(latLng, latLng2, latLng4);
    }

    private static LatLng b(LatLng latLng, LatLng latLng2) {
        return new LatLng(latLng2.latitude - latLng.latitude, latLng2.longitude - latLng.longitude);
    }

    private static boolean b(LatLng latLng, LatLng latLng2, LatLng latLng3) {
        double d = latLng.longitude - latLng2.longitude > 0.0d ? latLng.longitude : latLng2.longitude;
        return (((latLng.longitude - latLng2.longitude) > 0.0d ? 1 : ((latLng.longitude - latLng2.longitude) == 0.0d ? 0 : -1)) < 0 ? latLng.longitude : latLng2.longitude) <= latLng3.longitude && latLng3.longitude <= d && (((latLng.latitude - latLng2.latitude) > 0.0d ? 1 : ((latLng.latitude - latLng2.latitude) == 0.0d ? 0 : -1)) < 0 ? latLng.latitude : latLng2.latitude) <= latLng3.latitude && latLng3.latitude <= (((latLng.latitude - latLng2.latitude) > 0.0d ? 1 : ((latLng.latitude - latLng2.latitude) == 0.0d ? 0 : -1)) > 0 ? latLng.latitude : latLng2.latitude);
    }
}
