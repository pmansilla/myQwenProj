package me.panpf.sketch.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import me.panpf.sketch.SLog;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.cache.DiskCache;
import me.panpf.sketch.uri.UriModel;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class DownloadHelper {
    private static final String NAME = "DownloadHelper";
    private DownloadListener downloadListener;
    private DownloadOptions downloadOptions = new DownloadOptions();
    private DownloadProgressListener downloadProgressListener;
    private String key;
    private Sketch sketch;
    private boolean sync;
    private String uri;
    private UriModel uriModel;

    public DownloadHelper(@NonNull Sketch sketch, @NonNull String str, @Nullable DownloadListener downloadListener) {
        this.sketch = sketch;
        this.uri = str;
        this.downloadListener = downloadListener;
        this.uriModel = UriModel.match(sketch, str);
    }

    private boolean checkDiskCache() {
        DiskCache.Entry entry;
        if (this.downloadOptions.isCacheInDiskDisabled() || (entry = this.sketch.getConfiguration().getDiskCache().get(this.uriModel.getDiskCacheKey(this.uri))) == null) {
            return true;
        }
        if (SLog.isLoggable(65538)) {
            SLog.d(NAME, "Download image completed. %s", this.key);
        }
        if (this.downloadListener != null) {
            this.downloadListener.onCompleted(new DownloadResult(entry, ImageFrom.DISK_CACHE));
        }
        return false;
    }

    private boolean checkParam() {
        if (TextUtils.isEmpty(this.uri)) {
            SLog.e(NAME, "Uri is empty");
            CallbackHandler.postCallbackError(this.downloadListener, ErrorCause.URI_INVALID, this.sync);
            return false;
        }
        if (this.uriModel == null) {
            SLog.e(NAME, "Not support uri. %s", this.uri);
            CallbackHandler.postCallbackError(this.downloadListener, ErrorCause.URI_NO_SUPPORT, this.sync);
            return false;
        }
        if (this.uriModel.isFromNet()) {
            return true;
        }
        SLog.e(NAME, "Only support http ot https. %s", this.uri);
        CallbackHandler.postCallbackError(this.downloadListener, ErrorCause.URI_NO_SUPPORT, this.sync);
        return false;
    }

    private DownloadRequest submitRequest() {
        CallbackHandler.postCallbackStarted(this.downloadListener, this.sync);
        DownloadRequest newDownloadRequest = this.sketch.getConfiguration().getRequestFactory().newDownloadRequest(this.sketch, this.uri, this.uriModel, this.key, this.downloadOptions, this.downloadListener, this.downloadProgressListener);
        newDownloadRequest.setSync(this.sync);
        if (SLog.isLoggable(65538)) {
            SLog.d(NAME, "Run dispatch submitted. %s", this.key);
        }
        newDownloadRequest.submit();
        return newDownloadRequest;
    }

    @Nullable
    public DownloadRequest commit() {
        if (this.sync && SketchUtils.isMainThread()) {
            throw new IllegalStateException("Cannot sync perform the download in the UI thread ");
        }
        if (!checkParam()) {
            return null;
        }
        preProcessOptions();
        if (checkDiskCache()) {
            return submitRequest();
        }
        return null;
    }

    @NonNull
    public DownloadHelper disableCacheInDisk() {
        this.downloadOptions.setCacheInDiskDisabled(true);
        return this;
    }

    @NonNull
    public DownloadHelper downloadProgressListener(@Nullable DownloadProgressListener downloadProgressListener) {
        this.downloadProgressListener = downloadProgressListener;
        return this;
    }

    @NonNull
    public DownloadHelper options(@Nullable DownloadOptions downloadOptions) {
        this.downloadOptions.copy(downloadOptions);
        return this;
    }

    protected void preProcessOptions() {
        this.sketch.getConfiguration().getOptionsFilterManager().filter(this.downloadOptions);
        this.key = SketchUtils.makeRequestKey(this.uri, this.uriModel, this.downloadOptions.makeKey());
    }

    @NonNull
    public DownloadHelper requestLevel(@Nullable RequestLevel requestLevel) {
        if (requestLevel != null) {
            this.downloadOptions.setRequestLevel(requestLevel);
        }
        return this;
    }

    @NonNull
    public DownloadHelper sync() {
        this.sync = true;
        return this;
    }
}
