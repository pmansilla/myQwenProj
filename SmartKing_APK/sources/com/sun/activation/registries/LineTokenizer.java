package com.sun.activation.registries;

import java.util.NoSuchElementException;
import java.util.Vector;

/* compiled from: MimeTypeFile.java */
/* loaded from: classes2.dex */
class LineTokenizer {
    private static final String singles = "=";
    private int maxPosition;
    private String str;
    private Vector stack = new Vector();
    private int currentPosition = 0;

    public LineTokenizer(String str) {
        this.str = str;
        this.maxPosition = str.length();
    }

    private void skipWhiteSpace() {
        while (this.currentPosition < this.maxPosition && Character.isWhitespace(this.str.charAt(this.currentPosition))) {
            this.currentPosition++;
        }
    }

    public boolean hasMoreTokens() {
        if (this.stack.size() > 0) {
            return true;
        }
        skipWhiteSpace();
        return this.currentPosition < this.maxPosition;
    }

    public String nextToken() {
        int size = this.stack.size();
        if (size > 0) {
            int i = size - 1;
            String str = (String) this.stack.elementAt(i);
            this.stack.removeElementAt(i);
            return str;
        }
        skipWhiteSpace();
        if (this.currentPosition >= this.maxPosition) {
            throw new NoSuchElementException();
        }
        int i2 = this.currentPosition;
        char charAt = this.str.charAt(i2);
        if (charAt == '\"') {
            this.currentPosition++;
            boolean z = false;
            while (this.currentPosition < this.maxPosition) {
                String str2 = this.str;
                int i3 = this.currentPosition;
                this.currentPosition = i3 + 1;
                char charAt2 = str2.charAt(i3);
                if (charAt2 == '\\') {
                    this.currentPosition++;
                    z = true;
                } else if (charAt2 == '\"') {
                    if (!z) {
                        return this.str.substring(i2 + 1, this.currentPosition - 1);
                    }
                    StringBuffer stringBuffer = new StringBuffer();
                    for (int i4 = i2 + 1; i4 < this.currentPosition - 1; i4++) {
                        char charAt3 = this.str.charAt(i4);
                        if (charAt3 != '\\') {
                            stringBuffer.append(charAt3);
                        }
                    }
                    return stringBuffer.toString();
                }
            }
        } else if (singles.indexOf(charAt) >= 0) {
            this.currentPosition++;
        } else {
            while (this.currentPosition < this.maxPosition && singles.indexOf(this.str.charAt(this.currentPosition)) < 0 && !Character.isWhitespace(this.str.charAt(this.currentPosition))) {
                this.currentPosition++;
            }
        }
        return this.str.substring(i2, this.currentPosition);
    }

    public void pushToken(String str) {
        this.stack.addElement(str);
    }
}
