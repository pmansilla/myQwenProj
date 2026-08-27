package cn.sharesdk.wechat.utils;

import android.os.Bundle;
import cn.sharesdk.wechat.utils.WXMediaMessage;

/* compiled from: SendMessageReq.java */
/* loaded from: classes.dex */
public class d extends l {
    public WXMediaMessage a;
    public int b;

    @Override // cn.sharesdk.wechat.utils.l
    public int a() {
        return 2;
    }

    @Override // cn.sharesdk.wechat.utils.l
    public void a(Bundle bundle) {
        super.a(bundle);
        bundle.putAll(WXMediaMessage.a.a(this.a));
        bundle.putInt("_wxapi_sendmessagetowx_req_scene", this.b);
    }

    @Override // cn.sharesdk.wechat.utils.l
    public boolean a(boolean z) {
        if (this.a.getType() == 8 && (this.a.thumbData == null || this.a.thumbData.length <= 0)) {
            cn.sharesdk.framework.utils.e.b().d("checkArgs fail, thumbData should not be null when send emoji", new Object[0]);
            return false;
        }
        if (z) {
            if (this.a.thumbData != null && this.a.thumbData.length > 131072) {
                cn.sharesdk.framework.utils.e.b().d("checkArgs fail, thumbData is invalid", new Object[0]);
                return false;
            }
        } else if (this.a.thumbData != null && this.a.thumbData.length > 32768) {
            cn.sharesdk.framework.utils.e.b().d("checkArgs fail, thumbData is invalid", new Object[0]);
            return false;
        }
        if (this.a.title != null && this.a.title.length() > 512) {
            cn.sharesdk.framework.utils.e.b().d("checkArgs fail, title is invalid", new Object[0]);
            return false;
        }
        if (this.a.description != null && this.a.description.length() > 1024) {
            this.a.description = this.a.description.substring(0, 1021) + "...";
        }
        if (this.a.mediaObject != null) {
            return this.a.mediaObject.checkArgs();
        }
        cn.sharesdk.framework.utils.e.b().d("checkArgs fail, mediaObject is null", new Object[0]);
        return false;
    }
}
