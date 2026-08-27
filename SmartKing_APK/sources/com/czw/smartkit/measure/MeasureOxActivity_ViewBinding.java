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

/* loaded from: classes.dex */
public class MeasureOxActivity_ViewBinding implements Unbinder {
    private MeasureOxActivity target;
    private View view2131296612;

    @UiThread
    public MeasureOxActivity_ViewBinding(MeasureOxActivity measureOxActivity) {
        this(measureOxActivity, measureOxActivity.getWindow().getDecorView());
    }

    @UiThread
    public MeasureOxActivity_ViewBinding(final MeasureOxActivity measureOxActivity, View view) {
        this.target = measureOxActivity;
        measureOxActivity.measureTypeIcon = (ImageView) Utils.findRequiredViewAsType(view, R.id.ic_type, "field 'measureTypeIcon'", ImageView.class);
        View findRequiredView = Utils.findRequiredView(view, R.id.measureBtn, "field 'measureBtn' and method 'click'");
        measureOxActivity.measureBtn = (Button) Utils.castView(findRequiredView, R.id.measureBtn, "field 'measureBtn'", Button.class);
        this.view2131296612 = findRequiredView;
        findRequiredView.setOnClickListener(new DebouncingOnClickListener() { // from class: com.czw.smartkit.measure.MeasureOxActivity_ViewBinding.1
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                measureOxActivity.click(view2);
            }
        });
        measureOxActivity.measureOxValue = (TextView) Utils.findRequiredViewAsType(view, R.id.ic_type_value, "field 'measureOxValue'", TextView.class);
    }

    @Override // butterknife.Unbinder
    @CallSuper
    public void unbind() {
        MeasureOxActivity measureOxActivity = this.target;
        if (measureOxActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        measureOxActivity.measureTypeIcon = null;
        measureOxActivity.measureBtn = null;
        measureOxActivity.measureOxValue = null;
        this.view2131296612.setOnClickListener(null);
        this.view2131296612 = null;
    }
}
