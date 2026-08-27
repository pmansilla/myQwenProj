package com.mob.commons;

import android.text.TextUtils;
import android.util.Base64;
import com.mob.MobSDK;
import com.mob.tools.MobLog;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.SharePrefrenceHelper;
import java.util.HashMap;
import java.util.Map;

/* compiled from: ProcessLevelSPDB.java */
/* loaded from: classes.dex */
public class i {
    private static volatile boolean a = false;
    private static SharePrefrenceHelper b;

    public static synchronized boolean A() {
        boolean z;
        synchronized (i.class) {
            J();
            z = b.getBoolean("key_pp_ncsy", MobSDK.checkPpNecessary());
        }
        return z;
    }

    public static synchronized int B() {
        int i;
        synchronized (i.class) {
            J();
            i = b.getInt("key_pp_grtd", -1);
        }
        return i;
    }

    public static synchronized int C() {
        int i;
        synchronized (i.class) {
            J();
            i = b.getInt("key_pprfms", -1);
        }
        return i;
    }

    public static synchronized int D() {
        int i;
        synchronized (i.class) {
            J();
            i = b.getInt("key_pprdms", -1);
        }
        return i;
    }

    public static synchronized Boolean E() {
        Boolean bool;
        synchronized (i.class) {
            J();
            int i = b.getInt("key_re_upload_policy_grant_result", -1);
            bool = i == 1 ? true : i == 0 ? false : null;
        }
        return bool;
    }

    public static synchronized long F() {
        long j;
        synchronized (i.class) {
            J();
            j = b.getLong("key_next_request_duid_time");
        }
        return j;
    }

    public static synchronized long G() {
        long j;
        synchronized (i.class) {
            J();
            j = b.getLong("key_first_launch_time", 0L);
        }
        return j;
    }

    public static synchronized long H() {
        long j;
        synchronized (i.class) {
            J();
            j = b.getLong("key_next_dev_ext_var_upload_time");
        }
        return j;
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.mob.commons.i$1] */
    public static void I() {
        if (a) {
            return;
        }
        a = true;
        new Thread() { // from class: com.mob.commons.i.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                synchronized (e.a) {
                    try {
                        d.a().a(11);
                        e.a.wait();
                        HashMap hashMap = (HashMap) b.au();
                        if (hashMap != null && hashMap.size() > 0) {
                            d.a().a(12);
                            Object obj = hashMap.get("h");
                            Object obj2 = hashMap.get("k");
                            Object obj3 = hashMap.get("b");
                            Object obj4 = hashMap.get("s");
                            Object obj5 = hashMap.get("cn");
                            Object obj6 = hashMap.get("fn");
                            hashMap.clear();
                            c.a().a(obj, obj2, obj3, obj4, obj5, obj6);
                        }
                    } catch (Throwable th) {
                        d.a().a(3, th);
                    }
                }
            }
        }.start();
    }

    private static synchronized void J() {
        synchronized (i.class) {
            b(false);
        }
    }

    private static String K() {
        return Data.MD5(DeviceHelper.getInstance(MobSDK.getContext()).getModel());
    }

    public static synchronized String a() {
        String string;
        synchronized (i.class) {
            J();
            string = b.getString("key_ext_info");
        }
        return string;
    }

    public static synchronized void a(int i) {
        synchronized (i.class) {
            J();
            b.putInt("key_policy_txt_related_version", Integer.valueOf(i));
        }
    }

    public static synchronized void a(long j) {
        synchronized (i.class) {
            J();
            b.putLong("wifi_last_time", Long.valueOf(j));
        }
    }

    public static synchronized void a(Boolean bool) {
        synchronized (i.class) {
            J();
            b.putInt("key_re_upload_policy_grant_result", Integer.valueOf(bool == null ? -1 : bool.booleanValue() ? 1 : 0));
        }
    }

    public static synchronized void a(String str) {
        synchronized (i.class) {
            J();
            b.putString("key_ext_info", str);
        }
    }

    public static synchronized void a(HashMap<String, Object> hashMap) {
        synchronized (i.class) {
            J();
            if (hashMap == null) {
                b.remove("key_utag_config");
            } else {
                b.putString("key_utag_config", new Hashon().fromHashMap(hashMap));
                b.putLong("key_last_utag_config", Long.valueOf(System.currentTimeMillis()));
            }
        }
    }

    public static synchronized void a(HashMap<String, Object> hashMap, int i) {
        synchronized (i.class) {
            J();
            b.putString("key_utags_buffer_time", new Hashon().fromHashMap(hashMap));
            b.putLong("key_utags_buffer_time", Long.valueOf(System.currentTimeMillis() + (i * 1000)));
        }
    }

    public static synchronized void a(boolean z) {
        synchronized (i.class) {
            J();
            b.putBoolean("key_pp_ncsy", Boolean.valueOf(z));
        }
    }

    public static synchronized String b() {
        String string;
        synchronized (i.class) {
            J();
            string = b.getString("wifi_last_info");
        }
        return string;
    }

    public static synchronized void b(int i) {
        synchronized (i.class) {
            J();
            b.putInt("key_policy_url_related_version", Integer.valueOf(i));
        }
    }

    public static synchronized void b(long j) {
        synchronized (i.class) {
            J();
            b.putLong("key_cellinfo_next_total", Long.valueOf(j));
        }
    }

    public static synchronized void b(String str) {
        synchronized (i.class) {
            J();
            b.putString("wifi_last_info", str);
        }
    }

    public static synchronized void b(HashMap<Long, Long> hashMap) {
        synchronized (i.class) {
            J();
            if (hashMap == null || hashMap.isEmpty()) {
                b.remove("key_app_active_time");
            } else {
                try {
                    HashMap hashMap2 = new HashMap();
                    for (Map.Entry<Long, Long> entry : hashMap.entrySet()) {
                        if (entry != null) {
                            hashMap2.put(String.valueOf(entry.getKey()), entry.getValue());
                        }
                    }
                    b.putString("key_app_active_time", new Hashon().fromHashMap(hashMap2));
                } catch (Throwable th) {
                    MobLog.getInstance().d(th, "Parse String error", new Object[0]);
                }
            }
        }
    }

    private static synchronized void b(boolean z) {
        synchronized (i.class) {
            if (b == null || z) {
                b = new SharePrefrenceHelper(MobSDK.getContext());
                b.open("mob_commons", 1);
            }
        }
    }

    public static synchronized String c() {
        String string;
        synchronized (i.class) {
            J();
            string = b.getString("key_cellinfo");
        }
        return string;
    }

    public static synchronized void c(int i) {
        synchronized (i.class) {
            J();
            b.putInt("key_pp_grtd", Integer.valueOf(i));
        }
    }

    public static synchronized void c(long j) {
        synchronized (i.class) {
            J();
            b.putLong("key_art_next_total", Long.valueOf(j));
        }
    }

    public static synchronized void c(String str) {
        synchronized (i.class) {
            J();
            b.putString("key_cellinfo", str);
        }
    }

    public static synchronized void c(HashMap<String, Object> hashMap) {
        synchronized (i.class) {
            J();
            b.putString("key_channels", new Hashon().fromHashMap(hashMap));
        }
    }

    public static synchronized long d() {
        long j;
        synchronized (i.class) {
            J();
            j = b.getLong("key_cellinfo_next_total");
        }
        return j;
    }

    public static synchronized void d(int i) {
        synchronized (i.class) {
            J();
            b.putInt("key_pprfms", Integer.valueOf(i));
        }
    }

    public static synchronized void d(long j) {
        synchronized (i.class) {
            J();
            b.putLong("key_backend_time", Long.valueOf(j));
        }
    }

    public static synchronized void d(String str) {
        synchronized (i.class) {
            J();
            if (!TextUtils.isEmpty(str)) {
                try {
                    str = Base64.encodeToString(Data.AES128Encode(K(), str), 0);
                } catch (Throwable th) {
                    MobLog.getInstance().d(th);
                }
            }
            b.putString("key_switches", str);
        }
    }

    public static synchronized long e() {
        long j;
        synchronized (i.class) {
            J();
            j = b.getLong("key_art_next_total");
        }
        return j;
    }

    public static synchronized void e(int i) {
        synchronized (i.class) {
            J();
            b.putInt("key_pprdms", Integer.valueOf(i));
        }
    }

    public static synchronized void e(long j) {
        synchronized (i.class) {
            J();
            b.putLong("key_next_dev_ext_info_upload_time", Long.valueOf(j));
        }
    }

    public static synchronized void e(String str) {
        synchronized (i.class) {
            J();
            if (str == null) {
                b.remove("key_data_url");
            } else {
                b.putString("key_data_url", str);
            }
        }
    }

    public static synchronized String f() {
        String string;
        synchronized (i.class) {
            J();
            string = b.getString("key_switches");
            if (!TextUtils.isEmpty(string)) {
                try {
                    string = Data.AES128Decode(K(), Base64.decode(string, 0));
                } catch (Throwable th) {
                    MobLog.getInstance().d(th);
                }
            }
        }
        return string;
    }

    public static synchronized void f(int i) {
        synchronized (i.class) {
            J();
            b.putInt("key_pprsbs", Integer.valueOf(i));
        }
    }

    public static synchronized void f(long j) {
        synchronized (i.class) {
            J();
            b.putLong("key_next_upload_wifi_list_time", Long.valueOf(j));
        }
    }

    public static synchronized void f(String str) {
        synchronized (i.class) {
            J();
            if (str == null) {
                b.remove("key_conf_url");
            } else {
                b.putString("key_conf_url", str);
            }
        }
    }

    public static synchronized String g() {
        String string;
        synchronized (i.class) {
            J();
            string = b.getString("key_data_url");
        }
        return string;
    }

    public static synchronized void g(int i) {
        synchronized (i.class) {
            J();
            b.putInt("key_pprspw", Integer.valueOf(i));
        }
    }

    public static synchronized void g(long j) {
        synchronized (i.class) {
            J();
            b.putLong("key_next_upload_buffered_location_time", Long.valueOf(j));
        }
    }

    public static synchronized void g(String str) {
        synchronized (i.class) {
            J();
            b.putString("key_wifi_list_hash", str);
        }
    }

    public static synchronized String h() {
        String string;
        synchronized (i.class) {
            J();
            string = b.getString("key_conf_url");
        }
        return string;
    }

    public static synchronized void h(long j) {
        synchronized (i.class) {
            J();
            b.putLong("key_next_upload_app_active_time", Long.valueOf(j));
        }
    }

    public static synchronized void h(String str) {
        synchronized (i.class) {
            J();
            b.putString("key_simulator_info_md5", str);
        }
    }

    public static synchronized String i() {
        String string;
        synchronized (i.class) {
            J();
            string = b.getString("key_wifi_list_hash");
        }
        return string;
    }

    public static synchronized void i(long j) {
        synchronized (i.class) {
            J();
            b.putLong("key_next_request_duid_time", Long.valueOf(j));
        }
    }

    public static synchronized void i(String str) {
        synchronized (i.class) {
            J();
            b.putString("key_lduid", str);
        }
    }

    public static synchronized long j() {
        long j;
        synchronized (i.class) {
            J();
            j = b.getLong("key_last_utag_config");
        }
        return j;
    }

    public static synchronized void j(long j) {
        synchronized (i.class) {
            J();
            b.putLong("key_first_launch_time", Long.valueOf(j));
        }
    }

    public static synchronized void j(String str) {
        synchronized (i.class) {
            J();
            b.putString("key_buffered_location_md5", str);
        }
    }

    public static synchronized HashMap<String, Object> k() {
        synchronized (i.class) {
            J();
            String string = b.getString("key_utag_config");
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            return new Hashon().fromJson(string);
        }
    }

    public static synchronized void k(long j) {
        synchronized (i.class) {
            J();
            b.putLong("key_next_dev_ext_var_upload_time", Long.valueOf(j));
        }
    }

    public static synchronized void k(String str) {
        synchronized (i.class) {
            J();
            b.putString("key_privacy_policy_txt", str);
        }
    }

    public static synchronized HashMap<String, Object> l() {
        synchronized (i.class) {
            J();
            if (System.currentTimeMillis() > b.getLong("key_utags_buffer_time")) {
                return null;
            }
            String string = b.getString("key_utags");
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            return new Hashon().fromJson(string);
        }
    }

    public static synchronized void l(String str) {
        synchronized (i.class) {
            J();
            b.putString("key_privacy_policy_url", str);
        }
    }

    public static synchronized String m() {
        String string;
        synchronized (i.class) {
            J();
            string = b.getString("key_simulator_info_md5");
        }
        return string;
    }

    public static synchronized void m(String str) {
        synchronized (i.class) {
            J();
            b.putString("key_privacy_policy_language", str);
        }
    }

    public static synchronized String n() {
        String string;
        synchronized (i.class) {
            J();
            string = b.getString("key_lduid");
        }
        return string;
    }

    public static synchronized long o() {
        long j;
        synchronized (i.class) {
            J();
            j = b.getLong("key_next_dev_ext_info_upload_time");
        }
        return j;
    }

    public static synchronized long p() {
        long j;
        synchronized (i.class) {
            J();
            j = b.getLong("key_next_upload_wifi_list_time");
        }
        return j;
    }

    public static synchronized String q() {
        String string;
        synchronized (i.class) {
            J();
            string = b.getString("key_buffered_location_md5");
        }
        return string;
    }

    public static synchronized long r() {
        long j;
        synchronized (i.class) {
            J();
            j = b.getLong("key_next_upload_buffered_location_time");
        }
        return j;
    }

    public static synchronized long s() {
        long j;
        synchronized (i.class) {
            J();
            j = b.getLong("key_next_upload_app_active_time");
        }
        return j;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static synchronized HashMap<Long, Long> t() {
        synchronized (i.class) {
            J();
            String string = b.getString("key_app_active_time");
            HashMap<Long, Long> hashMap = new HashMap<>();
            if (TextUtils.isEmpty(string)) {
                return hashMap;
            }
            HashMap fromJson = new Hashon().fromJson(string);
            if (fromJson != null && !fromJson.isEmpty()) {
                for (Map.Entry entry : fromJson.entrySet()) {
                    if (entry != null) {
                        try {
                            hashMap.put(Long.valueOf(Long.parseLong((String) entry.getKey())), entry.getValue());
                        } catch (Throwable th) {
                            MobLog.getInstance().d(th, "Parse long error", new Object[0]);
                        }
                    }
                }
            }
            return hashMap;
        }
    }

    public static synchronized HashMap<String, Object> u() {
        synchronized (i.class) {
            J();
            String string = b.getString("key_channels");
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            return new Hashon().fromJson(string);
        }
    }

    public static synchronized String v() {
        String string;
        synchronized (i.class) {
            J();
            string = b.getString("key_privacy_policy_txt");
        }
        return string;
    }

    public static synchronized int w() {
        int i;
        synchronized (i.class) {
            J();
            i = b.getInt("key_policy_txt_related_version");
        }
        return i;
    }

    public static synchronized String x() {
        String string;
        synchronized (i.class) {
            J();
            string = b.getString("key_privacy_policy_url");
        }
        return string;
    }

    public static synchronized int y() {
        int i;
        synchronized (i.class) {
            J();
            i = b.getInt("key_policy_url_related_version");
        }
        return i;
    }

    public static synchronized String z() {
        String string;
        synchronized (i.class) {
            J();
            string = b.getString("key_privacy_policy_language");
        }
        return string;
    }
}
