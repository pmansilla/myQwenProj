package com.amap.api.mapcore.util;

import android.os.RemoteException;
import com.autonavi.amap.mapcore.MapConfig;
import com.autonavi.amap.mapcore.interfaces.IOverlay;

/* compiled from: IOverlayDelegate.java */
/* renamed from: com.amap.api.mapcore.util.do, reason: invalid class name */
/* loaded from: classes.dex */
public interface Cdo extends IOverlay {
    void a(MapConfig mapConfig) throws RemoteException;

    boolean a();

    boolean c();
}
