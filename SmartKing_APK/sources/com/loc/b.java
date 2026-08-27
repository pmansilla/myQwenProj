package com.loc;

/* compiled from: XXTEA.java */
/* loaded from: classes.dex */
public final class b {
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
        int[] a2 = a(bArr3);
        int[] a3 = a(bArr2);
        int length2 = a2.length - 1;
        if (length2 > 0) {
            if (a3.length < 4) {
                int[] iArr = new int[4];
                System.arraycopy(a3, 0, iArr, 0, a3.length);
                a3 = iArr;
            }
            int i2 = (52 / (length2 + 1)) + 6;
            int i3 = a2[length2];
            int i4 = 0;
            while (true) {
                int i5 = i2 - 1;
                if (i2 <= 0) {
                    break;
                }
                i4 -= 1640531527;
                int i6 = (i4 >>> 2) & 3;
                int i7 = i3;
                int i8 = 0;
                while (i8 < length2) {
                    int i9 = i8 + 1;
                    int i10 = a2[i9];
                    i7 = ((((i7 >>> 5) ^ (i10 << 2)) + ((i10 >>> 3) ^ (i7 << 4))) ^ ((i10 ^ i4) + (i7 ^ a3[(i8 & 3) ^ i6]))) + a2[i8];
                    a2[i8] = i7;
                    i8 = i9;
                }
                int i11 = a2[0];
                i3 = a2[length2] + ((((i7 >>> 5) ^ (i11 << 2)) + ((i11 >>> 3) ^ (i7 << 4))) ^ ((i11 ^ i4) + (a3[i6 ^ (i8 & 3)] ^ i7)));
                a2[length2] = i3;
                i2 = i5;
            }
        }
        int length3 = a2.length << 2;
        byte[] bArr4 = new byte[length3];
        for (int i12 = 0; i12 < length3; i12++) {
            bArr4[i12] = (byte) ((a2[i12 >>> 2] >>> ((i12 & 3) << 3)) & 255);
        }
        return bArr4;
    }

    private static int[] a(byte[] bArr) {
        int[] iArr = new int[bArr.length >>> 2];
        int length = bArr.length;
        for (int i = 0; i < length; i++) {
            int i2 = i >>> 2;
            iArr[i2] = iArr[i2] | ((bArr[i] & 255) << ((i & 3) << 3));
        }
        return iArr;
    }
}
