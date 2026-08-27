package com.loc;

import java.io.UnsupportedEncodingException;

/* compiled from: Base64.java */
/* loaded from: classes.dex */
public class dt {
    static final /* synthetic */ boolean a = !dt.class.desiredAssertionStatus();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: Base64.java */
    /* loaded from: classes.dex */
    public static abstract class a {
        public byte[] a;
        public int b;

        a() {
        }
    }

    /* compiled from: Base64.java */
    /* loaded from: classes.dex */
    static class b extends a {
        private static final int[] c = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private static final int[] d = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private int e;
        private int f;
        private final int[] g;

        public b(byte[] bArr) {
            this.a = bArr;
            this.g = c;
            this.e = 0;
            this.f = 0;
        }

        /* JADX WARN: Failed to find 'out' block for switch in B:27:0x0066. Please report as an issue. */
        /* JADX WARN: Removed duplicated region for block: B:17:0x00e0  */
        /* JADX WARN: Removed duplicated region for block: B:19:0x00e3  */
        /* JADX WARN: Removed duplicated region for block: B:20:0x00f2  */
        /* JADX WARN: Removed duplicated region for block: B:21:0x00fb  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final boolean a(byte[] r13, int r14) {
            /*
                Method dump skipped, instructions count: 290
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.loc.dt.b.a(byte[], int):boolean");
        }
    }

    /* compiled from: Base64.java */
    /* loaded from: classes.dex */
    static class c extends a {
        static final /* synthetic */ boolean g = !dt.class.desiredAssertionStatus();
        private static final byte[] h = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
        private static final byte[] i = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
        int c;
        public final boolean d;
        public final boolean e;
        public final boolean f;
        private final byte[] j;
        private int k;
        private final byte[] l;

        public c(int i2) {
            this.a = null;
            this.d = (i2 & 1) == 0;
            this.e = (i2 & 2) == 0;
            this.f = (i2 & 4) != 0;
            this.l = (i2 & 8) == 0 ? h : i;
            this.j = new byte[2];
            this.c = 0;
            this.k = this.e ? 19 : -1;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        public final boolean a(byte[] bArr, int i2) {
            int i3;
            int i4;
            int i5;
            int i6;
            byte b;
            int i7;
            byte b2;
            int i8;
            int i9;
            byte b3;
            int i10;
            int i11;
            int i12;
            byte[] bArr2 = this.l;
            byte[] bArr3 = this.a;
            int i13 = this.k;
            int i14 = 0;
            int i15 = i2 + 0;
            switch (this.c) {
                case 0:
                default:
                    i3 = -1;
                    i4 = 0;
                    break;
                case 1:
                    if (2 <= i15) {
                        i3 = ((this.j[0] & 255) << 16) | ((bArr[0] & 255) << 8) | (bArr[1] & 255);
                        this.c = 0;
                        i4 = 2;
                        break;
                    }
                    i3 = -1;
                    i4 = 0;
                    break;
                case 2:
                    if (i15 > 0) {
                        i3 = ((this.j[0] & 255) << 16) | ((this.j[1] & 255) << 8) | (bArr[0] & 255);
                        this.c = 0;
                        i4 = 1;
                        break;
                    }
                    i3 = -1;
                    i4 = 0;
                    break;
            }
            if (i3 != -1) {
                bArr3[0] = bArr2[(i3 >> 18) & 63];
                bArr3[1] = bArr2[(i3 >> 12) & 63];
                bArr3[2] = bArr2[(i3 >> 6) & 63];
                bArr3[3] = bArr2[i3 & 63];
                i13--;
                if (i13 == 0) {
                    if (this.f) {
                        i12 = 5;
                        bArr3[4] = 13;
                    } else {
                        i12 = 4;
                    }
                    i5 = i12 + 1;
                    bArr3[i12] = 10;
                    i13 = 19;
                } else {
                    i5 = 4;
                }
            } else {
                i5 = 0;
            }
            while (true) {
                int i16 = i4 + 3;
                if (i16 > i15) {
                    if (i4 - this.c == i15 - 1) {
                        if (this.c > 0) {
                            b3 = this.j[0];
                            i14 = 1;
                        } else {
                            b3 = bArr[i4];
                            i4++;
                        }
                        int i17 = (b3 & 255) << 4;
                        this.c -= i14;
                        int i18 = i5 + 1;
                        bArr3[i5] = bArr2[(i17 >> 6) & 63];
                        i5 = i18 + 1;
                        bArr3[i18] = bArr2[i17 & 63];
                        if (this.d) {
                            int i19 = i5 + 1;
                            bArr3[i5] = 61;
                            i5 = i19 + 1;
                            bArr3[i19] = 61;
                        }
                        if (this.e) {
                            if (this.f) {
                                i10 = i5 + 1;
                                bArr3[i5] = 13;
                            } else {
                                i10 = i5;
                            }
                            i9 = i10 + 1;
                            bArr3[i10] = 10;
                            i5 = i9;
                        }
                    } else if (i4 - this.c == i15 - 2) {
                        if (this.c > 1) {
                            b = this.j[0];
                            i7 = 1;
                        } else {
                            byte b4 = bArr[i4];
                            i4++;
                            b = b4;
                            i7 = 0;
                        }
                        int i20 = (b & 255) << 10;
                        if (this.c > 0) {
                            b2 = this.j[i7];
                            i7++;
                        } else {
                            b2 = bArr[i4];
                            i4++;
                        }
                        int i21 = ((b2 & 255) << 2) | i20;
                        this.c -= i7;
                        int i22 = i5 + 1;
                        bArr3[i5] = bArr2[(i21 >> 12) & 63];
                        int i23 = i22 + 1;
                        bArr3[i22] = bArr2[(i21 >> 6) & 63];
                        int i24 = i23 + 1;
                        bArr3[i23] = bArr2[i21 & 63];
                        if (this.d) {
                            i8 = i24 + 1;
                            bArr3[i24] = 61;
                        } else {
                            i8 = i24;
                        }
                        if (this.e) {
                            if (this.f) {
                                bArr3[i8] = 13;
                                i8++;
                            }
                            i9 = i8 + 1;
                            bArr3[i8] = 10;
                            i5 = i9;
                        } else {
                            i5 = i8;
                        }
                    } else if (this.e && i5 > 0 && i13 != 19) {
                        if (this.f) {
                            i6 = i5 + 1;
                            bArr3[i5] = 13;
                        } else {
                            i6 = i5;
                        }
                        i5 = i6 + 1;
                        bArr3[i6] = 10;
                    }
                    if (!g && this.c != 0) {
                        throw new AssertionError();
                    }
                    if (!g && i4 != i15) {
                        throw new AssertionError();
                    }
                    this.b = i5;
                    this.k = i13;
                    return true;
                }
                int i25 = ((bArr[i4 + 1] & 255) << 8) | ((bArr[i4] & 255) << 16) | (bArr[i4 + 2] & 255);
                bArr3[i5] = bArr2[(i25 >> 18) & 63];
                bArr3[i5 + 1] = bArr2[(i25 >> 12) & 63];
                bArr3[i5 + 2] = bArr2[(i25 >> 6) & 63];
                bArr3[i5 + 3] = bArr2[i25 & 63];
                i5 += 4;
                i13--;
                if (i13 == 0) {
                    if (this.f) {
                        i11 = i5 + 1;
                        bArr3[i5] = 13;
                    } else {
                        i11 = i5;
                    }
                    i5 = i11 + 1;
                    bArr3[i11] = 10;
                    i4 = i16;
                    i13 = 19;
                } else {
                    i4 = i16;
                }
            }
        }
    }

    private dt() {
    }

    public static String a(byte[] bArr, int i) {
        try {
            int length = bArr.length;
            c cVar = new c(i);
            int i2 = (length / 3) * 4;
            if (!cVar.d) {
                switch (length % 3) {
                    case 1:
                        i2 += 2;
                        break;
                    case 2:
                        i2 += 3;
                        break;
                }
            } else if (length % 3 > 0) {
                i2 += 4;
            }
            if (cVar.e && length > 0) {
                i2 += (((length - 1) / 57) + 1) * (cVar.f ? 2 : 1);
            }
            cVar.a = new byte[i2];
            cVar.a(bArr, length);
            if (!a && cVar.b != i2) {
                throw new AssertionError();
            }
            return new String(cVar.a, "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static byte[] a(String str) {
        byte[] bytes = str.getBytes();
        int length = bytes.length;
        b bVar = new b(new byte[(length * 3) / 4]);
        if (!bVar.a(bytes, length)) {
            throw new IllegalArgumentException("bad base-64");
        }
        if (bVar.b == bVar.a.length) {
            return bVar.a;
        }
        byte[] bArr = new byte[bVar.b];
        System.arraycopy(bVar.a, 0, bArr, 0, bVar.b);
        return bArr;
    }
}
