package cn.sharesdk.wechat.moments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import cn.sharesdk.framework.b.b.f;
import cn.sharesdk.framework.utils.e;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.utils.WechatClientNotExistException;
import cn.sharesdk.wechat.utils.WechatTimelineNotSupportedException;
import cn.sharesdk.wechat.utils.g;
import cn.sharesdk.wechat.utils.j;
import cn.sharesdk.wechat.utils.k;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.luck.picture.lib.config.PictureConfig;
import java.util.HashMap;

/* loaded from: classes.dex */
public class WechatMoments extends Platform {
    public static final String NAME = "WechatMoments";
    private String a;
    private String b;
    private boolean c;

    private boolean c() {
        if (TextUtils.isEmpty(getDb().get("refresh_token"))) {
            return false;
        }
        g gVar = new g(this, 23);
        gVar.a(this.a, this.b);
        return gVar.a();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public boolean checkAuthorize(int i, Object obj) {
        k a = k.a();
        a.c(this.a);
        if (!a.c()) {
            if (this.listener != null) {
                this.listener.onError(this, i, new WechatClientNotExistException());
            }
            return false;
        }
        if (!a.d()) {
            if (this.listener != null) {
                this.listener.onError(this, 1, new WechatTimelineNotSupportedException());
            }
            return false;
        }
        if (i == 9 || isAuthValid() || c()) {
            return true;
        }
        innerAuthorize(i, obj);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void doAuthorize(String[] strArr) {
        if (TextUtils.isEmpty(this.a) || TextUtils.isEmpty(this.b)) {
            if (this.listener != null) {
                this.listener.onError(this, 8, new Throwable("The params of appID or appSecret is missing !"));
                return;
            }
            return;
        }
        k a = k.a();
        a.c(this.a);
        if (!a.c()) {
            if (this.listener != null) {
                this.listener.onError(this, 1, new WechatClientNotExistException());
                return;
            }
            return;
        }
        if (!a.d()) {
            if (this.listener != null) {
                this.listener.onError(this, 1, new WechatTimelineNotSupportedException());
                return;
            }
            return;
        }
        g gVar = new g(this, 23);
        gVar.a(this.a, this.b);
        j jVar = new j(this);
        jVar.a(gVar);
        jVar.a(new AuthorizeListener() { // from class: cn.sharesdk.wechat.moments.WechatMoments.1
            @Override // cn.sharesdk.framework.authorize.AuthorizeListener
            public void onCancel() {
                if (WechatMoments.this.listener != null) {
                    WechatMoments.this.listener.onCancel(WechatMoments.this, 1);
                }
            }

            @Override // cn.sharesdk.framework.authorize.AuthorizeListener
            public void onComplete(Bundle bundle) {
                WechatMoments.this.afterRegister(1, null);
            }

            @Override // cn.sharesdk.framework.authorize.AuthorizeListener
            public void onError(Throwable th) {
                if (WechatMoments.this.listener != null) {
                    WechatMoments.this.listener.onError(WechatMoments.this, 1, th);
                }
            }
        });
        try {
            a.a(jVar);
        } catch (Throwable th) {
            if (this.listener != null) {
                this.listener.onError(this, 1, th);
            }
        }
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
        shareParams.set("scene", 1);
        k a = k.a();
        a.c(this.a);
        j jVar = new j(this);
        if (this.c) {
            try {
                a.a(jVar, shareParams, this.listener);
                return;
            } catch (Throwable th) {
                if (this.listener != null) {
                    this.listener.onError(this, 9, th);
                    return;
                }
                return;
            }
        }
        jVar.a(shareParams, this.listener);
        try {
            a.b(jVar);
            if (this.listener == null || !a.b()) {
                return;
            }
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("ShareParams", shareParams);
            if (this.listener != null) {
                this.listener.onComplete(this, 9, hashMap);
            }
        } catch (Throwable th2) {
            if (this.listener != null) {
                this.listener.onError(this, 9, th2);
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
        String text = shareParams.getText();
        aVar.b = text;
        String imageUrl = shareParams.getImageUrl();
        String imagePath = shareParams.getImagePath();
        Bitmap imageData = shareParams.getImageData();
        if (!TextUtils.isEmpty(imageUrl)) {
            aVar.d.add(imageUrl);
        } else if (imagePath != null) {
            aVar.e.add(imagePath);
        } else if (imageData != null) {
            aVar.f.add(imageData);
        }
        String url = shareParams.getUrl();
        if (url != null) {
            aVar.c.add(url);
        }
        HashMap<String, Object> hashMap2 = new HashMap<>();
        hashMap2.put("title", shareParams.getTitle());
        hashMap2.put(FileDownloadModel.URL, url);
        hashMap2.put("extInfo", null);
        hashMap2.put("content", text);
        hashMap2.put(PictureConfig.IMAGE, aVar.d);
        hashMap2.put("musicFileUrl", url);
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
        return 23;
    }

    @Override // cn.sharesdk.framework.Platform
    public int getVersion() {
        return 1;
    }

    @Override // cn.sharesdk.framework.Platform
    public boolean hasShareCallback() {
        return !this.c;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void initDevInfo(String str) {
        this.a = getDevinfo("AppId");
        this.b = getDevinfo("AppSecret");
        this.c = "true".equals(getDevinfo("BypassApproval"));
        if (this.a == null || this.a.length() <= 0) {
            this.a = getDevinfo(Wechat.NAME, "AppId");
            this.c = "true".equals(getDevinfo(Wechat.NAME, "BypassApproval"));
            if (this.a != null && this.a.length() > 0) {
                copyDevinfo(Wechat.NAME, NAME);
                this.a = getDevinfo("AppId");
                this.c = "true".equals(getDevinfo("BypassApproval"));
                e.b().d("Try to use the dev info of Wechat, this will cause Id and SortId field are always 0.", new Object[0]);
                return;
            }
            this.a = getDevinfo("WechatFavorite", "AppId");
            if (this.a == null || this.a.length() <= 0) {
                return;
            }
            copyDevinfo("WechatFavorite", NAME);
            this.a = getDevinfo("AppId");
            e.b().d("Try to use the dev info of WechatFavorite, this will cause Id and SortId field are always 0.", new Object[0]);
        }
    }

    @Override // cn.sharesdk.framework.Platform
    public boolean isClientValid() {
        k a = k.a();
        a.c(this.a);
        return a.c() && a.d();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void setNetworkDevinfo() {
        this.a = getNetworkDevinfo("app_id", "AppId");
        this.b = getNetworkDevinfo("app_secret", "AppSecret");
        if (this.a == null || this.a.length() <= 0) {
            this.a = getNetworkDevinfo(22, "app_id", "AppId");
            if (this.a == null || this.a.length() <= 0) {
                this.a = getNetworkDevinfo(37, "app_id", "AppId");
                if (this.a != null && this.a.length() > 0) {
                    copyNetworkDevinfo(23, 23);
                    this.a = getNetworkDevinfo("app_id", "AppId");
                    e.b().d("Try to use the dev info of WechatFavorite, this will cause Id and SortId field are always 0.", new Object[0]);
                }
            } else {
                copyNetworkDevinfo(22, 23);
                this.a = getNetworkDevinfo("app_id", "AppId");
                e.b().d("Try to use the dev info of Wechat, this will cause Id and SortId field are always 0.", new Object[0]);
            }
        }
        if (this.b == null || this.b.length() <= 0) {
            this.b = getNetworkDevinfo(22, "app_secret", "AppSecret");
            if (this.b != null && this.b.length() > 0) {
                copyNetworkDevinfo(22, 23);
                this.b = getNetworkDevinfo("app_secret", "AppSecret");
                e.b().d("Try to use the dev info of Wechat, this will cause Id and SortId field are always 0.", new Object[0]);
                return;
            }
            this.b = getNetworkDevinfo(37, "app_secret", "AppSecret");
            if (this.b == null || this.b.length() <= 0) {
                return;
            }
            copyNetworkDevinfo(23, 23);
            this.b = getNetworkDevinfo("app_secret", "AppSecret");
            e.b().d("Try to use the dev info of WechatFavorite, this will cause Id and SortId field are always 0.", new Object[0]);
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
        if (TextUtils.isEmpty(this.a) || TextUtils.isEmpty(this.b)) {
            if (this.listener != null) {
                this.listener.onError(this, 8, new Throwable("The params of appID or appSecret is missing !"));
                return;
            }
            return;
        }
        g gVar = new g(this, 23);
        gVar.a(this.a, this.b);
        try {
            gVar.a(this.listener);
        } catch (Throwable th) {
            e.b().d(th);
            if (this.listener != null) {
                this.listener.onError(this, 8, th);
            }
        }
    }
}
