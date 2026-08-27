package io.reactivex.internal.operators.flowable;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/* loaded from: classes2.dex */
public final class FlowableRefCount<T> extends AbstractFlowableWithUpstream<T, T> {
    volatile CompositeDisposable baseDisposable;
    final ReentrantLock lock;
    final ConnectableFlowable<? extends T> source;
    final AtomicInteger subscriptionCount;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public final class ConnectionSubscriber extends AtomicReference<Subscription> implements Subscriber<T>, Subscription {
        private static final long serialVersionUID = 152064694420235350L;
        final CompositeDisposable currentBase;
        final AtomicLong requested = new AtomicLong();
        final Disposable resource;
        final Subscriber<? super T> subscriber;

        ConnectionSubscriber(Subscriber<? super T> subscriber, CompositeDisposable compositeDisposable, Disposable disposable) {
            this.subscriber = subscriber;
            this.currentBase = compositeDisposable;
            this.resource = disposable;
        }

        @Override // org.reactivestreams.Subscription
        public void cancel() {
            SubscriptionHelper.cancel(this);
            this.resource.dispose();
        }

        void cleanup() {
            FlowableRefCount.this.lock.lock();
            try {
                if (FlowableRefCount.this.baseDisposable == this.currentBase) {
                    FlowableRefCount.this.baseDisposable.dispose();
                    FlowableRefCount.this.baseDisposable = new CompositeDisposable();
                    FlowableRefCount.this.subscriptionCount.set(0);
                }
            } finally {
                FlowableRefCount.this.lock.unlock();
            }
        }

        @Override // org.reactivestreams.Subscriber
        public void onComplete() {
            cleanup();
            this.subscriber.onComplete();
        }

        @Override // org.reactivestreams.Subscriber
        public void onError(Throwable th) {
            cleanup();
            this.subscriber.onError(th);
        }

        @Override // org.reactivestreams.Subscriber
        public void onNext(T t) {
            this.subscriber.onNext(t);
        }

        @Override // org.reactivestreams.Subscriber
        public void onSubscribe(Subscription subscription) {
            SubscriptionHelper.deferredSetOnce(this, this.requested, subscription);
        }

        @Override // org.reactivestreams.Subscription
        public void request(long j) {
            SubscriptionHelper.deferredRequest(this, this.requested, j);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public FlowableRefCount(ConnectableFlowable<T> connectableFlowable) {
        super(connectableFlowable);
        this.baseDisposable = new CompositeDisposable();
        this.subscriptionCount = new AtomicInteger();
        this.lock = new ReentrantLock();
        this.source = connectableFlowable;
    }

    private Disposable disconnect(final CompositeDisposable compositeDisposable) {
        return Disposables.fromRunnable(new Runnable() { // from class: io.reactivex.internal.operators.flowable.FlowableRefCount.2
            @Override // java.lang.Runnable
            public void run() {
                FlowableRefCount.this.lock.lock();
                try {
                    if (FlowableRefCount.this.baseDisposable == compositeDisposable && FlowableRefCount.this.subscriptionCount.decrementAndGet() == 0) {
                        FlowableRefCount.this.baseDisposable.dispose();
                        FlowableRefCount.this.baseDisposable = new CompositeDisposable();
                    }
                } finally {
                    FlowableRefCount.this.lock.unlock();
                }
            }
        });
    }

    private Consumer<Disposable> onSubscribe(final Subscriber<? super T> subscriber, final AtomicBoolean atomicBoolean) {
        return new Consumer<Disposable>() { // from class: io.reactivex.internal.operators.flowable.FlowableRefCount.1
            @Override // io.reactivex.functions.Consumer
            public void accept(Disposable disposable) {
                try {
                    FlowableRefCount.this.baseDisposable.add(disposable);
                    FlowableRefCount.this.doSubscribe(subscriber, FlowableRefCount.this.baseDisposable);
                } finally {
                    FlowableRefCount.this.lock.unlock();
                    atomicBoolean.set(false);
                }
            }
        };
    }

    void doSubscribe(Subscriber<? super T> subscriber, CompositeDisposable compositeDisposable) {
        ConnectionSubscriber connectionSubscriber = new ConnectionSubscriber(subscriber, compositeDisposable, disconnect(compositeDisposable));
        subscriber.onSubscribe(connectionSubscriber);
        this.source.subscribe(connectionSubscriber);
    }

    @Override // io.reactivex.Flowable
    public void subscribeActual(Subscriber<? super T> subscriber) {
        this.lock.lock();
        if (this.subscriptionCount.incrementAndGet() != 1) {
            try {
                doSubscribe(subscriber, this.baseDisposable);
            } finally {
                this.lock.unlock();
            }
        } else {
            AtomicBoolean atomicBoolean = new AtomicBoolean(true);
            try {
                this.source.connect(onSubscribe(subscriber, atomicBoolean));
            } finally {
                if (atomicBoolean.get()) {
                }
            }
        }
    }
}
