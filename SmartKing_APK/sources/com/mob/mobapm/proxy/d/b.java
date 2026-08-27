package com.mob.mobapm.proxy.d;

import com.mob.MobSDK;
import com.mob.mobapm.bean.TransactionType;
import com.mob.mobapm.core.Transaction;
import com.mob.mobapm.e.d;
import com.mob.mobapm.e.f;
import com.mob.tools.utils.DeviceHelper;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import javax.net.ssl.SSLException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

/* loaded from: classes.dex */
public class b extends com.mob.mobapm.c.a {
    /* JADX WARN: Removed duplicated region for block: B:30:0x00b5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static org.apache.http.HttpRequest a(com.mob.mobapm.core.Transaction r7, org.apache.http.HttpHost r8, org.apache.http.HttpRequest r9) {
        /*
            com.mob.tools.log.NLog r0 = com.mob.mobapm.d.a.a()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "APM: HttpClient request start, switch is "
            r1.append(r2)
            boolean r2 = com.mob.mobapm.core.c.e
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r2 = 0
            java.lang.Object[] r3 = new java.lang.Object[r2]
            r0.i(r1, r3)
            boolean r0 = com.mob.mobapm.core.c.e
            if (r0 == 0) goto Ldc
            if (r7 != 0) goto L25
            goto Ldc
        L25:
            org.apache.http.RequestLine r0 = r9.getRequestLine()     // Catch: java.lang.Throwable -> Lc1
            java.lang.String r0 = r0.getUri()     // Catch: java.lang.Throwable -> Lc1
            r1 = 0
            java.net.URL r3 = new java.net.URL     // Catch: java.net.MalformedURLException -> L38 java.lang.Throwable -> Lc1
            r3.<init>(r0)     // Catch: java.net.MalformedURLException -> L38 java.lang.Throwable -> Lc1
            java.lang.String r3 = r3.getHost()     // Catch: java.net.MalformedURLException -> L38 java.lang.Throwable -> Lc1
            goto L5b
        L38:
            r3 = move-exception
            com.mob.tools.log.NLog r4 = com.mob.mobapm.d.a.a()     // Catch: java.lang.Throwable -> Lc1
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lc1
            r5.<init>()     // Catch: java.lang.Throwable -> Lc1
            java.lang.String r6 = "dispatchHttpClientRequest error!"
            r5.append(r6)     // Catch: java.lang.Throwable -> Lc1
            java.lang.String r3 = r3.getMessage()     // Catch: java.lang.Throwable -> Lc1
            r5.append(r3)     // Catch: java.lang.Throwable -> Lc1
            r5.append(r0)     // Catch: java.lang.Throwable -> Lc1
            java.lang.String r3 = r5.toString()     // Catch: java.lang.Throwable -> Lc1
            java.lang.Object[] r5 = new java.lang.Object[r2]     // Catch: java.lang.Throwable -> Lc1
            r4.i(r3, r5)     // Catch: java.lang.Throwable -> Lc1
            r3 = r1
        L5b:
            org.apache.http.RequestLine r4 = r9.getRequestLine()     // Catch: java.lang.Throwable -> Lc1
            if (r4 == 0) goto Lb9
            if (r0 == 0) goto L7a
            int r5 = r0.length()     // Catch: java.lang.Throwable -> Lc1
            r6 = 10
            if (r5 < r6) goto L7a
            java.lang.String r5 = r0.substring(r2, r6)     // Catch: java.lang.Throwable -> Lc1
            java.lang.String r6 = "://"
            int r5 = r5.indexOf(r6)     // Catch: java.lang.Throwable -> Lc1
            if (r5 >= 0) goto L78
            goto L7a
        L78:
            r5 = 1
            goto L7b
        L7a:
            r5 = 0
        L7b:
            if (r5 != 0) goto Lb3
            if (r0 == 0) goto Lb3
            if (r3 == 0) goto Lb3
            java.lang.String r8 = r8.toURI()     // Catch: java.lang.Throwable -> Lc1
            java.lang.String r8 = r8.toString()     // Catch: java.lang.Throwable -> Lc1
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lc1
            r1.<init>()     // Catch: java.lang.Throwable -> Lc1
            r1.append(r8)     // Catch: java.lang.Throwable -> Lc1
            java.lang.String r5 = "/"
            boolean r8 = r8.endsWith(r5)     // Catch: java.lang.Throwable -> Lc1
            if (r8 != 0) goto La5
            java.lang.String r8 = "/"
            boolean r8 = r0.startsWith(r8)     // Catch: java.lang.Throwable -> Lc1
            if (r8 == 0) goto La2
            goto La5
        La2:
            java.lang.String r8 = "/"
            goto La7
        La5:
            java.lang.String r8 = ""
        La7:
            r1.append(r8)     // Catch: java.lang.Throwable -> Lc1
            r1.append(r0)     // Catch: java.lang.Throwable -> Lc1
            java.lang.String r8 = r1.toString()     // Catch: java.lang.Throwable -> Lc1
            r1 = r8
            goto Lb6
        Lb3:
            if (r5 == 0) goto Lb6
            r1 = r0
        Lb6:
            r7.setPath(r1)     // Catch: java.lang.Throwable -> Lc1
        Lb9:
            java.lang.String r8 = r4.getMethod()     // Catch: java.lang.Throwable -> Lc1
            a(r7, r0, r3, r1, r8)     // Catch: java.lang.Throwable -> Lc1
            goto Ldc
        Lc1:
            r7 = move-exception
            com.mob.tools.log.NLog r8 = com.mob.mobapm.d.a.a()
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "APM: HttpClient request start error:"
            r0.append(r1)
            r0.append(r7)
            java.lang.String r7 = r0.toString()
            java.lang.Object[] r0 = new java.lang.Object[r2]
            r8.d(r7, r0)
        Ldc:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.mobapm.proxy.d.b.a(com.mob.mobapm.core.Transaction, org.apache.http.HttpHost, org.apache.http.HttpRequest):org.apache.http.HttpRequest");
    }

    public static HttpResponse a(Transaction transaction, HttpResponse httpResponse) {
        com.mob.mobapm.d.a.a().i("APM: HttpClient request end, transaction switch is " + transaction.isCreate(), new Object[0]);
        if (com.mob.mobapm.core.c.e && transaction != null && transaction.isCreate()) {
            int i = -1;
            try {
                httpResponse.setEntity(new a(httpResponse.getEntity()));
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                if (statusCode >= 300) {
                    try {
                        transaction.setErrMsg(d.b(httpResponse.getEntity().getContent()));
                    } catch (Throwable th) {
                        th = th;
                        i = statusCode;
                        if (th instanceof UnknownHostException) {
                            i = 901;
                        } else if (th instanceof SocketTimeoutException) {
                            i = 903;
                        } else if (th instanceof ConnectException) {
                            i = 902;
                        } else if (th instanceof SSLException) {
                            i = 908;
                        }
                        com.mob.mobapm.d.a.a().i("APM: HttpClient get response code exception :" + th, new Object[0]);
                        com.mob.mobapm.c.a.a(transaction, (String) null, i);
                        return httpResponse;
                    }
                }
                i = statusCode;
            } catch (Throwable th2) {
                th = th2;
            }
            com.mob.mobapm.c.a.a(transaction, (String) null, i);
        }
        return httpResponse;
    }

    public static HttpUriRequest a(Transaction transaction, HttpUriRequest httpUriRequest) {
        com.mob.mobapm.d.a.a().i("APM: HttpClient request start, switch is " + com.mob.mobapm.core.c.e, new Object[0]);
        if (com.mob.mobapm.core.c.e && transaction != null) {
            a(transaction, httpUriRequest.getURI().toString(), httpUriRequest.getURI().getHost(), httpUriRequest.getURI().getPath(), httpUriRequest.getMethod());
        }
        return httpUriRequest;
    }

    private static void a(Transaction transaction, String str, String str2, String str3, String str4) {
        com.mob.mobapm.d.a.a().i("APM: HttpClient request start, switch is " + com.mob.mobapm.core.c.e, new Object[0]);
        if (!com.mob.mobapm.core.c.e || transaction == null) {
            return;
        }
        try {
            transaction.setTransType(!str.contains("https") ? TransactionType.http : TransactionType.https);
            transaction.setHost(str2);
            transaction.setPath(str3);
            f.a(transaction);
            transaction.setImei(DeviceHelper.getInstance(MobSDK.getContext()).getIMEI());
            transaction.setDuid(com.mob.mobapm.core.d.e());
            transaction.setMac(DeviceHelper.getInstance(MobSDK.getContext()).getMacAddress());
            transaction.setNetworkType(DeviceHelper.getInstance(MobSDK.getContext()).getNetworkType());
            transaction.setDataNetworkType(String.valueOf(DeviceHelper.getInstance(MobSDK.getContext()).getDataNtType()));
            transaction.setTransStatus(1);
            transaction.setClientTime(System.currentTimeMillis());
            transaction.setRequestTime(transaction.getClientTime());
            transaction.setMethod(str4);
        } catch (Throwable th) {
            com.mob.mobapm.d.a.a().d("APM: HttpClient request start error:" + th, new Object[0]);
        }
    }
}
