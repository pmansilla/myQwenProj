package me.panpf.sketch.request;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import me.panpf.sketch.process.ImageProcessor;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class LoadOptions extends DownloadOptions {
    private Bitmap.Config bitmapConfig;
    private boolean bitmapPoolDisabled;
    private boolean cacheProcessedImageInDisk;
    private boolean correctImageOrientationDisabled;
    private boolean decodeGifImage;
    private boolean inPreferQualityOverSpeed;
    private boolean lowQualityImage;
    private MaxSize maxSize;
    private ImageProcessor processor;
    private Resize resize;
    private boolean thumbnailMode;

    public LoadOptions() {
        reset();
    }

    public LoadOptions(@NonNull LoadOptions loadOptions) {
        copy(loadOptions);
    }

    public void copy(@Nullable LoadOptions loadOptions) {
        if (loadOptions == null) {
            return;
        }
        super.copy((DownloadOptions) loadOptions);
        this.maxSize = loadOptions.maxSize;
        this.resize = loadOptions.resize;
        this.lowQualityImage = loadOptions.lowQualityImage;
        this.processor = loadOptions.processor;
        this.decodeGifImage = loadOptions.decodeGifImage;
        this.bitmapConfig = loadOptions.bitmapConfig;
        this.inPreferQualityOverSpeed = loadOptions.inPreferQualityOverSpeed;
        this.thumbnailMode = loadOptions.thumbnailMode;
        this.cacheProcessedImageInDisk = loadOptions.cacheProcessedImageInDisk;
        this.bitmapPoolDisabled = loadOptions.bitmapPoolDisabled;
        this.correctImageOrientationDisabled = loadOptions.correctImageOrientationDisabled;
    }

    @Nullable
    public Bitmap.Config getBitmapConfig() {
        return this.bitmapConfig;
    }

    @Nullable
    public MaxSize getMaxSize() {
        return this.maxSize;
    }

    @Nullable
    public ImageProcessor getProcessor() {
        return this.processor;
    }

    @Nullable
    public Resize getResize() {
        return this.resize;
    }

    public boolean isBitmapPoolDisabled() {
        return this.bitmapPoolDisabled;
    }

    public boolean isCacheProcessedImageInDisk() {
        return this.cacheProcessedImageInDisk;
    }

    public boolean isCorrectImageOrientationDisabled() {
        return this.correctImageOrientationDisabled;
    }

    public boolean isDecodeGifImage() {
        return this.decodeGifImage;
    }

    public boolean isInPreferQualityOverSpeed() {
        return this.inPreferQualityOverSpeed;
    }

    public boolean isLowQualityImage() {
        return this.lowQualityImage;
    }

    public boolean isThumbnailMode() {
        return this.thumbnailMode;
    }

    @Override // me.panpf.sketch.request.DownloadOptions
    @NonNull
    public String makeKey() {
        StringBuilder sb = new StringBuilder();
        if (this.maxSize != null) {
            if (sb.length() > 0) {
                sb.append('-');
            }
            sb.append(this.maxSize.getKey());
        }
        if (this.resize != null) {
            if (sb.length() > 0) {
                sb.append('-');
            }
            sb.append(this.resize.getKey());
            if (this.thumbnailMode) {
                if (sb.length() > 0) {
                    sb.append('-');
                }
                sb.append("thumbnailMode");
            }
        }
        if (this.correctImageOrientationDisabled) {
            if (sb.length() > 0) {
                sb.append('-');
            }
            sb.append("ignoreOrientation");
        }
        if (this.lowQualityImage) {
            if (sb.length() > 0) {
                sb.append('-');
            }
            sb.append("lowQuality");
        }
        if (this.inPreferQualityOverSpeed) {
            if (sb.length() > 0) {
                sb.append('-');
            }
            sb.append("preferQuality");
        }
        if (this.decodeGifImage) {
            if (sb.length() > 0) {
                sb.append('-');
            }
            sb.append("gif");
        }
        if (this.bitmapConfig != null) {
            if (sb.length() > 0) {
                sb.append('-');
            }
            sb.append(this.bitmapConfig.name());
        }
        if (this.processor != null) {
            String key = this.processor.getKey();
            if (!TextUtils.isEmpty(key)) {
                if (sb.length() > 0) {
                    sb.append('-');
                }
                sb.append(key);
            }
        }
        return sb.toString();
    }

    @Override // me.panpf.sketch.request.DownloadOptions
    @NonNull
    public String makeStateImageKey() {
        StringBuilder sb = new StringBuilder();
        if (this.resize != null) {
            if (sb.length() > 0) {
                sb.append('-');
            }
            sb.append(this.resize.getKey());
        }
        if (this.lowQualityImage) {
            if (sb.length() > 0) {
                sb.append('-');
            }
            sb.append("lowQuality");
        }
        if (this.processor != null) {
            String key = this.processor.getKey();
            if (!TextUtils.isEmpty(key)) {
                if (sb.length() > 0) {
                    sb.append('-');
                }
                sb.append(key);
            }
        }
        return sb.toString();
    }

    @Override // me.panpf.sketch.request.DownloadOptions
    public void reset() {
        super.reset();
        this.maxSize = null;
        this.resize = null;
        this.lowQualityImage = false;
        this.processor = null;
        this.decodeGifImage = false;
        this.bitmapConfig = null;
        this.inPreferQualityOverSpeed = false;
        this.thumbnailMode = false;
        this.cacheProcessedImageInDisk = false;
        this.bitmapPoolDisabled = false;
        this.correctImageOrientationDisabled = false;
    }

    @NonNull
    public LoadOptions setBitmapConfig(@Nullable Bitmap.Config config) {
        if (config == Bitmap.Config.ARGB_4444 && SketchUtils.isDisabledARGB4444()) {
            config = Bitmap.Config.ARGB_8888;
        }
        this.bitmapConfig = config;
        return this;
    }

    @NonNull
    public LoadOptions setBitmapPoolDisabled(boolean z) {
        this.bitmapPoolDisabled = z;
        return this;
    }

    @Override // me.panpf.sketch.request.DownloadOptions
    @NonNull
    public LoadOptions setCacheInDiskDisabled(boolean z) {
        return (LoadOptions) super.setCacheInDiskDisabled(z);
    }

    @NonNull
    public LoadOptions setCacheProcessedImageInDisk(boolean z) {
        this.cacheProcessedImageInDisk = z;
        return this;
    }

    @NonNull
    public LoadOptions setCorrectImageOrientationDisabled(boolean z) {
        this.correctImageOrientationDisabled = z;
        return this;
    }

    @NonNull
    public LoadOptions setDecodeGifImage(boolean z) {
        this.decodeGifImage = z;
        return this;
    }

    @NonNull
    public LoadOptions setInPreferQualityOverSpeed(boolean z) {
        this.inPreferQualityOverSpeed = z;
        return this;
    }

    @NonNull
    public LoadOptions setLowQualityImage(boolean z) {
        this.lowQualityImage = z;
        return this;
    }

    @NonNull
    public LoadOptions setMaxSize(int i, int i2) {
        this.maxSize = new MaxSize(i, i2);
        return this;
    }

    @NonNull
    public LoadOptions setMaxSize(@Nullable MaxSize maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    @NonNull
    public LoadOptions setProcessor(@Nullable ImageProcessor imageProcessor) {
        this.processor = imageProcessor;
        return this;
    }

    @Override // me.panpf.sketch.request.DownloadOptions
    @NonNull
    public LoadOptions setRequestLevel(@Nullable RequestLevel requestLevel) {
        return (LoadOptions) super.setRequestLevel(requestLevel);
    }

    @NonNull
    public LoadOptions setResize(int i, int i2) {
        this.resize = new Resize(i, i2);
        return this;
    }

    @NonNull
    public LoadOptions setResize(int i, int i2, @Nullable ImageView.ScaleType scaleType) {
        this.resize = new Resize(i, i2, scaleType);
        return this;
    }

    @NonNull
    public LoadOptions setResize(@Nullable Resize resize) {
        this.resize = resize;
        return this;
    }

    @NonNull
    public LoadOptions setThumbnailMode(boolean z) {
        this.thumbnailMode = z;
        return this;
    }
}
