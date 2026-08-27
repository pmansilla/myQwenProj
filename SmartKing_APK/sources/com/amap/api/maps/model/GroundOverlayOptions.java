package com.amap.api.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/* loaded from: classes.dex */
public final class GroundOverlayOptions extends BaseOptions implements Parcelable {
    private static final String CLASSNAME = "GroundOverlayOptions";
    public static final GroundOverlayOptionsCreator CREATOR = new GroundOverlayOptionsCreator();
    public static final float NO_DIMENSION = -1.0f;
    private final double NF_PI;
    private final double R;
    private float anchorU;
    private float anchorV;
    private float bearing;
    private BitmapDescriptor bitmapDescriptor;
    private String bitmapSymbol;
    private float height;
    private boolean isVisible;
    private LatLng latLng;
    private LatLngBounds latlngBounds;
    private final int mVersionCode;
    private LatLng northeast;
    private LatLng southwest;
    private float transparency;
    private final String type;
    private float width;
    private float zIndex;

    public GroundOverlayOptions() {
        this.zIndex = 0.0f;
        this.isVisible = true;
        this.transparency = 0.0f;
        this.anchorU = 0.5f;
        this.anchorV = 0.5f;
        this.NF_PI = 0.01745329251994329d;
        this.R = 6371000.79d;
        this.type = CLASSNAME;
        this.mVersionCode = 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public GroundOverlayOptions(int i, IBinder iBinder, LatLng latLng, float f, float f2, LatLngBounds latLngBounds, float f3, float f4, boolean z, float f5, float f6, float f7) {
        this.zIndex = 0.0f;
        this.isVisible = true;
        this.transparency = 0.0f;
        this.anchorU = 0.5f;
        this.anchorV = 0.5f;
        this.NF_PI = 0.01745329251994329d;
        this.R = 6371000.79d;
        this.type = CLASSNAME;
        this.mVersionCode = i;
        this.bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(null);
        this.latLng = latLng;
        this.width = f;
        this.height = f2;
        this.latlngBounds = latLngBounds;
        this.bearing = f3;
        this.zIndex = f4;
        this.isVisible = z;
        this.transparency = f5;
        this.anchorU = f6;
        this.anchorV = f7;
        this.southwest = latLngBounds.southwest;
        this.northeast = latLngBounds.northeast;
    }

    private GroundOverlayOptions a(LatLng latLng, float f, float f2) {
        this.latLng = latLng;
        this.width = f;
        this.height = f2;
        b();
        return this;
    }

    private void a() {
        if (this.southwest == null || this.northeast == null) {
            return;
        }
        double d = this.southwest.latitude;
        double d2 = 1.0f - this.anchorV;
        double d3 = this.northeast.latitude - this.southwest.latitude;
        Double.isNaN(d2);
        double d4 = d + (d2 * d3);
        double d5 = this.southwest.longitude;
        double d6 = this.anchorU;
        double d7 = this.northeast.longitude - this.southwest.longitude;
        Double.isNaN(d6);
        this.latLng = new LatLng(d4, d5 + (d6 * d7));
        this.width = (float) (Math.cos(this.latLng.latitude * 0.01745329251994329d) * 6371000.79d * (this.northeast.longitude - this.southwest.longitude) * 0.01745329251994329d);
        this.height = (float) ((this.northeast.latitude - this.southwest.latitude) * 6371000.79d * 0.01745329251994329d);
    }

    private void b() {
        if (this.latLng == null) {
            return;
        }
        double d = this.width;
        double cos = Math.cos(this.latLng.latitude * 0.01745329251994329d) * 6371000.79d * 0.01745329251994329d;
        Double.isNaN(d);
        double d2 = d / cos;
        double d3 = this.height;
        Double.isNaN(d3);
        double d4 = d3 / 111194.94043265979d;
        try {
            double d5 = this.latLng.latitude;
            double d6 = 1.0f - this.anchorV;
            Double.isNaN(d6);
            double d7 = d5 - (d6 * d4);
            double d8 = this.latLng.longitude;
            double d9 = this.anchorU;
            Double.isNaN(d9);
            LatLng latLng = new LatLng(d7, d8 - (d9 * d2));
            double d10 = this.latLng.latitude;
            double d11 = this.anchorV;
            Double.isNaN(d11);
            double d12 = d10 + (d11 * d4);
            double d13 = this.latLng.longitude;
            double d14 = 1.0f - this.anchorU;
            Double.isNaN(d14);
            this.latlngBounds = new LatLngBounds(latLng, new LatLng(d12, d13 + (d14 * d2)));
            this.southwest = this.latlngBounds.southwest;
            this.northeast = this.latlngBounds.northeast;
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public GroundOverlayOptions anchor(float f, float f2) {
        this.anchorU = f;
        this.anchorV = f2;
        if (this.latlngBounds != null) {
            a();
        } else if (this.latLng != null) {
            b();
        }
        return this;
    }

    public GroundOverlayOptions bearing(float f) {
        this.bearing = f;
        return this;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public float getAnchorU() {
        return this.anchorU;
    }

    public float getAnchorV() {
        return this.anchorV;
    }

    public float getBearing() {
        return this.bearing;
    }

    public LatLngBounds getBounds() {
        return this.latlngBounds;
    }

    public float getHeight() {
        return this.height;
    }

    public BitmapDescriptor getImage() {
        return this.bitmapDescriptor;
    }

    public LatLng getLocation() {
        return this.latLng;
    }

    public float getTransparency() {
        return this.transparency;
    }

    public float getWidth() {
        return this.width;
    }

    public float getZIndex() {
        return this.zIndex;
    }

    public GroundOverlayOptions image(BitmapDescriptor bitmapDescriptor) {
        this.bitmapDescriptor = bitmapDescriptor;
        if (this.bitmapDescriptor != null) {
            this.bitmapSymbol = this.bitmapDescriptor.getId();
        }
        return this;
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    public GroundOverlayOptions position(LatLng latLng, float f) {
        if (this.latlngBounds != null) {
            Log.w(CLASSNAME, "Position has already been set using positionFromBounds");
        }
        if (latLng == null) {
            Log.w(CLASSNAME, "Location must be specified");
        }
        if (f <= 0.0f) {
            Log.w(CLASSNAME, "Width must be non-negative");
        }
        return a(latLng, f, f);
    }

    public GroundOverlayOptions position(LatLng latLng, float f, float f2) {
        if (this.latlngBounds != null) {
            Log.w(CLASSNAME, "Position has already been set using positionFromBounds");
        }
        if (latLng == null) {
            Log.w(CLASSNAME, "Location must be specified");
        }
        if (f <= 0.0f || f2 <= 0.0f) {
            Log.w(CLASSNAME, "Width and Height must be non-negative");
        }
        return a(latLng, f, f2);
    }

    public GroundOverlayOptions positionFromBounds(LatLngBounds latLngBounds) {
        this.latlngBounds = latLngBounds;
        this.southwest = latLngBounds.southwest;
        this.northeast = latLngBounds.northeast;
        a();
        return this;
    }

    public GroundOverlayOptions transparency(float f) {
        if (f < 0.0f) {
            Log.w(CLASSNAME, "Transparency must be in the range [0..1]");
            f = 0.0f;
        }
        this.transparency = f;
        return this;
    }

    public GroundOverlayOptions visible(boolean z) {
        this.isVisible = z;
        return this;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mVersionCode);
        parcel.writeParcelable(this.bitmapDescriptor, i);
        parcel.writeParcelable(this.latLng, i);
        parcel.writeFloat(this.width);
        parcel.writeFloat(this.height);
        parcel.writeParcelable(this.latlngBounds, i);
        parcel.writeFloat(this.bearing);
        parcel.writeFloat(this.zIndex);
        parcel.writeByte(this.isVisible ? (byte) 1 : (byte) 0);
        parcel.writeFloat(this.transparency);
        parcel.writeFloat(this.anchorU);
        parcel.writeFloat(this.anchorV);
    }

    public GroundOverlayOptions zIndex(float f) {
        this.zIndex = f;
        return this;
    }
}
