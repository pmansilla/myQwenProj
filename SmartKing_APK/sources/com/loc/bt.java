package com.loc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/* compiled from: ByteJoinDataStrategy.java */
/* loaded from: classes.dex */
public final class bt extends bz {
    ByteArrayOutputStream a;

    public bt() {
        this.a = new ByteArrayOutputStream();
    }

    public bt(bz bzVar) {
        super(bzVar);
        this.a = new ByteArrayOutputStream();
    }

    @Override // com.loc.bz
    protected final byte[] a(byte[] bArr) {
        byte[] byteArray = this.a.toByteArray();
        try {
            this.a.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.a = new ByteArrayOutputStream();
        return byteArray;
    }

    @Override // com.loc.bz
    public final void b(byte[] bArr) {
        try {
            this.a.write(bArr);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
