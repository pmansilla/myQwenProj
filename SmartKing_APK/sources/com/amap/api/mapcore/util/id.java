package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Build;
import com.amap.location.common.model.AmapLoc;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.time.DateUtils;

/* compiled from: ErrorLogManager.java */
/* loaded from: classes.dex */
public class id {
    private static WeakReference<jc> a = null;
    private static boolean b = true;
    private static WeakReference<jw> c;
    private static WeakReference<jw> d;
    private static String[] e = new String[10];
    private static int f = 0;
    private static boolean g = false;
    private static int h = 0;
    private static ho i;

    static ho a(Context context, String str) {
        List<ho> c2 = ia.c(context);
        if (c2 == null) {
            c2 = new ArrayList();
        }
        if (str == null || "".equals(str)) {
            return null;
        }
        for (ho hoVar : c2) {
            if (ia.a(hoVar.g(), str)) {
                return hoVar;
            }
        }
        if (str.contains("com.amap.api.col")) {
            try {
                return hp.a();
            } catch (hc e2) {
                e2.printStackTrace();
            }
        }
        if (str.contains("com.amap.co") || str.contains("com.amap.opensdk.co") || str.contains("com.amap.location")) {
            try {
                ho b2 = hp.b();
                b2.a(true);
                return b2;
            } catch (hc e3) {
                e3.printStackTrace();
            }
        }
        return null;
    }

    private static String a(Throwable th) {
        return th.toString();
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x0108  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x010d A[RETURN] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:94:0x009e -> B:30:0x0104). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static java.lang.String a(java.util.List<com.amap.api.mapcore.util.ho> r8) {
        /*
            Method dump skipped, instructions count: 270
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.id.a(java.util.List):java.lang.String");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(Context context) {
        String a2;
        List<ho> c2 = ia.c(context);
        if (c2 == null || c2.size() == 0 || (a2 = a(c2)) == null || "".equals(a2) || i == null) {
            return;
        }
        a(context, i, 2, "ANR", a2);
    }

    private static void a(Context context, ho hoVar, int i2, String str, String str2) {
        String str3;
        String a2 = jj.a();
        String a3 = jj.a(hd.a(context), jj.a(context, hoVar), a2, i2, str, str2);
        if (a3 == null || "".equals(a3)) {
            return;
        }
        String c2 = hl.c(str2);
        if (i2 == 1) {
            str3 = ia.b;
        } else if (i2 == 2) {
            str3 = ia.d;
        } else if (i2 != 0) {
            return;
        } else {
            str3 = ia.c;
        }
        a(context, c2, str3, a3);
    }

    private static void a(final Context context, final jw jwVar, final String str) {
        ic.d().submit(new Runnable() { // from class: com.amap.api.mapcore.util.id.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    synchronized (id.class) {
                        jc a2 = jj.a(id.a);
                        jj.a(context, a2, str, 1000, 40960, AmapLoc.RESULT_TYPE_WIFI_ONLY);
                        a2.f = jwVar;
                        if (a2.g == null) {
                            a2.g = new jn(new jm(context, new jr(), new hs(new hu(new hw())), "EImtleSI6IiVzIiwicGxhdGZvcm0iOiJhbmRyb2lkIiwiZGl1IjoiJXMiLCJwa2ciOiIlcyIsIm1vZGVsIjoiJXMiLCJhcHBuYW1lIjoiJXMiLCJhcHB2ZXJzaW9uIjoiJXMiLCJzeXN2ZXJzaW9uIjoiJXMiLA=", hd.f(context), hi.u(context), hd.c(context), Build.MODEL, hd.b(context), hd.d(context), Build.VERSION.RELEASE));
                        }
                        a2.h = DateUtils.MILLIS_IN_HOUR;
                        jd.a(a2);
                    }
                } catch (Throwable th) {
                    ic.c(th, "lg", "pul");
                }
            }
        });
    }

    private static void a(Context context, String str, String str2, String str3) {
        jc a2 = jj.a(a);
        jj.a(context, a2, str2, 1000, 40960, AmapLoc.RESULT_TYPE_WIFI_ONLY);
        if (a2.e == null) {
            a2.e = new hr(new hs(new hu(new hw())));
        }
        try {
            jd.a(str, hp.a(str3.replaceAll("\n", "<br/>")), a2);
        } catch (Throwable unused) {
        }
    }

    public static void a(Context context, Throwable th, int i2, String str, String str2) {
        String a2 = hp.a(th);
        ho a3 = a(context, a2);
        if (a(a3)) {
            String replaceAll = a2.replaceAll("\n", "<br/>");
            String a4 = a(th);
            if (a4 == null || "".equals(a4)) {
                return;
            }
            StringBuilder sb = new StringBuilder();
            if (str != null) {
                sb.append("class:");
                sb.append(str);
            }
            if (str2 != null) {
                sb.append(" method:");
                sb.append(str2);
                sb.append("$");
                sb.append("<br/>");
            }
            sb.append(replaceAll);
            a(context, a3, i2, a4, sb.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(ho hoVar, Context context, String str, String str2) {
        if (!a(hoVar) || str == null || "".equals(str)) {
            return;
        }
        a(context, hoVar, 0, str, str2);
    }

    private static void a(String str) {
        try {
            if (f > 9) {
                f = 0;
            }
            e[f] = str;
            f++;
        } catch (Throwable th) {
            ic.c(th, "alg", "aDa");
        }
    }

    private static boolean a(ho hoVar) {
        return hoVar != null && hoVar.f();
    }

    private static String b() {
        StringBuilder sb = new StringBuilder();
        try {
            for (int i2 = f; i2 < 10 && i2 <= 9; i2++) {
                sb.append(e[i2]);
            }
            for (int i3 = 0; i3 < f; i3++) {
                sb.append(e[i3]);
            }
        } catch (Throwable th) {
            ic.c(th, "alg", "gLI");
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b(Context context) {
        ju juVar = new ju(b);
        b = false;
        a(context, juVar, ia.c);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b(ho hoVar, Context context, String str, String str2) {
        if (!a(hoVar) || str == null || "".equals(str)) {
            return;
        }
        a(context, hoVar, 1, str, str2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void c(Context context) {
        if (c == null || c.get() == null) {
            c = new WeakReference<>(new jv(context, DateUtils.MILLIS_IN_HOUR, "hKey", new jx(context, false)));
        }
        a(context, c.get(), ia.d);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void d(Context context) {
        if (d == null || d.get() == null) {
            d = new WeakReference<>(new jv(context, DateUtils.MILLIS_IN_HOUR, "gKey", new jx(context, false)));
        }
        a(context, d.get(), ia.b);
    }
}
