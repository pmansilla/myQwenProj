package com.amap.api.mapcore.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: UiSettingsDelegateImp.java */
/* loaded from: classes.dex */
public class ar implements ag {
    private ad b;
    private boolean c = true;
    private boolean d = true;
    private boolean e = true;
    private boolean f = false;
    private boolean g = true;
    private boolean h = true;
    private boolean i = true;
    private boolean j = false;
    private boolean k = true;
    private int l = 0;
    private int m = 1;
    private boolean n = true;
    private boolean o = false;
    private boolean p = false;
    final Handler a = new Handler(Looper.getMainLooper()) { // from class: com.amap.api.mapcore.util.ar.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message == null || ar.this.b == null) {
                return;
            }
            try {
                switch (message.what) {
                    case 0:
                        ar.this.b.a(ar.this.h);
                        break;
                    case 1:
                        ar.this.b.e(ar.this.j);
                        break;
                    case 2:
                        ar.this.b.d(ar.this.i);
                        break;
                    case 3:
                        ar.this.b.c(ar.this.f);
                        break;
                    case 4:
                        ar.this.b.b(ar.this.n);
                        break;
                    case 5:
                        ar.this.b.h(ar.this.k);
                        break;
                    case 6:
                        ar.this.b.z();
                        break;
                }
            } catch (Throwable th) {
                ic.c(th, "UiSettingsDelegateImp", "handleMessage");
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    public ar(ad adVar) {
        this.b = adVar;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public float getLogoMarginRate(int i) {
        return this.b.l(i);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public int getLogoPosition() throws RemoteException {
        return this.l;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public int getZoomPosition() throws RemoteException {
        return this.m;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public boolean isCompassEnabled() throws RemoteException {
        return this.i;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public boolean isGestureScaleByMapCenter() throws RemoteException {
        return this.p;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public boolean isIndoorSwitchEnabled() throws RemoteException {
        return this.n;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public boolean isLogoEnable() {
        return this.k;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public boolean isMyLocationButtonEnabled() throws RemoteException {
        return this.f;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public boolean isRotateGesturesEnabled() throws RemoteException {
        return this.c;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public boolean isScaleControlsEnabled() throws RemoteException {
        return this.j;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public boolean isScrollGesturesEnabled() throws RemoteException {
        return this.d;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public boolean isTiltGesturesEnabled() throws RemoteException {
        return this.e;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public boolean isZoomControlsEnabled() throws RemoteException {
        return this.h;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public boolean isZoomGesturesEnabled() throws RemoteException {
        return this.g;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public boolean isZoomInByScreenCenter() {
        return this.o;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public void requestRefreshLogo() {
        this.a.obtainMessage(6).sendToTarget();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public void setAllGesturesEnabled(boolean z) throws RemoteException {
        setRotateGesturesEnabled(z);
        setTiltGesturesEnabled(z);
        setZoomGesturesEnabled(z);
        setScrollGesturesEnabled(z);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public void setCompassEnabled(boolean z) throws RemoteException {
        this.i = z;
        this.a.obtainMessage(2).sendToTarget();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public void setGestureScaleByMapCenter(boolean z) throws RemoteException {
        this.p = z;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public void setIndoorSwitchEnabled(boolean z) throws RemoteException {
        this.n = z;
        this.a.obtainMessage(4).sendToTarget();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public void setLogoBottomMargin(int i) {
        this.b.j(i);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public void setLogoEnable(boolean z) {
        this.k = z;
        this.a.obtainMessage(5).sendToTarget();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public void setLogoLeftMargin(int i) {
        this.b.k(i);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public void setLogoMarginRate(int i, float f) {
        this.b.a(i, f);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public void setLogoPosition(int i) throws RemoteException {
        this.l = i;
        this.b.i(i);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public void setMyLocationButtonEnabled(boolean z) throws RemoteException {
        this.f = z;
        this.a.obtainMessage(3).sendToTarget();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public void setRotateGesturesEnabled(boolean z) throws RemoteException {
        this.c = z;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public void setScaleControlsEnabled(boolean z) throws RemoteException {
        this.j = z;
        this.a.obtainMessage(1).sendToTarget();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public void setScrollGesturesEnabled(boolean z) throws RemoteException {
        this.d = z;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public void setTiltGesturesEnabled(boolean z) throws RemoteException {
        this.e = z;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public void setZoomControlsEnabled(boolean z) throws RemoteException {
        this.h = z;
        this.a.obtainMessage(0).sendToTarget();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public void setZoomGesturesEnabled(boolean z) throws RemoteException {
        this.g = z;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public void setZoomInByScreenCenter(boolean z) {
        this.o = z;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IUiSettings
    public void setZoomPosition(int i) throws RemoteException {
        this.m = i;
        this.b.g(i);
    }
}
