package com.amap.api.mapcore.util;

import android.content.Context;
import android.text.TextUtils;
import com.amap.api.maps.model.LatLng;
import com.autonavi.amap.mapcore.CoordUtil;
import com.autonavi.amap.mapcore.DPoint;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* compiled from: OffsetUtil.java */
/* loaded from: classes.dex */
public class ax {
    static double a = 3.141592653589793d;
    private static boolean d = false;
    private static final double[] e = {25.575374d, 120.391111d};
    private static final double[] f = {21.405235d, 121.649046d};
    private static final List<LatLng> g = new ArrayList(Arrays.asList(new LatLng(23.379947d, 119.757001d), new LatLng(24.983296d, 120.474496d), new LatLng(25.518722d, 121.359866d), new LatLng(25.41329d, 122.443582d), new LatLng(24.862708d, 122.288354d), new LatLng(24.461292d, 122.188319d), new LatLng(21.584761d, 120.968923d), new LatLng(21.830837d, 120.654445d)));
    public static double b = 6378245.0d;
    public static double c = 0.006693421622965943d;

    private static double a(double d2) {
        return Math.sin(d2 * 3000.0d * (a / 180.0d)) * 2.0E-5d;
    }

    public static double a(double d2, double d3) {
        return (Math.cos(d3 / 100000.0d) * (d2 / 18000.0d)) + (Math.sin(d2 / 100000.0d) * (d3 / 9000.0d));
    }

    private static double a(double d2, int i) {
        return new BigDecimal(d2).setScale(i, 4).doubleValue();
    }

    public static LatLng a(Context context, LatLng latLng) {
        if (context == null) {
            return null;
        }
        if (!fk.a(latLng.latitude, latLng.longitude)) {
            return latLng;
        }
        if (!TextUtils.isEmpty("Jni_wgs2gcj") && !d) {
            try {
                System.loadLibrary("Jni_wgs2gcj");
                d = true;
            } catch (Throwable th) {
                ic.c(th, "OffsetUtil", "load WGS2GCJ_SO filed");
            }
        }
        DPoint a2 = a(DPoint.obtain(latLng.longitude, latLng.latitude), d);
        LatLng latLng2 = new LatLng(a2.y, a2.x, false);
        a2.recycle();
        return latLng2;
    }

    public static LatLng a(LatLng latLng) {
        if (latLng != null) {
            try {
                if (fk.a(latLng.latitude, latLng.longitude)) {
                    DPoint a2 = a(latLng.longitude, latLng.latitude, 2);
                    LatLng latLng2 = new LatLng(a2.y, a2.x, false);
                    a2.recycle();
                    return latLng2;
                }
                if (!c(latLng.latitude, latLng.longitude)) {
                    return latLng;
                }
                DPoint a3 = a(latLng.longitude, latLng.latitude, 2);
                return h(a3.y, a3.x);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        return latLng;
    }

    private static DPoint a(double d2, double d3, double d4, double d5) {
        DPoint obtain = DPoint.obtain();
        double d6 = d2 - d4;
        double d7 = d3 - d5;
        DPoint g2 = g(d6, d7);
        obtain.x = a((d2 + d6) - g2.x, 8);
        obtain.y = a((d3 + d7) - g2.y, 8);
        return obtain;
    }

    private static DPoint a(double d2, double d3, int i) {
        DPoint dPoint = null;
        double d4 = 0.006401062d;
        double d5 = 0.0060424805d;
        for (int i2 = 0; i2 < i; i2++) {
            dPoint = a(d2, d3, d4, d5);
            d4 = d2 - dPoint.x;
            d5 = d3 - dPoint.y;
        }
        return dPoint;
    }

    private static DPoint a(DPoint dPoint, boolean z) {
        try {
            if (!fk.a(dPoint.y, dPoint.x)) {
                return dPoint;
            }
            double[] dArr = new double[2];
            if (z) {
                try {
                    if (CoordUtil.convertToGcj(new double[]{dPoint.x, dPoint.y}, dArr) != 0) {
                        dArr = ka.a(dPoint.x, dPoint.y);
                    }
                } catch (Throwable th) {
                    th.printStackTrace();
                    dArr = ka.a(dPoint.x, dPoint.y);
                }
            } else {
                dArr = ka.a(dPoint.x, dPoint.y);
            }
            dPoint.recycle();
            return DPoint.obtain(dArr[0], dArr[1]);
        } catch (Throwable unused) {
            return dPoint;
        }
    }

    private static double b(double d2) {
        return Math.cos(d2 * 3000.0d * (a / 180.0d)) * 3.0E-6d;
    }

    public static double b(double d2, double d3) {
        return (Math.sin(d3 / 100000.0d) * (d2 / 18000.0d)) + (Math.cos(d2 / 100000.0d) * (d3 / 9000.0d));
    }

    public static LatLng b(Context context, LatLng latLng) {
        try {
            if (!fk.a(latLng.latitude, latLng.longitude)) {
                return latLng;
            }
            DPoint f2 = f(latLng.longitude, latLng.latitude);
            LatLng a2 = a(context, new LatLng(f2.y, f2.x, false));
            f2.recycle();
            return a2;
        } catch (Throwable th) {
            th.printStackTrace();
            return latLng;
        }
    }

    public static boolean c(double d2, double d3) {
        return fr.a(new LatLng(d2, d3), g);
    }

    public static double d(double d2, double d3) {
        double d4 = d2 * 2.0d;
        return (-100.0d) + d4 + (d3 * 3.0d) + (d3 * 0.2d * d3) + (0.1d * d2 * d3) + (Math.sqrt(Math.abs(d2)) * 0.2d) + ((((Math.sin((d2 * 6.0d) * a) * 20.0d) + (Math.sin(d4 * a) * 20.0d)) * 2.0d) / 3.0d) + ((((Math.sin(a * d3) * 20.0d) + (Math.sin((d3 / 3.0d) * a) * 40.0d)) * 2.0d) / 3.0d) + ((((Math.sin((d3 / 12.0d) * a) * 160.0d) + (Math.sin((d3 * a) / 30.0d) * 320.0d)) * 2.0d) / 3.0d);
    }

    public static double e(double d2, double d3) {
        double d4 = d2 * 0.1d;
        return d2 + 300.0d + (d3 * 2.0d) + (d4 * d2) + (d4 * d3) + (Math.sqrt(Math.abs(d2)) * 0.1d) + ((((Math.sin((6.0d * d2) * a) * 20.0d) + (Math.sin((d2 * 2.0d) * a) * 20.0d)) * 2.0d) / 3.0d) + ((((Math.sin(a * d2) * 20.0d) + (Math.sin((d2 / 3.0d) * a) * 40.0d)) * 2.0d) / 3.0d) + ((((Math.sin((d2 / 12.0d) * a) * 150.0d) + (Math.sin((d2 / 30.0d) * a) * 300.0d)) * 2.0d) / 3.0d);
    }

    private static DPoint f(double d2, double d3) {
        double d4 = ((long) (d2 * 100000.0d)) % 36000000;
        double d5 = ((long) (d3 * 100000.0d)) % 36000000;
        double d6 = -a(d4, d5);
        Double.isNaN(d4);
        double d7 = -b(d4, d5);
        Double.isNaN(d5);
        double d8 = (int) (d6 + d4);
        double d9 = (int) (d7 + d5);
        double d10 = -a(d8, d9);
        Double.isNaN(d4);
        double d11 = d10 + d4;
        double d12 = d4 > 0.0d ? 1 : -1;
        Double.isNaN(d12);
        double d13 = (int) (d11 + d12);
        double d14 = -b(d13, d9);
        Double.isNaN(d5);
        double d15 = d14 + d5;
        double d16 = d5 > 0.0d ? 1 : -1;
        Double.isNaN(d16);
        Double.isNaN(d13);
        double d17 = (int) (d15 + d16);
        Double.isNaN(d17);
        return DPoint.obtain(d13 / 100000.0d, d17 / 100000.0d);
    }

    private static DPoint g(double d2, double d3) {
        DPoint obtain = DPoint.obtain();
        double d4 = (d2 * d2) + (d3 * d3);
        double cos = (Math.cos(b(d2) + Math.atan2(d3, d2)) * (a(d3) + Math.sqrt(d4))) + 0.0065d;
        double sin = (Math.sin(b(d2) + Math.atan2(d3, d2)) * (a(d3) + Math.sqrt(d4))) + 0.006d;
        obtain.x = a(cos, 8);
        obtain.y = a(sin, 8);
        return obtain;
    }

    private static LatLng h(double d2, double d3) {
        LatLng i = i(d2, d3);
        return new LatLng((d2 * 2.0d) - i.latitude, (d3 * 2.0d) - i.longitude);
    }

    private static LatLng i(double d2, double d3) {
        double d4 = d3 - 105.0d;
        double d5 = d2 - 35.0d;
        double d6 = d(d4, d5);
        double e2 = e(d4, d5);
        double d7 = (d2 / 180.0d) * a;
        double sin = Math.sin(d7);
        double d8 = 1.0d - ((c * sin) * sin);
        double sqrt = Math.sqrt(d8);
        return new LatLng(d2 + ((d6 * 180.0d) / (((b * (1.0d - c)) / (d8 * sqrt)) * a)), d3 + ((e2 * 180.0d) / (((b / sqrt) * Math.cos(d7)) * a)));
    }
}
