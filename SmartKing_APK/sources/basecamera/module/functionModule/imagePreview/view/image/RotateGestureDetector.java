package basecamera.module.functionModule.imagePreview.view.image;

import android.view.MotionEvent;

/* loaded from: classes.dex */
public class RotateGestureDetector {
    private static final int MAX_DEGREES_STEP = 120;
    private float mCurrSlope;
    private OnRotateListener mListener;
    private float mPrevSlope;
    private float x1;
    private float x2;
    private float y1;
    private float y2;

    public RotateGestureDetector(OnRotateListener onRotateListener) {
        this.mListener = onRotateListener;
    }

    private float caculateSlope(MotionEvent motionEvent) {
        this.x1 = motionEvent.getX(0);
        this.y1 = motionEvent.getY(0);
        this.x2 = motionEvent.getX(1);
        this.y2 = motionEvent.getY(1);
        return (this.y2 - this.y1) / (this.x2 - this.x1);
    }

    public void onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 2) {
            switch (actionMasked) {
                case 5:
                case 6:
                    if (motionEvent.getPointerCount() == 2) {
                        this.mPrevSlope = caculateSlope(motionEvent);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
        if (motionEvent.getPointerCount() > 1) {
            this.mCurrSlope = caculateSlope(motionEvent);
            double degrees = Math.toDegrees(Math.atan(this.mCurrSlope)) - Math.toDegrees(Math.atan(this.mPrevSlope));
            if (Math.abs(degrees) <= 120.0d) {
                this.mListener.onRotate((float) degrees, (this.x2 + this.x1) / 2.0f, (this.y2 + this.y1) / 2.0f);
            }
            this.mPrevSlope = this.mCurrSlope;
        }
    }
}
