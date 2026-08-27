package com.qmuiteam.qmui.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import java.lang.Character;

/* loaded from: classes2.dex */
public class QMUIVerticalTextView extends TextView {
    private boolean mIsVerticalMode;
    private int[] mLineBreakIndex;
    private int mLineCount;
    private float[] mLineWidths;

    public QMUIVerticalTextView(Context context) {
        super(context);
        this.mIsVerticalMode = true;
        init();
    }

    public QMUIVerticalTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mIsVerticalMode = true;
        init();
    }

    public QMUIVerticalTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mIsVerticalMode = true;
        init();
    }

    private void init() {
    }

    private static boolean isCJKCharacter(char c) {
        Character.UnicodeBlock of = Character.UnicodeBlock.of(c);
        return of == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || of == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || of == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || of == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || of == Character.UnicodeBlock.HANGUL_SYLLABLES || of == Character.UnicodeBlock.HANGUL_JAMO || of == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO || of == Character.UnicodeBlock.HIRAGANA || of == Character.UnicodeBlock.KATAKANA || of == Character.UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS;
    }

    public boolean isVerticalMode() {
        return this.mIsVerticalMode;
    }

    @Override // android.widget.TextView, android.view.View
    @TargetApi(16)
    protected void onDraw(Canvas canvas) {
        int i;
        if (!this.mIsVerticalMode) {
            super.onDraw(canvas);
            return;
        }
        if (this.mLineCount == 0) {
            return;
        }
        TextPaint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.drawableState = getDrawableState();
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        char[] charArray = getText().toString().toCharArray();
        canvas.save();
        float width = (getWidth() - getPaddingRight()) - this.mLineWidths[0];
        float f = width;
        float paddingTop = getPaddingTop();
        int i2 = 0;
        int i3 = 0;
        while (i2 < charArray.length) {
            boolean z = !isCJKCharacter(charArray[i2]);
            int save = canvas.save();
            if (z) {
                canvas.rotate(90.0f, width, paddingTop);
            }
            float f2 = width;
            canvas.drawText(charArray, i2, 1, width, z ? (paddingTop - ((this.mLineWidths[i3] - (fontMetricsInt.bottom - fontMetricsInt.top)) / 2.0f)) - fontMetricsInt.descent : paddingTop - fontMetricsInt.ascent, paint);
            canvas.restoreToCount(save);
            int i4 = i2 + 1;
            if (i4 < charArray.length) {
                if (!(i4 > this.mLineBreakIndex[i3]) || (i = i3 + 1) >= this.mLineWidths.length) {
                    paddingTop = z ? paddingTop + paint.measureText(charArray, i2, 1) : paddingTop + (fontMetricsInt.descent - fontMetricsInt.ascent);
                } else {
                    width = f - ((this.mLineWidths[i] * getLineSpacingMultiplier()) + getLineSpacingExtra());
                    i3 = i;
                    paddingTop = getPaddingTop();
                    f = width;
                    i2 = i4;
                }
            }
            width = f2;
            i2 = i4;
        }
        canvas.restore();
    }

    @Override // android.widget.TextView, android.view.View
    @SuppressLint({"DrawAllocation"})
    @TargetApi(16)
    protected void onMeasure(int i, int i2) {
        int i3;
        float measureText;
        TextPaint textPaint;
        float f;
        super.onMeasure(i, i2);
        if (this.mIsVerticalMode) {
            int mode = View.MeasureSpec.getMode(i);
            int mode2 = View.MeasureSpec.getMode(i2);
            int size = View.MeasureSpec.getSize(i);
            int size2 = View.MeasureSpec.getSize(i2);
            float paddingLeft = getPaddingLeft() + getPaddingRight();
            float paddingTop = getPaddingTop() + getPaddingBottom();
            char[] charArray = getText().toString().toCharArray();
            TextPaint paint = getPaint();
            Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
            int paddingBottom = (mode2 == 0 ? Integer.MAX_VALUE : size2) - getPaddingBottom();
            float paddingTop2 = getPaddingTop();
            this.mLineCount = 0;
            int i4 = 1;
            this.mLineWidths = new float[charArray.length + 1];
            this.mLineBreakIndex = new int[charArray.length + 1];
            float f2 = paddingLeft;
            float f3 = paddingTop;
            float f4 = paddingTop2;
            int i5 = 0;
            int i6 = 0;
            while (i5 < charArray.length) {
                if (((isCJKCharacter(charArray[i5]) ? 1 : 0) ^ i4) != 0) {
                    measureText = fontMetricsInt.descent - fontMetricsInt.ascent;
                    textPaint = paint;
                    f = paint.measureText(charArray, i5, 1);
                } else {
                    measureText = paint.measureText(charArray, i5, i4);
                    textPaint = paint;
                    f = fontMetricsInt.descent - fontMetricsInt.ascent;
                }
                float f5 = paddingTop2 + f;
                float f6 = f;
                if (f5 > ((float) paddingBottom) && i5 > 0) {
                    if (f4 < paddingTop2) {
                        f4 = paddingTop2;
                    }
                    this.mLineBreakIndex[i6] = i5 - 1;
                    f2 += this.mLineWidths[i6];
                    i6++;
                    paddingTop2 = f6;
                } else if (f4 < f5) {
                    paddingTop2 = f5;
                    f4 = paddingTop2;
                } else {
                    paddingTop2 = f5;
                }
                if (this.mLineWidths[i6] < measureText) {
                    this.mLineWidths[i6] = measureText;
                }
                if (i5 == charArray.length - 1) {
                    f2 += this.mLineWidths[i6];
                    f3 = f4 + getPaddingBottom();
                }
                i5++;
                paint = textPaint;
                i4 = 1;
            }
            if (charArray.length > 0) {
                this.mLineCount = i6 + 1;
                i3 = 1;
                this.mLineBreakIndex[i6] = charArray.length - 1;
            } else {
                i3 = 1;
            }
            if (this.mLineCount > i3) {
                int i7 = this.mLineCount - i3;
                for (int i8 = 0; i8 < i7; i8++) {
                    f2 += (this.mLineWidths[i8] * (getLineSpacingMultiplier() - 1.0f)) + getLineSpacingExtra();
                }
            }
            if (mode2 == 1073741824) {
                f3 = size2;
            } else if (mode2 == Integer.MIN_VALUE) {
                f3 = Math.min(f3, size2);
            }
            if (mode == 1073741824) {
                f2 = size;
            } else if (mode == Integer.MIN_VALUE) {
                f2 = Math.min(f2, size);
            }
            setMeasuredDimension((int) f2, (int) f3);
        }
    }

    public void setVerticalMode(boolean z) {
        this.mIsVerticalMode = z;
        requestLayout();
    }
}
