package com.loc;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/* compiled from: Table.java */
/* loaded from: classes.dex */
public class fd {
    protected int b;
    protected ByteBuffer c;
    private static final ThreadLocal<CharsetDecoder> d = new ThreadLocal<CharsetDecoder>() { // from class: com.loc.fd.1
        @Override // java.lang.ThreadLocal
        protected final /* synthetic */ CharsetDecoder initialValue() {
            return Charset.forName("UTF-8").newDecoder();
        }
    };
    public static final ThreadLocal<Charset> a = new ThreadLocal<Charset>() { // from class: com.loc.fd.2
        @Override // java.lang.ThreadLocal
        protected final /* synthetic */ Charset initialValue() {
            return Charset.forName("UTF-8");
        }
    };
    private static final ThreadLocal<CharBuffer> e = new ThreadLocal<>();

    protected int a(Integer num, Integer num2, ByteBuffer byteBuffer) {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int c(int i) {
        int i2 = this.b - this.c.getInt(this.b);
        if (i < this.c.getShort(i2)) {
            return this.c.getShort(i2 + i);
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int d(int i) {
        return i + this.c.getInt(i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int e(int i) {
        int i2 = i + this.b;
        return this.c.getInt(i2 + this.c.getInt(i2));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int f(int i) {
        int i2 = i + this.b;
        return i2 + this.c.getInt(i2) + 4;
    }
}
