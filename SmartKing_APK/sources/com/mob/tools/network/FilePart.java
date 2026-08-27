package com.mob.tools.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/* loaded from: classes2.dex */
public class FilePart extends HTTPPart {
    private File file;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.mob.tools.network.HTTPPart
    public InputStream getInputStream() throws Throwable {
        return new FileInputStream(this.file);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.mob.tools.network.HTTPPart
    public long length() throws Throwable {
        return this.file.length();
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setFile(String str) {
        this.file = new File(str);
    }

    public String toString() {
        return this.file.toString();
    }
}
