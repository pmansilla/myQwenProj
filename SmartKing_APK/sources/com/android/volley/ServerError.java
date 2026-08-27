package com.android.volley;

/* loaded from: classes.dex */
public class ServerError extends VolleyError {
    public ServerError() {
    }

    public ServerError(NetworkResponse networkResponse) {
        super(networkResponse);
    }
}
