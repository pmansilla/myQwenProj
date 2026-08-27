package com.loc;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.text.TextUtils;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.location.common.model.AmapLoc;
import com.autonavi.aps.amapapi.model.AMapLocationServer;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.tencent.bugly.BuglyStrategy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import no.nordicsemi.android.dfu.internal.scanner.BootloaderScanner;

/* compiled from: ApsManager.java */
/* loaded from: classes.dex */
public final class n {
    static boolean g = false;
    Context e;
    private boolean t = false;
    private boolean u = false;
    String a = null;
    b b = null;
    private long v = 0;
    private long w = 0;
    private AMapLocationServer x = null;
    AMapLocation c = null;
    private long y = 0;
    private int z = 0;
    a d = null;
    private r A = null;
    cs f = null;
    HashMap<Messenger, Long> h = new HashMap<>();
    ey i = null;
    long j = 0;
    long k = 0;
    String l = null;
    private boolean B = true;
    private String C = "";
    AMapLocationClientOption m = null;
    AMapLocationClientOption n = new AMapLocationClientOption();
    ServerSocket o = null;
    boolean p = false;
    Socket q = null;
    boolean r = false;
    c s = null;
    private final int D = 5000;
    private String E = "jsonp1";

    /* compiled from: ApsManager.java */
    /* loaded from: classes.dex */
    public class a extends Handler {
        public a(Looper looper) {
            super(looper);
        }

        /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
            jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
            	at jadx.core.dex.visitors.regions.RegionMaker.calcSwitchOut(RegionMaker.java:923)
            	at jadx.core.dex.visitors.regions.RegionMaker.processSwitch(RegionMaker.java:797)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:157)
            	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:91)
            	at jadx.core.dex.visitors.regions.RegionMaker.processExcHandler(RegionMaker.java:1110)
            	at jadx.core.dex.visitors.regions.RegionMaker.processTryCatchBlocks(RegionMaker.java:1046)
            	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:55)
            */
        /* JADX WARN: Removed duplicated region for block: B:61:0x003c A[Catch: Throwable -> 0x0055, TryCatch #3 {Throwable -> 0x0055, blocks: (B:49:0x000a, B:51:0x0010, B:53:0x0020, B:54:0x0028, B:56:0x002e, B:61:0x003c, B:63:0x0040), top: B:48:0x000a }] */
        @Override // android.os.Handler
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void handleMessage(android.os.Message r8) {
            /*
                Method dump skipped, instructions count: 310
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.loc.n.a.handleMessage(android.os.Message):void");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ApsManager.java */
    /* loaded from: classes.dex */
    public class b extends HandlerThread {
        public b(String str) {
            super(str);
        }

        @Override // android.os.HandlerThread
        protected final void onLooperPrepared() {
            try {
                try {
                    n.this.A = new r(n.this.e);
                } catch (Throwable th) {
                    es.a(th, "APSManager$ActionThread", "init 2");
                }
                n.this.f = new cs();
                super.onLooperPrepared();
            } catch (Throwable th2) {
                es.a(th2, "APSManager$ActionThread", "onLooperPrepared");
            }
        }

        @Override // android.os.HandlerThread, java.lang.Thread, java.lang.Runnable
        public final void run() {
            try {
                super.run();
            } catch (Throwable th) {
                es.a(th, "APSManager$ActionThread", "run");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ApsManager.java */
    /* loaded from: classes.dex */
    public class c extends Thread {
        c() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public final void run() {
            try {
                if (!n.this.p) {
                    n.this.p = true;
                    n.this.o = new ServerSocket(43689);
                }
                while (n.this.p && n.this.o != null) {
                    n.this.q = n.this.o.accept();
                    n.a(n.this, n.this.q);
                }
            } catch (Throwable th) {
                es.a(th, "ApsServiceCore", "run");
            }
            super.run();
        }
    }

    public n(Context context) {
        this.e = null;
        this.e = context;
    }

    private static AMapLocationServer a(int i, String str) {
        try {
            AMapLocationServer aMapLocationServer = new AMapLocationServer("");
            aMapLocationServer.setErrorCode(i);
            aMapLocationServer.setLocationDetail(str);
            return aMapLocationServer;
        } catch (Throwable th) {
            es.a(th, "ApsServiceCore", "newInstanceAMapLoc");
            return null;
        }
    }

    static /* synthetic */ AMapLocationServer a(String str) {
        return a(10, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Bundle bundle) {
        try {
            if (this.t) {
                return;
            }
            es.a(this.e);
            if (bundle != null) {
                this.n = es.a(bundle.getBundle("optBundle"));
            }
            this.f.a(this.e);
            this.f.a();
            a(this.n);
            this.f.b();
            this.t = true;
            this.B = true;
            this.C = "";
        } catch (Throwable th) {
            this.B = false;
            this.C = th.getMessage();
            es.a(th, "ApsServiceCore", "init");
        }
    }

    private void a(Messenger messenger) {
        try {
            cs.b(this.e);
            if (er.k()) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("ngpsAble", er.m());
                a(messenger, 7, bundle);
                er.l();
            }
            if (er.t()) {
                Bundle bundle2 = new Bundle();
                bundle2.putBoolean("installMockApp", true);
                a(messenger, 9, bundle2);
            }
        } catch (Throwable th) {
            es.a(th, "ApsServiceCore", "initAuth");
        }
    }

    private static void a(Messenger messenger, int i, Bundle bundle) {
        if (messenger != null) {
            try {
                Message obtain = Message.obtain();
                obtain.setData(bundle);
                obtain.what = i;
                messenger.send(obtain);
            } catch (Throwable th) {
                es.a(th, "ApsServiceCore", "sendMessage");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Messenger messenger, AMapLocation aMapLocation, String str, long j) {
        Bundle bundle = new Bundle();
        bundle.setClassLoader(AMapLocation.class.getClassLoader());
        bundle.putParcelable("loc", aMapLocation);
        bundle.putString("nb", str);
        bundle.putLong("netUseTime", j);
        this.h.put(messenger, Long.valueOf(fa.c()));
        a(messenger, 1, bundle);
    }

    private void a(AMapLocationClientOption aMapLocationClientOption) {
        try {
            if (this.f != null) {
                this.f.a(aMapLocationClientOption);
            }
            if (aMapLocationClientOption != null) {
                g = aMapLocationClientOption.isKillProcess();
                if (this.m != null && (aMapLocationClientOption.isOffset() != this.m.isOffset() || aMapLocationClientOption.isNeedAddress() != this.m.isNeedAddress() || aMapLocationClientOption.isLocationCacheEnable() != this.m.isLocationCacheEnable() || this.m.getGeoLanguage() != aMapLocationClientOption.getGeoLanguage())) {
                    this.w = 0L;
                    this.c = null;
                }
                this.m = aMapLocationClientOption;
            }
        } catch (Throwable th) {
            es.a(th, "ApsServiceCore", "setExtra");
        }
    }

    static /* synthetic */ void a(n nVar) {
        try {
            if (nVar.z < er.b()) {
                nVar.z++;
                nVar.f.e();
                nVar.d.sendEmptyMessageDelayed(4, 2000L);
            }
        } catch (Throwable th) {
            es.a(th, "ApsServiceCore", "doGpsFusion");
        }
    }

    static /* synthetic */ void a(n nVar, Messenger messenger) {
        try {
            nVar.a(messenger);
            er.e(nVar.e);
            try {
                nVar.f.h();
            } catch (Throwable unused) {
            }
        } catch (Throwable th) {
            es.a(th, "ApsServiceCore", "doCallOtherSer");
        }
    }

    static /* synthetic */ void a(n nVar, Messenger messenger, Bundle bundle) {
        if (bundle != null) {
            try {
                if (bundle.isEmpty() || nVar.u) {
                    return;
                }
                nVar.u = true;
                nVar.a(messenger);
                er.e(nVar.e);
                try {
                    nVar.f.g();
                } catch (Throwable unused) {
                }
                nVar.d();
                if (er.a(nVar.y) && AmapLoc.RESULT_TYPE_WIFI_ONLY.equals(bundle.getString("isCacheLoc"))) {
                    nVar.y = fa.c();
                    nVar.f.e();
                }
                nVar.f();
            } catch (Throwable th) {
                es.a(th, "ApsServiceCore", "doInitAuth");
            }
        }
    }

    static /* synthetic */ void a(n nVar, Socket socket) {
        BufferedReader bufferedReader;
        String str;
        String str2;
        if (socket == null) {
            return;
        }
        try {
            int i = es.f;
            String str3 = null;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                try {
                    try {
                        nVar.a(bufferedReader);
                        String e = nVar.e();
                        es.f = i;
                        try {
                            try {
                                nVar.b(e);
                                try {
                                    bufferedReader.close();
                                    socket.close();
                                } catch (Throwable th) {
                                    es.a(th, "ApsServiceCore", "invokeSocketLocation part3");
                                }
                            } catch (Throwable th2) {
                                try {
                                    bufferedReader.close();
                                    socket.close();
                                } catch (Throwable th3) {
                                    es.a(th3, "ApsServiceCore", "invokeSocketLocation part3");
                                }
                                throw th2;
                            }
                        } catch (Throwable th4) {
                            es.a(th4, "ApsServiceCore", "invokeSocketLocation part2");
                            try {
                                bufferedReader.close();
                                socket.close();
                            } catch (Throwable th5) {
                                es.a(th5, "ApsServiceCore", "invokeSocketLocation part3");
                            }
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        String str4 = nVar.E + "&&" + nVar.E + "({'package':'" + nVar.a + "','error_code':1,'error':'params error'})";
                        try {
                            es.a(th, "ApsServiceCore", "invokeSocketLocation");
                            es.f = i;
                            try {
                                try {
                                    nVar.b(str4);
                                    try {
                                        bufferedReader.close();
                                        socket.close();
                                    } catch (Throwable th7) {
                                        es.a(th7, "ApsServiceCore", "invokeSocketLocation part3");
                                    }
                                } catch (Throwable th8) {
                                    try {
                                        bufferedReader.close();
                                        socket.close();
                                    } catch (Throwable th9) {
                                        es.a(th9, "ApsServiceCore", "invokeSocketLocation part3");
                                    }
                                    throw th8;
                                }
                            } catch (Throwable th10) {
                                es.a(th10, "ApsServiceCore", "invokeSocketLocation part2");
                                try {
                                    bufferedReader.close();
                                    socket.close();
                                } catch (Throwable th11) {
                                    es.a(th11, "ApsServiceCore", "invokeSocketLocation part3");
                                }
                            }
                        } catch (Throwable th12) {
                            th = th12;
                            str3 = str4;
                            es.f = i;
                            try {
                                try {
                                    nVar.b(str3);
                                    try {
                                        bufferedReader.close();
                                        socket.close();
                                    } catch (Throwable th13) {
                                        th = th13;
                                        str = "ApsServiceCore";
                                        str2 = "invokeSocketLocation part3";
                                        es.a(th, str, str2);
                                        throw th;
                                    }
                                } catch (Throwable th14) {
                                    try {
                                        bufferedReader.close();
                                        socket.close();
                                    } catch (Throwable th15) {
                                        es.a(th15, "ApsServiceCore", "invokeSocketLocation part3");
                                    }
                                    throw th14;
                                }
                            } catch (Throwable th16) {
                                es.a(th16, "ApsServiceCore", "invokeSocketLocation part2");
                                try {
                                    bufferedReader.close();
                                    socket.close();
                                } catch (Throwable th17) {
                                    th = th17;
                                    str = "ApsServiceCore";
                                    str2 = "invokeSocketLocation part3";
                                    es.a(th, str, str2);
                                    throw th;
                                }
                                throw th;
                            }
                            throw th;
                        }
                    }
                } catch (Throwable th18) {
                    th = th18;
                    es.f = i;
                    nVar.b(str3);
                    bufferedReader.close();
                    socket.close();
                    throw th;
                }
            } catch (Throwable th19) {
                th = th19;
                bufferedReader = null;
                es.f = i;
                nVar.b(str3);
                bufferedReader.close();
                socket.close();
                throw th;
            }
        } catch (Throwable th20) {
            es.a(th20, "ApsServiceCore", "invokeSocketLocation part4");
        }
    }

    private void a(BufferedReader bufferedReader) throws Exception {
        String[] split;
        String[] split2;
        String[] split3;
        String readLine = bufferedReader.readLine();
        int i = BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH;
        if (readLine != null && readLine.length() > 0 && (split = readLine.split(SQLBuilder.BLANK)) != null && split.length > 1 && (split2 = split[1].split("\\?")) != null && split2.length > 1 && (split3 = split2[1].split("&")) != null && split3.length > 0) {
            int i2 = BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH;
            for (String str : split3) {
                String[] split4 = str.split("=");
                if (split4 != null && split4.length > 1) {
                    if ("to".equals(split4[0])) {
                        i2 = fa.g(split4[1]);
                    }
                    if ("callback".equals(split4[0])) {
                        this.E = split4[1];
                    }
                }
            }
            i = i2;
        }
        es.f = i;
    }

    private AMapLocationClientOption b(Bundle bundle) {
        AMapLocationClientOption a2 = es.a(bundle.getBundle("optBundle"));
        a(a2);
        try {
            String string = bundle.getString("d");
            if (!TextUtils.isEmpty(string)) {
                x.a(string);
            }
        } catch (Throwable th) {
            try {
                es.a(th, "APSManager", "doLocation setUmidToken");
            } catch (Throwable th2) {
                es.a(th2, "APSManager", "parseBundle");
            }
        }
        return a2;
    }

    static /* synthetic */ void b(n nVar) {
        cs csVar;
        try {
            if (!er.e()) {
                if (!fa.e(nVar.e)) {
                    csVar = nVar.f;
                }
                nVar.d.sendEmptyMessageDelayed(5, er.d() * 1000);
            }
            csVar = nVar.f;
            csVar.e();
            nVar.d.sendEmptyMessageDelayed(5, er.d() * 1000);
        } catch (Throwable th) {
            es.a(th, "ApsServiceCore", "doOffFusion");
        }
    }

    static /* synthetic */ void b(n nVar, Messenger messenger) {
        nVar.h.remove(messenger);
    }

    static /* synthetic */ void b(n nVar, Messenger messenger, Bundle bundle) {
        String str;
        AMapLocation aMapLocation;
        if (bundle != null) {
            try {
                if (bundle.isEmpty()) {
                    return;
                }
                AMapLocationClientOption b2 = nVar.b(bundle);
                if (nVar.h.containsKey(messenger) && !b2.isOnceLocation()) {
                    if (fa.c() - nVar.h.get(messenger).longValue() < 800) {
                        return;
                    }
                }
                AMapLocation aMapLocation2 = null;
                if (!nVar.B) {
                    nVar.x = a(9, "init error : " + nVar.C + "#0901");
                    nVar.a(messenger, nVar.x, nVar.x.l(), 0L);
                    ey.a((String) null, 2091);
                    return;
                }
                long c2 = fa.c();
                if (!fa.a(nVar.x) || c2 - nVar.w >= 600) {
                    ex exVar = new ex();
                    exVar.a(fa.c());
                    try {
                        nVar.x = nVar.f.d();
                        r4 = (nVar.x.getLocationType() == 6 || nVar.x.getLocationType() == 5) ? nVar.x.k() : 0L;
                        exVar.a(nVar.x);
                        nVar.x = nVar.f.a(nVar.x);
                    } catch (Throwable th) {
                        ey.a((String) null, 2081);
                        nVar.x = a(8, "loc error : " + th.getMessage() + "#0801");
                        es.a(th, "ApsServiceCore", "run part2");
                    }
                    long j = r4;
                    exVar.b(fa.c());
                    if (fa.a(nVar.x)) {
                        nVar.w = fa.c();
                    }
                    if (nVar.x == null) {
                        nVar.x = a(8, "loc is null#0801");
                    }
                    if (nVar.x != null) {
                        String l = nVar.x.l();
                        aMapLocation2 = nVar.x.m9clone();
                        str = l;
                    } else {
                        str = null;
                    }
                    try {
                        aMapLocation = (!b2.isLocationCacheEnable() || nVar.A == null) ? aMapLocation2 : nVar.A.a(aMapLocation2, str, b2.getLastLocationLifeCycle());
                    } catch (Throwable th2) {
                        es.a(th2, "ApsServiceCore", "fixLastLocation");
                        aMapLocation = aMapLocation2;
                    }
                    nVar.a(messenger, aMapLocation, str, j);
                    ey.a(nVar.e, exVar);
                } else {
                    nVar.a(messenger, nVar.x, nVar.x.l(), 0L);
                }
                nVar.a(messenger);
                if (er.u()) {
                    nVar.d();
                }
                if (er.a(nVar.y) && nVar.x != null && (nVar.x.getLocationType() == 2 || nVar.x.getLocationType() == 4 || nVar.x.getLocationType() == 9)) {
                    nVar.y = fa.c();
                    nVar.f.e();
                }
                nVar.f();
            } catch (Throwable th3) {
                es.a(th3, "ApsServiceCore", "doLocation");
            }
        }
    }

    private void b(String str) throws UnsupportedEncodingException, IOException {
        PrintStream printStream = new PrintStream(this.q.getOutputStream(), true, "UTF-8");
        printStream.println("HTTP/1.0 200 OK");
        printStream.println("Content-Length:" + str.getBytes("UTF-8").length);
        printStream.println();
        printStream.println(str);
        printStream.close();
    }

    public static void c() {
        g = false;
    }

    static /* synthetic */ void c(n nVar) {
        try {
            if (er.a(nVar.e, nVar.v)) {
                nVar.v = fa.b();
                nVar.f.e();
            }
        } catch (Throwable th) {
            es.a(th, "ApsServiceCore", "doNGps");
        }
    }

    private void d() {
        try {
            this.d.removeMessages(4);
            if (er.a()) {
                this.d.sendEmptyMessage(4);
            }
            this.d.removeMessages(5);
            if (!er.c() || er.d() <= 2) {
                return;
            }
            this.d.sendEmptyMessage(5);
        } catch (Throwable th) {
            es.a(th, "ApsServiceCore", "checkConfig");
        }
    }

    private String e() {
        StringBuilder sb;
        String str;
        long currentTimeMillis = System.currentTimeMillis();
        if (fa.e(this.e)) {
            sb = new StringBuilder();
            sb.append(this.E);
            sb.append("&&");
            sb.append(this.E);
            sb.append("({'package':'");
            sb.append(this.a);
            str = "','error_code':36,'error':'app is background'})";
        } else {
            if (this.x == null || currentTimeMillis - this.x.getTime() > BootloaderScanner.TIMEOUT) {
                try {
                    this.x = this.f.d();
                } catch (Throwable th) {
                    es.a(th, "ApsServiceCore", "getSocketLocResult");
                }
            }
            if (this.x == null) {
                sb = new StringBuilder();
                sb.append(this.E);
                sb.append("&&");
                sb.append(this.E);
                sb.append("({'package':'");
                sb.append(this.a);
                str = "','error_code':8,'error':'unknown error'})";
            } else if (this.x.getErrorCode() != 0) {
                sb = new StringBuilder();
                sb.append(this.E);
                sb.append("&&");
                sb.append(this.E);
                sb.append("({'package':'");
                sb.append(this.a);
                sb.append("','error_code':");
                sb.append(this.x.getErrorCode());
                sb.append(",'error':'");
                sb.append(this.x.getErrorInfo());
                str = "'})";
            } else {
                sb = new StringBuilder();
                sb.append(this.E);
                sb.append("&&");
                sb.append(this.E);
                sb.append("({'package':'");
                sb.append(this.a);
                sb.append("','error_code':0,'error':'','location':{'y':");
                sb.append(this.x.getLatitude());
                sb.append(",'precision':");
                sb.append(this.x.getAccuracy());
                sb.append(",'x':");
                sb.append(this.x.getLongitude());
                str = "},'version_code':'4.7.1','version':'4.7.1'})";
            }
        }
        sb.append(str);
        return sb.toString();
    }

    private void f() {
        try {
            if (this.f != null) {
                this.f.k();
            }
        } catch (Throwable th) {
            es.a(th, "ApsServiceCore", "startColl");
        }
    }

    public final void a() {
        try {
            if (this.q != null) {
                this.q.close();
            }
        } catch (Throwable th) {
            es.a(th, "ApsServiceCore", "doStopScocket 1");
        }
        try {
            if (this.o != null) {
                this.o.close();
            }
        } catch (Throwable th2) {
            es.a(th2, "ApsServiceCore", "doStopScocket 2");
        }
        try {
            if (this.s != null) {
                this.s.interrupt();
            }
        } catch (Throwable unused) {
        }
        this.s = null;
        this.o = null;
        this.p = false;
        this.r = false;
    }

    final void a(Messenger messenger, Bundle bundle) {
        float f;
        if (bundle != null) {
            try {
                if (!bundle.isEmpty() && er.q()) {
                    double d = bundle.getDouble("lat");
                    double d2 = bundle.getDouble("lon");
                    b(bundle);
                    if (this.c != null) {
                        f = fa.a(new double[]{d, d2, this.c.getLatitude(), this.c.getLongitude()});
                        if (f < er.r() * 3) {
                            Bundle bundle2 = new Bundle();
                            bundle2.setClassLoader(AMapLocation.class.getClassLoader());
                            bundle2.putInt("I_MAX_GEO_DIS", er.r() * 3);
                            bundle2.putInt("I_MIN_GEO_DIS", er.r());
                            bundle2.putParcelable("loc", this.c);
                            a(messenger, 6, bundle2);
                        }
                    } else {
                        f = -1.0f;
                    }
                    if (f == -1.0f || f > er.r()) {
                        a(bundle);
                        this.c = this.f.a(d, d2);
                    }
                }
            } catch (Throwable th) {
                es.a(th, "ApsServiceCore", "doLocationGeo");
            }
        }
    }

    public final void b() {
        b bVar;
        try {
            this.h.clear();
            this.h = null;
            if (this.f != null) {
                cs.b(this.e);
            }
            if (this.d != null) {
                this.d.removeCallbacksAndMessages(null);
            }
            if (this.b != null) {
                if (Build.VERSION.SDK_INT >= 18) {
                    try {
                        ew.a(this.b, (Class<?>) HandlerThread.class, "quitSafely", new Object[0]);
                    } catch (Throwable unused) {
                        bVar = this.b;
                    }
                } else {
                    bVar = this.b;
                }
                bVar.quit();
            }
            this.b = null;
            this.d = null;
            if (this.A != null) {
                this.A.c();
                this.A = null;
            }
            a();
            this.t = false;
            this.u = false;
            this.f.f();
            ey.a(this.e);
            if (this.i != null && this.j != 0 && this.k != 0) {
                long c2 = fa.c() - this.j;
                ey.a(this.e, this.i.c(this.e), this.i.d(this.e), this.k, c2);
                this.i.e(this.e);
            }
            aq.b();
            if (g) {
                Process.killProcess(Process.myPid());
            }
        } catch (Throwable th) {
            es.a(th, "ApsServiceCore", "threadDestroy");
        }
    }
}
