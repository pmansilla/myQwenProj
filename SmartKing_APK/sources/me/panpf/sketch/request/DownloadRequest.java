package me.panpf.sketch.request;

import android.support.annotation.NonNull;
import me.panpf.sketch.SLog;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.cache.DiskCache;
import me.panpf.sketch.http.DownloadException;
import me.panpf.sketch.request.BaseRequest;
import me.panpf.sketch.uri.UriModel;

/* loaded from: classes2.dex */
public class DownloadRequest extends AsyncRequest {
    private DownloadListener downloadListener;
    private DownloadProgressListener downloadProgressListener;
    protected DownloadResult downloadResult;
    private DownloadOptions options;

    public DownloadRequest(Sketch sketch, String str, UriModel uriModel, String str2, DownloadOptions downloadOptions, DownloadListener downloadListener, DownloadProgressListener downloadProgressListener) {
        super(sketch, str, uriModel, str2);
        this.options = downloadOptions;
        this.downloadListener = downloadListener;
        this.downloadProgressListener = downloadProgressListener;
        setLogName("DownloadRequest");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.request.BaseRequest
    public void doCancel(@NonNull CancelCause cancelCause) {
        super.doCancel(cancelCause);
        if (this.downloadListener != null) {
            postRunCanceled();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.request.BaseRequest
    public void doError(@NonNull ErrorCause errorCause) {
        super.doError(errorCause);
        if (this.downloadListener != null) {
            postRunError();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void downloadCompleted() {
        if (this.downloadResult != null && this.downloadResult.hasData()) {
            postRunCompleted();
        } else {
            SLog.e(getLogName(), "Not found data after download completed. %s. %s", getThreadName(), getKey());
            doError(ErrorCause.DATA_LOST_AFTER_DOWNLOAD_COMPLETED);
        }
    }

    public DownloadResult getDownloadResult() {
        return this.downloadResult;
    }

    public DownloadOptions getOptions() {
        return this.options;
    }

    @Override // me.panpf.sketch.request.AsyncRequest
    public /* bridge */ /* synthetic */ boolean isSync() {
        return super.isSync();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.request.AsyncRequest
    public void runCanceledInMainThread() {
        if (this.downloadListener != null) {
            this.downloadListener.onCanceled(getCancelCause());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.request.AsyncRequest
    public void runCompletedInMainThread() {
        if (isCanceled()) {
            if (SLog.isLoggable(65538)) {
                SLog.d(getLogName(), "Request end before call completed. %s. %s", getThreadName(), getKey());
            }
        } else {
            setStatus(BaseRequest.Status.COMPLETED);
            if (this.downloadListener == null || this.downloadResult == null || !this.downloadResult.hasData()) {
                return;
            }
            this.downloadListener.onCompleted(this.downloadResult);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.request.AsyncRequest
    public void runDispatch() {
        if (isCanceled()) {
            if (SLog.isLoggable(65538)) {
                SLog.d(getLogName(), "Request end before dispatch. %s. %s", getThreadName(), getKey());
                return;
            }
            return;
        }
        if (!this.options.isCacheInDiskDisabled()) {
            setStatus(BaseRequest.Status.CHECK_DISK_CACHE);
            DiskCache.Entry entry = getConfiguration().getDiskCache().get(getDiskCacheKey());
            if (entry != null) {
                if (SLog.isLoggable(65538)) {
                    SLog.d(getLogName(), "Dispatch. Disk cache. %s. %s", getThreadName(), getKey());
                }
                this.downloadResult = new DownloadResult(entry, ImageFrom.DISK_CACHE);
                downloadCompleted();
                return;
            }
        }
        if (this.options.getRequestLevel() != RequestLevel.LOCAL) {
            if (SLog.isLoggable(65538)) {
                SLog.d(getLogName(), "Dispatch. Download. %s. %s", getThreadName(), getKey());
            }
            submitRunDownload();
        } else {
            doCancel(CancelCause.PAUSE_DOWNLOAD);
            if (SLog.isLoggable(2)) {
                SLog.d(getLogName(), "Request end because %s. %s. %s", CancelCause.PAUSE_DOWNLOAD, getThreadName(), getKey());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.request.AsyncRequest
    public void runDownload() {
        if (isCanceled()) {
            if (SLog.isLoggable(65538)) {
                SLog.d(getLogName(), "Request end before download. %s. %s", getThreadName(), getKey());
                return;
            }
            return;
        }
        try {
            this.downloadResult = getConfiguration().getDownloader().download(this);
            downloadCompleted();
        } catch (DownloadException e) {
            e.printStackTrace();
            doError(e.getErrorCause());
        } catch (CanceledException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.request.AsyncRequest
    public void runErrorInMainThread() {
        if (isCanceled()) {
            if (SLog.isLoggable(65538)) {
                SLog.d(getLogName(), "Request end before call error. %s. %s", getThreadName(), getKey());
            }
        } else if (this.downloadListener != null) {
            this.downloadListener.onError(getErrorCause());
        }
    }

    @Override // me.panpf.sketch.request.AsyncRequest
    protected void runLoad() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.request.AsyncRequest
    public void runUpdateProgressInMainThread(int i, int i2) {
        if (isFinished() || this.downloadProgressListener == null) {
            return;
        }
        this.downloadProgressListener.onUpdateDownloadProgress(i, i2);
    }

    @Override // me.panpf.sketch.request.AsyncRequest
    public /* bridge */ /* synthetic */ void setSync(boolean z) {
        super.setSync(z);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.request.AsyncRequest
    public void submitRunDispatch() {
        setStatus(BaseRequest.Status.WAIT_DISPATCH);
        super.submitRunDispatch();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.request.AsyncRequest
    public void submitRunDownload() {
        setStatus(BaseRequest.Status.WAIT_DOWNLOAD);
        super.submitRunDownload();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.request.AsyncRequest
    public void submitRunLoad() {
        setStatus(BaseRequest.Status.WAIT_LOAD);
        super.submitRunLoad();
    }

    public void updateProgress(int i, int i2) {
        if (this.downloadProgressListener == null || i <= 0) {
            return;
        }
        postRunUpdateProgress(i, i2);
    }
}
