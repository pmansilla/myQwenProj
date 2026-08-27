package me.panpf.sketch.cache.recycle;

import java.util.ArrayDeque;
import java.util.Queue;
import me.panpf.sketch.cache.recycle.Poolable;

/* loaded from: classes2.dex */
abstract class BaseKeyPool<T extends Poolable> {
    private static final int MAX_SIZE = 20;
    private final Queue<T> keyPool = new ArrayDeque(20);

    protected abstract T create();

    /* JADX INFO: Access modifiers changed from: protected */
    public T get() {
        T poll = this.keyPool.poll();
        return poll == null ? create() : poll;
    }

    public void offer(T t) {
        if (this.keyPool.size() < 20) {
            this.keyPool.offer(t);
        }
    }
}
