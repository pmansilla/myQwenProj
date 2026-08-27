package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlinx.coroutines.channels.Channel;
import kotlinx.coroutines.flow.FlowKt__ZipKt$combineLatest$1;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Zip.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000 \n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0004\u0010\u0000\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005¨\u0006\u0007"}, d2 = {"<anonymous>", "", "it", "", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "kotlinx/coroutines/flow/FlowKt__ZipKt$onReceive$1", "kotlinx/coroutines/flow/FlowKt__ZipKt$combineLatest$1$1$$special$$inlined$onReceive$FlowKt__ZipKt$1"}, k = 3, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final class FlowKt__ZipKt$combineLatest$1$1$invokeSuspend$$inlined$select$lambda$2 extends SuspendLambda implements Function2<Object, Continuation<? super Unit>, Object> {
    final /* synthetic */ Channel $firstChannel$inlined;
    final /* synthetic */ Ref.BooleanRef $firstIsClosed$inlined;
    final /* synthetic */ Ref.ObjectRef $firstValue$inlined;
    final /* synthetic */ Function2 $onReceive;
    final /* synthetic */ Channel $secondChannel$inlined;
    final /* synthetic */ Ref.BooleanRef $secondIsClosed$inlined;
    final /* synthetic */ Ref.ObjectRef $secondValue$inlined;
    int label;
    private Object p$0;
    final /* synthetic */ FlowKt__ZipKt$combineLatest$1.AnonymousClass1 this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FlowKt__ZipKt$combineLatest$1$1$invokeSuspend$$inlined$select$lambda$2(Function2 function2, Continuation continuation, FlowKt__ZipKt$combineLatest$1.AnonymousClass1 anonymousClass1, Ref.BooleanRef booleanRef, Channel channel, Ref.ObjectRef objectRef, Ref.ObjectRef objectRef2, Ref.BooleanRef booleanRef2, Channel channel2) {
        super(2, continuation);
        this.$onReceive = function2;
        this.this$0 = anonymousClass1;
        this.$firstIsClosed$inlined = booleanRef;
        this.$firstChannel$inlined = channel;
        this.$firstValue$inlined = objectRef;
        this.$secondValue$inlined = objectRef2;
        this.$secondIsClosed$inlined = booleanRef2;
        this.$secondChannel$inlined = channel2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> completion) {
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        FlowKt__ZipKt$combineLatest$1$1$invokeSuspend$$inlined$select$lambda$2 flowKt__ZipKt$combineLatest$1$1$invokeSuspend$$inlined$select$lambda$2 = new FlowKt__ZipKt$combineLatest$1$1$invokeSuspend$$inlined$select$lambda$2(this.$onReceive, completion, this.this$0, this.$firstIsClosed$inlined, this.$firstChannel$inlined, this.$firstValue$inlined, this.$secondValue$inlined, this.$secondIsClosed$inlined, this.$secondChannel$inlined);
        flowKt__ZipKt$combineLatest$1$1$invokeSuspend$$inlined$select$lambda$2.p$0 = obj;
        return flowKt__ZipKt$combineLatest$1$1$invokeSuspend$$inlined$select$lambda$2;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
        return ((FlowKt__ZipKt$combineLatest$1$1$invokeSuspend$$inlined$select$lambda$2) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @Nullable
    public final Object invokeSuspend(@NotNull Object obj) {
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure(obj);
                Object obj2 = this.p$0;
                if (obj2 == null) {
                    this.$firstIsClosed$inlined.element = true;
                    break;
                } else {
                    Function2 function2 = this.$onReceive;
                    this.label = 1;
                    if (function2.invoke(obj2, this) == coroutine_suspended) {
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
        return Unit.INSTANCE;
    }
}
