package cn.sharesdk.framework.authorize;

import android.content.Intent;

/* compiled from: SSOProcessor.java */
/* loaded from: classes.dex */
public abstract class d {
    protected c a;
    protected int b;
    protected SSOListener c;

    public d(c cVar) {
        this.a = cVar;
        this.c = cVar.a().getSSOListener();
    }

    public abstract void a();

    public void a(int i) {
        this.b = i;
    }

    public void a(int i, int i2, Intent intent) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a(Intent intent) {
    }
}
