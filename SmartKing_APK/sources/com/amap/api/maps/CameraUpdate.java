package com.amap.api.maps;

import com.autonavi.amap.mapcore.AbstractCameraUpdateMessage;

/* loaded from: classes.dex */
public final class CameraUpdate {
    AbstractCameraUpdateMessage a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CameraUpdate(AbstractCameraUpdateMessage abstractCameraUpdateMessage) {
        this.a = abstractCameraUpdateMessage;
    }

    public AbstractCameraUpdateMessage getCameraUpdateFactoryDelegate() {
        return this.a;
    }
}
