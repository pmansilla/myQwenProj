package com.loc;

import android.content.Context;
import android.text.TextUtils;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/* compiled from: HeaderAddStrategy.java */
/* loaded from: classes.dex */
public final class bv extends bz {
    private Context a;
    private String b;
    private ah e;
    private Object[] f;

    public bv(Context context, bz bzVar, ah ahVar, String str, Object... objArr) {
        super(bzVar);
        this.a = context;
        this.b = str;
        this.e = ahVar;
        this.f = objArr;
    }

    private String b() {
        try {
            return String.format(ad.c(this.b), this.f);
        } catch (Throwable th) {
            th.printStackTrace();
            aq.b(th, "ofm", "gpj");
            return "";
        }
    }

    @Override // com.loc.bz
    protected final byte[] a(byte[] bArr) throws CertificateException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        String a = ad.a(bArr);
        if (TextUtils.isEmpty(a)) {
            return null;
        }
        return ad.a("{\"pinfo\":\"" + ad.a(this.e.b(ad.a(b()))) + "\",\"els\":[" + a + "]}");
    }
}
