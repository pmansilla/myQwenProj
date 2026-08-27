package com.loc;

import com.amap.location.common.model.AmapLoc;
import java.util.HashMap;
import java.util.Map;
import no.nordicsemi.android.dfu.DfuBaseService;

/* compiled from: LogUpdateRequest.java */
/* loaded from: classes.dex */
public final class ap extends bj {
    private byte[] a;
    private String b;

    public ap(byte[] bArr, String str) {
        this.b = AmapLoc.RESULT_TYPE_WIFI_ONLY;
        this.a = (byte[]) bArr.clone();
        this.b = str;
    }

    @Override // com.loc.bj
    public final Map<String, String> b() {
        HashMap hashMap = new HashMap();
        hashMap.put("Content-Type", DfuBaseService.MIME_TYPE_ZIP);
        hashMap.put("Content-Length", String.valueOf(this.a.length));
        return hashMap;
    }

    @Override // com.loc.bj
    public final Map<String, String> b_() {
        return null;
    }

    @Override // com.loc.bj
    public final String c() {
        String c = ad.c(al.c);
        byte[] a = ad.a(al.b);
        byte[] bArr = new byte[a.length + 50];
        System.arraycopy(this.a, 0, bArr, 0, 50);
        System.arraycopy(a, 0, bArr, 50, a.length);
        return String.format(c, AmapLoc.RESULT_TYPE_WIFI_ONLY, this.b, AmapLoc.RESULT_TYPE_WIFI_ONLY, "open", aa.a(bArr));
    }

    @Override // com.loc.bj
    public final byte[] d() {
        return this.a;
    }
}
