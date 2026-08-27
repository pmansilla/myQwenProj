package org.apache.commons.lang.text;

import java.util.Arrays;
import kotlin.text.Typography;

/* loaded from: classes2.dex */
public abstract class StrMatcher {
    private static final StrMatcher COMMA_MATCHER = new CharMatcher(',');
    private static final StrMatcher TAB_MATCHER = new CharMatcher('\t');
    private static final StrMatcher SPACE_MATCHER = new CharMatcher(' ');
    private static final StrMatcher SPLIT_MATCHER = new CharSetMatcher(" \t\n\r\f".toCharArray());
    private static final StrMatcher TRIM_MATCHER = new TrimMatcher();
    private static final StrMatcher SINGLE_QUOTE_MATCHER = new CharMatcher('\'');
    private static final StrMatcher DOUBLE_QUOTE_MATCHER = new CharMatcher(Typography.quote);
    private static final StrMatcher QUOTE_MATCHER = new CharSetMatcher("'\"".toCharArray());
    private static final StrMatcher NONE_MATCHER = new NoMatcher();

    /* loaded from: classes2.dex */
    static final class CharMatcher extends StrMatcher {
        private char ch;

        CharMatcher(char c) {
            this.ch = c;
        }

        @Override // org.apache.commons.lang.text.StrMatcher
        public int isMatch(char[] cArr, int i, int i2, int i3) {
            return this.ch == cArr[i] ? 1 : 0;
        }
    }

    /* loaded from: classes2.dex */
    static final class CharSetMatcher extends StrMatcher {
        private char[] chars;

        CharSetMatcher(char[] cArr) {
            this.chars = (char[]) cArr.clone();
            Arrays.sort(this.chars);
        }

        @Override // org.apache.commons.lang.text.StrMatcher
        public int isMatch(char[] cArr, int i, int i2, int i3) {
            return Arrays.binarySearch(this.chars, cArr[i]) >= 0 ? 1 : 0;
        }
    }

    /* loaded from: classes2.dex */
    static final class NoMatcher extends StrMatcher {
        NoMatcher() {
        }

        @Override // org.apache.commons.lang.text.StrMatcher
        public int isMatch(char[] cArr, int i, int i2, int i3) {
            return 0;
        }
    }

    /* loaded from: classes2.dex */
    static final class StringMatcher extends StrMatcher {
        private char[] chars;

        StringMatcher(String str) {
            this.chars = str.toCharArray();
        }

        @Override // org.apache.commons.lang.text.StrMatcher
        public int isMatch(char[] cArr, int i, int i2, int i3) {
            int length = this.chars.length;
            if (i + length > i3) {
                return 0;
            }
            int i4 = i;
            int i5 = 0;
            while (i5 < this.chars.length) {
                if (this.chars[i5] != cArr[i4]) {
                    return 0;
                }
                i5++;
                i4++;
            }
            return length;
        }
    }

    /* loaded from: classes2.dex */
    static final class TrimMatcher extends StrMatcher {
        TrimMatcher() {
        }

        @Override // org.apache.commons.lang.text.StrMatcher
        public int isMatch(char[] cArr, int i, int i2, int i3) {
            return cArr[i] <= ' ' ? 1 : 0;
        }
    }

    protected StrMatcher() {
    }

    public static StrMatcher charMatcher(char c) {
        return new CharMatcher(c);
    }

    public static StrMatcher charSetMatcher(String str) {
        return (str == null || str.length() == 0) ? NONE_MATCHER : str.length() == 1 ? new CharMatcher(str.charAt(0)) : new CharSetMatcher(str.toCharArray());
    }

    public static StrMatcher charSetMatcher(char[] cArr) {
        return (cArr == null || cArr.length == 0) ? NONE_MATCHER : cArr.length == 1 ? new CharMatcher(cArr[0]) : new CharSetMatcher(cArr);
    }

    public static StrMatcher commaMatcher() {
        return COMMA_MATCHER;
    }

    public static StrMatcher doubleQuoteMatcher() {
        return DOUBLE_QUOTE_MATCHER;
    }

    public static StrMatcher noneMatcher() {
        return NONE_MATCHER;
    }

    public static StrMatcher quoteMatcher() {
        return QUOTE_MATCHER;
    }

    public static StrMatcher singleQuoteMatcher() {
        return SINGLE_QUOTE_MATCHER;
    }

    public static StrMatcher spaceMatcher() {
        return SPACE_MATCHER;
    }

    public static StrMatcher splitMatcher() {
        return SPLIT_MATCHER;
    }

    public static StrMatcher stringMatcher(String str) {
        return (str == null || str.length() == 0) ? NONE_MATCHER : new StringMatcher(str);
    }

    public static StrMatcher tabMatcher() {
        return TAB_MATCHER;
    }

    public static StrMatcher trimMatcher() {
        return TRIM_MATCHER;
    }

    public int isMatch(char[] cArr, int i) {
        return isMatch(cArr, i, 0, cArr.length);
    }

    public abstract int isMatch(char[] cArr, int i, int i2, int i3);
}
