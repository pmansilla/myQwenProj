package com.qmuiteam.qmui.widget.roundwidget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import com.qmuiteam.qmui.R;
import com.qmuiteam.qmui.util.QMUIViewHelper;

/* loaded from: classes2.dex */
public class QMUIRoundRelativeLayout extends RelativeLayout {
    public QMUIRoundRelativeLayout(Context context) {
        super(context);
        init(context, null, 0);
    }

    public QMUIRoundRelativeLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet, R.attr.QMUIButtonStyle);
    }

    public QMUIRoundRelativeLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet, i);
    }

    private void init(Context context, AttributeSet attributeSet, int i) {
        QMUIViewHelper.setBackgroundKeepingPadding(this, QMUIRoundButtonDrawable.fromAttributeSet(context, attributeSet, i));
    }
}
