package com.czw.smartkit.liveservice;

import android.app.ActivityManager;
import android.content.Context;
import java.util.List;

/* loaded from: classes.dex */
public class ServiceUtil {
    private ServiceUtil() {
    }

    public static boolean isServiceExisted(Context context, String str) {
        List<ActivityManager.RunningServiceInfo> runningServices = ((ActivityManager) context.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE);
        if (runningServices.size() <= 0) {
            return false;
        }
        for (int i = 0; i < runningServices.size(); i++) {
            if (runningServices.get(i).service.getClassName().equals(str)) {
                return true;
            }
        }
        return false;
    }
}
