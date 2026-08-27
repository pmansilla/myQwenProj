package com.mob.tools.network;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/* loaded from: classes2.dex */
public class StringPart extends HTTPPart {
    private StringBuilder sb = new StringBuilder();

    public StringPart append(String str) {
        this.sb.append(str);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.mob.tools.network.HTTPPart
    public InputStream getInputStream() throws Throwable {
        return new ByteArrayInputStream(this.sb.toString().getBytes("utf-8"));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.mob.tools.network.HTTPPart
    public long length() throws Throwable {
        return this.sb.toString().getBytes("utf-8").length;
    }

    public String toString() {
        return this.sb.toString();
    }
}
