package com.qmuiteam.qmui.util;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import java.util.Locale;

/* loaded from: classes2.dex */
public class QMUIDisplayHelper {
    private static final String TAG = "QMUIDisplayHelper";
    public static final float DENSITY = Resources.getSystem().getDisplayMetrics().density;
    private static Boolean sHasCamera = null;

    public static void cancelFullScreen(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            WindowManager.LayoutParams attributes = activity.getWindow().getAttributes();
            attributes.flags &= -1025;
            activity.getWindow().setAttributes(attributes);
            activity.getWindow().addFlags(512);
        }
    }

    public static int dp2px(Context context, int i) {
        double density = getDensity(context) * i;
        Double.isNaN(density);
        return (int) (density + 0.5d);
    }

    public static int dpToPx(int i) {
        return (int) ((i * DENSITY) + 0.5f);
    }

    public static int getActionBarHeight(Context context) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true)) {
            return TypedValue.complexToDimensionPixelSize(typedValue.data, context.getResources().getDisplayMetrics());
        }
        return 0;
    }

    public static String getCurCountryLan(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        Locale locale = Build.VERSION.SDK_INT >= 24 ? configuration.getLocales().get(0) : configuration.locale;
        return locale.getLanguage() + "-" + locale.getCountry();
    }

    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getApplicationContext().getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static float getFontDensity(Context context) {
        return context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static int getNavMenuHeight(Context context) {
        if (!isNavMenuExist(context)) {
            return 0;
        }
        int identifier = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        return identifier > 0 ? context.getResources().getDimensionPixelSize(identifier) : getRealScreenSize(context)[1] - getScreenHeight(context);
    }

    public static int[] getRealScreenSize(Context context) {
        int i;
        int i2;
        int[] iArr = new int[2];
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        int i3 = displayMetrics.widthPixels;
        int i4 = displayMetrics.heightPixels;
        try {
            i = ((Integer) Display.class.getMethod("getRawWidth", new Class[0]).invoke(defaultDisplay, new Object[0])).intValue();
            try {
                i4 = ((Integer) Display.class.getMethod("getRawHeight", new Class[0]).invoke(defaultDisplay, new Object[0])).intValue();
            } catch (Exception unused) {
            }
        } catch (Exception unused2) {
            i = i3;
        }
        try {
            Point point = new Point();
            defaultDisplay.getRealSize(point);
            Display.class.getMethod("getRealSize", Point.class).invoke(defaultDisplay, point);
            i2 = point.x;
            try {
                i4 = point.y;
            } catch (Exception unused3) {
            }
        } catch (Exception unused4) {
            i2 = i;
        }
        iArr[0] = i2;
        iArr[1] = i4;
        return iArr;
    }

    public static int getScreenHeight(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    public static int getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> cls = Class.forName("com.android.internal.R$dimen");
            return context.getResources().getDimensionPixelSize(Integer.parseInt(cls.getField("status_bar_height").get(cls.newInstance()).toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static final boolean hasCamera(Context context) {
        if (sHasCamera == null) {
            PackageManager packageManager = context.getPackageManager();
            sHasCamera = Boolean.valueOf(packageManager.hasSystemFeature("android.hardware.camera.front") || packageManager.hasSystemFeature("android.hardware.camera"));
        }
        return sHasCamera.booleanValue();
    }

    public static boolean hasHardwareMenuKey(Context context) {
        if (Build.VERSION.SDK_INT < 11) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= 14) {
            return ViewConfiguration.get(context).hasPermanentMenuKey();
        }
        return false;
    }

    public static boolean hasInternet(Context context) {
        return ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo() != null;
    }

    public static boolean hasStatusBar(Context context) {
        return ((context instanceof Activity) && (((Activity) context).getWindow().getAttributes().flags & 1024) == 1024) ? false : true;
    }

    public static boolean isElevationSupported() {
        return Build.VERSION.SDK_INT >= 21;
    }

    public static boolean isFullScreen(Activity activity) {
        return (activity.getWindow().getAttributes().flags & 1024) == 1024;
    }

    public static boolean isNavMenuExist(Context context) {
        return (ViewConfiguration.get(context).hasPermanentMenuKey() || KeyCharacterMap.deviceHasKey(4)) ? false : true;
    }

    public static boolean isPackageExist(Context context, String str) {
        return context.getPackageManager().getPackageInfo(str, 0) != null;
    }

    public static boolean isSdcardReady() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public static boolean isZhCN(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        return (Build.VERSION.SDK_INT >= 24 ? configuration.getLocales().get(0) : configuration.locale).getCountry().equalsIgnoreCase("CN");
    }

    public static int px2dp(Context context, int i) {
        double density = i / getDensity(context);
        Double.isNaN(density);
        return (int) (density + 0.5d);
    }

    public static int px2sp(Context context, int i) {
        double fontDensity = i / getFontDensity(context);
        Double.isNaN(fontDensity);
        return (int) (fontDensity + 0.5d);
    }

    public static int pxToDp(float f) {
        return (int) ((f / DENSITY) + 0.5f);
    }

    public static void setFullScreen(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            WindowManager.LayoutParams attributes = activity.getWindow().getAttributes();
            attributes.flags |= 1024;
            activity.getWindow().setAttributes(attributes);
            activity.getWindow().addFlags(512);
        }
    }

    public static int sp2px(Context context, int i) {
        double fontDensity = getFontDensity(context) * i;
        Double.isNaN(fontDensity);
        return (int) (fontDensity + 0.5d);
    }
}
