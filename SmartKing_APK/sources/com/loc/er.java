package com.loc;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.graphics.drawable.PathInterpolatorCompat;
import com.amap.location.common.model.AmapLoc;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.time.DateUtils;
import org.json.JSONObject;

/* compiled from: AuthUtil.java */
/* loaded from: classes.dex */
public final class er {
    private static long A = 0;
    public static boolean a = true;
    static boolean b = false;
    private static boolean i = false;
    private static boolean j = false;
    private static long k = 0;
    private static long l = 0;
    private static long m = 5000;
    private static boolean n = false;
    private static int o = 0;
    private static boolean p = false;
    private static int q = 0;
    private static boolean r = false;
    private static boolean s = true;
    private static int t = 1000;
    private static int u = 200;
    private static boolean v = false;
    private static int w = 20;
    private static boolean x = true;
    private static boolean y = true;
    private static int z = -1;
    private static ArrayList<String> B = new ArrayList<>();
    private static boolean C = false;
    private static int D = -1;
    private static long E = 0;
    private static ArrayList<String> F = new ArrayList<>();
    private static boolean G = false;
    private static int H = PathInterpolatorCompat.MAX_NUM_POINTS;
    private static int I = PathInterpolatorCompat.MAX_NUM_POINTS;
    private static boolean J = true;
    private static long K = 300000;
    static boolean c = false;
    private static List<ev> L = new ArrayList();
    private static boolean M = false;
    private static long N = 0;
    private static int O = 0;
    private static int P = 0;
    private static List<String> Q = new ArrayList();
    private static boolean R = true;
    private static int S = 80;
    static int d = DateUtils.MILLIS_IN_HOUR;
    private static boolean T = false;
    private static boolean U = true;
    private static boolean V = false;
    static long e = 0;
    static long f = 0;
    static boolean g = false;
    static boolean h = true;
    private static boolean W = false;
    private static boolean X = true;
    private static boolean Y = false;
    private static int Z = -1;
    private static boolean aa = true;
    private static long ab = -1;
    private static boolean ac = true;
    private static int ad = 1;
    private static long ae = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AuthUtil.java */
    /* loaded from: classes.dex */
    public static class a {
        boolean a = false;
        String b = AmapLoc.RESULT_TYPE_GPS;
        boolean c = false;
        int d = 5;

        a() {
        }
    }

    public static boolean A() {
        return aa;
    }

    public static long B() {
        return ab;
    }

    public static boolean C() {
        return ac && ad > 0;
    }

    public static int D() {
        return ad;
    }

    public static long E() {
        return ae;
    }

    private static a a(JSONObject jSONObject, String str) {
        a aVar;
        if (jSONObject != null) {
            try {
                JSONObject jSONObject2 = jSONObject.getJSONObject(str);
                if (jSONObject2 != null) {
                    aVar = new a();
                    try {
                        aVar.a = v.a(jSONObject2.optString("b"), false);
                        aVar.b = jSONObject2.optString("t");
                        aVar.c = v.a(jSONObject2.optString("st"), false);
                        aVar.d = jSONObject2.optInt("i", 0);
                        return aVar;
                    } catch (Throwable th) {
                        th = th;
                        es.a(th, "AuthUtil", "getLocateObj");
                        return aVar;
                    }
                }
            } catch (Throwable th2) {
                th = th2;
                aVar = null;
            }
        }
        return null;
    }

    public static boolean a() {
        return n;
    }

    public static boolean a(long j2) {
        long c2 = fa.c();
        return j && c2 - l <= k && c2 - j2 >= m;
    }

    public static boolean a(Context context) {
        boolean z2;
        x = true;
        try {
            i = ez.b(context, "pref", "oda", false);
            z2 = a(context, v.a(context, es.b(), es.c()));
        } catch (Throwable th) {
            es.a(th, "AuthUtil", "getConfig");
            z2 = false;
        }
        f = fa.c();
        e = fa.c();
        return z2;
    }

    public static boolean a(Context context, long j2) {
        if (!G) {
            return false;
        }
        long b2 = fa.b();
        if (b2 - j2 < H) {
            return false;
        }
        if (I == -1) {
            return true;
        }
        if (fa.c(b2, ez.b(context, "pref", "ngpsTime", 0L))) {
            int b3 = ez.b(context, "pref", "ngpsCount", 0);
            if (b3 >= I) {
                return false;
            }
            ez.a(context, "pref", "ngpsCount", b3 + 1);
            return true;
        }
        try {
            SharedPreferences.Editor edit = context.getSharedPreferences("pref", 0).edit();
            edit.putLong("ngpsTime", b2);
            edit.putInt("ngpsCount", 0);
            ez.a(edit);
        } catch (Throwable th) {
            es.a(th, "AuthUtil", "resetPrefsNGPS");
        }
        ez.a(context, "pref", "ngpsCount", 1);
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:138:0x038f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:165:0x0422  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static boolean a(android.content.Context r16, com.loc.v.a r17) {
        /*
            Method dump skipped, instructions count: 1456
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.er.a(android.content.Context, com.loc.v$a):boolean");
    }

    public static int b() {
        return o;
    }

    public static boolean b(long j2) {
        if (J) {
            return K < 0 || fa.b() - j2 < K;
        }
        return false;
    }

    public static boolean b(Context context) {
        if (!y) {
            return false;
        }
        if (z == -1 || A == 0) {
            return true;
        }
        if (!fa.b(A, ez.b(context, "pref", "nowtime", 0L))) {
            h(context);
            ez.a(context, "pref", "count", 1);
            return true;
        }
        int b2 = ez.b(context, "pref", "count", 0);
        if (b2 >= z) {
            return false;
        }
        ez.a(context, "pref", "count", b2 + 1);
        return true;
    }

    public static boolean c() {
        return p;
    }

    public static boolean c(Context context) {
        if (!C) {
            return false;
        }
        if (D == -1 || E == 0) {
            return true;
        }
        if (!fa.b(E, ez.b(context, "pref", "pushSerTime", 0L))) {
            i(context);
            ez.a(context, "pref", "pushCount", 1);
            return true;
        }
        int b2 = ez.b(context, "pref", "pushCount", 0);
        if (b2 >= D) {
            return false;
        }
        ez.a(context, "pref", "pushCount", b2 + 1);
        return true;
    }

    public static int d() {
        return q;
    }

    public static void d(Context context) {
        try {
            s = ez.b(context, "pref", "exception", s);
            e(context);
        } catch (Throwable th) {
            es.a(th, "AuthUtil", "loadLastAbleState p1");
        }
        try {
            t = ez.b(context, "pref", "fn", t);
            u = ez.b(context, "pref", "mpn", u);
            v = ez.b(context, "pref", "igu", v);
            w = ez.b(context, "pref", "ms", w);
            bp.a(t, v, w);
        } catch (Throwable unused) {
        }
        try {
            J = ez.b(context, "pref", "ca", J);
            K = ez.b(context, "pref", "ct", K);
        } catch (Throwable unused2) {
        }
        try {
            h = ez.b(context, "pref", "fr", h);
        } catch (Throwable unused3) {
        }
        try {
            W = ez.b(context, "pref", "ok0", W);
            X = ez.b(context, "pref", "ok2", X);
            Y = ez.b(context, "pref", "ok3", Y);
        } catch (Throwable unused4) {
        }
        try {
            aa = ez.b(context, "pref", "asw", aa);
        } catch (Throwable unused5) {
        }
        try {
            ab = ez.b(context, "pref", "awsi", ab);
        } catch (Throwable unused6) {
        }
        try {
            ac = ez.b(context, "pref", "15ua", ac);
            ad = ez.b(context, "pref", "15un", ad);
            ae = ez.b(context, "pref", "15ust", ae);
        } catch (Throwable unused7) {
        }
    }

    public static void e(Context context) {
        try {
            ac b2 = es.b();
            b2.a(s);
            aq.a(context, b2);
        } catch (Throwable unused) {
        }
    }

    public static boolean e() {
        return r;
    }

    public static boolean f() {
        return b;
    }

    public static boolean f(Context context) {
        boolean z2 = O != -1 && O < P;
        if (M && O != 0 && P != 0 && N != 0 && !z2) {
            if (Q != null && Q.size() > 0) {
                Iterator<String> it = Q.iterator();
                while (it.hasNext()) {
                    if (fa.b(context, it.next())) {
                        return false;
                    }
                }
            }
            if (O == -1 && P == -1) {
                return true;
            }
            long b2 = ez.b(context, "pref", "ots", 0L);
            long b3 = ez.b(context, "pref", "otsh", 0L);
            int b4 = ez.b(context, "pref", "otn", 0);
            int b5 = ez.b(context, "pref", "otnh", 0);
            if (O != -1) {
                if (!fa.b(N, b2)) {
                    try {
                        SharedPreferences.Editor edit = context.getSharedPreferences("pref", 0).edit();
                        edit.putLong("ots", N);
                        edit.putLong("otsh", N);
                        edit.putInt("otn", 0);
                        edit.putInt("otnh", 0);
                        ez.a(edit);
                    } catch (Throwable th) {
                        es.a(th, "AuthUtil", "resetPrefsBind");
                    }
                    ez.a(context, "pref", "otn", 1);
                    ez.a(context, "pref", "otnh", 1);
                    return true;
                }
                if (b4 < O) {
                    if (P == -1) {
                        ez.a(context, "pref", "otn", b4 + 1);
                        ez.a(context, "pref", "otnh", 0);
                        return true;
                    }
                    if (!fa.a(N, b3)) {
                        ez.a(context, "pref", "otsh", N);
                        ez.a(context, "pref", "otn", b4 + 1);
                        ez.a(context, "pref", "otnh", 1);
                        return true;
                    }
                    if (b5 < P) {
                        ez.a(context, "pref", "otn", b4 + 1);
                        ez.a(context, "pref", "otnh", b5 + 1);
                        return true;
                    }
                }
            }
            if (O == -1) {
                ez.a(context, "pref", "otn", 0);
                if (P == -1) {
                    ez.a(context, "pref", "otnh", 0);
                    return true;
                }
                if (!fa.a(N, b3)) {
                    ez.a(context, "pref", "otsh", N);
                    ez.a(context, "pref", "otnh", 1);
                    return true;
                }
                if (b5 < P) {
                    ez.a(context, "pref", "otnh", b5 + 1);
                    return true;
                }
            }
        }
        return false;
    }

    public static ArrayList<String> g() {
        return B;
    }

    public static boolean g(Context context) {
        if (context == null) {
            return false;
        }
        try {
            if (fa.c() - f >= d) {
                g = true;
                return true;
            }
        } catch (Throwable th) {
            es.a(th, "Aps", "isConfigNeedUpdate");
        }
        return false;
    }

    public static ArrayList<String> h() {
        return F;
    }

    private static void h(Context context) {
        try {
            SharedPreferences.Editor edit = context.getSharedPreferences("pref", 0).edit();
            edit.putLong("nowtime", A);
            edit.putInt("count", 0);
            ez.a(edit);
        } catch (Throwable th) {
            es.a(th, "AuthUtil", "resetPrefsBind");
        }
    }

    private static void i(Context context) {
        try {
            SharedPreferences.Editor edit = context.getSharedPreferences("pref", 0).edit();
            edit.putLong("pushSerTime", E);
            edit.putInt("pushCount", 0);
            ez.a(edit);
        } catch (Throwable th) {
            es.a(th, "AuthUtil", "resetPrefsBind");
        }
    }

    public static boolean i() {
        return s;
    }

    public static int j() {
        return u;
    }

    public static boolean k() {
        return x;
    }

    public static void l() {
        x = false;
    }

    public static boolean m() {
        return G;
    }

    public static long n() {
        return K;
    }

    public static boolean o() {
        return J;
    }

    public static List<ev> p() {
        return L;
    }

    public static boolean q() {
        return R;
    }

    public static int r() {
        return S;
    }

    public static boolean s() {
        return U;
    }

    public static boolean t() {
        return V;
    }

    public static boolean u() {
        if (!g) {
            return g;
        }
        g = false;
        return true;
    }

    public static boolean v() {
        return h;
    }

    public static boolean w() {
        return W;
    }

    public static boolean x() {
        return Y;
    }

    public static boolean y() {
        return X;
    }

    public static int z() {
        return Z;
    }
}
