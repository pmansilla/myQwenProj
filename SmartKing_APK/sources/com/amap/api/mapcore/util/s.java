package com.amap.api.mapcore.util;

import android.content.Context;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

/* compiled from: AuthRequest.java */
/* loaded from: classes.dex */
public class s extends gv<String, a> {
    private int[] h;

    /* compiled from: AuthRequest.java */
    /* loaded from: classes.dex */
    public static class a {
        public String b;
        public String c;
        public int a = -1;
        public boolean d = false;
    }

    public s(Context context, String str) {
        super(context, str);
        this.h = new int[]{10000, 0, 10018, 10019, 10020, 10021, 10022, 10023};
        this.g = "/feedback";
        this.isPostFlag = false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x003b, code lost:
    
        r0.d = true;
     */
    @Override // com.amap.api.mapcore.util.gv
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.amap.api.mapcore.util.s.a b(java.lang.String r6) throws com.amap.api.mapcore.util.gu {
        /*
            r5 = this;
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch: java.lang.Throwable -> L43
            r0.<init>(r6)     // Catch: java.lang.Throwable -> L43
            r6 = -1
            java.lang.String r1 = ""
            java.lang.String r2 = ""
            java.lang.String r3 = "errcode"
            boolean r3 = r0.has(r3)     // Catch: java.lang.Throwable -> L43
            if (r3 == 0) goto L24
            java.lang.String r6 = "errcode"
            int r6 = r0.optInt(r6)     // Catch: java.lang.Throwable -> L43
            java.lang.String r1 = "errmsg"
            java.lang.String r1 = r0.optString(r1)     // Catch: java.lang.Throwable -> L43
            java.lang.String r2 = "errdetail"
            java.lang.String r2 = r0.optString(r2)     // Catch: java.lang.Throwable -> L43
        L24:
            com.amap.api.mapcore.util.s$a r0 = new com.amap.api.mapcore.util.s$a     // Catch: java.lang.Throwable -> L43
            r0.<init>()     // Catch: java.lang.Throwable -> L43
            r0.a = r6     // Catch: java.lang.Throwable -> L43
            r0.b = r1     // Catch: java.lang.Throwable -> L43
            r0.c = r2     // Catch: java.lang.Throwable -> L43
            r1 = 0
            r0.d = r1     // Catch: java.lang.Throwable -> L43
            int[] r2 = r5.h     // Catch: java.lang.Throwable -> L43
            int r3 = r2.length     // Catch: java.lang.Throwable -> L43
        L35:
            if (r1 >= r3) goto L42
            r4 = r2[r1]     // Catch: java.lang.Throwable -> L43
            if (r4 != r6) goto L3f
            r6 = 1
            r0.d = r6     // Catch: java.lang.Throwable -> L43
            goto L42
        L3f:
            int r1 = r1 + 1
            goto L35
        L42:
            return r0
        L43:
            r6 = move-exception
            r6.printStackTrace()
            r6 = 0
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.s.b(java.lang.String):com.amap.api.mapcore.util.s$a");
    }

    @Override // com.amap.api.mapcore.util.eq, com.amap.api.mapcore.util.ix
    public Map<String, String> getParams() {
        Hashtable hashtable = new Hashtable(16);
        hashtable.put("key", hd.f(this.f));
        hashtable.put("pname", "3dmap");
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
