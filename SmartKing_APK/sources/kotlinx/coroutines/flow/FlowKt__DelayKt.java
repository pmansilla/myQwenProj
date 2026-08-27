package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.FlowPreview;
import kotlinx.coroutines.channels.ProduceKt;
import kotlinx.coroutines.channels.ReceiveChannel;
import org.jetbrains.annotations.NotNull;

/* compiled from: Delay.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u001a$\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004\u001a&\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u0004H\u0007\u001a&\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u0004H\u0007\u001a$\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t*\u00020\u000b2\u0006\u0010\f\u001a\u00020\u00042\b\b\u0002\u0010\r\u001a\u00020\u0004H\u0000\u001a$\u0010\u000e\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u0004¨\u0006\u0010"}, d2 = {"debounce", "Lkotlinx/coroutines/flow/Flow;", "T", "timeoutMillis", "", "delayEach", "timeMillis", "delayFlow", "fixedPeriodTicker", "Lkotlinx/coroutines/channels/ReceiveChannel;", "", "Lkotlinx/coroutines/CoroutineScope;", "delayMillis", "initialDelayMillis", "sample", "periodMillis", "kotlinx-coroutines-core"}, k = 5, mv = {1, 1, 15}, xs = "kotlinx/coroutines/flow/FlowKt")
/* loaded from: classes2.dex */
public final /* synthetic */ class FlowKt__DelayKt {
    @NotNull
    public static final <T> Flow<T> debounce(@NotNull Flow<? extends T> debounce, long j) {
        Intrinsics.checkParameterIsNotNull(debounce, "$this$debounce");
        if (j > 0) {
            return FlowKt.unsafeFlow(new FlowKt__DelayKt$debounce$2(debounce, j, null));
        }
        throw new IllegalArgumentException("Debounce timeout should be positive".toString());
    }

    @FlowPreview
    @NotNull
    public static final <T> Flow<T> delayEach(@NotNull Flow<? extends T> delayEach, long j) {
        Intrinsics.checkParameterIsNotNull(delayEach, "$this$delayEach");
        return FlowKt.unsafeFlow(new FlowKt__DelayKt$delayEach$1(delayEach, j, null));
    }

    @FlowPreview
    @NotNull
    public static final <T> Flow<T> delayFlow(@NotNull Flow<? extends T> delayFlow, long j) {
        Intrinsics.checkParameterIsNotNull(delayFlow, "$this$delayFlow");
        return FlowKt.unsafeFlow(new FlowKt__DelayKt$delayFlow$1(delayFlow, j, null));
    }

    @NotNull
    public static final ReceiveChannel<Unit> fixedPeriodTicker(@NotNull CoroutineScope fixedPeriodTicker, long j, long j2) {
        Intrinsics.checkParameterIsNotNull(fixedPeriodTicker, "$this$fixedPeriodTicker");
        if (!(j >= 0)) {
            throw new IllegalArgumentException(("Expected non-negative delay, but has " + j + " ms").toString());
        }
        if (j2 >= 0) {
            return ProduceKt.produce$default(fixedPeriodTicker, null, 0, new FlowKt__DelayKt$fixedPeriodTicker$3(j2, j, null), 1, null);
        }
        throw new IllegalArgumentException(("Expected non-negative initial delay, but has " + j2 + " ms").toString());
    }

    @NotNull
    public static /* synthetic */ ReceiveChannel fixedPeriodTicker$default(CoroutineScope coroutineScope, long j, long j2, int i, Object obj) {
        if ((i & 2) != 0) {
            j2 = j;
        }
        return FlowKt.fixedPeriodTicker(coroutineScope, j, j2);
    }

    @NotNull
    public static final <T> Flow<T> sample(@NotNull Flow<? extends T> sample, long j) {
        Intrinsics.checkParameterIsNotNull(sample, "$this$sample");
        if (j > 0) {
            return FlowKt.unsafeFlow(new FlowKt__DelayKt$sample$2(sample, j, null));
        }
        throw new IllegalArgumentException("Sample period should be positive".toString());
    }
}
