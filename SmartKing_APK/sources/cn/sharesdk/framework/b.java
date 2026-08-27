package cn.sharesdk.framework;

import cn.sharesdk.framework.authorize.AuthorizeHelper;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import cn.sharesdk.framework.authorize.SSOListener;

/* compiled from: PlatformHelper.java */
/* loaded from: classes.dex */
public abstract class b implements AuthorizeHelper {
    protected Platform a;
    private AuthorizeListener b;
    private SSOListener c;

    public b(Platform platform) {
        this.a = platform;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a(SSOListener sSOListener) {
        this.c = sSOListener;
        cn.sharesdk.framework.authorize.c cVar = new cn.sharesdk.framework.authorize.c();
        cVar.a(sSOListener);
        cVar.a(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void b(AuthorizeListener authorizeListener) {
        this.b = authorizeListener;
        cn.sharesdk.framework.authorize.f fVar = new cn.sharesdk.framework.authorize.f();
        fVar.a(this.b);
        fVar.a(this);
    }

    public int c() {
        return this.a.getPlatformId();
    }

    @Override // cn.sharesdk.framework.authorize.AuthorizeHelper
    public AuthorizeListener getAuthorizeListener() {
        return this.b;
    }

    @Override // cn.sharesdk.framework.authorize.AuthorizeHelper
    public Platform getPlatform() {
        return this.a;
    }

    @Override // cn.sharesdk.framework.authorize.AuthorizeHelper
    public SSOListener getSSOListener() {
        return this.c;
    }

    @Override // cn.sharesdk.framework.authorize.AuthorizeHelper
    public cn.sharesdk.framework.authorize.d getSSOProcessor(cn.sharesdk.framework.authorize.c cVar) {
        return null;
    }
}
