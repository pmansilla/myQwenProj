package com.amap.api.mapcore.util;

import android.location.Location;
import com.amap.api.maps.LocationSource;

/* compiled from: AMapOnLocationChangedListener.java */
/* loaded from: classes.dex */
class q implements LocationSource.OnLocationChangedListener {
    Location a;
    private ad b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public q(ad adVar) {
        this.b = adVar;
    }

    @Override // com.amap.api.maps.LocationSource.OnLocationChangedListener
    public void onLocationChanged(Location location) {
        this.a = location;
        try {
            if (this.b.isMyLocationEnabled()) {
                this.b.a(location);
            }
        } catch (Throwable th) {
            ic.c(th, "AMapOnLocationChangedListener", "onLocationChanged");
            th.printStackTrace();
        }
    }
}
