package com.mob.mobapm.proxy.okhttp2;

import com.mob.mobapm.core.Transaction;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;

/* loaded from: classes.dex */
public class a extends Call {
    private Call a;
    private Request b;
    private Transaction c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(OkHttpClient okHttpClient, Request request, Call call, Transaction transaction) {
        super(okHttpClient, request);
        this.b = request;
        this.a = call;
        this.c = transaction;
    }

    private Response a(Response response) {
        return this.c.getTransStatus() < 2 ? c.a(a(), response) : response;
    }

    public Transaction a() {
        if (this.c == null) {
            this.c = new Transaction();
        }
        c.a(this.c, this.b);
        return this.c;
    }

    public void a(Exception exc) {
        com.mob.mobapm.c.a.a(a(), exc);
    }

    @Override // com.squareup.okhttp.Call
    public void cancel() {
        this.a.cancel();
    }

    @Override // com.squareup.okhttp.Call
    public void enqueue(Callback callback) {
        a();
        this.a.enqueue(new b(callback, this.c));
    }

    @Override // com.squareup.okhttp.Call
    public Response execute() throws IOException {
        a();
        try {
            return a(this.a.execute());
        } catch (IOException e) {
            a(e);
            throw e;
        }
    }

    @Override // com.squareup.okhttp.Call
    public boolean isCanceled() {
        return this.a.isCanceled();
    }
}
