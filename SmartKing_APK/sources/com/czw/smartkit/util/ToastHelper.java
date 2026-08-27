package com.czw.smartkit.util;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;
import com.czw.smartkit.MainApplication;

/* loaded from: classes.dex */
public class ToastHelper {
    private static ToastHelper toastHelper = new ToastHelper();
    private Activity activity;
    Toast toast = null;

    private ToastHelper() {
    }

    public static ToastHelper getToastHelper() {
        return toastHelper;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void show(int i) {
        show(MainApplication.getApp().getResources().getString(i));
    }

    public void show(final String str) {
        if (this.activity == null) {
            this.toast = null;
        } else {
            this.activity.runOnUiThread(new Runnable() { // from class: com.czw.smartkit.util.ToastHelper.1
                @Override // java.lang.Runnable
                public void run() {
                    if (ToastHelper.this.toast == null) {
                        ToastHelper.this.toast = Toast.makeText(ToastHelper.this.activity, str, 1);
                        ToastHelper.this.toast.getView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.czw.smartkit.util.ToastHelper.1.1
                            @Override // android.view.View.OnAttachStateChangeListener
                            public void onViewAttachedToWindow(View view) {
                            }

                            @Override // android.view.View.OnAttachStateChangeListener
                            public void onViewDetachedFromWindow(View view) {
                                ToastHelper.this.toast = null;
                            }
                        });
                    } else {
                        ToastHelper.this.toast.setDuration(1);
                    }
                    ToastHelper.this.toast.show();
                }
            });
        }
    }
}
