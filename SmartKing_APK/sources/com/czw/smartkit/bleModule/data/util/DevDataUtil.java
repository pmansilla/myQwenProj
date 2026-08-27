package com.czw.smartkit.bleModule.data.util;

import com.czw.smartkit.bleModule.data.DevFunctionEntity;
import com.czw.smartkit.netModule.entity.WeatherEntity;
import ycble.runchinaup.util.BleUtil;

/* loaded from: classes.dex */
public class DevDataUtil {
    private DevDataUtil() {
    }

    public static byte[] createWeather(WeatherEntity weatherEntity) {
        byte[] bArr = new byte[4];
        bArr[0] = 5;
        bArr[1] = (byte) (Integer.valueOf(weatherEntity.getWeatherCode()).intValue() + 0);
        int intValue = Integer.valueOf(weatherEntity.getTemperature()).intValue();
        bArr[2] = (byte) (intValue <= 0 ? 1 : 0);
        bArr[3] = (byte) Math.abs(intValue);
        return bArr;
    }

    public static DevFunctionEntity getDevFunctionList(byte[] bArr) {
        DevFunctionEntity devFunctionEntity = new DevFunctionEntity();
        devFunctionEntity.branchNumber = BleUtil.byte2IntLR(bArr[1]);
        devFunctionEntity.setPlatform(BleUtil.byte2IntLR(bArr[2]));
        int byte2IntLR = BleUtil.byte2IntLR(bArr[3]);
        devFunctionEntity.setModel(byte2IntLR);
        devFunctionEntity.branchNumber = byte2IntLR == 1 ? 1 : 2;
        int byte2IntLR2 = BleUtil.byte2IntLR(bArr[4]);
        devFunctionEntity.setSupportHr((byte2IntLR2 & 1) == 1);
        devFunctionEntity.setSupportBlood((byte2IntLR2 & 2) == 2);
        devFunctionEntity.setSupportOx((byte2IntLR2 & 4) == 4);
        devFunctionEntity.setSupportOTA(BleUtil.byte2IntLR(bArr[5]) == 1);
        devFunctionEntity.setSupportWeather(BleUtil.byte2IntLR(bArr[6]) == 1);
        devFunctionEntity.setClockCount(BleUtil.byte2IntLR(bArr[7]));
        devFunctionEntity.setSupportRemind(BleUtil.byte2IntLR(bArr[8]) == 1);
        int byte2IntLR3 = BleUtil.byte2IntLR(bArr[9]);
        devFunctionEntity.setSupportStep((byte2IntLR3 & 1) == 1);
        devFunctionEntity.setSupportSleep((byte2IntLR3 & 2) == 2);
        return devFunctionEntity;
    }
}
