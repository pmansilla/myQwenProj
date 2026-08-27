package cn.sharesdk.sina.weibo;

import android.net.Uri;
import android.os.Bundle;
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
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import com.mob.tools.FakeActivity;
import com.mob.tools.RxMob;
import com.mob.tools.network.KVPair;
import com.mob.tools.utils.ResHelper;
import com.sun.mail.imap.IMAPStore;
import java.util.ArrayList;
import org.json.JSONObject;

/* compiled from: WebAuthPage.java */
/* loaded from: classes.dex */
public class d extends FakeActivity implements View.OnClickListener {
    private AuthorizeListener a;
    private String b;
    private String c;
    private String d;
    private int e;
    private cn.sharesdk.sina.weibo.sdk.a.a f;
    private LinearLayout g;
    private TextView h;
    private WebView i;
    private Button j;

    private void a() {
        this.h = this.f.b();
        this.i = this.f.c();
        this.g = this.f.d();
        this.j = this.f.a();
        this.h.setOnClickListener(this);
        this.j.setOnClickListener(this);
        if (ShareSDK.isRemoveCookieOnAuthorize()) {
            CookieSyncManager.createInstance(this.activity);
            CookieManager.getInstance().removeAllCookie();
        }
        this.i.setWebViewClient(new WebViewClient() { // from class: cn.sharesdk.sina.weibo.d.1
            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView webView, String str) {
                if (d.this.e == -1) {
                    d.this.g.setVisibility(0);
                    d.this.i.setVisibility(8);
                }
                d.this.e = 0;
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
                    d.this.e = -1;
                }
            }

            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                if (!str.startsWith(d.this.c)) {
                    return false;
                }
                if (d.this.a == null) {
                    return true;
                }
                d.this.a(str);
                return true;
            }
        });
        c();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str) {
        Bundle urlToBundle = ResHelper.urlToBundle(str);
        if (urlToBundle.containsKey("access_token")) {
            this.a.onComplete(urlToBundle);
        } else if (urlToBundle.containsKey("error")) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("error_uri:", urlToBundle.containsKey("error_uri") ? urlToBundle.getString("error_uri") : "");
                jSONObject.put("error:", urlToBundle.containsKey("error") ? urlToBundle.getString("error") : "");
                jSONObject.put("error_description:", urlToBundle.containsKey("error_description") ? urlToBundle.getString("error_description") : "");
                jSONObject.put("error_code:", urlToBundle.containsKey("error_code") ? urlToBundle.getString("error_code") : "");
                this.a.onError(new Throwable(jSONObject.toString()));
            } catch (Throwable th) {
                cn.sharesdk.framework.utils.e.b().e(th);
                this.a.onError(th);
            }
        }
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String b() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new KVPair("client_id", this.b));
        arrayList.add(new KVPair("response_type", "code"));
        arrayList.add(new KVPair("redirect_uri", this.c));
        arrayList.add(new KVPair("packagename", this.activity.getPackageName()));
        arrayList.add(new KVPair("response_type", "code"));
        arrayList.add(new KVPair("luicode", "10000360"));
        if (this.d != null && !TextUtils.isEmpty(this.d)) {
            arrayList.add(new KVPair("trans_token", this.d));
            arrayList.add(new KVPair("trans_access_token", this.d));
        }
        arrayList.add(new KVPair("key_hash", cn.sharesdk.sina.weibo.sdk.a.a(getContext(), this.activity.getPackageName())));
        arrayList.add(new KVPair(IMAPStore.ID_VERSION, "0041005000"));
        arrayList.add(new KVPair("scope", "email,direct_messages_read,direct_messages_write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog,invitation_write"));
        arrayList.add(new KVPair("display", "mobile"));
        return "https://open.weibo.cn/oauth2/authorize?" + ResHelper.encodeUrl((ArrayList<KVPair<String>>) arrayList);
    }

    private String c() {
        RxMob.Subscribable create = RxMob.create(new RxMob.OnSubscribe<String>() { // from class: cn.sharesdk.sina.weibo.d.2
            @Override // com.mob.tools.RxMob.OnSubscribe
            public void call(RxMob.Subscriber<String> subscriber) {
                d.this.i.loadUrl(d.this.b());
                subscriber.onCompleted();
            }
        });
        create.subscribeOn(RxMob.Thread.UI_THREAD);
        create.observeOn(RxMob.Thread.UI_THREAD);
        create.subscribe(new RxMob.Subscriber<String>() { // from class: cn.sharesdk.sina.weibo.d.3
            @Override // com.mob.tools.RxMob.Subscriber
            public void onCompleted() {
                super.onCompleted();
            }

            @Override // com.mob.tools.RxMob.Subscriber
            public void onError(Throwable th) {
                cn.sharesdk.framework.utils.e.b().d(th);
            }
        });
        return null;
    }

    public void a(AuthorizeListener authorizeListener) {
        this.a = authorizeListener;
    }

    public void a(String str, String str2, String str3) {
        this.b = str;
        this.c = str2;
        this.d = str3;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view == this.h && this.a != null) {
            this.a.onCancel();
            finish();
        } else if (view == this.j) {
            this.g.setVisibility(8);
            this.i.setVisibility(0);
            c();
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onCreate() {
        this.f = new cn.sharesdk.sina.weibo.sdk.a.a(getContext());
        this.activity.setContentView(this.f.a(ResHelper.getStringRes(getContext(), "ssdk_sina_web_login_title")));
        a();
    }

    @Override // com.mob.tools.FakeActivity
    public void onDestroy() {
        if (this.i != null) {
            this.i.setFocusable(false);
        }
    }

    @Override // com.mob.tools.FakeActivity
    public boolean onFinish() {
        if (this.a != null) {
            this.a = null;
        }
        return super.onFinish();
    }

    @Override // com.mob.tools.FakeActivity
    public boolean onKeyEvent(int i, KeyEvent keyEvent) {
        if (i != 4 || keyEvent.getAction() != 0) {
            return false;
        }
        if (this.a != null) {
            this.a.onCancel();
        }
        finish();
        return true;
    }
}
