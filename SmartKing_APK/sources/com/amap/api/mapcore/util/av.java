package com.amap.api.mapcore.util;

import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.AbstractCameraUpdateMessage;

/* compiled from: AbstractCameraZoomMessage.java */
/* loaded from: classes.dex */
public class av extends AbstractCameraUpdateMessage {
    @Override // com.autonavi.amap.mapcore.AbstractCameraUpdateMessage
    public void mergeCameraUpdateDelegate(AbstractCameraUpdateMessage abstractCameraUpdateMessage) {
        abstractCameraUpdateMessage.zoom += this.amount;
    }

    @Override // com.autonavi.amap.mapcore.AbstractCameraUpdateMessage
    public void runCameraUpdate(GLMapState gLMapState) {
        this.zoom = gLMapState.getMapZoomer() + this.amount;
        this.zoom = fr.a(this.mapConfig, this.zoom);
        normalChange(gLMapState);
    }
}
