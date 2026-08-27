package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.IOUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.TimeZone;
import kotlin.text.Typography;

/* loaded from: classes.dex */
public final class JSONScanner extends JSONLexerBase {
    private final int len;
    private final String text;

    public JSONScanner(String str) {
        this(str, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONScanner(String str, int i) {
        super(i);
        this.text = str;
        this.len = this.text.length();
        this.bp = -1;
        next();
        if (this.ch == 65279) {
            next();
        }
    }

    public JSONScanner(char[] cArr, int i) {
        this(cArr, i, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONScanner(char[] cArr, int i, int i2) {
        this(new String(cArr, 0, i), i2);
    }

    static boolean charArrayCompare(String str, int i, char[] cArr) {
        int length = cArr.length;
        if (length + i > str.length()) {
            return false;
        }
        for (int i2 = 0; i2 < length; i2++) {
            if (cArr[i2] != str.charAt(i + i2)) {
                return false;
            }
        }
        return true;
    }

    static boolean checkDate(char c, char c2, char c3, char c4, char c5, char c6, int i, int i2) {
        if (c < '1' || c > '3' || c2 < '0' || c2 > '9' || c3 < '0' || c3 > '9' || c4 < '0' || c4 > '9') {
            return false;
        }
        if (c5 == '0') {
            if (c6 < '1' || c6 > '9') {
                return false;
            }
        } else {
            if (c5 != '1') {
                return false;
            }
            if (c6 != '0' && c6 != '1' && c6 != '2') {
                return false;
            }
        }
        if (i == 48) {
            return i2 >= 49 && i2 <= 57;
        }
        if (i == 49 || i == 50) {
            return i2 >= 48 && i2 <= 57;
        }
        if (i == 51) {
            return i2 == 48 || i2 == 49;
        }
        return false;
    }

    private boolean checkTime(char c, char c2, char c3, char c4, char c5, char c6) {
        if (c == '0') {
            if (c2 < '0' || c2 > '9') {
                return false;
            }
        } else if (c == '1') {
            if (c2 < '0' || c2 > '9') {
                return false;
            }
        } else if (c != '2' || c2 < '0' || c2 > '4') {
            return false;
        }
        if (c3 < '0' || c3 > '5') {
            if (c3 != '6' || c4 != '0') {
                return false;
            }
        } else if (c4 < '0' || c4 > '9') {
            return false;
        }
        return (c5 < '0' || c5 > '5') ? c5 == '6' && c6 == '0' : c6 >= '0' && c6 <= '9';
    }

    /* JADX WARN: Removed duplicated region for block: B:65:0x01f1 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:66:0x01f3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean scanISO8601DateIfMatch(boolean r35, int r36) {
        /*
            Method dump skipped, instructions count: 1596
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONScanner.scanISO8601DateIfMatch(boolean, int):boolean");
    }

    private void setCalendar(char c, char c2, char c3, char c4, char c5, char c6, char c7, char c8) {
        this.calendar = Calendar.getInstance(this.timeZone, this.locale);
        this.calendar.set(1, ((c - '0') * 1000) + ((c2 - '0') * 100) + ((c3 - '0') * 10) + (c4 - '0'));
        this.calendar.set(2, (((c5 - '0') * 10) + (c6 - '0')) - 1);
        this.calendar.set(5, ((c7 - '0') * 10) + (c8 - '0'));
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final String addSymbol(int i, int i2, int i3, SymbolTable symbolTable) {
        return symbolTable.addSymbol(this.text, i, i2, i3);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    protected final void arrayCopy(int i, char[] cArr, int i2, int i3) {
        this.text.getChars(i, i3 + i, cArr, i2);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public byte[] bytesValue() {
        if (this.token != 26) {
            return IOUtils.decodeBase64(this.text, this.np + 1, this.sp);
        }
        int i = this.np + 1;
        int i2 = this.sp;
        if (i2 % 2 != 0) {
            throw new JSONException("illegal state. " + i2);
        }
        byte[] bArr = new byte[i2 / 2];
        for (int i3 = 0; i3 < bArr.length; i3++) {
            int i4 = (i3 * 2) + i;
            char charAt = this.text.charAt(i4);
            char charAt2 = this.text.charAt(i4 + 1);
            char c = '7';
            int i5 = charAt - (charAt <= '9' ? '0' : '7');
            if (charAt2 <= '9') {
                c = '0';
            }
            bArr[i3] = (byte) ((i5 << 4) | (charAt2 - c));
        }
        return bArr;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final boolean charArrayCompare(char[] cArr) {
        return charArrayCompare(this.text, this.bp, cArr);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final char charAt(int i) {
        return i >= this.len ? JSONLexer.EOI : this.text.charAt(i);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    protected final void copyTo(int i, int i2, char[] cArr) {
        this.text.getChars(i, i2 + i, cArr, 0);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public final BigDecimal decimalValue() {
        char charAt = charAt((this.np + this.sp) - 1);
        int i = this.sp;
        if (charAt == 'L' || charAt == 'S' || charAt == 'B' || charAt == 'F' || charAt == 'D') {
            i--;
        }
        int i2 = this.np;
        if (i < this.sbuf.length) {
            this.text.getChars(i2, i2 + i, this.sbuf, 0);
            return new BigDecimal(this.sbuf, 0, i);
        }
        char[] cArr = new char[i];
        this.text.getChars(i2, i + i2, cArr, 0);
        return new BigDecimal(cArr);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final int indexOf(char c, int i) {
        return this.text.indexOf(c, i);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public String info() {
        StringBuilder sb = new StringBuilder();
        sb.append("pos ");
        sb.append(this.bp);
        sb.append(", json : ");
        sb.append(this.text.length() < 65536 ? this.text : this.text.substring(0, 65536));
        return sb.toString();
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public boolean isEOF() {
        if (this.bp != this.len) {
            return this.ch == 26 && this.bp + 1 == this.len;
        }
        return true;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public boolean matchField2(char[] cArr) {
        while (isWhitespace(this.ch)) {
            next();
        }
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return false;
        }
        int length = this.bp + cArr.length;
        int i = length + 1;
        char charAt = this.text.charAt(length);
        while (isWhitespace(charAt)) {
            charAt = this.text.charAt(i);
            i++;
        }
        if (charAt != ':') {
            this.matchStat = -2;
            return false;
        }
        this.bp = i;
        this.ch = charAt(this.bp);
        return true;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
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

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public final char next() {
        int i = this.bp + 1;
        this.bp = i;
        char charAt = i >= this.len ? JSONLexer.EOI : this.text.charAt(i);
        this.ch = charAt;
        return charAt;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public final String numberString() {
        char charAt = charAt((this.np + this.sp) - 1);
        int i = this.sp;
        if (charAt == 'L' || charAt == 'S' || charAt == 'B' || charAt == 'F' || charAt == 'D') {
            i--;
        }
        return subString(this.np, i);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public Date scanDate(char c) {
        int i;
        boolean z;
        char c2;
        long j;
        char c3;
        Date date;
        int i2;
        this.matchStat = 0;
        int i3 = this.bp;
        char c4 = this.ch;
        int i4 = this.bp;
        int i5 = i4 + 1;
        char charAt = charAt(i4);
        if (charAt == '\"') {
            int indexOf = indexOf(Typography.quote, i5);
            if (indexOf == -1) {
                throw new JSONException("unclosed str");
            }
            this.bp = i5;
            if (!scanISO8601DateIfMatch(false, indexOf - i5)) {
                this.bp = i3;
                this.ch = c4;
                this.matchStat = -1;
                return null;
            }
            date = this.calendar.getTime();
            c3 = charAt(indexOf + 1);
            this.bp = i3;
            while (c3 != ',' && c3 != ']') {
                if (!isWhitespace(c3)) {
                    this.bp = i3;
                    this.ch = c4;
                    this.matchStat = -1;
                    return null;
                }
                indexOf++;
                c3 = charAt(indexOf + 1);
            }
            this.bp = indexOf + 1;
            this.ch = c3;
        } else {
            char c5 = '9';
            char c6 = '0';
            if (charAt != '-' && (charAt < '0' || charAt > '9')) {
                if (charAt == 'n') {
                    int i6 = i5 + 1;
                    if (charAt(i5) == 'u') {
                        int i7 = i6 + 1;
                        if (charAt(i6) == 'l') {
                            int i8 = i7 + 1;
                            if (charAt(i7) == 'l') {
                                c3 = charAt(i8);
                                this.bp = i8;
                                date = null;
                            }
                        }
                    }
                }
                this.bp = i3;
                this.ch = c4;
                this.matchStat = -1;
                return null;
            }
            if (charAt == '-') {
                i = i5 + 1;
                charAt = charAt(i5);
                z = true;
            } else {
                i = i5;
                z = false;
            }
            if (charAt < '0' || charAt > '9') {
                c2 = charAt;
                j = 0;
            } else {
                j = charAt - '0';
                while (true) {
                    i2 = i + 1;
                    c2 = charAt(i);
                    if (c2 < c6 || c2 > c5) {
                        break;
                    }
                    j = (j * 10) + (c2 - '0');
                    i = i2;
                    c5 = '9';
                    c6 = '0';
                }
                if (c2 == ',' || c2 == ']') {
                    this.bp = i2 - 1;
                }
            }
            if (j < 0) {
                this.bp = i3;
                this.ch = c4;
                this.matchStat = -1;
                return null;
            }
            if (z) {
                j = -j;
            }
            c3 = c2;
            date = new Date(j);
        }
        if (c3 == ',') {
            int i9 = this.bp + 1;
            this.bp = i9;
            this.ch = charAt(i9);
            this.matchStat = 3;
            return date;
        }
        int i10 = this.bp + 1;
        this.bp = i10;
        char charAt2 = charAt(i10);
        if (charAt2 == ',') {
            this.token = 16;
            int i11 = this.bp + 1;
            this.bp = i11;
            this.ch = charAt(i11);
        } else if (charAt2 == ']') {
            this.token = 15;
            int i12 = this.bp + 1;
            this.bp = i12;
            this.ch = charAt(i12);
        } else if (charAt2 == '}') {
            this.token = 13;
            int i13 = this.bp + 1;
            this.bp = i13;
            this.ch = charAt(i13);
        } else {
            if (charAt2 != 26) {
                this.bp = i3;
                this.ch = c4;
                this.matchStat = -1;
                return null;
            }
            this.ch = JSONLexer.EOI;
            this.token = 20;
        }
        this.matchStat = 4;
        return date;
    }

    /* JADX WARN: Removed duplicated region for block: B:46:0x00c0  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:46:0x00c4 -> B:42:0x00b4). Please report as a decompilation issue!!! */
    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public double scanDouble(char r22) {
        /*
            Method dump skipped, instructions count: 403
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONScanner.scanDouble(char):double");
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x010f  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x00fe A[SYNTHETIC] */
    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean scanFieldBoolean(char[] r11) {
        /*
            Method dump skipped, instructions count: 399
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONScanner.scanFieldBoolean(char[]):boolean");
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public Date scanFieldDate(char[] cArr) {
        char c;
        long j;
        Date date;
        int i;
        boolean z = false;
        this.matchStat = 0;
        int i2 = this.bp;
        char c2 = this.ch;
        if (!charArrayCompare(this.text, this.bp, cArr)) {
            this.matchStat = -2;
            return null;
        }
        int length = this.bp + cArr.length;
        int i3 = length + 1;
        char charAt = charAt(length);
        if (charAt == '\"') {
            int indexOf = indexOf(Typography.quote, i3);
            if (indexOf == -1) {
                throw new JSONException("unclosed str");
            }
            this.bp = i3;
            if (!scanISO8601DateIfMatch(false, indexOf - i3)) {
                this.bp = i2;
                this.matchStat = -1;
                return null;
            }
            Date time = this.calendar.getTime();
            char charAt2 = charAt(indexOf + 1);
            this.bp = i2;
            while (charAt2 != ',' && charAt2 != '}') {
                if (!isWhitespace(charAt2)) {
                    this.matchStat = -1;
                    return null;
                }
                indexOf++;
                charAt2 = charAt(indexOf + 1);
            }
            this.bp = indexOf + 1;
            this.ch = charAt2;
            char c3 = charAt2;
            date = time;
            c = c3;
        } else {
            char c4 = '9';
            char c5 = '0';
            if (charAt != '-' && (charAt < '0' || charAt > '9')) {
                this.matchStat = -1;
                return null;
            }
            if (charAt == '-') {
                charAt = charAt(i3);
                i3++;
                z = true;
            }
            if (charAt < '0' || charAt > '9') {
                c = charAt;
                j = 0;
            } else {
                j = charAt - '0';
                while (true) {
                    i = i3 + 1;
                    c = charAt(i3);
                    if (c < c5 || c > c4) {
                        break;
                    }
                    j = (j * 10) + (c - '0');
                    i3 = i;
                    c4 = '9';
                    c5 = '0';
                }
                if (c == ',' || c == '}') {
                    this.bp = i - 1;
                }
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
            int i4 = this.bp + 1;
            this.bp = i4;
            this.ch = charAt(i4);
            this.matchStat = 3;
            this.token = 16;
            return date;
        }
        int i5 = this.bp + 1;
        this.bp = i5;
        char charAt3 = charAt(i5);
        if (charAt3 == ',') {
            this.token = 16;
            int i6 = this.bp + 1;
            this.bp = i6;
            this.ch = charAt(i6);
        } else if (charAt3 == ']') {
            this.token = 15;
            int i7 = this.bp + 1;
            this.bp = i7;
            this.ch = charAt(i7);
        } else if (charAt3 == '}') {
            this.token = 13;
            int i8 = this.bp + 1;
            this.bp = i8;
            this.ch = charAt(i8);
        } else {
            if (charAt3 != 26) {
                this.bp = i2;
                this.ch = c2;
                this.matchStat = -1;
                return null;
            }
            this.token = 20;
        }
        this.matchStat = 4;
        return date;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public int scanFieldInt(char[] cArr) {
        int i;
        char c;
        int i2;
        char charAt;
        this.matchStat = 0;
        int i3 = this.bp;
        char c2 = this.ch;
        if (!charArrayCompare(this.text, this.bp, cArr)) {
            this.matchStat = -2;
            return 0;
        }
        int length = this.bp + cArr.length;
        int i4 = length + 1;
        char charAt2 = charAt(length);
        boolean z = charAt2 == '\"';
        if (z) {
            i = i4 + 1;
            c = charAt(i4);
        } else {
            i = i4;
            c = charAt2;
        }
        boolean z2 = c == '-';
        if (z2) {
            int i5 = i + 1;
            char charAt3 = charAt(i);
            i = i5;
            c = charAt3;
        }
        if (c < '0' || c > '9') {
            this.matchStat = -1;
            return 0;
        }
        int i6 = c - '0';
        while (true) {
            i2 = i + 1;
            charAt = charAt(i);
            if (charAt < '0' || charAt > '9') {
                break;
            }
            i6 = (i6 * 10) + (charAt - '0');
            i = i2;
        }
        if (charAt == '.') {
            this.matchStat = -1;
            return 0;
        }
        if (i6 < 0) {
            this.matchStat = -1;
            return 0;
        }
        if (z) {
            if (charAt != '\"') {
                this.matchStat = -1;
                return 0;
            }
            int i7 = i2 + 1;
            char charAt4 = charAt(i2);
            i2 = i7;
            charAt = charAt4;
        }
        while (charAt != ',' && charAt != '}') {
            if (!isWhitespace(charAt)) {
                this.matchStat = -1;
                return 0;
            }
            int i8 = i2 + 1;
            char charAt5 = charAt(i2);
            i2 = i8;
            charAt = charAt5;
        }
        int i9 = i2 - 1;
        this.bp = i9;
        if (charAt == ',') {
            int i10 = this.bp + 1;
            this.bp = i10;
            this.ch = charAt(i10);
            this.matchStat = 3;
            this.token = 16;
            return z2 ? -i6 : i6;
        }
        if (charAt == '}') {
            this.bp = i9;
            int i11 = this.bp + 1;
            this.bp = i11;
            char charAt6 = charAt(i11);
            while (true) {
                if (charAt6 == ',') {
                    this.token = 16;
                    int i12 = this.bp + 1;
                    this.bp = i12;
                    this.ch = charAt(i12);
                    break;
                }
                if (charAt6 == ']') {
                    this.token = 15;
                    int i13 = this.bp + 1;
                    this.bp = i13;
                    this.ch = charAt(i13);
                    break;
                }
                if (charAt6 == '}') {
                    this.token = 13;
                    int i14 = this.bp + 1;
                    this.bp = i14;
                    this.ch = charAt(i14);
                    break;
                }
                if (charAt6 == 26) {
                    this.token = 20;
                    break;
                }
                if (!isWhitespace(charAt6)) {
                    this.bp = i3;
                    this.ch = c2;
                    this.matchStat = -1;
                    return 0;
                }
                int i15 = this.bp + 1;
                this.bp = i15;
                charAt6 = charAt(i15);
            }
            this.matchStat = 4;
        }
        return z2 ? -i6 : i6;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public long scanFieldLong(char[] cArr) {
        int i;
        char c;
        boolean z;
        int i2;
        char charAt;
        int i3;
        char c2;
        this.matchStat = 0;
        int i4 = this.bp;
        char c3 = this.ch;
        if (!charArrayCompare(this.text, this.bp, cArr)) {
            this.matchStat = -2;
            return 0L;
        }
        int length = this.bp + cArr.length;
        int i5 = length + 1;
        char charAt2 = charAt(length);
        boolean z2 = charAt2 == '\"';
        if (z2) {
            i = i5 + 1;
            c = charAt(i5);
        } else {
            i = i5;
            c = charAt2;
        }
        if (c == '-') {
            int i6 = i + 1;
            char charAt3 = charAt(i);
            z = true;
            i = i6;
            c = charAt3;
        } else {
            z = false;
        }
        if (c >= '0') {
            char c4 = '9';
            if (c <= '9') {
                long j = c - '0';
                while (true) {
                    i2 = i + 1;
                    charAt = charAt(i);
                    if (charAt < '0' || charAt > c4) {
                        break;
                    }
                    j = (j * 10) + (charAt - '0');
                    i = i2;
                    c4 = '9';
                }
                if (charAt == '.') {
                    this.matchStat = -1;
                    return 0L;
                }
                if (!z2) {
                    i3 = i2;
                    c2 = charAt;
                } else {
                    if (charAt != '\"') {
                        this.matchStat = -1;
                        return 0L;
                    }
                    i3 = i2 + 1;
                    c2 = charAt(i2);
                }
                if (c2 == ',' || c2 == '}') {
                    this.bp = i3 - 1;
                }
                if (!(j >= 0 || (j == Long.MIN_VALUE && z))) {
                    this.bp = i4;
                    this.ch = c3;
                    this.matchStat = -1;
                    return 0L;
                }
                while (c2 != ',') {
                    if (c2 == '}') {
                        int i7 = this.bp + 1;
                        this.bp = i7;
                        char charAt4 = charAt(i7);
                        while (true) {
                            if (charAt4 == ',') {
                                this.token = 16;
                                int i8 = this.bp + 1;
                                this.bp = i8;
                                this.ch = charAt(i8);
                                break;
                            }
                            if (charAt4 == ']') {
                                this.token = 15;
                                int i9 = this.bp + 1;
                                this.bp = i9;
                                this.ch = charAt(i9);
                                break;
                            }
                            if (charAt4 == '}') {
                                this.token = 13;
                                int i10 = this.bp + 1;
                                this.bp = i10;
                                this.ch = charAt(i10);
                                break;
                            }
                            if (charAt4 == 26) {
                                this.token = 20;
                                break;
                            }
                            if (!isWhitespace(charAt4)) {
                                this.bp = i4;
                                this.ch = c3;
                                this.matchStat = -1;
                                return 0L;
                            }
                            int i11 = this.bp + 1;
                            this.bp = i11;
                            charAt4 = charAt(i11);
                        }
                        this.matchStat = 4;
                        return z ? -j : j;
                    }
                    if (!isWhitespace(c2)) {
                        this.matchStat = -1;
                        return 0L;
                    }
                    this.bp = i3;
                    int i12 = i3 + 1;
                    char charAt5 = charAt(i3);
                    i3 = i12;
                    c2 = charAt5;
                }
                int i13 = this.bp + 1;
                this.bp = i13;
                this.ch = charAt(i13);
                this.matchStat = 3;
                this.token = 16;
                return z ? -j : j;
            }
        }
        this.bp = i4;
        this.ch = c3;
        this.matchStat = -1;
        return 0L;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public String scanFieldString(char[] cArr) {
        this.matchStat = 0;
        int i = this.bp;
        char c = this.ch;
        while (!charArrayCompare(this.text, this.bp, cArr)) {
            if (!isWhitespace(this.ch)) {
                this.matchStat = -2;
                return stringDefaultValue();
            }
            next();
        }
        int length = this.bp + cArr.length;
        int i2 = length + 1;
        if (charAt(length) != '\"') {
            this.matchStat = -1;
            return stringDefaultValue();
        }
        int indexOf = indexOf(Typography.quote, i2);
        if (indexOf == -1) {
            throw new JSONException("unclosed str");
        }
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
            int length2 = indexOf - ((this.bp + cArr.length) + 1);
            subString = readString(sub_chars(this.bp + cArr.length + 1, length2), length2);
        }
        char charAt = charAt(indexOf + 1);
        while (charAt != ',' && charAt != '}') {
            if (!isWhitespace(charAt)) {
                this.matchStat = -1;
                return stringDefaultValue();
            }
            indexOf++;
            charAt = charAt(indexOf + 1);
        }
        this.bp = indexOf + 1;
        this.ch = charAt;
        if (charAt == ',') {
            int i5 = this.bp + 1;
            this.bp = i5;
            this.ch = charAt(i5);
            this.matchStat = 3;
            return subString;
        }
        int i6 = this.bp + 1;
        this.bp = i6;
        char charAt2 = charAt(i6);
        if (charAt2 == ',') {
            this.token = 16;
            int i7 = this.bp + 1;
            this.bp = i7;
            this.ch = charAt(i7);
        } else if (charAt2 == ']') {
            this.token = 15;
            int i8 = this.bp + 1;
            this.bp = i8;
            this.ch = charAt(i8);
        } else if (charAt2 == '}') {
            this.token = 13;
            int i9 = this.bp + 1;
            this.bp = i9;
            this.ch = charAt(i9);
        } else {
            if (charAt2 != 26) {
                this.bp = i;
                this.ch = c;
                this.matchStat = -1;
                return stringDefaultValue();
            }
            this.token = 20;
        }
        this.matchStat = 4;
        return subString;
    }

    /* JADX WARN: Code restructure failed: missing block: B:82:0x00d1, code lost:
    
        if (r1 != ']') goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x00d7, code lost:
    
        if (r3.size() != 0) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x00d9, code lost:
    
        r1 = r5 + 1;
        r5 = charAt(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x00e0, code lost:
    
        r17.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x00e2, code lost:
    
        return null;
     */
    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.util.Collection<java.lang.String> scanFieldStringArray(char[] r18, java.lang.Class<?> r19) {
        /*
            Method dump skipped, instructions count: 373
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONScanner.scanFieldStringArray(char[], java.lang.Class):java.util.Collection");
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public String[] scanFieldStringArray(char[] cArr, int i, SymbolTable symbolTable) {
        int i2;
        char c;
        int i3 = this.bp;
        char c2 = this.ch;
        while (isWhitespace(this.ch)) {
            next();
        }
        if (cArr != null) {
            this.matchStat = 0;
            if (!charArrayCompare(cArr)) {
                this.matchStat = -2;
                return null;
            }
            int length = this.bp + cArr.length;
            int i4 = length + 1;
            char charAt = this.text.charAt(length);
            while (isWhitespace(charAt)) {
                charAt = this.text.charAt(i4);
                i4++;
            }
            if (charAt != ':') {
                this.matchStat = -1;
                return null;
            }
            i2 = i4 + 1;
            c = this.text.charAt(i4);
            while (isWhitespace(c)) {
                c = this.text.charAt(i2);
                i2++;
            }
        } else {
            i2 = this.bp + 1;
            c = this.ch;
        }
        if (c != '[') {
            if (c != 'n' || !this.text.startsWith("ull", this.bp + 1)) {
                this.matchStat = -1;
                return null;
            }
            this.bp += 4;
            this.ch = this.text.charAt(this.bp);
            return null;
        }
        this.bp = i2;
        this.ch = this.text.charAt(this.bp);
        String[] strArr = i >= 0 ? new String[i] : new String[4];
        int i5 = 0;
        while (true) {
            if (isWhitespace(this.ch)) {
                next();
            } else {
                if (this.ch != '\"') {
                    this.bp = i3;
                    this.ch = c2;
                    this.matchStat = -1;
                    return null;
                }
                String scanSymbol = scanSymbol(symbolTable, Typography.quote);
                if (i5 == strArr.length) {
                    String[] strArr2 = new String[strArr.length + (strArr.length >> 1) + 1];
                    System.arraycopy(strArr, 0, strArr2, 0, strArr.length);
                    strArr = strArr2;
                }
                int i6 = i5 + 1;
                strArr[i5] = scanSymbol;
                while (isWhitespace(this.ch)) {
                    next();
                }
                if (this.ch != ',') {
                    if (strArr.length != i6) {
                        String[] strArr3 = new String[i6];
                        System.arraycopy(strArr, 0, strArr3, 0, i6);
                        strArr = strArr3;
                    }
                    while (isWhitespace(this.ch)) {
                        next();
                    }
                    if (this.ch == ']') {
                        next();
                        return strArr;
                    }
                    this.bp = i3;
                    this.ch = c2;
                    this.matchStat = -1;
                    return null;
                }
                next();
                i5 = i6;
            }
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public long scanFieldSymbol(char[] cArr) {
        this.matchStat = 0;
        if (!charArrayCompare(this.text, this.bp, cArr)) {
            this.matchStat = -2;
            return 0L;
        }
        int length = this.bp + cArr.length;
        int i = length + 1;
        if (charAt(length) != '\"') {
            this.matchStat = -1;
            return 0L;
        }
        long j = -3750763034362895579L;
        while (true) {
            int i2 = i + 1;
            char charAt = charAt(i);
            if (charAt == '\"') {
                this.bp = i2;
                char charAt2 = charAt(this.bp);
                this.ch = charAt2;
                while (charAt2 != ',') {
                    if (charAt2 == '}') {
                        next();
                        skipWhitespace();
                        char current = getCurrent();
                        if (current == ',') {
                            this.token = 16;
                            int i3 = this.bp + 1;
                            this.bp = i3;
                            this.ch = charAt(i3);
                        } else if (current == ']') {
                            this.token = 15;
                            int i4 = this.bp + 1;
                            this.bp = i4;
                            this.ch = charAt(i4);
                        } else if (current == '}') {
                            this.token = 13;
                            int i5 = this.bp + 1;
                            this.bp = i5;
                            this.ch = charAt(i5);
                        } else {
                            if (current != 26) {
                                this.matchStat = -1;
                                return 0L;
                            }
                            this.token = 20;
                        }
                        this.matchStat = 4;
                        return j;
                    }
                    if (!isWhitespace(charAt2)) {
                        this.matchStat = -1;
                        return 0L;
                    }
                    int i6 = this.bp + 1;
                    this.bp = i6;
                    charAt2 = charAt(i6);
                }
                int i7 = this.bp + 1;
                this.bp = i7;
                this.ch = charAt(i7);
                this.matchStat = 3;
                return j;
            }
            if (i2 > this.len) {
                this.matchStat = -1;
                return 0L;
            }
            j = (j ^ charAt) * 1099511628211L;
            i = i2;
        }
    }

    public boolean scanISO8601DateIfMatch() {
        return scanISO8601DateIfMatch(true);
    }

    public boolean scanISO8601DateIfMatch(boolean z) {
        return scanISO8601DateIfMatch(z, this.len - this.bp);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public final int scanInt(char c) {
        int i;
        char charAt;
        char c2;
        int i2;
        this.matchStat = 0;
        int i3 = this.bp;
        int i4 = i3 + 1;
        char charAt2 = charAt(i3);
        while (isWhitespace(charAt2)) {
            int i5 = i4 + 1;
            char charAt3 = charAt(i4);
            i4 = i5;
            charAt2 = charAt3;
        }
        boolean z = charAt2 == '\"';
        if (z) {
            int i6 = i4 + 1;
            char charAt4 = charAt(i4);
            i4 = i6;
            charAt2 = charAt4;
        }
        boolean z2 = charAt2 == '-';
        if (z2) {
            int i7 = i4 + 1;
            char charAt5 = charAt(i4);
            i4 = i7;
            charAt2 = charAt5;
        }
        if (charAt2 >= '0' && charAt2 <= '9') {
            int i8 = charAt2 - '0';
            while (true) {
                i = i4 + 1;
                charAt = charAt(i4);
                if (charAt < '0' || charAt > '9') {
                    break;
                }
                i8 = (i8 * 10) + (charAt - '0');
                i4 = i;
            }
            if (charAt == '.') {
                this.matchStat = -1;
                return 0;
            }
            if (!z) {
                c2 = charAt;
                i2 = i;
            } else {
                if (charAt != '\"') {
                    this.matchStat = -1;
                    return 0;
                }
                i2 = i + 1;
                c2 = charAt(i);
            }
            if (i8 < 0) {
                this.matchStat = -1;
                return 0;
            }
            while (c2 != c) {
                if (!isWhitespace(c2)) {
                    this.matchStat = -1;
                    return z2 ? -i8 : i8;
                }
                c2 = charAt(i2);
                i2++;
            }
            this.bp = i2;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            this.token = 16;
            return z2 ? -i8 : i8;
        }
        if (charAt2 == 'n') {
            int i9 = i4 + 1;
            if (charAt(i4) == 'u') {
                int i10 = i9 + 1;
                if (charAt(i9) == 'l') {
                    int i11 = i10 + 1;
                    if (charAt(i10) == 'l') {
                        this.matchStat = 5;
                        int i12 = i11 + 1;
                        char charAt6 = charAt(i11);
                        if (z && charAt6 == '\"') {
                            int i13 = i12 + 1;
                            char charAt7 = charAt(i12);
                            i12 = i13;
                            charAt6 = charAt7;
                        }
                        while (charAt6 != ',') {
                            if (charAt6 == ']') {
                                this.bp = i12;
                                this.ch = charAt(this.bp);
                                this.matchStat = 5;
                                this.token = 15;
                                return 0;
                            }
                            if (!isWhitespace(charAt6)) {
                                this.matchStat = -1;
                                return 0;
                            }
                            int i14 = i12 + 1;
                            char charAt8 = charAt(i12);
                            i12 = i14;
                            charAt6 = charAt8;
                        }
                        this.bp = i12;
                        this.ch = charAt(this.bp);
                        this.matchStat = 5;
                        this.token = 16;
                        return 0;
                    }
                }
            }
        }
        this.matchStat = -1;
        return 0;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public long scanLong(char c) {
        int i;
        char charAt;
        boolean z = false;
        this.matchStat = 0;
        int i2 = this.bp;
        int i3 = i2 + 1;
        char charAt2 = charAt(i2);
        boolean z2 = charAt2 == '\"';
        if (z2) {
            int i4 = i3 + 1;
            char charAt3 = charAt(i3);
            i3 = i4;
            charAt2 = charAt3;
        }
        boolean z3 = charAt2 == '-';
        if (z3) {
            int i5 = i3 + 1;
            char charAt4 = charAt(i3);
            i3 = i5;
            charAt2 = charAt4;
        }
        char c2 = '0';
        if (charAt2 >= '0' && charAt2 <= '9') {
            long j = charAt2 - '0';
            while (true) {
                i = i3 + 1;
                charAt = charAt(i3);
                if (charAt < c2 || charAt > '9') {
                    break;
                }
                j = (j * 10) + (charAt - '0');
                i3 = i;
                c2 = '0';
            }
            if (charAt == '.') {
                this.matchStat = -1;
                return 0L;
            }
            if (z2) {
                if (charAt != '\"') {
                    this.matchStat = -1;
                    return 0L;
                }
                charAt = charAt(i);
                i++;
            }
            if (j >= 0 || (j == Long.MIN_VALUE && z3)) {
                z = true;
            }
            if (!z) {
                this.matchStat = -1;
                return 0L;
            }
            while (charAt != c) {
                if (!isWhitespace(charAt)) {
                    this.matchStat = -1;
                    return j;
                }
                charAt = charAt(i);
                i++;
            }
            this.bp = i;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            this.token = 16;
            return z3 ? -j : j;
        }
        if (charAt2 == 'n') {
            int i6 = i3 + 1;
            if (charAt(i3) == 'u') {
                int i7 = i6 + 1;
                if (charAt(i6) == 'l') {
                    int i8 = i7 + 1;
                    if (charAt(i7) == 'l') {
                        this.matchStat = 5;
                        int i9 = i8 + 1;
                        char charAt5 = charAt(i8);
                        if (z2 && charAt5 == '\"') {
                            int i10 = i9 + 1;
                            char charAt6 = charAt(i9);
                            i9 = i10;
                            charAt5 = charAt6;
                        }
                        while (charAt5 != ',') {
                            if (charAt5 == ']') {
                                this.bp = i9;
                                this.ch = charAt(this.bp);
                                this.matchStat = 5;
                                this.token = 15;
                                return 0L;
                            }
                            if (!isWhitespace(charAt5)) {
                                this.matchStat = -1;
                                return 0L;
                            }
                            int i11 = i9 + 1;
                            char charAt7 = charAt(i9);
                            i9 = i11;
                            charAt5 = charAt7;
                        }
                        this.bp = i9;
                        this.ch = charAt(this.bp);
                        this.matchStat = 5;
                        this.token = 16;
                        return 0L;
                    }
                }
            }
        }
        this.matchStat = -1;
        return 0L;
    }

    protected void setTime(char c, char c2, char c3, char c4, char c5, char c6) {
        this.calendar.set(11, ((c - '0') * 10) + (c2 - '0'));
        this.calendar.set(12, ((c3 - '0') * 10) + (c4 - '0'));
        this.calendar.set(13, ((c5 - '0') * 10) + (c6 - '0'));
    }

    protected void setTimeZone(char c, char c2, char c3) {
        setTimeZone(c, c2, c3, '0', '0');
    }

    protected void setTimeZone(char c, char c2, char c3, char c4, char c5) {
        int i = ((((c2 - '0') * 10) + (c3 - '0')) * 3600 * 1000) + ((((c4 - '0') * 10) + (c5 - '0')) * 60 * 1000);
        if (c == '-') {
            i = -i;
        }
        if (this.calendar.getTimeZone().getRawOffset() != i) {
            String[] availableIDs = TimeZone.getAvailableIDs(i);
            if (availableIDs.length > 0) {
                this.calendar.setTimeZone(TimeZone.getTimeZone(availableIDs[0]));
            }
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public final String stringVal() {
        return !this.hasSpecial ? subString(this.np + 1, this.sp) : new String(this.sbuf, 0, this.sp);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final String subString(int i, int i2) {
        if (!ASMUtils.IS_ANDROID) {
            return this.text.substring(i, i2 + i);
        }
        if (i2 < this.sbuf.length) {
            this.text.getChars(i, i + i2, this.sbuf, 0);
            return new String(this.sbuf, 0, i2);
        }
        char[] cArr = new char[i2];
        this.text.getChars(i, i2 + i, cArr, 0);
        return new String(cArr);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final char[] sub_chars(int i, int i2) {
        if (ASMUtils.IS_ANDROID && i2 < this.sbuf.length) {
            this.text.getChars(i, i2 + i, this.sbuf, 0);
            return this.sbuf;
        }
        char[] cArr = new char[i2];
        this.text.getChars(i, i2 + i, cArr, 0);
        return cArr;
    }
}
