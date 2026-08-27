package cn.sharesdk.tencent.qzone;

import android.os.Bundle;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import cn.sharesdk.framework.b.b.f;
import cn.sharesdk.framework.utils.e;
import cn.sharesdk.framework.utils.f;
import cn.sharesdk.tencent.qq.QQ;
import com.amap.location.common.model.AmapLoc;
import com.mob.MobSDK;
import com.mob.tools.utils.BitmapHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import java.io.File;
import java.util.HashMap;

/* loaded from: classes.dex */
public class QZone extends Platform {
    public static final String NAME = "QZone";
    private String a;
    private boolean b;

    private void a(Platform.ShareParams shareParams) throws Throwable {
        f fVar = new f();
        fVar.a("com.qzone", "com.qzonex.module.operation.ui.QZonePublishMoodActivity");
        fVar.a(shareParams, this);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("ShareParams", shareParams);
        this.listener.onComplete(this, 9, hashMap);
    }

    private void b(final Platform.ShareParams shareParams) {
        String imageUrl = shareParams.getImageUrl();
        String imagePath = shareParams.getImagePath();
        boolean isShareTencentWeibo = shareParams.isShareTencentWeibo();
        try {
            if (TextUtils.isEmpty(imagePath) && !TextUtils.isEmpty(imageUrl)) {
                shareParams.setImagePath(BitmapHelper.downloadBitmap(MobSDK.getContext(), imageUrl));
                doShare(shareParams);
                return;
            }
            if (!isAuthValid()) {
                final PlatformActionListener platformActionListener = getPlatformActionListener();
                setPlatformActionListener(new PlatformActionListener() { // from class: cn.sharesdk.tencent.qzone.QZone.2
                    @Override // cn.sharesdk.framework.PlatformActionListener
                    public void onCancel(Platform platform, int i) {
                        if (platformActionListener != null) {
                            platformActionListener.onCancel(platform, 9);
                        }
                    }

                    @Override // cn.sharesdk.framework.PlatformActionListener
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        QZone.this.setPlatformActionListener(platformActionListener);
                        QZone.this.doShare(shareParams);
                    }

                    @Override // cn.sharesdk.framework.PlatformActionListener
                    public void onError(Platform platform, int i, Throwable th) {
                        if (platformActionListener != null) {
                            platformActionListener.onError(platform, 9, th);
                        }
                    }
                });
                authorize();
                return;
            }
            String text = shareParams.getText();
            if (TextUtils.isEmpty(text)) {
                if (this.listener != null) {
                    this.listener.onError(this, 9, new Throwable("share params' value of text is empty!"));
                    return;
                }
                return;
            }
            String shortLintk = getShortLintk(text, false);
            shareParams.setText(shortLintk);
            b a = b.a(this);
            HashMap<String, Object> b = isShareTencentWeibo ? a.b(imagePath, shortLintk) : a.a(imagePath, shortLintk);
            if (b == null && this.listener != null) {
                this.listener.onError(this, 9, new Throwable("response is empty"));
            }
            b.put("ShareParams", shareParams);
            if (this.listener != null) {
                this.listener.onComplete(this, 9, b);
            }
        } catch (Throwable th) {
            if (this.listener != null) {
                this.listener.onError(this, 9, th);
            }
        }
    }

    private void c(final Platform.ShareParams shareParams) {
        try {
            String imageUrl = shareParams.getImageUrl();
            String imagePath = shareParams.getImagePath();
            if (!isClientValid()) {
                if (this.listener != null) {
                    this.listener.onError(this, 9, new QQClientNotExistException());
                    return;
                }
                return;
            }
            String str = (TextUtils.isEmpty(imagePath) || !new File(imagePath).exists()) ? imageUrl : imagePath;
            String title = shareParams.getTitle();
            String titleUrl = shareParams.getTitleUrl();
            String site = shareParams.getSite();
            String text = shareParams.getText();
            String filePath = shareParams.getFilePath();
            int shareType = shareParams.getShareType();
            if (!TextUtils.isEmpty(text)) {
                text = getShortLintk(text, false);
                shareParams.setText(text);
            }
            String str2 = text;
            if (!TextUtils.isEmpty(titleUrl)) {
                titleUrl = getShortLintk(titleUrl, false);
                shareParams.setTitleUrl(titleUrl);
            }
            b.a(this).a(shareType, title, titleUrl, str2, str, site, filePath, new PlatformActionListener() { // from class: cn.sharesdk.tencent.qzone.QZone.3
                @Override // cn.sharesdk.framework.PlatformActionListener
                public void onCancel(Platform platform, int i) {
                    if (QZone.this.listener != null) {
                        QZone.this.listener.onCancel(QZone.this, 9);
                    }
                }

                @Override // cn.sharesdk.framework.PlatformActionListener
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    if (QZone.this.listener != null) {
                        hashMap.put("ShareParams", shareParams);
                        QZone.this.listener.onComplete(QZone.this, 9, hashMap);
                    }
                }

                @Override // cn.sharesdk.framework.PlatformActionListener
                public void onError(Platform platform, int i, Throwable th) {
                    if (QZone.this.listener != null) {
                        QZone.this.listener.onError(QZone.this, 9, th);
                    }
                }
            });
        } catch (Throwable th) {
            if (this.listener != null) {
                this.listener.onError(this, 9, th);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public boolean checkAuthorize(int i, Object obj) {
        b a = b.a(this);
        if (a.b() && this.b && i == 9) {
            return true;
        }
        if (!isAuthValid() && i != 9) {
            innerAuthorize(i, obj);
            return false;
        }
        a.a(this.a);
        a.b(this.db.getUserId());
        a.c(this.db.getToken());
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void doAuthorize(String[] strArr) {
        final b a = b.a(this);
        a.a(this.a);
        a.a(strArr);
        a.a(new AuthorizeListener() { // from class: cn.sharesdk.tencent.qzone.QZone.1
            @Override // cn.sharesdk.framework.authorize.AuthorizeListener
            public void onCancel() {
                if (QZone.this.listener != null) {
                    QZone.this.listener.onCancel(QZone.this, 1);
                }
            }

            @Override // cn.sharesdk.framework.authorize.AuthorizeListener
            public void onComplete(Bundle bundle) {
                String string = bundle.getString("open_id");
                String string2 = bundle.getString("access_token");
                String string3 = bundle.getString("expires_in");
                QZone.this.db.putToken(string2);
                QZone.this.db.putTokenSecret("");
                try {
                    QZone.this.db.putExpiresIn(ResHelper.parseLong(string3));
                } catch (Throwable th) {
                    e.b().d(th);
                }
                QZone.this.db.putUserId(string);
                String string4 = bundle.getString("pf");
                String string5 = bundle.getString("pfkey");
                String string6 = bundle.getString("pay_token");
                QZone.this.db.put("pf", string4);
                QZone.this.db.put("pfkey", string5);
                QZone.this.db.put("pay_token", string6);
                a.b(string);
                a.c(string2);
                a.a();
                QZone.this.afterRegister(1, null);
            }

            @Override // cn.sharesdk.framework.authorize.AuthorizeListener
            public void onError(Throwable th) {
                if (QZone.this.listener != null) {
                    QZone.this.listener.onError(QZone.this, 1, th);
                }
            }
        }, isSSODisable());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void doCustomerProtocol(String str, String str2, int i, HashMap<String, Object> hashMap, HashMap<String, String> hashMap2) {
        HashMap<String, Object> a = b.a(this).a(str, str2, hashMap, hashMap2);
        if (a == null || a.size() <= 0) {
            if (this.listener != null) {
                this.listener.onError(this, i, new Throwable());
            }
        } else if (!a.containsKey("ret")) {
            if (this.listener != null) {
                this.listener.onError(this, i, new Throwable());
            }
        } else if (((Integer) a.get("ret")).intValue() == 0) {
            if (this.listener != null) {
                this.listener.onComplete(this, i, a);
            }
        } else if (this.listener != null) {
            this.listener.onError(this, i, new Throwable(new Hashon().fromHashMap(a)));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void doShare(Platform.ShareParams shareParams) {
        if (!b.a(this).b() || !this.b) {
            if (shareParams.isShareTencentWeibo()) {
                b(shareParams);
                return;
            } else {
                c(shareParams);
                return;
            }
        }
        try {
            a(shareParams);
        } catch (Throwable th) {
            if (this.listener != null) {
                this.listener.onError(this, 9, th);
            }
        }
    }

    @Override // cn.sharesdk.framework.Platform
    protected HashMap<String, Object> filterFriendshipInfo(int i, HashMap<String, Object> hashMap) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public f.a filterShareContent(Platform.ShareParams shareParams, HashMap<String, Object> hashMap) {
        f.a aVar = new f.a();
        aVar.b = shareParams.getText();
        String imageUrl = shareParams.getImageUrl();
        String imagePath = shareParams.getImagePath();
        if (imagePath != null) {
            aVar.e.add(imagePath);
        } else if (hashMap.get("large_url") != null) {
            aVar.d.add(String.valueOf(hashMap.get("large_url")));
        } else if (hashMap.get("small_url") != null) {
            aVar.d.add(String.valueOf(hashMap.get("small_url")));
        } else if (imageUrl != null) {
            aVar.d.add(imageUrl);
        }
        String titleUrl = shareParams.getTitleUrl();
        if (titleUrl != null) {
            aVar.c.add(titleUrl);
        }
        HashMap<String, Object> hashMap2 = new HashMap<>();
        hashMap2.put("title", shareParams.getTitle());
        hashMap2.put("titleUrl", shareParams.getTitleUrl());
        hashMap2.put("site", shareParams.getSite());
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
        return 6;
    }

    @Override // cn.sharesdk.framework.Platform
    public int getVersion() {
        return 2;
    }

    @Override // cn.sharesdk.framework.Platform
    public boolean hasShareCallback() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void initDevInfo(String str) {
        this.a = getDevinfo("AppId");
        this.b = "true".equals(getDevinfo("BypassApproval"));
        if (this.a == null || this.a.length() <= 0) {
            this.a = getDevinfo(QQ.NAME, "AppId");
            if (this.a == null || this.a.length() <= 0) {
                return;
            }
            copyDevinfo(QQ.NAME, NAME);
            this.a = getDevinfo("AppId");
            e.b().d("Try to use the dev info of QQ, this will cause Id and SortId field are always 0.", new Object[0]);
        }
    }

    @Override // cn.sharesdk.framework.Platform
    public boolean isClientValid() {
        b a = b.a(this);
        a.a(this.a);
        return a.d();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void setNetworkDevinfo() {
        this.a = getNetworkDevinfo("app_id", "AppId");
        if (this.a == null || this.a.length() <= 0) {
            this.a = getNetworkDevinfo(24, "app_id", "AppId");
            if (this.a == null || this.a.length() <= 0) {
                return;
            }
            copyNetworkDevinfo(24, 6);
            this.a = getNetworkDevinfo("app_id", "AppId");
            e.b().d("Try to use the dev info of QQ, this will cause Id and SortId field are always 0.", new Object[0]);
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
            HashMap<String, Object> d = b.a(this).d(str);
            if (d != null && d.size() > 0) {
                if (!d.containsKey("ret")) {
                    if (this.listener != null) {
                        this.listener.onError(this, 8, new Throwable());
                        return;
                    }
                    return;
                }
                if (((Integer) d.get("ret")).intValue() != 0) {
                    if (this.listener != null) {
                        this.listener.onError(this, 8, new Throwable(new Hashon().fromHashMap(d)));
                        return;
                    }
                    return;
                }
                if (str == this.db.getUserId()) {
                    this.db.put("nickname", String.valueOf(d.get("nickname")));
                    if (d.containsKey("figureurl_qq_2")) {
                        this.db.put("iconQQ", String.valueOf(d.get("figureurl_qq_2")));
                    } else if (d.containsKey("figureurl_qq_1")) {
                        this.db.put("iconQQ", String.valueOf(d.get("figureurl_qq_1")));
                    }
                    if (d.containsKey("figureurl_2")) {
                        this.db.put("icon", String.valueOf(d.get("figureurl_2")));
                    } else if (d.containsKey("figureurl_1")) {
                        this.db.put("icon", String.valueOf(d.get("figureurl_1")));
                    } else if (d.containsKey("figureurl")) {
                        this.db.put("icon", String.valueOf(d.get("figureurl")));
                    }
                    this.db.put("secretType", String.valueOf(d.get("is_yellow_vip")));
                    if (String.valueOf(d.get("is_yellow_vip")).equals(AmapLoc.RESULT_TYPE_WIFI_ONLY)) {
                        this.db.put("snsUserLevel", String.valueOf(d.get("level")));
                    }
                    String valueOf = String.valueOf(d.get("gender"));
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
                        d.put("userTags", this.db.get("userTags"));
                    }
                    this.listener.onComplete(this, 8, d);
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
