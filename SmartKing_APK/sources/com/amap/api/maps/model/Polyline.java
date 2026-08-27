package com.amap.api.maps.model;

import android.text.TextUtils;
import com.amap.api.mapcore.util.ic;
import com.amap.api.maps.interfaces.IGlOverlayLayer;
import com.autonavi.amap.mapcore.interfaces.IPolyline;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class Polyline extends BaseOverlay {
    private WeakReference<IGlOverlayLayer> glOverlayLayerRef;
    private PolylineOptions options;
    private IPolyline polylineDelegate;

    public Polyline(IGlOverlayLayer iGlOverlayLayer, PolylineOptions polylineOptions) {
        this.glOverlayLayerRef = new WeakReference<>(iGlOverlayLayer);
        this.options = polylineOptions;
    }

    public Polyline(IGlOverlayLayer iGlOverlayLayer, PolylineOptions polylineOptions, String str) {
        this.glOverlayLayerRef = new WeakReference<>(iGlOverlayLayer);
        this.options = polylineOptions;
        this.overlayName = str;
    }

    public Polyline(IPolyline iPolyline) {
        this.polylineDelegate = iPolyline;
    }

    private void a() {
        try {
            synchronized (this) {
                IGlOverlayLayer iGlOverlayLayer = this.glOverlayLayerRef.get();
                if (!TextUtils.isEmpty(this.overlayName) && iGlOverlayLayer != null) {
                    setOptionPointList(this.options);
                    if (iGlOverlayLayer != null) {
                        iGlOverlayLayer.updateOption(this.overlayName, this.options);
                    }
                }
            }
        } catch (Throwable unused) {
        }
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Polyline)) {
            return false;
        }
        try {
            return this.polylineDelegate != null ? this.polylineDelegate.equalsRemote(((Polyline) obj).polylineDelegate) : super.equals(obj) || ((Polyline) obj).getId() == getId();
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    public int getColor() {
        try {
            if (this.polylineDelegate != null) {
                return this.polylineDelegate.getColor();
            }
            if (this.options != null) {
                return this.options.getColor();
            }
            return 0;
        } catch (Throwable th) {
            th.printStackTrace();
            return 0;
        }
    }

    public String getId() {
        try {
            return this.polylineDelegate != null ? this.polylineDelegate.getId() : this.overlayName;
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public LatLng getNearestLatLng(LatLng latLng) {
        if (this.polylineDelegate != null) {
            return this.polylineDelegate.getNearestLatLng(latLng);
        }
        IGlOverlayLayer iGlOverlayLayer = this.glOverlayLayerRef.get();
        if (iGlOverlayLayer != null) {
            return iGlOverlayLayer.getNearestLatLng(this.options, latLng);
        }
        return null;
    }

    public PolylineOptions getOptions() {
        return this.polylineDelegate != null ? this.polylineDelegate.getOptions() : this.options;
    }

    public List<LatLng> getPoints() {
        try {
            if (this.polylineDelegate != null) {
                return this.polylineDelegate.getPoints();
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

    public float getShownRatio() {
        try {
            if (this.polylineDelegate != null) {
                return this.polylineDelegate.getShownRatio();
            }
            if (this.options != null) {
                return this.options.getShownRatio();
            }
            return -1.0f;
        } catch (Throwable th) {
            th.printStackTrace();
            return -1.0f;
        }
    }

    public float getWidth() {
        try {
            if (this.polylineDelegate != null) {
                return this.polylineDelegate.getWidth();
            }
            if (this.options != null) {
                return this.options.getWidth();
            }
            return 0.0f;
        } catch (Throwable th) {
            th.printStackTrace();
            return 0.0f;
        }
    }

    public float getZIndex() {
        try {
            if (this.polylineDelegate != null) {
                return this.polylineDelegate.getZIndex();
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
            return this.polylineDelegate != null ? this.polylineDelegate.hashCodeRemote() : super.hashCode();
        } catch (Throwable th) {
            th.printStackTrace();
            return 0;
        }
    }

    public boolean isDottedLine() {
        if (this.polylineDelegate != null) {
            return this.polylineDelegate.isDottedLine();
        }
        if (this.options != null) {
            return this.options.isDottedLine();
        }
        return false;
    }

    public boolean isGeodesic() {
        if (this.polylineDelegate.isGeodesic()) {
            return true;
        }
        if (this.options != null) {
            return this.options.isGeodesic();
        }
        return false;
    }

    public boolean isVisible() {
        try {
            if (this.polylineDelegate != null) {
                return this.polylineDelegate.isVisible();
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
            if (this.polylineDelegate != null) {
                this.polylineDelegate.remove();
                return;
            }
            IGlOverlayLayer iGlOverlayLayer = this.glOverlayLayerRef.get();
            if (iGlOverlayLayer != null) {
                iGlOverlayLayer.removeOverlay(this.overlayName);
            }
            BitmapDescriptor customTexture = this.options.getCustomTexture();
            if (customTexture != null) {
                customTexture.recycle();
            }
            if (this.options.getCustomTextureList() != null) {
                Iterator<BitmapDescriptor> it = this.options.getCustomTextureList().iterator();
                while (it.hasNext()) {
                    it.next().recycle();
                }
                this.options.getCustomTextureList().clear();
            }
            this.options = null;
            this.overlayName = null;
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setAboveMaskLayer(boolean z) {
        if (this.polylineDelegate != null) {
            this.polylineDelegate.setAboveMaskLayer(z);
        } else if (this.options != null) {
            this.options.aboveMaskLayer(z);
            a();
        }
    }

    public void setColor(int i) {
        try {
            if (this.polylineDelegate != null) {
                this.polylineDelegate.setColor(i);
            } else if (this.options != null) {
                this.options.color(i);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setCustemTextureIndex(List<Integer> list) {
        if (this.polylineDelegate != null) {
            this.polylineDelegate.setCustemTextureIndex(list);
            return;
        }
        synchronized (this) {
            if (this.options != null) {
                this.options.setCustomTextureIndex(list);
                a();
            }
        }
    }

    public void setCustomTexture(BitmapDescriptor bitmapDescriptor) {
        if (this.polylineDelegate != null) {
            this.polylineDelegate.setCustomTexture(bitmapDescriptor);
        } else if (this.options != null) {
            this.options.setCustomTexture(bitmapDescriptor);
            a();
        }
    }

    public void setCustomTextureList(List<BitmapDescriptor> list) {
        try {
            if (this.polylineDelegate != null) {
                this.polylineDelegate.setCustomTextureList(list);
            } else {
                this.options.setCustomTextureList(list);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setDottedLine(boolean z) {
        if (this.polylineDelegate != null) {
            this.polylineDelegate.setDottedLine(z);
        } else if (this.options != null) {
            this.options.setDottedLine(z);
            a();
        }
    }

    public void setGeodesic(boolean z) {
        try {
            if (this.polylineDelegate != null) {
                if (this.polylineDelegate.isGeodesic() != z) {
                    List<LatLng> points = getPoints();
                    this.polylineDelegate.setGeodesic(z);
                    setPoints(points);
                }
            } else if (this.options != null) {
                this.options.geodesic(z);
                a();
            }
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
                List<LatLng> points = ((PolylineOptions) obj).getPoints();
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
            ic.c(th, "Polyline", "setOptionPointList");
        }
    }

    public void setOptions(PolylineOptions polylineOptions) {
        if (this.polylineDelegate != null) {
            this.polylineDelegate.setOptions(polylineOptions);
        } else {
            this.options = polylineOptions;
            a();
        }
    }

    public void setPoints(List<LatLng> list) {
        try {
            if (this.polylineDelegate != null) {
                this.polylineDelegate.setPoints(list);
                return;
            }
            synchronized (this) {
                if (this.options != null) {
                    this.options.setPoints(list);
                    a();
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setShownRange(float f, float f2) {
        try {
            if (this.polylineDelegate != null) {
                this.polylineDelegate.setShowRange(f, f2);
            } else if (this.options != null) {
                this.options.setShownRange(f, f2);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setShownRatio(float f) {
        try {
            if (this.polylineDelegate != null) {
                this.polylineDelegate.setShownRatio(f);
            } else if (this.options != null) {
                this.options.setShownRatio(f);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setTransparency(float f) {
        if (this.polylineDelegate != null) {
            this.polylineDelegate.setTransparency(f);
        } else if (this.options != null) {
            this.options.transparency(f);
            a();
        }
    }

    public void setVisible(boolean z) {
        try {
            if (this.polylineDelegate != null) {
                this.polylineDelegate.setVisible(z);
            } else if (this.options != null) {
                this.options.visible(z);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setWidth(float f) {
        try {
            if (this.polylineDelegate != null) {
                this.polylineDelegate.setWidth(f);
            } else if (this.options != null) {
                this.options.width(f);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setZIndex(float f) {
        try {
            if (this.polylineDelegate != null) {
                this.polylineDelegate.setZIndex(f);
            } else if (this.options != null) {
                this.options.zIndex(f);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
