package ycnet.runchinaup.core;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/* loaded from: classes2.dex */
public class OkHttpCore {
    private static OkHttpClient okHttpClient = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).retryOnConnectionFailure(true).build();

    public static Response getRequest(String str) throws IOException {
        return okHttpClient.newCall(packRequestData(str, (FormBody) null)).execute();
    }

    public static void getRequest(String str, Callback callback) {
        okHttpClient.newCall(packRequestData(str, (FormBody) null)).enqueue(callback);
    }

    private static Request packRequestData(String str, FormBody formBody) {
        Request.Builder url = new Request.Builder().url(str);
        if (formBody != null) {
            url.post(formBody);
        }
        return url.build();
    }

    private static Request packRequestData(String str, RequestBody requestBody) {
        Request.Builder url = new Request.Builder().url(str);
        if (requestBody != null) {
            url.post(requestBody);
        }
        return url.build();
    }

    public static Response postRequest(String str, FormBody formBody) throws IOException {
        return okHttpClient.newCall(packRequestData(str, formBody)).execute();
    }

    public static void postRequest(String str, Callback callback, FormBody formBody) {
        okHttpClient.newCall(packRequestData(str, formBody)).enqueue(callback);
    }

    public static void postRequest(String str, Callback callback, RequestBody requestBody) {
        okHttpClient.newCall(packRequestData(str, requestBody)).enqueue(callback);
    }

    public static Response uploadFile(String str, RequestBody requestBody) throws IOException {
        return okHttpClient.newCall(new Request.Builder().url(str).post(requestBody).build()).execute();
    }

    public static void uploadFile(String str, RequestBody requestBody, Callback callback) throws IOException {
        okHttpClient.newCall(new Request.Builder().url(str).post(requestBody).build()).enqueue(callback);
    }
}
