package cn.sharesdk.wechat.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.utils.h;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.luck.picture.lib.config.PictureConfig;
import com.mob.MobSDK;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.utils.BitmapHelper;
import com.mob.tools.utils.DeviceHelper;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

/* compiled from: WechatHelper.java */
/* loaded from: classes.dex */
public class k {
    private static k a;
    private i b = new i();
    private j c;
    private String d;
    private String e;
    private boolean f;
    private int g;

    private k() {
    }

    private Bitmap a(Bitmap bitmap, double d) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        double sqrt = Math.sqrt(d);
        double d2 = width;
        Double.isNaN(d2);
        double d3 = height;
        Double.isNaN(d3);
        return Bitmap.createScaledBitmap(bitmap, (int) (d2 / sqrt), (int) (d3 / sqrt), true);
    }

    public static k a() {
        if (a == null) {
            a = new k();
        }
        return a;
    }

    private void a(Context context, String str, String str2, Bitmap bitmap, int i, j jVar) throws Throwable {
        WXImageObject wXImageObject = new WXImageObject();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, byteArrayOutputStream);
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        wXImageObject.imageData = byteArrayOutputStream.toByteArray();
        WXMediaMessage wXMediaMessage = new WXMediaMessage();
        wXMediaMessage.mediaObject = wXImageObject;
        if (i != 0) {
            wXMediaMessage.title = str;
            wXMediaMessage.description = str2;
        }
        wXMediaMessage.thumbData = a(context, bitmap, false);
        a(wXMediaMessage, "img", i, jVar);
    }

    private void a(Context context, String str, String str2, String str3, int i, j jVar) throws Throwable {
        WXImageObject wXImageObject = new WXImageObject();
        if (str3.startsWith("/data/")) {
            wXImageObject.imageData = d(str3);
        } else {
            wXImageObject.imagePath = str3;
        }
        WXMediaMessage wXMediaMessage = new WXMediaMessage();
        wXMediaMessage.mediaObject = wXImageObject;
        if (i != 0) {
            wXMediaMessage.title = str;
            wXMediaMessage.description = str2;
        }
        wXMediaMessage.thumbData = a(context, str3, false);
        a(wXMediaMessage, "img", i, jVar);
    }

    private void a(Context context, String str, String str2, String str3, Bitmap bitmap, int i, j jVar) throws Throwable {
        WXVideoObject wXVideoObject = new WXVideoObject();
        wXVideoObject.videoUrl = str3;
        WXMediaMessage wXMediaMessage = new WXMediaMessage();
        wXMediaMessage.title = str;
        wXMediaMessage.description = str2;
        wXMediaMessage.mediaObject = wXVideoObject;
        wXMediaMessage.thumbData = a(context, bitmap, false);
        a(wXMediaMessage, PictureConfig.VIDEO, i, jVar);
    }

    private void a(Context context, String str, String str2, String str3, String str4, int i, j jVar) throws Throwable {
        WXVideoObject wXVideoObject = new WXVideoObject();
        wXVideoObject.videoUrl = str3;
        WXMediaMessage wXMediaMessage = new WXMediaMessage();
        wXMediaMessage.title = str;
        wXMediaMessage.description = str2;
        wXMediaMessage.mediaObject = wXVideoObject;
        wXMediaMessage.thumbData = a(context, str4, false);
        a(wXMediaMessage, PictureConfig.VIDEO, i, jVar);
    }

    private void a(Context context, String str, String str2, String str3, String str4, Bitmap bitmap, int i, j jVar) throws Throwable {
        WXMusicObject wXMusicObject = new WXMusicObject();
        wXMusicObject.musicUrl = str4;
        wXMusicObject.musicDataUrl = str3;
        WXMediaMessage wXMediaMessage = new WXMediaMessage();
        wXMediaMessage.title = str;
        wXMediaMessage.description = str2;
        wXMediaMessage.mediaObject = wXMusicObject;
        wXMediaMessage.thumbData = a(context, bitmap, false);
        a(wXMediaMessage, "music", i, jVar);
    }

    private void a(Context context, String str, String str2, String str3, String str4, String str5, int i, j jVar) throws Throwable {
        WXMusicObject wXMusicObject = new WXMusicObject();
        wXMusicObject.musicUrl = str4;
        wXMusicObject.musicDataUrl = str3;
        WXMediaMessage wXMediaMessage = new WXMediaMessage();
        wXMediaMessage.title = str;
        wXMediaMessage.description = str2;
        wXMediaMessage.mediaObject = wXMusicObject;
        wXMediaMessage.thumbData = a(context, str5, false);
        a(wXMediaMessage, "music", i, jVar);
    }

    private void a(Context context, String str, String str2, String str3, String str4, String str5, Bitmap bitmap, int i, j jVar) throws Throwable {
        String str6;
        WXMiniProgramObject wXMiniProgramObject = new WXMiniProgramObject();
        wXMiniProgramObject.webpageUrl = str;
        if (TextUtils.isEmpty(str2) || !str2.endsWith("@app")) {
            wXMiniProgramObject.userName = str2 + "@app";
        } else {
            wXMiniProgramObject.userName = str2;
        }
        if (!TextUtils.isEmpty(str3)) {
            String[] split = str3.split("\\?");
            if (split.length > 1) {
                str6 = split[0] + ".html?" + split[1];
            } else {
                str6 = split[0] + ".html";
            }
            wXMiniProgramObject.path = str6;
            wXMiniProgramObject.withShareTicket = this.f;
            wXMiniProgramObject.miniprogramType = this.g;
        }
        WXMediaMessage wXMediaMessage = new WXMediaMessage();
        wXMediaMessage.title = str4;
        wXMediaMessage.mediaObject = wXMiniProgramObject;
        wXMediaMessage.description = str5;
        if (bitmap != null && !bitmap.isRecycled()) {
            wXMediaMessage.thumbData = a(context, bitmap, true);
            if (wXMediaMessage.thumbData == null) {
                throw new RuntimeException("checkArgs fail, thumbData is null");
            }
            if (wXMediaMessage.thumbData.length > 131072) {
                throw new RuntimeException("checkArgs fail, thumbData is too large: " + wXMediaMessage.thumbData.length + " > 131072");
            }
        }
        a(wXMediaMessage, "webpage", i, jVar);
    }

    private void a(Context context, String str, String str2, String str3, String str4, String str5, String str6, int i, j jVar) throws Throwable {
        String str7;
        WXMiniProgramObject wXMiniProgramObject = new WXMiniProgramObject();
        wXMiniProgramObject.miniprogramType = this.g;
        wXMiniProgramObject.webpageUrl = str;
        if (TextUtils.isEmpty(str2) || !str2.endsWith("@app")) {
            wXMiniProgramObject.userName = str2 + "@app";
        } else {
            wXMiniProgramObject.userName = str2;
        }
        if (!TextUtils.isEmpty(str3)) {
            String[] split = str3.split("\\?");
            if (split.length > 1) {
                str7 = split[0] + ".html?" + split[1];
            } else {
                str7 = split[0] + ".html";
            }
            wXMiniProgramObject.path = str7;
            wXMiniProgramObject.withShareTicket = this.f;
            wXMiniProgramObject.miniprogramType = this.g;
        }
        WXMediaMessage wXMediaMessage = new WXMediaMessage();
        wXMediaMessage.title = str4;
        wXMediaMessage.mediaObject = wXMiniProgramObject;
        wXMediaMessage.description = str5;
        wXMediaMessage.thumbData = a(context, str6, true);
        a(wXMediaMessage, "webpage", i, jVar);
    }

    private void a(WXMediaMessage wXMediaMessage, String str, int i, j jVar) throws Throwable {
        Class<?> cls;
        String str2 = DeviceHelper.getInstance(MobSDK.getContext()).getPackageName() + ".wxapi.WXEntryActivity";
        try {
            cls = Class.forName(str2);
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
            cls = null;
        }
        if (cls != null && !WechatHandlerActivity.class.isAssignableFrom(cls)) {
            new Throwable(str2 + " does not extend from " + WechatHandlerActivity.class.getName()).printStackTrace();
        }
        d dVar = new d();
        dVar.d = str + System.currentTimeMillis();
        dVar.a = wXMediaMessage;
        dVar.b = i;
        this.c = jVar;
        this.b.a(dVar, wXMediaMessage.mediaObject instanceof WXMiniProgramObject);
    }

    private void a(String str, String str2) throws Throwable {
        h.a aVar = new h.a();
        aVar.a = str;
        aVar.b = str2;
        aVar.c = this.g;
        this.b.a(aVar);
    }

    private void a(String str, String str2, int i, j jVar) throws Throwable {
        WXTextObject wXTextObject = new WXTextObject();
        wXTextObject.text = str2;
        WXMediaMessage wXMediaMessage = new WXMediaMessage();
        wXMediaMessage.title = str;
        wXMediaMessage.mediaObject = wXTextObject;
        wXMediaMessage.description = str2;
        a(wXMediaMessage, "text", i, jVar);
    }

    private byte[] a(Context context, Bitmap bitmap, Bitmap.CompressFormat compressFormat, boolean z) throws Throwable {
        if (bitmap == null) {
            throw new RuntimeException("checkArgs fail, thumbData is null");
        }
        if (bitmap.isRecycled()) {
            throw new RuntimeException("checkArgs fail, thumbData is recycled");
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(compressFormat, 100, byteArrayOutputStream);
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        int length = byteArray.length;
        int i = z ? 131072 : 32768;
        while (length > i) {
            double d = length;
            double d2 = i;
            Double.isNaN(d);
            Double.isNaN(d2);
            bitmap = a(bitmap, d / d2);
            ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
            bitmap.compress(compressFormat, 100, byteArrayOutputStream2);
            byteArrayOutputStream2.flush();
            byteArrayOutputStream2.close();
            byteArray = byteArrayOutputStream2.toByteArray();
            length = byteArray.length;
        }
        return byteArray;
    }

    private byte[] a(Context context, Bitmap bitmap, boolean z) throws Throwable {
        if (bitmap == null) {
            throw new RuntimeException("checkArgs fail, thumbData is null");
        }
        if (bitmap.isRecycled()) {
            throw new RuntimeException("checkArgs fail, thumbData is recycled");
        }
        return a(context, bitmap, Bitmap.CompressFormat.PNG, z);
    }

    private byte[] a(Context context, String str, boolean z) throws Throwable {
        if (!new File(str).exists()) {
            throw new FileNotFoundException();
        }
        return a(context, BitmapHelper.getBitmap(str), BitmapHelper.getBmpFormat(str), z);
    }

    private void b(Context context, String str, String str2, Bitmap bitmap, int i, j jVar) throws Throwable {
        WXEmojiObject wXEmojiObject = new WXEmojiObject();
        byte[] a2 = a(context, bitmap, false);
        wXEmojiObject.emojiData = a2;
        WXMediaMessage wXMediaMessage = new WXMediaMessage();
        wXMediaMessage.title = str;
        wXMediaMessage.mediaObject = wXEmojiObject;
        wXMediaMessage.description = str2;
        wXMediaMessage.thumbData = a2;
        a(wXMediaMessage, "emoji", i, jVar);
    }

    private void b(Context context, String str, String str2, String str3, int i, j jVar) throws Throwable {
        WXEmojiObject wXEmojiObject = new WXEmojiObject();
        wXEmojiObject.emojiPath = str3;
        WXMediaMessage wXMediaMessage = new WXMediaMessage();
        wXMediaMessage.title = str;
        wXMediaMessage.mediaObject = wXEmojiObject;
        wXMediaMessage.description = str2;
        wXMediaMessage.thumbData = a(context, str3, false);
        a(wXMediaMessage, "emoji", i, jVar);
    }

    private void b(Context context, String str, String str2, String str3, Bitmap bitmap, int i, j jVar) throws Throwable {
        WXWebpageObject wXWebpageObject = new WXWebpageObject();
        wXWebpageObject.webpageUrl = str3;
        WXMediaMessage wXMediaMessage = new WXMediaMessage();
        wXMediaMessage.title = str;
        wXMediaMessage.description = str2;
        wXMediaMessage.mediaObject = wXWebpageObject;
        if (bitmap != null && !bitmap.isRecycled()) {
            wXMediaMessage.thumbData = a(context, bitmap, false);
            if (wXMediaMessage.thumbData == null) {
                throw new RuntimeException("checkArgs fail, thumbData is null");
            }
            if (wXMediaMessage.thumbData.length > 32768) {
                throw new RuntimeException("checkArgs fail, thumbData is too large: " + wXMediaMessage.thumbData.length + " > 32768");
            }
        }
        a(wXMediaMessage, "webpage", i, jVar);
    }

    private void b(Context context, String str, String str2, String str3, String str4, int i, j jVar) throws Throwable {
        WXWebpageObject wXWebpageObject = new WXWebpageObject();
        wXWebpageObject.webpageUrl = str3;
        WXMediaMessage wXMediaMessage = new WXMediaMessage();
        wXMediaMessage.title = str;
        wXMediaMessage.description = str2;
        wXMediaMessage.mediaObject = wXWebpageObject;
        if (str4 != null && new File(str4).exists()) {
            wXMediaMessage.thumbData = a(context, str4, false);
            if (wXMediaMessage.thumbData == null) {
                throw new RuntimeException("checkArgs fail, thumbData is null");
            }
            if (wXMediaMessage.thumbData.length > 32768) {
                throw new RuntimeException("checkArgs fail, thumbData is too large: " + wXMediaMessage.thumbData.length + " > 32768");
            }
        }
        a(wXMediaMessage, "webpage", i, jVar);
    }

    private void b(Context context, String str, String str2, String str3, String str4, Bitmap bitmap, int i, j jVar) throws Throwable {
        WXAppExtendObject wXAppExtendObject = new WXAppExtendObject();
        wXAppExtendObject.filePath = str3;
        wXAppExtendObject.extInfo = str4;
        WXMediaMessage wXMediaMessage = new WXMediaMessage();
        wXMediaMessage.title = str;
        wXMediaMessage.description = str2;
        wXMediaMessage.mediaObject = wXAppExtendObject;
        wXMediaMessage.thumbData = a(context, bitmap, false);
        a(wXMediaMessage, "appdata", i, jVar);
    }

    private void b(Context context, String str, String str2, String str3, String str4, String str5, int i, j jVar) throws Throwable {
        WXAppExtendObject wXAppExtendObject = new WXAppExtendObject();
        wXAppExtendObject.filePath = str3;
        wXAppExtendObject.extInfo = str4;
        WXMediaMessage wXMediaMessage = new WXMediaMessage();
        wXMediaMessage.title = str;
        wXMediaMessage.description = str2;
        wXMediaMessage.mediaObject = wXAppExtendObject;
        wXMediaMessage.thumbData = a(context, str5, false);
        a(wXMediaMessage, "appdata", i, jVar);
    }

    private void c(Context context, String str, String str2, String str3, Bitmap bitmap, int i, j jVar) throws Throwable {
        WXFileObject wXFileObject = new WXFileObject();
        wXFileObject.filePath = str3;
        WXMediaMessage wXMediaMessage = new WXMediaMessage();
        wXMediaMessage.title = str;
        wXMediaMessage.description = str2;
        wXMediaMessage.mediaObject = wXFileObject;
        wXMediaMessage.thumbData = a(context, bitmap, false);
        a(wXMediaMessage, "filedata", i, jVar);
    }

    private void c(Context context, String str, String str2, String str3, String str4, int i, j jVar) throws Throwable {
        WXFileObject wXFileObject = new WXFileObject();
        wXFileObject.filePath = str3;
        WXMediaMessage wXMediaMessage = new WXMediaMessage();
        wXMediaMessage.title = str;
        wXMediaMessage.description = str2;
        wXMediaMessage.mediaObject = wXFileObject;
        wXMediaMessage.thumbData = a(context, str4, false);
        a(wXMediaMessage, "filedata", i, jVar);
    }

    private byte[] d(String str) {
        try {
            FileInputStream fileInputStream = new FileInputStream(str);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[1024];
            for (int read = fileInputStream.read(bArr); read > 0; read = fileInputStream.read(bArr)) {
                byteArrayOutputStream.write(bArr, 0, read);
            }
            byteArrayOutputStream.flush();
            fileInputStream.close();
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
            return null;
        }
    }

    public void a(int i) {
        this.g = i;
    }

    public void a(j jVar) throws Throwable {
        this.c = jVar;
        a aVar = new a();
        aVar.a = "snsapi_userinfo";
        aVar.b = "sharesdk_wechat_auth";
        this.b.a((l) aVar, false);
    }

    public void a(j jVar, Platform.ShareParams shareParams, PlatformActionListener platformActionListener) throws Throwable {
        Platform b = jVar.b();
        String str = ((Integer) shareParams.get("scene", Integer.class)).intValue() == 1 ? "com.tencent.mm.ui.tools.ShareToTimeLineUI" : "com.tencent.mm.ui.tools.ShareImgUI";
        cn.sharesdk.framework.utils.f fVar = new cn.sharesdk.framework.utils.f();
        fVar.a("com.tencent.mm", str);
        fVar.a(shareParams, b);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("ShareParams", shareParams);
        platformActionListener.onComplete(b, 9, hashMap);
    }

    public void a(String str) {
        this.e = str;
    }

    public void a(boolean z) {
        this.f = z;
    }

    public boolean a(WechatHandlerActivity wechatHandlerActivity) {
        return this.b.a(wechatHandlerActivity, this.c);
    }

    public void b(j jVar) throws Throwable {
        Platform b = jVar.b();
        Platform.ShareParams a2 = jVar.a();
        PlatformActionListener c = jVar.c();
        int shareType = a2.getShareType();
        if (shareType == 11 && e() < 620756993) {
            shareType = 4;
        }
        String title = a2.getTitle();
        String text = a2.getText();
        int scence = a2.getScence();
        String imagePath = a2.getImagePath();
        String imageUrl = a2.getImageUrl();
        Bitmap imageData = a2.getImageData();
        String musicUrl = a2.getMusicUrl();
        String url = a2.getUrl();
        String filePath = a2.getFilePath();
        String extInfo = a2.getExtInfo();
        switch (shareType) {
            case 1:
                a(title, text, scence, jVar);
                return;
            case 2:
                if (imagePath != null && imagePath.length() > 0) {
                    a(MobSDK.getContext(), title, text, imagePath, scence, jVar);
                    return;
                }
                if (imageData != null && !imageData.isRecycled()) {
                    a(MobSDK.getContext(), title, text, imageData, scence, jVar);
                    return;
                } else if (imageUrl == null || imageUrl.length() <= 0) {
                    a(MobSDK.getContext(), title, text, "", scence, jVar);
                    return;
                } else {
                    a(MobSDK.getContext(), title, text, BitmapHelper.downloadBitmap(MobSDK.getContext(), imageUrl), scence, jVar);
                    return;
                }
            case 3:
            case 10:
            default:
                if (c != null) {
                    c.onError(b, 9, new IllegalArgumentException("shareType = " + shareType));
                    return;
                }
                return;
            case 4:
                String shortLintk = b.getShortLintk(url, false);
                jVar.a().setUrl(shortLintk);
                if (imagePath != null && imagePath.length() > 0) {
                    b(MobSDK.getContext(), title, text, shortLintk, imagePath, scence, jVar);
                    return;
                }
                if (imageData != null && !imageData.isRecycled()) {
                    b(MobSDK.getContext(), title, text, shortLintk, imageData, scence, jVar);
                    return;
                } else if (imageUrl == null || imageUrl.length() <= 0) {
                    b(MobSDK.getContext(), title, text, shortLintk, "", scence, jVar);
                    return;
                } else {
                    b(MobSDK.getContext(), title, text, shortLintk, BitmapHelper.downloadBitmap(MobSDK.getContext(), imageUrl), scence, jVar);
                    return;
                }
            case 5:
                String shortLintk2 = b.getShortLintk(musicUrl + SQLBuilder.BLANK + url, false);
                String str = shortLintk2.split(SQLBuilder.BLANK)[0];
                String str2 = shortLintk2.split(SQLBuilder.BLANK)[1];
                if (imagePath != null && imagePath.length() > 0) {
                    a(MobSDK.getContext(), title, text, str, str2, imagePath, scence, jVar);
                    return;
                }
                if (imageData != null && !imageData.isRecycled()) {
                    a(MobSDK.getContext(), title, text, str, str2, imageData, scence, jVar);
                    return;
                } else if (imageUrl == null || imageUrl.length() <= 0) {
                    a(MobSDK.getContext(), title, text, str, str2, "", scence, jVar);
                    return;
                } else {
                    a(MobSDK.getContext(), title, text, str, str2, BitmapHelper.downloadBitmap(MobSDK.getContext(), imageUrl), scence, jVar);
                    return;
                }
            case 6:
                String shortLintk3 = b.getShortLintk(url, false);
                jVar.a().setUrl(shortLintk3);
                if (imagePath != null && imagePath.length() > 0) {
                    a(MobSDK.getContext(), title, text, shortLintk3, imagePath, scence, jVar);
                    return;
                }
                if (imageData != null && !imageData.isRecycled()) {
                    a(MobSDK.getContext(), title, text, shortLintk3, imageData, scence, jVar);
                    return;
                } else if (imageUrl == null || imageUrl.length() <= 0) {
                    a(MobSDK.getContext(), title, text, shortLintk3, "", scence, jVar);
                    return;
                } else {
                    a(MobSDK.getContext(), title, text, shortLintk3, BitmapHelper.downloadBitmap(MobSDK.getContext(), imageUrl), scence, jVar);
                    return;
                }
            case 7:
                if (scence == 1) {
                    throw new Throwable("WechatMoments does not support SAHRE_APP");
                }
                if (scence == 2) {
                    throw new Throwable("WechatFavorite does not support SAHRE_APP");
                }
                if (imagePath != null && imagePath.length() > 0) {
                    b(MobSDK.getContext(), title, text, filePath, extInfo, imagePath, scence, jVar);
                    return;
                }
                if (imageData != null && !imageData.isRecycled()) {
                    b(MobSDK.getContext(), title, text, filePath, extInfo, imageData, scence, jVar);
                    return;
                } else if (imageUrl == null || imageUrl.length() <= 0) {
                    b(MobSDK.getContext(), title, text, filePath, extInfo, "", scence, jVar);
                    return;
                } else {
                    b(MobSDK.getContext(), title, text, filePath, extInfo, BitmapHelper.downloadBitmap(MobSDK.getContext(), imageUrl), scence, jVar);
                    return;
                }
            case 8:
                if (scence == 1) {
                    throw new Throwable("WechatMoments does not support SHARE_FILE");
                }
                if (imagePath != null && imagePath.length() > 0) {
                    c(MobSDK.getContext(), title, text, filePath, imagePath, scence, jVar);
                    return;
                }
                if (imageData != null && !imageData.isRecycled()) {
                    c(MobSDK.getContext(), title, text, filePath, imageData, scence, jVar);
                    return;
                } else if (imageUrl == null || imageUrl.length() <= 0) {
                    c(MobSDK.getContext(), title, text, filePath, "", scence, jVar);
                    return;
                } else {
                    c(MobSDK.getContext(), title, text, filePath, BitmapHelper.downloadBitmap(MobSDK.getContext(), imageUrl), scence, jVar);
                    return;
                }
            case 9:
                if (scence == 1) {
                    throw new Throwable("WechatMoments does not support SHARE_EMOJI");
                }
                if (scence == 2) {
                    throw new Throwable("WechatFavorite does not support SHARE_EMOJI");
                }
                if (imagePath != null && imagePath.length() > 0) {
                    b(MobSDK.getContext(), title, text, imagePath, scence, jVar);
                    return;
                }
                if (imageUrl != null && imageUrl.length() > 0) {
                    b(MobSDK.getContext(), title, text, new NetworkHelper().downloadCache(MobSDK.getContext(), imageUrl, "images", true, null), scence, jVar);
                    return;
                } else if (imageData == null || imageData.isRecycled()) {
                    b(MobSDK.getContext(), title, text, "", scence, jVar);
                    return;
                } else {
                    b(MobSDK.getContext(), title, text, imageData, scence, jVar);
                    return;
                }
            case 11:
                if (scence == 1) {
                    throw new Throwable("WechatMoments does not support SAHRE_WXMINIPROGRAM");
                }
                if (scence == 2) {
                    throw new Throwable("WechatFavorite does not support SAHRE_WXMINIPROGRAM");
                }
                if (TextUtils.isEmpty(this.d) || TextUtils.isEmpty(this.e)) {
                    c.onError(b, 9, new Throwable("checkArgs fail, UserName or Path is invalid"));
                    return;
                }
                String shortLintk4 = b.getShortLintk(url, false);
                jVar.a().setUrl(shortLintk4);
                if (imagePath != null && imagePath.length() > 0) {
                    a(MobSDK.getContext(), shortLintk4, this.d, this.e, title, text, imagePath, scence, jVar);
                    return;
                }
                if (imageData != null && !imageData.isRecycled()) {
                    a(MobSDK.getContext(), shortLintk4, this.d, this.e, title, text, imageData, scence, jVar);
                    return;
                } else if (imageUrl == null || imageUrl.length() <= 0) {
                    a(MobSDK.getContext(), shortLintk4, this.d, this.e, title, text, "", scence, jVar);
                    return;
                } else {
                    a(MobSDK.getContext(), shortLintk4, this.d, this.e, title, text, BitmapHelper.downloadBitmap(MobSDK.getContext(), imageUrl), scence, jVar);
                    return;
                }
            case 12:
                if (TextUtils.isEmpty(this.d) || TextUtils.isEmpty(this.e)) {
                    c.onError(b, 9, new Throwable("checkArgs fail, UserName or Path is invalid"));
                    return;
                } else {
                    a(this.d, this.e);
                    return;
                }
        }
    }

    public void b(String str) {
        this.d = str;
    }

    public boolean b() {
        return this.b.a();
    }

    public boolean c() {
        return this.b.b();
    }

    public boolean c(String str) {
        return this.b.a(str);
    }

    public boolean d() {
        return this.b.c();
    }

    public final int e() {
        if (!new Wechat().isClientValid()) {
            return 0;
        }
        try {
            return MobSDK.getContext().getPackageManager().getApplicationInfo("com.tencent.mm", 128).metaData.getInt("com.tencent.mm.BuildInfo.OPEN_SDK_VERSION", 0);
        } catch (Exception unused) {
            return 0;
        }
    }
}
