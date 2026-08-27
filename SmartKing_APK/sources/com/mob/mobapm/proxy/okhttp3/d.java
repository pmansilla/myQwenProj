package com.mob.mobapm.proxy.okhttp3;

import java.net.URL;
import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;

/* loaded from: classes.dex */
public class d extends Request.Builder {
    private Request.Builder a;

    public d(Request.Builder builder) {
        this.a = builder;
    }

    @Override // okhttp3.Request.Builder
    public Request.Builder addHeader(String str, String str2) {
        return this.a.addHeader(str, str2);
    }

    @Override // okhttp3.Request.Builder
    public Request build() {
        return this.a.build();
    }

    @Override // okhttp3.Request.Builder
    public Request.Builder cacheControl(CacheControl cacheControl) {
        return this.a.cacheControl(cacheControl);
    }

    @Override // okhttp3.Request.Builder
    public Request.Builder delete() {
        return this.a.delete();
    }

    @Override // okhttp3.Request.Builder
    public Request.Builder get() {
        return this.a.get();
    }

    @Override // okhttp3.Request.Builder
    public Request.Builder head() {
        return this.a.head();
    }

    @Override // okhttp3.Request.Builder
    public Request.Builder header(String str, String str2) {
        return this.a.header(str, str2);
    }

    @Override // okhttp3.Request.Builder
    public Request.Builder headers(Headers headers) {
        return this.a.headers(headers);
    }

    @Override // okhttp3.Request.Builder
    public Request.Builder method(String str, RequestBody requestBody) {
        return this.a.method(str, requestBody);
    }

    @Override // okhttp3.Request.Builder
    public Request.Builder patch(RequestBody requestBody) {
        return this.a.patch(requestBody);
    }

    @Override // okhttp3.Request.Builder
    public Request.Builder post(RequestBody requestBody) {
        return this.a.post(requestBody);
    }

    @Override // okhttp3.Request.Builder
    public Request.Builder put(RequestBody requestBody) {
        return this.a.put(requestBody);
    }

    @Override // okhttp3.Request.Builder
    public Request.Builder removeHeader(String str) {
        return this.a.removeHeader(str);
    }

    @Override // okhttp3.Request.Builder
    public Request.Builder tag(Object obj) {
        return this.a.tag(obj);
    }

    @Override // okhttp3.Request.Builder
    public Request.Builder url(String str) {
        return this.a.url(str);
    }

    @Override // okhttp3.Request.Builder
    public Request.Builder url(URL url) {
        return this.a.url(url);
    }
}
