package cn.smssdk.net.h;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/* compiled from: DES.java */
/* loaded from: classes.dex */
public class c {
    private static final byte[] c = "00000000".getBytes();
    private String a;
    private AlgorithmParameterSpec b;

    public c(String str, byte[] bArr) {
        this.a = "DES/CBC/PKCS5Padding";
        this.a = str;
        this.b = new IvParameterSpec(bArr);
    }

    private static Key a(byte[] bArr) throws Exception {
        return SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(bArr));
    }

    public static byte[] a(byte[] bArr, byte[] bArr2, byte[] bArr3) throws Exception {
        return new c("DES/CBC/PKCS5Padding", bArr3).a(bArr, bArr2);
    }

    public static byte[] b(byte[] bArr, byte[] bArr2) throws Exception {
        return a(bArr, bArr2, c);
    }

    public byte[] a(byte[] bArr, byte[] bArr2) throws Exception {
        Key a = a(bArr2);
        Cipher cipher = Cipher.getInstance(this.a);
        cipher.init(2, a, this.b);
        return cipher.doFinal(bArr);
    }
}
