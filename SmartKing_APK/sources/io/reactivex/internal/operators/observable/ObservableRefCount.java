package io.reactivex.internal.operators.observable;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.observables.ConnectableObservable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: classes2.dex */
public final class ObservableRefCount<T> extends AbstractObservableWithUpstream<T, T> {
    volatile CompositeDisposable baseDisposable;
    final ReentrantLock lock;
    final ConnectableObservable<? extends T> source;
    final AtomicInteger subscriptionCount;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public final class ConnectionObserver extends AtomicReference<Disposable> implements Observer<T>, Disposable {
        private static final long serialVersionUID = 3813126992133394324L;
        final CompositeDisposable currentBase;
        final Disposable resource;
        final Observer<? super T> subscriber;

        ConnectionObserver(Observer<? super T> observer, CompositeDisposable compositeDisposable, Disposable disposable) {
            this.subscriber = observer;
            this.currentBase = compositeDisposable;
            this.resource = disposable;
        }

        void cleanup() {
            ObservableRefCount.this.lock.lock();
            try {
                if (ObservableRefCount.this.baseDisposable == this.currentBase) {
                    ObservableRefCount.this.baseDisposable.dispose();
                    ObservableRefCount.this.baseDisposable = new CompositeDisposable();
                    ObservableRefCount.this.subscriptionCount.set(0);
                }
            } finally {
                ObservableRefCount.this.lock.unlock();
            }
        }

        @Override // io.reactivex.disposables.Disposable
        public void dispose() {
            DisposableHelper.dispose(this);
            this.resource.dispose();
        }

        @Override // io.reactivex.disposables.Disposable
        public boolean isDisposed() {
            return DisposableHelper.isDisposed(get());
        }

        @Override // io.reactivex.Observer
        public void onComplete() {
            cleanup();
            this.subscriber.onComplete();
        }

        @Override // io.reactivex.Observer
        public void onError(Throwable th) {
            cleanup();
            this.subscriber.onError(th);
        }

        @Override // io.reactivex.Observer
        public void onNext(T t) {
            this.subscriber.onNext(t);
        }

        @Override // io.reactivex.Observer
        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this, disposable);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ObservableRefCount(ConnectableObservable<T> connectableObservable) {
        super(connectableObservable);
        this.baseDisposable = new CompositeDisposable();
        this.subscriptionCount = new AtomicInteger();
        this.lock = new ReentrantLock();
        this.source = connectableObservable;
    }

    private Disposable disconnect(final CompositeDisposable compositeDisposable) {
        return Disposables.fromRunnable(new Runnable() { // from class: io.reactivex.internal.operators.observable.ObservableRefCount.2
            @Override // java.lang.Runnable
            public void run() {
                ObservableRefCount.this.lock.lock();
                try {
                    if (ObservableRefCount.this.baseDisposable == compositeDisposable && ObservableRefCount.this.subscriptionCount.decrementAndGet() == 0) {
                        ObservableRefCount.this.baseDisposable.dispose();
                        ObservableRefCount.this.baseDisposable = new CompositeDisposable();
                    }
                } finally {
                    ObservableRefCount.this.lock.unlock();
                }
            }
        });
    }

    private Consumer<Disposable> onSubscribe(final Observer<? super T> observer, final AtomicBoolean atomicBoolean) {
        return new Consumer<Disposable>() { // from class: io.reactivex.internal.operators.observable.ObservableRefCount.1
            @Override // io.reactivex.functions.Consumer
            public void accept(Disposable disposable) {
                try {
                    ObservableRefCount.this.baseDisposable.add(disposable);
                    ObservableRefCount.this.doSubscribe(observer, ObservableRefCount.this.baseDisposable);
                } finally {
                    ObservableRefCount.this.lock.unlock();
                    atomicBoolean.set(false);
                }
            }
        };
    }

    void doSubscribe(Observer<? super T> observer, CompositeDisposable compositeDisposable) {
        ConnectionObserver connectionObserver = new ConnectionObserver(observer, compositeDisposable, disconnect(compositeDisposable));
        observer.onSubscribe(connectionObserver);
        this.source.subscribe(connectionObserver);
    }

    @Override // io.reactivex.Observable
    public void subscribeActual(Observer<? super T> observer) {
        this.lock.lock();
        if (this.subscriptionCount.incrementAndGet() != 1) {
            try {
                doSubscribe(observer, this.baseDisposable);
            } finally {
                this.lock.unlock();
            }
        } else {
            AtomicBoolean atomicBoolean = new AtomicBoolean(true);
            try {
                this.source.connect(onSubscribe(observer, atomicBoolean));
            } finally {
                if (atomicBoolean.get()) {
                }
            }
        }
    }
}
