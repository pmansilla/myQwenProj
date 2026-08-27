package com.liulishuo.filedownloader.services;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import com.liulishuo.filedownloader.FileDownloadEventPool;
import com.liulishuo.filedownloader.IFileDownloadServiceProxy;
import com.liulishuo.filedownloader.event.DownloadServiceConnectChangedEvent;
import com.liulishuo.filedownloader.util.ExtraKeys;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public abstract class BaseFileServiceUIGuard<CALLBACK extends Binder, INTERFACE extends IInterface> implements IFileDownloadServiceProxy, ServiceConnection {
    private volatile INTERFACE service;
    private final Class<?> serviceClass;
    protected boolean runServiceForeground = false;
    private final HashMap<String, Object> uiCacheMap = new HashMap<>();
    private final List<Context> bindContexts = new ArrayList();
    private final ArrayList<Runnable> connectedRunnableList = new ArrayList<>();
    private final CALLBACK callback = createCallback();

    /* JADX INFO: Access modifiers changed from: protected */
    public BaseFileServiceUIGuard(Class<?> cls) {
        this.serviceClass = cls;
    }

    private void releaseConnect(boolean z) {
        if (!z && this.service != null) {
            try {
                unregisterCallback(this.service, this.callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "release connect resources %s", this.service);
        }
        this.service = null;
        FileDownloadEventPool.getImpl().asyncPublishInNewThread(new DownloadServiceConnectChangedEvent(z ? DownloadServiceConnectChangedEvent.ConnectStatus.lost : DownloadServiceConnectChangedEvent.ConnectStatus.disconnected, this.serviceClass));
    }

    protected abstract INTERFACE asInterface(IBinder iBinder);

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public void bindStartByContext(Context context) {
        bindStartByContext(context, null);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public void bindStartByContext(Context context, Runnable runnable) {
        if (FileDownloadUtils.isDownloaderProcess(context)) {
            throw new IllegalStateException("Fatal-Exception: You can't bind the FileDownloadService in :filedownloader process.\n It's the invalid operation and is likely to cause unexpected problems.\n Maybe you want to use non-separate process mode for FileDownloader, More detail about non-separate mode, please move to wiki manually: https://github.com/lingochamp/FileDownloader/wiki/filedownloader.properties");
        }
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "bindStartByContext %s", context.getClass().getSimpleName());
        }
        Intent intent = new Intent(context, this.serviceClass);
        if (runnable != null && !this.connectedRunnableList.contains(runnable)) {
            this.connectedRunnableList.add(runnable);
        }
        if (!this.bindContexts.contains(context)) {
            this.bindContexts.add(context);
        }
        this.runServiceForeground = FileDownloadUtils.needMakeServiceForeground(context);
        intent.putExtra(ExtraKeys.IS_FOREGROUND, this.runServiceForeground);
        context.bindService(intent, this, 1);
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

    protected abstract CALLBACK createCallback();

    protected CALLBACK getCallback() {
        return this.callback;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public INTERFACE getService() {
        return this.service;
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public boolean isConnected() {
        return getService() != null;
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public boolean isRunServiceForeground() {
        return this.runServiceForeground;
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        this.service = asInterface(iBinder);
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "onServiceConnected %s %s", componentName, this.service);
        }
        try {
            registerCallback(this.service, this.callback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        List list = (List) this.connectedRunnableList.clone();
        this.connectedRunnableList.clear();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            ((Runnable) it.next()).run();
        }
        FileDownloadEventPool.getImpl().asyncPublishInNewThread(new DownloadServiceConnectChangedEvent(DownloadServiceConnectChangedEvent.ConnectStatus.connected, this.serviceClass));
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName componentName) {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "onServiceDisconnected %s %s", componentName, this.service);
        }
        releaseConnect(true);
    }

    protected Object popCache(String str) {
        return this.uiCacheMap.remove(str);
    }

    protected String putCache(Object obj) {
        if (obj == null) {
            return null;
        }
        String obj2 = obj.toString();
        this.uiCacheMap.put(obj2, obj);
        return obj2;
    }

    protected abstract void registerCallback(INTERFACE r1, CALLBACK callback) throws RemoteException;

    @Override // com.liulishuo.filedownloader.IFileDownloadServiceProxy
    public void unbindByContext(Context context) {
        if (this.bindContexts.contains(context)) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "unbindByContext %s", context);
            }
            this.bindContexts.remove(context);
            if (this.bindContexts.isEmpty()) {
                releaseConnect(false);
            }
            Intent intent = new Intent(context, this.serviceClass);
            context.unbindService(this);
            context.stopService(intent);
        }
    }

    protected abstract void unregisterCallback(INTERFACE r1, CALLBACK callback) throws RemoteException;
}
