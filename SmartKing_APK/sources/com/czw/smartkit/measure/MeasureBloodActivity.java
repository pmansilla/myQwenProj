package com.czw.smartkit.measure;

import android.view.View;
import android.widget.Button;
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
import com.czw.smartkit.views.NpHgView;
import com.czw.utils.LogUtil;

/* loaded from: classes.dex */
public class MeasureBloodActivity extends TitleActivity implements DevMeasureCallback {
    private NpHgView blood1;
    private NpHgView blood2;
    protected boolean isMeasure = false;

    @BindView(R.id.ic_type_value)
    TextView measureBloodValue;

    @BindView(R.id.measureBtn)
    Button measureBtn;

    private void startAnim() {
        this.measureBtn.setText(R.string.stop_measure);
        this.measureBloodValue.setText(getString(R.string.current_blood_format, new Object[]{"-", "-"}));
        this.blood1.start();
        this.blood2.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopAnim() {
        this.isMeasure = false;
        this.measureBtn.setText(R.string.start_measure);
        this.blood1.stop();
        this.blood2.stop();
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
        BleManager.getBleManager().writeData(DataStruct.measure(this.isMeasure, 1));
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public void initView() {
        this.titleBar.setTitle(R.string.blood_title);
        this.blood1 = (NpHgView) $View(R.id.blood1);
        this.blood2 = (NpHgView) $View(R.id.blood2);
        this.measureBloodValue.setText(getString(R.string.current_blood_format, new Object[]{"-", "-"}));
        DevMeasureUtil.getInstance().registerCallback(this);
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public int loadLayout() {
        return R.layout.ui_measure_blood;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.czw.smartkit.base.BaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        DevMeasureUtil.getInstance().unRegisterCallback(this);
        if (this.isMeasure) {
            BleManager.getBleManager().writeData(DataStruct.measure(false, 1));
        }
    }

    @Override // com.czw.smartkit.bleModule.measure.DevMeasureCallback
    public void onMeasure(final DevMeasureBean devMeasureBean) {
        if (devMeasureBean == null) {
            return;
        }
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.measure.MeasureBloodActivity.2
            @Override // java.lang.Runnable
            public void run() {
                if (devMeasureBean.getIntBpH() == 0 || devMeasureBean.getIntBpL() == 0) {
                    return;
                }
                int intBpH = devMeasureBean.getIntBpH();
                int intBpL = devMeasureBean.getIntBpL();
                MeasureBloodActivity.this.measureBloodValue.setText(MeasureBloodActivity.this.getString(R.string.current_blood_format, new Object[]{"" + intBpH, "" + intBpL}));
                float f = (float) intBpL;
                float f2 = f / 2.0f;
                float f3 = ((float) (intBpL + intBpH)) - f2;
                MeasureBloodActivity.this.blood1.update((((float) intBpH) - f2) / f3);
                MeasureBloodActivity.this.blood2.update((f - f2) / f3);
            }
        });
    }

    @Override // com.czw.smartkit.bleModule.measure.DevMeasureCallback
    public void onStartMeasure(MeasureType measureType) {
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.measure.MeasureBloodActivity.1
            @Override // java.lang.Runnable
            public void run() {
                LogUtil.e("debug====>血氧开始测试");
            }
        });
    }

    @Override // com.czw.smartkit.bleModule.measure.DevMeasureCallback
    public void onStopMeasure(MeasureType measureType) {
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.measure.MeasureBloodActivity.3
            @Override // java.lang.Runnable
            public void run() {
                MeasureBloodActivity.this.stopAnim();
            }
        });
    }
}
