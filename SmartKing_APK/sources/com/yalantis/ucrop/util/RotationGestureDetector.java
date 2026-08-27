package com.yalantis.ucrop.util;

/* loaded from: classes2.dex */
public class RotationGestureDetector {
    private static final int INVALID_POINTER_INDEX = -1;
    private float fX;
    private float fY;
    private float mAngle;
    private boolean mIsFirstTouch;
    private OnRotationGestureListener mListener;
    private int mPointerIndex1 = -1;
    private int mPointerIndex2 = -1;
    private float sX;
    private float sY;

    /* loaded from: classes2.dex */
    public interface OnRotationGestureListener {
        boolean onRotation(RotationGestureDetector rotationGestureDetector);
    }

    /* loaded from: classes2.dex */
    public static class SimpleOnRotationGestureListener implements OnRotationGestureListener {
        @Override // com.yalantis.ucrop.util.RotationGestureDetector.OnRotationGestureListener
        public boolean onRotation(RotationGestureDetector rotationGestureDetector) {
            return false;
        }
    }

    public RotationGestureDetector(OnRotationGestureListener onRotationGestureListener) {
        this.mListener = onRotationGestureListener;
    }

    private float calculateAngleBetweenLines(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        return calculateAngleDelta((float) Math.toDegrees((float) Math.atan2(f2 - f4, f - f3)), (float) Math.toDegrees((float) Math.atan2(f6 - f8, f5 - f7)));
    }

    private float calculateAngleDelta(float f, float f2) {
        this.mAngle = (f2 % 360.0f) - (f % 360.0f);
        if (this.mAngle < -180.0f) {
            this.mAngle += 360.0f;
        } else if (this.mAngle > 180.0f) {
            this.mAngle -= 360.0f;
        }
        return this.mAngle;
    }

    public float getAngle() {
        return this.mAngle;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x00a4, code lost:
    
        return true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onTouchEvent(@android.support.annotation.NonNull android.view.MotionEvent r16) {
        /*
            r15 = this;
            r9 = r15
            r0 = r16
            int r1 = r16.getActionMasked()
            r2 = 0
            r3 = 0
            r10 = 1
            r4 = -1
            switch(r1) {
                case 0: goto L8a;
                case 1: goto L87;
                case 2: goto L34;
                case 3: goto Le;
                case 4: goto Le;
                case 5: goto L14;
                case 6: goto L10;
                default: goto Le;
            }
        Le:
            goto La4
        L10:
            r9.mPointerIndex2 = r4
            goto La4
        L14:
            float r1 = r16.getX()
            r9.fX = r1
            float r1 = r16.getY()
            r9.fY = r1
            int r1 = r16.getActionIndex()
            int r1 = r0.getPointerId(r1)
            int r0 = r0.findPointerIndex(r1)
            r9.mPointerIndex2 = r0
            r9.mAngle = r3
            r9.mIsFirstTouch = r10
            goto La4
        L34:
            int r1 = r9.mPointerIndex1
            if (r1 == r4) goto La4
            int r1 = r9.mPointerIndex2
            if (r1 == r4) goto La4
            int r1 = r16.getPointerCount()
            int r4 = r9.mPointerIndex2
            if (r1 <= r4) goto La4
            int r1 = r9.mPointerIndex1
            float r11 = r0.getX(r1)
            int r1 = r9.mPointerIndex1
            float r12 = r0.getY(r1)
            int r1 = r9.mPointerIndex2
            float r13 = r0.getX(r1)
            int r1 = r9.mPointerIndex2
            float r14 = r0.getY(r1)
            boolean r0 = r9.mIsFirstTouch
            if (r0 == 0) goto L65
            r9.mAngle = r3
            r9.mIsFirstTouch = r2
            goto L75
        L65:
            float r1 = r9.fX
            float r2 = r9.fY
            float r3 = r9.sX
            float r4 = r9.sY
            r0 = r15
            r5 = r13
            r6 = r14
            r7 = r11
            r8 = r12
            r0.calculateAngleBetweenLines(r1, r2, r3, r4, r5, r6, r7, r8)
        L75:
            com.yalantis.ucrop.util.RotationGestureDetector$OnRotationGestureListener r0 = r9.mListener
            if (r0 == 0) goto L7e
            com.yalantis.ucrop.util.RotationGestureDetector$OnRotationGestureListener r0 = r9.mListener
            r0.onRotation(r15)
        L7e:
            r9.fX = r13
            r9.fY = r14
            r9.sX = r11
            r9.sY = r12
            goto La4
        L87:
            r9.mPointerIndex1 = r4
            goto La4
        L8a:
            float r1 = r16.getX()
            r9.sX = r1
            float r1 = r16.getY()
            r9.sY = r1
            int r1 = r0.getPointerId(r2)
            int r0 = r0.findPointerIndex(r1)
            r9.mPointerIndex1 = r0
            r9.mAngle = r3
            r9.mIsFirstTouch = r10
        La4:
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yalantis.ucrop.util.RotationGestureDetector.onTouchEvent(android.view.MotionEvent):boolean");
    }
}
