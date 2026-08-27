package me.panpf.sketch.cache.recycle;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class AttributeStrategy implements LruPoolStrategy {
    private final KeyPool keyPool = new KeyPool();
    private final GroupedLinkedMap<Key, Bitmap> groupedMap = new GroupedLinkedMap<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class Key implements Poolable {
        private Bitmap.Config config;
        private int height;
        private final KeyPool pool;
        private int width;

        public Key(KeyPool keyPool) {
            this.pool = keyPool;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Key)) {
                return false;
            }
            Key key = (Key) obj;
            return this.width == key.width && this.height == key.height && this.config == key.config;
        }

        public int hashCode() {
            return (((this.width * 31) + this.height) * 31) + (this.config != null ? this.config.hashCode() : 0);
        }

        public void init(int i, int i2, Bitmap.Config config) {
            this.width = i;
            this.height = i2;
            this.config = config;
        }

        @Override // me.panpf.sketch.cache.recycle.Poolable
        public void offer() {
            this.pool.offer(this);
        }

        public String toString() {
            return AttributeStrategy.getBitmapString(this.width, this.height, this.config);
        }
    }

    /* loaded from: classes2.dex */
    static class KeyPool extends BaseKeyPool<Key> {
        KeyPool() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // me.panpf.sketch.cache.recycle.BaseKeyPool
        public Key create() {
            return new Key(this);
        }

        public Key get(int i, int i2, Bitmap.Config config) {
            Key key = get();
            key.init(i, i2, config);
            return key;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getBitmapString(int i, int i2, Bitmap.Config config) {
        return "[" + i + "x" + i2 + "], " + config;
    }

    private static String getBitmapString(Bitmap bitmap) {
        return getBitmapString(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
    }

    @Override // me.panpf.sketch.cache.recycle.LruPoolStrategy
    public Bitmap get(int i, int i2, Bitmap.Config config) {
        return this.groupedMap.get(this.keyPool.get(i, i2, config));
    }

    @Override // me.panpf.sketch.Key
    @Nullable
    public String getKey() {
        return "AttributeStrategy";
    }

    @Override // me.panpf.sketch.cache.recycle.LruPoolStrategy
    public int getSize(Bitmap bitmap) {
        return SketchUtils.getByteCount(bitmap);
    }

    @Override // me.panpf.sketch.cache.recycle.LruPoolStrategy
    public String logBitmap(int i, int i2, Bitmap.Config config) {
        return getBitmapString(i, i2, config);
    }

    @Override // me.panpf.sketch.cache.recycle.LruPoolStrategy
    public String logBitmap(Bitmap bitmap) {
        return getBitmapString(bitmap);
    }

    @Override // me.panpf.sketch.cache.recycle.LruPoolStrategy
    public void put(Bitmap bitmap) {
        this.groupedMap.put(this.keyPool.get(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig()), bitmap);
    }

    @Override // me.panpf.sketch.cache.recycle.LruPoolStrategy
    public Bitmap removeLast() {
        return this.groupedMap.removeLast();
    }

    public String toString() {
        return "AttributeStrategy(" + this.groupedMap + "）";
    }
}
