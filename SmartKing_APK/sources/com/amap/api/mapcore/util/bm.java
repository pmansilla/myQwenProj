package com.amap.api.mapcore.util;

import android.os.RemoteException;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.MultiPointItem;
import com.amap.api.maps.model.MultiPointOverlayOptions;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.MapConfig;
import com.autonavi.amap.mapcore.interfaces.IMultiPointOverlay;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* compiled from: MultiPointOverlayManagerLayer.java */
/* loaded from: classes.dex */
public class bm {
    ef a;
    private List<IMultiPointOverlay> b = new ArrayList();
    private AMap.OnMultiPointClickListener c;
    private ad d;

    public bm(ad adVar) {
        this.d = adVar;
    }

    private void a(IMultiPointOverlay iMultiPointOverlay) throws RemoteException {
        synchronized (this.b) {
            this.b.add(iMultiPointOverlay);
        }
    }

    public ef a() {
        this.a = this.d.y();
        return this.a;
    }

    public synchronized IMultiPointOverlay a(MultiPointOverlayOptions multiPointOverlayOptions) throws RemoteException {
        if (multiPointOverlayOptions == null) {
            return null;
        }
        bl blVar = new bl(multiPointOverlayOptions, this);
        a((IMultiPointOverlay) blVar);
        return blVar;
    }

    public void a(bl blVar) {
        this.b.remove(blVar);
    }

    public void a(AMap.OnMultiPointClickListener onMultiPointClickListener) {
        this.c = onMultiPointClickListener;
    }

    public void a(MapConfig mapConfig, float[] fArr, float[] fArr2) {
        try {
            synchronized (this.b) {
                Iterator<IMultiPointOverlay> it = this.b.iterator();
                while (it.hasNext()) {
                    it.next().draw(mapConfig, fArr, fArr2);
                }
            }
        } catch (Throwable th) {
            ic.c(th, "MultiPointOverlayManagerLayer", "draw");
            th.printStackTrace();
        }
    }

    public boolean a(IPoint iPoint) {
        MultiPointItem onClick;
        if (this.c == null) {
            return false;
        }
        synchronized (this.b) {
            for (IMultiPointOverlay iMultiPointOverlay : this.b) {
                if (iMultiPointOverlay != null && (onClick = iMultiPointOverlay.onClick(iPoint)) != null) {
                    return this.c != null ? this.c.onPointClick(onClick) : false;
                }
            }
            return false;
        }
    }

    public synchronized void b() {
        this.c = null;
        try {
            synchronized (this.b) {
                Iterator<IMultiPointOverlay> it = this.b.iterator();
                while (it.hasNext()) {
                    it.next().destroy(false);
                }
                this.b.clear();
            }
        } catch (Throwable th) {
            ic.c(th, "MultiPointOverlayManagerLayer", "destory");
            th.printStackTrace();
        }
    }

    public synchronized void c() {
        try {
            synchronized (this.b) {
                this.b.clear();
            }
        } catch (Throwable th) {
            ic.c(th, "MultiPointOverlayManagerLayer", "clear");
            th.printStackTrace();
        }
    }

    public void d() {
        if (this.d != null) {
            this.d.setRunLowFrame(false);
        }
    }
}
