package com.czw.net;

import com.squareup.okhttp.Request;
import java.io.IOException;

/* loaded from: classes.dex */
public abstract class NetRespListener<R> {
    public void onFailure(Request request, IOException iOException) {
    }

    public abstract void onResponse(R r);
}
