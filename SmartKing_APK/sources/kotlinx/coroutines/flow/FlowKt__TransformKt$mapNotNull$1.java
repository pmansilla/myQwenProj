package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: Add missing generic type declarations: [R, T] */
/* compiled from: Transform.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\b\b\u0001\u0010\u0003*\u00020\u0004*\b\u0012\u0004\u0012\u0002H\u00030\u00052\u0006\u0010\u0006\u001a\u0002H\u0002H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0007\u0010\b"}, d2 = {"<anonymous>", "", "T", "R", "", "Lkotlinx/coroutines/flow/FlowCollector;", "value", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
@DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__TransformKt$mapNotNull$1", f = "Transform.kt", i = {1}, l = {83, 84}, m = "invokeSuspend", n = {"transformed"}, s = {"L$0"})
/* loaded from: classes2.dex */
public final class FlowKt__TransformKt$mapNotNull$1<R, T> extends SuspendLambda implements Function3<FlowCollector<? super R>, T, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function2 $transformer;
    Object L$0;
    int label;
    private FlowCollector p$;
    private Object p$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FlowKt__TransformKt$mapNotNull$1(Function2 function2, Continuation continuation) {
        super(3, continuation);
        this.$transformer = function2;
    }

    @NotNull
    public final Continuation<Unit> create(@NotNull FlowCollector<? super R> create, T t, @NotNull Continuation<? super Unit> continuation) {
        Intrinsics.checkParameterIsNotNull(create, "$this$create");
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        FlowKt__TransformKt$mapNotNull$1 flowKt__TransformKt$mapNotNull$1 = new FlowKt__TransformKt$mapNotNull$1(this.$transformer, continuation);
        flowKt__TransformKt$mapNotNull$1.p$ = create;
        flowKt__TransformKt$mapNotNull$1.p$0 = t;
        return flowKt__TransformKt$mapNotNull$1;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlin.jvm.functions.Function3
    public final Object invoke(Object obj, Object obj2, Continuation<? super Unit> continuation) {
        return ((FlowKt__TransformKt$mapNotNull$1) create((FlowCollector) obj, obj2, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0006. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0036  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0045  */
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
            switch(r1) {
                case 0: goto L1f;
                case 1: goto L17;
                case 2: goto L11;
                default: goto L9;
            }
        L9:
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
            r5.<init>(r0)
            throw r5
        L11:
            java.lang.Object r0 = r4.L$0
            kotlin.ResultKt.throwOnFailure(r5)
            goto L42
        L17:
            java.lang.Object r1 = r4.L$0
            kotlinx.coroutines.flow.FlowCollector r1 = (kotlinx.coroutines.flow.FlowCollector) r1
            kotlin.ResultKt.throwOnFailure(r5)
            goto L34
        L1f:
            kotlin.ResultKt.throwOnFailure(r5)
            kotlinx.coroutines.flow.FlowCollector r1 = r4.p$
            java.lang.Object r5 = r4.p$0
            kotlin.jvm.functions.Function2 r2 = r4.$transformer
            r4.L$0 = r1
            r3 = 1
            r4.label = r3
            java.lang.Object r5 = r2.invoke(r5, r4)
            if (r5 != r0) goto L34
            return r0
        L34:
            if (r5 == 0) goto L45
            r4.L$0 = r5
            r2 = 2
            r4.label = r2
            java.lang.Object r5 = r1.emit(r5, r4)
            if (r5 != r0) goto L42
            return r0
        L42:
            kotlin.Unit r5 = kotlin.Unit.INSTANCE
            return r5
        L45:
            kotlin.Unit r5 = kotlin.Unit.INSTANCE
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__TransformKt$mapNotNull$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
