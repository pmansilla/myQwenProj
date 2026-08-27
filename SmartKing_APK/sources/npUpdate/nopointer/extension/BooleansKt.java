package npUpdate.nopointer.extension;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Booleans.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\u001a1\u0010\u0000\u001a\u0004\u0018\u00010\u0001*\u0004\u0018\u00010\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u0086\b\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0000¢\u0006\u0002\u0010\u0005\u001a1\u0010\u0006\u001a\u0004\u0018\u00010\u0001*\u0004\u0018\u00010\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u0086\b\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0000¢\u0006\u0002\u0010\u0005¨\u0006\u0007"}, d2 = {"no", "", "block", "Lkotlin/Function0;", "", "(Ljava/lang/Boolean;Lkotlin/jvm/functions/Function0;)Ljava/lang/Boolean;", "yes", "npUpdate_release"}, k = 2, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final class BooleansKt {
    @Nullable
    public static final Boolean no(@Nullable Boolean bool, @NotNull Function0<Unit> block) {
        Intrinsics.checkParameterIsNotNull(block, "block");
        if (true ^ Intrinsics.areEqual((Object) bool, (Object) true)) {
            block.invoke();
        }
        return bool;
    }

    @Nullable
    public static final Boolean yes(@Nullable Boolean bool, @NotNull Function0<Unit> block) {
        Intrinsics.checkParameterIsNotNull(block, "block");
        if (Intrinsics.areEqual((Object) bool, (Object) true)) {
            block.invoke();
        }
        return bool;
    }
}
