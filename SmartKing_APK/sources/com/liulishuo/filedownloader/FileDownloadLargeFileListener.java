package com.liulishuo.filedownloader;

/* loaded from: classes.dex */
public abstract class FileDownloadLargeFileListener extends FileDownloadListener {
    public FileDownloadLargeFileListener() {
    }

    public FileDownloadLargeFileListener(int i) {
        super(i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.liulishuo.filedownloader.FileDownloadListener
    public void connected(BaseDownloadTask baseDownloadTask, String str, boolean z, int i, int i2) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void connected(BaseDownloadTask baseDownloadTask, String str, boolean z, long j, long j2) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.liulishuo.filedownloader.FileDownloadListener
    public void paused(BaseDownloadTask baseDownloadTask, int i, int i2) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void paused(BaseDownloadTask baseDownloadTask, long j, long j2);

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.liulishuo.filedownloader.FileDownloadListener
    public void pending(BaseDownloadTask baseDownloadTask, int i, int i2) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void pending(BaseDownloadTask baseDownloadTask, long j, long j2);

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.liulishuo.filedownloader.FileDownloadListener
    public void progress(BaseDownloadTask baseDownloadTask, int i, int i2) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void progress(BaseDownloadTask baseDownloadTask, long j, long j2);

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.liulishuo.filedownloader.FileDownloadListener
    public void retry(BaseDownloadTask baseDownloadTask, Throwable th, int i, int i2) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void retry(BaseDownloadTask baseDownloadTask, Throwable th, int i, long j) {
    }
}
