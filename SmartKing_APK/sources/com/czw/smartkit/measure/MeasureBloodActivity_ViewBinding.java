package com.czw.smartkit.measure;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.czw.smartkit.R;

/* loaded from: classes.dex */
public class MeasureBloodActivity_ViewBinding implements Unbinder {
    private MeasureBloodActivity target;
    private View view2131296612;

    @UiThread
    public MeasureBloodActivity_ViewBinding(MeasureBloodActivity measureBloodActivity) {
        this(measureBloodActivity, measureBloodActivity.getWindow().getDecorView());
    }

    @UiThread
    public MeasureBloodActivity_ViewBinding(final MeasureBloodActivity measureBloodActivity, View view) {
        this.target = measureBloodActivity;
        View findRequiredView = Utils.findRequiredView(view, R.id.measureBtn, "field 'measureBtn' and method 'click'");
        measureBloodActivity.measureBtn = (Button) Utils.castView(findRequiredView, R.id.measureBtn, "field 'measureBtn'", Button.class);
        this.view2131296612 = findRequiredView;
        findRequiredView.setOnClickListener(new DebouncingOnClickListener() { // from class: com.czw.smartkit.measure.MeasureBloodActivity_ViewBinding.1
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                measureBloodActivity.click(view2);
            }
        });
        measureBloodActivity.measureBloodValue = (TextView) Utils.findRequiredViewAsType(view, R.id.ic_type_value, "field 'measureBloodValue'", TextView.class);
    }

    @Override // butterknife.Unbinder
    @CallSuper
    public void unbind() {
        MeasureBloodActivity measureBloodActivity = this.target;
        if (measureBloodActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        measureBloodActivity.measureBtn = null;
        measureBloodActivity.measureBloodValue = null;
        this.view2131296612.setOnClickListener(null);
        this.view2131296612 = null;
    }
}
