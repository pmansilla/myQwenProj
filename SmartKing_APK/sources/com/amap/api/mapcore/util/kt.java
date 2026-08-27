package com.amap.api.mapcore.util;

import android.content.Context;
import java.util.Map;

/* compiled from: LocationRequest.java */
/* loaded from: classes.dex */
public final class kt extends it {
    Map<String, String> f;
    String g;
    byte[] h;
    byte[] i;
    boolean j;
    String k;
    Map<String, String> l;
    boolean m;
    private String n;

    public kt(Context context, ho hoVar) {
        super(context, hoVar);
        this.f = null;
        this.n = "";
        this.g = "";
        this.h = null;
        this.i = null;
        this.j = false;
        this.k = null;
        this.l = null;
        this.m = false;
    }

    public final void a() {
        this.j = true;
    }

    public final void a(String str) {
        this.k = str;
    }

    public final void a(Map<String, String> map) {
        this.l = map;
    }

    public final void b(String str) {
        this.g = str;
    }

    public final void b(Map<String, String> map) {
        this.f = map;
    }

    public final void b(byte[] bArr) {
        this.h = bArr;
    }

    @Override // com.amap.api.mapcore.util.it
    public final byte[] d() {
        return this.h;
    }

    @Override // com.amap.api.mapcore.util.it
    public final byte[] e() {
        return this.i;
    }

    @Override // com.amap.api.mapcore.util.it
    public final boolean g() {
        return this.j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.amap.api.mapcore.util.ix
    public final String getIPDNSName() {
        return this.n;
    }

    @Override // com.amap.api.mapcore.util.it, com.amap.api.mapcore.util.ix
    public final Map<String, String> getParams() {
        return this.l;
    }

    @Override // com.amap.api.mapcore.util.ix
    public final Map<String, String> getRequestHead() {
        return this.f;
    }

    @Override // com.amap.api.mapcore.util.ix
    public final String getURL() {
        return this.g;
    }

    @Override // com.amap.api.mapcore.util.it
    public final String i() {
        return this.k;
    }

    @Override // com.amap.api.mapcore.util.it
    protected final boolean j() {
        return this.m;
    }

    public final void k() {
        this.m = true;
    }
}
