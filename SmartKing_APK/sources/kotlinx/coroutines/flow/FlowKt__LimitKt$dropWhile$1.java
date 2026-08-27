package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: Add missing generic type declarations: [T] */
/* compiled from: Limit.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", "T", "Lkotlinx/coroutines/flow/FlowCollector;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
@DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__LimitKt$dropWhile$1", f = "Limit.kt", i = {0}, l = {36}, m = "invokeSuspend", n = {"matched"}, s = {"L$0"})
/* loaded from: classes2.dex */
public final class FlowKt__LimitKt$dropWhile$1<T> extends SuspendLambda implements Function2<FlowCollector<? super T>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function2 $predicate;
    final /* synthetic */ Flow $this_dropWhile;
    Object L$0;
    int label;
    private FlowCollector p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: Limit.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u0002H\u0002H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", "T", "value", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
    @DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__LimitKt$dropWhile$1$1", f = "Limit.kt", i = {}, l = {38, 39, 41}, m = "invokeSuspend", n = {}, s = {})
    /* renamed from: kotlinx.coroutines.flow.FlowKt__LimitKt$dropWhile$1$1, reason: invalid class name */
    /* loaded from: classes2.dex */
    public static final class AnonymousClass1 extends SuspendLambda implements Function2<T, Continuation<? super Unit>, Object> {
        final /* synthetic */ Ref.BooleanRef $matched;
        final /* synthetic */ FlowCollector $receiver$0;
        Object L$0;
        int label;
        private Object p$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(FlowCollector flowCollector, Ref.BooleanRef booleanRef, Continuation continuation) {
            super(2, continuation);
            this.$receiver$0 = flowCollector;
            this.$matched = booleanRef;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @NotNull
        public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> completion) {
            Intrinsics.checkParameterIsNotNull(completion, "completion");
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(this.$receiver$0, this.$matched, completion);
            anonymousClass1.p$0 = obj;
            return anonymousClass1;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
            return ((AnonymousClass1) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0007. Please report as an issue. */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:9:0x004a  */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @org.jetbrains.annotations.Nullable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final java.lang.Object invokeSuspend(@org.jetbrains.annotations.NotNull java.lang.Object r5) {
            /*
                r4 = this;
                java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
                int r1 = r4.label
                r2 = 1
                switch(r1) {
                    case 0: goto L1c;
                    case 1: goto L18;
                    case 2: goto L12;
                    case 3: goto L18;
                    default: goto La;
                }
            La:
                java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
                java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
                r5.<init>(r0)
                throw r5
            L12:
                java.lang.Object r1 = r4.L$0
                kotlin.ResultKt.throwOnFailure(r5)
                goto L42
            L18:
                kotlin.ResultKt.throwOnFailure(r5)
                goto L5a
            L1c:
                kotlin.ResultKt.throwOnFailure(r5)
                java.lang.Object r1 = r4.p$0
                kotlin.jvm.internal.Ref$BooleanRef r5 = r4.$matched
                boolean r5 = r5.element
                if (r5 == 0) goto L32
                kotlinx.coroutines.flow.FlowCollector r5 = r4.$receiver$0
                r4.label = r2
                java.lang.Object r5 = r5.emit(r1, r4)
                if (r5 != r0) goto L5a
                return r0
            L32:
                kotlinx.coroutines.flow.FlowKt__LimitKt$dropWhile$1 r5 = kotlinx.coroutines.flow.FlowKt__LimitKt$dropWhile$1.this
                kotlin.jvm.functions.Function2 r5 = r5.$predicate
                r4.L$0 = r1
                r3 = 2
                r4.label = r3
                java.lang.Object r5 = r5.invoke(r1, r4)
                if (r5 != r0) goto L42
                return r0
            L42:
                java.lang.Boolean r5 = (java.lang.Boolean) r5
                boolean r5 = r5.booleanValue()
                if (r5 != 0) goto L5a
                kotlin.jvm.internal.Ref$BooleanRef r5 = r4.$matched
                r5.element = r2
                kotlinx.coroutines.flow.FlowCollector r5 = r4.$receiver$0
                r2 = 3
                r4.label = r2
                java.lang.Object r5 = r5.emit(r1, r4)
                if (r5 != r0) goto L5a
                return r0
            L5a:
                kotlin.Unit r5 = kotlin.Unit.INSTANCE
                return r5
            */
            throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__LimitKt$dropWhile$1.AnonymousClass1.invokeSuspend(java.lang.Object):java.lang.Object");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FlowKt__LimitKt$dropWhile$1(Flow flow, Function2 function2, Continuation continuation) {
        super(2, continuation);
        this.$this_dropWhile = flow;
        this.$predicate = function2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> completion) {
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        FlowKt__LimitKt$dropWhile$1 flowKt__LimitKt$dropWhile$1 = new FlowKt__LimitKt$dropWhile$1(this.$this_dropWhile, this.$predicate, completion);
        flowKt__LimitKt$dropWhile$1.p$ = (FlowCollector) obj;
        return flowKt__LimitKt$dropWhile$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
        return ((FlowKt__LimitKt$dropWhile$1) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @Nullable
    public final Object invokeSuspend(@NotNull Object obj) {
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure(obj);
                FlowCollector flowCollector = this.p$;
                Ref.BooleanRef booleanRef = new Ref.BooleanRef();
                booleanRef.element = false;
                Flow flow = this.$this_dropWhile;
                AnonymousClass1 anonymousClass1 = new AnonymousClass1(flowCollector, booleanRef, null);
                this.L$0 = booleanRef;
                this.label = 1;
                if (FlowKt.collect(flow, anonymousClass1, this) == coroutine_suspended) {
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
    }
}
