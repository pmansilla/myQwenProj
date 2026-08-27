package com.qmuiteam.qmui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.qmuiteam.qmui.R;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

/* loaded from: classes2.dex */
public class QMUIFontFitTextView extends TextView {
    private Paint mTestPaint;
    private float maxSize;
    private float minSize;

    public QMUIFontFitTextView(Context context) {
        this(context, null);
    }

    public QMUIFontFitTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mTestPaint = new Paint();
        this.mTestPaint.set(getPaint());
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.QMUIFontFitTextView);
        this.minSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUIFontFitTextView_qmui_minTextSize, Math.round(QMUIDisplayHelper.DENSITY * 14.0f));
        this.maxSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUIFontFitTextView_qmui_maxTextSize, Math.round(QMUIDisplayHelper.DENSITY * 18.0f));
        obtainStyledAttributes.recycle();
    }

    private void refitText(String str, int i) {
        if (i <= 0) {
            return;
        }
        int paddingLeft = (i - getPaddingLeft()) - getPaddingRight();
        float f = this.maxSize;
        float f2 = this.minSize;
        this.mTestPaint.set(getPaint());
        this.mTestPaint.setTextSize(this.maxSize);
        float f3 = paddingLeft;
        if (this.mTestPaint.measureText(str) <= f3) {
            f2 = this.maxSize;
        } else {
            this.mTestPaint.setTextSize(this.minSize);
            if (this.mTestPaint.measureText(str) < f3) {
                while (f - f2 > 0.5f) {
                    float f4 = (f + f2) / 2.0f;
                    this.mTestPaint.setTextSize(f4);
                    if (this.mTestPaint.measureText(str) >= f3) {
                        f = f4;
                    } else {
                        f2 = f4;
                    }
                }
            }
        }
        setTextSize(0, f2);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int size = View.MeasureSpec.getSize(i);
        int measuredHeight = getMeasuredHeight();
        refitText(getText().toString(), size);
        setMeasuredDimension(size, measuredHeight);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        if (i != i3) {
            refitText(getText().toString(), i);
        }
    }

    @Override // android.widget.TextView
    protected void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        refitText(charSequence.toString(), getWidth());
    }
}
