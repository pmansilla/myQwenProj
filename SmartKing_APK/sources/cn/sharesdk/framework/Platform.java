package cn.sharesdk.framework;

import android.graphics.Bitmap;
import cn.sharesdk.framework.b.b.f;
import java.util.HashMap;

/* loaded from: classes.dex */
public abstract class Platform {
    public static final int ACTION_AUTHORIZING = 1;
    protected static final int ACTION_CUSTOMER = 655360;
    public static final int ACTION_FOLLOWING_USER = 6;
    protected static final int ACTION_GETTING_BILATERAL_LIST = 10;
    protected static final int ACTION_GETTING_FOLLOWER_LIST = 11;
    public static final int ACTION_GETTING_FRIEND_LIST = 2;
    public static final int ACTION_SENDING_DIRECT_MESSAGE = 5;
    public static final int ACTION_SHARE = 9;
    public static final int ACTION_TIMELINE = 7;
    public static final int ACTION_USER_INFOR = 8;
    public static final int CUSTOMER_ACTION_MASK = 65535;
    public static final int INSTAGRAM_FRIEND = 13;
    public static final int OPEN_WXMINIPROGRAM = 12;
    public static final int SHARE_APPS = 7;
    public static final int SHARE_EMOJI = 9;
    public static final int SHARE_FILE = 8;
    public static final int SHARE_IMAGE = 2;
    public static final int SHARE_MUSIC = 5;
    public static final int SHARE_TEXT = 1;
    public static final int SHARE_VIDEO = 6;
    public static final int SHARE_WEBPAGE = 4;
    public static final int SHARE_WXMINIPROGRAM = 11;
    public static final int SHARE_ZHIFUBAO = 10;
    private c a = new c(this);
    protected final PlatformDb db = this.a.g();
    protected PlatformActionListener listener = this.a.i();

    /* loaded from: classes.dex */
    public static class ShareParams extends InnerShareParams {
        public ShareParams() {
        }

        public ShareParams(String str) {
            super(str);
        }

        public ShareParams(HashMap<String, Object> hashMap) {
            super(hashMap);
        }
    }

    public void SSOSetting(boolean z) {
        this.a.a(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        this.a.a(false);
        this.a.a(getName());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void afterRegister(int i, Object obj) {
        this.a.b(i, obj);
    }

    public void authorize() {
        authorize(null);
    }

    public void authorize(String[] strArr) {
        this.a.a(strArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean b() {
        return this.a.f();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract boolean checkAuthorize(int i, Object obj);

    /* JADX INFO: Access modifiers changed from: protected */
    public void copyDevinfo(String str, String str2) {
        ShareSDK.a(str, str2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void copyNetworkDevinfo(int i, int i2) {
        ShareSDK.a(i, i2);
    }

    public void customerProtocol(String str, String str2, short s, HashMap<String, Object> hashMap, HashMap<String, String> hashMap2) {
        this.a.a(str, str2, s, hashMap, hashMap2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void doAuthorize(String[] strArr);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void doCustomerProtocol(String str, String str2, int i, HashMap<String, Object> hashMap, HashMap<String, String> hashMap2);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void doShare(ShareParams shareParams);

    protected abstract HashMap<String, Object> filterFriendshipInfo(int i, HashMap<String, Object> hashMap);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract f.a filterShareContent(ShareParams shareParams, HashMap<String, Object> hashMap);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void follow(String str);

    public void followFriend(String str) {
        this.a.b(str);
    }

    protected abstract HashMap<String, Object> getBilaterals(int i, int i2, String str);

    public PlatformDb getDb() {
        return this.db;
    }

    public String getDevinfo(String str) {
        return getDevinfo(getName(), str);
    }

    public String getDevinfo(String str, String str2) {
        return ShareSDK.b(str, str2);
    }

    protected abstract HashMap<String, Object> getFollowers(int i, int i2, String str);

    protected abstract HashMap<String, Object> getFollowings(int i, int i2, String str);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void getFriendList(int i, int i2, String str);

    public int getId() {
        return this.a.a();
    }

    public abstract String getName();

    /* JADX INFO: Access modifiers changed from: protected */
    public String getNetworkDevinfo(int i, String str, String str2) {
        return this.a.a(i, str, str2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getNetworkDevinfo(String str, String str2) {
        return getNetworkDevinfo(getPlatformId(), str, str2);
    }

    public PlatformActionListener getPlatformActionListener() {
        return this.a.c();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract int getPlatformId();

    public String getShortLintk(String str, boolean z) {
        return this.a.a(str, z);
    }

    public int getSortId() {
        return this.a.b();
    }

    public void getTimeLine(String str, int i, int i2) {
        this.a.a(str, i, i2);
    }

    public abstract int getVersion();

    public abstract boolean hasShareCallback();

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void initDevInfo(String str);

    /* JADX INFO: Access modifiers changed from: protected */
    public void innerAuthorize(int i, Object obj) {
        this.a.a(i, obj);
    }

    public boolean isAuthValid() {
        return this.a.d();
    }

    public boolean isClientValid() {
        return false;
    }

    public boolean isSSODisable() {
        return this.a.e();
    }

    public void listFriend(int i, int i2, String str) {
        this.a.a(i, i2, str);
    }

    public void removeAccount(boolean z) {
        this.a.h();
        ShareSDK.removeCookieOnAuthorize(z);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void setNetworkDevinfo();

    public void setPlatformActionListener(PlatformActionListener platformActionListener) {
        this.a.a(platformActionListener);
    }

    public void share(ShareParams shareParams) {
        this.a.a(shareParams);
    }

    public void showUser(String str) {
        this.a.c(str);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void timeline(int i, int i2, String str);

    protected String uploadImageToFileServer(Bitmap bitmap) {
        return this.a.a(bitmap);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String uploadImageToFileServer(String str) {
        return this.a.d(str);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void userInfor(String str);
}
