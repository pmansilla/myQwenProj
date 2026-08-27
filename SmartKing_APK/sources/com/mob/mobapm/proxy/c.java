package com.mob.mobapm.proxy;

import com.mob.mobapm.core.Transaction;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class c extends OutputStream {
    private OutputStream a;
    public Transaction b;
    private ByteBuffer c;
    private int d;
    private boolean e = false;

    public c(OutputStream outputStream, Transaction transaction) {
        this.a = outputStream;
        this.b = transaction;
        int i = com.mob.mobapm.core.c.j;
        this.d = i;
        ByteBuffer allocate = ByteBuffer.allocate(i);
        this.c = allocate;
        allocate.clear();
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.a != null) {
            com.mob.mobapm.d.a.a().d("mob---close:", new Object[0]);
            try {
                if (this.c != null) {
                    this.c.flip();
                    int limit = this.c.limit();
                    boolean z = limit > 0;
                    if (!this.e && this.b != null && "POST".equalsIgnoreCase(this.b.getMethod()) && z) {
                        byte[] bArr = new byte[limit];
                        this.c.get(bArr, 0, limit);
                        this.b.setBody(new String(bArr));
                    }
                    this.c.clear();
                }
            } catch (Throwable th) {
                com.mob.mobapm.d.a.a().d(th);
            }
            this.a.close();
        }
        super.close();
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        OutputStream outputStream = this.a;
        if (outputStream != null) {
            outputStream.flush();
        }
    }

    @Override // java.io.OutputStream
    public void write(int i) throws IOException {
        OutputStream outputStream = this.a;
        if (outputStream != null) {
            outputStream.write(i);
        }
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr) throws IOException {
        if (this.a != null) {
            com.mob.mobapm.d.a.a().d("write b[]:", new Object[0]);
            write(bArr, 0, bArr.length);
        }
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr, int i, int i2) throws IOException {
        if (this.a != null) {
            com.mob.mobapm.d.a.a().d("write b[]:int:int", new Object[0]);
            try {
                if (this.c != null) {
                    boolean z = i2 > this.c.remaining();
                    this.e = z;
                    if (!z) {
                        if ("POST".equalsIgnoreCase(this.b.getMethod())) {
                            try {
                                this.c.put(bArr, i, i2);
                            } catch (Throwable unused) {
                            }
                        }
                    }
                }
            } catch (Throwable th) {
                com.mob.mobapm.d.a.a().d(th);
            }
            this.a.write(bArr, i, i2);
        }
    }
}
