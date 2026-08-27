package com.czw.smartkit.setting;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.czw.smartkit.R;

/* loaded from: classes.dex */
public class AboutActivity_ViewBinding implements Unbinder {
    private AboutActivity target;
    private View view2131296402;

    @UiThread
    public AboutActivity_ViewBinding(AboutActivity aboutActivity) {
        this(aboutActivity, aboutActivity.getWindow().getDecorView());
    }

    @UiThread
    public AboutActivity_ViewBinding(final AboutActivity aboutActivity, View view) {
        this.target = aboutActivity;
        View findRequiredView = Utils.findRequiredView(view, R.id.debug_btn, "field 'tvDebug' and method 'onClick'");
        aboutActivity.tvDebug = (TextView) Utils.castView(findRequiredView, R.id.debug_btn, "field 'tvDebug'", TextView.class);
        this.view2131296402 = findRequiredView;
        findRequiredView.setOnClickListener(new DebouncingOnClickListener() { // from class: com.czw.smartkit.setting.AboutActivity_ViewBinding.1
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                aboutActivity.onClick(view2);
            }
        });
    }

    @Override // butterknife.Unbinder
    @CallSuper
    public void unbind() {
        AboutActivity aboutActivity = this.target;
        if (aboutActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        aboutActivity.tvDebug = null;
        this.view2131296402.setOnClickListener(null);
        this.view2131296402 = null;
    }
}
