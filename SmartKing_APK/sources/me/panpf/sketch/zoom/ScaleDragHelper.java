package me.panpf.sketch.zoom;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.ImageView;
import me.panpf.sketch.SLog;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.decode.ImageSizeCalculator;
import me.panpf.sketch.util.SketchUtils;
import me.panpf.sketch.zoom.ImageZoomer;
import me.panpf.sketch.zoom.gestures.ActionListener;
import me.panpf.sketch.zoom.gestures.OnScaleDragGestureListener;
import me.panpf.sketch.zoom.gestures.ScaleDragGestureDetector;
import me.panpf.sketch.zoom.gestures.ScaleDragGestureDetectorCompat;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class ScaleDragHelper implements OnScaleDragGestureListener, ActionListener {
    private static final int EDGE_BOTH = 2;
    private static final int EDGE_END = 1;
    private static final int EDGE_NONE = -1;
    private static final int EDGE_START = 0;
    private boolean disallowParentInterceptTouchEvent;
    private FlingRunner flingRunner;
    private ImageZoomer imageZoomer;
    private LocationRunner locationRunner;
    private ScaleDragGestureDetector scaleDragGestureDetector;
    private float tempLastScaleFocusX;
    private float tempLastScaleFocusY;
    private boolean zooming;
    private Matrix baseMatrix = new Matrix();
    private Matrix supportMatrix = new Matrix();
    private Matrix drawMatrix = new Matrix();
    private RectF tempDisplayRectF = new RectF();
    private int horScrollEdge = -1;
    private int verScrollEdge = -1;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: me.panpf.sketch.zoom.ScaleDragHelper$1, reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$widget$ImageView$ScaleType = new int[ImageView.ScaleType.values().length];

        static {
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_START.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_END.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public ScaleDragHelper(Context context, ImageZoomer imageZoomer) {
        Context applicationContext = context.getApplicationContext();
        this.imageZoomer = imageZoomer;
        this.scaleDragGestureDetector = ScaleDragGestureDetectorCompat.newInstance(applicationContext, this);
        this.scaleDragGestureDetector.setActionListener(this);
    }

    private void checkAndApplyMatrix() {
        if (checkMatrixBounds()) {
            if (!ImageView.ScaleType.MATRIX.equals(this.imageZoomer.getImageView().getScaleType())) {
                throw new IllegalStateException("ImageView scaleType must be is MATRIX");
            }
            this.imageZoomer.onMatrixChanged();
        }
    }

    private boolean checkMatrixBounds() {
        float f;
        RectF rectF = this.tempDisplayRectF;
        getDrawRect(rectF);
        if (rectF.isEmpty()) {
            this.horScrollEdge = -1;
            this.verScrollEdge = -1;
            return false;
        }
        float height = rectF.height();
        float width = rectF.width();
        int height2 = this.imageZoomer.getViewSize().getHeight();
        int i = (int) height;
        float f2 = 0.0f;
        if (i <= height2) {
            switch (AnonymousClass1.$SwitchMap$android$widget$ImageView$ScaleType[this.imageZoomer.getScaleType().ordinal()]) {
                case 1:
                    f = -rectF.top;
                    break;
                case 2:
                    f = (height2 - height) - rectF.top;
                    break;
                default:
                    f = ((height2 - height) / 2.0f) - rectF.top;
                    break;
            }
        } else {
            f = ((int) rectF.top) > 0 ? -rectF.top : ((int) rectF.bottom) < height2 ? height2 - rectF.bottom : 0.0f;
        }
        int width2 = this.imageZoomer.getViewSize().getWidth();
        int i2 = (int) width;
        if (i2 <= width2) {
            switch (AnonymousClass1.$SwitchMap$android$widget$ImageView$ScaleType[this.imageZoomer.getScaleType().ordinal()]) {
                case 1:
                    f2 = -rectF.left;
                    break;
                case 2:
                    f2 = (width2 - width) - rectF.left;
                    break;
                default:
                    f2 = ((width2 - width) / 2.0f) - rectF.left;
                    break;
            }
        } else if (((int) rectF.left) > 0) {
            f2 = -rectF.left;
        } else if (((int) rectF.right) < width2) {
            f2 = width2 - rectF.right;
        }
        this.supportMatrix.postTranslate(f2, f);
        if (i <= height2) {
            this.verScrollEdge = 2;
        } else if (((int) rectF.top) >= 0) {
            this.verScrollEdge = 0;
        } else if (((int) rectF.bottom) <= height2) {
            this.verScrollEdge = 1;
        } else {
            this.verScrollEdge = -1;
        }
        if (i2 <= width2) {
            this.horScrollEdge = 2;
        } else if (((int) rectF.left) >= 0) {
            this.horScrollEdge = 0;
        } else if (((int) rectF.right) <= width2) {
            this.horScrollEdge = 1;
        } else {
            this.horScrollEdge = -1;
        }
        return true;
    }

    private static String getScrollEdgeName(int i) {
        return i == -1 ? SLog.LEVEL_NAME_NONE : i == 0 ? "START" : i == 1 ? "END" : i == 2 ? "BOTH" : "UNKNOWN";
    }

    private static void requestDisallowInterceptTouchEvent(ImageView imageView, boolean z) {
        ViewParent parent = imageView.getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(z);
        }
    }

    private void resetBaseMatrix() {
        this.baseMatrix.reset();
        Size viewSize = this.imageZoomer.getViewSize();
        Size imageSize = this.imageZoomer.getImageSize();
        Size drawableSize = this.imageZoomer.getDrawableSize();
        boolean isReadMode = this.imageZoomer.isReadMode();
        ImageView.ScaleType scaleType = this.imageZoomer.getScaleType();
        int width = this.imageZoomer.getRotateDegrees() % 180 == 0 ? drawableSize.getWidth() : drawableSize.getHeight();
        int height = this.imageZoomer.getRotateDegrees() % 180 == 0 ? drawableSize.getHeight() : drawableSize.getWidth();
        int width2 = this.imageZoomer.getRotateDegrees() % 180 == 0 ? imageSize.getWidth() : imageSize.getHeight();
        int height2 = this.imageZoomer.getRotateDegrees() % 180 == 0 ? imageSize.getHeight() : imageSize.getWidth();
        float f = width;
        float width3 = viewSize.getWidth() / f;
        float f2 = height;
        float height3 = viewSize.getHeight() / f2;
        boolean z = width > viewSize.getWidth() || height > viewSize.getHeight();
        if (scaleType == ImageView.ScaleType.CENTER || (scaleType == ImageView.ScaleType.CENTER_INSIDE && !z)) {
            ImageSizeCalculator sizeCalculator = Sketch.with(this.imageZoomer.getImageView().getContext()).getConfiguration().getSizeCalculator();
            if (isReadMode && sizeCalculator.canUseReadModeByHeight(width2, height2)) {
                this.baseMatrix.postScale(width3, width3);
                return;
            } else if (isReadMode && sizeCalculator.canUseReadModeByWidth(width2, height2)) {
                this.baseMatrix.postScale(height3, height3);
                return;
            } else {
                this.baseMatrix.postTranslate((viewSize.getWidth() - width) / 2.0f, (viewSize.getHeight() - height) / 2.0f);
                return;
            }
        }
        if (scaleType == ImageView.ScaleType.CENTER_CROP) {
            float max = Math.max(width3, height3);
            this.baseMatrix.postScale(max, max);
            this.baseMatrix.postTranslate((viewSize.getWidth() - (f * max)) / 2.0f, (viewSize.getHeight() - (f2 * max)) / 2.0f);
            return;
        }
        if (scaleType != ImageView.ScaleType.FIT_START && scaleType != ImageView.ScaleType.FIT_CENTER && scaleType != ImageView.ScaleType.FIT_END && (scaleType != ImageView.ScaleType.CENTER_INSIDE || !z)) {
            if (scaleType == ImageView.ScaleType.FIT_XY) {
                this.baseMatrix.setRectToRect(new RectF(0.0f, 0.0f, f, f2), new RectF(0.0f, 0.0f, viewSize.getWidth(), viewSize.getHeight()), Matrix.ScaleToFit.FILL);
                return;
            }
            return;
        }
        ImageSizeCalculator sizeCalculator2 = Sketch.with(this.imageZoomer.getImageView().getContext()).getConfiguration().getSizeCalculator();
        if (isReadMode && sizeCalculator2.canUseReadModeByHeight(width2, height2)) {
            this.baseMatrix.postScale(width3, width3);
        } else if (isReadMode && sizeCalculator2.canUseReadModeByWidth(width2, height2)) {
            this.baseMatrix.postScale(height3, height3);
        } else {
            this.baseMatrix.setRectToRect(new RectF(0.0f, 0.0f, f, f2), new RectF(0.0f, 0.0f, viewSize.getWidth(), viewSize.getHeight()), scaleType == ImageView.ScaleType.FIT_START ? Matrix.ScaleToFit.START : scaleType == ImageView.ScaleType.FIT_END ? Matrix.ScaleToFit.END : Matrix.ScaleToFit.CENTER);
        }
    }

    private void resetSupportMatrix() {
        this.supportMatrix.reset();
        this.supportMatrix.postRotate(this.imageZoomer.getRotateDegrees());
    }

    void cancelFling() {
        if (this.flingRunner != null) {
            this.flingRunner.cancelFling();
            this.flingRunner = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getDefaultZoomScale() {
        return SketchUtils.getMatrixScale(this.baseMatrix);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Matrix getDrawMatrix() {
        this.drawMatrix.set(this.baseMatrix);
        this.drawMatrix.postConcat(this.supportMatrix);
        return this.drawMatrix;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getDrawRect(RectF rectF) {
        if (this.imageZoomer.isWorking()) {
            Size drawableSize = this.imageZoomer.getDrawableSize();
            rectF.set(0.0f, 0.0f, drawableSize.getWidth(), drawableSize.getHeight());
            getDrawMatrix().mapRect(rectF);
        } else {
            if (SLog.isLoggable(524289)) {
                SLog.v(ImageZoomer.NAME, "not working. getDrawRect");
            }
            rectF.setEmpty();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getSupportZoomScale() {
        return SketchUtils.getMatrixScale(this.supportMatrix);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getVisibleRect(Rect rect) {
        if (!this.imageZoomer.isWorking()) {
            if (SLog.isLoggable(524289)) {
                SLog.v(ImageZoomer.NAME, "not working. getVisibleRect");
            }
            rect.setEmpty();
            return;
        }
        RectF rectF = new RectF();
        getDrawRect(rectF);
        if (rectF.isEmpty()) {
            rect.setEmpty();
            return;
        }
        Size viewSize = this.imageZoomer.getViewSize();
        Size drawableSize = this.imageZoomer.getDrawableSize();
        float width = rectF.width();
        float height = rectF.height();
        float width2 = width / (this.imageZoomer.getRotateDegrees() % 180 == 0 ? drawableSize.getWidth() : drawableSize.getHeight());
        float height2 = height / (this.imageZoomer.getRotateDegrees() % 180 == 0 ? drawableSize.getHeight() : drawableSize.getWidth());
        float abs = rectF.left >= 0.0f ? 0.0f : Math.abs(rectF.left);
        float width3 = width >= ((float) viewSize.getWidth()) ? viewSize.getWidth() + abs : rectF.right - rectF.left;
        float abs2 = rectF.top < 0.0f ? Math.abs(rectF.top) : 0.0f;
        rect.set(Math.round(abs / width2), Math.round(abs2 / height2), Math.round(width3 / width2), Math.round((height >= ((float) viewSize.getHeight()) ? viewSize.getHeight() + abs2 : rectF.bottom - rectF.top) / height2));
        SketchUtils.reverseRotateRect(rect, this.imageZoomer.getRotateDegrees(), drawableSize);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getZoomScale() {
        return SketchUtils.getMatrixScale(getDrawMatrix());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isZooming() {
        return this.zooming;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void location(float f, float f2, boolean z) {
        Size viewSize = this.imageZoomer.getViewSize();
        Size drawableSize = this.imageZoomer.getDrawableSize();
        PointF pointF = new PointF(f, f2);
        SketchUtils.rotatePoint(pointF, this.imageZoomer.getRotateDegrees(), drawableSize);
        float f3 = pointF.x;
        float f4 = pointF.y;
        cancelFling();
        if (this.locationRunner != null) {
            this.locationRunner.cancel();
        }
        int width = viewSize.getWidth();
        int height = viewSize.getHeight();
        if (SketchUtils.formatFloat(getZoomScale(), 2) == SketchUtils.formatFloat(this.imageZoomer.getFullZoomScale(), 2)) {
            float[] doubleClickZoomScales = this.imageZoomer.getDoubleClickZoomScales();
            zoom(doubleClickZoomScales[doubleClickZoomScales.length - 1], f3, f4, false);
        }
        RectF rectF = new RectF();
        getDrawRect(rectF);
        float zoomScale = getZoomScale();
        int min = Math.min(Math.max((int) (f3 * zoomScale), 0), (int) rectF.width());
        int min2 = Math.min(Math.max((int) (f4 * zoomScale), 0), (int) rectF.height()) - (height / 2);
        int max = Math.max(min - (width / 2), 0);
        int max2 = Math.max(min2, 0);
        int abs = Math.abs((int) rectF.left);
        int abs2 = Math.abs((int) rectF.top);
        if (SLog.isLoggable(524290)) {
            SLog.d(ImageZoomer.NAME, "location. start=%dx%d, end=%dx%d", Integer.valueOf(abs), Integer.valueOf(abs2), Integer.valueOf(max), Integer.valueOf(max2));
        }
        if (!z) {
            translateBy(-(max - abs), -(max2 - abs2));
        } else {
            this.locationRunner = new LocationRunner(this.imageZoomer, this);
            this.locationRunner.location(abs, abs2, max, max2);
        }
    }

    @Override // me.panpf.sketch.zoom.gestures.ActionListener
    public void onActionCancel(MotionEvent motionEvent) {
        onActionUp(motionEvent);
    }

    @Override // me.panpf.sketch.zoom.gestures.ActionListener
    public void onActionDown(MotionEvent motionEvent) {
        this.tempLastScaleFocusX = 0.0f;
        this.tempLastScaleFocusY = 0.0f;
        if (SLog.isLoggable(524290)) {
            SLog.d(ImageZoomer.NAME, "disallow parent intercept touch event. action down");
        }
        requestDisallowInterceptTouchEvent(this.imageZoomer.getImageView(), true);
        cancelFling();
    }

    @Override // me.panpf.sketch.zoom.gestures.ActionListener
    public void onActionUp(MotionEvent motionEvent) {
        float formatFloat = SketchUtils.formatFloat(getZoomScale(), 2);
        if (formatFloat < SketchUtils.formatFloat(this.imageZoomer.getMinZoomScale(), 2)) {
            RectF rectF = new RectF();
            getDrawRect(rectF);
            if (rectF.isEmpty()) {
                return;
            }
            zoom(this.imageZoomer.getMinZoomScale(), rectF.centerX(), rectF.centerY(), true);
            return;
        }
        if (formatFloat <= SketchUtils.formatFloat(this.imageZoomer.getMaxZoomScale(), 2) || this.tempLastScaleFocusX == 0.0f || this.tempLastScaleFocusY == 0.0f) {
            return;
        }
        zoom(this.imageZoomer.getMaxZoomScale(), this.tempLastScaleFocusX, this.tempLastScaleFocusY, true);
    }

    @Override // me.panpf.sketch.zoom.gestures.OnScaleDragGestureListener
    public void onDrag(float f, float f2) {
        if (this.imageZoomer.getImageView() == null || this.scaleDragGestureDetector.isScaling()) {
            return;
        }
        if (SLog.isLoggable(524290)) {
            SLog.d(ImageZoomer.NAME, "drag. dx: %s, dy: %s", Float.valueOf(f), Float.valueOf(f2));
        }
        this.supportMatrix.postTranslate(f, f2);
        checkAndApplyMatrix();
        if (!this.imageZoomer.isAllowParentInterceptOnEdge() || this.scaleDragGestureDetector.isScaling() || this.disallowParentInterceptTouchEvent) {
            if (SLog.isLoggable(524290)) {
                SLog.d(ImageZoomer.NAME, "disallow parent intercept touch event. onDrag. allowParentInterceptOnEdge=%s, scaling=%s, tempDisallowParentInterceptTouchEvent=%s", Boolean.valueOf(this.imageZoomer.isAllowParentInterceptOnEdge()), Boolean.valueOf(this.scaleDragGestureDetector.isScaling()), Boolean.valueOf(this.disallowParentInterceptTouchEvent));
            }
            requestDisallowInterceptTouchEvent(this.imageZoomer.getImageView(), true);
        } else if (this.horScrollEdge == 2 || ((this.horScrollEdge == 0 && f >= 1.0f) || (this.horScrollEdge == 1 && f <= -1.0f))) {
            if (SLog.isLoggable(524290)) {
                SLog.d(ImageZoomer.NAME, "allow parent intercept touch event. onDrag. scrollEdge=%s-%s", getScrollEdgeName(this.horScrollEdge), getScrollEdgeName(this.verScrollEdge));
            }
            requestDisallowInterceptTouchEvent(this.imageZoomer.getImageView(), false);
        } else {
            if (SLog.isLoggable(524290)) {
                SLog.d(ImageZoomer.NAME, "disallow parent intercept touch event. onDrag. scrollEdge=%s-%s", getScrollEdgeName(this.horScrollEdge), getScrollEdgeName(this.verScrollEdge));
            }
            requestDisallowInterceptTouchEvent(this.imageZoomer.getImageView(), true);
        }
    }

    @Override // me.panpf.sketch.zoom.gestures.OnScaleDragGestureListener
    public void onFling(float f, float f2, float f3, float f4) {
        this.flingRunner = new FlingRunner(this.imageZoomer, this);
        this.flingRunner.fling((int) f3, (int) f4);
        ImageZoomer.OnDragFlingListener onDragFlingListener = this.imageZoomer.getOnDragFlingListener();
        if (onDragFlingListener != null) {
            onDragFlingListener.onFling(f, f2, f3, f4);
        }
    }

    @Override // me.panpf.sketch.zoom.gestures.OnScaleDragGestureListener
    public void onScale(float f, float f2, float f3) {
        if (SLog.isLoggable(524290)) {
            SLog.d(ImageZoomer.NAME, "scale. scaleFactor: %s, dx: %s, dy: %s", Float.valueOf(f), Float.valueOf(f2), Float.valueOf(f3));
        }
        this.tempLastScaleFocusX = f2;
        this.tempLastScaleFocusY = f3;
        float supportZoomScale = getSupportZoomScale();
        float f4 = supportZoomScale * f;
        if (f > 1.0f) {
            if (supportZoomScale >= this.imageZoomer.getMaxZoomScale() / SketchUtils.getMatrixScale(this.baseMatrix)) {
                double d = f4 - supportZoomScale;
                Double.isNaN(d);
                f = (((float) (d * 0.4d)) + supportZoomScale) / supportZoomScale;
            }
        } else if (f < 1.0f && supportZoomScale <= this.imageZoomer.getMinZoomScale() / SketchUtils.getMatrixScale(this.baseMatrix)) {
            double d2 = f4 - supportZoomScale;
            Double.isNaN(d2);
            f = (((float) (d2 * 0.4d)) + supportZoomScale) / supportZoomScale;
        }
        this.supportMatrix.postScale(f, f, f2, f3);
        checkAndApplyMatrix();
        ImageZoomer.OnScaleChangeListener onScaleChangeListener = this.imageZoomer.getOnScaleChangeListener();
        if (onScaleChangeListener != null) {
            onScaleChangeListener.onScaleChanged(f, f2, f3);
        }
    }

    @Override // me.panpf.sketch.zoom.gestures.OnScaleDragGestureListener
    public boolean onScaleBegin() {
        if (SLog.isLoggable(524290)) {
            SLog.d(ImageZoomer.NAME, "scale begin");
        }
        this.zooming = true;
        return true;
    }

    @Override // me.panpf.sketch.zoom.gestures.OnScaleDragGestureListener
    public void onScaleEnd() {
        if (SLog.isLoggable(524290)) {
            SLog.d(ImageZoomer.NAME, "scale end");
        }
        float formatFloat = SketchUtils.formatFloat(getZoomScale(), 2);
        boolean z = formatFloat < SketchUtils.formatFloat(this.imageZoomer.getMinZoomScale(), 2);
        boolean z2 = formatFloat > SketchUtils.formatFloat(this.imageZoomer.getMaxZoomScale(), 2);
        if (z || z2) {
            return;
        }
        this.zooming = false;
        this.imageZoomer.onMatrixChanged();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.locationRunner != null) {
            if (this.locationRunner.isRunning()) {
                if (SLog.isLoggable(524290)) {
                    SLog.d(ImageZoomer.NAME, "disallow parent intercept touch event. location running");
                }
                requestDisallowInterceptTouchEvent(this.imageZoomer.getImageView(), true);
                return true;
            }
            this.locationRunner = null;
        }
        boolean isScaling = this.scaleDragGestureDetector.isScaling();
        boolean isDragging = this.scaleDragGestureDetector.isDragging();
        boolean onTouchEvent = this.scaleDragGestureDetector.onTouchEvent(motionEvent);
        this.disallowParentInterceptTouchEvent = !isScaling && !this.scaleDragGestureDetector.isScaling() && isDragging && this.scaleDragGestureDetector.isDragging();
        return onTouchEvent;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void recycle() {
        cancelFling();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reset() {
        resetBaseMatrix();
        resetSupportMatrix();
        checkAndApplyMatrix();
    }

    void scaleBy(float f, float f2, float f3) {
        this.supportMatrix.postScale(f, f, f2, f3);
        checkAndApplyMatrix();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setZooming(boolean z) {
        this.zooming = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void translateBy(float f, float f2) {
        this.supportMatrix.postTranslate(f, f2);
        checkAndApplyMatrix();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void zoom(float f, float f2, float f3, boolean z) {
        if (z) {
            new ZoomRunner(this.imageZoomer, this, getZoomScale(), f, f2, f3).zoom();
            return;
        }
        scaleBy((f / getDefaultZoomScale()) / getSupportZoomScale(), f2, f3);
    }
}
