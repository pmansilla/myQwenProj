package com.amap.api.maps.model;

import android.support.v4.internal.view.SupportMenu;
import android.text.TextUtils;
import com.amap.api.mapcore.util.dj;
import com.amap.api.maps.interfaces.IGlOverlayLayer;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class BuildingOverlay extends BaseOverlay {
    private BuildingOverlayTotalOptions buildingOverlayTotalOptions = new BuildingOverlayTotalOptions();
    private WeakReference<IGlOverlayLayer> glOverlayLayerRef;
    private BuildingOverlayOptions mDefaultOptions;
    private dj mDelegate;
    private List<BuildingOverlayOptions> optionList;

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class BuildingOverlayTotalOptions extends BaseOptions {
        public List<BuildingOverlayOptions> allOptionList;
        private final String type = "BuildingOptions";
        public boolean isVisible = true;
        private float zIndex = 0.0f;

        protected BuildingOverlayTotalOptions() {
        }
    }

    public BuildingOverlay(dj djVar) {
        this.mDelegate = djVar;
    }

    public BuildingOverlay(IGlOverlayLayer iGlOverlayLayer) {
        this.glOverlayLayerRef = new WeakReference<>(iGlOverlayLayer);
        this.buildingOverlayTotalOptions.allOptionList = new ArrayList();
        try {
            if (this.mDefaultOptions == null) {
                this.mDefaultOptions = new BuildingOverlayOptions();
                this.mDefaultOptions.setVisible(true);
                ArrayList arrayList = new ArrayList();
                arrayList.add(new LatLng(84.9d, -179.9d));
                arrayList.add(new LatLng(84.9d, 179.9d));
                arrayList.add(new LatLng(-84.9d, 179.9d));
                arrayList.add(new LatLng(-84.9d, -179.9d));
                this.mDefaultOptions.setBuildingLatlngs(arrayList);
                this.mDefaultOptions.setBuildingTopColor(SupportMenu.CATEGORY_MASK);
                this.mDefaultOptions.setBuildingSideColor(-12303292);
                this.mDefaultOptions.setVisible(true);
                this.mDefaultOptions.setZIndex(1.0f);
                this.buildingOverlayTotalOptions.allOptionList.add(this.mDefaultOptions);
                a(true);
            }
        } catch (Throwable unused) {
        }
    }

    private void a() {
        try {
            IGlOverlayLayer iGlOverlayLayer = this.glOverlayLayerRef.get();
            if (TextUtils.isEmpty(this.overlayName) || iGlOverlayLayer == null) {
                return;
            }
            iGlOverlayLayer.updateOption(this.overlayName, this.buildingOverlayTotalOptions);
        } catch (Throwable unused) {
        }
    }

    private void a(boolean z) {
        try {
            synchronized (this) {
                try {
                    if (z) {
                        this.buildingOverlayTotalOptions.allOptionList.set(0, this.mDefaultOptions);
                    } else {
                        this.buildingOverlayTotalOptions.allOptionList.removeAll(this.optionList);
                        this.buildingOverlayTotalOptions.allOptionList.set(0, this.mDefaultOptions);
                        this.buildingOverlayTotalOptions.allOptionList.addAll(this.optionList);
                    }
                    IGlOverlayLayer iGlOverlayLayer = this.glOverlayLayerRef.get();
                    if (iGlOverlayLayer != null) {
                        iGlOverlayLayer.updateOption(this.overlayName, this.buildingOverlayTotalOptions);
                    }
                } finally {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        try {
            if (this.mDelegate != null) {
                this.mDelegate.destroy();
            } else {
                IGlOverlayLayer iGlOverlayLayer = this.glOverlayLayerRef.get();
                if (iGlOverlayLayer != null) {
                    iGlOverlayLayer.removeOverlay(this.overlayName);
                }
            }
        } catch (Throwable unused) {
        }
    }

    public List<BuildingOverlayOptions> getCustomOptions() {
        return this.mDelegate != null ? this.mDelegate.b() : this.optionList;
    }

    public BuildingOverlayOptions getDefaultOptions() {
        return this.mDelegate != null ? this.mDelegate.d() : this.mDefaultOptions;
    }

    public String getId() {
        return this.mDelegate != null ? this.mDelegate.getId() : this.overlayName;
    }

    public float getZIndex() {
        if (this.mDelegate != null) {
            return this.mDelegate.getZIndex();
        }
        if (this.buildingOverlayTotalOptions != null) {
            return this.buildingOverlayTotalOptions.zIndex;
        }
        return 0.0f;
    }

    public boolean isVisible() {
        if (this.mDelegate != null) {
            return this.mDelegate.isVisible();
        }
        if (this.buildingOverlayTotalOptions != null) {
            return this.buildingOverlayTotalOptions.isVisible;
        }
        return false;
    }

    public void setCustomOptions(List<BuildingOverlayOptions> list) {
        if (this.mDelegate != null) {
            this.mDelegate.a(list);
        } else {
            if (list == null || list.size() <= 0) {
                return;
            }
            synchronized (this) {
                this.optionList = list;
            }
            a(false);
        }
    }

    public void setDefaultOptions(BuildingOverlayOptions buildingOverlayOptions) {
        if (this.mDelegate != null) {
            this.mDelegate.a(buildingOverlayOptions);
        } else if (buildingOverlayOptions != null) {
            synchronized (this) {
                this.mDefaultOptions = buildingOverlayOptions;
            }
            a(true);
        }
    }

    public void setVisible(boolean z) {
        if (this.mDelegate != null) {
            this.mDelegate.setVisible(z);
        } else if (this.buildingOverlayTotalOptions != null) {
            this.buildingOverlayTotalOptions.isVisible = z;
            a();
        }
    }

    public void setZIndex(float f) {
        if (this.mDelegate != null) {
            this.mDelegate.setZIndex(f);
            return;
        }
        if (this.mDefaultOptions != null) {
            this.mDefaultOptions.setZIndex(f);
        }
        if (this.buildingOverlayTotalOptions != null) {
            this.buildingOverlayTotalOptions.zIndex = f;
            a();
        }
    }
}
