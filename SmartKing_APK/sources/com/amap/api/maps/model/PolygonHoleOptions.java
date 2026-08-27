package com.amap.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class PolygonHoleOptions extends BaseHoleOptions implements Parcelable {
    public static final Parcelable.Creator<PolygonHoleOptions> CREATOR = new Parcelable.Creator<PolygonHoleOptions>() { // from class: com.amap.api.maps.model.PolygonHoleOptions.1
        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public PolygonHoleOptions createFromParcel(Parcel parcel) {
            return new PolygonHoleOptions(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public PolygonHoleOptions[] newArray(int i) {
            return new PolygonHoleOptions[i];
        }
    };
    private final List<LatLng> points;

    public PolygonHoleOptions() {
        this.points = new ArrayList();
        this.isPolygonHoleOptions = true;
    }

    protected PolygonHoleOptions(Parcel parcel) {
        this.points = parcel.createTypedArrayList(LatLng.CREATOR);
        this.isPolygonHoleOptions = true;
    }

    public PolygonHoleOptions addAll(Iterable<LatLng> iterable) {
        try {
            Iterator<LatLng> it = iterable.iterator();
            while (it.hasNext()) {
                this.points.add(it.next());
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return this;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public List<LatLng> getPoints() {
        return this.points;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(this.points);
    }
}
