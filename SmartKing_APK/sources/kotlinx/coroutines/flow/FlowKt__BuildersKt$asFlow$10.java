package kotlinx.coroutines.flow;

import com.alibaba.fastjson.asm.Opcodes;
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
import kotlin.ranges.LongRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Builders.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/flow/FlowCollector;", "", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
@DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__BuildersKt$asFlow$10", f = "Builders.kt", i = {0, 0, 0}, l = {Opcodes.PUTFIELD}, m = "invokeSuspend", n = {"$this$forEach$iv", "element$iv", "value"}, s = {"L$1", "L$3", "J$0"})
/* loaded from: classes2.dex */
public final class FlowKt__BuildersKt$asFlow$10 extends SuspendLambda implements Function2<FlowCollector<? super Long>, Continuation<? super Unit>, Object> {
    final /* synthetic */ LongRange $this_asFlow;
    long J$0;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    int label;
    private FlowCollector p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FlowKt__BuildersKt$asFlow$10(LongRange longRange, Continuation continuation) {
        super(2, continuation);
        this.$this_asFlow = longRange;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> completion) {
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        FlowKt__BuildersKt$asFlow$10 flowKt__BuildersKt$asFlow$10 = new FlowKt__BuildersKt$asFlow$10(this.$this_asFlow, completion);
        flowKt__BuildersKt$asFlow$10.p$ = (FlowCollector) obj;
        return flowKt__BuildersKt$asFlow$10;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(FlowCollector<? super Long> flowCollector, Continuation<? super Unit> continuation) {
        return ((FlowKt__BuildersKt$asFlow$10) create(flowCollector, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @Nullable
    public final Object invokeSuspend(@NotNull Object obj) {
        FlowCollector flowCollector;
        FlowKt__BuildersKt$asFlow$10 flowKt__BuildersKt$asFlow$10;
        Iterable iterable;
        Iterator<Long> it;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure(obj);
                FlowCollector flowCollector2 = this.p$;
                LongRange longRange = this.$this_asFlow;
                flowCollector = flowCollector2;
                flowKt__BuildersKt$asFlow$10 = this;
                iterable = longRange;
                it = longRange.iterator();
                break;
            case 1:
                long j = this.J$0;
                Object obj2 = this.L$3;
                it = (Iterator) this.L$2;
                iterable = (Iterable) this.L$1;
                flowCollector = (FlowCollector) this.L$0;
                ResultKt.throwOnFailure(obj);
                flowKt__BuildersKt$asFlow$10 = this;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        while (it.hasNext()) {
            Long next = it.next();
            long longValue = next.longValue();
            Long boxLong = Boxing.boxLong(longValue);
            flowKt__BuildersKt$asFlow$10.L$0 = flowCollector;
            flowKt__BuildersKt$asFlow$10.L$1 = iterable;
            flowKt__BuildersKt$asFlow$10.L$2 = it;
            flowKt__BuildersKt$asFlow$10.L$3 = next;
            flowKt__BuildersKt$asFlow$10.J$0 = longValue;
            flowKt__BuildersKt$asFlow$10.label = 1;
            if (flowCollector.emit(boxLong, flowKt__BuildersKt$asFlow$10) == coroutine_suspended) {
                return coroutine_suspended;
            }
        }
        return Unit.INSTANCE;
    }
}
