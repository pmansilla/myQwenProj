package com.liulishuo.filedownloader.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

/* loaded from: classes.dex */
public class FileDownloadTaskAtom implements Parcelable {
    public static final Parcelable.Creator<FileDownloadTaskAtom> CREATOR = new Parcelable.Creator<FileDownloadTaskAtom>() { // from class: com.liulishuo.filedownloader.model.FileDownloadTaskAtom.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FileDownloadTaskAtom createFromParcel(Parcel parcel) {
            return new FileDownloadTaskAtom(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FileDownloadTaskAtom[] newArray(int i) {
            return new FileDownloadTaskAtom[i];
        }
    };
    private int id;
    private String path;
    private long totalBytes;
    private String url;

    protected FileDownloadTaskAtom(Parcel parcel) {
        this.url = parcel.readString();
        this.path = parcel.readString();
        this.totalBytes = parcel.readLong();
    }

    public FileDownloadTaskAtom(String str, String str2, long j) {
        setUrl(str);
        setPath(str2);
        setTotalBytes(j);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getId() {
        if (this.id != 0) {
            return this.id;
        }
        int generateId = FileDownloadUtils.generateId(getUrl(), getPath());
        this.id = generateId;
        return generateId;
    }

    public String getPath() {
        return this.path;
    }

    public long getTotalBytes() {
        return this.totalBytes;
    }

    public String getUrl() {
        return this.url;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public void setTotalBytes(long j) {
        this.totalBytes = j;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.url);
        parcel.writeString(this.path);
        parcel.writeLong(this.totalBytes);
    }
}
