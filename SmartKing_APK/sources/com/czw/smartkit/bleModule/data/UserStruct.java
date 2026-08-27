package com.czw.smartkit.bleModule.data;

import android.support.v4.view.MotionEventCompat;
import ycble.runchinaup.util.BleUtil;

/* loaded from: classes.dex */
public class UserStruct {
    public int weight = 60;
    public int age = 20;
    public int height = 170;
    public int stepLen = 70;
    public boolean isMan = true;
    public int targetStep = 1000;

    public static UserStruct onParser(byte[] bArr) {
        UserStruct userStruct = new UserStruct();
        userStruct.isMan = (bArr[0] & 255) == 0;
        userStruct.age = bArr[1] & 255;
        userStruct.stepLen = bArr[2] & 255;
        userStruct.height = BleUtil.byte2IntLR(new byte[0]);
        userStruct.weight = (bArr[5] & 255) | ((bArr[6] & 255) << 8);
        return userStruct;
    }

    public static byte[] packData(UserStruct userStruct) {
        return new byte[]{-31, 1, 0, 10, (byte) ((userStruct.weight & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8), (byte) (userStruct.weight & 255), (byte) userStruct.age, (byte) userStruct.height, (byte) userStruct.stepLen, userStruct.isMan ? (byte) 1 : (byte) 0, (byte) ((userStruct.targetStep & (-16777216)) >> 24), (byte) ((userStruct.targetStep & 16711680) >> 16), (byte) ((65280 & userStruct.targetStep) >> 8), (byte) (userStruct.targetStep & 255)};
    }
}
