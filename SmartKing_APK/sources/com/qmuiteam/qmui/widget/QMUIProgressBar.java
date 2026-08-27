package com.qmuiteam.qmui.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.qmuiteam.qmui.R;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

/* loaded from: classes2.dex */
public class QMUIProgressBar extends View {
    public static int DEFAULT_BACKGROUND_COLOR = -7829368;
    public static int DEFAULT_PROGRESS_COLOR = -16776961;
    public static int DEFAULT_STROKE_WIDTH = QMUIDisplayHelper.dpToPx(40);
    public static int DEFAULT_TEXT_COLOR = -16777216;
    public static int DEFAULT_TEXT_SIZE = 20;
    public static int TOTAL_DURATION = 1000;
    public static int TYPE_CIRCLE = 1;
    public static int TYPE_RECT;
    private boolean isAnimating;
    private ValueAnimator mAnimator;
    private RectF mArcOval;
    private int mBackgroundColor;
    private Paint mBackgroundPaint;
    RectF mBgRect;
    private Point mCenterPoint;
    private int mCircleRadius;
    private int mHeight;
    private int mMaxValue;
    private Paint mPaint;
    private int mProgressColor;
    RectF mProgressRect;
    QMUIProgressBarTextGenerator mQMUIProgressBarTextGenerator;
    private int mStrokeWidth;
    private String mText;
    private Paint mTextPaint;
    private int mType;
    private int mValue;
    private int mWidth;

    /* loaded from: classes2.dex */
    public interface QMUIProgressBarTextGenerator {
        String generateText(QMUIProgressBar qMUIProgressBar, int i, int i2);
    }

    public QMUIProgressBar(Context context) {
        super(context);
        this.isAnimating = false;
        this.mBackgroundPaint = new Paint();
        this.mPaint = new Paint();
        this.mTextPaint = new Paint(1);
        this.mArcOval = new RectF();
        this.mText = "";
        setup(context, null);
    }

    public QMUIProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.isAnimating = false;
        this.mBackgroundPaint = new Paint();
        this.mPaint = new Paint();
        this.mTextPaint = new Paint(1);
        this.mArcOval = new RectF();
        this.mText = "";
        setup(context, attributeSet);
    }

    public QMUIProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.isAnimating = false;
        this.mBackgroundPaint = new Paint();
        this.mPaint = new Paint();
        this.mTextPaint = new Paint(1);
        this.mArcOval = new RectF();
        this.mText = "";
        setup(context, attributeSet);
    }

    private void configPaint(int i, int i2, boolean z) {
        this.mPaint.setColor(this.mProgressColor);
        this.mBackgroundPaint.setColor(this.mBackgroundColor);
        if (this.mType == TYPE_RECT) {
            this.mPaint.setStyle(Paint.Style.FILL);
            this.mBackgroundPaint.setStyle(Paint.Style.FILL);
        } else {
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setStrokeWidth(this.mStrokeWidth);
            this.mPaint.setAntiAlias(true);
            if (z) {
                this.mPaint.setStrokeCap(Paint.Cap.ROUND);
            }
            this.mBackgroundPaint.setStyle(Paint.Style.STROKE);
            this.mBackgroundPaint.setStrokeWidth(this.mStrokeWidth);
            this.mBackgroundPaint.setAntiAlias(true);
        }
        this.mTextPaint.setColor(i);
        this.mTextPaint.setTextSize(i2);
        this.mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    private void configShape() {
        if (this.mType == TYPE_RECT) {
            this.mBgRect = new RectF(getPaddingLeft(), getPaddingTop(), this.mWidth + getPaddingLeft(), this.mHeight + getPaddingTop());
            this.mProgressRect = new RectF();
        } else {
            this.mCircleRadius = (Math.min(this.mWidth, this.mHeight) - this.mStrokeWidth) / 2;
            this.mCenterPoint = new Point(this.mWidth / 2, this.mHeight / 2);
        }
    }

    private void drawCircle(Canvas canvas) {
        canvas.drawCircle(this.mCenterPoint.x, this.mCenterPoint.y, this.mCircleRadius, this.mBackgroundPaint);
        this.mArcOval.left = this.mCenterPoint.x - this.mCircleRadius;
        this.mArcOval.right = this.mCenterPoint.x + this.mCircleRadius;
        this.mArcOval.top = this.mCenterPoint.y - this.mCircleRadius;
        this.mArcOval.bottom = this.mCenterPoint.y + this.mCircleRadius;
        canvas.drawArc(this.mArcOval, 270.0f, (this.mValue * SpatialRelationUtil.A_CIRCLE_DEGREE) / this.mMaxValue, false, this.mPaint);
        if (this.mText == null || this.mText.length() <= 0) {
            return;
        }
        Paint.FontMetricsInt fontMetricsInt = this.mTextPaint.getFontMetricsInt();
        canvas.drawText(this.mText, this.mCenterPoint.x, (this.mArcOval.top + (((this.mArcOval.height() - fontMetricsInt.bottom) + fontMetricsInt.top) / 2.0f)) - fontMetricsInt.top, this.mTextPaint);
    }

    private void drawRect(Canvas canvas) {
        canvas.drawRect(this.mBgRect, this.mBackgroundPaint);
        this.mProgressRect.set(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + parseValueToWidth(), getPaddingTop() + this.mHeight);
        canvas.drawRect(this.mProgressRect, this.mPaint);
        if (this.mText == null || this.mText.length() <= 0) {
            return;
        }
        Paint.FontMetricsInt fontMetricsInt = this.mTextPaint.getFontMetricsInt();
        canvas.drawText(this.mText, this.mBgRect.centerX(), (this.mBgRect.top + (((this.mBgRect.height() - fontMetricsInt.bottom) + fontMetricsInt.top) / 2.0f)) - fontMetricsInt.top, this.mTextPaint);
    }

    private int parseValueToWidth() {
        return (this.mWidth * this.mValue) / this.mMaxValue;
    }

    private void startAnimation(int i, int i2) {
        this.mAnimator = ValueAnimator.ofInt(i, i2);
        this.mAnimator.setDuration(Math.abs((TOTAL_DURATION * (i2 - i)) / this.mMaxValue));
        this.mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.qmuiteam.qmui.widget.QMUIProgressBar.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                QMUIProgressBar.this.mValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                QMUIProgressBar.this.invalidate();
            }
        });
        this.mAnimator.addListener(new Animator.AnimatorListener() { // from class: com.qmuiteam.qmui.widget.QMUIProgressBar.2
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                QMUIProgressBar.this.isAnimating = false;
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                QMUIProgressBar.this.isAnimating = true;
            }
        });
        this.mAnimator.start();
    }

    public int getMaxValue() {
        return this.mMaxValue;
    }

    public int getProgress() {
        return this.mValue;
    }

    public QMUIProgressBarTextGenerator getQMUIProgressBarTextGenerator() {
        return this.mQMUIProgressBarTextGenerator;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.mQMUIProgressBarTextGenerator != null) {
            this.mText = this.mQMUIProgressBarTextGenerator.generateText(this, this.mValue, this.mMaxValue);
        }
        if (this.mType == TYPE_RECT) {
            drawRect(canvas);
        } else {
            drawCircle(canvas);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.mWidth = (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
        this.mHeight = (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom();
        configShape();
        setMeasuredDimension(this.mWidth, this.mHeight);
    }

    public void setMaxValue(int i) {
        this.mMaxValue = i;
    }

    public void setProgress(int i) {
        setProgress(i, true);
    }

    public void setProgress(int i, boolean z) {
        if (i <= this.mMaxValue || i >= 0) {
            if (this.isAnimating) {
                this.isAnimating = false;
                this.mAnimator.cancel();
            }
            int i2 = this.mValue;
            this.mValue = i;
            if (z) {
                startAnimation(i2, i);
            } else {
                invalidate();
            }
        }
    }

    public void setQMUIProgressBarTextGenerator(QMUIProgressBarTextGenerator qMUIProgressBarTextGenerator) {
        this.mQMUIProgressBarTextGenerator = qMUIProgressBarTextGenerator;
    }

    public void setStrokeRoundCap(boolean z) {
        this.mPaint.setStrokeCap(z ? Paint.Cap.ROUND : Paint.Cap.BUTT);
        invalidate();
    }

    public void setTextColor(int i) {
        this.mTextPaint.setColor(i);
        invalidate();
    }

    public void setTextSize(int i) {
        this.mTextPaint.setTextSize(i);
        invalidate();
    }

    public void setup(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.QMUIProgressBar);
        this.mType = obtainStyledAttributes.getInt(R.styleable.QMUIProgressBar_qmui_type, TYPE_RECT);
        this.mProgressColor = obtainStyledAttributes.getColor(R.styleable.QMUIProgressBar_qmui_progress_color, DEFAULT_PROGRESS_COLOR);
        this.mBackgroundColor = obtainStyledAttributes.getColor(R.styleable.QMUIProgressBar_qmui_background_color, DEFAULT_BACKGROUND_COLOR);
        this.mMaxValue = obtainStyledAttributes.getInt(R.styleable.QMUIProgressBar_qmui_max_value, 100);
        this.mValue = obtainStyledAttributes.getInt(R.styleable.QMUIProgressBar_qmui_value, 0);
        boolean z = obtainStyledAttributes.getBoolean(R.styleable.QMUIProgressBar_qmui_stroke_round_cap, false);
        int i = DEFAULT_TEXT_SIZE;
        if (obtainStyledAttributes.hasValue(R.styleable.QMUIProgressBar_android_textSize)) {
            i = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUIProgressBar_android_textSize, DEFAULT_TEXT_SIZE);
        }
        int i2 = DEFAULT_TEXT_COLOR;
        if (obtainStyledAttributes.hasValue(R.styleable.QMUIProgressBar_android_textColor)) {
            i2 = obtainStyledAttributes.getColor(R.styleable.QMUIProgressBar_android_textColor, DEFAULT_TEXT_COLOR);
        }
        if (this.mType == TYPE_CIRCLE) {
            this.mStrokeWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUIProgressBar_qmui_stroke_width, DEFAULT_STROKE_WIDTH);
        }
        obtainStyledAttributes.recycle();
        configPaint(i2, i, z);
        setProgress(this.mValue);
    }
}
