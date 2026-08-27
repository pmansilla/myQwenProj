package npPermission.nopointer.utils;

import android.content.Context;
import android.content.Intent;

/* loaded from: classes2.dex */
public class SettingsPageUtils {
    private SettingsPageUtils() {
    }

    public static void goToSavePowerPage(Context context) {
        context.startActivity(new Intent("android.settings.BATTERY_SAVER_SETTINGS"));
    }

    public static void goToSettingPage(Context context, PageType pageType) {
        PhoneRomSysPage.goToSettingPage(context, pageType);
    }

    public static void toToSettingsPage(Context context) {
        context.startActivity(new Intent("android.settings.SETTINGS"));
    }
}
