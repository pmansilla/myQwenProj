package javax.mail.internet;

/* loaded from: classes2.dex */
public class HeaderTokenizer {
    private static final Token EOFToken = new Token(-4, null);
    public static final String MIME = "()<>@,;:\\\"\t []/?=";
    public static final String RFC822 = "()<>@,;:\\\"\t .[]";
    private int currentPos;
    private String delimiters;
    private int maxPos;
    private int nextPos;
    private int peekPos;
    private boolean skipComments;
    private String string;

    /* loaded from: classes2.dex */
    public static class Token {
        public static final int ATOM = -1;
        public static final int COMMENT = -3;
        public static final int EOF = -4;
        public static final int QUOTEDSTRING = -2;
        private int type;
        private String value;

        public Token(int i, String str) {
            this.type = i;
            this.value = str;
        }

        public int getType() {
            return this.type;
        }

        public String getValue() {
            return this.value;
        }
    }

    public HeaderTokenizer(String str) {
        this(str, RFC822);
    }

    public HeaderTokenizer(String str, String str2) {
        this(str, str2, true);
    }

    public HeaderTokenizer(String str, String str2, boolean z) {
        this.string = str == null ? "" : str;
        this.skipComments = z;
        this.delimiters = str2;
        this.peekPos = 0;
        this.nextPos = 0;
        this.currentPos = 0;
        this.maxPos = this.string.length();
    }

    private Token collectString(char c, boolean z) throws ParseException {
        int i = this.currentPos;
        boolean z2 = false;
        while (this.currentPos < this.maxPos) {
            char charAt = this.string.charAt(this.currentPos);
            if (charAt == '\\') {
                this.currentPos++;
            } else if (charAt != '\r') {
                if (charAt == c) {
                    this.currentPos++;
                    String filterToken = z2 ? filterToken(this.string, i, this.currentPos - 1, z) : this.string.substring(i, this.currentPos - 1);
                    if (charAt != '\"') {
                        filterToken = trimWhiteSpace(filterToken);
                        this.currentPos--;
                    }
                    return new Token(-2, filterToken);
                }
                this.currentPos++;
            }
            z2 = true;
            this.currentPos++;
        }
        if (c != '\"') {
            return new Token(-2, trimWhiteSpace(z2 ? filterToken(this.string, i, this.currentPos, z) : this.string.substring(i, this.currentPos)));
        }
        throw new ParseException("Unbalanced quoted string");
    }

    private static String filterToken(String str, int i, int i2, boolean z) {
        StringBuffer stringBuffer = new StringBuffer();
        boolean z2 = false;
        boolean z3 = false;
        while (i < i2) {
            char charAt = str.charAt(i);
            if (charAt != '\n' || !z2) {
                if (z3) {
                    if (z) {
                        stringBuffer.append('\\');
                    }
                    stringBuffer.append(charAt);
                    z2 = false;
                    z3 = false;
                } else if (charAt == '\\') {
                    z2 = false;
                    z3 = true;
                } else if (charAt == '\r') {
                    z2 = true;
                } else {
                    stringBuffer.append(charAt);
                }
                i++;
            }
            z2 = false;
            i++;
        }
        return stringBuffer.toString();
    }

    /* JADX WARN: Code restructure failed: missing block: B:78:0x00e1, code lost:
    
        if (r2 == r10) goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x00e3, code lost:
    
        r9.currentPos = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x00e9, code lost:
    
        return collectString(r10, r11);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private javax.mail.internet.HeaderTokenizer.Token getNext(char r10, boolean r11) throws javax.mail.internet.ParseException {
        /*
            Method dump skipped, instructions count: 278
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.mail.internet.HeaderTokenizer.getNext(char, boolean):javax.mail.internet.HeaderTokenizer$Token");
    }

    private int skipWhiteSpace() {
        while (this.currentPos < this.maxPos) {
            char charAt = this.string.charAt(this.currentPos);
            if (charAt != ' ' && charAt != '\t' && charAt != '\r' && charAt != '\n') {
                return this.currentPos;
            }
            this.currentPos++;
        }
        return -4;
    }

    private static String trimWhiteSpace(String str) {
        int length = str.length() - 1;
        while (length >= 0) {
            char charAt = str.charAt(length);
            if (charAt != ' ' && charAt != '\t' && charAt != '\r' && charAt != '\n') {
                break;
            }
            length--;
        }
        return length <= 0 ? "" : str.substring(0, length + 1);
    }

    public String getRemainder() {
        if (this.nextPos >= this.string.length()) {
            return null;
        }
        return this.string.substring(this.nextPos);
    }

    public Token next() throws ParseException {
        return next((char) 0, false);
    }

    public Token next(char c) throws ParseException {
        return next(c, false);
    }

    public Token next(char c, boolean z) throws ParseException {
        this.currentPos = this.nextPos;
        Token next = getNext(c, z);
        int i = this.currentPos;
        this.peekPos = i;
        this.nextPos = i;
        return next;
    }

    public Token peek() throws ParseException {
        this.currentPos = this.peekPos;
        Token next = getNext((char) 0, false);
        this.peekPos = this.currentPos;
        return next;
    }
}
