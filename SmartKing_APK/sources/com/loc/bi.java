package com.loc;

import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import com.amap.api.maps.AMapException;
import com.loc.bg;
import java.io.DataOutputStream;
import java.io.IOException;
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
public final class bi {
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
    private bg.a k;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: HttpUrlUtil.java */
    /* loaded from: classes.dex */
    public static class a {
        private Vector<b> a;
        private volatile b b;

        private a() {
            this.a = new Vector<>();
            this.b = new b((byte) 0);
        }

        /* synthetic */ a(byte b) {
            this();
        }

        public final b a() {
            return this.b;
        }

        public final b a(String str) {
            if (TextUtils.isEmpty(str)) {
                return this.b;
            }
            byte b = 0;
            for (int i = 0; i < this.a.size(); i++) {
                b bVar = this.a.get(i);
                if (bVar != null && bVar.a().equals(str)) {
                    return bVar;
                }
            }
            b bVar2 = new b(b);
            bVar2.b(str);
            this.a.add(bVar2);
            return bVar2;
        }

        public final void b(String str) {
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

        /* synthetic */ b(byte b) {
            this();
        }

        public final String a() {
            return this.b;
        }

        public final void a(String str) {
            this.a = str;
        }

        public final void b(String str) {
            this.b = str;
        }

        @Override // javax.net.ssl.HostnameVerifier
        public final boolean verify(String str, SSLSession sSLSession) {
            HostnameVerifier defaultHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
            return !TextUtils.isEmpty(this.a) ? this.a.equals(str) : !TextUtils.isEmpty(this.b) ? defaultHostnameVerifier.verify(this.b, sSLSession) : defaultHostnameVerifier.verify(str, sSLSession);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public bi(int i, int i2, Proxy proxy, boolean z) {
        this(i, i2, proxy, z, (byte) 0);
    }

    private bi(int i, int i2, Proxy proxy, boolean z, byte b2) {
        byte b3 = 0;
        this.f = false;
        this.g = -1L;
        this.h = 0L;
        this.a = i;
        this.b = i2;
        this.e = proxy;
        this.c = z.a().b(z);
        if (z.b()) {
            this.c = false;
        }
        this.k = null;
        try {
            this.i = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        } catch (Throwable th) {
            an.a(th, "ht", "ic");
        }
        if (this.c) {
            try {
                SSLContext sSLContext = SSLContext.getInstance("TLS");
                sSLContext.init(null, null, null);
                this.d = sSLContext;
            } catch (Throwable th2) {
                an.a(th2, "ht", "ne");
            }
        }
        this.j = new a(b3);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0143 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:75:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0135 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0127 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0119 A[EXC_TOP_SPLITTER, SYNTHETIC] */
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
    private com.loc.bk a(java.net.HttpURLConnection r11, boolean r12) throws com.loc.t, java.io.IOException {
        /*
            Method dump skipped, instructions count: 336
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.bi.a(java.net.HttpURLConnection, boolean):com.loc.bk");
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
            an.a(th, "ht", "adh");
        }
        httpURLConnection.setConnectTimeout(this.a);
        httpURLConnection.setReadTimeout(this.b);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final bk a(String str, boolean z, String str2, Map<String, String> map, byte[] bArr, boolean z2) throws t {
        HttpURLConnection httpURLConnection;
        HttpURLConnection httpURLConnection2 = null;
        try {
            try {
                x.c();
                if (map == null) {
                    map = new HashMap<>();
                }
                b a2 = this.j.a();
                if (z && !TextUtils.isEmpty(str2)) {
                    a2 = this.j.a(str2);
                }
                String str3 = "";
                if (bg.a == 1) {
                    str3 = bg.b;
                }
                if (!TextUtils.isEmpty(str3)) {
                    Uri parse = Uri.parse(str);
                    String host = parse.getHost();
                    str = parse.buildUpon().encodedAuthority(str3).build().toString();
                    if (map != null) {
                        map.put("targetHost", host);
                    }
                    if (this.c) {
                        this.j.b(str3);
                    }
                }
                if (this.c) {
                    str = z.a(str);
                }
                URL url = new URL(str);
                URLConnection a3 = this.k != null ? this.k.a() : null;
                if (a3 == null) {
                    a3 = this.e != null ? url.openConnection(this.e) : url.openConnection();
                }
                if (this.c) {
                    httpURLConnection = (HttpsURLConnection) a3;
                    ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(this.d.getSocketFactory());
                    ((HttpsURLConnection) httpURLConnection).setHostnameVerifier(a2);
                } else {
                    httpURLConnection = (HttpURLConnection) a3;
                }
                if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
                    httpURLConnection.setRequestProperty("Connection", "close");
                }
                a(map, httpURLConnection);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                if (bArr != null) {
                    try {
                        if (bArr.length > 0) {
                            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                            dataOutputStream.write(bArr);
                            dataOutputStream.close();
                        }
                    } catch (t e) {
                        e = e;
                        an.a(e, "ht", "mPt");
                        throw e;
                    } catch (ConnectException e2) {
                        e = e2;
                        e.printStackTrace();
                        throw new t(AMapException.ERROR_CONNECTION);
                    } catch (MalformedURLException e3) {
                        e = e3;
                        e.printStackTrace();
                        throw new t(AMapException.ERROR_URL);
                    } catch (SocketException e4) {
                        e = e4;
                        e.printStackTrace();
                        throw new t(AMapException.ERROR_SOCKET);
                    } catch (SocketTimeoutException e5) {
                        e = e5;
                        e.printStackTrace();
                        throw new t(AMapException.ERROR_SOCKE_TIME_OUT);
                    } catch (InterruptedIOException unused) {
                        throw new t(AMapException.ERROR_UNKNOWN);
                    } catch (UnknownHostException e6) {
                        e = e6;
                        e.printStackTrace();
                        throw new t(AMapException.ERROR_UNKNOW_HOST);
                    } catch (IOException e7) {
                        e = e7;
                        e.printStackTrace();
                        throw new t(AMapException.ERROR_IO);
                    } catch (Throwable th) {
                        th = th;
                        an.a(th, "ht", "mPt");
                        throw new t(AMapException.ERROR_UNKNOWN);
                    }
                }
                bk a4 = a(httpURLConnection, z2);
                if (httpURLConnection != null) {
                    try {
                        httpURLConnection.disconnect();
                    } catch (Throwable th2) {
                        an.a(th2, "ht", "mPt");
                    }
                }
                return a4;
            } catch (Throwable th3) {
                th = th3;
            }
        } catch (t e8) {
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
}
