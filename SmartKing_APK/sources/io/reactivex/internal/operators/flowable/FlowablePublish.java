package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.fuseable.HasUpstreamPublisher;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/* loaded from: classes2.dex */
public final class FlowablePublish<T> extends ConnectableFlowable<T> implements HasUpstreamPublisher<T> {
    static final long CANCELLED = Long.MIN_VALUE;
    final int bufferSize;
    final AtomicReference<PublishSubscriber<T>> current;
    final Publisher<T> onSubscribe;
    final Publisher<T> source;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static final class InnerSubscriber<T> extends AtomicLong implements Subscription {
        private static final long serialVersionUID = -4453897557930727610L;
        final Subscriber<? super T> child;
        volatile PublishSubscriber<T> parent;

        InnerSubscriber(Subscriber<? super T> subscriber) {
            this.child = subscriber;
        }

        @Override // org.reactivestreams.Subscription
        public void cancel() {
            PublishSubscriber<T> publishSubscriber;
            if (get() == Long.MIN_VALUE || getAndSet(Long.MIN_VALUE) == Long.MIN_VALUE || (publishSubscriber = this.parent) == null) {
                return;
            }
            publishSubscriber.remove(this);
            publishSubscriber.dispatch();
        }

        public long produced(long j) {
            return BackpressureHelper.producedCancel(this, j);
        }

        @Override // org.reactivestreams.Subscription
        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.addCancel(this, j);
                PublishSubscriber<T> publishSubscriber = this.parent;
                if (publishSubscriber != null) {
                    publishSubscriber.dispatch();
                }
            }
        }
    }

    /* loaded from: classes2.dex */
    static final class PublishSubscriber<T> extends AtomicInteger implements Subscriber<T>, Disposable {
        static final InnerSubscriber[] EMPTY = new InnerSubscriber[0];
        static final InnerSubscriber[] TERMINATED = new InnerSubscriber[0];
        private static final long serialVersionUID = -202316842419149694L;
        final int bufferSize;
        final AtomicReference<PublishSubscriber<T>> current;
        volatile SimpleQueue<T> queue;
        int sourceMode;
        volatile Object terminalEvent;
        final AtomicReference<Subscription> s = new AtomicReference<>();
        final AtomicReference<InnerSubscriber[]> subscribers = new AtomicReference<>(EMPTY);
        final AtomicBoolean shouldConnect = new AtomicBoolean();

        PublishSubscriber(AtomicReference<PublishSubscriber<T>> atomicReference, int i) {
            this.current = atomicReference;
            this.bufferSize = i;
        }

        boolean add(InnerSubscriber<T> innerSubscriber) {
            InnerSubscriber[] innerSubscriberArr;
            InnerSubscriber[] innerSubscriberArr2;
            do {
                innerSubscriberArr = this.subscribers.get();
                if (innerSubscriberArr == TERMINATED) {
                    return false;
                }
                int length = innerSubscriberArr.length;
                innerSubscriberArr2 = new InnerSubscriber[length + 1];
                System.arraycopy(innerSubscriberArr, 0, innerSubscriberArr2, 0, length);
                innerSubscriberArr2[length] = innerSubscriber;
            } while (!this.subscribers.compareAndSet(innerSubscriberArr, innerSubscriberArr2));
            return true;
        }

        boolean checkTerminated(Object obj, boolean z) {
            int i = 0;
            if (obj != null) {
                if (!NotificationLite.isComplete(obj)) {
                    Throwable error = NotificationLite.getError(obj);
                    this.current.compareAndSet(this, null);
                    InnerSubscriber[] andSet = this.subscribers.getAndSet(TERMINATED);
                    if (andSet.length != 0) {
                        int length = andSet.length;
                        while (i < length) {
                            andSet[i].child.onError(error);
                            i++;
                        }
                    } else {
                        RxJavaPlugins.onError(error);
                    }
                    return true;
                }
                if (z) {
                    this.current.compareAndSet(this, null);
                    InnerSubscriber[] andSet2 = this.subscribers.getAndSet(TERMINATED);
                    int length2 = andSet2.length;
                    while (i < length2) {
                        andSet2[i].child.onComplete();
                        i++;
                    }
                    return true;
                }
            }
            return false;
        }

        void dispatch() {
            int i;
            int i2;
            long j;
            boolean z;
            T t;
            T t2;
            if (getAndIncrement() != 0) {
                return;
            }
            int i3 = 1;
            int i4 = 1;
            while (true) {
                Object obj = this.terminalEvent;
                SimpleQueue<T> simpleQueue = this.queue;
                boolean z2 = simpleQueue == null || simpleQueue.isEmpty();
                if (checkTerminated(obj, z2)) {
                    return;
                }
                if (z2) {
                    i = i4;
                } else {
                    InnerSubscriber[] innerSubscriberArr = this.subscribers.get();
                    int length = innerSubscriberArr.length;
                    int length2 = innerSubscriberArr.length;
                    long j2 = Long.MAX_VALUE;
                    int i5 = 0;
                    int i6 = 0;
                    while (i5 < length2) {
                        boolean z3 = z2;
                        long j3 = innerSubscriberArr[i5].get();
                        if (j3 >= 0) {
                            j2 = Math.min(j2, j3);
                        } else if (j3 == Long.MIN_VALUE) {
                            i6++;
                        }
                        i5++;
                        z2 = z3;
                    }
                    boolean z4 = z2;
                    if (length == i6) {
                        Object obj2 = this.terminalEvent;
                        try {
                            t2 = simpleQueue.poll();
                        } catch (Throwable th) {
                            Exceptions.throwIfFatal(th);
                            this.s.get().cancel();
                            obj2 = NotificationLite.error(th);
                            this.terminalEvent = obj2;
                            t2 = null;
                        }
                        if (checkTerminated(obj2, t2 == null)) {
                            return;
                        }
                        if (this.sourceMode != i3) {
                            this.s.get().request(1L);
                        }
                        i2 = i4;
                    } else {
                        i2 = i4;
                        int i7 = 0;
                        while (true) {
                            j = i7;
                            if (j >= j2) {
                                z = z4;
                                break;
                            }
                            Object obj3 = this.terminalEvent;
                            try {
                                t = simpleQueue.poll();
                            } catch (Throwable th2) {
                                Exceptions.throwIfFatal(th2);
                                this.s.get().cancel();
                                obj3 = NotificationLite.error(th2);
                                this.terminalEvent = obj3;
                                t = null;
                            }
                            z = t == null;
                            if (checkTerminated(obj3, z)) {
                                return;
                            }
                            if (z) {
                                break;
                            }
                            Object value = NotificationLite.getValue(t);
                            for (InnerSubscriber innerSubscriber : innerSubscriberArr) {
                                if (innerSubscriber.get() > 0) {
                                    innerSubscriber.child.onNext(value);
                                    innerSubscriber.produced(1L);
                                }
                            }
                            i7++;
                            z4 = z;
                        }
                        if (i7 > 0 && this.sourceMode != 1) {
                            this.s.get().request(j);
                        }
                        if (j2 == 0 || z) {
                            i = i2;
                        }
                    }
                    i4 = i2;
                    i3 = 1;
                }
                i4 = addAndGet(-i);
                if (i4 == 0) {
                    return;
                } else {
                    i3 = 1;
                }
            }
        }

        @Override // io.reactivex.disposables.Disposable
        public void dispose() {
            if (this.subscribers.get() == TERMINATED || this.subscribers.getAndSet(TERMINATED) == TERMINATED) {
                return;
            }
            this.current.compareAndSet(this, null);
            SubscriptionHelper.cancel(this.s);
        }

        @Override // io.reactivex.disposables.Disposable
        public boolean isDisposed() {
            return this.subscribers.get() == TERMINATED;
        }

        @Override // org.reactivestreams.Subscriber
        public void onComplete() {
            if (this.terminalEvent == null) {
                this.terminalEvent = NotificationLite.complete();
                dispatch();
            }
        }

        @Override // org.reactivestreams.Subscriber
        public void onError(Throwable th) {
            if (this.terminalEvent != null) {
                RxJavaPlugins.onError(th);
            } else {
                this.terminalEvent = NotificationLite.error(th);
                dispatch();
            }
        }

        @Override // org.reactivestreams.Subscriber
        public void onNext(T t) {
            if (this.sourceMode != 0 || this.queue.offer(t)) {
                dispatch();
            } else {
                onError(new MissingBackpressureException("Prefetch queue is full?!"));
            }
        }

        @Override // org.reactivestreams.Subscriber
        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.setOnce(this.s, subscription)) {
                if (subscription instanceof QueueSubscription) {
                    QueueSubscription queueSubscription = (QueueSubscription) subscription;
                    int requestFusion = queueSubscription.requestFusion(3);
                    if (requestFusion == 1) {
                        this.sourceMode = requestFusion;
                        this.queue = queueSubscription;
                        this.terminalEvent = NotificationLite.complete();
                        dispatch();
                        return;
                    }
                    if (requestFusion == 2) {
                        this.sourceMode = requestFusion;
                        this.queue = queueSubscription;
                        subscription.request(this.bufferSize);
                        return;
                    }
                }
                this.queue = new SpscArrayQueue(this.bufferSize);
                subscription.request(this.bufferSize);
            }
        }

        void remove(InnerSubscriber<T> innerSubscriber) {
            InnerSubscriber[] innerSubscriberArr;
            InnerSubscriber[] innerSubscriberArr2;
            do {
                innerSubscriberArr = this.subscribers.get();
                int length = innerSubscriberArr.length;
                if (length == 0) {
                    return;
                }
                int i = -1;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    }
                    if (innerSubscriberArr[i2].equals(innerSubscriber)) {
                        i = i2;
                        break;
                    }
                    i2++;
                }
                if (i < 0) {
                    return;
                }
                if (length == 1) {
                    innerSubscriberArr2 = EMPTY;
                } else {
                    InnerSubscriber[] innerSubscriberArr3 = new InnerSubscriber[length - 1];
                    System.arraycopy(innerSubscriberArr, 0, innerSubscriberArr3, 0, i);
                    System.arraycopy(innerSubscriberArr, i + 1, innerSubscriberArr3, i, (length - i) - 1);
                    innerSubscriberArr2 = innerSubscriberArr3;
                }
            } while (!this.subscribers.compareAndSet(innerSubscriberArr, innerSubscriberArr2));
        }
    }

    private FlowablePublish(Publisher<T> publisher, Publisher<T> publisher2, AtomicReference<PublishSubscriber<T>> atomicReference, int i) {
        this.onSubscribe = publisher;
        this.source = publisher2;
        this.current = atomicReference;
        this.bufferSize = i;
    }

    public static <T> ConnectableFlowable<T> create(Flowable<T> flowable, final int i) {
        final AtomicReference atomicReference = new AtomicReference();
        return RxJavaPlugins.onAssembly((ConnectableFlowable) new FlowablePublish(new Publisher<T>() { // from class: io.reactivex.internal.operators.flowable.FlowablePublish.1
            @Override // org.reactivestreams.Publisher
            public void subscribe(Subscriber<? super T> subscriber) {
                PublishSubscriber<T> publishSubscriber;
                InnerSubscriber<T> innerSubscriber = new InnerSubscriber<>(subscriber);
                subscriber.onSubscribe(innerSubscriber);
                while (true) {
                    publishSubscriber = (PublishSubscriber) atomicReference.get();
                    if (publishSubscriber == null || publishSubscriber.isDisposed()) {
                        PublishSubscriber<T> publishSubscriber2 = new PublishSubscriber<>(atomicReference, i);
                        if (atomicReference.compareAndSet(publishSubscriber, publishSubscriber2)) {
                            publishSubscriber = publishSubscriber2;
                        } else {
                            continue;
                        }
                    }
                    if (publishSubscriber.add(innerSubscriber)) {
                        break;
                    }
                }
                if (innerSubscriber.get() == Long.MIN_VALUE) {
                    publishSubscriber.remove(innerSubscriber);
                } else {
                    innerSubscriber.parent = publishSubscriber;
                }
                publishSubscriber.dispatch();
            }
        }, flowable, atomicReference, i));
    }

    @Override // io.reactivex.flowables.ConnectableFlowable
    public void connect(Consumer<? super Disposable> consumer) {
        PublishSubscriber<T> publishSubscriber;
        while (true) {
            publishSubscriber = this.current.get();
            if (publishSubscriber != null && !publishSubscriber.isDisposed()) {
                break;
            }
            PublishSubscriber<T> publishSubscriber2 = new PublishSubscriber<>(this.current, this.bufferSize);
            if (this.current.compareAndSet(publishSubscriber, publishSubscriber2)) {
                publishSubscriber = publishSubscriber2;
                break;
            }
        }
        boolean z = !publishSubscriber.shouldConnect.get() && publishSubscriber.shouldConnect.compareAndSet(false, true);
        try {
            consumer.accept(publishSubscriber);
            if (z) {
                this.source.subscribe(publishSubscriber);
            }
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    @Override // io.reactivex.internal.fuseable.HasUpstreamPublisher
    public Publisher<T> source() {
        return this.source;
    }

    @Override // io.reactivex.Flowable
    protected void subscribeActual(Subscriber<? super T> subscriber) {
        this.onSubscribe.subscribe(subscriber);
    }
}
