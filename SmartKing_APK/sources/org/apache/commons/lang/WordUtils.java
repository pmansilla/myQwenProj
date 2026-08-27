package org.apache.commons.lang;

import com.litesuits.orm.db.assit.SQLBuilder;

/* loaded from: classes2.dex */
public class WordUtils {
    public static String abbreviate(String str, int i, int i2, String str2) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return "";
        }
        if (i2 == -1 || i2 > str.length()) {
            i2 = str.length();
        }
        if (i2 < i) {
            i2 = i;
        }
        StringBuffer stringBuffer = new StringBuffer();
        int indexOf = StringUtils.indexOf(str, SQLBuilder.BLANK, i);
        if (indexOf == -1) {
            stringBuffer.append(str.substring(0, i2));
            if (i2 != str.length()) {
                stringBuffer.append(StringUtils.defaultString(str2));
            }
        } else if (indexOf > i2) {
            stringBuffer.append(str.substring(0, i2));
            stringBuffer.append(StringUtils.defaultString(str2));
        } else {
            stringBuffer.append(str.substring(0, indexOf));
            stringBuffer.append(StringUtils.defaultString(str2));
        }
        return stringBuffer.toString();
    }

    public static String capitalize(String str) {
        return capitalize(str, null);
    }

    public static String capitalize(String str, char[] cArr) {
        int length = cArr == null ? -1 : cArr.length;
        if (str == null || str.length() == 0 || length == 0) {
            return str;
        }
        int length2 = str.length();
        StringBuffer stringBuffer = new StringBuffer(length2);
        boolean z = true;
        for (int i = 0; i < length2; i++) {
            char charAt = str.charAt(i);
            if (isDelimiter(charAt, cArr)) {
                stringBuffer.append(charAt);
                z = true;
            } else if (z) {
                stringBuffer.append(Character.toTitleCase(charAt));
                z = false;
            } else {
                stringBuffer.append(charAt);
            }
        }
        return stringBuffer.toString();
    }

    public static String capitalizeFully(String str) {
        return capitalizeFully(str, null);
    }

    public static String capitalizeFully(String str, char[] cArr) {
        return (str == null || str.length() == 0 || (cArr == null ? -1 : cArr.length) == 0) ? str : capitalize(str.toLowerCase(), cArr);
    }

    public static String initials(String str) {
        return initials(str, null);
    }

    public static String initials(String str, char[] cArr) {
        if (str == null || str.length() == 0) {
            return str;
        }
        if (cArr != null && cArr.length == 0) {
            return "";
        }
        int length = str.length();
        char[] cArr2 = new char[(length / 2) + 1];
        int i = 0;
        boolean z = true;
        for (int i2 = 0; i2 < length; i2++) {
            char charAt = str.charAt(i2);
            if (isDelimiter(charAt, cArr)) {
                z = true;
            } else if (z) {
                cArr2[i] = charAt;
                i++;
                z = false;
            }
        }
        return new String(cArr2, 0, i);
    }

    private static boolean isDelimiter(char c, char[] cArr) {
        if (cArr == null) {
            return Character.isWhitespace(c);
        }
        for (char c2 : cArr) {
            if (c == c2) {
                return true;
            }
        }
        return false;
    }

    public static String swapCase(String str) {
        int length;
        if (str == null || (length = str.length()) == 0) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer(length);
        boolean z = true;
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            stringBuffer.append(Character.isUpperCase(charAt) ? Character.toLowerCase(charAt) : Character.isTitleCase(charAt) ? Character.toLowerCase(charAt) : Character.isLowerCase(charAt) ? z ? Character.toTitleCase(charAt) : Character.toUpperCase(charAt) : charAt);
            z = Character.isWhitespace(charAt);
        }
        return stringBuffer.toString();
    }

    public static String uncapitalize(String str) {
        return uncapitalize(str, null);
    }

    public static String uncapitalize(String str, char[] cArr) {
        int length = cArr == null ? -1 : cArr.length;
        if (str == null || str.length() == 0 || length == 0) {
            return str;
        }
        int length2 = str.length();
        StringBuffer stringBuffer = new StringBuffer(length2);
        boolean z = true;
        for (int i = 0; i < length2; i++) {
            char charAt = str.charAt(i);
            if (isDelimiter(charAt, cArr)) {
                stringBuffer.append(charAt);
                z = true;
            } else if (z) {
                stringBuffer.append(Character.toLowerCase(charAt));
                z = false;
            } else {
                stringBuffer.append(charAt);
            }
        }
        return stringBuffer.toString();
    }

    public static String wrap(String str, int i) {
        return wrap(str, i, null, false);
    }

    public static String wrap(String str, int i, String str2, boolean z) {
        if (str == null) {
            return null;
        }
        if (str2 == null) {
            str2 = SystemUtils.LINE_SEPARATOR;
        }
        if (i < 1) {
            i = 1;
        }
        int length = str.length();
        int i2 = 0;
        StringBuffer stringBuffer = new StringBuffer(length + 32);
        while (length - i2 > i) {
            if (str.charAt(i2) == ' ') {
                i2++;
            } else {
                int i3 = i + i2;
                int lastIndexOf = str.lastIndexOf(32, i3);
                if (lastIndexOf >= i2) {
                    stringBuffer.append(str.substring(i2, lastIndexOf));
                    stringBuffer.append(str2);
                    i2 = lastIndexOf + 1;
                } else if (z) {
                    stringBuffer.append(str.substring(i2, i3));
                    stringBuffer.append(str2);
                    i2 = i3;
                } else {
                    int indexOf = str.indexOf(32, i3);
                    if (indexOf >= 0) {
                        stringBuffer.append(str.substring(i2, indexOf));
                        stringBuffer.append(str2);
                        i2 = indexOf + 1;
                    } else {
                        stringBuffer.append(str.substring(i2));
                        i2 = length;
                    }
                }
            }
        }
        stringBuffer.append(str.substring(i2));
        return stringBuffer.toString();
    }
}
