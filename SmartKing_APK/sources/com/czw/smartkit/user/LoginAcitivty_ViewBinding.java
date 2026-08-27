package com.czw.smartkit.user;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.CheckBox;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import cn.droidlover.xrichtext.XRichText;
import com.czw.smartkit.R;

/* loaded from: classes.dex */
public class LoginAcitivty_ViewBinding implements Unbinder {
    private LoginAcitivty target;

    @UiThread
    public LoginAcitivty_ViewBinding(LoginAcitivty loginAcitivty) {
        this(loginAcitivty, loginAcitivty.getWindow().getDecorView());
    }

    @UiThread
    public LoginAcitivty_ViewBinding(LoginAcitivty loginAcitivty, View view) {
        this.target = loginAcitivty;
        loginAcitivty.bottomTextTv = (XRichText) Utils.findRequiredViewAsType(view, R.id.messageText, "field 'bottomTextTv'", XRichText.class);
        loginAcitivty.checkBoxAgreen = (CheckBox) Utils.findRequiredViewAsType(view, R.id.checkbox_agreen, "field 'checkBoxAgreen'", CheckBox.class);
    }

    @Override // butterknife.Unbinder
    @CallSuper
    public void unbind() {
        LoginAcitivty loginAcitivty = this.target;
        if (loginAcitivty == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        loginAcitivty.bottomTextTv = null;
        loginAcitivty.checkBoxAgreen = null;
    }
}
