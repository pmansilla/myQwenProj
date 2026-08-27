package com.amap.api.maps;

import android.content.Context;
import com.amap.api.mapcore.util.ax;
import com.amap.api.mapcore.util.fk;
import com.amap.api.mapcore.util.ic;
import com.amap.api.maps.model.LatLng;

/* loaded from: classes.dex */
public class CoordinateConverter {
    private Context a;
    private CoordType b = null;
    private LatLng c = null;

    /* loaded from: classes.dex */
    public enum CoordType {
        BAIDU,
        MAPBAR,
        GPS,
        MAPABC,
        SOSOMAP,
        ALIYUN,
        GOOGLE
    }

    public CoordinateConverter(Context context) {
        this.a = context;
    }

    public static boolean isAMapDataAvailable(double d, double d2) {
        return fk.a(d, d2);
    }

    public LatLng convert() {
        LatLng latLng = null;
        if (this.b == null || this.c == null) {
            return null;
        }
        try {
            switch (this.b) {
                case BAIDU:
                    latLng = ax.a(this.c);
                    break;
                case MAPBAR:
                    latLng = ax.b(this.a, this.c);
                    break;
                case MAPABC:
                case SOSOMAP:
                case ALIYUN:
                case GOOGLE:
                    latLng = this.c;
                    break;
                case GPS:
                    latLng = ax.a(this.a, this.c);
                    break;
            }
            return latLng;
        } catch (Throwable th) {
            th.printStackTrace();
            ic.c(th, "CoordinateConverter", "convert");
            return this.c;
        }
    }

    public CoordinateConverter coord(LatLng latLng) {
        this.c = latLng;
        return this;
    }

    public CoordinateConverter from(CoordType coordType) {
        this.b = coordType;
        return this;
    }
}
