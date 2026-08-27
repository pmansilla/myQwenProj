package com.mob.mobapm.proxy.okhttp3;

import com.mob.mobapm.core.Transaction;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Timeout;

/* loaded from: classes.dex */
public class a implements Call {
    private Transaction a;
    private Request b;
    private Call c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(OkHttpClient okHttpClient, Request request, Call call, Transaction transaction) {
        this.b = request;
        this.c = call;
        this.a = transaction;
    }

    private Response a(Response response) {
        return this.a.getTransStatus() < 2 ? c.a(b(), response) : response;
    }

    public Call a() {
        return this.c;
    }

    protected void a(Exception exc) {
        com.mob.mobapm.c.a.a(b(), exc);
    }

    protected Transaction b() {
        if (this.a == null) {
            this.a = new Transaction();
        }
        c.a(this.a, this.b);
        return this.a;
    }

    @Override // okhttp3.Call
    public void cancel() {
        this.c.cancel();
    }

    public Call clone() {
        return this.c.clone();
    }

    @Override // okhttp3.Call
    public void enqueue(Callback callback) {
        b();
        this.c.enqueue(new b(callback, this.a));
    }

    @Override // okhttp3.Call
    public Response execute() throws IOException {
        b();
        try {
            return a(this.c.execute());
        } catch (IOException e) {
            a(e);
            throw e;
        }
    }

    @Override // okhttp3.Call
    public boolean isCanceled() {
        return this.c.isCanceled();
    }

    @Override // okhttp3.Call
    public boolean isExecuted() {
        return false;
    }

    @Override // okhttp3.Call
    public Request request() {
        return this.c.request();
    }

    public Timeout timeout() {
        return this.c.timeout();
    }
}
