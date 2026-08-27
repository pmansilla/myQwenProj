package com.liulishuo.filedownloader;

import android.app.Notification;
import android.content.Context;
import com.liulishuo.filedownloader.model.FileDownloadHeader;
import com.liulishuo.filedownloader.services.FDServiceSharedHandler;
import com.liulishuo.filedownloader.util.FileDownloadProperties;

/* loaded from: classes.dex */
public class FileDownloadServiceProxy implements IFileDownloadServiceProxy {
    private final IFileDownloadServiceProxy handler;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class HolderClass {
        private static final FileDownloadServiceProxy INSTANCE = new FileDownloadServiceProxy();

        private HolderClass() {
        }
    }

    private FileDownloadServiceProxy() {
        this.handler = FileDownloadProperties.getImpl().processNonSeparate ? new FileDownloadServiceSharedTransmit() : new FileDownloadServiceUIGuard();
    }

    public static FDServiceSharedHandler.FileDownloadServiceSharedConnection getConnectionListener() {
        if (getImpl().handler instanceof FileDownloadServiceSharedTransmit) {
            return (FDServiceSharedHandler.FileDownloadServiceSharedConnection) getImpl().handler;
        }
        return null;
    }

    public static FileDownloadServiceProxy getImpl() {
        return HolderClass.INSTANCE;
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public void bindStartByContext(Context context) {
        this.handler.bindStartByContext(context);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public void bindStartByContext(Context context, Runnable runnable) {
        this.handler.bindStartByContext(context, runnable);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public void clearAllTaskData() {
        this.handler.clearAllTaskData();
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public boolean clearTaskData(int i) {
        return this.handler.clearTaskData(i);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public long getSofar(int i) {
        return this.handler.getSofar(i);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public byte getStatus(int i) {
        return this.handler.getStatus(i);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public long getTotal(int i) {
        return this.handler.getTotal(i);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public boolean isConnected() {
        return this.handler.isConnected();
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public boolean isDownloading(String str, String str2) {
        return this.handler.isDownloading(str, str2);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public boolean isIdle() {
        return this.handler.isIdle();
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public boolean isRunServiceForeground() {
        return this.handler.isRunServiceForeground();
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public boolean pause(int i) {
        return this.handler.pause(i);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public void pauseAllTasks() {
        this.handler.pauseAllTasks();
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public boolean setMaxNetworkThreadCount(int i) {
        return this.handler.setMaxNetworkThreadCount(i);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public boolean start(String str, String str2, boolean z, int i, int i2, int i3, boolean z2, FileDownloadHeader fileDownloadHeader, boolean z3) {
        return this.handler.start(str, str2, z, i, i2, i3, z2, fileDownloadHeader, z3);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public void startForeground(int i, Notification notification) {
        this.handler.startForeground(i, notification);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public void stopForeground(boolean z) {
        this.handler.stopForeground(z);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public void unbindByContext(Context context) {
        this.handler.unbindByContext(context);
    }
}
