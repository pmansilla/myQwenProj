package com.loc;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.amap.location.common.model.AmapLoc;
import java.io.ByteArrayOutputStream;

/* compiled from: ClientInfo.java */
/* loaded from: classes.dex */
public final class w {

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ClientInfo.java */
    /* loaded from: classes.dex */
    public static class a {
        String a;
        String b;
        String c;
        String d;
        String e;
        String f;
        String g;
        String h;
        String i;
        String j;
        String k;
        String l;
        String m;
        String n;
        String o;
        String p;
        String q;
        String r;
        String s;
        String t;
        String u;
        String v;
        String w;
        String x;
        String y;

        private a() {
        }

        /* synthetic */ a(byte b) {
            this();
        }
    }

    public static String a() {
        String str;
        Throwable th;
        try {
            str = String.valueOf(System.currentTimeMillis());
            try {
                String str2 = AmapLoc.RESULT_TYPE_WIFI_ONLY;
                if (!u.a()) {
                    str2 = AmapLoc.RESULT_TYPE_GPS;
                }
                int length = str.length();
                return str.substring(0, length - 2) + str2 + str.substring(length - 1);
            } catch (Throwable th2) {
                th = th2;
                an.a(th, "CI", "TS");
                return str;
            }
        } catch (Throwable th3) {
            str = null;
            th = th3;
        }
    }

    public static String a(Context context, String str, String str2) {
        try {
            return aa.b(u.e(context) + ":" + str.substring(0, str.length() - 3) + ":" + str2);
        } catch (Throwable th) {
            an.a(th, "CI", "Sco");
            return null;
        }
    }

    private static void a(ByteArrayOutputStream byteArrayOutputStream, String str) {
        if (TextUtils.isEmpty(str)) {
            ad.a(byteArrayOutputStream, (byte) 0, new byte[0]);
        } else {
            ad.a(byteArrayOutputStream, str.getBytes().length > 255 ? (byte) -1 : (byte) str.getBytes().length, ad.a(str));
        }
    }

    public static byte[] a(Context context, boolean z) {
        String str;
        try {
            a aVar = new a((byte) 0);
            aVar.a = x.u(context);
            aVar.b = x.l(context);
            String g = x.g(context);
            if (g == null) {
                g = "";
            }
            aVar.c = g;
            aVar.d = u.c(context);
            aVar.e = Build.MODEL;
            aVar.f = Build.MANUFACTURER;
            aVar.g = Build.DEVICE;
            aVar.h = u.b(context);
            aVar.i = u.d(context);
            aVar.j = String.valueOf(Build.VERSION.SDK_INT);
            aVar.k = x.x(context);
            aVar.l = x.t(context);
            StringBuilder sb = new StringBuilder();
            sb.append(x.q(context));
            aVar.m = sb.toString();
            StringBuilder sb2 = new StringBuilder();
            sb2.append(x.p(context));
            aVar.n = sb2.toString();
            aVar.o = x.z(context);
            aVar.p = x.o(context);
            aVar.q = z ? "" : x.k(context);
            aVar.r = z ? "" : x.j(context);
            if (z) {
                aVar.s = "";
                str = "";
            } else {
                String[] m = x.m(context);
                aVar.s = m[0];
                str = m[1];
            }
            aVar.t = str;
            aVar.w = x.a();
            String a2 = x.a(context);
            if (TextUtils.isEmpty(a2)) {
                a2 = "";
            }
            aVar.x = a2;
            aVar.y = "aid=" + x.i(context) + "|serial=" + x.h(context) + "|storage=" + x.d() + "|ram=" + x.y(context) + "|arch=" + x.e();
            String b = x.b();
            if (!TextUtils.isEmpty(b)) {
                aVar.y += "|adiuExtras=" + b;
            }
            String a3 = x.a(context, ",");
            if (!TextUtils.isEmpty(a3)) {
                aVar.y += "|multiImeis=" + a3;
            }
            String w = x.w(context);
            if (!TextUtils.isEmpty(w)) {
                aVar.y += "|meid=" + w;
            }
            return a(aVar);
        } catch (Throwable th) {
            an.a(th, "CI", "gz");
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x00d7 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static byte[] a(com.loc.w.a r7) {
        /*
            Method dump skipped, instructions count: 224
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.w.a(com.loc.w$a):byte[]");
    }
}
