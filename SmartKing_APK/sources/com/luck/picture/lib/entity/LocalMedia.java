package com.luck.picture.lib.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/* loaded from: classes.dex */
public class LocalMedia implements Parcelable {
    public static final Parcelable.Creator<LocalMedia> CREATOR = new Parcelable.Creator<LocalMedia>() { // from class: com.luck.picture.lib.entity.LocalMedia.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LocalMedia createFromParcel(Parcel parcel) {
            return new LocalMedia(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LocalMedia[] newArray(int i) {
            return new LocalMedia[i];
        }
    };
    private String compressPath;
    private boolean compressed;
    private String cutPath;
    private long duration;
    private int height;
    private boolean isChecked;
    private boolean isCut;
    private int mimeType;
    private int num;
    private String path;
    private String pictureType;
    public int position;
    private int width;

    public LocalMedia() {
    }

    protected LocalMedia(Parcel parcel) {
        this.path = parcel.readString();
        this.compressPath = parcel.readString();
        this.cutPath = parcel.readString();
        this.duration = parcel.readLong();
        this.isChecked = parcel.readByte() != 0;
        this.isCut = parcel.readByte() != 0;
        this.position = parcel.readInt();
        this.num = parcel.readInt();
        this.mimeType = parcel.readInt();
        this.pictureType = parcel.readString();
        this.compressed = parcel.readByte() != 0;
        this.width = parcel.readInt();
        this.height = parcel.readInt();
    }

    public LocalMedia(String str, long j, int i, String str2) {
        this.path = str;
        this.duration = j;
        this.mimeType = i;
        this.pictureType = str2;
    }

    public LocalMedia(String str, long j, int i, String str2, int i2, int i3) {
        this.path = str;
        this.duration = j;
        this.mimeType = i;
        this.pictureType = str2;
        this.width = i2;
        this.height = i3;
    }

    public LocalMedia(String str, long j, boolean z, int i, int i2, int i3) {
        this.path = str;
        this.duration = j;
        this.isChecked = z;
        this.position = i;
        this.num = i2;
        this.mimeType = i3;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getCompressPath() {
        return this.compressPath;
    }

    public String getCutPath() {
        return this.cutPath;
    }

    public long getDuration() {
        return this.duration;
    }

    public int getHeight() {
        return this.height;
    }

    public int getMimeType() {
        return this.mimeType;
    }

    public int getNum() {
        return this.num;
    }

    public String getPath() {
        return this.path;
    }

    public String getPictureType() {
        if (TextUtils.isEmpty(this.pictureType)) {
            this.pictureType = "image/jpeg";
        }
        return this.pictureType;
    }

    public int getPosition() {
        return this.position;
    }

    public int getWidth() {
        return this.width;
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public boolean isCompressed() {
        return this.compressed;
    }

    public boolean isCut() {
        return this.isCut;
    }

    public void setChecked(boolean z) {
        this.isChecked = z;
    }

    public void setCompressPath(String str) {
        this.compressPath = str;
    }

    public void setCompressed(boolean z) {
        this.compressed = z;
    }

    public void setCut(boolean z) {
        this.isCut = z;
    }

    public void setCutPath(String str) {
        this.cutPath = str;
    }

    public void setDuration(long j) {
        this.duration = j;
    }

    public void setHeight(int i) {
        this.height = i;
    }

    public void setMimeType(int i) {
        this.mimeType = i;
    }

    public void setNum(int i) {
        this.num = i;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public void setPictureType(String str) {
        this.pictureType = str;
    }

    public void setPosition(int i) {
        this.position = i;
    }

    public void setWidth(int i) {
        this.width = i;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.path);
        parcel.writeString(this.compressPath);
        parcel.writeString(this.cutPath);
        parcel.writeLong(this.duration);
        parcel.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.isCut ? (byte) 1 : (byte) 0);
        parcel.writeInt(this.position);
        parcel.writeInt(this.num);
        parcel.writeInt(this.mimeType);
        parcel.writeString(this.pictureType);
        parcel.writeByte(this.compressed ? (byte) 1 : (byte) 0);
        parcel.writeInt(this.width);
        parcel.writeInt(this.height);
    }
}
