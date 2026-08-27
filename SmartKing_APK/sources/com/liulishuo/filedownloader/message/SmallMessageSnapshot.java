package com.liulishuo.filedownloader.message;

import android.os.Parcel;
import com.liulishuo.filedownloader.message.MessageSnapshot;

/* loaded from: classes.dex */
public abstract class SmallMessageSnapshot extends MessageSnapshot {

    /* loaded from: classes.dex */
    public static class CompletedFlowDirectlySnapshot extends CompletedSnapshot implements IFlowDirectly {
        /* JADX INFO: Access modifiers changed from: package-private */
        public CompletedFlowDirectlySnapshot(int i, boolean z, int i2) {
            super(i, z, i2);
        }

        CompletedFlowDirectlySnapshot(Parcel parcel) {
            super(parcel);
        }
    }

    /* loaded from: classes.dex */
    public static class CompletedSnapshot extends SmallMessageSnapshot {
        private final boolean reusedDownloadedFile;
        private final int totalBytes;

        /* JADX INFO: Access modifiers changed from: package-private */
        public CompletedSnapshot(int i, boolean z, int i2) {
            super(i);
            this.reusedDownloadedFile = z;
            this.totalBytes = i2;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public CompletedSnapshot(Parcel parcel) {
            super(parcel);
            this.reusedDownloadedFile = parcel.readByte() != 0;
            this.totalBytes = parcel.readInt();
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, com.liulishuo.filedownloader.message.IMessageSnapshot
        public int getSmallTotalBytes() {
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
            parcel.writeInt(this.totalBytes);
        }
    }

    /* loaded from: classes.dex */
    public static class ConnectedMessageSnapshot extends SmallMessageSnapshot {
        private final String etag;
        private final String fileName;
        private final boolean resuming;
        private final int totalBytes;

        /* JADX INFO: Access modifiers changed from: package-private */
        public ConnectedMessageSnapshot(int i, boolean z, int i2, String str, String str2) {
            super(i);
            this.resuming = z;
            this.totalBytes = i2;
            this.etag = str;
            this.fileName = str2;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public ConnectedMessageSnapshot(Parcel parcel) {
            super(parcel);
            this.resuming = parcel.readByte() != 0;
            this.totalBytes = parcel.readInt();
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
        public int getSmallTotalBytes() {
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
            parcel.writeInt(this.totalBytes);
            parcel.writeString(this.etag);
            parcel.writeString(this.fileName);
        }
    }

    /* loaded from: classes.dex */
    public static class ErrorMessageSnapshot extends SmallMessageSnapshot {
        private final int sofarBytes;
        private final Throwable throwable;

        /* JADX INFO: Access modifiers changed from: package-private */
        public ErrorMessageSnapshot(int i, int i2, Throwable th) {
            super(i);
            this.sofarBytes = i2;
            this.throwable = th;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public ErrorMessageSnapshot(Parcel parcel) {
            super(parcel);
            this.sofarBytes = parcel.readInt();
            this.throwable = (Throwable) parcel.readSerializable();
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, com.liulishuo.filedownloader.message.IMessageSnapshot
        public int getSmallSofarBytes() {
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
            parcel.writeInt(this.sofarBytes);
            parcel.writeSerializable(this.throwable);
        }
    }

    /* loaded from: classes.dex */
    public static class PausedSnapshot extends PendingMessageSnapshot {
        /* JADX INFO: Access modifiers changed from: package-private */
        public PausedSnapshot(int i, int i2, int i3) {
            super(i, i2, i3);
        }

        @Override // com.liulishuo.filedownloader.message.SmallMessageSnapshot.PendingMessageSnapshot, com.liulishuo.filedownloader.message.IMessageSnapshot
        public byte getStatus() {
            return (byte) -2;
        }
    }

    /* loaded from: classes.dex */
    public static class PendingMessageSnapshot extends SmallMessageSnapshot {
        private final int sofarBytes;
        private final int totalBytes;

        /* JADX INFO: Access modifiers changed from: package-private */
        public PendingMessageSnapshot(int i, int i2, int i3) {
            super(i);
            this.sofarBytes = i2;
            this.totalBytes = i3;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public PendingMessageSnapshot(Parcel parcel) {
            super(parcel);
            this.sofarBytes = parcel.readInt();
            this.totalBytes = parcel.readInt();
        }

        PendingMessageSnapshot(PendingMessageSnapshot pendingMessageSnapshot) {
            this(pendingMessageSnapshot.getId(), pendingMessageSnapshot.getSmallSofarBytes(), pendingMessageSnapshot.getSmallTotalBytes());
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, com.liulishuo.filedownloader.message.IMessageSnapshot
        public int getSmallSofarBytes() {
            return this.sofarBytes;
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, com.liulishuo.filedownloader.message.IMessageSnapshot
        public int getSmallTotalBytes() {
            return this.totalBytes;
        }

        @Override // com.liulishuo.filedownloader.message.IMessageSnapshot
        public byte getStatus() {
            return (byte) 1;
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.sofarBytes);
            parcel.writeInt(this.totalBytes);
        }
    }

    /* loaded from: classes.dex */
    public static class ProgressMessageSnapshot extends SmallMessageSnapshot {
        private final int sofarBytes;

        /* JADX INFO: Access modifiers changed from: package-private */
        public ProgressMessageSnapshot(int i, int i2) {
            super(i);
            this.sofarBytes = i2;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public ProgressMessageSnapshot(Parcel parcel) {
            super(parcel);
            this.sofarBytes = parcel.readInt();
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, com.liulishuo.filedownloader.message.IMessageSnapshot
        public int getSmallSofarBytes() {
            return this.sofarBytes;
        }

        @Override // com.liulishuo.filedownloader.message.IMessageSnapshot
        public byte getStatus() {
            return (byte) 3;
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.sofarBytes);
        }
    }

    /* loaded from: classes.dex */
    public static class RetryMessageSnapshot extends ErrorMessageSnapshot {
        private final int retryingTimes;

        /* JADX INFO: Access modifiers changed from: package-private */
        public RetryMessageSnapshot(int i, int i2, Throwable th, int i3) {
            super(i, i2, th);
            this.retryingTimes = i3;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public RetryMessageSnapshot(Parcel parcel) {
            super(parcel);
            this.retryingTimes = parcel.readInt();
        }

        @Override // com.liulishuo.filedownloader.message.SmallMessageSnapshot.ErrorMessageSnapshot, com.liulishuo.filedownloader.message.MessageSnapshot, android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot, com.liulishuo.filedownloader.message.IMessageSnapshot
        public int getRetryingTimes() {
            return this.retryingTimes;
        }

        @Override // com.liulishuo.filedownloader.message.SmallMessageSnapshot.ErrorMessageSnapshot, com.liulishuo.filedownloader.message.IMessageSnapshot
        public byte getStatus() {
            return (byte) 5;
        }

        @Override // com.liulishuo.filedownloader.message.SmallMessageSnapshot.ErrorMessageSnapshot, com.liulishuo.filedownloader.message.MessageSnapshot, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.retryingTimes);
        }
    }

    /* loaded from: classes.dex */
    public static class WarnFlowDirectlySnapshot extends WarnMessageSnapshot implements IFlowDirectly {
        /* JADX INFO: Access modifiers changed from: package-private */
        public WarnFlowDirectlySnapshot(int i, int i2, int i3) {
            super(i, i2, i3);
        }

        WarnFlowDirectlySnapshot(Parcel parcel) {
            super(parcel);
        }
    }

    /* loaded from: classes.dex */
    public static class WarnMessageSnapshot extends PendingMessageSnapshot implements MessageSnapshot.IWarnMessageSnapshot {
        /* JADX INFO: Access modifiers changed from: package-private */
        public WarnMessageSnapshot(int i, int i2, int i3) {
            super(i, i2, i3);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public WarnMessageSnapshot(Parcel parcel) {
            super(parcel);
        }

        @Override // com.liulishuo.filedownloader.message.SmallMessageSnapshot.PendingMessageSnapshot, com.liulishuo.filedownloader.message.IMessageSnapshot
        public byte getStatus() {
            return (byte) -4;
        }

        @Override // com.liulishuo.filedownloader.message.MessageSnapshot.IWarnMessageSnapshot
        public MessageSnapshot turnToPending() {
            return new PendingMessageSnapshot(this);
        }
    }

    SmallMessageSnapshot(int i) {
        super(i);
        this.isLargeFile = false;
    }

    SmallMessageSnapshot(Parcel parcel) {
        super(parcel);
    }

    @Override // com.liulishuo.filedownloader.message.MessageSnapshot, com.liulishuo.filedownloader.message.IMessageSnapshot
    public long getLargeSofarBytes() {
        return getSmallSofarBytes();
    }

    @Override // com.liulishuo.filedownloader.message.MessageSnapshot, com.liulishuo.filedownloader.message.IMessageSnapshot
    public long getLargeTotalBytes() {
        return getSmallTotalBytes();
    }
}
