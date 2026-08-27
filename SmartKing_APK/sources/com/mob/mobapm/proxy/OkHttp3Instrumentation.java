package com.mob.mobapm.proxy;

import com.mob.tools.proguard.ClassKeeper;
import java.net.HttpURLConnection;
import javax.net.ssl.HttpsURLConnection;

/* loaded from: classes.dex */
public class OkHttp3Instrumentation implements ClassKeeper {
    public static HttpURLConnection open(HttpURLConnection httpURLConnection) {
        if (!com.mob.mobapm.core.c.e) {
            return httpURLConnection;
        }
        if (httpURLConnection instanceof HttpsURLConnection) {
            return new b((HttpsURLConnection) httpURLConnection);
        }
        if (httpURLConnection != null) {
            return new a(httpURLConnection);
        }
        return null;
    }

    public static HttpURLConnection openWithProxy(HttpURLConnection httpURLConnection) {
        if (!com.mob.mobapm.core.c.e) {
            return httpURLConnection;
        }
        if (httpURLConnection instanceof HttpsURLConnection) {
            return new b((HttpsURLConnection) httpURLConnection);
        }
        if (httpURLConnection != null) {
            return new a(httpURLConnection);
        }
        return null;
    }

    public static HttpURLConnection urlFactoryOpen(HttpURLConnection httpURLConnection) {
        if (!com.mob.mobapm.core.c.e) {
            return httpURLConnection;
        }
        com.mob.mobapm.d.a.a().d("OkHttpInstrumentation - wrapping return of call to OkUrlFactory.open...", new Object[0]);
        if (httpURLConnection instanceof HttpsURLConnection) {
            return new b((HttpsURLConnection) httpURLConnection);
        }
        if (httpURLConnection != null) {
            return new a(httpURLConnection);
        }
        return null;
    }
}
