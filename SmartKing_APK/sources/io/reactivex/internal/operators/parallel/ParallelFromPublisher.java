package io.reactivex.internal.operators.parallel;

import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.parallel.ParallelFlowable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLongArray;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/* loaded from: classes2.dex */
public final class ParallelFromPublisher<T> extends ParallelFlowable<T> {
    final int parallelism;
    final int prefetch;
    final Publisher<? extends T> source;

    /* loaded from: classes2.dex */
    static final class ParallelDispatcher<T> extends AtomicInteger implements Subscriber<T> {
        private static final long serialVersionUID = -4470634016609963609L;
        volatile boolean cancelled;
        volatile boolean done;
        final long[] emissions;
        Throwable error;
        int index;
        final int limit;
        final int prefetch;
        int produced;
        SimpleQueue<T> queue;
        final AtomicLongArray requests;
        Subscription s;
        int sourceMode;
        final AtomicInteger subscriberCount = new AtomicInteger();
        final Subscriber<? super T>[] subscribers;

        ParallelDispatcher(Subscriber<? super T>[] subscriberArr, int i) {
            this.subscribers = subscriberArr;
            this.prefetch = i;
            this.limit = i - (i >> 2);
            this.requests = new AtomicLongArray(subscriberArr.length);
            this.emissions = new long[subscriberArr.length];
        }

        void cancel() {
            if (this.cancelled) {
                return;
            }
            this.cancelled = true;
            this.s.cancel();
            if (getAndIncrement() == 0) {
                this.queue.clear();
            }
        }

        void drain() {
            if (getAndIncrement() != 0) {
                return;
            }
            if (this.sourceMode == 1) {
                drainSync();
            } else {
                drainAsync();
            }
        }

        void drainAsync() {
            Throwable th;
            SimpleQueue<T> simpleQueue = this.queue;
            Subscriber<? super T>[] subscriberArr = this.subscribers;
            AtomicLongArray atomicLongArray = this.requests;
            long[] jArr = this.emissions;
            int length = jArr.length;
            int i = this.index;
            int i2 = this.produced;
            int i3 = i;
            int i4 = 1;
            while (true) {
                int i5 = 0;
                int i6 = i2;
                int i7 = 0;
                while (!this.cancelled) {
                    boolean z = this.done;
                    if (z && (th = this.error) != null) {
                        simpleQueue.clear();
                        int length2 = subscriberArr.length;
                        while (i5 < length2) {
                            subscriberArr[i5].onError(th);
                            i5++;
                        }
                        return;
                    }
                    boolean isEmpty = simpleQueue.isEmpty();
                    if (z && isEmpty) {
                        int length3 = subscriberArr.length;
                        while (i5 < length3) {
                            subscriberArr[i5].onComplete();
                            i5++;
                        }
                        return;
                    }
                    if (!isEmpty) {
                        long j = atomicLongArray.get(i3);
                        long j2 = jArr[i3];
                        if (j != j2) {
                            try {
                                T poll = simpleQueue.poll();
                                if (poll != null) {
                                    subscriberArr[i3].onNext(poll);
                                    jArr[i3] = j2 + 1;
                                    int i8 = i6 + 1;
                                    if (i8 == this.limit) {
                                        this.s.request(i8);
                                        i8 = 0;
                                    }
                                    i6 = i8;
                                    i7 = 0;
                                }
                            } catch (Throwable th2) {
                                Exceptions.throwIfFatal(th2);
                                this.s.cancel();
                                int length4 = subscriberArr.length;
                                while (i5 < length4) {
                                    subscriberArr[i5].onError(th2);
                                    i5++;
                                }
                                return;
                            }
                        } else {
                            i7++;
                        }
                        i3++;
                        if (i3 == length) {
                            i3 = 0;
                        }
                        if (i7 == length) {
                        }
                    }
                    i2 = i6;
                    int i9 = get();
                    if (i9 == i4) {
                        this.index = i3;
                        this.produced = i2;
                        i4 = addAndGet(-i4);
                        if (i4 == 0) {
                            return;
                        }
                    } else {
                        i4 = i9;
                    }
                }
                simpleQueue.clear();
                return;
            }
        }

        void drainSync() {
            SimpleQueue<T> simpleQueue = this.queue;
            Subscriber<? super T>[] subscriberArr = this.subscribers;
            AtomicLongArray atomicLongArray = this.requests;
            long[] jArr = this.emissions;
            int length = jArr.length;
            int i = this.index;
            int i2 = 1;
            while (true) {
                int i3 = 0;
                int i4 = 0;
                while (!this.cancelled) {
                    if (simpleQueue.isEmpty()) {
                        int length2 = subscriberArr.length;
                        while (i3 < length2) {
                            subscriberArr[i3].onComplete();
                            i3++;
                        }
                        return;
                    }
                    long j = atomicLongArray.get(i);
                    long j2 = jArr[i];
                    if (j != j2) {
                        try {
                            T poll = simpleQueue.poll();
                            if (poll == null) {
                                int length3 = subscriberArr.length;
                                while (i3 < length3) {
                                    subscriberArr[i3].onComplete();
                                    i3++;
                                }
                                return;
                            }
                            subscriberArr[i].onNext(poll);
                            jArr[i] = j2 + 1;
                            i4 = 0;
                        } catch (Throwable th) {
                            Exceptions.throwIfFatal(th);
                            this.s.cancel();
                            int length4 = subscriberArr.length;
                            while (i3 < length4) {
                                subscriberArr[i3].onError(th);
                                i3++;
                            }
                            return;
                        }
                    } else {
                        i4++;
                    }
                    i++;
                    if (i == length) {
                        i = 0;
                    }
                    if (i4 == length) {
                        int i5 = get();
                        if (i5 == i2) {
                            this.index = i;
                            i2 = addAndGet(-i2);
                            if (i2 == 0) {
                                return;
                            }
                        } else {
                            i2 = i5;
                        }
                    }
                }
                simpleQueue.clear();
                return;
            }
        }

        @Override // org.reactivestreams.Subscriber
        public void onComplete() {
            this.done = true;
            drain();
        }

        @Override // org.reactivestreams.Subscriber
        public void onError(Throwable th) {
            this.error = th;
            this.done = true;
            drain();
        }

        @Override // org.reactivestreams.Subscriber
        public void onNext(T t) {
            if (this.sourceMode != 0 || this.queue.offer(t)) {
                drain();
            } else {
                this.s.cancel();
                onError(new MissingBackpressureException("Queue is full?"));
            }
        }

        @Override // org.reactivestreams.Subscriber
        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                if (subscription instanceof QueueSubscription) {
                    QueueSubscription queueSubscription = (QueueSubscription) subscription;
                    int requestFusion = queueSubscription.requestFusion(3);
                    if (requestFusion == 1) {
                        this.sourceMode = requestFusion;
                        this.queue = queueSubscription;
                        this.done = true;
                        setupSubscribers();
                        drain();
                        return;
                    }
                    if (requestFusion == 2) {
                        this.sourceMode = requestFusion;
                        this.queue = queueSubscription;
                        setupSubscribers();
                        subscription.request(this.prefetch);
                        return;
                    }
                }
                this.queue = new SpscArrayQueue(this.prefetch);
                setupSubscribers();
                subscription.request(this.prefetch);
            }
        }

        void setupSubscribers() {
            final int length = this.subscribers.length;
            final int i = 0;
            while (i < length && !this.cancelled) {
                int i2 = i + 1;
                this.subscriberCount.lazySet(i2);
                this.subscribers[i].onSubscribe(new Subscription() { // from class: io.reactivex.internal.operators.parallel.ParallelFromPublisher.ParallelDispatcher.1
                    @Override // org.reactivestreams.Subscription
                    public void cancel() {
                        ParallelDispatcher.this.cancel();
                    }

                    @Override // org.reactivestreams.Subscription
                    public void request(long j) {
                        long j2;
                        if (SubscriptionHelper.validate(j)) {
                            AtomicLongArray atomicLongArray = ParallelDispatcher.this.requests;
                            do {
                                j2 = atomicLongArray.get(i);
                                if (j2 == LongCompanionObject.MAX_VALUE) {
                                    return;
                                }
                            } while (!atomicLongArray.compareAndSet(i, j2, BackpressureHelper.addCap(j2, j)));
                            if (ParallelDispatcher.this.subscriberCount.get() == length) {
                                ParallelDispatcher.this.drain();
                            }
                        }
                    }
                });
                i = i2;
            }
        }
    }

    public ParallelFromPublisher(Publisher<? extends T> publisher, int i, int i2) {
        this.source = publisher;
        this.parallelism = i;
        this.prefetch = i2;
    }

    @Override // io.reactivex.parallel.ParallelFlowable
    public int parallelism() {
        return this.parallelism;
    }

    @Override // io.reactivex.parallel.ParallelFlowable
    public void subscribe(Subscriber<? super T>[] subscriberArr) {
        if (validate(subscriberArr)) {
            this.source.subscribe(new ParallelDispatcher(subscriberArr, this.prefetch));
        }
    }
}
