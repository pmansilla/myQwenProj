package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class JavaBeanSerializer extends SerializeFilterable implements ObjectSerializer {
    protected SerializeBeanInfo beanInfo;
    protected final FieldSerializer[] getters;
    private volatile transient long[] hashArray;
    private volatile transient short[] hashArrayMapping;
    protected final FieldSerializer[] sortedGetters;

    public JavaBeanSerializer(SerializeBeanInfo serializeBeanInfo) {
        this.beanInfo = serializeBeanInfo;
        this.sortedGetters = new FieldSerializer[serializeBeanInfo.sortedFields.length];
        for (int i = 0; i < this.sortedGetters.length; i++) {
            this.sortedGetters[i] = new FieldSerializer(serializeBeanInfo.beanType, serializeBeanInfo.sortedFields[i]);
        }
        if (serializeBeanInfo.fields == serializeBeanInfo.sortedFields) {
            this.getters = this.sortedGetters;
            return;
        }
        this.getters = new FieldSerializer[serializeBeanInfo.fields.length];
        for (int i2 = 0; i2 < this.getters.length; i2++) {
            this.getters[i2] = getFieldSerializer(serializeBeanInfo.fields[i2].name);
        }
    }

    public JavaBeanSerializer(Class<?> cls) {
        this(cls, (Map<String, String>) null);
    }

    public JavaBeanSerializer(Class<?> cls, Map<String, String> map) {
        this(TypeUtils.buildBeanInfo(cls, map, null));
    }

    public JavaBeanSerializer(Class<?> cls, String... strArr) {
        this(cls, createAliasMap(strArr));
    }

    static Map<String, String> createAliasMap(String... strArr) {
        HashMap hashMap = new HashMap();
        for (String str : strArr) {
            hashMap.put(str, str);
        }
        return hashMap;
    }

    protected boolean applyLabel(JSONSerializer jSONSerializer, String str) {
        if (jSONSerializer.labelFilters != null) {
            Iterator<LabelFilter> it = jSONSerializer.labelFilters.iterator();
            while (it.hasNext()) {
                if (!it.next().apply(str)) {
                    return false;
                }
            }
        }
        if (this.labelFilters == null) {
            return true;
        }
        Iterator<LabelFilter> it2 = this.labelFilters.iterator();
        while (it2.hasNext()) {
            if (!it2.next().apply(str)) {
                return false;
            }
        }
        return true;
    }

    protected BeanContext getBeanContext(int i) {
        return this.sortedGetters[i].fieldContext;
    }

    public FieldSerializer getFieldSerializer(long j) {
        PropertyNamingStrategy[] propertyNamingStrategyArr;
        int binarySearch;
        if (this.hashArray == null) {
            propertyNamingStrategyArr = PropertyNamingStrategy.values();
            long[] jArr = new long[this.sortedGetters.length * propertyNamingStrategyArr.length];
            int i = 0;
            int i2 = 0;
            while (i < this.sortedGetters.length) {
                String str = this.sortedGetters[i].fieldInfo.name;
                int i3 = i2 + 1;
                jArr[i2] = TypeUtils.fnv1a_64(str);
                for (PropertyNamingStrategy propertyNamingStrategy : propertyNamingStrategyArr) {
                    String translate = propertyNamingStrategy.translate(str);
                    if (!str.equals(translate)) {
                        jArr[i3] = TypeUtils.fnv1a_64(translate);
                        i3++;
                    }
                }
                i++;
                i2 = i3;
            }
            Arrays.sort(jArr, 0, i2);
            this.hashArray = new long[i2];
            System.arraycopy(jArr, 0, this.hashArray, 0, i2);
        } else {
            propertyNamingStrategyArr = null;
        }
        int binarySearch2 = Arrays.binarySearch(this.hashArray, j);
        if (binarySearch2 < 0) {
            return null;
        }
        if (this.hashArrayMapping == null) {
            if (propertyNamingStrategyArr == null) {
                propertyNamingStrategyArr = PropertyNamingStrategy.values();
            }
            short[] sArr = new short[this.hashArray.length];
            Arrays.fill(sArr, (short) -1);
            for (int i4 = 0; i4 < this.sortedGetters.length; i4++) {
                String str2 = this.sortedGetters[i4].fieldInfo.name;
                int binarySearch3 = Arrays.binarySearch(this.hashArray, TypeUtils.fnv1a_64(str2));
                if (binarySearch3 >= 0) {
                    sArr[binarySearch3] = (short) i4;
                }
                for (PropertyNamingStrategy propertyNamingStrategy2 : propertyNamingStrategyArr) {
                    String translate2 = propertyNamingStrategy2.translate(str2);
                    if (!str2.equals(translate2) && (binarySearch = Arrays.binarySearch(this.hashArray, TypeUtils.fnv1a_64(translate2))) >= 0) {
                        sArr[binarySearch] = (short) i4;
                    }
                }
            }
            this.hashArrayMapping = sArr;
        }
        short s = this.hashArrayMapping[binarySearch2];
        if (s != -1) {
            return this.sortedGetters[s];
        }
        return null;
    }

    public FieldSerializer getFieldSerializer(String str) {
        if (str == null) {
            return null;
        }
        int i = 0;
        int length = this.sortedGetters.length - 1;
        while (i <= length) {
            int i2 = (i + length) >>> 1;
            int compareTo = this.sortedGetters[i2].fieldInfo.name.compareTo(str);
            if (compareTo < 0) {
                i = i2 + 1;
            } else {
                if (compareTo <= 0) {
                    return this.sortedGetters[i2];
                }
                length = i2 - 1;
            }
        }
        return null;
    }

    protected Type getFieldType(int i) {
        return this.sortedGetters[i].fieldInfo.fieldType;
    }

    public Object getFieldValue(Object obj, String str) {
        FieldSerializer fieldSerializer = getFieldSerializer(str);
        if (fieldSerializer == null) {
            throw new JSONException("field not found. " + str);
        }
        try {
            return fieldSerializer.getPropertyValue(obj);
        } catch (IllegalAccessException e) {
            throw new JSONException("getFieldValue error." + str, e);
        } catch (InvocationTargetException e2) {
            throw new JSONException("getFieldValue error." + str, e2);
        }
    }

    public Object getFieldValue(Object obj, String str, long j, boolean z) {
        FieldSerializer fieldSerializer = getFieldSerializer(j);
        if (fieldSerializer == null) {
            if (!z) {
                return null;
            }
            throw new JSONException("field not found. " + str);
        }
        try {
            return fieldSerializer.getPropertyValue(obj);
        } catch (IllegalAccessException e) {
            throw new JSONException("getFieldValue error." + str, e);
        } catch (InvocationTargetException e2) {
            throw new JSONException("getFieldValue error." + str, e2);
        }
    }

    public List<Object> getFieldValues(Object obj) throws Exception {
        ArrayList arrayList = new ArrayList(this.sortedGetters.length);
        for (FieldSerializer fieldSerializer : this.sortedGetters) {
            arrayList.add(fieldSerializer.getPropertyValue(obj));
        }
        return arrayList;
    }

    public Map<String, Object> getFieldValuesMap(Object obj) throws Exception {
        LinkedHashMap linkedHashMap = new LinkedHashMap(this.sortedGetters.length);
        for (FieldSerializer fieldSerializer : this.sortedGetters) {
            linkedHashMap.put(fieldSerializer.fieldInfo.name, fieldSerializer.getPropertyValue(obj));
        }
        return linkedHashMap;
    }

    public List<Object> getObjectFieldValues(Object obj) throws Exception {
        ArrayList arrayList = new ArrayList(this.sortedGetters.length);
        for (FieldSerializer fieldSerializer : this.sortedGetters) {
            Class<?> cls = fieldSerializer.fieldInfo.fieldClass;
            if (!cls.isPrimitive() && !cls.getName().startsWith("java.lang.")) {
                arrayList.add(fieldSerializer.getPropertyValue(obj));
            }
        }
        return arrayList;
    }

    public int getSize(Object obj) throws Exception {
        int i = 0;
        for (FieldSerializer fieldSerializer : this.sortedGetters) {
            if (fieldSerializer.getPropertyValueDirect(obj) != null) {
                i++;
            }
        }
        return i;
    }

    public Class<?> getType() {
        return this.beanInfo.beanType;
    }

    protected boolean isWriteAsArray(JSONSerializer jSONSerializer) {
        return isWriteAsArray(jSONSerializer, 0);
    }

    protected boolean isWriteAsArray(JSONSerializer jSONSerializer, int i) {
        int i2 = SerializerFeature.BeanToArray.mask;
        return ((this.beanInfo.features & i2) == 0 && !jSONSerializer.out.beanToArray && (i & i2) == 0) ? false : true;
    }

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        write(jSONSerializer, obj, obj2, type, i, false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:305:0x0116, code lost:
    
        r0 = true;
     */
    /* JADX WARN: Removed duplicated region for block: B:234:0x03e5  */
    /* JADX WARN: Removed duplicated region for block: B:236:0x03e9 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:237:0x03c0 A[Catch: all -> 0x0429, Exception -> 0x042d, TryCatch #6 {Exception -> 0x042d, all -> 0x0429, blocks: (B:51:0x00e6, B:54:0x00ee, B:59:0x03e9, B:62:0x0103, B:64:0x0109, B:68:0x0117, B:70:0x011d, B:72:0x0127, B:295:0x0132, B:300:0x013b, B:303:0x03e8, B:76:0x0143, B:81:0x014e, B:84:0x015a, B:85:0x0162, B:87:0x0183, B:89:0x018b, B:92:0x019c, B:94:0x01a7, B:96:0x01ab, B:101:0x01b2, B:103:0x01b5, B:105:0x01ba, B:108:0x01c4, B:110:0x01cf, B:112:0x01d3, B:115:0x01da, B:117:0x01dd, B:120:0x01e6, B:122:0x01ee, B:124:0x01f9, B:126:0x01fd, B:129:0x0204, B:131:0x0207, B:133:0x020c, B:134:0x0211, B:136:0x0219, B:138:0x0224, B:140:0x0228, B:143:0x022f, B:145:0x0232, B:147:0x0237, B:149:0x023e, B:151:0x0242, B:155:0x0250, B:157:0x0254, B:159:0x025d, B:161:0x0268, B:163:0x026e, B:165:0x0272, B:168:0x027d, B:170:0x0281, B:172:0x0285, B:175:0x0290, B:177:0x0294, B:179:0x0298, B:182:0x02a3, B:184:0x02a7, B:186:0x02ab, B:189:0x02b9, B:191:0x02bd, B:193:0x02c1, B:196:0x02ce, B:198:0x02d2, B:200:0x02d6, B:203:0x02e4, B:205:0x02e8, B:207:0x02ec, B:211:0x02f8, B:213:0x02fc, B:215:0x0300, B:217:0x030d, B:219:0x0318, B:223:0x0321, B:224:0x0327, B:226:0x03ae, B:228:0x03b2, B:230:0x03b6, B:237:0x03c0, B:239:0x03c8, B:240:0x03d0, B:242:0x03d6, B:256:0x0332, B:257:0x0335, B:259:0x033b, B:262:0x0341, B:264:0x0353, B:267:0x035d, B:270:0x0367, B:272:0x0370, B:275:0x037a, B:276:0x037e, B:277:0x0384, B:279:0x038b, B:280:0x038f, B:281:0x0393, B:283:0x0397, B:285:0x039b, B:289:0x03a7, B:290:0x03ab, B:291:0x034b, B:330:0x0407, B:332:0x040f, B:334:0x0417, B:336:0x041f), top: B:50:0x00e6 }] */
    /* JADX WARN: Removed duplicated region for block: B:312:0x0455 A[Catch: all -> 0x048d, TryCatch #2 {all -> 0x048d, blocks: (B:323:0x0434, B:312:0x0455, B:313:0x0469, B:315:0x046f, B:316:0x0487, B:317:0x048c), top: B:322:0x0434 }] */
    /* JADX WARN: Removed duplicated region for block: B:315:0x046f A[Catch: all -> 0x048d, TryCatch #2 {all -> 0x048d, blocks: (B:323:0x0434, B:312:0x0455, B:313:0x0469, B:315:0x046f, B:316:0x0487, B:317:0x048c), top: B:322:0x0434 }] */
    /* JADX WARN: Removed duplicated region for block: B:322:0x0434 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:329:0x0405  */
    /* JADX WARN: Removed duplicated region for block: B:336:0x041f A[Catch: all -> 0x0429, Exception -> 0x042d, TRY_LEAVE, TryCatch #6 {Exception -> 0x042d, all -> 0x0429, blocks: (B:51:0x00e6, B:54:0x00ee, B:59:0x03e9, B:62:0x0103, B:64:0x0109, B:68:0x0117, B:70:0x011d, B:72:0x0127, B:295:0x0132, B:300:0x013b, B:303:0x03e8, B:76:0x0143, B:81:0x014e, B:84:0x015a, B:85:0x0162, B:87:0x0183, B:89:0x018b, B:92:0x019c, B:94:0x01a7, B:96:0x01ab, B:101:0x01b2, B:103:0x01b5, B:105:0x01ba, B:108:0x01c4, B:110:0x01cf, B:112:0x01d3, B:115:0x01da, B:117:0x01dd, B:120:0x01e6, B:122:0x01ee, B:124:0x01f9, B:126:0x01fd, B:129:0x0204, B:131:0x0207, B:133:0x020c, B:134:0x0211, B:136:0x0219, B:138:0x0224, B:140:0x0228, B:143:0x022f, B:145:0x0232, B:147:0x0237, B:149:0x023e, B:151:0x0242, B:155:0x0250, B:157:0x0254, B:159:0x025d, B:161:0x0268, B:163:0x026e, B:165:0x0272, B:168:0x027d, B:170:0x0281, B:172:0x0285, B:175:0x0290, B:177:0x0294, B:179:0x0298, B:182:0x02a3, B:184:0x02a7, B:186:0x02ab, B:189:0x02b9, B:191:0x02bd, B:193:0x02c1, B:196:0x02ce, B:198:0x02d2, B:200:0x02d6, B:203:0x02e4, B:205:0x02e8, B:207:0x02ec, B:211:0x02f8, B:213:0x02fc, B:215:0x0300, B:217:0x030d, B:219:0x0318, B:223:0x0321, B:224:0x0327, B:226:0x03ae, B:228:0x03b2, B:230:0x03b6, B:237:0x03c0, B:239:0x03c8, B:240:0x03d0, B:242:0x03d6, B:256:0x0332, B:257:0x0335, B:259:0x033b, B:262:0x0341, B:264:0x0353, B:267:0x035d, B:270:0x0367, B:272:0x0370, B:275:0x037a, B:276:0x037e, B:277:0x0384, B:279:0x038b, B:280:0x038f, B:281:0x0393, B:283:0x0397, B:285:0x039b, B:289:0x03a7, B:290:0x03ab, B:291:0x034b, B:330:0x0407, B:332:0x040f, B:334:0x0417, B:336:0x041f), top: B:50:0x00e6 }] */
    /* JADX WARN: Removed duplicated region for block: B:339:0x0406  */
    /* JADX WARN: Removed duplicated region for block: B:345:0x00c9  */
    /* JADX WARN: Removed duplicated region for block: B:347:0x00b3  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00b0  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00c7  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00dc A[Catch: all -> 0x005e, Exception -> 0x0062, TRY_LEAVE, TryCatch #5 {Exception -> 0x0062, all -> 0x005e, blocks: (B:357:0x005a, B:22:0x0066, B:24:0x0069, B:26:0x0071, B:27:0x0077, B:29:0x0083, B:31:0x008a, B:37:0x00b4, B:39:0x00b8, B:42:0x00c1, B:45:0x00ca, B:46:0x00d9, B:48:0x00dc, B:348:0x0090, B:350:0x0096, B:352:0x009a, B:354:0x00a2), top: B:356:0x005a }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void write(com.alibaba.fastjson.serializer.JSONSerializer r29, java.lang.Object r30, java.lang.Object r31, java.lang.reflect.Type r32, int r33, boolean r34) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 1169
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.JavaBeanSerializer.write(com.alibaba.fastjson.serializer.JSONSerializer, java.lang.Object, java.lang.Object, java.lang.reflect.Type, int, boolean):void");
    }

    protected char writeAfter(JSONSerializer jSONSerializer, Object obj, char c) {
        if (jSONSerializer.afterFilters != null) {
            Iterator<AfterFilter> it = jSONSerializer.afterFilters.iterator();
            while (it.hasNext()) {
                c = it.next().writeAfter(jSONSerializer, obj, c);
            }
        }
        if (this.afterFilters != null) {
            Iterator<AfterFilter> it2 = this.afterFilters.iterator();
            while (it2.hasNext()) {
                c = it2.next().writeAfter(jSONSerializer, obj, c);
            }
        }
        return c;
    }

    public void writeAsArray(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        write(jSONSerializer, obj, obj2, type, i);
    }

    public void writeAsArrayNonContext(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        write(jSONSerializer, obj, obj2, type, i);
    }

    protected char writeBefore(JSONSerializer jSONSerializer, Object obj, char c) {
        if (jSONSerializer.beforeFilters != null) {
            Iterator<BeforeFilter> it = jSONSerializer.beforeFilters.iterator();
            while (it.hasNext()) {
                c = it.next().writeBefore(jSONSerializer, obj, c);
            }
        }
        if (this.beforeFilters != null) {
            Iterator<BeforeFilter> it2 = this.beforeFilters.iterator();
            while (it2.hasNext()) {
                c = it2.next().writeBefore(jSONSerializer, obj, c);
            }
        }
        return c;
    }

    protected void writeClassName(JSONSerializer jSONSerializer, String str, Object obj) {
        if (str == null) {
            str = jSONSerializer.config.typeKey;
        }
        jSONSerializer.out.writeFieldName(str, false);
        String str2 = this.beanInfo.typeName;
        if (str2 == null) {
            Class<?> cls = obj.getClass();
            if (TypeUtils.isProxy(cls)) {
                cls = cls.getSuperclass();
            }
            str2 = cls.getName();
        }
        jSONSerializer.write(str2);
    }

    public void writeDirectNonContext(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        write(jSONSerializer, obj, obj2, type, i);
    }

    public void writeNoneASM(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        write(jSONSerializer, obj, obj2, type, i, false);
    }

    public boolean writeReference(JSONSerializer jSONSerializer, Object obj, int i) {
        SerialContext serialContext = jSONSerializer.context;
        int i2 = SerializerFeature.DisableCircularReferenceDetect.mask;
        if (serialContext == null || (serialContext.features & i2) != 0 || (i & i2) != 0 || jSONSerializer.references == null || !jSONSerializer.references.containsKey(obj)) {
            return false;
        }
        jSONSerializer.writeReference(obj);
        return true;
    }
}
