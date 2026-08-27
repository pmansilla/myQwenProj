package com.liulishuo.filedownloader;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.event.DownloadServiceConnectChangedEvent;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class LostServiceConnectedHandler extends FileDownloadConnectListener implements ILostServiceConnectedHandler {
    private final ArrayList<BaseDownloadTask.IRunningTask> mWaitingList = new ArrayList<>();

    @Override // com.liulishuo.filedownloader.FileDownloadConnectListener
    public void connected() {
        IQueuesHandler queuesHandler = FileDownloader.getImpl().getQueuesHandler();
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "The downloader service is connected.", new Object[0]);
        }
        synchronized (this.mWaitingList) {
            List<BaseDownloadTask.IRunningTask> list = (List) this.mWaitingList.clone();
            this.mWaitingList.clear();
            ArrayList arrayList = new ArrayList(queuesHandler.serialQueueSize());
            for (BaseDownloadTask.IRunningTask iRunningTask : list) {
                int attachKey = iRunningTask.getAttachKey();
                if (queuesHandler.contain(attachKey)) {
                    iRunningTask.getOrigin().asInQueueTask().enqueue();
                    if (!arrayList.contains(Integer.valueOf(attachKey))) {
                        arrayList.add(Integer.valueOf(attachKey));
                    }
                } else {
                    iRunningTask.startTaskByRescue();
                }
            }
            queuesHandler.unFreezeSerialQueues(arrayList);
        }
    }

    @Override // com.liulishuo.filedownloader.FileDownloadConnectListener
    public void disconnected() {
        if (getConnectStatus() != DownloadServiceConnectChangedEvent.ConnectStatus.lost) {
            if (FileDownloadList.getImpl().size() > 0) {
                FileDownloadLog.w(this, "file download service has be unbound but the size of active tasks are not empty %d ", Integer.valueOf(FileDownloadList.getImpl().size()));
                return;
            }
            return;
        }
        IQueuesHandler queuesHandler = FileDownloader.getImpl().getQueuesHandler();
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "lost the connection to the file download service, and current active task size is %d", Integer.valueOf(FileDownloadList.getImpl().size()));
        }
        if (FileDownloadList.getImpl().size() > 0) {
            synchronized (this.mWaitingList) {
                FileDownloadList.getImpl().divertAndIgnoreDuplicate(this.mWaitingList);
                Iterator<BaseDownloadTask.IRunningTask> it = this.mWaitingList.iterator();
                while (it.hasNext()) {
                    it.next().free();
                }
                queuesHandler.freezeAllSerialQueues();
            }
            try {
                FileDownloader.getImpl().bindService();
            } catch (IllegalStateException unused) {
                FileDownloadLog.w(this, "restart service failed, you may need to restart downloading manually when the app comes back to foreground", new Object[0]);
            }
        }
    }

    @Override // com.liulishuo.filedownloader.ILostServiceConnectedHandler
    public boolean dispatchTaskStart(BaseDownloadTask.IRunningTask iRunningTask) {
        if (!FileDownloader.getImpl().isServiceConnected()) {
            synchronized (this.mWaitingList) {
                if (!FileDownloader.getImpl().isServiceConnected()) {
                    if (FileDownloadLog.NEED_LOG) {
                        FileDownloadLog.d(this, "Waiting for connecting with the downloader service... %d", Integer.valueOf(iRunningTask.getOrigin().getId()));
                    }
                    FileDownloadServiceProxy.getImpl().bindStartByContext(FileDownloadHelper.getAppContext());
                    if (!this.mWaitingList.contains(iRunningTask)) {
                        iRunningTask.free();
                        this.mWaitingList.add(iRunningTask);
                    }
                    return true;
                }
            }
        }
        taskWorkFine(iRunningTask);
        return false;
    }

    @Override // com.liulishuo.filedownloader.ILostServiceConnectedHandler
    public boolean isInWaitingList(BaseDownloadTask.IRunningTask iRunningTask) {
        return !this.mWaitingList.isEmpty() && this.mWaitingList.contains(iRunningTask);
    }

    @Override // com.liulishuo.filedownloader.ILostServiceConnectedHandler
    public void taskWorkFine(BaseDownloadTask.IRunningTask iRunningTask) {
        if (this.mWaitingList.isEmpty()) {
            return;
        }
        synchronized (this.mWaitingList) {
            this.mWaitingList.remove(iRunningTask);
        }
    }
}
