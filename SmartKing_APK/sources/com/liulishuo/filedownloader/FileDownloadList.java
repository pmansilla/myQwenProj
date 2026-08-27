package com.liulishuo.filedownloader;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.message.MessageSnapshot;
import com.liulishuo.filedownloader.message.MessageSnapshotTaker;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class FileDownloadList {
    private final ArrayList<BaseDownloadTask.IRunningTask> mList;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class HolderClass {
        private static final FileDownloadList INSTANCE = new FileDownloadList();

        private HolderClass() {
        }
    }

    private FileDownloadList() {
        this.mList = new ArrayList<>();
    }

    public static FileDownloadList getImpl() {
        return HolderClass.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void add(BaseDownloadTask.IRunningTask iRunningTask) {
        if (!iRunningTask.getOrigin().isAttached()) {
            iRunningTask.setAttachKeyDefault();
        }
        if (iRunningTask.getMessageHandler().getMessenger().notifyBegin()) {
            addUnchecked(iRunningTask);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addUnchecked(BaseDownloadTask.IRunningTask iRunningTask) {
        if (iRunningTask.isMarkedAdded2List()) {
            return;
        }
        synchronized (this.mList) {
            if (this.mList.contains(iRunningTask)) {
                FileDownloadLog.w(this, "already has %s", iRunningTask);
            } else {
                iRunningTask.markAdded2List();
                this.mList.add(iRunningTask);
                if (FileDownloadLog.NEED_LOG) {
                    FileDownloadLog.v(this, "add list in all %s %d %d", iRunningTask, Byte.valueOf(iRunningTask.getOrigin().getStatus()), Integer.valueOf(this.mList.size()));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<BaseDownloadTask.IRunningTask> assembleTasksToStart(int i, FileDownloadListener fileDownloadListener) {
        ArrayList arrayList = new ArrayList();
        synchronized (this.mList) {
            Iterator<BaseDownloadTask.IRunningTask> it = this.mList.iterator();
            while (it.hasNext()) {
                BaseDownloadTask.IRunningTask next = it.next();
                if (next.getOrigin().getListener() == fileDownloadListener && !next.getOrigin().isAttached()) {
                    next.setAttachKeyByQueue(i);
                    arrayList.add(next);
                }
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<BaseDownloadTask.IRunningTask> copy(FileDownloadListener fileDownloadListener) {
        ArrayList arrayList = new ArrayList();
        synchronized (this.mList) {
            Iterator<BaseDownloadTask.IRunningTask> it = this.mList.iterator();
            while (it.hasNext()) {
                BaseDownloadTask.IRunningTask next = it.next();
                if (next.is(fileDownloadListener)) {
                    arrayList.add(next);
                }
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BaseDownloadTask.IRunningTask[] copy() {
        BaseDownloadTask.IRunningTask[] iRunningTaskArr;
        synchronized (this.mList) {
            iRunningTaskArr = (BaseDownloadTask.IRunningTask[]) this.mList.toArray(new BaseDownloadTask.IRunningTask[this.mList.size()]);
        }
        return iRunningTaskArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int count(int i) {
        int i2;
        synchronized (this.mList) {
            Iterator<BaseDownloadTask.IRunningTask> it = this.mList.iterator();
            i2 = 0;
            while (it.hasNext()) {
                if (it.next().is(i)) {
                    i2++;
                }
            }
        }
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void divertAndIgnoreDuplicate(List<BaseDownloadTask.IRunningTask> list) {
        synchronized (this.mList) {
            Iterator<BaseDownloadTask.IRunningTask> it = this.mList.iterator();
            while (it.hasNext()) {
                BaseDownloadTask.IRunningTask next = it.next();
                if (!list.contains(next)) {
                    list.add(next);
                }
            }
            this.mList.clear();
        }
    }

    public BaseDownloadTask.IRunningTask get(int i) {
        synchronized (this.mList) {
            Iterator<BaseDownloadTask.IRunningTask> it = this.mList.iterator();
            while (it.hasNext()) {
                BaseDownloadTask.IRunningTask next = it.next();
                if (next.is(i)) {
                    return next;
                }
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<BaseDownloadTask.IRunningTask> getDownloadingList(int i) {
        ArrayList arrayList = new ArrayList();
        synchronized (this.mList) {
            Iterator<BaseDownloadTask.IRunningTask> it = this.mList.iterator();
            while (it.hasNext()) {
                BaseDownloadTask.IRunningTask next = it.next();
                if (next.is(i) && !next.isOver()) {
                    arrayList.add(next);
                }
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<BaseDownloadTask.IRunningTask> getReceiveServiceTaskList(int i) {
        byte status;
        ArrayList arrayList = new ArrayList();
        synchronized (this.mList) {
            Iterator<BaseDownloadTask.IRunningTask> it = this.mList.iterator();
            while (it.hasNext()) {
                BaseDownloadTask.IRunningTask next = it.next();
                if (next.is(i) && !next.isOver() && (status = next.getOrigin().getStatus()) != 0 && status != 10) {
                    arrayList.add(next);
                }
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isEmpty() {
        return this.mList.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isNotContains(BaseDownloadTask.IRunningTask iRunningTask) {
        return this.mList.isEmpty() || !this.mList.contains(iRunningTask);
    }

    public boolean remove(BaseDownloadTask.IRunningTask iRunningTask, MessageSnapshot messageSnapshot) {
        boolean remove;
        byte status = messageSnapshot.getStatus();
        synchronized (this.mList) {
            remove = this.mList.remove(iRunningTask);
            if (remove && this.mList.size() == 0 && FileDownloadServiceProxy.getImpl().isRunServiceForeground()) {
                FileDownloader.getImpl().stopForeground(true);
            }
        }
        if (FileDownloadLog.NEED_LOG && this.mList.size() == 0) {
            FileDownloadLog.v(this, "remove %s left %d %d", iRunningTask, Byte.valueOf(status), Integer.valueOf(this.mList.size()));
        }
        if (remove) {
            IFileDownloadMessenger messenger = iRunningTask.getMessageHandler().getMessenger();
            switch (status) {
                case -4:
                    messenger.notifyWarn(messageSnapshot);
                    break;
                case -3:
                    messenger.notifyBlockComplete(MessageSnapshotTaker.takeBlockCompleted(messageSnapshot));
                    break;
                case -2:
                    messenger.notifyPaused(messageSnapshot);
                    break;
                case -1:
                    messenger.notifyError(messageSnapshot);
                    break;
            }
        } else {
            FileDownloadLog.e(this, "remove error, not exist: %s %d", iRunningTask, Byte.valueOf(status));
        }
        return remove;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int size() {
        return this.mList.size();
    }
}
