package org.apache.commons.lang;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* loaded from: classes2.dex */
public class CharSet implements Serializable {
    private static final long serialVersionUID = 5947847346149275958L;
    private Set set = new HashSet();
    public static final CharSet EMPTY = new CharSet((String) null);
    public static final CharSet ASCII_ALPHA = new CharSet("a-zA-Z");
    public static final CharSet ASCII_ALPHA_LOWER = new CharSet("a-z");
    public static final CharSet ASCII_ALPHA_UPPER = new CharSet("A-Z");
    public static final CharSet ASCII_NUMERIC = new CharSet("0-9");
    protected static final Map COMMON = new HashMap();

    static {
        COMMON.put(null, EMPTY);
        COMMON.put("", EMPTY);
        COMMON.put("a-zA-Z", ASCII_ALPHA);
        COMMON.put("A-Za-z", ASCII_ALPHA);
        COMMON.put("a-z", ASCII_ALPHA_LOWER);
        COMMON.put("A-Z", ASCII_ALPHA_UPPER);
        COMMON.put("0-9", ASCII_NUMERIC);
    }

    protected CharSet(String str) {
        add(str);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CharSet(String[] strArr) {
        for (String str : strArr) {
            add(str);
        }
    }

    public static CharSet getInstance(String str) {
        Object obj = COMMON.get(str);
        return obj != null ? (CharSet) obj : new CharSet(str);
    }

    public static CharSet getInstance(String[] strArr) {
        if (strArr == null) {
            return null;
        }
        return new CharSet(strArr);
    }

    protected void add(String str) {
        if (str == null) {
            return;
        }
        int length = str.length();
        int i = 0;
        while (i < length) {
            int i2 = length - i;
            if (i2 >= 4 && str.charAt(i) == '^' && str.charAt(i + 2) == '-') {
                this.set.add(new CharRange(str.charAt(i + 1), str.charAt(i + 3), true));
                i += 4;
            } else if (i2 >= 3 && str.charAt(i + 1) == '-') {
                this.set.add(new CharRange(str.charAt(i), str.charAt(i + 2)));
                i += 3;
            } else if (i2 < 2 || str.charAt(i) != '^') {
                this.set.add(new CharRange(str.charAt(i)));
                i++;
            } else {
                this.set.add(new CharRange(str.charAt(i + 1), true));
                i += 2;
            }
        }
    }

    public boolean contains(char c) {
        Iterator it = this.set.iterator();
        while (it.hasNext()) {
            if (((CharRange) it.next()).contains(c)) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof CharSet) {
            return this.set.equals(((CharSet) obj).set);
        }
        return false;
    }

    public CharRange[] getCharRanges() {
        return (CharRange[]) this.set.toArray(new CharRange[this.set.size()]);
    }

    public int hashCode() {
        return this.set.hashCode() + 89;
    }

    public String toString() {
        return this.set.toString();
    }
}
