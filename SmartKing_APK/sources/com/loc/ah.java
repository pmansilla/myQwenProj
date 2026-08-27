package com.loc;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/* compiled from: EncryptProcessor.java */
/* loaded from: classes.dex */
public abstract class ah {
    ah a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ah() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ah(ah ahVar) {
        this.a = ahVar;
    }

    protected abstract byte[] a(byte[] bArr) throws CertificateException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException;

    public final byte[] b(byte[] bArr) throws CertificateException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        if (this.a != null) {
            bArr = this.a.b(bArr);
        }
        return a(bArr);
    }
}
