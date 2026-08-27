package basecamera.module.lib.state;

import android.graphics.Bitmap;
import android.view.Surface;
import android.view.SurfaceHolder;
import basecamera.module.lib.CameraInterface;
import basecamera.module.lib.util.CameraLog;

/* loaded from: classes.dex */
class PreviewState implements State {
    public static final String TAG = "PreviewState";
    private CameraMachine machine;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PreviewState(CameraMachine cameraMachine) {
        this.machine = cameraMachine;
    }

    @Override // basecamera.module.lib.state.State
    public void cancle(SurfaceHolder surfaceHolder, float f) {
        CameraLog.i("浏览状态下,没有 cancle 事件");
    }

    @Override // basecamera.module.lib.state.State
    public void capture() {
        CameraInterface.getInstance().takePicture(new CameraInterface.TakePictureCallback() { // from class: basecamera.module.lib.state.PreviewState.1
            @Override // basecamera.module.lib.CameraInterface.TakePictureCallback
            public void captureResult(Bitmap bitmap, boolean z) {
                CameraLog.e("拍照的结果回调");
                PreviewState.this.machine.getView().showPicture(bitmap, z);
            }
        });
    }

    @Override // basecamera.module.lib.state.State
    public void confirm() {
        CameraLog.e("浏览状态下,没有 confirm 事件");
    }

    @Override // basecamera.module.lib.state.State
    public void flash(String str) {
        CameraInterface.getInstance().setFlashMode(str);
    }

    @Override // basecamera.module.lib.state.State
    public void foucs(float f, float f2, CameraInterface.FocusCallback focusCallback) {
        CameraLog.i("preview state foucs");
        if (this.machine.getView().handlerFoucs(f, f2)) {
            CameraInterface.getInstance().handleFocus(this.machine.getContext(), f, f2, focusCallback);
        }
    }

    @Override // basecamera.module.lib.state.State
    public void record(Surface surface, float f) {
        CameraInterface.getInstance().startRecord(surface, f, null);
    }

    @Override // basecamera.module.lib.state.State
    public void restart() {
    }

    @Override // basecamera.module.lib.state.State
    public void start(SurfaceHolder surfaceHolder, float f) {
        CameraInterface.getInstance().doStartPreview(surfaceHolder, f);
    }

    @Override // basecamera.module.lib.state.State
    public void stop() {
        CameraInterface.getInstance().doStopPreview();
    }

    @Override // basecamera.module.lib.state.State
    public void stopRecord(final boolean z, long j) {
        CameraInterface.getInstance().stopRecord(z, new CameraInterface.StopRecordCallback() { // from class: basecamera.module.lib.state.PreviewState.2
            @Override // basecamera.module.lib.CameraInterface.StopRecordCallback
            public void recordResult(String str, Bitmap bitmap) {
                if (z) {
                    PreviewState.this.machine.getView().resetState(3);
                } else {
                    PreviewState.this.machine.getView().playVideo(bitmap, str);
                    PreviewState.this.machine.setState(PreviewState.this.machine.getBorrowVideoState());
                }
            }
        });
    }

    @Override // basecamera.module.lib.state.State
    public void swtich(SurfaceHolder surfaceHolder, float f) {
        CameraInterface.getInstance().switchCamera(surfaceHolder, f);
    }

    @Override // basecamera.module.lib.state.State
    public void zoom(float f, int i) {
        CameraLog.i(TAG, "zoom");
        CameraInterface.getInstance().setZoom(f, i);
    }
}
