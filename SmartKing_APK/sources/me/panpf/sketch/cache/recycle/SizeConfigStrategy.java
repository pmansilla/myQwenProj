package me.panpf.sketch.cache.recycle;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import com.litesuits.orm.db.assit.SQLBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import me.panpf.sketch.util.SketchUtils;

@TargetApi(19)
/* loaded from: classes2.dex */
public class SizeConfigStrategy implements LruPoolStrategy {
    private static final int MAX_SIZE_MULTIPLE = 8;
    private static final Bitmap.Config[] ARGB_8888_IN_CONFIGS = {Bitmap.Config.ARGB_8888, null};
    private static final Bitmap.Config[] RGB_565_IN_CONFIGS = {Bitmap.Config.RGB_565};
    private static final Bitmap.Config[] ARGB_4444_IN_CONFIGS = {Bitmap.Config.ARGB_4444};
    private static final Bitmap.Config[] ALPHA_8_IN_CONFIGS = {Bitmap.Config.ALPHA_8};
    private final KeyPool keyPool = new KeyPool();
    private final GroupedLinkedMap<Key, Bitmap> groupedMap = new GroupedLinkedMap<>();
    private final Map<Bitmap.Config, NavigableMap<Integer, Integer>> sortedSizes = new HashMap();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: me.panpf.sketch.cache.recycle.SizeConfigStrategy$1, reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$graphics$Bitmap$Config = new int[Bitmap.Config.values().length];

        static {
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Bitmap.Config.ARGB_8888.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Bitmap.Config.RGB_565.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Bitmap.Config.ARGB_4444.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Bitmap.Config.ALPHA_8.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static final class Key implements Poolable {
        private Bitmap.Config config;
        private final KeyPool pool;
        private int size;

        public Key(KeyPool keyPool) {
            this.pool = keyPool;
        }

        Key(KeyPool keyPool, int i, Bitmap.Config config) {
            this(keyPool);
            init(i, config);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Key)) {
                return false;
            }
            Key key = (Key) obj;
            if (this.size != key.size) {
                return false;
            }
            if (this.config == null) {
                if (key.config != null) {
                    return false;
                }
            } else if (!this.config.equals(key.config)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return (this.size * 31) + (this.config != null ? this.config.hashCode() : 0);
        }

        public void init(int i, Bitmap.Config config) {
            this.size = i;
            this.config = config;
        }

        @Override // me.panpf.sketch.cache.recycle.Poolable
        public void offer() {
            this.pool.offer(this);
        }

        public String toString() {
            return SizeConfigStrategy.getBitmapString(this.size, this.config);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class KeyPool extends BaseKeyPool<Key> {
        KeyPool() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // me.panpf.sketch.cache.recycle.BaseKeyPool
        public Key create() {
            return new Key(this);
        }

        public Key get(int i, Bitmap.Config config) {
            Key key = get();
            key.init(i, config);
            return key;
        }
    }

    private void decrementBitmapOfSize(Integer num, Bitmap.Config config) {
        NavigableMap<Integer, Integer> sizesForConfig = getSizesForConfig(config);
        Integer num2 = (Integer) sizesForConfig.get(num);
        if (num2.intValue() == 1) {
            sizesForConfig.remove(num);
        } else {
            sizesForConfig.put(num, Integer.valueOf(num2.intValue() - 1));
        }
    }

    private Key findBestKey(Key key, int i, Bitmap.Config config) {
        for (Bitmap.Config config2 : getInConfigs(config)) {
            Integer ceilingKey = getSizesForConfig(config2).ceilingKey(Integer.valueOf(i));
            if (ceilingKey != null && ceilingKey.intValue() <= i * 8) {
                if (ceilingKey.intValue() == i) {
                    if (config2 == null) {
                        if (config == null) {
                            return key;
                        }
                    } else if (config2.equals(config)) {
                        return key;
                    }
                }
                this.keyPool.offer(key);
                return this.keyPool.get(ceilingKey.intValue(), config2);
            }
        }
        return key;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getBitmapString(int i, Bitmap.Config config) {
        return "[" + i + "](" + config + SQLBuilder.PARENTHESES_RIGHT;
    }

    private static Bitmap.Config[] getInConfigs(Bitmap.Config config) {
        switch (AnonymousClass1.$SwitchMap$android$graphics$Bitmap$Config[config.ordinal()]) {
            case 1:
                return ARGB_8888_IN_CONFIGS;
            case 2:
                return RGB_565_IN_CONFIGS;
            case 3:
                return ARGB_4444_IN_CONFIGS;
            case 4:
                return ALPHA_8_IN_CONFIGS;
            default:
                return new Bitmap.Config[]{config};
        }
    }

    private NavigableMap<Integer, Integer> getSizesForConfig(Bitmap.Config config) {
        NavigableMap<Integer, Integer> navigableMap = this.sortedSizes.get(config);
        if (navigableMap != null) {
            return navigableMap;
        }
        TreeMap treeMap = new TreeMap();
        this.sortedSizes.put(config, treeMap);
        return treeMap;
    }

    @Override // me.panpf.sketch.cache.recycle.LruPoolStrategy
    public Bitmap get(int i, int i2, Bitmap.Config config) {
        int computeByteCount = SketchUtils.computeByteCount(i, i2, config);
        Bitmap bitmap = this.groupedMap.get(findBestKey(this.keyPool.get(computeByteCount, config), computeByteCount, config));
        if (bitmap != null) {
            decrementBitmapOfSize(Integer.valueOf(SketchUtils.getByteCount(bitmap)), bitmap.getConfig());
            try {
                bitmap.reconfigure(i, i2, bitmap.getConfig() != null ? bitmap.getConfig() : Bitmap.Config.ARGB_8888);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                put(bitmap);
            }
        }
        return bitmap;
    }

    @Override // me.panpf.sketch.Key
    @Nullable
    public String getKey() {
        return "SizeConfigStrategy";
    }

    @Override // me.panpf.sketch.cache.recycle.LruPoolStrategy
    public int getSize(Bitmap bitmap) {
        return SketchUtils.getByteCount(bitmap);
    }

    @Override // me.panpf.sketch.cache.recycle.LruPoolStrategy
    public String logBitmap(int i, int i2, Bitmap.Config config) {
        return getBitmapString(SketchUtils.computeByteCount(i, i2, config), config);
    }

    @Override // me.panpf.sketch.cache.recycle.LruPoolStrategy
    public String logBitmap(Bitmap bitmap) {
        return getBitmapString(SketchUtils.getByteCount(bitmap), bitmap.getConfig());
    }

    @Override // me.panpf.sketch.cache.recycle.LruPoolStrategy
    public void put(Bitmap bitmap) {
        Key key = this.keyPool.get(SketchUtils.getByteCount(bitmap), bitmap.getConfig());
        this.groupedMap.put(key, bitmap);
        NavigableMap<Integer, Integer> sizesForConfig = getSizesForConfig(bitmap.getConfig());
        Integer num = (Integer) sizesForConfig.get(Integer.valueOf(key.size));
        sizesForConfig.put(Integer.valueOf(key.size), Integer.valueOf(num != null ? 1 + num.intValue() : 1));
    }

    @Override // me.panpf.sketch.cache.recycle.LruPoolStrategy
    public Bitmap removeLast() {
        Bitmap removeLast = this.groupedMap.removeLast();
        if (removeLast != null) {
            decrementBitmapOfSize(Integer.valueOf(SketchUtils.getByteCount(removeLast)), removeLast.getConfig());
        }
        return removeLast;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SizeConfigStrategy{groupedMap=");
        sb.append(this.groupedMap);
        sb.append(", sortedSizes=(");
        for (Map.Entry<Bitmap.Config, NavigableMap<Integer, Integer>> entry : this.sortedSizes.entrySet()) {
            sb.append(entry.getKey());
            sb.append('[');
            sb.append(entry.getValue());
            sb.append("], ");
        }
        if (!this.sortedSizes.isEmpty()) {
            sb.replace(sb.length() - 2, sb.length(), "");
        }
        sb.append(")}");
        return sb.toString();
    }
}
