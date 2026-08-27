package com.liulishuo.filedownloader;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.ITaskHunter;
import com.liulishuo.filedownloader.message.BlockCompleteMessage;
import com.liulishuo.filedownloader.message.IMessageSnapshot;
import com.liulishuo.filedownloader.message.MessageSnapshot;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/* loaded from: classes.dex */
class FileDownloadMessenger implements IFileDownloadMessenger {
    private boolean mIsDiscard = false;
    private BaseDownloadTask.LifeCycleCallback mLifeCycleCallback;
    private BaseDownloadTask.IRunningTask mTask;
    private Queue<MessageSnapshot> parcelQueue;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FileDownloadMessenger(BaseDownloadTask.IRunningTask iRunningTask, BaseDownloadTask.LifeCycleCallback lifeCycleCallback) {
        init(iRunningTask, lifeCycleCallback);
    }

    private void init(BaseDownloadTask.IRunningTask iRunningTask, BaseDownloadTask.LifeCycleCallback lifeCycleCallback) {
        this.mTask = iRunningTask;
        this.mLifeCycleCallback = lifeCycleCallback;
        this.parcelQueue = new LinkedBlockingQueue();
    }

    private void inspectAndHandleOverStatus(int i) {
        if (FileDownloadStatus.isOver(i)) {
            if (!this.parcelQueue.isEmpty()) {
                MessageSnapshot peek = this.parcelQueue.peek();
                FileDownloadLog.w(this, "the messenger[%s](with id[%d]) has already accomplished all his job, but there still are some messages in parcel queue[%d] queue-top-status[%d]", this, Integer.valueOf(peek.getId()), Integer.valueOf(this.parcelQueue.size()), Byte.valueOf(peek.getStatus()));
            }
            this.mTask = null;
        }
    }

    private void process(MessageSnapshot messageSnapshot) {
        if (this.mTask == null) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "occur this case, it would be the host task of this messenger has been over(paused/warn/completed/error) on the other thread before receiving the snapshot(id[%d], status[%d])", Integer.valueOf(messageSnapshot.getId()), Byte.valueOf(messageSnapshot.getStatus()));
            }
        } else {
            if (!this.mIsDiscard && this.mTask.getOrigin().getListener() != null) {
                this.parcelQueue.offer(messageSnapshot);
                FileDownloadMessageStation.getImpl().requestEnqueue(this);
                return;
            }
            if ((FileDownloadMonitor.isValid() || this.mTask.isContainFinishListener()) && messageSnapshot.getStatus() == 4) {
                this.mLifeCycleCallback.onOver();
            }
            inspectAndHandleOverStatus(messageSnapshot.getStatus());
        }
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadMessenger
    public void discard() {
        this.mIsDiscard = true;
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadMessenger
    public boolean handoverDirectly() {
        return this.mTask.getOrigin().isSyncCallback();
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadMessenger
    public void handoverMessage() {
        if (this.mIsDiscard) {
            return;
        }
        IMessageSnapshot iMessageSnapshot = (MessageSnapshot) this.parcelQueue.poll();
        byte status = iMessageSnapshot.getStatus();
        BaseDownloadTask.IRunningTask iRunningTask = this.mTask;
        if (iRunningTask == null) {
            throw new IllegalArgumentException(FileDownloadUtils.formatString("can't handover the message, no master to receive this message(status[%d]) size[%d]", Integer.valueOf(status), Integer.valueOf(this.parcelQueue.size())));
        }
        BaseDownloadTask origin = iRunningTask.getOrigin();
        FileDownloadListener listener = origin.getListener();
        ITaskHunter.IMessageHandler messageHandler = iRunningTask.getMessageHandler();
        inspectAndHandleOverStatus(status);
        if (listener == null || listener.isInvalid()) {
            return;
        }
        if (status == 4) {
            try {
                listener.blockComplete(origin);
                notifyCompleted(((BlockCompleteMessage) iMessageSnapshot).transmitToCompleted());
                return;
            } catch (Throwable th) {
                notifyError(messageHandler.prepareErrorMessage(th));
                return;
            }
        }
        FileDownloadLargeFileListener fileDownloadLargeFileListener = listener instanceof FileDownloadLargeFileListener ? (FileDownloadLargeFileListener) listener : null;
        switch (status) {
            case -4:
                listener.warn(origin);
                return;
            case -3:
                listener.completed(origin);
                return;
            case -2:
                if (fileDownloadLargeFileListener != null) {
                    fileDownloadLargeFileListener.paused(origin, iMessageSnapshot.getLargeSofarBytes(), iMessageSnapshot.getLargeTotalBytes());
                    return;
                } else {
                    listener.paused(origin, iMessageSnapshot.getSmallSofarBytes(), iMessageSnapshot.getSmallTotalBytes());
                    return;
                }
            case -1:
                listener.error(origin, iMessageSnapshot.getThrowable());
                return;
            case 0:
            case 4:
            default:
                return;
            case 1:
                if (fileDownloadLargeFileListener != null) {
                    fileDownloadLargeFileListener.pending(origin, iMessageSnapshot.getLargeSofarBytes(), iMessageSnapshot.getLargeTotalBytes());
                    return;
                } else {
                    listener.pending(origin, iMessageSnapshot.getSmallSofarBytes(), iMessageSnapshot.getSmallTotalBytes());
                    return;
                }
            case 2:
                if (fileDownloadLargeFileListener != null) {
                    fileDownloadLargeFileListener.connected(origin, iMessageSnapshot.getEtag(), iMessageSnapshot.isResuming(), origin.getLargeFileSoFarBytes(), iMessageSnapshot.getLargeTotalBytes());
                    return;
                } else {
                    listener.connected(origin, iMessageSnapshot.getEtag(), iMessageSnapshot.isResuming(), origin.getSmallFileSoFarBytes(), iMessageSnapshot.getSmallTotalBytes());
                    return;
                }
            case 3:
                if (fileDownloadLargeFileListener != null) {
                    fileDownloadLargeFileListener.progress(origin, iMessageSnapshot.getLargeSofarBytes(), origin.getLargeFileTotalBytes());
                    return;
                } else {
                    listener.progress(origin, iMessageSnapshot.getSmallSofarBytes(), origin.getSmallFileTotalBytes());
                    return;
                }
            case 5:
                if (fileDownloadLargeFileListener != null) {
                    fileDownloadLargeFileListener.retry(origin, iMessageSnapshot.getThrowable(), iMessageSnapshot.getRetryingTimes(), iMessageSnapshot.getLargeSofarBytes());
                    return;
                } else {
                    listener.retry(origin, iMessageSnapshot.getThrowable(), iMessageSnapshot.getRetryingTimes(), iMessageSnapshot.getSmallSofarBytes());
                    return;
                }
            case 6:
                listener.started(origin);
                return;
        }
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadMessenger
    public boolean isBlockingCompleted() {
        return this.parcelQueue.peek().getStatus() == 4;
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadMessenger
    public boolean notifyBegin() {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "notify begin %s", this.mTask);
        }
        if (this.mTask == null) {
            FileDownloadLog.w(this, "can't begin the task, the holder fo the messenger is nil, %d", Integer.valueOf(this.parcelQueue.size()));
            return false;
        }
        this.mLifeCycleCallback.onBegin();
        return true;
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadMessenger
    public void notifyBlockComplete(MessageSnapshot messageSnapshot) {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "notify block completed %s %s", this.mTask, Thread.currentThread().getName());
        }
        this.mLifeCycleCallback.onIng();
        process(messageSnapshot);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadMessenger
    public void notifyCompleted(MessageSnapshot messageSnapshot) {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "notify completed %s", this.mTask);
        }
        this.mLifeCycleCallback.onOver();
        process(messageSnapshot);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadMessenger
    public void notifyConnected(MessageSnapshot messageSnapshot) {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "notify connected %s", this.mTask);
        }
        this.mLifeCycleCallback.onIng();
        process(messageSnapshot);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadMessenger
    public void notifyError(MessageSnapshot messageSnapshot) {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "notify error %s %s", this.mTask, this.mTask.getOrigin().getErrorCause());
        }
        this.mLifeCycleCallback.onOver();
        process(messageSnapshot);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadMessenger
    public void notifyPaused(MessageSnapshot messageSnapshot) {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "notify paused %s", this.mTask);
        }
        this.mLifeCycleCallback.onOver();
        process(messageSnapshot);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadMessenger
    public void notifyPending(MessageSnapshot messageSnapshot) {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "notify pending %s", this.mTask);
        }
        this.mLifeCycleCallback.onIng();
        process(messageSnapshot);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadMessenger
    public void notifyProgress(MessageSnapshot messageSnapshot) {
        BaseDownloadTask origin = this.mTask.getOrigin();
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "notify progress %s %d %d", origin, Long.valueOf(origin.getLargeFileSoFarBytes()), Long.valueOf(origin.getLargeFileTotalBytes()));
        }
        if (origin.getCallbackProgressTimes() > 0) {
            this.mLifeCycleCallback.onIng();
            process(messageSnapshot);
        } else if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "notify progress but client not request notify %s", this.mTask);
        }
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadMessenger
    public void notifyRetry(MessageSnapshot messageSnapshot) {
        if (FileDownloadLog.NEED_LOG) {
            BaseDownloadTask origin = this.mTask.getOrigin();
            FileDownloadLog.d(this, "notify retry %s %d %d %s", this.mTask, Integer.valueOf(origin.getAutoRetryTimes()), Integer.valueOf(origin.getRetryingTimes()), origin.getErrorCause());
        }
        this.mLifeCycleCallback.onIng();
        process(messageSnapshot);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadMessenger
    public void notifyStarted(MessageSnapshot messageSnapshot) {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "notify started %s", this.mTask);
        }
        this.mLifeCycleCallback.onIng();
        process(messageSnapshot);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadMessenger
    public void notifyWarn(MessageSnapshot messageSnapshot) {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "notify warn %s", this.mTask);
        }
        this.mLifeCycleCallback.onOver();
        process(messageSnapshot);
    }

    @Override // com.liulishuo.filedownloader.IFileDownloadMessenger
    public void reAppointment(BaseDownloadTask.IRunningTask iRunningTask, BaseDownloadTask.LifeCycleCallback lifeCycleCallback) {
        if (this.mTask != null) {
            throw new IllegalStateException(FileDownloadUtils.formatString("the messenger is working, can't re-appointment for %s", iRunningTask));
        }
        init(iRunningTask, lifeCycleCallback);
    }

    public String toString() {
        Object[] objArr = new Object[2];
        objArr[0] = Integer.valueOf(this.mTask == null ? -1 : this.mTask.getOrigin().getId());
        objArr[1] = super.toString();
        return FileDownloadUtils.formatString("%d:%s", objArr);
    }
}
