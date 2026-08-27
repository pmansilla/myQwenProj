package com.amap.api.maps.model;

import android.text.TextUtils;
import com.amap.api.mapcore.util.ic;
import com.amap.api.maps.interfaces.IGlOverlayLayer;
import com.autonavi.amap.mapcore.interfaces.IPolygon;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class Polygon extends BaseOverlay {
    private WeakReference<IGlOverlayLayer> glOverlayLayerRef;
    private PolygonOptions options;
    private IPolygon polygonDelegate;

    public Polygon(IGlOverlayLayer iGlOverlayLayer, PolygonOptions polygonOptions) {
        this.glOverlayLayerRef = new WeakReference<>(iGlOverlayLayer);
        this.options = polygonOptions;
    }

    public Polygon(IPolygon iPolygon) {
        this.polygonDelegate = iPolygon;
    }

    private void a() {
        try {
            IGlOverlayLayer iGlOverlayLayer = this.glOverlayLayerRef.get();
            if (TextUtils.isEmpty(this.overlayName) || iGlOverlayLayer == null) {
                return;
            }
            setOptionPointList(this.options);
            iGlOverlayLayer.processPolygonHoleOption(this.options);
            iGlOverlayLayer.updateOption(this.overlayName, this.options);
        } catch (Throwable unused) {
        }
    }

    public boolean contains(LatLng latLng) {
        try {
            if (this.polygonDelegate != null) {
                return this.polygonDelegate.contains(latLng);
            }
            IGlOverlayLayer iGlOverlayLayer = this.glOverlayLayerRef.get();
            if (iGlOverlayLayer != null) {
                return iGlOverlayLayer.IsPolygonContainsPoint(this.options, latLng);
            }
            return false;
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Polygon)) {
            return false;
        }
        try {
            return this.polygonDelegate != null ? this.polygonDelegate.equalsRemote(((Polygon) obj).polygonDelegate) : super.equals(obj) || ((Polygon) obj).getId() == getId();
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    public int getFillColor() {
        try {
            if (this.polygonDelegate != null) {
                return this.polygonDelegate.getFillColor();
            }
            if (this.options != null) {
                return this.options.getFillColor();
            }
            return 0;
        } catch (Throwable th) {
            th.printStackTrace();
            return 0;
        }
    }

    public List<BaseHoleOptions> getHoleOptions() {
        try {
            if (this.polygonDelegate != null) {
                return this.polygonDelegate.getHoleOptions();
            }
            if (this.options != null) {
                return this.options.getHoleOptions();
            }
            return null;
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public String getId() {
        try {
            return this.polygonDelegate != null ? this.polygonDelegate.getId() : this.overlayName;
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public List<LatLng> getPoints() {
        try {
            if (this.polygonDelegate != null) {
                return this.polygonDelegate.getPoints();
            }
            if (this.options != null) {
                return this.options.getPoints();
            }
            return null;
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public int getStrokeColor() {
        try {
            if (this.polygonDelegate != null) {
                return this.polygonDelegate.getStrokeColor();
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
            if (this.polygonDelegate != null) {
                return this.polygonDelegate.getStrokeWidth();
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
            if (this.polygonDelegate != null) {
                return this.polygonDelegate.getZIndex();
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
            return this.polygonDelegate != null ? this.polygonDelegate.hashCodeRemote() : super.hashCode();
        } catch (Throwable unused) {
            return super.hashCode();
        }
    }

    public boolean isVisible() {
        try {
            if (this.polygonDelegate != null) {
                return this.polygonDelegate.isVisible();
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
            if (this.polygonDelegate != null) {
                this.polygonDelegate.remove();
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

    public void setFillColor(int i) {
        try {
            if (this.polygonDelegate != null) {
                this.polygonDelegate.setFillColor(i);
            } else if (this.options != null) {
                this.options.fillColor(i);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setHoleOptions(List<BaseHoleOptions> list) {
        try {
            if (this.polygonDelegate != null) {
                this.polygonDelegate.setHoleOptions(list);
                return;
            }
            if (list == null) {
                list = new ArrayList<>();
            }
            this.options.setHoleOptions(list);
            a();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    protected void setOptionPointList(Object obj) {
        try {
            Class<?> cls = obj.getClass();
            Field declaredField = cls.getDeclaredField("isPointsUpdated");
            if (declaredField == null) {
                return;
            }
            declaredField.setAccessible(true);
            if (declaredField.getBoolean(obj)) {
                List<LatLng> points = ((PolygonOptions) obj).getPoints();
                double[] dArr = new double[points.size() * 2];
                for (int i = 0; i < points.size(); i++) {
                    int i2 = i * 2;
                    dArr[i2] = points.get(i).latitude;
                    dArr[i2 + 1] = points.get(i).longitude;
                }
                Field declaredField2 = cls.getDeclaredField("pointList");
                if (declaredField2 == null) {
                    return;
                }
                declaredField2.setAccessible(true);
                declaredField2.set(obj, dArr);
            }
        } catch (Throwable th) {
            ic.c(th, "Polygon", "setOptionPointList");
        }
    }

    public void setPoints(List<LatLng> list) {
        try {
            if (this.polygonDelegate != null) {
                this.polygonDelegate.setPoints(list);
            } else {
                this.options.setPoints(list);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setStrokeColor(int i) {
        try {
            if (this.polygonDelegate != null) {
                this.polygonDelegate.setStrokeColor(i);
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
            if (this.polygonDelegate != null) {
                this.polygonDelegate.setStrokeWidth(f);
            } else {
                this.options.strokeWidth(f);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setVisible(boolean z) {
        try {
            if (this.polygonDelegate != null) {
                this.polygonDelegate.setVisible(z);
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
            if (this.polygonDelegate != null) {
                this.polygonDelegate.setZIndex(f);
            } else if (this.options != null) {
                this.options.zIndex(f);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
