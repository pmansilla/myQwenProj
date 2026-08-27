package com.amap.api.mapcore.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/* compiled from: FileAccessI.java */
/* loaded from: classes.dex */
class cf {
    RandomAccessFile a;

    public cf() throws IOException {
        this("", 0L);
    }

    public cf(String str, long j) throws IOException {
        File file = new File(str);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
            } catch (IOException e) {
                ic.c(e, "FileAccessI", "create");
                e.printStackTrace();
            }
        }
        this.a = new RandomAccessFile(str, "rw");
        this.a.seek(j);
    }

    public synchronized int a(byte[] bArr) throws IOException {
        this.a.write(bArr);
        return bArr.length;
    }

    public void a() {
        if (this.a != null) {
            try {
                this.a.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.a = null;
        }
    }
}
