package com.czw.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class PHPSerializer {
    private static final byte __0 = 48;
    private static final byte __1 = 49;
    private static final byte __C = 67;
    private static final byte __Colon = 58;
    private static final byte __LeftB = 123;
    private static final byte __N = 78;
    private static final byte __O = 79;
    private static final byte __Quote = 34;
    private static final byte __R = 82;
    private static final byte __RightB = 125;
    private static final byte __Semicolon = 59;
    private static final byte __Slash = 92;
    private static final byte __U = 85;
    private static final byte __a = 97;
    private static final byte __b = 98;
    private static final byte __d = 100;
    private static final byte __i = 105;
    private static final byte __r = 114;
    private static final byte __s = 115;
    private static Package[] __packages = Package.getPackages();
    private static final String __NAN = new String("NAN");
    private static final String __INF = new String("INF");
    private static final String __NINF = new String("-INF");

    private PHPSerializer() {
    }

    public static Number cast(Number number, Class cls) {
        return cls == Byte.class ? new Byte(number.byteValue()) : cls == Short.class ? new Short(number.shortValue()) : cls == Integer.class ? new Integer(number.intValue()) : cls == Long.class ? new Long(number.longValue()) : cls == Float.class ? new Float(number.floatValue()) : cls == Double.class ? new Double(number.doubleValue()) : number;
    }

    public static Object cast(Object obj, Class cls) {
        if (obj == null || cls == null || obj.getClass() == cls) {
            return obj;
        }
        if (obj instanceof Number) {
            return cast((Number) obj, cls);
        }
        if ((obj instanceof String) && cls == Character.class) {
            return new Character(((String) obj).charAt(0));
        }
        boolean z = obj instanceof ArrayList;
        return (z && cls.isArray()) ? toArray((ArrayList) obj, cls.getComponentType()) : (z && cls == HashMap.class) ? toHashMap((ArrayList) obj) : obj;
    }

    private static byte[] getBytes(Object obj) {
        try {
            return obj.toString().getBytes("US-ASCII");
        } catch (Exception unused) {
            return obj.toString().getBytes();
        }
    }

    private static byte[] getBytes(Object obj, String str) {
        try {
            return obj.toString().getBytes(str);
        } catch (Exception unused) {
            return obj.toString().getBytes();
        }
    }

    private static Class getClass(String str) {
        try {
            return Class.forName(str);
        } catch (Exception unused) {
            for (int i = 0; i < __packages.length; i++) {
                try {
                    return Class.forName(__packages[i].getName() + "." + str);
                } catch (Exception unused2) {
                }
            }
            return null;
        }
    }

    private static String getClassName(Class cls) {
        return cls.getName().substring(cls.getPackage().getName().length() + 1);
    }

    private static Field getField(Object obj, String str) {
        for (Class<?> cls = obj.getClass(); cls != null; cls = cls.getSuperclass()) {
            try {
                Field declaredField = cls.getDeclaredField(str);
                int modifiers = declaredField.getModifiers();
                if (!Modifier.isFinal(modifiers)) {
                    if (!Modifier.isStatic(modifiers)) {
                        return declaredField;
                    }
                }
                return null;
            } catch (Exception unused) {
            }
        }
        return null;
    }

    private static Field[] getFields(Object obj) {
        ArrayList arrayList = new ArrayList();
        Class<?> cls = obj.getClass();
        while (true) {
            if (cls == null) {
                return (Field[]) arrayList.toArray(new Field[0]);
            }
            Field[] declaredFields = cls.getDeclaredFields();
            for (int i = 0; i < declaredFields.length; i++) {
                int modifiers = declaredFields[i].getModifiers();
                if (!Modifier.isFinal(modifiers) && !Modifier.isStatic(modifiers)) {
                    arrayList.add(declaredFields[i]);
                }
            }
            cls = cls.getSuperclass();
        }
    }

    private static Field[] getFields(Object obj, String[] strArr) {
        if (strArr == null) {
            return getFields(obj);
        }
        ArrayList arrayList = new ArrayList(strArr.length);
        for (String str : strArr) {
            Field field = getField(obj, str);
            if (field != null) {
                arrayList.add(field);
            }
        }
        return (Field[]) arrayList.toArray(new Field[0]);
    }

    private static int getPos(ByteArrayInputStream byteArrayInputStream) {
        try {
            Field declaredField = byteArrayInputStream.getClass().getDeclaredField("pos");
            declaredField.setAccessible(true);
            return declaredField.getInt(byteArrayInputStream);
        } catch (Exception unused) {
            return 0;
        }
    }

    private static String getString(byte[] bArr, String str) {
        try {
            return new String(bArr, str);
        } catch (Exception unused) {
            return new String(bArr);
        }
    }

    public static void main(String[] strArr) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("sssss");
        System.out.println(new String(serialize(arrayList)));
    }

    public static Object newInstance(Class cls) {
        try {
            Constructor constructor = cls.getConstructor(new Class[0]);
            if (Modifier.isPublic(constructor.getModifiers())) {
                return constructor.newInstance(new Object[0]);
            }
        } catch (Exception unused) {
        }
        try {
            Constructor constructor2 = cls.getConstructor(Integer.TYPE);
            if (Modifier.isPublic(constructor2.getModifiers())) {
                return constructor2.newInstance(new Integer(0));
            }
        } catch (Exception unused2) {
        }
        try {
            Constructor constructor3 = cls.getConstructor(Boolean.TYPE);
            if (Modifier.isPublic(constructor3.getModifiers())) {
                return constructor3.newInstance(new Boolean(false));
            }
        } catch (Exception unused3) {
        }
        try {
            Constructor constructor4 = cls.getConstructor(String.class);
            if (Modifier.isPublic(constructor4.getModifiers())) {
                return constructor4.newInstance("");
            }
        } catch (Exception unused4) {
        }
        Field[] fields = cls.getFields();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getType() == cls && Modifier.isStatic(fields[i].getModifiers())) {
                try {
                    return fields[i].get(null);
                } catch (Exception unused5) {
                    continue;
                }
            }
        }
        Method[] methods = cls.getMethods();
        for (int i2 = 0; i2 < methods.length; i2++) {
            if (methods[i2].getReturnType() == cls && Modifier.isStatic(methods[i2].getModifiers())) {
                try {
                    try {
                        try {
                            try {
                                return methods[i2].invoke(null, new Object[0]);
                            } catch (Exception unused6) {
                                return methods[i2].invoke(null, "");
                            }
                        } catch (Exception unused7) {
                            return methods[i2].invoke(null, new Boolean(false));
                        }
                    } catch (Exception unused8) {
                        return methods[i2].invoke(null, new Integer(0));
                    }
                } catch (Exception unused9) {
                    continue;
                }
            }
        }
        return null;
    }

    private static UnSerializeResult readArray(ByteArrayInputStream byteArrayInputStream, HashMap hashMap, int i, HashMap hashMap2, String str) throws IllegalAccessException {
        Object readUnicodeString;
        byteArrayInputStream.skip(1L);
        int parseInt = Integer.parseInt(readNumber(byteArrayInputStream));
        byteArrayInputStream.skip(1L);
        HashMap hashMap3 = new HashMap(parseInt);
        ArrayList arrayList = new ArrayList(parseInt);
        Integer num = new Integer(i);
        hashMap2.put(num, new Boolean(false));
        int pos = getPos(byteArrayInputStream);
        int i2 = i + 1;
        hashMap.put(new Integer(i), hashMap3);
        for (int i3 = 0; i3 < parseInt; i3++) {
            int read = byteArrayInputStream.read();
            if (read == 85) {
                readUnicodeString = readUnicodeString(byteArrayInputStream);
            } else if (read == 105) {
                readUnicodeString = cast(readInteger(byteArrayInputStream), Integer.class);
            } else {
                if (read != 115) {
                    return null;
                }
                readUnicodeString = readString(byteArrayInputStream, str);
            }
            UnSerializeResult unserialize = unserialize(byteArrayInputStream, hashMap, i2, hashMap2, str);
            i2 = unserialize.hv;
            if (arrayList != null) {
                if ((readUnicodeString instanceof Integer) && ((Integer) readUnicodeString).intValue() == i3) {
                    arrayList.add(unserialize.value);
                } else {
                    arrayList = null;
                }
            }
            hashMap3.put(readUnicodeString, unserialize.value);
        }
        if (arrayList != null) {
            hashMap.put(num, arrayList);
            if (((Boolean) hashMap2.get(num)).booleanValue()) {
                int intValue = num.intValue() + 1;
                setPos(byteArrayInputStream, pos);
                i2 = intValue;
                for (int i4 = 0; i4 < parseInt; i4++) {
                    if (byteArrayInputStream.read() != 105) {
                        return null;
                    }
                    int intValue2 = ((Integer) cast(readInteger(byteArrayInputStream), Integer.class)).intValue();
                    UnSerializeResult unserialize2 = unserialize(byteArrayInputStream, hashMap, i2, hashMap2, str);
                    i2 = unserialize2.hv;
                    arrayList.set(intValue2, unserialize2.value);
                }
            }
        }
        hashMap2.remove(num);
        byteArrayInputStream.skip(1L);
        return new UnSerializeResult(hashMap.get(num), i2);
    }

    private static Boolean readBoolean(ByteArrayInputStream byteArrayInputStream) {
        byteArrayInputStream.skip(1L);
        Boolean bool = new Boolean(byteArrayInputStream.read() == 49);
        byteArrayInputStream.skip(1L);
        return bool;
    }

    private static UnSerializeResult readCustomObject(ByteArrayInputStream byteArrayInputStream, HashMap hashMap, int i, String str) {
        byteArrayInputStream.skip(1L);
        int parseInt = Integer.parseInt(readNumber(byteArrayInputStream));
        byteArrayInputStream.skip(1L);
        byte[] bArr = new byte[parseInt];
        byteArrayInputStream.read(bArr, 0, parseInt);
        String string = getString(bArr, str);
        byteArrayInputStream.skip(2L);
        int parseInt2 = Integer.parseInt(readNumber(byteArrayInputStream));
        byteArrayInputStream.skip(1L);
        Class cls = getClass(string);
        Object newInstance = cls != null ? newInstance(cls) : null;
        int i2 = i + 1;
        hashMap.put(new Integer(i), newInstance);
        if (newInstance == null) {
            byteArrayInputStream.skip(parseInt2);
        } else if (newInstance instanceof Serializable) {
            byte[] bArr2 = new byte[parseInt2];
            byteArrayInputStream.read(bArr2, 0, parseInt2);
            ((Serializable) newInstance).unserialize(bArr2);
        } else {
            byteArrayInputStream.skip(parseInt2);
        }
        byteArrayInputStream.skip(1L);
        return new UnSerializeResult(newInstance, i2);
    }

    private static Number readDouble(ByteArrayInputStream byteArrayInputStream) {
        byteArrayInputStream.skip(1L);
        String readNumber = readNumber(byteArrayInputStream);
        if (readNumber.equals(__NAN)) {
            return new Double(Double.NaN);
        }
        if (readNumber.equals(__INF)) {
            return new Double(Double.POSITIVE_INFINITY);
        }
        if (readNumber.equals(__NINF)) {
            return new Double(Double.NEGATIVE_INFINITY);
        }
        try {
            try {
                return new Long(readNumber);
            } catch (Exception unused) {
                Float f = new Float(readNumber);
                return f.isInfinite() ? new Double(readNumber) : f;
            }
        } catch (Exception unused2) {
            return new Float(0.0f);
        }
    }

    private static Number readInteger(ByteArrayInputStream byteArrayInputStream) {
        byteArrayInputStream.skip(1L);
        String readNumber = readNumber(byteArrayInputStream);
        try {
            try {
                return new Byte(readNumber);
            } catch (Exception unused) {
                return new Short(readNumber);
            }
        } catch (Exception unused2) {
            return new Integer(readNumber);
        }
    }

    private static Object readNull(ByteArrayInputStream byteArrayInputStream) {
        byteArrayInputStream.skip(1L);
        return null;
    }

    private static String readNumber(ByteArrayInputStream byteArrayInputStream) {
        StringBuffer stringBuffer = new StringBuffer();
        int read = byteArrayInputStream.read();
        while (read != 59 && read != 58) {
            stringBuffer.append((char) read);
            read = byteArrayInputStream.read();
        }
        return stringBuffer.toString();
    }

    private static UnSerializeResult readObject(ByteArrayInputStream byteArrayInputStream, HashMap hashMap, int i, HashMap hashMap2, String str) throws IllegalAccessException {
        Object hashMap3;
        String readUnicodeString;
        byteArrayInputStream.skip(1L);
        int parseInt = Integer.parseInt(readNumber(byteArrayInputStream));
        byteArrayInputStream.skip(1L);
        byte[] bArr = new byte[parseInt];
        byteArrayInputStream.read(bArr, 0, parseInt);
        String string = getString(bArr, str);
        byteArrayInputStream.skip(2L);
        int parseInt2 = Integer.parseInt(readNumber(byteArrayInputStream));
        byteArrayInputStream.skip(1L);
        Class cls = getClass(string);
        if (cls != null) {
            hashMap3 = newInstance(cls);
            if (hashMap3 == null) {
                hashMap3 = new HashMap(parseInt2);
            }
        } else {
            hashMap3 = new HashMap(parseInt2);
        }
        int i2 = i + 1;
        hashMap.put(new Integer(i), hashMap3);
        int i3 = 0;
        while (i3 < parseInt2) {
            int read = byteArrayInputStream.read();
            if (read == 85) {
                readUnicodeString = readUnicodeString(byteArrayInputStream);
            } else {
                if (read != 115) {
                    return null;
                }
                readUnicodeString = readString(byteArrayInputStream, str);
            }
            if (readUnicodeString.charAt(0) == 0) {
                readUnicodeString = readUnicodeString.substring(readUnicodeString.indexOf("\u0000", 1) + 1);
            }
            UnSerializeResult unserialize = unserialize(byteArrayInputStream, hashMap, i2, hashMap2, str);
            int i4 = unserialize.hv;
            if (hashMap3 instanceof HashMap) {
                ((HashMap) hashMap3).put(readUnicodeString, unserialize.value);
            } else {
                Field field = getField(hashMap3, readUnicodeString);
                field.setAccessible(true);
                field.set(hashMap3, unserialize.value);
            }
            i3++;
            i2 = i4;
        }
        byteArrayInputStream.skip(1L);
        try {
            hashMap3.getClass().getMethod("__wakeup", new Class[0]).invoke(hashMap3, new Object[0]);
        } catch (Exception unused) {
        }
        return new UnSerializeResult(hashMap3, i2);
    }

    private static UnSerializeResult readPointRef(ByteArrayInputStream byteArrayInputStream, HashMap hashMap, int i, HashMap hashMap2) {
        byteArrayInputStream.skip(1L);
        Integer num = new Integer(readNumber(byteArrayInputStream));
        if (hashMap2.containsKey(num)) {
            hashMap2.put(num, new Boolean(true));
        }
        return new UnSerializeResult(hashMap.get(num), i);
    }

    private static UnSerializeResult readRef(ByteArrayInputStream byteArrayInputStream, HashMap hashMap, int i, HashMap hashMap2) {
        byteArrayInputStream.skip(1L);
        Integer num = new Integer(readNumber(byteArrayInputStream));
        if (hashMap2.containsKey(num)) {
            hashMap2.put(num, new Boolean(true));
        }
        Object obj = hashMap.get(num);
        hashMap.put(new Integer(i), obj);
        return new UnSerializeResult(obj, i + 1);
    }

    private static String readString(ByteArrayInputStream byteArrayInputStream, String str) {
        byteArrayInputStream.skip(1L);
        int parseInt = Integer.parseInt(readNumber(byteArrayInputStream));
        byteArrayInputStream.skip(1L);
        byte[] bArr = new byte[parseInt];
        byteArrayInputStream.read(bArr, 0, parseInt);
        String string = getString(bArr, str);
        byteArrayInputStream.skip(2L);
        return string;
    }

    private static String readUnicodeString(ByteArrayInputStream byteArrayInputStream) {
        byteArrayInputStream.skip(1L);
        int parseInt = Integer.parseInt(readNumber(byteArrayInputStream));
        byteArrayInputStream.skip(1L);
        StringBuffer stringBuffer = new StringBuffer(parseInt);
        for (int i = 0; i < parseInt; i++) {
            int read = byteArrayInputStream.read();
            if (read == 92) {
                stringBuffer.append((char) Integer.parseInt(new String(new char[]{(char) byteArrayInputStream.read(), (char) byteArrayInputStream.read(), (char) byteArrayInputStream.read(), (char) byteArrayInputStream.read()}), 16));
            } else {
                stringBuffer.append((char) read);
            }
        }
        byteArrayInputStream.skip(2L);
        return stringBuffer.toString();
    }

    public static int serialize(ByteArrayOutputStream byteArrayOutputStream, Object obj, HashMap hashMap, int i, String str) {
        if (obj == null) {
            int i2 = i + 1;
            writeNull(byteArrayOutputStream);
            return i2;
        }
        if (obj instanceof Boolean) {
            int i3 = i + 1;
            writeBoolean(byteArrayOutputStream, ((Boolean) obj).booleanValue() ? __1 : __0);
            return i3;
        }
        if ((obj instanceof Byte) || (obj instanceof Short) || (obj instanceof Integer)) {
            int i4 = i + 1;
            writeInteger(byteArrayOutputStream, getBytes(obj));
            return i4;
        }
        if (obj instanceof Long) {
            int i5 = i + 1;
            writeDouble(byteArrayOutputStream, getBytes(obj));
            return i5;
        }
        if (obj instanceof Float) {
            int i6 = i + 1;
            Float f = (Float) obj;
            if (f.isNaN()) {
                writeDouble(byteArrayOutputStream, getBytes(__NAN));
                return i6;
            }
            if (!f.isInfinite()) {
                writeDouble(byteArrayOutputStream, getBytes(f));
                return i6;
            }
            if (f.floatValue() > 0.0f) {
                writeDouble(byteArrayOutputStream, getBytes(__INF));
                return i6;
            }
            writeDouble(byteArrayOutputStream, getBytes(__NINF));
            return i6;
        }
        if (obj instanceof Double) {
            int i7 = i + 1;
            Double d = (Double) obj;
            if (d.isNaN()) {
                writeDouble(byteArrayOutputStream, getBytes(__NAN));
                return i7;
            }
            if (!d.isInfinite()) {
                writeDouble(byteArrayOutputStream, getBytes(d));
                return i7;
            }
            if (d.doubleValue() > 0.0d) {
                writeDouble(byteArrayOutputStream, getBytes(__INF));
                return i7;
            }
            writeDouble(byteArrayOutputStream, getBytes(__NINF));
            return i7;
        }
        if ((obj instanceof Character) || (obj instanceof String)) {
            int i8 = i + 1;
            writeString(byteArrayOutputStream, getBytes(obj, str));
            return i8;
        }
        if (obj.getClass().isArray()) {
            if (hashMap.containsKey(new Integer(obj.hashCode()))) {
                writePointRef(byteArrayOutputStream, getBytes(hashMap.get(new Integer(obj.hashCode()))));
                return i;
            }
            hashMap.put(new Integer(obj.hashCode()), new Integer(i));
            return writeArray(byteArrayOutputStream, obj, hashMap, i + 1, str);
        }
        if (obj instanceof ArrayList) {
            if (hashMap.containsKey(new Integer(obj.hashCode()))) {
                writePointRef(byteArrayOutputStream, getBytes(hashMap.get(new Integer(obj.hashCode()))));
                return i;
            }
            hashMap.put(new Integer(obj.hashCode()), new Integer(i));
            return writeArrayList(byteArrayOutputStream, (ArrayList) obj, hashMap, i + 1, str);
        }
        if (obj instanceof HashMap) {
            if (hashMap.containsKey(new Integer(obj.hashCode()))) {
                writePointRef(byteArrayOutputStream, getBytes(hashMap.get(new Integer(obj.hashCode()))));
                return i;
            }
            hashMap.put(new Integer(obj.hashCode()), new Integer(i));
            return writeHashMap(byteArrayOutputStream, (HashMap) obj, hashMap, i + 1, str);
        }
        if (!hashMap.containsKey(new Integer(obj.hashCode()))) {
            hashMap.put(new Integer(obj.hashCode()), new Integer(i));
            return writeObject(byteArrayOutputStream, obj, hashMap, i + 1, str);
        }
        int i9 = i + 1;
        writeRef(byteArrayOutputStream, getBytes(hashMap.get(new Integer(obj.hashCode()))));
        return i9;
    }

    public static byte[] serialize(Object obj) {
        return serialize(obj, "UTF-8");
    }

    public static byte[] serialize(Object obj, String str) {
        HashMap hashMap = new HashMap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        serialize(byteArrayOutputStream, obj, hashMap, 1, str);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        try {
            byteArrayOutputStream.close();
        } catch (Exception unused) {
        }
        return byteArray;
    }

    private static void setPos(ByteArrayInputStream byteArrayInputStream, int i) {
        try {
            Field declaredField = byteArrayInputStream.getClass().getDeclaredField("pos");
            declaredField.setAccessible(true);
            declaredField.setInt(byteArrayInputStream, i);
        } catch (Exception unused) {
        }
    }

    private static Object toArray(ArrayList arrayList, Class cls) {
        int size = arrayList.size();
        Object newInstance = Array.newInstance((Class<?>) cls, size);
        for (int i = 0; i < size; i++) {
            Array.set(newInstance, i, cast(arrayList.get(i), cls));
        }
        return newInstance;
    }

    private static HashMap toHashMap(ArrayList arrayList) {
        int size = arrayList.size();
        HashMap hashMap = new HashMap(size);
        for (int i = 0; i < size; i++) {
            hashMap.put(new Integer(i), arrayList.get(i));
        }
        return hashMap;
    }

    private static UnSerializeResult unserialize(ByteArrayInputStream byteArrayInputStream, HashMap hashMap, int i, HashMap hashMap2, String str) throws IllegalAccessException {
        switch (byteArrayInputStream.read()) {
            case 67:
                return readCustomObject(byteArrayInputStream, hashMap, i, str);
            case 78:
                Object readNull = readNull(byteArrayInputStream);
                hashMap.put(new Integer(i), readNull);
                return new UnSerializeResult(readNull, i + 1);
            case 79:
                return readObject(byteArrayInputStream, hashMap, i, hashMap2, str);
            case 82:
                return readPointRef(byteArrayInputStream, hashMap, i, hashMap2);
            case 85:
                String readUnicodeString = readUnicodeString(byteArrayInputStream);
                hashMap.put(new Integer(i), readUnicodeString);
                return new UnSerializeResult(readUnicodeString, i + 1);
            case 97:
                return readArray(byteArrayInputStream, hashMap, i, hashMap2, str);
            case 98:
                Boolean readBoolean = readBoolean(byteArrayInputStream);
                hashMap.put(new Integer(i), readBoolean);
                return new UnSerializeResult(readBoolean, i + 1);
            case 100:
                Number readDouble = readDouble(byteArrayInputStream);
                hashMap.put(new Integer(i), readDouble);
                return new UnSerializeResult(readDouble, i + 1);
            case 105:
                Number readInteger = readInteger(byteArrayInputStream);
                hashMap.put(new Integer(i), readInteger);
                return new UnSerializeResult(readInteger, i + 1);
            case 114:
                return readRef(byteArrayInputStream, hashMap, i, hashMap2);
            case 115:
                String readString = readString(byteArrayInputStream, str);
                hashMap.put(new Integer(i), readString);
                return new UnSerializeResult(readString, i + 1);
            default:
                return null;
        }
    }

    public static Object unserialize(byte[] bArr) throws IllegalAccessException {
        return unserialize(bArr, null, "UTF-8");
    }

    public static Object unserialize(byte[] bArr, Class cls) throws IllegalAccessException {
        return unserialize(bArr, cls, "UTF-8");
    }

    public static Object unserialize(byte[] bArr, Class cls, String str) throws IllegalAccessException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        Object obj = unserialize(byteArrayInputStream, new HashMap(), 1, new HashMap(), str).value;
        try {
            byteArrayInputStream.close();
        } catch (Exception unused) {
        }
        return cast(obj, cls);
    }

    public static Object unserialize(byte[] bArr, String str) throws IllegalAccessException {
        return unserialize(bArr, null, str);
    }

    private static int writeArray(ByteArrayOutputStream byteArrayOutputStream, Object obj, HashMap hashMap, int i, String str) {
        int length = Array.getLength(obj);
        byte[] bytes = getBytes(new Integer(length));
        byteArrayOutputStream.write(97);
        byteArrayOutputStream.write(58);
        byteArrayOutputStream.write(bytes, 0, bytes.length);
        byteArrayOutputStream.write(58);
        byteArrayOutputStream.write(123);
        for (int i2 = 0; i2 < length; i2++) {
            writeInteger(byteArrayOutputStream, getBytes(new Integer(i2)));
            i = serialize(byteArrayOutputStream, Array.get(obj, i2), hashMap, i, str);
        }
        byteArrayOutputStream.write(125);
        return i;
    }

    private static int writeArrayList(ByteArrayOutputStream byteArrayOutputStream, ArrayList arrayList, HashMap hashMap, int i, String str) {
        int size = arrayList.size();
        byte[] bytes = getBytes(new Integer(size));
        byteArrayOutputStream.write(97);
        byteArrayOutputStream.write(58);
        byteArrayOutputStream.write(bytes, 0, bytes.length);
        byteArrayOutputStream.write(58);
        byteArrayOutputStream.write(123);
        for (int i2 = 0; i2 < size; i2++) {
            writeInteger(byteArrayOutputStream, getBytes(new Integer(i2)));
            i = serialize(byteArrayOutputStream, arrayList.get(i2), hashMap, i, str);
        }
        byteArrayOutputStream.write(125);
        return i;
    }

    private static void writeBoolean(ByteArrayOutputStream byteArrayOutputStream, byte b) {
        byteArrayOutputStream.write(98);
        byteArrayOutputStream.write(58);
        byteArrayOutputStream.write(b);
        byteArrayOutputStream.write(59);
    }

    private static void writeDouble(ByteArrayOutputStream byteArrayOutputStream, byte[] bArr) {
        byteArrayOutputStream.write(100);
        byteArrayOutputStream.write(58);
        byteArrayOutputStream.write(bArr, 0, bArr.length);
        byteArrayOutputStream.write(59);
    }

    private static int writeHashMap(ByteArrayOutputStream byteArrayOutputStream, HashMap hashMap, HashMap hashMap2, int i, String str) {
        byte[] bytes = getBytes(new Integer(hashMap.size()));
        byteArrayOutputStream.write(97);
        byteArrayOutputStream.write(58);
        byteArrayOutputStream.write(bytes, 0, bytes.length);
        byteArrayOutputStream.write(58);
        byteArrayOutputStream.write(123);
        for (Object obj : hashMap.keySet()) {
            if ((obj instanceof Byte) || (obj instanceof Short) || (obj instanceof Integer)) {
                writeInteger(byteArrayOutputStream, getBytes(obj));
            } else if (obj instanceof Boolean) {
                byte[] bArr = new byte[1];
                bArr[0] = ((Boolean) obj).booleanValue() ? __1 : __0;
                writeInteger(byteArrayOutputStream, bArr);
            } else {
                writeString(byteArrayOutputStream, getBytes(obj, str));
            }
            i = serialize(byteArrayOutputStream, hashMap.get(obj), hashMap2, i, str);
        }
        byteArrayOutputStream.write(125);
        return i;
    }

    private static void writeInteger(ByteArrayOutputStream byteArrayOutputStream, byte[] bArr) {
        byteArrayOutputStream.write(105);
        byteArrayOutputStream.write(58);
        byteArrayOutputStream.write(bArr, 0, bArr.length);
        byteArrayOutputStream.write(59);
    }

    private static void writeNull(ByteArrayOutputStream byteArrayOutputStream) {
        byteArrayOutputStream.write(78);
        byteArrayOutputStream.write(59);
    }

    private static int writeObject(ByteArrayOutputStream byteArrayOutputStream, Object obj, HashMap hashMap, int i, String str) {
        Method method;
        String[] strArr;
        Field[] fields;
        Object obj2;
        Class<?> cls = obj.getClass();
        if (obj instanceof java.io.Serializable) {
            byte[] bytes = getBytes(getClassName(cls), str);
            byte[] bytes2 = getBytes(new Integer(bytes.length));
            if (obj instanceof Serializable) {
                byte[] serialize = ((Serializable) obj).serialize();
                byte[] bytes3 = getBytes(new Integer(serialize.length));
                byteArrayOutputStream.write(67);
                byteArrayOutputStream.write(58);
                byteArrayOutputStream.write(bytes2, 0, bytes2.length);
                byteArrayOutputStream.write(58);
                byteArrayOutputStream.write(34);
                byteArrayOutputStream.write(bytes, 0, bytes.length);
                byteArrayOutputStream.write(34);
                byteArrayOutputStream.write(58);
                byteArrayOutputStream.write(bytes3, 0, bytes3.length);
                byteArrayOutputStream.write(58);
                byteArrayOutputStream.write(123);
                byteArrayOutputStream.write(serialize, 0, serialize.length);
                byteArrayOutputStream.write(125);
            } else {
                try {
                    method = cls.getMethod("__sleep", new Class[0]);
                } catch (Exception unused) {
                    method = null;
                }
                if (method != null) {
                    try {
                        method.setAccessible(true);
                        strArr = (String[]) method.invoke(obj, new Object[0]);
                    } catch (Exception unused2) {
                        strArr = null;
                    }
                    fields = getFields(obj, strArr);
                } else {
                    fields = getFields(obj);
                }
                AccessibleObject.setAccessible(fields, true);
                byte[] bytes4 = getBytes(new Integer(fields.length));
                byteArrayOutputStream.write(79);
                byteArrayOutputStream.write(58);
                byteArrayOutputStream.write(bytes2, 0, bytes2.length);
                byteArrayOutputStream.write(58);
                byteArrayOutputStream.write(34);
                byteArrayOutputStream.write(bytes, 0, bytes.length);
                byteArrayOutputStream.write(34);
                byteArrayOutputStream.write(58);
                byteArrayOutputStream.write(bytes4, 0, bytes4.length);
                byteArrayOutputStream.write(58);
                byteArrayOutputStream.write(123);
                int length = fields.length;
                for (int i2 = 0; i2 < length; i2++) {
                    int modifiers = fields[i2].getModifiers();
                    if (Modifier.isPublic(modifiers)) {
                        writeString(byteArrayOutputStream, getBytes(fields[i2].getName(), str));
                    } else if (Modifier.isProtected(modifiers)) {
                        writeString(byteArrayOutputStream, getBytes("\u0000*\u0000" + fields[i2].getName(), str));
                    } else {
                        writeString(byteArrayOutputStream, getBytes("\u0000" + getClassName(fields[i2].getDeclaringClass()) + "\u0000" + fields[i2].getName(), str));
                    }
                    try {
                        obj2 = fields[i2].get(obj);
                    } catch (Exception unused3) {
                        obj2 = null;
                    }
                    i = serialize(byteArrayOutputStream, obj2, hashMap, i, str);
                }
                byteArrayOutputStream.write(125);
            }
        } else {
            writeNull(byteArrayOutputStream);
        }
        return i;
    }

    private static void writePointRef(ByteArrayOutputStream byteArrayOutputStream, byte[] bArr) {
        byteArrayOutputStream.write(82);
        byteArrayOutputStream.write(58);
        byteArrayOutputStream.write(bArr, 0, bArr.length);
        byteArrayOutputStream.write(59);
    }

    private static void writeRef(ByteArrayOutputStream byteArrayOutputStream, byte[] bArr) {
        byteArrayOutputStream.write(114);
        byteArrayOutputStream.write(58);
        byteArrayOutputStream.write(bArr, 0, bArr.length);
        byteArrayOutputStream.write(59);
    }

    private static void writeString(ByteArrayOutputStream byteArrayOutputStream, byte[] bArr) {
        byte[] bytes = getBytes(new Integer(bArr.length));
        byteArrayOutputStream.write(115);
        byteArrayOutputStream.write(58);
        byteArrayOutputStream.write(bytes, 0, bytes.length);
        byteArrayOutputStream.write(58);
        byteArrayOutputStream.write(34);
        byteArrayOutputStream.write(bArr, 0, bArr.length);
        byteArrayOutputStream.write(34);
        byteArrayOutputStream.write(59);
    }
}
