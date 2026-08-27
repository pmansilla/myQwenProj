package cn.sharesdk.line;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.authorize.f;
import cn.sharesdk.framework.utils.e;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import java.util.HashMap;

/* compiled from: LineAuthorizeWebviewClient.java */
/* loaded from: classes.dex */
public class a extends cn.sharesdk.framework.authorize.b {
    private boolean a;

    public a(f fVar) {
        super(fVar);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [cn.sharesdk.line.a$1] */
    private void a(final Platform platform, final String str) {
        new Thread() { // from class: cn.sharesdk.line.a.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                String str2;
                try {
                    try {
                        str2 = b.a(platform).b(str);
                    } catch (Throwable th) {
                        a.this.listener.onError(th);
                        str2 = null;
                    }
                    if (str2 == null) {
                        a.this.listener.onError(new Throwable("Authorize token is empty"));
                        return;
                    }
                    HashMap fromJson = new Hashon().fromJson(str2);
                    Bundle bundle = new Bundle();
                    bundle.putString("mid", String.valueOf(fromJson.get("mid")));
                    bundle.putString("access_token", String.valueOf(fromJson.get("access_token")));
                    bundle.putString("refresh_token", String.valueOf(fromJson.get("refresh_token")));
                    bundle.putString("expires_in", String.valueOf(fromJson.get("expires_in")));
                    bundle.putString("token_type", String.valueOf(fromJson.get("token_type")));
                    a.this.listener.onComplete(bundle);
                } catch (Throwable th2) {
                    e.b().d(th2);
                }
            }
        }.start();
    }

    @Override // cn.sharesdk.framework.authorize.b
    protected void onComplete(String str) {
        if (this.a) {
            return;
        }
        this.a = true;
        Bundle urlToBundle = ResHelper.urlToBundle(str);
        String string = urlToBundle.getString("errorMessage");
        if (string != null && this.listener != null) {
            this.listener.onError(new Throwable(urlToBundle.toString()));
        }
        if (string != null || this.listener == null) {
            return;
        }
        String string2 = urlToBundle.getString("requestToken");
        if (TextUtils.isEmpty(string2)) {
            this.listener.onError(new Throwable(str));
        } else {
            a(this.activity.a().getPlatform(), string2);
        }
    }

    @Override // cn.sharesdk.framework.d, android.webkit.WebViewClient
    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        if (!str.startsWith("lineconnect://")) {
            return super.shouldOverrideUrlLoading(webView, str);
        }
        webView.stopLoading();
        this.activity.finish();
        onComplete(str);
        return true;
    }
}
