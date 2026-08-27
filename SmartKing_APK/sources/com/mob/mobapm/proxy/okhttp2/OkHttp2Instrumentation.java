package com.mob.mobapm.proxy.okhttp2;

import com.mob.mobapm.core.Transaction;
import com.mob.tools.proguard.ClassKeeper;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/* loaded from: classes.dex */
public class OkHttp2Instrumentation implements ClassKeeper {
    static final String CACHED_RESPONSE_CLASS = "com.squareup.okhttp.Cache$CacheResponseBody";

    public static Response.Builder body(Response.Builder builder, ResponseBody responseBody) {
        return !com.mob.mobapm.core.c.e ? builder.body(responseBody) : new f(builder).body(responseBody);
    }

    public static ResponseBody body(Response response) {
        return response.body();
    }

    public static Request build(Request.Builder builder) {
        return !com.mob.mobapm.core.c.e ? builder.build() : new e(builder).build();
    }

    public static Response.Builder newBuilder(Response.Builder builder) {
        return !com.mob.mobapm.core.c.e ? builder : new f(builder);
    }

    public static Call newCall(OkHttpClient okHttpClient, Request request) {
        if (!com.mob.mobapm.core.c.e) {
            return okHttpClient.newCall(request);
        }
        return new a(okHttpClient, request, okHttpClient.newCall(request), new Transaction());
    }

    public static HttpURLConnection open(OkUrlFactory okUrlFactory, URL url) {
        HttpURLConnection open = okUrlFactory.open(url);
        if (!com.mob.mobapm.core.c.e) {
            return open;
        }
        String protocol = url.getProtocol();
        return protocol.equals("http") ? new com.mob.mobapm.proxy.a(open) : (protocol.equals("https") && (open instanceof HttpsURLConnection)) ? new com.mob.mobapm.proxy.b((HttpsURLConnection) open) : new com.mob.mobapm.proxy.a(open);
    }
}
