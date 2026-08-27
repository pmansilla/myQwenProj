package com.sun.mail.auth;

import kotlin.jvm.internal.ByteCompanionObject;

/* loaded from: classes2.dex */
public final class MD4 {
    private static final int S11 = 3;
    private static final int S12 = 7;
    private static final int S13 = 11;
    private static final int S14 = 19;
    private static final int S21 = 3;
    private static final int S22 = 5;
    private static final int S23 = 9;
    private static final int S24 = 13;
    private static final int S31 = 3;
    private static final int S32 = 9;
    private static final int S33 = 11;
    private static final int S34 = 15;
    private static final int blockSize = 64;
    private static final byte[] padding = new byte[136];
    private int bufOfs;
    private long bytesProcessed;
    private final byte[] buffer = new byte[64];
    private final int[] state = new int[4];
    private final int[] x = new int[16];

    static {
        padding[0] = ByteCompanionObject.MIN_VALUE;
    }

    public MD4() {
        implReset();
    }

    private static int FF(int i, int i2, int i3, int i4, int i5, int i6) {
        int i7 = i + (((i2 ^ (-1)) & i4) | (i3 & i2)) + i5;
        return (i7 >>> (32 - i6)) | (i7 << i6);
    }

    private static int GG(int i, int i2, int i3, int i4, int i5, int i6) {
        int i7 = i + ((i2 & i4) | (i2 & i3) | (i3 & i4)) + i5 + 1518500249;
        return (i7 >>> (32 - i6)) | (i7 << i6);
    }

    private static int HH(int i, int i2, int i3, int i4, int i5, int i6) {
        int i7 = i + ((i2 ^ i3) ^ i4) + i5 + 1859775393;
        return (i7 >>> (32 - i6)) | (i7 << i6);
    }

    private void engineUpdate(byte[] bArr, int i, int i2) {
        if (i2 == 0) {
            return;
        }
        if (i < 0 || i2 < 0 || i > bArr.length - i2) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (this.bytesProcessed < 0) {
            implReset();
        }
        this.bytesProcessed += i2;
        if (this.bufOfs != 0) {
            int min = Math.min(i2, 64 - this.bufOfs);
            System.arraycopy(bArr, i, this.buffer, this.bufOfs, min);
            this.bufOfs += min;
            i += min;
            i2 -= min;
            if (this.bufOfs >= 64) {
                implCompress(this.buffer, 0);
                this.bufOfs = 0;
            }
        }
        while (i2 >= 64) {
            implCompress(bArr, i);
            i2 -= 64;
            i += 64;
        }
        if (i2 > 0) {
            System.arraycopy(bArr, i, this.buffer, 0, i2);
            this.bufOfs = i2;
        }
    }

    private void implCompress(byte[] bArr, int i) {
        int i2 = i;
        for (int i3 = 0; i3 < this.x.length; i3++) {
            this.x[i3] = ((bArr[i2 + 1] & 255) << 8) | (bArr[i2] & 255) | ((bArr[i2 + 2] & 255) << 16) | ((bArr[i2 + 3] & 255) << 24);
            i2 += 4;
        }
        int i4 = this.state[0];
        int i5 = this.state[1];
        int i6 = this.state[2];
        int i7 = this.state[3];
        int FF = FF(i4, i5, i6, i7, this.x[0], 3);
        int FF2 = FF(i7, FF, i5, i6, this.x[1], 7);
        int FF3 = FF(i6, FF2, FF, i5, this.x[2], 11);
        int FF4 = FF(i5, FF3, FF2, FF, this.x[3], 19);
        int FF5 = FF(FF, FF4, FF3, FF2, this.x[4], 3);
        int FF6 = FF(FF2, FF5, FF4, FF3, this.x[5], 7);
        int FF7 = FF(FF3, FF6, FF5, FF4, this.x[6], 11);
        int FF8 = FF(FF4, FF7, FF6, FF5, this.x[7], 19);
        int FF9 = FF(FF5, FF8, FF7, FF6, this.x[8], 3);
        int FF10 = FF(FF6, FF9, FF8, FF7, this.x[9], 7);
        int FF11 = FF(FF7, FF10, FF9, FF8, this.x[10], 11);
        int FF12 = FF(FF8, FF11, FF10, FF9, this.x[11], 19);
        int FF13 = FF(FF9, FF12, FF11, FF10, this.x[12], 3);
        int FF14 = FF(FF10, FF13, FF12, FF11, this.x[13], 7);
        int FF15 = FF(FF11, FF14, FF13, FF12, this.x[14], 11);
        int FF16 = FF(FF12, FF15, FF14, FF13, this.x[15], 19);
        int GG = GG(FF13, FF16, FF15, FF14, this.x[0], 3);
        int GG2 = GG(FF14, GG, FF16, FF15, this.x[4], 5);
        int GG3 = GG(FF15, GG2, GG, FF16, this.x[8], 9);
        int GG4 = GG(FF16, GG3, GG2, GG, this.x[12], 13);
        int GG5 = GG(GG, GG4, GG3, GG2, this.x[1], 3);
        int GG6 = GG(GG2, GG5, GG4, GG3, this.x[5], 5);
        int GG7 = GG(GG3, GG6, GG5, GG4, this.x[9], 9);
        int GG8 = GG(GG4, GG7, GG6, GG5, this.x[13], 13);
        int GG9 = GG(GG5, GG8, GG7, GG6, this.x[2], 3);
        int GG10 = GG(GG6, GG9, GG8, GG7, this.x[6], 5);
        int GG11 = GG(GG7, GG10, GG9, GG8, this.x[10], 9);
        int GG12 = GG(GG8, GG11, GG10, GG9, this.x[14], 13);
        int GG13 = GG(GG9, GG12, GG11, GG10, this.x[3], 3);
        int GG14 = GG(GG10, GG13, GG12, GG11, this.x[7], 5);
        int GG15 = GG(GG11, GG14, GG13, GG12, this.x[11], 9);
        int GG16 = GG(GG12, GG15, GG14, GG13, this.x[15], 13);
        int HH = HH(GG13, GG16, GG15, GG14, this.x[0], 3);
        int HH2 = HH(GG14, HH, GG16, GG15, this.x[8], 9);
        int HH3 = HH(GG15, HH2, HH, GG16, this.x[4], 11);
        int HH4 = HH(GG16, HH3, HH2, HH, this.x[12], 15);
        int HH5 = HH(HH, HH4, HH3, HH2, this.x[2], 3);
        int HH6 = HH(HH2, HH5, HH4, HH3, this.x[10], 9);
        int HH7 = HH(HH3, HH6, HH5, HH4, this.x[6], 11);
        int HH8 = HH(HH4, HH7, HH6, HH5, this.x[14], 15);
        int HH9 = HH(HH5, HH8, HH7, HH6, this.x[1], 3);
        int HH10 = HH(HH6, HH9, HH8, HH7, this.x[9], 9);
        int HH11 = HH(HH7, HH10, HH9, HH8, this.x[5], 11);
        int HH12 = HH(HH8, HH11, HH10, HH9, this.x[13], 15);
        int HH13 = HH(HH9, HH12, HH11, HH10, this.x[3], 3);
        int HH14 = HH(HH10, HH13, HH12, HH11, this.x[11], 9);
        int HH15 = HH(HH11, HH14, HH13, HH12, this.x[7], 11);
        int HH16 = HH(HH12, HH15, HH14, HH13, this.x[15], 15);
        int[] iArr = this.state;
        iArr[0] = iArr[0] + HH13;
        int[] iArr2 = this.state;
        iArr2[1] = iArr2[1] + HH16;
        int[] iArr3 = this.state;
        iArr3[2] = iArr3[2] + HH15;
        int[] iArr4 = this.state;
        iArr4[3] = iArr4[3] + HH14;
    }

    private void implDigest(byte[] bArr, int i) {
        long j = this.bytesProcessed << 3;
        int i2 = ((int) this.bytesProcessed) & 63;
        engineUpdate(padding, 0, i2 < 56 ? 56 - i2 : 120 - i2);
        this.buffer[56] = (byte) j;
        this.buffer[57] = (byte) (j >> 8);
        this.buffer[58] = (byte) (j >> 16);
        this.buffer[59] = (byte) (j >> 24);
        this.buffer[60] = (byte) (j >> 32);
        this.buffer[61] = (byte) (j >> 40);
        this.buffer[62] = (byte) (j >> 48);
        this.buffer[63] = (byte) (j >> 56);
        implCompress(this.buffer, 0);
        for (int i3 = 0; i3 < this.state.length; i3++) {
            int i4 = this.state[i3];
            int i5 = i + 1;
            bArr[i] = (byte) i4;
            int i6 = i5 + 1;
            bArr[i5] = (byte) (i4 >> 8);
            int i7 = i6 + 1;
            bArr[i6] = (byte) (i4 >> 16);
            i = i7 + 1;
            bArr[i7] = (byte) (i4 >> 24);
        }
    }

    private void implReset() {
        this.state[0] = 1732584193;
        this.state[1] = -271733879;
        this.state[2] = -1732584194;
        this.state[3] = 271733878;
        this.bufOfs = 0;
        this.bytesProcessed = 0L;
    }

    public byte[] digest(byte[] bArr) {
        implReset();
        engineUpdate(bArr, 0, bArr.length);
        byte[] bArr2 = new byte[16];
        implDigest(bArr2, 0);
        return bArr2;
    }
}
