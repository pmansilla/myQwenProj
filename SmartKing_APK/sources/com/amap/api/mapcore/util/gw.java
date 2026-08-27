package com.amap.api.mapcore.util;

import android.content.Context;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* compiled from: AbstractBasicLbsRestHandler.java */
/* loaded from: classes.dex */
public abstract class gw<T, V> extends gv<T, V> {
    public gw(Context context, T t) {
        super(context, t);
    }

    @Override // com.amap.api.mapcore.util.gv
    protected V d() {
        return null;
    }

    protected abstract String e();

    @Override // com.amap.api.mapcore.util.ix
    public byte[] getEntityBytes() {
        try {
            return e().getBytes("utf-8");
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    @Override // com.amap.api.mapcore.util.eq, com.amap.api.mapcore.util.ix
    public Map<String, String> getParams() {
        return null;
    }

    @Override // com.amap.api.mapcore.util.eq, com.amap.api.mapcore.util.ix
    public Map<String, String> getRequestHead() {
        HashMap hashMap = new HashMap(16);
        hashMap.put("Content-Type", " application/json");
        hashMap.put("Accept-Encoding", "gzip");
        hashMap.put("User-Agent", "AMAP SDK Android Trace 6.9.3");
        hashMap.put("x-INFO", hg.b(this.f));
        hashMap.put("platinfo", String.format(Locale.US, "platform=Android&sdkversion=%s&product=%s", "6.9.3", "trace"));
        hashMap.put("logversion", "2.1");
        return hashMap;
    }
}
