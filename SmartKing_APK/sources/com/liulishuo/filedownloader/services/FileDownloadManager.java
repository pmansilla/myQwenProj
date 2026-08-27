package com.liulishuo.filedownloader.services;

import android.text.TextUtils;
import com.liulishuo.filedownloader.IThreadPoolMonitor;
import com.liulishuo.filedownloader.PauseAllMarker;
import com.liulishuo.filedownloader.database.FileDownloadDatabase;
import com.liulishuo.filedownloader.download.CustomComponentHolder;
import com.liulishuo.filedownloader.download.DownloadLaunchRunnable;
import com.liulishuo.filedownloader.model.ConnectionModel;
import com.liulishuo.filedownloader.model.FileDownloadHeader;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
class FileDownloadManager implements IThreadPoolMonitor {
    private final FileDownloadDatabase mDatabase;
    private final FileDownloadThreadPool mThreadPool;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FileDownloadManager() {
        CustomComponentHolder impl = CustomComponentHolder.getImpl();
        this.mDatabase = impl.getDatabaseInstance();
        this.mThreadPool = new FileDownloadThreadPool(impl.getMaxNetworkThreadCount());
    }

    public void clearAllTaskData() {
        this.mDatabase.clear();
    }

    public boolean clearTaskData(int i) {
        if (i == 0) {
            FileDownloadLog.w(this, "The task[%d] id is invalid, can't clear it.", Integer.valueOf(i));
            return false;
        }
        if (isDownloading(i)) {
            FileDownloadLog.w(this, "The task[%d] is downloading, can't clear it.", Integer.valueOf(i));
            return false;
        }
        this.mDatabase.remove(i);
        this.mDatabase.removeConnections(i);
        return true;
    }

    @Override // com.liulishuo.filedownloader.IThreadPoolMonitor
    public int findRunningTaskIdBySameTempPath(String str, int i) {
        return this.mThreadPool.findRunningTaskIdBySameTempPath(str, i);
    }

    public long getSoFar(int i) {
        FileDownloadModel find = this.mDatabase.find(i);
        if (find == null) {
            return 0L;
        }
        int connectionCount = find.getConnectionCount();
        if (connectionCount <= 1) {
            return find.getSoFar();
        }
        List<ConnectionModel> findConnectionModel = this.mDatabase.findConnectionModel(i);
        if (findConnectionModel == null || findConnectionModel.size() != connectionCount) {
            return 0L;
        }
        return ConnectionModel.getTotalOffset(findConnectionModel);
    }

    public byte getStatus(int i) {
        FileDownloadModel find = this.mDatabase.find(i);
        if (find == null) {
            return (byte) 0;
        }
        return find.getStatus();
    }

    public long getTotal(int i) {
        FileDownloadModel find = this.mDatabase.find(i);
        if (find == null) {
            return 0L;
        }
        return find.getTotal();
    }

    public boolean isDownloading(int i) {
        return isDownloading(this.mDatabase.find(i));
    }

    @Override // com.liulishuo.filedownloader.IThreadPoolMonitor
    public boolean isDownloading(FileDownloadModel fileDownloadModel) {
        if (fileDownloadModel == null) {
            return false;
        }
        boolean isInThreadPool = this.mThreadPool.isInThreadPool(fileDownloadModel.getId());
        if (FileDownloadStatus.isOver(fileDownloadModel.getStatus())) {
            if (!isInThreadPool) {
                return false;
            }
        } else if (!isInThreadPool) {
            FileDownloadLog.e(this, "%d status is[%s](not finish) & but not in the pool", Integer.valueOf(fileDownloadModel.getId()), Byte.valueOf(fileDownloadModel.getStatus()));
            return false;
        }
        return true;
    }

    public boolean isDownloading(String str, String str2) {
        return isDownloading(FileDownloadUtils.generateId(str, str2));
    }

    public boolean isIdle() {
        return this.mThreadPool.exactSize() <= 0;
    }

    public boolean pause(int i) {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "request pause the task %d", Integer.valueOf(i));
        }
        FileDownloadModel find = this.mDatabase.find(i);
        if (find == null) {
            return false;
        }
        find.setStatus((byte) -2);
        this.mThreadPool.cancel(i);
        return true;
    }

    public void pauseAll() {
        List<Integer> allExactRunningDownloadIds = this.mThreadPool.getAllExactRunningDownloadIds();
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "pause all tasks %d", Integer.valueOf(allExactRunningDownloadIds.size()));
        }
        Iterator<Integer> it = allExactRunningDownloadIds.iterator();
        while (it.hasNext()) {
            pause(it.next().intValue());
        }
    }

    public synchronized boolean setMaxNetworkThreadCount(int i) {
        return this.mThreadPool.setMaxNetworkThreadCount(i);
    }

    public synchronized void start(String str, String str2, boolean z, int i, int i2, int i3, boolean z2, FileDownloadHeader fileDownloadHeader, boolean z3) {
        FileDownloadModel fileDownloadModel;
        List<ConnectionModel> list;
        boolean z4 = true;
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "request start the task with url(%s) path(%s) isDirectory(%B)", str, str2, Boolean.valueOf(z));
        }
        PauseAllMarker.clearMarker();
        int generateId = FileDownloadUtils.generateId(str, str2, z);
        FileDownloadModel find = this.mDatabase.find(generateId);
        if (z || find != null) {
            fileDownloadModel = find;
            list = null;
        } else {
            int generateId2 = FileDownloadUtils.generateId(str, FileDownloadUtils.getParent(str2), true);
            FileDownloadModel find2 = this.mDatabase.find(generateId2);
            if (find2 == null || !str2.equals(find2.getTargetFilePath())) {
                list = null;
            } else {
                if (FileDownloadLog.NEED_LOG) {
                    FileDownloadLog.d(this, "task[%d] find model by dirCaseId[%d]", Integer.valueOf(generateId), Integer.valueOf(generateId2));
                }
                list = this.mDatabase.findConnectionModel(generateId2);
            }
            fileDownloadModel = find2;
        }
        if (FileDownloadHelper.inspectAndInflowDownloading(generateId, fileDownloadModel, this, true)) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "has already started download %d", Integer.valueOf(generateId));
            }
            return;
        }
        String targetFilePath = fileDownloadModel != null ? fileDownloadModel.getTargetFilePath() : FileDownloadUtils.getTargetFilePath(str2, z, null);
        if (FileDownloadHelper.inspectAndInflowDownloaded(generateId, targetFilePath, z2, true)) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "has already completed downloading %d", Integer.valueOf(generateId));
            }
            return;
        }
        if (FileDownloadHelper.inspectAndInflowConflictPath(generateId, fileDownloadModel != null ? fileDownloadModel.getSoFar() : 0L, fileDownloadModel != null ? fileDownloadModel.getTempFilePath() : FileDownloadUtils.getTempPath(targetFilePath), targetFilePath, this)) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "there is an another task with the same target-file-path %d %s", Integer.valueOf(generateId), targetFilePath);
            }
            if (fileDownloadModel != null) {
                this.mDatabase.remove(generateId);
                this.mDatabase.removeConnections(generateId);
            }
            return;
        }
        if (fileDownloadModel == null || !(fileDownloadModel.getStatus() == -2 || fileDownloadModel.getStatus() == -1 || fileDownloadModel.getStatus() == 1 || fileDownloadModel.getStatus() == 6 || fileDownloadModel.getStatus() == 2)) {
            if (fileDownloadModel == null) {
                fileDownloadModel = new FileDownloadModel();
            }
            fileDownloadModel.setUrl(str);
            fileDownloadModel.setPath(str2, z);
            fileDownloadModel.setId(generateId);
            fileDownloadModel.setSoFar(0L);
            fileDownloadModel.setTotal(0L);
            fileDownloadModel.setStatus((byte) 1);
            fileDownloadModel.setConnectionCount(1);
        } else if (fileDownloadModel.getId() != generateId) {
            this.mDatabase.remove(fileDownloadModel.getId());
            this.mDatabase.removeConnections(fileDownloadModel.getId());
            fileDownloadModel.setId(generateId);
            fileDownloadModel.setPath(str2, z);
            if (list != null) {
                for (ConnectionModel connectionModel : list) {
                    connectionModel.setId(generateId);
                    this.mDatabase.insertConnectionModel(connectionModel);
                }
            }
        } else if (TextUtils.equals(str, fileDownloadModel.getUrl())) {
            z4 = false;
        } else {
            fileDownloadModel.setUrl(str);
        }
        if (z4) {
            this.mDatabase.update(fileDownloadModel);
        }
        this.mThreadPool.execute(new DownloadLaunchRunnable.Builder().setModel(fileDownloadModel).setHeader(fileDownloadHeader).setThreadPoolMonitor(this).setMinIntervalMillis(Integer.valueOf(i2)).setCallbackProgressMaxCount(Integer.valueOf(i)).setForceReDownload(Boolean.valueOf(z2)).setWifiRequired(Boolean.valueOf(z3)).setMaxRetryTimes(Integer.valueOf(i3)).build());
    }
}
