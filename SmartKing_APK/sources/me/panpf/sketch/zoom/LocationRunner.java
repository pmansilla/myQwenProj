package me.panpf.sketch.zoom;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Scroller;
import com.wx.wheelview.common.WheelConstants;
import me.panpf.sketch.SLog;
import me.panpf.sketch.util.SketchUtils;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class LocationRunner implements Runnable {
    private int currentX;
    private int currentY;
    private ImageZoomer imageZoomer;
    private ScaleDragHelper scaleDragHelper;
    private Scroller scroller;

    /* JADX INFO: Access modifiers changed from: package-private */
    public LocationRunner(ImageZoomer imageZoomer, ScaleDragHelper scaleDragHelper) {
        this.scroller = new Scroller(imageZoomer.getImageView().getContext(), new AccelerateDecelerateInterpolator());
        this.imageZoomer = imageZoomer;
        this.scaleDragHelper = scaleDragHelper;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cancel() {
        this.scroller.forceFinished(true);
        ImageView imageView = this.imageZoomer.getImageView();
        if (imageView != null) {
            imageView.removeCallbacks(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRunning() {
        return !this.scroller.isFinished();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void location(int i, int i2, int i3, int i4) {
        this.currentX = i;
        this.currentY = i2;
        this.scroller.startScroll(i, i2, i3 - i, i4 - i2, WheelConstants.WHEEL_SCROLL_DELAY_DURATION);
        ImageView imageView = this.imageZoomer.getImageView();
        imageView.removeCallbacks(this);
        imageView.post(this);
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.scroller.isFinished()) {
            if (SLog.isLoggable(524290)) {
                SLog.d(ImageZoomer.NAME, "finished. location run");
                return;
            }
            return;
        }
        if (!this.imageZoomer.isWorking()) {
            SLog.w(ImageZoomer.NAME, "not working. location run");
            this.scroller.forceFinished(true);
            return;
        }
        if (!this.scroller.computeScrollOffset()) {
            if (SLog.isLoggable(524290)) {
                SLog.d(ImageZoomer.NAME, "scroll finished. location run");
                return;
            }
            return;
        }
        int currX = this.scroller.getCurrX();
        int currY = this.scroller.getCurrY();
        this.scaleDragHelper.translateBy(this.currentX - currX, this.currentY - currY);
        this.currentX = currX;
        this.currentY = currY;
        SketchUtils.postOnAnimation(this.imageZoomer.getImageView(), this);
    }
}
