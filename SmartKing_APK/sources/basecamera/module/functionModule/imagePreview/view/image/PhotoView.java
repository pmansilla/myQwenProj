package basecamera.module.functionModule.imagePreview.view.image;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.OverScroller;
import android.widget.Scroller;

/* loaded from: classes.dex */
public class PhotoView extends ImageView {
    private static final int ANIMA_DURING = 340;
    private static final float MAX_SCALE = 2.5f;
    private static final int MIN_ROTATE = 35;
    private int MAX_ANIM_FROM_WAITE;
    private int MAX_FLING_OVER_SCROLL;
    private int MAX_OVER_RESISTANCE;
    private int MAX_OVER_SCROLL;
    private boolean canRotate;
    private boolean hasDrawable;
    private boolean hasMultiTouch;
    private boolean hasOverTranslate;
    private boolean imgLargeHeight;
    private boolean imgLargeWidth;
    private boolean isEnable;
    private boolean isInit;
    private boolean isKnowSize;
    private boolean isZoonUp;
    private boolean mAdjustViewBounds;
    private int mAnimaDuring;
    private Matrix mAnimaMatrix;
    private Matrix mBaseMatrix;
    private RectF mBaseRect;
    private View.OnClickListener mClickListener;
    private Runnable mClickRunnable;
    private RectF mClip;
    private RectF mCommonRect;
    private Runnable mCompleteCallBack;
    private float mDegrees;
    private GestureDetector mDetector;
    private PhotoInfo mFromInfo;
    private GestureDetector.OnGestureListener mGestureListener;
    private float mHalfBaseRectHeight;
    private float mHalfBaseRectWidth;
    private RectF mImgRect;
    private long mInfoTime;
    private View.OnLongClickListener mLongClick;
    private float mMaxScale;
    private int mMinRotate;
    private PointF mRotateCenter;
    private RotateGestureDetector mRotateDetector;
    private float mRotateFlag;
    private OnRotateListener mRotateListener;
    private float mScale;
    private PointF mScaleCenter;
    private ScaleGestureDetector mScaleDetector;
    private ScaleGestureDetector.OnScaleGestureListener mScaleListener;
    private ImageView.ScaleType mScaleType;
    private PointF mScreenCenter;
    private Matrix mSynthesisMatrix;
    private Matrix mTmpMatrix;
    private RectF mTmpRect;
    private Transform mTranslate;
    private int mTranslateX;
    private int mTranslateY;
    private RectF mWidgetRect;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: basecamera.module.functionModule.imagePreview.view.image.PhotoView$6, reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass6 {
        static final /* synthetic */ int[] $SwitchMap$android$widget$ImageView$ScaleType = new int[ImageView.ScaleType.values().length];

        static {
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.CENTER.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.CENTER_CROP.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.CENTER_INSIDE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_CENTER.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_START.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_END.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_XY.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    /* loaded from: classes.dex */
    public interface ClipCalculate {
        float calculateTop();
    }

    /* loaded from: classes.dex */
    public class END implements ClipCalculate {
        public END() {
        }

        @Override // basecamera.module.functionModule.imagePreview.view.image.PhotoView.ClipCalculate
        public float calculateTop() {
            return PhotoView.this.mImgRect.bottom;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class InterpolatorProxy implements Interpolator {
        private Interpolator mTarget;

        private InterpolatorProxy() {
            this.mTarget = new DecelerateInterpolator();
        }

        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float f) {
            return this.mTarget != null ? this.mTarget.getInterpolation(f) : f;
        }

        public void setTargetInterpolator(Interpolator interpolator) {
            this.mTarget = interpolator;
        }
    }

    /* loaded from: classes.dex */
    public class OTHER implements ClipCalculate {
        public OTHER() {
        }

        @Override // basecamera.module.functionModule.imagePreview.view.image.PhotoView.ClipCalculate
        public float calculateTop() {
            return (PhotoView.this.mImgRect.top + PhotoView.this.mImgRect.bottom) / 2.0f;
        }
    }

    /* loaded from: classes.dex */
    public class START implements ClipCalculate {
        public START() {
        }

        @Override // basecamera.module.functionModule.imagePreview.view.image.PhotoView.ClipCalculate
        public float calculateTop() {
            return PhotoView.this.mImgRect.top;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class Transform implements Runnable {
        ClipCalculate C;
        boolean isRuning;
        RectF mClipRect = new RectF();
        Scroller mClipScroller;
        OverScroller mFlingScroller;
        InterpolatorProxy mInterpolatorProxy;
        int mLastFlingX;
        int mLastFlingY;
        int mLastTranslateX;
        int mLastTranslateY;
        Scroller mRotateScroller;
        Scroller mScaleScroller;
        OverScroller mTranslateScroller;

        Transform() {
            this.mInterpolatorProxy = new InterpolatorProxy();
            Context context = PhotoView.this.getContext();
            this.mTranslateScroller = new OverScroller(context, this.mInterpolatorProxy);
            this.mScaleScroller = new Scroller(context, this.mInterpolatorProxy);
            this.mFlingScroller = new OverScroller(context, this.mInterpolatorProxy);
            this.mClipScroller = new Scroller(context, this.mInterpolatorProxy);
            this.mRotateScroller = new Scroller(context, this.mInterpolatorProxy);
        }

        private void applyAnima() {
            PhotoView.this.mAnimaMatrix.reset();
            PhotoView.this.mAnimaMatrix.postTranslate(-PhotoView.this.mBaseRect.left, -PhotoView.this.mBaseRect.top);
            PhotoView.this.mAnimaMatrix.postTranslate(PhotoView.this.mRotateCenter.x, PhotoView.this.mRotateCenter.y);
            PhotoView.this.mAnimaMatrix.postTranslate(-PhotoView.this.mHalfBaseRectWidth, -PhotoView.this.mHalfBaseRectHeight);
            PhotoView.this.mAnimaMatrix.postRotate(PhotoView.this.mDegrees, PhotoView.this.mRotateCenter.x, PhotoView.this.mRotateCenter.y);
            PhotoView.this.mAnimaMatrix.postScale(PhotoView.this.mScale, PhotoView.this.mScale, PhotoView.this.mScaleCenter.x, PhotoView.this.mScaleCenter.y);
            PhotoView.this.mAnimaMatrix.postTranslate(PhotoView.this.mTranslateX, PhotoView.this.mTranslateY);
            PhotoView.this.executeTranslate();
        }

        private void postExecute() {
            if (this.isRuning) {
                PhotoView.this.post(this);
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            boolean z;
            boolean z2 = true;
            boolean z3 = false;
            if (this.mScaleScroller.computeScrollOffset()) {
                PhotoView.this.mScale = this.mScaleScroller.getCurrX() / 10000.0f;
                z = false;
            } else {
                z = true;
            }
            if (this.mTranslateScroller.computeScrollOffset()) {
                int currX = this.mTranslateScroller.getCurrX() - this.mLastTranslateX;
                int currY = this.mTranslateScroller.getCurrY() - this.mLastTranslateY;
                PhotoView.this.mTranslateX += currX;
                PhotoView.this.mTranslateY += currY;
                this.mLastTranslateX = this.mTranslateScroller.getCurrX();
                this.mLastTranslateY = this.mTranslateScroller.getCurrY();
                z = false;
            }
            if (this.mFlingScroller.computeScrollOffset()) {
                int currX2 = this.mFlingScroller.getCurrX() - this.mLastFlingX;
                int currY2 = this.mFlingScroller.getCurrY() - this.mLastFlingY;
                this.mLastFlingX = this.mFlingScroller.getCurrX();
                this.mLastFlingY = this.mFlingScroller.getCurrY();
                PhotoView.this.mTranslateX += currX2;
                PhotoView.this.mTranslateY += currY2;
                z = false;
            }
            if (this.mRotateScroller.computeScrollOffset()) {
                PhotoView.this.mDegrees = this.mRotateScroller.getCurrX();
                z = false;
            }
            if (this.mClipScroller.computeScrollOffset() || PhotoView.this.mClip != null) {
                float currX3 = this.mClipScroller.getCurrX() / 10000.0f;
                float currY3 = this.mClipScroller.getCurrY() / 10000.0f;
                PhotoView.this.mTmpMatrix.setScale(currX3, currY3, (PhotoView.this.mImgRect.left + PhotoView.this.mImgRect.right) / 2.0f, this.C.calculateTop());
                PhotoView.this.mTmpMatrix.mapRect(this.mClipRect, PhotoView.this.mImgRect);
                if (currX3 == 1.0f) {
                    this.mClipRect.left = PhotoView.this.mWidgetRect.left;
                    this.mClipRect.right = PhotoView.this.mWidgetRect.right;
                }
                if (currY3 == 1.0f) {
                    this.mClipRect.top = PhotoView.this.mWidgetRect.top;
                    this.mClipRect.bottom = PhotoView.this.mWidgetRect.bottom;
                }
                PhotoView.this.mClip = this.mClipRect;
            }
            if (!z) {
                applyAnima();
                postExecute();
                return;
            }
            this.isRuning = false;
            if (PhotoView.this.imgLargeWidth) {
                if (PhotoView.this.mImgRect.left > 0.0f) {
                    PhotoView.this.mTranslateX = (int) (PhotoView.this.mTranslateX - PhotoView.this.mImgRect.left);
                } else if (PhotoView.this.mImgRect.right < PhotoView.this.mWidgetRect.width()) {
                    PhotoView.this.mTranslateX -= (int) (PhotoView.this.mWidgetRect.width() - PhotoView.this.mImgRect.right);
                }
                z3 = true;
            }
            if (!PhotoView.this.imgLargeHeight) {
                z2 = z3;
            } else if (PhotoView.this.mImgRect.top > 0.0f) {
                PhotoView.this.mTranslateY = (int) (PhotoView.this.mTranslateY - PhotoView.this.mImgRect.top);
            } else if (PhotoView.this.mImgRect.bottom < PhotoView.this.mWidgetRect.height()) {
                PhotoView.this.mTranslateY -= (int) (PhotoView.this.mWidgetRect.height() - PhotoView.this.mImgRect.bottom);
            }
            if (z2) {
                applyAnima();
            }
            PhotoView.this.invalidate();
            if (PhotoView.this.mCompleteCallBack != null) {
                PhotoView.this.mCompleteCallBack.run();
                PhotoView.this.mCompleteCallBack = null;
            }
        }

        public void setInterpolator(Interpolator interpolator) {
            this.mInterpolatorProxy.setTargetInterpolator(interpolator);
        }

        void start() {
            this.isRuning = true;
            postExecute();
        }

        void stop() {
            PhotoView.this.removeCallbacks(this);
            this.mTranslateScroller.abortAnimation();
            this.mScaleScroller.abortAnimation();
            this.mFlingScroller.abortAnimation();
            this.mRotateScroller.abortAnimation();
            this.isRuning = false;
        }

        void withClip(float f, float f2, float f3, float f4, int i, ClipCalculate clipCalculate) {
            this.mClipScroller.startScroll((int) (f * 10000.0f), (int) (f2 * 10000.0f), (int) (f3 * 10000.0f), (int) (f4 * 10000.0f), i);
            this.C = clipCalculate;
        }

        void withFling(float f, float f2) {
            int i;
            int i2;
            int i3;
            int i4;
            this.mLastFlingX = f < 0.0f ? Integer.MAX_VALUE : 0;
            int abs = (int) (f > 0.0f ? Math.abs(PhotoView.this.mImgRect.left) : PhotoView.this.mImgRect.right - PhotoView.this.mWidgetRect.right);
            if (f < 0.0f) {
                abs = Integer.MAX_VALUE - abs;
            }
            int i5 = f < 0.0f ? abs : 0;
            int i6 = f < 0.0f ? Integer.MAX_VALUE : abs;
            if (f < 0.0f) {
                abs = Integer.MAX_VALUE - i5;
            }
            this.mLastFlingY = f2 < 0.0f ? Integer.MAX_VALUE : 0;
            int abs2 = (int) (f2 > 0.0f ? Math.abs(PhotoView.this.mImgRect.top) : PhotoView.this.mImgRect.bottom - PhotoView.this.mWidgetRect.bottom);
            if (f2 < 0.0f) {
                abs2 = Integer.MAX_VALUE - abs2;
            }
            int i7 = f2 < 0.0f ? abs2 : 0;
            int i8 = f2 < 0.0f ? Integer.MAX_VALUE : abs2;
            if (f2 < 0.0f) {
                abs2 = Integer.MAX_VALUE - i7;
            }
            if (f == 0.0f) {
                i = 0;
                i2 = 0;
            } else {
                i = i5;
                i2 = i6;
            }
            if (f2 == 0.0f) {
                i3 = 0;
                i4 = 0;
            } else {
                i3 = i7;
                i4 = i8;
            }
            this.mFlingScroller.fling(this.mLastFlingX, this.mLastFlingY, (int) f, (int) f2, i, i2, i3, i4, Math.abs(abs) < PhotoView.this.MAX_FLING_OVER_SCROLL * 2 ? 0 : PhotoView.this.MAX_FLING_OVER_SCROLL, Math.abs(abs2) < PhotoView.this.MAX_FLING_OVER_SCROLL * 2 ? 0 : PhotoView.this.MAX_FLING_OVER_SCROLL);
        }

        void withRotate(int i, int i2) {
            this.mRotateScroller.startScroll(i, 0, i2 - i, 0, PhotoView.this.mAnimaDuring);
        }

        void withRotate(int i, int i2, int i3) {
            this.mRotateScroller.startScroll(i, 0, i2 - i, 0, i3);
        }

        void withScale(float f, float f2) {
            this.mScaleScroller.startScroll((int) (f * 10000.0f), 0, (int) ((f2 - f) * 10000.0f), 0, PhotoView.this.mAnimaDuring);
        }

        void withTranslate(int i, int i2, int i3, int i4) {
            this.mLastTranslateX = 0;
            this.mLastTranslateY = 0;
            this.mTranslateScroller.startScroll(0, 0, i3, i4, PhotoView.this.mAnimaDuring);
        }
    }

    public PhotoView(Context context) {
        super(context);
        this.MAX_OVER_SCROLL = 0;
        this.MAX_FLING_OVER_SCROLL = 0;
        this.MAX_OVER_RESISTANCE = 0;
        this.MAX_ANIM_FROM_WAITE = 500;
        this.mBaseMatrix = new Matrix();
        this.mAnimaMatrix = new Matrix();
        this.mSynthesisMatrix = new Matrix();
        this.mTmpMatrix = new Matrix();
        this.isEnable = false;
        this.mScale = 1.0f;
        this.mWidgetRect = new RectF();
        this.mBaseRect = new RectF();
        this.mImgRect = new RectF();
        this.mTmpRect = new RectF();
        this.mCommonRect = new RectF();
        this.mScreenCenter = new PointF();
        this.mScaleCenter = new PointF();
        this.mRotateCenter = new PointF();
        this.mTranslate = new Transform();
        this.mRotateListener = new OnRotateListener() { // from class: basecamera.module.functionModule.imagePreview.view.image.PhotoView.1
            @Override // basecamera.module.functionModule.imagePreview.view.image.OnRotateListener
            public void onRotate(float f, float f2, float f3) {
                PhotoView.this.mRotateFlag += f;
                if (PhotoView.this.canRotate) {
                    PhotoView.this.mDegrees += f;
                    PhotoView.this.mAnimaMatrix.postRotate(f, f2, f3);
                } else if (Math.abs(PhotoView.this.mRotateFlag) >= PhotoView.this.mMinRotate) {
                    PhotoView.this.canRotate = true;
                    PhotoView.this.mRotateFlag = 0.0f;
                }
            }
        };
        this.mScaleListener = new ScaleGestureDetector.OnScaleGestureListener() { // from class: basecamera.module.functionModule.imagePreview.view.image.PhotoView.2
            @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
                float scaleFactor = scaleGestureDetector.getScaleFactor();
                if (Float.isNaN(scaleFactor) || Float.isInfinite(scaleFactor)) {
                    return false;
                }
                PhotoView.this.mScale *= scaleFactor;
                if (PhotoView.this.mScale > 1.0f) {
                    PhotoView.this.isZoonUp = true;
                } else {
                    PhotoView.this.isZoonUp = false;
                }
                PhotoView.this.mAnimaMatrix.postScale(scaleFactor, scaleFactor, scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
                PhotoView.this.executeTranslate();
                return true;
            }

            @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
                return true;
            }

            @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
            public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            }
        };
        this.mClickRunnable = new Runnable() { // from class: basecamera.module.functionModule.imagePreview.view.image.PhotoView.3
            @Override // java.lang.Runnable
            public void run() {
                if (PhotoView.this.mClickListener != null) {
                    PhotoView.this.mClickListener.onClick(PhotoView.this);
                }
            }
        };
        this.mGestureListener = new GestureDetector.SimpleOnGestureListener() { // from class: basecamera.module.functionModule.imagePreview.view.image.PhotoView.4
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
            public boolean onDoubleTap(MotionEvent motionEvent) {
                float f;
                float f2;
                PhotoView.this.mTranslate.stop();
                float width = PhotoView.this.mImgRect.left + (PhotoView.this.mImgRect.width() / 2.0f);
                float height = PhotoView.this.mImgRect.top + (PhotoView.this.mImgRect.height() / 2.0f);
                PhotoView.this.mScaleCenter.set(width, height);
                PhotoView.this.mRotateCenter.set(width, height);
                PhotoView.this.mTranslateX = 0;
                PhotoView.this.mTranslateY = 0;
                if (PhotoView.this.isZoonUp) {
                    f = PhotoView.this.mScale;
                    f2 = 1.0f;
                } else {
                    float f3 = PhotoView.this.mScale;
                    float f4 = PhotoView.this.mMaxScale;
                    PhotoView.this.mScaleCenter.set(motionEvent.getX(), motionEvent.getY());
                    f = f3;
                    f2 = f4;
                }
                PhotoView.this.mTmpMatrix.reset();
                PhotoView.this.mTmpMatrix.postTranslate(-PhotoView.this.mBaseRect.left, -PhotoView.this.mBaseRect.top);
                PhotoView.this.mTmpMatrix.postTranslate(PhotoView.this.mRotateCenter.x, PhotoView.this.mRotateCenter.y);
                PhotoView.this.mTmpMatrix.postTranslate(-PhotoView.this.mHalfBaseRectWidth, -PhotoView.this.mHalfBaseRectHeight);
                PhotoView.this.mTmpMatrix.postRotate(PhotoView.this.mDegrees, PhotoView.this.mRotateCenter.x, PhotoView.this.mRotateCenter.y);
                PhotoView.this.mTmpMatrix.postScale(f2, f2, PhotoView.this.mScaleCenter.x, PhotoView.this.mScaleCenter.y);
                PhotoView.this.mTmpMatrix.postTranslate(PhotoView.this.mTranslateX, PhotoView.this.mTranslateY);
                PhotoView.this.mTmpMatrix.mapRect(PhotoView.this.mTmpRect, PhotoView.this.mBaseRect);
                PhotoView.this.doTranslateReset(PhotoView.this.mTmpRect);
                PhotoView.this.isZoonUp = !PhotoView.this.isZoonUp;
                PhotoView.this.mTranslate.withScale(f, f2);
                PhotoView.this.mTranslate.start();
                return false;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onDown(MotionEvent motionEvent) {
                PhotoView.this.hasOverTranslate = false;
                PhotoView.this.hasMultiTouch = false;
                PhotoView.this.canRotate = false;
                PhotoView.this.removeCallbacks(PhotoView.this.mClickRunnable);
                return false;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (PhotoView.this.hasMultiTouch) {
                    return false;
                }
                if ((!PhotoView.this.imgLargeWidth && !PhotoView.this.imgLargeHeight) || PhotoView.this.mTranslate.isRuning) {
                    return false;
                }
                float f3 = (((float) Math.round(PhotoView.this.mImgRect.left)) >= PhotoView.this.mWidgetRect.left || ((float) Math.round(PhotoView.this.mImgRect.right)) <= PhotoView.this.mWidgetRect.right) ? 0.0f : f;
                float f4 = (((float) Math.round(PhotoView.this.mImgRect.top)) >= PhotoView.this.mWidgetRect.top || ((float) Math.round(PhotoView.this.mImgRect.bottom)) <= PhotoView.this.mWidgetRect.bottom) ? 0.0f : f2;
                if (PhotoView.this.canRotate || PhotoView.this.mDegrees % 90.0f != 0.0f) {
                    float f5 = ((int) (PhotoView.this.mDegrees / 90.0f)) * 90;
                    float f6 = PhotoView.this.mDegrees % 90.0f;
                    if (f6 > 45.0f) {
                        f5 += 90.0f;
                    } else if (f6 < -45.0f) {
                        f5 -= 90.0f;
                    }
                    PhotoView.this.mTranslate.withRotate((int) PhotoView.this.mDegrees, (int) f5);
                    PhotoView.this.mDegrees = f5;
                }
                PhotoView.this.doTranslateReset(PhotoView.this.mImgRect);
                PhotoView.this.mTranslate.withFling(f3, f4);
                PhotoView.this.mTranslate.start();
                return super.onFling(motionEvent, motionEvent2, f, f2);
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public void onLongPress(MotionEvent motionEvent) {
                if (PhotoView.this.mLongClick != null) {
                    PhotoView.this.mLongClick.onLongClick(PhotoView.this);
                }
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (PhotoView.this.mTranslate.isRuning) {
                    PhotoView.this.mTranslate.stop();
                }
                if (PhotoView.this.canScrollHorizontallySelf(f)) {
                    if (f < 0.0f && PhotoView.this.mImgRect.left - f > PhotoView.this.mWidgetRect.left) {
                        f = PhotoView.this.mImgRect.left;
                    }
                    if (f > 0.0f && PhotoView.this.mImgRect.right - f < PhotoView.this.mWidgetRect.right) {
                        f = PhotoView.this.mImgRect.right - PhotoView.this.mWidgetRect.right;
                    }
                    PhotoView.this.mAnimaMatrix.postTranslate(-f, 0.0f);
                    PhotoView.this.mTranslateX = (int) (PhotoView.this.mTranslateX - f);
                } else if (PhotoView.this.imgLargeWidth || PhotoView.this.hasMultiTouch || PhotoView.this.hasOverTranslate) {
                    PhotoView.this.checkRect();
                    if (!PhotoView.this.hasMultiTouch) {
                        if (f < 0.0f && PhotoView.this.mImgRect.left - f > PhotoView.this.mCommonRect.left) {
                            f = PhotoView.this.resistanceScrollByX(PhotoView.this.mImgRect.left - PhotoView.this.mCommonRect.left, f);
                        }
                        if (f > 0.0f && PhotoView.this.mImgRect.right - f < PhotoView.this.mCommonRect.right) {
                            f = PhotoView.this.resistanceScrollByX(PhotoView.this.mImgRect.right - PhotoView.this.mCommonRect.right, f);
                        }
                    }
                    PhotoView.this.mTranslateX = (int) (PhotoView.this.mTranslateX - f);
                    PhotoView.this.mAnimaMatrix.postTranslate(-f, 0.0f);
                    PhotoView.this.hasOverTranslate = true;
                }
                if (PhotoView.this.canScrollVerticallySelf(f2)) {
                    if (f2 < 0.0f && PhotoView.this.mImgRect.top - f2 > PhotoView.this.mWidgetRect.top) {
                        f2 = PhotoView.this.mImgRect.top;
                    }
                    if (f2 > 0.0f && PhotoView.this.mImgRect.bottom - f2 < PhotoView.this.mWidgetRect.bottom) {
                        f2 = PhotoView.this.mImgRect.bottom - PhotoView.this.mWidgetRect.bottom;
                    }
                    PhotoView.this.mAnimaMatrix.postTranslate(0.0f, -f2);
                    PhotoView.this.mTranslateY = (int) (PhotoView.this.mTranslateY - f2);
                } else if (PhotoView.this.imgLargeHeight || PhotoView.this.hasOverTranslate || PhotoView.this.hasMultiTouch) {
                    PhotoView.this.checkRect();
                    if (!PhotoView.this.hasMultiTouch) {
                        if (f2 < 0.0f && PhotoView.this.mImgRect.top - f2 > PhotoView.this.mCommonRect.top) {
                            f2 = PhotoView.this.resistanceScrollByY(PhotoView.this.mImgRect.top - PhotoView.this.mCommonRect.top, f2);
                        }
                        if (f2 > 0.0f && PhotoView.this.mImgRect.bottom - f2 < PhotoView.this.mCommonRect.bottom) {
                            f2 = PhotoView.this.resistanceScrollByY(PhotoView.this.mImgRect.bottom - PhotoView.this.mCommonRect.bottom, f2);
                        }
                    }
                    PhotoView.this.mAnimaMatrix.postTranslate(0.0f, -f2);
                    PhotoView.this.mTranslateY = (int) (PhotoView.this.mTranslateY - f2);
                    PhotoView.this.hasOverTranslate = true;
                }
                PhotoView.this.executeTranslate();
                return true;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                PhotoView.this.postDelayed(PhotoView.this.mClickRunnable, 250L);
                return false;
            }
        };
        init();
    }

    public PhotoView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.MAX_OVER_SCROLL = 0;
        this.MAX_FLING_OVER_SCROLL = 0;
        this.MAX_OVER_RESISTANCE = 0;
        this.MAX_ANIM_FROM_WAITE = 500;
        this.mBaseMatrix = new Matrix();
        this.mAnimaMatrix = new Matrix();
        this.mSynthesisMatrix = new Matrix();
        this.mTmpMatrix = new Matrix();
        this.isEnable = false;
        this.mScale = 1.0f;
        this.mWidgetRect = new RectF();
        this.mBaseRect = new RectF();
        this.mImgRect = new RectF();
        this.mTmpRect = new RectF();
        this.mCommonRect = new RectF();
        this.mScreenCenter = new PointF();
        this.mScaleCenter = new PointF();
        this.mRotateCenter = new PointF();
        this.mTranslate = new Transform();
        this.mRotateListener = new OnRotateListener() { // from class: basecamera.module.functionModule.imagePreview.view.image.PhotoView.1
            @Override // basecamera.module.functionModule.imagePreview.view.image.OnRotateListener
            public void onRotate(float f, float f2, float f3) {
                PhotoView.this.mRotateFlag += f;
                if (PhotoView.this.canRotate) {
                    PhotoView.this.mDegrees += f;
                    PhotoView.this.mAnimaMatrix.postRotate(f, f2, f3);
                } else if (Math.abs(PhotoView.this.mRotateFlag) >= PhotoView.this.mMinRotate) {
                    PhotoView.this.canRotate = true;
                    PhotoView.this.mRotateFlag = 0.0f;
                }
            }
        };
        this.mScaleListener = new ScaleGestureDetector.OnScaleGestureListener() { // from class: basecamera.module.functionModule.imagePreview.view.image.PhotoView.2
            @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
                float scaleFactor = scaleGestureDetector.getScaleFactor();
                if (Float.isNaN(scaleFactor) || Float.isInfinite(scaleFactor)) {
                    return false;
                }
                PhotoView.this.mScale *= scaleFactor;
                if (PhotoView.this.mScale > 1.0f) {
                    PhotoView.this.isZoonUp = true;
                } else {
                    PhotoView.this.isZoonUp = false;
                }
                PhotoView.this.mAnimaMatrix.postScale(scaleFactor, scaleFactor, scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
                PhotoView.this.executeTranslate();
                return true;
            }

            @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
                return true;
            }

            @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
            public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            }
        };
        this.mClickRunnable = new Runnable() { // from class: basecamera.module.functionModule.imagePreview.view.image.PhotoView.3
            @Override // java.lang.Runnable
            public void run() {
                if (PhotoView.this.mClickListener != null) {
                    PhotoView.this.mClickListener.onClick(PhotoView.this);
                }
            }
        };
        this.mGestureListener = new GestureDetector.SimpleOnGestureListener() { // from class: basecamera.module.functionModule.imagePreview.view.image.PhotoView.4
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
            public boolean onDoubleTap(MotionEvent motionEvent) {
                float f;
                float f2;
                PhotoView.this.mTranslate.stop();
                float width = PhotoView.this.mImgRect.left + (PhotoView.this.mImgRect.width() / 2.0f);
                float height = PhotoView.this.mImgRect.top + (PhotoView.this.mImgRect.height() / 2.0f);
                PhotoView.this.mScaleCenter.set(width, height);
                PhotoView.this.mRotateCenter.set(width, height);
                PhotoView.this.mTranslateX = 0;
                PhotoView.this.mTranslateY = 0;
                if (PhotoView.this.isZoonUp) {
                    f = PhotoView.this.mScale;
                    f2 = 1.0f;
                } else {
                    float f3 = PhotoView.this.mScale;
                    float f4 = PhotoView.this.mMaxScale;
                    PhotoView.this.mScaleCenter.set(motionEvent.getX(), motionEvent.getY());
                    f = f3;
                    f2 = f4;
                }
                PhotoView.this.mTmpMatrix.reset();
                PhotoView.this.mTmpMatrix.postTranslate(-PhotoView.this.mBaseRect.left, -PhotoView.this.mBaseRect.top);
                PhotoView.this.mTmpMatrix.postTranslate(PhotoView.this.mRotateCenter.x, PhotoView.this.mRotateCenter.y);
                PhotoView.this.mTmpMatrix.postTranslate(-PhotoView.this.mHalfBaseRectWidth, -PhotoView.this.mHalfBaseRectHeight);
                PhotoView.this.mTmpMatrix.postRotate(PhotoView.this.mDegrees, PhotoView.this.mRotateCenter.x, PhotoView.this.mRotateCenter.y);
                PhotoView.this.mTmpMatrix.postScale(f2, f2, PhotoView.this.mScaleCenter.x, PhotoView.this.mScaleCenter.y);
                PhotoView.this.mTmpMatrix.postTranslate(PhotoView.this.mTranslateX, PhotoView.this.mTranslateY);
                PhotoView.this.mTmpMatrix.mapRect(PhotoView.this.mTmpRect, PhotoView.this.mBaseRect);
                PhotoView.this.doTranslateReset(PhotoView.this.mTmpRect);
                PhotoView.this.isZoonUp = !PhotoView.this.isZoonUp;
                PhotoView.this.mTranslate.withScale(f, f2);
                PhotoView.this.mTranslate.start();
                return false;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onDown(MotionEvent motionEvent) {
                PhotoView.this.hasOverTranslate = false;
                PhotoView.this.hasMultiTouch = false;
                PhotoView.this.canRotate = false;
                PhotoView.this.removeCallbacks(PhotoView.this.mClickRunnable);
                return false;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (PhotoView.this.hasMultiTouch) {
                    return false;
                }
                if ((!PhotoView.this.imgLargeWidth && !PhotoView.this.imgLargeHeight) || PhotoView.this.mTranslate.isRuning) {
                    return false;
                }
                float f3 = (((float) Math.round(PhotoView.this.mImgRect.left)) >= PhotoView.this.mWidgetRect.left || ((float) Math.round(PhotoView.this.mImgRect.right)) <= PhotoView.this.mWidgetRect.right) ? 0.0f : f;
                float f4 = (((float) Math.round(PhotoView.this.mImgRect.top)) >= PhotoView.this.mWidgetRect.top || ((float) Math.round(PhotoView.this.mImgRect.bottom)) <= PhotoView.this.mWidgetRect.bottom) ? 0.0f : f2;
                if (PhotoView.this.canRotate || PhotoView.this.mDegrees % 90.0f != 0.0f) {
                    float f5 = ((int) (PhotoView.this.mDegrees / 90.0f)) * 90;
                    float f6 = PhotoView.this.mDegrees % 90.0f;
                    if (f6 > 45.0f) {
                        f5 += 90.0f;
                    } else if (f6 < -45.0f) {
                        f5 -= 90.0f;
                    }
                    PhotoView.this.mTranslate.withRotate((int) PhotoView.this.mDegrees, (int) f5);
                    PhotoView.this.mDegrees = f5;
                }
                PhotoView.this.doTranslateReset(PhotoView.this.mImgRect);
                PhotoView.this.mTranslate.withFling(f3, f4);
                PhotoView.this.mTranslate.start();
                return super.onFling(motionEvent, motionEvent2, f, f2);
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public void onLongPress(MotionEvent motionEvent) {
                if (PhotoView.this.mLongClick != null) {
                    PhotoView.this.mLongClick.onLongClick(PhotoView.this);
                }
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (PhotoView.this.mTranslate.isRuning) {
                    PhotoView.this.mTranslate.stop();
                }
                if (PhotoView.this.canScrollHorizontallySelf(f)) {
                    if (f < 0.0f && PhotoView.this.mImgRect.left - f > PhotoView.this.mWidgetRect.left) {
                        f = PhotoView.this.mImgRect.left;
                    }
                    if (f > 0.0f && PhotoView.this.mImgRect.right - f < PhotoView.this.mWidgetRect.right) {
                        f = PhotoView.this.mImgRect.right - PhotoView.this.mWidgetRect.right;
                    }
                    PhotoView.this.mAnimaMatrix.postTranslate(-f, 0.0f);
                    PhotoView.this.mTranslateX = (int) (PhotoView.this.mTranslateX - f);
                } else if (PhotoView.this.imgLargeWidth || PhotoView.this.hasMultiTouch || PhotoView.this.hasOverTranslate) {
                    PhotoView.this.checkRect();
                    if (!PhotoView.this.hasMultiTouch) {
                        if (f < 0.0f && PhotoView.this.mImgRect.left - f > PhotoView.this.mCommonRect.left) {
                            f = PhotoView.this.resistanceScrollByX(PhotoView.this.mImgRect.left - PhotoView.this.mCommonRect.left, f);
                        }
                        if (f > 0.0f && PhotoView.this.mImgRect.right - f < PhotoView.this.mCommonRect.right) {
                            f = PhotoView.this.resistanceScrollByX(PhotoView.this.mImgRect.right - PhotoView.this.mCommonRect.right, f);
                        }
                    }
                    PhotoView.this.mTranslateX = (int) (PhotoView.this.mTranslateX - f);
                    PhotoView.this.mAnimaMatrix.postTranslate(-f, 0.0f);
                    PhotoView.this.hasOverTranslate = true;
                }
                if (PhotoView.this.canScrollVerticallySelf(f2)) {
                    if (f2 < 0.0f && PhotoView.this.mImgRect.top - f2 > PhotoView.this.mWidgetRect.top) {
                        f2 = PhotoView.this.mImgRect.top;
                    }
                    if (f2 > 0.0f && PhotoView.this.mImgRect.bottom - f2 < PhotoView.this.mWidgetRect.bottom) {
                        f2 = PhotoView.this.mImgRect.bottom - PhotoView.this.mWidgetRect.bottom;
                    }
                    PhotoView.this.mAnimaMatrix.postTranslate(0.0f, -f2);
                    PhotoView.this.mTranslateY = (int) (PhotoView.this.mTranslateY - f2);
                } else if (PhotoView.this.imgLargeHeight || PhotoView.this.hasOverTranslate || PhotoView.this.hasMultiTouch) {
                    PhotoView.this.checkRect();
                    if (!PhotoView.this.hasMultiTouch) {
                        if (f2 < 0.0f && PhotoView.this.mImgRect.top - f2 > PhotoView.this.mCommonRect.top) {
                            f2 = PhotoView.this.resistanceScrollByY(PhotoView.this.mImgRect.top - PhotoView.this.mCommonRect.top, f2);
                        }
                        if (f2 > 0.0f && PhotoView.this.mImgRect.bottom - f2 < PhotoView.this.mCommonRect.bottom) {
                            f2 = PhotoView.this.resistanceScrollByY(PhotoView.this.mImgRect.bottom - PhotoView.this.mCommonRect.bottom, f2);
                        }
                    }
                    PhotoView.this.mAnimaMatrix.postTranslate(0.0f, -f2);
                    PhotoView.this.mTranslateY = (int) (PhotoView.this.mTranslateY - f2);
                    PhotoView.this.hasOverTranslate = true;
                }
                PhotoView.this.executeTranslate();
                return true;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                PhotoView.this.postDelayed(PhotoView.this.mClickRunnable, 250L);
                return false;
            }
        };
        init();
    }

    public PhotoView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.MAX_OVER_SCROLL = 0;
        this.MAX_FLING_OVER_SCROLL = 0;
        this.MAX_OVER_RESISTANCE = 0;
        this.MAX_ANIM_FROM_WAITE = 500;
        this.mBaseMatrix = new Matrix();
        this.mAnimaMatrix = new Matrix();
        this.mSynthesisMatrix = new Matrix();
        this.mTmpMatrix = new Matrix();
        this.isEnable = false;
        this.mScale = 1.0f;
        this.mWidgetRect = new RectF();
        this.mBaseRect = new RectF();
        this.mImgRect = new RectF();
        this.mTmpRect = new RectF();
        this.mCommonRect = new RectF();
        this.mScreenCenter = new PointF();
        this.mScaleCenter = new PointF();
        this.mRotateCenter = new PointF();
        this.mTranslate = new Transform();
        this.mRotateListener = new OnRotateListener() { // from class: basecamera.module.functionModule.imagePreview.view.image.PhotoView.1
            @Override // basecamera.module.functionModule.imagePreview.view.image.OnRotateListener
            public void onRotate(float f, float f2, float f3) {
                PhotoView.this.mRotateFlag += f;
                if (PhotoView.this.canRotate) {
                    PhotoView.this.mDegrees += f;
                    PhotoView.this.mAnimaMatrix.postRotate(f, f2, f3);
                } else if (Math.abs(PhotoView.this.mRotateFlag) >= PhotoView.this.mMinRotate) {
                    PhotoView.this.canRotate = true;
                    PhotoView.this.mRotateFlag = 0.0f;
                }
            }
        };
        this.mScaleListener = new ScaleGestureDetector.OnScaleGestureListener() { // from class: basecamera.module.functionModule.imagePreview.view.image.PhotoView.2
            @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
                float scaleFactor = scaleGestureDetector.getScaleFactor();
                if (Float.isNaN(scaleFactor) || Float.isInfinite(scaleFactor)) {
                    return false;
                }
                PhotoView.this.mScale *= scaleFactor;
                if (PhotoView.this.mScale > 1.0f) {
                    PhotoView.this.isZoonUp = true;
                } else {
                    PhotoView.this.isZoonUp = false;
                }
                PhotoView.this.mAnimaMatrix.postScale(scaleFactor, scaleFactor, scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
                PhotoView.this.executeTranslate();
                return true;
            }

            @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
                return true;
            }

            @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
            public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            }
        };
        this.mClickRunnable = new Runnable() { // from class: basecamera.module.functionModule.imagePreview.view.image.PhotoView.3
            @Override // java.lang.Runnable
            public void run() {
                if (PhotoView.this.mClickListener != null) {
                    PhotoView.this.mClickListener.onClick(PhotoView.this);
                }
            }
        };
        this.mGestureListener = new GestureDetector.SimpleOnGestureListener() { // from class: basecamera.module.functionModule.imagePreview.view.image.PhotoView.4
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
            public boolean onDoubleTap(MotionEvent motionEvent) {
                float f;
                float f2;
                PhotoView.this.mTranslate.stop();
                float width = PhotoView.this.mImgRect.left + (PhotoView.this.mImgRect.width() / 2.0f);
                float height = PhotoView.this.mImgRect.top + (PhotoView.this.mImgRect.height() / 2.0f);
                PhotoView.this.mScaleCenter.set(width, height);
                PhotoView.this.mRotateCenter.set(width, height);
                PhotoView.this.mTranslateX = 0;
                PhotoView.this.mTranslateY = 0;
                if (PhotoView.this.isZoonUp) {
                    f = PhotoView.this.mScale;
                    f2 = 1.0f;
                } else {
                    float f3 = PhotoView.this.mScale;
                    float f4 = PhotoView.this.mMaxScale;
                    PhotoView.this.mScaleCenter.set(motionEvent.getX(), motionEvent.getY());
                    f = f3;
                    f2 = f4;
                }
                PhotoView.this.mTmpMatrix.reset();
                PhotoView.this.mTmpMatrix.postTranslate(-PhotoView.this.mBaseRect.left, -PhotoView.this.mBaseRect.top);
                PhotoView.this.mTmpMatrix.postTranslate(PhotoView.this.mRotateCenter.x, PhotoView.this.mRotateCenter.y);
                PhotoView.this.mTmpMatrix.postTranslate(-PhotoView.this.mHalfBaseRectWidth, -PhotoView.this.mHalfBaseRectHeight);
                PhotoView.this.mTmpMatrix.postRotate(PhotoView.this.mDegrees, PhotoView.this.mRotateCenter.x, PhotoView.this.mRotateCenter.y);
                PhotoView.this.mTmpMatrix.postScale(f2, f2, PhotoView.this.mScaleCenter.x, PhotoView.this.mScaleCenter.y);
                PhotoView.this.mTmpMatrix.postTranslate(PhotoView.this.mTranslateX, PhotoView.this.mTranslateY);
                PhotoView.this.mTmpMatrix.mapRect(PhotoView.this.mTmpRect, PhotoView.this.mBaseRect);
                PhotoView.this.doTranslateReset(PhotoView.this.mTmpRect);
                PhotoView.this.isZoonUp = !PhotoView.this.isZoonUp;
                PhotoView.this.mTranslate.withScale(f, f2);
                PhotoView.this.mTranslate.start();
                return false;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onDown(MotionEvent motionEvent) {
                PhotoView.this.hasOverTranslate = false;
                PhotoView.this.hasMultiTouch = false;
                PhotoView.this.canRotate = false;
                PhotoView.this.removeCallbacks(PhotoView.this.mClickRunnable);
                return false;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (PhotoView.this.hasMultiTouch) {
                    return false;
                }
                if ((!PhotoView.this.imgLargeWidth && !PhotoView.this.imgLargeHeight) || PhotoView.this.mTranslate.isRuning) {
                    return false;
                }
                float f3 = (((float) Math.round(PhotoView.this.mImgRect.left)) >= PhotoView.this.mWidgetRect.left || ((float) Math.round(PhotoView.this.mImgRect.right)) <= PhotoView.this.mWidgetRect.right) ? 0.0f : f;
                float f4 = (((float) Math.round(PhotoView.this.mImgRect.top)) >= PhotoView.this.mWidgetRect.top || ((float) Math.round(PhotoView.this.mImgRect.bottom)) <= PhotoView.this.mWidgetRect.bottom) ? 0.0f : f2;
                if (PhotoView.this.canRotate || PhotoView.this.mDegrees % 90.0f != 0.0f) {
                    float f5 = ((int) (PhotoView.this.mDegrees / 90.0f)) * 90;
                    float f6 = PhotoView.this.mDegrees % 90.0f;
                    if (f6 > 45.0f) {
                        f5 += 90.0f;
                    } else if (f6 < -45.0f) {
                        f5 -= 90.0f;
                    }
                    PhotoView.this.mTranslate.withRotate((int) PhotoView.this.mDegrees, (int) f5);
                    PhotoView.this.mDegrees = f5;
                }
                PhotoView.this.doTranslateReset(PhotoView.this.mImgRect);
                PhotoView.this.mTranslate.withFling(f3, f4);
                PhotoView.this.mTranslate.start();
                return super.onFling(motionEvent, motionEvent2, f, f2);
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public void onLongPress(MotionEvent motionEvent) {
                if (PhotoView.this.mLongClick != null) {
                    PhotoView.this.mLongClick.onLongClick(PhotoView.this);
                }
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (PhotoView.this.mTranslate.isRuning) {
                    PhotoView.this.mTranslate.stop();
                }
                if (PhotoView.this.canScrollHorizontallySelf(f)) {
                    if (f < 0.0f && PhotoView.this.mImgRect.left - f > PhotoView.this.mWidgetRect.left) {
                        f = PhotoView.this.mImgRect.left;
                    }
                    if (f > 0.0f && PhotoView.this.mImgRect.right - f < PhotoView.this.mWidgetRect.right) {
                        f = PhotoView.this.mImgRect.right - PhotoView.this.mWidgetRect.right;
                    }
                    PhotoView.this.mAnimaMatrix.postTranslate(-f, 0.0f);
                    PhotoView.this.mTranslateX = (int) (PhotoView.this.mTranslateX - f);
                } else if (PhotoView.this.imgLargeWidth || PhotoView.this.hasMultiTouch || PhotoView.this.hasOverTranslate) {
                    PhotoView.this.checkRect();
                    if (!PhotoView.this.hasMultiTouch) {
                        if (f < 0.0f && PhotoView.this.mImgRect.left - f > PhotoView.this.mCommonRect.left) {
                            f = PhotoView.this.resistanceScrollByX(PhotoView.this.mImgRect.left - PhotoView.this.mCommonRect.left, f);
                        }
                        if (f > 0.0f && PhotoView.this.mImgRect.right - f < PhotoView.this.mCommonRect.right) {
                            f = PhotoView.this.resistanceScrollByX(PhotoView.this.mImgRect.right - PhotoView.this.mCommonRect.right, f);
                        }
                    }
                    PhotoView.this.mTranslateX = (int) (PhotoView.this.mTranslateX - f);
                    PhotoView.this.mAnimaMatrix.postTranslate(-f, 0.0f);
                    PhotoView.this.hasOverTranslate = true;
                }
                if (PhotoView.this.canScrollVerticallySelf(f2)) {
                    if (f2 < 0.0f && PhotoView.this.mImgRect.top - f2 > PhotoView.this.mWidgetRect.top) {
                        f2 = PhotoView.this.mImgRect.top;
                    }
                    if (f2 > 0.0f && PhotoView.this.mImgRect.bottom - f2 < PhotoView.this.mWidgetRect.bottom) {
                        f2 = PhotoView.this.mImgRect.bottom - PhotoView.this.mWidgetRect.bottom;
                    }
                    PhotoView.this.mAnimaMatrix.postTranslate(0.0f, -f2);
                    PhotoView.this.mTranslateY = (int) (PhotoView.this.mTranslateY - f2);
                } else if (PhotoView.this.imgLargeHeight || PhotoView.this.hasOverTranslate || PhotoView.this.hasMultiTouch) {
                    PhotoView.this.checkRect();
                    if (!PhotoView.this.hasMultiTouch) {
                        if (f2 < 0.0f && PhotoView.this.mImgRect.top - f2 > PhotoView.this.mCommonRect.top) {
                            f2 = PhotoView.this.resistanceScrollByY(PhotoView.this.mImgRect.top - PhotoView.this.mCommonRect.top, f2);
                        }
                        if (f2 > 0.0f && PhotoView.this.mImgRect.bottom - f2 < PhotoView.this.mCommonRect.bottom) {
                            f2 = PhotoView.this.resistanceScrollByY(PhotoView.this.mImgRect.bottom - PhotoView.this.mCommonRect.bottom, f2);
                        }
                    }
                    PhotoView.this.mAnimaMatrix.postTranslate(0.0f, -f2);
                    PhotoView.this.mTranslateY = (int) (PhotoView.this.mTranslateY - f2);
                    PhotoView.this.hasOverTranslate = true;
                }
                PhotoView.this.executeTranslate();
                return true;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                PhotoView.this.postDelayed(PhotoView.this.mClickRunnable, 250L);
                return false;
            }
        };
        init();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkRect() {
        if (this.hasOverTranslate) {
            return;
        }
        mapRect(this.mWidgetRect, this.mImgRect, this.mCommonRect);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doTranslateReset(RectF rectF) {
        int i;
        int i2 = 0;
        if (rectF.width() <= this.mWidgetRect.width()) {
            if (!isImageCenterWidth(rectF)) {
                i = -((int) (((this.mWidgetRect.width() - rectF.width()) / 2.0f) - rectF.left));
            }
            i = 0;
        } else if (rectF.left > this.mWidgetRect.left) {
            i = (int) (rectF.left - this.mWidgetRect.left);
        } else {
            if (rectF.right < this.mWidgetRect.right) {
                i = (int) (rectF.right - this.mWidgetRect.right);
            }
            i = 0;
        }
        if (rectF.height() <= this.mWidgetRect.height()) {
            if (!isImageCenterHeight(rectF)) {
                i2 = -((int) (((this.mWidgetRect.height() - rectF.height()) / 2.0f) - rectF.top));
            }
        } else if (rectF.top > this.mWidgetRect.top) {
            i2 = (int) (rectF.top - this.mWidgetRect.top);
        } else if (rectF.bottom < this.mWidgetRect.bottom) {
            i2 = (int) (rectF.bottom - this.mWidgetRect.bottom);
        }
        if (i == 0 && i2 == 0) {
            return;
        }
        if (!this.mTranslate.mFlingScroller.isFinished()) {
            this.mTranslate.mFlingScroller.abortAnimation();
        }
        this.mTranslate.withTranslate(this.mTranslateX, this.mTranslateY, -i, -i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void executeTranslate() {
        this.mSynthesisMatrix.set(this.mBaseMatrix);
        this.mSynthesisMatrix.postConcat(this.mAnimaMatrix);
        setImageMatrix(this.mSynthesisMatrix);
        this.mAnimaMatrix.mapRect(this.mImgRect, this.mBaseRect);
        this.imgLargeWidth = this.mImgRect.width() > this.mWidgetRect.width();
        this.imgLargeHeight = this.mImgRect.height() > this.mWidgetRect.height();
    }

    private static int getDrawableHeight(Drawable drawable) {
        int intrinsicHeight = drawable.getIntrinsicHeight();
        if (intrinsicHeight <= 0) {
            intrinsicHeight = drawable.getMinimumHeight();
        }
        return intrinsicHeight <= 0 ? drawable.getBounds().height() : intrinsicHeight;
    }

    private static int getDrawableWidth(Drawable drawable) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        if (intrinsicWidth <= 0) {
            intrinsicWidth = drawable.getMinimumWidth();
        }
        return intrinsicWidth <= 0 ? drawable.getBounds().width() : intrinsicWidth;
    }

    public static PhotoInfo getImageViewInfo(ImageView imageView) {
        getLocation(imageView, new int[2]);
        Drawable drawable = imageView.getDrawable();
        Matrix imageMatrix = imageView.getImageMatrix();
        RectF rectF = new RectF(0.0f, 0.0f, getDrawableWidth(drawable), getDrawableHeight(drawable));
        imageMatrix.mapRect(rectF);
        RectF rectF2 = new RectF(r0[0] + rectF.left, r0[1] + rectF.top, r0[0] + rectF.right, r0[1] + rectF.bottom);
        RectF rectF3 = new RectF(0.0f, 0.0f, imageView.getWidth(), imageView.getHeight());
        return new PhotoInfo(rectF2, rectF, rectF3, new RectF(rectF3), new PointF(rectF3.width() / 2.0f, rectF3.height() / 2.0f), 1.0f, 0.0f, imageView.getScaleType());
    }

    private static void getLocation(View view, int[] iArr) {
        iArr[0] = iArr[0] + view.getLeft();
        iArr[1] = iArr[1] + view.getTop();
        Object parent = view.getParent();
        while (parent instanceof View) {
            View view2 = (View) parent;
            if (view2.getId() == 16908290) {
                return;
            }
            iArr[0] = iArr[0] - view2.getScrollX();
            iArr[1] = iArr[1] - view2.getScrollY();
            iArr[0] = iArr[0] + view2.getLeft();
            iArr[1] = iArr[1] + view2.getTop();
            parent = view2.getParent();
        }
        iArr[0] = (int) (iArr[0] + 0.5f);
        iArr[1] = (int) (iArr[1] + 0.5f);
    }

    private boolean hasSize(Drawable drawable) {
        if (drawable.getIntrinsicHeight() > 0 && drawable.getIntrinsicWidth() > 0) {
            return true;
        }
        if (drawable.getMinimumWidth() <= 0 || drawable.getMinimumHeight() <= 0) {
            return drawable.getBounds().width() > 0 && drawable.getBounds().height() > 0;
        }
        return true;
    }

    private void init() {
        super.setScaleType(ImageView.ScaleType.MATRIX);
        if (this.mScaleType == null) {
            this.mScaleType = ImageView.ScaleType.CENTER_INSIDE;
        }
        this.mRotateDetector = new RotateGestureDetector(this.mRotateListener);
        this.mDetector = new GestureDetector(getContext(), this.mGestureListener);
        this.mScaleDetector = new ScaleGestureDetector(getContext(), this.mScaleListener);
        float f = getResources().getDisplayMetrics().density;
        int i = (int) (30.0f * f);
        this.MAX_OVER_SCROLL = i;
        this.MAX_FLING_OVER_SCROLL = i;
        this.MAX_OVER_RESISTANCE = (int) (f * 140.0f);
        this.mMinRotate = 35;
        this.mAnimaDuring = ANIMA_DURING;
        this.mMaxScale = MAX_SCALE;
    }

    private void initBase() {
        if (this.hasDrawable && this.isKnowSize) {
            this.mBaseMatrix.reset();
            this.mAnimaMatrix.reset();
            this.isZoonUp = false;
            Drawable drawable = getDrawable();
            int width = getWidth();
            int height = getHeight();
            int drawableWidth = getDrawableWidth(drawable);
            int drawableHeight = getDrawableHeight(drawable);
            float f = drawableWidth;
            float f2 = drawableHeight;
            this.mBaseRect.set(0.0f, 0.0f, f, f2);
            int i = (width - drawableWidth) / 2;
            int i2 = (height - drawableHeight) / 2;
            float f3 = drawableWidth > width ? width / f : 1.0f;
            float f4 = drawableHeight > height ? height / f2 : 1.0f;
            if (f3 >= f4) {
                f3 = f4;
            }
            this.mBaseMatrix.reset();
            this.mBaseMatrix.postTranslate(i, i2);
            this.mBaseMatrix.postScale(f3, f3, this.mScreenCenter.x, this.mScreenCenter.y);
            this.mBaseMatrix.mapRect(this.mBaseRect);
            this.mHalfBaseRectWidth = this.mBaseRect.width() / 2.0f;
            this.mHalfBaseRectHeight = this.mBaseRect.height() / 2.0f;
            this.mScaleCenter.set(this.mScreenCenter);
            this.mRotateCenter.set(this.mScaleCenter);
            executeTranslate();
            switch (AnonymousClass6.$SwitchMap$android$widget$ImageView$ScaleType[this.mScaleType.ordinal()]) {
                case 1:
                    initCenter();
                    break;
                case 2:
                    initCenterCrop();
                    break;
                case 3:
                    initCenterInside();
                    break;
                case 4:
                    initFitCenter();
                    break;
                case 5:
                    initFitStart();
                    break;
                case 6:
                    initFitEnd();
                    break;
                case 7:
                    initFitXY();
                    break;
            }
            this.isInit = true;
            if (this.mFromInfo != null && System.currentTimeMillis() - this.mInfoTime < this.MAX_ANIM_FROM_WAITE) {
                animaFrom(this.mFromInfo);
            }
            this.mFromInfo = null;
            if (drawableHeight > drawableWidth * 3) {
                this.mAnimaMatrix.postTranslate(0.0f, -this.mImgRect.top);
                this.mTranslateY = (int) (this.mTranslateY - this.mImgRect.top);
                executeTranslate();
            }
        }
    }

    private void initCenter() {
        if (this.hasDrawable && this.isKnowSize) {
            Drawable drawable = getDrawable();
            int drawableWidth = getDrawableWidth(drawable);
            int drawableHeight = getDrawableHeight(drawable);
            float f = drawableWidth;
            if (f > this.mWidgetRect.width() || drawableHeight > this.mWidgetRect.height()) {
                float width = f / this.mImgRect.width();
                float height = drawableHeight / this.mImgRect.height();
                if (width > height) {
                    height = width;
                }
                this.mScale = height;
                this.mAnimaMatrix.postScale(this.mScale, this.mScale, this.mScreenCenter.x, this.mScreenCenter.y);
                executeTranslate();
                resetBase();
            }
        }
    }

    private void initCenterCrop() {
        if (this.mImgRect.width() < this.mWidgetRect.width() || this.mImgRect.height() < this.mWidgetRect.height()) {
            float width = this.mWidgetRect.width() / this.mImgRect.width();
            float height = this.mWidgetRect.height() / this.mImgRect.height();
            if (width <= height) {
                width = height;
            }
            this.mScale = width;
            this.mAnimaMatrix.postScale(this.mScale, this.mScale, this.mScreenCenter.x, this.mScreenCenter.y);
            executeTranslate();
            resetBase();
        }
    }

    private void initCenterInside() {
        if (this.mImgRect.width() > this.mWidgetRect.width() || this.mImgRect.height() > this.mWidgetRect.height()) {
            float width = this.mWidgetRect.width() / this.mImgRect.width();
            float height = this.mWidgetRect.height() / this.mImgRect.height();
            if (width >= height) {
                width = height;
            }
            this.mScale = width;
            this.mAnimaMatrix.postScale(this.mScale, this.mScale, this.mScreenCenter.x, this.mScreenCenter.y);
            executeTranslate();
            resetBase();
        }
    }

    private void initFitCenter() {
        if (this.mImgRect.width() < this.mWidgetRect.width()) {
            this.mScale = this.mWidgetRect.width() / this.mImgRect.width();
            this.mAnimaMatrix.postScale(this.mScale, this.mScale, this.mScreenCenter.x, this.mScreenCenter.y);
            executeTranslate();
            resetBase();
        }
    }

    private void initFitEnd() {
        initFitCenter();
        float f = this.mWidgetRect.bottom - this.mImgRect.bottom;
        this.mTranslateY = (int) (this.mTranslateY + f);
        this.mAnimaMatrix.postTranslate(0.0f, f);
        executeTranslate();
        resetBase();
    }

    private void initFitStart() {
        initFitCenter();
        float f = -this.mImgRect.top;
        this.mTranslateY = (int) (this.mTranslateY + f);
        this.mAnimaMatrix.postTranslate(0.0f, f);
        executeTranslate();
        resetBase();
    }

    private void initFitXY() {
        this.mAnimaMatrix.postScale(this.mWidgetRect.width() / this.mImgRect.width(), this.mWidgetRect.height() / this.mImgRect.height(), this.mScreenCenter.x, this.mScreenCenter.y);
        executeTranslate();
        resetBase();
    }

    private boolean isImageCenterHeight(RectF rectF) {
        return Math.abs(((float) Math.round(rectF.top)) - ((this.mWidgetRect.height() - rectF.height()) / 2.0f)) < 1.0f;
    }

    private boolean isImageCenterWidth(RectF rectF) {
        return Math.abs(((float) Math.round(rectF.left)) - ((this.mWidgetRect.width() - rectF.width()) / 2.0f)) < 1.0f;
    }

    private void mapRect(RectF rectF, RectF rectF2, RectF rectF3) {
        float f = rectF.left > rectF2.left ? rectF.left : rectF2.left;
        float f2 = rectF.right < rectF2.right ? rectF.right : rectF2.right;
        if (f > f2) {
            rectF3.set(0.0f, 0.0f, 0.0f, 0.0f);
            return;
        }
        float f3 = rectF.top > rectF2.top ? rectF.top : rectF2.top;
        float f4 = rectF.bottom < rectF2.bottom ? rectF.bottom : rectF2.bottom;
        if (f3 > f4) {
            rectF3.set(0.0f, 0.0f, 0.0f, 0.0f);
        } else {
            rectF3.set(f, f3, f2, f4);
        }
    }

    private void onUp() {
        if (this.mTranslate.isRuning) {
            return;
        }
        if (this.canRotate || this.mDegrees % 90.0f != 0.0f) {
            float f = ((int) (this.mDegrees / 90.0f)) * 90;
            float f2 = this.mDegrees % 90.0f;
            if (f2 > 45.0f) {
                f += 90.0f;
            } else if (f2 < -45.0f) {
                f -= 90.0f;
            }
            this.mTranslate.withRotate((int) this.mDegrees, (int) f);
            this.mDegrees = f;
        }
        float f3 = this.mScale;
        if (this.mScale < 1.0f) {
            this.mTranslate.withScale(this.mScale, 1.0f);
            f3 = 1.0f;
        } else if (this.mScale > this.mMaxScale) {
            f3 = this.mMaxScale;
            this.mTranslate.withScale(this.mScale, this.mMaxScale);
        }
        float width = this.mImgRect.left + (this.mImgRect.width() / 2.0f);
        float height = this.mImgRect.top + (this.mImgRect.height() / 2.0f);
        this.mScaleCenter.set(width, height);
        this.mRotateCenter.set(width, height);
        this.mTranslateX = 0;
        this.mTranslateY = 0;
        this.mTmpMatrix.reset();
        this.mTmpMatrix.postTranslate(-this.mBaseRect.left, -this.mBaseRect.top);
        this.mTmpMatrix.postTranslate(width - this.mHalfBaseRectWidth, height - this.mHalfBaseRectHeight);
        this.mTmpMatrix.postScale(f3, f3, width, height);
        this.mTmpMatrix.postRotate(this.mDegrees, width, height);
        this.mTmpMatrix.mapRect(this.mTmpRect, this.mBaseRect);
        doTranslateReset(this.mTmpRect);
        this.mTranslate.start();
    }

    private void resetBase() {
        Drawable drawable = getDrawable();
        this.mBaseRect.set(0.0f, 0.0f, getDrawableWidth(drawable), getDrawableHeight(drawable));
        this.mBaseMatrix.set(this.mSynthesisMatrix);
        this.mBaseMatrix.mapRect(this.mBaseRect);
        this.mHalfBaseRectWidth = this.mBaseRect.width() / 2.0f;
        this.mHalfBaseRectHeight = this.mBaseRect.height() / 2.0f;
        this.mScale = 1.0f;
        this.mTranslateX = 0;
        this.mTranslateY = 0;
        this.mAnimaMatrix.reset();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float resistanceScrollByX(float f, float f2) {
        return f2 * (Math.abs(Math.abs(f) - this.MAX_OVER_RESISTANCE) / this.MAX_OVER_RESISTANCE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float resistanceScrollByY(float f, float f2) {
        return f2 * (Math.abs(Math.abs(f) - this.MAX_OVER_RESISTANCE) / this.MAX_OVER_RESISTANCE);
    }

    public void animaFrom(PhotoInfo photoInfo) {
        if (!this.isInit) {
            this.mFromInfo = photoInfo;
            this.mInfoTime = System.currentTimeMillis();
            return;
        }
        reset();
        PhotoInfo info = getInfo();
        float width = photoInfo.mImgRect.width() / info.mImgRect.width();
        float height = photoInfo.mImgRect.height() / info.mImgRect.height();
        if (width < height) {
            height = width;
        }
        float width2 = photoInfo.mRect.left + (photoInfo.mRect.width() / 2.0f);
        float height2 = photoInfo.mRect.top + (photoInfo.mRect.height() / 2.0f);
        this.mAnimaMatrix.reset();
        this.mAnimaMatrix.postTranslate(-this.mBaseRect.left, -this.mBaseRect.top);
        this.mAnimaMatrix.postTranslate(width2 - (this.mBaseRect.width() / 2.0f), height2 - (this.mBaseRect.height() / 2.0f));
        this.mAnimaMatrix.postScale(height, height, width2, height2);
        this.mAnimaMatrix.postRotate(photoInfo.mDegrees, width2, height2);
        executeTranslate();
        this.mScaleCenter.set(width2, height2);
        this.mRotateCenter.set(width2, height2);
        this.mTranslate.withTranslate(0, 0, (int) (this.mScreenCenter.x - width2), (int) (this.mScreenCenter.y - height2));
        this.mTranslate.withScale(height, 1.0f);
        this.mTranslate.withRotate((int) photoInfo.mDegrees, 0);
        if (photoInfo.mWidgetRect.width() < photoInfo.mImgRect.width() || photoInfo.mWidgetRect.height() < photoInfo.mImgRect.height()) {
            float width3 = photoInfo.mWidgetRect.width() / photoInfo.mImgRect.width();
            float height3 = photoInfo.mWidgetRect.height() / photoInfo.mImgRect.height();
            if (width3 > 1.0f) {
                width3 = 1.0f;
            }
            if (height3 > 1.0f) {
                height3 = 1.0f;
            }
            ClipCalculate start = photoInfo.mScaleType == ImageView.ScaleType.FIT_START ? new START() : photoInfo.mScaleType == ImageView.ScaleType.FIT_END ? new END() : new OTHER();
            this.mTranslate.withClip(width3, height3, 1.0f - width3, 1.0f - height3, this.mAnimaDuring / 3, start);
            this.mTmpMatrix.setScale(width3, height3, (this.mImgRect.left + this.mImgRect.right) / 2.0f, start.calculateTop());
            this.mTmpMatrix.mapRect(this.mTranslate.mClipRect, this.mImgRect);
            this.mClip = this.mTranslate.mClipRect;
        }
        this.mTranslate.start();
    }

    public void animaTo(PhotoInfo photoInfo, Runnable runnable) {
        if (this.isInit) {
            this.mTranslate.stop();
            this.mTranslateX = 0;
            this.mTranslateY = 0;
            float width = photoInfo.mRect.left + (photoInfo.mRect.width() / 2.0f);
            float height = photoInfo.mRect.top + (photoInfo.mRect.height() / 2.0f);
            this.mScaleCenter.set(this.mImgRect.left + (this.mImgRect.width() / 2.0f), this.mImgRect.top + (this.mImgRect.height() / 2.0f));
            this.mRotateCenter.set(this.mScaleCenter);
            this.mAnimaMatrix.postRotate(-this.mDegrees, this.mScaleCenter.x, this.mScaleCenter.y);
            this.mAnimaMatrix.mapRect(this.mImgRect, this.mBaseRect);
            float width2 = photoInfo.mImgRect.width() / this.mBaseRect.width();
            float height2 = photoInfo.mImgRect.height() / this.mBaseRect.height();
            if (width2 <= height2) {
                width2 = height2;
            }
            this.mAnimaMatrix.postRotate(this.mDegrees, this.mScaleCenter.x, this.mScaleCenter.y);
            this.mAnimaMatrix.mapRect(this.mImgRect, this.mBaseRect);
            this.mDegrees %= 360.0f;
            this.mTranslate.withTranslate(0, 0, (int) (width - this.mScaleCenter.x), (int) (height - this.mScaleCenter.y));
            this.mTranslate.withScale(this.mScale, width2);
            this.mTranslate.withRotate((int) this.mDegrees, (int) photoInfo.mDegrees, (this.mAnimaDuring * 2) / 3);
            if (photoInfo.mWidgetRect.width() < photoInfo.mRect.width() || photoInfo.mWidgetRect.height() < photoInfo.mRect.height()) {
                final float width3 = photoInfo.mWidgetRect.width() / photoInfo.mRect.width();
                final float height3 = photoInfo.mWidgetRect.height() / photoInfo.mRect.height();
                if (width3 > 1.0f) {
                    width3 = 1.0f;
                }
                if (height3 > 1.0f) {
                    height3 = 1.0f;
                }
                final ClipCalculate start = photoInfo.mScaleType == ImageView.ScaleType.FIT_START ? new START() : photoInfo.mScaleType == ImageView.ScaleType.FIT_END ? new END() : new OTHER();
                postDelayed(new Runnable() { // from class: basecamera.module.functionModule.imagePreview.view.image.PhotoView.5
                    @Override // java.lang.Runnable
                    public void run() {
                        PhotoView.this.mTranslate.withClip(1.0f, 1.0f, width3 - 1.0f, height3 - 1.0f, PhotoView.this.mAnimaDuring / 2, start);
                    }
                }, this.mAnimaDuring / 2);
            }
            this.mCompleteCallBack = runnable;
            this.mTranslate.start();
        }
    }

    @Override // android.view.View
    public boolean canScrollHorizontally(int i) {
        if (this.hasMultiTouch) {
            return true;
        }
        return canScrollHorizontallySelf(i);
    }

    public boolean canScrollHorizontallySelf(float f) {
        if (this.mImgRect.width() <= this.mWidgetRect.width()) {
            return false;
        }
        if (f >= 0.0f || Math.round(this.mImgRect.left) - f < this.mWidgetRect.left) {
            return f <= 0.0f || ((float) Math.round(this.mImgRect.right)) - f > this.mWidgetRect.right;
        }
        return false;
    }

    @Override // android.view.View
    public boolean canScrollVertically(int i) {
        if (this.hasMultiTouch) {
            return true;
        }
        return canScrollVerticallySelf(i);
    }

    public boolean canScrollVerticallySelf(float f) {
        if (this.mImgRect.height() <= this.mWidgetRect.height()) {
            return false;
        }
        if (f >= 0.0f || Math.round(this.mImgRect.top) - f < this.mWidgetRect.top) {
            return f <= 0.0f || ((float) Math.round(this.mImgRect.bottom)) - f > this.mWidgetRect.bottom;
        }
        return false;
    }

    public void disenable() {
        this.isEnable = false;
    }

    @Override // android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (!this.isEnable) {
            return super.dispatchTouchEvent(motionEvent);
        }
        int actionMasked = motionEvent.getActionMasked();
        if (motionEvent.getPointerCount() >= 2) {
            this.hasMultiTouch = true;
        }
        this.mDetector.onTouchEvent(motionEvent);
        this.mRotateDetector.onTouchEvent(motionEvent);
        this.mScaleDetector.onTouchEvent(motionEvent);
        if (actionMasked == 1 || actionMasked == 3) {
            onUp();
        }
        return true;
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        if (this.mClip != null) {
            canvas.clipRect(this.mClip);
            this.mClip = null;
        }
        super.draw(canvas);
    }

    public void enable() {
        this.isEnable = true;
    }

    public int getAnimaDuring() {
        return this.mAnimaDuring;
    }

    public int getDefaultAnimaDuring() {
        return ANIMA_DURING;
    }

    public PhotoInfo getInfo() {
        RectF rectF = new RectF();
        getLocation(this, new int[2]);
        rectF.set(r0[0] + this.mImgRect.left, r0[1] + this.mImgRect.top, r0[0] + this.mImgRect.right, r0[1] + this.mImgRect.bottom);
        return new PhotoInfo(rectF, this.mImgRect, this.mWidgetRect, this.mBaseRect, this.mScreenCenter, this.mScale, this.mDegrees, this.mScaleType);
    }

    public float getMaxScale() {
        return this.mMaxScale;
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int i, int i2) {
        if (!this.hasDrawable) {
            super.onMeasure(i, i2);
            return;
        }
        Drawable drawable = getDrawable();
        int drawableWidth = getDrawableWidth(drawable);
        int drawableHeight = getDrawableHeight(drawable);
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        int mode = View.MeasureSpec.getMode(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(-2, -2);
        }
        if (layoutParams.width != -1 ? mode != 1073741824 && (mode != Integer.MIN_VALUE || drawableWidth <= size) : mode == 0) {
            size = drawableWidth;
        }
        if (layoutParams.height != -1 ? mode2 != 1073741824 && (mode2 != Integer.MIN_VALUE || drawableHeight <= size2) : mode2 == 0) {
            size2 = drawableHeight;
        }
        if (this.mAdjustViewBounds) {
            float f = drawableWidth;
            float f2 = drawableHeight;
            float f3 = size;
            float f4 = size2;
            if (f / f2 != f3 / f4) {
                float f5 = f4 / f2;
                float f6 = f3 / f;
                if (f5 < f6) {
                    f6 = f5;
                }
                if (layoutParams.width != -1) {
                    size = (int) (f * f6);
                }
                if (layoutParams.height != -1) {
                    size2 = (int) (f2 * f6);
                }
            }
        }
        setMeasuredDimension(size, size2);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mWidgetRect.set(0.0f, 0.0f, i, i2);
        this.mScreenCenter.set(i / 2, i2 / 2);
        if (this.isKnowSize) {
            return;
        }
        this.isKnowSize = true;
        initBase();
    }

    public void reset() {
        this.mAnimaMatrix.reset();
        executeTranslate();
        this.mScale = 1.0f;
        this.mTranslateX = 0;
        this.mTranslateY = 0;
    }

    @Override // android.widget.ImageView
    public void setAdjustViewBounds(boolean z) {
        super.setAdjustViewBounds(z);
        this.mAdjustViewBounds = z;
    }

    public void setAnimaDuring(int i) {
        this.mAnimaDuring = i;
    }

    @Override // android.widget.ImageView
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        if (drawable == null) {
            this.hasDrawable = false;
        } else if (hasSize(drawable)) {
            if (!this.hasDrawable) {
                this.hasDrawable = true;
            }
            initBase();
        }
    }

    @Override // android.widget.ImageView
    public void setImageResource(int i) {
        Drawable drawable;
        try {
            drawable = getResources().getDrawable(i);
        } catch (Exception unused) {
            drawable = null;
        }
        setImageDrawable(drawable);
    }

    public void setInterpolator(Interpolator interpolator) {
        this.mTranslate.setInterpolator(interpolator);
    }

    public void setMaxAnimFromWaiteTime(int i) {
        this.MAX_ANIM_FROM_WAITE = i;
    }

    public void setMaxScale(float f) {
        this.mMaxScale = f;
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener onClickListener) {
        super.setOnClickListener(onClickListener);
        this.mClickListener = onClickListener;
    }

    @Override // android.view.View
    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.mLongClick = onLongClickListener;
    }

    @Override // android.widget.ImageView
    public void setScaleType(ImageView.ScaleType scaleType) {
        ImageView.ScaleType scaleType2 = this.mScaleType;
        this.mScaleType = scaleType;
        if (scaleType2 != scaleType) {
            initBase();
        }
    }
}
