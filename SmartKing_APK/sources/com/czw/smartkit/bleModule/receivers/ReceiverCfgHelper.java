package com.czw.smartkit.bleModule.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import basecamera.module.cfg.BaseCameraCfg;
import com.czw.smartkit.bleModule.BleManager;
import com.czw.smartkit.bleModule.DataStruct;
import com.czw.smartkit.bleModule.LanguageUtil;
import com.czw.utils.LogUtil;
import ycble.runchinaup.log.ycBleLog;

/* loaded from: classes.dex */
public class ReceiverCfgHelper extends BroadcastReceiver {
    private static final ReceiverCfgHelper ourInstance = new ReceiverCfgHelper();

    private ReceiverCfgHelper() {
    }

    public static ReceiverCfgHelper getInstance() {
        return ourInstance;
    }

    IntentFilter createIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.LOCALE_CHANGED");
        intentFilter.addAction(BaseCameraCfg.exitTakePhotoForDev);
        return intentFilter;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (intent == null || TextUtils.isEmpty(intent.getAction())) {
            return;
        }
        String action = intent.getAction();
        char c = 65535;
        int hashCode = action.hashCode();
        if (hashCode != -19011148) {
            if (hashCode == 1692212397 && action.equals(BaseCameraCfg.exitTakePhotoForDev)) {
                c = 1;
            }
        } else if (action.equals("android.intent.action.LOCALE_CHANGED")) {
            c = 0;
        }
        switch (c) {
            case 0:
                LogUtil.e("手机系统语言切换了===>" + ((int) LanguageUtil.getLanguageCode()));
                BleManager.getBleManager().writeData(DataStruct.currentTime());
                return;
            case 1:
                ycBleLog.e("退出设备项目模式");
                BleManager.getBleManager().writeData(DataStruct.createTakePhoto(false));
                return;
            default:
                return;
        }
    }

    public void register(Context context) {
        try {
            context.registerReceiver(this, createIntentFilter());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unRegister(Context context) {
        try {
            context.unregisterReceiver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
