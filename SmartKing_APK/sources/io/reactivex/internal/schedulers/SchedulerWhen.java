package io.reactivex.internal.schedulers;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.Experimental;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.UnicastProcessor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Experimental
/* loaded from: classes2.dex */
public class SchedulerWhen extends Scheduler implements Disposable {
    private final Scheduler actualScheduler;
    private Disposable disposable;
    private final FlowableProcessor<Flowable<Completable>> workerProcessor = UnicastProcessor.create().toSerialized();
    static final Disposable SUBSCRIBED = new Disposable() { // from class: io.reactivex.internal.schedulers.SchedulerWhen.3
        @Override // io.reactivex.disposables.Disposable
        public void dispose() {
        }

        @Override // io.reactivex.disposables.Disposable
        public boolean isDisposed() {
            return false;
        }
    };
    static final Disposable DISPOSED = Disposables.disposed();

    /* loaded from: classes2.dex */
    static class DelayedAction extends ScheduledAction {
        private final Runnable action;
        private final long delayTime;
        private final TimeUnit unit;

        DelayedAction(Runnable runnable, long j, TimeUnit timeUnit) {
            this.action = runnable;
            this.delayTime = j;
            this.unit = timeUnit;
        }

        @Override // io.reactivex.internal.schedulers.SchedulerWhen.ScheduledAction
        protected Disposable callActual(Scheduler.Worker worker, CompletableObserver completableObserver) {
            return worker.schedule(new OnCompletedAction(this.action, completableObserver), this.delayTime, this.unit);
        }
    }

    /* loaded from: classes2.dex */
    static class ImmediateAction extends ScheduledAction {
        private final Runnable action;

        ImmediateAction(Runnable runnable) {
            this.action = runnable;
        }

        @Override // io.reactivex.internal.schedulers.SchedulerWhen.ScheduledAction
        protected Disposable callActual(Scheduler.Worker worker, CompletableObserver completableObserver) {
            return worker.schedule(new OnCompletedAction(this.action, completableObserver));
        }
    }

    /* loaded from: classes2.dex */
    static class OnCompletedAction implements Runnable {
        private Runnable action;
        private CompletableObserver actionCompletable;

        OnCompletedAction(Runnable runnable, CompletableObserver completableObserver) {
            this.action = runnable;
            this.actionCompletable = completableObserver;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                this.action.run();
            } finally {
                this.actionCompletable.onComplete();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static abstract class ScheduledAction extends AtomicReference<Disposable> implements Disposable {
        ScheduledAction() {
            super(SchedulerWhen.SUBSCRIBED);
        }

        void call(Scheduler.Worker worker, CompletableObserver completableObserver) {
            Disposable disposable = get();
            if (disposable != SchedulerWhen.DISPOSED && disposable == SchedulerWhen.SUBSCRIBED) {
                Disposable callActual = callActual(worker, completableObserver);
                if (compareAndSet(SchedulerWhen.SUBSCRIBED, callActual)) {
                    return;
                }
                callActual.dispose();
            }
        }

        protected abstract Disposable callActual(Scheduler.Worker worker, CompletableObserver completableObserver);

        @Override // io.reactivex.disposables.Disposable
        public void dispose() {
            Disposable disposable;
            Disposable disposable2 = SchedulerWhen.DISPOSED;
            do {
                disposable = get();
                if (disposable == SchedulerWhen.DISPOSED) {
                    return;
                }
            } while (!compareAndSet(disposable, disposable2));
            if (disposable != SchedulerWhen.SUBSCRIBED) {
                disposable.dispose();
            }
        }

        @Override // io.reactivex.disposables.Disposable
        public boolean isDisposed() {
            return get().isDisposed();
        }
    }

    public SchedulerWhen(Function<Flowable<Flowable<Completable>>, Completable> function, Scheduler scheduler) {
        this.actualScheduler = scheduler;
        try {
            this.disposable = function.apply(this.workerProcessor).subscribe();
        } catch (Throwable th) {
            Exceptions.propagate(th);
        }
    }

    @Override // io.reactivex.Scheduler
    public Scheduler.Worker createWorker() {
        final Scheduler.Worker createWorker = this.actualScheduler.createWorker();
        final FlowableProcessor<T> serialized = UnicastProcessor.create().toSerialized();
        Flowable<Completable> map = serialized.map(new Function<ScheduledAction, Completable>() { // from class: io.reactivex.internal.schedulers.SchedulerWhen.1
            @Override // io.reactivex.functions.Function
            public Completable apply(final ScheduledAction scheduledAction) {
                return new Completable() { // from class: io.reactivex.internal.schedulers.SchedulerWhen.1.1
                    @Override // io.reactivex.Completable
                    protected void subscribeActual(CompletableObserver completableObserver) {
                        completableObserver.onSubscribe(scheduledAction);
                        scheduledAction.call(createWorker, completableObserver);
                    }
                };
            }
        });
        Scheduler.Worker worker = new Scheduler.Worker() { // from class: io.reactivex.internal.schedulers.SchedulerWhen.2
            private final AtomicBoolean unsubscribed = new AtomicBoolean();

            @Override // io.reactivex.disposables.Disposable
            public void dispose() {
                if (this.unsubscribed.compareAndSet(false, true)) {
                    createWorker.dispose();
                    serialized.onComplete();
                }
            }

            @Override // io.reactivex.disposables.Disposable
            public boolean isDisposed() {
                return this.unsubscribed.get();
            }

            @Override // io.reactivex.Scheduler.Worker
            public Disposable schedule(Runnable runnable) {
                ImmediateAction immediateAction = new ImmediateAction(runnable);
                serialized.onNext(immediateAction);
                return immediateAction;
            }

            @Override // io.reactivex.Scheduler.Worker
            public Disposable schedule(Runnable runnable, long j, TimeUnit timeUnit) {
                DelayedAction delayedAction = new DelayedAction(runnable, j, timeUnit);
                serialized.onNext(delayedAction);
                return delayedAction;
            }
        };
        this.workerProcessor.onNext(map);
        return worker;
    }

    @Override // io.reactivex.disposables.Disposable
    public void dispose() {
        this.disposable.dispose();
    }

    @Override // io.reactivex.disposables.Disposable
    public boolean isDisposed() {
        return this.disposable.isDisposed();
    }
}
