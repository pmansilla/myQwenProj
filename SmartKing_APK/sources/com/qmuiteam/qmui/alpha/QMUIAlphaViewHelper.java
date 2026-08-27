package com.qmuiteam.qmui.alpha;

import android.support.annotation.NonNull;
import android.view.View;
import com.qmuiteam.qmui.R;
import com.qmuiteam.qmui.util.QMUIResHelper;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public class QMUIAlphaViewHelper {
    private float mDisabledAlpha;
    private float mPressedAlpha;
    private WeakReference<View> mTarget;
    private boolean mChangeAlphaWhenPress = true;
    private boolean mChangeAlphaWhenDisable = true;
    private float mNormalAlpha = 1.0f;

    public QMUIAlphaViewHelper(@NonNull View view) {
        this.mPressedAlpha = 0.5f;
        this.mDisabledAlpha = 0.5f;
        this.mTarget = new WeakReference<>(view);
        this.mPressedAlpha = QMUIResHelper.getAttrFloatValue(view.getContext(), R.attr.qmui_alpha_pressed);
        this.mDisabledAlpha = QMUIResHelper.getAttrFloatValue(view.getContext(), R.attr.qmui_alpha_disabled);
    }

    public QMUIAlphaViewHelper(@NonNull View view, float f, float f2) {
        this.mPressedAlpha = 0.5f;
        this.mDisabledAlpha = 0.5f;
        this.mTarget = new WeakReference<>(view);
        this.mPressedAlpha = f;
        this.mDisabledAlpha = f2;
    }

    public void onEnabledChanged(View view, boolean z) {
        View view2 = this.mTarget.get();
        if (view2 == null) {
            return;
        }
        float f = this.mChangeAlphaWhenDisable ? z ? this.mNormalAlpha : this.mDisabledAlpha : this.mNormalAlpha;
        if (view != view2 && view2.isEnabled() != z) {
            view2.setEnabled(z);
        }
        view2.setAlpha(f);
    }

    public void onPressedChanged(View view, boolean z) {
        View view2 = this.mTarget.get();
        if (view2 == null) {
            return;
        }
        if (view.isEnabled()) {
            view2.setAlpha((this.mChangeAlphaWhenPress && z && view.isClickable()) ? this.mPressedAlpha : this.mNormalAlpha);
        } else if (this.mChangeAlphaWhenDisable) {
            view2.setAlpha(this.mDisabledAlpha);
        }
    }

    public void setChangeAlphaWhenDisable(boolean z) {
        this.mChangeAlphaWhenDisable = z;
        View view = this.mTarget.get();
        if (view != null) {
            onEnabledChanged(view, view.isEnabled());
        }
    }

    public void setChangeAlphaWhenPress(boolean z) {
        this.mChangeAlphaWhenPress = z;
    }
}
