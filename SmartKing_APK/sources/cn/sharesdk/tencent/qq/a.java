package cn.sharesdk.tencent.qq;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import cn.sharesdk.framework.authorize.SSOListener;
import com.mob.tools.FakeActivity;
import java.lang.reflect.Method;

/* compiled from: DownLoadWebPage.java */
/* loaded from: classes.dex */
public class a extends FakeActivity {
    private LinearLayout a;
    private WebView b;
    private String c = "http://qzs.qq.com/open/mobile/login/qzsjump.html?sdkv=3.3.0.lite&display=mobile";
    private String d = "http://app.qq.com/detail/com.tencent.mobileqq?autodownload=1&norecommend=1&rootvia=opensdk";
    private SSOListener e;

    private void a(Activity activity) {
        b();
        this.b.setWebViewClient(new WebViewClient() { // from class: cn.sharesdk.tencent.qq.a.1
            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                if (str != null) {
                    try {
                        if (str.startsWith("download://")) {
                            a.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(a.this.d)));
                            return true;
                        }
                    } catch (Throwable th) {
                        cn.sharesdk.framework.utils.e.b().d(th);
                        a.this.e.onFailed(th);
                        a.this.e = null;
                        a.this.a();
                        return false;
                    }
                }
                return false;
            }
        });
        this.b.loadUrl(this.c);
    }

    private void b() {
        this.a = new LinearLayout(getContext());
        this.a.setOrientation(1);
        new LinearLayout.LayoutParams(-1, -1);
        this.b = new WebView(getContext());
        this.a.addView(this.b, new LinearLayout.LayoutParams(-1, 0, 11.0f));
        c();
    }

    private void c() {
        if (Build.VERSION.SDK_INT > 10 && Build.VERSION.SDK_INT < 17) {
            try {
                Method method = this.b.getClass().getMethod("removeJavascriptInterface", String.class);
                method.setAccessible(true);
                method.invoke(this.b, "searchBoxJavaBridge_");
                method.invoke(this.b, "accessibility");
                method.invoke(this.b, "accessibilityTraversal");
            } catch (Throwable th) {
                cn.sharesdk.framework.utils.e.b().d(th);
            }
        }
        WebSettings settings = this.b.getSettings();
        settings.setCacheMode(2);
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        this.b.setVerticalScrollBarEnabled(false);
        this.b.setHorizontalScrollBarEnabled(false);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }

    public void a() {
        finish();
    }

    public void a(SSOListener sSOListener) {
        this.e = sSOListener;
    }

    @Override // com.mob.tools.FakeActivity
    public void onCreate() {
        this.activity.getWindow().setBackgroundDrawable(new ColorDrawable(-1));
        a(this.activity);
        this.activity.setContentView(this.a);
    }

    @Override // com.mob.tools.FakeActivity
    public boolean onKeyEvent(int i, KeyEvent keyEvent) {
        if (i != 4 || keyEvent.getAction() != 1) {
            return super.onKeyEvent(i, keyEvent);
        }
        if (this.b == null || !this.b.canGoBack()) {
            this.e.onCancel();
            this.e = null;
            a();
        } else {
            this.b.goBack();
        }
        return true;
    }
}
