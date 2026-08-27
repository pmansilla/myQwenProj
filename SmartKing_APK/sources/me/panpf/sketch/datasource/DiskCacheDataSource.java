package me.panpf.sketch.datasource;

import android.support.annotation.NonNull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import me.panpf.sketch.cache.BitmapPool;
import me.panpf.sketch.cache.DiskCache;
import me.panpf.sketch.decode.ImageAttrs;
import me.panpf.sketch.decode.NotFoundGifLibraryException;
import me.panpf.sketch.drawable.SketchGifDrawable;
import me.panpf.sketch.drawable.SketchGifFactory;
import me.panpf.sketch.request.ImageFrom;

/* loaded from: classes2.dex */
public class DiskCacheDataSource implements DataSource {
    private DiskCache.Entry diskCacheEntry;
    private boolean fromProcessedCache;
    private ImageFrom imageFrom;
    private long length = -1;

    public DiskCacheDataSource(DiskCache.Entry entry, ImageFrom imageFrom) {
        this.diskCacheEntry = entry;
        this.imageFrom = imageFrom;
    }

    public DiskCache.Entry getDiskCacheEntry() {
        return this.diskCacheEntry;
    }

    @Override // me.panpf.sketch.datasource.DataSource
    public File getFile(File file, String str) {
        return this.diskCacheEntry.getFile();
    }

    @Override // me.panpf.sketch.datasource.DataSource
    @NonNull
    public ImageFrom getImageFrom() {
        return this.imageFrom;
    }

    @Override // me.panpf.sketch.datasource.DataSource
    @NonNull
    public InputStream getInputStream() throws IOException {
        return this.diskCacheEntry.newInputStream();
    }

    @Override // me.panpf.sketch.datasource.DataSource
    public long getLength() throws IOException {
        if (this.length >= 0) {
            return this.length;
        }
        this.length = this.diskCacheEntry.getFile().length();
        return this.length;
    }

    public boolean isFromProcessedCache() {
        return this.fromProcessedCache;
    }

    @Override // me.panpf.sketch.datasource.DataSource
    @NonNull
    public SketchGifDrawable makeGifDrawable(@NonNull String str, @NonNull String str2, @NonNull ImageAttrs imageAttrs, @NonNull BitmapPool bitmapPool) throws IOException, NotFoundGifLibraryException {
        return SketchGifFactory.createGifDrawable(str, str2, imageAttrs, getImageFrom(), bitmapPool, this.diskCacheEntry.getFile());
    }

    public DiskCacheDataSource setFromProcessedCache(boolean z) {
        this.fromProcessedCache = z;
        return this;
    }
}
