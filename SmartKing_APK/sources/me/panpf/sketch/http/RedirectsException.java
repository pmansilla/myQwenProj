package me.panpf.sketch.http;

import android.support.annotation.NonNull;

/* loaded from: classes2.dex */
public class RedirectsException extends Exception {
    private String newUrl;

    public RedirectsException(@NonNull String str) {
        this.newUrl = str;
    }

    @NonNull
    public String getNewUrl() {
        return this.newUrl;
    }
}
