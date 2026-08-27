package com.amap.api.mapcore.util;

import java.util.Map;

/* compiled from: ADIURequest.java */
/* loaded from: classes.dex */
public class ir extends ix {
    private byte[] d;
    private Map<String, String> e;

    public ir(byte[] bArr, Map<String, String> map) {
        this.d = bArr;
        this.e = map;
    }

    @Override // com.amap.api.mapcore.util.ix
    public byte[] getEntityBytes() {
        return this.d;
    }

    @Override // com.amap.api.mapcore.util.ix
    public Map<String, String> getParams() {
        return this.e;
    }

    @Override // com.amap.api.mapcore.util.ix
    public Map<String, String> getRequestHead() {
        return null;
    }

    @Override // com.amap.api.mapcore.util.ix
    public String getURL() {
        return "https://adiu.amap.com/ws/device/adius";
    }
}
