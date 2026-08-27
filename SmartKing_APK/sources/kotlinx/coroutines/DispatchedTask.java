package kotlinx.coroutines;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.scheduling.Task;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Dispatched.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u000e\b \u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\u00060\u0002j\u0002`\u0003B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u001f\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0010¢\u0006\u0002\b\u0011J\u0019\u0010\u0012\u001a\u0004\u0018\u00010\u00102\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0000¢\u0006\u0002\b\u0013J\u001f\u0010\u0014\u001a\u0002H\u0001\"\u0004\b\u0001\u0010\u00012\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0010¢\u0006\u0004\b\u0015\u0010\u0016J!\u0010\u0017\u001a\u00020\f2\b\u0010\u0018\u001a\u0004\u0018\u00010\u00102\b\u0010\u0019\u001a\u0004\u0018\u00010\u0010H\u0000¢\u0006\u0002\b\u001aJ\u0006\u0010\u001b\u001a\u00020\fJ\u000f\u0010\u001c\u001a\u0004\u0018\u00010\u000eH ¢\u0006\u0002\b\u001dR\u0018\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bX \u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0012\u0010\u0004\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000¨\u0006\u001e"}, d2 = {"Lkotlinx/coroutines/DispatchedTask;", "T", "Lkotlinx/coroutines/scheduling/Task;", "Lkotlinx/coroutines/SchedulerTask;", "resumeMode", "", "(I)V", "delegate", "Lkotlin/coroutines/Continuation;", "getDelegate$kotlinx_coroutines_core", "()Lkotlin/coroutines/Continuation;", "cancelResult", "", "state", "", "cause", "", "cancelResult$kotlinx_coroutines_core", "getExceptionalResult", "getExceptionalResult$kotlinx_coroutines_core", "getSuccessfulResult", "getSuccessfulResult$kotlinx_coroutines_core", "(Ljava/lang/Object;)Ljava/lang/Object;", "handleFatalException", "exception", "finallyException", "handleFatalException$kotlinx_coroutines_core", "run", "takeState", "takeState$kotlinx_coroutines_core", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public abstract class DispatchedTask<T> extends Task {

    @JvmField
    public int resumeMode;

    public DispatchedTask(int i) {
        this.resumeMode = i;
    }

    public void cancelResult$kotlinx_coroutines_core(@Nullable Object state, @NotNull Throwable cause) {
        Intrinsics.checkParameterIsNotNull(cause, "cause");
    }

    @NotNull
    public abstract Continuation<T> getDelegate$kotlinx_coroutines_core();

    @Nullable
    public final Throwable getExceptionalResult$kotlinx_coroutines_core(@Nullable Object state) {
        if (!(state instanceof CompletedExceptionally)) {
            state = null;
        }
        CompletedExceptionally completedExceptionally = (CompletedExceptionally) state;
        if (completedExceptionally != null) {
            return completedExceptionally.cause;
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> T getSuccessfulResult$kotlinx_coroutines_core(@Nullable Object state) {
        return state;
    }

    public final void handleFatalException$kotlinx_coroutines_core(@Nullable Throwable exception, @Nullable Throwable finallyException) {
        if (exception == null && finallyException == null) {
            return;
        }
        if (exception != null && finallyException != null) {
            kotlin.ExceptionsKt.addSuppressed(exception, finallyException);
        }
        if (exception == null) {
            exception = finallyException;
        }
        String str = "Fatal exception in coroutines machinery for " + this + ". Please read KDoc to 'handleFatalException' method and report this incident to maintainers";
        if (exception == null) {
            Intrinsics.throwNpe();
        }
        CoroutineExceptionHandlerKt.handleCoroutineException(getDelegate$kotlinx_coroutines_core().getContext(), new CoroutinesInternalError(str, exception));
    }

    /* JADX WARN: Can't wrap try/catch for region: R(12:5|(1:7)|8|(3:29|30|(7:32|13|14|15|16|17|18))|10|(1:12)(1:28)|13|14|15|16|17|18) */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0091, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0092, code lost:
    
        r1 = kotlin.Result.INSTANCE;
        r0 = kotlin.Result.m33constructorimpl(kotlin.ResultKt.createFailure(r0));
     */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void run() {
        /*
            r8 = this;
            kotlinx.coroutines.scheduling.TaskContext r0 = r8.taskContext
            r1 = 0
            r2 = r1
            java.lang.Throwable r2 = (java.lang.Throwable) r2
            kotlin.coroutines.Continuation r3 = r8.getDelegate$kotlinx_coroutines_core()     // Catch: java.lang.Throwable -> Lb0 java.lang.Throwable -> Ld3
            if (r3 == 0) goto La8
            kotlinx.coroutines.DispatchedContinuation r3 = (kotlinx.coroutines.DispatchedContinuation) r3     // Catch: java.lang.Throwable -> Lb0 java.lang.Throwable -> Ld3
            kotlin.coroutines.Continuation<T> r4 = r3.continuation     // Catch: java.lang.Throwable -> Lb0 java.lang.Throwable -> Ld3
            kotlin.coroutines.CoroutineContext r5 = r4.getContext()     // Catch: java.lang.Throwable -> Lb0 java.lang.Throwable -> Ld3
            int r6 = r8.resumeMode     // Catch: java.lang.Throwable -> Lb0 java.lang.Throwable -> Ld3
            boolean r6 = kotlinx.coroutines.ResumeModeKt.isCancellableMode(r6)     // Catch: java.lang.Throwable -> Lb0 java.lang.Throwable -> Ld3
            if (r6 == 0) goto L26
            kotlinx.coroutines.Job$Key r1 = kotlinx.coroutines.Job.INSTANCE     // Catch: java.lang.Throwable -> Lb0 java.lang.Throwable -> Ld3
            kotlin.coroutines.CoroutineContext$Key r1 = (kotlin.coroutines.CoroutineContext.Key) r1     // Catch: java.lang.Throwable -> Lb0 java.lang.Throwable -> Ld3
            kotlin.coroutines.CoroutineContext$Element r1 = r5.get(r1)     // Catch: java.lang.Throwable -> Lb0 java.lang.Throwable -> Ld3
            kotlinx.coroutines.Job r1 = (kotlinx.coroutines.Job) r1     // Catch: java.lang.Throwable -> Lb0 java.lang.Throwable -> Ld3
        L26:
            java.lang.Object r6 = r8.takeState$kotlinx_coroutines_core()     // Catch: java.lang.Throwable -> Lb0 java.lang.Throwable -> Ld3
            java.lang.Object r3 = r3.countOrElement     // Catch: java.lang.Throwable -> Lb0 java.lang.Throwable -> Ld3
            java.lang.Object r3 = kotlinx.coroutines.internal.ThreadContextKt.updateThreadContext(r5, r3)     // Catch: java.lang.Throwable -> Lb0 java.lang.Throwable -> Ld3
            if (r1 == 0) goto L58
            boolean r7 = r1.isActive()     // Catch: java.lang.Throwable -> L56
            if (r7 != 0) goto L58
            java.util.concurrent.CancellationException r1 = r1.getCancellationException()     // Catch: java.lang.Throwable -> L56
            r7 = r1
            java.lang.Throwable r7 = (java.lang.Throwable) r7     // Catch: java.lang.Throwable -> L56
            r8.cancelResult$kotlinx_coroutines_core(r6, r7)     // Catch: java.lang.Throwable -> L56
            kotlin.Result$Companion r6 = kotlin.Result.INSTANCE     // Catch: java.lang.Throwable -> L56
            java.lang.Throwable r1 = (java.lang.Throwable) r1     // Catch: java.lang.Throwable -> L56
            java.lang.Throwable r1 = kotlinx.coroutines.internal.StackTraceRecoveryKt.recoverStackTrace(r1, r4)     // Catch: java.lang.Throwable -> L56
            java.lang.Object r1 = kotlin.ResultKt.createFailure(r1)     // Catch: java.lang.Throwable -> L56
            java.lang.Object r1 = kotlin.Result.m33constructorimpl(r1)     // Catch: java.lang.Throwable -> L56
            r4.resumeWith(r1)     // Catch: java.lang.Throwable -> L56
            goto L7d
        L56:
            r1 = move-exception
            goto La4
        L58:
            java.lang.Throwable r1 = r8.getExceptionalResult$kotlinx_coroutines_core(r6)     // Catch: java.lang.Throwable -> L56
            if (r1 == 0) goto L70
            kotlin.Result$Companion r6 = kotlin.Result.INSTANCE     // Catch: java.lang.Throwable -> L56
            java.lang.Throwable r1 = kotlinx.coroutines.internal.StackTraceRecoveryKt.recoverStackTrace(r1, r4)     // Catch: java.lang.Throwable -> L56
            java.lang.Object r1 = kotlin.ResultKt.createFailure(r1)     // Catch: java.lang.Throwable -> L56
            java.lang.Object r1 = kotlin.Result.m33constructorimpl(r1)     // Catch: java.lang.Throwable -> L56
            r4.resumeWith(r1)     // Catch: java.lang.Throwable -> L56
            goto L7d
        L70:
            java.lang.Object r1 = r8.getSuccessfulResult$kotlinx_coroutines_core(r6)     // Catch: java.lang.Throwable -> L56
            kotlin.Result$Companion r6 = kotlin.Result.INSTANCE     // Catch: java.lang.Throwable -> L56
            java.lang.Object r1 = kotlin.Result.m33constructorimpl(r1)     // Catch: java.lang.Throwable -> L56
            r4.resumeWith(r1)     // Catch: java.lang.Throwable -> L56
        L7d:
            kotlin.Unit r1 = kotlin.Unit.INSTANCE     // Catch: java.lang.Throwable -> L56
            kotlinx.coroutines.internal.ThreadContextKt.restoreThreadContext(r5, r3)     // Catch: java.lang.Throwable -> Lb0 java.lang.Throwable -> Ld3
            kotlin.Result$Companion r1 = kotlin.Result.INSTANCE     // Catch: java.lang.Throwable -> L91
            r1 = r8
            kotlinx.coroutines.DispatchedTask r1 = (kotlinx.coroutines.DispatchedTask) r1     // Catch: java.lang.Throwable -> L91
            r0.afterTask()     // Catch: java.lang.Throwable -> L91
            kotlin.Unit r0 = kotlin.Unit.INSTANCE     // Catch: java.lang.Throwable -> L91
            java.lang.Object r0 = kotlin.Result.m33constructorimpl(r0)     // Catch: java.lang.Throwable -> L91
            goto L9c
        L91:
            r0 = move-exception
            kotlin.Result$Companion r1 = kotlin.Result.INSTANCE
            java.lang.Object r0 = kotlin.ResultKt.createFailure(r0)
            java.lang.Object r0 = kotlin.Result.m33constructorimpl(r0)
        L9c:
            java.lang.Throwable r0 = kotlin.Result.m36exceptionOrNullimpl(r0)
            r8.handleFatalException$kotlinx_coroutines_core(r2, r0)
            goto Lf5
        La4:
            kotlinx.coroutines.internal.ThreadContextKt.restoreThreadContext(r5, r3)     // Catch: java.lang.Throwable -> Lb0 java.lang.Throwable -> Ld3
            throw r1     // Catch: java.lang.Throwable -> Lb0 java.lang.Throwable -> Ld3
        La8:
            kotlin.TypeCastException r1 = new kotlin.TypeCastException     // Catch: java.lang.Throwable -> Lb0 java.lang.Throwable -> Ld3
            java.lang.String r3 = "null cannot be cast to non-null type kotlinx.coroutines.DispatchedContinuation<T>"
            r1.<init>(r3)     // Catch: java.lang.Throwable -> Lb0 java.lang.Throwable -> Ld3
            throw r1     // Catch: java.lang.Throwable -> Lb0 java.lang.Throwable -> Ld3
        Lb0:
            r1 = move-exception
            kotlin.Result$Companion r3 = kotlin.Result.INSTANCE     // Catch: java.lang.Throwable -> Lc0
            r3 = r8
            kotlinx.coroutines.DispatchedTask r3 = (kotlinx.coroutines.DispatchedTask) r3     // Catch: java.lang.Throwable -> Lc0
            r0.afterTask()     // Catch: java.lang.Throwable -> Lc0
            kotlin.Unit r0 = kotlin.Unit.INSTANCE     // Catch: java.lang.Throwable -> Lc0
            java.lang.Object r0 = kotlin.Result.m33constructorimpl(r0)     // Catch: java.lang.Throwable -> Lc0
            goto Lcb
        Lc0:
            r0 = move-exception
            kotlin.Result$Companion r3 = kotlin.Result.INSTANCE
            java.lang.Object r0 = kotlin.ResultKt.createFailure(r0)
            java.lang.Object r0 = kotlin.Result.m33constructorimpl(r0)
        Lcb:
            java.lang.Throwable r0 = kotlin.Result.m36exceptionOrNullimpl(r0)
            r8.handleFatalException$kotlinx_coroutines_core(r2, r0)
            throw r1
        Ld3:
            r1 = move-exception
            kotlin.Result$Companion r2 = kotlin.Result.INSTANCE     // Catch: java.lang.Throwable -> Le3
            r2 = r8
            kotlinx.coroutines.DispatchedTask r2 = (kotlinx.coroutines.DispatchedTask) r2     // Catch: java.lang.Throwable -> Le3
            r0.afterTask()     // Catch: java.lang.Throwable -> Le3
            kotlin.Unit r0 = kotlin.Unit.INSTANCE     // Catch: java.lang.Throwable -> Le3
            java.lang.Object r0 = kotlin.Result.m33constructorimpl(r0)     // Catch: java.lang.Throwable -> Le3
            goto Lee
        Le3:
            r0 = move-exception
            kotlin.Result$Companion r2 = kotlin.Result.INSTANCE
            java.lang.Object r0 = kotlin.ResultKt.createFailure(r0)
            java.lang.Object r0 = kotlin.Result.m33constructorimpl(r0)
        Lee:
            java.lang.Throwable r0 = kotlin.Result.m36exceptionOrNullimpl(r0)
            r8.handleFatalException$kotlinx_coroutines_core(r1, r0)
        Lf5:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.DispatchedTask.run():void");
    }

    @Nullable
    public abstract Object takeState$kotlinx_coroutines_core();
}
