package cn.sharesdk.facebook;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.authorize.RegisterView;
import com.mob.tools.FakeActivity;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.ResHelper;
import io.reactivex.annotations.SchedulerSupport;
import java.util.HashMap;

/* compiled from: WebShareActivity.java */
/* loaded from: classes.dex */
public class g extends FakeActivity {
    private String a;
    private PlatformActionListener b;
    private a c;
    private RegisterView d;
    private WebView e;
    private boolean f;
    private boolean g;

    private a b() {
        try {
            String string = this.activity.getPackageManager().getActivityInfo(this.activity.getComponentName(), 128).metaData.getString("FBWebShareAdapter");
            if (string != null && string.length() > 0) {
                Object newInstance = Class.forName(string).newInstance();
                if (newInstance instanceof a) {
                    return (a) newInstance;
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
    public void b(String str) {
        String str2 = str == null ? "" : new String(str);
        Bundle urlToBundle = ResHelper.urlToBundle(str);
        if (urlToBundle == null) {
            this.f = true;
            finish();
            this.b.onError(null, 0, new Throwable("failed to parse callback uri: " + str2));
            return;
        }
        String string = urlToBundle.getString("post_id");
        HashMap<String, Object> hashMap = new HashMap<>();
        if (!TextUtils.isEmpty(string)) {
            hashMap.put("post_id", string);
        }
        if (!urlToBundle.containsKey("error_code") && !urlToBundle.containsKey("error")) {
            this.g = true;
            finish();
            this.b.onComplete(null, 0, hashMap);
            return;
        }
        if (this.b != null) {
            String string2 = urlToBundle.getString("error_code");
            if (urlToBundle.containsKey("error_code") && string2.equals("4201")) {
                this.b.onCancel(null, 9);
            } else {
                this.b.onError(null, 9, new Throwable(ResHelper.encodeUrl(urlToBundle)));
            }
        }
        this.f = true;
        finish();
    }

    protected RegisterView a() {
        RegisterView registerView = new RegisterView(this.activity);
        registerView.c().getChildAt(registerView.c().getChildCount() - 1).setVisibility(8);
        registerView.a().setOnClickListener(new View.OnClickListener() { // from class: cn.sharesdk.facebook.g.1
            /* JADX WARN: Type inference failed for: r1v1, types: [cn.sharesdk.facebook.g$1$1] */
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                new Thread() { // from class: cn.sharesdk.facebook.g.1.1
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() {
                        try {
                            new Instrumentation().sendKeyDownUpSync(4);
                        } catch (Throwable th) {
                            cn.sharesdk.framework.utils.e.b().d(th);
                            g.this.finish();
                            g.this.b.onCancel(null, 0);
                        }
                    }
                }.start();
            }
        });
        this.e = registerView.b();
        WebSettings settings = this.e.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(1);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setSavePassword(false);
        settings.setDatabasePath(this.activity.getDir("database", 0).getPath());
        this.e.setVerticalScrollBarEnabled(false);
        this.e.setHorizontalScrollBarEnabled(false);
        this.e.setWebViewClient(new cn.sharesdk.framework.d() { // from class: cn.sharesdk.facebook.g.2
            @Override // cn.sharesdk.framework.d, android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                if (str != null) {
                    try {
                        if (str.startsWith("fbconnect://success")) {
                            g.this.b(str);
                        }
                    } catch (Exception e) {
                        cn.sharesdk.framework.utils.e.b().d(e.getMessage(), new Object[0]);
                    }
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

    @Override // com.mob.tools.FakeActivity
    public void onCreate() {
        this.d = a();
        try {
            int stringRes = ResHelper.getStringRes(getContext(), "ssdk_share_to_facebook");
            if (stringRes > 0) {
                this.d.c().getTvTitle().setText(stringRes);
            }
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
            this.d.c().setVisibility(8);
        }
        this.c.a(this.d.d());
        this.c.a(this.d.b());
        this.c.a(this.d.c());
        this.c.a();
        disableScreenCapture();
        this.activity.setContentView(this.d);
        if (!SchedulerSupport.NONE.equals(DeviceHelper.getInstance(this.activity).getDetailNetworkTypeForStatic())) {
            this.d.b().loadUrl(this.a);
            return;
        }
        this.f = true;
        finish();
        this.b.onError(null, 0, new Throwable("failed to load webpage, network disconnected."));
    }

    @Override // com.mob.tools.FakeActivity
    public void onDestroy() {
        if (!this.f && !this.g) {
            this.b.onCancel(null, 0);
        }
        if (this.e != null) {
            this.e.setFocusable(false);
        }
        if (this.c != null) {
            this.c.b();
        }
    }

    @Override // com.mob.tools.FakeActivity
    public boolean onFinish() {
        return this.c != null ? this.c.h() : super.onFinish();
    }

    @Override // com.mob.tools.FakeActivity
    public void onPause() {
        if (this.c != null) {
            this.c.d();
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onRestart() {
        if (this.c != null) {
            this.c.g();
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onResume() {
        if (this.c != null) {
            this.c.e();
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onStart() {
        if (this.c != null) {
            this.c.c();
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onStop() {
        if (this.c != null) {
            this.c.f();
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void setActivity(Activity activity) {
        super.setActivity(activity);
        if (this.c == null) {
            this.c = b();
            if (this.c == null) {
                this.c = new a();
            }
        }
        this.c.a(activity);
    }
}
