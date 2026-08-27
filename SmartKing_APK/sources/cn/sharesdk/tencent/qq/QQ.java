package cn.sharesdk.tencent.qq;

import android.os.Bundle;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import cn.sharesdk.framework.b.b.f;
import cn.sharesdk.tencent.qzone.QZone;
import com.amap.location.common.model.AmapLoc;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.mob.MobSDK;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import com.tencent.bugly.Bugly;
import java.util.HashMap;

/* loaded from: classes.dex */
public class QQ extends Platform {
    public static final String NAME = "QQ";
    private String a;
    private boolean b;
    private boolean c = true;
    private boolean d;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public boolean checkAuthorize(int i, Object obj) {
        c a = c.a(this);
        if (a.b() && this.d && i == 9) {
            return true;
        }
        if (!isAuthValid() && (i != 9 || obj == null || !(obj instanceof Platform.ShareParams) || ((Platform.ShareParams) obj).isShareTencentWeibo())) {
            innerAuthorize(i, obj);
            return false;
        }
        a.a(this.a);
        a.b(this.db.getUserId());
        a.d(this.db.getToken());
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void doAuthorize(String[] strArr) {
        final c a = c.a(this);
        a.a(this.a);
        a.a(strArr);
        a.a(new AuthorizeListener() { // from class: cn.sharesdk.tencent.qq.QQ.1
            @Override // cn.sharesdk.framework.authorize.AuthorizeListener
            public void onCancel() {
                if (QQ.this.listener != null) {
                    QQ.this.listener.onCancel(QQ.this, 1);
                }
            }

            @Override // cn.sharesdk.framework.authorize.AuthorizeListener
            public void onComplete(Bundle bundle) {
                String string = bundle.getString("open_id");
                String string2 = bundle.getString("access_token");
                String string3 = bundle.getString("expires_in");
                QQ.this.db.putToken(string2);
                QQ.this.db.putTokenSecret("");
                try {
                    QQ.this.db.putExpiresIn(ResHelper.parseLong(string3));
                } catch (Throwable th) {
                    cn.sharesdk.framework.utils.e.b().w(th);
                }
                QQ.this.db.putUserId(string);
                String string4 = bundle.getString("pf");
                String string5 = bundle.getString("pfkey");
                String string6 = bundle.getString("pay_token");
                QQ.this.db.put("pf", string4);
                QQ.this.db.put("pfkey", string5);
                QQ.this.db.put("pay_token", string6);
                a.b(string);
                a.d(string2);
                a.a();
                QQ.this.afterRegister(1, null);
            }

            @Override // cn.sharesdk.framework.authorize.AuthorizeListener
            public void onError(Throwable th) {
                if (QQ.this.listener != null) {
                    QQ.this.listener.onError(QQ.this, 1, th);
                }
            }
        }, isSSODisable());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void doCustomerProtocol(String str, String str2, int i, HashMap<String, Object> hashMap, HashMap<String, String> hashMap2) {
        if (this.listener != null) {
            this.listener.onCancel(this, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void doShare(Platform.ShareParams shareParams) {
        String str;
        c a = c.a(this);
        if (this.d && a.b()) {
            try {
                a.a(this, shareParams, this.listener);
                return;
            } catch (Throwable th) {
                if (this.listener != null) {
                    this.listener.onError(this, 9, th);
                    return;
                }
                return;
            }
        }
        String title = shareParams.getTitle();
        String text = shareParams.getText();
        String imagePath = shareParams.getImagePath();
        String imageUrl = shareParams.getImageUrl();
        String musicUrl = shareParams.getMusicUrl();
        String titleUrl = shareParams.getTitleUrl();
        boolean isShareTencentWeibo = shareParams.isShareTencentWeibo();
        int hidden = shareParams.getHidden();
        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(text) && TextUtils.isEmpty(imagePath) && TextUtils.isEmpty(imageUrl) && TextUtils.isEmpty(musicUrl)) {
            if (this.listener != null) {
                this.listener.onError(this, 9, new Throwable("qq share must have one param at least"));
                return;
            }
            return;
        }
        if (TextUtils.isEmpty(titleUrl)) {
            str = titleUrl;
        } else {
            str = getShortLintk(titleUrl, false);
            shareParams.setTitleUrl(str);
        }
        if (!TextUtils.isEmpty(text)) {
            text = getShortLintk(text, false);
            shareParams.setText(text);
        }
        a.a(title, str, text, imagePath, imageUrl, musicUrl, this.b, new PlatformActionListener() { // from class: cn.sharesdk.tencent.qq.QQ.2
            @Override // cn.sharesdk.framework.PlatformActionListener
            public void onCancel(Platform platform, int i) {
                if (QQ.this.listener != null) {
                    QQ.this.listener.onCancel(QQ.this, 9);
                }
            }

            @Override // cn.sharesdk.framework.PlatformActionListener
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                HashMap<String, Object> hashMap2 = new HashMap<>();
                if (hashMap != null) {
                    hashMap2.putAll(hashMap);
                }
                if (QQ.this.listener != null) {
                    QQ.this.listener.onComplete(QQ.this, 9, hashMap2);
                }
            }

            @Override // cn.sharesdk.framework.PlatformActionListener
            public void onError(Platform platform, int i, Throwable th2) {
                if (QQ.this.listener != null) {
                    QQ.this.listener.onError(QQ.this, 9, th2);
                }
            }
        }, isShareTencentWeibo, hidden);
    }

    @Override // cn.sharesdk.framework.Platform
    protected HashMap<String, Object> filterFriendshipInfo(int i, HashMap<String, Object> hashMap) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public f.a filterShareContent(Platform.ShareParams shareParams, HashMap<String, Object> hashMap) {
        f.a aVar = new f.a();
        String titleUrl = shareParams.getTitleUrl();
        aVar.c.add(titleUrl);
        aVar.a = this.a;
        String text = shareParams.getText();
        if (!TextUtils.isEmpty(text)) {
            aVar.b = text;
        }
        String imageUrl = shareParams.getImageUrl();
        String imagePath = shareParams.getImagePath();
        if (!TextUtils.isEmpty(imagePath)) {
            aVar.e.add(imagePath);
        } else if (!TextUtils.isEmpty(imageUrl)) {
            aVar.d.add(imageUrl);
        }
        HashMap<String, Object> hashMap2 = new HashMap<>();
        hashMap2.put("title", shareParams.getTitle());
        hashMap2.put(FileDownloadModel.URL, titleUrl);
        hashMap2.put("imageLocalUrl", imagePath);
        hashMap2.put("summary", text);
        hashMap2.put("appName", DeviceHelper.getInstance(MobSDK.getContext()).getAppName());
        aVar.g = hashMap2;
        return aVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void follow(String str) {
        if (this.listener != null) {
            this.listener.onCancel(this, 6);
        }
    }

    @Override // cn.sharesdk.framework.Platform
    protected HashMap<String, Object> getBilaterals(int i, int i2, String str) {
        return null;
    }

    @Override // cn.sharesdk.framework.Platform
    protected HashMap<String, Object> getFollowers(int i, int i2, String str) {
        return null;
    }

    @Override // cn.sharesdk.framework.Platform
    protected HashMap<String, Object> getFollowings(int i, int i2, String str) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void getFriendList(int i, int i2, String str) {
        if (this.listener != null) {
            this.listener.onCancel(this, 2);
        }
    }

    @Override // cn.sharesdk.framework.Platform
    public String getName() {
        return NAME;
    }

    @Override // cn.sharesdk.framework.Platform
    public int getPlatformId() {
        return 24;
    }

    @Override // cn.sharesdk.framework.Platform
    public int getVersion() {
        return 2;
    }

    @Override // cn.sharesdk.framework.Platform
    public boolean hasShareCallback() {
        return this.c;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void initDevInfo(String str) {
        this.a = getDevinfo("AppId");
        this.b = !Bugly.SDK_IS_DEV.equals(getDevinfo("ShareByAppClient"));
        this.d = "true".equals(getDevinfo("BypassApproval"));
        if (this.a == null || this.a.length() <= 0) {
            this.a = getDevinfo(QZone.NAME, "AppId");
            if (this.a == null || this.a.length() <= 0) {
                return;
            }
            copyDevinfo(QZone.NAME, NAME);
            this.a = getDevinfo("AppId");
            this.b = !Bugly.SDK_IS_DEV.equals(getDevinfo("ShareByAppClient"));
            cn.sharesdk.framework.utils.e.b().d("Try to use the dev info of QZone, this will cause Id and SortId field are always 0.", new Object[0]);
        }
    }

    @Override // cn.sharesdk.framework.Platform
    public boolean isClientValid() {
        c a = c.a(this);
        a.a(this.a);
        return a.b();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void setNetworkDevinfo() {
        this.a = getNetworkDevinfo("app_id", "AppId");
        if (this.a == null || this.a.length() <= 0) {
            this.a = getNetworkDevinfo(6, "app_id", "AppId");
            if (this.a == null || this.a.length() <= 0) {
                return;
            }
            copyNetworkDevinfo(6, 24);
            this.a = getNetworkDevinfo("app_id", "AppId");
            cn.sharesdk.framework.utils.e.b().d("Try to use the dev info of QZone, this will cause Id and SortId field are always 0.", new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void timeline(int i, int i2, String str) {
        if (this.listener != null) {
            this.listener.onCancel(this, 7);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public String uploadImageToFileServer(String str) {
        return super.uploadImageToFileServer(str);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void userInfor(String str) {
        if (str == null || str.length() < 0) {
            str = this.db.getUserId();
        }
        if (str == null || str.length() < 0) {
            if (this.listener != null) {
                this.listener.onError(this, 8, new RuntimeException("qq account is null"));
                return;
            }
            return;
        }
        try {
            HashMap<String, Object> e = c.a(this).e(str);
            if (e != null && e.size() > 0) {
                if (!e.containsKey("ret")) {
                    if (this.listener != null) {
                        this.listener.onError(this, 8, new Throwable());
                        return;
                    }
                    return;
                }
                if (((Integer) e.get("ret")).intValue() != 0) {
                    if (this.listener != null) {
                        this.listener.onError(this, 8, new Throwable(new Hashon().fromHashMap(e)));
                        return;
                    }
                    return;
                }
                if (str == this.db.getUserId()) {
                    this.db.put("nickname", String.valueOf(e.get("nickname")));
                    if (e.containsKey("figureurl_qq_2")) {
                        this.db.put("icon", String.valueOf(e.get("figureurl_qq_2")));
                    } else if (e.containsKey("figureurl_qq_1")) {
                        this.db.put("icon", String.valueOf(e.get("figureurl_qq_1")));
                    }
                    if (e.containsKey("figureurl_2")) {
                        this.db.put("iconQzone", String.valueOf(e.get("figureurl_2")));
                    } else if (e.containsKey("figureurl_1")) {
                        this.db.put("iconQzone", String.valueOf(e.get("figureurl_1")));
                    } else if (e.containsKey("figureurl")) {
                        this.db.put("iconQzone", String.valueOf(e.get("figureurl")));
                    }
                    this.db.put("secretType", String.valueOf(e.get("is_yellow_vip")));
                    if (String.valueOf(e.get("is_yellow_vip")).equals(AmapLoc.RESULT_TYPE_WIFI_ONLY)) {
                        this.db.put("snsUserLevel", String.valueOf(e.get("level")));
                    }
                    String valueOf = String.valueOf(e.get("gender"));
                    int stringRes = ResHelper.getStringRes(MobSDK.getContext(), "ssdk_gender_male");
                    int stringRes2 = ResHelper.getStringRes(MobSDK.getContext(), "ssdk_gender_female");
                    if (valueOf.equals(MobSDK.getContext().getString(stringRes))) {
                        this.db.put("gender", AmapLoc.RESULT_TYPE_GPS);
                    } else if (valueOf.equals(MobSDK.getContext().getString(stringRes2))) {
                        this.db.put("gender", AmapLoc.RESULT_TYPE_WIFI_ONLY);
                    } else {
                        this.db.put("gender", AmapLoc.RESULT_TYPE_FUSED);
                    }
                }
                if (this.listener != null) {
                    if (this.db.get("userTags") != null) {
                        e.put("userTags", this.db.get("userTags"));
                    }
                    this.listener.onComplete(this, 8, e);
                    return;
                }
                return;
            }
            if (this.listener != null) {
                this.listener.onError(this, 8, new Throwable());
            }
        } catch (Throwable th) {
            if (this.listener != null) {
                this.listener.onError(this, 8, th);
            }
        }
    }
}
