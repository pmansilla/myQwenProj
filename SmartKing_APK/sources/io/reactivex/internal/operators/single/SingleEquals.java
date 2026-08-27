package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes2.dex */
public final class SingleEquals<T> extends Single<Boolean> {
    final SingleSource<? extends T> first;
    final SingleSource<? extends T> second;

    public SingleEquals(SingleSource<? extends T> singleSource, SingleSource<? extends T> singleSource2) {
        this.first = singleSource;
        this.second = singleSource2;
    }

    @Override // io.reactivex.Single
    protected void subscribeActual(SingleObserver<? super Boolean> singleObserver) {
        AtomicInteger atomicInteger = new AtomicInteger();
        Object[] objArr = {null, null};
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        singleObserver.onSubscribe(compositeDisposable);
        this.first.subscribe(new SingleObserver<T>(0, compositeDisposable, objArr, atomicInteger, singleObserver) { // from class: io.reactivex.internal.operators.single.SingleEquals.1InnerObserver
            final int index;
            final /* synthetic */ AtomicInteger val$count;
            final /* synthetic */ SingleObserver val$s;
            final /* synthetic */ CompositeDisposable val$set;
            final /* synthetic */ Object[] val$values;

            {
                this.val$set = compositeDisposable;
                this.val$values = objArr;
                this.val$count = atomicInteger;
                this.val$s = singleObserver;
                this.index = r2;
            }

            @Override // io.reactivex.SingleObserver
            public void onError(Throwable th) {
                int i;
                do {
                    i = this.val$count.get();
                    if (i >= 2) {
                        RxJavaPlugins.onError(th);
                        return;
                    }
                } while (!this.val$count.compareAndSet(i, 2));
                this.val$set.dispose();
                this.val$s.onError(th);
            }

            @Override // io.reactivex.SingleObserver
            public void onSubscribe(Disposable disposable) {
                this.val$set.add(disposable);
            }

            @Override // io.reactivex.SingleObserver
            public void onSuccess(T t) {
                this.val$values[this.index] = t;
                if (this.val$count.incrementAndGet() == 2) {
                    this.val$s.onSuccess(Boolean.valueOf(ObjectHelper.equals(this.val$values[0], this.val$values[1])));
                }
            }
        });
        this.second.subscribe(new SingleObserver<T>(1, compositeDisposable, objArr, atomicInteger, singleObserver) { // from class: io.reactivex.internal.operators.single.SingleEquals.1InnerObserver
            final int index;
            final /* synthetic */ AtomicInteger val$count;
            final /* synthetic */ SingleObserver val$s;
            final /* synthetic */ CompositeDisposable val$set;
            final /* synthetic */ Object[] val$values;

            {
                this.val$set = compositeDisposable;
                this.val$values = objArr;
                this.val$count = atomicInteger;
                this.val$s = singleObserver;
                this.index = r2;
            }

            @Override // io.reactivex.SingleObserver
            public void onError(Throwable th) {
                int i;
                do {
                    i = this.val$count.get();
                    if (i >= 2) {
                        RxJavaPlugins.onError(th);
                        return;
                    }
                } while (!this.val$count.compareAndSet(i, 2));
                this.val$set.dispose();
                this.val$s.onError(th);
            }

            @Override // io.reactivex.SingleObserver
            public void onSubscribe(Disposable disposable) {
                this.val$set.add(disposable);
            }

            @Override // io.reactivex.SingleObserver
            public void onSuccess(T t) {
                this.val$values[this.index] = t;
                if (this.val$count.incrementAndGet() == 2) {
                    this.val$s.onSuccess(Boolean.valueOf(ObjectHelper.equals(this.val$values[0], this.val$values[1])));
                }
            }
        });
    }
}
