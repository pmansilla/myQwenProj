package com.amap.api.mapcore.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* compiled from: MD5.java */
/* loaded from: classes.dex */
public class hl {
    /* JADX WARN: Removed duplicated region for block: B:40:0x006b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String a(java.lang.String r5) {
        /*
            r0 = 0
            boolean r1 = android.text.TextUtils.isEmpty(r5)     // Catch: java.lang.Throwable -> L4d java.lang.Throwable -> L4f
            if (r1 == 0) goto L8
            return r0
        L8:
            java.io.File r1 = new java.io.File     // Catch: java.lang.Throwable -> L4d java.lang.Throwable -> L4f
            r1.<init>(r5)     // Catch: java.lang.Throwable -> L4d java.lang.Throwable -> L4f
            boolean r5 = r1.isFile()     // Catch: java.lang.Throwable -> L4d java.lang.Throwable -> L4f
            if (r5 == 0) goto L4c
            boolean r5 = r1.exists()     // Catch: java.lang.Throwable -> L4d java.lang.Throwable -> L4f
            if (r5 != 0) goto L1a
            goto L4c
        L1a:
            r5 = 2048(0x800, float:2.87E-42)
            byte[] r5 = new byte[r5]     // Catch: java.lang.Throwable -> L4d java.lang.Throwable -> L4f
            java.lang.String r2 = "MD5"
            java.security.MessageDigest r2 = java.security.MessageDigest.getInstance(r2)     // Catch: java.lang.Throwable -> L4d java.lang.Throwable -> L4f
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L4d java.lang.Throwable -> L4f
            r3.<init>(r1)     // Catch: java.lang.Throwable -> L4d java.lang.Throwable -> L4f
        L29:
            int r1 = r3.read(r5)     // Catch: java.lang.Throwable -> L4a java.lang.Throwable -> L67
            r4 = -1
            if (r1 == r4) goto L35
            r4 = 0
            r2.update(r5, r4, r1)     // Catch: java.lang.Throwable -> L4a java.lang.Throwable -> L67
            goto L29
        L35:
            byte[] r5 = r2.digest()     // Catch: java.lang.Throwable -> L4a java.lang.Throwable -> L67
            java.lang.String r5 = com.amap.api.mapcore.util.hp.e(r5)     // Catch: java.lang.Throwable -> L4a java.lang.Throwable -> L67
            r3.close()     // Catch: java.io.IOException -> L41
            goto L49
        L41:
            r0 = move-exception
            java.lang.String r1 = "MD5"
            java.lang.String r2 = "gfm"
            com.amap.api.mapcore.util.hz.a(r0, r1, r2)
        L49:
            return r5
        L4a:
            r5 = move-exception
            goto L51
        L4c:
            return r0
        L4d:
            r5 = move-exception
            goto L69
        L4f:
            r5 = move-exception
            r3 = r0
        L51:
            java.lang.String r1 = "MD5"
            java.lang.String r2 = "gfm"
            com.amap.api.mapcore.util.hz.a(r5, r1, r2)     // Catch: java.lang.Throwable -> L67
            if (r3 == 0) goto L66
            r3.close()     // Catch: java.io.IOException -> L5e
            goto L66
        L5e:
            r5 = move-exception
            java.lang.String r1 = "MD5"
            java.lang.String r2 = "gfm"
            com.amap.api.mapcore.util.hz.a(r5, r1, r2)
        L66:
            return r0
        L67:
            r5 = move-exception
            r0 = r3
        L69:
            if (r0 == 0) goto L77
            r0.close()     // Catch: java.io.IOException -> L6f
            goto L77
        L6f:
            r0 = move-exception
            java.lang.String r1 = "MD5"
            java.lang.String r2 = "gfm"
            com.amap.api.mapcore.util.hz.a(r0, r1, r2)
        L77:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.hl.a(java.lang.String):java.lang.String");
    }

    public static String a(byte[] bArr) {
        return hp.e(b(bArr));
    }

    public static byte[] a(byte[] bArr, String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(str);
            messageDigest.update(bArr);
            return messageDigest.digest();
        } catch (Throwable th) {
            hz.a(th, "MD5", "gmb");
            return null;
        }
    }

    public static String b(String str) {
        if (str == null) {
            return null;
        }
        return hp.e(d(str));
    }

    private static byte[] b(byte[] bArr) {
        return a(bArr, "MD5");
    }

    public static String c(String str) {
        return hp.f(e(str));
    }

    public static byte[] d(String str) {
        try {
            return f(str);
        } catch (Throwable th) {
            hz.a(th, "MD5", "gmb");
            return new byte[0];
        }
    }

    private static byte[] e(String str) {
        try {
            return f(str);
        } catch (Throwable th) {
            th.printStackTrace();
            return new byte[0];
        }
    }

    private static byte[] f(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (str == null) {
            return null;
        }
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(hp.a(str));
        return messageDigest.digest();
    }
}
