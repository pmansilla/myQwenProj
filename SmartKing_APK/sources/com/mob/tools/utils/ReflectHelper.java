package com.mob.tools.utils;

import android.content.BroadcastReceiver;
import com.mob.tools.gui.CachePool;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class ReflectHelper {
    private static CachePool<String, Constructor<?>> cachedConstr;
    private static CachePool<String, Method> cachedMethod;
    private static HashMap<String, Class<?>> classMap;
    private static HashMap<Class<?>, String> nameMap;
    private static HashSet<String> packageSet = new HashSet<>();

    /* loaded from: classes2.dex */
    public interface ReflectRunnable<ArgType, RetType> {
        RetType run(ArgType argtype);
    }

    static {
        packageSet.add("java.lang");
        packageSet.add("java.io");
        packageSet.add("java.nio");
        packageSet.add("java.net");
        packageSet.add("java.util");
        packageSet.add("com.mob.tools");
        packageSet.add("com.mob.tools.gui");
        packageSet.add("com.mob.tools.log");
        packageSet.add("com.mob.tools.network");
        packageSet.add("com.mob.tools.utils");
        classMap = new HashMap<>();
        classMap.put("double", Double.TYPE);
        classMap.put("float", Float.TYPE);
        classMap.put("long", Long.TYPE);
        classMap.put("int", Integer.TYPE);
        classMap.put("short", Short.TYPE);
        classMap.put("byte", Byte.TYPE);
        classMap.put("char", Character.TYPE);
        classMap.put("boolean", Boolean.TYPE);
        classMap.put("Object", Object.class);
        classMap.put("String", String.class);
        classMap.put("Thread", Thread.class);
        classMap.put("Runnable", Runnable.class);
        classMap.put("System", System.class);
        classMap.put("double", Double.class);
        classMap.put("Float", Float.class);
        classMap.put("Long", Long.class);
        classMap.put("Integer", Integer.class);
        classMap.put("Short", Short.class);
        classMap.put("Byte", Byte.class);
        classMap.put("Character", Character.class);
        classMap.put("Boolean", Boolean.class);
        nameMap = new HashMap<>();
        for (Map.Entry<String, Class<?>> entry : classMap.entrySet()) {
            nameMap.put(entry.getValue(), entry.getKey());
        }
        cachedMethod = new CachePool<>(25);
        cachedConstr = new CachePool<>(5);
    }

    public static Object createProxy(HashMap<String, ReflectRunnable<Object, Object[]>> hashMap, Class<?>... clsArr) throws Throwable {
        HashMap hashMap2 = new HashMap();
        for (final Map.Entry<String, ReflectRunnable<Object, Object[]>> entry : hashMap.entrySet()) {
            hashMap2.put(entry.getKey(), new ReflectRunnable<Object[], Object>() { // from class: com.mob.tools.utils.ReflectHelper.1
                @Override // com.mob.tools.utils.ReflectHelper.ReflectRunnable
                public Object run(Object[] objArr) {
                    return ((Object[]) ((ReflectRunnable) entry.getValue()).run(objArr))[0];
                }
            });
        }
        return createProxy((Map<String, ReflectRunnable<Object[], Object>>) hashMap2, clsArr);
    }

    public static Object createProxy(final Map<String, ReflectRunnable<Object[], Object>> map, Class<?>... clsArr) throws Throwable {
        if (clsArr.length == 0) {
            return null;
        }
        return Proxy.newProxyInstance(clsArr[0].getClassLoader(), clsArr, new InvocationHandler() { // from class: com.mob.tools.utils.ReflectHelper.2
            @Override // java.lang.reflect.InvocationHandler
            public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
                ReflectRunnable reflectRunnable = (ReflectRunnable) map.get(method.getName());
                if (reflectRunnable != null) {
                    return reflectRunnable.run(objArr);
                }
                throw new NoSuchMethodException();
            }
        });
    }

    public static Class<?> getClass(String str) throws Throwable {
        Class<?> importedClass = getImportedClass(str);
        if (importedClass != null) {
            return importedClass;
        }
        try {
            Class<?> cls = Class.forName(str);
            if (cls != null) {
                try {
                    classMap.put(str, cls);
                } catch (Throwable unused) {
                }
            }
            return cls;
        } catch (Throwable unused2) {
            return importedClass;
        }
    }

    private static synchronized Class<?> getImportedClass(String str) {
        Class<?> cls;
        synchronized (ReflectHelper.class) {
            cls = classMap.get(str);
            if (cls == null) {
                Iterator<String> it = packageSet.iterator();
                while (it.hasNext()) {
                    try {
                        importClass(it.next() + "." + str);
                    } catch (Throwable unused) {
                    }
                    cls = classMap.get(str);
                    if (cls != null) {
                        break;
                    }
                }
            }
        }
        return cls;
    }

    public static <T> T getInstanceField(Object obj, String str) throws Throwable {
        try {
            return (T) onGetInstanceField(obj, str);
        } catch (Throwable th) {
            if (th instanceof NoSuchFieldException) {
                throw th;
            }
            throw new Throwable("className: " + obj.getClass() + ", fieldName: " + str, th);
        }
    }

    public static String getName(Class<?> cls) throws Throwable {
        String str = nameMap.get(cls);
        if (str == null) {
            str = cls.getSimpleName();
            if (classMap.containsKey(str)) {
                nameMap.remove(classMap.get(str));
            }
            classMap.put(str, cls);
            nameMap.put(cls, str);
        }
        return str;
    }

    public static <T> T getStaticField(String str, String str2) throws Throwable {
        try {
            return (T) onGetStaticField(str, str2);
        } catch (Throwable th) {
            if (th instanceof NoSuchFieldException) {
                throw th;
            }
            throw new Throwable("className: " + str + ", fieldName: " + str2, th);
        }
    }

    private static Class<?>[] getTypes(Object[] objArr) {
        Class<?>[] clsArr = new Class[objArr.length];
        for (int i = 0; i < objArr.length; i++) {
            if (objArr[i] instanceof BroadcastReceiver) {
                clsArr[i] = BroadcastReceiver.class;
            } else {
                clsArr[i] = objArr[i] == null ? null : objArr[i].getClass();
            }
        }
        return clsArr;
    }

    public static String importClass(String str) throws Throwable {
        return importClass(null, str);
    }

    public static synchronized String importClass(String str, String str2) throws Throwable {
        synchronized (ReflectHelper.class) {
            if (str2.endsWith(".*")) {
                packageSet.add(str2.substring(0, str2.length() - 2));
                return "*";
            }
            Class<?> cls = Class.forName(str2);
            if (str == null) {
                str = cls.getSimpleName();
            }
            if (classMap.containsKey(str)) {
                nameMap.remove(classMap.get(str));
            }
            classMap.put(str, cls);
            nameMap.put(cls, str);
            return str;
        }
    }

    public static <T> T invokeInstanceMethod(Object obj, String str, Object... objArr) throws Throwable {
        try {
            return (T) invokeMethod(null, obj, str, objArr);
        } catch (Throwable th) {
            if (th instanceof NoSuchMethodException) {
                throw th;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("className: ");
            sb.append(obj != null ? obj.getClass() : null);
            sb.append(", methodName: ");
            sb.append(str);
            throw new Throwable(sb.toString(), th);
        }
    }

    public static <T> T invokeInstanceMethod(Object obj, String str, Object[] objArr, Class<?>[] clsArr) throws Throwable {
        return (T) invokeMethod(null, obj, str, objArr, clsArr);
    }

    private static <T> T invokeMethod(String str, Object obj, String str2, Object... objArr) throws Throwable {
        Class<?>[] types;
        Class<?> importedClass = obj == null ? getImportedClass(str) : obj.getClass();
        boolean z = false;
        if (str2.equals("getMethod") && objArr != null && objArr.length == 2) {
            types = new Class[]{String.class, Class[].class};
            if (objArr[1] == String.class) {
                Class[] clsArr = new Class[1];
                clsArr[0] = String.class;
                objArr[1] = clsArr;
            }
        } else {
            types = (str2.equals("getDeviceId") && objArr != null && objArr.length == 1) ? new Class[]{Integer.TYPE} : (str2.equals("invoke") && objArr != null && objArr.length == 2) ? new Class[]{Object.class, Object[].class} : (str2.equals("setAccessible") && objArr != null && objArr.length == 1) ? new Class[]{Boolean.TYPE} : getTypes(objArr);
        }
        StringBuffer stringBuffer = new StringBuffer();
        int length = types.length;
        for (int i = 0; i < length; i++) {
            Class<?> cls = types[i];
            stringBuffer.append(cls == null ? "" : cls.getName());
        }
        String str3 = importedClass.getName() + "#" + str2 + "#" + objArr.length + stringBuffer.toString();
        Method method = cachedMethod.get(str3);
        if (method != null) {
            boolean isStatic = Modifier.isStatic(method.getModifiers());
            if (obj == null) {
                z = isStatic;
            } else if (!isStatic) {
                z = true;
            }
            if (z && matchParams(method.getParameterTypes(), types)) {
                method.setAccessible(true);
                if (method.getReturnType() != Void.TYPE) {
                    return (T) method.invoke(obj, objArr);
                }
                method.invoke(obj, objArr);
                return null;
            }
        }
        while (importedClass != null) {
            try {
                Method declaredMethod = importedClass.getDeclaredMethod(str2, types);
                cachedMethod.put(str3, declaredMethod);
                declaredMethod.setAccessible(true);
                if (declaredMethod.getReturnType() != Void.TYPE) {
                    return (T) declaredMethod.invoke(obj, objArr);
                }
                declaredMethod.invoke(obj, objArr);
                return null;
            } catch (Throwable unused) {
                importedClass = importedClass.getSuperclass();
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("className: ");
        Object obj2 = str;
        if (obj != null) {
            obj2 = obj.getClass();
        }
        sb.append(obj2);
        sb.append(", methodName: ");
        sb.append(str2);
        throw new NoSuchMethodException(sb.toString());
    }

    private static <T> T invokeMethod(String str, Object obj, String str2, Object[] objArr, Class<?>[] clsArr) throws Throwable {
        if (objArr == null) {
            objArr = new Object[0];
        }
        if (clsArr == null) {
            clsArr = new Class[0];
        }
        Class<?> importedClass = obj == null ? getImportedClass(str) : obj.getClass();
        String str3 = importedClass.getName() + "#" + str2 + "#" + objArr.length;
        Method method = cachedMethod.get(str3);
        if (method != null) {
            method.setAccessible(true);
            if (method.getReturnType() != Void.TYPE) {
                return (T) method.invoke(obj, objArr);
            }
            method.invoke(obj, objArr);
            return null;
        }
        while (importedClass != null) {
            try {
                Method declaredMethod = importedClass.getDeclaredMethod(str2, clsArr);
                cachedMethod.put(str3, declaredMethod);
                declaredMethod.setAccessible(true);
                if (declaredMethod.getReturnType() != Void.TYPE) {
                    return (T) declaredMethod.invoke(obj, objArr);
                }
                declaredMethod.invoke(obj, objArr);
                return null;
            } catch (Throwable unused) {
                importedClass = importedClass.getSuperclass();
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("className: ");
        Object obj2 = str;
        if (obj != null) {
            obj2 = obj.getClass();
        }
        sb.append(obj2);
        sb.append(", methodName: ");
        sb.append(str2);
        throw new NoSuchMethodException(sb.toString());
    }

    public static <T> T invokeStaticMethod(String str, String str2, Object... objArr) throws Throwable {
        try {
            return (T) invokeMethod(str, null, str2, objArr);
        } catch (Throwable th) {
            if (th instanceof NoSuchMethodException) {
                throw th;
            }
            throw new Throwable("className: " + str + ", methodName: " + str2, th);
        }
    }

    public static <T> T invokeStaticMethod(String str, String str2, Object[] objArr, Class<?>[] clsArr) throws Throwable {
        return (T) invokeMethod(str, null, str2, objArr, clsArr);
    }

    private static boolean matchParams(Class<?>[] clsArr, Class<?>[] clsArr2) {
        if (clsArr.length != clsArr2.length) {
            return false;
        }
        for (int i = 0; i < clsArr2.length; i++) {
            if (clsArr2[i] != null && !primitiveEquals(clsArr[i], clsArr2[i]) && !clsArr[i].isAssignableFrom(clsArr2[i])) {
                return false;
            }
        }
        return true;
    }

    private static Object newArray(String str, Object... objArr) throws Throwable {
        int i = 0;
        String str2 = str;
        while (str2.startsWith("[")) {
            i++;
            str2 = str2.substring(1);
        }
        int[] iArr = null;
        if (i == objArr.length) {
            int[] iArr2 = new int[i];
            for (int i2 = 0; i2 < i; i2++) {
                try {
                    iArr2[i2] = Integer.parseInt(String.valueOf(objArr[i2]));
                } catch (Throwable unused) {
                }
            }
            iArr = iArr2;
        }
        if (iArr != null) {
            Class<?> importedClass = "B".equals(str2) ? Byte.TYPE : "S".equals(str2) ? Short.TYPE : "I".equals(str2) ? Integer.TYPE : "J".equals(str2) ? Long.TYPE : "F".equals(str2) ? Float.TYPE : "D".equals(str2) ? Double.TYPE : "Z".equals(str2) ? Boolean.TYPE : "C".equals(str2) ? Character.TYPE : getImportedClass(str2);
            if (importedClass != null) {
                return Array.newInstance(importedClass, iArr);
            }
        }
        throw new NoSuchMethodException("className: [" + str + ", methodName: <init>");
    }

    public static Object newInstance(String str, Object... objArr) throws Throwable {
        try {
            return onNewInstance(str, objArr);
        } catch (Throwable th) {
            if (th instanceof NoSuchMethodException) {
                throw th;
            }
            throw new Throwable("className: " + str + ", methodName: <init>", th);
        }
    }

    private static Object onGetElement(Object obj, String str) throws Throwable {
        int i;
        int i2;
        if (obj instanceof List) {
            if (str.startsWith("[") && str.endsWith("]")) {
                try {
                    i2 = Integer.parseInt(str.substring(1, str.length() - 1));
                } catch (Throwable unused) {
                    i2 = -1;
                }
                if (i2 != -1) {
                    return ((List) obj).get(i2);
                }
            }
        } else {
            if ("length".equals(str)) {
                return Integer.valueOf(Array.getLength(obj));
            }
            if (str.startsWith("[") && str.endsWith("]")) {
                try {
                    i = Integer.parseInt(str.substring(1, str.length() - 1));
                } catch (Throwable unused2) {
                    i = -1;
                }
                if (i != -1) {
                    return Array.get(obj, i);
                }
            }
        }
        throw new NoSuchFieldException("className: " + obj.getClass() + ", fieldName: " + str);
    }

    private static <T> T onGetInstanceField(Object obj, String str) throws Throwable {
        Field field;
        if ((obj instanceof List) || obj.getClass().isArray()) {
            return (T) onGetElement(obj, str);
        }
        if (obj instanceof Map) {
            return (T) ((Map) obj).get(str);
        }
        ArrayList arrayList = new ArrayList();
        for (Class<?> cls = obj.getClass(); cls != null; cls = cls.getSuperclass()) {
            arrayList.add(cls);
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            try {
                field = ((Class) it.next()).getDeclaredField(str);
            } catch (Throwable unused) {
                field = null;
            }
            if (field != null && !Modifier.isStatic(field.getModifiers())) {
                field.setAccessible(true);
                return (T) field.get(obj);
            }
        }
        throw new NoSuchFieldException("className: " + obj.getClass() + ", fieldName: " + str);
    }

    private static <T> T onGetStaticField(String str, String str2) throws Throwable {
        Field field;
        ArrayList arrayList = new ArrayList();
        for (Class<?> importedClass = getImportedClass(str); importedClass != null; importedClass = importedClass.getSuperclass()) {
            arrayList.add(importedClass);
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            try {
                field = ((Class) it.next()).getDeclaredField(str2);
            } catch (Throwable unused) {
                field = null;
            }
            if (field != null && Modifier.isStatic(field.getModifiers())) {
                field.setAccessible(true);
                return (T) field.get(null);
            }
        }
        throw new NoSuchFieldException("className: " + str + ", fieldName: " + str2);
    }

    private static Object onNewInstance(String str, Object... objArr) throws Throwable {
        boolean z;
        if (str.startsWith("[")) {
            return newArray(str, objArr);
        }
        Class<?> importedClass = getImportedClass(str);
        String str2 = importedClass.getName() + "#" + objArr.length;
        Constructor<?> constructor = cachedConstr.get(str2);
        Class<?>[] types = getTypes(objArr);
        if (constructor != null && matchParams(constructor.getParameterTypes(), types)) {
            constructor.setAccessible(true);
            return constructor.newInstance(objArr);
        }
        Constructor<?>[] declaredConstructors = importedClass.getDeclaredConstructors();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (Constructor<?> constructor2 : declaredConstructors) {
            Class<?>[] parameterTypes = constructor2.getParameterTypes();
            if (matchParams(parameterTypes, types)) {
                cachedConstr.put(str2, constructor2);
                constructor2.setAccessible(true);
                return constructor2.newInstance(objArr);
            }
            if (parameterTypes.length > 0 && parameterTypes[parameterTypes.length - 1].isArray() && types.length >= parameterTypes.length - 1) {
                arrayList.add(constructor2);
                arrayList2.add(parameterTypes);
            }
        }
        for (int i = 0; i < arrayList2.size(); i++) {
            Class[] clsArr = (Class[]) arrayList2.get(i);
            Class<?> componentType = clsArr[clsArr.length - 1].getComponentType();
            if (tryMatchParams(clsArr, types)) {
                Object[] objArr2 = new Object[objArr.length + 1];
                System.arraycopy(objArr, 0, objArr2, 0, objArr.length);
                objArr2[objArr.length] = Array.newInstance(componentType, 0);
                Constructor constructor3 = (Constructor) arrayList.get(i);
                constructor3.setAccessible(true);
                return constructor3.newInstance(objArr);
            }
            int length = clsArr.length - 1;
            while (true) {
                if (length >= types.length) {
                    z = true;
                    break;
                }
                if (!types[length].equals(componentType)) {
                    z = false;
                    break;
                }
                length++;
            }
            if (z) {
                int length2 = (types.length - clsArr.length) + 1;
                Object newInstance = Array.newInstance(componentType, length2);
                for (int i2 = 0; i2 < length2; i2++) {
                    Array.set(newInstance, i2, objArr[(clsArr.length - 1) + i2]);
                }
                Object[] objArr3 = new Object[objArr.length + 1];
                System.arraycopy(objArr, 0, objArr3, 0, objArr.length);
                objArr3[objArr.length] = newInstance;
                Constructor constructor4 = (Constructor) arrayList.get(i);
                constructor4.setAccessible(true);
                return constructor4.newInstance(objArr);
            }
        }
        throw new NoSuchMethodException("className: " + str + ", methodName: <init>");
    }

    private static void onSetElement(Object obj, String str, Object obj2) throws Throwable {
        int i;
        int i2;
        if (obj instanceof List) {
            if (str.startsWith("[") && str.endsWith("]")) {
                try {
                    i2 = Integer.parseInt(str.substring(1, str.length() - 1));
                } catch (Throwable unused) {
                    i2 = -1;
                }
                if (i2 != -1) {
                    ((List) obj).set(i2, obj2);
                    return;
                }
            }
        } else if (str.startsWith("[") && str.endsWith("]")) {
            try {
                i = Integer.parseInt(str.substring(1, str.length() - 1));
            } catch (Throwable unused2) {
                i = -1;
            }
            if (i != -1) {
                String name = obj.getClass().getName();
                while (name.startsWith("[")) {
                    name = name.substring(1);
                }
                Class<?> cls = obj2.getClass();
                if ("B".equals(name)) {
                    if (cls == Byte.class) {
                        Array.set(obj, i, obj2);
                        return;
                    }
                } else if ("S".equals(name)) {
                    Object valueOf = cls == Short.class ? obj2 : cls == Byte.class ? Short.valueOf(((Byte) obj2).byteValue()) : null;
                    if (valueOf != null) {
                        Array.set(obj, i, valueOf);
                        return;
                    }
                } else if ("I".equals(name)) {
                    Object valueOf2 = cls == Integer.class ? obj2 : cls == Short.class ? Integer.valueOf(((Short) obj2).shortValue()) : cls == Byte.class ? Integer.valueOf(((Byte) obj2).byteValue()) : null;
                    if (valueOf2 != null) {
                        Array.set(obj, i, valueOf2);
                        return;
                    }
                } else if ("J".equals(name)) {
                    Object valueOf3 = cls == Long.class ? obj2 : cls == Integer.class ? Long.valueOf(((Integer) obj2).intValue()) : cls == Short.class ? Long.valueOf(((Short) obj2).shortValue()) : cls == Byte.class ? Long.valueOf(((Byte) obj2).byteValue()) : null;
                    if (valueOf3 != null) {
                        Array.set(obj, i, valueOf3);
                        return;
                    }
                } else if ("F".equals(name)) {
                    Object valueOf4 = cls == Float.class ? obj2 : cls == Long.class ? Float.valueOf((float) ((Long) obj2).longValue()) : cls == Integer.class ? Float.valueOf(((Integer) obj2).intValue()) : cls == Short.class ? Float.valueOf(((Short) obj2).shortValue()) : cls == Byte.class ? Float.valueOf(((Byte) obj2).byteValue()) : null;
                    if (valueOf4 != null) {
                        Array.set(obj, i, valueOf4);
                        return;
                    }
                } else if ("D".equals(name)) {
                    Object valueOf5 = cls == Double.class ? obj2 : cls == Float.class ? Double.valueOf(((Float) obj2).floatValue()) : cls == Long.class ? Double.valueOf(((Long) obj2).longValue()) : cls == Integer.class ? Double.valueOf(((Integer) obj2).intValue()) : cls == Short.class ? Double.valueOf(((Short) obj2).shortValue()) : cls == Byte.class ? Double.valueOf(((Byte) obj2).byteValue()) : null;
                    if (valueOf5 != null) {
                        Array.set(obj, i, valueOf5);
                        return;
                    }
                } else if ("Z".equals(name)) {
                    if (cls == Boolean.class) {
                        Array.set(obj, i, obj2);
                        return;
                    }
                } else if ("C".equals(name)) {
                    if (cls == Character.class) {
                        Array.set(obj, i, obj2);
                        return;
                    }
                } else if (name.equals(cls.getName())) {
                    Array.set(obj, i, obj2);
                    return;
                }
            }
        }
        throw new NoSuchFieldException("className: " + obj.getClass() + ", fieldName: " + str + ", value: " + String.valueOf(obj2));
    }

    private static void onSetInstanceField(Object obj, String str, Object obj2) throws Throwable {
        Field field;
        if ((obj instanceof List) || obj.getClass().isArray()) {
            onSetElement(obj, str, obj2);
            return;
        }
        if (obj instanceof Map) {
            ((Map) obj).put(str, obj2);
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (Class<?> cls = obj.getClass(); cls != null; cls = cls.getSuperclass()) {
            arrayList.add(cls);
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            try {
                field = ((Class) it.next()).getDeclaredField(str);
            } catch (Throwable unused) {
                field = null;
            }
            if (field != null && !Modifier.isStatic(field.getModifiers())) {
                field.setAccessible(true);
                field.set(obj, obj2);
                return;
            }
        }
        throw new NoSuchFieldException("className: " + obj.getClass() + ", fieldName: " + str + ", value: " + String.valueOf(obj2));
    }

    private static void onSetStaticField(String str, String str2, Object obj) throws Throwable {
        Field field;
        ArrayList arrayList = new ArrayList();
        for (Class<?> importedClass = getImportedClass(str); importedClass != null; importedClass = importedClass.getSuperclass()) {
            arrayList.add(importedClass);
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            try {
                field = ((Class) it.next()).getDeclaredField(str2);
            } catch (Throwable unused) {
                field = null;
            }
            if (field != null && Modifier.isStatic(field.getModifiers())) {
                field.setAccessible(true);
                field.set(null, obj);
                return;
            }
        }
        throw new NoSuchFieldException("className: " + str + ", fieldName: " + str2 + ", value: " + String.valueOf(obj));
    }

    private static boolean primitiveEquals(Class<?> cls, Class<?> cls2) {
        return (cls == Byte.TYPE && cls2 == Byte.class) || (cls == Short.TYPE && (cls2 == Short.class || cls2 == Byte.class || cls2 == Character.class)) || ((cls == Character.TYPE && (cls2 == Character.class || cls2 == Short.class || cls2 == Byte.class)) || ((cls == Integer.TYPE && (cls2 == Integer.class || cls2 == Short.class || cls2 == Byte.class || cls2 == Character.class)) || ((cls == Long.TYPE && (cls2 == Long.class || cls2 == Integer.class || cls2 == Short.class || cls2 == Byte.class || cls2 == Character.class)) || ((cls == Float.TYPE && (cls2 == Float.class || cls2 == Long.class || cls2 == Integer.class || cls2 == Short.class || cls2 == Byte.class || cls2 == Character.class)) || ((cls == Double.TYPE && (cls2 == Double.class || cls2 == Float.class || cls2 == Long.class || cls2 == Integer.class || cls2 == Short.class || cls2 == Byte.class || cls2 == Character.class)) || (cls == Boolean.TYPE && cls2 == Boolean.class))))));
    }

    public static void setInstanceField(Object obj, String str, Object obj2) throws Throwable {
        try {
            onSetInstanceField(obj, str, obj2);
        } catch (Throwable th) {
            if (th instanceof NoSuchFieldException) {
                throw th;
            }
            throw new Throwable("className: " + obj.getClass() + ", fieldName: " + str + ", value: " + String.valueOf(obj2), th);
        }
    }

    public static void setStaticField(String str, String str2, Object obj) throws Throwable {
        try {
            onSetStaticField(str, str2, obj);
        } catch (Throwable th) {
            if (th instanceof NoSuchFieldException) {
                throw th;
            }
            throw new Throwable("className: " + str + ", fieldName: " + str2 + ", value: " + String.valueOf(obj), th);
        }
    }

    private static boolean tryMatchParams(Class<?>[] clsArr, Class<?>[] clsArr2) {
        boolean z;
        if (clsArr.length - clsArr2.length != 1) {
            return false;
        }
        int i = 0;
        while (true) {
            if (i >= clsArr2.length) {
                z = true;
                break;
            }
            if (clsArr2[i] != null && !primitiveEquals(clsArr[i], clsArr2[i]) && !clsArr[i].isAssignableFrom(clsArr2[i])) {
                z = false;
                break;
            }
            i++;
        }
        return z && clsArr[clsArr.length - 1].isArray();
    }
}
