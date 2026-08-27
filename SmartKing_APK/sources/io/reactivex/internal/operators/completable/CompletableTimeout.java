package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes2.dex */
public final class CompletableTimeout extends Completable {
    final CompletableSource other;
    final Scheduler scheduler;
    final CompletableSource source;
    final long timeout;
    final TimeUnit unit;

    public CompletableTimeout(CompletableSource completableSource, long j, TimeUnit timeUnit, Scheduler scheduler, CompletableSource completableSource2) {
        this.source = completableSource;
        this.timeout = j;
        this.unit = timeUnit;
        this.scheduler = scheduler;
        this.other = completableSource2;
    }

    @Override // io.reactivex.Completable
    public void subscribeActual(final CompletableObserver completableObserver) {
        final CompositeDisposable compositeDisposable = new CompositeDisposable();
        completableObserver.onSubscribe(compositeDisposable);
        final AtomicBoolean atomicBoolean = new AtomicBoolean();
        compositeDisposable.add(this.scheduler.scheduleDirect(new Runnable() { // from class: io.reactivex.internal.operators.completable.CompletableTimeout.1
            @Override // java.lang.Runnable
            public void run() {
                if (atomicBoolean.compareAndSet(false, true)) {
                    compositeDisposable.clear();
                    if (CompletableTimeout.this.other == null) {
                        completableObserver.onError(new TimeoutException());
                    } else {
                        CompletableTimeout.this.other.subscribe(new CompletableObserver() { // from class: io.reactivex.internal.operators.completable.CompletableTimeout.1.1
                            @Override // io.reactivex.CompletableObserver, io.reactivex.MaybeObserver
                            public void onComplete() {
                                compositeDisposable.dispose();
                                completableObserver.onComplete();
                            }

                            @Override // io.reactivex.CompletableObserver
                            public void onError(Throwable th) {
                                compositeDisposable.dispose();
                                completableObserver.onError(th);
                            }

                            @Override // io.reactivex.CompletableObserver
                            public void onSubscribe(Disposable disposable) {
                                compositeDisposable.add(disposable);
                            }
                        });
                    }
                }
            }
        }, this.timeout, this.unit));
        this.source.subscribe(new CompletableObserver() { // from class: io.reactivex.internal.operators.completable.CompletableTimeout.2
            @Override // io.reactivex.CompletableObserver, io.reactivex.MaybeObserver
            public void onComplete() {
                if (atomicBoolean.compareAndSet(false, true)) {
                    compositeDisposable.dispose();
                    completableObserver.onComplete();
                }
            }

            @Override // io.reactivex.CompletableObserver
            public void onError(Throwable th) {
                if (!atomicBoolean.compareAndSet(false, true)) {
                    RxJavaPlugins.onError(th);
                } else {
                    compositeDisposable.dispose();
                    completableObserver.onError(th);
                }
            }

            @Override // io.reactivex.CompletableObserver
            public void onSubscribe(Disposable disposable) {
                compositeDisposable.add(disposable);
            }
        });
    }
}
