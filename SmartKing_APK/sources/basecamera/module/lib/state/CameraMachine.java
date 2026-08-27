package basecamera.module.lib.state;

import android.content.Context;
import android.view.Surface;
import android.view.SurfaceHolder;
import basecamera.module.lib.CameraInterface;
import basecamera.module.lib.util.CameraLog;
import basecamera.module.lib.view.CameraView;

/* loaded from: classes.dex */
public class CameraMachine implements State {
    private Context context;
    private CameraView view;
    private State previewState = new PreviewState(this);
    private State borrowPictureState = new BorrowPictureState(this);
    private State borrowVideoState = new BorrowVideoState(this);
    private State state = this.previewState;

    public CameraMachine(Context context, CameraView cameraView, CameraInterface.CameraOpenOverCallback cameraOpenOverCallback) {
        this.context = context;
        this.view = cameraView;
    }

    @Override // basecamera.module.lib.state.State
    public void cancle(SurfaceHolder surfaceHolder, float f) {
        this.state.cancle(surfaceHolder, f);
    }

    @Override // basecamera.module.lib.state.State
    public void capture() {
        this.state.capture();
    }

    @Override // basecamera.module.lib.state.State
    public void confirm() {
        this.state.confirm();
    }

    @Override // basecamera.module.lib.state.State
    public void flash(String str) {
        this.state.flash(str);
    }

    @Override // basecamera.module.lib.state.State
    public void foucs(float f, float f2, CameraInterface.FocusCallback focusCallback) {
        this.state.foucs(f, f2, focusCallback);
    }

    public State getBorrowPictureState() {
        return this.borrowPictureState;
    }

    public State getBorrowVideoState() {
        return this.borrowVideoState;
    }

    public Context getContext() {
        return this.context;
    }

    public State getPreviewState() {
        return this.previewState;
    }

    public State getState() {
        return this.state;
    }

    public CameraView getView() {
        return this.view;
    }

    public boolean isBackgroundCamera() {
        return CameraInterface.getInstance().getSELECTED_CAMERA() == CameraInterface.CAMERA_POST_POSITION;
    }

    @Override // basecamera.module.lib.state.State
    public void record(Surface surface, float f) {
        this.state.record(surface, f);
    }

    @Override // basecamera.module.lib.state.State
    public void restart() {
        this.state.restart();
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override // basecamera.module.lib.state.State
    public void start(SurfaceHolder surfaceHolder, float f) {
        this.state.start(surfaceHolder, f);
    }

    @Override // basecamera.module.lib.state.State
    public void stop() {
        this.state.stop();
    }

    @Override // basecamera.module.lib.state.State
    public void stopRecord(boolean z, long j) {
        this.state.stopRecord(z, j);
    }

    @Override // basecamera.module.lib.state.State
    public void swtich(SurfaceHolder surfaceHolder, float f) {
        this.state.swtich(surfaceHolder, f);
        CameraLog.e("CameraInterface====>" + CameraInterface.getInstance().getSELECTED_CAMERA());
    }

    @Override // basecamera.module.lib.state.State
    public void zoom(float f, int i) {
        this.state.zoom(f, i);
    }
}
