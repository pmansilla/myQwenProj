package cn.sharesdk.line;

import android.os.Bundle;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import cn.sharesdk.framework.b.b.f;
import cn.sharesdk.framework.utils.e;
import com.mob.MobSDK;
import com.mob.tools.utils.BitmapHelper;
import com.mob.tools.utils.ResHelper;
import java.io.File;
import java.util.HashMap;

/* loaded from: classes.dex */
public class Line extends Platform {
    public static final String NAME = "Line";
    private b a = b.a(this);
    private String b;
    private String c;
    private String d;

    private boolean c() {
        if (TextUtils.isEmpty(getDb().get("refresh_token"))) {
            return false;
        }
        this.a.a(this.b, this.c);
        this.a.a(this.d);
        return this.a.a();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public boolean checkAuthorize(int i, Object obj) {
        if (!isClientValid() && i == 9) {
            if (this.listener != null) {
                this.listener.onError(this, i, new LineClientNotExistException());
            }
            return false;
        }
        if (i == 9) {
            return true;
        }
        if (!TextUtils.isEmpty(this.db.getToken()) && !this.a.c(this.db.getToken())) {
            innerAuthorize(i, obj);
            return false;
        }
        if (!isAuthValid() && !c()) {
            innerAuthorize(i, obj);
            return false;
        }
        this.a.a(this.b, this.c);
        this.a.a(this.d);
        this.a.d(this.db.getToken());
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void doAuthorize(String[] strArr) {
        this.a.a(this.b, this.c);
        this.a.a(this.d);
        this.a.a(new AuthorizeListener() { // from class: cn.sharesdk.line.Line.1
            @Override // cn.sharesdk.framework.authorize.AuthorizeListener
            public void onCancel() {
                if (Line.this.listener != null) {
                    Line.this.listener.onCancel(Line.this, 1);
                }
            }

            @Override // cn.sharesdk.framework.authorize.AuthorizeListener
            public void onComplete(Bundle bundle) {
                long j;
                String string = bundle.getString("mid");
                String string2 = bundle.getString("access_token");
                String string3 = bundle.getString("expires_in");
                String string4 = bundle.getString("refresh_token");
                String string5 = bundle.getString("token_type");
                Line.this.db.putToken(string2);
                Line.this.db.put("token_type", string5);
                try {
                    j = ResHelper.parseLong(string3);
                } catch (Throwable unused) {
                    j = 0;
                }
                Line.this.db.putExpiresIn(j);
                Line.this.db.put("refresh_token", string4);
                Line.this.db.putUserId(string);
                Line.this.a.d(string2);
                Line.this.afterRegister(1, null);
            }

            @Override // cn.sharesdk.framework.authorize.AuthorizeListener
            public void onError(Throwable th) {
                if (Line.this.listener != null) {
                    Line.this.listener.onError(Line.this, 1, th);
                }
            }
        }, isSSODisable());
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
    public void doShare(Platform.ShareParams shareParams) {
        String text = shareParams.getText();
        if (!TextUtils.isEmpty(text)) {
            try {
                String shortLintk = getShortLintk(text, false);
                shareParams.setText(shortLintk);
                b.a(this).e(shortLintk);
                if (this.listener != null) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("ShareParams", shareParams);
                    this.listener.onComplete(this, 9, hashMap);
                    return;
                }
                return;
            } catch (Throwable th) {
                if (this.listener != null) {
                    this.listener.onError(this, 9, th);
                    return;
                }
                return;
            }
        }
        String imagePath = shareParams.getImagePath();
        if (!TextUtils.isEmpty(imagePath) && new File(imagePath).exists()) {
            try {
                b.a(this).f(imagePath);
                if (this.listener != null) {
                    HashMap<String, Object> hashMap2 = new HashMap<>();
                    hashMap2.put("ShareParams", shareParams);
                    this.listener.onComplete(this, 9, hashMap2);
                    return;
                }
                return;
            } catch (Throwable th2) {
                if (this.listener != null) {
                    this.listener.onError(this, 9, th2);
                    return;
                }
                return;
            }
        }
        try {
            String downloadBitmap = BitmapHelper.downloadBitmap(MobSDK.getContext(), shareParams.getImageUrl());
            if (TextUtils.isEmpty(downloadBitmap) || !new File(downloadBitmap).exists()) {
                if (this.listener != null) {
                    this.listener.onError(this, 9, new Throwable("both text and image are null"));
                    return;
                }
                return;
            }
            try {
                b.a(this).f(downloadBitmap);
                if (this.listener != null) {
                    HashMap<String, Object> hashMap3 = new HashMap<>();
                    hashMap3.put("ShareParams", shareParams);
                    this.listener.onComplete(this, 9, hashMap3);
                }
            } catch (Throwable th3) {
                if (this.listener != null) {
                    this.listener.onError(this, 9, th3);
                }
            }
        } catch (Throwable th4) {
            if (this.listener != null) {
                this.listener.onError(this, 9, th4);
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
        aVar.b = shareParams.getText();
        String imageUrl = shareParams.getImageUrl();
        if (imageUrl != null) {
            aVar.d.add(imageUrl);
        } else {
            String imagePath = shareParams.getImagePath();
            if (imagePath != null) {
                aVar.e.add(imagePath);
            }
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

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public int getPlatformId() {
        return 42;
    }

    @Override // cn.sharesdk.framework.Platform
    public int getVersion() {
        return 1;
    }

    @Override // cn.sharesdk.framework.Platform
    public boolean hasShareCallback() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void initDevInfo(String str) {
        this.b = getDevinfo("ChannelID");
        this.c = getDevinfo("ChannelSecret");
        this.d = "lineconnect://success";
    }

    @Override // cn.sharesdk.framework.Platform
    public boolean isClientValid() {
        return b.a(this).d();
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
        try {
            HashMap<String, Object> b = this.a.b();
            if (b == null) {
                if (this.listener != null) {
                    this.listener.onError(this, 8, new Throwable());
                }
            } else {
                this.db.putUserId(String.valueOf(b.get("mid")));
                this.db.put("nickname", String.valueOf(b.get("displayName")));
                this.db.put("icon", String.valueOf(b.get("pictureUrl")));
                if (this.listener != null) {
                    this.listener.onComplete(this, 8, b);
                }
            }
        } catch (Throwable th) {
            e.b().w(th);
            if (this.listener != null) {
                this.listener.onError(this, 8, th);
            }
        }
    }
}
