package cn.sharesdk.onekeyshare;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;
import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.CustomPlatform;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.line.Line;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import cn.sharesdk.whatsapp.WhatsApp;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.mob.MobSDK;
import com.mob.tools.utils.ResHelper;
import com.mob.tools.utils.UIHandler;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public abstract class OnekeyShareThemeImpl implements PlatformActionListener, Handler.Callback {
    public PlatformActionListener callback = this;
    protected Context context;
    protected ArrayList<CustomerLogo> customerLogos;
    protected ShareContentCustomizeCallback customizeCallback;
    protected boolean dialogMode;
    protected boolean disableSSO;
    protected HashMap<String, String> hiddenPlatforms;
    protected HashMap<String, Object> shareParamsMap;
    protected boolean silent;

    private void prepareForEditPage(Platform platform) {
        Platform.ShareParams shareDataToShareParams;
        if (!formateShareData(platform) || (shareDataToShareParams = shareDataToShareParams(platform)) == null) {
            return;
        }
        ShareSDK.logDemoEvent(3, platform);
        shareDataToShareParams.setOpenCustomEven(true);
        if (this.customizeCallback != null) {
            this.customizeCallback.onShare(platform, shareDataToShareParams);
        }
        showEditPage(this.context, platform, shareDataToShareParams);
        this.customizeCallback = null;
    }

    private void toast(final String str) {
        UIHandler.sendEmptyMessage(0, new Handler.Callback() { // from class: cn.sharesdk.onekeyshare.OnekeyShareThemeImpl.1
            @Override // android.os.Handler.Callback
            public boolean handleMessage(Message message) {
                int stringRes = ResHelper.getStringRes(OnekeyShareThemeImpl.this.context, str);
                if (stringRes > 0) {
                    Toast.makeText(OnekeyShareThemeImpl.this.context, stringRes, 0).show();
                } else {
                    Toast.makeText(OnekeyShareThemeImpl.this.context, str, 0).show();
                }
                return false;
            }
        });
    }

    public final void disableSSO() {
        this.disableSSO = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x01ae, code lost:
    
        if (r1 != false) goto L172;
     */
    /* JADX WARN: Code restructure failed: missing block: B:125:0x0202, code lost:
    
        if (r1 != false) goto L172;
     */
    /* JADX WARN: Code restructure failed: missing block: B:141:0x025e, code lost:
    
        if (r1 != false) goto L172;
     */
    /* JADX WARN: Code restructure failed: missing block: B:155:0x02b1, code lost:
    
        if (r1 != false) goto L172;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean formateShareData(cn.sharesdk.framework.Platform r9) {
        /*
            Method dump skipped, instructions count: 705
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.sharesdk.onekeyshare.OnekeyShareThemeImpl.formateShareData(cn.sharesdk.framework.Platform):boolean");
    }

    @Override // android.os.Handler.Callback
    public final boolean handleMessage(Message message) {
        switch (message.arg1) {
            case 1:
                int stringRes = ResHelper.getStringRes(this.context, "ssdk_oks_share_completed");
                if (stringRes <= 0) {
                    return false;
                }
                toast(this.context.getString(stringRes));
                return false;
            case 2:
                String simpleName = message.obj.getClass().getSimpleName();
                if ("WechatClientNotExistException".equals(simpleName) || "WechatTimelineNotSupportedException".equals(simpleName) || "WechatFavoriteNotSupportedException".equals(simpleName)) {
                    toast("ssdk_wechat_client_inavailable");
                    return false;
                }
                if ("GooglePlusClientNotExistException".equals(simpleName)) {
                    toast("ssdk_google_plus_client_inavailable");
                    return false;
                }
                if ("QQClientNotExistException".equals(simpleName)) {
                    toast("ssdk_qq_client_inavailable");
                    return false;
                }
                if ("YixinClientNotExistException".equals(simpleName) || "YixinTimelineNotSupportedException".equals(simpleName)) {
                    toast("ssdk_yixin_client_inavailable");
                    return false;
                }
                if ("KakaoTalkClientNotExistException".equals(simpleName)) {
                    toast("ssdk_kakaotalk_client_inavailable");
                    return false;
                }
                if ("KakaoStoryClientNotExistException".equals(simpleName)) {
                    toast("ssdk_kakaostory_client_inavailable");
                    return false;
                }
                if ("WhatsAppClientNotExistException".equals(simpleName)) {
                    toast("ssdk_whatsapp_client_inavailable");
                    return false;
                }
                if ("FacebookMessengerClientNotExistException".equals(simpleName)) {
                    toast("ssdk_facebookmessenger_client_inavailable");
                    return false;
                }
                toast("ssdk_oks_share_failed");
                return false;
            case 3:
                toast("ssdk_oks_share_canceled");
                return false;
            default:
                return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean isUseClientToShare(Platform platform) {
        String name = platform.getName();
        if (SinaWeibo.NAME.equals(name) || Wechat.NAME.equals(name) || WechatMoments.NAME.equals(name) || "WechatFavorite".equals(name) || "ShortMessage".equals(name) || "Email".equals(name) || "Qzone".equals(name) || QQ.NAME.equals(name) || "Pinterest".equals(name) || "Instagram".equals(name) || "Yixin".equals(name) || "YixinMoments".equals(name) || QZone.NAME.equals(name) || "Mingdao".equals(name) || Line.NAME.equals(name) || "KakaoStory".equals(name) || "KakaoTalk".equals(name) || "Bluetooth".equals(name) || WhatsApp.NAME.equals(name) || "BaiduTieba".equals(name) || "Laiwang".equals(name) || "LaiwangMoments".equals(name) || "Alipay".equals(name) || "AlipayMoments".equals(name) || "FacebookMessenger".equals(name) || "GooglePlus".equals(name) || "Dingding".equals(name) || "Youtube".equals(name) || "Meipai".equals(name) || "Telegram".equals(name) || "Douyin".equals(name)) {
            return true;
        }
        if ("Evernote".equals(name)) {
            return "true".equals(platform.getDevinfo("ShareByAppClient"));
        }
        if (!Facebook.NAME.equals(name)) {
            return false;
        }
        if ("true".equals(platform.getDevinfo("ShareByAppClient")) && platform.isClientValid()) {
            return true;
        }
        return this.shareParamsMap.containsKey(FileDownloadModel.URL) && !TextUtils.isEmpty((String) this.shareParamsMap.get(FileDownloadModel.URL));
    }

    @Override // cn.sharesdk.framework.PlatformActionListener
    public final void onCancel(Platform platform, int i) {
        Message message = new Message();
        message.arg1 = 3;
        message.arg2 = i;
        message.obj = platform;
        UIHandler.sendMessage(message, this);
        ShareSDK.logDemoEvent(5, platform);
    }

    @Override // cn.sharesdk.framework.PlatformActionListener
    public final void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Message message = new Message();
        message.arg1 = 1;
        message.arg2 = i;
        message.obj = platform;
        UIHandler.sendMessage(message, this);
    }

    @Override // cn.sharesdk.framework.PlatformActionListener
    public final void onError(Platform platform, int i, Throwable th) {
        th.printStackTrace();
        Message message = new Message();
        message.arg1 = 2;
        message.arg2 = i;
        message.obj = th;
        UIHandler.sendMessage(message, this);
        ShareSDK.logDemoEvent(4, platform);
    }

    public final void setCustomerLogos(ArrayList<CustomerLogo> arrayList) {
        this.customerLogos = arrayList;
    }

    public final void setDialogMode(boolean z) {
        this.dialogMode = z;
    }

    public final void setHiddenPlatforms(HashMap<String, String> hashMap) {
        this.hiddenPlatforms = hashMap;
    }

    public final void setPlatformActionListener(PlatformActionListener platformActionListener) {
        if (platformActionListener == null) {
            platformActionListener = this;
        }
        this.callback = platformActionListener;
    }

    public final void setShareContentCustomizeCallback(ShareContentCustomizeCallback shareContentCustomizeCallback) {
        this.customizeCallback = shareContentCustomizeCallback;
    }

    public final void setShareParamsMap(HashMap<String, Object> hashMap) {
        this.shareParamsMap = hashMap;
    }

    public final void setSilent(boolean z) {
        this.silent = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Platform.ShareParams shareDataToShareParams(Platform platform) {
        if (platform == null || this.shareParamsMap == null) {
            toast("ssdk_oks_share_failed");
            return null;
        }
        try {
            String str = (String) ResHelper.forceCast(this.shareParamsMap.get("imagePath"));
            Bitmap bitmap = (Bitmap) ResHelper.forceCast(this.shareParamsMap.get("viewToShare"));
            if (TextUtils.isEmpty(str) && bitmap != null && !bitmap.isRecycled()) {
                File file = new File(ResHelper.getCachePath(MobSDK.getContext(), "screenshot"), String.valueOf(System.currentTimeMillis()) + ".jpg");
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                this.shareParamsMap.put("imagePath", file.getAbsolutePath());
            }
            return new Platform.ShareParams(this.shareParamsMap);
        } catch (Throwable th) {
            th.printStackTrace();
            toast("ssdk_oks_share_failed");
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void shareSilently(Platform platform) {
        Platform.ShareParams shareDataToShareParams;
        if (!formateShareData(platform) || (shareDataToShareParams = shareDataToShareParams(platform)) == null) {
            return;
        }
        toast("ssdk_oks_sharing");
        if (this.customizeCallback != null) {
            this.customizeCallback.onShare(platform, shareDataToShareParams);
        }
        if (this.disableSSO) {
            platform.SSOSetting(this.disableSSO);
        }
        platform.setPlatformActionListener(this.callback);
        platform.share(shareDataToShareParams);
        this.callback = null;
        this.customizeCallback = null;
    }

    public final void show(Context context) {
        this.context = context;
        if (!this.shareParamsMap.containsKey("platform")) {
            showPlatformPage(context);
            return;
        }
        Platform platform = ShareSDK.getPlatform(String.valueOf(this.shareParamsMap.get("platform")));
        boolean z = platform instanceof CustomPlatform;
        boolean isUseClientToShare = isUseClientToShare(platform);
        if (this.silent || z || isUseClientToShare) {
            shareSilently(platform);
        } else {
            prepareForEditPage(platform);
        }
    }

    protected abstract void showEditPage(Context context, Platform platform, Platform.ShareParams shareParams);

    protected abstract void showPlatformPage(Context context);
}
