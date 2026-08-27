package com.autonavi.ae.gmap.maploader;

/* loaded from: classes.dex */
public final class Pools {

    /* loaded from: classes.dex */
    public interface Pool<T> {
        T acquire();

        void destory();

        boolean release(T t);
    }

    /* loaded from: classes.dex */
    public static class SimplePool<T> implements Pool<T> {
        private final Object[] mPool;
        private int mPoolSize;

        public SimplePool(int i) {
            if (i <= 0) {
                throw new IllegalArgumentException("The max pool size must be > 0");
            }
            this.mPool = new Object[i];
        }

        private boolean isInPool(T t) {
            for (int i = 0; i < this.mPoolSize; i++) {
                if (this.mPool[i] == t) {
                    return true;
                }
            }
            return false;
        }

        @Override // com.autonavi.ae.gmap.maploader.Pools.Pool
        public T acquire() {
            if (this.mPoolSize <= 0) {
                return null;
            }
            int i = this.mPoolSize - 1;
            T t = (T) this.mPool[i];
            this.mPool[i] = null;
            this.mPoolSize--;
            return t;
        }

        @Override // com.autonavi.ae.gmap.maploader.Pools.Pool
        public void destory() {
            for (int i = 0; i < this.mPool.length; i++) {
                this.mPool[i] = null;
            }
        }

        @Override // com.autonavi.ae.gmap.maploader.Pools.Pool
        public boolean release(T t) {
            if (isInPool(t)) {
                throw new IllegalStateException("Already in the pool!");
            }
            if (this.mPoolSize >= this.mPool.length) {
                return false;
            }
            this.mPool[this.mPoolSize] = t;
            this.mPoolSize++;
            return true;
        }
    }

    /* loaded from: classes.dex */
    public static class SynchronizedPool<T> extends SimplePool<T> {
        private final Object mLock;

        public SynchronizedPool(int i) {
            super(i);
            this.mLock = new Object();
        }

        @Override // com.autonavi.ae.gmap.maploader.Pools.SimplePool, com.autonavi.ae.gmap.maploader.Pools.Pool
        public T acquire() {
            T t;
            synchronized (this.mLock) {
                t = (T) super.acquire();
            }
            return t;
        }

        @Override // com.autonavi.ae.gmap.maploader.Pools.SimplePool, com.autonavi.ae.gmap.maploader.Pools.Pool
        public boolean release(T t) {
            boolean release;
            synchronized (this.mLock) {
                release = super.release(t);
            }
            return release;
        }
    }

    private Pools() {
    }
}
