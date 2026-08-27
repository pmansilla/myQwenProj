package basecamera.module.lib;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.VideoView;
import basecamera.module.lib.CameraInterface;
import basecamera.module.lib.listener.CaptureListener;
import basecamera.module.lib.listener.ClickListener;
import basecamera.module.lib.listener.ErrorListener;
import basecamera.module.lib.listener.JCameraListener;
import basecamera.module.lib.listener.TypeListener;
import basecamera.module.lib.state.CameraMachine;
import basecamera.module.lib.util.CameraLog;
import basecamera.module.lib.util.FileUtil;
import basecamera.module.lib.util.ScreenUtils;
import basecamera.module.lib.view.CameraView;
import basecamera.module.utils.PreferencesUtils;
import java.io.IOException;
import kotlinx.coroutines.DebugKt;

/* loaded from: classes.dex */
public class JCameraView extends FrameLayout implements CameraInterface.CameraOpenOverCallback, SurfaceHolder.Callback, CameraView {
    public static final int BUTTON_STATE_BOTH = 259;
    public static final int BUTTON_STATE_ONLY_CAPTURE = 257;
    public static final int BUTTON_STATE_ONLY_RECORDER = 258;
    public static final int MEDIA_QUALITY_DESPAIR = 200000;
    public static final int MEDIA_QUALITY_FUNNY = 400000;
    public static final int MEDIA_QUALITY_HIGH = 2000000;
    public static final int MEDIA_QUALITY_LOW = 1200000;
    public static final int MEDIA_QUALITY_MIDDLE = 1600000;
    public static final int MEDIA_QUALITY_POOR = 800000;
    public static final int MEDIA_QUALITY_SORRY = 80000;
    public static final int TYPE_DEFAULT = 4;
    private static final int TYPE_FLASH_AUTO = 33;
    private static final int TYPE_FLASH_OFF = 35;
    private static final int TYPE_FLASH_ON = 34;
    public static final int TYPE_PICTURE = 1;
    public static final int TYPE_SHORT = 3;
    public static final int TYPE_VIDEO = 2;
    private Bitmap captureBitmap;
    private int duration;
    private ErrorListener errorLisenter;
    private Bitmap firstFrame;
    private boolean firstTouch;
    private float firstTouchLength;
    private int iconLeft;
    private int iconMargin;
    private int iconRight;
    private int iconSize;
    private int iconSrc;
    private JCameraListener jCameraLisenter;
    private int layout_width;
    private ClickListener leftClickListener;
    private CaptureLayout mCaptureLayout;
    private Context mContext;
    private ImageView mFlashLamp;
    private FoucsView mFoucsView;
    private MediaPlayer mMediaPlayer;
    private ImageView mPhoto;
    private ImageView mSwitchCamera;
    private VideoView mVideoView;
    private CameraMachine machine;
    private OnCameraSomeStateListener onCameraSomeStateListener;
    private ClickListener rightClickListener;
    private float screenProp;
    private int type_flash;
    private String videoUrl;
    private int zoomGradient;

    /* loaded from: classes.dex */
    public interface OnCameraSomeStateListener {
        void onBeforeTakePhoto();
    }

    public JCameraView(Context context) {
        this(context, null);
    }

    public JCameraView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public JCameraView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.type_flash = 35;
        this.screenProp = 0.0f;
        this.iconSize = 0;
        this.iconMargin = 0;
        this.iconSrc = 0;
        this.iconLeft = 0;
        this.iconRight = 0;
        this.duration = 0;
        this.zoomGradient = 0;
        this.firstTouch = true;
        this.firstTouchLength = 0.0f;
        this.mContext = context;
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.JCameraView, i, 0);
        this.iconSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.JCameraView_iconSize, (int) TypedValue.applyDimension(2, 35.0f, getResources().getDisplayMetrics()));
        this.iconMargin = obtainStyledAttributes.getDimensionPixelSize(R.styleable.JCameraView_iconMargin, (int) TypedValue.applyDimension(2, 15.0f, getResources().getDisplayMetrics()));
        this.iconSrc = obtainStyledAttributes.getResourceId(R.styleable.JCameraView_iconSrc, R.mipmap.basecamera_icon_flip);
        this.iconLeft = obtainStyledAttributes.getResourceId(R.styleable.JCameraView_iconLeft, R.mipmap.basecamera_icon_back);
        this.iconRight = obtainStyledAttributes.getResourceId(R.styleable.JCameraView_iconRight, R.mipmap.basecamera_icon_gallery);
        this.duration = obtainStyledAttributes.getInteger(R.styleable.JCameraView_duration_max, 10000);
        obtainStyledAttributes.recycle();
        initData();
        initView();
    }

    static /* synthetic */ int access$008(JCameraView jCameraView) {
        int i = jCameraView.type_flash;
        jCameraView.type_flash = i + 1;
        return i;
    }

    private void initData() {
        this.layout_width = ScreenUtils.getScreenWidth(this.mContext);
        this.zoomGradient = (int) (this.layout_width / 16.0f);
        CameraLog.i("zoom = " + this.zoomGradient);
        this.machine = new CameraMachine(getContext(), this, this);
    }

    private void initView() {
        setWillNotDraw(false);
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.basecamera_camera_view, this);
        this.mVideoView = (VideoView) inflate.findViewById(R.id.video_preview);
        this.mPhoto = (ImageView) inflate.findViewById(R.id.image_photo);
        this.mSwitchCamera = (ImageView) inflate.findViewById(R.id.image_switch);
        this.mSwitchCamera.setImageResource(this.iconSrc);
        this.mFlashLamp = (ImageView) inflate.findViewById(R.id.image_flash);
        this.type_flash = PreferencesUtils.getInstance().getInt(getContext(), "flash", 35);
        setFlashRes();
        this.mFlashLamp.setOnClickListener(new View.OnClickListener() { // from class: basecamera.module.lib.JCameraView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                JCameraView.access$008(JCameraView.this);
                if (JCameraView.this.type_flash > 35) {
                    JCameraView.this.type_flash = 33;
                }
                PreferencesUtils.getInstance().putInt(JCameraView.this.getContext(), "flash", JCameraView.this.type_flash);
                JCameraView.this.setFlashRes();
            }
        });
        this.mCaptureLayout = (CaptureLayout) inflate.findViewById(R.id.capture_layout);
        this.mCaptureLayout.setDuration(this.duration);
        this.mCaptureLayout.setIconSrc(this.iconLeft, this.iconRight);
        this.mFoucsView = (FoucsView) inflate.findViewById(R.id.fouce_view);
        this.mVideoView.getHolder().addCallback(this);
        if (this.machine.isBackgroundCamera()) {
            this.mFlashLamp.setClickable(true);
            this.mFlashLamp.setVisibility(0);
            this.type_flash = PreferencesUtils.getInstance().getInt(getContext(), "flash", 35);
            setFlashRes();
        } else {
            this.mFlashLamp.setClickable(false);
            this.mFlashLamp.setVisibility(4);
            this.type_flash = 35;
            setFlashRes();
        }
        this.mSwitchCamera.setOnClickListener(new View.OnClickListener() { // from class: basecamera.module.lib.JCameraView.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                JCameraView.this.machine.swtich(JCameraView.this.mVideoView.getHolder(), JCameraView.this.screenProp);
                if (!JCameraView.this.machine.isBackgroundCamera()) {
                    JCameraView.this.mFlashLamp.setClickable(false);
                    JCameraView.this.mFlashLamp.setVisibility(4);
                    JCameraView.this.type_flash = 35;
                    JCameraView.this.setFlashRes();
                    return;
                }
                JCameraView.this.mFlashLamp.setClickable(true);
                JCameraView.this.mFlashLamp.setVisibility(0);
                JCameraView.this.type_flash = PreferencesUtils.getInstance().getInt(JCameraView.this.getContext(), "flash", 35);
                JCameraView.this.setFlashRes();
            }
        });
        this.mCaptureLayout.setCaptureLisenter(new CaptureListener() { // from class: basecamera.module.lib.JCameraView.3
            @Override // basecamera.module.lib.listener.CaptureListener
            public void recordEnd(long j) {
                JCameraView.this.machine.stopRecord(false, j);
            }

            @Override // basecamera.module.lib.listener.CaptureListener
            public void recordError() {
                if (JCameraView.this.errorLisenter != null) {
                    JCameraView.this.errorLisenter.AudioPermissionError();
                }
            }

            @Override // basecamera.module.lib.listener.CaptureListener
            public void recordShort(final long j) {
                JCameraView.this.mCaptureLayout.setTextWithAnimation("录制时间过短");
                JCameraView.this.mSwitchCamera.setVisibility(0);
                JCameraView.this.mFlashLamp.setVisibility(0);
                JCameraView.this.postDelayed(new Runnable() { // from class: basecamera.module.lib.JCameraView.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        JCameraView.this.machine.stopRecord(true, j);
                    }
                }, 1500 - j);
            }

            @Override // basecamera.module.lib.listener.CaptureListener
            public void recordStart() {
                JCameraView.this.mSwitchCamera.setVisibility(4);
                JCameraView.this.mFlashLamp.setVisibility(4);
                JCameraView.this.machine.record(JCameraView.this.mVideoView.getHolder().getSurface(), JCameraView.this.screenProp);
            }

            @Override // basecamera.module.lib.listener.CaptureListener
            public void recordZoom(float f) {
                CameraLog.e("recordZoom");
                JCameraView.this.machine.zoom(f, CameraInterface.TYPE_RECORDER);
            }

            @Override // basecamera.module.lib.listener.CaptureListener
            public void takePictures() {
                if (JCameraView.this.onCameraSomeStateListener != null) {
                    JCameraView.this.onCameraSomeStateListener.onBeforeTakePhoto();
                }
                JCameraView.this.machine.capture();
            }
        });
        this.mCaptureLayout.setTypeLisenter(new TypeListener() { // from class: basecamera.module.lib.JCameraView.4
            @Override // basecamera.module.lib.listener.TypeListener
            public void cancel() {
                JCameraView.this.machine.cancle(JCameraView.this.mVideoView.getHolder(), JCameraView.this.screenProp);
            }

            @Override // basecamera.module.lib.listener.TypeListener
            public void confirm() {
                CameraLog.e(JCameraView.this.machine.getState() + "=====>");
                JCameraView.this.machine.confirm();
            }
        });
        this.mCaptureLayout.setLeftClickListener(new ClickListener() { // from class: basecamera.module.lib.JCameraView.5
            @Override // basecamera.module.lib.listener.ClickListener
            public void onClick() {
                if (JCameraView.this.leftClickListener != null) {
                    JCameraView.this.leftClickListener.onClick();
                }
            }
        });
        this.mCaptureLayout.setRightClickListener(new ClickListener() { // from class: basecamera.module.lib.JCameraView.6
            @Override // basecamera.module.lib.listener.ClickListener
            public void onClick() {
                if (JCameraView.this.rightClickListener != null) {
                    JCameraView.this.rightClickListener.onClick();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setFlashRes() {
        switch (this.type_flash) {
            case 33:
                this.mFlashLamp.setImageResource(R.mipmap.camera_flash_auto);
                this.machine.flash(DebugKt.DEBUG_PROPERTY_VALUE_AUTO);
                return;
            case 34:
                this.mFlashLamp.setImageResource(R.mipmap.camera_flash_on);
                this.machine.flash(DebugKt.DEBUG_PROPERTY_VALUE_ON);
                return;
            case 35:
                this.mFlashLamp.setImageResource(R.mipmap.camera_flash_off);
                this.machine.flash(DebugKt.DEBUG_PROPERTY_VALUE_OFF);
                return;
            default:
                return;
        }
    }

    private void setFocusViewWidthAnimation(float f, float f2) {
        this.machine.foucs(f, f2, new CameraInterface.FocusCallback() { // from class: basecamera.module.lib.JCameraView.9
            @Override // basecamera.module.lib.CameraInterface.FocusCallback
            public void focusSuccess() {
                JCameraView.this.mFoucsView.setVisibility(4);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateVideoViewSize(float f, float f2) {
        if (f > f2) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, (int) ((f2 / f) * getWidth()));
            layoutParams.gravity = 17;
            this.mVideoView.setLayoutParams(layoutParams);
        }
    }

    @Override // basecamera.module.lib.CameraInterface.CameraOpenOverCallback
    public void cameraHasOpened() {
        CameraInterface.getInstance().doStartPreview(this.mVideoView.getHolder(), this.screenProp);
        if (this.machine.isBackgroundCamera()) {
            this.type_flash = PreferencesUtils.getInstance().getInt(getContext(), "flash", 35);
        } else {
            this.type_flash = 35;
        }
        post(new Runnable() { // from class: basecamera.module.lib.JCameraView.7
            @Override // java.lang.Runnable
            public void run() {
                JCameraView.this.setFlashRes();
            }
        });
    }

    @Override // basecamera.module.lib.view.CameraView
    public void confirmState(int i) {
        switch (i) {
            case 1:
                this.mPhoto.setVisibility(4);
                if (this.jCameraLisenter != null) {
                    this.jCameraLisenter.captureSuccess(this.captureBitmap);
                    break;
                }
                break;
            case 2:
                stopVideo();
                this.mVideoView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
                this.machine.start(this.mVideoView.getHolder(), this.screenProp);
                if (this.jCameraLisenter != null) {
                    this.jCameraLisenter.recordSuccess(this.videoUrl, this.firstFrame);
                    break;
                }
                break;
        }
        CameraLog.e("confirmState===>" + i);
        this.mCaptureLayout.resetCaptureLayout();
    }

    @Override // basecamera.module.lib.view.CameraView
    public boolean handlerFoucs(float f, float f2) {
        if (f2 > this.mCaptureLayout.getTop()) {
            return false;
        }
        this.mFoucsView.setVisibility(0);
        if (f < this.mFoucsView.getWidth() / 2) {
            f = this.mFoucsView.getWidth() / 2;
        }
        if (f > this.layout_width - (this.mFoucsView.getWidth() / 2)) {
            f = this.layout_width - (this.mFoucsView.getWidth() / 2);
        }
        if (f2 < this.mFoucsView.getWidth() / 2) {
            f2 = this.mFoucsView.getWidth() / 2;
        }
        if (f2 > this.mCaptureLayout.getTop() - (this.mFoucsView.getWidth() / 2)) {
            f2 = this.mCaptureLayout.getTop() - (this.mFoucsView.getWidth() / 2);
        }
        this.mFoucsView.setX(f - (this.mFoucsView.getWidth() / 2));
        this.mFoucsView.setY(f2 - (this.mFoucsView.getHeight() / 2));
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mFoucsView, "scaleX", 1.0f, 0.6f);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.mFoucsView, "scaleY", 1.0f, 0.6f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(ofFloat).with(ofFloat2);
        animatorSet.setDuration(300L);
        animatorSet.start();
        return true;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        float measuredWidth = this.mVideoView.getMeasuredWidth();
        float measuredHeight = this.mVideoView.getMeasuredHeight();
        if (this.screenProp == 0.0f) {
            this.screenProp = measuredHeight / measuredWidth;
        }
    }

    public void onPause() {
        CameraLog.e("JCameraView onPause");
        stopVideo();
        resetState(1);
        this.machine.stop();
        CameraInterface.getInstance().isPreview(false);
        CameraInterface.getInstance().unregisterSensorManager(this.mContext);
    }

    public void onResume() {
        CameraLog.e("JCameraView onResume");
        resetState(4);
        CameraInterface.getInstance().registerSensorManager(this.mContext);
        CameraInterface.getInstance().setSwitchView(this.mSwitchCamera, this.mFlashLamp);
        try {
            this.machine.start(this.mVideoView.getHolder(), this.screenProp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x007e, code lost:
    
        return true;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onTouchEvent(android.view.MotionEvent r11) {
        /*
            r10 = this;
            int r0 = r11.getAction()
            r1 = 2
            r2 = 1
            switch(r0) {
                case 0: goto L60;
                case 1: goto L5d;
                case 2: goto Lb;
                default: goto L9;
            }
        L9:
            goto L7e
        Lb:
            int r0 = r11.getPointerCount()
            if (r0 != r2) goto L13
            r10.firstTouch = r2
        L13:
            int r0 = r11.getPointerCount()
            if (r0 != r1) goto L7e
            r0 = 0
            float r1 = r11.getX(r0)
            float r3 = r11.getY(r0)
            float r4 = r11.getX(r2)
            float r11 = r11.getY(r2)
            float r1 = r1 - r4
            double r4 = (double) r1
            r6 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r4 = java.lang.Math.pow(r4, r6)
            float r3 = r3 - r11
            double r8 = (double) r3
            double r6 = java.lang.Math.pow(r8, r6)
            double r4 = r4 + r6
            double r3 = java.lang.Math.sqrt(r4)
            float r11 = (float) r3
            boolean r1 = r10.firstTouch
            if (r1 == 0) goto L46
            r10.firstTouchLength = r11
            r10.firstTouch = r0
        L46:
            float r0 = r10.firstTouchLength
            float r0 = r11 - r0
            int r0 = (int) r0
            int r1 = r10.zoomGradient
            int r0 = r0 / r1
            if (r0 == 0) goto L7e
            r10.firstTouch = r2
            basecamera.module.lib.state.CameraMachine r0 = r10.machine
            float r1 = r10.firstTouchLength
            float r11 = r11 - r1
            r1 = 145(0x91, float:2.03E-43)
            r0.zoom(r11, r1)
            goto L7e
        L5d:
            r10.firstTouch = r2
            goto L7e
        L60:
            int r0 = r11.getPointerCount()
            if (r0 != r2) goto L71
            float r0 = r11.getX()
            float r3 = r11.getY()
            r10.setFocusViewWidthAnimation(r0, r3)
        L71:
            int r11 = r11.getPointerCount()
            if (r11 != r1) goto L7e
            java.lang.String r11 = "CJT"
            java.lang.String r0 = "ACTION_DOWN = 2"
            android.util.Log.i(r11, r0)
        L7e:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: basecamera.module.lib.JCameraView.onTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // basecamera.module.lib.view.CameraView
    public void playVideo(Bitmap bitmap, final String str) {
        this.videoUrl = str;
        this.firstFrame = bitmap;
        new Thread(new Runnable() { // from class: basecamera.module.lib.JCameraView.10
            @Override // java.lang.Runnable
            @RequiresApi(api = 16)
            public void run() {
                try {
                    if (JCameraView.this.mMediaPlayer == null) {
                        JCameraView.this.mMediaPlayer = new MediaPlayer();
                    } else {
                        JCameraView.this.mMediaPlayer.reset();
                    }
                    JCameraView.this.mMediaPlayer.setDataSource(str);
                    JCameraView.this.mMediaPlayer.setSurface(JCameraView.this.mVideoView.getHolder().getSurface());
                    JCameraView.this.mMediaPlayer.setVideoScalingMode(1);
                    JCameraView.this.mMediaPlayer.setAudioStreamType(3);
                    JCameraView.this.mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() { // from class: basecamera.module.lib.JCameraView.10.1
                        @Override // android.media.MediaPlayer.OnVideoSizeChangedListener
                        public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2) {
                            JCameraView.this.updateVideoViewSize(JCameraView.this.mMediaPlayer.getVideoWidth(), JCameraView.this.mMediaPlayer.getVideoHeight());
                        }
                    });
                    JCameraView.this.mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: basecamera.module.lib.JCameraView.10.2
                        @Override // android.media.MediaPlayer.OnPreparedListener
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            JCameraView.this.mMediaPlayer.start();
                        }
                    });
                    JCameraView.this.mMediaPlayer.setLooping(true);
                    JCameraView.this.mMediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override // basecamera.module.lib.view.CameraView
    public void resetState(int i) {
        switch (i) {
            case 1:
                this.mPhoto.setVisibility(4);
                break;
            case 2:
                stopVideo();
                FileUtil.deleteFile(this.videoUrl);
                this.mVideoView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
                this.machine.start(this.mVideoView.getHolder(), this.screenProp);
                break;
            case 4:
                this.mVideoView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
                break;
        }
        this.mSwitchCamera.setVisibility(0);
        this.mCaptureLayout.resetCaptureLayout();
    }

    public void setErrorLisenter(ErrorListener errorListener) {
        this.errorLisenter = errorListener;
        CameraInterface.getInstance().setErrorLinsenter(errorListener);
    }

    public void setFeatures(int i) {
        this.mCaptureLayout.setButtonFeatures(i);
    }

    public void setJCameraLisenter(JCameraListener jCameraListener) {
        this.jCameraLisenter = jCameraListener;
    }

    public void setLeftClickListener(ClickListener clickListener) {
        this.leftClickListener = clickListener;
    }

    public void setMediaQuality(int i) {
        CameraInterface.getInstance().setMediaQuality(i);
    }

    public void setOnCameraSomeStateListener(OnCameraSomeStateListener onCameraSomeStateListener) {
        this.onCameraSomeStateListener = onCameraSomeStateListener;
    }

    public void setRightClickListener(ClickListener clickListener) {
        this.rightClickListener = clickListener;
    }

    public void setSaveVideoPath(String str) {
        CameraInterface.getInstance().setSaveVideoPath(str);
    }

    @Override // basecamera.module.lib.view.CameraView
    public void setTip(String str) {
        this.mCaptureLayout.setTip(str);
    }

    @Override // basecamera.module.lib.view.CameraView
    public void showPicture(Bitmap bitmap, boolean z) {
        this.captureBitmap = bitmap;
        if (this.jCameraLisenter != null) {
            this.jCameraLisenter.captureSuccess(this.captureBitmap);
        }
        this.machine.setState(this.machine.getBorrowPictureState());
        this.machine.cancle(this.mVideoView.getHolder(), this.screenProp);
    }

    @Override // basecamera.module.lib.view.CameraView
    public void startPreviewCallback() {
        CameraLog.i("startPreviewCallback");
        handlerFoucs(this.mFoucsView.getWidth() / 2, this.mFoucsView.getHeight() / 2);
    }

    @Override // basecamera.module.lib.view.CameraView
    public void stopVideo() {
        if (this.mMediaPlayer == null || !this.mMediaPlayer.isPlaying()) {
            return;
        }
        this.mMediaPlayer.stop();
        this.mMediaPlayer.release();
        this.mMediaPlayer = null;
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [basecamera.module.lib.JCameraView$8] */
    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        CameraLog.i("JCameraView SurfaceCreated");
        new Thread() { // from class: basecamera.module.lib.JCameraView.8
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                CameraInterface.getInstance().doOpenCamera(JCameraView.this);
            }
        }.start();
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        CameraLog.e("JCameraView SurfaceDestroyed");
        CameraInterface.getInstance().doDestroyCamera();
    }

    public void switchCamera() {
        this.machine.swtich(this.mVideoView.getHolder(), this.screenProp);
    }

    public void takePhoto() {
        if (this.machine != null) {
            this.machine.capture();
        } else {
            Log.e("npCamera", "machine = null !!!");
        }
    }
}
