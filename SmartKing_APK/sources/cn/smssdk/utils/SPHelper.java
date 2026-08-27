package cn.smssdk.utils;

import android.text.TextUtils;
import com.mob.MobSDK;
import com.mob.tools.utils.SharePrefrenceHelper;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class SPHelper {
    private static SPHelper c;
    private SharePrefrenceHelper a = new SharePrefrenceHelper(MobSDK.getContext());
    private SharePrefrenceHelper b;

    private SPHelper() {
        this.a.open("SMSSDK", 2);
        this.b = new SharePrefrenceHelper(MobSDK.getContext());
        this.b.open("SMSSDK_VCODE", 1);
    }

    public static SPHelper getInstance() {
        if (c == null) {
            c = new SPHelper();
        }
        return c;
    }

    public void clearBuffer() {
        this.a.remove("bufferedNewFriends");
        this.a.remove("bufferedFriends");
        this.a.remove("lastRequestNewFriendsTime");
        this.a.remove("bufferedContactPhones");
    }

    public void clearLog() {
        synchronized ("KEY_LOG") {
            this.b.remove("KEY_LOG");
        }
    }

    public String[] getBufferedContactPhones() {
        Object obj = this.a.get("bufferedContactPhones");
        return obj != null ? (String[]) obj : new String[0];
    }

    public ArrayList<HashMap<String, Object>> getBufferedContacts() {
        Object obj = this.a.get("bufferedContacts");
        return obj != null ? (ArrayList) obj : new ArrayList<>();
    }

    public String getBufferedContactsSignature() {
        return this.a.getString("bufferedContactsSignature");
    }

    public String getBufferedCountrylist() {
        return this.a.getString("bufferedCountryList");
    }

    public ArrayList<HashMap<String, Object>> getBufferedFriends() {
        synchronized ("bufferedFriends") {
            Object obj = this.a.get("bufferedFriends");
            if (obj != null) {
                return (ArrayList) obj;
            }
            return new ArrayList<>();
        }
    }

    public ArrayList<HashMap<String, Object>> getBufferedNewFriends() {
        Object obj = this.a.get("bufferedNewFriends");
        return obj != null ? (ArrayList) obj : new ArrayList<>();
    }

    public String getConfig() throws Throwable {
        String a;
        String string = this.a.getString("config");
        if (TextUtils.isEmpty(string) || (a = cn.smssdk.net.c.a(string)) == null) {
            return null;
        }
        return a;
    }

    public long getLastRequestNewFriendsTime() {
        return this.a.getLong("lastRequestNewFriendsTime");
    }

    public long getLastRequestTimeMillis(String str) {
        return this.a.getLong(str);
    }

    public long getLastZoneAt() {
        return this.a.getLong("lastZoneAt");
    }

    public String getLog() {
        return this.b.getString("KEY_LOG");
    }

    public String getSMSID() {
        return this.b.getString("KEY_SMSID");
    }

    public String getToken() {
        return this.a.getString("token");
    }

    public long getTokenCacheAt() {
        return this.a.getLong("token_cache_at", 0L);
    }

    public String getVCodeHash() {
        return this.b.getString("KEY_VCODE_HASH");
    }

    public String getVerifyCountry() throws Throwable {
        String string = this.a.getString("verify_country");
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        return (String) cn.smssdk.net.c.a(MobSDK.getAppkey(), string);
    }

    public String getVerifyPhone() throws Throwable {
        String string = this.a.getString("verify_phone");
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        return (String) cn.smssdk.net.c.a(MobSDK.getAppkey(), string);
    }

    public boolean isAgree() {
        return this.a.getBoolean("is_agree", false);
    }

    public void setAgree(boolean z) {
        this.a.putBoolean("is_agree", Boolean.valueOf(z));
    }

    public void setBufferedContactPhones(String[] strArr) {
        this.a.put("bufferedContactPhones", strArr);
    }

    public void setBufferedContacts(ArrayList<HashMap<String, Object>> arrayList) {
        this.a.put("bufferedContacts", arrayList);
    }

    public void setBufferedContactsSignature(String str) {
        this.a.putString("bufferedContactsSignature", str);
    }

    public void setBufferedCountrylist(String str) {
        this.a.putString("bufferedCountryList", str);
    }

    public void setBufferedFriends(ArrayList<HashMap<String, Object>> arrayList) {
        synchronized ("bufferedFriends") {
            this.a.put("bufferedFriends", arrayList);
        }
    }

    public void setBufferedNewFriends(ArrayList<HashMap<String, Object>> arrayList) {
        this.a.put("bufferedNewFriends", arrayList);
    }

    public void setConfig(String str) throws Throwable {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.a.putString("config", cn.smssdk.net.c.b(str));
    }

    public void setLastRequestTimeMillis(String str, long j) {
        this.a.putLong(str, Long.valueOf(j));
    }

    public void setLastZoneAt(long j) {
        this.a.putLong("lastZoneAt", Long.valueOf(j));
    }

    public void setLog(String str) {
        synchronized ("KEY_LOG") {
            String log = getLog();
            if (!TextUtils.isEmpty(log)) {
                str = log + "\r\n" + str;
            }
            this.b.putString("KEY_LOG", str);
        }
    }

    public void setRequestNewFriendsTime() {
        this.a.putLong("lastRequestNewFriendsTime", Long.valueOf(System.currentTimeMillis()));
    }

    public void setSMSID(String str) {
        this.b.putString("KEY_SMSID", str);
    }

    public void setToken(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.a.putString("token", str);
    }

    public void setTokenCacheAt(long j) {
        this.a.putLong("token_cache_at", Long.valueOf(j));
    }

    public void setVCodeHash(String str) {
        this.b.putString("KEY_VCODE_HASH", str);
    }

    public void setVerifyCountry(String str) throws Throwable {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.a.putString("verify_country", cn.smssdk.net.c.a(MobSDK.getAppkey(), (Object) str));
    }

    public void setVerifyPhone(String str) throws Throwable {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.a.putString("verify_phone", cn.smssdk.net.c.a(MobSDK.getAppkey(), (Object) str));
    }

    public void setWarnWhenReadContact(boolean z) {
        this.a.putBoolean("read_contact_warn", Boolean.valueOf(z));
    }
}
