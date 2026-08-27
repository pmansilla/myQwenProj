package kotlinx.coroutines.flow;

import basecamera.module.lib.CameraInterface;
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
import kotlinx.coroutines.channels.ReceiveChannel;
import kotlinx.coroutines.flow.FlowKt__DelayKt$sample$2;
import kotlinx.coroutines.flow.internal.NullSurrogate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Delay.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\b\u0004\n\u0002\b\u0004\n\u0002\b\u0005\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u0001H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005¨\u0006\u0006"}, d2 = {"<anonymous>", "", "T", "it", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "kotlinx/coroutines/flow/FlowKt__DelayKt$sample$2$1$1$2"}, k = 3, mv = {1, 1, 15})
@DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__DelayKt$sample$2$1$1$2", f = "Delay.kt", i = {0}, l = {CameraInterface.TYPE_RECORDER}, m = "invokeSuspend", n = {"value"}, s = {"L$0"})
/* loaded from: classes2.dex */
final class FlowKt__DelayKt$sample$2$1$invokeSuspend$$inlined$select$lambda$2 extends SuspendLambda implements Function2<Unit, Continuation<? super Unit>, Object> {
    final /* synthetic */ Ref.BooleanRef $isDone$inlined;
    final /* synthetic */ Ref.ObjectRef $lastValue$inlined;
    final /* synthetic */ ReceiveChannel $ticker$inlined;
    final /* synthetic */ ReceiveChannel $values$inlined;
    Object L$0;
    int label;
    private Unit p$0;
    final /* synthetic */ FlowKt__DelayKt$sample$2.AnonymousClass1 this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FlowKt__DelayKt$sample$2$1$invokeSuspend$$inlined$select$lambda$2(Continuation continuation, FlowKt__DelayKt$sample$2.AnonymousClass1 anonymousClass1, ReceiveChannel receiveChannel, ReceiveChannel receiveChannel2, Ref.BooleanRef booleanRef, Ref.ObjectRef objectRef) {
        super(2, continuation);
        this.this$0 = anonymousClass1;
        this.$values$inlined = receiveChannel;
        this.$ticker$inlined = receiveChannel2;
        this.$isDone$inlined = booleanRef;
        this.$lastValue$inlined = objectRef;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> completion) {
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        FlowKt__DelayKt$sample$2$1$invokeSuspend$$inlined$select$lambda$2 flowKt__DelayKt$sample$2$1$invokeSuspend$$inlined$select$lambda$2 = new FlowKt__DelayKt$sample$2$1$invokeSuspend$$inlined$select$lambda$2(completion, this.this$0, this.$values$inlined, this.$ticker$inlined, this.$isDone$inlined, this.$lastValue$inlined);
        flowKt__DelayKt$sample$2$1$invokeSuspend$$inlined$select$lambda$2.p$0 = (Unit) obj;
        return flowKt__DelayKt$sample$2$1$invokeSuspend$$inlined$select$lambda$2;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Unit unit, Continuation<? super Unit> continuation) {
        return ((FlowKt__DelayKt$sample$2$1$invokeSuspend$$inlined$select$lambda$2) create(unit, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @Nullable
    public final Object invokeSuspend(@NotNull Object obj) {
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure(obj);
                Unit unit = this.p$0;
                T t = this.$lastValue$inlined.element;
                if (t == 0) {
                    return Unit.INSTANCE;
                }
                this.$lastValue$inlined.element = null;
                FlowCollector flowCollector = this.this$0.$receiver$0;
                Object unbox$kotlinx_coroutines_core = NullSurrogate.unbox$kotlinx_coroutines_core(t);
                this.L$0 = t;
                this.label = 1;
                if (flowCollector.emit(unbox$kotlinx_coroutines_core, this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                Object obj2 = this.L$0;
                ResultKt.throwOnFailure(obj);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return Unit.INSTANCE;
    }
}
