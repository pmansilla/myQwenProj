package cn.sharesdk.framework;

import android.text.TextUtils;
import com.amap.location.common.model.AmapLoc;
import com.mob.MobSDK;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.SharePrefrenceHelper;
import java.util.HashMap;

/* loaded from: classes.dex */
public class PlatformDb {
    private static final String DB_NAME = "cn_sharesdk_weibodb";
    private String platformNname;
    private int platformVersion;
    private SharePrefrenceHelper sp = new SharePrefrenceHelper(MobSDK.getContext());

    public PlatformDb(String str, int i) {
        this.sp.open("cn_sharesdk_weibodb_" + str, i);
        this.platformNname = str;
        this.platformVersion = i;
    }

    public String exportData() {
        try {
            HashMap hashMap = new HashMap();
            hashMap.putAll(this.sp.getAll());
            return new Hashon().fromHashMap(hashMap);
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
            return null;
        }
    }

    public String get(String str) {
        return this.sp.getString(str);
    }

    public long getExpiresIn() {
        try {
            try {
                return this.sp.getLong("expiresIn");
            } catch (Throwable unused) {
                return 0L;
            }
        } catch (Throwable unused2) {
            return this.sp.getInt("expiresIn");
        }
    }

    public long getExpiresTime() {
        return this.sp.getLong("expiresTime") + (getExpiresIn() * 1000);
    }

    public String getPlatformNname() {
        return this.platformNname;
    }

    public int getPlatformVersion() {
        return this.platformVersion;
    }

    public String getToken() {
        return this.sp.getString("token");
    }

    public String getTokenSecret() {
        return this.sp.getString("secret");
    }

    public String getUserGender() {
        String string = this.sp.getString("gender");
        if (AmapLoc.RESULT_TYPE_GPS.equals(string)) {
            return "m";
        }
        if (AmapLoc.RESULT_TYPE_WIFI_ONLY.equals(string)) {
            return "f";
        }
        return null;
    }

    public String getUserIcon() {
        return this.sp.getString("icon");
    }

    public String getUserId() {
        String string = this.sp.getString("userID");
        return TextUtils.isEmpty(string) ? this.sp.getString("weibo") : string;
    }

    public String getUserName() {
        return this.sp.getString("nickname");
    }

    public void importData(String str) {
        try {
            HashMap<String, Object> fromJson = new Hashon().fromJson(str);
            if (fromJson != null) {
                this.sp.putAll(fromJson);
            }
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
        }
    }

    public boolean isValid() {
        String token = getToken();
        if (token == null || token.length() <= 0) {
            return false;
        }
        return getExpiresIn() == 0 || getExpiresTime() > System.currentTimeMillis();
    }

    public void put(String str, String str2) {
        this.sp.putString(str, str2);
    }

    public void putExpiresIn(long j) {
        this.sp.putLong("expiresIn", Long.valueOf(j));
        this.sp.putLong("expiresTime", Long.valueOf(System.currentTimeMillis()));
    }

    public void putToken(String str) {
        this.sp.putString("token", str);
    }

    public void putTokenSecret(String str) {
        this.sp.putString("secret", str);
    }

    public void putUserId(String str) {
        this.sp.putString("userID", str);
    }

    public void removeAccount() {
        this.sp.clear();
    }
}
