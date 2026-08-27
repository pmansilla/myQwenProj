package io.reactivex.internal.operators.single;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes2.dex */
public final class SingleTimeout<T> extends Single<T> {
    final SingleSource<? extends T> other;
    final Scheduler scheduler;
    final SingleSource<T> source;
    final long timeout;
    final TimeUnit unit;

    public SingleTimeout(SingleSource<T> singleSource, long j, TimeUnit timeUnit, Scheduler scheduler, SingleSource<? extends T> singleSource2) {
        this.source = singleSource;
        this.timeout = j;
        this.unit = timeUnit;
        this.scheduler = scheduler;
        this.other = singleSource2;
    }

    @Override // io.reactivex.Single
    protected void subscribeActual(final SingleObserver<? super T> singleObserver) {
        final CompositeDisposable compositeDisposable = new CompositeDisposable();
        singleObserver.onSubscribe(compositeDisposable);
        final AtomicBoolean atomicBoolean = new AtomicBoolean();
        compositeDisposable.add(this.scheduler.scheduleDirect(new Runnable() { // from class: io.reactivex.internal.operators.single.SingleTimeout.1
            @Override // java.lang.Runnable
            public void run() {
                if (atomicBoolean.compareAndSet(false, true)) {
                    if (SingleTimeout.this.other != null) {
                        compositeDisposable.clear();
                        SingleTimeout.this.other.subscribe(new SingleObserver<T>() { // from class: io.reactivex.internal.operators.single.SingleTimeout.1.1
                            @Override // io.reactivex.SingleObserver
                            public void onError(Throwable th) {
                                compositeDisposable.dispose();
                                singleObserver.onError(th);
                            }

                            @Override // io.reactivex.SingleObserver
                            public void onSubscribe(Disposable disposable) {
                                compositeDisposable.add(disposable);
                            }

                            @Override // io.reactivex.SingleObserver
                            public void onSuccess(T t) {
                                compositeDisposable.dispose();
                                singleObserver.onSuccess(t);
                            }
                        });
                    } else {
                        compositeDisposable.dispose();
                        singleObserver.onError(new TimeoutException());
                    }
                }
            }
        }, this.timeout, this.unit));
        this.source.subscribe(new SingleObserver<T>() { // from class: io.reactivex.internal.operators.single.SingleTimeout.2
            @Override // io.reactivex.SingleObserver
            public void onError(Throwable th) {
                if (atomicBoolean.compareAndSet(false, true)) {
                    compositeDisposable.dispose();
                    singleObserver.onError(th);
                }
            }

            @Override // io.reactivex.SingleObserver
            public void onSubscribe(Disposable disposable) {
                compositeDisposable.add(disposable);
            }

            @Override // io.reactivex.SingleObserver
            public void onSuccess(T t) {
                if (atomicBoolean.compareAndSet(false, true)) {
                    compositeDisposable.dispose();
                    singleObserver.onSuccess(t);
                }
            }
        });
    }
}
