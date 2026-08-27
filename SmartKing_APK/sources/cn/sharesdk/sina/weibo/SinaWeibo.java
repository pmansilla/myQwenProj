package cn.sharesdk.sina.weibo;

import android.os.Bundle;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import cn.sharesdk.framework.b.b.f;
import com.amap.location.common.model.AmapLoc;
import com.liulishuo.filedownloader.model.ConnectionModel;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import com.tencent.bugly.Bugly;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public class SinaWeibo extends Platform {
    public static final String NAME = "SinaWeibo";
    private String a;
    private String b;
    private String c;
    private boolean d;

    private boolean c() {
        if (TextUtils.isEmpty(getDb().get("refresh_token"))) {
            return false;
        }
        f a = f.a(this);
        a.a(this.a, this.b);
        a.a(this.c);
        return a.a();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public boolean checkAuthorize(int i, Object obj) {
        f a = f.a(this);
        a.c(this.db.getToken());
        a.a(this.a, this.b);
        a.a(this.c);
        a.d();
        if (i == 9 || isAuthValid() || c()) {
            return true;
        }
        innerAuthorize(i, obj);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void doAuthorize(String[] strArr) {
        final f a = f.a(this);
        a.a(this.a, this.b);
        a.a(this.c);
        a.a(strArr);
        a.a(new AuthorizeListener() { // from class: cn.sharesdk.sina.weibo.SinaWeibo.1
            @Override // cn.sharesdk.framework.authorize.AuthorizeListener
            public void onCancel() {
                if (SinaWeibo.this.listener != null) {
                    SinaWeibo.this.listener.onCancel(SinaWeibo.this, 1);
                }
            }

            @Override // cn.sharesdk.framework.authorize.AuthorizeListener
            public void onComplete(Bundle bundle) {
                long j;
                String string = bundle.getString("uid");
                String string2 = bundle.getString("access_token");
                String string3 = bundle.getString("expires_in");
                String string4 = bundle.getString("refresh_token");
                if (bundle.containsKey("username")) {
                    SinaWeibo.this.db.put("nickname", bundle.getString("userName"));
                }
                SinaWeibo.this.db.put("remind_in", bundle.getString("remind_in"));
                SinaWeibo.this.db.putToken(string2);
                try {
                    j = ResHelper.parseLong(string3);
                } catch (Throwable unused) {
                    j = 0;
                }
                SinaWeibo.this.db.putExpiresIn(j);
                SinaWeibo.this.db.put("refresh_token", string4);
                SinaWeibo.this.db.putUserId(string);
                a.c(string2);
                SinaWeibo.this.afterRegister(1, null);
            }

            @Override // cn.sharesdk.framework.authorize.AuthorizeListener
            public void onError(Throwable th) {
                if (SinaWeibo.this.listener != null) {
                    SinaWeibo.this.listener.onError(SinaWeibo.this, 1, th);
                }
            }
        }, isSSODisable());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void doCustomerProtocol(String str, String str2, int i, HashMap<String, Object> hashMap, HashMap<String, String> hashMap2) {
        try {
            HashMap<String, Object> a = f.a(this).a(str, str2, hashMap, hashMap2);
            if (a != null && a.size() > 0) {
                if (!a.containsKey("error_code") || ((Integer) a.get("error_code")).intValue() == 0) {
                    if (this.listener != null) {
                        this.listener.onComplete(this, i, a);
                        return;
                    }
                    return;
                } else {
                    if (this.listener != null) {
                        this.listener.onError(this, i, new Throwable(new Hashon().fromHashMap(a)));
                        return;
                    }
                    return;
                }
            }
            if (this.listener != null) {
                this.listener.onError(this, i, new Throwable());
            }
        } catch (Throwable th) {
            this.listener.onError(this, i, th);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void doShare(Platform.ShareParams shareParams) {
        f a = f.a(this);
        a.a(this.a, this.b);
        if (this.d && a.b()) {
            try {
                a.a(shareParams, this.listener);
                return;
            } catch (Throwable th) {
                this.listener.onError(this, 9, th);
                return;
            }
        }
        try {
            a.b(shareParams, this.listener);
        } catch (Throwable th2) {
            this.listener.onError(this, 9, th2);
        }
    }

    @Override // cn.sharesdk.framework.Platform
    protected HashMap<String, Object> filterFriendshipInfo(int i, HashMap<String, Object> hashMap) {
        Object obj;
        StringBuilder sb;
        String str;
        StringBuilder sb2;
        String str2;
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
        int parseInt = Integer.parseInt(String.valueOf(hashMap.get("current_cursor")));
        int parseInt2 = Integer.parseInt(String.valueOf(hashMap.get("total_number")));
        if (parseInt2 == 0 || (obj = hashMap.get("users")) == null) {
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
                hashMap4.put("icon", String.valueOf(hashMap3.get("avatar_hd")));
                if (String.valueOf(hashMap3.get("verified")).equals("true")) {
                    hashMap4.put("secretType", AmapLoc.RESULT_TYPE_WIFI_ONLY);
                } else {
                    hashMap4.put("secretType", AmapLoc.RESULT_TYPE_GPS);
                }
                hashMap4.put("secret", String.valueOf(hashMap3.get("verified_reason")));
                String valueOf = String.valueOf(hashMap3.get("gender"));
                if (valueOf.equals("m")) {
                    hashMap4.put("gender", AmapLoc.RESULT_TYPE_GPS);
                } else if (valueOf.equals("f")) {
                    hashMap4.put("gender", AmapLoc.RESULT_TYPE_WIFI_ONLY);
                } else {
                    hashMap4.put("gender", AmapLoc.RESULT_TYPE_FUSED);
                }
                hashMap4.put("snsUserUrl", "http://weibo.com/" + String.valueOf(hashMap3.get("profile_url")));
                hashMap4.put("resume", String.valueOf(hashMap3.get("description")));
                hashMap4.put("followerCount", String.valueOf(hashMap3.get("followers_count")));
                hashMap4.put("favouriteCount", String.valueOf(hashMap3.get("friends_count")));
                hashMap4.put("shareCount", String.valueOf(hashMap3.get("statuses_count")));
                hashMap4.put("snsregat", String.valueOf(ResHelper.dateToLong(String.valueOf(hashMap3.get("created_at")))));
                arrayList.add(hashMap4);
            }
        }
        if (arrayList.size() <= 0) {
            return null;
        }
        if (10 == i) {
            int intValue = ((Integer) hashMap.get("page_count")).intValue();
            int i2 = parseInt + 1;
            if (intValue * i2 >= parseInt2) {
                sb2 = new StringBuilder();
                sb2.append(parseInt);
                str2 = "_true";
            } else {
                sb2 = new StringBuilder();
                sb2.append(i2);
                str2 = "_false";
            }
            sb2.append(str2);
            hashMap2.put("nextCursor", sb2.toString());
        } else {
            int size = parseInt + arrayList.size();
            if (size >= parseInt2) {
                sb = new StringBuilder();
                sb.append(parseInt2);
                str = "_true";
            } else {
                sb = new StringBuilder();
                sb.append(size);
                str = "_false";
            }
            sb.append(str);
            hashMap2.put("nextCursor", sb.toString());
        }
        hashMap2.put("list", arrayList);
        return hashMap2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public f.a filterShareContent(Platform.ShareParams shareParams, HashMap<String, Object> hashMap) {
        f.a aVar = new f.a();
        aVar.b = shareParams.getText();
        if (hashMap != null) {
            aVar.a = String.valueOf(hashMap.get(ConnectionModel.ID));
            aVar.d.add(String.valueOf(hashMap.get("original_pic")));
            aVar.g = hashMap;
        }
        return aVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void follow(String str) {
        try {
            HashMap<String, Object> e = f.a(this).e(str);
            if (e == null) {
                if (this.listener != null) {
                    this.listener.onError(this, 6, new Throwable());
                }
            } else if (!e.containsKey("error_code") || ((Integer) e.get("error_code")).intValue() == 0) {
                if (this.listener != null) {
                    this.listener.onComplete(this, 6, e);
                }
            } else if (this.listener != null) {
                this.listener.onError(this, 6, new Throwable(new Hashon().fromHashMap(e)));
            }
        } catch (Throwable th) {
            this.listener.onError(this, 6, th);
        }
    }

    @Override // cn.sharesdk.framework.Platform
    protected HashMap<String, Object> getBilaterals(int i, int i2, String str) {
        if (TextUtils.isEmpty(str)) {
            str = this.db.getUserId();
        }
        if (TextUtils.isEmpty(str)) {
            str = this.db.get("nickname");
        }
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            HashMap<String, Object> c = f.a(this).c(i, i2, str);
            if (c == null || c.containsKey("error_code")) {
                return null;
            }
            c.put("page_count", Integer.valueOf(i));
            c.put("current_cursor", Integer.valueOf(i2));
            return filterFriendshipInfo(10, c);
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
            return null;
        }
    }

    @Override // cn.sharesdk.framework.Platform
    protected HashMap<String, Object> getFollowers(int i, int i2, String str) {
        if (TextUtils.isEmpty(str)) {
            str = this.db.getUserId();
        }
        if (TextUtils.isEmpty(str)) {
            str = this.db.get("nickname");
        }
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            HashMap<String, Object> d = f.a(this).d(i, i2, str);
            if (d == null || d.containsKey("error_code")) {
                return null;
            }
            d.put("current_cursor", Integer.valueOf(i2));
            return filterFriendshipInfo(11, d);
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
            return null;
        }
    }

    @Override // cn.sharesdk.framework.Platform
    protected HashMap<String, Object> getFollowings(int i, int i2, String str) {
        if (TextUtils.isEmpty(str)) {
            str = this.db.getUserId();
        }
        if (TextUtils.isEmpty(str)) {
            str = this.db.get("nickname");
        }
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            HashMap<String, Object> b = f.a(this).b(i, i2, str);
            if (b == null || b.containsKey("error_code")) {
                return null;
            }
            b.put("current_cursor", Integer.valueOf(i2));
            return filterFriendshipInfo(2, b);
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void getFriendList(int i, int i2, String str) {
        if (TextUtils.isEmpty(str)) {
            str = this.db.getUserId();
        }
        if (TextUtils.isEmpty(str)) {
            str = this.db.get("nickname");
        }
        if (TextUtils.isEmpty(str)) {
            if (this.listener != null) {
                this.listener.onError(this, 2, new RuntimeException("Both weibo id and screen_name are null"));
                return;
            }
            return;
        }
        try {
            HashMap<String, Object> b = f.a(this).b(i, i2, str);
            if (b == null) {
                if (this.listener != null) {
                    this.listener.onError(this, 2, new Throwable());
                }
            } else if (!b.containsKey("error_code") || ((Integer) b.get("error_code")).intValue() == 0) {
                if (this.listener != null) {
                    this.listener.onComplete(this, 2, b);
                }
            } else if (this.listener != null) {
                this.listener.onError(this, 2, new Throwable(new Hashon().fromHashMap(b)));
            }
        } catch (Throwable th) {
            this.listener.onError(this, 2, th);
        }
    }

    @Override // cn.sharesdk.framework.Platform
    public String getName() {
        return NAME;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public int getPlatformId() {
        return 1;
    }

    @Override // cn.sharesdk.framework.Platform
    public int getVersion() {
        return 1;
    }

    @Override // cn.sharesdk.framework.Platform
    public boolean hasShareCallback() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void initDevInfo(String str) {
        this.a = getDevinfo("AppKey");
        this.b = getDevinfo("AppSecret");
        this.c = getDevinfo("RedirectUrl");
        this.d = !Bugly.SDK_IS_DEV.equals(getDevinfo("ShareByAppClient"));
    }

    @Override // cn.sharesdk.framework.Platform
    public boolean isClientValid() {
        return f.a(this).b();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void setNetworkDevinfo() {
        this.a = getNetworkDevinfo("app_key", "AppKey");
        this.b = getNetworkDevinfo("app_secret", "AppSecret");
        this.c = getNetworkDevinfo("redirect_uri", "RedirectUrl");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void timeline(int i, int i2, String str) {
        if (TextUtils.isEmpty(str)) {
            str = this.db.getUserId();
        }
        if (TextUtils.isEmpty(str)) {
            str = this.db.get("nickname");
        }
        if (TextUtils.isEmpty(str)) {
            if (this.listener != null) {
                this.listener.onError(this, 7, new RuntimeException("Both weibo id and screen_name are null"));
                return;
            }
            return;
        }
        try {
            HashMap<String, Object> a = f.a(this).a(i, i2, str);
            if (a == null) {
                if (this.listener != null) {
                    this.listener.onError(this, 7, new Throwable());
                }
            } else if (!a.containsKey("error_code") || ((Integer) a.get("error_code")).intValue() == 0) {
                if (this.listener != null) {
                    this.listener.onComplete(this, 7, a);
                }
            } else if (this.listener != null) {
                this.listener.onError(this, 7, new Throwable(new Hashon().fromHashMap(a)));
            }
        } catch (Throwable th) {
            this.listener.onError(this, 7, th);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void userInfor(String str) {
        boolean z;
        if (TextUtils.isEmpty(str)) {
            str = this.db.getUserId();
            z = true;
        } else {
            z = false;
        }
        if (TextUtils.isEmpty(str)) {
            str = this.db.get("nickname");
            z = true;
        }
        if (TextUtils.isEmpty(str)) {
            if (this.listener != null) {
                this.listener.onError(this, 8, new RuntimeException("Both weibo id and screen_name are null"));
                return;
            }
            return;
        }
        try {
            HashMap<String, Object> d = f.a(this).d(str);
            if (d == null) {
                if (this.listener != null) {
                    this.listener.onError(this, 8, new Throwable());
                    return;
                }
                return;
            }
            if (d.containsKey("error_code") && ((Integer) d.get("error_code")).intValue() != 0) {
                if (this.listener != null) {
                    this.listener.onError(this, 8, new Throwable(new Hashon().fromHashMap(d)));
                    return;
                }
                return;
            }
            if (z) {
                this.db.putUserId(String.valueOf(d.get(ConnectionModel.ID)));
                this.db.put("nickname", String.valueOf(d.get("screen_name")));
                this.db.put("icon", String.valueOf(d.get("avatar_hd")));
                if (String.valueOf(d.get("verified")).equals("true")) {
                    this.db.put("secretType", AmapLoc.RESULT_TYPE_WIFI_ONLY);
                } else {
                    this.db.put("secretType", AmapLoc.RESULT_TYPE_GPS);
                }
                this.db.put("secret", String.valueOf(d.get("verified_reason")));
                String valueOf = String.valueOf(d.get("gender"));
                if (valueOf.equals("m")) {
                    this.db.put("gender", AmapLoc.RESULT_TYPE_GPS);
                } else if (valueOf.equals("f")) {
                    this.db.put("gender", AmapLoc.RESULT_TYPE_WIFI_ONLY);
                } else {
                    this.db.put("gender", AmapLoc.RESULT_TYPE_FUSED);
                }
                this.db.put("snsUserUrl", "http://weibo.com/" + String.valueOf(d.get("profile_url")));
                this.db.put("resume", String.valueOf(d.get("description")));
                this.db.put("followerCount", String.valueOf(d.get("followers_count")));
                this.db.put("favouriteCount", String.valueOf(d.get("friends_count")));
                this.db.put("shareCount", String.valueOf(d.get("statuses_count")));
                this.db.put("snsregat", String.valueOf(ResHelper.dateToLong(String.valueOf(d.get("created_at")))));
            }
            if (this.listener != null) {
                this.listener.onComplete(this, 8, d);
            }
        } catch (Throwable th) {
            this.listener.onError(this, 8, th);
        }
    }
}
