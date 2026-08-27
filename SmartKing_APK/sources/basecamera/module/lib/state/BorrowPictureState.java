package basecamera.module.lib.state;

import android.view.Surface;
import android.view.SurfaceHolder;
import basecamera.module.lib.CameraInterface;
import basecamera.module.lib.util.CameraLog;

/* loaded from: classes.dex */
public class BorrowPictureState implements State {
    private final String TAG = "BorrowPictureState";
    private CameraMachine machine;

    public BorrowPictureState(CameraMachine cameraMachine) {
        this.machine = cameraMachine;
    }

    @Override // basecamera.module.lib.state.State
    public void cancle(SurfaceHolder surfaceHolder, float f) {
        CameraInterface.getInstance().doStartPreview(surfaceHolder, f);
        this.machine.getView().resetState(1);
        this.machine.setState(this.machine.getPreviewState());
    }

    @Override // basecamera.module.lib.state.State
    public void capture() {
    }

    @Override // basecamera.module.lib.state.State
    public void confirm() {
        this.machine.getView().confirmState(1);
        this.machine.setState(this.machine.getPreviewState());
    }

    @Override // basecamera.module.lib.state.State
    public void flash(String str) {
    }

    @Override // basecamera.module.lib.state.State
    public void foucs(float f, float f2, CameraInterface.FocusCallback focusCallback) {
    }

    @Override // basecamera.module.lib.state.State
    public void record(Surface surface, float f) {
    }

    @Override // basecamera.module.lib.state.State
    public void restart() {
    }

    @Override // basecamera.module.lib.state.State
    public void start(SurfaceHolder surfaceHolder, float f) {
        CameraInterface.getInstance().doStartPreview(surfaceHolder, f);
        this.machine.setState(this.machine.getPreviewState());
    }

    @Override // basecamera.module.lib.state.State
    public void stop() {
    }

    @Override // basecamera.module.lib.state.State
    public void stopRecord(boolean z, long j) {
    }

    @Override // basecamera.module.lib.state.State
    public void swtich(SurfaceHolder surfaceHolder, float f) {
    }

    @Override // basecamera.module.lib.state.State
    public void zoom(float f, int i) {
        CameraLog.i("BorrowPictureState", "zoom");
    }
}
