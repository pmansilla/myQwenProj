package com.mob.tools.utils;

import com.mob.tools.MobLog;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.text.Typography;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class Hashon {

    /* loaded from: classes2.dex */
    public interface TabulateAdapter {
        Object tabulate();
    }

    private ArrayList<?> arrayToList(Object obj) {
        int i = 0;
        if (obj instanceof byte[]) {
            ArrayList<?> arrayList = new ArrayList<>();
            byte[] bArr = (byte[]) obj;
            int length = bArr.length;
            while (i < length) {
                arrayList.add(Byte.valueOf(bArr[i]));
                i++;
            }
            return arrayList;
        }
        if (obj instanceof short[]) {
            ArrayList<?> arrayList2 = new ArrayList<>();
            short[] sArr = (short[]) obj;
            int length2 = sArr.length;
            while (i < length2) {
                arrayList2.add(Short.valueOf(sArr[i]));
                i++;
            }
            return arrayList2;
        }
        if (obj instanceof int[]) {
            ArrayList<?> arrayList3 = new ArrayList<>();
            int[] iArr = (int[]) obj;
            int length3 = iArr.length;
            while (i < length3) {
                arrayList3.add(Integer.valueOf(iArr[i]));
                i++;
            }
            return arrayList3;
        }
        if (obj instanceof long[]) {
            ArrayList<?> arrayList4 = new ArrayList<>();
            long[] jArr = (long[]) obj;
            int length4 = jArr.length;
            while (i < length4) {
                arrayList4.add(Long.valueOf(jArr[i]));
                i++;
            }
            return arrayList4;
        }
        if (obj instanceof float[]) {
            ArrayList<?> arrayList5 = new ArrayList<>();
            float[] fArr = (float[]) obj;
            int length5 = fArr.length;
            while (i < length5) {
                arrayList5.add(Float.valueOf(fArr[i]));
                i++;
            }
            return arrayList5;
        }
        if (obj instanceof double[]) {
            ArrayList<?> arrayList6 = new ArrayList<>();
            double[] dArr = (double[]) obj;
            int length6 = dArr.length;
            while (i < length6) {
                arrayList6.add(Double.valueOf(dArr[i]));
                i++;
            }
            return arrayList6;
        }
        if (obj instanceof char[]) {
            ArrayList<?> arrayList7 = new ArrayList<>();
            char[] cArr = (char[]) obj;
            int length7 = cArr.length;
            while (i < length7) {
                arrayList7.add(Character.valueOf(cArr[i]));
                i++;
            }
            return arrayList7;
        }
        if (!(obj instanceof boolean[])) {
            if (obj instanceof String[]) {
                return new ArrayList<>(Arrays.asList((String[]) obj));
            }
            return null;
        }
        ArrayList<?> arrayList8 = new ArrayList<>();
        boolean[] zArr = (boolean[]) obj;
        int length8 = zArr.length;
        while (i < length8) {
            arrayList8.add(Boolean.valueOf(zArr[i]));
            i++;
        }
        return arrayList8;
    }

    private String format(String str, ArrayList<Object> arrayList) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        String str2 = str + "\t";
        Iterator<Object> it = arrayList.iterator();
        int i = 0;
        while (it.hasNext()) {
            Object next = it.next();
            if (i > 0) {
                sb.append(",\n");
            }
            sb.append(str2);
            if (next instanceof HashMap) {
                sb.append(format(str2, (HashMap<String, Object>) next));
            } else if (next instanceof ArrayList) {
                sb.append(format(str2, (ArrayList<Object>) next));
            } else if (next instanceof String) {
                sb.append(Typography.quote);
                sb.append(next);
                sb.append(Typography.quote);
            } else {
                sb.append(next);
            }
            i++;
        }
        sb.append('\n');
        sb.append(str);
        sb.append(']');
        return sb.toString();
    }

    private String format(String str, HashMap<String, Object> hashMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        String str2 = str + "\t";
        int i = 0;
        for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
            if (i > 0) {
                sb.append(",\n");
            }
            sb.append(str2);
            sb.append(Typography.quote);
            sb.append(entry.getKey());
            sb.append("\":");
            Object value = entry.getValue();
            if (value instanceof HashMap) {
                sb.append(format(str2, (HashMap<String, Object>) value));
            } else if (value instanceof ArrayList) {
                sb.append(format(str2, (ArrayList<Object>) value));
            } else if (value instanceof String) {
                sb.append(Typography.quote);
                sb.append(value);
                sb.append(Typography.quote);
            } else {
                sb.append(value);
            }
            i++;
        }
        sb.append('\n');
        sb.append(str);
        sb.append('}');
        return sb.toString();
    }

    private ArrayList<Object> fromJson(JSONArray jSONArray) throws Throwable {
        ArrayList<Object> arrayList = new ArrayList<>();
        int length = jSONArray.length();
        for (int i = 0; i < length; i++) {
            Object opt = jSONArray.opt(i);
            if (opt instanceof JSONObject) {
                opt = fromJson((JSONObject) opt);
            } else if (opt instanceof JSONArray) {
                opt = fromJson((JSONArray) opt);
            }
            arrayList.add(opt);
        }
        return arrayList;
    }

    private <T> HashMap<String, T> fromJson(JSONObject jSONObject) throws Throwable {
        LinkedHashMap linkedHashMap = (HashMap<String, T>) new HashMap();
        Iterator<String> keys = jSONObject.keys();
        while (keys.hasNext()) {
            String next = keys.next();
            Object opt = jSONObject.opt(next);
            if (JSONObject.NULL.equals(opt)) {
                opt = null;
            }
            if (opt != null) {
                if (opt instanceof JSONObject) {
                    opt = fromJson((JSONObject) opt);
                } else if (opt instanceof JSONArray) {
                    opt = fromJson((JSONArray) opt);
                }
                linkedHashMap.put(next, opt);
            }
        }
        return linkedHashMap;
    }

    private JSONArray getJSONArray(ArrayList<Object> arrayList) throws Throwable {
        JSONArray jSONArray = new JSONArray();
        Iterator<Object> it = arrayList.iterator();
        while (it.hasNext()) {
            Object next = it.next();
            if (next instanceof HashMap) {
                next = getJSONObject((HashMap) next);
            } else if (next instanceof ArrayList) {
                next = getJSONArray((ArrayList) next);
            }
            jSONArray.put(next);
        }
        return jSONArray;
    }

    private <T> JSONObject getJSONObject(HashMap<String, T> hashMap) throws Throwable {
        JSONObject jSONObject = new JSONObject();
        for (Map.Entry<String, T> entry : hashMap.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof HashMap) {
                value = getJSONObject((HashMap) value);
            } else if (value instanceof ArrayList) {
                value = getJSONArray((ArrayList) value);
            } else if (isBasicArray(value)) {
                value = getJSONArray(arrayToList(value));
            }
            jSONObject.put(entry.getKey(), value);
        }
        return jSONObject;
    }

    private boolean isBasicArray(Object obj) {
        return (obj instanceof byte[]) || (obj instanceof short[]) || (obj instanceof int[]) || (obj instanceof long[]) || (obj instanceof float[]) || (obj instanceof double[]) || (obj instanceof char[]) || (obj instanceof boolean[]) || (obj instanceof String[]);
    }

    private Object tabulate(Object obj) throws Throwable {
        if (obj == null || obj.getClass().isPrimitive() || (obj instanceof String) || (obj instanceof Number) || (obj instanceof Character) || (obj instanceof Boolean)) {
            return obj;
        }
        if (obj instanceof TabulateAdapter) {
            return tabulate(((TabulateAdapter) obj).tabulate());
        }
        if (obj instanceof Enum) {
            HashMap hashMap = new HashMap();
            hashMap.put("enum", ((Enum) obj).name());
            return hashMap;
        }
        if (obj.getClass().isArray()) {
            ArrayList arrayList = new ArrayList();
            int length = Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                arrayList.add(tabulate(Array.get(obj, i)));
            }
            return arrayList;
        }
        if (obj instanceof Collection) {
            ArrayList arrayList2 = new ArrayList();
            Iterator it = ((Collection) obj).iterator();
            while (it.hasNext()) {
                arrayList2.add(tabulate(it.next()));
            }
            return arrayList2;
        }
        if (obj instanceof Map) {
            HashMap hashMap2 = new HashMap();
            for (Map.Entry entry : ((Map) obj).entrySet()) {
                Object key = entry.getKey();
                if (key instanceof String) {
                    hashMap2.put((String) key, tabulate(entry.getValue()));
                }
            }
            return hashMap2;
        }
        ArrayList arrayList3 = new ArrayList();
        for (Class<?> cls = obj.getClass(); !cls.equals(Object.class); cls = cls.getSuperclass()) {
            arrayList3.add(0, cls);
        }
        ArrayList arrayList4 = new ArrayList();
        Iterator it2 = arrayList3.iterator();
        while (it2.hasNext()) {
            for (Field field : ((Class) it2.next()).getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers()) && !field.getName().contains("$")) {
                    arrayList4.add(field);
                }
            }
        }
        HashMap hashMap3 = new HashMap();
        Iterator it3 = arrayList4.iterator();
        while (it3.hasNext()) {
            Field field2 = (Field) it3.next();
            field2.setAccessible(true);
            hashMap3.put(field2.getName(), tabulate(field2.get(obj)));
        }
        return hashMap3;
    }

    /* JADX WARN: Type inference failed for: r10v10, types: [T, java.util.Collection] */
    /* JADX WARN: Type inference failed for: r10v8, types: [T, java.util.Map] */
    private <T> T tabulate(Object obj, Class<T> cls, Type[] typeArr) throws Throwable {
        Field field;
        Type type;
        Type type2;
        Object obj2;
        Object obj3;
        int i = 0;
        if (cls.isPrimitive() || Number.class.isAssignableFrom(cls) || cls.equals(Character.class)) {
            return (cls.equals(Boolean.TYPE) || cls.equals(Boolean.class)) ? (T) Boolean.valueOf("true".equals(String.valueOf(obj))) : (cls.equals(Character.TYPE) || cls.equals(Character.class)) ? (T) Character.valueOf(String.valueOf(obj).charAt(0)) : (cls.equals(Byte.TYPE) || cls.equals(Byte.class)) ? (T) Byte.valueOf(String.valueOf(obj)) : (cls.equals(Short.TYPE) || cls.equals(Short.class)) ? (T) Short.valueOf(String.valueOf(obj)) : (cls.equals(Integer.TYPE) || cls.equals(Integer.class)) ? (T) Integer.valueOf(String.valueOf(obj)) : (cls.equals(Long.TYPE) || cls.equals(Long.class)) ? (T) Long.valueOf(String.valueOf(obj)) : (cls.equals(Float.TYPE) || cls.equals(Float.class)) ? (T) Float.valueOf(String.valueOf(obj)) : (T) Double.valueOf(String.valueOf(obj));
        }
        if (TabulateAdapter.class.isAssignableFrom(cls)) {
            try {
                return (T) ReflectHelper.invokeStaticMethod(ReflectHelper.importClass(cls.getName()), "valueOf", obj);
            } catch (Throwable unused) {
                return null;
            }
        }
        if (cls.equals(String.class) || cls.equals(Boolean.class)) {
            return obj;
        }
        if (cls.isEnum()) {
            return (T) Enum.valueOf(cls, String.valueOf(((HashMap) obj).get("enum")));
        }
        if (cls.isArray()) {
            ArrayList arrayList = (ArrayList) obj;
            Class<?> componentType = cls.getComponentType();
            T t = (T) Array.newInstance(componentType, arrayList.size());
            int size = arrayList.size();
            while (i < size) {
                Array.set(t, i, tabulate(arrayList.get(i), componentType, null));
                i++;
            }
            return t;
        }
        if (Collection.class.isAssignableFrom(cls)) {
            ?? r10 = (T) ((Collection) cls.newInstance());
            Type type3 = (typeArr == null || typeArr.length <= 0) ? null : typeArr[0];
            ArrayList arrayList2 = (ArrayList) obj;
            int size2 = arrayList2.size();
            while (i < size2) {
                if (type3 != null && (type3 instanceof Class) && !type3.equals(Object.class)) {
                    r10.add(tabulate(arrayList2.get(i), (Class) type3, null));
                } else if (type3 == null || !(type3 instanceof ParameterizedType)) {
                    r10.add(arrayList2.get(i));
                } else {
                    ParameterizedType parameterizedType = (ParameterizedType) type3;
                    r10.add(tabulate(arrayList2.get(i), (Class) parameterizedType.getRawType(), parameterizedType.getActualTypeArguments()));
                }
                i++;
            }
            return r10;
        }
        if (!Map.class.isAssignableFrom(cls)) {
            ArrayList arrayList3 = new ArrayList();
            for (Class<T> cls2 = cls; !cls2.equals(Object.class); cls2 = cls2.getSuperclass()) {
                arrayList3.add(cls2);
            }
            HashMap hashMap = (HashMap) obj;
            HashMap hashMap2 = new HashMap();
            for (String str : hashMap.keySet()) {
                if (hashMap.get(str) != null) {
                    Iterator it = arrayList3.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            try {
                                field = ((Class) it.next()).getDeclaredField(str);
                            } catch (Throwable unused2) {
                                field = null;
                            }
                            if (field != null) {
                                hashMap2.put(str, field);
                                break;
                            }
                        }
                    }
                }
            }
            T t2 = (T) ReflectHelper.newInstance(ReflectHelper.getName(cls), new Object[0]);
            for (String str2 : hashMap2.keySet()) {
                Object obj4 = hashMap.get(str2);
                Field field2 = (Field) hashMap2.get(str2);
                Class<?> type4 = field2.getType();
                Type genericType = field2.getGenericType();
                Type[] actualTypeArguments = genericType instanceof ParameterizedType ? ((ParameterizedType) genericType).getActualTypeArguments() : null;
                field2.setAccessible(true);
                field2.set(t2, tabulate(obj4, type4, actualTypeArguments));
            }
            return t2;
        }
        ?? r102 = (T) ((Map) cls.newInstance());
        if (typeArr == null || typeArr.length <= 1) {
            type = null;
            type2 = null;
        } else {
            type2 = typeArr[0];
            type = typeArr[1];
        }
        HashMap hashMap3 = (HashMap) obj;
        for (Object obj5 : hashMap3.keySet()) {
            if (type2 != null && (type2 instanceof Class) && !type.equals(Object.class)) {
                obj2 = tabulate(obj5, (Class) type2, null);
            } else if (type2 == null || !(type2 instanceof ParameterizedType)) {
                obj2 = obj5;
            } else {
                ParameterizedType parameterizedType2 = (ParameterizedType) type2;
                obj2 = tabulate(obj5, (Class) parameterizedType2.getRawType(), parameterizedType2.getActualTypeArguments());
            }
            if (type != null && (type instanceof Class) && !type.equals(Object.class)) {
                obj3 = tabulate(hashMap3.get(obj5), (Class) type, null);
            } else if (type == null || !(type instanceof ParameterizedType)) {
                obj3 = hashMap3.get(obj5);
            } else {
                ParameterizedType parameterizedType3 = (ParameterizedType) type;
                obj3 = tabulate(hashMap3.get(obj5), (Class) parameterizedType3.getRawType(), parameterizedType3.getActualTypeArguments());
            }
            r102.put(obj2, obj3);
        }
        return r102;
    }

    public String format(String str) {
        try {
            return format("", fromJson(str));
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return "";
        }
    }

    public <T> String fromHashMap(HashMap<String, T> hashMap) {
        try {
            JSONObject jSONObject = getJSONObject(hashMap);
            return jSONObject == null ? "" : jSONObject.toString();
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return "";
        }
    }

    public <T> T fromJson(String str, Class<T> cls) {
        HashMap<String, T> fromJson = fromJson(str);
        T t = fromJson;
        if (str.startsWith("[")) {
            t = fromJson;
            if (str.endsWith("]")) {
                t = fromJson.get("fakelist");
            }
        }
        try {
            Type genericSuperclass = cls.getGenericSuperclass();
            return (T) tabulate(t, cls, genericSuperclass instanceof ParameterizedType ? ((ParameterizedType) genericSuperclass).getActualTypeArguments() : null);
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return null;
        }
    }

    public <T> HashMap<String, T> fromJson(String str) {
        if (str == null || str.isEmpty()) {
            return new HashMap<>();
        }
        try {
            if (str.startsWith("[") && str.endsWith("]")) {
                str = "{\"fakelist\":" + str + "}";
            }
            return fromJson(new JSONObject(str));
        } catch (Throwable th) {
            MobLog.getInstance().w(str);
            MobLog.getInstance().w(th);
            return new HashMap<>();
        }
    }

    public String fromObject(Object obj) {
        Object obj2;
        try {
            obj2 = tabulate(obj);
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            obj2 = null;
        }
        if (obj2 == null) {
            return "";
        }
        if (!(obj2 instanceof ArrayList)) {
            return fromHashMap((HashMap) obj2);
        }
        HashMap hashMap = new HashMap();
        hashMap.put("list", obj2);
        return fromHashMap(hashMap).substring("{\"list\":".length(), r3.length() - 1).trim();
    }
}
