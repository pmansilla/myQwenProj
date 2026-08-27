package com.loc;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.view.PointerIconCompat;
import android.text.TextUtils;
import android.webkit.WebView;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.amap.api.location.APSService;
import com.amap.api.location.LocationManagerBase;
import com.amap.api.location.UmidtokenInfo;
import com.amap.location.common.model.AmapLoc;
import com.autonavi.aps.amapapi.model.AMapLocationServer;
import com.example.otalib.boads.Constant;
import java.util.ArrayList;
import java.util.Iterator;

/* compiled from: AmapLocationManager.java */
/* loaded from: classes.dex */
public final class m implements LocationManagerBase {
    private static boolean E = false;
    public c b;
    p c;
    r g;
    Intent j;
    b l;
    ey p;
    a w;
    private Context z;
    AMapLocationClientOption a = new AMapLocationClientOption();
    private boolean A = false;
    private volatile boolean B = false;
    ArrayList<AMapLocationListener> d = new ArrayList<>();
    boolean e = false;
    private boolean C = true;
    public boolean f = true;
    Messenger h = null;
    Messenger i = null;
    int k = 0;
    private boolean D = true;
    boolean m = false;
    AMapLocationClientOption.AMapLocationMode n = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy;
    Object o = new Object();
    boolean q = false;
    n r = null;
    private q F = null;
    String s = null;
    private ServiceConnection G = new ServiceConnection() { // from class: com.loc.m.1
        @Override // android.content.ServiceConnection
        public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                m.this.h = new Messenger(iBinder);
                m.this.A = true;
                m.this.q = true;
            } catch (Throwable th) {
                es.a(th, "AmapLocationManager", "onServiceConnected");
            }
        }

        @Override // android.content.ServiceConnection
        public final void onServiceDisconnected(ComponentName componentName) {
            m.this.h = null;
            m.this.A = false;
        }
    };
    AMapLocationQualityReport t = null;
    boolean u = false;
    boolean v = false;
    String x = null;
    boolean y = false;

    /* compiled from: AmapLocationManager.java */
    /* loaded from: classes.dex */
    public class a extends Handler {
        public a(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            try {
                super.handleMessage(message);
                switch (message.what) {
                    case 1002:
                        try {
                            m.a(m.this, (AMapLocationListener) message.obj);
                            return;
                        } catch (Throwable th) {
                            es.a(th, "AMapLocationManage$MHandlerr", "handleMessage SET_LISTENER");
                            return;
                        }
                    case 1003:
                        try {
                            m.this.c();
                            return;
                        } catch (Throwable th2) {
                            es.a(th2, "AMapLocationManage$MHandlerr", "handleMessage START_LOCATION");
                            return;
                        }
                    case 1004:
                        try {
                            m.this.d();
                            return;
                        } catch (Throwable th3) {
                            es.a(th3, "AMapLocationManage$MHandlerr", "handleMessage STOP_LOCATION");
                            return;
                        }
                    case Constant.MSG_WHAT_READ_PART /* 1005 */:
                        try {
                            m.b(m.this, (AMapLocationListener) message.obj);
                            return;
                        } catch (Throwable th4) {
                            es.a(th4, "AMapLocationManage$MHandlerr", "handleMessage REMOVE_LISTENER");
                            return;
                        }
                    case 1006:
                    case 1007:
                        return;
                    case 1008:
                        try {
                            m.g(m.this);
                            return;
                        } catch (Throwable th5) {
                            es.a(th5, "AMapLocationManage$MHandlerr", "handleMessage START_SOCKET");
                            return;
                        }
                    case PointerIconCompat.TYPE_VERTICAL_TEXT /* 1009 */:
                        try {
                            m.h(m.this);
                            return;
                        } catch (Throwable th6) {
                            es.a(th6, "AMapLocationManage$MHandlerr", "handleMessage STOP_SOCKET");
                            return;
                        }
                    case PointerIconCompat.TYPE_ALIAS /* 1010 */:
                        return;
                    case 1011:
                        try {
                            m.this.a();
                            return;
                        } catch (Throwable th7) {
                            es.a(th7, "AMapLocationManage$MHandlerr", "handleMessage DESTROY");
                            return;
                        }
                    case PointerIconCompat.TYPE_NO_DROP /* 1012 */:
                    case PointerIconCompat.TYPE_ALL_SCROLL /* 1013 */:
                    default:
                        return;
                    case PointerIconCompat.TYPE_HORIZONTAL_DOUBLE_ARROW /* 1014 */:
                        m.b(m.this, message);
                        return;
                    case PointerIconCompat.TYPE_VERTICAL_DOUBLE_ARROW /* 1015 */:
                        try {
                            m.this.c.a(m.this.a);
                            m.this.a(1025, (Object) null, 300000L);
                            return;
                        } catch (Throwable th8) {
                            es.a(th8, "AMapLocationManage$MHandlerr", "handleMessage START_GPS_LOCATION");
                            return;
                        }
                    case PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW /* 1016 */:
                        try {
                            if (m.this.c.b()) {
                                m.this.a(PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW, (Object) null, 1000L);
                                return;
                            } else {
                                m.d(m.this);
                                return;
                            }
                        } catch (Throwable th9) {
                            es.a(th9, "AMapLocationManage$MHandlerr", "handleMessage START_LBS_LOCATION");
                            return;
                        }
                    case PointerIconCompat.TYPE_TOP_LEFT_DIAGONAL_DOUBLE_ARROW /* 1017 */:
                        try {
                            m.this.c.a();
                            m.this.a(1025);
                            return;
                        } catch (Throwable th10) {
                            es.a(th10, "AMapLocationManage$MHandlerr", "handleMessage STOP_GPS_LOCATION");
                            return;
                        }
                    case PointerIconCompat.TYPE_ZOOM_IN /* 1018 */:
                        try {
                            m.this.a = (AMapLocationClientOption) message.obj;
                            if (m.this.a != null) {
                                m.f(m.this);
                                return;
                            }
                            return;
                        } catch (Throwable th11) {
                            es.a(th11, "AMapLocationManage$MHandlerr", "handleMessage SET_OPTION");
                            return;
                        }
                    case PointerIconCompat.TYPE_ZOOM_OUT /* 1019 */:
                    case PointerIconCompat.TYPE_GRAB /* 1020 */:
                    case 1021:
                    case 1022:
                        return;
                    case 1023:
                        try {
                            m.c(m.this, message);
                            return;
                        } catch (Throwable th12) {
                            es.a(th12, "AMapLocationManage$MHandlerr", "handleMessage ACTION_ENABLE_BACKGROUND");
                            return;
                        }
                    case 1024:
                        try {
                            m.d(m.this, message);
                            return;
                        } catch (Throwable th13) {
                            es.a(th13, "AMapLocationManage$MHandlerr", "handleMessage ACTION_DISABLE_BACKGROUND");
                            return;
                        }
                    case 1025:
                        try {
                            if (m.this.c != null) {
                                if (fa.c() - m.this.c.d > 300000) {
                                    m.this.c.a();
                                    m.this.c.a(m.this.a);
                                }
                                m.this.a(1025, (Object) null, 300000L);
                                return;
                            }
                            return;
                        } catch (Throwable th14) {
                            es.a(th14, "AMapLocationManage$MHandlerr", "handleMessage ACTION_REBOOT_GPS_LOCATION");
                            return;
                        }
                }
            } catch (Throwable th15) {
                es.a(th15, "AMapLocationManage$MHandlerr", "handleMessage");
            }
            es.a(th15, "AMapLocationManage$MHandlerr", "handleMessage");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AmapLocationManager.java */
    /* loaded from: classes.dex */
    public static class b extends HandlerThread {
        m a;

        public b(String str, m mVar) {
            super(str);
            this.a = null;
            this.a = mVar;
        }

        @Override // android.os.HandlerThread
        protected final void onLooperPrepared() {
            try {
                this.a.g.a();
                this.a.f();
                super.onLooperPrepared();
            } catch (Throwable unused) {
            }
        }

        @Override // android.os.HandlerThread, java.lang.Thread, java.lang.Runnable
        public final void run() {
            try {
                super.run();
            } catch (Throwable unused) {
            }
        }
    }

    /* compiled from: AmapLocationManager.java */
    /* loaded from: classes.dex */
    public class c extends Handler {
        public c() {
        }

        public c(Looper looper) {
            super(looper);
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Failed to find 'out' block for switch in B:9:0x0012. Please report as an issue. */
        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            try {
                super.handleMessage(message);
                if (!m.this.m || es.d()) {
                    switch (message.what) {
                        case 1:
                            try {
                                m.a(m.this, message.getData());
                                return;
                            } catch (Throwable th) {
                                es.a(th, "AmapLocationManager$ActionHandler", "handleMessage RESULT_LBS_LOCATIONSUCCESS");
                                return;
                            }
                        case 2:
                            try {
                                m.a(m.this, message);
                                return;
                            } catch (Throwable th2) {
                                es.a(th2, "AmapLocationManager$ActionHandler", "handleMessage RESULT_GPS_LOCATIONSUCCESS");
                                return;
                            }
                        case 3:
                            return;
                        case 4:
                        default:
                            return;
                        case 5:
                            try {
                                Bundle data = message.getData();
                                data.putBundle("optBundle", es.a(m.this.a));
                                m.this.a(10, data);
                                return;
                            } catch (Throwable th3) {
                                es.a(th3, "AmapLocationManager$ActionHandler", "handleMessage RESULT_GPS_LOCATIONCHANGE");
                                return;
                            }
                        case 6:
                            try {
                                Bundle data2 = message.getData();
                                if (m.this.c != null) {
                                    p pVar = m.this.c;
                                    if (data2 != null) {
                                        try {
                                            data2.setClassLoader(AMapLocation.class.getClassLoader());
                                            pVar.g = data2.getInt("I_MAX_GEO_DIS");
                                            pVar.h = data2.getInt("I_MIN_GEO_DIS");
                                            AMapLocation aMapLocation = (AMapLocation) data2.getParcelable("loc");
                                            if (TextUtils.isEmpty(aMapLocation.getAdCode())) {
                                                return;
                                            }
                                            synchronized (pVar.o) {
                                                pVar.y = aMapLocation;
                                            }
                                            return;
                                        } catch (Throwable th4) {
                                            es.a(th4, "GpsLocation", "setLastGeoLocation");
                                            return;
                                        }
                                    }
                                    return;
                                }
                                return;
                            } catch (Throwable th5) {
                                es.a(th5, "AmapLocationManager$ActionHandler", "handleMessage RESULT_GPS_GEO_SUCCESS");
                                return;
                            }
                        case 7:
                            try {
                                Bundle data3 = message.getData();
                                m.this.D = data3.getBoolean("ngpsAble");
                                return;
                            } catch (Throwable th6) {
                                es.a(th6, "AmapLocationManager$ActionHandler", "handleMessage RESULT_NGPS_ABLE");
                                return;
                            }
                        case 8:
                            ey.a((String) null, 2141);
                            m.a(m.this, message);
                            return;
                        case 9:
                            try {
                                boolean unused = m.E = message.getData().getBoolean("installMockApp");
                                return;
                            } catch (Throwable th7) {
                                es.a(th7, "AmapLocationManager$ActionHandler", "handleMessage RESULT_INSTALLED_MOCK_APP");
                                return;
                            }
                    }
                }
            } catch (Throwable th8) {
                es.a(th8, "AmapLocationManager$MainHandler", "handleMessage");
            }
        }
    }

    public m(Context context, Intent intent) {
        this.c = null;
        this.j = null;
        this.l = null;
        this.p = null;
        this.w = null;
        this.z = context;
        this.j = intent;
        try {
            this.b = Looper.myLooper() == null ? new c(this.z.getMainLooper()) : new c();
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "init 1");
        }
        try {
            try {
                this.g = new r(this.z);
            } catch (Throwable th2) {
                es.a(th2, "AmapLocationManager", "init 5");
            }
        } catch (Throwable th3) {
            es.a(th3, "AmapLocationManager", "init 2");
        }
        this.l = new b("amapLocManagerThread", this);
        this.l.setPriority(5);
        this.l.start();
        this.w = a(this.l.getLooper());
        try {
            this.c = new p(this.z, this.b);
        } catch (Throwable th4) {
            es.a(th4, "AmapLocationManager", "init 3");
        }
        if (this.p == null) {
            this.p = new ey();
        }
    }

    private AMapLocationServer a(cs csVar) {
        if (!this.a.isLocationCacheEnable()) {
            return null;
        }
        try {
            return csVar.j();
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "doFirstCacheLoc");
            return null;
        }
    }

    private a a(Looper looper) {
        a aVar;
        synchronized (this.o) {
            this.w = new a(looper);
            aVar = this.w;
        }
        return aVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i) {
        synchronized (this.o) {
            if (this.w != null) {
                this.w.removeMessages(i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i, Bundle bundle) {
        if (bundle == null) {
            try {
                bundle = new Bundle();
            } catch (Throwable th) {
                boolean z = (th instanceof IllegalStateException) && th.getMessage().contains("sending message to a Handler on a dead thread");
                if ((th instanceof RemoteException) || z) {
                    this.h = null;
                    this.A = false;
                }
                es.a(th, "AmapLocationManager", "sendLocMessage");
                return;
            }
        }
        if (TextUtils.isEmpty(this.s)) {
            this.s = es.b(this.z);
        }
        bundle.putString("c", this.s);
        Message obtain = Message.obtain();
        obtain.what = i;
        obtain.setData(bundle);
        obtain.replyTo = this.i;
        if (this.h != null) {
            this.h.send(obtain);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i, Object obj, long j) {
        synchronized (this.o) {
            if (this.w != null) {
                Message obtain = Message.obtain();
                obtain.what = i;
                if (obj instanceof Bundle) {
                    obtain.setData((Bundle) obj);
                } else {
                    obtain.obj = obj;
                }
                this.w.sendMessageDelayed(obtain, j);
            }
        }
    }

    private void a(Intent intent, boolean z) {
        if (this.z != null) {
            if (Build.VERSION.SDK_INT >= 26 && z) {
                try {
                    this.z.getClass().getMethod("startForegroundService", Intent.class).invoke(this.z, intent);
                } catch (Throwable unused) {
                }
                this.y = true;
            }
            this.z.startService(intent);
            this.y = true;
        }
    }

    private void a(AMapLocation aMapLocation) {
        try {
            if (aMapLocation.getErrorCode() != 0) {
                aMapLocation.setLocationType(0);
            }
            if (aMapLocation.getErrorCode() == 0) {
                double latitude = aMapLocation.getLatitude();
                double longitude = aMapLocation.getLongitude();
                if ((latitude == 0.0d && longitude == 0.0d) || latitude < -90.0d || latitude > 90.0d || longitude < -180.0d || longitude > 180.0d) {
                    ey.a("errorLatLng", aMapLocation.toStr());
                    aMapLocation.setLocationType(0);
                    aMapLocation.setErrorCode(8);
                    aMapLocation.setLocationDetail("LatLng is error#0802");
                }
            }
            if ("gps".equalsIgnoreCase(aMapLocation.getProvider()) || !this.c.b()) {
                aMapLocation.setAltitude(fa.b(aMapLocation.getAltitude()));
                aMapLocation.setBearing(fa.a(aMapLocation.getBearing()));
                aMapLocation.setSpeed(fa.a(aMapLocation.getSpeed()));
                Iterator<AMapLocationListener> it = this.d.iterator();
                while (it.hasNext()) {
                    try {
                        it.next().onLocationChanged(aMapLocation);
                    } catch (Throwable unused) {
                    }
                }
            }
        } catch (Throwable unused2) {
        }
    }

    private synchronized void a(AMapLocation aMapLocation, long j) {
        try {
            if (aMapLocation == null) {
                try {
                    aMapLocation = new AMapLocation("");
                    aMapLocation.setErrorCode(8);
                    aMapLocation.setLocationDetail("amapLocation is null#0801");
                } catch (Throwable th) {
                    es.a(th, "AmapLocationManager", "handlerLocation part3");
                    return;
                }
            }
            if (!"gps".equalsIgnoreCase(aMapLocation.getProvider())) {
                aMapLocation.setProvider("lbs");
            }
            if (this.t == null) {
                this.t = new AMapLocationQualityReport();
            }
            this.t.setLocationMode(this.a.getLocationMode());
            if (this.c != null) {
                this.t.setGPSSatellites(this.c.d());
                this.t.setGpsStatus(this.c.c());
            }
            this.t.setWifiAble(fa.h(this.z));
            this.t.setNetworkType(fa.i(this.z));
            if (aMapLocation.getLocationType() == 1 || "gps".equalsIgnoreCase(aMapLocation.getProvider())) {
                j = 0;
            }
            this.t.setNetUseTime(j);
            this.t.setInstallHighDangerMockApp(E);
            aMapLocation.setLocationQualityReport(this.t);
            try {
                if (this.B) {
                    String str = this.x;
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("loc", aMapLocation);
                    bundle.putString("lastLocNb", str);
                    a(PointerIconCompat.TYPE_HORIZONTAL_DOUBLE_ARROW, bundle, 0L);
                    ey.a(this.z, aMapLocation);
                    ey.b(this.z, aMapLocation);
                    a(aMapLocation.m9clone());
                }
            } catch (Throwable th2) {
                es.a(th2, "AmapLocationManager", "handlerLocation part2");
            }
            if (!this.m || es.d()) {
                if (this.a.isOnceLocation()) {
                    d();
                }
            }
        } catch (Throwable th3) {
            throw th3;
        }
    }

    private static void a(cs csVar, AMapLocationServer aMapLocationServer) {
        if (aMapLocationServer != null) {
            try {
                if (aMapLocationServer.getErrorCode() == 0) {
                    csVar.b(aMapLocationServer);
                }
            } catch (Throwable th) {
                es.a(th, "AmapLocationManager", "apsLocation:doFirstAddCache");
            }
        }
    }

    static /* synthetic */ void a(m mVar, Bundle bundle) {
        AMapLocation aMapLocation;
        long j = 0;
        AMapLocation aMapLocation2 = null;
        if (bundle != null) {
            try {
                bundle.setClassLoader(AMapLocation.class.getClassLoader());
                aMapLocation = (AMapLocation) bundle.getParcelable("loc");
                mVar.x = bundle.getString("nb");
                long j2 = bundle.getLong("netUseTime", 0L);
                if (aMapLocation != null) {
                    try {
                        if (aMapLocation.getErrorCode() == 0 && mVar.c != null) {
                            mVar.c.w = 0;
                            if (!TextUtils.isEmpty(aMapLocation.getAdCode())) {
                                mVar.c.y = aMapLocation;
                            }
                        }
                    } catch (Throwable th) {
                        th = th;
                        j = j2;
                        es.a(th, "AmapLocationManager", "resultLbsLocationSuccess");
                        mVar.a(aMapLocation2, j);
                    }
                }
                j = j2;
            } catch (Throwable th2) {
                th = th2;
            }
        } else {
            aMapLocation = null;
        }
        aMapLocation2 = mVar.c != null ? mVar.c.a(aMapLocation, mVar.x) : aMapLocation;
        mVar.a(aMapLocation2, j);
    }

    static /* synthetic */ void a(m mVar, Message message) {
        try {
            AMapLocation aMapLocation = (AMapLocation) message.obj;
            if (mVar.f && mVar.h != null) {
                Bundle bundle = new Bundle();
                bundle.putBundle("optBundle", es.a(mVar.a));
                mVar.a(0, bundle);
                mVar.f = false;
            }
            mVar.a(aMapLocation, 0L);
            if (mVar.D) {
                mVar.a(7, (Bundle) null);
            }
            mVar.a(1025);
            mVar.a(1025, (Object) null, 300000L);
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "resultGpsLocationSuccess");
        }
    }

    static /* synthetic */ void a(m mVar, AMapLocationListener aMapLocationListener) {
        if (aMapLocationListener == null) {
            throw new IllegalArgumentException("listener参数不能为null");
        }
        if (mVar.d == null) {
            mVar.d = new ArrayList<>();
        }
        if (mVar.d.contains(aMapLocationListener)) {
            return;
        }
        mVar.d.add(aMapLocationListener);
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x0090 A[Catch: Throwable -> 0x0110, all -> 0x0112, TRY_LEAVE, TryCatch #2 {all -> 0x0112, blocks: (B:3:0x0001, B:5:0x000e, B:7:0x0018, B:10:0x0026, B:12:0x0030, B:15:0x003c, B:79:0x004b, B:16:0x0052, B:21:0x0061, B:71:0x0067, B:24:0x0071, B:38:0x0084, B:40:0x0090, B:43:0x009d, B:45:0x00a5, B:47:0x00a9, B:51:0x00bf, B:53:0x00c6, B:54:0x00d9, B:55:0x00f0, B:59:0x00f9, B:62:0x0105, B:66:0x00e9, B:68:0x00b8, B:29:0x007a, B:31:0x0117, B:81:0x0035, B:83:0x001f), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00c6 A[Catch: Throwable -> 0x00e8, all -> 0x0112, TryCatch #2 {all -> 0x0112, blocks: (B:3:0x0001, B:5:0x000e, B:7:0x0018, B:10:0x0026, B:12:0x0030, B:15:0x003c, B:79:0x004b, B:16:0x0052, B:21:0x0061, B:71:0x0067, B:24:0x0071, B:38:0x0084, B:40:0x0090, B:43:0x009d, B:45:0x00a5, B:47:0x00a9, B:51:0x00bf, B:53:0x00c6, B:54:0x00d9, B:55:0x00f0, B:59:0x00f9, B:62:0x0105, B:66:0x00e9, B:68:0x00b8, B:29:0x007a, B:31:0x0117, B:81:0x0035, B:83:0x001f), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x009c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private com.autonavi.aps.amapapi.model.AMapLocationServer b(com.loc.cs r14) {
        /*
            Method dump skipped, instructions count: 292
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.m.b(com.loc.cs):com.autonavi.aps.amapapi.model.AMapLocationServer");
    }

    static /* synthetic */ void b(m mVar, Message message) {
        try {
            Bundle data = message.getData();
            AMapLocation aMapLocation = (AMapLocation) data.getParcelable("loc");
            String string = data.getString("lastLocNb");
            if (aMapLocation != null) {
                AMapLocation aMapLocation2 = null;
                try {
                    if (r.b != null) {
                        aMapLocation2 = r.b.a();
                    } else if (mVar.g != null) {
                        aMapLocation2 = mVar.g.b();
                    }
                    ey.a(aMapLocation2, aMapLocation);
                } catch (Throwable unused) {
                }
            }
            if (mVar.g.a(aMapLocation, string)) {
                mVar.g.d();
            }
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "doSaveLastLocation");
        }
    }

    static /* synthetic */ void b(m mVar, AMapLocationListener aMapLocationListener) {
        if (!mVar.d.isEmpty() && mVar.d.contains(aMapLocationListener)) {
            mVar.d.remove(aMapLocationListener);
        }
        if (mVar.d.isEmpty()) {
            mVar.d();
        }
    }

    private boolean b() {
        boolean z = false;
        int i = 0;
        while (this.h == null) {
            try {
                Thread.sleep(100L);
                i++;
                if (i >= 50) {
                    break;
                }
            } catch (Throwable th) {
                es.a(th, "AmapLocationManager", "checkAPSManager");
            }
        }
        if (this.h == null) {
            Message obtain = Message.obtain();
            Bundle bundle = new Bundle();
            AMapLocation aMapLocation = new AMapLocation("");
            aMapLocation.setErrorCode(10);
            aMapLocation.setLocationDetail(!fa.l(this.z.getApplicationContext()) ? "请检查配置文件是否配置服务，并且manifest中service标签是否配置在application标签内#1003" : "启动ApsServcie失败#1001");
            bundle.putParcelable("loc", aMapLocation);
            obtain.setData(bundle);
            obtain.what = 1;
            this.b.sendMessage(obtain);
        } else {
            z = true;
        }
        if (!z) {
            ey.a((String) null, !fa.l(this.z.getApplicationContext()) ? 2103 : 2101);
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void c() {
        if (this.a == null) {
            this.a = new AMapLocationClientOption();
        }
        if (this.B) {
            return;
        }
        this.B = true;
        long j = 0;
        switch (this.a.getLocationMode()) {
            case Battery_Saving:
                a(PointerIconCompat.TYPE_TOP_LEFT_DIAGONAL_DOUBLE_ARROW, (Object) null, 0L);
                a(PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW, (Object) null, 0L);
                return;
            case Device_Sensors:
                a(PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW);
                a(PointerIconCompat.TYPE_VERTICAL_DOUBLE_ARROW, (Object) null, 0L);
                return;
            case Hight_Accuracy:
                a(PointerIconCompat.TYPE_VERTICAL_DOUBLE_ARROW, (Object) null, 0L);
                if (this.a.isGpsFirst() && this.a.isOnceLocation()) {
                    j = this.a.getGpsFirstTimeout();
                }
                a(PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW, (Object) null, j);
                break;
        }
    }

    static /* synthetic */ void c(m mVar, Message message) {
        if (message == null) {
            return;
        }
        try {
            Bundle data = message.getData();
            if (data == null) {
                return;
            }
            int i = data.getInt("i", 0);
            Notification notification = (Notification) data.getParcelable("h");
            Intent g = mVar.g();
            g.putExtra("i", i);
            g.putExtra("h", notification);
            g.putExtra("g", 1);
            mVar.a(g, true);
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "doEnableBackgroundLocation");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        try {
            a(1025);
            if (this.c != null) {
                this.c.a();
            }
            a(PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW);
            this.B = false;
            this.k = 0;
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "stopLocation");
        }
    }

    static /* synthetic */ void d(m mVar) {
        try {
            try {
                if (mVar.C) {
                    mVar.C = false;
                    AMapLocationServer b2 = mVar.b(new cs());
                    if (mVar.b()) {
                        Bundle bundle = new Bundle();
                        String str = AmapLoc.RESULT_TYPE_GPS;
                        if (b2 != null && (b2.getLocationType() == 2 || b2.getLocationType() == 4)) {
                            str = AmapLoc.RESULT_TYPE_WIFI_ONLY;
                        }
                        bundle.putBundle("optBundle", es.a(mVar.a));
                        bundle.putString("isCacheLoc", str);
                        mVar.a(0, bundle);
                    }
                } else {
                    try {
                        if (mVar.q && !mVar.isStarted() && !mVar.v) {
                            mVar.v = true;
                            mVar.f();
                        }
                    } catch (Throwable th) {
                        mVar.v = true;
                        es.a(th, "AmapLocationManager", "doLBSLocation reStartService");
                    }
                    if (mVar.b()) {
                        mVar.v = false;
                        Bundle bundle2 = new Bundle();
                        bundle2.putBundle("optBundle", es.a(mVar.a));
                        bundle2.putString("d", UmidtokenInfo.getUmidtoken());
                        if (!mVar.c.b()) {
                            mVar.a(1, bundle2);
                        }
                    }
                }
                try {
                    if (mVar.a.isOnceLocation()) {
                        return;
                    }
                    mVar.e();
                } catch (Throwable unused) {
                }
            } catch (Throwable th2) {
                es.a(th2, "AmapLocationManager", "doLBSLocation");
                try {
                    if (mVar.a.isOnceLocation()) {
                        return;
                    }
                    mVar.e();
                } catch (Throwable unused2) {
                }
            }
        } catch (Throwable th3) {
            try {
                if (!mVar.a.isOnceLocation()) {
                    mVar.e();
                }
            } catch (Throwable unused3) {
            }
            throw th3;
        }
    }

    static /* synthetic */ void d(m mVar, Message message) {
        if (message == null) {
            return;
        }
        try {
            Bundle data = message.getData();
            if (data == null) {
                return;
            }
            boolean z = data.getBoolean("j", true);
            Intent g = mVar.g();
            g.putExtra("j", z);
            g.putExtra("g", 2);
            mVar.a(g, false);
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "doDisableBackgroundLocation");
        }
    }

    private void e() {
        if (this.a.getLocationMode() != AMapLocationClientOption.AMapLocationMode.Device_Sensors) {
            a(PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW, (Object) null, this.a.getInterval() >= 1000 ? this.a.getInterval() : 1000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
        try {
            if (this.i == null) {
                this.i = new Messenger(this.b);
            }
            try {
                this.z.bindService(g(), this.G, 1);
            } catch (Throwable th) {
                es.a(th, "AmapLocationManager", "startServiceImpl");
            }
        } catch (Throwable unused) {
        }
    }

    static /* synthetic */ void f(m mVar) {
        ey eyVar;
        Context context;
        int i;
        p pVar = mVar.c;
        AMapLocationClientOption aMapLocationClientOption = mVar.a;
        if (aMapLocationClientOption == null) {
            aMapLocationClientOption = new AMapLocationClientOption();
        }
        pVar.c = aMapLocationClientOption;
        if (pVar.c.getLocationMode() != AMapLocationClientOption.AMapLocationMode.Device_Sensors && pVar.a != null) {
            pVar.a.removeMessages(8);
        }
        if (pVar.r != pVar.c.getGeoLanguage()) {
            synchronized (pVar.o) {
                pVar.y = null;
            }
        }
        pVar.r = pVar.c.getGeoLanguage();
        if (mVar.B && !mVar.a.getLocationMode().equals(mVar.n)) {
            mVar.d();
            mVar.c();
        }
        mVar.n = mVar.a.getLocationMode();
        if (mVar.p != null) {
            if (mVar.a.isOnceLocation()) {
                eyVar = mVar.p;
                context = mVar.z;
                i = 0;
            } else {
                eyVar = mVar.p;
                context = mVar.z;
                i = 1;
            }
            eyVar.a(context, i);
            mVar.p.a(mVar.z, mVar.a);
        }
    }

    private Intent g() {
        String str;
        if (this.j == null) {
            this.j = new Intent(this.z, (Class<?>) APSService.class);
        }
        try {
            str = !TextUtils.isEmpty(AMapLocationClientOption.getAPIKEY()) ? AMapLocationClientOption.getAPIKEY() : u.f(this.z);
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "startServiceImpl p2");
            str = "";
        }
        this.j.putExtra("a", str);
        this.j.putExtra("b", u.c(this.z));
        this.j.putExtra("d", UmidtokenInfo.getUmidtoken());
        this.j.putExtra("f", AMapLocationClientOption.isDownloadCoordinateConvertLibrary());
        return this.j;
    }

    static /* synthetic */ void g(m mVar) {
        try {
            if (mVar.h != null) {
                mVar.k = 0;
                Bundle bundle = new Bundle();
                bundle.putBundle("optBundle", es.a(mVar.a));
                mVar.a(2, bundle);
                return;
            }
            mVar.k++;
            if (mVar.k < 10) {
                mVar.a(1008, (Object) null, 50L);
            }
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "startAssistantLocationImpl");
        }
    }

    static /* synthetic */ void h(m mVar) {
        try {
            Bundle bundle = new Bundle();
            bundle.putBundle("optBundle", es.a(mVar.a));
            mVar.a(3, bundle);
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "stopAssistantLocationImpl");
        }
    }

    final void a() {
        a(12, (Bundle) null);
        this.C = true;
        this.f = true;
        this.A = false;
        this.q = false;
        d();
        if (this.p != null) {
            this.p.b(this.z);
        }
        ey.a(this.z);
        if (this.r != null) {
            this.r.d.sendEmptyMessage(11);
        } else if (this.G != null) {
            this.z.unbindService(this.G);
        }
        try {
            if (this.y) {
                this.z.stopService(g());
            }
        } catch (Throwable unused) {
        }
        this.y = false;
        if (this.d != null) {
            this.d.clear();
            this.d = null;
        }
        this.G = null;
        synchronized (this.o) {
            if (this.w != null) {
                this.w.removeCallbacksAndMessages(null);
            }
            this.w = null;
        }
        if (this.l != null) {
            if (Build.VERSION.SDK_INT >= 18) {
                try {
                    ew.a(this.l, (Class<?>) HandlerThread.class, "quitSafely", new Object[0]);
                } catch (Throwable unused2) {
                }
            }
            this.l.quit();
        }
        this.l = null;
        if (this.b != null) {
            this.b.removeCallbacksAndMessages(null);
        }
        if (this.g != null) {
            this.g.c();
            this.g = null;
        }
    }

    @Override // com.amap.api.location.LocationManagerBase
    public final void disableBackgroundLocation(boolean z) {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean("j", z);
            a(1024, bundle, 0L);
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "disableBackgroundLocation");
        }
    }

    @Override // com.amap.api.location.LocationManagerBase
    public final void enableBackgroundLocation(int i, Notification notification) {
        if (i == 0 || notification == null) {
            return;
        }
        try {
            Bundle bundle = new Bundle();
            bundle.putInt("i", i);
            bundle.putParcelable("h", notification);
            a(1023, bundle, 0L);
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "disableBackgroundLocation");
        }
    }

    @Override // com.amap.api.location.LocationManagerBase
    public final AMapLocation getLastKnownLocation() {
        AMapLocation aMapLocation = null;
        try {
            if (this.g == null) {
                return null;
            }
            AMapLocation b2 = this.g.b();
            if (b2 != null) {
                try {
                    b2.setTrustedLevel(3);
                } catch (Throwable th) {
                    th = th;
                    aMapLocation = b2;
                    es.a(th, "AmapLocationManager", "getLastKnownLocation");
                    return aMapLocation;
                }
            }
            return b2;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    @Override // com.amap.api.location.LocationManagerBase
    public final boolean isStarted() {
        return this.A;
    }

    @Override // com.amap.api.location.LocationManagerBase
    public final void onDestroy() {
        try {
            if (this.F != null) {
                this.F.b();
                this.F = null;
            }
            a(1011, (Object) null, 0L);
            this.m = true;
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "onDestroy");
        }
    }

    @Override // com.amap.api.location.LocationManagerBase
    public final void setLocationListener(AMapLocationListener aMapLocationListener) {
        try {
            a(1002, aMapLocationListener, 0L);
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "setLocationListener");
        }
    }

    @Override // com.amap.api.location.LocationManagerBase
    public final void setLocationOption(AMapLocationClientOption aMapLocationClientOption) {
        try {
            a(PointerIconCompat.TYPE_ZOOM_IN, aMapLocationClientOption.m10clone(), 0L);
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "setLocationOption");
        }
    }

    @Override // com.amap.api.location.LocationManagerBase
    public final void startAssistantLocation() {
        try {
            a(1008, (Object) null, 0L);
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "startAssistantLocation");
        }
    }

    @Override // com.amap.api.location.LocationManagerBase
    public final void startAssistantLocation(WebView webView) {
        if (this.F == null) {
            this.F = new q(this.z, webView);
        }
        this.F.a();
    }

    @Override // com.amap.api.location.LocationManagerBase
    public final void startLocation() {
        try {
            a(1003, (Object) null, 0L);
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "startLocation");
        }
    }

    @Override // com.amap.api.location.LocationManagerBase
    public final void stopAssistantLocation() {
        try {
            if (this.F != null) {
                this.F.b();
                this.F = null;
            }
            a(PointerIconCompat.TYPE_VERTICAL_TEXT, (Object) null, 0L);
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "stopAssistantLocation");
        }
    }

    @Override // com.amap.api.location.LocationManagerBase
    public final void stopLocation() {
        try {
            a(1004, (Object) null, 0L);
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "stopLocation");
        }
    }

    @Override // com.amap.api.location.LocationManagerBase
    public final void unRegisterLocationListener(AMapLocationListener aMapLocationListener) {
        try {
            a(Constant.MSG_WHAT_READ_PART, aMapLocationListener, 0L);
        } catch (Throwable th) {
            es.a(th, "AmapLocationManager", "unRegisterLocationListener");
        }
    }
}
