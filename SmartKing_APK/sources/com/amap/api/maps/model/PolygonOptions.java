package com.amap.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public final class PolygonOptions extends BaseOptions implements Parcelable {
    public static final PolygonOptionsCreator CREATOR = new PolygonOptionsCreator();
    String a;
    private double[] pointList;
    private float strokeWidth = 10.0f;
    private int strokeColor = -16777216;
    private int fillColor = -16777216;
    private float zIndex = 0.0f;
    private boolean isVisible = true;
    private final String type = "PolygonOptions";
    private boolean isPointsUpdated = false;
    private boolean isHoleOptionsUpdated = false;
    private final List<LatLng> points = new ArrayList();
    private List<BaseHoleOptions> holeOptions = new ArrayList();

    public PolygonOptions add(LatLng latLng) {
        try {
            this.points.add(latLng);
            this.isPointsUpdated = true;
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return this;
    }

    public PolygonOptions add(LatLng... latLngArr) {
        try {
            this.points.addAll(Arrays.asList(latLngArr));
            this.isPointsUpdated = true;
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return this;
    }

    public PolygonOptions addAll(Iterable<LatLng> iterable) {
        try {
            Iterator<LatLng> it = iterable.iterator();
            while (it.hasNext()) {
                this.points.add(it.next());
            }
            this.isPointsUpdated = true;
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return this;
    }

    public PolygonOptions addHoles(Iterable<BaseHoleOptions> iterable) {
        try {
            Iterator<BaseHoleOptions> it = iterable.iterator();
            while (it.hasNext()) {
                this.holeOptions.add(it.next());
            }
            this.isHoleOptionsUpdated = true;
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return this;
    }

    public PolygonOptions addHoles(BaseHoleOptions... baseHoleOptionsArr) {
        try {
            this.holeOptions.addAll(Arrays.asList(baseHoleOptionsArr));
            this.isHoleOptionsUpdated = true;
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return this;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public PolygonOptions fillColor(int i) {
        this.fillColor = i;
        return this;
    }

    public int getFillColor() {
        return this.fillColor;
    }

    public List<BaseHoleOptions> getHoleOptions() {
        return this.holeOptions;
    }

    public List<LatLng> getPoints() {
        return this.points;
    }

    public int getStrokeColor() {
        return this.strokeColor;
    }

    public float getStrokeWidth() {
        return this.strokeWidth;
    }

    public float getZIndex() {
        return this.zIndex;
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    public void setHoleOptions(List<BaseHoleOptions> list) {
        try {
            this.holeOptions.clear();
            this.holeOptions.addAll(list);
            this.isHoleOptionsUpdated = true;
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setPoints(List<LatLng> list) {
        try {
            this.points.clear();
            this.points.addAll(list);
            this.isPointsUpdated = true;
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public PolygonOptions strokeColor(int i) {
        this.strokeColor = i;
        return this;
    }

    public PolygonOptions strokeWidth(float f) {
        this.strokeWidth = f;
        return this;
    }

    public PolygonOptions visible(boolean z) {
        this.isVisible = z;
        return this;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(this.points);
        parcel.writeFloat(this.strokeWidth);
        parcel.writeInt(this.strokeColor);
        parcel.writeInt(this.fillColor);
        parcel.writeFloat(this.zIndex);
        parcel.writeByte(this.isVisible ? (byte) 1 : (byte) 0);
        parcel.writeString(this.a);
        parcel.writeList(this.holeOptions);
    }

    public PolygonOptions zIndex(float f) {
        this.zIndex = f;
        return this;
    }
}
