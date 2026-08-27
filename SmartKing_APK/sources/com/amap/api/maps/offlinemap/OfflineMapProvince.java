package com.amap.api.maps.offlinemap;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class OfflineMapProvince extends Province {
    public static final Parcelable.Creator<OfflineMapProvince> CREATOR = new Parcelable.Creator<OfflineMapProvince>() { // from class: com.amap.api.maps.offlinemap.OfflineMapProvince.1
        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public OfflineMapProvince createFromParcel(Parcel parcel) {
            return new OfflineMapProvince(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public OfflineMapProvince[] newArray(int i) {
            return new OfflineMapProvince[i];
        }
    };
    private String a;
    private int b;
    private long c;
    private String d;
    private int e;
    private ArrayList<OfflineMapCity> f;

    public OfflineMapProvince() {
        this.b = 6;
        this.e = 0;
    }

    public OfflineMapProvince(Parcel parcel) {
        super(parcel);
        this.b = 6;
        this.e = 0;
        this.a = parcel.readString();
        this.b = parcel.readInt();
        this.c = parcel.readLong();
        this.d = parcel.readString();
        this.e = parcel.readInt();
        this.f = parcel.createTypedArrayList(OfflineMapCity.CREATOR);
    }

    @Override // com.amap.api.maps.offlinemap.Province, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public ArrayList<OfflineMapCity> getCityList() {
        return this.f == null ? new ArrayList<>() : this.f;
    }

    public ArrayList<OfflineMapCity> getDownloadedCityList() {
        ArrayList<OfflineMapCity> arrayList = new ArrayList<>();
        if (this.f == null) {
            return arrayList;
        }
        Iterator<OfflineMapCity> it = this.f.iterator();
        while (it.hasNext()) {
            OfflineMapCity next = it.next();
            if (next.getState() != 6) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    public long getSize() {
        return this.c;
    }

    public int getState() {
        return this.b;
    }

    public String getUrl() {
        return this.a;
    }

    public String getVersion() {
        return this.d;
    }

    public int getcompleteCode() {
        return this.e;
    }

    public void setCityList(ArrayList<OfflineMapCity> arrayList) {
        this.f = arrayList;
    }

    public void setCompleteCode(int i) {
        this.e = i;
    }

    public void setSize(long j) {
        this.c = j;
    }

    public void setState(int i) {
        this.b = i;
    }

    public void setUrl(String str) {
        this.a = str;
    }

    public void setVersion(String str) {
        this.d = str;
    }

    @Override // com.amap.api.maps.offlinemap.Province, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(this.a);
        parcel.writeInt(this.b);
        parcel.writeLong(this.c);
        parcel.writeString(this.d);
        parcel.writeInt(this.e);
        parcel.writeTypedList(this.f);
    }
}
