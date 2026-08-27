package com.amap.api.maps.model;

import android.graphics.Bitmap;
import com.autonavi.ae.gmap.gloverlay.GLCrossVector;

/* loaded from: classes.dex */
public class CrossOverlayOptions {
    GLCrossVector.AVectorCrossAttr a = null;
    private Bitmap bitmapDescriptor = null;

    public GLCrossVector.AVectorCrossAttr getAttribute() {
        return this.a;
    }

    public Bitmap getRes() {
        return this.bitmapDescriptor;
    }

    public CrossOverlayOptions setAttribute(GLCrossVector.AVectorCrossAttr aVectorCrossAttr) {
        this.a = aVectorCrossAttr;
        return this;
    }

    public CrossOverlayOptions setRes(Bitmap bitmap) {
        this.bitmapDescriptor = bitmap;
        return this;
    }
}
