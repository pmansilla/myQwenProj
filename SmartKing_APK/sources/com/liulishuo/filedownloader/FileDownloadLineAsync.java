package com.liulishuo.filedownloader;

import android.app.Notification;

/* loaded from: classes.dex */
public class FileDownloadLineAsync {
    public boolean startForeground(final int i, final Notification notification) {
        if (FileDownloader.getImpl().isServiceConnected()) {
            FileDownloader.getImpl().startForeground(i, notification);
            return true;
        }
        FileDownloader.getImpl().bindService(new Runnable() { // from class: com.liulishuo.filedownloader.FileDownloadLineAsync.1
            @Override // java.lang.Runnable
            public void run() {
                FileDownloader.getImpl().startForeground(i, notification);
            }
        });
        return false;
    }
}
