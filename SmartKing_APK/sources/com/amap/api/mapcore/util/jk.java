package com.amap.api.mapcore.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/* compiled from: ByteJoinDataStrategy.java */
/* loaded from: classes.dex */
public class jk extends jq {
    ByteArrayOutputStream a;

    public jk() {
        this.a = new ByteArrayOutputStream();
    }

    public jk(jq jqVar) {
        super(jqVar);
        this.a = new ByteArrayOutputStream();
    }

    @Override // com.amap.api.mapcore.util.jq
    protected byte[] a(byte[] bArr) {
        byte[] byteArray = this.a.toByteArray();
        try {
            this.a.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.a = new ByteArrayOutputStream();
        return byteArray;
    }

    @Override // com.amap.api.mapcore.util.jq
    public void b(byte[] bArr) {
        try {
            this.a.write(bArr);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
