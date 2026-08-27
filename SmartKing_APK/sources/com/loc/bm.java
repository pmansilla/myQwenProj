package com.loc;

import com.loc.bc;
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
public final class bm {
    private static void a(bc bcVar, List<String> list) {
        if (bcVar != null) {
            try {
                Iterator<String> it = list.iterator();
                while (it.hasNext()) {
                    bcVar.c(it.next());
                }
                bcVar.close();
            } catch (Throwable th) {
                aq.b(th, "ofm", "dlo");
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x007f, code lost:
    
        r1.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0083, code lost:
    
        r8 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0084, code lost:
    
        r8.printStackTrace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0087, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void a(com.loc.bl r8) {
        /*
            r0 = 0
            com.loc.cf r1 = r8.f     // Catch: java.lang.Throwable -> L98 java.lang.Throwable -> L9b
            boolean r1 = r1.c()     // Catch: java.lang.Throwable -> L98 java.lang.Throwable -> L9b
            if (r1 == 0) goto L8d
            com.loc.cf r1 = r8.f     // Catch: java.lang.Throwable -> L98 java.lang.Throwable -> L9b
            r2 = 1
            r1.a(r2)     // Catch: java.lang.Throwable -> L98 java.lang.Throwable -> L9b
            java.io.File r1 = new java.io.File     // Catch: java.lang.Throwable -> L98 java.lang.Throwable -> L9b
            java.lang.String r3 = r8.a     // Catch: java.lang.Throwable -> L98 java.lang.Throwable -> L9b
            r1.<init>(r3)     // Catch: java.lang.Throwable -> L98 java.lang.Throwable -> L9b
            long r3 = r8.b     // Catch: java.lang.Throwable -> L98 java.lang.Throwable -> L9b
            com.loc.bc r1 = com.loc.bc.a(r1, r3)     // Catch: java.lang.Throwable -> L98 java.lang.Throwable -> L9b
            java.util.ArrayList r3 = new java.util.ArrayList     // Catch: java.lang.Throwable -> L88 java.lang.Throwable -> L8a
            r3.<init>()     // Catch: java.lang.Throwable -> L88 java.lang.Throwable -> L8a
            byte[] r4 = a(r1, r8, r3)     // Catch: java.lang.Throwable -> L88 java.lang.Throwable -> L8a
            if (r4 == 0) goto L7d
            int r5 = r4.length     // Catch: java.lang.Throwable -> L88 java.lang.Throwable -> L8a
            if (r5 != 0) goto L2b
            goto L7d
        L2b:
            com.loc.ap r5 = new com.loc.ap     // Catch: java.lang.Throwable -> L88 java.lang.Throwable -> L8a
            java.lang.String r6 = r8.c     // Catch: java.lang.Throwable -> L88 java.lang.Throwable -> L8a
            r5.<init>(r4, r6)     // Catch: java.lang.Throwable -> L88 java.lang.Throwable -> L8a
            com.loc.bg.a()     // Catch: java.lang.Throwable -> L88 java.lang.Throwable -> L8a
            byte[] r5 = com.loc.bg.b(r5)     // Catch: java.lang.Throwable -> L88 java.lang.Throwable -> L8a
            org.json.JSONObject r6 = new org.json.JSONObject     // Catch: java.lang.Throwable -> L88 java.lang.Throwable -> L8a
            java.lang.String r7 = new java.lang.String     // Catch: java.lang.Throwable -> L88 java.lang.Throwable -> L8a
            r7.<init>(r5)     // Catch: java.lang.Throwable -> L88 java.lang.Throwable -> L8a
            r6.<init>(r7)     // Catch: java.lang.Throwable -> L88 java.lang.Throwable -> L8a
            java.lang.String r5 = "code"
            boolean r5 = r6.has(r5)     // Catch: java.lang.Throwable -> L88 java.lang.Throwable -> L8a
            if (r5 == 0) goto L7b
            java.lang.String r5 = "code"
            int r5 = r6.getInt(r5)     // Catch: java.lang.Throwable -> L88 java.lang.Throwable -> L8a
            if (r5 != r2) goto L7b
            com.loc.cf r2 = r8.f     // Catch: java.lang.Throwable -> L88 java.lang.Throwable -> L8a
            if (r2 == 0) goto L5f
            if (r4 == 0) goto L5f
            com.loc.cf r2 = r8.f     // Catch: java.lang.Throwable -> L88 java.lang.Throwable -> L8a
            int r4 = r4.length     // Catch: java.lang.Throwable -> L88 java.lang.Throwable -> L8a
            r2.a(r4)     // Catch: java.lang.Throwable -> L88 java.lang.Throwable -> L8a
        L5f:
            com.loc.cf r8 = r8.f     // Catch: java.lang.Throwable -> L88 java.lang.Throwable -> L8a
            int r8 = r8.b()     // Catch: java.lang.Throwable -> L88 java.lang.Throwable -> L8a
            r2 = 2147483647(0x7fffffff, float:NaN)
            if (r8 >= r2) goto L6e
            a(r1, r3)     // Catch: java.lang.Throwable -> L88 java.lang.Throwable -> L8a
            goto L8d
        L6e:
            r1.d()     // Catch: java.lang.Throwable -> L72 java.lang.Throwable -> L88
            goto L8d
        L72:
            r8 = move-exception
            java.lang.String r2 = "ofm"
            java.lang.String r3 = "dlo"
            com.loc.aq.b(r8, r2, r3)     // Catch: java.lang.Throwable -> L88 java.lang.Throwable -> L8a
            goto L8d
        L7b:
            r0 = r1
            goto L8d
        L7d:
            if (r1 == 0) goto L87
            r1.close()     // Catch: java.lang.Throwable -> L83
            goto L87
        L83:
            r8 = move-exception
            r8.printStackTrace()
        L87:
            return
        L88:
            r8 = move-exception
            goto Lae
        L8a:
            r8 = move-exception
            r0 = r1
            goto L9c
        L8d:
            if (r0 == 0) goto L97
            r0.close()     // Catch: java.lang.Throwable -> L93
            goto L97
        L93:
            r8 = move-exception
            r8.printStackTrace()
        L97:
            return
        L98:
            r8 = move-exception
            r1 = r0
            goto Lae
        L9b:
            r8 = move-exception
        L9c:
            java.lang.String r1 = "leg"
            java.lang.String r2 = "uts"
            com.loc.aq.b(r8, r1, r2)     // Catch: java.lang.Throwable -> L98
            if (r0 == 0) goto Lad
            r0.close()     // Catch: java.lang.Throwable -> La9
            goto Lad
        La9:
            r8 = move-exception
            r8.printStackTrace()
        Lad:
            return
        Lae:
            if (r1 == 0) goto Lb8
            r1.close()     // Catch: java.lang.Throwable -> Lb4
            goto Lb8
        Lb4:
            r0 = move-exception
            r0.printStackTrace()
        Lb8:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.bm.a(com.loc.bl):void");
    }

    public static void a(String str, byte[] bArr, bl blVar) throws IOException, CertificateException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        bc bcVar;
        OutputStream outputStream = null;
        try {
            if (a(blVar.a, str)) {
                return;
            }
            File file = new File(blVar.a);
            if (!file.exists()) {
                file.mkdirs();
            }
            bcVar = bc.a(file, blVar.b);
            try {
                bcVar.a(blVar.d);
                byte[] b = blVar.e.b(bArr);
                bc.a b2 = bcVar.b(str);
                OutputStream a = b2.a();
                try {
                    a.write(b);
                    b2.b();
                    bcVar.c();
                    if (a != null) {
                        try {
                            a.close();
                        } catch (Throwable th) {
                            th.printStackTrace();
                        }
                    }
                    if (bcVar != null) {
                        try {
                            bcVar.close();
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
                    if (bcVar == null) {
                        throw th;
                    }
                    try {
                        bcVar.close();
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
            bcVar = null;
        }
    }

    private static boolean a(String str, String str2) {
        try {
            return new File(str, str2 + ".0").exists();
        } catch (Throwable th) {
            aq.b(th, "leg", "fet");
            return false;
        }
    }

    private static byte[] a(bc bcVar, bl blVar, List<String> list) {
        try {
            File b = bcVar.b();
            if (b != null && b.exists()) {
                int i = 0;
                for (String str : b.list()) {
                    if (str.contains(".0")) {
                        String str2 = str.split("\\.")[0];
                        byte[] a = bs.a(bcVar, str2);
                        i += a.length;
                        list.add(str2);
                        if (i > blVar.f.b()) {
                            break;
                        }
                        blVar.g.b(a);
                    }
                }
                return blVar.g.a();
            }
        } catch (Throwable th) {
            aq.b(th, "leg", "gCo");
        }
        return new byte[0];
    }
}
