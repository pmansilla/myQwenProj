package com.loc;

import com.amap.api.maps.AMapException;
import java.net.URLConnection;

/* compiled from: BaseNetManager.java */
/* loaded from: classes.dex */
public final class bg {
    public static int a = 0;
    public static String b = "";
    private static bg c;

    /* compiled from: BaseNetManager.java */
    /* loaded from: classes.dex */
    public interface a {
        URLConnection a();
    }

    public static bg a() {
        if (c == null) {
            c = new bg();
        }
        return c;
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x007b A[Catch: Throwable -> 0x0099, t -> 0x00a5, TryCatch #2 {t -> 0x00a5, Throwable -> 0x0099, blocks: (B:3:0x0002, B:5:0x0008, B:7:0x0014, B:10:0x001c, B:12:0x002b, B:15:0x002f, B:18:0x0036, B:19:0x0057, B:21:0x006a, B:23:0x007f, B:26:0x006d, B:28:0x007b, B:29:0x0053, B:30:0x001a, B:31:0x0089, B:32:0x0090, B:33:0x0091, B:34:0x0098), top: B:1:0x0000 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.loc.bk a(com.loc.bj r8, boolean r9) throws com.loc.t {
        /*
            if (r8 == 0) goto L91
            java.lang.String r0 = r8.c()     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            if (r0 == 0) goto L89
            java.lang.String r0 = ""
            java.lang.String r1 = r8.c()     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            boolean r0 = r0.equals(r1)     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            if (r0 != 0) goto L89
            java.net.Proxy r0 = r8.e     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            if (r0 != 0) goto L1a
            r0 = 0
            goto L1c
        L1a:
            java.net.Proxy r0 = r8.e     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
        L1c:
            com.loc.bi r1 = new com.loc.bi     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            int r2 = r8.c     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            int r3 = r8.d     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            r1.<init>(r2, r3, r0, r9)     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            byte[] r9 = r8.d()     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            if (r9 == 0) goto L53
            int r9 = r9.length     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            if (r9 != 0) goto L2f
            goto L53
        L2f:
            java.util.Map r9 = r8.b_()     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            if (r9 != 0) goto L36
            goto L53
        L36:
            java.lang.String r9 = com.loc.bi.a(r9)     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            java.lang.StringBuffer r0 = new java.lang.StringBuffer     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            r0.<init>()     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            java.lang.String r2 = r8.c()     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            r0.append(r2)     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            java.lang.String r2 = "?"
            r0.append(r2)     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            r0.append(r9)     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            java.lang.String r9 = r0.toString()     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            goto L57
        L53:
            java.lang.String r9 = r8.c()     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
        L57:
            r2 = r9
            boolean r3 = r8.k()     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            java.lang.String r4 = r8.j()     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            java.util.Map r5 = r8.b()     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            byte[] r9 = r8.d()     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            if (r9 == 0) goto L6d
            int r0 = r9.length     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            if (r0 != 0) goto L7f
        L6d:
            java.util.Map r0 = r8.b_()     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            java.lang.String r0 = com.loc.bi.a(r0)     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            boolean r6 = android.text.TextUtils.isEmpty(r0)     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            if (r6 != 0) goto L7f
            byte[] r9 = com.loc.ad.a(r0)     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
        L7f:
            r6 = r9
            boolean r7 = r8.l()     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            com.loc.bk r8 = r1.a(r2, r3, r4, r5, r6, r7)     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            return r8
        L89:
            com.loc.t r8 = new com.loc.t     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            java.lang.String r9 = "request url is empty"
            r8.<init>(r9)     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            throw r8     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
        L91:
            com.loc.t r8 = new com.loc.t     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            java.lang.String r9 = "requeust is null"
            r8.<init>(r9)     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
            throw r8     // Catch: java.lang.Throwable -> L99 com.loc.t -> La5
        L99:
            r8 = move-exception
            r8.printStackTrace()
            com.loc.t r8 = new com.loc.t
            java.lang.String r9 = "未知的错误"
            r8.<init>(r9)
            throw r8
        La5:
            r8 = move-exception
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.bg.a(com.loc.bj, boolean):com.loc.bk");
    }

    public static byte[] a(bj bjVar) throws t {
        try {
            bk a2 = a(bjVar, true);
            if (a2 != null) {
                return a2.a;
            }
            return null;
        } catch (t e) {
            throw e;
        }
    }

    public static byte[] b(bj bjVar) throws t {
        try {
            bk a2 = a(bjVar, false);
            if (a2 != null) {
                return a2.a;
            }
            return null;
        } catch (t e) {
            throw e;
        } catch (Throwable th) {
            an.a(th, "bm", "msp");
            throw new t(AMapException.ERROR_UNKNOWN);
        }
    }
}
