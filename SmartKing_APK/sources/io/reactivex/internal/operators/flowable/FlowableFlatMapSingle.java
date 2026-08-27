package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/* loaded from: classes2.dex */
public final class FlowableFlatMapSingle<T, R> extends AbstractFlowableWithUpstream<T, R> {
    final boolean delayErrors;
    final Function<? super T, ? extends SingleSource<? extends R>> mapper;
    final int maxConcurrency;

    /* loaded from: classes2.dex */
    static final class FlatMapSingleSubscriber<T, R> extends AtomicInteger implements Subscriber<T>, Subscription {
        private static final long serialVersionUID = 8600231336733376951L;
        final Subscriber<? super R> actual;
        volatile boolean cancelled;
        final boolean delayErrors;
        final Function<? super T, ? extends SingleSource<? extends R>> mapper;
        final int maxConcurrency;
        Subscription s;
        final AtomicLong requested = new AtomicLong();
        final CompositeDisposable set = new CompositeDisposable();
        final AtomicThrowable errors = new AtomicThrowable();
        final AtomicInteger active = new AtomicInteger(1);
        final AtomicReference<SpscLinkedArrayQueue<R>> queue = new AtomicReference<>();

        /* loaded from: classes2.dex */
        final class InnerObserver extends AtomicReference<Disposable> implements SingleObserver<R>, Disposable {
            private static final long serialVersionUID = -502562646270949838L;

            InnerObserver() {
            }

            @Override // io.reactivex.disposables.Disposable
            public void dispose() {
                DisposableHelper.dispose(this);
            }

            @Override // io.reactivex.disposables.Disposable
            public boolean isDisposed() {
                return DisposableHelper.isDisposed(get());
            }

            @Override // io.reactivex.SingleObserver
            public void onError(Throwable th) {
                FlatMapSingleSubscriber.this.innerError(this, th);
            }

            @Override // io.reactivex.SingleObserver
            public void onSubscribe(Disposable disposable) {
                DisposableHelper.setOnce(this, disposable);
            }

            @Override // io.reactivex.SingleObserver
            public void onSuccess(R r) {
                FlatMapSingleSubscriber.this.innerSuccess(this, r);
            }
        }

        FlatMapSingleSubscriber(Subscriber<? super R> subscriber, Function<? super T, ? extends SingleSource<? extends R>> function, boolean z, int i) {
            this.actual = subscriber;
            this.mapper = function;
            this.delayErrors = z;
            this.maxConcurrency = i;
        }

        @Override // org.reactivestreams.Subscription
        public void cancel() {
            this.cancelled = true;
            this.s.cancel();
            this.set.dispose();
        }

        void clear() {
            SpscLinkedArrayQueue<R> spscLinkedArrayQueue = this.queue.get();
            if (spscLinkedArrayQueue != null) {
                spscLinkedArrayQueue.clear();
            }
        }

        void drain() {
            if (getAndIncrement() == 0) {
                drainLoop();
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:36:0x0075, code lost:
        
            if (r9 != r5) goto L65;
         */
        /* JADX WARN: Code restructure failed: missing block: B:38:0x0079, code lost:
        
            if (r15.cancelled == false) goto L43;
         */
        /* JADX WARN: Code restructure failed: missing block: B:40:0x0081, code lost:
        
            if (r15.delayErrors != false) goto L49;
         */
        /* JADX WARN: Code restructure failed: missing block: B:42:0x008b, code lost:
        
            if (r15.errors.get() == null) goto L49;
         */
        /* JADX WARN: Code restructure failed: missing block: B:44:0x008d, code lost:
        
            r1 = r15.errors.terminate();
            clear();
            r0.onError(r1);
         */
        /* JADX WARN: Code restructure failed: missing block: B:45:0x0099, code lost:
        
            return;
         */
        /* JADX WARN: Code restructure failed: missing block: B:47:0x009e, code lost:
        
            if (r1.get() != 0) goto L52;
         */
        /* JADX WARN: Code restructure failed: missing block: B:48:0x00a0, code lost:
        
            r5 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:49:0x00a3, code lost:
        
            r6 = r2.get();
         */
        /* JADX WARN: Code restructure failed: missing block: B:50:0x00a9, code lost:
        
            if (r6 == null) goto L57;
         */
        /* JADX WARN: Code restructure failed: missing block: B:52:0x00af, code lost:
        
            if (r6.isEmpty() == false) goto L58;
         */
        /* JADX WARN: Code restructure failed: missing block: B:53:0x00b2, code lost:
        
            if (r5 == false) goto L65;
         */
        /* JADX WARN: Code restructure failed: missing block: B:54:0x00b4, code lost:
        
            if (r11 == false) goto L65;
         */
        /* JADX WARN: Code restructure failed: missing block: B:56:0x00b6, code lost:
        
            r1 = r15.errors.terminate();
         */
        /* JADX WARN: Code restructure failed: missing block: B:57:0x00bc, code lost:
        
            if (r1 == null) goto L63;
         */
        /* JADX WARN: Code restructure failed: missing block: B:58:0x00be, code lost:
        
            r0.onError(r1);
         */
        /* JADX WARN: Code restructure failed: missing block: B:59:?, code lost:
        
            return;
         */
        /* JADX WARN: Code restructure failed: missing block: B:60:0x00c2, code lost:
        
            r0.onComplete();
         */
        /* JADX WARN: Code restructure failed: missing block: B:61:0x00c5, code lost:
        
            return;
         */
        /* JADX WARN: Code restructure failed: missing block: B:62:0x00b1, code lost:
        
            r11 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:63:0x00a2, code lost:
        
            r5 = false;
         */
        /* JADX WARN: Code restructure failed: missing block: B:65:0x007b, code lost:
        
            clear();
         */
        /* JADX WARN: Code restructure failed: missing block: B:66:0x007e, code lost:
        
            return;
         */
        /* JADX WARN: Code restructure failed: missing block: B:68:0x00c8, code lost:
        
            if (r9 == 0) goto L70;
         */
        /* JADX WARN: Code restructure failed: missing block: B:69:0x00ca, code lost:
        
            io.reactivex.internal.util.BackpressureHelper.produced(r15.requested, r9);
         */
        /* JADX WARN: Code restructure failed: missing block: B:70:0x00d4, code lost:
        
            if (r15.maxConcurrency == Integer.MAX_VALUE) goto L70;
         */
        /* JADX WARN: Code restructure failed: missing block: B:71:0x00d6, code lost:
        
            r15.s.request(r9);
         */
        /* JADX WARN: Code restructure failed: missing block: B:72:0x00db, code lost:
        
            r4 = addAndGet(-r4);
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        void drainLoop() {
            /*
                Method dump skipped, instructions count: 227
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.flowable.FlowableFlatMapSingle.FlatMapSingleSubscriber.drainLoop():void");
        }

        SpscLinkedArrayQueue<R> getOrCreateQueue() {
            SpscLinkedArrayQueue<R> spscLinkedArrayQueue;
            do {
                SpscLinkedArrayQueue<R> spscLinkedArrayQueue2 = this.queue.get();
                if (spscLinkedArrayQueue2 != null) {
                    return spscLinkedArrayQueue2;
                }
                spscLinkedArrayQueue = new SpscLinkedArrayQueue<>(Flowable.bufferSize());
            } while (!this.queue.compareAndSet(null, spscLinkedArrayQueue));
            return spscLinkedArrayQueue;
        }

        void innerError(FlatMapSingleSubscriber<T, R>.InnerObserver innerObserver, Throwable th) {
            this.set.delete(innerObserver);
            if (!this.errors.addThrowable(th)) {
                RxJavaPlugins.onError(th);
                return;
            }
            if (!this.delayErrors) {
                this.s.cancel();
                this.set.dispose();
            }
            this.active.decrementAndGet();
            drain();
        }

        void innerSuccess(FlatMapSingleSubscriber<T, R>.InnerObserver innerObserver, R r) {
            this.set.delete(innerObserver);
            if (get() == 0) {
                if (compareAndSet(0, 1)) {
                    boolean z = this.active.decrementAndGet() == 0;
                    if (this.requested.get() != 0) {
                        this.actual.onNext(r);
                        SpscLinkedArrayQueue<R> spscLinkedArrayQueue = this.queue.get();
                        if (z && (spscLinkedArrayQueue == null || spscLinkedArrayQueue.isEmpty())) {
                            Throwable terminate = this.errors.terminate();
                            if (terminate != null) {
                                this.actual.onError(terminate);
                                return;
                            } else {
                                this.actual.onComplete();
                                return;
                            }
                        }
                        BackpressureHelper.produced(this.requested, 1L);
                        if (this.maxConcurrency != Integer.MAX_VALUE) {
                            this.s.request(1L);
                        }
                    } else {
                        SpscLinkedArrayQueue<R> orCreateQueue = getOrCreateQueue();
                        synchronized (orCreateQueue) {
                            orCreateQueue.offer(r);
                        }
                    }
                    if (decrementAndGet() == 0) {
                        return;
                    }
                    drainLoop();
                }
            }
            SpscLinkedArrayQueue<R> orCreateQueue2 = getOrCreateQueue();
            synchronized (orCreateQueue2) {
                orCreateQueue2.offer(r);
            }
            this.active.decrementAndGet();
            if (getAndIncrement() != 0) {
                return;
            }
            drainLoop();
        }

        @Override // org.reactivestreams.Subscriber
        public void onComplete() {
            this.active.decrementAndGet();
            drain();
        }

        @Override // org.reactivestreams.Subscriber
        public void onError(Throwable th) {
            this.active.decrementAndGet();
            if (!this.errors.addThrowable(th)) {
                RxJavaPlugins.onError(th);
                return;
            }
            if (!this.delayErrors) {
                this.set.dispose();
            }
            drain();
        }

        @Override // org.reactivestreams.Subscriber
        public void onNext(T t) {
            try {
                SingleSource singleSource = (SingleSource) ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null SingleSource");
                this.active.getAndIncrement();
                InnerObserver innerObserver = new InnerObserver();
                if (this.set.add(innerObserver)) {
                    singleSource.subscribe(innerObserver);
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.s.cancel();
                onError(th);
            }
        }

        @Override // org.reactivestreams.Subscriber
        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
                if (this.maxConcurrency == Integer.MAX_VALUE) {
                    subscription.request(LongCompanionObject.MAX_VALUE);
                } else {
                    subscription.request(this.maxConcurrency);
                }
            }
        }

        @Override // org.reactivestreams.Subscription
        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
                drain();
            }
        }
    }

    public FlowableFlatMapSingle(Publisher<T> publisher, Function<? super T, ? extends SingleSource<? extends R>> function, boolean z, int i) {
        super(publisher);
        this.mapper = function;
        this.delayErrors = z;
        this.maxConcurrency = i;
    }

    @Override // io.reactivex.Flowable
    protected void subscribeActual(Subscriber<? super R> subscriber) {
        this.source.subscribe(new FlatMapSingleSubscriber(subscriber, this.mapper, this.delayErrors, this.maxConcurrency));
    }
}
