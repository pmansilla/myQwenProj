package com.mob.mcl.b;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.graphics.drawable.PathInterpolatorCompat;
import android.text.TextUtils;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.mob.MobSDK;
import com.mob.apc.MobAPC;
import com.mob.mcl.BusinessMessageListener;
import com.mob.mcl.MCLSDK;
import com.mob.mcl.c.d;
import com.mob.mcl.c.i;
import com.mob.tools.network.KVPair;
import com.mob.tools.network.NetworkHelper;
import com.mob.tools.network.StringPart;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ReflectHelper;
import com.mob.tools.utils.UIHandler;
import com.wx.wheelview.common.WheelConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.text.Typography;

/* compiled from: MCLink.java */
/* loaded from: classes.dex */
public class a implements MobAPC.MobAPCMessageListener {
    public static final ExecutorService e = Executors.newSingleThreadExecutor();
    public static final AtomicBoolean f = new AtomicBoolean(false);
    private NetworkHelper c = new NetworkHelper();
    private HashSet<String> b = new HashSet<>();
    private Hashon d = new Hashon();
    private Context a = MobSDK.getContext();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MCLink.java */
    /* renamed from: com.mob.mcl.b.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public class C0059a implements Handler.Callback {
        final /* synthetic */ int a;

        C0059a(int i) {
            this.a = i;
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            ExecutorService executorService = a.e;
            a aVar = a.this;
            int i = this.a;
            if (aVar == null) {
                throw null;
            }
            executorService.execute(new com.mob.mcl.b.b(aVar, i));
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MCLink.java */
    /* loaded from: classes.dex */
    public class b implements Runnable {
        b(a aVar) {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                if (i.c().f()) {
                    return;
                }
                if (!i.c().i()) {
                    i.c().j();
                }
                i.c().a(5000);
                if (i.c().h) {
                    return;
                }
                a.d();
            } catch (Throwable unused) {
            }
        }
    }

    private String a(String str, String str2, ArrayList<KVPair<String>> arrayList, StringPart stringPart) {
        HashMap hashMap = new HashMap();
        hashMap.put("type", str);
        hashMap.put(FileDownloadModel.URL, str2);
        HashMap hashMap2 = new HashMap();
        if (arrayList != null) {
            Iterator<KVPair<String>> it = arrayList.iterator();
            while (it.hasNext()) {
                KVPair<String> next = it.next();
                hashMap2.put(next.name, next.value);
            }
        }
        hashMap.put("headers", hashMap2);
        if (stringPart != null) {
            hashMap.put("body", stringPart.toString());
        }
        return this.d.fromHashMap(hashMap);
    }

    private void a(int i, int i2) {
        UIHandler.sendEmptyMessageDelayed(0, i2 * 1000, new C0059a(i));
    }

    public static void a(int i, BusinessMessageListener businessMessageListener) {
        i.c().a(i, businessMessageListener);
    }

    public static void a(MCLSDK.ELPMessageListener eLPMessageListener) {
        i.c().a(eLPMessageListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(a aVar, int i) {
        if (aVar == null) {
            throw null;
        }
        if (i < 6) {
            if ((!e() && com.mob.mcl.a.b.a().b()) || a(5000) || i.c().h()) {
                return;
            }
            if (i == 0) {
                aVar.a(i + 1, 10);
                return;
            }
            if (i == 1) {
                aVar.a(i + 1, 30);
                return;
            }
            if (i == 2) {
                aVar.a(i + 1, 60);
            } else if (i == 3) {
                aVar.a(i + 1, 180);
            } else if (i == 4) {
                aVar.a(i + 1, WheelConstants.WHEEL_SCROLL_DELAY_DURATION);
            }
        }
    }

    private static boolean a(int i) {
        try {
        } finally {
            try {
                return false;
            } finally {
            }
        }
        if (f.getAndSet(true)) {
            return false;
        }
        if (e()) {
            i.c().j();
            if (!i.c().f) {
                f.set(false);
                return false;
            }
            if (!i.c().i()) {
                com.mob.mcl.d.b.a().b("tcp register isTcpAvailable false");
            } else {
                if (i.c().a(i)) {
                    d.a();
                    if (!i.c().h) {
                        d();
                    }
                    f.set(false);
                    return true;
                }
                boolean z = i.c().l;
                com.mob.mcl.d.b.a().b("tcp register failed");
            }
        }
        d();
        return false;
    }

    public static boolean a(String str, long j) {
        i c = i.c();
        synchronized (c) {
            c.j = str;
            c.k = j;
            com.mob.mcl.d.d.b(str);
            com.mob.mcl.d.d.a(c.k);
        }
        try {
            if (!i.c().i()) {
                return false;
            }
            if (!i.c().f()) {
                a(PathInterpolatorCompat.MAX_NUM_POINTS);
            }
            if (i.c().f()) {
                return i.c().k();
            }
            return false;
        } catch (Throwable th) {
            com.mob.mcl.d.b.a().a(th);
            return false;
        }
    }

    public static long b() {
        i.c().a();
        return i.c().k;
    }

    public static String c() {
        i.c().a();
        return i.c().j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void d() {
        if (com.mob.mcl.a.b.a().b()) {
            return;
        }
        com.mob.mcl.a.b.a().c();
    }

    public static boolean e() {
        List<ActivityManager.RunningAppProcessInfo> list;
        String str = null;
        try {
            list = (List) ReflectHelper.invokeInstanceMethod((ActivityManager) MobSDK.getContext().getSystemService("activity"), "getRunningAppProcesses", new Object[0]);
        } catch (Throwable th) {
            com.mob.mcl.d.b.a().a(th);
        }
        if (list == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : list) {
            try {
                if (runningAppProcessInfo.pid == Process.myPid()) {
                    str = runningAppProcessInfo.processName;
                }
            } catch (Throwable th2) {
                com.mob.mcl.d.b.a().a(th2);
            }
        }
        return str != null && str.equalsIgnoreCase(MobSDK.getContext().getPackageName());
    }

    private void f() {
        try {
        } finally {
            try {
            } finally {
            }
        }
        if (f.getAndSet(true)) {
            return;
        }
        if (e()) {
            if (!i.c().f) {
                f.set(false);
            } else if (!i.c().i()) {
                e.execute(new b(this));
            } else {
                if (i.c().a(PathInterpolatorCompat.MAX_NUM_POINTS)) {
                    d.a();
                    f.set(false);
                    return;
                }
                com.mob.mcl.d.b.a().b("tcp register failed");
            }
        }
    }

    public KVPair<String> a(boolean z, String str, ArrayList<KVPair<String>> arrayList, ArrayList<KVPair<String>> arrayList2, NetworkHelper.NetworkTimeOut networkTimeOut) throws Throwable {
        HashSet<String> hashSet;
        if (!z && (hashSet = this.b) != null) {
            Iterator<String> it = hashSet.iterator();
            while (it.hasNext()) {
                if (str.equals(it.next())) {
                    return new KVPair<>("http", this.c.httpGet(str, arrayList, arrayList2, networkTimeOut));
                }
            }
        }
        if (arrayList != null) {
            StringBuilder sb = new StringBuilder();
            Iterator<KVPair<String>> it2 = arrayList.iterator();
            while (it2.hasNext()) {
                KVPair<String> next = it2.next();
                String urlEncode = Data.urlEncode(next.name, "utf-8");
                String str2 = next.value;
                String urlEncode2 = str2 != null ? Data.urlEncode(str2, "utf-8") : "";
                if (sb.length() > 0) {
                    sb.append(Typography.amp);
                }
                sb.append(urlEncode);
                sb.append('=');
                sb.append(urlEncode2);
            }
            String sb2 = sb.toString();
            if (sb2.length() > 0) {
                str = str + "?" + sb2;
            }
            arrayList = null;
        }
        if (i.c().g()) {
            if (!i.c().f()) {
                f();
            }
            if (i.c().f()) {
                HashMap<String, Object> a = i.c().a(1004, networkTimeOut.readTimout, a("GET", str, arrayList2, null));
                if (a != null) {
                    return new KVPair<>("tcp", this.d.fromHashMap(a));
                }
                if (!z) {
                    return new KVPair<>("http", this.c.httpGet(str, arrayList, arrayList2, networkTimeOut));
                }
            }
        }
        if (z) {
            return null;
        }
        if (!com.mob.mcl.a.b.a().b()) {
            a(5, 0);
        }
        String a2 = com.mob.mcl.a.b.a().a("GET", str, arrayList2, null, 0, networkTimeOut);
        return !TextUtils.isEmpty(a2) ? new KVPair<>("apc-tcp", a2) : new KVPair<>("http", this.c.httpGet(str, arrayList2, null, networkTimeOut));
    }

    public void a(Context context, String str, String str2) {
        com.mob.mcl.d.b.a().b("mcl init");
        if (context != null) {
            this.a = context.getApplicationContext();
        }
        com.mob.mcl.a.b.a().a(this.a, this);
        i.c().a(this.a, str, str2);
        com.mob.mcl.d.d.a(false);
        a(0, 0);
    }

    public void a(String str) {
        this.b.add(str);
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x0090  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0095  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void a(boolean r14, java.lang.String r15, java.util.ArrayList<com.mob.tools.network.KVPair<java.lang.String>> r16, com.mob.tools.network.StringPart r17, int r18, com.mob.tools.network.HttpResponseCallback r19, com.mob.tools.network.NetworkHelper.NetworkTimeOut r20) throws java.lang.Throwable {
        /*
            r13 = this;
            r0 = r13
            r8 = r15
            r9 = r19
            if (r14 != 0) goto L31
            java.util.HashSet<java.lang.String> r2 = r0.b
            if (r2 == 0) goto L31
            java.util.Iterator r2 = r2.iterator()
        Le:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L31
            java.lang.Object r3 = r2.next()
            java.lang.String r3 = (java.lang.String) r3
            boolean r3 = r15.equals(r3)
            if (r3 == 0) goto Le
            com.mob.tools.network.NetworkHelper r1 = r0.c
            r2 = r15
            r3 = r16
            r4 = r17
            r5 = r18
            r6 = r19
            r7 = r20
            r1.rawPost(r2, r3, r4, r5, r6, r7)
            return
        L31:
            com.mob.mcl.c.i r2 = com.mob.mcl.c.i.c()
            boolean r2 = r2.g()
            if (r2 == 0) goto L88
            com.mob.mcl.c.i r2 = com.mob.mcl.c.i.c()
            boolean r2 = r2.f()
            if (r2 != 0) goto L48
            r13.f()
        L48:
            com.mob.mcl.c.i r2 = com.mob.mcl.c.i.c()
            boolean r2 = r2.f()
            if (r2 == 0) goto L88
            com.mob.mcl.c.i r2 = com.mob.mcl.c.i.c()
            r10 = r20
            int r3 = r10.readTimout
            java.lang.String r4 = "POST"
            r11 = r16
            r12 = r17
            java.lang.String r4 = r13.a(r4, r15, r11, r12)
            r5 = 1004(0x3ec, float:1.407E-42)
            java.util.HashMap r2 = r2.a(r5, r3, r4)
            if (r2 == 0) goto L75
            com.mob.mcl.b.c r1 = new com.mob.mcl.b.c
            r1.<init>(r2)
            r9.onResponse(r1)
            return
        L75:
            if (r14 != 0) goto L8e
            com.mob.tools.network.NetworkHelper r1 = r0.c
            r2 = r15
            r3 = r16
            r4 = r17
            r5 = r18
            r6 = r19
            r7 = r20
            r1.rawPost(r2, r3, r4, r5, r6, r7)
            return
        L88:
            r11 = r16
            r12 = r17
            r10 = r20
        L8e:
            if (r14 == 0) goto L95
            r1 = 0
            r9.onResponse(r1)
            goto Ldd
        L95:
            com.mob.mcl.a.b r1 = com.mob.mcl.a.b.a()
            boolean r1 = r1.b()
            if (r1 != 0) goto La4
            r1 = 5
            r2 = 0
            r13.a(r1, r2)
        La4:
            com.mob.mcl.a.b r1 = com.mob.mcl.a.b.a()
            java.lang.String r2 = "POST"
            r3 = r15
            r4 = r16
            r5 = r17
            r6 = r18
            r7 = r20
            java.lang.String r1 = r1.a(r2, r3, r4, r5, r6, r7)
            boolean r2 = android.text.TextUtils.isEmpty(r1)
            if (r2 != 0) goto Lcd
            com.mob.mcl.b.c r2 = new com.mob.mcl.b.c
            com.mob.tools.utils.Hashon r3 = r0.d
            java.util.HashMap r1 = r3.fromJson(r1)
            r3 = 1
            r2.<init>(r1, r3)
            r9.onResponse(r2)
            goto Ldd
        Lcd:
            com.mob.tools.network.NetworkHelper r1 = r0.c
            r2 = r15
            r3 = r16
            r4 = r17
            r5 = r18
            r6 = r19
            r7 = r20
            r1.rawPost(r2, r3, r4, r5, r6, r7)
        Ldd:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.mcl.b.a.a(boolean, java.lang.String, java.util.ArrayList, com.mob.tools.network.StringPart, int, com.mob.tools.network.HttpResponseCallback, com.mob.tools.network.NetworkHelper$NetworkTimeOut):void");
    }

    public void b(String str) {
        this.b.remove(str);
    }

    /* JADX WARN: Code restructure failed: missing block: B:59:0x0095, code lost:
    
        if (r2 == 9004) goto L17;
     */
    /* JADX WARN: Removed duplicated region for block: B:11:0x009b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // com.mob.apc.MobAPC.MobAPCMessageListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.mob.apc.APCMessage onMessageReceive(java.lang.String r11, com.mob.apc.APCMessage r12, long r13) {
        /*
            Method dump skipped, instructions count: 488
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.mcl.b.a.onMessageReceive(java.lang.String, com.mob.apc.APCMessage, long):com.mob.apc.APCMessage");
    }
}
