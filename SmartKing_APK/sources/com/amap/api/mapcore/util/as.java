package com.amap.api.mapcore.util;

import android.util.Pair;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.AbstractCameraUpdateMessage;
import com.autonavi.amap.mapcore.IPoint;

/* compiled from: AbstractCameraBoundsMessage.java */
/* loaded from: classes.dex */
public class as extends AbstractCameraUpdateMessage {
    @Override // com.autonavi.amap.mapcore.AbstractCameraUpdateMessage
    public void mergeCameraUpdateDelegate(AbstractCameraUpdateMessage abstractCameraUpdateMessage) {
    }

    @Override // com.autonavi.amap.mapcore.AbstractCameraUpdateMessage
    public void runCameraUpdate(GLMapState gLMapState) {
        Pair<Float, IPoint> a = fr.a(this, gLMapState, this.mapConfig);
        if (a == null) {
            return;
        }
        gLMapState.setMapZoomer(((Float) a.first).floatValue());
        gLMapState.setMapGeoCenter(((IPoint) a.second).x, ((IPoint) a.second).y);
        gLMapState.setCameraDegree(0.0f);
        gLMapState.setMapAngle(0.0f);
    }
}
