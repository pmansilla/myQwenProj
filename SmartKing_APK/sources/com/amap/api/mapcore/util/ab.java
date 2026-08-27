package com.amap.api.mapcore.util;

import android.opengl.GLES20;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import com.amap.api.maps.interfaces.IGlOverlayLayer;
import com.amap.api.maps.model.ArcOptions;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.GroundOverlayOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.NavigateArrowOptions;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.particle.ParticleOverlayOptions;
import com.autonavi.amap.mapcore.MapConfig;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/* compiled from: GlOverlayLayer.java */
/* loaded from: classes.dex */
public class ab implements IGlOverlayLayer {
    ad a;
    private ef c;
    private int d = 0;
    private List<Cdo> e = new Vector(500);
    private List<am> f = new ArrayList();
    private int[] g = new int[1];
    private Handler h = new Handler(Looper.getMainLooper());
    private Runnable i = new Runnable() { // from class: com.amap.api.mapcore.util.ab.1
        @Override // java.lang.Runnable
        public synchronized void run() {
            try {
                synchronized (ab.this) {
                    if (ab.this.e != null && ab.this.e.size() > 0) {
                        Collections.sort(ab.this.e, ab.this.b);
                    }
                }
            } catch (Throwable th) {
                ic.c(th, "MapOverlayImageView", "changeOverlayIndex");
            }
        }
    };
    a b = new a();

    /* compiled from: GlOverlayLayer.java */
    /* loaded from: classes.dex */
    static class a implements Serializable, Comparator<Object> {
        a() {
        }

        @Override // java.util.Comparator
        public int compare(Object obj, Object obj2) {
            Cdo cdo = (Cdo) obj;
            Cdo cdo2 = (Cdo) obj2;
            if (cdo == null || cdo2 == null) {
                return 0;
            }
            try {
                if (cdo.getZIndex() > cdo2.getZIndex()) {
                    return 1;
                }
                return cdo.getZIndex() < cdo2.getZIndex() ? -1 : 0;
            } catch (Throwable th) {
                ic.c(th, "GlOverlayLayer", "compare");
                th.printStackTrace();
                return 0;
            }
        }
    }

    public ab(ad adVar) {
        this.a = adVar;
    }

    private void a(Cdo cdo) throws RemoteException {
        this.e.add(cdo);
        e();
    }

    @Override // com.amap.api.maps.interfaces.IGlOverlayLayer
    public boolean IsCircleContainPoint(CircleOptions circleOptions, LatLng latLng) {
        return false;
    }

    @Override // com.amap.api.maps.interfaces.IGlOverlayLayer
    public boolean IsPolygonContainsPoint(PolygonOptions polygonOptions, LatLng latLng) {
        return false;
    }

    public am a(BitmapDescriptor bitmapDescriptor) {
        if (this.a != null) {
            return this.a.a(bitmapDescriptor, true);
        }
        return null;
    }

    public synchronized di a(ArcOptions arcOptions) throws RemoteException {
        if (arcOptions == null) {
            return null;
        }
        dd ddVar = new dd(this.a);
        ddVar.setStrokeColor(arcOptions.getStrokeColor());
        ddVar.a(arcOptions.getStart());
        ddVar.b(arcOptions.getPassed());
        ddVar.c(arcOptions.getEnd());
        ddVar.setVisible(arcOptions.isVisible());
        ddVar.setStrokeWidth(arcOptions.getStrokeWidth());
        ddVar.setZIndex(arcOptions.getZIndex());
        a(ddVar);
        return ddVar;
    }

    public dj a() throws RemoteException {
        de deVar = new de(this);
        deVar.a(this.c);
        a(deVar);
        return deVar;
    }

    public synchronized dk a(CircleOptions circleOptions) throws RemoteException {
        if (circleOptions == null) {
            return null;
        }
        df dfVar = new df(this.a);
        dfVar.setFillColor(circleOptions.getFillColor());
        dfVar.setCenter(circleOptions.getCenter());
        dfVar.setVisible(circleOptions.isVisible());
        dfVar.setHoleOptions(circleOptions.getHoleOptions());
        dfVar.setStrokeWidth(circleOptions.getStrokeWidth());
        dfVar.setZIndex(circleOptions.getZIndex());
        dfVar.setStrokeColor(circleOptions.getStrokeColor());
        dfVar.setRadius(circleOptions.getRadius());
        dfVar.setDottedLineType(circleOptions.getStrokeDottedLineType());
        a(dfVar);
        return dfVar;
    }

    public synchronized dl a(GroundOverlayOptions groundOverlayOptions) throws RemoteException {
        if (groundOverlayOptions == null) {
            return null;
        }
        dh dhVar = new dh(this.a, this);
        dhVar.a(groundOverlayOptions.getAnchorU(), groundOverlayOptions.getAnchorV());
        dhVar.setDimensions(groundOverlayOptions.getWidth(), groundOverlayOptions.getHeight());
        dhVar.setImage(groundOverlayOptions.getImage());
        dhVar.setPosition(groundOverlayOptions.getLocation());
        dhVar.setPositionFromBounds(groundOverlayOptions.getBounds());
        dhVar.setBearing(groundOverlayOptions.getBearing());
        dhVar.setTransparency(groundOverlayOptions.getTransparency());
        dhVar.setVisible(groundOverlayOptions.isVisible());
        dhVar.setZIndex(groundOverlayOptions.getZIndex());
        a(dhVar);
        return dhVar;
    }

    public synchronized dn a(NavigateArrowOptions navigateArrowOptions) throws RemoteException {
        if (navigateArrowOptions == null) {
            return null;
        }
        dx dxVar = new dx(this.a);
        dxVar.setTopColor(navigateArrowOptions.getTopColor());
        dxVar.setSideColor(navigateArrowOptions.getSideColor());
        dxVar.setPoints(navigateArrowOptions.getPoints());
        dxVar.setVisible(navigateArrowOptions.isVisible());
        dxVar.setWidth(navigateArrowOptions.getWidth());
        dxVar.setZIndex(navigateArrowOptions.getZIndex());
        dxVar.set3DModel(navigateArrowOptions.is3DModel());
        a(dxVar);
        return dxVar;
    }

    public synchronized Cdo a(LatLng latLng) {
        for (Cdo cdo : this.e) {
            if (cdo != null && cdo.c() && (cdo instanceof ds) && ((ds) cdo).a(latLng)) {
                return cdo;
            }
        }
        return null;
    }

    public synchronized dq a(ParticleOverlayOptions particleOverlayOptions) throws RemoteException {
        if (particleOverlayOptions == null) {
            return null;
        }
        dy dyVar = new dy(this);
        dyVar.a(particleOverlayOptions);
        a(dyVar);
        return dyVar;
    }

    public synchronized dr a(PolygonOptions polygonOptions) throws RemoteException {
        if (polygonOptions == null) {
            return null;
        }
        dz dzVar = new dz(this.a);
        dzVar.setFillColor(polygonOptions.getFillColor());
        dzVar.setPoints(polygonOptions.getPoints());
        dzVar.setHoleOptions(polygonOptions.getHoleOptions());
        dzVar.setVisible(polygonOptions.isVisible());
        dzVar.setStrokeWidth(polygonOptions.getStrokeWidth());
        dzVar.setZIndex(polygonOptions.getZIndex());
        dzVar.setStrokeColor(polygonOptions.getStrokeColor());
        a(dzVar);
        return dzVar;
    }

    public synchronized ds a(PolylineOptions polylineOptions) throws RemoteException {
        if (polylineOptions == null) {
            return null;
        }
        ea eaVar = new ea(this, polylineOptions);
        if (this.c != null) {
            eaVar.a(this.c);
        }
        a(eaVar);
        return eaVar;
    }

    public synchronized String a(String str) {
        this.d++;
        return str + this.d;
    }

    public void a(am amVar) {
        synchronized (this.f) {
            if (amVar != null) {
                try {
                    this.f.add(amVar);
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public void a(ef efVar) {
        this.c = efVar;
    }

    public void a(boolean z) {
        if (this.a != null) {
            this.a.setRunLowFrame(z);
        }
    }

    public synchronized void a(boolean z, int i) {
        MapConfig mapConfig;
        try {
            f();
            mapConfig = this.a.getMapConfig();
        } catch (Throwable th) {
            ic.c(th, "GlOverlayLayer", "draw");
        }
        if (mapConfig == null) {
            return;
        }
        int size = this.e.size();
        for (Cdo cdo : this.e) {
            if (cdo.isVisible()) {
                if (size > 20) {
                    if (cdo.a()) {
                        if (z) {
                            if (cdo.getZIndex() <= i) {
                                cdo.a(mapConfig);
                            }
                        } else if (cdo.getZIndex() > i) {
                            cdo.a(mapConfig);
                        }
                    }
                } else if (z) {
                    if (cdo.getZIndex() <= i) {
                        cdo.a(mapConfig);
                    }
                } else if (cdo.getZIndex() > i) {
                    cdo.a(mapConfig);
                }
            }
        }
    }

    public ef b() {
        return this.c;
    }

    public synchronized void b(String str) {
        try {
            if (str != null) {
                try {
                } catch (Throwable th) {
                    ic.c(th, "GlOverlayLayer", "clear");
                    th.printStackTrace();
                    Log.d("amapApi", "GlOverlayLayer clear erro" + th.getMessage());
                }
                if (str.trim().length() != 0) {
                    Cdo cdo = null;
                    Iterator<Cdo> it = this.e.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        Cdo next = it.next();
                        if (str.equals(next.getId())) {
                            cdo = next;
                            break;
                        }
                    }
                    this.e.clear();
                    if (cdo != null) {
                        this.e.add(cdo);
                    }
                }
            }
            this.e.clear();
            c();
        } catch (Throwable th2) {
            throw th2;
        }
    }

    synchronized Cdo c(String str) throws RemoteException {
        for (Cdo cdo : this.e) {
            if (cdo != null && cdo.getId().equals(str)) {
                return cdo;
            }
        }
        return null;
    }

    public synchronized void c() {
        this.d = 0;
    }

    public synchronized void d() {
        try {
            Iterator<Cdo> it = this.e.iterator();
            while (it.hasNext()) {
                it.next().destroy();
            }
            b(null);
        } catch (Throwable th) {
            ic.c(th, "GlOverlayLayer", "destory");
            th.printStackTrace();
            Log.d("amapApi", "GlOverlayLayer destory erro" + th.getMessage());
        }
    }

    public synchronized void e() {
        this.h.removeCallbacks(this.i);
        this.h.postDelayed(this.i, 10L);
    }

    public void f() {
        synchronized (this.f) {
            for (int i = 0; i < this.f.size(); i++) {
                am amVar = this.f.get(i);
                if (amVar != null) {
                    amVar.m();
                    if (amVar.n() <= 0) {
                        this.g[0] = amVar.k();
                        GLES20.glDeleteTextures(1, this.g, 0);
                        if (this.a != null) {
                            this.a.c(amVar.o());
                        }
                    }
                }
            }
            this.f.clear();
        }
    }

    public ad g() {
        return this.a;
    }

    @Override // com.amap.api.maps.interfaces.IGlOverlayLayer
    public int getCurrentParticleNum(String str) {
        return 0;
    }

    @Override // com.amap.api.maps.interfaces.IGlOverlayLayer
    public LatLng getNearestLatLng(PolylineOptions polylineOptions, LatLng latLng) {
        return null;
    }

    public float[] h() {
        return this.a != null ? this.a.x() : new float[16];
    }

    @Override // com.amap.api.maps.interfaces.IGlOverlayLayer
    public void prepareIcon(Object obj) {
    }

    @Override // com.amap.api.maps.interfaces.IGlOverlayLayer
    public void processCircleHoleOption(CircleOptions circleOptions) {
    }

    @Override // com.amap.api.maps.interfaces.IGlOverlayLayer
    public void processPolygonHoleOption(PolygonOptions polygonOptions) {
    }

    @Override // com.amap.api.maps.interfaces.IGlOverlayLayer
    public synchronized boolean removeOverlay(String str) throws RemoteException {
        Cdo c = c(str);
        if (c == null) {
            return false;
        }
        return this.e.remove(c);
    }

    @Override // com.amap.api.maps.interfaces.IGlOverlayLayer
    public void updateOption(String str, Object obj) {
    }
}
