package com.amap.openapi;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* compiled from: AbstractBuilder.java */
/* loaded from: classes.dex */
public abstract class cf {
    protected ch a;
    private ByteBuffer b;

    /* JADX INFO: Access modifiers changed from: protected */
    public cf(int i) {
        this.b = ByteBuffer.allocate(i);
        this.b.order(ByteOrder.LITTLE_ENDIAN);
        this.a = new ch(this.b);
    }

    public cf a() {
        this.a.a(this.b);
        return this;
    }
}
