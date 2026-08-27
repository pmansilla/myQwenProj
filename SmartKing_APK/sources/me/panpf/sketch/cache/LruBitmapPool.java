package me.panpf.sketch.cache;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.format.Formatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import me.panpf.sketch.SLog;
import me.panpf.sketch.cache.recycle.AttributeStrategy;
import me.panpf.sketch.cache.recycle.LruPoolStrategy;
import me.panpf.sketch.cache.recycle.SizeConfigStrategy;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class LruBitmapPool implements BitmapPool {
    private static final Bitmap.Config DEFAULT_CONFIG = Bitmap.Config.ARGB_8888;
    private static final String NAME = "LruBitmapPool";

    @NonNull
    private final Set<Bitmap.Config> allowedConfigs;
    private boolean closed;
    private Context context;
    private int currentSize;
    private boolean disabled;
    private int evictions;
    private int hits;
    private final int initialMaxSize;
    private int maxSize;
    private int misses;
    private int puts;

    @NonNull
    private final LruPoolStrategy strategy;
    private final BitmapTracker tracker;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public interface BitmapTracker {
        void add(Bitmap bitmap);

        void remove(Bitmap bitmap);
    }

    /* loaded from: classes2.dex */
    private static class NullBitmapTracker implements BitmapTracker {
        private NullBitmapTracker() {
        }

        @Override // me.panpf.sketch.cache.LruBitmapPool.BitmapTracker
        public void add(Bitmap bitmap) {
        }

        @Override // me.panpf.sketch.cache.LruBitmapPool.BitmapTracker
        public void remove(Bitmap bitmap) {
        }
    }

    /* loaded from: classes2.dex */
    private static class ThrowingBitmapTracker implements BitmapTracker {
        private final Set<Bitmap> bitmaps = Collections.synchronizedSet(new HashSet());

        private ThrowingBitmapTracker() {
        }

        @Override // me.panpf.sketch.cache.LruBitmapPool.BitmapTracker
        public void add(Bitmap bitmap) {
            if (!this.bitmaps.contains(bitmap)) {
                this.bitmaps.add(bitmap);
                return;
            }
            throw new IllegalStateException("Can't add already added bitmap: " + bitmap + " [" + bitmap.getWidth() + "x" + bitmap.getHeight() + "]");
        }

        @Override // me.panpf.sketch.cache.LruBitmapPool.BitmapTracker
        public void remove(Bitmap bitmap) {
            if (!this.bitmaps.contains(bitmap)) {
                throw new IllegalStateException("Cannot remove bitmap not in tracker");
            }
            this.bitmaps.remove(bitmap);
        }
    }

    public LruBitmapPool(Context context, int i) {
        this(context, i, getDefaultStrategy(), getDefaultAllowedConfigs());
    }

    public LruBitmapPool(Context context, int i, @NonNull Set<Bitmap.Config> set) {
        this(context, i, getDefaultStrategy(), set);
    }

    LruBitmapPool(Context context, int i, @NonNull LruPoolStrategy lruPoolStrategy, @NonNull Set<Bitmap.Config> set) {
        this.context = context.getApplicationContext();
        this.initialMaxSize = i;
        this.maxSize = i;
        this.strategy = lruPoolStrategy;
        this.allowedConfigs = set;
        this.tracker = new NullBitmapTracker();
    }

    private void dump() {
        dumpUnchecked();
    }

    private void dumpUnchecked() {
        if (SLog.isLoggable(131074)) {
            SLog.d(NAME, "Hits=%d, misses=%d, puts=%d, evictions=%d, currentSize=%d, maxSize=%d, Strategy=%s", Integer.valueOf(this.hits), Integer.valueOf(this.misses), Integer.valueOf(this.puts), Integer.valueOf(this.evictions), Integer.valueOf(this.currentSize), Integer.valueOf(this.maxSize), this.strategy);
        }
    }

    private void evict() {
        if (this.closed) {
            return;
        }
        trimToSize(this.maxSize);
    }

    private static Set<Bitmap.Config> getDefaultAllowedConfigs() {
        HashSet hashSet = new HashSet();
        hashSet.addAll(Arrays.asList(Bitmap.Config.values()));
        if (Build.VERSION.SDK_INT >= 19) {
            hashSet.add(null);
        }
        return Collections.unmodifiableSet(hashSet);
    }

    private static LruPoolStrategy getDefaultStrategy() {
        return Build.VERSION.SDK_INT >= 19 ? new SizeConfigStrategy() : new AttributeStrategy();
    }

    private synchronized void trimToSize(int i) {
        while (this.currentSize > i) {
            Bitmap removeLast = this.strategy.removeLast();
            if (removeLast == null) {
                SLog.w(NAME, "Size mismatch, resetting");
                dumpUnchecked();
                this.currentSize = 0;
                return;
            } else {
                if (SLog.isLoggable(131074)) {
                    SLog.d(NAME, "Evicting bitmap=%s,%s", this.strategy.logBitmap(removeLast), SketchUtils.toHexString(removeLast));
                }
                this.tracker.remove(removeLast);
                this.currentSize -= this.strategy.getSize(removeLast);
                removeLast.recycle();
                this.evictions++;
                dump();
            }
        }
    }

    @Override // me.panpf.sketch.cache.BitmapPool
    public synchronized void clear() {
        SLog.w(NAME, "clear. before size %s", Formatter.formatFileSize(this.context, getSize()));
        trimToSize(0);
    }

    @Override // me.panpf.sketch.cache.BitmapPool
    public synchronized void close() {
        if (this.closed) {
            return;
        }
        this.closed = true;
        trimToSize(0);
    }

    @Override // me.panpf.sketch.cache.BitmapPool
    public synchronized Bitmap get(int i, int i2, @NonNull Bitmap.Config config) {
        Bitmap dirty;
        dirty = getDirty(i, i2, config);
        if (dirty != null) {
            dirty.eraseColor(0);
        }
        return dirty;
    }

    @Override // me.panpf.sketch.cache.BitmapPool
    @TargetApi(12)
    public synchronized Bitmap getDirty(int i, int i2, @NonNull Bitmap.Config config) {
        if (this.closed) {
            return null;
        }
        if (this.disabled) {
            if (SLog.isLoggable(131074)) {
                SLog.d(NAME, "Disabled. Unable get, bitmap=%s,%s", this.strategy.logBitmap(i, i2, config));
            }
            return null;
        }
        Bitmap bitmap = this.strategy.get(i, i2, config != null ? config : DEFAULT_CONFIG);
        if (bitmap == null) {
            if (SLog.isLoggable(131074)) {
                SLog.d(NAME, "Missing bitmap=%s", this.strategy.logBitmap(i, i2, config));
            }
            this.misses++;
        } else {
            if (SLog.isLoggable(131074)) {
                SLog.d(NAME, "Get bitmap=%s,%s", this.strategy.logBitmap(i, i2, config), SketchUtils.toHexString(bitmap));
            }
            this.hits++;
            this.currentSize -= this.strategy.getSize(bitmap);
            this.tracker.remove(bitmap);
            if (Build.VERSION.SDK_INT >= 12) {
                bitmap.setHasAlpha(true);
            }
        }
        dump();
        return bitmap;
    }

    @Override // me.panpf.sketch.cache.BitmapPool
    public int getMaxSize() {
        return this.maxSize;
    }

    @Override // me.panpf.sketch.cache.BitmapPool
    @NonNull
    public Bitmap getOrMake(int i, int i2, @NonNull Bitmap.Config config) {
        Bitmap bitmap = get(i, i2, config);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(i, i2, config);
            if (SLog.isLoggable(131074)) {
                StackTraceElement[] stackTrace = new Exception().getStackTrace();
                StackTraceElement stackTraceElement = stackTrace.length > 1 ? stackTrace[1] : stackTrace[0];
                SLog.d(NAME, "Make bitmap. info:%dx%d,%s,%s - %s.%s:%d", Integer.valueOf(bitmap.getWidth()), Integer.valueOf(bitmap.getHeight()), bitmap.getConfig(), SketchUtils.toHexString(bitmap), stackTraceElement.getClassName(), stackTraceElement.getMethodName(), Integer.valueOf(stackTraceElement.getLineNumber()));
            }
        }
        return bitmap;
    }

    @Override // me.panpf.sketch.cache.BitmapPool
    public int getSize() {
        return this.currentSize;
    }

    @Override // me.panpf.sketch.cache.BitmapPool
    public synchronized boolean isClosed() {
        return this.closed;
    }

    @Override // me.panpf.sketch.cache.BitmapPool
    public boolean isDisabled() {
        return this.disabled;
    }

    @Override // me.panpf.sketch.cache.BitmapPool
    public synchronized boolean put(@NonNull Bitmap bitmap) {
        if (this.closed) {
            return false;
        }
        if (this.disabled) {
            if (SLog.isLoggable(131074)) {
                SLog.d(NAME, "Disabled. Unable put, bitmap=%s,%s", this.strategy.logBitmap(bitmap), SketchUtils.toHexString(bitmap));
            }
            return false;
        }
        if (bitmap == null) {
            throw new NullPointerException("Bitmap must not be null");
        }
        if (!bitmap.isRecycled() && bitmap.isMutable() && this.strategy.getSize(bitmap) <= this.maxSize && this.allowedConfigs.contains(bitmap.getConfig())) {
            int size = this.strategy.getSize(bitmap);
            this.strategy.put(bitmap);
            this.tracker.add(bitmap);
            this.puts++;
            this.currentSize += size;
            if (SLog.isLoggable(131074)) {
                SLog.d(NAME, "Put bitmap in pool=%s,%s", this.strategy.logBitmap(bitmap), SketchUtils.toHexString(bitmap));
            }
            dump();
            evict();
            return true;
        }
        SLog.w(NAME, "Reject bitmap from pool, bitmap: %s, is recycled: %s, is mutable: %s, is allowed config: %s, %s", this.strategy.logBitmap(bitmap), Boolean.valueOf(bitmap.isRecycled()), Boolean.valueOf(bitmap.isMutable()), Boolean.valueOf(this.allowedConfigs.contains(bitmap.getConfig())), SketchUtils.toHexString(bitmap));
        return false;
    }

    @Override // me.panpf.sketch.cache.BitmapPool
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

    @Override // me.panpf.sketch.cache.BitmapPool
    public synchronized void setSizeMultiplier(float f) {
        if (this.closed) {
            return;
        }
        this.maxSize = Math.round(this.initialMaxSize * f);
        evict();
    }

    @NonNull
    public String toString() {
        return String.format("%s(maxSize=%s,strategy=%s,allowedConfigs=%s)", NAME, Formatter.formatFileSize(this.context, getMaxSize()), this.strategy.getKey(), this.allowedConfigs.toString());
    }

    @Override // me.panpf.sketch.cache.BitmapPool
    @SuppressLint({"InlinedApi"})
    public synchronized void trimMemory(int i) {
        long size = getSize();
        if (i >= 60) {
            trimToSize(0);
        } else if (i >= 40) {
            trimToSize(this.maxSize / 2);
        }
        SLog.w(NAME, "trimMemory. level=%s, released: %s", SketchUtils.getTrimLevelName(i), Formatter.formatFileSize(this.context, size - getSize()));
    }
}
