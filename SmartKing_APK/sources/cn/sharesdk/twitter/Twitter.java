package cn.sharesdk.twitter;

import android.os.Bundle;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import cn.sharesdk.framework.b.b.f;
import cn.sharesdk.framework.utils.e;
import com.amap.location.common.model.AmapLoc;
import com.liulishuo.filedownloader.model.ConnectionModel;
import com.luck.picture.lib.config.PictureConfig;
import com.mob.MobSDK;
import com.mob.tools.utils.BitmapHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public class Twitter extends Platform {
    public static final String NAME = "Twitter";
    private String a;
    private String b;
    private String c;

    /* loaded from: classes.dex */
    public static class ShareParams extends Platform.ShareParams {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public boolean checkAuthorize(int i, Object obj) {
        if (isAuthValid()) {
            c a = c.a(this);
            a.a(this.a, this.b, this.c);
            String token = this.db.getToken();
            String tokenSecret = this.db.getTokenSecret();
            if (token != null && tokenSecret != null) {
                a.a(token, tokenSecret);
                return true;
            }
        }
        innerAuthorize(i, obj);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void doAuthorize(String[] strArr) {
        final c a = c.a(this);
        a.a(this.a, this.b, this.c);
        a.a(new AuthorizeListener() { // from class: cn.sharesdk.twitter.Twitter.1
            @Override // cn.sharesdk.framework.authorize.AuthorizeListener
            public void onCancel() {
                if (Twitter.this.listener != null) {
                    Twitter.this.listener.onCancel(Twitter.this, 1);
                }
            }

            @Override // cn.sharesdk.framework.authorize.AuthorizeListener
            public void onComplete(Bundle bundle) {
                try {
                    String string = bundle.getString("oauth_token");
                    String string2 = bundle.getString("screen_name");
                    if (string != null && !string.equals("")) {
                        new RuntimeException("platformtools.bh").fillInStackTrace().printStackTrace();
                        String string3 = bundle.getString("oauth_token_secret");
                        String string4 = bundle.getString("user_id");
                        Twitter.this.db.putToken(string);
                        Twitter.this.db.putTokenSecret(string3);
                        Twitter.this.db.putUserId(string4);
                        Twitter.this.db.put("nickname", string2);
                        a.a(string, string3);
                        Twitter.this.afterRegister(1, null);
                    }
                    String string5 = bundle.getString("tk");
                    String string6 = bundle.getString("ts");
                    String valueOf = String.valueOf(bundle.getLong("user_id", 0L));
                    Twitter.this.db.putToken(string5);
                    Twitter.this.db.putTokenSecret(string6);
                    Twitter.this.db.putUserId(valueOf);
                    Twitter.this.db.put("nickname", string2);
                    Twitter.this.afterRegister(1, null);
                } catch (Exception e) {
                    e.b().d(e);
                }
            }

            @Override // cn.sharesdk.framework.authorize.AuthorizeListener
            public void onError(Throwable th) {
                if (Twitter.this.listener != null) {
                    Twitter.this.listener.onError(Twitter.this, 1, th);
                }
            }
        }, isSSODisable());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void doCustomerProtocol(String str, String str2, int i, HashMap<String, Object> hashMap, HashMap<String, String> hashMap2) {
        try {
            HashMap<String, Object> a = c.a(this).a(str, str2, hashMap, hashMap2);
            if (a != null && a.size() > 0) {
                if (!a.containsKey("error_code") && !a.containsKey("error")) {
                    if (this.listener != null) {
                        this.listener.onComplete(this, i, a);
                        return;
                    }
                    return;
                }
                if (this.listener != null) {
                    this.listener.onError(this, i, new Throwable(new Hashon().fromHashMap(a)));
                    return;
                }
                return;
            }
            if (this.listener != null) {
                this.listener.onError(this, i, new Throwable("response is null"));
            }
        } catch (Throwable th) {
            if (this.listener != null) {
                this.listener.onError(this, i, th);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void doShare(Platform.ShareParams shareParams) {
        HashMap<String, Object> e;
        c a = c.a(this);
        String shortLintk = getShortLintk(shareParams.getText(), false);
        shareParams.setText(shortLintk);
        try {
            String[] imageArray = shareParams.getImageArray();
            String imagePath = shareParams.getImagePath();
            String imageUrl = shareParams.getImageUrl();
            String text = shareParams.getText();
            String filePath = shareParams.getFilePath();
            String shortLintk2 = getShortLintk(shareParams.getUrl(), false);
            if (shareParams.getShareType() == 6 && !TextUtils.isEmpty(filePath)) {
                e = a.f(text, filePath);
            } else if (imageArray != null && imageArray.length > 0) {
                e = a.a(shortLintk, imageArray);
            } else if (!TextUtils.isEmpty(imagePath) && new File(imagePath).exists()) {
                e = a.e(shortLintk, imagePath);
            } else if (TextUtils.isEmpty(imageUrl)) {
                e = !TextUtils.isEmpty(shortLintk2) ? a.e(shortLintk2) : a.e(shortLintk);
            } else {
                String downloadBitmap = BitmapHelper.downloadBitmap(MobSDK.getContext(), imageUrl);
                e = new File(downloadBitmap).exists() ? a.e(shortLintk, downloadBitmap) : null;
            }
            if (e == null) {
                if (this.listener != null) {
                    this.listener.onError(this, 8, new Throwable("response is null"));
                }
            } else if (e.containsKey("error_code") || e.containsKey("error")) {
                if (this.listener != null) {
                    this.listener.onError(this, 8, new Throwable(new Hashon().fromHashMap(e)));
                }
            } else {
                e.put("ShareParams", shareParams);
                if (this.listener != null) {
                    this.listener.onComplete(this, 9, e);
                }
            }
        } catch (Throwable th) {
            if (this.listener != null) {
                this.listener.onError(this, 9, th);
            }
        }
    }

    @Override // cn.sharesdk.framework.Platform
    protected HashMap<String, Object> filterFriendshipInfo(int i, HashMap<String, Object> hashMap) {
        HashMap<String, Object> hashMap2 = new HashMap<>();
        if (i != 2) {
            switch (i) {
                case 10:
                    hashMap2.put("type", "FRIENDS");
                    break;
                case 11:
                    hashMap2.put("type", "FOLLOWERS");
                    break;
                default:
                    return null;
            }
        } else {
            hashMap2.put("type", "FOLLOWING");
        }
        hashMap2.put("snsplat", Integer.valueOf(getPlatformId()));
        hashMap2.put("snsuid", this.db.getUserId());
        String valueOf = hashMap.containsKey("next_cursor") ? String.valueOf(hashMap.get("next_cursor")) : null;
        Object obj = hashMap.get("users");
        if (obj == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = (ArrayList) obj;
        if (arrayList2.size() <= 0) {
            return null;
        }
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            HashMap hashMap3 = (HashMap) it.next();
            if (hashMap3 != null) {
                HashMap hashMap4 = new HashMap();
                hashMap4.put("snsuid", String.valueOf(hashMap3.get(ConnectionModel.ID)));
                hashMap4.put("nickname", String.valueOf(hashMap3.get("screen_name")));
                hashMap4.put("icon", String.valueOf(hashMap3.get("profile_image_url")));
                hashMap4.put("gender", AmapLoc.RESULT_TYPE_FUSED);
                hashMap4.put("resume", String.valueOf(hashMap3.get("description")));
                hashMap4.put("secretType", "true".equals(String.valueOf(hashMap3.get("verified"))) ? AmapLoc.RESULT_TYPE_WIFI_ONLY : AmapLoc.RESULT_TYPE_GPS);
                hashMap4.put("followerCount", String.valueOf(hashMap3.get("followers_count")));
                hashMap4.put("favouriteCount", String.valueOf(hashMap3.get("friends_count")));
                hashMap4.put("shareCount", String.valueOf(hashMap3.get("statuses_count")));
                hashMap4.put("snsregat", String.valueOf(ResHelper.dateToLong(String.valueOf(hashMap3.get("created_at")))));
                hashMap4.put("snsUserUrl", "https://twitter.com/" + hashMap3.get("screen_name"));
                arrayList.add(hashMap4);
            }
        }
        if (arrayList.size() <= 0) {
            return null;
        }
        String str = valueOf + "_false";
        if (TextUtils.isEmpty(valueOf) || AmapLoc.RESULT_TYPE_GPS.equals(valueOf)) {
            str = "0_true";
        }
        hashMap2.put("nextCursor", str);
        hashMap2.put("list", arrayList);
        return hashMap2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public f.a filterShareContent(Platform.ShareParams shareParams, HashMap<String, Object> hashMap) {
        ArrayList arrayList;
        f.a aVar = new f.a();
        aVar.b = shareParams.getText();
        if (hashMap != null) {
            HashMap hashMap2 = (HashMap) hashMap.get("entities");
            if (hashMap2 != null && (arrayList = (ArrayList) hashMap2.get(PictureConfig.EXTRA_MEDIA)) != null && arrayList.size() > 0 && ((HashMap) arrayList.get(0)) != null) {
                aVar.d.add(String.valueOf(hashMap.get("media_url")));
            }
            aVar.a = String.valueOf(hashMap.get(ConnectionModel.ID));
            aVar.g = hashMap;
        }
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
        String userId = TextUtils.isEmpty(null) ? this.db.getUserId() : null;
        if (TextUtils.isEmpty(userId)) {
            userId = this.db.getUserName();
        }
        if (TextUtils.isEmpty(userId)) {
            return null;
        }
        c a = c.a(this);
        try {
            if (TextUtils.isEmpty(str)) {
                str = AmapLoc.RESULT_TYPE_GPS;
            }
            HashMap<String, Object> c = a.c(userId, str);
            if (c != null && c.size() > 0 && !c.containsKey("error_code") && !c.containsKey("error")) {
                return filterFriendshipInfo(11, c);
            }
            return null;
        } catch (Throwable th) {
            e.b().d(th);
            return null;
        }
    }

    @Override // cn.sharesdk.framework.Platform
    protected HashMap<String, Object> getFollowings(int i, int i2, String str) {
        String userId = TextUtils.isEmpty(null) ? this.db.getUserId() : null;
        if (TextUtils.isEmpty(userId)) {
            userId = this.db.getUserName();
        }
        if (TextUtils.isEmpty(userId)) {
            return null;
        }
        c a = c.a(this);
        try {
            if (TextUtils.isEmpty(str)) {
                str = AmapLoc.RESULT_TYPE_GPS;
            }
            HashMap<String, Object> b = a.b(userId, str);
            if (b != null && b.size() > 0 && !b.containsKey("error_code") && !b.containsKey("error")) {
                return filterFriendshipInfo(2, b);
            }
            return null;
        } catch (Throwable th) {
            e.b().d(th);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void getFriendList(int i, int i2, String str) {
        String userId = TextUtils.isEmpty(null) ? this.db.getUserId() : null;
        if (TextUtils.isEmpty(userId)) {
            userId = this.db.getUserName();
        }
        if (TextUtils.isEmpty(userId) && this.listener != null) {
            this.listener.onError(this, 2, new Throwable("The account do not authorize!"));
        }
        c a = c.a(this);
        try {
            if (TextUtils.isEmpty(str)) {
                str = AmapLoc.RESULT_TYPE_GPS;
            }
            HashMap<String, Object> b = a.b(userId, str);
            if (b != null && b.size() > 0) {
                if (!b.containsKey("error_code") && !b.containsKey("error")) {
                    if (this.listener != null) {
                        this.listener.onComplete(this, 2, b);
                        return;
                    }
                    return;
                }
                if (this.listener != null) {
                    this.listener.onError(this, 2, new Throwable(new Hashon().fromHashMap(b)));
                    return;
                }
                return;
            }
            if (this.listener != null) {
                this.listener.onError(this, 2, new Throwable("response is null"));
            }
        } catch (Throwable th) {
            if (this.listener != null) {
                this.listener.onError(this, 2, th);
            }
        }
    }

    @Override // cn.sharesdk.framework.Platform
    public String getName() {
        return NAME;
    }

    @Override // cn.sharesdk.framework.Platform
    public int getPlatformId() {
        return 11;
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
        this.a = getDevinfo("ConsumerKey");
        this.b = getDevinfo("ConsumerSecret");
        this.c = getDevinfo("CallbackUrl");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void setNetworkDevinfo() {
        this.a = getNetworkDevinfo("consumer_key", "ConsumerKey");
        this.b = getNetworkDevinfo("consumer_secret", "ConsumerSecret");
        this.c = getNetworkDevinfo("redirect_uri", "CallbackUrl");
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
        try {
            HashMap<String, Object> d = c.a(this).d(str);
            if (d != null && d.size() > 0) {
                if (!d.containsKey("error_code") && !d.containsKey("error")) {
                    if (str == null) {
                        this.db.put("nickname", String.valueOf(d.get("screen_name")));
                        this.db.put("icon", String.valueOf(d.get("profile_image_url")));
                        this.db.put("gender", AmapLoc.RESULT_TYPE_FUSED);
                        this.db.put("resume", String.valueOf(d.get("description")));
                        this.db.put("secretType", "true".equals(String.valueOf(d.get("verified"))) ? AmapLoc.RESULT_TYPE_WIFI_ONLY : AmapLoc.RESULT_TYPE_GPS);
                        this.db.put("followerCount", String.valueOf(d.get("followers_count")));
                        this.db.put("favouriteCount", String.valueOf(d.get("friends_count")));
                        this.db.put("shareCount", String.valueOf(d.get("statuses_count")));
                        this.db.put("snsregat", String.valueOf(ResHelper.dateToLong(String.valueOf(d.get("created_at")))));
                        this.db.put("snsUserUrl", "https://twitter.com/" + d.get("screen_name"));
                    }
                    if (this.listener != null) {
                        this.listener.onComplete(this, 8, d);
                        return;
                    }
                    return;
                }
                if (this.listener != null) {
                    this.listener.onError(this, 8, new Throwable(new Hashon().fromHashMap(d)));
                    return;
                }
                return;
            }
            if (this.listener != null) {
                this.listener.onError(this, 8, new Throwable("response is null"));
            }
        } catch (Throwable th) {
            if (this.listener != null) {
                this.listener.onError(this, 8, th);
            }
        }
    }
}
