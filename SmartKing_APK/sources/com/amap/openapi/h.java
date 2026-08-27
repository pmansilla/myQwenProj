package com.amap.openapi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* compiled from: FpsBufferBuilder.java */
/* loaded from: classes.dex */
public class h extends g {
    public h() {
        super(2048);
    }

    private int a(long j, @NonNull List<aa> list) {
        a(list);
        int size = list.size();
        if (size <= 0) {
            return -1;
        }
        int[] iArr = new int[size];
        for (int i = 0; i < size; i++) {
            aa aaVar = list.get(i);
            iArr[i] = ar.a(this.a, aaVar.a == j && aaVar.a != -1, aaVar.a, aaVar.b, this.a.a(aaVar.c), aaVar.d, aaVar.f);
        }
        return aq.a(this.a, aq.a(this.a, iArr));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:13:0x00eb A[LOOP:0: B:4:0x0014->B:13:0x00eb, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:14:0x00ff A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int a(@android.support.annotation.NonNull com.amap.openapi.q r21) {
        /*
            Method dump skipped, instructions count: 383
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.h.a(com.amap.openapi.q):int");
    }

    private int a(@NonNull v vVar) {
        return aj.a(this.a, vVar.a, vVar.b, vVar.c, vVar.d, vVar.e, vVar.f, vVar.g, vVar.h, vVar.i);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void a(ArrayList<r> arrayList) {
        int i;
        int i2;
        if (arrayList == null || arrayList.size() == 0) {
            return;
        }
        Iterator<r> it = arrayList.iterator();
        while (it.hasNext()) {
            r next = it.next();
            if (next.a == 1) {
                w wVar = (w) next.f;
                i = wVar.c;
                i2 = wVar.d;
            } else if (next.a == 3) {
                x xVar = (x) next.f;
                i = xVar.c;
                i2 = xVar.d;
            } else if (next.a == 4) {
                z zVar = (z) next.f;
                i = zVar.c;
                i2 = zVar.d;
            } else if (next.a == 2) {
                p pVar = (p) next.f;
                i = pVar.b;
                i2 = pVar.c;
            }
            next.d = as.a(as.a(i, i2));
        }
    }

    private void a(@NonNull List<aa> list) {
        for (aa aaVar : list) {
            aaVar.d = as.b(aaVar.a);
        }
    }

    @Nullable
    public byte[] a(@NonNull Context context, @NonNull v vVar, @Nullable q qVar, long j, @Nullable List<aa> list) {
        super.a();
        try {
            int a = a(vVar);
            int i = -1;
            int a2 = (qVar == null || qVar.c.size() <= 0) ? -1 : a(qVar);
            if (list != null && list.size() > 0) {
                i = a(j, list);
            }
            ab.a(this.a);
            ab.a(this.a, a);
            if (a2 > 0) {
                ab.c(this.a, a2);
            }
            if (i > 0) {
                ab.b(this.a, i);
            }
            this.a.h(ab.b(this.a));
            return aw.a(az.a(context), com.amap.location.common.util.d.a(this.a.f()));
        } catch (Throwable unused) {
            return null;
        }
    }
}
