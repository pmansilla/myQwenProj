package com.nostra13.universalimageloader.core;

import android.graphics.Bitmap;
import android.os.Handler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.decode.ImageDecoder;
import com.nostra13.universalimageloader.core.decode.ImageDecodingInfo;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.utils.IoUtils;
import com.nostra13.universalimageloader.utils.L;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class LoadAndDisplayImageTask implements Runnable, IoUtils.CopyListener {
    private static final String ERROR_NO_IMAGE_STREAM = "No stream for image [%s]";
    private static final String ERROR_POST_PROCESSOR_NULL = "Post-processor returned null [%s]";
    private static final String ERROR_PRE_PROCESSOR_NULL = "Pre-processor returned null [%s]";
    private static final String ERROR_PROCESSOR_FOR_DISK_CACHE_NULL = "Bitmap processor for disk cache returned null [%s]";
    private static final String LOG_CACHE_IMAGE_IN_MEMORY = "Cache image in memory [%s]";
    private static final String LOG_CACHE_IMAGE_ON_DISK = "Cache image on disk [%s]";
    private static final String LOG_DELAY_BEFORE_LOADING = "Delay %d ms before loading...  [%s]";
    private static final String LOG_GET_IMAGE_FROM_MEMORY_CACHE_AFTER_WAITING = "...Get cached bitmap from memory after waiting. [%s]";
    private static final String LOG_LOAD_IMAGE_FROM_DISK_CACHE = "Load image from disk cache [%s]";
    private static final String LOG_LOAD_IMAGE_FROM_NETWORK = "Load image from network [%s]";
    private static final String LOG_POSTPROCESS_IMAGE = "PostProcess image before displaying [%s]";
    private static final String LOG_PREPROCESS_IMAGE = "PreProcess image before caching in memory [%s]";
    private static final String LOG_PROCESS_IMAGE_BEFORE_CACHE_ON_DISK = "Process image before cache on disk [%s]";
    private static final String LOG_RESIZE_CACHED_IMAGE_FILE = "Resize image in disk cache [%s]";
    private static final String LOG_RESUME_AFTER_PAUSE = ".. Resume loading [%s]";
    private static final String LOG_START_DISPLAY_IMAGE_TASK = "Start display image task [%s]";
    private static final String LOG_TASK_CANCELLED_IMAGEAWARE_COLLECTED = "ImageAware was collected by GC. Task is cancelled. [%s]";
    private static final String LOG_TASK_CANCELLED_IMAGEAWARE_REUSED = "ImageAware is reused for another image. Task is cancelled. [%s]";
    private static final String LOG_TASK_INTERRUPTED = "Task was interrupted [%s]";
    private static final String LOG_WAITING_FOR_IMAGE_LOADED = "Image already is loading. Waiting... [%s]";
    private static final String LOG_WAITING_FOR_RESUME = "ImageLoader is paused. Waiting...  [%s]";
    private final ImageLoaderConfiguration configuration;
    private final ImageDecoder decoder;
    private final ImageDownloader downloader;
    private final ImageLoaderEngine engine;
    private final Handler handler;
    final ImageAware imageAware;
    private final ImageLoadingInfo imageLoadingInfo;
    final ImageLoadingListener listener;
    private LoadedFrom loadedFrom = LoadedFrom.NETWORK;
    private final String memoryCacheKey;
    private final ImageDownloader networkDeniedDownloader;
    final DisplayImageOptions options;
    final ImageLoadingProgressListener progressListener;
    private final ImageDownloader slowNetworkDownloader;
    private final boolean syncLoading;
    private final ImageSize targetSize;
    final String uri;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class TaskCancelledException extends Exception {
        TaskCancelledException() {
        }
    }

    public LoadAndDisplayImageTask(ImageLoaderEngine imageLoaderEngine, ImageLoadingInfo imageLoadingInfo, Handler handler) {
        this.engine = imageLoaderEngine;
        this.imageLoadingInfo = imageLoadingInfo;
        this.handler = handler;
        this.configuration = imageLoaderEngine.configuration;
        this.downloader = this.configuration.downloader;
        this.networkDeniedDownloader = this.configuration.networkDeniedDownloader;
        this.slowNetworkDownloader = this.configuration.slowNetworkDownloader;
        this.decoder = this.configuration.decoder;
        this.uri = imageLoadingInfo.uri;
        this.memoryCacheKey = imageLoadingInfo.memoryCacheKey;
        this.imageAware = imageLoadingInfo.imageAware;
        this.targetSize = imageLoadingInfo.targetSize;
        this.options = imageLoadingInfo.options;
        this.listener = imageLoadingInfo.listener;
        this.progressListener = imageLoadingInfo.progressListener;
        this.syncLoading = this.options.isSyncLoading();
    }

    private void checkTaskInterrupted() throws TaskCancelledException {
        if (isTaskInterrupted()) {
            throw new TaskCancelledException();
        }
    }

    private void checkTaskNotActual() throws TaskCancelledException {
        checkViewCollected();
        checkViewReused();
    }

    private void checkViewCollected() throws TaskCancelledException {
        if (isViewCollected()) {
            throw new TaskCancelledException();
        }
    }

    private void checkViewReused() throws TaskCancelledException {
        if (isViewReused()) {
            throw new TaskCancelledException();
        }
    }

    private Bitmap decodeImage(String str) throws IOException {
        return this.decoder.decode(new ImageDecodingInfo(this.memoryCacheKey, str, this.uri, this.targetSize, this.imageAware.getScaleType(), getDownloader(), this.options));
    }

    private boolean delayIfNeed() {
        if (!this.options.shouldDelayBeforeLoading()) {
            return false;
        }
        L.d(LOG_DELAY_BEFORE_LOADING, Integer.valueOf(this.options.getDelayBeforeLoading()), this.memoryCacheKey);
        try {
            Thread.sleep(this.options.getDelayBeforeLoading());
            return isTaskNotActual();
        } catch (InterruptedException unused) {
            L.e(LOG_TASK_INTERRUPTED, this.memoryCacheKey);
            return true;
        }
    }

    private boolean downloadImage() throws IOException {
        InputStream stream = getDownloader().getStream(this.uri, this.options.getExtraForDownloader());
        if (stream == null) {
            L.e(ERROR_NO_IMAGE_STREAM, this.memoryCacheKey);
            return false;
        }
        try {
            return this.configuration.diskCache.save(this.uri, stream, this);
        } finally {
            IoUtils.closeSilently(stream);
        }
    }

    private void fireCancelEvent() {
        if (this.syncLoading || isTaskInterrupted()) {
            return;
        }
        runTask(new Runnable() { // from class: com.nostra13.universalimageloader.core.LoadAndDisplayImageTask.3
            @Override // java.lang.Runnable
            public void run() {
                LoadAndDisplayImageTask.this.listener.onLoadingCancelled(LoadAndDisplayImageTask.this.uri, LoadAndDisplayImageTask.this.imageAware.getWrappedView());
            }
        }, false, this.handler, this.engine);
    }

    private void fireFailEvent(final FailReason.FailType failType, final Throwable th) {
        if (this.syncLoading || isTaskInterrupted() || isTaskNotActual()) {
            return;
        }
        runTask(new Runnable() { // from class: com.nostra13.universalimageloader.core.LoadAndDisplayImageTask.2
            @Override // java.lang.Runnable
            public void run() {
                if (LoadAndDisplayImageTask.this.options.shouldShowImageOnFail()) {
                    LoadAndDisplayImageTask.this.imageAware.setImageDrawable(LoadAndDisplayImageTask.this.options.getImageOnFail(LoadAndDisplayImageTask.this.configuration.resources));
                }
                LoadAndDisplayImageTask.this.listener.onLoadingFailed(LoadAndDisplayImageTask.this.uri, LoadAndDisplayImageTask.this.imageAware.getWrappedView(), new FailReason(failType, th));
            }
        }, false, this.handler, this.engine);
    }

    private boolean fireProgressEvent(final int i, final int i2) {
        if (isTaskInterrupted() || isTaskNotActual()) {
            return false;
        }
        if (this.progressListener == null) {
            return true;
        }
        runTask(new Runnable() { // from class: com.nostra13.universalimageloader.core.LoadAndDisplayImageTask.1
            @Override // java.lang.Runnable
            public void run() {
                LoadAndDisplayImageTask.this.progressListener.onProgressUpdate(LoadAndDisplayImageTask.this.uri, LoadAndDisplayImageTask.this.imageAware.getWrappedView(), i, i2);
            }
        }, false, this.handler, this.engine);
        return true;
    }

    private ImageDownloader getDownloader() {
        return this.engine.isNetworkDenied() ? this.networkDeniedDownloader : this.engine.isSlowNetwork() ? this.slowNetworkDownloader : this.downloader;
    }

    private boolean isTaskInterrupted() {
        if (!Thread.interrupted()) {
            return false;
        }
        L.d(LOG_TASK_INTERRUPTED, this.memoryCacheKey);
        return true;
    }

    private boolean isTaskNotActual() {
        return isViewCollected() || isViewReused();
    }

    private boolean isViewCollected() {
        if (!this.imageAware.isCollected()) {
            return false;
        }
        L.d(LOG_TASK_CANCELLED_IMAGEAWARE_COLLECTED, this.memoryCacheKey);
        return true;
    }

    private boolean isViewReused() {
        if (!(!this.memoryCacheKey.equals(this.engine.getLoadingUriForView(this.imageAware)))) {
            return false;
        }
        L.d(LOG_TASK_CANCELLED_IMAGEAWARE_REUSED, this.memoryCacheKey);
        return true;
    }

    private boolean resizeAndSaveImage(int i, int i2) throws IOException {
        File file = this.configuration.diskCache.get(this.uri);
        if (file == null || !file.exists()) {
            return false;
        }
        Bitmap decode = this.decoder.decode(new ImageDecodingInfo(this.memoryCacheKey, ImageDownloader.Scheme.FILE.wrap(file.getAbsolutePath()), this.uri, new ImageSize(i, i2), ViewScaleType.FIT_INSIDE, getDownloader(), new DisplayImageOptions.Builder().cloneFrom(this.options).imageScaleType(ImageScaleType.IN_SAMPLE_INT).build()));
        if (decode != null && this.configuration.processorForDiskCache != null) {
            L.d(LOG_PROCESS_IMAGE_BEFORE_CACHE_ON_DISK, this.memoryCacheKey);
            decode = this.configuration.processorForDiskCache.process(decode);
            if (decode == null) {
                L.e(ERROR_PROCESSOR_FOR_DISK_CACHE_NULL, this.memoryCacheKey);
            }
        }
        if (decode == null) {
            return false;
        }
        boolean save = this.configuration.diskCache.save(this.uri, decode);
        decode.recycle();
        return save;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void runTask(Runnable runnable, boolean z, Handler handler, ImageLoaderEngine imageLoaderEngine) {
        if (z) {
            runnable.run();
        } else if (handler == null) {
            imageLoaderEngine.fireCallback(runnable);
        } else {
            handler.post(runnable);
        }
    }

    private boolean tryCacheImageOnDisk() throws TaskCancelledException {
        L.d(LOG_CACHE_IMAGE_ON_DISK, this.memoryCacheKey);
        try {
            boolean downloadImage = downloadImage();
            if (!downloadImage) {
                return downloadImage;
            }
            int i = this.configuration.maxImageWidthForDiskCache;
            int i2 = this.configuration.maxImageHeightForDiskCache;
            if (i <= 0 && i2 <= 0) {
                return downloadImage;
            }
            L.d(LOG_RESIZE_CACHED_IMAGE_FILE, this.memoryCacheKey);
            resizeAndSaveImage(i, i2);
            return downloadImage;
        } catch (IOException e) {
            L.e(e);
            return false;
        }
    }

    private Bitmap tryLoadBitmap() throws TaskCancelledException {
        Bitmap bitmap;
        Throwable th;
        OutOfMemoryError e;
        IOException e2;
        File file;
        try {
            try {
                File file2 = this.configuration.diskCache.get(this.uri);
                if (file2 == null || !file2.exists() || file2.length() <= 0) {
                    bitmap = null;
                } else {
                    L.d(LOG_LOAD_IMAGE_FROM_DISK_CACHE, this.memoryCacheKey);
                    this.loadedFrom = LoadedFrom.DISC_CACHE;
                    checkTaskNotActual();
                    bitmap = decodeImage(ImageDownloader.Scheme.FILE.wrap(file2.getAbsolutePath()));
                }
                if (bitmap != null) {
                    try {
                        if (bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
                            return bitmap;
                        }
                    } catch (IOException e3) {
                        e2 = e3;
                        L.e(e2);
                        fireFailEvent(FailReason.FailType.IO_ERROR, e2);
                        return bitmap;
                    } catch (IllegalStateException unused) {
                        fireFailEvent(FailReason.FailType.NETWORK_DENIED, null);
                        return bitmap;
                    } catch (OutOfMemoryError e4) {
                        e = e4;
                        L.e(e);
                        fireFailEvent(FailReason.FailType.OUT_OF_MEMORY, e);
                        return bitmap;
                    } catch (Throwable th2) {
                        th = th2;
                        L.e(th);
                        fireFailEvent(FailReason.FailType.UNKNOWN, th);
                        return bitmap;
                    }
                }
                L.d(LOG_LOAD_IMAGE_FROM_NETWORK, this.memoryCacheKey);
                this.loadedFrom = LoadedFrom.NETWORK;
                String str = this.uri;
                if (this.options.isCacheOnDisk() && tryCacheImageOnDisk() && (file = this.configuration.diskCache.get(this.uri)) != null) {
                    str = ImageDownloader.Scheme.FILE.wrap(file.getAbsolutePath());
                }
                checkTaskNotActual();
                Bitmap decodeImage = decodeImage(str);
                if (decodeImage != null) {
                    try {
                        if (decodeImage.getWidth() > 0) {
                            if (decodeImage.getHeight() <= 0) {
                            }
                            return decodeImage;
                        }
                    } catch (IOException e5) {
                        e2 = e5;
                        bitmap = decodeImage;
                        L.e(e2);
                        fireFailEvent(FailReason.FailType.IO_ERROR, e2);
                        return bitmap;
                    } catch (IllegalStateException unused2) {
                        bitmap = decodeImage;
                        fireFailEvent(FailReason.FailType.NETWORK_DENIED, null);
                        return bitmap;
                    } catch (OutOfMemoryError e6) {
                        e = e6;
                        bitmap = decodeImage;
                        L.e(e);
                        fireFailEvent(FailReason.FailType.OUT_OF_MEMORY, e);
                        return bitmap;
                    } catch (Throwable th3) {
                        th = th3;
                        bitmap = decodeImage;
                        L.e(th);
                        fireFailEvent(FailReason.FailType.UNKNOWN, th);
                        return bitmap;
                    }
                }
                fireFailEvent(FailReason.FailType.DECODING_ERROR, null);
                return decodeImage;
            } catch (TaskCancelledException e7) {
                throw e7;
            }
        } catch (IOException e8) {
            bitmap = null;
            e2 = e8;
        } catch (IllegalStateException unused3) {
            bitmap = null;
        } catch (OutOfMemoryError e9) {
            bitmap = null;
            e = e9;
        } catch (Throwable th4) {
            bitmap = null;
            th = th4;
        }
    }

    private boolean waitIfPaused() {
        AtomicBoolean pause = this.engine.getPause();
        if (pause.get()) {
            synchronized (this.engine.getPauseLock()) {
                if (pause.get()) {
                    L.d(LOG_WAITING_FOR_RESUME, this.memoryCacheKey);
                    try {
                        this.engine.getPauseLock().wait();
                        L.d(LOG_RESUME_AFTER_PAUSE, this.memoryCacheKey);
                    } catch (InterruptedException unused) {
                        L.e(LOG_TASK_INTERRUPTED, this.memoryCacheKey);
                        return true;
                    }
                }
            }
        }
        return isTaskNotActual();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getLoadingUri() {
        return this.uri;
    }

    @Override // com.nostra13.universalimageloader.utils.IoUtils.CopyListener
    public boolean onBytesCopied(int i, int i2) {
        return this.syncLoading || fireProgressEvent(i, i2);
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x00d2 A[Catch: all -> 0x00fb, TaskCancelledException -> 0x00fd, Merged into TryCatch #1 {all -> 0x00fb, TaskCancelledException -> 0x00fd, blocks: (B:13:0x0033, B:15:0x0042, B:18:0x0049, B:20:0x00b3, B:22:0x00bb, B:24:0x00d2, B:25:0x00dd, B:29:0x0059, B:33:0x0063, B:35:0x0071, B:37:0x0088, B:39:0x0095, B:41:0x009d, B:42:0x00fd), top: B:12:0x0033 }] */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void run() {
        /*
            Method dump skipped, instructions count: 264
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nostra13.universalimageloader.core.LoadAndDisplayImageTask.run():void");
    }
}
