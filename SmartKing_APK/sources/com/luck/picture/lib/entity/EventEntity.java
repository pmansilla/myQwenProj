package com.luck.picture.lib.entity;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class EventEntity implements Parcelable {
    public static final Parcelable.Creator<EventEntity> CREATOR = new Parcelable.Creator<EventEntity>() { // from class: com.luck.picture.lib.entity.EventEntity.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public EventEntity createFromParcel(Parcel parcel) {
            return new EventEntity(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public EventEntity[] newArray(int i) {
            return new EventEntity[i];
        }
    };
    public List<LocalMedia> medias;
    public int position;
    public int what;

    public EventEntity() {
        this.medias = new ArrayList();
    }

    public EventEntity(int i) {
        this.medias = new ArrayList();
        this.what = i;
    }

    public EventEntity(int i, int i2) {
        this.medias = new ArrayList();
        this.what = i;
        this.position = i2;
    }

    public EventEntity(int i, List<LocalMedia> list) {
        this.medias = new ArrayList();
        this.what = i;
        this.medias = list;
    }

    public EventEntity(int i, List<LocalMedia> list, int i2) {
        this.medias = new ArrayList();
        this.what = i;
        this.position = i2;
        this.medias = list;
    }

    protected EventEntity(Parcel parcel) {
        this.medias = new ArrayList();
        this.what = parcel.readInt();
        this.position = parcel.readInt();
        this.medias = parcel.createTypedArrayList(LocalMedia.CREATOR);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.what);
        parcel.writeInt(this.position);
        parcel.writeTypedList(this.medias);
    }
}
