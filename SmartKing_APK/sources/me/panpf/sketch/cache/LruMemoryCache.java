package me.panpf.sketch.cache;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.format.Formatter;
import me.panpf.sketch.SLog;
import me.panpf.sketch.drawable.SketchRefBitmap;
import me.panpf.sketch.util.LruCache;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class LruMemoryCache implements MemoryCache {
    private static final String NAME = "LruMemoryCache";
    private final LruCache<String, SketchRefBitmap> cache;
    private boolean closed;
    private Context context;
    private boolean disabled;

    /* loaded from: classes2.dex */
    private static class RefBitmapLruCache extends LruCache<String, SketchRefBitmap> {
        public RefBitmapLruCache(int i) {
            super(i);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // me.panpf.sketch.util.LruCache
        public void entryRemoved(boolean z, String str, SketchRefBitmap sketchRefBitmap, SketchRefBitmap sketchRefBitmap2) {
            sketchRefBitmap.setIsCached("LruMemoryCache:entryRemoved", false);
        }

        @Override // me.panpf.sketch.util.LruCache
        public SketchRefBitmap put(String str, SketchRefBitmap sketchRefBitmap) {
            sketchRefBitmap.setIsCached("LruMemoryCache:put", true);
            return (SketchRefBitmap) super.put((RefBitmapLruCache) str, (String) sketchRefBitmap);
        }

        @Override // me.panpf.sketch.util.LruCache
        public int sizeOf(String str, SketchRefBitmap sketchRefBitmap) {
            int byteCount = sketchRefBitmap.getByteCount();
            if (byteCount == 0) {
                return 1;
            }
            return byteCount;
        }
    }

    public LruMemoryCache(Context context, int i) {
        this.context = context.getApplicationContext();
        this.cache = new RefBitmapLruCache(i);
    }

    @Override // me.panpf.sketch.cache.MemoryCache
    public synchronized void clear() {
        if (this.closed) {
            return;
        }
        SLog.w(NAME, "clear. before size: %s", Formatter.formatFileSize(this.context, this.cache.size()));
        this.cache.evictAll();
    }

    @Override // me.panpf.sketch.cache.MemoryCache
    public synchronized void close() {
        if (this.closed) {
            return;
        }
        this.closed = true;
        this.cache.evictAll();
    }

    @Override // me.panpf.sketch.cache.MemoryCache
    public synchronized SketchRefBitmap get(@NonNull String str) {
        if (this.closed) {
            return null;
        }
        if (!this.disabled) {
            return this.cache.get(str);
        }
        if (SLog.isLoggable(131074)) {
            SLog.d(NAME, "Disabled. Unable get, key=%s", str);
        }
        return null;
    }

    @Override // me.panpf.sketch.cache.MemoryCache
    public long getMaxSize() {
        return this.cache.maxSize();
    }

    @Override // me.panpf.sketch.cache.MemoryCache
    public synchronized long getSize() {
        if (this.closed) {
            return 0L;
        }
        return this.cache.size();
    }

    @Override // me.panpf.sketch.cache.MemoryCache
    public synchronized boolean isClosed() {
        return this.closed;
    }

    @Override // me.panpf.sketch.cache.MemoryCache
    public boolean isDisabled() {
        return this.disabled;
    }

    @Override // me.panpf.sketch.cache.MemoryCache
    public synchronized void put(@NonNull String str, @NonNull SketchRefBitmap sketchRefBitmap) {
        if (this.closed) {
            return;
        }
        if (this.disabled) {
            if (SLog.isLoggable(131074)) {
                SLog.d(NAME, "Disabled. Unable put, key=%s", str);
            }
        } else {
            if (this.cache.get(str) != null) {
                SLog.w(NAME, String.format("Exist. key=%s", str));
                return;
            }
            int size = SLog.isLoggable(131074) ? this.cache.size() : 0;
            this.cache.put(str, sketchRefBitmap);
            if (SLog.isLoggable(131074)) {
                SLog.d(NAME, "put. beforeCacheSize=%s. %s. afterCacheSize=%s", Formatter.formatFileSize(this.context, size), sketchRefBitmap.getInfo(), Formatter.formatFileSize(this.context, this.cache.size()));
            }
        }
    }

    @Override // me.panpf.sketch.cache.MemoryCache
    public synchronized SketchRefBitmap remove(@NonNull String str) {
        if (this.closed) {
            return null;
        }
        if (this.disabled) {
            if (SLog.isLoggable(131074)) {
                SLog.d(NAME, "Disabled. Unable remove, key=%s", str);
            }
            return null;
        }
        SketchRefBitmap remove = this.cache.remove(str);
        if (SLog.isLoggable(131074)) {
            SLog.d(NAME, "remove. memoryCacheSize: %s", Formatter.formatFileSize(this.context, this.cache.size()));
        }
        return remove;
    }

    @Override // me.panpf.sketch.cache.MemoryCache
    public void setDisabled(boolean z) {
        if (this.disabled != z) {
            this.disabled = z;
            if (z) {
                SLog.w(NAME, "setDisabled. %s", true);
            } else {
                SLog.w(NAME, "setDisabled. %s", false);
            }
        }
    }

    @NonNull
    public String toString() {
        return String.format("%s(maxSize=%s)", NAME, Formatter.formatFileSize(this.context, getMaxSize()));
    }

    @Override // me.panpf.sketch.cache.MemoryCache
    public synchronized void trimMemory(int i) {
        if (this.closed) {
            return;
        }
        long size = getSize();
        if (i >= 60) {
            this.cache.evictAll();
        } else if (i >= 40) {
            this.cache.trimToSize(this.cache.maxSize() / 2);
        }
        SLog.w(NAME, "trimMemory. level=%s, released: %s", SketchUtils.getTrimLevelName(i), Formatter.formatFileSize(this.context, size - getSize()));
    }
}
