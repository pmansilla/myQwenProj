package com.qmuiteam.qmui.widget.dialog;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.qmuiteam.qmui.R;
import com.qmuiteam.qmui.layout.QMUILinearLayout;
import com.qmuiteam.qmui.util.QMUIResHelper;

/* loaded from: classes2.dex */
public class QMUIDialogView extends QMUILinearLayout {
    private int mMaxWidth;
    private int mMinWidth;
    private OnDecorationListener mOnDecorationListener;

    /* loaded from: classes2.dex */
    public interface OnDecorationListener {
        void onDraw(Canvas canvas, QMUIDialogView qMUIDialogView);

        void onDrawOver(Canvas canvas, QMUIDialogView qMUIDialogView);
    }

    public QMUIDialogView(Context context) {
        this(context, null);
    }

    public QMUIDialogView(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public QMUIDialogView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mMinWidth = QMUIResHelper.getAttrDimen(context, R.attr.qmui_dialog_min_width);
        this.mMaxWidth = QMUIResHelper.getAttrDimen(context, R.attr.qmui_dialog_max_width);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.qmuiteam.qmui.layout.QMUILinearLayout, android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.mOnDecorationListener != null) {
            this.mOnDecorationListener.onDrawOver(canvas, this);
        }
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mOnDecorationListener != null) {
            this.mOnDecorationListener.onDraw(canvas, this);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.qmuiteam.qmui.layout.QMUILinearLayout, android.widget.LinearLayout, android.view.View
    public void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        int mode = View.MeasureSpec.getMode(i);
        if (this.mMaxWidth > 0 && size > this.mMaxWidth) {
            i = View.MeasureSpec.makeMeasureSpec(this.mMaxWidth, mode);
        }
        super.onMeasure(i, i2);
        if (mode != Integer.MIN_VALUE || getMeasuredWidth() >= this.mMinWidth || this.mMinWidth >= size) {
            return;
        }
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(this.mMinWidth, 1073741824), i2);
    }

    public void setOnDecorationListener(OnDecorationListener onDecorationListener) {
        this.mOnDecorationListener = onDecorationListener;
    }
}
