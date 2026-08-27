package basecamera.module.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import basecamera.module.cfg.BaseCameraCfg;
import basecamera.module.cfg.CameraSateHelper;
import basecamera.module.lib.JCameraView;
import basecamera.module.lib.R;
import basecamera.module.lib.listener.ClickListener;
import basecamera.module.lib.listener.ErrorListener;
import basecamera.module.lib.listener.JCameraListener;
import basecamera.module.lib.util.CameraLog;
import basecamera.module.lib.util.FileUtil;
import basecamera.module.views.LoadingView;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/* loaded from: classes.dex */
public class BaseCameraTakePhotoActivity extends Activity {
    public static boolean isStartUI = false;
    private JCameraView jCameraView;
    private LoadingView loadView;
    private boolean isTakePhotoIng = false;
    private Handler handler = new Handler();
    BroadcastReceiver receiver = new BroadcastReceiver() { // from class: basecamera.module.activity.BaseCameraTakePhotoActivity.6
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            char c;
            String action = intent.getAction();
            int hashCode = action.hashCode();
            if (hashCode == -111690705) {
                if (action.equals(BaseCameraCfg.exitTakePhotoForAppWithDisconnected)) {
                    c = 2;
                }
                c = 65535;
            } else if (hashCode != 222935873) {
                if (hashCode == 1692209849 && action.equals(BaseCameraCfg.exitTakePhotoForApp)) {
                    c = 1;
                }
                c = 65535;
            } else {
                if (action.equals(BaseCameraCfg.takePhotoAction)) {
                    c = 0;
                }
                c = 65535;
            }
            switch (c) {
                case 0:
                    if (BaseCameraTakePhotoActivity.this.isTakePhotoIng) {
                        return;
                    }
                    BaseCameraTakePhotoActivity.this.isTakePhotoIng = true;
                    BaseCameraTakePhotoActivity.this.showLoading();
                    Log.e("npCamera", "开始拍照，显示loading");
                    BaseCameraTakePhotoActivity.this.jCameraView.takePhoto();
                    return;
                case 1:
                    if (!BaseCameraTakePhotoActivity.this.isTakePhotoIng) {
                        BaseCameraTakePhotoActivity.this.finish();
                        return;
                    }
                    if (BaseCameraTakePhotoActivity.this.handler == null) {
                        BaseCameraTakePhotoActivity.this.handler = new Handler();
                    }
                    BaseCameraTakePhotoActivity.this.handler.postDelayed(new Runnable() { // from class: basecamera.module.activity.BaseCameraTakePhotoActivity.6.1
                        @Override // java.lang.Runnable
                        public void run() {
                            BaseCameraTakePhotoActivity.this.finish();
                        }
                    }, BaseCameraCfg.delayExitTime);
                    return;
                case 2:
                    if (!BaseCameraTakePhotoActivity.this.isTakePhotoIng) {
                        Toast.makeText(BaseCameraTakePhotoActivity.this, BaseCameraCfg.withTakeUIDisconnetedMessage, 0).show();
                        BaseCameraTakePhotoActivity.this.finish();
                        return;
                    } else {
                        if (BaseCameraTakePhotoActivity.this.handler == null) {
                            BaseCameraTakePhotoActivity.this.handler = new Handler();
                        }
                        BaseCameraTakePhotoActivity.this.handler.postDelayed(new Runnable() { // from class: basecamera.module.activity.BaseCameraTakePhotoActivity.6.2
                            @Override // java.lang.Runnable
                            public void run() {
                                BaseCameraTakePhotoActivity.this.finish();
                            }
                        }, BaseCameraCfg.delayExitTime);
                        return;
                    }
                default:
                    return;
            }
        }
    };

    private void initReceiver(boolean z) {
        try {
            if (z) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(BaseCameraCfg.takePhotoAction);
                intentFilter.addAction(BaseCameraCfg.exitTakePhotoForApp);
                intentFilter.addAction(BaseCameraCfg.exitTakePhotoForAppWithDisconnected);
                registerReceiver(this.receiver, intentFilter);
            } else {
                unregisterReceiver(this.receiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        isStartUI = true;
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setRequestedOrientation(1);
        setContentView(R.layout.basecamera_activity_camera);
        this.jCameraView = (JCameraView) findViewById(R.id.jcameraview);
        this.loadView = (LoadingView) findViewById(R.id.loadView);
        this.jCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "JCamera");
        this.jCameraView.setFeatures(257);
        this.jCameraView.setTip("");
        this.jCameraView.setErrorLisenter(new ErrorListener() { // from class: basecamera.module.activity.BaseCameraTakePhotoActivity.1
            @Override // basecamera.module.lib.listener.ErrorListener
            public void AudioPermissionError() {
                Toast.makeText(BaseCameraTakePhotoActivity.this, "给点录音权限可以?", 0).show();
            }

            @Override // basecamera.module.lib.listener.ErrorListener
            public void onError() {
                Log.i("CJT", "camera error");
                BaseCameraTakePhotoActivity.this.setResult(103, new Intent());
                BaseCameraTakePhotoActivity.this.finish();
            }
        });
        this.jCameraView.setJCameraLisenter(new JCameraListener() { // from class: basecamera.module.activity.BaseCameraTakePhotoActivity.2
            @Override // basecamera.module.lib.listener.JCameraListener
            public void captureSuccess(Bitmap bitmap) {
                String saveBitmap = FileUtil.saveBitmap(BaseCameraCfg.photoPath, new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".jpg", bitmap);
                BaseCameraTakePhotoActivity.this.isTakePhotoIng = false;
                CameraLog.e("path===>" + saveBitmap);
                BaseCameraTakePhotoActivity.this.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(new File(saveBitmap))));
                if (TextUtils.isEmpty(saveBitmap)) {
                    CameraSateHelper.getInstance().notifyFailure(1);
                } else {
                    CameraSateHelper.getInstance().notifySuccess(saveBitmap);
                }
                BaseCameraTakePhotoActivity.this.runOnUiThread(new Runnable() { // from class: basecamera.module.activity.BaseCameraTakePhotoActivity.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (BaseCameraTakePhotoActivity.this.loadView != null) {
                            BaseCameraTakePhotoActivity.this.loadView.setVisibility(8);
                        }
                    }
                });
            }

            @Override // basecamera.module.lib.listener.JCameraListener
            public void recordSuccess(String str, Bitmap bitmap) {
                String saveBitmap = FileUtil.saveBitmap("JCamera/videoScreen", new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".jpg", bitmap);
                Log.i("CJT", "url = " + str + ", Bitmap = " + saveBitmap);
                Intent intent = new Intent();
                intent.putExtra(FileDownloadModel.PATH, saveBitmap);
                BaseCameraTakePhotoActivity.this.setResult(101, intent);
                BaseCameraTakePhotoActivity.this.finish();
            }
        });
        this.jCameraView.setOnCameraSomeStateListener(new JCameraView.OnCameraSomeStateListener() { // from class: basecamera.module.activity.BaseCameraTakePhotoActivity.3
            @Override // basecamera.module.lib.JCameraView.OnCameraSomeStateListener
            public void onBeforeTakePhoto() {
                Log.e("fuck,camera", "开始拍照");
                BaseCameraTakePhotoActivity.this.sendBroadcast(new Intent(BaseCameraCfg.takePhotoAction));
            }
        });
        this.jCameraView.setLeftClickListener(new ClickListener() { // from class: basecamera.module.activity.BaseCameraTakePhotoActivity.4
            @Override // basecamera.module.lib.listener.ClickListener
            public void onClick() {
                if (BaseCameraTakePhotoActivity.this.isTakePhotoIng) {
                    return;
                }
                BaseCameraTakePhotoActivity.this.finish();
            }
        });
        this.jCameraView.setRightClickListener(new ClickListener() { // from class: basecamera.module.activity.BaseCameraTakePhotoActivity.5
            @Override // basecamera.module.lib.listener.ClickListener
            public void onClick() {
                BaseCameraTakePhotoActivity.this.startActivity(new Intent(BaseCameraTakePhotoActivity.this, (Class<?>) BaseCameraGalleryActivity.class));
            }
        });
        Log.e("npCamera", "开始进入相机界面");
        initReceiver(true);
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        Log.e("npCamera", "onDestroy");
        if (this.loadView != null) {
            this.loadView.setVisibility(8);
        }
        isStartUI = false;
        sendExitCamera();
        this.isTakePhotoIng = false;
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            if (this.isTakePhotoIng) {
                return true;
            }
            sendExitCamera();
        }
        return super.onKeyDown(i, keyEvent);
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        Log.e("npCamera", "onPause");
        initReceiver(false);
        this.jCameraView.onPause();
        isStartUI = false;
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        Log.e("npCamera", "onResume");
        initReceiver(true);
        this.jCameraView.onResume();
        isStartUI = true;
        this.isTakePhotoIng = false;
    }

    public void sendExitCamera() {
        isStartUI = false;
        sendBroadcast(new Intent(BaseCameraCfg.exitTakePhotoForDev));
    }

    public void showLoading() {
        runOnUiThread(new Runnable() { // from class: basecamera.module.activity.BaseCameraTakePhotoActivity.7
            @Override // java.lang.Runnable
            public void run() {
                try {
                    if (BaseCameraTakePhotoActivity.this.loadView != null) {
                        BaseCameraTakePhotoActivity.this.loadView.setVisibility(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
