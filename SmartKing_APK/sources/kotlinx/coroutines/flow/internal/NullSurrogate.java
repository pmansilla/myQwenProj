package kotlinx.coroutines.flow.internal;

import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import org.jetbrains.annotations.Nullable;

/* compiled from: NullSurrogate.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0007\bÀ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001f\u0010\u0003\u001a\u0002H\u0004\"\u0004\b\u0000\u0010\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0001H\u0001¢\u0006\u0004\b\u0006\u0010\u0007¨\u0006\b"}, d2 = {"Lkotlinx/coroutines/flow/internal/NullSurrogate;", "", "()V", "unbox", "T", "value", "unbox$kotlinx_coroutines_core", "(Ljava/lang/Object;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final class NullSurrogate {
    public static final NullSurrogate INSTANCE = new NullSurrogate();

    private NullSurrogate() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    @JvmStatic
    public static final <T> T unbox$kotlinx_coroutines_core(@Nullable Object value) {
        if (value == INSTANCE) {
            return null;
        }
        return value;
    }
}
