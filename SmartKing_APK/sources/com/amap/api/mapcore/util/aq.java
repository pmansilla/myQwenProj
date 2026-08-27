package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.RemoteException;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.TileOverlay;
import com.amap.api.maps.model.TileOverlayOptions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/* compiled from: TileOverlayView.java */
/* loaded from: classes.dex */
public class aq {
    ed d;
    private ad f;
    private Context g;
    List<du> a = new ArrayList();
    a b = new a();
    List<Integer> c = new ArrayList();
    float[] e = new float[16];

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TileOverlayView.java */
    /* loaded from: classes.dex */
    public static class a implements Serializable, Comparator<Object> {
        a() {
        }

        @Override // java.util.Comparator
        public int compare(Object obj, Object obj2) {
            du duVar = (du) obj;
            du duVar2 = (du) obj2;
            if (duVar == null || duVar2 == null) {
                return 0;
            }
            try {
                return Float.compare(duVar.getZIndex(), duVar2.getZIndex());
            } catch (Throwable th) {
                ic.c(th, "TileOverlayView", "compare");
                th.printStackTrace();
                return 0;
            }
        }
    }

    public aq(Context context, ad adVar) {
        this.d = null;
        this.f = adVar;
        this.g = context;
        TileOverlayOptions tileProvider = new TileOverlayOptions().tileProvider(new eu(256, 256, this.f.getMapConfig()));
        tileProvider.memCacheSize(10485760);
        tileProvider.diskCacheSize(20480);
        this.d = new ed(tileProvider, this, true);
    }

    private boolean i() {
        if (this.f == null) {
            return false;
        }
        return MapsInitializer.isLoadWorldGridMap() || this.f.getMapConfig().getMapLanguage().equals(AMap.ENGLISH);
    }

    public ad a() {
        return this.f;
    }

    public TileOverlay a(TileOverlayOptions tileOverlayOptions) throws RemoteException {
        if (tileOverlayOptions == null || tileOverlayOptions.getTileProvider() == null) {
            return null;
        }
        try {
            ed edVar = new ed(tileOverlayOptions, this, false);
            a(edVar);
            edVar.a(true);
            this.f.setRunLowFrame(false);
            return new TileOverlay(edVar);
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public void a(int i) {
        this.c.add(Integer.valueOf(i));
    }

    public void a(du duVar) {
        synchronized (this.a) {
            b(duVar);
            this.a.add(duVar);
        }
        d();
    }

    public void a(String str) {
        if (this.d != null) {
            this.d.a(str);
        }
    }

    public void a(boolean z) {
        try {
            if (i()) {
                CameraPosition cameraPosition = this.f.getCameraPosition();
                if (cameraPosition == null) {
                    return;
                }
                if (!cameraPosition.isAbroad || cameraPosition.zoom <= 7.0f) {
                    if (this.d != null) {
                        if (this.f.getMapConfig().getMapLanguage().equals(AMap.ENGLISH)) {
                            this.d.a(z);
                        } else {
                            this.d.b();
                        }
                    }
                } else if (this.f.getMapType() == 1) {
                    if (this.d != null) {
                        this.d.a(z);
                    }
                } else if (this.d != null) {
                    this.d.b();
                }
            }
            synchronized (this.a) {
                int size = this.a.size();
                for (int i = 0; i < size; i++) {
                    du duVar = this.a.get(i);
                    if (duVar != null && duVar.isVisible()) {
                        duVar.a(z);
                    }
                }
            }
        } catch (Throwable th) {
            ic.c(th, "TileOverlayView", "refresh");
        }
    }

    public void b() {
        try {
            Iterator<Integer> it = this.c.iterator();
            while (it.hasNext()) {
                fr.b(it.next().intValue());
            }
            this.c.clear();
            if (i() && this.d != null) {
                this.d.a();
            }
            synchronized (this.a) {
                int size = this.a.size();
                for (int i = 0; i < size; i++) {
                    du duVar = this.a.get(i);
                    if (duVar.isVisible()) {
                        duVar.a();
                    }
                }
            }
        } catch (Throwable unused) {
        }
    }

    public void b(boolean z) {
        if (this.d != null) {
            this.d.b(z);
        }
        synchronized (this.a) {
            int size = this.a.size();
            for (int i = 0; i < size; i++) {
                du duVar = this.a.get(i);
                if (duVar != null) {
                    duVar.b(z);
                }
            }
        }
    }

    public boolean b(du duVar) {
        boolean remove;
        synchronized (this.a) {
            remove = this.a.remove(duVar);
        }
        return remove;
    }

    public void c() {
        synchronized (this.a) {
            int size = this.a.size();
            for (int i = 0; i < size; i++) {
                du duVar = this.a.get(i);
                if (duVar != null) {
                    duVar.destroy(true);
                }
            }
            this.a.clear();
        }
    }

    public void d() {
        synchronized (this.a) {
            Collections.sort(this.a, this.b);
        }
    }

    public Context e() {
        return this.g;
    }

    public void f() {
        c();
        if (this.d != null) {
            this.d.c();
            this.d.destroy(false);
        }
        this.d = null;
    }

    public float[] g() {
        return this.f != null ? this.f.x() : this.e;
    }

    public void h() {
        if (this.d != null) {
            this.d.clearTileCache();
            fh.a(this.g, "Map3DCache", "time", (Object) Long.valueOf(System.currentTimeMillis()));
        }
        synchronized (this.a) {
            int size = this.a.size();
            for (int i = 0; i < size; i++) {
                du duVar = this.a.get(i);
                if (duVar != null) {
                    duVar.clearTileCache();
                }
            }
        }
    }
}
