package cn.sharesdk.wechat.utils;

import android.os.Bundle;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import com.amap.location.common.model.AmapLoc;
import com.mob.tools.network.KVPair;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import java.util.ArrayList;
import java.util.HashMap;

/* compiled from: WXAuthHelper.java */
/* loaded from: classes.dex */
public class g {
    private String a;
    private String b;
    private cn.sharesdk.framework.a.b c = cn.sharesdk.framework.a.b.a();
    private Platform d;
    private int e;

    public g(Platform platform, int i) {
        this.d = platform;
        this.e = i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str) {
        cn.sharesdk.framework.utils.e.b().d("wechat getAuthorizeToken ==>>" + str, new Object[0]);
        HashMap fromJson = new Hashon().fromJson(str);
        String valueOf = String.valueOf(fromJson.get("access_token"));
        String valueOf2 = String.valueOf(fromJson.get("refresh_token"));
        String valueOf3 = String.valueOf(fromJson.get("expires_in"));
        this.d.getDb().put("openid", String.valueOf(fromJson.get("openid")));
        this.d.getDb().putExpiresIn(Long.valueOf(valueOf3).longValue());
        this.d.getDb().putToken(valueOf);
        this.d.getDb().put("refresh_token", valueOf2);
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [cn.sharesdk.wechat.utils.g$1] */
    private void a(final String str, final AuthorizeListener authorizeListener) throws Throwable {
        cn.sharesdk.framework.utils.e.b().d("getAuthorizeToken ==>> " + str, new Object[0]);
        new Thread() { // from class: cn.sharesdk.wechat.utils.g.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    ArrayList<KVPair<String>> arrayList = new ArrayList<>();
                    arrayList.add(new KVPair<>("appid", g.this.a));
                    arrayList.add(new KVPair<>("secret", g.this.b));
                    arrayList.add(new KVPair<>("code", str));
                    arrayList.add(new KVPair<>("grant_type", "authorization_code"));
                    try {
                        String a = g.this.c.a("https://api.weixin.qq.com/sns/oauth2/access_token", arrayList, "/sns/oauth2/access_token", g.this.e);
                        if (TextUtils.isEmpty(a)) {
                            authorizeListener.onError(new Throwable("Authorize token is empty"));
                            return;
                        }
                        if (!a.contains("errcode")) {
                            g.this.a(a);
                            authorizeListener.onComplete(null);
                        } else if (authorizeListener != null) {
                            authorizeListener.onError(new Throwable(a));
                        }
                    } catch (Throwable th) {
                        authorizeListener.onError(th);
                    }
                } catch (Throwable th2) {
                    cn.sharesdk.framework.utils.e.b().d(th2);
                }
            }
        }.start();
    }

    public void a(Bundle bundle, AuthorizeListener authorizeListener) {
        String string = bundle.getString("_wxapi_sendauth_resp_url");
        if (TextUtils.isEmpty(string)) {
            if (authorizeListener != null) {
                authorizeListener.onError(null);
                return;
            }
            return;
        }
        int indexOf = string.indexOf("://oauth?");
        if (indexOf >= 0) {
            string = string.substring(indexOf + 1);
        }
        try {
            a(ResHelper.urlToBundle(string).getString("code"), authorizeListener);
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
            if (authorizeListener != null) {
                authorizeListener.onError(th);
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [cn.sharesdk.wechat.utils.g$2] */
    public void a(final PlatformActionListener platformActionListener) throws Throwable {
        new Thread() { // from class: cn.sharesdk.wechat.utils.g.2
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                int i;
                try {
                    ArrayList<KVPair<String>> arrayList = new ArrayList<>();
                    arrayList.add(new KVPair<>("access_token", g.this.d.getDb().getToken()));
                    arrayList.add(new KVPair<>("openid", g.this.d.getDb().get("openid")));
                    String a = g.this.c.a("https://api.weixin.qq.com/sns/userinfo", arrayList, "/sns/userinfo", g.this.e);
                    if (TextUtils.isEmpty(a)) {
                        if (platformActionListener != null) {
                            platformActionListener.onError(g.this.d, 8, new Throwable());
                            return;
                        }
                        return;
                    }
                    cn.sharesdk.framework.utils.e.b().d("getUserInfo ==>>" + a, new Object[0]);
                    HashMap<String, Object> fromJson = new Hashon().fromJson(a);
                    if (fromJson.containsKey("errcode") && ((Integer) fromJson.get("errcode")).intValue() != 0) {
                        if (platformActionListener != null) {
                            platformActionListener.onError(g.this.d, 8, new Throwable(new Hashon().fromHashMap(fromJson)));
                            return;
                        }
                        return;
                    }
                    String valueOf = String.valueOf(fromJson.get("openid"));
                    String valueOf2 = String.valueOf(fromJson.get("nickname"));
                    try {
                        i = ResHelper.parseInt(String.valueOf(fromJson.get("sex")));
                    } catch (Throwable th) {
                        cn.sharesdk.framework.utils.e.b().d(th);
                        i = 2;
                    }
                    String valueOf3 = String.valueOf(fromJson.get("province"));
                    String valueOf4 = String.valueOf(fromJson.get("city"));
                    String valueOf5 = String.valueOf(fromJson.get("country"));
                    String valueOf6 = String.valueOf(fromJson.get("headimgurl"));
                    String valueOf7 = String.valueOf(fromJson.get("unionid"));
                    g.this.d.getDb().put("nickname", valueOf2);
                    if (i == 1) {
                        g.this.d.getDb().put("gender", AmapLoc.RESULT_TYPE_GPS);
                    } else if (i == 2) {
                        g.this.d.getDb().put("gender", AmapLoc.RESULT_TYPE_WIFI_ONLY);
                    } else {
                        g.this.d.getDb().put("gender", AmapLoc.RESULT_TYPE_FUSED);
                    }
                    g.this.d.getDb().putUserId(valueOf);
                    g.this.d.getDb().put("icon", valueOf6);
                    g.this.d.getDb().put("province", valueOf3);
                    g.this.d.getDb().put("city", valueOf4);
                    g.this.d.getDb().put("country", valueOf5);
                    g.this.d.getDb().put("openid", valueOf);
                    g.this.d.getDb().put("unionid", valueOf7);
                    if (g.this.d.getDb().get("userTags") != null) {
                        fromJson.put("userTags", g.this.d.getDb().get("userTags"));
                    }
                    platformActionListener.onComplete(g.this.d, 8, fromJson);
                } catch (Throwable th2) {
                    cn.sharesdk.framework.utils.e.b().d(th2);
                }
            }
        }.start();
    }

    public void a(String str, String str2) {
        this.a = str;
        this.b = str2;
    }

    public boolean a() {
        String str = this.d.getDb().get("refresh_token");
        if (TextUtils.isEmpty(this.a) || TextUtils.isEmpty(str)) {
            return false;
        }
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("appid", this.a));
        arrayList.add(new KVPair<>("refresh_token", str));
        arrayList.add(new KVPair<>("grant_type", "refresh_token"));
        try {
            String a = this.c.a("https://api.weixin.qq.com/sns/oauth2/refresh_token", arrayList, "/sns/oauth2/refresh_token", this.e);
            if (TextUtils.isEmpty(a) || a.contains("errcode")) {
                return false;
            }
            a(a);
            return true;
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
            return false;
        }
    }
}
