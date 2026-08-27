package me.panpf.sketch.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/* loaded from: classes2.dex */
public class DownloadOptions {
    private boolean cacheInDiskDisabled;
    private RequestLevel requestLevel;

    public DownloadOptions() {
        reset();
    }

    public DownloadOptions(@NonNull DownloadOptions downloadOptions) {
        copy(downloadOptions);
    }

    public void copy(@Nullable DownloadOptions downloadOptions) {
        if (downloadOptions == null) {
            return;
        }
        this.cacheInDiskDisabled = downloadOptions.cacheInDiskDisabled;
        this.requestLevel = downloadOptions.requestLevel;
    }

    @Nullable
    public RequestLevel getRequestLevel() {
        return this.requestLevel;
    }

    public boolean isCacheInDiskDisabled() {
        return this.cacheInDiskDisabled;
    }

    @NonNull
    public String makeKey() {
        return "";
    }

    @NonNull
    public String makeStateImageKey() {
        return "";
    }

    public void reset() {
        this.cacheInDiskDisabled = false;
        this.requestLevel = null;
    }

    @NonNull
    public DownloadOptions setCacheInDiskDisabled(boolean z) {
        this.cacheInDiskDisabled = z;
        return this;
    }

    @NonNull
    public DownloadOptions setRequestLevel(@Nullable RequestLevel requestLevel) {
        this.requestLevel = requestLevel;
        return this;
    }
}
