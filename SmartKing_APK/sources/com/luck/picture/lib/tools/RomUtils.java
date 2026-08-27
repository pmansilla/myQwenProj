package com.luck.picture.lib.tools;

import android.os.Build;
import android.text.TextUtils;
import com.litesuits.orm.db.assit.SQLBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/* loaded from: classes.dex */
public class RomUtils {

    /* loaded from: classes.dex */
    class AvailableRomType {
        public static final int ANDROID_NATIVE = 3;
        public static final int FLYME = 2;
        public static final int MIUI = 1;
        public static final int NA = 4;

        AvailableRomType() {
        }
    }

    public static int getLightStatausBarAvailableRomType() {
        if (isMIUIV6OrAbove()) {
            return 1;
        }
        if (isFlymeV4OrAbove()) {
            return 2;
        }
        return isAndroidMOrAbove() ? 3 : 4;
    }

    private static String getSystemProperty(String str) {
        BufferedReader bufferedReader;
        BufferedReader bufferedReader2 = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("getprop " + str).getInputStream()), 1024);
            try {
                String readLine = bufferedReader.readLine();
                bufferedReader.close();
                try {
                    bufferedReader.close();
                } catch (IOException unused) {
                }
                return readLine;
            } catch (IOException unused2) {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException unused3) {
                    }
                }
                return null;
            } catch (Throwable th) {
                th = th;
                bufferedReader2 = bufferedReader;
                if (bufferedReader2 != null) {
                    try {
                        bufferedReader2.close();
                    } catch (IOException unused4) {
                    }
                }
                throw th;
            }
        } catch (IOException unused5) {
            bufferedReader = null;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private static boolean isAndroidMOrAbove() {
        return Build.VERSION.SDK_INT >= 23;
    }

    private static boolean isFlymeV4OrAbove() {
        String str = Build.DISPLAY;
        if (!TextUtils.isEmpty(str) && str.contains("Flyme")) {
            for (String str2 : str.split(SQLBuilder.BLANK)) {
                if (str2.matches("^[4-9]\\.(\\d+\\.)+\\S*")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isLightStatusBarAvailable() {
        return isMIUIV6OrAbove() || isFlymeV4OrAbove() || isAndroidMOrAbove();
    }

    private static boolean isMIUIV6OrAbove() {
        String systemProperty = getSystemProperty("ro.miui.ui.version.code");
        if (TextUtils.isEmpty(systemProperty)) {
            return false;
        }
        try {
            return Integer.parseInt(systemProperty) >= 4;
        } catch (Exception unused) {
            return false;
        }
    }
}
