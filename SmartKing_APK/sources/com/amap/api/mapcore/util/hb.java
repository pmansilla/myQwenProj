package com.amap.api.mapcore.util;

/* compiled from: AESMD5Util.java */
/* loaded from: classes.dex */
public class hb {
    private static byte[] a = ie.a;
    private static byte[] b = ie.b;
    private static int c = 6;

    public static byte[] a(byte[] bArr) {
        try {
            return hj.b(a, bArr, b);
        } catch (Throwable unused) {
            return new byte[0];
        }
    }

    public static byte[] b(byte[] bArr) {
        try {
            return hj.a(a, bArr, b);
        } catch (Exception unused) {
            return new byte[0];
        }
    }
}
