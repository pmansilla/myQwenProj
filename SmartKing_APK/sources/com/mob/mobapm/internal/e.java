package com.mob.mobapm.internal;

import android.os.Build;
import com.mob.tools.MobLog;
import com.mob.tools.network.HttpConnection;
import com.mob.tools.network.HttpConnectionImpl23;
import com.mob.tools.network.HttpResponseCallback;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.network.RawNetworkCallback;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ReflectHelper;
import com.tencent.bugly.Bugly;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import kotlin.text.Typography;
import no.nordicsemi.android.dfu.DfuBaseService;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;

/* loaded from: classes.dex */
public class e {
    private static boolean b = true;
    protected boolean a = b;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements RawNetworkCallback {
        final /* synthetic */ byte[] a;
        final /* synthetic */ OutputStream b;

        a(e eVar, byte[] bArr, OutputStream outputStream) {
            this.a = bArr;
            this.b = outputStream;
        }

        @Override // com.mob.tools.network.RawNetworkCallback
        public void onResponse(InputStream inputStream) throws Throwable {
            int read = inputStream.read(this.a);
            while (read != -1) {
                this.b.write(this.a, 0, read);
                read = inputStream.read(this.a);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements HttpResponseCallback {
        final /* synthetic */ HashMap a;

        b(e eVar, HashMap hashMap) {
            this.a = hashMap;
        }

        @Override // com.mob.tools.network.HttpResponseCallback
        public void onResponse(HttpConnection httpConnection) throws Throwable {
            this.a.put("code", String.valueOf(httpConnection.getResponseCode()));
        }
    }

    /* loaded from: classes.dex */
    public static class c {
        public int a;
        public int b;
    }

    /* loaded from: classes.dex */
    public static final class d implements X509TrustManager {
        private X509TrustManager a;

        public d(KeyStore keyStore) {
            try {
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("X509");
                trustManagerFactory.init(keyStore);
                TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
                if (trustManagers == null || trustManagers.length == 0) {
                    throw new NoSuchAlgorithmException("no trust manager found.");
                }
                this.a = (X509TrustManager) trustManagers[0];
            } catch (Exception e) {
                MobLog.getInstance().d("failed to initialize the standard trust manager: " + e.getMessage(), new Object[0]);
                this.a = null;
            }
        }

        @Override // javax.net.ssl.X509TrustManager
        public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        }

        @Override // javax.net.ssl.X509TrustManager
        public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
            if (x509CertificateArr == null) {
                throw new IllegalArgumentException("there were no certificates.");
            }
            if (x509CertificateArr.length == 1) {
                x509CertificateArr[0].checkValidity();
                return;
            }
            X509TrustManager x509TrustManager = this.a;
            if (x509TrustManager == null) {
                throw new CertificateException("there were one more certificates but no trust manager found.");
            }
            x509TrustManager.checkServerTrusted(x509CertificateArr, str);
        }

        @Override // javax.net.ssl.X509TrustManager
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private com.mob.mobapm.internal.b a(HttpURLConnection httpURLConnection, String str, ArrayList<KVPair<String>> arrayList) throws Throwable {
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        f fVar = new f();
        if (arrayList != null) {
            fVar.a(a(arrayList));
        }
        return fVar;
    }

    private com.mob.mobapm.internal.b a(HttpURLConnection httpURLConnection, String str, ArrayList<KVPair<String>> arrayList, ArrayList<KVPair<String>> arrayList2) throws Throwable {
        String uuid = UUID.randomUUID().toString();
        httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + uuid);
        com.mob.mobapm.internal.c cVar = new com.mob.mobapm.internal.c();
        f fVar = new f();
        if (arrayList != null) {
            Iterator<KVPair<String>> it = arrayList.iterator();
            while (it.hasNext()) {
                KVPair<String> next = it.next();
                fVar.a("--").a(uuid).a("\r\n");
                fVar.a("Content-Disposition: form-data; name=\"").a(next.name).a("\"\r\n\r\n");
                fVar.a(next.value).a("\r\n");
            }
        }
        cVar.a(fVar);
        Iterator<KVPair<String>> it2 = arrayList2.iterator();
        while (it2.hasNext()) {
            KVPair<String> next2 = it2.next();
            f fVar2 = new f();
            File file = new File(next2.value);
            fVar2.a("--").a(uuid).a("\r\n");
            fVar2.a("Content-Disposition: form-data; name=\"").a(next2.name).a("\"; filename=\"").a(file.getName()).a("\"\r\n");
            String contentTypeFor = URLConnection.getFileNameMap().getContentTypeFor(next2.value);
            if (contentTypeFor == null || contentTypeFor.length() <= 0) {
                if (next2.value.toLowerCase().endsWith("jpg") || next2.value.toLowerCase().endsWith("jpeg")) {
                    contentTypeFor = "image/jpeg";
                } else if (next2.value.toLowerCase().endsWith("png")) {
                    contentTypeFor = "image/png";
                } else if (next2.value.toLowerCase().endsWith("gif")) {
                    contentTypeFor = "image/gif";
                } else {
                    FileInputStream fileInputStream = new FileInputStream(next2.value);
                    String guessContentTypeFromStream = URLConnection.guessContentTypeFromStream(fileInputStream);
                    fileInputStream.close();
                    contentTypeFor = (guessContentTypeFromStream == null || guessContentTypeFromStream.length() <= 0) ? DfuBaseService.MIME_TYPE_OCTET_STREAM : guessContentTypeFromStream;
                }
            }
            fVar2.a("Content-Type: ").a(contentTypeFor).a("\r\n\r\n");
            cVar.a(fVar2);
            com.mob.mobapm.internal.a aVar = new com.mob.mobapm.internal.a();
            aVar.a(next2.value);
            cVar.a(aVar);
            f fVar3 = new f();
            fVar3.a("\r\n");
            cVar.a(fVar3);
        }
        f fVar4 = new f();
        fVar4.a("--").a(uuid).a("--\r\n");
        cVar.a(fVar4);
        return cVar;
    }

    private String a(ArrayList<KVPair<String>> arrayList) throws Throwable {
        StringBuilder sb = new StringBuilder();
        Iterator<KVPair<String>> it = arrayList.iterator();
        while (it.hasNext()) {
            KVPair<String> next = it.next();
            String urlEncode = Data.urlEncode(next.name, "utf-8");
            String str = next.value;
            String urlEncode2 = str != null ? Data.urlEncode(str, "utf-8") : "";
            if (sb.length() > 0) {
                sb.append(Typography.amp);
            }
            sb.append(urlEncode);
            sb.append('=');
            sb.append(urlEncode2);
        }
        return sb.toString();
    }

    private HttpURLConnection a(String str, c cVar) throws Throwable {
        Object obj;
        String str2;
        boolean z;
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        try {
            obj = ReflectHelper.getInstanceField(httpURLConnection, "methodTokens");
        } catch (Throwable unused) {
            obj = null;
        }
        if (obj == null) {
            try {
                obj = ReflectHelper.getStaticField("HttpURLConnection", "PERMITTED_USER_METHODS");
            } catch (Throwable unused2) {
            }
            str2 = "PERMITTED_USER_METHODS";
            z = true;
        } else {
            str2 = "methodTokens";
            z = false;
        }
        if (obj != null) {
            String[] strArr = (String[]) obj;
            String[] strArr2 = new String[strArr.length + 1];
            System.arraycopy(strArr, 0, strArr2, 0, strArr.length);
            strArr2[strArr.length] = "PATCH";
            if (z) {
                ReflectHelper.setStaticField("HttpURLConnection", str2, strArr2);
            } else {
                ReflectHelper.setInstanceField(httpURLConnection, str2, strArr2);
            }
        }
        if (Build.VERSION.SDK_INT < 8) {
            System.setProperty("http.keepAlive", Bugly.SDK_IS_DEV);
        }
        if (httpURLConnection instanceof HttpsURLConnection) {
            X509HostnameVerifier x509HostnameVerifier = SSLSocketFactory.STRICT_HOSTNAME_VERIFIER;
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) httpURLConnection;
            SSLContext sSLContext = SSLContext.getInstance("TLS");
            sSLContext.init(null, new TrustManager[]{new d(null)}, new SecureRandom());
            httpsURLConnection.setSSLSocketFactory(sSLContext.getSocketFactory());
            httpsURLConnection.setHostnameVerifier(x509HostnameVerifier);
        }
        int i = cVar == null ? NetworkHelper.connectionTimeout : cVar.b;
        if (i > 0) {
            httpURLConnection.setConnectTimeout(i);
        }
        int i2 = cVar == null ? NetworkHelper.readTimout : cVar.a;
        if (i2 > 0) {
            httpURLConnection.setReadTimeout(i2);
        }
        return httpURLConnection;
    }

    public String a(String str, ArrayList<KVPair<String>> arrayList, ArrayList<KVPair<String>> arrayList2, c cVar) throws Throwable {
        long currentTimeMillis = System.currentTimeMillis();
        MobLog.getInstance().i("httpGet: " + str, new Object[0]);
        if (arrayList != null) {
            String a2 = a(arrayList);
            if (a2.length() > 0) {
                str = str + "?" + a2;
            }
        }
        HttpURLConnection a3 = a(str, cVar);
        if (arrayList2 != null) {
            Iterator<KVPair<String>> it = arrayList2.iterator();
            while (it.hasNext()) {
                KVPair<String> next = it.next();
                a3.setRequestProperty(next.name, next.value);
            }
        }
        a3.setInstanceFollowRedirects(this.a);
        a3.connect();
        int responseCode = a3.getResponseCode();
        if (responseCode != 200) {
            StringBuilder sb = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(a3.getErrorStream(), Charset.forName("utf-8")));
            for (String readLine = bufferedReader.readLine(); readLine != null; readLine = bufferedReader.readLine()) {
                if (sb.length() > 0) {
                    sb.append('\n');
                }
                sb.append(readLine);
            }
            bufferedReader.close();
            a3.disconnect();
            HashMap hashMap = new HashMap();
            hashMap.put("error", sb.toString());
            hashMap.put("status", Integer.valueOf(responseCode));
            throw new Throwable(new Hashon().fromHashMap(hashMap));
        }
        StringBuilder sb2 = new StringBuilder();
        BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(a3.getInputStream(), Charset.forName("utf-8")));
        for (String readLine2 = bufferedReader2.readLine(); readLine2 != null; readLine2 = bufferedReader2.readLine()) {
            if (sb2.length() > 0) {
                sb2.append('\n');
            }
            sb2.append(readLine2);
        }
        bufferedReader2.close();
        a3.disconnect();
        String sb3 = sb2.toString();
        MobLog.getInstance().i("use time: " + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
        return sb3;
    }

    public HashMap<String, String> a(String str, ArrayList<KVPair<String>> arrayList, KVPair<String> kVPair, ArrayList<KVPair<String>> arrayList2, int i, c cVar) throws Throwable {
        ArrayList<KVPair<String>> arrayList3 = new ArrayList<>();
        if (kVPair != null && kVPair.value != null && new File(kVPair.value).exists()) {
            arrayList3.add(kVPair);
        }
        return a(str, arrayList, arrayList3, arrayList2, i, cVar);
    }

    public HashMap<String, String> a(String str, ArrayList<KVPair<String>> arrayList, KVPair<String> kVPair, ArrayList<KVPair<String>> arrayList2, c cVar) throws Throwable {
        return a(str, arrayList, kVPair, arrayList2, 0, cVar);
    }

    public HashMap<String, String> a(String str, ArrayList<KVPair<String>> arrayList, ArrayList<KVPair<String>> arrayList2, ArrayList<KVPair<String>> arrayList3, int i, c cVar) throws Throwable {
        HashMap<String, String> hashMap = new HashMap<>();
        a(str, arrayList, arrayList2, arrayList3, i, new b(this, hashMap), cVar);
        return hashMap;
    }

    public void a(String str, RawNetworkCallback rawNetworkCallback, c cVar) throws Throwable {
        a(str, (ArrayList<KVPair<String>>) null, rawNetworkCallback, cVar);
    }

    public void a(String str, ArrayList<KVPair<String>> arrayList, com.mob.mobapm.internal.b bVar, int i, HttpResponseCallback httpResponseCallback, c cVar) throws Throwable {
        long currentTimeMillis = System.currentTimeMillis();
        MobLog.getInstance().i("rawpost: " + str, new Object[0]);
        HttpURLConnection a2 = a(str, cVar);
        a2.setDoOutput(true);
        if (i >= 0) {
            a2.setChunkedStreamingMode(0);
        }
        if (arrayList != null) {
            Iterator<KVPair<String>> it = arrayList.iterator();
            while (it.hasNext()) {
                KVPair<String> next = it.next();
                a2.setRequestProperty(next.name, next.value);
            }
        }
        a2.setInstanceFollowRedirects(this.a);
        a2.connect();
        OutputStream outputStream = a2.getOutputStream();
        InputStream c2 = bVar.c();
        byte[] bArr = new byte[65536];
        for (int read = c2.read(bArr); read > 0; read = c2.read(bArr)) {
            outputStream.write(bArr, 0, read);
        }
        outputStream.flush();
        c2.close();
        outputStream.close();
        if (httpResponseCallback != null) {
            try {
                httpResponseCallback.onResponse(new HttpConnectionImpl23(a2));
                a2.disconnect();
            } finally {
            }
        }
        MobLog.getInstance().i("use time: " + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
    }

    public void a(String str, ArrayList<KVPair<String>> arrayList, RawNetworkCallback rawNetworkCallback, c cVar) throws Throwable {
        long currentTimeMillis = System.currentTimeMillis();
        MobLog.getInstance().i("rawGet: " + str, new Object[0]);
        HttpURLConnection a2 = a(str, cVar);
        if (arrayList != null) {
            Iterator<KVPair<String>> it = arrayList.iterator();
            while (it.hasNext()) {
                KVPair<String> next = it.next();
                a2.setRequestProperty(next.name, next.value);
            }
        }
        a2.setInstanceFollowRedirects(this.a);
        a2.connect();
        int responseCode = a2.getResponseCode();
        if (responseCode == 200) {
            if (rawNetworkCallback != null) {
                rawNetworkCallback.onResponse(a2.getInputStream());
            }
            a2.disconnect();
            MobLog.getInstance().i("use time: " + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
            return;
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(a2.getErrorStream(), Charset.forName("utf-8")));
        for (String readLine = bufferedReader.readLine(); readLine != null; readLine = bufferedReader.readLine()) {
            if (sb.length() > 0) {
                sb.append('\n');
            }
            sb.append(readLine);
        }
        bufferedReader.close();
        a2.disconnect();
        HashMap hashMap = new HashMap();
        hashMap.put("error", sb.toString());
        hashMap.put("status", Integer.valueOf(responseCode));
        throw new Throwable(new Hashon().fromHashMap(hashMap));
    }

    public void a(String str, ArrayList<KVPair<String>> arrayList, ArrayList<KVPair<String>> arrayList2, ArrayList<KVPair<String>> arrayList3, int i, HttpResponseCallback httpResponseCallback, c cVar) throws Throwable {
        com.mob.mobapm.internal.b a2;
        long currentTimeMillis = System.currentTimeMillis();
        MobLog.getInstance().i("httpPost: " + str, new Object[0]);
        HttpURLConnection a3 = a(str, cVar);
        a3.setDoOutput(true);
        a3.setRequestProperty("Connection", "Keep-Alive");
        if (arrayList2 == null || arrayList2.size() <= 0) {
            a2 = a(a3, str, arrayList);
            a3.setFixedLengthStreamingMode((int) a2.b());
        } else {
            a2 = a(a3, str, arrayList, arrayList2);
            if (i >= 0) {
                a3.setChunkedStreamingMode(i);
            }
        }
        if (arrayList3 != null) {
            Iterator<KVPair<String>> it = arrayList3.iterator();
            while (it.hasNext()) {
                KVPair<String> next = it.next();
                a3.setRequestProperty(next.name, next.value);
            }
        }
        a3.setInstanceFollowRedirects(this.a);
        a3.connect();
        OutputStream outputStream = a3.getOutputStream();
        InputStream c2 = a2.c();
        byte[] bArr = new byte[65536];
        for (int read = c2.read(bArr); read > 0; read = c2.read(bArr)) {
            outputStream.write(bArr, 0, read);
        }
        outputStream.flush();
        c2.close();
        outputStream.close();
        if (httpResponseCallback != null) {
            try {
                httpResponseCallback.onResponse(new HttpConnectionImpl23(a3));
                a3.disconnect();
            } finally {
            }
        }
        MobLog.getInstance().i("use time: " + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
    }

    public boolean a(String str, OutputStream outputStream, c cVar) throws Throwable {
        a(str, new a(this, new byte[1024], outputStream), cVar);
        outputStream.flush();
        return true;
    }

    public HashMap<String, Object> b(String str, ArrayList<KVPair<String>> arrayList, ArrayList<KVPair<String>> arrayList2, c cVar) throws Throwable {
        long currentTimeMillis = System.currentTimeMillis();
        MobLog.getInstance().i("httpGet: " + str, new Object[0]);
        if (arrayList != null) {
            String a2 = a(arrayList);
            if (a2.length() > 0) {
                str = str + "?" + a2;
            }
        }
        HttpURLConnection a3 = a(str, cVar);
        if (arrayList2 != null) {
            Iterator<KVPair<String>> it = arrayList2.iterator();
            while (it.hasNext()) {
                KVPair<String> next = it.next();
                a3.setRequestProperty(next.name, next.value);
            }
        }
        a3.setInstanceFollowRedirects(this.a);
        a3.connect();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("code", String.valueOf(a3.getResponseCode()));
        MobLog.getInstance().i("use time: " + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
        return hashMap;
    }
}
