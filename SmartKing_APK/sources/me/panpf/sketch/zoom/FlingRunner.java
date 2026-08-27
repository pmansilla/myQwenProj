package me.panpf.sketch.zoom;

import android.graphics.RectF;
import android.widget.ImageView;
import me.panpf.sketch.SLog;
import me.panpf.sketch.util.SketchUtils;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class FlingRunner implements Runnable {
    private int currentX;
    private int currentY;
    private ImageZoomer imageZoomer;
    private ScaleDragHelper scaleDragHelper;
    private ScrollerProxy scroller;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FlingRunner(ImageZoomer imageZoomer, ScaleDragHelper scaleDragHelper) {
        this.scroller = ScrollerProxy.getScroller(imageZoomer.getImageView().getContext());
        this.imageZoomer = imageZoomer;
        this.scaleDragHelper = scaleDragHelper;
    }

    public void cancelFling() {
        if (SLog.isLoggable(524290)) {
            SLog.d(ImageZoomer.NAME, "cancel fling");
        }
        if (this.scroller != null) {
            this.scroller.forceFinished(true);
        }
        ImageView imageView = this.imageZoomer.getImageView();
        if (imageView != null) {
            imageView.removeCallbacks(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void fling(int i, int i2) {
        int i3;
        int i4;
        int i5;
        int i6;
        if (!this.imageZoomer.isWorking()) {
            SLog.w(ImageZoomer.NAME, "not working. fling");
            return;
        }
        RectF rectF = new RectF();
        this.scaleDragHelper.getDrawRect(rectF);
        if (rectF.isEmpty()) {
            return;
        }
        Size viewSize = this.imageZoomer.getViewSize();
        int width = viewSize.getWidth();
        int height = viewSize.getHeight();
        int round = Math.round(-rectF.left);
        float f = width;
        if (f < rectF.width()) {
            i4 = Math.round(rectF.width() - f);
            i3 = 0;
        } else {
            i3 = round;
            i4 = i3;
        }
        int round2 = Math.round(-rectF.top);
        float f2 = height;
        if (f2 < rectF.height()) {
            i6 = Math.round(rectF.height() - f2);
            i5 = 0;
        } else {
            i5 = round2;
            i6 = i5;
        }
        if (SLog.isLoggable(524290)) {
            SLog.d(ImageZoomer.NAME, "fling. start=%dx %d, min=%dx%d, max=%dx%d", Integer.valueOf(round), Integer.valueOf(round2), Integer.valueOf(i3), Integer.valueOf(i5), Integer.valueOf(i4), Integer.valueOf(i6));
        }
        if (round != i4 || round2 != i6) {
            this.currentX = round;
            this.currentY = round2;
            this.scroller.fling(round, round2, i, i2, i3, i4, i5, i6, 0, 0);
        }
        ImageView imageView = this.imageZoomer.getImageView();
        imageView.removeCallbacks(this);
        imageView.post(this);
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.scroller.isFinished()) {
            if (SLog.isLoggable(524290)) {
                SLog.d(ImageZoomer.NAME, "finished. fling run");
            }
        } else {
            if (!this.imageZoomer.isWorking()) {
                SLog.w(ImageZoomer.NAME, "not working. fling run");
                return;
            }
            if (!this.scroller.computeScrollOffset()) {
                if (SLog.isLoggable(524290)) {
                    SLog.d(ImageZoomer.NAME, "scroll finished. fling run");
                }
            } else {
                int currX = this.scroller.getCurrX();
                int currY = this.scroller.getCurrY();
                this.scaleDragHelper.translateBy(this.currentX - currX, this.currentY - currY);
                this.currentX = currX;
                this.currentY = currY;
                SketchUtils.postOnAnimation(this.imageZoomer.getImageView(), this);
            }
        }
    }
}
