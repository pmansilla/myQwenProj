package org.apache.commons.lang.text;

import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang.Validate;

/* loaded from: classes2.dex */
public class ExtendedMessageFormat extends MessageFormat {
    private static final String DUMMY_PATTERN = "";
    private static final char END_FE = '}';
    private static final String ESCAPED_QUOTE = "''";
    private static final char QUOTE = '\'';
    private static final char START_FE = '{';
    private static final char START_FMT = ',';
    private static final long serialVersionUID = -2362048321261811743L;
    private Map registry;
    private String toPattern;

    public ExtendedMessageFormat(String str) {
        this(str, Locale.getDefault());
    }

    public ExtendedMessageFormat(String str, Locale locale) {
        this(str, locale, null);
    }

    public ExtendedMessageFormat(String str, Locale locale, Map map) {
        super("");
        setLocale(locale);
        this.registry = map;
        applyPattern(str);
    }

    public ExtendedMessageFormat(String str, Map map) {
        this(str, Locale.getDefault(), map);
    }

    private StringBuffer appendQuotedString(String str, ParsePosition parsePosition, StringBuffer stringBuffer, boolean z) {
        int index = parsePosition.getIndex();
        char[] charArray = str.toCharArray();
        if (z && charArray[index] == '\'') {
            if (stringBuffer == null) {
                return null;
            }
            stringBuffer.append(QUOTE);
            return stringBuffer;
        }
        int i = index;
        for (int index2 = parsePosition.getIndex(); index2 < str.length(); index2++) {
            if (z && str.substring(index2).startsWith(ESCAPED_QUOTE)) {
                stringBuffer.append(charArray, i, parsePosition.getIndex() - i);
                stringBuffer.append(QUOTE);
                parsePosition.setIndex(ESCAPED_QUOTE.length() + index2);
                i = parsePosition.getIndex();
            } else {
                if (charArray[parsePosition.getIndex()] == '\'') {
                    next(parsePosition);
                    if (stringBuffer == null) {
                        return null;
                    }
                    stringBuffer.append(charArray, i, parsePosition.getIndex() - i);
                    return stringBuffer;
                }
                next(parsePosition);
            }
        }
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append("Unterminated quoted string at position ");
        stringBuffer2.append(index);
        throw new IllegalArgumentException(stringBuffer2.toString());
    }

    private boolean containsElements(Collection collection) {
        if (collection == null || collection.size() == 0) {
            return false;
        }
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            if (it.next() != null) {
                return true;
            }
        }
        return false;
    }

    private Format getFormat(String str) {
        String str2;
        if (this.registry != null) {
            int indexOf = str.indexOf(44);
            if (indexOf > 0) {
                String trim = str.substring(0, indexOf).trim();
                str2 = str.substring(indexOf + 1).trim();
                str = trim;
            } else {
                str2 = null;
            }
            FormatFactory formatFactory = (FormatFactory) this.registry.get(str);
            if (formatFactory != null) {
                return formatFactory.getFormat(str, str2, getLocale());
            }
        }
        return null;
    }

    private void getQuotedString(String str, ParsePosition parsePosition, boolean z) {
        appendQuotedString(str, parsePosition, null, z);
    }

    private String insertFormats(String str, ArrayList arrayList) {
        if (!containsElements(arrayList)) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer(str.length() * 2);
        ParsePosition parsePosition = new ParsePosition(0);
        int i = 0;
        int i2 = -1;
        while (parsePosition.getIndex() < str.length()) {
            char charAt = str.charAt(parsePosition.getIndex());
            if (charAt == '\'') {
                appendQuotedString(str, parsePosition, stringBuffer, false);
            } else if (charAt != '{') {
                if (charAt == '}') {
                    i--;
                }
                stringBuffer.append(charAt);
                next(parsePosition);
            } else {
                i++;
                if (i == 1) {
                    i2++;
                    stringBuffer.append(START_FE);
                    stringBuffer.append(readArgumentIndex(str, next(parsePosition)));
                    String str2 = (String) arrayList.get(i2);
                    if (str2 != null) {
                        stringBuffer.append(START_FMT);
                        stringBuffer.append(str2);
                    }
                }
            }
        }
        return stringBuffer.toString();
    }

    private ParsePosition next(ParsePosition parsePosition) {
        parsePosition.setIndex(parsePosition.getIndex() + 1);
        return parsePosition;
    }

    private String parseFormatDescription(String str, ParsePosition parsePosition) {
        int index = parsePosition.getIndex();
        seekNonWs(str, parsePosition);
        int index2 = parsePosition.getIndex();
        int i = 1;
        while (parsePosition.getIndex() < str.length()) {
            char charAt = str.charAt(parsePosition.getIndex());
            if (charAt == '\'') {
                getQuotedString(str, parsePosition, false);
            } else if (charAt == '{') {
                i++;
            } else if (charAt == '}' && i - 1 == 0) {
                return str.substring(index2, parsePosition.getIndex());
            }
            next(parsePosition);
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Unterminated format element at position ");
        stringBuffer.append(index);
        throw new IllegalArgumentException(stringBuffer.toString());
    }

    private int readArgumentIndex(String str, ParsePosition parsePosition) {
        int index = parsePosition.getIndex();
        seekNonWs(str, parsePosition);
        StringBuffer stringBuffer = new StringBuffer();
        boolean z = false;
        while (!z && parsePosition.getIndex() < str.length()) {
            char charAt = str.charAt(parsePosition.getIndex());
            if (Character.isWhitespace(charAt)) {
                seekNonWs(str, parsePosition);
                charAt = str.charAt(parsePosition.getIndex());
                if (charAt != ',' && charAt != '}') {
                    z = true;
                    next(parsePosition);
                }
            }
            if ((charAt == ',' || charAt == '}') && stringBuffer.length() > 0) {
                try {
                    return Integer.parseInt(stringBuffer.toString());
                } catch (NumberFormatException unused) {
                }
            }
            boolean z2 = !Character.isDigit(charAt);
            stringBuffer.append(charAt);
            z = z2;
            next(parsePosition);
        }
        if (!z) {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("Unterminated format element at position ");
            stringBuffer2.append(index);
            throw new IllegalArgumentException(stringBuffer2.toString());
        }
        StringBuffer stringBuffer3 = new StringBuffer();
        stringBuffer3.append("Invalid format argument index at position ");
        stringBuffer3.append(index);
        stringBuffer3.append(": ");
        stringBuffer3.append(str.substring(index, parsePosition.getIndex()));
        throw new IllegalArgumentException(stringBuffer3.toString());
    }

    private void seekNonWs(String str, ParsePosition parsePosition) {
        char[] charArray = str.toCharArray();
        do {
            int isMatch = StrMatcher.splitMatcher().isMatch(charArray, parsePosition.getIndex());
            parsePosition.setIndex(parsePosition.getIndex() + isMatch);
            if (isMatch <= 0) {
                return;
            }
        } while (parsePosition.getIndex() < str.length());
    }

    @Override // java.text.MessageFormat
    public final void applyPattern(String str) {
        String str2;
        Format format;
        if (this.registry == null) {
            super.applyPattern(str);
            this.toPattern = super.toPattern();
            return;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        StringBuffer stringBuffer = new StringBuffer(str.length());
        int i = 0;
        ParsePosition parsePosition = new ParsePosition(0);
        char[] charArray = str.toCharArray();
        int i2 = 0;
        while (parsePosition.getIndex() < str.length()) {
            char c = charArray[parsePosition.getIndex()];
            if (c != '\'') {
                if (c == '{') {
                    i2++;
                    seekNonWs(str, parsePosition);
                    int index = parsePosition.getIndex();
                    int readArgumentIndex = readArgumentIndex(str, next(parsePosition));
                    stringBuffer.append(START_FE);
                    stringBuffer.append(readArgumentIndex);
                    seekNonWs(str, parsePosition);
                    if (charArray[parsePosition.getIndex()] == ',') {
                        str2 = parseFormatDescription(str, next(parsePosition));
                        format = getFormat(str2);
                        if (format == null) {
                            stringBuffer.append(START_FMT);
                            stringBuffer.append(str2);
                        }
                    } else {
                        str2 = null;
                        format = null;
                    }
                    arrayList.add(format);
                    if (format == null) {
                        str2 = null;
                    }
                    arrayList2.add(str2);
                    Validate.isTrue(arrayList.size() == i2);
                    Validate.isTrue(arrayList2.size() == i2);
                    if (charArray[parsePosition.getIndex()] != '}') {
                        StringBuffer stringBuffer2 = new StringBuffer();
                        stringBuffer2.append("Unreadable format element at position ");
                        stringBuffer2.append(index);
                        throw new IllegalArgumentException(stringBuffer2.toString());
                    }
                }
                stringBuffer.append(charArray[parsePosition.getIndex()]);
                next(parsePosition);
            } else {
                appendQuotedString(str, parsePosition, stringBuffer, true);
            }
        }
        super.applyPattern(stringBuffer.toString());
        this.toPattern = insertFormats(super.toPattern(), arrayList2);
        if (containsElements(arrayList)) {
            Format[] formats = getFormats();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                Format format2 = (Format) it.next();
                if (format2 != null) {
                    formats[i] = format2;
                }
                i++;
            }
            super.setFormats(formats);
        }
    }

    @Override // java.text.MessageFormat
    public void setFormat(int i, Format format) {
        throw new UnsupportedOperationException();
    }

    @Override // java.text.MessageFormat
    public void setFormatByArgumentIndex(int i, Format format) {
        throw new UnsupportedOperationException();
    }

    @Override // java.text.MessageFormat
    public void setFormats(Format[] formatArr) {
        throw new UnsupportedOperationException();
    }

    @Override // java.text.MessageFormat
    public void setFormatsByArgumentIndex(Format[] formatArr) {
        throw new UnsupportedOperationException();
    }

    @Override // java.text.MessageFormat
    public String toPattern() {
        return this.toPattern;
    }
}
