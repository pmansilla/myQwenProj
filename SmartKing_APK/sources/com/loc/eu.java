package com.loc;

import android.content.Context;
import com.amap.api.location.CoordUtil;
import com.amap.api.location.DPoint;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* compiled from: OffsetUtil.java */
/* loaded from: classes.dex */
public final class eu {
    static double a = 3.141592653589793d;
    private static String b = "Jni_wgs2gcj";
    private static final List<DPoint> c = new ArrayList(Arrays.asList(new DPoint(23.379947d, 119.757001d), new DPoint(24.983296d, 120.474496d), new DPoint(25.518722d, 121.359866d), new DPoint(25.41329d, 122.443582d), new DPoint(24.862708d, 122.288354d), new DPoint(24.461292d, 122.188319d), new DPoint(21.584761d, 120.968923d), new DPoint(21.830837d, 120.654445d)));

    private static double a(double d) {
        return Math.sin(d * 3000.0d * (a / 180.0d)) * 2.0E-5d;
    }

    private static double a(double d, double d2) {
        return (Math.cos(d2 / 100000.0d) * (d / 18000.0d)) + (Math.sin(d / 100000.0d) * (d2 / 9000.0d));
    }

    public static DPoint a(Context context, double d, double d2) {
        if (context == null) {
            return null;
        }
        return a(context, new DPoint(d2, d));
    }

    public static DPoint a(Context context, DPoint dPoint) {
        if (context == null) {
            return null;
        }
        if (!CoordUtil.isLoadedSo()) {
            System.loadLibrary(b);
            CoordUtil.setLoadedSo(true);
        }
        return b(dPoint);
    }

    public static DPoint a(DPoint dPoint) {
        if (dPoint != null) {
            try {
                if (es.a(dPoint.getLatitude(), dPoint.getLongitude())) {
                    return c(dPoint);
                }
                if (!es.a(new DPoint(dPoint.getLatitude(), dPoint.getLongitude()), c)) {
                    return dPoint;
                }
                DPoint c2 = c(dPoint);
                double latitude = c2.getLatitude();
                double longitude = c2.getLongitude();
                double d = longitude - 105.0d;
                double d2 = latitude - 35.0d;
                double d3 = d * 2.0d;
                double d4 = d * 0.1d;
                double d5 = d4 * d2;
                double d6 = 6.0d * d;
                double sqrt = (-100.0d) + d3 + (d2 * 3.0d) + (d2 * 0.2d * d2) + d5 + (Math.sqrt(Math.abs(d)) * 0.2d) + ((((Math.sin(a * d6) * 20.0d) + (Math.sin(a * d3) * 20.0d)) * 2.0d) / 3.0d) + ((((Math.sin(a * d2) * 20.0d) + (Math.sin((d2 / 3.0d) * a) * 40.0d)) * 2.0d) / 3.0d) + ((((Math.sin((d2 / 12.0d) * a) * 160.0d) + (Math.sin((a * d2) / 30.0d) * 320.0d)) * 2.0d) / 3.0d);
                double sqrt2 = d + 300.0d + (d2 * 2.0d) + (d4 * d) + d5 + (Math.sqrt(Math.abs(d)) * 0.1d) + ((((Math.sin(d6 * a) * 20.0d) + (Math.sin(d3 * a) * 20.0d)) * 2.0d) / 3.0d) + ((((Math.sin(a * d) * 20.0d) + (Math.sin((d / 3.0d) * a) * 40.0d)) * 2.0d) / 3.0d) + ((((Math.sin((d / 12.0d) * a) * 150.0d) + (Math.sin((d / 30.0d) * a) * 300.0d)) * 2.0d) / 3.0d);
                double d7 = (latitude / 180.0d) * a;
                double sin = Math.sin(d7);
                double d8 = 1.0d - ((0.006693421622965943d * sin) * sin);
                double sqrt3 = Math.sqrt(d8);
                DPoint dPoint2 = new DPoint(((sqrt * 180.0d) / ((6335552.717000426d / (d8 * sqrt3)) * a)) + latitude, longitude + ((sqrt2 * 180.0d) / (((6378245.0d / sqrt3) * Math.cos(d7)) * a)));
                return new DPoint((latitude * 2.0d) - dPoint2.getLatitude(), (longitude * 2.0d) - dPoint2.getLongitude());
            } catch (Throwable th) {
                es.a(th, "OffsetUtil", "b2G");
            }
        }
        return dPoint;
    }

    private static double b(double d) {
        return Math.cos(d * 3000.0d * (a / 180.0d)) * 3.0E-6d;
    }

    private static double b(double d, double d2) {
        return (Math.sin(d2 / 100000.0d) * (d / 18000.0d)) + (Math.cos(d / 100000.0d) * (d2 / 9000.0d));
    }

    public static DPoint b(Context context, DPoint dPoint) {
        try {
            if (!es.a(dPoint.getLatitude(), dPoint.getLongitude())) {
                return dPoint;
            }
            double longitude = ((long) (dPoint.getLongitude() * 100000.0d)) % 36000000;
            double latitude = ((long) (dPoint.getLatitude() * 100000.0d)) % 36000000;
            double d = -a(longitude, latitude);
            Double.isNaN(longitude);
            int i = (int) (d + longitude);
            double d2 = -b(longitude, latitude);
            Double.isNaN(latitude);
            double d3 = (int) (d2 + latitude);
            double d4 = -a(i, d3);
            Double.isNaN(longitude);
            double d5 = d4 + longitude;
            double d6 = longitude > 0.0d ? 1 : -1;
            Double.isNaN(d6);
            double d7 = (int) (d5 + d6);
            double d8 = -b(d7, d3);
            Double.isNaN(latitude);
            double d9 = d8 + latitude;
            double d10 = latitude > 0.0d ? 1 : -1;
            Double.isNaN(d10);
            Double.isNaN(d7);
            double d11 = d7 / 100000.0d;
            double d12 = (int) (d9 + d10);
            Double.isNaN(d12);
            return a(context, new DPoint(d12 / 100000.0d, d11));
        } catch (Throwable th) {
            es.a(th, "OffsetUtil", "marbar2G");
            return dPoint;
        }
    }

    private static DPoint b(DPoint dPoint) {
        double longitude;
        double latitude;
        try {
            if (!es.a(dPoint.getLatitude(), dPoint.getLongitude())) {
                return dPoint;
            }
            double[] dArr = new double[2];
            try {
                try {
                } catch (Throwable th) {
                    fb.a(dPoint.getLongitude(), dPoint.getLatitude());
                    throw th;
                }
            } catch (Throwable th2) {
                es.a(th2, "OffsetUtil", "cover part1");
                longitude = dPoint.getLongitude();
                latitude = dPoint.getLatitude();
            }
            if (CoordUtil.convertToGcj(new double[]{dPoint.getLongitude(), dPoint.getLatitude()}, dArr) != 0) {
                longitude = dPoint.getLongitude();
                latitude = dPoint.getLatitude();
                dArr = fb.a(longitude, latitude);
            }
            return new DPoint(dArr[1], dArr[0]);
        } catch (Throwable th3) {
            es.a(th3, "OffsetUtil", "cover part2");
            return dPoint;
        }
    }

    private static double c(double d) {
        return new BigDecimal(d).setScale(8, 4).doubleValue();
    }

    private static DPoint c(DPoint dPoint) {
        double d = 0.006401062d;
        double d2 = 0.0060424805d;
        DPoint dPoint2 = null;
        for (int i = 0; i < 2; i++) {
            double longitude = dPoint.getLongitude();
            double latitude = dPoint.getLatitude();
            dPoint2 = new DPoint();
            double d3 = longitude - d;
            double d4 = latitude - d2;
            DPoint dPoint3 = new DPoint();
            double d5 = (d3 * d3) + (d4 * d4);
            double cos = (Math.cos(b(d3) + Math.atan2(d4, d3)) * (a(d4) + Math.sqrt(d5))) + 0.0065d;
            double sin = (Math.sin(b(d3) + Math.atan2(d4, d3)) * (a(d4) + Math.sqrt(d5))) + 0.006d;
            dPoint3.setLongitude(c(cos));
            dPoint3.setLatitude(c(sin));
            dPoint2.setLongitude(c((longitude + d3) - dPoint3.getLongitude()));
            dPoint2.setLatitude(c((latitude + d4) - dPoint3.getLatitude()));
            d = dPoint.getLongitude() - dPoint2.getLongitude();
            d2 = dPoint.getLatitude() - dPoint2.getLatitude();
        }
        return dPoint2;
    }
}
