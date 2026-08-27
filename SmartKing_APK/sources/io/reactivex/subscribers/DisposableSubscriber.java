package io.reactivex.subscribers;

import io.reactivex.disposables.Disposable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/* loaded from: classes2.dex */
public abstract class DisposableSubscriber<T> implements Subscriber<T>, Disposable {
    final AtomicReference<Subscription> s = new AtomicReference<>();

    /* JADX INFO: Access modifiers changed from: protected */
    public final void cancel() {
        dispose();
    }

    @Override // io.reactivex.disposables.Disposable
    public final void dispose() {
        SubscriptionHelper.cancel(this.s);
    }

    @Override // io.reactivex.disposables.Disposable
    public final boolean isDisposed() {
        return this.s.get() == SubscriptionHelper.CANCELLED;
    }

    protected void onStart() {
        this.s.get().request(LongCompanionObject.MAX_VALUE);
    }

    @Override // org.reactivestreams.Subscriber
    public final void onSubscribe(Subscription subscription) {
        if (SubscriptionHelper.setOnce(this.s, subscription)) {
            onStart();
        }
    }

    protected final void request(long j) {
        this.s.get().request(j);
    }
}
