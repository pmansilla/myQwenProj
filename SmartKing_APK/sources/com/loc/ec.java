package com.loc;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.lang.time.DateUtils;

/* compiled from: CellAgeEstimator.java */
/* loaded from: classes.dex */
public final class ec {
    private HashMap<Long, ed> a = new HashMap<>();
    private long b = 0;

    private static long a(int i, int i2) {
        return (i2 & 65535) | ((i & 65535) << 32);
    }

    public final long a(ed edVar) {
        int i;
        int i2;
        long a;
        if (edVar == null || !edVar.p) {
            return 0L;
        }
        HashMap<Long, ed> hashMap = this.a;
        switch (edVar.k) {
            case 1:
            case 3:
            case 4:
                i = edVar.c;
                i2 = edVar.d;
                a = a(i, i2);
                break;
            case 2:
                i = edVar.h;
                i2 = edVar.i;
                a = a(i, i2);
                break;
            default:
                a = 0;
                break;
        }
        ed edVar2 = hashMap.get(Long.valueOf(a));
        if (edVar2 == null) {
            edVar.m = fa.c();
            hashMap.put(Long.valueOf(a), edVar);
            return 0L;
        }
        if (edVar2.j != edVar.j) {
            edVar.m = fa.c();
            hashMap.put(Long.valueOf(a), edVar);
            return 0L;
        }
        edVar.m = edVar2.m;
        hashMap.put(Long.valueOf(a), edVar);
        return (fa.c() - edVar2.m) / 1000;
    }

    public final void a() {
        this.a.clear();
        this.b = 0L;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:13:0x0031. Please report as an issue. */
    public final void a(ArrayList<? extends ed> arrayList) {
        int i;
        int i2;
        int i3;
        int i4;
        if (arrayList != null) {
            long c = fa.c();
            if (this.b <= 0 || c - this.b >= DateUtils.MILLIS_PER_MINUTE) {
                HashMap<Long, ed> hashMap = this.a;
                int size = arrayList.size();
                long j = 0;
                for (int i5 = 0; i5 < size; i5++) {
                    ed edVar = arrayList.get(i5);
                    if (edVar.p) {
                        switch (edVar.k) {
                            case 1:
                            case 3:
                            case 4:
                                i3 = edVar.c;
                                i4 = edVar.d;
                                j = a(i3, i4);
                                break;
                            case 2:
                                i3 = edVar.h;
                                i4 = edVar.i;
                                j = a(i3, i4);
                                break;
                        }
                        ed edVar2 = hashMap.get(Long.valueOf(j));
                        if (edVar2 != null) {
                            if (edVar2.j == edVar.j) {
                                edVar.m = edVar2.m;
                            } else {
                                edVar.m = c;
                            }
                        }
                    }
                }
                hashMap.clear();
                int size2 = arrayList.size();
                for (int i6 = 0; i6 < size2; i6++) {
                    ed edVar3 = arrayList.get(i6);
                    if (edVar3.p) {
                        switch (edVar3.k) {
                            case 1:
                            case 3:
                            case 4:
                                i = edVar3.c;
                                i2 = edVar3.d;
                                break;
                            case 2:
                                i = edVar3.h;
                                i2 = edVar3.i;
                                break;
                        }
                        j = a(i, i2);
                        hashMap.put(Long.valueOf(j), edVar3);
                        continue;
                    }
                }
                this.b = c;
            }
        }
    }
}
