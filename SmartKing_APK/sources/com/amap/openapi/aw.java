package com.amap.openapi;

import android.support.annotation.NonNull;
import android.util.Base64;
import com.amap.location.common.model.AmapLoc;
import com.amap.location.security.Core;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* compiled from: AESUtil.java */
/* loaded from: classes.dex */
public class aw {
    private static byte[] a() {
        String[] split = new StringBuffer("16,16,18,77,15,911,121,77,121,911,38,77,911,99,86,67,611,96,48,77,84,911,38,67,021,301,86,67,611,98,48,77,511,77,48,97,511,58,48,97,511,84,501,87,511,96,48,77,221,911,38,77,121,37,86,67,25,301,86,67,021,96,86,67,021,701,86,67,35,56,86,67,611,37,221,87").reverse().toString().split(",");
        byte[] bArr = new byte[split.length];
        for (int i = 0; i < split.length; i++) {
            bArr[i] = Byte.parseByte(split[i]);
        }
        String[] split2 = new StringBuffer(new String(Base64.decode(bArr, 2))).reverse().toString().split(",");
        byte[] bArr2 = new byte[split2.length];
        for (int i2 = 0; i2 < split2.length; i2++) {
            bArr2[i2] = Byte.parseByte(split2[i2]);
        }
        return bArr2;
    }

    public static byte[] a(String str) throws Exception {
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
        return b(stringBuffer.toString());
    }

    public static byte[] a(String str, byte[] bArr) throws Exception {
        return Core.cole(bArr, a(str), a());
    }

    public static byte[] b(@NonNull String str) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        messageDigest.update(str.getBytes());
        byte[] digest = messageDigest.digest();
        StringBuffer stringBuffer = new StringBuffer("");
        for (int i : digest) {
            if (i < 0) {
                i += 256;
            }
            if (i < 16) {
                stringBuffer.append(AmapLoc.RESULT_TYPE_GPS);
            }
            stringBuffer.append(Integer.toHexString(i));
        }
        if (stringBuffer.length() > 16) {
            stringBuffer.setLength(16);
        }
        return stringBuffer.toString().getBytes();
    }
}
