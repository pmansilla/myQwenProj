package com.qmuiteam.qmui.widget.textview;

import android.content.Context;
import android.text.Spannable;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;
import com.qmuiteam.qmui.link.QMUILinkTouchMovementMethod;

/* loaded from: classes2.dex */
public class QMUISpanTouchFixTextView extends TextView implements ISpanTouchFix {
    private boolean mIsPressedRecord;
    private boolean mNeedForceEventToParent;
    private boolean mTouchSpanHit;

    public QMUISpanTouchFixTextView(Context context) {
        this(context, null);
    }

    public QMUISpanTouchFixTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public QMUISpanTouchFixTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mIsPressedRecord = false;
        this.mNeedForceEventToParent = false;
        setHighlightColor(0);
    }

    protected void onSetPressed(boolean z) {
        super.setPressed(z);
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!(getText() instanceof Spannable)) {
            return super.onTouchEvent(motionEvent);
        }
        this.mTouchSpanHit = true;
        return this.mNeedForceEventToParent ? this.mTouchSpanHit : super.onTouchEvent(motionEvent);
    }

    @Override // android.view.View
    public boolean performClick() {
        if (this.mTouchSpanHit || this.mNeedForceEventToParent) {
            return false;
        }
        return super.performClick();
    }

    @Override // android.widget.TextView, android.view.View
    public boolean performLongClick() {
        if (this.mTouchSpanHit || this.mNeedForceEventToParent) {
            return false;
        }
        return super.performLongClick();
    }

    public void setMovementMethodCompat(MovementMethod movementMethod) {
        setMovementMethod(movementMethod);
        if (this.mNeedForceEventToParent) {
            setNeedForceEventToParent(true);
        }
    }

    public void setMovementMethodDefault() {
        setMovementMethodCompat(QMUILinkTouchMovementMethod.getInstance());
    }

    public void setNeedForceEventToParent(boolean z) {
        this.mNeedForceEventToParent = z;
        setFocusable(!z);
        setClickable(!z);
        setLongClickable(!z);
    }

    @Override // android.view.View
    public final void setPressed(boolean z) {
        this.mIsPressedRecord = z;
        if (this.mTouchSpanHit) {
            return;
        }
        onSetPressed(z);
    }

    @Override // com.qmuiteam.qmui.widget.textview.ISpanTouchFix
    public void setTouchSpanHit(boolean z) {
        if (this.mTouchSpanHit != z) {
            this.mTouchSpanHit = z;
            setPressed(this.mIsPressedRecord);
        }
    }
}
