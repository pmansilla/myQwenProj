package com.alibaba.fastjson;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.FieldSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.util.IOUtils;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;
import me.panpf.sketch.uri.FileUriModel;

/* loaded from: classes.dex */
public class JSONPath implements JSONAware {
    static final long LENGTH = -1580386065683472715L;
    static final long SIZE = 5614464919154503228L;
    private static ConcurrentMap<String, JSONPath> pathCache = new ConcurrentHashMap(128, 0.75f, 1);
    private ParserConfig parserConfig;
    private final String path;
    private Segement[] segments;
    private SerializeConfig serializeConfig;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class ArrayAccessSegement implements Segement {
        private final int index;

        public ArrayAccessSegement(int i) {
            this.index = i;
        }

        @Override // com.alibaba.fastjson.JSONPath.Segement
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            return jSONPath.getArrayItem(obj2, this.index);
        }

        public boolean remove(JSONPath jSONPath, Object obj) {
            return jSONPath.removeArrayItem(jSONPath, obj, this.index);
        }

        public boolean setValue(JSONPath jSONPath, Object obj, Object obj2) {
            return jSONPath.setArrayItem(jSONPath, obj, this.index, obj2);
        }
    }

    /* loaded from: classes.dex */
    static class CombineSegement implements Segement {
        public final Segement left;
        public final Operator operator;
        public final Segement right;

        /* loaded from: classes.dex */
        enum Operator {
            And,
            Or
        }

        public CombineSegement(Segement segement, Segement segement2, Operator operator) {
            this.left = segement;
            this.right = segement2;
            this.operator = operator;
        }

        @Override // com.alibaba.fastjson.JSONPath.Segement
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class DoubleOpSegement implements Filter {
        private final Operator op;
        private final String propertyName;
        private final long propertyNameHash;
        private final double value;

        public DoubleOpSegement(String str, double d, Operator operator) {
            this.propertyName = str;
            this.value = d;
            this.op = operator;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object propertyValue = jSONPath.getPropertyValue(obj3, this.propertyName, this.propertyNameHash);
            if (propertyValue == null || !(propertyValue instanceof Number)) {
                return false;
            }
            double doubleValue = ((Number) propertyValue).doubleValue();
            return this.op == Operator.EQ ? doubleValue == this.value : this.op == Operator.NE ? doubleValue != this.value : this.op == Operator.GE ? doubleValue >= this.value : this.op == Operator.GT ? doubleValue > this.value : this.op == Operator.LE ? doubleValue <= this.value : this.op == Operator.LT && doubleValue < this.value;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface Filter {
        boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class FilterGroup implements Filter {
        private boolean and;
        private List<Filter> fitlers;

        public FilterGroup(Filter filter, Filter filter2, boolean z) {
            this.fitlers = new ArrayList(2);
            this.fitlers.add(filter);
            this.fitlers.add(filter2);
            this.and = z;
        }

        public FilterGroup(List<Filter> list, boolean z) {
            this.and = z;
            this.fitlers = list;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            if (this.and) {
                Iterator<Filter> it = this.fitlers.iterator();
                while (it.hasNext()) {
                    if (!it.next().apply(jSONPath, obj, obj2, obj3)) {
                        return false;
                    }
                }
                return true;
            }
            Iterator<Filter> it2 = this.fitlers.iterator();
            while (it2.hasNext()) {
                if (it2.next().apply(jSONPath, obj, obj2, obj3)) {
                    return true;
                }
            }
            return false;
        }
    }

    /* loaded from: classes.dex */
    public static class FilterSegement implements Segement {
        private final Filter filter;

        public FilterSegement(Filter filter) {
            this.filter = filter;
        }

        @Override // com.alibaba.fastjson.JSONPath.Segement
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            if (obj2 == null) {
                return null;
            }
            JSONArray jSONArray = new JSONArray();
            if (!(obj2 instanceof Iterable)) {
                if (this.filter.apply(jSONPath, obj, obj2, obj2)) {
                    return obj2;
                }
                return null;
            }
            for (Object obj3 : (Iterable) obj2) {
                if (this.filter.apply(jSONPath, obj, obj2, obj3)) {
                    jSONArray.add(obj3);
                }
            }
            return jSONArray;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class IntBetweenSegement implements Filter {
        private final long endValue;
        private final boolean not;
        private final String propertyName;
        private final long propertyNameHash;
        private final long startValue;

        public IntBetweenSegement(String str, long j, long j2, boolean z) {
            this.propertyName = str;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
            this.startValue = j;
            this.endValue = j2;
            this.not = z;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object propertyValue = jSONPath.getPropertyValue(obj3, this.propertyName, this.propertyNameHash);
            if (propertyValue == null) {
                return false;
            }
            if (propertyValue instanceof Number) {
                long longValue = ((Number) propertyValue).longValue();
                if (longValue >= this.startValue && longValue <= this.endValue) {
                    return !this.not;
                }
            }
            return this.not;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class IntInSegement implements Filter {
        private final boolean not;
        private final String propertyName;
        private final long propertyNameHash;
        private final long[] values;

        public IntInSegement(String str, long[] jArr, boolean z) {
            this.propertyName = str;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
            this.values = jArr;
            this.not = z;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object propertyValue = jSONPath.getPropertyValue(obj3, this.propertyName, this.propertyNameHash);
            if (propertyValue == null) {
                return false;
            }
            if (propertyValue instanceof Number) {
                long longValue = ((Number) propertyValue).longValue();
                for (long j : this.values) {
                    if (j == longValue) {
                        return !this.not;
                    }
                }
            }
            return this.not;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class IntObjInSegement implements Filter {
        private final boolean not;
        private final String propertyName;
        private final long propertyNameHash;
        private final Long[] values;

        public IntObjInSegement(String str, Long[] lArr, boolean z) {
            this.propertyName = str;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
            this.values = lArr;
            this.not = z;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object propertyValue = jSONPath.getPropertyValue(obj3, this.propertyName, this.propertyNameHash);
            int i = 0;
            if (propertyValue == null) {
                Long[] lArr = this.values;
                int length = lArr.length;
                while (i < length) {
                    if (lArr[i] == null) {
                        return !this.not;
                    }
                    i++;
                }
                return this.not;
            }
            if (propertyValue instanceof Number) {
                long longValue = ((Number) propertyValue).longValue();
                Long[] lArr2 = this.values;
                int length2 = lArr2.length;
                while (i < length2) {
                    Long l = lArr2[i];
                    if (l != null && l.longValue() == longValue) {
                        return !this.not;
                    }
                    i++;
                }
            }
            return this.not;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class IntOpSegement implements Filter {
        private final Operator op;
        private final String propertyName;
        private final long propertyNameHash;
        private final long value;

        public IntOpSegement(String str, long j, Operator operator) {
            this.propertyName = str;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
            this.value = j;
            this.op = operator;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object propertyValue = jSONPath.getPropertyValue(obj3, this.propertyName, this.propertyNameHash);
            if (propertyValue == null || !(propertyValue instanceof Number)) {
                return false;
            }
            long longValue = ((Number) propertyValue).longValue();
            return this.op == Operator.EQ ? longValue == this.value : this.op == Operator.NE ? longValue != this.value : this.op == Operator.GE ? longValue >= this.value : this.op == Operator.GT ? longValue > this.value : this.op == Operator.LE ? longValue <= this.value : this.op == Operator.LT && longValue < this.value;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class JSONPathParser {
        private char ch;
        private int level;
        private final String path;
        private int pos;

        public JSONPathParser(String str) {
            this.path = str;
            next();
        }

        static boolean isDigitFirst(char c) {
            return c == '-' || c == '+' || (c >= '0' && c <= '9');
        }

        void accept(char c) {
            if (this.ch == c) {
                if (isEOF()) {
                    return;
                }
                next();
            } else {
                throw new JSONPathException("expect '" + c + ", but '" + this.ch + "'");
            }
        }

        Segement buildArraySegement(String str) {
            int length = str.length();
            int i = 0;
            char charAt = str.charAt(0);
            int i2 = length - 1;
            char charAt2 = str.charAt(i2);
            int indexOf = str.indexOf(44);
            if (str.length() > 2 && charAt == '\'' && charAt2 == '\'') {
                if (indexOf == -1) {
                    return new PropertySegement(str.substring(1, i2), false);
                }
                String[] split = str.split(",");
                String[] strArr = new String[split.length];
                while (i < split.length) {
                    String str2 = split[i];
                    strArr[i] = str2.substring(1, str2.length() - 1);
                    i++;
                }
                return new MultiPropertySegement(strArr);
            }
            int indexOf2 = str.indexOf(58);
            if (indexOf == -1 && indexOf2 == -1) {
                if (!TypeUtils.isNumber(str)) {
                    return new PropertySegement(str, false);
                }
                try {
                    return new ArrayAccessSegement(Integer.parseInt(str));
                } catch (NumberFormatException unused) {
                    return new PropertySegement(str, false);
                }
            }
            if (indexOf != -1) {
                String[] split2 = str.split(",");
                int[] iArr = new int[split2.length];
                while (i < split2.length) {
                    iArr[i] = Integer.parseInt(split2[i]);
                    i++;
                }
                return new MultiIndexSegement(iArr);
            }
            if (indexOf2 == -1) {
                throw new UnsupportedOperationException();
            }
            String[] split3 = str.split(":");
            int[] iArr2 = new int[split3.length];
            for (int i3 = 0; i3 < split3.length; i3++) {
                String str3 = split3[i3];
                if (str3.length() != 0) {
                    iArr2[i3] = Integer.parseInt(str3);
                } else {
                    if (i3 != 0) {
                        throw new UnsupportedOperationException();
                    }
                    iArr2[i3] = 0;
                }
            }
            int i4 = iArr2[0];
            int i5 = iArr2.length > 1 ? iArr2[1] : -1;
            int i6 = iArr2.length == 3 ? iArr2[2] : 1;
            if (i5 < 0 || i5 >= i4) {
                if (i6 > 0) {
                    return new RangeSegement(i4, i5, i6);
                }
                throw new UnsupportedOperationException("step must greater than zero : " + i6);
            }
            throw new UnsupportedOperationException("end must greater than or equals start. start " + i4 + ",  end " + i5);
        }

        public Segement[] explain() {
            if (this.path == null || this.path.length() == 0) {
                throw new IllegalArgumentException();
            }
            Segement[] segementArr = new Segement[8];
            while (true) {
                Segement readSegement = readSegement();
                if (readSegement == null) {
                    break;
                }
                if (this.level == segementArr.length) {
                    Segement[] segementArr2 = new Segement[(this.level * 3) / 2];
                    System.arraycopy(segementArr, 0, segementArr2, 0, this.level);
                    segementArr = segementArr2;
                }
                int i = this.level;
                this.level = i + 1;
                segementArr[i] = readSegement;
            }
            if (this.level == segementArr.length) {
                return segementArr;
            }
            Segement[] segementArr3 = new Segement[this.level];
            System.arraycopy(segementArr, 0, segementArr3, 0, this.level);
            return segementArr3;
        }

        Filter filterRest(Filter filter) {
            boolean z = this.ch == '&';
            if ((this.ch != '&' || getNextChar() != '&') && (this.ch != '|' || getNextChar() != '|')) {
                return filter;
            }
            next();
            next();
            while (this.ch == ' ') {
                next();
            }
            return new FilterGroup(filter, (Filter) parseArrayAccessFilter(false), z);
        }

        char getNextChar() {
            return this.path.charAt(this.pos);
        }

        boolean isEOF() {
            return this.pos >= this.path.length();
        }

        void next() {
            String str = this.path;
            int i = this.pos;
            this.pos = i + 1;
            this.ch = str.charAt(i);
        }

        Segement parseArrayAccess(boolean z) {
            Object parseArrayAccessFilter = parseArrayAccessFilter(z);
            return parseArrayAccessFilter instanceof Segement ? (Segement) parseArrayAccessFilter : new FilterSegement((Filter) parseArrayAccessFilter);
        }

        /* JADX WARN: Code restructure failed: missing block: B:27:0x0066, code lost:
        
            r5 = r18.pos - 1;
         */
        /* JADX WARN: Removed duplicated region for block: B:228:0x0373 A[LOOP:7: B:226:0x036f->B:228:0x0373, LOOP_END] */
        /* JADX WARN: Removed duplicated region for block: B:235:0x0385  */
        /* JADX WARN: Removed duplicated region for block: B:237:0x038a  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        java.lang.Object parseArrayAccessFilter(boolean r19) {
            /*
                Method dump skipped, instructions count: 1537
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.JSONPath.JSONPathParser.parseArrayAccessFilter(boolean):java.lang.Object");
        }

        protected double readDoubleValue(long j) {
            int i = this.pos - 1;
            next();
            while (this.ch >= '0' && this.ch <= '9') {
                next();
            }
            double parseDouble = Double.parseDouble(this.path.substring(i, this.pos - 1));
            double d = j;
            Double.isNaN(d);
            return parseDouble + d;
        }

        protected long readLongValue() {
            int i = this.pos - 1;
            if (this.ch == '+' || this.ch == '-') {
                next();
            }
            while (this.ch >= '0' && this.ch <= '9') {
                next();
            }
            return Long.parseLong(this.path.substring(i, this.pos - 1));
        }

        String readName() {
            skipWhitespace();
            if (this.ch != '\\' && !IOUtils.firstIdentifier(this.ch)) {
                throw new JSONPathException("illeal jsonpath syntax. " + this.path);
            }
            StringBuilder sb = new StringBuilder();
            while (!isEOF()) {
                if (this.ch == '\\') {
                    next();
                    sb.append(this.ch);
                    if (isEOF()) {
                        break;
                    }
                    next();
                } else {
                    if (!IOUtils.isIdent(this.ch)) {
                        break;
                    }
                    sb.append(this.ch);
                    next();
                }
            }
            if (isEOF() && IOUtils.isIdent(this.ch)) {
                sb.append(this.ch);
            }
            return sb.toString();
        }

        protected Operator readOp() {
            Operator operator;
            if (this.ch == '=') {
                next();
                operator = Operator.EQ;
            } else if (this.ch == '!') {
                next();
                accept('=');
                operator = Operator.NE;
            } else if (this.ch == '<') {
                next();
                if (this.ch == '=') {
                    next();
                    operator = Operator.LE;
                } else {
                    operator = Operator.LT;
                }
            } else if (this.ch == '>') {
                next();
                if (this.ch == '=') {
                    next();
                    operator = Operator.GE;
                } else {
                    operator = Operator.GT;
                }
            } else {
                operator = null;
            }
            if (operator != null) {
                return operator;
            }
            String readName = readName();
            if (!"not".equalsIgnoreCase(readName)) {
                if ("like".equalsIgnoreCase(readName)) {
                    return Operator.LIKE;
                }
                if ("rlike".equalsIgnoreCase(readName)) {
                    return Operator.RLIKE;
                }
                if ("in".equalsIgnoreCase(readName)) {
                    return Operator.IN;
                }
                if ("between".equalsIgnoreCase(readName)) {
                    return Operator.BETWEEN;
                }
                throw new UnsupportedOperationException();
            }
            skipWhitespace();
            String readName2 = readName();
            if ("like".equalsIgnoreCase(readName2)) {
                return Operator.NOT_LIKE;
            }
            if ("rlike".equalsIgnoreCase(readName2)) {
                return Operator.NOT_RLIKE;
            }
            if ("in".equalsIgnoreCase(readName2)) {
                return Operator.NOT_IN;
            }
            if ("between".equalsIgnoreCase(readName2)) {
                return Operator.NOT_BETWEEN;
            }
            throw new UnsupportedOperationException();
        }

        Segement readSegement() {
            boolean z = true;
            if (this.level == 0 && this.path.length() == 1) {
                if (isDigitFirst(this.ch)) {
                    return new ArrayAccessSegement(this.ch - '0');
                }
                if ((this.ch >= 'a' && this.ch <= 'z') || (this.ch >= 'A' && this.ch <= 'Z')) {
                    return new PropertySegement(Character.toString(this.ch), false);
                }
            }
            while (!isEOF()) {
                skipWhitespace();
                if (this.ch != '$') {
                    if (this.ch != '.' && this.ch != '/') {
                        if (this.ch == '[') {
                            return parseArrayAccess(true);
                        }
                        if (this.level == 0) {
                            return new PropertySegement(readName(), false);
                        }
                        throw new JSONPathException("not support jsonpath : " + this.path);
                    }
                    char c = this.ch;
                    next();
                    if (c == '.' && this.ch == '.') {
                        next();
                        if (this.path.length() > this.pos + 3 && this.ch == '[' && this.path.charAt(this.pos) == '*' && this.path.charAt(this.pos + 1) == ']' && this.path.charAt(this.pos + 2) == '.') {
                            next();
                            next();
                            next();
                            next();
                        }
                    } else {
                        z = false;
                    }
                    if (this.ch == '*') {
                        if (!isEOF()) {
                            next();
                        }
                        return WildCardSegement.instance;
                    }
                    if (isDigitFirst(this.ch)) {
                        return parseArrayAccess(false);
                    }
                    String readName = readName();
                    if (this.ch != '(') {
                        return new PropertySegement(readName, z);
                    }
                    next();
                    if (this.ch != ')') {
                        throw new JSONPathException("not support jsonpath : " + this.path);
                    }
                    if (!isEOF()) {
                        next();
                    }
                    if ("size".equals(readName) || "length".equals(readName)) {
                        return SizeSegement.instance;
                    }
                    throw new JSONPathException("not support jsonpath : " + this.path);
                }
                next();
            }
            return null;
        }

        String readString() {
            char c = this.ch;
            next();
            int i = this.pos - 1;
            while (this.ch != c && !isEOF()) {
                next();
            }
            String substring = this.path.substring(i, isEOF() ? this.pos : this.pos - 1);
            accept(c);
            return substring;
        }

        protected Object readValue() {
            skipWhitespace();
            if (isDigitFirst(this.ch)) {
                return Long.valueOf(readLongValue());
            }
            if (this.ch == '\"' || this.ch == '\'') {
                return readString();
            }
            if (this.ch != 'n') {
                throw new UnsupportedOperationException();
            }
            if ("null".equals(readName())) {
                return null;
            }
            throw new JSONPathException(this.path);
        }

        public final void skipWhitespace() {
            while (this.ch <= ' ') {
                if (this.ch != ' ' && this.ch != '\r' && this.ch != '\n' && this.ch != '\t' && this.ch != '\f' && this.ch != '\b') {
                    return;
                } else {
                    next();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class MatchSegement implements Filter {
        private final String[] containsValues;
        private final String endsWithValue;
        private final int minLength;
        private final boolean not;
        private final String propertyName;
        private final long propertyNameHash;
        private final String startsWithValue;

        public MatchSegement(String str, String str2, String str3, String[] strArr, boolean z) {
            this.propertyName = str;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
            this.startsWithValue = str2;
            this.endsWithValue = str3;
            this.containsValues = strArr;
            this.not = z;
            int length = str2 != null ? str2.length() + 0 : 0;
            length = str3 != null ? length + str3.length() : length;
            if (strArr != null) {
                for (String str4 : strArr) {
                    length += str4.length();
                }
            }
            this.minLength = length;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            int i;
            Object propertyValue = jSONPath.getPropertyValue(obj3, this.propertyName, this.propertyNameHash);
            if (propertyValue == null) {
                return false;
            }
            String obj4 = propertyValue.toString();
            if (obj4.length() < this.minLength) {
                return this.not;
            }
            if (this.startsWithValue == null) {
                i = 0;
            } else {
                if (!obj4.startsWith(this.startsWithValue)) {
                    return this.not;
                }
                i = this.startsWithValue.length() + 0;
            }
            if (this.containsValues != null) {
                for (String str : this.containsValues) {
                    int indexOf = obj4.indexOf(str, i);
                    if (indexOf == -1) {
                        return this.not;
                    }
                    i = indexOf + str.length();
                }
            }
            return (this.endsWithValue == null || obj4.endsWith(this.endsWithValue)) ? !this.not : this.not;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class MultiIndexSegement implements Segement {
        private final int[] indexes;

        public MultiIndexSegement(int[] iArr) {
            this.indexes = iArr;
        }

        @Override // com.alibaba.fastjson.JSONPath.Segement
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            ArrayList arrayList = new ArrayList(this.indexes.length);
            for (int i = 0; i < this.indexes.length; i++) {
                arrayList.add(jSONPath.getArrayItem(obj2, this.indexes[i]));
            }
            return arrayList;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class MultiPropertySegement implements Segement {
        private final String[] propertyNames;
        private final long[] propertyNamesHash;

        public MultiPropertySegement(String[] strArr) {
            this.propertyNames = strArr;
            this.propertyNamesHash = new long[strArr.length];
            for (int i = 0; i < this.propertyNamesHash.length; i++) {
                this.propertyNamesHash[i] = TypeUtils.fnv1a_64(strArr[i]);
            }
        }

        @Override // com.alibaba.fastjson.JSONPath.Segement
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            ArrayList arrayList = new ArrayList(this.propertyNames.length);
            for (int i = 0; i < this.propertyNames.length; i++) {
                arrayList.add(jSONPath.getPropertyValue(obj2, this.propertyNames[i], this.propertyNamesHash[i]));
            }
            return arrayList;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class NotNullSegement implements Filter {
        private final String propertyName;
        private final long propertyNameHash;

        public NotNullSegement(String str) {
            this.propertyName = str;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            return jSONPath.getPropertyValue(obj3, this.propertyName, this.propertyNameHash) != null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class NullSegement implements Filter {
        private final String propertyName;
        private final long propertyNameHash;

        public NullSegement(String str) {
            this.propertyName = str;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            return jSONPath.getPropertyValue(obj3, this.propertyName, this.propertyNameHash) == null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum Operator {
        EQ,
        NE,
        GT,
        GE,
        LT,
        LE,
        LIKE,
        NOT_LIKE,
        RLIKE,
        NOT_RLIKE,
        IN,
        NOT_IN,
        BETWEEN,
        NOT_BETWEEN,
        And,
        Or
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class PropertySegement implements Segement {
        private final boolean deep;
        private final String propertyName;
        private final long propertyNameHash;

        public PropertySegement(String str, boolean z) {
            this.propertyName = str;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
            this.deep = z;
        }

        @Override // com.alibaba.fastjson.JSONPath.Segement
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            if (!this.deep) {
                return jSONPath.getPropertyValue(obj2, this.propertyName, this.propertyNameHash);
            }
            ArrayList arrayList = new ArrayList();
            jSONPath.deepScan(obj2, this.propertyName, arrayList);
            return arrayList;
        }

        public boolean remove(JSONPath jSONPath, Object obj) {
            return jSONPath.removePropertyValue(obj, this.propertyName);
        }

        public void setValue(JSONPath jSONPath, Object obj, Object obj2) {
            if (this.deep) {
                jSONPath.deepSet(obj, this.propertyName, this.propertyNameHash, obj2);
            } else {
                jSONPath.setPropertyValue(obj, this.propertyName, this.propertyNameHash, obj2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class RangeSegement implements Segement {
        private final int end;
        private final int start;
        private final int step;

        public RangeSegement(int i, int i2, int i3) {
            this.start = i;
            this.end = i2;
            this.step = i3;
        }

        @Override // com.alibaba.fastjson.JSONPath.Segement
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            int intValue = SizeSegement.instance.eval(jSONPath, obj, obj2).intValue();
            int i = this.start >= 0 ? this.start : this.start + intValue;
            int i2 = this.end >= 0 ? this.end : this.end + intValue;
            int i3 = ((i2 - i) / this.step) + 1;
            if (i3 == -1) {
                return null;
            }
            ArrayList arrayList = new ArrayList(i3);
            while (i <= i2 && i < intValue) {
                arrayList.add(jSONPath.getArrayItem(obj2, i));
                i += this.step;
            }
            return arrayList;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class RlikeSegement implements Filter {
        private final boolean not;
        private final Pattern pattern;
        private final String propertyName;
        private final long propertyNameHash;

        public RlikeSegement(String str, String str2, boolean z) {
            this.propertyName = str;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
            this.pattern = Pattern.compile(str2);
            this.not = z;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object propertyValue = jSONPath.getPropertyValue(obj3, this.propertyName, this.propertyNameHash);
            if (propertyValue == null) {
                return false;
            }
            boolean matches = this.pattern.matcher(propertyValue.toString()).matches();
            return this.not ? !matches : matches;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface Segement {
        Object eval(JSONPath jSONPath, Object obj, Object obj2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SizeSegement implements Segement {
        public static final SizeSegement instance = new SizeSegement();

        SizeSegement() {
        }

        @Override // com.alibaba.fastjson.JSONPath.Segement
        public Integer eval(JSONPath jSONPath, Object obj, Object obj2) {
            return Integer.valueOf(jSONPath.evalSize(obj2));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class StringInSegement implements Filter {
        private final boolean not;
        private final String propertyName;
        private final long propertyNameHash;
        private final String[] values;

        public StringInSegement(String str, String[] strArr, boolean z) {
            this.propertyName = str;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
            this.values = strArr;
            this.not = z;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object propertyValue = jSONPath.getPropertyValue(obj3, this.propertyName, this.propertyNameHash);
            for (String str : this.values) {
                if (str == propertyValue) {
                    return !this.not;
                }
                if (str != null && str.equals(propertyValue)) {
                    return !this.not;
                }
            }
            return this.not;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class StringOpSegement implements Filter {
        private final Operator op;
        private final String propertyName;
        private final long propertyNameHash;
        private final String value;

        public StringOpSegement(String str, String str2, Operator operator) {
            this.propertyName = str;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
            this.value = str2;
            this.op = operator;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object propertyValue = jSONPath.getPropertyValue(obj3, this.propertyName, this.propertyNameHash);
            if (this.op == Operator.EQ) {
                return this.value.equals(propertyValue);
            }
            if (this.op == Operator.NE) {
                return !this.value.equals(propertyValue);
            }
            if (propertyValue == null) {
                return false;
            }
            int compareTo = this.value.compareTo(propertyValue.toString());
            return this.op == Operator.GE ? compareTo <= 0 : this.op == Operator.GT ? compareTo < 0 : this.op == Operator.LE ? compareTo >= 0 : this.op == Operator.LT && compareTo > 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class ValueSegment implements Filter {
        private boolean eq;
        private final String propertyName;
        private final long propertyNameHash;
        private final Object value;

        public ValueSegment(String str, Object obj, boolean z) {
            this.eq = true;
            if (obj == null) {
                throw new IllegalArgumentException("value is null");
            }
            this.propertyName = str;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
            this.value = obj;
            this.eq = z;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            boolean equals = this.value.equals(jSONPath.getPropertyValue(obj3, this.propertyName, this.propertyNameHash));
            return !this.eq ? !equals : equals;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class WildCardSegement implements Segement {
        public static WildCardSegement instance = new WildCardSegement();

        WildCardSegement() {
        }

        @Override // com.alibaba.fastjson.JSONPath.Segement
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            return jSONPath.getPropertyValues(obj2);
        }
    }

    public JSONPath(String str) {
        this(str, SerializeConfig.getGlobalInstance(), ParserConfig.getGlobalInstance());
    }

    public JSONPath(String str, SerializeConfig serializeConfig, ParserConfig parserConfig) {
        if (str == null || str.length() == 0) {
            throw new JSONPathException("json-path can not be null or empty");
        }
        this.path = str;
        this.serializeConfig = serializeConfig;
        this.parserConfig = parserConfig;
    }

    public static void arrayAdd(Object obj, String str, Object... objArr) {
        compile(str).arrayAdd(obj, objArr);
    }

    public static JSONPath compile(String str) {
        if (str == null) {
            throw new JSONPathException("jsonpath can not be null");
        }
        JSONPath jSONPath = pathCache.get(str);
        if (jSONPath != null) {
            return jSONPath;
        }
        JSONPath jSONPath2 = new JSONPath(str);
        if (pathCache.size() >= 1024) {
            return jSONPath2;
        }
        pathCache.putIfAbsent(str, jSONPath2);
        return pathCache.get(str);
    }

    public static boolean contains(Object obj, String str) {
        if (obj == null) {
            return false;
        }
        return compile(str).contains(obj);
    }

    public static boolean containsValue(Object obj, String str, Object obj2) {
        return compile(str).containsValue(obj, obj2);
    }

    static boolean eq(Object obj, Object obj2) {
        if (obj == obj2) {
            return true;
        }
        if (obj == null || obj2 == null) {
            return false;
        }
        if (obj.getClass() != obj2.getClass() && (obj instanceof Number)) {
            if (obj2 instanceof Number) {
                return eqNotNull((Number) obj, (Number) obj2);
            }
            return false;
        }
        return obj.equals(obj2);
    }

    static boolean eqNotNull(Number number, Number number2) {
        Class<?> cls = number.getClass();
        boolean isInt = isInt(cls);
        Class<?> cls2 = number2.getClass();
        boolean isInt2 = isInt(cls2);
        if (number instanceof BigDecimal) {
            BigDecimal bigDecimal = (BigDecimal) number;
            if (isInt2) {
                return bigDecimal.equals(BigDecimal.valueOf(number2.longValue()));
            }
        }
        if (isInt) {
            if (isInt2) {
                return number.longValue() == number2.longValue();
            }
            if (number2 instanceof BigInteger) {
                return BigInteger.valueOf(number.longValue()).equals((BigInteger) number);
            }
        }
        if (isInt2 && (number instanceof BigInteger)) {
            return ((BigInteger) number).equals(BigInteger.valueOf(number2.longValue()));
        }
        boolean isDouble = isDouble(cls);
        boolean isDouble2 = isDouble(cls2);
        return ((isDouble && isDouble2) || ((isDouble && isInt2) || (isDouble2 && isInt))) && number.doubleValue() == number2.doubleValue();
    }

    public static Object eval(Object obj, String str) {
        return compile(str).eval(obj);
    }

    protected static boolean isDouble(Class<?> cls) {
        return cls == Float.class || cls == Double.class;
    }

    protected static boolean isInt(Class<?> cls) {
        return cls == Byte.class || cls == Short.class || cls == Integer.class || cls == Long.class;
    }

    public static Map<String, Object> paths(Object obj) {
        return paths(obj, SerializeConfig.globalInstance);
    }

    public static Map<String, Object> paths(Object obj, SerializeConfig serializeConfig) {
        IdentityHashMap identityHashMap = new IdentityHashMap();
        HashMap hashMap = new HashMap();
        paths(identityHashMap, hashMap, FileUriModel.SCHEME, obj, serializeConfig);
        return hashMap;
    }

    private static void paths(Map<Object, String> map, String str, Object obj, SerializeConfig serializeConfig) {
        StringBuilder sb;
        StringBuilder sb2;
        StringBuilder sb3;
        StringBuilder sb4;
        if (obj == null || map.containsKey(obj)) {
            return;
        }
        map.put(obj, str);
        if (obj instanceof Map) {
            for (Map.Entry entry : ((Map) obj).entrySet()) {
                Object key = entry.getKey();
                if (key instanceof String) {
                    if (str.equals(FileUriModel.SCHEME)) {
                        sb4 = new StringBuilder();
                    } else {
                        sb4 = new StringBuilder();
                        sb4.append(str);
                    }
                    sb4.append(FileUriModel.SCHEME);
                    sb4.append(key);
                    paths(map, sb4.toString(), entry.getValue(), serializeConfig);
                }
            }
            return;
        }
        int i = 0;
        if (obj instanceof Collection) {
            for (Object obj2 : (Collection) obj) {
                if (str.equals(FileUriModel.SCHEME)) {
                    sb3 = new StringBuilder();
                } else {
                    sb3 = new StringBuilder();
                    sb3.append(str);
                }
                sb3.append(FileUriModel.SCHEME);
                sb3.append(i);
                paths(map, sb3.toString(), obj2, serializeConfig);
                i++;
            }
            return;
        }
        Class<?> cls = obj.getClass();
        if (cls.isArray()) {
            int length = Array.getLength(obj);
            while (i < length) {
                Object obj3 = Array.get(obj, i);
                if (str.equals(FileUriModel.SCHEME)) {
                    sb2 = new StringBuilder();
                } else {
                    sb2 = new StringBuilder();
                    sb2.append(str);
                }
                sb2.append(FileUriModel.SCHEME);
                sb2.append(i);
                paths(map, sb2.toString(), obj3, serializeConfig);
                i++;
            }
            return;
        }
        if (ParserConfig.isPrimitive2(cls) || cls.isEnum()) {
            return;
        }
        ObjectSerializer objectWriter = serializeConfig.getObjectWriter(cls);
        if (objectWriter instanceof JavaBeanSerializer) {
            try {
                for (Map.Entry<String, Object> entry2 : ((JavaBeanSerializer) objectWriter).getFieldValuesMap(obj).entrySet()) {
                    String key2 = entry2.getKey();
                    if (key2 instanceof String) {
                        if (str.equals(FileUriModel.SCHEME)) {
                            sb = new StringBuilder();
                            sb.append(FileUriModel.SCHEME);
                            sb.append(key2);
                        } else {
                            sb = new StringBuilder();
                            sb.append(str);
                            sb.append(FileUriModel.SCHEME);
                            sb.append(key2);
                        }
                        paths(map, sb.toString(), entry2.getValue(), serializeConfig);
                    }
                }
            } catch (Exception e) {
                throw new JSONException("toJSON error", e);
            }
        }
    }

    private static void paths(Map<Object, String> map, Map<String, Object> map2, String str, Object obj, SerializeConfig serializeConfig) {
        StringBuilder sb;
        StringBuilder sb2;
        StringBuilder sb3;
        StringBuilder sb4;
        if (obj == null) {
            return;
        }
        int i = 0;
        if (map.put(obj, str) != null) {
            if (!((obj instanceof String) || (obj instanceof Number) || (obj instanceof Date) || (obj instanceof UUID))) {
                return;
            }
        }
        map2.put(str, obj);
        if (obj instanceof Map) {
            for (Map.Entry entry : ((Map) obj).entrySet()) {
                Object key = entry.getKey();
                if (key instanceof String) {
                    if (str.equals(FileUriModel.SCHEME)) {
                        sb4 = new StringBuilder();
                    } else {
                        sb4 = new StringBuilder();
                        sb4.append(str);
                    }
                    sb4.append(FileUriModel.SCHEME);
                    sb4.append(key);
                    paths(map, map2, sb4.toString(), entry.getValue(), serializeConfig);
                }
            }
            return;
        }
        if (obj instanceof Collection) {
            for (Object obj2 : (Collection) obj) {
                if (str.equals(FileUriModel.SCHEME)) {
                    sb3 = new StringBuilder();
                } else {
                    sb3 = new StringBuilder();
                    sb3.append(str);
                }
                sb3.append(FileUriModel.SCHEME);
                sb3.append(i);
                paths(map, map2, sb3.toString(), obj2, serializeConfig);
                i++;
            }
            return;
        }
        Class<?> cls = obj.getClass();
        if (cls.isArray()) {
            int length = Array.getLength(obj);
            while (i < length) {
                Object obj3 = Array.get(obj, i);
                if (str.equals(FileUriModel.SCHEME)) {
                    sb2 = new StringBuilder();
                } else {
                    sb2 = new StringBuilder();
                    sb2.append(str);
                }
                sb2.append(FileUriModel.SCHEME);
                sb2.append(i);
                paths(map, map2, sb2.toString(), obj3, serializeConfig);
                i++;
            }
            return;
        }
        if (ParserConfig.isPrimitive2(cls) || cls.isEnum()) {
            return;
        }
        ObjectSerializer objectWriter = serializeConfig.getObjectWriter(cls);
        if (objectWriter instanceof JavaBeanSerializer) {
            try {
                for (Map.Entry<String, Object> entry2 : ((JavaBeanSerializer) objectWriter).getFieldValuesMap(obj).entrySet()) {
                    String key2 = entry2.getKey();
                    if (key2 instanceof String) {
                        if (str.equals(FileUriModel.SCHEME)) {
                            sb = new StringBuilder();
                            sb.append(FileUriModel.SCHEME);
                            sb.append(key2);
                        } else {
                            sb = new StringBuilder();
                            sb.append(str);
                            sb.append(FileUriModel.SCHEME);
                            sb.append(key2);
                        }
                        paths(map, map2, sb.toString(), entry2.getValue(), serializeConfig);
                    }
                }
            } catch (Exception e) {
                throw new JSONException("toJSON error", e);
            }
        }
    }

    public static Object read(String str, String str2) {
        return compile(str2).eval(JSON.parse(str));
    }

    public static boolean remove(Object obj, String str) {
        return compile(str).remove(obj);
    }

    public static boolean set(Object obj, String str, Object obj2) {
        return compile(str).set(obj, obj2);
    }

    public static int size(Object obj, String str) {
        JSONPath compile = compile(str);
        return compile.evalSize(compile.eval(obj));
    }

    public void arrayAdd(Object obj, Object... objArr) {
        if (objArr == null || objArr.length == 0 || obj == null) {
            return;
        }
        init();
        int i = 0;
        Object obj2 = obj;
        Object obj3 = null;
        for (int i2 = 0; i2 < this.segments.length; i2++) {
            if (i2 == this.segments.length - 1) {
                obj3 = obj2;
            }
            obj2 = this.segments[i2].eval(this, obj, obj2);
        }
        if (obj2 == null) {
            throw new JSONPathException("value not found in path " + this.path);
        }
        if (obj2 instanceof Collection) {
            Collection collection = (Collection) obj2;
            int length = objArr.length;
            while (i < length) {
                collection.add(objArr[i]);
                i++;
            }
            return;
        }
        Class<?> cls = obj2.getClass();
        if (!cls.isArray()) {
            throw new JSONException("unsupported array put operation. " + cls);
        }
        int length2 = Array.getLength(obj2);
        Object newInstance = Array.newInstance(cls.getComponentType(), objArr.length + length2);
        System.arraycopy(obj2, 0, newInstance, 0, length2);
        while (i < objArr.length) {
            Array.set(newInstance, length2 + i, objArr[i]);
            i++;
        }
        Segement segement = this.segments[this.segments.length - 1];
        if (segement instanceof PropertySegement) {
            ((PropertySegement) segement).setValue(this, obj3, newInstance);
        } else {
            if (!(segement instanceof ArrayAccessSegement)) {
                throw new UnsupportedOperationException();
            }
            ((ArrayAccessSegement) segement).setValue(this, obj3, newInstance);
        }
    }

    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        init();
        Object obj2 = obj;
        for (int i = 0; i < this.segments.length; i++) {
            obj2 = this.segments[i].eval(this, obj, obj2);
            if (obj2 == null) {
                return false;
            }
        }
        return true;
    }

    public boolean containsValue(Object obj, Object obj2) {
        Object eval = eval(obj);
        if (eval == obj2) {
            return true;
        }
        if (eval == null) {
            return false;
        }
        if (!(eval instanceof Iterable)) {
            return eq(eval, obj2);
        }
        Iterator it = ((Iterable) eval).iterator();
        while (it.hasNext()) {
            if (eq(it.next(), obj2)) {
                return true;
            }
        }
        return false;
    }

    protected void deepScan(Object obj, String str, List<Object> list) {
        if (obj == null) {
            return;
        }
        if (obj instanceof Map) {
            Map map = (Map) obj;
            if (map.containsKey(str)) {
                list.add(map.get(str));
                return;
            }
            Iterator it = map.values().iterator();
            while (it.hasNext()) {
                deepScan(it.next(), str, list);
            }
            return;
        }
        JavaBeanSerializer javaBeanSerializer = getJavaBeanSerializer(obj.getClass());
        if (javaBeanSerializer == null) {
            if (obj instanceof List) {
                List list2 = (List) obj;
                for (int i = 0; i < list2.size(); i++) {
                    deepScan(list2.get(i), str, list);
                }
                return;
            }
            return;
        }
        try {
            FieldSerializer fieldSerializer = javaBeanSerializer.getFieldSerializer(str);
            if (fieldSerializer == null) {
                Iterator<Object> it2 = javaBeanSerializer.getFieldValues(obj).iterator();
                while (it2.hasNext()) {
                    deepScan(it2.next(), str, list);
                }
                return;
            }
            try {
                list.add(fieldSerializer.getPropertyValueDirect(obj));
            } catch (IllegalAccessException e) {
                throw new JSONException("getFieldValue error." + str, e);
            } catch (InvocationTargetException e2) {
                throw new JSONException("getFieldValue error." + str, e2);
            }
        } catch (Exception e3) {
            throw new JSONPathException("jsonpath error, path " + this.path + ", segement " + str, e3);
        }
    }

    protected void deepSet(Object obj, String str, long j, Object obj2) {
        if (obj == null) {
            return;
        }
        if (obj instanceof Map) {
            Map map = (Map) obj;
            if (map.containsKey(str)) {
                map.get(str);
                map.put(str, obj2);
                return;
            } else {
                Iterator it = map.values().iterator();
                while (it.hasNext()) {
                    deepSet(it.next(), str, j, obj2);
                }
                return;
            }
        }
        Class<?> cls = obj.getClass();
        JavaBeanDeserializer javaBeanDeserializer = getJavaBeanDeserializer(cls);
        if (javaBeanDeserializer == null) {
            if (obj instanceof List) {
                List list = (List) obj;
                for (int i = 0; i < list.size(); i++) {
                    deepSet(list.get(i), str, j, obj2);
                }
                return;
            }
            return;
        }
        try {
            FieldDeserializer fieldDeserializer = javaBeanDeserializer.getFieldDeserializer(str);
            if (fieldDeserializer != null) {
                fieldDeserializer.setValue(obj, obj2);
                return;
            }
            Iterator<Object> it2 = getJavaBeanSerializer(cls).getObjectFieldValues(obj).iterator();
            while (it2.hasNext()) {
                deepSet(it2.next(), str, j, obj2);
            }
        } catch (Exception e) {
            throw new JSONPathException("jsonpath error, path " + this.path + ", segement " + str, e);
        }
    }

    public Object eval(Object obj) {
        if (obj == null) {
            return null;
        }
        init();
        Object obj2 = obj;
        for (int i = 0; i < this.segments.length; i++) {
            obj2 = this.segments[i].eval(this, obj, obj2);
        }
        return obj2;
    }

    int evalSize(Object obj) {
        if (obj == null) {
            return -1;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).size();
        }
        if (obj instanceof Object[]) {
            return ((Object[]) obj).length;
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj);
        }
        if (obj instanceof Map) {
            int i = 0;
            Iterator it = ((Map) obj).values().iterator();
            while (it.hasNext()) {
                if (it.next() != null) {
                    i++;
                }
            }
            return i;
        }
        JavaBeanSerializer javaBeanSerializer = getJavaBeanSerializer(obj.getClass());
        if (javaBeanSerializer == null) {
            return -1;
        }
        try {
            return javaBeanSerializer.getSize(obj);
        } catch (Exception e) {
            throw new JSONPathException("evalSize error : " + this.path, e);
        }
    }

    protected Object getArrayItem(Object obj, int i) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof List) {
            List list = (List) obj;
            if (i >= 0) {
                if (i < list.size()) {
                    return list.get(i);
                }
                return null;
            }
            if (Math.abs(i) <= list.size()) {
                return list.get(list.size() + i);
            }
            return null;
        }
        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            if (i >= 0) {
                if (i < length) {
                    return Array.get(obj, i);
                }
                return null;
            }
            if (Math.abs(i) <= length) {
                return Array.get(obj, length + i);
            }
            return null;
        }
        if (obj instanceof Map) {
            Map map = (Map) obj;
            Object obj2 = map.get(Integer.valueOf(i));
            return obj2 == null ? map.get(Integer.toString(i)) : obj2;
        }
        if (!(obj instanceof Collection)) {
            throw new UnsupportedOperationException();
        }
        int i2 = 0;
        for (Object obj3 : (Collection) obj) {
            if (i2 == i) {
                return obj3;
            }
            i2++;
        }
        return null;
    }

    protected JavaBeanDeserializer getJavaBeanDeserializer(Class<?> cls) {
        ObjectDeserializer deserializer = this.parserConfig.getDeserializer(cls);
        if (deserializer instanceof JavaBeanDeserializer) {
            return (JavaBeanDeserializer) deserializer;
        }
        return null;
    }

    protected JavaBeanSerializer getJavaBeanSerializer(Class<?> cls) {
        ObjectSerializer objectWriter = this.serializeConfig.getObjectWriter(cls);
        if (objectWriter instanceof JavaBeanSerializer) {
            return (JavaBeanSerializer) objectWriter;
        }
        return null;
    }

    public String getPath() {
        return this.path;
    }

    protected Object getPropertyValue(Object obj, String str, long j) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Map) {
            Map map = (Map) obj;
            Object obj2 = map.get(str);
            return obj2 == null ? (SIZE == j || LENGTH == j) ? Integer.valueOf(map.size()) : obj2 : obj2;
        }
        JavaBeanSerializer javaBeanSerializer = getJavaBeanSerializer(obj.getClass());
        if (javaBeanSerializer != null) {
            try {
                return javaBeanSerializer.getFieldValue(obj, str, j, false);
            } catch (Exception e) {
                throw new JSONPathException("jsonpath error, path " + this.path + ", segement " + str, e);
            }
        }
        if (obj instanceof List) {
            List list = (List) obj;
            if (SIZE == j || LENGTH == j) {
                return Integer.valueOf(list.size());
            }
            JSONArray jSONArray = new JSONArray(list.size());
            for (int i = 0; i < list.size(); i++) {
                Object obj3 = list.get(i);
                if (obj3 == list) {
                    jSONArray.add(obj3);
                } else {
                    Object propertyValue = getPropertyValue(obj3, str, j);
                    if (propertyValue instanceof Collection) {
                        jSONArray.addAll((Collection) propertyValue);
                    } else if (propertyValue != null) {
                        jSONArray.add(propertyValue);
                    }
                }
            }
            return jSONArray;
        }
        if (obj instanceof Enum) {
            Enum r3 = (Enum) obj;
            if (-4270347329889690746L == j) {
                return r3.name();
            }
            if (-1014497654951707614L == j) {
                return Integer.valueOf(r3.ordinal());
            }
        }
        if (obj instanceof Calendar) {
            Calendar calendar = (Calendar) obj;
            if (8963398325558730460L == j) {
                return Integer.valueOf(calendar.get(1));
            }
            if (-811277319855450459L == j) {
                return Integer.valueOf(calendar.get(2));
            }
            if (-3851359326990528739L == j) {
                return Integer.valueOf(calendar.get(5));
            }
            if (4647432019745535567L == j) {
                return Integer.valueOf(calendar.get(11));
            }
            if (6607618197526598121L == j) {
                return Integer.valueOf(calendar.get(12));
            }
            if (-6586085717218287427L == j) {
                return Integer.valueOf(calendar.get(13));
            }
        }
        return null;
    }

    protected Collection<Object> getPropertyValues(Object obj) {
        JavaBeanSerializer javaBeanSerializer = getJavaBeanSerializer(obj.getClass());
        if (javaBeanSerializer == null) {
            if (obj instanceof Map) {
                return ((Map) obj).values();
            }
            throw new UnsupportedOperationException();
        }
        try {
            return javaBeanSerializer.getFieldValues(obj);
        } catch (Exception e) {
            throw new JSONPathException("jsonpath error, path " + this.path, e);
        }
    }

    protected void init() {
        if (this.segments != null) {
            return;
        }
        if ("*".equals(this.path)) {
            this.segments = new Segement[]{WildCardSegement.instance};
        } else {
            this.segments = new JSONPathParser(this.path).explain();
        }
    }

    public boolean remove(Object obj) {
        boolean z = false;
        if (obj == null) {
            return false;
        }
        init();
        Object obj2 = null;
        Object obj3 = obj;
        int i = 0;
        while (true) {
            if (i >= this.segments.length) {
                break;
            }
            if (i == this.segments.length - 1) {
                obj2 = obj3;
                break;
            }
            obj3 = this.segments[i].eval(this, obj, obj3);
            if (obj3 == null) {
                break;
            }
            i++;
        }
        if (obj2 == null) {
            return false;
        }
        Segement segement = this.segments[this.segments.length - 1];
        if (!(segement instanceof PropertySegement)) {
            if (segement instanceof ArrayAccessSegement) {
                return ((ArrayAccessSegement) segement).remove(this, obj2);
            }
            throw new UnsupportedOperationException();
        }
        PropertySegement propertySegement = (PropertySegement) segement;
        if ((obj2 instanceof Collection) && this.segments.length > 1) {
            Segement segement2 = this.segments[this.segments.length - 2];
            if ((segement2 instanceof RangeSegement) || (segement2 instanceof MultiIndexSegement)) {
                Iterator it = ((Collection) obj2).iterator();
                while (it.hasNext()) {
                    if (propertySegement.remove(this, it.next())) {
                        z = true;
                    }
                }
                return z;
            }
        }
        return propertySegement.remove(this, obj2);
    }

    public boolean removeArrayItem(JSONPath jSONPath, Object obj, int i) {
        if (!(obj instanceof List)) {
            throw new JSONPathException("unsupported set operation." + obj.getClass());
        }
        List list = (List) obj;
        if (i >= 0) {
            if (i >= list.size()) {
                return false;
            }
            list.remove(i);
            return true;
        }
        int size = list.size() + i;
        if (size < 0) {
            return false;
        }
        list.remove(size);
        return true;
    }

    protected boolean removePropertyValue(Object obj, String str) {
        if (obj instanceof Map) {
            return ((Map) obj).remove(str) != null;
        }
        ObjectDeserializer deserializer = this.parserConfig.getDeserializer(obj.getClass());
        JavaBeanDeserializer javaBeanDeserializer = deserializer instanceof JavaBeanDeserializer ? (JavaBeanDeserializer) deserializer : null;
        if (javaBeanDeserializer == null) {
            throw new UnsupportedOperationException();
        }
        FieldDeserializer fieldDeserializer = javaBeanDeserializer.getFieldDeserializer(str);
        if (fieldDeserializer == null) {
            return false;
        }
        fieldDeserializer.setValue(obj, (String) null);
        return true;
    }

    public boolean set(Object obj, Object obj2) {
        return set(obj, obj2, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0053  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x005f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean set(java.lang.Object r9, java.lang.Object r10, boolean r11) {
        /*
            r8 = this;
            r11 = 0
            if (r9 != 0) goto L4
            return r11
        L4:
            r8.init()
            r0 = 0
            r2 = r9
            r3 = r0
            r1 = 0
        Lb:
            com.alibaba.fastjson.JSONPath$Segement[] r4 = r8.segments
            int r4 = r4.length
            r5 = 1
            if (r1 >= r4) goto L8a
            com.alibaba.fastjson.JSONPath$Segement[] r3 = r8.segments
            r3 = r3[r1]
            java.lang.Object r4 = r3.eval(r8, r9, r2)
            if (r4 != 0) goto L85
            com.alibaba.fastjson.JSONPath$Segement[] r4 = r8.segments
            int r4 = r4.length
            int r4 = r4 - r5
            if (r1 >= r4) goto L28
            com.alibaba.fastjson.JSONPath$Segement[] r4 = r8.segments
            int r6 = r1 + 1
            r4 = r4[r6]
            goto L29
        L28:
            r4 = r0
        L29:
            boolean r6 = r4 instanceof com.alibaba.fastjson.JSONPath.PropertySegement
            if (r6 == 0) goto L65
            boolean r4 = r3 instanceof com.alibaba.fastjson.JSONPath.PropertySegement
            if (r4 == 0) goto L4f
            r4 = r3
            com.alibaba.fastjson.JSONPath$PropertySegement r4 = (com.alibaba.fastjson.JSONPath.PropertySegement) r4
            java.lang.String r4 = com.alibaba.fastjson.JSONPath.PropertySegement.access$000(r4)
            java.lang.Class r6 = r2.getClass()
            com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer r6 = r8.getJavaBeanDeserializer(r6)
            if (r6 == 0) goto L4f
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer r4 = r6.getFieldDeserializer(r4)
            com.alibaba.fastjson.util.FieldInfo r4 = r4.fieldInfo
            java.lang.Class<?> r4 = r4.fieldClass
            com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer r6 = r8.getJavaBeanDeserializer(r4)
            goto L51
        L4f:
            r4 = r0
            r6 = r4
        L51:
            if (r6 == 0) goto L5f
            com.alibaba.fastjson.util.JavaBeanInfo r7 = r6.beanInfo
            java.lang.reflect.Constructor<?> r7 = r7.defaultConstructor
            if (r7 == 0) goto L5e
            java.lang.Object r4 = r6.createInstance(r0, r4)
            goto L70
        L5e:
            return r11
        L5f:
            com.alibaba.fastjson.JSONObject r4 = new com.alibaba.fastjson.JSONObject
            r4.<init>()
            goto L70
        L65:
            boolean r4 = r4 instanceof com.alibaba.fastjson.JSONPath.ArrayAccessSegement
            if (r4 == 0) goto L6f
            com.alibaba.fastjson.JSONArray r4 = new com.alibaba.fastjson.JSONArray
            r4.<init>()
            goto L70
        L6f:
            r4 = r0
        L70:
            if (r4 == 0) goto L8b
            boolean r6 = r3 instanceof com.alibaba.fastjson.JSONPath.PropertySegement
            if (r6 == 0) goto L7c
            com.alibaba.fastjson.JSONPath$PropertySegement r3 = (com.alibaba.fastjson.JSONPath.PropertySegement) r3
            r3.setValue(r8, r2, r4)
            goto L85
        L7c:
            boolean r6 = r3 instanceof com.alibaba.fastjson.JSONPath.ArrayAccessSegement
            if (r6 == 0) goto L8b
            com.alibaba.fastjson.JSONPath$ArrayAccessSegement r3 = (com.alibaba.fastjson.JSONPath.ArrayAccessSegement) r3
            r3.setValue(r8, r2, r4)
        L85:
            int r1 = r1 + 1
            r3 = r2
            r2 = r4
            goto Lb
        L8a:
            r2 = r3
        L8b:
            if (r2 != 0) goto L8e
            return r11
        L8e:
            com.alibaba.fastjson.JSONPath$Segement[] r9 = r8.segments
            com.alibaba.fastjson.JSONPath$Segement[] r11 = r8.segments
            int r11 = r11.length
            int r11 = r11 - r5
            r9 = r9[r11]
            boolean r11 = r9 instanceof com.alibaba.fastjson.JSONPath.PropertySegement
            if (r11 == 0) goto La0
            com.alibaba.fastjson.JSONPath$PropertySegement r9 = (com.alibaba.fastjson.JSONPath.PropertySegement) r9
            r9.setValue(r8, r2, r10)
            return r5
        La0:
            boolean r11 = r9 instanceof com.alibaba.fastjson.JSONPath.ArrayAccessSegement
            if (r11 == 0) goto Lab
            com.alibaba.fastjson.JSONPath$ArrayAccessSegement r9 = (com.alibaba.fastjson.JSONPath.ArrayAccessSegement) r9
            boolean r9 = r9.setValue(r8, r2, r10)
            return r9
        Lab:
            java.lang.UnsupportedOperationException r9 = new java.lang.UnsupportedOperationException
            r9.<init>()
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.JSONPath.set(java.lang.Object, java.lang.Object, boolean):boolean");
    }

    public boolean setArrayItem(JSONPath jSONPath, Object obj, int i, Object obj2) {
        if (obj instanceof List) {
            List list = (List) obj;
            if (i >= 0) {
                list.set(i, obj2);
            } else {
                list.set(list.size() + i, obj2);
            }
            return true;
        }
        Class<?> cls = obj.getClass();
        if (!cls.isArray()) {
            throw new JSONPathException("unsupported set operation." + cls);
        }
        int length = Array.getLength(obj);
        if (i >= 0) {
            if (i < length) {
                Array.set(obj, i, obj2);
            }
        } else if (Math.abs(i) <= length) {
            Array.set(obj, length + i, obj2);
        }
        return true;
    }

    protected boolean setPropertyValue(Object obj, String str, long j, Object obj2) {
        if (obj instanceof Map) {
            ((Map) obj).put(str, obj2);
            return true;
        }
        if (obj instanceof List) {
            for (Object obj3 : (List) obj) {
                if (obj3 != null) {
                    setPropertyValue(obj3, str, j, obj2);
                }
            }
            return true;
        }
        ObjectDeserializer deserializer = this.parserConfig.getDeserializer(obj.getClass());
        JavaBeanDeserializer javaBeanDeserializer = deserializer instanceof JavaBeanDeserializer ? (JavaBeanDeserializer) deserializer : null;
        if (javaBeanDeserializer == null) {
            throw new UnsupportedOperationException();
        }
        FieldDeserializer fieldDeserializer = javaBeanDeserializer.getFieldDeserializer(j);
        if (fieldDeserializer == null) {
            return false;
        }
        fieldDeserializer.setValue(obj, obj2);
        return true;
    }

    public int size(Object obj) {
        if (obj == null) {
            return -1;
        }
        init();
        Object obj2 = obj;
        for (int i = 0; i < this.segments.length; i++) {
            obj2 = this.segments[i].eval(this, obj, obj2);
        }
        return evalSize(obj2);
    }

    @Override // com.alibaba.fastjson.JSONAware
    public String toJSONString() {
        return JSON.toJSONString(this.path);
    }
}
