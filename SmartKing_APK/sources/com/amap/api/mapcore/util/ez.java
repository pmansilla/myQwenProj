package com.amap.api.mapcore.util;

import android.graphics.Color;
import android.opengl.GLES20;
import android.util.Log;
import android.view.SurfaceHolder;
import com.amap.api.mapcore.util.ef;
import com.autonavi.amap.mapcore.AMapNativeRenderer;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

/* compiled from: GlesUtility.java */
/* loaded from: classes.dex */
public class ez {

    /* compiled from: GlesUtility.java */
    /* loaded from: classes.dex */
    public static class a extends ex {
        private static int g = 4;
        protected int a;
        protected int b;
        protected int c;
        protected int d;
        protected int e;
        protected int f;
        private int[] h = new int[1];

        public a(int i, int i2, int i3, int i4, int i5, int i6) {
            this.a = i;
            this.b = i2;
            this.c = i3;
            this.d = i4;
            this.e = i5;
            this.f = i6;
        }

        private int a(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, int i, int i2) {
            return egl10.eglGetConfigAttrib(eGLDisplay, eGLConfig, i, this.h) ? this.h[0] : i2;
        }

        private c a(EGL10 egl10, EGLDisplay eGLDisplay) {
            c cVar = new c();
            cVar.a = a(2, true);
            egl10.eglChooseConfig(eGLDisplay, cVar.a, null, 0, cVar.b);
            if (cVar.b[0] <= 0) {
                cVar.a = a(2, false);
                egl10.eglChooseConfig(eGLDisplay, cVar.a, null, 0, cVar.b);
                if (cVar.b[0] <= 0) {
                    return null;
                }
            }
            return cVar;
        }

        private int[] a(int i, boolean z) {
            return i == 2 ? new int[]{12324, this.a, 12323, this.b, 12322, this.c, 12321, this.d, 12325, this.e, 12326, this.f, 12338, z ? 1 : 0, 12352, g, 12344} : z ? new int[]{12324, this.a, 12323, this.b, 12322, this.c, 12338, 1, 12344} : new int[]{12324, this.a, 12323, this.b, 12322, this.c, 12344};
        }

        public EGLConfig a(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr) {
            for (EGLConfig eGLConfig : eGLConfigArr) {
                int a = a(egl10, eGLDisplay, eGLConfig, 12325, 0);
                int a2 = a(egl10, eGLDisplay, eGLConfig, 12326, 0);
                if (a >= this.e && a2 >= this.f) {
                    int a3 = a(egl10, eGLDisplay, eGLConfig, 12324, 0);
                    int a4 = a(egl10, eGLDisplay, eGLConfig, 12323, 0);
                    int a5 = a(egl10, eGLDisplay, eGLConfig, 12322, 0);
                    int a6 = a(egl10, eGLDisplay, eGLConfig, 12321, 0);
                    if (a3 == this.a && a4 == this.b && a5 == this.c && a6 == this.d) {
                        return eGLConfig;
                    }
                }
            }
            return null;
        }

        @Override // com.amap.api.mapcore.util.ex, android.opengl.GLSurfaceView.EGLConfigChooser, com.amap.api.mapcore.util.x.e
        public EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay) {
            c a = a(egl10, eGLDisplay);
            if (a == null || a.a == null) {
                return null;
            }
            EGLConfig[] eGLConfigArr = new EGLConfig[a.b[0]];
            egl10.eglChooseConfig(eGLDisplay, a.a, eGLConfigArr, a.b[0], a.b);
            EGLConfig a2 = a(egl10, eGLDisplay, eGLConfigArr);
            if (a2 != null) {
                return a2;
            }
            this.a = 8;
            this.b = 8;
            this.c = 8;
            c a3 = a(egl10, eGLDisplay);
            if (a3 == null || a3.a == null) {
                return a2;
            }
            EGLConfig[] eGLConfigArr2 = new EGLConfig[a3.b[0]];
            egl10.eglChooseConfig(eGLDisplay, a3.a, eGLConfigArr2, a3.b[0], a3.b);
            return a(egl10, eGLDisplay, eGLConfigArr2);
        }
    }

    /* compiled from: GlesUtility.java */
    /* loaded from: classes.dex */
    public static class b extends ey {
        @Override // com.amap.api.mapcore.util.ey, android.opengl.GLSurfaceView.EGLContextFactory, com.amap.api.mapcore.util.x.f
        public EGLContext createContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig) {
            try {
                return egl10.eglCreateContext(eGLDisplay, eGLConfig, EGL10.EGL_NO_CONTEXT, new int[]{12440, 2, 12344});
            } catch (Throwable th) {
                th.printStackTrace();
                return null;
            }
        }

        @Override // com.amap.api.mapcore.util.ey, android.opengl.GLSurfaceView.EGLContextFactory, com.amap.api.mapcore.util.x.f
        public void destroyContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLContext eGLContext) {
            egl10.eglDestroyContext(eGLDisplay, eGLContext);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: GlesUtility.java */
    /* loaded from: classes.dex */
    public static class c {
        public int[] a;
        public int[] b;

        private c() {
            this.a = null;
            this.b = new int[1];
        }
    }

    public static void a(ae aeVar, int i, int i2, int i3, int i4, int i5, int i6) {
        SurfaceHolder holder;
        if (i4 > 0 && (holder = aeVar.getHolder()) != null) {
            holder.setFormat(-3);
        }
        aeVar.a(new b());
        aeVar.a(new a(i, i2, i3, i4, i5, i6));
    }

    private static void a(ef.e eVar, int i, int i2, FloatBuffer floatBuffer, float f, int i3, int i4, float[] fArr) {
        if (f == 0.0f || eVar == null) {
            return;
        }
        eVar.a();
        GLES20.glEnable(3042);
        GLES20.glDisable(2929);
        GLES20.glBlendFunc(770, 771);
        GLES20.glLineWidth(f);
        GLES20.glEnableVertexAttribArray(eVar.b);
        GLES20.glVertexAttribPointer(eVar.b, 3, 5126, false, 0, (Buffer) floatBuffer);
        GLES20.glUniform4fv(eVar.c, 1, new float[]{Color.red(i2) / 255.0f, Color.green(i2) / 255.0f, Color.blue(i2) / 255.0f, Color.alpha(i2) / 255.0f}, 0);
        GLES20.glUniformMatrix4fv(eVar.a, 1, false, fArr, 0);
        GLES20.glDrawArrays(i, i3, i4);
        GLES20.glDisableVertexAttribArray(eVar.b);
        GLES20.glDisable(3042);
        GLES20.glUseProgram(0);
    }

    private static void a(ef.e eVar, int i, int i2, FloatBuffer floatBuffer, float f, int i3, float[] fArr) {
        a(eVar, 2, i2, floatBuffer, f, 1, i3 - 1, fArr);
    }

    public static void a(ef.e eVar, int i, int i2, FloatBuffer floatBuffer, float f, int i3, float[] fArr, float f2, int i4) {
        b(eVar, 6, i, floatBuffer, 1.0f, i3, fArr);
        b(eVar, i, i2, floatBuffer, f, i3, fArr, f2, i4);
    }

    private static void a(ef.e eVar, int i, int i2, FloatBuffer floatBuffer, float f, int i3, float[] fArr, int i4) {
        try {
            float alpha = Color.alpha(i2) / 255.0f;
            float red = Color.red(i2) / 255.0f;
            float green = Color.green(i2) / 255.0f;
            float blue = Color.blue(i2) / 255.0f;
            if (i3 < 3) {
                return;
            }
            int i5 = (i3 - 2) * 3;
            if (floatBuffer != null && floatBuffer.limit() >= i5 + 3) {
                float[] fArr2 = new float[i5];
                for (int i6 = 0; i6 < i5; i6++) {
                    fArr2[i6] = floatBuffer.get(i6 + 3);
                }
                AMapNativeRenderer.nativeDrawLineByTextureID(fArr2, fArr2.length, f, i4, red, green, blue, alpha, 0.0f, true, true, false, fArr, 3, 0);
            }
        } catch (Throwable th) {
            ic.c(th, "GlesUtility", "drawCircleLine");
        }
    }

    public static void a(ef.e eVar, int i, int i2, FloatBuffer floatBuffer, float f, FloatBuffer floatBuffer2, int i3, int i4, float[] fArr) {
        b(eVar, 4, i, floatBuffer2, 1.0f, i4, fArr);
        b(eVar, 2, i2, floatBuffer, f, i3, fArr);
    }

    public static void a(String str) {
        int glGetError = GLES20.glGetError();
        if (glGetError == 0) {
            return;
        }
        Log.e("amap", str + ": glError " + glGetError);
        throw new RuntimeException(str + ": glError " + glGetError);
    }

    private static void b(ef.e eVar, int i, int i2, FloatBuffer floatBuffer, float f, int i3, float[] fArr) {
        a(eVar, i, i2, floatBuffer, f, 0, i3, fArr);
    }

    public static void b(ef.e eVar, int i, int i2, FloatBuffer floatBuffer, float f, int i3, float[] fArr, float f2, int i4) {
        if (i4 != -1) {
            a(eVar, i, i2, floatBuffer, f * f2, i3, fArr, i4);
        } else {
            a(eVar, i, i2, floatBuffer, f, i3, fArr);
        }
    }

    public static void b(ef.e eVar, int i, int i2, FloatBuffer floatBuffer, float f, FloatBuffer floatBuffer2, int i3, int i4, float[] fArr) {
        b(eVar, 2, i2, floatBuffer, f, i3, fArr);
    }
}
