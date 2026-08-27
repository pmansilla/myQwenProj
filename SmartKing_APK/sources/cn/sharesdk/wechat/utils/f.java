package cn.sharesdk.wechat.utils;

import android.os.Bundle;
import cn.sharesdk.wechat.utils.WXMediaMessage;

/* compiled from: ShowMessageFromWechatResp.java */
/* loaded from: classes.dex */
public class f extends WechatResp {
    public WXMediaMessage a;

    public f(Bundle bundle) {
        super(bundle);
    }

    @Override // cn.sharesdk.wechat.utils.WechatResp
    public int a() {
        return 4;
    }

    @Override // cn.sharesdk.wechat.utils.WechatResp
    public void a(Bundle bundle) {
        super.a(bundle);
        this.a = WXMediaMessage.a.a(bundle);
    }

    @Override // cn.sharesdk.wechat.utils.WechatResp
    public void b(Bundle bundle) {
        super.b(bundle);
        bundle.putAll(WXMediaMessage.a.a(this.a));
    }
}
