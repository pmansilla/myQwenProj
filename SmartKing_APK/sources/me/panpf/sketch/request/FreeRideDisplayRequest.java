package me.panpf.sketch.request;

import java.util.HashSet;
import java.util.Set;
import me.panpf.sketch.SLog;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.cache.MemoryCache;
import me.panpf.sketch.drawable.SketchBitmapDrawable;
import me.panpf.sketch.drawable.SketchRefBitmap;
import me.panpf.sketch.request.FreeRideManager;
import me.panpf.sketch.uri.UriModel;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class FreeRideDisplayRequest extends DisplayRequest implements FreeRideManager.DisplayFreeRide {
    private Set<FreeRideManager.DisplayFreeRide> displayFreeRideSet;

    public FreeRideDisplayRequest(Sketch sketch, String str, UriModel uriModel, String str2, DisplayOptions displayOptions, ViewInfo viewInfo, RequestAndViewBinder requestAndViewBinder, DisplayListener displayListener, DownloadProgressListener downloadProgressListener) {
        super(sketch, str, uriModel, str2, displayOptions, viewInfo, requestAndViewBinder, displayListener, downloadProgressListener);
    }

    @Override // me.panpf.sketch.request.FreeRideManager.DisplayFreeRide
    public synchronized void byDisplayFreeRide(FreeRideManager.DisplayFreeRide displayFreeRide) {
        if (this.displayFreeRideSet == null) {
            synchronized (this) {
                if (this.displayFreeRideSet == null) {
                    this.displayFreeRideSet = new HashSet();
                }
            }
        }
        this.displayFreeRideSet.add(displayFreeRide);
    }

    @Override // me.panpf.sketch.request.FreeRideManager.DisplayFreeRide
    public boolean canByDisplayFreeRide() {
        MemoryCache memoryCache = getConfiguration().getMemoryCache();
        return (memoryCache.isClosed() || memoryCache.isDisabled() || getOptions().isCacheInMemoryDisabled() || getOptions().isDecodeGifImage() || isSync() || getConfiguration().getExecutor().isShutdown()) ? false : true;
    }

    @Override // me.panpf.sketch.request.FreeRideManager.DisplayFreeRide
    public String getDisplayFreeRideKey() {
        return getKey();
    }

    @Override // me.panpf.sketch.request.FreeRideManager.DisplayFreeRide
    public String getDisplayFreeRideLog() {
        return String.format("%s@%s", SketchUtils.toHexString(this), getKey());
    }

    @Override // me.panpf.sketch.request.FreeRideManager.DisplayFreeRide
    public Set<FreeRideManager.DisplayFreeRide> getDisplayFreeRideSet() {
        return this.displayFreeRideSet;
    }

    @Override // me.panpf.sketch.request.FreeRideManager.DisplayFreeRide
    public synchronized boolean processDisplayFreeRide() {
        if (!getOptions().isCacheInDiskDisabled()) {
            MemoryCache memoryCache = getConfiguration().getMemoryCache();
            SketchRefBitmap sketchRefBitmap = memoryCache.get(getMemoryCacheKey());
            if (sketchRefBitmap != null && sketchRefBitmap.isRecycled()) {
                memoryCache.remove(getMemoryCacheKey());
                SLog.e(getLogName(), "memory cache drawable recycled. processFreeRideRequests. bitmap=%s. %s. %s", sketchRefBitmap.getInfo(), getThreadName(), getKey());
                sketchRefBitmap = null;
            }
            if (sketchRefBitmap != null) {
                sketchRefBitmap.setIsWaitingUse(String.format("%s:waitingUse:fromMemory", getLogName()), true);
                this.displayResult = new DisplayResult(new SketchBitmapDrawable(sketchRefBitmap, ImageFrom.MEMORY_CACHE), ImageFrom.MEMORY_CACHE, sketchRefBitmap.getAttrs());
                displayCompleted();
                return true;
            }
        }
        submitRunLoad();
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.request.DisplayRequest, me.panpf.sketch.request.LoadRequest, me.panpf.sketch.request.DownloadRequest, me.panpf.sketch.request.AsyncRequest
    public void runLoad() {
        super.runLoad();
        if (canByDisplayFreeRide()) {
            getConfiguration().getFreeRideManager().unregisterDisplayFreeRideProvider(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // me.panpf.sketch.request.DownloadRequest, me.panpf.sketch.request.AsyncRequest
    public void submitRunLoad() {
        if (canByDisplayFreeRide()) {
            FreeRideManager freeRideManager = getConfiguration().getFreeRideManager();
            if (freeRideManager.byDisplayFreeRide(this)) {
                return;
            } else {
                freeRideManager.registerDisplayFreeRideProvider(this);
            }
        }
        super.submitRunLoad();
    }
}
