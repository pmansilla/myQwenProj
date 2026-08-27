package com.qmuiteam.qmui.widget.roundwidget;

import android.content.Context;
import android.util.AttributeSet;
import com.qmuiteam.qmui.R;
import com.qmuiteam.qmui.alpha.QMUIAlphaButton;
import com.qmuiteam.qmui.util.QMUIViewHelper;

/* loaded from: classes2.dex */
public class QMUIRoundButton extends QMUIAlphaButton {
    public QMUIRoundButton(Context context) {
        super(context);
        init(context, null, 0);
    }

    public QMUIRoundButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, R.attr.QMUIButtonStyle);
        init(context, attributeSet, R.attr.QMUIButtonStyle);
    }

    public QMUIRoundButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet, i);
    }

    private void init(Context context, AttributeSet attributeSet, int i) {
        QMUIViewHelper.setBackgroundKeepingPadding(this, QMUIRoundButtonDrawable.fromAttributeSet(context, attributeSet, i));
        setChangeAlphaWhenDisable(false);
        setChangeAlphaWhenPress(false);
    }
}
