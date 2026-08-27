package com.tencent.bugly.proguard;

import android.content.Context;
import com.tencent.bugly.BuglyStrategy;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/* compiled from: BUGLY */
/* loaded from: classes2.dex */
public final class s {
    private static s b;
    public Map<String, String> a = null;
    private Context c;

    private s(Context context) {
        this.c = context;
    }

    public static s a(Context context) {
        if (b == null) {
            b = new s(context);
        }
        return b;
    }

    private static HttpURLConnection a(String str, String str2) {
        HttpURLConnection httpURLConnection;
        try {
            URL url = new URL(str2);
            if (a.b() != null) {
                httpURLConnection = (HttpURLConnection) url.openConnection(a.b());
            } else if (str == null || !str.toLowerCase(Locale.US).contains("wap")) {
                httpURLConnection = (HttpURLConnection) url.openConnection();
            } else {
                httpURLConnection = (HttpURLConnection) url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(System.getProperty("http.proxyHost"), Integer.parseInt(System.getProperty("http.proxyPort")))));
            }
            httpURLConnection.setConnectTimeout(BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setInstanceFollowRedirects(false);
            return httpURLConnection;
        } catch (Throwable th) {
            if (x.a(th)) {
                return null;
            }
            th.printStackTrace();
            return null;
        }
    }

    private HttpURLConnection a(String str, byte[] bArr, String str2, Map<String, String> map) {
        if (str == null) {
            x.e("destUrl is null.", new Object[0]);
            return null;
        }
        TrustManager[] trustManagerArr = {new X509TrustManager() { // from class: com.tencent.bugly.proguard.s.1
            @Override // javax.net.ssl.X509TrustManager
            public final void checkClientTrusted(X509Certificate[] x509CertificateArr, String str3) throws CertificateException {
                x.c("checkClientTrusted", new Object[0]);
            }

            @Override // javax.net.ssl.X509TrustManager
            public final void checkServerTrusted(X509Certificate[] x509CertificateArr, String str3) throws CertificateException {
                x.c("checkServerTrusted", new Object[0]);
            }

            @Override // javax.net.ssl.X509TrustManager
            public final X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};
        try {
            SSLContext sSLContext = SSLContext.getInstance("TLS");
            sSLContext.init(null, trustManagerArr, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sSLContext.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpURLConnection a = a(str2, str);
        if (a == null) {
            x.e("Failed to get HttpURLConnection object.", new Object[0]);
            return null;
        }
        try {
            a.setRequestProperty("wup_version", "3.0");
            if (map != null && map.size() > 0) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    a.setRequestProperty(entry.getKey(), URLEncoder.encode(entry.getValue(), "utf-8"));
                }
            }
            a.setRequestProperty("A37", URLEncoder.encode(str2, "utf-8"));
            a.setRequestProperty("A38", URLEncoder.encode(str2, "utf-8"));
            OutputStream outputStream = a.getOutputStream();
            if (bArr == null) {
                outputStream.write(0);
            } else {
                outputStream.write(bArr);
            }
            return a;
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
            x.e("Failed to upload, please check your network.", new Object[0]);
            return null;
        }
    }

    private static Map<String, String> a(HttpURLConnection httpURLConnection) {
        HashMap hashMap = new HashMap();
        Map<String, List<String>> headerFields = httpURLConnection.getHeaderFields();
        if (headerFields == null || headerFields.size() == 0) {
            return null;
        }
        for (String str : headerFields.keySet()) {
            List<String> list = headerFields.get(str);
            if (list.size() > 0) {
                hashMap.put(str, list.get(0));
            }
        }
        return hashMap;
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x004f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static byte[] b(java.net.HttpURLConnection r5) {
        /*
            r0 = 0
            if (r5 != 0) goto L4
            return r0
        L4:
            java.io.BufferedInputStream r1 = new java.io.BufferedInputStream     // Catch: java.lang.Throwable -> L33 java.lang.Throwable -> L36
            java.io.InputStream r5 = r5.getInputStream()     // Catch: java.lang.Throwable -> L33 java.lang.Throwable -> L36
            r1.<init>(r5)     // Catch: java.lang.Throwable -> L33 java.lang.Throwable -> L36
            java.io.ByteArrayOutputStream r5 = new java.io.ByteArrayOutputStream     // Catch: java.lang.Throwable -> L31 java.lang.Throwable -> L4c
            r5.<init>()     // Catch: java.lang.Throwable -> L31 java.lang.Throwable -> L4c
            r2 = 1024(0x400, float:1.435E-42)
            byte[] r2 = new byte[r2]     // Catch: java.lang.Throwable -> L31 java.lang.Throwable -> L4c
        L16:
            int r3 = r1.read(r2)     // Catch: java.lang.Throwable -> L31 java.lang.Throwable -> L4c
            if (r3 <= 0) goto L21
            r4 = 0
            r5.write(r2, r4, r3)     // Catch: java.lang.Throwable -> L31 java.lang.Throwable -> L4c
            goto L16
        L21:
            r5.flush()     // Catch: java.lang.Throwable -> L31 java.lang.Throwable -> L4c
            byte[] r5 = r5.toByteArray()     // Catch: java.lang.Throwable -> L31 java.lang.Throwable -> L4c
            r1.close()     // Catch: java.lang.Throwable -> L2c
            goto L30
        L2c:
            r0 = move-exception
            r0.printStackTrace()
        L30:
            return r5
        L31:
            r5 = move-exception
            goto L38
        L33:
            r5 = move-exception
            r1 = r0
            goto L4d
        L36:
            r5 = move-exception
            r1 = r0
        L38:
            boolean r2 = com.tencent.bugly.proguard.x.a(r5)     // Catch: java.lang.Throwable -> L4c
            if (r2 != 0) goto L41
            r5.printStackTrace()     // Catch: java.lang.Throwable -> L4c
        L41:
            if (r1 == 0) goto L4b
            r1.close()     // Catch: java.lang.Throwable -> L47
            goto L4b
        L47:
            r5 = move-exception
            r5.printStackTrace()
        L4b:
            return r0
        L4c:
            r5 = move-exception
        L4d:
            if (r1 == 0) goto L57
            r1.close()     // Catch: java.lang.Throwable -> L53
            goto L57
        L53:
            r0 = move-exception
            r0.printStackTrace()
        L57:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.s.b(java.net.HttpURLConnection):byte[]");
    }

    /* JADX WARN: Can't wrap try/catch for region: R(11:40|(1:113)(1:48)|(10:79|80|(5:96|97|99|100|101)(7:82|83|84|85|86|87|88)|92|68|(1:70)|71|72|73|59)(1:50)|51|52|(1:54)|55|56|57|58|59) */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x015a, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0160, code lost:
    
        if (com.tencent.bugly.proguard.x.a(r0) == false) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0162, code lost:
    
        r0.printStackTrace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0167, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0168, code lost:
    
        r6 = r5;
     */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0174 A[Catch: all -> 0x016a, TRY_LEAVE, TryCatch #9 {all -> 0x016a, blocks: (B:24:0x009a, B:26:0x00a2, B:29:0x00b3, B:39:0x00b1, B:80:0x00dc, B:97:0x00e4, B:84:0x010e, B:87:0x011b, B:52:0x0132, B:55:0x0153, B:68:0x016e, B:70:0x0174), top: B:23:0x009a }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final byte[] a(java.lang.String r19, byte[] r20, com.tencent.bugly.proguard.v r21, java.util.Map<java.lang.String, java.lang.String> r22) {
        /*
            Method dump skipped, instructions count: 431
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.s.a(java.lang.String, byte[], com.tencent.bugly.proguard.v, java.util.Map):byte[]");
    }
}
