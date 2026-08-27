package com.loc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClientOption;
import com.autonavi.aps.amapapi.model.AMapLocationServer;
import java.util.ArrayList;
import java.util.Hashtable;
import org.json.JSONObject;

/* compiled from: Cache.java */
/* loaded from: classes.dex */
public final class ei {
    Hashtable<String, ArrayList<a>> a = new Hashtable<>();
    private long i = 0;
    private boolean j = false;
    private String k = "2.0.201501131131".replace(".", "");
    private String l = null;
    boolean b = true;
    long c = 0;
    String d = null;
    ed e = null;
    private String m = null;
    private long n = 0;
    boolean f = true;
    boolean g = true;
    String h = String.valueOf(AMapLocationClientOption.GeoLanguage.DEFAULT);

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: Cache.java */
    /* loaded from: classes.dex */
    public static class a {
        private AMapLocationServer a = null;
        private String b = null;

        protected a() {
        }

        public final AMapLocationServer a() {
            return this.a;
        }

        public final void a(AMapLocationServer aMapLocationServer) {
            this.a = aMapLocationServer;
        }

        public final void a(String str) {
            this.b = TextUtils.isEmpty(str) ? null : str.replace("##", "#");
        }

        public final String b() {
            return this.b;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x005e A[Catch: Throwable -> 0x0085, TryCatch #0 {Throwable -> 0x0085, blocks: (B:3:0x0001, B:5:0x0009, B:7:0x003a, B:9:0x0044, B:11:0x005e, B:13:0x0064, B:14:0x0068, B:17:0x006d, B:19:0x0071, B:21:0x0079, B:23:0x000e, B:26:0x0017, B:28:0x001f, B:30:0x0027), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:17:0x006d A[Catch: Throwable -> 0x0085, TryCatch #0 {Throwable -> 0x0085, blocks: (B:3:0x0001, B:5:0x0009, B:7:0x003a, B:9:0x0044, B:11:0x005e, B:13:0x0064, B:14:0x0068, B:17:0x006d, B:19:0x0071, B:21:0x0079, B:23:0x000e, B:26:0x0017, B:28:0x001f, B:30:0x0027), top: B:2:0x0001 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private com.autonavi.aps.amapapi.model.AMapLocationServer a(java.lang.String r5, java.lang.StringBuilder r6) {
        /*
            r4 = this;
            r0 = 0
            java.lang.String r1 = "cgiwifi"
            boolean r1 = r5.contains(r1)     // Catch: java.lang.Throwable -> L85
            if (r1 == 0) goto Le
        L9:
            com.loc.ei$a r6 = r4.a(r6, r5)     // Catch: java.lang.Throwable -> L85
            goto L38
        Le:
            java.lang.String r1 = "wifi"
            boolean r1 = r5.contains(r1)     // Catch: java.lang.Throwable -> L85
            if (r1 == 0) goto L17
            goto L9
        L17:
            java.lang.String r6 = "cgi"
            boolean r6 = r5.contains(r6)     // Catch: java.lang.Throwable -> L85
            if (r6 == 0) goto L37
            java.util.Hashtable<java.lang.String, java.util.ArrayList<com.loc.ei$a>> r6 = r4.a     // Catch: java.lang.Throwable -> L85
            boolean r6 = r6.containsKey(r5)     // Catch: java.lang.Throwable -> L85
            if (r6 == 0) goto L37
            java.util.Hashtable<java.lang.String, java.util.ArrayList<com.loc.ei$a>> r6 = r4.a     // Catch: java.lang.Throwable -> L85
            java.lang.Object r6 = r6.get(r5)     // Catch: java.lang.Throwable -> L85
            java.util.ArrayList r6 = (java.util.ArrayList) r6     // Catch: java.lang.Throwable -> L85
            r1 = 0
            java.lang.Object r6 = r6.get(r1)     // Catch: java.lang.Throwable -> L85
            com.loc.ei$a r6 = (com.loc.ei.a) r6     // Catch: java.lang.Throwable -> L85
            goto L38
        L37:
            r6 = r0
        L38:
            if (r6 == 0) goto L8d
            com.autonavi.aps.amapapi.model.AMapLocationServer r1 = r6.a()     // Catch: java.lang.Throwable -> L85
            boolean r1 = com.loc.fa.a(r1)     // Catch: java.lang.Throwable -> L85
            if (r1 == 0) goto L8d
            com.autonavi.aps.amapapi.model.AMapLocationServer r1 = r6.a()     // Catch: java.lang.Throwable -> L85
            java.lang.String r2 = "mem"
            r1.e(r2)     // Catch: java.lang.Throwable -> L85
            java.lang.String r2 = r6.b()     // Catch: java.lang.Throwable -> L85
            r1.h(r2)     // Catch: java.lang.Throwable -> L85
            long r2 = r1.getTime()     // Catch: java.lang.Throwable -> L85
            boolean r2 = com.loc.er.b(r2)     // Catch: java.lang.Throwable -> L85
            if (r2 == 0) goto L6d
            boolean r5 = com.loc.fa.a(r1)     // Catch: java.lang.Throwable -> L85
            if (r5 == 0) goto L68
            r5 = 0
            r4.c = r5     // Catch: java.lang.Throwable -> L85
        L68:
            r5 = 4
            r1.setLocationType(r5)     // Catch: java.lang.Throwable -> L85
            return r1
        L6d:
            java.util.Hashtable<java.lang.String, java.util.ArrayList<com.loc.ei$a>> r1 = r4.a     // Catch: java.lang.Throwable -> L85
            if (r1 == 0) goto L8d
            java.util.Hashtable<java.lang.String, java.util.ArrayList<com.loc.ei$a>> r1 = r4.a     // Catch: java.lang.Throwable -> L85
            boolean r1 = r1.containsKey(r5)     // Catch: java.lang.Throwable -> L85
            if (r1 == 0) goto L8d
            java.util.Hashtable<java.lang.String, java.util.ArrayList<com.loc.ei$a>> r1 = r4.a     // Catch: java.lang.Throwable -> L85
            java.lang.Object r5 = r1.get(r5)     // Catch: java.lang.Throwable -> L85
            java.util.ArrayList r5 = (java.util.ArrayList) r5     // Catch: java.lang.Throwable -> L85
            r5.remove(r6)     // Catch: java.lang.Throwable -> L85
            goto L8d
        L85:
            r5 = move-exception
            java.lang.String r6 = "Cache"
            java.lang.String r1 = "get1"
            com.loc.es.a(r5, r6, r1)
        L8d:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ei.a(java.lang.String, java.lang.StringBuilder):com.autonavi.aps.amapapi.model.AMapLocationServer");
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x00bb  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00ed A[LOOP:1: B:32:0x00e7->B:34:0x00ed, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0107 A[LOOP:2: B:37:0x0101->B:39:0x0107, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0143  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0150  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0152 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0146  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0177  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x00cd  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private com.loc.ei.a a(java.lang.StringBuilder r26, java.lang.String r27) {
        /*
            Method dump skipped, instructions count: 407
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ei.a(java.lang.StringBuilder, java.lang.String):com.loc.ei$a");
    }

    private String a(String str, StringBuilder sb, Context context) {
        String substring;
        String str2;
        if (context == null) {
            return null;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            if (this.l == null) {
                this.l = eh.a("MD5", u.c(context));
            }
            if (str.contains("&")) {
                str = str.substring(0, str.indexOf("&"));
            }
            String substring2 = str.substring(str.lastIndexOf("#") + 1);
            if (!substring2.equals("cgi")) {
                if (!TextUtils.isEmpty(sb) && sb.indexOf(",access") != -1) {
                    jSONObject.put("cgi", str.substring(0, str.length() - (substring2.length() + 9)));
                    String[] split = sb.toString().split(",access");
                    substring = split[0].contains("#") ? split[0].substring(split[0].lastIndexOf("#") + 1) : split[0];
                    str2 = "mmac";
                }
                return y.b(eh.c(jSONObject.toString().getBytes("UTF-8"), this.l));
            }
            str2 = "cgi";
            substring = str.substring(0, str.length() - 12);
            jSONObject.put(str2, substring);
            return y.b(eh.c(jSONObject.toString().getBytes("UTF-8"), this.l));
        } catch (Throwable unused) {
            return null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:30:0x008f, code lost:
    
        if (r12.moveToFirst() != false) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x009d, code lost:
    
        if (r12.getString(0).startsWith("{") == false) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x009f, code lost:
    
        r4 = new org.json.JSONObject(r12.getString(0));
        r0.delete(0, r0.length());
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00b7, code lost:
    
        if (android.text.TextUtils.isEmpty(r12.getString(1)) != false) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00b9, code lost:
    
        r6 = r12.getString(1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00bd, code lost:
    
        r0.append(r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00da, code lost:
    
        r6 = new org.json.JSONObject(r12.getString(2));
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00e9, code lost:
    
        if (com.loc.fa.a(r6, "type") == false) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00eb, code lost:
    
        r6.put("type", com.amap.location.common.model.AmapLoc.TYPE_NEW);
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x017d, code lost:
    
        r7 = new com.autonavi.aps.amapapi.model.AMapLocationServer("");
        r7.b(r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x018d, code lost:
    
        if (com.loc.fa.a(r4, "mmac") == false) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0195, code lost:
    
        if (com.loc.fa.a(r4, "cgi") == false) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0197, code lost:
    
        r5 = (r4.getString("cgi") + "#") + "network#";
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x01cb, code lost:
    
        if (r4.getString("cgi").contains("#") == false) goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x01cd, code lost:
    
        r4 = new java.lang.StringBuilder();
        r4.append(r5);
        r4.append("cgiwifi");
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x023a, code lost:
    
        a(r4.toString(), r0, r7, r11, false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0245, code lost:
    
        if (r12.moveToNext() != false) goto L106;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x0247, code lost:
    
        r0.delete(0, r0.length());
        r3.delete(0, r3.length());
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x01e0, code lost:
    
        r4 = new java.lang.StringBuilder();
        r4.append(r5);
        r4.append("wifi");
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x01f4, code lost:
    
        if (com.loc.fa.a(r4, "cgi") == false) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x01f6, code lost:
    
        r5 = (r4.getString("cgi") + "#") + "network#";
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x022a, code lost:
    
        if (r4.getString("cgi").contains("#") == false) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x022c, code lost:
    
        r4 = new java.lang.StringBuilder();
        r4.append(r5);
        r4.append("cgi");
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00c7, code lost:
    
        if (com.loc.fa.a(r4, "mmac") == false) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x00c9, code lost:
    
        r0.append("#");
        r0.append(r4.getString("mmac"));
        r6 = ",access";
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00f4, code lost:
    
        r7 = new org.json.JSONObject(new java.lang.String(com.loc.eh.d(com.loc.y.b(r12.getString(0)), r10.l), "UTF-8"));
        r0.delete(0, r0.length());
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x011d, code lost:
    
        if (android.text.TextUtils.isEmpty(r12.getString(1)) != false) goto L41;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x011f, code lost:
    
        r0.append(new java.lang.String(com.loc.eh.d(com.loc.y.b(r12.getString(1)), r10.l), "UTF-8"));
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0153, code lost:
    
        r6 = new org.json.JSONObject(new java.lang.String(com.loc.eh.d(com.loc.y.b(r12.getString(2)), r10.l), "UTF-8"));
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0173, code lost:
    
        if (com.loc.fa.a(r6, "type") == false) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x0175, code lost:
    
        r6.put("type", com.amap.location.common.model.AmapLoc.TYPE_NEW);
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x017c, code lost:
    
        r4 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x013e, code lost:
    
        if (com.loc.fa.a(r7, "mmac") == false) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0140, code lost:
    
        r0.append("#");
        r0.append(r7.getString("mmac"));
        r0.append(",access");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void a(android.content.Context r11, java.lang.String r12) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 670
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ei.a(android.content.Context, java.lang.String):void");
    }

    private void a(String str, AMapLocation aMapLocation, StringBuilder sb, Context context) throws Exception {
        SQLiteDatabase sQLiteDatabase;
        if (context == null) {
            return;
        }
        if (this.l == null) {
            this.l = eh.a("MD5", u.c(context));
        }
        String a2 = a(str, sb, context);
        StringBuilder sb2 = new StringBuilder();
        SQLiteDatabase sQLiteDatabase2 = null;
        try {
            try {
                sQLiteDatabase = context.openOrCreateDatabase("hmdb", 0, null);
            } catch (Throwable th) {
                th = th;
                sQLiteDatabase = null;
            }
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            sb2.append("CREATE TABLE IF NOT EXISTS hist");
            sb2.append(this.k);
            sb2.append(" (feature VARCHAR PRIMARY KEY, nb VARCHAR, loc VARCHAR, time VARCHAR);");
            sQLiteDatabase.execSQL(sb2.toString());
            sb2.delete(0, sb2.length());
            sb2.append("REPLACE INTO ");
            sb2.append("hist");
            sb2.append(this.k);
            sb2.append(" VALUES (?, ?, ?, ?)");
            Object[] objArr = new Object[4];
            objArr[0] = a2;
            byte[] c = eh.c(sb.toString().getBytes("UTF-8"), this.l);
            objArr[1] = c;
            objArr[2] = eh.c(aMapLocation.toStr().getBytes("UTF-8"), this.l);
            objArr[3] = Long.valueOf(aMapLocation.getTime());
            for (int i = 1; i < 3; i++) {
                objArr[i] = y.b((byte[]) objArr[i]);
            }
            sQLiteDatabase.execSQL(sb2.toString(), objArr);
            sb2.delete(0, sb2.length());
            sb2.delete(0, sb2.length());
            if (sQLiteDatabase == null || !sQLiteDatabase.isOpen()) {
                return;
            }
            sQLiteDatabase.close();
        } catch (Throwable th3) {
            th = th3;
            sb2.delete(0, sb2.length());
            if (sQLiteDatabase != null && sQLiteDatabase.isOpen()) {
                sQLiteDatabase.close();
            }
            throw th;
        }
    }

    private static void a(String str, Hashtable<String, String> hashtable) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        hashtable.clear();
        for (String str2 : str.split("#")) {
            if (!TextUtils.isEmpty(str2) && !str2.contains("|")) {
                hashtable.put(str2, "");
            }
        }
    }

    private static double[] a(double[] dArr, double[] dArr2) {
        double[] dArr3 = new double[3];
        double d = 0.0d;
        double d2 = 0.0d;
        double d3 = 0.0d;
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < dArr.length; i3++) {
            d += dArr[i3] * dArr[i3];
            d2 += dArr2[i3] * dArr2[i3];
            d3 += dArr[i3] * dArr2[i3];
            if (dArr2[i3] == 1.0d) {
                i2++;
                if (dArr[i3] == 1.0d) {
                    i++;
                }
            }
        }
        dArr3[0] = d3 / (Math.sqrt(d) * Math.sqrt(d2));
        double d4 = i;
        Double.isNaN(d4);
        double d5 = i2;
        Double.isNaN(d5);
        dArr3[1] = (d4 * 1.0d) / d5;
        dArr3[2] = d4;
        for (int i4 = 0; i4 < 2; i4++) {
            if (dArr3[i4] > 1.0d) {
                dArr3[i4] = 1.0d;
            }
        }
        return dArr3;
    }

    private boolean b() {
        return this.i != 0 && (this.a.size() > 360 || fa.c() - this.i > 36000000);
    }

    private void c() {
        this.i = 0L;
        if (!this.a.isEmpty()) {
            this.a.clear();
        }
        this.j = false;
    }

    public final AMapLocationServer a(Context context, String str, StringBuilder sb, boolean z) {
        if (TextUtils.isEmpty(str) || !er.o()) {
            return null;
        }
        String str2 = str + "&" + this.f + "&" + this.g + "&" + this.h;
        if (str2.contains("gps") || !er.o() || sb == null) {
            return null;
        }
        if (b()) {
            c();
            return null;
        }
        if (z && !this.j) {
            try {
                String a2 = a(str2, sb, context);
                c();
                a(context, a2);
            } catch (Throwable unused) {
            }
        }
        if (this.a.isEmpty()) {
            return null;
        }
        return a(str2, sb);
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0046 A[Catch: Throwable -> 0x010c, TryCatch #0 {Throwable -> 0x010c, blocks: (B:13:0x002a, B:15:0x0030, B:19:0x0046, B:26:0x0065, B:30:0x006d, B:32:0x007d, B:38:0x00a3, B:39:0x00ac, B:41:0x00b0, B:43:0x00b8, B:45:0x00c4, B:48:0x00e4, B:50:0x00f2, B:58:0x0105, B:63:0x00c7, B:64:0x00cb, B:66:0x00d0, B:68:0x00d4, B:69:0x00d9, B:70:0x00aa, B:71:0x0090, B:73:0x0096, B:78:0x0034, B:80:0x0038), top: B:12:0x002a }] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0065 A[Catch: Throwable -> 0x010c, TryCatch #0 {Throwable -> 0x010c, blocks: (B:13:0x002a, B:15:0x0030, B:19:0x0046, B:26:0x0065, B:30:0x006d, B:32:0x007d, B:38:0x00a3, B:39:0x00ac, B:41:0x00b0, B:43:0x00b8, B:45:0x00c4, B:48:0x00e4, B:50:0x00f2, B:58:0x0105, B:63:0x00c7, B:64:0x00cb, B:66:0x00d0, B:68:0x00d4, B:69:0x00d9, B:70:0x00aa, B:71:0x0090, B:73:0x0096, B:78:0x0034, B:80:0x0038), top: B:12:0x002a }] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00a3 A[Catch: Throwable -> 0x010c, TryCatch #0 {Throwable -> 0x010c, blocks: (B:13:0x002a, B:15:0x0030, B:19:0x0046, B:26:0x0065, B:30:0x006d, B:32:0x007d, B:38:0x00a3, B:39:0x00ac, B:41:0x00b0, B:43:0x00b8, B:45:0x00c4, B:48:0x00e4, B:50:0x00f2, B:58:0x0105, B:63:0x00c7, B:64:0x00cb, B:66:0x00d0, B:68:0x00d4, B:69:0x00d9, B:70:0x00aa, B:71:0x0090, B:73:0x0096, B:78:0x0034, B:80:0x0038), top: B:12:0x002a }] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00b0 A[Catch: Throwable -> 0x010c, TryCatch #0 {Throwable -> 0x010c, blocks: (B:13:0x002a, B:15:0x0030, B:19:0x0046, B:26:0x0065, B:30:0x006d, B:32:0x007d, B:38:0x00a3, B:39:0x00ac, B:41:0x00b0, B:43:0x00b8, B:45:0x00c4, B:48:0x00e4, B:50:0x00f2, B:58:0x0105, B:63:0x00c7, B:64:0x00cb, B:66:0x00d0, B:68:0x00d4, B:69:0x00d9, B:70:0x00aa, B:71:0x0090, B:73:0x0096, B:78:0x0034, B:80:0x0038), top: B:12:0x002a }] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00e2 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00f2 A[Catch: Throwable -> 0x010c, TryCatch #0 {Throwable -> 0x010c, blocks: (B:13:0x002a, B:15:0x0030, B:19:0x0046, B:26:0x0065, B:30:0x006d, B:32:0x007d, B:38:0x00a3, B:39:0x00ac, B:41:0x00b0, B:43:0x00b8, B:45:0x00c4, B:48:0x00e4, B:50:0x00f2, B:58:0x0105, B:63:0x00c7, B:64:0x00cb, B:66:0x00d0, B:68:0x00d4, B:69:0x00d9, B:70:0x00aa, B:71:0x0090, B:73:0x0096, B:78:0x0034, B:80:0x0038), top: B:12:0x002a }] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00ff A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x00d4 A[Catch: Throwable -> 0x010c, TryCatch #0 {Throwable -> 0x010c, blocks: (B:13:0x002a, B:15:0x0030, B:19:0x0046, B:26:0x0065, B:30:0x006d, B:32:0x007d, B:38:0x00a3, B:39:0x00ac, B:41:0x00b0, B:43:0x00b8, B:45:0x00c4, B:48:0x00e4, B:50:0x00f2, B:58:0x0105, B:63:0x00c7, B:64:0x00cb, B:66:0x00d0, B:68:0x00d4, B:69:0x00d9, B:70:0x00aa, B:71:0x0090, B:73:0x0096, B:78:0x0034, B:80:0x0038), top: B:12:0x002a }] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x00d9 A[Catch: Throwable -> 0x010c, TryCatch #0 {Throwable -> 0x010c, blocks: (B:13:0x002a, B:15:0x0030, B:19:0x0046, B:26:0x0065, B:30:0x006d, B:32:0x007d, B:38:0x00a3, B:39:0x00ac, B:41:0x00b0, B:43:0x00b8, B:45:0x00c4, B:48:0x00e4, B:50:0x00f2, B:58:0x0105, B:63:0x00c7, B:64:0x00cb, B:66:0x00d0, B:68:0x00d4, B:69:0x00d9, B:70:0x00aa, B:71:0x0090, B:73:0x0096, B:78:0x0034, B:80:0x0038), top: B:12:0x002a }] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x00aa A[Catch: Throwable -> 0x010c, TryCatch #0 {Throwable -> 0x010c, blocks: (B:13:0x002a, B:15:0x0030, B:19:0x0046, B:26:0x0065, B:30:0x006d, B:32:0x007d, B:38:0x00a3, B:39:0x00ac, B:41:0x00b0, B:43:0x00b8, B:45:0x00c4, B:48:0x00e4, B:50:0x00f2, B:58:0x0105, B:63:0x00c7, B:64:0x00cb, B:66:0x00d0, B:68:0x00d4, B:69:0x00d9, B:70:0x00aa, B:71:0x0090, B:73:0x0096, B:78:0x0034, B:80:0x0038), top: B:12:0x002a }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.autonavi.aps.amapapi.model.AMapLocationServer a(com.loc.ee r18, boolean r19, com.autonavi.aps.amapapi.model.AMapLocationServer r20, com.loc.eg r21, java.lang.StringBuilder r22, java.lang.String r23, android.content.Context r24) {
        /*
            Method dump skipped, instructions count: 269
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ei.a(com.loc.ee, boolean, com.autonavi.aps.amapapi.model.AMapLocationServer, com.loc.eg, java.lang.StringBuilder, java.lang.String, android.content.Context):com.autonavi.aps.amapapi.model.AMapLocationServer");
    }

    public final void a() {
        this.c = 0L;
        this.d = null;
    }

    public final void a(Context context) {
        if (this.j) {
            return;
        }
        try {
            c();
            a(context, (String) null);
        } catch (Throwable th) {
            es.a(th, "Cache", "loadDB");
        }
        this.j = true;
    }

    public final void a(AMapLocationClientOption aMapLocationClientOption) {
        this.g = aMapLocationClientOption.isNeedAddress();
        this.f = aMapLocationClientOption.isOffset();
        this.b = aMapLocationClientOption.isLocationCacheEnable();
        this.h = String.valueOf(aMapLocationClientOption.getGeoLanguage());
    }

    public final void a(ed edVar) {
        this.e = edVar;
    }

    public final void a(String str) {
        this.d = str;
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x0093 A[Catch: Throwable -> 0x01a8, TryCatch #1 {Throwable -> 0x01a8, blocks: (B:2:0x0000, B:6:0x0007, B:8:0x003e, B:11:0x0045, B:14:0x004e, B:17:0x0059, B:20:0x0066, B:23:0x0073, B:26:0x0080, B:29:0x008d, B:31:0x0093, B:32:0x0096, B:34:0x00a2, B:35:0x00aa, B:37:0x00b2, B:40:0x00b9, B:42:0x00c3, B:44:0x00d1, B:46:0x00db, B:48:0x00dd, B:54:0x00f0, B:56:0x00f8, B:58:0x0102, B:60:0x0114, B:61:0x0141, B:63:0x014b, B:66:0x015b, B:69:0x0175, B:71:0x0180, B:78:0x01a0, B:82:0x018c, B:83:0x0171, B:84:0x00e5, B:87:0x0120, B:90:0x012a, B:93:0x0134, B:74:0x019b), top: B:1:0x0000, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00a2 A[Catch: Throwable -> 0x01a8, TryCatch #1 {Throwable -> 0x01a8, blocks: (B:2:0x0000, B:6:0x0007, B:8:0x003e, B:11:0x0045, B:14:0x004e, B:17:0x0059, B:20:0x0066, B:23:0x0073, B:26:0x0080, B:29:0x008d, B:31:0x0093, B:32:0x0096, B:34:0x00a2, B:35:0x00aa, B:37:0x00b2, B:40:0x00b9, B:42:0x00c3, B:44:0x00d1, B:46:0x00db, B:48:0x00dd, B:54:0x00f0, B:56:0x00f8, B:58:0x0102, B:60:0x0114, B:61:0x0141, B:63:0x014b, B:66:0x015b, B:69:0x0175, B:71:0x0180, B:78:0x01a0, B:82:0x018c, B:83:0x0171, B:84:0x00e5, B:87:0x0120, B:90:0x012a, B:93:0x0134, B:74:0x019b), top: B:1:0x0000, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00b2 A[Catch: Throwable -> 0x01a8, TryCatch #1 {Throwable -> 0x01a8, blocks: (B:2:0x0000, B:6:0x0007, B:8:0x003e, B:11:0x0045, B:14:0x004e, B:17:0x0059, B:20:0x0066, B:23:0x0073, B:26:0x0080, B:29:0x008d, B:31:0x0093, B:32:0x0096, B:34:0x00a2, B:35:0x00aa, B:37:0x00b2, B:40:0x00b9, B:42:0x00c3, B:44:0x00d1, B:46:0x00db, B:48:0x00dd, B:54:0x00f0, B:56:0x00f8, B:58:0x0102, B:60:0x0114, B:61:0x0141, B:63:0x014b, B:66:0x015b, B:69:0x0175, B:71:0x0180, B:78:0x01a0, B:82:0x018c, B:83:0x0171, B:84:0x00e5, B:87:0x0120, B:90:0x012a, B:93:0x0134, B:74:0x019b), top: B:1:0x0000, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x016f  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0180 A[Catch: Throwable -> 0x01a8, TryCatch #1 {Throwable -> 0x01a8, blocks: (B:2:0x0000, B:6:0x0007, B:8:0x003e, B:11:0x0045, B:14:0x004e, B:17:0x0059, B:20:0x0066, B:23:0x0073, B:26:0x0080, B:29:0x008d, B:31:0x0093, B:32:0x0096, B:34:0x00a2, B:35:0x00aa, B:37:0x00b2, B:40:0x00b9, B:42:0x00c3, B:44:0x00d1, B:46:0x00db, B:48:0x00dd, B:54:0x00f0, B:56:0x00f8, B:58:0x0102, B:60:0x0114, B:61:0x0141, B:63:0x014b, B:66:0x015b, B:69:0x0175, B:71:0x0180, B:78:0x01a0, B:82:0x018c, B:83:0x0171, B:84:0x00e5, B:87:0x0120, B:90:0x012a, B:93:0x0134, B:74:0x019b), top: B:1:0x0000, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x019b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:81:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:82:0x018c A[Catch: Throwable -> 0x01a8, TRY_LEAVE, TryCatch #1 {Throwable -> 0x01a8, blocks: (B:2:0x0000, B:6:0x0007, B:8:0x003e, B:11:0x0045, B:14:0x004e, B:17:0x0059, B:20:0x0066, B:23:0x0073, B:26:0x0080, B:29:0x008d, B:31:0x0093, B:32:0x0096, B:34:0x00a2, B:35:0x00aa, B:37:0x00b2, B:40:0x00b9, B:42:0x00c3, B:44:0x00d1, B:46:0x00db, B:48:0x00dd, B:54:0x00f0, B:56:0x00f8, B:58:0x0102, B:60:0x0114, B:61:0x0141, B:63:0x014b, B:66:0x015b, B:69:0x0175, B:71:0x0180, B:78:0x01a0, B:82:0x018c, B:83:0x0171, B:84:0x00e5, B:87:0x0120, B:90:0x012a, B:93:0x0134, B:74:0x019b), top: B:1:0x0000, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0171 A[Catch: Throwable -> 0x01a8, TryCatch #1 {Throwable -> 0x01a8, blocks: (B:2:0x0000, B:6:0x0007, B:8:0x003e, B:11:0x0045, B:14:0x004e, B:17:0x0059, B:20:0x0066, B:23:0x0073, B:26:0x0080, B:29:0x008d, B:31:0x0093, B:32:0x0096, B:34:0x00a2, B:35:0x00aa, B:37:0x00b2, B:40:0x00b9, B:42:0x00c3, B:44:0x00d1, B:46:0x00db, B:48:0x00dd, B:54:0x00f0, B:56:0x00f8, B:58:0x0102, B:60:0x0114, B:61:0x0141, B:63:0x014b, B:66:0x015b, B:69:0x0175, B:71:0x0180, B:78:0x01a0, B:82:0x018c, B:83:0x0171, B:84:0x00e5, B:87:0x0120, B:90:0x012a, B:93:0x0134, B:74:0x019b), top: B:1:0x0000, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0120 A[Catch: Throwable -> 0x01a8, TryCatch #1 {Throwable -> 0x01a8, blocks: (B:2:0x0000, B:6:0x0007, B:8:0x003e, B:11:0x0045, B:14:0x004e, B:17:0x0059, B:20:0x0066, B:23:0x0073, B:26:0x0080, B:29:0x008d, B:31:0x0093, B:32:0x0096, B:34:0x00a2, B:35:0x00aa, B:37:0x00b2, B:40:0x00b9, B:42:0x00c3, B:44:0x00d1, B:46:0x00db, B:48:0x00dd, B:54:0x00f0, B:56:0x00f8, B:58:0x0102, B:60:0x0114, B:61:0x0141, B:63:0x014b, B:66:0x015b, B:69:0x0175, B:71:0x0180, B:78:0x01a0, B:82:0x018c, B:83:0x0171, B:84:0x00e5, B:87:0x0120, B:90:0x012a, B:93:0x0134, B:74:0x019b), top: B:1:0x0000, inners: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void a(java.lang.String r9, java.lang.StringBuilder r10, com.autonavi.aps.amapapi.model.AMapLocationServer r11, android.content.Context r12, boolean r13) {
        /*
            Method dump skipped, instructions count: 433
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ei.a(java.lang.String, java.lang.StringBuilder, com.autonavi.aps.amapapi.model.AMapLocationServer, android.content.Context, boolean):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x0066, code lost:
    
        if (r9.isOpen() != false) goto L22;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r9v3 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void b(android.content.Context r9) {
        /*
            r8 = this;
            r8.c()     // Catch: java.lang.Throwable -> L99
            r0 = 0
            r1 = 0
            if (r9 == 0) goto L90
            java.lang.String r2 = "hmdb"
            android.database.sqlite.SQLiteDatabase r9 = r9.openOrCreateDatabase(r2, r0, r1)     // Catch: java.lang.Throwable -> L6e java.lang.Throwable -> L71
            java.lang.String r2 = "hist"
            boolean r2 = com.loc.fa.a(r9, r2)     // Catch: java.lang.Throwable -> L6c java.lang.Throwable -> L83
            if (r2 != 0) goto L22
            if (r9 == 0) goto L90
            boolean r2 = r9.isOpen()     // Catch: java.lang.Throwable -> L6c java.lang.Throwable -> L83
            if (r2 == 0) goto L90
            r9.close()     // Catch: java.lang.Throwable -> L6c java.lang.Throwable -> L83
            goto L90
        L22:
            java.lang.String r2 = "time<?"
            r3 = 1
            java.lang.String[] r3 = new java.lang.String[r3]     // Catch: java.lang.Throwable -> L6c java.lang.Throwable -> L83
            long r4 = com.loc.fa.b()     // Catch: java.lang.Throwable -> L6c java.lang.Throwable -> L83
            r6 = 86400000(0x5265c00, double:4.2687272E-316)
            long r4 = r4 - r6
            java.lang.String r4 = java.lang.String.valueOf(r4)     // Catch: java.lang.Throwable -> L6c java.lang.Throwable -> L83
            r3[r0] = r4     // Catch: java.lang.Throwable -> L6c java.lang.Throwable -> L83
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L49 java.lang.Throwable -> L83
            java.lang.String r5 = "hist"
            r4.<init>(r5)     // Catch: java.lang.Throwable -> L49 java.lang.Throwable -> L83
            java.lang.String r5 = r8.k     // Catch: java.lang.Throwable -> L49 java.lang.Throwable -> L83
            r4.append(r5)     // Catch: java.lang.Throwable -> L49 java.lang.Throwable -> L83
            java.lang.String r4 = r4.toString()     // Catch: java.lang.Throwable -> L49 java.lang.Throwable -> L83
            r9.delete(r4, r2, r3)     // Catch: java.lang.Throwable -> L49 java.lang.Throwable -> L83
            goto L60
        L49:
            r2 = move-exception
            java.lang.String r3 = "DB"
            java.lang.String r4 = "clearHist"
            com.loc.es.a(r2, r3, r4)     // Catch: java.lang.Throwable -> L6c java.lang.Throwable -> L83
            java.lang.String r2 = r2.getMessage()     // Catch: java.lang.Throwable -> L6c java.lang.Throwable -> L83
            boolean r3 = android.text.TextUtils.isEmpty(r2)     // Catch: java.lang.Throwable -> L6c java.lang.Throwable -> L83
            if (r3 != 0) goto L60
            java.lang.String r3 = "no such table"
            r2.contains(r3)     // Catch: java.lang.Throwable -> L6c java.lang.Throwable -> L83
        L60:
            if (r9 == 0) goto L90
            boolean r2 = r9.isOpen()     // Catch: java.lang.Throwable -> L99
            if (r2 == 0) goto L90
        L68:
            r9.close()     // Catch: java.lang.Throwable -> L99
            goto L90
        L6c:
            r2 = move-exception
            goto L73
        L6e:
            r0 = move-exception
            r9 = r1
            goto L84
        L71:
            r2 = move-exception
            r9 = r1
        L73:
            java.lang.String r3 = "DB"
            java.lang.String r4 = "clearHist p2"
            com.loc.es.a(r2, r3, r4)     // Catch: java.lang.Throwable -> L83
            if (r9 == 0) goto L90
            boolean r2 = r9.isOpen()     // Catch: java.lang.Throwable -> L99
            if (r2 == 0) goto L90
            goto L68
        L83:
            r0 = move-exception
        L84:
            if (r9 == 0) goto L8f
            boolean r1 = r9.isOpen()     // Catch: java.lang.Throwable -> L99
            if (r1 == 0) goto L8f
            r9.close()     // Catch: java.lang.Throwable -> L99
        L8f:
            throw r0     // Catch: java.lang.Throwable -> L99
        L90:
            r8.j = r0     // Catch: java.lang.Throwable -> L99
            r8.d = r1     // Catch: java.lang.Throwable -> L99
            r0 = 0
            r8.n = r0     // Catch: java.lang.Throwable -> L99
            return
        L99:
            r9 = move-exception
            java.lang.String r0 = "Cache"
            java.lang.String r1 = "destroy part"
            com.loc.es.a(r9, r0, r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ei.b(android.content.Context):void");
    }
}
