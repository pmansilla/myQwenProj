package me.panpf.sketch.request;

import me.panpf.sketch.cache.DiskCache;

/* loaded from: classes2.dex */
public class DownloadResult {
    private DiskCache.Entry diskCacheEntry;
    private byte[] imageData;
    private ImageFrom imageFrom;

    public DownloadResult(DiskCache.Entry entry, ImageFrom imageFrom) {
        this.diskCacheEntry = entry;
        this.imageFrom = imageFrom;
    }

    public DownloadResult(byte[] bArr, ImageFrom imageFrom) {
        this.imageData = bArr;
        this.imageFrom = imageFrom;
    }

    public DiskCache.Entry getDiskCacheEntry() {
        return this.diskCacheEntry;
    }

    public byte[] getImageData() {
        return this.imageData;
    }

    public ImageFrom getImageFrom() {
        return this.imageFrom;
    }

    public boolean hasData() {
        return this.diskCacheEntry != null || (this.imageData != null && this.imageData.length > 0);
    }
}
