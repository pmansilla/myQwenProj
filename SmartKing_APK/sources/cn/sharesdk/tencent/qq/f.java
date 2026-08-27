package cn.sharesdk.tencent.qq;

import android.app.Activity;
import android.app.Instrumentation;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.authorize.RegisterView;
import com.mob.tools.FakeActivity;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import io.reactivex.annotations.SchedulerSupport;

/* compiled from: WebShareActivity.java */
/* loaded from: classes.dex */
public class f extends FakeActivity {
    private String a;
    private PlatformActionListener b;
    private String c;
    private QQWebShareAdapter d;
    private RegisterView e;
    private WebView f;
    private boolean g;
    private boolean h;

    private QQWebShareAdapter b() {
        try {
            String string = this.activity.getPackageManager().getActivityInfo(this.activity.getComponentName(), 128).metaData.getString("QQWebShareAdapter");
            if (string != null && string.length() > 0) {
                Object newInstance = Class.forName(string).newInstance();
                if (newInstance instanceof QQWebShareAdapter) {
                    return (QQWebShareAdapter) newInstance;
                }
                return null;
            }
            return null;
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(String str) {
        String str2 = str == null ? "" : new String(str);
        Bundle urlToBundle = ResHelper.urlToBundle(str);
        if (urlToBundle == null) {
            this.g = true;
            finish();
            this.b.onError(null, 0, new Throwable("failed to parse callback uri: " + str2));
            return;
        }
        String string = urlToBundle.getString("action");
        if (!"share".equals(string) && !"shareToQQ".equals(string)) {
            this.g = true;
            finish();
            this.b.onError(null, 0, new Throwable("action error: " + str2));
            return;
        }
        String string2 = urlToBundle.getString("result");
        if ("cancel".equals(string2)) {
            finish();
            this.b.onCancel(null, 0);
            return;
        }
        if (!"complete".equals(string2)) {
            this.g = true;
            finish();
            this.b.onError(null, 0, new Throwable("operation failed: " + str2));
            return;
        }
        String string3 = urlToBundle.getString("response");
        if (!TextUtils.isEmpty(string3)) {
            this.h = true;
            finish();
            this.b.onComplete(null, 0, new Hashon().fromJson(string3));
            return;
        }
        this.g = true;
        finish();
        this.b.onError(null, 0, new Throwable("response empty" + str2));
    }

    protected RegisterView a() {
        RegisterView registerView = new RegisterView(this.activity);
        registerView.c().getChildAt(registerView.c().getChildCount() - 1).setVisibility(8);
        registerView.a().setOnClickListener(new View.OnClickListener() { // from class: cn.sharesdk.tencent.qq.f.1
            /* JADX WARN: Type inference failed for: r1v1, types: [cn.sharesdk.tencent.qq.f$1$1] */
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                new Thread() { // from class: cn.sharesdk.tencent.qq.f.1.1
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() {
                        try {
                            new Instrumentation().sendKeyDownUpSync(4);
                        } catch (Throwable th) {
                            cn.sharesdk.framework.utils.e.b().d(th);
                            f.this.finish();
                            f.this.b.onCancel(null, 0);
                        }
                    }
                }.start();
            }
        });
        this.f = registerView.b();
        WebSettings settings = this.f.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(1);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setSavePassword(false);
        settings.setDatabasePath(this.activity.getDir("database", 0).getPath());
        this.f.setVerticalScrollBarEnabled(false);
        this.f.setHorizontalScrollBarEnabled(false);
        this.f.setWebViewClient(new cn.sharesdk.framework.d() { // from class: cn.sharesdk.tencent.qq.f.2
            @Override // cn.sharesdk.framework.d, android.webkit.WebViewClient
            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                if (str == null || !str.startsWith("wtloginmqq://")) {
                    super.onPageStarted(webView, str, bitmap);
                    return;
                }
                int stringRes = ResHelper.getStringRes(f.this.activity, "ssdk_use_login_button");
                if (stringRes > 0) {
                    Toast.makeText(f.this.activity, stringRes, 0).show();
                }
            }

            @Override // cn.sharesdk.framework.d, android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                if (str != null && str.startsWith(f.this.c)) {
                    f.this.c(str);
                } else if (str != null && str.startsWith("http://www.myapp.com/down/")) {
                    f.this.h = true;
                } else if (str != null && str.startsWith("wtloginmqq://")) {
                    int stringRes = ResHelper.getStringRes(f.this.activity, "ssdk_use_login_button");
                    if (stringRes > 0) {
                        Toast.makeText(f.this.activity, stringRes, 0).show();
                    }
                    return true;
                }
                return super.shouldOverrideUrlLoading(webView, str);
            }
        });
        return registerView;
    }

    public void a(PlatformActionListener platformActionListener) {
        this.b = platformActionListener;
    }

    public void a(String str) {
        this.a = str;
    }

    public void b(String str) {
        this.c = "tencent" + str;
    }

    @Override // com.mob.tools.FakeActivity
    public void onCreate() {
        this.e = a();
        try {
            int stringRes = ResHelper.getStringRes(getContext(), "ssdk_share_to_qq");
            if (stringRes > 0) {
                this.e.c().getTvTitle().setText(stringRes);
            }
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
            this.e.c().setVisibility(8);
        }
        this.d.setBodyView(this.e.d());
        this.d.setWebView(this.e.b());
        this.d.setTitleView(this.e.c());
        this.d.onCreate();
        disableScreenCapture();
        this.activity.setContentView(this.e);
        if (!SchedulerSupport.NONE.equals(DeviceHelper.getInstance(this.activity).getDetailNetworkTypeForStatic())) {
            this.e.b().loadUrl(this.a);
            return;
        }
        this.g = true;
        finish();
        this.b.onError(null, 0, new Throwable("failed to load webpage, network disconnected."));
    }

    @Override // com.mob.tools.FakeActivity
    public void onDestroy() {
        if (!this.g && !this.h) {
            this.b.onCancel(null, 0);
        }
        if (this.f != null) {
            this.f.setFocusable(false);
        }
        if (this.d != null) {
            this.d.onDestroy();
        }
    }

    @Override // com.mob.tools.FakeActivity
    public boolean onFinish() {
        return this.d != null ? this.d.onFinish() : super.onFinish();
    }

    @Override // com.mob.tools.FakeActivity
    public void onPause() {
        if (this.d != null) {
            this.d.onPause();
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onRestart() {
        if (this.d != null) {
            this.d.onRestart();
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onResume() {
        if (this.d != null) {
            this.d.onResume();
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onStart() {
        if (this.d != null) {
            this.d.onStart();
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onStop() {
        if (this.d != null) {
            this.d.onStop();
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void setActivity(Activity activity) {
        super.setActivity(activity);
        if (this.d == null) {
            this.d = b();
            if (this.d == null) {
                this.d = new QQWebShareAdapter();
            }
        }
        this.d.setActivity(activity);
    }
}
