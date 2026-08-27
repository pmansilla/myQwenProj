package kotlinx.coroutines.selects;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.jvm.JvmField;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CompletedExceptionally;
import kotlinx.coroutines.CompletedExceptionallyKt;
import kotlinx.coroutines.CoroutineExceptionHandlerKt;
import kotlinx.coroutines.DelayKt;
import kotlinx.coroutines.DispatchedKt;
import kotlinx.coroutines.DisposableHandle;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.JobCancellingNode;
import kotlinx.coroutines.internal.AtomicDesc;
import kotlinx.coroutines.internal.AtomicOp;
import kotlinx.coroutines.internal.LockFreeLinkedListHead;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;
import kotlinx.coroutines.internal.OpDescriptor;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
import kotlinx.coroutines.intrinsics.CancellableKt;
import kotlinx.coroutines.intrinsics.UndispatchedKt;
import kotlinx.coroutines.selects.SelectBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Select.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000¢\u0001\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0001\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\u00020\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\b\u0012\u0004\u0012\u0002H\u00010\u00042\b\u0012\u0004\u0012\u0002H\u00010\u00052\u00060\u0006j\u0002`\u0007:\u0003LMNB\u0013\u0012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005¢\u0006\u0002\u0010\tJ\u0010\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u001cH\u0016J\b\u0010#\u001a\u00020!H\u0002J'\u0010$\u001a\u00020!2\u000e\u0010%\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0&2\f\u0010'\u001a\b\u0012\u0004\u0012\u00020!0&H\u0082\bJ\n\u0010(\u001a\u0004\u0018\u00010\fH\u0001J\u0010\u0010)\u001a\n\u0018\u00010*j\u0004\u0018\u0001`+H\u0016J\u0010\u0010,\u001a\u00020!2\u0006\u0010-\u001a\u00020.H\u0001J\b\u0010/\u001a\u00020!H\u0002J6\u00100\u001a\u00020!2\u0006\u00101\u001a\u0002022\u001c\u0010'\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0005\u0012\u0006\u0012\u0004\u0018\u00010\f03H\u0016ø\u0001\u0000¢\u0006\u0002\u00104J\u0012\u00105\u001a\u0004\u0018\u00010\f2\u0006\u00106\u001a\u000207H\u0016J\u0012\u00108\u001a\u0004\u0018\u00010\f2\u0006\u00106\u001a\u000207H\u0016J\u0010\u00109\u001a\u00020!2\u0006\u0010:\u001a\u00020.H\u0016J\u001e\u0010;\u001a\u00020!2\f\u0010<\u001a\b\u0012\u0004\u0012\u00028\u00000=H\u0016ø\u0001\u0000¢\u0006\u0002\u0010>J\u0012\u0010?\u001a\u00020\u00192\b\u0010@\u001a\u0004\u0018\u00010\fH\u0016J3\u0010A\u001a\u00020!*\u00020B2\u001c\u0010'\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0005\u0012\u0006\u0012\u0004\u0018\u00010\f03H\u0096\u0002ø\u0001\u0000¢\u0006\u0002\u0010CJE\u0010A\u001a\u00020!\"\u0004\b\u0001\u0010D*\b\u0012\u0004\u0012\u0002HD0E2\"\u0010'\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002HD\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0005\u0012\u0006\u0012\u0004\u0018\u00010\f0FH\u0096\u0002ø\u0001\u0000¢\u0006\u0002\u0010GJY\u0010A\u001a\u00020!\"\u0004\b\u0001\u0010H\"\u0004\b\u0002\u0010D*\u000e\u0012\u0004\u0012\u0002HH\u0012\u0004\u0012\u0002HD0I2\u0006\u0010J\u001a\u0002HH2\"\u0010'\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002HD\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0005\u0012\u0006\u0012\u0004\u0018\u00010\f0FH\u0096\u0002ø\u0001\u0000¢\u0006\u0002\u0010KR\u0016\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u000e\u001a\n\u0018\u00010\u0006j\u0004\u0018\u0001`\u00078VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\b\u0012\u0004\u0012\u00028\u00000\u00058VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013R\u0014\u0010\u0014\u001a\u00020\u00158VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017R\u0014\u0010\u0018\u001a\u00020\u00198VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0018\u0010\u001aR\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u001cX\u0082\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\u001d\u001a\u0004\u0018\u00010\f8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u001e\u0010\u001fR\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005X\u0082\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006O"}, d2 = {"Lkotlinx/coroutines/selects/SelectBuilderImpl;", "R", "Lkotlinx/coroutines/internal/LockFreeLinkedListHead;", "Lkotlinx/coroutines/selects/SelectBuilder;", "Lkotlinx/coroutines/selects/SelectInstance;", "Lkotlin/coroutines/Continuation;", "Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "Lkotlinx/coroutines/internal/CoroutineStackFrame;", "uCont", "(Lkotlin/coroutines/Continuation;)V", "_result", "Lkotlinx/atomicfu/AtomicRef;", "", "_state", "callerFrame", "getCallerFrame", "()Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "completion", "getCompletion", "()Lkotlin/coroutines/Continuation;", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "isSelected", "", "()Z", "parentHandle", "Lkotlinx/coroutines/DisposableHandle;", "state", "getState", "()Ljava/lang/Object;", "disposeOnSelect", "", "handle", "doAfterSelect", "doResume", "value", "Lkotlin/Function0;", "block", "getResult", "getStackTraceElement", "Ljava/lang/StackTraceElement;", "Lkotlinx/coroutines/internal/StackTraceElement;", "handleBuilderException", "e", "", "initCancellability", "onTimeout", "timeMillis", "", "Lkotlin/Function1;", "(JLkotlin/jvm/functions/Function1;)V", "performAtomicIfNotSelected", "desc", "Lkotlinx/coroutines/internal/AtomicDesc;", "performAtomicTrySelect", "resumeSelectCancellableWithException", "exception", "resumeWith", "result", "Lkotlin/Result;", "(Ljava/lang/Object;)V", "trySelect", "idempotent", "invoke", "Lkotlinx/coroutines/selects/SelectClause0;", "(Lkotlinx/coroutines/selects/SelectClause0;Lkotlin/jvm/functions/Function1;)V", "Q", "Lkotlinx/coroutines/selects/SelectClause1;", "Lkotlin/Function2;", "(Lkotlinx/coroutines/selects/SelectClause1;Lkotlin/jvm/functions/Function2;)V", "P", "Lkotlinx/coroutines/selects/SelectClause2;", "param", "(Lkotlinx/coroutines/selects/SelectClause2;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V", "AtomicSelectOp", "DisposeNode", "SelectOnCancelling", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 15})
@PublishedApi
/* loaded from: classes2.dex */
public final class SelectBuilderImpl<R> extends LockFreeLinkedListHead implements SelectBuilder<R>, SelectInstance<R>, Continuation<R>, CoroutineStackFrame {
    volatile Object _result;
    volatile Object _state;
    private volatile DisposableHandle parentHandle;
    private final Continuation<R> uCont;
    static final AtomicReferenceFieldUpdater _state$FU = AtomicReferenceFieldUpdater.newUpdater(SelectBuilderImpl.class, Object.class, "_state");
    static final AtomicReferenceFieldUpdater _result$FU = AtomicReferenceFieldUpdater.newUpdater(SelectBuilderImpl.class, Object.class, "_result");

    /* compiled from: Select.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\b\u0082\u0004\u0018\u00002\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u001c\u0010\b\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\u00022\b\u0010\u000b\u001a\u0004\u0018\u00010\u0002H\u0016J\u0012\u0010\f\u001a\u00020\t2\b\u0010\u000b\u001a\u0004\u0018\u00010\u0002H\u0002J\u0014\u0010\r\u001a\u0004\u0018\u00010\u00022\b\u0010\n\u001a\u0004\u0018\u00010\u0002H\u0016J\b\u0010\u000e\u001a\u0004\u0018\u00010\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u00020\u00068\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lkotlinx/coroutines/selects/SelectBuilderImpl$AtomicSelectOp;", "Lkotlinx/coroutines/internal/AtomicOp;", "", "desc", "Lkotlinx/coroutines/internal/AtomicDesc;", "select", "", "(Lkotlinx/coroutines/selects/SelectBuilderImpl;Lkotlinx/coroutines/internal/AtomicDesc;Z)V", "complete", "", "affected", "failure", "completeSelect", "prepare", "prepareIfNotSelected", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 15})
    /* loaded from: classes2.dex */
    private final class AtomicSelectOp extends AtomicOp<Object> {

        @JvmField
        @NotNull
        public final AtomicDesc desc;

        @JvmField
        public final boolean select;
        final /* synthetic */ SelectBuilderImpl this$0;

        public AtomicSelectOp(@NotNull SelectBuilderImpl selectBuilderImpl, AtomicDesc desc, boolean z) {
            Intrinsics.checkParameterIsNotNull(desc, "desc");
            this.this$0 = selectBuilderImpl;
            this.desc = desc;
            this.select = z;
        }

        private final void completeSelect(Object failure) {
            boolean z = this.select && failure == null;
            if (SelectBuilderImpl._state$FU.compareAndSet(this.this$0, this, z ? null : this.this$0) && z) {
                this.this$0.doAfterSelect();
            }
        }

        @Override // kotlinx.coroutines.internal.AtomicOp
        public void complete(@Nullable Object affected, @Nullable Object failure) {
            completeSelect(failure);
            this.desc.complete(this, failure);
        }

        @Override // kotlinx.coroutines.internal.AtomicOp
        @Nullable
        public Object prepare(@Nullable Object affected) {
            Object prepareIfNotSelected;
            return (affected != null || (prepareIfNotSelected = prepareIfNotSelected()) == null) ? this.desc.prepare(this) : prepareIfNotSelected;
        }

        @Nullable
        public final Object prepareIfNotSelected() {
            SelectBuilderImpl selectBuilderImpl = this.this$0;
            while (true) {
                Object obj = selectBuilderImpl._state;
                if (obj == this) {
                    return null;
                }
                if (obj instanceof OpDescriptor) {
                    ((OpDescriptor) obj).perform(this.this$0);
                } else {
                    if (obj != this.this$0) {
                        return SelectKt.getALREADY_SELECTED();
                    }
                    if (SelectBuilderImpl._state$FU.compareAndSet(this.this$0, this.this$0, this)) {
                        return null;
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: Select.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lkotlinx/coroutines/selects/SelectBuilderImpl$DisposeNode;", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "handle", "Lkotlinx/coroutines/DisposableHandle;", "(Lkotlinx/coroutines/DisposableHandle;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 15})
    /* loaded from: classes2.dex */
    public static final class DisposeNode extends LockFreeLinkedListNode {

        @JvmField
        @NotNull
        public final DisposableHandle handle;

        public DisposeNode(@NotNull DisposableHandle handle) {
            Intrinsics.checkParameterIsNotNull(handle, "handle");
            this.handle = handle;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: Select.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0002¢\u0006\u0002\u0010\u0004J\u0013\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0096\u0002J\b\u0010\t\u001a\u00020\nH\u0016¨\u0006\u000b"}, d2 = {"Lkotlinx/coroutines/selects/SelectBuilderImpl$SelectOnCancelling;", "Lkotlinx/coroutines/JobCancellingNode;", "Lkotlinx/coroutines/Job;", "job", "(Lkotlinx/coroutines/selects/SelectBuilderImpl;Lkotlinx/coroutines/Job;)V", "invoke", "", "cause", "", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 15})
    /* loaded from: classes2.dex */
    public final class SelectOnCancelling extends JobCancellingNode<Job> {
        final /* synthetic */ SelectBuilderImpl this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SelectOnCancelling(@NotNull SelectBuilderImpl selectBuilderImpl, Job job) {
            super(job);
            Intrinsics.checkParameterIsNotNull(job, "job");
            this.this$0 = selectBuilderImpl;
        }

        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
            invoke2(th);
            return Unit.INSTANCE;
        }

        @Override // kotlinx.coroutines.CompletionHandlerBase
        /* renamed from: invoke, reason: avoid collision after fix types in other method */
        public void invoke2(@Nullable Throwable cause) {
            if (this.this$0.trySelect(null)) {
                this.this$0.resumeSelectCancellableWithException(this.job.getCancellationException());
            }
        }

        @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode
        @NotNull
        public String toString() {
            return "SelectOnCancelling[" + this.this$0 + ']';
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public SelectBuilderImpl(@NotNull Continuation<? super R> uCont) {
        Object obj;
        Intrinsics.checkParameterIsNotNull(uCont, "uCont");
        this.uCont = uCont;
        this._state = this;
        obj = SelectKt.UNDECIDED;
        this._result = obj;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void doAfterSelect() {
        DisposableHandle disposableHandle = this.parentHandle;
        if (disposableHandle != null) {
            disposableHandle.dispose();
        }
        Object next = getNext();
        if (next == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
        }
        for (LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) next; !Intrinsics.areEqual(lockFreeLinkedListNode, this); lockFreeLinkedListNode = lockFreeLinkedListNode.getNextNode()) {
            if (lockFreeLinkedListNode instanceof DisposeNode) {
                ((DisposeNode) lockFreeLinkedListNode).handle.dispose();
            }
        }
    }

    private final void doResume(Function0<? extends Object> value, Function0<Unit> block) {
        Object obj;
        Object obj2;
        Object obj3;
        if (!isSelected()) {
            throw new IllegalStateException("Must be selected first".toString());
        }
        while (true) {
            Object obj4 = this._result;
            obj = SelectKt.UNDECIDED;
            if (obj4 == obj) {
                AtomicReferenceFieldUpdater atomicReferenceFieldUpdater = _result$FU;
                obj2 = SelectKt.UNDECIDED;
                if (atomicReferenceFieldUpdater.compareAndSet(this, obj2, value.invoke())) {
                    return;
                }
            } else {
                if (obj4 != IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                    throw new IllegalStateException("Already resumed");
                }
                AtomicReferenceFieldUpdater atomicReferenceFieldUpdater2 = _result$FU;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                obj3 = SelectKt.RESUMED;
                if (atomicReferenceFieldUpdater2.compareAndSet(this, coroutine_suspended, obj3)) {
                    block.invoke();
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Object getState() {
        while (true) {
            Object obj = this._state;
            if (!(obj instanceof OpDescriptor)) {
                return obj;
            }
            ((OpDescriptor) obj).perform(this);
        }
    }

    private final void initCancellability() {
        Job job = (Job) getContext().get(Job.INSTANCE);
        if (job != null) {
            DisposableHandle invokeOnCompletion$default = Job.DefaultImpls.invokeOnCompletion$default(job, true, false, new SelectOnCancelling(this, job), 2, null);
            this.parentHandle = invokeOnCompletion$default;
            if (isSelected()) {
                invokeOnCompletion$default.dispose();
            }
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.RegionMaker.calcSwitchOut(RegionMaker.java:923)
        	at jadx.core.dex.visitors.regions.RegionMaker.processSwitch(RegionMaker.java:797)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:157)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:91)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:735)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:152)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:91)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeEndlessLoop(RegionMaker.java:411)
        	at jadx.core.dex.visitors.regions.RegionMaker.processLoop(RegionMaker.java:201)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:135)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:91)
        	at jadx.core.dex.visitors.regions.RegionMaker.processLoop(RegionMaker.java:263)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:135)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:91)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:52)
        */
    @Override // kotlinx.coroutines.selects.SelectInstance
    public void disposeOnSelect(@org.jetbrains.annotations.NotNull kotlinx.coroutines.DisposableHandle r5) {
        /*
            r4 = this;
            java.lang.String r0 = "handle"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r5, r0)
            kotlinx.coroutines.selects.SelectBuilderImpl$DisposeNode r0 = new kotlinx.coroutines.selects.SelectBuilderImpl$DisposeNode
            r0.<init>(r5)
        La:
            java.lang.Object r1 = r4.getState()
            r2 = r4
            kotlinx.coroutines.selects.SelectBuilderImpl r2 = (kotlinx.coroutines.selects.SelectBuilderImpl) r2
            if (r1 != r2) goto L3b
            kotlinx.coroutines.selects.SelectBuilderImpl$disposeOnSelect$$inlined$addLastIf$1 r1 = new kotlinx.coroutines.selects.SelectBuilderImpl$disposeOnSelect$$inlined$addLastIf$1
            r2 = r0
            kotlinx.coroutines.internal.LockFreeLinkedListNode r2 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r2
            r1.<init>(r2)
            kotlinx.coroutines.internal.LockFreeLinkedListNode$CondAddOp r1 = (kotlinx.coroutines.internal.LockFreeLinkedListNode.CondAddOp) r1
        L1d:
            java.lang.Object r3 = r4.getPrev()
            if (r3 == 0) goto L33
            kotlinx.coroutines.internal.LockFreeLinkedListNode r3 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r3
            int r3 = r3.tryCondAddNext(r2, r4, r1)
            switch(r3) {
                case 1: goto L2f;
                case 2: goto L2d;
                default: goto L2c;
            }
        L2c:
            goto L1d
        L2d:
            r1 = 0
            goto L30
        L2f:
            r1 = 1
        L30:
            if (r1 == 0) goto La
            return
        L33:
            kotlin.TypeCastException r5 = new kotlin.TypeCastException
        */
        //  java.lang.String r0 = "null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */"
        /*
            r5.<init>(r0)
            throw r5
        L3b:
            r5.dispose()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.selects.SelectBuilderImpl.disposeOnSelect(kotlinx.coroutines.DisposableHandle):void");
    }

    @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
    @Nullable
    public CoroutineStackFrame getCallerFrame() {
        Continuation<R> continuation = this.uCont;
        if (!(continuation instanceof CoroutineStackFrame)) {
            continuation = null;
        }
        return (CoroutineStackFrame) continuation;
    }

    @Override // kotlinx.coroutines.selects.SelectInstance
    @NotNull
    public Continuation<R> getCompletion() {
        return this;
    }

    @Override // kotlin.coroutines.Continuation
    @NotNull
    public CoroutineContext getContext() {
        return this.uCont.getContext();
    }

    @PublishedApi
    @Nullable
    public final Object getResult() {
        Object obj;
        Object obj2;
        Object obj3;
        if (!isSelected()) {
            initCancellability();
        }
        Object obj4 = this._result;
        obj = SelectKt.UNDECIDED;
        if (obj4 == obj) {
            AtomicReferenceFieldUpdater atomicReferenceFieldUpdater = _result$FU;
            obj3 = SelectKt.UNDECIDED;
            if (atomicReferenceFieldUpdater.compareAndSet(this, obj3, IntrinsicsKt.getCOROUTINE_SUSPENDED())) {
                return IntrinsicsKt.getCOROUTINE_SUSPENDED();
            }
            obj4 = this._result;
        }
        obj2 = SelectKt.RESUMED;
        if (obj4 == obj2) {
            throw new IllegalStateException("Already resumed");
        }
        if (obj4 instanceof CompletedExceptionally) {
            throw ((CompletedExceptionally) obj4).cause;
        }
        return obj4;
    }

    @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
    @Nullable
    public StackTraceElement getStackTraceElement() {
        return null;
    }

    @PublishedApi
    public final void handleBuilderException(@NotNull Throwable e) {
        Intrinsics.checkParameterIsNotNull(e, "e");
        if (!trySelect(null)) {
            CoroutineExceptionHandlerKt.handleCoroutineException(getContext(), e);
        } else {
            Result.Companion companion = Result.INSTANCE;
            resumeWith(Result.m33constructorimpl(ResultKt.createFailure(e)));
        }
    }

    @Override // kotlinx.coroutines.selects.SelectBuilder
    public void invoke(@NotNull SelectClause0 invoke, @NotNull Function1<? super Continuation<? super R>, ? extends Object> block) {
        Intrinsics.checkParameterIsNotNull(invoke, "$this$invoke");
        Intrinsics.checkParameterIsNotNull(block, "block");
        invoke.registerSelectClause0(this, block);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlinx.coroutines.selects.SelectBuilder
    public <Q> void invoke(@NotNull SelectClause1<? extends Q> invoke, @NotNull Function2<? super Q, ? super Continuation<? super R>, ? extends Object> block) {
        Intrinsics.checkParameterIsNotNull(invoke, "$this$invoke");
        Intrinsics.checkParameterIsNotNull(block, "block");
        invoke.registerSelectClause1(this, block);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlinx.coroutines.selects.SelectBuilder
    public <P, Q> void invoke(@NotNull SelectClause2<? super P, ? extends Q> invoke, P p, @NotNull Function2<? super Q, ? super Continuation<? super R>, ? extends Object> block) {
        Intrinsics.checkParameterIsNotNull(invoke, "$this$invoke");
        Intrinsics.checkParameterIsNotNull(block, "block");
        invoke.registerSelectClause2(this, p, block);
    }

    @Override // kotlinx.coroutines.selects.SelectBuilder
    public <P, Q> void invoke(@NotNull SelectClause2<? super P, ? extends Q> invoke, @NotNull Function2<? super Q, ? super Continuation<? super R>, ? extends Object> block) {
        Intrinsics.checkParameterIsNotNull(invoke, "$this$invoke");
        Intrinsics.checkParameterIsNotNull(block, "block");
        SelectBuilder.DefaultImpls.invoke(this, invoke, block);
    }

    @Override // kotlinx.coroutines.selects.SelectInstance
    public boolean isSelected() {
        return getState() != this;
    }

    @Override // kotlinx.coroutines.selects.SelectBuilder
    public void onTimeout(long timeMillis, @NotNull final Function1<? super Continuation<? super R>, ? extends Object> block) {
        Intrinsics.checkParameterIsNotNull(block, "block");
        if (timeMillis > 0) {
            disposeOnSelect(DelayKt.getDelay(getContext()).invokeOnTimeout(timeMillis, new Runnable() { // from class: kotlinx.coroutines.selects.SelectBuilderImpl$onTimeout$$inlined$Runnable$1
                @Override // java.lang.Runnable
                public final void run() {
                    if (SelectBuilderImpl.this.trySelect(null)) {
                        CancellableKt.startCoroutineCancellable(block, SelectBuilderImpl.this.getCompletion());
                    }
                }
            }));
        } else if (trySelect(null)) {
            UndispatchedKt.startCoroutineUnintercepted(block, getCompletion());
        }
    }

    @Override // kotlinx.coroutines.selects.SelectInstance
    @Nullable
    public Object performAtomicIfNotSelected(@NotNull AtomicDesc desc) {
        Intrinsics.checkParameterIsNotNull(desc, "desc");
        return new AtomicSelectOp(this, desc, false).perform(null);
    }

    @Override // kotlinx.coroutines.selects.SelectInstance
    @Nullable
    public Object performAtomicTrySelect(@NotNull AtomicDesc desc) {
        Intrinsics.checkParameterIsNotNull(desc, "desc");
        return new AtomicSelectOp(this, desc, true).perform(null);
    }

    @Override // kotlinx.coroutines.selects.SelectInstance
    public void resumeSelectCancellableWithException(@NotNull Throwable exception) {
        Object obj;
        Object obj2;
        Object obj3;
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        if (!isSelected()) {
            throw new IllegalStateException("Must be selected first".toString());
        }
        while (true) {
            Object obj4 = this._result;
            obj = SelectKt.UNDECIDED;
            if (obj4 == obj) {
                obj2 = SelectKt.UNDECIDED;
                if (_result$FU.compareAndSet(this, obj2, new CompletedExceptionally(exception, false, 2, null))) {
                    return;
                }
            } else {
                if (obj4 != IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                    throw new IllegalStateException("Already resumed");
                }
                AtomicReferenceFieldUpdater atomicReferenceFieldUpdater = _result$FU;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                obj3 = SelectKt.RESUMED;
                if (atomicReferenceFieldUpdater.compareAndSet(this, coroutine_suspended, obj3)) {
                    DispatchedKt.resumeCancellableWithException(IntrinsicsKt.intercepted(this.uCont), exception);
                    return;
                }
            }
        }
    }

    @Override // kotlin.coroutines.Continuation
    public void resumeWith(@NotNull Object result) {
        Object obj;
        Object obj2;
        Object obj3;
        if (!isSelected()) {
            throw new IllegalStateException("Must be selected first".toString());
        }
        while (true) {
            Object obj4 = this._result;
            obj = SelectKt.UNDECIDED;
            if (obj4 == obj) {
                obj2 = SelectKt.UNDECIDED;
                if (_result$FU.compareAndSet(this, obj2, CompletedExceptionallyKt.toState(result))) {
                    return;
                }
            } else {
                if (obj4 != IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                    throw new IllegalStateException("Already resumed");
                }
                AtomicReferenceFieldUpdater atomicReferenceFieldUpdater = _result$FU;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                obj3 = SelectKt.RESUMED;
                if (atomicReferenceFieldUpdater.compareAndSet(this, coroutine_suspended, obj3)) {
                    if (!Result.m39isFailureimpl(result)) {
                        this.uCont.resumeWith(result);
                        return;
                    }
                    Continuation<R> continuation = this.uCont;
                    Throwable m36exceptionOrNullimpl = Result.m36exceptionOrNullimpl(result);
                    if (m36exceptionOrNullimpl == null) {
                        Intrinsics.throwNpe();
                    }
                    Result.Companion companion = Result.INSTANCE;
                    continuation.resumeWith(Result.m33constructorimpl(ResultKt.createFailure(StackTraceRecoveryKt.recoverStackTrace(m36exceptionOrNullimpl, continuation))));
                    return;
                }
            }
        }
    }

    @Override // kotlinx.coroutines.selects.SelectInstance
    public boolean trySelect(@Nullable Object idempotent) {
        if (!(!(idempotent instanceof OpDescriptor))) {
            throw new IllegalStateException("cannot use OpDescriptor as idempotent marker".toString());
        }
        do {
            Object state = getState();
            if (state != this) {
                return idempotent != null && state == idempotent;
            }
        } while (!_state$FU.compareAndSet(this, this, idempotent));
        doAfterSelect();
        return true;
    }
}
