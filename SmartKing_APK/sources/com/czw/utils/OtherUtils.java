package com.czw.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/* loaded from: classes.dex */
public class OtherUtils {
    private static final int STRING_BUFFER_LENGTH = 100;
    private static SSLSocketFactory sslSocketFactory;

    private OtherUtils() {
    }

    public static long getAvailableSpace(File file) {
        try {
            StatFs statFs = new StatFs(file.getPath());
            return statFs.getBlockSize() * statFs.getAvailableBlocks();
        } catch (Throwable th) {
            LogUtil.e(th.getMessage(), th);
            return -1L;
        }
    }

    public static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

    public static StackTraceElement getCurrentStackTraceElement() {
        return Thread.currentThread().getStackTrace()[3];
    }

    public static String getDiskCacheDir(Context context, String str) {
        File cacheDir;
        File externalCacheDir;
        String path = (!"mounted".equals(Environment.getExternalStorageState()) || (externalCacheDir = context.getExternalCacheDir()) == null) ? null : externalCacheDir.getPath();
        if (path == null && (cacheDir = context.getCacheDir()) != null && cacheDir.exists()) {
            path = cacheDir.getPath();
        }
        return path + File.separator + str;
    }

    public static String getSubString(String str, int i, int i2) {
        return new String(str.substring(i, i2));
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x004c  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0075  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x008d  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0066  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x003c  */
    /* JADX WARN: Removed duplicated region for block: B:6:0x0025  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0038  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String getUserAgent(android.content.Context r4) {
        /*
            r0 = 0
            if (r4 == 0) goto L1e
            java.lang.String r1 = "com.android.internal.R$string"
            java.lang.Class r1 = java.lang.Class.forName(r1)     // Catch: java.lang.Throwable -> L1e
            java.lang.String r2 = "web_user_agent"
            java.lang.reflect.Field r1 = r1.getDeclaredField(r2)     // Catch: java.lang.Throwable -> L1e
            java.lang.Object r1 = r1.get(r0)     // Catch: java.lang.Throwable -> L1e
            java.lang.Integer r1 = (java.lang.Integer) r1     // Catch: java.lang.Throwable -> L1e
            int r1 = r1.intValue()     // Catch: java.lang.Throwable -> L1e
            java.lang.String r4 = r4.getString(r1)     // Catch: java.lang.Throwable -> L1e
            goto L1f
        L1e:
            r4 = r0
        L1f:
            boolean r0 = android.text.TextUtils.isEmpty(r4)
            if (r0 == 0) goto L27
            java.lang.String r4 = "Mozilla/5.0 (Linux; U; Android %s) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 %sSafari/533.1"
        L27:
            java.util.Locale r0 = java.util.Locale.getDefault()
            java.lang.StringBuffer r1 = new java.lang.StringBuffer
            r1.<init>()
            java.lang.String r2 = android.os.Build.VERSION.RELEASE
            int r3 = r2.length()
            if (r3 <= 0) goto L3c
            r1.append(r2)
            goto L41
        L3c:
            java.lang.String r2 = "1.0"
            r1.append(r2)
        L41:
            java.lang.String r2 = "; "
            r1.append(r2)
            java.lang.String r2 = r0.getLanguage()
            if (r2 == 0) goto L66
            java.lang.String r2 = r2.toLowerCase()
            r1.append(r2)
            java.lang.String r0 = r0.getCountry()
            if (r0 == 0) goto L6b
            java.lang.String r2 = "-"
            r1.append(r2)
            java.lang.String r0 = r0.toLowerCase()
            r1.append(r0)
            goto L6b
        L66:
            java.lang.String r0 = "en"
            r1.append(r0)
        L6b:
            java.lang.String r0 = "REL"
            java.lang.String r2 = android.os.Build.VERSION.CODENAME
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L85
            java.lang.String r0 = android.os.Build.MODEL
            int r2 = r0.length()
            if (r2 <= 0) goto L85
            java.lang.String r2 = "; "
            r1.append(r2)
            r1.append(r0)
        L85:
            java.lang.String r0 = android.os.Build.ID
            int r2 = r0.length()
            if (r2 <= 0) goto L95
            java.lang.String r2 = " Build/"
            r1.append(r2)
            r1.append(r0)
        L95:
            r0 = 2
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r2 = 0
            r0[r2] = r1
            r1 = 1
            java.lang.String r2 = "Mobile "
            r0[r1] = r2
            java.lang.String r4 = java.lang.String.format(r4, r0)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.czw.utils.OtherUtils.getUserAgent(android.content.Context):java.lang.String");
    }

    public static long sizeOfString(String str, String str2) throws UnsupportedEncodingException {
        long j = 0;
        if (TextUtils.isEmpty(str)) {
            return 0L;
        }
        int length = str.length();
        if (length < 100) {
            return str.getBytes(str2).length;
        }
        int i = 0;
        while (i < length) {
            int i2 = i + 100;
            j += getSubString(str, i, i2 < length ? i2 : length).getBytes(str2).length;
            i = i2;
        }
        return j;
    }

    public static void trustAllHttpsURLConnection() {
        if (sslSocketFactory == null) {
            TrustManager[] trustManagerArr = {new X509TrustManager() { // from class: com.czw.utils.OtherUtils.1
                @Override // javax.net.ssl.X509TrustManager
                public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) {
                }

                @Override // javax.net.ssl.X509TrustManager
                public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) {
                }

                @Override // javax.net.ssl.X509TrustManager
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }};
            try {
                SSLContext sSLContext = SSLContext.getInstance("TLS");
                sSLContext.init(null, trustManagerArr, null);
                sslSocketFactory = sSLContext.getSocketFactory();
            } catch (Throwable th) {
                LogUtil.e(th.getMessage(), th);
            }
        }
        if (sslSocketFactory != null) {
            HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
            HttpsURLConnection.setDefaultHostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        }
    }
}
