package com.liulishuo.filedownloader;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import com.liulishuo.filedownloader.event.DownloadServiceConnectChangedEvent;
import com.liulishuo.filedownloader.model.FileDownloadHeader;
import com.liulishuo.filedownloader.services.FDServiceSharedHandler;
import com.liulishuo.filedownloader.services.FileDownloadService;
import com.liulishuo.filedownloader.util.DownloadServiceNotConnectedHelper;
import com.liulishuo.filedownloader.util.ExtraKeys;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class FileDownloadServiceSharedTransmit implements IFileDownloadServiceProxy, FDServiceSharedHandler.FileDownloadServiceSharedConnection {
    private static final Class<?> SERVICE_CLASS = FileDownloadService.SharedMainProcessService.class;
    private FDServiceSharedHandler handler;
    private boolean runServiceForeground = false;
    private final ArrayList<Runnable> connectedRunnableList = new ArrayList<>();

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public void bindStartByContext(Context context) {
        bindStartByContext(context, null);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public void bindStartByContext(Context context, Runnable runnable) {
        if (runnable != null && !this.connectedRunnableList.contains(runnable)) {
            this.connectedRunnableList.add(runnable);
        }
        Intent intent = new Intent(context, SERVICE_CLASS);
        this.runServiceForeground = FileDownloadUtils.needMakeServiceForeground(context);
        intent.putExtra(ExtraKeys.IS_FOREGROUND, this.runServiceForeground);
        if (!this.runServiceForeground) {
            context.startService(intent);
            return;
        }
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "start foreground service", new Object[0]);
        }
        if (Build.VERSION.SDK_INT >= 26) {
            context.startForegroundService(intent);
        }
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public void clearAllTaskData() {
        if (isConnected()) {
            this.handler.clearAllTaskData();
        } else {
            DownloadServiceNotConnectedHelper.clearAllTaskData();
        }
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public boolean clearTaskData(int i) {
        return !isConnected() ? DownloadServiceNotConnectedHelper.clearTaskData(i) : this.handler.clearTaskData(i);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public long getSofar(int i) {
        return !isConnected() ? DownloadServiceNotConnectedHelper.getSofar(i) : this.handler.getSofar(i);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public byte getStatus(int i) {
        return !isConnected() ? DownloadServiceNotConnectedHelper.getStatus(i) : this.handler.getStatus(i);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public long getTotal(int i) {
        return !isConnected() ? DownloadServiceNotConnectedHelper.getTotal(i) : this.handler.getTotal(i);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public boolean isConnected() {
        return this.handler != null;
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public boolean isDownloading(String str, String str2) {
        return !isConnected() ? DownloadServiceNotConnectedHelper.isDownloading(str, str2) : this.handler.checkDownloading(str, str2);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public boolean isIdle() {
        return !isConnected() ? DownloadServiceNotConnectedHelper.isIdle() : this.handler.isIdle();
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public boolean isRunServiceForeground() {
        return this.runServiceForeground;
    }

    @Override // com.liulishuo.filedownloader.services.FDServiceSharedHandler.FileDownloadServiceSharedConnection
    public void onConnected(FDServiceSharedHandler fDServiceSharedHandler) {
        this.handler = fDServiceSharedHandler;
        List list = (List) this.connectedRunnableList.clone();
        this.connectedRunnableList.clear();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            ((Runnable) it.next()).run();
        }
        FileDownloadEventPool.getImpl().asyncPublishInNewThread(new DownloadServiceConnectChangedEvent(DownloadServiceConnectChangedEvent.ConnectStatus.connected, SERVICE_CLASS));
    }

    @Override // com.liulishuo.filedownloader.services.FDServiceSharedHandler.FileDownloadServiceSharedConnection
    public void onDisconnected() {
        this.handler = null;
        FileDownloadEventPool.getImpl().asyncPublishInNewThread(new DownloadServiceConnectChangedEvent(DownloadServiceConnectChangedEvent.ConnectStatus.disconnected, SERVICE_CLASS));
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public boolean pause(int i) {
        return !isConnected() ? DownloadServiceNotConnectedHelper.pause(i) : this.handler.pause(i);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public void pauseAllTasks() {
        if (isConnected()) {
            this.handler.pauseAllTasks();
        } else {
            DownloadServiceNotConnectedHelper.pauseAllTasks();
        }
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public boolean setMaxNetworkThreadCount(int i) {
        return !isConnected() ? DownloadServiceNotConnectedHelper.setMaxNetworkThreadCount(i) : this.handler.setMaxNetworkThreadCount(i);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public boolean start(String str, String str2, boolean z, int i, int i2, int i3, boolean z2, FileDownloadHeader fileDownloadHeader, boolean z3) {
        if (!isConnected()) {
            return DownloadServiceNotConnectedHelper.start(str, str2, z);
        }
        this.handler.start(str, str2, z, i, i2, i3, z2, fileDownloadHeader, z3);
        return true;
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public void startForeground(int i, Notification notification) {
        if (isConnected()) {
            this.handler.startForeground(i, notification);
        } else {
            DownloadServiceNotConnectedHelper.startForeground(i, notification);
        }
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public void stopForeground(boolean z) {
        if (!isConnected()) {
            DownloadServiceNotConnectedHelper.stopForeground(z);
        } else {
            this.handler.stopForeground(z);
            this.runServiceForeground = false;
        }
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public void unbindByContext(Context context) {
        context.stopService(new Intent(context, SERVICE_CLASS));
        this.handler = null;
    }
}
