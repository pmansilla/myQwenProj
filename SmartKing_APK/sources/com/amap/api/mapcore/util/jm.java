package com.amap.api.mapcore.util;

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
public class jm extends jq {
    private Context a;
    private String b;
    private ht e;
    private Object[] f;

    public jm(Context context, jq jqVar, ht htVar, String str, Object... objArr) {
        super(jqVar);
        this.a = context;
        this.b = str;
        this.e = htVar;
        this.f = objArr;
    }

    private String a(Context context) {
        try {
            return String.format(hp.c(this.b), this.f);
        } catch (Throwable th) {
            th.printStackTrace();
            ic.c(th, "ofm", "gpj");
            return "";
        }
    }

    private String b(Context context) throws CertificateException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        return hp.a(this.e.b(hp.a(a(context))));
    }

    @Override // com.amap.api.mapcore.util.jq
    protected byte[] a(byte[] bArr) throws CertificateException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        String a = hp.a(bArr);
        if (TextUtils.isEmpty(a)) {
            return null;
        }
        return hp.a("{\"pinfo\":\"" + b(this.a) + "\",\"els\":[" + a + "]}");
    }
}
