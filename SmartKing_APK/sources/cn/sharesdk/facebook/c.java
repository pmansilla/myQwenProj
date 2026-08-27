package cn.sharesdk.facebook;

import android.os.Bundle;
import android.webkit.WebView;
import com.mob.tools.utils.ResHelper;

/* compiled from: FacebookWebViewClient.java */
/* loaded from: classes.dex */
public class c extends cn.sharesdk.framework.authorize.b {
    public c(cn.sharesdk.framework.authorize.f fVar) {
        super(fVar);
    }

    @Override // cn.sharesdk.framework.authorize.b
    protected void onComplete(String str) {
        int i;
        Bundle urlToBundle = ResHelper.urlToBundle(str);
        String string = urlToBundle.getString("error_message");
        if (string != null && this.listener != null) {
            string = "error_message ==>>" + string + "\nerror_code ==>>" + urlToBundle.getString("error_code");
            this.listener.onError(new Throwable(str));
        }
        if (string == null) {
            String string2 = urlToBundle.getString("access_token");
            String string3 = urlToBundle.containsKey("expires_in") ? urlToBundle.getString("expires_in") : "-1";
            if (this.listener != null) {
                Bundle bundle = new Bundle();
                bundle.putString("oauth_token", string2);
                bundle.putString("oauth_token_secret", "");
                try {
                    i = ResHelper.parseInt(string3);
                } catch (Throwable th) {
                    cn.sharesdk.framework.utils.e.b().d(th);
                    i = -1;
                }
                bundle.putInt("oauth_token_expires", i);
                this.listener.onComplete(bundle);
            }
        }
    }

    @Override // cn.sharesdk.framework.d, android.webkit.WebViewClient
    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        try {
            if (this.redirectUri != null && str.startsWith(this.redirectUri)) {
                webView.stopLoading();
                webView.postDelayed(new Runnable() { // from class: cn.sharesdk.facebook.c.1
                    @Override // java.lang.Runnable
                    public void run() {
                        c.this.activity.finish();
                    }
                }, 500L);
                onComplete(str);
                return true;
            }
        } catch (Exception e) {
            cn.sharesdk.framework.utils.e.b().e(e.getMessage(), new Object[0]);
        }
        return super.shouldOverrideUrlLoading(webView, str);
    }
}
