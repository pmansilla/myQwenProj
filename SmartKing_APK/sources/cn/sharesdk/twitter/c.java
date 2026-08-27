package cn.sharesdk.twitter;

import android.os.Bundle;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import cn.sharesdk.framework.authorize.SSOListener;
import cn.sharesdk.framework.authorize.f;
import cn.sharesdk.framework.utils.e;
import com.luck.picture.lib.config.PictureConfig;
import com.mob.MobSDK;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.utils.BitmapHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import com.sun.mail.imap.IMAPStore;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* compiled from: TwitterHelper.java */
/* loaded from: classes.dex */
public class c extends cn.sharesdk.framework.b {
    private static c b;
    private cn.sharesdk.framework.utils.a c;
    private cn.sharesdk.framework.a.b d;
    private MappedFileReader e;
    private int f;
    private String g;
    private String h;

    private c(Platform platform) {
        super(platform);
        this.f = 0;
        this.c = new cn.sharesdk.framework.utils.a();
        this.d = cn.sharesdk.framework.a.b.a();
    }

    public static c a(Platform platform) {
        if (b == null) {
            b = new c(platform);
        }
        return b;
    }

    private HashMap<String, Object> a(String str, long j, String str2) throws Throwable {
        HashMap<String, Object> f = f(str);
        if (f == null || !((String) f.get("media_id_string")).equals(str)) {
            return null;
        }
        return d(str2, str);
    }

    private HashMap<String, Object> a(String str, String str2, long j) throws Throwable {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>(IMAPStore.ID_COMMAND, "INIT"));
        arrayList.add(new KVPair<>("media_type", "video/mp4"));
        arrayList.add(new KVPair<>("total_bytes", String.valueOf(j)));
        ArrayList<KVPair<String>> a = this.c.a(this.c.a("https://upload.twitter.com/1.1/media/upload.json", arrayList));
        a.remove(1);
        String a2 = this.d.a("https://upload.twitter.com/1.1/media/upload.json", arrayList, (KVPair<String>) null, a, "/1.1/media/upload.json", c());
        if (a2 == null || a2.length() <= 0) {
            return null;
        }
        return new Hashon().fromJson(a2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str, boolean z, UpLoadViewCallBack upLoadViewCallBack, String str2, long j) throws Throwable {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>(IMAPStore.ID_COMMAND, "APPEND"));
        arrayList.add(new KVPair<>("media_id", str2));
        if (z) {
            arrayList.add(new KVPair<>("media_data", str));
        } else {
            arrayList.add(new KVPair<>(PictureConfig.EXTRA_MEDIA, str));
        }
        arrayList.add(new KVPair<>("segment_index", this.f + ""));
        this.f = this.f + 1;
        String a = this.d.a("https://upload.twitter.com/1.1/media/upload.json", arrayList, (KVPair<String>) null, this.c.a(this.c.a("https://upload.twitter.com/1.1/media/upload.json", arrayList)), "/1.1/media/upload.json", c());
        if (upLoadViewCallBack != null) {
            upLoadViewCallBack.onResule(a);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public byte[] a(MappedFileReader mappedFileReader) throws IOException {
        if (mappedFileReader == null || mappedFileReader.read() == -1) {
            return null;
        }
        return mappedFileReader.getArray();
    }

    private String b(String str, final String str2, final long j) throws Throwable {
        this.e = new MappedFileReader(str, 524288);
        byte[] a = a(this.e);
        if (a == null) {
            return null;
        }
        a(MappedFileReader.byteToBase64(a), true, new UpLoadViewCallBack() { // from class: cn.sharesdk.twitter.c.2
            @Override // cn.sharesdk.twitter.UpLoadViewCallBack
            public void onResule(String str3) throws Throwable {
                byte[] a2 = c.this.a(c.this.e);
                if (a2 != null) {
                    c.this.a(MappedFileReader.byteToBase64(a2), true, this, str2, j);
                } else if (c.this.e != null) {
                    c.this.e.close();
                }
            }
        }, str2, j);
        return "Done";
    }

    private HashMap<String, Object> f(String str) throws Throwable {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>(IMAPStore.ID_COMMAND, "FINALIZE"));
        arrayList.add(new KVPair<>("media_id", str));
        ArrayList<KVPair<String>> a = this.c.a(this.c.a("https://upload.twitter.com/1.1/media/upload.json", arrayList));
        a.remove(1);
        String a2 = this.d.a("https://upload.twitter.com/1.1/media/upload.json", arrayList, (KVPair<String>) null, a, "/1.1/media/upload.json", c());
        if (a2 == null || a2.length() <= 0) {
            return null;
        }
        return new Hashon().fromJson(a2);
    }

    public HashMap<String, Object> a(String str, String str2, HashMap<String, Object> hashMap, HashMap<String, String> hashMap2) throws Throwable {
        KVPair<String> kVPair;
        String str3;
        ArrayList<KVPair<String>> a;
        if (str2 == null) {
            return null;
        }
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        if (hashMap != null && hashMap.size() > 0) {
            for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
                arrayList.add(new KVPair<>(entry.getKey(), String.valueOf(entry.getValue())));
            }
        }
        if (hashMap2 == null || hashMap2.size() <= 0) {
            kVPair = null;
        } else {
            KVPair<String> kVPair2 = null;
            for (Map.Entry<String, String> entry2 : hashMap2.entrySet()) {
                kVPair2 = new KVPair<>(entry2.getKey(), entry2.getValue());
            }
            kVPair = kVPair2;
        }
        if ("GET".equals(str2.toUpperCase())) {
            str3 = this.d.httpGet(str, arrayList, this.c.a(this.c.b(str, arrayList)), null);
        } else if ("POST".equals(str2.toUpperCase())) {
            if (hashMap2 == null || hashMap2.size() <= 0) {
                a = this.c.a(this.c.a(str, arrayList));
            } else {
                a = this.c.a(this.c.a(str, new ArrayList<>()));
                a.remove(1);
            }
            str3 = this.d.httpPost(str, arrayList, kVPair, a, (NetworkHelper.NetworkTimeOut) null);
        } else {
            str3 = null;
        }
        if (str3 == null || str3.length() <= 0) {
            return null;
        }
        return new Hashon().fromJson(str3);
    }

    public HashMap<String, Object> a(String str, String[] strArr) throws Throwable {
        String str2;
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        ArrayList<KVPair<String>> a = this.c.a(this.c.a("https://upload.twitter.com/1.1/media/upload.json", arrayList));
        a.remove(1);
        StringBuilder sb = new StringBuilder();
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < strArr.length && arrayList2.size() <= 3; i++) {
            try {
                str2 = strArr[i];
            } catch (Exception unused) {
                e.b().d(sb.toString(), new Object[0]);
            }
            if (str2.startsWith("http")) {
                str2 = BitmapHelper.downloadBitmap(MobSDK.getContext(), str2);
            } else {
                if (!TextUtils.isEmpty(str2)) {
                    if (!new File(str2).exists()) {
                    }
                }
            }
            String a2 = this.d.a("https://upload.twitter.com/1.1/media/upload.json", arrayList, new KVPair<>(PictureConfig.EXTRA_MEDIA, str2), a, "/1.1/media/upload.json", c());
            sb.append(strArr[i]);
            sb.append(": ");
            sb.append(a2);
            sb.append("\n");
            if (a2 != null && a2.length() > 0) {
                arrayList2.add(new Hashon().fromJson(a2));
            }
        }
        sb.setLength(0);
        for (int i2 = 0; i2 < arrayList2.size(); i2++) {
            if (((HashMap) arrayList2.get(i2)).containsKey(PictureConfig.IMAGE)) {
                if (sb.length() > 0) {
                    sb.append(',');
                }
                sb.append(String.valueOf(((HashMap) arrayList2.get(i2)).get("media_id")));
            }
        }
        return d(str, sb.toString());
    }

    public void a(final AuthorizeListener authorizeListener, boolean z) {
        if (z) {
            b(authorizeListener);
        } else {
            a(new SSOListener() { // from class: cn.sharesdk.twitter.c.1
                @Override // cn.sharesdk.framework.authorize.SSOListener
                public void onCancel() {
                    try {
                        authorizeListener.onCancel();
                    } catch (Exception e) {
                        e.b().d(e);
                    }
                }

                @Override // cn.sharesdk.framework.authorize.SSOListener
                public void onComplete(Bundle bundle) {
                    try {
                        authorizeListener.onComplete(bundle);
                    } catch (Throwable th) {
                        onFailed(th);
                    }
                }

                @Override // cn.sharesdk.framework.authorize.SSOListener
                public void onFailed(Throwable th) {
                    c.this.b(authorizeListener);
                }
            });
        }
    }

    public void a(String str) {
        this.g = str;
    }

    public void a(String str, String str2) {
        this.c.a(str, str2);
    }

    public void a(String str, String str2, String str3) {
        this.c.a(str, str2, str3);
        a(str);
        b(str2);
    }

    public HashMap<String, Object> b(String str, String str2) throws Throwable {
        boolean z;
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("nextCursor", str2));
        try {
            ResHelper.parseLong(str);
            z = true;
        } catch (Throwable unused) {
            z = false;
        }
        if (z) {
            arrayList.add(new KVPair<>("user_id", str));
        } else {
            arrayList.add(new KVPair<>("screen_name", str));
        }
        String a = this.d.a("https://api.twitter.com/1.1/friends/list.json", arrayList, this.c.a(this.c.b("https://api.twitter.com/1.1/friends/list.json", arrayList)), (NetworkHelper.NetworkTimeOut) null, "/1.1/friends/list.json", c());
        if (a == null || a.length() <= 0) {
            return null;
        }
        return new Hashon().fromJson(a);
    }

    public void b(String str) {
        this.h = str;
    }

    public String c(String str) {
        try {
            ArrayList<KVPair<String>> arrayList = new ArrayList<>();
            arrayList.add(new KVPair<>("oauth_verifier", str));
            return this.d.a("https://api.twitter.com/oauth/access_token", arrayList, (KVPair<String>) null, this.c.a(this.c.a("https://api.twitter.com/oauth/access_token", arrayList)), "/oauth/access_token", c());
        } catch (Throwable th) {
            e.b().d(th);
            return null;
        }
    }

    public HashMap<String, Object> c(String str, String str2) throws Throwable {
        boolean z;
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("nextCursor", str2));
        try {
            ResHelper.parseLong(str);
            z = true;
        } catch (Throwable unused) {
            z = false;
        }
        if (z) {
            arrayList.add(new KVPair<>("user_id", str));
        } else {
            arrayList.add(new KVPair<>("screen_name", str));
        }
        String a = this.d.a("https://api.twitter.com/1.1/followers/list.json", arrayList, this.c.a(this.c.b("https://api.twitter.com/1.1/followers/list.json", arrayList)), (NetworkHelper.NetworkTimeOut) null, "/1.1/followers/list.json", c());
        if (a == null || a.length() <= 0) {
            return null;
        }
        return new Hashon().fromJson(a);
    }

    public HashMap<String, Object> d(String str) throws Throwable {
        long j;
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        try {
            j = ResHelper.parseLong(str);
        } catch (Throwable unused) {
            j = 0;
            str = null;
        }
        arrayList.add(new KVPair<>("user_id", str == null ? this.a.getDb().getUserId() : String.valueOf(j)));
        String a = this.d.a("https://api.twitter.com/1.1/users/show.json", arrayList, this.c.a(this.c.b("https://api.twitter.com/1.1/users/show.json", arrayList)), (NetworkHelper.NetworkTimeOut) null, "/1.1/users/show.json", c());
        if (a == null || a.length() <= 0) {
            return null;
        }
        return new Hashon().fromJson(a);
    }

    public HashMap<String, Object> d(String str, String str2) throws Throwable {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("status", str));
        if (!TextUtils.isEmpty(str2)) {
            arrayList.add(new KVPair<>("media_ids", str2));
        }
        String a = this.d.a("https://api.twitter.com/1.1/statuses/update.json", arrayList, (KVPair<String>) null, this.c.a(this.c.a("https://api.twitter.com/1.1/statuses/update.json", arrayList)), "/1.1/statuses/update.json", c());
        if (a == null || a.length() <= 0) {
            return null;
        }
        return new Hashon().fromJson(a);
    }

    public HashMap<String, Object> e(String str) throws Throwable {
        return d(str, null);
    }

    public HashMap<String, Object> e(String str, String str2) throws Throwable {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        ArrayList<KVPair<String>> a = this.c.a(this.c.a("https://api.twitter.com/1.1/statuses/update_with_media.json", arrayList));
        a.remove(1);
        arrayList.add(new KVPair<>("status", str));
        String a2 = this.d.a("https://api.twitter.com/1.1/statuses/update_with_media.json", arrayList, new KVPair<>("media[]", str2), a, "/1.1/statuses/update_with_media.json", c());
        if (a2 == null || a2.length() <= 0) {
            return null;
        }
        return new Hashon().fromJson(a2);
    }

    public HashMap<String, Object> f(String str, String str2) throws Throwable {
        long fileSize = ResHelper.getFileSize(str2);
        this.f = 0;
        HashMap<String, Object> a = a(str, str2, fileSize);
        if (a == null) {
            return null;
        }
        String str3 = (String) a.get("media_id_string");
        if (TextUtils.isEmpty(str3) || b(str2, str3, fileSize) == null) {
            return null;
        }
        return a(str3, fileSize, str);
    }

    @Override // cn.sharesdk.framework.authorize.AuthorizeHelper
    public String getAuthorizeUrl() {
        String a;
        try {
            ArrayList<KVPair<String>> arrayList = new ArrayList<>();
            arrayList.add(new KVPair<>("oauth_callback", getRedirectUri()));
            a((String) null, (String) null);
            a = this.d.a("https://api.twitter.com/oauth/request_token", arrayList, (KVPair<String>) null, this.c.a(this.c.a("https://api.twitter.com/oauth/request_token", arrayList)), "/oauth/request_token", c());
        } catch (Throwable th) {
            e.b().d(th);
        }
        if (a == null) {
            return null;
        }
        String[] split = a.split("&");
        HashMap hashMap = new HashMap();
        for (String str : split) {
            if (str != null) {
                String[] split2 = str.split("=");
                if (split2.length >= 2) {
                    hashMap.put(split2[0], split2[1]);
                }
            }
        }
        if (hashMap.containsKey("oauth_token")) {
            String str2 = (String) hashMap.get("oauth_token");
            a(str2, (String) hashMap.get("oauth_token_secret"));
            ShareSDK.logApiEvent("/oauth/authorize", c());
            return "https://api.twitter.com/oauth/authorize?oauth_token=" + str2;
        }
        return null;
    }

    @Override // cn.sharesdk.framework.authorize.AuthorizeHelper
    public cn.sharesdk.framework.authorize.b getAuthorizeWebviewClient(f fVar) {
        return new b(fVar);
    }

    @Override // cn.sharesdk.framework.authorize.AuthorizeHelper
    public String getRedirectUri() {
        return this.c.a().e;
    }

    @Override // cn.sharesdk.framework.b, cn.sharesdk.framework.authorize.AuthorizeHelper
    public cn.sharesdk.framework.authorize.d getSSOProcessor(cn.sharesdk.framework.authorize.c cVar) {
        d dVar = new d(cVar);
        dVar.a(this.g);
        dVar.b(this.h);
        return dVar;
    }
}
