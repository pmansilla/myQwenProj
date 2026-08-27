package com.amap.api.mapcore.util;

import java.util.Hashtable;
import java.util.Map;

/* compiled from: OfflineDownloadRequest.java */
/* loaded from: classes.dex */
public class cq extends eq {
    private String d;

    public cq(String str) {
        this.d = str;
    }

    @Override // com.amap.api.mapcore.util.eq, com.amap.api.mapcore.util.ix
    public Map<String, String> getParams() {
        return null;
    }

    @Override // com.amap.api.mapcore.util.eq, com.amap.api.mapcore.util.ix
    public Map<String, String> getRequestHead() {
        Hashtable hashtable = new Hashtable(32);
        hashtable.put("User-Agent", "MAC=channel:amapapi");
        return hashtable;
    }

    @Override // com.amap.api.mapcore.util.ix
    public String getURL() {
        return this.d;
    }
}
