package io.reactivex.observers;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

/* loaded from: classes2.dex */
public abstract class DefaultObserver<T> implements Observer<T> {
    private Disposable s;

    protected final void cancel() {
        Disposable disposable = this.s;
        this.s = DisposableHelper.DISPOSED;
        disposable.dispose();
    }

    protected void onStart() {
    }

    @Override // io.reactivex.Observer
    public final void onSubscribe(Disposable disposable) {
        if (DisposableHelper.validate(this.s, disposable)) {
            this.s = disposable;
            onStart();
        }
    }
}
