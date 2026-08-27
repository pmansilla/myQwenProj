package com.loc;

/* compiled from: ADDNumEncryptProcessor.java */
/* loaded from: classes.dex */
public final class af extends ah {
    af() {
    }

    public af(ah ahVar) {
        super(ahVar);
    }

    @Override // com.loc.ah
    protected final byte[] a(byte[] bArr) {
        return ad.a(ad.a(bArr) + "||1");
    }
}
