package com.liulishuo.filedownloader.event;

/* loaded from: classes.dex */
public class DownloadEventSampleListener extends IDownloadListener {
    private final IEventListener i;

    /* loaded from: classes.dex */
    public interface IEventListener {
        boolean callback(IDownloadEvent iDownloadEvent);
    }

    public DownloadEventSampleListener(IEventListener iEventListener) {
        this.i = iEventListener;
    }

    @Override // com.liulishuo.filedownloader.event.IDownloadListener
    public boolean callback(IDownloadEvent iDownloadEvent) {
        return this.i != null && this.i.callback(iDownloadEvent);
    }
}
