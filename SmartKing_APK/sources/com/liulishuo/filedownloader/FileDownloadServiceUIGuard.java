package com.liulishuo.filedownloader;

import android.app.Notification;
import android.os.IBinder;
import android.os.RemoteException;
import com.liulishuo.filedownloader.i.IFileDownloadIPCCallback;
import com.liulishuo.filedownloader.i.IFileDownloadIPCService;
import com.liulishuo.filedownloader.message.MessageSnapshot;
import com.liulishuo.filedownloader.message.MessageSnapshotFlow;
import com.liulishuo.filedownloader.model.FileDownloadHeader;
import com.liulishuo.filedownloader.services.BaseFileServiceUIGuard;
import com.liulishuo.filedownloader.services.FileDownloadService;
import com.liulishuo.filedownloader.util.DownloadServiceNotConnectedHelper;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class FileDownloadServiceUIGuard extends BaseFileServiceUIGuard<FileDownloadServiceCallback, IFileDownloadIPCService> {

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static class FileDownloadServiceCallback extends IFileDownloadIPCCallback.Stub {
        protected FileDownloadServiceCallback() {
        }

        @Override // com.liulishuo.filedownloader.i.IFileDownloadIPCCallback
        public void callback(MessageSnapshot messageSnapshot) throws RemoteException {
            MessageSnapshotFlow.getImpl().inflow(messageSnapshot);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FileDownloadServiceUIGuard() {
        super(FileDownloadService.SeparateProcessService.class);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.liulishuo.filedownloader.services.BaseFileServiceUIGuard
    public IFileDownloadIPCService asInterface(IBinder iBinder) {
        return IFileDownloadIPCService.Stub.asInterface(iBinder);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public void clearAllTaskData() {
        if (!isConnected()) {
            DownloadServiceNotConnectedHelper.clearAllTaskData();
            return;
        }
        try {
            getService().clearAllTaskData();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public boolean clearTaskData(int i) {
        if (!isConnected()) {
            return DownloadServiceNotConnectedHelper.clearTaskData(i);
        }
        try {
            return getService().clearTaskData(i);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.liulishuo.filedownloader.services.BaseFileServiceUIGuard
    public FileDownloadServiceCallback createCallback() {
        return new FileDownloadServiceCallback();
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public long getSofar(int i) {
        if (!isConnected()) {
            return DownloadServiceNotConnectedHelper.getSofar(i);
        }
        try {
            return getService().getSofar(i);
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public byte getStatus(int i) {
        if (!isConnected()) {
            return DownloadServiceNotConnectedHelper.getStatus(i);
        }
        try {
            return getService().getStatus(i);
        } catch (RemoteException e) {
            e.printStackTrace();
            return (byte) 0;
        }
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public long getTotal(int i) {
        if (!isConnected()) {
            return DownloadServiceNotConnectedHelper.getTotal(i);
        }
        try {
            return getService().getTotal(i);
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public boolean isDownloading(String str, String str2) {
        if (!isConnected()) {
            return DownloadServiceNotConnectedHelper.isDownloading(str, str2);
        }
        try {
            return getService().checkDownloading(str, str2);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public boolean isIdle() {
        if (!isConnected()) {
            return DownloadServiceNotConnectedHelper.isIdle();
        }
        try {
            getService().isIdle();
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
            return true;
        }
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public boolean pause(int i) {
        if (!isConnected()) {
            return DownloadServiceNotConnectedHelper.pause(i);
        }
        try {
            return getService().pause(i);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public void pauseAllTasks() {
        if (!isConnected()) {
            DownloadServiceNotConnectedHelper.pauseAllTasks();
            return;
        }
        try {
            getService().pauseAllTasks();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.liulishuo.filedownloader.services.BaseFileServiceUIGuard
    public void registerCallback(IFileDownloadIPCService iFileDownloadIPCService, FileDownloadServiceCallback fileDownloadServiceCallback) throws RemoteException {
        iFileDownloadIPCService.registerCallback(fileDownloadServiceCallback);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public boolean setMaxNetworkThreadCount(int i) {
        if (!isConnected()) {
            return DownloadServiceNotConnectedHelper.setMaxNetworkThreadCount(i);
        }
        try {
            return getService().setMaxNetworkThreadCount(i);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public boolean start(String str, String str2, boolean z, int i, int i2, int i3, boolean z2, FileDownloadHeader fileDownloadHeader, boolean z3) {
        if (!isConnected()) {
            return DownloadServiceNotConnectedHelper.start(str, str2, z);
        }
        try {
            getService().start(str, str2, z, i, i2, i3, z2, fileDownloadHeader, z3);
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public void startForeground(int i, Notification notification) {
        if (!isConnected()) {
            DownloadServiceNotConnectedHelper.startForeground(i, notification);
            return;
        }
        try {
            getService().startForeground(i, notification);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public void stopForeground(boolean z) {
        if (!isConnected()) {
            DownloadServiceNotConnectedHelper.stopForeground(z);
            return;
        }
        try {
            try {
                getService().stopForeground(z);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } finally {
            this.runServiceForeground = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.liulishuo.filedownloader.services.BaseFileServiceUIGuard
    public void unregisterCallback(IFileDownloadIPCService iFileDownloadIPCService, FileDownloadServiceCallback fileDownloadServiceCallback) throws RemoteException {
        iFileDownloadIPCService.unregisterCallback(fileDownloadServiceCallback);
    }
}
