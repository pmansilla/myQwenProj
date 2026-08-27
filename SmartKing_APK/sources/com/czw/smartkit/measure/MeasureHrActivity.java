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
import com.czw.modes.widget.TitleBar;
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
public class MeasureHrActivity extends TitleActivity implements DevMeasureCallback {
    private TextView hr_info;

    @BindView(R.id.measureBtn)
    Button measureBtn;

    @BindView(R.id.ic_type_value)
    TextView measureHrValue;

    @BindView(R.id.lineView)
    LineView measureLineShowView;

    @BindView(R.id.ic_type)
    ImageView measureTypeIcon;
    protected boolean isMeasure = false;
    AnimatorSet animatorSet = new AnimatorSet();

    private void startAnim() {
        if (this.animatorSet.isRunning()) {
            this.animatorSet.cancel();
        }
        this.animatorSet.start();
        this.measureHrValue.setText("- bpm");
        this.measureBtn.setText(R.string.stop_measure);
        this.hr_info.setVisibility(4);
        this.measureLineShowView.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopAnim() {
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
        BleManager.getBleManager().writeData(DataStruct.measure(this.isMeasure, 0));
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public void initView() {
        this.titleBar.setTitle(R.string.hr_title);
        this.hr_info = (TextView) $View(R.id.hr_info);
        this.measureLineShowView.initValue(140, 40, 10, -2091199);
        this.titleBar.setClick(new TitleBar.TitleClick() { // from class: com.czw.smartkit.measure.MeasureHrActivity.1
            @Override // com.czw.modes.widget.TitleBar.LeftClick
            public void onLeftClick(View view) {
                DevMeasureUtil.getInstance().unRegisterCallback(MeasureHrActivity.this);
                if (MeasureHrActivity.this.isMeasure) {
                    BleManager.getBleManager().writeData(DataStruct.measure(false, 0));
                }
                MeasureHrActivity.this.finish();
            }

            @Override // com.czw.modes.widget.TitleBar.TitleClick
            public void onRightClick(View view) {
            }
        });
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
        return R.layout.ui_measure_hr;
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        DevMeasureUtil.getInstance().unRegisterCallback(this);
        if (this.isMeasure) {
            BleManager.getBleManager().writeData(DataStruct.measure(false, 0));
        }
        super.onBackPressed();
    }

    @Override // com.czw.smartkit.bleModule.measure.DevMeasureCallback
    public void onMeasure(final DevMeasureBean devMeasureBean) {
        if (devMeasureBean == null) {
            return;
        }
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.measure.MeasureHrActivity.3
            @Override // java.lang.Runnable
            public void run() {
                if (devMeasureBean.getIntHr() != 0) {
                    MeasureHrActivity.this.measureLineShowView.update(devMeasureBean.getIntHr());
                    MeasureHrActivity.this.measureHrValue.setText(devMeasureBean.getIntHr() + " bpm");
                }
                if (devMeasureBean.getAvgHr() == 0 || devMeasureBean.getMinHr() == 0 || devMeasureBean.getMaxHr() == 0) {
                    return;
                }
                MeasureHrActivity.this.hr_info.setVisibility(0);
                MeasureHrActivity.this.hr_info.setText(MeasureHrActivity.this.getString(R.string.for_mat_hr_info, new Object[]{devMeasureBean.getAvgHr() + "", devMeasureBean.getMaxHr() + "", devMeasureBean.getMinHr() + ""}));
            }
        });
    }

    @Override // com.czw.smartkit.bleModule.measure.DevMeasureCallback
    public void onStartMeasure(MeasureType measureType) {
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.measure.MeasureHrActivity.2
            @Override // java.lang.Runnable
            public void run() {
                LogUtil.e("debug====>心率开始测试");
            }
        });
    }

    @Override // com.czw.smartkit.bleModule.measure.DevMeasureCallback
    public void onStopMeasure(MeasureType measureType) {
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.measure.MeasureHrActivity.4
            @Override // java.lang.Runnable
            public void run() {
                if (MeasureHrActivity.this.isMeasure) {
                    MeasureHrActivity.this.isMeasure = false;
                }
                MeasureHrActivity.this.stopAnim();
            }
        });
    }
}
