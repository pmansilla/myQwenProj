package com.liulishuo.filedownloader.download;

import com.liulishuo.filedownloader.database.FileDownloadDatabase;
import com.liulishuo.filedownloader.download.ConnectTask;
import com.liulishuo.filedownloader.model.ConnectionModel;
import com.liulishuo.filedownloader.model.FileDownloadHeader;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

/* loaded from: classes.dex */
public class DownloadRunnable implements Runnable {
    private final ProcessCallback callback;
    private final ConnectTask connectTask;
    final int connectionIndex;
    private final int downloadId;
    private FetchDataTask fetchDataTask;
    private final boolean isWifiRequired;
    private final String path;
    private volatile boolean paused;

    /* loaded from: classes.dex */
    public static class Builder {
        private ProcessCallback callback;
        private final ConnectTask.Builder connectTaskBuilder = new ConnectTask.Builder();
        private Integer connectionIndex;
        private Boolean isWifiRequired;
        private String path;

        public DownloadRunnable build() {
            if (this.callback == null || this.path == null || this.isWifiRequired == null || this.connectionIndex == null) {
                throw new IllegalArgumentException(FileDownloadUtils.formatString("%s %s %B", this.callback, this.path, this.isWifiRequired));
            }
            ConnectTask build = this.connectTaskBuilder.build();
            return new DownloadRunnable(build.downloadId, this.connectionIndex.intValue(), build, this.callback, this.isWifiRequired.booleanValue(), this.path);
        }

        DownloadRunnable buildForTest(ConnectTask connectTask) {
            return new DownloadRunnable(connectTask.downloadId, 0, connectTask, this.callback, false, "");
        }

        public Builder setCallback(ProcessCallback processCallback) {
            this.callback = processCallback;
            return this;
        }

        public Builder setConnectionIndex(Integer num) {
            this.connectionIndex = num;
            return this;
        }

        public Builder setConnectionModel(ConnectionProfile connectionProfile) {
            this.connectTaskBuilder.setConnectionProfile(connectionProfile);
            return this;
        }

        public Builder setEtag(String str) {
            this.connectTaskBuilder.setEtag(str);
            return this;
        }

        public Builder setHeader(FileDownloadHeader fileDownloadHeader) {
            this.connectTaskBuilder.setHeader(fileDownloadHeader);
            return this;
        }

        public Builder setId(int i) {
            this.connectTaskBuilder.setDownloadId(i);
            return this;
        }

        public Builder setPath(String str) {
            this.path = str;
            return this;
        }

        public Builder setUrl(String str) {
            this.connectTaskBuilder.setUrl(str);
            return this;
        }

        public Builder setWifiRequired(boolean z) {
            this.isWifiRequired = Boolean.valueOf(z);
            return this;
        }
    }

    private DownloadRunnable(int i, int i2, ConnectTask connectTask, ProcessCallback processCallback, boolean z, String str) {
        this.downloadId = i;
        this.connectionIndex = i2;
        this.paused = false;
        this.callback = processCallback;
        this.path = str;
        this.connectTask = connectTask;
        this.isWifiRequired = z;
    }

    private long getDownloadedOffset() {
        FileDownloadDatabase databaseInstance = CustomComponentHolder.getImpl().getDatabaseInstance();
        if (this.connectionIndex < 0) {
            FileDownloadModel find = databaseInstance.find(this.downloadId);
            if (find != null) {
                return find.getSoFar();
            }
            return 0L;
        }
        for (ConnectionModel connectionModel : databaseInstance.findConnectionModel(this.downloadId)) {
            if (connectionModel.getIndex() == this.connectionIndex) {
                return connectionModel.getCurrentOffset();
            }
        }
        return 0L;
    }

    public void discard() {
        pause();
    }

    public void pause() {
        this.paused = true;
        if (this.fetchDataTask != null) {
            this.fetchDataTask.pause();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:45:0x0105  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x013c A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x014b  */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void run() {
        /*
            Method dump skipped, instructions count: 335
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.liulishuo.filedownloader.download.DownloadRunnable.run():void");
    }
}
