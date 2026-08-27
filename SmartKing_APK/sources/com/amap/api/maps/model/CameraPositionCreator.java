package com.amap.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class CameraPositionCreator implements Parcelable.Creator<CameraPosition> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public CameraPosition createFromParcel(Parcel parcel) {
        float readFloat = parcel.readFloat();
        float readFloat2 = parcel.readFloat();
        float readFloat3 = parcel.readFloat();
        return new CameraPosition(new LatLng(readFloat2, readFloat3), parcel.readFloat(), parcel.readFloat(), readFloat);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public CameraPosition[] newArray(int i) {
        return new CameraPosition[i];
    }
}
