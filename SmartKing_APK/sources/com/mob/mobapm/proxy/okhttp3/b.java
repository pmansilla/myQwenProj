package com.mob.mobapm.proxy.okhttp3;

import com.mob.mobapm.core.Transaction;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/* loaded from: classes.dex */
public class b implements Callback {
    private Transaction a;
    private Callback b;

    public b(Callback callback, Transaction transaction) {
        this.b = callback;
        this.a = transaction;
    }

    private Response a(Response response) {
        Transaction transaction = this.a;
        return (transaction == null || transaction.getTransStatus() >= 2) ? response : c.a(this.a, response);
    }

    protected Transaction a() {
        return this.a;
    }

    protected void a(Exception exc) {
        com.mob.mobapm.c.a.a(a(), exc);
    }

    @Override // okhttp3.Callback
    public void onFailure(Call call, IOException iOException) {
        a(iOException);
        this.b.onFailure(call, iOException);
    }

    @Override // okhttp3.Callback
    public void onResponse(Call call, Response response) throws IOException {
        this.b.onResponse(call, a(response));
    }
}
