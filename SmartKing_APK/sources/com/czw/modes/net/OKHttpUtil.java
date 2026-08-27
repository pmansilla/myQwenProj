package com.czw.modes.net;

import android.text.TextUtils;
import com.czw.utils.LogUtil;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;
import no.nordicsemi.android.dfu.DfuBaseService;

/* loaded from: classes.dex */
public class OKHttpUtil {
    private static OkHttpClient client = new OkHttpClient();
    private static OKHttpUtil httpUtil = new OKHttpUtil();

    /* loaded from: classes.dex */
    public interface InitCallback {
    }

    /* loaded from: classes.dex */
    public static class Param {
        public int id;
        public String name;
        public String value;

        public Param() {
        }

        public Param(String str, String str2) {
            this.name = str;
            this.value = str2;
        }
    }

    private OKHttpUtil() {
        client.setConnectTimeout(15L, TimeUnit.SECONDS);
        client.setWriteTimeout(20L, TimeUnit.SECONDS);
        client.setReadTimeout(10L, TimeUnit.SECONDS);
    }

    private Request createMultipartFormRequest(String str, String str2, File[] fileArr, String[] strArr, Param... paramArr) {
        Param[] validateParam = validateParam(paramArr);
        MultipartBuilder type = new MultipartBuilder().type(MultipartBuilder.FORM);
        for (Param param : validateParam) {
            type.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.name + "\""), RequestBody.create((MediaType) null, param.value));
        }
        if (fileArr != null) {
            int length = fileArr.length;
            for (int i = 0; i < length; i++) {
                File file = fileArr[i];
                String name = file.getName();
                type.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + strArr[i] + "\"; filename=\"" + name + "\""), RequestBody.create(MediaType.parse(guessMimeType(name)), file));
            }
        }
        return new Request.Builder().url(str).addHeader("cookie", str2).post(type.build()).build();
    }

    private Request createPostParam(String str, Param[] paramArr) {
        if (paramArr == null) {
            paramArr = new Param[0];
        }
        StringBuilder sb = new StringBuilder();
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        for (Param param : paramArr) {
            if (param != null && !TextUtils.isEmpty(param.name) && !TextUtils.isEmpty(param.value)) {
                formEncodingBuilder.add(param.name, param.value);
                sb.append(param.name);
                sb.append("=");
                sb.append(param.value);
            }
        }
        RequestBody build = formEncodingBuilder.build();
        LogUtil.e("请求:" + str + ":{" + sb.toString() + "}");
        return new Request.Builder().url(str).post(build).build();
    }

    private Request.Builder createRequest(String str) {
        return new Request.Builder().url(str);
    }

    private Request.Builder createRequest(String str, String str2) {
        return createRequest(str).addHeader("cookie", str2);
    }

    public static OKHttpUtil getInstance() {
        return httpUtil;
    }

    private String guessMimeType(String str) {
        String contentTypeFor = URLConnection.getFileNameMap().getContentTypeFor(str);
        return contentTypeFor == null ? DfuBaseService.MIME_TYPE_OCTET_STREAM : contentTypeFor;
    }

    private Param[] validateParam(Param... paramArr) {
        return paramArr == null ? new Param[0] : paramArr;
    }

    public void asyncGetRequest(String str, Callback callback) {
        client.newCall(createRequest(str).build()).enqueue(callback);
    }

    public void asyncGetRequest(String str, String str2, Callback callback) {
        client.newCall(createRequest(str, str2).build()).enqueue(callback);
    }

    public void asyncPostRequest(String str, Callback callback, Param... paramArr) {
        client.newCall(createPostParam(str, paramArr)).enqueue(callback);
    }

    public void asyncUpload(Callback callback, String str, File file, String str2) throws IOException {
        client.newCall(createMultipartFormRequest(str, null, new File[]{file}, new String[]{str2}, new Param[0])).enqueue(callback);
    }

    public void asyncUpload(Callback callback, String str, String str2, File file, String str3, Param... paramArr) throws IOException {
        client.newCall(createMultipartFormRequest(str, str2, new File[]{file}, new String[]{str3}, paramArr)).enqueue(callback);
    }

    public void asyncUpload(Callback callback, String str, String str2, File[] fileArr, String[] strArr, Param... paramArr) throws IOException {
        client.newCall(createMultipartFormRequest(str, str2, fileArr, strArr, paramArr)).enqueue(callback);
    }

    public void downLoad(String str, Callback callback) {
        asyncGetRequest(str, callback);
    }

    public byte[] getBytes(String str) throws IOException {
        return request(str).body().bytes();
    }

    public InputStream getStream(String str) throws IOException {
        return request(str).body().byteStream();
    }

    public String getString(String str) throws IOException {
        return request(str).body().string();
    }

    public byte[] postBytes(String str, Param... paramArr) throws IOException {
        return request(str, paramArr).body().bytes();
    }

    public InputStream postStream(String str, Param... paramArr) throws IOException {
        return request(str, paramArr).body().byteStream();
    }

    public String postString(String str, Param... paramArr) throws IOException {
        return request(str, paramArr).body().toString();
    }

    public Response request(String str) throws IOException {
        return client.newCall(createRequest(str).build()).execute();
    }

    public Response request(String str, Param... paramArr) throws IOException {
        return client.newCall(createPostParam(str, paramArr)).execute();
    }

    public Response upload(String str, File file, String str2) throws IOException {
        return client.newCall(createMultipartFormRequest(str, null, new File[]{file}, new String[]{str2}, new Param[0])).execute();
    }

    public Response upload(String str, File file, String str2, Param... paramArr) throws IOException {
        return client.newCall(createMultipartFormRequest(str, null, new File[]{file}, new String[]{str2}, paramArr)).execute();
    }

    public Response upload(String str, File[] fileArr, String[] strArr, Param... paramArr) throws IOException {
        return client.newCall(createMultipartFormRequest(str, null, fileArr, strArr, paramArr)).execute();
    }
}
