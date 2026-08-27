package kotlinx.coroutines.flow;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: Add missing generic type declarations: [T] */
/* compiled from: Builders.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", "T", "Lkotlinx/coroutines/flow/FlowCollector;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
@DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__BuildersKt$asFlow$3", f = "Builders.kt", i = {0, 0, 0}, l = {91}, m = "invokeSuspend", n = {"$this$forEach$iv", "element$iv", "value"}, s = {"L$1", "L$3", "L$4"})
/* loaded from: classes2.dex */
final class FlowKt__BuildersKt$asFlow$3<T> extends SuspendLambda implements Function2<FlowCollector<? super T>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Iterable $this_asFlow;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    Object L$4;
    int label;
    private FlowCollector p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FlowKt__BuildersKt$asFlow$3(Iterable iterable, Continuation continuation) {
        super(2, continuation);
        this.$this_asFlow = iterable;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> completion) {
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        FlowKt__BuildersKt$asFlow$3 flowKt__BuildersKt$asFlow$3 = new FlowKt__BuildersKt$asFlow$3(this.$this_asFlow, completion);
        flowKt__BuildersKt$asFlow$3.p$ = (FlowCollector) obj;
        return flowKt__BuildersKt$asFlow$3;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
        return ((FlowKt__BuildersKt$asFlow$3) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @Nullable
    public final Object invokeSuspend(@NotNull Object obj) {
        FlowCollector flowCollector;
        FlowKt__BuildersKt$asFlow$3<T> flowKt__BuildersKt$asFlow$3;
        Iterable iterable;
        Iterator<T> it;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure(obj);
                FlowCollector flowCollector2 = this.p$;
                Iterable iterable2 = this.$this_asFlow;
                flowCollector = flowCollector2;
                flowKt__BuildersKt$asFlow$3 = this;
                iterable = iterable2;
                it = iterable2.iterator();
                break;
            case 1:
                Object obj2 = this.L$4;
                Object obj3 = this.L$3;
                it = (Iterator) this.L$2;
                iterable = (Iterable) this.L$1;
                flowCollector = (FlowCollector) this.L$0;
                ResultKt.throwOnFailure(obj);
                flowKt__BuildersKt$asFlow$3 = this;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        while (it.hasNext()) {
            T next = it.next();
            flowKt__BuildersKt$asFlow$3.L$0 = flowCollector;
            flowKt__BuildersKt$asFlow$3.L$1 = iterable;
            flowKt__BuildersKt$asFlow$3.L$2 = it;
            flowKt__BuildersKt$asFlow$3.L$3 = next;
            flowKt__BuildersKt$asFlow$3.L$4 = next;
            flowKt__BuildersKt$asFlow$3.label = 1;
            if (flowCollector.emit(next, flowKt__BuildersKt$asFlow$3) == coroutine_suspended) {
                return coroutine_suspended;
            }
        }
        return Unit.INSTANCE;
    }
}
