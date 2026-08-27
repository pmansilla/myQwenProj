package org.apache.commons.lang;

import java.io.Serializable;

/* loaded from: classes2.dex */
public class ObjectUtils {
    public static final Null NULL = new Null();

    /* loaded from: classes2.dex */
    public static class Null implements Serializable {
        private static final long serialVersionUID = 7092611880189329093L;

        Null() {
        }

        private Object readResolve() {
            return ObjectUtils.NULL;
        }
    }

    public static StringBuffer appendIdentityToString(StringBuffer stringBuffer, Object obj) {
        if (obj == null) {
            return null;
        }
        if (stringBuffer == null) {
            stringBuffer = new StringBuffer();
        }
        stringBuffer.append(obj.getClass().getName());
        stringBuffer.append('@');
        stringBuffer.append(Integer.toHexString(System.identityHashCode(obj)));
        return stringBuffer;
    }

    public static Object defaultIfNull(Object obj, Object obj2) {
        return obj != null ? obj : obj2;
    }

    public static boolean equals(Object obj, Object obj2) {
        if (obj == obj2) {
            return true;
        }
        if (obj == null || obj2 == null) {
            return false;
        }
        return obj.equals(obj2);
    }

    public static int hashCode(Object obj) {
        if (obj == null) {
            return 0;
        }
        return obj.hashCode();
    }

    public static String identityToString(Object obj) {
        if (obj == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        identityToString(stringBuffer, obj);
        return stringBuffer.toString();
    }

    public static void identityToString(StringBuffer stringBuffer, Object obj) {
        if (obj == null) {
            throw new NullPointerException("Cannot get the toString of a null identity");
        }
        stringBuffer.append(obj.getClass().getName());
        stringBuffer.append('@');
        stringBuffer.append(Integer.toHexString(System.identityHashCode(obj)));
    }

    public static Object max(Comparable comparable, Comparable comparable2) {
        return (comparable == null || comparable2 == null) ? comparable != null ? comparable : comparable2 : comparable.compareTo(comparable2) >= 0 ? comparable : comparable2;
    }

    public static Object min(Comparable comparable, Comparable comparable2) {
        return (comparable == null || comparable2 == null) ? comparable != null ? comparable : comparable2 : comparable.compareTo(comparable2) < 1 ? comparable : comparable2;
    }

    public static String toString(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    public static String toString(Object obj, String str) {
        return obj == null ? str : obj.toString();
    }
}
