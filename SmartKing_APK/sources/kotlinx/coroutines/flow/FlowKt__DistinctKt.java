package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.FlowPreview;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Distinct.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\u001a\u001e\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001H\u0007\u001a8\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00040\u0006H\u0007¨\u0006\u0007"}, d2 = {"distinctUntilChanged", "Lkotlinx/coroutines/flow/Flow;", "T", "distinctUntilChangedBy", "K", "keySelector", "Lkotlin/Function1;", "kotlinx-coroutines-core"}, k = 5, mv = {1, 1, 15}, xs = "kotlinx/coroutines/flow/FlowKt")
/* loaded from: classes2.dex */
public final /* synthetic */ class FlowKt__DistinctKt {
    @FlowPreview
    @NotNull
    public static final <T> Flow<T> distinctUntilChanged(@NotNull Flow<? extends T> distinctUntilChanged) {
        Intrinsics.checkParameterIsNotNull(distinctUntilChanged, "$this$distinctUntilChanged");
        return FlowKt.distinctUntilChangedBy(distinctUntilChanged, new Function1<T, T>() { // from class: kotlinx.coroutines.flow.FlowKt__DistinctKt$distinctUntilChanged$1
            @Override // kotlin.jvm.functions.Function1
            public final T invoke(T t) {
                return t;
            }
        });
    }

    @FlowPreview
    @NotNull
    public static final <T, K> Flow<T> distinctUntilChangedBy(@NotNull Flow<? extends T> distinctUntilChangedBy, @NotNull Function1<? super T, ? extends K> keySelector) {
        Intrinsics.checkParameterIsNotNull(distinctUntilChangedBy, "$this$distinctUntilChangedBy");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        return FlowKt.unsafeFlow(new FlowKt__DistinctKt$distinctUntilChangedBy$1(distinctUntilChangedBy, keySelector, null));
    }
}
