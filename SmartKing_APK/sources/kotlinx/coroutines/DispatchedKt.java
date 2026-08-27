package kotlinx.coroutines;

import java.util.concurrent.CancellationException;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
import kotlinx.coroutines.internal.Symbol;
import kotlinx.coroutines.internal.ThreadContextKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: Dispatched.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000N\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0003\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\"\u0010\u0004\u001a\u00020\u0005\"\u0004\b\u0000\u0010\u0006*\b\u0012\u0004\u0012\u0002H\u00060\u00072\b\b\u0002\u0010\b\u001a\u00020\tH\u0000\u001a;\u0010\n\u001a\u00020\u000b*\u0006\u0012\u0002\b\u00030\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\u000f\u001a\u00020\u000b2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00050\u0011H\u0082\b\u001a.\u0010\u0012\u001a\u00020\u0005\"\u0004\b\u0000\u0010\u0006*\b\u0012\u0004\u0012\u0002H\u00060\u00072\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u00060\u00142\u0006\u0010\u0015\u001a\u00020\tH\u0000\u001a%\u0010\u0016\u001a\u00020\u0005\"\u0004\b\u0000\u0010\u0006*\b\u0012\u0004\u0012\u0002H\u00060\u00142\u0006\u0010\u0017\u001a\u0002H\u0006H\u0000¢\u0006\u0002\u0010\u0018\u001a \u0010\u0019\u001a\u00020\u0005\"\u0004\b\u0000\u0010\u0006*\b\u0012\u0004\u0012\u0002H\u00060\u00142\u0006\u0010\u001a\u001a\u00020\u001bH\u0000\u001a%\u0010\u001c\u001a\u00020\u0005\"\u0004\b\u0000\u0010\u0006*\b\u0012\u0004\u0012\u0002H\u00060\u00142\u0006\u0010\u0017\u001a\u0002H\u0006H\u0000¢\u0006\u0002\u0010\u0018\u001a \u0010\u001d\u001a\u00020\u0005\"\u0004\b\u0000\u0010\u0006*\b\u0012\u0004\u0012\u0002H\u00060\u00142\u0006\u0010\u001a\u001a\u00020\u001bH\u0000\u001a\u0010\u0010\u001e\u001a\u00020\u0005*\u0006\u0012\u0002\b\u00030\u0007H\u0002\u001a\u0019\u0010\u001f\u001a\u00020\u0005*\u0006\u0012\u0002\b\u00030\u00142\u0006\u0010\u001a\u001a\u00020\u001bH\u0080\b\u001a'\u0010 \u001a\u00020\u0005*\u0006\u0012\u0002\b\u00030\u00072\u0006\u0010!\u001a\u00020\"2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00050\u0011H\u0082\b\u001a\u0012\u0010#\u001a\u00020\u000b*\b\u0012\u0004\u0012\u00020\u00050\fH\u0000\"\u0016\u0010\u0000\u001a\u00020\u00018\u0002X\u0083\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0002\u0010\u0003¨\u0006$"}, d2 = {"UNDEFINED", "Lkotlinx/coroutines/internal/Symbol;", "UNDEFINED$annotations", "()V", "dispatch", "", "T", "Lkotlinx/coroutines/DispatchedTask;", "mode", "", "executeUnconfined", "", "Lkotlinx/coroutines/DispatchedContinuation;", "contState", "", "doYield", "block", "Lkotlin/Function0;", "resume", "delegate", "Lkotlin/coroutines/Continuation;", "useMode", "resumeCancellable", "value", "(Lkotlin/coroutines/Continuation;Ljava/lang/Object;)V", "resumeCancellableWithException", "exception", "", "resumeDirect", "resumeDirectWithException", "resumeUnconfined", "resumeWithStackTrace", "runUnconfinedEventLoop", "eventLoop", "Lkotlinx/coroutines/EventLoop;", "yieldUndispatched", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final class DispatchedKt {
    private static final Symbol UNDEFINED = new Symbol("UNDEFINED");

    private static /* synthetic */ void UNDEFINED$annotations() {
    }

    public static final /* synthetic */ Symbol access$getUNDEFINED$p() {
        return UNDEFINED;
    }

    public static final <T> void dispatch(@NotNull DispatchedTask<? super T> dispatch, int i) {
        Intrinsics.checkParameterIsNotNull(dispatch, "$this$dispatch");
        Continuation<? super T> delegate$kotlinx_coroutines_core = dispatch.getDelegate$kotlinx_coroutines_core();
        if (!ResumeModeKt.isDispatchedMode(i) || !(delegate$kotlinx_coroutines_core instanceof DispatchedContinuation) || ResumeModeKt.isCancellableMode(i) != ResumeModeKt.isCancellableMode(dispatch.resumeMode)) {
            resume(dispatch, delegate$kotlinx_coroutines_core, i);
            return;
        }
        CoroutineDispatcher coroutineDispatcher = ((DispatchedContinuation) delegate$kotlinx_coroutines_core).dispatcher;
        CoroutineContext context = delegate$kotlinx_coroutines_core.getContext();
        if (coroutineDispatcher.isDispatchNeeded(context)) {
            coroutineDispatcher.mo931dispatch(context, dispatch);
        } else {
            resumeUnconfined(dispatch);
        }
    }

    public static /* synthetic */ void dispatch$default(DispatchedTask dispatchedTask, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = 1;
        }
        dispatch(dispatchedTask, i);
    }

    private static final boolean executeUnconfined(@NotNull DispatchedContinuation<?> dispatchedContinuation, Object obj, int i, boolean z, Function0<Unit> function0) {
        EventLoop eventLoop$kotlinx_coroutines_core = ThreadLocalEventLoop.INSTANCE.getEventLoop$kotlinx_coroutines_core();
        if (z && eventLoop$kotlinx_coroutines_core.isUnconfinedQueueEmpty()) {
            return false;
        }
        if (eventLoop$kotlinx_coroutines_core.isUnconfinedLoopActive()) {
            dispatchedContinuation._state = obj;
            dispatchedContinuation.resumeMode = i;
            eventLoop$kotlinx_coroutines_core.dispatchUnconfined(dispatchedContinuation);
            return true;
        }
        DispatchedContinuation<?> dispatchedContinuation2 = dispatchedContinuation;
        eventLoop$kotlinx_coroutines_core.incrementUseCount(true);
        try {
            try {
                function0.invoke();
                do {
                } while (eventLoop$kotlinx_coroutines_core.processUnconfinedEvent());
                InlineMarker.finallyStart(1);
            } catch (Throwable th) {
                dispatchedContinuation2.handleFatalException$kotlinx_coroutines_core(th, null);
                InlineMarker.finallyStart(1);
            }
            eventLoop$kotlinx_coroutines_core.decrementUseCount(true);
            InlineMarker.finallyEnd(1);
            return false;
        } catch (Throwable th2) {
            InlineMarker.finallyStart(1);
            eventLoop$kotlinx_coroutines_core.decrementUseCount(true);
            InlineMarker.finallyEnd(1);
            throw th2;
        }
    }

    static /* synthetic */ boolean executeUnconfined$default(DispatchedContinuation dispatchedContinuation, Object obj, int i, boolean z, Function0 function0, int i2, Object obj2) {
        if ((i2 & 4) != 0) {
            z = false;
        }
        EventLoop eventLoop$kotlinx_coroutines_core = ThreadLocalEventLoop.INSTANCE.getEventLoop$kotlinx_coroutines_core();
        if (z && eventLoop$kotlinx_coroutines_core.isUnconfinedQueueEmpty()) {
            return false;
        }
        if (eventLoop$kotlinx_coroutines_core.isUnconfinedLoopActive()) {
            dispatchedContinuation._state = obj;
            dispatchedContinuation.resumeMode = i;
            eventLoop$kotlinx_coroutines_core.dispatchUnconfined(dispatchedContinuation);
            return true;
        }
        DispatchedContinuation dispatchedContinuation2 = dispatchedContinuation;
        eventLoop$kotlinx_coroutines_core.incrementUseCount(true);
        try {
            try {
                function0.invoke();
                do {
                } while (eventLoop$kotlinx_coroutines_core.processUnconfinedEvent());
                InlineMarker.finallyStart(1);
            } catch (Throwable th) {
                dispatchedContinuation2.handleFatalException$kotlinx_coroutines_core(th, null);
                InlineMarker.finallyStart(1);
            }
            eventLoop$kotlinx_coroutines_core.decrementUseCount(true);
            InlineMarker.finallyEnd(1);
            return false;
        } catch (Throwable th2) {
            InlineMarker.finallyStart(1);
            eventLoop$kotlinx_coroutines_core.decrementUseCount(true);
            InlineMarker.finallyEnd(1);
            throw th2;
        }
    }

    public static final <T> void resume(@NotNull DispatchedTask<? super T> resume, @NotNull Continuation<? super T> delegate, int i) {
        Intrinsics.checkParameterIsNotNull(resume, "$this$resume");
        Intrinsics.checkParameterIsNotNull(delegate, "delegate");
        Object takeState$kotlinx_coroutines_core = resume.takeState$kotlinx_coroutines_core();
        Throwable exceptionalResult$kotlinx_coroutines_core = resume.getExceptionalResult$kotlinx_coroutines_core(takeState$kotlinx_coroutines_core);
        if (exceptionalResult$kotlinx_coroutines_core != null) {
            ResumeModeKt.resumeWithExceptionMode(delegate, exceptionalResult$kotlinx_coroutines_core, i);
        } else {
            ResumeModeKt.resumeMode(delegate, resume.getSuccessfulResult$kotlinx_coroutines_core(takeState$kotlinx_coroutines_core), i);
        }
    }

    public static final <T> void resumeCancellable(@NotNull Continuation<? super T> resumeCancellable, T t) {
        boolean z;
        Intrinsics.checkParameterIsNotNull(resumeCancellable, "$this$resumeCancellable");
        if (!(resumeCancellable instanceof DispatchedContinuation)) {
            Result.Companion companion = Result.INSTANCE;
            resumeCancellable.resumeWith(Result.m33constructorimpl(t));
            return;
        }
        DispatchedContinuation dispatchedContinuation = (DispatchedContinuation) resumeCancellable;
        if (dispatchedContinuation.dispatcher.isDispatchNeeded(dispatchedContinuation.getContext())) {
            dispatchedContinuation._state = t;
            dispatchedContinuation.resumeMode = 1;
            dispatchedContinuation.dispatcher.mo931dispatch(dispatchedContinuation.getContext(), dispatchedContinuation);
            return;
        }
        EventLoop eventLoop$kotlinx_coroutines_core = ThreadLocalEventLoop.INSTANCE.getEventLoop$kotlinx_coroutines_core();
        if (eventLoop$kotlinx_coroutines_core.isUnconfinedLoopActive()) {
            dispatchedContinuation._state = t;
            dispatchedContinuation.resumeMode = 1;
            eventLoop$kotlinx_coroutines_core.dispatchUnconfined(dispatchedContinuation);
            return;
        }
        DispatchedContinuation dispatchedContinuation2 = dispatchedContinuation;
        eventLoop$kotlinx_coroutines_core.incrementUseCount(true);
        try {
            try {
                Job job = (Job) dispatchedContinuation.getContext().get(Job.INSTANCE);
                if (job == null || job.isActive()) {
                    z = false;
                } else {
                    CancellationException cancellationException = job.getCancellationException();
                    Result.Companion companion2 = Result.INSTANCE;
                    dispatchedContinuation.resumeWith(Result.m33constructorimpl(ResultKt.createFailure(cancellationException)));
                    z = true;
                }
                if (!z) {
                    CoroutineContext context = dispatchedContinuation.getContext();
                    Object updateThreadContext = ThreadContextKt.updateThreadContext(context, dispatchedContinuation.countOrElement);
                    try {
                        Continuation<T> continuation = dispatchedContinuation.continuation;
                        Result.Companion companion3 = Result.INSTANCE;
                        continuation.resumeWith(Result.m33constructorimpl(t));
                        Unit unit = Unit.INSTANCE;
                        ThreadContextKt.restoreThreadContext(context, updateThreadContext);
                    } catch (Throwable th) {
                        ThreadContextKt.restoreThreadContext(context, updateThreadContext);
                        throw th;
                    }
                }
                do {
                } while (eventLoop$kotlinx_coroutines_core.processUnconfinedEvent());
            } catch (Throwable th2) {
                dispatchedContinuation2.handleFatalException$kotlinx_coroutines_core(th2, null);
            }
        } finally {
            eventLoop$kotlinx_coroutines_core.decrementUseCount(true);
        }
    }

    public static final <T> void resumeCancellableWithException(@NotNull Continuation<? super T> resumeCancellableWithException, @NotNull Throwable exception) {
        Intrinsics.checkParameterIsNotNull(resumeCancellableWithException, "$this$resumeCancellableWithException");
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        if (!(resumeCancellableWithException instanceof DispatchedContinuation)) {
            Result.Companion companion = Result.INSTANCE;
            resumeCancellableWithException.resumeWith(Result.m33constructorimpl(ResultKt.createFailure(StackTraceRecoveryKt.recoverStackTrace(exception, resumeCancellableWithException))));
            return;
        }
        DispatchedContinuation dispatchedContinuation = (DispatchedContinuation) resumeCancellableWithException;
        CoroutineContext context = dispatchedContinuation.continuation.getContext();
        boolean z = false;
        CompletedExceptionally completedExceptionally = new CompletedExceptionally(exception, false, 2, null);
        if (dispatchedContinuation.dispatcher.isDispatchNeeded(context)) {
            dispatchedContinuation._state = new CompletedExceptionally(exception, false, 2, null);
            dispatchedContinuation.resumeMode = 1;
            dispatchedContinuation.dispatcher.mo931dispatch(context, dispatchedContinuation);
            return;
        }
        EventLoop eventLoop$kotlinx_coroutines_core = ThreadLocalEventLoop.INSTANCE.getEventLoop$kotlinx_coroutines_core();
        if (eventLoop$kotlinx_coroutines_core.isUnconfinedLoopActive()) {
            dispatchedContinuation._state = completedExceptionally;
            dispatchedContinuation.resumeMode = 1;
            eventLoop$kotlinx_coroutines_core.dispatchUnconfined(dispatchedContinuation);
            return;
        }
        DispatchedContinuation dispatchedContinuation2 = dispatchedContinuation;
        eventLoop$kotlinx_coroutines_core.incrementUseCount(true);
        try {
            try {
                Job job = (Job) dispatchedContinuation.getContext().get(Job.INSTANCE);
                if (job != null && !job.isActive()) {
                    CancellationException cancellationException = job.getCancellationException();
                    Result.Companion companion2 = Result.INSTANCE;
                    dispatchedContinuation.resumeWith(Result.m33constructorimpl(ResultKt.createFailure(cancellationException)));
                    z = true;
                }
                if (!z) {
                    CoroutineContext context2 = dispatchedContinuation.getContext();
                    Object updateThreadContext = ThreadContextKt.updateThreadContext(context2, dispatchedContinuation.countOrElement);
                    try {
                        Continuation<T> continuation = dispatchedContinuation.continuation;
                        Result.Companion companion3 = Result.INSTANCE;
                        continuation.resumeWith(Result.m33constructorimpl(ResultKt.createFailure(StackTraceRecoveryKt.recoverStackTrace(exception, continuation))));
                        Unit unit = Unit.INSTANCE;
                        ThreadContextKt.restoreThreadContext(context2, updateThreadContext);
                    } catch (Throwable th) {
                        ThreadContextKt.restoreThreadContext(context2, updateThreadContext);
                        throw th;
                    }
                }
                do {
                } while (eventLoop$kotlinx_coroutines_core.processUnconfinedEvent());
            } finally {
                eventLoop$kotlinx_coroutines_core.decrementUseCount(true);
            }
        } catch (Throwable th2) {
            dispatchedContinuation2.handleFatalException$kotlinx_coroutines_core(th2, null);
        }
    }

    public static final <T> void resumeDirect(@NotNull Continuation<? super T> resumeDirect, T t) {
        Intrinsics.checkParameterIsNotNull(resumeDirect, "$this$resumeDirect");
        if (!(resumeDirect instanceof DispatchedContinuation)) {
            Result.Companion companion = Result.INSTANCE;
            resumeDirect.resumeWith(Result.m33constructorimpl(t));
        } else {
            Continuation<T> continuation = ((DispatchedContinuation) resumeDirect).continuation;
            Result.Companion companion2 = Result.INSTANCE;
            continuation.resumeWith(Result.m33constructorimpl(t));
        }
    }

    public static final <T> void resumeDirectWithException(@NotNull Continuation<? super T> resumeDirectWithException, @NotNull Throwable exception) {
        Intrinsics.checkParameterIsNotNull(resumeDirectWithException, "$this$resumeDirectWithException");
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        if (!(resumeDirectWithException instanceof DispatchedContinuation)) {
            Result.Companion companion = Result.INSTANCE;
            resumeDirectWithException.resumeWith(Result.m33constructorimpl(ResultKt.createFailure(StackTraceRecoveryKt.recoverStackTrace(exception, resumeDirectWithException))));
        } else {
            Continuation<T> continuation = ((DispatchedContinuation) resumeDirectWithException).continuation;
            Result.Companion companion2 = Result.INSTANCE;
            continuation.resumeWith(Result.m33constructorimpl(ResultKt.createFailure(StackTraceRecoveryKt.recoverStackTrace(exception, continuation))));
        }
    }

    private static final void resumeUnconfined(@NotNull DispatchedTask<?> dispatchedTask) {
        EventLoop eventLoop$kotlinx_coroutines_core = ThreadLocalEventLoop.INSTANCE.getEventLoop$kotlinx_coroutines_core();
        if (eventLoop$kotlinx_coroutines_core.isUnconfinedLoopActive()) {
            eventLoop$kotlinx_coroutines_core.dispatchUnconfined(dispatchedTask);
            return;
        }
        eventLoop$kotlinx_coroutines_core.incrementUseCount(true);
        try {
            try {
                resume(dispatchedTask, dispatchedTask.getDelegate$kotlinx_coroutines_core(), 3);
                do {
                } while (eventLoop$kotlinx_coroutines_core.processUnconfinedEvent());
            } catch (Throwable th) {
                dispatchedTask.handleFatalException$kotlinx_coroutines_core(th, null);
            }
        } finally {
            eventLoop$kotlinx_coroutines_core.decrementUseCount(true);
        }
    }

    public static final void resumeWithStackTrace(@NotNull Continuation<?> resumeWithStackTrace, @NotNull Throwable exception) {
        Intrinsics.checkParameterIsNotNull(resumeWithStackTrace, "$this$resumeWithStackTrace");
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        Result.Companion companion = Result.INSTANCE;
        resumeWithStackTrace.resumeWith(Result.m33constructorimpl(ResultKt.createFailure(StackTraceRecoveryKt.recoverStackTrace(exception, resumeWithStackTrace))));
    }

    public static final void runUnconfinedEventLoop(@NotNull DispatchedTask<?> dispatchedTask, EventLoop eventLoop, Function0<Unit> function0) {
        eventLoop.incrementUseCount(true);
        try {
            try {
                function0.invoke();
                do {
                } while (eventLoop.processUnconfinedEvent());
                InlineMarker.finallyStart(1);
            } catch (Throwable th) {
                dispatchedTask.handleFatalException$kotlinx_coroutines_core(th, null);
                InlineMarker.finallyStart(1);
            }
            eventLoop.decrementUseCount(true);
            InlineMarker.finallyEnd(1);
        } catch (Throwable th2) {
            InlineMarker.finallyStart(1);
            eventLoop.decrementUseCount(true);
            InlineMarker.finallyEnd(1);
            throw th2;
        }
    }

    public static final boolean yieldUndispatched(@NotNull DispatchedContinuation<? super Unit> yieldUndispatched) {
        Intrinsics.checkParameterIsNotNull(yieldUndispatched, "$this$yieldUndispatched");
        Unit unit = Unit.INSTANCE;
        EventLoop eventLoop$kotlinx_coroutines_core = ThreadLocalEventLoop.INSTANCE.getEventLoop$kotlinx_coroutines_core();
        if (eventLoop$kotlinx_coroutines_core.isUnconfinedQueueEmpty()) {
            return false;
        }
        if (eventLoop$kotlinx_coroutines_core.isUnconfinedLoopActive()) {
            yieldUndispatched._state = unit;
            yieldUndispatched.resumeMode = 1;
            eventLoop$kotlinx_coroutines_core.dispatchUnconfined(yieldUndispatched);
            return true;
        }
        DispatchedContinuation<? super Unit> dispatchedContinuation = yieldUndispatched;
        eventLoop$kotlinx_coroutines_core.incrementUseCount(true);
        try {
            try {
                yieldUndispatched.run();
                do {
                } while (eventLoop$kotlinx_coroutines_core.processUnconfinedEvent());
            } catch (Throwable th) {
                dispatchedContinuation.handleFatalException$kotlinx_coroutines_core(th, null);
            }
            return false;
        } finally {
            eventLoop$kotlinx_coroutines_core.decrementUseCount(true);
        }
    }
}
