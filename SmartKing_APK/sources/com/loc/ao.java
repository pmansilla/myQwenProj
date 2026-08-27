package com.loc;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.litesuits.orm.db.assit.SQLBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

/* compiled from: Log.java */
/* loaded from: classes.dex */
public final class ao {
    public static final String a = "/a/";
    static final String b = "b";
    static final String c = "c";
    static final String d = "d";
    static final String e = "i";
    public static final String f = "g";
    public static final String g = "h";
    public static final String h = "e";
    public static final String i = "f";
    public static final String j = "j";

    public static String a(Context context) {
        return c(context, e);
    }

    public static String a(Context context, String str) {
        return context.getSharedPreferences("AMSKLG_CFG", 0).getString(str, "");
    }

    @TargetApi(9)
    public static void a(Context context, String str, String str2) {
        SharedPreferences.Editor edit = context.getSharedPreferences("AMSKLG_CFG", 0).edit();
        edit.putString(str, str2);
        edit.apply();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(String[] strArr, String str) {
        if (strArr != null && str != null) {
            try {
                for (String str2 : str.split("\n")) {
                    String trim = str2.trim();
                    if (!TextUtils.isEmpty(trim) && trim.contains("uncaughtException")) {
                        return false;
                    }
                    if (b(strArr, trim)) {
                        return true;
                    }
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        return false;
    }

    public static void b(final Context context) {
        try {
            ExecutorService d2 = aq.d();
            if (d2 != null && !d2.isShutdown()) {
                d2.submit(new Runnable() { // from class: com.loc.ao.1
                    @Override // java.lang.Runnable
                    public final void run() {
                        try {
                            bn.a(context);
                            ar.b(context);
                            ar.d(context);
                            ar.c(context);
                            br.a(context);
                            bp.a(context);
                        } catch (RejectedExecutionException unused) {
                        } catch (Throwable th) {
                            aq.b(th, "Lg", "proL");
                        }
                    }
                });
            }
        } catch (Throwable th) {
            aq.b(th, "Lg", "proL");
        }
    }

    public static void b(Context context, String str) {
        SharedPreferences.Editor edit = context.getSharedPreferences("AMSKLG_CFG", 0).edit();
        edit.remove(str);
        edit.apply();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean b(String[] strArr, String str) {
        if (strArr != null && str != null) {
            try {
                String str2 = str;
                for (String str3 : strArr) {
                    str2 = str2.trim();
                    if (str2.startsWith("at ")) {
                        if (str2.contains(str3 + ".") && str2.endsWith(SQLBuilder.PARENTHESES_RIGHT) && !str2.contains("uncaughtException")) {
                            return true;
                        }
                    }
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        return false;
    }

    public static String c(Context context, String str) {
        return context.getFilesDir().getAbsolutePath() + a + str;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:17:0x0017
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1166)
        	at jadx.core.dex.visitors.regions.RegionMaker.processTryCatchBlocks(RegionMaker.java:1022)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:55)
        */
    static java.util.List<com.loc.ac> c(android.content.Context r5) {
        /*
            r0 = 0
            android.os.Looper r1 = android.os.Looper.getMainLooper()     // Catch: java.lang.Throwable -> L1a
            monitor-enter(r1)     // Catch: java.lang.Throwable -> L1a
            com.loc.ba r2 = new com.loc.ba     // Catch: java.lang.Throwable -> L17
            r3 = 0
            r2.<init>(r5, r3)     // Catch: java.lang.Throwable -> L17
            java.util.List r5 = r2.a()     // Catch: java.lang.Throwable -> L17
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L12
            goto L1f
        L12:
            r0 = move-exception
            r4 = r0
            r0 = r5
            r5 = r4
            goto L18
        L17:
            r5 = move-exception
        L18:
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L17
            throw r5     // Catch: java.lang.Throwable -> L1a
        L1a:
            r5 = move-exception
            r5.printStackTrace()
            r5 = r0
        L1f:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ao.c(android.content.Context):java.util.List");
    }
}
