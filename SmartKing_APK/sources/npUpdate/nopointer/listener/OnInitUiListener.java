package npUpdate.nopointer.listener;

import android.view.View;
import kotlin.Metadata;
import npUpdate.nopointer.model.UiConfig;
import npUpdate.nopointer.model.UpdateConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: OnInitUiListener.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\"\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH&¨\u0006\n"}, d2 = {"LnpUpdate/nopointer/listener/OnInitUiListener;", "", "onInitUpdateUi", "", "view", "Landroid/view/View;", "updateConfig", "LnpUpdate/nopointer/model/UpdateConfig;", "uiConfig", "LnpUpdate/nopointer/model/UiConfig;", "npUpdate_release"}, k = 1, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public interface OnInitUiListener {
    void onInitUpdateUi(@Nullable View view, @NotNull UpdateConfig updateConfig, @NotNull UiConfig uiConfig);
}
