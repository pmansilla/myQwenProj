package com.liulishuo.filedownloader;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.message.MessageSnapshot;

/* loaded from: classes.dex */
interface IFileDownloadMessenger {
    void discard();

    boolean handoverDirectly();

    void handoverMessage();

    boolean isBlockingCompleted();

    boolean notifyBegin();

    void notifyBlockComplete(MessageSnapshot messageSnapshot);

    void notifyCompleted(MessageSnapshot messageSnapshot);

    void notifyConnected(MessageSnapshot messageSnapshot);

    void notifyError(MessageSnapshot messageSnapshot);

    void notifyPaused(MessageSnapshot messageSnapshot);

    void notifyPending(MessageSnapshot messageSnapshot);

    void notifyProgress(MessageSnapshot messageSnapshot);

    void notifyRetry(MessageSnapshot messageSnapshot);

    void notifyStarted(MessageSnapshot messageSnapshot);

    void notifyWarn(MessageSnapshot messageSnapshot);

    void reAppointment(BaseDownloadTask.IRunningTask iRunningTask, BaseDownloadTask.LifeCycleCallback lifeCycleCallback);
}
