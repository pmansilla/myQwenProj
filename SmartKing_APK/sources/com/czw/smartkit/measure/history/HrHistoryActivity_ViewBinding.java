package com.czw.smartkit.measure.history;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.czw.smartkit.R;
import com.czw.smartkit.views.lineview.LineShowView;

/* loaded from: classes.dex */
public class HrHistoryActivity_ViewBinding implements Unbinder {
    private HrHistoryActivity target;

    @UiThread
    public HrHistoryActivity_ViewBinding(HrHistoryActivity hrHistoryActivity) {
        this(hrHistoryActivity, hrHistoryActivity.getWindow().getDecorView());
    }

    @UiThread
    public HrHistoryActivity_ViewBinding(HrHistoryActivity hrHistoryActivity, View view) {
        this.target = hrHistoryActivity;
        hrHistoryActivity.lineView = (LineShowView) Utils.findRequiredViewAsType(view, R.id.hr_history_lineView, "field 'lineView'", LineShowView.class);
    }

    @Override // butterknife.Unbinder
    @CallSuper
    public void unbind() {
        HrHistoryActivity hrHistoryActivity = this.target;
        if (hrHistoryActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        hrHistoryActivity.lineView = null;
    }
}
