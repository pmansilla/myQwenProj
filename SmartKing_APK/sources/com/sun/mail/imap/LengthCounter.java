package com.sun.mail.imap;

import java.io.IOException;
import java.io.OutputStream;

/* compiled from: IMAPFolder.java */
/* loaded from: classes2.dex */
class LengthCounter extends OutputStream {
    private int maxsize;
    private int size = 0;
    private byte[] buf = new byte[8192];

    public LengthCounter(int i) {
        this.maxsize = i;
    }

    public byte[] getBytes() {
        return this.buf;
    }

    public int getSize() {
        return this.size;
    }

    @Override // java.io.OutputStream
    public void write(int i) {
        int i2 = this.size + 1;
        if (this.buf != null) {
            if (i2 > this.maxsize && this.maxsize >= 0) {
                this.buf = null;
            } else if (i2 > this.buf.length) {
                byte[] bArr = new byte[Math.max(this.buf.length << 1, i2)];
                System.arraycopy(this.buf, 0, bArr, 0, this.size);
                this.buf = bArr;
                this.buf[this.size] = (byte) i;
            } else {
                this.buf[this.size] = (byte) i;
            }
        }
        this.size = i2;
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr) throws IOException {
        write(bArr, 0, bArr.length);
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr, int i, int i2) {
        int i3;
        if (i < 0 || i > bArr.length || i2 < 0 || (i3 = i + i2) > bArr.length || i3 < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (i2 == 0) {
            return;
        }
        int i4 = this.size + i2;
        if (this.buf != null) {
            if (i4 > this.maxsize && this.maxsize >= 0) {
                this.buf = null;
            } else if (i4 > this.buf.length) {
                byte[] bArr2 = new byte[Math.max(this.buf.length << 1, i4)];
                System.arraycopy(this.buf, 0, bArr2, 0, this.size);
                this.buf = bArr2;
                System.arraycopy(bArr, i, this.buf, this.size, i2);
            } else {
                System.arraycopy(bArr, i, this.buf, this.size, i2);
            }
        }
        this.size = i4;
    }
}
