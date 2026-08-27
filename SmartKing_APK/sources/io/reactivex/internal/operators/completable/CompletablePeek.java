package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;

/* loaded from: classes2.dex */
public final class CompletablePeek extends Completable {
    final Action onAfterTerminate;
    final Action onComplete;
    final Action onDispose;
    final Consumer<? super Throwable> onError;
    final Consumer<? super Disposable> onSubscribe;
    final Action onTerminate;
    final CompletableSource source;

    public CompletablePeek(CompletableSource completableSource, Consumer<? super Disposable> consumer, Consumer<? super Throwable> consumer2, Action action, Action action2, Action action3, Action action4) {
        this.source = completableSource;
        this.onSubscribe = consumer;
        this.onError = consumer2;
        this.onComplete = action;
        this.onTerminate = action2;
        this.onAfterTerminate = action3;
        this.onDispose = action4;
    }

    @Override // io.reactivex.Completable
    protected void subscribeActual(final CompletableObserver completableObserver) {
        this.source.subscribe(new CompletableObserver() { // from class: io.reactivex.internal.operators.completable.CompletablePeek.1
            void doAfter() {
                try {
                    CompletablePeek.this.onAfterTerminate.run();
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    RxJavaPlugins.onError(th);
                }
            }

            @Override // io.reactivex.CompletableObserver, io.reactivex.MaybeObserver
            public void onComplete() {
                try {
                    CompletablePeek.this.onComplete.run();
                    CompletablePeek.this.onTerminate.run();
                    completableObserver.onComplete();
                    doAfter();
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    completableObserver.onError(th);
                }
            }

            @Override // io.reactivex.CompletableObserver
            public void onError(Throwable th) {
                try {
                    CompletablePeek.this.onError.accept(th);
                    CompletablePeek.this.onTerminate.run();
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    th = new CompositeException(th, th2);
                }
                completableObserver.onError(th);
                doAfter();
            }

            @Override // io.reactivex.CompletableObserver
            public void onSubscribe(final Disposable disposable) {
                try {
                    CompletablePeek.this.onSubscribe.accept(disposable);
                    completableObserver.onSubscribe(Disposables.fromRunnable(new Runnable() { // from class: io.reactivex.internal.operators.completable.CompletablePeek.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            try {
                                CompletablePeek.this.onDispose.run();
                            } catch (Throwable th) {
                                Exceptions.throwIfFatal(th);
                                RxJavaPlugins.onError(th);
                            }
                            disposable.dispose();
                        }
                    }));
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    disposable.dispose();
                    EmptyDisposable.error(th, completableObserver);
                }
            }
        });
    }
}
