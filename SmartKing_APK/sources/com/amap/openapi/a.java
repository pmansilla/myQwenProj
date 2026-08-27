package com.amap.openapi;

import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: CloudCommand.java */
/* loaded from: classes.dex */
public class a {
    protected String b;
    protected int c;
    protected long d;
    protected C0029a e;
    private final long f = 300000;
    private final long g = 259200000;
    protected long a = 43200000;

    /* compiled from: CloudCommand.java */
    /* renamed from: com.amap.openapi.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public class C0029a {
        private JSONObject b;

        C0029a(String str) throws JSONException {
            this.b = new JSONObject(str);
        }
    }

    public String a() {
        return this.b;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0046  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean a(java.lang.String r7) {
        /*
            r6 = this;
            r0 = 1
            java.lang.String r1 = "$"
            int r1 = r7.indexOf(r1)     // Catch: java.lang.Exception -> L42
            int r1 = r1 + r0
            int r2 = r7.length()     // Catch: java.lang.Exception -> L42
            java.lang.String r7 = r7.substring(r1, r2)     // Catch: java.lang.Exception -> L42
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch: java.lang.Exception -> L43
            r1.<init>(r7)     // Catch: java.lang.Exception -> L43
            java.lang.String r2 = "p"
            r3 = 43200000(0x2932e00, double:2.1343636E-316)
            long r2 = r1.optLong(r2, r3)     // Catch: java.lang.Exception -> L43
            r4 = 259200000(0xf731400, double:1.280618154E-315)
            long r2 = java.lang.Math.min(r2, r4)     // Catch: java.lang.Exception -> L43
            r6.a = r2     // Catch: java.lang.Exception -> L43
            long r2 = r6.a     // Catch: java.lang.Exception -> L43
            r4 = 300000(0x493e0, double:1.482197E-318)
            long r2 = java.lang.Math.max(r2, r4)     // Catch: java.lang.Exception -> L43
            r6.a = r2     // Catch: java.lang.Exception -> L43
            java.lang.String r2 = "v"
            int r1 = r1.optInt(r2)     // Catch: java.lang.Exception -> L43
            r6.c = r1     // Catch: java.lang.Exception -> L43
            com.amap.openapi.a$a r1 = new com.amap.openapi.a$a     // Catch: java.lang.Exception -> L43
            r1.<init>(r7)     // Catch: java.lang.Exception -> L43
            r6.e = r1     // Catch: java.lang.Exception -> L43
            goto L44
        L42:
            r7 = 0
        L43:
            r0 = 0
        L44:
            if (r0 == 0) goto L48
            r6.b = r7
        L48:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.a.a(java.lang.String):boolean");
    }
}
