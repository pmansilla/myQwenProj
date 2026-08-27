package com.amap.api.mapcore.util;

import android.content.Context;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* compiled from: CustomStyleTextureRequest.java */
/* loaded from: classes.dex */
public class ei extends gv<String, a> {

    /* compiled from: CustomStyleTextureRequest.java */
    /* loaded from: classes.dex */
    public static class a {
        public byte[] a;
        public int b = -1;
    }

    public ei(Context context, String str) {
        super(context, str);
        this.g = "/map/styles";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.amap.api.mapcore.util.gv
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public a b(byte[] bArr) throws gu {
        a aVar = new a();
        aVar.a = bArr;
        return aVar;
    }

    public void a(String str) {
        this.g = str;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.amap.api.mapcore.util.gv
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public a b(String str) throws gu {
        return null;
    }

    @Override // com.amap.api.mapcore.util.eq, com.amap.api.mapcore.util.ix
    public Map<String, String> getParams() {
        HashMap hashMap = new HashMap(16);
        hashMap.put("key", hd.f(this.f));
        hashMap.put("output", "bin");
        String a2 = hg.a();
        String a3 = hg.a(this.f, a2, hp.c(hashMap));
        hashMap.put("ts", a2);
        hashMap.put("scode", a3);
        return hashMap;
    }

    @Override // com.amap.api.mapcore.util.eq, com.amap.api.mapcore.util.ix
    public Map<String, String> getRequestHead() {
        ho e = fr.e();
        String b = e != null ? e.b() : null;
        HashMap hashMap = new HashMap(16);
        hashMap.put("User-Agent", w.c);
        hashMap.put("Accept-Encoding", "gzip");
        hashMap.put("platinfo", String.format(Locale.US, "platform=Android&sdkversion=%s&product=%s", b, "3dmap"));
        hashMap.put("x-INFO", hg.a(this.f));
        hashMap.put("key", hd.f(this.f));
        hashMap.put("logversion", "2.1");
        return hashMap;
    }

    @Override // com.amap.api.mapcore.util.ix
    public String getURL() {
        return this.g;
    }
}
