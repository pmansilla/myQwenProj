package io.reactivex.internal.operators.flowable;

import io.reactivex.Scheduler;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.subscribers.SerializedSubscriber;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/* loaded from: classes2.dex */
public final class FlowableDelay<T> extends AbstractFlowableWithUpstream<T, T> {
    final long delay;
    final boolean delayError;
    final Scheduler scheduler;
    final TimeUnit unit;

    /* loaded from: classes2.dex */
    static final class DelaySubscriber<T> implements Subscriber<T>, Subscription {
        final Subscriber<? super T> actual;
        final long delay;
        final boolean delayError;
        Subscription s;
        final TimeUnit unit;
        final Scheduler.Worker w;

        DelaySubscriber(Subscriber<? super T> subscriber, long j, TimeUnit timeUnit, Scheduler.Worker worker, boolean z) {
            this.actual = subscriber;
            this.delay = j;
            this.unit = timeUnit;
            this.w = worker;
            this.delayError = z;
        }

        @Override // org.reactivestreams.Subscription
        public void cancel() {
            this.w.dispose();
            this.s.cancel();
        }

        @Override // org.reactivestreams.Subscriber
        public void onComplete() {
            this.w.schedule(new Runnable() { // from class: io.reactivex.internal.operators.flowable.FlowableDelay.DelaySubscriber.3
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        DelaySubscriber.this.actual.onComplete();
                    } finally {
                        DelaySubscriber.this.w.dispose();
                    }
                }
            }, this.delay, this.unit);
        }

        @Override // org.reactivestreams.Subscriber
        public void onError(final Throwable th) {
            this.w.schedule(new Runnable() { // from class: io.reactivex.internal.operators.flowable.FlowableDelay.DelaySubscriber.2
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        DelaySubscriber.this.actual.onError(th);
                    } finally {
                        DelaySubscriber.this.w.dispose();
                    }
                }
            }, this.delayError ? this.delay : 0L, this.unit);
        }

        @Override // org.reactivestreams.Subscriber
        public void onNext(final T t) {
            this.w.schedule(new Runnable() { // from class: io.reactivex.internal.operators.flowable.FlowableDelay.DelaySubscriber.1
                @Override // java.lang.Runnable
                public void run() {
                    DelaySubscriber.this.actual.onNext((Object) t);
                }
            }, this.delay, this.unit);
        }

        @Override // org.reactivestreams.Subscriber
        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        @Override // org.reactivestreams.Subscription
        public void request(long j) {
            this.s.request(j);
        }
    }

    public FlowableDelay(Publisher<T> publisher, long j, TimeUnit timeUnit, Scheduler scheduler, boolean z) {
        super(publisher);
        this.delay = j;
        this.unit = timeUnit;
        this.scheduler = scheduler;
        this.delayError = z;
    }

    @Override // io.reactivex.Flowable
    protected void subscribeActual(Subscriber<? super T> subscriber) {
        this.source.subscribe(new DelaySubscriber(this.delayError ? subscriber : new SerializedSubscriber(subscriber), this.delay, this.unit, this.scheduler.createWorker(), this.delayError));
    }
}
