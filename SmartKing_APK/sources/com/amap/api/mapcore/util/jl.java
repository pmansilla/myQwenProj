package com.amap.api.mapcore.util;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/* compiled from: EncryptRsaDataStrategy.java */
/* loaded from: classes.dex */
public class jl extends jq {
    private ht a;

    public jl() {
        this.a = new hw();
    }

    public jl(jq jqVar) {
        super(jqVar);
        this.a = new hw();
    }

    @Override // com.amap.api.mapcore.util.jq
    protected byte[] a(byte[] bArr) throws CertificateException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        return this.a.b(bArr);
    }
}
