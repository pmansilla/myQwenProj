package com.mob.mobapm.proxy.d;

import com.mob.mobapm.core.Transaction;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

@Deprecated
/* loaded from: classes.dex */
public final class c<T> implements ResponseHandler<T> {
    private final ResponseHandler<T> a;
    private final Transaction b;

    private c(ResponseHandler<T> responseHandler, Transaction transaction) {
        this.a = responseHandler;
        this.b = transaction;
    }

    public static <T> ResponseHandler<? extends T> a(ResponseHandler<? extends T> responseHandler, Transaction transaction) {
        return new c(responseHandler, transaction);
    }

    @Override // org.apache.http.client.ResponseHandler
    public T handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
        b.a(this.b, httpResponse);
        return this.a.handleResponse(httpResponse);
    }
}
