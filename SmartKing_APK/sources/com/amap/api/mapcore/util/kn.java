package com.amap.api.mapcore.util;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.lang.time.DateUtils;

/* compiled from: CellAgeEstimator.java */
/* loaded from: classes.dex */
public final class kn {
    private HashMap<Long, ko> a = new HashMap<>();
    private long b = 0;

    private static long a(int i, int i2) {
        return (i2 & 65535) | ((i & 65535) << 32);
    }

    public final long a(ko koVar) {
        int a;
        int b;
        long a2;
        if (koVar == null || !koVar.p) {
            return 0L;
        }
        HashMap<Long, ko> hashMap = this.a;
        switch (koVar.k) {
            case 1:
            case 3:
            case 4:
                a = koVar.a();
                b = koVar.b();
                a2 = a(a, b);
                break;
            case 2:
                a = koVar.c();
                b = koVar.d();
                a2 = a(a, b);
                break;
            default:
                a2 = 0;
                break;
        }
        ko koVar2 = hashMap.get(Long.valueOf(a2));
        if (koVar2 == null) {
            koVar.m = la.b();
            hashMap.put(Long.valueOf(a2), koVar);
            return 0L;
        }
        if (koVar2.e() != koVar.e()) {
            koVar.m = la.b();
            hashMap.put(Long.valueOf(a2), koVar);
            return 0L;
        }
        koVar.m = koVar2.m;
        hashMap.put(Long.valueOf(a2), koVar);
        return (la.b() - koVar2.m) / 1000;
    }

    public final void a() {
        this.a.clear();
        this.b = 0L;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:13:0x0031. Please report as an issue. */
    public final void a(ArrayList<? extends ko> arrayList) {
        int a;
        int b;
        int i;
        int i2;
        if (arrayList != null) {
            long b2 = la.b();
            if (this.b <= 0 || b2 - this.b >= DateUtils.MILLIS_PER_MINUTE) {
                HashMap<Long, ko> hashMap = this.a;
                int size = arrayList.size();
                long j = 0;
                for (int i3 = 0; i3 < size; i3++) {
                    ko koVar = arrayList.get(i3);
                    if (koVar.p) {
                        switch (koVar.k) {
                            case 1:
                            case 3:
                            case 4:
                                i = koVar.c;
                                i2 = koVar.d;
                                j = a(i, i2);
                                break;
                            case 2:
                                i = koVar.h;
                                i2 = koVar.i;
                                j = a(i, i2);
                                break;
                        }
                        ko koVar2 = hashMap.get(Long.valueOf(j));
                        if (koVar2 != null) {
                            if (koVar2.e() == koVar.e()) {
                                koVar.m = koVar2.m;
                            } else {
                                koVar.m = b2;
                            }
                        }
                    }
                }
                hashMap.clear();
                int size2 = arrayList.size();
                for (int i4 = 0; i4 < size2; i4++) {
                    ko koVar3 = arrayList.get(i4);
                    if (koVar3.p) {
                        switch (koVar3.k) {
                            case 1:
                            case 3:
                            case 4:
                                a = koVar3.a();
                                b = koVar3.b();
                                break;
                            case 2:
                                a = koVar3.c();
                                b = koVar3.d();
                                break;
                        }
                        j = a(a, b);
                        hashMap.put(Long.valueOf(j), koVar3);
                        continue;
                    }
                }
                this.b = b2;
            }
        }
    }
}
