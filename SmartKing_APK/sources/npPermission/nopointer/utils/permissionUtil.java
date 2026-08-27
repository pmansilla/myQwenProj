package npPermission.nopointer.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.appcompat.BuildConfig;
import com.autonavi.amap.mapcore.AMapEngineUtils;

/* loaded from: classes2.dex */
class permissionUtil {
    private static final String MANUFACTURER_HUAWEI = "Huawei";
    private static final String MANUFACTURER_LENOVO = "LENOVO";
    private static final String MANUFACTURER_LETV = "Letv";
    private static final String MANUFACTURER_LG = "LG";
    private static final String MANUFACTURER_MEIZU = "Meizu";
    private static final String MANUFACTURER_OPPO = "OPPO";
    private static final String MANUFACTURER_SAMSUNG = "samsung";
    private static final String MANUFACTURER_SONY = "Sony";
    private static final String MANUFACTURER_VIVO = "vivo";
    private static final String MANUFACTURER_XIAOMI = "Xiaomi";
    private static final String MANUFACTURER_YULONG = "YuLong";
    private static final String MANUFACTURER_ZTE = "ZTE";
    private static final int PERMISSION_SETTING_FOR_RESULT = 10;
    public static boolean isAppSettingOpen = false;

    permissionUtil() {
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static void GoToSetting(Activity activity) {
        char c;
        String str = Build.MANUFACTURER;
        switch (str.hashCode()) {
            case -2122609145:
                if (str.equals(MANUFACTURER_HUAWEI)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -1675632421:
                if (str.equals(MANUFACTURER_XIAOMI)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 2427:
                if (str.equals(MANUFACTURER_LG)) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 2364891:
                if (str.equals(MANUFACTURER_LETV)) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 2432928:
                if (str.equals(MANUFACTURER_OPPO)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 2582855:
                if (str.equals(MANUFACTURER_SONY)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 74224812:
                if (str.equals(MANUFACTURER_MEIZU)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                Huawei(activity);
                return;
            case 1:
                Meizu(activity);
                return;
            case 2:
                Xiaomi(activity);
                return;
            case 3:
                Sony(activity);
                return;
            case 4:
                OPPO(activity);
                return;
            case 5:
                LG(activity);
                return;
            case 6:
                Letv(activity);
                return;
            default:
                try {
                    openAppDetailSetting(activity);
                    return;
                } catch (Exception unused) {
                    SystemConfig(activity);
                    return;
                }
        }
    }

    public static void Huawei(Activity activity) {
        try {
            Intent intent = new Intent();
            intent.setFlags(AMapEngineUtils.MAX_P20_WIDTH);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity"));
            activity.startActivityForResult(intent, 10);
            isAppSettingOpen = false;
        } catch (Exception unused) {
            openAppDetailSetting(activity);
        }
    }

    public static void LG(Activity activity) {
        try {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setFlags(AMapEngineUtils.MAX_P20_WIDTH);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity"));
            activity.startActivity(intent);
            isAppSettingOpen = false;
        } catch (Exception unused) {
            openAppDetailSetting(activity);
        }
    }

    public static void Letv(Activity activity) {
        try {
            Intent intent = new Intent();
            intent.setFlags(AMapEngineUtils.MAX_P20_WIDTH);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.PermissionAndApps"));
            activity.startActivity(intent);
            isAppSettingOpen = false;
        } catch (Exception unused) {
            openAppDetailSetting(activity);
        }
    }

    public static void Meizu(Activity activity) {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            activity.startActivity(intent);
            isAppSettingOpen = false;
        } catch (Exception unused) {
            openAppDetailSetting(activity);
        }
    }

    public static void OPPO(Activity activity) {
        try {
            Intent intent = new Intent();
            intent.setFlags(AMapEngineUtils.MAX_P20_WIDTH);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            intent.setComponent(new ComponentName("com.color.safecenter", "com.color.safecenter.permission.PermissionManagerActivity"));
            activity.startActivity(intent);
            isAppSettingOpen = false;
        } catch (Exception unused) {
            openAppDetailSetting(activity);
        }
    }

    public static void Sony(Activity activity) {
        try {
            Intent intent = new Intent();
            intent.setFlags(AMapEngineUtils.MAX_P20_WIDTH);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            intent.setComponent(new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity"));
            activity.startActivity(intent);
            isAppSettingOpen = false;
        } catch (Exception unused) {
            openAppDetailSetting(activity);
        }
    }

    public static void SystemConfig(Activity activity) {
        activity.startActivity(new Intent("android.settings.SETTINGS"));
    }

    public static void Xiaomi(Activity activity) {
        try {
            try {
                Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
                intent.putExtra("extra_pkgname", activity.getPackageName());
                activity.startActivityForResult(intent, 10);
                isAppSettingOpen = false;
            } catch (Exception unused) {
                Intent intent2 = new Intent("miui.intent.action.APP_PERM_EDITOR");
                intent2.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                intent2.putExtra("extra_pkgname", activity.getPackageName());
                activity.startActivityForResult(intent2, 10);
                isAppSettingOpen = false;
            }
        } catch (Exception unused2) {
            openAppDetailSetting(activity);
        }
    }

    public static void _360(Activity activity) {
        try {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setFlags(AMapEngineUtils.MAX_P20_WIDTH);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            intent.setComponent(new ComponentName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity"));
            activity.startActivity(intent);
        } catch (Exception unused) {
            openAppDetailSetting(activity);
        }
    }

    private static Intent getAppDetailSettingIntent(Activity activity) {
        Intent intent = new Intent();
        intent.addFlags(AMapEngineUtils.MAX_P20_WIDTH);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction("android.intent.action.VIEW");
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", activity.getPackageName());
        }
        return intent;
    }

    public static void openAppDetailSetting(Activity activity) {
        activity.startActivityForResult(getAppDetailSettingIntent(activity), 10);
        isAppSettingOpen = true;
    }
}
