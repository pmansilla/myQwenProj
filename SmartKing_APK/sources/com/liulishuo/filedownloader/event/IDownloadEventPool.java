package com.liulishuo.filedownloader.event;

/* loaded from: classes.dex */
interface IDownloadEventPool {
    boolean addListener(String str, IDownloadListener iDownloadListener);

    void asyncPublishInNewThread(IDownloadEvent iDownloadEvent);

    boolean publish(IDownloadEvent iDownloadEvent);

    boolean removeListener(String str, IDownloadListener iDownloadListener);
}
