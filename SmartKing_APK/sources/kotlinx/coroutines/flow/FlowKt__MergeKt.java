package kotlinx.coroutines.flow;

import com.sun.mail.imap.IMAPStore;
import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.FlowPreview;
import org.jetbrains.annotations.NotNull;

/* compiled from: Merge.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000>\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\u001ae\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0003\"\u0004\b\u0001\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00030\u000127\u0010\u0004\u001a3\b\u0001\u0012\u0013\u0012\u0011H\u0003¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\b\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00010\t\u0012\u0006\u0012\u0004\u0018\u00010\n0\u0005H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u000b\u001ay\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0003\"\u0004\b\u0001\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00030\u00012\b\b\u0002\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u000e27\u0010\u0004\u001a3\b\u0001\u0012\u0013\u0012\u0011H\u0003¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\b\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00010\t\u0012\u0006\u0012\u0004\u0018\u00010\n0\u0005H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0010\u001a$\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0003*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u00010\u0001H\u0007\u001a8\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0003*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u00010\u00012\b\b\u0002\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u000eH\u0007\u001a\u0012\u0010\u0013\u001a\u00020\u0014*\u00020\u0015H\u0082\b¢\u0006\u0002\b\u0016\u001ae\u0010\u0017\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0003\"\u0004\b\u0001\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00030\u000127\u0010\u0004\u001a3\b\u0001\u0012\u0013\u0012\u0011H\u0003¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\b\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00010\t\u0012\u0006\u0012\u0004\u0018\u00010\n0\u0005H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u000b\u001a\u0012\u0010\u0018\u001a\u00020\u0019*\u00020\u0015H\u0082\b¢\u0006\u0002\b\u001a\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u001b"}, d2 = {"flatMapConcat", "Lkotlinx/coroutines/flow/Flow;", "R", "T", "transform", "Lkotlin/Function2;", "Lkotlin/ParameterName;", IMAPStore.ID_NAME, "value", "Lkotlin/coroutines/Continuation;", "", "(Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/flow/Flow;", "flatMapMerge", "concurrency", "", "bufferSize", "(Lkotlinx/coroutines/flow/Flow;IILkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/flow/Flow;", "flattenConcat", "flattenMerge", "release", "", "Lkotlinx/atomicfu/AtomicBoolean;", "release$FlowKt__MergeKt", "switchMap", "tryAcquire", "", "tryAcquire$FlowKt__MergeKt", "kotlinx-coroutines-core"}, k = 5, mv = {1, 1, 15}, xs = "kotlinx/coroutines/flow/FlowKt")
/* loaded from: classes2.dex */
public final /* synthetic */ class FlowKt__MergeKt {
    @FlowPreview
    @NotNull
    public static final <T, R> Flow<R> flatMapConcat(@NotNull Flow<? extends T> flatMapConcat, @NotNull Function2<? super T, ? super Continuation<? super Flow<? extends R>>, ? extends Object> transform) {
        Intrinsics.checkParameterIsNotNull(flatMapConcat, "$this$flatMapConcat");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        return FlowKt.unsafeFlow(new FlowKt__MergeKt$flatMapConcat$1(flatMapConcat, transform, null));
    }

    @FlowPreview
    @NotNull
    public static final <T, R> Flow<R> flatMapMerge(@NotNull Flow<? extends T> flatMapMerge, int i, int i2, @NotNull Function2<? super T, ? super Continuation<? super Flow<? extends R>>, ? extends Object> transform) {
        Intrinsics.checkParameterIsNotNull(flatMapMerge, "$this$flatMapMerge");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        if (!(i2 >= 0)) {
            throw new IllegalArgumentException(("Expected non-negative buffer size, but had " + i2).toString());
        }
        if (i >= 0) {
            return FlowKt.unsafeFlow(new FlowKt__MergeKt$flatMapMerge$3(flatMapMerge, i, i2, transform, null));
        }
        throw new IllegalArgumentException(("Expected non-negative concurrency level, but had " + i).toString());
    }

    @FlowPreview
    @NotNull
    public static /* synthetic */ Flow flatMapMerge$default(Flow flow, int i, int i2, Function2 function2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 16;
        }
        if ((i3 & 2) != 0) {
            i2 = 16;
        }
        return FlowKt.flatMapMerge(flow, i, i2, function2);
    }

    @FlowPreview
    @NotNull
    public static final <T> Flow<T> flattenConcat(@NotNull Flow<? extends Flow<? extends T>> flattenConcat) {
        Intrinsics.checkParameterIsNotNull(flattenConcat, "$this$flattenConcat");
        return FlowKt.unsafeFlow(new FlowKt__MergeKt$flattenConcat$1(flattenConcat, null));
    }

    @FlowPreview
    @NotNull
    public static final <T> Flow<T> flattenMerge(@NotNull Flow<? extends Flow<? extends T>> flattenMerge, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(flattenMerge, "$this$flattenMerge");
        return FlowKt.flatMapMerge(flattenMerge, i, i2, new FlowKt__MergeKt$flattenMerge$1(null));
    }

    @FlowPreview
    @NotNull
    public static /* synthetic */ Flow flattenMerge$default(Flow flow, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 16;
        }
        if ((i3 & 2) != 0) {
            i2 = 16;
        }
        return FlowKt.flattenMerge(flow, i, i2);
    }

    @FlowPreview
    @NotNull
    public static final <T, R> Flow<R> switchMap(@NotNull Flow<? extends T> switchMap, @NotNull Function2<? super T, ? super Continuation<? super Flow<? extends R>>, ? extends Object> transform) {
        Intrinsics.checkParameterIsNotNull(switchMap, "$this$switchMap");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        return FlowKt.unsafeFlow(new FlowKt__MergeKt$switchMap$1(switchMap, transform, null));
    }
}
