package com.loc;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import com.amap.location.common.model.AmapLoc;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.loc.ac;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.json.JSONObject;

/* compiled from: Utils.java */
/* loaded from: classes.dex */
public final class ad {
    static String a;
    private static final String[] b = {"arm64-v8a", "x86_64"};
    private static final String[] c = {"arm", "x86"};

    static {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 80; i++) {
            sb.append("=");
        }
        a = sb.toString();
    }

    public static ac a() throws t {
        return new ac.a("collection", "1.0", "AMap_collection_1.0").a(new String[]{"com.amap.api.collection"}).a();
    }

    public static String a(long j) {
        try {
            return new SimpleDateFormat("yyyyMMdd HH:mm:ss:SSS", Locale.CHINA).format(new Date(j));
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public static String a(long j, String str) {
        try {
            return new SimpleDateFormat(str, Locale.CHINA).format(new Date(j));
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public static String a(Context context) {
        String[] strArr;
        String str = "";
        if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 28) {
            try {
                ApplicationInfo applicationInfo = context.getApplicationInfo();
                Field declaredField = Class.forName(ApplicationInfo.class.getName()).getDeclaredField("primaryCpuAbi");
                declaredField.setAccessible(true);
                str = (String) declaredField.get(applicationInfo);
            } catch (Throwable th) {
                an.a(th, "ut", "gct");
            }
        }
        if (Build.VERSION.SDK_INT >= 28) {
            try {
                String[] strArr2 = (String[]) Build.class.getDeclaredField("SUPPORTED_ABIS").get(null);
                if (strArr2 != null && strArr2.length > 0) {
                    str = strArr2[0];
                }
                if (!TextUtils.isEmpty(str) && Arrays.asList(b).contains(str)) {
                    String str2 = context.getApplicationInfo().nativeLibraryDir;
                    if (!TextUtils.isEmpty(str2)) {
                        if (Arrays.asList(c).contains(str2.substring(str2.lastIndexOf(File.separator) + 1)) && (strArr = (String[]) Build.class.getDeclaredField("SUPPORTED_32_BIT_ABIS").get(null)) != null && strArr.length > 0) {
                            str = strArr[0];
                        }
                    }
                }
            } catch (Throwable th2) {
                an.a(th2, "ut", "gct_p");
            }
        }
        return TextUtils.isEmpty(str) ? Build.CPU_ABI : str;
    }

    /* JADX WARN: Removed duplicated region for block: B:46:0x0061 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:53:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0057 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String a(java.lang.Throwable r3) {
        /*
            r0 = 0
            java.io.StringWriter r1 = new java.io.StringWriter     // Catch: java.lang.Throwable -> L35 java.lang.Throwable -> L39
            r1.<init>()     // Catch: java.lang.Throwable -> L35 java.lang.Throwable -> L39
            java.io.PrintWriter r2 = new java.io.PrintWriter     // Catch: java.lang.Throwable -> L2f java.lang.Throwable -> L32
            r2.<init>(r1)     // Catch: java.lang.Throwable -> L2f java.lang.Throwable -> L32
            r3.printStackTrace(r2)     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L54
        Le:
            java.lang.Throwable r3 = r3.getCause()     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L54
            if (r3 == 0) goto L18
            r3.printStackTrace(r2)     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L54
            goto Le
        L18:
            java.lang.String r3 = r1.toString()     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L54
            r1.close()     // Catch: java.lang.Throwable -> L20
            goto L24
        L20:
            r0 = move-exception
            r0.printStackTrace()
        L24:
            r2.close()     // Catch: java.lang.Throwable -> L28
            goto L2c
        L28:
            r0 = move-exception
            r0.printStackTrace()
        L2c:
            return r3
        L2d:
            r3 = move-exception
            goto L3c
        L2f:
            r3 = move-exception
            r2 = r0
            goto L55
        L32:
            r3 = move-exception
            r2 = r0
            goto L3c
        L35:
            r3 = move-exception
            r1 = r0
            r2 = r1
            goto L55
        L39:
            r3 = move-exception
            r1 = r0
            r2 = r1
        L3c:
            r3.printStackTrace()     // Catch: java.lang.Throwable -> L54
            if (r1 == 0) goto L49
            r1.close()     // Catch: java.lang.Throwable -> L45
            goto L49
        L45:
            r3 = move-exception
            r3.printStackTrace()
        L49:
            if (r2 == 0) goto L53
            r2.close()     // Catch: java.lang.Throwable -> L4f
            goto L53
        L4f:
            r3 = move-exception
            r3.printStackTrace()
        L53:
            return r0
        L54:
            r3 = move-exception
        L55:
            if (r1 == 0) goto L5f
            r1.close()     // Catch: java.lang.Throwable -> L5b
            goto L5f
        L5b:
            r0 = move-exception
            r0.printStackTrace()
        L5f:
            if (r2 == 0) goto L69
            r2.close()     // Catch: java.lang.Throwable -> L65
            goto L69
        L65:
            r0 = move-exception
            r0.printStackTrace()
        L69:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ad.a(java.lang.Throwable):java.lang.String");
    }

    public static String a(Map<String, String> map) {
        String value;
        if (map.size() == 0) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        boolean z = true;
        try {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (z) {
                    z = false;
                    stringBuffer.append(entry.getKey());
                    stringBuffer.append("=");
                    value = entry.getValue();
                } else {
                    stringBuffer.append("&");
                    stringBuffer.append(entry.getKey());
                    stringBuffer.append("=");
                    value = entry.getValue();
                }
                stringBuffer.append(value);
            }
        } catch (Throwable th) {
            an.a(th, "ut", "abP");
        }
        return stringBuffer.toString();
    }

    public static String a(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return "";
        }
        try {
            return new String(bArr, "UTF-8");
        } catch (UnsupportedEncodingException unused) {
            return new String(bArr);
        }
    }

    public static Method a(Class cls, String str, Class<?>... clsArr) {
        try {
            return cls.getDeclaredMethod(c(str), clsArr);
        } catch (Throwable unused) {
            return null;
        }
    }

    public static void a(Context context, String str, String str2, JSONObject jSONObject) {
        String str3;
        String str4 = "";
        String e = u.e(context);
        String b2 = aa.b(e);
        String str5 = "";
        String a2 = u.a(context);
        try {
            if (jSONObject.has("info")) {
                str4 = jSONObject.getString("info");
                str5 = "请在高德开放平台官网中搜索\"" + str4 + "\"相关内容进行解决";
            }
        } catch (Throwable unused) {
        }
        if ("INVALID_USER_SCODE".equals(str4)) {
            String string = jSONObject.has("sec_code") ? jSONObject.getString("sec_code") : "";
            String string2 = jSONObject.has("sec_code_debug") ? jSONObject.getString("sec_code_debug") : "";
            if (b2.equals(string) || b2.equals(string2)) {
                str3 = "请在高德开放平台官网中搜索\"请求内容过长导致业务调用失败\"相关内容进行解决";
            }
            str3 = str5;
        } else {
            if ("INVALID_USER_KEY".equals(str4)) {
                String string3 = jSONObject.has("key") ? jSONObject.getString("key") : "";
                if (string3.length() > 0 && !a2.equals(string3)) {
                    str3 = "请在高德开放平台官网上发起技术咨询工单—>账号与Key问题，咨询INVALID_USER_KEY如何解决";
                }
            }
            str3 = str5;
        }
        Log.i("authErrLog", a);
        Log.i("authErrLog", "                                   鉴权错误信息                                  ");
        Log.i("authErrLog", a);
        f("SHA1Package:" + e);
        f("key:" + a2);
        f("csid:" + str);
        f("gsid:" + str2);
        f("json:" + jSONObject.toString());
        Log.i("authErrLog", "                                                                               ");
        Log.i("authErrLog", str3);
        Log.i("authErrLog", a);
    }

    public static void a(ByteArrayOutputStream byteArrayOutputStream, byte b2, byte[] bArr) {
        try {
            byteArrayOutputStream.write(new byte[]{b2});
            int i = b2 & 255;
            if (i < 255 && i > 0) {
                byteArrayOutputStream.write(bArr);
            } else if (i == 255) {
                byteArrayOutputStream.write(bArr, 0, 255);
            }
        } catch (IOException e) {
            an.a(e, "ut", "wFie");
        }
    }

    public static void a(ByteArrayOutputStream byteArrayOutputStream, String str) {
        if (TextUtils.isEmpty(str)) {
            try {
                byteArrayOutputStream.write(new byte[]{0});
                return;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        int length = str.length();
        if (length > 255) {
            length = 255;
        }
        a(byteArrayOutputStream, (byte) length, a(str));
    }

    public static boolean a(Context context, String str) {
        if (context == null || context.checkCallingOrSelfPermission(str) != 0) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= 23 && context.getApplicationInfo().targetSdkVersion >= 23) {
            try {
                if (((Integer) context.getClass().getMethod("checkSelfPermission", String.class).invoke(context, str)).intValue() != 0) {
                    return false;
                }
            } catch (Throwable unused) {
            }
        }
        return true;
    }

    public static boolean a(JSONObject jSONObject, String str) {
        return jSONObject != null && jSONObject.has(str);
    }

    public static byte[] a(String str) {
        if (TextUtils.isEmpty(str)) {
            return new byte[0];
        }
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException unused) {
            return str.getBytes();
        }
    }

    public static ac b() throws t {
        return new ac.a("co", "1.0.0", "AMap_co_1.0.0").a(new String[]{"com.amap.co", "com.amap.opensdk.co", "com.amap.location"}).a();
    }

    public static String b(String str) {
        if (str == null) {
            return null;
        }
        String c2 = y.c(a(str));
        try {
            return ((char) ((c2.length() % 26) + 65)) + c2;
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }

    public static String b(Map<String, String> map) {
        String str;
        if (map != null) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(entry.getValue());
            }
            str = sb.toString();
        } else {
            str = null;
        }
        return d(str);
    }

    public static boolean b(Context context) {
        return at.a(context);
    }

    public static byte[] b(byte[] bArr) {
        try {
            return h(bArr);
        } catch (Throwable th) {
            an.a(th, "ut", "gZp");
            return new byte[0];
        }
    }

    public static String c(String str) {
        return str.length() < 2 ? "" : y.a(str.substring(1));
    }

    public static byte[] c() {
        try {
            String[] split = new StringBuffer("16,16,18,77,15,911,121,77,121,911,38,77,911,99,86,67,611,96,48,77,84,911,38,67,021,301,86,67,611,98,48,77,511,77,48,97,511,58,48,97,511,84,501,87,511,96,48,77,221,911,38,77,121,37,86,67,25,301,86,67,021,96,86,67,021,701,86,67,35,56,86,67,611,37,221,87").reverse().toString().split(",");
            byte[] bArr = new byte[split.length];
            for (int i = 0; i < split.length; i++) {
                bArr[i] = Byte.parseByte(split[i]);
            }
            String[] split2 = new StringBuffer(new String(y.b(new String(bArr)))).reverse().toString().split(",");
            byte[] bArr2 = new byte[split2.length];
            for (int i2 = 0; i2 < split2.length; i2++) {
                bArr2[i2] = Byte.parseByte(split2[i2]);
            }
            return bArr2;
        } catch (Throwable th) {
            an.a(th, "ut", "gIV");
            return new byte[16];
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0, types: [int] */
    /* JADX WARN: Type inference failed for: r1v1 */
    /* JADX WARN: Type inference failed for: r1v10, types: [java.io.OutputStream, java.io.ByteArrayOutputStream] */
    /* JADX WARN: Type inference failed for: r1v12 */
    /* JADX WARN: Type inference failed for: r1v13 */
    /* JADX WARN: Type inference failed for: r1v14 */
    /* JADX WARN: Type inference failed for: r1v2, types: [java.io.ByteArrayOutputStream] */
    /* JADX WARN: Type inference failed for: r1v4 */
    /* JADX WARN: Type inference failed for: r1v5 */
    /* JADX WARN: Type inference failed for: r1v6, types: [java.io.ByteArrayOutputStream] */
    /* JADX WARN: Type inference failed for: r1v7, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v8 */
    /* JADX WARN: Type inference failed for: r1v9 */
    /* JADX WARN: Type inference failed for: r2v10 */
    /* JADX WARN: Type inference failed for: r2v15, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r2v17, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r2v23 */
    /* JADX WARN: Type inference failed for: r2v24 */
    /* JADX WARN: Type inference failed for: r2v25 */
    /* JADX WARN: Type inference failed for: r2v8 */
    /* JADX WARN: Type inference failed for: r2v9, types: [java.lang.String] */
    public static byte[] c(byte[] bArr) {
        ZipOutputStream zipOutputStream;
        ZipOutputStream zipOutputStream2;
        byte[] bArr2;
        ZipOutputStream zipOutputStream3;
        if (bArr != null) {
            ?? length = bArr.length;
            try {
                if (length != 0) {
                    try {
                        length = new ByteArrayOutputStream();
                        try {
                            zipOutputStream2 = new ZipOutputStream(length);
                            try {
                                zipOutputStream2.putNextEntry(new ZipEntry("log"));
                                zipOutputStream2.write(bArr);
                                zipOutputStream2.closeEntry();
                                zipOutputStream2.finish();
                                bArr2 = length.toByteArray();
                                try {
                                    zipOutputStream2.close();
                                    zipOutputStream3 = zipOutputStream2;
                                } catch (Throwable th) {
                                    an.a(th, "ut", "zp1");
                                    zipOutputStream3 = "ut";
                                }
                                try {
                                    length.close();
                                    length = length;
                                    zipOutputStream = zipOutputStream3;
                                } catch (Throwable th2) {
                                    an.a(th2, "ut", "zp2");
                                    length = "ut";
                                    zipOutputStream = "zp2";
                                }
                            } catch (Throwable th3) {
                                th = th3;
                                an.a(th, "ut", "zp");
                                ?? r2 = zipOutputStream2;
                                if (zipOutputStream2 != null) {
                                    try {
                                        zipOutputStream2.close();
                                        r2 = zipOutputStream2;
                                    } catch (Throwable th4) {
                                        an.a(th4, "ut", "zp1");
                                        r2 = "ut";
                                    }
                                }
                                if (length != 0) {
                                    try {
                                        length.close();
                                    } catch (Throwable th5) {
                                        length = "ut";
                                        r2 = "zp2";
                                        an.a(th5, "ut", "zp2");
                                    }
                                }
                                bArr2 = null;
                                length = length;
                                zipOutputStream = r2;
                                return bArr2;
                            }
                        } catch (Throwable th6) {
                            th = th6;
                            zipOutputStream2 = null;
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        length = 0;
                        zipOutputStream = null;
                    }
                    return bArr2;
                }
            } catch (Throwable th8) {
                th = th8;
            }
        }
        return null;
    }

    public static String d(String str) {
        try {
        } catch (Throwable th) {
            an.a(th, "ut", "sPa");
        }
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        String[] split = str.split("&");
        Arrays.sort(split);
        StringBuffer stringBuffer = new StringBuffer();
        for (String str2 : split) {
            stringBuffer.append(str2);
            stringBuffer.append("&");
        }
        String stringBuffer2 = stringBuffer.toString();
        if (stringBuffer2.length() > 1) {
            return (String) stringBuffer2.subSequence(0, stringBuffer2.length() - 1);
        }
        return str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:37:0x005c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.security.PublicKey d() throws java.security.cert.CertificateException, java.security.spec.InvalidKeySpecException, java.security.NoSuchAlgorithmException, java.lang.NullPointerException, java.io.IOException {
        /*
            java.lang.String r0 = "MIICnjCCAgegAwIBAgIJAJ0Pdzos7ZfYMA0GCSqGSIb3DQEBBQUAMGgxCzAJBgNVBAYTAkNOMRMwEQYDVQQIDApTb21lLVN0YXRlMRAwDgYDVQQHDAdCZWlqaW5nMREwDwYDVQQKDAhBdXRvbmF2aTEfMB0GA1UEAwwWY29tLmF1dG9uYXZpLmFwaXNlcnZlcjAeFw0xMzA4MTUwNzU2NTVaFw0yMzA4MTMwNzU2NTVaMGgxCzAJBgNVBAYTAkNOMRMwEQYDVQQIDApTb21lLVN0YXRlMRAwDgYDVQQHDAdCZWlqaW5nMREwDwYDVQQKDAhBdXRvbmF2aTEfMB0GA1UEAwwWY29tLmF1dG9uYXZpLmFwaXNlcnZlcjCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEA8eWAyHbFPoFPfdx5AD+D4nYFq4dbJ1p7SIKt19Oz1oivF/6H43v5Fo7s50pD1UF8+Qu4JoUQxlAgOt8OCyQ8DYdkaeB74XKb1wxkIYg/foUwN1CMHPZ9O9ehgna6K4EJXZxR7Y7XVZnbjHZIVn3VpPU/Rdr2v37LjTw+qrABJxMCAwEAAaNQME4wHQYDVR0OBBYEFOM/MLGP8xpVFuVd+3qZkw7uBvOTMB8GA1UdIwQYMBaAFOM/MLGP8xpVFuVd+3qZkw7uBvOTMAwGA1UdEwQFMAMBAf8wDQYJKoZIhvcNAQEFBQADgYEA4LY3g8aAD8JkxAOqUXDDyLuCCGOc2pTIhn0TwMNaVdH4hZlpTeC/wuRD5LJ0z3j+IQ0vLvuQA5uDjVyEOlBrvVIGwSem/1XGUo13DfzgAJ5k1161S5l+sFUo5TxpHOXr8Z5nqJMjieXmhnE/I99GFyHpQmw4cC6rhYUhdhtg+Zk="
            r1 = 0
            java.io.ByteArrayInputStream r2 = new java.io.ByteArrayInputStream     // Catch: java.lang.Throwable -> L46 java.lang.Throwable -> L49
            byte[] r0 = com.loc.y.b(r0)     // Catch: java.lang.Throwable -> L46 java.lang.Throwable -> L49
            r2.<init>(r0)     // Catch: java.lang.Throwable -> L46 java.lang.Throwable -> L49
            java.lang.String r0 = "X.509"
            java.security.cert.CertificateFactory r0 = java.security.cert.CertificateFactory.getInstance(r0)     // Catch: java.lang.Throwable -> L44 java.lang.Throwable -> L59
            java.lang.String r3 = "RSA"
            java.security.KeyFactory r3 = java.security.KeyFactory.getInstance(r3)     // Catch: java.lang.Throwable -> L44 java.lang.Throwable -> L59
            java.security.cert.Certificate r0 = r0.generateCertificate(r2)     // Catch: java.lang.Throwable -> L44 java.lang.Throwable -> L59
            if (r0 == 0) goto L3b
            if (r3 != 0) goto L21
            goto L3b
        L21:
            java.security.spec.X509EncodedKeySpec r4 = new java.security.spec.X509EncodedKeySpec     // Catch: java.lang.Throwable -> L44 java.lang.Throwable -> L59
            java.security.PublicKey r0 = r0.getPublicKey()     // Catch: java.lang.Throwable -> L44 java.lang.Throwable -> L59
            byte[] r0 = r0.getEncoded()     // Catch: java.lang.Throwable -> L44 java.lang.Throwable -> L59
            r4.<init>(r0)     // Catch: java.lang.Throwable -> L44 java.lang.Throwable -> L59
            java.security.PublicKey r0 = r3.generatePublic(r4)     // Catch: java.lang.Throwable -> L44 java.lang.Throwable -> L59
            r2.close()     // Catch: java.lang.Throwable -> L36
            goto L3a
        L36:
            r1 = move-exception
            r1.printStackTrace()
        L3a:
            return r0
        L3b:
            r2.close()     // Catch: java.lang.Throwable -> L3f
            goto L43
        L3f:
            r0 = move-exception
            r0.printStackTrace()
        L43:
            return r1
        L44:
            r0 = move-exception
            goto L4b
        L46:
            r0 = move-exception
            r2 = r1
            goto L5a
        L49:
            r0 = move-exception
            r2 = r1
        L4b:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L59
            if (r2 == 0) goto L58
            r2.close()     // Catch: java.lang.Throwable -> L54
            goto L58
        L54:
            r0 = move-exception
            r0.printStackTrace()
        L58:
            return r1
        L59:
            r0 = move-exception
        L5a:
            if (r2 == 0) goto L64
            r2.close()     // Catch: java.lang.Throwable -> L60
            goto L64
        L60:
            r1 = move-exception
            r1.printStackTrace()
        L64:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ad.d():java.security.PublicKey");
    }

    public static byte[] d(byte[] bArr) {
        try {
            return h(bArr);
        } catch (Throwable th) {
            th.printStackTrace();
            return new byte[0];
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String e(byte[] bArr) {
        try {
            return g(bArr);
        } catch (Throwable th) {
            an.a(th, "ut", "h2s");
            return null;
        }
    }

    public static byte[] e(String str) {
        if (str.length() % 2 != 0) {
            str = AmapLoc.RESULT_TYPE_GPS + str;
        }
        byte[] bArr = new byte[str.length() / 2];
        for (int i = 0; i < bArr.length; i++) {
            int i2 = i * 2;
            bArr[i] = (byte) Integer.parseInt(str.substring(i2, i2 + 2), 16);
        }
        return bArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String f(byte[] bArr) {
        try {
            return g(bArr);
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    private static void f(String str) {
        int i;
        while (true) {
            if (str.length() < 78) {
                break;
            }
            Log.i("authErrLog", "|" + str.substring(0, 78) + "|");
            str = str.substring(78);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("|");
        sb.append(str);
        for (i = 0; i < 78 - str.length(); i++) {
            sb.append(SQLBuilder.BLANK);
        }
        sb.append("|");
        Log.i("authErrLog", sb.toString());
    }

    public static String g(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        if (bArr == null) {
            return null;
        }
        for (byte b2 : bArr) {
            String hexString = Integer.toHexString(b2 & 255);
            if (hexString.length() == 1) {
                hexString = AmapLoc.RESULT_TYPE_GPS + hexString;
            }
            sb.append(hexString);
        }
        return sb.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0040 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0038 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static byte[] h(byte[] r3) throws java.io.IOException, java.lang.Throwable {
        /*
            r0 = 0
            if (r3 != 0) goto L4
            return r0
        L4:
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L30
            r1.<init>()     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L30
            java.util.zip.GZIPOutputStream r2 = new java.util.zip.GZIPOutputStream     // Catch: java.lang.Throwable -> L27 java.lang.Throwable -> L29
            r2.<init>(r1)     // Catch: java.lang.Throwable -> L27 java.lang.Throwable -> L29
            r2.write(r3)     // Catch: java.lang.Throwable -> L23 java.lang.Throwable -> L25
            r2.finish()     // Catch: java.lang.Throwable -> L23 java.lang.Throwable -> L25
            byte[] r3 = r1.toByteArray()     // Catch: java.lang.Throwable -> L23 java.lang.Throwable -> L25
            r2.close()     // Catch: java.lang.Throwable -> L21
            r1.close()     // Catch: java.lang.Throwable -> L1f
            return r3
        L1f:
            r3 = move-exception
            throw r3
        L21:
            r3 = move-exception
            throw r3
        L23:
            r3 = move-exception
            goto L35
        L25:
            r3 = move-exception
            goto L2b
        L27:
            r3 = move-exception
            goto L36
        L29:
            r3 = move-exception
            r2 = r0
        L2b:
            r0 = r1
            goto L32
        L2d:
            r3 = move-exception
            r1 = r0
            goto L36
        L30:
            r3 = move-exception
            r2 = r0
        L32:
            throw r3     // Catch: java.lang.Throwable -> L33
        L33:
            r3 = move-exception
            r1 = r0
        L35:
            r0 = r2
        L36:
            if (r0 == 0) goto L3e
            r0.close()     // Catch: java.lang.Throwable -> L3c
            goto L3e
        L3c:
            r3 = move-exception
            throw r3
        L3e:
            if (r1 == 0) goto L46
            r1.close()     // Catch: java.lang.Throwable -> L44
            goto L46
        L44:
            r3 = move-exception
            throw r3
        L46:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ad.h(byte[]):byte[]");
    }
}
