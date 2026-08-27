package com.amap.openapi;

import com.loc.fc;
import java.nio.ByteBuffer;

/* compiled from: RobustFlatBufferBuilder.java */
/* loaded from: classes.dex */
public class ch extends fc {
    public ch() {
        this(1024);
    }

    public ch(int i) {
        super(i);
    }

    public ch(ByteBuffer byteBuffer) {
        super(byteBuffer);
    }

    @Override // com.loc.fc
    public int a(CharSequence charSequence) {
        try {
            return super.a(charSequence);
        } catch (Throwable unused) {
            return super.a("");
        }
    }
}
