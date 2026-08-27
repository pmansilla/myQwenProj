package org.apache.commons.lang;

import com.litesuits.orm.db.assit.SQLBuilder;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class ClassUtils {
    public static final char INNER_CLASS_SEPARATOR_CHAR = '$';
    private static Map abbreviationMap;
    static /* synthetic */ Class class$java$lang$Boolean;
    static /* synthetic */ Class class$java$lang$Byte;
    static /* synthetic */ Class class$java$lang$Character;
    static /* synthetic */ Class class$java$lang$Double;
    static /* synthetic */ Class class$java$lang$Float;
    static /* synthetic */ Class class$java$lang$Integer;
    static /* synthetic */ Class class$java$lang$Long;
    static /* synthetic */ Class class$java$lang$Short;
    static /* synthetic */ Class class$org$apache$commons$lang$ClassUtils;
    private static Map reverseAbbreviationMap;
    private static Map wrapperPrimitiveMap;
    public static final char PACKAGE_SEPARATOR_CHAR = '.';
    public static final String PACKAGE_SEPARATOR = String.valueOf(PACKAGE_SEPARATOR_CHAR);
    public static final String INNER_CLASS_SEPARATOR = String.valueOf('$');
    private static Map primitiveWrapperMap = new HashMap();

    static {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        Class cls5;
        Class cls6;
        Class cls7;
        Class cls8;
        Map map = primitiveWrapperMap;
        Class cls9 = Boolean.TYPE;
        if (class$java$lang$Boolean == null) {
            cls = class$("java.lang.Boolean");
            class$java$lang$Boolean = cls;
        } else {
            cls = class$java$lang$Boolean;
        }
        map.put(cls9, cls);
        Map map2 = primitiveWrapperMap;
        Class cls10 = Byte.TYPE;
        if (class$java$lang$Byte == null) {
            cls2 = class$("java.lang.Byte");
            class$java$lang$Byte = cls2;
        } else {
            cls2 = class$java$lang$Byte;
        }
        map2.put(cls10, cls2);
        Map map3 = primitiveWrapperMap;
        Class cls11 = Character.TYPE;
        if (class$java$lang$Character == null) {
            cls3 = class$("java.lang.Character");
            class$java$lang$Character = cls3;
        } else {
            cls3 = class$java$lang$Character;
        }
        map3.put(cls11, cls3);
        Map map4 = primitiveWrapperMap;
        Class cls12 = Short.TYPE;
        if (class$java$lang$Short == null) {
            cls4 = class$("java.lang.Short");
            class$java$lang$Short = cls4;
        } else {
            cls4 = class$java$lang$Short;
        }
        map4.put(cls12, cls4);
        Map map5 = primitiveWrapperMap;
        Class cls13 = Integer.TYPE;
        if (class$java$lang$Integer == null) {
            cls5 = class$("java.lang.Integer");
            class$java$lang$Integer = cls5;
        } else {
            cls5 = class$java$lang$Integer;
        }
        map5.put(cls13, cls5);
        Map map6 = primitiveWrapperMap;
        Class cls14 = Long.TYPE;
        if (class$java$lang$Long == null) {
            cls6 = class$("java.lang.Long");
            class$java$lang$Long = cls6;
        } else {
            cls6 = class$java$lang$Long;
        }
        map6.put(cls14, cls6);
        Map map7 = primitiveWrapperMap;
        Class cls15 = Double.TYPE;
        if (class$java$lang$Double == null) {
            cls7 = class$("java.lang.Double");
            class$java$lang$Double = cls7;
        } else {
            cls7 = class$java$lang$Double;
        }
        map7.put(cls15, cls7);
        Map map8 = primitiveWrapperMap;
        Class cls16 = Float.TYPE;
        if (class$java$lang$Float == null) {
            cls8 = class$("java.lang.Float");
            class$java$lang$Float = cls8;
        } else {
            cls8 = class$java$lang$Float;
        }
        map8.put(cls16, cls8);
        primitiveWrapperMap.put(Void.TYPE, Void.TYPE);
        wrapperPrimitiveMap = new HashMap();
        for (Class cls17 : primitiveWrapperMap.keySet()) {
            Class cls18 = (Class) primitiveWrapperMap.get(cls17);
            if (!cls17.equals(cls18)) {
                wrapperPrimitiveMap.put(cls18, cls17);
            }
        }
        abbreviationMap = new HashMap();
        reverseAbbreviationMap = new HashMap();
        addAbbreviation("int", "I");
        addAbbreviation("boolean", "Z");
        addAbbreviation("float", "F");
        addAbbreviation("long", "J");
        addAbbreviation("short", "S");
        addAbbreviation("byte", "B");
        addAbbreviation("double", "D");
        addAbbreviation("char", "C");
    }

    private static void addAbbreviation(String str, String str2) {
        abbreviationMap.put(str, str2);
        reverseAbbreviationMap.put(str2, str);
    }

    static /* synthetic */ Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    public static List convertClassNamesToClasses(List list) {
        if (list == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(list.size());
        Iterator it = list.iterator();
        while (it.hasNext()) {
            try {
                arrayList.add(Class.forName((String) it.next()));
            } catch (Exception unused) {
                arrayList.add(null);
            }
        }
        return arrayList;
    }

    public static List convertClassesToClassNames(List list) {
        if (list == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(list.size());
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Class cls = (Class) it.next();
            if (cls == null) {
                arrayList.add(null);
            } else {
                arrayList.add(cls.getName());
            }
        }
        return arrayList;
    }

    public static List getAllInterfaces(Class cls) {
        if (cls == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        while (cls != null) {
            Class<?>[] interfaces = cls.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                if (!arrayList.contains(interfaces[i])) {
                    arrayList.add(interfaces[i]);
                }
                for (Class cls2 : getAllInterfaces(interfaces[i])) {
                    if (!arrayList.contains(cls2)) {
                        arrayList.add(cls2);
                    }
                }
            }
            cls = cls.getSuperclass();
        }
        return arrayList;
    }

    public static List getAllSuperclasses(Class cls) {
        if (cls == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (Class superclass = cls.getSuperclass(); superclass != null; superclass = superclass.getSuperclass()) {
            arrayList.add(superclass);
        }
        return arrayList;
    }

    private static String getCanonicalName(String str) {
        String deleteWhitespace = StringUtils.deleteWhitespace(str);
        if (deleteWhitespace == null) {
            return null;
        }
        int i = 0;
        while (deleteWhitespace.startsWith("[")) {
            i++;
            deleteWhitespace = deleteWhitespace.substring(1);
        }
        if (i < 1) {
            return deleteWhitespace;
        }
        if (deleteWhitespace.startsWith("L")) {
            deleteWhitespace = deleteWhitespace.substring(1, deleteWhitespace.endsWith(";") ? deleteWhitespace.length() - 1 : deleteWhitespace.length());
        } else if (deleteWhitespace.length() > 0) {
            deleteWhitespace = (String) reverseAbbreviationMap.get(deleteWhitespace.substring(0, 1));
        }
        StringBuffer stringBuffer = new StringBuffer(deleteWhitespace);
        for (int i2 = 0; i2 < i; i2++) {
            stringBuffer.append("[]");
        }
        return stringBuffer.toString();
    }

    public static Class getClass(ClassLoader classLoader, String str) throws ClassNotFoundException {
        return getClass(classLoader, str, true);
    }

    public static Class getClass(ClassLoader classLoader, String str, boolean z) throws ClassNotFoundException {
        if (!abbreviationMap.containsKey(str)) {
            return Class.forName(toCanonicalName(str), z, classLoader);
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[");
        stringBuffer.append(abbreviationMap.get(str));
        return Class.forName(stringBuffer.toString(), z, classLoader).getComponentType();
    }

    public static Class getClass(String str) throws ClassNotFoundException {
        return getClass(str, true);
    }

    public static Class getClass(String str, boolean z) throws ClassNotFoundException {
        Class cls;
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader == null) {
            if (class$org$apache$commons$lang$ClassUtils == null) {
                cls = class$("org.apache.commons.lang.ClassUtils");
                class$org$apache$commons$lang$ClassUtils = cls;
            } else {
                cls = class$org$apache$commons$lang$ClassUtils;
            }
            contextClassLoader = cls.getClassLoader();
        }
        return getClass(contextClassLoader, str, z);
    }

    public static String getPackageCanonicalName(Class cls) {
        return cls == null ? "" : getPackageCanonicalName(cls.getName());
    }

    public static String getPackageCanonicalName(Object obj, String str) {
        return obj == null ? str : getPackageCanonicalName(obj.getClass().getName());
    }

    public static String getPackageCanonicalName(String str) {
        return getPackageName(getCanonicalName(str));
    }

    public static String getPackageName(Class cls) {
        return cls == null ? "" : getPackageName(cls.getName());
    }

    public static String getPackageName(Object obj, String str) {
        return obj == null ? str : getPackageName(obj.getClass().getName());
    }

    public static String getPackageName(String str) {
        int lastIndexOf;
        return (str == null || (lastIndexOf = str.lastIndexOf(46)) == -1) ? "" : str.substring(0, lastIndexOf);
    }

    public static Method getPublicMethod(Class cls, String str, Class[] clsArr) throws SecurityException, NoSuchMethodException {
        Method method = cls.getMethod(str, clsArr);
        if (Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
            return method;
        }
        ArrayList<Class> arrayList = new ArrayList();
        arrayList.addAll(getAllInterfaces(cls));
        arrayList.addAll(getAllSuperclasses(cls));
        for (Class cls2 : arrayList) {
            if (Modifier.isPublic(cls2.getModifiers())) {
                try {
                    Method method2 = cls2.getMethod(str, clsArr);
                    if (Modifier.isPublic(method2.getDeclaringClass().getModifiers())) {
                        return method2;
                    }
                } catch (NoSuchMethodException unused) {
                }
            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Can't find a public method for ");
        stringBuffer.append(str);
        stringBuffer.append(SQLBuilder.BLANK);
        stringBuffer.append(ArrayUtils.toString(clsArr));
        throw new NoSuchMethodException(stringBuffer.toString());
    }

    public static String getShortCanonicalName(Class cls) {
        return cls == null ? "" : getShortCanonicalName(cls.getName());
    }

    public static String getShortCanonicalName(Object obj, String str) {
        return obj == null ? str : getShortCanonicalName(obj.getClass().getName());
    }

    public static String getShortCanonicalName(String str) {
        return getShortClassName(getCanonicalName(str));
    }

    public static String getShortClassName(Class cls) {
        return cls == null ? "" : getShortClassName(cls.getName());
    }

    public static String getShortClassName(Object obj, String str) {
        return obj == null ? str : getShortClassName(obj.getClass().getName());
    }

    public static String getShortClassName(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        int lastIndexOf = str.lastIndexOf(46);
        int indexOf = str.indexOf(36, lastIndexOf == -1 ? 0 : lastIndexOf + 1);
        String substring = str.substring(lastIndexOf + 1);
        return indexOf != -1 ? substring.replace('$', PACKAGE_SEPARATOR_CHAR) : substring;
    }

    public static boolean isAssignable(Class cls, Class cls2) {
        if (cls2 == null) {
            return false;
        }
        if (cls == null) {
            return !cls2.isPrimitive();
        }
        if (cls.equals(cls2)) {
            return true;
        }
        if (!cls.isPrimitive()) {
            return cls2.isAssignableFrom(cls);
        }
        if (!cls2.isPrimitive()) {
            return false;
        }
        if (Integer.TYPE.equals(cls)) {
            return Long.TYPE.equals(cls2) || Float.TYPE.equals(cls2) || Double.TYPE.equals(cls2);
        }
        if (Long.TYPE.equals(cls)) {
            return Float.TYPE.equals(cls2) || Double.TYPE.equals(cls2);
        }
        if (Boolean.TYPE.equals(cls) || Double.TYPE.equals(cls)) {
            return false;
        }
        if (Float.TYPE.equals(cls)) {
            return Double.TYPE.equals(cls2);
        }
        if (Character.TYPE.equals(cls)) {
            return Integer.TYPE.equals(cls2) || Long.TYPE.equals(cls2) || Float.TYPE.equals(cls2) || Double.TYPE.equals(cls2);
        }
        if (Short.TYPE.equals(cls)) {
            return Integer.TYPE.equals(cls2) || Long.TYPE.equals(cls2) || Float.TYPE.equals(cls2) || Double.TYPE.equals(cls2);
        }
        if (Byte.TYPE.equals(cls)) {
            return Short.TYPE.equals(cls2) || Integer.TYPE.equals(cls2) || Long.TYPE.equals(cls2) || Float.TYPE.equals(cls2) || Double.TYPE.equals(cls2);
        }
        return false;
    }

    public static boolean isAssignable(Class[] clsArr, Class[] clsArr2) {
        if (!ArrayUtils.isSameLength(clsArr, clsArr2)) {
            return false;
        }
        if (clsArr == null) {
            clsArr = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        if (clsArr2 == null) {
            clsArr2 = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        for (int i = 0; i < clsArr.length; i++) {
            if (!isAssignable(clsArr[i], clsArr2[i])) {
                return false;
            }
        }
        return true;
    }

    public static boolean isInnerClass(Class cls) {
        return cls != null && cls.getName().indexOf(36) >= 0;
    }

    public static Class primitiveToWrapper(Class cls) {
        return (cls == null || !cls.isPrimitive()) ? cls : (Class) primitiveWrapperMap.get(cls);
    }

    public static Class[] primitivesToWrappers(Class[] clsArr) {
        if (clsArr == null) {
            return null;
        }
        if (clsArr.length == 0) {
            return clsArr;
        }
        Class[] clsArr2 = new Class[clsArr.length];
        for (int i = 0; i < clsArr.length; i++) {
            clsArr2[i] = primitiveToWrapper(clsArr[i]);
        }
        return clsArr2;
    }

    private static String toCanonicalName(String str) {
        String deleteWhitespace = StringUtils.deleteWhitespace(str);
        if (deleteWhitespace == null) {
            throw new NullArgumentException("className");
        }
        if (!deleteWhitespace.endsWith("[]")) {
            return deleteWhitespace;
        }
        StringBuffer stringBuffer = new StringBuffer();
        while (deleteWhitespace.endsWith("[]")) {
            deleteWhitespace = deleteWhitespace.substring(0, deleteWhitespace.length() - 2);
            stringBuffer.append("[");
        }
        String str2 = (String) abbreviationMap.get(deleteWhitespace);
        if (str2 != null) {
            stringBuffer.append(str2);
        } else {
            stringBuffer.append("L");
            stringBuffer.append(deleteWhitespace);
            stringBuffer.append(";");
        }
        return stringBuffer.toString();
    }

    public static Class[] toClass(Object[] objArr) {
        if (objArr == null) {
            return null;
        }
        if (objArr.length == 0) {
            return ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        Class[] clsArr = new Class[objArr.length];
        for (int i = 0; i < objArr.length; i++) {
            clsArr[i] = objArr[i].getClass();
        }
        return clsArr;
    }

    public static Class wrapperToPrimitive(Class cls) {
        return (Class) wrapperPrimitiveMap.get(cls);
    }

    public static Class[] wrappersToPrimitives(Class[] clsArr) {
        if (clsArr == null) {
            return null;
        }
        if (clsArr.length == 0) {
            return clsArr;
        }
        Class[] clsArr2 = new Class[clsArr.length];
        for (int i = 0; i < clsArr.length; i++) {
            clsArr2[i] = wrapperToPrimitive(clsArr[i]);
        }
        return clsArr2;
    }
}
