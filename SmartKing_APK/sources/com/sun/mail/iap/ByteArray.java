package com.sun.mail.iap;

import java.io.ByteArrayInputStream;

/* loaded from: classes2.dex */
public class ByteArray {
    private byte[] bytes;
    private int count;
    private int start;

    public ByteArray(int i) {
        this(new byte[i], 0, i);
    }

    public ByteArray(byte[] bArr, int i, int i2) {
        this.bytes = bArr;
        this.start = i;
        this.count = i2;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public int getCount() {
        return this.count;
    }

    public byte[] getNewBytes() {
        byte[] bArr = new byte[this.count];
        System.arraycopy(this.bytes, this.start, bArr, 0, this.count);
        return bArr;
    }

    public int getStart() {
        return this.start;
    }

    public void grow(int i) {
        byte[] bArr = new byte[this.bytes.length + i];
        System.arraycopy(this.bytes, 0, bArr, 0, this.bytes.length);
        this.bytes = bArr;
    }

    public void setCount(int i) {
        this.count = i;
    }

    public ByteArrayInputStream toByteArrayInputStream() {
        return new ByteArrayInputStream(this.bytes, this.start, this.count);
    }
}
