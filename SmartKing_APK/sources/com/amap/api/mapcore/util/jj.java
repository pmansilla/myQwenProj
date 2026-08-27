package com.amap.api.mapcore.util;

import android.content.Context;
import com.amap.api.mapcore.util.in;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/* compiled from: Utils.java */
/* loaded from: classes.dex */
public class jj {
    public static jc a(WeakReference<jc> weakReference) {
        if (weakReference == null || weakReference.get() == null) {
            weakReference = new WeakReference<>(new jc());
        }
        return weakReference.get();
    }

    public static String a() {
        return hp.a(System.currentTimeMillis());
    }

    public static String a(Context context, ho hoVar) {
        StringBuilder sb = new StringBuilder();
        try {
            String g = hi.g(context);
            sb.append("\"sim\":\"");
            sb.append(g);
            sb.append("\",\"sdkversion\":\"");
            sb.append(hoVar.c());
            sb.append("\",\"product\":\"");
            sb.append(hoVar.a());
            sb.append("\",\"ed\":\"");
            sb.append(hoVar.e());
            sb.append("\",\"nt\":\"");
            sb.append(hi.e(context));
            sb.append("\",\"np\":\"");
            sb.append(hi.c(context));
            sb.append("\",\"mnc\":\"");
            sb.append(hi.d(context));
            sb.append("\",\"ant\":\"");
            sb.append(hi.f(context));
            sb.append("\"");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return sb.toString();
    }

    public static String a(String str, String str2, String str3, int i, String str4, String str5) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str2);
        stringBuffer.append(",");
        stringBuffer.append("\"timestamp\":\"");
        stringBuffer.append(str3);
        stringBuffer.append("\",\"et\":\"");
        stringBuffer.append(i);
        stringBuffer.append("\",\"classname\":\"");
        stringBuffer.append(str4);
        stringBuffer.append("\",");
        stringBuffer.append("\"detail\":\"");
        stringBuffer.append(str5);
        stringBuffer.append("\"");
        return stringBuffer.toString();
    }

    public static void a(Context context, jc jcVar, String str, int i, int i2, String str2) {
        jcVar.a = ia.c(context, str);
        jcVar.d = i;
        jcVar.b = i2;
        jcVar.c = str2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] a(in inVar, String str, boolean z) {
        in.b bVar;
        byte[] bArr = new byte[0];
        InputStream inputStream = null;
        try {
            bVar = inVar.a(str);
            if (bVar == null) {
                if (bVar != null) {
                    try {
                        bVar.close();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
                return bArr;
            }
            try {
                try {
                    InputStream a = bVar.a(0);
                    if (a == null) {
                        if (a != null) {
                            try {
                                a.close();
                            } catch (Throwable th2) {
                                th2.printStackTrace();
                            }
                        }
                        if (bVar != null) {
                            try {
                                bVar.close();
                            } catch (Throwable th3) {
                                th3.printStackTrace();
                            }
                        }
                        return bArr;
                    }
                    try {
                        try {
                            byte[] bArr2 = new byte[a.available()];
                            try {
                                a.read(bArr2);
                                if (z) {
                                    inVar.c(str);
                                }
                                if (a != null) {
                                    try {
                                        a.close();
                                    } catch (Throwable th4) {
                                        th4.printStackTrace();
                                    }
                                }
                                if (bVar != null) {
                                    try {
                                        bVar.close();
                                    } catch (Throwable th5) {
                                        th5.printStackTrace();
                                    }
                                }
                                return bArr2;
                            } catch (Throwable th6) {
                                th = th6;
                                bArr = bArr2;
                                inputStream = a;
                                ic.c(th, "sui", "rdS");
                                if (inputStream != null) {
                                    try {
                                        inputStream.close();
                                    } catch (Throwable th7) {
                                        th7.printStackTrace();
                                    }
                                }
                                if (bVar != null) {
                                    try {
                                        bVar.close();
                                    } catch (Throwable th8) {
                                        th8.printStackTrace();
                                    }
                                }
                                return bArr;
                            }
                        } catch (Throwable th9) {
                            th = th9;
                            inputStream = a;
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (Throwable th10) {
                                    th10.printStackTrace();
                                }
                            }
                            if (bVar == null) {
                                throw th;
                            }
                            try {
                                bVar.close();
                                throw th;
                            } catch (Throwable th11) {
                                th11.printStackTrace();
                                throw th;
                            }
                        }
                    } catch (Throwable th12) {
                        th = th12;
                    }
                } catch (Throwable th13) {
                    th = th13;
                }
            } catch (Throwable th14) {
                th = th14;
            }
        } catch (Throwable th15) {
            th = th15;
            bVar = null;
        }
    }
}
