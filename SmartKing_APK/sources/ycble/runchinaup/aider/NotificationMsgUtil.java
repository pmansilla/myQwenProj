package ycble.runchinaup.aider;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.NotificationManagerCompat;
import com.autonavi.amap.mapcore.AMapEngineUtils;
import com.google.gson.Gson;
import java.util.List;
import java.util.Set;
import ycble.runchinaup.log.ycBleLog;

/* loaded from: classes2.dex */
public final class NotificationMsgUtil {
    public static void closeService(Context context) {
        context.getPackageManager().setComponentEnabledSetting(new ComponentName(context, (Class<?>) NPNotificationService.class), 2, 1);
    }

    public static void goToSettingAccessibility(Context context) {
        try {
            Intent intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
            intent.addFlags(AMapEngineUtils.MAX_P20_WIDTH);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void goToSettingNotificationAccess(Context context) {
        try {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            intent.addFlags(AMapEngineUtils.MAX_P20_WIDTH);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isEnabled(Context context) {
        Set<String> enabledListenerPackages = NotificationManagerCompat.getEnabledListenerPackages(context);
        ycBleLog.e("获取了通知栏监听权限的应用包名:" + new Gson().toJson(enabledListenerPackages));
        return enabledListenerPackages.contains(context.getPackageName());
    }

    public static boolean isServiceExisted(Context context, Class cls) {
        String name = cls.getName();
        List<ActivityManager.RunningServiceInfo> runningServices = ((ActivityManager) context.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE);
        if (runningServices.size() <= 0) {
            return false;
        }
        for (int i = 0; i < runningServices.size(); i++) {
            if (runningServices.get(i).service.getClassName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static void reStartNotifyListenService(Context context) {
        ycBleLog.e("reBindService==>NPNotificationService");
        ComponentName componentName = new ComponentName(context, (Class<?>) NPNotificationService.class);
        PackageManager packageManager = context.getPackageManager();
        packageManager.setComponentEnabledSetting(componentName, 2, 1);
        packageManager.setComponentEnabledSetting(componentName, 1, 1);
    }
}
