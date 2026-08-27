package com.amap.openapi;

import com.loc.fc;
import com.loc.fd;

/* compiled from: TWifiInfo.java */
/* loaded from: classes.dex */
public final class ar extends fd {
    public static int a(fc fcVar) {
        return fcVar.e();
    }

    public static int a(fc fcVar, boolean z, long j, short s, int i, short s2, short s3) {
        fcVar.f(6);
        a(fcVar, j);
        a(fcVar, i);
        c(fcVar, s3);
        b(fcVar, s2);
        a(fcVar, s);
        a(fcVar, z);
        return a(fcVar);
    }

    public static void a(fc fcVar, int i) {
        fcVar.c(3, i, 0);
    }

    public static void a(fc fcVar, long j) {
        fcVar.a(1, j, 0L);
    }

    public static void a(fc fcVar, short s) {
        fcVar.a(2, s, 0);
    }

    public static void a(fc fcVar, boolean z) {
        fcVar.a(0, z, false);
    }

    public static void b(fc fcVar, short s) {
        fcVar.a(4, s, 0);
    }

    public static void c(fc fcVar, short s) {
        fcVar.a(5, s, 0);
    }
}
