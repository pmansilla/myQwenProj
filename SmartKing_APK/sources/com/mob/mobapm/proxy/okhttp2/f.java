package com.mob.mobapm.proxy.okhttp2;

import com.squareup.okhttp.Handshake;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

/* loaded from: classes.dex */
public class f extends Response.Builder {
    private Response.Builder a;

    public f(Response.Builder builder) {
        this.a = builder;
    }

    @Override // com.squareup.okhttp.Response.Builder
    public Response.Builder addHeader(String str, String str2) {
        return this.a.addHeader(str, str2);
    }

    @Override // com.squareup.okhttp.Response.Builder
    public Response.Builder body(ResponseBody responseBody) {
        return this.a.body(responseBody);
    }

    @Override // com.squareup.okhttp.Response.Builder
    public Response build() {
        return this.a.build();
    }

    @Override // com.squareup.okhttp.Response.Builder
    public Response.Builder cacheResponse(Response response) {
        return this.a.cacheResponse(response);
    }

    @Override // com.squareup.okhttp.Response.Builder
    public Response.Builder code(int i) {
        return this.a.code(i);
    }

    @Override // com.squareup.okhttp.Response.Builder
    public Response.Builder handshake(Handshake handshake) {
        return this.a.handshake(handshake);
    }

    @Override // com.squareup.okhttp.Response.Builder
    public Response.Builder header(String str, String str2) {
        return this.a.header(str, str2);
    }

    @Override // com.squareup.okhttp.Response.Builder
    public Response.Builder headers(Headers headers) {
        return this.a.headers(headers);
    }

    @Override // com.squareup.okhttp.Response.Builder
    public Response.Builder message(String str) {
        return this.a.message(str);
    }

    @Override // com.squareup.okhttp.Response.Builder
    public Response.Builder networkResponse(Response response) {
        return this.a.networkResponse(response);
    }

    @Override // com.squareup.okhttp.Response.Builder
    public Response.Builder priorResponse(Response response) {
        return this.a.priorResponse(response);
    }

    @Override // com.squareup.okhttp.Response.Builder
    public Response.Builder protocol(Protocol protocol) {
        return this.a.protocol(protocol);
    }

    @Override // com.squareup.okhttp.Response.Builder
    public Response.Builder removeHeader(String str) {
        return this.a.removeHeader(str);
    }

    @Override // com.squareup.okhttp.Response.Builder
    public Response.Builder request(Request request) {
        return this.a.request(request);
    }
}
