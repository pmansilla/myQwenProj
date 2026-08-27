package com.mob.mobapm.proxy.okhttp3;

import com.mob.mobapm.bean.TransactionType;
import com.mob.mobapm.core.Transaction;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

/* loaded from: classes.dex */
public class c extends com.mob.mobapm.c.a {
    public static Response a(Transaction transaction, Response response) {
        int code;
        com.mob.mobapm.d.a.a().i("APM: request end, transaction switch is " + transaction.isCreate(), new Object[0]);
        if (com.mob.mobapm.core.c.e && transaction != null && transaction.isCreate()) {
            String str = null;
            if (response == null) {
                code = 500;
            } else {
                try {
                    code = response.code();
                    if (code != 200) {
                        try {
                            transaction.setErrMsg(response.peekBody(2147483647L).string());
                        } catch (Throwable th) {
                            com.mob.mobapm.d.a.a().i("APM: OKHttp3 request end error： " + th, new Object[0]);
                        }
                    }
                    Request request = response.request();
                    if (request != null) {
                        str = request.method();
                    }
                } catch (Throwable th2) {
                    com.mob.mobapm.d.a.a().i("APM: OKHttp3 request end error： " + th2, new Object[0]);
                }
            }
            com.mob.mobapm.c.a.a(transaction, str, code);
        }
        return response;
    }

    public static void a(Transaction transaction, Request request) {
        com.mob.mobapm.d.a.a().i("APM: request start, switch is " + com.mob.mobapm.core.c.e, new Object[0]);
        if (!com.mob.mobapm.core.c.e || transaction == null || request == null) {
            return;
        }
        try {
            HttpUrl url = request.url();
            String host = url.host();
            String encodedPath = url.encodedPath();
            String str = url.isHttps() ? "https" : "http";
            transaction.setMethod(request.method());
            com.mob.mobapm.c.a.a(transaction, host, encodedPath, TransactionType.valueOf(str));
        } catch (Throwable th) {
            com.mob.mobapm.d.a.a().i("APM: OkHttp3 request start error: " + th, new Object[0]);
        }
    }
}
