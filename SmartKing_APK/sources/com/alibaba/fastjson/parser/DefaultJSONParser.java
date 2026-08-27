package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.JSONPathException;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessable;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.parser.deserializer.ExtraTypeProvider;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.FieldTypeResolver;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.ResolveFieldDeserializer;
import com.alibaba.fastjson.serializer.BeanContext;
import com.alibaba.fastjson.serializer.IntegerCodec;
import com.alibaba.fastjson.serializer.LongCodec;
import com.alibaba.fastjson.serializer.StringCodec;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.Closeable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/* loaded from: classes.dex */
public class DefaultJSONParser implements Closeable {
    public static final int NONE = 0;
    public static final int NeedToResolve = 1;
    public static final int TypeNameRedirect = 2;
    private static final Set<Class<?>> primitiveClasses = new HashSet();
    private String[] autoTypeAccept;
    private boolean autoTypeEnable;
    protected ParserConfig config;
    protected ParseContext context;
    private ParseContext[] contextArray;
    private int contextArrayIndex;
    private DateFormat dateFormat;
    private String dateFormatPattern;
    private List<ExtraProcessor> extraProcessors;
    private List<ExtraTypeProvider> extraTypeProviders;
    protected FieldTypeResolver fieldTypeResolver;
    public final Object input;
    protected transient BeanContext lastBeanContext;
    public final JSONLexer lexer;
    public int resolveStatus;
    private List<ResolveTask> resolveTaskList;
    public final SymbolTable symbolTable;

    /* loaded from: classes.dex */
    public static class ResolveTask {
        public final ParseContext context;
        public FieldDeserializer fieldDeserializer;
        public ParseContext ownerContext;
        public final String referenceValue;

        public ResolveTask(ParseContext parseContext, String str) {
            this.context = parseContext;
            this.referenceValue = str;
        }
    }

    static {
        for (Class<?> cls : new Class[]{Boolean.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Boolean.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, BigInteger.class, BigDecimal.class, String.class}) {
            primitiveClasses.add(cls);
        }
    }

    public DefaultJSONParser(JSONLexer jSONLexer) {
        this(jSONLexer, ParserConfig.getGlobalInstance());
    }

    public DefaultJSONParser(JSONLexer jSONLexer, ParserConfig parserConfig) {
        this((Object) null, jSONLexer, parserConfig);
    }

    public DefaultJSONParser(Object obj, JSONLexer jSONLexer, ParserConfig parserConfig) {
        this.dateFormatPattern = JSON.DEFFAULT_DATE_FORMAT;
        this.contextArrayIndex = 0;
        this.resolveStatus = 0;
        this.extraTypeProviders = null;
        this.extraProcessors = null;
        this.fieldTypeResolver = null;
        this.autoTypeAccept = null;
        this.lexer = jSONLexer;
        this.input = obj;
        this.config = parserConfig;
        this.symbolTable = parserConfig.symbolTable;
        char current = jSONLexer.getCurrent();
        if (current == '{') {
            jSONLexer.next();
            ((JSONLexerBase) jSONLexer).token = 12;
        } else if (current != '[') {
            jSONLexer.nextToken();
        } else {
            jSONLexer.next();
            ((JSONLexerBase) jSONLexer).token = 14;
        }
    }

    public DefaultJSONParser(String str) {
        this(str, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
    }

    public DefaultJSONParser(String str, ParserConfig parserConfig) {
        this(str, new JSONScanner(str, JSON.DEFAULT_PARSER_FEATURE), parserConfig);
    }

    public DefaultJSONParser(String str, ParserConfig parserConfig, int i) {
        this(str, new JSONScanner(str, i), parserConfig);
    }

    public DefaultJSONParser(char[] cArr, int i, ParserConfig parserConfig, int i2) {
        this(cArr, new JSONScanner(cArr, i, i2), parserConfig);
    }

    private void addContext(ParseContext parseContext) {
        int i = this.contextArrayIndex;
        this.contextArrayIndex = i + 1;
        if (this.contextArray == null) {
            this.contextArray = new ParseContext[8];
        } else if (i >= this.contextArray.length) {
            ParseContext[] parseContextArr = new ParseContext[(this.contextArray.length * 3) / 2];
            System.arraycopy(this.contextArray, 0, parseContextArr, 0, this.contextArray.length);
            this.contextArray = parseContextArr;
        }
        this.contextArray[i] = parseContext;
    }

    public final void accept(int i) {
        JSONLexer jSONLexer = this.lexer;
        if (jSONLexer.token() == i) {
            jSONLexer.nextToken();
            return;
        }
        throw new JSONException("syntax error, expect " + JSONToken.name(i) + ", actual " + JSONToken.name(jSONLexer.token()));
    }

    public final void accept(int i, int i2) {
        JSONLexer jSONLexer = this.lexer;
        if (jSONLexer.token() == i) {
            jSONLexer.nextToken(i2);
        } else {
            throwException(i);
        }
    }

    public void acceptType(String str) {
        JSONLexer jSONLexer = this.lexer;
        jSONLexer.nextTokenWithColon();
        if (jSONLexer.token() != 4) {
            throw new JSONException("type not match error");
        }
        if (!str.equals(jSONLexer.stringVal())) {
            throw new JSONException("type not match error");
        }
        jSONLexer.nextToken();
        if (jSONLexer.token() == 16) {
            jSONLexer.nextToken();
        }
    }

    public void addResolveTask(ResolveTask resolveTask) {
        if (this.resolveTaskList == null) {
            this.resolveTaskList = new ArrayList(2);
        }
        this.resolveTaskList.add(resolveTask);
    }

    public void checkListResolve(Collection collection) {
        if (this.resolveStatus == 1) {
            if (!(collection instanceof List)) {
                ResolveTask lastResolveTask = getLastResolveTask();
                lastResolveTask.fieldDeserializer = new ResolveFieldDeserializer(collection);
                lastResolveTask.ownerContext = this.context;
                setResolveStatus(0);
                return;
            }
            int size = collection.size() - 1;
            ResolveTask lastResolveTask2 = getLastResolveTask();
            lastResolveTask2.fieldDeserializer = new ResolveFieldDeserializer(this, (List) collection, size);
            lastResolveTask2.ownerContext = this.context;
            setResolveStatus(0);
        }
    }

    public void checkMapResolve(Map map, Object obj) {
        if (this.resolveStatus == 1) {
            ResolveFieldDeserializer resolveFieldDeserializer = new ResolveFieldDeserializer(map, obj);
            ResolveTask lastResolveTask = getLastResolveTask();
            lastResolveTask.fieldDeserializer = resolveFieldDeserializer;
            lastResolveTask.ownerContext = this.context;
            setResolveStatus(0);
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        JSONLexer jSONLexer = this.lexer;
        try {
            if (jSONLexer.isEnabled(Feature.AutoCloseSource) && jSONLexer.token() != 20) {
                throw new JSONException("not close json text, token : " + JSONToken.name(jSONLexer.token()));
            }
        } finally {
            jSONLexer.close();
        }
    }

    public void config(Feature feature, boolean z) {
        this.lexer.config(feature, z);
    }

    public ParserConfig getConfig() {
        return this.config;
    }

    public ParseContext getContext() {
        return this.context;
    }

    public String getDateFomartPattern() {
        return this.dateFormatPattern;
    }

    public DateFormat getDateFormat() {
        if (this.dateFormat == null) {
            this.dateFormat = new SimpleDateFormat(this.dateFormatPattern, this.lexer.getLocale());
            this.dateFormat.setTimeZone(this.lexer.getTimeZone());
        }
        return this.dateFormat;
    }

    public List<ExtraProcessor> getExtraProcessors() {
        if (this.extraProcessors == null) {
            this.extraProcessors = new ArrayList(2);
        }
        return this.extraProcessors;
    }

    public List<ExtraTypeProvider> getExtraTypeProviders() {
        if (this.extraTypeProviders == null) {
            this.extraTypeProviders = new ArrayList(2);
        }
        return this.extraTypeProviders;
    }

    public FieldTypeResolver getFieldTypeResolver() {
        return this.fieldTypeResolver;
    }

    public String getInput() {
        return this.input instanceof char[] ? new String((char[]) this.input) : this.input.toString();
    }

    public ResolveTask getLastResolveTask() {
        return this.resolveTaskList.get(this.resolveTaskList.size() - 1);
    }

    public JSONLexer getLexer() {
        return this.lexer;
    }

    public Object getObject(String str) {
        for (int i = 0; i < this.contextArrayIndex; i++) {
            if (str.equals(this.contextArray[i].toString())) {
                return this.contextArray[i].object;
            }
        }
        return null;
    }

    public int getResolveStatus() {
        return this.resolveStatus;
    }

    public List<ResolveTask> getResolveTaskList() {
        if (this.resolveTaskList == null) {
            this.resolveTaskList = new ArrayList(2);
        }
        return this.resolveTaskList;
    }

    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    public void handleResovleTask(Object obj) {
        Object obj2;
        if (this.resolveTaskList == null) {
            return;
        }
        int size = this.resolveTaskList.size();
        for (int i = 0; i < size; i++) {
            ResolveTask resolveTask = this.resolveTaskList.get(i);
            String str = resolveTask.referenceValue;
            Object obj3 = resolveTask.ownerContext != null ? resolveTask.ownerContext.object : null;
            if (str.startsWith("$")) {
                obj2 = getObject(str);
                if (obj2 == null) {
                    try {
                        obj2 = JSONPath.eval(obj, str);
                    } catch (JSONPathException unused) {
                    }
                }
            } else {
                obj2 = resolveTask.context.object;
            }
            FieldDeserializer fieldDeserializer = resolveTask.fieldDeserializer;
            if (fieldDeserializer != null) {
                if (obj2 != null && obj2.getClass() == JSONObject.class && fieldDeserializer.fieldInfo != null && !Map.class.isAssignableFrom(fieldDeserializer.fieldInfo.fieldClass)) {
                    obj2 = JSONPath.eval(this.contextArray[0].object, str);
                }
                fieldDeserializer.setValue(obj3, obj2);
            }
        }
    }

    public boolean isEnabled(Feature feature) {
        return this.lexer.isEnabled(feature);
    }

    public Object parse() {
        return parse(null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:70:0x0238, code lost:
    
        return r10;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.Object parse(com.alibaba.fastjson.parser.deserializer.PropertyProcessable r10, java.lang.Object r11) {
        /*
            Method dump skipped, instructions count: 619
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.DefaultJSONParser.parse(com.alibaba.fastjson.parser.deserializer.PropertyProcessable, java.lang.Object):java.lang.Object");
    }

    public Object parse(Object obj) {
        JSONLexer jSONLexer = this.lexer;
        switch (jSONLexer.token()) {
            case 2:
                Number integerValue = jSONLexer.integerValue();
                jSONLexer.nextToken();
                return integerValue;
            case 3:
                Number decimalValue = jSONLexer.decimalValue(jSONLexer.isEnabled(Feature.UseBigDecimal));
                jSONLexer.nextToken();
                return decimalValue;
            case 4:
                String stringVal = jSONLexer.stringVal();
                jSONLexer.nextToken(16);
                if (jSONLexer.isEnabled(Feature.AllowISO8601DateFormat)) {
                    JSONScanner jSONScanner = new JSONScanner(stringVal);
                    try {
                        if (jSONScanner.scanISO8601DateIfMatch()) {
                            return jSONScanner.getCalendar().getTime();
                        }
                    } finally {
                        jSONScanner.close();
                    }
                }
                return stringVal;
            case 5:
            case 10:
            case 11:
            case 13:
            case 15:
            case 16:
            case 17:
            case 19:
            case 24:
            case 25:
            default:
                throw new JSONException("syntax error, " + jSONLexer.info());
            case 6:
                jSONLexer.nextToken();
                return Boolean.TRUE;
            case 7:
                jSONLexer.nextToken();
                return Boolean.FALSE;
            case 8:
                jSONLexer.nextToken();
                return null;
            case 9:
                jSONLexer.nextToken(18);
                if (jSONLexer.token() != 18) {
                    throw new JSONException("syntax error");
                }
                jSONLexer.nextToken(10);
                accept(10);
                long longValue = jSONLexer.integerValue().longValue();
                accept(2);
                accept(11);
                return new Date(longValue);
            case 12:
                return parseObject(new JSONObject(jSONLexer.isEnabled(Feature.OrderedField)), obj);
            case 14:
                JSONArray jSONArray = new JSONArray();
                parseArray(jSONArray, obj);
                return jSONLexer.isEnabled(Feature.UseObjectArray) ? jSONArray.toArray() : jSONArray;
            case 18:
                if ("NaN".equals(jSONLexer.stringVal())) {
                    jSONLexer.nextToken();
                    return null;
                }
                throw new JSONException("syntax error, " + jSONLexer.info());
            case 20:
                if (jSONLexer.isBlankInput()) {
                    return null;
                }
                throw new JSONException("unterminated json string, " + jSONLexer.info());
            case 21:
                jSONLexer.nextToken();
                HashSet hashSet = new HashSet();
                parseArray(hashSet, obj);
                return hashSet;
            case 22:
                jSONLexer.nextToken();
                TreeSet treeSet = new TreeSet();
                parseArray(treeSet, obj);
                return treeSet;
            case 23:
                jSONLexer.nextToken();
                return null;
            case 26:
                byte[] bytesValue = jSONLexer.bytesValue();
                jSONLexer.nextToken();
                return bytesValue;
        }
    }

    public <T> List<T> parseArray(Class<T> cls) {
        ArrayList arrayList = new ArrayList();
        parseArray((Class<?>) cls, (Collection) arrayList);
        return arrayList;
    }

    public void parseArray(Class<?> cls, Collection collection) {
        parseArray((Type) cls, collection);
    }

    public void parseArray(Type type, Collection collection) {
        parseArray(type, collection, null);
    }

    public void parseArray(Type type, Collection collection, Object obj) {
        ObjectDeserializer deserializer;
        int i = this.lexer.token();
        if (i == 21 || i == 22) {
            this.lexer.nextToken();
            i = this.lexer.token();
        }
        if (i != 14) {
            throw new JSONException("exepct '[', but " + JSONToken.name(i) + ", " + this.lexer.info());
        }
        if (Integer.TYPE == type) {
            deserializer = IntegerCodec.instance;
            this.lexer.nextToken(2);
        } else if (String.class == type) {
            deserializer = StringCodec.instance;
            this.lexer.nextToken(4);
        } else {
            deserializer = this.config.getDeserializer(type);
            this.lexer.nextToken(deserializer.getFastMatchToken());
        }
        ParseContext parseContext = this.context;
        setContext(collection, obj);
        int i2 = 0;
        while (true) {
            try {
                if (this.lexer.isEnabled(Feature.AllowArbitraryCommas)) {
                    while (this.lexer.token() == 16) {
                        this.lexer.nextToken();
                    }
                }
                if (this.lexer.token() == 15) {
                    setContext(parseContext);
                    this.lexer.nextToken(16);
                    return;
                }
                Object obj2 = null;
                if (Integer.TYPE == type) {
                    collection.add(IntegerCodec.instance.deserialze(this, null, null));
                } else if (String.class == type) {
                    if (this.lexer.token() == 4) {
                        obj2 = this.lexer.stringVal();
                        this.lexer.nextToken(16);
                    } else {
                        Object parse = parse();
                        if (parse != null) {
                            obj2 = parse.toString();
                        }
                    }
                    collection.add(obj2);
                } else {
                    if (this.lexer.token() == 8) {
                        this.lexer.nextToken();
                    } else {
                        obj2 = deserializer.deserialze(this, type, Integer.valueOf(i2));
                    }
                    collection.add(obj2);
                    checkListResolve(collection);
                }
                if (this.lexer.token() == 16) {
                    this.lexer.nextToken(deserializer.getFastMatchToken());
                }
                i2++;
            } catch (Throwable th) {
                setContext(parseContext);
                throw th;
            }
        }
    }

    public final void parseArray(Collection collection) {
        parseArray(collection, (Object) null);
    }

    public final void parseArray(Collection collection, Object obj) {
        JSONLexer jSONLexer = this.lexer;
        if (jSONLexer.token() == 21 || jSONLexer.token() == 22) {
            jSONLexer.nextToken();
        }
        if (jSONLexer.token() != 14) {
            throw new JSONException("syntax error, expect [, actual " + JSONToken.name(jSONLexer.token()) + ", pos " + jSONLexer.pos() + ", fieldName " + obj);
        }
        jSONLexer.nextToken(4);
        ParseContext parseContext = this.context;
        setContext(collection, obj);
        int i = 0;
        while (true) {
            try {
                if (jSONLexer.isEnabled(Feature.AllowArbitraryCommas)) {
                    while (jSONLexer.token() == 16) {
                        jSONLexer.nextToken();
                    }
                }
                Object obj2 = null;
                obj2 = null;
                switch (jSONLexer.token()) {
                    case 2:
                        Number integerValue = jSONLexer.integerValue();
                        jSONLexer.nextToken(16);
                        obj2 = integerValue;
                        break;
                    case 3:
                        obj2 = jSONLexer.isEnabled(Feature.UseBigDecimal) ? jSONLexer.decimalValue(true) : jSONLexer.decimalValue(false);
                        jSONLexer.nextToken(16);
                        break;
                    case 4:
                        String stringVal = jSONLexer.stringVal();
                        jSONLexer.nextToken(16);
                        obj2 = stringVal;
                        if (jSONLexer.isEnabled(Feature.AllowISO8601DateFormat)) {
                            JSONScanner jSONScanner = new JSONScanner(stringVal);
                            Object obj3 = stringVal;
                            if (jSONScanner.scanISO8601DateIfMatch()) {
                                obj3 = jSONScanner.getCalendar().getTime();
                            }
                            jSONScanner.close();
                            obj2 = obj3;
                            break;
                        }
                        break;
                    case 6:
                        Boolean bool = Boolean.TRUE;
                        jSONLexer.nextToken(16);
                        obj2 = bool;
                        break;
                    case 7:
                        Boolean bool2 = Boolean.FALSE;
                        jSONLexer.nextToken(16);
                        obj2 = bool2;
                        break;
                    case 8:
                        jSONLexer.nextToken(4);
                        break;
                    case 12:
                        obj2 = parseObject(new JSONObject(jSONLexer.isEnabled(Feature.OrderedField)), Integer.valueOf(i));
                        break;
                    case 14:
                        JSONArray jSONArray = new JSONArray();
                        parseArray(jSONArray, Integer.valueOf(i));
                        obj2 = jSONArray;
                        if (jSONLexer.isEnabled(Feature.UseObjectArray)) {
                            obj2 = jSONArray.toArray();
                            break;
                        }
                        break;
                    case 15:
                        jSONLexer.nextToken(16);
                        return;
                    case 20:
                        throw new JSONException("unclosed jsonArray");
                    case 23:
                        jSONLexer.nextToken(4);
                        break;
                    default:
                        obj2 = parse();
                        break;
                }
                collection.add(obj2);
                checkListResolve(collection);
                if (jSONLexer.token() == 16) {
                    jSONLexer.nextToken(4);
                }
                i++;
            } finally {
                setContext(parseContext);
            }
        }
    }

    public Object[] parseArray(Type[] typeArr) {
        Object cast;
        Class<?> cls;
        boolean z;
        if (this.lexer.token() == 8) {
            this.lexer.nextToken(16);
            return null;
        }
        if (this.lexer.token() != 14) {
            throw new JSONException("syntax error : " + this.lexer.tokenName());
        }
        Object[] objArr = new Object[typeArr.length];
        if (typeArr.length == 0) {
            this.lexer.nextToken(15);
            if (this.lexer.token() != 15) {
                throw new JSONException("syntax error");
            }
            this.lexer.nextToken(16);
            return new Object[0];
        }
        this.lexer.nextToken(2);
        for (int i = 0; i < typeArr.length; i++) {
            if (this.lexer.token() == 8) {
                this.lexer.nextToken(16);
                cast = null;
            } else {
                Type type = typeArr[i];
                if (type == Integer.TYPE || type == Integer.class) {
                    if (this.lexer.token() == 2) {
                        cast = Integer.valueOf(this.lexer.intValue());
                        this.lexer.nextToken(16);
                    } else {
                        cast = TypeUtils.cast(parse(), type, this.config);
                    }
                } else if (type != String.class) {
                    if (i == typeArr.length - 1 && (type instanceof Class)) {
                        Class cls2 = (Class) type;
                        z = cls2.isArray();
                        cls = cls2.getComponentType();
                    } else {
                        cls = null;
                        z = false;
                    }
                    if (!z || this.lexer.token() == 14) {
                        cast = this.config.getDeserializer(type).deserialze(this, type, Integer.valueOf(i));
                    } else {
                        ArrayList arrayList = new ArrayList();
                        ObjectDeserializer deserializer = this.config.getDeserializer(cls);
                        int fastMatchToken = deserializer.getFastMatchToken();
                        if (this.lexer.token() != 15) {
                            while (true) {
                                arrayList.add(deserializer.deserialze(this, type, null));
                                if (this.lexer.token() != 16) {
                                    break;
                                }
                                this.lexer.nextToken(fastMatchToken);
                            }
                            if (this.lexer.token() != 15) {
                                throw new JSONException("syntax error :" + JSONToken.name(this.lexer.token()));
                            }
                        }
                        cast = TypeUtils.cast(arrayList, type, this.config);
                    }
                } else if (this.lexer.token() == 4) {
                    cast = this.lexer.stringVal();
                    this.lexer.nextToken(16);
                } else {
                    cast = TypeUtils.cast(parse(), type, this.config);
                }
            }
            objArr[i] = cast;
            if (this.lexer.token() == 15) {
                break;
            }
            if (this.lexer.token() != 16) {
                throw new JSONException("syntax error :" + JSONToken.name(this.lexer.token()));
            }
            if (i == typeArr.length - 1) {
                this.lexer.nextToken(15);
            } else {
                this.lexer.nextToken(2);
            }
        }
        if (this.lexer.token() != 15) {
            throw new JSONException("syntax error");
        }
        this.lexer.nextToken(16);
        return objArr;
    }

    public Object parseArrayWithType(Type type) {
        if (this.lexer.token() == 8) {
            this.lexer.nextToken();
            return null;
        }
        Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
        if (actualTypeArguments.length != 1) {
            throw new JSONException("not support type " + type);
        }
        Type type2 = actualTypeArguments[0];
        if (type2 instanceof Class) {
            ArrayList arrayList = new ArrayList();
            parseArray((Class<?>) type2, (Collection) arrayList);
            return arrayList;
        }
        if (type2 instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type2;
            Type type3 = wildcardType.getUpperBounds()[0];
            if (!Object.class.equals(type3)) {
                ArrayList arrayList2 = new ArrayList();
                parseArray((Class<?>) type3, (Collection) arrayList2);
                return arrayList2;
            }
            if (wildcardType.getLowerBounds().length == 0) {
                return parse();
            }
            throw new JSONException("not support type : " + type);
        }
        if (type2 instanceof TypeVariable) {
            TypeVariable typeVariable = (TypeVariable) type2;
            Type[] bounds = typeVariable.getBounds();
            if (bounds.length != 1) {
                throw new JSONException("not support : " + typeVariable);
            }
            Type type4 = bounds[0];
            if (type4 instanceof Class) {
                ArrayList arrayList3 = new ArrayList();
                parseArray((Class<?>) type4, (Collection) arrayList3);
                return arrayList3;
            }
        }
        if (type2 instanceof ParameterizedType) {
            ArrayList arrayList4 = new ArrayList();
            parseArray((ParameterizedType) type2, arrayList4);
            return arrayList4;
        }
        throw new JSONException("TODO : " + type);
    }

    public void parseExtra(Object obj, String str) {
        this.lexer.nextTokenWithColon();
        Type type = null;
        if (this.extraTypeProviders != null) {
            Iterator<ExtraTypeProvider> it = this.extraTypeProviders.iterator();
            while (it.hasNext()) {
                type = it.next().getExtraType(obj, str);
            }
        }
        Object parse = type == null ? parse() : parseObject(type);
        if (obj instanceof ExtraProcessable) {
            ((ExtraProcessable) obj).processExtra(str, parse);
            return;
        }
        if (this.extraProcessors != null) {
            Iterator<ExtraProcessor> it2 = this.extraProcessors.iterator();
            while (it2.hasNext()) {
                it2.next().processExtra(obj, str, parse);
            }
        }
        if (this.resolveStatus == 1) {
            this.resolveStatus = 0;
        }
    }

    public Object parseKey() {
        if (this.lexer.token() != 18) {
            return parse(null);
        }
        String stringVal = this.lexer.stringVal();
        this.lexer.nextToken(16);
        return stringVal;
    }

    public JSONObject parseObject() {
        return (JSONObject) parseObject((Map) new JSONObject(this.lexer.isEnabled(Feature.OrderedField)));
    }

    public <T> T parseObject(Class<T> cls) {
        return (T) parseObject(cls, (Object) null);
    }

    public <T> T parseObject(Type type) {
        return (T) parseObject(type, (Object) null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> T parseObject(Type type, Object obj) {
        int i = this.lexer.token();
        if (i == 8) {
            this.lexer.nextToken();
            return null;
        }
        if (i == 4) {
            if (type == byte[].class) {
                T t = (T) this.lexer.bytesValue();
                this.lexer.nextToken();
                return t;
            }
            if (type == char[].class) {
                String stringVal = this.lexer.stringVal();
                this.lexer.nextToken();
                return (T) stringVal.toCharArray();
            }
        }
        try {
            return (T) this.config.getDeserializer(type).deserialze(this, type, obj);
        } catch (JSONException e) {
            throw e;
        } catch (Throwable th) {
            throw new JSONException(th.getMessage(), th);
        }
    }

    public Object parseObject(Map map) {
        return parseObject(map, (Object) null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x02f8, code lost:
    
        popContext();
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x02ff, code lost:
    
        if (r19.size() <= 0) goto L163;
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x0301, code lost:
    
        r0 = com.alibaba.fastjson.util.TypeUtils.cast((java.lang.Object) r19, (java.lang.Class<java.lang.Object>) r6, r18.config);
        parseObject(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x030a, code lost:
    
        setContext(r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x030d, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x030e, code lost:
    
        r0 = r18.config.getDeserializer(r6);
        r3 = r0.getClass();
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x031e, code lost:
    
        if (com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.class.isAssignableFrom(r3) == false) goto L170;
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x0322, code lost:
    
        if (r3 == com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.class) goto L170;
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x0326, code lost:
    
        if (r3 == com.alibaba.fastjson.parser.deserializer.ThrowableDeserializer.class) goto L170;
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x0328, code lost:
    
        setResolveStatus(0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:113:0x032c, code lost:
    
        r0 = r0.deserialze(r18, r6, r20);
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x0330, code lost:
    
        setContext(r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x0333, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:372:0x0106, code lost:
    
        if (r11 == null) goto L51;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x026b, code lost:
    
        r3.nextToken(16);
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0276, code lost:
    
        if (r3.token() != 13) goto L150;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0278, code lost:
    
        r3.nextToken(16);
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x027b, code lost:
    
        r0 = r18.config.getDeserializer(r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0283, code lost:
    
        if ((r0 instanceof com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer) == false) goto L136;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0285, code lost:
    
        r0 = (com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer) r0;
        r2 = r0.createInstance(r18, r6);
        r3 = r8.entrySet().iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0297, code lost:
    
        if (r3.hasNext() == false) goto L379;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0299, code lost:
    
        r4 = (java.util.Map.Entry) r3.next();
        r7 = r4.getKey();
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x02a5, code lost:
    
        if ((r7 instanceof java.lang.String) == false) goto L382;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x02a7, code lost:
    
        r7 = r0.getFieldDeserializer((java.lang.String) r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x02ad, code lost:
    
        if (r7 == null) goto L383;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x02af, code lost:
    
        r7.setValue(r2, r4.getValue());
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x02b8, code lost:
    
        if (r2 != null) goto L145;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x02bc, code lost:
    
        if (r6 != java.lang.Cloneable.class) goto L141;
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x02be, code lost:
    
        r2 = new java.util.HashMap();
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x02ca, code lost:
    
        if ("java.util.Collections$EmptyMap".equals(r5) == false) goto L144;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x02cc, code lost:
    
        r2 = java.util.Collections.emptyMap();
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x02d1, code lost:
    
        r2 = r6.newInstance();
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x02d5, code lost:
    
        setContext(r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x02d8, code lost:
    
        return r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x02b7, code lost:
    
        r2 = r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x02d9, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x02e1, code lost:
    
        throw new com.alibaba.fastjson.JSONException("create instance error", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x02e2, code lost:
    
        setResolveStatus(2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x02e8, code lost:
    
        if (r18.context == null) goto L158;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x02ea, code lost:
    
        if (r20 == null) goto L158;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x02ee, code lost:
    
        if ((r20 instanceof java.lang.Integer) != false) goto L158;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x02f6, code lost:
    
        if ((r18.context.fieldName instanceof java.lang.Integer) != false) goto L158;
     */
    /* JADX WARN: Removed duplicated region for block: B:168:0x03ff A[Catch: all -> 0x0623, TryCatch #0 {all -> 0x0623, blocks: (B:29:0x007f, B:32:0x0092, B:36:0x00ac, B:40:0x0210, B:41:0x0216, B:43:0x0221, B:45:0x0229, B:52:0x023d, B:54:0x024b, B:56:0x025e, B:58:0x026b, B:60:0x0278, B:62:0x027b, B:64:0x0285, B:65:0x0293, B:67:0x0299, B:70:0x02a7, B:73:0x02af, B:82:0x02be, B:83:0x02c4, B:85:0x02cc, B:86:0x02d1, B:91:0x02da, B:92:0x02e1, B:93:0x02e2, B:96:0x02ec, B:98:0x02f0, B:100:0x02f8, B:101:0x02fb, B:103:0x0301, B:106:0x030e, B:112:0x0328, B:113:0x032c, B:116:0x0251, B:121:0x033c, B:124:0x0344, B:126:0x034e, B:128:0x035f, B:130:0x0363, B:132:0x036b, B:135:0x0370, B:137:0x0374, B:138:0x03c4, B:140:0x03cc, B:143:0x03d5, B:144:0x03dc, B:146:0x0379, B:148:0x0381, B:150:0x0385, B:151:0x0388, B:152:0x0394, B:155:0x039d, B:157:0x03a1, B:159:0x03a4, B:161:0x03a8, B:162:0x03ac, B:163:0x03b8, B:164:0x03dd, B:165:0x03fb, B:168:0x03ff, B:170:0x0403, B:172:0x0409, B:174:0x040f, B:175:0x0413, B:180:0x041d, B:186:0x042d, B:188:0x043c, B:190:0x0447, B:191:0x044f, B:192:0x0452, B:193:0x0478, B:195:0x0481, B:201:0x048c, B:204:0x049c, B:205:0x04be, B:208:0x045c, B:210:0x0466, B:211:0x0475, B:212:0x046b, B:216:0x04c3, B:218:0x04cd, B:220:0x04d5, B:221:0x04d8, B:223:0x04e3, B:224:0x04e7, B:233:0x04f2, B:226:0x04f9, B:230:0x0502, B:231:0x0509, B:238:0x050e, B:240:0x0513, B:243:0x051e, B:245:0x0526, B:247:0x0544, B:248:0x054a, B:251:0x0550, B:252:0x0556, B:254:0x055e, B:256:0x0570, B:259:0x0578, B:261:0x057c, B:262:0x0583, B:264:0x0588, B:265:0x058b, B:276:0x0593, B:267:0x059d, B:270:0x05a7, B:271:0x05ac, B:273:0x05b1, B:274:0x05cb, B:282:0x0537, B:284:0x05cc, B:292:0x05de, B:286:0x05e5, B:289:0x05f0, B:290:0x0612, B:296:0x00be, B:297:0x00e0, B:361:0x00e3, B:363:0x00ee, B:365:0x00f2, B:367:0x00f8, B:369:0x00fe, B:371:0x0102, B:301:0x0111, B:303:0x0119, B:307:0x0129, B:308:0x0143, B:310:0x0144, B:311:0x014b, B:317:0x0158, B:319:0x015e, B:321:0x0165, B:322:0x016e, B:324:0x0176, B:326:0x017b, B:330:0x0183, B:331:0x019d, B:332:0x016a, B:334:0x019e, B:335:0x01b8, B:342:0x01c2, B:344:0x01ca, B:348:0x01db, B:349:0x01fd, B:351:0x01fe, B:352:0x0205, B:353:0x0206, B:355:0x0613, B:356:0x061a, B:358:0x061b, B:359:0x0622), top: B:28:0x007f, inners: #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:186:0x042d A[Catch: all -> 0x0623, TryCatch #0 {all -> 0x0623, blocks: (B:29:0x007f, B:32:0x0092, B:36:0x00ac, B:40:0x0210, B:41:0x0216, B:43:0x0221, B:45:0x0229, B:52:0x023d, B:54:0x024b, B:56:0x025e, B:58:0x026b, B:60:0x0278, B:62:0x027b, B:64:0x0285, B:65:0x0293, B:67:0x0299, B:70:0x02a7, B:73:0x02af, B:82:0x02be, B:83:0x02c4, B:85:0x02cc, B:86:0x02d1, B:91:0x02da, B:92:0x02e1, B:93:0x02e2, B:96:0x02ec, B:98:0x02f0, B:100:0x02f8, B:101:0x02fb, B:103:0x0301, B:106:0x030e, B:112:0x0328, B:113:0x032c, B:116:0x0251, B:121:0x033c, B:124:0x0344, B:126:0x034e, B:128:0x035f, B:130:0x0363, B:132:0x036b, B:135:0x0370, B:137:0x0374, B:138:0x03c4, B:140:0x03cc, B:143:0x03d5, B:144:0x03dc, B:146:0x0379, B:148:0x0381, B:150:0x0385, B:151:0x0388, B:152:0x0394, B:155:0x039d, B:157:0x03a1, B:159:0x03a4, B:161:0x03a8, B:162:0x03ac, B:163:0x03b8, B:164:0x03dd, B:165:0x03fb, B:168:0x03ff, B:170:0x0403, B:172:0x0409, B:174:0x040f, B:175:0x0413, B:180:0x041d, B:186:0x042d, B:188:0x043c, B:190:0x0447, B:191:0x044f, B:192:0x0452, B:193:0x0478, B:195:0x0481, B:201:0x048c, B:204:0x049c, B:205:0x04be, B:208:0x045c, B:210:0x0466, B:211:0x0475, B:212:0x046b, B:216:0x04c3, B:218:0x04cd, B:220:0x04d5, B:221:0x04d8, B:223:0x04e3, B:224:0x04e7, B:233:0x04f2, B:226:0x04f9, B:230:0x0502, B:231:0x0509, B:238:0x050e, B:240:0x0513, B:243:0x051e, B:245:0x0526, B:247:0x0544, B:248:0x054a, B:251:0x0550, B:252:0x0556, B:254:0x055e, B:256:0x0570, B:259:0x0578, B:261:0x057c, B:262:0x0583, B:264:0x0588, B:265:0x058b, B:276:0x0593, B:267:0x059d, B:270:0x05a7, B:271:0x05ac, B:273:0x05b1, B:274:0x05cb, B:282:0x0537, B:284:0x05cc, B:292:0x05de, B:286:0x05e5, B:289:0x05f0, B:290:0x0612, B:296:0x00be, B:297:0x00e0, B:361:0x00e3, B:363:0x00ee, B:365:0x00f2, B:367:0x00f8, B:369:0x00fe, B:371:0x0102, B:301:0x0111, B:303:0x0119, B:307:0x0129, B:308:0x0143, B:310:0x0144, B:311:0x014b, B:317:0x0158, B:319:0x015e, B:321:0x0165, B:322:0x016e, B:324:0x0176, B:326:0x017b, B:330:0x0183, B:331:0x019d, B:332:0x016a, B:334:0x019e, B:335:0x01b8, B:342:0x01c2, B:344:0x01ca, B:348:0x01db, B:349:0x01fd, B:351:0x01fe, B:352:0x0205, B:353:0x0206, B:355:0x0613, B:356:0x061a, B:358:0x061b, B:359:0x0622), top: B:28:0x007f, inners: #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:195:0x0481 A[Catch: all -> 0x0623, TryCatch #0 {all -> 0x0623, blocks: (B:29:0x007f, B:32:0x0092, B:36:0x00ac, B:40:0x0210, B:41:0x0216, B:43:0x0221, B:45:0x0229, B:52:0x023d, B:54:0x024b, B:56:0x025e, B:58:0x026b, B:60:0x0278, B:62:0x027b, B:64:0x0285, B:65:0x0293, B:67:0x0299, B:70:0x02a7, B:73:0x02af, B:82:0x02be, B:83:0x02c4, B:85:0x02cc, B:86:0x02d1, B:91:0x02da, B:92:0x02e1, B:93:0x02e2, B:96:0x02ec, B:98:0x02f0, B:100:0x02f8, B:101:0x02fb, B:103:0x0301, B:106:0x030e, B:112:0x0328, B:113:0x032c, B:116:0x0251, B:121:0x033c, B:124:0x0344, B:126:0x034e, B:128:0x035f, B:130:0x0363, B:132:0x036b, B:135:0x0370, B:137:0x0374, B:138:0x03c4, B:140:0x03cc, B:143:0x03d5, B:144:0x03dc, B:146:0x0379, B:148:0x0381, B:150:0x0385, B:151:0x0388, B:152:0x0394, B:155:0x039d, B:157:0x03a1, B:159:0x03a4, B:161:0x03a8, B:162:0x03ac, B:163:0x03b8, B:164:0x03dd, B:165:0x03fb, B:168:0x03ff, B:170:0x0403, B:172:0x0409, B:174:0x040f, B:175:0x0413, B:180:0x041d, B:186:0x042d, B:188:0x043c, B:190:0x0447, B:191:0x044f, B:192:0x0452, B:193:0x0478, B:195:0x0481, B:201:0x048c, B:204:0x049c, B:205:0x04be, B:208:0x045c, B:210:0x0466, B:211:0x0475, B:212:0x046b, B:216:0x04c3, B:218:0x04cd, B:220:0x04d5, B:221:0x04d8, B:223:0x04e3, B:224:0x04e7, B:233:0x04f2, B:226:0x04f9, B:230:0x0502, B:231:0x0509, B:238:0x050e, B:240:0x0513, B:243:0x051e, B:245:0x0526, B:247:0x0544, B:248:0x054a, B:251:0x0550, B:252:0x0556, B:254:0x055e, B:256:0x0570, B:259:0x0578, B:261:0x057c, B:262:0x0583, B:264:0x0588, B:265:0x058b, B:276:0x0593, B:267:0x059d, B:270:0x05a7, B:271:0x05ac, B:273:0x05b1, B:274:0x05cb, B:282:0x0537, B:284:0x05cc, B:292:0x05de, B:286:0x05e5, B:289:0x05f0, B:290:0x0612, B:296:0x00be, B:297:0x00e0, B:361:0x00e3, B:363:0x00ee, B:365:0x00f2, B:367:0x00f8, B:369:0x00fe, B:371:0x0102, B:301:0x0111, B:303:0x0119, B:307:0x0129, B:308:0x0143, B:310:0x0144, B:311:0x014b, B:317:0x0158, B:319:0x015e, B:321:0x0165, B:322:0x016e, B:324:0x0176, B:326:0x017b, B:330:0x0183, B:331:0x019d, B:332:0x016a, B:334:0x019e, B:335:0x01b8, B:342:0x01c2, B:344:0x01ca, B:348:0x01db, B:349:0x01fd, B:351:0x01fe, B:352:0x0205, B:353:0x0206, B:355:0x0613, B:356:0x061a, B:358:0x061b, B:359:0x0622), top: B:28:0x007f, inners: #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:199:0x048a A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:206:0x0456  */
    /* JADX WARN: Removed duplicated region for block: B:256:0x0570 A[Catch: all -> 0x0623, TryCatch #0 {all -> 0x0623, blocks: (B:29:0x007f, B:32:0x0092, B:36:0x00ac, B:40:0x0210, B:41:0x0216, B:43:0x0221, B:45:0x0229, B:52:0x023d, B:54:0x024b, B:56:0x025e, B:58:0x026b, B:60:0x0278, B:62:0x027b, B:64:0x0285, B:65:0x0293, B:67:0x0299, B:70:0x02a7, B:73:0x02af, B:82:0x02be, B:83:0x02c4, B:85:0x02cc, B:86:0x02d1, B:91:0x02da, B:92:0x02e1, B:93:0x02e2, B:96:0x02ec, B:98:0x02f0, B:100:0x02f8, B:101:0x02fb, B:103:0x0301, B:106:0x030e, B:112:0x0328, B:113:0x032c, B:116:0x0251, B:121:0x033c, B:124:0x0344, B:126:0x034e, B:128:0x035f, B:130:0x0363, B:132:0x036b, B:135:0x0370, B:137:0x0374, B:138:0x03c4, B:140:0x03cc, B:143:0x03d5, B:144:0x03dc, B:146:0x0379, B:148:0x0381, B:150:0x0385, B:151:0x0388, B:152:0x0394, B:155:0x039d, B:157:0x03a1, B:159:0x03a4, B:161:0x03a8, B:162:0x03ac, B:163:0x03b8, B:164:0x03dd, B:165:0x03fb, B:168:0x03ff, B:170:0x0403, B:172:0x0409, B:174:0x040f, B:175:0x0413, B:180:0x041d, B:186:0x042d, B:188:0x043c, B:190:0x0447, B:191:0x044f, B:192:0x0452, B:193:0x0478, B:195:0x0481, B:201:0x048c, B:204:0x049c, B:205:0x04be, B:208:0x045c, B:210:0x0466, B:211:0x0475, B:212:0x046b, B:216:0x04c3, B:218:0x04cd, B:220:0x04d5, B:221:0x04d8, B:223:0x04e3, B:224:0x04e7, B:233:0x04f2, B:226:0x04f9, B:230:0x0502, B:231:0x0509, B:238:0x050e, B:240:0x0513, B:243:0x051e, B:245:0x0526, B:247:0x0544, B:248:0x054a, B:251:0x0550, B:252:0x0556, B:254:0x055e, B:256:0x0570, B:259:0x0578, B:261:0x057c, B:262:0x0583, B:264:0x0588, B:265:0x058b, B:276:0x0593, B:267:0x059d, B:270:0x05a7, B:271:0x05ac, B:273:0x05b1, B:274:0x05cb, B:282:0x0537, B:284:0x05cc, B:292:0x05de, B:286:0x05e5, B:289:0x05f0, B:290:0x0612, B:296:0x00be, B:297:0x00e0, B:361:0x00e3, B:363:0x00ee, B:365:0x00f2, B:367:0x00f8, B:369:0x00fe, B:371:0x0102, B:301:0x0111, B:303:0x0119, B:307:0x0129, B:308:0x0143, B:310:0x0144, B:311:0x014b, B:317:0x0158, B:319:0x015e, B:321:0x0165, B:322:0x016e, B:324:0x0176, B:326:0x017b, B:330:0x0183, B:331:0x019d, B:332:0x016a, B:334:0x019e, B:335:0x01b8, B:342:0x01c2, B:344:0x01ca, B:348:0x01db, B:349:0x01fd, B:351:0x01fe, B:352:0x0205, B:353:0x0206, B:355:0x0613, B:356:0x061a, B:358:0x061b, B:359:0x0622), top: B:28:0x007f, inners: #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:261:0x057c A[Catch: all -> 0x0623, TryCatch #0 {all -> 0x0623, blocks: (B:29:0x007f, B:32:0x0092, B:36:0x00ac, B:40:0x0210, B:41:0x0216, B:43:0x0221, B:45:0x0229, B:52:0x023d, B:54:0x024b, B:56:0x025e, B:58:0x026b, B:60:0x0278, B:62:0x027b, B:64:0x0285, B:65:0x0293, B:67:0x0299, B:70:0x02a7, B:73:0x02af, B:82:0x02be, B:83:0x02c4, B:85:0x02cc, B:86:0x02d1, B:91:0x02da, B:92:0x02e1, B:93:0x02e2, B:96:0x02ec, B:98:0x02f0, B:100:0x02f8, B:101:0x02fb, B:103:0x0301, B:106:0x030e, B:112:0x0328, B:113:0x032c, B:116:0x0251, B:121:0x033c, B:124:0x0344, B:126:0x034e, B:128:0x035f, B:130:0x0363, B:132:0x036b, B:135:0x0370, B:137:0x0374, B:138:0x03c4, B:140:0x03cc, B:143:0x03d5, B:144:0x03dc, B:146:0x0379, B:148:0x0381, B:150:0x0385, B:151:0x0388, B:152:0x0394, B:155:0x039d, B:157:0x03a1, B:159:0x03a4, B:161:0x03a8, B:162:0x03ac, B:163:0x03b8, B:164:0x03dd, B:165:0x03fb, B:168:0x03ff, B:170:0x0403, B:172:0x0409, B:174:0x040f, B:175:0x0413, B:180:0x041d, B:186:0x042d, B:188:0x043c, B:190:0x0447, B:191:0x044f, B:192:0x0452, B:193:0x0478, B:195:0x0481, B:201:0x048c, B:204:0x049c, B:205:0x04be, B:208:0x045c, B:210:0x0466, B:211:0x0475, B:212:0x046b, B:216:0x04c3, B:218:0x04cd, B:220:0x04d5, B:221:0x04d8, B:223:0x04e3, B:224:0x04e7, B:233:0x04f2, B:226:0x04f9, B:230:0x0502, B:231:0x0509, B:238:0x050e, B:240:0x0513, B:243:0x051e, B:245:0x0526, B:247:0x0544, B:248:0x054a, B:251:0x0550, B:252:0x0556, B:254:0x055e, B:256:0x0570, B:259:0x0578, B:261:0x057c, B:262:0x0583, B:264:0x0588, B:265:0x058b, B:276:0x0593, B:267:0x059d, B:270:0x05a7, B:271:0x05ac, B:273:0x05b1, B:274:0x05cb, B:282:0x0537, B:284:0x05cc, B:292:0x05de, B:286:0x05e5, B:289:0x05f0, B:290:0x0612, B:296:0x00be, B:297:0x00e0, B:361:0x00e3, B:363:0x00ee, B:365:0x00f2, B:367:0x00f8, B:369:0x00fe, B:371:0x0102, B:301:0x0111, B:303:0x0119, B:307:0x0129, B:308:0x0143, B:310:0x0144, B:311:0x014b, B:317:0x0158, B:319:0x015e, B:321:0x0165, B:322:0x016e, B:324:0x0176, B:326:0x017b, B:330:0x0183, B:331:0x019d, B:332:0x016a, B:334:0x019e, B:335:0x01b8, B:342:0x01c2, B:344:0x01ca, B:348:0x01db, B:349:0x01fd, B:351:0x01fe, B:352:0x0205, B:353:0x0206, B:355:0x0613, B:356:0x061a, B:358:0x061b, B:359:0x0622), top: B:28:0x007f, inners: #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:264:0x0588 A[Catch: all -> 0x0623, TryCatch #0 {all -> 0x0623, blocks: (B:29:0x007f, B:32:0x0092, B:36:0x00ac, B:40:0x0210, B:41:0x0216, B:43:0x0221, B:45:0x0229, B:52:0x023d, B:54:0x024b, B:56:0x025e, B:58:0x026b, B:60:0x0278, B:62:0x027b, B:64:0x0285, B:65:0x0293, B:67:0x0299, B:70:0x02a7, B:73:0x02af, B:82:0x02be, B:83:0x02c4, B:85:0x02cc, B:86:0x02d1, B:91:0x02da, B:92:0x02e1, B:93:0x02e2, B:96:0x02ec, B:98:0x02f0, B:100:0x02f8, B:101:0x02fb, B:103:0x0301, B:106:0x030e, B:112:0x0328, B:113:0x032c, B:116:0x0251, B:121:0x033c, B:124:0x0344, B:126:0x034e, B:128:0x035f, B:130:0x0363, B:132:0x036b, B:135:0x0370, B:137:0x0374, B:138:0x03c4, B:140:0x03cc, B:143:0x03d5, B:144:0x03dc, B:146:0x0379, B:148:0x0381, B:150:0x0385, B:151:0x0388, B:152:0x0394, B:155:0x039d, B:157:0x03a1, B:159:0x03a4, B:161:0x03a8, B:162:0x03ac, B:163:0x03b8, B:164:0x03dd, B:165:0x03fb, B:168:0x03ff, B:170:0x0403, B:172:0x0409, B:174:0x040f, B:175:0x0413, B:180:0x041d, B:186:0x042d, B:188:0x043c, B:190:0x0447, B:191:0x044f, B:192:0x0452, B:193:0x0478, B:195:0x0481, B:201:0x048c, B:204:0x049c, B:205:0x04be, B:208:0x045c, B:210:0x0466, B:211:0x0475, B:212:0x046b, B:216:0x04c3, B:218:0x04cd, B:220:0x04d5, B:221:0x04d8, B:223:0x04e3, B:224:0x04e7, B:233:0x04f2, B:226:0x04f9, B:230:0x0502, B:231:0x0509, B:238:0x050e, B:240:0x0513, B:243:0x051e, B:245:0x0526, B:247:0x0544, B:248:0x054a, B:251:0x0550, B:252:0x0556, B:254:0x055e, B:256:0x0570, B:259:0x0578, B:261:0x057c, B:262:0x0583, B:264:0x0588, B:265:0x058b, B:276:0x0593, B:267:0x059d, B:270:0x05a7, B:271:0x05ac, B:273:0x05b1, B:274:0x05cb, B:282:0x0537, B:284:0x05cc, B:292:0x05de, B:286:0x05e5, B:289:0x05f0, B:290:0x0612, B:296:0x00be, B:297:0x00e0, B:361:0x00e3, B:363:0x00ee, B:365:0x00f2, B:367:0x00f8, B:369:0x00fe, B:371:0x0102, B:301:0x0111, B:303:0x0119, B:307:0x0129, B:308:0x0143, B:310:0x0144, B:311:0x014b, B:317:0x0158, B:319:0x015e, B:321:0x0165, B:322:0x016e, B:324:0x0176, B:326:0x017b, B:330:0x0183, B:331:0x019d, B:332:0x016a, B:334:0x019e, B:335:0x01b8, B:342:0x01c2, B:344:0x01ca, B:348:0x01db, B:349:0x01fd, B:351:0x01fe, B:352:0x0205, B:353:0x0206, B:355:0x0613, B:356:0x061a, B:358:0x061b, B:359:0x0622), top: B:28:0x007f, inners: #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:267:0x059d A[Catch: all -> 0x0623, TRY_ENTER, TryCatch #0 {all -> 0x0623, blocks: (B:29:0x007f, B:32:0x0092, B:36:0x00ac, B:40:0x0210, B:41:0x0216, B:43:0x0221, B:45:0x0229, B:52:0x023d, B:54:0x024b, B:56:0x025e, B:58:0x026b, B:60:0x0278, B:62:0x027b, B:64:0x0285, B:65:0x0293, B:67:0x0299, B:70:0x02a7, B:73:0x02af, B:82:0x02be, B:83:0x02c4, B:85:0x02cc, B:86:0x02d1, B:91:0x02da, B:92:0x02e1, B:93:0x02e2, B:96:0x02ec, B:98:0x02f0, B:100:0x02f8, B:101:0x02fb, B:103:0x0301, B:106:0x030e, B:112:0x0328, B:113:0x032c, B:116:0x0251, B:121:0x033c, B:124:0x0344, B:126:0x034e, B:128:0x035f, B:130:0x0363, B:132:0x036b, B:135:0x0370, B:137:0x0374, B:138:0x03c4, B:140:0x03cc, B:143:0x03d5, B:144:0x03dc, B:146:0x0379, B:148:0x0381, B:150:0x0385, B:151:0x0388, B:152:0x0394, B:155:0x039d, B:157:0x03a1, B:159:0x03a4, B:161:0x03a8, B:162:0x03ac, B:163:0x03b8, B:164:0x03dd, B:165:0x03fb, B:168:0x03ff, B:170:0x0403, B:172:0x0409, B:174:0x040f, B:175:0x0413, B:180:0x041d, B:186:0x042d, B:188:0x043c, B:190:0x0447, B:191:0x044f, B:192:0x0452, B:193:0x0478, B:195:0x0481, B:201:0x048c, B:204:0x049c, B:205:0x04be, B:208:0x045c, B:210:0x0466, B:211:0x0475, B:212:0x046b, B:216:0x04c3, B:218:0x04cd, B:220:0x04d5, B:221:0x04d8, B:223:0x04e3, B:224:0x04e7, B:233:0x04f2, B:226:0x04f9, B:230:0x0502, B:231:0x0509, B:238:0x050e, B:240:0x0513, B:243:0x051e, B:245:0x0526, B:247:0x0544, B:248:0x054a, B:251:0x0550, B:252:0x0556, B:254:0x055e, B:256:0x0570, B:259:0x0578, B:261:0x057c, B:262:0x0583, B:264:0x0588, B:265:0x058b, B:276:0x0593, B:267:0x059d, B:270:0x05a7, B:271:0x05ac, B:273:0x05b1, B:274:0x05cb, B:282:0x0537, B:284:0x05cc, B:292:0x05de, B:286:0x05e5, B:289:0x05f0, B:290:0x0612, B:296:0x00be, B:297:0x00e0, B:361:0x00e3, B:363:0x00ee, B:365:0x00f2, B:367:0x00f8, B:369:0x00fe, B:371:0x0102, B:301:0x0111, B:303:0x0119, B:307:0x0129, B:308:0x0143, B:310:0x0144, B:311:0x014b, B:317:0x0158, B:319:0x015e, B:321:0x0165, B:322:0x016e, B:324:0x0176, B:326:0x017b, B:330:0x0183, B:331:0x019d, B:332:0x016a, B:334:0x019e, B:335:0x01b8, B:342:0x01c2, B:344:0x01ca, B:348:0x01db, B:349:0x01fd, B:351:0x01fe, B:352:0x0205, B:353:0x0206, B:355:0x0613, B:356:0x061a, B:358:0x061b, B:359:0x0622), top: B:28:0x007f, inners: #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:275:0x0593 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0210 A[Catch: all -> 0x0623, TryCatch #0 {all -> 0x0623, blocks: (B:29:0x007f, B:32:0x0092, B:36:0x00ac, B:40:0x0210, B:41:0x0216, B:43:0x0221, B:45:0x0229, B:52:0x023d, B:54:0x024b, B:56:0x025e, B:58:0x026b, B:60:0x0278, B:62:0x027b, B:64:0x0285, B:65:0x0293, B:67:0x0299, B:70:0x02a7, B:73:0x02af, B:82:0x02be, B:83:0x02c4, B:85:0x02cc, B:86:0x02d1, B:91:0x02da, B:92:0x02e1, B:93:0x02e2, B:96:0x02ec, B:98:0x02f0, B:100:0x02f8, B:101:0x02fb, B:103:0x0301, B:106:0x030e, B:112:0x0328, B:113:0x032c, B:116:0x0251, B:121:0x033c, B:124:0x0344, B:126:0x034e, B:128:0x035f, B:130:0x0363, B:132:0x036b, B:135:0x0370, B:137:0x0374, B:138:0x03c4, B:140:0x03cc, B:143:0x03d5, B:144:0x03dc, B:146:0x0379, B:148:0x0381, B:150:0x0385, B:151:0x0388, B:152:0x0394, B:155:0x039d, B:157:0x03a1, B:159:0x03a4, B:161:0x03a8, B:162:0x03ac, B:163:0x03b8, B:164:0x03dd, B:165:0x03fb, B:168:0x03ff, B:170:0x0403, B:172:0x0409, B:174:0x040f, B:175:0x0413, B:180:0x041d, B:186:0x042d, B:188:0x043c, B:190:0x0447, B:191:0x044f, B:192:0x0452, B:193:0x0478, B:195:0x0481, B:201:0x048c, B:204:0x049c, B:205:0x04be, B:208:0x045c, B:210:0x0466, B:211:0x0475, B:212:0x046b, B:216:0x04c3, B:218:0x04cd, B:220:0x04d5, B:221:0x04d8, B:223:0x04e3, B:224:0x04e7, B:233:0x04f2, B:226:0x04f9, B:230:0x0502, B:231:0x0509, B:238:0x050e, B:240:0x0513, B:243:0x051e, B:245:0x0526, B:247:0x0544, B:248:0x054a, B:251:0x0550, B:252:0x0556, B:254:0x055e, B:256:0x0570, B:259:0x0578, B:261:0x057c, B:262:0x0583, B:264:0x0588, B:265:0x058b, B:276:0x0593, B:267:0x059d, B:270:0x05a7, B:271:0x05ac, B:273:0x05b1, B:274:0x05cb, B:282:0x0537, B:284:0x05cc, B:292:0x05de, B:286:0x05e5, B:289:0x05f0, B:290:0x0612, B:296:0x00be, B:297:0x00e0, B:361:0x00e3, B:363:0x00ee, B:365:0x00f2, B:367:0x00f8, B:369:0x00fe, B:371:0x0102, B:301:0x0111, B:303:0x0119, B:307:0x0129, B:308:0x0143, B:310:0x0144, B:311:0x014b, B:317:0x0158, B:319:0x015e, B:321:0x0165, B:322:0x016e, B:324:0x0176, B:326:0x017b, B:330:0x0183, B:331:0x019d, B:332:0x016a, B:334:0x019e, B:335:0x01b8, B:342:0x01c2, B:344:0x01ca, B:348:0x01db, B:349:0x01fd, B:351:0x01fe, B:352:0x0205, B:353:0x0206, B:355:0x0613, B:356:0x061a, B:358:0x061b, B:359:0x0622), top: B:28:0x007f, inners: #2, #3 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object parseObject(java.util.Map r19, java.lang.Object r20) {
        /*
            Method dump skipped, instructions count: 1579
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.DefaultJSONParser.parseObject(java.util.Map, java.lang.Object):java.lang.Object");
    }

    public void parseObject(Object obj) {
        Object deserialze;
        Class<?> cls = obj.getClass();
        ObjectDeserializer deserializer = this.config.getDeserializer(cls);
        JavaBeanDeserializer javaBeanDeserializer = deserializer instanceof JavaBeanDeserializer ? (JavaBeanDeserializer) deserializer : null;
        if (this.lexer.token() != 12 && this.lexer.token() != 16) {
            throw new JSONException("syntax error, expect {, actual " + this.lexer.tokenName());
        }
        while (true) {
            String scanSymbol = this.lexer.scanSymbol(this.symbolTable);
            if (scanSymbol == null) {
                if (this.lexer.token() == 13) {
                    this.lexer.nextToken(16);
                    return;
                } else if (this.lexer.token() == 16 && this.lexer.isEnabled(Feature.AllowArbitraryCommas)) {
                }
            }
            FieldDeserializer fieldDeserializer = javaBeanDeserializer != null ? javaBeanDeserializer.getFieldDeserializer(scanSymbol) : null;
            if (fieldDeserializer != null) {
                Class<?> cls2 = fieldDeserializer.fieldInfo.fieldClass;
                Type type = fieldDeserializer.fieldInfo.fieldType;
                if (cls2 == Integer.TYPE) {
                    this.lexer.nextTokenWithColon(2);
                    deserialze = IntegerCodec.instance.deserialze(this, type, null);
                } else if (cls2 == String.class) {
                    this.lexer.nextTokenWithColon(4);
                    deserialze = StringCodec.deserialze(this);
                } else if (cls2 == Long.TYPE) {
                    this.lexer.nextTokenWithColon(2);
                    deserialze = LongCodec.instance.deserialze(this, type, null);
                } else {
                    ObjectDeserializer deserializer2 = this.config.getDeserializer(cls2, type);
                    this.lexer.nextTokenWithColon(deserializer2.getFastMatchToken());
                    deserialze = deserializer2.deserialze(this, type, null);
                }
                fieldDeserializer.setValue(obj, deserialze);
                if (this.lexer.token() != 16 && this.lexer.token() == 13) {
                    this.lexer.nextToken(16);
                    return;
                }
            } else {
                if (!this.lexer.isEnabled(Feature.IgnoreNotMatch)) {
                    throw new JSONException("setter not found, class " + cls.getName() + ", property " + scanSymbol);
                }
                this.lexer.nextTokenWithColon();
                parse();
                if (this.lexer.token() == 13) {
                    this.lexer.nextToken();
                    return;
                }
            }
        }
    }

    public void popContext() {
        if (this.lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            return;
        }
        this.context = this.context.parent;
        if (this.contextArrayIndex <= 0) {
            return;
        }
        this.contextArrayIndex--;
        this.contextArray[this.contextArrayIndex] = null;
    }

    public Object resolveReference(String str) {
        if (this.contextArray == null) {
            return null;
        }
        for (int i = 0; i < this.contextArray.length && i < this.contextArrayIndex; i++) {
            ParseContext parseContext = this.contextArray[i];
            if (parseContext.toString().equals(str)) {
                return parseContext.object;
            }
        }
        return null;
    }

    public void setConfig(ParserConfig parserConfig) {
        this.config = parserConfig;
    }

    public ParseContext setContext(ParseContext parseContext, Object obj, Object obj2) {
        if (this.lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            return null;
        }
        this.context = new ParseContext(parseContext, obj, obj2);
        addContext(this.context);
        return this.context;
    }

    public ParseContext setContext(Object obj, Object obj2) {
        if (this.lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            return null;
        }
        return setContext(this.context, obj, obj2);
    }

    public void setContext(ParseContext parseContext) {
        if (this.lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            return;
        }
        this.context = parseContext;
    }

    public void setDateFomrat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public void setDateFormat(String str) {
        this.dateFormatPattern = str;
        this.dateFormat = null;
    }

    public void setFieldTypeResolver(FieldTypeResolver fieldTypeResolver) {
        this.fieldTypeResolver = fieldTypeResolver;
    }

    public void setResolveStatus(int i) {
        this.resolveStatus = i;
    }

    public void throwException(int i) {
        throw new JSONException("syntax error, expect " + JSONToken.name(i) + ", actual " + JSONToken.name(this.lexer.token()));
    }
}
