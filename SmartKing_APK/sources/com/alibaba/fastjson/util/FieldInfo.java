package com.alibaba.fastjson.util;

import com.alibaba.fastjson.annotation.JSONField;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import kotlin.text.Typography;

/* loaded from: classes.dex */
public class FieldInfo implements Comparable<FieldInfo> {
    public final String[] alternateNames;
    public final Class<?> declaringClass;
    public final Field field;
    public final boolean fieldAccess;
    private final JSONField fieldAnnotation;
    public final Class<?> fieldClass;
    public final boolean fieldTransient;
    public final Type fieldType;
    public final String format;
    public final boolean getOnly;
    public final boolean isEnum;
    public final boolean jsonDirect;
    public final String label;
    public final Method method;
    private final JSONField methodAnnotation;
    public final String name;
    public final char[] name_chars;
    private int ordinal;
    public final int parserFeatures;
    public final int serialzeFeatures;
    public final boolean unwrapped;

    public FieldInfo(String str, Class<?> cls, Class<?> cls2, Type type, Field field, int i, int i2, int i3) {
        this.ordinal = 0;
        this.name = str;
        this.declaringClass = cls;
        this.fieldClass = cls2;
        this.fieldType = type;
        this.method = null;
        this.field = field;
        this.ordinal = i;
        this.serialzeFeatures = i2;
        this.parserFeatures = i3;
        this.isEnum = cls2.isEnum();
        if (field != null) {
            int modifiers = field.getModifiers();
            this.fieldAccess = (modifiers & 1) != 0 || this.method == null;
            this.fieldTransient = Modifier.isTransient(modifiers);
        } else {
            this.fieldTransient = false;
            this.fieldAccess = false;
        }
        this.name_chars = genFieldNameChars();
        if (field != null) {
            TypeUtils.setAccessible(field);
        }
        this.label = "";
        this.fieldAnnotation = null;
        this.methodAnnotation = null;
        this.getOnly = false;
        this.jsonDirect = false;
        this.unwrapped = false;
        this.format = null;
        this.alternateNames = new String[0];
    }

    public FieldInfo(String str, Method method, Field field, Class<?> cls, Type type, int i, int i2, int i3, JSONField jSONField, JSONField jSONField2, String str2) {
        boolean z;
        Class<?> type2;
        Type genericType;
        boolean isFinal;
        Type type3;
        Type inheritGenericType;
        this.ordinal = 0;
        if (field != null) {
            String name = field.getName();
            if (name.equals(str)) {
                str = name;
            }
        }
        this.name = str;
        this.method = method;
        this.field = field;
        this.ordinal = i;
        this.serialzeFeatures = i2;
        this.parserFeatures = i3;
        this.fieldAnnotation = jSONField;
        this.methodAnnotation = jSONField2;
        if (field != null) {
            int modifiers = field.getModifiers();
            this.fieldAccess = (modifiers & 1) != 0 || method == null;
            this.fieldTransient = Modifier.isTransient(modifiers) || TypeUtils.isTransient(method);
        } else {
            this.fieldAccess = false;
            this.fieldTransient = false;
        }
        if (str2 == null || str2.length() <= 0) {
            this.label = "";
        } else {
            this.label = str2;
        }
        JSONField annotation = getAnnotation();
        if (annotation != null) {
            String format = annotation.format();
            r10 = format.trim().length() != 0 ? format : null;
            z = annotation.jsonDirect();
            this.unwrapped = annotation.unwrapped();
            this.alternateNames = annotation.alternateNames();
        } else {
            this.unwrapped = false;
            this.alternateNames = new String[0];
            z = false;
        }
        this.format = r10;
        this.name_chars = genFieldNameChars();
        if (method != null) {
            TypeUtils.setAccessible(method);
        }
        if (field != null) {
            TypeUtils.setAccessible(field);
        }
        if (method != null) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 1) {
                Class<?> cls2 = parameterTypes[0];
                genericType = method.getGenericParameterTypes()[0];
                type2 = cls2;
            } else if (parameterTypes.length == 2 && parameterTypes[0] == String.class && parameterTypes[1] == Object.class) {
                type2 = parameterTypes[0];
                genericType = type2;
            } else {
                Class<?> returnType = method.getReturnType();
                genericType = method.getGenericReturnType();
                type2 = returnType;
                isFinal = true;
                this.declaringClass = method.getDeclaringClass();
            }
            isFinal = false;
            this.declaringClass = method.getDeclaringClass();
        } else {
            type2 = field.getType();
            genericType = field.getGenericType();
            this.declaringClass = field.getDeclaringClass();
            isFinal = Modifier.isFinal(field.getModifiers());
        }
        this.getOnly = isFinal;
        this.jsonDirect = z && type2 == String.class;
        if (cls != null && type2 == Object.class && (genericType instanceof TypeVariable) && (inheritGenericType = getInheritGenericType(cls, type, (TypeVariable) genericType)) != null) {
            this.fieldClass = TypeUtils.getClass(inheritGenericType);
            this.fieldType = inheritGenericType;
            this.isEnum = type2.isEnum();
            return;
        }
        if (genericType instanceof Class) {
            type3 = genericType;
        } else {
            type3 = getFieldType(cls, type == null ? cls : type, genericType);
            if (type3 != genericType) {
                if (type3 instanceof ParameterizedType) {
                    type2 = TypeUtils.getClass(type3);
                } else if (type3 instanceof Class) {
                    type2 = TypeUtils.getClass(type3);
                }
            }
        }
        this.fieldType = type3;
        this.fieldClass = type2;
        this.isEnum = type2.isEnum();
    }

    private static boolean getArgument(Type[] typeArr, TypeVariable[] typeVariableArr, Type[] typeArr2) {
        if (typeArr2 == null || typeVariableArr.length == 0) {
            return false;
        }
        boolean z = false;
        for (int i = 0; i < typeArr.length; i++) {
            Type type = typeArr[i];
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (getArgument(actualTypeArguments, typeVariableArr, typeArr2)) {
                    typeArr[i] = new ParameterizedTypeImpl(actualTypeArguments, parameterizedType.getOwnerType(), parameterizedType.getRawType());
                    z = true;
                }
            } else if (type instanceof TypeVariable) {
                boolean z2 = z;
                for (int i2 = 0; i2 < typeVariableArr.length; i2++) {
                    if (type.equals(typeVariableArr[i2])) {
                        typeArr[i] = typeArr2[i2];
                        z2 = true;
                    }
                }
                z = z2;
            }
        }
        return z;
    }

    public static Type getFieldType(Class<?> cls, Type type, Type type2) {
        TypeVariable<Class<?>>[] typeParameters;
        ParameterizedType parameterizedType;
        if (cls == null || type == null) {
            return type2;
        }
        if (type2 instanceof GenericArrayType) {
            Type genericComponentType = ((GenericArrayType) type2).getGenericComponentType();
            Type fieldType = getFieldType(cls, type, genericComponentType);
            return genericComponentType != fieldType ? Array.newInstance(TypeUtils.getClass(fieldType), 0).getClass() : type2;
        }
        if (!TypeUtils.isGenericParamType(type)) {
            return type2;
        }
        if (type2 instanceof TypeVariable) {
            ParameterizedType parameterizedType2 = (ParameterizedType) TypeUtils.getGenericParamType(type);
            TypeVariable typeVariable = (TypeVariable) type2;
            TypeVariable<Class<?>>[] typeParameters2 = TypeUtils.getClass(parameterizedType2).getTypeParameters();
            for (int i = 0; i < typeParameters2.length; i++) {
                if (typeParameters2[i].getName().equals(typeVariable.getName())) {
                    return parameterizedType2.getActualTypeArguments()[i];
                }
            }
        }
        if (type2 instanceof ParameterizedType) {
            ParameterizedType parameterizedType3 = (ParameterizedType) type2;
            Type[] actualTypeArguments = parameterizedType3.getActualTypeArguments();
            if (type instanceof ParameterizedType) {
                parameterizedType = (ParameterizedType) type;
                typeParameters = cls.getTypeParameters();
            } else if (cls.getGenericSuperclass() instanceof ParameterizedType) {
                parameterizedType = (ParameterizedType) cls.getGenericSuperclass();
                typeParameters = cls.getSuperclass().getTypeParameters();
            } else {
                typeParameters = type.getClass().getTypeParameters();
                parameterizedType = parameterizedType3;
            }
            if (getArgument(actualTypeArguments, typeParameters, parameterizedType.getActualTypeArguments())) {
                return new ParameterizedTypeImpl(actualTypeArguments, parameterizedType3.getOwnerType(), parameterizedType3.getRawType());
            }
        }
        return type2;
    }

    private static Type getInheritGenericType(Class<?> cls, Type type, TypeVariable<?> typeVariable) {
        Type[] typeArr;
        Class<?> cls2 = typeVariable.getGenericDeclaration() instanceof Class ? (Class) typeVariable.getGenericDeclaration() : null;
        if (cls2 == cls) {
            typeArr = type instanceof ParameterizedType ? ((ParameterizedType) type).getActualTypeArguments() : null;
        } else {
            Type[] typeArr2 = null;
            while (cls != null && cls != Object.class && cls != cls2) {
                Type genericSuperclass = cls.getGenericSuperclass();
                if (genericSuperclass instanceof ParameterizedType) {
                    Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
                    getArgument(actualTypeArguments, cls.getTypeParameters(), typeArr2);
                    typeArr2 = actualTypeArguments;
                }
                cls = cls.getSuperclass();
            }
            typeArr = typeArr2;
        }
        if (typeArr == null || cls2 == null) {
            return null;
        }
        TypeVariable<Class<?>>[] typeParameters = cls2.getTypeParameters();
        for (int i = 0; i < typeParameters.length; i++) {
            if (typeVariable.equals(typeParameters[i])) {
                return typeArr[i];
            }
        }
        return null;
    }

    @Override // java.lang.Comparable
    public int compareTo(FieldInfo fieldInfo) {
        if (this.ordinal < fieldInfo.ordinal) {
            return -1;
        }
        if (this.ordinal > fieldInfo.ordinal) {
            return 1;
        }
        int compareTo = this.name.compareTo(fieldInfo.name);
        if (compareTo != 0) {
            return compareTo;
        }
        Class<?> declaredClass = getDeclaredClass();
        Class<?> declaredClass2 = fieldInfo.getDeclaredClass();
        if (declaredClass != null && declaredClass2 != null && declaredClass != declaredClass2) {
            if (declaredClass.isAssignableFrom(declaredClass2)) {
                return -1;
            }
            if (declaredClass2.isAssignableFrom(declaredClass)) {
                return 1;
            }
        }
        boolean z = false;
        boolean z2 = this.field != null && this.field.getType() == this.fieldClass;
        if (fieldInfo.field != null && fieldInfo.field.getType() == fieldInfo.fieldClass) {
            z = true;
        }
        if (z2 && !z) {
            return 1;
        }
        if (z && !z2) {
            return -1;
        }
        if (fieldInfo.fieldClass.isPrimitive() && !this.fieldClass.isPrimitive()) {
            return 1;
        }
        if (this.fieldClass.isPrimitive() && !fieldInfo.fieldClass.isPrimitive()) {
            return -1;
        }
        if (fieldInfo.fieldClass.getName().startsWith("java.") && !this.fieldClass.getName().startsWith("java.")) {
            return 1;
        }
        if (!this.fieldClass.getName().startsWith("java.") || fieldInfo.fieldClass.getName().startsWith("java.")) {
            return this.fieldClass.getName().compareTo(fieldInfo.fieldClass.getName());
        }
        return -1;
    }

    protected char[] genFieldNameChars() {
        int length = this.name.length();
        char[] cArr = new char[length + 3];
        this.name.getChars(0, this.name.length(), cArr, 1);
        cArr[0] = Typography.quote;
        cArr[length + 1] = Typography.quote;
        cArr[length + 2] = ':';
        return cArr;
    }

    public Object get(Object obj) throws IllegalAccessException, InvocationTargetException {
        return this.method != null ? this.method.invoke(obj, new Object[0]) : this.field.get(obj);
    }

    public <T extends Annotation> T getAnnation(Class<T> cls) {
        if (cls == JSONField.class) {
            return getAnnotation();
        }
        T t = null;
        if (this.method != null) {
            t = (T) this.method.getAnnotation(cls);
        }
        return (t != null || this.field == null) ? t : (T) this.field.getAnnotation(cls);
    }

    public JSONField getAnnotation() {
        return this.fieldAnnotation != null ? this.fieldAnnotation : this.methodAnnotation;
    }

    protected Class<?> getDeclaredClass() {
        if (this.method != null) {
            return this.method.getDeclaringClass();
        }
        if (this.field != null) {
            return this.field.getDeclaringClass();
        }
        return null;
    }

    public String getFormat() {
        return this.format;
    }

    public Member getMember() {
        return this.method != null ? this.method : this.field;
    }

    public void set(Object obj, Object obj2) throws IllegalAccessException, InvocationTargetException {
        if (this.method != null) {
            this.method.invoke(obj, obj2);
        } else {
            this.field.set(obj, obj2);
        }
    }

    public void setAccessible() throws SecurityException {
        if (this.method != null) {
            TypeUtils.setAccessible(this.method);
        } else {
            TypeUtils.setAccessible(this.field);
        }
    }

    public String toString() {
        return this.name;
    }
}
