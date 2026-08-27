package cn.sharesdk.wechat.utils;

import android.os.Bundle;

/* compiled from: WechatReq.java */
/* loaded from: classes.dex */
public abstract class l {
    public String d;

    public abstract int a();

    public void a(Bundle bundle) {
        bundle.putInt("_wxapi_command_type", a());
        bundle.putString("_wxapi_basereq_transaction", this.d);
    }

    public abstract boolean a(boolean z);
}
