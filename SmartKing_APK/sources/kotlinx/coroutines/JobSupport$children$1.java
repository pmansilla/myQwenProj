package kotlinx.coroutines;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequenceScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: JobSupport.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", "Lkotlin/sequences/SequenceScope;", "Lkotlinx/coroutines/ChildJob;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
@DebugMetadata(c = "kotlinx.coroutines.JobSupport$children$1", f = "JobSupport.kt", i = {0, 1, 1, 1, 1, 1}, l = {869, 871}, m = "invokeSuspend", n = {"state", "state", "list", "this_$iv", "cur$iv", "it"}, s = {"L$0", "L$1", "L$2", "L$3", "L$4", "L$5"})
/* loaded from: classes2.dex */
final class JobSupport$children$1 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super ChildJob>, Continuation<? super Unit>, Object> {
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    Object L$4;
    Object L$5;
    int label;
    private SequenceScope p$;
    final /* synthetic */ JobSupport this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public JobSupport$children$1(JobSupport jobSupport, Continuation continuation) {
        super(2, continuation);
        this.this$0 = jobSupport;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> completion) {
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        JobSupport$children$1 jobSupport$children$1 = new JobSupport$children$1(this.this$0, completion);
        jobSupport$children$1.p$ = (SequenceScope) obj;
        return jobSupport$children$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(SequenceScope<? super ChildJob> sequenceScope, Continuation<? super Unit> continuation) {
        return ((JobSupport$children$1) create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0007. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0077  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:11:0x0079 -> B:7:0x0096). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:13:0x0093 -> B:7:0x0096). Please report as a decompilation issue!!! */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @org.jetbrains.annotations.Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(@org.jetbrains.annotations.NotNull java.lang.Object r10) {
        /*
            r9 = this;
            java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r1 = r9.label
            r2 = 1
            switch(r1) {
                case 0: goto L34;
                case 1: goto L2d;
                case 2: goto L12;
                default: goto La;
            }
        La:
            java.lang.IllegalStateException r10 = new java.lang.IllegalStateException
            java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
            r10.<init>(r0)
            throw r10
        L12:
            java.lang.Object r1 = r9.L$5
            kotlinx.coroutines.ChildHandleNode r1 = (kotlinx.coroutines.ChildHandleNode) r1
            java.lang.Object r1 = r9.L$4
            kotlinx.coroutines.internal.LockFreeLinkedListNode r1 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r1
            java.lang.Object r3 = r9.L$3
            kotlinx.coroutines.NodeList r3 = (kotlinx.coroutines.NodeList) r3
            java.lang.Object r4 = r9.L$2
            kotlinx.coroutines.NodeList r4 = (kotlinx.coroutines.NodeList) r4
            java.lang.Object r5 = r9.L$1
            java.lang.Object r6 = r9.L$0
            kotlin.sequences.SequenceScope r6 = (kotlin.sequences.SequenceScope) r6
            kotlin.ResultKt.throwOnFailure(r10)
            r10 = r9
            goto L96
        L2d:
            java.lang.Object r0 = r9.L$0
            kotlin.ResultKt.throwOnFailure(r10)
            goto La3
        L34:
            kotlin.ResultKt.throwOnFailure(r10)
            kotlin.sequences.SequenceScope r10 = r9.p$
            kotlinx.coroutines.JobSupport r1 = r9.this$0
            java.lang.Object r1 = r1.getState$kotlinx_coroutines_core()
            boolean r3 = r1 instanceof kotlinx.coroutines.ChildHandleNode
            if (r3 == 0) goto L53
            r3 = r1
            kotlinx.coroutines.ChildHandleNode r3 = (kotlinx.coroutines.ChildHandleNode) r3
            kotlinx.coroutines.ChildJob r3 = r3.childJob
            r9.L$0 = r1
            r9.label = r2
            java.lang.Object r10 = r10.yield(r3, r9)
            if (r10 != r0) goto La3
            return r0
        L53:
            boolean r3 = r1 instanceof kotlinx.coroutines.Incomplete
            if (r3 == 0) goto La3
            r3 = r1
            kotlinx.coroutines.Incomplete r3 = (kotlinx.coroutines.Incomplete) r3
            kotlinx.coroutines.NodeList r3 = r3.getList()
            if (r3 == 0) goto La3
            java.lang.Object r4 = r3.getNext()
            if (r4 == 0) goto L9b
            kotlinx.coroutines.internal.LockFreeLinkedListNode r4 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r4
            r6 = r10
            r5 = r1
            r1 = r4
            r10 = r9
            r4 = r3
        L6d:
            r7 = r3
            kotlinx.coroutines.internal.LockFreeLinkedListHead r7 = (kotlinx.coroutines.internal.LockFreeLinkedListHead) r7
            boolean r7 = kotlin.jvm.internal.Intrinsics.areEqual(r1, r7)
            r7 = r7 ^ r2
            if (r7 == 0) goto La3
            boolean r7 = r1 instanceof kotlinx.coroutines.ChildHandleNode
            if (r7 == 0) goto L96
            r7 = r1
            kotlinx.coroutines.ChildHandleNode r7 = (kotlinx.coroutines.ChildHandleNode) r7
            kotlinx.coroutines.ChildJob r8 = r7.childJob
            r10.L$0 = r6
            r10.L$1 = r5
            r10.L$2 = r4
            r10.L$3 = r3
            r10.L$4 = r1
            r10.L$5 = r7
            r7 = 2
            r10.label = r7
            java.lang.Object r7 = r6.yield(r8, r10)
            if (r7 != r0) goto L96
            return r0
        L96:
            kotlinx.coroutines.internal.LockFreeLinkedListNode r1 = r1.getNextNode()
            goto L6d
        L9b:
            kotlin.TypeCastException r10 = new kotlin.TypeCastException
        */
        //  java.lang.String r0 = "null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */"
        /*
            r10.<init>(r0)
            throw r10
        La3:
            kotlin.Unit r10 = kotlin.Unit.INSTANCE
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.JobSupport$children$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
