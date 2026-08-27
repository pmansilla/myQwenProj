package io.reactivex.internal.operators.single;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.SequentialDisposable;
import java.util.concurrent.TimeUnit;

/* loaded from: classes2.dex */
public final class SingleDelay<T> extends Single<T> {
    final Scheduler scheduler;
    final SingleSource<? extends T> source;
    final long time;
    final TimeUnit unit;

    public SingleDelay(SingleSource<? extends T> singleSource, long j, TimeUnit timeUnit, Scheduler scheduler) {
        this.source = singleSource;
        this.time = j;
        this.unit = timeUnit;
        this.scheduler = scheduler;
    }

    @Override // io.reactivex.Single
    protected void subscribeActual(final SingleObserver<? super T> singleObserver) {
        final SequentialDisposable sequentialDisposable = new SequentialDisposable();
        singleObserver.onSubscribe(sequentialDisposable);
        this.source.subscribe(new SingleObserver<T>() { // from class: io.reactivex.internal.operators.single.SingleDelay.1
            @Override // io.reactivex.SingleObserver
            public void onError(final Throwable th) {
                sequentialDisposable.replace(SingleDelay.this.scheduler.scheduleDirect(new Runnable() { // from class: io.reactivex.internal.operators.single.SingleDelay.1.2
                    @Override // java.lang.Runnable
                    public void run() {
                        singleObserver.onError(th);
                    }
                }, 0L, SingleDelay.this.unit));
            }

            @Override // io.reactivex.SingleObserver
            public void onSubscribe(Disposable disposable) {
                sequentialDisposable.replace(disposable);
            }

            @Override // io.reactivex.SingleObserver
            public void onSuccess(final T t) {
                sequentialDisposable.replace(SingleDelay.this.scheduler.scheduleDirect(new Runnable() { // from class: io.reactivex.internal.operators.single.SingleDelay.1.1
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // java.lang.Runnable
                    public void run() {
                        singleObserver.onSuccess(t);
                    }
                }, SingleDelay.this.time, SingleDelay.this.unit));
            }
        });
    }
}
