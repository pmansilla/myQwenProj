package com.amap.api.maps.model;

import android.text.TextUtils;
import com.amap.api.maps.interfaces.IGlOverlayLayer;
import com.autonavi.amap.mapcore.interfaces.IArc;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public final class Arc extends BaseOverlay {
    private WeakReference<IGlOverlayLayer> glOverlayLayerRef;
    private IArc iArcDel;
    private ArcOptions options;
    private String overlayName;

    public Arc(IGlOverlayLayer iGlOverlayLayer, ArcOptions arcOptions) {
        this.glOverlayLayerRef = new WeakReference<>(iGlOverlayLayer);
        this.options = arcOptions;
        this.overlayName = "";
    }

    public Arc(IArc iArc) {
        this.iArcDel = iArc;
    }

    private void a() {
        try {
            IGlOverlayLayer iGlOverlayLayer = this.glOverlayLayerRef.get();
            if (TextUtils.isEmpty(this.overlayName) || iGlOverlayLayer == null) {
                return;
            }
            iGlOverlayLayer.updateOption(this.overlayName, this.options);
        } catch (Throwable unused) {
        }
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Arc)) {
            return false;
        }
        try {
            return this.iArcDel != null ? this.iArcDel.equalsRemote(((Arc) obj).iArcDel) : super.equals(obj) || ((Arc) obj).getId() == getId();
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    public String getId() {
        try {
            return this.iArcDel != null ? this.iArcDel.getId() : this.overlayName;
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public int getStrokeColor() {
        try {
            if (this.iArcDel != null) {
                return this.iArcDel.getStrokeColor();
            }
            if (this.options != null) {
                return this.options.getStrokeColor();
            }
            return 0;
        } catch (Throwable th) {
            th.printStackTrace();
            return 0;
        }
    }

    public float getStrokeWidth() {
        try {
            if (this.iArcDel != null) {
                return this.iArcDel.getStrokeWidth();
            }
            if (this.options != null) {
                return this.options.getStrokeWidth();
            }
            return 0.0f;
        } catch (Throwable th) {
            th.printStackTrace();
            return 0.0f;
        }
    }

    public float getZIndex() {
        try {
            if (this.iArcDel != null) {
                return this.iArcDel.getZIndex();
            }
            if (this.options != null) {
                return this.options.getZIndex();
            }
            return 0.0f;
        } catch (Throwable th) {
            th.printStackTrace();
            return 0.0f;
        }
    }

    public int hashCode() {
        try {
            return this.iArcDel != null ? this.iArcDel.hashCodeRemote() : super.hashCode();
        } catch (Throwable th) {
            th.printStackTrace();
            return 0;
        }
    }

    public boolean isVisible() {
        try {
            if (this.iArcDel != null) {
                return this.iArcDel.isVisible();
            }
            if (this.options != null) {
                return this.options.isVisible();
            }
            return false;
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    public void remove() {
        try {
            if (this.iArcDel != null) {
                this.iArcDel.remove();
            } else {
                IGlOverlayLayer iGlOverlayLayer = this.glOverlayLayerRef.get();
                if (iGlOverlayLayer != null) {
                    iGlOverlayLayer.removeOverlay(this.overlayName);
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setStrokeColor(int i) {
        try {
            if (this.iArcDel != null) {
                this.iArcDel.setStrokeColor(i);
            } else if (this.options != null) {
                this.options.strokeColor(i);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setStrokeWidth(float f) {
        try {
            if (this.iArcDel != null) {
                this.iArcDel.setStrokeWidth(f);
            } else if (this.options != null) {
                this.options.strokeWidth(f);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setVisible(boolean z) {
        try {
            if (this.iArcDel != null) {
                this.iArcDel.setVisible(z);
            } else if (this.options != null) {
                this.options.visible(z);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setZIndex(float f) {
        try {
            if (this.iArcDel != null) {
                this.iArcDel.setZIndex(f);
            } else if (this.options != null) {
                this.options.zIndex(f);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
