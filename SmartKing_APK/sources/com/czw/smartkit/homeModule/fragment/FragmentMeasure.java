package com.czw.smartkit.homeModule.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.amap.location.common.model.AmapLoc;
import com.czw.smartkit.R;
import com.czw.smartkit.base.BaseFragment;
import com.czw.smartkit.bleModule.BleManager;
import com.czw.smartkit.bleModule.DataStruct;
import com.czw.smartkit.bleModule.DevFunction.DevFunctionHelper;
import com.czw.smartkit.bleModule.callback.DeviceUIValueCallback;
import com.czw.smartkit.bleModule.data.DevFunctionEntity;
import com.czw.smartkit.bleModule.measure.DevMeasureBean;
import com.czw.smartkit.bleModule.measure.DevMeasureCallback;
import com.czw.smartkit.bleModule.measure.DevMeasureUtil;
import com.czw.smartkit.bleModule.measure.MeasureType;
import com.czw.smartkit.databaseModule.blood.BloodDataTable;
import com.czw.smartkit.databaseModule.blood.BloodServiceImpl;
import com.czw.smartkit.databaseModule.hr.HrDataTable;
import com.czw.smartkit.databaseModule.hr.HrServiceImpl;
import com.czw.smartkit.databaseModule.ox.OxDataTable;
import com.czw.smartkit.databaseModule.ox.OxServiceImpl;
import com.czw.smartkit.homeModule.syncData.SyncDataType;
import com.czw.smartkit.homeModule.syncData.SyncDataUtil;
import com.czw.smartkit.measure.MeasureBloodActivity;
import com.czw.smartkit.measure.MeasureHrActivity;
import com.czw.smartkit.measure.MeasureOxActivity;
import com.czw.smartkit.measure.history.BloodHistoryActivity;
import com.czw.smartkit.measure.history.HrHistoryActivity;
import com.czw.smartkit.measure.history.OxHistoryActivity;
import com.czw.smartkit.user.UserUtil;
import com.czw.utils.LogUtil;
import me.panpf.sketch.uri.FileUriModel;

/* loaded from: classes.dex */
public class FragmentMeasure extends BaseFragment implements View.OnClickListener, DeviceUIValueCallback, SyncDataUtil.SyncDataCallback, DevMeasureCallback, DevFunctionHelper.DeviceFunctionCallback {
    private View bp_history;
    private View hr_history;
    private TextView lastBdTextView;
    private TextView lastHrTextView;
    private TextView lastOxTextView;
    private View ox_history;
    private ProgressBar proBar;
    private boolean isOneKeyMeasureIng = true;
    private BleManager bleManager = BleManager.getBleManager();

    private void handOnekeyMeasure() {
        if (this.isOneKeyMeasureIng) {
            this.proBar.setVisibility(0);
            this.bleManager.writeData(DataStruct.measure(true, 3));
        } else {
            this.proBar.setVisibility(8);
            this.bleManager.writeData(DataStruct.measure(false, 3));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateShowItem(DevFunctionEntity devFunctionEntity) {
        this.hr_history.setVisibility(8);
        this.ox_history.setVisibility(8);
        this.bp_history.setVisibility(8);
        if (devFunctionEntity == null) {
            return;
        }
        if (devFunctionEntity.isSupportHr()) {
            this.hr_history.setVisibility(0);
        }
        if (devFunctionEntity.isSupportOx()) {
            this.ox_history.setVisibility(0);
        }
        if (devFunctionEntity.isSupportBlood()) {
            this.bp_history.setVisibility(0);
        }
    }

    @Override // com.czw.modes.fragment.RootFragment
    public void initAfterCreate() {
        this.lastHrTextView = (TextView) $View(R.id.lastHr);
        this.lastOxTextView = (TextView) $View(R.id.lastOx);
        this.lastBdTextView = (TextView) $View(R.id.lastBlood);
        this.hr_history = $View(R.id.hr_history);
        this.ox_history = $View(R.id.ox_history);
        this.bp_history = $View(R.id.blood_history);
        this.proBar = (ProgressBar) $View(R.id.proBar);
        $View(R.id.measureAll).setOnClickListener(this);
        $View(R.id.measureHr).setOnClickListener(this);
        $View(R.id.measureOx).setOnClickListener(this);
        $View(R.id.measureBd).setOnClickListener(this);
        $View(R.id.hr_history).setOnClickListener(this);
        $View(R.id.ox_history).setOnClickListener(this);
        $View(R.id.blood_history).setOnClickListener(this);
        refreshLastData();
        SyncDataUtil.getInstance().registerCallback(this);
        DevMeasureUtil.getInstance().registerCallback(this);
        DevMeasureUtil.getInstance().setDeviceUIValueCallback(this);
        DevFunctionHelper.getInstance().registerDeviceFunctionCallback(this);
        onGetFunction(DevFunctionHelper.getInstance().getDevFunctionEntity());
    }

    @Override // com.czw.modes.fragment.RootFragment
    public int loadLayout() {
        return R.layout.fragment_measure;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.blood_history /* 2131296319 */:
                this.bleManager.writeData(DataStruct.createHistoryHealthData(0));
                jumpTo(BloodHistoryActivity.class);
                return;
            case R.id.hr_history /* 2131296487 */:
                this.bleManager.writeData(DataStruct.createHistoryHealthData(0));
                jumpTo(HrHistoryActivity.class);
                return;
            case R.id.measureAll /* 2131296610 */:
                if (!this.bleManager.isConn()) {
                    toast(R.string.not_conn);
                    return;
                } else {
                    this.isOneKeyMeasureIng = !this.isOneKeyMeasureIng;
                    handOnekeyMeasure();
                    return;
                }
            case R.id.measureBd /* 2131296611 */:
                if (this.bleManager.isConn()) {
                    jumpTo(MeasureBloodActivity.class);
                    return;
                } else {
                    toast(R.string.not_conn);
                    return;
                }
            case R.id.measureHr /* 2131296613 */:
                if (this.bleManager.isConn()) {
                    jumpTo(MeasureHrActivity.class);
                    return;
                } else {
                    toast(R.string.not_conn);
                    return;
                }
            case R.id.measureOx /* 2131296614 */:
                if (this.bleManager.isConn()) {
                    jumpTo(MeasureOxActivity.class);
                    return;
                } else {
                    toast(R.string.not_conn);
                    return;
                }
            case R.id.ox_history /* 2131296642 */:
                this.bleManager.writeData(DataStruct.createHistoryHealthData(0));
                jumpTo(OxHistoryActivity.class);
                return;
            default:
                return;
        }
    }

    @Override // com.czw.smartkit.homeModule.syncData.SyncDataUtil.SyncDataCallback
    public void onDataSyncFinish(SyncDataType syncDataType) {
        if (syncDataType == SyncDataType.HR || syncDataType == SyncDataType.BLOOD || syncDataType == SyncDataType.OX) {
            refreshLastData();
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        SyncDataUtil.getInstance().unRegisterCallback(this);
        DevFunctionHelper.getInstance().unRegisterDeviceFunctionCallback(this);
    }

    @Override // com.czw.smartkit.bleModule.callback.DeviceUIValueCallback
    public void onDeviceUIvalue(final int i, final int i2, final int i3, final int i4) {
        if (getActivity() == null || this.lastHrTextView == null) {
            return;
        }
        LogUtil.e("设备上的测量数据:" + i + FileUriModel.SCHEME + i4 + FileUriModel.SCHEME + i2 + FileUriModel.SCHEME + i3);
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.homeModule.fragment.FragmentMeasure.1
            @Override // java.lang.Runnable
            public void run() {
                if (i != 0) {
                    FragmentMeasure.this.lastHrTextView.setText(i + "bpm");
                }
                if (i4 != 0) {
                    FragmentMeasure.this.lastOxTextView.setText(i4 + "%");
                }
                if (i2 == 0 || i3 == 0) {
                    return;
                }
                FragmentMeasure.this.lastBdTextView.setText(i2 + FileUriModel.SCHEME + i3 + "mmHg");
            }
        });
    }

    @Override // com.czw.smartkit.bleModule.DevFunction.DevFunctionHelper.DeviceFunctionCallback
    public void onGetFunction(final DevFunctionEntity devFunctionEntity) {
        if (getActivity() == null || this.hr_history == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() { // from class: com.czw.smartkit.homeModule.fragment.FragmentMeasure.6
            @Override // java.lang.Runnable
            public void run() {
                FragmentMeasure.this.updateShowItem(devFunctionEntity);
            }
        });
    }

    @Override // com.czw.smartkit.bleModule.measure.DevMeasureCallback
    public void onMeasure(final DevMeasureBean devMeasureBean) {
        if (getActivity() == null || this.lastHrTextView == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() { // from class: com.czw.smartkit.homeModule.fragment.FragmentMeasure.4
            @Override // java.lang.Runnable
            public void run() {
                if (devMeasureBean.getIntHr() != 0) {
                    FragmentMeasure.this.lastHrTextView.setText(devMeasureBean.getIntHr() + "bpm");
                }
                if (devMeasureBean.getIntOx() != 0) {
                    FragmentMeasure.this.lastOxTextView.setText(devMeasureBean.getIntOx() + "%");
                }
                if ((devMeasureBean.getIntBpH() != 0) && (devMeasureBean.getIntBpL() != 0)) {
                    FragmentMeasure.this.lastBdTextView.setText(devMeasureBean.getIntBpH() + FileUriModel.SCHEME + devMeasureBean.getIntBpL() + "mmHg");
                }
            }
        });
    }

    @Override // android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        DevMeasureUtil.getInstance().setDeviceUIValueCallback(null);
        DevMeasureUtil.getInstance().unRegisterCallback(null);
    }

    @Override // com.czw.smartkit.bleModule.measure.DevMeasureCallback
    public void onStartMeasure(MeasureType measureType) {
        if (getActivity() == null || this.proBar == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() { // from class: com.czw.smartkit.homeModule.fragment.FragmentMeasure.3
            @Override // java.lang.Runnable
            public void run() {
                FragmentMeasure.this.proBar.setVisibility(0);
            }
        });
    }

    @Override // com.czw.smartkit.bleModule.measure.DevMeasureCallback
    public void onStopMeasure(MeasureType measureType) {
        this.isOneKeyMeasureIng = false;
        if (getActivity() == null || this.proBar == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() { // from class: com.czw.smartkit.homeModule.fragment.FragmentMeasure.5
            @Override // java.lang.Runnable
            public void run() {
                FragmentMeasure.this.proBar.setVisibility(8);
            }
        });
    }

    public void refreshLastData() {
        if (getActivity() == null || this.lastHrTextView == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() { // from class: com.czw.smartkit.homeModule.fragment.FragmentMeasure.2
            @Override // java.lang.Runnable
            public void run() {
                HrDataTable findLast = HrServiceImpl.getInstance().findLast(UserUtil.getUid());
                if (findLast != null && !TextUtils.isEmpty(findLast.getNumber()) && !findLast.getNumber().equalsIgnoreCase(AmapLoc.RESULT_TYPE_GPS)) {
                    FragmentMeasure.this.lastHrTextView.setText(findLast.getNumber() + "bpm");
                }
                OxDataTable findLast2 = OxServiceImpl.getInstance().findLast(UserUtil.getUid());
                if (findLast2 != null && !TextUtils.isEmpty(findLast2.getNumber()) && !findLast2.getNumber().equalsIgnoreCase(AmapLoc.RESULT_TYPE_GPS)) {
                    FragmentMeasure.this.lastOxTextView.setText(findLast2.getNumber() + "%");
                }
                BloodDataTable findLast3 = BloodServiceImpl.getInstance().findLast(UserUtil.getUid());
                if (findLast3 == null || TextUtils.isEmpty(findLast3.getBpH()) || TextUtils.isEmpty(findLast3.getBpL())) {
                    return;
                }
                String bpH = findLast3.getBpH();
                String bpL = findLast3.getBpL();
                if (bpH.equalsIgnoreCase(AmapLoc.RESULT_TYPE_GPS) || bpL.equalsIgnoreCase(AmapLoc.RESULT_TYPE_GPS)) {
                    return;
                }
                FragmentMeasure.this.lastBdTextView.setText(bpH + FileUriModel.SCHEME + bpL + "mmHg");
            }
        });
    }
}
