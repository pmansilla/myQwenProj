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
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/flow/FlowCollector;", "", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
@DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__BuildersKt$asFlow$8", f = "Builders.kt", i = {0, 0, 0}, l = {Opcodes.IF_ICMPLT}, m = "invokeSuspend", n = {"$this$forEach$iv", "element$iv", "value"}, s = {"L$1", "J$0", "J$1"})
/* loaded from: classes2.dex */
final class FlowKt__BuildersKt$asFlow$8 extends SuspendLambda implements Function2<FlowCollector<? super Long>, Continuation<? super Unit>, Object> {
    final /* synthetic */ long[] $this_asFlow;
    int I$0;
    int I$1;
    long J$0;
    long J$1;
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    private FlowCollector p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FlowKt__BuildersKt$asFlow$8(long[] jArr, Continuation continuation) {
        super(2, continuation);
        this.$this_asFlow = jArr;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> completion) {
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        FlowKt__BuildersKt$asFlow$8 flowKt__BuildersKt$asFlow$8 = new FlowKt__BuildersKt$asFlow$8(this.$this_asFlow, completion);
        flowKt__BuildersKt$asFlow$8.p$ = (FlowCollector) obj;
        return flowKt__BuildersKt$asFlow$8;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(FlowCollector<? super Long> flowCollector, Continuation<? super Unit> continuation) {
        return ((FlowKt__BuildersKt$asFlow$8) create(flowCollector, continuation)).invokeSuspend(Unit.INSTANCE);
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
    public final java.lang.Object invokeSuspend(@org.jetbrains.annotations.NotNull java.lang.Object r13) {
        /*
            r12 = this;
            java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r1 = r12.label
            r2 = 1
            switch(r1) {
                case 0: goto L2b;
                case 1: goto L12;
                default: goto La;
            }
        La:
            java.lang.IllegalStateException r13 = new java.lang.IllegalStateException
            java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
            r13.<init>(r0)
            throw r13
        L12:
            long r3 = r12.J$1
            long r3 = r12.J$0
            int r1 = r12.I$1
            int r3 = r12.I$0
            java.lang.Object r4 = r12.L$2
            long[] r4 = (long[]) r4
            java.lang.Object r5 = r12.L$1
            long[] r5 = (long[]) r5
            java.lang.Object r6 = r12.L$0
            kotlinx.coroutines.flow.FlowCollector r6 = (kotlinx.coroutines.flow.FlowCollector) r6
            kotlin.ResultKt.throwOnFailure(r13)
            r13 = r12
            goto L62
        L2b:
            kotlin.ResultKt.throwOnFailure(r13)
            kotlinx.coroutines.flow.FlowCollector r13 = r12.p$
            long[] r1 = r12.$this_asFlow
            int r3 = r1.length
            r4 = 0
            r6 = r13
            r4 = r1
            r5 = r4
            r1 = 0
            r13 = r12
        L39:
            if (r1 >= r3) goto L64
            r7 = r4[r1]
            java.lang.Long r9 = kotlin.coroutines.jvm.internal.Boxing.boxLong(r7)
            java.lang.Number r9 = (java.lang.Number) r9
            long r9 = r9.longValue()
            java.lang.Long r11 = kotlin.coroutines.jvm.internal.Boxing.boxLong(r9)
            r13.L$0 = r6
            r13.L$1 = r5
            r13.L$2 = r4
            r13.I$0 = r3
            r13.I$1 = r1
            r13.J$0 = r7
            r13.J$1 = r9
            r13.label = r2
            java.lang.Object r7 = r6.emit(r11, r13)
            if (r7 != r0) goto L62
            return r0
        L62:
            int r1 = r1 + r2
            goto L39
        L64:
            kotlin.Unit r13 = kotlin.Unit.INSTANCE
            return r13
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__BuildersKt$asFlow$8.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
