package com.amap.api.mapcore.util;

import android.content.Context;
import android.net.Proxy;
import android.os.Build;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;

/* compiled from: ProxyUtil.java */
/* loaded from: classes.dex */
public class hm {
    private static String a() {
        String str;
        try {
            str = Proxy.getDefaultHost();
        } catch (Throwable th) {
            ic.c(th, "pu", "gdh");
            str = null;
        }
        return str == null ? "null" : str;
    }

    public static String a(String str) {
        return hp.c(str);
    }

    public static java.net.Proxy a(Context context) {
        try {
            return Build.VERSION.SDK_INT >= 11 ? a(context, new URI("http://restapi.amap.com")) : b(context);
        } catch (Throwable th) {
            ic.c(th, "pu", "gp");
            return null;
        }
    }

    private static java.net.Proxy a(Context context, URI uri) {
        java.net.Proxy proxy;
        if (c(context)) {
            try {
                List<java.net.Proxy> select = ProxySelector.getDefault().select(uri);
                if (select == null || select.isEmpty() || (proxy = select.get(0)) == null) {
                    return null;
                }
                if (proxy.type() == Proxy.Type.DIRECT) {
                    return null;
                }
                return proxy;
            } catch (Throwable th) {
                ic.c(th, "pu", "gpsc");
            }
        }
        return null;
    }

    private static boolean a(String str, int i) {
        return (str == null || str.length() <= 0 || i == -1) ? false : true;
    }

    private static int b() {
        try {
            return android.net.Proxy.getDefaultPort();
        } catch (Throwable th) {
            ic.c(th, "pu", "gdp");
            return -1;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:40:0x006f, code lost:
    
        if (r4 == (-1)) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0072, code lost:
    
        r9 = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00ac, code lost:
    
        if (r4 == (-1)) goto L58;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0155 A[Catch: Throwable -> 0x0161, TRY_LEAVE, TryCatch #5 {Throwable -> 0x0161, blocks: (B:14:0x014f, B:16:0x0155), top: B:13:0x014f }] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x00f2 A[Catch: all -> 0x016d, TryCatch #12 {all -> 0x016d, blocks: (B:23:0x0020, B:25:0x0026, B:27:0x0032, B:30:0x003a, B:32:0x0042, B:33:0x004a, B:35:0x0050, B:39:0x005f, B:90:0x00d0, B:60:0x00e5, B:62:0x00f2, B:64:0x0108, B:66:0x010e, B:70:0x011b, B:77:0x0126, B:79:0x012e, B:81:0x0134, B:85:0x0141, B:45:0x007f, B:47:0x0087, B:48:0x008f, B:50:0x0095, B:54:0x00a4), top: B:4:0x001a }] */
    /* JADX WARN: Removed duplicated region for block: B:74:0x014c A[Catch: Throwable -> 0x00be, TRY_ENTER, TRY_LEAVE, TryCatch #4 {Throwable -> 0x00be, blocks: (B:10:0x00b9, B:92:0x00dc, B:74:0x014c), top: B:5:0x001a }] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x00dc A[Catch: Throwable -> 0x00be, TRY_ENTER, TRY_LEAVE, TryCatch #4 {Throwable -> 0x00be, blocks: (B:10:0x00b9, B:92:0x00dc, B:74:0x014c), top: B:5:0x001a }] */
    /* JADX WARN: Type inference failed for: r2v0, types: [android.content.ContentResolver] */
    /* JADX WARN: Type inference failed for: r2v1, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r2v4 */
    /* JADX WARN: Type inference failed for: r3v0, types: [android.net.Uri] */
    /* JADX WARN: Type inference failed for: r3v12 */
    /* JADX WARN: Type inference failed for: r3v13 */
    /* JADX WARN: Type inference failed for: r3v14, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r3v15 */
    /* JADX WARN: Type inference failed for: r3v16 */
    /* JADX WARN: Type inference failed for: r3v17 */
    /* JADX WARN: Type inference failed for: r3v18, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r3v19, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r3v20 */
    /* JADX WARN: Type inference failed for: r3v26, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r3v28 */
    /* JADX WARN: Type inference failed for: r3v3 */
    /* JADX WARN: Type inference failed for: r3v30 */
    /* JADX WARN: Type inference failed for: r3v36 */
    /* JADX WARN: Type inference failed for: r3v37 */
    /* JADX WARN: Type inference failed for: r3v41 */
    /* JADX WARN: Type inference failed for: r3v45 */
    /* JADX WARN: Type inference failed for: r3v46 */
    /* JADX WARN: Type inference failed for: r3v51 */
    /* JADX WARN: Type inference failed for: r3v52 */
    /* JADX WARN: Type inference failed for: r3v53 */
    /* JADX WARN: Type inference failed for: r3v54 */
    /* JADX WARN: Type inference failed for: r3v55 */
    /* JADX WARN: Type inference failed for: r3v58 */
    /* JADX WARN: Type inference failed for: r3v59 */
    /* JADX WARN: Type inference failed for: r3v6 */
    /* JADX WARN: Type inference failed for: r3v60 */
    /* JADX WARN: Type inference failed for: r3v61 */
    /* JADX WARN: Type inference failed for: r3v62 */
    /* JADX WARN: Type inference failed for: r3v8 */
    /* JADX WARN: Type inference failed for: r3v9 */
    /* JADX WARN: Type inference failed for: r4v13, types: [java.util.Locale] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.net.Proxy b(android.content.Context r11) {
        /*
            Method dump skipped, instructions count: 382
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.hm.b(android.content.Context):java.net.Proxy");
    }

    private static boolean c(Context context) {
        return hi.q(context) == 0;
    }
}
