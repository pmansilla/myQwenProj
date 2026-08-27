package com.liulishuo.filedownloader.notification;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadList;
import com.liulishuo.filedownloader.FileDownloadListener;

/* loaded from: classes.dex */
public abstract class FileDownloadNotificationListener extends FileDownloadListener {
    private final FileDownloadNotificationHelper helper;

    public FileDownloadNotificationListener(FileDownloadNotificationHelper fileDownloadNotificationHelper) {
        if (fileDownloadNotificationHelper == null) {
            throw new IllegalArgumentException("helper must not be null!");
        }
        this.helper = fileDownloadNotificationHelper;
    }

    public void addNotificationItem(int i) {
        BaseDownloadTask.IRunningTask iRunningTask;
        if (i == 0 || (iRunningTask = FileDownloadList.getImpl().get(i)) == null) {
            return;
        }
        addNotificationItem(iRunningTask.getOrigin());
    }

    public void addNotificationItem(BaseDownloadTask baseDownloadTask) {
        BaseNotificationItem create;
        if (disableNotification(baseDownloadTask) || (create = create(baseDownloadTask)) == null) {
            return;
        }
        this.helper.add(create);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.liulishuo.filedownloader.FileDownloadListener
    public void blockComplete(BaseDownloadTask baseDownloadTask) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.liulishuo.filedownloader.FileDownloadListener
    public void completed(BaseDownloadTask baseDownloadTask) {
        destroyNotification(baseDownloadTask);
    }

    protected abstract BaseNotificationItem create(BaseDownloadTask baseDownloadTask);

    public void destroyNotification(BaseDownloadTask baseDownloadTask) {
        if (disableNotification(baseDownloadTask)) {
            return;
        }
        this.helper.showIndeterminate(baseDownloadTask.getId(), baseDownloadTask.getStatus());
        BaseNotificationItem remove = this.helper.remove(baseDownloadTask.getId());
        if (interceptCancel(baseDownloadTask, remove) || remove == null) {
            return;
        }
        remove.cancel();
    }

    protected boolean disableNotification(BaseDownloadTask baseDownloadTask) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.liulishuo.filedownloader.FileDownloadListener
    public void error(BaseDownloadTask baseDownloadTask, Throwable th) {
        destroyNotification(baseDownloadTask);
    }

    public FileDownloadNotificationHelper getHelper() {
        return this.helper;
    }

    protected boolean interceptCancel(BaseDownloadTask baseDownloadTask, BaseNotificationItem baseNotificationItem) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.liulishuo.filedownloader.FileDownloadListener
    public void paused(BaseDownloadTask baseDownloadTask, int i, int i2) {
        destroyNotification(baseDownloadTask);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.liulishuo.filedownloader.FileDownloadListener
    public void pending(BaseDownloadTask baseDownloadTask, int i, int i2) {
        addNotificationItem(baseDownloadTask);
        showIndeterminate(baseDownloadTask);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.liulishuo.filedownloader.FileDownloadListener
    public void progress(BaseDownloadTask baseDownloadTask, int i, int i2) {
        showProgress(baseDownloadTask, i, i2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.liulishuo.filedownloader.FileDownloadListener
    public void retry(BaseDownloadTask baseDownloadTask, Throwable th, int i, int i2) {
        super.retry(baseDownloadTask, th, i, i2);
        showIndeterminate(baseDownloadTask);
    }

    public void showIndeterminate(BaseDownloadTask baseDownloadTask) {
        if (disableNotification(baseDownloadTask)) {
            return;
        }
        this.helper.showIndeterminate(baseDownloadTask.getId(), baseDownloadTask.getStatus());
    }

    public void showProgress(BaseDownloadTask baseDownloadTask, int i, int i2) {
        if (disableNotification(baseDownloadTask)) {
            return;
        }
        this.helper.showProgress(baseDownloadTask.getId(), baseDownloadTask.getSmallFileSoFarBytes(), baseDownloadTask.getSmallFileTotalBytes());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.liulishuo.filedownloader.FileDownloadListener
    public void started(BaseDownloadTask baseDownloadTask) {
        super.started(baseDownloadTask);
        showIndeterminate(baseDownloadTask);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.liulishuo.filedownloader.FileDownloadListener
    public void warn(BaseDownloadTask baseDownloadTask) {
    }
}
