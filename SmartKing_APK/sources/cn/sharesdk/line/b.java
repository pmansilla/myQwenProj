package cn.sharesdk.line;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import cn.sharesdk.framework.authorize.SSOListener;
import cn.sharesdk.framework.authorize.d;
import cn.sharesdk.framework.authorize.f;
import cn.sharesdk.framework.utils.e;
import com.autonavi.amap.mapcore.AMapEngineUtils;
import com.liulishuo.filedownloader.model.ConnectionModel;
import com.luck.picture.lib.config.PictureConfig;
import com.mob.MobSDK;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import me.panpf.sketch.uri.FileUriModel;

/* compiled from: LineImpl.java */
/* loaded from: classes.dex */
public class b extends cn.sharesdk.framework.b {
    private static b b;
    private cn.sharesdk.framework.a.b c;
    private String d;
    private String e;
    private String f;
    private String g;

    private b(Platform platform) {
        super(platform);
        this.c = cn.sharesdk.framework.a.b.a();
    }

    public static b a(Platform platform) {
        if (b == null) {
            b = new b(platform);
        }
        return b;
    }

    private void b(String str, String str2) throws Throwable {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse("line://msg/" + str + FileUriModel.SCHEME + str2));
        intent.addFlags(AMapEngineUtils.MAX_P20_WIDTH);
        try {
            MobSDK.getContext().startActivity(intent);
        } catch (Throwable th) {
            e.b().d(th);
        }
    }

    private void g(String str) {
        HashMap fromJson = new Hashon().fromJson(str);
        String valueOf = String.valueOf(fromJson.get("mid"));
        String valueOf2 = String.valueOf(fromJson.get("expires_in"));
        String valueOf3 = String.valueOf(fromJson.get("access_token"));
        String valueOf4 = String.valueOf(fromJson.get("refresh_token"));
        String valueOf5 = String.valueOf(fromJson.get("token_type"));
        this.a.getDb().putUserId(valueOf);
        this.a.getDb().putExpiresIn(Long.valueOf(valueOf2).longValue());
        this.a.getDb().putToken(valueOf3);
        this.a.getDb().put("refresh_token", valueOf4);
        this.a.getDb().put("token_type", valueOf5);
    }

    public void a(final AuthorizeListener authorizeListener, boolean z) {
        if (z) {
            b(authorizeListener);
        } else {
            a(new SSOListener() { // from class: cn.sharesdk.line.b.1
                @Override // cn.sharesdk.framework.authorize.SSOListener
                public void onCancel() {
                    authorizeListener.onCancel();
                }

                @Override // cn.sharesdk.framework.authorize.SSOListener
                public void onComplete(Bundle bundle) {
                    authorizeListener.onComplete(bundle);
                }

                @Override // cn.sharesdk.framework.authorize.SSOListener
                public void onFailed(Throwable th) {
                    b.this.b(authorizeListener);
                }
            });
        }
    }

    public void a(String str) {
        this.f = str;
    }

    public void a(String str, String str2) {
        this.d = str;
        this.e = str2;
    }

    public boolean a() {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("refreshToken", this.a.getDb().get("refresh_token")));
        ArrayList<KVPair<String>> arrayList2 = new ArrayList<>();
        arrayList2.add(new KVPair<>("Content-Type", "application/x-www-form-urlencoded"));
        arrayList2.add(new KVPair<>("X-Line-ChannelToken", this.a.getDb().getToken()));
        try {
            String httpPost = this.c.httpPost("https://api.line.me/v1/oauth/accessToken", arrayList, (KVPair<String>) null, arrayList2, (NetworkHelper.NetworkTimeOut) null);
            if (TextUtils.isEmpty(httpPost) || httpPost.contains("error")) {
                return false;
            }
            g(httpPost);
            return true;
        } catch (Throwable th) {
            e.b().d(th);
            return false;
        }
    }

    public String b(String str) throws Throwable {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("code", str));
        arrayList.add(new KVPair<>("client_id", this.d));
        arrayList.add(new KVPair<>("client_secret", this.e));
        arrayList.add(new KVPair<>("redirect_uri", this.f));
        arrayList.add(new KVPair<>("grant_type", "authorization_code"));
        return this.c.b("https://api.line.me/v1/oauth/accessToken", arrayList, "/v1/oauth/accessToken", c());
    }

    public HashMap<String, Object> b() throws Throwable {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        ArrayList<KVPair<String>> arrayList2 = new ArrayList<>();
        arrayList2.add(new KVPair<>("Authorization", "Bearer " + this.g));
        String a = this.c.a("https://api.line.me/v1/profile", arrayList, arrayList2, (NetworkHelper.NetworkTimeOut) null, "/v1/profile", c());
        if (a != null) {
            return new Hashon().fromJson(a);
        }
        return null;
    }

    public boolean c(String str) {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("Authorization", "Bearer " + str));
        try {
            String httpGet = this.c.httpGet("https://api.line.me/v1/oauth/verify", null, arrayList, null);
            if (TextUtils.isEmpty(httpGet)) {
                return false;
            }
            return !httpGet.contains("error");
        } catch (Throwable th) {
            e.b().d(th);
            return false;
        }
    }

    public void d(String str) {
        this.g = str;
    }

    public boolean d() {
        try {
            return MobSDK.getContext().getPackageManager().getPackageInfo("jp.naver.line.android", 0) != null;
        } catch (Throwable th) {
            e.b().d(th);
            return false;
        }
    }

    public void e(String str) throws Throwable {
        b("text", Data.urlEncode(str, "utf-8"));
    }

    public void f(String str) throws Throwable {
        b(PictureConfig.IMAGE, str);
    }

    @Override // cn.sharesdk.framework.authorize.AuthorizeHelper
    public String getAuthorizeUrl() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new KVPair("channelId", this.d));
        arrayList.add(new KVPair("otpId", this.d));
        String language = Locale.getDefault().getLanguage();
        String country = Locale.getDefault().getCountry();
        String mcc = DeviceHelper.getInstance(MobSDK.getContext()).getMCC();
        if (TextUtils.isEmpty(language)) {
            language = "";
        } else if (language.equals("in")) {
            language = ConnectionModel.ID;
        } else if (language.equals("zh")) {
            if (country == null || Locale.SIMPLIFIED_CHINESE.getCountry().equals(country)) {
                language = language + "-Hans";
            } else {
                language = language + "-Hant";
            }
        }
        arrayList.add(new KVPair("lang", language));
        arrayList.add(new KVPair("country", country));
        arrayList.add(new KVPair("mcc", mcc));
        return "https://access.line.me/dialog/oauth/login?" + ResHelper.encodeUrl((ArrayList<KVPair<String>>) arrayList);
    }

    @Override // cn.sharesdk.framework.authorize.AuthorizeHelper
    public cn.sharesdk.framework.authorize.b getAuthorizeWebviewClient(f fVar) {
        return new a(fVar);
    }

    @Override // cn.sharesdk.framework.authorize.AuthorizeHelper
    public String getRedirectUri() {
        return this.f;
    }

    @Override // cn.sharesdk.framework.b, cn.sharesdk.framework.authorize.AuthorizeHelper
    public d getSSOProcessor(cn.sharesdk.framework.authorize.c cVar) {
        c cVar2 = new c(cVar);
        cVar2.a(this.d);
        return cVar2;
    }
}
