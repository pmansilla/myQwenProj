package com.sun.mail.pop3;

import java.io.File;
import java.io.IOException;

/* loaded from: classes2.dex */
class TempFile {
    private File file;
    private WritableSharedFile sf;

    public TempFile(File file) throws IOException {
        this.file = File.createTempFile("pop3.", ".mbox", file);
        this.file.deleteOnExit();
        this.sf = new WritableSharedFile(this.file);
    }

    public void close() {
        try {
            this.sf.close();
        } catch (IOException unused) {
        }
        this.file.delete();
    }

    protected void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    public AppendStream getAppendStream() throws IOException {
        return this.sf.getAppendStream();
    }
}
