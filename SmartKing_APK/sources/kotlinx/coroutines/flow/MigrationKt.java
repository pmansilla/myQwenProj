package kotlinx.coroutines.flow;

import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: Migration.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000D\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\b\u0010\u0000\u001a\u00020\u0001H\u0007\u001a\b\u0010\u0002\u001a\u00020\u0001H\u0007\u001a\b\u0010\u0003\u001a\u00020\u0001H\u0007\u001a>\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00060\u0005\"\u0004\b\u0000\u0010\u0007\"\u0004\b\u0001\u0010\u0006*\b\u0012\u0004\u0012\u0002H\u00070\u00052\u0018\u0010\b\u001a\u0014\u0012\u0004\u0012\u0002H\u0007\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00060\u00050\tH\u0007\u001aV\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\u00060\u0005\"\u0004\b\u0000\u0010\u0007\"\u0004\b\u0001\u0010\u0006*\b\u0012\u0004\u0012\u0002H\u00070\u00052(\u0010\b\u001a$\b\u0001\u0012\u0004\u0012\u0002H\u0007\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00060\u00050\f\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u000bH\u0007ø\u0001\u0000¢\u0006\u0002\u0010\r\u001a$\u0010\u000e\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0005\"\u0004\b\u0000\u0010\u0007*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00070\u00050\u0005H\u0007\u001a$\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0005\"\u0004\b\u0000\u0010\u0007*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00070\u00050\u0005H\u0007\u001a&\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0005\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u00052\u0006\u0010\u0011\u001a\u00020\u0012H\u0007\u001a,\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0005\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u00052\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0005H\u0007\u001a&\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0005\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u00052\u0006\u0010\u0011\u001a\u00020\u0012H\u0007\u001a\u0018\u0010\u0016\u001a\u00020\u0017\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u0005H\u0007\u001a,\u0010\u0016\u001a\u00020\u0017\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u00052\u0012\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u0002H\u0007\u0012\u0004\u0012\u00020\u00170\tH\u0007\u001a@\u0010\u0016\u001a\u00020\u0017\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u00052\u0012\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u0002H\u0007\u0012\u0004\u0012\u00020\u00170\t2\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\u00170\tH\u0007\u001a&\u0010\u001b\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0005\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u00052\u0006\u0010\u0011\u001a\u00020\u0012H\u0007\u001aL\u0010\u001c\u001a\u00020\u0017\"\u0004\b\u0000\u0010\u0007\"\u0004\b\u0001\u0010\u0006*\b\u0012\u0004\u0012\u0002H\u00070\u001d2\u0006\u0010\u0011\u001a\u00020\u00122\u001c\u0010\u001e\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00060\f\u0012\u0006\u0012\u0004\u0018\u00010\u00010\tH\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u001f\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006 "}, d2 = {"BehaviourSubject", "", "PublishSubject", "ReplaySubject", "concatMap", "Lkotlinx/coroutines/flow/Flow;", "R", "T", "mapper", "Lkotlin/Function1;", "flatMap", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "(Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/flow/Flow;", "flatten", "merge", "observeOn", "context", "Lkotlin/coroutines/CoroutineContext;", "onErrorResume", "fallback", "publishOn", "subscribe", "", "onEach", "onError", "", "subscribeOn", "withContext", "Lkotlinx/coroutines/flow/FlowCollector;", "block", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/functions/Function1;)V", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 15})
/* loaded from: classes.dex */
public final class MigrationKt {
    @Deprecated(level = DeprecationLevel.ERROR, message = "Use BroadcastChannel.asFlow()")
    @NotNull
    public static final Object BehaviourSubject() {
        throw new IllegalStateException("Should not be called".toString());
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "PublishSubject is not supported")
    @NotNull
    public static final Object PublishSubject() {
        throw new IllegalStateException("Should not be called".toString());
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "ReplaySubject is not supported. The closest analogue is buffered broadcast channel")
    @NotNull
    public static final Object ReplaySubject() {
        throw new IllegalStateException("Should not be called".toString());
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Flow analogue is named flatMapConcat", replaceWith = @ReplaceWith(expression = "flatMapConcat(mapper)", imports = {}))
    @NotNull
    public static final <T, R> Flow<R> concatMap(@NotNull Flow<? extends T> concatMap, @NotNull Function1<? super T, ? extends Flow<? extends R>> mapper) {
        Intrinsics.checkParameterIsNotNull(concatMap, "$this$concatMap");
        Intrinsics.checkParameterIsNotNull(mapper, "mapper");
        throw new IllegalStateException("Should not be called".toString());
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Flow analogue is named flatMapConcat", replaceWith = @ReplaceWith(expression = "flatMapConcat(mapper)", imports = {}))
    @NotNull
    public static final <T, R> Flow<R> flatMap(@NotNull Flow<? extends T> flatMap, @NotNull Function2<? super T, ? super Continuation<? super Flow<? extends R>>, ? extends Object> mapper) {
        Intrinsics.checkParameterIsNotNull(flatMap, "$this$flatMap");
        Intrinsics.checkParameterIsNotNull(mapper, "mapper");
        throw new IllegalStateException("Should not be called".toString());
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Flow analogue is named flattenConcat", replaceWith = @ReplaceWith(expression = "flattenConcat()", imports = {}))
    @NotNull
    public static final <T> Flow<T> flatten(@NotNull Flow<? extends Flow<? extends T>> flatten) {
        Intrinsics.checkParameterIsNotNull(flatten, "$this$flatten");
        throw new IllegalStateException("Should not be called".toString());
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Flow analogue is named flattenConcat", replaceWith = @ReplaceWith(expression = "flattenConcat()", imports = {}))
    @NotNull
    public static final <T> Flow<T> merge(@NotNull Flow<? extends Flow<? extends T>> merge) {
        Intrinsics.checkParameterIsNotNull(merge, "$this$merge");
        throw new IllegalStateException("Should not be called".toString());
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Collect flow in the desired context instead")
    @NotNull
    public static final <T> Flow<T> observeOn(@NotNull Flow<? extends T> observeOn, @NotNull CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(observeOn, "$this$observeOn");
        Intrinsics.checkParameterIsNotNull(context, "context");
        throw new IllegalStateException("Should not be called".toString());
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Flow analogue is named onErrorCollect", replaceWith = @ReplaceWith(expression = "onErrorCollect(fallback)", imports = {}))
    @NotNull
    public static final <T> Flow<T> onErrorResume(@NotNull Flow<? extends T> onErrorResume, @NotNull Flow<? extends T> fallback) {
        Intrinsics.checkParameterIsNotNull(onErrorResume, "$this$onErrorResume");
        Intrinsics.checkParameterIsNotNull(fallback, "fallback");
        throw new IllegalStateException("Should not be called".toString());
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Collect flow in the desired context instead")
    @NotNull
    public static final <T> Flow<T> publishOn(@NotNull Flow<? extends T> publishOn, @NotNull CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(publishOn, "$this$publishOn");
        Intrinsics.checkParameterIsNotNull(context, "context");
        throw new IllegalStateException("Should not be called".toString());
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Use launch + collect instead")
    public static final <T> void subscribe(@NotNull Flow<? extends T> subscribe) {
        Intrinsics.checkParameterIsNotNull(subscribe, "$this$subscribe");
        throw new IllegalStateException("Should not be called".toString());
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Use launch + collect instead")
    public static final <T> void subscribe(@NotNull Flow<? extends T> subscribe, @NotNull Function1<? super T, Unit> onEach) {
        Intrinsics.checkParameterIsNotNull(subscribe, "$this$subscribe");
        Intrinsics.checkParameterIsNotNull(onEach, "onEach");
        throw new IllegalStateException("Should not be called".toString());
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Use launch + collect instead")
    public static final <T> void subscribe(@NotNull Flow<? extends T> subscribe, @NotNull Function1<? super T, Unit> onEach, @NotNull Function1<? super Throwable, Unit> onError) {
        Intrinsics.checkParameterIsNotNull(subscribe, "$this$subscribe");
        Intrinsics.checkParameterIsNotNull(onEach, "onEach");
        Intrinsics.checkParameterIsNotNull(onError, "onError");
        throw new IllegalStateException("Should not be called".toString());
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Use flowWith or flowOn instead")
    @NotNull
    public static final <T> Flow<T> subscribeOn(@NotNull Flow<? extends T> subscribeOn, @NotNull CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(subscribeOn, "$this$subscribeOn");
        Intrinsics.checkParameterIsNotNull(context, "context");
        throw new IllegalStateException("Should not be called".toString());
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "withContext in flow body is deprecated, use flowOn instead")
    public static final <T, R> void withContext(@NotNull FlowCollector<? super T> withContext, @NotNull CoroutineContext context, @NotNull Function1<? super Continuation<? super R>, ? extends Object> block) {
        Intrinsics.checkParameterIsNotNull(withContext, "$this$withContext");
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(block, "block");
        throw new IllegalStateException("Should not be called".toString());
    }
}
