package cn.smssdk.utils;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import com.mob.MobSDK;
import com.mob.tools.utils.ResHelper;

/* compiled from: SmsResHelper.java */
/* loaded from: classes.dex */
public class e extends ResHelper {
    private static String a = "SmsResHelper";
    private static Resources b;

    public static int a(int i) {
        if (b == null) {
            b = MobSDK.getContext().getResources();
        }
        return b.getColor(i);
    }

    public static int a(int i, int i2) {
        if (i > 0) {
            try {
                return a(i);
            } catch (Resources.NotFoundException unused) {
                SMSLog.getInstance().w(SMSLog.FORMAT, a, "getColorSafe", "Color resource not found. id: " + i);
            }
        }
        return a(i2);
    }

    public static int a(String str) {
        return ResHelper.getResId(MobSDK.getContext(), "dimen", str);
    }

    public static int b(int i) {
        return ResHelper.pxToDip(MobSDK.getContext(), c(i));
    }

    public static String b(int i, int i2) {
        if (i > 0) {
            try {
                return e(i);
            } catch (Throwable unused) {
                SMSLog.getInstance().w(SMSLog.FORMAT, a, "getStringSafe", "String resource not found. id: " + i);
            }
        }
        return e(i2);
    }

    public static int c(int i) {
        if (b == null) {
            b = MobSDK.getContext().getResources();
        }
        return b.getDimensionPixelSize(i);
    }

    public static int d(int i) {
        try {
            return MobSDK.getContext().getPackageManager().getApplicationInfo(MobSDK.getContext().getPackageName(), 0).icon;
        } catch (PackageManager.NameNotFoundException unused) {
            SMSLog.getInstance().w(SMSLog.FORMAT, a, "getIconIdSafe", "No icon found");
            try {
                return ResHelper.getBitmapRes(MobSDK.getContext(), "ic_launcher");
            } catch (Throwable unused2) {
                SMSLog.getInstance().w(SMSLog.FORMAT, a, "getIconIdSafe", "No icon named 'ic_launcher' found");
                return i;
            }
        }
    }

    public static String e(int i) {
        if (b == null) {
            b = MobSDK.getContext().getResources();
        }
        return b.getString(i);
    }
}
