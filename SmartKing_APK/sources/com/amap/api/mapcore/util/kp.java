package com.amap.api.mapcore.util;

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
import org.apache.commons.lang.time.DateUtils;

/* compiled from: CgiManager.java */
@SuppressLint({"NewApi"})
/* loaded from: classes.dex */
public final class kp {
    TelephonyManager c;
    CellLocation e;
    private Context l;
    private kn p;
    private Object q;
    int a = 0;
    ArrayList<ko> b = new ArrayList<>();
    private String m = null;
    private ArrayList<ko> n = new ArrayList<>();
    private int o = -113;
    long d = 0;
    private int r = 0;
    private long s = 0;
    boolean f = false;
    PhoneStateListener g = null;
    String h = null;
    boolean i = false;
    StringBuilder j = null;
    HandlerThread k = null;
    private boolean t = false;
    private Object u = new Object();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: CgiManager.java */
    /* loaded from: classes.dex */
    public class a extends HandlerThread {
        public a(String str) {
            super(str);
        }

        @Override // android.os.HandlerThread
        protected final void onLooperPrepared() {
            try {
                super.onLooperPrepared();
                synchronized (kp.this.u) {
                    if (!kp.this.t) {
                        kp.this.k();
                    }
                }
            } catch (Throwable unused) {
            }
        }

        @Override // android.os.HandlerThread, java.lang.Thread, java.lang.Runnable
        public final void run() {
            try {
                try {
                    super.run();
                } catch (Throwable unused) {
                    kp.this.c.listen(kp.this.g, 0);
                    kp.this.g = null;
                    quit();
                }
            } catch (Throwable unused2) {
            }
        }
    }

    public kp(Context context) {
        this.c = null;
        this.p = null;
        this.l = context;
        if (this.c == null) {
            this.c = (TelephonyManager) la.a(this.l, "phone");
        }
        j();
        this.p = new kn();
    }

    private CellLocation a(Object obj, String str, Object... objArr) {
        CellLocation cellLocation;
        if (obj == null) {
            return null;
        }
        try {
            Object a2 = ky.a(obj, str, objArr);
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
            com.amap.api.mapcore.util.ko r2 = r10.a(r2, r3)     // Catch: java.lang.Throwable -> L76
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
            com.amap.api.mapcore.util.ko r2 = a(r2, r3)     // Catch: java.lang.Throwable -> L76
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
            com.amap.api.mapcore.util.ko r2 = a(r2, r3)     // Catch: java.lang.Throwable -> L76
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
            com.amap.api.mapcore.util.ko r2 = a(r2, r3)     // Catch: java.lang.Throwable -> L76
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
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.kp.a(java.util.List):android.telephony.CellLocation");
    }

    private static ko a(int i, boolean z, int i2, int i3, int i4, int i5, int i6) {
        ko koVar = new ko(i, z);
        koVar.a = i2;
        koVar.b = i3;
        koVar.c = i4;
        koVar.d = i5;
        koVar.j = i6;
        return koVar;
    }

    @SuppressLint({"NewApi"})
    private ko a(CellInfoCdma cellInfoCdma, boolean z) {
        int i;
        int i2;
        int i3;
        CellIdentityCdma cellIdentity = cellInfoCdma.getCellIdentity();
        String[] a2 = la.a(this.c);
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
            ko a3 = a(2, z, i2, i3, 0, 0, cellInfoCdma.getCellSignalStrength().getCdmaDbm());
            a3.g = cellIdentity.getSystemId();
            a3.h = cellIdentity.getNetworkId();
            a3.i = cellIdentity.getBasestationId();
            a3.e = cellIdentity.getLatitude();
            a3.f = cellIdentity.getLongitude();
            return a3;
        }
        ko a32 = a(2, z, i2, i3, 0, 0, cellInfoCdma.getCellSignalStrength().getCdmaDbm());
        a32.g = cellIdentity.getSystemId();
        a32.h = cellIdentity.getNetworkId();
        a32.i = cellIdentity.getBasestationId();
        a32.e = cellIdentity.getLatitude();
        a32.f = cellIdentity.getLongitude();
        return a32;
    }

    @SuppressLint({"NewApi"})
    private static ko a(CellInfoGsm cellInfoGsm, boolean z) {
        CellIdentityGsm cellIdentity = cellInfoGsm.getCellIdentity();
        return a(1, z, cellIdentity.getMcc(), cellIdentity.getMnc(), cellIdentity.getLac(), cellIdentity.getCid(), cellInfoGsm.getCellSignalStrength().getDbm());
    }

    @SuppressLint({"NewApi"})
    private static ko a(CellInfoLte cellInfoLte, boolean z) {
        CellIdentityLte cellIdentity = cellInfoLte.getCellIdentity();
        ko a2 = a(3, z, cellIdentity.getMcc(), cellIdentity.getMnc(), cellIdentity.getTac(), cellIdentity.getCi(), cellInfoLte.getCellSignalStrength().getDbm());
        a2.o = cellIdentity.getPci();
        return a2;
    }

    @SuppressLint({"NewApi"})
    private static ko a(CellInfoWcdma cellInfoWcdma, boolean z) {
        CellIdentityWcdma cellIdentity = cellInfoWcdma.getCellIdentity();
        ko a2 = a(4, z, cellIdentity.getMcc(), cellIdentity.getMnc(), cellIdentity.getLac(), cellIdentity.getCid(), cellInfoWcdma.getCellSignalStrength().getDbm());
        a2.o = cellIdentity.getPsc();
        return a2;
    }

    private static ko a(NeighboringCellInfo neighboringCellInfo, String[] strArr) {
        try {
            ko koVar = new ko(1, false);
            koVar.a = Integer.parseInt(strArr[0]);
            koVar.b = Integer.parseInt(strArr[1]);
            koVar.c = ky.b(neighboringCellInfo, "getLac", new Object[0]);
            koVar.d = neighboringCellInfo.getCid();
            koVar.j = la.a(neighboringCellInfo.getRssi());
            return koVar;
        } catch (Throwable th) {
            kw.a(th, "CgiManager", "getGsm");
            return null;
        }
    }

    private void a(CellLocation cellLocation, String[] strArr) {
        ko a2;
        if (cellLocation == null || this.c == null) {
            return;
        }
        this.b.clear();
        if (b(cellLocation)) {
            this.a = 1;
            this.b.add(c(cellLocation, strArr));
            List<NeighboringCellInfo> list = Build.VERSION.SDK_INT <= 28 ? (List) ky.a(this.c, "getNeighboringCellInfo", new Object[0]) : null;
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

    public static boolean a(int i) {
        return i > 0 && i <= 15;
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
        return cellIdentityGsm != null && c(cellIdentityGsm.getLac()) && d(cellIdentityGsm.getCid());
    }

    @SuppressLint({"NewApi"})
    private static boolean a(CellIdentityLte cellIdentityLte) {
        return cellIdentityLte != null && c(cellIdentityLte.getTac()) && d(cellIdentityLte.getCi());
    }

    @SuppressLint({"NewApi"})
    private static boolean a(CellIdentityWcdma cellIdentityWcdma) {
        return cellIdentityWcdma != null && c(cellIdentityWcdma.getLac()) && d(cellIdentityWcdma.getCid());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(int i) {
        if (i == -113) {
            this.o = -113;
            return;
        }
        this.o = i;
        switch (this.a) {
            case 1:
            case 2:
                if (this.b == null || this.b.isEmpty()) {
                    return;
                }
                try {
                    this.b.get(0).j = this.o;
                    return;
                } catch (Throwable unused) {
                    return;
                }
            default:
                return;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:47:0x003f A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void b(android.telephony.CellLocation r5, java.lang.String[] r6) {
        /*
            r4 = this;
            if (r5 != 0) goto L3
            return
        L3:
            java.util.ArrayList<com.amap.api.mapcore.util.ko> r0 = r4.b
            r0.clear()
            int r0 = com.amap.api.mapcore.util.la.c()
            r1 = 5
            if (r0 >= r1) goto L10
            return
        L10:
            java.lang.Object r0 = r4.q     // Catch: java.lang.Throwable -> Lc8
            r1 = 1
            r2 = 0
            if (r0 == 0) goto L40
            java.lang.Class r0 = r5.getClass()     // Catch: java.lang.Throwable -> L3c
            java.lang.String r3 = "mGsmCellLoc"
            java.lang.reflect.Field r0 = r0.getDeclaredField(r3)     // Catch: java.lang.Throwable -> L3c
            boolean r3 = r0.isAccessible()     // Catch: java.lang.Throwable -> L3c
            if (r3 != 0) goto L29
            r0.setAccessible(r1)     // Catch: java.lang.Throwable -> L3c
        L29:
            java.lang.Object r0 = r0.get(r5)     // Catch: java.lang.Throwable -> L3c
            android.telephony.gsm.GsmCellLocation r0 = (android.telephony.gsm.GsmCellLocation) r0     // Catch: java.lang.Throwable -> L3c
            if (r0 == 0) goto L3c
            boolean r3 = r4.b(r0)     // Catch: java.lang.Throwable -> L3c
            if (r3 == 0) goto L3c
            r4.a(r0, r6)     // Catch: java.lang.Throwable -> L3c
            r0 = 1
            goto L3d
        L3c:
            r0 = 0
        L3d:
            if (r0 == 0) goto L40
            return
        L40:
            boolean r0 = r4.b(r5)     // Catch: java.lang.Throwable -> Lc8
            if (r0 != 0) goto L47
            return
        L47:
            r0 = 2
            r4.a = r0     // Catch: java.lang.Throwable -> Lc8
            com.amap.api.mapcore.util.ko r3 = new com.amap.api.mapcore.util.ko     // Catch: java.lang.Throwable -> Lc8
            r3.<init>(r0, r1)     // Catch: java.lang.Throwable -> Lc8
            r0 = r6[r2]     // Catch: java.lang.Throwable -> Lc8
            int r0 = java.lang.Integer.parseInt(r0)     // Catch: java.lang.Throwable -> Lc8
            r3.a = r0     // Catch: java.lang.Throwable -> Lc8
            r6 = r6[r1]     // Catch: java.lang.Throwable -> Lc8
            int r6 = java.lang.Integer.parseInt(r6)     // Catch: java.lang.Throwable -> Lc8
            r3.b = r6     // Catch: java.lang.Throwable -> Lc8
            java.lang.String r6 = "getSystemId"
            java.lang.Object[] r0 = new java.lang.Object[r2]     // Catch: java.lang.Throwable -> Lc8
            int r6 = com.amap.api.mapcore.util.ky.b(r5, r6, r0)     // Catch: java.lang.Throwable -> Lc8
            r3.g = r6     // Catch: java.lang.Throwable -> Lc8
            java.lang.String r6 = "getNetworkId"
            java.lang.Object[] r0 = new java.lang.Object[r2]     // Catch: java.lang.Throwable -> Lc8
            int r6 = com.amap.api.mapcore.util.ky.b(r5, r6, r0)     // Catch: java.lang.Throwable -> Lc8
            r3.h = r6     // Catch: java.lang.Throwable -> Lc8
            java.lang.String r6 = "getBaseStationId"
            java.lang.Object[] r0 = new java.lang.Object[r2]     // Catch: java.lang.Throwable -> Lc8
            int r6 = com.amap.api.mapcore.util.ky.b(r5, r6, r0)     // Catch: java.lang.Throwable -> Lc8
            r3.i = r6     // Catch: java.lang.Throwable -> Lc8
            int r6 = r4.o     // Catch: java.lang.Throwable -> Lc8
            r3.j = r6     // Catch: java.lang.Throwable -> Lc8
            java.lang.String r6 = "getBaseStationLatitude"
            java.lang.Object[] r0 = new java.lang.Object[r2]     // Catch: java.lang.Throwable -> Lc8
            int r6 = com.amap.api.mapcore.util.ky.b(r5, r6, r0)     // Catch: java.lang.Throwable -> Lc8
            r3.e = r6     // Catch: java.lang.Throwable -> Lc8
            java.lang.String r6 = "getBaseStationLongitude"
            java.lang.Object[] r0 = new java.lang.Object[r2]     // Catch: java.lang.Throwable -> Lc8
            int r5 = com.amap.api.mapcore.util.ky.b(r5, r6, r0)     // Catch: java.lang.Throwable -> Lc8
            r3.f = r5     // Catch: java.lang.Throwable -> Lc8
            int r5 = r3.e     // Catch: java.lang.Throwable -> Lc8
            int r6 = r3.f     // Catch: java.lang.Throwable -> Lc8
            if (r5 != r6) goto La0
            int r5 = r3.e     // Catch: java.lang.Throwable -> Lc8
            if (r5 <= 0) goto La0
            goto La1
        La0:
            r1 = 0
        La1:
            int r5 = r3.e     // Catch: java.lang.Throwable -> Lc8
            if (r5 < 0) goto Lb6
            int r5 = r3.f     // Catch: java.lang.Throwable -> Lc8
            if (r5 < 0) goto Lb6
            int r5 = r3.e     // Catch: java.lang.Throwable -> Lc8
            r6 = 2147483647(0x7fffffff, float:NaN)
            if (r5 == r6) goto Lb6
            int r5 = r3.f     // Catch: java.lang.Throwable -> Lc8
            if (r5 == r6) goto Lb6
            if (r1 == 0) goto Lba
        Lb6:
            r3.e = r2     // Catch: java.lang.Throwable -> Lc8
            r3.f = r2     // Catch: java.lang.Throwable -> Lc8
        Lba:
            java.util.ArrayList<com.amap.api.mapcore.util.ko> r5 = r4.b     // Catch: java.lang.Throwable -> Lc8
            boolean r5 = r5.contains(r3)     // Catch: java.lang.Throwable -> Lc8
            if (r5 != 0) goto Lc7
            java.util.ArrayList<com.amap.api.mapcore.util.ko> r5 = r4.b     // Catch: java.lang.Throwable -> Lc8
            r5.add(r3)     // Catch: java.lang.Throwable -> Lc8
        Lc7:
            return
        Lc8:
            r5 = move-exception
            java.lang.String r6 = "CgiManager"
            java.lang.String r0 = "hdlCdmaLocChange"
            com.amap.api.mapcore.util.kw.a(r5, r6, r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.kp.b(android.telephony.CellLocation, java.lang.String[]):void");
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
            kw.a(th, "Utils", "getCellLocT");
            return 0;
        }
    }

    private ko c(CellLocation cellLocation, String[] strArr) {
        GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
        ko koVar = new ko(1, true);
        koVar.a = la.d(strArr[0]);
        koVar.b = la.d(strArr[1]);
        koVar.c = gsmCellLocation.getLac();
        koVar.d = gsmCellLocation.getCid();
        koVar.j = this.o;
        return koVar;
    }

    private static boolean c(int i) {
        return (i == -1 || i == 0 || i > 65535) ? false : true;
    }

    private static boolean d(int i) {
        return (i == -1 || i == 0 || i == 65535 || i >= 268435455) ? false : true;
    }

    private void j() {
        Object a2;
        if (this.c == null) {
            return;
        }
        try {
            this.a = c(this.c.getCellLocation());
        } catch (SecurityException e) {
            this.h = e.getMessage();
        } catch (Throwable th) {
            this.h = null;
            kw.a(th, "CgiManager", "CgiManager");
            this.a = 0;
        }
        try {
            this.r = u();
            switch (this.r) {
                case 1:
                    a2 = la.a(this.l, "phone_msim");
                    break;
                case 2:
                    a2 = la.a(this.l, "phone2");
                    break;
                default:
                    a2 = la.a(this.l, "phone2");
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

    /* JADX INFO: Access modifiers changed from: private */
    public void k() {
        int i;
        this.g = new PhoneStateListener() { // from class: com.amap.api.mapcore.util.kp.1
            @Override // android.telephony.PhoneStateListener
            public final void onCellLocationChanged(CellLocation cellLocation) {
                try {
                    if (kp.this.a(cellLocation)) {
                        kp.this.e = cellLocation;
                        kp.this.f = true;
                        kp.this.s = la.b();
                    }
                } catch (Throwable unused) {
                }
            }

            @Override // android.telephony.PhoneStateListener
            public final void onServiceStateChanged(ServiceState serviceState) {
                try {
                    switch (serviceState.getState()) {
                        case 0:
                            kp.this.f();
                            return;
                        case 1:
                            kp.this.h();
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
                    switch (kp.this.a) {
                        case 1:
                        case 2:
                            i3 = la.a(i2);
                            break;
                    }
                    kp.this.b(i3);
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
                    switch (kp.this.a) {
                        case 1:
                            i2 = la.a(signalStrength.getGsmSignalStrength());
                            break;
                        case 2:
                            i2 = signalStrength.getCdmaDbm();
                            break;
                    }
                    kp.this.b(i2);
                } catch (Throwable unused) {
                }
            }
        };
        try {
            i = ky.b("android.telephony.PhoneStateListener", la.c() < 7 ? "LISTEN_SIGNAL_STRENGTH" : "LISTEN_SIGNAL_STRENGTHS");
        } catch (Throwable unused) {
            i = 0;
        }
        if (i == 0) {
            try {
                this.c.listen(this.g, 16);
            } catch (Throwable unused2) {
            }
        } else {
            try {
                this.c.listen(this.g, i | 16);
            } catch (Throwable unused3) {
            }
        }
    }

    private CellLocation l() {
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
                kw.a(th, "CgiManager", "getCellLocation");
            }
        }
        return null;
    }

    private boolean m() {
        return !this.i && la.b() - this.d >= 10000;
    }

    private void n() {
        h();
    }

    private void o() {
        switch (d()) {
            case 1:
                if (this.b.isEmpty()) {
                    this.a = 0;
                    return;
                }
                return;
            case 2:
                if (this.b.isEmpty()) {
                    this.a = 0;
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void p() {
        if (!this.i && this.c != null) {
            CellLocation q = q();
            if (!b(q)) {
                q = r();
            }
            if (b(q)) {
                this.e = q;
                this.s = la.b();
            } else if (la.b() - this.s > DateUtils.MILLIS_PER_MINUTE) {
                this.e = null;
                this.b.clear();
                this.n.clear();
            }
        }
        this.f = true;
        if (b(this.e)) {
            String[] a2 = la.a(this.c);
            switch (c(this.e)) {
                case 1:
                    a(this.e, a2);
                    break;
                case 2:
                    b(this.e, a2);
                    break;
            }
        }
        try {
            if (la.c() >= 18) {
                t();
            }
        } catch (Throwable unused) {
        }
        if (this.c != null) {
            this.m = this.c.getNetworkOperator();
            if (TextUtils.isEmpty(this.m)) {
                return;
            }
            this.a |= 8;
        }
    }

    @SuppressLint({"NewApi"})
    private CellLocation q() {
        TelephonyManager telephonyManager = this.c;
        CellLocation cellLocation = null;
        if (telephonyManager == null) {
            return null;
        }
        CellLocation l = l();
        if (b(l)) {
            return l;
        }
        if (la.c() >= 18) {
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

    private CellLocation r() {
        Object obj = this.q;
        CellLocation cellLocation = null;
        if (obj == null) {
            return null;
        }
        try {
            Class<?> s = s();
            if (s.isInstance(obj)) {
                Object cast = s.cast(obj);
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
                        kw.a(th, "CgiManager", "getSim2Cgi");
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

    private Class<?> s() {
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
            kw.a(th, "CgiManager", "getSim2TmClass");
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x002a  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0030  */
    @android.annotation.SuppressLint({"NewApi"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void t() {
        /*
            r11 = this;
            android.telephony.TelephonyManager r0 = r11.c
            if (r0 != 0) goto L5
            return
        L5:
            java.util.ArrayList<com.amap.api.mapcore.util.ko> r0 = r11.n
            com.amap.api.mapcore.util.kn r1 = r11.p
            r2 = 0
            android.telephony.TelephonyManager r3 = r11.c     // Catch: java.lang.SecurityException -> L19
            java.util.List r3 = r3.getAllCellInfo()     // Catch: java.lang.SecurityException -> L19
            r11.h = r2     // Catch: java.lang.SecurityException -> L14
            r2 = r3
            goto L20
        L14:
            r2 = move-exception
            r10 = r3
            r3 = r2
            r2 = r10
            goto L1a
        L19:
            r3 = move-exception
        L1a:
            java.lang.String r3 = r3.getMessage()
            r11.h = r3
        L20:
            if (r2 == 0) goto Lc3
            int r3 = r2.size()
            if (r3 == 0) goto Lc3
            if (r0 == 0) goto L2d
            r0.clear()
        L2d:
            r4 = 0
        L2e:
            if (r4 >= r3) goto Lc3
            java.lang.Object r5 = r2.get(r4)
            android.telephony.CellInfo r5 = (android.telephony.CellInfo) r5
            if (r5 == 0) goto Lbf
            boolean r6 = r5.isRegistered()     // Catch: java.lang.Throwable -> Lbf
            boolean r7 = r5 instanceof android.telephony.CellInfoCdma     // Catch: java.lang.Throwable -> Lbf
            r8 = 65535(0xffff, double:3.23786E-319)
            if (r7 == 0) goto L65
            android.telephony.CellInfoCdma r5 = (android.telephony.CellInfoCdma) r5     // Catch: java.lang.Throwable -> Lbf
            android.telephony.CellIdentityCdma r7 = r5.getCellIdentity()     // Catch: java.lang.Throwable -> Lbf
            boolean r7 = a(r7)     // Catch: java.lang.Throwable -> Lbf
            if (r7 != 0) goto L51
            goto Lbf
        L51:
            com.amap.api.mapcore.util.ko r5 = r11.a(r5, r6)     // Catch: java.lang.Throwable -> Lbf
            long r6 = r1.a(r5)     // Catch: java.lang.Throwable -> Lbf
            long r6 = java.lang.Math.min(r8, r6)     // Catch: java.lang.Throwable -> Lbf
        L5d:
            int r6 = (int) r6     // Catch: java.lang.Throwable -> Lbf
            short r6 = (short) r6     // Catch: java.lang.Throwable -> Lbf
            r5.l = r6     // Catch: java.lang.Throwable -> Lbf
            r0.add(r5)     // Catch: java.lang.Throwable -> Lbf
            goto Lbf
        L65:
            boolean r7 = r5 instanceof android.telephony.CellInfoGsm     // Catch: java.lang.Throwable -> Lbf
            if (r7 == 0) goto L83
            android.telephony.CellInfoGsm r5 = (android.telephony.CellInfoGsm) r5     // Catch: java.lang.Throwable -> Lbf
            android.telephony.CellIdentityGsm r7 = r5.getCellIdentity()     // Catch: java.lang.Throwable -> Lbf
            boolean r7 = a(r7)     // Catch: java.lang.Throwable -> Lbf
            if (r7 != 0) goto L76
            goto Lbf
        L76:
            com.amap.api.mapcore.util.ko r5 = a(r5, r6)     // Catch: java.lang.Throwable -> Lbf
            long r6 = r1.a(r5)     // Catch: java.lang.Throwable -> Lbf
            long r6 = java.lang.Math.min(r8, r6)     // Catch: java.lang.Throwable -> Lbf
            goto L5d
        L83:
            boolean r7 = r5 instanceof android.telephony.CellInfoWcdma     // Catch: java.lang.Throwable -> Lbf
            if (r7 == 0) goto La1
            android.telephony.CellInfoWcdma r5 = (android.telephony.CellInfoWcdma) r5     // Catch: java.lang.Throwable -> Lbf
            android.telephony.CellIdentityWcdma r7 = r5.getCellIdentity()     // Catch: java.lang.Throwable -> Lbf
            boolean r7 = a(r7)     // Catch: java.lang.Throwable -> Lbf
            if (r7 != 0) goto L94
            goto Lbf
        L94:
            com.amap.api.mapcore.util.ko r5 = a(r5, r6)     // Catch: java.lang.Throwable -> Lbf
            long r6 = r1.a(r5)     // Catch: java.lang.Throwable -> Lbf
            long r6 = java.lang.Math.min(r8, r6)     // Catch: java.lang.Throwable -> Lbf
            goto L5d
        La1:
            boolean r7 = r5 instanceof android.telephony.CellInfoLte     // Catch: java.lang.Throwable -> Lbf
            if (r7 == 0) goto Lbf
            android.telephony.CellInfoLte r5 = (android.telephony.CellInfoLte) r5     // Catch: java.lang.Throwable -> Lbf
            android.telephony.CellIdentityLte r7 = r5.getCellIdentity()     // Catch: java.lang.Throwable -> Lbf
            boolean r7 = a(r7)     // Catch: java.lang.Throwable -> Lbf
            if (r7 != 0) goto Lb2
            goto Lbf
        Lb2:
            com.amap.api.mapcore.util.ko r5 = a(r5, r6)     // Catch: java.lang.Throwable -> Lbf
            long r6 = r1.a(r5)     // Catch: java.lang.Throwable -> Lbf
            long r6 = java.lang.Math.min(r8, r6)     // Catch: java.lang.Throwable -> Lbf
            goto L5d
        Lbf:
            int r4 = r4 + 1
            goto L2e
        Lc3:
            if (r0 == 0) goto Ld4
            int r2 = r0.size()
            if (r2 <= 0) goto Ld4
            int r2 = r11.a
            r2 = r2 | 4
            r11.a = r2
            r1.a(r0)
        Ld4:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.kp.t():void");
    }

    private int u() {
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

    public final ArrayList<ko> a() {
        return this.b;
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
                    if (ky.b(cellLocation, "getSystemId", new Object[0]) > 0 && ky.b(cellLocation, "getNetworkId", new Object[0]) >= 0) {
                        if (ky.b(cellLocation, "getBaseStationId", new Object[0]) >= 0) {
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
        kw.a(th, str, str2);
        return true;
    }

    public final ArrayList<ko> b() {
        return this.n;
    }

    public final int c() {
        return this.a;
    }

    public final int d() {
        return this.a & 3;
    }

    public final TelephonyManager e() {
        return this.c;
    }

    public final void f() {
        try {
            this.i = la.a(this.l);
            if (m() || this.b.isEmpty()) {
                p();
                this.d = la.b();
            }
            if (this.i) {
                n();
            } else {
                o();
            }
        } catch (SecurityException e) {
            this.h = e.getMessage();
        } catch (Throwable th) {
            kw.a(th, "CgiManager", "refresh");
        }
    }

    public final void g() {
        this.p.a();
        this.s = 0L;
        synchronized (this.u) {
            this.t = true;
        }
        if (this.c != null && this.g != null) {
            try {
                this.c.listen(this.g, 0);
            } catch (Throwable th) {
                kw.a(th, "CgiManager", "destroy");
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

    final void h() {
        this.h = null;
        this.e = null;
        this.a = 0;
        this.b.clear();
        this.n.clear();
    }

    public final String i() {
        return this.m;
    }
}
