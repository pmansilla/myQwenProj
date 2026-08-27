package me.panpf.sketch;

import android.annotation.TargetApi;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import me.panpf.sketch.cache.BitmapPool;
import me.panpf.sketch.cache.DiskCache;
import me.panpf.sketch.cache.LruBitmapPool;
import me.panpf.sketch.cache.LruDiskCache;
import me.panpf.sketch.cache.LruMemoryCache;
import me.panpf.sketch.cache.MemoryCache;
import me.panpf.sketch.cache.MemorySizeCalculator;
import me.panpf.sketch.decode.ImageDecoder;
import me.panpf.sketch.decode.ImageOrientationCorrector;
import me.panpf.sketch.decode.ImageSizeCalculator;
import me.panpf.sketch.decode.ProcessedImageCache;
import me.panpf.sketch.decode.ResizeCalculator;
import me.panpf.sketch.display.DefaultImageDisplayer;
import me.panpf.sketch.display.ImageDisplayer;
import me.panpf.sketch.http.HttpStack;
import me.panpf.sketch.http.HurlStack;
import me.panpf.sketch.http.ImageDownloader;
import me.panpf.sketch.optionsfilter.OptionsFilterManager;
import me.panpf.sketch.process.ImageProcessor;
import me.panpf.sketch.process.ResizeImageProcessor;
import me.panpf.sketch.request.FreeRideManager;
import me.panpf.sketch.request.HelperFactory;
import me.panpf.sketch.request.RequestExecutor;
import me.panpf.sketch.request.RequestFactory;
import me.panpf.sketch.uri.UriModelManager;

/* loaded from: classes2.dex */
public final class Configuration {
    private static final String NAME = "Configuration";
    private BitmapPool bitmapPool;
    private Context context;
    private ImageDecoder decoder;
    private ImageDisplayer defaultDisplayer;
    private DiskCache diskCache;
    private ImageDownloader downloader;
    private ErrorTracker errorTracker;
    private RequestExecutor executor;
    private FreeRideManager freeRideManager;
    private HelperFactory helperFactory;
    private HttpStack httpStack;
    private MemoryCache memoryCache;
    private OptionsFilterManager optionsFilterManager;
    private ImageOrientationCorrector orientationCorrector;
    private ProcessedImageCache processedImageCache;
    private RequestFactory requestFactory;
    private ResizeCalculator resizeCalculator;
    private ImageProcessor resizeProcessor;
    private ImageSizeCalculator sizeCalculator;
    private UriModelManager uriModelManager;

    @TargetApi(14)
    /* loaded from: classes2.dex */
    private static class MemoryChangedListener implements ComponentCallbacks2 {
        private Context context;

        public MemoryChangedListener(Context context) {
            this.context = context.getApplicationContext();
        }

        @Override // android.content.ComponentCallbacks
        public void onConfigurationChanged(android.content.res.Configuration configuration) {
        }

        @Override // android.content.ComponentCallbacks
        public void onLowMemory() {
            Sketch.with(this.context).onLowMemory();
        }

        @Override // android.content.ComponentCallbacks2
        public void onTrimMemory(int i) {
            Sketch.with(this.context).onTrimMemory(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Configuration(@NonNull Context context) {
        Context applicationContext = context.getApplicationContext();
        this.context = applicationContext;
        this.uriModelManager = new UriModelManager();
        this.optionsFilterManager = new OptionsFilterManager();
        this.diskCache = new LruDiskCache(applicationContext, this, 2, DiskCache.DISK_CACHE_MAX_SIZE);
        MemorySizeCalculator memorySizeCalculator = new MemorySizeCalculator(applicationContext);
        this.bitmapPool = new LruBitmapPool(applicationContext, memorySizeCalculator.getBitmapPoolSize());
        this.memoryCache = new LruMemoryCache(applicationContext, memorySizeCalculator.getMemoryCacheSize());
        this.decoder = new ImageDecoder();
        this.executor = new RequestExecutor();
        this.httpStack = new HurlStack();
        this.downloader = new ImageDownloader();
        this.sizeCalculator = new ImageSizeCalculator();
        this.freeRideManager = new FreeRideManager();
        this.resizeProcessor = new ResizeImageProcessor();
        this.resizeCalculator = new ResizeCalculator();
        this.defaultDisplayer = new DefaultImageDisplayer();
        this.processedImageCache = new ProcessedImageCache();
        this.orientationCorrector = new ImageOrientationCorrector();
        this.helperFactory = new HelperFactory();
        this.requestFactory = new RequestFactory();
        this.errorTracker = new ErrorTracker(applicationContext);
        if (Build.VERSION.SDK_INT >= 14) {
            applicationContext.getApplicationContext().registerComponentCallbacks(new MemoryChangedListener(applicationContext));
        }
    }

    @NonNull
    public BitmapPool getBitmapPool() {
        return this.bitmapPool;
    }

    @NonNull
    public Context getContext() {
        return this.context;
    }

    @NonNull
    public ImageDecoder getDecoder() {
        return this.decoder;
    }

    @NonNull
    public ImageDisplayer getDefaultDisplayer() {
        return this.defaultDisplayer;
    }

    @NonNull
    public DiskCache getDiskCache() {
        return this.diskCache;
    }

    @NonNull
    public ImageDownloader getDownloader() {
        return this.downloader;
    }

    @NonNull
    public ErrorTracker getErrorTracker() {
        return this.errorTracker;
    }

    @NonNull
    public RequestExecutor getExecutor() {
        return this.executor;
    }

    @NonNull
    public FreeRideManager getFreeRideManager() {
        return this.freeRideManager;
    }

    @NonNull
    public HelperFactory getHelperFactory() {
        return this.helperFactory;
    }

    @NonNull
    public HttpStack getHttpStack() {
        return this.httpStack;
    }

    @NonNull
    public MemoryCache getMemoryCache() {
        return this.memoryCache;
    }

    public OptionsFilterManager getOptionsFilterManager() {
        return this.optionsFilterManager;
    }

    @NonNull
    public ImageOrientationCorrector getOrientationCorrector() {
        return this.orientationCorrector;
    }

    @NonNull
    public ProcessedImageCache getProcessedImageCache() {
        return this.processedImageCache;
    }

    @NonNull
    public RequestFactory getRequestFactory() {
        return this.requestFactory;
    }

    @NonNull
    public ResizeCalculator getResizeCalculator() {
        return this.resizeCalculator;
    }

    @NonNull
    public ImageProcessor getResizeProcessor() {
        return this.resizeProcessor;
    }

    @NonNull
    public ImageSizeCalculator getSizeCalculator() {
        return this.sizeCalculator;
    }

    @NonNull
    public UriModelManager getUriModelManager() {
        return this.uriModelManager;
    }

    public boolean isInPreferQualityOverSpeedEnabled() {
        return this.optionsFilterManager.isInPreferQualityOverSpeedEnabled();
    }

    public boolean isLowQualityImageEnabled() {
        return this.optionsFilterManager.isLowQualityImageEnabled();
    }

    public boolean isMobileDataPauseDownloadEnabled() {
        return this.optionsFilterManager.isMobileDataPauseDownloadEnabled();
    }

    public boolean isPauseDownloadEnabled() {
        return this.optionsFilterManager.isPauseDownloadEnabled();
    }

    public boolean isPauseLoadEnabled() {
        return this.optionsFilterManager.isPauseLoadEnabled();
    }

    @NonNull
    public Configuration setBitmapPool(@NonNull BitmapPool bitmapPool) {
        if (bitmapPool != null) {
            BitmapPool bitmapPool2 = this.bitmapPool;
            this.bitmapPool = bitmapPool;
            if (bitmapPool2 != null) {
                bitmapPool2.close();
            }
            SLog.w(NAME, "bitmapPool=%s", this.bitmapPool.toString());
        }
        return this;
    }

    @NonNull
    public Configuration setDecoder(@NonNull ImageDecoder imageDecoder) {
        if (imageDecoder != null) {
            this.decoder = imageDecoder;
            SLog.w(NAME, "decoder=%s", imageDecoder.toString());
        }
        return this;
    }

    @NonNull
    public Configuration setDefaultDisplayer(@NonNull ImageDisplayer imageDisplayer) {
        if (imageDisplayer != null) {
            this.defaultDisplayer = imageDisplayer;
            SLog.w(NAME, "defaultDisplayer=%s", imageDisplayer.toString());
        }
        return this;
    }

    @NonNull
    public Configuration setDiskCache(@NonNull DiskCache diskCache) {
        if (diskCache != null) {
            DiskCache diskCache2 = this.diskCache;
            this.diskCache = diskCache;
            if (diskCache2 != null) {
                diskCache2.close();
            }
            SLog.w(NAME, "diskCache=%s", this.diskCache.toString());
        }
        return this;
    }

    @NonNull
    public Configuration setDownloader(@NonNull ImageDownloader imageDownloader) {
        if (imageDownloader != null) {
            this.downloader = imageDownloader;
            SLog.w(NAME, "downloader=%s", imageDownloader.toString());
        }
        return this;
    }

    @NonNull
    public Configuration setErrorTracker(@NonNull ErrorTracker errorTracker) {
        if (errorTracker != null) {
            this.errorTracker = errorTracker;
            SLog.w(NAME, "errorTracker=%s", errorTracker.toString());
        }
        return this;
    }

    @NonNull
    public Configuration setExecutor(@NonNull RequestExecutor requestExecutor) {
        if (requestExecutor != null) {
            RequestExecutor requestExecutor2 = this.executor;
            this.executor = requestExecutor;
            if (requestExecutor2 != null) {
                requestExecutor2.shutdown();
            }
            SLog.w(NAME, "executor=%s", this.executor.toString());
        }
        return this;
    }

    @NonNull
    public Configuration setFreeRideManager(@NonNull FreeRideManager freeRideManager) {
        if (freeRideManager != null) {
            this.freeRideManager = freeRideManager;
            SLog.w(NAME, "freeRideManager=%s", freeRideManager.toString());
        }
        return this;
    }

    @NonNull
    public Configuration setHelperFactory(@NonNull HelperFactory helperFactory) {
        if (helperFactory != null) {
            this.helperFactory = helperFactory;
            SLog.w(NAME, "helperFactory=%s", helperFactory.toString());
        }
        return this;
    }

    @NonNull
    public Configuration setHttpStack(@NonNull HttpStack httpStack) {
        if (httpStack != null) {
            this.httpStack = httpStack;
            SLog.w(NAME, "httpStack=", httpStack.toString());
        }
        return this;
    }

    @NonNull
    public Configuration setInPreferQualityOverSpeedEnabled(boolean z) {
        if (this.optionsFilterManager.isInPreferQualityOverSpeedEnabled() != z) {
            this.optionsFilterManager.setInPreferQualityOverSpeedEnabled(z);
            SLog.w(NAME, "inPreferQualityOverSpeed=%s", Boolean.valueOf(z));
        }
        return this;
    }

    @NonNull
    public Configuration setLowQualityImageEnabled(boolean z) {
        if (this.optionsFilterManager.isLowQualityImageEnabled() != z) {
            this.optionsFilterManager.setLowQualityImageEnabled(z);
            SLog.w(NAME, "lowQualityImage=%s", Boolean.valueOf(z));
        }
        return this;
    }

    @NonNull
    public Configuration setMemoryCache(@NonNull MemoryCache memoryCache) {
        if (memoryCache != null) {
            MemoryCache memoryCache2 = this.memoryCache;
            this.memoryCache = memoryCache;
            if (memoryCache2 != null) {
                memoryCache2.close();
            }
            SLog.w(NAME, "memoryCache=", memoryCache.toString());
        }
        return this;
    }

    @NonNull
    public Configuration setMobileDataPauseDownloadEnabled(boolean z) {
        if (isMobileDataPauseDownloadEnabled() != z) {
            this.optionsFilterManager.setMobileDataPauseDownloadEnabled(this, z);
            SLog.w(NAME, "mobileDataPauseDownload=%s", Boolean.valueOf(isMobileDataPauseDownloadEnabled()));
        }
        return this;
    }

    @NonNull
    public Configuration setOrientationCorrector(@NonNull ImageOrientationCorrector imageOrientationCorrector) {
        if (imageOrientationCorrector != null) {
            this.orientationCorrector = imageOrientationCorrector;
            SLog.w(NAME, "orientationCorrector=%s", imageOrientationCorrector.toString());
        }
        return this;
    }

    @NonNull
    public Configuration setPauseDownloadEnabled(boolean z) {
        if (this.optionsFilterManager.isPauseDownloadEnabled() != z) {
            this.optionsFilterManager.setPauseDownloadEnabled(z);
            SLog.w(NAME, "pauseDownload=%s", Boolean.valueOf(z));
        }
        return this;
    }

    @NonNull
    public Configuration setPauseLoadEnabled(boolean z) {
        if (this.optionsFilterManager.isPauseLoadEnabled() != z) {
            this.optionsFilterManager.setPauseLoadEnabled(z);
            SLog.w(NAME, "pauseLoad=%s", Boolean.valueOf(z));
        }
        return this;
    }

    @NonNull
    public Configuration setProcessedImageCache(@NonNull ProcessedImageCache processedImageCache) {
        if (processedImageCache != null) {
            this.processedImageCache = processedImageCache;
            SLog.w(NAME, "processedCache=", processedImageCache.toString());
        }
        return this;
    }

    @NonNull
    public Configuration setRequestFactory(@NonNull RequestFactory requestFactory) {
        if (requestFactory != null) {
            this.requestFactory = requestFactory;
            SLog.w(NAME, "requestFactory=%s", requestFactory.toString());
        }
        return this;
    }

    @NonNull
    public Configuration setResizeCalculator(@NonNull ResizeCalculator resizeCalculator) {
        if (resizeCalculator != null) {
            this.resizeCalculator = resizeCalculator;
            SLog.w(NAME, "resizeCalculator=%s", resizeCalculator.toString());
        }
        return this;
    }

    @NonNull
    public Configuration setResizeProcessor(@NonNull ImageProcessor imageProcessor) {
        if (imageProcessor != null) {
            this.resizeProcessor = imageProcessor;
            SLog.w(NAME, "resizeProcessor=%s", imageProcessor.toString());
        }
        return this;
    }

    @NonNull
    public Configuration setSizeCalculator(@NonNull ImageSizeCalculator imageSizeCalculator) {
        if (imageSizeCalculator != null) {
            this.sizeCalculator = imageSizeCalculator;
            SLog.w(NAME, "sizeCalculator=%s", imageSizeCalculator.toString());
        }
        return this;
    }

    @NonNull
    public String toString() {
        return "Configuration: \nuriModelManager：" + this.uriModelManager.toString() + "\noptionsFilterManager：" + this.optionsFilterManager.toString() + "\ndiskCache：" + this.diskCache.toString() + "\nbitmapPool：" + this.bitmapPool.toString() + "\nmemoryCache：" + this.memoryCache.toString() + "\nprocessedImageCache：" + this.processedImageCache.toString() + "\nhttpStack：" + this.httpStack.toString() + "\ndecoder：" + this.decoder.toString() + "\ndownloader：" + this.downloader.toString() + "\norientationCorrector：" + this.orientationCorrector.toString() + "\ndefaultDisplayer：" + this.defaultDisplayer.toString() + "\nresizeProcessor：" + this.resizeProcessor.toString() + "\nresizeCalculator：" + this.resizeCalculator.toString() + "\nsizeCalculator：" + this.sizeCalculator.toString() + "\nfreeRideManager：" + this.freeRideManager.toString() + "\nexecutor：" + this.executor.toString() + "\nhelperFactory：" + this.helperFactory.toString() + "\nrequestFactory：" + this.requestFactory.toString() + "\nerrorTracker：" + this.errorTracker.toString() + "\npauseDownload：" + this.optionsFilterManager.isPauseDownloadEnabled() + "\npauseLoad：" + this.optionsFilterManager.isPauseLoadEnabled() + "\nlowQualityImage：" + this.optionsFilterManager.isLowQualityImageEnabled() + "\ninPreferQualityOverSpeed：" + this.optionsFilterManager.isInPreferQualityOverSpeedEnabled() + "\nmobileDataPauseDownload：" + isMobileDataPauseDownloadEnabled();
    }
}
