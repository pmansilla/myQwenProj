package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: Add missing generic type declarations: [T] */
/* compiled from: Errors.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", "T", "Lkotlinx/coroutines/flow/FlowCollector;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
@DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__ErrorsKt$retry$2", f = "Errors.kt", i = {0, 0}, l = {60}, m = "invokeSuspend", n = {"retries", "fromDownstream"}, s = {"I$0", "L$1"})
/* loaded from: classes2.dex */
public final class FlowKt__ErrorsKt$retry$2<T> extends SuspendLambda implements Function2<FlowCollector<? super T>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function1 $predicate;
    final /* synthetic */ int $retries;
    final /* synthetic */ Flow $this_retry;
    int I$0;
    Object L$0;
    Object L$1;
    int label;
    private FlowCollector p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: Errors.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u0002H\u0002H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", "T", "value", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
    @DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__ErrorsKt$retry$2$1", f = "Errors.kt", i = {}, l = {62}, m = "invokeSuspend", n = {}, s = {})
    /* renamed from: kotlinx.coroutines.flow.FlowKt__ErrorsKt$retry$2$1, reason: invalid class name */
    /* loaded from: classes2.dex */
    public static final class AnonymousClass1 extends SuspendLambda implements Function2<T, Continuation<? super Unit>, Object> {
        final /* synthetic */ Ref.BooleanRef $fromDownstream;
        final /* synthetic */ FlowCollector $receiver$0;
        int label;
        private Object p$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(FlowCollector flowCollector, Ref.BooleanRef booleanRef, Continuation continuation) {
            super(2, continuation);
            this.$receiver$0 = flowCollector;
            this.$fromDownstream = booleanRef;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @NotNull
        public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> completion) {
            Intrinsics.checkParameterIsNotNull(completion, "completion");
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(this.$receiver$0, this.$fromDownstream, completion);
            anonymousClass1.p$0 = obj;
            return anonymousClass1;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
            return ((AnonymousClass1) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @Nullable
        public final Object invokeSuspend(@NotNull Object obj) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            try {
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure(obj);
                        Object obj2 = this.p$0;
                        FlowCollector flowCollector = this.$receiver$0;
                        this.label = 1;
                        if (flowCollector.emit(obj2, this) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        break;
                    case 1:
                        ResultKt.throwOnFailure(obj);
                        break;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                return Unit.INSTANCE;
            } catch (Throwable th) {
                this.$fromDownstream.element = true;
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FlowKt__ErrorsKt$retry$2(Flow flow, int i, Function1 function1, Continuation continuation) {
        super(2, continuation);
        this.$this_retry = flow;
        this.$retries = i;
        this.$predicate = function1;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> completion) {
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        FlowKt__ErrorsKt$retry$2 flowKt__ErrorsKt$retry$2 = new FlowKt__ErrorsKt$retry$2(this.$this_retry, this.$retries, this.$predicate, completion);
        flowKt__ErrorsKt$retry$2.p$ = (FlowCollector) obj;
        return flowKt__ErrorsKt$retry$2;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
        return ((FlowKt__ErrorsKt$retry$2) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:27:0x0052
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1166)
        	at jadx.core.dex.visitors.regions.RegionMaker.processTryCatchBlocks(RegionMaker.java:1022)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:55)
        */
    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0006. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:25:0x004e A[RETURN] */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @org.jetbrains.annotations.Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(@org.jetbrains.annotations.NotNull java.lang.Object r8) {
        /*
            r7 = this;
            java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r1 = r7.label
            switch(r1) {
                case 0: goto L23;
                case 1: goto L11;
                default: goto L9;
            }
        L9:
            java.lang.IllegalStateException r8 = new java.lang.IllegalStateException
            java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
            r8.<init>(r0)
            throw r8
        L11:
            java.lang.Object r1 = r7.L$1
            kotlin.jvm.internal.Ref$BooleanRef r1 = (kotlin.jvm.internal.Ref.BooleanRef) r1
            int r2 = r7.I$0
            java.lang.Object r3 = r7.L$0
            kotlinx.coroutines.flow.FlowCollector r3 = (kotlinx.coroutines.flow.FlowCollector) r3
            kotlin.ResultKt.throwOnFailure(r8)     // Catch: java.lang.Throwable -> L1f
            goto L4f
        L1f:
            r8 = move-exception
            r4 = r8
            r8 = r7
            goto L53
        L23:
            kotlin.ResultKt.throwOnFailure(r8)
            kotlinx.coroutines.flow.FlowCollector r8 = r7.p$
            int r1 = r7.$retries
            r3 = r8
            r2 = r1
            r8 = r7
        L2d:
            kotlin.jvm.internal.Ref$BooleanRef r1 = new kotlin.jvm.internal.Ref$BooleanRef
            r1.<init>()
            r4 = 0
            r1.element = r4
            kotlinx.coroutines.flow.Flow r4 = r8.$this_retry     // Catch: java.lang.Throwable -> L52
            kotlinx.coroutines.flow.FlowKt__ErrorsKt$retry$2$1 r5 = new kotlinx.coroutines.flow.FlowKt__ErrorsKt$retry$2$1     // Catch: java.lang.Throwable -> L52
            r6 = 0
            r5.<init>(r3, r1, r6)     // Catch: java.lang.Throwable -> L52
            kotlin.jvm.functions.Function2 r5 = (kotlin.jvm.functions.Function2) r5     // Catch: java.lang.Throwable -> L52
            r8.L$0 = r3     // Catch: java.lang.Throwable -> L52
            r8.I$0 = r2     // Catch: java.lang.Throwable -> L52
            r8.L$1 = r1     // Catch: java.lang.Throwable -> L52
            r6 = 1
            r8.label = r6     // Catch: java.lang.Throwable -> L52
            java.lang.Object r4 = kotlinx.coroutines.flow.FlowKt.collect(r4, r5, r8)     // Catch: java.lang.Throwable -> L52
            if (r4 != r0) goto L4f
            return r0
        L4f:
            kotlin.Unit r8 = kotlin.Unit.INSTANCE
            return r8
        L52:
            r4 = move-exception
        L53:
            boolean r1 = r1.element
            if (r1 != 0) goto L6c
            kotlin.jvm.functions.Function1 r1 = r8.$predicate
            java.lang.Object r1 = r1.invoke(r4)
            java.lang.Boolean r1 = (java.lang.Boolean) r1
            boolean r1 = r1.booleanValue()
            if (r1 == 0) goto L6b
            int r1 = r2 + (-1)
            if (r2 == 0) goto L6b
            r2 = r1
            goto L2d
        L6b:
            throw r4
        L6c:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__ErrorsKt$retry$2.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
