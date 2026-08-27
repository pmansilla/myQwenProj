package com.czw.smartkit.base;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.webkit.WebView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.czw.smartkit.R;

/* loaded from: classes.dex */
public class WebActivity_ViewBinding implements Unbinder {
    private WebActivity target;

    @UiThread
    public WebActivity_ViewBinding(WebActivity webActivity) {
        this(webActivity, webActivity.getWindow().getDecorView());
    }

    @UiThread
    public WebActivity_ViewBinding(WebActivity webActivity, View view) {
        this.target = webActivity;
        webActivity.webView = (WebView) Utils.findRequiredViewAsType(view, R.id.webView, "field 'webView'", WebView.class);
    }

    @Override // butterknife.Unbinder
    @CallSuper
    public void unbind() {
        WebActivity webActivity = this.target;
        if (webActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        webActivity.webView = null;
    }
}
