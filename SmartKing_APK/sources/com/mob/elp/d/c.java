package com.mob.elp.d;

import android.os.Build;
import android.text.TextUtils;
import com.mob.MobSDK;

/* compiled from: ELPDeviceHelper.java */
/* loaded from: classes.dex */
public class c {
    private static c a;
    private static String b;

    private c() {
    }

    public static synchronized c a() {
        c cVar;
        synchronized (c.class) {
            if (a == null) {
                a = new c();
                MobSDK.getContext();
                b = Build.BRAND;
                b();
            }
            cVar = a;
        }
        return cVar;
    }

    private static String a(String str) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            String str2 = (String) cls.getDeclaredMethod("get", String.class).invoke(cls, str);
            if (str.equalsIgnoreCase("ro.build.version.emui")) {
                str2 = str2.substring(str2.lastIndexOf("_") + 1, str2.length());
            }
            if (str.equalsIgnoreCase("ro.miui.ui.version.name") && str2.startsWith("V")) {
                str2 = str2.substring(1);
            }
            if (str.equalsIgnoreCase("ro.build.display.id")) {
                str2 = str2.substring(6, str2.lastIndexOf(".") + 2);
            }
            if (str.equalsIgnoreCase("ro.build.version.opporom") && str2.startsWith("V")) {
                str2 = str2.substring(1);
            }
            d.a().a("System UI Version:" + str2);
            return str2;
        } catch (Throwable th) {
            d.a().a(th);
            return null;
        }
    }

    public static String b() {
        if ("huawei".equalsIgnoreCase(b)) {
            String a2 = a("ro.build.version.emui");
            if (!TextUtils.isEmpty(a2)) {
                b = "huawei";
                return a2;
            }
        } else if ("xiaomi".equalsIgnoreCase(b)) {
            String a3 = a("ro.miui.ui.version.name");
            if (!TextUtils.isEmpty(a3)) {
                b = "xiaomi";
                return a3;
            }
        } else if ("meizu".equalsIgnoreCase(b)) {
            if (!TextUtils.isEmpty(a("ro.meizu.product.model"))) {
                b = "meizu";
                return a("ro.build.display.id");
            }
        } else if ("oppo".equalsIgnoreCase(b)) {
            String a4 = a("ro.build.version.opporom");
            if (!TextUtils.isEmpty(a4)) {
                b = "oppo";
                return a4;
            }
        } else if ("vivo".equalsIgnoreCase(b)) {
            String a5 = a("ro.vivo.os.version");
            if (!TextUtils.isEmpty(a5)) {
                b = "vivo";
                return a5;
            }
        }
        String a6 = a("ro.build.version.emui");
        if (!TextUtils.isEmpty(a6)) {
            b = "huawei";
            return a6;
        }
        String a7 = a("ro.miui.ui.version.name");
        if (!TextUtils.isEmpty(a7)) {
            b = "xiaomi";
            return a7;
        }
        if (!TextUtils.isEmpty(a("ro.meizu.product.model"))) {
            b = "meizu";
            return a("ro.build.display.id");
        }
        String a8 = a("ro.build.version.opporom");
        if (!TextUtils.isEmpty(a8)) {
            b = "oppo";
            return a8;
        }
        String a9 = a("ro.vivo.os.version");
        if (!TextUtils.isEmpty(a9)) {
            b = "vivo";
        }
        return a9;
    }

    public String c() {
        return (b.equals("huawei") || b.equals("xiaomi") || b.equals("meizu") || b.equals("oppo") || b.equals("vivo")) ? b : "";
    }
}
