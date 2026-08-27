package cn.sharesdk.whatsapp;

import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.b.b.f;
import com.mob.MobSDK;
import com.mob.tools.utils.BitmapHelper;
import java.io.File;
import java.util.HashMap;

/* loaded from: classes.dex */
public class WhatsApp extends Platform {
    public static final String NAME = "WhatsApp";
    private b a = new b(this);

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public boolean checkAuthorize(int i, Object obj) {
        if (isClientValid()) {
            return true;
        }
        if (this.listener == null) {
            return false;
        }
        this.listener.onError(this, i, new WhatsAppClientNotExistException());
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void doAuthorize(String[] strArr) {
        if (isClientValid()) {
            afterRegister(1, null);
        } else if (this.listener != null) {
            this.listener.onError(this, 1, new WhatsAppClientNotExistException());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void doCustomerProtocol(String str, String str2, int i, HashMap<String, Object> hashMap, HashMap<String, String> hashMap2) {
        if (this.listener != null) {
            this.listener.onCancel(this, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void doShare(final Platform.ShareParams shareParams) {
        String text = shareParams.getText();
        String title = shareParams.getTitle();
        String filePath = shareParams.getFilePath();
        String address = shareParams.getAddress();
        try {
            String imagePath = shareParams.getImagePath();
            String imageUrl = shareParams.getImageUrl();
            PlatformActionListener platformActionListener = new PlatformActionListener() { // from class: cn.sharesdk.whatsapp.WhatsApp.1
                @Override // cn.sharesdk.framework.PlatformActionListener
                public void onCancel(Platform platform, int i) {
                    if (WhatsApp.this.listener != null) {
                        WhatsApp.this.listener.onCancel(platform, i);
                    }
                }

                @Override // cn.sharesdk.framework.PlatformActionListener
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    HashMap<String, Object> hashMap2 = new HashMap<>();
                    if (hashMap != null) {
                        hashMap2.putAll(hashMap);
                    }
                    hashMap2.put("ShareParams", shareParams);
                    if (WhatsApp.this.listener != null) {
                        WhatsApp.this.listener.onComplete(platform, i, hashMap2);
                    }
                }

                @Override // cn.sharesdk.framework.PlatformActionListener
                public void onError(Platform platform, int i, Throwable th) {
                    if (WhatsApp.this.listener != null) {
                        WhatsApp.this.listener.onError(platform, i, th);
                    }
                }
            };
            if (!TextUtils.isEmpty(text)) {
                String shortLintk = getShortLintk(text, false);
                shareParams.setText(shortLintk);
                this.a.a(shortLintk, title, platformActionListener);
                return;
            }
            if (!TextUtils.isEmpty(imagePath)) {
                this.a.a(2, imagePath, platformActionListener);
                return;
            }
            if (!TextUtils.isEmpty(imageUrl)) {
                File file = new File(BitmapHelper.downloadBitmap(MobSDK.getContext(), imageUrl));
                if (file.exists()) {
                    imagePath = file.getAbsolutePath();
                }
                this.a.a(2, imagePath, platformActionListener);
                return;
            }
            if (!TextUtils.isEmpty(filePath)) {
                this.a.a(6, filePath, platformActionListener);
            } else if (!TextUtils.isEmpty(address)) {
                this.a.a(address, platformActionListener);
            } else if (this.listener != null) {
                this.listener.onError(this, 9, new Throwable("Missing parameters"));
            }
        } catch (Throwable th) {
            if (this.listener != null) {
                this.listener.onError(this, 9, th);
            }
        }
    }

    @Override // cn.sharesdk.framework.Platform
    protected HashMap<String, Object> filterFriendshipInfo(int i, HashMap<String, Object> hashMap) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public f.a filterShareContent(Platform.ShareParams shareParams, HashMap<String, Object> hashMap) {
        f.a aVar = new f.a();
        String text = shareParams.getText();
        String imageUrl = shareParams.getImageUrl();
        String imagePath = shareParams.getImagePath();
        if (!TextUtils.isEmpty(text)) {
            aVar.b = text;
        } else if (!TextUtils.isEmpty(imageUrl)) {
            aVar.d.add(imageUrl);
        } else if (!TextUtils.isEmpty(imagePath)) {
            aVar.e.add(imagePath);
        }
        if (hashMap != null) {
            aVar.g = hashMap;
        }
        return aVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void follow(String str) {
        if (this.listener != null) {
            this.listener.onCancel(this, 6);
        }
    }

    @Override // cn.sharesdk.framework.Platform
    protected HashMap<String, Object> getBilaterals(int i, int i2, String str) {
        return null;
    }

    @Override // cn.sharesdk.framework.Platform
    protected HashMap<String, Object> getFollowers(int i, int i2, String str) {
        return null;
    }

    @Override // cn.sharesdk.framework.Platform
    protected HashMap<String, Object> getFollowings(int i, int i2, String str) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void getFriendList(int i, int i2, String str) {
        if (this.listener != null) {
            this.listener.onCancel(this, 2);
        }
    }

    @Override // cn.sharesdk.framework.Platform
    public String getName() {
        return NAME;
    }

    @Override // cn.sharesdk.framework.Platform
    public int getPlatformId() {
        return 43;
    }

    @Override // cn.sharesdk.framework.Platform
    public int getVersion() {
        return 1;
    }

    @Override // cn.sharesdk.framework.Platform
    public boolean hasShareCallback() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void initDevInfo(String str) {
    }

    @Override // cn.sharesdk.framework.Platform
    public boolean isClientValid() {
        return this.a.a();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void setNetworkDevinfo() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void timeline(int i, int i2, String str) {
        if (this.listener != null) {
            this.listener.onCancel(this, 7);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void userInfor(String str) {
        if (this.listener != null) {
            this.listener.onCancel(this, 8);
        }
    }
}
