package com.mob.mobapm.proxy.okhttp2;

import com.mob.mobapm.core.Transaction;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;

/* loaded from: classes.dex */
public class b implements Callback {
    private Callback a;
    private Transaction b;

    public b(Callback callback, Transaction transaction) {
        this.a = callback;
        this.b = transaction;
    }

    private Response a(Response response) {
        return this.b.getTransStatus() >= 2 ? response : c.a(this.b, response);
    }

    public void a(Exception exc) {
        com.mob.mobapm.c.a.a(this.b, exc);
    }

    @Override // com.squareup.okhttp.Callback
    public void onFailure(Request request, IOException iOException) {
        a(iOException);
        this.a.onFailure(request, iOException);
    }

    @Override // com.squareup.okhttp.Callback
    public void onResponse(Response response) throws IOException {
        this.a.onResponse(a(response));
    }
}
