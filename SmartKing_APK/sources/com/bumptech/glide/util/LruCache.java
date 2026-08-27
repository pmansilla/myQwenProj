package com.bumptech.glide.util;

import android.support.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class LruCache<T, Y> {
    private final LinkedHashMap<T, Y> cache = new LinkedHashMap<>(100, 0.75f, true);
    private int currentSize = 0;
    private final int initialMaxSize;
    private int maxSize;

    public LruCache(int i) {
        this.initialMaxSize = i;
        this.maxSize = i;
    }

    private void evict() {
        trimToSize(this.maxSize);
    }

    public void clearMemory() {
        trimToSize(0);
    }

    public synchronized boolean contains(T t) {
        return this.cache.containsKey(t);
    }

    @Nullable
    public synchronized Y get(T t) {
        return this.cache.get(t);
    }

    public synchronized int getCurrentSize() {
        return this.currentSize;
    }

    public synchronized int getMaxSize() {
        return this.maxSize;
    }

    protected int getSize(Y y) {
        return 1;
    }

    protected void onItemEvicted(T t, Y y) {
    }

    public synchronized Y put(T t, Y y) {
        if (getSize(y) >= this.maxSize) {
            onItemEvicted(t, y);
            return null;
        }
        Y put = this.cache.put(t, y);
        if (y != null) {
            this.currentSize += getSize(y);
        }
        if (put != null) {
            this.currentSize -= getSize(put);
        }
        evict();
        return put;
    }

    @Nullable
    public synchronized Y remove(T t) {
        Y remove;
        remove = this.cache.remove(t);
        if (remove != null) {
            this.currentSize -= getSize(remove);
        }
        return remove;
    }

    public synchronized void setSizeMultiplier(float f) {
        if (f < 0.0f) {
            throw new IllegalArgumentException("Multiplier must be >= 0");
        }
        this.maxSize = Math.round(this.initialMaxSize * f);
        evict();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void trimToSize(int i) {
        while (this.currentSize > i) {
            Map.Entry<T, Y> next = this.cache.entrySet().iterator().next();
            Y value = next.getValue();
            this.currentSize -= getSize(value);
            T key = next.getKey();
            this.cache.remove(key);
            onItemEvicted(key, value);
        }
    }
}
