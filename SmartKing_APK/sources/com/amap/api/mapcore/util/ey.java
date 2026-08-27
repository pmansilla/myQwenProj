package com.amap.api.mapcore.util;

import android.opengl.GLSurfaceView;
import com.amap.api.mapcore.util.x;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

/* compiled from: GlMapSurfaceEglContextFactory.java */
/* loaded from: classes.dex */
public class ey implements GLSurfaceView.EGLContextFactory, x.f {
    @Override // android.opengl.GLSurfaceView.EGLContextFactory, com.amap.api.mapcore.util.x.f
    public EGLContext createContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig) {
        return null;
    }

    @Override // android.opengl.GLSurfaceView.EGLContextFactory, com.amap.api.mapcore.util.x.f
    public void destroyContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLContext eGLContext) {
    }
}
