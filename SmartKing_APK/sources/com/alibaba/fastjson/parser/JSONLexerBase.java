package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.IOUtils;
import com.amap.location.common.model.AmapLoc;
import com.autonavi.amap.mapcore.tools.GlMapUtil;
import com.tencent.bugly.Bugly;
import java.io.Closeable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import kotlin.text.Typography;
import org.apache.commons.lang.CharUtils;

/* loaded from: classes.dex */
public abstract class JSONLexerBase implements JSONLexer, Closeable {
    protected static final int INT_MULTMIN_RADIX_TEN = -214748364;
    protected static final long MULTMIN_RADIX_TEN = -922337203685477580L;
    protected int bp;
    protected char ch;
    protected int eofPos;
    protected int features;
    protected boolean hasSpecial;
    protected int np;
    protected int pos;
    protected char[] sbuf;
    protected int sp;
    protected String stringDefaultValue;
    protected int token;
    private static final ThreadLocal<char[]> SBUF_LOCAL = new ThreadLocal<>();
    protected static final char[] typeFieldName = ("\"" + JSON.DEFAULT_TYPE_KEY + "\":\"").toCharArray();
    protected static final int[] digits = new int[103];
    protected Calendar calendar = null;
    protected TimeZone timeZone = JSON.defaultTimeZone;
    protected Locale locale = JSON.defaultLocale;
    public int matchStat = 0;

    static {
        for (int i = 48; i <= 57; i++) {
            digits[i] = i - 48;
        }
        for (int i2 = 97; i2 <= 102; i2++) {
            digits[i2] = (i2 - 97) + 10;
        }
        for (int i3 = 65; i3 <= 70; i3++) {
            digits[i3] = (i3 - 65) + 10;
        }
    }

    public JSONLexerBase(int i) {
        this.stringDefaultValue = null;
        this.features = i;
        if ((i & Feature.InitStringFieldAsEmpty.mask) != 0) {
            this.stringDefaultValue = "";
        }
        this.sbuf = SBUF_LOCAL.get();
        if (this.sbuf == null) {
            this.sbuf = new char[512];
        }
    }

    public static boolean isWhitespace(char c) {
        return c <= ' ' && (c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == '\f' || c == '\b');
    }

    public static String readString(char[] cArr, int i) {
        int i2;
        char[] cArr2 = new char[i];
        int i3 = 0;
        int i4 = 0;
        while (i3 < i) {
            char c = cArr[i3];
            if (c != '\\') {
                cArr2[i4] = c;
                i4++;
            } else {
                i3++;
                char c2 = cArr[i3];
                switch (c2) {
                    case '/':
                        i2 = i4 + 1;
                        cArr2[i4] = '/';
                        break;
                    case '0':
                        i2 = i4 + 1;
                        cArr2[i4] = 0;
                        break;
                    case '1':
                        i2 = i4 + 1;
                        cArr2[i4] = 1;
                        break;
                    case '2':
                        i2 = i4 + 1;
                        cArr2[i4] = 2;
                        break;
                    case '3':
                        i2 = i4 + 1;
                        cArr2[i4] = 3;
                        break;
                    case '4':
                        i2 = i4 + 1;
                        cArr2[i4] = 4;
                        break;
                    case '5':
                        i2 = i4 + 1;
                        cArr2[i4] = 5;
                        break;
                    case '6':
                        i2 = i4 + 1;
                        cArr2[i4] = 6;
                        break;
                    case '7':
                        i2 = i4 + 1;
                        cArr2[i4] = 7;
                        break;
                    default:
                        switch (c2) {
                            case 't':
                                i2 = i4 + 1;
                                cArr2[i4] = '\t';
                                break;
                            case 'u':
                                i2 = i4 + 1;
                                int i5 = i3 + 1;
                                int i6 = i5 + 1;
                                int i7 = i6 + 1;
                                i3 = i7 + 1;
                                cArr2[i4] = (char) Integer.parseInt(new String(new char[]{cArr[i5], cArr[i6], cArr[i7], cArr[i3]}), 16);
                                break;
                            case 'v':
                                i2 = i4 + 1;
                                cArr2[i4] = 11;
                                break;
                            default:
                                switch (c2) {
                                    case '\"':
                                        i2 = i4 + 1;
                                        cArr2[i4] = Typography.quote;
                                        break;
                                    case '\'':
                                        i2 = i4 + 1;
                                        cArr2[i4] = '\'';
                                        break;
                                    case 'F':
                                    case 'f':
                                        i2 = i4 + 1;
                                        cArr2[i4] = '\f';
                                        break;
                                    case '\\':
                                        i2 = i4 + 1;
                                        cArr2[i4] = '\\';
                                        break;
                                    case 'b':
                                        i2 = i4 + 1;
                                        cArr2[i4] = '\b';
                                        break;
                                    case 'n':
                                        i2 = i4 + 1;
                                        cArr2[i4] = '\n';
                                        break;
                                    case 'r':
                                        i2 = i4 + 1;
                                        cArr2[i4] = CharUtils.CR;
                                        break;
                                    case GlMapUtil.DEVICE_DISPLAY_DPI_LOW /* 120 */:
                                        i2 = i4 + 1;
                                        int i8 = i3 + 1;
                                        int i9 = digits[cArr[i8]] * 16;
                                        i3 = i8 + 1;
                                        cArr2[i4] = (char) (i9 + digits[cArr[i3]]);
                                        break;
                                    default:
                                        throw new JSONException("unclosed.str.lit");
                                }
                        }
                }
                i4 = i2;
            }
            i3++;
        }
        return new String(cArr2, 0, i4);
    }

    private void scanStringSingleQuote() {
        this.np = this.bp;
        this.hasSpecial = false;
        while (true) {
            char next = next();
            if (next == '\'') {
                this.token = 4;
                next();
                return;
            }
            if (next == 26) {
                if (isEOF()) {
                    throw new JSONException("unclosed single-quote string");
                }
                putChar(JSONLexer.EOI);
            } else if (next == '\\') {
                if (!this.hasSpecial) {
                    this.hasSpecial = true;
                    if (this.sp > this.sbuf.length) {
                        char[] cArr = new char[this.sp * 2];
                        System.arraycopy(this.sbuf, 0, cArr, 0, this.sbuf.length);
                        this.sbuf = cArr;
                    }
                    copyTo(this.np + 1, this.sp, this.sbuf);
                }
                char next2 = next();
                switch (next2) {
                    case '/':
                        putChar('/');
                        break;
                    case '0':
                        putChar((char) 0);
                        break;
                    case '1':
                        putChar((char) 1);
                        break;
                    case '2':
                        putChar((char) 2);
                        break;
                    case '3':
                        putChar((char) 3);
                        break;
                    case '4':
                        putChar((char) 4);
                        break;
                    case '5':
                        putChar((char) 5);
                        break;
                    case '6':
                        putChar((char) 6);
                        break;
                    case '7':
                        putChar((char) 7);
                        break;
                    default:
                        switch (next2) {
                            case 't':
                                putChar('\t');
                                break;
                            case 'u':
                                putChar((char) Integer.parseInt(new String(new char[]{next(), next(), next(), next()}), 16));
                                break;
                            case 'v':
                                putChar((char) 11);
                                break;
                            default:
                                switch (next2) {
                                    case '\"':
                                        putChar(Typography.quote);
                                        break;
                                    case '\'':
                                        putChar('\'');
                                        break;
                                    case 'F':
                                    case 'f':
                                        putChar('\f');
                                        break;
                                    case '\\':
                                        putChar('\\');
                                        break;
                                    case 'b':
                                        putChar('\b');
                                        break;
                                    case 'n':
                                        putChar('\n');
                                        break;
                                    case 'r':
                                        putChar(CharUtils.CR);
                                        break;
                                    case GlMapUtil.DEVICE_DISPLAY_DPI_LOW /* 120 */:
                                        putChar((char) ((digits[next()] * 16) + digits[next()]));
                                        break;
                                    default:
                                        this.ch = next2;
                                        throw new JSONException("unclosed single-quote string");
                                }
                        }
                }
            } else if (!this.hasSpecial) {
                this.sp++;
            } else if (this.sp == this.sbuf.length) {
                putChar(next);
            } else {
                char[] cArr2 = this.sbuf;
                int i = this.sp;
                this.sp = i + 1;
                cArr2[i] = next;
            }
        }
    }

    public abstract String addSymbol(int i, int i2, int i3, SymbolTable symbolTable);

    protected abstract void arrayCopy(int i, char[] cArr, int i2, int i3);

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public abstract byte[] bytesValue();

    protected abstract boolean charArrayCompare(char[] cArr);

    public abstract char charAt(int i);

    @Override // com.alibaba.fastjson.parser.JSONLexer, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.sbuf.length <= 8192) {
            SBUF_LOCAL.set(this.sbuf);
        }
        this.sbuf = null;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public void config(Feature feature, boolean z) {
        this.features = Feature.config(this.features, feature, z);
        if ((this.features & Feature.InitStringFieldAsEmpty.mask) != 0) {
            this.stringDefaultValue = "";
        }
    }

    protected abstract void copyTo(int i, int i2, char[] cArr);

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final Number decimalValue(boolean z) {
        char charAt = charAt((this.np + this.sp) - 1);
        try {
            return charAt == 'F' ? Float.valueOf(Float.parseFloat(numberString())) : charAt == 'D' ? Double.valueOf(Double.parseDouble(numberString())) : z ? decimalValue() : Double.valueOf(doubleValue());
        } catch (NumberFormatException e) {
            throw new JSONException(e.getMessage() + ", " + info());
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public abstract BigDecimal decimalValue();

    public double doubleValue() {
        return Double.parseDouble(numberString());
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public float floatValue() {
        char charAt;
        String numberString = numberString();
        float parseFloat = Float.parseFloat(numberString);
        if ((parseFloat != 0.0f && parseFloat != Float.POSITIVE_INFINITY) || (charAt = numberString.charAt(0)) <= '0' || charAt > '9') {
            return parseFloat;
        }
        throw new JSONException("float overflow : " + numberString);
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final char getCurrent() {
        return this.ch;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public int getFeatures() {
        return this.features;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public Locale getLocale() {
        return this.locale;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public TimeZone getTimeZone() {
        return this.timeZone;
    }

    public abstract int indexOf(char c, int i);

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public String info() {
        return "";
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final int intValue() {
        boolean z;
        int i;
        int i2 = 0;
        if (this.np == -1) {
            this.np = 0;
        }
        int i3 = this.np;
        int i4 = this.np + this.sp;
        if (charAt(this.np) == '-') {
            i3++;
            z = true;
            i = Integer.MIN_VALUE;
        } else {
            z = false;
            i = -2147483647;
        }
        if (i3 < i4) {
            i2 = -(charAt(i3) - '0');
            i3++;
        }
        while (i3 < i4) {
            int i5 = i3 + 1;
            char charAt = charAt(i3);
            if (charAt == 'L' || charAt == 'S' || charAt == 'B') {
                i3 = i5;
                break;
            }
            int i6 = charAt - '0';
            if (i2 < -214748364) {
                throw new NumberFormatException(numberString());
            }
            int i7 = i2 * 10;
            if (i7 < i + i6) {
                throw new NumberFormatException(numberString());
            }
            i2 = i7 - i6;
            i3 = i5;
        }
        if (!z) {
            return -i2;
        }
        if (i3 > this.np + 1) {
            return i2;
        }
        throw new NumberFormatException(numberString());
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final Number integerValue() throws NumberFormatException {
        long j;
        long j2;
        boolean z = false;
        if (this.np == -1) {
            this.np = 0;
        }
        int i = this.np;
        int i2 = this.np + this.sp;
        char c = ' ';
        char charAt = charAt(i2 - 1);
        if (charAt == 'B') {
            i2--;
            c = 'B';
        } else if (charAt == 'L') {
            i2--;
            c = 'L';
        } else if (charAt == 'S') {
            i2--;
            c = 'S';
        }
        if (charAt(this.np) == '-') {
            j = Long.MIN_VALUE;
            i++;
            z = true;
        } else {
            j = -9223372036854775807L;
        }
        long j3 = MULTMIN_RADIX_TEN;
        if (i < i2) {
            j2 = -(charAt(i) - '0');
            i++;
        } else {
            j2 = 0;
        }
        while (i < i2) {
            int i3 = i + 1;
            int charAt2 = charAt(i) - '0';
            if (j2 < j3) {
                return new BigInteger(numberString());
            }
            long j4 = j2 * 10;
            long j5 = charAt2;
            if (j4 < j + j5) {
                return new BigInteger(numberString());
            }
            j2 = j4 - j5;
            i = i3;
            j3 = MULTMIN_RADIX_TEN;
        }
        if (!z) {
            long j6 = -j2;
            return (j6 > 2147483647L || c == 'L') ? Long.valueOf(j6) : c == 'S' ? Short.valueOf((short) j6) : c == 'B' ? Byte.valueOf((byte) j6) : Integer.valueOf((int) j6);
        }
        if (i > this.np + 1) {
            return (j2 < -2147483648L || c == 'L') ? Long.valueOf(j2) : c == 'S' ? Short.valueOf((short) j2) : c == 'B' ? Byte.valueOf((byte) j2) : Integer.valueOf((int) j2);
        }
        throw new NumberFormatException(numberString());
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public boolean isBlankInput() {
        int i = 0;
        while (true) {
            char charAt = charAt(i);
            if (charAt == 26) {
                this.token = 20;
                return true;
            }
            if (!isWhitespace(charAt)) {
                return false;
            }
            i++;
        }
    }

    public abstract boolean isEOF();

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final boolean isEnabled(int i) {
        return (i & this.features) != 0;
    }

    public final boolean isEnabled(int i, int i2) {
        return ((this.features & i2) == 0 && (i & i2) == 0) ? false : true;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final boolean isEnabled(Feature feature) {
        return isEnabled(feature.mask);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final boolean isRef() {
        return this.sp == 4 && charAt(this.np + 1) == '$' && charAt(this.np + 2) == 'r' && charAt(this.np + 3) == 'e' && charAt(this.np + 4) == 'f';
    }

    protected void lexError(String str, Object... objArr) {
        this.token = 1;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x003c  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0079  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0089  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:22:0x0060 -> B:10:0x0036). Please report as a decompilation issue!!! */
    @Override // com.alibaba.fastjson.parser.JSONLexer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final long longValue() throws java.lang.NumberFormatException {
        /*
            r15 = this;
            int r0 = r15.np
            r1 = 0
            r2 = -1
            if (r0 != r2) goto L8
            r15.np = r1
        L8:
            int r0 = r15.np
            int r2 = r15.np
            int r3 = r15.sp
            int r2 = r2 + r3
            int r3 = r15.np
            char r3 = r15.charAt(r3)
            r4 = 45
            r5 = 1
            if (r3 != r4) goto L20
            r3 = -9223372036854775808
            int r0 = r0 + 1
            r1 = 1
            goto L25
        L20:
            r3 = -9223372036854775807(0x8000000000000001, double:-4.9E-324)
        L25:
            r6 = -922337203685477580(0xf333333333333334, double:-8.390303882365713E246)
            if (r0 >= r2) goto L38
            int r8 = r0 + 1
            char r0 = r15.charAt(r0)
            int r0 = r0 + (-48)
            int r0 = -r0
            long r9 = (long) r0
        L36:
            r0 = r8
            goto L3a
        L38:
            r9 = 0
        L3a:
            if (r0 >= r2) goto L77
            int r8 = r0 + 1
            char r0 = r15.charAt(r0)
            r11 = 76
            if (r0 == r11) goto L76
            r11 = 83
            if (r0 == r11) goto L76
            r11 = 66
            if (r0 != r11) goto L4f
            goto L76
        L4f:
            int r0 = r0 + (-48)
            int r11 = (r9 > r6 ? 1 : (r9 == r6 ? 0 : -1))
            if (r11 < 0) goto L6c
            r11 = 10
            long r9 = r9 * r11
            long r11 = (long) r0
            long r13 = r3 + r11
            int r0 = (r9 > r13 ? 1 : (r9 == r13 ? 0 : -1))
            if (r0 < 0) goto L62
            long r9 = r9 - r11
            goto L36
        L62:
            java.lang.NumberFormatException r0 = new java.lang.NumberFormatException
            java.lang.String r1 = r15.numberString()
            r0.<init>(r1)
            throw r0
        L6c:
            java.lang.NumberFormatException r0 = new java.lang.NumberFormatException
            java.lang.String r1 = r15.numberString()
            r0.<init>(r1)
            throw r0
        L76:
            r0 = r8
        L77:
            if (r1 == 0) goto L89
            int r1 = r15.np
            int r1 = r1 + r5
            if (r0 <= r1) goto L7f
            return r9
        L7f:
            java.lang.NumberFormatException r0 = new java.lang.NumberFormatException
            java.lang.String r1 = r15.numberString()
            r0.<init>(r1)
            throw r0
        L89:
            long r0 = -r9
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.longValue():long");
    }

    public final boolean matchField(char[] cArr) {
        while (!charArrayCompare(cArr)) {
            if (!isWhitespace(this.ch)) {
                return false;
            }
            next();
        }
        this.bp += cArr.length;
        this.ch = charAt(this.bp);
        if (this.ch == '{') {
            next();
            this.token = 12;
        } else if (this.ch == '[') {
            next();
            this.token = 14;
        } else if (this.ch == 'S' && charAt(this.bp + 1) == 'e' && charAt(this.bp + 2) == 't' && charAt(this.bp + 3) == '[') {
            this.bp += 3;
            this.ch = charAt(this.bp);
            this.token = 21;
        } else {
            nextToken();
        }
        return true;
    }

    public boolean matchField2(char[] cArr) {
        throw new UnsupportedOperationException();
    }

    public final int matchStat() {
        return this.matchStat;
    }

    public Collection<String> newCollectionByType(Class<?> cls) {
        if (cls.isAssignableFrom(HashSet.class)) {
            return new HashSet();
        }
        if (cls.isAssignableFrom(ArrayList.class)) {
            return new ArrayList();
        }
        try {
            return (Collection) cls.newInstance();
        } catch (Exception e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public abstract char next();

    public final void nextIdent() {
        while (isWhitespace(this.ch)) {
            next();
        }
        if (this.ch == '_' || this.ch == '$' || Character.isLetter(this.ch)) {
            scanIdent();
        } else {
            nextToken();
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final void nextToken() {
        this.sp = 0;
        while (true) {
            this.pos = this.bp;
            if (this.ch == '/') {
                skipComment();
            } else {
                if (this.ch == '\"') {
                    scanString();
                    return;
                }
                if (this.ch == ',') {
                    next();
                    this.token = 16;
                    return;
                }
                if (this.ch >= '0' && this.ch <= '9') {
                    scanNumber();
                    return;
                }
                if (this.ch == '-') {
                    scanNumber();
                    return;
                }
                switch (this.ch) {
                    case '\b':
                    case '\t':
                    case '\n':
                    case '\f':
                    case '\r':
                    case ' ':
                        next();
                        break;
                    case '\'':
                        if (!isEnabled(Feature.AllowSingleQuotes)) {
                            throw new JSONException("Feature.AllowSingleQuotes is false");
                        }
                        scanStringSingleQuote();
                        return;
                    case '(':
                        next();
                        this.token = 10;
                        return;
                    case ')':
                        next();
                        this.token = 11;
                        return;
                    case '+':
                        next();
                        scanNumber();
                        return;
                    case '.':
                        next();
                        this.token = 25;
                        return;
                    case ':':
                        next();
                        this.token = 17;
                        return;
                    case ';':
                        next();
                        this.token = 24;
                        return;
                    case 'N':
                    case 'S':
                    case 'T':
                    case 'u':
                        scanIdent();
                        return;
                    case '[':
                        next();
                        this.token = 14;
                        return;
                    case ']':
                        next();
                        this.token = 15;
                        return;
                    case 'f':
                        scanFalse();
                        return;
                    case 'n':
                        scanNullOrNew();
                        return;
                    case 't':
                        scanTrue();
                        return;
                    case GlMapUtil.DEVICE_DISPLAY_DPI_LOW /* 120 */:
                        scanHex();
                        return;
                    case '{':
                        next();
                        this.token = 12;
                        return;
                    case '}':
                        next();
                        this.token = 13;
                        return;
                    default:
                        if (isEOF()) {
                            if (this.token == 20) {
                                throw new JSONException("EOF error");
                            }
                            this.token = 20;
                            int i = this.eofPos;
                            this.bp = i;
                            this.pos = i;
                            return;
                        }
                        if (this.ch > 31 && this.ch != 127) {
                            lexError("illegal.char", String.valueOf((int) this.ch));
                            next();
                            return;
                        } else {
                            next();
                            break;
                        }
                }
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:12:0x0029. Please report as an issue. */
    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final void nextToken(int i) {
        this.sp = 0;
        while (true) {
            if (i != 2) {
                if (i != 4) {
                    if (i != 12) {
                        if (i == 18) {
                            nextIdent();
                            return;
                        }
                        if (i != 20) {
                            switch (i) {
                                case 14:
                                    if (this.ch == '[') {
                                        this.token = 14;
                                        next();
                                        return;
                                    } else if (this.ch == '{') {
                                        this.token = 12;
                                        next();
                                        return;
                                    }
                                    break;
                                case 15:
                                    if (this.ch == ']') {
                                        this.token = 15;
                                        next();
                                        return;
                                    }
                                    break;
                                case 16:
                                    if (this.ch == ',') {
                                        this.token = 16;
                                        next();
                                        return;
                                    } else if (this.ch == '}') {
                                        this.token = 13;
                                        next();
                                        return;
                                    } else if (this.ch == ']') {
                                        this.token = 15;
                                        next();
                                        return;
                                    } else if (this.ch == 26) {
                                        this.token = 20;
                                        return;
                                    }
                                    break;
                            }
                        }
                        if (this.ch == 26) {
                            this.token = 20;
                            return;
                        }
                    } else if (this.ch == '{') {
                        this.token = 12;
                        next();
                        return;
                    } else if (this.ch == '[') {
                        this.token = 14;
                        next();
                        return;
                    }
                } else {
                    if (this.ch == '\"') {
                        this.pos = this.bp;
                        scanString();
                        return;
                    }
                    if (this.ch >= '0' && this.ch <= '9') {
                        this.pos = this.bp;
                        scanNumber();
                        return;
                    } else if (this.ch == '[') {
                        this.token = 14;
                        next();
                        return;
                    } else if (this.ch == '{') {
                        this.token = 12;
                        next();
                        return;
                    }
                }
            } else {
                if (this.ch >= '0' && this.ch <= '9') {
                    this.pos = this.bp;
                    scanNumber();
                    return;
                }
                if (this.ch == '\"') {
                    this.pos = this.bp;
                    scanString();
                    return;
                } else if (this.ch == '[') {
                    this.token = 14;
                    next();
                    return;
                } else if (this.ch == '{') {
                    this.token = 12;
                    next();
                    return;
                }
            }
            if (this.ch != ' ' && this.ch != '\n' && this.ch != '\r' && this.ch != '\t' && this.ch != '\f' && this.ch != '\b') {
                nextToken();
                return;
            }
            next();
        }
    }

    public final void nextTokenWithChar(char c) {
        this.sp = 0;
        while (this.ch != c) {
            if (this.ch != ' ' && this.ch != '\n' && this.ch != '\r' && this.ch != '\t' && this.ch != '\f' && this.ch != '\b') {
                throw new JSONException("not match " + c + " - " + this.ch + ", info : " + info());
            }
            next();
        }
        next();
        nextToken();
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final void nextTokenWithColon() {
        nextTokenWithChar(':');
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final void nextTokenWithColon(int i) {
        nextTokenWithChar(':');
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public abstract String numberString();

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final int pos() {
        return this.pos;
    }

    protected final void putChar(char c) {
        if (this.sp == this.sbuf.length) {
            char[] cArr = new char[this.sbuf.length * 2];
            System.arraycopy(this.sbuf, 0, cArr, 0, this.sbuf.length);
            this.sbuf = cArr;
        }
        char[] cArr2 = this.sbuf;
        int i = this.sp;
        this.sp = i + 1;
        cArr2[i] = c;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final void resetStringPosition() {
        this.sp = 0;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public boolean scanBoolean(char c) {
        boolean z = false;
        this.matchStat = 0;
        char charAt = charAt(this.bp + 0);
        int i = 2;
        if (charAt == 't') {
            if (charAt(this.bp + 1) != 'r' || charAt(this.bp + 1 + 1) != 'u' || charAt(this.bp + 1 + 2) != 'e') {
                this.matchStat = -1;
                return false;
            }
            charAt = charAt(this.bp + 4);
            z = true;
            i = 5;
        } else if (charAt == 'f') {
            if (charAt(this.bp + 1) != 'a' || charAt(this.bp + 1 + 1) != 'l' || charAt(this.bp + 1 + 2) != 's' || charAt(this.bp + 1 + 3) != 'e') {
                this.matchStat = -1;
                return false;
            }
            charAt = charAt(this.bp + 5);
            i = 6;
        } else if (charAt == '1') {
            charAt = charAt(this.bp + 1);
            z = true;
        } else if (charAt == '0') {
            charAt = charAt(this.bp + 1);
        } else {
            i = 1;
        }
        while (charAt != c) {
            if (!isWhitespace(charAt)) {
                this.matchStat = -1;
                return z;
            }
            charAt = charAt(this.bp + i);
            i++;
        }
        this.bp += i;
        this.ch = charAt(this.bp);
        this.matchStat = 3;
        return z;
    }

    public Date scanDate(char c) {
        long j;
        int i;
        Date date;
        boolean z = false;
        this.matchStat = 0;
        char charAt = charAt(this.bp + 0);
        if (charAt == '\"') {
            int indexOf = indexOf(Typography.quote, this.bp + 1);
            if (indexOf == -1) {
                throw new JSONException("unclosed str");
            }
            int i2 = this.bp + 1;
            String subString = subString(i2, indexOf - i2);
            if (subString.indexOf(92) != -1) {
                while (true) {
                    int i3 = 0;
                    for (int i4 = indexOf - 1; i4 >= 0 && charAt(i4) == '\\'; i4--) {
                        i3++;
                    }
                    if (i3 % 2 == 0) {
                        break;
                    }
                    indexOf = indexOf(Typography.quote, indexOf + 1);
                }
                int i5 = indexOf - (this.bp + 1);
                subString = readString(sub_chars(this.bp + 1, i5), i5);
            }
            int i6 = (indexOf - (this.bp + 1)) + 1 + 1;
            int i7 = i6 + 1;
            charAt = charAt(this.bp + i6);
            JSONScanner jSONScanner = new JSONScanner(subString);
            try {
                if (!jSONScanner.scanISO8601DateIfMatch(false)) {
                    this.matchStat = -1;
                    return null;
                }
                date = jSONScanner.getCalendar().getTime();
                jSONScanner.close();
                i = i7;
            } finally {
                jSONScanner.close();
            }
        } else {
            int i8 = 2;
            char c2 = '9';
            char c3 = '0';
            if (charAt == '-' || (charAt >= '0' && charAt <= '9')) {
                if (charAt == '-') {
                    charAt = charAt(this.bp + 1);
                    z = true;
                } else {
                    i8 = 1;
                }
                if (charAt >= '0' && charAt <= '9') {
                    j = charAt - '0';
                    while (true) {
                        i = i8 + 1;
                        charAt = charAt(this.bp + i8);
                        if (charAt < c3 || charAt > c2) {
                            break;
                        }
                        j = (j * 10) + (charAt - '0');
                        i8 = i;
                        c2 = '9';
                        c3 = '0';
                    }
                } else {
                    j = 0;
                    i = i8;
                }
                if (j < 0) {
                    this.matchStat = -1;
                    return null;
                }
                if (z) {
                    j = -j;
                }
                date = new Date(j);
            } else {
                if (charAt != 'n' || charAt(this.bp + 1) != 'u' || charAt(this.bp + 1 + 1) != 'l' || charAt(this.bp + 1 + 2) != 'l') {
                    this.matchStat = -1;
                    return null;
                }
                this.matchStat = 5;
                charAt = charAt(this.bp + 4);
                date = null;
                i = 5;
            }
        }
        if (charAt == ',') {
            this.bp += i;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            this.token = 16;
            return date;
        }
        if (charAt != ']') {
            this.matchStat = -1;
            return null;
        }
        int i9 = i + 1;
        char charAt2 = charAt(this.bp + i);
        if (charAt2 == ',') {
            this.token = 16;
            this.bp += i9;
            this.ch = charAt(this.bp);
        } else if (charAt2 == ']') {
            this.token = 15;
            this.bp += i9;
            this.ch = charAt(this.bp);
        } else if (charAt2 == '}') {
            this.token = 13;
            this.bp += i9;
            this.ch = charAt(this.bp);
        } else {
            if (charAt2 != 26) {
                this.matchStat = -1;
                return null;
            }
            this.token = 20;
            this.bp += i9 - 1;
            this.ch = JSONLexer.EOI;
        }
        this.matchStat = 4;
        return date;
    }

    /* JADX WARN: Removed duplicated region for block: B:48:0x00ae A[ADDED_TO_REGION] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:46:0x00b0 -> B:43:0x009e). Please report as a decompilation issue!!! */
    @Override // com.alibaba.fastjson.parser.JSONLexer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.math.BigDecimal scanDecimal(char r19) {
        /*
            Method dump skipped, instructions count: 491
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanDecimal(char):java.math.BigDecimal");
    }

    /* JADX WARN: Removed duplicated region for block: B:47:0x00d7 A[ADDED_TO_REGION] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:46:0x00d9 -> B:43:0x00c7). Please report as a decompilation issue!!! */
    @Override // com.alibaba.fastjson.parser.JSONLexer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public double scanDouble(char r24) {
        /*
            Method dump skipped, instructions count: 468
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanDouble(char):double");
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public Enum<?> scanEnum(Class<?> cls, SymbolTable symbolTable, char c) {
        String scanSymbolWithSeperator = scanSymbolWithSeperator(symbolTable, c);
        if (scanSymbolWithSeperator == null) {
            return null;
        }
        return Enum.valueOf(cls, scanSymbolWithSeperator);
    }

    public final void scanFalse() {
        if (this.ch != 'f') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'a') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'l') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 's') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != ' ' && this.ch != ',' && this.ch != '}' && this.ch != ']' && this.ch != '\n' && this.ch != '\r' && this.ch != '\t' && this.ch != 26 && this.ch != '\f' && this.ch != '\b' && this.ch != ':' && this.ch != '/') {
            throw new JSONException("scan false error");
        }
        this.token = 7;
    }

    public BigInteger scanFieldBigInteger(char[] cArr) {
        int i;
        char charAt;
        int length;
        int i2;
        BigInteger valueOf;
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return null;
        }
        int length2 = cArr.length;
        int i3 = length2 + 1;
        char charAt2 = charAt(this.bp + length2);
        boolean z = charAt2 == '\"';
        if (z) {
            charAt2 = charAt(this.bp + i3);
            i3++;
        }
        boolean z2 = charAt2 == '-';
        if (z2) {
            charAt2 = charAt(this.bp + i3);
            i3++;
        }
        char c = '0';
        if (charAt2 < '0' || charAt2 > '9') {
            if (charAt2 != 'n' || charAt(this.bp + i3) != 'u' || charAt(this.bp + i3 + 1) != 'l' || charAt(this.bp + i3 + 2) != 'l') {
                this.matchStat = -1;
                return null;
            }
            this.matchStat = 5;
            int i4 = i3 + 3;
            int i5 = i4 + 1;
            char charAt3 = charAt(this.bp + i4);
            if (z && charAt3 == '\"') {
                charAt3 = charAt(this.bp + i5);
                i5++;
            }
            while (charAt3 != ',') {
                if (charAt3 == '}') {
                    this.bp += i5;
                    this.ch = charAt(this.bp);
                    this.matchStat = 5;
                    this.token = 13;
                    return null;
                }
                if (!isWhitespace(charAt3)) {
                    this.matchStat = -1;
                    return null;
                }
                charAt3 = charAt(this.bp + i5);
                i5++;
            }
            this.bp += i5;
            this.ch = charAt(this.bp);
            this.matchStat = 5;
            this.token = 16;
            return null;
        }
        long j = charAt2 - '0';
        while (true) {
            i = i3 + 1;
            charAt = charAt(this.bp + i3);
            if (charAt < c || charAt > '9') {
                break;
            }
            j = (j * 10) + (charAt - '0');
            i3 = i;
            c = '0';
        }
        if (!z) {
            length = this.bp + cArr.length;
            i2 = ((this.bp + i) - length) - 1;
        } else {
            if (charAt != '\"') {
                this.matchStat = -1;
                return null;
            }
            int i6 = i + 1;
            charAt = charAt(this.bp + i);
            length = this.bp + cArr.length + 1;
            i2 = ((this.bp + i6) - length) - 2;
            i = i6;
        }
        if (i2 < 20 || (z2 && i2 < 21)) {
            if (z2) {
                j = -j;
            }
            valueOf = BigInteger.valueOf(j);
        } else {
            valueOf = new BigInteger(subString(length, i2));
        }
        if (charAt == ',') {
            this.bp += i;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            this.token = 16;
            return valueOf;
        }
        if (charAt != '}') {
            this.matchStat = -1;
            return null;
        }
        int i7 = i + 1;
        char charAt4 = charAt(this.bp + i);
        if (charAt4 == ',') {
            this.token = 16;
            this.bp += i7;
            this.ch = charAt(this.bp);
        } else if (charAt4 == ']') {
            this.token = 15;
            this.bp += i7;
            this.ch = charAt(this.bp);
        } else if (charAt4 == '}') {
            this.token = 13;
            this.bp += i7;
            this.ch = charAt(this.bp);
        } else {
            if (charAt4 != 26) {
                this.matchStat = -1;
                return null;
            }
            this.token = 20;
            this.bp += i7 - 1;
            this.ch = JSONLexer.EOI;
        }
        this.matchStat = 4;
        return valueOf;
    }

    public boolean scanFieldBoolean(char[] cArr) {
        int i;
        boolean z;
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return false;
        }
        int length = cArr.length;
        int i2 = length + 1;
        char charAt = charAt(this.bp + length);
        if (charAt == 't') {
            int i3 = i2 + 1;
            if (charAt(this.bp + i2) != 'r') {
                this.matchStat = -1;
                return false;
            }
            int i4 = i3 + 1;
            if (charAt(this.bp + i3) != 'u') {
                this.matchStat = -1;
                return false;
            }
            i = i4 + 1;
            if (charAt(this.bp + i4) != 'e') {
                this.matchStat = -1;
                return false;
            }
            z = true;
        } else {
            if (charAt != 'f') {
                this.matchStat = -1;
                return false;
            }
            int i5 = i2 + 1;
            if (charAt(this.bp + i2) != 'a') {
                this.matchStat = -1;
                return false;
            }
            int i6 = i5 + 1;
            if (charAt(this.bp + i5) != 'l') {
                this.matchStat = -1;
                return false;
            }
            int i7 = i6 + 1;
            if (charAt(this.bp + i6) != 's') {
                this.matchStat = -1;
                return false;
            }
            int i8 = i7 + 1;
            if (charAt(this.bp + i7) != 'e') {
                this.matchStat = -1;
                return false;
            }
            i = i8;
            z = false;
        }
        int i9 = i + 1;
        char charAt2 = charAt(this.bp + i);
        if (charAt2 == ',') {
            this.bp += i9;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            this.token = 16;
            return z;
        }
        if (charAt2 != '}') {
            this.matchStat = -1;
            return false;
        }
        int i10 = i9 + 1;
        char charAt3 = charAt(this.bp + i9);
        if (charAt3 == ',') {
            this.token = 16;
            this.bp += i10;
            this.ch = charAt(this.bp);
        } else if (charAt3 == ']') {
            this.token = 15;
            this.bp += i10;
            this.ch = charAt(this.bp);
        } else if (charAt3 == '}') {
            this.token = 13;
            this.bp += i10;
            this.ch = charAt(this.bp);
        } else {
            if (charAt3 != 26) {
                this.matchStat = -1;
                return false;
            }
            this.token = 20;
            this.bp += i10 - 1;
            this.ch = JSONLexer.EOI;
        }
        this.matchStat = 4;
        return z;
    }

    public Date scanFieldDate(char[] cArr) {
        char c;
        int i;
        long j;
        Date date;
        boolean z = false;
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return null;
        }
        int length = cArr.length;
        int i2 = length + 1;
        char charAt = charAt(this.bp + length);
        if (charAt == '\"') {
            int indexOf = indexOf(Typography.quote, this.bp + cArr.length + 1);
            if (indexOf == -1) {
                throw new JSONException("unclosed str");
            }
            int length2 = this.bp + cArr.length + 1;
            String subString = subString(length2, indexOf - length2);
            if (subString.indexOf(92) != -1) {
                while (true) {
                    int i3 = 0;
                    for (int i4 = indexOf - 1; i4 >= 0 && charAt(i4) == '\\'; i4--) {
                        i3++;
                    }
                    if (i3 % 2 == 0) {
                        break;
                    }
                    indexOf = indexOf(Typography.quote, indexOf + 1);
                }
                int length3 = indexOf - ((this.bp + cArr.length) + 1);
                subString = readString(sub_chars(this.bp + cArr.length + 1, length3), length3);
            }
            int length4 = i2 + (indexOf - ((this.bp + cArr.length) + 1)) + 1;
            i = length4 + 1;
            c = charAt(this.bp + length4);
            JSONScanner jSONScanner = new JSONScanner(subString);
            try {
                if (!jSONScanner.scanISO8601DateIfMatch(false)) {
                    this.matchStat = -1;
                    return null;
                }
                date = jSONScanner.getCalendar().getTime();
            } finally {
                jSONScanner.close();
            }
        } else {
            if (charAt != '-' && (charAt < '0' || charAt > '9')) {
                this.matchStat = -1;
                return null;
            }
            if (charAt == '-') {
                charAt = charAt(this.bp + i2);
                i2++;
                z = true;
            }
            if (charAt >= '0' && charAt <= '9') {
                j = charAt - '0';
                while (true) {
                    i = i2 + 1;
                    c = charAt(this.bp + i2);
                    if (c < '0' || c > '9') {
                        break;
                    }
                    j = (j * 10) + (c - '0');
                    i2 = i;
                }
            } else {
                c = charAt;
                i = i2;
                j = 0;
            }
            if (j < 0) {
                this.matchStat = -1;
                return null;
            }
            if (z) {
                j = -j;
            }
            date = new Date(j);
        }
        if (c == ',') {
            this.bp += i;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            return date;
        }
        if (c != '}') {
            this.matchStat = -1;
            return null;
        }
        int i5 = i + 1;
        char charAt2 = charAt(this.bp + i);
        if (charAt2 == ',') {
            this.token = 16;
            this.bp += i5;
            this.ch = charAt(this.bp);
        } else if (charAt2 == ']') {
            this.token = 15;
            this.bp += i5;
            this.ch = charAt(this.bp);
        } else if (charAt2 == '}') {
            this.token = 13;
            this.bp += i5;
            this.ch = charAt(this.bp);
        } else {
            if (charAt2 != 26) {
                this.matchStat = -1;
                return null;
            }
            this.token = 20;
            this.bp += i5 - 1;
            this.ch = JSONLexer.EOI;
        }
        this.matchStat = 4;
        return date;
    }

    /* JADX WARN: Removed duplicated region for block: B:51:0x00bc A[ADDED_TO_REGION] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:49:0x00be -> B:46:0x00ac). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.math.BigDecimal scanFieldDecimal(char[] r19) {
        /*
            Method dump skipped, instructions count: 510
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanFieldDecimal(char[]):java.math.BigDecimal");
    }

    public final double scanFieldDouble(char[] cArr) {
        int i;
        char charAt;
        boolean z;
        long j;
        int length;
        int i2;
        char c;
        int i3;
        double parseDouble;
        int i4;
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return 0.0d;
        }
        int length2 = cArr.length;
        int i5 = length2 + 1;
        char charAt2 = charAt(this.bp + length2);
        boolean z2 = charAt2 == '\"';
        if (z2) {
            charAt2 = charAt(this.bp + i5);
            i5++;
        }
        boolean z3 = charAt2 == '-';
        if (z3) {
            charAt2 = charAt(this.bp + i5);
            i5++;
        }
        char c2 = '0';
        if (charAt2 < '0' || charAt2 > '9') {
            boolean z4 = z2;
            if (charAt2 != 'n' || charAt(this.bp + i5) != 'u' || charAt(this.bp + i5 + 1) != 'l' || charAt(this.bp + i5 + 2) != 'l') {
                this.matchStat = -1;
                return 0.0d;
            }
            this.matchStat = 5;
            int i6 = i5 + 3;
            int i7 = i6 + 1;
            char charAt3 = charAt(this.bp + i6);
            if (z4 && charAt3 == '\"') {
                charAt3 = charAt(this.bp + i7);
                i7++;
            }
            while (charAt3 != ',') {
                if (charAt3 == '}') {
                    this.bp += i7;
                    this.ch = charAt(this.bp);
                    this.matchStat = 5;
                    this.token = 13;
                    return 0.0d;
                }
                if (!isWhitespace(charAt3)) {
                    this.matchStat = -1;
                    return 0.0d;
                }
                charAt3 = charAt(this.bp + i7);
                i7++;
            }
            this.bp += i7;
            this.ch = charAt(this.bp);
            this.matchStat = 5;
            this.token = 16;
            return 0.0d;
        }
        long j2 = charAt2 - '0';
        while (true) {
            i = i5 + 1;
            charAt = charAt(this.bp + i5);
            if (charAt < '0' || charAt > '9') {
                break;
            }
            j2 = (j2 * 10) + (charAt - '0');
            i5 = i;
        }
        if (charAt == '.') {
            int i8 = i + 1;
            char charAt4 = charAt(this.bp + i);
            if (charAt4 < '0' || charAt4 > '9') {
                this.matchStat = -1;
                return 0.0d;
            }
            z = z2;
            j2 = (j2 * 10) + (charAt4 - '0');
            j = 10;
            while (true) {
                i4 = i8 + 1;
                charAt = charAt(this.bp + i8);
                if (charAt < c2 || charAt > '9') {
                    break;
                }
                j2 = (j2 * 10) + (charAt - '0');
                j *= 10;
                i8 = i4;
                c2 = '0';
            }
            i = i4;
        } else {
            z = z2;
            j = 1;
        }
        boolean z5 = charAt == 'e' || charAt == 'E';
        if (z5) {
            int i9 = i + 1;
            char charAt5 = charAt(this.bp + i);
            if (charAt5 == '+' || charAt5 == '-') {
                int i10 = i9 + 1;
                charAt = charAt(this.bp + i9);
                i = i10;
            } else {
                i = i9;
                charAt = charAt5;
            }
            while (charAt >= '0' && charAt <= '9') {
                charAt = charAt(this.bp + i);
                i++;
            }
        }
        if (!z) {
            length = this.bp + cArr.length;
            i2 = ((this.bp + i) - length) - 1;
            c = charAt;
            i3 = i;
        } else {
            if (charAt != '\"') {
                this.matchStat = -1;
                return 0.0d;
            }
            i3 = i + 1;
            c = charAt(this.bp + i);
            length = this.bp + cArr.length + 1;
            i2 = ((this.bp + i3) - length) - 2;
        }
        if (z5 || i2 >= 20) {
            parseDouble = Double.parseDouble(subString(length, i2));
        } else {
            double d = j2;
            double d2 = j;
            Double.isNaN(d);
            Double.isNaN(d2);
            parseDouble = d / d2;
            if (z3) {
                parseDouble = -parseDouble;
            }
        }
        if (c == ',') {
            this.bp += i3;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            this.token = 16;
            return parseDouble;
        }
        if (c != '}') {
            this.matchStat = -1;
            return 0.0d;
        }
        int i11 = i3 + 1;
        char charAt6 = charAt(this.bp + i3);
        if (charAt6 == ',') {
            this.token = 16;
            this.bp += i11;
            this.ch = charAt(this.bp);
        } else if (charAt6 == ']') {
            this.token = 15;
            this.bp += i11;
            this.ch = charAt(this.bp);
        } else if (charAt6 == '}') {
            this.token = 13;
            this.bp += i11;
            this.ch = charAt(this.bp);
        } else {
            if (charAt6 != 26) {
                this.matchStat = -1;
                return 0.0d;
            }
            this.token = 20;
            this.bp += i11 - 1;
            this.ch = JSONLexer.EOI;
        }
        this.matchStat = 4;
        return parseDouble;
    }

    public final float scanFieldFloat(char[] cArr) {
        int i;
        char charAt;
        int i2;
        int length;
        int i3;
        float parseFloat;
        char charAt2;
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return 0.0f;
        }
        int length2 = cArr.length;
        int i4 = length2 + 1;
        char charAt3 = charAt(this.bp + length2);
        boolean z = charAt3 == '\"';
        if (z) {
            charAt3 = charAt(this.bp + i4);
            i4++;
        }
        boolean z2 = charAt3 == '-';
        if (z2) {
            charAt3 = charAt(this.bp + i4);
            i4++;
        }
        if (charAt3 < '0' || charAt3 > '9') {
            if (charAt3 != 'n' || charAt(this.bp + i4) != 'u' || charAt(this.bp + i4 + 1) != 'l' || charAt(this.bp + i4 + 2) != 'l') {
                this.matchStat = -1;
                return 0.0f;
            }
            this.matchStat = 5;
            int i5 = i4 + 3;
            int i6 = i5 + 1;
            char charAt4 = charAt(this.bp + i5);
            if (z && charAt4 == '\"') {
                charAt4 = charAt(this.bp + i6);
                i6++;
            }
            while (charAt4 != ',') {
                if (charAt4 == '}') {
                    this.bp += i6;
                    this.ch = charAt(this.bp);
                    this.matchStat = 5;
                    this.token = 13;
                    return 0.0f;
                }
                if (!isWhitespace(charAt4)) {
                    this.matchStat = -1;
                    return 0.0f;
                }
                charAt4 = charAt(this.bp + i6);
                i6++;
            }
            this.bp += i6;
            this.ch = charAt(this.bp);
            this.matchStat = 5;
            this.token = 16;
            return 0.0f;
        }
        int i7 = charAt3 - '0';
        while (true) {
            i = i4 + 1;
            charAt = charAt(this.bp + i4);
            if (charAt < '0' || charAt > '9') {
                break;
            }
            i7 = (i7 * 10) + (charAt - '0');
            i4 = i;
        }
        if (charAt == '.') {
            int i8 = i + 1;
            char charAt5 = charAt(this.bp + i);
            if (charAt5 < '0' || charAt5 > '9') {
                this.matchStat = -1;
                return 0.0f;
            }
            i7 = (i7 * 10) + (charAt5 - '0');
            int i9 = 10;
            while (true) {
                i = i8 + 1;
                charAt2 = charAt(this.bp + i8);
                if (charAt2 < '0' || charAt2 > '9') {
                    break;
                }
                i7 = (i7 * 10) + (charAt2 - '0');
                i9 *= 10;
                i8 = i;
            }
            i2 = i9;
            charAt = charAt2;
        } else {
            i2 = 1;
        }
        boolean z3 = charAt == 'e' || charAt == 'E';
        if (z3) {
            int i10 = i + 1;
            charAt = charAt(this.bp + i);
            if (charAt == '+' || charAt == '-') {
                int i11 = i10 + 1;
                charAt = charAt(this.bp + i10);
                i = i11;
            } else {
                i = i10;
            }
            while (charAt >= '0' && charAt <= '9') {
                int i12 = i + 1;
                charAt = charAt(this.bp + i);
                i = i12;
            }
        }
        if (!z) {
            length = this.bp + cArr.length;
            i3 = ((this.bp + i) - length) - 1;
        } else {
            if (charAt != '\"') {
                this.matchStat = -1;
                return 0.0f;
            }
            int i13 = i + 1;
            charAt = charAt(this.bp + i);
            length = this.bp + cArr.length + 1;
            i3 = ((this.bp + i13) - length) - 2;
            i = i13;
        }
        if (z3 || i3 >= 17) {
            parseFloat = Float.parseFloat(subString(length, i3));
        } else {
            parseFloat = i7 / i2;
            if (z2) {
                parseFloat = -parseFloat;
            }
        }
        if (charAt == ',') {
            this.bp += i;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            this.token = 16;
            return parseFloat;
        }
        if (charAt != '}') {
            this.matchStat = -1;
            return 0.0f;
        }
        int i14 = i + 1;
        char charAt6 = charAt(this.bp + i);
        if (charAt6 == ',') {
            this.token = 16;
            this.bp += i14;
            this.ch = charAt(this.bp);
        } else if (charAt6 == ']') {
            this.token = 15;
            this.bp += i14;
            this.ch = charAt(this.bp);
        } else if (charAt6 == '}') {
            this.token = 13;
            this.bp += i14;
            this.ch = charAt(this.bp);
        } else {
            if (charAt6 != 26) {
                this.matchStat = -1;
                return 0.0f;
            }
            this.bp += i14 - 1;
            this.token = 20;
            this.ch = JSONLexer.EOI;
        }
        this.matchStat = 4;
        return parseFloat;
    }

    /* JADX WARN: Code restructure failed: missing block: B:111:0x01b3, code lost:
    
        r2 = r4;
        r18.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x01b6, code lost:
    
        return r2;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final float[] scanFieldFloatArray(char[] r19) {
        /*
            Method dump skipped, instructions count: 439
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanFieldFloatArray(char[]):float[]");
    }

    /* JADX WARN: Code restructure failed: missing block: B:130:0x00b3, code lost:
    
        r19.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:131:0x00b7, code lost:
    
        return r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:136:0x0227, code lost:
    
        r19.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:137:0x022d, code lost:
    
        return r4;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final float[][] scanFieldFloatArray2(char[] r20) {
        /*
            Method dump skipped, instructions count: 562
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanFieldFloatArray2(char[]):float[][]");
    }

    public int scanFieldInt(char[] cArr) {
        int i;
        char charAt;
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return 0;
        }
        int length = cArr.length;
        int i2 = length + 1;
        char charAt2 = charAt(this.bp + length);
        boolean z = charAt2 == '-';
        if (z) {
            charAt2 = charAt(this.bp + i2);
            i2++;
        }
        if (charAt2 < '0' || charAt2 > '9') {
            this.matchStat = -1;
            return 0;
        }
        int i3 = charAt2 - '0';
        while (true) {
            i = i2 + 1;
            charAt = charAt(this.bp + i2);
            if (charAt < '0' || charAt > '9') {
                break;
            }
            i3 = (i3 * 10) + (charAt - '0');
            i2 = i;
        }
        if (charAt == '.') {
            this.matchStat = -1;
            return 0;
        }
        if ((i3 < 0 || i > cArr.length + 14) && !(i3 == Integer.MIN_VALUE && i == 17 && z)) {
            this.matchStat = -1;
            return 0;
        }
        if (charAt == ',') {
            this.bp += i;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            this.token = 16;
            return z ? -i3 : i3;
        }
        if (charAt != '}') {
            this.matchStat = -1;
            return 0;
        }
        int i4 = i + 1;
        char charAt3 = charAt(this.bp + i);
        if (charAt3 == ',') {
            this.token = 16;
            this.bp += i4;
            this.ch = charAt(this.bp);
        } else if (charAt3 == ']') {
            this.token = 15;
            this.bp += i4;
            this.ch = charAt(this.bp);
        } else if (charAt3 == '}') {
            this.token = 13;
            this.bp += i4;
            this.ch = charAt(this.bp);
        } else {
            if (charAt3 != 26) {
                this.matchStat = -1;
                return 0;
            }
            this.token = 20;
            this.bp += i4 - 1;
            this.ch = JSONLexer.EOI;
        }
        this.matchStat = 4;
        return z ? -i3 : i3;
    }

    public final int[] scanFieldIntArray(char[] cArr) {
        boolean z;
        int i;
        char charAt;
        int i2;
        int i3;
        char charAt2;
        int[] iArr;
        char c;
        int[] iArr2;
        this.matchStat = 0;
        int[] iArr3 = null;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return null;
        }
        int length = cArr.length;
        int i4 = length + 1;
        if (charAt(this.bp + length) != '[') {
            this.matchStat = -2;
            return null;
        }
        int i5 = i4 + 1;
        char charAt3 = charAt(this.bp + i4);
        int[] iArr4 = new int[16];
        if (charAt3 != ']') {
            int i6 = 0;
            while (true) {
                if (charAt3 == '-') {
                    charAt3 = charAt(this.bp + i5);
                    i5++;
                    z = true;
                } else {
                    z = false;
                }
                if (charAt3 < '0' || charAt3 > '9') {
                    break;
                }
                int i7 = charAt3 - '0';
                while (true) {
                    i = i5 + 1;
                    charAt = charAt(this.bp + i5);
                    if (charAt < '0' || charAt > '9') {
                        break;
                    }
                    i7 = (i7 * 10) + (charAt - '0');
                    i5 = i;
                }
                if (i6 >= iArr4.length) {
                    int[] iArr5 = new int[(iArr4.length * 3) / 2];
                    System.arraycopy(iArr4, 0, iArr5, 0, i6);
                    iArr4 = iArr5;
                }
                i2 = i6 + 1;
                if (z) {
                    i7 = -i7;
                }
                iArr4[i6] = i7;
                if (charAt == ',') {
                    i5 = i + 1;
                    c = charAt(this.bp + i);
                    iArr = null;
                } else {
                    if (charAt == ']') {
                        i3 = i + 1;
                        charAt2 = charAt(this.bp + i);
                        break;
                    }
                    iArr = null;
                    c = charAt;
                    i5 = i;
                }
                iArr3 = iArr;
                charAt3 = c;
                i6 = i2;
            }
            int[] iArr6 = iArr3;
            this.matchStat = -1;
            return iArr6;
        }
        i3 = i5 + 1;
        charAt2 = charAt(this.bp + i5);
        i2 = 0;
        if (i2 != iArr4.length) {
            iArr2 = new int[i2];
            System.arraycopy(iArr4, 0, iArr2, 0, i2);
        } else {
            iArr2 = iArr4;
        }
        if (charAt2 == ',') {
            this.bp += i3 - 1;
            next();
            this.matchStat = 3;
            this.token = 16;
            return iArr2;
        }
        if (charAt2 != '}') {
            this.matchStat = -1;
            return null;
        }
        int i8 = i3 + 1;
        char charAt4 = charAt(this.bp + i3);
        if (charAt4 == ',') {
            this.token = 16;
            this.bp += i8 - 1;
            next();
        } else if (charAt4 == ']') {
            this.token = 15;
            this.bp += i8 - 1;
            next();
        } else if (charAt4 == '}') {
            this.token = 13;
            this.bp += i8 - 1;
            next();
        } else {
            if (charAt4 != 26) {
                this.matchStat = -1;
                return null;
            }
            this.bp += i8 - 1;
            this.token = 20;
            this.ch = JSONLexer.EOI;
        }
        this.matchStat = 4;
        return iArr2;
    }

    public long scanFieldLong(char[] cArr) {
        int i;
        boolean z;
        int i2;
        char charAt;
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return 0L;
        }
        int length = cArr.length;
        int i3 = length + 1;
        char charAt2 = charAt(this.bp + length);
        if (charAt2 == '-') {
            i = i3 + 1;
            charAt2 = charAt(this.bp + i3);
            z = true;
        } else {
            i = i3;
            z = false;
        }
        if (charAt2 < '0' || charAt2 > '9') {
            this.matchStat = -1;
            return 0L;
        }
        long j = charAt2 - '0';
        while (true) {
            i2 = i + 1;
            charAt = charAt(this.bp + i);
            if (charAt < '0' || charAt > '9') {
                break;
            }
            j = (j * 10) + (charAt - '0');
            i = i2;
        }
        if (charAt == '.') {
            this.matchStat = -1;
            return 0L;
        }
        if (!(i2 - cArr.length < 21 && (j >= 0 || (j == Long.MIN_VALUE && z)))) {
            this.matchStat = -1;
            return 0L;
        }
        if (charAt == ',') {
            this.bp += i2;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            this.token = 16;
            return z ? -j : j;
        }
        if (charAt != '}') {
            this.matchStat = -1;
            return 0L;
        }
        int i4 = i2 + 1;
        char charAt3 = charAt(this.bp + i2);
        if (charAt3 == ',') {
            this.token = 16;
            this.bp += i4;
            this.ch = charAt(this.bp);
        } else if (charAt3 == ']') {
            this.token = 15;
            this.bp += i4;
            this.ch = charAt(this.bp);
        } else if (charAt3 == '}') {
            this.token = 13;
            this.bp += i4;
            this.ch = charAt(this.bp);
        } else {
            if (charAt3 != 26) {
                this.matchStat = -1;
                return 0L;
            }
            this.token = 20;
            this.bp += i4 - 1;
            this.ch = JSONLexer.EOI;
        }
        this.matchStat = 4;
        return z ? -j : j;
    }

    public String scanFieldString(char[] cArr) {
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return stringDefaultValue();
        }
        int length = cArr.length;
        int i = length + 1;
        if (charAt(this.bp + length) != '\"') {
            this.matchStat = -1;
            return stringDefaultValue();
        }
        int indexOf = indexOf(Typography.quote, this.bp + cArr.length + 1);
        if (indexOf == -1) {
            throw new JSONException("unclosed str");
        }
        int length2 = this.bp + cArr.length + 1;
        String subString = subString(length2, indexOf - length2);
        if (subString.indexOf(92) != -1) {
            while (true) {
                int i2 = 0;
                for (int i3 = indexOf - 1; i3 >= 0 && charAt(i3) == '\\'; i3--) {
                    i2++;
                }
                if (i2 % 2 == 0) {
                    break;
                }
                indexOf = indexOf(Typography.quote, indexOf + 1);
            }
            int length3 = indexOf - ((this.bp + cArr.length) + 1);
            subString = readString(sub_chars(this.bp + cArr.length + 1, length3), length3);
        }
        int length4 = i + (indexOf - ((this.bp + cArr.length) + 1)) + 1;
        int i4 = length4 + 1;
        char charAt = charAt(this.bp + length4);
        if (charAt == ',') {
            this.bp += i4;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            return subString;
        }
        if (charAt != '}') {
            this.matchStat = -1;
            return stringDefaultValue();
        }
        int i5 = i4 + 1;
        char charAt2 = charAt(this.bp + i4);
        if (charAt2 == ',') {
            this.token = 16;
            this.bp += i5;
            this.ch = charAt(this.bp);
        } else if (charAt2 == ']') {
            this.token = 15;
            this.bp += i5;
            this.ch = charAt(this.bp);
        } else if (charAt2 == '}') {
            this.token = 13;
            this.bp += i5;
            this.ch = charAt(this.bp);
        } else {
            if (charAt2 != 26) {
                this.matchStat = -1;
                return stringDefaultValue();
            }
            this.token = 20;
            this.bp += i5 - 1;
            this.ch = JSONLexer.EOI;
        }
        this.matchStat = 4;
        return subString;
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x00f9, code lost:
    
        if (r12 != ',') goto L51;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00fb, code lost:
    
        r11.bp += r0;
        r11.ch = charAt(r11.bp);
        r11.matchStat = 3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x010b, code lost:
    
        return r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x010e, code lost:
    
        if (r12 != '}') goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0110, code lost:
    
        r6 = r0 + 1;
        r12 = charAt(r11.bp + r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0119, code lost:
    
        if (r12 != ',') goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x011b, code lost:
    
        r11.token = 16;
        r11.bp += r6;
        r11.ch = charAt(r11.bp);
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0166, code lost:
    
        r11.matchStat = 4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0169, code lost:
    
        return r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x012d, code lost:
    
        if (r12 != ']') goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x012f, code lost:
    
        r11.token = 15;
        r11.bp += r6;
        r11.ch = charAt(r11.bp);
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0141, code lost:
    
        if (r12 != '}') goto L60;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0143, code lost:
    
        r11.token = 13;
        r11.bp += r6;
        r11.ch = charAt(r11.bp);
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0157, code lost:
    
        if (r12 != 26) goto L65;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0159, code lost:
    
        r11.bp += r6 - 1;
        r11.token = 20;
        r11.ch = com.alibaba.fastjson.parser.JSONLexer.EOI;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x016a, code lost:
    
        r11.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x016c, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x016d, code lost:
    
        r11.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x016f, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x00ee, code lost:
    
        if (r13.size() != 0) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x00f0, code lost:
    
        r0 = r1 + 1;
        r12 = charAt(r11.bp + r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0177, code lost:
    
        throw new com.alibaba.fastjson.JSONException("illega str");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.util.Collection<java.lang.String> scanFieldStringArray(char[] r12, java.lang.Class<?> r13) {
        /*
            Method dump skipped, instructions count: 376
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanFieldStringArray(char[], java.lang.Class):java.util.Collection");
    }

    public String[] scanFieldStringArray(char[] cArr, int i, SymbolTable symbolTable) {
        throw new UnsupportedOperationException();
    }

    public long scanFieldSymbol(char[] cArr) {
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return 0L;
        }
        int length = cArr.length;
        int i = length + 1;
        if (charAt(this.bp + length) != '\"') {
            this.matchStat = -1;
            return 0L;
        }
        long j = -3750763034362895579L;
        while (true) {
            int i2 = i + 1;
            char charAt = charAt(this.bp + i);
            if (charAt == '\"') {
                int i3 = i2 + 1;
                char charAt2 = charAt(this.bp + i2);
                if (charAt2 == ',') {
                    this.bp += i3;
                    this.ch = charAt(this.bp);
                    this.matchStat = 3;
                    return j;
                }
                if (charAt2 != '}') {
                    this.matchStat = -1;
                    return 0L;
                }
                int i4 = i3 + 1;
                char charAt3 = charAt(this.bp + i3);
                if (charAt3 == ',') {
                    this.token = 16;
                    this.bp += i4;
                    this.ch = charAt(this.bp);
                } else if (charAt3 == ']') {
                    this.token = 15;
                    this.bp += i4;
                    this.ch = charAt(this.bp);
                } else if (charAt3 == '}') {
                    this.token = 13;
                    this.bp += i4;
                    this.ch = charAt(this.bp);
                } else {
                    if (charAt3 != 26) {
                        this.matchStat = -1;
                        return 0L;
                    }
                    this.token = 20;
                    this.bp += i4 - 1;
                    this.ch = JSONLexer.EOI;
                }
                this.matchStat = 4;
                return j;
            }
            j = (j ^ charAt) * 1099511628211L;
            if (charAt == '\\') {
                this.matchStat = -1;
                return 0L;
            }
            i = i2;
        }
    }

    public UUID scanFieldUUID(char[] cArr) {
        int i;
        char charAt;
        UUID uuid;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return null;
        }
        int length = cArr.length;
        int i9 = length + 1;
        char charAt2 = charAt(this.bp + length);
        char c = 4;
        if (charAt2 != '\"') {
            if (charAt2 == 'n') {
                int i10 = i9 + 1;
                if (charAt(this.bp + i9) == 'u') {
                    int i11 = i10 + 1;
                    if (charAt(this.bp + i10) == 'l') {
                        int i12 = i11 + 1;
                        if (charAt(this.bp + i11) == 'l') {
                            i = i12 + 1;
                            charAt = charAt(this.bp + i12);
                            uuid = null;
                        }
                    }
                }
            }
            this.matchStat = -1;
            return null;
        }
        int indexOf = indexOf(Typography.quote, this.bp + cArr.length + 1);
        if (indexOf == -1) {
            throw new JSONException("unclosed str");
        }
        int length2 = this.bp + cArr.length + 1;
        int i13 = indexOf - length2;
        char c2 = 'F';
        char c3 = 'f';
        char c4 = 'A';
        char c5 = 'a';
        char c6 = '0';
        if (i13 == 36) {
            int i14 = 0;
            long j = 0;
            while (i14 < 8) {
                char charAt3 = charAt(length2 + i14);
                if (charAt3 >= '0' && charAt3 <= '9') {
                    i8 = charAt3 - '0';
                } else if (charAt3 >= 'a' && charAt3 <= 'f') {
                    i8 = (charAt3 - 'a') + 10;
                } else {
                    if (charAt3 < c4 || charAt3 > c2) {
                        this.matchStat = -2;
                        return null;
                    }
                    i8 = (charAt3 - 'A') + 10;
                }
                j = (j << 4) | i8;
                i14++;
                c4 = 'A';
                c2 = 'F';
            }
            int i15 = 9;
            while (i15 < 13) {
                char charAt4 = charAt(length2 + i15);
                if (charAt4 >= '0' && charAt4 <= '9') {
                    i7 = charAt4 - '0';
                } else if (charAt4 >= 'a' && charAt4 <= c3) {
                    i7 = (charAt4 - 'a') + 10;
                } else {
                    if (charAt4 < 'A' || charAt4 > 'F') {
                        this.matchStat = -2;
                        return null;
                    }
                    i7 = (charAt4 - 'A') + 10;
                }
                j = (j << 4) | i7;
                i15++;
                indexOf = indexOf;
                c3 = 'f';
            }
            int i16 = indexOf;
            long j2 = j;
            for (int i17 = 14; i17 < 18; i17++) {
                char charAt5 = charAt(length2 + i17);
                if (charAt5 >= '0' && charAt5 <= '9') {
                    i6 = charAt5 - '0';
                } else if (charAt5 >= 'a' && charAt5 <= 'f') {
                    i6 = (charAt5 - 'a') + 10;
                } else {
                    if (charAt5 < 'A' || charAt5 > 'F') {
                        this.matchStat = -2;
                        return null;
                    }
                    i6 = (charAt5 - 'A') + 10;
                }
                j2 = (j2 << 4) | i6;
            }
            int i18 = 19;
            long j3 = 0;
            while (i18 < 23) {
                char charAt6 = charAt(length2 + i18);
                if (charAt6 >= '0' && charAt6 <= '9') {
                    i5 = charAt6 - '0';
                } else if (charAt6 >= 'a' && charAt6 <= 'f') {
                    i5 = (charAt6 - 'a') + 10;
                } else {
                    if (charAt6 < 'A' || charAt6 > 'F') {
                        this.matchStat = -2;
                        return null;
                    }
                    i5 = (charAt6 - 'A') + 10;
                }
                j3 = (j3 << c) | i5;
                i18++;
                j2 = j2;
                c = 4;
            }
            long j4 = j2;
            long j5 = j3;
            for (int i19 = 24; i19 < 36; i19++) {
                char charAt7 = charAt(length2 + i19);
                if (charAt7 >= '0' && charAt7 <= '9') {
                    i4 = charAt7 - '0';
                } else if (charAt7 >= 'a' && charAt7 <= 'f') {
                    i4 = (charAt7 - 'a') + 10;
                } else {
                    if (charAt7 < 'A' || charAt7 > 'F') {
                        this.matchStat = -2;
                        return null;
                    }
                    i4 = (charAt7 - 'A') + 10;
                }
                j5 = (j5 << 4) | i4;
            }
            uuid = new UUID(j4, j5);
            int length3 = i9 + (i16 - ((this.bp + cArr.length) + 1)) + 1;
            i = length3 + 1;
            charAt = charAt(this.bp + length3);
        } else {
            if (i13 != 32) {
                this.matchStat = -1;
                return null;
            }
            int i20 = 0;
            long j6 = 0;
            for (int i21 = 16; i20 < i21; i21 = 16) {
                char charAt8 = charAt(length2 + i20);
                if (charAt8 >= '0' && charAt8 <= '9') {
                    i3 = charAt8 - '0';
                } else if (charAt8 >= 'a' && charAt8 <= 'f') {
                    i3 = (charAt8 - 'a') + 10;
                } else {
                    if (charAt8 < 'A' || charAt8 > 'F') {
                        this.matchStat = -2;
                        return null;
                    }
                    i3 = (charAt8 - 'A') + 10;
                }
                j6 = (j6 << 4) | i3;
                i20++;
            }
            int i22 = 16;
            long j7 = 0;
            while (i22 < 32) {
                char charAt9 = charAt(length2 + i22);
                if (charAt9 < c6 || charAt9 > '9') {
                    if (charAt9 >= c5 && charAt9 <= 'f') {
                        i2 = (charAt9 - 'a') + 10;
                    }
                    if (charAt9 < 'A' || charAt9 > 'F') {
                        this.matchStat = -2;
                        return null;
                    }
                    i2 = (charAt9 - 'A') + 10;
                } else {
                    i2 = charAt9 - '0';
                }
                j7 = (j7 << 4) | i2;
                i22++;
                c6 = '0';
                c5 = 'a';
            }
            uuid = new UUID(j6, j7);
            int length4 = i9 + (indexOf - ((this.bp + cArr.length) + 1)) + 1;
            i = length4 + 1;
            charAt = charAt(this.bp + length4);
        }
        if (charAt == ',') {
            this.bp += i;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            return uuid;
        }
        if (charAt != '}') {
            this.matchStat = -1;
            return null;
        }
        int i23 = i + 1;
        char charAt10 = charAt(this.bp + i);
        if (charAt10 == ',') {
            this.token = 16;
            this.bp += i23;
            this.ch = charAt(this.bp);
        } else if (charAt10 == ']') {
            this.token = 15;
            this.bp += i23;
            this.ch = charAt(this.bp);
        } else if (charAt10 == '}') {
            this.token = 13;
            this.bp += i23;
            this.ch = charAt(this.bp);
        } else {
            if (charAt10 != 26) {
                this.matchStat = -1;
                return null;
            }
            this.token = 20;
            this.bp += i23 - 1;
            this.ch = JSONLexer.EOI;
        }
        this.matchStat = 4;
        return uuid;
    }

    /* JADX WARN: Removed duplicated region for block: B:50:0x00ca A[ADDED_TO_REGION] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:48:0x00cc -> B:45:0x00b8). Please report as a decompilation issue!!! */
    @Override // com.alibaba.fastjson.parser.JSONLexer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final float scanFloat(char r23) {
        /*
            Method dump skipped, instructions count: 447
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanFloat(char):float");
    }

    public final void scanHex() {
        char next;
        if (this.ch != 'x') {
            throw new JSONException("illegal state. " + this.ch);
        }
        next();
        if (this.ch != '\'') {
            throw new JSONException("illegal state. " + this.ch);
        }
        this.np = this.bp;
        next();
        while (true) {
            next = next();
            if ((next < '0' || next > '9') && (next < 'A' || next > 'F')) {
                break;
            } else {
                this.sp++;
            }
        }
        if (next == '\'') {
            this.sp++;
            next();
            this.token = 26;
        } else {
            throw new JSONException("illegal state. " + next);
        }
    }

    public final void scanIdent() {
        this.np = this.bp - 1;
        this.hasSpecial = false;
        do {
            this.sp++;
            next();
        } while (Character.isLetterOrDigit(this.ch));
        String stringVal = stringVal();
        if ("null".equalsIgnoreCase(stringVal)) {
            this.token = 8;
            return;
        }
        if (AmapLoc.TYPE_NEW.equals(stringVal)) {
            this.token = 9;
            return;
        }
        if ("true".equals(stringVal)) {
            this.token = 6;
            return;
        }
        if (Bugly.SDK_IS_DEV.equals(stringVal)) {
            this.token = 7;
            return;
        }
        if ("undefined".equals(stringVal)) {
            this.token = 23;
            return;
        }
        if ("Set".equals(stringVal)) {
            this.token = 21;
        } else if ("TreeSet".equals(stringVal)) {
            this.token = 22;
        } else {
            this.token = 18;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:61:0x00d3  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x00e5  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:66:0x00ce -> B:53:0x00cf). Please report as a decompilation issue!!! */
    @Override // com.alibaba.fastjson.parser.JSONLexer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int scanInt(char r14) {
        /*
            Method dump skipped, instructions count: 275
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanInt(char):int");
    }

    /* JADX WARN: Removed duplicated region for block: B:72:0x0110  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0122  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:77:0x010b -> B:64:0x010c). Please report as a decompilation issue!!! */
    @Override // com.alibaba.fastjson.parser.JSONLexer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public long scanLong(char r21) {
        /*
            Method dump skipped, instructions count: 336
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanLong(char):long");
    }

    public final void scanNullOrNew() {
        if (this.ch != 'n') {
            throw new JSONException("error parse null or new");
        }
        next();
        if (this.ch != 'u') {
            if (this.ch != 'e') {
                throw new JSONException("error parse new");
            }
            next();
            if (this.ch != 'w') {
                throw new JSONException("error parse new");
            }
            next();
            if (this.ch != ' ' && this.ch != ',' && this.ch != '}' && this.ch != ']' && this.ch != '\n' && this.ch != '\r' && this.ch != '\t' && this.ch != 26 && this.ch != '\f' && this.ch != '\b') {
                throw new JSONException("scan new error");
            }
            this.token = 9;
            return;
        }
        next();
        if (this.ch != 'l') {
            throw new JSONException("error parse null");
        }
        next();
        if (this.ch != 'l') {
            throw new JSONException("error parse null");
        }
        next();
        if (this.ch != ' ' && this.ch != ',' && this.ch != '}' && this.ch != ']' && this.ch != '\n' && this.ch != '\r' && this.ch != '\t' && this.ch != 26 && this.ch != '\f' && this.ch != '\b') {
            throw new JSONException("scan null error");
        }
        this.token = 8;
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x00e0  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00e4  */
    @Override // com.alibaba.fastjson.parser.JSONLexer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void scanNumber() {
        /*
            Method dump skipped, instructions count: 232
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanNumber():void");
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public String scanString(char c) {
        this.matchStat = 0;
        char charAt = charAt(this.bp + 0);
        if (charAt == 'n') {
            if (charAt(this.bp + 1) != 'u' || charAt(this.bp + 1 + 1) != 'l' || charAt(this.bp + 1 + 2) != 'l') {
                this.matchStat = -1;
                return null;
            }
            if (charAt(this.bp + 4) != c) {
                this.matchStat = -1;
                return null;
            }
            this.bp += 5;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            return null;
        }
        int i = 1;
        while (charAt != '\"') {
            if (!isWhitespace(charAt)) {
                this.matchStat = -1;
                return stringDefaultValue();
            }
            charAt = charAt(this.bp + i);
            i++;
        }
        int i2 = this.bp + i;
        int indexOf = indexOf(Typography.quote, i2);
        if (indexOf == -1) {
            throw new JSONException("unclosed str");
        }
        String subString = subString(this.bp + i, indexOf - i2);
        if (subString.indexOf(92) != -1) {
            while (true) {
                int i3 = 0;
                for (int i4 = indexOf - 1; i4 >= 0 && charAt(i4) == '\\'; i4--) {
                    i3++;
                }
                if (i3 % 2 == 0) {
                    break;
                }
                indexOf = indexOf(Typography.quote, indexOf + 1);
            }
            int i5 = indexOf - i2;
            subString = readString(sub_chars(this.bp + 1, i5), i5);
        }
        int i6 = i + (indexOf - i2) + 1;
        int i7 = i6 + 1;
        char charAt2 = charAt(this.bp + i6);
        while (charAt2 != c) {
            if (!isWhitespace(charAt2)) {
                this.matchStat = -1;
                return subString;
            }
            charAt2 = charAt(this.bp + i7);
            i7++;
        }
        this.bp += i7;
        this.ch = charAt(this.bp);
        this.matchStat = 3;
        return subString;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final void scanString() {
        this.np = this.bp;
        this.hasSpecial = false;
        while (true) {
            char next = next();
            if (next == '\"') {
                this.token = 4;
                this.ch = next();
                return;
            }
            if (next == 26) {
                if (isEOF()) {
                    throw new JSONException("unclosed string : " + next);
                }
                putChar(JSONLexer.EOI);
            } else if (next == '\\') {
                if (!this.hasSpecial) {
                    this.hasSpecial = true;
                    if (this.sp >= this.sbuf.length) {
                        int length = this.sbuf.length * 2;
                        if (this.sp > length) {
                            length = this.sp;
                        }
                        char[] cArr = new char[length];
                        System.arraycopy(this.sbuf, 0, cArr, 0, this.sbuf.length);
                        this.sbuf = cArr;
                    }
                    copyTo(this.np + 1, this.sp, this.sbuf);
                }
                char next2 = next();
                switch (next2) {
                    case '/':
                        putChar('/');
                        break;
                    case '0':
                        putChar((char) 0);
                        break;
                    case '1':
                        putChar((char) 1);
                        break;
                    case '2':
                        putChar((char) 2);
                        break;
                    case '3':
                        putChar((char) 3);
                        break;
                    case '4':
                        putChar((char) 4);
                        break;
                    case '5':
                        putChar((char) 5);
                        break;
                    case '6':
                        putChar((char) 6);
                        break;
                    case '7':
                        putChar((char) 7);
                        break;
                    default:
                        switch (next2) {
                            case 't':
                                putChar('\t');
                                break;
                            case 'u':
                                putChar((char) Integer.parseInt(new String(new char[]{next(), next(), next(), next()}), 16));
                                break;
                            case 'v':
                                putChar((char) 11);
                                break;
                            default:
                                switch (next2) {
                                    case '\"':
                                        putChar(Typography.quote);
                                        break;
                                    case '\'':
                                        putChar('\'');
                                        break;
                                    case 'F':
                                    case 'f':
                                        putChar('\f');
                                        break;
                                    case '\\':
                                        putChar('\\');
                                        break;
                                    case 'b':
                                        putChar('\b');
                                        break;
                                    case 'n':
                                        putChar('\n');
                                        break;
                                    case 'r':
                                        putChar(CharUtils.CR);
                                        break;
                                    case GlMapUtil.DEVICE_DISPLAY_DPI_LOW /* 120 */:
                                        putChar((char) ((digits[next()] * 16) + digits[next()]));
                                        break;
                                    default:
                                        this.ch = next2;
                                        throw new JSONException("unclosed string : " + next2);
                                }
                        }
                }
            } else if (!this.hasSpecial) {
                this.sp++;
            } else if (this.sp == this.sbuf.length) {
                putChar(next);
            } else {
                char[] cArr2 = this.sbuf;
                int i = this.sp;
                this.sp = i + 1;
                cArr2[i] = next;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x012e, code lost:
    
        if (r1 != r18) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0130, code lost:
    
        r16.bp += r3;
        r16.ch = charAt(r16.bp);
        r16.matchStat = 3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x013f, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0140, code lost:
    
        r16.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0142, code lost:
    
        return;
     */
    @Override // com.alibaba.fastjson.parser.JSONLexer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void scanStringArray(java.util.Collection<java.lang.String> r17, char r18) {
        /*
            Method dump skipped, instructions count: 334
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanStringArray(java.util.Collection, char):void");
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final String scanSymbol(SymbolTable symbolTable) {
        skipWhitespace();
        if (this.ch == '\"') {
            return scanSymbol(symbolTable, Typography.quote);
        }
        if (this.ch == '\'') {
            if (isEnabled(Feature.AllowSingleQuotes)) {
                return scanSymbol(symbolTable, '\'');
            }
            throw new JSONException("syntax error");
        }
        if (this.ch == '}') {
            next();
            this.token = 13;
            return null;
        }
        if (this.ch == ',') {
            next();
            this.token = 16;
            return null;
        }
        if (this.ch == 26) {
            this.token = 20;
            return null;
        }
        if (isEnabled(Feature.AllowUnQuotedFieldNames)) {
            return scanSymbolUnQuoted(symbolTable);
        }
        throw new JSONException("syntax error");
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final String scanSymbol(SymbolTable symbolTable, char c) {
        String addSymbol;
        this.np = this.bp;
        this.sp = 0;
        boolean z = false;
        int i = 0;
        while (true) {
            char next = next();
            if (next == c) {
                this.token = 4;
                if (z) {
                    addSymbol = symbolTable.addSymbol(this.sbuf, 0, this.sp, i);
                } else {
                    addSymbol = addSymbol(this.np == -1 ? 0 : this.np + 1, this.sp, i, symbolTable);
                }
                this.sp = 0;
                next();
                return addSymbol;
            }
            if (next == 26) {
                throw new JSONException("unclosed.str");
            }
            if (next == '\\') {
                if (!z) {
                    if (this.sp >= this.sbuf.length) {
                        int length = this.sbuf.length * 2;
                        if (this.sp > length) {
                            length = this.sp;
                        }
                        char[] cArr = new char[length];
                        System.arraycopy(this.sbuf, 0, cArr, 0, this.sbuf.length);
                        this.sbuf = cArr;
                    }
                    arrayCopy(this.np + 1, this.sbuf, 0, this.sp);
                    z = true;
                }
                char next2 = next();
                switch (next2) {
                    case '/':
                        i = (i * 31) + 47;
                        putChar('/');
                        break;
                    case '0':
                        i = (i * 31) + next2;
                        putChar((char) 0);
                        break;
                    case '1':
                        i = (i * 31) + next2;
                        putChar((char) 1);
                        break;
                    case '2':
                        i = (i * 31) + next2;
                        putChar((char) 2);
                        break;
                    case '3':
                        i = (i * 31) + next2;
                        putChar((char) 3);
                        break;
                    case '4':
                        i = (i * 31) + next2;
                        putChar((char) 4);
                        break;
                    case '5':
                        i = (i * 31) + next2;
                        putChar((char) 5);
                        break;
                    case '6':
                        i = (i * 31) + next2;
                        putChar((char) 6);
                        break;
                    case '7':
                        i = (i * 31) + next2;
                        putChar((char) 7);
                        break;
                    default:
                        switch (next2) {
                            case 't':
                                i = (i * 31) + 9;
                                putChar('\t');
                                break;
                            case 'u':
                                int parseInt = Integer.parseInt(new String(new char[]{next(), next(), next(), next()}), 16);
                                i = (i * 31) + parseInt;
                                putChar((char) parseInt);
                                break;
                            case 'v':
                                i = (i * 31) + 11;
                                putChar((char) 11);
                                break;
                            default:
                                switch (next2) {
                                    case '\"':
                                        i = (i * 31) + 34;
                                        putChar(Typography.quote);
                                        break;
                                    case '\'':
                                        i = (i * 31) + 39;
                                        putChar('\'');
                                        break;
                                    case 'F':
                                    case 'f':
                                        i = (i * 31) + 12;
                                        putChar('\f');
                                        break;
                                    case '\\':
                                        i = (i * 31) + 92;
                                        putChar('\\');
                                        break;
                                    case 'b':
                                        i = (i * 31) + 8;
                                        putChar('\b');
                                        break;
                                    case 'n':
                                        i = (i * 31) + 10;
                                        putChar('\n');
                                        break;
                                    case 'r':
                                        i = (i * 31) + 13;
                                        putChar(CharUtils.CR);
                                        break;
                                    case GlMapUtil.DEVICE_DISPLAY_DPI_LOW /* 120 */:
                                        char next3 = next();
                                        this.ch = next3;
                                        char next4 = next();
                                        this.ch = next4;
                                        char c2 = (char) ((digits[next3] * 16) + digits[next4]);
                                        i = (i * 31) + c2;
                                        putChar(c2);
                                        break;
                                    default:
                                        this.ch = next2;
                                        throw new JSONException("unclosed.str.lit");
                                }
                        }
                }
            } else {
                i = (i * 31) + next;
                if (!z) {
                    this.sp++;
                } else if (this.sp == this.sbuf.length) {
                    putChar(next);
                } else {
                    char[] cArr2 = this.sbuf;
                    int i2 = this.sp;
                    this.sp = i2 + 1;
                    cArr2[i2] = next;
                }
            }
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final String scanSymbolUnQuoted(SymbolTable symbolTable) {
        if (this.token == 1 && this.pos == 0 && this.bp == 1) {
            this.bp = 0;
        }
        boolean[] zArr = IOUtils.firstIdentifierFlags;
        int i = this.ch;
        if (!(this.ch >= zArr.length || zArr[i])) {
            throw new JSONException("illegal identifier : " + this.ch + info());
        }
        boolean[] zArr2 = IOUtils.identifierFlags;
        this.np = this.bp;
        this.sp = 1;
        while (true) {
            char next = next();
            if (next < zArr2.length && !zArr2[next]) {
                break;
            }
            i = (i * 31) + next;
            this.sp++;
        }
        this.ch = charAt(this.bp);
        this.token = 18;
        if (this.sp == 4 && i == 3392903 && charAt(this.np) == 'n' && charAt(this.np + 1) == 'u' && charAt(this.np + 2) == 'l' && charAt(this.np + 3) == 'l') {
            return null;
        }
        return symbolTable == null ? subString(this.np, this.sp) : addSymbol(this.np, this.sp, i, symbolTable);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public String scanSymbolWithSeperator(SymbolTable symbolTable, char c) {
        this.matchStat = 0;
        char charAt = charAt(this.bp + 0);
        if (charAt == 'n') {
            if (charAt(this.bp + 1) != 'u' || charAt(this.bp + 1 + 1) != 'l' || charAt(this.bp + 1 + 2) != 'l') {
                this.matchStat = -1;
                return null;
            }
            if (charAt(this.bp + 4) != c) {
                this.matchStat = -1;
                return null;
            }
            this.bp += 5;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            return null;
        }
        if (charAt != '\"') {
            this.matchStat = -1;
            return null;
        }
        int i = 1;
        int i2 = 0;
        while (true) {
            int i3 = i + 1;
            char charAt2 = charAt(this.bp + i);
            if (charAt2 == '\"') {
                int i4 = this.bp + 0 + 1;
                String addSymbol = addSymbol(i4, ((this.bp + i3) - i4) - 1, i2, symbolTable);
                int i5 = i3 + 1;
                char charAt3 = charAt(this.bp + i3);
                while (charAt3 != c) {
                    if (!isWhitespace(charAt3)) {
                        this.matchStat = -1;
                        return addSymbol;
                    }
                    charAt3 = charAt(this.bp + i5);
                    i5++;
                }
                this.bp += i5;
                this.ch = charAt(this.bp);
                this.matchStat = 3;
                return addSymbol;
            }
            i2 = (i2 * 31) + charAt2;
            if (charAt2 == '\\') {
                this.matchStat = -1;
                return null;
            }
            i = i3;
        }
    }

    public final void scanTrue() {
        if (this.ch != 't') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'r') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'u') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != ' ' && this.ch != ',' && this.ch != '}' && this.ch != ']' && this.ch != '\n' && this.ch != '\r' && this.ch != '\t' && this.ch != 26 && this.ch != '\f' && this.ch != '\b' && this.ch != ':' && this.ch != '/') {
            throw new JSONException("scan true error");
        }
        this.token = 6;
    }

    public final int scanType(String str) {
        this.matchStat = 0;
        if (!charArrayCompare(typeFieldName)) {
            return -2;
        }
        int length = this.bp + typeFieldName.length;
        int length2 = str.length();
        for (int i = 0; i < length2; i++) {
            if (str.charAt(i) != charAt(length + i)) {
                return -1;
            }
        }
        int i2 = length + length2;
        if (charAt(i2) != '\"') {
            return -1;
        }
        int i3 = i2 + 1;
        this.ch = charAt(i3);
        if (this.ch == ',') {
            int i4 = i3 + 1;
            this.ch = charAt(i4);
            this.bp = i4;
            this.token = 16;
            return 3;
        }
        if (this.ch == '}') {
            i3++;
            this.ch = charAt(i3);
            if (this.ch == ',') {
                this.token = 16;
                i3++;
                this.ch = charAt(i3);
            } else if (this.ch == ']') {
                this.token = 15;
                i3++;
                this.ch = charAt(i3);
            } else if (this.ch == '}') {
                this.token = 13;
                i3++;
                this.ch = charAt(i3);
            } else {
                if (this.ch != 26) {
                    return -1;
                }
                this.token = 20;
            }
            this.matchStat = 4;
        }
        this.bp = i3;
        return this.matchStat;
    }

    public UUID scanUUID(char c) {
        int i;
        char charAt;
        UUID uuid;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        this.matchStat = 0;
        char charAt2 = charAt(this.bp + 0);
        char c2 = 4;
        if (charAt2 == '\"') {
            int indexOf = indexOf(Typography.quote, this.bp + 1);
            if (indexOf == -1) {
                throw new JSONException("unclosed str");
            }
            int i9 = this.bp + 1;
            int i10 = indexOf - i9;
            char c3 = 'F';
            char c4 = 'f';
            char c5 = '9';
            char c6 = 'A';
            char c7 = 'a';
            char c8 = '0';
            if (i10 == 36) {
                int i11 = 0;
                long j = 0;
                while (i11 < 8) {
                    char charAt3 = charAt(i9 + i11);
                    if (charAt3 >= '0' && charAt3 <= '9') {
                        i8 = charAt3 - '0';
                    } else if (charAt3 >= 'a' && charAt3 <= c4) {
                        i8 = (charAt3 - 'a') + 10;
                    } else {
                        if (charAt3 < 'A' || charAt3 > c3) {
                            this.matchStat = -2;
                            return null;
                        }
                        i8 = (charAt3 - 'A') + 10;
                    }
                    j = (j << 4) | i8;
                    i11++;
                    c3 = 'F';
                    c4 = 'f';
                }
                int i12 = 9;
                while (i12 < 13) {
                    char charAt4 = charAt(i9 + i12);
                    if (charAt4 >= '0' && charAt4 <= '9') {
                        i7 = charAt4 - '0';
                    } else if (charAt4 >= c7 && charAt4 <= 'f') {
                        i7 = (charAt4 - 'a') + 10;
                    } else {
                        if (charAt4 < c6 || charAt4 > 'F') {
                            this.matchStat = -2;
                            return null;
                        }
                        i7 = (charAt4 - 'A') + 10;
                    }
                    j = (j << 4) | i7;
                    i12++;
                    c6 = 'A';
                    c7 = 'a';
                }
                long j2 = j;
                for (int i13 = 14; i13 < 18; i13++) {
                    char charAt5 = charAt(i9 + i13);
                    if (charAt5 >= '0' && charAt5 <= '9') {
                        i6 = charAt5 - '0';
                    } else if (charAt5 >= 'a' && charAt5 <= 'f') {
                        i6 = (charAt5 - 'a') + 10;
                    } else {
                        if (charAt5 < 'A' || charAt5 > 'F') {
                            this.matchStat = -2;
                            return null;
                        }
                        i6 = (charAt5 - 'A') + 10;
                    }
                    j2 = (j2 << 4) | i6;
                }
                int i14 = 19;
                long j3 = 0;
                while (i14 < 23) {
                    char charAt6 = charAt(i9 + i14);
                    if (charAt6 >= '0' && charAt6 <= c5) {
                        i5 = charAt6 - '0';
                    } else if (charAt6 >= 'a' && charAt6 <= 'f') {
                        i5 = (charAt6 - 'a') + 10;
                    } else {
                        if (charAt6 < 'A' || charAt6 > 'F') {
                            this.matchStat = -2;
                            return null;
                        }
                        i5 = (charAt6 - 'A') + 10;
                    }
                    j3 = (j3 << 4) | i5;
                    i14++;
                    indexOf = indexOf;
                    c5 = '9';
                }
                int i15 = indexOf;
                int i16 = 24;
                long j4 = j3;
                while (i16 < 36) {
                    char charAt7 = charAt(i9 + i16);
                    if (charAt7 >= c8 && charAt7 <= '9') {
                        i4 = charAt7 - '0';
                    } else if (charAt7 >= 'a' && charAt7 <= 'f') {
                        i4 = (charAt7 - 'a') + 10;
                    } else {
                        if (charAt7 < 'A' || charAt7 > 'F') {
                            this.matchStat = -2;
                            return null;
                        }
                        i4 = (charAt7 - 'A') + 10;
                    }
                    j4 = (j4 << c2) | i4;
                    i16++;
                    c8 = '0';
                    c2 = 4;
                }
                uuid = new UUID(j2, j4);
                int i17 = (i15 - (this.bp + 1)) + 1 + 1;
                i = i17 + 1;
                charAt = charAt(this.bp + i17);
            } else {
                if (i10 != 32) {
                    this.matchStat = -1;
                    return null;
                }
                long j5 = 0;
                for (int i18 = 0; i18 < 16; i18++) {
                    char charAt8 = charAt(i9 + i18);
                    if (charAt8 >= '0' && charAt8 <= '9') {
                        i3 = charAt8 - '0';
                    } else if (charAt8 >= 'a' && charAt8 <= 'f') {
                        i3 = (charAt8 - 'a') + 10;
                    } else {
                        if (charAt8 < 'A' || charAt8 > 'F') {
                            this.matchStat = -2;
                            return null;
                        }
                        i3 = (charAt8 - 'A') + 10;
                    }
                    j5 = (j5 << 4) | i3;
                }
                long j6 = 0;
                for (int i19 = 16; i19 < 32; i19++) {
                    char charAt9 = charAt(i9 + i19);
                    if (charAt9 >= '0' && charAt9 <= '9') {
                        i2 = charAt9 - '0';
                        j6 = (j6 << 4) | i2;
                    }
                    if (charAt9 >= 'a' && charAt9 <= 'f') {
                        i2 = (charAt9 - 'a') + 10;
                        j6 = (j6 << 4) | i2;
                    }
                    if (charAt9 < 'A' || charAt9 > 'F') {
                        this.matchStat = -2;
                        return null;
                    }
                    i2 = (charAt9 - 'A') + 10;
                    j6 = (j6 << 4) | i2;
                }
                uuid = new UUID(j5, j6);
                int i20 = (indexOf - (this.bp + 1)) + 1 + 1;
                i = i20 + 1;
                charAt = charAt(this.bp + i20);
            }
        } else {
            if (charAt2 != 'n' || charAt(this.bp + 1) != 'u' || charAt(this.bp + 2) != 'l' || charAt(this.bp + 3) != 'l') {
                this.matchStat = -1;
                return null;
            }
            i = 5;
            charAt = charAt(this.bp + 4);
            uuid = null;
        }
        if (charAt == ',') {
            this.bp += i;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            return uuid;
        }
        if (charAt != ']') {
            this.matchStat = -1;
            return null;
        }
        int i21 = i + 1;
        char charAt10 = charAt(this.bp + i);
        if (charAt10 == ',') {
            this.token = 16;
            this.bp += i21;
            this.ch = charAt(this.bp);
        } else if (charAt10 == ']') {
            this.token = 15;
            this.bp += i21;
            this.ch = charAt(this.bp);
        } else if (charAt10 == '}') {
            this.token = 13;
            this.bp += i21;
            this.ch = charAt(this.bp);
        } else {
            if (charAt10 != 26) {
                this.matchStat = -1;
                return null;
            }
            this.token = 20;
            this.bp += i21 - 1;
            this.ch = JSONLexer.EOI;
        }
        this.matchStat = 4;
        return uuid;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public void setToken(int i) {
        this.token = i;
    }

    protected void skipComment() {
        next();
        if (this.ch != '/') {
            if (this.ch != '*') {
                throw new JSONException("invalid comment");
            }
            next();
            while (this.ch != 26) {
                if (this.ch == '*') {
                    next();
                    if (this.ch == '/') {
                        next();
                        return;
                    }
                } else {
                    next();
                }
            }
            return;
        }
        do {
            next();
            if (this.ch == '\n') {
                next();
                return;
            }
        } while (this.ch != 26);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final void skipWhitespace() {
        while (this.ch <= '/') {
            if (this.ch == ' ' || this.ch == '\r' || this.ch == '\n' || this.ch == '\t' || this.ch == '\f' || this.ch == '\b') {
                next();
            } else if (this.ch != '/') {
                return;
            } else {
                skipComment();
            }
        }
    }

    public final String stringDefaultValue() {
        return this.stringDefaultValue;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public abstract String stringVal();

    public abstract String subString(int i, int i2);

    protected abstract char[] sub_chars(int i, int i2);

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final int token() {
        return this.token;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final String tokenName() {
        return JSONToken.name(this.token);
    }
}
