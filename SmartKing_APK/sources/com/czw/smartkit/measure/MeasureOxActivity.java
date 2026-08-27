package com.czw.smartkit.measure;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.czw.smartkit.R;
import com.czw.smartkit.base.TitleActivity;
import com.czw.smartkit.bleModule.BleManager;
import com.czw.smartkit.bleModule.DataStruct;
import com.czw.smartkit.bleModule.measure.DevMeasureBean;
import com.czw.smartkit.bleModule.measure.DevMeasureCallback;
import com.czw.smartkit.bleModule.measure.DevMeasureUtil;
import com.czw.smartkit.bleModule.measure.MeasureType;
import com.czw.smartkit.views.LineView;
import com.czw.utils.LogUtil;

/* loaded from: classes.dex */
public class MeasureOxActivity extends TitleActivity implements DevMeasureCallback {
    private LineView lineView;

    @BindView(R.id.measureBtn)
    Button measureBtn;

    @BindView(R.id.ic_type_value)
    TextView measureOxValue;

    @BindView(R.id.ic_type)
    ImageView measureTypeIcon;
    protected boolean isMeasure = false;
    AnimatorSet animatorSet = new AnimatorSet();

    private void startAnim() {
        if (this.animatorSet.isRunning()) {
            this.animatorSet.cancel();
        }
        this.animatorSet.start();
        this.measureOxValue.setText("- %");
        this.measureBtn.setText(R.string.stop_measure);
        this.lineView.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopAnim() {
        this.lineView.stopUpdate();
        this.measureBtn.setText(R.string.start_measure);
        this.animatorSet.end();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @OnClick({R.id.measureBtn})
    public void click(View view) {
        if (view.getId() != R.id.measureBtn) {
            return;
        }
        this.isMeasure = !this.isMeasure;
        if (this.isMeasure) {
            startAnim();
        } else {
            stopAnim();
        }
        BleManager.getBleManager().writeData(DataStruct.measure(this.isMeasure, 2));
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public void initView() {
        this.titleBar.setTitle(R.string.ox_title);
        this.lineView = (LineView) $View(R.id.lineView);
        this.lineView.initValue(100, 40, 10, -10962435);
        this.lineView.initUnit("%");
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.measureTypeIcon, "scaleX", 1.0f, 0.6f, 1.0f);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.measureTypeIcon, "scaleY", 1.0f, 0.6f, 1.0f);
        ofFloat.setRepeatCount(-1);
        ofFloat2.setRepeatCount(-1);
        this.animatorSet.setDuration(1000L);
        this.animatorSet.setInterpolator(new DecelerateInterpolator());
        this.animatorSet.play(ofFloat).with(ofFloat2);
        DevMeasureUtil.getInstance().registerCallback(this);
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public int loadLayout() {
        return R.layout.ui_measure_ox;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.czw.smartkit.base.BaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        DevMeasureUtil.getInstance().unRegisterCallback(this);
        if (this.isMeasure) {
            BleManager.getBleManager().writeData(DataStruct.measure(false, 2));
        }
    }

    @Override // com.czw.smartkit.bleModule.measure.DevMeasureCallback
    public void onMeasure(final DevMeasureBean devMeasureBean) {
        if (devMeasureBean == null) {
            return;
        }
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.measure.MeasureOxActivity.2
            @Override // java.lang.Runnable
            public void run() {
                if (devMeasureBean.getIntOx() != 0) {
                    MeasureOxActivity.this.lineView.update(devMeasureBean.getIntOx());
                    MeasureOxActivity.this.measureOxValue.setText(devMeasureBean.getIntOx() + " %");
                }
            }
        });
    }

    @Override // com.czw.smartkit.bleModule.measure.DevMeasureCallback
    public void onStartMeasure(MeasureType measureType) {
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.measure.MeasureOxActivity.1
            @Override // java.lang.Runnable
            public void run() {
                LogUtil.e("debug====>血氧开始测试");
            }
        });
    }

    @Override // com.czw.smartkit.bleModule.measure.DevMeasureCallback
    public void onStopMeasure(MeasureType measureType) {
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.measure.MeasureOxActivity.3
            @Override // java.lang.Runnable
            public void run() {
                if (MeasureOxActivity.this.isMeasure) {
                    MeasureOxActivity.this.isMeasure = false;
                }
                MeasureOxActivity.this.stopAnim();
            }
        });
    }
}
