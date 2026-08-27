package com.amap.api.maps.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class CircleHoleOptions extends BaseHoleOptions implements Parcelable {
    public static final Parcelable.Creator<CircleHoleOptions> CREATOR = new Parcelable.Creator<CircleHoleOptions>() { // from class: com.amap.api.maps.model.CircleHoleOptions.1
        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public CircleHoleOptions createFromParcel(Parcel parcel) {
            return new CircleHoleOptions(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public CircleHoleOptions[] newArray(int i) {
            return new CircleHoleOptions[i];
        }
    };
    private LatLng point;
    private double radius;

    public CircleHoleOptions() {
        this.point = null;
        this.radius = 0.0d;
        this.isPolygonHoleOptions = false;
    }

    protected CircleHoleOptions(Parcel parcel) {
        this.point = null;
        this.radius = 0.0d;
        Bundle readBundle = parcel.readBundle();
        this.point = new LatLng(readBundle.getDouble("lat"), readBundle.getDouble("lng"));
        this.radius = parcel.readDouble();
    }

    public CircleHoleOptions center(LatLng latLng) {
        this.point = latLng;
        return this;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public LatLng getCenter() {
        return this.point;
    }

    public double getRadius() {
        return this.radius;
    }

    public CircleHoleOptions radius(double d) {
        this.radius = d;
        return this;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        Bundle bundle = new Bundle();
        if (this.point != null) {
            bundle.putDouble("lat", this.point.latitude);
            bundle.putDouble("lng", this.point.longitude);
        }
        parcel.writeBundle(bundle);
        parcel.writeDouble(this.radius);
    }
}
