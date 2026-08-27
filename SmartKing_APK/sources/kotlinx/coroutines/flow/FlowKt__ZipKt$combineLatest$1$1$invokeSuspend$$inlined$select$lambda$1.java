package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlinx.coroutines.channels.Channel;
import kotlinx.coroutines.flow.FlowKt__ZipKt$combineLatest$1;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Zip.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\b\u0004\n\u0002\b\u0004\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\b\u0003\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u008a@ø\u0001\u0000¢\u0006\u0004\b\u0007\u0010\b¨\u0006\t"}, d2 = {"<anonymous>", "", "T1", "T2", "R", "value", "", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "kotlinx/coroutines/flow/FlowKt__ZipKt$combineLatest$1$1$1$2"}, k = 3, mv = {1, 1, 15})
@DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__ZipKt$combineLatest$1$1$1$2", f = "Zip.kt", i = {}, l = {63, 63}, m = "invokeSuspend", n = {}, s = {})
/* loaded from: classes2.dex */
final class FlowKt__ZipKt$combineLatest$1$1$invokeSuspend$$inlined$select$lambda$1 extends SuspendLambda implements Function2<Object, Continuation<? super Unit>, Object> {
    final /* synthetic */ Channel $firstChannel$inlined;
    final /* synthetic */ Ref.BooleanRef $firstIsClosed$inlined;
    final /* synthetic */ Ref.ObjectRef $firstValue$inlined;
    final /* synthetic */ Channel $secondChannel$inlined;
    final /* synthetic */ Ref.BooleanRef $secondIsClosed$inlined;
    final /* synthetic */ Ref.ObjectRef $secondValue$inlined;
    Object L$0;
    int label;
    private Object p$0;
    final /* synthetic */ FlowKt__ZipKt$combineLatest$1.AnonymousClass1 this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FlowKt__ZipKt$combineLatest$1$1$invokeSuspend$$inlined$select$lambda$1(Continuation continuation, FlowKt__ZipKt$combineLatest$1.AnonymousClass1 anonymousClass1, Ref.BooleanRef booleanRef, Channel channel, Ref.ObjectRef objectRef, Ref.ObjectRef objectRef2, Ref.BooleanRef booleanRef2, Channel channel2) {
        super(2, continuation);
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
        FlowKt__ZipKt$combineLatest$1$1$invokeSuspend$$inlined$select$lambda$1 flowKt__ZipKt$combineLatest$1$1$invokeSuspend$$inlined$select$lambda$1 = new FlowKt__ZipKt$combineLatest$1$1$invokeSuspend$$inlined$select$lambda$1(completion, this.this$0, this.$firstIsClosed$inlined, this.$firstChannel$inlined, this.$firstValue$inlined, this.$secondValue$inlined, this.$secondIsClosed$inlined, this.$secondChannel$inlined);
        flowKt__ZipKt$combineLatest$1$1$invokeSuspend$$inlined$select$lambda$1.p$0 = obj;
        return flowKt__ZipKt$combineLatest$1$1$invokeSuspend$$inlined$select$lambda$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Continuation<? super Unit> continuation) {
        return ((FlowKt__ZipKt$combineLatest$1$1$invokeSuspend$$inlined$select$lambda$1) create(obj, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0006. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:12:0x005b A[RETURN] */
    /* JADX WARN: Type inference failed for: r6v1, types: [T, java.lang.Object] */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @org.jetbrains.annotations.Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(@org.jetbrains.annotations.NotNull java.lang.Object r6) {
        /*
            r5 = this;
            java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r1 = r5.label
            switch(r1) {
                case 0: goto L1d;
                case 1: goto L15;
                case 2: goto L11;
                default: goto L9;
            }
        L9:
            java.lang.IllegalStateException r6 = new java.lang.IllegalStateException
            java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
            r6.<init>(r0)
            throw r6
        L11:
            kotlin.ResultKt.throwOnFailure(r6)
            goto L5c
        L15:
            java.lang.Object r1 = r5.L$0
            kotlinx.coroutines.flow.FlowCollector r1 = (kotlinx.coroutines.flow.FlowCollector) r1
            kotlin.ResultKt.throwOnFailure(r6)
            goto L52
        L1d:
            kotlin.ResultKt.throwOnFailure(r6)
            java.lang.Object r6 = r5.p$0
            kotlin.jvm.internal.Ref$ObjectRef r1 = r5.$firstValue$inlined
            r1.element = r6
            kotlin.jvm.internal.Ref$ObjectRef r6 = r5.$secondValue$inlined
            T r6 = r6.element
            if (r6 == 0) goto L5c
            kotlinx.coroutines.flow.FlowKt__ZipKt$combineLatest$1$1 r6 = r5.this$0
            kotlinx.coroutines.flow.FlowCollector r1 = r6.$receiver$0
            kotlinx.coroutines.flow.FlowKt__ZipKt$combineLatest$1$1 r6 = r5.this$0
            kotlinx.coroutines.flow.FlowKt__ZipKt$combineLatest$1 r6 = kotlinx.coroutines.flow.FlowKt__ZipKt$combineLatest$1.this
            kotlin.jvm.functions.Function3 r6 = r6.$transform
            kotlin.jvm.internal.Ref$ObjectRef r2 = r5.$firstValue$inlined
            T r2 = r2.element
            java.lang.Object r2 = kotlinx.coroutines.flow.internal.NullSurrogate.unbox$kotlinx_coroutines_core(r2)
            kotlin.jvm.internal.Ref$ObjectRef r3 = r5.$secondValue$inlined
            T r3 = r3.element
            java.lang.Object r3 = kotlinx.coroutines.flow.internal.NullSurrogate.unbox$kotlinx_coroutines_core(r3)
            r5.L$0 = r1
            r4 = 1
            r5.label = r4
            java.lang.Object r6 = r6.invoke(r2, r3, r5)
            if (r6 != r0) goto L52
            return r0
        L52:
            r2 = 2
            r5.label = r2
            java.lang.Object r6 = r1.emit(r6, r5)
            if (r6 != r0) goto L5c
            return r0
        L5c:
            kotlin.Unit r6 = kotlin.Unit.INSTANCE
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__ZipKt$combineLatest$1$1$invokeSuspend$$inlined$select$lambda$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
