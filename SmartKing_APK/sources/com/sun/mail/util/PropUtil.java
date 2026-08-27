package com.sun.mail.util;

import com.tencent.bugly.Bugly;
import java.util.Properties;
import javax.mail.Session;

/* loaded from: classes2.dex */
public class PropUtil {
    private PropUtil() {
    }

    private static boolean getBoolean(Object obj, boolean z) {
        return obj == null ? z : obj instanceof String ? z ? !((String) obj).equalsIgnoreCase(Bugly.SDK_IS_DEV) : ((String) obj).equalsIgnoreCase("true") : obj instanceof Boolean ? ((Boolean) obj).booleanValue() : z;
    }

    public static boolean getBooleanProperty(Properties properties, String str, boolean z) {
        return getBoolean(getProp(properties, str), z);
    }

    public static boolean getBooleanSessionProperty(Session session, String str, boolean z) {
        return getBoolean(getProp(session.getProperties(), str), z);
    }

    public static boolean getBooleanSystemProperty(String str, boolean z) {
        try {
            try {
                return getBoolean(getProp(System.getProperties(), str), z);
            } catch (SecurityException unused) {
                return z;
            }
        } catch (SecurityException unused2) {
            String property = System.getProperty(str);
            return property == null ? z : z ? !property.equalsIgnoreCase(Bugly.SDK_IS_DEV) : property.equalsIgnoreCase("true");
        }
    }

    private static int getInt(Object obj, int i) {
        if (obj == null) {
            return i;
        }
        if (obj instanceof String) {
            try {
                return Integer.parseInt((String) obj);
            } catch (NumberFormatException unused) {
            }
        }
        return obj instanceof Integer ? ((Integer) obj).intValue() : i;
    }

    public static int getIntProperty(Properties properties, String str, int i) {
        return getInt(getProp(properties, str), i);
    }

    public static int getIntSessionProperty(Session session, String str, int i) {
        return getInt(getProp(session.getProperties(), str), i);
    }

    private static Object getProp(Properties properties, String str) {
        Object obj = properties.get(str);
        return obj != null ? obj : properties.getProperty(str);
    }
}
