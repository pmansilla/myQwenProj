package kotlinx.coroutines.flow;

import com.sun.mail.imap.IMAPStore;
import java.util.Iterator;
import kotlin.BuilderInference;
import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.ranges.LongRange;
import kotlin.sequences.Sequence;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.FlowPreview;
import kotlinx.coroutines.channels.SendChannel;
import kotlinx.coroutines.flow.internal.SafeCollector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Builders.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000n\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u0015\n\u0002\u0010\t\n\u0002\u0010\u0016\n\u0002\u0010\u001c\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0014\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002H\u0007\u001aM\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022/\b\u0001\u0010\u0004\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0006\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0005¢\u0006\u0002\b\nH\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u000b\u001a-\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0012\u0010\r\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u000e\"\u0002H\u0002H\u0007¢\u0006\u0002\u0010\u000f\u001aT\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\b\b\u0002\u0010\u0011\u001a\u00020\u001224\b\u0001\u0010\u0004\u001a.\u0012\u0004\u0012\u00020\u0013\u0012\u0019\u0012\u0017\u0012\u0004\u0012\u0002H\u00020\u0014¢\u0006\f\b\u0015\u0012\b\b\u0016\u0012\u0004\b\b(\u0017\u0012\u0004\u0012\u00020\b0\u0005¢\u0006\u0002\b\nH\u0007\u001aM\u0010\u0018\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022/\b\u0001\u0010\u0004\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0006\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0005¢\u0006\u0002\b\nH\u0001ø\u0001\u0000¢\u0006\u0002\u0010\u000b\u001a\u001e\u0010\u0019\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u001aH\u0007\u001a#\u0010\u0019\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000eH\u0007¢\u0006\u0002\u0010\u000f\u001a\u0012\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00120\u0001*\u00020\u001bH\u0007\u001a\u0012\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001c0\u0001*\u00020\u001dH\u0007\u001a\u001e\u0010\u0019\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u001eH\u0007\u001a\u001e\u0010\u0019\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u001fH\u0007\u001a\u0012\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00120\u0001*\u00020 H\u0007\u001a\u0012\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001c0\u0001*\u00020!H\u0007\u001a\u001e\u0010\u0019\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\"H\u0007\u001a6\u0010\u0019\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0007\u0012\u0006\u0012\u0004\u0018\u00010\t0#H\u0007ø\u0001\u0000¢\u0006\u0002\u0010$\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006%"}, d2 = {"emptyFlow", "Lkotlinx/coroutines/flow/Flow;", "T", "flow", "block", "Lkotlin/Function2;", "Lkotlinx/coroutines/flow/FlowCollector;", "Lkotlin/coroutines/Continuation;", "", "", "Lkotlin/ExtensionFunctionType;", "(Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/flow/Flow;", "flowOf", "elements", "", "([Ljava/lang/Object;)Lkotlinx/coroutines/flow/Flow;", "flowViaChannel", "bufferSize", "", "Lkotlinx/coroutines/CoroutineScope;", "Lkotlinx/coroutines/channels/SendChannel;", "Lkotlin/ParameterName;", IMAPStore.ID_NAME, "channel", "unsafeFlow", "asFlow", "Lkotlin/Function0;", "", "", "", "", "", "Lkotlin/ranges/IntRange;", "Lkotlin/ranges/LongRange;", "Lkotlin/sequences/Sequence;", "Lkotlin/Function1;", "(Lkotlin/jvm/functions/Function1;)Lkotlinx/coroutines/flow/Flow;", "kotlinx-coroutines-core"}, k = 5, mv = {1, 1, 15}, xs = "kotlinx/coroutines/flow/FlowKt")
/* loaded from: classes2.dex */
public final /* synthetic */ class FlowKt__BuildersKt {
    @FlowPreview
    @NotNull
    public static final <T> Flow<T> asFlow(@NotNull Iterable<? extends T> asFlow) {
        Intrinsics.checkParameterIsNotNull(asFlow, "$this$asFlow");
        return FlowKt.unsafeFlow(new FlowKt__BuildersKt$asFlow$3(asFlow, null));
    }

    @FlowPreview
    @NotNull
    public static final <T> Flow<T> asFlow(@NotNull Iterator<? extends T> asFlow) {
        Intrinsics.checkParameterIsNotNull(asFlow, "$this$asFlow");
        return FlowKt.unsafeFlow(new FlowKt__BuildersKt$asFlow$4(asFlow, null));
    }

    @FlowPreview
    @NotNull
    public static final <T> Flow<T> asFlow(@NotNull Function0<? extends T> asFlow) {
        Intrinsics.checkParameterIsNotNull(asFlow, "$this$asFlow");
        return FlowKt.unsafeFlow(new FlowKt__BuildersKt$asFlow$1(asFlow, null));
    }

    @FlowPreview
    @NotNull
    public static final <T> Flow<T> asFlow(@NotNull Function1<? super Continuation<? super T>, ? extends Object> asFlow) {
        Intrinsics.checkParameterIsNotNull(asFlow, "$this$asFlow");
        return FlowKt.unsafeFlow(new FlowKt__BuildersKt$asFlow$2(asFlow, null));
    }

    @FlowPreview
    @NotNull
    public static final Flow<Integer> asFlow(@NotNull IntRange asFlow) {
        Intrinsics.checkParameterIsNotNull(asFlow, "$this$asFlow");
        return FlowKt.flow(new FlowKt__BuildersKt$asFlow$9(asFlow, null));
    }

    @FlowPreview
    @NotNull
    public static final Flow<Long> asFlow(@NotNull LongRange asFlow) {
        Intrinsics.checkParameterIsNotNull(asFlow, "$this$asFlow");
        return FlowKt.flow(new FlowKt__BuildersKt$asFlow$10(asFlow, null));
    }

    @FlowPreview
    @NotNull
    public static final <T> Flow<T> asFlow(@NotNull Sequence<? extends T> asFlow) {
        Intrinsics.checkParameterIsNotNull(asFlow, "$this$asFlow");
        return FlowKt.unsafeFlow(new FlowKt__BuildersKt$asFlow$5(asFlow, null));
    }

    @FlowPreview
    @NotNull
    public static final Flow<Integer> asFlow(@NotNull int[] asFlow) {
        Intrinsics.checkParameterIsNotNull(asFlow, "$this$asFlow");
        return FlowKt.flow(new FlowKt__BuildersKt$asFlow$7(asFlow, null));
    }

    @FlowPreview
    @NotNull
    public static final Flow<Long> asFlow(@NotNull long[] asFlow) {
        Intrinsics.checkParameterIsNotNull(asFlow, "$this$asFlow");
        return FlowKt.flow(new FlowKt__BuildersKt$asFlow$8(asFlow, null));
    }

    @FlowPreview
    @NotNull
    public static final <T> Flow<T> asFlow(@NotNull T[] asFlow) {
        Intrinsics.checkParameterIsNotNull(asFlow, "$this$asFlow");
        return FlowKt.flow(new FlowKt__BuildersKt$asFlow$6(asFlow, null));
    }

    @FlowPreview
    @NotNull
    public static final <T> Flow<T> emptyFlow() {
        return EmptyFlow.INSTANCE;
    }

    @FlowPreview
    @NotNull
    public static final <T> Flow<T> flow(@BuilderInference @NotNull final Function2<? super FlowCollector<? super T>, ? super Continuation<? super Unit>, ? extends Object> block) {
        Intrinsics.checkParameterIsNotNull(block, "block");
        return new Flow<T>() { // from class: kotlinx.coroutines.flow.FlowKt__BuildersKt$flow$1
            @Override // kotlinx.coroutines.flow.Flow
            @Nullable
            public Object collect(@NotNull FlowCollector<? super T> flowCollector, @NotNull Continuation<? super Unit> continuation) {
                return Function2.this.invoke(new SafeCollector(flowCollector, continuation.getContext()), continuation);
            }
        };
    }

    @FlowPreview
    @NotNull
    public static final <T> Flow<T> flowOf(@NotNull T... elements) {
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        return FlowKt.unsafeFlow(new FlowKt__BuildersKt$flowOf$1(elements, null));
    }

    @FlowPreview
    @NotNull
    public static final <T> Flow<T> flowViaChannel(int i, @BuilderInference @NotNull Function2<? super CoroutineScope, ? super SendChannel<? super T>, Unit> block) {
        Intrinsics.checkParameterIsNotNull(block, "block");
        return FlowKt.flow(new FlowKt__BuildersKt$flowViaChannel$1(i, block, null));
    }

    @FlowPreview
    @NotNull
    public static /* synthetic */ Flow flowViaChannel$default(int i, Function2 function2, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = 16;
        }
        return FlowKt.flowViaChannel(i, function2);
    }

    @FlowPreview
    @PublishedApi
    @NotNull
    public static final <T> Flow<T> unsafeFlow(@BuilderInference @NotNull final Function2<? super FlowCollector<? super T>, ? super Continuation<? super Unit>, ? extends Object> block) {
        Intrinsics.checkParameterIsNotNull(block, "block");
        return new Flow<T>() { // from class: kotlinx.coroutines.flow.FlowKt__BuildersKt$unsafeFlow$1
            @Override // kotlinx.coroutines.flow.Flow
            @Nullable
            public Object collect(@NotNull FlowCollector<? super T> flowCollector, @NotNull Continuation<? super Unit> continuation) {
                return Function2.this.invoke(flowCollector, continuation);
            }
        };
    }
}
