package com.amap.api.mapcore.util;

/* compiled from: ADDNumEncryptProcessor.java */
/* loaded from: classes.dex */
public class hr extends ht {
    hr() {
    }

    public hr(ht htVar) {
        super(htVar);
    }

    @Override // com.amap.api.mapcore.util.ht
    protected byte[] a(byte[] bArr) {
        return hp.a(hp.a(bArr) + "||1");
    }
}
