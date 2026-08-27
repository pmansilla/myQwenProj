package com.czw.smartkit.measure;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.czw.smartkit.R;
import com.czw.smartkit.views.LineView;

/* loaded from: classes.dex */
public class MeasureHrActivity_ViewBinding implements Unbinder {
    private MeasureHrActivity target;
    private View view2131296612;

    @UiThread
    public MeasureHrActivity_ViewBinding(MeasureHrActivity measureHrActivity) {
        this(measureHrActivity, measureHrActivity.getWindow().getDecorView());
    }

    @UiThread
    public MeasureHrActivity_ViewBinding(final MeasureHrActivity measureHrActivity, View view) {
        this.target = measureHrActivity;
        View findRequiredView = Utils.findRequiredView(view, R.id.measureBtn, "field 'measureBtn' and method 'click'");
        measureHrActivity.measureBtn = (Button) Utils.castView(findRequiredView, R.id.measureBtn, "field 'measureBtn'", Button.class);
        this.view2131296612 = findRequiredView;
        findRequiredView.setOnClickListener(new DebouncingOnClickListener() { // from class: com.czw.smartkit.measure.MeasureHrActivity_ViewBinding.1
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                measureHrActivity.click(view2);
            }
        });
        measureHrActivity.measureHrValue = (TextView) Utils.findRequiredViewAsType(view, R.id.ic_type_value, "field 'measureHrValue'", TextView.class);
        measureHrActivity.measureLineShowView = (LineView) Utils.findRequiredViewAsType(view, R.id.lineView, "field 'measureLineShowView'", LineView.class);
        measureHrActivity.measureTypeIcon = (ImageView) Utils.findRequiredViewAsType(view, R.id.ic_type, "field 'measureTypeIcon'", ImageView.class);
    }

    @Override // butterknife.Unbinder
    @CallSuper
    public void unbind() {
        MeasureHrActivity measureHrActivity = this.target;
        if (measureHrActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        measureHrActivity.measureBtn = null;
        measureHrActivity.measureHrValue = null;
        measureHrActivity.measureLineShowView = null;
        measureHrActivity.measureTypeIcon = null;
        this.view2131296612.setOnClickListener(null);
        this.view2131296612 = null;
    }
}
