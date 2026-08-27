package com.qmuiteam.qmui.qqface;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.qmuiteam.qmui.QMUILog;
import com.qmuiteam.qmui.R;
import com.qmuiteam.qmui.link.ITouchableSpan;
import com.qmuiteam.qmui.qqface.QMUIQQFaceCompiler;
import com.qmuiteam.qmui.span.QMUITouchableSpan;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUILangHelper;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* loaded from: classes2.dex */
public class QMUIQQFaceView extends View {
    private static final String TAG = "QMUIQQFaceView";
    private static final String mEllipsizeText = "...";
    private QMUIQQFaceCompiler mCompiler;
    private int mContentCalMaxWidth;
    private int mCurrentCalLine;
    private int mCurrentCalWidth;
    private int mCurrentDrawBaseLine;
    private int mCurrentDrawLine;
    private QMUITouchableSpan mCurrentDrawSpan;
    private int mCurrentDrawUsedWidth;
    private Runnable mDelayTextSetter;
    private QMUIQQFaceCompiler.ElementList mElementList;
    private TextUtils.TruncateAt mEllipsize;
    private int mEllipsizeTextLength;
    private int mFirstBaseLine;
    private int mFontHeight;
    private boolean mIncludePad;
    private boolean mIsExecutedMiddleEllipsize;
    private boolean mIsInDrawSpan;
    private boolean mIsNeedEllipsize;
    private boolean mIsSingleLine;
    private boolean mJumpHandleMeasureAndDraw;
    private int mLastCalContentWidth;
    private int mLastCalLimitWidth;
    private int mLastCalLines;
    private int mLastNeedStopLineRecord;
    private int mLineSpace;
    private int mLines;
    private QQFaceViewListener mListener;
    private int mMaxLine;
    private int mMaxWidth;
    private int mMiddleEllipsizeWidthRecord;
    private int mMoreActionColor;
    private String mMoreActionText;
    private int mMoreActionTextLength;
    private int mNeedDrawLine;
    private boolean mNeedReCalculateLines;
    private boolean mOpenQQFace;
    private CharSequence mOriginText;
    private TextPaint mPaint;
    private int mParagraphSpace;
    private PressCancelAction mPendingPressCancelAction;
    private int mQQFaceSize;
    private int mQQFaceSizeAddon;
    private Paint mSpanBgPaint;
    private Set<SpanInfo> mSpanInfos;
    private int mSpecialDrawablePadding;
    private int mTextColor;
    private int mTextSize;
    SpanInfo mTouchSpanInfo;
    private Typeface mTypeface;
    private boolean needReCalculateFontHeight;

    /* loaded from: classes2.dex */
    public static class PressCancelAction implements Runnable {
        private WeakReference<SpanInfo> mWeakReference;

        public PressCancelAction(SpanInfo spanInfo) {
            this.mWeakReference = new WeakReference<>(spanInfo);
        }

        @Override // java.lang.Runnable
        public void run() {
            SpanInfo spanInfo = this.mWeakReference.get();
            if (spanInfo != null) {
                spanInfo.setPressed(false);
                spanInfo.invalidateSpan();
            }
        }
    }

    /* loaded from: classes2.dex */
    public interface QQFaceViewListener {
        void onCalculateLinesChange(int i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class SpanInfo {
        private int mEndLine;
        private int mEndPoint;
        private int mStartLine;
        private int mStartPoint;
        private ITouchableSpan mTouchableSpan;

        public SpanInfo(ITouchableSpan iTouchableSpan) {
            this.mTouchableSpan = iTouchableSpan;
        }

        public void invalidateSpan() {
            int paddingTop = QMUIQQFaceView.this.getPaddingTop();
            if (this.mStartLine > 1) {
                paddingTop += (this.mStartLine - 1) * (QMUIQQFaceView.this.mFontHeight + QMUIQQFaceView.this.mLineSpace);
            }
            int i = ((this.mEndLine - 1) * (QMUIQQFaceView.this.mFontHeight + QMUIQQFaceView.this.mLineSpace)) + paddingTop + QMUIQQFaceView.this.mFontHeight;
            Rect rect = new Rect();
            rect.top = paddingTop;
            rect.bottom = i;
            rect.left = QMUIQQFaceView.this.getPaddingLeft();
            rect.right = QMUIQQFaceView.this.getWidth() - QMUIQQFaceView.this.getPaddingRight();
            if (this.mStartLine == this.mEndLine) {
                rect.left = this.mStartPoint;
                rect.right = this.mEndPoint;
            }
            QMUIQQFaceView.this.invalidate(rect);
        }

        public void onClick() {
            this.mTouchableSpan.onClick(QMUIQQFaceView.this);
        }

        public boolean onTouch(int i, int i2) {
            int paddingTop = QMUIQQFaceView.this.getPaddingTop();
            if (this.mStartLine > 1) {
                paddingTop += (this.mStartLine - 1) * (QMUIQQFaceView.this.mFontHeight + QMUIQQFaceView.this.mLineSpace);
            }
            int i3 = ((this.mEndLine - 1) * (QMUIQQFaceView.this.mFontHeight + QMUIQQFaceView.this.mLineSpace)) + paddingTop + QMUIQQFaceView.this.mFontHeight;
            if (i2 < paddingTop || i2 > i3) {
                return false;
            }
            if (this.mStartLine == this.mEndLine) {
                return i >= this.mStartPoint && i <= this.mEndPoint;
            }
            int i4 = paddingTop + QMUIQQFaceView.this.mFontHeight;
            int i5 = i3 - QMUIQQFaceView.this.mFontHeight;
            if (i2 <= i4 || i2 >= i5) {
                return i2 <= i4 ? i >= this.mStartPoint : i <= this.mEndPoint;
            }
            if (this.mEndLine - this.mStartLine == 1) {
                return i >= this.mStartPoint && i <= this.mEndPoint;
            }
            return true;
        }

        public void setEnd(int i, int i2) {
            this.mEndLine = i;
            this.mEndPoint = i2;
        }

        public void setPressed(boolean z) {
            this.mTouchableSpan.setPressed(z);
        }

        public void setStart(int i, int i2) {
            this.mStartLine = i;
            this.mStartPoint = i2;
        }
    }

    public QMUIQQFaceView(Context context) {
        this(context, null);
    }

    public QMUIQQFaceView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.QMUIQQFaceStyle);
    }

    public QMUIQQFaceView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mOpenQQFace = true;
        this.mLineSpace = -1;
        this.mQQFaceSize = 0;
        this.mMaxLine = Integer.MAX_VALUE;
        this.mIsSingleLine = false;
        this.mLines = 0;
        this.mSpanInfos = new HashSet();
        this.mMoreActionTextLength = 0;
        this.mEllipsizeTextLength = 0;
        this.mEllipsize = TextUtils.TruncateAt.END;
        this.mIsNeedEllipsize = false;
        this.mNeedDrawLine = 0;
        this.mQQFaceSizeAddon = 0;
        this.mMaxWidth = Integer.MAX_VALUE;
        this.mPendingPressCancelAction = null;
        this.mJumpHandleMeasureAndDraw = false;
        this.mDelayTextSetter = null;
        this.mIncludePad = true;
        this.mTypeface = null;
        this.mParagraphSpace = 0;
        this.mSpecialDrawablePadding = 0;
        this.mTouchSpanInfo = null;
        this.needReCalculateFontHeight = true;
        this.mCurrentCalWidth = 0;
        this.mCurrentCalLine = 0;
        this.mContentCalMaxWidth = 0;
        this.mNeedReCalculateLines = false;
        this.mLastCalLimitWidth = 0;
        this.mLastCalContentWidth = 0;
        this.mLastCalLines = 0;
        this.mIsInDrawSpan = false;
        this.mMiddleEllipsizeWidthRecord = -1;
        this.mIsExecutedMiddleEllipsize = false;
        this.mLastNeedStopLineRecord = -1;
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.QMUIQQFaceView, i, 0);
        this.mQQFaceSizeAddon = -QMUIDisplayHelper.dp2px(context, 2);
        this.mTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUIQQFaceView_android_textSize, QMUIDisplayHelper.dp2px(context, 14));
        this.mTextColor = obtainStyledAttributes.getColor(R.styleable.QMUIQQFaceView_android_textColor, -16777216);
        this.mIsSingleLine = obtainStyledAttributes.getBoolean(R.styleable.QMUIQQFaceView_android_singleLine, false);
        this.mMaxLine = obtainStyledAttributes.getInt(R.styleable.QMUIQQFaceView_android_maxLines, this.mMaxLine);
        setLineSpace(obtainStyledAttributes.getDimensionPixelOffset(R.styleable.QMUIQQFaceView_android_lineSpacingExtra, 0));
        switch (obtainStyledAttributes.getInt(R.styleable.QMUIQQFaceView_android_ellipsize, -1)) {
            case 1:
                this.mEllipsize = TextUtils.TruncateAt.START;
                break;
            case 2:
                this.mEllipsize = TextUtils.TruncateAt.MIDDLE;
                break;
            default:
                this.mEllipsize = TextUtils.TruncateAt.END;
                break;
        }
        this.mMaxWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUIQQFaceView_android_maxWidth, this.mMaxWidth);
        this.mSpecialDrawablePadding = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUIQQFaceView_qmui_special_drawable_padding, 0);
        final String string = obtainStyledAttributes.getString(R.styleable.QMUIQQFaceView_android_text);
        if (!QMUILangHelper.isNullOrEmpty(string)) {
            this.mDelayTextSetter = new Runnable() { // from class: com.qmuiteam.qmui.qqface.QMUIQQFaceView.1
                @Override // java.lang.Runnable
                public void run() {
                    QMUIQQFaceView.this.setText(string);
                }
            };
        }
        this.mMoreActionText = obtainStyledAttributes.getString(R.styleable.QMUIQQFaceView_qmui_more_action_text);
        this.mMoreActionColor = obtainStyledAttributes.getColor(R.styleable.QMUIQQFaceView_qmui_more_action_color, this.mTextColor);
        obtainStyledAttributes.recycle();
        this.mPaint = new TextPaint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setTextSize(this.mTextSize);
        this.mPaint.setColor(this.mTextColor);
        this.mEllipsizeTextLength = (int) Math.ceil(this.mPaint.measureText(mEllipsizeText));
        measureMoreActionTextLength();
        this.mSpanBgPaint = new Paint();
        this.mSpanBgPaint.setAntiAlias(true);
        this.mSpanBgPaint.setStyle(Paint.Style.FILL);
    }

    private void calculateFontHeight() {
        if (this.needReCalculateFontHeight) {
            Paint.FontMetricsInt fontMetricsInt = this.mPaint.getFontMetricsInt();
            if (fontMetricsInt == null) {
                this.mQQFaceSize = 0;
                this.mFontHeight = 0;
                return;
            }
            this.needReCalculateFontHeight = false;
            int fontHeightCalTop = getFontHeightCalTop(fontMetricsInt, this.mIncludePad);
            int fontHeightCalBottom = getFontHeightCalBottom(fontMetricsInt, this.mIncludePad) - fontHeightCalTop;
            this.mQQFaceSize = this.mQQFaceSizeAddon + fontHeightCalBottom;
            int max = Math.max(this.mQQFaceSize, this.mCompiler.getSpecialBoundsMaxHeight());
            if (fontHeightCalBottom >= max) {
                this.mFontHeight = fontHeightCalBottom;
                this.mFirstBaseLine = -fontHeightCalTop;
            } else {
                this.mFontHeight = max;
                this.mFirstBaseLine = (-fontHeightCalTop) + ((this.mFontHeight - max) / 2);
            }
        }
    }

    private int calculateLinesAndContentWidth(int i) {
        if (i <= getPaddingRight() + getPaddingLeft() || isElementEmpty()) {
            this.mLines = 0;
            this.mLastCalLines = 0;
            this.mLastCalContentWidth = 0;
            return this.mLastCalContentWidth;
        }
        if (!this.mNeedReCalculateLines && this.mLastCalLimitWidth == i) {
            this.mLines = this.mLastCalLines;
            return this.mLastCalContentWidth;
        }
        this.mLastCalLimitWidth = i;
        List<QMUIQQFaceCompiler.Element> elements = this.mElementList.getElements();
        this.mSpanInfos.clear();
        this.mCurrentCalLine = 1;
        this.mCurrentCalWidth = getPaddingLeft();
        calculateLinesInner(elements, i);
        if (this.mCurrentCalLine != this.mLines) {
            if (this.mListener != null) {
                this.mListener.onCalculateLinesChange(this.mCurrentCalLine);
            }
            this.mLines = this.mCurrentCalLine;
        }
        if (this.mLines == 1) {
            this.mLastCalContentWidth = this.mCurrentCalWidth + getPaddingRight();
        } else {
            this.mLastCalContentWidth = i;
        }
        this.mLastCalLines = this.mLines;
        return this.mLastCalContentWidth;
    }

    private void calculateLinesInner(List<QMUIQQFaceCompiler.Element> list, int i) {
        int paddingLeft = getPaddingLeft();
        int paddingRight = i - getPaddingRight();
        int i2 = 0;
        while (i2 < list.size() && !this.mJumpHandleMeasureAndDraw) {
            if (this.mCurrentCalLine > this.mMaxLine && this.mEllipsize == TextUtils.TruncateAt.END && Build.VERSION.SDK_INT < 21) {
                return;
            }
            QMUIQQFaceCompiler.Element element = list.get(i2);
            if (element.getType() == QMUIQQFaceCompiler.ElementType.DRAWABLE) {
                if (this.mCurrentCalWidth + this.mQQFaceSize > paddingRight) {
                    gotoCalNextLine(paddingLeft);
                    this.mCurrentCalWidth += this.mQQFaceSize;
                } else if (this.mCurrentCalWidth + this.mQQFaceSize == paddingRight) {
                    gotoCalNextLine(paddingLeft);
                } else {
                    this.mCurrentCalWidth += this.mQQFaceSize;
                }
                if (paddingRight - paddingLeft < this.mQQFaceSize) {
                    this.mJumpHandleMeasureAndDraw = true;
                }
            } else if (element.getType() == QMUIQQFaceCompiler.ElementType.TEXT) {
                measureText(element.getText(), paddingLeft, paddingRight);
            } else if (element.getType() == QMUIQQFaceCompiler.ElementType.SPAN) {
                QMUIQQFaceCompiler.ElementList childList = element.getChildList();
                QMUITouchableSpan touchableSpan = element.getTouchableSpan();
                if (childList != null && childList.getElements().size() > 0) {
                    if (touchableSpan == null) {
                        calculateLinesInner(childList.getElements(), i);
                    } else {
                        SpanInfo spanInfo = new SpanInfo(touchableSpan);
                        spanInfo.setStart(this.mCurrentCalLine, this.mCurrentCalWidth);
                        calculateLinesInner(childList.getElements(), i);
                        spanInfo.setEnd(this.mCurrentCalLine, this.mCurrentCalWidth);
                        this.mSpanInfos.add(spanInfo);
                    }
                }
            } else if (element.getType() == QMUIQQFaceCompiler.ElementType.NEXTLINE) {
                gotoCalNextLine(paddingLeft);
            } else if (element.getType() == QMUIQQFaceCompiler.ElementType.SPECIAL_BOUNDS_DRAWABLE) {
                int intrinsicWidth = element.getSpecialBoundsDrawable().getIntrinsicWidth();
                int i3 = (i2 == 0 || i2 == list.size() - 1) ? intrinsicWidth + this.mSpecialDrawablePadding : intrinsicWidth + (this.mSpecialDrawablePadding * 2);
                if (this.mCurrentCalWidth + i3 > paddingRight) {
                    gotoCalNextLine(paddingLeft);
                    this.mCurrentCalWidth += i3;
                } else if (this.mCurrentCalWidth + i3 == paddingRight) {
                    gotoCalNextLine(paddingLeft);
                } else {
                    this.mCurrentCalWidth += i3;
                }
                if (paddingRight - paddingLeft < i3) {
                    this.mJumpHandleMeasureAndDraw = true;
                }
            }
            i2++;
        }
    }

    private void calculateNeedDrawLine() {
        this.mNeedDrawLine = this.mLines;
        if (this.mIsSingleLine) {
            this.mNeedDrawLine = Math.min(1, this.mLines);
        } else if (this.mMaxLine < this.mLines) {
            this.mNeedDrawLine = this.mMaxLine;
        }
        this.mIsNeedEllipsize = this.mLines > this.mNeedDrawLine;
    }

    private void drawElements(Canvas canvas, List<QMUIQQFaceCompiler.Element> list, int i) {
        int paddingLeft = getPaddingLeft();
        int i2 = i + paddingLeft;
        if (this.mIsNeedEllipsize && this.mEllipsize == TextUtils.TruncateAt.START) {
            canvas.drawText(mEllipsizeText, 0, mEllipsizeText.length(), paddingLeft, this.mFirstBaseLine, (Paint) this.mPaint);
        }
        int i3 = 0;
        while (i3 < list.size()) {
            QMUIQQFaceCompiler.Element element = list.get(i3);
            QMUIQQFaceCompiler.ElementType type = element.getType();
            if (type == QMUIQQFaceCompiler.ElementType.DRAWABLE) {
                onDrawQQFace(canvas, element.getDrawableRes(), null, paddingLeft, i2, i3 == 0, i3 == list.size() - 1);
            } else if (type == QMUIQQFaceCompiler.ElementType.SPECIAL_BOUNDS_DRAWABLE) {
                onDrawQQFace(canvas, 0, element.getSpecialBoundsDrawable(), paddingLeft, i2, i3 == 0, i3 == list.size() - 1);
            } else if (type == QMUIQQFaceCompiler.ElementType.TEXT) {
                onDrawText(canvas, element.getText(), paddingLeft, i2);
            } else if (type == QMUIQQFaceCompiler.ElementType.SPAN) {
                QMUIQQFaceCompiler.ElementList childList = element.getChildList();
                this.mCurrentDrawSpan = element.getTouchableSpan();
                if (childList != null && !childList.getElements().isEmpty()) {
                    if (this.mCurrentDrawSpan == null) {
                        drawElements(canvas, childList.getElements(), i);
                    } else {
                        this.mIsInDrawSpan = true;
                        int pressedTextColor = this.mCurrentDrawSpan.isPressed() ? this.mCurrentDrawSpan.getPressedTextColor() : this.mCurrentDrawSpan.getNormalTextColor();
                        TextPaint textPaint = this.mPaint;
                        if (pressedTextColor == 0) {
                            pressedTextColor = this.mTextColor;
                        }
                        textPaint.setColor(pressedTextColor);
                        drawElements(canvas, childList.getElements(), i);
                        this.mPaint.setColor(this.mTextColor);
                        this.mIsInDrawSpan = false;
                    }
                }
            } else if (type == QMUIQQFaceCompiler.ElementType.NEXTLINE) {
                int i4 = this.mEllipsizeTextLength + this.mMoreActionTextLength;
                if (this.mIsNeedEllipsize && this.mEllipsize == TextUtils.TruncateAt.END && this.mCurrentDrawUsedWidth <= i2 - i4 && this.mCurrentDrawLine == this.mNeedDrawLine) {
                    drawText(canvas, mEllipsizeText, 0, mEllipsizeText.length(), this.mEllipsizeTextLength);
                    this.mCurrentDrawUsedWidth += this.mEllipsizeTextLength;
                    drawMoreActionText(canvas);
                    return;
                }
                toNewDrawLine(paddingLeft, true);
            } else {
                continue;
            }
            i3++;
        }
    }

    private void drawMoreActionText(Canvas canvas) {
        if (QMUILangHelper.isNullOrEmpty(this.mMoreActionText)) {
            return;
        }
        this.mPaint.setColor(this.mMoreActionColor);
        canvas.drawText(this.mMoreActionText, 0, this.mMoreActionText.length(), this.mCurrentDrawUsedWidth, this.mCurrentDrawBaseLine, (Paint) this.mPaint);
        this.mPaint.setColor(this.mTextColor);
    }

    private void drawQQFace(Canvas canvas, int i, Drawable drawable, int i2, boolean z, boolean z2) {
        int intrinsicWidth;
        Drawable drawable2 = i != 0 ? ContextCompat.getDrawable(getContext(), i) : drawable;
        if (i != 0) {
            intrinsicWidth = this.mQQFaceSize;
        } else {
            intrinsicWidth = drawable.getIntrinsicWidth() + ((z || z2) ? this.mSpecialDrawablePadding : this.mSpecialDrawablePadding * 2);
        }
        if (drawable2 == null) {
            return;
        }
        if (i != 0) {
            int i3 = (this.mFontHeight - this.mQQFaceSize) / 2;
            drawable2.setBounds(0, i3, this.mQQFaceSize, this.mQQFaceSize + i3);
        } else {
            int intrinsicHeight = (this.mFontHeight - drawable2.getIntrinsicHeight()) / 2;
            int i4 = z2 ? this.mSpecialDrawablePadding : 0;
            drawable2.setBounds(i4, intrinsicHeight, drawable2.getIntrinsicWidth() + i4, drawable2.getIntrinsicHeight() + intrinsicHeight);
        }
        int paddingTop = getPaddingTop();
        if (i2 > 1) {
            paddingTop += (i2 - 1) * (this.mFontHeight + this.mLineSpace);
        }
        canvas.save();
        canvas.translate(this.mCurrentDrawUsedWidth, paddingTop);
        if (this.mIsInDrawSpan && this.mCurrentDrawSpan != null) {
            int pressedBackgroundColor = this.mCurrentDrawSpan.isPressed() ? this.mCurrentDrawSpan.getPressedBackgroundColor() : this.mCurrentDrawSpan.getNormalBackgroundColor();
            if (pressedBackgroundColor != 0) {
                this.mSpanBgPaint.setColor(pressedBackgroundColor);
                canvas.drawRect(0.0f, 0.0f, intrinsicWidth, this.mFontHeight, this.mSpanBgPaint);
            }
        }
        drawable2.draw(canvas);
        canvas.restore();
    }

    private void drawText(Canvas canvas, CharSequence charSequence, int i, int i2, int i3) {
        if (this.mIsInDrawSpan && this.mCurrentDrawSpan != null) {
            int pressedBackgroundColor = this.mCurrentDrawSpan.isPressed() ? this.mCurrentDrawSpan.getPressedBackgroundColor() : this.mCurrentDrawSpan.getNormalBackgroundColor();
            if (pressedBackgroundColor != 0) {
                this.mSpanBgPaint.setColor(pressedBackgroundColor);
                canvas.drawRect(this.mCurrentDrawUsedWidth, this.mCurrentDrawBaseLine - this.mFirstBaseLine, this.mCurrentDrawUsedWidth + i3, (this.mCurrentDrawBaseLine - this.mFirstBaseLine) + this.mFontHeight, this.mSpanBgPaint);
            }
        }
        canvas.drawText(charSequence, i, i2, this.mCurrentDrawUsedWidth, this.mCurrentDrawBaseLine, this.mPaint);
    }

    private int getMiddleEllipsizeLine() {
        return this.mNeedDrawLine % 2 == 0 ? this.mNeedDrawLine / 2 : (this.mNeedDrawLine + 1) / 2;
    }

    private void gotoCalNextLine(int i) {
        this.mCurrentCalLine++;
        setContentCalMaxWidth(this.mCurrentCalWidth);
        this.mCurrentCalWidth = i;
    }

    private void handleQQFaceAfterMiddleEllipsize(Canvas canvas, int i, Drawable drawable, int i2, int i3, int i4, boolean z, boolean z2) {
        int intrinsicWidth;
        if (i != 0) {
            intrinsicWidth = this.mQQFaceSize;
        } else {
            intrinsicWidth = drawable.getIntrinsicWidth() + ((z || z2) ? this.mSpecialDrawablePadding : this.mSpecialDrawablePadding * 2);
        }
        if (this.mMiddleEllipsizeWidthRecord == -1) {
            onRealDrawQQFace(canvas, i, drawable, i4 - this.mLastNeedStopLineRecord, i2, i3, z, z2);
            return;
        }
        int i5 = this.mNeedDrawLine - i4;
        int i6 = (i3 - this.mMiddleEllipsizeWidthRecord) - this.mCurrentCalWidth;
        int i7 = i6 > 0 ? (this.mLines - i5) - 1 : this.mLines - i5;
        int dp2px = (i6 > 0 ? i3 - i6 : this.mMiddleEllipsizeWidthRecord - (i3 - this.mCurrentCalWidth)) + QMUIDisplayHelper.dp2px(getContext(), 5);
        if (this.mCurrentDrawLine < i7) {
            if (this.mCurrentDrawUsedWidth + intrinsicWidth <= i3) {
                this.mCurrentDrawUsedWidth += intrinsicWidth;
                return;
            } else {
                toNewDrawLine(i2);
                onDrawQQFace(canvas, i, drawable, i2, i3, z, z2);
                return;
            }
        }
        if (this.mCurrentDrawLine != i7) {
            onRealDrawQQFace(canvas, i, drawable, i4 - i7, i2, i3, z, z2);
        } else {
            if (this.mCurrentDrawUsedWidth + intrinsicWidth < dp2px) {
                this.mCurrentDrawUsedWidth += intrinsicWidth;
                return;
            }
            this.mCurrentDrawUsedWidth = this.mMiddleEllipsizeWidthRecord;
            this.mMiddleEllipsizeWidthRecord = -1;
            this.mLastNeedStopLineRecord = i7;
        }
    }

    private void handleTextAfterMiddleEllipsize(Canvas canvas, CharSequence charSequence, int i, int i2, int i3, int i4) {
        if (this.mMiddleEllipsizeWidthRecord == -1) {
            onRealDrawText(canvas, charSequence, i, i2);
            return;
        }
        int i5 = this.mNeedDrawLine - i3;
        int i6 = (i2 - this.mMiddleEllipsizeWidthRecord) - this.mCurrentCalWidth;
        int i7 = i6 > 0 ? (this.mLines - i5) - 1 : this.mLines - i5;
        int dp2px = (i6 > 0 ? i2 - i6 : this.mMiddleEllipsizeWidthRecord - (i2 - this.mCurrentCalWidth)) + QMUIDisplayHelper.dp2px(getContext(), 5);
        if (this.mCurrentDrawLine < i7) {
            if (i4 + this.mCurrentDrawUsedWidth <= i2) {
                this.mCurrentDrawUsedWidth += i4;
                return;
            }
            int breakText = this.mPaint.breakText(charSequence, 0, charSequence.length(), true, i2 - this.mCurrentDrawUsedWidth, null);
            toNewDrawLine(i);
            onDrawText(canvas, charSequence.subSequence(breakText, charSequence.length()), i, i2);
            return;
        }
        if (this.mCurrentDrawLine != i7) {
            onRealDrawText(canvas, charSequence, i, i2);
            return;
        }
        if (i4 + this.mCurrentDrawUsedWidth < dp2px) {
            this.mCurrentDrawUsedWidth += i4;
            return;
        }
        if (i4 + this.mCurrentDrawUsedWidth == dp2px) {
            this.mCurrentDrawUsedWidth = this.mMiddleEllipsizeWidthRecord;
            this.mMiddleEllipsizeWidthRecord = -1;
            this.mLastNeedStopLineRecord = i7;
        } else {
            int breakText2 = this.mPaint.breakText(charSequence, 0, charSequence.length(), true, dp2px - this.mCurrentDrawUsedWidth, null);
            this.mCurrentDrawUsedWidth = this.mMiddleEllipsizeWidthRecord;
            this.mMiddleEllipsizeWidthRecord = -1;
            this.mLastNeedStopLineRecord = i7;
            onRealDrawText(canvas, charSequence.subSequence(breakText2, charSequence.length()), i, i2);
        }
    }

    private boolean isElementEmpty() {
        return this.mElementList == null || this.mElementList.getElements() == null || this.mElementList.getElements().isEmpty();
    }

    private void measureMoreActionTextLength() {
        if (QMUILangHelper.isNullOrEmpty(this.mMoreActionText)) {
            this.mMoreActionTextLength = 0;
        } else {
            this.mMoreActionTextLength = (int) Math.ceil(this.mPaint.measureText(this.mMoreActionText));
        }
    }

    private void measureText(CharSequence charSequence, int i, int i2) {
        float[] fArr = new float[charSequence.length()];
        this.mPaint.getTextWidths(charSequence.toString(), fArr);
        int i3 = i2 - i;
        long currentTimeMillis = System.currentTimeMillis();
        for (int i4 = 0; i4 < fArr.length; i4++) {
            if (i3 < fArr[i4]) {
                this.mJumpHandleMeasureAndDraw = true;
                return;
            }
            if (System.currentTimeMillis() - currentTimeMillis > 2000) {
                QMUILog.d(TAG, "measureText: text = %s, mCurrentCalWidth = %d, widthStart = %d, widthEnd = %d", charSequence, Integer.valueOf(this.mCurrentCalWidth), Integer.valueOf(i), Integer.valueOf(i2));
                this.mJumpHandleMeasureAndDraw = true;
                return;
            }
            if (this.mCurrentCalWidth + fArr[i4] > i2) {
                gotoCalNextLine(i);
            }
            double d = this.mCurrentCalWidth;
            double ceil = Math.ceil(fArr[i4]);
            Double.isNaN(d);
            this.mCurrentCalWidth = (int) (d + ceil);
        }
    }

    private void onDrawQQFace(Canvas canvas, int i, Drawable drawable, int i2, int i3, boolean z, boolean z2) {
        int intrinsicWidth;
        if (i != -1) {
            intrinsicWidth = this.mQQFaceSize;
        } else {
            intrinsicWidth = drawable.getIntrinsicWidth() + ((z || z2) ? this.mSpecialDrawablePadding : this.mSpecialDrawablePadding * 2);
        }
        int i4 = intrinsicWidth;
        if (!this.mIsNeedEllipsize) {
            onRealDrawQQFace(canvas, i, drawable, 0, i2, i3, z, z2);
            return;
        }
        if (this.mEllipsize == TextUtils.TruncateAt.START) {
            if (this.mCurrentDrawLine > this.mLines - this.mNeedDrawLine) {
                onRealDrawQQFace(canvas, i, drawable, this.mNeedDrawLine - this.mLines, i2, i3, z, z2);
                return;
            }
            if (this.mCurrentDrawLine >= this.mLines - this.mNeedDrawLine) {
                if (this.mCurrentDrawUsedWidth + i4 < this.mCurrentCalWidth + this.mEllipsizeTextLength) {
                    this.mCurrentDrawUsedWidth += i4;
                    return;
                } else {
                    toNewDrawLine(this.mEllipsizeTextLength + i2);
                    return;
                }
            }
            if (this.mCurrentDrawUsedWidth + i4 <= i3) {
                this.mCurrentDrawUsedWidth += i4;
                return;
            } else {
                toNewDrawLine(i2);
                onDrawQQFace(canvas, i, drawable, i2, i3, z, z2);
                return;
            }
        }
        if (this.mEllipsize != TextUtils.TruncateAt.MIDDLE) {
            if (this.mCurrentDrawLine != this.mNeedDrawLine) {
                if (this.mCurrentDrawLine < this.mNeedDrawLine) {
                    if (this.mCurrentDrawUsedWidth + i4 > i3) {
                        onRealDrawQQFace(canvas, i, drawable, 0, i2, i3, z, z2);
                        return;
                    } else {
                        drawQQFace(canvas, i, drawable, this.mCurrentDrawLine, z, z2);
                        this.mCurrentDrawUsedWidth += i4;
                        return;
                    }
                }
                return;
            }
            int i5 = i3 - (this.mEllipsizeTextLength + this.mMoreActionTextLength);
            if (this.mCurrentDrawUsedWidth + i4 < i5) {
                drawQQFace(canvas, i, drawable, this.mCurrentDrawLine, z, z2);
                this.mCurrentDrawUsedWidth += i4;
                return;
            }
            if (this.mCurrentDrawUsedWidth + i4 == i5) {
                drawQQFace(canvas, i, drawable, this.mCurrentDrawLine, z, z2);
                this.mCurrentDrawUsedWidth += i4;
            }
            drawText(canvas, mEllipsizeText, 0, mEllipsizeText.length(), this.mEllipsizeTextLength);
            this.mCurrentDrawUsedWidth += this.mEllipsizeTextLength;
            drawMoreActionText(canvas);
            toNewDrawLine(i2);
            return;
        }
        int middleEllipsizeLine = getMiddleEllipsizeLine();
        if (this.mCurrentDrawLine < middleEllipsizeLine) {
            if (this.mCurrentDrawUsedWidth + i4 > i3) {
                onRealDrawQQFace(canvas, i, drawable, 0, i2, i3, z, z2);
                return;
            } else {
                drawQQFace(canvas, i, drawable, this.mCurrentDrawLine, z, z2);
                this.mCurrentDrawUsedWidth += i4;
                return;
            }
        }
        if (this.mCurrentDrawLine != middleEllipsizeLine) {
            handleQQFaceAfterMiddleEllipsize(canvas, i, drawable, i2, i3, middleEllipsizeLine, z, z2);
            return;
        }
        int width = (getWidth() / 2) - (this.mEllipsizeTextLength / 2);
        if (this.mIsExecutedMiddleEllipsize) {
            handleQQFaceAfterMiddleEllipsize(canvas, i, drawable, i2, i3, middleEllipsizeLine, z, z2);
            return;
        }
        if (this.mCurrentDrawUsedWidth + i4 < width) {
            drawQQFace(canvas, i, drawable, this.mCurrentDrawLine, z, z2);
            this.mCurrentDrawUsedWidth += i4;
            return;
        }
        if (this.mCurrentDrawUsedWidth + i4 != width) {
            drawText(canvas, mEllipsizeText, 0, mEllipsizeText.length(), this.mEllipsizeTextLength);
            this.mCurrentDrawUsedWidth += this.mEllipsizeTextLength;
            this.mMiddleEllipsizeWidthRecord = this.mCurrentDrawUsedWidth;
            this.mIsExecutedMiddleEllipsize = true;
            return;
        }
        drawQQFace(canvas, i, drawable, this.mCurrentDrawLine, z, z2);
        this.mCurrentDrawUsedWidth += i4;
        drawText(canvas, mEllipsizeText, 0, mEllipsizeText.length(), this.mEllipsizeTextLength);
        this.mCurrentDrawUsedWidth += this.mEllipsizeTextLength;
        this.mMiddleEllipsizeWidthRecord = this.mCurrentDrawUsedWidth;
        this.mIsExecutedMiddleEllipsize = true;
    }

    private void onDrawText(Canvas canvas, CharSequence charSequence, int i, int i2) {
        if (!this.mIsNeedEllipsize) {
            onRealDrawText(canvas, charSequence, i, i2);
            return;
        }
        if (this.mEllipsize == TextUtils.TruncateAt.START) {
            if (this.mCurrentDrawLine > this.mLines - this.mNeedDrawLine) {
                onRealDrawText(canvas, charSequence, i, i2);
                return;
            }
            if (this.mCurrentDrawLine < this.mLines - this.mNeedDrawLine) {
                int ceil = (int) Math.ceil(this.mPaint.measureText(charSequence, 0, charSequence.length()));
                if (this.mCurrentDrawUsedWidth + ceil <= i2) {
                    this.mCurrentDrawUsedWidth += ceil;
                    return;
                }
                int breakText = this.mPaint.breakText(charSequence, 0, charSequence.length(), true, i2 - this.mCurrentDrawUsedWidth, null);
                toNewDrawLine(i);
                onDrawText(canvas, charSequence.subSequence(breakText, charSequence.length()), i, i2);
                return;
            }
            int ceil2 = (int) Math.ceil(this.mPaint.measureText(charSequence, 0, charSequence.length()));
            int dp2px = this.mCurrentCalWidth + this.mEllipsizeTextLength + QMUIDisplayHelper.dp2px(getContext(), 5);
            if (this.mCurrentDrawUsedWidth + ceil2 < dp2px) {
                this.mCurrentDrawUsedWidth += ceil2;
                return;
            } else {
                if (ceil2 + this.mCurrentDrawUsedWidth == dp2px) {
                    toNewDrawLine(this.mEllipsizeTextLength + i);
                    return;
                }
                int breakText2 = this.mPaint.breakText(charSequence, 0, charSequence.length(), true, dp2px - this.mCurrentDrawUsedWidth, null);
                toNewDrawLine(this.mEllipsizeTextLength + i);
                onDrawText(canvas, charSequence.subSequence(breakText2, charSequence.length()), i, i2);
                return;
            }
        }
        if (this.mEllipsize != TextUtils.TruncateAt.MIDDLE) {
            int i3 = i;
            int ceil3 = (int) Math.ceil(this.mPaint.measureText(charSequence, 0, charSequence.length()));
            if (this.mCurrentDrawLine != this.mNeedDrawLine) {
                if (this.mCurrentDrawLine < this.mNeedDrawLine) {
                    if (ceil3 + this.mCurrentDrawUsedWidth <= i2) {
                        drawText(canvas, charSequence, 0, charSequence.length(), ceil3);
                        this.mCurrentDrawUsedWidth += ceil3;
                        return;
                    } else {
                        int breakText3 = this.mPaint.breakText(charSequence, 0, charSequence.length(), true, i2 - this.mCurrentDrawUsedWidth, null);
                        drawText(canvas, charSequence, 0, breakText3, i2 - this.mCurrentDrawUsedWidth);
                        toNewDrawLine(i3);
                        onDrawText(canvas, charSequence.subSequence(breakText3, charSequence.length()), i3, i2);
                        return;
                    }
                }
                return;
            }
            int i4 = i2 - (this.mEllipsizeTextLength + this.mMoreActionTextLength);
            if (this.mCurrentDrawUsedWidth + ceil3 < i4) {
                drawText(canvas, charSequence, 0, charSequence.length(), ceil3);
                this.mCurrentDrawUsedWidth += ceil3;
                return;
            }
            if (this.mCurrentDrawUsedWidth + ceil3 > i4) {
                i3 = i3;
                drawText(canvas, charSequence, 0, this.mPaint.breakText(charSequence, 0, charSequence.length(), true, (i2 - this.mCurrentDrawUsedWidth) - r0, null), ceil3);
                this.mCurrentDrawUsedWidth += (int) Math.ceil(this.mPaint.measureText(charSequence, 0, r8));
            } else {
                drawText(canvas, charSequence, 0, charSequence.length(), ceil3);
                this.mCurrentDrawUsedWidth += ceil3;
            }
            drawText(canvas, mEllipsizeText, 0, mEllipsizeText.length(), this.mEllipsizeTextLength);
            this.mCurrentDrawUsedWidth += this.mEllipsizeTextLength;
            drawMoreActionText(canvas);
            toNewDrawLine(i3);
            return;
        }
        int middleEllipsizeLine = getMiddleEllipsizeLine();
        int ceil4 = (int) Math.ceil(this.mPaint.measureText(charSequence, 0, charSequence.length()));
        if (this.mCurrentDrawLine < middleEllipsizeLine) {
            if (this.mCurrentDrawUsedWidth + ceil4 <= i2) {
                drawText(canvas, charSequence, 0, charSequence.length(), ceil4);
                this.mCurrentDrawUsedWidth += ceil4;
                return;
            } else {
                int breakText4 = this.mPaint.breakText(charSequence, 0, charSequence.length(), true, i2 - this.mCurrentDrawUsedWidth, null);
                drawText(canvas, charSequence, 0, breakText4, i2 - this.mCurrentDrawUsedWidth);
                toNewDrawLine(i);
                onDrawText(canvas, charSequence.subSequence(breakText4, charSequence.length()), i, i2);
                return;
            }
        }
        if (this.mCurrentDrawLine != middleEllipsizeLine) {
            handleTextAfterMiddleEllipsize(canvas, charSequence, i, i2, middleEllipsizeLine, ceil4);
            return;
        }
        int width = (getWidth() / 2) - (this.mEllipsizeTextLength / 2);
        if (this.mIsExecutedMiddleEllipsize) {
            handleTextAfterMiddleEllipsize(canvas, charSequence, i, i2, middleEllipsizeLine, ceil4);
            return;
        }
        if (this.mCurrentDrawUsedWidth + ceil4 < width) {
            drawText(canvas, charSequence, 0, charSequence.length(), ceil4);
            this.mCurrentDrawUsedWidth += ceil4;
            return;
        }
        if (this.mCurrentDrawUsedWidth + ceil4 == width) {
            drawText(canvas, charSequence, 0, charSequence.length(), ceil4);
            this.mCurrentDrawUsedWidth += ceil4;
            drawText(canvas, mEllipsizeText, 0, mEllipsizeText.length(), this.mEllipsizeTextLength);
            this.mCurrentDrawUsedWidth += this.mEllipsizeTextLength;
            this.mMiddleEllipsizeWidthRecord = this.mCurrentDrawUsedWidth;
            this.mIsExecutedMiddleEllipsize = true;
            return;
        }
        int breakText5 = this.mPaint.breakText(charSequence, 0, charSequence.length(), true, width - this.mCurrentDrawUsedWidth, null);
        int ceil5 = (int) Math.ceil(this.mPaint.measureText(charSequence, 0, breakText5));
        drawText(canvas, charSequence, 0, breakText5, ceil5);
        this.mCurrentDrawUsedWidth += ceil5;
        drawText(canvas, mEllipsizeText, 0, mEllipsizeText.length(), this.mEllipsizeTextLength);
        this.mCurrentDrawUsedWidth += this.mEllipsizeTextLength;
        this.mMiddleEllipsizeWidthRecord = this.mCurrentDrawUsedWidth;
        this.mIsExecutedMiddleEllipsize = true;
        if (breakText5 < charSequence.length()) {
            handleTextAfterMiddleEllipsize(canvas, charSequence.subSequence(breakText5, charSequence.length()), i, i2, middleEllipsizeLine, (int) Math.ceil(this.mPaint.measureText(r2, 0, r2.length())));
        }
    }

    private void onRealDrawQQFace(Canvas canvas, int i, Drawable drawable, int i2, int i3, int i4, boolean z, boolean z2) {
        int intrinsicWidth;
        if (i != 0) {
            intrinsicWidth = this.mQQFaceSize;
        } else {
            intrinsicWidth = drawable.getIntrinsicWidth() + ((z || z2) ? this.mSpecialDrawablePadding : this.mSpecialDrawablePadding * 2);
        }
        int i5 = intrinsicWidth;
        if (this.mCurrentDrawUsedWidth + i5 > i4) {
            toNewDrawLine(i3);
        }
        drawQQFace(canvas, i, drawable, this.mCurrentDrawLine + i2, z, z2);
        this.mCurrentDrawUsedWidth += i5;
    }

    private void onRealDrawText(Canvas canvas, CharSequence charSequence, int i, int i2) {
        double ceil = Math.ceil(this.mPaint.measureText(charSequence, 0, charSequence.length()));
        while (true) {
            int i3 = (int) ceil;
            if (this.mCurrentDrawUsedWidth + i3 <= i2) {
                drawText(canvas, charSequence, 0, charSequence.length(), i3);
                this.mCurrentDrawUsedWidth += i3;
                return;
            } else {
                int breakText = this.mPaint.breakText(charSequence, 0, charSequence.length(), true, i2 - this.mCurrentDrawUsedWidth, null);
                drawText(canvas, charSequence, 0, breakText, i2 - this.mCurrentDrawUsedWidth);
                toNewDrawLine(i);
                charSequence = charSequence.subSequence(breakText, charSequence.length());
                ceil = Math.ceil(this.mPaint.measureText(charSequence, 0, charSequence.length()));
            }
        }
    }

    private void setContentCalMaxWidth(int i) {
        this.mContentCalMaxWidth = Math.max(i, this.mContentCalMaxWidth);
    }

    private void toNewDrawLine(int i) {
        toNewDrawLine(i, false);
    }

    private void toNewDrawLine(int i, boolean z) {
        int i2 = (z ? this.mParagraphSpace : 0) + this.mLineSpace;
        this.mCurrentDrawLine++;
        if (!this.mIsNeedEllipsize) {
            this.mCurrentDrawBaseLine += this.mFontHeight + i2;
        } else if (this.mEllipsize == TextUtils.TruncateAt.START) {
            if (this.mCurrentDrawLine > (this.mLines - this.mNeedDrawLine) + 1) {
                this.mCurrentDrawBaseLine += this.mFontHeight + i2;
            }
        } else if (this.mEllipsize != TextUtils.TruncateAt.MIDDLE) {
            this.mCurrentDrawBaseLine += this.mFontHeight + i2;
        } else if (!this.mIsExecutedMiddleEllipsize || this.mMiddleEllipsizeWidthRecord == -1) {
            this.mCurrentDrawBaseLine += this.mFontHeight + i2;
        }
        this.mCurrentDrawUsedWidth = i;
    }

    protected int getFontHeightCalBottom(Paint.FontMetricsInt fontMetricsInt, boolean z) {
        return z ? fontMetricsInt.bottom : fontMetricsInt.descent;
    }

    protected int getFontHeightCalTop(Paint.FontMetricsInt fontMetricsInt, boolean z) {
        return z ? fontMetricsInt.top : fontMetricsInt.ascent;
    }

    public int getLineCount() {
        return this.mLines;
    }

    public int getMaxLine() {
        return this.mMaxLine;
    }

    public int getMaxWidth() {
        return this.mMaxWidth;
    }

    public TextPaint getPaint() {
        return this.mPaint;
    }

    public CharSequence getText() {
        return this.mOriginText;
    }

    public int getTextSize() {
        return this.mTextSize;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.mJumpHandleMeasureAndDraw || this.mOriginText == null || this.mLines == 0 || isElementEmpty()) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        List<QMUIQQFaceCompiler.Element> elements = this.mElementList.getElements();
        this.mCurrentDrawBaseLine = getPaddingTop() + this.mFirstBaseLine;
        this.mCurrentDrawLine = 1;
        this.mCurrentDrawUsedWidth = getPaddingLeft();
        this.mIsExecutedMiddleEllipsize = false;
        drawElements(canvas, elements, (getWidth() - getPaddingLeft()) - getPaddingRight());
        Log.i(TAG, "onDraw spend time = " + (System.currentTimeMillis() - currentTimeMillis));
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        long currentTimeMillis = System.currentTimeMillis();
        this.mJumpHandleMeasureAndDraw = false;
        calculateFontHeight();
        int mode = View.MeasureSpec.getMode(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        Log.i(TAG, "widthSize = " + size + "; heightSize = " + size2);
        this.mLines = 0;
        if (mode == 0 || mode == 1073741824) {
            calculateLinesAndContentWidth(size);
        } else {
            size = (this.mOriginText == null || this.mOriginText.length() == 0) ? 0 : calculateLinesAndContentWidth(Math.min(size, this.mMaxWidth));
        }
        if (this.mJumpHandleMeasureAndDraw) {
            if (mode2 == Integer.MIN_VALUE) {
                size2 = 0;
            }
            setMeasuredDimension(size, size2);
            return;
        }
        calculateNeedDrawLine();
        if (mode2 != 1073741824) {
            int paddingTop = getPaddingTop() + getPaddingBottom();
            size2 = this.mNeedDrawLine < 2 ? paddingTop + (this.mNeedDrawLine * this.mFontHeight) : paddingTop + ((this.mNeedDrawLine - 1) * (this.mFontHeight + this.mLineSpace)) + this.mFontHeight;
        }
        setMeasuredDimension(size, size2);
        Log.i(TAG, "mLines = " + this.mLines + " ; width = " + size + " ; height = " + size2 + "; measure time = " + (System.currentTimeMillis() - currentTimeMillis));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        if (this.mSpanInfos.isEmpty()) {
            return super.onTouchEvent(motionEvent);
        }
        int action = motionEvent.getAction();
        if (this.mTouchSpanInfo == null && action != 0) {
            return super.onTouchEvent(motionEvent);
        }
        if (this.mPendingPressCancelAction != null) {
            this.mPendingPressCancelAction.run();
            this.mPendingPressCancelAction = null;
        }
        switch (action) {
            case 0:
                this.mTouchSpanInfo = null;
                Iterator<SpanInfo> it = this.mSpanInfos.iterator();
                while (true) {
                    if (it.hasNext()) {
                        SpanInfo next = it.next();
                        if (next.onTouch(x, y)) {
                            this.mTouchSpanInfo = next;
                        }
                    }
                }
                if (this.mTouchSpanInfo == null) {
                    return super.onTouchEvent(motionEvent);
                }
                this.mTouchSpanInfo.setPressed(true);
                this.mTouchSpanInfo.invalidateSpan();
                return true;
            case 1:
                this.mTouchSpanInfo.onClick();
                this.mPendingPressCancelAction = new PressCancelAction(this.mTouchSpanInfo);
                postDelayed(new Runnable() { // from class: com.qmuiteam.qmui.qqface.QMUIQQFaceView.2
                    @Override // java.lang.Runnable
                    public void run() {
                        if (QMUIQQFaceView.this.mPendingPressCancelAction != null) {
                            QMUIQQFaceView.this.mPendingPressCancelAction.run();
                        }
                    }
                }, 100L);
                return true;
            case 2:
                if (!this.mTouchSpanInfo.onTouch(x, y)) {
                    this.mTouchSpanInfo.setPressed(false);
                    this.mTouchSpanInfo.invalidateSpan();
                    this.mTouchSpanInfo = null;
                }
                return true;
            case 3:
                this.mPendingPressCancelAction = null;
                this.mTouchSpanInfo.setPressed(false);
                this.mTouchSpanInfo.invalidateSpan();
                return true;
            default:
                return true;
        }
    }

    public void setCompiler(QMUIQQFaceCompiler qMUIQQFaceCompiler) {
        this.mCompiler = qMUIQQFaceCompiler;
        if (this.mDelayTextSetter != null) {
            this.mDelayTextSetter.run();
        }
    }

    public void setEllipsize(TextUtils.TruncateAt truncateAt) {
        if (this.mEllipsize != truncateAt) {
            this.mEllipsize = truncateAt;
            requestLayout();
            invalidate();
        }
    }

    public void setIncludeFontPadding(boolean z) {
        if (this.mIncludePad != z) {
            this.needReCalculateFontHeight = true;
            this.mIncludePad = z;
            requestLayout();
            invalidate();
        }
    }

    public void setLineSpace(int i) {
        if (this.mLineSpace != i) {
            this.mLineSpace = i;
            requestLayout();
            invalidate();
        }
    }

    public void setListener(QQFaceViewListener qQFaceViewListener) {
        this.mListener = qQFaceViewListener;
    }

    public void setMaxLine(int i) {
        if (this.mMaxLine != i) {
            this.mMaxLine = i;
            requestLayout();
            invalidate();
        }
    }

    public void setMaxWidth(int i) {
        if (this.mMaxWidth != i) {
            this.mMaxWidth = i;
            requestLayout();
        }
    }

    public void setMoreActionColor(int i) {
        if (i != this.mMoreActionColor) {
            this.mMoreActionColor = i;
            invalidate();
        }
    }

    public void setMoreActionText(String str) {
        if (this.mMoreActionText == null || !this.mMoreActionText.equals(str)) {
            this.mMoreActionText = str;
            measureMoreActionTextLength();
            requestLayout();
            invalidate();
        }
    }

    public void setOpenQQFace(boolean z) {
        this.mOpenQQFace = z;
    }

    @Override // android.view.View
    public void setPadding(int i, int i2, int i3, int i4) {
        if (getPaddingLeft() != i || getPaddingRight() != i3) {
            this.mNeedReCalculateLines = true;
        }
        super.setPadding(i, i2, i3, i4);
    }

    public void setParagraphSpace(int i) {
        if (this.mParagraphSpace != i) {
            this.mParagraphSpace = i;
            requestLayout();
            invalidate();
        }
    }

    public void setQQFaceSizeAddon(int i) {
        if (this.mQQFaceSizeAddon != i) {
            this.mQQFaceSizeAddon = i;
            this.mNeedReCalculateLines = true;
            requestLayout();
            invalidate();
        }
    }

    public void setSingleLine(boolean z) {
        if (this.mIsSingleLine != z) {
            this.mIsSingleLine = z;
            requestLayout();
            invalidate();
        }
    }

    public void setSpecialDrawablePadding(int i) {
        if (this.mSpecialDrawablePadding != i) {
            this.mSpecialDrawablePadding = i;
            requestLayout();
            invalidate();
        }
    }

    public void setText(CharSequence charSequence) {
        this.mDelayTextSetter = null;
        CharSequence charSequence2 = this.mOriginText;
        if (this.mOriginText == null || !this.mOriginText.equals(charSequence)) {
            this.mOriginText = charSequence;
            if (this.mOpenQQFace && this.mCompiler == null) {
                throw new RuntimeException("mCompiler == null");
            }
            if (QMUILangHelper.isNullOrEmpty(this.mOriginText)) {
                if (QMUILangHelper.isNullOrEmpty(charSequence2)) {
                    return;
                }
                this.mElementList = null;
                requestLayout();
                invalidate();
                return;
            }
            if (!this.mOpenQQFace || this.mCompiler == null) {
                this.mElementList = new QMUIQQFaceCompiler.ElementList(0, this.mOriginText.length());
                String[] split = this.mOriginText.toString().split("\\n");
                for (int i = 0; i < split.length; i++) {
                    this.mElementList.add(QMUIQQFaceCompiler.Element.createTextElement(split[i]));
                    if (i != split.length - 1) {
                        this.mElementList.add(QMUIQQFaceCompiler.Element.createNextLineElement());
                    }
                }
            } else {
                this.mElementList = this.mCompiler.compile(this.mOriginText);
            }
            this.mNeedReCalculateLines = true;
            int paddingLeft = getPaddingLeft() + getPaddingRight();
            if (getLayoutParams() == null) {
                return;
            }
            if (getLayoutParams().width == -2) {
                requestLayout();
                invalidate();
                return;
            }
            if (getWidth() > paddingLeft) {
                this.mLines = 0;
                calculateLinesAndContentWidth(getWidth());
                int i2 = this.mNeedDrawLine;
                calculateNeedDrawLine();
                if (i2 == this.mNeedDrawLine || getLayoutParams().height != -2) {
                    invalidate();
                } else {
                    requestLayout();
                    invalidate();
                }
            }
        }
    }

    public void setTextColor(@ColorInt int i) {
        if (this.mTextColor != i) {
            this.mTextColor = i;
            this.mPaint.setColor(i);
            invalidate();
        }
    }

    public void setTextSize(int i) {
        if (this.mTextSize != i) {
            this.mTextSize = i;
            this.mPaint.setTextSize(this.mTextSize);
            this.needReCalculateFontHeight = true;
            this.mNeedReCalculateLines = true;
            this.mEllipsizeTextLength = (int) Math.ceil(this.mPaint.measureText(mEllipsizeText));
            measureMoreActionTextLength();
            requestLayout();
            invalidate();
        }
    }

    public void setTypeface(Typeface typeface) {
        if (this.mTypeface != typeface) {
            this.mTypeface = typeface;
            this.needReCalculateFontHeight = true;
            this.mPaint.setTypeface(typeface);
            requestLayout();
            invalidate();
        }
    }

    public void setTypeface(Typeface typeface, int i) {
        if (i <= 0) {
            this.mPaint.setFakeBoldText(false);
            this.mPaint.setTextSkewX(0.0f);
            setTypeface(typeface);
        } else {
            Typeface defaultFromStyle = typeface == null ? Typeface.defaultFromStyle(i) : Typeface.create(typeface, i);
            setTypeface(defaultFromStyle);
            int style = ((defaultFromStyle != null ? defaultFromStyle.getStyle() : 0) ^ (-1)) & i;
            this.mPaint.setFakeBoldText((style & 1) != 0);
            this.mPaint.setTextSkewX((style & 2) != 0 ? -0.25f : 0.0f);
        }
    }
}
