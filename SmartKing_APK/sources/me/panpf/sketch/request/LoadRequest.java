package me.panpf.sketch.request;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import me.panpf.sketch.SLog;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.cache.BitmapPoolUtils;
import me.panpf.sketch.datasource.DataSource;
import me.panpf.sketch.datasource.DiskCacheDataSource;
import me.panpf.sketch.decode.BitmapDecodeResult;
import me.panpf.sketch.decode.DecodeException;
import me.panpf.sketch.decode.DecodeResult;
import me.panpf.sketch.decode.GifDecodeResult;
import me.panpf.sketch.decode.ImageAttrs;
import me.panpf.sketch.decode.ProcessedImageCache;
import me.panpf.sketch.drawable.SketchGifDrawable;
import me.panpf.sketch.request.BaseRequest;
import me.panpf.sketch.uri.GetDataSourceException;
import me.panpf.sketch.uri.UriModel;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class LoadRequest extends FreeRideDownloadRequest {
    private LoadListener loadListener;
    private LoadOptions loadOptions;
    private LoadResult loadResult;

    public LoadRequest(Sketch sketch, String str, UriModel uriModel, String str2, LoadOptions loadOptions, LoadListener loadListener, DownloadProgressListener downloadProgressListener) {
        super(sketch, str, uriModel, str2, loadOptions, null, downloadProgressListener);
        this.loadOptions = loadOptions;
        this.loadListener = loadListener;
        setLogName("LoadRequest");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.request.DownloadRequest, me.panpf.sketch.request.BaseRequest
    public void doCancel(@NonNull CancelCause cancelCause) {
        super.doCancel(cancelCause);
        if (this.loadListener != null) {
            postRunCanceled();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.request.DownloadRequest, me.panpf.sketch.request.BaseRequest
    public void doError(@NonNull ErrorCause errorCause) {
        super.doError(errorCause);
        if (this.loadListener != null) {
            postRunError();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.request.DownloadRequest
    public void downloadCompleted() {
        DownloadResult downloadResult = getDownloadResult();
        if (downloadResult != null && downloadResult.hasData()) {
            submitRunLoad();
        } else {
            SLog.e(getLogName(), "Not found data after download completed. %s. %s", getThreadName(), getKey());
            doError(ErrorCause.DATA_LOST_AFTER_DOWNLOAD_COMPLETED);
        }
    }

    @NonNull
    public DataSource getDataSource() throws GetDataSourceException {
        return getUriModel().getDataSource(getContext(), getUri(), getUriModel().isFromNet() ? getDownloadResult() : null);
    }

    @NonNull
    public DataSource getDataSourceWithPressedCache() throws GetDataSourceException {
        DiskCacheDataSource diskCache;
        ProcessedImageCache processedImageCache = getConfiguration().getProcessedImageCache();
        return (!processedImageCache.canUse(getOptions()) || (diskCache = processedImageCache.getDiskCache(this)) == null) ? getDataSource() : diskCache;
    }

    public LoadResult getLoadResult() {
        return this.loadResult;
    }

    @Override // me.panpf.sketch.request.DownloadRequest
    public LoadOptions getOptions() {
        return this.loadOptions;
    }

    public String getProcessedDiskCacheKey() {
        return getKey();
    }

    protected void loadCompleted() {
        postRunCompleted();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.request.DownloadRequest, me.panpf.sketch.request.AsyncRequest
    public void runCanceledInMainThread() {
        if (this.loadListener != null) {
            this.loadListener.onCanceled(getCancelCause());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.request.DownloadRequest, me.panpf.sketch.request.AsyncRequest
    public void runCompletedInMainThread() {
        if (!isCanceled()) {
            setStatus(BaseRequest.Status.COMPLETED);
            if (this.loadListener == null || this.loadResult == null) {
                return;
            }
            this.loadListener.onCompleted(this.loadResult);
            return;
        }
        if (this.loadResult != null && this.loadResult.getBitmap() != null) {
            BitmapPoolUtils.freeBitmapToPool(this.loadResult.getBitmap(), getConfiguration().getBitmapPool());
        } else if (this.loadResult != null && this.loadResult.getGifDrawable() != null) {
            this.loadResult.getGifDrawable().recycle();
        }
        if (SLog.isLoggable(65538)) {
            SLog.d(getLogName(), "Request end before call completed. %s. %s", getThreadName(), getKey());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.request.DownloadRequest, me.panpf.sketch.request.AsyncRequest
    public void runDispatch() {
        if (isCanceled()) {
            if (SLog.isLoggable(65538)) {
                SLog.d(getLogName(), "Request end before dispatch. %s. %s", getThreadName(), getKey());
                return;
            }
            return;
        }
        setStatus(BaseRequest.Status.INTERCEPT_LOCAL_TASK);
        if (!getUriModel().isFromNet()) {
            if (SLog.isLoggable(65538)) {
                SLog.d(getLogName(), "Dispatch. Local image. %s. %s", getThreadName(), getKey());
            }
            submitRunLoad();
            return;
        }
        ProcessedImageCache processedImageCache = getConfiguration().getProcessedImageCache();
        if (!processedImageCache.canUse(getOptions()) || !processedImageCache.checkDiskCache(this)) {
            super.runDispatch();
            return;
        }
        if (SLog.isLoggable(65538)) {
            SLog.d(getLogName(), "Dispatch. Processed disk cache. %s. %s", getThreadName(), getKey());
        }
        submitRunLoad();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.request.DownloadRequest, me.panpf.sketch.request.AsyncRequest
    public void runErrorInMainThread() {
        if (isCanceled()) {
            if (SLog.isLoggable(65538)) {
                SLog.d(getLogName(), "Request end before call err. %s. %s", getThreadName(), getKey());
            }
        } else if (this.loadListener != null) {
            this.loadListener.onError(getErrorCause());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.request.DownloadRequest, me.panpf.sketch.request.AsyncRequest
    public void runLoad() {
        if (isCanceled()) {
            if (SLog.isLoggable(65538)) {
                SLog.d(getLogName(), "Request end before decode. %s. %s", getThreadName(), getKey());
                return;
            }
            return;
        }
        setStatus(BaseRequest.Status.DECODING);
        try {
            DecodeResult decode = getConfiguration().getDecoder().decode(this);
            if (decode instanceof BitmapDecodeResult) {
                Bitmap bitmap = ((BitmapDecodeResult) decode).getBitmap();
                if (bitmap.isRecycled()) {
                    ImageAttrs imageAttrs = decode.getImageAttrs();
                    SLog.e(getLogName(), "Decode failed because bitmap recycled. bitmapInfo: %s. %s. %s", SketchUtils.makeImageInfo(null, imageAttrs.getWidth(), imageAttrs.getHeight(), imageAttrs.getMimeType(), imageAttrs.getExifOrientation(), bitmap, SketchUtils.getByteCount(bitmap), null), getThreadName(), getKey());
                    doError(ErrorCause.BITMAP_RECYCLED);
                    return;
                }
                if (SLog.isLoggable(65538)) {
                    ImageAttrs imageAttrs2 = decode.getImageAttrs();
                    SLog.d(getLogName(), "Decode success. bitmapInfo: %s. %s. %s", SketchUtils.makeImageInfo(null, imageAttrs2.getWidth(), imageAttrs2.getHeight(), imageAttrs2.getMimeType(), imageAttrs2.getExifOrientation(), bitmap, SketchUtils.getByteCount(bitmap), null), getThreadName(), getKey());
                }
                if (!isCanceled()) {
                    this.loadResult = new LoadResult(bitmap, decode);
                    loadCompleted();
                    return;
                } else {
                    BitmapPoolUtils.freeBitmapToPool(bitmap, getConfiguration().getBitmapPool());
                    if (SLog.isLoggable(65538)) {
                        SLog.d(getLogName(), "Request end after decode. %s. %s", getThreadName(), getKey());
                        return;
                    }
                    return;
                }
            }
            if (!(decode instanceof GifDecodeResult)) {
                SLog.e(getLogName(), "Unknown DecodeResult type. %S. %s. %s", decode.getClass().getName(), getThreadName(), getKey());
                doError(ErrorCause.DECODE_UNKNOWN_RESULT_TYPE);
                return;
            }
            SketchGifDrawable gifDrawable = ((GifDecodeResult) decode).getGifDrawable();
            if (gifDrawable.isRecycled()) {
                SLog.e(getLogName(), "Decode failed because gif drawable recycled. gifInfo: %s. %s. %s", gifDrawable.getInfo(), getThreadName(), getKey());
                doError(ErrorCause.GIF_DRAWABLE_RECYCLED);
                return;
            }
            if (SLog.isLoggable(65538)) {
                SLog.d(getLogName(), "Decode gif success. gifInfo: %s. %s. %s", gifDrawable.getInfo(), getThreadName(), getKey());
            }
            if (!isCanceled()) {
                this.loadResult = new LoadResult(gifDrawable, decode);
                loadCompleted();
            } else {
                gifDrawable.recycle();
                if (SLog.isLoggable(65538)) {
                    SLog.d(getLogName(), "Request end after decode. %s. %s", getThreadName(), getKey());
                }
            }
        } catch (DecodeException e) {
            e.printStackTrace();
            doError(e.getErrorCause());
        }
    }
}
