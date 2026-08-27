package com.czw.smartkit.base;

import com.czw.smartkit.R;
import com.czw.smartkit.bleModule.BleManager;
import ycble.runchinaup.core.BleConnState;
import ycble.runchinaup.core.callback.BleConnCallback;

/* loaded from: classes.dex */
public abstract class BaseBleConnTitleActivity extends TitleActivity implements BleConnCallback {
    private BleManager bleManager = BleManager.getBleManager();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.czw.smartkit.base.TitleActivity, com.czw.smartkit.base.BaseActivity
    public void insertInit() {
        super.insertInit();
        this.bleManager.registerConnCallback(this);
    }

    @Override // ycble.runchinaup.core.callback.BleConnCallback
    public void onConnState(BleConnState bleConnState) {
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.base.BaseBleConnTitleActivity.1
            @Override // java.lang.Runnable
            public void run() {
                BaseBleConnTitleActivity.this.toast(R.string.ble_is_disconn);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.czw.smartkit.base.BaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        this.bleManager.unRegisterConnCallback(this);
    }
}
