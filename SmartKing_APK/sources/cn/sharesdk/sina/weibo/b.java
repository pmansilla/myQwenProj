package cn.sharesdk.sina.weibo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import cn.sharesdk.framework.Platform;
import com.autonavi.amap.mapcore.AMapEngineUtils;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import java.util.HashMap;

/* compiled from: SinaWeiboAuthorizeWebviewClient.java */
/* loaded from: classes.dex */
public class b extends cn.sharesdk.framework.authorize.b {
    private boolean a;

    public b(cn.sharesdk.framework.authorize.f fVar) {
        super(fVar);
    }

    private Intent a(String str) {
        Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + str));
        intent.putExtra("sms_body", "");
        intent.setFlags(AMapEngineUtils.MAX_P20_WIDTH);
        return intent;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [cn.sharesdk.sina.weibo.b$1] */
    private void a(final Platform platform, final String str) {
        new Thread() { // from class: cn.sharesdk.sina.weibo.b.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                String str2;
                try {
                    try {
                        str2 = f.a(platform).b(str);
                    } catch (Throwable th) {
                        b.this.listener.onError(th);
                        str2 = null;
                    }
                    if (str2 == null) {
                        b.this.listener.onError(new Throwable("Authorize token is empty"));
                        return;
                    }
                    HashMap fromJson = new Hashon().fromJson(str2);
                    Bundle bundle = new Bundle();
                    bundle.putString("uid", String.valueOf(fromJson.get("uid")));
                    bundle.putString("remind_in", String.valueOf(fromJson.get("remind_in")));
                    bundle.putString("expires_in", String.valueOf(fromJson.get("expires_in")));
                    bundle.putString("access_token", String.valueOf(fromJson.get("access_token")));
                    b.this.listener.onComplete(bundle);
                } catch (Throwable th2) {
                    cn.sharesdk.framework.utils.e.b().d(th2);
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
        String string = urlToBundle.getString("error");
        String string2 = urlToBundle.getString("error_code");
        if (this.listener != null) {
            if (string == null && string2 == null) {
                String string3 = urlToBundle.getString("code");
                if (TextUtils.isEmpty(string3)) {
                    this.listener.onError(new Throwable("Authorize code is empty"));
                }
                a(this.activity.a().getPlatform(), string3);
                return;
            }
            if (string.equals("access_denied")) {
                this.listener.onCancel();
                return;
            }
            int i = 0;
            try {
                i = ResHelper.parseInt(string2);
            } catch (Throwable th) {
                cn.sharesdk.framework.utils.e.b().d(th);
            }
            this.listener.onError(new Throwable(string + " (" + i + SQLBuilder.PARENTHESES_RIGHT));
        }
    }

    @Override // cn.sharesdk.framework.d, android.webkit.WebViewClient
    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        if (!TextUtils.isEmpty(this.redirectUri) && str.startsWith(this.redirectUri)) {
            webView.stopLoading();
            this.activity.finish();
            onComplete(str);
            return;
        }
        if (!str.startsWith("sms:")) {
            super.onPageStarted(webView, str, bitmap);
            return;
        }
        String substring = str.substring(4);
        try {
            try {
                Intent a = a(substring);
                a.setPackage("com.android.mms");
                webView.getContext().startActivity(a);
            } catch (Throwable unused) {
                webView.getContext().startActivity(a(substring));
            }
        } catch (Throwable th) {
            if (this.listener != null) {
                this.listener.onError(th);
            }
        }
    }

    @Override // cn.sharesdk.framework.d, android.webkit.WebViewClient
    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        if (!TextUtils.isEmpty(this.redirectUri) && str.startsWith(this.redirectUri)) {
            webView.stopLoading();
            this.activity.finish();
            onComplete(str);
            return true;
        }
        if (!str.startsWith("sms:")) {
            return super.shouldOverrideUrlLoading(webView, str);
        }
        String substring = str.substring(4);
        try {
            try {
                Intent a = a(substring);
                a.setPackage("com.android.mms");
                webView.getContext().startActivity(a);
            } catch (Throwable unused) {
                webView.getContext().startActivity(a(substring));
            }
        } catch (Throwable th) {
            if (this.listener != null) {
                this.listener.onError(th);
            }
        }
        return true;
    }
}
