package com.litesuits.orm.db.utils;

import com.litesuits.orm.db.annotation.MapCollection;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Date;

/* loaded from: classes.dex */
public class ClassUtil {
    public static Object getDefaultPrimiticeValue(Class cls) {
        if (cls.isPrimitive()) {
            return cls == Boolean.TYPE ? false : 0;
        }
        return null;
    }

    public static boolean isArray(Class cls) {
        return cls.isArray();
    }

    public static boolean isBaseDataType(Class<?> cls) {
        return cls.isPrimitive() || cls.equals(String.class) || cls.equals(Boolean.class) || cls.equals(Integer.class) || cls.equals(Long.class) || cls.equals(Float.class) || cls.equals(Double.class) || cls.equals(Byte.class) || cls.equals(Character.class) || cls.equals(Short.class) || cls.equals(Date.class) || cls.equals(byte[].class) || cls.equals(Byte[].class);
    }

    public static boolean isCollection(Class cls) {
        return Collection.class.isAssignableFrom(cls);
    }

    public static Object newArray(Class<?> cls, int i) {
        return Array.newInstance(cls, i);
    }

    public static Object newCollection(Class<?> cls) throws IllegalAccessException, InstantiationException {
        return cls.newInstance();
    }

    public static Object newCollectionForField(Field field) throws IllegalAccessException, InstantiationException {
        MapCollection mapCollection = (MapCollection) field.getAnnotation(MapCollection.class);
        return mapCollection == null ? field.getType().newInstance() : mapCollection.value().newInstance();
    }

    public static <T> T newInstance(Class<T> cls) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?>[] declaredConstructors = cls.getDeclaredConstructors();
        if (declaredConstructors.length <= 0) {
            return null;
        }
        Constructor<?> constructor = declaredConstructors[0];
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        if (parameterTypes.length == 0) {
            constructor.setAccessible(true);
            return (T) constructor.newInstance(new Object[0]);
        }
        Object[] objArr = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            objArr[i] = getDefaultPrimiticeValue(parameterTypes[i]);
        }
        constructor.setAccessible(true);
        return (T) constructor.newInstance(objArr);
    }
}
