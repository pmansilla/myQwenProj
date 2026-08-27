package com.liulishuo.filedownloader.message;

import android.os.Parcel;
import com.liulishuo.filedownloader.message.MessageSnapshot;

/* loaded from: classes.dex */
public abstract class LargeMessageSnapshot extends MessageSnapshot {

    /* loaded from: classes.dex */
    public static class CompletedFlowDirectlySnapshot extends CompletedSnapshot implements IFlowDirectly {
        /* JADX INFO: Access modifiers changed from: package-private */
        public CompletedFlowDirectlySnapshot(int i, boolean z, long j) {
            super(i, z, j);
        }

        CompletedFlowDirectlySnapshot(Parcel parcel) {
            super(parcel);
        }
    }

    /* loaded from: classes.dex */
    public static class CompletedSnapshot extends LargeMessageSnapshot {
        private final boolean reusedDownloadedFile;
        private final long totalBytes;

        /* JADX INFO: Access modifiers changed from: package-private */
        public CompletedSnapshot(int i, boolean z, long j) {
            super(i);
            this.reusedDownloadedFile = z;
            this.totalBytes = j;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public CompletedSnapshot(Parcel parcel) {
            super(parcel);
            this.reusedDownloadedFile = parcel.readByte() != 0;
            this.totalBytes = parcel.readLong();
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, com.liulishuo.filedownloader.message.IMessageSnapshot
        public long getLargeTotalBytes() {
            return this.totalBytes;
        }

        @Override // com.liulishuo.filedownloader.message.IMessageSnapshot
        public byte getStatus() {
            return (byte) -3;
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, com.liulishuo.filedownloader.message.IMessageSnapshot
        public boolean isReusedDownloadedFile() {
            return this.reusedDownloadedFile;
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeByte(this.reusedDownloadedFile ? (byte) 1 : (byte) 0);
            parcel.writeLong(this.totalBytes);
        }
    }

    /* loaded from: classes.dex */
    public static class ConnectedMessageSnapshot extends LargeMessageSnapshot {
        private final String etag;
        private final String fileName;
        private final boolean resuming;
        private final long totalBytes;

        /* JADX INFO: Access modifiers changed from: package-private */
        public ConnectedMessageSnapshot(int i, boolean z, long j, String str, String str2) {
            super(i);
            this.resuming = z;
            this.totalBytes = j;
            this.etag = str;
            this.fileName = str2;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public ConnectedMessageSnapshot(Parcel parcel) {
            super(parcel);
            this.resuming = parcel.readByte() != 0;
            this.totalBytes = parcel.readLong();
            this.etag = parcel.readString();
            this.fileName = parcel.readString();
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, com.liulishuo.filedownloader.message.IMessageSnapshot
        public String getEtag() {
            return this.etag;
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, com.liulishuo.filedownloader.message.IMessageSnapshot
        public String getFileName() {
            return this.fileName;
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, com.liulishuo.filedownloader.message.IMessageSnapshot
        public long getLargeTotalBytes() {
            return this.totalBytes;
        }

        @Override // com.liulishuo.filedownloader.message.IMessageSnapshot
        public byte getStatus() {
            return (byte) 2;
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, com.liulishuo.filedownloader.message.IMessageSnapshot
        public boolean isResuming() {
            return this.resuming;
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeByte(this.resuming ? (byte) 1 : (byte) 0);
            parcel.writeLong(this.totalBytes);
            parcel.writeString(this.etag);
            parcel.writeString(this.fileName);
        }
    }

    /* loaded from: classes.dex */
    public static class ErrorMessageSnapshot extends LargeMessageSnapshot {
        private final long sofarBytes;
        private final Throwable throwable;

        /* JADX INFO: Access modifiers changed from: package-private */
        public ErrorMessageSnapshot(int i, long j, Throwable th) {
            super(i);
            this.sofarBytes = j;
            this.throwable = th;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public ErrorMessageSnapshot(Parcel parcel) {
            super(parcel);
            this.sofarBytes = parcel.readLong();
            this.throwable = (Throwable) parcel.readSerializable();
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, com.liulishuo.filedownloader.message.IMessageSnapshot
        public long getLargeSofarBytes() {
            return this.sofarBytes;
        }

        @Override // com.liulishuo.filedownloader.message.IMessageSnapshot
        public byte getStatus() {
            return (byte) -1;
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, com.liulishuo.filedownloader.message.IMessageSnapshot
        public Throwable getThrowable() {
            return this.throwable;
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeLong(this.sofarBytes);
            parcel.writeSerializable(this.throwable);
        }
    }

    /* loaded from: classes.dex */
    public static class PausedSnapshot extends PendingMessageSnapshot {
        /* JADX INFO: Access modifiers changed from: package-private */
        public PausedSnapshot(int i, long j, long j2) {
            super(i, j, j2);
        }

        @Override // com.liulishuo.filedownloader.message.LargeMessageSnapshot.PendingMessageSnapshot, com.liulishuo.filedownloader.message.IMessageSnapshot
        public byte getStatus() {
            return (byte) -2;
        }
    }

    /* loaded from: classes.dex */
    public static class PendingMessageSnapshot extends LargeMessageSnapshot {
        private final long sofarBytes;
        private final long totalBytes;

        /* JADX INFO: Access modifiers changed from: package-private */
        public PendingMessageSnapshot(int i, long j, long j2) {
            super(i);
            this.sofarBytes = j;
            this.totalBytes = j2;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public PendingMessageSnapshot(Parcel parcel) {
            super(parcel);
            this.sofarBytes = parcel.readLong();
            this.totalBytes = parcel.readLong();
        }

        PendingMessageSnapshot(PendingMessageSnapshot pendingMessageSnapshot) {
            this(pendingMessageSnapshot.getId(), pendingMessageSnapshot.getLargeSofarBytes(), pendingMessageSnapshot.getLargeTotalBytes());
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, com.liulishuo.filedownloader.message.IMessageSnapshot
        public long getLargeSofarBytes() {
            return this.sofarBytes;
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, com.liulishuo.filedownloader.message.IMessageSnapshot
        public long getLargeTotalBytes() {
            return this.totalBytes;
        }

        @Override // com.liulishuo.filedownloader.message.IMessageSnapshot
        public byte getStatus() {
            return (byte) 1;
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeLong(this.sofarBytes);
            parcel.writeLong(this.totalBytes);
        }
    }

    /* loaded from: classes.dex */
    public static class ProgressMessageSnapshot extends LargeMessageSnapshot {
        private final long sofarBytes;

        /* JADX INFO: Access modifiers changed from: package-private */
        public ProgressMessageSnapshot(int i, long j) {
            super(i);
            this.sofarBytes = j;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public ProgressMessageSnapshot(Parcel parcel) {
            super(parcel);
            this.sofarBytes = parcel.readLong();
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, com.liulishuo.filedownloader.message.IMessageSnapshot
        public long getLargeSofarBytes() {
            return this.sofarBytes;
        }

        @Override // com.liulishuo.filedownloader.message.IMessageSnapshot
        public byte getStatus() {
            return (byte) 3;
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeLong(this.sofarBytes);
        }
    }

    /* loaded from: classes.dex */
    public static class RetryMessageSnapshot extends ErrorMessageSnapshot {
        private final int retryingTimes;

        /* JADX INFO: Access modifiers changed from: package-private */
        public RetryMessageSnapshot(int i, long j, Throwable th, int i2) {
            super(i, j, th);
            this.retryingTimes = i2;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public RetryMessageSnapshot(Parcel parcel) {
            super(parcel);
            this.retryingTimes = parcel.readInt();
        }

        @Override // com.liulishuo.filedownloader.message.LargeMessageSnapshot.ErrorMessageSnapshot, com.liulishuo.filedownloader.message.MessageSnapshot, android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, com.liulishuo.filedownloader.message.IMessageSnapshot
        public int getRetryingTimes() {
            return this.retryingTimes;
        }

        @Override // com.liulishuo.filedownloader.message.LargeMessageSnapshot.ErrorMessageSnapshot, com.liulishuo.filedownloader.message.IMessageSnapshot
        public byte getStatus() {
            return (byte) 5;
        }

        @Override // com.liulishuo.filedownloader.message.LargeMessageSnapshot.ErrorMessageSnapshot, com.liulishuo.filedownloader.message.MessageSnapshot, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.retryingTimes);
        }
    }

    /* loaded from: classes.dex */
    public static class WarnFlowDirectlySnapshot extends WarnMessageSnapshot implements IFlowDirectly {
        /* JADX INFO: Access modifiers changed from: package-private */
        public WarnFlowDirectlySnapshot(int i, long j, long j2) {
            super(i, j, j2);
        }

        WarnFlowDirectlySnapshot(Parcel parcel) {
            super(parcel);
        }
    }

    /* loaded from: classes.dex */
    public static class WarnMessageSnapshot extends PendingMessageSnapshot implements MessageSnapshot.IWarnMessageSnapshot {
        /* JADX INFO: Access modifiers changed from: package-private */
        public WarnMessageSnapshot(int i, long j, long j2) {
            super(i, j, j2);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public WarnMessageSnapshot(Parcel parcel) {
            super(parcel);
        }

        @Override // com.liulishuo.filedownloader.message.LargeMessageSnapshot.PendingMessageSnapshot, com.liulishuo.filedownloader.message.IMessageSnapshot
        public byte getStatus() {
            return (byte) -4;
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot.IWarnMessageSnapshot
        public MessageSnapshot turnToPending() {
            return new PendingMessageSnapshot(this);
        }
    }

    LargeMessageSnapshot(int i) {
        super(i);
        this.isLargeFile = true;
    }

    LargeMessageSnapshot(Parcel parcel) {
        super(parcel);
    }

    @Override // com.liulishuo.filedownloader.message.MessageSnapshot, com.liulishuo.filedownloader.message.IMessageSnapshot
    public int getSmallSofarBytes() {
        if (getLargeSofarBytes() > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) getLargeSofarBytes();
    }

    @Override // com.liulishuo.filedownloader.message.MessageSnapshot, com.liulishuo.filedownloader.message.IMessageSnapshot
    public int getSmallTotalBytes() {
        if (getLargeTotalBytes() > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) getLargeTotalBytes();
    }
}
