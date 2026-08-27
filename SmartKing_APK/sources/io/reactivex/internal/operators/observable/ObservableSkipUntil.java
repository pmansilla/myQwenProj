package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.ArrayCompositeDisposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.observers.SerializedObserver;

/* loaded from: classes2.dex */
public final class ObservableSkipUntil<T, U> extends AbstractObservableWithUpstream<T, T> {
    final ObservableSource<U> other;

    /* loaded from: classes2.dex */
    static final class SkipUntilObserver<T> implements Observer<T> {
        final Observer<? super T> actual;
        final ArrayCompositeDisposable frc;
        volatile boolean notSkipping;
        boolean notSkippingLocal;
        Disposable s;

        SkipUntilObserver(Observer<? super T> observer, ArrayCompositeDisposable arrayCompositeDisposable) {
            this.actual = observer;
            this.frc = arrayCompositeDisposable;
        }

        @Override // io.reactivex.Observer
        public void onComplete() {
            this.frc.dispose();
            this.actual.onComplete();
        }

        @Override // io.reactivex.Observer
        public void onError(Throwable th) {
            this.frc.dispose();
            this.actual.onError(th);
        }

        @Override // io.reactivex.Observer
        public void onNext(T t) {
            if (this.notSkippingLocal) {
                this.actual.onNext(t);
            } else if (this.notSkipping) {
                this.notSkippingLocal = true;
                this.actual.onNext(t);
            }
        }

        @Override // io.reactivex.Observer
        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.frc.setResource(0, disposable);
            }
        }
    }

    public ObservableSkipUntil(ObservableSource<T> observableSource, ObservableSource<U> observableSource2) {
        super(observableSource);
        this.other = observableSource2;
    }

    @Override // io.reactivex.Observable
    public void subscribeActual(Observer<? super T> observer) {
        final SerializedObserver serializedObserver = new SerializedObserver(observer);
        final ArrayCompositeDisposable arrayCompositeDisposable = new ArrayCompositeDisposable(2);
        serializedObserver.onSubscribe(arrayCompositeDisposable);
        final SkipUntilObserver skipUntilObserver = new SkipUntilObserver(serializedObserver, arrayCompositeDisposable);
        this.other.subscribe(new Observer<U>() { // from class: io.reactivex.internal.operators.observable.ObservableSkipUntil.1
            Disposable s;

            @Override // io.reactivex.Observer
            public void onComplete() {
                skipUntilObserver.notSkipping = true;
            }

            @Override // io.reactivex.Observer
            public void onError(Throwable th) {
                arrayCompositeDisposable.dispose();
                serializedObserver.onError(th);
            }

            @Override // io.reactivex.Observer
            public void onNext(U u) {
                this.s.dispose();
                skipUntilObserver.notSkipping = true;
            }

            @Override // io.reactivex.Observer
            public void onSubscribe(Disposable disposable) {
                if (DisposableHelper.validate(this.s, disposable)) {
                    this.s = disposable;
                    arrayCompositeDisposable.setResource(1, disposable);
                }
            }
        });
        this.source.subscribe(skipUntilObserver);
    }
}
