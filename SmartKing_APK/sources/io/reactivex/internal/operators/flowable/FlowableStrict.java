package io.reactivex.internal.operators.flowable;

import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.HalfSerializer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/* loaded from: classes2.dex */
public final class FlowableStrict<T> extends AbstractFlowableWithUpstream<T, T> {

    /* loaded from: classes2.dex */
    static final class StrictSubscriber<T> extends AtomicInteger implements Subscriber<T>, Subscription {
        private static final long serialVersionUID = -4945028590049415624L;
        final Subscriber<? super T> actual;
        volatile boolean done;
        final AtomicThrowable error = new AtomicThrowable();
        final AtomicLong requested = new AtomicLong();
        final AtomicReference<Subscription> s = new AtomicReference<>();
        final AtomicBoolean once = new AtomicBoolean();

        StrictSubscriber(Subscriber<? super T> subscriber) {
            this.actual = subscriber;
        }

        @Override // org.reactivestreams.Subscription
        public void cancel() {
            if (this.done) {
                return;
            }
            SubscriptionHelper.cancel(this.s);
        }

        @Override // org.reactivestreams.Subscriber
        public void onComplete() {
            this.done = true;
            HalfSerializer.onComplete(this.actual, this, this.error);
        }

        @Override // org.reactivestreams.Subscriber
        public void onError(Throwable th) {
            this.done = true;
            HalfSerializer.onError(this.actual, th, this, this.error);
        }

        @Override // org.reactivestreams.Subscriber
        public void onNext(T t) {
            HalfSerializer.onNext(this.actual, t, this, this.error);
        }

        @Override // org.reactivestreams.Subscriber
        public void onSubscribe(Subscription subscription) {
            if (this.once.compareAndSet(false, true)) {
                this.actual.onSubscribe(this);
                SubscriptionHelper.deferredSetOnce(this.s, this.requested, subscription);
            } else {
                subscription.cancel();
                cancel();
                onError(new IllegalStateException("§2.12 violated: onSubscribe must be called at most once"));
            }
        }

        @Override // org.reactivestreams.Subscription
        public void request(long j) {
            if (j > 0) {
                SubscriptionHelper.deferredRequest(this.s, this.requested, j);
                return;
            }
            cancel();
            onError(new IllegalArgumentException("§3.9 violated: positive request amount required but it was " + j));
        }
    }

    public FlowableStrict(Publisher<T> publisher) {
        super(publisher);
    }

    @Override // io.reactivex.Flowable
    protected void subscribeActual(Subscriber<? super T> subscriber) {
        this.source.subscribe(new StrictSubscriber(subscriber));
    }
}
