package com.amap.api.mapcore.util;

import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.opengl.Matrix;
import com.amap.api.mapcore.util.ef;
import com.amap.api.maps.model.CrossOverlay;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* compiled from: PboPluginTexture.java */
/* loaded from: classes.dex */
public class an {
    private final ad c;
    private ExecutorService h;
    private boolean j;
    private volatile EGLContext o;
    private volatile EGLConfig p;
    private ef.f t;
    private FloatBuffer u;
    private FloatBuffer v;
    private a w;
    private CrossOverlay.GenerateCrossImageListener x;
    private int d = 0;
    private int e = 0;
    private int f = 0;
    private BlockingQueue<Runnable> g = new LinkedBlockingQueue();
    private boolean i = false;
    private int k = 0;
    private int l = 0;
    private int m = 0;
    private boolean n = false;
    private EGLDisplay q = EGL14.EGL_NO_DISPLAY;
    private EGLContext r = EGL14.EGL_NO_CONTEXT;
    private EGLSurface s = EGL14.EGL_NO_SURFACE;
    float[] a = {1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};
    float[] b = {-1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, -1.0f, 0.0f, -1.0f, -1.0f, 0.0f};

    /* compiled from: PboPluginTexture.java */
    /* loaded from: classes.dex */
    public interface a {
        int getTextureID();
    }

    public an(ad adVar) {
        this.h = null;
        this.j = false;
        this.c = adVar;
        this.j = false;
        this.h = new ThreadPoolExecutor(1, Runtime.getRuntime().availableProcessors() * 2, 1, TimeUnit.SECONDS, this.g, new fe("AMapPboRenderThread"), new ThreadPoolExecutor.AbortPolicy());
    }

    private void a(String str) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        this.q = EGL14.eglGetDisplay(0);
        if (this.q == EGL14.EGL_NO_DISPLAY) {
            a("eglGetDisplay failed");
            return;
        }
        int[] iArr = new int[2];
        if (!EGL14.eglInitialize(this.q, iArr, 0, iArr, 1)) {
            this.q = null;
            a("eglInitialize failed");
            return;
        }
        this.r = EGL14.eglCreateContext(this.q, this.p, this.o, new int[]{12440, 2, 12344}, 0);
        if (this.r == EGL14.EGL_NO_CONTEXT) {
            a("eglCreateContext failed");
            return;
        }
        this.s = EGL14.eglCreatePbufferSurface(this.q, this.p, new int[]{12375, this.e, 12374, this.f, 12344}, 0);
        if (this.s == EGL14.EGL_NO_SURFACE) {
            a("eglCreatePbufferSurface failed");
        } else {
            if (!EGL14.eglMakeCurrent(this.q, this.s, this.s, this.r)) {
                a("eglMakeCurrent failed");
                return;
            }
            GLES20.glFlush();
            a("initOpenGL complete");
            this.i = true;
        }
    }

    private void e() {
        if (this.c != null) {
            this.t = (ef.f) this.c.u(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
        try {
            if (this.j) {
                return;
            }
            if (this.w == null) {
                a("renderTextureAndReadPixel failed textureHelper is null");
                return;
            }
            if (this.w != null) {
                this.d = this.w.getTextureID();
            }
            if (this.d <= 0) {
                a("renderTextureAndReadPixel failed mTextureID is <= 0 mTextureID " + this.d);
                return;
            }
            a("renderTextureAndReadPixel  mTextureID is  mTextureID " + this.d);
            if (this.t == null || this.t.c()) {
                e();
            }
            if (this.u == null) {
                this.u = fr.a(this.b);
            }
            if (this.v == null) {
                this.v = fr.a(new float[]{0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f});
            }
            this.t.a();
            GLES20.glDisable(3042);
            GLES20.glBlendFunc(1, 771);
            GLES20.glBlendColor(1.0f, 1.0f, 1.0f, 1.0f);
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(3553, this.d);
            GLES20.glEnableVertexAttribArray(this.t.b);
            GLES20.glVertexAttribPointer(this.t.b, 3, 5126, false, 12, (Buffer) this.u);
            GLES20.glEnableVertexAttribArray(this.t.c);
            GLES20.glVertexAttribPointer(this.t.c, 2, 5126, false, 8, (Buffer) this.v);
            Matrix.setIdentityM(this.a, 0);
            Matrix.scaleM(this.a, 0, 1.0f, 1.0f, 1.0f);
            GLES20.glUniformMatrix4fv(this.t.a, 1, false, this.a, 0);
            GLES20.glDrawArrays(6, 0, 4);
            GLES20.glDisableVertexAttribArray(this.t.b);
            GLES20.glDisableVertexAttribArray(this.t.c);
            GLES20.glBindTexture(3553, 0);
            GLES20.glUseProgram(0);
            GLES20.glDisable(3042);
            ez.a("drawTexure");
            GLES20.glFinish();
            this.k++;
            if (this.k == 50) {
                g();
            }
        } catch (Throwable unused) {
            if (this.x != null) {
                this.x.onGenerateComplete(null, -1);
            }
        }
    }

    private void g() {
        if (this.x != null) {
            if (this.l == 0) {
                this.l = this.e;
            }
            if (this.m == 0) {
                this.m = this.f;
            }
            this.x.onGenerateComplete(fr.a(0, this.f - this.m, this.l, this.m), this.i ? 0 : -1);
            this.n = true;
        }
    }

    public void a() {
        if (this.h == null || this.h.isShutdown()) {
            return;
        }
        this.h.execute(new Runnable() { // from class: com.amap.api.mapcore.util.an.2
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.lang.Runnable
            public void run() {
                try {
                    an.this.n = false;
                    if (an.this.j) {
                        return;
                    }
                    an.this.k = 0;
                    int i = 0;
                    while (!an.this.j && an.this.k < 5 && i < 50) {
                        i++;
                        try {
                            Thread.sleep(16L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!an.this.i) {
                            if (an.this.x != null) {
                                an.this.x.onGenerateComplete(null, -1);
                            }
                            if (!an.this.n) {
                                an.this.n = true;
                                if (an.this.x != null) {
                                    an.this.x.onGenerateComplete(null, -1);
                                }
                            }
                            if (an.this.r != EGL14.EGL_NO_CONTEXT) {
                                EGL14.eglDestroyContext(an.this.q, an.this.r);
                                EGL14.eglDestroySurface(an.this.q, an.this.s);
                                an.this.r = null;
                            }
                            if (an.this.q != EGL14.EGL_NO_DISPLAY) {
                                EGL14.eglTerminate(an.this.q);
                                an.this.q = null;
                            }
                            an.this.r = EGL14.EGL_NO_CONTEXT;
                            an.this.q = EGL14.EGL_NO_DISPLAY;
                            return;
                        }
                        GLES20.glViewport(0, 0, an.this.e, an.this.f);
                        GLES20.glClear(16640);
                        an.this.f();
                    }
                    if (!an.this.n) {
                        an.this.n = true;
                        if (an.this.x != null) {
                            an.this.x.onGenerateComplete(null, -1);
                        }
                    }
                    if (an.this.r != EGL14.EGL_NO_CONTEXT) {
                        EGL14.eglDestroyContext(an.this.q, an.this.r);
                        EGL14.eglDestroySurface(an.this.q, an.this.s);
                        an.this.r = null;
                    }
                    if (an.this.q != EGL14.EGL_NO_DISPLAY) {
                        EGL14.eglTerminate(an.this.q);
                        an.this.q = null;
                    }
                    an.this.r = EGL14.EGL_NO_CONTEXT;
                    an.this.q = EGL14.EGL_NO_DISPLAY;
                } finally {
                    if (!an.this.n) {
                        an.this.n = true;
                        if (an.this.x != null) {
                            an.this.x.onGenerateComplete(null, -1);
                        }
                    }
                    if (an.this.r != EGL14.EGL_NO_CONTEXT) {
                        EGL14.eglDestroyContext(an.this.q, an.this.r);
                        EGL14.eglDestroySurface(an.this.q, an.this.s);
                        an.this.r = null;
                    }
                    if (an.this.q != EGL14.EGL_NO_DISPLAY) {
                        EGL14.eglTerminate(an.this.q);
                        an.this.q = null;
                    }
                    an.this.r = EGL14.EGL_NO_CONTEXT;
                    an.this.q = EGL14.EGL_NO_DISPLAY;
                }
            }
        });
    }

    public void a(int i, int i2) {
        this.e = i;
        this.f = i2;
        this.o = EGL14.eglGetCurrentContext();
        if (this.o == EGL14.EGL_NO_CONTEXT) {
            a("eglGetCurrentContext failed");
            return;
        }
        EGLDisplay eglGetCurrentDisplay = EGL14.eglGetCurrentDisplay();
        if (eglGetCurrentDisplay == EGL14.EGL_NO_DISPLAY) {
            a("sharedEglDisplay failed");
            return;
        }
        EGLConfig[] eGLConfigArr = new EGLConfig[1];
        if (!EGL14.eglGetConfigs(eglGetCurrentDisplay, eGLConfigArr, 0, eGLConfigArr.length, new int[1], 0)) {
            a("eglGetConfigs failed");
            return;
        }
        this.p = eGLConfigArr[0];
        if (this.h == null || this.h.isShutdown()) {
            return;
        }
        this.h.execute(new Runnable() { // from class: com.amap.api.mapcore.util.an.1
            @Override // java.lang.Runnable
            public void run() {
                an.this.d();
            }
        });
    }

    public void a(a aVar) {
        this.w = aVar;
    }

    public void a(CrossOverlay.GenerateCrossImageListener generateCrossImageListener) {
        this.x = generateCrossImageListener;
    }

    public void b() {
        this.j = true;
        if (this.v != null) {
            this.v.clear();
            this.v = null;
        }
        if (this.u != null) {
            this.u.clear();
            this.u = null;
        }
        this.w = null;
        this.h.shutdownNow();
    }

    public void b(int i, int i2) {
        this.l = i;
        this.m = i2;
    }

    public boolean c() {
        return this.j;
    }
}
