package com.amap.api.mapcore.util;

/* compiled from: EarClippingTriangulator.java */
/* loaded from: classes.dex */
public class ew {
    private short[] b;
    private double[] c;
    private int d;
    private final fn a = new fn();
    private final fc e = new fc();
    private final fn f = new fn();

    private static int a(double d, double d2, double d3, double d4, double d5, double d6) {
        return (int) Math.signum((d * (d6 - d4)) + (d3 * (d2 - d6)) + (d5 * (d4 - d2)));
    }

    private int a(int i) {
        short[] sArr = this.b;
        int i2 = sArr[d(i)] * 2;
        int i3 = sArr[i] * 2;
        int i4 = sArr[e(i)] * 2;
        double[] dArr = this.c;
        return a(dArr[i2], dArr[i2 + 1], dArr[i3], dArr[i3 + 1], dArr[i4], dArr[i4 + 1]);
    }

    private void a() {
        int[] iArr = this.e.a;
        while (this.d > 3) {
            int b = b();
            c(b);
            int d = d(b);
            if (b == this.d) {
                b = 0;
            }
            iArr[d] = a(d);
            iArr[b] = a(b);
        }
        if (this.d == 3) {
            fn fnVar = this.f;
            short[] sArr = this.b;
            fnVar.a(sArr[0]);
            fnVar.a(sArr[1]);
            fnVar.a(sArr[2]);
        }
    }

    private int b() {
        int i = this.d;
        for (int i2 = 0; i2 < i; i2++) {
            if (b(i2)) {
                return i2;
            }
        }
        int[] iArr = this.e.a;
        for (int i3 = 0; i3 < i; i3++) {
            if (iArr[i3] != -1) {
                return i3;
            }
        }
        return 0;
    }

    private boolean b(int i) {
        int[] iArr = this.e.a;
        if (iArr[i] == -1) {
            return false;
        }
        int d = d(i);
        int e = e(i);
        short[] sArr = this.b;
        int i2 = sArr[d] * 2;
        int i3 = sArr[i] * 2;
        int i4 = sArr[e] * 2;
        double[] dArr = this.c;
        double d2 = dArr[i2];
        int i5 = 1;
        double d3 = dArr[i2 + 1];
        double d4 = dArr[i3];
        double d5 = dArr[i3 + 1];
        double d6 = dArr[i4];
        double d7 = dArr[i4 + 1];
        int e2 = e(e);
        while (e2 != d) {
            if (iArr[e2] != i5) {
                int i6 = sArr[e2] * 2;
                double d8 = dArr[i6];
                double d9 = dArr[i6 + i5];
                if (a(d6, d7, d2, d3, d8, d9) >= 0 && a(d2, d3, d4, d5, d8, d9) >= 0 && a(d4, d5, d6, d7, d8, d9) >= 0) {
                    return false;
                }
            }
            e2 = e(e2);
            i5 = 1;
        }
        return true;
    }

    private void c(int i) {
        short[] sArr = this.b;
        fn fnVar = this.f;
        fnVar.a(sArr[d(i)]);
        fnVar.a(sArr[i]);
        fnVar.a(sArr[e(i)]);
        this.a.b(i);
        this.e.b(i);
        this.d--;
    }

    private int d(int i) {
        if (i == 0) {
            i = this.d;
        }
        return i - 1;
    }

    private int e(int i) {
        return (i + 1) % this.d;
    }

    public fn a(double[] dArr) {
        return a(dArr, 0, dArr.length);
    }

    public fn a(double[] dArr, int i, int i2) {
        this.c = dArr;
        int i3 = i2 / 2;
        this.d = i3;
        int i4 = i / 2;
        fn fnVar = this.a;
        fnVar.a();
        fnVar.c(i3);
        fnVar.b = i3;
        short[] sArr = fnVar.a;
        this.b = sArr;
        int i5 = i3 - 1;
        for (int i6 = 0; i6 < i3; i6++) {
            sArr[i6] = (short) ((i4 + i5) - i6);
        }
        fc fcVar = this.e;
        fcVar.a();
        fcVar.c(i3);
        for (int i7 = 0; i7 < i3; i7++) {
            fcVar.a(a(i7));
        }
        fn fnVar2 = this.f;
        fnVar2.a();
        fnVar2.c(Math.max(0, i3 - 2) * 3);
        a();
        return fnVar2;
    }
}
