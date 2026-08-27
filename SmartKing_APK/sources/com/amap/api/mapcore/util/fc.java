package com.amap.api.mapcore.util;

/* compiled from: IntArray.java */
/* loaded from: classes.dex */
public class fc {
    public int[] a;
    public int b;
    public boolean c;

    public fc() {
        this(true, 16);
    }

    public fc(boolean z, int i) {
        this.c = z;
        this.a = new int[i];
    }

    public void a() {
        this.b = 0;
    }

    public void a(int i) {
        int[] iArr = this.a;
        if (this.b == iArr.length) {
            iArr = d(Math.max(8, (int) (this.b * 1.75f)));
        }
        int i2 = this.b;
        this.b = i2 + 1;
        iArr[i2] = i;
    }

    public int b(int i) {
        if (i >= this.b) {
            throw new IndexOutOfBoundsException("index can't be >= size: " + i + " >= " + this.b);
        }
        int[] iArr = this.a;
        int i2 = iArr[i];
        this.b--;
        if (this.c) {
            System.arraycopy(iArr, i + 1, iArr, i, this.b - i);
        } else {
            iArr[i] = iArr[this.b];
        }
        return i2;
    }

    public int[] c(int i) {
        int i2 = this.b + i;
        if (i2 > this.a.length) {
            d(Math.max(8, i2));
        }
        return this.a;
    }

    protected int[] d(int i) {
        int[] iArr = new int[i];
        System.arraycopy(this.a, 0, iArr, 0, Math.min(this.b, iArr.length));
        this.a = iArr;
        return iArr;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof fc)) {
            return false;
        }
        fc fcVar = (fc) obj;
        int i = this.b;
        if (i != fcVar.b) {
            return false;
        }
        for (int i2 = 0; i2 < i; i2++) {
            if (this.a[i2] != fcVar.a[i2]) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        return 42;
    }

    public String toString() {
        if (this.b == 0) {
            return "[]";
        }
        int[] iArr = this.a;
        StringBuilder sb = new StringBuilder(32);
        sb.append('[');
        sb.append(iArr[0]);
        for (int i = 1; i < this.b; i++) {
            sb.append(", ");
            sb.append(iArr[i]);
        }
        sb.append(']');
        return sb.toString();
    }
}
