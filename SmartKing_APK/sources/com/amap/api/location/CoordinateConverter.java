package com.amap.api.location;

import android.content.Context;
import com.loc.es;
import com.loc.eu;
import com.loc.fa;

/* loaded from: classes.dex */
public class CoordinateConverter {
    private Context b;
    private CoordType c = null;
    private DPoint d = null;
    DPoint a = null;

    /* loaded from: classes.dex */
    public enum CoordType {
        BAIDU,
        MAPBAR,
        MAPABC,
        SOSOMAP,
        ALIYUN,
        GOOGLE,
        GPS
    }

    public CoordinateConverter(Context context) {
        this.b = context;
    }

    public static float calculateLineDistance(DPoint dPoint, DPoint dPoint2) {
        try {
            return fa.a(dPoint, dPoint2);
        } catch (Throwable unused) {
            return 0.0f;
        }
    }

    public static boolean isAMapDataAvailable(double d, double d2) {
        return es.a(d, d2);
    }

    public synchronized DPoint convert() throws Exception {
        DPoint a;
        if (this.c == null) {
            throw new IllegalArgumentException("转换坐标类型不能为空");
        }
        if (this.d == null) {
            throw new IllegalArgumentException("转换坐标源不能为空");
        }
        if (this.d.getLongitude() > 180.0d || this.d.getLongitude() < -180.0d) {
            throw new IllegalArgumentException("请传入合理经度");
        }
        if (this.d.getLatitude() > 90.0d || this.d.getLatitude() < -90.0d) {
            throw new IllegalArgumentException("请传入合理纬度");
        }
        switch (this.c) {
            case BAIDU:
                a = eu.a(this.d);
                this.a = a;
                break;
            case MAPBAR:
                a = eu.b(this.b, this.d);
                this.a = a;
                break;
            case MAPABC:
            case SOSOMAP:
            case ALIYUN:
            case GOOGLE:
                a = this.d;
                this.a = a;
                break;
            case GPS:
                a = eu.a(this.b, this.d);
                this.a = a;
                break;
        }
        return this.a;
    }

    public synchronized CoordinateConverter coord(DPoint dPoint) throws Exception {
        if (dPoint == null) {
            throw new IllegalArgumentException("传入经纬度对象为空");
        }
        if (dPoint.getLongitude() > 180.0d || dPoint.getLongitude() < -180.0d) {
            throw new IllegalArgumentException("请传入合理经度");
        }
        if (dPoint.getLatitude() > 90.0d || dPoint.getLatitude() < -90.0d) {
            throw new IllegalArgumentException("请传入合理纬度");
        }
        this.d = dPoint;
        return this;
    }

    public synchronized CoordinateConverter from(CoordType coordType) {
        this.c = coordType;
        return this;
    }
}
