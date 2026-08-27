package com.loc;

import java.util.Map;

/* compiled from: HttpRequest.java */
/* loaded from: classes.dex */
public final class em extends bj {
    Map<String, String> a = null;
    Map<String, String> b = null;
    String f = "";
    byte[] g = null;

    public final void a(String str) {
        this.f = str;
    }

    public final void a(Map<String, String> map) {
        this.a = map;
    }

    @Override // com.loc.bj
    public final Map<String, String> b() {
        return this.a;
    }

    public final void b(Map<String, String> map) {
        this.b = map;
    }

    @Override // com.loc.bj
    public final Map<String, String> b_() {
        return this.b;
    }

    @Override // com.loc.bj
    public final String c() {
        return this.f;
    }

    @Override // com.loc.bj
    public final byte[] d() {
        return this.g;
    }
}
