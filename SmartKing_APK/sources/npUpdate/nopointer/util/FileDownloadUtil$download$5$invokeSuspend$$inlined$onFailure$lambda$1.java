package npUpdate.nopointer.util;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlinx.coroutines.CoroutineScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: FileDownloadUtil.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0003\u0010\u0004¨\u0006\u0005"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "npUpdate/nopointer/util/FileDownloadUtil$download$5$3$1"}, k = 3, mv = {1, 1, 15})
/* loaded from: classes2.dex */
final class FileDownloadUtil$download$5$invokeSuspend$$inlined$onFailure$lambda$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ Ref.ObjectRef $connection$inlined;
    final /* synthetic */ Throwable $it;
    final /* synthetic */ Ref.ObjectRef $outputStream$inlined;
    final /* synthetic */ CoroutineScope $receiver$0$inlined;
    int label;
    private CoroutineScope p$;
    final /* synthetic */ FileDownloadUtil$download$5 this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FileDownloadUtil$download$5$invokeSuspend$$inlined$onFailure$lambda$1(Throwable th, Continuation continuation, FileDownloadUtil$download$5 fileDownloadUtil$download$5, CoroutineScope coroutineScope, Ref.ObjectRef objectRef, Ref.ObjectRef objectRef2) {
        super(2, continuation);
        this.$it = th;
        this.this$0 = fileDownloadUtil$download$5;
        this.$receiver$0$inlined = coroutineScope;
        this.$connection$inlined = objectRef;
        this.$outputStream$inlined = objectRef2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> completion) {
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        FileDownloadUtil$download$5$invokeSuspend$$inlined$onFailure$lambda$1 fileDownloadUtil$download$5$invokeSuspend$$inlined$onFailure$lambda$1 = new FileDownloadUtil$download$5$invokeSuspend$$inlined$onFailure$lambda$1(this.$it, completion, this.this$0, this.$receiver$0$inlined, this.$connection$inlined, this.$outputStream$inlined);
        fileDownloadUtil$download$5$invokeSuspend$$inlined$onFailure$lambda$1.p$ = (CoroutineScope) obj;
        return fileDownloadUtil$download$5$invokeSuspend$$inlined$onFailure$lambda$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((FileDownloadUtil$download$5$invokeSuspend$$inlined$onFailure$lambda$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @Nullable
    public final Object invokeSuspend(@NotNull Object obj) {
        IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (this.label != 0) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        ResultKt.throwOnFailure(obj);
        CoroutineScope coroutineScope = this.p$;
        this.this$0.$onError.invoke(this.$it);
        return Unit.INSTANCE;
    }
}
