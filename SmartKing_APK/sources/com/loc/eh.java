package com.loc;

import com.amap.location.common.model.AmapLoc;
import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* compiled from: Encrypt.java */
/* loaded from: classes.dex */
public final class eh {
    private static final char[] a = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final byte[] b = {0, 1, 1, 2, 3, 5, 8, 13, 8, 7, 6, 5, 4, 3, 2, 1};
    private static final IvParameterSpec c = new IvParameterSpec(b);

    public static String a(String str) {
        if (str != null) {
            try {
                if (str.length() != 0) {
                    return a("MD5", a("SHA1", str) + str);
                }
            } catch (Throwable th) {
                es.a(th, "Encrypt", "generatorKey");
            }
        }
        return null;
    }

    public static String a(String str, String str2) {
        if (str2 == null) {
            return null;
        }
        try {
            byte[] a2 = aa.a(str2.getBytes("UTF-8"), str);
            int length = a2.length;
            StringBuilder sb = new StringBuilder(length * 2);
            for (int i = 0; i < length; i++) {
                sb.append(a[(a2[i] >> 4) & 15]);
                sb.append(a[a2[i] & 15]);
            }
            return sb.toString();
        } catch (Throwable th) {
            es.a(th, "Encrypt", "encode");
            return null;
        }
    }

    public static byte[] a(byte[] bArr) {
        try {
            byte[] bArr2 = new byte[16];
            byte[] bArr3 = new byte[bArr.length - 16];
            System.arraycopy(bArr, 0, bArr2, 0, 16);
            System.arraycopy(bArr, 16, bArr3, 0, bArr.length - 16);
            SecretKeySpec secretKeySpec = new SecretKeySpec(bArr2, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(2, secretKeySpec, new IvParameterSpec(ad.c()));
            return cipher.doFinal(bArr3);
        } catch (Throwable th) {
            es.a(th, "Encrypt", "decryptRsponse length = " + (bArr != null ? bArr.length : 0));
            return null;
        }
    }

    public static synchronized byte[] a(byte[] bArr, String str) throws Exception {
        byte[] byteArray;
        synchronized (eh.class) {
            PrivateKey generatePrivate = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(y.b(str)));
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(1, generatePrivate);
            int length = bArr.length;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int i = 0;
            int i2 = 0;
            while (true) {
                int i3 = length - i;
                if (i3 > 0) {
                    byte[] doFinal = i3 > 245 ? cipher.doFinal(bArr, i, 245) : cipher.doFinal(bArr, i, i3);
                    byteArrayOutputStream.write(doFinal, 0, doFinal.length);
                    i2++;
                    i = i2 * 245;
                } else {
                    byteArray = byteArrayOutputStream.toByteArray();
                    byteArrayOutputStream.close();
                }
            }
        }
        return byteArray;
    }

    public static byte[] a(byte[] bArr, byte[] bArr2) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(2, new SecretKeySpec(bArr, "AES"), c);
        return cipher.doFinal(bArr2);
    }

    private static SecretKeySpec b(String str) {
        byte[] bArr;
        if (str == null) {
            str = "";
        }
        StringBuffer stringBuffer = new StringBuffer(16);
        while (true) {
            stringBuffer.append(str);
            if (stringBuffer.length() >= 16) {
                break;
            }
            str = AmapLoc.RESULT_TYPE_GPS;
        }
        if (stringBuffer.length() > 16) {
            stringBuffer.setLength(16);
        }
        try {
            bArr = stringBuffer.toString().getBytes("UTF-8");
        } catch (Throwable th) {
            es.a(th, "Encrypt", "createKey");
            bArr = null;
        }
        return new SecretKeySpec(bArr, "AES");
    }

    public static synchronized byte[] b(byte[] bArr, String str) throws Exception {
        byte[] byteArray;
        synchronized (eh.class) {
            PrivateKey generatePrivate = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(y.b(str)));
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(2, generatePrivate);
            int length = bArr.length;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int i = 0;
            int i2 = 0;
            while (true) {
                int i3 = length - i;
                if (i3 > 0) {
                    byte[] doFinal = i3 > 256 ? cipher.doFinal(bArr, i, 256) : cipher.doFinal(bArr, i, i3);
                    byteArrayOutputStream.write(doFinal, 0, doFinal.length);
                    i2++;
                    i = i2 * 256;
                } else {
                    byteArray = byteArrayOutputStream.toByteArray();
                    byteArrayOutputStream.close();
                }
            }
        }
        return byteArray;
    }

    public static byte[] c(byte[] bArr, String str) {
        try {
            SecretKeySpec b2 = b(str);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ad.c());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(1, b2, ivParameterSpec);
            return cipher.doFinal(bArr);
        } catch (Throwable th) {
            es.a(th, "Encrypt", "aesEncrypt");
            return null;
        }
    }

    public static byte[] d(byte[] bArr, String str) {
        try {
            SecretKeySpec b2 = b(str);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ad.c());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(2, b2, ivParameterSpec);
            return cipher.doFinal(bArr);
        } catch (Throwable th) {
            es.a(th, "Encrypt", "aesDecrypt");
            return null;
        }
    }
}
