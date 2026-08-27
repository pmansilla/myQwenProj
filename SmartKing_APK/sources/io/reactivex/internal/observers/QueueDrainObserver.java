package io.reactivex.internal.observers;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.util.ObservableQueueDrain;
import io.reactivex.internal.util.QueueDrainHelper;

/* loaded from: classes2.dex */
public abstract class QueueDrainObserver<T, U, V> extends QueueDrainSubscriberPad2 implements Observer<T>, ObservableQueueDrain<U, V> {
    protected final Observer<? super V> actual;
    protected volatile boolean cancelled;
    protected volatile boolean done;
    protected Throwable error;
    protected final SimpleQueue<U> queue;

    public QueueDrainObserver(Observer<? super V> observer, SimpleQueue<U> simpleQueue) {
        this.actual = observer;
        this.queue = simpleQueue;
    }

    @Override // io.reactivex.internal.util.ObservableQueueDrain
    public void accept(Observer<? super V> observer, U u) {
    }

    @Override // io.reactivex.internal.util.ObservableQueueDrain
    public final boolean cancelled() {
        return this.cancelled;
    }

    @Override // io.reactivex.internal.util.ObservableQueueDrain
    public final boolean done() {
        return this.done;
    }

    public void drain(boolean z, Disposable disposable) {
        if (enter()) {
            QueueDrainHelper.drainLoop(this.queue, this.actual, z, disposable, this);
        }
    }

    @Override // io.reactivex.internal.util.ObservableQueueDrain
    public final boolean enter() {
        return this.wip.getAndIncrement() == 0;
    }

    @Override // io.reactivex.internal.util.ObservableQueueDrain
    public final Throwable error() {
        return this.error;
    }

    public final boolean fastEnter() {
        return this.wip.get() == 0 && this.wip.compareAndSet(0, 1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void fastPathEmit(U u, boolean z, Disposable disposable) {
        Observer<? super V> observer = this.actual;
        SimpleQueue<U> simpleQueue = this.queue;
        if (this.wip.get() == 0 && this.wip.compareAndSet(0, 1)) {
            accept(observer, u);
            if (leave(-1) == 0) {
                return;
            }
        } else {
            simpleQueue.offer(u);
            if (!enter()) {
                return;
            }
        }
        QueueDrainHelper.drainLoop(simpleQueue, observer, z, disposable, this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void fastPathOrderedEmit(U u, boolean z, Disposable disposable) {
        Observer<? super V> observer = this.actual;
        SimpleQueue<U> simpleQueue = this.queue;
        if (this.wip.get() != 0 || !this.wip.compareAndSet(0, 1)) {
            simpleQueue.offer(u);
            if (!enter()) {
                return;
            }
        } else if (simpleQueue.isEmpty()) {
            accept(observer, u);
            if (leave(-1) == 0) {
                return;
            }
        } else {
            simpleQueue.offer(u);
        }
        QueueDrainHelper.drainLoop(simpleQueue, observer, z, disposable, this);
    }

    @Override // io.reactivex.internal.util.ObservableQueueDrain
    public final int leave(int i) {
        return this.wip.addAndGet(i);
    }
}
