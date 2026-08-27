package cn.sharesdk.wechat.utils;

import android.os.Bundle;
import android.text.TextUtils;
import cn.sharesdk.wechat.utils.WXMediaMessage;
import java.io.File;

/* loaded from: classes.dex */
public class WXEmojiObject implements WXMediaMessage.IMediaObject {
    public byte[] emojiData;
    public String emojiPath;

    public WXEmojiObject() {
    }

    public WXEmojiObject(String str) {
        this.emojiPath = str;
    }

    public WXEmojiObject(byte[] bArr) {
        this.emojiData = bArr;
    }

    @Override // cn.sharesdk.wechat.utils.WXMediaMessage.IMediaObject
    public boolean checkArgs() {
        if ((this.emojiData == null || this.emojiData.length == 0) && TextUtils.isEmpty(this.emojiPath)) {
            cn.sharesdk.framework.utils.e.b().d("MicroMsg.SDK.WXEmojiObject", "checkArgs fail, both arguments is null");
            return false;
        }
        if (this.emojiData != null && this.emojiData.length > 10485760) {
            cn.sharesdk.framework.utils.e.b().d("MicroMsg.SDK.WXEmojiObject", "checkArgs fail, emojiData is too large");
            return false;
        }
        if (this.emojiPath != null) {
            File file = new File(this.emojiPath);
            if (!file.exists()) {
                cn.sharesdk.framework.utils.e.b().d("MicroMsg.SDK.WXEmojiObject", "checkArgs fail, emojiPath not found");
                return false;
            }
            if (file.length() > 10485760) {
                cn.sharesdk.framework.utils.e.b().d("MicroMsg.SDK.WXEmojiObject", "checkArgs fail, emojiSize is too large");
                return false;
            }
        }
        return true;
    }

    @Override // cn.sharesdk.wechat.utils.WXMediaMessage.IMediaObject
    public void serialize(Bundle bundle) {
        bundle.putByteArray("_wxemojiobject_emojiData", this.emojiData);
        bundle.putString("_wxemojiobject_emojiPath", this.emojiPath);
    }

    public void setEmojiData(byte[] bArr) {
        this.emojiData = bArr;
    }

    public void setEmojiPath(String str) {
        this.emojiPath = str;
    }

    @Override // cn.sharesdk.wechat.utils.WXMediaMessage.IMediaObject
    public int type() {
        return 8;
    }

    @Override // cn.sharesdk.wechat.utils.WXMediaMessage.IMediaObject
    public void unserialize(Bundle bundle) {
        this.emojiData = bundle.getByteArray("_wxemojiobject_emojiData");
        this.emojiPath = bundle.getString("_wxemojiobject_emojiPath");
    }
}
