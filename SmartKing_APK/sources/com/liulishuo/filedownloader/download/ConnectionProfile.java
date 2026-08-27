package com.liulishuo.filedownloader.download;

import com.liulishuo.filedownloader.connection.FileDownloadConnection;
import com.liulishuo.filedownloader.util.FileDownloadProperties;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.net.ProtocolException;

/* loaded from: classes.dex */
public class ConnectionProfile {
    static final int RANGE_INFINITE = -1;
    final long contentLength;
    final long currentOffset;
    final long endOffset;
    private final boolean isForceNoRange;
    private final boolean isTrialConnect;
    final long startOffset;

    /* loaded from: classes.dex */
    public static class ConnectionProfileBuild {
        public static ConnectionProfile buildBeginToEndConnectionProfile(long j) {
            return new ConnectionProfile(0L, 0L, -1L, j);
        }

        public static ConnectionProfile buildConnectionProfile(long j, long j2, long j3, long j4) {
            return new ConnectionProfile(j, j2, j3, j4);
        }

        public static ConnectionProfile buildToEndConnectionProfile(long j, long j2, long j3) {
            return new ConnectionProfile(j, j2, -1L, j3);
        }

        public static ConnectionProfile buildTrialConnectionProfile() {
            return new ConnectionProfile();
        }

        public static ConnectionProfile buildTrialConnectionProfileNoRange() {
            return new ConnectionProfile(0L, 0L, 0L, 0L, true);
        }
    }

    private ConnectionProfile() {
        this.startOffset = 0L;
        this.currentOffset = 0L;
        this.endOffset = 0L;
        this.contentLength = 0L;
        this.isForceNoRange = false;
        this.isTrialConnect = true;
    }

    private ConnectionProfile(long j, long j2, long j3, long j4) {
        this(j, j2, j3, j4, false);
    }

    private ConnectionProfile(long j, long j2, long j3, long j4, boolean z) {
        if (!(j == 0 && j3 == 0) && z) {
            throw new IllegalArgumentException();
        }
        this.startOffset = j;
        this.currentOffset = j2;
        this.endOffset = j3;
        this.contentLength = j4;
        this.isForceNoRange = z;
        this.isTrialConnect = false;
    }

    public void processProfile(FileDownloadConnection fileDownloadConnection) throws ProtocolException {
        if (this.isForceNoRange) {
            return;
        }
        if (this.isTrialConnect && FileDownloadProperties.getImpl().trialConnectionHeadMethod) {
            fileDownloadConnection.setRequestMethod("HEAD");
        }
        fileDownloadConnection.addHeader("Range", this.endOffset == -1 ? FileDownloadUtils.formatString("bytes=%d-", Long.valueOf(this.currentOffset)) : FileDownloadUtils.formatString("bytes=%d-%d", Long.valueOf(this.currentOffset), Long.valueOf(this.endOffset)));
    }

    public String toString() {
        return FileDownloadUtils.formatString("range[%d, %d) current offset[%d]", Long.valueOf(this.startOffset), Long.valueOf(this.endOffset), Long.valueOf(this.currentOffset));
    }
}
