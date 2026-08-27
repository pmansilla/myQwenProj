package com.sun.activation.registries;

import android.support.v4.os.EnvironmentCompat;

/* loaded from: classes2.dex */
public class MailcapTokenizer {
    public static final int EOI_TOKEN = 5;
    public static final int EQUALS_TOKEN = 61;
    public static final int SEMICOLON_TOKEN = 59;
    public static final int SLASH_TOKEN = 47;
    public static final int START_TOKEN = 1;
    public static final int STRING_TOKEN = 2;
    public static final int UNKNOWN_TOKEN = 0;
    private String data;
    private int dataLength;
    private int dataIndex = 0;
    private int currentToken = 1;
    private String currentTokenValue = "";
    private boolean isAutoquoting = false;
    private char autoquoteChar = ';';

    public MailcapTokenizer(String str) {
        this.data = str;
        this.dataLength = str.length();
    }

    private static String fixEscapeSequences(String str) {
        int length = str.length();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.ensureCapacity(length);
        int i = 0;
        while (i < length) {
            char charAt = str.charAt(i);
            if (charAt != '\\') {
                stringBuffer.append(charAt);
            } else if (i < length - 1) {
                i++;
                stringBuffer.append(str.charAt(i));
            } else {
                stringBuffer.append(charAt);
            }
            i++;
        }
        return stringBuffer.toString();
    }

    private static boolean isControlChar(char c) {
        return Character.isISOControl(c);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:7:0x000c. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:8:0x000f. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0017 A[FALL_THROUGH, ORIG_RETURN, RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static boolean isSpecialChar(char r1) {
        /*
            r0 = 34
            if (r1 == r0) goto L17
            r0 = 44
            if (r1 == r0) goto L17
            r0 = 47
            if (r1 == r0) goto L17
            switch(r1) {
                case 40: goto L17;
                case 41: goto L17;
                default: goto Lf;
            }
        Lf:
            switch(r1) {
                case 58: goto L17;
                case 59: goto L17;
                case 60: goto L17;
                case 61: goto L17;
                case 62: goto L17;
                case 63: goto L17;
                case 64: goto L17;
                default: goto L12;
            }
        L12:
            switch(r1) {
                case 91: goto L17;
                case 92: goto L17;
                case 93: goto L17;
                default: goto L15;
            }
        L15:
            r1 = 0
            goto L18
        L17:
            r1 = 1
        L18:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.activation.registries.MailcapTokenizer.isSpecialChar(char):boolean");
    }

    private static boolean isStringTokenChar(char c) {
        return (isSpecialChar(c) || isControlChar(c) || isWhiteSpaceChar(c)) ? false : true;
    }

    private static boolean isWhiteSpaceChar(char c) {
        return Character.isWhitespace(c);
    }

    public static String nameForToken(int i) {
        if (i == 5) {
            return "EOI";
        }
        if (i == 47) {
            return "'/'";
        }
        if (i == 59) {
            return "';'";
        }
        if (i == 61) {
            return "'='";
        }
        switch (i) {
            case 0:
                return EnvironmentCompat.MEDIA_UNKNOWN;
            case 1:
                return "start";
            case 2:
                return "string";
            default:
                return "really unknown";
        }
    }

    private void processAutoquoteToken() {
        int i = this.dataIndex;
        boolean z = false;
        while (this.dataIndex < this.dataLength && !z) {
            if (this.data.charAt(this.dataIndex) != this.autoquoteChar) {
                this.dataIndex++;
            } else {
                z = true;
            }
        }
        this.currentToken = 2;
        this.currentTokenValue = fixEscapeSequences(this.data.substring(i, this.dataIndex));
    }

    private void processStringToken() {
        int i = this.dataIndex;
        while (this.dataIndex < this.dataLength && isStringTokenChar(this.data.charAt(this.dataIndex))) {
            this.dataIndex++;
        }
        this.currentToken = 2;
        this.currentTokenValue = this.data.substring(i, this.dataIndex);
    }

    public int getCurrentToken() {
        return this.currentToken;
    }

    public String getCurrentTokenValue() {
        return this.currentTokenValue;
    }

    public int nextToken() {
        if (this.dataIndex < this.dataLength) {
            while (this.dataIndex < this.dataLength && isWhiteSpaceChar(this.data.charAt(this.dataIndex))) {
                this.dataIndex++;
            }
            if (this.dataIndex < this.dataLength) {
                char charAt = this.data.charAt(this.dataIndex);
                if (this.isAutoquoting) {
                    if (charAt == ';' || charAt == '=') {
                        this.currentToken = charAt;
                        this.currentTokenValue = new Character(charAt).toString();
                        this.dataIndex++;
                    } else {
                        processAutoquoteToken();
                    }
                } else if (isStringTokenChar(charAt)) {
                    processStringToken();
                } else if (charAt == '/' || charAt == ';' || charAt == '=') {
                    this.currentToken = charAt;
                    this.currentTokenValue = new Character(charAt).toString();
                    this.dataIndex++;
                } else {
                    this.currentToken = 0;
                    this.currentTokenValue = new Character(charAt).toString();
                    this.dataIndex++;
                }
            } else {
                this.currentToken = 5;
                this.currentTokenValue = null;
            }
        } else {
            this.currentToken = 5;
            this.currentTokenValue = null;
        }
        return this.currentToken;
    }

    public void setIsAutoquoting(boolean z) {
        this.isAutoquoting = z;
    }
}
