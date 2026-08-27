package com.amap.api.mapcore.util;

import com.autonavi.amap.mapcore.animation.GLAlphaAnimation;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/* compiled from: MaskLayer.java */
/* loaded from: classes.dex */
public class ak {
    public FloatBuffer a;
    public ShortBuffer b;
    private GLAlphaAnimation i;
    private float e = 0.0f;
    private float f = 0.0f;
    private float g = 0.0f;
    private float h = 0.7f;
    float[] c = {-1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};
    short[] d = {0, 1, 3, 0, 3, 2};

    public ak() {
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(this.d.length * 2);
        allocateDirect.order(ByteOrder.nativeOrder());
        this.b = allocateDirect.asShortBuffer();
        this.b.put(this.d);
        this.b.position(0);
        ByteBuffer allocateDirect2 = ByteBuffer.allocateDirect(this.c.length * 4);
        allocateDirect2.order(ByteOrder.nativeOrder());
        this.a = allocateDirect2.asFloatBuffer();
        this.a.put(this.c);
        this.a.position(0);
    }

    public void a(int i, int i2, int i3, int i4) {
        this.e = i / 255.0f;
        this.f = i2 / 255.0f;
        this.g = i3 / 255.0f;
        this.h = i4 / 255.0f;
    }

    public void a(GLAlphaAnimation gLAlphaAnimation) {
        if (this.i != null && !this.i.hasEnded()) {
            this.i.cancel();
        }
        if (gLAlphaAnimation == null) {
            return;
        }
        this.i = gLAlphaAnimation;
        this.i.start();
    }
}
