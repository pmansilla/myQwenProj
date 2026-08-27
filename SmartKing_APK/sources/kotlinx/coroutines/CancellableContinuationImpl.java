package kotlinx.coroutines;

import com.sun.mail.imap.IMAPStore;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: CancellableContinuationImpl.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000´\u0001\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0003\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0011\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\u00060\u0004j\u0002`\u0005B\u001b\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u0012\u0010#\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010\u000fH\u0002J\u0012\u0010&\u001a\u00020\u001a2\b\u0010'\u001a\u0004\u0018\u00010(H\u0016J\u001f\u0010)\u001a\u00020$2\b\u0010 \u001a\u0004\u0018\u00010\u000f2\u0006\u0010'\u001a\u00020(H\u0010¢\u0006\u0002\b*J\u0010\u0010+\u001a\u00020$2\u0006\u0010,\u001a\u00020\u000fH\u0016J\u0010\u0010-\u001a\u00020$2\u0006\u0010.\u001a\u00020\tH\u0002J\b\u0010/\u001a\u00020$H\u0002J\u0010\u00100\u001a\u00020(2\u0006\u00101\u001a\u000202H\u0016J\n\u00103\u001a\u0004\u0018\u00010\u000fH\u0001J\u0010\u00104\u001a\n\u0018\u000105j\u0004\u0018\u0001`6H\u0016J\u001f\u00107\u001a\u0002H\u0001\"\u0004\b\u0001\u0010\u00012\b\u0010 \u001a\u0004\u0018\u00010\u000fH\u0010¢\u0006\u0004\b8\u00109J\b\u0010:\u001a\u00020$H\u0016J\b\u0010;\u001a\u00020$H\u0002J\u0017\u0010<\u001a\u00020$2\f\u0010=\u001a\b\u0012\u0004\u0012\u00020$0>H\u0082\bJ1\u0010?\u001a\u00020$2'\u0010@\u001a#\u0012\u0015\u0012\u0013\u0018\u00010(¢\u0006\f\bB\u0012\b\bC\u0012\u0004\b\b('\u0012\u0004\u0012\u00020$0Aj\u0002`DH\u0016J1\u0010E\u001a\u00020F2'\u0010@\u001a#\u0012\u0015\u0012\u0013\u0018\u00010(¢\u0006\f\bB\u0012\b\bC\u0012\u0004\b\b('\u0012\u0004\u0012\u00020$0Aj\u0002`DH\u0002J;\u0010G\u001a\u00020$2'\u0010@\u001a#\u0012\u0015\u0012\u0013\u0018\u00010(¢\u0006\f\bB\u0012\b\bC\u0012\u0004\b\b('\u0012\u0004\u0012\u00020$0Aj\u0002`D2\b\u0010 \u001a\u0004\u0018\u00010\u000fH\u0002J\b\u0010H\u001a\u00020IH\u0014J8\u0010J\u001a\u00020$2\u0006\u0010K\u001a\u00028\u00002!\u0010L\u001a\u001d\u0012\u0013\u0012\u00110(¢\u0006\f\bB\u0012\b\bC\u0012\u0004\b\b('\u0012\u0004\u0012\u00020$0AH\u0016¢\u0006\u0002\u0010MJ\u001c\u0010N\u001a\u0004\u0018\u00010O2\b\u0010%\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\b\u001a\u00020\tH\u0002J\u001e\u0010P\u001a\u00020$2\f\u0010Q\u001a\b\u0012\u0004\u0012\u00028\u00000RH\u0016ø\u0001\u0000¢\u0006\u0002\u0010SJ\u001f\u0010T\u001a\u0004\u0018\u00010O2\u0006\u0010U\u001a\u00020(2\u0006\u0010.\u001a\u00020\tH\u0000¢\u0006\u0002\bVJ\u000f\u0010W\u001a\u0004\u0018\u00010\u000fH\u0010¢\u0006\u0002\bXJ\b\u0010Y\u001a\u00020IH\u0016J\b\u0010Z\u001a\u00020\u001aH\u0002J!\u0010Z\u001a\u0004\u0018\u00010\u000f2\u0006\u0010K\u001a\u00028\u00002\b\u0010[\u001a\u0004\u0018\u00010\u000fH\u0016¢\u0006\u0002\u0010\\J\u0012\u0010]\u001a\u0004\u0018\u00010\u000f2\u0006\u0010U\u001a\u00020(H\u0016J\b\u0010^\u001a\u00020\u001aH\u0002J\u0019\u0010_\u001a\u00020$*\u00020`2\u0006\u0010K\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010aJ\u0014\u0010b\u001a\u00020$*\u00020`2\u0006\u0010U\u001a\u00020(H\u0016R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u0010\u001a\n\u0018\u00010\u0004j\u0004\u0018\u0001`\u00058VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012R\u0014\u0010\u0013\u001a\u00020\u0014X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u001a\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u0007X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0014\u0010\u0019\u001a\u00020\u001a8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u001bR\u0014\u0010\u001c\u001a\u00020\u001a8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u001c\u0010\u001bR\u0014\u0010\u001d\u001a\u00020\u001a8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u001d\u0010\u001bR\u0010\u0010\u001e\u001a\u0004\u0018\u00010\u001fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0016\u0010 \u001a\u0004\u0018\u00010\u000f8@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b!\u0010\"\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006c"}, d2 = {"Lkotlinx/coroutines/CancellableContinuationImpl;", "T", "Lkotlinx/coroutines/DispatchedTask;", "Lkotlinx/coroutines/CancellableContinuation;", "Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "Lkotlinx/coroutines/internal/CoroutineStackFrame;", "delegate", "Lkotlin/coroutines/Continuation;", "resumeMode", "", "(Lkotlin/coroutines/Continuation;I)V", "_decision", "Lkotlinx/atomicfu/AtomicInt;", "_state", "Lkotlinx/atomicfu/AtomicRef;", "", "callerFrame", "getCallerFrame", "()Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "getDelegate$kotlinx_coroutines_core", "()Lkotlin/coroutines/Continuation;", "isActive", "", "()Z", "isCancelled", "isCompleted", "parentHandle", "Lkotlinx/coroutines/DisposableHandle;", "state", "getState$kotlinx_coroutines_core", "()Ljava/lang/Object;", "alreadyResumedError", "", "proposedUpdate", "cancel", "cause", "", "cancelResult", "cancelResult$kotlinx_coroutines_core", "completeResume", "token", "dispatchResume", "mode", "disposeParentHandle", "getContinuationCancellationCause", "parent", "Lkotlinx/coroutines/Job;", "getResult", "getStackTraceElement", "Ljava/lang/StackTraceElement;", "Lkotlinx/coroutines/internal/StackTraceElement;", "getSuccessfulResult", "getSuccessfulResult$kotlinx_coroutines_core", "(Ljava/lang/Object;)Ljava/lang/Object;", "initCancellability", "installParentCancellationHandler", "invokeHandlerSafely", "block", "Lkotlin/Function0;", "invokeOnCancellation", "handler", "Lkotlin/Function1;", "Lkotlin/ParameterName;", IMAPStore.ID_NAME, "Lkotlinx/coroutines/CompletionHandler;", "makeHandler", "Lkotlinx/coroutines/CancelHandler;", "multipleHandlersError", "nameString", "", "resume", "value", "onCancellation", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)V", "resumeImpl", "Lkotlinx/coroutines/CancelledContinuation;", "resumeWith", "result", "Lkotlin/Result;", "(Ljava/lang/Object;)V", "resumeWithExceptionMode", "exception", "resumeWithExceptionMode$kotlinx_coroutines_core", "takeState", "takeState$kotlinx_coroutines_core", "toString", "tryResume", "idempotent", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "tryResumeWithException", "trySuspend", "resumeUndispatched", "Lkotlinx/coroutines/CoroutineDispatcher;", "(Lkotlinx/coroutines/CoroutineDispatcher;Ljava/lang/Object;)V", "resumeUndispatchedWithException", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 15})
@PublishedApi
/* loaded from: classes2.dex */
public class CancellableContinuationImpl<T> extends DispatchedTask<T> implements CancellableContinuation<T>, CoroutineStackFrame {
    private static final AtomicIntegerFieldUpdater _decision$FU = AtomicIntegerFieldUpdater.newUpdater(CancellableContinuationImpl.class, "_decision");
    private static final AtomicReferenceFieldUpdater _state$FU = AtomicReferenceFieldUpdater.newUpdater(CancellableContinuationImpl.class, Object.class, "_state");
    private volatile int _decision;
    private volatile Object _state;

    @NotNull
    private final CoroutineContext context;

    @NotNull
    private final Continuation<T> delegate;
    private volatile DisposableHandle parentHandle;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public CancellableContinuationImpl(@NotNull Continuation<? super T> delegate, int i) {
        super(i);
        Intrinsics.checkParameterIsNotNull(delegate, "delegate");
        this.delegate = delegate;
        this.context = this.delegate.getContext();
        this._decision = 0;
        this._state = Active.INSTANCE;
    }

    private final void alreadyResumedError(Object proposedUpdate) {
        throw new IllegalStateException(("Already resumed, but proposed with update " + proposedUpdate).toString());
    }

    private final void dispatchResume(int mode) {
        if (tryResume()) {
            return;
        }
        DispatchedKt.dispatch(this, mode);
    }

    private final void disposeParentHandle() {
        DisposableHandle disposableHandle = this.parentHandle;
        if (disposableHandle != null) {
            disposableHandle.dispose();
            this.parentHandle = NonDisposableHandle.INSTANCE;
        }
    }

    private final void installParentCancellationHandler() {
        Job job;
        if (isCompleted() || (job = (Job) this.delegate.getContext().get(Job.INSTANCE)) == null) {
            return;
        }
        job.start();
        DisposableHandle invokeOnCompletion$default = Job.DefaultImpls.invokeOnCompletion$default(job, true, false, new ChildContinuation(job, this), 2, null);
        this.parentHandle = invokeOnCompletion$default;
        if (isCompleted()) {
            invokeOnCompletion$default.dispose();
            this.parentHandle = NonDisposableHandle.INSTANCE;
        }
    }

    private final void invokeHandlerSafely(Function0<Unit> block) {
        try {
            block.invoke();
        } catch (Throwable th) {
            CoroutineExceptionHandlerKt.handleCoroutineException(getContext(), new CompletionHandlerException("Exception in cancellation handler for " + this, th));
        }
    }

    private final CancelHandler makeHandler(Function1<? super Throwable, Unit> handler) {
        return handler instanceof CancelHandler ? (CancelHandler) handler : new InvokeOnCancel(handler);
    }

    private final void multipleHandlersError(Function1<? super Throwable, Unit> handler, Object state) {
        throw new IllegalStateException(("It's prohibited to register multiple handlers, tried to register " + handler + ", already has " + state).toString());
    }

    private final CancelledContinuation resumeImpl(Object proposedUpdate, int resumeMode) {
        while (true) {
            Object obj = this._state;
            if (!(obj instanceof NotCompleted)) {
                if (obj instanceof CancelledContinuation) {
                    CancelledContinuation cancelledContinuation = (CancelledContinuation) obj;
                    if (cancelledContinuation.makeResumed()) {
                        return cancelledContinuation;
                    }
                }
                alreadyResumedError(proposedUpdate);
            } else if (_state$FU.compareAndSet(this, obj, proposedUpdate)) {
                disposeParentHandle();
                dispatchResume(resumeMode);
                return null;
            }
        }
    }

    private final boolean tryResume() {
        do {
            switch (this._decision) {
                case 0:
                    break;
                case 1:
                    return false;
                default:
                    throw new IllegalStateException("Already resumed".toString());
            }
        } while (!_decision$FU.compareAndSet(this, 0, 2));
        return true;
    }

    private final boolean trySuspend() {
        do {
            int i = this._decision;
            if (i != 0) {
                if (i == 2) {
                    return false;
                }
                throw new IllegalStateException("Already suspended".toString());
            }
        } while (!_decision$FU.compareAndSet(this, 0, 1));
        return true;
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public boolean cancel(@Nullable Throwable cause) {
        Object obj;
        boolean z;
        do {
            obj = this._state;
            if (!(obj instanceof NotCompleted)) {
                return false;
            }
            z = obj instanceof CancelHandler;
        } while (!_state$FU.compareAndSet(this, obj, new CancelledContinuation(this, cause, z)));
        if (z) {
            try {
                ((CancelHandler) obj).invoke(cause);
            } catch (Throwable th) {
                CoroutineExceptionHandlerKt.handleCoroutineException(getContext(), new CompletionHandlerException("Exception in cancellation handler for " + this, th));
            }
        }
        disposeParentHandle();
        dispatchResume(0);
        return true;
    }

    @Override // kotlinx.coroutines.DispatchedTask
    public void cancelResult$kotlinx_coroutines_core(@Nullable Object state, @NotNull Throwable cause) {
        Intrinsics.checkParameterIsNotNull(cause, "cause");
        if (state instanceof CompletedWithCancellation) {
            try {
                ((CompletedWithCancellation) state).onCancellation.invoke(cause);
            } catch (Throwable th) {
                CoroutineExceptionHandlerKt.handleCoroutineException(getContext(), new CompletionHandlerException("Exception in cancellation handler for " + this, th));
            }
        }
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public void completeResume(@NotNull Object token) {
        Intrinsics.checkParameterIsNotNull(token, "token");
        dispatchResume(this.resumeMode);
    }

    @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
    @Nullable
    public CoroutineStackFrame getCallerFrame() {
        Continuation<T> continuation = this.delegate;
        if (!(continuation instanceof CoroutineStackFrame)) {
            continuation = null;
        }
        return (CoroutineStackFrame) continuation;
    }

    @Override // kotlin.coroutines.Continuation
    @NotNull
    public CoroutineContext getContext() {
        return this.context;
    }

    @NotNull
    public Throwable getContinuationCancellationCause(@NotNull Job parent) {
        Intrinsics.checkParameterIsNotNull(parent, "parent");
        return parent.getCancellationException();
    }

    @Override // kotlinx.coroutines.DispatchedTask
    @NotNull
    public final Continuation<T> getDelegate$kotlinx_coroutines_core() {
        return this.delegate;
    }

    @PublishedApi
    @Nullable
    public final Object getResult() {
        Job job;
        installParentCancellationHandler();
        if (trySuspend()) {
            return IntrinsicsKt.getCOROUTINE_SUSPENDED();
        }
        Object obj = get_state();
        if (obj instanceof CompletedExceptionally) {
            throw StackTraceRecoveryKt.recoverStackTrace(((CompletedExceptionally) obj).cause, this);
        }
        if (this.resumeMode != 1 || (job = (Job) getContext().get(Job.INSTANCE)) == null || job.isActive()) {
            return getSuccessfulResult$kotlinx_coroutines_core(obj);
        }
        CancellationException cancellationException = job.getCancellationException();
        cancelResult$kotlinx_coroutines_core(obj, cancellationException);
        throw StackTraceRecoveryKt.recoverStackTrace(cancellationException, this);
    }

    @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
    @Nullable
    public StackTraceElement getStackTraceElement() {
        return null;
    }

    @Nullable
    /* renamed from: getState$kotlinx_coroutines_core, reason: from getter */
    public final Object get_state() {
        return this._state;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlinx.coroutines.DispatchedTask
    public <T> T getSuccessfulResult$kotlinx_coroutines_core(@Nullable Object state) {
        return state instanceof CompletedIdempotentResult ? (T) ((CompletedIdempotentResult) state).result : state instanceof CompletedWithCancellation ? (T) ((CompletedWithCancellation) state).result : state;
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public /* synthetic */ void initCancellability() {
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public void invokeOnCancellation(@NotNull Function1<? super Throwable, Unit> handler) {
        Intrinsics.checkParameterIsNotNull(handler, "handler");
        CancelHandler cancelHandler = (CancelHandler) null;
        while (true) {
            Object obj = this._state;
            if (obj instanceof Active) {
                if (cancelHandler == null) {
                    cancelHandler = makeHandler(handler);
                }
                if (_state$FU.compareAndSet(this, obj, cancelHandler)) {
                    return;
                }
            } else {
                if (!(obj instanceof CancelHandler)) {
                    if (obj instanceof CancelledContinuation) {
                        if (!((CancelledContinuation) obj).makeHandled()) {
                            multipleHandlersError(handler, obj);
                        }
                        try {
                            if (!(obj instanceof CompletedExceptionally)) {
                                obj = null;
                            }
                            CompletedExceptionally completedExceptionally = (CompletedExceptionally) obj;
                            handler.invoke(completedExceptionally != null ? completedExceptionally.cause : null);
                            return;
                        } catch (Throwable th) {
                            CoroutineExceptionHandlerKt.handleCoroutineException(getContext(), new CompletionHandlerException("Exception in cancellation handler for " + this, th));
                            return;
                        }
                    }
                    return;
                }
                multipleHandlersError(handler, obj);
            }
        }
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public boolean isActive() {
        return get_state() instanceof NotCompleted;
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public boolean isCancelled() {
        return get_state() instanceof CancelledContinuation;
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public boolean isCompleted() {
        return !(get_state() instanceof NotCompleted);
    }

    @NotNull
    protected String nameString() {
        return "CancellableContinuation";
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public void resume(T value, @NotNull Function1<? super Throwable, Unit> onCancellation) {
        Intrinsics.checkParameterIsNotNull(onCancellation, "onCancellation");
        CancelledContinuation resumeImpl = resumeImpl(new CompletedWithCancellation(value, onCancellation), this.resumeMode);
        if (resumeImpl != null) {
            try {
                onCancellation.invoke(resumeImpl.cause);
            } catch (Throwable th) {
                CoroutineExceptionHandlerKt.handleCoroutineException(getContext(), new CompletionHandlerException("Exception in cancellation handler for " + this, th));
            }
        }
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public void resumeUndispatched(@NotNull CoroutineDispatcher resumeUndispatched, T t) {
        Intrinsics.checkParameterIsNotNull(resumeUndispatched, "$this$resumeUndispatched");
        Continuation<T> continuation = this.delegate;
        if (!(continuation instanceof DispatchedContinuation)) {
            continuation = null;
        }
        DispatchedContinuation dispatchedContinuation = (DispatchedContinuation) continuation;
        resumeImpl(t, (dispatchedContinuation != null ? dispatchedContinuation.dispatcher : null) == resumeUndispatched ? 3 : this.resumeMode);
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public void resumeUndispatchedWithException(@NotNull CoroutineDispatcher resumeUndispatchedWithException, @NotNull Throwable exception) {
        Intrinsics.checkParameterIsNotNull(resumeUndispatchedWithException, "$this$resumeUndispatchedWithException");
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        Continuation<T> continuation = this.delegate;
        if (!(continuation instanceof DispatchedContinuation)) {
            continuation = null;
        }
        DispatchedContinuation dispatchedContinuation = (DispatchedContinuation) continuation;
        resumeImpl(new CompletedExceptionally(exception, false, 2, null), (dispatchedContinuation != null ? dispatchedContinuation.dispatcher : null) == resumeUndispatchedWithException ? 3 : this.resumeMode);
    }

    @Override // kotlin.coroutines.Continuation
    public void resumeWith(@NotNull Object result) {
        resumeImpl(CompletedExceptionallyKt.toState(result), this.resumeMode);
    }

    @Nullable
    public final CancelledContinuation resumeWithExceptionMode$kotlinx_coroutines_core(@NotNull Throwable exception, int mode) {
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        return resumeImpl(new CompletedExceptionally(exception, false, 2, null), mode);
    }

    @Override // kotlinx.coroutines.DispatchedTask
    @Nullable
    public Object takeState$kotlinx_coroutines_core() {
        return get_state();
    }

    @NotNull
    public String toString() {
        return nameString() + '(' + DebugKt.toDebugString(this.delegate) + "){" + get_state() + "}@" + DebugKt.getHexAddress(this);
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    @Nullable
    public Object tryResume(T value, @Nullable Object idempotent) {
        Object obj;
        do {
            obj = this._state;
            if (!(obj instanceof NotCompleted)) {
                if (!(obj instanceof CompletedIdempotentResult)) {
                    return null;
                }
                CompletedIdempotentResult completedIdempotentResult = (CompletedIdempotentResult) obj;
                if (completedIdempotentResult.idempotentResume != idempotent) {
                    return null;
                }
                if (completedIdempotentResult.result == value) {
                    return completedIdempotentResult.token;
                }
                throw new IllegalStateException("Non-idempotent resume".toString());
            }
        } while (!_state$FU.compareAndSet(this, obj, idempotent == null ? value : new CompletedIdempotentResult(idempotent, value, (NotCompleted) obj)));
        disposeParentHandle();
        return obj;
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    @Nullable
    public Object tryResumeWithException(@NotNull Throwable exception) {
        Object obj;
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        do {
            obj = this._state;
            if (!(obj instanceof NotCompleted)) {
                return null;
            }
        } while (!_state$FU.compareAndSet(this, obj, new CompletedExceptionally(exception, false, 2, null)));
        disposeParentHandle();
        return obj;
    }
}
