package com.amap.api.maps.model.particle;

import com.autonavi.amap.mapcore.AMapNativeParticleSystem;
import com.autonavi.amap.mapcore.AbstractNativeInstance;

/* loaded from: classes.dex */
public abstract class ParticleShapeModule extends AbstractNativeInstance {
    protected final int TYPE_DEFAULT = -1;
    protected final int TYPE_SINGLEPOINT = 0;
    protected final int TYPE_RECT = 1;
    protected int type = -1;
    protected boolean isUseRatio = false;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.autonavi.amap.mapcore.AbstractNativeInstance
    public void finalize() throws Throwable {
        super.finalize();
        if (this.nativeInstance != 0) {
            AMapNativeParticleSystem.nativeReleaseParticleShapeModule(this.nativeInstance);
            this.nativeInstance = 0L;
        }
    }

    public abstract float[] getPoint();

    public boolean isUseRatio() {
        return this.isUseRatio;
    }
}
