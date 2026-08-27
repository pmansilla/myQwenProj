package com.amap.api.mapcore.util;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

/* compiled from: AMapGLRenderer.java */
/* loaded from: classes.dex */
public class m implements ae {
    protected boolean a;
    private ad b;

    public m(Context context) {
        this(context, null);
    }

    public m(Context context, AttributeSet attributeSet) {
        this.b = null;
        this.a = false;
        this.b = new l(this, context, attributeSet);
    }

    public ad a() {
        return this.b;
    }

    @Override // com.amap.api.mapcore.util.ae
    public void a(ex exVar) {
    }

    @Override // com.amap.api.mapcore.util.ae
    public void a(ey eyVar) {
    }

    @Override // com.amap.api.mapcore.util.ae
    public void b() {
    }

    @Override // com.amap.api.mapcore.util.ae
    public int getHeight() {
        return 0;
    }

    @Override // com.amap.api.mapcore.util.ae
    public SurfaceHolder getHolder() {
        return null;
    }

    @Override // com.amap.api.mapcore.util.ae
    public int getRenderMode() {
        return 0;
    }

    @Override // com.amap.api.mapcore.util.ae
    public int getWidth() {
        return 0;
    }

    @Override // com.amap.api.mapcore.util.ae
    public boolean isEnabled() {
        return this.b != null;
    }

    @Override // com.amap.api.mapcore.util.ae
    public boolean post(Runnable runnable) {
        return false;
    }

    @Override // com.amap.api.mapcore.util.ae
    public boolean postDelayed(Runnable runnable, long j) {
        return false;
    }

    @Override // com.amap.api.mapcore.util.ae
    public void queueEvent(Runnable runnable) {
        if (runnable != null) {
            runnable.run();
        }
    }

    @Override // com.amap.api.mapcore.util.ae
    public void requestRender() {
    }

    @Override // com.amap.api.mapcore.util.ae
    public void setRenderMode(int i) {
    }

    @Override // com.amap.api.mapcore.util.ae
    public void setRenderer(GLSurfaceView.Renderer renderer) {
    }

    @Override // com.amap.api.mapcore.util.ae
    public void setVisibility(int i) {
    }
}
