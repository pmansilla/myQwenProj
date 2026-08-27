package com.autonavi.ae.gmap.gloverlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.util.TypedValue;
import com.amap.api.mapcore.util.ad;
import com.amap.api.mapcore.util.an;
import com.amap.api.mapcore.util.ic;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CrossOverlay;
import com.autonavi.ae.gmap.gloverlay.GLCrossVector;

/* loaded from: classes.dex */
public class CrossVectorOverlay extends BaseMapOverlay<GLCrossVector, Object> implements an.a {
    GLCrossVector.AVectorCrossAttr attr;
    private CrossOverlay.GenerateCrossImageListener imageListener;
    private boolean isImageMode;
    private an pluginTexture;

    public CrossVectorOverlay(int i, Context context, ad adVar) {
        super(i, context, adVar);
        this.isImageMode = false;
        this.attr = null;
    }

    private void drawVectorFailed(int i) {
        if (this.pluginTexture != null) {
            this.pluginTexture.b();
        }
        if (this.imageListener != null) {
            this.imageListener.onGenerateComplete(null, i);
        }
    }

    private void initImageMode(int i, int i2) {
        if (this.pluginTexture == null) {
            this.pluginTexture = new an(this.mMapView);
            this.pluginTexture.a(this.imageListener);
            this.pluginTexture.a(this);
            this.pluginTexture.b(this.attr.stAreaRect.width(), this.attr.stAreaRect.height());
        }
        if (this.mGLOverlay != 0) {
            ((GLCrossVector) this.mGLOverlay).initFBOTexture(i, i2);
        }
    }

    public void AddOverlayTexture(Bitmap bitmap, int i, int i2) {
        GLTextureProperty gLTextureProperty = new GLTextureProperty();
        gLTextureProperty.mId = i;
        gLTextureProperty.mAnchor = i2;
        gLTextureProperty.mBitmap = bitmap;
        gLTextureProperty.mXRatio = 0.0f;
        gLTextureProperty.mYRatio = 0.0f;
        gLTextureProperty.isGenMimps = true;
        this.mMapView.addOverlayTexture(this.mEngineID, gLTextureProperty);
    }

    @Override // com.autonavi.ae.gmap.gloverlay.BaseMapOverlay
    public void addItem(Object obj) {
    }

    public int dipToPixel(Context context, int i) {
        if (context == null) {
            return i;
        }
        try {
            return (int) TypedValue.applyDimension(1, i, context.getResources().getDisplayMetrics());
        } catch (Exception unused) {
            return i;
        }
    }

    @Override // com.amap.api.mapcore.util.an.a
    public int getTextureID() {
        return ((GLCrossVector) this.mGLOverlay).getFBOTextureId();
    }

    @Override // com.autonavi.ae.gmap.gloverlay.BaseMapOverlay
    protected void iniGLOverlay() {
        this.mGLOverlay = new GLCrossVector(this.mEngineID, this.mMapView, hashCode());
    }

    public void remove() {
        if (this.pluginTexture != null) {
            this.pluginTexture.b();
            this.pluginTexture = null;
        }
        this.imageListener = null;
        setVisible(false);
        releaseInstance();
    }

    @Override // com.autonavi.ae.gmap.gloverlay.BaseMapOverlay
    public void resumeMarker(Bitmap bitmap) {
        AddOverlayTexture(bitmap, 12345, 4);
        ((GLCrossVector) this.mGLOverlay).setArrowResId(false, 12345);
        ((GLCrossVector) this.mGLOverlay).setCarResId(12345);
        BitmapDescriptor fromAsset = BitmapDescriptorFactory.fromAsset("cross/crossing_nigth_bk.data");
        AddOverlayTexture(fromAsset != null ? fromAsset.getBitmap() : null, 54321, 0);
        ((GLCrossVector) this.mGLOverlay).setBackgroundResId(54321);
    }

    public void setAttribute(GLCrossVector.AVectorCrossAttr aVectorCrossAttr) {
        this.attr = aVectorCrossAttr;
    }

    public int setData(byte[] bArr) {
        int i;
        if (Build.VERSION.SDK_INT < 21) {
            return -1;
        }
        if (this.attr == null) {
            this.attr = new GLCrossVector.AVectorCrossAttr();
            this.attr.stAreaRect = new Rect(0, 0, this.mMapView.getMapWidth(), (this.mMapView.getMapHeight() * 4) / 11);
            this.attr.stAreaColor = Color.argb(217, 95, 95, 95);
            this.attr.fArrowBorderWidth = dipToPixel(this.mContext, 22);
            this.attr.stArrowBorderColor = Color.argb(0, 0, 50, 20);
            this.attr.fArrowLineWidth = dipToPixel(this.mContext, 18);
            this.attr.stArrowLineColor = Color.argb(255, 255, 253, 65);
            this.attr.dayMode = false;
        }
        if (bArr == null || this.attr == null) {
            i = -1;
        } else {
            final int mapWidth = this.mMapView.getMapWidth();
            final int mapHeight = this.mMapView.getMapHeight();
            if (this.isImageMode && this.imageListener != null) {
                initImageMode(mapWidth, mapHeight);
            }
            i = ((GLCrossVector) this.mGLOverlay).addVectorItem(this.attr, bArr, bArr.length);
            ((GLCrossVector) this.mGLOverlay).setVisible(true);
            if (this.isImageMode && this.imageListener != null) {
                this.mMapView.queueEvent(new Runnable() { // from class: com.autonavi.ae.gmap.gloverlay.CrossVectorOverlay.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            if (CrossVectorOverlay.this.mGLOverlay == 0 || !((GLCrossVector) CrossVectorOverlay.this.mGLOverlay).isVisible() || CrossVectorOverlay.this.pluginTexture == null || CrossVectorOverlay.this.pluginTexture.c()) {
                                return;
                            }
                            CrossVectorOverlay.this.pluginTexture.a(mapWidth, mapHeight);
                            CrossVectorOverlay.this.pluginTexture.a();
                        } catch (Throwable th) {
                            ic.c(th, "CrossVectorOverlay", "setData");
                        }
                    }
                });
            }
        }
        if (i == -1) {
            drawVectorFailed(i);
        }
        return i;
    }

    public void setGenerateCrossImageListener(CrossOverlay.GenerateCrossImageListener generateCrossImageListener) {
        this.imageListener = generateCrossImageListener;
        if (this.pluginTexture != null) {
            this.pluginTexture.a(this.imageListener);
        }
    }

    public void setImageMode(boolean z) {
        this.isImageMode = z;
    }
}
