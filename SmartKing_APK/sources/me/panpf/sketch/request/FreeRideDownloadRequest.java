package me.panpf.sketch.request;

import java.util.HashSet;
import java.util.Set;
import me.panpf.sketch.SLog;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.cache.DiskCache;
import me.panpf.sketch.request.FreeRideManager;
import me.panpf.sketch.uri.UriModel;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class FreeRideDownloadRequest extends DownloadRequest implements FreeRideManager.DownloadFreeRide {
    private Set<FreeRideManager.DownloadFreeRide> downloadFreeRideSet;

    public FreeRideDownloadRequest(Sketch sketch, String str, UriModel uriModel, String str2, DownloadOptions downloadOptions, DownloadListener downloadListener, DownloadProgressListener downloadProgressListener) {
        super(sketch, str, uriModel, str2, downloadOptions, downloadListener, downloadProgressListener);
    }

    @Override // me.panpf.sketch.request.FreeRideManager.DownloadFreeRide
    public synchronized void byDownloadFreeRide(FreeRideManager.DownloadFreeRide downloadFreeRide) {
        if (this.downloadFreeRideSet == null) {
            synchronized (this) {
                if (this.downloadFreeRideSet == null) {
                    this.downloadFreeRideSet = new HashSet();
                }
            }
        }
        this.downloadFreeRideSet.add(downloadFreeRide);
    }

    @Override // me.panpf.sketch.request.FreeRideManager.DownloadFreeRide
    public boolean canByDownloadFreeRide() {
        DiskCache diskCache = getConfiguration().getDiskCache();
        return (diskCache.isClosed() || diskCache.isDisabled() || getOptions().isCacheInDiskDisabled() || isSync() || getConfiguration().getExecutor().isShutdown()) ? false : true;
    }

    @Override // me.panpf.sketch.request.FreeRideManager.DownloadFreeRide
    public String getDownloadFreeRideKey() {
        return getUri();
    }

    @Override // me.panpf.sketch.request.FreeRideManager.DownloadFreeRide
    public String getDownloadFreeRideLog() {
        return String.format("%s@%s", SketchUtils.toHexString(this), getKey());
    }

    @Override // me.panpf.sketch.request.FreeRideManager.DownloadFreeRide
    public Set<FreeRideManager.DownloadFreeRide> getDownloadFreeRideSet() {
        return this.downloadFreeRideSet;
    }

    @Override // me.panpf.sketch.request.FreeRideManager.DownloadFreeRide
    public synchronized boolean processDownloadFreeRide() {
        DiskCache.Entry entry = getConfiguration().getDiskCache().get(getDiskCacheKey());
        if (entry == null) {
            submitRunDownload();
            return false;
        }
        if (SLog.isLoggable(65538)) {
            SLog.d(getLogName(), "from diskCache. processDownloadFreeRide. %s. %s", getThreadName(), getKey());
        }
        this.downloadResult = new DownloadResult(entry, ImageFrom.DISK_CACHE);
        downloadCompleted();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.request.DownloadRequest, me.panpf.sketch.request.AsyncRequest
    public void runDownload() {
        super.runDownload();
        if (canByDownloadFreeRide()) {
            getConfiguration().getFreeRideManager().unregisterDownloadFreeRideProvider(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.request.DownloadRequest, me.panpf.sketch.request.AsyncRequest
    public void submitRunDownload() {
        if (canByDownloadFreeRide()) {
            FreeRideManager freeRideManager = getConfiguration().getFreeRideManager();
            if (freeRideManager.byDownloadFreeRide(this)) {
                return;
            } else {
                freeRideManager.registerDownloadFreeRideProvider(this);
            }
        }
        super.submitRunDownload();
    }

    @Override // me.panpf.sketch.request.DownloadRequest
    public void updateProgress(int i, int i2) {
        super.updateProgress(i, i2);
        if (this.downloadFreeRideSet == null || this.downloadFreeRideSet.isEmpty()) {
            return;
        }
        for (Object obj : this.downloadFreeRideSet) {
            if (obj != null && (obj instanceof DownloadRequest)) {
                ((DownloadRequest) obj).updateProgress(i, i2);
            }
        }
    }
}
