package com.mob.commons;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.autonavi.amap.mapcore.AeUtil;
import com.mob.MobSDK;
import com.mob.OperationCallback;
import com.mob.commons.a.m;
import com.mob.commons.a.n;
import com.mob.commons.a.o;
import com.mob.commons.a.p;
import com.mob.commons.a.q;
import com.mob.commons.a.r;
import com.mob.commons.a.s;
import com.mob.commons.a.t;
import com.mob.commons.authorize.DeviceAuthorizer;
import com.mob.commons.b;
import com.mob.tools.MobLog;
import com.mob.tools.log.NLog;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.UIHandler;
import java.util.ArrayList;
import java.util.HashMap;

/* compiled from: CltsInitializer.java */
/* loaded from: classes.dex */
public final class a {
    private static Boolean h;
    private static Boolean i;
    private static int j;
    private static Boolean k;
    private static Boolean l;
    private static int o;
    private static Boolean p;
    private static Boolean q;
    private static Boolean r;
    private static Boolean s;
    private static final String b = j.a();
    private static final String c = j.b();
    private static final String d = b + "/privacy/policy/ms/version";
    private static final String e = b + "/privacy/policy/rejection/strategy";
    private static final String f = c + "/privacy/policy/authorization/status";
    private static final String g = c + "/privacy/policy/permission/window/status";
    private static int m = -1;
    private static int n = -1;
    private static byte[] t = new byte[0];
    private static byte[] u = new byte[0];
    private static volatile int v = -1;
    public static volatile boolean a = true;

    public static final void a() {
        new Thread(new Runnable() { // from class: com.mob.commons.a.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    a.w();
                    boolean c2 = a.c();
                    MobLog.getInstance().d("====> ppNece: " + c2, new Object[0]);
                    if (!c2) {
                        MobLog.getInstance().d("====> Entr 1", new Object[0]);
                        Boolean unused = a.i = null;
                        Boolean unused2 = a.h = null;
                        int unused3 = a.j = a.n();
                        a.v();
                        return;
                    }
                    boolean d2 = a.d();
                    MobLog.getInstance().d("====> isAgrPp: " + d2, new Object[0]);
                    if (d2) {
                        MobLog.getInstance().d("====> Entr 2", new Object[0]);
                        Boolean unused4 = a.h = true;
                        Boolean unused5 = a.i = null;
                        int unused6 = a.j = a.n();
                        a.v();
                        return;
                    }
                    boolean j2 = a.j();
                    MobLog.getInstance().d("====> cltStch: " + j2, new Object[0]);
                    if (j2) {
                        MobLog.getInstance().d("====> Entr 3", new Object[0]);
                        Boolean unused7 = a.h = a.k();
                        Boolean unused8 = a.i = null;
                        int unused9 = a.j = a.n();
                        a.v();
                    } else {
                        MobLog.getInstance().d("====> Entr cltSwth=false", new Object[0]);
                        b.ar();
                        b.a((HashMap<String, Object>) null);
                        a.a(1);
                    }
                    a.u();
                } catch (Throwable th) {
                    MobLog.getInstance().e(th, "Clt init error", new Object[0]);
                }
            }
        }).start();
        new Thread(new Runnable() { // from class: com.mob.commons.a.2
            @Override // java.lang.Runnable
            public void run() {
                Boolean E = i.E();
                if (E != null) {
                    a.c(E.booleanValue(), null);
                }
            }
        }).start();
    }

    public static void a(int i2) {
        MobLog.getInstance().d("Notify initLock. initialized: " + i2, new Object[0]);
        synchronized (u) {
            try {
                v = i2;
                u.notifyAll();
            } catch (Throwable th) {
                MobLog.getInstance().d(th, "Init lock error", new Object[0]);
            }
        }
    }

    public static void a(boolean z, OperationCallback<Void> operationCallback) {
        c(z, operationCallback);
        MobLog.getInstance().d("submitPpResult().", new Object[0]);
        MobLog.getInstance().d("grtd: " + z, new Object[0]);
        if (!z) {
            l = false;
            i.c(0);
            return;
        }
        boolean d2 = d();
        MobLog.getInstance().d("bfdIsAgrPp: " + d2, new Object[0]);
        if (d2) {
            return;
        }
        l = true;
        i.c(1);
        MobLog.getInstance().d("====> Entr 5", new Object[0]);
        h = Boolean.valueOf(d());
        i = null;
        j = y();
        b.a(new b.a() { // from class: com.mob.commons.a.4
            @Override // com.mob.commons.b.a
            public void a() {
                a.v();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String b(long j2, String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            return new String(Data.AES128Decode(Data.rawMD5(MobSDK.getAppkey() + ":" + DeviceHelper.getInstance(MobSDK.getContext()).getPackageName() + ":" + j2), Base64.decode(str, 0)), "UTF-8");
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void b(int i2, boolean z) throws Throwable {
        int i3 = i2 + 1;
        try {
            DeviceHelper deviceHelper = DeviceHelper.getInstance(MobSDK.getContext());
            ArrayList<KVPair<String>> arrayList = new ArrayList<>();
            arrayList.add(new KVPair<>("appkey", MobSDK.getAppkey()));
            arrayList.add(new KVPair<>("apppkg", deviceHelper.getPackageName()));
            arrayList.add(new KVPair<>("appver", deviceHelper.getAppVersionName()));
            arrayList.add(new KVPair<>("plat", String.valueOf(deviceHelper.getPlatformCode())));
            arrayList.add(new KVPair<>("networktype", deviceHelper.getDetailNetworkTypeForStatic()));
            String authorizeForOnce = DeviceAuthorizer.authorizeForOnce();
            if (!TextUtils.isEmpty(authorizeForOnce)) {
                arrayList.add(new KVPair<>("duid", authorizeForOnce));
            }
            arrayList.add(new KVPair<>("isAgreePp", String.valueOf(z)));
            NetworkHelper.NetworkTimeOut networkTimeOut = new NetworkHelper.NetworkTimeOut();
            networkTimeOut.readTimout = 10000;
            networkTimeOut.connectionTimeout = 5000;
            ArrayList<KVPair<String>> arrayList2 = new ArrayList<>();
            arrayList2.add(new KVPair<>("User-Identity", MobProductCollector.getUserIdentity()));
            MobLog.getInstance().d("Request: " + f + "\nHeaders: " + arrayList2 + "\nValues: " + arrayList, new Object[0]);
            String httpGet = new NetworkHelper().httpGet(f, arrayList, arrayList2, networkTimeOut);
            NLog mobLog = MobLog.getInstance();
            StringBuilder sb = new StringBuilder();
            sb.append("Response: ");
            sb.append(httpGet);
            mobLog.d(sb.toString(), new Object[0]);
            HashMap fromJson = new Hashon().fromJson(httpGet);
            if (fromJson == null) {
                if (i3 >= 2) {
                    throw new Throwable("Response is illegal: " + httpGet);
                }
                b(i3, z);
            }
            if ("200".equals(String.valueOf(fromJson.get("code")))) {
                return;
            }
            if (i3 < 2) {
                b(i3, z);
                return;
            }
            throw new Throwable("Response code is not 200: " + httpGet);
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            if (i3 >= 2) {
                throw th;
            }
            b(i3, z);
        }
    }

    public static boolean b() {
        if (v != -1) {
            return v == 1;
        }
        synchronized (u) {
            if (v == -1) {
                try {
                    MobLog.getInstance().d("Wait initLock", new Object[0]);
                    u.wait();
                } catch (Throwable th) {
                    MobLog.getInstance().d(th, "Init lock error", new Object[0]);
                }
            }
        }
        return v == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void c(final boolean z, final OperationCallback<Void> operationCallback) {
        new Thread(new Runnable() { // from class: com.mob.commons.a.7
            @Override // java.lang.Runnable
            public void run() {
                try {
                    a.b(0, z);
                    i.a((Boolean) null);
                    if (operationCallback != null) {
                        UIHandler.sendEmptyMessage(0, new Handler.Callback() { // from class: com.mob.commons.a.7.1
                            @Override // android.os.Handler.Callback
                            public boolean handleMessage(Message message) {
                                operationCallback.onComplete(null);
                                return false;
                            }
                        });
                    }
                } catch (Throwable th) {
                    MobLog.getInstance().e(th, "Submit privacy grant result error", new Object[0]);
                    i.a(Boolean.valueOf(z));
                    if (operationCallback != null) {
                        UIHandler.sendEmptyMessage(0, new Handler.Callback() { // from class: com.mob.commons.a.7.2
                            @Override // android.os.Handler.Callback
                            public boolean handleMessage(Message message) {
                                operationCallback.onFailure(th);
                                return false;
                            }
                        });
                    }
                }
            }
        }).start();
    }

    public static boolean c() {
        if (k == null) {
            k = Boolean.valueOf(i.A());
        }
        return k.booleanValue();
    }

    public static boolean d() {
        Boolean k2 = k();
        if (k2 == null) {
            k2 = false;
        }
        return k2.booleanValue();
    }

    public static Boolean e() {
        return h;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void e(int i2) {
        if (m == -1) {
            m = i.w();
        }
        if (n == -1) {
            n = i.y();
        }
        try {
            if (m != i2) {
                m = new h().b(2, null).getPpVersion();
            }
            if (n != i2) {
                n = new h().b(1, null).getPpVersion();
            }
        } catch (Throwable unused) {
            MobLog.getInstance().d("Update privacy policy err.", new Object[0]);
        }
    }

    public static Boolean f() {
        return i;
    }

    public static int g() {
        return j;
    }

    public static int h() {
        return o;
    }

    public static synchronized boolean i() {
        boolean booleanValue;
        synchronized (a.class) {
            if (p == null) {
                int C = i.C();
                if (C == 1) {
                    p = true;
                } else if (C == 0) {
                    p = false;
                } else {
                    p = true;
                }
            }
            booleanValue = p.booleanValue();
        }
        return booleanValue;
    }

    public static boolean j() {
        if (q == null) {
            synchronized (t) {
                if (q == null) {
                    int D = i.D();
                    if (D == -1) {
                        x();
                    } else {
                        if (D == 1) {
                            q = true;
                        } else if (D == 0) {
                            q = false;
                        } else {
                            q = false;
                        }
                        new Thread(new Runnable() { // from class: com.mob.commons.a.3
                            @Override // java.lang.Runnable
                            public void run() {
                                a.x();
                            }
                        }).start();
                    }
                }
            }
        }
        return q.booleanValue();
    }

    public static Boolean k() {
        int B;
        if (l == null && (B = i.B()) != -1) {
            l = Boolean.valueOf(B == 1);
        }
        return l;
    }

    public static void l() {
        m.a().a(new FBListener() { // from class: com.mob.commons.a.5
            @Override // com.mob.commons.FBListener
            public void onFBChanged(boolean z, boolean z2, long j2) {
                if (z) {
                    MobLog.getInstance().d("App goes fg.", new Object[0]);
                    a.a = true;
                } else {
                    MobLog.getInstance().d("App goes bg.", new Object[0]);
                    a.a = false;
                }
            }
        });
    }

    static /* synthetic */ int n() {
        return y();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void u() {
        if (b.ae()) {
            Log.w("MobSDK/Policy", "您好！依照国家对网络安全及数据安全的要求，请您运营的APP集成并向终端用户展示Mob SDK的隐私政策。");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void v() {
        com.mob.commons.a.d.a((Class<? extends com.mob.commons.a.d>[]) new Class[]{com.mob.commons.a.a.class, com.mob.commons.a.k.class, p.class, r.class, com.mob.commons.a.b.class, t.class, com.mob.commons.a.h.class, s.class, q.class, n.class, com.mob.commons.a.j.class, com.mob.commons.a.i.class, com.mob.commons.a.e.class, com.mob.commons.a.c.class, com.mob.commons.a.f.class, com.mob.commons.a.g.class, com.mob.commons.a.l.class, o.class});
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void w() {
        new Thread(new Runnable() { // from class: com.mob.commons.a.6
            @Override // java.lang.Runnable
            public void run() {
                try {
                    DeviceHelper deviceHelper = DeviceHelper.getInstance(MobSDK.getContext());
                    ArrayList<KVPair<String>> arrayList = new ArrayList<>();
                    arrayList.add(new KVPair<>("appkey", MobSDK.getAppkey()));
                    arrayList.add(new KVPair<>("apppkg", deviceHelper.getPackageName()));
                    arrayList.add(new KVPair<>("appver", deviceHelper.getAppVersionName()));
                    arrayList.add(new KVPair<>("plat", String.valueOf(deviceHelper.getPlatformCode())));
                    arrayList.add(new KVPair<>("networktype", deviceHelper.getDetailNetworkTypeForStatic()));
                    String authorizeForOnce = DeviceAuthorizer.authorizeForOnce();
                    if (!TextUtils.isEmpty(authorizeForOnce)) {
                        arrayList.add(new KVPair<>("duid", authorizeForOnce));
                    }
                    NetworkHelper.NetworkTimeOut networkTimeOut = new NetworkHelper.NetworkTimeOut();
                    networkTimeOut.readTimout = 10000;
                    networkTimeOut.connectionTimeout = 5000;
                    ArrayList<KVPair<String>> arrayList2 = new ArrayList<>();
                    arrayList2.add(new KVPair<>("User-Identity", MobProductCollector.getUserIdentity()));
                    MobLog.getInstance().d("Request: " + a.d + "\nHeaders: " + arrayList2 + "\nValues: " + arrayList, new Object[0]);
                    String httpGet = new NetworkHelper().httpGet(a.d, arrayList, arrayList2, networkTimeOut);
                    NLog mobLog = MobLog.getInstance();
                    StringBuilder sb = new StringBuilder();
                    sb.append("Response: ");
                    sb.append(httpGet);
                    mobLog.d(sb.toString(), new Object[0]);
                    Hashon hashon = new Hashon();
                    HashMap fromJson = hashon.fromJson(httpGet);
                    if (fromJson == null) {
                        throw new Throwable("Response is illegal: " + httpGet);
                    }
                    if (!"200".equals(String.valueOf(fromJson.get("code")))) {
                        throw new Throwable("Response code is not 200: " + httpGet);
                    }
                    Object obj = fromJson.get(AeUtil.ROOT_DATA_PATH_OLD_NAME);
                    if (obj == null) {
                        throw new Throwable("Response is illegal: " + httpGet);
                    }
                    HashMap hashMap = (HashMap) obj;
                    if (hashMap == null) {
                        throw new Throwable("Response is illegal: " + httpGet);
                    }
                    String str = (String) hashMap.get("content");
                    Long l2 = (Long) hashMap.get("timestamp");
                    if (TextUtils.isEmpty(str)) {
                        return;
                    }
                    String b2 = a.b(l2.longValue(), str);
                    MobLog.getInstance().d("contentDe: " + b2 + " (ppms->ppNece)", new Object[0]);
                    HashMap fromJson2 = hashon.fromJson(b2);
                    if (fromJson2 == null || fromJson2.isEmpty()) {
                        return;
                    }
                    Integer num = (Integer) fromJson2.get("ppms");
                    if (num != null) {
                        boolean z = true;
                        if (num.intValue() != 1) {
                            z = false;
                        }
                        Boolean unused = a.k = Boolean.valueOf(z);
                        i.a(a.k.booleanValue());
                    }
                    Integer num2 = (Integer) fromJson2.get("ppVersion");
                    if (num2 != null) {
                        int unused2 = a.o = num2.intValue();
                        a.e(a.o);
                    }
                } catch (Throwable th) {
                    MobLog.getInstance().e(th, "Request total switch error", new Object[0]);
                }
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void x() {
        try {
            DeviceHelper deviceHelper = DeviceHelper.getInstance(MobSDK.getContext());
            ArrayList<KVPair<String>> arrayList = new ArrayList<>();
            arrayList.add(new KVPair<>("appkey", MobSDK.getAppkey()));
            arrayList.add(new KVPair<>("apppkg", deviceHelper.getPackageName()));
            arrayList.add(new KVPair<>("appver", deviceHelper.getAppVersionName()));
            arrayList.add(new KVPair<>("plat", String.valueOf(deviceHelper.getPlatformCode())));
            arrayList.add(new KVPair<>("networktype", deviceHelper.getDetailNetworkTypeForStatic()));
            String authorizeForOnce = DeviceAuthorizer.authorizeForOnce();
            if (!TextUtils.isEmpty(authorizeForOnce)) {
                arrayList.add(new KVPair<>("duid", authorizeForOnce));
            }
            NetworkHelper.NetworkTimeOut networkTimeOut = new NetworkHelper.NetworkTimeOut();
            networkTimeOut.readTimout = 10000;
            networkTimeOut.connectionTimeout = 5000;
            ArrayList<KVPair<String>> arrayList2 = new ArrayList<>();
            arrayList2.add(new KVPair<>("User-Identity", MobProductCollector.getUserIdentity()));
            MobLog.getInstance().d("Request: " + e + "\nHeaders: " + arrayList2 + "\nValues: " + arrayList, new Object[0]);
            String httpGet = new NetworkHelper().httpGet(e, arrayList, arrayList2, networkTimeOut);
            NLog mobLog = MobLog.getInstance();
            StringBuilder sb = new StringBuilder();
            sb.append("Response: ");
            sb.append(httpGet);
            mobLog.d(sb.toString(), new Object[0]);
            Hashon hashon = new Hashon();
            HashMap fromJson = hashon.fromJson(httpGet);
            if (fromJson == null) {
                throw new Throwable("Response is illegal: " + httpGet);
            }
            if (!"200".equals(String.valueOf(fromJson.get("code")))) {
                throw new Throwable("Response code is not 200: " + httpGet);
            }
            Object obj = fromJson.get(AeUtil.ROOT_DATA_PATH_OLD_NAME);
            if (obj == null) {
                throw new Throwable("Response is illegal: " + httpGet);
            }
            HashMap hashMap = (HashMap) obj;
            if (hashMap == null) {
                throw new Throwable("Response is illegal: " + httpGet);
            }
            String str = (String) hashMap.get("content");
            Long l2 = (Long) hashMap.get("timestamp");
            if (TextUtils.isEmpty(str)) {
                return;
            }
            String b2 = b(l2.longValue(), str);
            MobLog.getInstance().d("contentDe: " + b2 + " (pprdms->clt, pprfms->func, pprsbs->cover, pprspw->dialog)", new Object[0]);
            HashMap fromJson2 = hashon.fromJson(b2);
            if (fromJson2 == null || fromJson2.isEmpty()) {
                return;
            }
            Integer num = (Integer) fromJson2.get("pprdms");
            if (num != null) {
                q = Boolean.valueOf(num.intValue() == 1);
                i.e(num.intValue());
            }
            Integer num2 = (Integer) fromJson2.get("pprfms");
            if (num2 != null) {
                p = Boolean.valueOf(num2.intValue() == 1);
                i.d(num2.intValue());
            }
            Integer num3 = (Integer) fromJson2.get("pprsbs");
            if (num3 != null) {
                r = Boolean.valueOf(num3.intValue() == 1);
                i.f(num3.intValue());
            }
            Integer num4 = (Integer) fromJson2.get("pprspw");
            if (num4 != null) {
                s = Boolean.valueOf(num4.intValue() == 1);
                i.g(num4.intValue());
            }
        } catch (Throwable th) {
            MobLog.getInstance().e(th, "Request total switch error", new Object[0]);
            q = false;
            p = true;
            r = true;
            s = true;
        }
    }

    private static int y() {
        if (m == -1) {
            m = i.w();
        }
        if (n == -1) {
            n = i.y();
        }
        return m >= n ? m : n;
    }
}
