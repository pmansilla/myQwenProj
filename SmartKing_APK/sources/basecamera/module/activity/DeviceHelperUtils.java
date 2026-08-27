package basecamera.module.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.autonavi.amap.mapcore.AMapEngineUtils;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint({"PrivateApi"})
/* loaded from: classes.dex */
public class DeviceHelperUtils {
    private static final String ESSENTIAL = "essential";
    private static final String FLYME = "flyme";
    private static final String KEY_FLYME_VERSION_NAME = "ro.build.display.id";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String TAG = "DeviceHelperUtils";
    private static final String ZTEC2016 = "zte c2016";
    private static final String ZUKZ1 = "zuk z1";
    private static String sFlymeVersionName;
    private static String sMiuiVersionName;
    private static final String[] MEIZUBOARD = {"m9", "M9", "mx", "MX"};
    private static boolean sIsTabletChecked = false;
    private static boolean sIsTabletValue = false;
    private static final String BRAND = Build.BRAND.toLowerCase();

    static {
        Properties properties = new Properties();
        if (Build.VERSION.SDK_INT < 26) {
            try {
                properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            Method declaredMethod = Class.forName("android.os.SystemProperties").getDeclaredMethod("get", String.class);
            sMiuiVersionName = getLowerCaseName(properties, declaredMethod, KEY_MIUI_VERSION_NAME);
            sFlymeVersionName = getLowerCaseName(properties, declaredMethod, KEY_FLYME_VERSION_NAME);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private static boolean _isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & 15) >= 3;
    }

    @TargetApi(19)
    private static boolean checkOp(Context context, int i) {
        if (Build.VERSION.SDK_INT >= 19) {
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService("appops");
            try {
                return ((Integer) appOpsManager.getClass().getDeclaredMethod("checkOp", Integer.TYPE, Integer.TYPE, String.class).invoke(appOpsManager, Integer.valueOf(i), Integer.valueOf(Binder.getCallingUid()), context.getPackageName())).intValue() == 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Nullable
    private static String getLowerCaseName(Properties properties, Method method, String str) {
        String property = properties.getProperty(str);
        if (property == null) {
            try {
                property = (String) method.invoke(null, str);
            } catch (Exception unused) {
            }
        }
        return property != null ? property.toLowerCase() : property;
    }

    public static boolean isEssentialPhone() {
        return BRAND.contains(ESSENTIAL);
    }

    public static boolean isFloatWindowOpAllowed(Context context) {
        if (Build.VERSION.SDK_INT >= 19) {
            return checkOp(context, 24);
        }
        try {
            return (context.getApplicationInfo().flags & AMapEngineUtils.HALF_MAX_P20_WIDTH) == 134217728;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isFlyme() {
        return !TextUtils.isEmpty(sFlymeVersionName) && sFlymeVersionName.contains(FLYME);
    }

    public static boolean isFlymeVersionHigher5_2_4() {
        boolean z;
        String group;
        if (sFlymeVersionName != null && !sFlymeVersionName.equals("")) {
            Matcher matcher = Pattern.compile("(\\d+\\.){2}\\d").matcher(sFlymeVersionName);
            if (matcher.find() && (group = matcher.group()) != null && !group.equals("")) {
                String[] split = group.split("\\.");
                if (split.length == 3) {
                    if (Integer.valueOf(split[0]).intValue() >= 5) {
                        if (Integer.valueOf(split[0]).intValue() <= 5) {
                            if (Integer.valueOf(split[1]).intValue() >= 2) {
                                if (Integer.valueOf(split[1]).intValue() <= 2) {
                                    if (Integer.valueOf(split[2]).intValue() >= 4) {
                                        Integer.valueOf(split[2]).intValue();
                                    }
                                }
                            }
                        }
                    }
                    z = false;
                    return isMeizu() && z;
                }
            }
        }
        z = true;
        if (isMeizu()) {
            return false;
        }
    }

    public static boolean isHuawei() {
        return BRAND.contains("huawei") || BRAND.contains("honor");
    }

    public static boolean isMIUI() {
        return !TextUtils.isEmpty(sMiuiVersionName);
    }

    public static boolean isMIUIV5() {
        return "v5".equals(sMiuiVersionName);
    }

    public static boolean isMIUIV6() {
        return "v6".equals(sMiuiVersionName);
    }

    public static boolean isMIUIV7() {
        return "v7".equals(sMiuiVersionName);
    }

    public static boolean isMIUIV8() {
        return "v8".equals(sMiuiVersionName);
    }

    public static boolean isMIUIV9() {
        return "v9".equals(sMiuiVersionName);
    }

    public static boolean isMeizu() {
        return isPhone(MEIZUBOARD) || isFlyme();
    }

    public static boolean isOppo() {
        return BRAND.contains("oppo");
    }

    private static boolean isPhone(String[] strArr) {
        String str = Build.BOARD;
        if (str == null) {
            return false;
        }
        for (String str2 : strArr) {
            if (str.equals(str2)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTablet(Context context) {
        if (sIsTabletChecked) {
            return sIsTabletValue;
        }
        sIsTabletValue = _isTablet(context);
        sIsTabletChecked = true;
        return sIsTabletValue;
    }

    public static boolean isVivo() {
        return BRAND.contains("vivo") || BRAND.contains("bbk");
    }

    public static boolean isXiaomi() {
        return Build.MANUFACTURER.toLowerCase().equals("xiaomi");
    }

    public static boolean isZTKC2016() {
        String str = Build.MODEL;
        return str != null && str.toLowerCase().contains(ZTEC2016);
    }

    public static boolean isZUKZ1() {
        String str = Build.MODEL;
        return str != null && str.toLowerCase().contains(ZUKZ1);
    }
}
