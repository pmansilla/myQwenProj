package com.liulishuo.filedownloader.database;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.liulishuo.filedownloader.database.FileDownloadDatabase;
import com.liulishuo.filedownloader.model.ConnectionModel;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadProperties;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/* loaded from: classes.dex */
public class RemitDatabase implements FileDownloadDatabase {
    private static final int WHAT_CLEAN_LOCK = 0;
    private Handler handler;
    private volatile Thread parkThread;
    private final List<Integer> freeToDBIdList = new ArrayList();
    private AtomicInteger handlingId = new AtomicInteger();
    private final NoDatabaseImpl cachedDatabase = new NoDatabaseImpl();
    private final SqliteDatabaseImpl realDatabase = new SqliteDatabaseImpl();
    private final long minInterval = FileDownloadProperties.getImpl().downloadMinProgressTime;

    /* loaded from: classes.dex */
    public static class Maker implements FileDownloadHelper.DatabaseCustomMaker {
        @Override // com.liulishuo.filedownloader.util.FileDownloadHelper.DatabaseCustomMaker
        public FileDownloadDatabase customMake() {
            return new RemitDatabase();
        }
    }

    public RemitDatabase() {
        HandlerThread handlerThread = new HandlerThread(FileDownloadUtils.getThreadPoolName("RemitHandoverToDB"));
        handlerThread.start();
        this.handler = new Handler(handlerThread.getLooper(), new Handler.Callback() { // from class: com.liulishuo.filedownloader.database.RemitDatabase.1
            @Override // android.os.Handler.Callback
            public boolean handleMessage(Message message) {
                int i = message.what;
                if (i == 0) {
                    if (RemitDatabase.this.parkThread != null) {
                        LockSupport.unpark(RemitDatabase.this.parkThread);
                        RemitDatabase.this.parkThread = null;
                    }
                    return false;
                }
                try {
                    RemitDatabase.this.handlingId.set(i);
                    RemitDatabase.this.syncCacheToDB(i);
                    RemitDatabase.this.freeToDBIdList.add(Integer.valueOf(i));
                    return false;
                } finally {
                    RemitDatabase.this.handlingId.set(0);
                    if (RemitDatabase.this.parkThread != null) {
                        LockSupport.unpark(RemitDatabase.this.parkThread);
                        RemitDatabase.this.parkThread = null;
                    }
                }
            }
        });
    }

    private void ensureCacheToDB(int i) {
        this.handler.removeMessages(i);
        if (this.handlingId.get() != i) {
            syncCacheToDB(i);
            return;
        }
        this.parkThread = Thread.currentThread();
        this.handler.sendEmptyMessage(0);
        LockSupport.park();
    }

    private boolean isNoNeedUpdateToRealDB(int i) {
        return !this.freeToDBIdList.contains(Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void syncCacheToDB(int i) {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "sync cache to db %d", Integer.valueOf(i));
        }
        this.realDatabase.update(this.cachedDatabase.find(i));
        List<ConnectionModel> findConnectionModel = this.cachedDatabase.findConnectionModel(i);
        this.realDatabase.removeConnections(i);
        Iterator<ConnectionModel> it = findConnectionModel.iterator();
        while (it.hasNext()) {
            this.realDatabase.insertConnectionModel(it.next());
        }
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void clear() {
        this.cachedDatabase.clear();
        this.realDatabase.clear();
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public FileDownloadModel find(int i) {
        return this.cachedDatabase.find(i);
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public List<ConnectionModel> findConnectionModel(int i) {
        return this.cachedDatabase.findConnectionModel(i);
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void insert(FileDownloadModel fileDownloadModel) {
        this.cachedDatabase.insert(fileDownloadModel);
        if (isNoNeedUpdateToRealDB(fileDownloadModel.getId())) {
            return;
        }
        this.realDatabase.insert(fileDownloadModel);
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void insertConnectionModel(ConnectionModel connectionModel) {
        this.cachedDatabase.insertConnectionModel(connectionModel);
        if (isNoNeedUpdateToRealDB(connectionModel.getId())) {
            return;
        }
        this.realDatabase.insertConnectionModel(connectionModel);
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public FileDownloadDatabase.Maintainer maintainer() {
        return this.realDatabase.maintainer(this.cachedDatabase.downloaderModelMap, this.cachedDatabase.connectionModelListMap);
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void onTaskStart(int i) {
        this.handler.sendEmptyMessageDelayed(i, this.minInterval);
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public boolean remove(int i) {
        this.realDatabase.remove(i);
        return this.cachedDatabase.remove(i);
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void removeConnections(int i) {
        this.cachedDatabase.removeConnections(i);
        if (isNoNeedUpdateToRealDB(i)) {
            return;
        }
        this.realDatabase.removeConnections(i);
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void update(FileDownloadModel fileDownloadModel) {
        this.cachedDatabase.update(fileDownloadModel);
        if (isNoNeedUpdateToRealDB(fileDownloadModel.getId())) {
            return;
        }
        this.realDatabase.update(fileDownloadModel);
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void updateCompleted(int i, long j) {
        this.cachedDatabase.updateCompleted(i, j);
        if (isNoNeedUpdateToRealDB(i)) {
            this.handler.removeMessages(i);
            if (this.handlingId.get() == i) {
                this.parkThread = Thread.currentThread();
                this.handler.sendEmptyMessage(0);
                LockSupport.park();
                this.realDatabase.updateCompleted(i, j);
            }
        } else {
            this.realDatabase.updateCompleted(i, j);
        }
        this.freeToDBIdList.remove(Integer.valueOf(i));
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void updateConnected(int i, long j, String str, String str2) {
        this.cachedDatabase.updateConnected(i, j, str, str2);
        if (isNoNeedUpdateToRealDB(i)) {
            return;
        }
        this.realDatabase.updateConnected(i, j, str, str2);
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void updateConnectionCount(int i, int i2) {
        this.cachedDatabase.updateConnectionCount(i, i2);
        if (isNoNeedUpdateToRealDB(i)) {
            return;
        }
        this.realDatabase.updateConnectionCount(i, i2);
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void updateConnectionModel(int i, int i2, long j) {
        this.cachedDatabase.updateConnectionModel(i, i2, j);
        if (isNoNeedUpdateToRealDB(i)) {
            return;
        }
        this.realDatabase.updateConnectionModel(i, i2, j);
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void updateError(int i, Throwable th, long j) {
        this.cachedDatabase.updateError(i, th, j);
        if (isNoNeedUpdateToRealDB(i)) {
            ensureCacheToDB(i);
        }
        this.realDatabase.updateError(i, th, j);
        this.freeToDBIdList.remove(Integer.valueOf(i));
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void updateOldEtagOverdue(int i, String str, long j, long j2, int i2) {
        this.cachedDatabase.updateOldEtagOverdue(i, str, j, j2, i2);
        if (isNoNeedUpdateToRealDB(i)) {
            return;
        }
        this.realDatabase.updateOldEtagOverdue(i, str, j, j2, i2);
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void updatePause(int i, long j) {
        this.cachedDatabase.updatePause(i, j);
        if (isNoNeedUpdateToRealDB(i)) {
            ensureCacheToDB(i);
        }
        this.realDatabase.updatePause(i, j);
        this.freeToDBIdList.remove(Integer.valueOf(i));
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void updatePending(int i) {
        this.cachedDatabase.updatePending(i);
        if (isNoNeedUpdateToRealDB(i)) {
            return;
        }
        this.realDatabase.updatePending(i);
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void updateProgress(int i, long j) {
        this.cachedDatabase.updateProgress(i, j);
        if (isNoNeedUpdateToRealDB(i)) {
            return;
        }
        this.realDatabase.updateProgress(i, j);
    }

    @Override // com.liulishuo.filedownloader.database.FileDownloadDatabase
    public void updateRetry(int i, Throwable th) {
        this.cachedDatabase.updateRetry(i, th);
        if (isNoNeedUpdateToRealDB(i)) {
            return;
        }
        this.realDatabase.updateRetry(i, th);
    }
}
