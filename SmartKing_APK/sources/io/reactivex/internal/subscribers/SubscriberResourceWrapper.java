package io.reactivex.internal.subscribers;

import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/* loaded from: classes2.dex */
public final class SubscriberResourceWrapper<T> extends AtomicReference<Disposable> implements Subscriber<T>, Disposable, Subscription {
    private static final long serialVersionUID = -8612022020200669122L;
    final Subscriber<? super T> actual;
    final AtomicReference<Subscription> subscription = new AtomicReference<>();

    public SubscriberResourceWrapper(Subscriber<? super T> subscriber) {
        this.actual = subscriber;
    }

    @Override // org.reactivestreams.Subscription
    public void cancel() {
        dispose();
    }

    @Override // io.reactivex.disposables.Disposable
    public void dispose() {
        SubscriptionHelper.cancel(this.subscription);
        DisposableHelper.dispose(this);
    }

    @Override // io.reactivex.disposables.Disposable
    public boolean isDisposed() {
        return this.subscription.get() == SubscriptionHelper.CANCELLED;
    }

    @Override // org.reactivestreams.Subscriber
    public void onComplete() {
        dispose();
        this.actual.onComplete();
    }

    @Override // org.reactivestreams.Subscriber
    public void onError(Throwable th) {
        dispose();
        this.actual.onError(th);
    }

    @Override // org.reactivestreams.Subscriber
    public void onNext(T t) {
        this.actual.onNext(t);
    }

    @Override // org.reactivestreams.Subscriber
    public void onSubscribe(Subscription subscription) {
        do {
            Subscription subscription2 = this.subscription.get();
            if (subscription2 == SubscriptionHelper.CANCELLED) {
                subscription.cancel();
                return;
            } else if (subscription2 != null) {
                subscription.cancel();
                SubscriptionHelper.reportSubscriptionSet();
                return;
            }
        } while (!this.subscription.compareAndSet(null, subscription));
        this.actual.onSubscribe(this);
    }

    @Override // org.reactivestreams.Subscription
    public void request(long j) {
        if (SubscriptionHelper.validate(j)) {
            this.subscription.get().request(j);
        }
    }

    public void setResource(Disposable disposable) {
        DisposableHelper.set(this, disposable);
    }
}
