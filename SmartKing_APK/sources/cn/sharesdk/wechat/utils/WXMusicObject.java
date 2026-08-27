package cn.sharesdk.wechat.utils;

import android.os.Bundle;
import android.text.TextUtils;
import cn.sharesdk.wechat.utils.WXMediaMessage;

/* loaded from: classes.dex */
public class WXMusicObject implements WXMediaMessage.IMediaObject {
    public String musicDataUrl;
    public String musicLowBandDataUrl;
    public String musicLowBandUrl;
    public String musicUrl;

    @Override // cn.sharesdk.wechat.utils.WXMediaMessage.IMediaObject
    public boolean checkArgs() {
        if (TextUtils.isEmpty(this.musicUrl) && TextUtils.isEmpty(this.musicLowBandUrl)) {
            cn.sharesdk.framework.utils.e.b().d("both musicUrl and musicLowBandUrl are null", new Object[0]);
            return false;
        }
        if (this.musicUrl != null && this.musicUrl.length() > 10240) {
            cn.sharesdk.framework.utils.e.b().d("checkArgs fail, musicUrl is too long", new Object[0]);
            return false;
        }
        if (this.musicLowBandUrl == null || this.musicLowBandUrl.length() <= 10240) {
            return true;
        }
        cn.sharesdk.framework.utils.e.b().d("checkArgs fail, musicLowBandUrl is too long", new Object[0]);
        return false;
    }

    @Override // cn.sharesdk.wechat.utils.WXMediaMessage.IMediaObject
    public void serialize(Bundle bundle) {
        bundle.putString("_wxmusicobject_musicUrl", this.musicUrl);
        bundle.putString("_wxmusicobject_musicLowBandUrl", this.musicLowBandUrl);
        bundle.putString("_wxmusicobject_musicDataUrl", this.musicDataUrl);
        bundle.putString("_wxmusicobject_musicLowBandDataUrl", this.musicLowBandDataUrl);
    }

    @Override // cn.sharesdk.wechat.utils.WXMediaMessage.IMediaObject
    public int type() {
        return 3;
    }

    @Override // cn.sharesdk.wechat.utils.WXMediaMessage.IMediaObject
    public void unserialize(Bundle bundle) {
        this.musicUrl = bundle.getString("_wxmusicobject_musicUrl");
        this.musicLowBandUrl = bundle.getString("_wxmusicobject_musicLowBandUrl");
        this.musicDataUrl = bundle.getString("_wxmusicobject_musicDataUrl");
        this.musicLowBandDataUrl = bundle.getString("_wxmusicobject_musicLowBandDataUrl");
    }
}
