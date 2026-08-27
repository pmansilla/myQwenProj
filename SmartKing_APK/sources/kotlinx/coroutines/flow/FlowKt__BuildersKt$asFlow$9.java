package kotlinx.coroutines.flow;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Builders.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/flow/FlowCollector;", "", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
@DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__BuildersKt$asFlow$9", f = "Builders.kt", i = {0, 0, 0}, l = {171}, m = "invokeSuspend", n = {"$this$forEach$iv", "element$iv", "value"}, s = {"L$1", "L$3", "I$0"})
/* loaded from: classes2.dex */
public final class FlowKt__BuildersKt$asFlow$9 extends SuspendLambda implements Function2<FlowCollector<? super Integer>, Continuation<? super Unit>, Object> {
    final /* synthetic */ IntRange $this_asFlow;
    int I$0;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    int label;
    private FlowCollector p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FlowKt__BuildersKt$asFlow$9(IntRange intRange, Continuation continuation) {
        super(2, continuation);
        this.$this_asFlow = intRange;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> completion) {
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        FlowKt__BuildersKt$asFlow$9 flowKt__BuildersKt$asFlow$9 = new FlowKt__BuildersKt$asFlow$9(this.$this_asFlow, completion);
        flowKt__BuildersKt$asFlow$9.p$ = (FlowCollector) obj;
        return flowKt__BuildersKt$asFlow$9;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(FlowCollector<? super Integer> flowCollector, Continuation<? super Unit> continuation) {
        return ((FlowKt__BuildersKt$asFlow$9) create(flowCollector, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @Nullable
    public final Object invokeSuspend(@NotNull Object obj) {
        FlowCollector flowCollector;
        FlowKt__BuildersKt$asFlow$9 flowKt__BuildersKt$asFlow$9;
        Iterable iterable;
        Iterator<Integer> it;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure(obj);
                FlowCollector flowCollector2 = this.p$;
                IntRange intRange = this.$this_asFlow;
                flowCollector = flowCollector2;
                flowKt__BuildersKt$asFlow$9 = this;
                iterable = intRange;
                it = intRange.iterator();
                break;
            case 1:
                int i = this.I$0;
                Object obj2 = this.L$3;
                it = (Iterator) this.L$2;
                iterable = (Iterable) this.L$1;
                flowCollector = (FlowCollector) this.L$0;
                ResultKt.throwOnFailure(obj);
                flowKt__BuildersKt$asFlow$9 = this;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        while (it.hasNext()) {
            Integer next = it.next();
            int intValue = next.intValue();
            Integer boxInt = Boxing.boxInt(intValue);
            flowKt__BuildersKt$asFlow$9.L$0 = flowCollector;
            flowKt__BuildersKt$asFlow$9.L$1 = iterable;
            flowKt__BuildersKt$asFlow$9.L$2 = it;
            flowKt__BuildersKt$asFlow$9.L$3 = next;
            flowKt__BuildersKt$asFlow$9.I$0 = intValue;
            flowKt__BuildersKt$asFlow$9.label = 1;
            if (flowCollector.emit(boxInt, flowKt__BuildersKt$asFlow$9) == coroutine_suspended) {
                return coroutine_suspended;
            }
        }
        return Unit.INSTANCE;
    }
}
