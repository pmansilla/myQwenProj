package cn.sharesdk.twitter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import cn.sharesdk.framework.authorize.f;
import cn.sharesdk.framework.utils.e;
import com.mob.tools.utils.ResHelper;

/* compiled from: TwitterAuthorizeWebviewClient.java */
/* loaded from: classes.dex */
public class b extends cn.sharesdk.framework.authorize.b {
    private boolean a;

    public b(f fVar) {
        super(fVar);
    }

    @Override // cn.sharesdk.framework.authorize.b
    public void onComplete(String str) {
        if (this.a) {
            return;
        }
        this.a = true;
        String c = c.a(this.activity.a().getPlatform()).c(str);
        if (c == null || c.length() <= 0) {
            if (this.listener != null) {
                this.listener.onError(new Throwable());
                return;
            }
            return;
        }
        String[] split = c.split("&");
        Bundle bundle = new Bundle();
        for (String str2 : split) {
            if (str2 != null) {
                String[] split2 = str2.split("=");
                if (split2.length >= 2) {
                    bundle.putString(split2[0], split2[1]);
                }
            }
        }
        if (bundle.size() <= 0) {
            if (this.listener != null) {
                this.listener.onError(new Throwable());
            }
        } else if (this.listener != null) {
            this.listener.onComplete(bundle);
        }
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [cn.sharesdk.twitter.b$2] */
    @Override // cn.sharesdk.framework.d, android.webkit.WebViewClient
    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        if (this.redirectUri != null && str.startsWith(this.redirectUri)) {
            webView.stopLoading();
            this.activity.finish();
            final String valueOf = String.valueOf(ResHelper.urlToBundle(str).get("oauth_verifier"));
            new Thread() { // from class: cn.sharesdk.twitter.b.2
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    try {
                        b.this.onComplete(valueOf);
                    } catch (Throwable th) {
                        e.b().d(th);
                    }
                }
            }.start();
        }
        super.onPageStarted(webView, str, bitmap);
    }

    /* JADX WARN: Type inference failed for: r3v2, types: [cn.sharesdk.twitter.b$1] */
    @Override // cn.sharesdk.framework.d, android.webkit.WebViewClient
    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        if (this.redirectUri == null || !str.startsWith(this.redirectUri)) {
            return super.shouldOverrideUrlLoading(webView, str);
        }
        webView.stopLoading();
        this.activity.finish();
        final String valueOf = String.valueOf(ResHelper.urlToBundle(str).get("oauth_verifier"));
        new Thread() { // from class: cn.sharesdk.twitter.b.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    b.this.onComplete(valueOf);
                } catch (Throwable th) {
                    e.b().d(th);
                }
            }
        }.start();
        return true;
    }
}
