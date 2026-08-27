package com.autonavi.ae.gmap.gloverlay;

import com.amap.api.mapcore.util.ad;
import com.autonavi.ae.gmap.GLMapEngine;

/* loaded from: classes.dex */
public abstract class GLOverlay {
    protected int mCode;
    protected int mEngineID;
    protected ad mGLMapView;
    protected int mItemPriority;
    protected long mNativeInstance;
    protected boolean isNightStyle = false;
    boolean mIsInBundle = false;

    /* loaded from: classes.dex */
    public enum EAMapOverlayTpye {
        AMAPOVERLAY_POINT,
        AMAPOVERLAY_POLYLINE,
        AMAPOVERLAY_POLYGON,
        AMAPOVERLAY_ARC,
        AMAPOVERLAY_ARROW,
        AMAPOVERLAY_VECTOR,
        AMAPOVERLAY_GROUP,
        AMAPOVERLAY_MODEL,
        AMAPOVERLAY_PLANE
    }

    public GLOverlay(int i, ad adVar, int i2) {
        this.mNativeInstance = 0L;
        this.mItemPriority = 0;
        this.mEngineID = i;
        this.mGLMapView = adVar;
        this.mCode = i2;
        this.mNativeInstance = 0L;
        this.mItemPriority = 0;
    }

    private static native int nativeGetCount(long j);

    private static native int nativeGetOverlayPriority(long j);

    private static native int nativeGetSubType(long j);

    private static native int nativeGetType(long j);

    private static native boolean nativeIsClickable(long j);

    private static native boolean nativeIsVisible(long j);

    private static native void nativeRemoveAll(long j);

    private static native void nativeRemoveItem(long j, int i);

    private static native void nativeSetClickable(long j, boolean z);

    private static native void nativeSetMaxDisplayLevel(long j, float f);

    private static native void nativeSetMinDisplayLevel(long j, float f);

    private static native void nativeSetOverlayItemPriority(long j, int i);

    private static native void nativeSetOverlayOnTop(long j, boolean z);

    private static native void nativeSetOverlayPriority(long j, int i);

    private static native void nativeSetShownMaxCount(long j, int i);

    protected static native void nativeSetVisible(long j, boolean z);

    public void clearFocus() {
    }

    public int getCode() {
        return this.mCode;
    }

    public boolean getIsInBundle() {
        return this.mIsInBundle;
    }

    public long getNativeInstatnce() {
        return this.mNativeInstance;
    }

    public int getOverlayPriority() {
        return nativeGetOverlayPriority(this.mNativeInstance);
    }

    public int getSize() {
        if (this.mNativeInstance == 0) {
            return 0;
        }
        return nativeGetCount(this.mNativeInstance);
    }

    public int getSubType() {
        if (this.mNativeInstance == 0) {
            return -1;
        }
        return nativeGetSubType(this.mNativeInstance);
    }

    public int getType() {
        if (this.mNativeInstance == 0) {
            return -1;
        }
        return nativeGetType(this.mNativeInstance);
    }

    public boolean isClickable() {
        if (this.mNativeInstance == 0) {
            return false;
        }
        return nativeIsClickable(this.mNativeInstance);
    }

    public boolean isVisible() {
        if (this.mNativeInstance == 0) {
            return false;
        }
        return nativeIsVisible(this.mNativeInstance);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void releaseInstance() {
        if (this.mNativeInstance != 0) {
            long j = this.mNativeInstance;
            this.mNativeInstance = 0L;
            GLMapEngine.destroyOverlay(this.mEngineID, j);
        }
    }

    public void removeAll() {
        if (this.mNativeInstance == 0) {
            return;
        }
        nativeRemoveAll(this.mNativeInstance);
        ad adVar = this.mGLMapView;
    }

    public void removeItem(int i) {
        if (this.mNativeInstance == 0) {
            return;
        }
        nativeRemoveItem(this.mNativeInstance, i);
    }

    public void setClickable(boolean z) {
        if (this.mNativeInstance == 0) {
            return;
        }
        nativeSetClickable(this.mNativeInstance, z);
    }

    public void setMaxCountShown(int i) {
        nativeSetShownMaxCount(this.mNativeInstance, i);
    }

    public void setMaxDisplayLevel(float f) {
        nativeSetMaxDisplayLevel(this.mNativeInstance, f);
    }

    public void setMinDisplayLevel(float f) {
        nativeSetMinDisplayLevel(this.mNativeInstance, f);
    }

    public void setOverlayItemPriority(int i) {
        this.mItemPriority = i;
    }

    public void setOverlayOnTop(boolean z) {
        nativeSetOverlayOnTop(this.mNativeInstance, z);
    }

    public void setOverlayPriority(int i) {
        GLOverlayBundle overlayBundle;
        nativeSetOverlayPriority(this.mNativeInstance, i);
        if (this.mGLMapView == null || this.mGLMapView.a() == null || (overlayBundle = this.mGLMapView.a().getOverlayBundle(this.mEngineID)) == null) {
            return;
        }
        overlayBundle.sortOverlay();
    }

    public void setVisible(boolean z) {
        if (this.mNativeInstance == 0) {
            return;
        }
        nativeSetVisible(this.mNativeInstance, z);
    }

    public void useNightStyle(boolean z) {
        this.isNightStyle = z;
    }
}
