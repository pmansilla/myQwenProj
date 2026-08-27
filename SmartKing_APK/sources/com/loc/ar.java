package com.loc;

import android.content.Context;
import android.os.Build;
import com.amap.location.common.model.AmapLoc;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.time.DateUtils;

/* compiled from: ErrorLogManager.java */
/* loaded from: classes.dex */
public class ar {
    private static WeakReference<bl> a = null;
    private static boolean b = true;
    private static WeakReference<cf> c;
    private static WeakReference<cf> d;
    private static String[] e = new String[10];
    private static int f = 0;
    private static boolean g = false;
    private static int h = 0;
    private static ac i;

    private static ac a(Context context, String str) {
        List<ac> c2 = ao.c(context);
        if (c2 == null) {
            c2 = new ArrayList();
        }
        if (str != null && !"".equals(str)) {
            for (ac acVar : c2) {
                if (ao.a(acVar.f(), str)) {
                    return acVar;
                }
            }
            if (str.contains("com.amap.api.col")) {
                try {
                    return ad.a();
                } catch (t e2) {
                    e2.printStackTrace();
                }
            }
            if (str.contains("com.amap.co") || str.contains("com.amap.opensdk.co") || str.contains("com.amap.location")) {
                try {
                    ac b2 = ad.b();
                    b2.a(true);
                    return b2;
                } catch (t e3) {
                    e3.printStackTrace();
                }
            }
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x0120  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0125 A[RETURN] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:107:0x00b6 -> B:29:0x011c). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.lang.String a(java.util.List<com.loc.ac> r10) {
        /*
            Method dump skipped, instructions count: 294
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ar.a(java.util.List):java.lang.String");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(Context context) {
        String a2;
        List<ac> c2 = ao.c(context);
        if (c2 == null || c2.size() == 0 || (a2 = a(c2)) == null || "".equals(a2) || i == null) {
            return;
        }
        a(context, i, 2, "ANR", a2);
    }

    private static void a(Context context, ac acVar, int i2, String str, String str2) {
        String str3;
        String a2 = ad.a(System.currentTimeMillis());
        String a3 = bs.a(context, acVar);
        u.a(context);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(a3);
        stringBuffer.append(",\"timestamp\":\"");
        stringBuffer.append(a2);
        stringBuffer.append("\",\"et\":\"");
        stringBuffer.append(i2);
        stringBuffer.append("\",\"classname\":\"");
        stringBuffer.append(str);
        stringBuffer.append("\",");
        stringBuffer.append("\"detail\":\"");
        stringBuffer.append(str2);
        stringBuffer.append("\"");
        String stringBuffer2 = stringBuffer.toString();
        if (stringBuffer2 == null || "".equals(stringBuffer2)) {
            return;
        }
        String c2 = aa.c(str2);
        if (i2 == 1) {
            str3 = ao.b;
        } else if (i2 == 2) {
            str3 = ao.d;
        } else if (i2 != 0) {
            return;
        } else {
            str3 = ao.c;
        }
        String str4 = str3;
        bl a4 = bs.a(a);
        bs.a(context, a4, str4, 1000, 40960, AmapLoc.RESULT_TYPE_WIFI_ONLY);
        if (a4.e == null) {
            a4.e = new af(new ag(new ai(new ak())));
        }
        try {
            bm.a(c2, ad.a(stringBuffer2.replaceAll("\n", "<br/>")), a4);
        } catch (Throwable unused) {
        }
    }

    private static void a(final Context context, final cf cfVar, final String str) {
        aq.d().submit(new Runnable() { // from class: com.loc.ar.1
            @Override // java.lang.Runnable
            public final void run() {
                try {
                    synchronized (ar.class) {
                        bl a2 = bs.a(ar.a);
                        bs.a(context, a2, str, 1000, 40960, AmapLoc.RESULT_TYPE_WIFI_ONLY);
                        a2.f = cfVar;
                        if (a2.g == null) {
                            a2.g = new bw(new bv(context, new ca(), new ag(new ai(new ak())), "EImtleSI6IiVzIiwicGxhdGZvcm0iOiJhbmRyb2lkIiwiZGl1IjoiJXMiLCJwa2ciOiIlcyIsIm1vZGVsIjoiJXMiLCJhcHBuYW1lIjoiJXMiLCJhcHB2ZXJzaW9uIjoiJXMiLCJzeXN2ZXJzaW9uIjoiJXMiLA=", u.f(context), x.u(context), u.c(context), Build.MODEL, u.b(context), u.d(context), Build.VERSION.RELEASE));
                        }
                        a2.h = DateUtils.MILLIS_IN_HOUR;
                        bm.a(a2);
                    }
                } catch (Throwable th) {
                    aq.b(th, "lg", "pul");
                }
            }
        });
    }

    public static void a(Context context, Throwable th, int i2, String str, String str2) {
        String a2 = ad.a(th);
        ac a3 = a(context, a2);
        if (a(a3)) {
            String replaceAll = a2.replaceAll("\n", "<br/>");
            String th2 = th.toString();
            if (th2 == null || "".equals(th2)) {
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
                sb.append("$<br/>");
            }
            sb.append(replaceAll);
            a(context, a3, i2, th2, sb.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(ac acVar, Context context, String str, String str2) {
        if (!a(acVar) || str == null || "".equals(str)) {
            return;
        }
        a(context, acVar, 0, str, str2);
    }

    private static boolean a(ac acVar) {
        return acVar != null && acVar.e();
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
            aq.b(th, "alg", "gLI");
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b(Context context) {
        cd cdVar = new cd(b);
        b = false;
        a(context, cdVar, ao.c);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b(ac acVar, Context context, String str, String str2) {
        if (!a(acVar) || str == null || "".equals(str)) {
            return;
        }
        a(context, acVar, 1, str, str2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void c(Context context) {
        if (c == null || c.get() == null) {
            c = new WeakReference<>(new ce(context, DateUtils.MILLIS_IN_HOUR, "hKey", new cg(context)));
        }
        a(context, c.get(), ao.d);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void d(Context context) {
        if (d == null || d.get() == null) {
            d = new WeakReference<>(new ce(context, DateUtils.MILLIS_IN_HOUR, "gKey", new cg(context)));
        }
        a(context, d.get(), ao.b);
    }
}
