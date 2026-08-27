package basecamera.module.lib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.view.View;
import basecamera.module.lib.listener.CaptureListener;
import basecamera.module.lib.util.CameraLog;
import basecamera.module.lib.util.CheckPermission;
import com.amap.api.maps.utils.SpatialRelationUtil;

/* loaded from: classes.dex */
public class CaptureButton extends View {
    public static final int STATE_BAN = 5;
    public static final int STATE_IDLE = 1;
    public static final int STATE_LONG_PRESS = 3;
    public static final int STATE_PRESS = 2;
    public static final int STATE_RECORDERING = 4;
    private float button_inside_radius;
    private float button_outside_radius;
    private float button_radius;
    private int button_size;
    private int button_state;
    private CaptureListener captureLisenter;
    private float center_X;
    private float center_Y;
    private int duration;
    private float event_Y;
    private int inside_color;
    private int inside_reduce_size;
    private LongPressRunnable longPressRunnable;
    private Paint mPaint;
    private int min_duration;
    private int outside_add_size;
    private int outside_color;
    private float progress;
    private int progress_color;
    private int recorded_time;
    private RectF rectF;
    private int state;
    private float strokeWidth;
    private RecordCountDownTimer timer;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class LongPressRunnable implements Runnable {
        private LongPressRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            CaptureButton.this.state = 3;
            if (CheckPermission.getRecordState() != 1) {
                CaptureButton.this.state = 1;
                if (CaptureButton.this.captureLisenter != null) {
                    CaptureButton.this.captureLisenter.recordError();
                    return;
                }
            }
            CaptureButton.this.startRecordAnimation(CaptureButton.this.button_outside_radius, CaptureButton.this.button_outside_radius + CaptureButton.this.outside_add_size, CaptureButton.this.button_inside_radius, CaptureButton.this.button_inside_radius - CaptureButton.this.inside_reduce_size);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class RecordCountDownTimer extends CountDownTimer {
        RecordCountDownTimer(long j, long j2) {
            super(j, j2);
        }

        @Override // android.os.CountDownTimer
        public void onFinish() {
            CaptureButton.this.updateProgress(0L);
            CaptureButton.this.recordEnd();
        }

        @Override // android.os.CountDownTimer
        public void onTick(long j) {
            CaptureButton.this.updateProgress(j);
        }
    }

    public CaptureButton(Context context) {
        super(context);
        this.progress_color = -300503530;
        this.outside_color = -287515428;
        this.inside_color = -1;
    }

    public CaptureButton(Context context, int i) {
        super(context);
        this.progress_color = -300503530;
        this.outside_color = -287515428;
        this.inside_color = -1;
        this.button_size = i;
        this.button_radius = i / 2.0f;
        this.button_outside_radius = this.button_radius;
        this.button_inside_radius = this.button_radius * 0.75f;
        this.strokeWidth = i / 15;
        this.outside_add_size = i / 5;
        this.inside_reduce_size = i / 8;
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.progress = 0.0f;
        this.longPressRunnable = new LongPressRunnable();
        this.state = 1;
        this.button_state = JCameraView.BUTTON_STATE_BOTH;
        CameraLog.i("CaptureButtom start");
        this.duration = 10000;
        CameraLog.i("CaptureButtom end");
        this.min_duration = 1500;
        this.center_X = (this.button_size + (this.outside_add_size * 2)) / 2;
        this.center_Y = (this.button_size + (this.outside_add_size * 2)) / 2;
        this.rectF = new RectF(this.center_X - ((this.button_radius + this.outside_add_size) - (this.strokeWidth / 2.0f)), this.center_Y - ((this.button_radius + this.outside_add_size) - (this.strokeWidth / 2.0f)), this.center_X + ((this.button_radius + this.outside_add_size) - (this.strokeWidth / 2.0f)), this.center_Y + ((this.button_radius + this.outside_add_size) - (this.strokeWidth / 2.0f)));
        this.timer = new RecordCountDownTimer(this.duration, this.duration / SpatialRelationUtil.A_CIRCLE_DEGREE);
    }

    private void handlerUnpressByState() {
        removeCallbacks(this.longPressRunnable);
        int i = this.state;
        if (i != 2) {
            if (i != 4) {
                return;
            }
            this.timer.cancel();
            recordEnd();
            return;
        }
        if (this.captureLisenter == null || !(this.button_state == 257 || this.button_state == 259)) {
            this.state = 1;
        } else {
            startCaptureAnimation(this.button_inside_radius);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordEnd() {
        if (this.captureLisenter != null) {
            if (this.recorded_time < this.min_duration) {
                this.captureLisenter.recordShort(this.recorded_time);
            } else {
                this.captureLisenter.recordEnd(this.recorded_time);
            }
        }
        resetRecordAnim();
    }

    private void resetRecordAnim() {
        this.state = 5;
        this.progress = 0.0f;
        invalidate();
        startRecordAnimation(this.button_outside_radius, this.button_radius, this.button_inside_radius, this.button_radius * 0.75f);
    }

    private void startCaptureAnimation(float f) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(f, 0.75f * f, f);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: basecamera.module.lib.CaptureButton.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                CaptureButton.this.button_inside_radius = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                CaptureButton.this.invalidate();
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: basecamera.module.lib.CaptureButton.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                CaptureButton.this.captureLisenter.takePictures();
                CaptureButton.this.state = 5;
            }
        });
        ofFloat.setDuration(100L);
        ofFloat.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startRecordAnimation(float f, float f2, float f3, float f4) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(f, f2);
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(f3, f4);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: basecamera.module.lib.CaptureButton.3
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                CaptureButton.this.button_outside_radius = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                CaptureButton.this.invalidate();
            }
        });
        ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: basecamera.module.lib.CaptureButton.4
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                CaptureButton.this.button_inside_radius = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                CaptureButton.this.invalidate();
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: basecamera.module.lib.CaptureButton.5
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                if (CaptureButton.this.state == 3) {
                    if (CaptureButton.this.captureLisenter != null) {
                        CaptureButton.this.captureLisenter.recordStart();
                    }
                    CaptureButton.this.state = 4;
                    CaptureButton.this.timer.start();
                }
            }
        });
        animatorSet.playTogether(ofFloat, ofFloat2);
        animatorSet.setDuration(100L);
        animatorSet.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateProgress(long j) {
        this.recorded_time = (int) (this.duration - j);
        this.progress = 360.0f - ((((float) j) / this.duration) * 360.0f);
        invalidate();
    }

    public boolean isIdle() {
        return this.state == 1;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setColor(this.outside_color);
        canvas.drawCircle(this.center_X, this.center_Y, this.button_outside_radius, this.mPaint);
        this.mPaint.setColor(this.inside_color);
        canvas.drawCircle(this.center_X, this.center_Y, this.button_inside_radius, this.mPaint);
        if (this.state == 4) {
            this.mPaint.setColor(this.progress_color);
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setStrokeWidth(this.strokeWidth);
            canvas.drawArc(this.rectF, -90.0f, this.progress, false, this.mPaint);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        setMeasuredDimension(this.button_size + (this.outside_add_size * 2), this.button_size + (this.outside_add_size * 2));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0068, code lost:
    
        return true;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onTouchEvent(android.view.MotionEvent r6) {
        /*
            r5 = this;
            int r0 = r6.getAction()
            r1 = 259(0x103, float:3.63E-43)
            r2 = 258(0x102, float:3.62E-43)
            r3 = 1
            switch(r0) {
                case 0: goto L2f;
                case 1: goto L2b;
                case 2: goto Ld;
                default: goto Lc;
            }
        Lc:
            goto L68
        Ld:
            basecamera.module.lib.listener.CaptureListener r0 = r5.captureLisenter
            if (r0 == 0) goto L68
            int r0 = r5.state
            r4 = 4
            if (r0 != r4) goto L68
            int r0 = r5.button_state
            if (r0 == r2) goto L1e
            int r0 = r5.button_state
            if (r0 != r1) goto L68
        L1e:
            basecamera.module.lib.listener.CaptureListener r0 = r5.captureLisenter
            float r1 = r5.event_Y
            float r6 = r6.getY()
            float r1 = r1 - r6
            r0.recordZoom(r1)
            goto L68
        L2b:
            r5.handlerUnpressByState()
            goto L68
        L2f:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r4 = "state = "
            r0.append(r4)
            int r4 = r5.state
            r0.append(r4)
            java.lang.String r0 = r0.toString()
            basecamera.module.lib.util.CameraLog.i(r0)
            int r0 = r6.getPointerCount()
            if (r0 > r3) goto L68
            int r0 = r5.state
            if (r0 == r3) goto L50
            goto L68
        L50:
            float r6 = r6.getY()
            r5.event_Y = r6
            r6 = 2
            r5.state = r6
            int r6 = r5.button_state
            if (r6 == r2) goto L61
            int r6 = r5.button_state
            if (r6 != r1) goto L68
        L61:
            basecamera.module.lib.CaptureButton$LongPressRunnable r6 = r5.longPressRunnable
            r0 = 500(0x1f4, double:2.47E-321)
            r5.postDelayed(r6, r0)
        L68:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: basecamera.module.lib.CaptureButton.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public void resetState() {
        this.state = 1;
    }

    public void setButtonFeatures(int i) {
        this.button_state = i;
    }

    public void setCaptureLisenter(CaptureListener captureListener) {
        this.captureLisenter = captureListener;
    }

    public void setDuration(int i) {
        this.duration = i;
        this.timer = new RecordCountDownTimer(i, i / SpatialRelationUtil.A_CIRCLE_DEGREE);
    }

    public void setMinDuration(int i) {
        this.min_duration = i;
    }
}
