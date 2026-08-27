package com.loc;

import android.content.Context;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/* compiled from: Utils.java */
/* loaded from: classes.dex */
public final class bs {
    public static bl a(WeakReference<bl> weakReference) {
        if (weakReference == null || weakReference.get() == null) {
            weakReference = new WeakReference<>(new bl());
        }
        return weakReference.get();
    }

    public static String a(Context context, ac acVar) {
        StringBuilder sb = new StringBuilder();
        try {
            String f = x.f(context);
            sb.append("\"sim\":\"");
            sb.append(f);
            sb.append("\",\"sdkversion\":\"");
            sb.append(acVar.c());
            sb.append("\",\"product\":\"");
            sb.append(acVar.a());
            sb.append("\",\"ed\":\"");
            sb.append(acVar.d());
            sb.append("\",\"nt\":\"");
            sb.append(x.d(context));
            sb.append("\",\"np\":\"");
            sb.append(x.b(context));
            sb.append("\",\"mnc\":\"");
            sb.append(x.c(context));
            sb.append("\",\"ant\":\"");
            sb.append(x.e(context));
            sb.append("\"");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return sb.toString();
    }

    public static void a(Context context, bl blVar, String str, int i, int i2, String str2) {
        blVar.a = ao.c(context, str);
        blVar.d = i;
        blVar.b = i2;
        blVar.c = str2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v0, types: [com.loc.bc] */
    /* JADX WARN: Type inference failed for: r5v1 */
    /* JADX WARN: Type inference failed for: r5v3 */
    /* JADX WARN: Type inference failed for: r5v4, types: [com.loc.bc$b] */
    /* JADX WARN: Type inference failed for: r5v6, types: [com.loc.bc$b] */
    /* JADX WARN: Type inference failed for: r5v8, types: [com.loc.bc$b] */
    /* JADX WARN: Type inference failed for: r6v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r6v1 */
    /* JADX WARN: Type inference failed for: r6v3, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r6v9 */
    public static byte[] a(bc bcVar, String str) {
        InputStream inputStream;
        Throwable th;
        byte[] bArr = new byte[0];
        try {
            try {
                bcVar = bcVar.a(str);
                if (bcVar == 0) {
                    if (bcVar != 0) {
                        try {
                            bcVar.close();
                        } catch (Throwable th2) {
                            th2.printStackTrace();
                        }
                    }
                    return bArr;
                }
                try {
                    inputStream = bcVar.a();
                    if (inputStream == null) {
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (Throwable th3) {
                                th3.printStackTrace();
                            }
                        }
                        if (bcVar != 0) {
                            try {
                                bcVar.close();
                            } catch (Throwable th4) {
                                th4.printStackTrace();
                            }
                        }
                        return bArr;
                    }
                    try {
                        byte[] bArr2 = new byte[inputStream.available()];
                        try {
                            inputStream.read(bArr2);
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (Throwable th5) {
                                    th5.printStackTrace();
                                }
                            }
                            if (bcVar != 0) {
                                try {
                                    bcVar.close();
                                } catch (Throwable th6) {
                                    th6.printStackTrace();
                                }
                            }
                            return bArr2;
                        } catch (Throwable th7) {
                            th = th7;
                            bArr = bArr2;
                            aq.b(th, "sui", "rdS");
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (Throwable th8) {
                                    th8.printStackTrace();
                                }
                            }
                            if (bcVar != 0) {
                                try {
                                    bcVar.close();
                                } catch (Throwable th9) {
                                    th9.printStackTrace();
                                }
                            }
                            return bArr;
                        }
                    } catch (Throwable th10) {
                        th = th10;
                    }
                } catch (Throwable th11) {
                    th = th11;
                    inputStream = null;
                }
            } catch (Throwable th12) {
                th = th12;
            }
        } catch (Throwable th13) {
            th = th13;
            bcVar = 0;
            str = 0;
        }
    }
}
