package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.FlowPreview;
import kotlinx.coroutines.Job;
import org.jetbrains.annotations.NotNull;

/* compiled from: Context.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u001d\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0002¢\u0006\u0002\b\u0006\u001a0\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\t0\b\"\u0004\b\u0000\u0010\t*\b\u0012\u0004\u0012\u0002H\t0\b2\u0006\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001a[\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\u000b0\b\"\u0004\b\u0000\u0010\t\"\u0004\b\u0001\u0010\u000b*\b\u0012\u0004\u0012\u0002H\t0\b2\u0006\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052#\u0010\f\u001a\u001f\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\t0\b\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000b0\b0\r¢\u0006\u0002\b\u000eH\u0007¨\u0006\u000f"}, d2 = {"check", "", "flowContext", "Lkotlin/coroutines/CoroutineContext;", "bufferSize", "", "check$FlowKt__ContextKt", "flowOn", "Lkotlinx/coroutines/flow/Flow;", "T", "flowWith", "R", "builder", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "kotlinx-coroutines-core"}, k = 5, mv = {1, 1, 15}, xs = "kotlinx/coroutines/flow/FlowKt")
/* loaded from: classes2.dex */
public final /* synthetic */ class FlowKt__ContextKt {
    private static final void check$FlowKt__ContextKt(CoroutineContext coroutineContext, int i) {
        if (!(coroutineContext.get(Job.INSTANCE) == null)) {
            throw new IllegalArgumentException(("Flow context cannot contain job in it. Had " + coroutineContext).toString());
        }
        if (i >= 0) {
            return;
        }
        throw new IllegalArgumentException(("Buffer size should be positive, but was " + i).toString());
    }

    @FlowPreview
    @NotNull
    public static final <T> Flow<T> flowOn(@NotNull Flow<? extends T> flowOn, @NotNull CoroutineContext flowContext, int i) {
        Intrinsics.checkParameterIsNotNull(flowOn, "$this$flowOn");
        Intrinsics.checkParameterIsNotNull(flowContext, "flowContext");
        check$FlowKt__ContextKt(flowContext, i);
        return FlowKt.unsafeFlow(new FlowKt__ContextKt$flowOn$1(flowOn, flowContext, i, null));
    }

    @FlowPreview
    @NotNull
    public static /* synthetic */ Flow flowOn$default(Flow flow, CoroutineContext coroutineContext, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 16;
        }
        return FlowKt.flowOn(flow, coroutineContext, i);
    }

    @FlowPreview
    @NotNull
    public static final <T, R> Flow<R> flowWith(@NotNull Flow<? extends T> flowWith, @NotNull CoroutineContext flowContext, int i, @NotNull Function1<? super Flow<? extends T>, ? extends Flow<? extends R>> builder) {
        Intrinsics.checkParameterIsNotNull(flowWith, "$this$flowWith");
        Intrinsics.checkParameterIsNotNull(flowContext, "flowContext");
        Intrinsics.checkParameterIsNotNull(builder, "builder");
        check$FlowKt__ContextKt(flowContext, i);
        return FlowKt.unsafeFlow(new FlowKt__ContextKt$flowWith$1(flowWith, i, builder, flowContext, null));
    }

    @FlowPreview
    @NotNull
    public static /* synthetic */ Flow flowWith$default(Flow flow, CoroutineContext coroutineContext, int i, Function1 function1, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 16;
        }
        return FlowKt.flowWith(flow, coroutineContext, i, function1);
    }
}
