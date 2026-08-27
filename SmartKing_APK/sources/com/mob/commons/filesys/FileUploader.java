package com.mob.commons.filesys;

import android.text.TextUtils;
import com.amap.location.common.model.AmapLoc;
import com.liulishuo.filedownloader.model.ConnectionModel;
import com.mob.MobSDK;
import com.mob.commons.ForbThrowable;
import com.mob.commons.MobProduct;
import com.mob.commons.authorize.DeviceAuthorizer;
import com.mob.commons.b;
import com.mob.commons.j;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.proguard.PublicMemberKeeper;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.Hashon;
import com.tencent.bugly.BuglyStrategy;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import me.panpf.sketch.uri.FileUriModel;

/* loaded from: classes.dex */
public class FileUploader implements PublicMemberKeeper {
    private static String a = j.c("http://up.sdk.mob.com");

    /* loaded from: classes.dex */
    public static class UploadedAudio extends UploadedFile {
        public final int durationUSec;

        public UploadedAudio(HashMap<String, Object> hashMap) {
            super(hashMap);
            int i;
            try {
                i = Integer.parseInt(String.valueOf(hashMap.get("time")));
            } catch (Throwable unused) {
                i = -1;
            }
            this.durationUSec = i;
        }
    }

    /* loaded from: classes.dex */
    public static class UploadedAvatar extends UploadedImage {
        public UploadedAvatar(HashMap<String, Object> hashMap) {
            super(hashMap);
        }
    }

    /* loaded from: classes.dex */
    public static class UploadedFile implements PublicMemberKeeper {
        public final String id;
        public final String url;

        public UploadedFile(HashMap<String, Object> hashMap) {
            if (hashMap.containsKey("src")) {
                this.url = (String) hashMap.get("src");
            } else {
                this.url = null;
            }
            if (hashMap.containsKey(ConnectionModel.ID)) {
                this.id = (String) hashMap.get(ConnectionModel.ID);
            } else {
                this.id = null;
            }
        }
    }

    /* loaded from: classes.dex */
    public static class UploadedImage extends UploadedFile {
        public final HashMap<String, String> zoomList;

        public UploadedImage(HashMap<String, Object> hashMap) {
            super(hashMap);
            HashMap<String, String> hashMap2;
            try {
                hashMap2 = (HashMap) hashMap.get("list");
            } catch (Throwable unused) {
                hashMap2 = null;
            }
            this.zoomList = hashMap2;
        }
    }

    /* loaded from: classes.dex */
    public static class UploadedVideo extends UploadedFile {
        public final int durationUSec;
        public final int height;
        public final int width;

        public UploadedVideo(HashMap<String, Object> hashMap) {
            super(hashMap);
            int i;
            int i2;
            int i3;
            try {
                i = Integer.parseInt(String.valueOf(hashMap.get("time")));
            } catch (Throwable unused) {
                i = -1;
            }
            try {
                i2 = Integer.parseInt(String.valueOf(hashMap.get("height")));
                try {
                    i3 = Integer.parseInt(String.valueOf(hashMap.get("width")));
                } catch (Throwable unused2) {
                    i3 = -1;
                    this.durationUSec = i;
                    this.width = i3;
                    this.height = i2;
                }
            } catch (Throwable unused3) {
                i2 = -1;
                i3 = -1;
                this.durationUSec = i;
                this.width = i3;
                this.height = i2;
            }
            this.durationUSec = i;
            this.width = i3;
            this.height = i2;
        }
    }

    private static String a() {
        return a + "/image";
    }

    private static String a(MobProduct mobProduct) throws Throwable {
        NetworkHelper networkHelper = new NetworkHelper();
        ArrayList<KVPair<String>> a2 = a(mobProduct, "1234567890abcdeffedcba0987654321");
        NetworkHelper.NetworkTimeOut networkTimeOut = new NetworkHelper.NetworkTimeOut();
        networkTimeOut.readTimout = BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH;
        networkTimeOut.connectionTimeout = 5000;
        String httpPost = networkHelper.httpPost(f(), (ArrayList<KVPair<String>>) null, (KVPair<String>) null, a2, networkTimeOut);
        HashMap fromJson = new Hashon().fromJson(httpPost);
        if (fromJson == null || !"200".equals(String.valueOf(fromJson.get("status")))) {
            throw new Throwable(httpPost);
        }
        try {
            return (String) ((HashMap) fromJson.get("res")).get("token");
        } catch (Throwable th) {
            throw new Throwable(httpPost, th);
        }
    }

    private static Throwable a(long j, long j2) {
        return new Throwable("{\"status\": ,\"error\":\"max size: " + j2 + ", file size: " + j + "\"}");
    }

    private static ArrayList<KVPair<String>> a(MobProduct mobProduct, String str) {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("sign", Data.MD5(str + MobSDK.getAppSecret())));
        arrayList.add(new KVPair<>("key", MobSDK.getAppkey()));
        arrayList.add(new KVPair<>("token", str));
        arrayList.add(new KVPair<>("product", mobProduct.getProductTag()));
        arrayList.add(new KVPair<>("cliid", DeviceAuthorizer.authorize(mobProduct)));
        return arrayList;
    }

    private static HashMap<String, Object> a(MobProduct mobProduct, String str, HashMap<String, Object> hashMap, String str2, long j) throws Throwable {
        if (b.ad()) {
            throw new ForbThrowable();
        }
        File file = new File(str);
        if (file.length() > j) {
            throw a(file.length(), j);
        }
        NetworkHelper networkHelper = new NetworkHelper();
        ArrayList<KVPair<String>> a2 = a(mobProduct, a(mobProduct));
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        for (String str3 : hashMap.keySet()) {
            arrayList.add(new KVPair<>(str3, String.valueOf(hashMap.get(str3))));
        }
        KVPair<String> kVPair = new KVPair<>(AmapLoc.TYPE_OFFLINE_CELL, str);
        NetworkHelper.NetworkTimeOut networkTimeOut = new NetworkHelper.NetworkTimeOut();
        networkTimeOut.readTimout = BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH;
        networkTimeOut.connectionTimeout = 5000;
        String httpPost = networkHelper.httpPost(str2, arrayList, kVPair, a2, networkTimeOut);
        HashMap fromJson = new Hashon().fromJson(httpPost);
        if (fromJson == null || !"200".equals(String.valueOf(fromJson.get("status")))) {
            throw new Throwable(httpPost);
        }
        return (HashMap) fromJson.get("res");
    }

    private static HashMap<String, Object> a(MobProduct mobProduct, String str, boolean z, String str2, String str3) throws Throwable {
        HashMap hashMap = new HashMap();
        hashMap.put("sm", Integer.valueOf(z ? 2 : 1));
        if (!TextUtils.isEmpty(str2)) {
            hashMap.put("zoomScaleWidths", str2);
        }
        return a(mobProduct, str, (HashMap<String, Object>) hashMap, str3, 10485760L);
    }

    private static String b() {
        return a + "/avatar";
    }

    private static String c() {
        return a + "/video";
    }

    private static String d() {
        return a + "/audio";
    }

    private static String e() {
        return a + "/file";
    }

    private static String f() {
        return a + "/getToken";
    }

    public static void setUploadServer(String str) {
        if (str.endsWith(FileUriModel.SCHEME)) {
            str = str.substring(0, str.length() - 1);
        }
        a = j.c(str);
    }

    public static UploadedAudio uploadAudio(MobProduct mobProduct, String str, boolean z) throws Throwable {
        HashMap hashMap = new HashMap();
        hashMap.put("sm", Integer.valueOf(z ? 2 : 1));
        return new UploadedAudio(a(mobProduct, str, (HashMap<String, Object>) hashMap, d(), 209715200L));
    }

    public static UploadedAvatar uploadAvatar(MobProduct mobProduct, String str) throws Throwable {
        return uploadAvatar(mobProduct, str, false, new int[0]);
    }

    public static UploadedAvatar uploadAvatar(MobProduct mobProduct, String str, boolean z, int... iArr) throws Throwable {
        String str2 = null;
        if (iArr != null && iArr.length > 0) {
            String str3 = "";
            for (int i : iArr) {
                str3 = str3 + "," + i;
            }
            if (str3.length() > 0) {
                str2 = str3.substring(1);
            }
        }
        return new UploadedAvatar(a(mobProduct, str, z, str2, b()));
    }

    public static UploadedFile uploadFile(MobProduct mobProduct, String str, boolean z) throws Throwable {
        HashMap hashMap = new HashMap();
        hashMap.put("sm", Integer.valueOf(z ? 2 : 1));
        return new UploadedFile(a(mobProduct, str, (HashMap<String, Object>) hashMap, e(), 52428800L));
    }

    public static UploadedImage uploadImage(MobProduct mobProduct, String str) throws Throwable {
        return uploadImage(mobProduct, str, false, new int[0]);
    }

    public static UploadedImage uploadImage(MobProduct mobProduct, String str, boolean z, int... iArr) throws Throwable {
        String str2 = null;
        if (iArr != null && iArr.length > 0) {
            String str3 = "";
            for (int i : iArr) {
                str3 = str3 + "," + i;
            }
            if (str3.length() > 0) {
                str2 = str3.substring(1);
            }
        }
        return new UploadedImage(a(mobProduct, str, z, str2, a()));
    }

    public static UploadedVideo uploadVideo(MobProduct mobProduct, String str, boolean z) throws Throwable {
        HashMap hashMap = new HashMap();
        hashMap.put("sm", Integer.valueOf(z ? 2 : 1));
        return new UploadedVideo(a(mobProduct, str, (HashMap<String, Object>) hashMap, c(), 209715200L));
    }
}
