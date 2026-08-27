package com.amap.openapi;

import android.text.TextUtils;
import com.amap.location.common.log.ALLog;
import com.amap.location.common.network.HttpRequest;
import com.amap.location.common.network.HttpResponse;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.HashMap;
import javax.crypto.Cipher;
import kotlin.jvm.internal.ByteCompanionObject;

/* compiled from: CloudUtils.java */
/* loaded from: classes.dex */
public class e {
    private static final byte[] a = {48, 92, 48, 13, 6, 9, 42, -122, 72, -122, -9, 13, 1, 1, 1, 5, 0, 3, 75, 0, 48, 72, 2, 65, 0, -18, 74, 99, -65, 73, -9, -121, 104, -44, 122, 126, -72, -9, 4, -10, -30, 102, -73, 101, 13, -119, -83, -90, -101, 87, -8, -70, 57, 80, 65, 125, -117, -49, -118, 45, -73, 75, 39, -45, -16, -116, 34, -36, -118, -121, -78, -72, ByteCompanionObject.MIN_VALUE, 67, -15, -31, 23, -7, -21, -72, -127, -89, -95, -23, 121, -60, 24, 5, -75, 2, 3, 1, 0, 1};

    public static boolean a(String str) {
        int indexOf;
        String substring;
        String substring2;
        if (TextUtils.isEmpty(str) || (indexOf = str.indexOf("$")) <= 0) {
            return false;
        }
        try {
            substring = str.substring(0, indexOf);
            substring2 = str.substring(indexOf + 1, str.length());
        } catch (Exception unused) {
        }
        if (TextUtils.isEmpty(substring)) {
            return false;
        }
        byte[] a2 = a(com.amap.location.common.util.b.a(substring, "", ""));
        byte[] b = b(substring2);
        if (a2 != null && b != null) {
            return Arrays.equals(a2, b);
        }
        return false;
    }

    public static byte[] a(String str, byte[] bArr, d dVar) {
        if (dVar.f() == null) {
            ALLog.trace("CloudUtils", "网络库为空");
            return null;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("et", "111");
        hashMap.put("Accept-Encoding", "gzip");
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.headers = hashMap;
        httpRequest.body = bArr;
        httpRequest.url = str;
        HttpResponse post = dVar.f().post(httpRequest);
        if (post == null || post.statusCode != 200) {
            return null;
        }
        return post.body;
    }

    private static byte[] a(byte[] bArr) {
        try {
            PublicKey generatePublic = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(a));
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(2, generatePublic);
            return cipher.doFinal(bArr);
        } catch (Exception unused) {
            return null;
        }
    }

    private static byte[] b(String str) {
        try {
            return MessageDigest.getInstance("SHA1").digest(str.getBytes());
        } catch (Exception unused) {
            return null;
        }
    }
}
