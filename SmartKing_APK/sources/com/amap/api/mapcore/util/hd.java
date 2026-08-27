package com.amap.api.mapcore.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import com.amap.location.common.model.AmapLoc;
import java.io.File;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.util.Locale;

/* compiled from: AppInfo.java */
/* loaded from: classes.dex */
public class hd {
    static String a = null;
    static boolean b = false;
    private static String c = "";
    private static String d = "";
    private static String e = "";
    private static String f = "";

    public static String a(Context context) {
        try {
            return h(context);
        } catch (Throwable th) {
            th.printStackTrace();
            return f;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        f = str;
        if (context != null) {
            b(context, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a() {
        try {
            if (b) {
                return true;
            }
            if (a(a)) {
                b = true;
                return true;
            }
            if (!TextUtils.isEmpty(a)) {
                b = false;
                a = null;
                return false;
            }
            if (a(d)) {
                b = true;
                return true;
            }
            if (TextUtils.isEmpty(d)) {
                return true;
            }
            b = false;
            d = null;
            return false;
        } catch (Throwable unused) {
            return true;
        }
    }

    private static boolean a(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        str.toCharArray();
        for (char c2 : str.toCharArray()) {
            if (('A' > c2 || c2 > 'z') && (('0' > c2 || c2 > ':') && c2 != '.')) {
                try {
                    ic.b(hp.a(), str, "errorPackage");
                } catch (Throwable unused) {
                }
                return false;
            }
        }
        return true;
    }

    public static String b(Context context) {
        try {
        } catch (Throwable th) {
            hz.a(th, "AI", "gAN");
        }
        if (!"".equals(c)) {
            return c;
        }
        PackageManager packageManager = context.getPackageManager();
        c = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(context.getPackageName(), 0));
        return c;
    }

    private static void b(final Context context, final String str) {
        ic.d().submit(new Runnable() { // from class: com.amap.api.mapcore.util.hd.1
            @Override // java.lang.Runnable
            public void run() {
                FileOutputStream fileOutputStream;
                FileOutputStream fileOutputStream2 = null;
                try {
                    try {
                        try {
                            File file = new File(ia.c(context, "k.store"));
                            if (!file.getParentFile().exists()) {
                                file.getParentFile().mkdirs();
                            }
                            fileOutputStream = new FileOutputStream(file);
                        } catch (Throwable th) {
                            th = th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                    }
                    try {
                        fileOutputStream.write(hp.a(str));
                        fileOutputStream.close();
                    } catch (Throwable th3) {
                        th = th3;
                        fileOutputStream2 = fileOutputStream;
                        if (fileOutputStream2 != null) {
                            try {
                                fileOutputStream2.close();
                            } catch (Throwable th4) {
                                th4.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th5) {
                    th5.printStackTrace();
                }
            }
        });
    }

    public static String c(Context context) {
        try {
        } catch (Throwable th) {
            hz.a(th, "AI", "gpck");
        }
        if (d != null && !"".equals(d)) {
            return d;
        }
        d = context.getPackageName();
        if (!a(d)) {
            d = context.getPackageName();
        }
        return d;
    }

    public static String d(Context context) {
        try {
        } catch (Throwable th) {
            hz.a(th, "AI", "gAV");
        }
        if (!"".equals(e)) {
            return e;
        }
        e = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        return e == null ? "" : e;
    }

    public static String e(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 64);
            byte[] digest = MessageDigest.getInstance("SHA1").digest(packageInfo.signatures[0].toByteArray());
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b2 : digest) {
                String upperCase = Integer.toHexString(b2 & 255).toUpperCase(Locale.US);
                if (upperCase.length() == 1) {
                    stringBuffer.append(AmapLoc.RESULT_TYPE_GPS);
                }
                stringBuffer.append(upperCase);
                stringBuffer.append(":");
            }
            String str = packageInfo.packageName;
            if (a(str)) {
                str = packageInfo.packageName;
            }
            if (!TextUtils.isEmpty(d)) {
                str = c(context);
            }
            stringBuffer.append(str);
            a = stringBuffer.toString();
            return a;
        } catch (Throwable th) {
            hz.a(th, "AI", "gsp");
            return a;
        }
    }

    public static String f(Context context) {
        try {
            return h(context);
        } catch (Throwable th) {
            hz.a(th, "AI", "gKy");
            return f;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x006a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.lang.String g(android.content.Context r5) {
        /*
            java.lang.String r0 = "k.store"
            java.lang.String r5 = com.amap.api.mapcore.util.ia.c(r5, r0)
            java.io.File r0 = new java.io.File
            r0.<init>(r5)
            boolean r5 = r0.exists()
            if (r5 != 0) goto L14
            java.lang.String r5 = ""
            return r5
        L14:
            r5 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L3d java.lang.Throwable -> L41
            r1.<init>(r0)     // Catch: java.lang.Throwable -> L3d java.lang.Throwable -> L41
            int r5 = r1.available()     // Catch: java.lang.Throwable -> L3b java.lang.Throwable -> L67
            byte[] r5 = new byte[r5]     // Catch: java.lang.Throwable -> L3b java.lang.Throwable -> L67
            r1.read(r5)     // Catch: java.lang.Throwable -> L3b java.lang.Throwable -> L67
            java.lang.String r5 = com.amap.api.mapcore.util.hp.a(r5)     // Catch: java.lang.Throwable -> L3b java.lang.Throwable -> L67
            int r2 = r5.length()     // Catch: java.lang.Throwable -> L3b java.lang.Throwable -> L67
            r3 = 32
            if (r2 != r3) goto L30
            goto L32
        L30:
            java.lang.String r5 = ""
        L32:
            r1.close()     // Catch: java.lang.Throwable -> L36
            goto L3a
        L36:
            r0 = move-exception
            r0.printStackTrace()
        L3a:
            return r5
        L3b:
            r5 = move-exception
            goto L45
        L3d:
            r0 = move-exception
            r1 = r5
            r5 = r0
            goto L68
        L41:
            r1 = move-exception
            r4 = r1
            r1 = r5
            r5 = r4
        L45:
            java.lang.String r2 = "AI"
            java.lang.String r3 = "gKe"
            com.amap.api.mapcore.util.hz.a(r5, r2, r3)     // Catch: java.lang.Throwable -> L67
            boolean r5 = r0.exists()     // Catch: java.lang.Throwable -> L56 java.lang.Throwable -> L67
            if (r5 == 0) goto L5a
            r0.delete()     // Catch: java.lang.Throwable -> L56 java.lang.Throwable -> L67
            goto L5a
        L56:
            r5 = move-exception
            r5.printStackTrace()     // Catch: java.lang.Throwable -> L67
        L5a:
            if (r1 == 0) goto L64
            r1.close()     // Catch: java.lang.Throwable -> L60
            goto L64
        L60:
            r5 = move-exception
            r5.printStackTrace()
        L64:
            java.lang.String r5 = ""
            return r5
        L67:
            r5 = move-exception
        L68:
            if (r1 == 0) goto L72
            r1.close()     // Catch: java.lang.Throwable -> L6e
            goto L72
        L6e:
            r0 = move-exception
            r0.printStackTrace()
        L72:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.hd.g(android.content.Context):java.lang.String");
    }

    private static String h(Context context) throws PackageManager.NameNotFoundException {
        if (f == null || f.equals("")) {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo == null || applicationInfo.metaData == null) {
                return f;
            }
            f = applicationInfo.metaData.getString("com.amap.api.v2.apikey");
            if (f == null) {
                f = g(context);
            }
        }
        return f;
    }
}
