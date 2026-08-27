package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.internal.subscriptions.SubscriptionArbiter;
import io.reactivex.plugins.RxJavaPlugins;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/* loaded from: classes2.dex */
public final class FlowableDelaySubscriptionOther<T, U> extends Flowable<T> {
    final Publisher<? extends T> main;
    final Publisher<U> other;

    public FlowableDelaySubscriptionOther(Publisher<? extends T> publisher, Publisher<U> publisher2) {
        this.main = publisher;
        this.other = publisher2;
    }

    @Override // io.reactivex.Flowable
    public void subscribeActual(final Subscriber<? super T> subscriber) {
        final SubscriptionArbiter subscriptionArbiter = new SubscriptionArbiter();
        subscriber.onSubscribe(subscriptionArbiter);
        this.other.subscribe(new Subscriber<U>() { // from class: io.reactivex.internal.operators.flowable.FlowableDelaySubscriptionOther.1
            boolean done;

            @Override // org.reactivestreams.Subscriber
            public void onComplete() {
                if (this.done) {
                    return;
                }
                this.done = true;
                FlowableDelaySubscriptionOther.this.main.subscribe(new Subscriber<T>() { // from class: io.reactivex.internal.operators.flowable.FlowableDelaySubscriptionOther.1.2
                    @Override // org.reactivestreams.Subscriber
                    public void onComplete() {
                        subscriber.onComplete();
                    }

                    @Override // org.reactivestreams.Subscriber
                    public void onError(Throwable th) {
                        subscriber.onError(th);
                    }

                    @Override // org.reactivestreams.Subscriber
                    public void onNext(T t) {
                        subscriber.onNext(t);
                    }

                    @Override // org.reactivestreams.Subscriber
                    public void onSubscribe(Subscription subscription) {
                        subscriptionArbiter.setSubscription(subscription);
                    }
                });
            }

            @Override // org.reactivestreams.Subscriber
            public void onError(Throwable th) {
                if (this.done) {
                    RxJavaPlugins.onError(th);
                } else {
                    this.done = true;
                    subscriber.onError(th);
                }
            }

            @Override // org.reactivestreams.Subscriber
            public void onNext(U u) {
                onComplete();
            }

            @Override // org.reactivestreams.Subscriber
            public void onSubscribe(final Subscription subscription) {
                subscriptionArbiter.setSubscription(new Subscription() { // from class: io.reactivex.internal.operators.flowable.FlowableDelaySubscriptionOther.1.1
                    @Override // org.reactivestreams.Subscription
                    public void cancel() {
                        subscription.cancel();
                    }

                    @Override // org.reactivestreams.Subscription
                    public void request(long j) {
                    }
                });
                subscription.request(LongCompanionObject.MAX_VALUE);
            }
        });
    }
}
