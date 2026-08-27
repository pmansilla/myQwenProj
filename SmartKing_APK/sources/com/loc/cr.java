package com.loc;

import android.content.Context;
import android.text.TextUtils;
import com.amap.location.common.network.IHttpClient;
import com.amap.location.security.Core;
import com.loc.ac;
import com.sun.mail.imap.IMAPStore;

/* compiled from: HttpClient.java */
/* loaded from: classes.dex */
public final class cr implements IHttpClient {
    private static ac e;
    Context a;
    private String b = null;
    private int c = 1;
    private ci d = null;

    public cr(Context context) {
        this.a = null;
        this.a = context;
    }

    private ac a() {
        try {
            String b = this.d.b();
            String[] strArr = {"com.amap.api.location", "com.loc", "com.amap.api.fence", "com.amap.co"};
            if (e == null) {
                e = new ac.a("loc", b, "AMAP_Location_SDK_Android " + b).a(strArr).a(b).a();
            }
        } catch (Throwable unused) {
        }
        return e;
    }

    private static String a(String str) {
        String[] split;
        if (TextUtils.isEmpty(str) || (split = str.split("\\$")) == null) {
            return null;
        }
        return split[0];
    }

    private boolean a(String str, byte[] bArr) {
        try {
            if (str.contains("http://control.aps.amap.com/conf/r?type=3&mid=300&sver=140")) {
                String str2 = new String(Core.xxt(a(bArr), -1), "UTF-8");
                if (this.c == -1) {
                    return true;
                }
                int a = cl.a(this.a, "his_config", IMAPStore.ID_VERSION);
                if (a == -1) {
                    cl.a(this.a, "his_config", str2, this.c);
                    return true;
                }
                String b = cl.b(this.a, "his_config", IMAPStore.ID_COMMAND);
                if (!a(b).equals(a(str2))) {
                    if (a != this.c) {
                        cl.a(this.a, "his_config", str2, this.c);
                        return true;
                    }
                    cl.a(this.a, "LocationCloudConfig", IMAPStore.ID_COMMAND, b);
                    try {
                        aq.b(a(), "cloudcheck", "云控项有改变，版本号未变");
                        return false;
                    } catch (Throwable unused) {
                        return false;
                    }
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0057 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:43:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0050 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0049 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v10, types: [java.io.ByteArrayOutputStream] */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v4, types: [java.io.ByteArrayOutputStream] */
    /* JADX WARN: Type inference failed for: r2v5 */
    /* JADX WARN: Type inference failed for: r2v8 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static byte[] a(byte[] r7) {
        /*
            r0 = 0
            java.io.ByteArrayInputStream r1 = new java.io.ByteArrayInputStream     // Catch: java.lang.Throwable -> L42 java.lang.Throwable -> L5b
            r1.<init>(r7)     // Catch: java.lang.Throwable -> L42 java.lang.Throwable -> L5b
            java.util.zip.GZIPInputStream r7 = new java.util.zip.GZIPInputStream     // Catch: java.lang.Throwable -> L3c java.lang.Throwable -> L3f
            r7.<init>(r1)     // Catch: java.lang.Throwable -> L3c java.lang.Throwable -> L3f
            java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream     // Catch: java.lang.Throwable -> L35 java.lang.Throwable -> L3a
            r2.<init>()     // Catch: java.lang.Throwable -> L35 java.lang.Throwable -> L3a
            r3 = 512(0x200, float:7.175E-43)
            byte[] r3 = new byte[r3]     // Catch: java.lang.Throwable -> L31 java.lang.Throwable -> L33
        L14:
            int r4 = r7.read(r3)     // Catch: java.lang.Throwable -> L31 java.lang.Throwable -> L33
            if (r4 < 0) goto L1f
            r5 = 0
            r2.write(r3, r5, r4)     // Catch: java.lang.Throwable -> L31 java.lang.Throwable -> L33
            goto L14
        L1f:
            r2.flush()     // Catch: java.lang.Throwable -> L31 java.lang.Throwable -> L33
            byte[] r3 = r2.toByteArray()     // Catch: java.lang.Throwable -> L31 java.lang.Throwable -> L33
            r1.close()     // Catch: java.io.IOException -> L29
        L29:
            r7.close()     // Catch: java.io.IOException -> L2c
        L2c:
            r2.close()     // Catch: java.io.IOException -> L2f
        L2f:
            r0 = r3
            goto L71
        L31:
            r0 = move-exception
            goto L47
        L33:
            goto L5e
        L35:
            r2 = move-exception
            r6 = r2
            r2 = r0
            r0 = r6
            goto L47
        L3a:
            r2 = r0
            goto L5e
        L3c:
            r7 = move-exception
            r2 = r0
            goto L45
        L3f:
            r7 = r0
            r2 = r7
            goto L5e
        L42:
            r7 = move-exception
            r1 = r0
            r2 = r1
        L45:
            r0 = r7
            r7 = r2
        L47:
            if (r1 == 0) goto L4e
            r1.close()     // Catch: java.io.IOException -> L4d
            goto L4e
        L4d:
        L4e:
            if (r7 == 0) goto L55
            r7.close()     // Catch: java.io.IOException -> L54
            goto L55
        L54:
        L55:
            if (r2 == 0) goto L5a
            r2.close()     // Catch: java.io.IOException -> L5a
        L5a:
            throw r0
        L5b:
            r7 = r0
            r1 = r7
            r2 = r1
        L5e:
            if (r1 == 0) goto L65
            r1.close()     // Catch: java.io.IOException -> L64
            goto L65
        L64:
        L65:
            if (r7 == 0) goto L6c
            r7.close()     // Catch: java.io.IOException -> L6b
            goto L6c
        L6b:
        L6c:
            if (r2 == 0) goto L71
            r2.close()     // Catch: java.io.IOException -> L71
        L71:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.cr.a(byte[]):byte[]");
    }

    public final void a(int i) {
        this.c = i;
    }

    public final void a(ci ciVar) {
        this.d = ciVar;
        if (ciVar != null) {
            this.b = ciVar.d();
        }
        if (TextUtils.isEmpty(this.b)) {
            this.b = u.f(this.a);
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(14:1|(4:(1:3)(2:42|(1:44)(14:45|5|6|7|8|9|(4:11|(1:13)(1:17)|(1:15)|16)|(1:19)|20|21|22|24|25|(2:27|28)(1:31)))|24|25|(0)(0))|4|5|6|7|8|9|(0)|(0)|20|21|22|(1:(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00da, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00db, code lost:
    
        r12 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0031, code lost:
    
        r6 = null;
     */
    /* JADX WARN: Removed duplicated region for block: B:11:0x004b A[Catch: Throwable -> 0x00ed, TryCatch #0 {Throwable -> 0x00ed, blocks: (B:9:0x0032, B:11:0x004b, B:13:0x0057, B:15:0x0085, B:16:0x008a, B:17:0x006d, B:19:0x00b6, B:20:0x00b9, B:22:0x00bd, B:25:0x00c4, B:27:0x00cc, B:34:0x00de), top: B:8:0x0032 }] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00b6 A[Catch: Throwable -> 0x00ed, TryCatch #0 {Throwable -> 0x00ed, blocks: (B:9:0x0032, B:11:0x004b, B:13:0x0057, B:15:0x0085, B:16:0x008a, B:17:0x006d, B:19:0x00b6, B:20:0x00b9, B:22:0x00bd, B:25:0x00c4, B:27:0x00cc, B:34:0x00de), top: B:8:0x0032 }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00cc A[Catch: t -> 0x00d8, Throwable -> 0x00ed, TRY_LEAVE, TryCatch #0 {Throwable -> 0x00ed, blocks: (B:9:0x0032, B:11:0x004b, B:13:0x0057, B:15:0x0085, B:16:0x008a, B:17:0x006d, B:19:0x00b6, B:20:0x00b9, B:22:0x00bd, B:25:0x00c4, B:27:0x00cc, B:34:0x00de), top: B:8:0x0032 }] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00d7 A[RETURN] */
    @Override // com.amap.location.common.network.IHttpClient
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.amap.location.common.network.HttpResponse post(com.amap.location.common.network.HttpRequest r12) {
        /*
            Method dump skipped, instructions count: 238
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.cr.post(com.amap.location.common.network.HttpRequest):com.amap.location.common.network.HttpResponse");
    }
}
