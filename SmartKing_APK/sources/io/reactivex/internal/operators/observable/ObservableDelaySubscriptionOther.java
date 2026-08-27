package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.SequentialDisposable;
import io.reactivex.plugins.RxJavaPlugins;

/* loaded from: classes2.dex */
public final class ObservableDelaySubscriptionOther<T, U> extends Observable<T> {
    final ObservableSource<? extends T> main;
    final ObservableSource<U> other;

    public ObservableDelaySubscriptionOther(ObservableSource<? extends T> observableSource, ObservableSource<U> observableSource2) {
        this.main = observableSource;
        this.other = observableSource2;
    }

    @Override // io.reactivex.Observable
    public void subscribeActual(final Observer<? super T> observer) {
        final SequentialDisposable sequentialDisposable = new SequentialDisposable();
        observer.onSubscribe(sequentialDisposable);
        this.other.subscribe(new Observer<U>() { // from class: io.reactivex.internal.operators.observable.ObservableDelaySubscriptionOther.1
            boolean done;

            @Override // io.reactivex.Observer
            public void onComplete() {
                if (this.done) {
                    return;
                }
                this.done = true;
                ObservableDelaySubscriptionOther.this.main.subscribe(new Observer<T>() { // from class: io.reactivex.internal.operators.observable.ObservableDelaySubscriptionOther.1.1
                    @Override // io.reactivex.Observer
                    public void onComplete() {
                        observer.onComplete();
                    }

                    @Override // io.reactivex.Observer
                    public void onError(Throwable th) {
                        observer.onError(th);
                    }

                    @Override // io.reactivex.Observer
                    public void onNext(T t) {
                        observer.onNext(t);
                    }

                    @Override // io.reactivex.Observer
                    public void onSubscribe(Disposable disposable) {
                        sequentialDisposable.update(disposable);
                    }
                });
            }

            @Override // io.reactivex.Observer
            public void onError(Throwable th) {
                if (this.done) {
                    RxJavaPlugins.onError(th);
                } else {
                    this.done = true;
                    observer.onError(th);
                }
            }

            @Override // io.reactivex.Observer
            public void onNext(U u) {
                onComplete();
            }

            @Override // io.reactivex.Observer
            public void onSubscribe(Disposable disposable) {
                sequentialDisposable.update(disposable);
            }
        });
    }
}
