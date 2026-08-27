package com.qmuiteam.qmui.util;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/* loaded from: classes2.dex */
public class QMUIKeyboardHelper {
    private static final int KEYBOARD_VISIBLE_THRESHOLD_DP = 100;
    public static final int SHOW_KEYBOARD_DELAY_TIME = 200;
    private static final String TAG = "QMUIKeyboardHelper";

    /* loaded from: classes2.dex */
    public interface KeyboardVisibilityEventListener {
        void onVisibilityChanged(boolean z);
    }

    public static boolean hideKeyboard(View view) {
        if (view == null) {
            return false;
        }
        return ((InputMethodManager) view.getContext().getApplicationContext().getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 2);
    }

    public static boolean isKeyboardVisible(Activity activity) {
        Rect rect = new Rect();
        View activityRoot = QMUIViewHelper.getActivityRoot(activity);
        int round = Math.round(QMUIDisplayHelper.dp2px(activity, 100));
        activityRoot.getWindowVisibleDisplayFrame(rect);
        return activityRoot.getRootView().getHeight() - rect.height() > round;
    }

    public static void setVisibilityEventListener(final Activity activity, final KeyboardVisibilityEventListener keyboardVisibilityEventListener) {
        if (activity == null) {
            throw new NullPointerException("Parameter:activity must not be null");
        }
        if (keyboardVisibilityEventListener == null) {
            throw new NullPointerException("Parameter:listener must not be null");
        }
        final View activityRoot = QMUIViewHelper.getActivityRoot(activity);
        final ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.qmuiteam.qmui.util.QMUIKeyboardHelper.2
            private final int visibleThreshold;
            private final Rect r = new Rect();
            private boolean wasOpened = false;

            {
                this.visibleThreshold = Math.round(QMUIDisplayHelper.dp2px(activity, 100));
            }

            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                activityRoot.getWindowVisibleDisplayFrame(this.r);
                boolean z = activityRoot.getRootView().getHeight() - this.r.height() > this.visibleThreshold;
                if (z == this.wasOpened) {
                    return;
                }
                this.wasOpened = z;
                keyboardVisibilityEventListener.onVisibilityChanged(z);
            }
        };
        activityRoot.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        activity.getApplication().registerActivityLifecycleCallbacks(new QMUIActivityLifecycleCallbacks(activity) { // from class: com.qmuiteam.qmui.util.QMUIKeyboardHelper.3
            @Override // com.qmuiteam.qmui.util.QMUIActivityLifecycleCallbacks
            protected void onTargetActivityDestroyed() {
                if (Build.VERSION.SDK_INT >= 16) {
                    activityRoot.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
                } else {
                    activityRoot.getViewTreeObserver().removeGlobalOnLayoutListener(onGlobalLayoutListener);
                }
            }
        });
    }

    public static void showKeyboard(final EditText editText, int i) {
        if (editText == null) {
            return;
        }
        if (!editText.requestFocus()) {
            Log.w(TAG, "showSoftInput() can not get focus");
        } else if (i > 0) {
            editText.postDelayed(new Runnable() { // from class: com.qmuiteam.qmui.util.QMUIKeyboardHelper.1
                @Override // java.lang.Runnable
                public void run() {
                    ((InputMethodManager) editText.getContext().getApplicationContext().getSystemService("input_method")).showSoftInput(editText, 1);
                }
            }, i);
        } else {
            ((InputMethodManager) editText.getContext().getApplicationContext().getSystemService("input_method")).showSoftInput(editText, 1);
        }
    }

    public static void showKeyboard(EditText editText, boolean z) {
        showKeyboard(editText, z ? 200 : 0);
    }
}
