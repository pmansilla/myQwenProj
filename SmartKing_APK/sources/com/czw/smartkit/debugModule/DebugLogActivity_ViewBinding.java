package com.czw.smartkit.debugModule;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.czw.smartkit.R;

/* loaded from: classes.dex */
public class DebugLogActivity_ViewBinding implements Unbinder {
    private DebugLogActivity target;
    private View view2131296371;
    private View view2131296698;
    private View view2131296742;

    @UiThread
    public DebugLogActivity_ViewBinding(DebugLogActivity debugLogActivity) {
        this(debugLogActivity, debugLogActivity.getWindow().getDecorView());
    }

    @UiThread
    public DebugLogActivity_ViewBinding(final DebugLogActivity debugLogActivity, View view) {
        this.target = debugLogActivity;
        debugLogActivity.log_text_tv = (TextView) Utils.findRequiredViewAsType(view, R.id.log_text_tv, "field 'log_text_tv'", TextView.class);
        View findRequiredView = Utils.findRequiredView(view, R.id.refresh_log_file_btn, "method 'click'");
        this.view2131296698 = findRequiredView;
        findRequiredView.setOnClickListener(new DebouncingOnClickListener() { // from class: com.czw.smartkit.debugModule.DebugLogActivity_ViewBinding.1
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                debugLogActivity.click(view2);
            }
        });
        View findRequiredView2 = Utils.findRequiredView(view, R.id.clear_log_file_btn, "method 'click'");
        this.view2131296371 = findRequiredView2;
        findRequiredView2.setOnClickListener(new DebouncingOnClickListener() { // from class: com.czw.smartkit.debugModule.DebugLogActivity_ViewBinding.2
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                debugLogActivity.click(view2);
            }
        });
        View findRequiredView3 = Utils.findRequiredView(view, R.id.send_log_file_btn, "method 'click'");
        this.view2131296742 = findRequiredView3;
        findRequiredView3.setOnClickListener(new DebouncingOnClickListener() { // from class: com.czw.smartkit.debugModule.DebugLogActivity_ViewBinding.3
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                debugLogActivity.click(view2);
            }
        });
    }

    @Override // butterknife.Unbinder
    @CallSuper
    public void unbind() {
        DebugLogActivity debugLogActivity = this.target;
        if (debugLogActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        debugLogActivity.log_text_tv = null;
        this.view2131296698.setOnClickListener(null);
        this.view2131296698 = null;
        this.view2131296371.setOnClickListener(null);
        this.view2131296371 = null;
        this.view2131296742.setOnClickListener(null);
        this.view2131296742 = null;
    }
}
