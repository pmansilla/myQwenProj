package cn.sharesdk.tencent.qzone;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import cn.sharesdk.framework.authorize.SSOListener;
import cn.sharesdk.framework.authorize.f;
import cn.sharesdk.framework.utils.e;
import com.alibaba.fastjson.asm.Opcodes;
import com.amap.location.common.model.AmapLoc;
import com.luck.picture.lib.config.PictureConfig;
import com.mob.MobSDK;
import com.mob.tools.RxMob;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ReflectHelper;
import com.mob.tools.utils.ResHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/* compiled from: QZoneHelper.java */
/* loaded from: classes.dex */
public class b extends cn.sharesdk.framework.b {
    private static final String[] b = {"get_user_info", "get_simple_userinfo", "get_user_profile", "get_app_friends", "add_share", "list_album", "upload_pic", "add_album", "set_user_face", "get_vip_info", "get_vip_rich_info", "get_intimate_friends_weibo", "match_nick_tips_weibo", "add_t", "add_pic_t"};
    private static b c;
    private String d;
    private String e;
    private String f;
    private String g;
    private cn.sharesdk.framework.a.b h;
    private String[] i;

    private b(Platform platform) {
        super(platform);
        this.h = cn.sharesdk.framework.a.b.a();
    }

    public static b a(Platform platform) {
        if (c == null) {
            c = new b(platform);
        }
        return c;
    }

    private String e() {
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
        return sb.toString();
    }

    private String f(String str) {
        if (Build.VERSION.SDK_INT <= 10) {
            return "";
        }
        try {
            Class<?> cls = ReflectHelper.getClass("android.media.MediaMetadataRetriever");
            Object newInstance = cls.newInstance();
            cls.getMethod("setDataSource", String.class).invoke(newInstance, str);
            return (String) ReflectHelper.invokeInstanceMethod(newInstance, "extractMetadata", 9);
        } catch (Throwable unused) {
            return "";
        }
    }

    public HashMap<String, Object> a(String str, String str2) throws Throwable {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("access_token", this.f));
        arrayList.add(new KVPair<>("oauth_consumer_key", this.d));
        arrayList.add(new KVPair<>("openid", this.e));
        arrayList.add(new KVPair<>("format", "json"));
        if (!TextUtils.isEmpty(str2)) {
            if (str2.length() > 200) {
                str2 = str2.substring(0, Opcodes.IFNONNULL) + MobSDK.getContext().getString(ResHelper.getStringRes(MobSDK.getContext(), "ssdk_symbol_ellipsis"));
            }
            arrayList.add(new KVPair<>("photodesc", str2));
        }
        arrayList.add(new KVPair<>("mobile", AmapLoc.RESULT_TYPE_WIFI_ONLY));
        KVPair<String> kVPair = new KVPair<>(PictureConfig.FC_TAG, str);
        ArrayList<KVPair<String>> arrayList2 = new ArrayList<>();
        arrayList2.add(new KVPair<>("User-Agent", System.getProperties().getProperty("http.agent") + " ArzenAndroidSDK"));
        String a = this.h.a("https://graph.qq.com/photo/upload_pic", arrayList, kVPair, arrayList2, "/photo/upload_pic", c());
        if (a == null || a.length() <= 0) {
            return null;
        }
        return new Hashon().fromJson(a);
    }

    /* JADX WARN: Code restructure failed: missing block: B:39:0x00fe, code lost:
    
        r8 = null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.util.HashMap<java.lang.String, java.lang.Object> a(java.lang.String r8, java.lang.String r9, java.util.HashMap<java.lang.String, java.lang.Object> r10, java.util.HashMap<java.lang.String, java.lang.String> r11) {
        /*
            Method dump skipped, instructions count: 273
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.sharesdk.tencent.qzone.b.a(java.lang.String, java.lang.String, java.util.HashMap, java.util.HashMap):java.util.HashMap");
    }

    public void a() {
        RxMob.Subscribable create = RxMob.create(new RxMob.OnSubscribe() { // from class: cn.sharesdk.tencent.qzone.b.2
            @Override // com.mob.tools.RxMob.OnSubscribe
            public void call(RxMob.Subscriber subscriber) {
                ArrayList<KVPair<String>> arrayList = new ArrayList<>();
                arrayList.add(new KVPair<>("access_token", b.this.f));
                arrayList.add(new KVPair<>("unionid", AmapLoc.RESULT_TYPE_WIFI_ONLY));
                NetworkHelper.NetworkTimeOut networkTimeOut = new NetworkHelper.NetworkTimeOut();
                networkTimeOut.readTimout = 10000;
                networkTimeOut.connectionTimeout = 10000;
                cn.sharesdk.framework.a.b a = cn.sharesdk.framework.a.b.a();
                try {
                    b.this.g = a.httpPost("https://graph.qq.com/oauth2.0/me", arrayList, (KVPair<String>) null, (ArrayList<KVPair<String>>) null, networkTimeOut);
                    subscriber.onCompleted();
                } catch (Throwable unused) {
                    b.this.a.getDb().put("unionid", "");
                    e.b().d("qq auth, get unionId fail", new Object[0]);
                }
            }
        });
        create.subscribeOn(RxMob.Thread.NEW_THREAD);
        create.observeOn(RxMob.Thread.IMMEDIATE);
        create.subscribe(new RxMob.Subscriber() { // from class: cn.sharesdk.tencent.qzone.b.3
            @Override // com.mob.tools.RxMob.Subscriber
            public void onCompleted() {
                if (b.this.g == null || b.this.g.length() <= 0) {
                    return;
                }
                b.this.g = b.this.g.replace("callback( ", "");
                b.this.g = b.this.g.replace(" );", "");
                HashMap fromJson = new Hashon().fromJson(b.this.g);
                if (!fromJson.containsKey("unionid")) {
                    b.this.a.getDb().put("unionid", "");
                } else {
                    b.this.a.getDb().put("unionid", (String) fromJson.get("unionid"));
                }
            }

            @Override // com.mob.tools.RxMob.Subscriber
            public void onError(Throwable th) {
                b.this.a.getDb().put("unionid", "");
                e.b().d("qq auth, get unionId fail", new Object[0]);
            }
        });
    }

    public void a(int i, String str, String str2, String str3, String str4, String str5, String str6, PlatformActionListener platformActionListener) throws Throwable {
        String appName = TextUtils.isEmpty(str5) ? DeviceHelper.getInstance(MobSDK.getContext()).getAppName() : str5;
        if (appName.length() > 20) {
            appName = appName.substring(0, 20) + "...";
        }
        b(i, (TextUtils.isEmpty(str) || str.length() <= 200) ? str : str.substring(0, 200), str2, str3, str4, appName, str6, platformActionListener);
    }

    public void a(final AuthorizeListener authorizeListener, boolean z) {
        a(new SSOListener() { // from class: cn.sharesdk.tencent.qzone.b.1
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
                if (th != null) {
                    authorizeListener.onError(th);
                } else {
                    authorizeListener.onError(new Throwable("Unknown Throwable!"));
                }
            }
        });
    }

    public void a(String str) {
        this.d = str;
    }

    public void a(String[] strArr) {
        this.i = strArr;
    }

    public HashMap<String, Object> b(String str, String str2) throws Throwable {
        boolean z = !TextUtils.isEmpty(str);
        String str3 = z ? "/t/add_pic_t" : "/t/add_t";
        String str4 = "https://graph.qq.com" + str3;
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("oauth_consumer_key", this.d));
        arrayList.add(new KVPair<>("access_token", this.f));
        arrayList.add(new KVPair<>("openid", this.e));
        arrayList.add(new KVPair<>("format", "json"));
        arrayList.add(new KVPair<>("content", str2));
        String a = z ? this.h.a(str4, arrayList, new KVPair<>("pic", str), str3, c()) : this.h.b(str4, arrayList, str3, c());
        if (a == null || a.length() <= 0) {
            return null;
        }
        HashMap<String, Object> fromJson = new Hashon().fromJson(a);
        if (((Integer) fromJson.get("ret")).intValue() == 0) {
            return fromJson;
        }
        throw new Throwable(a);
    }

    public void b(int i, String str, String str2, String str3, String str4, String str5, String str6, PlatformActionListener platformActionListener) throws Throwable {
        String str7 = AmapLoc.RESULT_TYPE_WIFI_ONLY;
        if (!TextUtils.isEmpty(str6)) {
            str7 = AmapLoc.RESULT_TYPE_CELL_WITH_NEIGHBORS;
        } else if (TextUtils.isEmpty(str) && TextUtils.isEmpty(str2)) {
            str7 = AmapLoc.RESULT_TYPE_CELL_ONLY;
        } else if (TextUtils.isEmpty(str2)) {
            if (platformActionListener != null) {
                platformActionListener.onError(null, 9, new Throwable("The param of title or titleUrl is null !"));
                return;
            }
            return;
        }
        if (!TextUtils.isEmpty(str4)) {
            File file = new File(str4);
            if (file.exists() && str4.startsWith("/data/")) {
                String absolutePath = new File(ResHelper.getCachePath(MobSDK.getContext(), "images"), System.currentTimeMillis() + file.getName()).getAbsolutePath();
                str4 = ResHelper.copyFile(str4, absolutePath) ? absolutePath : null;
            }
        }
        if (!TextUtils.isEmpty(str3) && str3.length() > 600) {
            str3 = str3.substring(0, 600);
        }
        StringBuilder sb = new StringBuilder();
        if (str7 == AmapLoc.RESULT_TYPE_CELL_ONLY || str7 == AmapLoc.RESULT_TYPE_CELL_WITH_NEIGHBORS) {
            sb.append("mqqapi://qzone/publish?src_type=app&version=1&file_type=news");
        } else {
            sb.append("mqqapi://share/to_qzone?src_type=app&version=1&file_type=news");
        }
        if (!TextUtils.isEmpty(str4)) {
            sb.append("&image_url=");
            sb.append(Base64.encodeToString(str4.getBytes("utf-8"), 2));
        }
        if (!TextUtils.isEmpty(str6) && str7.equals(AmapLoc.RESULT_TYPE_CELL_WITH_NEIGHBORS)) {
            ResHelper.getFileSize(str6);
            String valueOf = String.valueOf(str5);
            String f = f(str6);
            sb.append("&videoPath=");
            sb.append(Base64.encodeToString(str6.getBytes("utf-8"), 2));
            sb.append("&videoSize=");
            sb.append(Base64.encodeToString(valueOf.getBytes("utf-8"), 2));
            if (!TextUtils.isEmpty(f)) {
                sb.append("&videoDuration=");
                sb.append(Base64.encodeToString(f.getBytes("utf-8"), 2));
            }
        }
        if (!TextUtils.isEmpty(str)) {
            sb.append("&title=");
            sb.append(Base64.encodeToString(str.getBytes("utf-8"), 2));
        }
        if (!TextUtils.isEmpty(str3)) {
            sb.append("&description=");
            sb.append(Base64.encodeToString(str3.getBytes("utf-8"), 2));
        }
        sb.append("&share_id=");
        sb.append(this.d);
        if (!TextUtils.isEmpty(str2)) {
            sb.append("&url=");
            sb.append(Base64.encodeToString(str2.getBytes("utf-8"), 2));
        }
        sb.append("&app_name=");
        sb.append(Base64.encodeToString(str5.getBytes("utf-8"), 2));
        if (!TextUtils.isEmpty(str3)) {
            sb.append("&share_qq_ext_str=");
            sb.append(Base64.encodeToString(str3.getBytes(), 2));
        }
        sb.append("&req_type=");
        sb.append(Base64.encodeToString(str7.getBytes("utf-8"), 2));
        String str8 = d() ? AmapLoc.RESULT_TYPE_WIFI_ONLY : AmapLoc.RESULT_TYPE_GPS;
        sb.append("&cflag=");
        sb.append(Base64.encodeToString(str8.getBytes("utf-8"), 2));
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse(sb.toString()));
        if (MobSDK.getContext().getPackageManager().resolveActivity(intent, 1) != null) {
            d dVar = new d();
            dVar.a(sb.toString(), true);
            dVar.a(platformActionListener);
            dVar.a(this.d);
            dVar.show(MobSDK.getContext(), null);
        }
    }

    public void b(String str) {
        this.e = str;
    }

    public boolean b() {
        try {
            PackageInfo packageInfo = MobSDK.getContext().getPackageManager().getPackageInfo("com.qzone", 0);
            if (packageInfo == null) {
                return false;
            }
            String[] split = packageInfo.versionName.split("\\.");
            int[] iArr = new int[split.length];
            for (int i = 0; i < iArr.length; i++) {
                try {
                    iArr[i] = ResHelper.parseInt(split[i]);
                } catch (Throwable th) {
                    e.b().d(th);
                    iArr[i] = 0;
                }
            }
            return iArr.length > 1 && (iArr[0] >= 4 || iArr[1] >= 1);
        } catch (Throwable th2) {
            e.b().d(th2);
            return false;
        }
    }

    public void c(String str) {
        this.f = str;
    }

    public HashMap<String, Object> d(String str) throws Throwable {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("access_token", this.f));
        arrayList.add(new KVPair<>("oauth_consumer_key", this.d));
        arrayList.add(new KVPair<>("openid", this.e));
        ArrayList<KVPair<String>> arrayList2 = new ArrayList<>();
        arrayList2.add(new KVPair<>("User-Agent", System.getProperties().getProperty("http.agent") + " ArzenAndroidSDK"));
        String a = this.h.a("https://graph.qq.com/user/get_simple_userinfo", arrayList, arrayList2, (NetworkHelper.NetworkTimeOut) null, "/user/get_simple_userinfo", c());
        if (a == null || a.length() <= 0) {
            return null;
        }
        return new Hashon().fromJson(a);
    }

    public boolean d() {
        String str;
        String str2;
        try {
            str = MobSDK.getContext().getPackageManager().getPackageInfo("com.tencent.mobileqq", 0).versionName;
        } catch (Throwable th) {
            try {
                try {
                    str2 = MobSDK.getContext().getPackageManager().getPackageInfo("com.tencent.tim", 0).versionName;
                } catch (Throwable unused) {
                    str2 = MobSDK.getContext().getPackageManager().getPackageInfo("com.tencent.minihd.qq", 0).versionName;
                }
                str = str2;
            } catch (Throwable unused2) {
                e.b().d(th);
                str = null;
            }
        }
        return !TextUtils.isEmpty(str);
    }

    public HashMap<String, Object> e(String str) throws Throwable {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("access_token", str));
        ArrayList<KVPair<String>> arrayList2 = new ArrayList<>();
        arrayList2.add(new KVPair<>("User-Agent", System.getProperties().getProperty("http.agent") + " ArzenAndroidSDK"));
        String a = this.h.a("https://graph.qq.com/oauth2.0/me", arrayList, arrayList2, (NetworkHelper.NetworkTimeOut) null, "/oauth2.0/me", c());
        if (a.startsWith("callback")) {
            while (!a.startsWith("{") && a.length() > 0) {
                a = a.substring(1);
            }
            while (!a.endsWith("}") && a.length() > 0) {
                a = a.substring(0, a.length() - 1);
            }
        }
        if (a == null || a.length() <= 0) {
            return null;
        }
        return new Hashon().fromJson(a);
    }

    @Override // cn.sharesdk.framework.authorize.AuthorizeHelper
    public String getAuthorizeUrl() {
        String redirectUri;
        ShareSDK.logApiEvent("/oauth2.0/authorize", c());
        String e = e();
        try {
            redirectUri = Data.urlEncode(getRedirectUri(), "utf-8");
        } catch (Throwable th) {
            e.b().d(th);
            redirectUri = getRedirectUri();
        }
        return "https://graph.qq.com/oauth2.0/m_authorize?response_type=token&client_id=" + this.d + "&redirect_uri=" + redirectUri + "&display=mobile&scope=" + e;
    }

    @Override // cn.sharesdk.framework.authorize.AuthorizeHelper
    public cn.sharesdk.framework.authorize.b getAuthorizeWebviewClient(f fVar) {
        return new a(fVar);
    }

    @Override // cn.sharesdk.framework.authorize.AuthorizeHelper
    public String getRedirectUri() {
        return "auth://tauth.qq.com/";
    }

    @Override // cn.sharesdk.framework.b, cn.sharesdk.framework.authorize.AuthorizeHelper
    public cn.sharesdk.framework.authorize.d getSSOProcessor(cn.sharesdk.framework.authorize.c cVar) {
        c cVar2 = new c(cVar);
        cVar2.a(5656);
        cVar2.a(this.d, e());
        return cVar2;
    }
}
