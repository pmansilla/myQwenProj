package cn.sharesdk.framework;

import android.graphics.Bitmap;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.ResHelper;
import com.tencent.bugly.Bugly;
import java.lang.reflect.Field;
import java.util.HashMap;

/* compiled from: PlatformImpl.java */
/* loaded from: classes.dex */
public class c {
    private Platform a;
    private PlatformDb b;
    private a c;
    private int d;
    private int e;
    private boolean f;
    private boolean g = true;
    private boolean h;

    public c(Platform platform) {
        this.a = platform;
        String name = platform.getName();
        this.b = new PlatformDb(name, platform.getVersion());
        a(name);
        this.c = new a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean j() {
        if (ShareSDK.a()) {
            String a = a(this.a.getPlatformId(), "covert_url", (String) null);
            if (a != null) {
                a.trim();
            }
            this.g = !Bugly.SDK_IS_DEV.equals(a);
            this.a.setNetworkDevinfo();
            return true;
        }
        try {
            if (!ShareSDK.b()) {
                return false;
            }
            String a2 = a(this.a.getPlatformId(), "covert_url", (String) null);
            if (a2 != null) {
                a2.trim();
            }
            this.g = !Bugly.SDK_IS_DEV.equals(a2);
            this.a.setNetworkDevinfo();
            return true;
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().w(th);
            return false;
        }
    }

    private String k() {
        StringBuilder sb = new StringBuilder();
        try {
            if ("TencentWeibo".equals(this.a.getName())) {
                cn.sharesdk.framework.utils.e.b().i("user id %s ==>>", g().getUserName());
                sb.append(Data.urlEncode(g().getUserName(), "utf-8"));
            } else {
                sb.append(Data.urlEncode(g().getUserId(), "utf-8"));
            }
            sb.append("|");
            sb.append(Data.urlEncode(g().get("secretType"), "utf-8"));
            sb.append("|");
            sb.append(Data.urlEncode(g().get("gender"), "utf-8"));
            sb.append("|");
            sb.append(Data.urlEncode(g().get("birthday"), "utf-8"));
            sb.append("|");
            sb.append(Data.urlEncode(g().get("educationJSONArrayStr"), "utf-8"));
            sb.append("|");
            sb.append(Data.urlEncode(g().get("workJSONArrayStr"), "utf-8"));
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().w(th);
        }
        return sb.toString();
    }

    public int a() {
        return this.d;
    }

    public String a(int i, String str, String str2) {
        String a = ShareSDK.a(i, str);
        return (TextUtils.isEmpty(a) || "null".equals(a)) ? this.a.getDevinfo(this.a.getName(), str2) : a;
    }

    public String a(Bitmap bitmap) {
        return ShareSDK.a(bitmap);
    }

    public String a(String str, boolean z) {
        long currentTimeMillis = System.currentTimeMillis();
        if (!this.g) {
            cn.sharesdk.framework.utils.e.b().i("getShortLintk use time: " + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
            return str;
        }
        if (TextUtils.isEmpty(str)) {
            cn.sharesdk.framework.utils.e.b().i("getShortLintk use time: " + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
            return str;
        }
        String a = ShareSDK.a(str, z, this.a.getPlatformId(), k());
        cn.sharesdk.framework.utils.e.b().i("getShortLintk use time: " + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
        return a;
    }

    public void a(int i, int i2, String str) {
        c(2, new Object[]{Integer.valueOf(i), Integer.valueOf(i2), str});
    }

    public void a(int i, Object obj) {
        this.c.a(this.a, i, obj);
    }

    public void a(Platform.ShareParams shareParams) {
        if (shareParams == null) {
            if (this.c != null) {
                this.c.onError(this.a, 9, new NullPointerException());
            }
        } else {
            try {
                if (!shareParams.getOpenCustomEven()) {
                    ShareSDK.logDemoEvent(3, this.a);
                }
            } catch (Throwable unused) {
            }
            c(9, shareParams);
        }
    }

    public void a(PlatformActionListener platformActionListener) {
        this.c.a(platformActionListener);
    }

    public void a(String str) {
        try {
            this.d = ResHelper.parseInt(String.valueOf(ShareSDK.b(str, "Id")).trim());
        } catch (Throwable unused) {
            if (!(this.a instanceof CustomPlatform)) {
                cn.sharesdk.framework.utils.e.b().d(this.a.getName() + " failed to parse Id, this will cause method getId() always returens 0", new Object[0]);
            }
        }
        try {
            this.e = ResHelper.parseInt(String.valueOf(ShareSDK.b(str, "SortId")).trim());
        } catch (Throwable unused2) {
            if (!(this.a instanceof CustomPlatform)) {
                cn.sharesdk.framework.utils.e.b().d(this.a.getName() + " failed to parse SortId, this won't cause any problem, don't worry", new Object[0]);
            }
        }
        String b = ShareSDK.b(str, "Enable");
        if (b == null) {
            this.h = true;
            if (!(this.a instanceof CustomPlatform)) {
                cn.sharesdk.framework.utils.e.b().d(this.a.getName() + " failed to parse Enable, this will cause platform always be enable", new Object[0]);
            }
        } else {
            this.h = "true".equals(b.trim());
        }
        this.a.initDevInfo(str);
    }

    public void a(String str, int i, int i2) {
        c(7, new Object[]{Integer.valueOf(i), Integer.valueOf(i2), str});
    }

    public void a(String str, String str2, short s, HashMap<String, Object> hashMap, HashMap<String, String> hashMap2) {
        c(s | 655360, new Object[]{str, str2, hashMap, hashMap2});
    }

    public void a(boolean z) {
        this.f = z;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [cn.sharesdk.framework.c$2] */
    public void a(final String[] strArr) {
        new Thread() { // from class: cn.sharesdk.framework.c.2
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    c.this.j();
                    c.this.a.doAuthorize(strArr);
                } catch (Throwable th) {
                    cn.sharesdk.framework.utils.e.b().w(th);
                }
            }
        }.start();
    }

    public int b() {
        return this.e;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void b(int i, Object obj) {
        Object obj2;
        switch (i) {
            case 1:
                if (this.c != null) {
                    this.c.onComplete(this.a, 1, null);
                    return;
                }
                return;
            case 2:
                Object[] objArr = (Object[]) obj;
                this.a.getFriendList(((Integer) objArr[0]).intValue(), ((Integer) objArr[1]).intValue(), (String) objArr[2]);
                return;
            case 3:
            case 4:
            case 5:
            default:
                Object[] objArr2 = (Object[]) obj;
                this.a.doCustomerProtocol(String.valueOf(objArr2[0]), String.valueOf(objArr2[1]), i, (HashMap) objArr2[2], (HashMap) objArr2[3]);
                return;
            case 6:
                this.a.follow((String) obj);
                return;
            case 7:
                Object[] objArr3 = (Object[]) obj;
                this.a.timeline(((Integer) objArr3[0]).intValue(), ((Integer) objArr3[1]).intValue(), (String) objArr3[2]);
                return;
            case 8:
                this.a.userInfor(obj != null ? (String) obj : null);
                return;
            case 9:
                Platform.ShareParams shareParams = (Platform.ShareParams) obj;
                HashMap<String, Object> map = shareParams.toMap();
                for (Field field : shareParams.getClass().getFields()) {
                    if (map.get(field.getName()) == null) {
                        field.setAccessible(true);
                        try {
                            obj2 = field.get(shareParams);
                        } catch (Throwable th) {
                            cn.sharesdk.framework.utils.e.b().w(th);
                            obj2 = null;
                        }
                        if (obj2 != null) {
                            map.put(field.getName(), obj2);
                        }
                    }
                }
                if (this.c instanceof a) {
                    this.c.a(this.a, shareParams);
                }
                this.a.doShare(shareParams);
                return;
        }
    }

    public void b(String str) {
        c(6, str);
    }

    public PlatformActionListener c() {
        return this.c.a();
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [cn.sharesdk.framework.c$1] */
    protected void c(final int i, final Object obj) {
        new Thread() { // from class: cn.sharesdk.framework.c.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    c.this.j();
                    if (c.this.a.checkAuthorize(i, obj)) {
                        c.this.b(i, obj);
                    }
                } catch (Throwable th) {
                    cn.sharesdk.framework.utils.e.b().w(th);
                }
            }
        }.start();
    }

    public void c(String str) {
        c(8, str);
    }

    public String d(String str) {
        return ShareSDK.a(str);
    }

    public boolean d() {
        return this.b.isValid();
    }

    public boolean e() {
        return this.f;
    }

    public boolean f() {
        return this.h;
    }

    public PlatformDb g() {
        return this.b;
    }

    public void h() {
        this.b.removeAccount();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public PlatformActionListener i() {
        return this.c;
    }
}
