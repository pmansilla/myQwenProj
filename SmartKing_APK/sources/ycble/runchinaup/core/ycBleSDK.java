package ycble.runchinaup.core;

import android.content.Context;
import ycble.runchinaup.aider.PushAiderHelper;

/* loaded from: classes.dex */
public class ycBleSDK {
    public static void initSDK(Context context) {
        initSDK(context, true);
    }

    public static void initSDK(Context context, boolean z) {
        BleScanner.init(context);
        AbsBleManager.initSDK(context);
        if (z) {
            PushAiderHelper.getAiderHelper().start(context);
        } else {
            PushAiderHelper.getAiderHelper().stop(context);
        }
    }

    public static void setScanLog(boolean z) {
        BleScanner.isShowScanLog = z;
    }
}
