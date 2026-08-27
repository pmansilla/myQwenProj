package com.amap.api.mapcore.util;

import com.amap.api.maps.model.LatLng;

/* compiled from: BaseOverlayImp.java */
/* loaded from: classes.dex */
public abstract class v implements ah {
    public abstract String getId();

    public abstract LatLng getPosition();

    public abstract String getSnippet();

    public abstract String getTitle();

    public boolean isDraggable() {
        return false;
    }

    public boolean isInfoWindowAutoOverturn() {
        return false;
    }

    public abstract boolean isVisible();
}
