package com.liulishuo.filedownloader;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.IDownloadSpeed;
import com.liulishuo.filedownloader.ITaskHunter;
import com.liulishuo.filedownloader.message.MessageSnapshot;
import com.liulishuo.filedownloader.message.MessageSnapshotTaker;
import com.liulishuo.filedownloader.model.FileDownloadHeader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class DownloadTaskHunter implements ITaskHunter, ITaskHunter.IStarter, ITaskHunter.IMessageHandler, BaseDownloadTask.LifeCycleCallback {
    private String mEtag;
    private boolean mIsLargeFile;
    private boolean mIsResuming;
    private IFileDownloadMessenger mMessenger;
    private final Object mPauseLock;
    private int mRetryingTimes;
    private long mSoFarBytes;
    private final IDownloadSpeed.Lookup mSpeedLookup;
    private final IDownloadSpeed.Monitor mSpeedMonitor;
    private final ICaptureTask mTask;
    private long mTotalBytes;
    private volatile byte mStatus = 0;
    private Throwable mThrowable = null;
    private boolean mIsReusedOldFile = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface ICaptureTask {
        ArrayList<BaseDownloadTask.FinishListener> getFinishListenerList();

        FileDownloadHeader getHeader();

        BaseDownloadTask.IRunningTask getRunningTask();

        void setFileName(String str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DownloadTaskHunter(ICaptureTask iCaptureTask, Object obj) {
        this.mPauseLock = obj;
        this.mTask = iCaptureTask;
        DownloadSpeedMonitor downloadSpeedMonitor = new DownloadSpeedMonitor();
        this.mSpeedMonitor = downloadSpeedMonitor;
        this.mSpeedLookup = downloadSpeedMonitor;
        this.mMessenger = new FileDownloadMessenger(iCaptureTask.getRunningTask(), this);
    }

    private int getId() {
        return this.mTask.getRunningTask().getOrigin().getId();
    }

    private void prepare() throws IOException {
        File file;
        BaseDownloadTask origin = this.mTask.getRunningTask().getOrigin();
        if (origin.getPath() == null) {
            origin.setPath(FileDownloadUtils.getDefaultSaveFilePath(origin.getUrl()));
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "save Path is null to %s", origin.getPath());
            }
        }
        if (origin.isPathAsDirectory()) {
            file = new File(origin.getPath());
        } else {
            String parent = FileDownloadUtils.getParent(origin.getPath());
            if (parent == null) {
                throw new InvalidParameterException(FileDownloadUtils.formatString("the provided mPath[%s] is invalid, can't find its directory", origin.getPath()));
            }
            file = new File(parent);
        }
        if (!file.exists() && !file.mkdirs() && !file.exists()) {
            throw new IOException(FileDownloadUtils.formatString("Create parent directory failed, please make sure you have permission to create file or directory on the path: %s", file.getAbsolutePath()));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void update(MessageSnapshot messageSnapshot) {
        BaseDownloadTask origin = this.mTask.getRunningTask().getOrigin();
        byte status = messageSnapshot.getStatus();
        this.mStatus = status;
        this.mIsLargeFile = messageSnapshot.isLargeFile();
        switch (status) {
            case -4:
                this.mSpeedMonitor.reset();
                int count = FileDownloadList.getImpl().count(origin.getId());
                if (count + ((count > 1 || !origin.isPathAsDirectory()) ? 0 : FileDownloadList.getImpl().count(FileDownloadUtils.generateId(origin.getUrl(), origin.getTargetFilePath()))) <= 1) {
                    byte status2 = FileDownloadServiceProxy.getImpl().getStatus(origin.getId());
                    FileDownloadLog.w(this, "warn, but no mListener to receive, switch to pending %d %d", Integer.valueOf(origin.getId()), Integer.valueOf(status2));
                    if (FileDownloadStatus.isIng(status2)) {
                        this.mStatus = (byte) 1;
                        this.mTotalBytes = messageSnapshot.getLargeTotalBytes();
                        this.mSoFarBytes = messageSnapshot.getLargeSofarBytes();
                        this.mSpeedMonitor.start(this.mSoFarBytes);
                        this.mMessenger.notifyPending(((MessageSnapshot.IWarnMessageSnapshot) messageSnapshot).turnToPending());
                        return;
                    }
                }
                FileDownloadList.getImpl().remove(this.mTask.getRunningTask(), messageSnapshot);
                return;
            case -3:
                this.mIsReusedOldFile = messageSnapshot.isReusedDownloadedFile();
                this.mSoFarBytes = messageSnapshot.getLargeTotalBytes();
                this.mTotalBytes = messageSnapshot.getLargeTotalBytes();
                FileDownloadList.getImpl().remove(this.mTask.getRunningTask(), messageSnapshot);
                return;
            case -2:
            case 0:
            case 4:
            default:
                return;
            case -1:
                this.mThrowable = messageSnapshot.getThrowable();
                this.mSoFarBytes = messageSnapshot.getLargeSofarBytes();
                FileDownloadList.getImpl().remove(this.mTask.getRunningTask(), messageSnapshot);
                return;
            case 1:
                this.mSoFarBytes = messageSnapshot.getLargeSofarBytes();
                this.mTotalBytes = messageSnapshot.getLargeTotalBytes();
                this.mMessenger.notifyPending(messageSnapshot);
                return;
            case 2:
                this.mTotalBytes = messageSnapshot.getLargeTotalBytes();
                this.mIsResuming = messageSnapshot.isResuming();
                this.mEtag = messageSnapshot.getEtag();
                String fileName = messageSnapshot.getFileName();
                if (fileName != null) {
                    if (origin.getFilename() != null) {
                        FileDownloadLog.w(this, "already has mFilename[%s], but assign mFilename[%s] again", origin.getFilename(), fileName);
                    }
                    this.mTask.setFileName(fileName);
                }
                this.mSpeedMonitor.start(this.mSoFarBytes);
                this.mMessenger.notifyConnected(messageSnapshot);
                return;
            case 3:
                this.mSoFarBytes = messageSnapshot.getLargeSofarBytes();
                this.mSpeedMonitor.update(messageSnapshot.getLargeSofarBytes());
                this.mMessenger.notifyProgress(messageSnapshot);
                return;
            case 5:
                this.mSoFarBytes = messageSnapshot.getLargeSofarBytes();
                this.mThrowable = messageSnapshot.getThrowable();
                this.mRetryingTimes = messageSnapshot.getRetryingTimes();
                this.mSpeedMonitor.reset();
                this.mMessenger.notifyRetry(messageSnapshot);
                return;
            case 6:
                this.mMessenger.notifyStarted(messageSnapshot);
                return;
        }
    }

    @Override // com.liulishuo.filedownloader.ITaskHunter.IStarter
    public boolean equalListener(FileDownloadListener fileDownloadListener) {
        return this.mTask.getRunningTask().getOrigin().getListener() == fileDownloadListener;
    }

    @Override // com.liulishuo.filedownloader.ITaskHunter
    public void free() {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "free the task %d, when the status is %d", Integer.valueOf(getId()), Byte.valueOf(this.mStatus));
        }
        this.mStatus = (byte) 0;
    }

    @Override // com.liulishuo.filedownloader.ITaskHunter
    public Throwable getErrorCause() {
        return this.mThrowable;
    }

    @Override // com.liulishuo.filedownloader.ITaskHunter
    public String getEtag() {
        return this.mEtag;
    }

    @Override // com.liulishuo.filedownloader.ITaskHunter.IMessageHandler
    public IFileDownloadMessenger getMessenger() {
        return this.mMessenger;
    }

    @Override // com.liulishuo.filedownloader.ITaskHunter
    public int getRetryingTimes() {
        return this.mRetryingTimes;
    }

    @Override // com.liulishuo.filedownloader.ITaskHunter
    public long getSofarBytes() {
        return this.mSoFarBytes;
    }

    @Override // com.liulishuo.filedownloader.IDownloadSpeed.Lookup
    public int getSpeed() {
        return this.mSpeedLookup.getSpeed();
    }

    @Override // com.liulishuo.filedownloader.ITaskHunter
    public byte getStatus() {
        return this.mStatus;
    }

    @Override // com.liulishuo.filedownloader.ITaskHunter
    public long getTotalBytes() {
        return this.mTotalBytes;
    }

    @Override // com.liulishuo.filedownloader.ITaskHunter
    public void intoLaunchPool() {
        boolean z;
        synchronized (this.mPauseLock) {
            if (this.mStatus != 0) {
                FileDownloadLog.w(this, "High concurrent cause, this task %d will not input to launch pool, because of the status isn't idle : %d", Integer.valueOf(getId()), Byte.valueOf(this.mStatus));
                return;
            }
            this.mStatus = (byte) 10;
            BaseDownloadTask.IRunningTask runningTask = this.mTask.getRunningTask();
            BaseDownloadTask origin = runningTask.getOrigin();
            if (FileDownloadMonitor.isValid()) {
                FileDownloadMonitor.getMonitor().onRequestStart(origin);
            }
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.v(this, "call start Url[%s], Path[%s] Listener[%s], Tag[%s]", origin.getUrl(), origin.getPath(), origin.getListener(), origin.getTag());
            }
            try {
                prepare();
                z = true;
            } catch (Throwable th) {
                FileDownloadList.getImpl().add(runningTask);
                FileDownloadList.getImpl().remove(runningTask, prepareErrorMessage(th));
                z = false;
            }
            if (z) {
                FileDownloadTaskLauncher.getImpl().launch(this);
            }
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.v(this, "the task[%d] has been into the launch pool.", Integer.valueOf(getId()));
            }
        }
    }

    @Override // com.liulishuo.filedownloader.ITaskHunter
    public boolean isLargeFile() {
        return this.mIsLargeFile;
    }

    @Override // com.liulishuo.filedownloader.ITaskHunter
    public boolean isResuming() {
        return this.mIsResuming;
    }

    @Override // com.liulishuo.filedownloader.ITaskHunter
    public boolean isReusedOldFile() {
        return this.mIsReusedOldFile;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask.LifeCycleCallback
    public void onBegin() {
        if (FileDownloadMonitor.isValid()) {
            FileDownloadMonitor.getMonitor().onTaskBegin(this.mTask.getRunningTask().getOrigin());
        }
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.v(this, "filedownloader:lifecycle:start %s by %d ", toString(), Byte.valueOf(getStatus()));
        }
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask.LifeCycleCallback
    public void onIng() {
        if (FileDownloadMonitor.isValid() && getStatus() == 6) {
            FileDownloadMonitor.getMonitor().onTaskStarted(this.mTask.getRunningTask().getOrigin());
        }
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask.LifeCycleCallback
    public void onOver() {
        BaseDownloadTask origin = this.mTask.getRunningTask().getOrigin();
        if (FileDownloadMonitor.isValid()) {
            FileDownloadMonitor.getMonitor().onTaskOver(origin);
        }
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.v(this, "filedownloader:lifecycle:over %s by %d ", toString(), Byte.valueOf(getStatus()));
        }
        this.mSpeedMonitor.end(this.mSoFarBytes);
        if (this.mTask.getFinishListenerList() != null) {
            ArrayList arrayList = (ArrayList) this.mTask.getFinishListenerList().clone();
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                ((BaseDownloadTask.FinishListener) arrayList.get(i)).over(origin);
            }
        }
        FileDownloader.getImpl().getLostConnectedHandler().taskWorkFine(this.mTask.getRunningTask());
    }

    @Override // com.liulishuo.filedownloader.ITaskHunter
    public boolean pause() {
        if (FileDownloadStatus.isOver(getStatus())) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "High concurrent cause, Already is over, can't pause again, %d %d", Byte.valueOf(getStatus()), Integer.valueOf(this.mTask.getRunningTask().getOrigin().getId()));
            }
            return false;
        }
        this.mStatus = (byte) -2;
        BaseDownloadTask.IRunningTask runningTask = this.mTask.getRunningTask();
        BaseDownloadTask origin = runningTask.getOrigin();
        FileDownloadTaskLauncher.getImpl().expire(this);
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.v(this, "the task[%d] has been expired from the launch pool.", Integer.valueOf(getId()));
        }
        if (FileDownloader.getImpl().isServiceConnected()) {
            FileDownloadServiceProxy.getImpl().pause(origin.getId());
        } else if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "request pause the task[%d] to the download service, but the download service isn't connected yet.", Integer.valueOf(origin.getId()));
        }
        FileDownloadList.getImpl().add(runningTask);
        FileDownloadList.getImpl().remove(runningTask, MessageSnapshotTaker.catchPause(origin));
        FileDownloader.getImpl().getLostConnectedHandler().taskWorkFine(runningTask);
        return true;
    }

    @Override // com.liulishuo.filedownloader.ITaskHunter.IMessageHandler
    public MessageSnapshot prepareErrorMessage(Throwable th) {
        this.mStatus = (byte) -1;
        this.mThrowable = th;
        return MessageSnapshotTaker.catchException(getId(), getSofarBytes(), th);
    }

    @Override // com.liulishuo.filedownloader.ITaskHunter
    public void reset() {
        this.mThrowable = null;
        this.mEtag = null;
        this.mIsResuming = false;
        this.mRetryingTimes = 0;
        this.mIsReusedOldFile = false;
        this.mIsLargeFile = false;
        this.mSoFarBytes = 0L;
        this.mTotalBytes = 0L;
        this.mSpeedMonitor.reset();
        if (FileDownloadStatus.isOver(this.mStatus)) {
            this.mMessenger.discard();
            this.mMessenger = new FileDownloadMessenger(this.mTask.getRunningTask(), this);
        } else {
            this.mMessenger.reAppointment(this.mTask.getRunningTask(), this);
        }
        this.mStatus = (byte) 0;
    }

    @Override // com.liulishuo.filedownloader.IDownloadSpeed.Lookup
    public void setMinIntervalUpdateSpeed(int i) {
        this.mSpeedLookup.setMinIntervalUpdateSpeed(i);
    }

    @Override // com.liulishuo.filedownloader.ITaskHunter.IStarter
    public void start() {
        if (this.mStatus != 10) {
            FileDownloadLog.w(this, "High concurrent cause, this task %d will not start, because the of status isn't toLaunchPool: %d", Integer.valueOf(getId()), Byte.valueOf(this.mStatus));
            return;
        }
        BaseDownloadTask.IRunningTask runningTask = this.mTask.getRunningTask();
        BaseDownloadTask origin = runningTask.getOrigin();
        ILostServiceConnectedHandler lostConnectedHandler = FileDownloader.getImpl().getLostConnectedHandler();
        try {
            if (lostConnectedHandler.dispatchTaskStart(runningTask)) {
                return;
            }
            synchronized (this.mPauseLock) {
                if (this.mStatus != 10) {
                    FileDownloadLog.w(this, "High concurrent cause, this task %d will not start, the status can't assign to toFileDownloadService, because the status isn't toLaunchPool: %d", Integer.valueOf(getId()), Byte.valueOf(this.mStatus));
                    return;
                }
                this.mStatus = FileDownloadStatus.toFileDownloadService;
                FileDownloadList.getImpl().add(runningTask);
                if (FileDownloadHelper.inspectAndInflowDownloaded(origin.getId(), origin.getTargetFilePath(), origin.isForceReDownload(), true)) {
                    return;
                }
                boolean start = FileDownloadServiceProxy.getImpl().start(origin.getUrl(), origin.getPath(), origin.isPathAsDirectory(), origin.getCallbackProgressTimes(), origin.getCallbackProgressMinInterval(), origin.getAutoRetryTimes(), origin.isForceReDownload(), this.mTask.getHeader(), origin.isWifiRequired());
                if (this.mStatus == -2) {
                    FileDownloadLog.w(this, "High concurrent cause, this task %d will be paused,because of the status is paused, so the pause action must be applied", Integer.valueOf(getId()));
                    if (start) {
                        FileDownloadServiceProxy.getImpl().pause(getId());
                        return;
                    }
                    return;
                }
                if (start) {
                    lostConnectedHandler.taskWorkFine(runningTask);
                    return;
                }
                if (lostConnectedHandler.dispatchTaskStart(runningTask)) {
                    return;
                }
                MessageSnapshot prepareErrorMessage = prepareErrorMessage(new RuntimeException("Occur Unknown Error, when request to start maybe some problem in binder, maybe the process was killed in unexpected."));
                if (FileDownloadList.getImpl().isNotContains(runningTask)) {
                    lostConnectedHandler.taskWorkFine(runningTask);
                    FileDownloadList.getImpl().add(runningTask);
                }
                FileDownloadList.getImpl().remove(runningTask, prepareErrorMessage);
            }
        } catch (Throwable th) {
            th.printStackTrace();
            FileDownloadList.getImpl().remove(runningTask, prepareErrorMessage(th));
        }
    }

    @Override // com.liulishuo.filedownloader.ITaskHunter.IMessageHandler
    public boolean updateKeepAhead(MessageSnapshot messageSnapshot) {
        if (FileDownloadStatus.isKeepAhead(getStatus(), messageSnapshot.getStatus())) {
            update(messageSnapshot);
            return true;
        }
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "can't update mStatus change by keep ahead, %d, but the current mStatus is %d, %d", Byte.valueOf(this.mStatus), Byte.valueOf(getStatus()), Integer.valueOf(getId()));
        }
        return false;
    }

    @Override // com.liulishuo.filedownloader.ITaskHunter.IMessageHandler
    public boolean updateKeepFlow(MessageSnapshot messageSnapshot) {
        byte status = getStatus();
        byte status2 = messageSnapshot.getStatus();
        if (-2 == status && FileDownloadStatus.isIng(status2)) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "High concurrent cause, callback pending, but has already be paused %d", Integer.valueOf(getId()));
            }
            return true;
        }
        if (FileDownloadStatus.isKeepFlow(status, status2)) {
            update(messageSnapshot);
            return true;
        }
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "can't update mStatus change by keep flow, %d, but the current mStatus is %d, %d", Byte.valueOf(this.mStatus), Byte.valueOf(getStatus()), Integer.valueOf(getId()));
        }
        return false;
    }

    @Override // com.liulishuo.filedownloader.ITaskHunter.IMessageHandler
    public boolean updateMoreLikelyCompleted(MessageSnapshot messageSnapshot) {
        if (!FileDownloadStatus.isMoreLikelyCompleted(this.mTask.getRunningTask().getOrigin())) {
            return false;
        }
        update(messageSnapshot);
        return true;
    }

    @Override // com.liulishuo.filedownloader.ITaskHunter.IMessageHandler
    public boolean updateSameFilePathTaskRunning(MessageSnapshot messageSnapshot) {
        if (!this.mTask.getRunningTask().getOrigin().isPathAsDirectory() || messageSnapshot.getStatus() != -4 || getStatus() != 2) {
            return false;
        }
        update(messageSnapshot);
        return true;
    }
}
