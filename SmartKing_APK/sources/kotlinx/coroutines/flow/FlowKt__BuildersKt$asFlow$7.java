package kotlinx.coroutines.flow;

import com.alibaba.fastjson.asm.Opcodes;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Builders.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/flow/FlowCollector;", "", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
@DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__BuildersKt$asFlow$7", f = "Builders.kt", i = {0, 0, 0}, l = {Opcodes.DCMPL}, m = "invokeSuspend", n = {"$this$forEach$iv", "element$iv", "value"}, s = {"L$1", "I$2", "I$3"})
/* loaded from: classes2.dex */
final class FlowKt__BuildersKt$asFlow$7 extends SuspendLambda implements Function2<FlowCollector<? super Integer>, Continuation<? super Unit>, Object> {
    final /* synthetic */ int[] $this_asFlow;
    int I$0;
    int I$1;
    int I$2;
    int I$3;
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    private FlowCollector p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FlowKt__BuildersKt$asFlow$7(int[] iArr, Continuation continuation) {
        super(2, continuation);
        this.$this_asFlow = iArr;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> completion) {
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        FlowKt__BuildersKt$asFlow$7 flowKt__BuildersKt$asFlow$7 = new FlowKt__BuildersKt$asFlow$7(this.$this_asFlow, completion);
        flowKt__BuildersKt$asFlow$7.p$ = (FlowCollector) obj;
        return flowKt__BuildersKt$asFlow$7;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(FlowCollector<? super Integer> flowCollector, Continuation<? super Unit> continuation) {
        return ((FlowKt__BuildersKt$asFlow$7) create(flowCollector, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0007. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0064  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x003b  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:10:0x005f -> B:7:0x0062). Please report as a decompilation issue!!! */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @org.jetbrains.annotations.Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(@org.jetbrains.annotations.NotNull java.lang.Object r11) {
        /*
            r10 = this;
            java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r1 = r10.label
            r2 = 1
            switch(r1) {
                case 0: goto L2b;
                case 1: goto L12;
                default: goto La;
            }
        La:
            java.lang.IllegalStateException r11 = new java.lang.IllegalStateException
            java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
            r11.<init>(r0)
            throw r11
        L12:
            int r1 = r10.I$3
            int r1 = r10.I$2
            int r1 = r10.I$1
            int r3 = r10.I$0
            java.lang.Object r4 = r10.L$2
            int[] r4 = (int[]) r4
            java.lang.Object r5 = r10.L$1
            int[] r5 = (int[]) r5
            java.lang.Object r6 = r10.L$0
            kotlinx.coroutines.flow.FlowCollector r6 = (kotlinx.coroutines.flow.FlowCollector) r6
            kotlin.ResultKt.throwOnFailure(r11)
            r11 = r10
            goto L62
        L2b:
            kotlin.ResultKt.throwOnFailure(r11)
            kotlinx.coroutines.flow.FlowCollector r11 = r10.p$
            int[] r1 = r10.$this_asFlow
            int r3 = r1.length
            r4 = 0
            r6 = r11
            r4 = r1
            r5 = r4
            r1 = 0
            r11 = r10
        L39:
            if (r1 >= r3) goto L64
            r7 = r4[r1]
            java.lang.Integer r8 = kotlin.coroutines.jvm.internal.Boxing.boxInt(r7)
            java.lang.Number r8 = (java.lang.Number) r8
            int r8 = r8.intValue()
            java.lang.Integer r9 = kotlin.coroutines.jvm.internal.Boxing.boxInt(r8)
            r11.L$0 = r6
            r11.L$1 = r5
            r11.L$2 = r4
            r11.I$0 = r3
            r11.I$1 = r1
            r11.I$2 = r7
            r11.I$3 = r8
            r11.label = r2
            java.lang.Object r7 = r6.emit(r9, r11)
            if (r7 != r0) goto L62
            return r0
        L62:
            int r1 = r1 + r2
            goto L39
        L64:
            kotlin.Unit r11 = kotlin.Unit.INSTANCE
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__BuildersKt$asFlow$7.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
