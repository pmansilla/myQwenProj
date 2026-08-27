package com.mob.commons;

import android.location.Location;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Base64;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.mob.MobSDK;
import com.mob.commons.authorize.DeviceAuthorizer;
import com.mob.tools.MobHandlerThread;
import com.mob.tools.MobLog;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Dic;
import com.mob.tools.utils.FileLocker;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ReflectHelper;
import com.mob.tools.utils.ResHelper;
import com.tencent.bugly.BuglyStrategy;
import com.wx.wheelview.common.WheelConstants;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.panpf.sketch.uri.HttpUriModel;
import org.apache.commons.lang.time.DateUtils;

/* compiled from: CommonConfig.java */
/* loaded from: classes.dex */
public class b {
    private static HashMap<String, Object> g;
    private static HashMap<String, Object> a = new HashMap<>();
    private static boolean b = false;
    private static volatile boolean c = false;
    private static int d = 0;
    private static Object e = new Object();
    private static Object f = new Object();
    private static HashMap<String, Object> h = null;
    private static Object i = new Object();
    private static long j = 0;

    /* compiled from: CommonConfig.java */
    /* loaded from: classes.dex */
    public static abstract class a {
        public void a() {
        }
    }

    public static boolean A() {
        return 1 == ((Integer) a("ol", 0)).intValue();
    }

    public static long B() {
        return ((Long) a("olgapl", 3600L)).longValue();
    }

    public static long C() {
        return ((Long) a("olgaph", 60L)).longValue();
    }

    public static long D() {
        return ((Long) a("xmar", 0L)).longValue() * 1000;
    }

    public static boolean E() {
        return 1 == ((Integer) a("bi", 0)).intValue();
    }

    public static long F() {
        return ((Long) a("bigap", 30L)).longValue();
    }

    public static long G() {
        return ((Long) a("pl", 0L)).longValue();
    }

    public static long H() {
        return ((Long) a("plgap", 86400L)).longValue();
    }

    public static long I() {
        return ((Long) a("le", 0L)).longValue();
    }

    public static long J() {
        return ((Long) a("legap", 86400L)).longValue();
    }

    public static long K() {
        return ((Long) a("sd", 0L)).longValue();
    }

    public static boolean L() {
        return a(480000L);
    }

    public static boolean M() {
        return a(480000L);
    }

    public static long N() {
        return ((Integer) a("deup", 2)).intValue() * 1000;
    }

    public static long O() {
        return ((Integer) a("digap", 2592000)).intValue() * 1000;
    }

    public static long P() {
        return ((Long) a("pe", 0L)).longValue();
    }

    public static long Q() {
        return ((Long) a("ac", 0L)).longValue();
    }

    public static long R() {
        return ((Long) a(NotificationCompat.CATEGORY_SYSTEM, 0L)).longValue();
    }

    public static long S() {
        return ((Long) a("sysgap", 2592000L)).longValue();
    }

    public static long T() {
        return ((Long) a("arpl", 0L)).longValue();
    }

    public static long U() {
        return ((Long) a("arplgap", 604800L)).longValue();
    }

    public static boolean V() {
        return ((Long) a("gm", 0L)).longValue() == 1;
    }

    public static long W() {
        return ((Long) a("aa", 0L)).longValue();
    }

    public static long X() {
        return ((Long) a("aagap", 86400L)).longValue();
    }

    public static long Y() {
        return ((Long) a("rs", 0L)).longValue();
    }

    public static long Z() {
        return ((Long) a("at", 0L)).longValue();
    }

    public static long a() {
        long j2;
        try {
            j2 = Long.valueOf(String.valueOf(g.get("serverTime"))).longValue();
        } catch (Throwable unused) {
            j2 = 0;
        }
        return j2 == 0 ? System.currentTimeMillis() : j2 + aA();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:23:0x003c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r0v24, types: [com.mob.commons.b$2] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static <T> T a(java.lang.String r6, T r7) {
        /*
            Method dump skipped, instructions count: 234
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.b.a(java.lang.String, java.lang.Object):java.lang.Object");
    }

    private static void a(int i2) {
        if (d < i2) {
            synchronized (e) {
                d = i2;
            }
        }
    }

    public static void a(long j2, boolean z) {
        j = j2;
        if (!z && j2 == 0) {
            z = DeviceHelper.getInstance(MobSDK.getContext()).amIOnForeground();
        }
        if (z) {
            i.d(j2);
        }
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.mob.commons.b$1] */
    public static void a(final a aVar) {
        if (b) {
            return;
        }
        b = true;
        new Thread() { // from class: com.mob.commons.b.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    if (!b.av()) {
                        b.aD();
                    }
                } catch (Throwable th) {
                    MobLog.getInstance().d(th);
                }
                boolean unused = b.b = false;
                if (a.this != null) {
                    a.this.a();
                }
            }
        }.start();
    }

    private static void a(Object obj) {
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("list", obj);
            File dataCacheFile = ResHelper.getDataCacheFile(MobSDK.getContext(), ".mcli");
            ResHelper.saveObjectToFile(dataCacheFile.getPath(), Data.AES128Encode(k.a(155), new Hashon().fromHashMap(hashMap)));
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
    }

    public static void a(HashMap<String, Object> hashMap) {
        synchronized (i) {
            h = new HashMap<>();
            if (hashMap != null) {
                h.putAll(hashMap);
            }
            MobLog.getInstance().d("notify onlineLock", new Object[0]);
            i.notifyAll();
        }
    }

    /* JADX WARN: Type inference failed for: r5v2, types: [com.mob.commons.b$4] */
    public static void a(HashMap<String, Object> hashMap, boolean z) {
        if (c || hashMap == null || ((Integer) ResHelper.forceCast(hashMap.get("to"), 0)).intValue() == 1 || ((Integer) ResHelper.forceCast(hashMap.get("conn"), 0)).intValue() == 0) {
            return;
        }
        c = true;
        final String str = (String) hashMap.get("fnc");
        if (TextUtils.isEmpty(str) && !z) {
            c = false;
        } else {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            new MobHandlerThread() { // from class: com.mob.commons.b.4
                /* JADX INFO: Access modifiers changed from: private */
                public void a() {
                    super.run();
                }

                @Override // com.mob.tools.MobHandlerThread, java.lang.Thread, java.lang.Runnable
                public void run() {
                    e.a(e.a("comm/locks/.dy_lock"), true, new LockAction() { // from class: com.mob.commons.b.4.1
                        @Override // com.mob.commons.LockAction
                        public boolean run(FileLocker fileLocker) {
                            try {
                                synchronized (b.a) {
                                    b.b(str);
                                }
                                a();
                                return false;
                            } catch (Throwable th) {
                                d.a().a(1, th);
                                return false;
                            }
                        }
                    });
                }
            }.start();
        }
    }

    private static boolean a(long j2) {
        return false;
    }

    private static long aA() {
        long j2;
        try {
            j2 = Long.valueOf(String.valueOf(g.get("deviceTime"))).longValue();
        } catch (Throwable unused) {
            j2 = 0;
        }
        if (j2 == 0) {
            return 0L;
        }
        return SystemClock.elapsedRealtime() - j2;
    }

    private static boolean aB() {
        HashMap hashMap = null;
        try {
            try {
                String f2 = i.f();
                if (!TextUtils.isEmpty(f2)) {
                    hashMap = new Hashon().fromJson(f2);
                }
            } catch (Throwable th) {
                MobLog.getInstance().d(th);
            }
            if (hashMap != null && !hashMap.isEmpty()) {
                b((HashMap<String, Object>) hashMap);
                a((HashMap<String, Object>) hashMap, false);
                return true;
            }
            return false;
        } catch (Throwable th2) {
            MobLog.getInstance().d(th2);
            return false;
        }
    }

    private static boolean aC() {
        String aF = aF();
        HashMap fromJson = !TextUtils.isEmpty(aF) ? new Hashon().fromJson(aF) : null;
        if (fromJson == null || fromJson.isEmpty()) {
            i.d((String) null);
            b((HashMap<String, Object>) null);
            return false;
        }
        i.d(aF);
        a((HashMap<String, Object>) fromJson);
        b((HashMap<String, Object>) fromJson);
        aJ();
        a((HashMap<String, Object>) fromJson, true);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0025, code lost:
    
        aJ();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void aD() {
        /*
            r0 = 2
            r2 = 1
            r6 = r0
            r4 = r2
        L6:
            r8 = 1000(0x3e8, double:4.94E-321)
            long r8 = r8 * r4
            r10 = 0
            java.lang.Thread.sleep(r8)     // Catch: java.lang.Throwable -> Lf java.lang.Throwable -> L11
            goto L19
        Lf:
            r0 = move-exception
            goto L56
        L11:
            r8 = move-exception
            com.mob.tools.log.NLog r9 = com.mob.tools.MobLog.getInstance()     // Catch: java.lang.Throwable -> Lf java.lang.Throwable -> L4a
            r9.d(r8)     // Catch: java.lang.Throwable -> Lf java.lang.Throwable -> L4a
        L19:
            boolean r8 = aH()     // Catch: java.lang.Throwable -> Lf java.lang.Throwable -> L4a
            if (r8 == 0) goto L2b
            r11 = 0
            int r9 = (r6 > r11 ? 1 : (r6 == r11 ? 0 : -1))
            if (r9 > 0) goto L29
            aJ()     // Catch: java.lang.Throwable -> Lf java.lang.Throwable -> L4a
            goto L52
        L29:
            r9 = 0
            long r6 = r6 - r2
        L2b:
            long r4 = r4 * r0
            r11 = 8
            if (r8 != 0) goto L36
            int r8 = (r4 > r11 ? 1 : (r4 == r11 ? 0 : -1))
            if (r8 >= 0) goto L36
            r4 = r11
        L36:
            r8 = 300(0x12c, double:1.48E-321)
            int r13 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
            if (r13 <= 0) goto L3d
            r4 = r11
        L3d:
            boolean r8 = aC()     // Catch: java.lang.Throwable -> Lf java.lang.Throwable -> L4a
            if (r8 != 0) goto L52
            boolean r8 = L()     // Catch: java.lang.Throwable -> Lf java.lang.Throwable -> L4a
            if (r8 == 0) goto L6
            goto L52
        L4a:
            r0 = move-exception
            com.mob.tools.log.NLog r1 = com.mob.tools.MobLog.getInstance()     // Catch: java.lang.Throwable -> Lf
            r1.d(r0)     // Catch: java.lang.Throwable -> Lf
        L52:
            a(r10)
            return
        L56:
            a(r10)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.b.aD():void");
    }

    private static boolean aE() {
        boolean z;
        synchronized (e) {
            z = true;
            if (d != 1) {
                z = false;
            }
        }
        return z;
    }

    private static String aF() {
        HashMap fromJson;
        try {
            if (!L() && aH()) {
                DeviceHelper deviceHelper = DeviceHelper.getInstance(MobSDK.getContext());
                NetworkHelper networkHelper = new NetworkHelper();
                String packageName = deviceHelper.getPackageName();
                String appkey = MobSDK.getAppkey();
                a(TextUtils.isEmpty(appkey) ? 1 : 2);
                ArrayList<KVPair<String>> arrayList = new ArrayList<>();
                arrayList.add(new KVPair<>("appkey", appkey));
                arrayList.add(new KVPair<>("plat", String.valueOf(deviceHelper.getPlatformCode())));
                arrayList.add(new KVPair<>("apppkg", packageName));
                arrayList.add(new KVPair<>("appver", deviceHelper.getAppVersionName()));
                arrayList.add(new KVPair<>("networktype", deviceHelper.getDetailNetworkTypeForStatic()));
                String authorizeForOnce = DeviceAuthorizer.authorizeForOnce();
                if (!TextUtils.isEmpty(authorizeForOnce)) {
                    arrayList.add(new KVPair<>("duid", authorizeForOnce));
                }
                arrayList.add(new KVPair<>("ags", String.valueOf(deviceHelper.isPackageInstalled(k.a(17)) ? 1 : -1)));
                long currentTimeMillis = System.currentTimeMillis();
                arrayList.add(new KVPair<>("ts", String.valueOf(currentTimeMillis)));
                String defaultResolvePkg = deviceHelper.getDefaultResolvePkg(k.a(18));
                List<String> resolvePkgs = deviceHelper.getResolvePkgs(k.a(18));
                StringBuilder sb = new StringBuilder();
                sb.append(defaultResolvePkg);
                sb.append("|");
                if (resolvePkgs == null || resolvePkgs.size() <= 0) {
                    sb.append("null");
                } else {
                    int size = resolvePkgs.size();
                    for (int i2 = 0; i2 < size; i2++) {
                        sb.append(resolvePkgs.get(i2));
                        if (i2 < size - 1) {
                            sb.append(",");
                        }
                    }
                }
                arrayList.add(new KVPair<>("as", Base64.encodeToString(Data.AES128Encode(Data.rawMD5(appkey + ":" + packageName + ":" + currentTimeMillis), sb.toString()), 2)));
                Boolean e2 = com.mob.commons.a.e();
                if (e2 != null) {
                    arrayList.add(new KVPair<>("isAgreePp", String.valueOf(e2)));
                }
                Boolean f2 = com.mob.commons.a.f();
                if (f2 != null) {
                    arrayList.add(new KVPair<>("isAgreePd", String.valueOf(f2)));
                }
                arrayList.add(new KVPair<>("ppVersion", String.valueOf(com.mob.commons.a.g())));
                arrayList.add(new KVPair<>("v6", String.valueOf(MobSDK.checkV6() ? 1 : -1)));
                arrayList.add(new KVPair<>("uc", String.valueOf(deviceHelper.checkUA() ? 1 : 0)));
                arrayList.add(new KVPair<>("ud", String.valueOf(deviceHelper.usbEnable() ? 1 : 0)));
                arrayList.add(new KVPair<>("dv", String.valueOf(deviceHelper.devEnable() ? 1 : 0)));
                arrayList.add(new KVPair<>("vp", String.valueOf(deviceHelper.vpn() ? 1 : 0)));
                arrayList.add(new KVPair<>("wp", String.valueOf(deviceHelper.isWifiProxy() ? 1 : 0)));
                arrayList.add(new KVPair<>("rt", String.valueOf(deviceHelper.isRooted() ? 1 : 0)));
                arrayList.add(new KVPair<>("xp", String.valueOf(deviceHelper.cx() ? 1 : 0)));
                arrayList.add(new KVPair<>("ad", String.valueOf(deviceHelper.debugable() ? 1 : 0)));
                NetworkHelper.NetworkTimeOut networkTimeOut = new NetworkHelper.NetworkTimeOut();
                networkTimeOut.readTimout = BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH;
                networkTimeOut.connectionTimeout = 10000;
                ArrayList<KVPair<String>> arrayList2 = new ArrayList<>();
                arrayList2.add(new KVPair<>("User-Identity", MobProductCollector.getUserIdentity()));
                arrayList2.add(new KVPair<>(k.a(68), com.mob.commons.b.d.d(MobSDK.getContext())));
                String httpGet = networkHelper.httpGet(aI(), arrayList, arrayList2, networkTimeOut);
                Hashon hashon = new Hashon();
                HashMap fromJson2 = hashon.fromJson(httpGet);
                if (fromJson2 == null) {
                    return null;
                }
                if (!"200".equals(String.valueOf(fromJson2.get("status")))) {
                    throw new Throwable("response is illegal: " + httpGet);
                }
                String str = (String) ResHelper.forceCast(fromJson2.get("sr"));
                byte[] rawMD5 = Data.rawMD5((appkey + ":" + packageName + ":" + fromJson2.get("timestamp")).getBytes("utf-8"));
                if (str != null && (fromJson = hashon.fromJson(new String(Data.AES128Decode(rawMD5, Base64.decode(str, 2)), "utf-8"))) != null) {
                    HashMap hashMap = (HashMap) ResHelper.forceCast(fromJson.get("cdata"));
                    if (hashMap != null) {
                        String str2 = (String) ResHelper.forceCast(hashMap.get("host"));
                        int intValue = ((Integer) ResHelper.forceCast(hashMap.get("httpport"), 0)).intValue();
                        String str3 = (String) ResHelper.forceCast(hashMap.get(FileDownloadModel.PATH));
                        if (str2 == null || intValue == 0 || str3 == null) {
                            i.e((String) null);
                        } else {
                            i.e(HttpUriModel.SCHEME + str2 + ":" + intValue + str3);
                        }
                    } else {
                        i.e((String) null);
                    }
                    HashMap hashMap2 = (HashMap) ResHelper.forceCast(fromJson.get("cconf"));
                    if (hashMap2 != null) {
                        String str4 = (String) ResHelper.forceCast(hashMap2.get("host"));
                        int intValue2 = ((Integer) ResHelper.forceCast(hashMap2.get("httpport"), 0)).intValue();
                        String str5 = (String) ResHelper.forceCast(hashMap2.get(FileDownloadModel.PATH));
                        if (str4 == null || intValue2 == 0 || str5 == null) {
                            i.f((String) null);
                        } else {
                            i.f(HttpUriModel.SCHEME + str4 + ":" + intValue2 + str5);
                        }
                    } else {
                        i.f((String) null);
                    }
                }
                String str6 = (String) ResHelper.forceCast(fromJson2.get("sc"));
                if (str6 == null) {
                    throw new Throwable("response is illegal: " + httpGet);
                }
                HashMap fromJson3 = hashon.fromJson(new String(Data.AES128Decode(rawMD5, Base64.decode(str6, 2)), "utf-8"));
                if (fromJson3 == null) {
                    throw new Throwable("response is illegal: " + httpGet);
                }
                a(fromJson3.get("illegalMacs"));
                long longValue = ((Long) ResHelper.forceCast(fromJson2.get("timestamp"), 0L)).longValue();
                fromJson3.put("deviceTime", Long.valueOf(SystemClock.elapsedRealtime()));
                fromJson3.put("serverTime", Long.valueOf(longValue));
                return hashon.fromHashMap(fromJson3);
            }
            return null;
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            try {
                i.e((String) null);
                i.f((String) null);
            } catch (Throwable th2) {
                MobLog.getInstance().d(th2);
            }
            return null;
        }
    }

    private static void aG() {
        g = new HashMap<>();
        g.put("conn", 0);
        g.put("in", 0);
        g.put("all", 0);
        g.put("aspa", 2592000L);
        g.put("un", 0);
        g.put("rt", 0);
        g.put("rtsr", 180);
        g.put("ext", 0);
        g.put("bs", 0);
        g.put("bsgap", 86400);
        g.put("di", 0);
        g.put("l", 0);
        g.put("lgap", 86400);
        g.put("wi", 0);
        g.put("wigap", 3600L);
        g.put("wl", 0);
        g.put("wlsr", Integer.valueOf(WheelConstants.WHEEL_SCROLL_DELAY_DURATION));
        g.put("wlgap", 7200);
        g.put("adle", 0);
        g.put("rtgap", 3600);
        g.put("p", 0);
        g.put("ol", 0);
        g.put("olgapl", 3600L);
        g.put("olgaph", 60L);
        g.put("xmar", 0);
        g.put("bi", 0);
        g.put("bigap", 30L);
        g.put("pl", 0);
        g.put("plgap", 86400L);
        g.put("le", 0L);
        g.put("legap", 86400L);
        g.put("sd", 0L);
        g.put("deup", 2);
        g.put("digap", 2592000L);
        g.put("illegalMacs", null);
        g.put("pe", 0L);
        g.put("pegap", 86400L);
        g.put("ac", 0L);
        g.put("acgap", 86400L);
        g.put(NotificationCompat.CATEGORY_SYSTEM, 0L);
        g.put("sysgap", 2592000L);
        g.put("arpl", 0L);
        g.put("arplgap", 604800L);
        g.put("mph", 0L);
        g.put("aw", null);
        g.put("to", 0);
        g.put("gm", 0);
        g.put("gmgap", 900);
        g.put("aa", 0L);
        g.put("aagap", 86400L);
        g.put("rs", 0L);
        g.put("rsgap", 86400L);
        g.put("cl", null);
        g.put("at", 0L);
        g.put("atgap", 900L);
        g.put("bt", 0L);
        g.put("bts", 0L);
        g.put("btsgap", 7200L);
        g.put("ppl", 0L);
        g.put("lno", 0);
        g.put("dv", 0L);
        g.put("dvch", 3600L);
        g.put("dvuh", 3600L);
        g.put("cerr", 1);
        g.put("serr", 0);
        g.put("strategyId", 0L);
        g.put("apm", 0);
        g.put("apmhuh", 300L);
        g.put("apmauh", 300L);
        g.put("oid", 0);
        g.put("pa", 0);
        g.put("pasr", 20);
        g.put("pagap", 3600L);
        g.put("nr", 1);
    }

    private static boolean aH() {
        try {
            String detailNetworkTypeForStatic = DeviceHelper.getInstance(MobSDK.getContext()).getDetailNetworkTypeForStatic();
            if (!"wifi".equals(detailNetworkTypeForStatic) && !"5g".equals(detailNetworkTypeForStatic) && !"4g".equals(detailNetworkTypeForStatic) && !"3g".equals(detailNetworkTypeForStatic)) {
                if (!"2g".equals(detailNetworkTypeForStatic)) {
                    return false;
                }
            }
            return true;
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:8:0x001b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.lang.String aI() {
        /*
            java.lang.String r0 = com.mob.commons.i.h()     // Catch: java.lang.Throwable -> Lc
            java.lang.String r1 = com.mob.commons.j.b(r0)     // Catch: java.lang.Throwable -> La
            r0 = r1
            goto L15
        La:
            r1 = move-exception
            goto Le
        Lc:
            r1 = move-exception
            r0 = 0
        Le:
            com.mob.tools.log.NLog r2 = com.mob.tools.MobLog.getInstance()
            r2.w(r1)
        L15:
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 == 0) goto L30
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = com.mob.commons.j.a()
            r0.append(r1)
            java.lang.String r1 = "/v5/gcf"
            r0.append(r1)
            java.lang.String r0 = r0.toString()
        L30:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.b.aI():java.lang.String");
    }

    private static void aJ() {
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("apm", Integer.valueOf(al()));
            hashMap.put("apmhuh", Long.valueOf(am()));
            hashMap.put("apmauh", Long.valueOf(an()));
            String fromHashMap = new Hashon().fromHashMap(hashMap);
            ReflectHelper.invokeStaticMethod(ReflectHelper.importClass("com.mob.mobapm.MobAPM"), "setJson", fromHashMap);
            MobLog.getInstance().d(">>>>> Has APM <<<<< conf: " + fromHashMap, new Object[0]);
        } catch (Throwable unused) {
            MobLog.getInstance().d(">>>>> No APM <<<<<", new Object[0]);
        }
    }

    public static boolean aa() {
        return ((Integer) a("lno", 0)).intValue() > 0;
    }

    public static long ab() {
        return ((Long) a("atgap", 900L)).longValue();
    }

    public static boolean ac() {
        return 1 == ((Integer) b("to", 0)).intValue();
    }

    public static boolean ad() {
        return 1 == ((Integer) ResHelper.forceCast(g != null ? g.get("to") : null, 0)).intValue();
    }

    public static boolean ae() {
        return 1 == ((Integer) a("ppl", 0)).intValue();
    }

    public static boolean af() {
        return 1 == ((Long) a("dv", 0L)).longValue();
    }

    public static long ag() {
        return ((Long) a("dvch", 3600L)).longValue();
    }

    public static long ah() {
        return ((Long) a("dvuh", 3600L)).longValue();
    }

    public static boolean ai() {
        return ((Integer) a("cerr", 1)).intValue() == 1;
    }

    public static boolean aj() {
        return ((Integer) a("serr", 0)).intValue() == 1;
    }

    public static long ak() {
        return ((Long) a("strategyId", 0L)).longValue();
    }

    public static int al() {
        return ((Integer) a("apm", 0)).intValue();
    }

    public static long am() {
        return ((Long) a("apmhuh", 300L)).longValue();
    }

    public static long an() {
        return ((Long) a("apmauh", 300L)).longValue();
    }

    public static boolean ao() {
        return 1 == ((Integer) a("oid", 0)).intValue();
    }

    public static boolean ap() {
        return 1 == ((Integer) a("nr", 1)).intValue();
    }

    public static boolean aq() {
        return ap() || com.mob.commons.a.a;
    }

    public static void ar() {
        b((HashMap<String, Object>) null);
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.mob.commons.b$3] */
    public static void as() {
        if (aE()) {
            new Thread() { // from class: com.mob.commons.b.3
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    while (b.ax() && b.b) {
                        try {
                            Thread.sleep(1000L);
                        } catch (Throwable unused) {
                        }
                    }
                    if (!b.ax() || b.b) {
                        return;
                    }
                    boolean unused2 = b.b = true;
                    boolean unused3 = b.c = false;
                    try {
                        if (!b.av()) {
                            b.aD();
                        }
                    } catch (Throwable th) {
                        MobLog.getInstance().d(th);
                    }
                    boolean unused4 = b.b = false;
                }
            }.start();
        }
    }

    public static HashMap<String, Object> at() {
        Location location;
        HashMap<String, Object> hashMap = new HashMap<>();
        DeviceHelper deviceHelper = DeviceHelper.getInstance(MobSDK.getContext());
        hashMap.put(k.a(41), MobSDK.getAppkey());
        hashMap.put(k.a(42), Integer.valueOf(MobSDK.SDK_VERSION_CODE));
        hashMap.put(k.a(64), 1);
        hashMap.put(k.a(43), DeviceAuthorizer.authorize(null));
        hashMap.put(k.a(44), MobSDK.getContext().getPackageName());
        hashMap.put(k.a(45), Integer.valueOf(deviceHelper.getAppVersion()));
        hashMap.put(Dic.IMEI, deviceHelper.getIMEI());
        hashMap.put(Dic.SERIAL_NO, deviceHelper.getSerialno());
        hashMap.put(Dic.MAC, deviceHelper.getMacAddress());
        hashMap.put(k.a(49), deviceHelper.getCarrier());
        hashMap.put(k.a(50), deviceHelper.getModel());
        hashMap.put(k.a(51), deviceHelper.getManufacturer());
        hashMap.put(k.a(52), deviceHelper.getNetworkType());
        hashMap.put(k.a(53), deviceHelper.getOSVersionName());
        hashMap.put(k.a(54), deviceHelper.getMIUIVersion());
        hashMap.put(k.a(55), Integer.valueOf(deviceHelper.getOSVersionInt()));
        if (A() && (location = deviceHelper.getLocation(0, 0, true)) != null) {
            hashMap.put("accmt", Float.valueOf(location.getAccuracy()));
            hashMap.put("ltdmt", Double.valueOf(location.getLatitude()));
            hashMap.put("lndmt", Double.valueOf(location.getLongitude()));
        }
        hashMap.put(k.a(59), Long.valueOf(System.currentTimeMillis()));
        hashMap.put(k.a(60), deviceHelper.getSignMD5());
        hashMap.put(k.a(61), Integer.valueOf(deviceHelper.cscreen()));
        try {
            hashMap.put(k.a(62), Integer.valueOf(deviceHelper.ih(MobSDK.getContext())));
        } catch (Throwable unused) {
        }
        hashMap.put(k.a(63), Boolean.valueOf(deviceHelper.amIOnForeground()));
        hashMap.put("anmt", deviceHelper.getAndroidID());
        hashMap.put(Dic.IMSI, deviceHelper.getIMSI());
        hashMap.put(k.a(67), Build.BRAND);
        return hashMap;
    }

    public static Object au() {
        return a;
    }

    static /* synthetic */ boolean av() {
        return aC();
    }

    static /* synthetic */ boolean ax() {
        return aE();
    }

    private static <T> T b(String str, T t) {
        T t2;
        boolean z = aA() >= DateUtils.MILLIS_PER_DAY;
        Object obj = null;
        if (g == null || g.isEmpty() || z) {
            try {
                String f2 = i.f();
                HashMap<String, T> fromJson = !TextUtils.isEmpty(f2) ? new Hashon().fromJson(f2) : null;
                if (fromJson == null) {
                    fromJson = new HashMap<>();
                }
                if ("to".equals(str)) {
                    t2 = fromJson.get("to");
                } else if ("conn".equals(str) && 1 != ((Integer) ResHelper.forceCast(fromJson.get("to"), 0)).intValue()) {
                    t2 = fromJson.get("conn");
                }
                obj = t2;
            } catch (Throwable th) {
                MobLog.getInstance().d(th);
            }
        } else if ("to".equals(str)) {
            obj = g.get("to");
        } else if ("conn".equals(str) && 1 != ((Integer) ResHelper.forceCast(g.get("to"), 0)).intValue()) {
            obj = g.get("conn");
        }
        return (T) ResHelper.forceCast(obj, t);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:50:0x019c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void b(java.lang.String r15) {
        /*
            Method dump skipped, instructions count: 467
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.b.b(java.lang.String):void");
    }

    private static synchronized void b(HashMap<String, Object> hashMap) {
        synchronized (b.class) {
            if (hashMap != null) {
                try {
                    if (!hashMap.isEmpty()) {
                        g = hashMap;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            if (g == null || g.isEmpty()) {
                aG();
            }
        }
    }

    public static boolean b() {
        return 1 == ((Integer) b("conn", 0)).intValue();
    }

    private static <T> T c(String str, T t) {
        synchronized (i) {
            if (h == null) {
                try {
                    MobLog.getInstance().d("wait onlineLock", new Object[0]);
                    i.wait(600000L);
                } catch (Throwable th) {
                    MobLog.getInstance().d(th);
                }
            }
            if (h == null) {
                return t;
            }
            return (T) ResHelper.forceCast(h.get(str), t);
        }
    }

    public static boolean c() {
        return 1 == ((Integer) a("rt", 0)).intValue();
    }

    private static boolean c(boolean z) {
        boolean z2 = aA() >= DateUtils.MILLIS_PER_DAY;
        if (!z && !z2) {
            return true;
        }
        if (!aB()) {
            return false;
        }
        if (z || z2) {
            a((a) null);
        }
        com.mob.commons.a.a(1);
        return true;
    }

    public static int d() {
        return ((Integer) a("rtsr", 180)).intValue();
    }

    public static boolean e() {
        return 1 == ((Integer) a("pa", 0)).intValue();
    }

    public static int f() {
        return ((Integer) a("pasr", 20)).intValue();
    }

    public static long g() {
        return ((Integer) a("pagap", 3600)).intValue() * 1000;
    }

    public static boolean h() {
        return 1 == ((Integer) a("in", 0)).intValue();
    }

    public static boolean i() {
        return 1 == ((Integer) a("all", 0)).intValue();
    }

    public static boolean j() {
        return 1 == ((Integer) a("un", 0)).intValue();
    }

    public static long k() {
        return ((Long) a("aspa", 2592000L)).longValue();
    }

    public static boolean l() {
        return 1 == ((Integer) a("di", 0)).intValue();
    }

    public static boolean m() {
        return 1 == ((Integer) a("ext", 0)).intValue();
    }

    public static boolean n() {
        return 1 == ((Integer) a("bs", 0)).intValue();
    }

    public static int o() {
        return ((Integer) a("bsgap", 86400)).intValue();
    }

    public static boolean p() {
        return 1 == ((Integer) a("l", 0)).intValue();
    }

    public static int q() {
        return ((Integer) a("lgap", 86400)).intValue();
    }

    public static boolean r() {
        return 1 == ((Integer) a("wi", 0)).intValue();
    }

    public static int s() {
        return ((Integer) a("wigap", 3600)).intValue();
    }

    public static boolean t() {
        return ((Integer) a("wl", 0)).intValue() > 0;
    }

    public static long u() {
        return ((Integer) a("wlsr", Integer.valueOf(WheelConstants.WHEEL_SCROLL_DELAY_DURATION))).intValue();
    }

    public static int v() {
        return ((Integer) a("wlgap", 7200)).intValue();
    }

    public static ArrayList<String> w() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("SSID");
        arrayList.add("BSSID");
        arrayList.add("level");
        arrayList.add("frequency");
        arrayList.add("___curConn");
        return (ArrayList) a("wisc", arrayList);
    }

    public static long x() {
        return a() + (((Integer) a("adle", 0)).intValue() * 1000);
    }

    public static long y() {
        return ((Integer) a("rtgap", 3600)).intValue() * 1000;
    }

    public static boolean z() {
        return 1 == ((Integer) a("p", 0)).intValue();
    }
}
