package com.amap.api.mapcore.util;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/* compiled from: UpdateDataStrategy.java */
/* loaded from: classes.dex */
public abstract class jq {
    jq c;
    byte[] d = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    public jq() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public jq(jq jqVar) {
        this.c = jqVar;
    }

    public byte[] a() throws CertificateException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        byte[] a = a(this.d);
        if (this.c == null) {
            return a;
        }
        this.c.c(a);
        return this.c.a();
    }

    protected abstract byte[] a(byte[] bArr) throws CertificateException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException;

    public void b(byte[] bArr) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(byte[] bArr) {
        this.d = bArr;
    }
}
