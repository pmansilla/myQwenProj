package com.czw.smartkit.preferenceModule;

import android.text.TextUtils;
import com.czw.smartkit.databaseModule.userDevice.UserDeviceDatabaseUtil;
import com.czw.smartkit.databaseModule.userDevice.UserDeviceEntity;
import com.czw.smartkit.user.UserUtil;
import ycble.runchinaup.device.BleDevice;

/* loaded from: classes.dex */
public class SharePreferenceDevice {
    public static void clear() {
        UserDeviceDatabaseUtil.getInstance().deleteData(UserUtil.getUid());
    }

    public static BleDevice read() {
        UserDeviceEntity userDevice = UserDeviceDatabaseUtil.getInstance().getUserDevice(UserUtil.getUid());
        if (userDevice == null || TextUtils.isEmpty(userDevice.getMac())) {
            return null;
        }
        return new BleDevice(userDevice.getName(), userDevice.getMac());
    }

    public static void save(BleDevice bleDevice) {
        UserDeviceEntity userDeviceEntity = new UserDeviceEntity();
        userDeviceEntity.setUid(UserUtil.getUid());
        userDeviceEntity.setMac(bleDevice.getMac());
        userDeviceEntity.setName(bleDevice.getName());
        UserDeviceDatabaseUtil.getInstance().saveData(userDeviceEntity);
    }
}
