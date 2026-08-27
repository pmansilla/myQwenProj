package com.mob.commons.b;

import android.content.Context;
import android.os.Build;
import android.support.v4.os.EnvironmentCompat;
import android.text.TextUtils;
import android.util.Base64;
import com.mob.MobSDK;
import com.mob.tools.MobLog;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.DeviceHelper;
import java.lang.reflect.Method;
import java.util.HashMap;

/* compiled from: FidsSDK.java */
/* loaded from: classes.dex */
public class d {
    private static f a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FidsSDK.java */
    /* loaded from: classes.dex */
    public enum a {
        UNSUPPORT(-1, com.mob.commons.k.a(103)),
        HUA_WEI(0, com.mob.commons.k.a(104)),
        XIAOMI(1, com.mob.commons.k.a(105)),
        VIVO(2, com.mob.commons.k.a(106)),
        OPPO(3, com.mob.commons.k.a(107)),
        MOTO(4, com.mob.commons.k.a(108)),
        LENOVO(5, com.mob.commons.k.a(109)),
        ASUS(6, com.mob.commons.k.a(110)),
        SAMSUNG(7, com.mob.commons.k.a(111)),
        MEIZU(8, com.mob.commons.k.a(112)),
        ALPS(9, com.mob.commons.k.a(113)),
        NUBIA(10, com.mob.commons.k.a(114)),
        ONEPLUS(11, com.mob.commons.k.a(133)),
        BLACKSHARK(12, com.mob.commons.k.a(134)),
        ZTE(13, com.mob.commons.k.a(135)),
        FERRMEOS(14, com.mob.commons.k.a(136)),
        SSUI(15, com.mob.commons.k.a(137));

        private int r;
        private String s;

        a(int i, String str) {
            this.r = i;
            this.s = str;
        }
    }

    public static a a(String str) {
        if (!TextUtils.isEmpty(str)) {
            for (a aVar : a.values()) {
                if (aVar.s.equalsIgnoreCase(str)) {
                    return aVar;
                }
            }
        }
        return (a() || b()) ? a.ZTE : a.UNSUPPORT;
    }

    public static synchronized HashMap<String, Object> a(Context context) {
        synchronized (d.class) {
            HashMap<String, Object> hashMap = new HashMap<>();
            b a2 = b.a(context);
            HashMap<String, Object> a3 = a2.a();
            boolean z = a3 != null && a3.size() > 0;
            if (z) {
                HashMap hashMap2 = new HashMap();
                if (a3.containsKey(com.mob.commons.k.a(71))) {
                    a3.put(com.mob.commons.k.a(72), a3.remove(com.mob.commons.k.a(71)));
                }
                if (a3.containsKey(com.mob.commons.k.a(74))) {
                    a3.put(com.mob.commons.k.a(73), a3.remove(com.mob.commons.k.a(74)));
                }
                hashMap2.putAll(a3);
                hashMap.put("fidsCache", hashMap2);
            }
            String c = c(context);
            String e = e(context);
            String g = g(context);
            String f = f(context);
            if (!z && TextUtils.isEmpty(c) && TextUtils.isEmpty(f)) {
                return null;
            }
            boolean b = b(context);
            hashMap.put(com.mob.commons.k.a(75), e);
            hashMap.put(com.mob.commons.k.a(69), c);
            hashMap.put(com.mob.commons.k.a(70), g);
            hashMap.put(com.mob.commons.k.a(72), f);
            hashMap.put(com.mob.commons.k.a(73), Boolean.valueOf(b));
            a2.a(c, e, g, f, b);
            return hashMap;
        }
    }

    private static boolean a() {
        try {
            String b = b("ro.build.freeme.label");
            if (TextUtils.isEmpty(b)) {
                return false;
            }
            return b.equalsIgnoreCase("FREEMEOS");
        } catch (Throwable unused) {
            return false;
        }
    }

    public static String b(String str) {
        try {
            Method declaredMethod = Class.forName("android.os.SystemProperties").getDeclaredMethod("get", String.class);
            declaredMethod.setAccessible(true);
            return String.valueOf(declaredMethod.invoke(null, str));
        } catch (Throwable unused) {
            return "";
        }
    }

    private static boolean b() {
        try {
            String b = b("ro.ssui.product");
            if (TextUtils.isEmpty(b)) {
                return false;
            }
            return !b.equalsIgnoreCase(EnvironmentCompat.MEDIA_UNKNOWN);
        } catch (Throwable unused) {
            return false;
        }
    }

    public static boolean b(Context context) {
        h(context);
        if (a != null) {
            return a.h();
        }
        return false;
    }

    public static String c(Context context) {
        h(context);
        if (a != null) {
            return a.e();
        }
        return null;
    }

    public static String d(Context context) {
        h(context);
        if (a == null) {
            return null;
        }
        String e = a.e();
        if (TextUtils.isEmpty(e)) {
            return null;
        }
        try {
            return Base64.encodeToString(Data.AES128Encode(Data.MD5(DeviceHelper.getInstance(MobSDK.getContext()).getManufacturer()), e), 2);
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return e;
        }
    }

    public static String e(Context context) {
        h(context);
        if (a != null) {
            return a.b();
        }
        return null;
    }

    public static String f(Context context) {
        h(context);
        if (a != null) {
            return a.f();
        }
        return null;
    }

    public static String g(Context context) {
        h(context);
        if (a != null) {
            return a.g();
        }
        return null;
    }

    private static synchronized void h(Context context) {
        synchronized (d.class) {
            if (a != null) {
                return;
            }
            a a2 = a(Build.MANUFACTURER);
            if (a2 == a.UNSUPPORT) {
                c.a().a(Build.MANUFACTURER + " not support");
                return;
            }
            switch (a2) {
                case XIAOMI:
                case BLACKSHARK:
                    a = new m(context);
                    break;
                case VIVO:
                    a = new l(context);
                    break;
                case HUA_WEI:
                    a = new e(context);
                    break;
                case OPPO:
                case ONEPLUS:
                    a = new j(context);
                    break;
                case MOTO:
                case LENOVO:
                    a = new h(context);
                    break;
                case ASUS:
                    a = new com.mob.commons.b.a(context);
                    break;
                case SAMSUNG:
                    a = new k(context);
                    break;
                case MEIZU:
                case ALPS:
                    a = new g(context);
                    break;
                case NUBIA:
                    a = new i(context);
                    break;
                case ZTE:
                case FERRMEOS:
                case SSUI:
                    a = new n(context);
                    break;
            }
        }
    }
}
