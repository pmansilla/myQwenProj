package npPermission.nopointer.utils;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import com.autonavi.amap.mapcore.AMapEngineUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import npPermission.nopointer.log.NpPerLog;

/* loaded from: classes2.dex */
public class PermissionPageUtils {
    private PermissionPageUtils() {
    }

    private static void doStartApplicationWithPackageName(String str, Context context) throws PackageManager.NameNotFoundException {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(str, 0);
            if (packageInfo == null) {
                return;
            }
            Intent intent = new Intent("android.intent.action.MAIN", (Uri) null);
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.setPackage(packageInfo.packageName);
            List<ResolveInfo> queryIntentActivities = context.getPackageManager().queryIntentActivities(intent, 0);
            NpPerLog.log("resolveinfoList" + queryIntentActivities.size());
            for (int i = 0; i < queryIntentActivities.size(); i++) {
                NpPerLog.log(queryIntentActivities.get(i).activityInfo.packageName + queryIntentActivities.get(i).activityInfo.name);
            }
            ResolveInfo next = queryIntentActivities.iterator().next();
            if (next != null) {
                String str2 = next.activityInfo.packageName;
                String str3 = next.activityInfo.name;
                Intent intent2 = new Intent("android.intent.action.MAIN");
                intent2.addCategory("android.intent.category.LAUNCHER");
                intent2.setComponent(new ComponentName(str2, str3));
                try {
                    context.startActivity(intent2);
                } catch (Exception e) {
                    goIntentSetting(context);
                    e.printStackTrace();
                }
            }
        } catch (PackageManager.NameNotFoundException e2) {
            throw e2;
        }
    }

    private static Intent getAppDetailSettingIntent(Context context) {
        Intent intent = new Intent();
        intent.addFlags(AMapEngineUtils.MAX_P20_WIDTH);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction("android.intent.action.VIEW");
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        return intent;
    }

    /* JADX WARN: Not initialized variable reg: 2, insn: 0x004f: MOVE (r1 I:??[OBJECT, ARRAY]) = (r2 I:??[OBJECT, ARRAY]), block:B:24:0x004f */
    private static String getMiuiVersion() {
        BufferedReader bufferedReader;
        BufferedReader bufferedReader2;
        BufferedReader bufferedReader3 = null;
        try {
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("getprop ro.miui.ui.version.name").getInputStream()), 1024);
                try {
                    String readLine = bufferedReader.readLine();
                    bufferedReader.close();
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return readLine;
                } catch (IOException e2) {
                    e = e2;
                    e.printStackTrace();
                    try {
                        bufferedReader.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                    return null;
                }
            } catch (Throwable th) {
                th = th;
                bufferedReader3 = bufferedReader2;
                try {
                    bufferedReader3.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
                throw th;
            }
        } catch (IOException e5) {
            e = e5;
            bufferedReader = null;
        } catch (Throwable th2) {
            th = th2;
            bufferedReader3.close();
            throw th;
        }
    }

    private static void goCoolpadMainager(Context context) throws PackageManager.NameNotFoundException {
        doStartApplicationWithPackageName("com.yulong.android.security:remote", context);
    }

    private static void goHuaWeiMainager(String str, Context context) {
        try {
            Intent intent = new Intent(str);
            intent.setFlags(AMapEngineUtils.MAX_P20_WIDTH);
            intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity"));
            context.startActivity(intent);
        } catch (Exception e) {
            NpPerLog.log("跳转失败" + e.getMessage());
            e.printStackTrace();
            goIntentSetting(context);
        }
    }

    private static void goIntentSetting(Context context) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void goLGMainager(String str, Context context) {
        try {
            Intent intent = new Intent(str);
            intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity"));
            context.startActivity(intent);
        } catch (Exception e) {
            NpPerLog.log("跳转失败" + e.getMessage());
            e.printStackTrace();
            goIntentSetting(context);
        }
    }

    private static void goMeizuMainager(String str, Context context) {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.putExtra("packageName", str);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            goIntentSetting(context);
        }
    }

    private static void goOppoMainager(Context context) throws PackageManager.NameNotFoundException {
        doStartApplicationWithPackageName("com.coloros.safecenter", context);
    }

    private static void goSangXinMainager(Context context) {
        goIntentSetting(context);
    }

    private static void goSonyMainager(String str, Context context) {
        try {
            Intent intent = new Intent(str);
            intent.setComponent(new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity"));
            context.startActivity(intent);
        } catch (Exception e) {
            NpPerLog.log("跳转失败" + e.getMessage());
            e.printStackTrace();
            goIntentSetting(context);
        }
    }

    private static void goVivoMainager(Context context) throws PackageManager.NameNotFoundException {
        doStartApplicationWithPackageName("com.iqoo.secure", context);
    }

    private static void goXiaoMiMainager(String str, Context context) {
        String miuiVersion = getMiuiVersion();
        NpPerLog.log("goMiaoMiMainager --- rom : " + miuiVersion);
        Intent intent = new Intent();
        if ("V6".equals(miuiVersion) || "V7".equals(miuiVersion)) {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            intent.putExtra("extra_pkgname", str);
        } else if ("V8".equals(miuiVersion) || "V9".equals(miuiVersion) || "V10".equals(miuiVersion) || "V11".equals(miuiVersion) || "V12".equals(miuiVersion)) {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            intent.putExtra("extra_pkgname", str);
        } else {
            goIntentSetting(context);
        }
        context.startActivity(intent);
    }

    public static void jumpPermissionPage(Context context) {
        String upperCase = Build.MANUFACTURER.toUpperCase();
        String packageName = context.getPackageName();
        NpPerLog.log("jumpPermissionPage --- name : " + upperCase);
        char c = 65535;
        try {
            switch (upperCase.hashCode()) {
                case -1712043046:
                    if (upperCase.equals("SAMSUNG")) {
                        c = 6;
                        break;
                    }
                    break;
                case -1706170181:
                    if (upperCase.equals("XIAOMI")) {
                        c = 5;
                        break;
                    }
                    break;
                case 2427:
                    if (upperCase.equals("LG")) {
                        c = '\b';
                        break;
                    }
                    break;
                case 2432928:
                    if (upperCase.equals("OPPO")) {
                        c = 2;
                        break;
                    }
                    break;
                case 2551079:
                    if (upperCase.equals("SONY")) {
                        c = 7;
                        break;
                    }
                    break;
                case 2634924:
                    if (upperCase.equals("VIVO")) {
                        c = 1;
                        break;
                    }
                    break;
                case 73239724:
                    if (upperCase.equals("MEIZU")) {
                        c = 4;
                        break;
                    }
                    break;
                case 1670208650:
                    if (upperCase.equals("COOLPAD")) {
                        c = 3;
                        break;
                    }
                    break;
                case 2141820391:
                    if (upperCase.equals("HUAWEI")) {
                        c = 0;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    goHuaWeiMainager(packageName, context);
                    return;
                case 1:
                    goVivoMainager(context);
                    return;
                case 2:
                    goOppoMainager(context);
                    return;
                case 3:
                    goCoolpadMainager(context);
                    return;
                case 4:
                    goMeizuMainager(packageName, context);
                    return;
                case 5:
                    goXiaoMiMainager(packageName, context);
                    return;
                case 6:
                    goSangXinMainager(context);
                    return;
                case 7:
                    goSonyMainager(packageName, context);
                    return;
                case '\b':
                    goLGMainager(packageName, context);
                    return;
                default:
                    goIntentSetting(context);
                    return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent();
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
            context.startActivity(intent);
        }
    }
}
