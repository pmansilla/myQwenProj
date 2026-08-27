package com.liulishuo.filedownloader.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: classes.dex */
public class FileDownloadModel implements Parcelable {
    public static final String CONNECTION_COUNT = "connectionCount";
    public static final Parcelable.Creator<FileDownloadModel> CREATOR = new Parcelable.Creator<FileDownloadModel>() { // from class: com.liulishuo.filedownloader.model.FileDownloadModel.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FileDownloadModel createFromParcel(Parcel parcel) {
            return new FileDownloadModel(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FileDownloadModel[] newArray(int i) {
            return new FileDownloadModel[i];
        }
    };
    public static final int DEFAULT_CALLBACK_PROGRESS_TIMES = 100;
    public static final String ERR_MSG = "errMsg";
    public static final String ETAG = "etag";
    public static final String FILENAME = "filename";
    public static final String ID = "_id";
    public static final String PATH = "path";
    public static final String PATH_AS_DIRECTORY = "pathAsDirectory";
    public static final String SOFAR = "sofar";
    public static final String STATUS = "status";
    public static final String TOTAL = "total";
    public static final int TOTAL_VALUE_IN_CHUNKED_RESOURCE = -1;
    public static final String URL = "url";
    private int connectionCount;
    private String eTag;
    private String errMsg;
    private String filename;
    private int id;
    private boolean isLargeFile;
    private String path;
    private boolean pathAsDirectory;
    private final AtomicLong soFar;
    private final AtomicInteger status;
    private long total;
    private String url;

    public FileDownloadModel() {
        this.soFar = new AtomicLong();
        this.status = new AtomicInteger();
    }

    protected FileDownloadModel(Parcel parcel) {
        this.id = parcel.readInt();
        this.url = parcel.readString();
        this.path = parcel.readString();
        this.pathAsDirectory = parcel.readByte() != 0;
        this.filename = parcel.readString();
        this.status = new AtomicInteger(parcel.readByte());
        this.soFar = new AtomicLong(parcel.readLong());
        this.total = parcel.readLong();
        this.errMsg = parcel.readString();
        this.eTag = parcel.readString();
        this.connectionCount = parcel.readInt();
        this.isLargeFile = parcel.readByte() != 0;
    }

    public void deleteTargetFile() {
        String targetFilePath = getTargetFilePath();
        if (targetFilePath != null) {
            File file = new File(targetFilePath);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public void deleteTaskFiles() {
        deleteTempFile();
        deleteTargetFile();
    }

    public void deleteTempFile() {
        String tempFilePath = getTempFilePath();
        if (tempFilePath != null) {
            File file = new File(tempFilePath);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getConnectionCount() {
        return this.connectionCount;
    }

    public String getETag() {
        return this.eTag;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public String getFilename() {
        return this.filename;
    }

    public int getId() {
        return this.id;
    }

    public String getPath() {
        return this.path;
    }

    public long getSoFar() {
        return this.soFar.get();
    }

    public byte getStatus() {
        return (byte) this.status.get();
    }

    public String getTargetFilePath() {
        return FileDownloadUtils.getTargetFilePath(getPath(), isPathAsDirectory(), getFilename());
    }

    public String getTempFilePath() {
        if (getTargetFilePath() == null) {
            return null;
        }
        return FileDownloadUtils.getTempPath(getTargetFilePath());
    }

    public long getTotal() {
        return this.total;
    }

    public String getUrl() {
        return this.url;
    }

    public void increaseSoFar(long j) {
        this.soFar.addAndGet(j);
    }

    public boolean isChunked() {
        return this.total == -1;
    }

    public boolean isLargeFile() {
        return this.isLargeFile;
    }

    public boolean isPathAsDirectory() {
        return this.pathAsDirectory;
    }

    public void resetConnectionCount() {
        this.connectionCount = 1;
    }

    public void setConnectionCount(int i) {
        this.connectionCount = i;
    }

    public void setETag(String str) {
        this.eTag = str;
    }

    public void setErrMsg(String str) {
        this.errMsg = str;
    }

    public void setFilename(String str) {
        this.filename = str;
    }

    public void setId(int i) {
        this.id = i;
    }

    public void setPath(String str, boolean z) {
        this.path = str;
        this.pathAsDirectory = z;
    }

    public void setSoFar(long j) {
        this.soFar.set(j);
    }

    public void setStatus(byte b) {
        this.status.set(b);
    }

    public void setTotal(long j) {
        this.isLargeFile = j > 2147483647L;
        this.total = j;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, Integer.valueOf(getId()));
        contentValues.put(URL, getUrl());
        contentValues.put(PATH, getPath());
        contentValues.put("status", Byte.valueOf(getStatus()));
        contentValues.put(SOFAR, Long.valueOf(getSoFar()));
        contentValues.put(TOTAL, Long.valueOf(getTotal()));
        contentValues.put(ERR_MSG, getErrMsg());
        contentValues.put(ETAG, getETag());
        contentValues.put(CONNECTION_COUNT, Integer.valueOf(getConnectionCount()));
        contentValues.put(PATH_AS_DIRECTORY, Boolean.valueOf(isPathAsDirectory()));
        if (isPathAsDirectory() && getFilename() != null) {
            contentValues.put(FILENAME, getFilename());
        }
        return contentValues;
    }

    public String toString() {
        return FileDownloadUtils.formatString("id[%d], url[%s], path[%s], status[%d], sofar[%s], total[%d], etag[%s], %s", Integer.valueOf(this.id), this.url, this.path, Integer.valueOf(this.status.get()), this.soFar, Long.valueOf(this.total), this.eTag, super.toString());
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.url);
        parcel.writeString(this.path);
        parcel.writeByte(this.pathAsDirectory ? (byte) 1 : (byte) 0);
        parcel.writeString(this.filename);
        parcel.writeByte((byte) this.status.get());
        parcel.writeLong(this.soFar.get());
        parcel.writeLong(this.total);
        parcel.writeString(this.errMsg);
        parcel.writeString(this.eTag);
        parcel.writeInt(this.connectionCount);
        parcel.writeByte(this.isLargeFile ? (byte) 1 : (byte) 0);
    }
}
