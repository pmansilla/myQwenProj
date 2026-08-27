package me.panpf.sketch.request;

import android.content.Context;
import android.support.annotation.NonNull;
import me.panpf.sketch.Configuration;
import me.panpf.sketch.SLog;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.uri.UriModel;

/* loaded from: classes2.dex */
public abstract class BaseRequest {
    private CancelCause cancelCause;
    private String diskCacheKey;
    private ErrorCause errorCause;
    private String key;
    private String logName = "Request";
    private Sketch sketch;
    private Status status;
    private String uri;
    private UriModel uriModel;

    /* loaded from: classes2.dex */
    public enum Status {
        WAIT_DISPATCH,
        START_DISPATCH,
        INTERCEPT_LOCAL_TASK,
        WAIT_DOWNLOAD,
        START_DOWNLOAD,
        CHECK_DISK_CACHE,
        CONNECTING,
        READ_DATA,
        WAIT_LOAD,
        START_LOAD,
        CHECK_MEMORY_CACHE,
        DECODING,
        PROCESSING,
        WAIT_DISPLAY,
        COMPLETED,
        FAILED,
        CANCELED
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BaseRequest(@NonNull Sketch sketch, @NonNull String str, @NonNull UriModel uriModel, @NonNull String str2) {
        this.sketch = sketch;
        this.uri = str;
        this.uriModel = uriModel;
        this.key = str2;
    }

    public boolean cancel(CancelCause cancelCause) {
        if (isFinished()) {
            return false;
        }
        doCancel(cancelCause);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void doCancel(@NonNull CancelCause cancelCause) {
        setCancelCause(cancelCause);
        setStatus(Status.CANCELED);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void doError(@NonNull ErrorCause errorCause) {
        setErrorCause(errorCause);
        setStatus(Status.FAILED);
    }

    public CancelCause getCancelCause() {
        return this.cancelCause;
    }

    public Configuration getConfiguration() {
        return this.sketch.getConfiguration();
    }

    public Context getContext() {
        return this.sketch.getConfiguration().getContext();
    }

    public String getDiskCacheKey() {
        if (this.diskCacheKey == null) {
            this.diskCacheKey = this.uriModel.getDiskCacheKey(this.uri);
        }
        return this.diskCacheKey;
    }

    public ErrorCause getErrorCause() {
        return this.errorCause;
    }

    public String getKey() {
        return this.key;
    }

    public String getLogName() {
        return this.logName;
    }

    public Sketch getSketch() {
        return this.sketch;
    }

    public Status getStatus() {
        return this.status;
    }

    public String getThreadName() {
        return Thread.currentThread().getName();
    }

    public String getUri() {
        return this.uri;
    }

    public UriModel getUriModel() {
        return this.uriModel;
    }

    public boolean isCanceled() {
        return this.status == Status.CANCELED;
    }

    public boolean isFinished() {
        return this.status == null || this.status == Status.COMPLETED || this.status == Status.CANCELED || this.status == Status.FAILED;
    }

    protected void setCancelCause(@NonNull CancelCause cancelCause) {
        if (isFinished()) {
            return;
        }
        this.cancelCause = cancelCause;
        if (SLog.isLoggable(65538)) {
            SLog.d(getLogName(), "Request cancel. %s. %s. %s", cancelCause.name(), getThreadName(), getKey());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setErrorCause(@NonNull ErrorCause errorCause) {
        if (isFinished()) {
            return;
        }
        this.errorCause = errorCause;
        if (SLog.isLoggable(65538)) {
            SLog.d(getLogName(), "Request error. %s. %s. %s", errorCause.name(), getThreadName(), getKey());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLogName(String str) {
        this.logName = str;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
