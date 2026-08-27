package kotlinx.coroutines.flow;

import java.util.concurrent.CancellationException;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Delay.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\b\u0003\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, d2 = {"<anonymous>", "", "T", "it", "", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "kotlinx/coroutines/flow/FlowKt__DelayKt$sample$2$1$1$1"}, k = 3, mv = {1, 1, 15})
@DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__DelayKt$sample$2$1$1$1", f = "Delay.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
/* loaded from: classes2.dex */
final class FlowKt__DelayKt$sample$2$1$invokeSuspend$$inlined$select$lambda$1 extends SuspendLambda implements Function2<Object, Continuation<? super Unit>, Object> {
    final /* synthetic */ Ref.BooleanRef $isDone$inlined;
    final /* synthetic */ Ref.ObjectRef $lastValue$inlined;
    final /* synthetic */ ReceiveChannel $ticker$inlined;
    final /* synthetic */ ReceiveChannel $values$inlined;
    int label;
    private Object p$0;
    final /* synthetic */ FlowKt__DelayKt$sample$2.AnonymousClass1 this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FlowKt__DelayKt$sample$2$1$invokeSuspend$$inlined$select$lambda$1(Continuation continuation, FlowKt__DelayKt$sample$2.AnonymousClass1 anonymousClass1, ReceiveChannel receiveChannel, ReceiveChannel receiveChannel2, Ref.BooleanRef booleanRef, Ref.ObjectRef objectRef) {
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
        FlowKt__DelayKt$sample$2$1$invokeSuspend$$inlined$select$lambda$1 flowKt__DelayKt$sample$2$1$invokeSuspend$$inlined$select$lambda$1 = new FlowKt__DelayKt$sample$2$1$invokeSuspend$$inlined$select$lambda$1(completion, this.this$0, this.$values$inlined, this.$ticker$inlined, this.$isDone$inlined, this.$lastValue$inlined);
        flowKt__DelayKt$sample$2$1$invokeSuspend$$inlined$select$lambda$1.p$0 = obj;
        return flowKt__DelayKt$sample$2$1$invokeSuspend$$inlined$select$lambda$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
        return ((FlowKt__DelayKt$sample$2$1$invokeSuspend$$inlined$select$lambda$1) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Type inference failed for: r3v2, types: [T, java.lang.Object] */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @Nullable
    public final Object invokeSuspend(@NotNull Object obj) {
        IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (this.label != 0) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        ResultKt.throwOnFailure(obj);
        ?? r3 = this.p$0;
        if (r3 == 0) {
            ReceiveChannel.DefaultImpls.cancel$default(this.$ticker$inlined, (CancellationException) null, 1, (Object) null);
            this.$isDone$inlined.element = true;
        } else {
            this.$lastValue$inlined.element = r3;
        }
        return Unit.INSTANCE;
    }
}
