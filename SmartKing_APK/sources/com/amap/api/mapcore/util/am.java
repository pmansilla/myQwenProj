package com.amap.api.mapcore.util;

import com.amap.api.maps.model.BitmapDescriptor;

/* compiled from: OverlayTextureItem.java */
/* loaded from: classes.dex */
public class am {
    private BitmapDescriptor b;
    private int c;
    private float d = 0.0f;
    private float e = 0.0f;
    private float f = 1.0f;
    private float g = 1.0f;
    private boolean h = false;
    private float i = 0.0f;
    private float j = 0.0f;
    private float k = 0.0f;
    private float l = 0.0f;
    private int m = 0;
    private String a = fj.a();

    public am(BitmapDescriptor bitmapDescriptor, int i) {
        this.b = bitmapDescriptor;
        this.c = i;
    }

    public void a(float f) {
        this.k = f;
    }

    public void a(int i) {
        this.c = i;
    }

    public void a(boolean z) {
        this.h = z;
    }

    public boolean a() {
        return this.h;
    }

    public float b() {
        return this.k;
    }

    public void b(float f) {
        this.l = f;
    }

    public float c() {
        return this.l;
    }

    public void c(float f) {
        this.i = f;
    }

    public float d() {
        return this.i;
    }

    public void d(float f) {
        this.j = f;
    }

    public float e() {
        return this.j;
    }

    public void e(float f) {
        this.e = f;
    }

    public float f() {
        return this.e;
    }

    public void f(float f) {
        this.d = f;
    }

    public float g() {
        return this.d;
    }

    public void g(float f) {
        this.f = f;
    }

    public float h() {
        return this.f;
    }

    public void h(float f) {
        this.g = f;
    }

    public float i() {
        return this.g;
    }

    public BitmapDescriptor j() {
        return this.b;
    }

    public int k() {
        return this.c;
    }

    public void l() {
        this.m++;
    }

    public void m() {
        this.m--;
    }

    public int n() {
        return this.m;
    }

    public String o() {
        return this.a;
    }
}
