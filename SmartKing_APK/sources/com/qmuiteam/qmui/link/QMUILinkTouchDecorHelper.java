package com.qmuiteam.qmui.link;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.view.MotionEvent;
import android.widget.TextView;
import com.qmuiteam.qmui.widget.textview.ISpanTouchFix;

/* loaded from: classes2.dex */
public class QMUILinkTouchDecorHelper {
    private ITouchableSpan mPressedSpan;

    public ITouchableSpan getPressedSpan(TextView textView, Spannable spannable, MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        int totalPaddingLeft = x - textView.getTotalPaddingLeft();
        int totalPaddingTop = y - textView.getTotalPaddingTop();
        int scrollX = totalPaddingLeft + textView.getScrollX();
        int scrollY = totalPaddingTop + textView.getScrollY();
        Layout layout = textView.getLayout();
        int lineForVertical = layout.getLineForVertical(scrollY);
        float f = scrollX;
        int offsetForHorizontal = layout.getOffsetForHorizontal(lineForVertical, f);
        if (f < layout.getLineLeft(lineForVertical) || f > layout.getLineRight(lineForVertical)) {
            offsetForHorizontal = -1;
        }
        ITouchableSpan[] iTouchableSpanArr = (ITouchableSpan[]) spannable.getSpans(offsetForHorizontal, offsetForHorizontal, ITouchableSpan.class);
        if (iTouchableSpanArr.length > 0) {
            return iTouchableSpanArr[0];
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent motionEvent) {
        boolean z = true;
        if (motionEvent.getAction() == 0) {
            this.mPressedSpan = getPressedSpan(textView, spannable, motionEvent);
            if (this.mPressedSpan != null) {
                this.mPressedSpan.setPressed(true);
                Selection.setSelection(spannable, spannable.getSpanStart(this.mPressedSpan), spannable.getSpanEnd(this.mPressedSpan));
            }
            if (textView instanceof ISpanTouchFix) {
                ((ISpanTouchFix) textView).setTouchSpanHit(this.mPressedSpan != null);
            }
            return this.mPressedSpan != null;
        }
        if (motionEvent.getAction() == 2) {
            ITouchableSpan pressedSpan = getPressedSpan(textView, spannable, motionEvent);
            if (this.mPressedSpan != null && pressedSpan != this.mPressedSpan) {
                this.mPressedSpan.setPressed(false);
                this.mPressedSpan = null;
                Selection.removeSelection(spannable);
            }
            if (textView instanceof ISpanTouchFix) {
                ((ISpanTouchFix) textView).setTouchSpanHit(this.mPressedSpan != null);
            }
            return this.mPressedSpan != null;
        }
        if (motionEvent.getAction() != 1) {
            if (this.mPressedSpan != null) {
                this.mPressedSpan.setPressed(false);
            }
            if (textView instanceof ISpanTouchFix) {
                ((ISpanTouchFix) textView).setTouchSpanHit(false);
            }
            Selection.removeSelection(spannable);
            return false;
        }
        if (this.mPressedSpan != null) {
            this.mPressedSpan.setPressed(false);
            this.mPressedSpan.onClick(textView);
        } else {
            z = false;
        }
        this.mPressedSpan = null;
        Selection.removeSelection(spannable);
        if (textView instanceof ISpanTouchFix) {
            ((ISpanTouchFix) textView).setTouchSpanHit(z);
        }
        return z;
    }
}
