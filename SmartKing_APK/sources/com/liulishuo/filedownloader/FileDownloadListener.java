package com.liulishuo.filedownloader;

import com.liulishuo.filedownloader.util.FileDownloadLog;

/* loaded from: classes.dex */
public abstract class FileDownloadListener {
    public FileDownloadListener() {
    }

    public FileDownloadListener(int i) {
        FileDownloadLog.w(this, "not handle priority any more", new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void blockComplete(BaseDownloadTask baseDownloadTask) throws Throwable {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void completed(BaseDownloadTask baseDownloadTask);

    /* JADX INFO: Access modifiers changed from: protected */
    public void connected(BaseDownloadTask baseDownloadTask, String str, boolean z, int i, int i2) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void error(BaseDownloadTask baseDownloadTask, Throwable th);

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isInvalid() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void paused(BaseDownloadTask baseDownloadTask, int i, int i2);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void pending(BaseDownloadTask baseDownloadTask, int i, int i2);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void progress(BaseDownloadTask baseDownloadTask, int i, int i2);

    /* JADX INFO: Access modifiers changed from: protected */
    public void retry(BaseDownloadTask baseDownloadTask, Throwable th, int i, int i2) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void started(BaseDownloadTask baseDownloadTask) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void warn(BaseDownloadTask baseDownloadTask);
}
