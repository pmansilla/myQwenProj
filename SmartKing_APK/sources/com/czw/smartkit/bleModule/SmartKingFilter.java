package com.czw.smartkit.bleModule;

import com.czw.utils.LogUtil;
import com.google.gson.Gson;
import ycble.runchinaup.core.BleDeviceFilter;
import ycble.runchinaup.device.BleDevice;
import ycble.runchinaup.util.BleUtil;

/* loaded from: classes.dex */
public class SmartKingFilter extends BleDeviceFilter<BleDevice> {
    private static final String filterStr = "FF1600";
    private static final String filterStr2 = "FF1700";
    private static SmartKingFilter instance = new SmartKingFilter();

    private SmartKingFilter() {
    }

    public static SmartKingFilter getInstance() {
        return instance;
    }

    @Override // ycble.runchinaup.core.BleDeviceFilter
    public boolean filter(BleDevice bleDevice) {
        LogUtil.e("debug=filter=" + new Gson().toJson(bleDevice));
        if (bleDevice == null || bleDevice.getScanBytes() == null) {
            return false;
        }
        String byte2HexStr = BleUtil.byte2HexStr(bleDevice.getScanBytes());
        if (byte2HexStr.length() < 20) {
            return false;
        }
        return byte2HexStr.contains(filterStr) || byte2HexStr.contains(filterStr2);
    }
}
