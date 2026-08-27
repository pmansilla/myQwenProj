package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes2.dex */
public final class ObservableCombineLatest<T, R> extends Observable<R> {
    final int bufferSize;
    final Function<? super Object[], ? extends R> combiner;
    final boolean delayError;
    final ObservableSource<? extends T>[] sources;
    final Iterable<? extends ObservableSource<? extends T>> sourcesIterable;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static final class CombinerObserver<T, R> implements Observer<T> {
        final int index;
        final LatestCoordinator<T, R> parent;
        final AtomicReference<Disposable> s = new AtomicReference<>();

        CombinerObserver(LatestCoordinator<T, R> latestCoordinator, int i) {
            this.parent = latestCoordinator;
            this.index = i;
        }

        public void dispose() {
            DisposableHelper.dispose(this.s);
        }

        @Override // io.reactivex.Observer
        public void onComplete() {
            this.parent.combine(null, this.index);
        }

        @Override // io.reactivex.Observer
        public void onError(Throwable th) {
            this.parent.onError(th);
            this.parent.combine(null, this.index);
        }

        @Override // io.reactivex.Observer
        public void onNext(T t) {
            this.parent.combine(t, this.index);
        }

        @Override // io.reactivex.Observer
        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this.s, disposable);
        }
    }

    /* loaded from: classes2.dex */
    static final class LatestCoordinator<T, R> extends AtomicInteger implements Disposable {
        private static final long serialVersionUID = 8567835998786448817L;
        int active;
        final Observer<? super R> actual;
        volatile boolean cancelled;
        final Function<? super Object[], ? extends R> combiner;
        int complete;
        final boolean delayError;
        volatile boolean done;
        final AtomicThrowable errors = new AtomicThrowable();
        final T[] latest;
        final CombinerObserver<T, R>[] observers;
        final SpscLinkedArrayQueue<Object> queue;

        LatestCoordinator(Observer<? super R> observer, Function<? super Object[], ? extends R> function, int i, int i2, boolean z) {
            this.actual = observer;
            this.combiner = function;
            this.delayError = z;
            this.latest = (T[]) new Object[i];
            this.observers = new CombinerObserver[i];
            this.queue = new SpscLinkedArrayQueue<>(i2);
        }

        void cancel(SpscLinkedArrayQueue<?> spscLinkedArrayQueue) {
            clear(spscLinkedArrayQueue);
            for (CombinerObserver<T, R> combinerObserver : this.observers) {
                combinerObserver.dispose();
            }
        }

        boolean checkTerminated(boolean z, boolean z2, Observer<?> observer, SpscLinkedArrayQueue<?> spscLinkedArrayQueue, boolean z3) {
            if (this.cancelled) {
                cancel(spscLinkedArrayQueue);
                return true;
            }
            if (!z) {
                return false;
            }
            if (z3) {
                if (!z2) {
                    return false;
                }
                cancel(spscLinkedArrayQueue);
                Throwable terminate = this.errors.terminate();
                if (terminate != null) {
                    observer.onError(terminate);
                } else {
                    observer.onComplete();
                }
                return true;
            }
            if (this.errors.get() != null) {
                cancel(spscLinkedArrayQueue);
                observer.onError(this.errors.terminate());
                return true;
            }
            if (!z2) {
                return false;
            }
            clear(this.queue);
            observer.onComplete();
            return true;
        }

        void clear(SpscLinkedArrayQueue<?> spscLinkedArrayQueue) {
            synchronized (this) {
                Arrays.fill(this.latest, (Object) null);
            }
            spscLinkedArrayQueue.clear();
        }

        void combine(T t, int i) {
            CombinerObserver<T, R> combinerObserver = this.observers[i];
            synchronized (this) {
                if (this.cancelled) {
                    return;
                }
                int length = this.latest.length;
                T t2 = this.latest[i];
                int i2 = this.active;
                if (t2 == null) {
                    i2++;
                    this.active = i2;
                }
                int i3 = this.complete;
                if (t == null) {
                    i3++;
                    this.complete = i3;
                } else {
                    this.latest[i] = t;
                }
                boolean z = false;
                boolean z2 = i2 == length;
                if (i3 == length || (t == null && t2 == null)) {
                    z = true;
                }
                if (z) {
                    this.done = true;
                } else if (t != null && z2) {
                    this.queue.offer(combinerObserver, this.latest.clone());
                } else if (t == null && this.errors.get() != null) {
                    this.done = true;
                }
                if (z2 || t == null) {
                    drain();
                }
            }
        }

        @Override // io.reactivex.disposables.Disposable
        public void dispose() {
            if (this.cancelled) {
                return;
            }
            this.cancelled = true;
            if (getAndIncrement() == 0) {
                cancel(this.queue);
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:23:0x003c, code lost:
        
            r10 = addAndGet(-r10);
         */
        /* JADX WARN: Code restructure failed: missing block: B:24:0x0041, code lost:
        
            if (r10 != 0) goto L31;
         */
        /* JADX WARN: Code restructure failed: missing block: B:26:0x0043, code lost:
        
            return;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        void drain() {
            /*
                r12 = this;
                int r0 = r12.getAndIncrement()
                if (r0 == 0) goto L7
                return
            L7:
                io.reactivex.internal.queue.SpscLinkedArrayQueue<java.lang.Object> r0 = r12.queue
                io.reactivex.Observer<? super R> r7 = r12.actual
                boolean r8 = r12.delayError
                r9 = 1
                r10 = 1
            Lf:
                boolean r2 = r12.done
                boolean r3 = r0.isEmpty()
                r1 = r12
                r4 = r7
                r5 = r0
                r6 = r8
                boolean r1 = r1.checkTerminated(r2, r3, r4, r5, r6)
                if (r1 == 0) goto L20
                return
            L20:
                boolean r2 = r12.done
                java.lang.Object r1 = r0.poll()
                io.reactivex.internal.operators.observable.ObservableCombineLatest$CombinerObserver r1 = (io.reactivex.internal.operators.observable.ObservableCombineLatest.CombinerObserver) r1
                if (r1 != 0) goto L2c
                r11 = 1
                goto L2e
            L2c:
                r1 = 0
                r11 = 0
            L2e:
                r1 = r12
                r3 = r11
                r4 = r7
                r5 = r0
                r6 = r8
                boolean r1 = r1.checkTerminated(r2, r3, r4, r5, r6)
                if (r1 == 0) goto L3a
                return
            L3a:
                if (r11 == 0) goto L44
                int r1 = -r10
                int r10 = r12.addAndGet(r1)
                if (r10 != 0) goto Lf
                return
            L44:
                java.lang.Object r1 = r0.poll()
                java.lang.Object[] r1 = (java.lang.Object[]) r1
                io.reactivex.functions.Function<? super java.lang.Object[], ? extends R> r2 = r12.combiner     // Catch: java.lang.Throwable -> L5a
                java.lang.Object r1 = r2.apply(r1)     // Catch: java.lang.Throwable -> L5a
                java.lang.String r2 = "The combiner returned a null"
                java.lang.Object r1 = io.reactivex.internal.functions.ObjectHelper.requireNonNull(r1, r2)     // Catch: java.lang.Throwable -> L5a
                r7.onNext(r1)
                goto L20
            L5a:
                r1 = move-exception
                io.reactivex.exceptions.Exceptions.throwIfFatal(r1)
                r12.cancelled = r9
                r12.cancel(r0)
                r7.onError(r1)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.observable.ObservableCombineLatest.LatestCoordinator.drain():void");
        }

        @Override // io.reactivex.disposables.Disposable
        public boolean isDisposed() {
            return this.cancelled;
        }

        void onError(Throwable th) {
            if (this.errors.addThrowable(th)) {
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void subscribe(ObservableSource<? extends T>[] observableSourceArr) {
            CombinerObserver<T, R>[] combinerObserverArr = this.observers;
            int length = combinerObserverArr.length;
            for (int i = 0; i < length; i++) {
                combinerObserverArr[i] = new CombinerObserver<>(this, i);
            }
            lazySet(0);
            this.actual.onSubscribe(this);
            for (int i2 = 0; i2 < length && !this.done && !this.cancelled; i2++) {
                observableSourceArr[i2].subscribe(combinerObserverArr[i2]);
            }
        }
    }

    public ObservableCombineLatest(ObservableSource<? extends T>[] observableSourceArr, Iterable<? extends ObservableSource<? extends T>> iterable, Function<? super Object[], ? extends R> function, int i, boolean z) {
        this.sources = observableSourceArr;
        this.sourcesIterable = iterable;
        this.combiner = function;
        this.bufferSize = i;
        this.delayError = z;
    }

    @Override // io.reactivex.Observable
    public void subscribeActual(Observer<? super R> observer) {
        int length;
        ObservableSource<? extends T>[] observableSourceArr = this.sources;
        if (observableSourceArr == null) {
            ObservableSource<? extends T>[] observableSourceArr2 = new Observable[8];
            int i = 0;
            for (ObservableSource<? extends T> observableSource : this.sourcesIterable) {
                if (i == observableSourceArr2.length) {
                    ObservableSource<? extends T>[] observableSourceArr3 = new ObservableSource[(i >> 2) + i];
                    System.arraycopy(observableSourceArr2, 0, observableSourceArr3, 0, i);
                    observableSourceArr2 = observableSourceArr3;
                }
                observableSourceArr2[i] = observableSource;
                i++;
            }
            length = i;
            observableSourceArr = observableSourceArr2;
        } else {
            length = observableSourceArr.length;
        }
        if (length == 0) {
            EmptyDisposable.complete(observer);
        } else {
            new LatestCoordinator(observer, this.combiner, length, this.bufferSize, this.delayError).subscribe(observableSourceArr);
        }
    }
}
