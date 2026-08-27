package com.czw.smartkit.bleModule.util;

import android.text.TextUtils;
import com.czw.smartkit.BaseCfg;

/* loaded from: classes.dex */
public class FirmwareUtils {
    public static boolean isCanOTA(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return false;
        }
        int intValue = Integer.valueOf(str.replaceAll("\\.", "")).intValue();
        int intValue2 = Integer.valueOf(str2.replaceAll("\\.", "")).intValue();
        return BaseCfg.isAllowSameVersionOTA ? intValue2 >= intValue : intValue2 > intValue;
    }
}
