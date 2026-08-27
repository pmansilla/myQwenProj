package com.liulishuo.filedownloader;

import android.text.TextUtils;
import android.util.SparseArray;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.DownloadTaskHunter;
import com.liulishuo.filedownloader.ITaskHunter;
import com.liulishuo.filedownloader.model.FileDownloadHeader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.io.File;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class DownloadTask implements BaseDownloadTask, BaseDownloadTask.IRunningTask, DownloadTaskHunter.ICaptureTask {
    public static final int DEFAULT_CALLBACK_PROGRESS_MIN_INTERVAL_MILLIS = 10;
    private String mFilename;
    private ArrayList<BaseDownloadTask.FinishListener> mFinishListenerList;
    private FileDownloadHeader mHeader;
    private final ITaskHunter mHunter;
    private int mId;
    private SparseArray<Object> mKeyedTags;
    private FileDownloadListener mListener;
    private final ITaskHunter.IMessageHandler mMessageHandler;
    private String mPath;
    private boolean mPathAsDirectory;
    private Object mTag;
    private final String mUrl;
    private int mAutoRetryTimes = 0;
    private boolean mSyncCallback = false;
    private boolean mIsWifiRequired = false;
    private int mCallbackProgressTimes = 100;
    private int mCallbackProgressMinIntervalMillis = 10;
    private boolean mIsForceReDownload = false;
    volatile int mAttachKey = 0;
    private boolean mIsInQueueTask = false;
    private final Object headerCreateLock = new Object();
    private volatile boolean mIsMarkedAdded2List = false;
    private final Object mPauseLock = new Object();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InQueueTaskImpl implements BaseDownloadTask.InQueueTask {
        private final DownloadTask mTask;

        private InQueueTaskImpl(DownloadTask downloadTask) {
            this.mTask = downloadTask;
            this.mTask.mIsInQueueTask = true;
        }

        @Override // com.liulishuo.filedownloader.BaseDownloadTask.InQueueTask
        public int enqueue() {
            int id = this.mTask.getId();
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "add the task[%d] to the queue", Integer.valueOf(id));
            }
            FileDownloadList.getImpl().addUnchecked(this.mTask);
            return id;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DownloadTask(String str) {
        this.mUrl = str;
        DownloadTaskHunter downloadTaskHunter = new DownloadTaskHunter(this, this.mPauseLock);
        this.mHunter = downloadTaskHunter;
        this.mMessageHandler = downloadTaskHunter;
    }

    private void checkAndCreateHeader() {
        if (this.mHeader == null) {
            synchronized (this.headerCreateLock) {
                if (this.mHeader == null) {
                    this.mHeader = new FileDownloadHeader();
                }
            }
        }
    }

    private int startTaskUnchecked() {
        if (!isUsing()) {
            if (!isAttached()) {
                setAttachKeyDefault();
            }
            this.mHunter.intoLaunchPool();
            return getId();
        }
        if (isRunning()) {
            throw new IllegalStateException(FileDownloadUtils.formatString("This task is running %d, if you want to start the same task, please create a new one by FileDownloader.create", Integer.valueOf(getId())));
        }
        throw new IllegalStateException("This task is dirty to restart, If you want to reuse this task, please invoke #reuse method manually and retry to restart again." + this.mHunter.toString());
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public BaseDownloadTask addFinishListener(BaseDownloadTask.FinishListener finishListener) {
        if (this.mFinishListenerList == null) {
            this.mFinishListenerList = new ArrayList<>();
        }
        if (!this.mFinishListenerList.contains(finishListener)) {
            this.mFinishListenerList.add(finishListener);
        }
        return this;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public BaseDownloadTask addHeader(String str) {
        checkAndCreateHeader();
        this.mHeader.add(str);
        return this;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public BaseDownloadTask addHeader(String str, String str2) {
        checkAndCreateHeader();
        this.mHeader.add(str, str2);
        return this;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public BaseDownloadTask.InQueueTask asInQueueTask() {
        return new InQueueTaskImpl();
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public boolean cancel() {
        return pause();
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask.IRunningTask
    public void free() {
        this.mHunter.free();
        if (FileDownloadList.getImpl().isNotContains(this)) {
            this.mIsMarkedAdded2List = false;
        }
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask.IRunningTask
    public int getAttachKey() {
        return this.mAttachKey;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public int getAutoRetryTimes() {
        return this.mAutoRetryTimes;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public int getCallbackProgressMinInterval() {
        return this.mCallbackProgressMinIntervalMillis;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public int getCallbackProgressTimes() {
        return this.mCallbackProgressTimes;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public int getDownloadId() {
        return getId();
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public Throwable getErrorCause() {
        return this.mHunter.getErrorCause();
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public String getEtag() {
        return this.mHunter.getEtag();
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public Throwable getEx() {
        return getErrorCause();
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public String getFilename() {
        return this.mFilename;
    }

    @Override // com.liulishuo.filedownloader.DownloadTaskHunter.ICaptureTask
    public ArrayList<BaseDownloadTask.FinishListener> getFinishListenerList() {
        return this.mFinishListenerList;
    }

    @Override // com.liulishuo.filedownloader.DownloadTaskHunter.ICaptureTask
    public FileDownloadHeader getHeader() {
        return this.mHeader;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public int getId() {
        if (this.mId != 0) {
            return this.mId;
        }
        if (TextUtils.isEmpty(this.mPath) || TextUtils.isEmpty(this.mUrl)) {
            return 0;
        }
        int generateId = FileDownloadUtils.generateId(this.mUrl, this.mPath, this.mPathAsDirectory);
        this.mId = generateId;
        return generateId;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public long getLargeFileSoFarBytes() {
        return this.mHunter.getSofarBytes();
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public long getLargeFileTotalBytes() {
        return this.mHunter.getTotalBytes();
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public FileDownloadListener getListener() {
        return this.mListener;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask.IRunningTask
    public ITaskHunter.IMessageHandler getMessageHandler() {
        return this.mMessageHandler;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask.IRunningTask
    public BaseDownloadTask getOrigin() {
        return this;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public String getPath() {
        return this.mPath;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask.IRunningTask
    public Object getPauseLock() {
        return this.mPauseLock;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public int getRetryingTimes() {
        return this.mHunter.getRetryingTimes();
    }

    @Override // com.liulishuo.filedownloader.DownloadTaskHunter.ICaptureTask
    public BaseDownloadTask.IRunningTask getRunningTask() {
        return this;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public int getSmallFileSoFarBytes() {
        if (this.mHunter.getSofarBytes() > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) this.mHunter.getSofarBytes();
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public int getSmallFileTotalBytes() {
        if (this.mHunter.getTotalBytes() > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) this.mHunter.getTotalBytes();
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public int getSoFarBytes() {
        return getSmallFileSoFarBytes();
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public int getSpeed() {
        return this.mHunter.getSpeed();
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public byte getStatus() {
        return this.mHunter.getStatus();
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public Object getTag() {
        return this.mTag;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public Object getTag(int i) {
        if (this.mKeyedTags == null) {
            return null;
        }
        return this.mKeyedTags.get(i);
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public String getTargetFilePath() {
        return FileDownloadUtils.getTargetFilePath(getPath(), isPathAsDirectory(), getFilename());
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public int getTotalBytes() {
        return getSmallFileTotalBytes();
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public String getUrl() {
        return this.mUrl;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask.IRunningTask
    public boolean is(int i) {
        return getId() == i;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask.IRunningTask
    public boolean is(FileDownloadListener fileDownloadListener) {
        return getListener() == fileDownloadListener;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public boolean isAttached() {
        return this.mAttachKey != 0;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask.IRunningTask
    public boolean isContainFinishListener() {
        return this.mFinishListenerList != null && this.mFinishListenerList.size() > 0;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public boolean isContinue() {
        return isResuming();
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public boolean isForceReDownload() {
        return this.mIsForceReDownload;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public boolean isLargeFile() {
        return this.mHunter.isLargeFile();
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask.IRunningTask
    public boolean isMarkedAdded2List() {
        return this.mIsMarkedAdded2List;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask.IRunningTask
    public boolean isOver() {
        return FileDownloadStatus.isOver(getStatus());
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public boolean isPathAsDirectory() {
        return this.mPathAsDirectory;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public boolean isResuming() {
        return this.mHunter.isResuming();
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public boolean isReusedOldFile() {
        return this.mHunter.isReusedOldFile();
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public boolean isRunning() {
        if (FileDownloader.getImpl().getLostConnectedHandler().isInWaitingList(this)) {
            return true;
        }
        return FileDownloadStatus.isIng(getStatus());
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public boolean isSyncCallback() {
        return this.mSyncCallback;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public boolean isUsing() {
        return this.mHunter.getStatus() != 0;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public boolean isWifiRequired() {
        return this.mIsWifiRequired;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask.IRunningTask
    public void markAdded2List() {
        this.mIsMarkedAdded2List = true;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public boolean pause() {
        boolean pause;
        synchronized (this.mPauseLock) {
            pause = this.mHunter.pause();
        }
        return pause;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public int ready() {
        return asInQueueTask().enqueue();
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public BaseDownloadTask removeAllHeaders(String str) {
        if (this.mHeader == null) {
            synchronized (this.headerCreateLock) {
                if (this.mHeader == null) {
                    return this;
                }
            }
        }
        this.mHeader.removeAll(str);
        return this;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public boolean removeFinishListener(BaseDownloadTask.FinishListener finishListener) {
        return this.mFinishListenerList != null && this.mFinishListenerList.remove(finishListener);
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public boolean reuse() {
        if (isRunning()) {
            FileDownloadLog.w(this, "This task[%d] is running, if you want start the same task, please create a new one by FileDownloader#create", Integer.valueOf(getId()));
            return false;
        }
        this.mAttachKey = 0;
        this.mIsInQueueTask = false;
        this.mIsMarkedAdded2List = false;
        this.mHunter.reset();
        return true;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask.IRunningTask
    public void setAttachKeyByQueue(int i) {
        this.mAttachKey = i;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask.IRunningTask
    public void setAttachKeyDefault() {
        this.mAttachKey = getListener() != null ? getListener().hashCode() : hashCode();
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public BaseDownloadTask setAutoRetryTimes(int i) {
        this.mAutoRetryTimes = i;
        return this;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public BaseDownloadTask setCallbackProgressIgnored() {
        return setCallbackProgressTimes(-1);
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public BaseDownloadTask setCallbackProgressMinInterval(int i) {
        this.mCallbackProgressMinIntervalMillis = i;
        return this;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public BaseDownloadTask setCallbackProgressTimes(int i) {
        this.mCallbackProgressTimes = i;
        return this;
    }

    @Override // com.liulishuo.filedownloader.DownloadTaskHunter.ICaptureTask
    public void setFileName(String str) {
        this.mFilename = str;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public BaseDownloadTask setFinishListener(BaseDownloadTask.FinishListener finishListener) {
        addFinishListener(finishListener);
        return this;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public BaseDownloadTask setForceReDownload(boolean z) {
        this.mIsForceReDownload = z;
        return this;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public BaseDownloadTask setListener(FileDownloadListener fileDownloadListener) {
        this.mListener = fileDownloadListener;
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "setListener %s", fileDownloadListener);
        }
        return this;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public BaseDownloadTask setMinIntervalUpdateSpeed(int i) {
        this.mHunter.setMinIntervalUpdateSpeed(i);
        return this;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public BaseDownloadTask setPath(String str) {
        return setPath(str, false);
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public BaseDownloadTask setPath(String str, boolean z) {
        this.mPath = str;
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "setPath %s", str);
        }
        this.mPathAsDirectory = z;
        if (z) {
            this.mFilename = null;
        } else {
            this.mFilename = new File(str).getName();
        }
        return this;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public BaseDownloadTask setSyncCallback(boolean z) {
        this.mSyncCallback = z;
        return this;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public BaseDownloadTask setTag(int i, Object obj) {
        if (this.mKeyedTags == null) {
            this.mKeyedTags = new SparseArray<>(2);
        }
        this.mKeyedTags.put(i, obj);
        return this;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public BaseDownloadTask setTag(Object obj) {
        this.mTag = obj;
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "setTag %s", obj);
        }
        return this;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public BaseDownloadTask setWifiRequired(boolean z) {
        this.mIsWifiRequired = z;
        return this;
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask
    public int start() {
        if (this.mIsInQueueTask) {
            throw new IllegalStateException("If you start the task manually, it means this task doesn't belong to a queue, so you must not invoke BaseDownloadTask#ready() or InQueueTask#enqueue() before you start() this method. For detail: If this task doesn't belong to a queue, what is just an isolated task, you just need to invoke BaseDownloadTask#start() to start this task, that's all. In other words, If this task doesn't belong to a queue, you must not invoke BaseDownloadTask#ready() method or InQueueTask#enqueue() method before invoke BaseDownloadTask#start(), If you do that and if there is the same listener object to start a queue in another thread, this task may be assembled by the queue, in that case, when you invoke BaseDownloadTask#start() manually to start this task or this task is started by the queue, there is an exception buried in there, because this task object is started two times without declare BaseDownloadTask#reuse() : 1. you invoke BaseDownloadTask#start() manually;  2. the queue start this task automatically.");
        }
        return startTaskUnchecked();
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask.IRunningTask
    public void startTaskByQueue() {
        startTaskUnchecked();
    }

    @Override // com.liulishuo.filedownloader.BaseDownloadTask.IRunningTask
    public void startTaskByRescue() {
        startTaskUnchecked();
    }

    public String toString() {
        return FileDownloadUtils.formatString("%d@%s", Integer.valueOf(getId()), super.toString());
    }
}
