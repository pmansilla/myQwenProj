package com.liulishuo.filedownloader.download;

import android.database.sqlite.SQLiteFullException;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.media.session.PlaybackStateCompat;
import com.liulishuo.filedownloader.database.FileDownloadDatabase;
import com.liulishuo.filedownloader.exception.FileDownloadGiveUpRetryException;
import com.liulishuo.filedownloader.exception.FileDownloadOutOfSpaceException;
import com.liulishuo.filedownloader.message.MessageSnapshotFlow;
import com.liulishuo.filedownloader.message.MessageSnapshotTaker;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.liulishuo.filedownloader.services.FileDownloadBroadcastHandler;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadProperties;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

/* loaded from: classes.dex */
public class DownloadStatusCallback implements Handler.Callback {
    private static final String ALREADY_DEAD_MESSAGE = "require callback %d but the host thread of the flow has already dead, what is occurred because of there are several reason can final this flow on different thread.";
    private static final int CALLBACK_SAFE_MIN_INTERVAL_BYTES = 1;
    private static final int CALLBACK_SAFE_MIN_INTERVAL_MILLIS = 5;
    private static final int NO_ANY_PROGRESS_CALLBACK = -1;
    private long callbackMinIntervalBytes;
    private final int callbackProgressMaxCount;
    private final int callbackProgressMinInterval;
    private Handler handler;
    private HandlerThread handlerThread;
    private final int maxRetryTimes;
    private final FileDownloadModel model;
    private volatile Thread parkThread;
    private final ProcessParams processParams;
    private volatile boolean handlingMessage = false;
    private volatile long lastCallbackTimestamp = 0;
    private final AtomicLong callbackIncreaseBuffer = new AtomicLong();
    private final AtomicBoolean needCallbackProgressToUser = new AtomicBoolean(false);
    private final AtomicBoolean needSetProcess = new AtomicBoolean(false);
    private final AtomicBoolean isFirstCallback = new AtomicBoolean(true);
    private final FileDownloadDatabase database = CustomComponentHolder.getImpl().getDatabaseInstance();

    /* loaded from: classes.dex */
    public static class ProcessParams {
        private Exception exception;
        private boolean isResuming;
        private int retryingTimes;

        public Exception getException() {
            return this.exception;
        }

        public int getRetryingTimes() {
            return this.retryingTimes;
        }

        public boolean isResuming() {
            return this.isResuming;
        }

        void setException(Exception exc) {
            this.exception = exc;
        }

        void setResuming(boolean z) {
            this.isResuming = z;
        }

        void setRetryingTimes(int i) {
            this.retryingTimes = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DownloadStatusCallback(FileDownloadModel fileDownloadModel, int i, int i2, int i3) {
        this.model = fileDownloadModel;
        this.callbackProgressMinInterval = i2 >= 5 ? i2 : 5;
        this.callbackProgressMaxCount = i3;
        this.processParams = new ProcessParams();
        this.maxRetryTimes = i;
    }

    private static long calculateCallbackMinIntervalBytes(long j, long j2) {
        if (j2 <= 0) {
            return -1L;
        }
        if (j == -1) {
            return 1L;
        }
        long j3 = j / j2;
        if (j3 <= 0) {
            return 1L;
        }
        return j3;
    }

    private Exception exFiltrate(Exception exc) {
        long length;
        String tempFilePath = this.model.getTempFilePath();
        if ((!this.model.isChunked() && !FileDownloadProperties.getImpl().fileNonPreAllocation) || !(exc instanceof IOException) || !new File(tempFilePath).exists()) {
            return exc;
        }
        long freeSpaceBytes = FileDownloadUtils.getFreeSpaceBytes(tempFilePath);
        if (freeSpaceBytes > PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM) {
            return exc;
        }
        File file = new File(tempFilePath);
        if (file.exists()) {
            length = file.length();
        } else {
            FileDownloadLog.e(this, exc, "Exception with: free space isn't enough, and the target file not exist.", new Object[0]);
            length = 0;
        }
        return Build.VERSION.SDK_INT >= 9 ? new FileDownloadOutOfSpaceException(freeSpaceBytes, PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM, length, exc) : new FileDownloadOutOfSpaceException(freeSpaceBytes, PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM, length);
    }

    private void handleCompleted() throws IOException {
        renameTempFile();
        this.model.setStatus((byte) -3);
        this.database.updateCompleted(this.model.getId(), this.model.getTotal());
        this.database.removeConnections(this.model.getId());
        onStatusChanged((byte) -3);
        if (FileDownloadProperties.getImpl().broadcastCompleted) {
            FileDownloadBroadcastHandler.sendCompletedBroadcast(this.model);
        }
    }

    private void handleError(Exception exc) {
        Exception exFiltrate = exFiltrate(exc);
        if (exFiltrate instanceof SQLiteFullException) {
            handleSQLiteFullException((SQLiteFullException) exFiltrate);
        } else {
            try {
                this.model.setStatus((byte) -1);
                this.model.setErrMsg(exc.toString());
                this.database.updateError(this.model.getId(), exFiltrate, this.model.getSoFar());
            } catch (SQLiteFullException e) {
                exFiltrate = e;
                handleSQLiteFullException((SQLiteFullException) exFiltrate);
            }
        }
        this.processParams.setException(exFiltrate);
        onStatusChanged((byte) -1);
    }

    private void handlePaused() {
        this.model.setStatus((byte) -2);
        this.database.updatePause(this.model.getId(), this.model.getSoFar());
        onStatusChanged((byte) -2);
    }

    private void handleProgress() {
        if (this.model.getSoFar() == this.model.getTotal()) {
            this.database.updateProgress(this.model.getId(), this.model.getSoFar());
            return;
        }
        if (this.needSetProcess.compareAndSet(true, false)) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.i(this, "handleProgress update model's status with progress", new Object[0]);
            }
            this.model.setStatus((byte) 3);
        }
        if (this.needCallbackProgressToUser.compareAndSet(true, false)) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.i(this, "handleProgress notify user progress status", new Object[0]);
            }
            onStatusChanged((byte) 3);
        }
    }

    private void handleRetry(Exception exc, int i) {
        Exception exFiltrate = exFiltrate(exc);
        this.processParams.setException(exFiltrate);
        this.processParams.setRetryingTimes(this.maxRetryTimes - i);
        this.model.setStatus((byte) 5);
        this.model.setErrMsg(exFiltrate.toString());
        this.database.updateRetry(this.model.getId(), exFiltrate);
        onStatusChanged((byte) 5);
    }

    private void handleSQLiteFullException(SQLiteFullException sQLiteFullException) {
        int id = this.model.getId();
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "the data of the task[%d] is dirty, because the SQLite full exception[%s], so remove it from the database directly.", Integer.valueOf(id), sQLiteFullException.toString());
        }
        this.model.setErrMsg(sQLiteFullException.toString());
        this.model.setStatus((byte) -1);
        this.database.remove(id);
        this.database.removeConnections(id);
    }

    private void inspectNeedCallbackToUser(long j) {
        boolean z;
        if (!this.isFirstCallback.compareAndSet(true, false)) {
            long j2 = j - this.lastCallbackTimestamp;
            if (this.callbackMinIntervalBytes == -1 || this.callbackIncreaseBuffer.get() < this.callbackMinIntervalBytes || j2 < this.callbackProgressMinInterval) {
                z = false;
                if (z || !this.needCallbackProgressToUser.compareAndSet(false, true)) {
                }
                if (FileDownloadLog.NEED_LOG) {
                    FileDownloadLog.i(this, "inspectNeedCallbackToUser need callback to user", new Object[0]);
                }
                this.lastCallbackTimestamp = j;
                this.callbackIncreaseBuffer.set(0L);
                return;
            }
        }
        z = true;
        if (z) {
        }
    }

    private boolean interceptBeforeCompleted() {
        if (this.model.isChunked()) {
            this.model.setTotal(this.model.getSoFar());
        } else if (this.model.getSoFar() != this.model.getTotal()) {
            onErrorDirectly(new FileDownloadGiveUpRetryException(FileDownloadUtils.formatString("sofar[%d] not equal total[%d]", Long.valueOf(this.model.getSoFar()), Long.valueOf(this.model.getTotal()))));
            return true;
        }
        return false;
    }

    private void onStatusChanged(byte b) {
        if (b != -2) {
            MessageSnapshotFlow.getImpl().inflow(MessageSnapshotTaker.take(b, this.model, this.processParams));
        } else if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "High concurrent cause, Already paused and we don't need to call-back to Task in here, %d", Integer.valueOf(this.model.getId()));
        }
    }

    private void renameTempFile() throws IOException {
        boolean z;
        String tempFilePath = this.model.getTempFilePath();
        String targetFilePath = this.model.getTargetFilePath();
        File file = new File(tempFilePath);
        try {
            File file2 = new File(targetFilePath);
            if (file2.exists()) {
                long length = file2.length();
                if (!file2.delete()) {
                    throw new IOException(FileDownloadUtils.formatString("Can't delete the old file([%s], [%d]), so can't replace it with the new downloaded one.", targetFilePath, Long.valueOf(length)));
                }
                FileDownloadLog.w(this, "The target file([%s], [%d]) will be replaced with the new downloaded file[%d]", targetFilePath, Long.valueOf(length), Long.valueOf(file.length()));
            }
            z = !file.renameTo(file2);
            if (!z) {
                if (z && file.exists() && !file.delete()) {
                    FileDownloadLog.w(this, "delete the temp file(%s) failed, on completed downloading.", tempFilePath);
                    return;
                }
                return;
            }
            try {
                throw new IOException(FileDownloadUtils.formatString("Can't rename the  temp downloaded file(%s) to the target file(%s)", tempFilePath, targetFilePath));
            } catch (Throwable th) {
                th = th;
                if (z && file.exists() && !file.delete()) {
                    FileDownloadLog.w(this, "delete the temp file(%s) failed, on completed downloading.", tempFilePath);
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            z = true;
        }
    }

    private synchronized void sendMessage(Message message) {
        if (!this.handlerThread.isAlive()) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, ALREADY_DEAD_MESSAGE, Integer.valueOf(message.what));
            }
            return;
        }
        try {
            this.handler.sendMessage(message);
        } catch (IllegalStateException e) {
            if (this.handlerThread.isAlive()) {
                throw e;
            }
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, ALREADY_DEAD_MESSAGE, Integer.valueOf(message.what));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void discardAllMessage() {
        if (this.handler != null) {
            this.handler.removeCallbacksAndMessages(null);
            this.handlerThread.quit();
            this.parkThread = Thread.currentThread();
            while (this.handlingMessage) {
                LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(100L));
            }
            this.parkThread = null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0020 A[DONT_GENERATE] */
    @Override // android.os.Handler.Callback
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean handleMessage(android.os.Message r5) {
        /*
            r4 = this;
            r0 = 1
            r4.handlingMessage = r0
            int r1 = r5.what
            r2 = 3
            r3 = 0
            if (r1 == r2) goto L17
            r2 = 5
            if (r1 == r2) goto Ld
            goto L1a
        Ld:
            java.lang.Object r1 = r5.obj     // Catch: java.lang.Throwable -> L26
            java.lang.Exception r1 = (java.lang.Exception) r1     // Catch: java.lang.Throwable -> L26
            int r5 = r5.arg1     // Catch: java.lang.Throwable -> L26
            r4.handleRetry(r1, r5)     // Catch: java.lang.Throwable -> L26
            goto L1a
        L17:
            r4.handleProgress()     // Catch: java.lang.Throwable -> L26
        L1a:
            r4.handlingMessage = r3
            java.lang.Thread r5 = r4.parkThread
            if (r5 == 0) goto L25
            java.lang.Thread r5 = r4.parkThread
            java.util.concurrent.locks.LockSupport.unpark(r5)
        L25:
            return r0
        L26:
            r5 = move-exception
            r4.handlingMessage = r3
            java.lang.Thread r0 = r4.parkThread
            if (r0 == 0) goto L32
            java.lang.Thread r0 = r4.parkThread
            java.util.concurrent.locks.LockSupport.unpark(r0)
        L32:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.liulishuo.filedownloader.download.DownloadStatusCallback.handleMessage(android.os.Message):boolean");
    }

    public boolean isAlive() {
        return this.handlerThread != null && this.handlerThread.isAlive();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onCompletedDirectly() throws IOException {
        if (interceptBeforeCompleted()) {
            return;
        }
        handleCompleted();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onConnected(boolean z, long j, String str, String str2) throws IllegalArgumentException {
        String eTag = this.model.getETag();
        if (eTag != null && !eTag.equals(str)) {
            throw new IllegalArgumentException(FileDownloadUtils.formatString("callback onConnected must with precondition succeed, but the etag is changes(%s != %s)", str, eTag));
        }
        this.processParams.setResuming(z);
        this.model.setStatus((byte) 2);
        this.model.setTotal(j);
        this.model.setETag(str);
        this.model.setFilename(str2);
        this.database.updateConnected(this.model.getId(), j, str, str2);
        onStatusChanged((byte) 2);
        this.callbackMinIntervalBytes = calculateCallbackMinIntervalBytes(j, this.callbackProgressMaxCount);
        this.needSetProcess.compareAndSet(false, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onErrorDirectly(Exception exc) {
        handleError(exc);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onMultiConnection() {
        this.handlerThread = new HandlerThread("source-status-callback");
        this.handlerThread.start();
        this.handler = new Handler(this.handlerThread.getLooper(), this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPausedDirectly() {
        handlePaused();
    }

    public void onPending() {
        this.model.setStatus((byte) 1);
        this.database.updatePending(this.model.getId());
        onStatusChanged((byte) 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onProgress(long j) {
        this.callbackIncreaseBuffer.addAndGet(j);
        this.model.increaseSoFar(j);
        inspectNeedCallbackToUser(SystemClock.elapsedRealtime());
        if (this.handler == null) {
            handleProgress();
        } else if (this.needCallbackProgressToUser.get()) {
            sendMessage(this.handler.obtainMessage(3));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onRetry(Exception exc, int i) {
        this.callbackIncreaseBuffer.set(0L);
        if (this.handler == null) {
            handleRetry(exc, i);
        } else {
            sendMessage(this.handler.obtainMessage(5, i, 0, exc));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onStartThread() {
        this.model.setStatus((byte) 6);
        onStatusChanged((byte) 6);
        this.database.onTaskStart(this.model.getId());
    }
}
