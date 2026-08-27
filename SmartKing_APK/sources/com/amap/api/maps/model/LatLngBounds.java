package com.amap.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.amap.api.mapcore.util.fr;
import com.litesuits.orm.db.assit.SQLBuilder;

/* loaded from: classes.dex */
public final class LatLngBounds implements Parcelable {
    private static final String CLASSNAME = "LatLngBounds";
    public static final LatLngBoundsCreator CREATOR = new LatLngBoundsCreator();
    private final int mVersionCode;
    public final LatLng northeast;
    public final LatLng southwest;

    /* loaded from: classes.dex */
    public static final class Builder {
        private double mSouth = Double.POSITIVE_INFINITY;
        private double mNorth = Double.NEGATIVE_INFINITY;
        private double mWest = Double.NaN;
        private double mEast = Double.NaN;

        private boolean a(double d) {
            return this.mWest <= this.mEast ? this.mWest <= d && d <= this.mEast : this.mWest <= d || d <= this.mEast;
        }

        public LatLngBounds build() {
            if (Double.isNaN(this.mWest)) {
                Log.w(LatLngBounds.CLASSNAME, "no included points");
                return null;
            }
            if (this.mWest > this.mEast) {
                double d = this.mWest;
                this.mWest = this.mEast;
                this.mEast = d;
            }
            if (this.mSouth > this.mNorth) {
                double d2 = this.mSouth;
                this.mSouth = this.mNorth;
                this.mNorth = d2;
            }
            return new LatLngBounds(new LatLng(this.mSouth, this.mWest, false), new LatLng(this.mNorth, this.mEast, false));
        }

        public Builder include(LatLng latLng) {
            if (latLng == null) {
                return this;
            }
            this.mSouth = Math.min(this.mSouth, latLng.latitude);
            this.mNorth = Math.max(this.mNorth, latLng.latitude);
            double d = latLng.longitude;
            if (Double.isNaN(this.mWest)) {
                this.mWest = d;
                this.mEast = d;
            } else if (!a(d)) {
                if (LatLngBounds.c(this.mWest, d) < LatLngBounds.d(this.mEast, d)) {
                    this.mWest = d;
                } else {
                    this.mEast = d;
                }
            }
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LatLngBounds(int i, LatLng latLng, LatLng latLng2) {
        boolean z;
        try {
        } catch (Throwable th) {
            Log.e(CLASSNAME, "the structure parameters are illegal!");
            th.printStackTrace();
            z = false;
        }
        if (latLng == null) {
            throw new RuntimeRemoteException("null southwest");
        }
        if (latLng2 == null) {
            throw new RuntimeRemoteException("null northeast");
        }
        if (latLng2.latitude >= latLng.latitude) {
            z = true;
            this.mVersionCode = z ? i : 0;
            this.southwest = z ? latLng : null;
            this.northeast = z ? latLng2 : null;
            return;
        }
        throw new RuntimeRemoteException("southern latitude exceeds northern latitude (" + latLng.latitude + " > " + latLng2.latitude + SQLBuilder.PARENTHESES_RIGHT);
    }

    public LatLngBounds(LatLng latLng, LatLng latLng2) {
        this(1, latLng, latLng2);
    }

    private boolean a(double d) {
        return this.southwest.latitude <= d && d <= this.northeast.latitude;
    }

    private boolean a(LatLngBounds latLngBounds) {
        if (latLngBounds == null || latLngBounds.northeast == null || latLngBounds.southwest == null) {
            return false;
        }
        return Math.abs(((latLngBounds.northeast.longitude + latLngBounds.southwest.longitude) - this.northeast.longitude) - this.southwest.longitude) < ((this.northeast.longitude - this.southwest.longitude) + latLngBounds.northeast.longitude) - this.southwest.longitude && Math.abs(((latLngBounds.northeast.latitude + latLngBounds.southwest.latitude) - this.northeast.latitude) - this.southwest.latitude) < ((this.northeast.latitude - this.southwest.latitude) + latLngBounds.northeast.latitude) - latLngBounds.southwest.latitude;
    }

    private boolean b(double d) {
        return this.southwest.longitude <= this.northeast.longitude ? this.southwest.longitude <= d && d <= this.northeast.longitude : this.southwest.longitude <= d || d <= this.northeast.longitude;
    }

    public static Builder builder() {
        return new Builder();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double c(double d, double d2) {
        return ((d - d2) + 360.0d) % 360.0d;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double d(double d, double d2) {
        return ((d2 - d) + 360.0d) % 360.0d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int a() {
        return this.mVersionCode;
    }

    public boolean contains(LatLng latLng) {
        if (latLng == null) {
            return false;
        }
        if (this.northeast != null && this.southwest != null) {
            return a(latLng.latitude) && b(latLng.longitude);
        }
        Log.e(CLASSNAME, "current LatLngBounds is invalid, please check the structure parameters are legal");
        return false;
    }

    public boolean contains(LatLngBounds latLngBounds) {
        return latLngBounds != null && contains(latLngBounds.southwest) && contains(latLngBounds.northeast);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LatLngBounds)) {
            return false;
        }
        LatLngBounds latLngBounds = (LatLngBounds) obj;
        return this.southwest.equals(latLngBounds.southwest) && this.northeast.equals(latLngBounds.northeast);
    }

    public int hashCode() {
        return fr.a(new Object[]{this.southwest, this.northeast});
    }

    public LatLngBounds including(LatLng latLng) {
        double d;
        if (latLng == null) {
            return this;
        }
        if (this.northeast == null || this.southwest == null) {
            Log.e(CLASSNAME, "current LatLngBounds is invalid, please check the structure parameters are legal");
            return this;
        }
        double min = Math.min(this.southwest.latitude, latLng.latitude);
        double max = Math.max(this.northeast.latitude, latLng.latitude);
        double d2 = this.northeast.longitude;
        double d3 = this.southwest.longitude;
        double d4 = latLng.longitude;
        try {
            if (!b(d4)) {
                if (c(d3, d4) >= d(d2, d4)) {
                    d = d4;
                    return new LatLngBounds(new LatLng(min, d3, false), new LatLng(max, d, false));
                }
                d3 = d4;
            }
            return new LatLngBounds(new LatLng(min, d3, false), new LatLng(max, d, false));
        } catch (Throwable th) {
            th.printStackTrace();
            return this;
        }
        d = d2;
    }

    public boolean intersects(LatLngBounds latLngBounds) {
        if (latLngBounds == null) {
            return false;
        }
        if (this.northeast != null && this.southwest != null) {
            return a(latLngBounds) || latLngBounds.a(this);
        }
        Log.e(CLASSNAME, "current LatLngBounds is invalid, please check the structure parameters are legal");
        return false;
    }

    public String toString() {
        return fr.a(fr.a("southwest", this.southwest), fr.a("northeast", this.northeast));
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        LatLngBoundsCreator.a(this, parcel, i);
    }
}
