package com.liulishuo.filedownloader.services;

import android.util.SparseArray;
import com.liulishuo.filedownloader.download.DownloadLaunchRunnable;
import com.liulishuo.filedownloader.util.FileDownloadExecutors;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/* loaded from: classes.dex */
class FileDownloadThreadPool {
    private int mMaxThreadCount;
    private ThreadPoolExecutor mThreadPool;
    private SparseArray<DownloadLaunchRunnable> runnablePool = new SparseArray<>();
    private final String threadPrefix = "Network";
    private int mIgnoreCheckTimes = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FileDownloadThreadPool(int i) {
        this.mThreadPool = FileDownloadExecutors.newDefaultThreadPool(i, "Network");
        this.mMaxThreadCount = i;
    }

    private synchronized void filterOutNoExist() {
        SparseArray<DownloadLaunchRunnable> sparseArray = new SparseArray<>();
        int size = this.runnablePool.size();
        for (int i = 0; i < size; i++) {
            int keyAt = this.runnablePool.keyAt(i);
            DownloadLaunchRunnable downloadLaunchRunnable = this.runnablePool.get(keyAt);
            if (downloadLaunchRunnable != null && downloadLaunchRunnable.isAlive()) {
                sparseArray.put(keyAt, downloadLaunchRunnable);
            }
        }
        this.runnablePool = sparseArray;
    }

    public void cancel(int i) {
        filterOutNoExist();
        synchronized (this) {
            DownloadLaunchRunnable downloadLaunchRunnable = this.runnablePool.get(i);
            if (downloadLaunchRunnable != null) {
                downloadLaunchRunnable.pause();
                boolean remove = this.mThreadPool.remove(downloadLaunchRunnable);
                if (FileDownloadLog.NEED_LOG) {
                    FileDownloadLog.d(this, "successful cancel %d %B", Integer.valueOf(i), Boolean.valueOf(remove));
                }
            }
            this.runnablePool.remove(i);
        }
    }

    public synchronized int exactSize() {
        filterOutNoExist();
        return this.runnablePool.size();
    }

    public void execute(DownloadLaunchRunnable downloadLaunchRunnable) {
        downloadLaunchRunnable.pending();
        synchronized (this) {
            this.runnablePool.put(downloadLaunchRunnable.getId(), downloadLaunchRunnable);
        }
        this.mThreadPool.execute(downloadLaunchRunnable);
        if (this.mIgnoreCheckTimes < 600) {
            this.mIgnoreCheckTimes++;
        } else {
            filterOutNoExist();
            this.mIgnoreCheckTimes = 0;
        }
    }

    public int findRunningTaskIdBySameTempPath(String str, int i) {
        if (str == null) {
            return 0;
        }
        int size = this.runnablePool.size();
        for (int i2 = 0; i2 < size; i2++) {
            DownloadLaunchRunnable valueAt = this.runnablePool.valueAt(i2);
            if (valueAt != null && valueAt.isAlive() && valueAt.getId() != i && str.equals(valueAt.getTempFilePath())) {
                return valueAt.getId();
            }
        }
        return 0;
    }

    public synchronized List<Integer> getAllExactRunningDownloadIds() {
        ArrayList arrayList;
        filterOutNoExist();
        arrayList = new ArrayList();
        for (int i = 0; i < this.runnablePool.size(); i++) {
            arrayList.add(Integer.valueOf(this.runnablePool.get(this.runnablePool.keyAt(i)).getId()));
        }
        return arrayList;
    }

    public boolean isInThreadPool(int i) {
        DownloadLaunchRunnable downloadLaunchRunnable = this.runnablePool.get(i);
        return downloadLaunchRunnable != null && downloadLaunchRunnable.isAlive();
    }

    public synchronized boolean setMaxNetworkThreadCount(int i) {
        if (exactSize() > 0) {
            FileDownloadLog.w(this, "Can't change the max network thread count, because the  network thread pool isn't in IDLE, please try again after all running tasks are completed or invoking FileDownloader#pauseAll directly.", new Object[0]);
            return false;
        }
        int validNetworkThreadCount = FileDownloadProperties.getValidNetworkThreadCount(i);
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "change the max network thread count, from %d to %d", Integer.valueOf(this.mMaxThreadCount), Integer.valueOf(validNetworkThreadCount));
        }
        List<Runnable> shutdownNow = this.mThreadPool.shutdownNow();
        this.mThreadPool = FileDownloadExecutors.newDefaultThreadPool(validNetworkThreadCount, "Network");
        if (shutdownNow.size() > 0) {
            FileDownloadLog.w(this, "recreate the network thread pool and discard %d tasks", Integer.valueOf(shutdownNow.size()));
        }
        this.mMaxThreadCount = validNetworkThreadCount;
        return true;
    }
}
