package kotlinx.coroutines.internal;

import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.InternalCoroutinesApi;
import kotlinx.coroutines.MainCoroutineDispatcher;
import org.jetbrains.annotations.NotNull;

/* compiled from: MainDispatchers.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\u001a\f\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0007\u001a\u001a\u0010\u0003\u001a\u00020\u0002*\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0006H\u0007¨\u0006\u0007"}, d2 = {"isMissing", "", "Lkotlinx/coroutines/MainCoroutineDispatcher;", "tryCreateDispatcher", "Lkotlinx/coroutines/internal/MainDispatcherFactory;", "factories", "", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final class MainDispatchersKt {
    @InternalCoroutinesApi
    public static final boolean isMissing(@NotNull MainCoroutineDispatcher isMissing) {
        Intrinsics.checkParameterIsNotNull(isMissing, "$this$isMissing");
        return isMissing instanceof MissingMainCoroutineDispatcher;
    }

    @InternalCoroutinesApi
    @NotNull
    public static final MainCoroutineDispatcher tryCreateDispatcher(@NotNull MainDispatcherFactory tryCreateDispatcher, @NotNull List<? extends MainDispatcherFactory> factories) {
        Intrinsics.checkParameterIsNotNull(tryCreateDispatcher, "$this$tryCreateDispatcher");
        Intrinsics.checkParameterIsNotNull(factories, "factories");
        try {
            return tryCreateDispatcher.createDispatcher(factories);
        } catch (Throwable th) {
            return new MissingMainCoroutineDispatcher(th, tryCreateDispatcher.hintOnError());
        }
    }
}
