package com.loc;

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
public abstract class bz {
    bz c;
    byte[] d = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    public bz() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public bz(bz bzVar) {
        this.c = bzVar;
    }

    public final byte[] a() throws CertificateException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        bz bzVar = this;
        while (true) {
            byte[] a = bzVar.a(bzVar.d);
            if (bzVar.c == null) {
                return a;
            }
            bzVar.c.d = a;
            bzVar = bzVar.c;
        }
    }

    protected abstract byte[] a(byte[] bArr) throws CertificateException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException;

    public void b(byte[] bArr) {
    }
}
