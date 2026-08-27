package cn.sharesdk.framework;

import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.b.b.f;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.Hashon;
import com.sun.mail.imap.IMAPStore;
import java.util.HashMap;

/* compiled from: InnerPlatformActionListener.java */
/* loaded from: classes.dex */
public class a implements PlatformActionListener {
    private PlatformActionListener a;
    private HashMap<Platform, Platform.ShareParams> b = new HashMap<>();
    private int c;

    /* JADX INFO: Access modifiers changed from: private */
    public String a(Platform platform) {
        try {
            try {
                return a(platform.getDb(), new String[]{"nickname", "icon", "gender", "snsUserUrl", "resume", "secretType", "secret", "birthday", "followerCount", "favouriteCount", "shareCount", "snsregat", "snsUserLevel", "educationJSONArrayStr", "workJSONArrayStr"});
            } catch (Throwable th) {
                th = th;
                cn.sharesdk.framework.utils.e.b().w(th);
                return null;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private String a(PlatformDb platformDb, String[] strArr) throws Throwable {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        int i = 0;
        for (String str : strArr) {
            if (i > 0) {
                sb2.append('|');
                sb.append('|');
            }
            i++;
            String str2 = platformDb.get(str);
            if (!TextUtils.isEmpty(str2)) {
                sb.append(str2);
                sb2.append(Data.urlEncode(str2, "utf-8"));
            }
        }
        cn.sharesdk.framework.utils.e.b().i("======UserData: " + sb.toString(), new Object[0]);
        return sb2.toString();
    }

    private void a(Platform platform, final int i, final HashMap<String, Object> hashMap) {
        if (cn.sharesdk.framework.authorize.e.c().b() == null && ShareSDK.getEnableAuthTag()) {
            b();
        }
        final PlatformActionListener platformActionListener = this.a;
        this.a = new PlatformActionListener() { // from class: cn.sharesdk.framework.a.2
            @Override // cn.sharesdk.framework.PlatformActionListener
            public void onCancel(Platform platform2, int i2) {
                a.this.a = platformActionListener;
                if (a.this.a != null) {
                    a.this.a.onComplete(platform2, i, hashMap);
                }
            }

            @Override // cn.sharesdk.framework.PlatformActionListener
            public void onComplete(Platform platform2, int i2, HashMap<String, Object> hashMap2) {
                a.this.a = platformActionListener;
                if (a.this.a != null) {
                    a.this.a.onComplete(platform2, i, hashMap);
                }
                cn.sharesdk.framework.b.b.b bVar = new cn.sharesdk.framework.b.b.b();
                bVar.a = platform2.getPlatformId();
                bVar.b = "TencentWeibo".equals(platform2.getName()) ? platform2.getDb().get(IMAPStore.ID_NAME) : platform2.getDb().getUserId();
                bVar.c = new Hashon().fromHashMap(hashMap2);
                bVar.d = a.this.a(platform2);
                cn.sharesdk.framework.b.d a = cn.sharesdk.framework.b.d.a();
                if (a != null) {
                    a.a(bVar);
                }
            }

            @Override // cn.sharesdk.framework.PlatformActionListener
            public void onError(Platform platform2, int i2, Throwable th) {
                cn.sharesdk.framework.utils.e.b().w(th);
                a.this.a = platformActionListener;
                if (a.this.a != null) {
                    a.this.a.onComplete(platform2, i, hashMap);
                }
            }
        };
        platform.showUser(null);
    }

    private String b(Platform platform) {
        Platform platform2;
        PlatformDb db = platform.getDb();
        if ((WechatMoments.NAME.equals(platform.getName()) || "WechatFavorite".equals(platform.getName())) && TextUtils.isEmpty(db.getUserGender()) && (platform2 = ShareSDK.getPlatform(Wechat.NAME)) != null) {
            db = platform2.getDb();
        }
        try {
            return a(db, new String[]{"gender", "birthday", "secretType", "educationJSONArrayStr", "workJSONArrayStr"});
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().w(th);
            return null;
        }
    }

    private void b() {
        new Thread(new Runnable() { // from class: cn.sharesdk.framework.a.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    cn.sharesdk.framework.authorize.e.c().a(2, 2);
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }).start();
    }

    private void b(Platform platform, int i, HashMap<String, Object> hashMap) {
        HashMap<String, Object> hashMap2;
        Platform.ShareParams remove = this.b.remove(platform);
        if (hashMap != null) {
            remove = (Platform.ShareParams) hashMap.remove("ShareParams");
        }
        try {
            hashMap2 = (HashMap) hashMap.clone();
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
            hashMap2 = hashMap;
        }
        if (remove != null) {
            cn.sharesdk.framework.b.b.f fVar = new cn.sharesdk.framework.b.b.f();
            fVar.n = remove.getCustomFlag();
            String userId = platform.getDb().getUserId();
            if ((WechatMoments.NAME.equals(platform.getName()) || "WechatFavorite".equals(platform.getName())) && TextUtils.isEmpty(userId)) {
                Platform platform2 = ShareSDK.getPlatform(Wechat.NAME);
                if (platform2 != null) {
                    userId = platform2.getDb().getUserId();
                }
            } else if ("TencentWeibo".equals(platform.getName())) {
                userId = platform.getDb().get(IMAPStore.ID_NAME);
            }
            fVar.b = userId;
            fVar.a = platform.getPlatformId();
            f.a filterShareContent = platform.filterShareContent(remove, hashMap2);
            if (filterShareContent != null) {
                fVar.c = filterShareContent.a;
                fVar.d = filterShareContent;
            }
            if (platform != null) {
                fVar.m = b(platform);
            }
            cn.sharesdk.framework.b.d a = cn.sharesdk.framework.b.d.a();
            if (a != null) {
                a.a(fVar);
            }
        }
        if (this.a != null) {
            try {
                this.a.onComplete(platform, i, hashMap);
                this.a = null;
                this.c = 0;
            } catch (Throwable th2) {
                cn.sharesdk.framework.utils.e.b().d(th2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PlatformActionListener a() {
        return this.a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Platform platform, final int i, final Object obj) {
        if (cn.sharesdk.framework.authorize.e.c().b() == null && ShareSDK.getEnableAuthTag()) {
            b();
        }
        this.c = i;
        final PlatformActionListener platformActionListener = this.a;
        this.a = new PlatformActionListener() { // from class: cn.sharesdk.framework.a.3
            @Override // cn.sharesdk.framework.PlatformActionListener
            public void onCancel(Platform platform2, int i2) {
                a.this.a = platformActionListener;
                if (a.this.a != null) {
                    a.this.a.onCancel(platform2, i);
                }
            }

            @Override // cn.sharesdk.framework.PlatformActionListener
            public void onComplete(Platform platform2, int i2, HashMap<String, Object> hashMap) {
                if (ShareSDK.getEnableAuthTag()) {
                    String fromHashMap = new Hashon().fromHashMap(cn.sharesdk.framework.authorize.e.c().b());
                    if (!TextUtils.isEmpty(fromHashMap)) {
                        platform2.getDb().put("userTags", fromHashMap);
                    }
                }
                a.this.a = platformActionListener;
                platform2.afterRegister(i, obj);
            }

            @Override // cn.sharesdk.framework.PlatformActionListener
            public void onError(Platform platform2, int i2, Throwable th) {
                a.this.a = platformActionListener;
                if (a.this.a != null) {
                    a.this.a.onError(platform2, i2, th);
                }
            }
        };
        platform.doAuthorize(null);
    }

    public void a(Platform platform, Platform.ShareParams shareParams) {
        this.b.put(platform, shareParams);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(PlatformActionListener platformActionListener) {
        this.a = platformActionListener;
    }

    @Override // cn.sharesdk.framework.PlatformActionListener
    public void onCancel(Platform platform, int i) {
        if (this.a != null) {
            this.a.onCancel(platform, i);
            this.a = null;
            this.c = 0;
        }
    }

    @Override // cn.sharesdk.framework.PlatformActionListener
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (platform instanceof CustomPlatform) {
            if (this.a != null) {
                this.a.onComplete(platform, i, hashMap);
                this.a = null;
                this.c = 0;
                return;
            }
            return;
        }
        if (i == 1) {
            a(platform, i, hashMap);
            return;
        }
        if (i == 9) {
            b(platform, i, hashMap);
            return;
        }
        if (this.a != null) {
            this.a.onComplete(platform, i, hashMap);
            if (Wechat.NAME.equals(platform.getName())) {
                return;
            }
            if (this.c == 0 || this.c == i) {
                this.a = null;
                this.c = 0;
            }
        }
    }

    @Override // cn.sharesdk.framework.PlatformActionListener
    public void onError(Platform platform, int i, Throwable th) {
        if (this.a != null) {
            this.a.onError(platform, i, th);
            this.a = null;
            this.c = 0;
        }
    }
}
