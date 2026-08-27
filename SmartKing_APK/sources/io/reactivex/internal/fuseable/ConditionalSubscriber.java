package io.reactivex.internal.fuseable;

import org.reactivestreams.Subscriber;

/* loaded from: classes2.dex */
public interface ConditionalSubscriber<T> extends Subscriber<T> {
    boolean tryOnNext(T t);
}
