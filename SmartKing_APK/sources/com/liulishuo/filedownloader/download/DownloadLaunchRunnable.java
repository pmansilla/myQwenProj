package com.liulishuo.filedownloader.download;

import android.os.Process;
import com.liulishuo.filedownloader.IThreadPoolMonitor;
import com.liulishuo.filedownloader.connection.FileDownloadConnection;
import com.liulishuo.filedownloader.database.FileDownloadDatabase;
import com.liulishuo.filedownloader.download.ConnectTask;
import com.liulishuo.filedownloader.download.ConnectionProfile;
import com.liulishuo.filedownloader.download.DownloadRunnable;
import com.liulishuo.filedownloader.exception.FileDownloadGiveUpRetryException;
import com.liulishuo.filedownloader.exception.FileDownloadHttpException;
import com.liulishuo.filedownloader.exception.FileDownloadNetworkPolicyException;
import com.liulishuo.filedownloader.exception.FileDownloadOutOfSpaceException;
import com.liulishuo.filedownloader.exception.FileDownloadSecurityException;
import com.liulishuo.filedownloader.model.ConnectionModel;
import com.liulishuo.filedownloader.model.FileDownloadHeader;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.liulishuo.filedownloader.stream.FileDownloadOutputStream;
import com.liulishuo.filedownloader.util.FileDownloadExecutors;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadProperties;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes.dex */
public class DownloadLaunchRunnable implements Runnable, ProcessCallback {
    private static final ThreadPoolExecutor DOWNLOAD_EXECUTOR = FileDownloadExecutors.newFixedThreadPool("ConnectionBlock");
    private static final int HTTP_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
    private static final int TOTAL_VALUE_IN_CHUNKED_RESOURCE = -1;
    private boolean acceptPartial;
    private final AtomicBoolean alive;
    private final FileDownloadDatabase database;
    private final int defaultConnectionCount;
    private final ArrayList<DownloadRunnable> downloadRunnableList;
    private volatile boolean error;
    private volatile Exception errorException;
    private boolean isChunked;
    private final boolean isForceReDownload;
    private boolean isNeedForceDiscardRange;
    private boolean isResumeAvailableOnDB;
    private boolean isSingleConnection;
    private boolean isTriedFixRangeNotSatisfiable;
    private final boolean isWifiRequired;
    private long lastCallbackBytes;
    private long lastCallbackTimestamp;
    private long lastUpdateBytes;
    private long lastUpdateTimestamp;
    private final FileDownloadModel model;
    private volatile boolean paused;
    private String redirectedUrl;
    private DownloadRunnable singleDownloadRunnable;
    private final DownloadStatusCallback statusCallback;
    private final boolean supportSeek;
    private final IThreadPoolMonitor threadPoolMonitor;
    private final FileDownloadHeader userRequestHeader;
    int validRetryTimes;

    /* loaded from: classes.dex */
    public static class Builder {
        private Integer callbackProgressMaxCount;
        private FileDownloadHeader header;
        private Boolean isForceReDownload;
        private Boolean isWifiRequired;
        private Integer maxRetryTimes;
        private Integer minIntervalMillis;
        private FileDownloadModel model;
        private IThreadPoolMonitor threadPoolMonitor;

        public DownloadLaunchRunnable build() {
            if (this.model == null || this.threadPoolMonitor == null || this.minIntervalMillis == null || this.callbackProgressMaxCount == null || this.isForceReDownload == null || this.isWifiRequired == null || this.maxRetryTimes == null) {
                throw new IllegalArgumentException();
            }
            return new DownloadLaunchRunnable(this.model, this.header, this.threadPoolMonitor, this.minIntervalMillis.intValue(), this.callbackProgressMaxCount.intValue(), this.isForceReDownload.booleanValue(), this.isWifiRequired.booleanValue(), this.maxRetryTimes.intValue());
        }

        public Builder setCallbackProgressMaxCount(Integer num) {
            this.callbackProgressMaxCount = num;
            return this;
        }

        public Builder setForceReDownload(Boolean bool) {
            this.isForceReDownload = bool;
            return this;
        }

        public Builder setHeader(FileDownloadHeader fileDownloadHeader) {
            this.header = fileDownloadHeader;
            return this;
        }

        public Builder setMaxRetryTimes(Integer num) {
            this.maxRetryTimes = num;
            return this;
        }

        public Builder setMinIntervalMillis(Integer num) {
            this.minIntervalMillis = num;
            return this;
        }

        public Builder setModel(FileDownloadModel fileDownloadModel) {
            this.model = fileDownloadModel;
            return this;
        }

        public Builder setThreadPoolMonitor(IThreadPoolMonitor iThreadPoolMonitor) {
            this.threadPoolMonitor = iThreadPoolMonitor;
            return this;
        }

        public Builder setWifiRequired(Boolean bool) {
            this.isWifiRequired = bool;
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class DiscardSafely extends Throwable {
        DiscardSafely() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class RetryDirectly extends Throwable {
        RetryDirectly() {
        }
    }

    private DownloadLaunchRunnable(DownloadStatusCallback downloadStatusCallback, FileDownloadModel fileDownloadModel, FileDownloadHeader fileDownloadHeader, IThreadPoolMonitor iThreadPoolMonitor, int i, int i2, boolean z, boolean z2, int i3) {
        this.defaultConnectionCount = 5;
        this.isNeedForceDiscardRange = false;
        this.downloadRunnableList = new ArrayList<>(5);
        this.lastCallbackBytes = 0L;
        this.lastCallbackTimestamp = 0L;
        this.lastUpdateBytes = 0L;
        this.lastUpdateTimestamp = 0L;
        this.alive = new AtomicBoolean(true);
        this.paused = false;
        this.isTriedFixRangeNotSatisfiable = false;
        this.model = fileDownloadModel;
        this.userRequestHeader = fileDownloadHeader;
        this.isForceReDownload = z;
        this.isWifiRequired = z2;
        this.database = CustomComponentHolder.getImpl().getDatabaseInstance();
        this.supportSeek = CustomComponentHolder.getImpl().isSupportSeek();
        this.threadPoolMonitor = iThreadPoolMonitor;
        this.validRetryTimes = i3;
        this.statusCallback = downloadStatusCallback;
    }

    private DownloadLaunchRunnable(FileDownloadModel fileDownloadModel, FileDownloadHeader fileDownloadHeader, IThreadPoolMonitor iThreadPoolMonitor, int i, int i2, boolean z, boolean z2, int i3) {
        this.defaultConnectionCount = 5;
        this.isNeedForceDiscardRange = false;
        this.downloadRunnableList = new ArrayList<>(5);
        this.lastCallbackBytes = 0L;
        this.lastCallbackTimestamp = 0L;
        this.lastUpdateBytes = 0L;
        this.lastUpdateTimestamp = 0L;
        this.alive = new AtomicBoolean(true);
        this.paused = false;
        this.isTriedFixRangeNotSatisfiable = false;
        this.model = fileDownloadModel;
        this.userRequestHeader = fileDownloadHeader;
        this.isForceReDownload = z;
        this.isWifiRequired = z2;
        this.database = CustomComponentHolder.getImpl().getDatabaseInstance();
        this.supportSeek = CustomComponentHolder.getImpl().isSupportSeek();
        this.threadPoolMonitor = iThreadPoolMonitor;
        this.validRetryTimes = i3;
        this.statusCallback = new DownloadStatusCallback(fileDownloadModel, i3, i, i2);
    }

    private int calcConnectionCount(long j) {
        if (isMultiConnectionAvailable()) {
            return this.isResumeAvailableOnDB ? this.model.getConnectionCount() : CustomComponentHolder.getImpl().determineConnectionCount(this.model.getId(), this.model.getUrl(), this.model.getPath(), j);
        }
        return 1;
    }

    private void checkupAfterGetFilename() throws RetryDirectly, DiscardSafely {
        int id = this.model.getId();
        if (this.model.isPathAsDirectory()) {
            String targetFilePath = this.model.getTargetFilePath();
            int generateId = FileDownloadUtils.generateId(this.model.getUrl(), targetFilePath);
            if (FileDownloadHelper.inspectAndInflowDownloaded(id, targetFilePath, this.isForceReDownload, false)) {
                this.database.remove(id);
                this.database.removeConnections(id);
                throw new DiscardSafely();
            }
            FileDownloadModel find = this.database.find(generateId);
            if (find != null) {
                if (FileDownloadHelper.inspectAndInflowDownloading(id, find, this.threadPoolMonitor, false)) {
                    this.database.remove(id);
                    this.database.removeConnections(id);
                    throw new DiscardSafely();
                }
                List<ConnectionModel> findConnectionModel = this.database.findConnectionModel(generateId);
                this.database.remove(generateId);
                this.database.removeConnections(generateId);
                FileDownloadUtils.deleteTargetFile(this.model.getTargetFilePath());
                if (FileDownloadUtils.isBreakpointAvailable(generateId, find)) {
                    this.model.setSoFar(find.getSoFar());
                    this.model.setTotal(find.getTotal());
                    this.model.setETag(find.getETag());
                    this.model.setConnectionCount(find.getConnectionCount());
                    this.database.update(this.model);
                    if (findConnectionModel != null) {
                        for (ConnectionModel connectionModel : findConnectionModel) {
                            connectionModel.setId(id);
                            this.database.insertConnectionModel(connectionModel);
                        }
                    }
                    throw new RetryDirectly();
                }
            }
            if (FileDownloadHelper.inspectAndInflowConflictPath(id, this.model.getSoFar(), this.model.getTempFilePath(), targetFilePath, this.threadPoolMonitor)) {
                this.database.remove(id);
                this.database.removeConnections(id);
                throw new DiscardSafely();
            }
        }
    }

    private void checkupBeforeConnect() throws FileDownloadGiveUpRetryException {
        if (this.isWifiRequired && !FileDownloadUtils.checkPermission("android.permission.ACCESS_NETWORK_STATE")) {
            throw new FileDownloadGiveUpRetryException(FileDownloadUtils.formatString("Task[%d] can't start the download runnable, because this task require wifi, but user application nor current process has %s, so we can't check whether the network type connection.", Integer.valueOf(this.model.getId()), "android.permission.ACCESS_NETWORK_STATE"));
        }
        if (this.isWifiRequired && FileDownloadUtils.isNetworkNotOnWifiType()) {
            throw new FileDownloadNetworkPolicyException();
        }
    }

    static DownloadLaunchRunnable createForTest(DownloadStatusCallback downloadStatusCallback, FileDownloadModel fileDownloadModel, FileDownloadHeader fileDownloadHeader, IThreadPoolMonitor iThreadPoolMonitor, int i, int i2, boolean z, boolean z2, int i3) {
        return new DownloadLaunchRunnable(downloadStatusCallback, fileDownloadModel, fileDownloadHeader, iThreadPoolMonitor, i, i2, z, z2, i3);
    }

    private int determineConnectionCount() {
        return 5;
    }

    private void fetchWithMultipleConnection(List<ConnectionModel> list, long j) throws InterruptedException {
        int id = this.model.getId();
        String eTag = this.model.getETag();
        String url = this.redirectedUrl != null ? this.redirectedUrl : this.model.getUrl();
        String tempFilePath = this.model.getTempFilePath();
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "fetch data with multiple connection(count: [%d]) for task[%d] totalLength[%d]", Integer.valueOf(list.size()), Integer.valueOf(id), Long.valueOf(j));
        }
        boolean z = this.isResumeAvailableOnDB;
        long j2 = 0;
        long j3 = 0;
        for (ConnectionModel connectionModel : list) {
            long currentOffset = connectionModel.getEndOffset() == -1 ? j - connectionModel.getCurrentOffset() : (connectionModel.getEndOffset() - connectionModel.getCurrentOffset()) + 1;
            j3 += connectionModel.getCurrentOffset() - connectionModel.getStartOffset();
            if (currentOffset != j2) {
                DownloadRunnable build = new DownloadRunnable.Builder().setId(id).setConnectionIndex(Integer.valueOf(connectionModel.getIndex())).setCallback(this).setUrl(url).setEtag(z ? eTag : null).setHeader(this.userRequestHeader).setWifiRequired(this.isWifiRequired).setConnectionModel(ConnectionProfile.ConnectionProfileBuild.buildConnectionProfile(connectionModel.getStartOffset(), connectionModel.getCurrentOffset(), connectionModel.getEndOffset(), currentOffset)).setPath(tempFilePath).build();
                if (FileDownloadLog.NEED_LOG) {
                    FileDownloadLog.d(this, "enable multiple connection: %s", connectionModel);
                }
                if (build == null) {
                    throw new IllegalArgumentException("the download runnable must not be null!");
                }
                this.downloadRunnableList.add(build);
            } else if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "pass connection[%d-%d], because it has been completed", Integer.valueOf(connectionModel.getId()), Integer.valueOf(connectionModel.getIndex()));
            }
            j2 = 0;
        }
        if (j3 != this.model.getSoFar()) {
            FileDownloadLog.w(this, "correct the sofar[%d] from connection table[%d]", Long.valueOf(this.model.getSoFar()), Long.valueOf(j3));
            this.model.setSoFar(j3);
        }
        ArrayList arrayList = new ArrayList(this.downloadRunnableList.size());
        Iterator<DownloadRunnable> it = this.downloadRunnableList.iterator();
        while (it.hasNext()) {
            DownloadRunnable next = it.next();
            if (this.paused) {
                next.pause();
            } else {
                arrayList.add(Executors.callable(next));
            }
        }
        if (this.paused) {
            this.model.setStatus((byte) -2);
            return;
        }
        List<Future> invokeAll = DOWNLOAD_EXECUTOR.invokeAll(arrayList);
        if (FileDownloadLog.NEED_LOG) {
            for (Future future : invokeAll) {
                FileDownloadLog.d(this, "finish sub-task for [%d] %B %B", Integer.valueOf(id), Boolean.valueOf(future.isDone()), Boolean.valueOf(future.isCancelled()));
            }
        }
    }

    private void handlePreAllocate(long j, String str) throws IOException, IllegalAccessException {
        FileDownloadOutputStream fileDownloadOutputStream;
        if (j != -1) {
            try {
                fileDownloadOutputStream = FileDownloadUtils.createOutputStream(this.model.getTempFilePath());
                try {
                    long length = new File(str).length();
                    long j2 = j - length;
                    long freeSpaceBytes = FileDownloadUtils.getFreeSpaceBytes(str);
                    if (freeSpaceBytes < j2) {
                        throw new FileDownloadOutOfSpaceException(freeSpaceBytes, j2, length);
                    }
                    if (!FileDownloadProperties.getImpl().fileNonPreAllocation) {
                        fileDownloadOutputStream.setLength(j);
                    }
                } catch (Throwable th) {
                    th = th;
                    if (fileDownloadOutputStream != null) {
                        fileDownloadOutputStream.close();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fileDownloadOutputStream = null;
            }
        } else {
            fileDownloadOutputStream = null;
        }
        if (fileDownloadOutputStream != null) {
            fileDownloadOutputStream.close();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x008a  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x011b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void handleTrialConnectResult(java.util.Map<java.lang.String, java.util.List<java.lang.String>> r18, com.liulishuo.filedownloader.download.ConnectTask r19, com.liulishuo.filedownloader.connection.FileDownloadConnection r20) throws java.io.IOException, com.liulishuo.filedownloader.download.DownloadLaunchRunnable.RetryDirectly, java.lang.IllegalArgumentException, com.liulishuo.filedownloader.exception.FileDownloadSecurityException {
        /*
            Method dump skipped, instructions count: 357
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.liulishuo.filedownloader.download.DownloadLaunchRunnable.handleTrialConnectResult(java.util.Map, com.liulishuo.filedownloader.download.ConnectTask, com.liulishuo.filedownloader.connection.FileDownloadConnection):void");
    }

    private boolean isMultiConnectionAvailable() {
        return (!this.isResumeAvailableOnDB || this.model.getConnectionCount() > 1) && this.acceptPartial && this.supportSeek && !this.isChunked;
    }

    private void realDownloadWithMultiConnectionFromBeginning(long j, int i) throws InterruptedException {
        long j2 = j / i;
        int id = this.model.getId();
        ArrayList arrayList = new ArrayList();
        long j3 = 0;
        int i2 = 0;
        while (i2 < i) {
            long j4 = i2 == i + (-1) ? -1L : (j3 + j2) - 1;
            ConnectionModel connectionModel = new ConnectionModel();
            connectionModel.setId(id);
            connectionModel.setIndex(i2);
            connectionModel.setStartOffset(j3);
            connectionModel.setCurrentOffset(j3);
            connectionModel.setEndOffset(j4);
            arrayList.add(connectionModel);
            this.database.insertConnectionModel(connectionModel);
            j3 += j2;
            i2++;
        }
        this.model.setConnectionCount(i);
        this.database.updateConnectionCount(id, i);
        fetchWithMultipleConnection(arrayList, j);
    }

    private void realDownloadWithMultiConnectionFromResume(int i, List<ConnectionModel> list) throws InterruptedException {
        if (i <= 1 || list.size() != i) {
            throw new IllegalArgumentException();
        }
        fetchWithMultipleConnection(list, this.model.getTotal());
    }

    private void realDownloadWithSingleConnection(long j) throws IOException, IllegalAccessException {
        ConnectionProfile buildToEndConnectionProfile;
        if (this.acceptPartial) {
            buildToEndConnectionProfile = ConnectionProfile.ConnectionProfileBuild.buildToEndConnectionProfile(this.model.getSoFar(), this.model.getSoFar(), j - this.model.getSoFar());
        } else {
            this.model.setSoFar(0L);
            buildToEndConnectionProfile = ConnectionProfile.ConnectionProfileBuild.buildBeginToEndConnectionProfile(j);
        }
        this.singleDownloadRunnable = new DownloadRunnable.Builder().setId(this.model.getId()).setConnectionIndex(-1).setCallback(this).setUrl(this.model.getUrl()).setEtag(this.model.getETag()).setHeader(this.userRequestHeader).setWifiRequired(this.isWifiRequired).setConnectionModel(buildToEndConnectionProfile).setPath(this.model.getTempFilePath()).build();
        this.model.setConnectionCount(1);
        this.database.updateConnectionCount(this.model.getId(), 1);
        if (!this.paused) {
            this.singleDownloadRunnable.run();
        } else {
            this.model.setStatus((byte) -2);
            this.singleDownloadRunnable.pause();
        }
    }

    private void trialConnect() throws IOException, RetryDirectly, IllegalAccessException, FileDownloadSecurityException {
        FileDownloadConnection fileDownloadConnection = null;
        try {
            ConnectTask build = new ConnectTask.Builder().setDownloadId(this.model.getId()).setUrl(this.model.getUrl()).setEtag(this.model.getETag()).setHeader(this.userRequestHeader).setConnectionProfile(this.isNeedForceDiscardRange ? ConnectionProfile.ConnectionProfileBuild.buildTrialConnectionProfileNoRange() : ConnectionProfile.ConnectionProfileBuild.buildTrialConnectionProfile()).build();
            FileDownloadConnection connect = build.connect();
            try {
                handleTrialConnectResult(build.getRequestHeader(), build, connect);
                if (connect != null) {
                    connect.ending();
                }
            } catch (Throwable th) {
                th = th;
                fileDownloadConnection = connect;
                if (fileDownloadConnection != null) {
                    fileDownloadConnection.ending();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public int getId() {
        return this.model.getId();
    }

    public String getTempFilePath() {
        return this.model.getTempFilePath();
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0068  */
    /* JADX WARN: Removed duplicated region for block: B:15:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0061  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void inspectTaskModelResumeAvailableOnDB(java.util.List<com.liulishuo.filedownloader.model.ConnectionModel> r11) {
        /*
            r10 = this;
            com.liulishuo.filedownloader.model.FileDownloadModel r0 = r10.model
            int r0 = r0.getConnectionCount()
            com.liulishuo.filedownloader.model.FileDownloadModel r1 = r10.model
            java.lang.String r1 = r1.getTempFilePath()
            com.liulishuo.filedownloader.model.FileDownloadModel r2 = r10.model
            java.lang.String r2 = r2.getTargetFilePath()
            r3 = 0
            r4 = 1
            if (r0 <= r4) goto L18
            r5 = 1
            goto L19
        L18:
            r5 = 0
        L19:
            boolean r6 = r10.isNeedForceDiscardRange
            r7 = 0
            if (r6 == 0) goto L21
        L1f:
            r5 = r7
            goto L58
        L21:
            if (r5 == 0) goto L28
            boolean r6 = r10.supportSeek
            if (r6 != 0) goto L28
            goto L1f
        L28:
            com.liulishuo.filedownloader.model.FileDownloadModel r6 = r10.model
            int r6 = r6.getId()
            com.liulishuo.filedownloader.model.FileDownloadModel r9 = r10.model
            boolean r6 = com.liulishuo.filedownloader.util.FileDownloadUtils.isBreakpointAvailable(r6, r9)
            if (r6 == 0) goto L1f
            boolean r6 = r10.supportSeek
            if (r6 != 0) goto L44
            java.io.File r11 = new java.io.File
            r11.<init>(r1)
            long r5 = r11.length()
            goto L58
        L44:
            if (r5 == 0) goto L52
            int r5 = r11.size()
            if (r0 == r5) goto L4d
            goto L1f
        L4d:
            long r5 = com.liulishuo.filedownloader.model.ConnectionModel.getTotalOffset(r11)
            goto L58
        L52:
            com.liulishuo.filedownloader.model.FileDownloadModel r11 = r10.model
            long r5 = r11.getSoFar()
        L58:
            com.liulishuo.filedownloader.model.FileDownloadModel r11 = r10.model
            r11.setSoFar(r5)
            int r11 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r11 <= 0) goto L62
            r3 = 1
        L62:
            r10.isResumeAvailableOnDB = r3
            boolean r11 = r10.isResumeAvailableOnDB
            if (r11 != 0) goto L76
            com.liulishuo.filedownloader.database.FileDownloadDatabase r11 = r10.database
            com.liulishuo.filedownloader.model.FileDownloadModel r0 = r10.model
            int r0 = r0.getId()
            r11.removeConnections(r0)
            com.liulishuo.filedownloader.util.FileDownloadUtils.deleteTaskFiles(r2, r1)
        L76:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.liulishuo.filedownloader.download.DownloadLaunchRunnable.inspectTaskModelResumeAvailableOnDB(java.util.List):void");
    }

    public boolean isAlive() {
        return this.alive.get() || this.statusCallback.isAlive();
    }

    @Override // com.liulishuo.filedownloader.download.ProcessCallback
    public boolean isRetry(Exception exc) {
        if (exc instanceof FileDownloadHttpException) {
            int code = ((FileDownloadHttpException) exc).getCode();
            if (this.isSingleConnection && code == HTTP_REQUESTED_RANGE_NOT_SATISFIABLE && !this.isTriedFixRangeNotSatisfiable) {
                FileDownloadUtils.deleteTaskFiles(this.model.getTargetFilePath(), this.model.getTempFilePath());
                this.isTriedFixRangeNotSatisfiable = true;
                return true;
            }
        }
        return this.validRetryTimes > 0 && !(exc instanceof FileDownloadGiveUpRetryException);
    }

    @Override // com.liulishuo.filedownloader.download.ProcessCallback
    public void onCompleted(DownloadRunnable downloadRunnable, long j, long j2) {
        if (this.paused) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "the task[%d] has already been paused, so pass the completed callback", Integer.valueOf(this.model.getId()));
                return;
            }
            return;
        }
        int i = downloadRunnable.connectionIndex;
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "the connection has been completed(%d): [%d, %d)  %d", Integer.valueOf(i), Long.valueOf(j), Long.valueOf(j2), Long.valueOf(this.model.getTotal()));
        }
        if (!this.isSingleConnection) {
            synchronized (this.downloadRunnableList) {
                this.downloadRunnableList.remove(downloadRunnable);
            }
        } else {
            if (j == 0 || j2 == this.model.getTotal()) {
                return;
            }
            FileDownloadLog.e(this, "the single task not completed corrected(%d, %d != %d) for task(%d)", Long.valueOf(j), Long.valueOf(j2), Long.valueOf(this.model.getTotal()), Integer.valueOf(this.model.getId()));
        }
    }

    @Override // com.liulishuo.filedownloader.download.ProcessCallback
    public void onError(Exception exc) {
        this.error = true;
        this.errorException = exc;
        if (this.paused) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "the task[%d] has already been paused, so pass the error callback", Integer.valueOf(this.model.getId()));
            }
        } else {
            Iterator it = ((ArrayList) this.downloadRunnableList.clone()).iterator();
            while (it.hasNext()) {
                DownloadRunnable downloadRunnable = (DownloadRunnable) it.next();
                if (downloadRunnable != null) {
                    downloadRunnable.discard();
                }
            }
        }
    }

    @Override // com.liulishuo.filedownloader.download.ProcessCallback
    public void onProgress(long j) {
        if (this.paused) {
            return;
        }
        this.statusCallback.onProgress(j);
    }

    @Override // com.liulishuo.filedownloader.download.ProcessCallback
    public void onRetry(Exception exc) {
        if (this.paused) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "the task[%d] has already been paused, so pass the retry callback", Integer.valueOf(this.model.getId()));
            }
        } else {
            int i = this.validRetryTimes;
            this.validRetryTimes = i - 1;
            if (i < 0) {
                FileDownloadLog.e(this, "valid retry times is less than 0(%d) for download task(%d)", Integer.valueOf(this.validRetryTimes), Integer.valueOf(this.model.getId()));
            }
            this.statusCallback.onRetry(exc, this.validRetryTimes);
        }
    }

    public void pause() {
        this.paused = true;
        if (this.singleDownloadRunnable != null) {
            this.singleDownloadRunnable.pause();
        }
        Iterator it = ((ArrayList) this.downloadRunnableList.clone()).iterator();
        while (it.hasNext()) {
            DownloadRunnable downloadRunnable = (DownloadRunnable) it.next();
            if (downloadRunnable != null) {
                downloadRunnable.pause();
            }
        }
    }

    public void pending() {
        inspectTaskModelResumeAvailableOnDB(this.database.findConnectionModel(this.model.getId()));
        this.statusCallback.onPending();
    }

    @Override // java.lang.Runnable
    public void run() {
        List<ConnectionModel> findConnectionModel;
        try {
            Process.setThreadPriority(10);
            if (this.model.getStatus() != 1) {
                if (this.model.getStatus() != -2) {
                    onError(new RuntimeException(FileDownloadUtils.formatString("Task[%d] can't start the download runnable, because its status is %d not %d", Integer.valueOf(this.model.getId()), Byte.valueOf(this.model.getStatus()), (byte) 1)));
                } else if (FileDownloadLog.NEED_LOG) {
                    FileDownloadLog.d(this, "High concurrent cause, start runnable but already paused %d", Integer.valueOf(this.model.getId()));
                }
                this.statusCallback.discardAllMessage();
                if (this.paused) {
                    this.statusCallback.onPausedDirectly();
                } else if (this.error) {
                    this.statusCallback.onErrorDirectly(this.errorException);
                } else {
                    try {
                        this.statusCallback.onCompletedDirectly();
                    } catch (IOException e) {
                        this.statusCallback.onErrorDirectly(e);
                    }
                }
                this.alive.set(false);
                return;
            }
            if (!this.paused) {
                this.statusCallback.onStartThread();
            }
            while (!this.paused) {
                try {
                    try {
                        checkupBeforeConnect();
                        trialConnect();
                        checkupAfterGetFilename();
                        findConnectionModel = this.database.findConnectionModel(this.model.getId());
                        inspectTaskModelResumeAvailableOnDB(findConnectionModel);
                    } catch (FileDownloadGiveUpRetryException | FileDownloadSecurityException | IOException | IllegalAccessException | IllegalArgumentException | InterruptedException e2) {
                        if (isRetry(e2)) {
                            onRetry(e2);
                        } else {
                            onError(e2);
                        }
                    }
                    if (this.paused) {
                        this.model.setStatus((byte) -2);
                        this.statusCallback.discardAllMessage();
                        if (this.paused) {
                            this.statusCallback.onPausedDirectly();
                        } else if (this.error) {
                            this.statusCallback.onErrorDirectly(this.errorException);
                        } else {
                            try {
                                this.statusCallback.onCompletedDirectly();
                            } catch (IOException e3) {
                                this.statusCallback.onErrorDirectly(e3);
                            }
                        }
                        this.alive.set(false);
                        return;
                    }
                    long total = this.model.getTotal();
                    handlePreAllocate(total, this.model.getTempFilePath());
                    int calcConnectionCount = calcConnectionCount(total);
                    if (calcConnectionCount <= 0) {
                        throw new IllegalAccessException(FileDownloadUtils.formatString("invalid connection count %d, the connection count must be larger than 0", Integer.valueOf(calcConnectionCount)));
                    }
                    if (total == 0) {
                        this.statusCallback.discardAllMessage();
                        if (this.paused) {
                            this.statusCallback.onPausedDirectly();
                        } else if (this.error) {
                            this.statusCallback.onErrorDirectly(this.errorException);
                        } else {
                            try {
                                this.statusCallback.onCompletedDirectly();
                            } catch (IOException e4) {
                                this.statusCallback.onErrorDirectly(e4);
                            }
                        }
                        this.alive.set(false);
                        return;
                    }
                    if (this.paused) {
                        this.model.setStatus((byte) -2);
                        this.statusCallback.discardAllMessage();
                        if (this.paused) {
                            this.statusCallback.onPausedDirectly();
                        } else if (this.error) {
                            this.statusCallback.onErrorDirectly(this.errorException);
                        } else {
                            try {
                                this.statusCallback.onCompletedDirectly();
                            } catch (IOException e5) {
                                this.statusCallback.onErrorDirectly(e5);
                            }
                        }
                        this.alive.set(false);
                        return;
                    }
                    this.isSingleConnection = calcConnectionCount == 1;
                    if (this.isSingleConnection) {
                        realDownloadWithSingleConnection(total);
                    } else {
                        this.statusCallback.onMultiConnection();
                        if (this.isResumeAvailableOnDB) {
                            realDownloadWithMultiConnectionFromResume(calcConnectionCount, findConnectionModel);
                        } else {
                            realDownloadWithMultiConnectionFromBeginning(total, calcConnectionCount);
                        }
                    }
                    this.statusCallback.discardAllMessage();
                    if (this.paused) {
                        this.statusCallback.onPausedDirectly();
                    } else if (this.error) {
                        this.statusCallback.onErrorDirectly(this.errorException);
                    } else {
                        try {
                            this.statusCallback.onCompletedDirectly();
                        } catch (IOException e6) {
                            this.statusCallback.onErrorDirectly(e6);
                        }
                    }
                    this.alive.set(false);
                    return;
                } catch (DiscardSafely unused) {
                    this.statusCallback.discardAllMessage();
                    if (this.paused) {
                        this.statusCallback.onPausedDirectly();
                    } else if (this.error) {
                        this.statusCallback.onErrorDirectly(this.errorException);
                    } else {
                        try {
                            this.statusCallback.onCompletedDirectly();
                        } catch (IOException e7) {
                            this.statusCallback.onErrorDirectly(e7);
                        }
                    }
                    this.alive.set(false);
                    return;
                } catch (RetryDirectly unused2) {
                    this.model.setStatus((byte) 5);
                }
            }
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "High concurrent cause, start runnable but already paused %d", Integer.valueOf(this.model.getId()));
            }
            this.statusCallback.discardAllMessage();
            if (this.paused) {
                this.statusCallback.onPausedDirectly();
            } else if (this.error) {
                this.statusCallback.onErrorDirectly(this.errorException);
            } else {
                try {
                    this.statusCallback.onCompletedDirectly();
                } catch (IOException e8) {
                    this.statusCallback.onErrorDirectly(e8);
                }
            }
            this.alive.set(false);
        } catch (Throwable th) {
            this.statusCallback.discardAllMessage();
            if (this.paused) {
                this.statusCallback.onPausedDirectly();
            } else if (this.error) {
                this.statusCallback.onErrorDirectly(this.errorException);
            } else {
                try {
                    this.statusCallback.onCompletedDirectly();
                } catch (IOException e9) {
                    this.statusCallback.onErrorDirectly(e9);
                }
            }
            this.alive.set(false);
            throw th;
        }
    }

    @Override // com.liulishuo.filedownloader.download.ProcessCallback
    public void syncProgressFromCache() {
        this.database.updateProgress(this.model.getId(), this.model.getSoFar());
    }
}
