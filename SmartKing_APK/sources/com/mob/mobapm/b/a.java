package com.mob.mobapm.b;

import android.text.TextUtils;
import com.liulishuo.filedownloader.model.ConnectionModel;
import com.mob.MobSDK;
import com.mob.mobapm.e.h;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.SharePrefrenceHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class a {
    private static SharePrefrenceHelper a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.mob.mobapm.b.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static class C0063a implements Comparator<String> {
        C0063a() {
        }

        @Override // java.util.Comparator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(String str, String str2) {
            return (int) (Long.valueOf(str).longValue() - Long.valueOf(str2).longValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class b implements Comparator<String> {
        b() {
        }

        @Override // java.util.Comparator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(String str, String str2) {
            return (int) (Long.valueOf(str).longValue() - Long.valueOf(str2).longValue());
        }
    }

    static {
        a();
    }

    private static final synchronized void a() {
        synchronized (a.class) {
            if (a == null) {
                SharePrefrenceHelper sharePrefrenceHelper = new SharePrefrenceHelper(MobSDK.getContext());
                a = sharePrefrenceHelper;
                sharePrefrenceHelper.open("apm", 1);
            }
        }
    }

    public static synchronized void a(String str) {
        synchronized (a.class) {
            a();
            a.putString("mm", h.a().f(str));
        }
    }

    public static synchronized void a(HashMap<String, Object> hashMap) {
        synchronized (a.class) {
            a();
            try {
                HashMap<String, Object> b2 = b();
                if (b2 == null) {
                    b2 = new HashMap<>();
                }
                String valueOf = hashMap.containsKey("happenTime") ? String.valueOf(hashMap.get("happenTime")) : null;
                if (b2.containsKey(valueOf)) {
                    b2.remove(valueOf);
                }
                a.putString("anrInfo", h.a().a(b2));
            } catch (Throwable th) {
                com.mob.mobapm.d.a.a().d("APM: remove anr info error: " + th, new Object[0]);
            }
        }
    }

    public static synchronized void a(List<HashMap<String, Object>> list) {
        synchronized (a.class) {
            a();
            a.putString("appRunningTime", h.a().a(list));
        }
    }

    public static synchronized HashMap<String, Object> b() {
        HashMap<String, Object> a2;
        synchronized (a.class) {
            a();
            String string = a.getString("anrInfo");
            a2 = TextUtils.isEmpty(string) ? null : h.a().a(string);
        }
        return a2;
    }

    public static synchronized void b(HashMap<String, Object> hashMap) {
        synchronized (a.class) {
            a();
            try {
                HashMap<String, Object> h = h();
                if (h == null) {
                    h = new HashMap<>();
                }
                String valueOf = hashMap.containsKey("happenTime") ? String.valueOf(hashMap.get("happenTime")) : null;
                if (h.containsKey(valueOf)) {
                    h.remove(valueOf);
                }
                a.putString("crashInfo", h.a().a(h));
            } catch (Throwable th) {
                com.mob.mobapm.d.a.a().d("APM: remove crash info error: " + th, new Object[0]);
            }
        }
    }

    public static synchronized void b(List<HashMap<String, Object>> list) {
        synchronized (a.class) {
            a();
            try {
                List<HashMap<String, Object>> f = f();
                ArrayList arrayList = new ArrayList();
                for (HashMap<String, Object> hashMap : list) {
                    String valueOf = String.valueOf(hashMap.get("appLaunchTime"));
                    String valueOf2 = String.valueOf(hashMap.get("appCloseTime"));
                    String valueOf3 = String.valueOf(hashMap.get("appDuration"));
                    Iterator<HashMap<String, Object>> it = f.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            HashMap<String, Object> next = it.next();
                            String valueOf4 = String.valueOf(next.get("appLaunchTime"));
                            String valueOf5 = String.valueOf(next.get("appCloseTime"));
                            String valueOf6 = String.valueOf(next.get("appDuration"));
                            if (valueOf.equals(valueOf4) && valueOf2.equals(valueOf5) && valueOf3.equals(valueOf6)) {
                                arrayList.add(next);
                                break;
                            }
                        }
                    }
                }
                f.removeAll(arrayList);
                a(f);
            } catch (Throwable th) {
                com.mob.mobapm.d.a.a().i("APM: update running time error: " + th, new Object[0]);
            }
        }
    }

    public static synchronized String c() {
        String string;
        synchronized (a.class) {
            a();
            string = a.getString("appANRVersion");
        }
        return string;
    }

    public static synchronized void c(HashMap<String, Object> hashMap) {
        synchronized (a.class) {
            a();
            try {
                HashMap<String, Object> j = j();
                if (j == null) {
                    j = new HashMap<>();
                }
                int intValue = ((Integer) hashMap.get(ConnectionModel.ID)).intValue();
                if (j.containsKey(String.valueOf(intValue))) {
                    j.remove(String.valueOf(intValue));
                }
                a.putString("radar", h.a().a((Object) j));
            } catch (Throwable th) {
                com.mob.mobapm.d.a.a().d("APM: remove radar data error: " + th, new Object[0]);
            }
        }
    }

    public static synchronized String d() {
        String string;
        synchronized (a.class) {
            a();
            string = a.getString("appCrashVersion");
        }
        return string;
    }

    public static synchronized void d(HashMap hashMap) {
        String c;
        String valueOf;
        synchronized (a.class) {
            if (hashMap != null) {
                if (!hashMap.isEmpty()) {
                    a();
                    try {
                        c = c();
                        valueOf = String.valueOf(DeviceHelper.getInstance(MobSDK.getContext()).getAppVersion());
                    } catch (Throwable th) {
                        com.mob.mobapm.d.a.a().d("APM: set anr info error: " + th, new Object[0]);
                    }
                    if (!TextUtils.isEmpty(c) && valueOf.equals(c)) {
                        HashMap<String, Object> b2 = b();
                        if (b2 == null || b2.size() < 5) {
                            if (b2 == null) {
                                b2 = new HashMap<>();
                            }
                            b2.putAll(hashMap);
                        } else {
                            ArrayList arrayList = new ArrayList(b2.keySet());
                            Collections.sort(arrayList, new b());
                            long longValue = Long.valueOf((String) new ArrayList(hashMap.keySet()).get(0)).longValue();
                            long longValue2 = Long.valueOf((String) arrayList.get(0)).longValue();
                            if (longValue > longValue2) {
                                b2.remove(String.valueOf(longValue2));
                                b2.putAll(hashMap);
                            }
                        }
                        a.putString("anrInfo", h.a().a(b2));
                    }
                    a.putString("anrInfo", h.a().a((HashMap<String, Object>) hashMap));
                    m();
                }
            }
        }
    }

    public static HashMap<String, Object> e() {
        a();
        String string = a.getString("appinfo");
        if (string != null) {
            return h.a().a(string);
        }
        return null;
    }

    public static void e(HashMap<String, Object> hashMap) {
        a();
        if (hashMap != null) {
            a.putString("appinfo", h.a().a(hashMap));
        }
    }

    public static synchronized List<HashMap<String, Object>> f() {
        List<HashMap<String, Object>> b2;
        synchronized (a.class) {
            a();
            String string = a.getString("appRunningTime");
            b2 = TextUtils.isEmpty(string) ? null : h.a().b(string);
        }
        return b2;
    }

    public static synchronized void f(HashMap<String, Object> hashMap) {
        synchronized (a.class) {
            a();
            try {
                j(hashMap);
                List f = f();
                if (f == null) {
                    f = new ArrayList();
                }
                f.add(hashMap);
                a.putString("appRunningTime", h.a().a(f));
            } catch (Throwable th) {
                com.mob.mobapm.d.a.a().i("APM: set running time error: " + th, new Object[0]);
            }
        }
    }

    public static synchronized HashMap<String, Object> g() {
        HashMap<String, Object> a2;
        synchronized (a.class) {
            a();
            String string = a.getString("appRunningTimeTemp");
            a2 = TextUtils.isEmpty(string) ? null : h.a().a(string);
        }
        return a2;
    }

    public static synchronized void g(HashMap<String, Object> hashMap) {
        synchronized (a.class) {
            a();
            j(hashMap);
            a.putString("appRunningTimeTemp", h.a().a(hashMap));
        }
    }

    public static synchronized HashMap<String, Object> h() {
        HashMap<String, Object> a2;
        synchronized (a.class) {
            a();
            String string = a.getString("crashInfo");
            a2 = TextUtils.isEmpty(string) ? null : h.a().a(string);
        }
        return a2;
    }

    public static synchronized void h(HashMap hashMap) {
        String d;
        String valueOf;
        synchronized (a.class) {
            if (hashMap != null) {
                if (!hashMap.isEmpty()) {
                    a();
                    try {
                        d = d();
                        valueOf = String.valueOf(DeviceHelper.getInstance(MobSDK.getContext()).getAppVersion());
                    } catch (Throwable th) {
                        com.mob.mobapm.d.a.a().d("APM: set crash info error: " + th, new Object[0]);
                    }
                    if (!TextUtils.isEmpty(d) && valueOf.equals(d)) {
                        HashMap<String, Object> h = h();
                        if (h == null || h.size() < 5) {
                            if (h == null) {
                                h = new HashMap<>();
                            }
                            h.putAll(hashMap);
                        } else {
                            ArrayList arrayList = new ArrayList(h.keySet());
                            Collections.sort(arrayList, new C0063a());
                            long longValue = Long.valueOf((String) new ArrayList(hashMap.keySet()).get(0)).longValue();
                            long longValue2 = Long.valueOf((String) arrayList.get(0)).longValue();
                            if (longValue > longValue2) {
                                h.remove(String.valueOf(longValue2));
                                h.putAll(hashMap);
                            }
                        }
                        a.putString("crashInfo", h.a().a(h));
                    }
                    a.putString("crashInfo", h.a().a((HashMap<String, Object>) hashMap));
                    n();
                }
            }
        }
    }

    public static synchronized String i() {
        String trim;
        synchronized (a.class) {
            a();
            String string = a.getString("mm");
            trim = TextUtils.isEmpty(string) ? null : h.a().d(string).trim();
        }
        return trim;
    }

    public static synchronized void i(HashMap<String, Object> hashMap) {
        synchronized (a.class) {
            a();
            try {
                HashMap<String, Object> j = j();
                if (j == null) {
                    j = new HashMap<>();
                }
                for (String str : hashMap.keySet()) {
                    if (!j.containsKey(str)) {
                        j.put(str, hashMap.get(str));
                    }
                }
                a.putString("radar", h.a().a((Object) j));
            } catch (Throwable th) {
                com.mob.mobapm.d.a.a().d("APM: set radar data error: " + th, new Object[0]);
            }
        }
    }

    public static synchronized HashMap<String, Object> j() {
        HashMap<String, Object> a2;
        synchronized (a.class) {
            a();
            String string = a.getString("radar");
            a2 = TextUtils.isEmpty(string) ? null : h.a().a(string);
        }
        return a2;
    }

    private static void j(HashMap<String, Object> hashMap) {
        try {
            hashMap.put("networkType", DeviceHelper.getInstance(MobSDK.getContext()).getNetworkType());
            hashMap.put("dataNetworkType", Integer.valueOf(DeviceHelper.getInstance(MobSDK.getContext()).getDataNtType()));
            hashMap.put("mac", DeviceHelper.getInstance(MobSDK.getContext()).getMacAddress());
            hashMap.put("imei", DeviceHelper.getInstance(MobSDK.getContext()).getIMEI());
            hashMap.put("duid", com.mob.mobapm.core.d.e());
        } catch (Throwable th) {
            com.mob.mobapm.d.a.a().d("APM: update device info error: " + th, new Object[0]);
        }
    }

    public static synchronized void k() {
        synchronized (a.class) {
            a();
            a.putString("anrInfo", null);
            m();
        }
    }

    public static synchronized void l() {
        synchronized (a.class) {
            a();
            a.putString("crashInfo", null);
            n();
        }
    }

    public static synchronized void m() {
        synchronized (a.class) {
            a();
            a.putString("appANRVersion", String.valueOf(DeviceHelper.getInstance(MobSDK.getContext()).getAppVersion()));
        }
    }

    public static synchronized void n() {
        synchronized (a.class) {
            a();
            a.putString("appCrashVersion", String.valueOf(DeviceHelper.getInstance(MobSDK.getContext()).getAppVersion()));
        }
    }
}
