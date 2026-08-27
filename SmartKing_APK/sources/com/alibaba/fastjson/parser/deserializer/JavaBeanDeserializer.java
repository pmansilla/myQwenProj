package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONLexerBase;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.JavaBeanInfo;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/* loaded from: classes.dex */
public class JavaBeanDeserializer implements ObjectDeserializer {
    private final Map<String, FieldDeserializer> alterNameFieldDeserializers;
    public final JavaBeanInfo beanInfo;
    protected final Class<?> clazz;
    private ConcurrentMap<String, Object> extraFieldDeserializers;
    private final FieldDeserializer[] fieldDeserializers;
    private transient long[] hashArray;
    private transient short[] hashArrayMapping;
    private transient long[] smartMatchHashArray;
    private transient short[] smartMatchHashArrayMapping;
    protected final FieldDeserializer[] sortedFieldDeserializers;

    public JavaBeanDeserializer(ParserConfig parserConfig, JavaBeanInfo javaBeanInfo) {
        this.clazz = javaBeanInfo.clazz;
        this.beanInfo = javaBeanInfo;
        this.sortedFieldDeserializers = new FieldDeserializer[javaBeanInfo.sortedFields.length];
        int length = javaBeanInfo.sortedFields.length;
        HashMap hashMap = null;
        int i = 0;
        while (i < length) {
            FieldInfo fieldInfo = javaBeanInfo.sortedFields[i];
            FieldDeserializer createFieldDeserializer = parserConfig.createFieldDeserializer(parserConfig, javaBeanInfo, fieldInfo);
            this.sortedFieldDeserializers[i] = createFieldDeserializer;
            HashMap hashMap2 = hashMap;
            for (String str : fieldInfo.alternateNames) {
                if (hashMap2 == null) {
                    hashMap2 = new HashMap();
                }
                hashMap2.put(str, createFieldDeserializer);
            }
            i++;
            hashMap = hashMap2;
        }
        this.alterNameFieldDeserializers = hashMap;
        this.fieldDeserializers = new FieldDeserializer[javaBeanInfo.fields.length];
        int length2 = javaBeanInfo.fields.length;
        for (int i2 = 0; i2 < length2; i2++) {
            this.fieldDeserializers[i2] = getFieldDeserializer(javaBeanInfo.fields[i2].name);
        }
    }

    public JavaBeanDeserializer(ParserConfig parserConfig, Class<?> cls) {
        this(parserConfig, cls, cls);
    }

    public JavaBeanDeserializer(ParserConfig parserConfig, Class<?> cls, Type type) {
        this(parserConfig, JavaBeanInfo.build(cls, type, parserConfig.propertyNamingStrategy, parserConfig.fieldBased, parserConfig.compatibleWithJavaBean));
    }

    static boolean isSetFlag(int i, int[] iArr) {
        if (iArr == null) {
            return false;
        }
        int i2 = i / 32;
        int i3 = i % 32;
        if (i2 < iArr.length) {
            if (((1 << i3) & iArr[i2]) != 0) {
                return true;
            }
        }
        return false;
    }

    protected static void parseArray(Collection collection, ObjectDeserializer objectDeserializer, DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        JSONLexerBase jSONLexerBase = (JSONLexerBase) defaultJSONParser.lexer;
        int i = jSONLexerBase.token();
        if (i == 8) {
            jSONLexerBase.nextToken(16);
            jSONLexerBase.token();
            return;
        }
        if (i != 14) {
            defaultJSONParser.throwException(i);
        }
        if (jSONLexerBase.getCurrent() == '[') {
            jSONLexerBase.next();
            jSONLexerBase.setToken(14);
        } else {
            jSONLexerBase.nextToken(14);
        }
        if (jSONLexerBase.token() == 15) {
            jSONLexerBase.nextToken();
            return;
        }
        int i2 = 0;
        while (true) {
            collection.add(objectDeserializer.deserialze(defaultJSONParser, type, Integer.valueOf(i2)));
            i2++;
            if (jSONLexerBase.token() != 16) {
                break;
            }
            if (jSONLexerBase.getCurrent() == '[') {
                jSONLexerBase.next();
                jSONLexerBase.setToken(14);
            } else {
                jSONLexerBase.nextToken(14);
            }
        }
        int i3 = jSONLexerBase.token();
        if (i3 != 15) {
            defaultJSONParser.throwException(i3);
        }
        if (jSONLexerBase.getCurrent() != ',') {
            jSONLexerBase.nextToken(16);
        } else {
            jSONLexerBase.next();
            jSONLexerBase.setToken(16);
        }
    }

    protected void check(JSONLexer jSONLexer, int i) {
        if (jSONLexer.token() != i) {
            throw new JSONException("syntax error");
        }
    }

    public Object createInstance(DefaultJSONParser defaultJSONParser, Type type) {
        Object newInstance;
        if ((type instanceof Class) && this.clazz.isInterface()) {
            return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{(Class) type}, new JSONObject());
        }
        Object obj = null;
        if (this.beanInfo.defaultConstructor == null && this.beanInfo.factoryMethod == null) {
            return null;
        }
        if (this.beanInfo.factoryMethod != null && this.beanInfo.defaultConstructorParameterSize > 0) {
            return null;
        }
        try {
            Constructor<?> constructor = this.beanInfo.defaultConstructor;
            if (this.beanInfo.defaultConstructorParameterSize == 0) {
                newInstance = constructor != null ? constructor.newInstance(new Object[0]) : this.beanInfo.factoryMethod.invoke(null, new Object[0]);
            } else {
                ParseContext context = defaultJSONParser.getContext();
                if (context == null || context.object == null) {
                    throw new JSONException("can't create non-static inner class instance.");
                }
                if (!(type instanceof Class)) {
                    throw new JSONException("can't create non-static inner class instance.");
                }
                String name = ((Class) type).getName();
                String substring = name.substring(0, name.lastIndexOf(36));
                Object obj2 = context.object;
                String name2 = obj2.getClass().getName();
                if (!name2.equals(substring)) {
                    ParseContext parseContext = context.parent;
                    if (parseContext != null && parseContext.object != null && (("java.util.ArrayList".equals(name2) || "java.util.List".equals(name2) || "java.util.Collection".equals(name2) || "java.util.Map".equals(name2) || "java.util.HashMap".equals(name2)) && parseContext.object.getClass().getName().equals(substring))) {
                        obj = parseContext.object;
                    }
                    obj2 = obj;
                }
                if (obj2 == null) {
                    throw new JSONException("can't create non-static inner class instance.");
                }
                newInstance = constructor.newInstance(obj2);
            }
            if (defaultJSONParser != null && defaultJSONParser.lexer.isEnabled(Feature.InitStringFieldAsEmpty)) {
                for (FieldInfo fieldInfo : this.beanInfo.fields) {
                    if (fieldInfo.fieldClass == String.class) {
                        try {
                            fieldInfo.set(newInstance, "");
                        } catch (Exception e) {
                            throw new JSONException("create instance error, class " + this.clazz.getName(), e);
                        }
                    }
                }
            }
            return newInstance;
        } catch (JSONException e2) {
            throw e2;
        } catch (Exception e3) {
            throw new JSONException("create instance error, class " + this.clazz.getName(), e3);
        }
    }

    public Object createInstance(Map<String, Object> map, ParserConfig parserConfig) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Integer num;
        if (this.beanInfo.creatorConstructor == null && this.beanInfo.factoryMethod == null) {
            Object createInstance = createInstance((DefaultJSONParser) null, this.clazz);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                FieldDeserializer smartMatch = smartMatch(key);
                if (smartMatch != null) {
                    FieldInfo fieldInfo = smartMatch.fieldInfo;
                    Type type = fieldInfo.fieldType;
                    String str = fieldInfo.format;
                    smartMatch.setValue(createInstance, (str == null || type != Date.class) ? TypeUtils.cast(value, type, parserConfig) : TypeUtils.castToDate(value, str));
                }
            }
            if (this.beanInfo.buildMethod == null) {
                return createInstance;
            }
            try {
                return this.beanInfo.buildMethod.invoke(createInstance, new Object[0]);
            } catch (Exception e) {
                throw new JSONException("build object error", e);
            }
        }
        FieldInfo[] fieldInfoArr = this.beanInfo.fields;
        int length = fieldInfoArr.length;
        Object[] objArr = new Object[length];
        HashMap hashMap = null;
        for (int i = 0; i < length; i++) {
            FieldInfo fieldInfo2 = fieldInfoArr[i];
            Object obj = map.get(fieldInfo2.name);
            if (obj == null) {
                Class<?> cls = fieldInfo2.fieldClass;
                if (cls == Integer.TYPE) {
                    obj = 0;
                } else if (cls == Long.TYPE) {
                    obj = 0L;
                } else if (cls == Short.TYPE) {
                    obj = (short) 0;
                } else if (cls == Byte.TYPE) {
                    obj = (byte) 0;
                } else if (cls == Float.TYPE) {
                    obj = Float.valueOf(0.0f);
                } else if (cls == Double.TYPE) {
                    obj = Double.valueOf(0.0d);
                } else if (cls == Character.TYPE) {
                    obj = '0';
                } else if (cls == Boolean.TYPE) {
                    obj = false;
                }
                if (hashMap == null) {
                    hashMap = new HashMap();
                }
                hashMap.put(fieldInfo2.name, Integer.valueOf(i));
            }
            objArr[i] = obj;
        }
        if (hashMap != null) {
            for (Map.Entry<String, Object> entry2 : map.entrySet()) {
                String key2 = entry2.getKey();
                Object value2 = entry2.getValue();
                FieldDeserializer smartMatch2 = smartMatch(key2);
                if (smartMatch2 != null && (num = (Integer) hashMap.get(smartMatch2.fieldInfo.name)) != null) {
                    objArr[num.intValue()] = value2;
                }
            }
        }
        if (this.beanInfo.creatorConstructor != null) {
            try {
                return this.beanInfo.creatorConstructor.newInstance(objArr);
            } catch (Exception e2) {
                throw new JSONException("create instance error, " + this.beanInfo.creatorConstructor.toGenericString(), e2);
            }
        }
        if (this.beanInfo.factoryMethod == null) {
            return null;
        }
        try {
            return this.beanInfo.factoryMethod.invoke(null, objArr);
        } catch (Exception e3) {
            throw new JSONException("create factory method error, " + this.beanInfo.factoryMethod.toString(), e3);
        }
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        return (T) deserialze(defaultJSONParser, type, obj, 0);
    }

    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj, int i) {
        return (T) deserialze(defaultJSONParser, type, obj, null, i, null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:121:0x0799, code lost:
    
        r6.object = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:210:0x0458, code lost:
    
        r14 = r6;
        r0 = r18;
        r28 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:212:0x0573, code lost:
    
        if (r1 != null) goto L504;
     */
    /* JADX WARN: Code restructure failed: missing block: B:213:0x0575, code lost:
    
        if (r0 != null) goto L412;
     */
    /* JADX WARN: Code restructure failed: missing block: B:214:0x0577, code lost:
    
        r2 = (T) createInstance(r30, r31);
     */
    /* JADX WARN: Code restructure failed: missing block: B:215:0x057b, code lost:
    
        if (r14 != null) goto L408;
     */
    /* JADX WARN: Code restructure failed: missing block: B:216:0x0587, code lost:
    
        if (r14 == null) goto L410;
     */
    /* JADX WARN: Code restructure failed: missing block: B:217:0x0589, code lost:
    
        r14.object = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:218:0x058b, code lost:
    
        r30.setContext(r15);
     */
    /* JADX WARN: Code restructure failed: missing block: B:219:0x058e, code lost:
    
        return r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:222:0x0581, code lost:
    
        r14 = r30.setContext(r15, r2, r32);
     */
    /* JADX WARN: Code restructure failed: missing block: B:224:0x0583, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:225:0x0584, code lost:
    
        r1 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:226:0x004c, code lost:
    
        r6 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:227:0x058f, code lost:
    
        r2 = r29.beanInfo.creatorConstructorParameters;
     */
    /* JADX WARN: Code restructure failed: missing block: B:228:0x0596, code lost:
    
        if (r2 == null) goto L446;
     */
    /* JADX WARN: Code restructure failed: missing block: B:229:0x0598, code lost:
    
        r10 = new java.lang.Object[r2.length];
        r11 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:231:0x059d, code lost:
    
        if (r11 >= r2.length) goto L588;
     */
    /* JADX WARN: Code restructure failed: missing block: B:232:0x059f, code lost:
    
        r12 = r0.remove(r2[r11]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:233:0x05a5, code lost:
    
        if (r12 != null) goto L590;
     */
    /* JADX WARN: Code restructure failed: missing block: B:234:0x05a7, code lost:
    
        r13 = r29.beanInfo.creatorConstructorParameterTypes[r11];
        r3 = r29.beanInfo.fields[r11];
     */
    /* JADX WARN: Code restructure failed: missing block: B:235:0x05b5, code lost:
    
        if (r13 != java.lang.Byte.TYPE) goto L422;
     */
    /* JADX WARN: Code restructure failed: missing block: B:236:0x05b7, code lost:
    
        r12 = java.lang.Byte.valueOf(r28);
     */
    /* JADX WARN: Code restructure failed: missing block: B:238:0x0601, code lost:
    
        r10[r11] = r12;
        r11 = r11 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:240:0x05be, code lost:
    
        if (r13 != java.lang.Short.TYPE) goto L425;
     */
    /* JADX WARN: Code restructure failed: missing block: B:241:0x05c0, code lost:
    
        r12 = java.lang.Short.valueOf(r28);
     */
    /* JADX WARN: Code restructure failed: missing block: B:244:0x05c7, code lost:
    
        if (r13 != java.lang.Integer.TYPE) goto L428;
     */
    /* JADX WARN: Code restructure failed: missing block: B:245:0x05c9, code lost:
    
        r12 = java.lang.Integer.valueOf(r28);
     */
    /* JADX WARN: Code restructure failed: missing block: B:248:0x05d0, code lost:
    
        if (r13 != java.lang.Long.TYPE) goto L431;
     */
    /* JADX WARN: Code restructure failed: missing block: B:249:0x05d2, code lost:
    
        r12 = 0L;
     */
    /* JADX WARN: Code restructure failed: missing block: B:252:0x05d9, code lost:
    
        if (r13 != java.lang.Float.TYPE) goto L434;
     */
    /* JADX WARN: Code restructure failed: missing block: B:253:0x05db, code lost:
    
        r12 = java.lang.Float.valueOf(0.0f);
     */
    /* JADX WARN: Code restructure failed: missing block: B:256:0x05e2, code lost:
    
        if (r13 != java.lang.Double.TYPE) goto L437;
     */
    /* JADX WARN: Code restructure failed: missing block: B:257:0x05e4, code lost:
    
        r12 = java.lang.Double.valueOf(0.0d);
     */
    /* JADX WARN: Code restructure failed: missing block: B:260:0x05ed, code lost:
    
        if (r13 != java.lang.Boolean.TYPE) goto L440;
     */
    /* JADX WARN: Code restructure failed: missing block: B:261:0x05ef, code lost:
    
        r12 = java.lang.Boolean.FALSE;
     */
    /* JADX WARN: Code restructure failed: missing block: B:264:0x05f4, code lost:
    
        if (r13 != java.lang.String.class) goto L598;
     */
    /* JADX WARN: Code restructure failed: missing block: B:266:0x05fd, code lost:
    
        if ((r3.parserFeatures & com.alibaba.fastjson.parser.Feature.InitStringFieldAsEmpty.mask) == 0) goto L599;
     */
    /* JADX WARN: Code restructure failed: missing block: B:267:0x05ff, code lost:
    
        r12 = "";
     */
    /* JADX WARN: Code restructure failed: missing block: B:274:0x067f, code lost:
    
        if (r29.beanInfo.creatorConstructor == null) goto L496;
     */
    /* JADX WARN: Code restructure failed: missing block: B:276:0x06e5, code lost:
    
        if (r29.beanInfo.factoryMethod == null) goto L503;
     */
    /* JADX WARN: Code restructure failed: missing block: B:277:0x0712, code lost:
    
        r14.object = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:280:0x06f0, code lost:
    
        r1 = (T) r29.beanInfo.factoryMethod.invoke(null, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:282:0x06f2, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:284:0x0711, code lost:
    
        throw new com.alibaba.fastjson.JSONException("create factory method error, " + r29.beanInfo.factoryMethod.toString(), r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:286:0x0681, code lost:
    
        r3 = r29.beanInfo.creatorConstructor.newInstance(r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:287:0x0689, code lost:
    
        if (r2 == null) goto L492;
     */
    /* JADX WARN: Code restructure failed: missing block: B:288:0x06b7, code lost:
    
        r1 = (T) r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:290:0x068b, code lost:
    
        r0 = r0.entrySet().iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:292:0x0697, code lost:
    
        if (r0.hasNext() == false) goto L600;
     */
    /* JADX WARN: Code restructure failed: missing block: B:293:0x0699, code lost:
    
        r1 = r0.next();
        r2 = getFieldDeserializer(r1.getKey());
     */
    /* JADX WARN: Code restructure failed: missing block: B:294:0x06a9, code lost:
    
        if (r2 == null) goto L603;
     */
    /* JADX WARN: Code restructure failed: missing block: B:296:0x06ab, code lost:
    
        r2.setValue(r3, r1.getValue());
     */
    /* JADX WARN: Code restructure failed: missing block: B:302:0x06b3, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:303:0x06b4, code lost:
    
        r1 = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:305:0x06b9, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:307:0x06e0, code lost:
    
        throw new com.alibaba.fastjson.JSONException("create instance error, " + r2 + ", " + r29.beanInfo.creatorConstructor.toGenericString(), r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:308:0x0606, code lost:
    
        r3 = r29.beanInfo.fields;
        r4 = r3.length;
        r10 = new java.lang.Object[r4];
        r11 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x060e, code lost:
    
        if (r11 >= r4) goto L605;
     */
    /* JADX WARN: Code restructure failed: missing block: B:310:0x0610, code lost:
    
        r12 = r3[r11];
        r13 = r0.get(r12.name);
     */
    /* JADX WARN: Code restructure failed: missing block: B:311:0x0618, code lost:
    
        if (r13 != null) goto L453;
     */
    /* JADX WARN: Code restructure failed: missing block: B:312:0x061a, code lost:
    
        r5 = r12.fieldType;
     */
    /* JADX WARN: Code restructure failed: missing block: B:313:0x061e, code lost:
    
        if (r5 != java.lang.Byte.TYPE) goto L455;
     */
    /* JADX WARN: Code restructure failed: missing block: B:314:0x0620, code lost:
    
        r13 = java.lang.Byte.valueOf(r28);
     */
    /* JADX WARN: Code restructure failed: missing block: B:316:0x062a, code lost:
    
        if (r5 != java.lang.Short.TYPE) goto L458;
     */
    /* JADX WARN: Code restructure failed: missing block: B:317:0x062c, code lost:
    
        r13 = java.lang.Short.valueOf(r28);
     */
    /* JADX WARN: Code restructure failed: missing block: B:319:0x0633, code lost:
    
        if (r5 != java.lang.Integer.TYPE) goto L461;
     */
    /* JADX WARN: Code restructure failed: missing block: B:320:0x0635, code lost:
    
        r13 = java.lang.Integer.valueOf(r28);
     */
    /* JADX WARN: Code restructure failed: missing block: B:322:0x063c, code lost:
    
        if (r5 != java.lang.Long.TYPE) goto L464;
     */
    /* JADX WARN: Code restructure failed: missing block: B:323:0x063e, code lost:
    
        r13 = 0L;
     */
    /* JADX WARN: Code restructure failed: missing block: B:325:0x0647, code lost:
    
        if (r5 != java.lang.Float.TYPE) goto L467;
     */
    /* JADX WARN: Code restructure failed: missing block: B:326:0x0649, code lost:
    
        r13 = java.lang.Float.valueOf(0.0f);
     */
    /* JADX WARN: Code restructure failed: missing block: B:329:0x0673, code lost:
    
        r10[r11] = r13;
        r11 = r11 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:331:0x0652, code lost:
    
        if (r5 != java.lang.Double.TYPE) goto L470;
     */
    /* JADX WARN: Code restructure failed: missing block: B:332:0x0654, code lost:
    
        r13 = java.lang.Double.valueOf(0.0d);
     */
    /* JADX WARN: Code restructure failed: missing block: B:335:0x065f, code lost:
    
        if (r5 != java.lang.Boolean.TYPE) goto L473;
     */
    /* JADX WARN: Code restructure failed: missing block: B:336:0x0661, code lost:
    
        r13 = java.lang.Boolean.FALSE;
     */
    /* JADX WARN: Code restructure failed: missing block: B:339:0x0666, code lost:
    
        if (r5 != java.lang.String.class) goto L610;
     */
    /* JADX WARN: Code restructure failed: missing block: B:341:0x066f, code lost:
    
        if ((r12.parserFeatures & com.alibaba.fastjson.parser.Feature.InitStringFieldAsEmpty.mask) == 0) goto L611;
     */
    /* JADX WARN: Code restructure failed: missing block: B:342:0x0671, code lost:
    
        r13 = "";
     */
    /* JADX WARN: Code restructure failed: missing block: B:348:0x0714, code lost:
    
        r0 = r29.beanInfo.buildMethod;
     */
    /* JADX WARN: Code restructure failed: missing block: B:349:0x0718, code lost:
    
        if (r0 != null) goto L510;
     */
    /* JADX WARN: Code restructure failed: missing block: B:350:0x071a, code lost:
    
        if (r14 == null) goto L508;
     */
    /* JADX WARN: Code restructure failed: missing block: B:351:0x071c, code lost:
    
        r14.object = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:352:0x071e, code lost:
    
        r30.setContext(r15);
     */
    /* JADX WARN: Code restructure failed: missing block: B:353:0x0721, code lost:
    
        return (T) r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:356:0x0723, code lost:
    
        r0 = (T) r0.invoke(r1, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:357:0x0729, code lost:
    
        if (r14 == null) goto L514;
     */
    /* JADX WARN: Code restructure failed: missing block: B:358:0x072b, code lost:
    
        r14.object = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:359:0x072d, code lost:
    
        r30.setContext(r15);
     */
    /* JADX WARN: Code restructure failed: missing block: B:360:0x0730, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:362:0x0731, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:364:0x0739, code lost:
    
        throw new com.alibaba.fastjson.JSONException("build object error", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:365:0x073a, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:401:0x0781, code lost:
    
        throw new com.alibaba.fastjson.JSONException("syntax error, unexpect token " + com.alibaba.fastjson.parser.JSONToken.name(r11.token()));
     */
    /* JADX WARN: Code restructure failed: missing block: B:405:0x056f, code lost:
    
        r1 = (T) r17;
        r14 = r20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:594:0x02c7, code lost:
    
        if (r11.matchStat == (-2)) goto L231;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:104:0x02d4 A[Catch: all -> 0x0790, TryCatch #6 {all -> 0x0790, blocks: (B:89:0x012d, B:91:0x0132, B:93:0x0140, B:95:0x0144, B:97:0x0154, B:99:0x015a, B:104:0x02d4, B:106:0x02de, B:459:0x02ea, B:110:0x02f5, B:128:0x0308, B:130:0x0312, B:132:0x031e, B:134:0x03a1, B:136:0x03ac, B:141:0x03bc, B:142:0x03c3, B:143:0x0323, B:145:0x032b, B:147:0x0331, B:148:0x0334, B:150:0x0341, B:153:0x034a, B:155:0x034e, B:157:0x0351, B:159:0x0355, B:160:0x0358, B:161:0x0364, B:163:0x036c, B:164:0x0372, B:166:0x0378, B:168:0x037e, B:170:0x0384, B:173:0x038a, B:174:0x038e, B:177:0x0396, B:178:0x03c4, B:179:0x03de, B:181:0x03e1, B:185:0x03ef, B:187:0x03f8, B:189:0x040b, B:193:0x0414, B:195:0x041c, B:196:0x0432, B:198:0x043a, B:200:0x043e, B:206:0x044d, B:209:0x0455, B:367:0x0471, B:368:0x0478, B:369:0x03ea, B:375:0x0489, B:462:0x0164, B:467:0x016f, B:474:0x017d, B:478:0x0188, B:485:0x0195, B:487:0x0199, B:490:0x01a2, B:495:0x01ac, B:498:0x01b5, B:503:0x01bf, B:506:0x01c8, B:509:0x01ce, B:514:0x01d8, B:519:0x01e2, B:524:0x01ec, B:526:0x01f2, B:529:0x0200, B:531:0x0208, B:533:0x020c, B:536:0x021b, B:542:0x0226, B:545:0x0230, B:550:0x023b, B:553:0x0245, B:558:0x0250, B:561:0x025a, B:564:0x0261, B:567:0x0269, B:570:0x0276, B:573:0x027c, B:576:0x0289, B:579:0x028f, B:582:0x029c, B:585:0x02a2, B:588:0x02af, B:591:0x02b5, B:593:0x02c4), top: B:88:0x012d }] */
    /* JADX WARN: Removed duplicated region for block: B:121:0x0799  */
    /* JADX WARN: Removed duplicated region for block: B:136:0x03ac A[Catch: all -> 0x0790, TRY_LEAVE, TryCatch #6 {all -> 0x0790, blocks: (B:89:0x012d, B:91:0x0132, B:93:0x0140, B:95:0x0144, B:97:0x0154, B:99:0x015a, B:104:0x02d4, B:106:0x02de, B:459:0x02ea, B:110:0x02f5, B:128:0x0308, B:130:0x0312, B:132:0x031e, B:134:0x03a1, B:136:0x03ac, B:141:0x03bc, B:142:0x03c3, B:143:0x0323, B:145:0x032b, B:147:0x0331, B:148:0x0334, B:150:0x0341, B:153:0x034a, B:155:0x034e, B:157:0x0351, B:159:0x0355, B:160:0x0358, B:161:0x0364, B:163:0x036c, B:164:0x0372, B:166:0x0378, B:168:0x037e, B:170:0x0384, B:173:0x038a, B:174:0x038e, B:177:0x0396, B:178:0x03c4, B:179:0x03de, B:181:0x03e1, B:185:0x03ef, B:187:0x03f8, B:189:0x040b, B:193:0x0414, B:195:0x041c, B:196:0x0432, B:198:0x043a, B:200:0x043e, B:206:0x044d, B:209:0x0455, B:367:0x0471, B:368:0x0478, B:369:0x03ea, B:375:0x0489, B:462:0x0164, B:467:0x016f, B:474:0x017d, B:478:0x0188, B:485:0x0195, B:487:0x0199, B:490:0x01a2, B:495:0x01ac, B:498:0x01b5, B:503:0x01bf, B:506:0x01c8, B:509:0x01ce, B:514:0x01d8, B:519:0x01e2, B:524:0x01ec, B:526:0x01f2, B:529:0x0200, B:531:0x0208, B:533:0x020c, B:536:0x021b, B:542:0x0226, B:545:0x0230, B:550:0x023b, B:553:0x0245, B:558:0x0250, B:561:0x025a, B:564:0x0261, B:567:0x0269, B:570:0x0276, B:573:0x027c, B:576:0x0289, B:579:0x028f, B:582:0x029c, B:585:0x02a2, B:588:0x02af, B:591:0x02b5, B:593:0x02c4), top: B:88:0x012d }] */
    /* JADX WARN: Removed duplicated region for block: B:141:0x03bc A[Catch: all -> 0x0790, TRY_ENTER, TryCatch #6 {all -> 0x0790, blocks: (B:89:0x012d, B:91:0x0132, B:93:0x0140, B:95:0x0144, B:97:0x0154, B:99:0x015a, B:104:0x02d4, B:106:0x02de, B:459:0x02ea, B:110:0x02f5, B:128:0x0308, B:130:0x0312, B:132:0x031e, B:134:0x03a1, B:136:0x03ac, B:141:0x03bc, B:142:0x03c3, B:143:0x0323, B:145:0x032b, B:147:0x0331, B:148:0x0334, B:150:0x0341, B:153:0x034a, B:155:0x034e, B:157:0x0351, B:159:0x0355, B:160:0x0358, B:161:0x0364, B:163:0x036c, B:164:0x0372, B:166:0x0378, B:168:0x037e, B:170:0x0384, B:173:0x038a, B:174:0x038e, B:177:0x0396, B:178:0x03c4, B:179:0x03de, B:181:0x03e1, B:185:0x03ef, B:187:0x03f8, B:189:0x040b, B:193:0x0414, B:195:0x041c, B:196:0x0432, B:198:0x043a, B:200:0x043e, B:206:0x044d, B:209:0x0455, B:367:0x0471, B:368:0x0478, B:369:0x03ea, B:375:0x0489, B:462:0x0164, B:467:0x016f, B:474:0x017d, B:478:0x0188, B:485:0x0195, B:487:0x0199, B:490:0x01a2, B:495:0x01ac, B:498:0x01b5, B:503:0x01bf, B:506:0x01c8, B:509:0x01ce, B:514:0x01d8, B:519:0x01e2, B:524:0x01ec, B:526:0x01f2, B:529:0x0200, B:531:0x0208, B:533:0x020c, B:536:0x021b, B:542:0x0226, B:545:0x0230, B:550:0x023b, B:553:0x0245, B:558:0x0250, B:561:0x025a, B:564:0x0261, B:567:0x0269, B:570:0x0276, B:573:0x027c, B:576:0x0289, B:579:0x028f, B:582:0x029c, B:585:0x02a2, B:588:0x02af, B:591:0x02b5, B:593:0x02c4), top: B:88:0x012d }] */
    /* JADX WARN: Removed duplicated region for block: B:187:0x03f8 A[Catch: all -> 0x0790, TryCatch #6 {all -> 0x0790, blocks: (B:89:0x012d, B:91:0x0132, B:93:0x0140, B:95:0x0144, B:97:0x0154, B:99:0x015a, B:104:0x02d4, B:106:0x02de, B:459:0x02ea, B:110:0x02f5, B:128:0x0308, B:130:0x0312, B:132:0x031e, B:134:0x03a1, B:136:0x03ac, B:141:0x03bc, B:142:0x03c3, B:143:0x0323, B:145:0x032b, B:147:0x0331, B:148:0x0334, B:150:0x0341, B:153:0x034a, B:155:0x034e, B:157:0x0351, B:159:0x0355, B:160:0x0358, B:161:0x0364, B:163:0x036c, B:164:0x0372, B:166:0x0378, B:168:0x037e, B:170:0x0384, B:173:0x038a, B:174:0x038e, B:177:0x0396, B:178:0x03c4, B:179:0x03de, B:181:0x03e1, B:185:0x03ef, B:187:0x03f8, B:189:0x040b, B:193:0x0414, B:195:0x041c, B:196:0x0432, B:198:0x043a, B:200:0x043e, B:206:0x044d, B:209:0x0455, B:367:0x0471, B:368:0x0478, B:369:0x03ea, B:375:0x0489, B:462:0x0164, B:467:0x016f, B:474:0x017d, B:478:0x0188, B:485:0x0195, B:487:0x0199, B:490:0x01a2, B:495:0x01ac, B:498:0x01b5, B:503:0x01bf, B:506:0x01c8, B:509:0x01ce, B:514:0x01d8, B:519:0x01e2, B:524:0x01ec, B:526:0x01f2, B:529:0x0200, B:531:0x0208, B:533:0x020c, B:536:0x021b, B:542:0x0226, B:545:0x0230, B:550:0x023b, B:553:0x0245, B:558:0x0250, B:561:0x025a, B:564:0x0261, B:567:0x0269, B:570:0x0276, B:573:0x027c, B:576:0x0289, B:579:0x028f, B:582:0x029c, B:585:0x02a2, B:588:0x02af, B:591:0x02b5, B:593:0x02c4), top: B:88:0x012d }] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0071 A[Catch: all -> 0x0049, TRY_LEAVE, TryCatch #1 {all -> 0x0049, blocks: (B:17:0x0039, B:19:0x003e, B:25:0x0054, B:27:0x005f, B:29:0x0067, B:34:0x0071, B:41:0x0080, B:46:0x008c, B:48:0x0096, B:51:0x009d, B:53:0x00a3, B:55:0x00ae, B:58:0x00b8, B:68:0x00cb, B:70:0x00d3, B:73:0x00dd, B:75:0x00fe, B:76:0x0106, B:77:0x0119, B:80:0x00c6, B:85:0x011f), top: B:15:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:366:0x0471 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:374:0x0487 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:382:0x04c0  */
    /* JADX WARN: Removed duplicated region for block: B:389:0x0565  */
    /* JADX WARN: Removed duplicated region for block: B:392:0x0566 A[Catch: all -> 0x078a, TryCatch #2 {all -> 0x078a, blocks: (B:116:0x0752, B:387:0x055d, B:392:0x0566, B:404:0x056c, B:395:0x073f, B:397:0x0747, B:400:0x0763, B:401:0x0781, B:435:0x053e, B:437:0x0544, B:441:0x054a, B:442:0x0555, B:445:0x0782, B:446:0x0789), top: B:115:0x0752 }] */
    /* JADX WARN: Removed duplicated region for block: B:434:0x0521  */
    /* JADX WARN: Removed duplicated region for block: B:461:0x047c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected <T> T deserialze(com.alibaba.fastjson.parser.DefaultJSONParser r30, java.lang.reflect.Type r31, java.lang.Object r32, java.lang.Object r33, int r34, int[] r35) {
        /*
            Method dump skipped, instructions count: 1956
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.deserialze(com.alibaba.fastjson.parser.DefaultJSONParser, java.lang.reflect.Type, java.lang.Object, java.lang.Object, int, int[]):java.lang.Object");
    }

    public <T> T deserialzeArrayMapping(DefaultJSONParser defaultJSONParser, Type type, Object obj, Object obj2) {
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        if (jSONLexer.token() != 14) {
            throw new JSONException("error");
        }
        T t = (T) createInstance(defaultJSONParser, type);
        int i = 0;
        int length = this.sortedFieldDeserializers.length;
        while (true) {
            if (i >= length) {
                break;
            }
            char c = i == length + (-1) ? ']' : ',';
            FieldDeserializer fieldDeserializer = this.sortedFieldDeserializers[i];
            Class<?> cls = fieldDeserializer.fieldInfo.fieldClass;
            if (cls == Integer.TYPE) {
                fieldDeserializer.setValue((Object) t, jSONLexer.scanInt(c));
            } else if (cls == String.class) {
                fieldDeserializer.setValue((Object) t, jSONLexer.scanString(c));
            } else if (cls == Long.TYPE) {
                fieldDeserializer.setValue(t, jSONLexer.scanLong(c));
            } else if (cls.isEnum()) {
                char current = jSONLexer.getCurrent();
                fieldDeserializer.setValue(t, (current == '\"' || current == 'n') ? jSONLexer.scanEnum(cls, defaultJSONParser.getSymbolTable(), c) : (current < '0' || current > '9') ? scanEnum(jSONLexer, c) : ((EnumDeserializer) ((DefaultFieldDeserializer) fieldDeserializer).getFieldValueDeserilizer(defaultJSONParser.getConfig())).valueOf(jSONLexer.scanInt(c)));
            } else if (cls == Boolean.TYPE) {
                fieldDeserializer.setValue(t, jSONLexer.scanBoolean(c));
            } else if (cls == Float.TYPE) {
                fieldDeserializer.setValue(t, Float.valueOf(jSONLexer.scanFloat(c)));
            } else if (cls == Double.TYPE) {
                fieldDeserializer.setValue(t, Double.valueOf(jSONLexer.scanDouble(c)));
            } else if (cls == Date.class && jSONLexer.getCurrent() == '1') {
                fieldDeserializer.setValue(t, new Date(jSONLexer.scanLong(c)));
            } else if (cls == BigDecimal.class) {
                fieldDeserializer.setValue(t, jSONLexer.scanDecimal(c));
            } else {
                jSONLexer.nextToken(14);
                fieldDeserializer.setValue(t, defaultJSONParser.parseObject(fieldDeserializer.fieldInfo.fieldType, fieldDeserializer.fieldInfo.name));
                if (jSONLexer.token() == 15) {
                    break;
                }
                check(jSONLexer, c == ']' ? 15 : 16);
            }
            i++;
        }
        jSONLexer.nextToken(16);
        return t;
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public int getFastMatchToken() {
        return 12;
    }

    public FieldDeserializer getFieldDeserializer(long j) {
        if (this.hashArray == null) {
            long[] jArr = new long[this.sortedFieldDeserializers.length];
            for (int i = 0; i < this.sortedFieldDeserializers.length; i++) {
                jArr[i] = TypeUtils.fnv1a_64(this.sortedFieldDeserializers[i].fieldInfo.name);
            }
            Arrays.sort(jArr);
            this.hashArray = jArr;
        }
        int binarySearch = Arrays.binarySearch(this.hashArray, j);
        if (binarySearch < 0) {
            return null;
        }
        if (this.hashArrayMapping == null) {
            short[] sArr = new short[this.hashArray.length];
            Arrays.fill(sArr, (short) -1);
            for (int i2 = 0; i2 < this.sortedFieldDeserializers.length; i2++) {
                int binarySearch2 = Arrays.binarySearch(this.hashArray, TypeUtils.fnv1a_64(this.sortedFieldDeserializers[i2].fieldInfo.name));
                if (binarySearch2 >= 0) {
                    sArr[binarySearch2] = (short) i2;
                }
            }
            this.hashArrayMapping = sArr;
        }
        short s = this.hashArrayMapping[binarySearch];
        if (s != -1) {
            return this.sortedFieldDeserializers[s];
        }
        return null;
    }

    public FieldDeserializer getFieldDeserializer(String str) {
        return getFieldDeserializer(str, null);
    }

    public FieldDeserializer getFieldDeserializer(String str, int[] iArr) {
        if (str == null) {
            return null;
        }
        int i = 0;
        int length = this.sortedFieldDeserializers.length - 1;
        while (i <= length) {
            int i2 = (i + length) >>> 1;
            int compareTo = this.sortedFieldDeserializers[i2].fieldInfo.name.compareTo(str);
            if (compareTo < 0) {
                i = i2 + 1;
            } else {
                if (compareTo <= 0) {
                    if (isSetFlag(i2, iArr)) {
                        return null;
                    }
                    return this.sortedFieldDeserializers[i2];
                }
                length = i2 - 1;
            }
        }
        if (this.alterNameFieldDeserializers != null) {
            return this.alterNameFieldDeserializers.get(str);
        }
        return null;
    }

    public Type getFieldType(int i) {
        return this.sortedFieldDeserializers[i].fieldInfo.fieldType;
    }

    protected JavaBeanDeserializer getSeeAlso(ParserConfig parserConfig, JavaBeanInfo javaBeanInfo, String str) {
        if (javaBeanInfo.jsonType == null) {
            return null;
        }
        for (Class<?> cls : javaBeanInfo.jsonType.seeAlso()) {
            ObjectDeserializer deserializer = parserConfig.getDeserializer(cls);
            if (deserializer instanceof JavaBeanDeserializer) {
                JavaBeanDeserializer javaBeanDeserializer = (JavaBeanDeserializer) deserializer;
                JavaBeanInfo javaBeanInfo2 = javaBeanDeserializer.beanInfo;
                if (javaBeanInfo2.typeName.equals(str)) {
                    return javaBeanDeserializer;
                }
                JavaBeanDeserializer seeAlso = getSeeAlso(parserConfig, javaBeanInfo2, str);
                if (seeAlso != null) {
                    return seeAlso;
                }
            }
        }
        return null;
    }

    public boolean parseField(DefaultJSONParser defaultJSONParser, String str, Object obj, Type type, Map<String, Object> map) {
        return parseField(defaultJSONParser, str, obj, type, map, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:105:0x01b5  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00d8  */
    /* JADX WARN: Type inference failed for: r17v0 */
    /* JADX WARN: Type inference failed for: r17v1, types: [boolean] */
    /* JADX WARN: Type inference failed for: r17v3 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean parseField(com.alibaba.fastjson.parser.DefaultJSONParser r22, java.lang.String r23, java.lang.Object r24, java.lang.reflect.Type r25, java.util.Map<java.lang.String, java.lang.Object> r26, int[] r27) {
        /*
            Method dump skipped, instructions count: 492
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.parseField(com.alibaba.fastjson.parser.DefaultJSONParser, java.lang.String, java.lang.Object, java.lang.reflect.Type, java.util.Map, int[]):boolean");
    }

    protected Object parseRest(DefaultJSONParser defaultJSONParser, Type type, Object obj, Object obj2, int i) {
        return parseRest(defaultJSONParser, type, obj, obj2, i, new int[0]);
    }

    protected Object parseRest(DefaultJSONParser defaultJSONParser, Type type, Object obj, Object obj2, int i, int[] iArr) {
        return deserialze(defaultJSONParser, type, obj, obj2, i, iArr);
    }

    protected Enum<?> scanEnum(JSONLexer jSONLexer, char c) {
        throw new JSONException("illegal enum. " + jSONLexer.info());
    }

    protected Enum scanEnum(JSONLexerBase jSONLexerBase, char[] cArr, ObjectDeserializer objectDeserializer) {
        EnumDeserializer enumDeserializer = objectDeserializer instanceof EnumDeserializer ? (EnumDeserializer) objectDeserializer : null;
        if (enumDeserializer == null) {
            jSONLexerBase.matchStat = -1;
            return null;
        }
        long scanFieldSymbol = jSONLexerBase.scanFieldSymbol(cArr);
        if (jSONLexerBase.matchStat > 0) {
            return enumDeserializer.getEnumByHashCode(scanFieldSymbol);
        }
        return null;
    }

    public FieldDeserializer smartMatch(String str) {
        return smartMatch(str, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x0099  */
    /* JADX WARN: Removed duplicated region for block: B:54:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.alibaba.fastjson.parser.deserializer.FieldDeserializer smartMatch(java.lang.String r10, int[] r11) {
        /*
            r9 = this;
            r0 = 0
            if (r10 != 0) goto L4
            return r0
        L4:
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer r1 = r9.getFieldDeserializer(r10, r11)
            if (r1 != 0) goto Lb3
            long r2 = com.alibaba.fastjson.util.TypeUtils.fnv1a_64_lower(r10)
            long[] r4 = r9.smartMatchHashArray
            r5 = 0
            if (r4 != 0) goto L34
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer[] r4 = r9.sortedFieldDeserializers
            int r4 = r4.length
            long[] r4 = new long[r4]
            r6 = 0
        L19:
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer[] r7 = r9.sortedFieldDeserializers
            int r7 = r7.length
            if (r6 >= r7) goto L2f
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer[] r7 = r9.sortedFieldDeserializers
            r7 = r7[r6]
            com.alibaba.fastjson.util.FieldInfo r7 = r7.fieldInfo
            java.lang.String r7 = r7.name
            long r7 = com.alibaba.fastjson.util.TypeUtils.fnv1a_64_lower(r7)
            r4[r6] = r7
            int r6 = r6 + 1
            goto L19
        L2f:
            java.util.Arrays.sort(r4)
            r9.smartMatchHashArray = r4
        L34:
            long[] r4 = r9.smartMatchHashArray
            int r2 = java.util.Arrays.binarySearch(r4, r2)
            if (r2 >= 0) goto L54
            java.lang.String r3 = "is"
            boolean r3 = r10.startsWith(r3)
            if (r3 == 0) goto L55
            r2 = 2
            java.lang.String r10 = r10.substring(r2)
            long r6 = com.alibaba.fastjson.util.TypeUtils.fnv1a_64_lower(r10)
            long[] r10 = r9.smartMatchHashArray
            int r2 = java.util.Arrays.binarySearch(r10, r6)
            goto L55
        L54:
            r3 = 0
        L55:
            if (r2 < 0) goto L96
            short[] r10 = r9.smartMatchHashArrayMapping
            r4 = -1
            if (r10 != 0) goto L85
            long[] r10 = r9.smartMatchHashArray
            int r10 = r10.length
            short[] r10 = new short[r10]
            java.util.Arrays.fill(r10, r4)
        L64:
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer[] r6 = r9.sortedFieldDeserializers
            int r6 = r6.length
            if (r5 >= r6) goto L83
            long[] r6 = r9.smartMatchHashArray
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer[] r7 = r9.sortedFieldDeserializers
            r7 = r7[r5]
            com.alibaba.fastjson.util.FieldInfo r7 = r7.fieldInfo
            java.lang.String r7 = r7.name
            long r7 = com.alibaba.fastjson.util.TypeUtils.fnv1a_64_lower(r7)
            int r6 = java.util.Arrays.binarySearch(r6, r7)
            if (r6 < 0) goto L80
            short r7 = (short) r5
            r10[r6] = r7
        L80:
            int r5 = r5 + 1
            goto L64
        L83:
            r9.smartMatchHashArrayMapping = r10
        L85:
            short[] r10 = r9.smartMatchHashArrayMapping
            short r10 = r10[r2]
            if (r10 == r4) goto L96
            boolean r11 = isSetFlag(r10, r11)
            if (r11 != 0) goto L96
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer[] r11 = r9.sortedFieldDeserializers
            r10 = r11[r10]
            goto L97
        L96:
            r10 = r1
        L97:
            if (r10 == 0) goto Lb4
            com.alibaba.fastjson.util.FieldInfo r11 = r10.fieldInfo
            int r1 = r11.parserFeatures
            com.alibaba.fastjson.parser.Feature r2 = com.alibaba.fastjson.parser.Feature.DisableFieldSmartMatch
            int r2 = r2.mask
            r1 = r1 & r2
            if (r1 == 0) goto La5
            return r0
        La5:
            java.lang.Class<?> r11 = r11.fieldClass
            if (r3 == 0) goto Lb4
            java.lang.Class r1 = java.lang.Boolean.TYPE
            if (r11 == r1) goto Lb4
            java.lang.Class<java.lang.Boolean> r1 = java.lang.Boolean.class
            if (r11 == r1) goto Lb4
            r10 = r0
            goto Lb4
        Lb3:
            r10 = r1
        Lb4:
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.smartMatch(java.lang.String, int[]):com.alibaba.fastjson.parser.deserializer.FieldDeserializer");
    }
}
