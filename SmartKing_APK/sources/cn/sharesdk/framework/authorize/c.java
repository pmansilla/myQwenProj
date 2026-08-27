package cn.sharesdk.framework.authorize;

import android.content.Intent;

/* compiled from: SSOAuthorizeActivity.java */
/* loaded from: classes.dex */
public class c extends a {
    protected SSOListener b;
    private d c;

    public void a(SSOListener sSOListener) {
        this.b = sSOListener;
    }

    @Override // com.mob.tools.FakeActivity
    public void onActivityResult(int i, int i2, Intent intent) {
        this.c.a(i, i2, intent);
    }

    @Override // com.mob.tools.FakeActivity
    public void onCreate() {
        this.c = this.a.getSSOProcessor(this);
        if (this.c != null) {
            this.c.a(32973);
            this.c.a();
            return;
        }
        finish();
        AuthorizeListener authorizeListener = this.a.getAuthorizeListener();
        if (authorizeListener != null) {
            authorizeListener.onError(new Throwable("Failed to start SSO for " + this.a.getPlatform().getName()));
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onNewIntent(Intent intent) {
        this.c.a(intent);
    }
}
