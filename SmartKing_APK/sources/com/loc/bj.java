package com.loc;

import android.text.TextUtils;
import java.net.Proxy;
import java.util.Map;

/* compiled from: Request.java */
/* loaded from: classes.dex */
public abstract class bj {
    int c = 20000;
    int d = 20000;
    Proxy e = null;

    public final void a(int i) {
        this.c = i;
    }

    public final void a(Proxy proxy) {
        this.e = proxy;
    }

    public abstract Map<String, String> b();

    public final void b(int i) {
        this.d = i;
    }

    public abstract Map<String, String> b_();

    public abstract String c();

    public byte[] d() {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String j() {
        return "";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean k() {
        return !TextUtils.isEmpty(j());
    }

    public boolean l() {
        return false;
    }
}
