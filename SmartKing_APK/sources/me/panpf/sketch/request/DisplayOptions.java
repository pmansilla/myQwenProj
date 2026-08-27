package me.panpf.sketch.request;

import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import me.panpf.sketch.display.ImageDisplayer;
import me.panpf.sketch.process.ImageProcessor;
import me.panpf.sketch.shaper.ImageShaper;
import me.panpf.sketch.state.DrawableStateImage;
import me.panpf.sketch.state.StateImage;

/* loaded from: classes2.dex */
public class DisplayOptions extends LoadOptions {
    private boolean cacheInMemoryDisabled;
    private ImageDisplayer displayer;
    private StateImage errorImage;
    private StateImage loadingImage;
    private StateImage pauseDownloadImage;
    private ShapeSize shapeSize;
    private ImageShaper shaper;

    public DisplayOptions() {
        reset();
    }

    public DisplayOptions(DisplayOptions displayOptions) {
        copy(displayOptions);
    }

    public void copy(@Nullable DisplayOptions displayOptions) {
        if (displayOptions == null) {
            return;
        }
        super.copy((LoadOptions) displayOptions);
        this.cacheInMemoryDisabled = displayOptions.cacheInMemoryDisabled;
        this.displayer = displayOptions.displayer;
        this.loadingImage = displayOptions.loadingImage;
        this.errorImage = displayOptions.errorImage;
        this.pauseDownloadImage = displayOptions.pauseDownloadImage;
        this.shaper = displayOptions.shaper;
        this.shapeSize = displayOptions.shapeSize;
    }

    @Nullable
    public ImageDisplayer getDisplayer() {
        return this.displayer;
    }

    @Nullable
    public StateImage getErrorImage() {
        return this.errorImage;
    }

    @Nullable
    public StateImage getLoadingImage() {
        return this.loadingImage;
    }

    @Nullable
    public StateImage getPauseDownloadImage() {
        return this.pauseDownloadImage;
    }

    @Nullable
    public ShapeSize getShapeSize() {
        return this.shapeSize;
    }

    @Nullable
    public ImageShaper getShaper() {
        return this.shaper;
    }

    public boolean isCacheInMemoryDisabled() {
        return this.cacheInMemoryDisabled;
    }

    @Override // me.panpf.sketch.request.LoadOptions, me.panpf.sketch.request.DownloadOptions
    public void reset() {
        super.reset();
        this.cacheInMemoryDisabled = false;
        this.displayer = null;
        this.loadingImage = null;
        this.errorImage = null;
        this.pauseDownloadImage = null;
        this.shaper = null;
        this.shapeSize = null;
    }

    @Override // me.panpf.sketch.request.LoadOptions
    @NonNull
    public DisplayOptions setBitmapConfig(@Nullable Bitmap.Config config) {
        return (DisplayOptions) super.setBitmapConfig(config);
    }

    @Override // me.panpf.sketch.request.LoadOptions
    @NonNull
    public DisplayOptions setBitmapPoolDisabled(boolean z) {
        return (DisplayOptions) super.setBitmapPoolDisabled(z);
    }

    @Override // me.panpf.sketch.request.LoadOptions, me.panpf.sketch.request.DownloadOptions
    @NonNull
    public DisplayOptions setCacheInDiskDisabled(boolean z) {
        return (DisplayOptions) super.setCacheInDiskDisabled(z);
    }

    @NonNull
    public DisplayOptions setCacheInMemoryDisabled(boolean z) {
        this.cacheInMemoryDisabled = z;
        return this;
    }

    @Override // me.panpf.sketch.request.LoadOptions
    @NonNull
    public DisplayOptions setCacheProcessedImageInDisk(boolean z) {
        return (DisplayOptions) super.setCacheProcessedImageInDisk(z);
    }

    @Override // me.panpf.sketch.request.LoadOptions
    @NonNull
    public DisplayOptions setCorrectImageOrientationDisabled(boolean z) {
        return (DisplayOptions) super.setCorrectImageOrientationDisabled(z);
    }

    @Override // me.panpf.sketch.request.LoadOptions
    @NonNull
    public DisplayOptions setDecodeGifImage(boolean z) {
        return (DisplayOptions) super.setDecodeGifImage(z);
    }

    @NonNull
    public DisplayOptions setDisplayer(@Nullable ImageDisplayer imageDisplayer) {
        this.displayer = imageDisplayer;
        return this;
    }

    @NonNull
    public DisplayOptions setErrorImage(@DrawableRes int i) {
        setErrorImage(new DrawableStateImage(i));
        return this;
    }

    @NonNull
    public DisplayOptions setErrorImage(@Nullable StateImage stateImage) {
        this.errorImage = stateImage;
        return this;
    }

    @Override // me.panpf.sketch.request.LoadOptions
    @NonNull
    public DisplayOptions setInPreferQualityOverSpeed(boolean z) {
        return (DisplayOptions) super.setInPreferQualityOverSpeed(z);
    }

    @NonNull
    public DisplayOptions setLoadingImage(@DrawableRes int i) {
        setLoadingImage(new DrawableStateImage(i));
        return this;
    }

    @NonNull
    public DisplayOptions setLoadingImage(@Nullable StateImage stateImage) {
        this.loadingImage = stateImage;
        return this;
    }

    @Override // me.panpf.sketch.request.LoadOptions
    @NonNull
    public DisplayOptions setLowQualityImage(boolean z) {
        return (DisplayOptions) super.setLowQualityImage(z);
    }

    @Override // me.panpf.sketch.request.LoadOptions
    @NonNull
    public DisplayOptions setMaxSize(int i, int i2) {
        return (DisplayOptions) super.setMaxSize(i, i2);
    }

    @Override // me.panpf.sketch.request.LoadOptions
    @NonNull
    public DisplayOptions setMaxSize(@Nullable MaxSize maxSize) {
        return (DisplayOptions) super.setMaxSize(maxSize);
    }

    @NonNull
    public DisplayOptions setPauseDownloadImage(@DrawableRes int i) {
        setPauseDownloadImage(new DrawableStateImage(i));
        return this;
    }

    @NonNull
    public DisplayOptions setPauseDownloadImage(@Nullable StateImage stateImage) {
        this.pauseDownloadImage = stateImage;
        return this;
    }

    @Override // me.panpf.sketch.request.LoadOptions
    @NonNull
    public DisplayOptions setProcessor(@Nullable ImageProcessor imageProcessor) {
        return (DisplayOptions) super.setProcessor(imageProcessor);
    }

    @Override // me.panpf.sketch.request.LoadOptions, me.panpf.sketch.request.DownloadOptions
    @NonNull
    public DisplayOptions setRequestLevel(@Nullable RequestLevel requestLevel) {
        return (DisplayOptions) super.setRequestLevel(requestLevel);
    }

    @Override // me.panpf.sketch.request.LoadOptions
    @NonNull
    public DisplayOptions setResize(int i, int i2) {
        return (DisplayOptions) super.setResize(i, i2);
    }

    @Override // me.panpf.sketch.request.LoadOptions
    @NonNull
    public DisplayOptions setResize(int i, int i2, @Nullable ImageView.ScaleType scaleType) {
        return (DisplayOptions) super.setResize(i, i2, scaleType);
    }

    @Override // me.panpf.sketch.request.LoadOptions
    @NonNull
    public DisplayOptions setResize(@Nullable Resize resize) {
        return (DisplayOptions) super.setResize(resize);
    }

    @NonNull
    public DisplayOptions setShapeSize(int i, int i2) {
        return setShapeSize(new ShapeSize(i, i2));
    }

    @NonNull
    public DisplayOptions setShapeSize(int i, int i2, @Nullable ImageView.ScaleType scaleType) {
        return setShapeSize(new ShapeSize(i, i2, scaleType));
    }

    @NonNull
    public DisplayOptions setShapeSize(@Nullable ShapeSize shapeSize) {
        this.shapeSize = shapeSize;
        return this;
    }

    @NonNull
    public DisplayOptions setShaper(@Nullable ImageShaper imageShaper) {
        this.shaper = imageShaper;
        return this;
    }

    @Override // me.panpf.sketch.request.LoadOptions
    @NonNull
    public DisplayOptions setThumbnailMode(boolean z) {
        return (DisplayOptions) super.setThumbnailMode(z);
    }
}
