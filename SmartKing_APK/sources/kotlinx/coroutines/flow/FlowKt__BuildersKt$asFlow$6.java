package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: Add missing generic type declarations: [T] */
/* compiled from: Builders.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", "T", "Lkotlinx/coroutines/flow/FlowCollector;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
@DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__BuildersKt$asFlow$6", f = "Builders.kt", i = {0, 0, 0}, l = {141}, m = "invokeSuspend", n = {"$this$forEach$iv", "element$iv", "value"}, s = {"L$1", "L$3", "L$4"})
/* loaded from: classes2.dex */
final class FlowKt__BuildersKt$asFlow$6<T> extends SuspendLambda implements Function2<FlowCollector<? super T>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Object[] $this_asFlow;
    int I$0;
    int I$1;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    Object L$4;
    int label;
    private FlowCollector p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FlowKt__BuildersKt$asFlow$6(Object[] objArr, Continuation continuation) {
        super(2, continuation);
        this.$this_asFlow = objArr;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> completion) {
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        FlowKt__BuildersKt$asFlow$6 flowKt__BuildersKt$asFlow$6 = new FlowKt__BuildersKt$asFlow$6(this.$this_asFlow, completion);
        flowKt__BuildersKt$asFlow$6.p$ = (FlowCollector) obj;
        return flowKt__BuildersKt$asFlow$6;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
        return ((FlowKt__BuildersKt$asFlow$6) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0007. Please report as an issue. */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0056  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x003b  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:10:0x0051 -> B:7:0x0054). Please report as a decompilation issue!!! */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @org.jetbrains.annotations.Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(@org.jetbrains.annotations.NotNull java.lang.Object r9) {
        /*
            r8 = this;
            java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r1 = r8.label
            r2 = 1
            switch(r1) {
                case 0: goto L2b;
                case 1: goto L12;
                default: goto La;
            }
        La:
            java.lang.IllegalStateException r9 = new java.lang.IllegalStateException
            java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
            r9.<init>(r0)
            throw r9
        L12:
            java.lang.Object r1 = r8.L$4
            java.lang.Object r1 = r8.L$3
            int r1 = r8.I$1
            int r3 = r8.I$0
            java.lang.Object r4 = r8.L$2
            java.lang.Object[] r4 = (java.lang.Object[]) r4
            java.lang.Object r5 = r8.L$1
            java.lang.Object[] r5 = (java.lang.Object[]) r5
            java.lang.Object r6 = r8.L$0
            kotlinx.coroutines.flow.FlowCollector r6 = (kotlinx.coroutines.flow.FlowCollector) r6
            kotlin.ResultKt.throwOnFailure(r9)
            r9 = r8
            goto L54
        L2b:
            kotlin.ResultKt.throwOnFailure(r9)
            kotlinx.coroutines.flow.FlowCollector r9 = r8.p$
            java.lang.Object[] r1 = r8.$this_asFlow
            int r3 = r1.length
            r4 = 0
            r6 = r9
            r4 = r1
            r5 = r4
            r1 = 0
            r9 = r8
        L39:
            if (r1 >= r3) goto L56
            r7 = r4[r1]
            r9.L$0 = r6
            r9.L$1 = r5
            r9.L$2 = r4
            r9.I$0 = r3
            r9.I$1 = r1
            r9.L$3 = r7
            r9.L$4 = r7
            r9.label = r2
            java.lang.Object r7 = r6.emit(r7, r9)
            if (r7 != r0) goto L54
            return r0
        L54:
            int r1 = r1 + r2
            goto L39
        L56:
            kotlin.Unit r9 = kotlin.Unit.INSTANCE
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__BuildersKt$asFlow$6.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
