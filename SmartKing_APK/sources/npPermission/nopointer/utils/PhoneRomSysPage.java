package npPermission.nopointer.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import com.autonavi.amap.mapcore.AMapEngineUtils;
import npPermission.nopointer.log.NpPerLog;

/* loaded from: classes2.dex */
public class PhoneRomSysPage {
    private static String TAG = "PhoneRomSysPage";

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: npPermission.nopointer.utils.PhoneRomSysPage$1, reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$npPermission$nopointer$utils$PageType = new int[PageType.values().length];

        static {
            try {
                $SwitchMap$npPermission$nopointer$utils$PageType[PageType.AUTO_START.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    private PhoneRomSysPage() {
    }

    public static void goToHuaWeiSettingsPage(Context context, PageType pageType) {
        Intent intent = new Intent();
        intent.setFlags(AMapEngineUtils.MAX_P20_WIDTH);
        ComponentName componentName = AnonymousClass1.$SwitchMap$npPermission$nopointer$utils$PageType[pageType.ordinal()] != 1 ? null : Build.VERSION.SDK_INT >= 28 ? new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity") : Build.VERSION.SDK_INT >= 26 ? new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity") : Build.VERSION.SDK_INT >= 23 ? new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity") : ComponentName.unflattenFromString("com.huawei.systemmanager/com.huawei.permissionmanager.ui.MainActivity");
        try {
            if (componentName != null) {
                intent.setComponent(componentName);
                context.startActivity(intent);
            } else {
                NpPerLog.log("华为手机：componentName=null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void goToOppoSettingsPage(Context context, PageType pageType) {
        Intent intent = new Intent();
        intent.setFlags(AMapEngineUtils.MAX_P20_WIDTH);
        ComponentName unflattenFromString = AnonymousClass1.$SwitchMap$npPermission$nopointer$utils$PageType[pageType.ordinal()] != 1 ? null : Build.VERSION.SDK_INT >= 21 ? ComponentName.unflattenFromString("com.coloros.safecenter/.startupapp.StartupAppListActivity") : ComponentName.unflattenFromString("com.color.safecenter/.permission.startup.StartupAppListActivity");
        try {
            if (unflattenFromString != null) {
                intent.setComponent(unflattenFromString);
                context.startActivity(intent);
            } else {
                NpPerLog.log("oppo手机：componentName=null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void goToSettingPage(Context context, PageType pageType) {
        String str = Build.MANUFACTURER;
        if (str.equalsIgnoreCase("HUAWEI")) {
            goToHuaWeiSettingsPage(context, pageType);
        } else if (str.equalsIgnoreCase("Xiaomi")) {
            goToXiaoMiSettingsPage(context, pageType);
        } else if (str.equalsIgnoreCase("OPPO")) {
            goToOppoSettingsPage(context, pageType);
        } else if (str.equalsIgnoreCase("VIVO")) {
            goToVIVOSettingsPage(context, pageType);
        }
        NpPerLog.log("romName==>" + str);
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0039 A[Catch: Exception -> 0x0037, TRY_LEAVE, TryCatch #0 {Exception -> 0x0037, blocks: (B:9:0x0030, B:14:0x0039), top: B:7:0x002e }] */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0030 A[Catch: Exception -> 0x0037, TRY_ENTER, TryCatch #0 {Exception -> 0x0037, blocks: (B:9:0x0030, B:14:0x0039), top: B:7:0x002e }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void goToVIVOSettingsPage(android.content.Context r3, npPermission.nopointer.utils.PageType r4) {
        /*
            android.content.Intent r0 = new android.content.Intent
            r0.<init>()
            r1 = 268435456(0x10000000, float:2.5243549E-29)
            r0.setFlags(r1)
            int[] r1 = npPermission.nopointer.utils.PhoneRomSysPage.AnonymousClass1.$SwitchMap$npPermission$nopointer$utils$PageType
            int r4 = r4.ordinal()
            r4 = r1[r4]
            r1 = 1
            if (r4 == r1) goto L16
            goto L23
        L16:
            int r4 = android.os.Build.VERSION.SDK_INT
            r1 = 23
            if (r4 < r1) goto L25
            java.lang.String r4 = "com.vivo.permissionmanager"
            java.lang.String r1 = "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
            r0.setClassName(r4, r1)
        L23:
            r4 = 0
            goto L2e
        L25:
            android.content.ComponentName r4 = new android.content.ComponentName
            java.lang.String r1 = "com.iqoo.secure"
            java.lang.String r2 = "com.vivo.permissionmanager/.activity.BgStartUpManagerActivity"
            r4.<init>(r1, r2)
        L2e:
            if (r4 == 0) goto L39
            r0.setComponent(r4)     // Catch: java.lang.Exception -> L37
            r3.startActivity(r0)     // Catch: java.lang.Exception -> L37
            goto L42
        L37:
            r3 = move-exception
            goto L3f
        L39:
            java.lang.String r3 = "vivo手机：componentName=null"
            npPermission.nopointer.log.NpPerLog.log(r3)     // Catch: java.lang.Exception -> L37
            goto L42
        L3f:
            r3.printStackTrace()
        L42:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: npPermission.nopointer.utils.PhoneRomSysPage.goToVIVOSettingsPage(android.content.Context, npPermission.nopointer.utils.PageType):void");
    }

    public static void goToXiaoMiSettingsPage(Context context, PageType pageType) {
        Intent intent = new Intent();
        intent.setFlags(AMapEngineUtils.MAX_P20_WIDTH);
        ComponentName componentName = AnonymousClass1.$SwitchMap$npPermission$nopointer$utils$PageType[pageType.ordinal()] != 1 ? null : new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity");
        try {
            if (componentName != null) {
                intent.setComponent(componentName);
                context.startActivity(intent);
            } else {
                NpPerLog.log("小米手机：componentName=null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
