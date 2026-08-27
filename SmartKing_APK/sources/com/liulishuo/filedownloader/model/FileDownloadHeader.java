package com.liulishuo.filedownloader.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class FileDownloadHeader implements Parcelable {
    public static final Parcelable.Creator<FileDownloadHeader> CREATOR = new Parcelable.Creator<FileDownloadHeader>() { // from class: com.liulishuo.filedownloader.model.FileDownloadHeader.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FileDownloadHeader createFromParcel(Parcel parcel) {
            return new FileDownloadHeader(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FileDownloadHeader[] newArray(int i) {
            return new FileDownloadHeader[i];
        }
    };
    private HashMap<String, List<String>> mHeaderMap;

    public FileDownloadHeader() {
    }

    protected FileDownloadHeader(Parcel parcel) {
        this.mHeaderMap = parcel.readHashMap(String.class.getClassLoader());
    }

    public void add(String str) {
        String[] split = str.split(":");
        add(split[0].trim(), split[1].trim());
    }

    public void add(String str, String str2) {
        if (str == null) {
            throw new NullPointerException("name == null");
        }
        if (str.isEmpty()) {
            throw new IllegalArgumentException("name is empty");
        }
        if (str2 == null) {
            throw new NullPointerException("value == null");
        }
        if (this.mHeaderMap == null) {
            this.mHeaderMap = new HashMap<>();
        }
        List<String> list = this.mHeaderMap.get(str);
        if (list == null) {
            list = new ArrayList<>();
            this.mHeaderMap.put(str, list);
        }
        if (list.contains(str2)) {
            return;
        }
        list.add(str2);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public HashMap<String, List<String>> getHeaders() {
        return this.mHeaderMap;
    }

    public void removeAll(String str) {
        if (this.mHeaderMap == null) {
            return;
        }
        this.mHeaderMap.remove(str);
    }

    public String toString() {
        return this.mHeaderMap.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeMap(this.mHeaderMap);
    }
}
