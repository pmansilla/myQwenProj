package cn.sharesdk.wechat.utils;

import android.os.Bundle;
import cn.sharesdk.wechat.utils.WXMediaMessage;

/* compiled from: GetMessageFromWechatResp.java */
/* loaded from: classes.dex */
public class c extends WechatResp {
    public WXMediaMessage a;

    public c(Bundle bundle) {
        super(bundle);
    }

    @Override // cn.sharesdk.wechat.utils.WechatResp
    public int a() {
        return 3;
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
