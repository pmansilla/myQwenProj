package com.amap.api.mapcore.util;

import android.content.Context;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

/* compiled from: CustomStyleRequest.java */
/* loaded from: classes.dex */
public class eg extends gv<String, a> {
    private String h;
    private boolean i;

    /* compiled from: CustomStyleRequest.java */
    /* loaded from: classes.dex */
    public static class a {
        public byte[] a;
        public int b = -1;
    }

    public eg(Context context, String str) {
        super(context, str);
        this.i = false;
        this.g = "/map/styles";
    }

    public eg(Context context, String str, boolean z) {
        super(context, str);
        this.i = false;
        this.i = z;
        if (!z) {
            this.g = "/map/styles";
        } else {
            this.g = "/sdk/map/styles";
            this.isPostFlag = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.amap.api.mapcore.util.gv
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public a b(String str) throws gu {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.amap.api.mapcore.util.gv
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public a b(byte[] bArr) throws gu {
        a aVar = new a();
        aVar.a = bArr;
        if (this.i && bArr != null) {
            if (bArr.length == 0) {
                aVar.a = null;
            } else if (aVar.a.length <= 1024) {
                try {
                    if (new String(bArr, "utf-8").contains("errcode")) {
                        aVar.a = null;
                    }
                } catch (Exception e) {
                    ic.c(e, "CustomStyleRequest", "loadData");
                }
            }
        }
        return aVar;
    }

    public void c(String str) {
        this.h = str;
    }

    @Override // com.amap.api.mapcore.util.eq, com.amap.api.mapcore.util.ix
    public Map<String, String> getParams() {
        Hashtable hashtable = new Hashtable(16);
        hashtable.put("key", hd.f(this.f));
        if (!this.i) {
            hashtable.put("output", "bin");
        }
        hashtable.put("styleid", this.h);
        String a2 = hg.a();
        String a3 = hg.a(this.f, a2, hp.c(hashtable));
        hashtable.put("ts", a2);
        hashtable.put("scode", a3);
        return hashtable;
    }

    @Override // com.amap.api.mapcore.util.eq, com.amap.api.mapcore.util.ix
    public Map<String, String> getRequestHead() {
        ho e = fr.e();
        String b = e != null ? e.b() : null;
        Hashtable hashtable = new Hashtable(16);
        hashtable.put("User-Agent", w.c);
        hashtable.put("Accept-Encoding", "gzip");
        hashtable.put("platinfo", String.format(Locale.US, "platform=Android&sdkversion=%s&product=%s", b, "3dmap"));
        hashtable.put("x-INFO", hg.a(this.f));
        hashtable.put("key", hd.f(this.f));
        hashtable.put("logversion", "2.1");
        return hashtable;
    }

    @Override // com.amap.api.mapcore.util.ix
    public String getURL() {
        return "http://restapi.amap.com/v4" + this.g;
    }
}
