package com.czw.smartkit.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import com.czw.smartkit.R;
import com.czw.smartkit.bleModule.BleManager;
import com.czw.utils.PhoneFunctionUtil;
import npPermission.nopointer.core.RequestPermissionInfo;

/* loaded from: classes.dex */
public abstract class BaseBleScanActivity extends TitleActivity {
    private static final int REQUEST_ENABLE_BT = 111;
    private BleManager bleManager = BleManager.getBleManager();
    private boolean isOpenLocation = false;
    private boolean isGetPermission = false;
    private boolean isBleOpen = false;

    private void dialogForLocation() {
        AlertDialog create = new AlertDialog.Builder(this).setTitle(R.string.scan_need_location_open_title).setMessage(R.string.scan_need_location_open_message).setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() { // from class: com.czw.smartkit.base.BaseBleScanActivity.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                PhoneFunctionUtil.jump2LocationSetting(BaseBleScanActivity.this);
            }
        }).setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null).create();
        create.setCanceledOnTouchOutside(false);
        create.setCancelable(false);
        create.show();
    }

    private void jump2OpenBleSetting() {
        startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 111);
    }

    private void requestLocationPermission() {
        RequestPermissionInfo requestPermissionInfo = new RequestPermissionInfo();
        requestPermissionInfo.setPermissionArr(new String[]{"android.permission.ACCESS_FINE_LOCATION"});
        requestPermissionInfo.setPermissionMessage($str(R.string.permission_location_message));
        requestPermissionInfo.setPermissionCancelText(getString(android.R.string.cancel));
        requestPermissionInfo.setPermissionSureText(getString(android.R.string.ok));
        requestPermissionInfo.setAgainPermissionTitle($str(R.string.permission_agin_title));
        requestPermissionInfo.setAgainPermissionMessage($str(R.string.permission_location_agin_message));
        requestPermissionInfo.setAgainPermissionSureText(getString(android.R.string.ok));
        requestPermissionInfo.setAgainPermissionCancelText(getString(android.R.string.cancel));
        requestPermission(requestPermissionInfo);
    }

    private void verifyCanScanDevice() {
        if (!this.isGetPermission) {
            requestLocationPermission();
        } else if (!this.isBleOpen) {
            jump2OpenBleSetting();
        } else {
            if (this.isOpenLocation) {
                return;
            }
            dialogForLocation();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.czw.smartkit.base.TitleActivity, com.czw.smartkit.base.BaseActivity
    public void insertInit() {
        super.insertInit();
        this.isOpenLocation = PhoneFunctionUtil.isLocationModeOpenWith23(this);
        this.isBleOpen = this.bleManager.isBLeEnabled();
    }

    public boolean isCanScanDevice() {
        this.isOpenLocation = PhoneFunctionUtil.isLocationModeOpenWith23(this);
        this.isBleOpen = this.bleManager.isBLeEnabled();
        verifyCanScanDevice();
        return this.isGetPermission && this.isOpenLocation && this.isBleOpen;
    }

    @Override // com.czw.smartkit.base.BasePermissionActivity, npPermission.nopointer.core.callback.PermissionCallback
    public void onGetAllPermission() {
        super.onGetAllPermission();
        this.isGetPermission = true;
        verifyCanScanDevice();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.czw.smartkit.base.TitleActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.isOpenLocation = PhoneFunctionUtil.isLocationModeOpenWith23(this);
    }
}
