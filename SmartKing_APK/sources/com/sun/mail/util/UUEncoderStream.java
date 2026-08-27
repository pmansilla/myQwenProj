package com.sun.mail.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/* loaded from: classes2.dex */
public class UUEncoderStream extends FilterOutputStream {
    private byte[] buffer;
    private int bufsize;
    private int mode;
    private String name;
    private boolean wrotePrefix;
    private boolean wroteSuffix;

    public UUEncoderStream(OutputStream outputStream) {
        this(outputStream, "encoder.buf", 420);
    }

    public UUEncoderStream(OutputStream outputStream, String str) {
        this(outputStream, str, 420);
    }

    public UUEncoderStream(OutputStream outputStream, String str, int i) {
        super(outputStream);
        this.bufsize = 0;
        this.wrotePrefix = false;
        this.wroteSuffix = false;
        this.name = str;
        this.mode = i;
        this.buffer = new byte[45];
    }

    private void encode() throws IOException {
        byte b;
        this.out.write((this.bufsize & 63) + 32);
        int i = 0;
        while (i < this.bufsize) {
            int i2 = i + 1;
            byte b2 = this.buffer[i];
            byte b3 = 1;
            if (i2 < this.bufsize) {
                int i3 = i2 + 1;
                b = this.buffer[i2];
                if (i3 < this.bufsize) {
                    b3 = this.buffer[i3];
                    i2 = i3 + 1;
                } else {
                    i2 = i3;
                }
            } else {
                b = 1;
            }
            int i4 = (b2 >>> 2) & 63;
            int i5 = ((b2 << 4) & 48) | ((b >>> 4) & 15);
            this.out.write(i4 + 32);
            this.out.write(i5 + 32);
            this.out.write((((b << 2) & 60) | ((b3 >>> 6) & 3)) + 32);
            this.out.write((b3 & 63) + 32);
            i = i2;
        }
        this.out.write(10);
    }

    private void writePrefix() throws IOException {
        if (this.wrotePrefix) {
            return;
        }
        PrintStream printStream = new PrintStream(this.out, false, "utf-8");
        printStream.format("begin %o %s%n", Integer.valueOf(this.mode), this.name);
        printStream.flush();
        this.wrotePrefix = true;
    }

    private void writeSuffix() throws IOException {
        if (this.wroteSuffix) {
            return;
        }
        PrintStream printStream = new PrintStream(this.out, false, "us-ascii");
        printStream.println(" \nend");
        printStream.flush();
        this.wroteSuffix = true;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        flush();
        this.out.close();
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        if (this.bufsize > 0) {
            writePrefix();
            encode();
            this.bufsize = 0;
        }
        writeSuffix();
        this.out.flush();
    }

    public void setNameMode(String str, int i) {
        this.name = str;
        this.mode = i;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int i) throws IOException {
        byte[] bArr = this.buffer;
        int i2 = this.bufsize;
        this.bufsize = i2 + 1;
        bArr[i2] = (byte) i;
        if (this.bufsize == 45) {
            writePrefix();
            encode();
            this.bufsize = 0;
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr) throws IOException {
        write(bArr, 0, bArr.length);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i, int i2) throws IOException {
        for (int i3 = 0; i3 < i2; i3++) {
            write(bArr[i + i3]);
        }
    }
}
