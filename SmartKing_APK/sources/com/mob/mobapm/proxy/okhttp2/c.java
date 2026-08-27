package com.mob.mobapm.proxy.okhttp2;

import com.mob.mobapm.bean.TransactionType;
import com.mob.mobapm.core.Transaction;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.io.IOException;

/* loaded from: classes.dex */
public class c extends com.mob.mobapm.c.a {
    public static Response a(Transaction transaction, Response response) {
        int code;
        com.mob.mobapm.d.a.a().i("APM: OkHttp2 request end, transaction switch is " + com.mob.mobapm.core.c.e, new Object[0]);
        if (com.mob.mobapm.core.c.e && transaction != null && transaction.isCreate()) {
            String str = null;
            if (response == null) {
                code = 500;
            } else {
                code = response.code();
                if (code != 200) {
                    try {
                        ResponseBody a = d.a(response.body(), 2147483647L);
                        transaction.setErrMsg(a == null ? "" : a.string());
                    } catch (IOException e) {
                        com.mob.mobapm.d.a.a().i("APM: OkHttp2 request end error: " + e, new Object[0]);
                    }
                }
                Request request = response.request();
                if (request != null) {
                    str = request.method();
                }
            }
            com.mob.mobapm.c.a.a(transaction, str, code);
        }
        return response;
    }

    public static void a(Transaction transaction, Request request) {
        com.mob.mobapm.d.a.a().i("APM: OkHttp2 request start, transaction switch is " + com.mob.mobapm.core.c.e, new Object[0]);
        if (!com.mob.mobapm.core.c.e || transaction == null || request == null) {
            return;
        }
        String host = request.url().getHost();
        String path = request.url().getPath();
        String protocol = request.url().getProtocol();
        transaction.setMethod(request.method());
        com.mob.mobapm.c.a.a(transaction, host, path, TransactionType.valueOf(protocol));
    }
}
