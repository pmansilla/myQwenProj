package cn.sharesdk.onekeyshare;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.mob.MobApplication;
import com.mob.MobSDK;
import com.mob.tools.utils.BitmapHelper;
import com.mob.tools.utils.ResHelper;
import com.sun.mail.imap.IMAPStore;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class OnekeyShare {
    private HashMap<String, Object> params = new HashMap<>();

    public OnekeyShare() {
        this.params.put("customers", new ArrayList());
        this.params.put("hiddenPlatforms", new HashMap());
    }

    public void addHiddenPlatform(String str) {
        ((HashMap) ResHelper.forceCast(this.params.get("hiddenPlatforms"))).put(str, str);
    }

    public void disableSSOWhenAuthorize() {
        this.params.put("disableSSO", true);
    }

    public PlatformActionListener getCallback() {
        return (PlatformActionListener) ResHelper.forceCast(this.params.get("callback"));
    }

    public ShareContentCustomizeCallback getShareContentCustomizeCallback() {
        return (ShareContentCustomizeCallback) ResHelper.forceCast(this.params.get("customizeCallback"));
    }

    public String getText() {
        if (this.params.containsKey("text")) {
            return String.valueOf(this.params.get("text"));
        }
        return null;
    }

    public void setAddress(String str) {
        this.params.put(IMAPStore.ID_ADDRESS, str);
    }

    public void setCallback(PlatformActionListener platformActionListener) {
        this.params.put("callback", platformActionListener);
    }

    public void setComment(String str) {
        this.params.put("comment", str);
    }

    public void setCustomerLogo(Bitmap bitmap, String str, View.OnClickListener onClickListener) {
        CustomerLogo customerLogo = new CustomerLogo();
        customerLogo.logo = bitmap;
        customerLogo.label = str;
        customerLogo.listener = onClickListener;
        ((ArrayList) ResHelper.forceCast(this.params.get("customers"))).add(customerLogo);
    }

    public void setDialogMode(boolean z) {
        this.params.put("dialogMode", Boolean.valueOf(z));
    }

    public void setExecuteUrl(String str) {
        this.params.put("executeurl", str);
    }

    public void setFilePath(String str) {
        this.params.put("filePath", str);
    }

    public void setImageArray(String[] strArr) {
        this.params.put("imageArray", strArr);
    }

    public void setImageData(Bitmap bitmap) {
        if (bitmap != null) {
            this.params.put("imageData", bitmap);
        }
    }

    public void setImagePath(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.params.put("imagePath", str);
    }

    public void setImageUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.params.put("imageUrl", str);
    }

    public void setInstallUrl(String str) {
        this.params.put("installurl", str);
    }

    public void setLatitude(float f) {
        this.params.put("latitude", Float.valueOf(f));
    }

    public void setLongitude(float f) {
        this.params.put("longitude", Float.valueOf(f));
    }

    public void setMusicUrl(String str) {
        this.params.put("musicUrl", str);
    }

    public void setPlatform(String str) {
        this.params.put("platform", str);
    }

    public void setShareContentCustomizeCallback(ShareContentCustomizeCallback shareContentCustomizeCallback) {
        this.params.put("customizeCallback", shareContentCustomizeCallback);
    }

    public void setShareToTencentWeiboWhenPerformingQQOrQZoneSharing() {
        this.params.put("isShareTencentWeibo", true);
    }

    public void setSilent(boolean z) {
        this.params.put("silent", Boolean.valueOf(z));
    }

    public void setSite(String str) {
        this.params.put("site", str);
    }

    public void setSiteUrl(String str) {
        this.params.put("siteUrl", str);
    }

    public void setText(String str) {
        this.params.put("text", str);
    }

    public void setTheme(OnekeyShareTheme onekeyShareTheme) {
        this.params.put("theme", Integer.valueOf(onekeyShareTheme.getValue()));
    }

    public void setTitle(String str) {
        this.params.put("title", str);
    }

    public void setTitleUrl(String str) {
        this.params.put("titleUrl", str);
    }

    public void setUrl(String str) {
        this.params.put(FileDownloadModel.URL, str);
    }

    public void setVenueDescription(String str) {
        this.params.put("venueDescription", str);
    }

    public void setVenueName(String str) {
        this.params.put("venueName", str);
    }

    public void setVideoUrl(String str) {
        this.params.put(FileDownloadModel.URL, str);
        this.params.put("shareType", 6);
    }

    public void setViewToShare(View view) {
        try {
            this.params.put("viewToShare", BitmapHelper.captureView(view, view.getWidth(), view.getHeight()));
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void show(Context context) {
        int i;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.putAll(this.params);
        if (!(context instanceof MobApplication)) {
            MobSDK.init(context.getApplicationContext());
        }
        ShareSDK.logDemoEvent(1, null);
        try {
            i = ResHelper.parseInt(String.valueOf(hashMap.remove("theme")));
        } catch (Throwable unused) {
            i = 0;
        }
        OnekeyShareThemeImpl impl = OnekeyShareTheme.fromValue(i).getImpl();
        impl.setShareParamsMap(hashMap);
        impl.setDialogMode(hashMap.containsKey("dialogMode") ? ((Boolean) hashMap.remove("dialogMode")).booleanValue() : false);
        impl.setSilent(hashMap.containsKey("silent") ? ((Boolean) hashMap.remove("silent")).booleanValue() : false);
        impl.setCustomerLogos((ArrayList) hashMap.remove("customers"));
        impl.setHiddenPlatforms((HashMap) hashMap.remove("hiddenPlatforms"));
        impl.setPlatformActionListener((PlatformActionListener) hashMap.remove("callback"));
        impl.setShareContentCustomizeCallback((ShareContentCustomizeCallback) hashMap.remove("customizeCallback"));
        if (hashMap.containsKey("disableSSO") && ((Boolean) hashMap.remove("disableSSO")).booleanValue()) {
            impl.disableSSO();
        }
        impl.show(context.getApplicationContext());
    }
}
