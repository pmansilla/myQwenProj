package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.FlowPreview;
import org.jetbrains.annotations.NotNull;

/* compiled from: Errors.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000B\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\u0002\u001a]\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\t0\b\"\u0004\b\u0000\u0010\t*\b\u0012\u0004\u0012\u0002H\t0\b23\u0010\n\u001a/\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\t0\f\u0012\u0004\u0012\u00020\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\r\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\u000b¢\u0006\u0002\b\u0010H\u0002ø\u0001\u0000¢\u0006\u0004\b\u0011\u0010\u0012\u001aF\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\t0\b\"\u0004\b\u0000\u0010\t*\b\u0012\u0004\u0012\u0002H\t0\b2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\t0\b2\u0018\b\u0002\u0010\u0015\u001a\u0012\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001j\u0002`\u0004H\u0007\u001aE\u0010\u0016\u001a\b\u0012\u0004\u0012\u0002H\t0\b\"\u0004\b\u0000\u0010\t*\b\u0012\u0004\u0012\u0002H\t0\b2\u0006\u0010\u0014\u001a\u0002H\t2\u0018\b\u0002\u0010\u0015\u001a\u0012\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001j\u0002`\u0004H\u0007¢\u0006\u0002\u0010\u0017\u001aB\u0010\u0018\u001a\b\u0012\u0004\u0012\u0002H\t0\b\"\u0004\b\u0000\u0010\t*\b\u0012\u0004\u0012\u0002H\t0\b2\b\b\u0002\u0010\u0019\u001a\u00020\u001a2\u0018\b\u0002\u0010\u0015\u001a\u0012\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001j\u0002`\u0004H\u0007\"$\u0010\u0000\u001a\u0012\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001j\u0002`\u0004X\u0082\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006*\"\u0010\u001b\"\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u00012\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u001c"}, d2 = {"ALWAYS_TRUE", "Lkotlin/Function1;", "", "", "Lkotlinx/coroutines/flow/ExceptionPredicate;", "getALWAYS_TRUE$FlowKt__ErrorsKt", "()Lkotlin/jvm/functions/Function1;", "collectSafely", "Lkotlinx/coroutines/flow/Flow;", "T", "onException", "Lkotlin/Function3;", "Lkotlinx/coroutines/flow/FlowCollector;", "Lkotlin/coroutines/Continuation;", "", "", "Lkotlin/ExtensionFunctionType;", "collectSafely$FlowKt__ErrorsKt", "(Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function3;)Lkotlinx/coroutines/flow/Flow;", "onErrorCollect", "fallback", "predicate", "onErrorReturn", "(Lkotlinx/coroutines/flow/Flow;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)Lkotlinx/coroutines/flow/Flow;", "retry", "retries", "", "ExceptionPredicate", "kotlinx-coroutines-core"}, k = 5, mv = {1, 1, 15}, xs = "kotlinx/coroutines/flow/FlowKt")
/* loaded from: classes2.dex */
public final /* synthetic */ class FlowKt__ErrorsKt {
    private static final Function1<Throwable, Boolean> ALWAYS_TRUE = new Function1<Throwable, Boolean>() { // from class: kotlinx.coroutines.flow.FlowKt__ErrorsKt$ALWAYS_TRUE$1
        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ Boolean invoke(Throwable th) {
            return Boolean.valueOf(invoke2(th));
        }

        /* renamed from: invoke, reason: avoid collision after fix types in other method */
        public final boolean invoke2(@NotNull Throwable it) {
            Intrinsics.checkParameterIsNotNull(it, "it");
            return true;
        }
    };

    private static final <T> Flow<T> collectSafely$FlowKt__ErrorsKt(@NotNull Flow<? extends T> flow, Function3<? super FlowCollector<? super T>, ? super Throwable, ? super Continuation<? super Unit>, ? extends Object> function3) {
        return FlowKt.unsafeFlow(new FlowKt__ErrorsKt$collectSafely$1(flow, function3, null));
    }

    @FlowPreview
    @NotNull
    public static final <T> Flow<T> onErrorCollect(@NotNull Flow<? extends T> onErrorCollect, @NotNull Flow<? extends T> fallback, @NotNull Function1<? super Throwable, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull(onErrorCollect, "$this$onErrorCollect");
        Intrinsics.checkParameterIsNotNull(fallback, "fallback");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        return collectSafely$FlowKt__ErrorsKt(onErrorCollect, new FlowKt__ErrorsKt$onErrorCollect$1(predicate, fallback, null));
    }

    @FlowPreview
    @NotNull
    public static /* synthetic */ Flow onErrorCollect$default(Flow flow, Flow flow2, Function1 function1, int i, Object obj) {
        if ((i & 2) != 0) {
            function1 = ALWAYS_TRUE;
        }
        return FlowKt.onErrorCollect(flow, flow2, function1);
    }

    @FlowPreview
    @NotNull
    public static final <T> Flow<T> onErrorReturn(@NotNull Flow<? extends T> onErrorReturn, T t, @NotNull Function1<? super Throwable, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull(onErrorReturn, "$this$onErrorReturn");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        return collectSafely$FlowKt__ErrorsKt(onErrorReturn, new FlowKt__ErrorsKt$onErrorReturn$1(predicate, t, null));
    }

    @FlowPreview
    @NotNull
    public static /* synthetic */ Flow onErrorReturn$default(Flow flow, Object obj, Function1 function1, int i, Object obj2) {
        if ((i & 2) != 0) {
            function1 = ALWAYS_TRUE;
        }
        return FlowKt.onErrorReturn(flow, obj, function1);
    }

    @FlowPreview
    @NotNull
    public static final <T> Flow<T> retry(@NotNull Flow<? extends T> retry, int i, @NotNull Function1<? super Throwable, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull(retry, "$this$retry");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        if (i > 0) {
            return FlowKt.unsafeFlow(new FlowKt__ErrorsKt$retry$2(retry, i, predicate, null));
        }
        throw new IllegalArgumentException(("Expected positive amount of retries, but had " + i).toString());
    }

    @FlowPreview
    @NotNull
    public static /* synthetic */ Flow retry$default(Flow flow, int i, Function1 function1, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = Integer.MAX_VALUE;
        }
        if ((i2 & 2) != 0) {
            function1 = ALWAYS_TRUE;
        }
        return FlowKt.retry(flow, i, function1);
    }
}
