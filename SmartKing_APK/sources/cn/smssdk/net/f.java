package cn.smssdk.net;

import android.text.TextUtils;
import cn.smssdk.utils.SMSLog;
import cn.smssdk.utils.SPHelper;
import com.mob.tools.utils.Hashon;
import java.util.ArrayList;
import java.util.HashMap;

/* compiled from: Protocols.java */
/* loaded from: classes.dex */
public class f {
    private static f d;
    private Hashon a = new Hashon();
    private SPHelper b = SPHelper.getInstance();
    private b c = b.g();

    private f() {
        e.b();
    }

    public static f d() {
        if (d == null) {
            d = new f();
        }
        return d;
    }

    public ArrayList<HashMap<String, Object>> a() throws Throwable {
        long lastZoneAt = this.b.getLastZoneAt();
        String bufferedCountrylist = this.b.getBufferedCountrylist();
        if (lastZoneAt == this.c.b() && !TextUtils.isEmpty(bufferedCountrylist) && !c()) {
            SMSLog.getInstance().d(SMSLog.FORMAT, "Protocols", "getSupportedCountries", "Use country list buffered in SP.");
            ArrayList<HashMap<String, Object>> arrayList = (ArrayList) this.a.fromJson(bufferedCountrylist).get("list");
            if (arrayList != null && !arrayList.isEmpty()) {
                return arrayList;
            }
            SMSLog.getInstance().d(SMSLog.FORMAT, "Protocols", "getSupportedCountries", "Country list buffered in SP dirty!");
        }
        SMSLog.getInstance().d(SMSLog.FORMAT, "Protocols", "getSupportedCountries", "Observe country list from server.");
        HashMap<String, Object> a = this.c.a(2, (HashMap<String, Object>) null);
        this.b.setBufferedCountrylist(this.a.fromHashMap(a));
        this.b.setLastZoneAt(this.c.b());
        this.c.c();
        return (ArrayList) a.get("list");
    }

    public void a(String str, String str2, String str3) throws Throwable {
        if (TextUtils.isEmpty(str2)) {
            throw new Throwable("{\"detail\":\"country code cant be empty\"}");
        }
        if (TextUtils.isEmpty(str)) {
            throw new Throwable("{\"detail\":\"phone number cant be empty\"}");
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        HashMap hashMap2 = new HashMap();
        if (!TextUtils.isEmpty(str3)) {
            hashMap2.put("extKey", str3);
        }
        hashMap.put("phone", str);
        hashMap.put("zone", str2);
        hashMap.put("attr", hashMap2);
        hashMap.put("tempCode", "Nul2");
        this.c.a(10, hashMap);
    }

    public boolean a(String str, String str2, String str3, String str4) throws Throwable {
        if (TextUtils.isEmpty(str)) {
            throw new Throwable("{\"detail\":\"country code cant be empty\"}");
        }
        if (TextUtils.isEmpty(str2)) {
            throw new Throwable("{\"detail\":\"phone number cant be empty\"}");
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("phone", str2);
        hashMap.put("zone", str);
        if (!TextUtils.isEmpty(str3)) {
            HashMap hashMap2 = new HashMap();
            hashMap.put("attr", hashMap2);
            hashMap2.put("extKey", str3);
        }
        if (!TextUtils.isEmpty(str4)) {
            hashMap.put("tempCode", str4);
        }
        HashMap<String, Object> a = this.c.a(9, hashMap);
        String str5 = (String) a.get("vCode");
        String str6 = (String) a.get("smsId");
        Integer num = (Integer) a.get("smart");
        this.b.setSMSID(str6);
        this.b.setVCodeHash(str5);
        if (num == null || num.intValue() != 1) {
            return false;
        }
        this.b.clearBuffer();
        try {
            this.b.setVerifyCountry(str);
            this.b.setVerifyPhone(str2);
        } catch (Throwable th) {
            SMSLog.getInstance().w(th);
        }
        return true;
    }

    public boolean a(HashMap<String, Object> hashMap) {
        boolean z;
        try {
            this.c.a(13, hashMap);
            z = true;
        } catch (Throwable th) {
            SMSLog.getInstance().d(th, SMSLog.FORMAT_SIMPLE, "Upload SDK Log Failed");
            z = false;
        }
        SMSLog.getInstance().d(SMSLog.FORMAT, "Protocols", "uploadSdkLog", "Upload SDK Log: " + z);
        return z;
    }

    public HashMap<String, Object> b(String str, String str2, String str3) throws Throwable {
        if (TextUtils.isEmpty(str)) {
            throw new Throwable("{\"status\":\"466\"}");
        }
        if (TextUtils.isEmpty(str2)) {
            throw new Throwable("{\"detail\":\"country code cant be empty\"}");
        }
        if (TextUtils.isEmpty(str3)) {
            throw new Throwable("{\"detail\":\"phone number cant be empty\"}");
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("phone", str3);
        hashMap.put("code", str);
        hashMap.put("zone", str2);
        this.c.a(11, hashMap);
        HashMap<String, Object> hashMap2 = new HashMap<>();
        hashMap2.put("country", str2);
        hashMap2.put("phone", str3);
        this.b.clearBuffer();
        try {
            this.b.setVerifyCountry(str2);
            this.b.setVerifyPhone(str3);
        } catch (Throwable th) {
            SMSLog.getInstance().w(th);
        }
        return hashMap2;
    }

    public void b() {
        try {
            this.c.a(true);
        } catch (Throwable th) {
            SMSLog.getInstance().d(th, SMSLog.FORMAT_SIMPLE, "Init token error");
        }
    }

    public boolean c() {
        return this.c.a();
    }
}
