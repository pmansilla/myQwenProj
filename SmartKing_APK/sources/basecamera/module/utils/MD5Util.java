package basecamera.module.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* loaded from: classes.dex */
public class MD5Util {
    public static String getMD5(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes());
            return getString(messageDigest.digest());
        } catch (NoSuchAlgorithmException unused) {
            return str;
        }
    }

    private static String getString(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : bArr) {
            stringBuffer.append((int) b);
        }
        return stringBuffer.toString();
    }
}
