package cn.sharesdk.framework.authorize;

import android.webkit.WebView;

/* compiled from: AuthorizeWebviewClient.java */
/* loaded from: classes.dex */
public abstract class b extends cn.sharesdk.framework.d {
    protected f activity;
    protected AuthorizeListener listener;
    protected String redirectUri;

    public b(f fVar) {
        this.activity = fVar;
        AuthorizeHelper a = fVar.a();
        this.redirectUri = a.getRedirectUri();
        this.listener = a.getAuthorizeListener();
    }

    protected abstract void onComplete(String str);

    @Override // cn.sharesdk.framework.d, android.webkit.WebViewClient
    public void onReceivedError(WebView webView, int i, String str, String str2) {
        webView.stopLoading();
        AuthorizeListener authorizeListener = this.activity.a().getAuthorizeListener();
        this.activity.finish();
        if (authorizeListener != null) {
            authorizeListener.onError(new Throwable(str + " (" + i + "): " + str2));
        }
    }
}
