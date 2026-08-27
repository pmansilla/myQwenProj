package com.amap.api.mapcore.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLDebugHelper;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

/* compiled from: GLTextureView.java */
@SuppressLint({"NewApi"})
/* loaded from: classes.dex */
public class x extends TextureView implements TextureView.SurfaceTextureListener {
    private static final j a = new j();
    private final WeakReference<x> b;
    private i c;
    private GLSurfaceView.Renderer d;
    private boolean e;
    private e f;
    private f g;
    private g h;
    private k i;
    private int j;
    private int k;
    private boolean l;

    /* compiled from: GLTextureView.java */
    /* loaded from: classes.dex */
    private abstract class a implements e {
        protected int[] a;

        public a(int[] iArr) {
            this.a = a(iArr);
        }

        private int[] a(int[] iArr) {
            if (x.this.k != 2 && x.this.k != 3) {
                return iArr;
            }
            int length = iArr.length;
            int[] iArr2 = new int[length + 2];
            int i = length - 1;
            System.arraycopy(iArr, 0, iArr2, 0, i);
            iArr2[i] = 12352;
            if (x.this.k == 2) {
                iArr2[length] = 4;
            } else {
                iArr2[length] = 64;
            }
            iArr2[length + 1] = 12344;
            return iArr2;
        }

        abstract EGLConfig a(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr);

        @Override // com.amap.api.mapcore.util.x.e
        public EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay) {
            int[] iArr = new int[1];
            if (!egl10.eglChooseConfig(eGLDisplay, this.a, null, 0, iArr)) {
                throw new IllegalArgumentException("eglChooseConfig failed");
            }
            int i = iArr[0];
            if (i <= 0) {
                throw new IllegalArgumentException("No configs match configSpec");
            }
            EGLConfig[] eGLConfigArr = new EGLConfig[i];
            if (!egl10.eglChooseConfig(eGLDisplay, this.a, eGLConfigArr, i, iArr)) {
                throw new IllegalArgumentException("eglChooseConfig#2 failed");
            }
            EGLConfig a = a(egl10, eGLDisplay, eGLConfigArr);
            if (a != null) {
                return a;
            }
            throw new IllegalArgumentException("No config chosen");
        }
    }

    /* compiled from: GLTextureView.java */
    /* loaded from: classes.dex */
    private class b extends a {
        protected int c;
        protected int d;
        protected int e;
        protected int f;
        protected int g;
        protected int h;
        private int[] j;

        public b(int i, int i2, int i3, int i4, int i5, int i6) {
            super(new int[]{12324, i, 12323, i2, 12322, i3, 12321, i4, 12325, i5, 12326, i6, 12344});
            this.j = new int[1];
            this.c = i;
            this.d = i2;
            this.e = i3;
            this.f = i4;
            this.g = i5;
            this.h = i6;
        }

        private int a(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, int i, int i2) {
            return egl10.eglGetConfigAttrib(eGLDisplay, eGLConfig, i, this.j) ? this.j[0] : i2;
        }

        @Override // com.amap.api.mapcore.util.x.a
        public EGLConfig a(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr) {
            for (EGLConfig eGLConfig : eGLConfigArr) {
                int a = a(egl10, eGLDisplay, eGLConfig, 12325, 0);
                int a2 = a(egl10, eGLDisplay, eGLConfig, 12326, 0);
                if (a >= this.g && a2 >= this.h) {
                    int a3 = a(egl10, eGLDisplay, eGLConfig, 12324, 0);
                    int a4 = a(egl10, eGLDisplay, eGLConfig, 12323, 0);
                    int a5 = a(egl10, eGLDisplay, eGLConfig, 12322, 0);
                    int a6 = a(egl10, eGLDisplay, eGLConfig, 12321, 0);
                    if (a3 == this.c && a4 == this.d && a5 == this.e && a6 == this.f) {
                        return eGLConfig;
                    }
                }
            }
            return null;
        }
    }

    /* compiled from: GLTextureView.java */
    /* loaded from: classes.dex */
    private class c implements f {
        private c() {
        }

        @Override // com.amap.api.mapcore.util.x.f
        public EGLContext createContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig) {
            int[] iArr = {12440, x.this.k, 12344};
            EGLContext eGLContext = EGL10.EGL_NO_CONTEXT;
            if (x.this.k == 0) {
                iArr = null;
            }
            return egl10.eglCreateContext(eGLDisplay, eGLConfig, eGLContext, iArr);
        }

        @Override // com.amap.api.mapcore.util.x.f
        public void destroyContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLContext eGLContext) {
            if (egl10.eglDestroyContext(eGLDisplay, eGLContext)) {
                return;
            }
            Log.e("DefaultContextFactory", "display:" + eGLDisplay + " context: " + eGLContext);
            h.a("eglDestroyContex", egl10.eglGetError());
        }
    }

    /* compiled from: GLTextureView.java */
    /* loaded from: classes.dex */
    private static class d implements g {
        private d() {
        }

        @Override // com.amap.api.mapcore.util.x.g
        public EGLSurface a(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, Object obj) {
            try {
                return egl10.eglCreateWindowSurface(eGLDisplay, eGLConfig, obj, null);
            } catch (IllegalArgumentException e) {
                Log.e("GLSurfaceView", "eglCreateWindowSurface", e);
                return null;
            }
        }

        @Override // com.amap.api.mapcore.util.x.g
        public void a(EGL10 egl10, EGLDisplay eGLDisplay, EGLSurface eGLSurface) {
            egl10.eglDestroySurface(eGLDisplay, eGLSurface);
        }
    }

    /* compiled from: GLTextureView.java */
    /* loaded from: classes.dex */
    public interface e {
        EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay);
    }

    /* compiled from: GLTextureView.java */
    /* loaded from: classes.dex */
    public interface f {
        EGLContext createContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig);

        void destroyContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLContext eGLContext);
    }

    /* compiled from: GLTextureView.java */
    /* loaded from: classes.dex */
    public interface g {
        EGLSurface a(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, Object obj);

        void a(EGL10 egl10, EGLDisplay eGLDisplay, EGLSurface eGLSurface);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: GLTextureView.java */
    /* loaded from: classes.dex */
    public static class h {
        EGL10 a;
        EGLDisplay b;
        EGLSurface c;
        EGLConfig d;
        EGLContext e;
        private WeakReference<x> f;

        public h(WeakReference<x> weakReference) {
            this.f = weakReference;
        }

        private void a(String str) {
            a(str, this.a.eglGetError());
        }

        public static void a(String str, int i) {
            throw new RuntimeException(b(str, i));
        }

        public static void a(String str, String str2, int i) {
            Log.w(str, b(str2, i));
        }

        public static String b(String str, int i) {
            return str + " failed: " + i;
        }

        private void g() {
            if (this.c == null || this.c == EGL10.EGL_NO_SURFACE) {
                return;
            }
            this.a.eglMakeCurrent(this.b, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
            x xVar = this.f.get();
            if (xVar != null) {
                xVar.h.a(this.a, this.b, this.c);
            }
            this.c = null;
        }

        public void a() {
            this.a = (EGL10) EGLContext.getEGL();
            this.b = this.a.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            if (this.b == EGL10.EGL_NO_DISPLAY) {
                throw new RuntimeException("eglGetDisplay failed");
            }
            if (!this.a.eglInitialize(this.b, new int[2])) {
                throw new RuntimeException("eglInitialize failed");
            }
            x xVar = this.f.get();
            if (xVar == null) {
                this.d = null;
                this.e = null;
            } else {
                this.d = xVar.f.chooseConfig(this.a, this.b);
                this.e = xVar.g.createContext(this.a, this.b, this.d);
            }
            if (this.e == null || this.e == EGL10.EGL_NO_CONTEXT) {
                this.e = null;
                a("createContext");
            }
            this.c = null;
        }

        public boolean b() {
            if (this.a == null) {
                throw new RuntimeException("egl not initialized");
            }
            if (this.b == null) {
                throw new RuntimeException("eglDisplay not initialized");
            }
            if (this.d == null) {
                throw new RuntimeException("mEglConfig not initialized");
            }
            g();
            x xVar = this.f.get();
            if (xVar != null) {
                this.c = xVar.h.a(this.a, this.b, this.d, xVar.getSurfaceTexture());
            } else {
                this.c = null;
            }
            if (this.c == null || this.c == EGL10.EGL_NO_SURFACE) {
                if (this.a.eglGetError() == 12299) {
                    Log.e("EglHelper", "createWindowSurface returned EGL_BAD_NATIVE_WINDOW.");
                }
                return false;
            }
            if (this.a.eglMakeCurrent(this.b, this.c, this.c, this.e)) {
                return true;
            }
            a("EGLHelper", "eglMakeCurrent", this.a.eglGetError());
            return false;
        }

        GL c() {
            GL gl = this.e.getGL();
            x xVar = this.f.get();
            if (xVar == null) {
                return gl;
            }
            if (xVar.i != null) {
                gl = xVar.i.a(gl);
            }
            if ((xVar.j & 3) != 0) {
                return GLDebugHelper.wrap(gl, (xVar.j & 1) != 0 ? 1 : 0, (xVar.j & 2) != 0 ? new l() : null);
            }
            return gl;
        }

        public int d() {
            if (this.a.eglSwapBuffers(this.b, this.c)) {
                return 12288;
            }
            return this.a.eglGetError();
        }

        public void e() {
            g();
        }

        public void f() {
            if (this.e != null) {
                x xVar = this.f.get();
                if (xVar != null) {
                    xVar.g.destroyContext(this.a, this.b, this.e);
                }
                this.e = null;
            }
            if (this.b != null) {
                this.a.eglTerminate(this.b);
                this.b = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: GLTextureView.java */
    /* loaded from: classes.dex */
    public static class i extends Thread {
        private boolean a;
        private boolean b;
        private boolean c;
        private boolean d;
        private boolean e;
        private boolean f;
        private boolean g;
        private boolean h;
        private boolean i;
        private boolean j;
        private boolean k;
        private boolean p;
        private h s;
        private WeakReference<x> t;
        private ArrayList<Runnable> q = new ArrayList<>();
        private boolean r = true;
        private int l = 0;
        private int m = 0;
        private boolean o = true;
        private int n = 1;

        i(WeakReference<x> weakReference) {
            this.t = weakReference;
        }

        private void j() {
            if (this.i) {
                this.i = false;
                this.s.e();
            }
        }

        private void k() {
            if (this.h) {
                this.s.f();
                this.h = false;
                x.a.c(this);
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:175:0x0229 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private void l() throws java.lang.InterruptedException {
            /*
                Method dump skipped, instructions count: 564
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.x.i.l():void");
        }

        private boolean m() {
            return !this.d && this.e && !this.f && this.l > 0 && this.m > 0 && (this.o || this.n == 1);
        }

        public void a(int i) {
            if (i < 0 || i > 1) {
                throw new IllegalArgumentException("renderMode");
            }
            synchronized (x.a) {
                this.n = i;
                x.a.notifyAll();
            }
        }

        public void a(int i, int i2) {
            synchronized (x.a) {
                this.l = i;
                this.m = i2;
                this.r = true;
                this.o = true;
                this.p = false;
                x.a.notifyAll();
                while (!this.b && !this.d && !this.p && a()) {
                    try {
                        x.a.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void a(Runnable runnable) {
            if (runnable == null) {
                throw new IllegalArgumentException("r must not be null");
            }
            synchronized (x.a) {
                this.q.add(runnable);
                x.a.notifyAll();
            }
        }

        public boolean a() {
            return this.h && this.i && m();
        }

        public int b() {
            int i;
            synchronized (x.a) {
                i = this.n;
            }
            return i;
        }

        public void c() {
            synchronized (x.a) {
                this.o = true;
                x.a.notifyAll();
            }
        }

        public void d() {
            synchronized (x.a) {
                this.e = true;
                this.j = false;
                x.a.notifyAll();
                while (this.g && !this.j && !this.b) {
                    try {
                        x.a.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void e() {
            synchronized (x.a) {
                this.e = false;
                x.a.notifyAll();
                while (!this.g && !this.b) {
                    try {
                        x.a.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void f() {
            synchronized (x.a) {
                this.c = true;
                x.a.notifyAll();
                while (!this.b && !this.d) {
                    try {
                        x.a.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void g() {
            synchronized (x.a) {
                this.c = false;
                this.o = true;
                this.p = false;
                x.a.notifyAll();
                while (!this.b && this.d && !this.p) {
                    try {
                        x.a.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void h() {
            synchronized (x.a) {
                this.a = true;
                x.a.notifyAll();
                while (!this.b) {
                    try {
                        x.a.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void i() {
            this.k = true;
            x.a.notifyAll();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            setName("GLThread " + getId());
            try {
                l();
            } catch (InterruptedException unused) {
            } catch (Throwable th) {
                x.a.a(this);
                throw th;
            }
            x.a.a(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: GLTextureView.java */
    /* loaded from: classes.dex */
    public static class j {
        private static String a = "GLThreadManager";
        private boolean b;
        private int c;
        private boolean d;
        private boolean e;
        private boolean f;
        private i g;

        private j() {
        }

        private void c() {
            if (this.b) {
                return;
            }
            this.c = 131072;
            if (this.c >= 131072) {
                this.e = true;
            }
            this.b = true;
        }

        public synchronized void a(i iVar) {
            iVar.b = true;
            if (this.g == iVar) {
                this.g = null;
            }
            notifyAll();
        }

        public synchronized void a(GL10 gl10) {
            if (!this.d && gl10 != null) {
                c();
                String glGetString = gl10.glGetString(7937);
                if (this.c < 131072) {
                    this.e = !glGetString.startsWith("Q3Dimension MSM7500 ");
                    notifyAll();
                }
                this.f = !this.e;
                this.d = true;
            }
        }

        public synchronized boolean a() {
            return this.f;
        }

        public synchronized boolean b() {
            c();
            return !this.e;
        }

        public boolean b(i iVar) {
            if (this.g == iVar || this.g == null) {
                this.g = iVar;
                notifyAll();
                return true;
            }
            c();
            if (this.e) {
                return true;
            }
            if (this.g == null) {
                return false;
            }
            this.g.i();
            return false;
        }

        public void c(i iVar) {
            if (this.g == iVar) {
                this.g = null;
            }
            notifyAll();
        }
    }

    /* compiled from: GLTextureView.java */
    /* loaded from: classes.dex */
    public interface k {
        GL a(GL gl);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: GLTextureView.java */
    /* loaded from: classes.dex */
    public static class l extends Writer {
        private StringBuilder a = new StringBuilder();

        l() {
        }

        private void a() {
            if (this.a.length() > 0) {
                Log.v("GLSurfaceView", this.a.toString());
                this.a.delete(0, this.a.length());
            }
        }

        @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            a();
        }

        @Override // java.io.Writer, java.io.Flushable
        public void flush() {
            a();
        }

        @Override // java.io.Writer
        public void write(char[] cArr, int i, int i2) {
            for (int i3 = 0; i3 < i2; i3++) {
                char c = cArr[i + i3];
                if (c == '\n') {
                    a();
                } else {
                    this.a.append(c);
                }
            }
        }
    }

    /* compiled from: GLTextureView.java */
    /* loaded from: classes.dex */
    private class m extends b {
        public m(boolean z) {
            super(8, 8, 8, 0, z ? 16 : 0, 0);
        }
    }

    public x(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.b = new WeakReference<>(this);
        a();
    }

    private void a() {
        setSurfaceTextureListener(this);
    }

    private void b() {
        if (this.c != null) {
            throw new IllegalStateException("setRenderer has already been called for this instance.");
        }
    }

    public void a(e eVar) {
        b();
        this.f = eVar;
    }

    public void a(f fVar) {
        b();
        this.g = fVar;
    }

    public void c() {
        this.c.f();
    }

    public void d() {
        this.c.g();
    }

    protected void finalize() throws Throwable {
        try {
            if (this.c != null) {
                this.c.h();
            }
        } finally {
            super.finalize();
        }
    }

    public int getRenderMode() {
        return this.c.b();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.TextureView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.e && this.d != null) {
            int b2 = this.c != null ? this.c.b() : 1;
            this.c = new i(this.b);
            if (b2 != 1) {
                this.c.a(b2);
            }
            this.c.start();
        }
        this.e = false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onDetachedFromWindow() {
        if (this.c != null) {
            this.c.h();
        }
        this.e = true;
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i2, int i3, int i4, int i5) {
        onSurfaceTextureSizeChanged(getSurfaceTexture(), i4 - i2, i5 - i3);
        super.onLayout(z, i2, i3, i4, i5);
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i2, int i3) {
        this.c.d();
        onSurfaceTextureSizeChanged(surfaceTexture, i2, i3);
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        this.c.e();
        return true;
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i2, int i3) {
        this.c.a(i2, i3);
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    public void queueEvent(Runnable runnable) {
        this.c.a(runnable);
    }

    public void requestRender() {
        this.c.c();
    }

    public void setRenderMode(int i2) {
        this.c.a(i2);
    }

    public void setRenderer(GLSurfaceView.Renderer renderer) {
        b();
        if (this.f == null) {
            this.f = new m(true);
        }
        if (this.g == null) {
            this.g = new c();
        }
        if (this.h == null) {
            this.h = new d();
        }
        this.d = renderer;
        this.c = new i(this.b);
        this.c.start();
    }
}
