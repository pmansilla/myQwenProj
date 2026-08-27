package com.autonavi.ae.gmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.text.TextUtils;
import com.amap.api.mapcore.util.ad;
import com.amap.api.mapcore.util.fq;
import com.amap.api.mapcore.util.fr;
import com.amap.api.mapcore.util.ic;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.autonavi.ae.gmap.gesture.EAMapPlatformGestureInfo;
import com.autonavi.ae.gmap.glanimation.AdglMapAnimFling;
import com.autonavi.ae.gmap.glanimation.AdglMapAnimGroup;
import com.autonavi.ae.gmap.glanimation.AdglMapAnimationMgr;
import com.autonavi.ae.gmap.gloverlay.BaseMapOverlay;
import com.autonavi.ae.gmap.gloverlay.GLOverlayBundle;
import com.autonavi.ae.gmap.gloverlay.GLTextureProperty;
import com.autonavi.ae.gmap.style.StyleItem;
import com.autonavi.amap.mapcore.AMapEngineUtils;
import com.autonavi.amap.mapcore.AbstractCameraUpdateMessage;
import com.autonavi.amap.mapcore.FileUtil;
import com.autonavi.amap.mapcore.IAMapEngineCallback;
import com.autonavi.amap.mapcore.interfaces.IAMapListener;
import com.autonavi.amap.mapcore.maploader.AMapLoader;
import com.autonavi.amap.mapcore.maploader.NetworkState;
import com.autonavi.amap.mapcore.message.AbstractGestureMapMessage;
import com.autonavi.amap.mapcore.message.HoverGestureMapMessage;
import com.autonavi.amap.mapcore.message.MoveGestureMapMessage;
import com.autonavi.amap.mapcore.message.RotateGestureMapMessage;
import com.autonavi.amap.mapcore.message.ScaleGestureMapMessage;
import com.autonavi.amap.mapcore.tools.GLConvertUtil;
import com.autonavi.amap.mapcore.tools.GlMapUtil;
import com.autonavi.amap.mapcore.tools.TextTextureGenerator;
import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: classes.dex */
public class GLMapEngine implements IAMapEngineCallback, NetworkState.NetworkChangeListener {
    private Context context;
    private ad mGlMapView;
    private IAMapListener mMapListener;
    boolean mRequestDestroy;
    private TextTextureGenerator mTextTextureGenerator;
    private AdglMapAnimationMgr mapAnimationMgr;
    GLMapState state;
    private String userAgent;
    private long mNativeMapengineInstance = 0;
    private List<AbstractCameraUpdateMessage> mStateMessageList = new Vector();
    private List<AbstractGestureMapMessage> mGestureMessageList = new Vector();
    private List<AbstractGestureMapMessage> mGestureEndMessageList = new Vector();
    private List<AbstractCameraUpdateMessage> mAnimateStateMessageList = new Vector();
    boolean isMoveCameraStep = false;
    boolean isGestureStep = false;
    private int mapGestureCount = 0;
    private GLMapState copyGLMapState = null;
    private Lock mLock = new ReentrantLock();
    private Object mutLock = new Object();
    private NetworkState mNetworkState = null;
    GLOverlayBundle<BaseMapOverlay<?, ?>> bundle = null;
    private boolean isEngineRenderComplete = false;
    Hashtable<Long, AMapLoader> aMapLoaderHashtable = new Hashtable<>();
    private AtomicInteger mRequestID = new AtomicInteger(1);

    /* loaded from: classes.dex */
    public static class InitParam {
        public String mConfigContent;
        public String mConfigPath;
        public String mOfflineDataPath;
        public String mP3dCrossPath;
        public String mRootPath;
    }

    /* loaded from: classes.dex */
    public static class MapViewInitParam {
        public int engineId;
        public int height;
        public float mapZoomScale;
        public int screenHeight;
        public float screenScale;
        public int screenWidth;
        public float textScale;
        public int width;
        public int x;
        public int y;
    }

    public GLMapEngine(Context context, ad adVar) {
        this.mGlMapView = null;
        this.mapAnimationMgr = null;
        this.mRequestDestroy = false;
        this.mRequestDestroy = false;
        if (context == null) {
            return;
        }
        this.context = context.getApplicationContext();
        this.mGlMapView = adVar;
        this.mTextTextureGenerator = new TextTextureGenerator();
        this.mapAnimationMgr = new AdglMapAnimationMgr();
        this.mapAnimationMgr.setMapAnimationListener(new AdglMapAnimationMgr.MapAnimationListener() { // from class: com.autonavi.ae.gmap.GLMapEngine.5
            @Override // com.autonavi.ae.gmap.glanimation.AdglMapAnimationMgr.MapAnimationListener
            public void onMapAnimationFinish(AMap.CancelableCallback cancelableCallback) {
                GLMapEngine.this.doMapAnimationFinishCallback(cancelableCallback);
            }
        });
        this.userAgent = System.getProperty("http.agent") + " amap/" + GlMapUtil.getAppVersionName(context);
    }

    public static void destroyOverlay(int i, long j) {
        synchronized (GLMapEngine.class) {
            nativeDestroyOverlay(i, j);
        }
    }

    private void doMapAnimationCancelCallback(final AMap.CancelableCallback cancelableCallback) {
        if (cancelableCallback == null || this.mGlMapView == null) {
            return;
        }
        this.mGlMapView.getMainHandler().post(new Runnable() { // from class: com.autonavi.ae.gmap.GLMapEngine.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    cancelableCallback.onCancel();
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doMapAnimationFinishCallback(final AMap.CancelableCallback cancelableCallback) {
        if (this.mMapListener != null) {
            this.mMapListener.afterAnimation();
        }
        if (cancelableCallback == null || this.mGlMapView == null) {
            return;
        }
        this.mGlMapView.getMainHandler().post(new Runnable() { // from class: com.autonavi.ae.gmap.GLMapEngine.3
            @Override // java.lang.Runnable
            public void run() {
                try {
                    cancelableCallback.onFinish();
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        });
    }

    private void gestureBegin() {
        this.mapGestureCount++;
    }

    private void gestureEnd() {
        this.mapGestureCount--;
        if (this.mapGestureCount == 0) {
            recycleMessage();
        }
    }

    private void initAnimation() {
        AbstractCameraUpdateMessage remove;
        if (this.mStateMessageList.size() <= 0 && this.mAnimateStateMessageList.size() > 0 && (remove = this.mAnimateStateMessageList.remove(0)) != null) {
            remove.generateMapAnimation(this);
        }
    }

    private void initNetworkState() {
        this.mNetworkState = new NetworkState();
        this.mNetworkState.setNetworkListener(this);
        this.mNetworkState.registerNetChangeReceiver(this.context.getApplicationContext(), true);
        boolean isNetworkConnected = NetworkState.isNetworkConnected(this.context.getApplicationContext());
        if (this.mNativeMapengineInstance != 0) {
            nativeSetNetStatus(this.mNativeMapengineInstance, isNetworkConnected ? 1 : 0);
        }
    }

    protected static native String nativeAddNativeOverlay(int i, long j, int i2, int i3);

    private static native boolean nativeAddOverlayTexture(int i, long j, int i2, int i3, float f, float f2, Bitmap bitmap, boolean z, boolean z2);

    private static native void nativeCreateAMapEngineWithFrame(long j, int i, int i2, int i3, int i4, int i5, int i6, int i7, float f, float f2, float f3);

    private static native long nativeCreateAMapInstance(String str, String str2, String str3);

    protected static native long nativeCreateOverlay(int i, long j, int i2);

    private static native void nativeDestroy(long j);

    private static native void nativeDestroyCurrentState(long j, long j2);

    protected static native void nativeDestroyOverlay(int i, long j);

    private static native void nativeFinishDownLoad(int i, long j, long j2);

    private static native void nativeGetCurTileIDs(int i, long j, int[] iArr, int i2);

    private static native long nativeGetCurrentMapState(int i, long j);

    private static native long nativeGetGlOverlayMgrPtr(int i, long j);

    public static native String nativeGetMapEngineVersion(int i);

    private static native int[] nativeGetMapModeState(int i, long j, boolean z);

    private static native boolean nativeGetSrvViewStateBoolValue(int i, long j, int i2);

    private static native void nativeInitAMapEngineCallback(long j, Object obj);

    private static native void nativeInitParam(String str, String str2, String str3, String str4);

    private static native boolean nativeIsEngineCreated(long j, int i);

    private static native void nativePopRenderState(int i, long j);

    private static native void nativePostRenderAMap(long j, int i);

    private static native void nativePushRendererState(int i, long j);

    private static native void nativeReceiveNetData(int i, long j, byte[] bArr, long j2, int i2);

    protected static native void nativeRemoveNativeAllOverlay(int i, long j);

    protected static native void nativeRemoveNativeOverlay(int i, long j, String str);

    private static native void nativeRenderAMap(long j, int i);

    private static native void nativeSelectMapPois(int i, long j, int i2, int i3, int i4, byte[] bArr);

    private static native void nativeSetAllContentEnable(int i, long j, boolean z);

    private static native void nativeSetBuildingEnable(int i, long j, boolean z);

    private static native void nativeSetBuildingTextureEnable(int i, long j, boolean z);

    private static native void nativeSetCustomStyleData(int i, long j, byte[] bArr, byte[] bArr2);

    private static native void nativeSetCustomStyleTexture(int i, long j, byte[] bArr);

    private static native void nativeSetHighlightSubwayEnable(int i, long j, boolean z);

    private static native void nativeSetIndoorBuildingToBeActive(int i, long j, String str, int i2, String str2);

    private static native void nativeSetIndoorEnable(int i, long j, boolean z);

    private static native void nativeSetLabelEnable(int i, long j, boolean z);

    private static native boolean nativeSetMapModeAndStyle(int i, long j, int[] iArr, boolean z, boolean z2, StyleItem[] styleItemArr);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeSetNetStatus(long j, int i);

    private static native void nativeSetOfflineDataEnable(int i, long j, boolean z);

    private static native void nativeSetParameter(int i, long j, int i2, int i3, int i4, int i5, int i6);

    private static native void nativeSetProjectionCenter(int i, long j, float f, float f2);

    private static native void nativeSetRenderListenerStatus(int i, long j);

    private static native void nativeSetRoadArrowEnable(int i, long j, boolean z);

    private static native void nativeSetServiceViewRect(int i, long j, int i2, int i3, int i4, int i5, int i6, int i7);

    private static native void nativeSetSetBackgroundTexture(int i, long j, byte[] bArr);

    private static native void nativeSetSimple3DEnable(int i, long j, boolean z);

    private static native void nativeSetSkyTexture(int i, long j, byte[] bArr);

    private static native void nativeSetSrvViewStateBoolValue(int i, long j, int i2, boolean z);

    private static native void nativeSetTrafficEnable(int i, long j, boolean z);

    private static native void nativeSetTrafficTexture(int i, long j, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4);

    private static native void nativeSetTrafficTextureAllInOne(int i, long j, byte[] bArr);

    protected static native void nativeUpdateNativeArrowOverlay(int i, long j, String str, int[] iArr, int[] iArr2, int i2, int i3, int i4, float f, boolean z, int i5, int i6, int i7);

    private static native void nativesetMapOpenLayer(int i, long j, byte[] bArr);

    private boolean processAnimations(GLMapState gLMapState) {
        try {
            if (this.mapAnimationMgr.getAnimationsCount() <= 0) {
                return false;
            }
            gLMapState.recalculate();
            this.mapAnimationMgr.doAnimations(gLMapState);
            return true;
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    private boolean processGestureMessage(GLMapState gLMapState) {
        AbstractGestureMapMessage remove;
        if (this.mGestureMessageList.size() <= 0) {
            if (this.isGestureStep) {
                this.isGestureStep = false;
            }
            return false;
        }
        this.isGestureStep = true;
        if (gLMapState == null) {
            return false;
        }
        while (this.mGestureMessageList.size() > 0 && (remove = this.mGestureMessageList.remove(0)) != null) {
            if (remove.width == 0) {
                remove.width = this.mGlMapView.getMapWidth();
            }
            if (remove.height == 0) {
                remove.height = this.mGlMapView.getMapHeight();
            }
            int mapGestureState = remove.getMapGestureState();
            if (mapGestureState == 100) {
                gestureBegin();
            } else if (mapGestureState == 101) {
                remove.runCameraUpdate(gLMapState);
            } else if (mapGestureState == 102) {
                gestureEnd();
            }
            this.mGestureEndMessageList.add(remove);
        }
        if (this.mGestureEndMessageList.size() == 1) {
            recycleMessage();
        }
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x003a A[Catch: Exception -> 0x0041, TryCatch #0 {Exception -> 0x0041, blocks: (B:3:0x0002, B:6:0x0014, B:11:0x002e, B:16:0x003a, B:17:0x003d, B:23:0x001f, B:25:0x0027), top: B:2:0x0002 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean processMessage() {
        /*
            r5 = this;
            r0 = 0
            r1 = 1
            com.autonavi.ae.gmap.GLMapState r2 = r5.getNewMapState(r1)     // Catch: java.lang.Exception -> L41
            boolean r3 = r5.processGestureMessage(r2)     // Catch: java.lang.Exception -> L41
            java.util.List<com.autonavi.amap.mapcore.message.AbstractGestureMapMessage> r4 = r5.mGestureMessageList     // Catch: java.lang.Exception -> L41
            int r4 = r4.size()     // Catch: java.lang.Exception -> L41
            if (r4 > 0) goto L1f
            if (r3 != 0) goto L1d
            boolean r3 = r5.processStateMapMessage(r2)     // Catch: java.lang.Exception -> L41
            if (r3 == 0) goto L1b
            goto L1d
        L1b:
            r3 = 0
            goto L2c
        L1d:
            r3 = 1
            goto L2c
        L1f:
            java.util.List<com.autonavi.amap.mapcore.AbstractCameraUpdateMessage> r4 = r5.mStateMessageList     // Catch: java.lang.Exception -> L41
            int r4 = r4.size()     // Catch: java.lang.Exception -> L41
            if (r4 <= 0) goto L2c
            java.util.List<com.autonavi.amap.mapcore.AbstractCameraUpdateMessage> r4 = r5.mStateMessageList     // Catch: java.lang.Exception -> L41
            r4.clear()     // Catch: java.lang.Exception -> L41
        L2c:
            if (r3 != 0) goto L37
            boolean r3 = r5.processAnimations(r2)     // Catch: java.lang.Exception -> L41
            if (r3 == 0) goto L35
            goto L37
        L35:
            r3 = 0
            goto L38
        L37:
            r3 = 1
        L38:
            if (r3 == 0) goto L3d
            r5.setMapState(r1, r2)     // Catch: java.lang.Exception -> L41
        L3d:
            r2.recycle()     // Catch: java.lang.Exception -> L41
            return r3
        L41:
            r1 = move-exception
            r1.printStackTrace()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.autonavi.ae.gmap.GLMapEngine.processMessage():boolean");
    }

    private boolean processStateMapMessage(GLMapState gLMapState) {
        AbstractCameraUpdateMessage remove;
        if (this.mStateMessageList.size() <= 0) {
            if (this.isMoveCameraStep) {
                this.isMoveCameraStep = false;
            }
            return false;
        }
        this.isMoveCameraStep = true;
        if (gLMapState == null) {
            return false;
        }
        while (this.mStateMessageList.size() > 0 && (remove = this.mStateMessageList.remove(0)) != null) {
            if (remove.width == 0) {
                remove.width = this.mGlMapView.getMapWidth();
            }
            if (remove.height == 0) {
                remove.height = this.mGlMapView.getMapHeight();
            }
            gLMapState.recalculate();
            remove.runCameraUpdate(gLMapState);
        }
        return true;
    }

    private void recycleMessage() {
        AbstractGestureMapMessage remove;
        while (this.mGestureEndMessageList.size() > 0 && (remove = this.mGestureEndMessageList.remove(0)) != null) {
            if (remove instanceof MoveGestureMapMessage) {
                ((MoveGestureMapMessage) remove).recycle();
            } else if (remove instanceof HoverGestureMapMessage) {
                ((HoverGestureMapMessage) remove).recycle();
            } else if (remove instanceof RotateGestureMapMessage) {
                ((RotateGestureMapMessage) remove).recycle();
            } else if (remove instanceof ScaleGestureMapMessage) {
                ((ScaleGestureMapMessage) remove).recycle();
            }
        }
    }

    public void AddOverlayTexture(int i, Bitmap bitmap, int i2, int i3) {
        GLTextureProperty gLTextureProperty = new GLTextureProperty();
        gLTextureProperty.mId = i2;
        gLTextureProperty.mAnchor = i3;
        gLTextureProperty.mBitmap = bitmap;
        gLTextureProperty.mXRatio = 0.0f;
        gLTextureProperty.mYRatio = 0.0f;
        gLTextureProperty.isGenMimps = true;
        addOverlayTexture(i, gLTextureProperty);
    }

    @Override // com.autonavi.amap.mapcore.IAMapEngineCallback
    public void OnIndoorBuildingActivity(int i, byte[] bArr) {
        if (this.mGlMapView != null) {
            try {
                this.mGlMapView.onIndoorBuildingActivity(i, bArr);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public synchronized void addGestureMessage(int i, AbstractGestureMapMessage abstractGestureMapMessage, boolean z, int i2, int i3) {
        if (abstractGestureMapMessage == null) {
            return;
        }
        abstractGestureMapMessage.isGestureScaleByMapCenter = z;
        this.mGestureMessageList.add(abstractGestureMapMessage);
    }

    public void addGroupAnimation(int i, int i2, float f, int i3, int i4, int i5, int i6, AMap.CancelableCallback cancelableCallback) {
        AdglMapAnimGroup adglMapAnimGroup = new AdglMapAnimGroup(i2);
        adglMapAnimGroup.setToCameraDegree(i4, 0);
        adglMapAnimGroup.setToMapAngle(i3, 0);
        adglMapAnimGroup.setToMapLevel(f, 0);
        adglMapAnimGroup.setToMapCenterGeo(i5, i6, 0);
        if (this.mapAnimationMgr == null || !adglMapAnimGroup.isValid()) {
            return;
        }
        this.mapAnimationMgr.addAnimation(adglMapAnimGroup, cancelableCallback);
    }

    public void addMessage(AbstractCameraUpdateMessage abstractCameraUpdateMessage, boolean z) {
        if (!z) {
            if (this.mStateMessageList != null) {
                this.mStateMessageList.add(abstractCameraUpdateMessage);
            }
        } else if (this.mAnimateStateMessageList != null) {
            this.mAnimateStateMessageList.clear();
            this.mAnimateStateMessageList.add(abstractCameraUpdateMessage);
        }
    }

    public String addNativeOverlay(int i, int i2, int i3) {
        if (this.mNativeMapengineInstance == 0) {
            return null;
        }
        String nativeAddNativeOverlay = nativeAddNativeOverlay(i, this.mNativeMapengineInstance, i2, i3);
        if (TextUtils.isEmpty(nativeAddNativeOverlay)) {
            return null;
        }
        return nativeAddNativeOverlay;
    }

    public void addOverlayTexture(int i, GLTextureProperty gLTextureProperty) {
        if (this.mNativeMapengineInstance == 0 || gLTextureProperty == null || gLTextureProperty.mBitmap == null || gLTextureProperty.mBitmap.isRecycled()) {
            return;
        }
        nativeAddOverlayTexture(i, this.mNativeMapengineInstance, gLTextureProperty.mId, gLTextureProperty.mAnchor, gLTextureProperty.mXRatio, gLTextureProperty.mYRatio, gLTextureProperty.mBitmap, gLTextureProperty.isGenMimps, gLTextureProperty.isRepeat);
    }

    public boolean canStopMapRender(int i) {
        return this.isEngineRenderComplete;
    }

    public void cancelAllAMapDownload() {
        try {
            synchronized (this.aMapLoaderHashtable) {
                Iterator<Map.Entry<Long, AMapLoader>> it = this.aMapLoaderHashtable.entrySet().iterator();
                while (it.hasNext()) {
                    AMapLoader value = it.next().getValue();
                    value.doCancel();
                    if (!value.isFinish) {
                        synchronized (value) {
                            if (!value.isFinish) {
                                value.notify();
                                value.isFinish = true;
                            }
                        }
                    }
                }
                this.aMapLoaderHashtable.clear();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.IAMapEngineCallback
    public void cancelRequireMapData(Object obj) {
        if (obj == null || !(obj instanceof AMapLoader)) {
            return;
        }
        ((AMapLoader) obj).doCancel();
    }

    public void changeSurface(int i, int i2) {
    }

    public void clearAllMessages(int i) {
    }

    public void clearAnimations(int i, boolean z) {
        this.mapAnimationMgr.clearAnimations();
    }

    public void clearAnimations(int i, boolean z, int i2) {
        this.mapAnimationMgr.clearAnimations();
    }

    public void createAMapEngineWithFrame(MapViewInitParam mapViewInitParam) {
        if (this.mNativeMapengineInstance != 0) {
            synchronized (GLMapEngine.class) {
                nativeCreateAMapEngineWithFrame(this.mNativeMapengineInstance, mapViewInitParam.engineId, mapViewInitParam.x, mapViewInitParam.y, mapViewInitParam.width, mapViewInitParam.height, mapViewInitParam.screenWidth, mapViewInitParam.screenHeight, mapViewInitParam.screenScale, mapViewInitParam.textScale, mapViewInitParam.mapZoomScale);
            }
        }
    }

    public void createAMapInstance(InitParam initParam) {
        if (initParam == null) {
            return;
        }
        synchronized (GLMapEngine.class) {
            nativeInitParam(initParam.mRootPath, initParam.mConfigContent, initParam.mOfflineDataPath, initParam.mP3dCrossPath);
            this.mNativeMapengineInstance = nativeCreateAMapInstance("", "http://mpsapi.amap.com/", "http://m5.amap.com/");
            nativeInitAMapEngineCallback(this.mNativeMapengineInstance, this);
            initNetworkState();
        }
    }

    public long createOverlay(int i, int i2) {
        if (this.mNativeMapengineInstance != 0) {
            return nativeCreateOverlay(i, this.mNativeMapengineInstance, i2);
        }
        return 0L;
    }

    public void destroyAMapEngine() {
        try {
            this.mRequestDestroy = true;
            cancelAllAMapDownload();
            synchronized (this.mutLock) {
                if (this.mNativeMapengineInstance != 0) {
                    synchronized (this) {
                        if (this.copyGLMapState != null) {
                            this.copyGLMapState.recycle();
                        }
                    }
                    nativeDestroyCurrentState(this.mNativeMapengineInstance, this.state.getNativeInstance());
                    nativeDestroy(this.mNativeMapengineInstance);
                }
                this.mNativeMapengineInstance = 0L;
            }
            this.mGlMapView = null;
            this.mStateMessageList.clear();
            this.mAnimateStateMessageList.clear();
            this.mGestureEndMessageList.clear();
            this.mGestureMessageList.clear();
            this.mMapListener = null;
            fq.b();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public synchronized void finishDownLoad(int i, long j) {
        if (this.aMapLoaderHashtable.containsKey(Long.valueOf(j))) {
            if (this.mNativeMapengineInstance != 0) {
                nativeFinishDownLoad(i, this.mNativeMapengineInstance, j);
            }
            this.aMapLoaderHashtable.remove(Long.valueOf(j));
        }
    }

    @Override // com.autonavi.amap.mapcore.IAMapEngineCallback
    public int generateRequestId() {
        return this.mRequestID.incrementAndGet();
    }

    public int getAnimateionsCount() {
        if (this.mNativeMapengineInstance != 0) {
            return this.mapAnimationMgr.getAnimationsCount();
        }
        return 0;
    }

    public synchronized GLMapState getCloneMapState() {
        this.mLock.lock();
        try {
            if (this.mNativeMapengineInstance != 0) {
                if (this.copyGLMapState == null) {
                    this.copyGLMapState = new GLMapState(1, this.mNativeMapengineInstance);
                }
                this.copyGLMapState.setMapZoomer(this.mGlMapView.getMapConfig().getSZ());
                this.copyGLMapState.setCameraDegree(this.mGlMapView.getMapConfig().getSC());
                this.copyGLMapState.setMapAngle(this.mGlMapView.getMapConfig().getSR());
                this.copyGLMapState.setMapGeoCenter(this.mGlMapView.getMapConfig().getSX(), this.mGlMapView.getMapConfig().getSY());
            }
            this.mLock.unlock();
        } catch (Throwable th) {
            this.mLock.unlock();
            throw th;
        }
        return this.copyGLMapState;
    }

    public Context getContext() {
        return this.context;
    }

    public void getCurTileIDs(int i, int[] iArr) {
        if (iArr != null) {
            for (int i2 = 0; i2 < iArr.length; i2++) {
                iArr[i2] = 0;
            }
            nativeGetCurTileIDs(i, this.mNativeMapengineInstance, iArr, iArr.length);
        }
    }

    public int getEngineIDWithGestureInfo(EAMapPlatformGestureInfo eAMapPlatformGestureInfo) {
        long j = this.mNativeMapengineInstance;
        return 1;
    }

    public int getEngineIDWithType(int i) {
        return 1;
    }

    public long getGlOverlayMgrPtr(int i) {
        if (this.mNativeMapengineInstance != 0) {
            return nativeGetGlOverlayMgrPtr(i, this.mNativeMapengineInstance);
        }
        return 0L;
    }

    public boolean getIsProcessBuildingMark(int i) {
        return false;
    }

    public byte[] getLabelBuffer(int i, int i2, int i3, int i4) {
        this.mLock.lock();
        try {
            byte[] bArr = new byte[3072];
            if (this.mNativeMapengineInstance != 0) {
                nativeSelectMapPois(i, this.mNativeMapengineInstance, i2, i3, i4, bArr);
            }
            return bArr;
        } finally {
            this.mLock.unlock();
        }
    }

    public boolean getMapDataTaskIsCancel(int i, long j) {
        return !this.aMapLoaderHashtable.containsKey(Long.valueOf(j));
    }

    public int[] getMapModeState(int i, boolean z) {
        if (this.mNativeMapengineInstance == 0) {
            return null;
        }
        nativeGetMapModeState(i, this.mNativeMapengineInstance, z);
        return null;
    }

    public GLMapState getMapState(int i) {
        this.mLock.lock();
        try {
            if (this.mNativeMapengineInstance != 0 && this.state == null) {
                long nativeGetCurrentMapState = nativeGetCurrentMapState(i, this.mNativeMapengineInstance);
                if (nativeGetCurrentMapState != 0) {
                    this.state = new GLMapState(this.mNativeMapengineInstance, nativeGetCurrentMapState);
                }
            }
            this.mLock.unlock();
            return this.state;
        } catch (Throwable th) {
            this.mLock.unlock();
            throw th;
        }
    }

    public long getMapStateInstance(int i) {
        return 0L;
    }

    public long getNativeInstance() {
        return this.mNativeMapengineInstance;
    }

    public GLMapState getNewMapState(int i) {
        this.mLock.lock();
        try {
            if (this.mNativeMapengineInstance != 0) {
                return new GLMapState(i, this.mNativeMapengineInstance);
            }
            this.mLock.unlock();
            return null;
        } finally {
            this.mLock.unlock();
        }
    }

    public GLOverlayBundle getOverlayBundle(int i) {
        return this.bundle;
    }

    public boolean getSrvViewStateBoolValue(int i, int i2) {
        if (this.mNativeMapengineInstance != 0) {
            return nativeGetSrvViewStateBoolValue(i, this.mNativeMapengineInstance, i2);
        }
        return false;
    }

    public synchronized AbstractCameraUpdateMessage getStateMessage() {
        if (this.mStateMessageList != null && this.mStateMessageList.size() != 0) {
            AbstractCameraUpdateMessage abstractCameraUpdateMessage = this.mStateMessageList.get(0);
            this.mStateMessageList.remove(abstractCameraUpdateMessage);
            return abstractCameraUpdateMessage;
        }
        return null;
    }

    public int getStateMessageCount() {
        return this.mStateMessageList.size();
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public void initNativeTexture(int i) {
        try {
            BitmapDescriptor fromAsset = BitmapDescriptorFactory.fromAsset("arrow/arrow_line_inner.png");
            Bitmap bitmap = fromAsset != null ? fromAsset.getBitmap() : null;
            BitmapDescriptor fromAsset2 = BitmapDescriptorFactory.fromAsset("arrow/arrow_line_outer.png");
            Bitmap bitmap2 = fromAsset2 != null ? fromAsset2.getBitmap() : null;
            BitmapDescriptor fromAsset3 = BitmapDescriptorFactory.fromAsset("arrow/arrow_line_shadow.png");
            Bitmap bitmap3 = fromAsset3 != null ? fromAsset3.getBitmap() : null;
            AddOverlayTexture(i, bitmap, 111, 4);
            AddOverlayTexture(i, bitmap2, AMapEngineUtils.ARROW_LINE_OUTER_TEXTURE_ID, 4);
            AddOverlayTexture(i, bitmap3, AMapEngineUtils.ARROW_LINE_SHADOW_TEXTURE_ID, 4);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void interruptAnimation() {
        if (isInMapAnimation(1)) {
            try {
                doMapAnimationCancelCallback(this.mapAnimationMgr.getCancelCallback());
                clearAnimations(1, false);
            } catch (Throwable th) {
                ic.c(th, getClass().getName(), "CancelableCallback.onCancel");
                th.printStackTrace();
            }
        }
    }

    public boolean isEngineCreated(int i) {
        if (this.mNativeMapengineInstance != 0) {
            return nativeIsEngineCreated(this.mNativeMapengineInstance, i);
        }
        return false;
    }

    public boolean isInMapAction(int i) {
        return false;
    }

    public boolean isInMapAnimation(int i) {
        return getAnimateionsCount() > 0;
    }

    public synchronized void netError(int i, long j, int i2) {
        if (this.aMapLoaderHashtable.containsKey(Long.valueOf(j))) {
            if (this.mNativeMapengineInstance != 0) {
                nativeFinishDownLoad(i, this.mNativeMapengineInstance, j);
            }
            this.aMapLoaderHashtable.remove(Long.valueOf(j));
        }
    }

    @Override // com.autonavi.amap.mapcore.maploader.NetworkState.NetworkChangeListener
    public void networkStateChanged(Context context) {
        if (this.mRequestDestroy || this.mNativeMapengineInstance == 0) {
            return;
        }
        final boolean isNetworkConnected = NetworkState.isNetworkConnected(context);
        this.mGlMapView.queueEvent(new Runnable() { // from class: com.autonavi.ae.gmap.GLMapEngine.4
            @Override // java.lang.Runnable
            public void run() {
                GLMapEngine.nativeSetNetStatus(GLMapEngine.this.mNativeMapengineInstance, isNetworkConnected ? 1 : 0);
            }
        });
    }

    public void onClearCache(int i) {
    }

    @Override // com.autonavi.amap.mapcore.IAMapEngineCallback
    public void onMapRender(int i, int i2) {
        try {
            if (i2 != 13) {
                switch (i2) {
                    case 5:
                        if (this.mMapListener != null) {
                            this.mMapListener.beforeDrawLabel(i, getMapState(i));
                            break;
                        }
                        break;
                    case 6:
                        if (this.mMapListener != null) {
                            this.mMapListener.afterDrawLabel(i, getMapState(i));
                            break;
                        }
                        break;
                    case 7:
                        if (this.mMapListener != null) {
                            this.mMapListener.afterRendererOver(i, getMapState(i));
                            break;
                        }
                        break;
                    default:
                }
            } else {
                this.isEngineRenderComplete = true;
            }
        } catch (Throwable unused) {
        }
    }

    public void popRendererState() {
        if (this.mNativeMapengineInstance != 0) {
            nativePopRenderState(1, this.mNativeMapengineInstance);
        }
    }

    public void pushRendererState() {
        if (this.mNativeMapengineInstance != 0) {
            nativePushRendererState(1, this.mNativeMapengineInstance);
        }
    }

    public void putResourceData(int i, byte[] bArr) {
    }

    public synchronized void receiveNetData(int i, long j, byte[] bArr, int i2) {
        if (this.mRequestDestroy) {
            return;
        }
        if (this.mNativeMapengineInstance != 0) {
            nativeReceiveNetData(i, this.mNativeMapengineInstance, bArr, j, i2);
        }
    }

    public void releaseNetworkState() {
        if (this.mNetworkState != null) {
            this.mNetworkState.registerNetChangeReceiver(this.context.getApplicationContext(), false);
            this.mNetworkState.setNetworkListener(null);
            this.mNetworkState = null;
        }
    }

    @Override // com.autonavi.amap.mapcore.IAMapEngineCallback
    public void reloadMapResource(int i, String str, int i2) {
    }

    public void removeNativeAllOverlay(int i) {
        if (this.mNativeMapengineInstance != 0) {
            nativeRemoveNativeAllOverlay(i, this.mNativeMapengineInstance);
        }
    }

    public void removeNativeOverlay(int i, String str) {
        if (this.mNativeMapengineInstance == 0 || str == null) {
            return;
        }
        nativeRemoveNativeOverlay(i, this.mNativeMapengineInstance, str);
    }

    public void renderAMap() {
        if (this.mNativeMapengineInstance != 0) {
            boolean processMessage = processMessage();
            synchronized (GLMapEngine.class) {
                nativeRenderAMap(this.mNativeMapengineInstance, 1);
                nativePostRenderAMap(this.mNativeMapengineInstance, 1);
            }
            initAnimation();
            if (processMessage) {
                startCheckEngineRenderComplete();
            }
            if (this.isEngineRenderComplete) {
                return;
            }
            nativeSetRenderListenerStatus(1, this.mNativeMapengineInstance);
        }
    }

    @Override // com.autonavi.amap.mapcore.IAMapEngineCallback
    public byte[] requireCharBitmap(int i, int i2, int i3) {
        return this.mTextTextureGenerator.getTextPixelBuffer(i2, i3);
    }

    @Override // com.autonavi.amap.mapcore.IAMapEngineCallback
    public byte[] requireCharsWidths(int i, int[] iArr, int i2, int i3) {
        return this.mTextTextureGenerator.getCharsWidths(iArr);
    }

    @Override // com.autonavi.amap.mapcore.IAMapEngineCallback
    public void requireMapData(int i, byte[] bArr) {
    }

    @Override // com.autonavi.amap.mapcore.IAMapEngineCallback
    public int requireMapDataAsyn(int i, byte[] bArr) {
        if (!this.mRequestDestroy && bArr != null) {
            AMapLoader.ADataRequestParam aDataRequestParam = new AMapLoader.ADataRequestParam();
            int i2 = GLConvertUtil.getInt(bArr, 0);
            aDataRequestParam.requestBaseUrl = GLConvertUtil.getString(bArr, 4, i2);
            int i3 = i2 + 4;
            int i4 = GLConvertUtil.getInt(bArr, i3);
            int i5 = i3 + 4;
            aDataRequestParam.requestUrl = GLConvertUtil.getString(bArr, i5, i4);
            int i6 = i5 + i4;
            aDataRequestParam.handler = GLConvertUtil.getLong(bArr, i6);
            int i7 = i6 + 8;
            aDataRequestParam.nRequestType = GLConvertUtil.getInt(bArr, i7);
            int i8 = i7 + 4;
            int i9 = GLConvertUtil.getInt(bArr, i8);
            int i10 = i8 + 4;
            aDataRequestParam.enCodeString = GLConvertUtil.getSubBytes(bArr, i10, i9);
            aDataRequestParam.nCompress = GLConvertUtil.getInt(bArr, i10 + i9);
            final AMapLoader aMapLoader = new AMapLoader(i, this, aDataRequestParam);
            synchronized (this) {
                this.aMapLoaderHashtable.put(Long.valueOf(aDataRequestParam.handler), aMapLoader);
            }
            aMapLoader.isFinish = false;
            try {
                fq.a().a(new Runnable() { // from class: com.autonavi.ae.gmap.GLMapEngine.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            try {
                            } catch (Throwable th) {
                                ic.c(th, "download Thread", "AMapLoader doRequest");
                                if (aMapLoader != null && !aMapLoader.isFinish) {
                                    synchronized (aMapLoader) {
                                        if (!aMapLoader.isFinish) {
                                            aMapLoader.notify();
                                            aMapLoader.isFinish = true;
                                        }
                                    }
                                }
                            }
                            if (GLMapEngine.this.mRequestDestroy) {
                                if (aMapLoader == null || aMapLoader.isFinish) {
                                    return;
                                }
                                synchronized (aMapLoader) {
                                    if (!aMapLoader.isFinish) {
                                        aMapLoader.notify();
                                        aMapLoader.isFinish = true;
                                    }
                                }
                                return;
                            }
                            if (aMapLoader == null) {
                                if (aMapLoader == null || aMapLoader.isFinish) {
                                    return;
                                }
                                synchronized (aMapLoader) {
                                    if (!aMapLoader.isFinish) {
                                        aMapLoader.notify();
                                        aMapLoader.isFinish = true;
                                    }
                                }
                                return;
                            }
                            aMapLoader.doRequest();
                            if (aMapLoader != null && !aMapLoader.isFinish) {
                                synchronized (aMapLoader) {
                                    if (!aMapLoader.isFinish) {
                                        aMapLoader.notify();
                                        aMapLoader.isFinish = true;
                                    }
                                }
                            }
                        } catch (Throwable th2) {
                            if (aMapLoader != null && !aMapLoader.isFinish) {
                                synchronized (aMapLoader) {
                                    if (!aMapLoader.isFinish) {
                                        aMapLoader.notify();
                                        aMapLoader.isFinish = true;
                                    }
                                }
                            }
                            throw th2;
                        }
                    }
                });
                synchronized (aMapLoader) {
                    while (!aMapLoader.isFinish) {
                        aMapLoader.wait();
                    }
                }
            } catch (Throwable th) {
                ic.c(th, "download Thread", "requireMapData");
            }
        }
        return 0;
    }

    @Override // com.autonavi.amap.mapcore.IAMapEngineCallback
    public void requireMapRender(int i, int i2, int i3) {
    }

    @Override // com.autonavi.amap.mapcore.IAMapEngineCallback
    public byte[] requireMapResource(int i, String str) {
        byte[] bArr;
        if (str == null) {
            return null;
        }
        String str2 = "map_assets/" + str;
        try {
            if (this.mGlMapView.getMapConfig().isCustomStyleEnable()) {
                if (str.startsWith("icons_5")) {
                    bArr = FileUtil.readFileContents(this.mGlMapView.getMapConfig().getCustomTextureResourcePath());
                } else if (str.startsWith("bktile")) {
                    bArr = FileUtil.readFileContentsFromAssets(this.context, str2);
                    int customBackgroundColor = this.mGlMapView.getMapConfig().getCustomBackgroundColor();
                    if (customBackgroundColor != 0) {
                        bArr = fr.a(bArr, customBackgroundColor);
                    }
                } else {
                    bArr = null;
                }
                if (bArr != null) {
                    return bArr;
                }
            }
            return FileUtil.readFileContentsFromAssets(this.context, str2);
        } catch (Throwable unused) {
            return null;
        }
    }

    public void setAllContentEnable(int i, boolean z) {
        if (this.mNativeMapengineInstance != 0) {
            nativeSetAllContentEnable(i, this.mNativeMapengineInstance, z);
        }
    }

    public void setBackgroundTexture(int i, byte[] bArr) {
        if (bArr == null || this.mNativeMapengineInstance == 0) {
            return;
        }
        nativeSetSetBackgroundTexture(i, this.mNativeMapengineInstance, bArr);
    }

    public void setBuildingEnable(int i, boolean z) {
        if (this.mNativeMapengineInstance != 0) {
            nativeSetBuildingEnable(i, this.mNativeMapengineInstance, z);
        }
    }

    public void setBuildingTextureEnable(int i, boolean z) {
        if (this.mNativeMapengineInstance != 0) {
            nativeSetBuildingTextureEnable(i, this.mNativeMapengineInstance, z);
        }
    }

    public void setCustomStyleData(int i, byte[] bArr, byte[] bArr2) {
        if (bArr == null || this.mNativeMapengineInstance == 0) {
            return;
        }
        nativeSetCustomStyleData(i, this.mNativeMapengineInstance, bArr, bArr2);
    }

    public void setCustomStyleTexture(int i, byte[] bArr) {
        if (bArr == null || this.mNativeMapengineInstance == 0) {
            return;
        }
        nativeSetCustomStyleTexture(i, this.mNativeMapengineInstance, bArr);
    }

    public void setHighlightSubwayEnable(int i, boolean z) {
        if (this.mNativeMapengineInstance != 0) {
            nativeSetHighlightSubwayEnable(i, this.mNativeMapengineInstance, z);
        }
    }

    public void setIndoorBuildingToBeActive(int i, String str, int i2, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || this.mNativeMapengineInstance == 0) {
            return;
        }
        nativeSetIndoorBuildingToBeActive(i, this.mNativeMapengineInstance, str, i2, str2);
    }

    public void setIndoorEnable(int i, boolean z) {
        if (this.mNativeMapengineInstance != 0) {
            nativeSetIndoorEnable(i, this.mNativeMapengineInstance, z);
        }
    }

    public void setInternaltexture(int i, byte[] bArr, int i2) {
    }

    public void setLabelEnable(int i, boolean z) {
        if (this.mNativeMapengineInstance != 0) {
            nativeSetLabelEnable(i, this.mNativeMapengineInstance, z);
        }
    }

    public void setMapListener(IAMapListener iAMapListener) {
        this.mMapListener = iAMapListener;
    }

    public void setMapLoaderToTask(int i, long j, AMapLoader aMapLoader) {
    }

    public boolean setMapModeAndStyle(int i, int i2, int i3, int i4, boolean z, boolean z2, StyleItem[] styleItemArr) {
        if (this.mNativeMapengineInstance == 0) {
            return false;
        }
        boolean nativeMapModeAndStyle = setNativeMapModeAndStyle(i, i2, i3, i4, z, z2, styleItemArr);
        if (styleItemArr != null && z2) {
            int customBackgroundColor = this.mGlMapView.getMapConfig().getCustomBackgroundColor();
            if (customBackgroundColor != 0) {
                setBackgroundTexture(i, fr.a(FileUtil.readFileContentsFromAssets(this.context, AMapEngineUtils.MAP_MAP_ASSETS_NAME + File.separator + AMapEngineUtils.MAP_MAP_ASSETS_BACKGROUND_NAME), customBackgroundColor));
            }
            String customTextureResourcePath = this.mGlMapView.getMapConfig().getCustomTextureResourcePath();
            if (this.mGlMapView.getMapConfig().isProFunctionAuthEnable() && !TextUtils.isEmpty(customTextureResourcePath)) {
                this.mGlMapView.getMapConfig().setUseProFunction(true);
                setCustomStyleTexture(i, FileUtil.readFileContents(customTextureResourcePath));
            }
        } else if (i2 == 0 && i3 == 0 && i4 == 0) {
            setBackgroundTexture(i, FileUtil.readFileContentsFromAssets(this.context, AMapEngineUtils.MAP_MAP_ASSETS_NAME + File.separator + AMapEngineUtils.MAP_MAP_ASSETS_BACKGROUND_NAME));
            setCustomStyleTexture(i, FileUtil.readFileContentsFromAssets(this.context, AMapEngineUtils.MAP_MAP_ASSETS_NAME + File.separator + AMapEngineUtils.MAP_MAP_ASSETS_ICON_5_NAME));
        }
        return nativeMapModeAndStyle;
    }

    public void setMapOpenLayer(String str) {
        if (this.mNativeMapengineInstance == 0 || str == null) {
            return;
        }
        nativesetMapOpenLayer(1, this.mNativeMapengineInstance, str.getBytes());
    }

    public void setMapState(int i, GLMapState gLMapState) {
        setMapState(i, gLMapState, true);
    }

    public void setMapState(int i, GLMapState gLMapState, boolean z) {
        if (this.mNativeMapengineInstance != 0) {
            if (z && this.mGlMapView != null && this.mGlMapView.getMapConfig() != null) {
                this.mGlMapView.checkMapState(gLMapState);
            }
            this.mLock.lock();
            try {
                gLMapState.setNativeMapengineState(i, this.mNativeMapengineInstance);
            } finally {
                this.mLock.unlock();
            }
        }
    }

    public boolean setNativeMapModeAndStyle(int i, int i2, int i3, int i4, boolean z, boolean z2, StyleItem[] styleItemArr) {
        if (this.mNativeMapengineInstance == 0) {
            return false;
        }
        return nativeSetMapModeAndStyle(i, this.mNativeMapengineInstance, new int[]{i2, i3, i4, 0, 0}, z, z2, styleItemArr);
    }

    public void setOfflineDataEnable(int i, boolean z) {
        if (this.mNativeMapengineInstance != 0) {
            nativeSetOfflineDataEnable(i, this.mNativeMapengineInstance, z);
        }
    }

    public void setOvelayBundle(int i, GLOverlayBundle<BaseMapOverlay<?, ?>> gLOverlayBundle) {
        this.bundle = gLOverlayBundle;
    }

    public void setParamater(int i, int i2, int i3, int i4, int i5, int i6) {
        this.mLock.lock();
        try {
            if (this.mNativeMapengineInstance != 0) {
                nativeSetParameter(i, this.mNativeMapengineInstance, i2, i3, i4, i5, i6);
            }
        } finally {
            this.mLock.unlock();
        }
    }

    public void setProjectionCenter(int i, int i2, int i3) {
        if (this.mNativeMapengineInstance != 0) {
            nativeSetProjectionCenter(i, this.mNativeMapengineInstance, i2, i3);
        }
    }

    public void setRoadArrowEnable(int i, boolean z) {
        if (this.mNativeMapengineInstance != 0) {
            nativeSetRoadArrowEnable(i, this.mNativeMapengineInstance, z);
        }
    }

    public void setServiceViewRect(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        nativeSetServiceViewRect(i, this.mNativeMapengineInstance, i2, i3, i4, i5, i6, i7);
    }

    public void setSimple3DEnable(int i, boolean z) {
        if (this.mNativeMapengineInstance != 0) {
            nativeSetSimple3DEnable(i, this.mNativeMapengineInstance, z);
        }
    }

    public void setSkyTexture(int i, byte[] bArr) {
        if (bArr == null || this.mNativeMapengineInstance == 0) {
            return;
        }
        nativeSetSkyTexture(i, this.mNativeMapengineInstance, bArr);
    }

    public void setSrvViewStateBoolValue(int i, int i2, boolean z) {
        if (this.mNativeMapengineInstance != 0) {
            nativeSetSrvViewStateBoolValue(i, this.mNativeMapengineInstance, i2, z);
        }
    }

    public void setTrafficEnable(int i, boolean z) {
        if (this.mNativeMapengineInstance != 0) {
            nativeSetTrafficEnable(i, this.mNativeMapengineInstance, z);
        }
    }

    public void setTrafficStyle(int i, int i2, int i3, int i4, int i5) {
        setTrafficStyle(i, i2, i3, i4, i5, true);
    }

    public void setTrafficStyle(int i, int i2, int i3, int i4, int i5, boolean z) {
        if (this.mNativeMapengineInstance != 0) {
            byte[] readFileContentsFromAssets = FileUtil.readFileContentsFromAssets(this.context, AMapEngineUtils.MAP_MAP_ASSETS_NAME + File.separator + AMapEngineUtils.MAP_MAP_ASSETS_TRL_NAME);
            if (z) {
                nativeSetTrafficTextureAllInOne(i, this.mNativeMapengineInstance, fr.a(readFileContentsFromAssets, new int[]{i4, i5, i3, i2}));
            } else {
                nativeSetTrafficTextureAllInOne(i, this.mNativeMapengineInstance, readFileContentsFromAssets);
            }
        }
    }

    public void startCheckEngineRenderComplete() {
        this.isEngineRenderComplete = false;
    }

    public void startMapSlidAnim(int i, Point point, float f, float f2) {
        if (point == null) {
            return;
        }
        try {
            clearAnimations(i, true);
            GLMapState cloneMapState = getCloneMapState();
            cloneMapState.reset();
            cloneMapState.recalculate();
            float abs = Math.abs(f);
            float abs2 = Math.abs(f2);
            float f3 = 12000;
            if ((abs > abs2 ? abs : abs2) > f3) {
                if (abs > abs2) {
                    f = f > 0.0f ? f3 : -12000;
                    f2 *= f3 / abs;
                } else {
                    f *= f3 / abs2;
                    f2 = f2 > 0.0f ? f3 : -12000;
                }
            }
            int mapWidth = this.mGlMapView.getMapWidth() >> 1;
            int mapHeight = this.mGlMapView.getMapHeight() >> 1;
            if (this.mGlMapView.k()) {
                mapWidth = this.mGlMapView.getMapConfig().getAnchorX();
                mapHeight = this.mGlMapView.getMapConfig().getAnchorY();
            }
            AdglMapAnimFling adglMapAnimFling = new AdglMapAnimFling(500, mapWidth, mapHeight);
            adglMapAnimFling.setPositionAndVelocity(f, f2);
            adglMapAnimFling.commitAnimation(cloneMapState);
            this.mapAnimationMgr.addAnimation(adglMapAnimFling, null);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void startPivotZoomRotateAnim(int i, Point point, float f, int i2, int i3) {
        if (f != -9999.0f || i2 == -9999) {
        }
    }

    public void updateNativeArrowOverlay(int i, String str, int[] iArr, int[] iArr2, int i2, int i3, int i4, float f, int i5, int i6, int i7, boolean z) {
        if (this.mNativeMapengineInstance == 0 || str == null) {
            return;
        }
        nativeUpdateNativeArrowOverlay(i, this.mNativeMapengineInstance, str, iArr, iArr2, i2, i3, i4, f, z, i5, i6, i7);
    }
}
