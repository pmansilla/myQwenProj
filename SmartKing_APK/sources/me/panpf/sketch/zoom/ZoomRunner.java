package me.panpf.sketch.zoom;

import me.panpf.sketch.SLog;
import me.panpf.sketch.util.SketchUtils;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class ZoomRunner implements Runnable {
    private ImageZoomer imageZoomer;
    private final float mFocalX;
    private final float mFocalY;
    private final long mStartTime = System.currentTimeMillis();
    private final float mZoomEnd;
    private final float mZoomStart;
    private ScaleDragHelper scaleDragHelper;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ZoomRunner(ImageZoomer imageZoomer, ScaleDragHelper scaleDragHelper, float f, float f2, float f3, float f4) {
        this.imageZoomer = imageZoomer;
        this.scaleDragHelper = scaleDragHelper;
        this.mFocalX = f3;
        this.mFocalY = f4;
        this.mZoomStart = f;
        this.mZoomEnd = f2;
    }

    private float interpolate() {
        return this.imageZoomer.getZoomInterpolator().getInterpolation(Math.min(1.0f, (((float) (System.currentTimeMillis() - this.mStartTime)) * 1.0f) / this.imageZoomer.getZoomDuration()));
    }

    @Override // java.lang.Runnable
    public void run() {
        if (!this.imageZoomer.isWorking()) {
            SLog.w(ImageZoomer.NAME, "not working. zoom run");
            return;
        }
        float interpolate = interpolate();
        float zoomScale = (this.mZoomStart + ((this.mZoomEnd - this.mZoomStart) * interpolate)) / this.scaleDragHelper.getZoomScale();
        boolean z = interpolate < 1.0f;
        this.scaleDragHelper.setZooming(z);
        this.scaleDragHelper.onScale(zoomScale, this.mFocalX, this.mFocalY);
        if (z) {
            SketchUtils.postOnAnimation(this.imageZoomer.getImageView(), this);
        } else if (SLog.isLoggable(524290)) {
            SLog.d(ImageZoomer.NAME, "finished. zoom run");
        }
    }

    public void zoom() {
        this.imageZoomer.getImageView().post(this);
    }
}
