package com.loc;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.util.Arrays;

/* compiled from: FlatBufferBuilder.java */
/* loaded from: classes.dex */
public class fc {
    ByteBuffer a;
    int b;
    int d;
    int[] e;
    int f;
    boolean g;
    boolean h;
    int i;
    int[] j;
    int k;
    int l;
    boolean m;
    CharsetEncoder n;
    ByteBuffer o;
    static final /* synthetic */ boolean p = !fc.class.desiredAssertionStatus();
    static final Charset c = Charset.forName("UTF-8");

    public fc() {
        this(1024);
    }

    public fc(int i) {
        this.d = 1;
        this.e = null;
        this.f = 0;
        this.g = false;
        this.h = false;
        this.j = new int[16];
        this.k = 0;
        this.l = 0;
        this.m = false;
        this.n = c.newEncoder();
        i = i <= 0 ? 1 : i;
        this.b = i;
        this.a = a(i);
    }

    public fc(ByteBuffer byteBuffer) {
        this.d = 1;
        this.e = null;
        this.f = 0;
        this.g = false;
        this.h = false;
        this.j = new int[16];
        this.k = 0;
        this.l = 0;
        this.m = false;
        this.n = c.newEncoder();
        a(byteBuffer);
    }

    static ByteBuffer a(int i) {
        ByteBuffer allocate = ByteBuffer.allocate(i);
        allocate.order(ByteOrder.LITTLE_ENDIAN);
        return allocate;
    }

    static ByteBuffer b(ByteBuffer byteBuffer) {
        int capacity = byteBuffer.capacity();
        if (((-1073741824) & capacity) != 0) {
            throw new AssertionError("FlatBuffers: cannot grow buffer beyond 2 gigabytes.");
        }
        int i = capacity << 1;
        byteBuffer.position(0);
        ByteBuffer a = a(i);
        a.position(i - capacity);
        a.put(byteBuffer);
        return a;
    }

    public int a() {
        return this.a.capacity() - this.b;
    }

    public int a(CharSequence charSequence) {
        int length = (int) (charSequence.length() * this.n.maxBytesPerChar());
        if (this.o == null || this.o.capacity() < length) {
            this.o = ByteBuffer.allocate(Math.max(128, length));
        }
        this.o.clear();
        CoderResult encode = this.n.encode(charSequence instanceof CharBuffer ? (CharBuffer) charSequence : CharBuffer.wrap(charSequence), this.o, true);
        if (encode.isError()) {
            try {
                encode.throwException();
            } catch (CharacterCodingException e) {
                throw new Error(e);
            }
        }
        this.o.flip();
        return c(this.o);
    }

    public fc a(ByteBuffer byteBuffer) {
        this.a = byteBuffer;
        this.a.clear();
        this.a.order(ByteOrder.LITTLE_ENDIAN);
        this.d = 1;
        this.b = this.a.capacity();
        this.f = 0;
        this.g = false;
        this.h = false;
        this.i = 0;
        this.k = 0;
        this.l = 0;
        return this;
    }

    public void a(byte b) {
        ByteBuffer byteBuffer = this.a;
        int i = this.b - 1;
        this.b = i;
        byteBuffer.put(i, b);
    }

    public void a(int i, byte b, int i2) {
        if (this.m || b != i2) {
            b(b);
            g(i);
        }
    }

    public void a(int i, int i2) {
        if (i > this.d) {
            this.d = i;
        }
        int capacity = ((((this.a.capacity() - this.b) + i2) ^ (-1)) + 1) & (i - 1);
        while (this.b < capacity + i + i2) {
            int capacity2 = this.a.capacity();
            this.a = b(this.a);
            this.b += this.a.capacity() - capacity2;
        }
        b(capacity);
    }

    public void a(int i, int i2, int i3) {
        d();
        this.l = i2;
        int i4 = i * i2;
        a(4, i4);
        a(i3, i4);
        this.g = true;
    }

    public void a(int i, long j, long j2) {
        if (this.m || j != j2) {
            b(j);
            g(i);
        }
    }

    public void a(int i, short s, int i2) {
        if (this.m || s != i2) {
            b(s);
            g(i);
        }
    }

    public void a(int i, boolean z, boolean z2) {
        if (this.m || z != z2) {
            b(z);
            g(i);
        }
    }

    public void a(long j) {
        ByteBuffer byteBuffer = this.a;
        int i = this.b - 8;
        this.b = i;
        byteBuffer.putLong(i, j);
    }

    public void a(short s) {
        ByteBuffer byteBuffer = this.a;
        int i = this.b - 2;
        this.b = i;
        byteBuffer.putShort(i, s);
    }

    public void a(boolean z) {
        ByteBuffer byteBuffer = this.a;
        int i = this.b - 1;
        this.b = i;
        byteBuffer.put(i, z ? (byte) 1 : (byte) 0);
    }

    public int b() {
        if (!this.g) {
            throw new AssertionError("FlatBuffers: endVector called without startVector");
        }
        this.g = false;
        c(this.l);
        return a();
    }

    public void b(byte b) {
        a(1, 0);
        a(b);
    }

    public void b(int i) {
        for (int i2 = 0; i2 < i; i2++) {
            ByteBuffer byteBuffer = this.a;
            int i3 = this.b - 1;
            this.b = i3;
            byteBuffer.put(i3, (byte) 0);
        }
    }

    public void b(int i, int i2, int i3) {
        if (this.m || i2 != i3) {
            d(i2);
            g(i);
        }
    }

    public void b(long j) {
        a(8, 0);
        a(j);
    }

    public void b(short s) {
        a(2, 0);
        a(s);
    }

    public void b(boolean z) {
        a(1, 0);
        a(z);
    }

    public byte[] b(int i, int i2) {
        c();
        byte[] bArr = new byte[i2];
        this.a.position(i);
        this.a.get(bArr);
        return bArr;
    }

    public int c(ByteBuffer byteBuffer) {
        int remaining = byteBuffer.remaining();
        b((byte) 0);
        a(1, remaining, 1);
        ByteBuffer byteBuffer2 = this.a;
        int i = this.b - remaining;
        this.b = i;
        byteBuffer2.position(i);
        this.a.put(byteBuffer);
        return b();
    }

    public void c() {
        if (!this.h) {
            throw new AssertionError("FlatBuffers: you can only access the serialized buffer after it has been finished by FlatBufferBuilder.finish().");
        }
    }

    public void c(int i) {
        ByteBuffer byteBuffer = this.a;
        int i2 = this.b - 4;
        this.b = i2;
        byteBuffer.putInt(i2, i);
    }

    public void c(int i, int i2, int i3) {
        if (this.m || i2 != i3) {
            e(i2);
            g(i);
        }
    }

    public void d() {
        if (this.g) {
            throw new AssertionError("FlatBuffers: object serialization must not be nested.");
        }
    }

    public void d(int i) {
        a(4, 0);
        c(i);
    }

    public int e() {
        int i;
        int i2;
        if (this.e == null || !this.g) {
            throw new AssertionError("FlatBuffers: endObject called without startObject");
        }
        d(0);
        int a = a();
        for (int i3 = this.f - 1; i3 >= 0; i3--) {
            b((short) (this.e[i3] != 0 ? a - this.e[i3] : 0));
        }
        b((short) (a - this.i));
        b((short) ((this.f + 2) * 2));
        int i4 = 0;
        loop1: while (true) {
            if (i4 >= this.k) {
                i = 0;
                break;
            }
            int capacity = this.a.capacity() - this.j[i4];
            int i5 = this.b;
            short s = this.a.getShort(capacity);
            if (s == this.a.getShort(i5)) {
                while (i2 < s) {
                    i2 = this.a.getShort(capacity + i2) == this.a.getShort(i5 + i2) ? i2 + 2 : 2;
                }
                i = this.j[i4];
                break loop1;
            }
            i4++;
        }
        if (i != 0) {
            this.b = this.a.capacity() - a;
            this.a.putInt(this.b, i - a);
        } else {
            if (this.k == this.j.length) {
                this.j = Arrays.copyOf(this.j, this.k * 2);
            }
            int[] iArr = this.j;
            int i6 = this.k;
            this.k = i6 + 1;
            iArr[i6] = a();
            this.a.putInt(this.a.capacity() - a, a() - a);
        }
        this.g = false;
        return a;
    }

    public void e(int i) {
        a(4, 0);
        if (!p && i > a()) {
            throw new AssertionError();
        }
        c((a() - i) + 4);
    }

    public void f(int i) {
        d();
        if (this.e == null || this.e.length < i) {
            this.e = new int[i];
        }
        this.f = i;
        Arrays.fill(this.e, 0, this.f, 0);
        this.g = true;
        this.i = a();
    }

    public byte[] f() {
        return b(this.b, this.a.capacity() - this.b);
    }

    public void g(int i) {
        this.e[i] = a();
    }

    public void h(int i) {
        a(this.d, 4);
        e(i);
        this.a.position(this.b);
        this.h = true;
    }
}
