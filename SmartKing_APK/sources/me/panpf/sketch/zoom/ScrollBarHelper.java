package me.panpf.sketch.zoom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Scroller;
import com.wx.wheelview.common.WheelConstants;
import me.panpf.sketch.SLog;
import me.panpf.sketch.util.SketchUtils;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class ScrollBarHelper {
    private FadeScrollBarRunner fadeScrollBarRunner;
    private Handler handler;
    private HiddenScrollBarRunner hiddenScrollBarRunner;
    private ImageZoomer imageZoomer;
    private int scrollBarMargin;
    private int scrollBarRadius;
    private int scrollBarSize;
    private int scrollBarAlpha = 51;
    private RectF scrollBarRectF = new RectF();
    private RectF tempDisplayRectF = new RectF();
    private Paint scrollBarPaint = new Paint();

    /* loaded from: classes2.dex */
    private class FadeScrollBarRunner implements Runnable {
        private Scroller scroller;

        FadeScrollBarRunner(Context context) {
            this.scroller = new Scroller(context, new DecelerateInterpolator());
            this.scroller.forceFinished(true);
        }

        void abort() {
            this.scroller.forceFinished(true);
        }

        boolean isRunning() {
            return !this.scroller.isFinished();
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.scroller.computeScrollOffset()) {
                ScrollBarHelper.this.scrollBarPaint.setAlpha(this.scroller.getCurrX());
                ScrollBarHelper.this.invalidateView();
                ScrollBarHelper.this.handler.postDelayed(this, 60L);
            }
        }

        public void start() {
            this.scroller.startScroll(ScrollBarHelper.this.scrollBarAlpha, 0, -ScrollBarHelper.this.scrollBarAlpha, 0, WheelConstants.WHEEL_SCROLL_DELAY_DURATION);
            ScrollBarHelper.this.handler.post(this);
        }
    }

    /* loaded from: classes2.dex */
    private class HiddenScrollBarRunner implements Runnable {
        private HiddenScrollBarRunner() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ScrollBarHelper.this.fadeScrollBarRunner.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ScrollBarHelper(Context context, ImageZoomer imageZoomer) {
        this.imageZoomer = imageZoomer;
        this.scrollBarPaint.setColor(Color.parseColor("#000000"));
        this.scrollBarPaint.setAlpha(this.scrollBarAlpha);
        this.scrollBarSize = SketchUtils.dp2px(context, 3);
        this.scrollBarMargin = SketchUtils.dp2px(context, 3);
        this.scrollBarRadius = Math.round(this.scrollBarSize / 2);
        this.handler = new Handler(Looper.getMainLooper());
        this.hiddenScrollBarRunner = new HiddenScrollBarRunner();
        this.fadeScrollBarRunner = new FadeScrollBarRunner(context);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invalidateView() {
        ImageView imageView = this.imageZoomer.getImageView();
        if (imageView != null) {
            imageView.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDraw(Canvas canvas) {
        RectF rectF = this.tempDisplayRectF;
        this.imageZoomer.getDrawRect(rectF);
        if (rectF.isEmpty()) {
            if (SLog.isLoggable(524290)) {
                SLog.d(ImageZoomer.NAME, "displayRectF is empty. drawScrollBar. drawRectF=%s", rectF.toString());
                return;
            }
            return;
        }
        Size viewSize = this.imageZoomer.getViewSize();
        int width = viewSize.getWidth();
        int height = viewSize.getHeight();
        float width2 = rectF.width();
        float height2 = rectF.height();
        if (width <= 0 || height <= 0 || width2 == 0.0f || height2 == 0.0f) {
            if (SLog.isLoggable(524290)) {
                SLog.d(ImageZoomer.NAME, "size is 0. drawScrollBar. viewSize=%dx%d, displaySize=%sx%s", Integer.valueOf(width), Integer.valueOf(height), Float.valueOf(width2), Float.valueOf(height2));
                return;
            }
            return;
        }
        ImageView imageView = this.imageZoomer.getImageView();
        int i = width - (this.scrollBarMargin * 2);
        int i2 = height - (this.scrollBarMargin * 2);
        if (((int) width2) > width) {
            int i3 = (int) ((width / width2) * i);
            RectF rectF2 = this.scrollBarRectF;
            rectF2.setEmpty();
            rectF2.left = imageView.getPaddingLeft() + this.scrollBarMargin + (rectF.left < 0.0f ? (int) ((Math.abs(rectF.left) / rectF.width()) * r6) : 0);
            rectF2.top = ((imageView.getPaddingTop() + this.scrollBarMargin) + i2) - this.scrollBarSize;
            rectF2.right = rectF2.left + i3;
            rectF2.bottom = rectF2.top + this.scrollBarSize;
            canvas.drawRoundRect(rectF2, this.scrollBarRadius, this.scrollBarRadius, this.scrollBarPaint);
        }
        if (((int) height2) > height) {
            int i4 = (int) ((height / height2) * i2);
            RectF rectF3 = this.scrollBarRectF;
            rectF3.setEmpty();
            rectF3.left = ((imageView.getPaddingLeft() + this.scrollBarMargin) + i) - this.scrollBarSize;
            rectF3.top = imageView.getPaddingTop() + this.scrollBarMargin + (rectF.top < 0.0f ? (int) ((Math.abs(rectF.top) / rectF.height()) * r5) : 0);
            rectF3.right = rectF3.left + this.scrollBarSize;
            rectF3.bottom = rectF3.top + i4;
            canvas.drawRoundRect(rectF3, this.scrollBarRadius, this.scrollBarRadius, this.scrollBarPaint);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onMatrixChanged() {
        this.scrollBarPaint.setAlpha(this.scrollBarAlpha);
        if (this.fadeScrollBarRunner.isRunning()) {
            this.fadeScrollBarRunner.abort();
        }
        this.handler.removeCallbacks(this.hiddenScrollBarRunner);
        this.handler.postDelayed(this.hiddenScrollBarRunner, 800L);
    }
}
