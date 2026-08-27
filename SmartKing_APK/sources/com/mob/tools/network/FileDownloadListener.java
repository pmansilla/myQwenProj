package com.mob.tools.network;

/* loaded from: classes2.dex */
public abstract class FileDownloadListener {
    private boolean isCanceled = false;

    public void cancel() {
        this.isCanceled = true;
    }

    public boolean isCanceled() {
        return this.isCanceled;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void onProgress(int i, long j, long j2);
}
