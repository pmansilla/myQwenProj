package com.czw.smartkit.measure.history;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.czw.smartkit.R;
import com.czw.smartkit.views.lineview.LineShowView;

/* loaded from: classes.dex */
public class OxHistoryActivity_ViewBinding implements Unbinder {
    private OxHistoryActivity target;

    @UiThread
    public OxHistoryActivity_ViewBinding(OxHistoryActivity oxHistoryActivity) {
        this(oxHistoryActivity, oxHistoryActivity.getWindow().getDecorView());
    }

    @UiThread
    public OxHistoryActivity_ViewBinding(OxHistoryActivity oxHistoryActivity, View view) {
        this.target = oxHistoryActivity;
        oxHistoryActivity.lineView = (LineShowView) Utils.findRequiredViewAsType(view, R.id.ox_history_lineView, "field 'lineView'", LineShowView.class);
    }

    @Override // butterknife.Unbinder
    @CallSuper
    public void unbind() {
        OxHistoryActivity oxHistoryActivity = this.target;
        if (oxHistoryActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        oxHistoryActivity.lineView = null;
    }
}
