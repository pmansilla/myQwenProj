package com.mob.mobapm.proxy.okhttp3;

import okhttp3.Handshake;
import okhttp3.Headers;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/* loaded from: classes.dex */
public class e extends Response.Builder {
    private Response.Builder a;

    public e(Response.Builder builder) {
        this.a = builder;
    }

    @Override // okhttp3.Response.Builder
    public Response.Builder addHeader(String str, String str2) {
        return this.a.addHeader(str, str2);
    }

    @Override // okhttp3.Response.Builder
    public Response.Builder body(ResponseBody responseBody) {
        return this.a.body(responseBody);
    }

    @Override // okhttp3.Response.Builder
    public Response build() {
        return this.a.build();
    }

    @Override // okhttp3.Response.Builder
    public Response.Builder cacheResponse(Response response) {
        return this.a.cacheResponse(response);
    }

    @Override // okhttp3.Response.Builder
    public Response.Builder code(int i) {
        return this.a.code(i);
    }

    @Override // okhttp3.Response.Builder
    public Response.Builder handshake(Handshake handshake) {
        return this.a.handshake(handshake);
    }

    @Override // okhttp3.Response.Builder
    public Response.Builder header(String str, String str2) {
        return this.a.header(str, str2);
    }

    @Override // okhttp3.Response.Builder
    public Response.Builder headers(Headers headers) {
        return this.a.headers(headers);
    }

    @Override // okhttp3.Response.Builder
    public Response.Builder message(String str) {
        return this.a.message(str);
    }

    @Override // okhttp3.Response.Builder
    public Response.Builder networkResponse(Response response) {
        return this.a.networkResponse(response);
    }

    @Override // okhttp3.Response.Builder
    public Response.Builder priorResponse(Response response) {
        return this.a.priorResponse(response);
    }

    @Override // okhttp3.Response.Builder
    public Response.Builder protocol(Protocol protocol) {
        return this.a.protocol(protocol);
    }

    @Override // okhttp3.Response.Builder
    public Response.Builder removeHeader(String str) {
        return this.a.removeHeader(str);
    }

    @Override // okhttp3.Response.Builder
    public Response.Builder request(Request request) {
        return this.a.request(request);
    }
}
