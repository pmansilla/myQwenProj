package com.mob.mcl.c;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.graphics.drawable.PathInterpolatorCompat;
import android.text.TextUtils;
import android.util.Base64;
import com.amap.location.common.model.AmapLoc;
import com.autonavi.amap.mapcore.AeUtil;
import com.example.otalib.boads.Constant;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;
import com.mob.MobSDK;
import com.mob.apc.APCMessage;
import com.mob.mcl.BusinessCallBack;
import com.mob.mcl.BusinessMessageListener;
import com.mob.mcl.MCLSDK;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.utils.ActivityTracker;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.UIHandler;
import com.sun.mail.imap.IMAPStore;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.lang.time.DateUtils;

/* compiled from: TcpHelper.java */
/* loaded from: classes.dex */
public class i implements f, ActivityTracker.Tracker {
    private static volatile i y;
    public long a;
    public String b;
    public ArrayList<String> d;
    public boolean f;
    public boolean g;
    public boolean h;
    public boolean i;
    public String j;
    public long k;
    public boolean l;
    private String p;
    private String q;
    private Context r;
    private MCLSDK.ELPMessageListener s;
    private boolean u;
    private long w;
    private Activity x;
    public AtomicLong c = new AtomicLong(0);
    public int e = SubsamplingScaleImageView.ORIENTATION_270;
    private h o = new h(this);
    private NetworkHelper m = new NetworkHelper();
    private Hashon n = new Hashon();
    private HashMap<Integer, HashSet<BusinessMessageListener>> t = new HashMap<>();
    private com.mob.mcl.d.c v = new com.mob.mcl.d.c(MobSDK.getContext());

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TcpHelper.java */
    /* loaded from: classes.dex */
    public class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (!i.c().i()) {
                i.c().j();
            }
            i.this.a(5000);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TcpHelper.java */
    /* loaded from: classes.dex */
    public class b implements Runnable {
        final /* synthetic */ BusinessCallBack a;

        /* compiled from: TcpHelper.java */
        /* loaded from: classes.dex */
        class a implements Handler.Callback {
            final /* synthetic */ boolean a;

            a(boolean z) {
                this.a = z;
            }

            @Override // android.os.Handler.Callback
            public boolean handleMessage(Message message) {
                BusinessCallBack businessCallBack = b.this.a;
                if (businessCallBack == null) {
                    return false;
                }
                businessCallBack.callback(Boolean.valueOf(this.a));
                return false;
            }
        }

        b(BusinessCallBack businessCallBack) {
            this.a = businessCallBack;
        }

        @Override // java.lang.Runnable
        public void run() {
            boolean a2 = i.this.f() ? i.c().a(PathInterpolatorCompat.MAX_NUM_POINTS, 3) : false;
            UIHandler.sendEmptyMessage(0, new a(a2));
            if (a2) {
                return;
            }
            if (!i.c().i()) {
                i.c().j();
            }
            i.this.a(5000);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TcpHelper.java */
    /* loaded from: classes.dex */
    public class c implements Handler.Callback {
        final /* synthetic */ BusinessMessageListener a;
        final /* synthetic */ Integer b;
        final /* synthetic */ String c;
        final /* synthetic */ String d;

        c(i iVar, BusinessMessageListener businessMessageListener, Integer num, String str, String str2) {
            this.a = businessMessageListener;
            this.b = num;
            this.c = str;
            this.d = str2;
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            BusinessMessageListener businessMessageListener = this.a;
            if (businessMessageListener == null) {
                return false;
            }
            businessMessageListener.messageReceived(this.b.intValue(), this.c, this.d);
            return false;
        }
    }

    private i() {
    }

    public static int a(HashMap<String, Object> hashMap, String str, int i) {
        if (hashMap != null && hashMap.containsKey(str)) {
            Object obj = hashMap.get(str);
            if (obj instanceof Integer) {
                return ((Integer) obj).intValue();
            }
        }
        return i;
    }

    private String a(long j) {
        return String.format("%16s", Integer.valueOf(Math.abs(Arrays.hashCode(new long[]{j})))).replaceAll(SQLBuilder.BLANK, AmapLoc.RESULT_TYPE_GPS).substring(0, 16);
    }

    private HashMap<String, Object> a(HashMap<String, Object> hashMap) {
        return (a(hashMap, "code", 0) == 200 && hashMap.containsKey(AeUtil.ROOT_DATA_PATH_OLD_NAME)) ? (HashMap) hashMap.get(AeUtil.ROOT_DATA_PATH_OLD_NAME) : new HashMap<>();
    }

    private void a(long j, boolean z) {
        if (this.o != null) {
            try {
                String a2 = a(this.c.get());
                HashMap hashMap = new HashMap();
                hashMap.put("state", Boolean.valueOf(z));
                String fromHashMap = this.n.fromHashMap(hashMap);
                String b2 = b(a2, fromHashMap);
                int length = b2 != null ? b2.length() : 0;
                com.mob.mcl.d.b.a().a("tp sd ty = 1006 , u = " + j + " bo : " + fromHashMap);
                h hVar = this.o;
                if (j == 0) {
                    j = hVar.c.incrementAndGet();
                }
                com.mob.mcl.c.a aVar = hVar.a;
                if (aVar == null) {
                    throw null;
                }
                e eVar = new e();
                synchronized (aVar.e) {
                    aVar.e.put(eVar, Long.valueOf(j));
                }
                try {
                    OutputStream outputStream = aVar.a.getOutputStream();
                    ByteBuffer allocate = ByteBuffer.allocate(length + 17);
                    allocate.put((byte) 1);
                    allocate.putInt(length);
                    allocate.putInt(1006);
                    allocate.putLong(j);
                    if (b2 != null) {
                        allocate.put(b2.getBytes(Charset.forName("UTF-8")));
                    }
                    outputStream.write(allocate.array());
                    outputStream.flush();
                } catch (Throwable th) {
                    ((i) aVar.b).a(aVar, th);
                }
            } catch (Throwable th2) {
                com.mob.mcl.d.b.a().a(th2);
            }
        }
    }

    private synchronized boolean a(String str, int i) {
        if (i != 0) {
            if (!TextUtils.isEmpty(str)) {
                if (System.currentTimeMillis() <= this.v.b(str)) {
                    return true;
                }
                this.v.a(str, System.currentTimeMillis() + (i * 1000));
            }
        }
        return false;
    }

    private synchronized boolean a(boolean z, String str, int i, String str2, int i2) {
        try {
            if (i < this.d.size() && i < 3) {
                com.mob.mcl.d.b.a().a("tp rg domain : " + str + " count : " + i);
                try {
                    HashMap<String, Object> a2 = a(str, str2, i2);
                    if (a2 != null && a2.containsKey("type")) {
                        int intValue = ((Integer) a2.get("type")).intValue();
                        if (intValue == 1 && a2.containsKey("token")) {
                            this.c.set(((Long) a2.get("token")).longValue());
                            d.a().d();
                            com.mob.mcl.d.b.a().b("tcp register success");
                            return true;
                        }
                        if (intValue == 2 && a2.containsKey("domain")) {
                            String str3 = (String) a2.get("domain");
                            if (!TextUtils.isEmpty(str3)) {
                                return a(true, str3, 2, str2, i2);
                            }
                        } else if (intValue == 3) {
                            this.u = true;
                            com.mob.mcl.c.a aVar = this.o.a;
                            if (aVar != null) {
                                aVar.a(false);
                            }
                            return false;
                        }
                    }
                } catch (Throwable th) {
                    com.mob.mcl.d.b.a().a("tcp register exp : " + th.getMessage());
                }
                int i3 = i + 1;
                if (i3 < this.d.size() && !z) {
                    return a(false, this.d.get(i3), i3, str2, i2);
                }
            }
            com.mob.mcl.d.d.a((String) null);
            this.d = null;
        } finally {
            return false;
        }
        return false;
    }

    private String b() {
        Object obj;
        HashMap hashMap = new HashMap();
        hashMap.put("appkey", this.p);
        hashMap.put("apppkg", this.r.getPackageName());
        hashMap.put("plat", 1);
        hashMap.put("pushId", d());
        hashMap.put("guardId", this.j);
        try {
            Bundle bundle = this.r.getPackageManager().getPackageInfo(this.r.getPackageName(), 128).applicationInfo.metaData;
            if (bundle != null && !bundle.isEmpty() && (obj = bundle.get("mob_guard_version")) != null) {
                hashMap.put(IMAPStore.ID_VERSION, String.valueOf(obj));
            }
        } catch (Throwable th) {
            com.mob.mcl.d.b.a().a(th);
        }
        return this.n.fromHashMap(hashMap);
    }

    private String b(String str, String str2) throws Throwable {
        return Base64.encodeToString(Data.AES128Encode(str, str2), 2);
    }

    private HashMap<String, Object> b(String str) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            if (!TextUtils.isEmpty(str) && str.startsWith("{")) {
                com.mob.mcl.d.b.a().a(str);
                return a(this.n.fromJson(str));
            }
        } catch (Throwable th) {
            com.mob.mcl.d.b.a().a(th);
        }
        return hashMap;
    }

    private void b(long j) {
        long incrementAndGet;
        h hVar = this.o;
        if (hVar != null) {
            if (j == 0) {
                try {
                    incrementAndGet = hVar.c.incrementAndGet();
                } catch (Throwable th) {
                    com.mob.mcl.d.b.a().a(th);
                    return;
                }
            } else {
                incrementAndGet = j;
            }
            com.mob.mcl.c.a aVar = hVar.a;
            if (aVar == null) {
                throw null;
            }
            e eVar = new e();
            synchronized (aVar.e) {
                aVar.e.put(eVar, Long.valueOf(incrementAndGet));
            }
            try {
                OutputStream outputStream = aVar.a.getOutputStream();
                ByteBuffer allocate = ByteBuffer.allocate(17);
                allocate.put((byte) 1);
                allocate.putInt(0);
                allocate.putInt(Constant.MSG_WHAT_READ_PART);
                allocate.putLong(incrementAndGet);
                outputStream.write(allocate.array());
                outputStream.flush();
            } catch (Throwable th2) {
                ((i) aVar.b).a(aVar, th2);
            }
            com.mob.mcl.d.b.a().a("tp sd ty = " + Constant.MSG_WHAT_READ_PART + " , u = " + j + " bo : " + ((String) null));
        }
    }

    private void b(long j, boolean z) {
        if (this.o != null) {
            try {
                String a2 = a(this.c.get());
                HashMap hashMap = new HashMap();
                hashMap.put("repeat", Boolean.valueOf(z));
                String fromHashMap = this.n.fromHashMap(hashMap);
                String b2 = b(a2, fromHashMap);
                int length = b2 != null ? b2.length() : 0;
                h hVar = this.o;
                long incrementAndGet = j == 0 ? hVar.c.incrementAndGet() : j;
                com.mob.mcl.c.a aVar = hVar.a;
                if (aVar == null) {
                    throw null;
                }
                e eVar = new e();
                synchronized (aVar.e) {
                    aVar.e.put(eVar, Long.valueOf(incrementAndGet));
                }
                try {
                    OutputStream outputStream = aVar.a.getOutputStream();
                    ByteBuffer allocate = ByteBuffer.allocate(length + 17);
                    allocate.put((byte) 1);
                    allocate.putInt(length);
                    allocate.putInt(1007);
                    allocate.putLong(incrementAndGet);
                    if (b2 != null) {
                        allocate.put(b2.getBytes(Charset.forName("UTF-8")));
                    }
                    outputStream.write(allocate.array());
                    outputStream.flush();
                } catch (Throwable th) {
                    ((i) aVar.b).a(aVar, th);
                }
                com.mob.mcl.d.b.a().a("tp sd ty = 1007 , u = " + j + " bo : " + fromHashMap);
            } catch (Throwable th2) {
                com.mob.mcl.d.b.a().a(th2);
            }
        }
    }

    private boolean b(HashMap<String, Object> hashMap) {
        try {
            this.l = false;
            HashMap<String, Object> a2 = a(hashMap);
            if (a2.containsKey("domains") && a2.containsKey("uniqueId") && a2.containsKey("uniqueKey")) {
                this.d = (ArrayList) a2.get("domains");
                this.a = ((Long) a2.get("uniqueId")).longValue();
                this.b = (String) a2.get("uniqueKey");
                this.e = a(a2, "tick", this.e);
                this.f = a(a2, "globalSwitch", 0) == 1;
                this.g = a(a2, "connectSwitch", 0) == 1;
                this.h = a(a2, "forwardSwitch", 0) == 1;
                this.i = a(a2, "bindRequestSwitch", 0) == 1;
                if (a2.containsKey("determineDomain")) {
                    String str = (String) a2.get("determineDomain");
                    if (!TextUtils.isEmpty(str)) {
                        if (this.d == null) {
                            this.d = new ArrayList<>();
                        }
                        this.d.remove(str);
                        this.d.add(0, str);
                    }
                }
                if (this.d != null && this.d.size() > 0) {
                    if (!TextUtils.isEmpty(this.b)) {
                        return true;
                    }
                }
            }
        } catch (Throwable th) {
            com.mob.mcl.d.b.a().a(th);
        }
        return false;
    }

    public static i c() {
        if (y == null) {
            synchronized (i.class) {
                if (y == null) {
                    y = new i();
                }
            }
        }
        return y;
    }

    private String d() {
        return this.q + this.r.getPackageName();
    }

    public int a(Bundle bundle) {
        if (this.s == null) {
            return -1;
        }
        if (a(bundle.getString("workId"), bundle.getInt("expire"))) {
            return 1;
        }
        return this.s.messageReceived(bundle) ? 1 : 0;
    }

    public String a(String str, String str2) throws Throwable {
        return Data.AES128Decode(str, Base64.decode(str2, 2));
    }

    public HashMap<String, Object> a(int i, int i2, String str) {
        if (this.o != null) {
            try {
                String a2 = a(this.c.get());
                com.mob.mcl.d.b.a().a("tp sd ty = " + i + " , bo = " + str + " , out = " + i2);
                g gVar = this.o.a(TextUtils.isEmpty(str) ? new g(i, null) : new g(i, b(a2, str))).get(i2, TimeUnit.MILLISECONDS);
                if (gVar != null && gVar.b == 1000) {
                    String a3 = a(a2, gVar.d);
                    gVar.d = a3;
                    return b(a3);
                }
                com.mob.mcl.d.b.a().a(" tp rp : " + gVar);
            } catch (Throwable th) {
                com.mob.mcl.d.b.a().a(th);
            }
        }
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0067, code lost:
    
        if ((r4 != null && r4.d.get()) != false) goto L18;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.util.HashMap<java.lang.String, java.lang.Object> a(java.lang.String r11, java.lang.String r12, int r13) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 307
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.mcl.c.i.a(java.lang.String, java.lang.String, int):java.util.HashMap");
    }

    public void a() {
        if (TextUtils.isEmpty(this.j) || this.k <= 0) {
            this.j = com.mob.mcl.d.d.c();
            this.k = com.mob.mcl.d.d.d();
            if (TextUtils.isEmpty(this.j)) {
                this.j = UUID.randomUUID().toString();
            }
            if (this.k <= 0) {
                this.k = System.currentTimeMillis();
            }
            String str = this.j;
            long j = this.k;
            synchronized (this) {
                this.j = str;
                this.k = j;
                com.mob.mcl.d.d.b(str);
                com.mob.mcl.d.d.a(this.k);
            }
        }
    }

    public void a(int i, BusinessMessageListener businessMessageListener) {
        HashSet<BusinessMessageListener> hashSet;
        try {
            Integer valueOf = Integer.valueOf(i);
            if (businessMessageListener == null) {
                this.t.remove(valueOf);
                return;
            }
            if (this.t.containsKey(valueOf)) {
                hashSet = this.t.get(valueOf);
            } else {
                HashSet<BusinessMessageListener> hashSet2 = new HashSet<>();
                this.t.put(valueOf, hashSet2);
                hashSet = hashSet2;
            }
            hashSet.add(businessMessageListener);
        } catch (Throwable th) {
            com.mob.mcl.d.b.a().a(th);
        }
    }

    public void a(Context context, String str, String str2) {
        this.r = context;
        this.p = str;
        if (!TextUtils.isEmpty(str2)) {
            this.q = str2;
        }
        a();
        ActivityTracker.getInstance(context).addTracker(this);
        this.v.a();
    }

    public void a(BusinessCallBack<Boolean> businessCallBack) {
        boolean f = f();
        if (businessCallBack != null) {
            businessCallBack.callback(Boolean.valueOf(f));
        }
        if (f) {
            return;
        }
        com.mob.mcl.b.a.e.execute(new a());
    }

    public void a(MCLSDK.ELPMessageListener eLPMessageListener) {
        this.s = eLPMessageListener;
    }

    public void a(com.mob.mcl.c.a aVar) {
        com.mob.mcl.d.b.a().a("sessionOpened");
    }

    public void a(com.mob.mcl.c.a aVar, g gVar) {
        try {
            if (TextUtils.isEmpty(gVar.d)) {
                return;
            }
            if (this.c.get() == 0) {
                com.mob.mcl.d.b.a().a("tcp received push msg, but send token is 0");
                return;
            }
            String a2 = a(a(this.c.get()), gVar.d);
            gVar.d = a2;
            if (gVar.b == 9001) {
                com.mob.mcl.d.b.a().a(" tcp msg push msgType: " + gVar.b + " body = " + gVar.d);
                b(gVar.c);
                HashMap<String, Object> b2 = b(gVar.d);
                if (b2.containsKey(AeUtil.ROOT_DATA_PATH_OLD_NAME)) {
                    int a3 = a(b2, "expire", 0);
                    String str = (String) b2.get("workId");
                    String str2 = (String) b2.get(AeUtil.ROOT_DATA_PATH_OLD_NAME);
                    boolean z = a(b2, "needRepeat", 0) == 1;
                    int a4 = a(b2, "type", 0);
                    if (a4 != 1 && a4 != 2) {
                        boolean a5 = a(gVar.c, str, a3, a4, str2);
                        if (z) {
                            b(gVar.c, a5);
                            return;
                        }
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString(AeUtil.ROOT_DATA_PATH_OLD_NAME, str2);
                    bundle.putInt("expire", a3);
                    bundle.putString("workId", str);
                    bundle.putLong("uniqueId", gVar.c);
                    bundle.putInt("msgType", a4);
                    boolean z2 = a(bundle) == 1;
                    if (z) {
                        b(gVar.c, z2);
                        return;
                    }
                    return;
                }
                return;
            }
            if (gVar.b == 9002) {
                String str3 = (String) b(a2).get("domain");
                if (TextUtils.isEmpty(str3)) {
                    return;
                }
                this.l = true;
                a(true, str3, 2, b(), 5000);
                return;
            }
            if (gVar.b == 9004) {
                com.mob.mcl.d.b.a().a(" tp mg ty: " + gVar.b + " bo = " + gVar.d);
                b(gVar.c);
                HashMap<String, Object> b3 = b(gVar.d);
                if (b3.containsKey(AeUtil.ROOT_DATA_PATH_OLD_NAME) && b3.containsKey("targetPackage")) {
                    String str4 = (String) b3.get("targetPackage");
                    String str5 = (String) b3.get(AeUtil.ROOT_DATA_PATH_OLD_NAME);
                    int a6 = a(b3, "logicTimeout", 1000);
                    if (TextUtils.isEmpty(str5) || TextUtils.isEmpty(str4)) {
                        return;
                    }
                    Bundle bundle2 = new Bundle();
                    bundle2.putString(AeUtil.ROOT_DATA_PATH_OLD_NAME, str5);
                    bundle2.putLong("uniqueId", gVar.c);
                    APCMessage a7 = com.mob.mcl.a.b.a().a(9004, bundle2, str4, a6);
                    if (a7 != null && a7.data != null) {
                        a(gVar.c, true);
                    } else {
                        com.mob.mcl.d.b.a().a("apc fw rp mg is null");
                        a(gVar.c, false);
                    }
                }
            }
        } catch (Throwable th) {
            com.mob.mcl.d.b.a().a(th);
        }
    }

    public void a(com.mob.mcl.c.a aVar, Throwable th) {
        com.mob.mcl.d.b.a().a("exceptionCaught : " + th.getMessage());
    }

    public void a(String str) {
        this.v.a(str);
    }

    public synchronized boolean a(int i) {
        if (!i()) {
            return false;
        }
        return a(this.l, this.d.get(0), 0, b(), i);
    }

    public boolean a(int i, int i2) {
        if (i2 >= 4) {
            return false;
        }
        if (a(1002, i, (String) null) != null) {
            return true;
        }
        if (i2 == 0 || i2 == 1) {
            a(1000, i2 + 1);
            return false;
        }
        a(PathInterpolatorCompat.MAX_NUM_POINTS, i2 + 1);
        return false;
    }

    public boolean a(long j, String str, int i, int i2, String str2) {
        try {
        } catch (Throwable th) {
            com.mob.mcl.d.b.a().a(th);
        }
        if (a(str, i)) {
            return true;
        }
        HashMap fromJson = this.n.fromJson(str2);
        fromJson.put("uniqueId", Long.valueOf(j));
        String fromHashMap = this.n.fromHashMap(fromJson);
        Integer valueOf = Integer.valueOf(i2);
        if (this.t.containsKey(valueOf)) {
            Iterator<BusinessMessageListener> it = this.t.get(valueOf).iterator();
            while (it.hasNext()) {
                UIHandler.sendEmptyMessage(0, new c(this, it.next(), valueOf, str, fromHashMap));
            }
        }
        return false;
    }

    public void b(BusinessCallBack<Boolean> businessCallBack) {
        com.mob.mcl.b.a.e.execute(new b(businessCallBack));
    }

    public String e() {
        return String.format("%16s", Integer.valueOf(Math.abs(Arrays.hashCode(new Object[]{this.p, d()})))).replaceAll(SQLBuilder.BLANK, AmapLoc.RESULT_TYPE_GPS).substring(0, 16);
    }

    public boolean f() {
        h hVar = this.o;
        if (hVar == null) {
            return false;
        }
        com.mob.mcl.c.a aVar = hVar.a;
        return (aVar != null && aVar.d.get()) && this.c.get() != 0;
    }

    public boolean g() {
        return i() && this.h;
    }

    public boolean h() {
        return (this.f && this.g && !this.u) ? false : true;
    }

    public boolean i() {
        ArrayList<String> arrayList;
        return this.f && this.g && !this.u && (arrayList = this.d) != null && arrayList.size() > 0 && !TextUtils.isEmpty(this.b);
    }

    public void j() {
        if (com.mob.mcl.b.a.e()) {
            if (TextUtils.isEmpty(this.q) || this.r == null) {
                com.mob.mcl.d.b.a().a("mcl has not been initialized");
                return;
            }
            String b2 = com.mob.mcl.d.d.b();
            if (!TextUtils.isEmpty(b2)) {
                HashMap<String, Object> fromJson = this.n.fromJson(b2);
                if (fromJson.containsKey("requestTimes") && ((Long) fromJson.get("requestTimes")).longValue() + DateUtils.MILLIS_PER_DAY > System.currentTimeMillis() && c().b(fromJson) && com.mob.mcl.d.d.e()) {
                    com.mob.mcl.d.b.a().a(" cf cc : " + b2);
                    return;
                }
            }
            ArrayList<KVPair<String>> arrayList = new ArrayList<>();
            NetworkHelper.NetworkTimeOut networkTimeOut = new NetworkHelper.NetworkTimeOut();
            networkTimeOut.readTimout = 10000;
            networkTimeOut.connectionTimeout = 5000;
            try {
                ArrayList<KVPair<String>> arrayList2 = new ArrayList<>();
                arrayList2.add(new KVPair<>("appkey", this.p));
                arrayList2.add(new KVPair<>("pushId", d()));
                String checkRequestUrl = MobSDK.checkRequestUrl("http://m.mpl.dutils.com/tcp/config/init");
                String httpPost = this.m.httpPost(checkRequestUrl, arrayList2, (KVPair<String>) null, arrayList, networkTimeOut);
                com.mob.mcl.d.b.a().a("tp cf url : " + checkRequestUrl + " -> rp : " + httpPost);
                HashMap<String, Object> fromJson2 = this.n.fromJson(httpPost);
                fromJson2.put("requestTimes", Long.valueOf(System.currentTimeMillis()));
                if (c().b(fromJson2)) {
                    com.mob.mcl.d.d.a(true);
                    com.mob.mcl.d.d.a(this.n.fromHashMap(fromJson2));
                }
            } catch (Throwable th) {
                com.mob.mcl.d.b.a().a(th.getMessage());
            }
        }
    }

    public boolean k() {
        return a(1003, 10000, b()) != null;
    }

    @Override // com.mob.tools.utils.ActivityTracker.Tracker
    public void onCreated(Activity activity, Bundle bundle) {
    }

    @Override // com.mob.tools.utils.ActivityTracker.Tracker
    public void onDestroyed(Activity activity) {
    }

    @Override // com.mob.tools.utils.ActivityTracker.Tracker
    public void onPaused(Activity activity) {
    }

    @Override // com.mob.tools.utils.ActivityTracker.Tracker
    public void onResumed(Activity activity) {
        if (this.w == 0) {
            this.w = SystemClock.elapsedRealtime();
            if (!f()) {
                com.mob.mcl.b.a.e.execute(new j(this));
            }
        }
        this.x = activity;
    }

    @Override // com.mob.tools.utils.ActivityTracker.Tracker
    public void onSaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override // com.mob.tools.utils.ActivityTracker.Tracker
    public void onStarted(Activity activity) {
    }

    @Override // com.mob.tools.utils.ActivityTracker.Tracker
    public void onStopped(Activity activity) {
        Activity activity2 = this.x;
        if (activity2 == null || activity2 == activity) {
            this.w = 0L;
            this.x = null;
            if (f()) {
                return;
            }
            com.mob.mcl.b.a.e.execute(new j(this));
        }
    }
}
