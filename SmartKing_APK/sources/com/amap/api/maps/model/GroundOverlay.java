package com.amap.api.maps.model;

import android.text.TextUtils;
import com.amap.api.maps.interfaces.IGlOverlayLayer;
import com.autonavi.amap.mapcore.interfaces.IGroundOverlay;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public final class GroundOverlay extends BaseOverlay {
    private WeakReference<IGlOverlayLayer> glOverlayLayerRef;
    private float high;
    private IGroundOverlay iGroundOverlayDelegate;
    private GroundOverlayOptions options;
    private String overlayName;
    private LatLng point;
    private float width;

    public GroundOverlay(IGlOverlayLayer iGlOverlayLayer, GroundOverlayOptions groundOverlayOptions) {
        this.glOverlayLayerRef = new WeakReference<>(iGlOverlayLayer);
        this.options = groundOverlayOptions;
        this.overlayName = "";
    }

    public GroundOverlay(IGroundOverlay iGroundOverlay) {
        this.iGroundOverlayDelegate = iGroundOverlay;
    }

    private void a() {
        IGlOverlayLayer iGlOverlayLayer = this.glOverlayLayerRef.get();
        if (TextUtils.isEmpty(this.overlayName) || iGlOverlayLayer == null) {
            return;
        }
        iGlOverlayLayer.updateOption(this.overlayName, this.options);
    }

    public void destroy() {
        try {
            if (this.iGroundOverlayDelegate != null) {
                this.iGroundOverlayDelegate.destroy();
            } else {
                IGlOverlayLayer iGlOverlayLayer = this.glOverlayLayerRef.get();
                if (iGlOverlayLayer != null) {
                    iGlOverlayLayer.removeOverlay(this.overlayName);
                }
            }
        } catch (Throwable unused) {
        }
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof GroundOverlay)) {
            return false;
        }
        try {
            return this.iGroundOverlayDelegate != null ? this.iGroundOverlayDelegate.equalsRemote(((GroundOverlay) obj).iGroundOverlayDelegate) : super.equals(obj) || ((GroundOverlay) obj).getId() == getId();
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    public float getBearing() {
        try {
            if (this.iGroundOverlayDelegate != null) {
                return this.iGroundOverlayDelegate.getBearing();
            }
            if (this.options != null) {
                return this.options.getBearing();
            }
            return 0.0f;
        } catch (Throwable th) {
            th.printStackTrace();
            return 0.0f;
        }
    }

    public LatLngBounds getBounds() {
        try {
            if (this.iGroundOverlayDelegate != null) {
                return this.iGroundOverlayDelegate.getBounds();
            }
            if (this.options != null) {
                return this.options.getBounds();
            }
            return null;
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public float getHeight() {
        try {
            if (this.iGroundOverlayDelegate != null) {
                return this.iGroundOverlayDelegate.getHeight();
            }
            if (this.options != null) {
                return this.options.getHeight();
            }
            return 0.0f;
        } catch (Throwable th) {
            th.printStackTrace();
            return 0.0f;
        }
    }

    public String getId() {
        try {
            return this.iGroundOverlayDelegate != null ? this.iGroundOverlayDelegate.getId() : this.overlayName;
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public LatLng getPosition() {
        try {
            if (this.iGroundOverlayDelegate != null) {
                return this.iGroundOverlayDelegate.getPosition();
            }
            if (this.options != null) {
                return this.options.getLocation();
            }
            return null;
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public float getTransparency() {
        try {
            if (this.iGroundOverlayDelegate != null) {
                return this.iGroundOverlayDelegate.getTransparency();
            }
            if (this.options != null) {
                return this.options.getTransparency();
            }
            return 0.0f;
        } catch (Throwable th) {
            th.printStackTrace();
            return 0.0f;
        }
    }

    public float getWidth() {
        try {
            if (this.iGroundOverlayDelegate != null) {
                return this.iGroundOverlayDelegate.getWidth();
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
            if (this.iGroundOverlayDelegate != null) {
                return this.iGroundOverlayDelegate.getZIndex();
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
        return this.iGroundOverlayDelegate != null ? this.iGroundOverlayDelegate.hashCode() : super.hashCode();
    }

    public boolean isVisible() {
        try {
            if (this.iGroundOverlayDelegate != null) {
                return this.iGroundOverlayDelegate.isVisible();
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
            if (this.iGroundOverlayDelegate != null) {
                this.iGroundOverlayDelegate.remove();
                return;
            }
            IGlOverlayLayer iGlOverlayLayer = this.glOverlayLayerRef.get();
            if (iGlOverlayLayer != null) {
                iGlOverlayLayer.removeOverlay(this.overlayName);
            }
            if (this.options == null || this.options.getImage() == null) {
                return;
            }
            this.options.getImage().recycle();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setBearing(float f) {
        try {
            if (this.iGroundOverlayDelegate != null) {
                this.iGroundOverlayDelegate.setBearing(f);
            } else if (this.options != null) {
                this.options.bearing(f);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setDimensions(float f) {
        try {
            if (this.iGroundOverlayDelegate != null) {
                this.iGroundOverlayDelegate.setDimensions(f);
                return;
            }
            if (this.options != null) {
                LatLng location = this.point != null ? this.point : this.options.getLocation();
                if (location == null) {
                    this.width = f;
                } else {
                    this.options.position(location, f);
                    a();
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setDimensions(float f, float f2) {
        try {
            if (this.iGroundOverlayDelegate != null) {
                this.iGroundOverlayDelegate.setDimensions(f, f2);
                return;
            }
            if (this.options != null) {
                if ((this.point != null ? this.point : this.options.getLocation()) == null) {
                    this.width = f;
                    this.high = f2;
                } else {
                    this.options.position(this.options.getLocation(), f, f2);
                    a();
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setImage(BitmapDescriptor bitmapDescriptor) {
        try {
            if (this.iGroundOverlayDelegate != null) {
                this.iGroundOverlayDelegate.setImage(bitmapDescriptor);
            } else if (this.options != null) {
                this.options.image(bitmapDescriptor);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setPosition(LatLng latLng) {
        try {
            if (this.iGroundOverlayDelegate != null) {
                this.iGroundOverlayDelegate.setPosition(latLng);
                return;
            }
            if (this.options == null || latLng == null) {
                return;
            }
            float width = this.width > 0.0f ? this.width : this.options.getWidth();
            float height = this.high > 0.0f ? this.high : this.options.getHeight();
            if (width == 0.0f) {
                this.point = latLng;
                return;
            }
            if (height == 0.0f) {
                this.options.position(latLng, width);
                a();
            } else if (height > 0.0f) {
                this.options.position(latLng, width, height);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setPositionFromBounds(LatLngBounds latLngBounds) {
        try {
            if (this.iGroundOverlayDelegate != null) {
                this.iGroundOverlayDelegate.setPositionFromBounds(latLngBounds);
            } else if (this.options != null && latLngBounds != null) {
                this.options.positionFromBounds(latLngBounds);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setTransparency(float f) {
        try {
            if (this.iGroundOverlayDelegate != null) {
                this.iGroundOverlayDelegate.setTransparency(f);
            } else if (this.options != null) {
                this.options.transparency(f);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setVisible(boolean z) {
        try {
            if (this.iGroundOverlayDelegate != null) {
                this.iGroundOverlayDelegate.setVisible(z);
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
            if (this.iGroundOverlayDelegate != null) {
                this.iGroundOverlayDelegate.setZIndex(f);
            } else if (this.options != null) {
                this.options.zIndex(f);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
