package cn.sharesdk.wechat.utils;

import android.os.Bundle;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import com.mob.tools.utils.Hashon;
import java.util.HashMap;

/* compiled from: WechatHandler.java */
/* loaded from: classes.dex */
public class j {
    private Platform a;
    private Platform.ShareParams b;
    private PlatformActionListener c;
    private AuthorizeListener d;
    private g e;

    public j(Platform platform) {
        this.a = platform;
    }

    public Platform.ShareParams a() {
        return this.b;
    }

    public void a(Platform.ShareParams shareParams, PlatformActionListener platformActionListener) {
        this.b = shareParams;
        this.c = platformActionListener;
    }

    public void a(AuthorizeListener authorizeListener) {
        this.d = authorizeListener;
    }

    public void a(WechatResp wechatResp) {
        int i = wechatResp.f;
        if (i == 0) {
            switch (wechatResp.a()) {
                case 1:
                    if (this.d != null) {
                        Bundle bundle = new Bundle();
                        wechatResp.b(bundle);
                        this.e.a(bundle, this.d);
                        return;
                    }
                    return;
                case 2:
                    if (this.c != null) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("ShareParams", this.b);
                        this.c.onComplete(this.a, 9, hashMap);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
        switch (i) {
            case -4:
                HashMap hashMap2 = new HashMap();
                hashMap2.put("errCode", Integer.valueOf(wechatResp.f));
                hashMap2.put("errStr", wechatResp.g);
                hashMap2.put("transaction", wechatResp.h);
                Throwable th = new Throwable(new Hashon().fromHashMap(hashMap2));
                if (wechatResp.a() == 1 && this.d != null) {
                    this.d.onError(th);
                    return;
                }
                return;
            case -3:
                HashMap hashMap3 = new HashMap();
                hashMap3.put("errCode", Integer.valueOf(wechatResp.f));
                hashMap3.put("errStr", wechatResp.g);
                hashMap3.put("transaction", wechatResp.h);
                Throwable th2 = new Throwable(new Hashon().fromHashMap(hashMap3));
                switch (wechatResp.a()) {
                    case 1:
                        if (this.d != null) {
                            this.d.onError(th2);
                            return;
                        }
                        return;
                    case 2:
                        if (this.c != null) {
                            this.c.onError(this.a, 9, th2);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            case -2:
                switch (wechatResp.a()) {
                    case 1:
                        if (this.d != null) {
                            this.d.onCancel();
                            return;
                        }
                        return;
                    case 2:
                        if (this.c != null) {
                            this.c.onCancel(this.a, 9);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            default:
                HashMap hashMap4 = new HashMap();
                hashMap4.put("req", wechatResp.getClass().getSimpleName());
                hashMap4.put("errCode", Integer.valueOf(wechatResp.f));
                hashMap4.put("errStr", wechatResp.g);
                hashMap4.put("transaction", wechatResp.h);
                Throwable th3 = new Throwable(new Hashon().fromHashMap(hashMap4));
                if (this.c != null) {
                    this.c.onError(this.a, 9, th3);
                }
                th3.printStackTrace();
                return;
        }
    }

    public void a(g gVar) {
        this.e = gVar;
    }

    public Platform b() {
        return this.a;
    }

    public PlatformActionListener c() {
        return this.c;
    }
}
