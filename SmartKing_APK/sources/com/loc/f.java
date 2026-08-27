package com.loc;

import android.support.v4.view.MotionEventCompat;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;

/* compiled from: Base64Util.java */
/* loaded from: classes.dex */
public class f {
    static final /* synthetic */ boolean a = !f.class.desiredAssertionStatus();
    private static final byte[] b = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
    private static final byte[] c = {-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, -9, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, FileDownloadStatus.toFileDownloadService, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9};
    private static final byte[] d = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
    private static final byte[] e = {-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, FileDownloadStatus.toFileDownloadService, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, 63, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9};
    private static final byte[] f = {45, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 95, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122};
    private static final byte[] g = {-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 0, -9, -9, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, -9, -9, -9, -1, -9, -9, -9, FileDownloadStatus.toFileDownloadService, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, -9, -9, -9, -9, 37, -9, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9};

    private f() {
    }

    public static String a(byte[] bArr) {
        String str;
        try {
            str = a(bArr, bArr.length);
        } catch (IOException e2) {
            if (!a) {
                throw new AssertionError(e2.getMessage());
            }
            str = null;
        }
        if (a || str != null) {
            return str;
        }
        throw new AssertionError();
    }

    private static String a(byte[] bArr, int i) throws IOException {
        byte[] bArr2;
        if (bArr == null) {
            throw new NullPointerException("Cannot serialize a null array.");
        }
        if (i < 0) {
            throw new IllegalArgumentException("Cannot have length offset: " + i);
        }
        if (i + 0 > bArr.length) {
            throw new IllegalArgumentException(String.format("Cannot have offset of %d and length of %d with array of length %d", 0, Integer.valueOf(i), Integer.valueOf(bArr.length)));
        }
        byte[] bArr3 = new byte[((i / 3) * 4) + (i % 3 <= 0 ? 0 : 4)];
        int i2 = i - 2;
        int i3 = 0;
        int i4 = 0;
        while (i3 < i2) {
            a(bArr, i3 + 0, 3, bArr3, i4);
            i3 += 3;
            i4 += 4;
        }
        if (i3 < i) {
            a(bArr, i3 + 0, i - i3, bArr3, i4);
            i4 += 4;
        }
        if (i4 <= bArr3.length - 1) {
            bArr2 = new byte[i4];
            System.arraycopy(bArr3, 0, bArr2, 0, i4);
        } else {
            bArr2 = bArr3;
        }
        try {
            return new String(bArr2, "US-ASCII");
        } catch (UnsupportedEncodingException unused) {
            return new String(bArr2);
        }
    }

    public static byte[] a(String str) throws IOException {
        return b(str);
    }

    private static byte[] a(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        byte[] bArr3 = b;
        int i4 = (i2 > 0 ? (bArr[i] << 24) >>> 8 : 0) | (i2 > 1 ? (bArr[i + 1] << 24) >>> 16 : 0) | (i2 > 2 ? (bArr[i + 2] << 24) >>> 24 : 0);
        switch (i2) {
            case 1:
                bArr2[i3] = bArr3[i4 >>> 18];
                bArr2[i3 + 1] = bArr3[(i4 >>> 12) & 63];
                bArr2[i3 + 2] = 61;
                bArr2[i3 + 3] = 61;
                return bArr2;
            case 2:
                bArr2[i3] = bArr3[i4 >>> 18];
                bArr2[i3 + 1] = bArr3[(i4 >>> 12) & 63];
                bArr2[i3 + 2] = bArr3[(i4 >>> 6) & 63];
                bArr2[i3 + 3] = 61;
                return bArr2;
            case 3:
                bArr2[i3] = bArr3[i4 >>> 18];
                bArr2[i3 + 1] = bArr3[(i4 >>> 12) & 63];
                bArr2[i3 + 2] = bArr3[(i4 >>> 6) & 63];
                bArr2[i3 + 3] = bArr3[i4 & 63];
                return bArr2;
            default:
                return bArr2;
        }
    }

    private static byte[] b(String str) throws IOException {
        byte[] bytes;
        ByteArrayOutputStream byteArrayOutputStream;
        ByteArrayInputStream byteArrayInputStream;
        GZIPInputStream gZIPInputStream;
        if (str == null) {
            throw new NullPointerException("Input string was null.");
        }
        try {
            bytes = str.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException unused) {
            bytes = str.getBytes();
        }
        byte[] b2 = b(bytes, bytes.length);
        if (b2.length < 4 || 35615 != ((b2[0] & 255) | ((b2[1] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK))) {
            return b2;
        }
        byte[] bArr = new byte[2048];
        GZIPInputStream gZIPInputStream2 = null;
        gZIPInputStream2 = null;
        gZIPInputStream2 = null;
        ByteArrayOutputStream byteArrayOutputStream2 = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                byteArrayInputStream = new ByteArrayInputStream(b2);
            } catch (IOException e2) {
                e = e2;
                byteArrayInputStream = null;
                gZIPInputStream = null;
            } catch (Throwable th) {
                th = th;
                byteArrayInputStream = null;
            }
        } catch (IOException e3) {
            e = e3;
            byteArrayInputStream = null;
            gZIPInputStream = null;
        } catch (Throwable th2) {
            th = th2;
            byteArrayOutputStream = null;
            byteArrayInputStream = null;
        }
        try {
            gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
            while (true) {
                try {
                    int read = gZIPInputStream.read(bArr);
                    if (read < 0) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr, 0, read);
                } catch (IOException e4) {
                    e = e4;
                    byteArrayOutputStream2 = byteArrayOutputStream;
                    try {
                        e.printStackTrace();
                        try {
                            byteArrayOutputStream2.close();
                        } catch (Exception unused2) {
                        }
                        try {
                            gZIPInputStream.close();
                        } catch (Exception unused3) {
                        }
                        try {
                            byteArrayInputStream.close();
                            return b2;
                        } catch (Exception unused4) {
                            return b2;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        byteArrayOutputStream = byteArrayOutputStream2;
                        gZIPInputStream2 = gZIPInputStream;
                        try {
                            byteArrayOutputStream.close();
                        } catch (Exception unused5) {
                        }
                        try {
                            gZIPInputStream2.close();
                        } catch (Exception unused6) {
                        }
                        try {
                            byteArrayInputStream.close();
                            throw th;
                        } catch (Exception unused7) {
                            throw th;
                        }
                    }
                } catch (Throwable th4) {
                    th = th4;
                    gZIPInputStream2 = gZIPInputStream;
                    byteArrayOutputStream.close();
                    gZIPInputStream2.close();
                    byteArrayInputStream.close();
                    throw th;
                }
            }
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            try {
                byteArrayOutputStream.close();
            } catch (Exception unused8) {
            }
            try {
                gZIPInputStream.close();
            } catch (Exception unused9) {
            }
            try {
                byteArrayInputStream.close();
            } catch (Exception unused10) {
            }
            return byteArray;
        } catch (IOException e5) {
            e = e5;
            gZIPInputStream = null;
        } catch (Throwable th5) {
            th = th5;
            byteArrayOutputStream.close();
            gZIPInputStream2.close();
            byteArrayInputStream.close();
            throw th;
        }
    }

    private static byte[] b(byte[] bArr, int i) throws IOException {
        int i2;
        int i3;
        if (bArr == null) {
            throw new NullPointerException("Cannot decode null source array.");
        }
        int i4 = i + 0;
        if (i4 > bArr.length) {
            throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and process %d bytes.", Integer.valueOf(bArr.length), 0, Integer.valueOf(i)));
        }
        if (i == 0) {
            return new byte[0];
        }
        if (i < 4) {
            throw new IllegalArgumentException("Base64Util-encoded string must have at least four characters, but length specified was " + i);
        }
        byte[] bArr2 = c;
        byte[] bArr3 = new byte[(i * 3) / 4];
        byte[] bArr4 = new byte[4];
        int i5 = 0;
        int i6 = 0;
        for (int i7 = 0; i7 < i4; i7++) {
            byte b2 = bArr2[bArr[i7] & 255];
            if (b2 < -5) {
                throw new IOException(String.format("Bad Base64Util input character decimal %d in array position %d", Integer.valueOf(bArr[i7] & 255), Integer.valueOf(i7)));
            }
            if (b2 >= -1) {
                int i8 = i5 + 1;
                bArr4[i5] = bArr[i7];
                if (i8 <= 3) {
                    i5 = i8;
                } else {
                    if (i6 < 0 || (i2 = i6 + 2) >= bArr3.length) {
                        throw new IllegalArgumentException(String.format("Destination array with length %d cannot have offset of %d and still store three bytes.", Integer.valueOf(bArr3.length), Integer.valueOf(i6)));
                    }
                    byte[] bArr5 = c;
                    if (bArr4[2] == 61) {
                        bArr3[i6] = (byte) ((((bArr5[bArr4[0]] & 255) << 18) | ((bArr5[bArr4[1]] & 255) << 12)) >>> 16);
                        i3 = 1;
                    } else if (bArr4[3] == 61) {
                        int i9 = ((bArr5[bArr4[0]] & 255) << 18) | ((bArr5[bArr4[1]] & 255) << 12) | ((bArr5[bArr4[2]] & 255) << 6);
                        bArr3[i6] = (byte) (i9 >>> 16);
                        bArr3[i6 + 1] = (byte) (i9 >>> 8);
                        i3 = 2;
                    } else {
                        int i10 = (bArr5[bArr4[3]] & 255) | ((bArr5[bArr4[0]] & 255) << 18) | ((bArr5[bArr4[1]] & 255) << 12) | ((bArr5[bArr4[2]] & 255) << 6);
                        bArr3[i6] = (byte) (i10 >> 16);
                        bArr3[i6 + 1] = (byte) (i10 >> 8);
                        bArr3[i2] = (byte) i10;
                        i3 = 3;
                    }
                    i6 += i3;
                    if (bArr[i7] == 61) {
                        break;
                    }
                    i5 = 0;
                }
            }
        }
        byte[] bArr6 = new byte[i6];
        System.arraycopy(bArr3, 0, bArr6, 0, i6);
        return bArr6;
    }
}
