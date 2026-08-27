package cn.sharesdk.wechat.utils;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import java.io.ByteArrayOutputStream;

/* loaded from: classes.dex */
public final class WXMediaMessage {
    public static final String ACTION_WXAPPMESSAGE = "com.tencent.mm.sdk.openapi.Intent.ACTION_WXAPPMESSAGE";
    public String description;
    public IMediaObject mediaObject;
    public String openId;
    public int sdkVer;
    public byte[] thumbData;
    public String title;

    /* loaded from: classes.dex */
    public interface IMediaObject {
        public static final int TYPE_APPDATA = 7;
        public static final int TYPE_EMOJI = 8;
        public static final int TYPE_FILE = 6;
        public static final int TYPE_IMAGE = 2;
        public static final int TYPE_MINIPROGRAM = 9;
        public static final int TYPE_MUSIC = 3;
        public static final int TYPE_TEXT = 1;
        public static final int TYPE_UNKNOWN = 0;
        public static final int TYPE_URL = 5;
        public static final int TYPE_VIDEO = 4;

        boolean checkArgs();

        void serialize(Bundle bundle);

        int type();

        void unserialize(Bundle bundle);
    }

    /* loaded from: classes.dex */
    public static class a {
        public static Bundle a(WXMediaMessage wXMediaMessage) {
            Bundle bundle = new Bundle();
            bundle.putInt("_wxobject_sdkVer", wXMediaMessage.sdkVer);
            bundle.putString("_wxobject_title", wXMediaMessage.title);
            bundle.putString("_wxobject_description", wXMediaMessage.description);
            bundle.putByteArray("_wxobject_thumbdata", wXMediaMessage.thumbData);
            if (wXMediaMessage.mediaObject != null) {
                bundle.putString("_wxobject_identifier_", "com.tencent.mm.sdk.openapi." + wXMediaMessage.mediaObject.getClass().getSimpleName());
                wXMediaMessage.mediaObject.serialize(bundle);
            }
            return bundle;
        }

        public static WXMediaMessage a(Bundle bundle) {
            String str;
            WXMediaMessage wXMediaMessage = new WXMediaMessage();
            wXMediaMessage.sdkVer = bundle.getInt("_wxobject_sdkVer");
            wXMediaMessage.title = bundle.getString("_wxobject_title");
            wXMediaMessage.description = bundle.getString("_wxobject_description");
            wXMediaMessage.thumbData = bundle.getByteArray("_wxobject_thumbdata");
            wXMediaMessage.openId = bundle.getString(TextUtils.isEmpty(bundle.getString("_wxapi_basereq_openid")) ? "_wxapi_baseresp_openId" : "_wxapi_basereq_openid");
            String string = bundle.getString("_wxobject_identifier_");
            if (string == null || string.length() <= 0) {
                return wXMediaMessage;
            }
            try {
                str = string.replace("com.tencent.mm.sdk.openapi", "cn.sharesdk.wechat.utils");
            } catch (Exception e) {
                e = e;
                str = string;
            }
            try {
                wXMediaMessage.mediaObject = (IMediaObject) Class.forName(str).newInstance();
                wXMediaMessage.mediaObject.unserialize(bundle);
                return wXMediaMessage;
            } catch (Exception e2) {
                e = e2;
                cn.sharesdk.framework.utils.e.b().d(e);
                cn.sharesdk.framework.utils.e.b().d("get media object from bundle failed: unknown ident " + str, new Object[0]);
                return wXMediaMessage;
            }
        }
    }

    public WXMediaMessage() {
        this(null);
    }

    public WXMediaMessage(IMediaObject iMediaObject) {
        this.mediaObject = iMediaObject;
    }

    public final int getType() {
        if (this.mediaObject == null) {
            return 0;
        }
        return this.mediaObject.type();
    }

    public final void setThumbImage(Bitmap bitmap) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, byteArrayOutputStream);
            this.thumbData = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
        } catch (Exception e) {
            cn.sharesdk.framework.utils.e.b().d(e);
            cn.sharesdk.framework.utils.e.b().d("put thumb failed", new Object[0]);
        }
    }
}
