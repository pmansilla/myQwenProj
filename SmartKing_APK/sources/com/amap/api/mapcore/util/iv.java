package com.amap.api.mapcore.util;

import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import com.amap.api.mapcore.util.is;
import com.amap.api.mapcore.util.iu;
import com.amap.api.maps.AMapException;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

/* compiled from: HttpUrlUtil.java */
/* loaded from: classes.dex */
public class iv {
    private int a;
    private int b;
    private boolean c;
    private SSLContext d;
    private Proxy e;
    private volatile boolean f;
    private long g;
    private long h;
    private String i;
    private a j;
    private is.a k;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: HttpUrlUtil.java */
    /* loaded from: classes.dex */
    public static class a {
        private Vector<b> a;
        private volatile b b;

        private a() {
            this.a = new Vector<>();
            this.b = new b();
        }

        public b a() {
            return this.b;
        }

        public b a(String str) {
            if (TextUtils.isEmpty(str)) {
                return this.b;
            }
            for (int i = 0; i < this.a.size(); i++) {
                b bVar = this.a.get(i);
                if (bVar != null && bVar.a().equals(str)) {
                    return bVar;
                }
            }
            b bVar2 = new b();
            bVar2.b(str);
            this.a.add(bVar2);
            return bVar2;
        }

        public void b(String str) {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            this.b.a(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: HttpUrlUtil.java */
    /* loaded from: classes.dex */
    public static class b implements HostnameVerifier {
        private String a;
        private String b;

        private b() {
        }

        public String a() {
            return this.b;
        }

        public void a(String str) {
            this.a = str;
        }

        public void b(String str) {
            this.b = str;
        }

        @Override // javax.net.ssl.HostnameVerifier
        public boolean verify(String str, SSLSession sSLSession) {
            HostnameVerifier defaultHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
            return !TextUtils.isEmpty(this.a) ? this.a.equals(str) : !TextUtils.isEmpty(this.b) ? defaultHostnameVerifier.verify(this.b, sSLSession) : defaultHostnameVerifier.verify(str, sSLSession);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public iv(int i, int i2, Proxy proxy, boolean z) {
        this(i, i2, proxy, z, null);
    }

    iv(int i, int i2, Proxy proxy, boolean z, is.a aVar) {
        this.f = false;
        this.g = -1L;
        this.h = 0L;
        this.a = i;
        this.b = i2;
        this.e = proxy;
        this.c = hk.a().b(z);
        if (hk.c()) {
            this.c = false;
        }
        this.k = aVar;
        b();
        if (this.c) {
            try {
                SSLContext sSLContext = SSLContext.getInstance("TLS");
                sSLContext.init(null, null, null);
                this.d = sSLContext;
            } catch (Throwable th) {
                hz.a(th, "ht", "ne");
            }
        }
        this.j = new a();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0146 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:75:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0138 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x012a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x011c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r1v10 */
    /* JADX WARN: Type inference failed for: r1v11 */
    /* JADX WARN: Type inference failed for: r1v12 */
    /* JADX WARN: Type inference failed for: r1v13, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r1v15 */
    /* JADX WARN: Type inference failed for: r1v17 */
    /* JADX WARN: Type inference failed for: r1v18 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private com.amap.api.mapcore.util.iz a(java.net.HttpURLConnection r11, boolean r12) throws com.amap.api.mapcore.util.hc, java.io.IOException {
        /*
            Method dump skipped, instructions count: 339
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.iv.a(java.net.HttpURLConnection, boolean):com.amap.api.mapcore.util.iz");
    }

    private String a(int i, String str, Map<String, String> map) {
        String str2 = i == 1 ? is.b : "";
        if (TextUtils.isEmpty(str2)) {
            return str;
        }
        Uri parse = Uri.parse(str);
        String host = parse.getHost();
        String uri = parse.buildUpon().encodedAuthority(str2).build().toString();
        if (map != null) {
            map.put("targetHost", host);
        }
        if (this.c) {
            this.j.b(str2);
        }
        return uri;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String a(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (value == null) {
                value = "";
            }
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(URLEncoder.encode(key));
            sb.append("=");
            sb.append(URLEncoder.encode(value));
        }
        return sb.toString();
    }

    private void a(Map<String, String> map, HttpURLConnection httpURLConnection) {
        if (map != null) {
            for (String str : map.keySet()) {
                httpURLConnection.addRequestProperty(str, map.get(str));
            }
        }
        try {
            httpURLConnection.addRequestProperty("csid", this.i);
        } catch (Throwable th) {
            hz.a(th, "ht", "adh");
        }
        httpURLConnection.setConnectTimeout(this.a);
        httpURLConnection.setReadTimeout(this.b);
    }

    private void b() {
        try {
            this.i = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        } catch (Throwable th) {
            hz.a(th, "ht", "ic");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public iz a(String str, boolean z, String str2, Map<String, String> map, byte[] bArr, boolean z2) throws hc {
        HttpURLConnection httpURLConnection = null;
        try {
            try {
                HttpURLConnection a2 = a(str, z, str2, map, true);
                if (bArr != null) {
                    try {
                        if (bArr.length > 0) {
                            DataOutputStream dataOutputStream = new DataOutputStream(a2.getOutputStream());
                            dataOutputStream.write(bArr);
                            dataOutputStream.close();
                        }
                    } catch (hc e) {
                        e = e;
                        hz.a(e, "ht", "mPt");
                        throw e;
                    } catch (ConnectException e2) {
                        e = e2;
                        e.printStackTrace();
                        throw new hc(AMapException.ERROR_CONNECTION);
                    } catch (MalformedURLException e3) {
                        e = e3;
                        e.printStackTrace();
                        throw new hc(AMapException.ERROR_URL);
                    } catch (SocketException e4) {
                        e = e4;
                        e.printStackTrace();
                        throw new hc(AMapException.ERROR_SOCKET);
                    } catch (SocketTimeoutException e5) {
                        e = e5;
                        e.printStackTrace();
                        throw new hc(AMapException.ERROR_SOCKE_TIME_OUT);
                    } catch (InterruptedIOException unused) {
                        throw new hc(AMapException.ERROR_UNKNOWN);
                    } catch (UnknownHostException e6) {
                        e = e6;
                        e.printStackTrace();
                        throw new hc(AMapException.ERROR_UNKNOW_HOST);
                    } catch (IOException e7) {
                        e = e7;
                        e.printStackTrace();
                        throw new hc(AMapException.ERROR_IO);
                    } catch (Throwable th) {
                        th = th;
                        hz.a(th, "ht", "mPt");
                        throw new hc(AMapException.ERROR_UNKNOWN);
                    }
                }
                iz a3 = a(a2, z2);
                if (a2 != null) {
                    try {
                        a2.disconnect();
                    } catch (Throwable th2) {
                        hz.a(th2, "ht", "mPt");
                    }
                }
                return a3;
            } catch (Throwable th3) {
                th = th3;
            }
        } catch (hc e8) {
            e = e8;
        } catch (InterruptedIOException unused2) {
        } catch (ConnectException e9) {
            e = e9;
        } catch (MalformedURLException e10) {
            e = e10;
        } catch (SocketException e11) {
            e = e11;
        } catch (SocketTimeoutException e12) {
            e = e12;
        } catch (UnknownHostException e13) {
            e = e13;
        } catch (IOException e14) {
            e = e14;
        } catch (Throwable th4) {
            th = th4;
        }
    }

    HttpURLConnection a(String str, boolean z, String str2, Map<String, String> map, boolean z2) throws IOException {
        HttpURLConnection httpURLConnection;
        hi.b();
        if (map == null) {
            map = new HashMap<>();
        }
        b a2 = this.j.a();
        if (z && !TextUtils.isEmpty(str2)) {
            a2 = this.j.a(str2);
        }
        String a3 = a(is.a, str, map);
        if (this.c) {
            a3 = hk.a(a3);
        }
        URL url = new URL(a3);
        URLConnection a4 = this.k != null ? this.k.a(this.e, url) : null;
        if (a4 == null) {
            a4 = this.e != null ? url.openConnection(this.e) : url.openConnection();
        }
        if (this.c) {
            httpURLConnection = (HttpsURLConnection) a4;
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) httpURLConnection;
            httpsURLConnection.setSSLSocketFactory(this.d.getSocketFactory());
            httpsURLConnection.setHostnameVerifier(a2);
        } else {
            httpURLConnection = (HttpURLConnection) a4;
        }
        if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
            httpURLConnection.setRequestProperty("Connection", "close");
        }
        a(map, httpURLConnection);
        if (z2) {
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
        } else {
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
        }
        return httpURLConnection;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Map<String, String> a(String str, boolean z, String str2, Map<String, String> map, Map<String, String> map2, boolean z2) throws hc {
        Throwable th;
        HttpURLConnection a2;
        String headerFieldKey;
        HttpURLConnection httpURLConnection = null;
        try {
            try {
                String a3 = a(map2);
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(str);
                if (a3 != null) {
                    stringBuffer.append("?");
                    stringBuffer.append(a3);
                }
                a2 = a(stringBuffer.toString(), z, str2, map, false);
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (hc e) {
            throw e;
        } catch (ConnectException unused) {
        } catch (MalformedURLException unused2) {
        } catch (SocketTimeoutException unused3) {
        } catch (InterruptedIOException unused4) {
        } catch (SocketException unused5) {
        } catch (UnknownHostException unused6) {
        } catch (IOException unused7) {
        } catch (Throwable th3) {
            th = th3;
        }
        try {
            if (a2.getResponseCode() >= 400) {
                throw new hc("http读取header失败");
            }
            HashMap hashMap = new HashMap();
            for (int i = 0; i < 50 && (headerFieldKey = a2.getHeaderFieldKey(i)) != null; i++) {
                hashMap.put(headerFieldKey.toLowerCase(), a2.getHeaderField(headerFieldKey));
            }
            if (a2 != null) {
                try {
                    a2.disconnect();
                } catch (Throwable th4) {
                    hz.a(th4, "hth", "mgr");
                }
            }
            return hashMap;
        } catch (hc e2) {
            throw e2;
        } catch (InterruptedIOException unused8) {
            throw new hc(AMapException.ERROR_UNKNOWN);
        } catch (ConnectException unused9) {
            throw new hc(AMapException.ERROR_CONNECTION);
        } catch (MalformedURLException unused10) {
            throw new hc(AMapException.ERROR_URL);
        } catch (SocketException unused11) {
            throw new hc(AMapException.ERROR_SOCKET);
        } catch (SocketTimeoutException unused12) {
            throw new hc(AMapException.ERROR_SOCKE_TIME_OUT);
        } catch (UnknownHostException unused13) {
            throw new hc(AMapException.ERROR_UNKNOW_HOST);
        } catch (IOException unused14) {
            throw new hc(AMapException.ERROR_IO);
        } catch (Throwable th5) {
            httpURLConnection = a2;
            th = th5;
            if (httpURLConnection != null) {
                try {
                    httpURLConnection.disconnect();
                } catch (Throwable th6) {
                    hz.a(th6, "hth", "mgr");
                }
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        this.f = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(long j) {
        this.h = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(String str, boolean z, String str2, Map<String, String> map, Map<String, String> map2, byte[] bArr, iu.a aVar) {
        HttpURLConnection httpURLConnection;
        InputStream inputStream;
        int read;
        if (aVar == null) {
            return;
        }
        InputStream inputStream2 = null;
        try {
            try {
                String a2 = a(map2);
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(str);
                if (a2 != null) {
                    stringBuffer.append("?");
                    stringBuffer.append(a2);
                }
                boolean z2 = true;
                boolean z3 = bArr != null && bArr.length > 0;
                httpURLConnection = a(stringBuffer.toString(), z, str2, map, z3);
                try {
                    try {
                        httpURLConnection.setRequestProperty("RANGE", "bytes=" + this.h + "-");
                        if (z3) {
                            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                            dataOutputStream.write(bArr);
                            dataOutputStream.close();
                        }
                        httpURLConnection.connect();
                        int responseCode = httpURLConnection.getResponseCode();
                        boolean z4 = responseCode != 200;
                        if (responseCode == 206) {
                            z2 = false;
                        }
                        if (z2 & z4) {
                            aVar.onException(new hc("网络异常原因：" + httpURLConnection.getResponseMessage() + " 网络异常状态码：" + responseCode));
                        }
                        inputStream = httpURLConnection.getInputStream();
                    } catch (Throwable th) {
                        th = th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
                try {
                    byte[] bArr2 = new byte[1024];
                    while (!Thread.interrupted() && !this.f && (read = inputStream.read(bArr2, 0, 1024)) > 0 && (this.g == -1 || this.h < this.g)) {
                        if (read == 1024) {
                            aVar.onDownload(bArr2, this.h);
                        } else {
                            byte[] bArr3 = new byte[read];
                            System.arraycopy(bArr2, 0, bArr3, 0, read);
                            aVar.onDownload(bArr3, this.h);
                        }
                        this.h += read;
                    }
                    if (this.f) {
                        aVar.onStop();
                    } else {
                        aVar.onFinish();
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            hz.a(e, "ht", "mdr");
                        } catch (Throwable th3) {
                            hz.a(th3, "ht", "mdr");
                        }
                    }
                } catch (Throwable th4) {
                    inputStream2 = inputStream;
                    th = th4;
                    aVar.onException(th);
                    if (inputStream2 != null) {
                        try {
                            inputStream2.close();
                        } catch (IOException e2) {
                            hz.a(e2, "ht", "mdr");
                        } catch (Throwable th5) {
                            hz.a(th5, "ht", "mdr");
                        }
                    }
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    return;
                }
            } catch (Throwable th6) {
                hz.a(th6, "ht", "mdr");
                return;
            }
        } catch (Throwable th7) {
            th = th7;
            httpURLConnection = null;
        }
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public iz b(String str, boolean z, String str2, Map<String, String> map, Map<String, String> map2, boolean z2) throws hc {
        HttpURLConnection a2;
        HttpURLConnection httpURLConnection = null;
        try {
            try {
                String a3 = a(map2);
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(str);
                if (a3 != null) {
                    stringBuffer.append("?");
                    stringBuffer.append(a3);
                }
                a2 = a(stringBuffer.toString(), z, str2, map, false);
            } catch (Throwable th) {
                th = th;
            }
            try {
                iz a4 = a(a2, z2);
                if (a2 != null) {
                    try {
                        a2.disconnect();
                    } catch (Throwable th2) {
                        hz.a(th2, "ht", "mgr");
                    }
                }
                return a4;
            } catch (hc e) {
                throw e;
            } catch (ConnectException unused) {
                throw new hc(AMapException.ERROR_CONNECTION);
            } catch (MalformedURLException unused2) {
                throw new hc(AMapException.ERROR_URL);
            } catch (SocketTimeoutException unused3) {
                throw new hc(AMapException.ERROR_SOCKE_TIME_OUT);
            } catch (InterruptedIOException unused4) {
                throw new hc(AMapException.ERROR_UNKNOWN);
            } catch (SocketException unused5) {
                throw new hc(AMapException.ERROR_SOCKET);
            } catch (UnknownHostException unused6) {
                throw new hc(AMapException.ERROR_UNKNOW_HOST);
            } catch (IOException unused7) {
                throw new hc(AMapException.ERROR_IO);
            } catch (Throwable th3) {
                th = th3;
                th.printStackTrace();
                throw new hc(AMapException.ERROR_UNKNOWN);
            }
        } catch (hc e2) {
            throw e2;
        } catch (ConnectException unused8) {
        } catch (MalformedURLException unused9) {
        } catch (SocketException unused10) {
        } catch (SocketTimeoutException unused11) {
        } catch (InterruptedIOException unused12) {
        } catch (UnknownHostException unused13) {
        } catch (IOException unused14) {
        } catch (Throwable th4) {
            th = th4;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(long j) {
        this.g = j;
    }
}
