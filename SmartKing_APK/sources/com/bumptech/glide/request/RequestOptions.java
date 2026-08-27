package com.bumptech.glide.request;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableTransformation;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import com.bumptech.glide.load.resource.gif.ByteBufferGifDecoder;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawableTransformation;
import com.bumptech.glide.load.resource.gif.StreamGifDecoder;
import com.bumptech.glide.signature.EmptySignature;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class RequestOptions implements Cloneable {
    private static final int DISK_CACHE_STRATEGY = 4;
    private static final int ERROR_ID = 32;
    private static final int ERROR_PLACEHOLDER = 16;
    private static final int FALLBACK = 8192;
    private static final int FALLBACK_ID = 16384;
    private static final int IS_CACHEABLE = 256;
    private static final int ONLY_RETRIEVE_FROM_CACHE = 524288;
    private static final int OVERRIDE = 512;
    private static final int PLACEHOLDER = 64;
    private static final int PLACEHOLDER_ID = 128;
    private static final int PRIORITY = 8;
    private static final int RESOURCE_CLASS = 4096;
    private static final int SIGNATURE = 1024;
    private static final int SIZE_MULTIPLIER = 2;
    private static final int THEME = 32768;
    private static final int TRANSFORMATION = 2048;
    private static final int TRANSFORMATION_ALLOWED = 65536;
    private static final int TRANSFORMATION_REQUIRED = 131072;
    private static final int UNSET = -1;
    private static final int USE_UNLIMITED_SOURCE_GENERATORS_POOL = 262144;
    private static RequestOptions centerCropOptions;
    private static RequestOptions centerInsideOptions;
    private static RequestOptions circleCropOptions;
    private static RequestOptions fitCenterOptions;
    private static RequestOptions noAnimationOptions;
    private static RequestOptions noTransformOptions;
    private static RequestOptions skipMemoryCacheFalseOptions;
    private static RequestOptions skipMemoryCacheTrueOptions;
    private int errorId;
    private Drawable errorPlaceholder;
    private Drawable fallbackDrawable;
    private int fallbackId;
    private int fields;
    private boolean isAutoCloneEnabled;
    private boolean isLocked;
    private boolean isTransformationRequired;
    private boolean onlyRetrieveFromCache;
    private Drawable placeholderDrawable;
    private int placeholderId;
    private Resources.Theme theme;
    private boolean useUnlimitedSourceGeneratorsPool;
    private float sizeMultiplier = 1.0f;
    private DiskCacheStrategy diskCacheStrategy = DiskCacheStrategy.AUTOMATIC;
    private Priority priority = Priority.NORMAL;
    private boolean isCacheable = true;
    private int overrideHeight = -1;
    private int overrideWidth = -1;
    private Key signature = EmptySignature.obtain();
    private boolean isTransformationAllowed = true;
    private Options options = new Options();
    private Map<Class<?>, Transformation<?>> transformations = new HashMap();
    private Class<?> resourceClass = Object.class;

    public static RequestOptions bitmapTransform(@NonNull Transformation<Bitmap> transformation) {
        return new RequestOptions().transform(transformation);
    }

    public static RequestOptions centerCropTransform() {
        if (centerCropOptions == null) {
            centerCropOptions = new RequestOptions().centerCrop().autoClone();
        }
        return centerCropOptions;
    }

    public static RequestOptions centerInsideTransform() {
        if (centerInsideOptions == null) {
            centerInsideOptions = new RequestOptions().centerInside().autoClone();
        }
        return centerInsideOptions;
    }

    public static RequestOptions circleCropTransform() {
        if (circleCropOptions == null) {
            circleCropOptions = new RequestOptions().circleCrop().autoClone();
        }
        return circleCropOptions;
    }

    public static RequestOptions decodeTypeOf(@NonNull Class<?> cls) {
        return new RequestOptions().decode(cls);
    }

    public static RequestOptions diskCacheStrategyOf(@NonNull DiskCacheStrategy diskCacheStrategy) {
        return new RequestOptions().diskCacheStrategy(diskCacheStrategy);
    }

    public static RequestOptions downsampleOf(@NonNull DownsampleStrategy downsampleStrategy) {
        return new RequestOptions().downsample(downsampleStrategy);
    }

    public static RequestOptions encodeFormatOf(@NonNull Bitmap.CompressFormat compressFormat) {
        return new RequestOptions().encodeFormat(compressFormat);
    }

    public static RequestOptions encodeQualityOf(int i) {
        return new RequestOptions().encodeQuality(i);
    }

    public static RequestOptions errorOf(int i) {
        return new RequestOptions().error(i);
    }

    public static RequestOptions errorOf(@Nullable Drawable drawable) {
        return new RequestOptions().error(drawable);
    }

    public static RequestOptions fitCenterTransform() {
        if (fitCenterOptions == null) {
            fitCenterOptions = new RequestOptions().fitCenter().autoClone();
        }
        return fitCenterOptions;
    }

    public static RequestOptions formatOf(@NonNull DecodeFormat decodeFormat) {
        return new RequestOptions().format(decodeFormat);
    }

    public static RequestOptions frameOf(long j) {
        return new RequestOptions().frame(j);
    }

    private boolean isSet(int i) {
        return isSet(this.fields, i);
    }

    private static boolean isSet(int i, int i2) {
        return (i & i2) != 0;
    }

    public static RequestOptions noAnimation() {
        if (noAnimationOptions == null) {
            noAnimationOptions = new RequestOptions().dontAnimate().autoClone();
        }
        return noAnimationOptions;
    }

    public static RequestOptions noTransformation() {
        if (noTransformOptions == null) {
            noTransformOptions = new RequestOptions().dontTransform().autoClone();
        }
        return noTransformOptions;
    }

    public static <T> RequestOptions option(@NonNull Option<T> option, @NonNull T t) {
        return new RequestOptions().set(option, t);
    }

    public static RequestOptions overrideOf(int i) {
        return overrideOf(i, i);
    }

    public static RequestOptions overrideOf(int i, int i2) {
        return new RequestOptions().override(i, i2);
    }

    public static RequestOptions placeholderOf(int i) {
        return new RequestOptions().placeholder(i);
    }

    public static RequestOptions placeholderOf(@Nullable Drawable drawable) {
        return new RequestOptions().placeholder(drawable);
    }

    public static RequestOptions priorityOf(@NonNull Priority priority) {
        return new RequestOptions().priority(priority);
    }

    private RequestOptions selfOrThrowIfLocked() {
        if (this.isLocked) {
            throw new IllegalStateException("You cannot modify locked RequestOptions, consider clone()");
        }
        return this;
    }

    public static RequestOptions signatureOf(@NonNull Key key) {
        return new RequestOptions().signature(key);
    }

    public static RequestOptions sizeMultiplierOf(float f) {
        return new RequestOptions().sizeMultiplier(f);
    }

    public static RequestOptions skipMemoryCacheOf(boolean z) {
        if (z) {
            if (skipMemoryCacheTrueOptions == null) {
                skipMemoryCacheTrueOptions = new RequestOptions().skipMemoryCache(true).autoClone();
            }
            return skipMemoryCacheTrueOptions;
        }
        if (skipMemoryCacheFalseOptions == null) {
            skipMemoryCacheFalseOptions = new RequestOptions().skipMemoryCache(false).autoClone();
        }
        return skipMemoryCacheFalseOptions;
    }

    public RequestOptions apply(RequestOptions requestOptions) {
        if (this.isAutoCloneEnabled) {
            return m25clone().apply(requestOptions);
        }
        if (isSet(requestOptions.fields, 2)) {
            this.sizeMultiplier = requestOptions.sizeMultiplier;
        }
        if (isSet(requestOptions.fields, 262144)) {
            this.useUnlimitedSourceGeneratorsPool = requestOptions.useUnlimitedSourceGeneratorsPool;
        }
        if (isSet(requestOptions.fields, 4)) {
            this.diskCacheStrategy = requestOptions.diskCacheStrategy;
        }
        if (isSet(requestOptions.fields, 8)) {
            this.priority = requestOptions.priority;
        }
        if (isSet(requestOptions.fields, 16)) {
            this.errorPlaceholder = requestOptions.errorPlaceholder;
        }
        if (isSet(requestOptions.fields, 32)) {
            this.errorId = requestOptions.errorId;
        }
        if (isSet(requestOptions.fields, 64)) {
            this.placeholderDrawable = requestOptions.placeholderDrawable;
        }
        if (isSet(requestOptions.fields, 128)) {
            this.placeholderId = requestOptions.placeholderId;
        }
        if (isSet(requestOptions.fields, 256)) {
            this.isCacheable = requestOptions.isCacheable;
        }
        if (isSet(requestOptions.fields, 512)) {
            this.overrideWidth = requestOptions.overrideWidth;
            this.overrideHeight = requestOptions.overrideHeight;
        }
        if (isSet(requestOptions.fields, 1024)) {
            this.signature = requestOptions.signature;
        }
        if (isSet(requestOptions.fields, 4096)) {
            this.resourceClass = requestOptions.resourceClass;
        }
        if (isSet(requestOptions.fields, 8192)) {
            this.fallbackDrawable = requestOptions.fallbackDrawable;
        }
        if (isSet(requestOptions.fields, 16384)) {
            this.fallbackId = requestOptions.fallbackId;
        }
        if (isSet(requestOptions.fields, 32768)) {
            this.theme = requestOptions.theme;
        }
        if (isSet(requestOptions.fields, 65536)) {
            this.isTransformationAllowed = requestOptions.isTransformationAllowed;
        }
        if (isSet(requestOptions.fields, 131072)) {
            this.isTransformationRequired = requestOptions.isTransformationRequired;
        }
        if (isSet(requestOptions.fields, 2048)) {
            this.transformations.putAll(requestOptions.transformations);
        }
        if (isSet(requestOptions.fields, 524288)) {
            this.onlyRetrieveFromCache = requestOptions.onlyRetrieveFromCache;
        }
        if (!this.isTransformationAllowed) {
            this.transformations.clear();
            this.fields &= -2049;
            this.isTransformationRequired = false;
            this.fields &= -131073;
        }
        this.fields |= requestOptions.fields;
        this.options.putAll(requestOptions.options);
        return selfOrThrowIfLocked();
    }

    public RequestOptions autoClone() {
        if (this.isLocked && !this.isAutoCloneEnabled) {
            throw new IllegalStateException("You cannot auto lock an already locked options object, try clone() first");
        }
        this.isAutoCloneEnabled = true;
        return lock();
    }

    public RequestOptions centerCrop() {
        return transform(DownsampleStrategy.CENTER_OUTSIDE, new CenterCrop());
    }

    public RequestOptions centerInside() {
        return transform(DownsampleStrategy.CENTER_INSIDE, new CenterInside());
    }

    public RequestOptions circleCrop() {
        return transform(DownsampleStrategy.CENTER_INSIDE, new CircleCrop());
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public RequestOptions m25clone() {
        try {
            RequestOptions requestOptions = (RequestOptions) super.clone();
            requestOptions.options = new Options();
            requestOptions.options.putAll(this.options);
            requestOptions.transformations = new HashMap();
            requestOptions.transformations.putAll(this.transformations);
            requestOptions.isLocked = false;
            requestOptions.isAutoCloneEnabled = false;
            return requestOptions;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public RequestOptions decode(@NonNull Class<?> cls) {
        if (this.isAutoCloneEnabled) {
            return m25clone().decode(cls);
        }
        this.resourceClass = (Class) Preconditions.checkNotNull(cls);
        this.fields |= 4096;
        return selfOrThrowIfLocked();
    }

    public RequestOptions diskCacheStrategy(@NonNull DiskCacheStrategy diskCacheStrategy) {
        if (this.isAutoCloneEnabled) {
            return m25clone().diskCacheStrategy(diskCacheStrategy);
        }
        this.diskCacheStrategy = (DiskCacheStrategy) Preconditions.checkNotNull(diskCacheStrategy);
        this.fields |= 4;
        return selfOrThrowIfLocked();
    }

    public RequestOptions dontAnimate() {
        if (this.isAutoCloneEnabled) {
            return m25clone().dontAnimate();
        }
        set(ByteBufferGifDecoder.DISABLE_ANIMATION, true);
        set(StreamGifDecoder.DISABLE_ANIMATION, true);
        return selfOrThrowIfLocked();
    }

    public RequestOptions dontTransform() {
        if (this.isAutoCloneEnabled) {
            return m25clone().dontTransform();
        }
        this.transformations.clear();
        this.fields &= -2049;
        this.isTransformationRequired = false;
        this.fields &= -131073;
        this.isTransformationAllowed = false;
        this.fields |= 65536;
        return selfOrThrowIfLocked();
    }

    public RequestOptions downsample(@NonNull DownsampleStrategy downsampleStrategy) {
        return set(Downsampler.DOWNSAMPLE_STRATEGY, Preconditions.checkNotNull(downsampleStrategy));
    }

    public RequestOptions encodeFormat(@NonNull Bitmap.CompressFormat compressFormat) {
        return set(BitmapEncoder.COMPRESSION_FORMAT, Preconditions.checkNotNull(compressFormat));
    }

    public RequestOptions encodeQuality(int i) {
        return set(BitmapEncoder.COMPRESSION_QUALITY, Integer.valueOf(i));
    }

    public RequestOptions error(int i) {
        if (this.isAutoCloneEnabled) {
            return m25clone().error(i);
        }
        this.errorId = i;
        this.fields |= 32;
        return selfOrThrowIfLocked();
    }

    public RequestOptions error(@Nullable Drawable drawable) {
        if (this.isAutoCloneEnabled) {
            return m25clone().error(drawable);
        }
        this.errorPlaceholder = drawable;
        this.fields |= 16;
        return selfOrThrowIfLocked();
    }

    public RequestOptions fallback(int i) {
        if (this.isAutoCloneEnabled) {
            return m25clone().fallback(i);
        }
        this.fallbackId = i;
        this.fields |= 16384;
        return selfOrThrowIfLocked();
    }

    public RequestOptions fallback(Drawable drawable) {
        if (this.isAutoCloneEnabled) {
            return m25clone().fallback(drawable);
        }
        this.fallbackDrawable = drawable;
        this.fields |= 8192;
        return selfOrThrowIfLocked();
    }

    public RequestOptions fitCenter() {
        return transform(DownsampleStrategy.FIT_CENTER, new FitCenter());
    }

    public RequestOptions format(@NonNull DecodeFormat decodeFormat) {
        return set(Downsampler.DECODE_FORMAT, Preconditions.checkNotNull(decodeFormat));
    }

    public RequestOptions frame(long j) {
        return set(VideoBitmapDecoder.TARGET_FRAME, Long.valueOf(j));
    }

    public final DiskCacheStrategy getDiskCacheStrategy() {
        return this.diskCacheStrategy;
    }

    public final int getErrorId() {
        return this.errorId;
    }

    public final Drawable getErrorPlaceholder() {
        return this.errorPlaceholder;
    }

    public final Drawable getFallbackDrawable() {
        return this.fallbackDrawable;
    }

    public final int getFallbackId() {
        return this.fallbackId;
    }

    public final boolean getOnlyRetrieveFromCache() {
        return this.onlyRetrieveFromCache;
    }

    public final Options getOptions() {
        return this.options;
    }

    public final int getOverrideHeight() {
        return this.overrideHeight;
    }

    public final int getOverrideWidth() {
        return this.overrideWidth;
    }

    public final Drawable getPlaceholderDrawable() {
        return this.placeholderDrawable;
    }

    public final int getPlaceholderId() {
        return this.placeholderId;
    }

    public final Priority getPriority() {
        return this.priority;
    }

    public final Class<?> getResourceClass() {
        return this.resourceClass;
    }

    public final Key getSignature() {
        return this.signature;
    }

    public final float getSizeMultiplier() {
        return this.sizeMultiplier;
    }

    public final Resources.Theme getTheme() {
        return this.theme;
    }

    public final Map<Class<?>, Transformation<?>> getTransformations() {
        return this.transformations;
    }

    public final boolean getUseUnlimitedSourceGeneratorsPool() {
        return this.useUnlimitedSourceGeneratorsPool;
    }

    public final boolean isLocked() {
        return this.isLocked;
    }

    public final boolean isMemoryCacheable() {
        return this.isCacheable;
    }

    public final boolean isPrioritySet() {
        return isSet(8);
    }

    public final boolean isTransformationAllowed() {
        return this.isTransformationAllowed;
    }

    public final boolean isTransformationRequired() {
        return this.isTransformationRequired;
    }

    public final boolean isTransformationSet() {
        return isSet(2048);
    }

    public final boolean isValidOverride() {
        return Util.isValidDimensions(this.overrideWidth, this.overrideHeight);
    }

    public RequestOptions lock() {
        this.isLocked = true;
        return this;
    }

    public RequestOptions onlyRetrieveFromCache(boolean z) {
        if (this.isAutoCloneEnabled) {
            return m25clone().onlyRetrieveFromCache(z);
        }
        this.onlyRetrieveFromCache = z;
        this.fields |= 524288;
        return selfOrThrowIfLocked();
    }

    public RequestOptions optionalCenterCrop() {
        return optionalTransform(DownsampleStrategy.CENTER_OUTSIDE, new CenterCrop());
    }

    public RequestOptions optionalCenterInside() {
        return optionalTransform(DownsampleStrategy.CENTER_INSIDE, new CenterInside());
    }

    public RequestOptions optionalCircleCrop() {
        return optionalTransform(DownsampleStrategy.CENTER_OUTSIDE, new CircleCrop());
    }

    public RequestOptions optionalFitCenter() {
        return optionalTransform(DownsampleStrategy.FIT_CENTER, new FitCenter());
    }

    public RequestOptions optionalTransform(Transformation<Bitmap> transformation) {
        if (this.isAutoCloneEnabled) {
            return m25clone().optionalTransform(transformation);
        }
        optionalTransform(Bitmap.class, transformation);
        optionalTransform(BitmapDrawable.class, new BitmapDrawableTransformation(transformation));
        optionalTransform(GifDrawable.class, new GifDrawableTransformation(transformation));
        return selfOrThrowIfLocked();
    }

    final RequestOptions optionalTransform(DownsampleStrategy downsampleStrategy, Transformation<Bitmap> transformation) {
        if (this.isAutoCloneEnabled) {
            return m25clone().optionalTransform(downsampleStrategy, transformation);
        }
        downsample(downsampleStrategy);
        return optionalTransform(transformation);
    }

    public <T> RequestOptions optionalTransform(Class<T> cls, Transformation<T> transformation) {
        if (this.isAutoCloneEnabled) {
            return m25clone().optionalTransform(cls, transformation);
        }
        Preconditions.checkNotNull(cls);
        Preconditions.checkNotNull(transformation);
        this.transformations.put(cls, transformation);
        this.fields |= 2048;
        this.isTransformationAllowed = true;
        this.fields |= 65536;
        return selfOrThrowIfLocked();
    }

    public RequestOptions override(int i) {
        return override(i, i);
    }

    public RequestOptions override(int i, int i2) {
        if (this.isAutoCloneEnabled) {
            return m25clone().override(i, i2);
        }
        this.overrideWidth = i;
        this.overrideHeight = i2;
        this.fields |= 512;
        return selfOrThrowIfLocked();
    }

    public RequestOptions placeholder(int i) {
        if (this.isAutoCloneEnabled) {
            return m25clone().placeholder(i);
        }
        this.placeholderId = i;
        this.fields |= 128;
        return selfOrThrowIfLocked();
    }

    public RequestOptions placeholder(@Nullable Drawable drawable) {
        if (this.isAutoCloneEnabled) {
            return m25clone().placeholder(drawable);
        }
        this.placeholderDrawable = drawable;
        this.fields |= 64;
        return selfOrThrowIfLocked();
    }

    public RequestOptions priority(@NonNull Priority priority) {
        if (this.isAutoCloneEnabled) {
            return m25clone().priority(priority);
        }
        this.priority = (Priority) Preconditions.checkNotNull(priority);
        this.fields |= 8;
        return selfOrThrowIfLocked();
    }

    public <T> RequestOptions set(@NonNull Option<T> option, @NonNull T t) {
        if (this.isAutoCloneEnabled) {
            return m25clone().set(option, t);
        }
        Preconditions.checkNotNull(option);
        Preconditions.checkNotNull(t);
        this.options.set(option, t);
        return selfOrThrowIfLocked();
    }

    public RequestOptions signature(@NonNull Key key) {
        if (this.isAutoCloneEnabled) {
            return m25clone().signature(key);
        }
        this.signature = (Key) Preconditions.checkNotNull(key);
        this.fields |= 1024;
        return selfOrThrowIfLocked();
    }

    public RequestOptions sizeMultiplier(float f) {
        if (this.isAutoCloneEnabled) {
            return m25clone().sizeMultiplier(f);
        }
        if (f < 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("sizeMultiplier must be between 0 and 1");
        }
        this.sizeMultiplier = f;
        this.fields |= 2;
        return selfOrThrowIfLocked();
    }

    public RequestOptions skipMemoryCache(boolean z) {
        if (this.isAutoCloneEnabled) {
            return m25clone().skipMemoryCache(true);
        }
        this.isCacheable = !z;
        this.fields |= 256;
        return selfOrThrowIfLocked();
    }

    public RequestOptions theme(Resources.Theme theme) {
        if (this.isAutoCloneEnabled) {
            return m25clone().theme(theme);
        }
        this.theme = theme;
        this.fields |= 32768;
        return selfOrThrowIfLocked();
    }

    public RequestOptions transform(@NonNull Transformation<Bitmap> transformation) {
        if (this.isAutoCloneEnabled) {
            return m25clone().transform(transformation);
        }
        optionalTransform(transformation);
        this.isTransformationRequired = true;
        this.fields |= 131072;
        return selfOrThrowIfLocked();
    }

    final RequestOptions transform(DownsampleStrategy downsampleStrategy, Transformation<Bitmap> transformation) {
        if (this.isAutoCloneEnabled) {
            return m25clone().transform(downsampleStrategy, transformation);
        }
        downsample(downsampleStrategy);
        return transform(transformation);
    }

    public <T> RequestOptions transform(Class<T> cls, Transformation<T> transformation) {
        if (this.isAutoCloneEnabled) {
            return m25clone().transform(cls, transformation);
        }
        optionalTransform(cls, transformation);
        this.isTransformationRequired = true;
        this.fields |= 131072;
        return selfOrThrowIfLocked();
    }

    public RequestOptions useUnlimitedSourceGeneratorsPool(boolean z) {
        if (this.isAutoCloneEnabled) {
            return m25clone().useUnlimitedSourceGeneratorsPool(z);
        }
        this.useUnlimitedSourceGeneratorsPool = z;
        this.fields |= 262144;
        return selfOrThrowIfLocked();
    }
}
