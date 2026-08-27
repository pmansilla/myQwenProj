package com.amap.api.mapcore.util;

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
public final class ku {
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
    protected ArrayList<ko> C = new ArrayList<>();
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
            kw.a(th, "Req", "copyContentWithByteLen");
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
            com.amap.api.mapcore.util.kw.a(r0, r1, r7)
            java.lang.String r7 = "00:00:00:00:00:00"
            byte[] r2 = r6.a(r7)
        L5a:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.ku.a(java.lang.String):byte[]");
    }

    private String b(String str) {
        if (!this.A.contains(str + ">")) {
            return AmapLoc.RESULT_TYPE_GPS;
        }
        return this.A.substring(this.A.indexOf(str + ">") + str.length() + 1, this.A.indexOf("</" + str));
    }

    private void b() {
        if (TextUtils.isEmpty(this.a)) {
            this.a = "";
        }
        if (TextUtils.isEmpty(this.c)) {
            this.c = "";
        }
        if (TextUtils.isEmpty(this.d)) {
            this.d = "";
        }
        if (TextUtils.isEmpty(this.e)) {
            this.e = "";
        }
        if (TextUtils.isEmpty(this.f)) {
            this.f = "";
        }
        if (TextUtils.isEmpty(this.g)) {
            this.g = "";
        }
        if (TextUtils.isEmpty(this.h)) {
            this.h = "";
        }
        if (TextUtils.isEmpty(this.i)) {
            this.i = "";
        }
        if (TextUtils.isEmpty(this.j) || (!AmapLoc.RESULT_TYPE_GPS.equals(this.j) && !AmapLoc.RESULT_TYPE_FUSED.equals(this.j))) {
            this.j = AmapLoc.RESULT_TYPE_GPS;
        }
        if (TextUtils.isEmpty(this.k) || (!AmapLoc.RESULT_TYPE_GPS.equals(this.k) && !AmapLoc.RESULT_TYPE_WIFI_ONLY.equals(this.k))) {
            this.k = AmapLoc.RESULT_TYPE_GPS;
        }
        if (TextUtils.isEmpty(this.l)) {
            this.l = "";
        }
        if (TextUtils.isEmpty(this.m)) {
            this.m = "";
        }
        if (TextUtils.isEmpty(this.n)) {
            this.n = "";
        }
        if (TextUtils.isEmpty(this.o)) {
            this.o = "";
        }
        if (TextUtils.isEmpty(this.p)) {
            this.p = "";
        }
        if (TextUtils.isEmpty(this.q)) {
            this.q = "";
        }
        if (TextUtils.isEmpty(this.r)) {
            this.r = "";
        }
        if (TextUtils.isEmpty(this.s)) {
            this.s = "";
        }
        if (TextUtils.isEmpty(this.t)) {
            this.t = "";
        }
        if (TextUtils.isEmpty(this.u)) {
            this.u = "";
        }
        if (TextUtils.isEmpty(this.v)) {
            this.v = "";
        }
        if (TextUtils.isEmpty(this.w)) {
            this.w = "";
        }
        if (TextUtils.isEmpty(this.x)) {
            this.x = "";
        }
        if (TextUtils.isEmpty(this.y) || (!AmapLoc.RESULT_TYPE_WIFI_ONLY.equals(this.y) && !AmapLoc.RESULT_TYPE_FUSED.equals(this.y))) {
            this.y = AmapLoc.RESULT_TYPE_GPS;
        }
        if (!kp.a(this.z)) {
            this.z = 0;
        }
        if (TextUtils.isEmpty(this.A)) {
            this.A = "";
        }
        if (TextUtils.isEmpty(this.B)) {
            this.B = "";
        }
        if (TextUtils.isEmpty(this.E)) {
            this.E = "";
        }
        if (TextUtils.isEmpty(this.G)) {
            this.G = "";
        }
        if (TextUtils.isEmpty(this.H)) {
            this.H = "";
        }
        if (TextUtils.isEmpty(J)) {
            J = "";
        }
        if (this.I == null) {
            this.I = new byte[0];
        }
        if (TextUtils.isEmpty(this.N)) {
            this.N = "";
        }
    }

    public final void a(Context context, boolean z, boolean z2, kp kpVar, kq kqVar, ConnectivityManager connectivityManager, String str) {
        String str2;
        NetworkInfo networkInfo;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        int i;
        String str8;
        String str9 = AmapLoc.RESULT_TYPE_GPS;
        String f = hd.f(context);
        int f2 = la.f();
        this.K = str;
        String str10 = "api_serverSDK_130905";
        String str11 = "S128DF1572465B890OE3F7A13167KLEI";
        if (!z2) {
            str10 = "UC_nlp_20131029";
            str11 = "BKZCHMBBSSUK7U8GLUKHBB56CCFF78U";
        }
        StringBuilder sb = new StringBuilder();
        int c = kpVar.c();
        int d = kpVar.d();
        TelephonyManager e = kpVar.e();
        ArrayList<ko> a = kpVar.a();
        ArrayList<ko> b = kpVar.b();
        ArrayList<ScanResult> a2 = kqVar.a();
        if (d == 2) {
            str9 = AmapLoc.RESULT_TYPE_WIFI_ONLY;
        }
        String str12 = str9;
        if (e != null) {
            if (TextUtils.isEmpty(kw.d)) {
                try {
                    kw.d = hi.u(context);
                } catch (Throwable th) {
                    str2 = f;
                    kw.a(th, "Aps", "getApsReq part4");
                }
            }
            str2 = f;
            if (TextUtils.isEmpty(kw.d) && Build.VERSION.SDK_INT < 29) {
                kw.d = "888888888888888";
            }
            if (TextUtils.isEmpty(kw.e)) {
                try {
                    kw.e = e.getSubscriberId();
                } catch (SecurityException unused) {
                } catch (Throwable th2) {
                    kw.a(th2, "Aps", "getApsReq part2");
                }
            }
            if (TextUtils.isEmpty(kw.e) && Build.VERSION.SDK_INT < 29) {
                kw.e = "888888888888888";
            }
        } else {
            str2 = f;
        }
        try {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Throwable th3) {
            kw.a(th3, "Aps", "getApsReq part");
            networkInfo = null;
        }
        boolean a3 = kqVar.a(connectivityManager);
        if (la.a(networkInfo) != -1) {
            str3 = la.b(e);
            str4 = a3 ? AmapLoc.RESULT_TYPE_FUSED : AmapLoc.RESULT_TYPE_WIFI_ONLY;
        } else {
            str3 = "";
            str4 = "";
        }
        String str13 = str4;
        if (a.isEmpty()) {
            str5 = str3;
            str6 = AmapLoc.RESULT_TYPE_GPS;
            str7 = "";
        } else {
            StringBuilder sb2 = new StringBuilder();
            switch (d) {
                case 1:
                    str5 = str3;
                    ko koVar = a.get(0);
                    str6 = AmapLoc.RESULT_TYPE_GPS;
                    sb2.delete(0, sb2.length());
                    sb2.append("<mcc>");
                    sb2.append(koVar.a);
                    sb2.append("</mcc>");
                    sb2.append("<mnc>");
                    sb2.append(koVar.b);
                    sb2.append("</mnc>");
                    sb2.append("<lac>");
                    sb2.append(koVar.c);
                    sb2.append("</lac>");
                    sb2.append("<cellid>");
                    sb2.append(koVar.d);
                    sb2.append("</cellid>");
                    sb2.append("<signal>");
                    sb2.append(koVar.j);
                    sb2.append("</signal>");
                    String sb3 = sb2.toString();
                    int i2 = 1;
                    while (i2 < a.size()) {
                        ko koVar2 = a.get(i2);
                        String str14 = sb3;
                        sb.append(koVar2.c);
                        sb.append(",");
                        sb.append(koVar2.d);
                        sb.append(",");
                        sb.append(koVar2.j);
                        if (i2 < a.size() - 1) {
                            sb.append("*");
                        }
                        i2++;
                        sb3 = str14;
                    }
                    str8 = sb3;
                    break;
                case 2:
                    ko koVar3 = a.get(0);
                    str5 = str3;
                    sb2.delete(0, sb2.length());
                    sb2.append("<mcc>");
                    sb2.append(koVar3.a);
                    sb2.append("</mcc>");
                    sb2.append("<sid>");
                    sb2.append(koVar3.g);
                    sb2.append("</sid>");
                    sb2.append("<nid>");
                    sb2.append(koVar3.h);
                    sb2.append("</nid>");
                    sb2.append("<bid>");
                    sb2.append(koVar3.i);
                    sb2.append("</bid>");
                    if (koVar3.f > 0 && koVar3.e > 0) {
                        sb2.append("<lon>");
                        sb2.append(koVar3.f);
                        sb2.append("</lon>");
                        sb2.append("<lat>");
                        sb2.append(koVar3.e);
                        sb2.append("</lat>");
                    }
                    sb2.append("<signal>");
                    sb2.append(koVar3.j);
                    sb2.append("</signal>");
                    str8 = sb2.toString();
                    str6 = AmapLoc.RESULT_TYPE_GPS;
                    break;
                default:
                    str5 = str3;
                    str6 = AmapLoc.RESULT_TYPE_GPS;
                    str8 = "";
                    break;
            }
            sb2.delete(0, sb2.length());
            str7 = str8;
        }
        if ((c & 4) != 4 || b.isEmpty()) {
            this.C.clear();
        } else {
            this.C.clear();
            this.C.addAll(b);
        }
        StringBuilder sb4 = new StringBuilder();
        if (kqVar.e()) {
            if (a3) {
                WifiInfo f3 = kqVar.f();
                if (kq.a(f3)) {
                    sb4.append(f3.getBSSID());
                    sb4.append(",");
                    int rssi = f3.getRssi();
                    if (rssi < -128 || rssi > 127) {
                        rssi = 0;
                    }
                    sb4.append(rssi);
                    sb4.append(",");
                    String ssid = f3.getSSID();
                    try {
                        i = f3.getSSID().getBytes("UTF-8").length;
                    } catch (Exception unused2) {
                        i = 32;
                    }
                    if (i >= 32) {
                        ssid = "unkwn";
                    }
                    sb4.append(ssid.replace("*", "."));
                }
            }
            if (a2 != null && this.F != null) {
                this.F.clear();
                this.F.addAll(a2);
            }
        } else {
            kqVar.b();
            if (this.F != null) {
                this.F.clear();
            }
        }
        this.b = (short) 0;
        if (!z) {
            this.b = (short) (this.b | 2);
        }
        this.c = str10;
        this.d = str11;
        this.f = la.d();
        this.g = "android" + la.e();
        this.h = la.b(context);
        this.i = str12;
        this.j = AmapLoc.RESULT_TYPE_GPS;
        this.k = AmapLoc.RESULT_TYPE_GPS;
        this.l = AmapLoc.RESULT_TYPE_GPS;
        this.m = AmapLoc.RESULT_TYPE_GPS;
        this.n = str6;
        this.o = str2;
        this.p = kw.d;
        this.q = kw.e;
        this.s = String.valueOf(f2);
        this.t = la.d(context);
        this.v = "4.7.1";
        this.w = null;
        this.u = "";
        this.x = str5;
        this.y = str13;
        this.z = c;
        this.A = str7;
        this.B = sb.toString();
        this.D = kpVar.i();
        this.G = kq.i();
        this.E = sb4.toString();
        try {
            if (TextUtils.isEmpty(J)) {
                J = hi.h(context);
            }
        } catch (Throwable unused3) {
        }
        try {
            if (TextUtils.isEmpty(L)) {
                L = hi.b(context);
            }
        } catch (Throwable unused4) {
        }
        try {
            if (TextUtils.isEmpty(this.N)) {
                this.N = hi.i(context);
            }
        } catch (Throwable unused5) {
        }
        sb.delete(0, sb.length());
        sb4.delete(0, sb4.length());
    }

    /* JADX WARN: Can't wrap try/catch for region: R(20:13|14|15|(8:193|(1:195)(5:(1:221)|197|(1:199)|200|(2:202|(2:204|205)(4:206|(3:208|(1:214)(2:210|211)|212)|215|216))(1:(2:218|205)))|196|197|(0)|199|200|(0)(0))|18|(15:22|23|24|25|(1:189)(5:28|(1:30)|31|(3:33|(1:73)(8:35|(3:69|(1:71)|72)(6:41|(3:43|(1:45)|46)|48|(1:50)|51|(5:59|(1:61)|(1:63)|64|65))|47|48|(1:67)|50|51|(1:66)(6:53|59|(0)|(0)|64|65))|58)|74)|75|(1:77)(13:166|167|168|169|170|171|(1:173)|174|175|176|(3:181|(1:183)|179)|178|179)|78|79|(1:81)(8:132|(1:134)(1:165)|135|(1:137)|138|(11:140|141|142|143|144|(1:146)(2:158|(1:160))|147|(1:157)|151|(2:153|154)(1:156)|155)|163|164)|82|83|84|(1:86)|(24:88|89|90|91|92|(1:94)|95|96|(3:122|123|124)|98|99|100|101|102|103|104|105|(1:107)(1:117)|108|(1:110)|111|(1:113)|114|115)(24:129|130|90|91|92|(0)|95|96|(0)|98|99|100|101|102|103|104|105|(0)(0)|108|(0)|111|(0)|114|115))|192|25|(0)|189|75|(0)(0)|78|79|(0)(0)|82|83|84|(0)|(0)(0)) */
    /* JADX WARN: Code restructure failed: missing block: B:131:0x04a6, code lost:
    
        r6[r15] = 0;
     */
    /* JADX WARN: Removed duplicated region for block: B:107:0x04f4  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0506  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x0520  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x04f8  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x04c8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:129:0x0499 A[Catch: Throwable -> 0x04a6, TRY_LEAVE, TryCatch #7 {Throwable -> 0x04a6, blocks: (B:84:0x0486, B:88:0x0496, B:129:0x0499), top: B:83:0x0486 }] */
    /* JADX WARN: Removed duplicated region for block: B:132:0x03de  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x0356  */
    /* JADX WARN: Removed duplicated region for block: B:195:0x00f9  */
    /* JADX WARN: Removed duplicated region for block: B:202:0x019c  */
    /* JADX WARN: Removed duplicated region for block: B:217:0x01f6  */
    /* JADX WARN: Removed duplicated region for block: B:219:0x018a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:220:0x012a  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x022d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:61:0x032c  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0330  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0351  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x03d9  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0493  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0496 A[Catch: Throwable -> 0x04a6, TryCatch #7 {Throwable -> 0x04a6, blocks: (B:84:0x0486, B:88:0x0496, B:129:0x0499), top: B:83:0x0486 }] */
    /* JADX WARN: Removed duplicated region for block: B:94:0x04b7 A[Catch: Throwable -> 0x04d8, TryCatch #9 {Throwable -> 0x04d8, blocks: (B:92:0x04af, B:94:0x04b7, B:95:0x04c1), top: B:91:0x04af }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final byte[] a() {
        /*
            Method dump skipped, instructions count: 1373
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.ku.a():byte[]");
    }
}
