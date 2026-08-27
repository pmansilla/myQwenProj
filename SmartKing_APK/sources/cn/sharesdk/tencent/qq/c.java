package cn.sharesdk.tencent.qq;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import cn.sharesdk.framework.authorize.SSOListener;
import com.amap.location.common.model.AmapLoc;
import com.mob.MobSDK;
import com.mob.tools.RxMob;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/* compiled from: QQHelper.java */
/* loaded from: classes.dex */
public class c extends cn.sharesdk.framework.b {
    private static final String[] b = {"get_user_info", "get_simple_userinfo", "get_user_profile", "get_app_friends", "add_share", "list_album", "upload_pic", "add_album", "set_user_face", "get_vip_info", "get_vip_rich_info", "get_intimate_friends_weibo", "match_nick_tips_weibo", "add_t", "add_pic_t"};
    private static c c;
    private String d;
    private String[] e;
    private String f;
    private String g;
    private String h;
    private String i;

    private c(Platform platform) {
        super(platform);
        b();
    }

    public static c a(Platform platform) {
        if (c == null) {
            c = new c(platform);
        }
        return c;
    }

    private void a(String str, String str2, String str3, String str4, String str5, String str6, PlatformActionListener platformActionListener) {
        if (str5 == null && str4 != null && new File(str4).exists()) {
            str5 = ((QQ) this.a).uploadImageToFileServer(str4);
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("http://openmobile.qq.com/api/check?");
            sb.append("page=shareindex.html&");
            sb.append("style=9&");
            sb.append("action=shareToQQ&");
            sb.append("sdkv=2.2.1&");
            sb.append("sdkp=a&");
            sb.append("appId=");
            sb.append(this.d);
            sb.append("&");
            DeviceHelper deviceHelper = DeviceHelper.getInstance(MobSDK.getContext());
            sb.append("status_os=");
            sb.append(Data.urlEncode(deviceHelper.getOSVersionName(), "utf-8"));
            sb.append("&");
            sb.append("status_machine=");
            sb.append(Data.urlEncode(deviceHelper.getModel(), "utf-8"));
            sb.append("&");
            sb.append("status_version=");
            sb.append(Data.urlEncode(String.valueOf(deviceHelper.getOSVersionInt()), "utf-8"));
            sb.append("&");
            String appName = deviceHelper.getAppName();
            if (!TextUtils.isEmpty(appName)) {
                sb.append("site=");
                sb.append(Data.urlEncode(appName, "utf-8"));
                sb.append("&");
            }
            if (!TextUtils.isEmpty(str)) {
                if (str.length() > 40) {
                    str = str.substring(40) + "...";
                }
                if (str.length() > 80) {
                    str = str.substring(80) + "...";
                }
                sb.append("title=");
                sb.append(Data.urlEncode(str, "utf-8"));
                sb.append("&");
            }
            if (!TextUtils.isEmpty(str3)) {
                sb.append("summary=");
                sb.append(Data.urlEncode(str3, "utf-8"));
                sb.append("&");
            }
            if (!TextUtils.isEmpty(str2)) {
                sb.append("targeturl=");
                sb.append(Data.urlEncode(str2, "utf-8"));
                sb.append("&");
            }
            if (!TextUtils.isEmpty(str5)) {
                sb.append("imageUrl=");
                sb.append(Data.urlEncode(str5, "utf-8"));
                sb.append("&");
            }
            sb.append("type=1");
            f fVar = new f();
            fVar.a(sb.toString());
            fVar.a(platformActionListener);
            fVar.b(this.d);
            fVar.show(MobSDK.getContext(), null);
        } catch (Throwable th) {
            if (platformActionListener != null) {
                platformActionListener.onError(this.a, 9, th);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0014  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x006c A[Catch: Throwable -> 0x00cc, TryCatch #0 {Throwable -> 0x00cc, blocks: (B:3:0x0002, B:5:0x0008, B:11:0x0019, B:13:0x006c, B:15:0x0072, B:16:0x007a, B:18:0x009c, B:21:0x00a4, B:23:0x00bb, B:26:0x00c6, B:31:0x008e), top: B:2:0x0002 }] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x008e A[Catch: Throwable -> 0x00cc, TryCatch #0 {Throwable -> 0x00cc, blocks: (B:3:0x0002, B:5:0x0008, B:11:0x0019, B:13:0x006c, B:15:0x0072, B:16:0x007a, B:18:0x009c, B:21:0x00a4, B:23:0x00bb, B:26:0x00c6, B:31:0x008e), top: B:2:0x0002 }] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0017  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void b(java.lang.String r7, java.lang.String r8, java.lang.String r9, java.lang.String r10, java.lang.String r11, java.lang.String r12, cn.sharesdk.framework.PlatformActionListener r13) {
        /*
            r6 = this;
            r7 = 9
            boolean r8 = android.text.TextUtils.isEmpty(r10)     // Catch: java.lang.Throwable -> Lcc
            if (r8 == 0) goto L11
            boolean r8 = android.text.TextUtils.isEmpty(r11)     // Catch: java.lang.Throwable -> Lcc
            if (r8 != 0) goto Lf
            goto L11
        Lf:
            r8 = 0
            goto L12
        L11:
            r8 = 1
        L12:
            if (r8 != 0) goto L17
            java.lang.String r12 = "/t/add_t"
            goto L19
        L17:
            java.lang.String r12 = "/t/add_pic_t"
        L19:
            r4 = r12
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lcc
            r12.<init>()     // Catch: java.lang.Throwable -> Lcc
            java.lang.String r0 = "https://graph.qq.com"
            r12.append(r0)     // Catch: java.lang.Throwable -> Lcc
            r12.append(r4)     // Catch: java.lang.Throwable -> Lcc
            java.lang.String r1 = r12.toString()     // Catch: java.lang.Throwable -> Lcc
            java.util.ArrayList r2 = new java.util.ArrayList     // Catch: java.lang.Throwable -> Lcc
            r2.<init>()     // Catch: java.lang.Throwable -> Lcc
            com.mob.tools.network.KVPair r12 = new com.mob.tools.network.KVPair     // Catch: java.lang.Throwable -> Lcc
            java.lang.String r0 = "oauth_consumer_key"
            java.lang.String r3 = r6.d     // Catch: java.lang.Throwable -> Lcc
            r12.<init>(r0, r3)     // Catch: java.lang.Throwable -> Lcc
            r2.add(r12)     // Catch: java.lang.Throwable -> Lcc
            com.mob.tools.network.KVPair r12 = new com.mob.tools.network.KVPair     // Catch: java.lang.Throwable -> Lcc
            java.lang.String r0 = "access_token"
            java.lang.String r3 = r6.h     // Catch: java.lang.Throwable -> Lcc
            r12.<init>(r0, r3)     // Catch: java.lang.Throwable -> Lcc
            r2.add(r12)     // Catch: java.lang.Throwable -> Lcc
            com.mob.tools.network.KVPair r12 = new com.mob.tools.network.KVPair     // Catch: java.lang.Throwable -> Lcc
            java.lang.String r0 = "openid"
            java.lang.String r3 = r6.f     // Catch: java.lang.Throwable -> Lcc
            r12.<init>(r0, r3)     // Catch: java.lang.Throwable -> Lcc
            r2.add(r12)     // Catch: java.lang.Throwable -> Lcc
            com.mob.tools.network.KVPair r12 = new com.mob.tools.network.KVPair     // Catch: java.lang.Throwable -> Lcc
            java.lang.String r0 = "format"
            java.lang.String r3 = "json"
            r12.<init>(r0, r3)     // Catch: java.lang.Throwable -> Lcc
            r2.add(r12)     // Catch: java.lang.Throwable -> Lcc
            com.mob.tools.network.KVPair r12 = new com.mob.tools.network.KVPair     // Catch: java.lang.Throwable -> Lcc
            java.lang.String r0 = "content"
            r12.<init>(r0, r9)     // Catch: java.lang.Throwable -> Lcc
            r2.add(r12)     // Catch: java.lang.Throwable -> Lcc
            if (r8 == 0) goto L8e
            boolean r8 = android.text.TextUtils.isEmpty(r10)     // Catch: java.lang.Throwable -> Lcc
            if (r8 == 0) goto L7a
            android.content.Context r8 = com.mob.MobSDK.getContext()     // Catch: java.lang.Throwable -> Lcc
            java.lang.String r10 = com.mob.tools.utils.BitmapHelper.downloadBitmap(r8, r11)     // Catch: java.lang.Throwable -> Lcc
        L7a:
            com.mob.tools.network.KVPair r3 = new com.mob.tools.network.KVPair     // Catch: java.lang.Throwable -> Lcc
            java.lang.String r8 = "pic"
            r3.<init>(r8, r10)     // Catch: java.lang.Throwable -> Lcc
            cn.sharesdk.framework.a.b r0 = cn.sharesdk.framework.a.b.a()     // Catch: java.lang.Throwable -> Lcc
            int r5 = r6.c()     // Catch: java.lang.Throwable -> Lcc
            java.lang.String r8 = r0.a(r1, r2, r3, r4, r5)     // Catch: java.lang.Throwable -> Lcc
            goto L9a
        L8e:
            cn.sharesdk.framework.a.b r8 = cn.sharesdk.framework.a.b.a()     // Catch: java.lang.Throwable -> Lcc
            int r9 = r6.c()     // Catch: java.lang.Throwable -> Lcc
            java.lang.String r8 = r8.b(r1, r2, r4, r9)     // Catch: java.lang.Throwable -> Lcc
        L9a:
            if (r8 == 0) goto Ld4
            int r9 = r8.length()     // Catch: java.lang.Throwable -> Lcc
            if (r9 <= 0) goto Ld4
            if (r13 == 0) goto Ld4
            com.mob.tools.utils.Hashon r9 = new com.mob.tools.utils.Hashon     // Catch: java.lang.Throwable -> Lcc
            r9.<init>()     // Catch: java.lang.Throwable -> Lcc
            java.util.HashMap r9 = r9.fromJson(r8)     // Catch: java.lang.Throwable -> Lcc
            java.lang.String r10 = "ret"
            java.lang.Object r10 = r9.get(r10)     // Catch: java.lang.Throwable -> Lcc
            java.lang.Integer r10 = (java.lang.Integer) r10     // Catch: java.lang.Throwable -> Lcc
            int r10 = r10.intValue()     // Catch: java.lang.Throwable -> Lcc
            if (r10 == 0) goto Lc6
            cn.sharesdk.framework.Platform r9 = r6.a     // Catch: java.lang.Throwable -> Lcc
            java.lang.Exception r10 = new java.lang.Exception     // Catch: java.lang.Throwable -> Lcc
            r10.<init>(r8)     // Catch: java.lang.Throwable -> Lcc
            r13.onError(r9, r7, r10)     // Catch: java.lang.Throwable -> Lcc
            goto Ld4
        Lc6:
            cn.sharesdk.framework.Platform r8 = r6.a     // Catch: java.lang.Throwable -> Lcc
            r13.onComplete(r8, r7, r9)     // Catch: java.lang.Throwable -> Lcc
            goto Ld4
        Lcc:
            r8 = move-exception
            if (r13 == 0) goto Ld4
            cn.sharesdk.framework.Platform r9 = r6.a
            r13.onError(r9, r7, r8)
        Ld4:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.sharesdk.tencent.qq.c.b(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, cn.sharesdk.framework.PlatformActionListener):void");
    }

    private String d() {
        String[] strArr = this.e == null ? b : this.e;
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

    public void a() {
        RxMob.Subscribable create = RxMob.create(new RxMob.OnSubscribe() { // from class: cn.sharesdk.tencent.qq.c.2
            @Override // com.mob.tools.RxMob.OnSubscribe
            public void call(RxMob.Subscriber subscriber) {
                ArrayList<KVPair<String>> arrayList = new ArrayList<>();
                arrayList.add(new KVPair<>("access_token", c.this.h));
                arrayList.add(new KVPair<>("unionid", AmapLoc.RESULT_TYPE_WIFI_ONLY));
                NetworkHelper.NetworkTimeOut networkTimeOut = new NetworkHelper.NetworkTimeOut();
                networkTimeOut.readTimout = 10000;
                networkTimeOut.connectionTimeout = 10000;
                cn.sharesdk.framework.a.b a = cn.sharesdk.framework.a.b.a();
                try {
                    c.this.g = a.httpPost("https://graph.qq.com/oauth2.0/me", arrayList, (KVPair<String>) null, (ArrayList<KVPair<String>>) null, networkTimeOut);
                    subscriber.onCompleted();
                } catch (Throwable th) {
                    th.printStackTrace();
                    c.this.a.getDb().put("unionid", "");
                    cn.sharesdk.framework.utils.e.b().d("qq auth,get unionId fail", new Object[0]);
                }
            }
        });
        create.subscribeOn(RxMob.Thread.NEW_THREAD);
        create.observeOn(RxMob.Thread.IMMEDIATE);
        create.subscribe(new RxMob.Subscriber() { // from class: cn.sharesdk.tencent.qq.c.3
            @Override // com.mob.tools.RxMob.Subscriber
            public void onCompleted() {
                if (c.this.g == null || c.this.g.length() <= 0) {
                    return;
                }
                c.this.g = c.this.g.replace("callback( ", "");
                c.this.g = c.this.g.replace(" );", "");
                HashMap fromJson = new Hashon().fromJson(c.this.g);
                if (!fromJson.containsKey("unionid")) {
                    c.this.a.getDb().put("unionid", "");
                } else {
                    c.this.a.getDb().put("unionid", (String) fromJson.get("unionid"));
                }
            }

            @Override // com.mob.tools.RxMob.Subscriber
            public void onError(Throwable th) {
                c.this.a.getDb().put("unionid", "");
                cn.sharesdk.framework.utils.e.b().d("qq auth,get unionId fail", new Object[0]);
            }
        });
    }

    public void a(Platform platform, Platform.ShareParams shareParams, PlatformActionListener platformActionListener) throws Throwable {
        cn.sharesdk.framework.utils.f fVar = new cn.sharesdk.framework.utils.f();
        fVar.a(this.i, "com.tencent.mobileqq.activity.JumpActivity");
        fVar.a(shareParams, platform);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("ShareParams", shareParams);
        platformActionListener.onComplete(platform, 9, hashMap);
    }

    public void a(final AuthorizeListener authorizeListener, boolean z) {
        a(new SSOListener() { // from class: cn.sharesdk.tencent.qq.c.1
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

    public void a(String str, String str2, String str3, String str4, String str5, String str6, boolean z, PlatformActionListener platformActionListener, boolean z2, int i) {
        String str7;
        if (z2) {
            b(str, str2, str3, str4, str5, str6, platformActionListener);
            return;
        }
        if (!z || !b()) {
            a(str, str2, str3, str4, str5, str6, platformActionListener);
            return;
        }
        if (!TextUtils.isEmpty(str4)) {
            File file = new File(str4);
            if (file.exists() && str4.startsWith("/data/")) {
                str7 = new File(ResHelper.getCachePath(MobSDK.getContext(), "images"), System.currentTimeMillis() + file.getName()).getAbsolutePath();
                if (!ResHelper.copyFile(str4, str7)) {
                    str7 = null;
                }
                Intent intent = new Intent();
                intent.putExtra("title", str);
                intent.putExtra("titleUrl", str2);
                intent.putExtra("summary", str3);
                intent.putExtra("imagePath", str7);
                intent.putExtra("imageUrl", str5);
                intent.putExtra("musicUrl", str6);
                intent.putExtra("appId", this.d);
                intent.putExtra("hidden", i);
                e eVar = new e();
                eVar.a(this.a, platformActionListener);
                eVar.a(this.d);
                eVar.show(MobSDK.getContext(), intent);
            }
        }
        str7 = str4;
        Intent intent2 = new Intent();
        intent2.putExtra("title", str);
        intent2.putExtra("titleUrl", str2);
        intent2.putExtra("summary", str3);
        intent2.putExtra("imagePath", str7);
        intent2.putExtra("imageUrl", str5);
        intent2.putExtra("musicUrl", str6);
        intent2.putExtra("appId", this.d);
        intent2.putExtra("hidden", i);
        e eVar2 = new e();
        eVar2.a(this.a, platformActionListener);
        eVar2.a(this.d);
        eVar2.show(MobSDK.getContext(), intent2);
    }

    public void a(String[] strArr) {
        this.e = strArr;
    }

    public void b(String str) {
        this.f = str;
    }

    public boolean b() {
        String str;
        String str2;
        try {
            PackageInfo packageInfo = MobSDK.getContext().getPackageManager().getPackageInfo("com.tencent.mobileqq", 0);
            str = packageInfo.versionName;
            this.i = packageInfo.packageName;
            str2 = str;
        } catch (Throwable th) {
            try {
                try {
                    try {
                        try {
                            PackageInfo packageInfo2 = MobSDK.getContext().getPackageManager().getPackageInfo("com.tencent.tim", 0);
                            str2 = packageInfo2.versionName;
                            this.i = packageInfo2.packageName;
                        } catch (Throwable unused) {
                            PackageInfo packageInfo3 = MobSDK.getContext().getPackageManager().getPackageInfo("com.tencent.minihd.qq", 0);
                            str2 = packageInfo3.versionName;
                            this.i = packageInfo3.packageName;
                        }
                    } catch (Throwable unused2) {
                        str = null;
                        cn.sharesdk.framework.utils.e.b().d(th);
                    }
                } catch (Throwable unused3) {
                    PackageInfo packageInfo4 = MobSDK.getContext().getPackageManager().getPackageInfo("com.tencent.mobileqqi", 0);
                    str2 = packageInfo4.versionName;
                    this.i = packageInfo4.packageName;
                }
            } catch (Throwable unused4) {
                PackageInfo packageInfo5 = MobSDK.getContext().getPackageManager().getPackageInfo("com.tencent.qqlite", 0);
                str2 = packageInfo5.versionName;
                this.i = packageInfo5.packageName;
            }
        }
        return !TextUtils.isEmpty(str2);
    }

    public HashMap<String, Object> c(String str) throws Throwable {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("access_token", str));
        ArrayList<KVPair<String>> arrayList2 = new ArrayList<>();
        arrayList2.add(new KVPair<>("User-Agent", System.getProperties().getProperty("http.agent") + " ArzenAndroidSDK"));
        String a = cn.sharesdk.framework.a.b.a().a("https://graph.qq.com/oauth2.0/me", arrayList, arrayList2, (NetworkHelper.NetworkTimeOut) null, "/oauth2.0/me", c());
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

    public void d(String str) {
        this.h = str;
    }

    public HashMap<String, Object> e(String str) throws Throwable {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("access_token", this.h));
        arrayList.add(new KVPair<>("oauth_consumer_key", this.d));
        arrayList.add(new KVPair<>("openid", this.f));
        ArrayList<KVPair<String>> arrayList2 = new ArrayList<>();
        arrayList2.add(new KVPair<>("User-Agent", System.getProperties().getProperty("http.agent") + " ArzenAndroidSDK"));
        String a = cn.sharesdk.framework.a.b.a().a("https://graph.qq.com/user/get_simple_userinfo", arrayList, arrayList2, (NetworkHelper.NetworkTimeOut) null, "/user/get_simple_userinfo", c());
        if (a == null || a.length() <= 0) {
            return null;
        }
        return new Hashon().fromJson(a);
    }

    @Override // cn.sharesdk.framework.authorize.AuthorizeHelper
    public String getAuthorizeUrl() {
        String redirectUri;
        ShareSDK.logApiEvent("/oauth2.0/authorize", c());
        String d = d();
        try {
            redirectUri = Data.urlEncode(getRedirectUri(), "utf-8");
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
            redirectUri = getRedirectUri();
        }
        return "https://graph.qq.com/oauth2.0/m_authorize?response_type=token&client_id=" + this.d + "&redirect_uri=" + redirectUri + "&display=mobile&scope=" + d;
    }

    @Override // cn.sharesdk.framework.authorize.AuthorizeHelper
    public cn.sharesdk.framework.authorize.b getAuthorizeWebviewClient(cn.sharesdk.framework.authorize.f fVar) {
        return new b(fVar);
    }

    @Override // cn.sharesdk.framework.authorize.AuthorizeHelper
    public String getRedirectUri() {
        return "auth://tauth.qq.com/";
    }

    @Override // cn.sharesdk.framework.b, cn.sharesdk.framework.authorize.AuthorizeHelper
    public cn.sharesdk.framework.authorize.d getSSOProcessor(cn.sharesdk.framework.authorize.c cVar) {
        d dVar = new d(cVar);
        dVar.a(5656);
        dVar.a(this.d, d(), this.i);
        return dVar;
    }
}
