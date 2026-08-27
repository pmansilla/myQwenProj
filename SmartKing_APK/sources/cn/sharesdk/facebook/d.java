package cn.sharesdk.facebook;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import cn.sharesdk.framework.authorize.SSOListener;
import com.amap.location.common.model.AmapLoc;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.mob.MobSDK;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import me.panpf.sketch.uri.FileUriModel;

/* compiled from: FbHelper.java */
/* loaded from: classes.dex */
public class d extends cn.sharesdk.framework.b {
    private static final String[] b = {NotificationCompat.CATEGORY_EMAIL, "public_profile"};
    private static d c;
    private String d;
    private String e;
    private long f;
    private String g;
    private cn.sharesdk.framework.a.b h;
    private String[] i;
    private String j;

    private d(Platform platform) {
        super(platform);
        this.h = cn.sharesdk.framework.a.b.a();
    }

    public static d a(Platform platform) {
        if (c == null) {
            c = new d(platform);
        }
        return c;
    }

    public HashMap<String, Object> a(int i, int i2, String str) throws Throwable {
        String str2 = str != null ? str : "me";
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("access_token", this.e));
        arrayList.add(new KVPair<>("format", "json"));
        arrayList.add(new KVPair<>("limit", String.valueOf(i)));
        arrayList.add(new KVPair<>("offset", String.valueOf(i2)));
        arrayList.add(new KVPair<>("fields", "id,name,first_name,middle_name,last_name,gender,locale,languages,link,age_range,third_party_id,installed,timezone,updated_time,verified,birthday,cover,currency,devices,education,email,hometown,interested_in,location,political,payment_pricepoints,favorite_athletes,favorite_teams,picture,quotes,relationship_status,religion,security_settings,significant_other,video_upload_limits,website,work"));
        String a = this.h.a("https://graph.facebook.com/v3.2/" + str2 + (TextUtils.isEmpty(str) ? "/friends" : "/taggable_friends"), arrayList, "friends", c());
        if (a == null || a.length() <= 0) {
            return null;
        }
        return new Hashon().fromJson(a);
    }

    public HashMap<String, Object> a(String str, String str2, HashMap<String, Object> hashMap, HashMap<String, String> hashMap2) throws Throwable {
        KVPair<String> kVPair;
        if (str2 == null) {
            return null;
        }
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        if (hashMap != null && hashMap.size() > 0) {
            for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
                arrayList.add(new KVPair<>(entry.getKey(), String.valueOf(entry.getValue())));
            }
        }
        arrayList.add(new KVPair<>("access_token", this.e));
        arrayList.add(new KVPair<>("format", "json"));
        if (hashMap2 == null || hashMap2.size() <= 0) {
            kVPair = null;
        } else {
            KVPair<String> kVPair2 = null;
            for (Map.Entry<String, String> entry2 : hashMap2.entrySet()) {
                kVPair2 = new KVPair<>(entry2.getKey(), entry2.getValue());
            }
            kVPair = kVPair2;
        }
        String httpGet = "GET".equals(str2.toUpperCase()) ? this.h.httpGet(str, arrayList, null, null) : "POST".equals(str2.toUpperCase()) ? this.h.httpPost(str, arrayList, kVPair, (ArrayList<KVPair<String>>) null, (NetworkHelper.NetworkTimeOut) null) : null;
        if (httpGet == null || httpGet.length() <= 0) {
            return null;
        }
        return new Hashon().fromJson(httpGet);
    }

    public void a(Platform.ShareParams shareParams, PlatformActionListener platformActionListener) throws Throwable {
        String imageUrl = shareParams.getImageUrl();
        String title = shareParams.getTitle();
        String text = shareParams.getText();
        String musicUrl = shareParams.getMusicUrl();
        String url = shareParams.getUrl();
        String titleUrl = shareParams.getTitleUrl();
        if (!TextUtils.isEmpty(titleUrl)) {
            titleUrl = this.a.getShortLintk(titleUrl, false);
            shareParams.setTitleUrl(titleUrl);
        } else if (!TextUtils.isEmpty(url)) {
            titleUrl = this.a.getShortLintk(url, false);
            shareParams.setUrl(titleUrl);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("https://www.facebook.com/dialog/feed?");
        sb.append("app_id=");
        sb.append(this.g);
        sb.append("&redirect_uri=fbconnect://success");
        sb.append("&link=");
        sb.append(Data.urlEncode(titleUrl, "utf-8"));
        if (!TextUtils.isEmpty(imageUrl)) {
            sb.append("&picture=");
            sb.append(Data.urlEncode(imageUrl, "utf-8"));
        }
        if (!TextUtils.isEmpty(title)) {
            sb.append("&caption=");
            sb.append(Data.urlEncode(title, "utf-8"));
        }
        if (!TextUtils.isEmpty(text)) {
            sb.append("&description=");
            sb.append(Data.urlEncode(text, "utf-8"));
        }
        if (!TextUtils.isEmpty(musicUrl)) {
            sb.append("&source=");
            sb.append(Data.urlEncode(musicUrl, "utf-8"));
            if (!TextUtils.isEmpty(text)) {
                sb.append("&name=");
                sb.append(Data.urlEncode(text, "utf-8"));
            }
        }
        g gVar = new g();
        gVar.a(sb.toString());
        gVar.a(platformActionListener);
        gVar.show(MobSDK.getContext(), null);
    }

    public void a(PlatformActionListener platformActionListener, Platform.ShareParams shareParams) {
        Intent intent = new Intent();
        intent.putExtra("TITLE", shareParams.getTitle());
        f fVar = new f();
        fVar.a(platformActionListener, this.a, shareParams, this.g);
        fVar.show(MobSDK.getContext(), intent);
    }

    public void a(final AuthorizeListener authorizeListener, boolean z) {
        if (z) {
            b(authorizeListener);
        } else {
            a(new SSOListener() { // from class: cn.sharesdk.facebook.d.1
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
                    cn.sharesdk.framework.utils.e.b().d(th);
                    d.this.b(authorizeListener);
                }
            });
        }
    }

    public void a(String str) {
        this.g = str;
    }

    public void a(String str, String str2) {
        this.e = str;
        if (str2 == null || str2.equals(AmapLoc.RESULT_TYPE_GPS)) {
            return;
        }
        try {
            this.f = System.currentTimeMillis() + (Long.valueOf(str2).longValue() * 1000);
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
        }
    }

    public void a(String[] strArr) {
        this.i = strArr;
    }

    public boolean a() {
        return this.e != null && (this.f == 0 || System.currentTimeMillis() < this.f);
    }

    public HashMap<String, Object> b(String str) throws Throwable {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("access_token", this.e));
        arrayList.add(new KVPair<>("format", "json"));
        arrayList.add(new KVPair<>("message", str));
        String b2 = this.h.b("https://graph.facebook.com/v3.2/me/feed", arrayList, "/v3.2/me/feed", c());
        if (b2 == null || b2.length() <= 0) {
            return null;
        }
        return new Hashon().fromJson(b2);
    }

    public HashMap<String, Object> b(String str, String str2) throws Throwable {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("access_token", this.e));
        arrayList.add(new KVPair<>("format", "json"));
        arrayList.add(new KVPair<>("caption", str));
        String a = this.h.a("https://graph.facebook.com/v3.2/me/photos", arrayList, new KVPair<>(GlideExecutor.DEFAULT_SOURCE_EXECUTOR_NAME, str2), "/v3.2/me/photos", c());
        if (a == null || a.length() <= 0) {
            return null;
        }
        return new Hashon().fromJson(a);
    }

    public boolean b() {
        Intent intent = new Intent();
        intent.setClassName("com.facebook.katana", "com.facebook.katana.ProxyAuth");
        intent.putExtra("client_id", this.g);
        if (this.i != null && this.i.length > 0) {
            intent.putExtra("scope", TextUtils.join(",", this.i));
        }
        ResolveInfo resolveActivity = MobSDK.getContext().getPackageManager().resolveActivity(intent, 0);
        if (resolveActivity == null) {
            return false;
        }
        try {
            for (Signature signature : MobSDK.getContext().getPackageManager().getPackageInfo(resolveActivity.activityInfo.packageName, 64).signatures) {
                if ("30820268308201d102044a9c4610300d06092a864886f70d0101040500307a310b3009060355040613025553310b3009060355040813024341311230100603550407130950616c6f20416c746f31183016060355040a130f46616365626f6f6b204d6f62696c653111300f060355040b130846616365626f6f6b311d301b0603550403131446616365626f6f6b20436f72706f726174696f6e3020170d3039303833313231353231365a180f32303530303932353231353231365a307a310b3009060355040613025553310b3009060355040813024341311230100603550407130950616c6f20416c746f31183016060355040a130f46616365626f6f6b204d6f62696c653111300f060355040b130846616365626f6f6b311d301b0603550403131446616365626f6f6b20436f72706f726174696f6e30819f300d06092a864886f70d010101050003818d0030818902818100c207d51df8eb8c97d93ba0c8c1002c928fab00dc1b42fca5e66e99cc3023ed2d214d822bc59e8e35ddcf5f44c7ae8ade50d7e0c434f500e6c131f4a2834f987fc46406115de2018ebbb0d5a3c261bd97581ccfef76afc7135a6d59e8855ecd7eacc8f8737e794c60a761c536b72b11fac8e603f5da1a2d54aa103b8a13c0dbc10203010001300d06092a864886f70d0101040500038181005ee9be8bcbb250648d3b741290a82a1c9dc2e76a0af2f2228f1d9f9c4007529c446a70175c5a900d5141812866db46be6559e2141616483998211f4a673149fb2232a10d247663b26a9031e15f84bc1c74d141ff98a02d76f85b2c8ab2571b6469b232d8e768a7f7ca04f7abe4a775615916c07940656b58717457b42bd928a2".equals(signature.toCharsString())) {
                    return true;
                }
            }
            return false;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    public HashMap<String, Object> c(String str) throws Throwable {
        String str2 = "/me";
        if (str != null) {
            str2 = FileUriModel.SCHEME + str;
        }
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("access_token", this.e));
        arrayList.add(new KVPair<>("format", "json"));
        arrayList.add(new KVPair<>("fields", "id,name,first_name,middle_name,last_name,gender,locale,languages,link,age_range,third_party_id,installed,timezone,updated_time,verified,birthday,cover,currency,devices,education,email,hometown,interested_in,location,political,payment_pricepoints,favorite_athletes,favorite_teams,picture.type(large),quotes,relationship_status,religion,security_settings,significant_other,video_upload_limits,website,work"));
        String a = this.h.a("https://graph.facebook.com/v3.2" + str2, arrayList, "get_user_info", c());
        cn.sharesdk.framework.utils.e.b().i("facebook helper getUser", new Object[0]);
        if (a == null || a.length() <= 0) {
            return null;
        }
        return new Hashon().fromJson(a);
    }

    public void d(String str) {
        this.j = str;
    }

    @Override // cn.sharesdk.framework.authorize.AuthorizeHelper
    public String getAuthorizeUrl() {
        Bundle bundle = new Bundle();
        bundle.putString("display", "touch");
        bundle.putString("redirect_uri", this.j);
        bundle.putString("type", "user_agent");
        String[] strArr = this.i == null ? b : this.i;
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String str : strArr) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(str);
            i++;
        }
        bundle.putString("scope", sb.toString());
        bundle.putString("client_id", this.g);
        bundle.putString("response_type", "code");
        this.d = "https://www.facebook.com/dialog/oauth?" + ResHelper.encodeUrl(bundle);
        ShareSDK.logApiEvent("/dialog/oauth", c());
        return this.d;
    }

    @Override // cn.sharesdk.framework.authorize.AuthorizeHelper
    public cn.sharesdk.framework.authorize.b getAuthorizeWebviewClient(cn.sharesdk.framework.authorize.f fVar) {
        return new c(fVar);
    }

    @Override // cn.sharesdk.framework.authorize.AuthorizeHelper
    public String getRedirectUri() {
        return this.j;
    }

    @Override // cn.sharesdk.framework.b, cn.sharesdk.framework.authorize.AuthorizeHelper
    public cn.sharesdk.framework.authorize.d getSSOProcessor(cn.sharesdk.framework.authorize.c cVar) {
        b bVar = new b(cVar);
        bVar.a(32525);
        bVar.a(this.g, this.i == null ? b : this.i);
        return bVar;
    }
}
