package com.amap.api.mapcore.util;

/* compiled from: XXTEA.java */
/* loaded from: classes.dex */
public class b {
    private static int a = 4;

    public static byte[] a(byte[] bArr, byte[] bArr2) {
        if (bArr.length == 0) {
            return bArr;
        }
        int length = bArr.length;
        int i = a - (length % a);
        byte[] bArr3 = new byte[((length / a) + 1) * a];
        System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
        while (length < bArr3.length) {
            bArr3[length] = (byte) i;
            length++;
        }
        return a(a(a(bArr3, true), a(bArr2, false)), false);
    }

    private static byte[] a(int[] iArr, boolean z) {
        int i;
        int length = iArr.length << 2;
        if (z) {
            i = iArr[iArr.length - 1];
            if (i > length) {
                return null;
            }
        } else {
            i = length;
        }
        byte[] bArr = new byte[i];
        for (int i2 = 0; i2 < i; i2++) {
            bArr[i2] = (byte) ((iArr[i2 >>> 2] >>> ((i2 & 3) << 3)) & 255);
        }
        return bArr;
    }

    private static int[] a(byte[] bArr, boolean z) {
        int[] iArr = new int[bArr.length >>> 2];
        int length = bArr.length;
        for (int i = 0; i < length; i++) {
            int i2 = i >>> 2;
            iArr[i2] = iArr[i2] | ((bArr[i] & 255) << ((i & 3) << 3));
        }
        return iArr;
    }

    public static int[] a(int[] iArr, int[] iArr2) {
        int length = iArr.length - 1;
        if (length < 1) {
            return iArr;
        }
        if (iArr2.length < 4) {
            int[] iArr3 = new int[4];
            System.arraycopy(iArr2, 0, iArr3, 0, iArr2.length);
            iArr2 = iArr3;
        }
        int i = iArr[length];
        int i2 = iArr[0];
        int i3 = (52 / (length + 1)) + 6;
        int i4 = i;
        int i5 = 0;
        while (true) {
            int i6 = i3 - 1;
            if (i3 <= 0) {
                return iArr;
            }
            i5 -= 1640531527;
            int i7 = (i5 >>> 2) & 3;
            int i8 = i4;
            int i9 = 0;
            while (i9 < length) {
                int i10 = i9 + 1;
                int i11 = iArr[i10];
                i8 = ((((i8 >>> 5) ^ (i11 << 2)) + ((i11 >>> 3) ^ (i8 << 4))) ^ ((i11 ^ i5) + (i8 ^ iArr2[(i9 & 3) ^ i7]))) + iArr[i9];
                iArr[i9] = i8;
                i9 = i10;
            }
            int i12 = iArr[0];
            i4 = iArr[length] + ((((i8 >>> 5) ^ (i12 << 2)) + ((i12 >>> 3) ^ (i8 << 4))) ^ ((i12 ^ i5) + (iArr2[i7 ^ (i9 & 3)] ^ i8)));
            iArr[length] = i4;
            i3 = i6;
        }
    }
}
