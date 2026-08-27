package basecamera.module.lib.util;

import android.hardware.Camera;
import android.media.AudioRecord;
import android.util.Log;

/* loaded from: classes.dex */
public class CheckPermission {
    public static final int STATE_NO_PERMISSION = -2;
    public static final int STATE_RECORDING = -1;
    public static final int STATE_SUCCESS = 1;

    public static int getRecordState() {
        int minBufferSize = AudioRecord.getMinBufferSize(44100, 16, 2);
        AudioRecord audioRecord = new AudioRecord(0, 44100, 16, 2, minBufferSize * 100);
        short[] sArr = new short[minBufferSize];
        try {
            audioRecord.startRecording();
            if (audioRecord.getRecordingState() != 3) {
                audioRecord.stop();
                audioRecord.release();
                Log.d("CheckAudioPermission", "录音机被占用");
                return -1;
            }
            if (audioRecord.read(sArr, 0, sArr.length) > 0) {
                audioRecord.stop();
                audioRecord.release();
                return 1;
            }
            audioRecord.stop();
            audioRecord.release();
            Log.d("CheckAudioPermission", "录音的结果为空");
            return -2;
        } catch (Exception unused) {
            audioRecord.release();
            return -2;
        }
    }

    public static synchronized boolean isCameraUseable(int i) {
        boolean z;
        Camera camera;
        synchronized (CheckPermission.class) {
            Camera camera2 = null;
            z = false;
            try {
                try {
                    try {
                        camera = Camera.open(i);
                    } catch (Exception e) {
                        e = e;
                    }
                } catch (Throwable th) {
                    th = th;
                    camera = camera2;
                }
                try {
                    camera.setParameters(camera.getParameters());
                    if (camera != null) {
                        camera.release();
                        z = true;
                    }
                } catch (Exception e2) {
                    e = e2;
                    camera2 = camera;
                    e.printStackTrace();
                    if (camera2 != null) {
                        camera2.release();
                    }
                    return z;
                } catch (Throwable th2) {
                    th = th2;
                    if (camera != null) {
                        camera.release();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                throw th3;
            }
        }
        return z;
    }
}
