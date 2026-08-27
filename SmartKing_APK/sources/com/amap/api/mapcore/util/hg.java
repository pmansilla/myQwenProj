package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.amap.location.common.model.AmapLoc;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/* compiled from: ClientInfo.java */
/* loaded from: classes.dex */
public class hg {

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
    }

    public static String a() {
        String str;
        Throwable th;
        try {
            str = String.valueOf(System.currentTimeMillis());
            try {
                String str2 = AmapLoc.RESULT_TYPE_WIFI_ONLY;
                if (!hd.a()) {
                    str2 = AmapLoc.RESULT_TYPE_GPS;
                }
                int length = str.length();
                return str.substring(0, length - 2) + str2 + str.substring(length - 1);
            } catch (Throwable th2) {
                th = th2;
                hz.a(th, "CI", "TS");
                return str;
            }
        } catch (Throwable th3) {
            str = null;
            th = th3;
        }
    }

    public static String a(Context context) {
        try {
            a aVar = new a();
            aVar.d = hd.c(context);
            aVar.i = hd.d(context);
            return a(context, aVar);
        } catch (Throwable th) {
            hz.a(th, "CI", "IX");
            return null;
        }
    }

    private static String a(Context context, a aVar) {
        return hj.b(b(context, aVar));
    }

    public static String a(Context context, String str, String str2) {
        try {
            return hl.b(hd.e(context) + ":" + str.substring(0, str.length() - 3) + ":" + str2);
        } catch (Throwable th) {
            hz.a(th, "CI", "Sco");
            return null;
        }
    }

    public static void a(ByteArrayOutputStream byteArrayOutputStream, String str) {
        if (TextUtils.isEmpty(str)) {
            hp.a(byteArrayOutputStream, (byte) 0, new byte[0]);
        } else {
            hp.a(byteArrayOutputStream, str.getBytes().length > 255 ? (byte) -1 : (byte) str.getBytes().length, hp.a(str));
        }
    }

    private static byte[] a(Context context, ByteArrayOutputStream byteArrayOutputStream) throws CertificateException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        return b(context, hp.b(byteArrayOutputStream.toByteArray()));
    }

    public static byte[] a(Context context, boolean z) {
        try {
            return b(context, b(context, z));
        } catch (Throwable th) {
            hz.a(th, "CI", "gz");
            return null;
        }
    }

    public static byte[] a(Context context, byte[] bArr) throws CertificateException, InvalidKeySpecException, NoSuchAlgorithmException, NullPointerException, IOException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        return hj.a(bArr);
    }

    private static a b(Context context, boolean z) {
        a aVar = new a();
        aVar.a = hi.u(context);
        aVar.b = hi.m(context);
        String h = hi.h(context);
        if (h == null) {
            h = "";
        }
        aVar.c = h;
        aVar.d = hd.c(context);
        aVar.e = Build.MODEL;
        aVar.f = Build.MANUFACTURER;
        aVar.g = Build.DEVICE;
        aVar.h = hd.b(context);
        aVar.i = hd.d(context);
        aVar.j = String.valueOf(Build.VERSION.SDK_INT);
        aVar.k = hi.w(context);
        aVar.l = hi.t(context);
        aVar.m = hi.q(context) + "";
        aVar.n = hi.p(context) + "";
        aVar.o = hi.y(context);
        aVar.p = hi.o(context);
        if (z) {
            aVar.q = "";
        } else {
            aVar.q = hi.l(context);
        }
        if (z) {
            aVar.r = "";
        } else {
            aVar.r = hi.k(context);
        }
        if (z) {
            aVar.s = "";
            aVar.t = "";
        } else {
            String[] n = hi.n(context);
            aVar.s = n[0];
            aVar.t = n[1];
        }
        aVar.w = hi.a();
        String b = hi.b(context);
        if (TextUtils.isEmpty(b)) {
            aVar.x = "";
        } else {
            aVar.x = b;
        }
        aVar.y = "aid=" + hi.j(context) + "|serial=" + hi.i(context) + "|storage=" + hi.c() + "|ram=" + hi.x(context) + "|arch=" + hi.d();
        String a2 = hi.a(context);
        if (!TextUtils.isEmpty(a2)) {
            aVar.y += "|adiuExtras=" + a2;
        }
        String a3 = hi.a(context, ",", true);
        if (!TextUtils.isEmpty(a3)) {
            aVar.y += "|multiImeis=" + a3;
        }
        String v = hi.v(context);
        if (!TextUtils.isEmpty(v)) {
            aVar.y += "|meid=" + v;
        }
        return aVar;
    }

    public static String b(Context context) {
        try {
            return a(context, b(context, false));
        } catch (Throwable th) {
            hz.a(th, "CI", "gCX");
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x00ac A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static byte[] b(android.content.Context r3, com.amap.api.mapcore.util.hg.a r4) {
        /*
            r0 = 0
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch: java.lang.Throwable -> L92 java.lang.Throwable -> L95
            r1.<init>()     // Catch: java.lang.Throwable -> L92 java.lang.Throwable -> L95
            java.lang.String r2 = r4.a     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            a(r1, r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            java.lang.String r2 = r4.b     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            a(r1, r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            java.lang.String r2 = r4.c     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            a(r1, r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            java.lang.String r2 = r4.d     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            a(r1, r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            java.lang.String r2 = r4.e     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            a(r1, r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            java.lang.String r2 = r4.f     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            a(r1, r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            java.lang.String r2 = r4.g     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            a(r1, r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            java.lang.String r2 = r4.h     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            a(r1, r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            java.lang.String r2 = r4.i     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            a(r1, r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            java.lang.String r2 = r4.j     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            a(r1, r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            java.lang.String r2 = r4.k     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            a(r1, r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            java.lang.String r2 = r4.l     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            a(r1, r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            java.lang.String r2 = r4.m     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            a(r1, r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            java.lang.String r2 = r4.n     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            a(r1, r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            java.lang.String r2 = r4.o     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            a(r1, r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            java.lang.String r2 = r4.p     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            a(r1, r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            java.lang.String r2 = r4.q     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            a(r1, r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            java.lang.String r2 = r4.r     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            a(r1, r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            java.lang.String r2 = r4.s     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            a(r1, r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            java.lang.String r2 = r4.t     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            a(r1, r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            java.lang.String r2 = r4.u     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            a(r1, r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            java.lang.String r2 = r4.v     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            a(r1, r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            java.lang.String r2 = r4.w     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            a(r1, r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            java.lang.String r2 = r4.x     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            a(r1, r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            java.lang.String r4 = r4.y     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            a(r1, r4)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            byte[] r3 = a(r3, r1)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> La9
            r1.close()     // Catch: java.lang.Throwable -> L8b
            goto L8f
        L8b:
            r4 = move-exception
            r4.printStackTrace()
        L8f:
            return r3
        L90:
            r3 = move-exception
            goto L97
        L92:
            r3 = move-exception
            r1 = r0
            goto Laa
        L95:
            r3 = move-exception
            r1 = r0
        L97:
            java.lang.String r4 = "CI"
            java.lang.String r2 = "gzx"
            com.amap.api.mapcore.util.hz.a(r3, r4, r2)     // Catch: java.lang.Throwable -> La9
            if (r1 == 0) goto La8
            r1.close()     // Catch: java.lang.Throwable -> La4
            goto La8
        La4:
            r3 = move-exception
            r3.printStackTrace()
        La8:
            return r0
        La9:
            r3 = move-exception
        Laa:
            if (r1 == 0) goto Lb4
            r1.close()     // Catch: java.lang.Throwable -> Lb0
            goto Lb4
        Lb0:
            r4 = move-exception
            r4.printStackTrace()
        Lb4:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.hg.b(android.content.Context, com.amap.api.mapcore.util.hg$a):byte[]");
    }

    public static byte[] b(Context context, byte[] bArr) throws CertificateException, InvalidKeySpecException, NoSuchAlgorithmException, NullPointerException, IOException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        PublicKey d = hp.d();
        if (bArr.length <= 117) {
            return hj.a(bArr, d);
        }
        byte[] bArr2 = new byte[117];
        System.arraycopy(bArr, 0, bArr2, 0, 117);
        byte[] a2 = hj.a(bArr2, d);
        byte[] bArr3 = new byte[(bArr.length + 128) - 117];
        System.arraycopy(a2, 0, bArr3, 0, 128);
        System.arraycopy(bArr, 117, bArr3, 128, bArr.length - 117);
        return bArr3;
    }
}
