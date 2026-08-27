package com.amap.openapi;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* compiled from: AbstractBuilder.java */
/* loaded from: classes.dex */
public abstract class g {
    protected i a;
    private ByteBuffer b;

    /* JADX INFO: Access modifiers changed from: protected */
    public g(int i) {
        this.b = ByteBuffer.allocate(i);
        this.b.order(ByteOrder.LITTLE_ENDIAN);
        this.a = new i(this.b);
    }

    public g a() {
        this.a.a(this.b);
        return this;
    }
}
