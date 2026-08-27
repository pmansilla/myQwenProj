package io.reactivex.parallel;

import com.bumptech.glide.load.engine.executor.GlideExecutor;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.BackpressureKind;
import io.reactivex.annotations.BackpressureSupport;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.annotations.Experimental;
import io.reactivex.annotations.SchedulerSupport;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.LongConsumer;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.operators.parallel.ParallelCollect;
import io.reactivex.internal.operators.parallel.ParallelConcatMap;
import io.reactivex.internal.operators.parallel.ParallelFilter;
import io.reactivex.internal.operators.parallel.ParallelFlatMap;
import io.reactivex.internal.operators.parallel.ParallelFromArray;
import io.reactivex.internal.operators.parallel.ParallelFromPublisher;
import io.reactivex.internal.operators.parallel.ParallelJoin;
import io.reactivex.internal.operators.parallel.ParallelMap;
import io.reactivex.internal.operators.parallel.ParallelPeek;
import io.reactivex.internal.operators.parallel.ParallelReduce;
import io.reactivex.internal.operators.parallel.ParallelReduceFull;
import io.reactivex.internal.operators.parallel.ParallelRunOn;
import io.reactivex.internal.operators.parallel.ParallelSortedJoin;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.util.ErrorMode;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.internal.util.ListAddBiConsumer;
import io.reactivex.internal.util.MergerBiFunction;
import io.reactivex.internal.util.SorterFunction;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Experimental
/* loaded from: classes.dex */
public abstract class ParallelFlowable<T> {
    @CheckReturnValue
    public static <T> ParallelFlowable<T> from(Publisher<? extends T> publisher) {
        return from(publisher, Runtime.getRuntime().availableProcessors(), Flowable.bufferSize());
    }

    @CheckReturnValue
    public static <T> ParallelFlowable<T> from(Publisher<? extends T> publisher, int i) {
        return from(publisher, i, Flowable.bufferSize());
    }

    @CheckReturnValue
    public static <T> ParallelFlowable<T> from(Publisher<? extends T> publisher, int i, int i2) {
        ObjectHelper.requireNonNull(publisher, GlideExecutor.DEFAULT_SOURCE_EXECUTOR_NAME);
        ObjectHelper.verifyPositive(i, "parallelism");
        ObjectHelper.verifyPositive(i2, "prefetch");
        return new ParallelFromPublisher(publisher, i, i2);
    }

    @CheckReturnValue
    public static <T> ParallelFlowable<T> fromArray(Publisher<T>... publisherArr) {
        if (publisherArr.length != 0) {
            return new ParallelFromArray(publisherArr);
        }
        throw new IllegalArgumentException("Zero publishers not supported");
    }

    @CheckReturnValue
    public final <C> ParallelFlowable<C> collect(Callable<? extends C> callable, BiConsumer<? super C, ? super T> biConsumer) {
        return new ParallelCollect(this, callable, biConsumer);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @CheckReturnValue
    public final <U> ParallelFlowable<U> compose(Function<? super ParallelFlowable<T>, ParallelFlowable<U>> function) {
        return (ParallelFlowable) to(function);
    }

    @CheckReturnValue
    public final <R> ParallelFlowable<R> concatMap(Function<? super T, ? extends Publisher<? extends R>> function) {
        return concatMap(function, 2);
    }

    @CheckReturnValue
    public final <R> ParallelFlowable<R> concatMap(Function<? super T, ? extends Publisher<? extends R>> function, int i) {
        return new ParallelConcatMap(this, function, i, ErrorMode.IMMEDIATE);
    }

    @CheckReturnValue
    public final <R> ParallelFlowable<R> concatMapDelayError(Function<? super T, ? extends Publisher<? extends R>> function, int i, boolean z) {
        return new ParallelConcatMap(this, function, i, z ? ErrorMode.END : ErrorMode.BOUNDARY);
    }

    @CheckReturnValue
    public final <R> ParallelFlowable<R> concatMapDelayError(Function<? super T, ? extends Publisher<? extends R>> function, boolean z) {
        return concatMapDelayError(function, 2, z);
    }

    @CheckReturnValue
    public final ParallelFlowable<T> doAfterNext(Consumer<? super T> consumer) {
        return new ParallelPeek(this, Functions.emptyConsumer(), consumer, Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    public final ParallelFlowable<T> doAfterTerminated(Action action) {
        return new ParallelPeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, action, Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    public final ParallelFlowable<T> doOnCancel(Action action) {
        return new ParallelPeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, action);
    }

    @CheckReturnValue
    public final ParallelFlowable<T> doOnComplete(Action action) {
        return new ParallelPeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.emptyConsumer(), action, Functions.EMPTY_ACTION, Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    public final ParallelFlowable<T> doOnError(Consumer<Throwable> consumer) {
        return new ParallelPeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), consumer, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    public final ParallelFlowable<T> doOnNext(Consumer<? super T> consumer) {
        return new ParallelPeek(this, consumer, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    public final ParallelFlowable<T> doOnRequest(LongConsumer longConsumer) {
        return new ParallelPeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.emptyConsumer(), longConsumer, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    public final ParallelFlowable<T> doOnSubscribe(Consumer<? super Subscription> consumer) {
        return new ParallelPeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, consumer, Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    public final ParallelFlowable<T> filter(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate");
        return new ParallelFilter(this, predicate);
    }

    @CheckReturnValue
    public final <R> ParallelFlowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> function) {
        return flatMap(function, false, Integer.MAX_VALUE, Flowable.bufferSize());
    }

    @CheckReturnValue
    public final <R> ParallelFlowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> function, boolean z) {
        return flatMap(function, z, Integer.MAX_VALUE, Flowable.bufferSize());
    }

    @CheckReturnValue
    public final <R> ParallelFlowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> function, boolean z, int i) {
        return flatMap(function, z, i, Flowable.bufferSize());
    }

    @CheckReturnValue
    public final <R> ParallelFlowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> function, boolean z, int i, int i2) {
        return new ParallelFlatMap(this, function, z, i, i2);
    }

    @CheckReturnValue
    public final <R> ParallelFlowable<R> map(Function<? super T, ? extends R> function) {
        ObjectHelper.requireNonNull(function, "mapper");
        return new ParallelMap(this, function);
    }

    public abstract int parallelism();

    @CheckReturnValue
    public final Flowable<T> reduce(BiFunction<T, T, T> biFunction) {
        ObjectHelper.requireNonNull(biFunction, "reducer");
        return RxJavaPlugins.onAssembly(new ParallelReduceFull(this, biFunction));
    }

    @CheckReturnValue
    public final <R> ParallelFlowable<R> reduce(Callable<R> callable, BiFunction<R, ? super T, R> biFunction) {
        ObjectHelper.requireNonNull(callable, "initialSupplier");
        ObjectHelper.requireNonNull(biFunction, "reducer");
        return new ParallelReduce(this, callable, biFunction);
    }

    @CheckReturnValue
    public final ParallelFlowable<T> runOn(Scheduler scheduler) {
        return runOn(scheduler, Flowable.bufferSize());
    }

    @CheckReturnValue
    public final ParallelFlowable<T> runOn(Scheduler scheduler, int i) {
        ObjectHelper.requireNonNull(scheduler, "scheduler");
        ObjectHelper.verifyPositive(i, "prefetch");
        return new ParallelRunOn(this, scheduler, i);
    }

    @SchedulerSupport(SchedulerSupport.NONE)
    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    public final Flowable<T> sequential() {
        return sequential(Flowable.bufferSize());
    }

    @SchedulerSupport(SchedulerSupport.NONE)
    @BackpressureSupport(BackpressureKind.FULL)
    @CheckReturnValue
    public final Flowable<T> sequential(int i) {
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly(new ParallelJoin(this, i));
    }

    @CheckReturnValue
    public final Flowable<T> sorted(Comparator<? super T> comparator) {
        return sorted(comparator, 16);
    }

    @CheckReturnValue
    public final Flowable<T> sorted(Comparator<? super T> comparator, int i) {
        return RxJavaPlugins.onAssembly(new ParallelSortedJoin(reduce(Functions.createArrayList((i / parallelism()) + 1), ListAddBiConsumer.instance()).map(new SorterFunction(comparator)), comparator));
    }

    public abstract void subscribe(Subscriber<? super T>[] subscriberArr);

    @CheckReturnValue
    public final <U> U to(Function<? super ParallelFlowable<T>, U> function) {
        try {
            return function.apply(this);
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    @CheckReturnValue
    public final Flowable<List<T>> toSortedList(Comparator<? super T> comparator) {
        return toSortedList(comparator, 16);
    }

    @CheckReturnValue
    public final Flowable<List<T>> toSortedList(Comparator<? super T> comparator, int i) {
        return RxJavaPlugins.onAssembly(reduce(Functions.createArrayList((i / parallelism()) + 1), ListAddBiConsumer.instance()).map(new SorterFunction(comparator)).reduce(new MergerBiFunction(comparator)));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean validate(Subscriber<?>[] subscriberArr) {
        int parallelism = parallelism();
        if (subscriberArr.length == parallelism) {
            return true;
        }
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("parallelism = " + parallelism + ", subscribers = " + subscriberArr.length);
        for (Subscriber<?> subscriber : subscriberArr) {
            EmptySubscription.error(illegalArgumentException, subscriber);
        }
        return false;
    }
}
