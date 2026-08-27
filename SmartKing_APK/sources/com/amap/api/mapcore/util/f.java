package com.amap.api.mapcore.util;

import android.support.v4.view.MotionEventCompat;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

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

    /* compiled from: Base64Util.java */
    /* loaded from: classes.dex */
    public static class a extends FilterOutputStream {
        private boolean a;
        private int b;
        private byte[] c;
        private int d;
        private int e;
        private boolean f;
        private byte[] g;
        private boolean h;
        private int i;
        private byte[] j;

        public a(OutputStream outputStream, int i) {
            super(outputStream);
            this.f = (i & 8) != 0;
            this.a = (i & 1) != 0;
            this.d = this.a ? 3 : 4;
            this.c = new byte[this.d];
            this.b = 0;
            this.e = 0;
            this.h = false;
            this.g = new byte[4];
            this.i = i;
            this.j = f.c(i);
        }

        public void a() throws IOException {
            if (this.b > 0) {
                if (!this.a) {
                    throw new IOException("Base64Util input not properly padded.");
                }
                this.out.write(f.b(this.g, this.c, this.b, this.i));
                this.b = 0;
            }
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            a();
            super.close();
            this.c = null;
            this.out = null;
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream
        public void write(int i) throws IOException {
            if (this.h) {
                this.out.write(i);
                return;
            }
            if (this.a) {
                byte[] bArr = this.c;
                int i2 = this.b;
                this.b = i2 + 1;
                bArr[i2] = (byte) i;
                if (this.b >= this.d) {
                    this.out.write(f.b(this.g, this.c, this.d, this.i));
                    this.e += 4;
                    if (this.f && this.e >= 76) {
                        this.out.write(10);
                        this.e = 0;
                    }
                    this.b = 0;
                    return;
                }
                return;
            }
            int i3 = i & 127;
            if (this.j[i3] <= -5) {
                if (this.j[i3] != -5) {
                    throw new IOException("Invalid character in Base64Util data.");
                }
                return;
            }
            byte[] bArr2 = this.c;
            int i4 = this.b;
            this.b = i4 + 1;
            bArr2[i4] = (byte) i;
            if (this.b >= this.d) {
                this.out.write(this.g, 0, f.b(this.c, 0, this.g, 0, this.i));
                this.b = 0;
            }
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream
        public void write(byte[] bArr, int i, int i2) throws IOException {
            if (this.h) {
                this.out.write(bArr, i, i2);
                return;
            }
            for (int i3 = 0; i3 < i2; i3++) {
                write(bArr[i + i3]);
            }
        }
    }

    private f() {
    }

    public static String a(byte[] bArr) {
        String str;
        try {
            str = a(bArr, 0, bArr.length, 0);
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

    public static String a(byte[] bArr, int i, int i2, int i3) throws IOException {
        byte[] b2 = b(bArr, i, i2, i3);
        try {
            return new String(b2, "US-ASCII");
        } catch (UnsupportedEncodingException unused) {
            return new String(b2);
        }
    }

    public static byte[] a(String str) throws IOException {
        return a(str, 0);
    }

    public static byte[] a(String str, int i) throws IOException {
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
        byte[] c2 = c(bytes, 0, bytes.length, i);
        boolean z = (i & 4) != 0;
        if (c2 == null || c2.length < 4 || z || 35615 != ((c2[0] & 255) | ((c2[1] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK))) {
            return c2;
        }
        byte[] bArr = new byte[2048];
        GZIPInputStream gZIPInputStream2 = null;
        gZIPInputStream2 = null;
        gZIPInputStream2 = null;
        ByteArrayOutputStream byteArrayOutputStream2 = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                byteArrayInputStream = new ByteArrayInputStream(c2);
                try {
                    gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
                    while (true) {
                        try {
                            int read = gZIPInputStream.read(bArr);
                            if (read < 0) {
                                break;
                            }
                            byteArrayOutputStream.write(bArr, 0, read);
                        } catch (IOException e2) {
                            e = e2;
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
                                    return c2;
                                } catch (Exception unused4) {
                                    return c2;
                                }
                            } catch (Throwable th) {
                                th = th;
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
                        } catch (Throwable th2) {
                            th = th2;
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
                } catch (IOException e3) {
                    e = e3;
                    gZIPInputStream = null;
                } catch (Throwable th3) {
                    th = th3;
                    byteArrayOutputStream.close();
                    gZIPInputStream2.close();
                    byteArrayInputStream.close();
                    throw th;
                }
            } catch (IOException e4) {
                e = e4;
                byteArrayInputStream = null;
                gZIPInputStream = null;
            } catch (Throwable th4) {
                th = th4;
                byteArrayInputStream = null;
            }
        } catch (IOException e5) {
            e = e5;
            byteArrayInputStream = null;
            gZIPInputStream = null;
        } catch (Throwable th5) {
            th = th5;
            byteArrayOutputStream = null;
            byteArrayInputStream = null;
        }
    }

    private static byte[] a(byte[] bArr, int i, int i2, byte[] bArr2, int i3, int i4) {
        byte[] b2 = b(i4);
        int i5 = (i2 > 0 ? (bArr[i] << 24) >>> 8 : 0) | (i2 > 1 ? (bArr[i + 1] << 24) >>> 16 : 0) | (i2 > 2 ? (bArr[i + 2] << 24) >>> 24 : 0);
        switch (i2) {
            case 1:
                bArr2[i3] = b2[i5 >>> 18];
                bArr2[i3 + 1] = b2[(i5 >>> 12) & 63];
                bArr2[i3 + 2] = 61;
                bArr2[i3 + 3] = 61;
                return bArr2;
            case 2:
                bArr2[i3] = b2[i5 >>> 18];
                bArr2[i3 + 1] = b2[(i5 >>> 12) & 63];
                bArr2[i3 + 2] = b2[(i5 >>> 6) & 63];
                bArr2[i3 + 3] = 61;
                return bArr2;
            case 3:
                bArr2[i3] = b2[i5 >>> 18];
                bArr2[i3 + 1] = b2[(i5 >>> 12) & 63];
                bArr2[i3 + 2] = b2[(i5 >>> 6) & 63];
                bArr2[i3 + 3] = b2[i5 & 63];
                return bArr2;
            default:
                return bArr2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int b(byte[] bArr, int i, byte[] bArr2, int i2, int i3) {
        int i4;
        int i5;
        if (bArr == null) {
            throw new NullPointerException("Source array was null.");
        }
        if (bArr2 == null) {
            throw new NullPointerException("Destination array was null.");
        }
        if (i < 0 || (i4 = i + 3) >= bArr.length) {
            throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and still process four bytes.", Integer.valueOf(bArr.length), Integer.valueOf(i)));
        }
        if (i2 < 0 || (i5 = i2 + 2) >= bArr2.length) {
            throw new IllegalArgumentException(String.format("Destination array with length %d cannot have offset of %d and still store three bytes.", Integer.valueOf(bArr2.length), Integer.valueOf(i2)));
        }
        byte[] c2 = c(i3);
        int i6 = i + 2;
        if (bArr[i6] == 61) {
            bArr2[i2] = (byte) ((((c2[bArr[i + 1]] & 255) << 12) | ((c2[bArr[i]] & 255) << 18)) >>> 16);
            return 1;
        }
        if (bArr[i4] == 61) {
            int i7 = ((c2[bArr[i6]] & 255) << 6) | ((c2[bArr[i + 1]] & 255) << 12) | ((c2[bArr[i]] & 255) << 18);
            bArr2[i2] = (byte) (i7 >>> 16);
            bArr2[i2 + 1] = (byte) (i7 >>> 8);
            return 2;
        }
        int i8 = (c2[bArr[i4]] & 255) | ((c2[bArr[i + 1]] & 255) << 12) | ((c2[bArr[i]] & 255) << 18) | ((c2[bArr[i6]] & 255) << 6);
        bArr2[i2] = (byte) (i8 >> 16);
        bArr2[i2 + 1] = (byte) (i8 >> 8);
        bArr2[i5] = (byte) i8;
        return 3;
    }

    private static final byte[] b(int i) {
        return (i & 16) == 16 ? d : (i & 32) == 32 ? f : b;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static byte[] b(byte[] bArr, int i, int i2, int i3) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream;
        a aVar;
        GZIPOutputStream gZIPOutputStream;
        if (bArr == null) {
            throw new NullPointerException("Cannot serialize a null array.");
        }
        if (i < 0) {
            throw new IllegalArgumentException("Cannot have negative offset: " + i);
        }
        if (i2 < 0) {
            throw new IllegalArgumentException("Cannot have length offset: " + i2);
        }
        if (i + i2 > bArr.length) {
            throw new IllegalArgumentException(String.format("Cannot have offset of %d and length of %d with array of length %d", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(bArr.length)));
        }
        if ((i3 & 2) == 0) {
            boolean z = (i3 & 8) != 0;
            int i4 = ((i2 / 3) * 4) + (i2 % 3 > 0 ? 4 : 0);
            if (z) {
                i4 += i4 / 76;
            }
            byte[] bArr2 = new byte[i4];
            int i5 = i2 - 2;
            int i6 = 0;
            int i7 = 0;
            int i8 = 0;
            while (i6 < i5) {
                int i9 = i6;
                a(bArr, i6 + i, 3, bArr2, i7, i3);
                int i10 = i8 + 4;
                if (!z || i10 < 76) {
                    i8 = i10;
                } else {
                    bArr2[i7 + 4] = 10;
                    i7++;
                    i8 = 0;
                }
                i6 = i9 + 3;
                i7 += 4;
            }
            int i11 = i6;
            if (i11 < i2) {
                a(bArr, i11 + i, i2 - i11, bArr2, i7, i3);
                i7 += 4;
            }
            int i12 = i7;
            if (i12 > bArr2.length - 1) {
                return bArr2;
            }
            byte[] bArr3 = new byte[i12];
            System.arraycopy(bArr2, 0, bArr3, 0, i12);
            return bArr3;
        }
        GZIPOutputStream gZIPOutputStream2 = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                aVar = new a(byteArrayOutputStream, i3 | 1);
                try {
                    gZIPOutputStream = new GZIPOutputStream(aVar);
                } catch (IOException e2) {
                    e = e2;
                    gZIPOutputStream = null;
                } catch (Throwable th) {
                    th = th;
                    try {
                        gZIPOutputStream2.close();
                    } catch (Exception unused) {
                    }
                    try {
                        aVar.close();
                    } catch (Exception unused2) {
                    }
                    try {
                        byteArrayOutputStream.close();
                        throw th;
                    } catch (Exception unused3) {
                        throw th;
                    }
                }
            } catch (IOException e3) {
                e = e3;
                aVar = null;
                gZIPOutputStream = null;
            } catch (Throwable th2) {
                th = th2;
                aVar = null;
            }
        } catch (IOException e4) {
            e = e4;
            aVar = null;
            gZIPOutputStream = null;
        } catch (Throwable th3) {
            th = th3;
            byteArrayOutputStream = 0;
            aVar = null;
        }
        try {
            gZIPOutputStream.write(bArr, i, i2);
            gZIPOutputStream.close();
            try {
                gZIPOutputStream.close();
            } catch (Exception unused4) {
            }
            try {
                aVar.close();
            } catch (Exception unused5) {
            }
            try {
                byteArrayOutputStream.close();
            } catch (Exception unused6) {
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e5) {
            e = e5;
            gZIPOutputStream2 = byteArrayOutputStream;
            try {
                throw e;
            } catch (Throwable th4) {
                th = th4;
                byteArrayOutputStream = gZIPOutputStream2;
                gZIPOutputStream2 = gZIPOutputStream;
                gZIPOutputStream2.close();
                aVar.close();
                byteArrayOutputStream.close();
                throw th;
            }
        } catch (Throwable th5) {
            th = th5;
            gZIPOutputStream2 = gZIPOutputStream;
            gZIPOutputStream2.close();
            aVar.close();
            byteArrayOutputStream.close();
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] b(byte[] bArr, byte[] bArr2, int i, int i2) {
        a(bArr2, 0, i, bArr, 0, i2);
        return bArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final byte[] c(int i) {
        return (i & 16) == 16 ? e : (i & 32) == 32 ? g : c;
    }

    public static byte[] c(byte[] bArr, int i, int i2, int i3) throws IOException {
        int i4;
        if (bArr == null) {
            throw new NullPointerException("Cannot decode null source array.");
        }
        if (i < 0 || (i4 = i + i2) > bArr.length) {
            throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and process %d bytes.", Integer.valueOf(bArr.length), Integer.valueOf(i), Integer.valueOf(i2)));
        }
        if (i2 == 0) {
            return new byte[0];
        }
        if (i2 < 4) {
            throw new IllegalArgumentException("Base64Util-encoded string must have at least four characters, but length specified was " + i2);
        }
        byte[] c2 = c(i3);
        byte[] bArr2 = new byte[(i2 * 3) / 4];
        byte[] bArr3 = new byte[4];
        int i5 = 0;
        int i6 = 0;
        while (i < i4) {
            byte b2 = c2[bArr[i] & 255];
            if (b2 < -5) {
                throw new IOException(String.format("Bad Base64Util input character decimal %d in array position %d", Integer.valueOf(bArr[i] & 255), Integer.valueOf(i)));
            }
            if (b2 >= -1) {
                int i7 = i5 + 1;
                bArr3[i5] = bArr[i];
                if (i7 > 3) {
                    i6 += b(bArr3, 0, bArr2, i6, i3);
                    if (bArr[i] == 61) {
                        break;
                    }
                    i5 = 0;
                } else {
                    i5 = i7;
                }
            }
            i++;
        }
        byte[] bArr4 = new byte[i6];
        System.arraycopy(bArr2, 0, bArr4, 0, i6);
        return bArr4;
    }
}
