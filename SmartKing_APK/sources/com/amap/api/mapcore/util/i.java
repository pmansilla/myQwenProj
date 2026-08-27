package com.amap.api.mapcore.util;

import com.amap.location.common.model.AmapLoc;
import com.litesuits.orm.db.assit.SQLBuilder;
import java.security.MessageDigest;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* compiled from: SecurityUtil.java */
/* loaded from: classes.dex */
public final class i {
    private static byte[] a = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static int[] b = {-1835101520, 1886468213, 614966444, 1545358877, 153741611, 1460081543, -1035306422, -708721439};

    public static String a() {
        SecureRandom secureRandom = new SecureRandom();
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128, secureRandom);
            return f.a(keyGenerator.generateKey().getEncoded());
        } catch (Throwable unused) {
            return null;
        }
    }

    private static String a(int i) {
        char[] cArr = new char[4];
        for (int i2 = 0; i2 < 4; i2++) {
            int i3 = (4 - i2) - 1;
            cArr[i3] = (char) ((i >>> (i2 * 8)) & 255);
            b(cArr[i3]);
        }
        return new String(cArr);
    }

    private static String a(int i, int i2) {
        return a(b(c(i), i2));
    }

    public static String a(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            char[] charArray = str.toCharArray();
            byte[] bArr = new byte[charArray.length];
            for (int i = 0; i < charArray.length; i++) {
                bArr[i] = (byte) charArray[i];
            }
            byte[] digest = messageDigest.digest(bArr);
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b2 : digest) {
                int i2 = b2 & 255;
                if (i2 < 16) {
                    stringBuffer.append(AmapLoc.RESULT_TYPE_GPS);
                }
                stringBuffer.append(Integer.toHexString(i2));
            }
            return stringBuffer.toString();
        } catch (Throwable unused) {
            return "";
        }
    }

    private static String a(int[] iArr) {
        StringBuilder sb = new StringBuilder();
        if (iArr != null) {
            for (int i = 0; i < iArr.length; i++) {
                sb.append(a(iArr[i], i));
            }
        }
        return sb.toString();
    }

    public static byte[] a(byte[] bArr) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(a);
            SecretKeySpec secretKeySpec = new SecretKeySpec(a(b).getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(1, secretKeySpec, ivParameterSpec);
            return cipher.doFinal(bArr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static int b(int i, int i2) {
        int i3 = 0;
        for (int i4 = 0; i4 < i2; i4++) {
            i3 = (i3 >> 1) | Integer.MIN_VALUE;
        }
        return (i << i2) | ((i & i3) >>> (32 - i2));
    }

    private static String b(int i) {
        String str = SQLBuilder.BLANK;
        for (int i2 = 0; i2 < 32; i2++) {
            str = str + (((Integer.MIN_VALUE >>> i2) & i) >>> (31 - i2));
        }
        return str;
    }

    public static String b(String str) {
        try {
            return f.a(a(str.getBytes("UTF-8")));
        } catch (Throwable unused) {
            return null;
        }
    }

    public static byte[] b(byte[] bArr) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(a);
            SecretKeySpec secretKeySpec = new SecretKeySpec(a(b).getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(2, secretKeySpec, ivParameterSpec);
            return cipher.doFinal(bArr);
        } catch (Throwable unused) {
            return null;
        }
    }

    private static int c(int i) {
        int i2 = 1;
        for (int i3 = 0; i3 < 15; i3++) {
            i2 = (i2 << 2) | 1;
        }
        return ((i & i2) << 1) | (((i2 << 1) & i) >>> 1);
    }

    public static String c(String str) {
        try {
            return new String(b(f.a(str)), "UTF-8");
        } catch (Throwable unused) {
            return null;
        }
    }
}
