package com.czw.utils;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import com.autonavi.amap.mapcore.AMapEngineUtils;

/* loaded from: classes.dex */
public class PhoneFunctionUtil {
    public static boolean isLocationModeOpen(Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        LocationManager locationManager = (LocationManager) context.getSystemService("location");
        return locationManager.isProviderEnabled("gps") || locationManager.isProviderEnabled("network");
    }

    public static boolean isLocationModeOpenWith23(Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        return isLocationModeOpen(context);
    }

    public static void jump2LocationSetting(Context context) {
        Intent intent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
        intent.setFlags(AMapEngineUtils.MAX_P20_WIDTH);
        context.startActivity(intent);
    }
}
