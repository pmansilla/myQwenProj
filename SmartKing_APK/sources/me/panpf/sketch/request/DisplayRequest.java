package me.panpf.sketch.request;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.os.EnvironmentCompat;
import me.panpf.sketch.SLog;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.SketchView;
import me.panpf.sketch.cache.MemoryCache;
import me.panpf.sketch.drawable.SketchBitmapDrawable;
import me.panpf.sketch.drawable.SketchDrawable;
import me.panpf.sketch.drawable.SketchRefBitmap;
import me.panpf.sketch.drawable.SketchRefDrawable;
import me.panpf.sketch.drawable.SketchShapeBitmapDrawable;
import me.panpf.sketch.request.BaseRequest;
import me.panpf.sketch.uri.UriModel;

/* loaded from: classes2.dex */
public class DisplayRequest extends LoadRequest {
    private DisplayListener displayListener;
    private DisplayOptions displayOptions;
    protected DisplayResult displayResult;
    private RequestAndViewBinder requestAndViewBinder;
    private ViewInfo viewInfo;

    public DisplayRequest(Sketch sketch, String str, UriModel uriModel, String str2, DisplayOptions displayOptions, ViewInfo viewInfo, RequestAndViewBinder requestAndViewBinder, DisplayListener displayListener, DownloadProgressListener downloadProgressListener) {
        super(sketch, str, uriModel, str2, displayOptions, null, downloadProgressListener);
        this.viewInfo = viewInfo;
        this.displayOptions = displayOptions;
        this.requestAndViewBinder = requestAndViewBinder;
        this.displayListener = displayListener;
        this.requestAndViewBinder.setDisplayRequest(this);
        setLogName("DisplayRequest");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r12v0, types: [android.graphics.drawable.Drawable] */
    /* JADX WARN: Type inference failed for: r12v2 */
    /* JADX WARN: Type inference failed for: r12v3, types: [android.graphics.drawable.Drawable] */
    /* JADX WARN: Type inference failed for: r1v3, types: [me.panpf.sketch.display.ImageDisplayer] */
    private void displayImage(Drawable drawable) {
        if (isCanceled()) {
            if (SLog.isLoggable(65538)) {
                SLog.d(getLogName(), "Request end before call completed. %s. %s", getThreadName(), getKey());
                return;
            }
            return;
        }
        boolean z = drawable instanceof BitmapDrawable;
        if (z && ((BitmapDrawable) drawable).getBitmap().isRecycled()) {
            SketchDrawable sketchDrawable = (SketchDrawable) drawable;
            getConfiguration().getErrorTracker().onBitmapRecycledOnDisplay(this, sketchDrawable);
            if (SLog.isLoggable(65538)) {
                SLog.d(getLogName(), "Display image exception. bitmap recycled. %s. %s. %s. %s", sketchDrawable.getInfo(), this.displayResult.getImageFrom(), getThreadName(), getKey());
            }
            runErrorInMainThread();
            return;
        }
        if ((this.displayOptions.getShapeSize() != null || this.displayOptions.getShaper() != null) && z) {
            drawable = new SketchShapeBitmapDrawable(getConfiguration().getContext(), (BitmapDrawable) drawable, this.displayOptions.getShapeSize(), this.displayOptions.getShaper());
        }
        SketchView view = this.requestAndViewBinder.getView();
        if (SLog.isLoggable(65538)) {
            String str = EnvironmentCompat.MEDIA_UNKNOWN;
            if (drawable instanceof SketchRefDrawable) {
                str = drawable.getInfo();
            }
            SLog.d(getLogName(), "Display image completed. %s. %s. view(%s). %s. %s", this.displayResult.getImageFrom().name(), str, Integer.toHexString(view.hashCode()), getThreadName(), getKey());
        }
        setStatus(BaseRequest.Status.COMPLETED);
        this.displayOptions.getDisplayer().display(view, drawable);
        if (this.displayListener != null) {
            this.displayListener.onCompleted(this.displayResult.getDrawable(), this.displayResult.getImageFrom(), this.displayResult.getImageAttrs());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void displayCompleted() {
        postRunCompleted();
    }

    @Override // me.panpf.sketch.request.LoadRequest, me.panpf.sketch.request.DownloadRequest, me.panpf.sketch.request.BaseRequest
    protected void doCancel(@NonNull CancelCause cancelCause) {
        super.doCancel(cancelCause);
        if (this.displayListener != null) {
            postRunCanceled();
        }
    }

    @Override // me.panpf.sketch.request.LoadRequest, me.panpf.sketch.request.DownloadRequest, me.panpf.sketch.request.BaseRequest
    protected void doError(@NonNull ErrorCause errorCause) {
        if (this.displayListener == null && this.displayOptions.getErrorImage() == null) {
            super.doError(errorCause);
        } else {
            setErrorCause(errorCause);
            postRunError();
        }
    }

    public String getMemoryCacheKey() {
        return getKey();
    }

    @Override // me.panpf.sketch.request.LoadRequest, me.panpf.sketch.request.DownloadRequest
    public DisplayOptions getOptions() {
        return this.displayOptions;
    }

    public ViewInfo getViewInfo() {
        return this.viewInfo;
    }

    @Override // me.panpf.sketch.request.BaseRequest
    public boolean isCanceled() {
        if (super.isCanceled()) {
            return true;
        }
        if (!this.requestAndViewBinder.isBroken()) {
            return false;
        }
        if (SLog.isLoggable(2)) {
            SLog.d(getLogName(), "The request and the connection to the view are interrupted. %s. %s", getThreadName(), getKey());
        }
        doCancel(CancelCause.BIND_DISCONNECT);
        return true;
    }

    @Override // me.panpf.sketch.request.LoadRequest
    protected void loadCompleted() {
        LoadResult loadResult = getLoadResult();
        if (loadResult == null || loadResult.getBitmap() == null) {
            if (loadResult == null || loadResult.getGifDrawable() == null) {
                SLog.e(getLogName(), "Not found data after load completed. %s. %s", getThreadName(), getKey());
                doError(ErrorCause.DATA_LOST_AFTER_LOAD_COMPLETED);
                return;
            } else {
                this.displayResult = new DisplayResult((Drawable) loadResult.getGifDrawable(), loadResult.getImageFrom(), loadResult.getImageAttrs());
                displayCompleted();
                return;
            }
        }
        SketchRefBitmap sketchRefBitmap = new SketchRefBitmap(loadResult.getBitmap(), getKey(), getUri(), loadResult.getImageAttrs(), getConfiguration().getBitmapPool());
        sketchRefBitmap.setIsWaitingUse(String.format("%s:waitingUse:new", getLogName()), true);
        if (!this.displayOptions.isCacheInMemoryDisabled() && getMemoryCacheKey() != null) {
            getConfiguration().getMemoryCache().put(getMemoryCacheKey(), sketchRefBitmap);
        }
        this.displayResult = new DisplayResult(new SketchBitmapDrawable(sketchRefBitmap, loadResult.getImageFrom()), loadResult.getImageFrom(), loadResult.getImageAttrs());
        displayCompleted();
    }

    @Override // me.panpf.sketch.request.AsyncRequest
    protected void postRunCompleted() {
        setStatus(BaseRequest.Status.WAIT_DISPLAY);
        super.postRunCompleted();
    }

    @Override // me.panpf.sketch.request.AsyncRequest
    protected void postRunError() {
        setStatus(BaseRequest.Status.WAIT_DISPLAY);
        super.postRunError();
    }

    @Override // me.panpf.sketch.request.LoadRequest, me.panpf.sketch.request.DownloadRequest, me.panpf.sketch.request.AsyncRequest
    protected void runCanceledInMainThread() {
        if (this.displayListener != null) {
            this.displayListener.onCanceled(getCancelCause());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // me.panpf.sketch.request.LoadRequest, me.panpf.sketch.request.DownloadRequest, me.panpf.sketch.request.AsyncRequest
    protected void runCompletedInMainThread() {
        Drawable drawable = this.displayResult.getDrawable();
        if (drawable == 0) {
            if (SLog.isLoggable(65538)) {
                SLog.d(getLogName(), "Drawable is null before call completed. %s. %s", getThreadName(), getKey());
            }
        } else {
            displayImage(drawable);
            if (drawable instanceof SketchRefDrawable) {
                ((SketchRefDrawable) drawable).setIsWaitingUse(String.format("%s:waitingUse:finish", getLogName()), false);
            }
        }
    }

    @Override // me.panpf.sketch.request.LoadRequest, me.panpf.sketch.request.DownloadRequest, me.panpf.sketch.request.AsyncRequest
    protected void runErrorInMainThread() {
        if (isCanceled()) {
            if (SLog.isLoggable(65538)) {
                SLog.d(getLogName(), "Request end before call error. %s. %s", getThreadName(), getKey());
            }
        } else {
            setStatus(BaseRequest.Status.FAILED);
            if (this.displayOptions.getErrorImage() != null) {
                this.displayOptions.getDisplayer().display(this.requestAndViewBinder.getView(), this.displayOptions.getErrorImage().getDrawable(getContext(), this.requestAndViewBinder.getView(), this.displayOptions));
            }
            if (this.displayListener != null) {
                this.displayListener.onError(getErrorCause());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.request.LoadRequest, me.panpf.sketch.request.DownloadRequest, me.panpf.sketch.request.AsyncRequest
    public void runLoad() {
        if (isCanceled()) {
            if (SLog.isLoggable(65538)) {
                SLog.d(getLogName(), "Request end before decode. %s. %s", getThreadName(), getKey());
                return;
            }
            return;
        }
        if (!this.displayOptions.isCacheInDiskDisabled()) {
            setStatus(BaseRequest.Status.CHECK_MEMORY_CACHE);
            MemoryCache memoryCache = getConfiguration().getMemoryCache();
            SketchRefBitmap sketchRefBitmap = memoryCache.get(getMemoryCacheKey());
            if (sketchRefBitmap != null) {
                if (!sketchRefBitmap.isRecycled()) {
                    if (SLog.isLoggable(65538)) {
                        SLog.d(getLogName(), "From memory get drawable. bitmap=%s. %s. %s", sketchRefBitmap.getInfo(), getThreadName(), getKey());
                    }
                    sketchRefBitmap.setIsWaitingUse(String.format("%s:waitingUse:fromMemory", getLogName()), true);
                    this.displayResult = new DisplayResult(new SketchBitmapDrawable(sketchRefBitmap, ImageFrom.MEMORY_CACHE), ImageFrom.MEMORY_CACHE, sketchRefBitmap.getAttrs());
                    displayCompleted();
                    return;
                }
                memoryCache.remove(getMemoryCacheKey());
                SLog.e(getLogName(), "Memory cache drawable recycled. bitmap=%s. %s. %s", sketchRefBitmap.getInfo(), getThreadName(), getKey());
            }
        }
        super.runLoad();
    }
}
