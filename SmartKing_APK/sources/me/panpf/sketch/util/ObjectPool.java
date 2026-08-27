package me.panpf.sketch.util;

import java.util.LinkedList;
import java.util.Queue;

/* loaded from: classes2.dex */
public class ObjectPool<T> {
    private static final int MAX_POOL_SIZE = 10;
    private Queue<T> cacheQueue;
    private final Object editLock;
    private int maxPoolSize;
    private ObjectFactory<T> objectFactory;

    /* loaded from: classes2.dex */
    public interface CacheStatus {
        boolean isInCachePool();

        void setInCachePool(boolean z);
    }

    /* loaded from: classes2.dex */
    public interface ObjectFactory<T> {
        T newObject();
    }

    public ObjectPool(Class<T> cls) {
        this(cls, 10);
    }

    public ObjectPool(final Class<T> cls, int i) {
        this(new ObjectFactory<T>() { // from class: me.panpf.sketch.util.ObjectPool.1
            @Override // me.panpf.sketch.util.ObjectPool.ObjectFactory
            public T newObject() {
                try {
                    return (T) cls.newInstance();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return null;
                } catch (InstantiationException e2) {
                    e2.printStackTrace();
                    return null;
                }
            }
        }, i);
    }

    public ObjectPool(ObjectFactory<T> objectFactory) {
        this(objectFactory, 10);
    }

    public ObjectPool(ObjectFactory<T> objectFactory, int i) {
        this.editLock = new Object();
        this.objectFactory = objectFactory;
        this.maxPoolSize = i;
        this.cacheQueue = new LinkedList();
    }

    public void clear() {
        synchronized (this.editLock) {
            this.cacheQueue.clear();
        }
    }

    public T get() {
        T poll;
        synchronized (this.editLock) {
            poll = !this.cacheQueue.isEmpty() ? this.cacheQueue.poll() : this.objectFactory.newObject();
            if (poll instanceof CacheStatus) {
                poll.setInCachePool(false);
            }
        }
        return (T) poll;
    }

    public int getMaxPoolSize() {
        return this.maxPoolSize;
    }

    public void put(T t) {
        synchronized (this.editLock) {
            if (this.cacheQueue.size() < this.maxPoolSize) {
                if (t instanceof CacheStatus) {
                    ((CacheStatus) t).setInCachePool(true);
                }
                this.cacheQueue.add(t);
            }
        }
    }

    public void setMaxPoolSize(int i) {
        this.maxPoolSize = i;
        synchronized (this.editLock) {
            if (this.cacheQueue.size() > i) {
                int size = i - this.cacheQueue.size();
                int i2 = 0;
                while (true) {
                    int i3 = i2 + 1;
                    if (i2 >= size) {
                        break;
                    }
                    this.cacheQueue.poll();
                    i2 = i3;
                }
            }
        }
    }

    public int size() {
        int size;
        synchronized (this.editLock) {
            size = this.cacheQueue.size();
        }
        return size;
    }
}
