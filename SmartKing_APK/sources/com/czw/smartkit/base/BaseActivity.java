package com.czw.smartkit.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.czw.modes.activity.RootActivity;
import com.czw.smartkit.R;
import com.czw.smartkit.net.NetImpl;
import com.czw.smartkit.netModule.YCNetCodeParserHelper;
import com.czw.smartkit.util.ActivityManager;
import com.czw.smartkit.util.ToastHelper;
import com.czw.utils.ViewUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.wx.wheelview.common.WheelConstants;
import java.util.List;

/* loaded from: classes.dex */
public abstract class BaseActivity extends RootActivity {
    private QMUITipDialog.Builder builder;
    private QMUITipDialog loadingDialog;
    private long mExitTime;
    QMUITipDialog qmuiTipDialog;
    private final NetImpl netImpl = NetImpl.getNetImpl();
    protected DisplayMetrics dm = new DisplayMetrics();

    /* JADX INFO: Access modifiers changed from: protected */
    public <T extends View> T $View(int i) {
        return (T) findViewById(i);
    }

    protected <T extends View> T $View(View view, int i) {
        return (T) view.findViewById(i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String $str(int i) {
        return getString(i);
    }

    public void dismissLoadingDialog() {
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.base.BaseActivity.5
            @Override // java.lang.Runnable
            public void run() {
                if (BaseActivity.this.loadingDialog == null || !BaseActivity.this.loadingDialog.isShowing()) {
                    return;
                }
                BaseActivity.this.loadingDialog.dismiss();
            }
        });
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized NetImpl getNet() {
        return this.netImpl.loadActivity(this);
    }

    public FragmentActivity getUI() {
        return this;
    }

    public abstract void initView();

    /* JADX INFO: Access modifiers changed from: protected */
    public void insertInit() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isExit(int i) {
        if (i != 4) {
            return false;
        }
        if (System.currentTimeMillis() - this.mExitTime <= 2000) {
            finish();
            return true;
        }
        toast(R.string.agin_exit_app);
        this.mExitTime = System.currentTimeMillis();
        return true;
    }

    public boolean isFullScreen() {
        return false;
    }

    public boolean isNoTitle() {
        return true;
    }

    public void jump(Intent intent) {
        startActivity(intent);
    }

    public void jump(Class<?> cls) {
        if (cls != null) {
            startActivity(new Intent(this, cls));
        }
    }

    public void jump2Camera() {
        PictureSelector.create(this).openCamera(PictureMimeType.ofImage()).enableCrop(true).cropWH(WheelConstants.WHEEL_SCROLL_DELAY_DURATION, WheelConstants.WHEEL_SCROLL_DELAY_DURATION).scaleEnabled(true).freeStyleCropEnabled(true).withAspectRatio(1, 1).forResult(188);
    }

    public void jump2Gallery() {
        PictureSelector.create(this).openGallery(PictureMimeType.ofImage()).isCamera(false).enableCrop(true).cropWH(WheelConstants.WHEEL_SCROLL_DELAY_DURATION, WheelConstants.WHEEL_SCROLL_DELAY_DURATION).scaleEnabled(true).selectionMode(1).freeStyleCropEnabled(true).withAspectRatio(1, 1).forResult(188);
    }

    public void jump2GalleryMulite(int i) {
        PictureSelector.create(this).openGallery(PictureMimeType.ofImage()).isCamera(false).maxSelectNum(i).cropWH(WheelConstants.WHEEL_SCROLL_DELAY_DURATION, WheelConstants.WHEEL_SCROLL_DELAY_DURATION).compress(true).scaleEnabled(true).freeStyleCropEnabled(true).selectionMode(2).forResult(188);
    }

    public void jumpAndFinsh(Class<?> cls) {
        jump(cls);
        finish();
    }

    public void jumpAndFish(Class<?> cls) {
        startActivity(new Intent(this, cls));
        finish();
    }

    public void jumpFor(Intent intent, int i) {
        startActivityForResult(intent, i);
    }

    public void jumpFor(Class<?> cls, int i) {
        startActivityForResult(new Intent(this, cls), i);
    }

    public abstract int loadLayout();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == 188) {
            onImageSelect(PictureSelector.obtainMultipleResult(intent));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        ActivityManager.getInstance().putActivity(this);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarDarkMode(this);
        setContentView(loadLayout());
        YCNetCodeParserHelper.init(this);
        ButterKnife.bind(this);
        ViewUtil.load(this);
        ToastHelper.getToastHelper().setActivity(this);
        getWindowManager().getDefaultDisplay().getMetrics(this.dm);
        insertInit();
        initView();
        onCreateMap(bundle);
    }

    protected void onCreateMap(Bundle bundle) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        ActivityManager.getInstance().removeActivity(this);
        super.onDestroy();
    }

    protected void onImageSelect(List<LocalMedia> list) {
    }

    public void setLoadingDialogCancelable(boolean z) {
        if (this.loadingDialog != null) {
            this.loadingDialog.setCancelable(z);
        }
    }

    public void setLoadingDialogOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        if (this.loadingDialog != null) {
            this.loadingDialog.setOnCancelListener(onCancelListener);
        }
    }

    public void showFailDialog(String str) {
        try {
            final QMUITipDialog create = new QMUITipDialog.Builder(this).setIconType(3).setTipWord(str).create();
            create.show();
            new Handler().postDelayed(new Runnable() { // from class: com.czw.smartkit.base.BaseActivity.2
                @Override // java.lang.Runnable
                public void run() {
                    create.dismiss();
                }
            }, 1500L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showFailDialogNotClose(String str) {
        try {
            if (this.qmuiTipDialog == null) {
                this.qmuiTipDialog = new QMUITipDialog.Builder(this).setIconType(3).setTipWord(str).create();
            }
            this.qmuiTipDialog.setCanceledOnTouchOutside(true);
            this.qmuiTipDialog.setCancelable(true);
            this.qmuiTipDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showLoadingDialog(final String str) {
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.base.BaseActivity.4
            @Override // java.lang.Runnable
            public void run() {
                try {
                    if (BaseActivity.this.loadingDialog == null || !BaseActivity.this.loadingDialog.isShowing()) {
                        BaseActivity.this.builder = new QMUITipDialog.Builder(BaseActivity.this).setIconType(1);
                        BaseActivity.this.loadingDialog = BaseActivity.this.builder.setTipWord(str).create();
                        BaseActivity.this.setLoadingDialogCancelable(true);
                        BaseActivity.this.loadingDialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void showMessageDialog(final String str) {
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.base.BaseActivity.3
            @Override // java.lang.Runnable
            public void run() {
                try {
                    final QMUITipDialog create = new QMUITipDialog.Builder(BaseActivity.this).setIconType(4).setTipWord(str).create();
                    create.show();
                    new Handler().postDelayed(new Runnable() { // from class: com.czw.smartkit.base.BaseActivity.3.1
                        @Override // java.lang.Runnable
                        public void run() {
                            create.dismiss();
                        }
                    }, 1000L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void showSuccessDialog(String str) {
        try {
            final QMUITipDialog create = new QMUITipDialog.Builder(this).setIconType(2).setTipWord(str).create();
            create.show();
            new Handler().postDelayed(new Runnable() { // from class: com.czw.smartkit.base.BaseActivity.1
                @Override // java.lang.Runnable
                public void run() {
                    create.dismiss();
                }
            }, 1500L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void toast(int i) {
        toast(getString(i));
    }

    public final void toast(int i, int i2) {
        toast(getString(i), i2);
    }

    public final void toast(final String str) {
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.base.BaseActivity.6
            @Override // java.lang.Runnable
            public void run() {
                Toast.makeText(BaseActivity.this.getUI(), str, 0).show();
            }
        });
    }

    public final void toast(final String str, int i) {
        new Handler().postDelayed(new Runnable() { // from class: com.czw.smartkit.base.BaseActivity.7
            @Override // java.lang.Runnable
            public void run() {
                BaseActivity.this.toast(str);
            }
        }, i);
    }
}
