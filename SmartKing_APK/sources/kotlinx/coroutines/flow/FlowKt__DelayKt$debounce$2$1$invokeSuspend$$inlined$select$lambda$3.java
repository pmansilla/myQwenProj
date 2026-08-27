package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.channels.Channel;
import kotlinx.coroutines.flow.FlowKt__DelayKt$debounce$2;
import kotlinx.coroutines.flow.internal.NullSurrogate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Delay.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0004\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0003\u0010\u0004¨\u0006\u0005"}, d2 = {"<anonymous>", "", "T", "invoke", "(Ljava/lang/Object;)Ljava/lang/Object;", "kotlinx/coroutines/flow/FlowKt__DelayKt$debounce$2$1$1$3"}, k = 3, mv = {1, 1, 15})
@DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__DelayKt$debounce$2$1$1$3", f = "Delay.kt", i = {}, l = {93}, m = "invokeSuspend", n = {}, s = {})
/* loaded from: classes2.dex */
final class FlowKt__DelayKt$debounce$2$1$invokeSuspend$$inlined$select$lambda$3 extends SuspendLambda implements Function1<Continuation<? super Unit>, Object> {
    final /* synthetic */ Job $collector$inlined;
    final /* synthetic */ Ref.BooleanRef $isDone$inlined;
    final /* synthetic */ Ref.ObjectRef $lastValue$inlined;
    final /* synthetic */ Channel $values$inlined;
    int label;
    final /* synthetic */ FlowKt__DelayKt$debounce$2.AnonymousClass1 this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FlowKt__DelayKt$debounce$2$1$invokeSuspend$$inlined$select$lambda$3(Continuation continuation, FlowKt__DelayKt$debounce$2.AnonymousClass1 anonymousClass1, Channel channel, Ref.ObjectRef objectRef, Job job, Ref.BooleanRef booleanRef) {
        super(1, continuation);
        this.this$0 = anonymousClass1;
        this.$values$inlined = channel;
        this.$lastValue$inlined = objectRef;
        this.$collector$inlined = job;
        this.$isDone$inlined = booleanRef;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @NotNull
    public final Continuation<Unit> create(@NotNull Continuation<?> completion) {
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        return new FlowKt__DelayKt$debounce$2$1$invokeSuspend$$inlined$select$lambda$3(completion, this.this$0, this.$values$inlined, this.$lastValue$inlined, this.$collector$inlined, this.$isDone$inlined);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Object invoke(Continuation<? super Unit> continuation) {
        return ((FlowKt__DelayKt$debounce$2$1$invokeSuspend$$inlined$select$lambda$3) create(continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @Nullable
    public final Object invokeSuspend(@NotNull Object obj) {
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure(obj);
                if (this.$lastValue$inlined.element != 0) {
                    FlowCollector flowCollector = this.this$0.$receiver$0;
                    Object unbox$kotlinx_coroutines_core = NullSurrogate.unbox$kotlinx_coroutines_core(this.$lastValue$inlined.element);
                    this.label = 1;
                    if (flowCollector.emit(unbox$kotlinx_coroutines_core, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                }
                break;
            case 1:
                ResultKt.throwOnFailure(obj);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        this.$isDone$inlined.element = true;
        return Unit.INSTANCE;
    }
}
