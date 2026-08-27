package com.liulishuo.filedownloader;

/* loaded from: classes.dex */
public class FileDownloadMonitor {
    private static IMonitor monitor;

    /* loaded from: classes.dex */
    public interface IMonitor {
        void onRequestStart(int i, boolean z, FileDownloadListener fileDownloadListener);

        void onRequestStart(BaseDownloadTask baseDownloadTask);

        void onTaskBegin(BaseDownloadTask baseDownloadTask);

        void onTaskOver(BaseDownloadTask baseDownloadTask);

        void onTaskStarted(BaseDownloadTask baseDownloadTask);
    }

    public static IMonitor getMonitor() {
        return monitor;
    }

    public static boolean isValid() {
        return getMonitor() != null;
    }

    public static void releaseGlobalMonitor() {
        monitor = null;
    }

    public static void setGlobalMonitor(IMonitor iMonitor) {
        monitor = iMonitor;
    }
}
