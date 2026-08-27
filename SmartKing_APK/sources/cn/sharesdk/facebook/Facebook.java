package cn.sharesdk.facebook;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import cn.sharesdk.framework.b.b.f;
import com.amap.location.common.model.AmapLoc;
import com.autonavi.amap.mapcore.AeUtil;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.liulishuo.filedownloader.model.ConnectionModel;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.mob.MobSDK;
import com.mob.tools.utils.BitmapHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import com.sun.mail.imap.IMAPStore;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import me.panpf.sketch.uri.FileUriModel;

/* loaded from: classes.dex */
public class Facebook extends Platform {
    public static final String NAME = "Facebook";
    private String a;
    private String b;
    private boolean c;

    /* loaded from: classes.dex */
    public static class ShareParams extends Platform.ShareParams {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public boolean checkAuthorize(int i, Object obj) {
        if ((i == 9 && this.c && isClientValid()) || i == 6) {
            return true;
        }
        if (isAuthValid()) {
            d a = d.a(this);
            a.a(this.a);
            String token = this.db.getToken();
            String valueOf = String.valueOf(this.db.getExpiresIn());
            if (token != null && valueOf != null) {
                a.a(token, valueOf);
                if (a.a()) {
                    return true;
                }
            }
        } else if ((obj instanceof Platform.ShareParams) && ((Platform.ShareParams) obj).getShareType() == 4) {
            return true;
        }
        innerAuthorize(i, obj);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void doAuthorize(String[] strArr) {
        final d a = d.a(this);
        a.a(this.a);
        a.d(this.b);
        a.a(strArr);
        a.a(new AuthorizeListener() { // from class: cn.sharesdk.facebook.Facebook.1
            @Override // cn.sharesdk.framework.authorize.AuthorizeListener
            public void onCancel() {
                if (Facebook.this.listener != null) {
                    Facebook.this.listener.onCancel(Facebook.this, 1);
                }
            }

            @Override // cn.sharesdk.framework.authorize.AuthorizeListener
            public void onComplete(Bundle bundle) {
                String string = bundle.getString("oauth_token");
                int i = bundle.getInt("oauth_token_expires");
                if (i == 0) {
                    try {
                        i = ResHelper.parseInt(String.valueOf(bundle.get("expires_in")));
                    } catch (Throwable th) {
                        cn.sharesdk.framework.utils.e.b().d(th);
                        i = 0;
                    }
                }
                if (TextUtils.isEmpty(string)) {
                    string = bundle.getString("access_token");
                }
                Facebook.this.db.putToken(string);
                Facebook.this.db.putExpiresIn(i);
                a.a(string, String.valueOf(i));
                Facebook.this.afterRegister(1, null);
            }

            @Override // cn.sharesdk.framework.authorize.AuthorizeListener
            public void onError(Throwable th) {
                if (Facebook.this.listener != null) {
                    Facebook.this.listener.onError(Facebook.this, 1, th);
                }
            }
        }, isSSODisable());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void doCustomerProtocol(String str, String str2, int i, HashMap<String, Object> hashMap, HashMap<String, String> hashMap2) {
        try {
            HashMap<String, Object> a = d.a(this).a(str, str2, hashMap, hashMap2);
            if (a != null && a.size() > 0) {
                if (!a.containsKey("error_code") && !a.containsKey("error")) {
                    if (this.listener != null) {
                        this.listener.onComplete(this, i, a);
                        return;
                    }
                    return;
                }
                if (this.listener != null) {
                    this.listener.onError(this, i, new Throwable(new Hashon().fromHashMap(a)));
                    return;
                }
                return;
            }
            if (this.listener != null) {
                this.listener.onError(this, i, new Throwable("response is null"));
            }
        } catch (Throwable th) {
            if (this.listener != null) {
                this.listener.onError(this, i, th);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void doShare(final Platform.ShareParams shareParams) {
        HashMap<String, Object> b;
        d a = d.a(this);
        a.a(this.a);
        try {
            int i = 0;
            String shortLintk = getShortLintk(shareParams.getText(), false);
            String imagePath = shareParams.getImagePath();
            String imageUrl = shareParams.getImageUrl();
            String url = shareParams.getUrl();
            List<String> arrayList = new ArrayList();
            if (shareParams.getImageArray() != null) {
                arrayList = Arrays.asList(shareParams.getImageArray());
            }
            if (!this.c || !a.b()) {
                if (!TextUtils.isEmpty(url)) {
                    if (TextUtils.isEmpty(imageUrl) && !TextUtils.isEmpty(imagePath) && new File(imagePath).exists()) {
                        shareParams.setImageUrl(uploadImageToFileServer(imagePath));
                    }
                    a.a(shareParams, new PlatformActionListener() { // from class: cn.sharesdk.facebook.Facebook.2
                        @Override // cn.sharesdk.framework.PlatformActionListener
                        public void onCancel(Platform platform, int i2) {
                            if (Facebook.this.listener != null) {
                                Facebook.this.listener.onCancel(Facebook.this, 9);
                            }
                        }

                        @Override // cn.sharesdk.framework.PlatformActionListener
                        public void onComplete(Platform platform, int i2, HashMap<String, Object> hashMap) {
                            if (Facebook.this.listener != null) {
                                hashMap.put("ShareParams", shareParams);
                                Facebook.this.listener.onComplete(Facebook.this, 9, hashMap);
                            }
                        }

                        @Override // cn.sharesdk.framework.PlatformActionListener
                        public void onError(Platform platform, int i2, Throwable th) {
                            if (Facebook.this.listener != null) {
                                Facebook.this.listener.onError(Facebook.this, 9, th);
                            }
                        }
                    });
                    return;
                }
                if (!TextUtils.isEmpty(imagePath) && new File(imagePath).exists()) {
                    b = a.b(shortLintk, imagePath);
                } else if (TextUtils.isEmpty(imageUrl)) {
                    b = a.b(shortLintk);
                } else {
                    File file = new File(BitmapHelper.downloadBitmap(MobSDK.getContext(), imageUrl));
                    b = file.exists() ? a.b(shortLintk, file.getAbsolutePath()) : a.b(shortLintk);
                }
                if (b != null && b.size() > 0) {
                    if (!b.containsKey("error_code") && !b.containsKey("error")) {
                        if (this.listener != null) {
                            b.put("ShareParams", shareParams);
                            this.listener.onComplete(this, 9, b);
                            return;
                        }
                        return;
                    }
                    if (this.listener != null) {
                        this.listener.onError(this, 9, new Throwable(new Hashon().fromHashMap(b)));
                        return;
                    }
                    return;
                }
                if (this.listener != null) {
                    this.listener.onError(this, 9, new Throwable("response is null"));
                    return;
                }
                return;
            }
            if (shareParams.getShareType() == 7) {
                e eVar = new e();
                eVar.a(this.listener, this, shareParams);
                eVar.a(this.a);
                eVar.show(MobSDK.getContext(), null);
                return;
            }
            if (arrayList != null && arrayList.size() > 0) {
                for (String str : arrayList) {
                    if (str.startsWith("http")) {
                        str = BitmapHelper.downloadBitmap(MobSDK.getContext(), str);
                        arrayList.set(i, str);
                    }
                    File file2 = new File(str);
                    if (file2.exists() && str.startsWith("/data/")) {
                        arrayList.remove(str);
                        File file3 = new File(ResHelper.getCachePath(MobSDK.getContext(), "images"), System.currentTimeMillis() + file2.getName());
                        String absolutePath = file3.getAbsolutePath();
                        file3.createNewFile();
                        if (ResHelper.copyFile(str, absolutePath)) {
                            arrayList.add(file2.getAbsolutePath());
                        }
                    }
                    i++;
                }
                a.a(this.listener, shareParams);
            }
            if (TextUtils.isEmpty(imagePath) || !new File(imagePath).exists()) {
                Bitmap imageData = shareParams.getImageData();
                if (imageData != null && !imageData.isRecycled()) {
                    File file4 = new File(ResHelper.getCachePath(MobSDK.getContext(), "images"), System.currentTimeMillis() + PictureMimeType.PNG);
                    FileOutputStream fileOutputStream = new FileOutputStream(file4);
                    imageData.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    imagePath = file4.getAbsolutePath();
                } else if (!TextUtils.isEmpty(imageUrl)) {
                    imagePath = BitmapHelper.downloadBitmap(MobSDK.getContext(), imageUrl);
                }
            }
            if (!TextUtils.isEmpty(imagePath)) {
                arrayList.add(imagePath);
                shareParams.setImageArray((String[]) arrayList.toArray(new String[arrayList.size()]));
            }
            a.a(this.listener, shareParams);
        } catch (Throwable th) {
            if (this.listener != null) {
                this.listener.onError(this, 9, th);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v25 */
    /* JADX WARN: Type inference failed for: r0v26, types: [java.util.ArrayList] */
    /* JADX WARN: Type inference failed for: r0v98 */
    @Override // cn.sharesdk.framework.Platform
    protected HashMap<String, Object> filterFriendshipInfo(int i, HashMap<String, Object> hashMap) {
        Object obj = hashMap.get(AeUtil.ROOT_DATA_PATH_OLD_NAME);
        HashMap hashMap2 = null;
        if (obj == null) {
            return null;
        }
        HashMap<String, Object> hashMap3 = new HashMap<>();
        hashMap3.put("type", "FOLLOWING");
        hashMap3.put("snsplat", Integer.valueOf(getPlatformId()));
        hashMap3.put("snsuid", this.db.getUserId());
        int intValue = ((Integer) hashMap.get("current_cursor")).intValue();
        int intValue2 = ((Integer) hashMap.get("current_limit")).intValue();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = (ArrayList) obj;
        if (arrayList2.size() <= 0) {
            return null;
        }
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            HashMap hashMap4 = (HashMap) it.next();
            if (hashMap4 != null) {
                HashMap hashMap5 = new HashMap();
                hashMap5.put("snsuid", String.valueOf(hashMap4.get(ConnectionModel.ID)));
                hashMap5.put("nickname", String.valueOf(hashMap4.get(IMAPStore.ID_NAME)));
                hashMap5.put("gender", "male".equals(String.valueOf(hashMap4.get("gender"))) ? AmapLoc.RESULT_TYPE_GPS : AmapLoc.RESULT_TYPE_WIFI_ONLY);
                hashMap5.put("secretType", "true".equals(String.valueOf(hashMap4.get("verified"))) ? AmapLoc.RESULT_TYPE_WIFI_ONLY : AmapLoc.RESULT_TYPE_GPS);
                hashMap5.put("snsUserUrl", String.valueOf(hashMap4.get("link")));
                hashMap5.put("resume", String.valueOf(hashMap4.get("link")));
                HashMap hashMap6 = hashMap4.containsKey(PictureConfig.FC_TAG) ? (HashMap) hashMap4.get(PictureConfig.FC_TAG) : hashMap2;
                if (hashMap6 != null) {
                    HashMap hashMap7 = hashMap6.containsKey(AeUtil.ROOT_DATA_PATH_OLD_NAME) ? (HashMap) hashMap6.get(AeUtil.ROOT_DATA_PATH_OLD_NAME) : hashMap2;
                    if (hashMap7 != null) {
                        hashMap5.put("icon", String.valueOf(hashMap7.get(FileDownloadModel.URL)));
                    }
                }
                try {
                    if (hashMap4.containsKey("birthday")) {
                        String[] split = String.valueOf(hashMap4.get("birthday")).split(FileUriModel.SCHEME);
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(1, ResHelper.parseInt(split[2]));
                        calendar.set(2, ResHelper.parseInt(split[0]) - 1);
                        calendar.set(5, ResHelper.parseInt(split[1]));
                        hashMap5.put("birthday", String.valueOf(calendar.getTimeInMillis()));
                    }
                } catch (Throwable th) {
                    cn.sharesdk.framework.utils.e.b().d(th);
                }
                ?? r0 = hashMap4.containsKey("education") ? (ArrayList) hashMap4.get("education") : hashMap2;
                if (r0 != 0) {
                    ArrayList arrayList3 = new ArrayList();
                    Iterator it2 = r0.iterator();
                    while (it2.hasNext()) {
                        HashMap hashMap8 = (HashMap) it2.next();
                        HashMap hashMap9 = new HashMap();
                        hashMap9.put("school_type", 0);
                        HashMap hashMap10 = (HashMap) hashMap8.get("school");
                        if (hashMap10 != null) {
                            hashMap9.put("school", String.valueOf(hashMap10.get(IMAPStore.ID_NAME)));
                        }
                        try {
                            hashMap9.put("year", Integer.valueOf(ResHelper.parseInt(String.valueOf(((HashMap) hashMap8.get("year")).get(IMAPStore.ID_NAME)))));
                        } catch (Throwable th2) {
                            cn.sharesdk.framework.utils.e.b().d(th2);
                        }
                        hashMap9.put("background", 0);
                        arrayList3.add(hashMap9);
                    }
                    HashMap hashMap11 = new HashMap();
                    hashMap11.put("list", arrayList3);
                    String fromHashMap = new Hashon().fromHashMap(hashMap11);
                    hashMap5.put("educationJSONArrayStr", fromHashMap.substring(8, fromHashMap.length() - 1));
                }
                ArrayList arrayList4 = hashMap4.containsKey("work") ? (ArrayList) hashMap4.get("work") : null;
                if (arrayList4 != null) {
                    ArrayList arrayList5 = new ArrayList();
                    Iterator it3 = arrayList4.iterator();
                    while (it3.hasNext()) {
                        HashMap hashMap12 = (HashMap) it3.next();
                        HashMap hashMap13 = new HashMap();
                        HashMap hashMap14 = (HashMap) hashMap12.get("employer");
                        if (hashMap14 != null) {
                            hashMap13.put("company", String.valueOf(hashMap14.get(IMAPStore.ID_NAME)));
                        }
                        HashMap hashMap15 = (HashMap) hashMap12.get(PictureConfig.EXTRA_POSITION);
                        if (hashMap15 != null) {
                            hashMap13.put(PictureConfig.EXTRA_POSITION, String.valueOf(hashMap15.get(IMAPStore.ID_NAME)));
                        }
                        try {
                            String[] split2 = String.valueOf(hashMap12.get("start_date")).split("-");
                            hashMap13.put("start_date", Integer.valueOf((ResHelper.parseInt(split2[0]) * 100) + ResHelper.parseInt(split2[1])));
                        } catch (Throwable th3) {
                            cn.sharesdk.framework.utils.e.b().d(th3);
                        }
                        try {
                            String[] split3 = String.valueOf(hashMap12.get("end_date")).split("-");
                            hashMap13.put("end_date", Integer.valueOf((ResHelper.parseInt(split3[0]) * 100) + ResHelper.parseInt(split3[1])));
                        } catch (Throwable th4) {
                            cn.sharesdk.framework.utils.e.b().d(th4);
                            hashMap13.put("end_date", 0);
                        }
                        arrayList5.add(hashMap13);
                    }
                    HashMap hashMap16 = new HashMap();
                    hashMap16.put("list", arrayList5);
                    String fromHashMap2 = new Hashon().fromHashMap(hashMap16);
                    hashMap5.put("workJSONArrayStr", fromHashMap2.substring(8, fromHashMap2.length() - 1));
                }
                arrayList.add(hashMap5);
                hashMap2 = null;
            }
        }
        if (arrayList.size() <= 0) {
            return null;
        }
        hashMap3.put("nextCursor", (intValue + arrayList.size()) + (intValue2 >= arrayList.size() ? "_true" : "_false"));
        hashMap3.put("list", arrayList);
        return hashMap3;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public f.a filterShareContent(Platform.ShareParams shareParams, HashMap<String, Object> hashMap) {
        f.a aVar = new f.a();
        aVar.b = shareParams.getText();
        if (hashMap != null) {
            if (hashMap != null && hashMap.containsKey(GlideExecutor.DEFAULT_SOURCE_EXECUTOR_NAME)) {
                aVar.d.add(String.valueOf(hashMap.get(GlideExecutor.DEFAULT_SOURCE_EXECUTOR_NAME)));
            } else if (4 == shareParams.getShareType()) {
                aVar.d.add(shareParams.getImageUrl());
                String titleUrl = shareParams.getTitleUrl();
                if (TextUtils.isEmpty(titleUrl)) {
                    titleUrl = shareParams.getUrl();
                }
                aVar.c.add(titleUrl);
            }
            Object obj = hashMap.get("post_id");
            aVar.a = obj == null ? null : String.valueOf(obj);
            aVar.g = hashMap;
        }
        return aVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void follow(String str) {
        if (this.listener != null) {
            this.listener.onCancel(this, 7);
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
        try {
            HashMap<String, Object> a = d.a(this).a(i, i2, str);
            if (a != null && a.size() > 0 && !a.containsKey("error_code") && !a.containsKey("error")) {
                a.put("current_limit", Integer.valueOf(i));
                a.put("current_cursor", Integer.valueOf(i2));
                return filterFriendshipInfo(2, a);
            }
            return null;
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void getFriendList(int i, int i2, String str) {
        try {
            HashMap<String, Object> a = d.a(this).a(i, i2 * i, str);
            if (a != null && a.size() > 0) {
                if (!a.containsKey("error_code") && !a.containsKey("error")) {
                    if (this.listener != null) {
                        this.listener.onComplete(this, 2, a);
                        return;
                    }
                    return;
                }
                if (this.listener != null) {
                    this.listener.onError(this, 2, new Throwable(new Hashon().fromHashMap(a)));
                    return;
                }
                return;
            }
            if (this.listener != null) {
                this.listener.onError(this, 2, new Throwable("response is null"));
            }
        } catch (Throwable th) {
            if (this.listener != null) {
                this.listener.onError(this, 2, th);
            }
        }
    }

    @Override // cn.sharesdk.framework.Platform
    public String getName() {
        return NAME;
    }

    @Override // cn.sharesdk.framework.Platform
    public int getPlatformId() {
        return 10;
    }

    @Override // cn.sharesdk.framework.Platform
    public int getVersion() {
        return 2;
    }

    @Override // cn.sharesdk.framework.Platform
    public boolean hasShareCallback() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void initDevInfo(String str) {
        this.a = getDevinfo("ConsumerKey");
        this.b = getDevinfo("RedirectUrl");
        this.c = "true".equals(getDevinfo("ShareByAppClient"));
    }

    @Override // cn.sharesdk.framework.Platform
    public boolean isClientValid() {
        d a = d.a(this);
        a.a(this.a);
        return a.b();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.sharesdk.framework.Platform
    public void setNetworkDevinfo() {
        this.a = getNetworkDevinfo("api_key", "ConsumerKey");
        this.b = getNetworkDevinfo("redirect_uri", "RedirectUrl");
        if (TextUtils.isEmpty(this.b)) {
            this.b = "fbconnect://success";
        }
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
        HashMap hashMap;
        try {
            HashMap<String, Object> c = d.a(this).c(str);
            if (c != null && c.size() > 0) {
                if (!c.containsKey("error_code") && !c.containsKey("error")) {
                    if (str == null) {
                        this.db.putUserId(String.valueOf(c.get(ConnectionModel.ID)));
                        this.db.put("nickname", String.valueOf(c.get(IMAPStore.ID_NAME)));
                        this.db.put("gender", "male".equals(String.valueOf(c.get("gender"))) ? AmapLoc.RESULT_TYPE_GPS : AmapLoc.RESULT_TYPE_WIFI_ONLY);
                        this.db.put("token_for_business", (String) c.get("token_for_business"));
                        HashMap hashMap2 = c.containsKey(PictureConfig.FC_TAG) ? (HashMap) c.get(PictureConfig.FC_TAG) : null;
                        if (hashMap2 != null && (hashMap = (HashMap) hashMap2.get(AeUtil.ROOT_DATA_PATH_OLD_NAME)) != null) {
                            this.db.put("icon", String.valueOf(hashMap.get(FileDownloadModel.URL)));
                        }
                        try {
                            if (c.containsKey("birthday")) {
                                String[] split = String.valueOf(c.get("birthday")).split(FileUriModel.SCHEME);
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(1, ResHelper.parseInt(split[2]));
                                calendar.set(2, ResHelper.parseInt(split[0]) - 1);
                                calendar.set(5, ResHelper.parseInt(split[1]));
                                this.db.put("birthday", String.valueOf(calendar.getTimeInMillis()));
                            }
                        } catch (Throwable th) {
                            cn.sharesdk.framework.utils.e.b().d(th);
                        }
                        this.db.put("secretType", "true".equals(String.valueOf(c.get("verified"))) ? AmapLoc.RESULT_TYPE_WIFI_ONLY : AmapLoc.RESULT_TYPE_GPS);
                        this.db.put("snsUserUrl", String.valueOf(c.get("link")));
                        this.db.put("resume", String.valueOf(c.get("link")));
                        ArrayList arrayList = c.containsKey("education") ? (ArrayList) c.get("education") : null;
                        if (arrayList != null) {
                            ArrayList arrayList2 = new ArrayList();
                            Iterator it = arrayList.iterator();
                            while (it.hasNext()) {
                                HashMap hashMap3 = (HashMap) it.next();
                                HashMap hashMap4 = new HashMap();
                                hashMap4.put("school_type", 0);
                                HashMap hashMap5 = hashMap3.containsKey("school") ? (HashMap) hashMap3.get("school") : null;
                                if (hashMap5 != null) {
                                    hashMap4.put("school", String.valueOf(hashMap5.get(IMAPStore.ID_NAME)));
                                }
                                try {
                                    hashMap4.put("year", Integer.valueOf(ResHelper.parseInt(String.valueOf((hashMap3.containsKey("year") ? (HashMap) hashMap3.get("year") : null).get(IMAPStore.ID_NAME)))));
                                } catch (Throwable th2) {
                                    cn.sharesdk.framework.utils.e.b().d(th2);
                                }
                                hashMap4.put("background", 0);
                                arrayList2.add(hashMap4);
                            }
                            HashMap hashMap6 = new HashMap();
                            hashMap6.put("list", arrayList2);
                            String fromHashMap = new Hashon().fromHashMap(hashMap6);
                            this.db.put("educationJSONArrayStr", fromHashMap.substring(8, fromHashMap.length() - 1));
                        }
                        ArrayList arrayList3 = c.containsKey("work") ? (ArrayList) c.get("work") : null;
                        if (arrayList3 != null) {
                            ArrayList arrayList4 = new ArrayList();
                            Iterator it2 = arrayList3.iterator();
                            while (it2.hasNext()) {
                                HashMap hashMap7 = (HashMap) it2.next();
                                HashMap hashMap8 = new HashMap();
                                HashMap hashMap9 = (HashMap) hashMap7.get("employer");
                                if (hashMap9 != null) {
                                    hashMap8.put("company", String.valueOf(hashMap9.get(IMAPStore.ID_NAME)));
                                }
                                HashMap hashMap10 = (HashMap) hashMap7.get(PictureConfig.EXTRA_POSITION);
                                if (hashMap10 != null) {
                                    hashMap8.put(PictureConfig.EXTRA_POSITION, String.valueOf(hashMap10.get(IMAPStore.ID_NAME)));
                                }
                                try {
                                    String[] split2 = String.valueOf(hashMap7.get("start_date")).split("-");
                                    hashMap8.put("start_date", Integer.valueOf((ResHelper.parseInt(split2[0]) * 100) + ResHelper.parseInt(split2[1])));
                                } catch (Throwable th3) {
                                    cn.sharesdk.framework.utils.e.b().d(th3);
                                }
                                try {
                                    String[] split3 = String.valueOf(hashMap7.get("end_date")).split("-");
                                    hashMap8.put("end_date", Integer.valueOf((ResHelper.parseInt(split3[0]) * 100) + ResHelper.parseInt(split3[1])));
                                } catch (Throwable th4) {
                                    cn.sharesdk.framework.utils.e.b().d(th4);
                                    hashMap8.put("end_date", 0);
                                }
                                arrayList4.add(hashMap8);
                            }
                            HashMap hashMap11 = new HashMap();
                            hashMap11.put("list", arrayList4);
                            String fromHashMap2 = new Hashon().fromHashMap(hashMap11);
                            this.db.put("workJSONArrayStr", fromHashMap2.substring(8, fromHashMap2.length() - 1));
                        }
                    }
                    if (this.listener != null) {
                        this.listener.onComplete(this, 8, c);
                        return;
                    }
                    return;
                }
                if (this.listener != null) {
                    this.listener.onError(this, 8, new Throwable(new Hashon().fromHashMap(c)));
                    return;
                }
                return;
            }
            if (this.listener != null) {
                this.listener.onError(this, 8, new Throwable("response is null"));
            }
        } catch (Throwable th5) {
            if (this.listener != null) {
                this.listener.onError(this, 8, th5);
            }
        }
    }
}
