package basecamera.module.lib;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.widget.ImageView;
import basecamera.module.lib.listener.ErrorListener;
import basecamera.module.lib.util.AngleUtil;
import basecamera.module.lib.util.CameraLog;
import basecamera.module.lib.util.CameraParamUtil;
import basecamera.module.lib.util.CheckPermission;
import basecamera.module.lib.util.DeviceUtil;
import basecamera.module.lib.util.ScreenUtils;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.autonavi.amap.mapcore.AMapEngineUtils;
import com.autonavi.amap.mapcore.tools.GLMapStaticValue;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import kotlinx.coroutines.DebugKt;

/* loaded from: classes.dex */
public class CameraInterface implements Camera.PreviewCallback {
    public static int CAMERA_FRONT_POSITION = -1;
    public static int CAMERA_POST_POSITION = -1;
    private static final String TAG = "CJT";
    public static final int TYPE_CAPTURE = 145;
    public static final int TYPE_RECORDER = 144;
    private static volatile CameraInterface mCameraInterface;
    private int SELECTED_CAMERA;
    private ErrorListener errorLisenter;
    private byte[] firstFrame_data;
    private Camera mCamera;
    private ImageView mFlashLamp;
    private Camera.Parameters mParams;
    private ImageView mSwitchView;
    private MediaRecorder mediaRecorder;
    private int nowAngle;
    private int preview_height;
    private int preview_width;
    private String saveVideoPath;
    private String videoFileAbsPath;
    private String videoFileName;
    private boolean isPreviewing = false;
    private SurfaceHolder mHolder = null;
    private float screenProp = -1.0f;
    private boolean isRecorder = false;
    private Bitmap videoFirstFrame = null;
    private int angle = 0;
    private int cameraAngle = 90;
    private int rotation = 0;
    private int nowScaleRate = 0;
    private int recordScleRate = 0;
    private int mediaQuality = JCameraView.MEDIA_QUALITY_MIDDLE;
    private SensorManager sm = null;
    private SensorEventListener sensorEventListener = new SensorEventListener() { // from class: basecamera.module.lib.CameraInterface.1
        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (1 != sensorEvent.sensor.getType()) {
                return;
            }
            float[] fArr = sensorEvent.values;
            CameraInterface.this.angle = AngleUtil.getSensorAngle(fArr[0], fArr[1]);
            CameraInterface.this.rotationAnimation();
        }
    };
    private boolean canTakePhoto = true;
    int handlerTime = 0;

    /* loaded from: classes.dex */
    public interface CameraOpenOverCallback {
        void cameraHasOpened();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface ErrorCallback {
        void onError();
    }

    /* loaded from: classes.dex */
    public interface FocusCallback {
        void focusSuccess();
    }

    /* loaded from: classes.dex */
    public interface StopRecordCallback {
        void recordResult(String str, Bitmap bitmap);
    }

    /* loaded from: classes.dex */
    public interface TakePictureCallback {
        void captureResult(Bitmap bitmap, boolean z);
    }

    private CameraInterface() {
        this.SELECTED_CAMERA = -1;
        findAvailableCameras();
        this.SELECTED_CAMERA = CAMERA_POST_POSITION;
        this.saveVideoPath = "";
    }

    private static Rect calculateTapArea(float f, float f2, float f3, Context context) {
        int screenWidth = (int) (((f / ScreenUtils.getScreenWidth(context)) * 2000.0f) - 1000.0f);
        int screenHeight = (int) (((f2 / ScreenUtils.getScreenHeight(context)) * 2000.0f) - 1000.0f);
        int intValue = Float.valueOf(f3 * 300.0f).intValue() / 2;
        RectF rectF = new RectF(clamp(screenWidth - intValue, NotificationManagerCompat.IMPORTANCE_UNSPECIFIED, 1000), clamp(screenHeight - intValue, NotificationManagerCompat.IMPORTANCE_UNSPECIFIED, 1000), r2 + r4, r3 + r4);
        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    private static int clamp(int i, int i2, int i3) {
        return i > i3 ? i3 : i < i2 ? i2 : i;
    }

    public static void destroyCameraInterface() {
        if (mCameraInterface != null) {
            mCameraInterface = null;
        }
    }

    private void findAvailableCameras() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            switch (cameraInfo.facing) {
                case 0:
                    CAMERA_POST_POSITION = cameraInfo.facing;
                    break;
                case 1:
                    CAMERA_FRONT_POSITION = cameraInfo.facing;
                    break;
            }
        }
    }

    public static synchronized CameraInterface getInstance() {
        CameraInterface cameraInterface;
        synchronized (CameraInterface.class) {
            if (mCameraInterface == null) {
                synchronized (CameraInterface.class) {
                    if (mCameraInterface == null) {
                        mCameraInterface = new CameraInterface();
                    }
                }
            }
            cameraInterface = mCameraInterface;
        }
        return cameraInterface;
    }

    private synchronized void openCamera(int i) {
        try {
            this.mCamera = Camera.open(i);
        } catch (Exception e) {
            e.printStackTrace();
            if (this.errorLisenter != null) {
                this.errorLisenter.onError();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void rotationAnimation() {
        if (this.mSwitchView == null || this.rotation == this.angle) {
            return;
        }
        int i = this.rotation;
        int i2 = SubsamplingScaleImageView.ORIENTATION_270;
        int i3 = 180;
        int i4 = 90;
        if (i == 0) {
            int i5 = this.angle;
            if (i5 == 90) {
                i3 = -90;
            } else if (i5 == 270) {
                i3 = 90;
            }
            i4 = 0;
            float f = i4;
            float f2 = i3;
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mSwitchView, "rotation", f, f2);
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.mFlashLamp, "rotation", f, f2);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ofFloat, ofFloat2);
            animatorSet.setDuration(500L);
            animatorSet.start();
            this.rotation = this.angle;
        }
        if (i == 90) {
            int i6 = this.angle;
            i3 = (i6 == 0 || i6 != 180) ? 0 : AMapEngineUtils.MIN_LONGITUDE_DEGREE;
            i4 = -90;
        } else if (i == 180) {
            int i7 = this.angle;
            if (i7 != 90) {
                i2 = i7 != 270 ? 0 : 90;
            }
            i3 = i2;
            i4 = 180;
        } else if (i == 270) {
            int i8 = this.angle;
            if (i8 == 0 || i8 != 180) {
                i3 = 0;
            }
        }
        float f3 = i4;
        float f22 = i3;
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this.mSwitchView, "rotation", f3, f22);
        ObjectAnimator ofFloat22 = ObjectAnimator.ofFloat(this.mFlashLamp, "rotation", f3, f22);
        AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.playTogether(ofFloat3, ofFloat22);
        animatorSet2.setDuration(500L);
        animatorSet2.start();
        this.rotation = this.angle;
        i3 = 0;
        i4 = 0;
        float f32 = i4;
        float f222 = i3;
        ObjectAnimator ofFloat32 = ObjectAnimator.ofFloat(this.mSwitchView, "rotation", f32, f222);
        ObjectAnimator ofFloat222 = ObjectAnimator.ofFloat(this.mFlashLamp, "rotation", f32, f222);
        AnimatorSet animatorSet22 = new AnimatorSet();
        animatorSet22.playTogether(ofFloat32, ofFloat222);
        animatorSet22.setDuration(500L);
        animatorSet22.start();
        this.rotation = this.angle;
    }

    private void setFlashModel() {
        this.mParams = this.mCamera.getParameters();
        this.mParams.setFlashMode("torch");
        this.mCamera.setParameters(this.mParams);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void doDestroyCamera() {
        this.errorLisenter = null;
        if (this.mCamera == null) {
            Log.i(TAG, "=== Camera  Null===");
            return;
        }
        try {
            this.mCamera.setPreviewCallback(null);
            this.mSwitchView = null;
            this.mFlashLamp = null;
            this.mCamera.stopPreview();
            this.mCamera.setPreviewDisplay(null);
            this.mHolder = null;
            this.isPreviewing = false;
            this.mCamera.release();
            this.mCamera = null;
            Log.i(TAG, "=== Destroy Camera ===");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void doOpenCamera(CameraOpenOverCallback cameraOpenOverCallback) {
        if (Build.VERSION.SDK_INT < 23 && !CheckPermission.isCameraUseable(this.SELECTED_CAMERA) && this.errorLisenter != null) {
            this.errorLisenter.onError();
            return;
        }
        if (this.mCamera == null) {
            openCamera(this.SELECTED_CAMERA);
        }
        cameraOpenOverCallback.cameraHasOpened();
    }

    public void doStartPreview(SurfaceHolder surfaceHolder, float f) {
        if (this.isPreviewing) {
            CameraLog.i("doStartPreview isPreviewing");
        }
        if (this.screenProp < 0.0f) {
            this.screenProp = f;
        }
        if (surfaceHolder == null) {
            return;
        }
        this.mHolder = surfaceHolder;
        if (this.mCamera != null) {
            try {
                this.mParams = this.mCamera.getParameters();
                Camera.Size previewSize = CameraParamUtil.getInstance().getPreviewSize(this.mParams.getSupportedPreviewSizes(), 1000, f);
                Camera.Size pictureSize = CameraParamUtil.getInstance().getPictureSize(this.mParams.getSupportedPictureSizes(), 1200, f);
                this.mParams.setPreviewSize(previewSize.width, previewSize.height);
                this.preview_width = previewSize.width;
                this.preview_height = previewSize.height;
                this.mParams.setPictureSize(pictureSize.width, pictureSize.height);
                if (CameraParamUtil.getInstance().isSupportedFocusMode(this.mParams.getSupportedFocusModes(), DebugKt.DEBUG_PROPERTY_VALUE_AUTO)) {
                    this.mParams.setFocusMode(DebugKt.DEBUG_PROPERTY_VALUE_AUTO);
                }
                if (CameraParamUtil.getInstance().isSupportedPictureFormats(this.mParams.getSupportedPictureFormats(), 256)) {
                    this.mParams.setPictureFormat(256);
                    this.mParams.setJpegQuality(100);
                }
                this.mCamera.setParameters(this.mParams);
                this.mParams = this.mCamera.getParameters();
                this.mCamera.setPreviewDisplay(surfaceHolder);
                this.mCamera.setDisplayOrientation(this.cameraAngle);
                this.mCamera.setPreviewCallback(this);
                this.mCamera.startPreview();
                this.isPreviewing = true;
                Log.i(TAG, "=== Start Preview ===");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void doStopPreview() {
        if (this.mCamera != null) {
            try {
                this.mCamera.setPreviewCallback(null);
                this.mCamera.stopPreview();
                this.mCamera.setPreviewDisplay(null);
                this.isPreviewing = false;
                Log.i(TAG, "=== Stop Preview ===");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getSELECTED_CAMERA() {
        return this.SELECTED_CAMERA;
    }

    public void handleFocus(final Context context, final float f, final float f2, final FocusCallback focusCallback) {
        if (this.mCamera == null) {
            return;
        }
        Camera.Parameters parameters = this.mCamera.getParameters();
        Rect calculateTapArea = calculateTapArea(f, f2, 1.0f, context);
        this.mCamera.cancelAutoFocus();
        if (parameters.getMaxNumFocusAreas() <= 0) {
            Log.i(TAG, "focus areas not supported");
            focusCallback.focusSuccess();
            return;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new Camera.Area(calculateTapArea, GLMapStaticValue.ANIMATION_MOVE_TIME));
        parameters.setFocusAreas(arrayList);
        final String focusMode = parameters.getFocusMode();
        try {
            parameters.setFocusMode(DebugKt.DEBUG_PROPERTY_VALUE_AUTO);
            this.mCamera.setParameters(parameters);
            this.mCamera.autoFocus(new Camera.AutoFocusCallback() { // from class: basecamera.module.lib.CameraInterface.4
                @Override // android.hardware.Camera.AutoFocusCallback
                public void onAutoFocus(boolean z, Camera camera) {
                    if (!z && CameraInterface.this.handlerTime <= 10) {
                        CameraInterface.this.handlerTime++;
                        CameraInterface.this.handleFocus(context, f, f2, focusCallback);
                    } else {
                        Camera.Parameters parameters2 = camera.getParameters();
                        parameters2.setFocusMode(focusMode);
                        camera.setParameters(parameters2);
                        CameraInterface.this.handlerTime = 0;
                        focusCallback.focusSuccess();
                    }
                }
            });
        } catch (Exception unused) {
            Log.e(TAG, "autoFocus failer");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void isPreview(boolean z) {
        this.isPreviewing = z;
    }

    @Override // android.hardware.Camera.PreviewCallback
    public void onPreviewFrame(byte[] bArr, Camera camera) {
        this.firstFrame_data = bArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerSensorManager(Context context) {
        if (this.sm == null) {
            this.sm = (SensorManager) context.getSystemService("sensor");
        }
        this.sm.registerListener(this.sensorEventListener, this.sm.getDefaultSensor(1), 3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setErrorLinsenter(ErrorListener errorListener) {
        this.errorLisenter = errorListener;
    }

    public void setFlashMode(String str) {
        if (this.mCamera == null) {
            return;
        }
        Camera.Parameters parameters = this.mCamera.getParameters();
        parameters.setFlashMode(str);
        this.mCamera.setParameters(parameters);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setMediaQuality(int i) {
        this.mediaQuality = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSaveVideoPath(String str) {
        this.saveVideoPath = str;
        File file = new File(str);
        if (file.exists()) {
            return;
        }
        file.mkdirs();
    }

    public void setSwitchView(ImageView imageView, ImageView imageView2) {
        this.mSwitchView = imageView;
        this.mFlashLamp = imageView2;
        if (imageView != null) {
            this.cameraAngle = CameraParamUtil.getInstance().getCameraDisplayOrientation(imageView.getContext(), this.SELECTED_CAMERA);
        }
    }

    public void setZoom(float f, int i) {
        int i2;
        if (this.mCamera == null) {
            return;
        }
        if (this.mParams == null) {
            this.mParams = this.mCamera.getParameters();
        }
        if (this.mParams.isZoomSupported() && this.mParams.isSmoothZoomSupported()) {
            switch (i) {
                case TYPE_RECORDER /* 144 */:
                    if (this.isRecorder && f >= 0.0f && (i2 = (int) (f / 40.0f)) <= this.mParams.getMaxZoom() && i2 >= this.nowScaleRate && this.recordScleRate != i2) {
                        this.mParams.setZoom(i2);
                        this.mCamera.setParameters(this.mParams);
                        this.recordScleRate = i2;
                        return;
                    }
                    return;
                case TYPE_CAPTURE /* 145 */:
                    if (this.isRecorder) {
                        return;
                    }
                    int i3 = (int) (f / 50.0f);
                    if (i3 < this.mParams.getMaxZoom()) {
                        this.nowScaleRate += i3;
                        if (this.nowScaleRate < 0) {
                            this.nowScaleRate = 0;
                        } else if (this.nowScaleRate > this.mParams.getMaxZoom()) {
                            this.nowScaleRate = this.mParams.getMaxZoom();
                        }
                        this.mParams.setZoom(this.nowScaleRate);
                        this.mCamera.setParameters(this.mParams);
                    }
                    CameraLog.i("setZoom = " + this.nowScaleRate);
                    return;
                default:
                    return;
            }
        }
    }

    public void startRecord(Surface surface, float f, ErrorCallback errorCallback) {
        this.mCamera.setPreviewCallback(null);
        int i = (this.angle + 90) % SpatialRelationUtil.A_CIRCLE_DEGREE;
        Camera.Parameters parameters = this.mCamera.getParameters();
        int i2 = parameters.getPreviewSize().width;
        int i3 = parameters.getPreviewSize().height;
        YuvImage yuvImage = new YuvImage(this.firstFrame_data, parameters.getPreviewFormat(), i2, i3, null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, i2, i3), 50, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        this.videoFirstFrame = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        Matrix matrix = new Matrix();
        if (this.SELECTED_CAMERA == CAMERA_POST_POSITION) {
            matrix.setRotate(i);
        } else if (this.SELECTED_CAMERA == CAMERA_FRONT_POSITION) {
            matrix.setRotate(270.0f);
        }
        this.videoFirstFrame = Bitmap.createBitmap(this.videoFirstFrame, 0, 0, this.videoFirstFrame.getWidth(), this.videoFirstFrame.getHeight(), matrix, true);
        if (this.isRecorder) {
            return;
        }
        if (this.mCamera == null) {
            openCamera(this.SELECTED_CAMERA);
        }
        if (this.mediaRecorder == null) {
            this.mediaRecorder = new MediaRecorder();
        }
        if (this.mParams == null) {
            this.mParams = this.mCamera.getParameters();
        }
        if (this.mParams.getSupportedFocusModes().contains("continuous-video")) {
            this.mParams.setFocusMode("continuous-video");
        }
        this.mCamera.setParameters(this.mParams);
        this.mCamera.unlock();
        this.mediaRecorder.reset();
        this.mediaRecorder.setCamera(this.mCamera);
        this.mediaRecorder.setVideoSource(1);
        this.mediaRecorder.setAudioSource(1);
        this.mediaRecorder.setOutputFormat(2);
        this.mediaRecorder.setVideoEncoder(2);
        this.mediaRecorder.setAudioEncoder(3);
        Camera.Size previewSize = this.mParams.getSupportedVideoSizes() == null ? CameraParamUtil.getInstance().getPreviewSize(this.mParams.getSupportedPreviewSizes(), 600, f) : CameraParamUtil.getInstance().getPreviewSize(this.mParams.getSupportedVideoSizes(), 600, f);
        Log.i(TAG, "setVideoSize    width = " + previewSize.width + "height = " + previewSize.height);
        if (previewSize.width == previewSize.height) {
            this.mediaRecorder.setVideoSize(this.preview_width, this.preview_height);
        } else {
            this.mediaRecorder.setVideoSize(previewSize.width, previewSize.height);
        }
        if (this.SELECTED_CAMERA != CAMERA_FRONT_POSITION) {
            this.mediaRecorder.setOrientationHint(i);
        } else if (this.cameraAngle == 270) {
            if (i == 0) {
                this.mediaRecorder.setOrientationHint(180);
            } else if (i == 270) {
                this.mediaRecorder.setOrientationHint(SubsamplingScaleImageView.ORIENTATION_270);
            } else {
                this.mediaRecorder.setOrientationHint(90);
            }
        } else if (i == 90) {
            this.mediaRecorder.setOrientationHint(SubsamplingScaleImageView.ORIENTATION_270);
        } else if (i == 270) {
            this.mediaRecorder.setOrientationHint(90);
        } else {
            this.mediaRecorder.setOrientationHint(i);
        }
        if (DeviceUtil.isHuaWeiRongyao()) {
            this.mediaRecorder.setVideoEncodingBitRate(JCameraView.MEDIA_QUALITY_FUNNY);
        } else {
            this.mediaRecorder.setVideoEncodingBitRate(this.mediaQuality);
        }
        this.mediaRecorder.setPreviewDisplay(surface);
        this.videoFileName = "video_" + System.currentTimeMillis() + PictureFileUtils.POST_VIDEO;
        if (this.saveVideoPath.equals("")) {
            this.saveVideoPath = Environment.getExternalStorageDirectory().getPath();
        }
        this.videoFileAbsPath = this.saveVideoPath + File.separator + this.videoFileName;
        this.mediaRecorder.setOutputFile(this.videoFileAbsPath);
        try {
            this.mediaRecorder.prepare();
            this.mediaRecorder.start();
            this.isRecorder = true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "startRecord IOException");
            if (this.errorLisenter != null) {
                this.errorLisenter.onError();
            }
        } catch (IllegalStateException e2) {
            e2.printStackTrace();
            Log.i(TAG, "startRecord IllegalStateException");
            if (this.errorLisenter != null) {
                this.errorLisenter.onError();
            }
        } catch (RuntimeException unused) {
            Log.i(TAG, "startRecord RuntimeException");
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0041, code lost:
    
        if (r4 == false) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0049, code lost:
    
        if (basecamera.module.lib.util.FileUtil.deleteFile(r3.videoFileAbsPath) == false) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x004b, code lost:
    
        r5.recordResult(null, null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x004e, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x004f, code lost:
    
        doStopPreview();
        r5.recordResult(r3.saveVideoPath + java.io.File.separator + r3.videoFileName, r3.videoFirstFrame);
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x003e, code lost:
    
        if (r3.mediaRecorder == null) goto L12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void stopRecord(boolean r4, basecamera.module.lib.CameraInterface.StopRecordCallback r5) {
        /*
            r3 = this;
            boolean r0 = r3.isRecorder
            if (r0 != 0) goto L5
            return
        L5:
            android.media.MediaRecorder r0 = r3.mediaRecorder
            if (r0 == 0) goto L7e
            android.media.MediaRecorder r0 = r3.mediaRecorder
            r1 = 0
            r0.setOnErrorListener(r1)
            android.media.MediaRecorder r0 = r3.mediaRecorder
            r0.setOnInfoListener(r1)
            android.media.MediaRecorder r0 = r3.mediaRecorder
            r0.setPreviewDisplay(r1)
            r0 = 0
            android.media.MediaRecorder r2 = r3.mediaRecorder     // Catch: java.lang.Throwable -> L2d java.lang.RuntimeException -> L2f
            r2.stop()     // Catch: java.lang.Throwable -> L2d java.lang.RuntimeException -> L2f
            android.media.MediaRecorder r2 = r3.mediaRecorder
            if (r2 == 0) goto L28
        L23:
            android.media.MediaRecorder r2 = r3.mediaRecorder
            r2.release()
        L28:
            r3.mediaRecorder = r1
            r3.isRecorder = r0
            goto L41
        L2d:
            r4 = move-exception
            goto L70
        L2f:
            r2 = move-exception
            r2.printStackTrace()     // Catch: java.lang.Throwable -> L2d
            r3.mediaRecorder = r1     // Catch: java.lang.Throwable -> L2d
            android.media.MediaRecorder r2 = new android.media.MediaRecorder     // Catch: java.lang.Throwable -> L2d
            r2.<init>()     // Catch: java.lang.Throwable -> L2d
            r3.mediaRecorder = r2     // Catch: java.lang.Throwable -> L2d
            android.media.MediaRecorder r2 = r3.mediaRecorder
            if (r2 == 0) goto L28
            goto L23
        L41:
            if (r4 == 0) goto L4f
            java.lang.String r4 = r3.videoFileAbsPath
            boolean r4 = basecamera.module.lib.util.FileUtil.deleteFile(r4)
            if (r4 == 0) goto L4e
            r5.recordResult(r1, r1)
        L4e:
            return
        L4f:
            r3.doStopPreview()
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r0 = r3.saveVideoPath
            r4.append(r0)
            java.lang.String r0 = java.io.File.separator
            r4.append(r0)
            java.lang.String r0 = r3.videoFileName
            r4.append(r0)
            java.lang.String r4 = r4.toString()
            android.graphics.Bitmap r0 = r3.videoFirstFrame
            r5.recordResult(r4, r0)
            goto L7e
        L70:
            android.media.MediaRecorder r5 = r3.mediaRecorder
            if (r5 == 0) goto L79
            android.media.MediaRecorder r5 = r3.mediaRecorder
            r5.release()
        L79:
            r3.mediaRecorder = r1
            r3.isRecorder = r0
            throw r4
        L7e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: basecamera.module.lib.CameraInterface.stopRecord(boolean, basecamera.module.lib.CameraInterface$StopRecordCallback):void");
    }

    public synchronized void switchCamera(SurfaceHolder surfaceHolder, float f) {
        if (this.SELECTED_CAMERA == CAMERA_POST_POSITION) {
            this.SELECTED_CAMERA = CAMERA_FRONT_POSITION;
        } else {
            this.SELECTED_CAMERA = CAMERA_POST_POSITION;
        }
        doDestroyCamera();
        CameraLog.i("open start");
        openCamera(this.SELECTED_CAMERA);
        CameraLog.i("open end");
        doStartPreview(surfaceHolder, f);
    }

    public void takePicture(final TakePictureCallback takePictureCallback) {
        if (this.mCamera == null) {
            return;
        }
        int i = this.cameraAngle;
        if (i == 90) {
            this.nowAngle = Math.abs(this.angle + this.cameraAngle) % SpatialRelationUtil.A_CIRCLE_DEGREE;
        } else if (i == 270) {
            this.nowAngle = Math.abs(this.cameraAngle - this.angle);
        }
        Log.i(TAG, this.angle + " = " + this.cameraAngle + " = " + this.nowAngle);
        if (this.canTakePhoto) {
            this.mCamera.takePicture(new Camera.ShutterCallback() { // from class: basecamera.module.lib.CameraInterface.2
                @Override // android.hardware.Camera.ShutterCallback
                public void onShutter() {
                }
            }, null, new Camera.PictureCallback() { // from class: basecamera.module.lib.CameraInterface.3
                @Override // android.hardware.Camera.PictureCallback
                public void onPictureTaken(final byte[] bArr, Camera camera) {
                    new Thread(new Runnable() { // from class: basecamera.module.lib.CameraInterface.3.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
                            Matrix matrix = new Matrix();
                            if (CameraInterface.this.SELECTED_CAMERA == CameraInterface.CAMERA_POST_POSITION) {
                                matrix.setRotate(CameraInterface.this.nowAngle);
                            } else if (CameraInterface.this.SELECTED_CAMERA == CameraInterface.CAMERA_FRONT_POSITION) {
                                matrix.setRotate(360 - CameraInterface.this.nowAngle);
                                matrix.postScale(-1.0f, 1.0f);
                            }
                            Bitmap createBitmap = Bitmap.createBitmap(decodeByteArray, 0, 0, decodeByteArray.getWidth(), decodeByteArray.getHeight(), matrix, true);
                            if (takePictureCallback != null) {
                                if (CameraInterface.this.nowAngle == 90 || CameraInterface.this.nowAngle == 270) {
                                    takePictureCallback.captureResult(createBitmap, true);
                                } else {
                                    takePictureCallback.captureResult(createBitmap, false);
                                }
                            }
                            CameraInterface.this.canTakePhoto = true;
                        }
                    }).start();
                }
            });
            this.canTakePhoto = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterSensorManager(Context context) {
        if (this.sm == null) {
            this.sm = (SensorManager) context.getSystemService("sensor");
        }
        this.sm.unregisterListener(this.sensorEventListener);
    }
}
