package com.liulishuo.filedownloader.event;

import com.liulishuo.filedownloader.util.FileDownloadLog;

/* loaded from: classes.dex */
public abstract class IDownloadEvent {
    public Runnable callback = null;
    protected final String id;

    public IDownloadEvent(String str) {
        this.id = str;
    }

    public IDownloadEvent(String str, boolean z) {
        this.id = str;
        if (z) {
            FileDownloadLog.w(this, "do not handle ORDER any more, %s", str);
        }
    }

    public final String getId() {
        return this.id;
    }
}
