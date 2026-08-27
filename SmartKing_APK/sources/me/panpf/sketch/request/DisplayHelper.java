package me.panpf.sketch.request;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import me.panpf.sketch.Configuration;
import me.panpf.sketch.SLog;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.SketchView;
import me.panpf.sketch.decode.ImageSizeCalculator;
import me.panpf.sketch.display.ImageDisplayer;
import me.panpf.sketch.display.TransitionImageDisplayer;
import me.panpf.sketch.drawable.SketchBitmapDrawable;
import me.panpf.sketch.drawable.SketchLoadingDrawable;
import me.panpf.sketch.drawable.SketchRefBitmap;
import me.panpf.sketch.drawable.SketchShapeBitmapDrawable;
import me.panpf.sketch.process.ImageProcessor;
import me.panpf.sketch.request.Resize;
import me.panpf.sketch.request.ShapeSize;
import me.panpf.sketch.shaper.ImageShaper;
import me.panpf.sketch.state.StateImage;
import me.panpf.sketch.uri.UriModel;
import me.panpf.sketch.util.SketchUtils;
import me.panpf.sketch.util.Stopwatch;

/* loaded from: classes2.dex */
public class DisplayHelper {
    private static final String NAME = "DisplayHelper";
    private DisplayListener displayListener;
    private DownloadProgressListener downloadProgressListener;
    private String key;
    private Sketch sketch;
    private SketchView sketchView;
    private String uri;
    private UriModel uriModel;
    private DisplayOptions displayOptions = new DisplayOptions();
    private ViewInfo viewInfo = new ViewInfo();

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v5, types: [me.panpf.sketch.drawable.SketchShapeBitmapDrawable] */
    private boolean checkMemoryCache() {
        String str;
        SketchRefBitmap sketchRefBitmap;
        if (this.displayOptions.isCacheInMemoryDisabled() || (sketchRefBitmap = this.sketch.getConfiguration().getMemoryCache().get((str = this.key))) == null) {
            return true;
        }
        if (sketchRefBitmap.isRecycled()) {
            this.sketch.getConfiguration().getMemoryCache().remove(str);
            SLog.w(NAME, "Memory cache drawable recycled. %s. view(%s)", sketchRefBitmap.getInfo(), Integer.toHexString(this.sketchView.hashCode()));
            return true;
        }
        sketchRefBitmap.setIsWaitingUse(String.format("%s:waitingUse:fromMemory", NAME), true);
        if (SLog.isLoggable(65538)) {
            SLog.d(NAME, "Display image completed. %s. %s. view(%s)", ImageFrom.MEMORY_CACHE.name(), sketchRefBitmap.getInfo(), Integer.toHexString(this.sketchView.hashCode()));
        }
        SketchBitmapDrawable sketchBitmapDrawable = new SketchBitmapDrawable(sketchRefBitmap, ImageFrom.MEMORY_CACHE);
        if (this.displayOptions.getShapeSize() != null || this.displayOptions.getShaper() != null) {
            sketchBitmapDrawable = new SketchShapeBitmapDrawable(this.sketch.getConfiguration().getContext(), sketchBitmapDrawable, this.displayOptions.getShapeSize(), this.displayOptions.getShaper());
        }
        ImageDisplayer displayer = this.displayOptions.getDisplayer();
        if (displayer == null || !displayer.isAlwaysUse()) {
            this.sketchView.setImageDrawable(sketchBitmapDrawable);
        } else {
            displayer.display(this.sketchView, sketchBitmapDrawable);
        }
        if (this.displayListener != null) {
            this.displayListener.onCompleted(sketchBitmapDrawable, ImageFrom.MEMORY_CACHE, sketchRefBitmap.getAttrs());
        }
        sketchBitmapDrawable.setIsWaitingUse(String.format("%s:waitingUse:finish", NAME), false);
        return false;
    }

    private boolean checkParam() {
        Drawable drawable = null;
        if (TextUtils.isEmpty(this.uri)) {
            SLog.e(NAME, "Uri is empty. view(%s)", Integer.toHexString(this.sketchView.hashCode()));
            if (this.displayOptions.getErrorImage() != null) {
                drawable = this.displayOptions.getErrorImage().getDrawable(this.sketch.getConfiguration().getContext(), this.sketchView, this.displayOptions);
            } else if (this.displayOptions.getLoadingImage() != null) {
                drawable = this.displayOptions.getLoadingImage().getDrawable(this.sketch.getConfiguration().getContext(), this.sketchView, this.displayOptions);
            }
            this.sketchView.setImageDrawable(drawable);
            CallbackHandler.postCallbackError(this.displayListener, ErrorCause.URI_INVALID, false);
            return false;
        }
        if (this.uriModel != null) {
            return true;
        }
        SLog.e(NAME, "Not support uri. %s. view(%s)", this.uri, Integer.toHexString(this.sketchView.hashCode()));
        if (this.displayOptions.getErrorImage() != null) {
            drawable = this.displayOptions.getErrorImage().getDrawable(this.sketch.getConfiguration().getContext(), this.sketchView, this.displayOptions);
        } else if (this.displayOptions.getLoadingImage() != null) {
            drawable = this.displayOptions.getLoadingImage().getDrawable(this.sketch.getConfiguration().getContext(), this.sketchView, this.displayOptions);
        }
        this.sketchView.setImageDrawable(drawable);
        CallbackHandler.postCallbackError(this.displayListener, ErrorCause.URI_NO_SUPPORT, false);
        return false;
    }

    private DisplayRequest checkRepeatRequest() {
        DisplayRequest findDisplayRequest = SketchUtils.findDisplayRequest(this.sketchView);
        if (findDisplayRequest == null || findDisplayRequest.isFinished()) {
            return null;
        }
        if (this.key.equals(findDisplayRequest.getKey())) {
            if (SLog.isLoggable(65538)) {
                SLog.d(NAME, "Repeat request. key=%s. view(%s)", this.key, Integer.toHexString(this.sketchView.hashCode()));
            }
            return findDisplayRequest;
        }
        if (SLog.isLoggable(65538)) {
            SLog.d(NAME, "Cancel old request. newKey=%s. oldKey=%s. view(%s)", this.key, findDisplayRequest.getKey(), Integer.toHexString(this.sketchView.hashCode()));
        }
        findDisplayRequest.cancel(CancelCause.BE_REPLACED_ON_HELPER);
        return null;
    }

    private boolean checkRequestLevel() {
        if (this.displayOptions.getRequestLevel() == RequestLevel.MEMORY) {
            if (SLog.isLoggable(65538)) {
                SLog.d(NAME, "Request cancel. %s. view(%s). %s", CancelCause.PAUSE_LOAD, Integer.toHexString(this.sketchView.hashCode()), this.key);
            }
            r2 = this.displayOptions.getLoadingImage() != null ? this.displayOptions.getLoadingImage().getDrawable(this.sketch.getConfiguration().getContext(), this.sketchView, this.displayOptions) : null;
            this.sketchView.clearAnimation();
            this.sketchView.setImageDrawable(r2);
            CallbackHandler.postCallbackCanceled(this.displayListener, CancelCause.PAUSE_LOAD, false);
            return false;
        }
        if (this.displayOptions.getRequestLevel() != RequestLevel.LOCAL || !this.uriModel.isFromNet() || this.sketch.getConfiguration().getDiskCache().exist(this.uriModel.getDiskCacheKey(this.uri))) {
            return true;
        }
        if (SLog.isLoggable(65538)) {
            SLog.d(NAME, "Request cancel. %s. view(%s). %s", CancelCause.PAUSE_DOWNLOAD, Integer.toHexString(this.sketchView.hashCode()), this.key);
        }
        if (this.displayOptions.getPauseDownloadImage() != null) {
            r2 = this.displayOptions.getPauseDownloadImage().getDrawable(this.sketch.getConfiguration().getContext(), this.sketchView, this.displayOptions);
            this.sketchView.clearAnimation();
        } else if (this.displayOptions.getLoadingImage() != null) {
            r2 = this.displayOptions.getLoadingImage().getDrawable(this.sketch.getConfiguration().getContext(), this.sketchView, this.displayOptions);
        }
        this.sketchView.setImageDrawable(r2);
        CallbackHandler.postCallbackCanceled(this.displayListener, CancelCause.PAUSE_DOWNLOAD, false);
        return false;
    }

    private void saveParams() {
        DisplayCache displayCache = this.sketchView.getDisplayCache();
        if (displayCache == null) {
            displayCache = new DisplayCache();
            this.sketchView.setDisplayCache(displayCache);
        }
        displayCache.uri = this.uri;
        displayCache.options.copy(this.displayOptions);
    }

    private DisplayRequest submitRequest() {
        CallbackHandler.postCallbackStarted(this.displayListener, false);
        if (SLog.isLoggable(262146)) {
            Stopwatch.with().record("callbackStarted");
        }
        DisplayRequest newDisplayRequest = this.sketch.getConfiguration().getRequestFactory().newDisplayRequest(this.sketch, this.uri, this.uriModel, this.key, this.displayOptions, this.viewInfo, new RequestAndViewBinder(this.sketchView), this.displayListener, this.downloadProgressListener);
        if (SLog.isLoggable(262146)) {
            Stopwatch.with().record("createRequest");
        }
        StateImage loadingImage = this.displayOptions.getLoadingImage();
        SketchLoadingDrawable sketchLoadingDrawable = loadingImage != null ? new SketchLoadingDrawable(loadingImage.getDrawable(this.sketch.getConfiguration().getContext(), this.sketchView, this.displayOptions), newDisplayRequest) : new SketchLoadingDrawable(null, newDisplayRequest);
        if (SLog.isLoggable(262146)) {
            Stopwatch.with().record("createLoadingImage");
        }
        this.sketchView.setImageDrawable(sketchLoadingDrawable);
        if (SLog.isLoggable(262146)) {
            Stopwatch.with().record("setLoadingImage");
        }
        if (SLog.isLoggable(65538)) {
            SLog.d(NAME, "Run dispatch submitted. view(%s). %s", Integer.toHexString(this.sketchView.hashCode()), this.key);
        }
        newDisplayRequest.submit();
        if (SLog.isLoggable(262146)) {
            Stopwatch.with().record("submitRequest");
        }
        return newDisplayRequest;
    }

    @NonNull
    public DisplayHelper bitmapConfig(@Nullable Bitmap.Config config) {
        this.displayOptions.setBitmapConfig(config);
        return this;
    }

    @NonNull
    public DisplayHelper cacheProcessedImageInDisk() {
        this.displayOptions.setCacheProcessedImageInDisk(true);
        return this;
    }

    @Nullable
    public DisplayRequest commit() {
        if (!SketchUtils.isMainThread()) {
            SLog.w(NAME, "Please perform a commit in the UI thread. view(%s). %s", Integer.toHexString(this.sketchView.hashCode()), this.uri);
            if (SLog.isLoggable(262146)) {
                Stopwatch.with().print(this.uri);
            }
            this.sketch.getConfiguration().getHelperFactory().recycleDisplayHelper(this);
            return null;
        }
        boolean checkParam = checkParam();
        if (SLog.isLoggable(262146)) {
            Stopwatch.with().record("checkParam");
        }
        if (!checkParam) {
            if (SLog.isLoggable(262146)) {
                Stopwatch.with().print(this.uri);
            }
            this.sketch.getConfiguration().getHelperFactory().recycleDisplayHelper(this);
            return null;
        }
        preProcessOptions();
        if (SLog.isLoggable(262146)) {
            Stopwatch.with().record("preProcess");
        }
        saveParams();
        if (SLog.isLoggable(262146)) {
            Stopwatch.with().record("saveParams");
        }
        boolean checkMemoryCache = checkMemoryCache();
        if (SLog.isLoggable(262146)) {
            Stopwatch.with().record("checkMemoryCache");
        }
        if (!checkMemoryCache) {
            if (SLog.isLoggable(262146)) {
                Stopwatch.with().print(this.key);
            }
            this.sketch.getConfiguration().getHelperFactory().recycleDisplayHelper(this);
            return null;
        }
        boolean checkRequestLevel = checkRequestLevel();
        if (SLog.isLoggable(262146)) {
            Stopwatch.with().record("checkRequestLevel");
        }
        if (!checkRequestLevel) {
            if (SLog.isLoggable(262146)) {
                Stopwatch.with().print(this.key);
            }
            this.sketch.getConfiguration().getHelperFactory().recycleDisplayHelper(this);
            return null;
        }
        DisplayRequest checkRepeatRequest = checkRepeatRequest();
        if (SLog.isLoggable(262146)) {
            Stopwatch.with().record("checkRepeatRequest");
        }
        if (checkRepeatRequest != null) {
            if (SLog.isLoggable(262146)) {
                Stopwatch.with().print(this.key);
            }
            this.sketch.getConfiguration().getHelperFactory().recycleDisplayHelper(this);
            return checkRepeatRequest;
        }
        DisplayRequest submitRequest = submitRequest();
        if (SLog.isLoggable(262146)) {
            Stopwatch.with().print(this.key);
        }
        this.sketch.getConfiguration().getHelperFactory().recycleDisplayHelper(this);
        return submitRequest;
    }

    @NonNull
    public DisplayHelper decodeGifImage() {
        this.displayOptions.setDecodeGifImage(true);
        return this;
    }

    @NonNull
    public DisplayHelper disableBitmapPool() {
        this.displayOptions.setBitmapPoolDisabled(true);
        return this;
    }

    @NonNull
    public DisplayHelper disableCacheInDisk() {
        this.displayOptions.setCacheInDiskDisabled(true);
        return this;
    }

    @NonNull
    public DisplayHelper disableCacheInMemory() {
        this.displayOptions.setCacheInMemoryDisabled(true);
        return this;
    }

    @NonNull
    public DisplayHelper disableCorrectImageOrientation() {
        this.displayOptions.setCorrectImageOrientationDisabled(true);
        return this;
    }

    @NonNull
    public DisplayHelper displayer(@Nullable ImageDisplayer imageDisplayer) {
        this.displayOptions.setDisplayer(imageDisplayer);
        return this;
    }

    @NonNull
    public DisplayHelper errorImage(@DrawableRes int i) {
        this.displayOptions.setErrorImage(i);
        return this;
    }

    @NonNull
    public DisplayHelper errorImage(@Nullable StateImage stateImage) {
        this.displayOptions.setErrorImage(stateImage);
        return this;
    }

    @NonNull
    public DisplayHelper inPreferQualityOverSpeed(boolean z) {
        this.displayOptions.setInPreferQualityOverSpeed(z);
        return this;
    }

    public DisplayHelper init(@NonNull Sketch sketch, @NonNull String str, @NonNull SketchView sketchView) {
        this.sketch = sketch;
        this.uri = str;
        this.uriModel = UriModel.match(sketch, str);
        this.sketchView = sketchView;
        if (SLog.isLoggable(262146)) {
            Stopwatch.with().start("DisplayHelper. display use time");
        }
        this.sketchView.onReadyDisplay(this.uriModel);
        if (SLog.isLoggable(262146)) {
            Stopwatch.with().record("onReadyDisplay");
        }
        this.viewInfo.reset(sketchView, sketch);
        this.displayOptions.copy(sketchView.getOptions());
        if (SLog.isLoggable(262146)) {
            Stopwatch.with().record("init");
        }
        this.displayListener = sketchView.getDisplayListener();
        this.downloadProgressListener = sketchView.getDownloadProgressListener();
        return this;
    }

    @NonNull
    public DisplayHelper loadingImage(@DrawableRes int i) {
        this.displayOptions.setLoadingImage(i);
        return this;
    }

    @NonNull
    public DisplayHelper loadingImage(@Nullable StateImage stateImage) {
        this.displayOptions.setLoadingImage(stateImage);
        return this;
    }

    @NonNull
    public DisplayHelper lowQualityImage() {
        this.displayOptions.setLowQualityImage(true);
        return this;
    }

    @NonNull
    public DisplayHelper maxSize(int i, int i2) {
        this.displayOptions.setMaxSize(i, i2);
        return this;
    }

    @NonNull
    public DisplayHelper maxSize(@Nullable MaxSize maxSize) {
        this.displayOptions.setMaxSize(maxSize);
        return this;
    }

    @NonNull
    public DisplayHelper options(@Nullable DisplayOptions displayOptions) {
        this.displayOptions.copy(displayOptions);
        return this;
    }

    @NonNull
    public DisplayHelper pauseDownloadImage(@DrawableRes int i) {
        this.displayOptions.setPauseDownloadImage(i);
        return this;
    }

    @NonNull
    public DisplayHelper pauseDownloadImage(@Nullable StateImage stateImage) {
        this.displayOptions.setPauseDownloadImage(stateImage);
        return this;
    }

    protected void preProcessOptions() {
        Configuration configuration = this.sketch.getConfiguration();
        ImageSizeCalculator sizeCalculator = this.sketch.getConfiguration().getSizeCalculator();
        FixedSize fixedSize = this.viewInfo.getFixedSize();
        ShapeSize shapeSize = this.displayOptions.getShapeSize();
        if (shapeSize != null && (shapeSize instanceof ShapeSize.ByViewFixedSizeShapeSize)) {
            if (fixedSize == null) {
                throw new IllegalStateException("ImageView's width and height are not fixed, can not be applied with the ShapeSize.byViewFixedSize() function");
            }
            shapeSize = new ShapeSize(fixedSize.getWidth(), fixedSize.getHeight(), this.viewInfo.getScaleType());
            this.displayOptions.setShapeSize(shapeSize);
        }
        if (shapeSize != null && shapeSize.getScaleType() == null && this.sketchView != null) {
            shapeSize.setScaleType(this.viewInfo.getScaleType());
        }
        if (shapeSize != null && (shapeSize.getWidth() == 0 || shapeSize.getHeight() == 0)) {
            throw new IllegalArgumentException("ShapeSize width and height must be > 0");
        }
        Resize resize = this.displayOptions.getResize();
        if (resize != null && (resize instanceof Resize.ByViewFixedSizeResize)) {
            if (fixedSize == null) {
                throw new IllegalStateException("ImageView's width and height are not fixed, can not be applied with the Resize.byViewFixedSize() function");
            }
            Resize resize2 = new Resize(fixedSize.getWidth(), fixedSize.getHeight(), this.viewInfo.getScaleType(), resize.getMode());
            this.displayOptions.setResize(resize2);
            resize = resize2;
        }
        if (resize != null && resize.getScaleType() == null && this.sketchView != null) {
            resize.setScaleType(this.viewInfo.getScaleType());
        }
        if (resize != null && (resize.getWidth() <= 0 || resize.getHeight() <= 0)) {
            throw new IllegalArgumentException("Resize width and height must be > 0");
        }
        MaxSize maxSize = this.displayOptions.getMaxSize();
        if (maxSize == null) {
            maxSize = sizeCalculator.calculateImageMaxSize(this.sketchView);
            if (maxSize == null) {
                maxSize = sizeCalculator.getDefaultImageMaxSize(configuration.getContext());
            }
            this.displayOptions.setMaxSize(maxSize);
        }
        if (maxSize != null && maxSize.getWidth() <= 0 && maxSize.getHeight() <= 0) {
            throw new IllegalArgumentException("MaxSize width or height must be > 0");
        }
        if (this.displayOptions.getProcessor() == null && resize != null) {
            this.displayOptions.setProcessor(configuration.getResizeProcessor());
        }
        if (this.displayOptions.getDisplayer() == null) {
            this.displayOptions.setDisplayer(configuration.getDefaultDisplayer());
        }
        if ((this.displayOptions.getDisplayer() instanceof TransitionImageDisplayer) && this.displayOptions.getLoadingImage() != null && this.displayOptions.getShapeSize() == null) {
            if (fixedSize == null) {
                ViewGroup.LayoutParams layoutParams = this.sketchView.getLayoutParams();
                String format = String.format("If you use TransitionImageDisplayer and loadingImage, You must be setup ShapeSize or imageView width and height must be fixed. width=%s, height=%s", SketchUtils.viewLayoutFormatted(layoutParams != null ? layoutParams.width : -1), SketchUtils.viewLayoutFormatted(layoutParams != null ? layoutParams.height : -1));
                if (SLog.isLoggable(65538)) {
                    SLog.d(NAME, "%s. view(%s). %s", format, Integer.toHexString(this.sketchView.hashCode()), this.uri);
                }
                throw new IllegalArgumentException(format);
            }
            this.displayOptions.setShapeSize(fixedSize.getWidth(), fixedSize.getHeight());
        }
        configuration.getOptionsFilterManager().filter(this.displayOptions);
        this.key = SketchUtils.makeRequestKey(this.uri, this.uriModel, this.displayOptions.makeKey());
    }

    @NonNull
    public DisplayHelper processor(@Nullable ImageProcessor imageProcessor) {
        this.displayOptions.setProcessor(imageProcessor);
        return this;
    }

    @NonNull
    public DisplayHelper requestLevel(@Nullable RequestLevel requestLevel) {
        if (requestLevel != null) {
            this.displayOptions.setRequestLevel(requestLevel);
        }
        return this;
    }

    public void reset() {
        this.sketch = null;
        this.uri = null;
        this.uriModel = null;
        this.key = null;
        this.displayOptions.reset();
        this.displayListener = null;
        this.downloadProgressListener = null;
        this.viewInfo.reset(null, null);
        this.sketchView = null;
    }

    @NonNull
    public DisplayHelper resize(int i, int i2) {
        this.displayOptions.setResize(i, i2);
        return this;
    }

    @NonNull
    public DisplayHelper resize(int i, int i2, @NonNull ImageView.ScaleType scaleType) {
        this.displayOptions.setResize(i, i2, scaleType);
        return this;
    }

    @NonNull
    public DisplayHelper resize(@Nullable Resize resize) {
        this.displayOptions.setResize(resize);
        return this;
    }

    @NonNull
    public DisplayHelper shapeSize(int i, int i2) {
        this.displayOptions.setShapeSize(i, i2);
        return this;
    }

    @NonNull
    public DisplayHelper shapeSize(int i, int i2, ImageView.ScaleType scaleType) {
        this.displayOptions.setShapeSize(i, i2, scaleType);
        return this;
    }

    @NonNull
    public DisplayHelper shapeSize(@Nullable ShapeSize shapeSize) {
        this.displayOptions.setShapeSize(shapeSize);
        return this;
    }

    @NonNull
    public DisplayHelper shaper(@Nullable ImageShaper imageShaper) {
        this.displayOptions.setShaper(imageShaper);
        return this;
    }

    @NonNull
    public DisplayHelper thumbnailMode() {
        this.displayOptions.setThumbnailMode(true);
        return this;
    }
}
