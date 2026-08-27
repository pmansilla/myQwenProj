package com.bumptech.glide;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestCoordinator;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.SingleRequest;
import com.bumptech.glide.request.ThumbnailRequestCoordinator;
import com.bumptech.glide.request.target.PreloadTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ApplicationVersionSignature;
import com.bumptech.glide.signature.ObjectKey;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.io.File;
import java.net.URL;
import java.util.UUID;

/* loaded from: classes.dex */
public class RequestBuilder<TranscodeType> implements Cloneable {
    private static final TransitionOptions<?, ?> DEFAULT_ANIMATION_OPTIONS = new GenericTransitionOptions();
    protected static final RequestOptions DOWNLOAD_ONLY_OPTIONS = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA).priority(Priority.LOW).skipMemoryCache(true);
    private final GlideContext context;
    private final RequestOptions defaultRequestOptions;
    private final Glide glide;
    private boolean isModelSet;
    private boolean isThumbnailBuilt;

    @Nullable
    private Object model;

    @Nullable
    private RequestListener<TranscodeType> requestListener;
    private final RequestManager requestManager;

    @NonNull
    protected RequestOptions requestOptions;

    @Nullable
    private Float thumbSizeMultiplier;

    @Nullable
    private RequestBuilder<TranscodeType> thumbnailBuilder;
    private final Class<TranscodeType> transcodeClass;
    private TransitionOptions<?, ? super TranscodeType> transitionOptions;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.bumptech.glide.RequestBuilder$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$android$widget$ImageView$ScaleType;

        static {
            try {
                $SwitchMap$com$bumptech$glide$Priority[Priority.LOW.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$bumptech$glide$Priority[Priority.NORMAL.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$bumptech$glide$Priority[Priority.HIGH.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$bumptech$glide$Priority[Priority.IMMEDIATE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            $SwitchMap$android$widget$ImageView$ScaleType = new int[ImageView.ScaleType.values().length];
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.CENTER_CROP.ordinal()] = 1;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.CENTER_INSIDE.ordinal()] = 2;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_CENTER.ordinal()] = 3;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_START.ordinal()] = 4;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_END.ordinal()] = 5;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_XY.ordinal()] = 6;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.CENTER.ordinal()] = 7;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.MATRIX.ordinal()] = 8;
            } catch (NoSuchFieldError unused12) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public RequestBuilder(Glide glide, RequestManager requestManager, Class<TranscodeType> cls) {
        this.transitionOptions = (TransitionOptions<?, ? super TranscodeType>) DEFAULT_ANIMATION_OPTIONS;
        this.glide = glide;
        this.requestManager = requestManager;
        this.context = glide.getGlideContext();
        this.transcodeClass = cls;
        this.defaultRequestOptions = requestManager.getDefaultRequestOptions();
        this.requestOptions = this.defaultRequestOptions;
    }

    protected RequestBuilder(Class<TranscodeType> cls, RequestBuilder<?> requestBuilder) {
        this(requestBuilder.glide, requestBuilder.requestManager, cls);
        this.model = requestBuilder.model;
        this.isModelSet = requestBuilder.isModelSet;
        this.requestOptions = requestBuilder.requestOptions;
    }

    private Request buildRequest(Target<TranscodeType> target) {
        return buildRequestRecursive(target, null, this.transitionOptions, this.requestOptions.getPriority(), this.requestOptions.getOverrideWidth(), this.requestOptions.getOverrideHeight());
    }

    private Request buildRequestRecursive(Target<TranscodeType> target, @Nullable ThumbnailRequestCoordinator thumbnailRequestCoordinator, TransitionOptions<?, ? super TranscodeType> transitionOptions, Priority priority, int i, int i2) {
        if (this.thumbnailBuilder == null) {
            if (this.thumbSizeMultiplier == null) {
                return obtainRequest(target, this.requestOptions, thumbnailRequestCoordinator, transitionOptions, priority, i, i2);
            }
            ThumbnailRequestCoordinator thumbnailRequestCoordinator2 = new ThumbnailRequestCoordinator(thumbnailRequestCoordinator);
            thumbnailRequestCoordinator2.setRequests(obtainRequest(target, this.requestOptions, thumbnailRequestCoordinator2, transitionOptions, priority, i, i2), obtainRequest(target, this.requestOptions.m25clone().sizeMultiplier(this.thumbSizeMultiplier.floatValue()), thumbnailRequestCoordinator2, transitionOptions, getThumbnailPriority(priority), i, i2));
            return thumbnailRequestCoordinator2;
        }
        if (this.isThumbnailBuilt) {
            throw new IllegalStateException("You cannot use a request as both the main request and a thumbnail, consider using clone() on the request(s) passed to thumbnail()");
        }
        TransitionOptions<?, ? super TranscodeType> transitionOptions2 = this.thumbnailBuilder.transitionOptions;
        TransitionOptions<?, ? super TranscodeType> transitionOptions3 = DEFAULT_ANIMATION_OPTIONS.equals(transitionOptions2) ? transitionOptions : transitionOptions2;
        Priority priority2 = this.thumbnailBuilder.requestOptions.isPrioritySet() ? this.thumbnailBuilder.requestOptions.getPriority() : getThumbnailPriority(priority);
        int overrideWidth = this.thumbnailBuilder.requestOptions.getOverrideWidth();
        int overrideHeight = this.thumbnailBuilder.requestOptions.getOverrideHeight();
        if (Util.isValidDimensions(i, i2) && !this.thumbnailBuilder.requestOptions.isValidOverride()) {
            overrideWidth = this.requestOptions.getOverrideWidth();
            overrideHeight = this.requestOptions.getOverrideHeight();
        }
        ThumbnailRequestCoordinator thumbnailRequestCoordinator3 = new ThumbnailRequestCoordinator(thumbnailRequestCoordinator);
        Request obtainRequest = obtainRequest(target, this.requestOptions, thumbnailRequestCoordinator3, transitionOptions, priority, i, i2);
        this.isThumbnailBuilt = true;
        Request buildRequestRecursive = this.thumbnailBuilder.buildRequestRecursive(target, thumbnailRequestCoordinator3, transitionOptions3, priority2, overrideWidth, overrideHeight);
        this.isThumbnailBuilt = false;
        thumbnailRequestCoordinator3.setRequests(obtainRequest, buildRequestRecursive);
        return thumbnailRequestCoordinator3;
    }

    private Priority getThumbnailPriority(Priority priority) {
        switch (priority) {
            case LOW:
                return Priority.NORMAL;
            case NORMAL:
                return Priority.HIGH;
            case HIGH:
            case IMMEDIATE:
                return Priority.IMMEDIATE;
            default:
                throw new IllegalArgumentException("unknown priority: " + this.requestOptions.getPriority());
        }
    }

    private RequestBuilder<TranscodeType> loadGeneric(@Nullable Object obj) {
        this.model = obj;
        this.isModelSet = true;
        return this;
    }

    private Request obtainRequest(Target<TranscodeType> target, RequestOptions requestOptions, RequestCoordinator requestCoordinator, TransitionOptions<?, ? super TranscodeType> transitionOptions, Priority priority, int i, int i2) {
        requestOptions.lock();
        return SingleRequest.obtain(this.context, this.model, this.transcodeClass, requestOptions, i, i2, priority, target, this.requestListener, requestCoordinator, this.context.getEngine(), transitionOptions.getTransitionFactory());
    }

    public RequestBuilder<TranscodeType> apply(@NonNull RequestOptions requestOptions) {
        Preconditions.checkNotNull(requestOptions);
        this.requestOptions = getMutableOptions().apply(requestOptions);
        return this;
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public RequestBuilder<TranscodeType> m23clone() {
        try {
            RequestBuilder<TranscodeType> requestBuilder = (RequestBuilder) super.clone();
            requestBuilder.requestOptions = requestBuilder.requestOptions.m25clone();
            requestBuilder.transitionOptions = (TransitionOptions<?, ? super TranscodeType>) requestBuilder.transitionOptions.m24clone();
            return requestBuilder;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    public FutureTarget<File> downloadOnly(int i, int i2) {
        return getDownloadOnlyRequest().submit(i, i2);
    }

    @Deprecated
    public <Y extends Target<File>> Y downloadOnly(Y y) {
        return (Y) getDownloadOnlyRequest().into((RequestBuilder<File>) y);
    }

    protected RequestBuilder<File> getDownloadOnlyRequest() {
        return new RequestBuilder(File.class, this).apply(DOWNLOAD_ONLY_OPTIONS);
    }

    protected RequestOptions getMutableOptions() {
        return this.defaultRequestOptions == this.requestOptions ? this.requestOptions.m25clone() : this.requestOptions;
    }

    @Deprecated
    public FutureTarget<TranscodeType> into(int i, int i2) {
        return submit(i, i2);
    }

    public Target<TranscodeType> into(ImageView imageView) {
        Util.assertMainThread();
        Preconditions.checkNotNull(imageView);
        if (!this.requestOptions.isTransformationSet() && this.requestOptions.isTransformationAllowed() && imageView.getScaleType() != null) {
            if (this.requestOptions.isLocked()) {
                this.requestOptions = this.requestOptions.m25clone();
            }
            switch (AnonymousClass2.$SwitchMap$android$widget$ImageView$ScaleType[imageView.getScaleType().ordinal()]) {
                case 1:
                    this.requestOptions.optionalCenterCrop();
                    break;
                case 2:
                    this.requestOptions.optionalCenterInside();
                    break;
                case 3:
                case 4:
                case 5:
                    this.requestOptions.optionalFitCenter();
                    break;
                case 6:
                    this.requestOptions.optionalCenterInside();
                    break;
            }
        }
        return into((RequestBuilder<TranscodeType>) this.context.buildImageViewTarget(imageView, this.transcodeClass));
    }

    public <Y extends Target<TranscodeType>> Y into(@NonNull Y y) {
        Util.assertMainThread();
        Preconditions.checkNotNull(y);
        if (!this.isModelSet) {
            throw new IllegalArgumentException("You must call #load() before calling #into()");
        }
        if (y.getRequest() != null) {
            this.requestManager.clear((Target<?>) y);
        }
        this.requestOptions.lock();
        Request buildRequest = buildRequest(y);
        y.setRequest(buildRequest);
        this.requestManager.track(y, buildRequest);
        return y;
    }

    public RequestBuilder<TranscodeType> listener(@Nullable RequestListener<TranscodeType> requestListener) {
        this.requestListener = requestListener;
        return this;
    }

    public RequestBuilder<TranscodeType> load(@Nullable Uri uri) {
        return loadGeneric(uri);
    }

    public RequestBuilder<TranscodeType> load(@Nullable File file) {
        return loadGeneric(file);
    }

    public RequestBuilder<TranscodeType> load(@Nullable Integer num) {
        return loadGeneric(num).apply(RequestOptions.signatureOf(ApplicationVersionSignature.obtain(this.context)));
    }

    public RequestBuilder<TranscodeType> load(@Nullable Object obj) {
        return loadGeneric(obj);
    }

    public RequestBuilder<TranscodeType> load(@Nullable String str) {
        return loadGeneric(str);
    }

    @Deprecated
    public RequestBuilder<TranscodeType> load(@Nullable URL url) {
        return loadGeneric(url);
    }

    public RequestBuilder<TranscodeType> load(@Nullable byte[] bArr) {
        return loadGeneric(bArr).apply(RequestOptions.signatureOf(new ObjectKey(UUID.randomUUID().toString())).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true));
    }

    public Target<TranscodeType> preload() {
        return preload(Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public Target<TranscodeType> preload(int i, int i2) {
        return into((RequestBuilder<TranscodeType>) PreloadTarget.obtain(this.requestManager, i, i2));
    }

    public FutureTarget<TranscodeType> submit() {
        return submit(Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public FutureTarget<TranscodeType> submit(int i, int i2) {
        final RequestFutureTarget requestFutureTarget = new RequestFutureTarget(this.context.getMainHandler(), i, i2);
        if (Util.isOnBackgroundThread()) {
            this.context.getMainHandler().post(new Runnable() { // from class: com.bumptech.glide.RequestBuilder.1
                @Override // java.lang.Runnable
                public void run() {
                    if (requestFutureTarget.isCancelled()) {
                        return;
                    }
                    RequestBuilder.this.into((RequestBuilder) requestFutureTarget);
                }
            });
        } else {
            into((RequestBuilder<TranscodeType>) requestFutureTarget);
        }
        return requestFutureTarget;
    }

    public RequestBuilder<TranscodeType> thumbnail(float f) {
        if (f < 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("sizeMultiplier must be between 0 and 1");
        }
        this.thumbSizeMultiplier = Float.valueOf(f);
        return this;
    }

    public RequestBuilder<TranscodeType> thumbnail(@Nullable RequestBuilder<TranscodeType> requestBuilder) {
        this.thumbnailBuilder = requestBuilder;
        return this;
    }

    public RequestBuilder<TranscodeType> transition(@NonNull TransitionOptions<?, ? super TranscodeType> transitionOptions) {
        this.transitionOptions = (TransitionOptions) Preconditions.checkNotNull(transitionOptions);
        return this;
    }
}
