package kotlinx.coroutines.flow;

import com.sun.mail.imap.IMAPStore;
import kotlin.BuilderInference;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.FlowPreview;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Transform.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000<\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001aJ\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\"\u0010\u0003\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0004H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\b\u001a\u001f\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\n0\u0001\"\u0006\b\u0000\u0010\n\u0018\u0001*\u0006\u0012\u0002\b\u00030\u0001H\u0087\b\u001aJ\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\"\u0010\u0003\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0004H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\b\u001a$\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u0007*\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0001H\u0007\u001a_\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\n0\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n*\b\u0012\u0004\u0012\u0002H\u00020\u000121\u0010\u000e\u001a-\b\u0001\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0011\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\n0\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0004H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\b\u001ae\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\n0\u0001\"\u0004\b\u0000\u0010\u0002\"\b\b\u0001\u0010\n*\u00020\u0007*\b\u0012\u0004\u0012\u0002H\u00020\u000123\u0010\u000e\u001a/\b\u0001\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0011\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u0001H\n0\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0004H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\b\u001aJ\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\"\u0010\u0014\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0004H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\b\u001ar\u0010\u0016\u001a\b\u0012\u0004\u0012\u0002H\n0\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n*\b\u0012\u0004\u0012\u0002H\u00020\u00012D\b\u0001\u0010\u000e\u001a>\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\n0\u0018\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0011\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0017¢\u0006\u0002\b\u0019H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u001a\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u001b"}, d2 = {"filter", "Lkotlinx/coroutines/flow/Flow;", "T", "predicate", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "", "", "(Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/flow/Flow;", "filterIsInstance", "R", "filterNot", "filterNotNull", "map", "transformer", "Lkotlin/ParameterName;", IMAPStore.ID_NAME, "value", "mapNotNull", "onEach", "action", "", "transform", "Lkotlin/Function3;", "Lkotlinx/coroutines/flow/FlowCollector;", "Lkotlin/ExtensionFunctionType;", "(Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function3;)Lkotlinx/coroutines/flow/Flow;", "kotlinx-coroutines-core"}, k = 5, mv = {1, 1, 15}, xs = "kotlinx/coroutines/flow/FlowKt")
/* loaded from: classes2.dex */
public final /* synthetic */ class FlowKt__TransformKt {
    @FlowPreview
    @NotNull
    public static final <T> Flow<T> filter(@NotNull Flow<? extends T> filter, @NotNull Function2<? super T, ? super Continuation<? super Boolean>, ? extends Object> predicate) {
        Intrinsics.checkParameterIsNotNull(filter, "$this$filter");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        return FlowKt.unsafeFlow(new FlowKt__TransformKt$filter$1(filter, predicate, null));
    }

    @FlowPreview
    private static final <R> Flow<R> filterIsInstance(@NotNull Flow<?> flow) {
        Intrinsics.needClassReification();
        Flow<R> filter = FlowKt.filter(flow, new FlowKt__TransformKt$filterIsInstance$1(null));
        if (filter != null) {
            return filter;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.flow.Flow<R>");
    }

    @FlowPreview
    @NotNull
    public static final <T> Flow<T> filterNot(@NotNull Flow<? extends T> filterNot, @NotNull Function2<? super T, ? super Continuation<? super Boolean>, ? extends Object> predicate) {
        Intrinsics.checkParameterIsNotNull(filterNot, "$this$filterNot");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        return FlowKt.unsafeFlow(new FlowKt__TransformKt$filterNot$1(filterNot, predicate, null));
    }

    @FlowPreview
    @NotNull
    public static final <T> Flow<T> filterNotNull(@NotNull Flow<? extends T> filterNotNull) {
        Intrinsics.checkParameterIsNotNull(filterNotNull, "$this$filterNotNull");
        return FlowKt.unsafeFlow(new FlowKt__TransformKt$filterNotNull$1(filterNotNull, null));
    }

    @FlowPreview
    @NotNull
    public static final <T, R> Flow<R> map(@NotNull Flow<? extends T> map, @NotNull Function2<? super T, ? super Continuation<? super R>, ? extends Object> transformer) {
        Intrinsics.checkParameterIsNotNull(map, "$this$map");
        Intrinsics.checkParameterIsNotNull(transformer, "transformer");
        return FlowKt.transform(map, new FlowKt__TransformKt$map$1(transformer, null));
    }

    @FlowPreview
    @NotNull
    public static final <T, R> Flow<R> mapNotNull(@NotNull Flow<? extends T> mapNotNull, @NotNull Function2<? super T, ? super Continuation<? super R>, ? extends Object> transformer) {
        Intrinsics.checkParameterIsNotNull(mapNotNull, "$this$mapNotNull");
        Intrinsics.checkParameterIsNotNull(transformer, "transformer");
        return FlowKt.transform(mapNotNull, new FlowKt__TransformKt$mapNotNull$1(transformer, null));
    }

    @FlowPreview
    @NotNull
    public static final <T> Flow<T> onEach(@NotNull Flow<? extends T> onEach, @NotNull Function2<? super T, ? super Continuation<? super Unit>, ? extends Object> action) {
        Intrinsics.checkParameterIsNotNull(onEach, "$this$onEach");
        Intrinsics.checkParameterIsNotNull(action, "action");
        return FlowKt.unsafeFlow(new FlowKt__TransformKt$onEach$1(onEach, action, null));
    }

    @FlowPreview
    @NotNull
    public static final <T, R> Flow<R> transform(@NotNull Flow<? extends T> transform, @BuilderInference @NotNull Function3<? super FlowCollector<? super R>, ? super T, ? super Continuation<? super Unit>, ? extends Object> transformer) {
        Intrinsics.checkParameterIsNotNull(transform, "$this$transform");
        Intrinsics.checkParameterIsNotNull(transformer, "transformer");
        return FlowKt.unsafeFlow(new FlowKt__TransformKt$transform$1(transform, transformer, null));
    }
}
