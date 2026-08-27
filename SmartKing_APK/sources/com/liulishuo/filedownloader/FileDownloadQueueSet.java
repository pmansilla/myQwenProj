package com.liulishuo.filedownloader;

import com.liulishuo.filedownloader.BaseDownloadTask;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class FileDownloadQueueSet {
    private Integer autoRetryTimes;
    private Integer callbackProgressMinIntervalMillis;
    private Integer callbackProgressTimes;
    private String directory;
    private Boolean isForceReDownload;
    private boolean isSerial;
    private Boolean isWifiRequired;
    private Boolean syncCallback;
    private Object tag;
    private FileDownloadListener target;
    private List<BaseDownloadTask.FinishListener> taskFinishListenerList;
    private BaseDownloadTask[] tasks;

    public FileDownloadQueueSet(FileDownloadListener fileDownloadListener) {
        if (fileDownloadListener == null) {
            throw new IllegalArgumentException("create FileDownloadQueueSet must with valid target!");
        }
        this.target = fileDownloadListener;
    }

    public FileDownloadQueueSet addTaskFinishListener(BaseDownloadTask.FinishListener finishListener) {
        if (this.taskFinishListenerList == null) {
            this.taskFinishListenerList = new ArrayList();
        }
        this.taskFinishListenerList.add(finishListener);
        return this;
    }

    public FileDownloadQueueSet disableCallbackProgressTimes() {
        return setCallbackProgressTimes(0);
    }

    public FileDownloadQueueSet downloadSequentially(List<BaseDownloadTask> list) {
        this.isSerial = true;
        this.tasks = new BaseDownloadTask[list.size()];
        list.toArray(this.tasks);
        return this;
    }

    public FileDownloadQueueSet downloadSequentially(BaseDownloadTask... baseDownloadTaskArr) {
        this.isSerial = true;
        this.tasks = baseDownloadTaskArr;
        return this;
    }

    public FileDownloadQueueSet downloadTogether(List<BaseDownloadTask> list) {
        this.isSerial = false;
        this.tasks = new BaseDownloadTask[list.size()];
        list.toArray(this.tasks);
        return this;
    }

    public FileDownloadQueueSet downloadTogether(BaseDownloadTask... baseDownloadTaskArr) {
        this.isSerial = false;
        this.tasks = baseDownloadTaskArr;
        return this;
    }

    public FileDownloadQueueSet ignoreEachTaskInternalProgress() {
        setCallbackProgressTimes(-1);
        return this;
    }

    public void reuseAndStart() {
        for (BaseDownloadTask baseDownloadTask : this.tasks) {
            baseDownloadTask.reuse();
        }
        start();
    }

    public FileDownloadQueueSet setAutoRetryTimes(int i) {
        this.autoRetryTimes = Integer.valueOf(i);
        return this;
    }

    public FileDownloadQueueSet setCallbackProgressMinInterval(int i) {
        this.callbackProgressMinIntervalMillis = Integer.valueOf(i);
        return this;
    }

    public FileDownloadQueueSet setCallbackProgressTimes(int i) {
        this.callbackProgressTimes = Integer.valueOf(i);
        return this;
    }

    public FileDownloadQueueSet setDirectory(String str) {
        this.directory = str;
        return this;
    }

    public FileDownloadQueueSet setForceReDownload(boolean z) {
        this.isForceReDownload = Boolean.valueOf(z);
        return this;
    }

    public FileDownloadQueueSet setSyncCallback(boolean z) {
        this.syncCallback = Boolean.valueOf(z);
        return this;
    }

    public FileDownloadQueueSet setTag(Object obj) {
        this.tag = obj;
        return this;
    }

    public FileDownloadQueueSet setWifiRequired(boolean z) {
        this.isWifiRequired = Boolean.valueOf(z);
        return this;
    }

    public void start() {
        for (BaseDownloadTask baseDownloadTask : this.tasks) {
            baseDownloadTask.setListener(this.target);
            if (this.autoRetryTimes != null) {
                baseDownloadTask.setAutoRetryTimes(this.autoRetryTimes.intValue());
            }
            if (this.syncCallback != null) {
                baseDownloadTask.setSyncCallback(this.syncCallback.booleanValue());
            }
            if (this.isForceReDownload != null) {
                baseDownloadTask.setForceReDownload(this.isForceReDownload.booleanValue());
            }
            if (this.callbackProgressTimes != null) {
                baseDownloadTask.setCallbackProgressTimes(this.callbackProgressTimes.intValue());
            }
            if (this.callbackProgressMinIntervalMillis != null) {
                baseDownloadTask.setCallbackProgressMinInterval(this.callbackProgressMinIntervalMillis.intValue());
            }
            if (this.tag != null) {
                baseDownloadTask.setTag(this.tag);
            }
            if (this.taskFinishListenerList != null) {
                Iterator<BaseDownloadTask.FinishListener> it = this.taskFinishListenerList.iterator();
                while (it.hasNext()) {
                    baseDownloadTask.addFinishListener(it.next());
                }
            }
            if (this.directory != null) {
                baseDownloadTask.setPath(this.directory, true);
            }
            if (this.isWifiRequired != null) {
                baseDownloadTask.setWifiRequired(this.isWifiRequired.booleanValue());
            }
            baseDownloadTask.asInQueueTask().enqueue();
        }
        FileDownloader.getImpl().start(this.target, this.isSerial);
    }
}
