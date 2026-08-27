package com.amap.location.offline;

import android.content.Context;
import android.support.annotation.NonNull;
import com.amap.location.common.model.AmapLoc;
import com.amap.location.common.model.FPS;
import com.amap.openapi.bo;
import com.amap.openapi.bp;
import com.amap.openapi.co;

/* loaded from: classes.dex */
public class OfflineManager {
    private static final String TAG = "OfflineManager";
    private static volatile OfflineManager mInstance;
    private a mCloudConfig;
    private bo mCloudWrapper;
    private OfflineConfig mConfig;
    private Context mContext;
    private bp mOfflineCore;
    private b mOfflineRemoteProxy;

    private OfflineManager() {
    }

    public static OfflineManager getInstance() {
        if (mInstance == null) {
            synchronized (OfflineManager.class) {
                if (mInstance == null) {
                    mInstance = new OfflineManager();
                }
            }
        }
        return mInstance;
    }

    private void initOfflineCore() {
        this.mOfflineCore = new bp(this.mContext, this.mConfig, this.mCloudConfig);
        this.mOfflineCore.a();
    }

    public synchronized void correctLocation(@NonNull FPS fps, AmapLoc amapLoc) {
        correctLocation(fps, amapLoc, this.mContext.getPackageName());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void correctLocation(@NonNull FPS fps, AmapLoc amapLoc, String str) {
        if (isEnable()) {
            if (this.mOfflineCore != null) {
                this.mOfflineCore.a(fps, amapLoc);
            } else {
                if (!this.mOfflineRemoteProxy.a(fps, amapLoc, str)) {
                    initOfflineCore();
                }
            }
        }
    }

    public synchronized void destroy() {
        OfflineConfig offlineConfig = this.mConfig;
        this.mConfig = null;
        this.mCloudConfig = null;
        this.mOfflineRemoteProxy = null;
        if (this.mCloudWrapper != null) {
            this.mCloudWrapper.b();
            this.mCloudWrapper = null;
        }
        if (this.mOfflineCore != null) {
            this.mOfflineCore.b();
            this.mOfflineCore = null;
        }
        com.amap.location.offline.upload.a.a(offlineConfig);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized AmapLoc getLocation(@NonNull FPS fps, int i, boolean z, String str) {
        if (!isEnable()) {
            return null;
        }
        if (this.mOfflineCore == null) {
            co.a a = this.mOfflineRemoteProxy.a(fps, i, str);
            if (a.a) {
                return a.b;
            }
            initOfflineCore();
        }
        return this.mOfflineCore.a(fps, i, z);
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x001b A[Catch: all -> 0x000f, TryCatch #0 {all -> 0x000f, blocks: (B:17:0x0004, B:19:0x0008, B:6:0x0014, B:7:0x0021, B:15:0x001b), top: B:16:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:6:0x0014 A[Catch: all -> 0x000f, TryCatch #0 {all -> 0x000f, blocks: (B:17:0x0004, B:19:0x0008, B:6:0x0014, B:7:0x0021, B:15:0x001b), top: B:16:0x0004 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized com.amap.location.common.model.AmapLoc getLocation(@android.support.annotation.NonNull com.amap.location.common.model.FPS r3, boolean r4) {
        /*
            r2 = this;
            monitor-enter(r2)
            r0 = 0
            if (r4 == 0) goto L11
            com.amap.location.offline.a r1 = r2.mCloudConfig     // Catch: java.lang.Throwable -> Lf
            if (r1 == 0) goto L11
            com.amap.location.offline.a r1 = r2.mCloudConfig     // Catch: java.lang.Throwable -> Lf
            int r1 = r1.getMinWifiNum()     // Catch: java.lang.Throwable -> Lf
            goto L12
        Lf:
            r3 = move-exception
            goto L2d
        L11:
            r1 = 0
        L12:
            if (r4 == 0) goto L1b
            r4 = 100033(0x186c1, float:1.40176E-40)
            com.amap.location.offline.upload.a.a(r4)     // Catch: java.lang.Throwable -> Lf
            goto L21
        L1b:
            r4 = 100034(0x186c2, float:1.40177E-40)
            com.amap.location.offline.upload.a.a(r4)     // Catch: java.lang.Throwable -> Lf
        L21:
            android.content.Context r4 = r2.mContext     // Catch: java.lang.Throwable -> Lf
            java.lang.String r4 = r4.getPackageName()     // Catch: java.lang.Throwable -> Lf
            com.amap.location.common.model.AmapLoc r3 = r2.getLocation(r3, r1, r0, r4)     // Catch: java.lang.Throwable -> Lf
            monitor-exit(r2)
            return r3
        L2d:
            monitor-exit(r2)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.location.offline.OfflineManager.getLocation(com.amap.location.common.model.FPS, boolean):com.amap.location.common.model.AmapLoc");
    }

    public synchronized void init(@NonNull Context context, @NonNull OfflineConfig offlineConfig, @NonNull IOfflineCloudConfig iOfflineCloudConfig) {
        if (this.mConfig == null) {
            if (offlineConfig == null) {
                offlineConfig = new OfflineConfig();
            }
            this.mConfig = offlineConfig;
        }
        if (this.mCloudConfig == null) {
            this.mCloudConfig = new a();
            if (iOfflineCloudConfig != null) {
                this.mCloudConfig.a = iOfflineCloudConfig;
            }
        }
        if (this.mCloudWrapper == null) {
            this.mCloudWrapper = new bo(context, this.mConfig, this.mCloudConfig);
            this.mCloudWrapper.a();
        }
        if (this.mOfflineRemoteProxy == null) {
            this.mContext = context.getApplicationContext();
            com.amap.location.offline.upload.a.a(this.mContext, this.mConfig, this.mCloudConfig);
            this.mOfflineRemoteProxy = new b(context, this.mConfig, this.mCloudConfig);
            if (!this.mOfflineRemoteProxy.a(this.mContext.getPackageName()) && this.mOfflineCore == null) {
                initOfflineCore();
            }
        }
    }

    public synchronized boolean isEnable() {
        boolean z;
        if (this.mOfflineRemoteProxy != null && this.mConfig != null && this.mConfig.locEnable && this.mCloudConfig != null) {
            z = this.mCloudConfig.isEnable();
        }
        return z;
    }

    public synchronized void trainingFps(@NonNull FPS fps) {
        getLocation(fps, 0, true, this.mContext.getPackageName());
    }

    public synchronized void updateConfig(OfflineConfig offlineConfig) {
        if (offlineConfig != null) {
            if (this.mOfflineRemoteProxy != null) {
                this.mConfig = offlineConfig;
                this.mOfflineRemoteProxy.a(this.mConfig);
                if (this.mOfflineCore != null) {
                    this.mOfflineCore.a(this.mConfig);
                }
            }
        }
    }
}
