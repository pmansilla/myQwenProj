package cn.smssdk.net;

import android.text.TextUtils;
import cn.smssdk.utils.SMSLog;
import cn.smssdk.utils.SPHelper;
import com.mob.MobCommunicator;
import com.mob.MobSDK;
import com.mob.commons.SMSSDK;
import com.mob.commons.authorize.DeviceAuthorizer;
import com.mob.commons.eventrecoder.EventRecorder;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ReflectHelper;
import com.mob.tools.utils.ResHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.commons.lang.ClassUtils;

/* compiled from: Config.java */
/* loaded from: classes.dex */
public class b {
    private static b x;
    public static HashMap<String, Object> y;
    public static HashMap<String, Object> z;
    private Map<Integer, g> c;
    private C0019b g;
    private boolean i;
    private long j;
    private long l;
    private boolean m;
    private boolean n;
    private String o;
    private String p;
    private int q;
    private String r;
    private String s;
    private long t;
    private String u;
    private int k = 1;
    private String w = "";
    private final d d = new d();
    private boolean h = false;
    private SPHelper a = SPHelper.getInstance();
    private Hashon b = new Hashon();
    private ReentrantLock e = new ReentrantLock();
    private ReentrantReadWriteLock f = new ReentrantReadWriteLock();
    private DeviceHelper v = DeviceHelper.getInstance(MobSDK.getContext());

    /* compiled from: Config.java */
    /* loaded from: classes.dex */
    static class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                b.x.h();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: Config.java */
    /* renamed from: cn.smssdk.net.b$b, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public class C0019b extends Thread {
        private boolean a = true;

        C0019b() {
        }

        private void a() throws Throwable {
            boolean z;
            EventRecorder.addBegin("SMSSDK", "getConfig");
            String b = b.this.b(false);
            if (TextUtils.isEmpty(b)) {
                throw new Throwable("duid is empty!");
            }
            HashMap hashMap = new HashMap();
            hashMap.put("appkey", MobSDK.getAppkey());
            hashMap.put("duid", b);
            hashMap.put("sdkver", Integer.valueOf(cn.smssdk.utils.f.b()));
            hashMap.put("plat", Integer.valueOf(b.this.v.getPlatformCode()));
            hashMap.put("apppkg", b.this.v.getPackageName());
            hashMap.put("appver", b.this.v.getAppVersionName());
            hashMap.put("md5", b.this.v.getSignMD5());
            b bVar = b.this;
            HashMap a = bVar.a((cn.smssdk.net.a) bVar.d, (HashMap<String, Object>) hashMap, false, false, 1);
            if (cn.smssdk.utils.a.c.booleanValue()) {
                SMSLog.getInstance().d(SMSLog.FORMAT, "Config", "getConfigFromService", "Config from server got. resp: " + b.this.b.fromHashMap(a));
            }
            if (a == null) {
                b.this.w = "response is empty";
                throw new Throwable("response is empty");
            }
            cn.smssdk.logger.d.d().a(6, a);
            b.y = new HashMap<>(a);
            try {
                b.this.f.writeLock().lock();
                try {
                    z = b.this.a((HashMap<String, Object>) a);
                    try {
                        this.a = false;
                    } catch (Throwable th) {
                        th = th;
                        SMSLog.getInstance().w(th, SMSLog.FORMAT, "Config", "getConfigFromService", "parseConfig encounters error, use default config re-configure");
                        this.a = true;
                        b.this.a.setConfig("");
                        b.this.a((HashMap<String, Object>) b.this.b.fromJson("{\"updateAt\":1545204873539,\"zoneAt\":1517389200000,\"result\":{\"urls\":[{\"name\":\"getToken\",\"host\":\"sdkapi.sms.mob.com\",\"port\":80,\"action\":\"/v3/token/get\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"sign\",\"apppkg\",\"appver\",\"md5\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"getFriend\",\"host\":\"addrlist.sms.mob.com\",\"port\":80,\"action\":\"/v3/relat/fm\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"apppkg\",\"appver\",\"token\",\"contactphones\",\"md5\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"sendTextSMS\",\"host\":\"code.sms.mob.com\",\"port\":80,\"action\":\"/v3/verify/code\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"apppkg\",\"appver\",\"token\",\"zone\",\"phone\",\"simserial\",\"myPhone\",\"tempCode\",\"md5\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"verifyCode\",\"host\":\"code.sms.mob.com\",\"port\":80,\"action\":\"/v3/client/verification\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"apppkg\",\"appver\",\"token\",\"zone\",\"phone\",\"code\",\"md5\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"getZoneList\",\"host\":\"sdkapi.sms.mob.com\",\"port\":80,\"action\":\"/v3/utils/zonelist\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"apppkg\",\"appver\",\"token\",\"md5\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"getFriendNew\",\"host\":\"addrlist.sms.mob.com\",\"port\":80,\"action\":\"/v3/relat/fm/new\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"apppkg\",\"appver\",\"token\",\"contactphones\",\"md5\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"submitUser\",\"host\":\"sdkapi.sms.mob.com\",\"port\":80,\"action\":\"/v3/app/submituserinfo\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"apppkg\",\"appver\",\"token\",\"zone\",\"phone\",\"uid\",\"nickname\",\"avatar\",\"md5\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"sendVoiceSMS\",\"host\":\"code.sms.mob.com\",\"port\":80,\"action\":\"/v3/voice/verify/code\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"apppkg\",\"appver\",\"token\",\"zone\",\"phone\",\"md5\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"sdkLog\",\"host\":\"log.sms.mob.com\",\"port\":80,\"action\":\"/log/sdk\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"list\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"uploadContacts\",\"host\":\"addrlist.sms.mob.com\",\"port\":80,\"action\":\"/v3/relat/apply\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"apppkg\",\"appver\",\"token\",\"zone\",\"myPhone\",\"simserial\",\"operator\",\"secretKey\",\"contacts\",\"imsi\",\"md5\"],\"zip\":1,\"request\":1,\"frequency\":0},{\"name\":\"uploadContactsNew\",\"host\":\"addrlist.sms.mob.com\",\"port\":80,\"action\":\"/v3/relat/apply/new\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"zone\",\"myPhone\",\"simserial\",\"operator\",\"secretKey\",\"contacts\",\"md5\"],\"zip\":1,\"request\":1,\"frequency\":0}]},\"request\":1,\"isSensitiveOrigin\":true,\"isPhoneSensitiveOrigin\":false}"));
                        if (!this.a) {
                            SMSLog.getInstance().d(SMSLog.FORMAT, "Config", "getConfigFromService", "config observed from server has been updated, store into SP");
                            b.this.a.setConfig(b.this.b.fromHashMap(a));
                        }
                        b.this.f.writeLock().unlock();
                        EventRecorder.addEnd("SMSSDK", "getConfig");
                    }
                } catch (Throwable th2) {
                    th = th2;
                    z = false;
                }
                if (!this.a && z) {
                    SMSLog.getInstance().d(SMSLog.FORMAT, "Config", "getConfigFromService", "config observed from server has been updated, store into SP");
                    b.this.a.setConfig(b.this.b.fromHashMap(a));
                }
                b.this.f.writeLock().unlock();
                EventRecorder.addEnd("SMSSDK", "getConfig");
            } catch (Throwable th3) {
                b.this.f.writeLock().unlock();
                throw th3;
            }
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                a();
            } catch (Throwable th) {
                cn.smssdk.logger.d.d().a(6, th);
                if (b.this.f.writeLock().tryLock()) {
                    b.this.f.writeLock().unlock();
                }
            }
        }
    }

    private b() {
    }

    private g a(int i) throws Throwable {
        if (MobSDK.getAppkey() != null && MobSDK.getAppkey().equalsIgnoreCase("moba6b6c6d6")) {
            String valueOf = "zh".equals(DeviceHelper.getInstance(MobSDK.getContext()).getOSLanguage()) ? String.valueOf(new char[]{25152, 22635, 20889, 'A', 'P', 'P', 'K', 'E', 'Y', 20165, 20379, 27979, 35797, 20351, 29992, 65292, 19988, 19981, 23450, 26399, 22833, 25928, 65292, 35831, 21040, 'm', 'o', 'b', ClassUtils.PACKAGE_SEPARATOR_CHAR, 'c', 'o', 'm', 21518, 21488, 30003, 35831, 27491, 24335, 'A', 'P', 'P', 'K', 'E', 'Y'}) : "This appkey only for demo!Please request a new one for your own App";
            SMSLog.getInstance().e(SMSLog.FORMAT, "Config", "getApi", "SMSSDK WARNING: " + valueOf);
        }
        h();
        if (this.k != 0) {
            return this.c.get(Integer.valueOf(i));
        }
        throw new Throwable("{\"status\":605,\"detail\":\"" + MobSDK.getContext().getResources().getString(ResHelper.getStringRes(MobSDK.getContext(), "smssdk_error_desc_605")) + "\"}");
    }

    private String a(String str) throws Throwable {
        if (TextUtils.isEmpty(str)) {
            SMSLog.getInstance().d(SMSLog.FORMAT, "Config", "checkConfigVersion", "Local config does not exist in SP, use default config.");
            return str;
        }
        if (((Integer) this.b.fromJson(str).get("expire_at")) == null) {
            SMSLog.getInstance().d(SMSLog.FORMAT, "Config", "checkConfigVersion", "Local config is for SMSSDK V3.0.0 or later, use local config.");
            return str;
        }
        this.a.setConfig("");
        this.a.setBufferedCountrylist("");
        SMSLog.getInstance().d(SMSLog.FORMAT, "Config", "checkConfigVersion", "Local config is for SMSSDK V2.1.4 or older, clear SP and use default config instead.");
        return "";
    }

    private HashMap<String, Object> a(int i, cn.smssdk.net.a aVar, HashMap<String, Object> hashMap, int i2, Throwable th) throws Throwable {
        int c;
        SMSLog.getInstance().d(SMSLog.FORMAT, "Config", "handleErrorStatus", "[" + aVar.b + "]Handle error status. status: " + i + ", count: " + i2);
        int i3 = i2 + 1;
        if (i == 453) {
            if ((aVar instanceof g) && (c = aVar.c()) > 0) {
                aVar = a(c);
            }
            return a(aVar, hashMap, false, false, i3);
        }
        if (i == 419 || i == 420) {
            this.a.setToken("");
            return a(aVar, hashMap, true, true, i3);
        }
        if (i == 401 || i == 402) {
            this.a.setToken("");
            return a(aVar, hashMap, false, true, i3);
        }
        if (i == 482) {
            return a(aVar, hashMap, false, false, i3);
        }
        throw th;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public HashMap<String, Object> a(cn.smssdk.net.a aVar, HashMap<String, Object> hashMap, boolean z2, boolean z3, int i) throws Throwable {
        if (i > 5) {
            int stringRes = ResHelper.getStringRes(MobSDK.getContext(), "smssdk_error_desc_server_busy");
            String string = stringRes > 0 ? MobSDK.getContext().getString(stringRes) : "Server is busy!";
            HashMap hashMap2 = new HashMap();
            hashMap2.put("description", string);
            throw new Throwable(this.b.fromHashMap(hashMap2));
        }
        try {
            try {
                return this.b.fromJson(aVar.b(b(z2), (!(aVar instanceof g) || aVar.c() == 3) ? null : a(z3), hashMap));
            } catch (Throwable th) {
                SMSLog.getInstance().e(th);
                return null;
            }
        } catch (Throwable th2) {
            String b = aVar != null ? aVar.b() : "";
            SMSLog.getInstance().e(th2, SMSLog.FORMAT, "Config", "post", "[" + b + "]Request exception. msg= " + th2.getMessage());
            return a(th2, aVar, hashMap, i);
        }
    }

    private HashMap<String, Object> a(Throwable th, cn.smssdk.net.a aVar, HashMap<String, Object> hashMap, int i) throws Throwable {
        if (!(th instanceof MobCommunicator.NetworkError)) {
            throw th;
        }
        HashMap fromJson = this.b.fromJson(th.getMessage());
        ((Integer) ResHelper.forceCast(fromJson.get("httpStatus"), -1)).intValue();
        int intValue = ((Integer) ResHelper.forceCast(fromJson.get("status"), -1)).intValue();
        String str = (String) ResHelper.forceCast(fromJson.get("res"), "");
        TextUtils.isEmpty(str);
        if (intValue == -1) {
            throw th;
        }
        try {
            HashMap<String, Object> a2 = a(intValue, aVar, hashMap, i, th);
            if (a2 != null) {
                return a2;
            }
        } catch (Throwable th2) {
            SMSLog.getInstance().d(th2, SMSLog.FORMAT, "Config", "handleThrowable", "ErrorStatus no need to be handled");
        }
        fromJson.put("description", b(intValue));
        fromJson.put("detail", c(intValue));
        throw new Throwable(this.b.fromHashMap(fromJson));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean a(HashMap<String, Object> hashMap) throws Throwable {
        int i;
        if (cn.smssdk.utils.a.c.booleanValue()) {
            SMSLog.getInstance().d(SMSLog.FORMAT, "Config", "parseConfig", "Parse config. config: " + this.b.fromHashMap(hashMap));
        }
        Long l = (Long) hashMap.get("updateAt");
        long longValue = l.longValue();
        long j = this.j;
        if (longValue < j) {
            SMSLog.getInstance().d(SMSLog.FORMAT, "Config", "parseConfig", "'updateAt' < local config, DO NOT update local config.");
            return false;
        }
        if (j == 0) {
            SMSLog.getInstance().d(SMSLog.FORMAT, "Config", "parseConfig", "Initialize local config.");
        } else {
            SMSLog.getInstance().d(SMSLog.FORMAT, "Config", "parseConfig", "'updateAt' >= local config, update local config.");
        }
        this.j = l.longValue();
        long longValue2 = ((Long) hashMap.get("zoneAt")).longValue();
        long j2 = this.l;
        if (longValue2 > j2) {
            if (j2 != 0) {
                this.i = true;
            }
            this.l = longValue2;
        }
        this.k = ((Integer) hashMap.get("request")).intValue();
        Object obj = hashMap.get("isSensitiveOrigin");
        if (obj != null) {
            this.m = ((Boolean) obj).booleanValue();
            cn.smssdk.utils.b.c().c(this.m);
        }
        Object obj2 = hashMap.get("phoneIsSensitiveOrigin");
        if (obj2 != null) {
            this.n = ((Boolean) obj2).booleanValue();
            cn.smssdk.utils.b.c().b(this.n);
        }
        this.o = (String) hashMap.get("publicKey");
        this.p = (String) hashMap.get("modulus");
        Integer num = (Integer) hashMap.get("size");
        this.q = num != null ? num.intValue() : 0;
        if (!TextUtils.isEmpty(this.o) && !TextUtils.isEmpty(this.p) && (i = this.q) > 0) {
            c.a(this.o, this.p, i);
        }
        ArrayList arrayList = (ArrayList) ((HashMap) hashMap.get("result")).get("urls");
        Map<Integer, g> map = this.c;
        if (map == null) {
            this.c = new HashMap();
        } else if (map != null && map.size() > 0) {
            this.c.clear();
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            HashMap<String, Object> hashMap2 = (HashMap) it.next();
            g gVar = new g();
            gVar.a(hashMap2);
            gVar.a(this.f);
            this.c.put(Integer.valueOf(gVar.c()), gVar);
            if (cn.smssdk.utils.a.b.booleanValue()) {
                SMSLog.getInstance().d("api: " + cn.smssdk.utils.c.a(hashMap2) + " urls.size: " + this.c.size(), new Object[0]);
            }
        }
        return true;
    }

    private String b(int i) {
        try {
            int stringRes = ResHelper.getStringRes(MobSDK.getContext(), "smssdk_error_desc_" + i);
            if (stringRes > 0) {
                return MobSDK.getContext().getString(stringRes);
            }
            return null;
        } catch (Throwable th) {
            SMSLog.getInstance().w(th);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String b(boolean z2) {
        if (z2 || TextUtils.isEmpty(this.r)) {
            try {
                this.e.lock();
                if (!TextUtils.isEmpty(this.r)) {
                    return this.r;
                }
                this.r = DeviceAuthorizer.authorize(new SMSSDK());
                cn.smssdk.logger.a.p().a(this.r);
            } finally {
                this.e.unlock();
            }
        }
        return this.r;
    }

    private String c(int i) {
        try {
            int stringRes = ResHelper.getStringRes(MobSDK.getContext(), "smssdk_error_detail_" + i);
            if (stringRes > 0) {
                return MobSDK.getContext().getString(stringRes);
            }
            return null;
        } catch (Throwable th) {
            SMSLog.getInstance().w(th);
            return null;
        }
    }

    private String e() {
        if (!TextUtils.isEmpty(this.u)) {
            return this.u;
        }
        try {
            this.u = Data.MD5(MobSDK.getContext().getPackageManager().getPackageInfo(MobSDK.getContext().getPackageName(), 64).signatures[0].toByteArray());
            return this.u;
        } catch (Throwable th) {
            SMSLog.getInstance().d(th);
            return null;
        }
    }

    public static String f() {
        boolean z2 = false;
        try {
            ReflectHelper.invokeStaticMethod("DeviceHelper", "getInstance", MobSDK.getContext());
            z2 = true;
        } catch (Throwable unused) {
        }
        return "reflectInvoke: " + z2;
    }

    public static b g() {
        if (x == null) {
            synchronized (b.class) {
                x = new b();
                new Thread(new a()).start();
            }
        }
        return x;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't wrap try/catch for region: R(14:7|(1:9)|(1:11)|12|(9:17|18|19|20|21|22|23|24|25)|34|(1:36)|37|20|21|22|23|24|25) */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0098, code lost:
    
        r1 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0099, code lost:
    
        r6.w = r1.getMessage();
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00a6, code lost:
    
        cn.smssdk.utils.SMSLog.getInstance().d(cn.smssdk.utils.SMSLog.FORMAT, "Config", "initConfig", r6.w);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void h() throws java.lang.Throwable {
        /*
            r6 = this;
            java.util.concurrent.locks.ReentrantReadWriteLock r0 = r6.f     // Catch: java.lang.Throwable -> Lca
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r0 = r0.writeLock()     // Catch: java.lang.Throwable -> Lca
            r0.lock()     // Catch: java.lang.Throwable -> Lca
            boolean r0 = r6.h     // Catch: java.lang.Throwable -> Lca
            if (r0 == 0) goto L17
            java.util.concurrent.locks.ReentrantReadWriteLock r0 = r6.f
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r0 = r0.writeLock()
            r0.unlock()
            return
        L17:
            com.mob.commons.eventrecoder.EventRecorder.prepare()     // Catch: java.lang.Throwable -> Lca
            r0 = 0
            java.lang.Boolean r1 = cn.smssdk.utils.a.d     // Catch: java.lang.Throwable -> Lca
            boolean r1 = r1.booleanValue()     // Catch: java.lang.Throwable -> Lca
            if (r1 == 0) goto L29
            java.lang.String r0 = "SMSSDK"
            java.lang.String r0 = com.mob.commons.eventrecoder.EventRecorder.checkRecord(r0)     // Catch: java.lang.Throwable -> Lca
        L29:
            if (r0 == 0) goto L2e
            com.mob.commons.eventrecoder.EventRecorder.clear()     // Catch: java.lang.Throwable -> Lca
        L2e:
            cn.smssdk.utils.SPHelper r1 = r6.a     // Catch: java.lang.Throwable -> Lca
            java.lang.String r1 = r1.getConfig()     // Catch: java.lang.Throwable -> Lca
            cn.smssdk.net.b$b r2 = new cn.smssdk.net.b$b     // Catch: java.lang.Throwable -> Lca
            r2.<init>()     // Catch: java.lang.Throwable -> Lca
            r6.g = r2     // Catch: java.lang.Throwable -> Lca
            java.lang.String r1 = r6.a(r1)     // Catch: java.lang.Throwable -> Lca
            java.lang.String r2 = "SMSSDK"
            java.lang.String r3 = "parseConfig"
            com.mob.commons.eventrecoder.EventRecorder.addBegin(r2, r3)     // Catch: java.lang.Throwable -> Lca
            boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch: java.lang.Throwable -> Lca
            if (r0 == 0) goto L70
            boolean r0 = android.text.TextUtils.isEmpty(r1)     // Catch: java.lang.Throwable -> Lca
            if (r0 == 0) goto L53
            goto L70
        L53:
            com.mob.tools.utils.Hashon r0 = r6.b     // Catch: java.lang.Throwable -> Lca
            java.util.HashMap r0 = r0.fromJson(r1)     // Catch: java.lang.Throwable -> Lca
            r6.a(r0)     // Catch: java.lang.Throwable -> L5d
            goto L84
        L5d:
            cn.smssdk.utils.SPHelper r0 = r6.a     // Catch: java.lang.Throwable -> Lca
            java.lang.String r1 = ""
            r0.setConfig(r1)     // Catch: java.lang.Throwable -> Lca
            com.mob.tools.utils.Hashon r0 = r6.b     // Catch: java.lang.Throwable -> Lca
            java.lang.String r1 = "{\"updateAt\":1545204873539,\"zoneAt\":1517389200000,\"result\":{\"urls\":[{\"name\":\"getToken\",\"host\":\"sdkapi.sms.mob.com\",\"port\":80,\"action\":\"/v3/token/get\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"sign\",\"apppkg\",\"appver\",\"md5\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"getFriend\",\"host\":\"addrlist.sms.mob.com\",\"port\":80,\"action\":\"/v3/relat/fm\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"apppkg\",\"appver\",\"token\",\"contactphones\",\"md5\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"sendTextSMS\",\"host\":\"code.sms.mob.com\",\"port\":80,\"action\":\"/v3/verify/code\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"apppkg\",\"appver\",\"token\",\"zone\",\"phone\",\"simserial\",\"myPhone\",\"tempCode\",\"md5\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"verifyCode\",\"host\":\"code.sms.mob.com\",\"port\":80,\"action\":\"/v3/client/verification\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"apppkg\",\"appver\",\"token\",\"zone\",\"phone\",\"code\",\"md5\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"getZoneList\",\"host\":\"sdkapi.sms.mob.com\",\"port\":80,\"action\":\"/v3/utils/zonelist\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"apppkg\",\"appver\",\"token\",\"md5\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"getFriendNew\",\"host\":\"addrlist.sms.mob.com\",\"port\":80,\"action\":\"/v3/relat/fm/new\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"apppkg\",\"appver\",\"token\",\"contactphones\",\"md5\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"submitUser\",\"host\":\"sdkapi.sms.mob.com\",\"port\":80,\"action\":\"/v3/app/submituserinfo\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"apppkg\",\"appver\",\"token\",\"zone\",\"phone\",\"uid\",\"nickname\",\"avatar\",\"md5\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"sendVoiceSMS\",\"host\":\"code.sms.mob.com\",\"port\":80,\"action\":\"/v3/voice/verify/code\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"apppkg\",\"appver\",\"token\",\"zone\",\"phone\",\"md5\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"sdkLog\",\"host\":\"log.sms.mob.com\",\"port\":80,\"action\":\"/log/sdk\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"list\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"uploadContacts\",\"host\":\"addrlist.sms.mob.com\",\"port\":80,\"action\":\"/v3/relat/apply\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"apppkg\",\"appver\",\"token\",\"zone\",\"myPhone\",\"simserial\",\"operator\",\"secretKey\",\"contacts\",\"imsi\",\"md5\"],\"zip\":1,\"request\":1,\"frequency\":0},{\"name\":\"uploadContactsNew\",\"host\":\"addrlist.sms.mob.com\",\"port\":80,\"action\":\"/v3/relat/apply/new\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"zone\",\"myPhone\",\"simserial\",\"operator\",\"secretKey\",\"contacts\",\"md5\"],\"zip\":1,\"request\":1,\"frequency\":0}]},\"request\":1,\"isSensitiveOrigin\":true,\"isPhoneSensitiveOrigin\":false}"
            java.util.HashMap r0 = r0.fromJson(r1)     // Catch: java.lang.Throwable -> Lca
            r6.a(r0)     // Catch: java.lang.Throwable -> Lca
            goto L84
        L70:
            com.mob.tools.utils.Hashon r0 = r6.b     // Catch: java.lang.Throwable -> Lca
            java.lang.String r1 = "{\"updateAt\":1545204873539,\"zoneAt\":1517389200000,\"result\":{\"urls\":[{\"name\":\"getToken\",\"host\":\"sdkapi.sms.mob.com\",\"port\":80,\"action\":\"/v3/token/get\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"sign\",\"apppkg\",\"appver\",\"md5\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"getFriend\",\"host\":\"addrlist.sms.mob.com\",\"port\":80,\"action\":\"/v3/relat/fm\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"apppkg\",\"appver\",\"token\",\"contactphones\",\"md5\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"sendTextSMS\",\"host\":\"code.sms.mob.com\",\"port\":80,\"action\":\"/v3/verify/code\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"apppkg\",\"appver\",\"token\",\"zone\",\"phone\",\"simserial\",\"myPhone\",\"tempCode\",\"md5\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"verifyCode\",\"host\":\"code.sms.mob.com\",\"port\":80,\"action\":\"/v3/client/verification\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"apppkg\",\"appver\",\"token\",\"zone\",\"phone\",\"code\",\"md5\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"getZoneList\",\"host\":\"sdkapi.sms.mob.com\",\"port\":80,\"action\":\"/v3/utils/zonelist\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"apppkg\",\"appver\",\"token\",\"md5\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"getFriendNew\",\"host\":\"addrlist.sms.mob.com\",\"port\":80,\"action\":\"/v3/relat/fm/new\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"apppkg\",\"appver\",\"token\",\"contactphones\",\"md5\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"submitUser\",\"host\":\"sdkapi.sms.mob.com\",\"port\":80,\"action\":\"/v3/app/submituserinfo\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"apppkg\",\"appver\",\"token\",\"zone\",\"phone\",\"uid\",\"nickname\",\"avatar\",\"md5\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"sendVoiceSMS\",\"host\":\"code.sms.mob.com\",\"port\":80,\"action\":\"/v3/voice/verify/code\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"apppkg\",\"appver\",\"token\",\"zone\",\"phone\",\"md5\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"sdkLog\",\"host\":\"log.sms.mob.com\",\"port\":80,\"action\":\"/log/sdk\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"list\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"uploadContacts\",\"host\":\"addrlist.sms.mob.com\",\"port\":80,\"action\":\"/v3/relat/apply\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"apppkg\",\"appver\",\"token\",\"zone\",\"myPhone\",\"simserial\",\"operator\",\"secretKey\",\"contacts\",\"imsi\",\"md5\"],\"zip\":1,\"request\":1,\"frequency\":0},{\"name\":\"uploadContactsNew\",\"host\":\"addrlist.sms.mob.com\",\"port\":80,\"action\":\"/v3/relat/apply/new\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"zone\",\"myPhone\",\"simserial\",\"operator\",\"secretKey\",\"contacts\",\"md5\"],\"zip\":1,\"request\":1,\"frequency\":0}]},\"request\":1,\"isSensitiveOrigin\":true,\"isPhoneSensitiveOrigin\":false}"
            java.util.HashMap r0 = r0.fromJson(r1)     // Catch: java.lang.Throwable -> Lca
            if (r0 == 0) goto L81
            java.util.HashMap r1 = new java.util.HashMap     // Catch: java.lang.Throwable -> Lca
            r1.<init>(r0)     // Catch: java.lang.Throwable -> Lca
            cn.smssdk.net.b.z = r1     // Catch: java.lang.Throwable -> Lca
        L81:
            r6.a(r0)     // Catch: java.lang.Throwable -> Lca
        L84:
            r0 = 1
            r6.h = r0     // Catch: java.lang.Throwable -> Lca
            cn.smssdk.b.f()     // Catch: java.lang.Throwable -> L98
            cn.smssdk.logger.d r1 = cn.smssdk.logger.d.d()     // Catch: java.lang.Throwable -> L98
            r2 = 6
            r1.b(r2)     // Catch: java.lang.Throwable -> L98
            cn.smssdk.net.b$b r1 = r6.g     // Catch: java.lang.Throwable -> L98
            r1.start()     // Catch: java.lang.Throwable -> L98
            goto Lb9
        L98:
            r1 = move-exception
            java.lang.String r1 = r1.getMessage()     // Catch: java.lang.Throwable -> Lca
            r6.w = r1     // Catch: java.lang.Throwable -> Lca
            com.mob.tools.log.NLog r1 = cn.smssdk.utils.SMSLog.getInstance()     // Catch: java.lang.Throwable -> Lca
            java.lang.String r2 = "[SMSSDK][%s][%s] %s"
            r3 = 3
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch: java.lang.Throwable -> Lca
            r4 = 0
            java.lang.String r5 = "Config"
            r3[r4] = r5     // Catch: java.lang.Throwable -> Lca
            java.lang.String r4 = "initConfig"
            r3[r0] = r4     // Catch: java.lang.Throwable -> Lca
            r0 = 2
            java.lang.String r4 = r6.w     // Catch: java.lang.Throwable -> Lca
            r3[r0] = r4     // Catch: java.lang.Throwable -> Lca
            r1.d(r2, r3)     // Catch: java.lang.Throwable -> Lca
        Lb9:
            java.lang.String r0 = "SMSSDK"
            java.lang.String r1 = "parseConfig"
            com.mob.commons.eventrecoder.EventRecorder.addEnd(r0, r1)     // Catch: java.lang.Throwable -> Lca
            java.util.concurrent.locks.ReentrantReadWriteLock r0 = r6.f
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r0 = r0.writeLock()
            r0.unlock()
            return
        Lca:
            r0 = move-exception
            java.util.concurrent.locks.ReentrantReadWriteLock r1 = r6.f
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r1 = r1.writeLock()
            r1.unlock()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.smssdk.net.b.h():void");
    }

    public synchronized String a(boolean z2) throws Throwable {
        this.s = this.a.getToken();
        this.t = this.a.getTokenCacheAt();
        SMSLog.getInstance().d(SMSLog.FORMAT, "Config", "getToken", "force: " + z2 + ", tokenInSp: " + this.s + ", time: " + this.t);
        if (!z2 && !TextUtils.isEmpty(this.s) && this.t + 7200000 > System.currentTimeMillis()) {
            SMSLog.getInstance().d(SMSLog.FORMAT, "Config", "getToken", "Use token stored in SP. token=" + this.s);
            return this.s;
        }
        SMSLog.getInstance().d(SMSLog.FORMAT, "Config", "getToken", "Observe token from server.");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sign", e());
        this.s = (String) a(3, hashMap).get("token");
        if (TextUtils.isEmpty(this.s)) {
            throw new Throwable("get token error!");
        }
        this.a.setToken(this.s);
        this.a.setTokenCacheAt(System.currentTimeMillis());
        return this.s;
    }

    public HashMap<String, Object> a(int i, HashMap<String, Object> hashMap) throws Throwable {
        g a2 = a(i);
        HashMap<String, Object> a3 = a((cn.smssdk.net.a) a2, hashMap, false, false, 1);
        if (a2.c() != 9 || a3 == null) {
            if (a3 != null) {
                a2.d();
            }
        } else if (((Integer) a3.get("smart")) == null) {
            a2.d();
        }
        return a3;
    }

    public boolean a() {
        return this.i;
    }

    public long b() {
        return this.l;
    }

    public void c() {
        this.i = false;
    }
}
