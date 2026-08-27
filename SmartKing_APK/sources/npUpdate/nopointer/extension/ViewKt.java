package npUpdate.nopointer.extension;

import android.view.View;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: View.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\u001a\u0012\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004¨\u0006\u0005"}, d2 = {"visibleOrGone", "", "Landroid/view/View;", "show", "", "npUpdate_release"}, k = 2, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final class ViewKt {
    public static final void visibleOrGone(@NotNull View visibleOrGone, boolean z) {
        Intrinsics.checkParameterIsNotNull(visibleOrGone, "$this$visibleOrGone");
        if (z) {
            visibleOrGone.setVisibility(0);
        } else {
            visibleOrGone.setVisibility(8);
        }
    }
}
