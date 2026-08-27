package com.amap.api.mapcore.util;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.autonavi.amap.mapcore.Inner_3dMap_location;
import com.autonavi.amap.mapcore.Inner_3dMap_locationListener;

/* compiled from: LocationListener.java */
/* loaded from: classes.dex */
public final class kb implements AMapLocationListener {
    public Inner_3dMap_locationListener a = null;

    public final void a(Inner_3dMap_locationListener inner_3dMap_locationListener) {
        this.a = inner_3dMap_locationListener;
    }

    @Override // com.amap.api.location.AMapLocationListener
    public final void onLocationChanged(AMapLocation aMapLocation) {
        try {
            Inner_3dMap_location a = kd.a(aMapLocation);
            if (km.a(a)) {
                kd.a = a;
            }
            if (this.a != null) {
                this.a.onLocationChanged(a);
            }
        } catch (Throwable th) {
            kw.a(th, "LocationListener", "onLocationChanged");
        }
    }
}
