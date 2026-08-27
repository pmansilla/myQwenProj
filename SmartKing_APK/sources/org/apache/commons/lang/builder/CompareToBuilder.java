package org.apache.commons.lang.builder;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.lang.math.NumberUtils;

/* loaded from: classes2.dex */
public class CompareToBuilder {
    private int comparison = 0;

    private static void reflectionAppend(Object obj, Object obj2, Class cls, CompareToBuilder compareToBuilder, boolean z, String[] strArr) {
        Field[] declaredFields = cls.getDeclaredFields();
        List asList = strArr != null ? Arrays.asList(strArr) : Collections.EMPTY_LIST;
        AccessibleObject.setAccessible(declaredFields, true);
        for (int i = 0; i < declaredFields.length && compareToBuilder.comparison == 0; i++) {
            Field field = declaredFields[i];
            if (!asList.contains(field.getName()) && field.getName().indexOf(36) == -1 && ((z || !Modifier.isTransient(field.getModifiers())) && !Modifier.isStatic(field.getModifiers()))) {
                try {
                    compareToBuilder.append(field.get(obj), field.get(obj2));
                } catch (IllegalAccessException unused) {
                    throw new InternalError("Unexpected IllegalAccessException");
                }
            }
        }
    }

    public static int reflectionCompare(Object obj, Object obj2) {
        return reflectionCompare(obj, obj2, false, null, null);
    }

    public static int reflectionCompare(Object obj, Object obj2, Collection collection) {
        return reflectionCompare(obj, obj2, ReflectionToStringBuilder.toNoNullStringArray(collection));
    }

    public static int reflectionCompare(Object obj, Object obj2, boolean z) {
        return reflectionCompare(obj, obj2, z, null, null);
    }

    public static int reflectionCompare(Object obj, Object obj2, boolean z, Class cls) {
        return reflectionCompare(obj, obj2, false, cls, null);
    }

    public static int reflectionCompare(Object obj, Object obj2, boolean z, Class cls, String[] strArr) {
        if (obj == obj2) {
            return 0;
        }
        if (obj == null || obj2 == null) {
            throw new NullPointerException();
        }
        Class<?> cls2 = obj.getClass();
        if (!cls2.isInstance(obj2)) {
            throw new ClassCastException();
        }
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        reflectionAppend(obj, obj2, cls2, compareToBuilder, z, strArr);
        while (cls2.getSuperclass() != null && cls2 != cls) {
            cls2 = cls2.getSuperclass();
            reflectionAppend(obj, obj2, cls2, compareToBuilder, z, strArr);
        }
        return compareToBuilder.toComparison();
    }

    public static int reflectionCompare(Object obj, Object obj2, String[] strArr) {
        return reflectionCompare(obj, obj2, false, null, strArr);
    }

    public CompareToBuilder append(byte b, byte b2) {
        if (this.comparison != 0) {
            return this;
        }
        this.comparison = b < b2 ? -1 : b > b2 ? 1 : 0;
        return this;
    }

    public CompareToBuilder append(char c, char c2) {
        if (this.comparison != 0) {
            return this;
        }
        this.comparison = c < c2 ? -1 : c > c2 ? 1 : 0;
        return this;
    }

    public CompareToBuilder append(double d, double d2) {
        if (this.comparison != 0) {
            return this;
        }
        this.comparison = NumberUtils.compare(d, d2);
        return this;
    }

    public CompareToBuilder append(float f, float f2) {
        if (this.comparison != 0) {
            return this;
        }
        this.comparison = NumberUtils.compare(f, f2);
        return this;
    }

    public CompareToBuilder append(int i, int i2) {
        if (this.comparison != 0) {
            return this;
        }
        this.comparison = i < i2 ? -1 : i > i2 ? 1 : 0;
        return this;
    }

    public CompareToBuilder append(long j, long j2) {
        if (this.comparison != 0) {
            return this;
        }
        this.comparison = j < j2 ? -1 : j > j2 ? 1 : 0;
        return this;
    }

    public CompareToBuilder append(Object obj, Object obj2) {
        return append(obj, obj2, (Comparator) null);
    }

    public CompareToBuilder append(Object obj, Object obj2, Comparator comparator) {
        if (this.comparison != 0 || obj == obj2) {
            return this;
        }
        if (obj == null) {
            this.comparison = -1;
            return this;
        }
        if (obj2 == null) {
            this.comparison = 1;
            return this;
        }
        if (obj.getClass().isArray()) {
            if (obj instanceof long[]) {
                append((long[]) obj, (long[]) obj2);
            } else if (obj instanceof int[]) {
                append((int[]) obj, (int[]) obj2);
            } else if (obj instanceof short[]) {
                append((short[]) obj, (short[]) obj2);
            } else if (obj instanceof char[]) {
                append((char[]) obj, (char[]) obj2);
            } else if (obj instanceof byte[]) {
                append((byte[]) obj, (byte[]) obj2);
            } else if (obj instanceof double[]) {
                append((double[]) obj, (double[]) obj2);
            } else if (obj instanceof float[]) {
                append((float[]) obj, (float[]) obj2);
            } else if (obj instanceof boolean[]) {
                append((boolean[]) obj, (boolean[]) obj2);
            } else {
                append((Object[]) obj, (Object[]) obj2, comparator);
            }
        } else if (comparator == null) {
            this.comparison = ((Comparable) obj).compareTo(obj2);
        } else {
            this.comparison = comparator.compare(obj, obj2);
        }
        return this;
    }

    public CompareToBuilder append(short s, short s2) {
        if (this.comparison != 0) {
            return this;
        }
        this.comparison = s < s2 ? -1 : s > s2 ? 1 : 0;
        return this;
    }

    public CompareToBuilder append(boolean z, boolean z2) {
        if (this.comparison != 0 || z == z2) {
            return this;
        }
        if (z) {
            this.comparison = 1;
        } else {
            this.comparison = -1;
        }
        return this;
    }

    public CompareToBuilder append(byte[] bArr, byte[] bArr2) {
        if (this.comparison != 0 || bArr == bArr2) {
            return this;
        }
        if (bArr == null) {
            this.comparison = -1;
            return this;
        }
        if (bArr2 == null) {
            this.comparison = 1;
            return this;
        }
        if (bArr.length != bArr2.length) {
            this.comparison = bArr.length >= bArr2.length ? 1 : -1;
            return this;
        }
        for (int i = 0; i < bArr.length && this.comparison == 0; i++) {
            append(bArr[i], bArr2[i]);
        }
        return this;
    }

    public CompareToBuilder append(char[] cArr, char[] cArr2) {
        if (this.comparison != 0 || cArr == cArr2) {
            return this;
        }
        if (cArr == null) {
            this.comparison = -1;
            return this;
        }
        if (cArr2 == null) {
            this.comparison = 1;
            return this;
        }
        if (cArr.length != cArr2.length) {
            this.comparison = cArr.length >= cArr2.length ? 1 : -1;
            return this;
        }
        for (int i = 0; i < cArr.length && this.comparison == 0; i++) {
            append(cArr[i], cArr2[i]);
        }
        return this;
    }

    public CompareToBuilder append(double[] dArr, double[] dArr2) {
        if (this.comparison != 0 || dArr == dArr2) {
            return this;
        }
        if (dArr == null) {
            this.comparison = -1;
            return this;
        }
        if (dArr2 == null) {
            this.comparison = 1;
            return this;
        }
        if (dArr.length != dArr2.length) {
            this.comparison = dArr.length >= dArr2.length ? 1 : -1;
            return this;
        }
        for (int i = 0; i < dArr.length && this.comparison == 0; i++) {
            append(dArr[i], dArr2[i]);
        }
        return this;
    }

    public CompareToBuilder append(float[] fArr, float[] fArr2) {
        if (this.comparison != 0 || fArr == fArr2) {
            return this;
        }
        if (fArr == null) {
            this.comparison = -1;
            return this;
        }
        if (fArr2 == null) {
            this.comparison = 1;
            return this;
        }
        if (fArr.length != fArr2.length) {
            this.comparison = fArr.length >= fArr2.length ? 1 : -1;
            return this;
        }
        for (int i = 0; i < fArr.length && this.comparison == 0; i++) {
            append(fArr[i], fArr2[i]);
        }
        return this;
    }

    public CompareToBuilder append(int[] iArr, int[] iArr2) {
        if (this.comparison != 0 || iArr == iArr2) {
            return this;
        }
        if (iArr == null) {
            this.comparison = -1;
            return this;
        }
        if (iArr2 == null) {
            this.comparison = 1;
            return this;
        }
        if (iArr.length != iArr2.length) {
            this.comparison = iArr.length >= iArr2.length ? 1 : -1;
            return this;
        }
        for (int i = 0; i < iArr.length && this.comparison == 0; i++) {
            append(iArr[i], iArr2[i]);
        }
        return this;
    }

    public CompareToBuilder append(long[] jArr, long[] jArr2) {
        if (this.comparison != 0 || jArr == jArr2) {
            return this;
        }
        if (jArr == null) {
            this.comparison = -1;
            return this;
        }
        if (jArr2 == null) {
            this.comparison = 1;
            return this;
        }
        if (jArr.length != jArr2.length) {
            this.comparison = jArr.length >= jArr2.length ? 1 : -1;
            return this;
        }
        for (int i = 0; i < jArr.length && this.comparison == 0; i++) {
            append(jArr[i], jArr2[i]);
        }
        return this;
    }

    public CompareToBuilder append(Object[] objArr, Object[] objArr2) {
        return append(objArr, objArr2, (Comparator) null);
    }

    public CompareToBuilder append(Object[] objArr, Object[] objArr2, Comparator comparator) {
        if (this.comparison != 0 || objArr == objArr2) {
            return this;
        }
        if (objArr == null) {
            this.comparison = -1;
            return this;
        }
        if (objArr2 == null) {
            this.comparison = 1;
            return this;
        }
        if (objArr.length != objArr2.length) {
            this.comparison = objArr.length >= objArr2.length ? 1 : -1;
            return this;
        }
        for (int i = 0; i < objArr.length && this.comparison == 0; i++) {
            append(objArr[i], objArr2[i], comparator);
        }
        return this;
    }

    public CompareToBuilder append(short[] sArr, short[] sArr2) {
        if (this.comparison != 0 || sArr == sArr2) {
            return this;
        }
        if (sArr == null) {
            this.comparison = -1;
            return this;
        }
        if (sArr2 == null) {
            this.comparison = 1;
            return this;
        }
        if (sArr.length != sArr2.length) {
            this.comparison = sArr.length >= sArr2.length ? 1 : -1;
            return this;
        }
        for (int i = 0; i < sArr.length && this.comparison == 0; i++) {
            append(sArr[i], sArr2[i]);
        }
        return this;
    }

    public CompareToBuilder append(boolean[] zArr, boolean[] zArr2) {
        if (this.comparison != 0 || zArr == zArr2) {
            return this;
        }
        if (zArr == null) {
            this.comparison = -1;
            return this;
        }
        if (zArr2 == null) {
            this.comparison = 1;
            return this;
        }
        if (zArr.length != zArr2.length) {
            this.comparison = zArr.length >= zArr2.length ? 1 : -1;
            return this;
        }
        for (int i = 0; i < zArr.length && this.comparison == 0; i++) {
            append(zArr[i], zArr2[i]);
        }
        return this;
    }

    public CompareToBuilder appendSuper(int i) {
        if (this.comparison != 0) {
            return this;
        }
        this.comparison = i;
        return this;
    }

    public int toComparison() {
        return this.comparison;
    }
}
