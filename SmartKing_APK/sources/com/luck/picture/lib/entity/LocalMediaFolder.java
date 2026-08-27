package com.luck.picture.lib.entity;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class LocalMediaFolder implements Parcelable {
    public static final Parcelable.Creator<LocalMediaFolder> CREATOR = new Parcelable.Creator<LocalMediaFolder>() { // from class: com.luck.picture.lib.entity.LocalMediaFolder.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LocalMediaFolder createFromParcel(Parcel parcel) {
            return new LocalMediaFolder(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LocalMediaFolder[] newArray(int i) {
            return new LocalMediaFolder[i];
        }
    };
    private int checkedNum;
    private String firstImagePath;
    private int imageNum;
    private List<LocalMedia> images;
    private boolean isChecked;
    private String name;
    private String path;

    public LocalMediaFolder() {
        this.images = new ArrayList();
    }

    protected LocalMediaFolder(Parcel parcel) {
        this.images = new ArrayList();
        this.name = parcel.readString();
        this.path = parcel.readString();
        this.firstImagePath = parcel.readString();
        this.imageNum = parcel.readInt();
        this.checkedNum = parcel.readInt();
        this.isChecked = parcel.readByte() != 0;
        this.images = parcel.createTypedArrayList(LocalMedia.CREATOR);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getCheckedNum() {
        return this.checkedNum;
    }

    public String getFirstImagePath() {
        return this.firstImagePath;
    }

    public int getImageNum() {
        return this.imageNum;
    }

    public List<LocalMedia> getImages() {
        if (this.images == null) {
            this.images = new ArrayList();
        }
        return this.images;
    }

    public String getName() {
        return this.name;
    }

    public String getPath() {
        return this.path;
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void setChecked(boolean z) {
        this.isChecked = z;
    }

    public void setCheckedNum(int i) {
        this.checkedNum = i;
    }

    public void setFirstImagePath(String str) {
        this.firstImagePath = str;
    }

    public void setImageNum(int i) {
        this.imageNum = i;
    }

    public void setImages(List<LocalMedia> list) {
        this.images = list;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setPath(String str) {
        this.path = str;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeString(this.path);
        parcel.writeString(this.firstImagePath);
        parcel.writeInt(this.imageNum);
        parcel.writeInt(this.checkedNum);
        parcel.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
        parcel.writeTypedList(this.images);
    }
}
