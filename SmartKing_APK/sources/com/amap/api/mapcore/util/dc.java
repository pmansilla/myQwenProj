package com.amap.api.mapcore.util;

import javax.microedition.khronos.opengles.GL10;

/* compiled from: AbstractGlOverlay.java */
/* loaded from: classes.dex */
public abstract class dc {
    private ad map;

    public void destroy() {
        ad adVar = this.map;
    }

    public abstract int getZIndex();

    public abstract void onDrawFrame(GL10 gl10);
}
