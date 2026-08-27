package com.mob.mobapm.proxy.okhttp2;

import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import java.net.URL;

/* loaded from: classes.dex */
public class e extends Request.Builder {
    private Request.Builder a;

    public e(Request.Builder builder) {
        this.a = builder;
    }

    @Override // com.squareup.okhttp.Request.Builder
    public Request.Builder addHeader(String str, String str2) {
        return this.a.addHeader(str, str2);
    }

    @Override // com.squareup.okhttp.Request.Builder
    public Request build() {
        return this.a.build();
    }

    @Override // com.squareup.okhttp.Request.Builder
    public Request.Builder cacheControl(CacheControl cacheControl) {
        return this.a.cacheControl(cacheControl);
    }

    @Override // com.squareup.okhttp.Request.Builder
    public Request.Builder delete() {
        return this.a.delete();
    }

    @Override // com.squareup.okhttp.Request.Builder
    public Request.Builder get() {
        return this.a.get();
    }

    @Override // com.squareup.okhttp.Request.Builder
    public Request.Builder head() {
        return this.a.head();
    }

    @Override // com.squareup.okhttp.Request.Builder
    public Request.Builder header(String str, String str2) {
        return this.a.header(str, str2);
    }

    @Override // com.squareup.okhttp.Request.Builder
    public Request.Builder headers(Headers headers) {
        return this.a.headers(headers);
    }

    @Override // com.squareup.okhttp.Request.Builder
    public Request.Builder method(String str, RequestBody requestBody) {
        return this.a.method(str, requestBody);
    }

    @Override // com.squareup.okhttp.Request.Builder
    public Request.Builder patch(RequestBody requestBody) {
        return this.a.patch(requestBody);
    }

    @Override // com.squareup.okhttp.Request.Builder
    public Request.Builder post(RequestBody requestBody) {
        return this.a.post(requestBody);
    }

    @Override // com.squareup.okhttp.Request.Builder
    public Request.Builder put(RequestBody requestBody) {
        return this.a.put(requestBody);
    }

    @Override // com.squareup.okhttp.Request.Builder
    public Request.Builder removeHeader(String str) {
        return this.a.removeHeader(str);
    }

    @Override // com.squareup.okhttp.Request.Builder
    public Request.Builder tag(Object obj) {
        return this.a.tag(obj);
    }

    @Override // com.squareup.okhttp.Request.Builder
    public Request.Builder url(String str) {
        return this.a.url(str);
    }

    @Override // com.squareup.okhttp.Request.Builder
    public Request.Builder url(URL url) {
        return this.a.url(url);
    }
}
