package com.czw.smartkit.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/* loaded from: classes.dex */
public class AppVersion {
    private int versionCode;
    private String versionName;

    private AppVersion() {
    }

    public static AppVersion getAppVersion(Context context) {
        AppVersion appVersion = new AppVersion();
        try {
            PackageInfo packInfp = getPackInfp(context);
            appVersion.versionCode = packInfp.versionCode;
            appVersion.versionName = packInfp.versionName;
            return appVersion;
        } catch (Exception e) {
            e.printStackTrace();
            appVersion.versionName = "error";
            return appVersion;
        }
    }

    public static PackageInfo getPackInfp(Context context) throws PackageManager.NameNotFoundException {
        return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
    }

    public int getVersionCode() {
        return this.versionCode;
    }

    public String getVersionName() {
        return this.versionName;
    }
}
