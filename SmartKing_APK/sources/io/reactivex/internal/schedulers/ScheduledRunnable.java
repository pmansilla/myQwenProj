package io.reactivex.internal.schedulers;

import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableContainer;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReferenceArray;

/* loaded from: classes2.dex */
public final class ScheduledRunnable extends AtomicReferenceArray<Object> implements Runnable, Callable<Object>, Disposable {
    static final Object DISPOSED = new Object();
    static final Object DONE = new Object();
    static final int FUTURE_INDEX = 1;
    static final int PARENT_INDEX = 0;
    private static final long serialVersionUID = -6120223772001106981L;
    final Runnable actual;

    public ScheduledRunnable(Runnable runnable, DisposableContainer disposableContainer) {
        super(2);
        this.actual = runnable;
        lazySet(0, disposableContainer);
    }

    @Override // java.util.concurrent.Callable
    public Object call() {
        run();
        return null;
    }

    @Override // io.reactivex.disposables.Disposable
    public void dispose() {
        Object obj;
        while (true) {
            Object obj2 = get(1);
            if (obj2 == DONE || obj2 == DISPOSED) {
                break;
            } else if (compareAndSet(1, obj2, DISPOSED)) {
                if (obj2 != null) {
                    ((Future) obj2).cancel(true);
                }
            }
        }
        do {
            obj = get(0);
            if (obj == DONE || obj == DISPOSED || obj == null) {
                return;
            }
        } while (!compareAndSet(0, obj, DISPOSED));
        ((DisposableContainer) obj).delete(this);
    }

    @Override // io.reactivex.disposables.Disposable
    public boolean isDisposed() {
        Object obj = get(1);
        return obj == DISPOSED || obj == DONE;
    }

    @Override // java.lang.Runnable
    public void run() {
        Object obj;
        Object obj2;
        try {
            try {
                this.actual.run();
            } catch (Throwable th) {
                RxJavaPlugins.onError(th);
            }
            Object obj3 = get(0);
            if (obj3 != DISPOSED && obj3 != null && compareAndSet(0, obj3, DONE)) {
                ((DisposableContainer) obj3).delete(this);
            }
            do {
                obj2 = get(1);
                if (obj2 == DISPOSED) {
                    return;
                }
            } while (!compareAndSet(1, obj2, DONE));
        } catch (Throwable th2) {
            Object obj4 = get(0);
            if (obj4 != DISPOSED && obj4 != null && compareAndSet(0, obj4, DONE)) {
                ((DisposableContainer) obj4).delete(this);
            }
            do {
                obj = get(1);
                if (obj == DISPOSED) {
                    break;
                }
            } while (!compareAndSet(1, obj, DONE));
            throw th2;
        }
    }

    public void setFuture(Future<?> future) {
        Object obj;
        do {
            obj = get(1);
            if (obj == DONE) {
                return;
            }
            if (obj == DISPOSED) {
                future.cancel(true);
                return;
            }
        } while (!compareAndSet(1, obj, future));
    }
}
