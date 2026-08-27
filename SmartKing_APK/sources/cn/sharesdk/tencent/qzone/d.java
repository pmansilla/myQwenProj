package cn.sharesdk.tencent.qzone;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.authorize.RegisterView;
import cn.sharesdk.framework.utils.e;
import com.mob.tools.FakeActivity;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import io.reactivex.annotations.SchedulerSupport;
import java.util.List;

/* compiled from: ShareActivity.java */
/* loaded from: classes.dex */
public class d extends FakeActivity {
    private String a;
    private boolean b;
    private PlatformActionListener c;
    private RegisterView d;
    private WebView e;
    private String f;
    private boolean g;
    private boolean h;
    private QZoneWebShareAdapter i;

    private QZoneWebShareAdapter b() {
        try {
            String string = this.activity.getPackageManager().getActivityInfo(this.activity.getComponentName(), 128).metaData.getString("QZoneWebShareAdapter");
            if (string != null && string.length() > 0) {
                Object newInstance = Class.forName(string).newInstance();
                if (newInstance instanceof QZoneWebShareAdapter) {
                    return (QZoneWebShareAdapter) newInstance;
                }
                return null;
            }
            return null;
        } catch (Throwable th) {
            e.b().d(th);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(String str) {
        String str2 = str == null ? "" : new String(str);
        Bundle urlToBundle = ResHelper.urlToBundle(str);
        if (urlToBundle == null) {
            this.h = true;
            finish();
            this.c.onError(null, 0, new Throwable("failed to parse callback uri: " + str2));
            return;
        }
        String string = urlToBundle.getString("action");
        if (!"share".equals(string) && !"shareToQzone".equals(string)) {
            this.h = true;
            finish();
            this.c.onError(null, 0, new Throwable("action error: " + str2));
            return;
        }
        String string2 = urlToBundle.getString("result");
        if ("cancel".equals(string2)) {
            finish();
            this.c.onCancel(null, 0);
            return;
        }
        if (!"complete".equals(string2)) {
            this.h = true;
            finish();
            this.c.onError(null, 0, new Throwable("operation failed: " + str2));
            return;
        }
        String string3 = urlToBundle.getString("response");
        if (!TextUtils.isEmpty(string3)) {
            this.g = true;
            finish();
            this.c.onComplete(null, 0, new Hashon().fromJson(string3));
            return;
        }
        this.h = true;
        finish();
        this.c.onError(null, 0, new Throwable("response empty" + str2));
    }

    private void c() {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(this.a));
            intent.putExtra("pkg_name", this.activity.getPackageName());
            this.activity.startActivityForResult(intent, 100);
        } catch (Throwable th) {
            if (this.c != null) {
                this.c.onError(null, 0, th);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(String str) {
        List<ResolveInfo> list;
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse(str));
        try {
            list = this.activity.getPackageManager().queryIntentActivities(intent, 1);
        } catch (Throwable th) {
            e.b().d(th);
            list = null;
        }
        if (list == null || list.size() <= 0) {
            return;
        }
        try {
            startActivity(intent);
        } catch (Throwable th2) {
            e.b().d(th2);
        }
    }

    private void d() {
        this.d = a();
        try {
            int stringRes = ResHelper.getStringRes(getContext(), "ssdk_share_to_qzone");
            if (stringRes > 0) {
                this.d.c().getTvTitle().setText(stringRes);
            }
        } catch (Throwable th) {
            e.b().d(th);
            this.d.c().setVisibility(8);
        }
        this.i.setBodyView(this.d.d());
        this.i.setWebView(this.d.b());
        this.i.setTitleView(this.d.c());
        this.i.onCreate();
        this.activity.setContentView(this.d);
        if (!SchedulerSupport.NONE.equals(DeviceHelper.getInstance(this.activity).getDetailNetworkTypeForStatic())) {
            this.d.b().loadUrl(this.a);
            return;
        }
        this.h = true;
        finish();
        this.c.onError(null, 0, new Throwable("failed to load webpage, network disconnected."));
    }

    protected RegisterView a() {
        RegisterView registerView = new RegisterView(this.activity);
        registerView.c().getChildAt(registerView.c().getChildCount() - 1).setVisibility(8);
        registerView.a().setOnClickListener(new View.OnClickListener() { // from class: cn.sharesdk.tencent.qzone.d.1
            /* JADX WARN: Type inference failed for: r1v1, types: [cn.sharesdk.tencent.qzone.d$1$1] */
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                new Thread() { // from class: cn.sharesdk.tencent.qzone.d.1.1
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() {
                        try {
                            new Instrumentation().sendKeyDownUpSync(4);
                        } catch (Throwable th) {
                            e.b().d(th);
                            d.this.finish();
                            d.this.c.onCancel(null, 0);
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
        settings.setDatabasePath(this.activity.getDir("database", 0).getPath());
        settings.setSavePassword(false);
        this.e.setVerticalScrollBarEnabled(false);
        this.e.setHorizontalScrollBarEnabled(false);
        this.e.setWebViewClient(new cn.sharesdk.framework.d() { // from class: cn.sharesdk.tencent.qzone.d.2
            @Override // cn.sharesdk.framework.d, android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                if (str != null && str.startsWith(d.this.f)) {
                    d.this.b(str);
                } else if (str != null && str.startsWith("mqzone://")) {
                    d.this.c(str);
                }
                return super.shouldOverrideUrlLoading(webView, str);
            }
        });
        return registerView;
    }

    public void a(PlatformActionListener platformActionListener) {
        this.c = platformActionListener;
    }

    public void a(String str) {
        this.f = "tencent" + str;
    }

    public void a(String str, boolean z) {
        this.a = str;
        this.b = z;
    }

    @Override // com.mob.tools.FakeActivity
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 100 && i2 == 0) {
            this.c.onCancel(null, 9);
        }
        finish();
    }

    @Override // com.mob.tools.FakeActivity
    public void onCreate() {
        try {
            try {
                Class<?> cls = Class.forName("cn.sharesdk.tencent.qq.ReceiveActivity");
                cls.getMethod("setUriScheme", String.class).invoke(null, this.f);
                cls.getMethod("setPlatformActionListener", PlatformActionListener.class).invoke(null, this.c);
                if (this.b) {
                    c();
                } else {
                    d();
                }
            } catch (Throwable unused) {
                ReceiveActivity.a(this.f);
                ReceiveActivity.a(this.c);
                if (this.b) {
                    c();
                } else {
                    d();
                }
            }
        } catch (Throwable th) {
            this.activity.finish();
            if (this.c != null) {
                this.c.onError(null, 9, th);
            }
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onDestroy() {
        if (!this.b && !this.h && !this.g) {
            this.c.onCancel(null, 0);
        }
        if (this.e != null) {
            this.e.setFocusable(false);
        }
        if (this.i != null) {
            this.i.onDestroy();
        }
    }

    @Override // com.mob.tools.FakeActivity
    public boolean onFinish() {
        return this.i != null ? this.i.onFinish() : super.onFinish();
    }

    @Override // com.mob.tools.FakeActivity
    public void onPause() {
        if (this.i != null) {
            this.i.onPause();
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onRestart() {
        if (this.i != null) {
            this.i.onRestart();
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onResume() {
        if (this.i != null) {
            this.i.onResume();
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onStart() {
        if (this.i != null) {
            this.i.onStart();
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onStop() {
        if (this.i != null) {
            this.i.onStop();
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void setActivity(Activity activity) {
        super.setActivity(activity);
        if (this.i == null) {
            this.i = b();
            if (this.i == null) {
                this.i = new QZoneWebShareAdapter();
            }
        }
        this.i.setActivity(activity);
    }
}
