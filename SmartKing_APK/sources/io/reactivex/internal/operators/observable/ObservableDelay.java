package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.observers.SerializedObserver;
import java.util.concurrent.TimeUnit;

/* loaded from: classes2.dex */
public final class ObservableDelay<T> extends AbstractObservableWithUpstream<T, T> {
    final long delay;
    final boolean delayError;
    final Scheduler scheduler;
    final TimeUnit unit;

    /* loaded from: classes2.dex */
    static final class DelayObserver<T> implements Observer<T>, Disposable {
        final Observer<? super T> actual;
        final long delay;
        final boolean delayError;
        Disposable s;
        final TimeUnit unit;
        final Scheduler.Worker w;

        DelayObserver(Observer<? super T> observer, long j, TimeUnit timeUnit, Scheduler.Worker worker, boolean z) {
            this.actual = observer;
            this.delay = j;
            this.unit = timeUnit;
            this.w = worker;
            this.delayError = z;
        }

        @Override // io.reactivex.disposables.Disposable
        public void dispose() {
            this.w.dispose();
            this.s.dispose();
        }

        @Override // io.reactivex.disposables.Disposable
        public boolean isDisposed() {
            return this.w.isDisposed();
        }

        @Override // io.reactivex.Observer
        public void onComplete() {
            this.w.schedule(new Runnable() { // from class: io.reactivex.internal.operators.observable.ObservableDelay.DelayObserver.3
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        DelayObserver.this.actual.onComplete();
                    } finally {
                        DelayObserver.this.w.dispose();
                    }
                }
            }, this.delay, this.unit);
        }

        @Override // io.reactivex.Observer
        public void onError(final Throwable th) {
            this.w.schedule(new Runnable() { // from class: io.reactivex.internal.operators.observable.ObservableDelay.DelayObserver.2
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        DelayObserver.this.actual.onError(th);
                    } finally {
                        DelayObserver.this.w.dispose();
                    }
                }
            }, this.delayError ? this.delay : 0L, this.unit);
        }

        @Override // io.reactivex.Observer
        public void onNext(final T t) {
            this.w.schedule(new Runnable() { // from class: io.reactivex.internal.operators.observable.ObservableDelay.DelayObserver.1
                @Override // java.lang.Runnable
                public void run() {
                    DelayObserver.this.actual.onNext((Object) t);
                }
            }, this.delay, this.unit);
        }

        @Override // io.reactivex.Observer
        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservableDelay(ObservableSource<T> observableSource, long j, TimeUnit timeUnit, Scheduler scheduler, boolean z) {
        super(observableSource);
        this.delay = j;
        this.unit = timeUnit;
        this.scheduler = scheduler;
        this.delayError = z;
    }

    @Override // io.reactivex.Observable
    public void subscribeActual(Observer<? super T> observer) {
        this.source.subscribe(new DelayObserver(this.delayError ? observer : new SerializedObserver(observer), this.delay, this.unit, this.scheduler.createWorker(), this.delayError));
    }
}
