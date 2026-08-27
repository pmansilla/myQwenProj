package com.mob.mobapm.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class d extends InputStream {
    private ArrayList<InputStream> a = new ArrayList<>();
    private int b;

    private boolean a() {
        ArrayList<InputStream> arrayList = this.a;
        return arrayList == null || arrayList.size() <= 0;
    }

    public void a(InputStream inputStream) throws Throwable {
        this.a.add(inputStream);
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        if (a()) {
            return 0;
        }
        return this.a.get(this.b).available();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        Iterator<InputStream> it = this.a.iterator();
        while (it.hasNext()) {
            it.next().close();
        }
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (a()) {
            return -1;
        }
        int read = this.a.get(this.b).read();
        while (read < 0) {
            int i = this.b + 1;
            this.b = i;
            if (i >= this.a.size()) {
                break;
            }
            read = this.a.get(this.b).read();
        }
        return read;
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr, int i, int i2) throws IOException {
        if (a()) {
            return -1;
        }
        int read = this.a.get(this.b).read(bArr, i, i2);
        while (read < 0) {
            int i3 = this.b + 1;
            this.b = i3;
            if (i3 >= this.a.size()) {
                break;
            }
            read = this.a.get(this.b).read(bArr, i, i2);
        }
        return read;
    }

    @Override // java.io.InputStream
    public long skip(long j) throws IOException {
        throw new IOException();
    }
}
