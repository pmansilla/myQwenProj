package no.nordicsemi.android.support.v18.scanner;

import java.util.Arrays;

/* loaded from: classes2.dex */
class Objects {
    Objects() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean deepEquals(Object obj, Object obj2) {
        return (obj == null || obj2 == null) ? obj == obj2 : ((obj instanceof Object[]) && (obj2 instanceof Object[])) ? Arrays.deepEquals((Object[]) obj, (Object[]) obj2) : ((obj instanceof boolean[]) && (obj2 instanceof boolean[])) ? Arrays.equals((boolean[]) obj, (boolean[]) obj2) : ((obj instanceof byte[]) && (obj2 instanceof byte[])) ? Arrays.equals((byte[]) obj, (byte[]) obj2) : ((obj instanceof char[]) && (obj2 instanceof char[])) ? Arrays.equals((char[]) obj, (char[]) obj2) : ((obj instanceof double[]) && (obj2 instanceof double[])) ? Arrays.equals((double[]) obj, (double[]) obj2) : ((obj instanceof float[]) && (obj2 instanceof float[])) ? Arrays.equals((float[]) obj, (float[]) obj2) : ((obj instanceof int[]) && (obj2 instanceof int[])) ? Arrays.equals((int[]) obj, (int[]) obj2) : ((obj instanceof long[]) && (obj2 instanceof long[])) ? Arrays.equals((long[]) obj, (long[]) obj2) : ((obj instanceof short[]) && (obj2 instanceof short[])) ? Arrays.equals((short[]) obj, (short[]) obj2) : obj.equals(obj2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean equals(Object obj, Object obj2) {
        return obj == null ? obj2 == null : obj.equals(obj2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int hash(Object... objArr) {
        return Arrays.hashCode(objArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String toString(Object obj) {
        return obj == null ? "null" : obj.toString();
    }
}
