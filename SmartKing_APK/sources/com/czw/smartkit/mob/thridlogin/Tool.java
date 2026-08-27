package com.czw.smartkit.mob.thridlogin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import cn.sharesdk.whatsapp.WhatsApp;

/* loaded from: classes.dex */
public class Tool {
    public static String actionToString(int i) {
        switch (i) {
            case 1:
                return "ACTION_AUTHORIZING";
            case 2:
                return "ACTION_GETTING_FRIEND_LIST";
            case 3:
            case 4:
            default:
                return "UNKNOWN";
            case 5:
                return "ACTION_SENDING_DIRECT_MESSAGE";
            case 6:
                return "ACTION_FOLLOWING_USER";
            case 7:
                return "ACTION_TIMELINE";
            case 8:
                return "ACTION_USER_INFOR";
            case 9:
                return "ACTION_SHARE";
        }
    }

    public static boolean canGetUserInfo(Platform platform) {
        if (platform == null) {
            return false;
        }
        String name = platform.getName();
        return ((Wechat.NAME.equals(name) && !platform.isClientValid()) || WechatMoments.NAME.equals(name) || "WechatFavorite".equals(name) || "ShortMessage".equals(name) || "Email".equals(name) || "Telegram".equals(name) || "Pinterest".equals(name) || "Yixin".equals(name) || "YixinMoments".equals(name) || "Bluetooth".equals(name) || WhatsApp.NAME.equals(name) || "Pocket".equals(name) || "BaiduTieba".equals(name) || "Laiwang".equals(name) || "LaiwangMoments".equals(name) || "Alipay".equals(name) || "AlipayMoments".equals(name) || "FacebookMessenger".equals(name) || "Dingding".equals(name) || "Youtube".equals(name) || "Meipai".equals(name)) ? false : true;
    }

    public static Bitmap compressImageFromFile(String str) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        options.inJustDecodeBounds = false;
        int i = options.outWidth;
        int i2 = options.outHeight;
        int i3 = (i <= i2 || ((float) i) <= 480.0f) ? (i >= i2 || ((float) i2) <= 800.0f) ? 1 : (int) (options.outHeight / 800.0f) : (int) (options.outWidth / 480.0f);
        if (i3 <= 0) {
            i3 = 1;
        }
        options.inSampleSize = i3;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inPurgeable = true;
        options.inInputShareable = true;
        return BitmapFactory.decodeFile(str, options);
    }
}
