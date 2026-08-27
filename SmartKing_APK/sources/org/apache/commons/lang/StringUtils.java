package org.apache.commons.lang;

import com.litesuits.orm.db.assit.SQLBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/* loaded from: classes2.dex */
public class StringUtils {
    public static final String EMPTY = "";
    public static final int INDEX_NOT_FOUND = -1;
    private static final int PAD_LIMIT = 8192;

    public static String abbreviate(String str, int i) {
        return abbreviate(str, 0, i);
    }

    public static String abbreviate(String str, int i, int i2) {
        if (str == null) {
            return null;
        }
        if (i2 < 4) {
            throw new IllegalArgumentException("Minimum abbreviation width is 4");
        }
        if (str.length() <= i2) {
            return str;
        }
        if (i > str.length()) {
            i = str.length();
        }
        int i3 = i2 - 3;
        if (str.length() - i < i3) {
            i = str.length() - i3;
        }
        if (i <= 4) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(str.substring(0, i3));
            stringBuffer.append("...");
            return stringBuffer.toString();
        }
        if (i2 < 7) {
            throw new IllegalArgumentException("Minimum abbreviation width with offset is 7");
        }
        if (i + i3 < str.length()) {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("...");
            stringBuffer2.append(abbreviate(str.substring(i), i3));
            return stringBuffer2.toString();
        }
        StringBuffer stringBuffer3 = new StringBuffer();
        stringBuffer3.append("...");
        stringBuffer3.append(str.substring(str.length() - i3));
        return stringBuffer3.toString();
    }

    public static String capitalise(String str) {
        return capitalize(str);
    }

    public static String capitaliseAllWords(String str) {
        return WordUtils.capitalize(str);
    }

    public static String capitalize(String str) {
        int length;
        if (str == null || (length = str.length()) == 0) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer(length);
        stringBuffer.append(Character.toTitleCase(str.charAt(0)));
        stringBuffer.append(str.substring(1));
        return stringBuffer.toString();
    }

    public static String center(String str, int i) {
        return center(str, i, ' ');
    }

    public static String center(String str, int i, char c) {
        int length;
        int length2;
        return (str == null || i <= 0 || (length2 = i - (length = str.length())) <= 0) ? str : rightPad(leftPad(str, length + (length2 / 2), c), i, c);
    }

    public static String center(String str, int i, String str2) {
        if (str == null || i <= 0) {
            return str;
        }
        if (isEmpty(str2)) {
            str2 = SQLBuilder.BLANK;
        }
        int length = str.length();
        int i2 = i - length;
        return i2 <= 0 ? str : rightPad(leftPad(str, length + (i2 / 2), str2), i, str2);
    }

    public static String chomp(String str) {
        if (isEmpty(str)) {
            return str;
        }
        if (str.length() == 1) {
            char charAt = str.charAt(0);
            return (charAt == '\r' || charAt == '\n') ? "" : str;
        }
        int length = str.length() - 1;
        char charAt2 = str.charAt(length);
        if (charAt2 == '\n') {
            if (str.charAt(length - 1) == '\r') {
                length--;
            }
        } else if (charAt2 != '\r') {
            length++;
        }
        return str.substring(0, length);
    }

    public static String chomp(String str, String str2) {
        return (isEmpty(str) || str2 == null || !str.endsWith(str2)) ? str : str.substring(0, str.length() - str2.length());
    }

    public static String chompLast(String str) {
        return chompLast(str, "\n");
    }

    public static String chompLast(String str, String str2) {
        return (str.length() != 0 && str2.equals(str.substring(str.length() - str2.length()))) ? str.substring(0, str.length() - str2.length()) : str;
    }

    public static String chop(String str) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (length < 2) {
            return "";
        }
        int i = length - 1;
        String substring = str.substring(0, i);
        if (str.charAt(i) == '\n') {
            int i2 = i - 1;
            if (substring.charAt(i2) == '\r') {
                return substring.substring(0, i2);
            }
        }
        return substring;
    }

    public static String chopNewline(String str) {
        int length = str.length() - 1;
        if (length <= 0) {
            return "";
        }
        if (str.charAt(length) != '\n') {
            length++;
        } else if (str.charAt(length - 1) == '\r') {
            length--;
        }
        return str.substring(0, length);
    }

    public static String clean(String str) {
        return str == null ? "" : str.trim();
    }

    public static String concatenate(Object[] objArr) {
        return join(objArr, (String) null);
    }

    public static boolean contains(String str, char c) {
        return !isEmpty(str) && str.indexOf(c) >= 0;
    }

    public static boolean contains(String str, String str2) {
        return (str == null || str2 == null || str.indexOf(str2) < 0) ? false : true;
    }

    public static boolean containsAny(String str, String str2) {
        if (str2 == null) {
            return false;
        }
        return containsAny(str, str2.toCharArray());
    }

    public static boolean containsAny(String str, char[] cArr) {
        if (str == null || str.length() == 0 || cArr == null || cArr.length == 0) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            for (char c : cArr) {
                if (c == charAt) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean containsIgnoreCase(String str, String str2) {
        if (str == null || str2 == null) {
            return false;
        }
        return contains(str.toUpperCase(), str2.toUpperCase());
    }

    public static boolean containsNone(String str, String str2) {
        if (str == null || str2 == null) {
            return true;
        }
        return containsNone(str, str2.toCharArray());
    }

    public static boolean containsNone(String str, char[] cArr) {
        if (str == null || cArr == null) {
            return true;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            for (char c : cArr) {
                if (c == charAt) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean containsOnly(String str, String str2) {
        if (str == null || str2 == null) {
            return false;
        }
        return containsOnly(str, str2.toCharArray());
    }

    public static boolean containsOnly(String str, char[] cArr) {
        if (cArr == null || str == null) {
            return false;
        }
        if (str.length() == 0) {
            return true;
        }
        return cArr.length != 0 && indexOfAnyBut(str, cArr) == -1;
    }

    public static int countMatches(String str, String str2) {
        int i = 0;
        if (isEmpty(str) || isEmpty(str2)) {
            return 0;
        }
        int i2 = 0;
        while (true) {
            int indexOf = str.indexOf(str2, i);
            if (indexOf == -1) {
                return i2;
            }
            i2++;
            i = indexOf + str2.length();
        }
    }

    public static String defaultIfEmpty(String str, String str2) {
        return isEmpty(str) ? str2 : str;
    }

    public static String defaultString(String str) {
        return str == null ? "" : str;
    }

    public static String defaultString(String str, String str2) {
        return str == null ? str2 : str;
    }

    public static String deleteSpaces(String str) {
        if (str == null) {
            return null;
        }
        return CharSetUtils.delete(str, " \t\r\n\b");
    }

    public static String deleteWhitespace(String str) {
        if (isEmpty(str)) {
            return str;
        }
        int length = str.length();
        char[] cArr = new char[length];
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            if (!Character.isWhitespace(str.charAt(i2))) {
                cArr[i] = str.charAt(i2);
                i++;
            }
        }
        return i == length ? str : new String(cArr, 0, i);
    }

    public static String difference(String str, String str2) {
        if (str == null) {
            return str2;
        }
        if (str2 == null) {
            return str;
        }
        int indexOfDifference = indexOfDifference(str, str2);
        return indexOfDifference == -1 ? "" : str2.substring(indexOfDifference);
    }

    public static boolean endsWith(String str, String str2) {
        return endsWith(str, str2, false);
    }

    private static boolean endsWith(String str, String str2, boolean z) {
        if (str == null || str2 == null) {
            return str == null && str2 == null;
        }
        if (str2.length() > str.length()) {
            return false;
        }
        return str.regionMatches(z, str.length() - str2.length(), str2, 0, str2.length());
    }

    public static boolean endsWithIgnoreCase(String str, String str2) {
        return endsWith(str, str2, true);
    }

    public static boolean equals(String str, String str2) {
        return str == null ? str2 == null : str.equals(str2);
    }

    public static boolean equalsIgnoreCase(String str, String str2) {
        return str == null ? str2 == null : str.equalsIgnoreCase(str2);
    }

    public static String escape(String str) {
        return StringEscapeUtils.escapeJava(str);
    }

    public static String getChomp(String str, String str2) {
        int lastIndexOf = str.lastIndexOf(str2);
        return lastIndexOf == str.length() - str2.length() ? str2 : lastIndexOf != -1 ? str.substring(lastIndexOf) : "";
    }

    public static String getCommonPrefix(String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            return "";
        }
        int indexOfDifference = indexOfDifference(strArr);
        return indexOfDifference == -1 ? strArr[0] == null ? "" : strArr[0] : indexOfDifference == 0 ? "" : strArr[0].substring(0, indexOfDifference);
    }

    public static int getLevenshteinDistance(String str, String str2) {
        if (str == null || str2 == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }
        int length = str.length();
        int length2 = str2.length();
        if (length == 0) {
            return length2;
        }
        if (length2 == 0) {
            return length;
        }
        if (length > length2) {
            length2 = str.length();
            length = length2;
        } else {
            str2 = str;
            str = str2;
        }
        int i = length + 1;
        int[] iArr = new int[i];
        int[] iArr2 = new int[i];
        for (int i2 = 0; i2 <= length; i2++) {
            iArr[i2] = i2;
        }
        int[] iArr3 = iArr;
        int[] iArr4 = iArr2;
        int i3 = 1;
        while (i3 <= length2) {
            char charAt = str.charAt(i3 - 1);
            iArr4[0] = i3;
            for (int i4 = 1; i4 <= length; i4++) {
                int i5 = i4 - 1;
                iArr4[i4] = Math.min(Math.min(iArr4[i5] + 1, iArr3[i4] + 1), iArr3[i5] + (str2.charAt(i5) == charAt ? 0 : 1));
            }
            i3++;
            int[] iArr5 = iArr3;
            iArr3 = iArr4;
            iArr4 = iArr5;
        }
        return iArr3[length];
    }

    public static String getNestedString(String str, String str2) {
        return substringBetween(str, str2, str2);
    }

    public static String getNestedString(String str, String str2, String str3) {
        return substringBetween(str, str2, str3);
    }

    public static String getPrechomp(String str, String str2) {
        int indexOf = str.indexOf(str2);
        return indexOf == -1 ? "" : str.substring(0, indexOf + str2.length());
    }

    public static int indexOf(String str, char c) {
        if (isEmpty(str)) {
            return -1;
        }
        return str.indexOf(c);
    }

    public static int indexOf(String str, char c, int i) {
        if (isEmpty(str)) {
            return -1;
        }
        return str.indexOf(c, i);
    }

    public static int indexOf(String str, String str2) {
        if (str == null || str2 == null) {
            return -1;
        }
        return str.indexOf(str2);
    }

    public static int indexOf(String str, String str2, int i) {
        if (str == null || str2 == null) {
            return -1;
        }
        return (str2.length() != 0 || i < str.length()) ? str.indexOf(str2, i) : str.length();
    }

    public static int indexOfAny(String str, String str2) {
        if (isEmpty(str) || isEmpty(str2)) {
            return -1;
        }
        return indexOfAny(str, str2.toCharArray());
    }

    public static int indexOfAny(String str, char[] cArr) {
        if (isEmpty(str) || ArrayUtils.isEmpty(cArr)) {
            return -1;
        }
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            for (char c : cArr) {
                if (c == charAt) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static int indexOfAny(String str, String[] strArr) {
        int indexOf;
        if (str == null || strArr == null) {
            return -1;
        }
        int i = Integer.MAX_VALUE;
        for (String str2 : strArr) {
            if (str2 != null && (indexOf = str.indexOf(str2)) != -1 && indexOf < i) {
                i = indexOf;
            }
        }
        if (i == Integer.MAX_VALUE) {
            return -1;
        }
        return i;
    }

    public static int indexOfAnyBut(String str, String str2) {
        if (isEmpty(str) || isEmpty(str2)) {
            return -1;
        }
        for (int i = 0; i < str.length(); i++) {
            if (str2.indexOf(str.charAt(i)) < 0) {
                return i;
            }
        }
        return -1;
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0022, code lost:
    
        r2 = r2 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int indexOfAnyBut(java.lang.String r6, char[] r7) {
        /*
            boolean r0 = isEmpty(r6)
            r1 = -1
            if (r0 != 0) goto L2a
            boolean r0 = org.apache.commons.lang.ArrayUtils.isEmpty(r7)
            if (r0 == 0) goto Le
            goto L2a
        Le:
            r0 = 0
            r2 = 0
        L10:
            int r3 = r6.length()
            if (r2 >= r3) goto L29
            char r3 = r6.charAt(r2)
            r4 = 0
        L1b:
            int r5 = r7.length
            if (r4 >= r5) goto L28
            char r5 = r7[r4]
            if (r5 != r3) goto L25
            int r2 = r2 + 1
            goto L10
        L25:
            int r4 = r4 + 1
            goto L1b
        L28:
            return r2
        L29:
            return r1
        L2a:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang.StringUtils.indexOfAnyBut(java.lang.String, char[]):int");
    }

    public static int indexOfDifference(String str, String str2) {
        if (str == str2) {
            return -1;
        }
        int i = 0;
        if (str == null || str2 == null) {
            return 0;
        }
        while (i < str.length() && i < str2.length() && str.charAt(i) == str2.charAt(i)) {
            i++;
        }
        if (i < str2.length() || i < str.length()) {
            return i;
        }
        return -1;
    }

    public static int indexOfDifference(String[] strArr) {
        if (strArr == null || strArr.length <= 1) {
            return -1;
        }
        int length = strArr.length;
        boolean z = true;
        int i = Integer.MAX_VALUE;
        int i2 = 0;
        boolean z2 = false;
        for (int i3 = 0; i3 < length; i3++) {
            if (strArr[i3] == null) {
                i = 0;
                z2 = true;
            } else {
                int min = Math.min(strArr[i3].length(), i);
                i2 = Math.max(strArr[i3].length(), i2);
                i = min;
                z = false;
            }
        }
        if (z || (i2 == 0 && !z2)) {
            return -1;
        }
        if (i == 0) {
            return 0;
        }
        int i4 = -1;
        for (int i5 = 0; i5 < i; i5++) {
            char charAt = strArr[0].charAt(i5);
            int i6 = 1;
            while (true) {
                if (i6 >= length) {
                    break;
                }
                if (strArr[i6].charAt(i5) != charAt) {
                    i4 = i5;
                    break;
                }
                i6++;
            }
            if (i4 != -1) {
                break;
            }
        }
        return (i4 != -1 || i == i2) ? i4 : i;
    }

    public static boolean isAlpha(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isLetter(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlphaSpace(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isLetter(str.charAt(i)) && str.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlphanumeric(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isLetterOrDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlphanumericSpace(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isLetterOrDigit(str.charAt(i)) && str.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }

    public static boolean isAsciiPrintable(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!CharUtils.isAsciiPrintable(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isBlank(String str) {
        int length;
        if (str == null || (length = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumericSpace(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isDigit(str.charAt(i)) && str.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }

    public static boolean isWhitespace(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String join(Collection collection, char c) {
        if (collection == null) {
            return null;
        }
        return join(collection.iterator(), c);
    }

    public static String join(Collection collection, String str) {
        if (collection == null) {
            return null;
        }
        return join(collection.iterator(), str);
    }

    public static String join(Iterator it, char c) {
        if (it == null) {
            return null;
        }
        if (!it.hasNext()) {
            return "";
        }
        Object next = it.next();
        if (!it.hasNext()) {
            return ObjectUtils.toString(next);
        }
        StringBuffer stringBuffer = new StringBuffer(256);
        if (next != null) {
            stringBuffer.append(next);
        }
        while (it.hasNext()) {
            stringBuffer.append(c);
            Object next2 = it.next();
            if (next2 != null) {
                stringBuffer.append(next2);
            }
        }
        return stringBuffer.toString();
    }

    public static String join(Iterator it, String str) {
        if (it == null) {
            return null;
        }
        if (!it.hasNext()) {
            return "";
        }
        Object next = it.next();
        if (!it.hasNext()) {
            return ObjectUtils.toString(next);
        }
        StringBuffer stringBuffer = new StringBuffer(256);
        if (next != null) {
            stringBuffer.append(next);
        }
        while (it.hasNext()) {
            if (str != null) {
                stringBuffer.append(str);
            }
            Object next2 = it.next();
            if (next2 != null) {
                stringBuffer.append(next2);
            }
        }
        return stringBuffer.toString();
    }

    public static String join(Object[] objArr) {
        return join(objArr, (String) null);
    }

    public static String join(Object[] objArr, char c) {
        if (objArr == null) {
            return null;
        }
        return join(objArr, c, 0, objArr.length);
    }

    public static String join(Object[] objArr, char c, int i, int i2) {
        if (objArr == null) {
            return null;
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer(i3 * ((objArr[i] == null ? 16 : objArr[i].toString().length()) + 1));
        for (int i4 = i; i4 < i2; i4++) {
            if (i4 > i) {
                stringBuffer.append(c);
            }
            if (objArr[i4] != null) {
                stringBuffer.append(objArr[i4]);
            }
        }
        return stringBuffer.toString();
    }

    public static String join(Object[] objArr, String str) {
        if (objArr == null) {
            return null;
        }
        return join(objArr, str, 0, objArr.length);
    }

    public static String join(Object[] objArr, String str, int i, int i2) {
        if (objArr == null) {
            return null;
        }
        if (str == null) {
            str = "";
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer(i3 * ((objArr[i] == null ? 16 : objArr[i].toString().length()) + str.length()));
        for (int i4 = i; i4 < i2; i4++) {
            if (i4 > i) {
                stringBuffer.append(str);
            }
            if (objArr[i4] != null) {
                stringBuffer.append(objArr[i4]);
            }
        }
        return stringBuffer.toString();
    }

    public static int lastIndexOf(String str, char c) {
        if (isEmpty(str)) {
            return -1;
        }
        return str.lastIndexOf(c);
    }

    public static int lastIndexOf(String str, char c, int i) {
        if (isEmpty(str)) {
            return -1;
        }
        return str.lastIndexOf(c, i);
    }

    public static int lastIndexOf(String str, String str2) {
        if (str == null || str2 == null) {
            return -1;
        }
        return str.lastIndexOf(str2);
    }

    public static int lastIndexOf(String str, String str2, int i) {
        if (str == null || str2 == null) {
            return -1;
        }
        return str.lastIndexOf(str2, i);
    }

    public static int lastIndexOfAny(String str, String[] strArr) {
        int lastIndexOf;
        int i = -1;
        if (str == null || strArr == null) {
            return -1;
        }
        for (String str2 : strArr) {
            if (str2 != null && (lastIndexOf = str.lastIndexOf(str2)) > i) {
                i = lastIndexOf;
            }
        }
        return i;
    }

    public static String left(String str, int i) {
        if (str == null) {
            return null;
        }
        return i < 0 ? "" : str.length() <= i ? str : str.substring(0, i);
    }

    public static String leftPad(String str, int i) {
        return leftPad(str, i, ' ');
    }

    public static String leftPad(String str, int i, char c) {
        if (str == null) {
            return null;
        }
        int length = i - str.length();
        return length <= 0 ? str : length > 8192 ? leftPad(str, i, String.valueOf(c)) : padding(length, c).concat(str);
    }

    public static String leftPad(String str, int i, String str2) {
        if (str == null) {
            return null;
        }
        if (isEmpty(str2)) {
            str2 = SQLBuilder.BLANK;
        }
        int length = str2.length();
        int length2 = i - str.length();
        if (length2 <= 0) {
            return str;
        }
        if (length == 1 && length2 <= 8192) {
            return leftPad(str, i, str2.charAt(0));
        }
        if (length2 == length) {
            return str2.concat(str);
        }
        if (length2 < length) {
            return str2.substring(0, length2).concat(str);
        }
        char[] cArr = new char[length2];
        char[] charArray = str2.toCharArray();
        for (int i2 = 0; i2 < length2; i2++) {
            cArr[i2] = charArray[i2 % length];
        }
        return new String(cArr).concat(str);
    }

    public static int length(String str) {
        if (str == null) {
            return 0;
        }
        return str.length();
    }

    public static String lowerCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toLowerCase();
    }

    public static String mid(String str, int i, int i2) {
        if (str == null) {
            return null;
        }
        if (i2 < 0 || i > str.length()) {
            return "";
        }
        if (i < 0) {
            i = 0;
        }
        int i3 = i2 + i;
        return str.length() <= i3 ? str.substring(i) : str.substring(i, i3);
    }

    public static int ordinalIndexOf(String str, String str2, int i) {
        int i2 = -1;
        if (str == null || str2 == null || i <= 0) {
            return -1;
        }
        int i3 = 0;
        if (str2.length() == 0) {
            return 0;
        }
        do {
            i2 = str.indexOf(str2, i2 + 1);
            if (i2 < 0) {
                return i2;
            }
            i3++;
        } while (i3 < i);
        return i2;
    }

    public static String overlay(String str, String str2, int i, int i2) {
        if (str == null) {
            return null;
        }
        if (str2 == null) {
            str2 = "";
        }
        int length = str.length();
        if (i < 0) {
            i = 0;
        }
        if (i > length) {
            i = length;
        }
        if (i2 < 0) {
            i2 = 0;
        }
        if (i2 > length) {
            i2 = length;
        }
        if (i > i2) {
            int i3 = i2;
            i2 = i;
            i = i3;
        }
        StringBuffer stringBuffer = new StringBuffer(((length + i) - i2) + str2.length() + 1);
        stringBuffer.append(str.substring(0, i));
        stringBuffer.append(str2);
        stringBuffer.append(str.substring(i2));
        return stringBuffer.toString();
    }

    public static String overlayString(String str, String str2, int i, int i2) {
        StringBuffer stringBuffer = new StringBuffer((((str2.length() + i) + str.length()) - i2) + 1);
        stringBuffer.append(str.substring(0, i));
        stringBuffer.append(str2);
        stringBuffer.append(str.substring(i2));
        return stringBuffer.toString();
    }

    private static String padding(int i, char c) throws IndexOutOfBoundsException {
        if (i < 0) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Cannot pad a negative amount: ");
            stringBuffer.append(i);
            throw new IndexOutOfBoundsException(stringBuffer.toString());
        }
        char[] cArr = new char[i];
        for (int i2 = 0; i2 < cArr.length; i2++) {
            cArr[i2] = c;
        }
        return new String(cArr);
    }

    public static String prechomp(String str, String str2) {
        int indexOf = str.indexOf(str2);
        return indexOf == -1 ? str : str.substring(indexOf + str2.length());
    }

    public static String remove(String str, char c) {
        if (isEmpty(str) || str.indexOf(c) == -1) {
            return str;
        }
        char[] charArray = str.toCharArray();
        int i = 0;
        for (int i2 = 0; i2 < charArray.length; i2++) {
            if (charArray[i2] != c) {
                charArray[i] = charArray[i2];
                i++;
            }
        }
        return new String(charArray, 0, i);
    }

    public static String remove(String str, String str2) {
        return (isEmpty(str) || isEmpty(str2)) ? str : replace(str, str2, "", -1);
    }

    public static String removeEnd(String str, String str2) {
        return (isEmpty(str) || isEmpty(str2) || !str.endsWith(str2)) ? str : str.substring(0, str.length() - str2.length());
    }

    public static String removeEndIgnoreCase(String str, String str2) {
        return (isEmpty(str) || isEmpty(str2) || !endsWithIgnoreCase(str, str2)) ? str : str.substring(0, str.length() - str2.length());
    }

    public static String removeStart(String str, String str2) {
        return (isEmpty(str) || isEmpty(str2) || !str.startsWith(str2)) ? str : str.substring(str2.length());
    }

    public static String removeStartIgnoreCase(String str, String str2) {
        return (isEmpty(str) || isEmpty(str2) || !startsWithIgnoreCase(str, str2)) ? str : str.substring(str2.length());
    }

    public static String repeat(String str, int i) {
        if (str == null) {
            return null;
        }
        if (i <= 0) {
            return "";
        }
        int length = str.length();
        if (i == 1 || length == 0) {
            return str;
        }
        if (length == 1 && i <= 8192) {
            return padding(i, str.charAt(0));
        }
        int i2 = length * i;
        switch (length) {
            case 1:
                char charAt = str.charAt(0);
                char[] cArr = new char[i2];
                for (int i3 = i - 1; i3 >= 0; i3--) {
                    cArr[i3] = charAt;
                }
                return new String(cArr);
            case 2:
                char charAt2 = str.charAt(0);
                char charAt3 = str.charAt(1);
                char[] cArr2 = new char[i2];
                for (int i4 = (i * 2) - 2; i4 >= 0; i4 = (i4 - 1) - 1) {
                    cArr2[i4] = charAt2;
                    cArr2[i4 + 1] = charAt3;
                }
                return new String(cArr2);
            default:
                StringBuffer stringBuffer = new StringBuffer(i2);
                for (int i5 = 0; i5 < i; i5++) {
                    stringBuffer.append(str);
                }
                return stringBuffer.toString();
        }
    }

    public static String replace(String str, String str2, String str3) {
        return replace(str, str2, str3, -1);
    }

    public static String replace(String str, String str2, String str3, int i) {
        if (isEmpty(str) || isEmpty(str2) || str3 == null || i == 0) {
            return str;
        }
        int i2 = 0;
        int indexOf = str.indexOf(str2, 0);
        if (indexOf == -1) {
            return str;
        }
        int length = str2.length();
        int length2 = str3.length() - length;
        if (length2 < 0) {
            length2 = 0;
        }
        int i3 = 64;
        if (i < 0) {
            i3 = 16;
        } else if (i <= 64) {
            i3 = i;
        }
        StringBuffer stringBuffer = new StringBuffer(str.length() + (length2 * i3));
        while (indexOf != -1) {
            stringBuffer.append(str.substring(i2, indexOf));
            stringBuffer.append(str3);
            i2 = indexOf + length;
            i--;
            if (i == 0) {
                break;
            }
            indexOf = str.indexOf(str2, i2);
        }
        stringBuffer.append(str.substring(i2));
        return stringBuffer.toString();
    }

    public static String replaceChars(String str, char c, char c2) {
        if (str == null) {
            return null;
        }
        return str.replace(c, c2);
    }

    public static String replaceChars(String str, String str2, String str3) {
        if (isEmpty(str) || isEmpty(str2)) {
            return str;
        }
        if (str3 == null) {
            str3 = "";
        }
        int length = str3.length();
        int length2 = str.length();
        StringBuffer stringBuffer = new StringBuffer(length2);
        boolean z = false;
        for (int i = 0; i < length2; i++) {
            char charAt = str.charAt(i);
            int indexOf = str2.indexOf(charAt);
            if (indexOf >= 0) {
                if (indexOf < length) {
                    stringBuffer.append(str3.charAt(indexOf));
                }
                z = true;
            } else {
                stringBuffer.append(charAt);
            }
        }
        return z ? stringBuffer.toString() : str;
    }

    public static String replaceEach(String str, String[] strArr, String[] strArr2) {
        return replaceEach(str, strArr, strArr2, false, 0);
    }

    private static String replaceEach(String str, String[] strArr, String[] strArr2, boolean z, int i) {
        if (str == null || str.length() == 0 || strArr == null || strArr.length == 0 || strArr2 == null || strArr2.length == 0) {
            return str;
        }
        if (i < 0) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("TimeToLive of ");
            stringBuffer.append(i);
            stringBuffer.append(" is less than 0: ");
            stringBuffer.append(str);
            throw new IllegalStateException(stringBuffer.toString());
        }
        int length = strArr.length;
        int length2 = strArr2.length;
        if (length != length2) {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("Search and Replace array lengths don't match: ");
            stringBuffer2.append(length);
            stringBuffer2.append(" vs ");
            stringBuffer2.append(length2);
            throw new IllegalArgumentException(stringBuffer2.toString());
        }
        boolean[] zArr = new boolean[length];
        int i2 = -1;
        int i3 = -1;
        for (int i4 = 0; i4 < length; i4++) {
            if (!zArr[i4] && strArr[i4] != null && strArr[i4].length() != 0 && strArr2[i4] != null) {
                int indexOf = str.indexOf(strArr[i4]);
                if (indexOf == -1) {
                    zArr[i4] = true;
                } else if (i2 == -1 || indexOf < i2) {
                    i3 = i4;
                    i2 = indexOf;
                }
            }
        }
        if (i2 == -1) {
            return str;
        }
        int i5 = 0;
        for (int i6 = 0; i6 < strArr.length; i6++) {
            int length3 = strArr2[i6].length() - strArr[i6].length();
            if (length3 > 0) {
                i5 += length3 * 3;
            }
        }
        StringBuffer stringBuffer3 = new StringBuffer(str.length() + Math.min(i5, str.length() / 5));
        int i7 = 0;
        while (i2 != -1) {
            while (i7 < i2) {
                stringBuffer3.append(str.charAt(i7));
                i7++;
            }
            stringBuffer3.append(strArr2[i3]);
            i7 = strArr[i3].length() + i2;
            int i8 = -1;
            int i9 = -1;
            for (int i10 = 0; i10 < length; i10++) {
                if (!zArr[i10] && strArr[i10] != null && strArr[i10].length() != 0 && strArr2[i10] != null) {
                    int indexOf2 = str.indexOf(strArr[i10], i7);
                    if (indexOf2 == -1) {
                        zArr[i10] = true;
                    } else if (i8 == -1 || indexOf2 < i8) {
                        i9 = i10;
                        i8 = indexOf2;
                    }
                }
            }
            i2 = i8;
            i3 = i9;
        }
        int length4 = str.length();
        while (i7 < length4) {
            stringBuffer3.append(str.charAt(i7));
            i7++;
        }
        String stringBuffer4 = stringBuffer3.toString();
        return !z ? stringBuffer4 : replaceEach(stringBuffer4, strArr, strArr2, z, i - 1);
    }

    public static String replaceEachRepeatedly(String str, String[] strArr, String[] strArr2) {
        return replaceEach(str, strArr, strArr2, true, strArr == null ? 0 : strArr.length);
    }

    public static String replaceOnce(String str, String str2, String str3) {
        return replace(str, str2, str3, 1);
    }

    public static String reverse(String str) {
        if (str == null) {
            return null;
        }
        return new StringBuffer(str).reverse().toString();
    }

    public static String reverseDelimited(String str, char c) {
        if (str == null) {
            return null;
        }
        String[] split = split(str, c);
        ArrayUtils.reverse(split);
        return join(split, c);
    }

    public static String reverseDelimitedString(String str, String str2) {
        if (str == null) {
            return null;
        }
        String[] split = split(str, str2);
        ArrayUtils.reverse(split);
        return str2 == null ? join((Object[]) split, ' ') : join(split, str2);
    }

    public static String right(String str, int i) {
        if (str == null) {
            return null;
        }
        return i < 0 ? "" : str.length() <= i ? str : str.substring(str.length() - i);
    }

    public static String rightPad(String str, int i) {
        return rightPad(str, i, ' ');
    }

    public static String rightPad(String str, int i, char c) {
        if (str == null) {
            return null;
        }
        int length = i - str.length();
        return length <= 0 ? str : length > 8192 ? rightPad(str, i, String.valueOf(c)) : str.concat(padding(length, c));
    }

    public static String rightPad(String str, int i, String str2) {
        if (str == null) {
            return null;
        }
        if (isEmpty(str2)) {
            str2 = SQLBuilder.BLANK;
        }
        int length = str2.length();
        int length2 = i - str.length();
        if (length2 <= 0) {
            return str;
        }
        if (length == 1 && length2 <= 8192) {
            return rightPad(str, i, str2.charAt(0));
        }
        if (length2 == length) {
            return str.concat(str2);
        }
        if (length2 < length) {
            return str.concat(str2.substring(0, length2));
        }
        char[] cArr = new char[length2];
        char[] charArray = str2.toCharArray();
        for (int i2 = 0; i2 < length2; i2++) {
            cArr[i2] = charArray[i2 % length];
        }
        return str.concat(new String(cArr));
    }

    public static String[] split(String str) {
        return split(str, null, -1);
    }

    public static String[] split(String str, char c) {
        return splitWorker(str, c, false);
    }

    public static String[] split(String str, String str2) {
        return splitWorker(str, str2, -1, false);
    }

    public static String[] split(String str, String str2, int i) {
        return splitWorker(str, str2, i, false);
    }

    public static String[] splitByCharacterType(String str) {
        return splitByCharacterType(str, false);
    }

    private static String[] splitByCharacterType(String str, boolean z) {
        int i;
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        char[] charArray = str.toCharArray();
        ArrayList arrayList = new ArrayList();
        int type = Character.getType(charArray[0]);
        int i2 = 0;
        for (int i3 = 1; i3 < charArray.length; i3++) {
            int type2 = Character.getType(charArray[i3]);
            if (type2 != type) {
                if (z && type2 == 2 && type == 1) {
                    i = i3 - 1;
                    if (i != i2) {
                        arrayList.add(new String(charArray, i2, i - i2));
                    } else {
                        i = i2;
                    }
                } else {
                    arrayList.add(new String(charArray, i2, i3 - i2));
                    i = i3;
                }
                i2 = i;
                type = type2;
            }
        }
        arrayList.add(new String(charArray, i2, charArray.length - i2));
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public static String[] splitByCharacterTypeCamelCase(String str) {
        return splitByCharacterType(str, true);
    }

    public static String[] splitByWholeSeparator(String str, String str2) {
        return splitByWholeSeparatorWorker(str, str2, -1, false);
    }

    public static String[] splitByWholeSeparator(String str, String str2, int i) {
        return splitByWholeSeparatorWorker(str, str2, i, false);
    }

    public static String[] splitByWholeSeparatorPreserveAllTokens(String str, String str2) {
        return splitByWholeSeparatorWorker(str, str2, -1, true);
    }

    public static String[] splitByWholeSeparatorPreserveAllTokens(String str, String str2, int i) {
        return splitByWholeSeparatorWorker(str, str2, i, true);
    }

    private static String[] splitByWholeSeparatorWorker(String str, String str2, int i, boolean z) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (length == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        if (str2 == null || "".equals(str2)) {
            return splitWorker(str, null, i, z);
        }
        int length2 = str2.length();
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (i2 < length) {
            i2 = str.indexOf(str2, i3);
            if (i2 <= -1) {
                arrayList.add(str.substring(i3));
            } else if (i2 > i3) {
                i4++;
                if (i4 == i) {
                    arrayList.add(str.substring(i3));
                } else {
                    arrayList.add(str.substring(i3, i2));
                    i3 = i2 + length2;
                }
            } else {
                if (z) {
                    i4++;
                    if (i4 == i) {
                        arrayList.add(str.substring(i3));
                        i2 = length;
                    } else {
                        arrayList.add("");
                    }
                }
                i3 = i2 + length2;
            }
            i2 = length;
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public static String[] splitPreserveAllTokens(String str) {
        return splitWorker(str, null, -1, true);
    }

    public static String[] splitPreserveAllTokens(String str, char c) {
        return splitWorker(str, c, true);
    }

    public static String[] splitPreserveAllTokens(String str, String str2) {
        return splitWorker(str, str2, -1, true);
    }

    public static String[] splitPreserveAllTokens(String str, String str2, int i) {
        return splitWorker(str, str2, i, true);
    }

    private static String[] splitWorker(String str, char c, boolean z) {
        boolean z2;
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (length == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        ArrayList arrayList = new ArrayList();
        int i = 0;
        boolean z3 = false;
        int i2 = 0;
        loop0: while (true) {
            z2 = false;
            while (i < length) {
                if (str.charAt(i) == c) {
                    if (z3 || z) {
                        arrayList.add(str.substring(i2, i));
                        z3 = false;
                        z2 = true;
                    }
                    i2 = i + 1;
                    i = i2;
                }
            }
            i++;
            z3 = true;
        }
        if (z3 || (z && z2)) {
            arrayList.add(str.substring(i2, i));
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    private static String[] splitWorker(String str, String str2, int i, boolean z) {
        int i2;
        int i3;
        boolean z2;
        boolean z3;
        boolean z4;
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (length == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        ArrayList arrayList = new ArrayList();
        if (str2 == null) {
            int i4 = 0;
            boolean z5 = false;
            int i5 = 1;
            int i6 = 0;
            loop0: while (true) {
                z3 = false;
                while (i4 < length) {
                    if (Character.isWhitespace(str.charAt(i4))) {
                        if (z5 || z) {
                            int i7 = i5 + 1;
                            if (i5 == i) {
                                i4 = length;
                                z4 = false;
                            } else {
                                z4 = true;
                            }
                            arrayList.add(str.substring(i6, i4));
                            z3 = z4;
                            i5 = i7;
                            z5 = false;
                        }
                        i6 = i4 + 1;
                        i4 = i6;
                    }
                }
                i4++;
                z5 = true;
            }
            i3 = i6;
            z2 = z5;
            i2 = i4;
        } else if (str2.length() == 1) {
            char charAt = str2.charAt(0);
            i2 = 0;
            i3 = 0;
            z2 = false;
            z3 = false;
            int i8 = 1;
            while (i2 < length) {
                if (str.charAt(i2) == charAt) {
                    if (z2 || z) {
                        int i9 = i8 + 1;
                        if (i8 == i) {
                            i2 = length;
                            z3 = false;
                        } else {
                            z3 = true;
                        }
                        arrayList.add(str.substring(i3, i2));
                        i8 = i9;
                        z2 = false;
                    }
                    i3 = i2 + 1;
                    i2 = i3;
                } else {
                    i2++;
                    z2 = true;
                    z3 = false;
                }
            }
        } else {
            i2 = 0;
            i3 = 0;
            z2 = false;
            z3 = false;
            int i10 = 1;
            while (i2 < length) {
                if (str2.indexOf(str.charAt(i2)) >= 0) {
                    if (z2 || z) {
                        int i11 = i10 + 1;
                        if (i10 == i) {
                            i2 = length;
                            z3 = false;
                        } else {
                            z3 = true;
                        }
                        arrayList.add(str.substring(i3, i2));
                        i10 = i11;
                        z2 = false;
                    }
                    i3 = i2 + 1;
                    i2 = i3;
                } else {
                    i2++;
                    z2 = true;
                    z3 = false;
                }
            }
        }
        if (z2 || (z && z3)) {
            arrayList.add(str.substring(i3, i2));
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public static boolean startsWith(String str, String str2) {
        return startsWith(str, str2, false);
    }

    private static boolean startsWith(String str, String str2, boolean z) {
        if (str == null || str2 == null) {
            return str == null && str2 == null;
        }
        if (str2.length() > str.length()) {
            return false;
        }
        return str.regionMatches(z, 0, str2, 0, str2.length());
    }

    public static boolean startsWithIgnoreCase(String str, String str2) {
        return startsWith(str, str2, true);
    }

    public static String strip(String str) {
        return strip(str, null);
    }

    public static String strip(String str, String str2) {
        return isEmpty(str) ? str : stripEnd(stripStart(str, str2), str2);
    }

    public static String[] stripAll(String[] strArr) {
        return stripAll(strArr, null);
    }

    public static String[] stripAll(String[] strArr, String str) {
        int length;
        if (strArr == null || (length = strArr.length) == 0) {
            return strArr;
        }
        String[] strArr2 = new String[length];
        for (int i = 0; i < length; i++) {
            strArr2[i] = strip(strArr[i], str);
        }
        return strArr2;
    }

    public static String stripEnd(String str, String str2) {
        int length;
        if (str == null || (length = str.length()) == 0) {
            return str;
        }
        if (str2 == null) {
            while (length != 0 && Character.isWhitespace(str.charAt(length - 1))) {
                length--;
            }
        } else {
            if (str2.length() == 0) {
                return str;
            }
            while (length != 0 && str2.indexOf(str.charAt(length - 1)) != -1) {
                length--;
            }
        }
        return str.substring(0, length);
    }

    public static String stripStart(String str, String str2) {
        int length;
        if (str == null || (length = str.length()) == 0) {
            return str;
        }
        int i = 0;
        if (str2 == null) {
            while (i != length && Character.isWhitespace(str.charAt(i))) {
                i++;
            }
        } else {
            if (str2.length() == 0) {
                return str;
            }
            while (i != length && str2.indexOf(str.charAt(i)) != -1) {
                i++;
            }
        }
        return str.substring(i);
    }

    public static String stripToEmpty(String str) {
        return str == null ? "" : strip(str, null);
    }

    public static String stripToNull(String str) {
        if (str == null) {
            return null;
        }
        String strip = strip(str, null);
        if (strip.length() == 0) {
            return null;
        }
        return strip;
    }

    public static String substring(String str, int i) {
        if (str == null) {
            return null;
        }
        if (i < 0) {
            i += str.length();
        }
        if (i < 0) {
            i = 0;
        }
        return i > str.length() ? "" : str.substring(i);
    }

    public static String substring(String str, int i, int i2) {
        if (str == null) {
            return null;
        }
        if (i2 < 0) {
            i2 += str.length();
        }
        if (i < 0) {
            i += str.length();
        }
        if (i2 > str.length()) {
            i2 = str.length();
        }
        if (i > i2) {
            return "";
        }
        if (i < 0) {
            i = 0;
        }
        if (i2 < 0) {
            i2 = 0;
        }
        return str.substring(i, i2);
    }

    public static String substringAfter(String str, String str2) {
        int indexOf;
        return isEmpty(str) ? str : (str2 == null || (indexOf = str.indexOf(str2)) == -1) ? "" : str.substring(indexOf + str2.length());
    }

    public static String substringAfterLast(String str, String str2) {
        int lastIndexOf;
        return isEmpty(str) ? str : (isEmpty(str2) || (lastIndexOf = str.lastIndexOf(str2)) == -1 || lastIndexOf == str.length() - str2.length()) ? "" : str.substring(lastIndexOf + str2.length());
    }

    public static String substringBefore(String str, String str2) {
        if (isEmpty(str) || str2 == null) {
            return str;
        }
        if (str2.length() == 0) {
            return "";
        }
        int indexOf = str.indexOf(str2);
        return indexOf == -1 ? str : str.substring(0, indexOf);
    }

    public static String substringBeforeLast(String str, String str2) {
        int lastIndexOf;
        return (isEmpty(str) || isEmpty(str2) || (lastIndexOf = str.lastIndexOf(str2)) == -1) ? str : str.substring(0, lastIndexOf);
    }

    public static String substringBetween(String str, String str2) {
        return substringBetween(str, str2, str2);
    }

    public static String substringBetween(String str, String str2, String str3) {
        int indexOf;
        int indexOf2;
        if (str == null || str2 == null || str3 == null || (indexOf = str.indexOf(str2)) == -1 || (indexOf2 = str.indexOf(str3, str2.length() + indexOf)) == -1) {
            return null;
        }
        return str.substring(indexOf + str2.length(), indexOf2);
    }

    public static String[] substringsBetween(String str, String str2, String str3) {
        int indexOf;
        int i;
        int indexOf2;
        if (str == null || isEmpty(str2) || isEmpty(str3)) {
            return null;
        }
        int length = str.length();
        if (length == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        int length2 = str3.length();
        int length3 = str2.length();
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        while (i2 < length - length2 && (indexOf = str.indexOf(str2, i2)) >= 0 && (indexOf2 = str.indexOf(str3, (i = indexOf + length3))) >= 0) {
            arrayList.add(str.substring(i, indexOf2));
            i2 = indexOf2 + length2;
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public static String swapCase(String str) {
        int length;
        if (str == null || (length = str.length()) == 0) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer(length);
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if (Character.isUpperCase(charAt)) {
                charAt = Character.toLowerCase(charAt);
            } else if (Character.isTitleCase(charAt)) {
                charAt = Character.toLowerCase(charAt);
            } else if (Character.isLowerCase(charAt)) {
                charAt = Character.toUpperCase(charAt);
            }
            stringBuffer.append(charAt);
        }
        return stringBuffer.toString();
    }

    public static String trim(String str) {
        if (str == null) {
            return null;
        }
        return str.trim();
    }

    public static String trimToEmpty(String str) {
        return str == null ? "" : str.trim();
    }

    public static String trimToNull(String str) {
        String trim = trim(str);
        if (isEmpty(trim)) {
            return null;
        }
        return trim;
    }

    public static String uncapitalise(String str) {
        return uncapitalize(str);
    }

    public static String uncapitalize(String str) {
        int length;
        if (str == null || (length = str.length()) == 0) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer(length);
        stringBuffer.append(Character.toLowerCase(str.charAt(0)));
        stringBuffer.append(str.substring(1));
        return stringBuffer.toString();
    }

    public static String upperCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toUpperCase();
    }
}
