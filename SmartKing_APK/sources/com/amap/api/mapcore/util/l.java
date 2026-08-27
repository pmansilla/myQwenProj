package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.alibaba.fastjson.asm.Opcodes;
import com.amap.api.mapcore.util.ej;
import com.amap.api.mapcore.util.gc;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CustomRenderer;
import com.amap.api.maps.InfoWindowAnimationManager;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.Projection;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.AMapCameraInfo;
import com.amap.api.maps.model.AMapGestureListener;
import com.amap.api.maps.model.Arc;
import com.amap.api.maps.model.ArcOptions;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BuildingOverlay;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.CrossOverlay;
import com.amap.api.maps.model.CrossOverlayOptions;
import com.amap.api.maps.model.CustomMapStyleOptions;
import com.amap.api.maps.model.GL3DModel;
import com.amap.api.maps.model.GL3DModelOptions;
import com.amap.api.maps.model.GroundOverlay;
import com.amap.api.maps.model.GroundOverlayOptions;
import com.amap.api.maps.model.IndoorBuildingInfo;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MultiPointOverlay;
import com.amap.api.maps.model.MultiPointOverlayOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.MyTrafficStyle;
import com.amap.api.maps.model.NavigateArrow;
import com.amap.api.maps.model.NavigateArrowOptions;
import com.amap.api.maps.model.Poi;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.RouteOverlay;
import com.amap.api.maps.model.Text;
import com.amap.api.maps.model.TextOptions;
import com.amap.api.maps.model.TileOverlay;
import com.amap.api.maps.model.TileOverlayOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.particle.ParticleOverlay;
import com.amap.api.maps.model.particle.ParticleOverlayOptions;
import com.autonavi.ae.gmap.GLMapEngine;
import com.autonavi.ae.gmap.GLMapRender;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.ae.gmap.gesture.EAMapPlatformGestureInfo;
import com.autonavi.ae.gmap.glinterface.MapLabelItem;
import com.autonavi.ae.gmap.gloverlay.BaseMapOverlay;
import com.autonavi.ae.gmap.gloverlay.CrossVectorOverlay;
import com.autonavi.ae.gmap.gloverlay.GLOverlayBundle;
import com.autonavi.ae.gmap.gloverlay.GLTextureProperty;
import com.autonavi.ae.gmap.listener.AMapWidgetListener;
import com.autonavi.ae.gmap.style.StyleItem;
import com.autonavi.amap.mapcore.AMapNativeRenderer;
import com.autonavi.amap.mapcore.AbstractCameraUpdateMessage;
import com.autonavi.amap.mapcore.AeUtil;
import com.autonavi.amap.mapcore.DPoint;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.MapConfig;
import com.autonavi.amap.mapcore.Rectangle;
import com.autonavi.amap.mapcore.VirtualEarthProjection;
import com.autonavi.amap.mapcore.animation.GLAlphaAnimation;
import com.autonavi.amap.mapcore.interfaces.IAMapListener;
import com.autonavi.amap.mapcore.interfaces.IMarkerAction;
import com.autonavi.amap.mapcore.interfaces.IMultiPointOverlay;
import com.autonavi.amap.mapcore.message.AbstractGestureMapMessage;
import com.autonavi.amap.mapcore.tools.GLConvertUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import ycnet.runchinaup.core.abs.IDataParser;

/* compiled from: AMapDelegateImp.java */
/* loaded from: classes.dex */
public class l implements ad, ej.a, IAMapListener {
    private AMap.OnMapTouchListener A;
    private AMap.OnPOIClickListener B;
    private AMap.OnMapLongClickListener C;
    private AMap.OnInfoWindowClickListener D;
    private AMap.OnIndoorBuildingActiveListener E;
    private AMap.OnMyLocationChangeListener F;
    private q G;
    private AMapGestureListener J;
    private bh K;
    private eb L;
    private UiSettings M;
    private af N;
    private final ar O;
    private final ae Q;
    private ge R;
    private aq S;
    private final ab T;
    private int V;
    private y X;
    private AMapWidgetListener Y;
    private eh aB;
    private ej aC;
    private p aD;
    private GLMapRender aE;
    private z aF;
    private int aU;
    private int aV;
    private b aW;
    private ef aX;
    private ac aY;
    private bm aZ;
    private dw ac;
    private LocationSource ad;
    private Thread aq;
    private Thread ar;
    private CustomRenderer aw;
    private final ak ax;
    protected final aj b;
    private k ba;
    protected bf d;
    protected Context f;
    protected GLMapEngine g;
    public int h;
    public int i;
    private AMap.OnMarkerClickListener u;
    private AMap.OnPolylineClickListener v;
    private AMap.OnMarkerDragListener w;
    private AMap.OnMapLoadedListener x;
    private AMap.OnCameraChangeListener y;
    private AMap.OnMapClickListener z;
    private AMap.onMapPrintScreenListener H = null;
    private AMap.OnMapScreenShotListener I = null;
    protected boolean a = false;
    private boolean P = false;
    private boolean U = false;
    private boolean W = false;
    private boolean Z = false;
    protected MapConfig c = new MapConfig(true);
    private boolean aa = false;
    private boolean ab = false;
    private boolean ae = false;
    private Marker af = null;
    private v ag = null;
    private boolean ah = false;
    private boolean ai = false;
    private boolean aj = false;
    private boolean ak = false;
    private boolean al = false;
    private boolean am = true;
    private Rect an = new Rect();
    private int ao = 1;
    private MyTrafficStyle ap = null;
    private boolean as = false;
    private boolean at = false;
    private boolean au = false;
    private int av = 0;
    private int ay = -1;
    private int az = -1;
    private List<am> aA = new ArrayList();
    fi e = null;
    private boolean aG = false;
    private float aH = 0.0f;
    private float aI = 1.0f;
    private float aJ = 1.0f;
    private boolean aK = true;
    private boolean aL = false;
    private boolean aM = false;
    private int aN = 0;
    private volatile boolean aO = false;
    private volatile boolean aP = false;
    private boolean aQ = false;
    private boolean aR = false;
    private Lock aS = new ReentrantLock();
    private int aT = 0;
    protected final Handler j = new Handler(Looper.getMainLooper()) { // from class: com.amap.api.mapcore.util.l.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i;
            if (message == null || l.this.W) {
                return;
            }
            try {
                i = message.what;
            } catch (Throwable th) {
                ic.c(th, "AMapDelegateImp", "handleMessage");
                th.printStackTrace();
            }
            if (i == 2) {
                StringBuilder sb = new StringBuilder();
                sb.append("Key验证失败：[");
                if (message.obj != null) {
                    sb.append(message.obj);
                } else {
                    sb.append(he.b);
                }
                sb.append("]");
                Log.w("amapsdk", sb.toString());
                return;
            }
            boolean z = true;
            switch (i) {
                case 10:
                    CameraPosition cameraPosition = (CameraPosition) message.obj;
                    if (cameraPosition == null || l.this.y == null) {
                        return;
                    }
                    l.this.y.onCameraChange(cameraPosition);
                    return;
                case 11:
                    try {
                        CameraPosition cameraPosition2 = l.this.getCameraPosition();
                        if (cameraPosition2 != null && l.this.R != null) {
                            l.this.R.a(cameraPosition2);
                        }
                        l.this.a(cameraPosition2);
                        if (l.this.aM) {
                            l.this.aM = false;
                            if (l.this.S != null && !MapsInitializer.isTileOverlayClosed()) {
                                l.this.S.b(false);
                            }
                            l.this.g(true);
                        }
                        if (l.this.ak) {
                            l.this.j();
                            l.this.ak = false;
                        }
                        l.this.a(true, cameraPosition2);
                        return;
                    } catch (Throwable th2) {
                        ic.c(th2, "AMapDelegateImp", "CameraUpdateFinish");
                        return;
                    }
                case 12:
                    if (l.this.R != null) {
                        l.this.R.a(Float.valueOf(l.this.g()));
                        return;
                    }
                    return;
                case 13:
                    if (l.this.R != null) {
                        l.this.R.m();
                        return;
                    }
                    return;
                case 14:
                    try {
                        if (l.this.A != null) {
                            l.this.A.onTouch((MotionEvent) message.obj);
                            return;
                        }
                        return;
                    } catch (Throwable th3) {
                        ic.c(th3, "AMapDelegateImp", "onTouchHandler");
                        th3.printStackTrace();
                        return;
                    }
                case 15:
                    Bitmap bitmap = (Bitmap) message.obj;
                    int i2 = message.arg1;
                    if (bitmap == null || l.this.R == null) {
                        if (l.this.H != null) {
                            l.this.H.onMapPrint(null);
                        }
                        if (l.this.I != null) {
                            l.this.I.onMapScreenShot(null);
                            l.this.I.onMapScreenShot(null, i2);
                        }
                    } else {
                        Canvas canvas = new Canvas(bitmap);
                        gh i3 = l.this.R.i();
                        if (i3 != null) {
                            i3.onDraw(canvas);
                        }
                        l.this.R.a(canvas);
                        if (l.this.H != null) {
                            l.this.H.onMapPrint(new BitmapDrawable(l.this.f.getResources(), bitmap));
                        }
                        if (l.this.I != null) {
                            l.this.I.onMapScreenShot(bitmap);
                            l.this.I.onMapScreenShot(bitmap, i2);
                        }
                    }
                    l.this.H = null;
                    l.this.I = null;
                    return;
                case 16:
                    if (l.this.x != null) {
                        try {
                            l.this.x.onMapLoaded();
                        } catch (Throwable th4) {
                            ic.c(th4, "AMapDelegateImp", "onMapLoaded");
                            th4.printStackTrace();
                        }
                    }
                    if (l.this.R != null) {
                        l.this.R.n();
                        return;
                    }
                    return;
                case 17:
                    if (l.this.g.isInMapAnimation(1) && l.this.S != null && !MapsInitializer.isTileOverlayClosed()) {
                        l.this.S.b(false);
                    }
                    if (l.this.S == null || MapsInitializer.isTileOverlayClosed()) {
                        return;
                    }
                    aq aqVar = l.this.S;
                    if (message.arg1 == 0) {
                        z = false;
                    }
                    aqVar.a(z);
                    return;
                case 18:
                    if (l.this.K == null || !l.this.ab) {
                        return;
                    }
                    l.this.K.c();
                    return;
                case 19:
                    if (l.this.z != null) {
                        DPoint obtain = DPoint.obtain();
                        l.this.b(message.arg1, message.arg2, obtain);
                        try {
                            l.this.z.onMapClick(new LatLng(obtain.y, obtain.x));
                            obtain.recycle();
                            return;
                        } catch (Throwable th5) {
                            ic.c(th5, "AMapDelegateImp", "OnMapClickListener.onMapClick");
                            th5.printStackTrace();
                            return;
                        }
                    }
                    return;
                case 20:
                    try {
                        l.this.B.onPOIClick((Poi) message.obj);
                        return;
                    } catch (Throwable th6) {
                        ic.c(th6, "AMapDelegateImp", "OnPOIClickListener.onPOIClick");
                        th6.printStackTrace();
                        return;
                    }
                default:
                    return;
            }
            ic.c(th, "AMapDelegateImp", "handleMessage");
            th.printStackTrace();
        }
    };
    private a bb = new a() { // from class: com.amap.api.mapcore.util.l.11
        @Override // com.amap.api.mapcore.util.l.a, java.lang.Runnable
        public void run() {
            super.run();
            try {
                l.this.setTrafficEnabled(this.c);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    };
    private a bc = new a() { // from class: com.amap.api.mapcore.util.l.21
        @Override // com.amap.api.mapcore.util.l.a, java.lang.Runnable
        public void run() {
            super.run();
            try {
                l.this.setCenterToPixel(l.this.aU, l.this.aV);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    };
    private a bd = new a() { // from class: com.amap.api.mapcore.util.l.30
        @Override // com.amap.api.mapcore.util.l.a, java.lang.Runnable
        public void run() {
            super.run();
            l.this.b(this.g, this.d, this.e, this.f);
        }
    };
    private a be = new a() { // from class: com.amap.api.mapcore.util.l.31
        @Override // com.amap.api.mapcore.util.l.a, java.lang.Runnable
        public void run() {
            super.run();
            l.this.setMapCustomEnable(this.c);
        }
    };
    private a bf = new a() { // from class: com.amap.api.mapcore.util.l.32
        @Override // com.amap.api.mapcore.util.l.a, java.lang.Runnable
        public void run() {
            super.run();
            l.this.a(this.g, this.c);
        }
    };
    private a bg = new a() { // from class: com.amap.api.mapcore.util.l.33
        @Override // com.amap.api.mapcore.util.l.a, java.lang.Runnable
        public void run() {
            super.run();
            try {
                l.this.setMapTextEnable(this.c);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    };
    private a bh = new a() { // from class: com.amap.api.mapcore.util.l.34
        @Override // com.amap.api.mapcore.util.l.a, java.lang.Runnable
        public void run() {
            super.run();
            try {
                l.this.setRoadArrowEnable(this.c);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    };
    private a bi = new a() { // from class: com.amap.api.mapcore.util.l.35
        @Override // com.amap.api.mapcore.util.l.a, java.lang.Runnable
        public void run() {
            super.run();
            l.this.b(this.g, this.c);
        }
    };
    private a bj = new a() { // from class: com.amap.api.mapcore.util.l.2
        @Override // com.amap.api.mapcore.util.l.a, java.lang.Runnable
        public void run() {
            super.run();
            try {
                l.this.setIndoorEnabled(this.c);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    };
    private Runnable bk = new Runnable() { // from class: com.amap.api.mapcore.util.l.3
        @Override // java.lang.Runnable
        public void run() {
            gh i;
            if (l.this.R == null || (i = l.this.R.i()) == null) {
                return;
            }
            i.d();
        }
    };
    private a bl = new a() { // from class: com.amap.api.mapcore.util.l.4
        @Override // com.amap.api.mapcore.util.l.a, java.lang.Runnable
        public void run() {
            super.run();
            l.this.c(this.g, this.c);
        }
    };
    private a bm = new a() { // from class: com.amap.api.mapcore.util.l.5
        @Override // com.amap.api.mapcore.util.l.a, java.lang.Runnable
        public void run() {
            super.run();
            try {
                l.this.setMyTrafficStyle(l.this.ap);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    };
    private EAMapPlatformGestureInfo bn = new EAMapPlatformGestureInfo();
    Point k = new Point();
    Rect l = new Rect();
    private long bo = 0;
    protected String m = null;
    private bf bp = null;
    float[] n = new float[16];
    float[] o = new float[16];
    float[] p = new float[16];
    private IPoint[] bq = null;
    float[] q = new float[12];
    String r = "precision highp float;\nattribute vec3 aVertex;//顶点数组,三维坐标\nuniform mat4 aMVPMatrix;//mvp矩阵\nvoid main(){\n  gl_Position = aMVPMatrix * vec4(aVertex, 1.0);\n}";
    String s = "//有颜色 没有纹理\nprecision highp float;\nvoid main(){\n  gl_FragColor = vec4(1.0,0,0,1.0);\n}";
    int t = -1;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AMapDelegateImp.java */
    /* loaded from: classes.dex */
    public static abstract class a implements Runnable {
        boolean b;
        boolean c;
        int d;
        int e;
        int f;
        int g;

        private a() {
            this.b = false;
            this.c = false;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.b = false;
        }
    }

    /* compiled from: AMapDelegateImp.java */
    /* loaded from: classes.dex */
    class b {
        b() {
        }

        public void a(bf bfVar) {
            if (l.this.c == null || !l.this.c.isIndoorEnable()) {
                return;
            }
            final gc g = l.this.R.g();
            if (bfVar == null) {
                try {
                    if (l.this.E != null) {
                        l.this.E.OnIndoorBuilding(bfVar);
                    }
                } catch (Throwable th) {
                    th.printStackTrace();
                }
                if (l.this.d != null) {
                    l.this.d.g = null;
                }
                if (g.d()) {
                    l.this.j.post(new Runnable() { // from class: com.amap.api.mapcore.util.l.b.1
                        @Override // java.lang.Runnable
                        public void run() {
                            g.a(false);
                        }
                    });
                }
                l.this.c.maxZoomLevel = l.this.c.isSetLimitZoomLevel() ? l.this.c.getMaxZoomLevel() : 20.0f;
                try {
                    if (!l.this.O.isZoomControlsEnabled() || l.this.Y == null) {
                        return;
                    }
                    l.this.Y.invalidateZoomController(l.this.c.getSZ());
                    return;
                } catch (Throwable th2) {
                    th2.printStackTrace();
                }
            }
            if (bfVar != null && bfVar.floor_indexs != null && bfVar.floor_names != null && bfVar.floor_indexs.length == bfVar.floor_names.length) {
                int i = 0;
                while (true) {
                    if (i >= bfVar.floor_indexs.length) {
                        break;
                    }
                    if (bfVar.activeFloorIndex == bfVar.floor_indexs[i]) {
                        bfVar.activeFloorName = bfVar.floor_names[i];
                        break;
                    }
                    i++;
                }
            }
            if (bfVar == null || l.this.d == null || l.this.d.activeFloorIndex == bfVar.activeFloorIndex || !g.d()) {
                if (bfVar != null && (l.this.d == null || !l.this.d.poiid.equals(bfVar.poiid) || l.this.d.g == null)) {
                    l.this.d = bfVar;
                    if (l.this.c != null) {
                        l.this.d.g = l.this.c.getMapGeoCenter();
                    }
                }
                try {
                    if (l.this.E != null) {
                        l.this.E.OnIndoorBuilding(bfVar);
                    }
                    l.this.c.maxZoomLevel = l.this.c.isSetLimitZoomLevel() ? l.this.c.getMaxZoomLevel() : 20.0f;
                    if (l.this.O.isZoomControlsEnabled() && l.this.Y != null) {
                        l.this.Y.invalidateZoomController(l.this.c.getSZ());
                    }
                    if (l.this.O.isIndoorSwitchEnabled()) {
                        if (!g.d()) {
                            l.this.O.setIndoorSwitchEnabled(true);
                        }
                        l.this.j.post(new Runnable() { // from class: com.amap.api.mapcore.util.l.b.2
                            @Override // java.lang.Runnable
                            public void run() {
                                try {
                                    g.a(l.this.d.floor_names);
                                    g.a(l.this.d.activeFloorName);
                                    if (g.d()) {
                                        return;
                                    }
                                    g.a(true);
                                } catch (Throwable th3) {
                                    th3.printStackTrace();
                                }
                            }
                        });
                    } else {
                        if (l.this.O.isIndoorSwitchEnabled() || !g.d()) {
                            return;
                        }
                        l.this.O.setIndoorSwitchEnabled(false);
                    }
                } catch (Throwable th3) {
                    th3.printStackTrace();
                }
            }
        }
    }

    /* compiled from: AMapDelegateImp.java */
    /* loaded from: classes.dex */
    private class c implements gc.a {
        private c() {
        }

        @Override // com.amap.api.mapcore.util.gc.a
        public void a(int i) {
            if (l.this.d != null) {
                l.this.d.activeFloorIndex = l.this.d.floor_indexs[i];
                l.this.d.activeFloorName = l.this.d.floor_names[i];
                try {
                    l.this.setIndoorBuildingInfo(l.this.d);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AMapDelegateImp.java */
    /* loaded from: classes.dex */
    public class d implements Runnable {
        private Context b;
        private AMap.OnCacheRemoveListener c;

        public d(Context context, AMap.OnCacheRemoveListener onCacheRemoveListener) {
            this.b = context;
            this.c = onCacheRemoveListener;
        }

        public boolean equals(Object obj) {
            return obj instanceof d;
        }

        /* JADX WARN: Code restructure failed: missing block: B:13:0x0041, code lost:
        
            if (com.amap.api.mapcore.util.fr.e(r2) != false) goto L24;
         */
        /* JADX WARN: Removed duplicated region for block: B:12:0x003d A[Catch: all -> 0x0034, Throwable -> 0x0037, TRY_LEAVE, TryCatch #6 {all -> 0x0034, Throwable -> 0x0037, blocks: (B:52:0x0027, B:12:0x003d), top: B:51:0x0027 }] */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void run() {
            /*
                r6 = this;
                r0 = 0
                r1 = 1
                android.content.Context r2 = r6.b     // Catch: java.lang.Throwable -> L71 java.lang.Throwable -> L73
                android.content.Context r2 = r2.getApplicationContext()     // Catch: java.lang.Throwable -> L71 java.lang.Throwable -> L73
                java.lang.String r3 = com.amap.api.mapcore.util.fr.c(r2)     // Catch: java.lang.Throwable -> L71 java.lang.Throwable -> L73
                java.lang.String r4 = com.amap.api.mapcore.util.fr.a(r2)     // Catch: java.lang.Throwable -> L71 java.lang.Throwable -> L73
                java.io.File r5 = new java.io.File     // Catch: java.lang.Throwable -> L71 java.lang.Throwable -> L73
                r5.<init>(r3)     // Catch: java.lang.Throwable -> L71 java.lang.Throwable -> L73
                boolean r3 = r5.exists()     // Catch: java.lang.Throwable -> L71 java.lang.Throwable -> L73
                if (r3 == 0) goto L24
                boolean r3 = com.autonavi.amap.mapcore.FileUtil.deleteFile(r5)     // Catch: java.lang.Throwable -> L71 java.lang.Throwable -> L73
                if (r3 == 0) goto L22
                goto L24
            L22:
                r3 = 0
                goto L25
            L24:
                r3 = 1
            L25:
                if (r3 == 0) goto L3a
                java.io.File r5 = new java.io.File     // Catch: java.lang.Throwable -> L34 java.lang.Throwable -> L37
                r5.<init>(r4)     // Catch: java.lang.Throwable -> L34 java.lang.Throwable -> L37
                boolean r4 = com.autonavi.amap.mapcore.FileUtil.deleteFile(r5)     // Catch: java.lang.Throwable -> L34 java.lang.Throwable -> L37
                if (r4 == 0) goto L3a
                r3 = 1
                goto L3b
            L34:
                r0 = move-exception
                r1 = r3
                goto L8b
            L37:
                r2 = move-exception
                r1 = r3
                goto L74
            L3a:
                r3 = 0
            L3b:
                if (r3 == 0) goto L44
                boolean r2 = com.amap.api.mapcore.util.fr.e(r2)     // Catch: java.lang.Throwable -> L34 java.lang.Throwable -> L37
                if (r2 == 0) goto L44
                goto L45
            L44:
                r1 = 0
            L45:
                com.amap.api.mapcore.util.l r2 = com.amap.api.mapcore.util.l.this     // Catch: java.lang.Throwable -> L71 java.lang.Throwable -> L73
                com.amap.api.mapcore.util.aq r2 = com.amap.api.mapcore.util.l.i(r2)     // Catch: java.lang.Throwable -> L71 java.lang.Throwable -> L73
                if (r2 == 0) goto L5c
                boolean r2 = com.amap.api.maps.MapsInitializer.isTileOverlayClosed()     // Catch: java.lang.Throwable -> L71 java.lang.Throwable -> L73
                if (r2 != 0) goto L5c
                com.amap.api.mapcore.util.l r2 = com.amap.api.mapcore.util.l.this     // Catch: java.lang.Throwable -> L71 java.lang.Throwable -> L73
                com.amap.api.mapcore.util.aq r2 = com.amap.api.mapcore.util.l.i(r2)     // Catch: java.lang.Throwable -> L71 java.lang.Throwable -> L73
                r2.h()     // Catch: java.lang.Throwable -> L71 java.lang.Throwable -> L73
            L5c:
                com.amap.api.mapcore.util.l r0 = com.amap.api.mapcore.util.l.this     // Catch: java.lang.Throwable -> L6c
                com.autonavi.ae.gmap.GLMapEngine r0 = r0.g     // Catch: java.lang.Throwable -> L6c
                if (r0 == 0) goto L8a
                com.amap.api.maps.AMap$OnCacheRemoveListener r0 = r6.c     // Catch: java.lang.Throwable -> L6c
                if (r0 == 0) goto L8a
                com.amap.api.maps.AMap$OnCacheRemoveListener r0 = r6.c     // Catch: java.lang.Throwable -> L6c
                r0.onRemoveCacheFinish(r1)     // Catch: java.lang.Throwable -> L6c
                goto L8a
            L6c:
                r0 = move-exception
                r0.printStackTrace()
                goto L8a
            L71:
                r0 = move-exception
                goto L8b
            L73:
                r2 = move-exception
            L74:
                java.lang.String r3 = "AMapDelegateImp"
                java.lang.String r4 = "RemoveCacheRunnable"
                com.amap.api.mapcore.util.ic.c(r2, r3, r4)     // Catch: java.lang.Throwable -> L71
                com.amap.api.mapcore.util.l r1 = com.amap.api.mapcore.util.l.this     // Catch: java.lang.Throwable -> L6c
                com.autonavi.ae.gmap.GLMapEngine r1 = r1.g     // Catch: java.lang.Throwable -> L6c
                if (r1 == 0) goto L8a
                com.amap.api.maps.AMap$OnCacheRemoveListener r1 = r6.c     // Catch: java.lang.Throwable -> L6c
                if (r1 == 0) goto L8a
                com.amap.api.maps.AMap$OnCacheRemoveListener r1 = r6.c     // Catch: java.lang.Throwable -> L6c
                r1.onRemoveCacheFinish(r0)     // Catch: java.lang.Throwable -> L6c
            L8a:
                return
            L8b:
                com.amap.api.mapcore.util.l r2 = com.amap.api.mapcore.util.l.this     // Catch: java.lang.Throwable -> L9b
                com.autonavi.ae.gmap.GLMapEngine r2 = r2.g     // Catch: java.lang.Throwable -> L9b
                if (r2 == 0) goto L9f
                com.amap.api.maps.AMap$OnCacheRemoveListener r2 = r6.c     // Catch: java.lang.Throwable -> L9b
                if (r2 == 0) goto L9f
                com.amap.api.maps.AMap$OnCacheRemoveListener r2 = r6.c     // Catch: java.lang.Throwable -> L9b
                r2.onRemoveCacheFinish(r1)     // Catch: java.lang.Throwable -> L9b
                goto L9f
            L9b:
                r1 = move-exception
                r1.printStackTrace()
            L9f:
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.l.d.run():void");
        }
    }

    public l(ae aeVar, Context context, AttributeSet attributeSet) {
        this.L = null;
        this.S = null;
        this.f = context;
        this.aD = new p(context, this, aeVar);
        ic.a(this.f);
        fb.a().a(this.f);
        w.b = hd.c(context);
        es.a(this.f);
        this.aF = new z(this);
        this.g = new GLMapEngine(this.f, this);
        this.aE = new GLMapRender(this);
        this.Q = aeVar;
        aeVar.setRenderer(this.aE);
        this.O = new ar(this);
        this.R = new ge(this.f, this);
        this.R.a(new c());
        this.aW = new b();
        this.T = new ab(this);
        if (!MapsInitializer.isTileOverlayClosed()) {
            this.S = new aq(this.f, this);
        }
        this.b = new aj(this.f, this);
        this.X = new y(this.f, this);
        this.Q.setRenderMode(0);
        this.aE.setRenderFps(15.0f);
        this.g.setMapListener(this);
        this.N = new ao(this);
        this.G = new q(this);
        this.L = new eb(this, context);
        this.K = new bh(this.f);
        this.K.a(this.R);
        this.K.b(this.L);
        this.ax = new ak();
        this.aq = new t(this.f, this);
        this.ad = new bi(this.f);
        this.aZ = new bm(this);
        this.aY = new ac();
        this.aB = new eh(this.f, this);
        this.aC = new ej(this.f);
        this.aC.a(this);
        this.ba = new k(this, this.f);
    }

    private LatLng A() {
        if (this.c == null) {
            return null;
        }
        DPoint pixelsToLatLong = VirtualEarthProjection.pixelsToLatLong(this.c.getSX(), this.c.getSY(), 20);
        LatLng latLng = new LatLng(pixelsToLatLong.y, pixelsToLatLong.x, false);
        pixelsToLatLong.recycle();
        return latLng;
    }

    private synchronized void B() {
        synchronized (this.aA) {
            int size = this.aA.size();
            for (int i = 0; i < size; i++) {
                this.aA.get(i).j().recycle();
            }
            this.aA.clear();
        }
    }

    private void C() {
        try {
            this.c.setMapRect(fr.a((ad) this, true));
            GLMapState newMapState = this.g.getNewMapState(1);
            if (newMapState != null) {
                newMapState.recalculate();
                newMapState.getPixel20Bound(this.l, getMapWidth(), getMapHeight());
                this.c.getGeoRectangle().updateRect(this.l, this.c.getSX(), this.c.getSY());
                this.c.setMapPerPixelUnitLength(newMapState.getGLUnitWithWin(1));
                newMapState.recycle();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void D() {
        if (this.bo < 2) {
            this.bo++;
            return;
        }
        final ga f = this.R.f();
        if (f == null || f.getVisibility() == 8) {
            return;
        }
        if (!this.ab) {
            this.j.sendEmptyMessage(16);
            this.ab = true;
            g(true);
        }
        this.j.post(new Runnable() { // from class: com.amap.api.mapcore.util.l.10
            @Override // java.lang.Runnable
            public void run() {
                if (l.this.Z) {
                    return;
                }
                try {
                    if (l.this.d != null) {
                        l.this.setIndoorBuildingInfo(l.this.d);
                    }
                } catch (Throwable th) {
                    th.printStackTrace();
                }
                f.a(false);
            }
        });
    }

    private void E() {
        if (this.as) {
            return;
        }
        try {
            this.aq.setName("AuthThread");
            this.aq.start();
            this.as = true;
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void F() {
        if (this.at) {
            return;
        }
        try {
            if (this.ar == null) {
                this.ar = new r(this.f, this);
            }
            this.ar.setName("AuthProThread");
            this.ar.start();
            this.at = true;
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void G() {
        try {
            LatLngBounds limitLatLngBounds = this.c.getLimitLatLngBounds();
            if (this.g != null && a(limitLatLngBounds)) {
                GLMapState gLMapState = new GLMapState(1, this.g.getNativeInstance());
                IPoint obtain = IPoint.obtain();
                GLMapState.lonlat2Geo(limitLatLngBounds.northeast.longitude, limitLatLngBounds.northeast.latitude, obtain);
                IPoint obtain2 = IPoint.obtain();
                GLMapState.lonlat2Geo(limitLatLngBounds.southwest.longitude, limitLatLngBounds.southwest.latitude, obtain2);
                this.c.setLimitIPoints(new IPoint[]{obtain, obtain2});
                gLMapState.recycle();
                return;
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        this.c.setLimitIPoints(null);
    }

    private void H() {
        this.F = null;
        this.u = null;
        this.v = null;
        this.w = null;
        this.x = null;
        this.y = null;
        this.z = null;
        this.A = null;
        this.B = null;
        this.C = null;
        this.D = null;
        this.E = null;
        this.G = null;
        this.H = null;
        this.I = null;
    }

    private void a(int i, GL10 gl10) {
        if (this.az != -1) {
            this.aE.setRenderFps(this.az);
            resetRenderTime();
        } else if (this.g.isInMapAction(i) || this.aL) {
            this.aE.setRenderFps(40.0f);
        } else if (this.g.isInMapAnimation(i)) {
            this.aE.setRenderFps(30.0f);
            this.aE.resetTickCount(15);
        } else {
            this.aE.setRenderFps(15.0f);
        }
        if (this.c.isWorldMapEnable() != MapsInitializer.isLoadWorldGridMap()) {
            g(true);
            this.c.setWorldMapEnable(MapsInitializer.isLoadWorldGridMap());
        }
    }

    private void a(MotionEvent motionEvent) throws RemoteException {
        if (!this.ae || this.af == null || this.ag == null) {
            return;
        }
        int x = (int) motionEvent.getX();
        int y = (int) (motionEvent.getY() - 60.0f);
        LatLng b2 = this.ag.b();
        if (b2 != null) {
            LatLng position = this.ag.getPosition();
            DPoint obtain = DPoint.obtain();
            b(x, y, obtain);
            LatLng latLng = new LatLng((position.latitude + obtain.y) - b2.latitude, (position.longitude + obtain.x) - b2.longitude);
            obtain.recycle();
            this.af.setPosition(latLng);
            if (this.w != null) {
                this.w.onMarkerDrag(this.af);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(CameraPosition cameraPosition) {
        if (!this.c.getMapLanguage().equals(AMap.ENGLISH)) {
            if (this.am) {
                return;
            }
            this.am = true;
            b(1, this.am);
            return;
        }
        boolean b2 = b(cameraPosition);
        if (b2 != this.am) {
            this.am = b2;
            b(1, this.am);
        }
    }

    private void a(GL10 gl10) {
        if (this.al) {
            boolean canStopMapRender = this.g.canStopMapRender(1);
            Message obtainMessage = this.j.obtainMessage(15, fr.a(0, 0, getMapWidth(), getMapHeight()));
            obtainMessage.arg1 = canStopMapRender ? 1 : 0;
            obtainMessage.sendToTarget();
            this.al = false;
        }
    }

    private boolean a(LatLngBounds latLngBounds) {
        return (latLngBounds == null || latLngBounds.northeast == null || latLngBounds.southwest == null) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Poi b(int i, int i2, int i3) {
        if (!this.aO) {
            return null;
        }
        try {
            ArrayList<MapLabelItem> a2 = a(1, i, i2, i3);
            MapLabelItem mapLabelItem = (a2 == null || a2.size() <= 0) ? null : a2.get(0);
            if (mapLabelItem == null) {
                return null;
            }
            DPoint pixelsToLatLong = VirtualEarthProjection.pixelsToLatLong(mapLabelItem.pixel20X, mapLabelItem.pixel20Y, 20);
            Poi poi = new Poi(mapLabelItem.name, new LatLng(pixelsToLatLong.y, pixelsToLatLong.x, false), mapLabelItem.poiid);
            pixelsToLatLong.recycle();
            return poi;
        } catch (Throwable unused) {
            return null;
        }
    }

    private void b(final MotionEvent motionEvent) {
        queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.l.8
            @Override // java.lang.Runnable
            public void run() {
                try {
                    Message obtain = Message.obtain();
                    Poi b2 = l.this.b((int) motionEvent.getX(), (int) motionEvent.getY(), 25);
                    if (l.this.B == null) {
                        l.this.c(motionEvent);
                    } else if (b2 != null) {
                        obtain.what = 20;
                        obtain.obj = b2;
                        l.this.j.sendMessage(obtain);
                    } else {
                        l.this.c(motionEvent);
                    }
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        });
    }

    private boolean b(CameraPosition cameraPosition) {
        if (cameraPosition.zoom < 7.0f) {
            return false;
        }
        if (cameraPosition.isAbroad) {
            return true;
        }
        if (this.c == null) {
            return false;
        }
        try {
            return !fk.a(this.c.getGeoRectangle().getClipRect());
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(final MotionEvent motionEvent) {
        this.j.post(new Runnable() { // from class: com.amap.api.mapcore.util.l.9
            @Override // java.lang.Runnable
            public void run() {
                Message obtain = Message.obtain();
                obtain.what = 19;
                obtain.arg1 = (int) motionEvent.getX();
                obtain.arg2 = (int) motionEvent.getY();
                l.this.j.sendMessage(obtain);
            }
        });
    }

    private void c(AbstractCameraUpdateMessage abstractCameraUpdateMessage) {
        abstractCameraUpdateMessage.isUseAnchor = this.aa;
        if (this.aa) {
            abstractCameraUpdateMessage.anchorX = this.c.getAnchorX();
            abstractCameraUpdateMessage.anchorY = this.c.getAnchorY();
        }
        if (abstractCameraUpdateMessage.width == 0) {
            abstractCameraUpdateMessage.width = getMapWidth();
        }
        if (abstractCameraUpdateMessage.height == 0) {
            abstractCameraUpdateMessage.height = getMapHeight();
        }
        abstractCameraUpdateMessage.mapConfig = this.c;
    }

    private boolean c(int i, int i2) {
        if (this.g != null) {
            return this.g.getSrvViewStateBoolValue(i, i2);
        }
        return false;
    }

    private boolean d(MotionEvent motionEvent) {
        if (this.v != null) {
            DPoint obtain = DPoint.obtain();
            b((int) motionEvent.getX(), (int) motionEvent.getY(), obtain);
            LatLng latLng = new LatLng(obtain.y, obtain.x);
            obtain.recycle();
            Cdo a2 = this.T.a(latLng);
            if (a2 != null) {
                this.v.onPolylineClick(new Polyline((ds) a2));
                return false;
            }
        }
        return false;
    }

    private boolean e(MotionEvent motionEvent) throws RemoteException {
        boolean z;
        LatLng b2;
        if (!this.b.b(motionEvent)) {
            return false;
        }
        v d2 = this.b.d();
        if (d2 == null) {
            return true;
        }
        try {
            Marker marker = new Marker((dv) d2);
            this.b.a((dm) d2);
            if (this.u != null) {
                boolean onMarkerClick = this.u.onMarkerClick(marker);
                z = (!onMarkerClick && this.b.g() > 0) ? onMarkerClick : true;
                return true;
            }
            a((dv) d2);
            if (!d2.g() && (b2 = d2.b()) != null) {
                IPoint obtain = IPoint.obtain();
                a(b2.latitude, b2.longitude, obtain);
                a(aw.a(obtain));
            }
            return z;
        } catch (Throwable th) {
            ic.c(th, "AMapDelegateImp", "onMarkerTap");
            th.printStackTrace();
            return false;
        }
    }

    private boolean f(MotionEvent motionEvent) {
        if (this.aZ == null) {
            return false;
        }
        IPoint obtain = IPoint.obtain();
        if (this.g != null) {
            a((int) motionEvent.getX(), (int) motionEvent.getY(), obtain);
        }
        boolean a2 = this.aZ.a(obtain);
        obtain.recycle();
        return a2;
    }

    private boolean g(MotionEvent motionEvent) throws RemoteException {
        if (this.K == null || !this.K.a(motionEvent)) {
            return false;
        }
        if (this.D != null) {
            v d2 = this.b.d();
            if (!d2.isVisible() && d2.isInfoWindowEnable()) {
                return true;
            }
            this.D.onInfoWindowClick(new Marker((dv) d2));
        }
        return true;
    }

    private void w(final int i) {
        if (this.aO) {
            this.aF.a();
            this.aG = true;
            this.aL = true;
            try {
                stopAnimation();
            } catch (RemoteException unused) {
            }
            queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.l.6
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        l.this.g.clearAllMessages(i);
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            });
        }
    }

    private void x(int i) {
        this.aG = true;
        this.aL = false;
        if (this.ai) {
            this.ai = false;
        }
        if (this.ah) {
            this.ah = false;
        }
        if (this.aj) {
            this.aj = false;
        }
        this.ae = false;
        if (this.w != null && this.af != null) {
            try {
                this.w.onMarkerDragEnd(this.af);
            } catch (Throwable th) {
                ic.c(th, "AMapDelegateImp", "OnMarkerDragListener.onMarkerDragEnd");
                th.printStackTrace();
            }
            this.af = null;
        }
        this.Q.postDelayed(new Runnable() { // from class: com.amap.api.mapcore.util.l.7
            @Override // java.lang.Runnable
            public void run() {
                l.this.aN = 1;
            }
        }, 300L);
    }

    private void y(int i) {
    }

    @Override // com.amap.api.mapcore.util.ad
    public float a(int i) {
        if (this.c != null) {
            return this.c.getSZ();
        }
        return 0.0f;
    }

    public int a(int i, Rect rect, int i2, int i3) {
        if (this.g == null || i < 0 || rect == null) {
            return 0;
        }
        int engineIDWithType = this.g.getEngineIDWithType(i);
        if (this.g.isEngineCreated(engineIDWithType)) {
            a(engineIDWithType, rect.left, rect.top, rect.width(), rect.height(), i2, i3);
            return engineIDWithType;
        }
        int i4 = this.f.getResources().getDisplayMetrics().densityDpi;
        float f = this.f.getResources().getDisplayMetrics().density;
        this.aI = GLMapState.calMapZoomScalefactor(i2, i3, i4);
        GLMapEngine.MapViewInitParam mapViewInitParam = new GLMapEngine.MapViewInitParam();
        mapViewInitParam.engineId = engineIDWithType;
        mapViewInitParam.x = rect.left;
        mapViewInitParam.y = rect.top;
        mapViewInitParam.width = rect.width();
        mapViewInitParam.height = rect.height();
        mapViewInitParam.screenWidth = i2;
        mapViewInitParam.screenHeight = i3;
        mapViewInitParam.screenScale = f;
        mapViewInitParam.textScale = this.aJ * f;
        mapViewInitParam.mapZoomScale = this.aI;
        this.g.createAMapEngineWithFrame(mapViewInitParam);
        GLMapState mapState = this.g.getMapState(engineIDWithType);
        mapState.setMapZoomer(this.c.getSZ());
        mapState.setCameraDegree(this.c.getSC());
        mapState.setMapAngle(this.c.getSR());
        mapState.setMapGeoCenter(this.c.getSX(), this.c.getSY());
        this.g.setMapState(engineIDWithType, mapState);
        this.g.setOvelayBundle(engineIDWithType, new GLOverlayBundle<>(engineIDWithType, this));
        return engineIDWithType;
    }

    @Override // com.amap.api.mapcore.util.ad
    public int a(EAMapPlatformGestureInfo eAMapPlatformGestureInfo) {
        if (this.g != null) {
            return this.g.getEngineIDWithGestureInfo(eAMapPlatformGestureInfo);
        }
        return 1;
    }

    @Override // com.amap.api.mapcore.util.ad
    public int a(IMarkerAction iMarkerAction, Rect rect) {
        return 0;
    }

    @Override // com.amap.api.mapcore.util.ad
    public am a(BitmapDescriptor bitmapDescriptor) {
        return a(bitmapDescriptor, false);
    }

    @Override // com.amap.api.mapcore.util.ad
    public am a(BitmapDescriptor bitmapDescriptor, boolean z) {
        if (bitmapDescriptor == null || bitmapDescriptor.getBitmap() == null || bitmapDescriptor.getBitmap().isRecycled()) {
            return null;
        }
        synchronized (this.aA) {
            for (int i = 0; i < this.aA.size(); i++) {
                am amVar = this.aA.get(i);
                if ((!z || amVar.k() != e()) && amVar.j().equals(bitmapDescriptor)) {
                    return amVar;
                }
            }
            return null;
        }
    }

    @Override // com.amap.api.mapcore.util.ad
    public LatLngBounds a(LatLng latLng, float f, float f2, float f3) {
        int mapWidth = getMapWidth();
        int mapHeight = getMapHeight();
        if (mapWidth <= 0 || mapHeight <= 0 || this.W) {
            return null;
        }
        float a2 = fr.a(this.c, f);
        GLMapState gLMapState = new GLMapState(1, this.g.getNativeInstance());
        if (latLng != null) {
            IPoint obtain = IPoint.obtain();
            a(latLng.latitude, latLng.longitude, obtain);
            gLMapState.setCameraDegree(f3);
            gLMapState.setMapAngle(f2);
            gLMapState.setMapGeoCenter(obtain.x, obtain.y);
            gLMapState.setMapZoomer(a2);
            gLMapState.recalculate();
            obtain.recycle();
        }
        DPoint obtain2 = DPoint.obtain();
        a(gLMapState, 0, 0, obtain2);
        LatLng latLng2 = new LatLng(obtain2.y, obtain2.x, false);
        a(gLMapState, mapWidth, mapHeight, obtain2);
        LatLng latLng3 = new LatLng(obtain2.y, obtain2.x, false);
        obtain2.recycle();
        gLMapState.recycle();
        return LatLngBounds.builder().include(latLng3).include(latLng2).build();
    }

    @Override // com.amap.api.mapcore.util.ad
    public GLMapEngine a() {
        return this.g;
    }

    public ArrayList<MapLabelItem> a(int i, int i2, int i3, int i4) {
        if (!this.aO) {
            return null;
        }
        ArrayList<MapLabelItem> arrayList = new ArrayList<>();
        byte[] labelBuffer = this.g.getLabelBuffer(i, i2, i3, i4);
        if (labelBuffer == null) {
            return null;
        }
        int i5 = GLConvertUtil.getInt(labelBuffer, 0) >= 1 ? 1 : 0;
        int i6 = 0;
        int i7 = 4;
        while (i6 < i5) {
            MapLabelItem mapLabelItem = new MapLabelItem();
            int i8 = GLConvertUtil.getInt(labelBuffer, i7);
            int i9 = i7 + 4;
            int i10 = GLConvertUtil.getInt(labelBuffer, i9);
            int i11 = i9 + 4;
            mapLabelItem.x = i8;
            mapLabelItem.y = this.Q.getHeight() - i10;
            mapLabelItem.pixel20X = GLConvertUtil.getInt(labelBuffer, i11);
            int i12 = i11 + 4;
            mapLabelItem.pixel20Y = GLConvertUtil.getInt(labelBuffer, i12);
            int i13 = i12 + 4;
            mapLabelItem.pixel20Z = GLConvertUtil.getInt(labelBuffer, i13);
            int i14 = i13 + 4;
            mapLabelItem.type = GLConvertUtil.getInt(labelBuffer, i14);
            int i15 = i14 + 4;
            mapLabelItem.mSublayerId = GLConvertUtil.getInt(labelBuffer, i15);
            int i16 = i15 + 4;
            mapLabelItem.timeStamp = GLConvertUtil.getInt(labelBuffer, i16);
            int i17 = i16 + 4;
            mapLabelItem.mIsFouces = labelBuffer[i17] != 0;
            int i18 = i17 + 1;
            if (labelBuffer[i18] == 0) {
                mapLabelItem.poiid = null;
            } else {
                String str = "";
                for (int i19 = 0; i19 < 20; i19++) {
                    int i20 = i19 + i18;
                    if (labelBuffer[i20] == 0) {
                        break;
                    }
                    str = str + ((char) labelBuffer[i20]);
                }
                mapLabelItem.poiid = str;
            }
            int i21 = i18 + 20;
            int i22 = i21 + 1;
            byte b2 = labelBuffer[i21];
            StringBuffer stringBuffer = new StringBuffer();
            int i23 = i22;
            for (int i24 = 0; i24 < b2; i24++) {
                stringBuffer.append((char) GLConvertUtil.getShort(labelBuffer, i23));
                i23 += 2;
            }
            mapLabelItem.name = stringBuffer.toString();
            arrayList.add(mapLabelItem);
            i6++;
            i7 = i23;
        }
        return arrayList;
    }

    @Override // com.amap.api.mapcore.util.ad
    public void a(double d2, double d3, FPoint fPoint) {
        IPoint obtain = IPoint.obtain();
        a(d2, d3, obtain);
        a(obtain.x, obtain.y, fPoint);
        obtain.recycle();
    }

    @Override // com.amap.api.mapcore.util.ad
    public void a(double d2, double d3, IPoint iPoint) {
        Point latLongToPixels = VirtualEarthProjection.latLongToPixels(d2, d3, 20);
        iPoint.x = latLongToPixels.x;
        iPoint.y = latLongToPixels.y;
    }

    @Override // com.amap.api.mapcore.util.ad
    public void a(float f, float f2, IPoint iPoint) {
        iPoint.x = (int) (f + this.c.getSX());
        iPoint.y = (int) (f2 + this.c.getSY());
    }

    @Override // com.amap.api.mapcore.util.ad
    public void a(int i, float f) {
        if (this.R != null) {
            this.R.a(Integer.valueOf(i), Float.valueOf(f));
        }
    }

    @Override // com.amap.api.mapcore.util.ad
    public void a(int i, int i2) {
        if (this.aT == 0 || i2 != 5) {
            this.aT = i2;
        }
    }

    public void a(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        if (this.g != null) {
            this.g.setServiceViewRect(i, i2, i3, i4, i5, i6, i7);
        }
    }

    public synchronized void a(final int i, final int i2, final int i3, final int i4, final boolean z, final boolean z2, final StyleItem[] styleItemArr) {
        if (this.aP && this.aO && this.a) {
            r(i3);
            queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.l.13
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        l.this.g.setMapModeAndStyle(i, i2, i3, i4, z, z2, styleItemArr);
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            });
        } else {
            this.bd.g = i;
            this.bd.d = i2;
            this.bd.e = i3;
            this.bd.f = i4;
            this.bd.b = true;
        }
    }

    @Override // com.amap.api.mapcore.util.ad
    public void a(int i, int i2, PointF pointF) {
        if (!this.aO || this.Z || this.g == null) {
            return;
        }
        IPoint obtain = IPoint.obtain();
        a(i, i2, obtain);
        pointF.x = obtain.x - this.c.getSX();
        pointF.y = obtain.y - this.c.getSY();
        obtain.recycle();
    }

    @Override // com.amap.api.mapcore.util.ad
    public void a(int i, int i2, DPoint dPoint) {
        DPoint pixelsToLatLong = VirtualEarthProjection.pixelsToLatLong(i, i2, 20);
        dPoint.x = pixelsToLatLong.x;
        dPoint.y = pixelsToLatLong.y;
        pixelsToLatLong.recycle();
    }

    @Override // com.amap.api.mapcore.util.ad
    public void a(int i, int i2, FPoint fPoint) {
        fPoint.x = i - this.c.getSX();
        fPoint.y = i2 - this.c.getSY();
    }

    @Override // com.amap.api.mapcore.util.ad
    public void a(int i, int i2, IPoint iPoint) {
        GLMapState mapState;
        if (!this.aO || this.g == null || (mapState = this.g.getMapState(1)) == null) {
            return;
        }
        mapState.screenToP20Point(i, i2, iPoint);
    }

    @Override // com.amap.api.mapcore.util.ad
    public void a(int i, MotionEvent motionEvent) {
        try {
            this.ah = false;
            m(i);
            this.ag = this.b.a(motionEvent);
            if (this.ag != null && this.ag.isDraggable()) {
                this.af = new Marker((dv) this.ag);
                LatLng position = this.af.getPosition();
                LatLng b2 = this.ag.b();
                if (position != null && b2 != null) {
                    IPoint obtain = IPoint.obtain();
                    b(b2.latitude, b2.longitude, obtain);
                    obtain.y -= 60;
                    DPoint obtain2 = DPoint.obtain();
                    b(obtain.x, obtain.y, obtain2);
                    this.af.setPosition(new LatLng((position.latitude + obtain2.y) - b2.latitude, (position.longitude + obtain2.x) - b2.longitude));
                    this.b.a((dm) this.ag);
                    try {
                        if (this.w != null) {
                            this.w.onMarkerDragStart(this.af);
                        }
                    } catch (Throwable th) {
                        ic.c(th, "AMapDelegateImp", "onMarkerDragStart");
                        th.printStackTrace();
                    }
                    this.ae = true;
                    obtain.recycle();
                    obtain2.recycle();
                }
            } else if (this.C != null) {
                DPoint obtain3 = DPoint.obtain();
                b((int) motionEvent.getX(), (int) motionEvent.getY(), obtain3);
                this.C.onMapLongClick(new LatLng(obtain3.y, obtain3.x));
                this.ai = true;
                obtain3.recycle();
            }
            this.aE.resetTickCount(30);
        } catch (Throwable th2) {
            ic.c(th2, "AMapDelegateImp", "onLongPress");
            th2.printStackTrace();
        }
    }

    @Override // com.amap.api.mapcore.util.ad
    public void a(int i, IPoint iPoint) {
        if (this.c != null) {
            iPoint.x = this.c.getSX();
            iPoint.y = this.c.getSY();
        }
    }

    @Override // com.amap.api.mapcore.util.ad
    public void a(int i, AbstractGestureMapMessage abstractGestureMapMessage) {
        if (!this.aO || this.g == null) {
            return;
        }
        try {
            abstractGestureMapMessage.isUseAnchor = this.aa;
            abstractGestureMapMessage.anchorX = this.c.getAnchorX();
            abstractGestureMapMessage.anchorY = this.c.getAnchorY();
            this.g.addGestureMessage(i, abstractGestureMapMessage, this.O.isGestureScaleByMapCenter(), this.c.getAnchorX(), this.c.getAnchorY());
        } catch (RemoteException unused) {
        }
    }

    public void a(int i, GL10 gl10, int i2, int i3) {
        this.aR = false;
        if (!this.aO) {
            a(i, gl10, (EGLConfig) null);
        }
        this.h = i2;
        this.i = i3;
        this.au = true;
        this.an = new Rect(0, 0, i2, i3);
        this.V = a(i, new Rect(0, 0, this.h, this.i), this.h, this.i);
        if (!this.aP) {
            if (this.c != null) {
                this.c.setMapZoomScale(this.aI);
                this.c.setMapWidth(i2);
                this.c.setMapHeight(i3);
            }
            this.g.setIndoorEnable(this.V, false);
            this.g.setSimple3DEnable(this.V, false);
            this.g.initNativeTexture(this.V);
            this.g.setMapOpenLayer("{\"bounds\" : [{\"x2\" : 235405312,\"x1\" : 188874751,\"y2\" : 85065727,\"y1\" : 122421247}],\"sublyr\" : [{\"type\" : 4,\"sid\" : 9000006,\"zlevel\" : 2}],\"id\" : 9006,\"minzoom\" : 6,\"update_period\" : 90,\"maxzoom\" : 20,\"cachemode\" : 2,\"url\" : \"http://mpsapi.amap.com//ws/mps/lyrdata/ugc/\"}");
        }
        if (this.aD != null) {
            this.aD.a(new al(Opcodes.IFEQ));
        }
        synchronized (this) {
            this.aP = true;
        }
        if (this.aa) {
            this.c.setAnchorX(Math.max(1, Math.min(this.aU, i2 - 1)));
            this.c.setAnchorY(Math.max(1, Math.min(this.aV, i3 - 1)));
        } else {
            this.c.setAnchorX(i2 >> 1);
            this.c.setAnchorY(i3 >> 1);
        }
        this.g.setProjectionCenter(this.V, this.c.getAnchorX(), this.c.getAnchorY());
        this.a = true;
        if (this.bi.b) {
            this.bi.run();
        }
        if (this.bd.b) {
            this.bd.run();
        }
        if (this.be.b) {
            this.be.run();
        }
        if (this.bb.b) {
            this.bb.run();
        }
        if (this.bf.b) {
            this.bf.run();
        }
        if (this.bl.b) {
            this.bl.run();
        }
        if (this.bg.b) {
            this.bg.run();
        }
        if (this.bh.b) {
            this.bh.run();
        }
        if (this.bj.b) {
            this.bj.run();
        }
        if (this.bc.b) {
            this.bc.run();
        }
        if (this.bm.b) {
            this.bm.run();
        }
        if (this.aw != null) {
            this.aw.onSurfaceChanged(gl10, i2, i3);
        }
        if (this.j != null) {
            this.j.post(this.bk);
        }
    }

    public synchronized void a(int i, GL10 gl10, EGLConfig eGLConfig) {
        if (this.ao == 3) {
            this.R.f().a(ga.b);
        } else {
            this.R.f().a(ga.a);
        }
        this.aP = false;
        this.h = this.Q.getWidth();
        this.i = this.Q.getHeight();
        this.aR = false;
        try {
            AeUtil.loadLib(this.f);
            this.g.createAMapInstance(AeUtil.initResource(this.f));
            y(i);
            this.aX = new ef();
            this.T.a(this.aX);
            this.aO = true;
            this.m = gl10.glGetString(7937);
        } catch (Throwable th) {
            ic.c(th, "AMapDElegateImp", "createSurface");
        }
        GLMapState mapState = this.g.getMapState(1);
        if (mapState != null && mapState.getNativeInstance() != 0) {
            mapState.setMapGeoCenter(this.c.getSX(), this.c.getSY());
            mapState.setMapAngle(this.c.getSR());
            mapState.setMapZoomer(this.c.getSZ());
            mapState.setCameraDegree(this.c.getSC());
        }
        this.aY.a(this.f);
        E();
        if (this.aw != null) {
            this.aw.onSurfaceCreated(gl10, eGLConfig);
        }
        u();
    }

    public void a(final int i, final boolean z) {
        if (this.aO && this.aP) {
            resetRenderTime();
            queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.l.15
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        l.this.g.setBuildingEnable(i, z);
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            });
        } else {
            this.bf.c = z;
            this.bf.b = true;
            this.bf.g = i;
        }
    }

    @Override // com.amap.api.mapcore.util.ad
    public void a(Location location) throws RemoteException {
        if (location == null) {
            return;
        }
        try {
            if (this.U && this.ad != null) {
                if (this.ac == null) {
                    this.ac = new dw(this, this.f);
                }
                if (location.getLongitude() != 0.0d && location.getLatitude() != 0.0d) {
                    this.ac.a(location);
                }
                if (this.F != null) {
                    this.F.onMyLocationChange(location);
                }
                resetRenderTime();
                return;
            }
            if (this.ac != null) {
                this.ac.b();
            }
            this.ac = null;
        } catch (Throwable th) {
            ic.c(th, "AMapDelegateImp", "showMyLocationOverlay");
            th.printStackTrace();
        }
    }

    @Override // com.amap.api.mapcore.util.ad
    public void a(am amVar) {
        if (amVar == null || amVar.k() == 0) {
            return;
        }
        synchronized (this.aA) {
            this.aA.add(amVar);
        }
    }

    @Override // com.amap.api.mapcore.util.ad
    public void a(v vVar) throws RemoteException {
        if (vVar == null || this.K == null) {
            return;
        }
        try {
            this.K.a(vVar);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    protected void a(GLMapState gLMapState, int i, int i2, DPoint dPoint) {
        if (!this.aO || this.g == null) {
            return;
        }
        gLMapState.screenToP20Point(i, i2, new Point());
        DPoint pixelsToLatLong = VirtualEarthProjection.pixelsToLatLong(r0.x, r0.y, 20);
        dPoint.x = pixelsToLatLong.x;
        dPoint.y = pixelsToLatLong.y;
        pixelsToLatLong.recycle();
    }

    @Override // com.amap.api.mapcore.util.ad
    public void a(AMapWidgetListener aMapWidgetListener) {
        this.Y = aMapWidgetListener;
    }

    @Override // com.amap.api.mapcore.util.ad
    public void a(AbstractCameraUpdateMessage abstractCameraUpdateMessage) throws RemoteException {
        if (this.g == null || this.W) {
            return;
        }
        if (this.Z && this.g.getStateMessageCount() > 0) {
            AbstractCameraUpdateMessage c2 = aw.c();
            c2.nowType = AbstractCameraUpdateMessage.Type.changeGeoCenterZoomTiltBearing;
            c2.geoPoint = new Point(this.c.getSX(), this.c.getSY());
            c2.zoom = this.c.getSZ();
            c2.bearing = this.c.getSR();
            c2.tilt = this.c.getSC();
            this.g.addMessage(abstractCameraUpdateMessage, false);
            while (this.g.getStateMessageCount() > 0) {
                AbstractCameraUpdateMessage stateMessage = this.g.getStateMessage();
                if (stateMessage != null) {
                    stateMessage.mergeCameraUpdateDelegate(c2);
                }
            }
            abstractCameraUpdateMessage = c2;
        }
        resetRenderTime();
        this.g.clearAnimations(1, false);
        abstractCameraUpdateMessage.isChangeFinished = true;
        c(abstractCameraUpdateMessage);
        this.g.addMessage(abstractCameraUpdateMessage, false);
    }

    public void a(AbstractCameraUpdateMessage abstractCameraUpdateMessage, long j, AMap.CancelableCallback cancelableCallback) {
        if (abstractCameraUpdateMessage == null || this.W || this.g == null) {
            return;
        }
        abstractCameraUpdateMessage.mCallback = cancelableCallback;
        abstractCameraUpdateMessage.mDuration = j;
        if (this.Z || getMapHeight() == 0 || getMapWidth() == 0) {
            try {
                a(abstractCameraUpdateMessage);
                if (abstractCameraUpdateMessage.mCallback != null) {
                    abstractCameraUpdateMessage.mCallback.onFinish();
                    return;
                }
                return;
            } catch (Throwable th) {
                th.printStackTrace();
                return;
            }
        }
        try {
            this.g.interruptAnimation();
            resetRenderTime();
            c(abstractCameraUpdateMessage);
            this.g.addMessage(abstractCameraUpdateMessage, true);
        } catch (Throwable th2) {
            th2.printStackTrace();
        }
    }

    public void a(Runnable runnable) {
        if (this.Q != null) {
            this.Q.post(runnable);
        }
    }

    @Override // com.amap.api.mapcore.util.ej.a
    public void a(String str, ep epVar) {
        setCustomTextureResourcePath(str);
        if (!this.c.isCustomStyleEnable() || epVar == null) {
            return;
        }
        a(epVar.c(), false);
    }

    @Override // com.amap.api.mapcore.util.ad
    public void a(String str, boolean z, int i) {
        if (this.R != null) {
            this.R.a(str, Boolean.valueOf(z), Integer.valueOf(i));
        }
        if (this.O != null) {
            this.O.requestRefreshLogo();
        }
    }

    @Override // com.amap.api.mapcore.util.ad
    public void a(boolean z) {
        if (this.W || this.R == null) {
            return;
        }
        this.R.b(Boolean.valueOf(z));
    }

    protected void a(boolean z, CameraPosition cameraPosition) {
        CameraPosition cameraPosition2;
        if (this.c == null || this.c.getChangedCounter() == 0) {
            return;
        }
        try {
            if (!this.aL && this.g.getAnimateionsCount() == 0 && this.g.getStateMessageCount() == 0) {
                this.c.resetChangedCounter();
                if (this.J != null) {
                    this.J.onMapStable();
                }
                if (this.y != null && this.Q.isEnabled()) {
                    if (cameraPosition == null) {
                        try {
                            cameraPosition2 = getCameraPosition();
                        } catch (Throwable th) {
                            ic.c(th, "AMapDelegateImp", "cameraChangeFinish");
                            th.printStackTrace();
                        }
                        this.y.onCameraChangeFinish(cameraPosition2);
                    }
                    cameraPosition2 = cameraPosition;
                    this.y.onCameraChangeFinish(cameraPosition2);
                }
            }
        } catch (Throwable th2) {
            th2.printStackTrace();
        }
    }

    @Override // com.amap.api.mapcore.util.ad
    public void a(boolean z, boolean z2) {
        if (!this.aO || this.W) {
            this.be.b = true;
            this.be.c = z;
            return;
        }
        boolean z3 = z2 ? z2 : false;
        if (TextUtils.isEmpty(this.c.getCustomStylePath()) && TextUtils.isEmpty(this.c.getCustomStyleID())) {
            return;
        }
        if (z) {
            try {
                if (this.c.isProFunctionAuthEnable() && !TextUtils.isEmpty(this.c.getCustomStyleID()) && this.aB != null) {
                    this.aB.a(this.c.getCustomStyleID());
                    this.aB.b();
                }
            } catch (Throwable th) {
                th.printStackTrace();
                return;
            }
        }
        if (z2 || this.P || (this.c.isCustomStyleEnable() ^ z)) {
            a(z, (byte[]) null, z3);
        }
        this.P = false;
    }

    @Override // com.amap.api.mapcore.util.ad
    public void a(boolean z, byte[] bArr) {
        a(z, bArr, false);
    }

    public void a(boolean z, byte[] bArr, boolean z2) {
        ep epVar;
        this.c.setCustomStyleEnable(z);
        if (this.c.isHideLogoEnable()) {
            this.O.setLogoEnable(!z);
        }
        boolean z3 = false;
        if (!z) {
            c(1, false);
            a(1, this.c.getMapStyleMode(), this.c.getMapStyleTime(), this.c.getMapStyleState(), true, false, (StyleItem[]) null);
            return;
        }
        c(1, true);
        eo eoVar = new eo(this.f);
        if (this.ap != null && this.ap.getTrafficRoadBackgroundColor() != -1) {
            eoVar.a(this.ap.getTrafficRoadBackgroundColor());
        }
        if (this.c.isProFunctionAuthEnable() && !TextUtils.isEmpty(this.c.getCustomTextureResourcePath())) {
            z3 = true;
        }
        StyleItem[] styleItemArr = null;
        if (bArr != null) {
            epVar = eoVar.a(bArr, z3);
            if (epVar != null && (styleItemArr = epVar.c()) != null) {
                this.c.setUseProFunction(true);
            }
        } else {
            epVar = null;
        }
        if (styleItemArr == null && (epVar = eoVar.a(this.c.getCustomStylePath(), z3)) != null) {
            styleItemArr = epVar.c();
        }
        if (eoVar.a() != 0) {
            this.c.setCustomBackgroundColor(eoVar.a());
        }
        if (epVar == null || epVar.d() == null) {
            a(styleItemArr, z2);
        } else if (this.aC != null) {
            this.aC.a((String) epVar.d());
            this.aC.a(epVar);
            this.aC.b();
        }
    }

    protected void a(StyleItem[] styleItemArr, boolean z) {
        if (!(z || (styleItemArr != null && styleItemArr.length > 0))) {
            fp.a(this.f, false);
        } else {
            a(1, 0, 0, 0, true, true, styleItemArr);
            fp.a(this.f, true);
        }
    }

    public boolean a(int i, int i2, int i3) {
        AbstractCameraUpdateMessage a2;
        if (!this.aO || ((int) b(i)) >= this.c.getMaxZoomLevel()) {
            return false;
        }
        try {
            if (this.aa) {
                a2 = aw.a(1.0f, (Point) null);
            } else if (this.O.isZoomInByScreenCenter()) {
                a2 = aw.a(1.0f, (Point) null);
            } else {
                this.k.x = i2;
                this.k.y = i3;
                a2 = aw.a(1.0f, this.k);
            }
            b(a2);
        } catch (Throwable th) {
            ic.c(th, "AMapDelegateImp", "onDoubleTap");
            th.printStackTrace();
        }
        resetRenderTime();
        return true;
    }

    @Override // com.amap.api.mapcore.util.ad
    public boolean a(String str) throws RemoteException {
        resetRenderTime();
        return this.T.removeOverlay(str);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public Arc addArc(ArcOptions arcOptions) throws RemoteException {
        resetRenderTime();
        di a2 = this.T.a(arcOptions);
        if (a2 != null) {
            return new Arc(a2);
        }
        return null;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public BuildingOverlay addBuildingOverlay() {
        try {
            dj a2 = this.T.a();
            if (a2 != null) {
                return new BuildingOverlay(a2);
            }
            return null;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public Circle addCircle(CircleOptions circleOptions) throws RemoteException {
        resetRenderTime();
        dk a2 = this.T.a(circleOptions);
        if (a2 != null) {
            return new Circle(a2);
        }
        return null;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public CrossOverlay addCrossVector(CrossOverlayOptions crossOverlayOptions) {
        if (crossOverlayOptions == null || crossOverlayOptions.getRes() == null) {
            return null;
        }
        CrossVectorOverlay crossVectorOverlay = new CrossVectorOverlay(1, v(), this);
        if (crossOverlayOptions != null) {
            crossVectorOverlay.setAttribute(crossOverlayOptions.getAttribute());
        }
        if (this.g != null) {
            this.g.getOverlayBundle(1).addOverlay(crossVectorOverlay);
            crossVectorOverlay.resumeMarker(crossOverlayOptions.getRes());
        }
        return new CrossOverlay(crossOverlayOptions, crossVectorOverlay);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public GL3DModel addGLModel(GL3DModelOptions gL3DModelOptions) {
        return this.X.a(gL3DModelOptions);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public GroundOverlay addGroundOverlay(GroundOverlayOptions groundOverlayOptions) throws RemoteException {
        resetRenderTime();
        dl a2 = this.T.a(groundOverlayOptions);
        if (a2 != null) {
            return new GroundOverlay(a2);
        }
        return null;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public Marker addMarker(MarkerOptions markerOptions) throws RemoteException {
        resetRenderTime();
        return this.b.a(markerOptions);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public ArrayList<Marker> addMarkers(ArrayList<MarkerOptions> arrayList, boolean z) throws RemoteException {
        resetRenderTime();
        return this.b.a(arrayList, z);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public MultiPointOverlay addMultiPointOverlay(MultiPointOverlayOptions multiPointOverlayOptions) throws RemoteException {
        resetRenderTime();
        IMultiPointOverlay a2 = this.aZ.a(multiPointOverlayOptions);
        if (a2 != null) {
            return new MultiPointOverlay(a2);
        }
        return null;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public RouteOverlay addNaviRouteOverlay() {
        return null;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public NavigateArrow addNavigateArrow(NavigateArrowOptions navigateArrowOptions) throws RemoteException {
        resetRenderTime();
        dn a2 = this.T.a(navigateArrowOptions);
        if (a2 != null) {
            return new NavigateArrow(a2);
        }
        return null;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void addOverlayTexture(int i, GLTextureProperty gLTextureProperty) {
        GLOverlayBundle overlayBundle;
        if (this.g == null || (overlayBundle = this.g.getOverlayBundle(i)) == null || gLTextureProperty == null || gLTextureProperty.mBitmap == null) {
            return;
        }
        this.g.addOverlayTexture(i, gLTextureProperty);
        overlayBundle.addOverlayTextureItem(gLTextureProperty.mId, gLTextureProperty.mAnchor, gLTextureProperty.mXRatio, gLTextureProperty.mYRatio, gLTextureProperty.mBitmap.getWidth(), gLTextureProperty.mBitmap.getHeight());
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public ParticleOverlay addParticleOverlay(ParticleOverlayOptions particleOverlayOptions) {
        try {
            dq a2 = this.T.a(particleOverlayOptions);
            if (a2 != null) {
                return new ParticleOverlay(a2);
            }
            return null;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public Polygon addPolygon(PolygonOptions polygonOptions) throws RemoteException {
        resetRenderTime();
        dr a2 = this.T.a(polygonOptions);
        if (a2 != null) {
            return new Polygon(a2);
        }
        return null;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public Polyline addPolyline(PolylineOptions polylineOptions) throws RemoteException {
        resetRenderTime();
        ds a2 = this.T.a(polylineOptions);
        if (a2 != null) {
            return new Polyline(a2);
        }
        return null;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public Text addText(TextOptions textOptions) throws RemoteException {
        resetRenderTime();
        return this.b.a(textOptions);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public TileOverlay addTileOverlay(TileOverlayOptions tileOverlayOptions) throws RemoteException {
        if (this.S == null || MapsInitializer.isTileOverlayClosed()) {
            return null;
        }
        return this.S.a(tileOverlayOptions);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMapListener
    public void afterAnimation() {
        j();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMapListener
    public void afterDrawFrame(int i, GLMapState gLMapState) {
        float mapZoomer = gLMapState.getMapZoomer();
        if (!(this.g != null && (this.g.isInMapAction(i) || this.g.isInMapAnimation(i)))) {
            if (this.az != -1) {
                this.aE.setRenderFps(this.az);
            } else {
                this.aE.setRenderFps(15.0f);
            }
            if (this.aN == 1) {
                this.aN = 0;
            }
            if (this.aH != mapZoomer) {
                this.aH = mapZoomer;
            }
        }
        if (this.aR) {
            return;
        }
        this.aR = true;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMapListener
    public void afterDrawLabel(int i, GLMapState gLMapState) {
        p();
        if (this.g != null) {
            this.g.pushRendererState();
        }
        if (this.S != null && !MapsInitializer.isTileOverlayClosed()) {
            this.S.b();
        }
        this.T.a(false, this.av);
        if (this.aZ != null) {
            this.aZ.a(this.c, getViewMatrix(), getProjectionMatrix());
        }
        if (this.X != null) {
            this.X.a();
        }
        if (this.b != null) {
            this.b.a(false);
        }
        if (this.L != null) {
            this.L.b(getMapWidth(), getMapHeight());
        }
        if (this.g != null) {
            this.g.popRendererState();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMapListener
    public void afterRendererOver(int i, GLMapState gLMapState) {
        if (this.g != null) {
            this.g.pushRendererState();
        }
        if (this.b != null) {
            this.b.a(true);
        }
        if (this.g != null) {
            this.g.popRendererState();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void animateCamera(CameraUpdate cameraUpdate) throws RemoteException {
        if (cameraUpdate == null) {
            return;
        }
        b(cameraUpdate.getCameraUpdateFactoryDelegate());
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void animateCameraWithCallback(CameraUpdate cameraUpdate, AMap.CancelableCallback cancelableCallback) throws RemoteException {
        if (cameraUpdate == null) {
            return;
        }
        animateCameraWithDurationAndCallback(cameraUpdate, 250L, cancelableCallback);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void animateCameraWithDurationAndCallback(CameraUpdate cameraUpdate, long j, AMap.CancelableCallback cancelableCallback) {
        if (cameraUpdate == null) {
            return;
        }
        a(cameraUpdate.getCameraUpdateFactoryDelegate(), j, cancelableCallback);
    }

    public float b(int i) {
        if (this.c != null) {
            return getMapConfig().getSZ();
        }
        return 0.0f;
    }

    @Override // com.amap.api.mapcore.util.ad
    public void b() {
        if (this.S == null || MapsInitializer.isTileOverlayClosed()) {
            return;
        }
        this.S.h();
    }

    @Override // com.amap.api.mapcore.util.ad
    public void b(double d2, double d3, IPoint iPoint) {
        if (!this.aO || this.g == null) {
            return;
        }
        try {
            Point latLongToPixels = VirtualEarthProjection.latLongToPixels(d2, d3, 20);
            FPoint obtain = FPoint.obtain();
            b(latLongToPixels.x, latLongToPixels.y, obtain);
            float f = obtain.x;
            float f2 = IDataParser.CODE_NET_UNAVAILABLE;
            if (f == f2 && obtain.y == f2) {
                GLMapState newMapState = this.g.getNewMapState(1);
                newMapState.setCameraDegree(0.0f);
                newMapState.recalculate();
                newMapState.p20ToScreenPoint(latLongToPixels.x, latLongToPixels.y, obtain);
                newMapState.recycle();
            }
            iPoint.x = (int) obtain.x;
            iPoint.y = (int) obtain.y;
            obtain.recycle();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.amap.api.mapcore.util.ad
    public void b(int i, int i2) {
        if (this.c != null) {
            this.h = i;
            this.i = i2;
            this.c.setMapWidth(i);
            this.c.setMapHeight(i2);
        }
    }

    public synchronized void b(int i, int i2, int i3, int i4) {
        a(i, i2, i3, i4, false, false, (StyleItem[]) null);
    }

    @Override // com.amap.api.mapcore.util.ad
    public void b(int i, int i2, DPoint dPoint) {
        GLMapState mapState;
        if (!this.aO || this.g == null || (mapState = this.g.getMapState(1)) == null) {
            return;
        }
        IPoint obtain = IPoint.obtain();
        mapState.screenToP20Point(i, i2, obtain);
        DPoint pixelsToLatLong = VirtualEarthProjection.pixelsToLatLong(obtain.x, obtain.y, 20);
        dPoint.x = pixelsToLatLong.x;
        dPoint.y = pixelsToLatLong.y;
        obtain.recycle();
        pixelsToLatLong.recycle();
    }

    public void b(int i, int i2, FPoint fPoint) {
        GLMapState mapState;
        if (!this.aO || this.g == null || (mapState = this.g.getMapState(1)) == null) {
            return;
        }
        mapState.p20ToScreenPoint(i, i2, fPoint);
    }

    public void b(final int i, final boolean z) {
        if (this.aO && this.aP) {
            resetRenderTime();
            queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.l.16
                @Override // java.lang.Runnable
                public void run() {
                    if (l.this.g != null) {
                        if (z) {
                            l.this.g.setAllContentEnable(i, true);
                        } else {
                            l.this.g.setAllContentEnable(i, false);
                        }
                        l.this.g.setSimple3DEnable(i, false);
                    }
                }
            });
        } else {
            this.bi.c = z;
            this.bi.b = true;
            this.bi.g = i;
        }
    }

    @Override // com.amap.api.mapcore.util.ad
    public void b(AbstractCameraUpdateMessage abstractCameraUpdateMessage) throws RemoteException {
        a(abstractCameraUpdateMessage, 250L, (AMap.CancelableCallback) null);
    }

    @Override // com.amap.api.mapcore.util.ad
    public void b(boolean z) {
        if (this.W || this.R == null) {
            return;
        }
        this.R.a(Boolean.valueOf(z));
    }

    @Override // com.amap.api.mapcore.util.ad
    public boolean b(int i, MotionEvent motionEvent) {
        if (!this.aO) {
            return false;
        }
        a(i, (int) motionEvent.getX(), (int) motionEvent.getY());
        return false;
    }

    @Override // com.amap.api.mapcore.util.ad
    public boolean b(String str) {
        try {
            this.X.a(str);
            return false;
        } catch (Throwable th) {
            ic.c(th, "AMapDelegateImp", "removeGLModel");
            th.printStackTrace();
            return false;
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMapListener
    public void beforeDrawLabel(int i, GLMapState gLMapState) {
        p();
        if (this.g != null) {
            this.g.pushRendererState();
        }
        this.T.a(true, this.av);
        if (this.g != null) {
            this.g.popRendererState();
        }
    }

    @Override // com.amap.api.mapcore.util.ad
    public GLMapState c() {
        if (this.g != null) {
            return this.g.getMapState(1);
        }
        return null;
    }

    @Override // com.amap.api.mapcore.util.ad
    public void c(int i) {
        if (this.aO && ((int) b(i)) > this.c.getMinZoomLevel()) {
            try {
                b(aw.b());
            } catch (Throwable th) {
                ic.c(th, "AMapDelegateImp", "onDoubleTap");
                th.printStackTrace();
            }
            resetRenderTime();
        }
    }

    public void c(final int i, final boolean z) {
        if (this.aO && this.aP) {
            resetRenderTime();
            queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.l.17
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        if (z) {
                            l.this.g.setBuildingTextureEnable(i, true);
                        } else {
                            l.this.g.setBuildingTextureEnable(i, false);
                        }
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            });
        } else {
            this.bl.c = z;
            this.bl.b = true;
            this.bl.g = i;
        }
    }

    @Override // com.amap.api.mapcore.util.ad
    public void c(String str) {
        synchronized (this.aA) {
            int size = this.aA.size();
            int i = -1;
            int i2 = 0;
            while (true) {
                if (i2 >= size) {
                    break;
                }
                if (this.aA.get(i2).o().equals(str)) {
                    i = i2;
                    break;
                }
                i2++;
            }
            if (i >= 0) {
                this.aA.remove(i);
            }
        }
    }

    @Override // com.amap.api.mapcore.util.ad
    public void c(boolean z) {
        if (this.W || this.R == null) {
            return;
        }
        this.R.c(Boolean.valueOf(z));
    }

    @Override // com.amap.api.mapcore.util.ad
    public boolean c(int i, MotionEvent motionEvent) {
        if (!this.aO) {
            return false;
        }
        try {
            if (g(motionEvent) || e(motionEvent) || f(motionEvent) || d(motionEvent)) {
                return true;
            }
            b(motionEvent);
            return true;
        } catch (Throwable th) {
            ic.c(th, "AMapDelegateImp", "onSingleTapUp");
            th.printStackTrace();
            return true;
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public Pair<Float, LatLng> calculateZoomToSpanLevel(int i, int i2, int i3, int i4, LatLng latLng, LatLng latLng2) {
        if (latLng != null && latLng2 != null && i == i2 && i2 == i3 && i3 == i4 && latLng.latitude == latLng2.latitude && latLng.longitude == latLng2.longitude) {
            return new Pair<>(Float.valueOf(getMaxZoomLevel()), latLng);
        }
        MapConfig mapConfig = getMapConfig();
        if (latLng == null || latLng2 == null || !this.aO || this.W) {
            DPoint obtain = DPoint.obtain();
            GLMapState.geo2LonLat(mapConfig.getSX(), mapConfig.getSY(), obtain);
            Pair<Float, LatLng> pair = new Pair<>(Float.valueOf(mapConfig.getSZ()), new LatLng(obtain.y, obtain.x));
            obtain.recycle();
            return pair;
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(latLng);
        builder.include(latLng2);
        GLMapState gLMapState = new GLMapState(1, this.g.getNativeInstance());
        Pair<Float, IPoint> a2 = fr.a(mapConfig, i, i2, i3, i4, builder.build(), getMapWidth(), getMapHeight());
        gLMapState.recycle();
        if (a2 == null) {
            return null;
        }
        DPoint obtain2 = DPoint.obtain();
        GLMapState.geo2LonLat(((IPoint) a2.second).x, ((IPoint) a2.second).y, obtain2);
        Pair<Float, LatLng> pair2 = new Pair<>(a2.first, new LatLng(obtain2.y, obtain2.x));
        obtain2.recycle();
        return pair2;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public boolean canStopMapRender() {
        if (this.g != null) {
            this.g.canStopMapRender(1);
        }
        return true;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void changeSurface(GL10 gl10, int i, int i2) {
        try {
            a(1, gl10, i, i2);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void checkMapState(GLMapState gLMapState) {
        IPoint[] iPointArr;
        if (this.c == null || this.W) {
            return;
        }
        LatLngBounds limitLatLngBounds = this.c.getLimitLatLngBounds();
        try {
            if (limitLatLngBounds == null) {
                if (this.c.isSetLimitZoomLevel()) {
                    gLMapState.setMapZoomer(Math.max(this.c.getMinZoomLevel(), Math.min(gLMapState.getMapZoomer(), this.c.getMaxZoomLevel())));
                    return;
                }
                return;
            }
            IPoint[] limitIPoints = this.c.getLimitIPoints();
            if (limitIPoints == null) {
                IPoint obtain = IPoint.obtain();
                GLMapState.lonlat2Geo(limitLatLngBounds.northeast.longitude, limitLatLngBounds.northeast.latitude, obtain);
                IPoint obtain2 = IPoint.obtain();
                GLMapState.lonlat2Geo(limitLatLngBounds.southwest.longitude, limitLatLngBounds.southwest.latitude, obtain2);
                iPointArr = new IPoint[]{obtain, obtain2};
                this.c.setLimitIPoints(iPointArr);
            } else {
                iPointArr = limitIPoints;
            }
            float b2 = fr.b(this.c, iPointArr[0].x, iPointArr[0].y, iPointArr[1].x, iPointArr[1].y, getMapWidth(), getMapHeight());
            float mapZoomer = gLMapState.getMapZoomer();
            if (this.c.isSetLimitZoomLevel()) {
                float maxZoomLevel = this.c.getMaxZoomLevel();
                float minZoomLevel = this.c.getMinZoomLevel();
                mapZoomer = Math.max(b2, Math.min(mapZoomer, maxZoomLevel));
                if (b2 > maxZoomLevel) {
                    mapZoomer = maxZoomLevel;
                }
                if (mapZoomer < minZoomLevel) {
                    b2 = minZoomLevel;
                }
                b2 = mapZoomer;
            } else {
                if (b2 > 0.0f && mapZoomer < b2) {
                }
                b2 = mapZoomer;
            }
            gLMapState.setMapZoomer(b2);
            IPoint obtain3 = IPoint.obtain();
            gLMapState.getMapGeoCenter(obtain3);
            int i = obtain3.x;
            int i2 = obtain3.y;
            int[] a2 = fr.a(iPointArr[0].x, iPointArr[0].y, iPointArr[1].x, iPointArr[1].y, this.c, gLMapState, i, i2);
            if (a2 != null && a2.length == 2) {
                i = a2[0];
                i2 = a2[1];
            }
            gLMapState.setMapGeoCenter(i, i2);
            obtain3.recycle();
        } catch (Throwable unused) {
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void clear() throws RemoteException {
        try {
            clear(false);
        } catch (Throwable th) {
            ic.c(th, "AMapDelegateImp", "clear");
            th.printStackTrace();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x003f A[Catch: Throwable -> 0x005c, TryCatch #0 {Throwable -> 0x005c, blocks: (B:2:0x0000, B:5:0x000a, B:6:0x001d, B:8:0x0026, B:10:0x002c, B:11:0x0031, B:13:0x003f, B:14:0x0044, B:16:0x0048, B:17:0x004d, B:19:0x0051, B:20:0x0058, B:24:0x0017), top: B:1:0x0000 }] */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0048 A[Catch: Throwable -> 0x005c, TryCatch #0 {Throwable -> 0x005c, blocks: (B:2:0x0000, B:5:0x000a, B:6:0x001d, B:8:0x0026, B:10:0x002c, B:11:0x0031, B:13:0x003f, B:14:0x0044, B:16:0x0048, B:17:0x004d, B:19:0x0051, B:20:0x0058, B:24:0x0017), top: B:1:0x0000 }] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0051 A[Catch: Throwable -> 0x005c, TryCatch #0 {Throwable -> 0x005c, blocks: (B:2:0x0000, B:5:0x000a, B:6:0x001d, B:8:0x0026, B:10:0x002c, B:11:0x0031, B:13:0x003f, B:14:0x0044, B:16:0x0048, B:17:0x004d, B:19:0x0051, B:20:0x0058, B:24:0x0017), top: B:1:0x0000 }] */
    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void clear(boolean r3) throws android.os.RemoteException {
        /*
            r2 = this;
            r2.i()     // Catch: java.lang.Throwable -> L5c
            com.amap.api.mapcore.util.dw r0 = r2.ac     // Catch: java.lang.Throwable -> L5c
            r1 = 0
            if (r0 == 0) goto L1c
            if (r3 == 0) goto L17
            com.amap.api.mapcore.util.dw r3 = r2.ac     // Catch: java.lang.Throwable -> L5c
            java.lang.String r1 = r3.c()     // Catch: java.lang.Throwable -> L5c
            com.amap.api.mapcore.util.dw r3 = r2.ac     // Catch: java.lang.Throwable -> L5c
            java.lang.String r3 = r3.d()     // Catch: java.lang.Throwable -> L5c
            goto L1d
        L17:
            com.amap.api.mapcore.util.dw r3 = r2.ac     // Catch: java.lang.Throwable -> L5c
            r3.e()     // Catch: java.lang.Throwable -> L5c
        L1c:
            r3 = r1
        L1d:
            com.amap.api.mapcore.util.ab r0 = r2.T     // Catch: java.lang.Throwable -> L5c
            r0.b(r3)     // Catch: java.lang.Throwable -> L5c
            com.amap.api.mapcore.util.aq r3 = r2.S     // Catch: java.lang.Throwable -> L5c
            if (r3 == 0) goto L31
            boolean r3 = com.amap.api.maps.MapsInitializer.isTileOverlayClosed()     // Catch: java.lang.Throwable -> L5c
            if (r3 != 0) goto L31
            com.amap.api.mapcore.util.aq r3 = r2.S     // Catch: java.lang.Throwable -> L5c
            r3.c()     // Catch: java.lang.Throwable -> L5c
        L31:
            com.amap.api.mapcore.util.aj r3 = r2.b     // Catch: java.lang.Throwable -> L5c
            r3.a(r1)     // Catch: java.lang.Throwable -> L5c
            com.amap.api.mapcore.util.y r3 = r2.X     // Catch: java.lang.Throwable -> L5c
            r3.b()     // Catch: java.lang.Throwable -> L5c
            com.amap.api.mapcore.util.ge r3 = r2.R     // Catch: java.lang.Throwable -> L5c
            if (r3 == 0) goto L44
            com.amap.api.mapcore.util.ge r3 = r2.R     // Catch: java.lang.Throwable -> L5c
            r3.l()     // Catch: java.lang.Throwable -> L5c
        L44:
            com.amap.api.mapcore.util.bm r3 = r2.aZ     // Catch: java.lang.Throwable -> L5c
            if (r3 == 0) goto L4d
            com.amap.api.mapcore.util.bm r3 = r2.aZ     // Catch: java.lang.Throwable -> L5c
            r3.c()     // Catch: java.lang.Throwable -> L5c
        L4d:
            com.autonavi.ae.gmap.GLMapEngine r3 = r2.g     // Catch: java.lang.Throwable -> L5c
            if (r3 == 0) goto L58
            com.autonavi.ae.gmap.GLMapEngine r3 = r2.g     // Catch: java.lang.Throwable -> L5c
            int r0 = r2.V     // Catch: java.lang.Throwable -> L5c
            r3.removeNativeAllOverlay(r0)     // Catch: java.lang.Throwable -> L5c
        L58:
            r2.resetRenderTime()     // Catch: java.lang.Throwable -> L5c
            goto L67
        L5c:
            r3 = move-exception
            java.lang.String r0 = "AMapDelegateImp"
            java.lang.String r1 = "clear"
            com.amap.api.mapcore.util.ic.c(r3, r0, r1)
            r3.printStackTrace()
        L67:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.l.clear(boolean):void");
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public long createGLOverlay(int i) {
        if (this.g != null) {
            return this.g.createOverlay(1, i);
        }
        return 0L;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void createSurface(GL10 gl10, EGLConfig eGLConfig) {
        try {
            a(1, gl10, eGLConfig);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.amap.api.mapcore.util.ad
    public int d() {
        if (this.aY != null) {
            return this.aY.a();
        }
        return 0;
    }

    @Override // com.amap.api.mapcore.util.ad
    public String d(String str) {
        if (this.T != null) {
            return this.T.a(str);
        }
        return null;
    }

    @Override // com.amap.api.mapcore.util.ad
    public void d(boolean z) {
        if (this.W || this.R == null) {
            return;
        }
        this.R.d(Boolean.valueOf(z));
    }

    @Override // com.amap.api.mapcore.util.ad
    public boolean d(int i) {
        return false;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void destroy() {
        this.W = true;
        try {
            if (this.aD != null) {
                this.aD.a();
            }
            if (this.aZ != null) {
                this.aZ.b();
            }
            if (this.ad != null) {
                this.ad.deactivate();
            }
            this.ad = null;
            this.aW = null;
            if (this.aE != null) {
                this.aE.renderPause();
            }
            if (this.aY != null) {
                this.aY.d();
            }
            if (this.aF != null) {
                this.aF.a((AMapGestureListener) null);
                this.aF.b();
                this.aF = null;
            }
            if (this.T != null) {
                this.T.d();
            }
            if (this.b != null) {
                this.b.i();
            }
            if (this.S != null && !MapsInitializer.isTileOverlayClosed()) {
                this.S.f();
            }
            B();
            if (this.aq != null) {
                this.aq.interrupt();
                this.aq = null;
            }
            if (this.ar != null) {
                this.ar.interrupt();
                this.ar = null;
            }
            if (this.aB != null) {
                this.aB.a();
                this.aB = null;
            }
            if (this.aC != null) {
                this.aC.a((ej.a) null);
                this.aC.a();
                this.aC = null;
            }
            fb.b();
            if (this.g != null) {
                this.g.setMapListener(null);
                this.g.releaseNetworkState();
                queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.l.29
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            if (l.this.aY != null) {
                                l.this.aY.c();
                            }
                            if (l.this.aX != null) {
                                l.this.aX.b();
                                l.this.aX = null;
                            }
                            if (l.this.g != null) {
                                l.this.g.getOverlayBundle(l.this.V).removeAll(true);
                                l.this.g.destroyAMapEngine();
                                l.this.g = null;
                            }
                            l.this.X.d();
                        } catch (Throwable th) {
                            th.printStackTrace();
                        }
                    }
                });
                int i = 0;
                while (this.g != null) {
                    int i2 = i + 1;
                    if (i >= 20) {
                        break;
                    }
                    try {
                        Thread.sleep(50L);
                    } catch (InterruptedException unused) {
                    }
                    i = i2;
                }
            }
            if (this.X != null) {
                this.X.c();
            }
            if (this.K != null) {
                this.K.b();
            }
            if (this.Q != null) {
                try {
                    this.Q.b();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (this.R != null) {
                this.R.k();
                this.R = null;
            }
            if (this.ac != null) {
                this.ac.b();
                this.ac = null;
            }
            this.ad = null;
            H();
            this.ap = null;
            ic.b();
        } catch (Throwable th) {
            ic.c(th, "AMapDelegateImp", "destroy");
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void destroySurface(int i) {
        this.aS.lock();
        try {
            if (this.aO) {
                this.g.destroyAMapEngine();
            }
            this.aO = false;
            this.aP = false;
            this.aR = false;
        } catch (Throwable th) {
            this.aS.unlock();
            throw th;
        }
        this.aS.unlock();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void drawFrame(GL10 gl10) {
        if (this.W || this.g == null) {
            return;
        }
        a(1, gl10);
        this.g.renderAMap();
        this.g.pushRendererState();
        if (this.aw != null) {
            this.aw.onDrawFrame(gl10);
        }
        if (this.ba != null) {
            this.ba.a();
        }
        a(gl10);
        D();
        if (!this.aQ) {
            this.aQ = true;
        }
        this.g.popRendererState();
        if (this.aD != null) {
            this.aD.a(new al(Opcodes.IFEQ));
        }
        if (fi.a()) {
            try {
                if (this.Q instanceof o) {
                    if (this.e == null) {
                        this.e = new fi();
                    }
                    this.e.c();
                    if (!this.e.d() || this.e.b()) {
                        return;
                    }
                    if (this.e.a(((o) this.Q).getBitmap())) {
                        removecache();
                    }
                }
            } catch (Throwable th) {
                ic.c(th, "AMapDelegateImp", "PureScreenCheckTool.checkBlackScreen");
            }
        }
    }

    @Override // com.amap.api.mapcore.util.ad
    public int e() {
        if (this.aY != null) {
            return this.aY.b();
        }
        return 0;
    }

    @Override // com.amap.api.mapcore.util.ad
    public void e(boolean z) {
        if (this.W || this.R == null) {
            return;
        }
        this.R.e(Boolean.valueOf(z));
    }

    @Override // com.amap.api.mapcore.util.ad
    public boolean e(int i) {
        return c(i, 7);
    }

    @Override // com.amap.api.mapcore.util.ad
    public int f(int i) {
        if (this.aY != null) {
            return this.aY.a(i);
        }
        return 0;
    }

    public CameraPosition f(boolean z) {
        LatLng A;
        try {
            if (this.c == null) {
                return null;
            }
            if (!this.aO || this.Z || this.g == null) {
                DPoint obtain = DPoint.obtain();
                a(this.c.getSX(), this.c.getSY(), obtain);
                LatLng latLng = new LatLng(obtain.y, obtain.x);
                obtain.recycle();
                return CameraPosition.builder().target(latLng).bearing(this.c.getSR()).tilt(this.c.getSC()).zoom(this.c.getSZ()).build();
            }
            if (z) {
                DPoint obtain2 = DPoint.obtain();
                b(this.c.getAnchorX(), this.c.getAnchorY(), obtain2);
                A = new LatLng(obtain2.y, obtain2.x, false);
                obtain2.recycle();
            } else {
                A = A();
            }
            return CameraPosition.builder().target(A).bearing(this.c.getSR()).tilt(this.c.getSC()).zoom(this.c.getSZ()).build();
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    @Override // com.amap.api.mapcore.util.ad
    public void f() {
        this.T.e();
    }

    @Override // com.amap.api.mapcore.util.ad
    public float g() {
        return b(this.V);
    }

    @Override // com.amap.api.mapcore.util.ad
    public void g(int i) {
        if (this.W || this.R == null) {
            return;
        }
        this.R.a(Integer.valueOf(i));
    }

    void g(boolean z) {
        this.j.obtainMessage(17, z ? 1 : 0, 0).sendToTarget();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public Projection getAMapProjection() throws RemoteException {
        return new Projection(this.N);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public UiSettings getAMapUiSettings() throws RemoteException {
        if (this.M == null) {
            this.M = new UiSettings(this.O);
        }
        return this.M;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public AMapCameraInfo getCamerInfo() {
        return null;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public float getCameraAngle() {
        return o(this.V);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public CameraPosition getCameraPosition() throws RemoteException {
        return f(this.aa);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public long getGlOverlayMgrPtr() {
        if (this.g != null) {
            return this.g.getGlOverlayMgrPtr(1);
        }
        return 0L;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public InfoWindowAnimationManager getInfoWindowAnimationManager() {
        return new InfoWindowAnimationManager(this.L);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void getLatLngRect(DPoint[] dPointArr) {
        try {
            Rectangle geoRectangle = this.c.getGeoRectangle();
            if (geoRectangle != null) {
                IPoint[] clipRect = geoRectangle.getClipRect();
                for (int i = 0; i < 4; i++) {
                    GLMapState.geo2LonLat(clipRect[i].x, clipRect[i].y, dPointArr[i]);
                }
            }
        } catch (Throwable unused) {
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public Handler getMainHandler() {
        return this.j;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public MapConfig getMapConfig() {
        return this.c;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public String getMapContentApprovalNumber() {
        if (this.c == null || this.c.isCustomStyleEnable()) {
            return null;
        }
        String a2 = fh.a(this.f, "approval_number", "mc", "");
        return !TextUtils.isEmpty(a2) ? a2 : "GS（2017）3426号 | GS（2017）2550号";
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public int getMapHeight() {
        return this.i;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void getMapPrintScreen(AMap.onMapPrintScreenListener onmapprintscreenlistener) {
        this.H = onmapprintscreenlistener;
        this.al = true;
        resetRenderTime();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public List<Marker> getMapScreenMarkers() throws RemoteException {
        return !fr.b(getMapWidth(), getMapHeight()) ? new ArrayList() : this.b.e();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void getMapScreenShot(AMap.OnMapScreenShotListener onMapScreenShotListener) {
        this.I = onMapScreenShotListener;
        this.al = true;
        resetRenderTime();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public int getMapTextZIndex() throws RemoteException {
        return this.av;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public int getMapType() throws RemoteException {
        return this.ao;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public int getMapWidth() {
        return this.h;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public float getMaxZoomLevel() {
        if (this.c != null) {
            return this.c.getMaxZoomLevel();
        }
        return 20.0f;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public float getMinZoomLevel() {
        if (this.c != null) {
            return this.c.getMinZoomLevel();
        }
        return 3.0f;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public Location getMyLocation() throws RemoteException {
        if (this.ad != null) {
            return this.G.a;
        }
        return null;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public MyLocationStyle getMyLocationStyle() throws RemoteException {
        if (this.ac != null) {
            return this.ac.a();
        }
        return null;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public float[] getProjectionMatrix() {
        return this.c != null ? this.c.getProjectionMatrix() : this.p;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public int getRenderMode() {
        return this.Q.getRenderMode();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public String getSatelliteImageApprovalNumber() {
        String a2 = fh.a(this.f, "approval_number", "si", "");
        return !TextUtils.isEmpty(a2) ? a2 : "GS（2018）984号";
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public float getScalePerPixel() throws RemoteException {
        try {
            return ((float) ((((Math.cos((getCameraPosition().target.latitude * 3.141592653589793d) / 180.0d) * 2.0d) * 3.141592653589793d) * 6378137.0d) / (Math.pow(2.0d, g()) * 256.0d))) * t();
        } catch (Throwable th) {
            ic.c(th, "AMapDelegateImp", "getScalePerPixel");
            th.printStackTrace();
            return 0.0f;
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public float getSkyHeight() {
        return this.c.getSkyHeight();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public View getView() throws RemoteException {
        return this.R;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public float[] getViewMatrix() {
        return this.c != null ? this.c.getViewMatrix() : this.o;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public float getZoomToSpanLevel(LatLng latLng, LatLng latLng2) {
        MapConfig mapConfig = getMapConfig();
        if (latLng == null || latLng2 == null || !this.aO || this.W) {
            return mapConfig.getSZ();
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(latLng);
        builder.include(latLng2);
        GLMapState gLMapState = new GLMapState(1, this.g.getNativeInstance());
        Pair<Float, IPoint> a2 = fr.a(mapConfig, 0, 0, 0, 0, builder.build(), getMapWidth(), getMapHeight());
        gLMapState.recycle();
        return a2 != null ? ((Float) a2.first).floatValue() : gLMapState.getMapZoomer();
    }

    @Override // com.amap.api.mapcore.util.ad
    public float h(int i) {
        if (!this.aO || this.Z || this.g == null) {
            return 0.0f;
        }
        return this.g.getMapState(1).getGLUnitWithWin(i);
    }

    @Override // com.amap.api.mapcore.util.ad
    public ag h() {
        return this.O;
    }

    @Override // com.amap.api.mapcore.util.ad
    public void h(boolean z) {
        if (this.W) {
            return;
        }
        this.R.f(Boolean.valueOf(z));
    }

    @Override // com.amap.api.mapcore.util.ad
    public void i() {
        if (this.K != null) {
            this.K.e();
        }
    }

    @Override // com.amap.api.mapcore.util.ad
    public void i(int i) {
        if (this.R != null) {
            this.R.b(Integer.valueOf(i));
        }
    }

    @Override // com.amap.api.mapcore.util.ad
    public void i(boolean z) {
        if (this.c != null) {
            this.c.setHideLogoEnble(z);
            if (this.c.isCustomStyleEnable()) {
                this.O.setLogoEnable(!z);
            }
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public boolean isIndoorEnabled() throws RemoteException {
        return this.c.isIndoorEnable();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public boolean isMaploaded() {
        return this.ab;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public boolean isMyLocationEnabled() throws RemoteException {
        return this.U;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public boolean isTrafficEnabled() throws RemoteException {
        return this.c.isTrafficEnabled();
    }

    @Override // com.amap.api.mapcore.util.ad
    public void j() {
        if (this.aO) {
            this.j.sendEmptyMessage(18);
        }
    }

    @Override // com.amap.api.mapcore.util.ad
    public void j(int i) {
        if (this.R != null) {
            this.R.c(Integer.valueOf(i));
        }
    }

    @Override // com.amap.api.mapcore.util.ad
    public void k(int i) {
        if (this.R != null) {
            this.R.d(Integer.valueOf(i));
        }
    }

    @Override // com.amap.api.mapcore.util.ad
    public boolean k() {
        return this.aa;
    }

    @Override // com.amap.api.mapcore.util.ad
    public float l(int i) {
        if (this.R != null) {
            return this.R.a(i);
        }
        return 0.0f;
    }

    @Override // com.amap.api.mapcore.util.ad
    public Point l() {
        return this.R != null ? this.R.c() : new Point();
    }

    @Override // com.amap.api.mapcore.util.ad
    public View m() {
        if (this.Q instanceof View) {
            return (View) this.Q;
        }
        return null;
    }

    public void m(final int i) {
        queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.l.12
            @Override // java.lang.Runnable
            public void run() {
                if (!l.this.aO || l.this.g == null) {
                    return;
                }
                l.this.g.setHighlightSubwayEnable(i, false);
            }
        });
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void moveCamera(CameraUpdate cameraUpdate) throws RemoteException {
        if (cameraUpdate == null) {
            return;
        }
        a(cameraUpdate.getCameraUpdateFactoryDelegate());
    }

    @Override // com.amap.api.mapcore.util.ad
    public float n(int i) {
        if (this.c != null) {
            return this.c.getSR();
        }
        return 0.0f;
    }

    @Override // com.amap.api.mapcore.util.ad
    public boolean n() {
        if (g() < 17 || this.d == null || this.d.g == null) {
            return false;
        }
        FPoint obtain = FPoint.obtain();
        b(this.d.g.x, this.d.g.y, obtain);
        return this.an.contains((int) obtain.x, (int) obtain.y);
    }

    @Override // com.amap.api.mapcore.util.ad
    public float o(int i) {
        if (this.c != null) {
            return this.c.getSC();
        }
        return 0.0f;
    }

    @Override // com.amap.api.mapcore.util.ad
    public int o() {
        return this.ay;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void onActivityPause() {
        this.Z = true;
        p(this.V);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void onActivityResume() {
        this.Z = false;
        int i = this.V;
        if (i == 0) {
            i = this.g.getEngineIDWithType(0);
        }
        q(i);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void onChangeFinish() {
        Message obtainMessage = this.j.obtainMessage();
        obtainMessage.what = 11;
        this.j.sendMessage(obtainMessage);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void onFling() {
        if (this.S != null && !MapsInitializer.isTileOverlayClosed()) {
            this.S.b(true);
        }
        this.ak = true;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void onIndoorBuildingActivity(int i, byte[] bArr) {
        bf bfVar;
        if (bArr != null) {
            try {
                bfVar = new bf();
                byte b2 = bArr[0];
                bfVar.a = new String(bArr, 1, b2, "utf-8");
                int i2 = 1 + b2;
                int i3 = i2 + 1;
                byte b3 = bArr[i2];
                bfVar.b = new String(bArr, i3, b3, "utf-8");
                int i4 = i3 + b3;
                int i5 = i4 + 1;
                byte b4 = bArr[i4];
                bfVar.activeFloorName = new String(bArr, i5, b4, "utf-8");
                int i6 = i5 + b4;
                bfVar.activeFloorIndex = GLConvertUtil.getInt(bArr, i6);
                int i7 = i6 + 4;
                int i8 = i7 + 1;
                byte b5 = bArr[i7];
                bfVar.poiid = new String(bArr, i8, b5, "utf-8");
                int i9 = i8 + b5;
                int i10 = i9 + 1;
                byte b6 = bArr[i9];
                bfVar.h = new String(bArr, i10, b6, "utf-8");
                int i11 = i10 + b6;
                bfVar.c = GLConvertUtil.getInt(bArr, i11);
                int i12 = i11 + 4;
                bfVar.floor_indexs = new int[bfVar.c];
                bfVar.floor_names = new String[bfVar.c];
                bfVar.d = new String[bfVar.c];
                for (int i13 = 0; i13 < bfVar.c; i13++) {
                    bfVar.floor_indexs[i13] = GLConvertUtil.getInt(bArr, i12);
                    int i14 = i12 + 4;
                    int i15 = i14 + 1;
                    byte b7 = bArr[i14];
                    if (b7 > 0) {
                        bfVar.floor_names[i13] = new String(bArr, i15, b7, "utf-8");
                        i15 += b7;
                    }
                    i12 = i15 + 1;
                    byte b8 = bArr[i15];
                    if (b8 > 0) {
                        bfVar.d[i13] = new String(bArr, i12, b8, "utf-8");
                        i12 += b8;
                    }
                }
                bfVar.e = GLConvertUtil.getInt(bArr, i12);
                int i16 = i12 + 4;
                if (bfVar.e > 0) {
                    bfVar.f = new int[bfVar.e];
                    for (int i17 = 0; i17 < bfVar.e; i17++) {
                        bfVar.f[i17] = GLConvertUtil.getInt(bArr, i16);
                        i16 += 4;
                    }
                }
            } catch (Throwable th) {
                th.printStackTrace();
                return;
            }
        } else {
            bfVar = null;
        }
        this.bp = bfVar;
        a(new Runnable() { // from class: com.amap.api.mapcore.util.l.28
            @Override // java.lang.Runnable
            public void run() {
                if (l.this.aW != null) {
                    l.this.aW.a(l.this.bp);
                }
            }
        });
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.Z || !this.aO || !this.aK) {
            return false;
        }
        this.bn.mGestureState = 3;
        this.bn.mGestureType = 8;
        this.bn.mLocation = new float[]{motionEvent.getX(), motionEvent.getY()};
        int a2 = a(this.bn);
        r();
        switch (motionEvent.getAction() & 255) {
            case 0:
                s();
                w(a2);
                break;
            case 1:
                x(a2);
                break;
        }
        if (motionEvent.getAction() == 2 && this.ae) {
            try {
                a(motionEvent);
            } catch (Throwable th) {
                ic.c(th, "AMapDelegateImp", "onDragMarker");
                th.printStackTrace();
            }
            return true;
        }
        if (this.aG) {
            try {
                this.aF.a(motionEvent);
            } catch (Throwable th2) {
                th2.printStackTrace();
            }
        }
        if (this.A != null) {
            this.j.removeMessages(14);
            Message obtainMessage = this.j.obtainMessage();
            obtainMessage.what = 14;
            obtainMessage.obj = MotionEvent.obtain(motionEvent);
            obtainMessage.sendToTarget();
        }
        return true;
    }

    public void p() {
        boolean z = false;
        if (this.c.getMapRect() == null || this.au) {
            C();
            this.au = false;
        }
        this.g.getCurTileIDs(1, this.c.getCurTileIds());
        GLMapState mapState = this.g.getMapState(1);
        if (mapState != null) {
            mapState.getViewMatrix(this.c.getViewMatrix());
            mapState.getProjectionMatrix(this.c.getProjectionMatrix());
            this.c.updateFinalMatrix();
            Point mapGeoCenter = mapState.getMapGeoCenter();
            this.c.setSX(mapGeoCenter.x);
            this.c.setSY(mapGeoCenter.y);
            this.c.setSZ(mapState.getMapZoomer());
            this.c.setSC(mapState.getCameraDegree());
            this.c.setSR(mapState.getMapAngle());
            if (!this.c.isMapStateChange()) {
                if (!this.aL && this.g.getAnimateionsCount() == 0 && this.g.getStateMessageCount() == 0) {
                    onChangeFinish();
                    return;
                }
                return;
            }
            this.c.setSkyHeight(mapState.getSkyHeight());
            DPoint pixelsToLatLong = VirtualEarthProjection.pixelsToLatLong(mapGeoCenter.x, mapGeoCenter.y, 20);
            CameraPosition cameraPosition = new CameraPosition(new LatLng(pixelsToLatLong.y, pixelsToLatLong.x, false), this.c.getSZ(), this.c.getSC(), this.c.getSR());
            pixelsToLatLong.recycle();
            Message obtainMessage = this.j.obtainMessage();
            obtainMessage.what = 10;
            obtainMessage.obj = cameraPosition;
            this.j.sendMessage(obtainMessage);
            this.aM = true;
            j();
            C();
            try {
                if (this.O.isZoomControlsEnabled() && this.c.isNeedUpdateZoomControllerState() && this.Y != null) {
                    this.Y.invalidateZoomController(this.c.getSZ());
                }
                if (this.c.getChangeGridRatio() != 1.0d) {
                    g(true);
                }
                if (this.O.isCompassEnabled() && (this.c.isTiltChanged() || this.c.isBearingChanged())) {
                    z = true;
                }
                if (z && this.Y != null) {
                    this.Y.invalidateCompassView();
                }
                if (!this.O.isScaleControlsEnabled() || this.Y == null) {
                    return;
                }
                this.Y.invalidateScaleView();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public void p(int i) {
        if (this.aE != null) {
            this.aE.renderPause();
        }
        s(i);
    }

    @Override // com.amap.api.mapcore.util.ad
    public void q() {
        if (this.aE != null) {
            this.aE.resetTickCount(30);
        }
    }

    public void q(int i) {
        s(i);
        if (this.aE != null) {
            this.aE.renderResume();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void queueEvent(Runnable runnable) {
        try {
            if (this.g != null) {
                this.Q.queueEvent(runnable);
            }
        } catch (Throwable th) {
            ic.c(th, "AMapdelegateImp", "queueEvent");
        }
    }

    public void r() {
        if (this.aE != null) {
            this.aE.resetTickCount(2);
        }
    }

    protected void r(int i) {
        if (this.R != null) {
            if (i == 0) {
                if (this.R.d()) {
                    this.R.g(false);
                    this.R.e();
                    return;
                }
                return;
            }
            if (this.R.d()) {
                return;
            }
            this.R.g(true);
            this.R.e();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void reloadMap() {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void removeEngineGLOverlay(BaseMapOverlay baseMapOverlay) {
        if (this.g != null) {
            this.g.getOverlayBundle(1).removeOverlay(baseMapOverlay);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void removecache() throws RemoteException {
        removecache(null);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void removecache(AMap.OnCacheRemoveListener onCacheRemoveListener) throws RemoteException {
        if (this.j == null || this.g == null) {
            return;
        }
        try {
            d dVar = new d(this.f, onCacheRemoveListener);
            this.j.removeCallbacks(dVar);
            this.j.post(dVar);
        } catch (Throwable th) {
            ic.c(th, "AMapDelegateImp", "removecache");
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void renderSurface(GL10 gl10) {
        drawFrame(gl10);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void requestRender() {
        if (this.aE == null || this.aE.isRenderPause()) {
            return;
        }
        this.Q.requestRender();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void resetMinMaxZoomPreference() {
        this.c.resetMinMaxZoomPreference();
        try {
            if (this.O.isZoomControlsEnabled() && this.c.isNeedUpdateZoomControllerState() && this.Y != null) {
                this.Y.invalidateZoomController(this.c.getSZ());
            }
        } catch (RemoteException unused) {
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void resetRenderTime() {
        if (this.aE != null) {
            this.aE.resetTickCount(2);
        }
    }

    public void s() {
        if (!this.aO || this.aE == null || this.aE.isRenderPause()) {
            return;
        }
        requestRender();
    }

    public void s(final int i) {
        queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.l.14
            @Override // java.lang.Runnable
            public void run() {
                try {
                    l.this.g.clearAllMessages(i);
                    l.this.g.clearAnimations(i, true);
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        });
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void set3DBuildingEnabled(boolean z) throws RemoteException {
        p(1);
        a(1, z);
        q(1);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setAMapGestureListener(AMapGestureListener aMapGestureListener) {
        if (this.aF != null) {
            this.J = aMapGestureListener;
            this.aF.a(aMapGestureListener);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setCenterToPixel(int i, int i2) throws RemoteException {
        this.aa = true;
        this.aU = i;
        this.aV = i2;
        if (this.aP && this.aO) {
            if (this.c.getAnchorX() == this.aU && this.c.getAnchorY() == this.aV) {
                return;
            }
            this.c.setAnchorX(this.aU);
            this.c.setAnchorY(this.aV);
            queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.l.25
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        l.this.c.setAnchorX(Math.max(0, Math.min(l.this.aU, l.this.h)));
                        l.this.c.setAnchorY(Math.max(0, Math.min(l.this.aV, l.this.i)));
                        l.this.g.setProjectionCenter(1, l.this.c.getAnchorX(), l.this.c.getAnchorY());
                        l.this.au = true;
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            });
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setCustomMapStyle(CustomMapStyleOptions customMapStyleOptions) {
        if (customMapStyleOptions != null) {
            if (customMapStyleOptions.isEnable()) {
                F();
            }
            this.ba.c();
            this.ba.a(customMapStyleOptions);
        }
        resetRenderTime();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setCustomMapStyleID(String str) {
        if (TextUtils.isEmpty(str) || str.equals(this.c.getCustomStyleID())) {
            return;
        }
        this.c.setCustomStyleID(str);
        this.P = true;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setCustomMapStylePath(String str) {
        if (TextUtils.isEmpty(str) || str.equals(this.c.getCustomStylePath())) {
            return;
        }
        this.c.setCustomStylePath(str);
        this.P = true;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setCustomRenderer(CustomRenderer customRenderer) throws RemoteException {
        this.aw = customRenderer;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setCustomTextureResourcePath(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.c.setCustomTextureResourcePath(str);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setIndoorBuildingInfo(IndoorBuildingInfo indoorBuildingInfo) throws RemoteException {
        if (this.W || indoorBuildingInfo == null || indoorBuildingInfo.activeFloorName == null || indoorBuildingInfo.poiid == null) {
            return;
        }
        this.d = (bf) indoorBuildingInfo;
        resetRenderTime();
        queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.l.26
            @Override // java.lang.Runnable
            public void run() {
                if (l.this.g != null) {
                    l.this.g.setIndoorBuildingToBeActive(1, l.this.d.activeFloorName, l.this.d.activeFloorIndex, l.this.d.poiid);
                }
            }
        });
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setIndoorEnabled(final boolean z) throws RemoteException {
        if (!this.aO || this.W) {
            this.bj.c = z;
            this.bj.b = true;
            this.bj.g = 1;
            return;
        }
        this.c.setIndoorEnable(z);
        resetRenderTime();
        if (!z) {
            if (this.g != null) {
                this.g.setIndoorEnable(1, false);
            }
            this.c.maxZoomLevel = this.c.isSetLimitZoomLevel() ? this.c.getMaxZoomLevel() : 20.0f;
            if (this.O.isZoomControlsEnabled() && this.Y != null) {
                this.Y.invalidateZoomController(this.c.getSZ());
            }
        } else if (this.g != null) {
            this.g.setIndoorEnable(1, true);
        }
        if (this.O.isIndoorSwitchEnabled()) {
            this.j.post(new Runnable() { // from class: com.amap.api.mapcore.util.l.19
                @Override // java.lang.Runnable
                public void run() {
                    if (z) {
                        l.this.b(true);
                    } else if (l.this.R != null) {
                        l.this.R.i(false);
                    }
                }
            });
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setInfoWindowAdapter(AMap.CommonInfoWindowAdapter commonInfoWindowAdapter) throws RemoteException {
        if (this.W || this.K == null) {
            return;
        }
        this.K.a(commonInfoWindowAdapter);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setInfoWindowAdapter(AMap.InfoWindowAdapter infoWindowAdapter) throws RemoteException {
        if (this.W || this.K == null) {
            return;
        }
        this.K.a(infoWindowAdapter);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setLoadOfflineData(final boolean z) throws RemoteException {
        queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.l.20
            @Override // java.lang.Runnable
            public void run() {
                if (l.this.g != null) {
                    l.this.g.setOfflineDataEnable(1, z);
                }
            }
        });
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setLocationSource(LocationSource locationSource) throws RemoteException {
        try {
            if (this.W) {
                return;
            }
            if (this.ad != null && (this.ad instanceof bi)) {
                this.ad.deactivate();
            }
            this.ad = locationSource;
            if (locationSource != null) {
                this.R.h(true);
            } else {
                this.R.h(false);
            }
        } catch (Throwable th) {
            ic.c(th, "AMapDelegateImp", "setLocationSource");
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setMapCustomEnable(boolean z) {
        if (z) {
            F();
        }
        a(z, false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setMapLanguage(String str) {
        if (TextUtils.isEmpty(str) || this.c == null || this.c.isCustomStyleEnable() || this.c.getMapLanguage().equals(str)) {
            return;
        }
        if (!str.equals(AMap.ENGLISH)) {
            this.c.setMapLanguage("zh_cn");
            this.av = 0;
        } else {
            if (this.ao != 1) {
                try {
                    setMapType(1);
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
            this.c.setMapLanguage(AMap.ENGLISH);
            this.av = IDataParser.CODE_NET_UNAVAILABLE;
        }
        try {
            a(getCameraPosition());
        } catch (Throwable th2) {
            th2.printStackTrace();
        }
        if (this.S == null || MapsInitializer.isTileOverlayClosed()) {
            return;
        }
        this.S.a(this.c.getMapLanguage());
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setMapStatusLimits(LatLngBounds latLngBounds) {
        try {
            this.c.setLimitLatLngBounds(latLngBounds);
            G();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setMapTextEnable(final boolean z) throws RemoteException {
        if (this.aO && this.aP) {
            resetRenderTime();
            queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.l.22
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        l.this.g.setLabelEnable(1, z);
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            });
        } else {
            this.bg.c = z;
            this.bg.b = true;
            this.bg.g = 1;
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setMapTextZIndex(int i) throws RemoteException {
        this.av = i;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setMapType(int i) throws RemoteException {
        if (i != this.ao || (this.c != null && this.c.isCustomStyleEnable())) {
            if (this.aD != null) {
                this.aD.a(new al(1, Integer.valueOf(i)));
            }
            this.ao = i;
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setMaskLayerParams(int i, int i2, int i3, int i4, final int i5, long j) {
        GLAlphaAnimation gLAlphaAnimation;
        try {
            if (this.ax != null) {
                float f = i4 / 255.0f;
                if (i5 == -1) {
                    gLAlphaAnimation = new GLAlphaAnimation(f, 0.0f);
                    gLAlphaAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.amap.api.mapcore.util.l.27
                        @Override // com.amap.api.maps.model.animation.Animation.AnimationListener
                        public void onAnimationEnd() {
                            l.this.j.post(new Runnable() { // from class: com.amap.api.mapcore.util.l.27.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    l.this.ay = i5;
                                    if (l.this.R != null) {
                                        l.this.R.j(true);
                                    }
                                }
                            });
                        }

                        @Override // com.amap.api.maps.model.animation.Animation.AnimationListener
                        public void onAnimationStart() {
                        }
                    });
                } else {
                    this.ay = i5;
                    gLAlphaAnimation = new GLAlphaAnimation(0.0f, f);
                    if (f > 0.2f) {
                        if (this.R != null) {
                            this.R.j(false);
                        }
                    } else if (this.R != null) {
                        this.R.j(true);
                    }
                }
                gLAlphaAnimation.setInterpolator(new LinearInterpolator());
                gLAlphaAnimation.setDuration(j);
                this.ax.a(i, i2, i3, i4);
                this.ax.a(gLAlphaAnimation);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setMaxZoomLevel(float f) {
        this.c.setMaxZoomLevel(f);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setMinZoomLevel(float f) {
        this.c.setMinZoomLevel(f);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setMyLocationEnabled(boolean z) throws RemoteException {
        if (this.W) {
            return;
        }
        try {
            if (this.R != null) {
                this.R.h();
                if (this.ad == null) {
                    this.R.h(false);
                } else if (z) {
                    this.ad.activate(this.G);
                    this.R.h(true);
                    if (this.ac == null) {
                        this.ac = new dw(this, this.f);
                    }
                } else {
                    if (this.ac != null) {
                        this.ac.b();
                        this.ac = null;
                    }
                    this.ad.deactivate();
                }
            }
            if (!z) {
                this.O.setMyLocationButtonEnabled(z);
            }
            this.U = z;
            resetRenderTime();
        } catch (Throwable th) {
            ic.c(th, "AMapDelegateImp", "setMyLocationEnabled");
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setMyLocationRotateAngle(float f) throws RemoteException {
        if (this.ac != null) {
            this.ac.a(f);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setMyLocationStyle(MyLocationStyle myLocationStyle) throws RemoteException {
        if (this.W) {
            return;
        }
        if (this.ac == null) {
            this.ac = new dw(this, this.f);
        }
        if (this.ac != null) {
            long j = 1000;
            if (myLocationStyle.getInterval() < j) {
                myLocationStyle.interval(j);
            }
            if (this.ad != null && (this.ad instanceof bi)) {
                ((bi) this.ad).a(myLocationStyle.getInterval());
                ((bi) this.ad).a(myLocationStyle.getMyLocationType());
            }
            this.ac.a(myLocationStyle);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setMyLocationType(int i) throws RemoteException {
        if (this.ac == null || this.ac.a() == null) {
            return;
        }
        this.ac.a().myLocationType(i);
        setMyLocationStyle(this.ac.a());
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setMyTrafficStyle(MyTrafficStyle myTrafficStyle) throws RemoteException {
        if (this.W) {
            return;
        }
        this.ap = myTrafficStyle;
        if (this.aO && this.aP && myTrafficStyle != null) {
            resetRenderTime();
            queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.l.24
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        l.this.g.setTrafficStyle(1, l.this.ap.getSmoothColor(), l.this.ap.getSlowColor(), l.this.ap.getCongestedColor(), l.this.ap.getSeriousCongestedColor());
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            });
        } else {
            this.bm.c = false;
            this.bm.b = true;
            this.bm.g = 1;
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setOnCameraChangeListener(AMap.OnCameraChangeListener onCameraChangeListener) throws RemoteException {
        this.y = onCameraChangeListener;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setOnIndoorBuildingActiveListener(AMap.OnIndoorBuildingActiveListener onIndoorBuildingActiveListener) throws RemoteException {
        this.E = onIndoorBuildingActiveListener;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setOnInfoWindowClickListener(AMap.OnInfoWindowClickListener onInfoWindowClickListener) throws RemoteException {
        this.D = onInfoWindowClickListener;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setOnMapClickListener(AMap.OnMapClickListener onMapClickListener) throws RemoteException {
        this.z = onMapClickListener;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setOnMapLongClickListener(AMap.OnMapLongClickListener onMapLongClickListener) throws RemoteException {
        this.C = onMapLongClickListener;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setOnMapTouchListener(AMap.OnMapTouchListener onMapTouchListener) throws RemoteException {
        this.A = onMapTouchListener;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setOnMaploadedListener(AMap.OnMapLoadedListener onMapLoadedListener) throws RemoteException {
        this.x = onMapLoadedListener;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setOnMarkerClickListener(AMap.OnMarkerClickListener onMarkerClickListener) throws RemoteException {
        this.u = onMarkerClickListener;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setOnMarkerDragListener(AMap.OnMarkerDragListener onMarkerDragListener) throws RemoteException {
        this.w = onMarkerDragListener;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setOnMultiPointClickListener(AMap.OnMultiPointClickListener onMultiPointClickListener) {
        if (this.aZ != null) {
            this.aZ.a(onMultiPointClickListener);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setOnMyLocationChangeListener(AMap.OnMyLocationChangeListener onMyLocationChangeListener) {
        this.F = onMyLocationChangeListener;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setOnPOIClickListener(AMap.OnPOIClickListener onPOIClickListener) throws RemoteException {
        this.B = onPOIClickListener;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setOnPolylineClickListener(AMap.OnPolylineClickListener onPolylineClickListener) throws RemoteException {
        this.v = onPolylineClickListener;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setRenderFps(int i) {
        try {
            this.az = Math.max(10, Math.min(i, 40));
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setRenderMode(int i) {
        if (this.Q != null) {
            this.Q.setRenderMode(i);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setRoadArrowEnable(final boolean z) throws RemoteException {
        if (this.aO && this.aP) {
            resetRenderTime();
            queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.l.23
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        l.this.g.setRoadArrowEnable(1, z);
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            });
        } else {
            this.bh.c = z;
            this.bh.b = true;
            this.bh.g = 1;
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setRunLowFrame(boolean z) {
        if (z) {
            return;
        }
        s();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setTrafficEnabled(final boolean z) throws RemoteException {
        if (this.aO && !this.W) {
            queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.l.18
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        if (l.this.c.isTrafficEnabled() != z) {
                            l.this.c.setTrafficEnabled(z);
                            l.this.aE.setTrafficMode(z);
                            boolean z2 = z;
                            l.this.g.setTrafficEnable(1, z);
                            l.this.resetRenderTime();
                        }
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            });
            return;
        }
        this.bb.c = z;
        this.bb.b = true;
        this.bb.g = 1;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setVisibilityEx(int i) {
        if (this.Q != null) {
            try {
                this.Q.setVisibility(i);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setZOrderOnTop(boolean z) throws RemoteException {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void setZoomScaleParam(float f) {
        this.aI = f;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAMap
    public void stopAnimation() throws RemoteException {
        if (this.g != null) {
            this.g.interruptAnimation();
        }
        resetRenderTime();
    }

    @Override // com.amap.api.mapcore.util.ad
    public float t() {
        return this.aI;
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0062 A[Catch: Throwable -> 0x007c, TryCatch #0 {Throwable -> 0x007c, blocks: (B:5:0x0026, B:7:0x003d, B:9:0x0041, B:11:0x0049, B:12:0x005c, B:13:0x0078, B:20:0x004f, B:21:0x0062, B:23:0x0070, B:24:0x0075, B:36:0x0023), top: B:35:0x0023 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x003d A[Catch: Throwable -> 0x007c, TryCatch #0 {Throwable -> 0x007c, blocks: (B:5:0x0026, B:7:0x003d, B:9:0x0041, B:11:0x0049, B:12:0x005c, B:13:0x0078, B:20:0x004f, B:21:0x0062, B:23:0x0070, B:24:0x0075, B:36:0x0023), top: B:35:0x0023 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void t(int r15) {
        /*
            r14 = this;
            r14.ao = r15
            r0 = 5
            r1 = 2
            r2 = 4
            r3 = 1
            r4 = 0
            if (r15 != r3) goto Ld
        L9:
            r8 = 0
        La:
            r9 = 0
            r10 = 0
            goto L26
        Ld:
            if (r15 != r1) goto L11
            r8 = 1
            goto La
        L11:
            r5 = 3
            if (r15 != r5) goto L18
            r8 = 0
            r9 = 1
        L16:
            r10 = 4
            goto L26
        L18:
            if (r15 != r2) goto L1d
            r8 = 0
            r9 = 0
            goto L16
        L1d:
            if (r15 != r0) goto L23
            r8 = 2
            r9 = 0
            r10 = 5
            goto L26
        L23:
            r14.ao = r3     // Catch: java.lang.Throwable -> L7c
            goto L9
        L26:
            com.autonavi.amap.mapcore.MapConfig r15 = r14.c     // Catch: java.lang.Throwable -> L7c
            r15.setMapStyleMode(r8)     // Catch: java.lang.Throwable -> L7c
            com.autonavi.amap.mapcore.MapConfig r15 = r14.c     // Catch: java.lang.Throwable -> L7c
            r15.setMapStyleTime(r9)     // Catch: java.lang.Throwable -> L7c
            com.autonavi.amap.mapcore.MapConfig r15 = r14.c     // Catch: java.lang.Throwable -> L7c
            r15.setMapStyleState(r10)     // Catch: java.lang.Throwable -> L7c
            com.autonavi.amap.mapcore.MapConfig r15 = r14.c     // Catch: java.lang.Throwable -> L7c
            boolean r15 = r15.isCustomStyleEnable()     // Catch: java.lang.Throwable -> L7c
            if (r15 == 0) goto L62
            com.amap.api.mapcore.util.k r15 = r14.ba     // Catch: java.lang.Throwable -> L7c
            if (r15 == 0) goto L4f
            com.amap.api.mapcore.util.k r15 = r14.ba     // Catch: java.lang.Throwable -> L7c
            boolean r15 = r15.d()     // Catch: java.lang.Throwable -> L7c
            if (r15 == 0) goto L4f
            com.amap.api.mapcore.util.k r15 = r14.ba     // Catch: java.lang.Throwable -> L7c
            r15.e()     // Catch: java.lang.Throwable -> L7c
            goto L5c
        L4f:
            r7 = 1
            r11 = 1
            r12 = 0
            r13 = 0
            r6 = r14
            r6.a(r7, r8, r9, r10, r11, r12, r13)     // Catch: java.lang.Throwable -> L7c
            com.autonavi.amap.mapcore.MapConfig r15 = r14.c     // Catch: java.lang.Throwable -> L7c
            r15.setCustomStyleEnable(r4)     // Catch: java.lang.Throwable -> L7c
        L5c:
            com.amap.api.mapcore.util.ar r15 = r14.O     // Catch: java.lang.Throwable -> L7c
            r15.setLogoEnable(r3)     // Catch: java.lang.Throwable -> L7c
            goto L78
        L62:
            com.autonavi.amap.mapcore.MapConfig r15 = r14.c     // Catch: java.lang.Throwable -> L7c
            java.lang.String r15 = r15.getMapLanguage()     // Catch: java.lang.Throwable -> L7c
            java.lang.String r0 = "en"
            boolean r15 = r15.equals(r0)     // Catch: java.lang.Throwable -> L7c
            if (r15 == 0) goto L75
            java.lang.String r15 = "zh_cn"
            r14.setMapLanguage(r15)     // Catch: java.lang.Throwable -> L7c
        L75:
            r14.b(r3, r8, r9, r10)     // Catch: java.lang.Throwable -> L7c
        L78:
            r14.resetRenderTime()     // Catch: java.lang.Throwable -> L7c
            goto L87
        L7c:
            r15 = move-exception
            java.lang.String r0 = "AMapDelegateImp"
            java.lang.String r1 = "setMaptype"
            com.amap.api.mapcore.util.ic.c(r15, r0, r1)
            r15.printStackTrace()
        L87:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.l.t(int):void");
    }

    @Override // com.amap.api.mapcore.util.ad
    public ee u(int i) {
        if (this.aX == null) {
            return null;
        }
        return this.aX.a(i);
    }

    protected void u() {
        AMapNativeRenderer.nativeDrawLineInit();
    }

    @Override // com.amap.api.mapcore.util.ad
    public float v(int i) {
        GLMapState gLMapState = new GLMapState(1, this.g.getNativeInstance());
        gLMapState.setMapZoomer(i);
        gLMapState.recalculate();
        float gLUnitWithWin = gLMapState.getGLUnitWithWin(1);
        gLMapState.recycle();
        return gLUnitWithWin;
    }

    @Override // com.amap.api.mapcore.util.ad
    public Context v() {
        return this.f;
    }

    @Override // com.amap.api.mapcore.util.ad
    public void w() {
        if (this.ba != null) {
            this.ba.b();
        }
    }

    @Override // com.amap.api.mapcore.util.ad
    public float[] x() {
        return this.c != null ? this.c.getMvpMatrix() : this.n;
    }

    @Override // com.amap.api.mapcore.util.ad
    public ef y() {
        return this.aX;
    }

    @Override // com.amap.api.mapcore.util.ad
    public void z() {
        if (this.R != null) {
            this.R.e();
        }
    }
}
