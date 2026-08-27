package com.amap.api.mapcore.util;

import android.graphics.Point;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.AbstractCameraUpdateMessage;

/* compiled from: AbstractCameraScrollMessage.java */
/* loaded from: classes.dex */
public class au extends AbstractCameraUpdateMessage {
    public void a(GLMapState gLMapState, int i, int i2, Point point) {
        gLMapState.screenToP20Point(i, i2, point);
    }

    @Override // com.autonavi.amap.mapcore.AbstractCameraUpdateMessage
    public void mergeCameraUpdateDelegate(AbstractCameraUpdateMessage abstractCameraUpdateMessage) {
    }

    @Override // com.autonavi.amap.mapcore.AbstractCameraUpdateMessage
    public void runCameraUpdate(GLMapState gLMapState) {
        float f = this.xPixel;
        float f2 = (this.width / 2.0f) + f;
        float f3 = (this.height / 2.0f) + this.yPixel;
        Point point = new Point();
        a(gLMapState, (int) f2, (int) f3, point);
        gLMapState.setMapGeoCenter(point.x, point.y);
    }
}
