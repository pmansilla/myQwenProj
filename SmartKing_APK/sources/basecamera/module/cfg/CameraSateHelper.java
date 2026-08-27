package basecamera.module.cfg;

import java.util.HashSet;
import java.util.Iterator;

/* loaded from: classes.dex */
public class CameraSateHelper {
    private static final CameraSateHelper ourInstance = new CameraSateHelper();
    private HashSet<CameraCallback> cameraCallbackHashSet = new HashSet<>();

    /* loaded from: classes.dex */
    public interface CameraCallback {
        void onTakePhotoFailure(int i);

        void onTakePhotoSuccess(String str);
    }

    private CameraSateHelper() {
    }

    public static CameraSateHelper getInstance() {
        return ourInstance;
    }

    public void notifyFailure(int i) {
        Iterator<CameraCallback> it = this.cameraCallbackHashSet.iterator();
        while (it.hasNext()) {
            it.next().onTakePhotoFailure(i);
        }
    }

    public void notifySuccess(String str) {
        Iterator<CameraCallback> it = this.cameraCallbackHashSet.iterator();
        while (it.hasNext()) {
            it.next().onTakePhotoSuccess(str);
        }
    }

    public void registerCallback(CameraCallback cameraCallback) {
        if (this.cameraCallbackHashSet.contains(cameraCallback)) {
            return;
        }
        this.cameraCallbackHashSet.add(cameraCallback);
    }

    public void unRegisterCallback(CameraCallback cameraCallback) {
        if (this.cameraCallbackHashSet.contains(cameraCallback)) {
            this.cameraCallbackHashSet.remove(cameraCallback);
        }
    }
}
