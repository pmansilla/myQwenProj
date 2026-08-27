package cn.smssdk.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.amap.location.common.model.AmapLoc;
import com.mob.MobSDK;
import com.mob.tools.utils.ReflectHelper;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;

/* compiled from: Utils.java */
/* loaded from: classes.dex */
public class f {
    public static String a() {
        if (!c()) {
            return "NOSIM";
        }
        if (!a(MobSDK.getContext())) {
            return "CELLULAR_DISABLED";
        }
        try {
            String str = (String) ReflectHelper.invokeInstanceMethod((TelephonyManager) MobSDK.getContext().getSystemService("phone"), a("67657453696D4F70657261746F72"), new Object[0]);
            if (!"46001".equals(str) && !"46006".equals(str) && !"46009".equals(str)) {
                if (!"46000".equals(str) && !"46002".equals(str) && !"46004".equals(str) && !"46007".equals(str)) {
                    if (!"46003".equals(str) && !"46005".equals(str)) {
                        if (!"46011".equals(str)) {
                            return "UNKNOWN";
                        }
                    }
                    return "CTCC";
                }
                return "CMCC";
            }
            return "CUCC";
        } catch (Throwable unused) {
            SMSLog.getInstance().d(SMSLog.FORMAT, "Utils", "Util", "isMobileDataEnabled", "Check mobile data encountered exception");
            return "";
        }
    }

    private static String a(String str) {
        char[] charArray = str.toCharArray();
        byte[] bArr = new byte[str.length() / 2];
        for (int i = 0; i < bArr.length; i++) {
            int i2 = i * 2;
            bArr[i] = (byte) ((("0123456789ABCDEF".indexOf(charArray[i2]) * 16) + "0123456789ABCDEF".indexOf(charArray[i2 + 1])) & 255);
        }
        return new String(bArr);
    }

    public static String a(Throwable th) {
        StringWriter stringWriter = new StringWriter();
        th.printStackTrace(new PrintWriter((Writer) stringWriter, true));
        stringWriter.getBuffer().toString();
        return stringWriter.getBuffer().toString();
    }

    public static boolean a(Context context) {
        try {
            Method declaredMethod = ConnectivityManager.class.getDeclaredMethod("getMobileDataEnabled", new Class[0]);
            declaredMethod.setAccessible(true);
            return ((Boolean) declaredMethod.invoke((ConnectivityManager) context.getSystemService("connectivity"), new Object[0])).booleanValue();
        } catch (Throwable unused) {
            SMSLog.getInstance().d(SMSLog.FORMAT, "Utils", "Util", "isMobileDataEnabled", "Check mobile data encountered exception");
            return false;
        }
    }

    public static int b() {
        String[] split;
        StringBuffer stringBuffer = new StringBuffer();
        if (!TextUtils.isEmpty("3.8.3") && (split = "3.8.3".split("\\.")) != null && split.length > 0) {
            stringBuffer.append(split[0]);
            for (int i = 1; i < split.length; i++) {
                if (!TextUtils.isEmpty(split[i]) && split[i].length() < 2) {
                    stringBuffer.append(AmapLoc.RESULT_TYPE_GPS);
                    stringBuffer.append(split[i]);
                } else if (!TextUtils.isEmpty(split[i]) && split[i].length() >= 2) {
                    stringBuffer.append(split[i]);
                }
            }
        }
        return Integer.valueOf(stringBuffer.toString().trim()).intValue();
    }

    public static boolean c() {
        return ((TelephonyManager) MobSDK.getContext().getSystemService("phone")).getSimState() == 5;
    }
}
