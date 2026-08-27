package com.mob.mobapm.e;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import com.mob.tools.utils.ReflectHelper;
import java.util.List;

/* loaded from: classes.dex */
public class b {
    public static boolean a(Context context) {
        List<ActivityManager.RunningAppProcessInfo> list;
        String str = null;
        try {
            list = (List) ReflectHelper.invokeInstanceMethod((ActivityManager) context.getSystemService("activity"), "getRunningAppProcesses", new Object[0]);
        } catch (Throwable th) {
            com.mob.mobapm.d.a.a().e(th);
        }
        if (list == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : list) {
            try {
                if (runningAppProcessInfo.pid == Process.myPid()) {
                    str = runningAppProcessInfo.processName;
                }
            } catch (Throwable th2) {
                com.mob.mobapm.d.a.a().e(th2);
            }
        }
        boolean z = str != null && str.equalsIgnoreCase(context.getPackageName());
        com.mob.mobapm.d.a.a().d("APM: isInMainProcess process:" + str + ",res:" + z, new Object[0]);
        return z;
    }
}
