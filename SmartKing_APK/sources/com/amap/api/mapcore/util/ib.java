package com.amap.api.mapcore.util;

import com.amap.location.common.model.AmapLoc;
import java.util.HashMap;
import java.util.Map;
import no.nordicsemi.android.dfu.DfuBaseService;

/* compiled from: LogUpdateRequest.java */
/* loaded from: classes.dex */
public class ib extends ix {
    private byte[] d;
    private String e;

    public ib(byte[] bArr, String str) {
        this.e = AmapLoc.RESULT_TYPE_WIFI_ONLY;
        this.d = (byte[]) bArr.clone();
        this.e = str;
    }

    private String a() {
        byte[] a = hp.a(hx.b);
        byte[] bArr = new byte[a.length + 50];
        System.arraycopy(this.d, 0, bArr, 0, 50);
        System.arraycopy(a, 0, bArr, 50, a.length);
        return hl.a(bArr);
    }

    @Override // com.amap.api.mapcore.util.ix
    public byte[] getEntityBytes() {
        return this.d;
    }

    @Override // com.amap.api.mapcore.util.ix
    public Map<String, String> getParams() {
        return null;
    }

    @Override // com.amap.api.mapcore.util.ix
    public Map<String, String> getRequestHead() {
        HashMap hashMap = new HashMap();
        hashMap.put("Content-Type", DfuBaseService.MIME_TYPE_ZIP);
        hashMap.put("Content-Length", String.valueOf(this.d.length));
        return hashMap;
    }

    @Override // com.amap.api.mapcore.util.ix
    public String getURL() {
        return String.format(hp.c(hx.c), AmapLoc.RESULT_TYPE_WIFI_ONLY, this.e, AmapLoc.RESULT_TYPE_WIFI_ONLY, "open", a());
    }
}
