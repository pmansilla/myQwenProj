package com.mob.mobapm.proxy.okhttp3;

import com.mob.mobapm.core.Transaction;
import com.mob.tools.proguard.ClassKeeper;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.OkUrlFactory;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Internal;
import okhttp3.internal.connection.StreamAllocation;

/* loaded from: classes.dex */
public class OkHttp3Instrumentation implements ClassKeeper {

    /* loaded from: classes.dex */
    public static class OkHttp35 implements ClassKeeper {
        public static StreamAllocation callEngineGetStreamAllocation(Internal internal, Call call) {
            Method method;
            try {
                if (call instanceof a) {
                    call = ((a) call).a();
                }
                method = Internal.class.getMethod("callEngineGetStreamAllocation", Call.class);
            } catch (Exception e) {
                com.mob.mobapm.d.a.a().i("APM: callEngineGetStreamAllocation error: " + e.getMessage(), new Object[0]);
            }
            if (method != null) {
                return (StreamAllocation) method.invoke(internal, call);
            }
            OkHttp3Instrumentation.logReflectionError("callEngineGetStreamAllocation(Lokhttp3/Call;)Lokhttp3/internal/connection/StreamAllocation;");
            return null;
        }

        public static Call newWebSocketCall(Internal internal, OkHttpClient okHttpClient, Request request) {
            Method method;
            try {
                method = Internal.class.getMethod("newWebSocketCall", OkHttpClient.class, Request.class);
            } catch (Exception e) {
                com.mob.mobapm.d.a.a().i("APM: newWebSocketCall error: " + e.getMessage(), new Object[0]);
            }
            if (method != null) {
                return new a(okHttpClient, request, (Call) method.invoke(internal, okHttpClient, request), new Transaction());
            }
            OkHttp3Instrumentation.logReflectionError("newWebSocketCall(Lokhttp3/OkHttpClient;Lokhttp3/Request;)Lokhttp3/Call;");
            return null;
        }

        public static void setCallWebSocket(Internal internal, Call call) {
            try {
                if (call instanceof a) {
                    call = ((a) call).a();
                }
                Method method = Internal.class.getMethod("setCallWebSocket", Call.class);
                if (method != null) {
                    method.invoke(internal, call);
                } else {
                    OkHttp3Instrumentation.logReflectionError("setCallWebSocket(Lokhttp3/Call;)V");
                }
            } catch (Exception e) {
                com.mob.mobapm.d.a.a().i("APM: set callwebsocket error: " + e.getMessage(), new Object[0]);
            }
        }
    }

    public static Response.Builder body(Response.Builder builder, ResponseBody responseBody) {
        return !com.mob.mobapm.core.c.e ? builder.body(responseBody) : new e(builder).body(responseBody);
    }

    public static Request build(Request.Builder builder) {
        return !com.mob.mobapm.core.c.e ? builder.build() : new d(builder).build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logReflectionError(String str) {
        String property = System.getProperty("line.separator");
        com.mob.mobapm.d.a.a().i("Unable to resolve method \"" + str + "\"." + property + "This is usually due to building the app with unsupported OkHttp versions." + property + "Check your build configuration for compatibility.", new Object[0]);
    }

    public static Response.Builder newBuilder(Response.Builder builder) {
        return !com.mob.mobapm.core.c.e ? builder : new e(builder);
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
