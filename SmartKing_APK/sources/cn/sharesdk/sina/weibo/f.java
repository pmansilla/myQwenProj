package cn.sharesdk.sina.weibo;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import cn.sharesdk.framework.authorize.SSOListener;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.mob.MobSDK;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.utils.BitmapHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* compiled from: Weibo.java */
/* loaded from: classes.dex */
public class f extends cn.sharesdk.framework.b {
    private static f b;
    private String c;
    private String d;
    private String e;
    private String f;
    private String[] g;
    private cn.sharesdk.framework.a.b h;

    private f(Platform platform) {
        super(platform);
        this.g = new String[]{"follow_app_official_microblog"};
        this.h = cn.sharesdk.framework.a.b.a();
    }

    public static synchronized f a(Platform platform) {
        f fVar;
        synchronized (f.class) {
            if (b == null) {
                b = new f(platform);
            }
            fVar = b;
        }
        return fVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(AuthorizeListener authorizeListener) {
        d dVar = new d();
        dVar.a(this.c, this.e, this.f);
        dVar.a(authorizeListener);
        dVar.show(MobSDK.getContext(), null);
    }

    public HashMap<String, Object> a(int i, int i2, String str) throws Throwable {
        boolean z;
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>(GlideExecutor.DEFAULT_SOURCE_EXECUTOR_NAME, this.c));
        try {
            ResHelper.parseLong(str);
            z = true;
        } catch (Throwable unused) {
            z = false;
        }
        if (z) {
            arrayList.add(new KVPair<>("uid", str));
        } else {
            arrayList.add(new KVPair<>("screen_name", str));
        }
        arrayList.add(new KVPair<>("count", String.valueOf(i)));
        arrayList.add(new KVPair<>("page", String.valueOf(i2)));
        String a = this.h.a("https://api.weibo.com/2/statuses/user_timeline.json", arrayList, "/2/statuses/user_timeline.json", c());
        if (a != null) {
            return new Hashon().fromJson(a);
        }
        return null;
    }

    public HashMap<String, Object> a(String str, String str2, HashMap<String, Object> hashMap, HashMap<String, String> hashMap2) {
        KVPair<String> kVPair;
        String str3;
        if (str2 == null) {
            return null;
        }
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        if (hashMap != null && hashMap.size() > 0) {
            for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
                arrayList.add(new KVPair<>(entry.getKey(), String.valueOf(entry.getValue())));
            }
        }
        arrayList.add(new KVPair<>(GlideExecutor.DEFAULT_SOURCE_EXECUTOR_NAME, this.c));
        if (this.f != null) {
            arrayList.add(new KVPair<>("access_token", this.f));
        }
        if (hashMap2 == null || hashMap2.size() <= 0) {
            kVPair = null;
        } else {
            KVPair<String> kVPair2 = null;
            for (Map.Entry<String, String> entry2 : hashMap2.entrySet()) {
                kVPair2 = new KVPair<>(entry2.getKey(), entry2.getValue());
            }
            kVPair = kVPair2;
        }
        try {
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
        }
        if ("GET".equals(str2.toUpperCase())) {
            str3 = new NetworkHelper().httpGet(str, arrayList, null, null);
        } else {
            if ("POST".equals(str2.toUpperCase())) {
                str3 = new NetworkHelper().httpPost(str, arrayList, kVPair, (ArrayList<KVPair<String>>) null, (NetworkHelper.NetworkTimeOut) null);
            }
            str3 = null;
        }
        if (str3 == null || str3.length() <= 0) {
            return null;
        }
        return new Hashon().fromJson(str3);
    }

    public void a(final Platform.ShareParams shareParams, final PlatformActionListener platformActionListener) {
        if (shareParams.getImageData() == null && TextUtils.isEmpty(shareParams.getImagePath()) && !TextUtils.isEmpty(shareParams.getImageUrl())) {
            try {
                File file = new File(BitmapHelper.downloadBitmap(MobSDK.getContext(), shareParams.getImageUrl()));
                if (file.exists()) {
                    shareParams.setImagePath(file.getAbsolutePath());
                }
            } catch (Throwable th) {
                cn.sharesdk.framework.utils.e.b().d(th);
            }
        }
        if (shareParams.getImageArray() != null && shareParams.getImageArray().length > 0) {
            try {
                List<String> asList = Arrays.asList(shareParams.getImageArray());
                String[] strArr = new String[asList.size()];
                int i = 0;
                for (String str : asList) {
                    if (str.startsWith("http")) {
                        str = BitmapHelper.downloadBitmap(MobSDK.getContext(), str);
                    }
                    File file2 = new File(str);
                    if (file2.exists() && str.startsWith("/data/")) {
                        File file3 = new File(ResHelper.getCachePath(MobSDK.getContext(), "images"), System.currentTimeMillis() + file2.getName());
                        String absolutePath = file3.getAbsolutePath();
                        file3.createNewFile();
                        if (ResHelper.copyFile(str, absolutePath)) {
                            str = file3.getAbsolutePath();
                        }
                    }
                    strArr[i] = str;
                    i++;
                }
                shareParams.setImageArray(strArr);
            } catch (Throwable th2) {
                cn.sharesdk.framework.utils.e.b().d(th2);
            }
        }
        String text = shareParams.getText();
        if (!TextUtils.isEmpty(text)) {
            shareParams.setText(getPlatform().getShortLintk(text, false));
        }
        AuthorizeListener authorizeListener = new AuthorizeListener() { // from class: cn.sharesdk.sina.weibo.f.2
            @Override // cn.sharesdk.framework.authorize.AuthorizeListener
            public void onCancel() {
                if (platformActionListener != null) {
                    platformActionListener.onCancel(f.this.a, 9);
                }
            }

            @Override // cn.sharesdk.framework.authorize.AuthorizeListener
            public void onComplete(Bundle bundle) {
                if (platformActionListener != null) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("ShareParams", shareParams);
                    platformActionListener.onComplete(f.this.a, 9, hashMap);
                }
            }

            @Override // cn.sharesdk.framework.authorize.AuthorizeListener
            public void onError(Throwable th3) {
                if (platformActionListener != null) {
                    platformActionListener.onError(f.this.a, 9, th3);
                }
            }
        };
        a aVar = new a();
        aVar.a(this.c);
        aVar.a(shareParams);
        aVar.a(authorizeListener);
        aVar.show(MobSDK.getContext(), null);
    }

    public void a(final AuthorizeListener authorizeListener, boolean z) {
        if (z) {
            a(authorizeListener);
        } else {
            a(new SSOListener() { // from class: cn.sharesdk.sina.weibo.f.1
                @Override // cn.sharesdk.framework.authorize.SSOListener
                public void onCancel() {
                    authorizeListener.onCancel();
                }

                @Override // cn.sharesdk.framework.authorize.SSOListener
                public void onComplete(Bundle bundle) {
                    try {
                        ResHelper.parseLong(bundle.getString("expires_in"));
                        authorizeListener.onComplete(bundle);
                    } catch (Throwable th) {
                        onFailed(th);
                    }
                }

                @Override // cn.sharesdk.framework.authorize.SSOListener
                public void onFailed(Throwable th) {
                    cn.sharesdk.framework.utils.e.b().d(th);
                    f.this.a(authorizeListener);
                }
            });
        }
    }

    public void a(String str) {
        this.e = str;
    }

    public void a(String str, String str2) {
        this.c = str;
        this.d = str2;
    }

    public void a(String[] strArr) {
        if (strArr == null || strArr.length <= 0) {
            return;
        }
        this.g = strArr;
    }

    public boolean a() {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("client_id", this.c));
        arrayList.add(new KVPair<>("client_secret", this.d));
        arrayList.add(new KVPair<>("redirect_uri", this.e));
        arrayList.add(new KVPair<>("grant_type", "refresh_token"));
        arrayList.add(new KVPair<>("refresh_token", this.a.getDb().get("refresh_token")));
        try {
            String b2 = this.h.b("https://api.weibo.com/oauth2/access_token", arrayList, "/oauth2/access_token", c());
            if (TextUtils.isEmpty(b2) || b2.contains("error") || b2.contains("error_code")) {
                return false;
            }
            HashMap fromJson = new Hashon().fromJson(b2);
            String valueOf = String.valueOf(fromJson.get("uid"));
            String valueOf2 = String.valueOf(fromJson.get("expires_in"));
            this.f = String.valueOf(fromJson.get("access_token"));
            String valueOf3 = String.valueOf(fromJson.get("refresh_token"));
            String valueOf4 = String.valueOf(fromJson.get("remind_in"));
            this.a.getDb().putUserId(valueOf);
            this.a.getDb().putExpiresIn(Long.valueOf(valueOf2).longValue());
            this.a.getDb().putToken(this.f);
            this.a.getDb().put("refresh_token", valueOf3);
            this.a.getDb().put("remind_in", valueOf4);
            return true;
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
            return false;
        }
    }

    public String b(String str) throws Throwable {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("client_id", this.c));
        arrayList.add(new KVPair<>("client_secret", this.d));
        arrayList.add(new KVPair<>("redirect_uri", this.e));
        arrayList.add(new KVPair<>("grant_type", "authorization_code"));
        arrayList.add(new KVPair<>("code", str));
        String b2 = this.h.b("https://api.weibo.com/oauth2/access_token", arrayList, "/oauth2/access_token", c());
        ShareSDK.logApiEvent("/oauth2/access_token", c());
        return b2;
    }

    public HashMap<String, Object> b(int i, int i2, String str) throws Throwable {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>(GlideExecutor.DEFAULT_SOURCE_EXECUTOR_NAME, this.c));
        if (this.f != null) {
            arrayList.add(new KVPair<>("access_token", this.f));
        }
        boolean z = true;
        try {
            ResHelper.parseLong(str);
        } catch (Throwable unused) {
            z = false;
        }
        if (z) {
            arrayList.add(new KVPair<>("uid", str));
        } else {
            arrayList.add(new KVPair<>("screen_name", str));
        }
        arrayList.add(new KVPair<>("count", String.valueOf(i)));
        arrayList.add(new KVPair<>("cursor", String.valueOf(i2)));
        String a = this.h.a("https://api.weibo.com/2/friendships/friends.json", arrayList, "/2/friendships/friends.json", c());
        if (a != null) {
            return new Hashon().fromJson(a);
        }
        return null;
    }

    public void b(final Platform.ShareParams shareParams, final PlatformActionListener platformActionListener) {
        String str;
        if (TextUtils.isEmpty(shareParams.getUrl())) {
            str = shareParams.getText();
        } else {
            str = shareParams.getText() + SQLBuilder.BLANK + shareParams.getUrl();
        }
        if (TextUtils.isEmpty(str)) {
            int stringRes = ResHelper.getStringRes(MobSDK.getContext(), "ssdk_weibo_upload_content");
            if (stringRes > 0) {
                shareParams.setText(MobSDK.getContext().getResources().getString(stringRes));
            }
        } else {
            shareParams.setText(this.a.getShortLintk(str, false));
        }
        AuthorizeListener authorizeListener = new AuthorizeListener() { // from class: cn.sharesdk.sina.weibo.f.3
            @Override // cn.sharesdk.framework.authorize.AuthorizeListener
            public void onCancel() {
                if (platformActionListener != null) {
                    platformActionListener.onCancel(f.this.a, 9);
                }
            }

            @Override // cn.sharesdk.framework.authorize.AuthorizeListener
            public void onComplete(Bundle bundle) {
                if (platformActionListener != null) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("ShareParams", shareParams);
                    platformActionListener.onComplete(f.this.a, 9, hashMap);
                }
                if (bundle != null) {
                    String string = bundle.getString("uid");
                    String string2 = bundle.getString("access_token");
                    String string3 = bundle.getString("expire_in");
                    if (!TextUtils.isEmpty(string2)) {
                        f.this.f = string2;
                        f.this.a.getDb().putToken(f.this.f);
                    }
                    f.this.a.getDb().putUserId(string);
                    try {
                        f.this.a.getDb().putExpiresIn(ResHelper.parseLong(string3));
                    } catch (Throwable unused) {
                    }
                }
            }

            @Override // cn.sharesdk.framework.authorize.AuthorizeListener
            public void onError(Throwable th) {
                if (platformActionListener != null) {
                    platformActionListener.onError(f.this.a, 9, th);
                }
            }
        };
        e eVar = new e();
        eVar.a(this.c, this.f);
        eVar.a(shareParams);
        eVar.a(authorizeListener);
        eVar.show(MobSDK.getContext(), null);
    }

    public boolean b() {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setPackage("com.sina.weibo");
        intent.setType("image/*");
        ResolveInfo resolveActivity = MobSDK.getContext().getPackageManager().resolveActivity(intent, 0);
        if (resolveActivity == null) {
            Intent intent2 = new Intent("android.intent.action.SEND");
            intent2.setPackage("com.sina.weibog3");
            intent2.setType("image/*");
            resolveActivity = MobSDK.getContext().getPackageManager().resolveActivity(intent2, 0);
        }
        return resolveActivity != null;
    }

    public HashMap<String, Object> c(int i, int i2, String str) throws Throwable {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>(GlideExecutor.DEFAULT_SOURCE_EXECUTOR_NAME, this.c));
        if (this.f != null) {
            arrayList.add(new KVPair<>("access_token", this.f));
        }
        boolean z = true;
        try {
            ResHelper.parseLong(str);
        } catch (Throwable unused) {
            z = false;
        }
        if (z) {
            arrayList.add(new KVPair<>("uid", str));
        } else {
            arrayList.add(new KVPair<>("screen_name", str));
        }
        arrayList.add(new KVPair<>("count", String.valueOf(i)));
        arrayList.add(new KVPair<>("page", String.valueOf(i2)));
        String a = this.h.a("https://api.weibo.com/2/friendships/friends/bilateral.json", arrayList, "/2/friendships/friends/bilateral.json", c());
        if (a != null) {
            return new Hashon().fromJson(a);
        }
        return null;
    }

    public void c(String str) {
        this.f = str;
    }

    public HashMap<String, Object> d(int i, int i2, String str) throws Throwable {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>(GlideExecutor.DEFAULT_SOURCE_EXECUTOR_NAME, this.c));
        if (this.f != null) {
            arrayList.add(new KVPair<>("access_token", this.f));
        }
        boolean z = true;
        try {
            ResHelper.parseLong(str);
        } catch (Throwable unused) {
            z = false;
        }
        if (z) {
            arrayList.add(new KVPair<>("uid", str));
        } else {
            arrayList.add(new KVPair<>("screen_name", str));
        }
        arrayList.add(new KVPair<>("count", String.valueOf(i)));
        arrayList.add(new KVPair<>("cursor", String.valueOf(i2)));
        String a = this.h.a("https://api.weibo.com/2/friendships/followers.json", arrayList, "/2/friendships/followers.json", c());
        if (a != null) {
            return new Hashon().fromJson(a);
        }
        return null;
    }

    public HashMap<String, Object> d(String str) throws Throwable {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>(GlideExecutor.DEFAULT_SOURCE_EXECUTOR_NAME, this.c));
        if (this.f != null) {
            arrayList.add(new KVPair<>("access_token", this.f));
        }
        boolean z = true;
        try {
            ResHelper.parseLong(str);
        } catch (Throwable unused) {
            z = false;
        }
        if (z) {
            arrayList.add(new KVPair<>("uid", str));
        } else {
            arrayList.add(new KVPair<>("screen_name", str));
        }
        String a = this.h.a("https://api.weibo.com/2/users/show.json", arrayList, "/2/users/show.json", c());
        if (a != null) {
            return new Hashon().fromJson(a);
        }
        return null;
    }

    public boolean d() {
        Intent intent = new Intent();
        intent.setAction("com.sina.weibo.sdk.Intent.ACTION_WEIBO_REGISTER");
        String packageName = MobSDK.getContext().getPackageName();
        intent.putExtra("_weibo_sdkVersion", "0031405000");
        intent.putExtra("_weibo_appPackage", packageName);
        intent.putExtra("_weibo_appKey", this.c);
        intent.putExtra("_weibo_flag", 538116905);
        intent.putExtra("_weibo_sign", cn.sharesdk.sina.weibo.sdk.a.a(MobSDK.getContext(), packageName));
        cn.sharesdk.framework.utils.e.b().d("intent=" + intent + ", extra=" + intent.getExtras(), new Object[0]);
        MobSDK.getContext().sendBroadcast(intent, "com.sina.weibo.permission.WEIBO_SDK_PERMISSION");
        return true;
    }

    public HashMap<String, Object> e(String str) throws Throwable {
        boolean z;
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>(GlideExecutor.DEFAULT_SOURCE_EXECUTOR_NAME, this.c));
        arrayList.add(new KVPair<>("access_token", this.f));
        try {
            ResHelper.parseLong(str);
            z = true;
        } catch (Throwable unused) {
            z = false;
        }
        if (z) {
            arrayList.add(new KVPair<>("uid", str));
        } else {
            arrayList.add(new KVPair<>("screen_name", str));
        }
        String b2 = this.h.b("https://api.weibo.com/2/friendships/create.json", arrayList, "/2/friendships/create.json", c());
        if (b2 != null) {
            return new Hashon().fromJson(b2);
        }
        return null;
    }

    @Override // cn.sharesdk.framework.authorize.AuthorizeHelper
    public String getAuthorizeUrl() {
        return "";
    }

    @Override // cn.sharesdk.framework.authorize.AuthorizeHelper
    public cn.sharesdk.framework.authorize.b getAuthorizeWebviewClient(cn.sharesdk.framework.authorize.f fVar) {
        return new b(fVar);
    }

    @Override // cn.sharesdk.framework.authorize.AuthorizeHelper
    public String getRedirectUri() {
        return TextUtils.isEmpty(this.e) ? "https://api.weibo.com/oauth2/default.html" : this.e;
    }

    @Override // cn.sharesdk.framework.b, cn.sharesdk.framework.authorize.AuthorizeHelper
    public cn.sharesdk.framework.authorize.d getSSOProcessor(cn.sharesdk.framework.authorize.c cVar) {
        c cVar2 = new c(cVar);
        cVar2.a(32973);
        cVar2.a(this.c, this.e, this.g);
        return cVar2;
    }
}
