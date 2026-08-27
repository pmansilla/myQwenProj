package com.mob.tools.gui;

import com.mob.tools.MobLog;
import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: classes2.dex */
public class CachePool<K, V> {
    private int capacity;
    private OnRemoveListener<K, V> listener;
    private LinkedList<CachePool<K, V>.Node<K, V>> pool = new LinkedList<>();
    private int poolSize;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class Node<K, V> {
        private long cacheTime;
        public K key;
        private int size;
        public V value;

        private Node() {
        }
    }

    /* loaded from: classes2.dex */
    public interface OnRemoveListener<K, V> {
        void onRemove(K k, V v);
    }

    public CachePool(int i) {
        this.capacity = i;
    }

    public synchronized void clear() {
        if (this.pool != null && this.capacity > 0) {
            if (this.listener == null) {
                this.pool.clear();
            } else {
                while (this.pool.size() > 0) {
                    CachePool<K, V>.Node<K, V> removeLast = this.pool.removeLast();
                    if (removeLast != null) {
                        this.poolSize -= ((Node) removeLast).size;
                        if (this.listener != null) {
                            this.listener.onRemove(removeLast.key, removeLast.value);
                        }
                    }
                }
            }
            this.poolSize = 0;
        }
    }

    public synchronized V get(K k) {
        CachePool<K, V>.Node<K, V> node;
        if (this.pool != null && this.capacity > 0) {
            while (this.poolSize > this.capacity) {
                try {
                    CachePool<K, V>.Node<K, V> removeLast = this.pool.removeLast();
                    if (removeLast != null) {
                        this.poolSize -= ((Node) removeLast).size;
                        if (this.listener != null) {
                            this.listener.onRemove(removeLast.key, removeLast.value);
                        }
                    }
                } catch (Throwable th) {
                    MobLog.getInstance().w(th);
                }
            }
            Iterator<CachePool<K, V>.Node<K, V>> it = this.pool.iterator();
            while (true) {
                if (!it.hasNext()) {
                    node = null;
                    break;
                }
                node = it.next();
                if (node != null && ((k == null && node.key == null) || (k != null && k.equals(node.key)))) {
                    break;
                }
            }
            if (node != null) {
                this.pool.set(0, node);
                ((Node) node).cacheTime = System.currentTimeMillis();
                return node.value;
            }
        }
        return null;
    }

    public synchronized boolean put(K k, V v) {
        return put(k, v, 1);
    }

    public synchronized boolean put(K k, V v, int i) {
        if (this.pool != null && this.capacity > 0) {
            try {
                CachePool<K, V>.Node<K, V> node = new Node<>();
                node.key = k;
                node.value = v;
                ((Node) node).cacheTime = System.currentTimeMillis();
                ((Node) node).size = i;
                this.pool.add(0, node);
                this.poolSize += i;
                while (this.poolSize > this.capacity) {
                    CachePool<K, V>.Node<K, V> removeLast = this.pool.removeLast();
                    if (removeLast != null) {
                        this.poolSize -= ((Node) removeLast).size;
                        if (this.listener != null) {
                            this.listener.onRemove(removeLast.key, removeLast.value);
                        }
                    }
                }
                return true;
            } catch (Throwable th) {
                MobLog.getInstance().w(th);
            }
        }
        return false;
    }

    public void setOnRemoveListener(OnRemoveListener<K, V> onRemoveListener) {
        this.listener = onRemoveListener;
    }

    public synchronized int size() {
        return this.poolSize;
    }

    public synchronized void trimBeforeTime(long j) {
        if (this.pool != null && this.capacity > 0) {
            int size = this.pool.size() - 1;
            while (size >= 0) {
                if (((Node) this.pool.get(size)).cacheTime < j) {
                    CachePool<K, V>.Node<K, V> remove = this.pool.remove(size);
                    if (remove != null) {
                        this.poolSize -= ((Node) remove).size;
                        if (this.listener != null) {
                            this.listener.onRemove(remove.key, remove.value);
                        }
                    }
                } else {
                    size--;
                }
            }
            while (this.poolSize > this.capacity) {
                CachePool<K, V>.Node<K, V> removeLast = this.pool.removeLast();
                if (removeLast != null) {
                    this.poolSize -= ((Node) removeLast).size;
                    if (this.listener != null) {
                        this.listener.onRemove(removeLast.key, removeLast.value);
                    }
                }
            }
        }
    }
}
