package com.loc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.HandlerThread;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

/* compiled from: CgiManager.java */
@SuppressLint({"NewApi"})
/* loaded from: classes.dex */
public final class ee {
    int a;
    TelephonyManager c;
    CellLocation e;
    String h;
    HandlerThread k;
    private Context l;
    private ec p;
    private Object q;
    private int r;
    ArrayList<ed> b = new ArrayList<>();
    private String m = null;
    private ArrayList<ed> n = new ArrayList<>();
    private int o = -113;
    long d = 0;
    private long s = 0;
    boolean f = false;
    PhoneStateListener g = null;
    boolean i = false;
    StringBuilder j = null;
    private boolean t = false;
    private Object u = new Object();

    /* compiled from: CgiManager.java */
    /* loaded from: classes.dex */
    class a extends HandlerThread {
        public a(String str) {
            super(str);
        }

        @Override // android.os.HandlerThread
        protected final void onLooperPrepared() {
            int i;
            try {
                super.onLooperPrepared();
                synchronized (ee.this.u) {
                    if (!ee.this.t) {
                        final ee eeVar = ee.this;
                        eeVar.g = new PhoneStateListener() { // from class: com.loc.ee.1
                            @Override // android.telephony.PhoneStateListener
                            public final void onCellLocationChanged(CellLocation cellLocation) {
                                try {
                                    if (ee.this.a(cellLocation)) {
                                        ee.this.e = cellLocation;
                                        ee.this.f = true;
                                        ee.this.s = fa.c();
                                    }
                                } catch (Throwable unused) {
                                }
                            }

                            @Override // android.telephony.PhoneStateListener
                            public final void onServiceStateChanged(ServiceState serviceState) {
                                try {
                                    switch (serviceState.getState()) {
                                        case 0:
                                            ee.this.a(false, false);
                                            return;
                                        case 1:
                                            ee.this.i();
                                            return;
                                        default:
                                            return;
                                    }
                                } catch (Throwable unused) {
                                }
                            }

                            @Override // android.telephony.PhoneStateListener
                            public final void onSignalStrengthChanged(int i2) {
                                int i3 = -113;
                                try {
                                    switch (ee.this.a) {
                                        case 1:
                                        case 2:
                                            i3 = fa.a(i2);
                                            break;
                                    }
                                    ee.a(ee.this, i3);
                                } catch (Throwable unused) {
                                }
                            }

                            @Override // android.telephony.PhoneStateListener
                            public final void onSignalStrengthsChanged(SignalStrength signalStrength) {
                                if (signalStrength == null) {
                                    return;
                                }
                                int i2 = -113;
                                try {
                                    switch (ee.this.a) {
                                        case 1:
                                            i2 = fa.a(signalStrength.getGsmSignalStrength());
                                            break;
                                        case 2:
                                            i2 = signalStrength.getCdmaDbm();
                                            break;
                                    }
                                    ee.a(ee.this, i2);
                                } catch (Throwable unused) {
                                }
                            }
                        };
                        try {
                            i = ew.b("android.telephony.PhoneStateListener", fa.d() < 7 ? "LISTEN_SIGNAL_STRENGTH" : "LISTEN_SIGNAL_STRENGTHS");
                        } catch (Throwable unused) {
                            i = 0;
                        }
                        try {
                            if (i == 0) {
                                eeVar.c.listen(eeVar.g, 16);
                            } else {
                                eeVar.c.listen(eeVar.g, i | 16);
                            }
                        } catch (Throwable unused2) {
                        }
                    }
                }
            } catch (Throwable unused3) {
            }
        }

        @Override // android.os.HandlerThread, java.lang.Thread, java.lang.Runnable
        public final void run() {
            try {
                try {
                    super.run();
                } catch (Throwable unused) {
                    ee.this.c.listen(ee.this.g, 0);
                    ee.this.g = null;
                    quit();
                }
            } catch (Throwable unused2) {
            }
        }
    }

    public ee(Context context) {
        Object a2;
        this.a = 0;
        this.c = null;
        this.p = null;
        this.r = 0;
        this.h = null;
        this.k = null;
        this.l = context;
        if (this.c == null) {
            this.c = (TelephonyManager) fa.a(this.l, "phone");
        }
        if (this.c != null) {
            try {
                this.a = c(this.c.getCellLocation());
            } catch (SecurityException e) {
                this.h = e.getMessage();
            } catch (Throwable th) {
                this.h = null;
                es.a(th, "CgiManager", "CgiManager");
                this.a = 0;
            }
            try {
                this.r = r();
                switch (this.r) {
                    case 1:
                        a2 = fa.a(this.l, "phone_msim");
                        break;
                    case 2:
                        a2 = fa.a(this.l, "phone2");
                        break;
                    default:
                        a2 = fa.a(this.l, "phone2");
                        break;
                }
                this.q = a2;
            } catch (Throwable unused) {
            }
            if (this.k == null) {
                this.k = new a("listenerPhoneStateThread");
                this.k.start();
            }
        }
        this.p = new ec();
    }

    private CellLocation a(Object obj, String str, Object... objArr) {
        CellLocation cellLocation;
        if (obj == null) {
            return null;
        }
        try {
            Object a2 = ew.a(obj, str, objArr);
            cellLocation = a2 != null ? (CellLocation) a2 : null;
        } catch (Throwable unused) {
        }
        if (b(cellLocation)) {
            return cellLocation;
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x007c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00a6 A[SYNTHETIC] */
    @android.annotation.SuppressLint({"NewApi"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private android.telephony.CellLocation a(java.util.List<android.telephony.CellInfo> r11) {
        /*
            r10 = this;
            r0 = 0
            if (r11 == 0) goto Lae
            boolean r1 = r11.isEmpty()
            if (r1 == 0) goto Lb
            goto Lae
        Lb:
            r1 = 0
        Lc:
            int r2 = r11.size()
            if (r1 >= r2) goto L79
            java.lang.Object r2 = r11.get(r1)
            android.telephony.CellInfo r2 = (android.telephony.CellInfo) r2
            if (r2 == 0) goto L76
            boolean r3 = r2.isRegistered()     // Catch: java.lang.Throwable -> L76
            boolean r4 = r2 instanceof android.telephony.CellInfoCdma     // Catch: java.lang.Throwable -> L76
            if (r4 == 0) goto L34
            android.telephony.CellInfoCdma r2 = (android.telephony.CellInfoCdma) r2     // Catch: java.lang.Throwable -> L76
            android.telephony.CellIdentityCdma r4 = r2.getCellIdentity()     // Catch: java.lang.Throwable -> L76
            boolean r4 = a(r4)     // Catch: java.lang.Throwable -> L76
            if (r4 != 0) goto L2f
            goto L76
        L2f:
            com.loc.ed r2 = r10.a(r2, r3)     // Catch: java.lang.Throwable -> L76
            goto L7a
        L34:
            boolean r4 = r2 instanceof android.telephony.CellInfoGsm     // Catch: java.lang.Throwable -> L76
            if (r4 == 0) goto L4a
            android.telephony.CellInfoGsm r2 = (android.telephony.CellInfoGsm) r2     // Catch: java.lang.Throwable -> L76
            android.telephony.CellIdentityGsm r4 = r2.getCellIdentity()     // Catch: java.lang.Throwable -> L76
            boolean r4 = a(r4)     // Catch: java.lang.Throwable -> L76
            if (r4 != 0) goto L45
            goto L76
        L45:
            com.loc.ed r2 = a(r2, r3)     // Catch: java.lang.Throwable -> L76
            goto L7a
        L4a:
            boolean r4 = r2 instanceof android.telephony.CellInfoWcdma     // Catch: java.lang.Throwable -> L76
            if (r4 == 0) goto L60
            android.telephony.CellInfoWcdma r2 = (android.telephony.CellInfoWcdma) r2     // Catch: java.lang.Throwable -> L76
            android.telephony.CellIdentityWcdma r4 = r2.getCellIdentity()     // Catch: java.lang.Throwable -> L76
            boolean r4 = a(r4)     // Catch: java.lang.Throwable -> L76
            if (r4 != 0) goto L5b
            goto L76
        L5b:
            com.loc.ed r2 = a(r2, r3)     // Catch: java.lang.Throwable -> L76
            goto L7a
        L60:
            boolean r4 = r2 instanceof android.telephony.CellInfoLte     // Catch: java.lang.Throwable -> L76
            if (r4 == 0) goto L79
            android.telephony.CellInfoLte r2 = (android.telephony.CellInfoLte) r2     // Catch: java.lang.Throwable -> L76
            android.telephony.CellIdentityLte r4 = r2.getCellIdentity()     // Catch: java.lang.Throwable -> L76
            boolean r4 = a(r4)     // Catch: java.lang.Throwable -> L76
            if (r4 != 0) goto L71
            goto L76
        L71:
            com.loc.ed r2 = a(r2, r3)     // Catch: java.lang.Throwable -> L76
            goto L7a
        L76:
            int r1 = r1 + 1
            goto Lc
        L79:
            r2 = r0
        L7a:
            if (r2 == 0) goto La6
            int r11 = r2.k     // Catch: java.lang.Throwable -> La4
            r1 = 2
            if (r11 != r1) goto L97
            android.telephony.cdma.CdmaCellLocation r11 = new android.telephony.cdma.CdmaCellLocation     // Catch: java.lang.Throwable -> La4
            r11.<init>()     // Catch: java.lang.Throwable -> La4
            int r4 = r2.i     // Catch: java.lang.Throwable -> L95
            int r5 = r2.e     // Catch: java.lang.Throwable -> L95
            int r6 = r2.f     // Catch: java.lang.Throwable -> L95
            int r7 = r2.g     // Catch: java.lang.Throwable -> L95
            int r8 = r2.h     // Catch: java.lang.Throwable -> L95
            r3 = r11
            r3.setCellLocationData(r4, r5, r6, r7, r8)     // Catch: java.lang.Throwable -> L95
            goto Laa
        L95:
            goto Laa
        L97:
            android.telephony.gsm.GsmCellLocation r11 = new android.telephony.gsm.GsmCellLocation     // Catch: java.lang.Throwable -> La4
            r11.<init>()     // Catch: java.lang.Throwable -> La4
            int r1 = r2.c     // Catch: java.lang.Throwable -> La7
            int r2 = r2.d     // Catch: java.lang.Throwable -> La7
            r11.setLacAndCid(r1, r2)     // Catch: java.lang.Throwable -> La7
            goto La7
        La4:
            r11 = r0
            goto Laa
        La6:
            r11 = r0
        La7:
            r9 = r0
            r0 = r11
            r11 = r9
        Laa:
            if (r11 != 0) goto Lad
            return r0
        Lad:
            return r11
        Lae:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ee.a(java.util.List):android.telephony.CellLocation");
    }

    private static ed a(int i, boolean z, int i2, int i3, int i4, int i5, int i6) {
        ed edVar = new ed(i, z);
        edVar.a = i2;
        edVar.b = i3;
        edVar.c = i4;
        edVar.d = i5;
        edVar.j = i6;
        return edVar;
    }

    @SuppressLint({"NewApi"})
    private ed a(CellInfoCdma cellInfoCdma, boolean z) {
        int i;
        int i2;
        int i3;
        CellIdentityCdma cellIdentity = cellInfoCdma.getCellIdentity();
        String[] a2 = fa.a(this.c);
        try {
            i = Integer.parseInt(a2[0]);
        } catch (Throwable unused) {
            i = 0;
        }
        try {
            i3 = Integer.parseInt(a2[1]);
            i2 = i;
        } catch (Throwable unused2) {
            i2 = i;
            i3 = 0;
            ed a3 = a(2, z, i2, i3, 0, 0, cellInfoCdma.getCellSignalStrength().getCdmaDbm());
            a3.g = cellIdentity.getSystemId();
            a3.h = cellIdentity.getNetworkId();
            a3.i = cellIdentity.getBasestationId();
            a3.e = cellIdentity.getLatitude();
            a3.f = cellIdentity.getLongitude();
            return a3;
        }
        ed a32 = a(2, z, i2, i3, 0, 0, cellInfoCdma.getCellSignalStrength().getCdmaDbm());
        a32.g = cellIdentity.getSystemId();
        a32.h = cellIdentity.getNetworkId();
        a32.i = cellIdentity.getBasestationId();
        a32.e = cellIdentity.getLatitude();
        a32.f = cellIdentity.getLongitude();
        return a32;
    }

    @SuppressLint({"NewApi"})
    private static ed a(CellInfoGsm cellInfoGsm, boolean z) {
        CellIdentityGsm cellIdentity = cellInfoGsm.getCellIdentity();
        return a(1, z, cellIdentity.getMcc(), cellIdentity.getMnc(), cellIdentity.getLac(), cellIdentity.getCid(), cellInfoGsm.getCellSignalStrength().getDbm());
    }

    @SuppressLint({"NewApi"})
    private static ed a(CellInfoLte cellInfoLte, boolean z) {
        CellIdentityLte cellIdentity = cellInfoLte.getCellIdentity();
        ed a2 = a(3, z, cellIdentity.getMcc(), cellIdentity.getMnc(), cellIdentity.getTac(), cellIdentity.getCi(), cellInfoLte.getCellSignalStrength().getDbm());
        a2.o = cellIdentity.getPci();
        return a2;
    }

    @SuppressLint({"NewApi"})
    private static ed a(CellInfoWcdma cellInfoWcdma, boolean z) {
        CellIdentityWcdma cellIdentity = cellInfoWcdma.getCellIdentity();
        ed a2 = a(4, z, cellIdentity.getMcc(), cellIdentity.getMnc(), cellIdentity.getLac(), cellIdentity.getCid(), cellInfoWcdma.getCellSignalStrength().getDbm());
        a2.o = cellIdentity.getPsc();
        return a2;
    }

    private static ed a(NeighboringCellInfo neighboringCellInfo, String[] strArr) {
        try {
            ed edVar = new ed(1, false);
            edVar.a = Integer.parseInt(strArr[0]);
            edVar.b = Integer.parseInt(strArr[1]);
            edVar.c = ew.b(neighboringCellInfo, "getLac", new Object[0]);
            edVar.d = neighboringCellInfo.getCid();
            edVar.j = fa.a(neighboringCellInfo.getRssi());
            return edVar;
        } catch (Throwable th) {
            es.a(th, "CgiManager", "getGsm");
            return null;
        }
    }

    private void a(CellLocation cellLocation, String[] strArr, boolean z) {
        ed a2;
        if (cellLocation == null || this.c == null) {
            return;
        }
        this.b.clear();
        if (b(cellLocation)) {
            this.a = 1;
            ArrayList<ed> arrayList = this.b;
            GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
            ed edVar = new ed(1, true);
            edVar.a = fa.g(strArr[0]);
            edVar.b = fa.g(strArr[1]);
            edVar.c = gsmCellLocation.getLac();
            edVar.d = gsmCellLocation.getCid();
            edVar.j = this.o;
            arrayList.add(edVar);
            if (z) {
                return;
            }
            List<NeighboringCellInfo> list = Build.VERSION.SDK_INT <= 28 ? (List) ew.a(this.c, "getNeighboringCellInfo", new Object[0]) : null;
            if (list == null || list.isEmpty()) {
                return;
            }
            for (NeighboringCellInfo neighboringCellInfo : list) {
                if (neighboringCellInfo != null && a(neighboringCellInfo.getLac(), neighboringCellInfo.getCid()) && (a2 = a(neighboringCellInfo, strArr)) != null && !this.b.contains(a2)) {
                    this.b.add(a2);
                }
            }
        }
    }

    static /* synthetic */ void a(ee eeVar, int i) {
        if (i == -113) {
            eeVar.o = -113;
            return;
        }
        eeVar.o = i;
        switch (eeVar.a) {
            case 1:
            case 2:
                if (eeVar.b == null || eeVar.b.isEmpty()) {
                    return;
                }
                try {
                    eeVar.b.get(0).j = eeVar.o;
                    return;
                } catch (Throwable unused) {
                    return;
                }
            default:
                return;
        }
    }

    private static boolean a(int i) {
        return (i == -1 || i == 0 || i > 65535) ? false : true;
    }

    private static boolean a(int i, int i2) {
        return (i == -1 || i == 0 || i > 65535 || i2 == -1 || i2 == 0 || i2 == 65535 || i2 >= 268435455) ? false : true;
    }

    @SuppressLint({"NewApi"})
    private static boolean a(CellIdentityCdma cellIdentityCdma) {
        return cellIdentityCdma != null && cellIdentityCdma.getSystemId() > 0 && cellIdentityCdma.getNetworkId() >= 0 && cellIdentityCdma.getBasestationId() >= 0;
    }

    @SuppressLint({"NewApi"})
    private static boolean a(CellIdentityGsm cellIdentityGsm) {
        return cellIdentityGsm != null && a(cellIdentityGsm.getLac()) && b(cellIdentityGsm.getCid());
    }

    @SuppressLint({"NewApi"})
    private static boolean a(CellIdentityLte cellIdentityLte) {
        return cellIdentityLte != null && a(cellIdentityLte.getTac()) && b(cellIdentityLte.getCi());
    }

    @SuppressLint({"NewApi"})
    private static boolean a(CellIdentityWcdma cellIdentityWcdma) {
        return cellIdentityWcdma != null && a(cellIdentityWcdma.getLac()) && b(cellIdentityWcdma.getCid());
    }

    private static boolean b(int i) {
        return (i == -1 || i == 0 || i == 65535 || i >= 268435455) ? false : true;
    }

    private boolean b(CellLocation cellLocation) {
        boolean a2 = a(cellLocation);
        if (!a2) {
            this.a = 0;
        }
        return a2;
    }

    private int c(CellLocation cellLocation) {
        if (this.i || cellLocation == null) {
            return 0;
        }
        if (cellLocation instanceof GsmCellLocation) {
            return 1;
        }
        try {
            Class.forName("android.telephony.cdma.CdmaCellLocation");
            return 2;
        } catch (Throwable th) {
            es.a(th, "Utils", "getCellLocT");
            return 0;
        }
    }

    private CellLocation n() {
        if (this.c != null) {
            try {
                CellLocation cellLocation = this.c.getCellLocation();
                this.h = null;
                if (b(cellLocation)) {
                    this.e = cellLocation;
                    return cellLocation;
                }
            } catch (SecurityException e) {
                this.h = e.getMessage();
            } catch (Throwable th) {
                this.h = null;
                es.a(th, "CgiManager", "getCellLocation");
            }
        }
        return null;
    }

    @SuppressLint({"NewApi"})
    private CellLocation o() {
        TelephonyManager telephonyManager = this.c;
        CellLocation cellLocation = null;
        if (telephonyManager == null) {
            return null;
        }
        CellLocation n = n();
        if (b(n)) {
            return n;
        }
        if (fa.d() >= 18) {
            try {
                cellLocation = a(telephonyManager.getAllCellInfo());
            } catch (SecurityException e) {
                this.h = e.getMessage();
            }
        }
        if (cellLocation != null) {
            return cellLocation;
        }
        CellLocation a2 = a(telephonyManager, "getCellLocationExt", 1);
        if (a2 != null) {
            return a2;
        }
        CellLocation a3 = a(telephonyManager, "getCellLocationGemini", 1);
        if (a3 != null) {
        }
        return a3;
    }

    private CellLocation p() {
        Object obj = this.q;
        CellLocation cellLocation = null;
        if (obj == null) {
            return null;
        }
        try {
            Class<?> q = q();
            if (q.isInstance(obj)) {
                Object cast = q.cast(obj);
                CellLocation a2 = a(cast, "getCellLocation", new Object[0]);
                if (a2 != null) {
                    return a2;
                }
                try {
                    CellLocation a3 = a(cast, "getCellLocation", 1);
                    if (a3 != null) {
                        return a3;
                    }
                    try {
                        a2 = a(cast, "getCellLocationGemini", 1);
                        if (a2 != null) {
                            return a2;
                        }
                        cellLocation = a(cast, "getAllCellInfo", 1);
                        if (cellLocation != null) {
                            return cellLocation;
                        }
                    } catch (Throwable th) {
                        th = th;
                        cellLocation = a3;
                        es.a(th, "CgiManager", "getSim2Cgi");
                        return cellLocation;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    cellLocation = a2;
                }
            }
        } catch (Throwable th3) {
            th = th3;
        }
        return cellLocation;
    }

    private Class<?> q() {
        String str;
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        switch (this.r) {
            case 0:
                str = "android.telephony.TelephonyManager";
                break;
            case 1:
                str = "android.telephony.MSimTelephonyManager";
                break;
            case 2:
                str = "android.telephony.TelephonyManager2";
                break;
            default:
                str = null;
                break;
        }
        try {
            return systemClassLoader.loadClass(str);
        } catch (Throwable th) {
            es.a(th, "CgiManager", "getSim2TmClass");
            return null;
        }
    }

    private int r() {
        try {
            Class.forName("android.telephony.MSimTelephonyManager");
            this.r = 1;
        } catch (Throwable unused) {
        }
        if (this.r == 0) {
            try {
                Class.forName("android.telephony.TelephonyManager2");
                this.r = 2;
            } catch (Throwable unused2) {
            }
        }
        return this.r;
    }

    public final ArrayList<ed> a() {
        return this.b;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(19:1|2|(4:189|(12:27|(4:31|(1:33)|34|(1:36)(2:37|(1:39)))|40|(2:45|(5:46|47|48|49|(1:51)(1:55)))(0)|59|(2:61|62)|112|113|(7:117|118|119|120|121|(4:(1:126)|127|(3:129|(5:131|132|(2:134|(2:136|137)(1:139))(2:142|(2:144|(2:146|147)(1:148))(2:149|(2:151|(2:153|154)(1:155))(2:156|(2:158|(2:160|161)(1:162))(1:163))))|140|141)(1:166)|138)|167)|(1:171))|179|(2:181|(1:183))|184)|8|(2:10|11)(2:13|14))|4|(1:6)|27|(5:29|31|(0)|34|(0)(0))|40|(3:42|45|(5:46|47|48|49|(0)(0)))(0)|59|(0)|112|113|(8:115|117|118|119|120|121|(5:123|(0)|127|(0)|167)|(2:169|171))|179|(0)|184|8|(0)(0)) */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x00df, code lost:
    
        if (r10 == false) goto L64;
     */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0281 A[Catch: Throwable -> 0x02a3, SecurityException -> 0x02ac, TryCatch #5 {Throwable -> 0x02a3, blocks: (B:2:0x0000, B:6:0x0022, B:8:0x027d, B:10:0x0281, B:13:0x0285, B:14:0x0289, B:17:0x028d, B:19:0x0295, B:22:0x0298, B:24:0x02a0, B:27:0x002a, B:29:0x002f, B:31:0x0033, B:33:0x003d, B:34:0x0041, B:36:0x0047, B:37:0x0050, B:39:0x005f, B:40:0x006b, B:42:0x006f, B:48:0x0078, B:49:0x0080, B:58:0x007d, B:59:0x0089, B:61:0x0093, B:62:0x009f, B:64:0x00a4, B:66:0x00a8, B:110:0x016d, B:111:0x0175, B:179:0x025d, B:181:0x0261, B:183:0x0271, B:184:0x0277, B:186:0x0010), top: B:1:0x0000 }] */
    /* JADX WARN: Removed duplicated region for block: B:123:0x019f A[Catch: Throwable -> 0x025d, SecurityException -> 0x02ac, TryCatch #1 {Throwable -> 0x025d, blocks: (B:113:0x017a, B:115:0x0182, B:117:0x0186, B:119:0x018a, B:121:0x0190, B:123:0x019f, B:126:0x01a7, B:129:0x01ad, B:169:0x024e, B:171:0x0254, B:175:0x0197), top: B:112:0x017a }] */
    /* JADX WARN: Removed duplicated region for block: B:126:0x01a7 A[Catch: Throwable -> 0x025d, SecurityException -> 0x02ac, TryCatch #1 {Throwable -> 0x025d, blocks: (B:113:0x017a, B:115:0x0182, B:117:0x0186, B:119:0x018a, B:121:0x0190, B:123:0x019f, B:126:0x01a7, B:129:0x01ad, B:169:0x024e, B:171:0x0254, B:175:0x0197), top: B:112:0x017a }] */
    /* JADX WARN: Removed duplicated region for block: B:129:0x01ad A[Catch: Throwable -> 0x025d, SecurityException -> 0x02ac, TRY_LEAVE, TryCatch #1 {Throwable -> 0x025d, blocks: (B:113:0x017a, B:115:0x0182, B:117:0x0186, B:119:0x018a, B:121:0x0190, B:123:0x019f, B:126:0x01a7, B:129:0x01ad, B:169:0x024e, B:171:0x0254, B:175:0x0197), top: B:112:0x017a }] */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0285 A[Catch: Throwable -> 0x02a3, SecurityException -> 0x02ac, TryCatch #5 {Throwable -> 0x02a3, blocks: (B:2:0x0000, B:6:0x0022, B:8:0x027d, B:10:0x0281, B:13:0x0285, B:14:0x0289, B:17:0x028d, B:19:0x0295, B:22:0x0298, B:24:0x02a0, B:27:0x002a, B:29:0x002f, B:31:0x0033, B:33:0x003d, B:34:0x0041, B:36:0x0047, B:37:0x0050, B:39:0x005f, B:40:0x006b, B:42:0x006f, B:48:0x0078, B:49:0x0080, B:58:0x007d, B:59:0x0089, B:61:0x0093, B:62:0x009f, B:64:0x00a4, B:66:0x00a8, B:110:0x016d, B:111:0x0175, B:179:0x025d, B:181:0x0261, B:183:0x0271, B:184:0x0277, B:186:0x0010), top: B:1:0x0000 }] */
    /* JADX WARN: Removed duplicated region for block: B:169:0x024e A[Catch: Throwable -> 0x025d, SecurityException -> 0x02ac, TRY_ENTER, TryCatch #1 {Throwable -> 0x025d, blocks: (B:113:0x017a, B:115:0x0182, B:117:0x0186, B:119:0x018a, B:121:0x0190, B:123:0x019f, B:126:0x01a7, B:129:0x01ad, B:169:0x024e, B:171:0x0254, B:175:0x0197), top: B:112:0x017a }] */
    /* JADX WARN: Removed duplicated region for block: B:181:0x0261 A[Catch: Throwable -> 0x02a3, SecurityException -> 0x02ac, TryCatch #5 {Throwable -> 0x02a3, blocks: (B:2:0x0000, B:6:0x0022, B:8:0x027d, B:10:0x0281, B:13:0x0285, B:14:0x0289, B:17:0x028d, B:19:0x0295, B:22:0x0298, B:24:0x02a0, B:27:0x002a, B:29:0x002f, B:31:0x0033, B:33:0x003d, B:34:0x0041, B:36:0x0047, B:37:0x0050, B:39:0x005f, B:40:0x006b, B:42:0x006f, B:48:0x0078, B:49:0x0080, B:58:0x007d, B:59:0x0089, B:61:0x0093, B:62:0x009f, B:64:0x00a4, B:66:0x00a8, B:110:0x016d, B:111:0x0175, B:179:0x025d, B:181:0x0261, B:183:0x0271, B:184:0x0277, B:186:0x0010), top: B:1:0x0000 }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x003d A[Catch: Throwable -> 0x02a3, SecurityException -> 0x02ac, TryCatch #5 {Throwable -> 0x02a3, blocks: (B:2:0x0000, B:6:0x0022, B:8:0x027d, B:10:0x0281, B:13:0x0285, B:14:0x0289, B:17:0x028d, B:19:0x0295, B:22:0x0298, B:24:0x02a0, B:27:0x002a, B:29:0x002f, B:31:0x0033, B:33:0x003d, B:34:0x0041, B:36:0x0047, B:37:0x0050, B:39:0x005f, B:40:0x006b, B:42:0x006f, B:48:0x0078, B:49:0x0080, B:58:0x007d, B:59:0x0089, B:61:0x0093, B:62:0x009f, B:64:0x00a4, B:66:0x00a8, B:110:0x016d, B:111:0x0175, B:179:0x025d, B:181:0x0261, B:183:0x0271, B:184:0x0277, B:186:0x0010), top: B:1:0x0000 }] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0047 A[Catch: Throwable -> 0x02a3, SecurityException -> 0x02ac, TryCatch #5 {Throwable -> 0x02a3, blocks: (B:2:0x0000, B:6:0x0022, B:8:0x027d, B:10:0x0281, B:13:0x0285, B:14:0x0289, B:17:0x028d, B:19:0x0295, B:22:0x0298, B:24:0x02a0, B:27:0x002a, B:29:0x002f, B:31:0x0033, B:33:0x003d, B:34:0x0041, B:36:0x0047, B:37:0x0050, B:39:0x005f, B:40:0x006b, B:42:0x006f, B:48:0x0078, B:49:0x0080, B:58:0x007d, B:59:0x0089, B:61:0x0093, B:62:0x009f, B:64:0x00a4, B:66:0x00a8, B:110:0x016d, B:111:0x0175, B:179:0x025d, B:181:0x0261, B:183:0x0271, B:184:0x0277, B:186:0x0010), top: B:1:0x0000 }] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0050 A[Catch: Throwable -> 0x02a3, SecurityException -> 0x02ac, TryCatch #5 {Throwable -> 0x02a3, blocks: (B:2:0x0000, B:6:0x0022, B:8:0x027d, B:10:0x0281, B:13:0x0285, B:14:0x0289, B:17:0x028d, B:19:0x0295, B:22:0x0298, B:24:0x02a0, B:27:0x002a, B:29:0x002f, B:31:0x0033, B:33:0x003d, B:34:0x0041, B:36:0x0047, B:37:0x0050, B:39:0x005f, B:40:0x006b, B:42:0x006f, B:48:0x0078, B:49:0x0080, B:58:0x007d, B:59:0x0089, B:61:0x0093, B:62:0x009f, B:64:0x00a4, B:66:0x00a8, B:110:0x016d, B:111:0x0175, B:179:0x025d, B:181:0x0261, B:183:0x0271, B:184:0x0277, B:186:0x0010), top: B:1:0x0000 }] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0085  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0089 A[EDGE_INSN: B:55:0x0089->B:59:0x0089 BREAK  A[LOOP:0: B:46:0x0076->B:54:?], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0093 A[Catch: Throwable -> 0x02a3, SecurityException -> 0x02ac, TryCatch #5 {Throwable -> 0x02a3, blocks: (B:2:0x0000, B:6:0x0022, B:8:0x027d, B:10:0x0281, B:13:0x0285, B:14:0x0289, B:17:0x028d, B:19:0x0295, B:22:0x0298, B:24:0x02a0, B:27:0x002a, B:29:0x002f, B:31:0x0033, B:33:0x003d, B:34:0x0041, B:36:0x0047, B:37:0x0050, B:39:0x005f, B:40:0x006b, B:42:0x006f, B:48:0x0078, B:49:0x0080, B:58:0x007d, B:59:0x0089, B:61:0x0093, B:62:0x009f, B:64:0x00a4, B:66:0x00a8, B:110:0x016d, B:111:0x0175, B:179:0x025d, B:181:0x0261, B:183:0x0271, B:184:0x0277, B:186:0x0010), top: B:1:0x0000 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void a(boolean r10, boolean r11) {
        /*
            Method dump skipped, instructions count: 708
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ee.a(boolean, boolean):void");
    }

    final boolean a(CellLocation cellLocation) {
        String str;
        String str2;
        if (cellLocation == null) {
            return false;
        }
        switch (c(cellLocation)) {
            case 1:
                try {
                    GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
                    return a(gsmCellLocation.getLac(), gsmCellLocation.getCid());
                } catch (Throwable th) {
                    th = th;
                    str = "CgiManager";
                    str2 = "cgiUseful Cgi.I_GSM_T";
                    break;
                }
            case 2:
                try {
                    if (ew.b(cellLocation, "getSystemId", new Object[0]) > 0 && ew.b(cellLocation, "getNetworkId", new Object[0]) >= 0) {
                        if (ew.b(cellLocation, "getBaseStationId", new Object[0]) >= 0) {
                            return true;
                        }
                    }
                    return false;
                } catch (Throwable th2) {
                    th = th2;
                    str = "CgiManager";
                    str2 = "cgiUseful Cgi.I_CDMA_T";
                    break;
                }
                break;
            default:
                return true;
        }
        es.a(th, str, str2);
        return true;
    }

    public final ArrayList<ed> b() {
        return this.n;
    }

    public final ed c() {
        if (this.i) {
            return null;
        }
        ArrayList<ed> arrayList = this.b;
        if (arrayList.size() > 0) {
            return arrayList.get(0);
        }
        return null;
    }

    public final ed d() {
        if (this.i) {
            return null;
        }
        ArrayList<ed> arrayList = this.n;
        if (arrayList.size() > 0) {
            return arrayList.get(0);
        }
        return null;
    }

    public final int e() {
        return this.a;
    }

    public final int f() {
        return this.a & 3;
    }

    public final TelephonyManager g() {
        return this.c;
    }

    public final void h() {
        this.p.a();
        this.s = 0L;
        synchronized (this.u) {
            this.t = true;
        }
        if (this.c != null && this.g != null) {
            try {
                this.c.listen(this.g, 0);
            } catch (Throwable th) {
                es.a(th, "CgiManager", "destroy");
            }
        }
        this.g = null;
        if (this.k != null) {
            this.k.quit();
            this.k = null;
        }
        this.o = -113;
        this.c = null;
        this.q = null;
    }

    final void i() {
        this.h = null;
        this.e = null;
        this.a = 0;
        this.b.clear();
        this.n.clear();
    }

    public final String j() {
        return this.h;
    }

    public final String k() {
        return this.m;
    }

    public final String l() {
        if (this.i) {
            i();
        }
        if (this.j == null) {
            this.j = new StringBuilder();
        } else {
            this.j.delete(0, this.j.length());
        }
        if ((this.a & 3) == 1) {
            for (int i = 1; i < this.b.size(); i++) {
                StringBuilder sb = this.j;
                sb.append("#");
                sb.append(this.b.get(i).b);
                StringBuilder sb2 = this.j;
                sb2.append("|");
                sb2.append(this.b.get(i).c);
                StringBuilder sb3 = this.j;
                sb3.append("|");
                sb3.append(this.b.get(i).d);
            }
        }
        if (this.j.length() > 0) {
            this.j.deleteCharAt(0);
        }
        return this.j.toString();
    }

    public final boolean m() {
        try {
            if (this.c != null) {
                if (!TextUtils.isEmpty(this.c.getSimOperator())) {
                    return true;
                }
                if (!TextUtils.isEmpty(this.c.getSimCountryIso())) {
                    return true;
                }
            }
        } catch (Throwable unused) {
        }
        try {
            int a2 = fa.a(fa.c(this.l));
            return a2 == 0 || a2 == 4 || a2 == 2 || a2 == 5 || a2 == 3;
        } catch (Throwable unused2) {
            return false;
        }
    }
}
