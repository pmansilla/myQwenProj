package com.mob.mobapm.proxy;

import com.mob.tools.proguard.ClassKeeper;
import java.net.HttpURLConnection;
import javax.net.ssl.HttpsURLConnection;

/* loaded from: classes.dex */
public class OkHttpInstrumentation implements ClassKeeper {
    public static HttpURLConnection open(HttpURLConnection httpURLConnection) {
        if (com.mob.mobapm.core.c.e) {
            if (httpURLConnection instanceof HttpsURLConnection) {
                return new b((HttpsURLConnection) httpURLConnection);
            }
            if (httpURLConnection != null) {
                return new a(httpURLConnection);
            }
        }
        return httpURLConnection;
    }

    public static HttpURLConnection openWithProxy(HttpURLConnection httpURLConnection) {
        if (com.mob.mobapm.core.c.e) {
            if (httpURLConnection instanceof HttpsURLConnection) {
                return new b((HttpsURLConnection) httpURLConnection);
            }
            if (httpURLConnection != null) {
                return new a(httpURLConnection);
            }
        }
        return httpURLConnection;
    }

    public static HttpURLConnection urlFactoryOpen(HttpURLConnection httpURLConnection) {
        com.mob.mobapm.d.a.a().d("OkHttpInstrumentation - wrapping return of call to OkUrlFactory.open...", new Object[0]);
        if (com.mob.mobapm.core.c.e) {
            if (httpURLConnection instanceof HttpsURLConnection) {
                return new b((HttpsURLConnection) httpURLConnection);
            }
            if (httpURLConnection != null) {
                return new a(httpURLConnection);
            }
        }
        return httpURLConnection;
    }
}
