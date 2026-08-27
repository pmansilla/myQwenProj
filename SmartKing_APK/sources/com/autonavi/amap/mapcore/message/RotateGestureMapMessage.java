package com.autonavi.amap.mapcore.message;

import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.ae.gmap.maploader.Pools;
import com.autonavi.amap.mapcore.IPoint;

/* loaded from: classes.dex */
public class RotateGestureMapMessage extends AbstractGestureMapMessage {
    private static final Pools.SynchronizedPool<RotateGestureMapMessage> M_POOL = new Pools.SynchronizedPool<>(256);
    public float angleDelta;
    public int pivotX;
    public int pivotY;

    public RotateGestureMapMessage(int i, float f, int i2, int i3) {
        super(i);
        this.pivotX = 0;
        this.pivotY = 0;
        this.angleDelta = 0.0f;
        setParams(i, f, i2, i3);
        this.angleDelta = f;
        this.pivotX = i2;
        this.pivotY = i3;
    }

    public static void destory() {
        M_POOL.destory();
    }

    public static RotateGestureMapMessage obtain(int i, float f, int i2, int i3) {
        RotateGestureMapMessage acquire = M_POOL.acquire();
        if (acquire == null) {
            return new RotateGestureMapMessage(i, f, i2, i3);
        }
        acquire.reset();
        acquire.setParams(i, f, i2, i3);
        return acquire;
    }

    private void setParams(int i, float f, int i2, int i3) {
        setState(i);
        this.angleDelta = f;
        this.pivotX = i2;
        this.pivotY = i3;
    }

    @Override // com.autonavi.amap.mapcore.message.AbstractGestureMapMessage, com.autonavi.ae.gmap.AbstractMapMessage
    public int getType() {
        return 2;
    }

    public void recycle() {
        M_POOL.release(this);
    }

    @Override // com.autonavi.amap.mapcore.message.AbstractGestureMapMessage
    public void runCameraUpdate(GLMapState gLMapState) {
        IPoint iPoint;
        float mapAngle = gLMapState.getMapAngle() + this.angleDelta;
        if (this.isGestureScaleByMapCenter) {
            gLMapState.setMapAngle(mapAngle);
            gLMapState.recalculate();
            return;
        }
        int i = this.pivotX;
        int i2 = this.pivotY;
        if (this.isUseAnchor) {
            i = this.anchorX;
            i2 = this.anchorY;
        }
        IPoint iPoint2 = null;
        if (i > 0 || i2 > 0) {
            IPoint obtain = IPoint.obtain();
            IPoint obtain2 = IPoint.obtain();
            win2geo(gLMapState, i, i2, obtain);
            gLMapState.setMapGeoCenter(obtain.x, obtain.y);
            iPoint = obtain;
            iPoint2 = obtain2;
        } else {
            iPoint = null;
        }
        gLMapState.setMapAngle(mapAngle);
        gLMapState.recalculate();
        if (i > 0 || i2 > 0) {
            win2geo(gLMapState, i, i2, iPoint2);
            if (iPoint != null) {
                gLMapState.setMapGeoCenter((iPoint.x * 2) - iPoint2.x, (iPoint.y * 2) - iPoint2.y);
            }
            gLMapState.recalculate();
        }
        if (iPoint != null) {
            iPoint.recycle();
        }
        if (iPoint2 != null) {
            iPoint2.recycle();
        }
    }
}
