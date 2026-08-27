package cn.sharesdk.tencent.qq;

import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import com.litesuits.orm.db.assit.SQLBuilder;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.HashMap;

/* compiled from: QQAuthorizeWebviewClient.java */
/* loaded from: classes.dex */
public class b extends cn.sharesdk.framework.authorize.b {
    public b(cn.sharesdk.framework.authorize.f fVar) {
        super(fVar);
    }

    private void a(HashMap<String, String> hashMap) {
        String str = hashMap.get("access_token");
        String str2 = hashMap.get("expires_in");
        String str3 = hashMap.get("error");
        String str4 = hashMap.get("error_description");
        String str5 = hashMap.get("pf");
        String str6 = hashMap.get("pfkey");
        String str7 = hashMap.get("pay_token");
        if (TextUtils.isEmpty(str)) {
            if (TextUtils.isEmpty(str3)) {
                this.listener.onError(new Throwable());
                return;
            }
            String str8 = str4 + " (" + str3 + SQLBuilder.PARENTHESES_RIGHT;
            if (this.listener != null) {
                this.listener.onError(new Throwable(str8));
                return;
            }
            return;
        }
        try {
            HashMap<String, Object> c = c.a(this.activity.a().getPlatform()).c(str);
            if (c != null && c.size() > 0) {
                if (!c.containsKey("openid")) {
                    if (this.listener != null) {
                        this.listener.onError(new Throwable());
                        return;
                    }
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("access_token", str);
                bundle.putString("open_id", String.valueOf(c.get("openid")));
                bundle.putString("expires_in", str2);
                bundle.putString("pf", str5);
                bundle.putString("pfkey", str6);
                bundle.putString("pay_token", str7);
                if (this.listener != null) {
                    this.listener.onComplete(bundle);
                    return;
                }
                return;
            }
            if (this.listener != null) {
                this.listener.onError(new Throwable());
            }
        } catch (Throwable th) {
            if (this.listener != null) {
                this.listener.onError(th);
            }
        }
    }

    @Override // cn.sharesdk.framework.authorize.b
    protected void onComplete(String str) {
        if (str.startsWith(this.redirectUri)) {
            str = str.substring(str.indexOf(35) + 1);
        }
        String[] split = str.split("&");
        HashMap<String, String> hashMap = new HashMap<>();
        for (String str2 : split) {
            String[] split2 = str2.split("=");
            if (split2.length < 2) {
                hashMap.put(URLDecoder.decode(split2[0]), "");
            } else {
                hashMap.put(URLDecoder.decode(split2[0]), URLDecoder.decode(split2[1] == null ? "" : split2[1]));
            }
        }
        a(hashMap);
    }

    @Override // cn.sharesdk.framework.d, android.webkit.WebViewClient
    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        try {
            Method method = sslErrorHandler.getClass().getMethod("proceed", new Class[0]);
            method.setAccessible(true);
            method.invoke(sslErrorHandler, new Object[0]);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /* JADX WARN: Type inference failed for: r3v2, types: [cn.sharesdk.tencent.qq.b$1] */
    @Override // cn.sharesdk.framework.d, android.webkit.WebViewClient
    public boolean shouldOverrideUrlLoading(WebView webView, final String str) {
        if (!str.startsWith(this.redirectUri)) {
            webView.loadUrl(str);
            return true;
        }
        webView.setVisibility(4);
        webView.stopLoading();
        this.activity.finish();
        new Thread() { // from class: cn.sharesdk.tencent.qq.b.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    b.this.onComplete(str);
                } catch (Throwable th) {
                    cn.sharesdk.framework.utils.e.b().d(th);
                }
            }
        }.start();
        return true;
    }
}
