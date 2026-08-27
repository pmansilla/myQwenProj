package npUpdate.nopointer.extension;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import npUpdate.nopointer.update.UpdateAppUtils;
import npUpdate.nopointer.util.GlobalContextProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Any.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0000\n\u0002\u0010\u000e\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\u001a\u0012\u0010\u0005\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0006\u001a\u0014\u0010\u0007\u001a\u00020\b*\u00020\u00022\b\u0010\t\u001a\u0004\u0018\u00010\u0001\u001a\u001a\u0010\n\u001a\n \u000b*\u0004\u0018\u00010\u00010\u0001*\u00020\u00022\u0006\u0010\n\u001a\u00020\u0006\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004¨\u0006\f"}, d2 = {"TAG", "", "", "getTAG", "(Ljava/lang/Object;)Ljava/lang/String;", "color", "", "log", "", "content", "string", "kotlin.jvm.PlatformType", "npUpdate_release"}, k = 2, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final class AnyKt {
    public static final int color(@NotNull Object color, int i) {
        Intrinsics.checkParameterIsNotNull(color, "$this$color");
        return ContextCompat.getColor(GlobalContextProvider.INSTANCE.getGlobalContext(), i);
    }

    @NotNull
    public static final String getTAG(@NotNull Object TAG) {
        Intrinsics.checkParameterIsNotNull(TAG, "$this$TAG");
        String simpleName = TAG.getClass().getSimpleName();
        Intrinsics.checkExpressionValueIsNotNull(simpleName, "this::class.java.simpleName");
        return simpleName;
    }

    public static final void log(@NotNull Object log, @Nullable String str) {
        Intrinsics.checkParameterIsNotNull(log, "$this$log");
        if (UpdateAppUtils.INSTANCE.getUpdateInfo$npUpdate_release().getConfig().isDebug()) {
            Log.e("[UpdateAppUtils]", str);
        }
    }

    public static final String string(@NotNull Object string, int i) {
        Intrinsics.checkParameterIsNotNull(string, "$this$string");
        return GlobalContextProvider.INSTANCE.getGlobalContext().getString(i);
    }
}
