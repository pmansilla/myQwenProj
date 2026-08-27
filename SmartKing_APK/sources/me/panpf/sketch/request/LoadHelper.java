package me.panpf.sketch.request;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import me.panpf.sketch.Configuration;
import me.panpf.sketch.SLog;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.process.ImageProcessor;
import me.panpf.sketch.request.Resize;
import me.panpf.sketch.uri.UriModel;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class LoadHelper {
    private static final String NAME = "LoadHelper";
    private DownloadProgressListener downloadProgressListener;
    private String key;
    private LoadListener loadListener;
    private LoadOptions loadOptions = new LoadOptions();
    private Sketch sketch;
    private boolean sync;
    private String uri;
    private UriModel uriModel;

    public LoadHelper(@NonNull Sketch sketch, @NonNull String str, @NonNull LoadListener loadListener) {
        this.sketch = sketch;
        this.uri = str;
        this.uriModel = UriModel.match(sketch, str);
        this.loadListener = loadListener;
    }

    private boolean checkParam() {
        if (this.loadListener == null) {
            SLog.e(NAME, "Load request must have LoadListener. %s", this.uri);
        }
        if (TextUtils.isEmpty(this.uri)) {
            SLog.e(NAME, "Uri is empty");
            CallbackHandler.postCallbackError(this.loadListener, ErrorCause.URI_INVALID, this.sync);
            return false;
        }
        if (this.uriModel != null) {
            return true;
        }
        SLog.e(NAME, "Not support uri. %s", this.uri);
        CallbackHandler.postCallbackError(this.loadListener, ErrorCause.URI_NO_SUPPORT, this.sync);
        return false;
    }

    private boolean checkRequestLevel() {
        if (this.loadOptions.getRequestLevel() != RequestLevel.LOCAL || !this.uriModel.isFromNet() || this.sketch.getConfiguration().getDiskCache().exist(this.uriModel.getDiskCacheKey(this.uri))) {
            return true;
        }
        if (SLog.isLoggable(65538)) {
            SLog.d(NAME, "Request cancel. %s. %s", CancelCause.PAUSE_DOWNLOAD, this.key);
        }
        CallbackHandler.postCallbackCanceled(this.loadListener, CancelCause.PAUSE_DOWNLOAD, this.sync);
        return false;
    }

    private LoadRequest submitRequest() {
        CallbackHandler.postCallbackStarted(this.loadListener, this.sync);
        LoadRequest newLoadRequest = this.sketch.getConfiguration().getRequestFactory().newLoadRequest(this.sketch, this.uri, this.uriModel, this.key, this.loadOptions, this.loadListener, this.downloadProgressListener);
        newLoadRequest.setSync(this.sync);
        if (SLog.isLoggable(65538)) {
            SLog.d(NAME, "Run dispatch submitted. %s", this.key);
        }
        newLoadRequest.submit();
        return newLoadRequest;
    }

    @NonNull
    public LoadHelper bitmapConfig(@Nullable Bitmap.Config config) {
        this.loadOptions.setBitmapConfig(config);
        return this;
    }

    @NonNull
    public LoadHelper cacheProcessedImageInDisk() {
        this.loadOptions.setCacheProcessedImageInDisk(true);
        return this;
    }

    @Nullable
    public LoadRequest commit() {
        if (this.sync && SketchUtils.isMainThread()) {
            throw new IllegalStateException("Cannot sync perform the load in the UI thread ");
        }
        if (!checkParam()) {
            return null;
        }
        preProcessOptions();
        if (checkRequestLevel()) {
            return submitRequest();
        }
        return null;
    }

    @NonNull
    public LoadHelper decodeGifImage() {
        this.loadOptions.setDecodeGifImage(true);
        return this;
    }

    @NonNull
    public LoadHelper disableBitmapPool() {
        this.loadOptions.setBitmapPoolDisabled(true);
        return this;
    }

    @NonNull
    public LoadHelper disableCacheInDisk() {
        this.loadOptions.setCacheInDiskDisabled(true);
        return this;
    }

    @NonNull
    public LoadHelper disableCorrectImageOrientation() {
        this.loadOptions.setCorrectImageOrientationDisabled(true);
        return this;
    }

    @NonNull
    public LoadHelper downloadProgressListener(@Nullable DownloadProgressListener downloadProgressListener) {
        this.downloadProgressListener = downloadProgressListener;
        return this;
    }

    @NonNull
    public LoadHelper inPreferQualityOverSpeed(boolean z) {
        this.loadOptions.setInPreferQualityOverSpeed(z);
        return this;
    }

    @NonNull
    public LoadHelper lowQualityImage() {
        this.loadOptions.setLowQualityImage(true);
        return this;
    }

    @NonNull
    public LoadHelper maxSize(int i, int i2) {
        this.loadOptions.setMaxSize(i, i2);
        return this;
    }

    @NonNull
    public LoadHelper maxSize(@Nullable MaxSize maxSize) {
        this.loadOptions.setMaxSize(maxSize);
        return this;
    }

    @NonNull
    public LoadHelper options(@Nullable LoadOptions loadOptions) {
        this.loadOptions.copy(loadOptions);
        return this;
    }

    protected void preProcessOptions() {
        Configuration configuration = this.sketch.getConfiguration();
        Resize resize = this.loadOptions.getResize();
        if (resize != null && (resize instanceof Resize.ByViewFixedSizeResize)) {
            this.loadOptions.setResize(null);
            resize = null;
        }
        if (resize != null && (resize.getWidth() <= 0 || resize.getHeight() <= 0)) {
            throw new IllegalArgumentException("Resize width and height must be > 0");
        }
        MaxSize maxSize = this.loadOptions.getMaxSize();
        if (maxSize == null) {
            maxSize = configuration.getSizeCalculator().getDefaultImageMaxSize(configuration.getContext());
            this.loadOptions.setMaxSize(maxSize);
        }
        if (maxSize != null && maxSize.getWidth() <= 0 && maxSize.getHeight() <= 0) {
            throw new IllegalArgumentException("MaxSize width or height must be > 0");
        }
        if (this.loadOptions.getProcessor() == null && resize != null) {
            this.loadOptions.setProcessor(configuration.getResizeProcessor());
        }
        configuration.getOptionsFilterManager().filter(this.loadOptions);
        this.key = SketchUtils.makeRequestKey(this.uri, this.uriModel, this.loadOptions.makeKey());
    }

    @NonNull
    public LoadHelper processor(@Nullable ImageProcessor imageProcessor) {
        this.loadOptions.setProcessor(imageProcessor);
        return this;
    }

    @NonNull
    public LoadHelper requestLevel(@Nullable RequestLevel requestLevel) {
        if (requestLevel != null) {
            this.loadOptions.setRequestLevel(requestLevel);
        }
        return this;
    }

    @NonNull
    public LoadHelper resize(int i, int i2) {
        this.loadOptions.setResize(i, i2);
        return this;
    }

    @NonNull
    public LoadHelper resize(int i, int i2, @NonNull ImageView.ScaleType scaleType) {
        this.loadOptions.setResize(i, i2, scaleType);
        return this;
    }

    @NonNull
    public LoadHelper resize(@Nullable Resize resize) {
        this.loadOptions.setResize(resize);
        return this;
    }

    @NonNull
    public LoadHelper sync() {
        this.sync = true;
        return this;
    }

    @NonNull
    public LoadHelper thumbnailMode() {
        this.loadOptions.setThumbnailMode(true);
        return this;
    }
}
