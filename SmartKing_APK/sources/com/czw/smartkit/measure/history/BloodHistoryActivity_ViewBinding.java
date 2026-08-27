package com.czw.smartkit.measure.history;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.czw.smartkit.R;
import com.czw.smartkit.views.multiColumnView.MultiColumnView;

/* loaded from: classes.dex */
public class BloodHistoryActivity_ViewBinding implements Unbinder {
    private BloodHistoryActivity target;

    @UiThread
    public BloodHistoryActivity_ViewBinding(BloodHistoryActivity bloodHistoryActivity) {
        this(bloodHistoryActivity, bloodHistoryActivity.getWindow().getDecorView());
    }

    @UiThread
    public BloodHistoryActivity_ViewBinding(BloodHistoryActivity bloodHistoryActivity, View view) {
        this.target = bloodHistoryActivity;
        bloodHistoryActivity.multiColumnView = (MultiColumnView) Utils.findRequiredViewAsType(view, R.id.blood_history_multiColumnView, "field 'multiColumnView'", MultiColumnView.class);
    }

    @Override // butterknife.Unbinder
    @CallSuper
    public void unbind() {
        BloodHistoryActivity bloodHistoryActivity = this.target;
        if (bloodHistoryActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        bloodHistoryActivity.multiColumnView = null;
    }
}
