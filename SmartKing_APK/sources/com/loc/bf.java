package com.loc;

import java.util.Map;

/* compiled from: ADIURequest.java */
/* loaded from: classes.dex */
public final class bf extends bj {
    private byte[] a;
    private Map<String, String> b;

    public bf(byte[] bArr, Map<String, String> map) {
        this.a = bArr;
        this.b = map;
    }

    @Override // com.loc.bj
    public final Map<String, String> b() {
        return null;
    }

    @Override // com.loc.bj
    public final Map<String, String> b_() {
        return this.b;
    }

    @Override // com.loc.bj
    public final String c() {
        return "https://adiu.amap.com/ws/device/adius";
    }

    @Override // com.loc.bj
    public final byte[] d() {
        return this.a;
    }
}
