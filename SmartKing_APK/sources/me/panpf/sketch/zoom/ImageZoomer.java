package me.panpf.sketch.zoom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import com.amap.api.maps.utils.SpatialRelationUtil;
import java.util.ArrayList;
import me.panpf.sketch.SLog;

/* loaded from: classes2.dex */
public class ImageZoomer {
    public static final String NAME = "ImageZoomer";
    private BlockDisplayer blockDisplayer;
    private ImageView imageView;
    private OnDragFlingListener onDragFlingListener;
    private ArrayList<OnMatrixChangeListener> onMatrixChangeListenerList;
    private OnRotateChangeListener onRotateChangeListener;
    private OnScaleChangeListener onScaleChangeListener;
    private OnViewLongPressListener onViewLongPressListener;
    private OnViewTapListener onViewTapListener;
    private boolean readMode;
    private int rotateDegrees;
    private ScaleDragHelper scaleDragHelper;
    private ImageView.ScaleType scaleType;
    private ScrollBarHelper scrollBarHelper;
    private TapHelper tapHelper;
    private Sizes sizes = new Sizes();
    private Scales scales = new Scales();
    private int zoomDuration = 200;
    private Interpolator zoomInterpolator = new AccelerateDecelerateInterpolator();
    private boolean allowParentInterceptOnEdge = true;

    /* loaded from: classes2.dex */
    public interface OnDragFlingListener {
        void onFling(float f, float f2, float f3, float f4);
    }

    /* loaded from: classes2.dex */
    public interface OnMatrixChangeListener {
        void onMatrixChanged(@NonNull ImageZoomer imageZoomer);
    }

    /* loaded from: classes2.dex */
    public interface OnRotateChangeListener {
        void onRotateChanged(@NonNull ImageZoomer imageZoomer);
    }

    /* loaded from: classes2.dex */
    public interface OnScaleChangeListener {
        void onScaleChanged(float f, float f2, float f3);
    }

    /* loaded from: classes2.dex */
    public interface OnViewLongPressListener {
        void onViewLongPress(@NonNull View view, float f, float f2);
    }

    /* loaded from: classes2.dex */
    public interface OnViewTapListener {
        void onViewTap(@NonNull View view, float f, float f2);
    }

    public ImageZoomer(@NonNull ImageView imageView) {
        Context applicationContext = imageView.getContext().getApplicationContext();
        this.imageView = imageView;
        this.tapHelper = new TapHelper(applicationContext, this);
        this.scaleDragHelper = new ScaleDragHelper(applicationContext, this);
        this.scrollBarHelper = new ScrollBarHelper(applicationContext, this);
        this.blockDisplayer = new BlockDisplayer(applicationContext, this);
    }

    public void addOnMatrixChangeListener(@NonNull OnMatrixChangeListener onMatrixChangeListener) {
        if (onMatrixChangeListener != null) {
            if (this.onMatrixChangeListenerList == null) {
                this.onMatrixChangeListenerList = new ArrayList<>(1);
            }
            this.onMatrixChangeListenerList.add(onMatrixChangeListener);
        }
    }

    public float getBaseZoomScale() {
        return this.scaleDragHelper.getDefaultZoomScale();
    }

    @NonNull
    public BlockDisplayer getBlockDisplayer() {
        return this.blockDisplayer;
    }

    @NonNull
    public float[] getDoubleClickZoomScales() {
        return this.scales.doubleClickZoomScales;
    }

    public void getDrawMatrix(@NonNull Matrix matrix) {
        matrix.set(this.scaleDragHelper.getDrawMatrix());
    }

    public void getDrawRect(@NonNull RectF rectF) {
        this.scaleDragHelper.getDrawRect(rectF);
    }

    @NonNull
    public Size getDrawableSize() {
        return this.sizes.drawableSize;
    }

    public float getFillZoomScale() {
        return this.scales.fillZoomScale;
    }

    public float getFullZoomScale() {
        return this.scales.fullZoomScale;
    }

    @NonNull
    public Size getImageSize() {
        return this.sizes.imageSize;
    }

    @NonNull
    public ImageView getImageView() {
        return this.imageView;
    }

    public float getMaxZoomScale() {
        return this.scales.maxZoomScale;
    }

    public float getMinZoomScale() {
        return this.scales.minZoomScale;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public OnDragFlingListener getOnDragFlingListener() {
        return this.onDragFlingListener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public OnScaleChangeListener getOnScaleChangeListener() {
        return this.onScaleChangeListener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public OnViewLongPressListener getOnViewLongPressListener() {
        return this.onViewLongPressListener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public OnViewTapListener getOnViewTapListener() {
        return this.onViewTapListener;
    }

    public float getOriginZoomScale() {
        return this.scales.originZoomScale;
    }

    public int getRotateDegrees() {
        return this.rotateDegrees;
    }

    @NonNull
    public ImageView.ScaleType getScaleType() {
        return this.scaleType;
    }

    public float getSupportZoomScale() {
        return this.scaleDragHelper.getSupportZoomScale();
    }

    @NonNull
    public Size getViewSize() {
        return this.sizes.viewSize;
    }

    public void getVisibleRect(@NonNull Rect rect) {
        this.scaleDragHelper.getVisibleRect(rect);
    }

    public int getZoomDuration() {
        return this.zoomDuration;
    }

    @NonNull
    public Interpolator getZoomInterpolator() {
        return this.zoomInterpolator;
    }

    public float getZoomScale() {
        return this.scaleDragHelper.getZoomScale();
    }

    public boolean isAllowParentInterceptOnEdge() {
        return this.allowParentInterceptOnEdge;
    }

    public boolean isReadMode() {
        return this.readMode;
    }

    public boolean isWorking() {
        return !this.sizes.isEmpty();
    }

    public boolean isZooming() {
        return this.scaleDragHelper.isZooming();
    }

    public boolean location(float f, float f2) {
        return location(f, f2, false);
    }

    public boolean location(float f, float f2, boolean z) {
        if (isWorking()) {
            this.scaleDragHelper.location(f, f2, z);
            return true;
        }
        SLog.w(NAME, "not working. location");
        return false;
    }

    public void onDraw(@NonNull Canvas canvas) {
        if (isWorking()) {
            this.blockDisplayer.onDraw(canvas);
            this.scrollBarHelper.onDraw(canvas);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onMatrixChanged() {
        this.scrollBarHelper.onMatrixChanged();
        this.blockDisplayer.onMatrixChanged();
        this.imageView.setImageMatrix(this.scaleDragHelper.getDrawMatrix());
        if (this.onMatrixChangeListenerList == null || this.onMatrixChangeListenerList.isEmpty()) {
            return;
        }
        int size = this.onMatrixChangeListenerList.size();
        for (int i = 0; i < size; i++) {
            this.onMatrixChangeListenerList.get(i).onMatrixChanged(this);
        }
    }

    public boolean onTouchEvent(@NonNull MotionEvent motionEvent) {
        if (isWorking()) {
            return this.scaleDragHelper.onTouchEvent(motionEvent) || this.tapHelper.onTouchEvent(motionEvent);
        }
        return false;
    }

    public void recycle(@NonNull String str) {
        if (isWorking()) {
            this.sizes.clean();
            this.scales.clean();
            this.scaleDragHelper.recycle();
            this.blockDisplayer.recycle(str);
            this.imageView.setImageMatrix(null);
            this.imageView.setScaleType(this.scaleType);
            this.scaleType = null;
        }
    }

    public boolean removeOnMatrixChangeListener(@NonNull OnMatrixChangeListener onMatrixChangeListener) {
        return onMatrixChangeListener != null && this.onMatrixChangeListenerList != null && this.onMatrixChangeListenerList.size() > 0 && this.onMatrixChangeListenerList.remove(onMatrixChangeListener);
    }

    public boolean reset(@NonNull String str) {
        recycle(str);
        this.sizes.resetSizes(this.imageView);
        if (!isWorking()) {
            return false;
        }
        this.scaleType = this.imageView.getScaleType();
        this.imageView.setScaleType(ImageView.ScaleType.MATRIX);
        this.scales.reset(this.imageView.getContext(), this.sizes, this.scaleType, this.rotateDegrees, this.readMode);
        this.scaleDragHelper.reset();
        this.blockDisplayer.reset();
        return true;
    }

    public boolean rotateBy(int i) {
        return rotateTo(i + getRotateDegrees());
    }

    public boolean rotateTo(int i) {
        if (!isWorking()) {
            SLog.w(NAME, "not working. rotateTo");
            return false;
        }
        if (this.rotateDegrees == i) {
            return false;
        }
        if (i % 90 != 0) {
            SLog.w(NAME, "rotate degrees must be in multiples of 90");
            return false;
        }
        int i2 = i % SpatialRelationUtil.A_CIRCLE_DEGREE;
        if (i2 <= 0) {
            i2 = 360 - i2;
        }
        this.rotateDegrees = i2;
        reset("rotateTo");
        if (this.onRotateChangeListener == null) {
            return true;
        }
        this.onRotateChangeListener.onRotateChanged(this);
        return true;
    }

    public void setAllowParentInterceptOnEdge(boolean z) {
        this.allowParentInterceptOnEdge = z;
    }

    public void setOnDragFlingListener(@Nullable OnDragFlingListener onDragFlingListener) {
        this.onDragFlingListener = onDragFlingListener;
    }

    public void setOnRotateChangeListener(@Nullable OnRotateChangeListener onRotateChangeListener) {
        this.onRotateChangeListener = onRotateChangeListener;
    }

    public void setOnScaleChangeListener(@Nullable OnScaleChangeListener onScaleChangeListener) {
        this.onScaleChangeListener = onScaleChangeListener;
    }

    public void setOnViewLongPressListener(@Nullable OnViewLongPressListener onViewLongPressListener) {
        this.onViewLongPressListener = onViewLongPressListener;
    }

    public void setOnViewTapListener(@Nullable OnViewTapListener onViewTapListener) {
        this.onViewTapListener = onViewTapListener;
    }

    public void setReadMode(boolean z) {
        if (this.readMode == z) {
            return;
        }
        this.readMode = z;
        reset("setReadMode");
    }

    public void setScaleType(@NonNull ImageView.ScaleType scaleType) {
        if (scaleType == null || this.scaleType == scaleType) {
            return;
        }
        this.scaleType = scaleType;
        reset("setScaleType");
    }

    public void setZoomDuration(int i) {
        if (i > 0) {
            this.zoomDuration = i;
        }
    }

    public void setZoomInterpolator(@NonNull Interpolator interpolator) {
        this.zoomInterpolator = interpolator;
    }

    public boolean zoom(float f) {
        return zoom(f, false);
    }

    public boolean zoom(float f, float f2, float f3, boolean z) {
        if (!isWorking()) {
            SLog.w(NAME, "not working. zoom(float, float, float, boolean)");
            return false;
        }
        if (f < this.scales.minZoomScale || f > this.scales.maxZoomScale) {
            SLog.w(NAME, "Scale must be within the range of %s(minScale) and %s(maxScale). %s", Float.valueOf(this.scales.minZoomScale), Float.valueOf(this.scales.maxZoomScale), Float.valueOf(f));
            return false;
        }
        this.scaleDragHelper.zoom(f, f2, f3, z);
        return true;
    }

    public boolean zoom(float f, boolean z) {
        if (isWorking()) {
            ImageView imageView = getImageView();
            return zoom(f, imageView.getRight() / 2, imageView.getBottom() / 2, z);
        }
        SLog.w(NAME, "not working. zoom(float, boolean)");
        return false;
    }
}
