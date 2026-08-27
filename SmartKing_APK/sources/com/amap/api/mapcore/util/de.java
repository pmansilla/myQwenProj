package com.amap.api.mapcore.util;

import android.os.RemoteException;
import android.support.v4.internal.view.SupportMenu;
import com.amap.api.maps.model.BuildingOverlayOptions;
import com.amap.api.maps.model.LatLng;
import com.autonavi.amap.mapcore.AMapNativeBuildingRenderer;
import com.autonavi.amap.mapcore.MapConfig;
import com.autonavi.amap.mapcore.interfaces.IOverlay;
import java.util.ArrayList;
import java.util.List;

/* compiled from: BuildingOverlayDelegateImp.java */
/* loaded from: classes.dex */
public class de implements dj, Cdo {
    private ab b;
    private BuildingOverlayOptions c;
    private List<BuildingOverlayOptions> e;
    private String g;
    private float h;
    private boolean i;
    private ef j;
    long a = -1;
    private List<BuildingOverlayOptions> d = new ArrayList();
    private boolean f = true;

    public de(ab abVar) {
        try {
            this.b = abVar;
            if (this.c == null) {
                this.c = new BuildingOverlayOptions();
                this.c.setVisible(true);
                ArrayList arrayList = new ArrayList();
                arrayList.add(new LatLng(84.9d, -179.9d));
                arrayList.add(new LatLng(84.9d, 179.9d));
                arrayList.add(new LatLng(-84.9d, 179.9d));
                arrayList.add(new LatLng(-84.9d, -179.9d));
                this.c.setBuildingLatlngs(arrayList);
                this.c.setBuildingTopColor(SupportMenu.CATEGORY_MASK);
                this.c.setBuildingSideColor(-12303292);
                this.c.setVisible(true);
                this.c.setZIndex(1.0f);
                this.d.add(this.c);
                a(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void a(boolean z) {
        try {
            synchronized (this) {
                try {
                    if (z) {
                        this.d.set(0, this.c);
                    } else {
                        this.d.removeAll(this.e);
                        this.d.set(0, this.c);
                        this.d.addAll(this.e);
                    }
                    this.i = true;
                } finally {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void a(ef efVar) {
        this.j = efVar;
    }

    @Override // com.amap.api.mapcore.util.dj
    public void a(BuildingOverlayOptions buildingOverlayOptions) {
        if (buildingOverlayOptions != null) {
            synchronized (this) {
                this.c = buildingOverlayOptions;
            }
            a(true);
        }
    }

    @Override // com.amap.api.mapcore.util.Cdo
    public void a(MapConfig mapConfig) throws RemoteException {
        if (mapConfig == null) {
            return;
        }
        try {
            if (this.a == -1) {
                this.a = AMapNativeBuildingRenderer.nativeCreate();
                if (this.a == -1 || this.j == null) {
                    return;
                }
                AMapNativeBuildingRenderer.nativeSetGLShaderManager(this.a, this.j.a());
                return;
            }
            synchronized (this) {
                if (this.a != -1) {
                    if (this.i) {
                        AMapNativeBuildingRenderer.nativeClearBuildingOptions(this.a);
                        for (int i = 0; i < this.d.size(); i++) {
                            AMapNativeBuildingRenderer.addBuildingOptions(this.a, this.d.get(i));
                        }
                        this.i = false;
                    }
                    AMapNativeBuildingRenderer.render(this.a, mapConfig.getViewMatrix(), mapConfig.getProjectionMatrix(), mapConfig.getSX(), mapConfig.getSY(), mapConfig.getSZ(), mapConfig.getCurTileIds());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.amap.api.mapcore.util.dj
    public void a(List<BuildingOverlayOptions> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        synchronized (this) {
            this.e = list;
        }
        a(false);
    }

    @Override // com.amap.api.mapcore.util.Cdo
    public boolean a() {
        return true;
    }

    @Override // com.amap.api.mapcore.util.dj
    public List<BuildingOverlayOptions> b() {
        return this.e;
    }

    @Override // com.amap.api.mapcore.util.Cdo
    public boolean c() {
        return false;
    }

    @Override // com.amap.api.mapcore.util.dj
    public BuildingOverlayOptions d() {
        BuildingOverlayOptions buildingOverlayOptions;
        synchronized (this) {
            buildingOverlayOptions = this.c;
        }
        return buildingOverlayOptions;
    }

    @Override // com.amap.api.mapcore.util.dj, com.autonavi.amap.mapcore.interfaces.IOverlay
    public void destroy() {
        synchronized (this) {
            if (this.a != -1) {
                AMapNativeBuildingRenderer.nativeDestory(this.a);
                if (this.d != null) {
                    this.d.clear();
                }
                this.e = null;
                this.c = null;
                this.a = -1L;
            }
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean equalsRemote(IOverlay iOverlay) throws RemoteException {
        return false;
    }

    @Override // com.amap.api.mapcore.util.dj, com.autonavi.amap.mapcore.interfaces.IOverlay
    public String getId() {
        if (this.g == null) {
            this.g = this.b.a("Building");
        }
        return this.g;
    }

    @Override // com.amap.api.mapcore.util.dj, com.autonavi.amap.mapcore.interfaces.IOverlay
    public float getZIndex() {
        return this.h;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public int hashCodeRemote() throws RemoteException {
        return 0;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean isAboveMaskLayer() {
        return false;
    }

    @Override // com.amap.api.mapcore.util.dj, com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean isVisible() {
        return this.f;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void remove() throws RemoteException {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setAboveMaskLayer(boolean z) {
    }

    @Override // com.amap.api.mapcore.util.dj, com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setVisible(boolean z) {
        this.f = z;
    }

    @Override // com.amap.api.mapcore.util.dj, com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setZIndex(float f) {
        try {
            this.h = f;
            this.b.e();
            synchronized (this) {
                this.c.setZIndex(this.h);
            }
            a(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
