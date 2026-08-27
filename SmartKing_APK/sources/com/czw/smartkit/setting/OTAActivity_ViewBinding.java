package com.czw.smartkit.setting;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.czw.smartkit.R;

/* loaded from: classes.dex */
public class OTAActivity_ViewBinding implements Unbinder {
    private OTAActivity target;
    private View view2131296366;

    @UiThread
    public OTAActivity_ViewBinding(OTAActivity oTAActivity) {
        this(oTAActivity, oTAActivity.getWindow().getDecorView());
    }

    @UiThread
    public OTAActivity_ViewBinding(final OTAActivity oTAActivity, View view) {
        this.target = oTAActivity;
        View findRequiredView = Utils.findRequiredView(view, R.id.checkoutOtaBtn, "method 'checkoutFirmware'");
        this.view2131296366 = findRequiredView;
        findRequiredView.setOnClickListener(new DebouncingOnClickListener() { // from class: com.czw.smartkit.setting.OTAActivity_ViewBinding.1
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                oTAActivity.checkoutFirmware(view2);
            }
        });
    }

    @Override // butterknife.Unbinder
    @CallSuper
    public void unbind() {
        if (this.target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        this.view2131296366.setOnClickListener(null);
        this.view2131296366 = null;
    }
}
