package com.loc;

import android.content.Context;
import android.net.TrafficStats;
import android.os.Build;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

/* loaded from: classes.dex */
final class dq implements Callable {
    private static dg c = dg.a();
    private static Context d;
    private String a;
    private int b = 1;

    /* JADX INFO: Access modifiers changed from: package-private */
    public dq(String str) {
        this.a = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(Context context) {
        d = context;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Override // java.util.concurrent.Callable
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public String[] call() {
        InputStream inputStream;
        BufferedReader bufferedReader;
        Throwable th;
        HttpURLConnection httpURLConnection;
        BufferedReader bufferedReader2;
        Throwable th2;
        try {
            try {
                if (!dl.a()) {
                    Thread thread = new Thread(new dr(this));
                    thread.setUncaughtExceptionHandler(new dm());
                    thread.start();
                }
                if (Build.VERSION.SDK_INT >= 14) {
                    TrafficStats.setThreadStatsTag(40965);
                }
                dg.c(this.a);
                httpURLConnection = (HttpURLConnection) new URL("http://203.107.1.1:80/" + dj.a + "/d?host=" + this.a).openConnection();
                try {
                    httpURLConnection.setConnectTimeout(15000);
                    httpURLConnection.setReadTimeout(15000);
                } catch (Throwable th3) {
                    bufferedReader = null;
                    th = th3;
                    inputStream = null;
                }
            } catch (Throwable th4) {
                th = th4;
            }
        } catch (Throwable th5) {
            inputStream = null;
            bufferedReader = null;
            th = th5;
            httpURLConnection = null;
        }
        if (httpURLConnection.getResponseCode() != 200) {
            dk.b("response code is " + httpURLConnection.getResponseCode() + " expect 200");
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            dg.d(this.a);
            return dj.b;
        }
        inputStream = httpURLConnection.getInputStream();
        try {
            bufferedReader2 = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            try {
                StringBuilder sb = new StringBuilder();
                while (true) {
                    String readLine = bufferedReader2.readLine();
                    if (readLine == null) {
                        break;
                    }
                    sb.append(readLine);
                }
                dk.a("resolve host: " + this.a + ", return: " + sb.toString());
                dh dhVar = new dh(sb.toString());
                if (dg.b() >= 100) {
                    throw new Exception("the total number of hosts is exceed 100");
                }
                dg.a(this.a, dhVar);
                dg.d(this.a);
                String[] a = dhVar.a();
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        dk.a(e);
                    }
                }
                bufferedReader2.close();
                return a;
            } catch (Throwable th6) {
                th2 = th6;
                dk.a(th2);
                int i = this.b;
                this.b = i - 1;
                if (i <= 0) {
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e2) {
                            dk.a(e2);
                        }
                    }
                    if (bufferedReader2 != null) {
                        bufferedReader2.close();
                    }
                    dg.d(this.a);
                    return dj.b;
                }
                String[] call = call();
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e3) {
                        dk.a(e3);
                        return call;
                    }
                }
                if (bufferedReader2 != null) {
                    bufferedReader2.close();
                }
                return call;
            }
        } catch (Throwable th7) {
            bufferedReader = null;
            th = th7;
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e4) {
                    dk.a(e4);
                    throw th;
                }
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            throw th;
        }
    }
}
