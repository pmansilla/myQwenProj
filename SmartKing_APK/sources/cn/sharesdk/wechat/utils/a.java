package cn.sharesdk.wechat.utils;

import android.os.Bundle;

/* compiled from: AuthReq.java */
/* loaded from: classes.dex */
public class a extends l {
    public String a;
    public String b;

    @Override // cn.sharesdk.wechat.utils.l
    public int a() {
        return 1;
    }

    @Override // cn.sharesdk.wechat.utils.l
    public void a(Bundle bundle) {
        super.a(bundle);
        bundle.putString("_wxapi_sendauth_req_scope", this.a);
        bundle.putString("_wxapi_sendauth_req_state", this.b);
    }

    @Override // cn.sharesdk.wechat.utils.l
    public boolean a(boolean z) {
        if (this.a == null || this.a.length() == 0 || this.a.length() > 1024) {
            cn.sharesdk.framework.utils.e.b().d("MicroMsg.SDK.SendAuth.Req", "checkArgs fail, scope is invalid");
            return false;
        }
        if (this.b == null || this.b.length() <= 1024) {
            return true;
        }
        cn.sharesdk.framework.utils.e.b().d("MicroMsg.SDK.SendAuth.Req", "checkArgs fail, state is invalid");
        return false;
    }
}
