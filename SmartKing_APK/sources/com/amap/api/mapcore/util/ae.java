package com.amap.api.mapcore.util;

import android.opengl.GLSurfaceView;
import android.view.SurfaceHolder;

/* compiled from: IGLSurfaceView.java */
/* loaded from: classes.dex */
public interface ae {
    void a(ex exVar);

    void a(ey eyVar);

    void b();

    int getHeight();

    SurfaceHolder getHolder();

    int getRenderMode();

    int getWidth();

    boolean isEnabled();

    boolean post(Runnable runnable);

    boolean postDelayed(Runnable runnable, long j);

    void queueEvent(Runnable runnable);

    void requestRender();

    void setRenderMode(int i);

    void setRenderer(GLSurfaceView.Renderer renderer);

    void setVisibility(int i);
}
