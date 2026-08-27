package com.amap.api.mapcore.util;

import com.amap.api.mapcore.util.in;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Iterator;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/* compiled from: LogEngine.java */
/* loaded from: classes.dex */
public class jd {
    private static void a(in inVar) {
        if (inVar != null) {
            try {
                inVar.f();
            } catch (Throwable th) {
                ic.c(th, "ofm", "dlo");
            }
        }
    }

    private static void a(in inVar, List<String> list) {
        if (inVar != null) {
            try {
                Iterator<String> it = list.iterator();
                while (it.hasNext()) {
                    inVar.c(it.next());
                }
                inVar.close();
            } catch (Throwable th) {
                ic.c(th, "ofm", "dlo");
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x0077, code lost:
    
        r1.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x007b, code lost:
    
        r8 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x007c, code lost:
    
        r8.printStackTrace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x007f, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void a(com.amap.api.mapcore.util.jc r8) {
        /*
            r0 = 0
            com.amap.api.mapcore.util.jw r1 = r8.f     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L93
            boolean r1 = r1.c()     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L93
            if (r1 == 0) goto L85
            com.amap.api.mapcore.util.jw r1 = r8.f     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L93
            r2 = 1
            r1.a(r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L93
            java.io.File r1 = new java.io.File     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L93
            java.lang.String r3 = r8.a     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L93
            r1.<init>(r3)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L93
            long r3 = r8.b     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L93
            com.amap.api.mapcore.util.in r1 = com.amap.api.mapcore.util.in.a(r1, r2, r2, r3)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L93
            java.util.ArrayList r3 = new java.util.ArrayList     // Catch: java.lang.Throwable -> L80 java.lang.Throwable -> L82
            r3.<init>()     // Catch: java.lang.Throwable -> L80 java.lang.Throwable -> L82
            byte[] r4 = a(r1, r8, r3)     // Catch: java.lang.Throwable -> L80 java.lang.Throwable -> L82
            if (r4 == 0) goto L75
            int r5 = r4.length     // Catch: java.lang.Throwable -> L80 java.lang.Throwable -> L82
            if (r5 != 0) goto L2b
            goto L75
        L2b:
            com.amap.api.mapcore.util.ib r5 = new com.amap.api.mapcore.util.ib     // Catch: java.lang.Throwable -> L80 java.lang.Throwable -> L82
            java.lang.String r6 = r8.c     // Catch: java.lang.Throwable -> L80 java.lang.Throwable -> L82
            r5.<init>(r4, r6)     // Catch: java.lang.Throwable -> L80 java.lang.Throwable -> L82
            com.amap.api.mapcore.util.is r6 = com.amap.api.mapcore.util.is.a()     // Catch: java.lang.Throwable -> L80 java.lang.Throwable -> L82
            byte[] r5 = r6.b(r5)     // Catch: java.lang.Throwable -> L80 java.lang.Throwable -> L82
            org.json.JSONObject r6 = new org.json.JSONObject     // Catch: java.lang.Throwable -> L80 java.lang.Throwable -> L82
            java.lang.String r7 = new java.lang.String     // Catch: java.lang.Throwable -> L80 java.lang.Throwable -> L82
            r7.<init>(r5)     // Catch: java.lang.Throwable -> L80 java.lang.Throwable -> L82
            r6.<init>(r7)     // Catch: java.lang.Throwable -> L80 java.lang.Throwable -> L82
            java.lang.String r5 = "code"
            boolean r5 = r6.has(r5)     // Catch: java.lang.Throwable -> L80 java.lang.Throwable -> L82
            if (r5 == 0) goto L73
            java.lang.String r5 = "code"
            int r5 = r6.getInt(r5)     // Catch: java.lang.Throwable -> L80 java.lang.Throwable -> L82
            if (r5 != r2) goto L73
            com.amap.api.mapcore.util.jw r2 = r8.f     // Catch: java.lang.Throwable -> L80 java.lang.Throwable -> L82
            if (r2 == 0) goto L60
            if (r4 == 0) goto L60
            com.amap.api.mapcore.util.jw r2 = r8.f     // Catch: java.lang.Throwable -> L80 java.lang.Throwable -> L82
            int r4 = r4.length     // Catch: java.lang.Throwable -> L80 java.lang.Throwable -> L82
            r2.a(r4)     // Catch: java.lang.Throwable -> L80 java.lang.Throwable -> L82
        L60:
            com.amap.api.mapcore.util.jw r8 = r8.f     // Catch: java.lang.Throwable -> L80 java.lang.Throwable -> L82
            int r8 = r8.b()     // Catch: java.lang.Throwable -> L80 java.lang.Throwable -> L82
            r2 = 2147483647(0x7fffffff, float:NaN)
            if (r8 >= r2) goto L6f
            a(r1, r3)     // Catch: java.lang.Throwable -> L80 java.lang.Throwable -> L82
            goto L85
        L6f:
            a(r1)     // Catch: java.lang.Throwable -> L80 java.lang.Throwable -> L82
            goto L85
        L73:
            r0 = r1
            goto L85
        L75:
            if (r1 == 0) goto L7f
            r1.close()     // Catch: java.lang.Throwable -> L7b
            goto L7f
        L7b:
            r8 = move-exception
            r8.printStackTrace()
        L7f:
            return
        L80:
            r8 = move-exception
            goto La1
        L82:
            r8 = move-exception
            r0 = r1
            goto L94
        L85:
            if (r0 == 0) goto La0
            r0.close()     // Catch: java.lang.Throwable -> L8b
            goto La0
        L8b:
            r8 = move-exception
            r8.printStackTrace()
            goto La0
        L90:
            r8 = move-exception
            r1 = r0
            goto La1
        L93:
            r8 = move-exception
        L94:
            java.lang.String r1 = "leg"
            java.lang.String r2 = "uts"
            com.amap.api.mapcore.util.ic.c(r8, r1, r2)     // Catch: java.lang.Throwable -> L90
            if (r0 == 0) goto La0
            r0.close()     // Catch: java.lang.Throwable -> L8b
        La0:
            return
        La1:
            if (r1 == 0) goto Lab
            r1.close()     // Catch: java.lang.Throwable -> La7
            goto Lab
        La7:
            r0 = move-exception
            r0.printStackTrace()
        Lab:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.jd.a(com.amap.api.mapcore.util.jc):void");
    }

    public static void a(String str, byte[] bArr, jc jcVar) throws IOException, CertificateException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        in inVar;
        OutputStream outputStream = null;
        try {
            if (a(jcVar.a, str)) {
                return;
            }
            File file = new File(jcVar.a);
            if (!file.exists()) {
                file.mkdirs();
            }
            inVar = in.a(file, 1, 1, jcVar.b);
            try {
                inVar.a(jcVar.d);
                byte[] b = jcVar.e.b(bArr);
                in.a b2 = inVar.b(str);
                OutputStream a = b2.a(0);
                try {
                    a.write(b);
                    b2.a();
                    inVar.e();
                    if (a != null) {
                        try {
                            a.close();
                        } catch (Throwable th) {
                            th.printStackTrace();
                        }
                    }
                    if (inVar != null) {
                        try {
                            inVar.close();
                        } catch (Throwable th2) {
                            th2.printStackTrace();
                        }
                    }
                } catch (Throwable th3) {
                    th = th3;
                    outputStream = a;
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (Throwable th4) {
                            th4.printStackTrace();
                        }
                    }
                    if (inVar == null) {
                        throw th;
                    }
                    try {
                        inVar.close();
                        throw th;
                    } catch (Throwable th5) {
                        th5.printStackTrace();
                        throw th;
                    }
                }
            } catch (Throwable th6) {
                th = th6;
            }
        } catch (Throwable th7) {
            th = th7;
            inVar = null;
        }
    }

    private static boolean a(String str, String str2) {
        try {
            return new File(str, str2 + ".0").exists();
        } catch (Throwable th) {
            ic.c(th, "leg", "fet");
            return false;
        }
    }

    private static byte[] a(in inVar, jc jcVar, List<String> list) {
        try {
            File c = inVar.c();
            if (c != null && c.exists()) {
                int i = 0;
                for (String str : c.list()) {
                    if (str.contains(".0")) {
                        String str2 = str.split("\\.")[0];
                        byte[] a = jj.a(inVar, str2, false);
                        i += a.length;
                        list.add(str2);
                        if (i > jcVar.f.b()) {
                            break;
                        }
                        jcVar.g.b(a);
                    }
                }
                return jcVar.g.a();
            }
        } catch (Throwable th) {
            ic.c(th, "leg", "gCo");
        }
        return new byte[0];
    }
}
