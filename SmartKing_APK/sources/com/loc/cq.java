package com.loc;

import java.util.HashMap;
import java.util.Map;

/* compiled from: CoRequest.java */
/* loaded from: classes.dex */
public final class cq extends bj {
    private String b = null;
    private Map<String, String> f = new HashMap();
    byte[] a = null;

    public final void a(String str) {
        this.b = str;
    }

    public final void a(Map<String, String> map) {
        this.f = map;
    }

    @Override // com.loc.bj
    public final Map<String, String> b() {
        return this.f;
    }

    @Override // com.loc.bj
    public final Map<String, String> b_() {
        return null;
    }

    @Override // com.loc.bj
    public final String c() {
        return this.b;
    }

    @Override // com.loc.bj
    public final byte[] d() {
        return this.a;
    }

    @Override // com.loc.bj
    public final boolean l() {
        return true;
    }
}
