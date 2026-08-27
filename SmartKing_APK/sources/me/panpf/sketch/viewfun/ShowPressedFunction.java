package me.panpf.sketch.viewfun;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import me.panpf.sketch.SLog;
import me.panpf.sketch.request.DisplayCache;
import me.panpf.sketch.shaper.ImageShaper;

/* loaded from: classes2.dex */
public class ShowPressedFunction extends ViewFunction {
    static final int DEFAULT_MASK_COLOR = 855638016;
    private static final String NAME = "ShowPressedFunction";
    private Rect bounds;
    private GestureDetector gestureDetector;
    private int maskColor = DEFAULT_MASK_COLOR;
    private Paint maskPaint;
    private ImageShaper maskShaper;
    private boolean showProcessed;
    private boolean singleTapUp;
    private FunctionPropertyView view;

    /* loaded from: classes2.dex */
    private class PressedStatusManager extends GestureDetector.SimpleOnGestureListener {
        private Runnable runnable;

        private PressedStatusManager() {
            this.runnable = new Runnable() { // from class: me.panpf.sketch.viewfun.ShowPressedFunction.PressedStatusManager.1
                @Override // java.lang.Runnable
                public void run() {
                    ShowPressedFunction.this.showProcessed = false;
                    ShowPressedFunction.this.view.invalidate();
                }
            };
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent motionEvent) {
            ShowPressedFunction.this.showProcessed = false;
            ShowPressedFunction.this.singleTapUp = false;
            ShowPressedFunction.this.view.removeCallbacks(this.runnable);
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public void onShowPress(MotionEvent motionEvent) {
            super.onShowPress(motionEvent);
            ShowPressedFunction.this.showProcessed = true;
            ShowPressedFunction.this.view.invalidate();
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            ShowPressedFunction.this.singleTapUp = true;
            if (!ShowPressedFunction.this.showProcessed) {
                ShowPressedFunction.this.showProcessed = true;
                ShowPressedFunction.this.view.invalidate();
            }
            ShowPressedFunction.this.view.postDelayed(this.runnable, 120L);
            return super.onSingleTapUp(motionEvent);
        }
    }

    public ShowPressedFunction(@NonNull FunctionPropertyView functionPropertyView) {
        this.view = functionPropertyView;
        this.gestureDetector = new GestureDetector(functionPropertyView.getContext(), new PressedStatusManager());
    }

    private ImageShaper getMaskShaper() {
        if (this.maskShaper != null) {
            return this.maskShaper;
        }
        DisplayCache displayCache = this.view.getDisplayCache();
        ImageShaper shaper = displayCache != null ? displayCache.options.getShaper() : null;
        if (shaper != null) {
            return shaper;
        }
        ImageShaper shaper2 = this.view.getOptions().getShaper();
        if (shaper2 != null) {
            return shaper2;
        }
        return null;
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public void onDraw(@NonNull Canvas canvas) {
        if (this.showProcessed) {
            ImageShaper maskShaper = getMaskShaper();
            if (maskShaper != null) {
                canvas.save();
                try {
                    if (this.bounds == null) {
                        this.bounds = new Rect();
                    }
                    this.bounds.set(this.view.getPaddingLeft(), this.view.getPaddingTop(), this.view.getWidth() - this.view.getPaddingRight(), this.view.getHeight() - this.view.getPaddingBottom());
                    canvas.clipPath(maskShaper.getPath(this.bounds));
                } catch (UnsupportedOperationException e) {
                    SLog.e(NAME, "The current environment doesn't support clipPath has shut down automatically hardware acceleration");
                    if (Build.VERSION.SDK_INT >= 11) {
                        this.view.setLayerType(1, null);
                    }
                    e.printStackTrace();
                }
            }
            if (this.maskPaint == null) {
                this.maskPaint = new Paint();
                this.maskPaint.setColor(this.maskColor);
                this.maskPaint.setAntiAlias(true);
            }
            canvas.drawRect(this.view.getPaddingLeft(), this.view.getPaddingTop(), this.view.getWidth() - this.view.getPaddingRight(), this.view.getHeight() - this.view.getPaddingBottom(), this.maskPaint);
            if (maskShaper != null) {
                canvas.restore();
            }
        }
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public boolean onTouchEvent(@NonNull MotionEvent motionEvent) {
        if (this.view.isClickable()) {
            this.gestureDetector.onTouchEvent(motionEvent);
            int action = motionEvent.getAction() & 255;
            if (action != 1) {
                switch (action) {
                }
            }
            if (this.showProcessed && !this.singleTapUp) {
                this.showProcessed = false;
                this.view.invalidate();
            }
        }
        return false;
    }

    public boolean setMaskColor(@ColorInt int i) {
        if (this.maskColor == i) {
            return false;
        }
        this.maskColor = i;
        if (this.maskPaint == null) {
            return true;
        }
        this.maskPaint.setColor(i);
        return true;
    }

    public boolean setMaskShaper(@Nullable ImageShaper imageShaper) {
        if (this.maskShaper == imageShaper) {
            return false;
        }
        this.maskShaper = imageShaper;
        return true;
    }
}
