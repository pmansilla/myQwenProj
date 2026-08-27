package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.FlowPreview;
import kotlinx.coroutines.channels.BroadcastChannel;
import kotlinx.coroutines.channels.BroadcastKt;
import kotlinx.coroutines.channels.ProduceKt;
import kotlinx.coroutines.channels.ReceiveChannel;
import org.jetbrains.annotations.NotNull;

/* compiled from: ChannelFlow.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u001e\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0007\u001a:\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\nH\u0007\u001a0\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\f\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\bH\u0007¨\u0006\r"}, d2 = {"asFlow", "Lkotlinx/coroutines/flow/Flow;", "T", "Lkotlinx/coroutines/channels/BroadcastChannel;", "broadcastIn", "scope", "Lkotlinx/coroutines/CoroutineScope;", "capacity", "", "start", "Lkotlinx/coroutines/CoroutineStart;", "produceIn", "Lkotlinx/coroutines/channels/ReceiveChannel;", "kotlinx-coroutines-core"}, k = 5, mv = {1, 1, 15}, xs = "kotlinx/coroutines/flow/FlowKt")
/* loaded from: classes2.dex */
public final /* synthetic */ class FlowKt__ChannelFlowKt {
    @FlowPreview
    @NotNull
    public static final <T> Flow<T> asFlow(@NotNull BroadcastChannel<T> asFlow) {
        Intrinsics.checkParameterIsNotNull(asFlow, "$this$asFlow");
        return FlowKt.flow(new FlowKt__ChannelFlowKt$asFlow$1(asFlow, null));
    }

    @FlowPreview
    @NotNull
    public static final <T> BroadcastChannel<T> broadcastIn(@NotNull Flow<? extends T> broadcastIn, @NotNull CoroutineScope scope, int i, @NotNull CoroutineStart start) {
        Intrinsics.checkParameterIsNotNull(broadcastIn, "$this$broadcastIn");
        Intrinsics.checkParameterIsNotNull(scope, "scope");
        Intrinsics.checkParameterIsNotNull(start, "start");
        return BroadcastKt.broadcast$default(scope, null, i, start, null, new FlowKt__ChannelFlowKt$broadcastIn$1(broadcastIn, null), 9, null);
    }

    @FlowPreview
    @NotNull
    public static /* synthetic */ BroadcastChannel broadcastIn$default(Flow flow, CoroutineScope coroutineScope, int i, CoroutineStart coroutineStart, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 1;
        }
        if ((i2 & 4) != 0) {
            coroutineStart = CoroutineStart.LAZY;
        }
        return FlowKt.broadcastIn(flow, coroutineScope, i, coroutineStart);
    }

    @FlowPreview
    @NotNull
    public static final <T> ReceiveChannel<T> produceIn(@NotNull Flow<? extends T> produceIn, @NotNull CoroutineScope scope, int i) {
        Intrinsics.checkParameterIsNotNull(produceIn, "$this$produceIn");
        Intrinsics.checkParameterIsNotNull(scope, "scope");
        return ProduceKt.produce$default(scope, null, i, new FlowKt__ChannelFlowKt$produceIn$1(produceIn, null), 1, null);
    }

    @FlowPreview
    @NotNull
    public static /* synthetic */ ReceiveChannel produceIn$default(Flow flow, CoroutineScope coroutineScope, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 1;
        }
        return FlowKt.produceIn(flow, coroutineScope, i);
    }
}
