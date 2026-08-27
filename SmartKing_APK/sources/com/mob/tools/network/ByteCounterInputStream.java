package com.mob.tools.network;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes2.dex */
public class ByteCounterInputStream extends InputStream {
    private InputStream is;
    private OnReadListener listener;
    private long readBytes;

    public ByteCounterInputStream(InputStream inputStream) {
        this.is = inputStream;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        return this.is.available();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.is.close();
    }

    @Override // java.io.InputStream
    public void mark(int i) {
        this.is.mark(i);
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return this.is.markSupported();
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        int read = this.is.read();
        if (read >= 0) {
            this.readBytes++;
            if (this.listener != null) {
                this.listener.onRead(this.readBytes);
            }
        }
        return read;
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr, int i, int i2) throws IOException {
        int read = this.is.read(bArr, i, i2);
        if (read > 0) {
            this.readBytes += read;
            if (this.listener != null) {
                this.listener.onRead(this.readBytes);
            }
        }
        return read;
    }

    @Override // java.io.InputStream
    public synchronized void reset() throws IOException {
        this.is.reset();
        this.readBytes = 0L;
    }

    public void setOnInputStreamReadListener(OnReadListener onReadListener) {
        this.listener = onReadListener;
    }

    @Override // java.io.InputStream
    public long skip(long j) throws IOException {
        return this.is.skip(j);
    }
}
