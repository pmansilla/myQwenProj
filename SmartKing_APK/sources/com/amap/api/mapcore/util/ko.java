package com.amap.api.mapcore.util;

import android.support.v4.os.EnvironmentCompat;
import java.util.Locale;

/* compiled from: Cgi.java */
/* loaded from: classes.dex */
public final class ko {
    public int k;
    public boolean n;
    public int a = 0;
    public int b = 0;
    public int c = 0;
    public int d = 0;
    public int e = 0;
    public int f = 0;
    public int g = 0;
    public int h = 0;
    public int i = 0;
    public int j = -113;
    public short l = 0;
    public long m = 0;
    public int o = 32767;
    public boolean p = true;

    public ko(int i, boolean z) {
        this.k = 0;
        this.n = false;
        this.k = i;
        this.n = z;
    }

    public final int a() {
        return this.c;
    }

    public final int b() {
        return this.d;
    }

    public final int c() {
        return this.h;
    }

    public final int d() {
        return this.i;
    }

    public final int e() {
        return this.j;
    }

    public final boolean equals(Object obj) {
        if (obj != null && (obj instanceof ko)) {
            ko koVar = (ko) obj;
            switch (koVar.k) {
                case 1:
                    if (this.k == 1 && koVar.c == this.c && koVar.d == this.d && koVar.b == this.b) {
                        return true;
                    }
                    break;
                case 2:
                    return this.k == 2 && koVar.i == this.i && koVar.h == this.h && koVar.g == this.g;
                case 3:
                    return this.k == 3 && koVar.c == this.c && koVar.d == this.d && koVar.b == this.b;
                case 4:
                    return this.k == 4 && koVar.c == this.c && koVar.d == this.d && koVar.b == this.b;
                default:
                    return false;
            }
        }
        return false;
    }

    public final int hashCode() {
        int hashCode;
        int i;
        int hashCode2 = String.valueOf(this.k).hashCode();
        if (this.k == 2) {
            hashCode = String.valueOf(this.i).hashCode() + String.valueOf(this.h).hashCode();
            i = this.g;
        } else {
            hashCode = String.valueOf(this.c).hashCode() + String.valueOf(this.d).hashCode();
            i = this.b;
        }
        return hashCode2 + hashCode + String.valueOf(i).hashCode();
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x000e. Please report as an issue. */
    public final String toString() {
        Locale locale;
        String str;
        Object[] objArr;
        switch (this.k) {
            case 1:
                locale = Locale.CHINA;
                str = "GSM lac=%d, cid=%d, mnc=%s, valid=%b, sig=%d, age=%d, reg=%b";
                objArr = new Object[]{Integer.valueOf(this.c), Integer.valueOf(this.d), Integer.valueOf(this.b), Boolean.valueOf(this.p), Integer.valueOf(this.j), Short.valueOf(this.l), Boolean.valueOf(this.n)};
                return String.format(locale, str, objArr);
            case 2:
                locale = Locale.CHINA;
                str = "CDMA bid=%d, nid=%d, sid=%d, valid=%b, sig=%d, age=%d, reg=%b";
                objArr = new Object[]{Integer.valueOf(this.i), Integer.valueOf(this.h), Integer.valueOf(this.g), Boolean.valueOf(this.p), Integer.valueOf(this.j), Short.valueOf(this.l), Boolean.valueOf(this.n)};
                return String.format(locale, str, objArr);
            case 3:
                locale = Locale.CHINA;
                str = "LTE lac=%d, cid=%d, mnc=%s, valid=%b, sig=%d, age=%d, reg=%b, pci=%d";
                objArr = new Object[]{Integer.valueOf(this.c), Integer.valueOf(this.d), Integer.valueOf(this.b), Boolean.valueOf(this.p), Integer.valueOf(this.j), Short.valueOf(this.l), Boolean.valueOf(this.n), Integer.valueOf(this.o)};
                return String.format(locale, str, objArr);
            case 4:
                locale = Locale.CHINA;
                str = "WCDMA lac=%d, cid=%d, mnc=%s, valid=%b, sig=%d, age=%d, reg=%b, pci=%d";
                objArr = new Object[]{Integer.valueOf(this.c), Integer.valueOf(this.d), Integer.valueOf(this.b), Boolean.valueOf(this.p), Integer.valueOf(this.j), Short.valueOf(this.l), Boolean.valueOf(this.n), Integer.valueOf(this.o)};
                return String.format(locale, str, objArr);
            default:
                return EnvironmentCompat.MEDIA_UNKNOWN;
        }
    }
}
