package me.panpf.sketch.request;

import me.panpf.sketch.Sketch;
import me.panpf.sketch.request.BaseRequest;
import me.panpf.sketch.uri.UriModel;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class AsyncRequest extends BaseRequest implements Runnable {
    private RunStatus runStatus;
    private boolean sync;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public enum RunStatus {
        DISPATCH,
        LOAD,
        DOWNLOAD
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AsyncRequest(Sketch sketch, String str, UriModel uriModel, String str2) {
        super(sketch, str, uriModel, str2);
    }

    private void executeDispatch() {
        setStatus(BaseRequest.Status.START_DISPATCH);
        runDispatch();
    }

    private void executeDownload() {
        setStatus(BaseRequest.Status.START_DOWNLOAD);
        runDownload();
    }

    private void executeLoad() {
        setStatus(BaseRequest.Status.START_LOAD);
        runLoad();
    }

    public boolean isSync() {
        return this.sync;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postRunCanceled() {
        CallbackHandler.postRunCanceled(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void postRunCompleted() {
        CallbackHandler.postRunCompleted(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void postRunError() {
        CallbackHandler.postRunError(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postRunUpdateProgress(int i, int i2) {
        CallbackHandler.postRunUpdateProgress(this, i, i2);
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (this.runStatus != null) {
            switch (this.runStatus) {
                case DISPATCH:
                    executeDispatch();
                    return;
                case DOWNLOAD:
                    executeDownload();
                    return;
                case LOAD:
                    executeLoad();
                    return;
                default:
                    new IllegalArgumentException("unknown runStatus: " + this.runStatus.name()).printStackTrace();
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void runCanceledInMainThread();

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void runCompletedInMainThread();

    protected abstract void runDispatch();

    protected abstract void runDownload();

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void runErrorInMainThread();

    protected abstract void runLoad();

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void runUpdateProgressInMainThread(int i, int i2);

    public void setSync(boolean z) {
        this.sync = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void submit() {
        submitRunDispatch();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void submitRunDispatch() {
        this.runStatus = RunStatus.DISPATCH;
        if (this.sync) {
            executeDispatch();
        } else {
            getConfiguration().getExecutor().submitDispatch(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void submitRunDownload() {
        this.runStatus = RunStatus.DOWNLOAD;
        if (this.sync) {
            executeDownload();
        } else {
            getConfiguration().getExecutor().submitDownload(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void submitRunLoad() {
        this.runStatus = RunStatus.LOAD;
        if (this.sync) {
            executeLoad();
        } else {
            getConfiguration().getExecutor().submitLoad(this);
        }
    }
}
