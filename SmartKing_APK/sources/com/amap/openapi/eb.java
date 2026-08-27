package com.amap.openapi;

import android.support.annotation.NonNull;
import kotlin.jvm.internal.LongCompanionObject;
import org.apache.commons.lang.time.DateUtils;

/* compiled from: CountConfigProxy.java */
/* loaded from: classes.dex */
public class eb implements dp {
    private dp a;

    public eb(@NonNull dp dpVar) {
        this.a = dpVar;
    }

    @Override // com.amap.openapi.dp
    public long a() {
        return com.amap.location.common.util.b.a(this.a.a(), 0L, LongCompanionObject.MAX_VALUE);
    }

    @Override // com.amap.openapi.dr
    public long a(int i) {
        return com.amap.location.common.util.b.a(this.a.a(i), 1000L, 10000000L);
    }

    @Override // com.amap.openapi.dr
    public long b(int i) {
        return com.amap.location.common.util.b.a(this.a.b(i), 0L, 50000000L);
    }

    @Override // com.amap.openapi.dr
    public void b() {
        this.a.b();
    }

    @Override // com.amap.openapi.dr
    public long c() {
        return com.amap.location.common.util.b.a(this.a.c(), 0L, 1000000L);
    }

    @Override // com.amap.openapi.dr
    public boolean c(int i) {
        return this.a.c(i);
    }

    @Override // com.amap.openapi.dr
    public long d() {
        return com.amap.location.common.util.b.a(this.a.d(), DateUtils.MILLIS_PER_MINUTE, DateUtils.MILLIS_PER_DAY);
    }

    @Override // com.amap.openapi.dr
    public long e() {
        return com.amap.location.common.util.b.a(this.a.e(), 1000L, DateUtils.MILLIS_PER_HOUR);
    }

    @Override // com.amap.openapi.dr
    public int f() {
        return com.amap.location.common.util.b.a(this.a.f(), 1000, 600000);
    }

    @Override // com.amap.openapi.dr
    public long g() {
        return com.amap.location.common.util.b.a(this.a.g(), 0L, 50000000L);
    }

    @Override // com.amap.openapi.dr
    public long h() {
        return com.amap.location.common.util.b.a(this.a.h(), 0L, LongCompanionObject.MAX_VALUE);
    }
}
