package cn.sharesdk.sina.weibo;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import com.amap.location.common.model.AmapLoc;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.mob.tools.FakeActivity;
import com.mob.tools.RxMob;
import com.mob.tools.network.KVPair;
import com.mob.tools.utils.ResHelper;
import com.sun.mail.imap.IMAPStore;
import java.util.ArrayList;

/* compiled from: WebSharePage.java */
/* loaded from: classes.dex */
public class e extends FakeActivity implements View.OnClickListener {
    private String a;
    private String b;
    private Platform.ShareParams c;
    private AuthorizeListener d;
    private cn.sharesdk.sina.weibo.sdk.a.a e;
    private LinearLayout f;
    private TextView g;
    private WebView h;
    private Button i;
    private int j;

    private void a() {
        this.g = this.e.b();
        this.h = this.e.c();
        this.f = this.e.d();
        this.i = this.e.a();
        this.g.setOnClickListener(this);
        this.i.setOnClickListener(this);
        if (ShareSDK.isRemoveCookieOnAuthorize()) {
            CookieSyncManager.createInstance(this.activity);
            CookieManager.getInstance().removeAllCookie();
        }
        this.h.setWebViewClient(new WebViewClient() { // from class: cn.sharesdk.sina.weibo.e.1
            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView webView, String str) {
                if (e.this.j == -1) {
                    e.this.f.setVisibility(0);
                    e.this.h.setVisibility(8);
                }
                e.this.j = 0;
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedError(WebView webView, int i, String str, String str2) {
                super.onReceivedError(webView, i, str, str2);
                String url = webView.getUrl();
                if (TextUtils.isEmpty(url) || TextUtils.isEmpty(str2)) {
                    return;
                }
                Uri parse = Uri.parse(url);
                Uri parse2 = Uri.parse(str2);
                if (parse.getHost().equals(parse2.getHost()) && parse.getScheme().equals(parse2.getScheme())) {
                    e.this.j = -1;
                }
            }

            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                if (!str.startsWith("sinaweibo://browser/close")) {
                    return false;
                }
                if (e.this.d == null) {
                    return true;
                }
                e.this.a(str);
                return true;
            }
        });
        b();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str) {
        Bundle urlToBundle = ResHelper.urlToBundle(str);
        String string = urlToBundle.getString("code");
        String string2 = urlToBundle.getString(NotificationCompat.CATEGORY_MESSAGE);
        if (this.d != null) {
            if (TextUtils.isEmpty(string)) {
                this.d.onCancel();
            } else if (AmapLoc.RESULT_TYPE_GPS.equals(string)) {
                this.d.onComplete(urlToBundle);
            } else {
                this.d.onError(new Throwable(string2));
            }
        }
        finish();
    }

    private String b() {
        RxMob.Subscribable create = RxMob.create(new RxMob.OnSubscribe<String>() { // from class: cn.sharesdk.sina.weibo.e.2
            @Override // com.mob.tools.RxMob.OnSubscribe
            public void call(RxMob.Subscriber<String> subscriber) {
                subscriber.onNext(e.this.c());
            }
        });
        create.subscribeOn(RxMob.Thread.NEW_THREAD);
        create.observeOn(RxMob.Thread.UI_THREAD);
        create.subscribe(new RxMob.Subscriber<String>() { // from class: cn.sharesdk.sina.weibo.e.3
            @Override // com.mob.tools.RxMob.Subscriber
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public void onNext(String str) {
                e.this.h.loadUrl(e.this.b(str));
            }

            @Override // com.mob.tools.RxMob.Subscriber
            public void onCompleted() {
                super.onCompleted();
            }

            @Override // com.mob.tools.RxMob.Subscriber
            public void onError(Throwable th) {
                cn.sharesdk.framework.utils.e.b().w(th);
                e.this.h.loadUrl(e.this.b((String) null));
            }

            @Override // com.mob.tools.RxMob.Subscriber
            public void onStart() {
                super.onStart();
            }
        });
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String b(String str) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new KVPair("title", this.c.getText()));
        arrayList.add(new KVPair(GlideExecutor.DEFAULT_SOURCE_EXECUTOR_NAME, this.a));
        arrayList.add(new KVPair("access_token", this.b));
        arrayList.add(new KVPair("packagename", this.activity.getPackageName()));
        arrayList.add(new KVPair("picinfo", str));
        arrayList.add(new KVPair("luicode", "10000360"));
        arrayList.add(new KVPair("key_hash", cn.sharesdk.sina.weibo.sdk.a.a(getContext(), this.activity.getPackageName())));
        arrayList.add(new KVPair("lfid", "OP_" + this.a));
        arrayList.add(new KVPair(IMAPStore.ID_VERSION, "0041005000"));
        return "http://service.weibo.com/share/mobilesdk.php?" + ResHelper.encodeUrl((ArrayList<KVPair<String>>) arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0062 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0063 A[Catch: Throwable -> 0x00fc, TryCatch #0 {Throwable -> 0x00fc, blocks: (B:3:0x0001, B:5:0x0009, B:7:0x0015, B:9:0x003e, B:11:0x004a, B:13:0x004f, B:14:0x005c, B:18:0x0063, B:20:0x0099, B:21:0x009e, B:23:0x00a6, B:24:0x00ab, B:27:0x00bb, B:29:0x00c6, B:31:0x00ce, B:34:0x00d7, B:36:0x00f3, B:44:0x0022, B:46:0x002e), top: B:2:0x0001 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.String c() {
        /*
            Method dump skipped, instructions count: 261
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.sharesdk.sina.weibo.e.c():java.lang.String");
    }

    public void a(Platform.ShareParams shareParams) {
        this.c = shareParams;
    }

    public void a(AuthorizeListener authorizeListener) {
        this.d = authorizeListener;
    }

    public void a(String str, String str2) {
        this.a = str;
        this.b = str2;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view == this.g) {
            if (this.d != null) {
                this.d.onCancel();
            }
            finish();
        } else if (view == this.i) {
            this.f.setVisibility(8);
            this.h.setVisibility(0);
            b();
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onCreate() {
        this.e = new cn.sharesdk.sina.weibo.sdk.a.a(getContext());
        this.activity.setContentView(this.e.a(ResHelper.getStringRes(getContext(), "ssdk_sina_web_title")));
        a();
    }

    @Override // com.mob.tools.FakeActivity
    public void onDestroy() {
        if (this.h != null) {
            this.h.setFocusable(false);
        }
    }

    @Override // com.mob.tools.FakeActivity
    public boolean onFinish() {
        if (this.d != null) {
            this.d = null;
        }
        return super.onFinish();
    }

    @Override // com.mob.tools.FakeActivity
    public boolean onKeyEvent(int i, KeyEvent keyEvent) {
        if (i != 4 || keyEvent.getAction() != 0) {
            return false;
        }
        if (this.d != null) {
            this.d.onCancel();
        }
        finish();
        return true;
    }
}
