package com.mob.mobapm.proxy;

import com.mob.mobapm.core.Transaction;
import com.mob.tools.proguard.ClassKeeper;
import java.io.IOException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

/* loaded from: classes.dex */
public class ApacheInstrumentation implements ClassKeeper {
    private static HttpRequest delegate(HttpHost httpHost, HttpRequest httpRequest, Transaction transaction) {
        return com.mob.mobapm.proxy.d.b.a(transaction, httpHost, httpRequest);
    }

    private static HttpResponse delegate(HttpResponse httpResponse, Transaction transaction) {
        return com.mob.mobapm.proxy.d.b.a(transaction, httpResponse);
    }

    private static <T> ResponseHandler<? extends T> delegate(ResponseHandler<? extends T> responseHandler, Transaction transaction) {
        return com.mob.mobapm.proxy.d.c.a(responseHandler, transaction);
    }

    private static HttpUriRequest delegate(HttpUriRequest httpUriRequest, Transaction transaction) {
        return com.mob.mobapm.proxy.d.b.a(transaction, httpUriRequest);
    }

    public static <T> T execute(HttpClient httpClient, HttpHost httpHost, HttpRequest httpRequest, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        if (!com.mob.mobapm.core.c.e) {
            return (T) httpClient.execute(httpHost, httpRequest, responseHandler);
        }
        Transaction transaction = new Transaction();
        try {
            return (T) httpClient.execute(httpHost, delegate(httpHost, httpRequest, transaction), delegate(responseHandler, transaction));
        } catch (Throwable th) {
            com.mob.mobapm.c.a.a(transaction, th);
            throw th;
        }
    }

    public static <T> T execute(HttpClient httpClient, HttpHost httpHost, HttpRequest httpRequest, ResponseHandler<? extends T> responseHandler, HttpContext httpContext) throws IOException, ClientProtocolException {
        if (!com.mob.mobapm.core.c.e) {
            return (T) httpClient.execute(httpHost, httpRequest, responseHandler, httpContext);
        }
        Transaction transaction = new Transaction();
        try {
            return (T) httpClient.execute(httpHost, delegate(httpHost, httpRequest, transaction), delegate(responseHandler, transaction), httpContext);
        } catch (Throwable th) {
            com.mob.mobapm.c.a.a(transaction, th);
            throw th;
        }
    }

    public static <T> T execute(HttpClient httpClient, HttpUriRequest httpUriRequest, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        if (!com.mob.mobapm.core.c.e) {
            return (T) httpClient.execute(httpUriRequest, responseHandler);
        }
        Transaction transaction = new Transaction();
        try {
            return (T) httpClient.execute(delegate(httpUriRequest, transaction), delegate(responseHandler, transaction));
        } catch (Throwable th) {
            com.mob.mobapm.c.a.a(transaction, th);
            throw th;
        }
    }

    public static <T> T execute(HttpClient httpClient, HttpUriRequest httpUriRequest, ResponseHandler<? extends T> responseHandler, HttpContext httpContext) throws IOException, ClientProtocolException {
        if (!com.mob.mobapm.core.c.e) {
            return (T) httpClient.execute(httpUriRequest, responseHandler, httpContext);
        }
        Transaction transaction = new Transaction();
        try {
            return (T) httpClient.execute(delegate(httpUriRequest, transaction), delegate(responseHandler, transaction), httpContext);
        } catch (Throwable th) {
            com.mob.mobapm.c.a.a(transaction, th);
            throw th;
        }
    }

    public static HttpResponse execute(HttpClient httpClient, HttpHost httpHost, HttpRequest httpRequest) throws IOException {
        if (!com.mob.mobapm.core.c.e) {
            return httpClient.execute(httpHost, httpRequest);
        }
        Transaction transaction = new Transaction();
        try {
            return delegate(httpClient.execute(httpHost, delegate(httpHost, httpRequest, transaction)), transaction);
        } catch (Throwable th) {
            com.mob.mobapm.c.a.a(transaction, th);
            throw th;
        }
    }

    public static HttpResponse execute(HttpClient httpClient, HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext) throws IOException {
        if (!com.mob.mobapm.core.c.e) {
            return httpClient.execute(httpHost, httpRequest, httpContext);
        }
        Transaction transaction = new Transaction();
        try {
            return delegate(httpClient.execute(httpHost, delegate(httpHost, httpRequest, transaction), httpContext), transaction);
        } catch (Throwable th) {
            com.mob.mobapm.c.a.a(transaction, th);
            throw th;
        }
    }

    public static HttpResponse execute(HttpClient httpClient, HttpUriRequest httpUriRequest) throws IOException {
        if (!com.mob.mobapm.core.c.e) {
            return httpClient.execute(httpUriRequest);
        }
        Transaction transaction = new Transaction();
        try {
            return delegate(httpClient.execute(delegate(httpUriRequest, transaction)), transaction);
        } catch (Throwable th) {
            com.mob.mobapm.c.a.a(transaction, th);
            throw th;
        }
    }

    public static HttpResponse execute(HttpClient httpClient, HttpUriRequest httpUriRequest, HttpContext httpContext) throws IOException {
        if (!com.mob.mobapm.core.c.e) {
            return httpClient.execute(httpUriRequest, httpContext);
        }
        Transaction transaction = new Transaction();
        try {
            return delegate(httpClient.execute(delegate(httpUriRequest, transaction), httpContext), transaction);
        } catch (Throwable th) {
            com.mob.mobapm.c.a.a(transaction, th);
            throw th;
        }
    }
}
