package com.loc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.amap.location.common.model.AmapLoc;
import java.util.ArrayList;

/* compiled from: Req.java */
@SuppressLint({"NewApi"})
/* loaded from: classes.dex */
public final class eq {
    protected static String J;
    protected static String L;
    public String a = AmapLoc.RESULT_TYPE_WIFI_ONLY;
    protected short b = 0;
    protected String c = null;
    protected String d = null;
    protected String e = null;
    protected String f = null;
    protected String g = null;
    public String h = null;
    public String i = null;
    protected String j = null;
    protected String k = null;
    protected String l = null;
    protected String m = null;
    protected String n = null;
    protected String o = null;
    protected String p = null;
    protected String q = null;
    protected String r = null;
    protected String s = null;
    protected String t = null;
    protected String u = null;
    protected String v = null;
    protected String w = null;
    protected String x = null;
    protected String y = null;
    protected int z = 0;
    protected String A = null;
    protected String B = null;
    protected ArrayList<ed> C = new ArrayList<>();
    protected String D = null;
    protected String E = null;
    protected ArrayList<ScanResult> F = new ArrayList<>();
    protected String G = null;
    protected String H = null;
    protected byte[] I = null;
    private byte[] O = null;
    private int P = 0;
    protected String K = null;
    protected String M = null;
    protected String N = null;

    private static int a(String str, byte[] bArr, int i) {
        try {
        } catch (Throwable th) {
            es.a(th, "Req", "copyContentWithByteLen");
            bArr[i] = 0;
        }
        if (TextUtils.isEmpty(str)) {
            bArr[i] = 0;
            return i + 1;
        }
        byte[] bytes = str.getBytes("GBK");
        int length = bytes.length;
        if (length > 127) {
            length = 127;
        }
        bArr[i] = (byte) length;
        int i2 = i + 1;
        System.arraycopy(bytes, 0, bArr, i2, length);
        return i2 + length;
    }

    private String a(String str, int i) {
        String[] split = this.B.split("\\*")[i].split(",");
        if ("lac".equals(str)) {
            return split[0];
        }
        if ("cellid".equals(str)) {
            return split[1];
        }
        if ("signal".equals(str)) {
            return split[2];
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0022 A[Catch: Throwable -> 0x0010, TryCatch #0 {Throwable -> 0x0010, blocks: (B:24:0x000c, B:8:0x001f, B:10:0x0022, B:12:0x002b, B:13:0x0033, B:3:0x0012, B:5:0x0017), top: B:23:0x000c }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private byte[] a(java.lang.String r7) {
        /*
            r6 = this;
            java.lang.String r0 = ":"
            java.lang.String[] r0 = r7.split(r0)
            r1 = 6
            byte[] r2 = new byte[r1]
            r3 = 0
            if (r0 == 0) goto L12
            int r4 = r0.length     // Catch: java.lang.Throwable -> L10
            if (r4 == r1) goto L1e
            goto L12
        L10:
            r0 = move-exception
            goto L41
        L12:
            java.lang.String[] r0 = new java.lang.String[r1]     // Catch: java.lang.Throwable -> L10
            r4 = 0
        L15:
            if (r4 >= r1) goto L1e
            java.lang.String r5 = "0"
            r0[r4] = r5     // Catch: java.lang.Throwable -> L10
            int r4 = r4 + 1
            goto L15
        L1e:
            r1 = 0
        L1f:
            int r4 = r0.length     // Catch: java.lang.Throwable -> L10
            if (r1 >= r4) goto L5a
            r4 = r0[r1]     // Catch: java.lang.Throwable -> L10
            int r4 = r4.length()     // Catch: java.lang.Throwable -> L10
            r5 = 2
            if (r4 <= r5) goto L33
            r4 = r0[r1]     // Catch: java.lang.Throwable -> L10
            java.lang.String r4 = r4.substring(r3, r5)     // Catch: java.lang.Throwable -> L10
            r0[r1] = r4     // Catch: java.lang.Throwable -> L10
        L33:
            r4 = r0[r1]     // Catch: java.lang.Throwable -> L10
            r5 = 16
            int r4 = java.lang.Integer.parseInt(r4, r5)     // Catch: java.lang.Throwable -> L10
            byte r4 = (byte) r4     // Catch: java.lang.Throwable -> L10
            r2[r1] = r4     // Catch: java.lang.Throwable -> L10
            int r1 = r1 + 1
            goto L1f
        L41:
            java.lang.String r1 = "Req"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "getMacBa "
            r2.<init>(r3)
            r2.append(r7)
            java.lang.String r7 = r2.toString()
            com.loc.es.a(r0, r1, r7)
            java.lang.String r7 = "00:00:00:00:00:00"
            byte[] r2 = r6.a(r7)
        L5a:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.eq.a(java.lang.String):byte[]");
    }

    private String b(String str) {
        if (!this.A.contains(str + ">")) {
            return AmapLoc.RESULT_TYPE_GPS;
        }
        return this.A.substring(this.A.indexOf(str + ">") + str.length() + 1, this.A.indexOf("</" + str));
    }

    public final void a(Context context, boolean z, boolean z2, ee eeVar, eg egVar, ConnectivityManager connectivityManager, String str, String str2) {
        String str3;
        NetworkInfo networkInfo;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        int i;
        String str9;
        String str10 = AmapLoc.RESULT_TYPE_GPS;
        String f = u.f(context);
        int g = fa.g();
        this.K = str2;
        String str11 = "api_serverSDK_130905";
        String str12 = "S128DF1572465B890OE3F7A13167KLEI";
        if (!z2) {
            str11 = "UC_nlp_20131029";
            str12 = "BKZCHMBBSSUK7U8GLUKHBB56CCFF78U";
        }
        StringBuilder sb = new StringBuilder();
        int e = eeVar.e();
        int f2 = eeVar.f();
        TelephonyManager g2 = eeVar.g();
        ArrayList<ed> a = eeVar.a();
        ArrayList<ed> b = eeVar.b();
        ArrayList<ScanResult> c = egVar.c();
        if (f2 == 2) {
            str10 = AmapLoc.RESULT_TYPE_WIFI_ONLY;
        }
        String str13 = str10;
        if (g2 != null) {
            if (TextUtils.isEmpty(es.d)) {
                try {
                    es.d = x.u(context);
                } catch (Throwable th) {
                    str3 = f;
                    es.a(th, "Aps", "getApsReq part4");
                }
            }
            str3 = f;
            if (TextUtils.isEmpty(es.d) && Build.VERSION.SDK_INT < 29) {
                es.d = "888888888888888";
            }
            if (TextUtils.isEmpty(es.e)) {
                try {
                    es.e = g2.getSubscriberId();
                } catch (SecurityException unused) {
                } catch (Throwable th2) {
                    es.a(th2, "Aps", "getApsReq part2");
                }
            }
            if (TextUtils.isEmpty(es.e) && Build.VERSION.SDK_INT < 29) {
                es.e = "888888888888888";
            }
        } else {
            str3 = f;
        }
        try {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Throwable th3) {
            es.a(th3, "Aps", "getApsReq part");
            networkInfo = null;
        }
        boolean a2 = egVar.a(connectivityManager);
        if (fa.a(networkInfo) != -1) {
            str4 = fa.b(g2);
            str5 = a2 ? AmapLoc.RESULT_TYPE_FUSED : AmapLoc.RESULT_TYPE_WIFI_ONLY;
        } else {
            str4 = "";
            str5 = "";
        }
        String str14 = str5;
        if (a.isEmpty()) {
            str6 = AmapLoc.RESULT_TYPE_GPS;
            str7 = str4;
            str8 = "";
        } else {
            StringBuilder sb2 = new StringBuilder();
            switch (f2) {
                case 1:
                    str7 = str4;
                    ed edVar = a.get(0);
                    str6 = AmapLoc.RESULT_TYPE_GPS;
                    sb2.delete(0, sb2.length());
                    sb2.append("<mcc>");
                    sb2.append(edVar.a);
                    sb2.append("</mcc>");
                    sb2.append("<mnc>");
                    sb2.append(edVar.b);
                    sb2.append("</mnc>");
                    sb2.append("<lac>");
                    sb2.append(edVar.c);
                    sb2.append("</lac>");
                    sb2.append("<cellid>");
                    sb2.append(edVar.d);
                    sb2.append("</cellid>");
                    sb2.append("<signal>");
                    sb2.append(edVar.j);
                    sb2.append("</signal>");
                    String sb3 = sb2.toString();
                    int i2 = 1;
                    while (i2 < a.size()) {
                        ed edVar2 = a.get(i2);
                        String str15 = sb3;
                        sb.append(edVar2.c);
                        sb.append(",");
                        sb.append(edVar2.d);
                        sb.append(",");
                        sb.append(edVar2.j);
                        if (i2 < a.size() - 1) {
                            sb.append("*");
                        }
                        i2++;
                        sb3 = str15;
                    }
                    str9 = sb3;
                    break;
                case 2:
                    ed edVar3 = a.get(0);
                    str7 = str4;
                    sb2.delete(0, sb2.length());
                    sb2.append("<mcc>");
                    sb2.append(edVar3.a);
                    sb2.append("</mcc>");
                    sb2.append("<sid>");
                    sb2.append(edVar3.g);
                    sb2.append("</sid>");
                    sb2.append("<nid>");
                    sb2.append(edVar3.h);
                    sb2.append("</nid>");
                    sb2.append("<bid>");
                    sb2.append(edVar3.i);
                    sb2.append("</bid>");
                    if (edVar3.f > 0 && edVar3.e > 0) {
                        sb2.append("<lon>");
                        sb2.append(edVar3.f);
                        sb2.append("</lon>");
                        sb2.append("<lat>");
                        sb2.append(edVar3.e);
                        sb2.append("</lat>");
                    }
                    sb2.append("<signal>");
                    sb2.append(edVar3.j);
                    sb2.append("</signal>");
                    String sb4 = sb2.toString();
                    str6 = AmapLoc.RESULT_TYPE_GPS;
                    str9 = sb4;
                    break;
                default:
                    str6 = AmapLoc.RESULT_TYPE_GPS;
                    str7 = str4;
                    str9 = "";
                    break;
            }
            sb2.delete(0, sb2.length());
            str8 = str9;
        }
        if ((e & 4) != 4 || b.isEmpty()) {
            this.C.clear();
        } else {
            this.C.clear();
            this.C.addAll(b);
        }
        StringBuilder sb5 = new StringBuilder();
        if (egVar.p) {
            if (a2) {
                WifiInfo g3 = egVar.g();
                if (eg.a(g3)) {
                    sb5.append(g3.getBSSID());
                    sb5.append(",");
                    int rssi = g3.getRssi();
                    if (rssi < -128 || rssi > 127) {
                        rssi = 0;
                    }
                    sb5.append(rssi);
                    sb5.append(",");
                    String ssid = g3.getSSID();
                    try {
                        i = g3.getSSID().getBytes("UTF-8").length;
                    } catch (Exception unused2) {
                        i = 32;
                    }
                    if (i >= 32) {
                        ssid = "unkwn";
                    }
                    sb5.append(ssid.replace("*", "."));
                }
            }
            if (c != null && this.F != null) {
                this.F.clear();
                this.F.addAll(c);
            }
        } else {
            egVar.d();
            if (this.F != null) {
                this.F.clear();
            }
        }
        this.b = (short) 0;
        if (!z) {
            this.b = (short) (this.b | 2);
        }
        this.c = str11;
        this.d = str12;
        this.f = fa.e();
        this.g = "android" + fa.f();
        this.h = fa.b(context);
        this.i = str13;
        this.j = AmapLoc.RESULT_TYPE_GPS;
        this.k = AmapLoc.RESULT_TYPE_GPS;
        this.l = AmapLoc.RESULT_TYPE_GPS;
        this.m = AmapLoc.RESULT_TYPE_GPS;
        this.n = str6;
        this.o = str3;
        this.p = es.d;
        this.q = es.e;
        this.s = String.valueOf(g);
        this.t = fa.j(context);
        this.v = "4.7.1";
        this.w = str;
        this.u = "";
        this.x = str7;
        this.y = str14;
        this.z = e;
        this.A = str8;
        this.B = sb.toString();
        this.D = eeVar.k();
        this.G = eg.k();
        this.E = sb5.toString();
        try {
            if (TextUtils.isEmpty(J)) {
                J = x.g(context);
            }
        } catch (Throwable unused3) {
        }
        try {
            if (TextUtils.isEmpty(L)) {
                L = x.a(context);
            }
        } catch (Throwable unused4) {
        }
        try {
            if (TextUtils.isEmpty(this.N)) {
                this.N = x.h(context);
            }
        } catch (Throwable unused5) {
        }
        sb.delete(0, sb.length());
        sb5.delete(0, sb5.length());
    }

    /* JADX WARN: Can't wrap try/catch for region: R(20:116|117|118|(8:296|(1:298)(5:(1:324)|300|(1:302)|303|(2:305|(2:307|308)(4:309|(3:311|(1:317)(2:313|314)|315)|318|319))(1:(2:321|308)))|299|300|(0)|302|303|(0)(0))|121|(15:125|126|127|128|(1:292)(5:131|(1:133)|134|(3:136|(1:176)(8:138|(3:172|(1:174)|175)(6:144|(3:146|(1:148)|149)|151|(1:153)|154|(5:162|(1:164)|(1:166)|167|168))|150|151|(1:170)|153|154|(1:169)(6:156|162|(0)|(0)|167|168))|161)|177)|178|(1:180)(13:269|270|271|272|273|274|(1:276)|277|278|279|(3:284|(1:286)|282)|281|282)|181|182|(1:184)(8:235|(1:237)(1:268)|238|(1:240)|241|(11:243|244|245|246|247|(1:249)(2:261|(1:263))|250|(1:260)|254|(2:256|257)(1:259)|258)|266|267)|185|186|187|(1:189)|(24:191|192|193|194|195|(1:197)|198|199|(3:225|226|227)|201|202|203|204|205|206|207|208|(1:210)(1:220)|211|(1:213)|214|(1:216)|217|218)(24:232|233|193|194|195|(0)|198|199|(0)|201|202|203|204|205|206|207|208|(0)(0)|211|(0)|214|(0)|217|218))|295|128|(0)|292|178|(0)(0)|181|182|(0)(0)|185|186|187|(0)|(0)(0)) */
    /* JADX WARN: Code restructure failed: missing block: B:234:0x0671, code lost:
    
        r8[r15] = 0;
     */
    /* JADX WARN: Removed duplicated region for block: B:130:0x03f8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:164:0x04f7  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x04fb  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x051c  */
    /* JADX WARN: Removed duplicated region for block: B:184:0x05a4  */
    /* JADX WARN: Removed duplicated region for block: B:189:0x065e  */
    /* JADX WARN: Removed duplicated region for block: B:191:0x0661 A[Catch: Throwable -> 0x0671, TryCatch #4 {Throwable -> 0x0671, blocks: (B:187:0x0651, B:191:0x0661, B:232:0x0664), top: B:186:0x0651 }] */
    /* JADX WARN: Removed duplicated region for block: B:197:0x0682 A[Catch: Throwable -> 0x06a3, TryCatch #6 {Throwable -> 0x06a3, blocks: (B:195:0x067a, B:197:0x0682, B:198:0x068c), top: B:194:0x067a }] */
    /* JADX WARN: Removed duplicated region for block: B:210:0x06bf  */
    /* JADX WARN: Removed duplicated region for block: B:213:0x06d1  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x06eb  */
    /* JADX WARN: Removed duplicated region for block: B:220:0x06c3  */
    /* JADX WARN: Removed duplicated region for block: B:225:0x0693 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:232:0x0664 A[Catch: Throwable -> 0x0671, TRY_LEAVE, TryCatch #4 {Throwable -> 0x0671, blocks: (B:187:0x0651, B:191:0x0661, B:232:0x0664), top: B:186:0x0651 }] */
    /* JADX WARN: Removed duplicated region for block: B:235:0x05a9  */
    /* JADX WARN: Removed duplicated region for block: B:269:0x0521  */
    /* JADX WARN: Removed duplicated region for block: B:298:0x02c4  */
    /* JADX WARN: Removed duplicated region for block: B:305:0x0367  */
    /* JADX WARN: Removed duplicated region for block: B:320:0x03c1  */
    /* JADX WARN: Removed duplicated region for block: B:322:0x0355 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:323:0x02f5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final byte[] a() {
        /*
            Method dump skipped, instructions count: 1833
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.eq.a():byte[]");
    }
}
