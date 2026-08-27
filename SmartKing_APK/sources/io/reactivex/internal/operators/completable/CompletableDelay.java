package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.TimeUnit;

/* loaded from: classes2.dex */
public final class CompletableDelay extends Completable {
    final long delay;
    final boolean delayError;
    final Scheduler scheduler;
    final CompletableSource source;
    final TimeUnit unit;

    public CompletableDelay(CompletableSource completableSource, long j, TimeUnit timeUnit, Scheduler scheduler, boolean z) {
        this.source = completableSource;
        this.delay = j;
        this.unit = timeUnit;
        this.scheduler = scheduler;
        this.delayError = z;
    }

    @Override // io.reactivex.Completable
    protected void subscribeActual(final CompletableObserver completableObserver) {
        final CompositeDisposable compositeDisposable = new CompositeDisposable();
        this.source.subscribe(new CompletableObserver() { // from class: io.reactivex.internal.operators.completable.CompletableDelay.1
            @Override // io.reactivex.CompletableObserver, io.reactivex.MaybeObserver
            public void onComplete() {
                compositeDisposable.add(CompletableDelay.this.scheduler.scheduleDirect(new Runnable() { // from class: io.reactivex.internal.operators.completable.CompletableDelay.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        completableObserver.onComplete();
                    }
                }, CompletableDelay.this.delay, CompletableDelay.this.unit));
            }

            @Override // io.reactivex.CompletableObserver
            public void onError(final Throwable th) {
                compositeDisposable.add(CompletableDelay.this.scheduler.scheduleDirect(new Runnable() { // from class: io.reactivex.internal.operators.completable.CompletableDelay.1.2
                    @Override // java.lang.Runnable
                    public void run() {
                        completableObserver.onError(th);
                    }
                }, CompletableDelay.this.delayError ? CompletableDelay.this.delay : 0L, CompletableDelay.this.unit));
            }

            @Override // io.reactivex.CompletableObserver
            public void onSubscribe(Disposable disposable) {
                compositeDisposable.add(disposable);
                completableObserver.onSubscribe(compositeDisposable);
            }
        });
    }
}
