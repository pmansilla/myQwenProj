package basecamera.module.utils;

import com.litesuits.orm.db.assit.SQLBuilder;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import kotlin.text.Typography;

/* loaded from: classes.dex */
public class StringUtils {
    public static String abbreviate(String str, int i) {
        return abbreviate(str, 0, i);
    }

    public static String abbreviate(String str, int i, int i2) {
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
            return str.substring(0, i3) + "...";
        }
        if (i2 < 7) {
            throw new IllegalArgumentException("Minimum abbreviation width with offset is 7");
        }
        if (i + i3 < str.length()) {
            return "..." + abbreviate(str.substring(i), i3);
        }
        return "..." + str.substring(str.length() - i3);
    }

    public static String addAndDeHump(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (i != 0 && Character.isUpperCase(str.charAt(i))) {
                sb.append('-');
            }
            sb.append(str.charAt(i));
        }
        return sb.toString().trim().toLowerCase(Locale.ENGLISH);
    }

    public static String capitalise(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(str.length());
        sb.append(Character.toTitleCase(str.charAt(0)));
        sb.append(str.substring(1));
        return sb.toString();
    }

    public static String capitaliseAllWords(String str) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        StringBuilder sb = new StringBuilder(length);
        boolean z = true;
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if (Character.isWhitespace(charAt)) {
                sb.append(charAt);
                z = true;
            } else if (z) {
                sb.append(Character.toTitleCase(charAt));
                z = false;
            } else {
                sb.append(charAt);
            }
        }
        return sb.toString();
    }

    public static String capitalizeFirstLetter(String str) {
        return Character.toTitleCase(str.substring(0, 1).charAt(0)) + str.substring(1);
    }

    public static String center(String str, int i) {
        return center(str, i, SQLBuilder.BLANK);
    }

    public static String center(String str, int i, String str2) {
        int length = str.length();
        int i2 = i - length;
        return i2 < 1 ? str : rightPad(leftPad(str, length + (i2 / 2), str2), i, str2);
    }

    public static String chomp(String str) {
        return chomp(str, "\n");
    }

    public static String chomp(String str, String str2) {
        int lastIndexOf = str.lastIndexOf(str2);
        return lastIndexOf != -1 ? str.substring(0, lastIndexOf) : str;
    }

    public static String chompLast(String str) {
        return chompLast(str, "\n");
    }

    public static String chompLast(String str, String str2) {
        return (str.length() != 0 && str2.equals(str.substring(str.length() - str2.length()))) ? str.substring(0, str.length() - str2.length()) : str;
    }

    public static String chop(String str) {
        if ("".equals(str) || str.length() == 1) {
            return "";
        }
        int length = str.length() - 1;
        String substring = str.substring(0, length);
        if (str.charAt(length) == '\n') {
            int i = length - 1;
            if (substring.charAt(i) == '\r') {
                return substring.substring(0, i);
            }
        }
        return substring;
    }

    public static String chopNewline(String str) {
        int length = str.length() - 1;
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
        return join(objArr, "");
    }

    public static boolean contains(String str, char c) {
        return !isEmpty(str) && str.indexOf(c) >= 0;
    }

    public static boolean contains(String str, String str2) {
        return (str == null || str2 == null || str.indexOf(str2) < 0) ? false : true;
    }

    public static int countMatches(String str, String str2) {
        int i = 0;
        if (str2.equals("") || str == null) {
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

    public static String defaultString(Object obj) {
        return defaultString(obj, "");
    }

    public static String defaultString(Object obj, String str) {
        return obj == null ? str : obj.toString();
    }

    public static String deleteWhitespace(String str) {
        StringBuilder sb = new StringBuilder();
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                sb.append(str.charAt(i));
            }
        }
        return sb.toString();
    }

    public static String difference(String str, String str2) {
        int differenceAt = differenceAt(str, str2);
        return differenceAt == -1 ? "" : str2.substring(differenceAt);
    }

    public static int differenceAt(String str, String str2) {
        int i = 0;
        while (i < str.length() && i < str2.length() && str.charAt(i) == str2.charAt(i)) {
            i++;
        }
        if (i < str2.length() || i < str.length()) {
            return i;
        }
        return -1;
    }

    public static boolean equals(String str, String str2) {
        return str == null ? str2 == null : str.equals(str2);
    }

    public static boolean equalsIgnoreCase(String str, String str2) {
        return str == null ? str2 == null : str.equalsIgnoreCase(str2);
    }

    public static String escape(String str) {
        int length = str.length();
        StringBuilder sb = new StringBuilder(length * 2);
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if (charAt > 4095) {
                sb.append("\\u" + Integer.toHexString(charAt));
            } else if (charAt > 255) {
                sb.append("\\u0" + Integer.toHexString(charAt));
            } else if (charAt > 127) {
                sb.append("\\u00" + Integer.toHexString(charAt));
            } else if (charAt < ' ') {
                switch (charAt) {
                    case '\b':
                        sb.append('\\');
                        sb.append('b');
                        break;
                    case '\t':
                        sb.append('\\');
                        sb.append('t');
                        break;
                    case '\n':
                        sb.append('\\');
                        sb.append('n');
                        break;
                    case 11:
                    default:
                        if (charAt > 15) {
                            sb.append("\\u00" + Integer.toHexString(charAt));
                            break;
                        } else {
                            sb.append("\\u000" + Integer.toHexString(charAt));
                            break;
                        }
                    case '\f':
                        sb.append('\\');
                        sb.append('f');
                        break;
                    case '\r':
                        sb.append('\\');
                        sb.append('r');
                        break;
                }
            } else if (charAt == '\"') {
                sb.append('\\');
                sb.append(Typography.quote);
            } else if (charAt == '\'') {
                sb.append('\\');
                sb.append('\'');
            } else if (charAt != '\\') {
                sb.append(charAt);
            } else {
                sb.append('\\');
                sb.append('\\');
            }
        }
        return sb.toString();
    }

    public static String escape(String str, char[] cArr, char c) {
        if (str == null) {
            return null;
        }
        char[] cArr2 = new char[cArr.length];
        System.arraycopy(cArr, 0, cArr2, 0, cArr.length);
        Arrays.sort(cArr2);
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (Arrays.binarySearch(cArr2, charAt) > -1) {
                sb.append(c);
            }
            sb.append(charAt);
        }
        return sb.toString();
    }

    public static String getChomp(String str, String str2) {
        int lastIndexOf = str.lastIndexOf(str2);
        return lastIndexOf == str.length() - str2.length() ? str2 : lastIndexOf != -1 ? str.substring(lastIndexOf) : "";
    }

    public static String getNestedString(String str, String str2) {
        return getNestedString(str, str2, str2);
    }

    public static String getNestedString(String str, String str2, String str3) {
        int indexOf;
        int indexOf2;
        if (str == null || (indexOf = str.indexOf(str2)) == -1 || (indexOf2 = str.indexOf(str3, str2.length() + indexOf)) == -1) {
            return null;
        }
        return str.substring(indexOf + str2.length(), indexOf2);
    }

    public static String getPrechomp(String str, String str2) {
        int indexOf = str.indexOf(str2);
        return indexOf != -1 ? str.substring(0, indexOf + str2.length()) : "";
    }

    public static int indexOfAny(String str, String[] strArr) {
        if (str == null || strArr == null) {
            return -1;
        }
        int i = Integer.MAX_VALUE;
        for (String str2 : strArr) {
            int indexOf = str.indexOf(str2);
            if (indexOf != -1 && indexOf < i) {
                i = indexOf;
            }
        }
        if (i == Integer.MAX_VALUE) {
            return -1;
        }
        return i;
    }

    public static String interpolate(String str, Map map) {
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String obj = it.next().toString();
            Object obj2 = map.get(obj);
            if (obj2 == null) {
                throw new NullPointerException("The value of the key '" + obj + "' is null.");
            }
            String obj3 = obj2.toString();
            str = replace(str, "${" + obj + "}", obj3);
            if (obj.indexOf(SQLBuilder.BLANK) == -1) {
                str = replace(str, "$" + obj, obj3);
            }
        }
        return str;
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

    public static boolean isEmail(String str) {
        return Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$").matcher(str).matches();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isMobileNo(String str) {
        return Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$").matcher(str).matches();
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isNotEmpty(String str) {
        return str != null && str.trim().length() > 0;
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

    public static String join(Iterator it, String str) {
        if (str == null) {
            str = "";
        }
        StringBuilder sb = new StringBuilder(256);
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(str);
            }
        }
        return sb.toString();
    }

    public static String join(Object[] objArr, String str) {
        if (str == null) {
            str = "";
        }
        int length = objArr.length;
        StringBuilder sb = new StringBuilder(length == 0 ? 0 : (objArr[0].toString().length() + str.length()) * length);
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                sb.append(str);
            }
            sb.append(objArr[i]);
        }
        return sb.toString();
    }

    public static int lastIndexOfAny(String str, String[] strArr) {
        int i = -1;
        if (str == null || strArr == null) {
            return -1;
        }
        for (String str2 : strArr) {
            int lastIndexOf = str.lastIndexOf(str2);
            if (lastIndexOf > i) {
                i = lastIndexOf;
            }
        }
        return i;
    }

    public static String left(String str, int i) {
        if (i >= 0) {
            return (str == null || str.length() <= i) ? str : str.substring(0, i);
        }
        throw new IllegalArgumentException("Requested String length " + i + " is less than zero");
    }

    public static String leftPad(String str, int i) {
        return leftPad(str, i, SQLBuilder.BLANK);
    }

    public static String leftPad(String str, int i, String str2) {
        int length = (i - str.length()) / str2.length();
        if (length <= 0) {
            return str;
        }
        return repeat(str2, length) + str;
    }

    public static String lowerCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toLowerCase();
    }

    public static String lowercaseFirstLetter(String str) {
        return Character.toLowerCase(str.substring(0, 1).charAt(0)) + str.substring(1);
    }

    public static String mid(String str, int i, int i2) {
        if (i < 0 || (str != null && i > str.length())) {
            throw new StringIndexOutOfBoundsException("String index " + i + " is out of bounds");
        }
        if (i2 >= 0) {
            if (str == null) {
                return null;
            }
            int i3 = i2 + i;
            return str.length() <= i3 ? str.substring(i) : str.substring(i, i3);
        }
        throw new IllegalArgumentException("Requested String length " + i2 + " is less than zero");
    }

    public static String overlayString(String str, String str2, int i, int i2) {
        StringBuilder sb = new StringBuilder((((str2.length() + i) + str.length()) - i2) + 1);
        sb.append(str.substring(0, i));
        sb.append(str2);
        sb.append(str.substring(i2));
        return sb.toString();
    }

    public static String prechomp(String str, String str2) {
        int indexOf = str.indexOf(str2);
        return indexOf != -1 ? str.substring(indexOf + str2.length()) : str;
    }

    public static String quoteAndEscape(String str, char c) {
        return quoteAndEscape(str, c, new char[]{c}, new char[]{' '}, '\\', false);
    }

    public static String quoteAndEscape(String str, char c, char[] cArr) {
        return quoteAndEscape(str, c, new char[]{c}, cArr, '\\', false);
    }

    public static String quoteAndEscape(String str, char c, char[] cArr, char c2, boolean z) {
        return quoteAndEscape(str, c, cArr, new char[]{' '}, c2, z);
    }

    public static String quoteAndEscape(String str, char c, char[] cArr, char[] cArr2, char c2, boolean z) {
        if (str == null) {
            return null;
        }
        if (!z && str.startsWith(Character.toString(c)) && str.endsWith(Character.toString(c))) {
            return str;
        }
        String escape = escape(str, cArr, c2);
        boolean z2 = true;
        if (!z && escape.equals(str)) {
            int i = 0;
            while (true) {
                if (i >= cArr2.length) {
                    z2 = false;
                    break;
                }
                if (escape.indexOf(cArr2[i]) > -1) {
                    break;
                }
                i++;
            }
        }
        if (!z2) {
            return escape;
        }
        return c + escape + c;
    }

    public static String removeAndHump(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        StringTokenizer stringTokenizer = new StringTokenizer(str, str2);
        while (stringTokenizer.hasMoreTokens()) {
            sb.append(capitalizeFirstLetter((String) stringTokenizer.nextElement()));
        }
        return sb.toString();
    }

    public static String removeDuplicateWhitespace(String str) {
        StringBuilder sb = new StringBuilder();
        int length = str.length();
        int i = 0;
        boolean z = false;
        while (i < length) {
            char charAt = str.charAt(i);
            boolean isWhitespace = Character.isWhitespace(charAt);
            if (!z || !isWhitespace) {
                sb.append(charAt);
            }
            i++;
            z = isWhitespace;
        }
        return sb.toString();
    }

    public static String repeat(String str, int i) {
        StringBuilder sb = new StringBuilder(str.length() * i);
        for (int i2 = 0; i2 < i; i2++) {
            sb.append(str);
        }
        return sb.toString();
    }

    public static String replace(String str, char c, char c2) {
        return replace(str, c, c2, -1);
    }

    public static String replace(String str, char c, char c2, int i) {
        return replace(str, String.valueOf(c), String.valueOf(c2), i);
    }

    public static String replace(String str, String str2, String str3) {
        return replace(str, str2, str3, -1);
    }

    public static String replace(String str, String str2, String str3, int i) {
        if (str == null || str2 == null || str3 == null || str2.length() == 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str.length());
        int i2 = 0;
        do {
            int indexOf = str.indexOf(str2, i2);
            if (indexOf == -1) {
                break;
            }
            sb.append(str.substring(i2, indexOf));
            sb.append(str3);
            i2 = str2.length() + indexOf;
            i--;
        } while (i != 0);
        sb.append(str.substring(i2));
        return sb.toString();
    }

    public static String replaceOnce(String str, char c, char c2) {
        return replace(str, c, c2, 1);
    }

    public static String replaceOnce(String str, String str2, String str3) {
        return replace(str, str2, str3, 1);
    }

    public static String reverse(String str) {
        if (str == null) {
            return null;
        }
        return new StringBuilder(str).reverse().toString();
    }

    private static void reverseArray(Object[] objArr) {
        int length = objArr.length - 1;
        for (int i = 0; length > i; i++) {
            Object obj = objArr[length];
            objArr[length] = objArr[i];
            objArr[i] = obj;
            length--;
        }
    }

    public static String reverseDelimitedString(String str, String str2) {
        String[] split = split(str, str2);
        reverseArray(split);
        return join(split, str2);
    }

    public static String right(String str, int i) {
        if (i >= 0) {
            return (str == null || str.length() <= i) ? str : str.substring(str.length() - i);
        }
        throw new IllegalArgumentException("Requested String length " + i + " is less than zero");
    }

    public static String rightPad(String str, int i) {
        return rightPad(str, i, SQLBuilder.BLANK);
    }

    public static String rightPad(String str, int i, String str2) {
        int length = (i - str.length()) / str2.length();
        if (length <= 0) {
            return str;
        }
        return str + repeat(str2, length);
    }

    public static String[] split(String str) {
        return split(str, null, -1);
    }

    public static String[] split(String str, String str2) {
        return split(str, str2, -1);
    }

    public static String[] split(String str, String str2, int i) {
        StringTokenizer stringTokenizer = str2 == null ? new StringTokenizer(str) : new StringTokenizer(str, str2);
        int countTokens = stringTokenizer.countTokens();
        if (i > 0 && countTokens > i) {
            countTokens = i;
        }
        String[] strArr = new String[countTokens];
        int i2 = 0;
        int i3 = 0;
        while (true) {
            if (!stringTokenizer.hasMoreTokens()) {
                break;
            }
            if (i > 0 && i2 == countTokens - 1) {
                strArr[i2] = str.substring(str.indexOf(stringTokenizer.nextToken(), i3));
                break;
            }
            strArr[i2] = stringTokenizer.nextToken();
            i3 = str.indexOf(strArr[i2], i3) + strArr[i2].length();
            i2++;
        }
        return strArr;
    }

    public static String strip(String str) {
        return strip(str, null);
    }

    public static String strip(String str, String str2) {
        return stripEnd(stripStart(str, str2), str2);
    }

    public static String[] stripAll(String[] strArr) {
        return stripAll(strArr, null);
    }

    public static String[] stripAll(String[] strArr, String str) {
        if (strArr == null || strArr.length == 0) {
            return strArr;
        }
        int length = strArr.length;
        String[] strArr2 = new String[length];
        for (int i = 0; i < length; i++) {
            strArr2[i] = strip(strArr[i], str);
        }
        return strArr2;
    }

    public static String stripEnd(String str, String str2) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (str2 == null) {
            while (length != 0 && Character.isWhitespace(str.charAt(length - 1))) {
                length--;
            }
        } else {
            while (length != 0 && str2.indexOf(str.charAt(length - 1)) != -1) {
                length--;
            }
        }
        return str.substring(0, length);
    }

    public static String stripStart(String str, String str2) {
        if (str == null) {
            return null;
        }
        int i = 0;
        int length = str.length();
        if (str2 == null) {
            while (i != length && Character.isWhitespace(str.charAt(i))) {
                i++;
            }
        } else {
            while (i != length && str2.indexOf(str.charAt(i)) != -1) {
                i++;
            }
        }
        return str.substring(i);
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

    public static String swapCase(String str) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        StringBuilder sb = new StringBuilder(length);
        boolean z = false;
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            sb.append(Character.isUpperCase(charAt) ? Character.toLowerCase(charAt) : Character.isTitleCase(charAt) ? Character.toLowerCase(charAt) : Character.isLowerCase(charAt) ? z ? Character.toTitleCase(charAt) : Character.toUpperCase(charAt) : charAt);
            z = Character.isWhitespace(charAt);
        }
        return sb.toString();
    }

    public static String trim(String str) {
        if (str == null) {
            return null;
        }
        return str.trim();
    }

    public static String uncapitalise(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(str.length());
        sb.append(Character.toLowerCase(str.charAt(0)));
        sb.append(str.substring(1));
        return sb.toString();
    }

    public static String uncapitaliseAllWords(String str) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        StringBuilder sb = new StringBuilder(length);
        boolean z = true;
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if (Character.isWhitespace(charAt)) {
                sb.append(charAt);
                z = true;
            } else if (z) {
                sb.append(Character.toLowerCase(charAt));
                z = false;
            } else {
                sb.append(charAt);
            }
        }
        return sb.toString();
    }

    public static String unifyLineSeparators(String str) {
        return unifyLineSeparators(str, System.getProperty("line.separator"));
    }

    public static String unifyLineSeparators(String str, String str2) {
        if (str == null) {
            return null;
        }
        if (str2 == null) {
            str2 = System.getProperty("line.separator");
        }
        if (!str2.equals("\n") && !str2.equals("\r") && !str2.equals("\r\n")) {
            throw new IllegalArgumentException("Requested line separator is invalid.");
        }
        int length = str.length();
        StringBuilder sb = new StringBuilder(length);
        int i = 0;
        while (i < length) {
            if (str.charAt(i) == '\r') {
                int i2 = i + 1;
                if (i2 < length && str.charAt(i2) == '\n') {
                    i = i2;
                }
                sb.append(str2);
            } else if (str.charAt(i) == '\n') {
                sb.append(str2);
            } else {
                sb.append(str.charAt(i));
            }
            i++;
        }
        return sb.toString();
    }

    public static String upperCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toUpperCase();
    }
}
