package com.amap.api.mapcore.util;

import android.opengl.GLES20;
import android.opengl.Matrix;
import com.amap.api.mapcore.util.ef;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* compiled from: GlModelCore.java */
/* loaded from: classes.dex */
public class aa {
    List<Float> a;
    List<Float> b;
    private FloatBuffer c;
    private FloatBuffer d;
    private int e = 0;
    private float f;
    private float g;
    private float h;

    public aa(List<Float> list, List<Float> list2) {
        this.a = new ArrayList();
        this.b = new ArrayList();
        this.g = 0.0f;
        this.h = 0.0f;
        this.a = list;
        this.b = list2;
        if (this.c == null) {
            ByteBuffer allocateDirect = ByteBuffer.allocateDirect(list.size() * 4);
            allocateDirect.order(ByteOrder.nativeOrder());
            this.c = allocateDirect.asFloatBuffer();
        }
        this.c.clear();
        int i = 1;
        float f = 0.0f;
        float f2 = 1000000.0f;
        float f3 = 0.0f;
        float f4 = 1000000.0f;
        float f5 = 0.0f;
        for (int i2 = 1; i2 < list.size() + 1; i2++) {
            Float f6 = list.get(i2 - 1);
            this.c.put(f6.floatValue());
            if (i == 1) {
                f = Math.max(f6.floatValue(), f);
                f2 = Math.min(f6.floatValue(), f2);
            }
            if (i == 2) {
                f3 = Math.max(f6.floatValue(), f3);
                f4 = Math.min(f6.floatValue(), f4);
            }
            if (i == 3) {
                f5 = Math.max(f5, f6.floatValue());
                i = 0;
            }
            i++;
        }
        float abs = Math.abs(f - f2);
        float abs2 = Math.abs(f3 - f4);
        this.g = abs > abs2 ? abs : abs2;
        this.h = abs > abs2 ? abs2 : abs;
        this.c.position(0);
        if (this.d == null) {
            ByteBuffer allocateDirect2 = ByteBuffer.allocateDirect(list2.size() * 4);
            allocateDirect2.order(ByteOrder.nativeOrder());
            this.d = allocateDirect2.asFloatBuffer();
        }
        this.d.clear();
        Iterator<Float> it = list2.iterator();
        while (it.hasNext()) {
            this.d.put(it.next().floatValue());
        }
        this.d.position(0);
    }

    public float a() {
        return this.g;
    }

    public void a(float f) {
        this.f = -f;
    }

    public void a(int i) {
        this.e = i;
    }

    public void a(ef.b bVar, float[] fArr) {
        Matrix.rotateM(fArr, 0, this.f, 0.0f, 0.0f, 1.0f);
        GLES20.glUseProgram(bVar.d);
        GLES20.glClear(256);
        GLES20.glEnable(2929);
        GLES20.glDepthMask(true);
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(770, 771);
        GLES20.glBlendColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glBindTexture(3553, this.e);
        GLES20.glEnableVertexAttribArray(bVar.h);
        GLES20.glVertexAttribPointer(bVar.h, 2, 5126, false, 8, (Buffer) this.d);
        GLES20.glEnableVertexAttribArray(bVar.c);
        GLES20.glVertexAttribPointer(bVar.c, 3, 5126, false, 12, (Buffer) this.c);
        GLES20.glUniformMatrix4fv(bVar.g, 1, false, fArr, 0);
        GLES20.glDrawArrays(4, 0, this.a.size() / 3);
        GLES20.glBindTexture(3553, 0);
        GLES20.glDisable(2929);
        GLES20.glDisableVertexAttribArray(bVar.c);
        GLES20.glDisableVertexAttribArray(bVar.h);
        GLES20.glUseProgram(0);
    }

    public float b() {
        return this.h;
    }

    public void c() {
        this.a.clear();
        this.d.clear();
    }
}
